/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.json.bind.api.jsonb;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.api.model.SimpleContainer;

/**
 * @test
 * @sources JsonbTest.java
 * @executeClass com.sun.ts.tests.jsonb.api.JsonbTest
 **/
public class JsonbTest {
  private Jsonb jsonb = JsonbBuilder.create();

  /*
   * @testName: testFromJsonStringClass
   *
   * @assertion_ids: JSONB:JAVADOC:1
   *
   * @test_Strategy: Assert that Jsonb.fromJson method with String and Class
   * arguments is working as expected
   */
  @Test
  public void testFromJsonStringClass() {
    SimpleContainer unmarshalledObject = jsonb
        .fromJson("{ \"instance\" : \"Test String\" }", SimpleContainer.class);
    if (!"Test String".equals(unmarshalledObject.getInstance())) {
      fail(
          "Failed to unmarshal using Jsonb.fromJson method with String and Class arguments.");
    }

    return; // passed
  }

  /*
   * @testName: testFromJsonStringType
   *
   * @assertion_ids: JSONB:JAVADOC:3
   *
   * @test_Strategy: Assert that Jsonb.fromJson method with String and Type
   * arguments is working as expected
   */
  @Test
  public void testFromJsonStringType() {
    SimpleContainer unmarshalledObject = jsonb
        .fromJson("{ \"instance\" : \"Test String\" }", new SimpleContainer() {
        }.getClass().getGenericSuperclass());
    if (!"Test String".equals(unmarshalledObject.getInstance())) {
      fail(
          "Failed to unmarshal using Jsonb.fromJson method with String and Type arguments.");
    }

    return; // passed
  }

  /*
   * @testName: testFromJsonReaderClass
   *
   * @assertion_ids: JSONB:JAVADOC:5
   *
   * @test_Strategy: Assert that Jsonb.fromJson method with Reader and Class
   * arguments is working as expected
   */
  @Test
  public void testFromJsonReaderClass() {
    try (ByteArrayInputStream stream = new ByteArrayInputStream(
        "{ \"instance\" : \"Test String\" }".getBytes(StandardCharsets.UTF_8));
        InputStreamReader reader = new InputStreamReader(stream)) {

      SimpleContainer unmarshalledObject = jsonb.fromJson(reader,
          SimpleContainer.class);
      if (!"Test String".equals(unmarshalledObject.getInstance())) {
        fail(
            "Failed to unmarshal using Jsonb.fromJson method with Reader and Class arguments.");
      }
    } catch (IOException e) {
      fail(e.getMessage());
    }

    return; // passed
  }

  /*
   * @testName: testFromJsonReaderType
   *
   * @assertion_ids: JSONB:JAVADOC:7
   *
   * @test_Strategy: Assert that Jsonb.fromJson method with Reader and Type
   * arguments is working as expected
   */
  @Test
  public void testFromJsonReaderType() {
    try (ByteArrayInputStream stream = new ByteArrayInputStream(
        "{ \"instance\" : \"Test String\" }".getBytes(StandardCharsets.UTF_8));
        InputStreamReader reader = new InputStreamReader(stream)) {

      SimpleContainer unmarshalledObject = jsonb.fromJson(reader,
          new SimpleContainer() {
          }.getClass().getGenericSuperclass());
      if (!"Test String".equals(unmarshalledObject.getInstance())) {
        fail(
            "Failed to unmarshal using Jsonb.fromJson method with Reader and Type arguments.");
      }
    } catch (IOException e) {
      fail(e.getMessage());
    }

    return; // passed
  }

  /*
   * @testName: testFromJsonStreamClass
   *
   * @assertion_ids: JSONB:JAVADOC:9
   *
   * @test_Strategy: Assert that Jsonb.fromJson method with InputStream and
   * Class arguments is working as expected
   */
  @Test
  public void testFromJsonStreamClass() {
    try (ByteArrayInputStream stream = new ByteArrayInputStream(
        "{ \"instance\" : \"Test String\" }"
            .getBytes(StandardCharsets.UTF_8))) {
      SimpleContainer unmarshalledObject = jsonb.fromJson(stream,
          SimpleContainer.class);
      if (!"Test String".equals(unmarshalledObject.getInstance())) {
        fail(
            "Failed to unmarshal using Jsonb.fromJson method with InputStream and Class arguments.");
      }
    } catch (IOException e) {
      fail(e.getMessage());
    }

    return; // passed
  }

  /*
   * @testName: testFromJsonStreamType
   *
   * @assertion_ids: JSONB:JAVADOC:11
   *
   * @test_Strategy: Assert that Jsonb.fromJson method with InputStream and
   * Class arguments is working as expected
   */
  @Test
  public void testFromJsonStreamType() {
    try (ByteArrayInputStream stream = new ByteArrayInputStream(
        "{ \"instance\" : \"Test String\" }"
            .getBytes(StandardCharsets.UTF_8))) {
      SimpleContainer unmarshalledObject = jsonb.fromJson(stream,
          new SimpleContainer() {
          }.getClass().getGenericSuperclass());
      if (!"Test String".equals(unmarshalledObject.getInstance())) {
        fail(
            "Failed to unmarshal using Jsonb.fromJson method with InputStream and Type arguments.");
      }
    } catch (IOException e) {
      fail(e.getMessage());
    }

    return; // passed
  }

  /*
   * @testName: testToJsonObject
   *
   * @assertion_ids: JSONB:JAVADOC:13
   *
   * @test_Strategy: Assert that Jsonb.toJson method with Object argument is
   * working as expected
   */
  @Test
  public void testToJsonObject() {
    String jsonString = jsonb.toJson(new SimpleContainer());
    if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\"Test\"\\s*}")) {
      fail(
          "Failed to marshal using Jsonb.toJson method with Object argument.");
    }

    return; // passed
  }

  /*
   * @testName: testToJsonObjectType
   *
   * @assertion_ids: JSONB:JAVADOC:15
   *
   * @test_Strategy: Assert that Jsonb.toJson method with Object and Type
   * arguments is working as expected
   */
  @Test
  public void testToJsonObjectType() {
    String jsonString = jsonb.toJson(new SimpleContainer(),
        new SimpleContainer() {
        }.getClass().getGenericSuperclass());
    if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\"Test\"\\s*}")) {
      fail(
          "Failed to marshal using Jsonb.toJson method with Object and Type arguments.");
    }

    return; // passed
  }

  /*
   * @testName: testToJsonObjectWriter
   *
   * @assertion_ids: JSONB:JAVADOC:17
   *
   * @test_Strategy: Assert that Jsonb.toJson method with Object and Writer
   * arguments is working as expected
   */
  @Test
  public void testToJsonObjectWriter() {
    try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(stream)) {

      jsonb.toJson(new SimpleContainer(), writer);
      String jsonString = new String(stream.toByteArray(),
          StandardCharsets.UTF_8);
      if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\"Test\"\\s*}")) {
        fail(
            "Failed to marshal using Jsonb.toJson method with Object and Writer arguments.");
      }
    } catch (IOException e) {
      fail(e.getMessage());
    }

    return; // passed
  }

  /*
   * @testName: testToJsonObjectTypeWriter
   *
   * @assertion_ids: JSONB:JAVADOC:19
   *
   * @test_Strategy: Assert that Jsonb.toJson method with Object, Type and
   * Writer arguments is working as expected
   */
  @Test
  public void testToJsonObjectTypeWriter() {
    try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(stream)) {

      jsonb.toJson(new SimpleContainer(), new SimpleContainer() {
      }.getClass().getGenericSuperclass(), writer);
      String jsonString = new String(stream.toByteArray(),
          StandardCharsets.UTF_8);
      if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\"Test\"\\s*}")) {
        fail(
            "Failed to marshal using Jsonb.toJson method with Object, Type and Writer arguments.");
      }
    } catch (IOException e) {
      fail(e.getMessage());
    }

    return; // passed
  }

  /*
   * @testName: testToJsonObjectStream
   *
   * @assertion_ids: JSONB:JAVADOC:21
   *
   * @test_Strategy: Assert that Jsonb.toJson method with Object and
   * OutputStream arguments is working as expected
   */
  @Test
  public void testToJsonObjectStream() {
    try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
      jsonb.toJson(new SimpleContainer(), stream);
      String jsonString = new String(stream.toByteArray(),
          StandardCharsets.UTF_8);
      if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\"Test\"\\s*}")) {
        fail(
            "Failed to marshal using Jsonb.toJson method with Object and OutputStream arguments.");
      }
    } catch (IOException e) {
      fail(e.getMessage());
    }

    return; // passed
  }

  /*
   * @testName: testToJsonObjectTypeStream
   *
   * @assertion_ids: JSONB:JAVADOC:23
   *
   * @test_Strategy: Assert that Jsonb.toJson method with Object, Type and
   * OutputStream arguments is working as expected
   */
  @Test
  public void testToJsonObjectTypeStream() {
    try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
      jsonb.toJson(new SimpleContainer(), new SimpleContainer() {
      }.getClass().getGenericSuperclass(), stream);
      String jsonString = new String(stream.toByteArray(),
          StandardCharsets.UTF_8);
      if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\"Test\"\\s*}")) {
        fail(
            "Failed to marshal using Jsonb.toJson method with Object, Type and OutputStream arguments.");
      }
    } catch (IOException e) {
      fail(e.getMessage());
    }

    return; // passed
  }
}
