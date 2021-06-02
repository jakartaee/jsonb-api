/*
 * Copyright (c) 2017, 2021 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.json.bind.tck.defaultmapping.jsonptypes;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.tck.defaultmapping.jsonptypes.model.JsonArrayContainer;
import jakarta.json.bind.tck.defaultmapping.jsonptypes.model.JsonNumberContainer;
import jakarta.json.bind.tck.defaultmapping.jsonptypes.model.JsonObjectContainer;
import jakarta.json.bind.tck.defaultmapping.jsonptypes.model.JsonStringContainer;
import jakarta.json.bind.tck.defaultmapping.jsonptypes.model.JsonStructureContainer;
import jakarta.json.bind.tck.defaultmapping.jsonptypes.model.JsonValueContainer;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.nullValue;

/**
 * @test
 * @sources JSONPTypesMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.jsonptypes.JSONPTypesMappingTest
 **/
public class JSONPTypesMappingTest {

  private final Jsonb jsonb = JsonbBuilder.create();

  /*
   * @testName: testJsonObjectMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.20-1; JSONB:SPEC:JSB-3.20-2;
   * JSONB:SPEC:JSB-3.20-3
   *
   * @test_Strategy: Assert that JsonObject type is correctly handled
   */
  @Test
  public void testJsonObjectMapping() {
    JsonObject instance = Json.createObjectBuilder()
        .add("jsonObjectInstance", Json.createObjectBuilder().add("innerInstance", "Inner Test String"))
        .add("jsonArrayInstance",
            Json.createArrayBuilder()
                .add(Json.createObjectBuilder().add("arrayInstance1", "Array Test String 1"))
                .add(Json.createObjectBuilder().add("arrayInstance2", "Array Test String 2")))
        .add("jsonStringInstance", "Test String")
        .add("jsonNumberInstance", Integer.MAX_VALUE)
        .add("jsonTrueInstance", JsonValue.TRUE)
        .add("jsonFalseInstance", JsonValue.FALSE)
        .add("jsonNullInstance", JsonValue.NULL).build();

    String jsonString = jsonb.toJson(new JsonObjectContainer() {{
      setInstance(instance);
    }});
    String validationRegexp = "\\{\\s*\"instance\"\\s*:\\s*\\{"
            + "\\s*\"jsonObjectInstance\"\\s*:\\s*\\{\\s*\"innerInstance\"\\s*:\\s*\"Inner Test String\"\\s*\\}\\s*,"
            + "\\s*\"jsonArrayInstance\"\\s*:\\s*\\["
            + "\\s*\\{\\s*\"arrayInstance1\"\\s*:\\s*\"Array Test String 1\"\\s*\\}\\s*,"
            + "\\s*\\{\\s*\"arrayInstance2\"\\s*:\\s*\"Array Test String 2\"\\s*\\}\\s*\\]\\s*,"
            + "\\s*\"jsonStringInstance\"\\s*:\\s*\"Test String\"\\s*,"
            + "\\s*\"jsonNumberInstance\"\\s*:\\s*2147483647\\s*,"
            + "\\s*\"jsonTrueInstance\"\\s*:\\s*true\\s*,"
            + "\\s*\"jsonFalseInstance\"\\s*:\\s*false\\s*,"
            + "\\s*\"jsonNullInstance\"\\s*:\\s*null\\s*"
            + "\\}\\s*\\}";

    assertThat("Failed to marshal object with JsonObject attribute value.", jsonString, matchesPattern(validationRegexp));

    String toDeserialize = "{ \"instance\" : { "
            + "\"jsonObjectInstance\" : { \"innerInstance\" : \"Inner Test String\" }, "
            + "\"jsonArrayInstance\" : [ "
            + "{ \"arrayInstance1\" : \"Array Test String 1\" }, "
            + "{ \"arrayInstance2\" : \"Array Test String 2\" } ], "
            + "\"jsonStringInstance\" : \"Test String\", "
            + "\"jsonNumberInstance\" : 2147483647, "
            + "\"jsonTrueInstance\" : true, "
            + "\"jsonFalseInstance\" : false, "
            + "\"jsonNullInstance\" : null "
            + "} }";
    JsonObjectContainer unmarshalledObject = jsonb.fromJson(toDeserialize, JsonObjectContainer.class);
    assertThat("Failed to unmarshal object with JsonObject attribute value.",
               unmarshalledObject.getInstance(), is(instance));
  }

  /*
   * @testName: testEmptyJsonObjectMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.20-1; JSONB:SPEC:JSB-3.20-2;
   * JSONB:SPEC:JSB-3.20-3
   *
   * @test_Strategy: Assert that empty JsonObject is correctly handled
   */
  @Test
  public void testEmptyJsonObjectMapping() {
    JsonObject instance = Json.createObjectBuilder().build();
    String jsonString = jsonb.toJson(new JsonObjectContainer() {{
      setInstance(instance);
    }});
    assertThat("Failed to marshal object with empty JsonObject attribute value.",
               jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\\}\\s*\\}"));

    JsonObjectContainer unmarshalledObject = jsonb.fromJson("{ \"instance\" : { } }", JsonObjectContainer.class);
    assertThat("Failed to unmarshal object with empty JsonObject attribute value.",
               unmarshalledObject.getInstance(), is(instance));
  }

  /*
   * @testName: testJsonArrayMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.20-1; JSONB:SPEC:JSB-3.20-2;
   * JSONB:SPEC:JSB-3.20-3
   *
   * @test_Strategy: Assert that JsonArray type is correctly handled
   */
  @Test
  public void testJsonArrayMapping() {
    JsonArray instance = Json.createArrayBuilder()
        .add(Json.createObjectBuilder().add("arrayInstance1", "Array Test String 1"))
        .add(Json.createObjectBuilder().add("arrayInstance2", "Array Test String 2"))
        .build();

    String jsonString = jsonb.toJson(new JsonArrayContainer() {{
        setInstance(instance);
    }});
    String validationPattern = "\\{\\s*\"instance\"\\s*:\\s*\\["
            + "\\s*\\{\\s*\"arrayInstance1\"\\s*:\\s*\"Array Test String 1\"\\s*\\}\\s*,"
            + "\\s*\\{\\s*\"arrayInstance2\"\\s*:\\s*\"Array Test String 2\"\\s*\\}\\s*"
            + "\\]\\s*\\}";
    assertThat("Failed to marshal object with JsonArray attribute value.",
               jsonString, matchesPattern(validationPattern));

    String toDeserialize = "{ \"instance\" : [ "
            + "{ \"arrayInstance1\" : \"Array Test String 1\" }, "
            + "{ \"arrayInstance2\" : \"Array Test String 2\" } "
            + "] }";
    JsonArrayContainer unmarshalledObject = jsonb.fromJson(toDeserialize, JsonArrayContainer.class);
    assertThat("Failed to unmarshal object with JsonArray attribute value.",
               unmarshalledObject.getInstance(), is(instance));
  }

  /*
   * @testName: testEmptyJsonArrayMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.20-1; JSONB:SPEC:JSB-3.20-2;
   * JSONB:SPEC:JSB-3.20-3
   *
   * @test_Strategy: Assert that empty JsonArray is correctly handled
   */
  @Test
  public void testEmptyJsonArrayMapping() {
    JsonArray instance = Json.createArrayBuilder().build();
    String jsonString = jsonb.toJson(new JsonArrayContainer() {{
      setInstance(instance);
    }});
    assertThat("Failed to marshal object with empty JsonArray attribute value.",
               jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\\]\\s*\\}"));

    JsonArrayContainer unmarshalledObject = jsonb.fromJson("{ \"instance\" : [ ] }", JsonArrayContainer.class);
    assertThat("Failed to unmarshal object with empty JsonArray attribute value.",
               unmarshalledObject.getInstance(), is(instance));
  }

  /*
   * @testName: testJsonObjectStructureMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.20-1; JSONB:SPEC:JSB-3.20-2;
   * JSONB:SPEC:JSB-3.20-3
   *
   * @test_Strategy: Assert that JsonStructure of JsonObject is correctly
   * handled
   */
  @Test
  public void testJsonObjectStructureMapping() {
    JsonStructure instance = Json.createObjectBuilder()
        .add("jsonObjectInstance", Json.createObjectBuilder().add("innerInstance", "Inner Test String"))
        .add("jsonArrayInstance",
            Json.createArrayBuilder()
                    .add(Json.createObjectBuilder().add("arrayInstance1", "Array Test String 1"))
                    .add(Json.createObjectBuilder().add("arrayInstance2", "Array Test String 2")))
        .add("jsonStringInstance", "Test String")
        .add("jsonNumberInstance", Integer.MAX_VALUE)
        .add("jsonTrueInstance", JsonValue.TRUE)
        .add("jsonFalseInstance", JsonValue.FALSE)
        .add("jsonNullInstance", JsonValue.NULL).build();

    String jsonString = jsonb.toJson(new JsonStructureContainer() {{
      setInstance(instance);
    }});
    String validationRegexp = "\\{\\s*\"instance\"\\s*:\\s*\\{"
            + "\\s*\"jsonObjectInstance\"\\s*:\\s*\\{\\s*\"innerInstance\"\\s*:\\s*\"Inner Test String\"\\s*\\}\\s*,"
            + "\\s*\"jsonArrayInstance\"\\s*:\\s*\\["
            + "\\s*\\{\\s*\"arrayInstance1\"\\s*:\\s*\"Array Test String 1\"\\s*\\}\\s*,"
            + "\\s*\\{\\s*\"arrayInstance2\"\\s*:\\s*\"Array Test String 2\"\\s*\\}\\s*\\]\\s*,"
            + "\\s*\"jsonStringInstance\"\\s*:\\s*\"Test String\"\\s*,"
            + "\\s*\"jsonNumberInstance\"\\s*:\\s*2147483647\\s*,"
            + "\\s*\"jsonTrueInstance\"\\s*:\\s*true\\s*,"
            + "\\s*\"jsonFalseInstance\"\\s*:\\s*false\\s*,"
            + "\\s*\"jsonNullInstance\"\\s*:\\s*null\\s*"
            + "\\}\\s*\\}";

    assertThat("Failed to marshal object with JsonObject JsonStructure attribute value.",
               jsonString, matchesPattern(validationRegexp));

    String toDeserialize = "{ \"instance\" : { "
            + "\"jsonObjectInstance\" : { \"innerInstance\" : \"Inner Test String\" }, "
            + "\"jsonArrayInstance\" : [ "
            + "{ \"arrayInstance1\" : \"Array Test String 1\" }, "
            + "{ \"arrayInstance2\" : \"Array Test String 2\" } ], "
            + "\"jsonStringInstance\" : \"Test String\", "
            + "\"jsonNumberInstance\" : 2147483647, "
            + "\"jsonTrueInstance\" : true, "
            + "\"jsonFalseInstance\" : false, "
            + "\"jsonNullInstance\" : null "
            + "} }";
    JsonStructureContainer unmarshalledObject = jsonb.fromJson(toDeserialize, JsonStructureContainer.class);
    assertThat("Failed to unmarshal object with JsonObject JsonStructure attribute value.",
               unmarshalledObject.getInstance(), is(instance));
  }

  /*
   * @testName: testJsonArrayStructureMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.20-1; JSONB:SPEC:JSB-3.20-2;
   * JSONB:SPEC:JSB-3.20-3
   *
   * @test_Strategy: Assert that JsonStructure of JsonArray is correctly handled
   */
  @Test
  public void testJsonArrayStructureMapping() {
    JsonStructure instance = Json.createArrayBuilder()
        .add(Json.createObjectBuilder().add("arrayInstance1", "Array Test String 1"))
        .add(Json.createObjectBuilder().add("arrayInstance2", "Array Test String 2"))
        .build();

    String jsonString = jsonb.toJson(new JsonStructureContainer() {{
      setInstance(instance);
    }});
    String validationRegexp = "\\{\\s*\"instance\"\\s*:\\s*\\["
            + "\\s*\\{\\s*\"arrayInstance1\"\\s*:\\s*\"Array Test String 1\"\\s*\\}\\s*,"
            + "\\s*\\{\\s*\"arrayInstance2\"\\s*:\\s*\"Array Test String 2\"\\s*\\}\\s*"
            + "\\]\\s*\\}";
    assertThat("Failed to marshal object with JsonArray JsonStructure attribute value.",
               jsonString, matchesPattern(validationRegexp));

    String toDeserialize = "{ \"instance\" : [ "
            + "{ \"arrayInstance1\" : \"Array Test String 1\" }, "
            + "{ \"arrayInstance2\" : \"Array Test String 2\" } "
            + "] }";
    JsonStructureContainer unmarshalledObject = jsonb.fromJson(toDeserialize, JsonStructureContainer.class);
    assertThat("Failed to unmarshal object with JsonArray JsonStructure attribute value.",
               unmarshalledObject.getInstance(), is(instance));
  }

  /*
   * @testName: testJsonValueMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.20-1; JSONB:SPEC:JSB-3.20-2;
   * JSONB:SPEC:JSB-3.20-3
   *
   * @test_Strategy: Assert that JsonValue type is correctly handled
   */
  @Test
  public void testJsonValueMapping() {
    JsonValue instance = Json.createObjectBuilder().add("stringInstance", "Test String").build();
    String jsonString = jsonb.toJson(new JsonValueContainer() {{
      setInstance(instance);
    }});
    assertThat("Failed to marshal object with JsonValue attribute value.",
               jsonString,
               matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"stringInstance\"\\s*:\\s*\"Test String\"\\s*\\}\\s*\\}"));

    JsonValueContainer unmarshalledObject = jsonb.fromJson("{ \"instance\" : { \"stringInstance\" : \"Test String\" } }",
                                                           JsonValueContainer.class);
    assertThat("Failed to unmarshal object with JsonValue attribute value.", unmarshalledObject.getInstance(), is(instance));
  }

  /*
   * @testName: testJsonStringMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.20-1; JSONB:SPEC:JSB-3.20-2;
   * JSONB:SPEC:JSB-3.20-3
   *
   * @test_Strategy: Assert that JsonString type is correctly handled
   */
  @Test
  public void testJsonStringMapping() {
    JsonString instance = Json.createValue("Test String");
    String jsonString = jsonb.toJson(new JsonStringContainer() {{
      setInstance(instance);
    }});
    assertThat("Failed to marshal object with JsonString attribute value.",
               jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*\\}"));

    JsonStringContainer unmarshalledObject = jsonb.fromJson("{ \"instance\" : \"Test String\" }", JsonStringContainer.class);
    assertThat("Failed to unmarshal object with JsonString attribute value.", unmarshalledObject.getInstance(), is(instance));
  }

  /*
   * @testName: testJsonNumberMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.20-1; JSONB:SPEC:JSB-3.20-2;
   * JSONB:SPEC:JSB-3.20-3
   *
   * @test_Strategy: Assert that JsonNumber type is correctly handled
   */
  @Test
  public void testJsonNumberMapping() {
    JsonNumber instance = Json.createValue(0);
    String jsonString = jsonb.toJson(new JsonNumberContainer() {{
      setInstance(instance);
    }});
    assertThat("Failed to marshal object with JsonNumber attribute value.",
               jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*0\\s*\\}"));

    JsonNumberContainer unmarshalledObject = jsonb.fromJson("{ \"instance\" : 0 }", JsonNumberContainer.class);
    assertThat("Failed to unmarshal object with JsonNumber attribute value.", unmarshalledObject.getInstance(), is(instance));
  }

  /*
   * @testName: testNullDeserializedToJsonValueNull
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.20
   *
   * @test_Strategy: Assert that null is properly deserialized to the JsonValue.NULL
   */
  @Test
  public void testNullDeserializedToJsonValueNull() {
    JsonValueContainer unmarshalledValue = jsonb.fromJson("{ \"instance\" : null }", JsonValueContainer.class);
    assertThat("Failed to unmarshal null value to the JsonValue.NULL", unmarshalledValue.getInstance(), is(JsonValue.NULL));
    unmarshalledValue = jsonb.fromJson("{}", JsonValueContainer.class);
    assertThat("No value should have been deserialized.", unmarshalledValue.getInstance(), nullValue());
  }

}
