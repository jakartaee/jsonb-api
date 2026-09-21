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

package ee.jakarta.tck.json.bind.customizedmapping.records;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTransient;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @test
 * @sources RecordTransientCustomizationTest.java
 * @executeClass ee.jakarta.tck.json.bind.customizedmapping.records.RecordTransientCustomizationTest
 *
 * Tests covering all {@code @JsonbTransient} placement combinations on records:
 * record component accessor (serialization-only suppression) and the mutual-exclusivity
 * exception rules for record components and their accessors.
 */
public class RecordTransientCustomizationTest {

    private final Jsonb jsonb = JsonbBuilder.create();

    /**
     * Record with {@code @JsonbTransient} placed directly on the {@code password} component.
     * The annotated component must be excluded from both serialization and deserialization.
     */
    public record JsonbTransientComponentRecord(String user, @JsonbTransient String password) {
    }

    /*
     * @testName: testJsonbTransientOnRecordComponent
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.1.1-1
     *
     * @test_Strategy: Assert that a record component annotated with @JsonbTransient is
     * excluded from serialization output and that the corresponding JSON key is ignored
     * during deserialization, leaving the component null.
     */
    @Test
    public void testJsonbTransientOnRecordComponent() {
        String jsonString = jsonb.toJson(new JsonbTransientComponentRecord("alice", "secret"));
        assertThat("The non-transient component 'user' must appear in serialized output.",
                   jsonString, containsString("\"user\""));
        assertThat("The @JsonbTransient component 'password' must be absent from serialized output.",
                   jsonString, not(containsString("\"password\"")));

        JsonbTransientComponentRecord result = jsonb.fromJson(
                "{\"user\":\"alice\",\"password\":\"ignored\"}",
                JsonbTransientComponentRecord.class);
        assertThat("The non-transient component 'user' must be mapped from JSON.",
                   result.user(), is("alice"));
        assertNull(result.password(),
                   "The @JsonbTransient component 'password' must not be set during deserialization.");
    }


    // -------------------------------------------------------------------------
    // Record component accessor — serialization-only suppression
    // -------------------------------------------------------------------------

    /**
     * Record with {@code @JsonbTransient} placed on the redeclared component accessor for
     * {@code secret}. The annotation applies only to serialization, so {@code secret} must be
     * absent from the serialized output but must still be populated during deserialization.
     */
    public record TransientAccessorRecord(String user, String secret) {

        @JsonbTransient
        @Override
        public String secret() {
            return secret;
        }
    }

    /*
     * @testName: testTransientOnRecordComponentAccessorSuppressesSerializationOnly
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.1.1-1
     *
     * @test_Strategy: Assert that @JsonbTransient placed on a redeclared record component
     * accessor excludes the component from serialization output but does not prevent
     * the component from being populated during deserialization (the component name is
     * still matched from JSON).
     */
    @Test
    public void testTransientOnRecordComponentAccessorSuppressesSerializationOnly() {
        String jsonString = jsonb.toJson(new TransientAccessorRecord("alice", "s3cr3t"));
        assertThat("The non-transient component 'user' must appear in serialized output.",
                   jsonString, containsString("\"user\""));
        assertThat("The component whose accessor is @JsonbTransient must be absent from serialized output.",
                   jsonString, not(containsString("\"secret\"")));

        TransientAccessorRecord result = jsonb.fromJson("{\"user\":\"alice\",\"secret\":\"s3cr3t\"}",
                                                        TransientAccessorRecord.class);
        assertThat("The non-transient component 'user' must be mapped from JSON.",
                   result.user(), is("alice"));
        assertThat("The component whose accessor is @JsonbTransient must still be populated during deserialization.",
                   result.secret(), is("s3cr3t"));
    }

    // -------------------------------------------------------------------------
    // Mutual exclusivity — record component + other annotation
    // -------------------------------------------------------------------------

    /**
     * Record where both {@code @JsonbTransient} and {@code @JsonbProperty} are placed on the
     * same record component. This combination is illegal and must cause a
     * {@link jakarta.json.bind.JsonbException} during serialization or deserialization.
     */
    public record TransientComponentPlusAnnotationOnComponentRecord(
            String user,
            @JsonbTransient @JsonbProperty("pwd") String password) {
    }

    /*
     * @testName: testTransientOnComponentPlusAnnotationOnSameComponentThrows
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.1.1-2
     *
     * @test_Strategy: Assert that a JsonbException is thrown when @JsonbTransient and
     * another JSON Binding annotation are both placed on the same record component.
     */
    @Test
    public void testTransientOnComponentPlusAnnotationOnSameComponentThrows() {
        String message = "JsonbException not thrown for record component annotated with both "
                + "@JsonbTransient and another JSON Binding annotation.";
        assertThrows(JsonbException.class,
                     () -> jsonb.toJson(new TransientComponentPlusAnnotationOnComponentRecord("alice", "secret")),
                     message);
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{\"user\":\"alice\",\"pwd\":\"secret\"}",
                                          TransientComponentPlusAnnotationOnComponentRecord.class),
                     message);
    }

    /**
     * Record where {@code @JsonbTransient} is placed on the record component and
     * {@code @JsonbProperty} is placed on its redeclared accessor. This combination is illegal
     * and must cause a {@link jakarta.json.bind.JsonbException} during serialization or
     * deserialization.
     */
    public record TransientComponentPlusAnnotationOnAccessorRecord(String user, @JsonbTransient String password) {

        @JsonbProperty("pwd")
        @Override
        public String password() {
            return password;
        }
    }

    /*
     * @testName: testTransientOnComponentPlusAnnotationOnAccessorThrows
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.1.1-2
     *
     * @test_Strategy: Assert that a JsonbException is thrown when @JsonbTransient is placed
     * on a record component and another JSON Binding annotation is placed on its redeclared
     * accessor.
     */
    @Test
    public void testTransientOnComponentPlusAnnotationOnAccessorThrows() {
        String message = "JsonbException not thrown for record component annotated with @JsonbTransient "
                + "whose accessor carries another JSON Binding annotation.";
        assertThrows(JsonbException.class,
                     () -> jsonb.toJson(new TransientComponentPlusAnnotationOnAccessorRecord("alice", "secret")),
                     message);
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{\"user\":\"alice\",\"pwd\":\"secret\"}",
                                          TransientComponentPlusAnnotationOnAccessorRecord.class),
                     message);
    }

    // -------------------------------------------------------------------------
    // Mutual exclusivity — record component accessor + other annotation
    // -------------------------------------------------------------------------

    /**
     * Record where {@code @JsonbTransient} is placed on the redeclared accessor and
     * {@code @JsonbProperty} is placed on the corresponding record component. This
     * combination is illegal and must cause a {@link jakarta.json.bind.JsonbException}
     * during serialization or deserialization.
     */
    public record TransientAccessorPlusAnnotationOnComponentRecord(
            String user,
            @JsonbProperty("pwd") String password) {

        @JsonbTransient
        @Override
        public String password() {
            return password;
        }
    }

    /*
     * @testName: testTransientOnAccessorPlusAnnotationOnComponentThrows
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.1.1-2
     *
     * @test_Strategy: Assert that a JsonbException is thrown when @JsonbTransient is placed
     * on a redeclared record component accessor and another JSON Binding annotation is placed
     * on the corresponding record component.
     */
    @Test
    public void testTransientOnAccessorPlusAnnotationOnComponentThrows() {
        String message = "JsonbException not thrown for record component accessor annotated with "
                + "@JsonbTransient whose component carries another JSON Binding annotation.";
        assertThrows(JsonbException.class,
                     () -> jsonb.toJson(new TransientAccessorPlusAnnotationOnComponentRecord("alice", "secret")),
                     message);
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{\"user\":\"alice\",\"pwd\":\"secret\"}",
                                          TransientAccessorPlusAnnotationOnComponentRecord.class),
                     message);
    }

    /**
     * Record where both {@code @JsonbTransient} and {@code @JsonbProperty} are placed on the
     * same redeclared component accessor. This combination is illegal and must cause a
     * {@link jakarta.json.bind.JsonbException} during serialization or deserialization.
     */
    public record TransientAccessorPlusAnnotationOnAccessorRecord(String user, String password) {

        @JsonbTransient
        @JsonbProperty("pwd")
        @Override
        public String password() {
            return password;
        }
    }

    /*
     * @testName: testTransientOnAccessorPlusAnnotationOnSameAccessorThrows
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.1.1-2
     *
     * @test_Strategy: Assert that a JsonbException is thrown when @JsonbTransient and
     * another JSON Binding annotation are both placed on the same redeclared record
     * component accessor.
     */
    @Test
    public void testTransientOnAccessorPlusAnnotationOnSameAccessorThrows() {
        String message = "JsonbException not thrown for record component accessor annotated with both "
                + "@JsonbTransient and another JSON Binding annotation.";
        assertThrows(JsonbException.class,
                     () -> jsonb.toJson(new TransientAccessorPlusAnnotationOnAccessorRecord("alice", "secret")),
                     message);
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{\"user\":\"alice\",\"pwd\":\"secret\"}",
                                          TransientAccessorPlusAnnotationOnAccessorRecord.class),
                     message);
    }
}
