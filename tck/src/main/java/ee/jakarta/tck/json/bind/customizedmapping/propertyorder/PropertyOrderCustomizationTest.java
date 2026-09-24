/*
 * Copyright (c) 2017, 2022 Oracle and/or its affiliates. All rights reserved.
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
import jakarta.json.bind.config.PropertyNamingStrategy;
import jakarta.json.bind.config.PropertyOrderStrategy;

import ee.jakarta.tck.json.bind.customizedmapping.propertyorder.model.CustomOrderContainer;
import ee.jakarta.tck.json.bind.customizedmapping.propertyorder.model.OrderedNamingStrategyContainer;
import ee.jakarta.tck.json.bind.customizedmapping.propertyorder.model.OrderedRenamedPropertiesContainer;
import ee.jakarta.tck.json.bind.customizedmapping.propertyorder.model.PartialOrderContainer;
import ee.jakarta.tck.json.bind.customizedmapping.propertyorder.model.RenamedPropertiesContainer;
import ee.jakarta.tck.json.bind.customizedmapping.propertyorder.model.SimpleContainer;
import ee.jakarta.tck.json.bind.customizedmapping.propertyorder.model.SimpleOrderContainer;
import ee.jakarta.tck.json.bind.customizedmapping.propertyorder.model.SimpleOrderInverseNameStrategy;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

/**
 * @test
 * @sources PropertyOrderCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.propertyorder.PropertyOrderCustomizationTest
 **/
public class PropertyOrderCustomizationTest {

    private final Jsonb jsonb = JsonbBuilder.create();

    /*
     * @testName: testAnyPropertyOrderStrategy
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.2
     *
     * @test_Strategy: Assert that no error occurs when using
     * PropertyOrderStrategy.ANY
     */
    @Test
    public void testAnyPropertyOrderStrategy() {
        JsonbConfig config = new JsonbConfig().setProperty(JsonbConfig.PROPERTY_ORDER_STRATEGY, PropertyOrderStrategy.ANY);
        Jsonb jsonb = JsonbBuilder.create(config);
        String jsonString = jsonb.toJson(new SimpleContainer() {
            {
                setStringInstance("Test String");
            }
        });
        SimpleContainer unmarshalledObject = jsonb.fromJson(jsonString, SimpleContainer.class);
        String validationMessage = "Failed to correctly marshal and unmarshal object using PropertyOrderStrategy.ANY.";
        assertThat(validationMessage, unmarshalledObject.getStringInstance(), is("Test String"));
        assertThat(validationMessage, unmarshalledObject.getIntInstance(), is(0));
        assertThat(validationMessage, unmarshalledObject.getLongInstance(), is(0L));
    }

    /*
     * @testName: testLexicographicalPropertyOrderStrategy
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.2
     *
     * @test_Strategy: Assert that marshalling property order is lexicographical
     * when using PropertyOrderStrategy.LEXICOGRAPHICAL and unmarshalling property
     * order is the order of appearance in the JSON document
     */
    @Test
    public void testLexicographicalPropertyOrderStrategy() {
        JsonbConfig config = new JsonbConfig()
                .setProperty(JsonbConfig.PROPERTY_ORDER_STRATEGY, PropertyOrderStrategy.LEXICOGRAPHICAL);
        Jsonb jsonb = JsonbBuilder.create(config);
        String jsonString = jsonb.toJson(new SimpleOrderContainer() {
            {
                setStringInstance("Test String");
            }
        });
        assertThat("Failed to correctly marshal properties in lexicographical order using PropertyOrderStrategy.LEXICOGRAPHICAL.",
                   jsonString, matchesPattern("\\{\\s*\"intInstance\"\\s*\\:\\s*0\\s*,\\s*\"longInstance\"\\s*\\:\\s*0\\s*,"
                                                      + "\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}"));

        SimpleOrderContainer unmarshalledObject = jsonb.fromJson("{ \"intInstance\" : 1, \"stringInstance\" : \"Test String\", "
                                                                         + "\"longInstance\" : 1 }",
                                                                 SimpleOrderContainer.class);
        assertThat("Failed to correctly unmarshal properties in order of appearance using PropertyOrderStrategy.LEXICOGRAPHICAL.",
                   unmarshalledObject.getIntInstance(), is(3));
    }

    /*
     * @testName: testReversePropertyOrderStrategy
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.2
     *
     * @test_Strategy: Assert that marshalling property order is reverse
     * lexicographical when using PropertyOrderStrategy.REVERSE and unmarshalling
     * property order is the order of appearance in the JSON document
     */
    @Test
    public void testReversePropertyOrderStrategy() {
        JsonbConfig config = new JsonbConfig().setProperty(JsonbConfig.PROPERTY_ORDER_STRATEGY, PropertyOrderStrategy.REVERSE);
        Jsonb jsonb = JsonbBuilder.create(config);
        String jsonString = jsonb.toJson(new SimpleOrderContainer() {
            {
                setStringInstance("Test String");
            }
        });
        assertThat("Failed to correctly marshal properties in reverse lexicographical order using PropertyOrderStrategy.REVERSE.",
                   jsonString, matchesPattern("\\{\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\"\\s*,"
                                                      + "\\s*\"longInstance\"\\s*\\:\\s*0\\s*,"
                                                      + "\\s*\"intInstance\"\\s*\\:\\s*0\\s*\\}"));

        SimpleOrderContainer unmarshalledObject = jsonb.fromJson("{ \"intInstance\" : 1, \"stringInstance\" : \"Test String\", "
                                                                         + "\"longInstance\" : 1 }",
                                                                 SimpleOrderContainer.class);
        assertThat("Failed to correctly unmarshal properties in order of appearance using PropertyOrderStrategy.REVERSE.",
                   unmarshalledObject.getIntInstance(), is(3));
    }

    /*
     * @testName: testCustomPropertyOrder
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.2-2
     *
     * @test_Strategy: Assert that marshalling property order is as specified by
     * JsonbPropertyOrder annotation and unmarshalling property order is the order
     * of appearance in the JSON document
     */
    @Test
    public void testCustomPropertyOrder() {
        String jsonString = jsonb.toJson(new CustomOrderContainer() {
            {
                setStringInstance("Test String");
            }
        });
        assertThat("Failed to correctly marshal properties in custom order using JsonbPropertyOrder annotation.",
                   jsonString, matchesPattern("\\{\\s*\"longInstance\"\\s*\\:\\s*0\\s*,\\s*\"intInstance\"\\s*\\:\\s*0\\s*,"
                                                      + "\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}"));

        CustomOrderContainer unmarshalledObject = jsonb.fromJson("{ \"intInstance\" : 1, \"stringInstance\" : \"Test String\", "
                                                                         + "\"longInstance\" : 0 }",
                                                                 CustomOrderContainer.class);
        assertThat("Failed to correctly unmarshal properties in order of appearance using JsonbPropertyOrder annotation.",
                   unmarshalledObject.getIntInstance(), is(3));
    }

    /*
     * @testName: testCustomPropertyOrderStrategyOverride
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.3-1
     *
     * @test_Strategy: Assert that marshalling property order is as specified by
     * JsonbPropertyOrder annotation regardless of PropertyOrderStrategy specified
     * and unmarshalling property order is the order of appearance in the JSON
     * document
     */
    @Test
    public void testCustomPropertyOrderStrategyOverride() {
        JsonbConfig config = new JsonbConfig().setProperty(JsonbConfig.PROPERTY_ORDER_STRATEGY, PropertyOrderStrategy.REVERSE);
        Jsonb jsonb = JsonbBuilder.create(config);
        String jsonString = jsonb.toJson(new CustomOrderContainer() {
            {
                setStringInstance("Test String");
            }
        });
        assertThat("Failed to correctly marshal properties in custom order using JsonbPropertyOrder annotation.",
                   jsonString, matchesPattern("\\{\\s*\"longInstance\"\\s*\\:\\s*0\\s*,\\s*\"intInstance\"\\s*\\:\\s*0\\s*,"
                                                      + "\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}"));

        CustomOrderContainer unmarshalledObject = jsonb.fromJson("{ \"intInstance\" : 1, \"stringInstance\" : \"Test String\", "
                                                                         + "\"longInstance\" : 0 }",
                                                                 CustomOrderContainer.class);
        assertThat("Failed to correctly unmarshal properties in order of appearance using JsonbPropertyOrder annotation and "
                           + "PropertyOrderStrategy.REVERSE.",
                   unmarshalledObject.getIntInstance(), is(3));
    }

    /*
     * @testName: testCustomPartialPropertyOrder
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.2-2
     *
     * @test_Strategy: In that case, properties included in annotation declaration
     * will be serialized first (in defined order), followed by any properties not
     * included in the definition. The order of properties not included in the
     * definition is not guaranteed
     */
    @Test
    public void testCustomPartialPropertyOrder() {
        String jsonString = jsonb.toJson(new PartialOrderContainer() {
            {
                setStringInstance("Test String");
            }
        });
        assertThat("Failed to correctly marshal properties in custom order using JsonbPropertyOrder annotation.",
                   jsonString, matchesPattern("\\{\\s*\"longInstance\"\\s*\\:\\s*0\\s*,\\s*\"intInstance\"\\s*\\:\\s*0\\s*,"
                                                      + "\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\".*\\}"));

        String validationMessage = "Did not marshall all expected properties";
        assertThat(validationMessage, jsonString, containsString("anotherIntInstance"));
        assertThat(validationMessage, jsonString, containsString("anIntInstance"));
        assertThat(validationMessage, jsonString, containsString("yetAnotherIntInstance"));

        String toDeserialize = "{ \"anIntInstance\" : 100, \"yetAnotherIntInstance\":100, \"anotherIntInstance\": 100, "
                + "\"intInstance\" : 1, \"stringInstance\" : \"Test String\", \"longInstance\" : 0 }";
        PartialOrderContainer unmarshalledObject = jsonb.fromJson(toDeserialize, PartialOrderContainer.class);
        validationMessage = "Failed to correctly unmarshal properties in order of appearance using JsonbPropertyOrder "
                + "annotation.";
        assertThat(validationMessage, unmarshalledObject.getIntInstance(), is(3));
        assertThat(validationMessage, unmarshalledObject.getAnotherIntInstance(), is(100));
        assertThat(validationMessage, unmarshalledObject.getYetAnotherIntInstance(), is(100));
        assertThat(validationMessage, unmarshalledObject.getAnIntInstance(), is(100));
    }

    /*
     * @testName: testCustomPartialPropertyOrderStrategyOverride
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.3-1
     *
     * @test_Strategy: In that case, properties included in annotation declaration
     * will be serialized first (in defined order), followed by any properties not
     * included in the definition. The order of properties not included in the
     * definition is not guaranteed
     */
    @Test
    public void testCustomPartialPropertyOrderStrategyOverride() {
        JsonbConfig config = new JsonbConfig().setProperty(JsonbConfig.PROPERTY_ORDER_STRATEGY, PropertyOrderStrategy.REVERSE);
        Jsonb jsonb = JsonbBuilder.create(config);
        String jsonString = jsonb.toJson(new PartialOrderContainer() {
            {
                setStringInstance("Test String");
            }
        });
        assertThat("Failed to correctly marshal properties in custom order using JsonbPropertyOrder annotation.",
                   jsonString, matchesPattern("\\{\\s*\"longInstance\"\\s*\\:\\s*0\\s*,\\s*\"intInstance\"\\s*\\:\\s*0\\s*,"
                                                      + "\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\".*\\}"));

        String validationMessage = "Did not marshall all expected properties";
        assertThat(validationMessage, jsonString, containsString("anotherIntInstance"));
        assertThat(validationMessage, jsonString, containsString("anIntInstance"));
        assertThat(validationMessage, jsonString, containsString("yetAnotherIntInstance"));

        String toDeserialize = "{ \"anIntInstance\" : 100, \"yetAnotherIntInstance\":100, \"anotherIntInstance\": 100, "
                + "\"intInstance\" : 1, \"stringInstance\" : \"Test String\", \"longInstance\" : 0 }";
        PartialOrderContainer unmarshalledObject = jsonb.fromJson(toDeserialize, PartialOrderContainer.class);
        validationMessage = "Failed to correctly unmarshal properties in order of appearance using JsonbPropertyOrder "
                + "annotation.";
        assertThat(validationMessage, unmarshalledObject.getIntInstance(), is(3));
        assertThat(validationMessage, unmarshalledObject.getAnotherIntInstance(), is(100));
        assertThat(validationMessage, unmarshalledObject.getYetAnotherIntInstance(), is(100));
        assertThat(validationMessage, unmarshalledObject.getAnIntInstance(), is(100));
    }

    /*
     * @testName: testLexicographicalPropertyOrderRenamedProperties
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.2
     *
     * @test_Strategy: Assert that marshalling property order is lexicographical
     * after property renaming has been applied and unmarshalling property order
     * is the order of appearance in the JSON document
     */
    @Test
    public void testLexicographicalPropertyOrderRenamedProperties() {
        JsonbConfig config = new JsonbConfig()
                .setProperty(JsonbConfig.PROPERTY_ORDER_STRATEGY, PropertyOrderStrategy.LEXICOGRAPHICAL);
        Jsonb jsonb = JsonbBuilder.create(config);
        String jsonString = jsonb.toJson(new RenamedPropertiesContainer() {
            {
                setStringInstance("Test String");
                setLongInstance(1);
            }
        });
        assertThat("Failed to correctly marshal renamed properties in lexicographical order "
                           + "using PropertyOrderStrategy.LEXICOGRAPHICAL.",
                   jsonString, matchesPattern("\\{\\s*\"first\"\\s*\\:\\s*0\\s*,\\s*\"second\"\\s*\\:\\s*\"Test String\"\\s*,"
                                                      + "\\s*\"third\"\\s*\\:\\s*1\\s*\\}"));

        RenamedPropertiesContainer unmarshalledObject = jsonb.fromJson("{ \"first\" : 1, \"second\" : \"Test String\", "
                                                                               + "\"third\" : 1 }",
                                                                       RenamedPropertiesContainer.class);
        assertThat("Failed to correctly unmarshal renamed properties in order of appearance "
                           + "using PropertyOrderStrategy.LEXICOGRAPHICAL.",
                   unmarshalledObject.getIntInstance(), is(3));
    }

    /*
     * @testName: testPropertyOrderOriginalNamesWithRenamedProperties
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.2-2
     *
     * @test_Strategy: Assert that names provided in @JsonbPropertyOrder correspond
     * to the original property names before any @JsonbProperty renaming is applied,
     * and that the serialized JSON keys use the renamed values.
     */
    @Test
    public void testPropertyOrderOriginalNamesWithRenamedProperties() {
        // OrderedRenamedPropertiesContainer: @JsonbPropertyOrder({"longInstance","intInstance","stringInstance"})
        // fields renamed: intInstance->"alpha", stringInstance->"beta", longInstance->"gamma"
        // expected output order: gamma, alpha, beta  (following annotation order of original names)
        OrderedRenamedPropertiesContainer container = new OrderedRenamedPropertiesContainer();
        container.setStringInstance("Test String");

        String jsonString = jsonb.toJson(container);
        assertThat("Failed to serialize properties in @JsonbPropertyOrder order using original names "
                           + "when @JsonbProperty renames are present.",
                   jsonString, matchesPattern("\\{\\s*\"gamma\"\\s*:\\s*0\\s*,\\s*\"alpha\"\\s*:\\s*0\\s*,"
                                                      + "\\s*\"beta\"\\s*:\\s*\"Test String\"\\s*\\}"));
    }

    /*
     * @testName: testPropertyOrderOriginalNamesWithNamingStrategy
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.2-2
     *
     * @test_Strategy: Assert that names provided in @JsonbPropertyOrder correspond
     * to the original property names before any PropertyNamingStrategy is applied,
     * and that the serialized JSON keys use the strategy-transformed values.
     */
    @Test
    public void testPropertyOrderOriginalNamesWithNamingStrategy() {
        JsonbConfig config = new JsonbConfig().setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY,
                                                           PropertyNamingStrategy.LOWER_CASE_WITH_DASHES);
        Jsonb jsonb = JsonbBuilder.create(config);

        // OrderedNamingStrategyContainer: @JsonbPropertyOrder({"longInstance","intInstance","stringInstance"})
        // strategy transforms: longInstance->"long-instance", intInstance->"int-instance",
        //                      stringInstance->"string-instance"
        // expected order: long-instance, int-instance, string-instance
        OrderedNamingStrategyContainer container = new OrderedNamingStrategyContainer();
        container.setStringInstance("Test String");

        String jsonString = jsonb.toJson(container);
        assertThat("Failed to serialize properties in @JsonbPropertyOrder order using original names "
                           + "when PropertyNamingStrategy is present.",
                   jsonString, matchesPattern("\\{\\s*\"long-instance\"\\s*:\\s*0\\s*,\\s*\"int-instance\"\\s*:\\s*0\\s*,"
                                                      + "\\s*\"string-instance\"\\s*:\\s*\"Test String\"\\s*\\}"));
    }

    /*
     * @testName: testLexicographicalOrderWithNamingStrategy
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.2
     *
     * @test_Strategy: Assert that PropertyOrderStrategy.LEXICOGRAPHICAL orders
     * properties by their final JSON names after PropertyNamingStrategy is applied.
     */
    @Test
    public void testLexicographicalOrderWithNamingStrategy() {
        JsonbConfig config = new JsonbConfig()
                .withPropertyOrderStrategy(PropertyOrderStrategy.LEXICOGRAPHICAL)
                .withPropertyNamingStrategy(new SimpleOrderInverseNameStrategy());
        Jsonb jsonb = JsonbBuilder.create(config);

        // SimpleOrderContainer fields: intInstance, longInstance, stringInstance
        // After SimpleOrderInverseNameStrategy:
        //   intInstance    -> "z_int_instance"
        //   longInstance   -> "m_long_instance"
        //   stringInstance -> "a_string_instance"
        // Lexicographical order of final names: a_string_instance, m_long_instance, z_int_instance
        SimpleOrderContainer container = new SimpleOrderContainer();
        container.setStringInstance("Test String");

        String jsonString = jsonb.toJson(container);
        assertThat("Failed to apply LEXICOGRAPHICAL ordering on final names after PropertyNamingStrategy.",
                   jsonString, matchesPattern("\\{\\s*\"a_string_instance\"\\s*:\\s*\"Test String\"\\s*,"
                                                      + "\\s*\"m_long_instance\"\\s*:\\s*0\\s*,"
                                                      + "\\s*\"z_int_instance\"\\s*:\\s*0\\s*\\}"));
    }

    /*
     * @testName: testReverseOrderWithNamingStrategy
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.2
     *
     * @test_Strategy: Assert that PropertyOrderStrategy.REVERSE orders
     * properties by their final JSON names (descending) after PropertyNamingStrategy is applied.
     */
    @Test
    public void testReverseOrderWithNamingStrategy() {
        JsonbConfig config = new JsonbConfig()
                .withPropertyOrderStrategy(PropertyOrderStrategy.REVERSE)
                .withPropertyNamingStrategy(new SimpleOrderInverseNameStrategy());
        Jsonb jsonb = JsonbBuilder.create(config);

        // SimpleOrderContainer fields: intInstance, longInstance, stringInstance
        // After SimpleOrderInverseNameStrategy:
        //   intInstance    -> "z_int_instance"
        //   longInstance   -> "m_long_instance"
        //   stringInstance -> "a_string_instance"
        // Reverse lexicographical order of final names: z_int_instance, m_long_instance, a_string_instance
        SimpleOrderContainer container = new SimpleOrderContainer();
        container.setStringInstance("Test String");

        String jsonString = jsonb.toJson(container);
        assertThat("Failed to apply REVERSE ordering on final names after PropertyNamingStrategy.",
                   jsonString, matchesPattern("\\{\\s*\"z_int_instance\"\\s*:\\s*0\\s*,"
                                                      + "\\s*\"m_long_instance\"\\s*:\\s*0\\s*,"
                                                      + "\\s*\"a_string_instance\"\\s*:\\s*\"Test String\"\\s*\\}"));
    }

}
