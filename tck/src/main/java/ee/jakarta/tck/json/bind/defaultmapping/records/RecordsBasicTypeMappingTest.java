/*
 * Copyright (c) 2026 Eclipse and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */
package ee.jakarta.tck.json.bind.defaultmapping.records;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.json.bind.SimpleMappingTester;
import ee.jakarta.tck.json.bind.defaultmapping.records.model.BooleanContainer;
import ee.jakarta.tck.json.bind.defaultmapping.records.model.ByteContainer;
import ee.jakarta.tck.json.bind.defaultmapping.records.model.CharacterContainer;
import ee.jakarta.tck.json.bind.defaultmapping.records.model.DoubleContainer;
import ee.jakarta.tck.json.bind.defaultmapping.records.model.FloatContainer;
import ee.jakarta.tck.json.bind.defaultmapping.records.model.IntegerContainer;
import ee.jakarta.tck.json.bind.defaultmapping.records.model.LongContainer;
import ee.jakarta.tck.json.bind.defaultmapping.records.model.NumberContainer;
import ee.jakarta.tck.json.bind.defaultmapping.records.model.ShortContainer;
import ee.jakarta.tck.json.bind.defaultmapping.records.model.StringContainer;
import ee.jakarta.tck.json.bind.records.RecordMappingTester;

/**
 * @test
 * @sources RecordsBasicTypeMappingTest.java
 * @executeClass ee.jakarta.tck.json.bind.defaultmapping.records
 * 
 * Assert that records with basic component types are serialized and deserialized the same
 * as fields on a class.
 **/
public class RecordsBasicTypeMappingTest {
	
    /*
     * @testName: testStringMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.1-5; JSONB:SPEC:JSB-3.3-1;
     * JSONB:SPEC:JSB-3.3.1-1; JSONB:SPEC:JSB-3.3.1-2
     *
     * @test_Strategy: Assert that String type is correctly handled, encodings
     * other than UTF-8 are supported and that UTF-8 BOM does not produce an error
     */
    @Test
    public void testStringMapping() throws Exception {
        RecordMappingTester<String> stringRecordMappingTester = new RecordMappingTester<>(StringContainer.class);
        stringRecordMappingTester.test("Test String", "\"Test String\"");
        stringRecordMappingTester.test(new String(new byte[] {(byte) 0xef, (byte) 0xbb, (byte) 0xbf}, StandardCharsets.UTF_8),
                                 "\"" + new String(new byte[] {(byte) 0xef, (byte) 0xbb, (byte) 0xbf},
                                                   StandardCharsets.UTF_8) + "\"");
        stringRecordMappingTester.test(new String("Test String".getBytes(), StandardCharsets.UTF_8),
                                 "\"" + new String("Test String".getBytes(), StandardCharsets.UTF_8) + "\"");
    }

    /*
     * @testName: testCharacterMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.3-1; JSONB:SPEC:JSB-3.3.1-1
     *
     * @test_Strategy: Assert that Character type is correctly handled
     */
    @Test
    public void testCharacterMapping() {
        RecordMappingTester<Character> characterRecordMappingTester = new RecordMappingTester<>(CharacterContainer.class);
        characterRecordMappingTester.test('c', "\"c\"");
        // RFC 4627, paragraph 2.5: the control characters U+0000 must be
        // escaped
        characterRecordMappingTester.test(Character.MIN_VALUE, "\"\\u0000\"");
        characterRecordMappingTester.test(Character.MAX_VALUE, "\"" + Character.MAX_VALUE + "\"");
    }

    /*
     * @testName: testByteMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.3-1; JSONB:SPEC:JSB-3.3.2-1;
     * JSONB:SPEC:JSB-3.3.2-2
     *
     * @test_Strategy: Assert that Byte type is correctly handled in accordance
     * with toString and parseByte methods
     */
    @Test
    public void testByteMapping() {
        RecordMappingTester<Byte> byteRecordMappingTester = new RecordMappingTester<>(ByteContainer.class);
        byteRecordMappingTester.test((byte) 0, "0");
        byteRecordMappingTester.test(Byte.MIN_VALUE, String.valueOf(Byte.MIN_VALUE));
        byteRecordMappingTester.test(Byte.MAX_VALUE, String.valueOf(Byte.MAX_VALUE));
    }

    /*
     * @testName: testShortMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.3-1; JSONB:SPEC:JSB-3.3.2-1;
     * JSONB:SPEC:JSB-3.3.2-2
     *
     * @test_Strategy: Assert that Short type is correctly handled in accordance
     * with toString and parseShort methods
     */
    @Test
    public void testShortMapping() {
        RecordMappingTester<Short> shortRecordMappingTester = new RecordMappingTester<>(ShortContainer.class);
        shortRecordMappingTester.test((short) 0, "0");
        shortRecordMappingTester.test(Short.MIN_VALUE, String.valueOf(Short.MIN_VALUE));
        shortRecordMappingTester.test(Short.MAX_VALUE, String.valueOf(Short.MAX_VALUE));
    }

    /*
     * @testName: testIntegerMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.3-1; JSONB:SPEC:JSB-3.3.2-1;
     * JSONB:SPEC:JSB-3.3.2-2
     *
     * @test_Strategy: Assert that Integer type is correctly handled in accordance
     * with toString and parseInteger methods
     */
    @Test
    public void testIntegerMapping() {
        RecordMappingTester<Integer> integerRecordMappingTester = new RecordMappingTester<>(IntegerContainer.class);
        integerRecordMappingTester.test(0, "0");
        integerRecordMappingTester.test(Integer.MIN_VALUE, String.valueOf(Integer.MIN_VALUE));
        integerRecordMappingTester.test(Integer.MAX_VALUE, String.valueOf(Integer.MAX_VALUE));
    }

    /*
     * @testName: testLongMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.3-1; JSONB:SPEC:JSB-3.3.2-1;
     * JSONB:SPEC:JSB-3.3.2-2
     *
     * @test_Strategy: Assert that Long type is correctly handled in accordance
     * with toString and parseLong methods
     */
    @Test
    @Disabled("See https://github.com/jakartaee/jsonb-api/issues/180")
    public void testLongMapping() {
        RecordMappingTester<Long> longRecordMappingTester = new RecordMappingTester<>(LongContainer.class);
        longRecordMappingTester.test(0L, "0");
        longRecordMappingTester.test(Long.MIN_VALUE, String.valueOf(Long.MIN_VALUE));
        longRecordMappingTester.test(Long.MAX_VALUE, String.valueOf(Long.MAX_VALUE));
    }

    /*
     * @testName: testFloatMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.3-1; JSONB:SPEC:JSB-3.3.2-1;
     * JSONB:SPEC:JSB-3.3.2-2
     *
     * @test_Strategy: Assert that Float type is correctly handled in accordance
     * with toString and parseFloat methods
     */
    @Test
    public void testFloatMapping() {
        RecordMappingTester<Float> floatRecordMappingTester = new RecordMappingTester<>(FloatContainer.class);
        floatRecordMappingTester.test(0f, "0.0");
        floatRecordMappingTester.test(0.5f, "0.5");
        floatRecordMappingTester.test(Float.MIN_VALUE, String.valueOf(Float.MIN_VALUE));
        floatRecordMappingTester.test(Float.MAX_VALUE, String.valueOf(Float.MAX_VALUE));
    }

    /*
     * @testName: testDoubleMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.3-1; JSONB:SPEC:JSB-3.3.2-1;
     * JSONB:SPEC:JSB-3.3.2-2
     *
     * @test_Strategy: Assert that Double type is correctly handled in accordance
     * with toString and parseDouble methods
     */
    @Test
    public void testDoubleMapping() {
        RecordMappingTester<Double> doubleRecordMappingTester = new RecordMappingTester<>(DoubleContainer.class);
        doubleRecordMappingTester.test(0.0, "0.0");
        doubleRecordMappingTester.test(Double.MIN_VALUE, String.valueOf(Double.MIN_VALUE));
        doubleRecordMappingTester.test(Double.MAX_VALUE, String.valueOf(Double.MAX_VALUE));
    }

    /*
     * @testName: testBooleanMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.3-1; JSONB:SPEC:JSB-3.3.3-1;
     * JSONB:SPEC:JSB-3.3.3-1; JSONB:SPEC:JSB-3.3.3-2
     *
     * @test_Strategy: Assert that Boolean type is correctly handled in accordance
     * with toString and parseBoolean methods
     */
    @Test
    public void testBooleanMapping() {
        RecordMappingTester<Boolean> booleanRecordMappingTester = new RecordMappingTester<>(BooleanContainer.class);
        booleanRecordMappingTester.test(true, "true");
        booleanRecordMappingTester.test(false, "false");
        booleanRecordMappingTester.test(Boolean.TRUE, Boolean.TRUE.toString());
        booleanRecordMappingTester.test(Boolean.FALSE, Boolean.FALSE.toString());
    }

    /*
     * @testName: testNumberMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.3.4-1; JSONB:SPEC:JSB-3.3.4-2;
     * JSONB:SPEC:JSB-3.10-1
     *
     * @test_Strategy: Assert that Number type is correctly marshalled using
     * java.lang.Number.doubleValue() and toString methods and unmarshalled to
     * java.math.BigDecimal using String constructor
     */
    @Test
    public void testNumberMapping() {
        new SimpleMappingTester<>(NumberContainer.class, NumberContainer.class).test(
                new NumberContainer(0), "\\{\\s*\"instance\"\\s*:\\s*0[\\.0]?+\\s*}",
                "{ \"instance\" : 0 }", new NumberContainer(new BigDecimal("0")));
    }

}
