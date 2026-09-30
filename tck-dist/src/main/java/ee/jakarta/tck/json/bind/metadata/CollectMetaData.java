/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package ee.jakarta.tck.json.bind.metadata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Utility class that analyzes the TCK jar and generates documentation for:
 * <ol>
 *   <li>The names of tests that are disabled (due to challenges)</li>
 *   <li>The number of tests that need to pass for certification</li>
 *   <li>Expected JUnit output</li>
 *   <li>Expected signature test output</li>
 *   <li>The list of API packages covered by the TCK</li>
 * </ol>
 *
 * <p>This is run automatically each time the tck-dist module is built so the
 * generated documentation stays in sync with the test suite without manual
 * updates.</p>
 *
 * <p>Annotation metadata is read directly from {@code .class} bytecode using
 * ASM, so no test class is ever loaded into the JVM. This avoids static
 * initializer side-effects (e.g. {@code JsonbBuilder.create()}) and removes
 * any need for a Jsonb provider on the classpath.</p>
 *
 * <p>Files are output to:</p>
 * <pre>  ${project.basedir}/src/main/asciidoc/generated/</pre>
 *
 * <p>File names:</p>
 * <pre>
 *   expected-output.adoc
 *   expected-sig-output.adoc
 *   packages.adoc
 *   runtime-tests.adoc
 *   successful-challenges.adoc
 * </pre>
 */
public final class CollectMetaData {

    // Internal ASM descriptor strings for the JUnit annotations being scanned
    private static final String TEST_DESC     = "Lorg/junit/jupiter/api/Test;";
    private static final String DISABLED_DESC = "Lorg/junit/jupiter/api/Disabled;";

    // Framework package whose classes are helpers, not runnable tests
    private static final String FRAMEWORK_PACKAGE_PREFIX = "ee/jakarta/tck/json/bind/framework";

    private static final String RUNTIME_TESTS_FILE   = "runtime-tests.adoc";
    private static final String CHALLENGED_TESTS_FILE = "successful-challenges.adoc";
    private static final String SIG_OUTPUT_FILE       = "expected-sig-output.adoc";
    private static final String EXPECTED_OUTPUT_FILE  = "expected-output.adoc";
    private static final String PACKAGES_FILE         = "packages.adoc";

    // Data holders
    private static boolean debug = false;
    private static List<String> apiPackages;
    private static File adocGeneratedLocation;

    private CollectMetaData() {
        // Do nothing
    }

    /**
     * Main entry point. Expects exactly three arguments:
     * <ol>
     *   <li>{@code boolean} – enable debug output</li>
     *   <li>path to the TCK jar – used to collect metadata</li>
     *   <li>output directory – where generated pages should be written</li>
     * </ol>
     *
     * @param args the arguments needed to run
     * @throws Exception if any documentation cannot be generated
     */
    public static void main(final String[] args) throws Exception {
        if (args.length != 3) {
            throw new RuntimeException(
                    "CollectMetaData expected exactly 3 arguments [debug, path-to-tck, output-file-location]");
        }

        // Load arguments
        debug = Boolean.valueOf(args[0]);
        adocGeneratedLocation = new File(args[2]);

        // Ensure the output directory exists
        if (!adocGeneratedLocation.exists()) {
            adocGeneratedLocation.mkdirs();
        }

        // Scan bytecode directly — no class loading
        final List<TestMetaData> testMetaData = scanJar(args[1]);

        // Write the generated asciidoc files
        writeTestCounts(testMetaData, new File(adocGeneratedLocation, RUNTIME_TESTS_FILE));
        writeSuccessfulChallenges(testMetaData, new File(adocGeneratedLocation, CHALLENGED_TESTS_FILE));
        writeSigOutput(new File(adocGeneratedLocation, SIG_OUTPUT_FILE));
        writeOutput(testMetaData, new File(adocGeneratedLocation, EXPECTED_OUTPUT_FILE));
        writePackages(new File(adocGeneratedLocation, PACKAGES_FILE));
        writeGitIgnore(new File(adocGeneratedLocation, ".gitignore"),
                RUNTIME_TESTS_FILE, CHALLENGED_TESTS_FILE, SIG_OUTPUT_FILE, EXPECTED_OUTPUT_FILE, PACKAGES_FILE);

        for (TestMetaData data : testMetaData) {
            debug(data.debugString());
        }
    }

    // -------------------------------------------------------------------------
    // Bytecode scanning
    // -------------------------------------------------------------------------

    /**
     * Streams every {@code .class} entry in the TCK jar and uses ASM to read
     * annotation metadata directly from the bytecode. No class is loaded into
     * the JVM; static initializers are never executed.
     *
     * @param jarLocation path to the TCK jar
     * @return list of metadata for each {@code @Test}-annotated method
     * @throws Exception if the jar cannot be read
     */
    private static List<TestMetaData> scanJar(final String jarLocation) throws Exception {
        List<TestMetaData> results = new ArrayList<>();

        try (JarInputStream jar = new JarInputStream(new FileInputStream(jarLocation))) {
            for (JarEntry entry = jar.getNextJarEntry(); entry != null; entry = jar.getNextJarEntry()) {
                final String name = entry.getName();

                if (isTestClass(name)) {
                    debug("Scanning: " + name);
                    byte[] bytes = jar.readAllBytes();
                    results.addAll(scanClass(bytes));

                } else if (name.contains("sig-test-pkg-list.txt")) {
                    debug("Reading package list: " + name);
                    apiPackages = new String(jar.readAllBytes(), StandardCharsets.UTF_8).lines()
                            .filter(line -> !line.contains("#"))
                            .filter(line -> !line.isBlank())
                            .collect(Collectors.toList());
                    debug("apiPackages: " + apiPackages);
                }

                jar.closeEntry();
            }
        }

        return results;
    }

    /**
     * Uses ASM to visit a single {@code .class} file and collect
     * {@link TestMetaData} for every method annotated with {@code @Test}.
     *
     * @param classBytes raw bytecode of the class
     * @return list of test metadata found in this class (may be empty)
     */
    private static List<TestMetaData> scanClass(final byte[] classBytes) {
        List<TestMetaData> found = new ArrayList<>();

        ClassReader reader = new ClassReader(classBytes);
        reader.accept(new ClassVisitor(Opcodes.ASM9) {

            private String className;

            @Override
            public void visit(int version, int access, String name,
                              String signature, String superName, String[] interfaces) {
                // Convert internal name (slashes) to canonical name (dots)
                this.className = name.replace('/', '.');
            }

            @Override
            public MethodVisitor visitMethod(int access, String methodName,
                                             String descriptor, String signature, String[] exceptions) {
                return new MethodVisitor(Opcodes.ASM9) {

                    // Mutable holders filled by annotation visitors below
                    private boolean hasTest     = false;
                    private boolean hasDisabled = false;
                    private String  disabledValue = "";

                    @Override
                    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                        if (TEST_DESC.equals(desc)) {
                            hasTest = true;
                            return null;
                        }
                        if (DISABLED_DESC.equals(desc)) {
                            hasDisabled = true;
                            return new AnnotationVisitor(Opcodes.ASM9) {
                                @Override
                                public void visit(String attrName, Object value) {
                                    if ("value".equals(attrName)) disabledValue = (String) value;
                                }
                            };
                        }
                        return null;
                    }

                    @Override
                    public void visitEnd() {
                        if (hasTest) {
                            found.add(new TestMetaData(
                                    className,
                                    methodName,
                                    hasDisabled,
                                    parseDisabledLink(disabledValue),
                                    parseDisabledSince(disabledValue)));
                        }
                    }
                };
            }
        }, ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG);

        return found;
    }

    /**
     * Returns {@code true} when the jar entry is a test class that should be
     * scanned. A class qualifies when its simple name ends with {@code Test}
     * and it does not live under the framework helper package.
     *
     * @param entryName fully-qualified resource path inside the jar
     * @return {@code true} if the entry should be scanned
     */
    private static boolean isTestClass(final String entryName) {
        if (!entryName.endsWith(".class"))
            return false;
        if (entryName.contains(FRAMEWORK_PACKAGE_PREFIX))
            return false;

        String simpleName = entryName.substring(entryName.lastIndexOf('/') + 1, entryName.lastIndexOf('.'));
        return simpleName.endsWith("Test");
    }

    // -------------------------------------------------------------------------
    // @Disabled reason parsers
    // -------------------------------------------------------------------------

    /**
     * Extracts the {@code link:} segment from a structured {@code @Disabled}
     * reason string of the form {@code "link: <url> since: <version>"}.
     *
     * @param reason the full reason string, or empty
     * @return the link value, or empty string if not present
     */
    private static String parseDisabledLink(final String reason) {
        if (reason == null || reason.isBlank()) return "";
        int linkStart = reason.indexOf("link:");
        if (linkStart < 0) return "";
        int linkEnd = reason.indexOf("since:");
        String raw = linkEnd > linkStart
                ? reason.substring(linkStart + "link:".length(), linkEnd)
                : reason.substring(linkStart + "link:".length());
        return raw.strip();
    }

    /**
     * Extracts the {@code since:} segment from a structured {@code @Disabled}
     * reason string of the form {@code "link: <url> since: <version>"}.
     *
     * @param reason the full reason string, or empty
     * @return the version value, or empty string if not present
     */
    private static String parseDisabledSince(final String reason) {
        if (reason == null || reason.isBlank()) return "";
        int sinceStart = reason.indexOf("since:");
        if (sinceStart < 0) return "";
        return reason.substring(sinceStart + "since:".length()).strip();
    }

    // -------------------------------------------------------------------------
    // Asciidoc writers
    // -------------------------------------------------------------------------

    /**
     * Writes the names of generated files into a {@code .gitignore} so they
     * are never accidentally committed.
     *
     * @param outputLocation the {@code .gitignore} file to write
     * @param ignoredFiles   file names to ignore
     * @throws IOException if the file cannot be written
     */
    private static void writeGitIgnore(final File outputLocation, final String... ignoredFiles) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputLocation))) {
            for (String ignoredFile : ignoredFiles) {
                writer.write(ignoredFile + System.lineSeparator());
            }
        }
    }

    /**
     * Writes example Maven {@code test} output to the generated adoc folder.
     *
     * @param testMetaData   the test metadata previously collected
     * @param outputLocation the output file
     * @throws IOException if the file cannot be written
     */
    private static void writeOutput(final List<TestMetaData> testMetaData, final File outputLocation) throws IOException {
        String output =
                """
                [source, txt]
                ----
                $ mvn clean test
                ...
                [INFO] --- maven-surefire-plugin:x.x.x:test (default-test) @ tck.runner ---
                [INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
                [INFO]
                [INFO] -------------------------------------------------------
                [INFO]  T E S T S
                [INFO] -------------------------------------------------------
                $indiviualTests
                [INFO] Results:
                [INFO]
                $totalTests
                [INFO]
                [INFO] -------------------------------------------------------
                [INFO] BUILD SUCCESS
                [INFO] -------------------------------------------------------
                [INFO] Total time:  xx.xxx s
                [INFO] Finished at: yyyy-mm-ddThh:mm:ss.mmmm
                [INFO] -------------------------------------------------------
                ----"""
                .replaceAll("\\$indiviualTests", getIndividualTests(testMetaData))
                .replaceAll("\\$totalTests", getTotalTests(testMetaData));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputLocation))) {
            writer.write(output.trim() + System.lineSeparator());
        }
    }

    /**
     * Builds per-class test run lines for the expected-output block.
     *
     * @param testMetaData the full list of test metadata
     * @return formatted string of individual test class result lines
     */
    private static String getIndividualTests(final List<TestMetaData> testMetaData) {
        StringBuffer output = new StringBuffer();
        final String nl = System.lineSeparator();
        for (String testClass : testMetaData.stream()
                .map(TestMetaData::testClass)
                .distinct()
                .collect(Collectors.toList())) {

            List<TestMetaData> theseTests = testMetaData.stream()
                    .filter(m -> m.testClass().equals(testClass))
                    .collect(Collectors.toList());
            long testCount    = theseTests.stream().filter(m -> !m.isDisabled()).count();
            long disabledCount = theseTests.stream().filter(TestMetaData::isDisabled).count();

            output.append("[INFO] Running " + testClass + nl);
            if (disabledCount > 0) {
                output.append("[WARNING] Tests run: " + testCount + ", Failures: 0, Errors: 0, Skipped: " + disabledCount + ",");
            } else {
                output.append("[INFO] Tests run: " + testCount + ", Failures: 0, Errors: 0, Skipped: " + disabledCount + ",");
            }
            output.append("Time elapsed: y.yy s - in " + testClass + nl);
            output.append("[INFO]" + nl);
        }
        return output.toString().trim();
    }

    /**
     * Builds the summary totals line for the expected-output block.
     *
     * @param testMetaData the full list of test metadata
     * @return formatted totals line
     */
    private static String getTotalTests(final List<TestMetaData> testMetaData) {
        long totalTestCount    = testMetaData.stream().count();
        long totalDisabledCount = testMetaData.stream().filter(TestMetaData::isDisabled).count();

        if (totalDisabledCount > 0) {
            return "[WARNING] Tests run: " + totalTestCount + ", Failures: 0, Errors: 0, Skipped: " + totalDisabledCount;
        } else {
            return "[INFO] Tests run: " + totalTestCount + ", Failures: 0, Errors: 0, Skipped: " + totalDisabledCount;
        }
    }

    /**
     * Writes example signature test output to the generated adoc folder.
     *
     * @param outputLocation the output file
     * @throws IOException if the file cannot be written
     */
    private static void writeSigOutput(final File outputLocation) throws IOException {
        String output =
                """
                [source, txt]
                ----
                ******************************************************
                All package signatures passed.
                    Passed packages listed below:
                $packages
                ******************************************************
                ----""".replaceAll("\\$packages", getPackages());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputLocation))) {
            writer.write(output.trim() + System.lineSeparator());
        }
    }

    /**
     * Formats each API package as a pair of static/reflection mode lines.
     *
     * @return formatted package block
     */
    private static String getPackages() {
        String output = "";
        for (String apiPackage : apiPackages) {
            output +=
                    """
                    $package(static mode)
                    $package(reflection mode)
                    """.indent(8).replaceAll("\\$package", apiPackage);
        }
        return output;
    }

    /**
     * Writes the list of API packages to the generated adoc folder as an
     * AsciiDoc nested bullet list, matching the format:
     * <pre>
     *   ** `jakarta.json.bind`
     *   ** `jakarta.json.bind.adapter`
     * </pre>
     *
     * @param outputLocation the output file
     * @throws IOException if the file cannot be written
     */
    private static void writePackages(final File outputLocation) throws IOException {
        StringBuilder output = new StringBuilder();
        for (String apiPackage : apiPackages) {
            output.append("** `").append(apiPackage).append("`").append(System.lineSeparator());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputLocation))) {
            writer.write(output.toString().trim() + System.lineSeparator());
        }
    }

    /**
     * Writes disabled (challenged) test metadata to the generated adoc folder.
     *
     * @param testMetaData   the test metadata previously collected
     * @param outputLocation the output file
     * @throws IOException if the file cannot be written
     */
    private static void writeSuccessfulChallenges(final List<TestMetaData> testMetaData, final File outputLocation) throws IOException {
        String output =
                """
                |===
                |Class |Method |Link |Version
                $disabledTests
                |===""".replaceAll("\\$disabledTests", getDisabledTests(testMetaData));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputLocation))) {
            writer.write(output.trim() + System.lineSeparator());
        }
    }

    private static String getDisabledTests(final List<TestMetaData> testMetaData) {
        List<TestMetaData> disabledTests = testMetaData.stream().filter(TestMetaData::isDisabled).toList();
        String output = "";
        for (TestMetaData disabledTest : disabledTests) {
            output +=
                    """

                    |%s |%s |%s |%s
                    """.formatted(
                            disabledTest.testClass().substring(disabledTest.testClass().lastIndexOf('.') + 1),
                            disabledTest.testName(),
                            disabledTest.challengeIssue(),
                            disabledTest.challengeVersion());
        }
        return output;
    }

    /**
     * Writes test count totals to the generated adoc folder.
     * All JSON-B TCK tests apply to every implementation, so only the total
     * runnable count and skipped count are reported.
     *
     * @param testMetaData   the test metadata previously collected
     * @param outputLocation the output file
     * @throws IOException if the file cannot be written
     */
    private static void writeTestCounts(final List<TestMetaData> testMetaData, final File outputLocation) throws IOException {
        long total    = testMetaData.stream().count();
        long skipped  = testMetaData.stream().filter(TestMetaData::isDisabled).count();
        long runnable = total - skipped;

        String output =
                """
                |===
                |total |skipped

                |%d    |%d

                |===""".formatted(runnable, skipped);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputLocation))) {
            writer.write(output.trim() + System.lineSeparator());
        }
    }

    // -------------------------------------------------------------------------
    // Debug
    // -------------------------------------------------------------------------

    /**
     * Prints a message when debug mode is enabled.
     *
     * @param message the message to print
     */
    private static void debug(final String message) {
        if (debug)
            System.out.println(message);
    }

    // -------------------------------------------------------------------------
    // Data carrier
    // -------------------------------------------------------------------------

    /**
     * Immutable data carrier for a single test method.
     *
     * @param testClass        canonical name of the declaring test class
     * @param testName         name of the test method
     * @param isDisabled       {@code true} if the method also carries {@code @Disabled}
     * @param challengeIssue   the link parsed from the {@code @Disabled} reason, or empty
     * @param challengeVersion the version parsed from the {@code @Disabled} reason, or empty
     */
    public record TestMetaData(String testClass, String testName,
            boolean isDisabled, String challengeIssue, String challengeVersion) {

        boolean isRunnable() {
            return !isDisabled;
        }

        public String debugString() {
            return "TestMetaData [testName=" + testName + ", isDisabled=" + isDisabled + "]";
        }

        @Override
        public String toString() {
            return "TestMetaData [testName=" + testName
                    + ", isDisabled=" + isDisabled + ", challengeIssue=" + challengeIssue
                    + ", challengeVersion=" + challengeVersion + "]";
        }
    }
}
