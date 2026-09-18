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

package ee.jakarta.tck.json.bind.customizedmapping.propertyorder;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.config.PropertyOrderStrategy;

import ee.jakarta.tck.json.bind.customizedmapping.propertyorder.model.NamingStrategyOrderedRecord;
import ee.jakarta.tck.json.bind.customizedmapping.propertyorder.model.SimpleOrderInverseNameStrategy;
import ee.jakarta.tck.json.bind.customizedmapping.propertyorder.model.SimpleOrderRecord;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

/**
 * @test
 * @sources RecordPropertyOrderTest.java
 * @executeClass ee.jakarta.tck.json.bind.customizedmapping.propertyorder.RecordPropertyOrderTest
 **/
public class RecordPropertyOrderTest {

    private final Jsonb jsonb = JsonbBuilder.create();

    /*
     * @testName: testCustomPropertyOrderOnRecord
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.2-2
     *
     * @test_Strategy: Assert that @JsonbPropertyOrder on a record type controls
     * serialization order, and that names in the annotation correspond to the
     * original component names before any @JsonbProperty renaming is applied.
     * The serialized JSON keys must use the renamed values.
     */
    @Test
    public void testCustomPropertyOrderOnRecord() {
        // NamingStrategyOrderedRecord: @JsonbPropertyOrder({"longInstance","intInstance","stringInstance"})
        // components renamed: intInstance->"alpha", stringInstance->"beta", longInstance->"gamma"
        // expected output order: gamma, alpha, beta
        NamingStrategyOrderedRecord record = new NamingStrategyOrderedRecord(0, "Test String", 0L);

        String jsonString = jsonb.toJson(record);
        assertThat("Failed to serialize record properties in @JsonbPropertyOrder order "
                           + "using original component names when @JsonbProperty renames are present.",
                   jsonString, matchesPattern("\\{\\s*\"gamma\"\\s*:\\s*0\\s*,\\s*\"alpha\"\\s*:\\s*0\\s*,"
                                                      + "\\s*\"beta\"\\s*:\\s*\"Test String\"\\s*\\}"));
    }

    /*
     * @testName: testPropertyOrderOriginalNamesOnRecord
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.2-2
     *
     * @test_Strategy: Assert that when @JsonbPropertyOrder and @JsonbProperty are
     * both present on a record, the annotation value lists original component names
     * and the serialized output uses the @JsonbProperty-renamed keys in that order.
     */
    @Test
    public void testPropertyOrderOriginalNamesOnRecord() {
        // Same model as testCustomPropertyOrderOnRecord — verifies ordering is keyed on
        // original names, not the renamed JSON keys.
        NamingStrategyOrderedRecord record = new NamingStrategyOrderedRecord(42, "hello", 7L);

        String jsonString = jsonb.toJson(record);
        assertThat("@JsonbPropertyOrder must use original component names as ordering keys.",
                   jsonString, matchesPattern("\\{\\s*\"gamma\"\\s*:\\s*7\\s*,\\s*\"alpha\"\\s*:\\s*42\\s*,"
                                                      + "\\s*\"beta\"\\s*:\\s*\"hello\"\\s*\\}"));
    }

    /*
     * @testName: testLexicographicalPropertyOrderStrategyOnRecord
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.2
     *
     * @test_Strategy: Assert that PropertyOrderStrategy.LEXICOGRAPHICAL orders
     * record components in ascending lexicographical order of their identity names
     * during serialization.
     */
    @Test
    public void testLexicographicalPropertyOrderStrategyOnRecord() {
        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig()
                .withPropertyOrderStrategy(PropertyOrderStrategy.LEXICOGRAPHICAL));

        // SimpleOrderRecord components (declared order): intInstance, longInstance, stringInstance
        // Lexicographical order: intInstance, longInstance, stringInstance
        SimpleOrderRecord record = new SimpleOrderRecord(0, 0L, "Test String");

        String jsonString = jsonb.toJson(record);
        assertThat("Failed to serialize record components in lexicographical order.",
                   jsonString, matchesPattern("\\{\\s*\"intInstance\"\\s*:\\s*0\\s*,"
                                                      + "\\s*\"longInstance\"\\s*:\\s*0\\s*,"
                                                      + "\\s*\"stringInstance\"\\s*:\\s*\"Test String\"\\s*\\}"));
    }

    /*
     * @testName: testReversePropertyOrderStrategyOnRecord
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.2
     *
     * @test_Strategy: Assert that PropertyOrderStrategy.REVERSE orders
     * record components in descending lexicographical order of their identity names
     * during serialization.
     */
    @Test
    public void testReversePropertyOrderStrategyOnRecord() {
        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig()
                .withPropertyOrderStrategy(PropertyOrderStrategy.REVERSE));

        // SimpleOrderRecord components: intInstance, longInstance, stringInstance
        // Reverse lexicographical order: stringInstance, longInstance, intInstance
        SimpleOrderRecord record = new SimpleOrderRecord(0, 0L, "Test String");

        String jsonString = jsonb.toJson(record);
        assertThat("Failed to serialize record components in reverse lexicographical order.",
                   jsonString, matchesPattern("\\{\\s*\"stringInstance\"\\s*:\\s*\"Test String\"\\s*,"
                                                      + "\\s*\"longInstance\"\\s*:\\s*0\\s*,"
                                                      + "\\s*\"intInstance\"\\s*:\\s*0\\s*\\}"));
    }

    /*
     * @testName: testAnyPropertyOrderStrategyOnRecord
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.2
     *
     * @test_Strategy: Assert that no error occurs when using
     * PropertyOrderStrategy.ANY on a record type, and that all components are
     * present in the output regardless of order.
     */
    @Test
    public void testAnyPropertyOrderStrategyOnRecord() {
        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig()
                .withPropertyOrderStrategy(PropertyOrderStrategy.ANY));

        SimpleOrderRecord record = new SimpleOrderRecord(1, 2L, "Test String");

        SimpleOrderRecord unmarshalledObject = jsonb.fromJson(jsonb.toJson(record), SimpleOrderRecord.class);
        assertThat("Failed to correctly round-trip record using PropertyOrderStrategy.ANY.",
                   unmarshalledObject.intInstance(), is(1));
        assertThat("Failed to correctly round-trip record using PropertyOrderStrategy.ANY.",
                   unmarshalledObject.longInstance(), is(2L));
        assertThat("Failed to correctly round-trip record using PropertyOrderStrategy.ANY.",
                   unmarshalledObject.stringInstance(), is("Test String"));
    }

    /*
     * @testName: testLexicographicalOrderWithNamingStrategyOnRecord
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.2
     *
     * @test_Strategy: Assert that PropertyOrderStrategy.LEXICOGRAPHICAL orders
     * record components by their final JSON names after PropertyNamingStrategy is applied.
     */
    @Test
    public void testLexicographicalOrderWithNamingStrategyOnRecord() {
        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig()
                .withPropertyOrderStrategy(PropertyOrderStrategy.LEXICOGRAPHICAL)
                .withPropertyNamingStrategy(new SimpleOrderInverseNameStrategy()));

        // SimpleOrderRecord components: intInstance, longInstance, stringInstance
        // After SimpleOrderInverseNameStrategy:
        //   intInstance    -> "z_int_instance"
        //   longInstance   -> "m_long_instance"
        //   stringInstance -> "a_string_instance"
        // Lexicographical order of final names: a_string_instance, m_long_instance, z_int_instance
        SimpleOrderRecord record = new SimpleOrderRecord(0, 0L, "Test String");

        String jsonString = jsonb.toJson(record);
        assertThat("Failed to apply LEXICOGRAPHICAL ordering on final names after PropertyNamingStrategy on record.",
                   jsonString, matchesPattern("\\{\\s*\"a_string_instance\"\\s*:\\s*\"Test String\"\\s*,"
                                                      + "\\s*\"m_long_instance\"\\s*:\\s*0\\s*,"
                                                      + "\\s*\"z_int_instance\"\\s*:\\s*0\\s*\\}"));
    }

    /*
     * @testName: testReverseOrderWithNamingStrategyOnRecord
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.2
     *
     * @test_Strategy: Assert that PropertyOrderStrategy.REVERSE orders
     * record components by their final JSON names (descending) after PropertyNamingStrategy is applied.
     */
    @Test
    public void testReverseOrderWithNamingStrategyOnRecord() {
        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig()
                .withPropertyOrderStrategy(PropertyOrderStrategy.REVERSE)
                .withPropertyNamingStrategy(new SimpleOrderInverseNameStrategy()));

        // SimpleOrderRecord components: intInstance, longInstance, stringInstance
        // After SimpleOrderInverseNameStrategy:
        //   intInstance    -> "z_int_instance"
        //   longInstance   -> "m_long_instance"
        //   stringInstance -> "a_string_instance"
        // Reverse lexicographical order of final names: z_int_instance, m_long_instance, a_string_instance
        SimpleOrderRecord record = new SimpleOrderRecord(0, 0L, "Test String");

        String jsonString = jsonb.toJson(record);
        assertThat("Failed to apply REVERSE ordering on final names after PropertyNamingStrategy on record.",
                   jsonString, matchesPattern("\\{\\s*\"z_int_instance\"\\s*:\\s*0\\s*,"
                                                      + "\\s*\"m_long_instance\"\\s*:\\s*0\\s*,"
                                                      + "\\s*\"a_string_instance\"\\s*:\\s*\"Test String\"\\s*\\}"));
    }
}
