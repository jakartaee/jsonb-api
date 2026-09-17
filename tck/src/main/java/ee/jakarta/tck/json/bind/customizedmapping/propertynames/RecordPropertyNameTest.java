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

package ee.jakarta.tck.json.bind.customizedmapping.propertynames;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.config.PropertyNamingStrategy;

import ee.jakarta.tck.json.bind.customizedmapping.propertynames.model.JsonbPropertyRecordContainer;
import ee.jakarta.tck.json.bind.customizedmapping.propertynames.model.MixedNamingStrategyRecord;
import ee.jakarta.tck.json.bind.customizedmapping.propertynames.model.VirtualAttributeNamingStrategyRecord;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

/**
 * @test
 * @sources RecordPropertyNameTest.java
 * @executeClass ee.jakarta.tck.json.bind.customizedmapping.propertynames.RecordPropertyNameTest
 **/
public class RecordPropertyNameTest {

    private final Jsonb jsonb = JsonbBuilder.create();

    /*
     * @testName: testJsonbPropertyOnRecordComponent
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.1.2-1
     *
     * @test_Strategy: Assert that @JsonbProperty on a record component applies
     * to both serialization and deserialization. During serialization the custom
     * name is written to JSON; during deserialization the custom name is read
     * from JSON to populate the component.
     */
    @Test
    public void testJsonbPropertyOnRecordComponent() {
        JsonbPropertyRecordContainer container = new JsonbPropertyRecordContainer("Test String");

        String jsonString = jsonb.toJson(container);
        assertThat("Failed to use JsonbProperty custom name during serialization of record component.",
                   jsonString, matchesPattern("\\{\\s*\"stringInstance\"\\s*:\\s*\"Test String\"\\s*\\}"));

        JsonbPropertyRecordContainer unmarshalledObject =
                jsonb.fromJson("{ \"stringInstance\" : \"Test String\" }", JsonbPropertyRecordContainer.class);
        assertThat("Failed to use JsonbProperty custom name during deserialization of record component.",
                   unmarshalledObject.instance(), is("Test String"));
    }

    /*
     * @testName: testNamingStrategyOnRecord
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.1.3-1
     *
     * @test_Strategy: Assert that a PropertyNamingStrategy is applied to record
     * component names during serialization and deserialization. An un-annotated
     * component is transformed by the strategy; a component annotated with
     * @JsonbProperty retains its annotation value.
     */
    @Test
    public void testNamingStrategyOnRecord() {
        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig()
                .withPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE_WITH_DASHES));

        // MixedNamingStrategyRecord components:
        //   annotatedInstance -> fixed at "fixedName" by @JsonbProperty
        //   strategyInstance  -> transformed by strategy to "strategy-instance"
        MixedNamingStrategyRecord record = new MixedNamingStrategyRecord("fixed value", "strategy value");

        String jsonString = jsonb.toJson(record);
        assertThat("JsonbProperty annotation should override naming strategy for annotated record component.",
                   jsonString, matchesPattern(".*\"fixedName\"\\s*:\\s*\"fixed value\".*"));
        assertThat("PropertyNamingStrategy should transform un-annotated record component name.",
                   jsonString, matchesPattern(".*\"strategy-instance\"\\s*:\\s*\"strategy value\".*"));

        MixedNamingStrategyRecord unmarshalledObject =
                jsonb.fromJson("{ \"fixedName\" : \"fixed value\", \"strategy-instance\" : \"strategy value\" }",
                               MixedNamingStrategyRecord.class);
        assertThat("Failed to deserialize record component using JsonbProperty name.",
                   unmarshalledObject.annotatedInstance(), is("fixed value"));
        assertThat("Failed to deserialize record component using strategy-transformed name.",
                   unmarshalledObject.strategyInstance(), is("strategy value"));
    }

    /*
     * @testName: testNamingStrategyOnRecordVirtualAttribute
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.1.3-1
     *
     * @test_Strategy: Assert that a PropertyNamingStrategy is applied to record
     * virtual attribute method names during serialization.
     */
    @Test
    public void testNamingStrategyOnRecordVirtualAttribute() {
        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig()
                .withPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE_WITH_DASHES));

        // VirtualAttributeNamingStrategyRecord:
        //   component  firstName  -> "first-name"
        //   virtual    fullName() -> "full-name"
        VirtualAttributeNamingStrategyRecord record = new VirtualAttributeNamingStrategyRecord("John");

        String jsonString = jsonb.toJson(record);
        assertThat("PropertyNamingStrategy should transform record component name.",
                   jsonString, matchesPattern(".*\"first-name\"\\s*:\\s*\"John\".*"));
        assertThat("PropertyNamingStrategy should transform record virtual attribute name.",
                   jsonString, matchesPattern(".*\"full-name\"\\s*:\\s*\"John\".*"));
    }
}
