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

package ee.jakarta.tck.json.bind.defaultmapping.records;

import java.util.Locale;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @test
 * @sources RecordDefaultMappingTest.java
 * @executeClass ee.jakarta.tck.json.bind.defaultmapping.records
 *
 * Tests for default serialization and default deserialization of Java records.
 */
public class RecordDefaultMappingTest {

    /** Simple two-component record used to verify declaration-order serialization and missing-field defaults. */
    public record Point(int x, int y) {}

    /** Two-component record with a reference-type component to verify null default on deserialization. */
    public record LabeledPoint(int x, String label) {}

    /** Record with a compact constructor that normalizes {@code value} to uppercase. */
    public record Tag(String value) {
        public Tag {
            value = value == null ? null : value.toUpperCase(Locale.ROOT);
        }
    }

    private final Jsonb jsonb = JsonbBuilder.create();

    /*
     * @testName: testSerializationPropertyOrder
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.1-5
     *
     * @test_Strategy: Assert that a Java record is serialized to a JSON object whose
     * property names match the record component names in declaration order.
     */
    @Test
    public void testSerializationPropertyOrder() {
        String jsonString = jsonb.toJson(new Point(1, 2));
        // Properties must appear in declaration order: x before y
        assertThat("Record components must be serialized in declaration order.",
                   jsonString, matchesPattern("\\{\\s*\"x\"\\s*:\\s*1\\s*,\\s*\"y\"\\s*:\\s*2\\s*}"));
    }

    /*
     * @testName: testDeserializationMissingPrimitiveFieldDefaultsToZero
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.1-5
     *
     * @test_Strategy: Assert that a record component absent from the JSON string
     * receives the default value for its primitive type.
     */
    @Test
    public void testDeserializationMissingPrimitiveFieldDefaultsToZero() {
        Point result = jsonb.fromJson("{\"x\":5}", Point.class);
        assertThat("Present component x must be mapped from JSON.", result.x(), is(5));
        assertThat("Absent primitive component y must default to 0.", result.y(), is(0));
    }

    /*
     * @testName: testDeserializationMissingReferenceFieldDefaultsToNull
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.1-5
     *
     * @test_Strategy: Assert that a record component of reference type that is absent
     * from the JSON string receives null.
     */
    @Test
    public void testDeserializationMissingReferenceFieldDefaultsToNull() {
        LabeledPoint result = jsonb.fromJson("{\"x\":5}", LabeledPoint.class);
        assertThat("Present component x must be mapped from JSON.", result.x(), is(5));
        assertNull(result.label(), "Absent reference component label must default to null.");
    }

    /*
     * @testName: testCompactConstructorNormalizationAppliedOnDeserialization
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.1-5
     *
     * @test_Strategy: Assert that a compact constructor's normalization logic is
     * executed during deserialization, because the compact constructor body is merged
     * into the canonical constructor at compile time.
     */
    @Test
    public void testCompactConstructorNormalizationAppliedOnDeserialization() {
        Tag result = jsonb.fromJson("{\"value\":\"hello\"}", Tag.class);
        assertThat("Compact constructor must normalize value to uppercase during deserialization.",
                   result.value(), is("HELLO"));
    }

}
