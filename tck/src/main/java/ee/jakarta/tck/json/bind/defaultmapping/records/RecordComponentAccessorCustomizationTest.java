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

import java.time.LocalDate;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import org.junit.jupiter.api.Test;

import ee.jakarta.tck.json.bind.defaultmapping.records.model.DateFormatAccessorRecord;
import ee.jakarta.tck.json.bind.defaultmapping.records.model.NumberFormatAccessorRecord;
import ee.jakarta.tck.json.bind.defaultmapping.records.model.PropertyAccessorRecord;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

/**
 * @test
 * @sources RecordComponentAccessorCustomizationTest.java
 * @executeClass ee.jakarta.tck.json.bind.defaultmapping.records
 *
 * Assert that annotations on redeclared record component accessors apply only to serialization.
 */
public class RecordComponentAccessorCustomizationTest {

    private final Jsonb jsonb = JsonbBuilder.create();

    /*
     * @testName: testJsonbPropertyOnRecordComponentAccessor
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.1.2-1
     *
     * @test_Strategy: Assert that JsonbProperty on a redeclared record component accessor
     * renames the property only during serialization.
     */
    @Test
    public void testJsonbPropertyOnRecordComponentAccessor() {
        String jsonString = jsonb.toJson(new PropertyAccessorRecord("value"));
        assertThat(jsonString, matchesPattern("\\{\\s*\"renamed\"\\s*:\\s*\"value\"\\s*}"));

        PropertyAccessorRecord result = jsonb.fromJson("{\"value\":\"value\"}", PropertyAccessorRecord.class);
        assertThat(result.value(), is("value"));
    }

    /*
     * @testName: testJsonbDateFormatOnRecordComponentAccessor
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.8-1
     *
     * @test_Strategy: Assert that JsonbDateFormat on a redeclared record component accessor
     * formats the value only during serialization.
     */
    @Test
    public void testJsonbDateFormatOnRecordComponentAccessor() {
        LocalDate date = LocalDate.of(2026, 3, 13);
        String jsonString = jsonb.toJson(new DateFormatAccessorRecord(date));
        assertThat(jsonString, matchesPattern("\\{\\s*\"date\"\\s*:\\s*\"13-03-2026\"\\s*}"));

        DateFormatAccessorRecord result = jsonb.fromJson("{\"date\":\"2026-03-13\"}", DateFormatAccessorRecord.class);
        assertThat(result.date(), is(date));
    }

    /*
     * @testName: testJsonbNumberFormatOnRecordComponentAccessor
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.9-1
     *
     * @test_Strategy: Assert that JsonbNumberFormat on a redeclared record component accessor
     * formats the value only during serialization.
     */
    @Test
    public void testJsonbNumberFormatOnRecordComponentAccessor() {
        String jsonString = jsonb.toJson(new NumberFormatAccessorRecord(1234.56));
        assertThat(jsonString, matchesPattern("\\{\\s*\"amount\"\\s*:\\s*\"1,234\\.56\"\\s*}"));

        NumberFormatAccessorRecord result = jsonb.fromJson("{\"amount\":1234.56}", NumberFormatAccessorRecord.class);
        assertThat(result.amount(), is(1234.56));
    }
}
