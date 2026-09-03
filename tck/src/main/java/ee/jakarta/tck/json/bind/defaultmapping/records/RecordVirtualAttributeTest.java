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

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;

import org.junit.jupiter.api.Test;

import ee.jakarta.tck.json.bind.defaultmapping.records.model.BeanStyleVirtualAttributeRecord;
import ee.jakarta.tck.json.bind.defaultmapping.records.model.DateFormatVirtualAttributeRecord;
import ee.jakarta.tck.json.bind.defaultmapping.records.model.NillableTypeVirtualAttributeRecord;
import ee.jakarta.tck.json.bind.defaultmapping.records.model.NillableVirtualAttributeRecord;
import ee.jakarta.tck.json.bind.defaultmapping.records.model.NumberFormatVirtualAttributeRecord;
import ee.jakarta.tck.json.bind.defaultmapping.records.model.RenamedVirtualAttributeRecord;
import ee.jakarta.tck.json.bind.defaultmapping.records.model.TransientPlusAnnotationVirtualAttributeRecord;
import ee.jakarta.tck.json.bind.defaultmapping.records.model.TransientVirtualAttributeRecord;
import ee.jakarta.tck.json.bind.defaultmapping.records.model.VirtualAttributeRecord;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @test
 * @sources RecordVirtualAttributeTest.java
 * @executeClass ee.jakarta.tck.json.bind.defaultmapping.records
 *
 * Assert that record virtual attributes are serialized and are ignored during deserialization.
 */
public class RecordVirtualAttributeTest {

    private final Jsonb jsonb = JsonbBuilder.create();

    /*
     * @testName: testVirtualAttributeIncludedInSerialization
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7.1-1
     *
     * @test_Strategy: Serialize a record with a virtual attribute and assert that it is included
     * with its method name and in lexicographical property order.
     */
    @Test
    public void testVirtualAttributeIncludedInSerialization() {
        String jsonString = jsonb.toJson(new VirtualAttributeRecord("Jason", "Borne"));

        assertThat(jsonString, matchesPattern("\\{\\s*\"display\"\\s*:\\s*\"Jason Borne\"\\s*,\\s*"
                + "\"first\"\\s*:\\s*\"Jason\"\\s*,\\s*\"last\"\\s*:\\s*\"Borne\"\\s*}"));
    }

    /*
     * @testName: testVirtualAttributeIgnoredOnDeserialization
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7.1-2
     *
     * @test_Strategy: Deserialize JSON containing a virtual attribute and assert that the value
     * is ignored.
     */
    @Test
    public void testVirtualAttributeIgnoredOnDeserialization() {
        VirtualAttributeRecord result = jsonb.fromJson("{\"first\":\"A\",\"last\":\"B\",\"display\":\"ignored\"}",
                VirtualAttributeRecord.class);

        assertThat(result, is(new VirtualAttributeRecord("A", "B")));
    }

    /*
     * @testName: testVirtualAttributeSuppressedByJsonbTransient
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.1.1-1
     *
     * @test_Strategy: Serialize a record whose virtual attribute is annotated with JsonbTransient
     * and assert that the attribute is omitted.
     */
    @Test
    public void testVirtualAttributeSuppressedByJsonbTransient() {
        String jsonString = jsonb.toJson(new TransientVirtualAttributeRecord("Jason", "Borne"));

        assertThat(jsonString, matchesPattern("\\{\\s*\"first\"\\s*:\\s*\"Jason\"\\s*,\\s*\"last\"\\s*:\\s*\"Borne\"\\s*}"));
    }

    /*
     * @testName: testTransientPlusOtherAnnotationOnVirtualAttributeThrows
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.1.1-2
     *
     * @test_Strategy: Assert that a virtual attribute annotated with JsonbTransient and another
     * JSON Binding annotation causes serialization to fail.
     */
    @Test
    public void testTransientPlusOtherAnnotationOnVirtualAttributeThrows() {
        assertThrows(JsonbException.class, () -> jsonb.toJson(new TransientPlusAnnotationVirtualAttributeRecord("value")));
    }

    /*
     * @testName: testVirtualAttributeCustomNameViaJsonbProperty
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.1.2-1
     *
     * @test_Strategy: Serialize a record whose virtual attribute is annotated with JsonbProperty
     * and assert that its custom name is used.
     */
    @Test
    public void testVirtualAttributeCustomNameViaJsonbProperty() {
        String jsonString = jsonb.toJson(new RenamedVirtualAttributeRecord("Jason", "Borne"));

        assertThat(jsonString, matchesPattern("\\{\\s*\"first\"\\s*:\\s*\"Jason\"\\s*,\\s*\"fullName\"\\s*:\\s*"
                + "\"Jason Borne\"\\s*,\\s*\"last\"\\s*:\\s*\"Borne\"\\s*}"));
    }

    /*
     * @testName: testNullVirtualAttributeSerializedWithMethodLevelJsonbNillable
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.3.1-1
     *
     * @test_Strategy: Serialize a record whose null virtual attribute is annotated with
     * JsonbNillable and assert that the attribute is written as null.
     */
    @Test
    public void testNullVirtualAttributeSerializedWithMethodLevelJsonbNillable() {
        String jsonString = jsonb.toJson(new NillableVirtualAttributeRecord("value"));

        assertThat(jsonString, matchesPattern("\\{\\s*\"derived\"\\s*:\\s*null\\s*,\\s*\"value\"\\s*:\\s*\"value\"\\s*}"));
    }

    /*
     * @testName: testNullVirtualAttributeSerializedWithTypeLevelJsonbNillable
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.3.1-1
     *
     * @test_Strategy: Serialize a record annotated with JsonbNillable whose virtual attribute is
     * null and assert that the attribute is written as null.
     */
    @Test
    public void testNullVirtualAttributeSerializedWithTypeLevelJsonbNillable() {
        String jsonString = jsonb.toJson(new NillableTypeVirtualAttributeRecord("value"));

        assertThat(jsonString, matchesPattern("\\{\\s*\"derived\"\\s*:\\s*null\\s*,\\s*\"value\"\\s*:\\s*\"value\"\\s*}"));
    }

    /*
     * @testName: testDateFormatOnVirtualAttribute
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.8-1
     *
     * @test_Strategy: Serialize a date-valued virtual attribute with JsonbDateFormat and assert
     * that its configured format is used.
     */
    @Test
    public void testDateFormatOnVirtualAttribute() {
        ZonedDateTime timestamp = ZonedDateTime.of(2026, 3, 13, 12, 0, 0, 0, ZoneOffset.UTC);
        String jsonString = jsonb.toJson(new DateFormatVirtualAttributeRecord(timestamp));

        assertThat(jsonString, matchesPattern("\\{\\s*\"formatted\"\\s*:\\s*\"13-03-2026\"\\s*,\\s*\"timestamp\"\\s*:\\s*"
                + "\"[^\"]+\"\\s*}"));
    }

    /*
     * @testName: testNumberFormatOnVirtualAttribute
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.9-1
     *
     * @test_Strategy: Serialize a number-valued virtual attribute with JsonbNumberFormat and
     * assert that its configured format is used.
     */
    @Test
    public void testNumberFormatOnVirtualAttribute() {
        String jsonString = jsonb.toJson(new NumberFormatVirtualAttributeRecord(1234.56));

        assertThat(jsonString, matchesPattern("\\{\\s*\"amount\"\\s*:\\s*1234\\.56\\s*,\\s*\"formatted\"\\s*:\\s*"
                + "\"1,234\\.56\"\\s*}"));
    }

    /*
     * @testName: testBeanStyleVirtualAttributeNameNotStripped
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7.1-1
     *
     * @test_Strategy: Serialize a record whose virtual attribute method name begins with "get"
     * and assert that the "get" prefix is NOT stripped from the serialized property name,
     * i.e. the full method name is used as the component name.
     */
    @Test
    public void testBeanStyleVirtualAttributeNameNotStripped() {
        String jsonString = jsonb.toJson(new BeanStyleVirtualAttributeRecord("Jason", "Borne"));

        assertThat(jsonString, matchesPattern("\\{\\s*\"first\"\\s*:\\s*\"Jason\"\\s*,\\s*\"getDisplay\"\\s*:\\s*"
                + "\"Jason Borne\"\\s*,\\s*\"last\"\\s*:\\s*\"Borne\"\\s*}"));
    }
}
