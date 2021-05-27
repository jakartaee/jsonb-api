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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.lang.invoke.MethodHandles;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

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

/**
 * @test
 * @sources JSONPTypesMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.jsonptypes.JSONPTypesMappingTest
 **/
@RunWith(Arquillian.class)
public class JSONPTypesMappingTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, MethodHandles.lookup().lookupClass().getPackage().getName());
    }

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
        .add("jsonObjectInstance",
            Json.createObjectBuilder().add("innerInstance",
                "Inner Test String"))
        .add("jsonArrayInstance",
            Json.createArrayBuilder()
                .add(Json.createObjectBuilder().add("arrayInstance1",
                    "Array Test String 1"))
                .add(Json.createObjectBuilder().add("arrayInstance2",
                    "Array Test String 2")))
        .add("jsonStringInstance", "Test String")
        .add("jsonNumberInstance", Integer.MAX_VALUE)
        .add("jsonTrueInstance", JsonValue.TRUE)
        .add("jsonFalseInstance", JsonValue.FALSE)
        .add("jsonNullInstance", JsonValue.NULL).build();

    String jsonString = jsonb.toJson(new JsonObjectContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\\{"
        + "\\s*\"jsonObjectInstance\"\\s*:\\s*\\{\\s*\"innerInstance\"\\s*:\\s*\"Inner Test String\"\\s*\\}\\s*,"
        + "\\s*\"jsonArrayInstance\"\\s*:\\s*\\["
        + "\\s*\\{\\s*\"arrayInstance1\"\\s*:\\s*\"Array Test String 1\"\\s*\\}\\s*,"
        + "\\s*\\{\\s*\"arrayInstance2\"\\s*:\\s*\"Array Test String 2\"\\s*\\}\\s*\\]\\s*,"
        + "\\s*\"jsonStringInstance\"\\s*:\\s*\"Test String\"\\s*,"
        + "\\s*\"jsonNumberInstance\"\\s*:\\s*2147483647\\s*,"
        + "\\s*\"jsonTrueInstance\"\\s*:\\s*true\\s*,"
        + "\\s*\"jsonFalseInstance\"\\s*:\\s*false\\s*,"
        + "\\s*\"jsonNullInstance\"\\s*:\\s*null\\s*" + "\\}\\s*\\}")) {
      fail(
          "Failed to marshal object with JsonObject attribute value.");
    }

    JsonObjectContainer unmarshalledObject = jsonb
        .fromJson("{ \"instance\" : { "
            + "\"jsonObjectInstance\" : { \"innerInstance\" : \"Inner Test String\" }, "
            + "\"jsonArrayInstance\" : [ "
            + "{ \"arrayInstance1\" : \"Array Test String 1\" }, "
            + "{ \"arrayInstance2\" : \"Array Test String 2\" } ], "
            + "\"jsonStringInstance\" : \"Test String\", "
            + "\"jsonNumberInstance\" : 2147483647, "
            + "\"jsonTrueInstance\" : true, "
            + "\"jsonFalseInstance\" : false, " + "\"jsonNullInstance\" : null "
            + "} }", JsonObjectContainer.class);
    if (!instance.toString()
        .equals(unmarshalledObject.getInstance().toString())) {
      fail(
          "Failed to unmarshal object with JsonObject attribute value.");
    }

    return; // passed
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
    String jsonString = jsonb.toJson(new JsonObjectContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\\}\\s*\\}")) {
      fail(
          "Failed to marshal object with empty JsonObject attribute value.");
    }

    JsonObjectContainer unmarshalledObject = jsonb
        .fromJson("{ \"instance\" : { } }", JsonObjectContainer.class);
    if (!instance.toString()
        .equals(unmarshalledObject.getInstance().toString())) {
      fail(
          "Failed to unmarshal object with empty JsonObject attribute value.");
    }

    return; // passed
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
        .add(Json.createObjectBuilder().add("arrayInstance1",
            "Array Test String 1"))
        .add(Json.createObjectBuilder().add("arrayInstance2",
            "Array Test String 2"))
        .build();

    String jsonString = jsonb.toJson(new JsonArrayContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\\["
        + "\\s*\\{\\s*\"arrayInstance1\"\\s*:\\s*\"Array Test String 1\"\\s*\\}\\s*,"
        + "\\s*\\{\\s*\"arrayInstance2\"\\s*:\\s*\"Array Test String 2\"\\s*\\}\\s*"
        + "\\]\\s*\\}")) {
      fail(
          "Failed to marshal object with JsonArray attribute value.");
    }

    JsonArrayContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ "
            + "{ \"arrayInstance1\" : \"Array Test String 1\" }, "
            + "{ \"arrayInstance2\" : \"Array Test String 2\" } " + "] }",
        JsonArrayContainer.class);
    if (!instance.toString()
        .equals(unmarshalledObject.getInstance().toString())) {
      fail(
          "Failed to unmarshal object with JsonArray attribute value.");
    }

    return; // passed
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
    String jsonString = jsonb.toJson(new JsonArrayContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\\]\\s*\\}")) {
      fail(
          "Failed to marshal object with empty JsonArray attribute value.");
    }

    JsonArrayContainer unmarshalledObject = jsonb
        .fromJson("{ \"instance\" : [ ] }", JsonArrayContainer.class);
    if (!instance.toString()
        .equals(unmarshalledObject.getInstance().toString())) {
      fail(
          "Failed to unmarshal object with empty JsonArray attribute value.");
    }

    return; // passed
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
        .add("jsonObjectInstance",
            Json.createObjectBuilder().add("innerInstance",
                "Inner Test String"))
        .add("jsonArrayInstance",
            Json.createArrayBuilder()
                .add(Json.createObjectBuilder().add("arrayInstance1",
                    "Array Test String 1"))
                .add(Json.createObjectBuilder().add("arrayInstance2",
                    "Array Test String 2")))
        .add("jsonStringInstance", "Test String")
        .add("jsonNumberInstance", Integer.MAX_VALUE)
        .add("jsonTrueInstance", JsonValue.TRUE)
        .add("jsonFalseInstance", JsonValue.FALSE)
        .add("jsonNullInstance", JsonValue.NULL).build();

    String jsonString = jsonb.toJson(new JsonStructureContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\\{"
        + "\\s*\"jsonObjectInstance\"\\s*:\\s*\\{\\s*\"innerInstance\"\\s*:\\s*\"Inner Test String\"\\s*\\}\\s*,"
        + "\\s*\"jsonArrayInstance\"\\s*:\\s*\\["
        + "\\s*\\{\\s*\"arrayInstance1\"\\s*:\\s*\"Array Test String 1\"\\s*\\}\\s*,"
        + "\\s*\\{\\s*\"arrayInstance2\"\\s*:\\s*\"Array Test String 2\"\\s*\\}\\s*\\]\\s*,"
        + "\\s*\"jsonStringInstance\"\\s*:\\s*\"Test String\"\\s*,"
        + "\\s*\"jsonNumberInstance\"\\s*:\\s*2147483647\\s*,"
        + "\\s*\"jsonTrueInstance\"\\s*:\\s*true\\s*,"
        + "\\s*\"jsonFalseInstance\"\\s*:\\s*false\\s*,"
        + "\\s*\"jsonNullInstance\"\\s*:\\s*null\\s*" + "\\}\\s*\\}")) {
      fail(
          "Failed to marshal object with JsonObject JsonStructure attribute value.");
    }

    JsonStructureContainer unmarshalledObject = jsonb
        .fromJson("{ \"instance\" : { "
            + "\"jsonObjectInstance\" : { \"innerInstance\" : \"Inner Test String\" }, "
            + "\"jsonArrayInstance\" : [ "
            + "{ \"arrayInstance1\" : \"Array Test String 1\" }, "
            + "{ \"arrayInstance2\" : \"Array Test String 2\" } ], "
            + "\"jsonStringInstance\" : \"Test String\", "
            + "\"jsonNumberInstance\" : 2147483647, "
            + "\"jsonTrueInstance\" : true, "
            + "\"jsonFalseInstance\" : false, " + "\"jsonNullInstance\" : null "
            + "} }", JsonStructureContainer.class);
    if (!instance.toString()
        .equals(unmarshalledObject.getInstance().toString())) {
      fail(
          "Failed to unmarshal object with JsonObject JsonStructure attribute value.");
    }

    return; // passed
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
        .add(Json.createObjectBuilder().add("arrayInstance1",
            "Array Test String 1"))
        .add(Json.createObjectBuilder().add("arrayInstance2",
            "Array Test String 2"))
        .build();

    String jsonString = jsonb.toJson(new JsonStructureContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\\["
        + "\\s*\\{\\s*\"arrayInstance1\"\\s*:\\s*\"Array Test String 1\"\\s*\\}\\s*,"
        + "\\s*\\{\\s*\"arrayInstance2\"\\s*:\\s*\"Array Test String 2\"\\s*\\}\\s*"
        + "\\]\\s*\\}")) {
      fail(
          "Failed to marshal object with JsonArray JsonStructure attribute value.");
    }

    JsonStructureContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ "
            + "{ \"arrayInstance1\" : \"Array Test String 1\" }, "
            + "{ \"arrayInstance2\" : \"Array Test String 2\" } " + "] }",
        JsonStructureContainer.class);
    if (!instance.toString()
        .equals(unmarshalledObject.getInstance().toString())) {
      fail(
          "Failed to unmarshal object with JsonArray JsonStructure attribute value.");
    }

    return; // passed
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
    JsonValue instance = Json.createObjectBuilder()
        .add("stringInstance", "Test String").build();
    String jsonString = jsonb.toJson(new JsonValueContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"stringInstance\"\\s*:\\s*\"Test String\"\\s*\\}\\s*\\}")) {
      fail(
          "Failed to marshal object with JsonValue attribute value.");
    }

    JsonValueContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : { \"stringInstance\" : \"Test String\" } }",
        JsonValueContainer.class);
    if (!instance.toString()
        .equals(unmarshalledObject.getInstance().toString())) {
      fail(
          "Failed to unmarshal object with JsonValue attribute value.");
    }

    return; // passed
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
    JsonString instance = Json.createObjectBuilder()
        .add("stringInstance", "Test String").build()
        .getJsonString("stringInstance");
    String jsonString = jsonb.toJson(new JsonStringContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString
        .matches("\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*\\}")) {
      fail(
          "Failed to marshal object with JsonString attribute value.");
    }

    JsonStringContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : \"Test String\" }", JsonStringContainer.class);
    if (!instance.toString()
        .equals(unmarshalledObject.getInstance().toString())) {
      fail(
          "Failed to unmarshal object with JsonString attribute value.");
    }

    return; // passed
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
    JsonNumber instance = Json.createObjectBuilder().add("intInstance", 0)
        .build().getJsonNumber("intInstance");
    String jsonString = jsonb.toJson(new JsonNumberContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*0\\s*\\}")) {
      fail(
          "Failed to marshal object with JsonNumber attribute value.");
    }

    JsonNumberContainer unmarshalledObject = jsonb
        .fromJson("{ \"instance\" : 0 }", JsonNumberContainer.class);
    if (!instance.toString()
        .equals(unmarshalledObject.getInstance().toString())) {
      fail(
          "Failed to unmarshal object with JsonNumber attribute value.");
    }

    return; // passed
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
    assertEquals("Failed to unmarshal null value to the JsonValue.NULL", JsonValue.NULL, unmarshalledValue.getInstance());
    unmarshalledValue = jsonb.fromJson("{}", JsonValueContainer.class);
    assertNull("No value should have been deserialized.", unmarshalledValue.getInstance());
  }

}
