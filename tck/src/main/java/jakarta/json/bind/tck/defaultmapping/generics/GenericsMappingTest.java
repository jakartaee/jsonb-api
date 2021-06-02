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

package jakarta.json.bind.tck.defaultmapping.generics;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.tck.defaultmapping.generics.model.CollectionContainer;
import jakarta.json.bind.tck.defaultmapping.generics.model.GenericContainer;
import jakarta.json.bind.tck.defaultmapping.generics.model.MultipleBoundsContainer;
import jakarta.json.bind.tck.defaultmapping.generics.model.NumberContainer;
import jakarta.json.bind.tck.defaultmapping.generics.model.StringContainer;
import jakarta.json.bind.tck.defaultmapping.generics.model.WildcardContainer;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

/**
 * @test
 * @sources GenericsMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.generics.GenericsMappingTest
 **/
public class GenericsMappingTest {
    
  private final Jsonb jsonb = JsonbBuilder.create();

  /*
   * @testName: testClassInformationOnRuntime
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.17-1; JSONB:SPEC:JSB-3.17-3;
   * JSONB:SPEC:JSB-3.17.1-1; JSONB:SPEC:JSB-3.17.1-15
   *
   * @test_Strategy: Assert that passing Type information on runtime is handled
   * as expected
   */
  @Test
  public void testClassInformationOnRuntime() {
    String jsonString = jsonb.toJson(new GenericContainer<String>() {{
      setInstance("Test String");
    }});
    assertThat("Failed to marshal generic object with String attribute value.",
               jsonString, matchesPattern("\\{\\s*\"instance\"\\s*\\:\\s*\"Test String\"\\s*\\}"));

    Type runtimeType = new GenericContainer<String>() { }.getClass().getGenericSuperclass();
    GenericContainer<String> unmarshalledObject = jsonb.fromJson("{ \"instance\" : \"Test String\" }", runtimeType);
    assertThat("Failed to unmarshal generic object with String attribute value.",
               unmarshalledObject.getInstance(), is("Test String"));
  }

  /*
   * @testName: testClassFileAvailable
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.17-1; JSONB:SPEC:JSB-3.17-2;
   * JSONB:SPEC:JSB-3.17.1-1; JSONB:SPEC:JSB-3.17.1-5; JSONB:SPEC:JSB-3.17.1-6
   *
   * @test_Strategy: Assert that static type information is handled as expected
   */
  @Test
  public void testClassFileAvailable() {
    String jsonString = jsonb.toJson(new GenericContainer<String>() {{
      setInstance("Test String");
    }});
    assertThat("Failed to marshal generic object with String attribute value.",
               jsonString, matchesPattern("\\{\\s*\"instance\"\\s*\\:\\s*\"Test String\"\\s*\\}"));

    GenericContainer<String> unmarshalledObject = jsonb.fromJson("{ \"instance\" : \"Test String\" }", StringContainer.class);
    assertThat("Failed to unmarshal generic object with String attribute value.",
               unmarshalledObject.getInstance(), is("Test String"));
  }

  /*
   * @testName: testRawTypeInformation
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.17-1; JSONB:SPEC:JSB-3.17.1-1;
   * JSONB:SPEC:JSB-3.17.1-3; JSONB:SPEC:JSB-3.17.1-4; JSONB:SPEC:JSB-3.17.1-8
   *
   * @test_Strategy: Assert that raw type information is handled as expected
   */
  @Test
  public void testRawTypeInformation() {
    final List<String> list = Arrays.asList("Test 1", "Test 2");
    String jsonString = jsonb.toJson(new CollectionContainer() {{
      setInstance(list);
    }});
    assertThat("Failed to marshal object with raw List attribute.",
               jsonString, matchesPattern("\\{\\s*\"instance\"\\s*\\:\\s*\\[\\s*\"Test 1\"\\s*,\\s*\"Test 2\"\\s*\\]\\s*\\}"));

    CollectionContainer unmarshalledObject = jsonb.fromJson("{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }",
                                                                 CollectionContainer.class);
    assertThat("Failed to unmarshal object with raw List type attribute.",
               unmarshalledObject.getInstance(), is(list));
  }

  /*
   * @testName: testNoTypeInformation
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.17-1; JSONB:SPEC:JSB-3.17.1-1;
   * JSONB:SPEC:JSB-3.17.1-2; JSONB:SPEC:JSB-3.17.1-9; JSONB:SPEC:JSB-3.17.1-14
   *
   * @test_Strategy: Assert that if no type information is provided, type is
   * treated as java.lang.Object
   */
  @SuppressWarnings("unchecked")
  @Test
  public void testNoTypeInformation() {
    String jsonString = jsonb.toJson(new GenericContainer<String>() {{
      setInstance("Test String");
    }});
    assertThat("Failed to marshal generic object with String attribute value.",
               jsonString, matchesPattern("\\{\\s*\"instance\"\\s*\\:\\s*\"Test String\"\\s*\\}"));

    GenericContainer<?> unmarshalledObject = jsonb.fromJson("{ \"instance\" : {\"value\":\"Test String\" } }",
                                                            GenericContainer.class);
    String validationMessage = "Failed to unmarshal generic object without type information with String attribute value.";
    Object evaluatedInstance = unmarshalledObject.getInstance();
    assertThat(validationMessage, evaluatedInstance, instanceOf(Map.class));
    Map<String, Object> map = (Map<String, Object>) evaluatedInstance;
    assertThat(validationMessage, map.size(), is(1));
    assertThat(validationMessage, map, hasEntry("value", "Test String"));
  }

  /*
   * @testName: testBoundedTypeInformation
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.17-1; JSONB:SPEC:JSB-3.17.1-5;
   * JSONB:SPEC:JSB-3.17.1-7
   *
   * @test_Strategy: Assert that bounded type information is treated as expected
   */
  @Test
  public void testBoundedTypeInformation() {
    String jsonString = jsonb.toJson(new NumberContainer<Integer>() {{
      setInstance(Integer.MAX_VALUE);
    }});
    assertThat("Failed to marshal object with bounded Number attribute.",
               jsonString, matchesPattern("\\{\\s*\"instance\"\\s*\\:\\s*" + Integer.MAX_VALUE + "\\s*\\}"));
    Type runtimeType = new NumberContainer<Integer>() { }.getClass().getGenericSuperclass();
    NumberContainer<Integer> unmarshalledObject = jsonb.fromJson("{ \"instance\" : " + Integer.MAX_VALUE + " }", runtimeType);
    assertThat("Failed to unmarshal object with bounded Number attribute.",
               unmarshalledObject.getInstance(), is(Integer.MAX_VALUE));
  }

  /*
   * @testName: testMultipleBoundsTypeInformation
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.17-1; JSONB:SPEC:JSB-3.17.1-5;
   * JSONB:SPEC:JSB-3.17.1-7; JSONB:SPEC:JSB-3.17.1-10;
   * JSONB:SPEC:JSB-3.17.1-11; JSONB:SPEC:JSB-3.17.1-12
   *
   * @test_Strategy: Assert that when multiple bounds exist, the most specific
   * type is used
   */
  @Test
  public void testMultipleBoundsTypeInformation() {
    final LinkedList<String> list = new LinkedList<>(Arrays.asList("Test 1", "Test 2"));
    MultipleBoundsContainer<LinkedList<String>> container = new MultipleBoundsContainer<>();
    container.setInstance(new ArrayList<>());
    container.getInstance().add(list);

    final Type type = new MultipleBoundsContainer<LinkedList<String>>() { }.getClass().getGenericSuperclass();
    String jsonString = jsonb.toJson(container, type);
    assertThat("Failed to marshal object with multiple bounded attribute.",
               jsonString,
               matchesPattern("\\{\\s*\"instance\"\\s*\\:\\s*\\[\\[\\s*\"Test 1\"\\s*,\\s*\"Test 2\"\\s*\\]\\]\\s*\\}"));

    String toDeserialize = "{ \"instance\" : [[ \"Test 1\", \"Test 2\" ]] }";
    MultipleBoundsContainer<LinkedList<String>> unmarshalledObject = jsonb.fromJson(toDeserialize, type);
    assertThat("Failed to unmarshal object with multiple bounded attribute.",
               unmarshalledObject.getInstance(), is(container.getInstance()));
  }

  /*
   * @testName: testWildcardTypeInformation
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.17-1; JSONB:SPEC:JSB-3.17.1-5;
   * JSONB:SPEC:JSB-3.17.1-13
   *
   * @test_Strategy: Assert that wildcard type is handled as java.lang.Object
   */
  @Test
  public void testWildcardTypeInformation() {
    final List<String> list = Arrays.asList("Test 1", "Test 2");
    String jsonString = jsonb.toJson(new WildcardContainer() {{
      setInstance(list);
    }});
    assertThat("Failed to marshal object with unbound collection attribute.",
               jsonString,
               matchesPattern("\\{\\s*\"instance\"\\s*\\:\\s*\\[\\s*\"Test 1\"\\s*,\\s*\"Test 2\"\\s*\\]\\s*\\}"));

    WildcardContainer unmarshalledObject = jsonb.fromJson("{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }",
                                                          WildcardContainer.class);
    assertThat("Failed to unmarshal object with unbound collection attribute.",
               unmarshalledObject.getInstance(), is(list));
  }
}
