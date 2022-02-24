///////////////////////////////////////////////////////////////////////
NOTE TO WRITERS:
The following sections should be customized for the technology.
This text was originally from the JAX-RS TCK.  Most references
to JAX-RS have been parameterized to serve as a simple starting
point for customization.  There are still many details that will
need to be changed or removed.  The major sections 4.1, 4.2, and
4.3 should be preserved.  If their titles are changed, the links
at the top of config.adoc will need to be changed as well as well
as toc.adoc.
///////////////////////////////////////////////////////////////////////

[[GBFVU]][[configuring-your-environment-to-run-the-tck-against-the-reference-implementation]]

4.1 Configuring Your Environment to Run the TCK Against the Reference Implementation
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

After configuring your environment as described in this section,
continue with the instructions in link:using.html#GBFWO[Chapter 5, "Executing Tests."]


[NOTE]
=======================================================================

In these instructions, variables in angle brackets need to be expanded
for each platform. For example, `<JAVA_HOME>` becomes `$JAVA_HOME` on
Solaris/Linux and `%JAVA_HOME%` on Windows. In addition, the forward
slashes (`/`) used in all of the examples need to be replaced with
backslashes (`\`) for Windows. Finally, be sure to use the appropriate
separator for your operating system when specifying multiple path
entries (`;` on Windows, `:` on UNIX/Linux).

On Windows, you must escape any backslashes with an extra backslash in
path separators used in any of the following properties, or use forward
slashes as a path separator instead.

=======================================================================

1.  Set the following environment variables in your shell environment:
  a.  `JAVA_HOME` to the directory in which Java SE {SEversion} is installed
  b.  `M2_HOME` to the directory in which the Apache Maven build tool is installed.
  c.  +{TechnologyHomeEnv}+ to the directory in which the {TechnologyShortName}
  {TechnologyVersion} CI has been installed
  d.  `PATH` to include the following directories: `JAVA_HOME/bin`,
  and `M2_HOME/bin`

2.  Set below Maven/System properties
  a. `jimage.dir` This property specifies the directory where Java 11+ modules will be expanded by the
  jimage tool for use in sigTestClasspath
  b. `sigTestClasspath` This property must be set when running signature tests.
  This property should be set to a list of jar files and/or directories which contain your
  Jakarta EE and Java SE classes. Paths must be separated by the appropriate path separator (';' windows, ':' Unixes).
  c. `jsonb-impl.groupId` property is set to the Maven Group Id of the CI to test.
  d. `jsonb-impl.artifactId` property is set to the Maven Artifact Id of the CI to test.
  e. `jsonb-impl.version` property is set to the Maven Version of the CI to test.

3.  Set the below jars to the classpath
  a. JAR file for the {TechnologyShortName} {TechnologyVersion} CI. +
  `jakarta.json.bind-api.jar`.
  b. JUnit 5 jars ({JunitVersion})
  c. {TechnologyShortName} TCK tests (`jakarta.json.bind:jakarta.json.bind-tck`)

+
.Example: Maven configuration
[source,oac_no_warn,xml]
----
    <dependencies>
        <dependency>
          <groupId>CI.group.id</groupId>
          <artifactId>CI.artifact.id</artifactId>
          <version>CI.version</version>
          <scope>test</scope>
        </dependency>
        <dependency>
          <groupId>jakarta.json.bind</groupId>
          <artifactId>jakarta.json.bind-api</artifactId>
          <version>3.0.0</version>
          <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>jakarta.json</groupId>
            <artifactId>jakarta.json-api</artifactId>
            <version>2.1.0</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.jboss.weld.se</groupId>
            <artifactId>weld-se-core</artifactId>
            <version>5.0.0.Beta1</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>jakarta.json.bind</groupId>
            <artifactId>jakarta.json.bind-tck</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
----
+

[[GCLHU]][[configuring-your-environment-run-the-tck-against-the-vendor-implementation]]

4.2 Configuring Your Environment to Run the TCK Against the Vendor Implementation
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

After configuring your environment as described in this section,
continue with the instructions in link:using.html#GBFWO[Chapter 5, "Executing Tests."]


[NOTE]
=======================================================================

In these instructions, variables in angle brackets need to be expanded
for each platform. For example, `<JAVA_HOME>` becomes `$JAVA_HOME` on
Solaris/Linux and `%JAVA_HOME%` on Windows. In addition, the forward
slashes (`/`) used in all of the examples need to be replaced with
backslashes (`\`) for Windows. Finally, be sure to use the appropriate
separator for your operating system when specifying multiple path
entries (`;` on Windows, `:` on UNIX/Linux).

On Windows, you must escape any backslashes with an extra backslash in
path separators used in any of the following properties, or use forward
slashes as a path separator instead.

=======================================================================


1.  Set the following environment variables in your shell environment:
  a.  `JAVA_HOME` to the directory in which Java SE {SEversion} is installed
  b.  `M2_HOME` to the directory in which the Apache Maven build tool is installed.
  c.  +{TechnologyHomeEnv}+ to the directory in which the {TechnologyShortName}
  {TechnologyVersion} CI has been installed
  d.  `PATH` to include the following directories: `JAVA_HOME/bin`,
  and `M2_HOME/bin`

2.  Set below Maven/System properties
  a. `jimage.dir` This property specifies the directory where Java 11+ modules will be expanded by the
  jimage tool for use in sigTestClasspath
  b. `sigTestClasspath` This property must be set when running signature tests.
  This property should be set to a list of jar files and/or directories which contain your
  Java EE and Java SE classes. Paths must be separated by the appropriate path separator (';' windows, ':' Unixes).
  c. `jsonb-api.groupId` property is set to the Maven Group Id of the VI to test.
  d. `jsonb-api.artifactId` property is set to the Maven Artifact Id of the VI to test.
  e. `jsonb-api.version` property is set to the Maven Version of the VI to test.

3.  Set the below jars to the classpath
  a. JAR file for the {TechnologyShortName} {TechnologyVersion} VI. +
  `yasson.jar`.
  b. JUnit 5 jars ({JunitVersion})
  c. {TechnologyShortName} TCK tests (`jakarta.json.bind:jakarta.json.bind-tck`)

+
.Example: Maven configuration
[source,oac_no_warn,xml]
----
    <dependencies>
        <dependency>
          <groupId>VI.grou.id</groupId>
          <artifactId>VI.artefact.id</artifactId>
          <version>VI.version</version>
          <scope>test</scope>
        </dependency>
        <dependency>
          <groupId>org.eclipse</groupId>
          <artifactId>yasson</artifactId>
          <version>3.0.0-R1</version>
          <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>jakarta.json</groupId>
            <artifactId>jakarta.json-api</artifactId>
            <version>2.1.0</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.jboss.weld.se</groupId>
            <artifactId>weld-se-core</artifactId>
            <version>5.0.0.Beta1</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>jakarta.json.bind</groupId>
            <artifactId>jakarta.json.bind-tck</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
----
+

[[GHGDG]][[publishing-the-test-applications]]

4.3 Publishing the Test Applications
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Not needed for the {TechnologyShortName} TCK.
