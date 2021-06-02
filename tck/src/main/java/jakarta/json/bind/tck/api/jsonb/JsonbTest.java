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

package jakarta.json.bind.tck.api.jsonb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.tck.api.model.SimpleContainer;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

/**
 * @test
 * @sources JsonbTest.java
 * @executeClass com.sun.ts.tests.jsonb.api.JsonbTest
 **/
public class JsonbTest {

  private static final String TEST_STRING = "Test String";
  private static final String TEST_JSON = "{ \"instance\" : \"" + TEST_STRING + "\" }";
  private static final byte[] TEST_JSON_BYTE = TEST_JSON.getBytes(StandardCharsets.UTF_8);

  private static final String MATCHING_PATTERN = "\\{\\s*\"instance\"\\s*:\\s*\"Test\"\\s*}";

  private final Jsonb jsonb = JsonbBuilder.create();

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
    SimpleContainer unmarshalledObject = jsonb.fromJson(TEST_JSON, SimpleContainer.class);
    assertThat("Failed to unmarshal using Jsonb.fromJson method with String and Class arguments.",
               unmarshalledObject.getInstance(), is(TEST_STRING));
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
    SimpleContainer unmarshalledObject = jsonb.fromJson(TEST_JSON, new SimpleContainer() {}.getClass().getGenericSuperclass());
    assertThat("Failed to unmarshal using Jsonb.fromJson method with String and Type arguments.",
               unmarshalledObject.getInstance(), is(TEST_STRING));
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
  public void testFromJsonReaderClass() throws IOException {
    try (ByteArrayInputStream stream = new ByteArrayInputStream(TEST_JSON_BYTE);
            InputStreamReader reader = new InputStreamReader(stream)) {
      SimpleContainer unmarshalledObject = jsonb.fromJson(reader, SimpleContainer.class);
      assertThat("Failed to unmarshal using Jsonb.fromJson method with Reader and Class arguments.",
                 unmarshalledObject.getInstance(), is(TEST_STRING));
    }
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
  public void testFromJsonReaderType() throws IOException {
    try (ByteArrayInputStream stream = new ByteArrayInputStream(TEST_JSON_BYTE);
            InputStreamReader reader = new InputStreamReader(stream)) {
      SimpleContainer unmarshalledObject = jsonb.fromJson(reader, new SimpleContainer() {}.getClass().getGenericSuperclass());
      assertThat("Failed to unmarshal using Jsonb.fromJson method with Reader and Type arguments.",
                 unmarshalledObject.getInstance(), is(TEST_STRING));
    }
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
  public void testFromJsonStreamClass() throws IOException {
    try (ByteArrayInputStream stream = new ByteArrayInputStream(TEST_JSON_BYTE)) {
      SimpleContainer unmarshalledObject = jsonb.fromJson(stream, SimpleContainer.class);
      assertThat("Failed to unmarshal using Jsonb.fromJson method with InputStream and Class arguments.",
                 unmarshalledObject.getInstance(), is(TEST_STRING));
    }
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
  public void testFromJsonStreamType() throws IOException {
    try (ByteArrayInputStream stream = new ByteArrayInputStream(TEST_JSON_BYTE)) {
      SimpleContainer unmarshalledObject = jsonb.fromJson(stream, new SimpleContainer() {}.getClass().getGenericSuperclass());
      assertThat("Failed to unmarshal using Jsonb.fromJson method with InputStream and Type arguments.",
                 unmarshalledObject.getInstance(), is(TEST_STRING));
    }
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
    assertThat("Failed to marshal using Jsonb.toJson method with Object argument.",
               jsonString, matchesPattern(MATCHING_PATTERN));
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
    String jsonString = jsonb.toJson(new SimpleContainer(), new SimpleContainer() { }.getClass().getGenericSuperclass());
    assertThat("Failed to marshal using Jsonb.toJson method with Object and Type arguments.",
               jsonString, matchesPattern(MATCHING_PATTERN));
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
  public void testToJsonObjectWriter() throws IOException {
    try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(stream)) {
      jsonb.toJson(new SimpleContainer(), writer);
      String jsonString = new String(stream.toByteArray(), StandardCharsets.UTF_8);
      assertThat("Failed to marshal using Jsonb.toJson method with Object and Writer arguments.",
                 jsonString, matchesPattern(MATCHING_PATTERN));
    }
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
  public void testToJsonObjectTypeWriter() throws IOException {
    try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(stream)) {
      jsonb.toJson(new SimpleContainer(), new SimpleContainer() {}.getClass().getGenericSuperclass(), writer);
      String jsonString = new String(stream.toByteArray(), StandardCharsets.UTF_8);
      assertThat("Failed to marshal using Jsonb.toJson method with Object, Type and Writer arguments.",
                 jsonString, matchesPattern(MATCHING_PATTERN));
    }
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
  public void testToJsonObjectStream() throws IOException {
    try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
      jsonb.toJson(new SimpleContainer(), stream);
      String jsonString = new String(stream.toByteArray(), StandardCharsets.UTF_8);
      assertThat("Failed to marshal using Jsonb.toJson method with Object and OutputStream arguments.",
                 jsonString, matchesPattern(MATCHING_PATTERN));
    }
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
  public void testToJsonObjectTypeStream() throws IOException {
    try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
      jsonb.toJson(new SimpleContainer(), new SimpleContainer() {}.getClass().getGenericSuperclass(), stream);
      String jsonString = new String(stream.toByteArray(), StandardCharsets.UTF_8);
      assertThat("Failed to marshal using Jsonb.toJson method with Object, Type and OutputStream arguments.",
                 jsonString, matchesPattern(MATCHING_PATTERN));
    }
  }
}
