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

package jakarta.json.bind.tck.api.annotation;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.tck.api.model.SimpleAnnotatedAdaptedContainer;
import jakarta.json.bind.tck.api.model.SimpleAnnotatedDateContainer;
import jakarta.json.bind.tck.api.model.SimpleAnnotatedDoubleContainer;
import jakarta.json.bind.tck.api.model.SimpleAnnotatedNillableContainer;
import jakarta.json.bind.tck.api.model.SimpleAnnotatedNillablePropertyContainer;
import jakarta.json.bind.tck.api.model.SimpleAnnotatedPropertyOrderContainer;
import jakarta.json.bind.tck.api.model.SimpleAnnotatedPropertyVisibilityContainer;
import jakarta.json.bind.tck.api.model.SimpleAnnotatedSerializedArrayContainer;
import jakarta.json.bind.tck.api.model.SimpleAnnotatedSerializedContainer;
import jakarta.json.bind.tck.api.model.SimpleContainer;
import jakarta.json.bind.tck.api.model.SimplePartiallyAnnotatedPropertyOrderContainer;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

/**
 * @test
 * @sources AnnotationTest.java
 * @executeClass com.sun.ts.tests.jsonb.api.AnnotationTest
 **/
public class AnnotationTest {

  private final Jsonb jsonb = JsonbBuilder.create();

  /*
   * @testName: testJsonbDateFormat
   *
   * @assertion_ids: JSONB:JAVADOC:57; JSONB:JAVADOC:58
   *
   * @test_Strategy: Assert that JsonbDateFormat annotation can be used to
   * customize date format
   */
  @Test
  public void testJsonbDateFormat() {
    String jsonString = jsonb.toJson(new SimpleAnnotatedDateContainer());
    assertThat(jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\"Do(\\.?), 01 Jan(\\.?) 1970\"\\s*}"));
  }

  /*
   * @testName: testJsonbNillable
   *
   * @assertion_ids: JSONB:JAVADOC:59
   *
   * @test_Strategy: Assert that JsonbNillable annotation can be used to enable
   * serialization of null values
   */
  @Test
  public void testJsonbNillable() {
    String jsonString = jsonb.toJson(new SimpleAnnotatedNillableContainer());
    assertThat("Failed to enable serialization of null values using JsonbNillable annotation.",
               jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*null\\s*}"));
  }

  /*
   * @testName: testJsonbNumberFormat
   *
   * @assertion_ids: JSONB:JAVADOC:60; JSONB:JAVADOC:61
   *
   * @test_Strategy: Assert that JsonbNumberFormat annotation can be used to
   * customize number format
   */
  @Test
  public void testJsonbNumberFormat() {
    String jsonString = jsonb.toJson(new SimpleAnnotatedDoubleContainer());
    assertThat("Failed to customize number format using JsonbNumberFormat annotation.",
               jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\"123.456,8\"\\s*}"));
  }

  /*
   * @testName: testJsonbProperty
   *
   * @assertion_ids: JSONB:JAVADOC:62; JSONB:JAVADOC:63
   *
   * @test_Strategy: Assert that JsonbProperty annotation can be used to
   * customize property name and enable serialization of null values
   */
  @Test
  public void testJsonbProperty() {
    String jsonString = jsonb.toJson(new SimpleAnnotatedNillablePropertyContainer());
    assertThat("Failed to customize property name and enable serialization of null values using JsonbProperty annotation.",
               jsonString, matchesPattern("\\{\\s*\"nillableInstance\"\\s*:\\s*null\\s*}"));
  }

  /*
   * @testName: testJsonbPropertyOrder
   *
   * @assertion_ids: JSONB:JAVADOC:64
   *
   * @test_Strategy: Assert that JsonbPropertyOrder annotation can be used to
   * customize the order in which fields will be serialized
   */
  @Test
  public void testJsonbPropertyOrder() {
    String jsonString = jsonb.toJson(new SimpleAnnotatedPropertyOrderContainer());
    assertThat("Failed to customize the order in which fields will be serialized using JsonbPropertyOrder annotation.",
               jsonString, matchesPattern("\\{\\s*\"secondInstance\"\\s*:\\s*\"Second String\"\\s*,"
                                                  + "\\s*\"firstInstance\"\\s*:\\s*\"First String\"\\s*}"));
  }

  /*
   * @testName: testJsonbPropertyPartialOrder
   *
   * @assertion_ids: JSONB:JAVADOC:64
   *
   * @test_Strategy: In that case, properties included in annotation declaration
   * will be serialized first (in defined order), followed by any properties not
   * included in the definition. The order of properties not included in the
   * definition is not guaranteed
   */
  @Test
  public void testJsonbPropertyPartialOrder() {
    String jsonString = jsonb.toJson(new SimplePartiallyAnnotatedPropertyOrderContainer());
    assertThat("Failed to order the fields partially defined using JsonbPropertyOrder annotation.",
               jsonString, matchesPattern("\\{\\s*\"thirdInstance\"\\s*:\\s*\"Third String\"\\s*,"
                                                  + "\\s*\"fourthInstance\"\\s*:\\s*\"Fourth String\".*}"));
  }

  /*
   * @testName: testJsonbTypeAdapter
   *
   * @assertion_ids: JSONB:JAVADOC:65
   *
   * @test_Strategy: Assert that JsonbTypeAdapter annotation can be used to
   * configure a JsonbAdapter implementation to provide custom mapping
   */
  @Test
  public void testJsonbTypeAdapter() {
    String jsonString = jsonb.toJson(new SimpleAnnotatedAdaptedContainer() {
      {
        setInstance(new SimpleContainer() {
          {
            setInstance("Test String");
          }
        });
      }
    });
    assertThat("Failed to configure a JsonbAdapter implementation to provide custom mapping using JsonbTypeAdapter annotation.",
               jsonString,
               matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"instance\"\\s*:\\s*\"Test String Adapted\"\\s*}\\s*}"));
  }

  /*
   * @testName: testJsonbTypeSerializer
   *
   * @assertion_ids: JSONB:JAVADOC:66; JSONB:JAVADOC:67; JSONB:JAVADOC:72;
   * JSONB:JAVADOC:74; JSONB:JAVADOC:75; JSONB:JAVADOC:76
   *
   * @test_Strategy: Assert that JsonbTypeSerializer and JsonbTypeDeserializer
   * annotations can be used to configure a JsonbSerializer and
   * JsonbDeserializer implementation to provide custom mapping
   */
  @Test
  public void testJsonbTypeSerializer() {
    SimpleAnnotatedSerializedContainer container = new SimpleAnnotatedSerializedContainer();
    SimpleContainer instance = new SimpleContainer();
    instance.setInstance("Test String");
    container.setInstance(instance);

    String jsonString = jsonb.toJson(container);
    assertThat("Failed to configure a JsonbSerializer implementation to "
                       + "provide custom mapping using JsonbTypeSerializer annotation.",
               jsonString,
               matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"instance\"\\s*:\\s*\"Test String Serialized\"\\s*}\\s*}"));

    SimpleAnnotatedSerializedContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : { \"instance\" : \"Test String\" } }",
        SimpleAnnotatedSerializedContainer.class);
    assertThat("Failed to configure a JsonbDeserializer implementation to "
                       + "provide custom mapping using JsonbTypeDeserializer annotation.",
               unmarshalledObject.getInstance().getInstance(), is("Test String Deserialized"));
  }

  /*
   * @testName: testJsonbArrayTypeSerializer
   *
   * @assertion_ids: JSONB:JAVADOC:66; JSONB:JAVADOC:67; JSONB:JAVADOC:73;
   * JSONB:JAVADOC:74; JSONB:JAVADOC:75; JSONB:JAVADOC:77
   *
   * @test_Strategy: Assert that JsonbTypeSerializer and JsonbTypeDeserializer
   * annotations can be used to configure a JsonbSerializer and
   * JsonbDeserializer implementation to provide custom mapping for array type
   */
  @Test
  public void testJsonbArrayTypeSerializer() {
    SimpleAnnotatedSerializedArrayContainer container = new SimpleAnnotatedSerializedArrayContainer();
    SimpleContainer instance1 = new SimpleContainer();
    instance1.setInstance("Test String 1");
    SimpleContainer instance2 = new SimpleContainer();
    instance2.setInstance("Test String 2");
    container.setInstance(new SimpleContainer[] { instance1, instance2 });

    String jsonString = jsonb.toJson(container);
    assertThat("Failed to configure a JsonbSerializer implementation to "
                       + "provide custom mapping using JsonbTypeSerializer annotation.",
               jsonString,
               matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\\{\\s*\"instance\"\\s*:\\s*\"Test String 1\"\\s*}\\s*,"
                                      + "\\s*\\{\\s*\"instance\"\\s*:\\s*\"Test String 2\"\\s*}\\s*]\\s*}"));

    SimpleAnnotatedSerializedArrayContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ { \"instance\" : \"Test String 1\" }, { \"instance\" : \"Test String 2\" } ] }",
        SimpleAnnotatedSerializedArrayContainer.class);
    assertThat("Failed to configure a JsonbDeserializer implementation to "
                       + "provide custom mapping using JsonbTypeDeserializer annotation.",
               unmarshalledObject.getInstance()[0].getInstance(), is("Test String 1"));
  }

  /*
   * @testName: testJsonbVisibility
   *
   * @assertion_ids: JSONB:JAVADOC:68; JSONB:JAVADOC:70; JSONB:JAVADOC:71
   *
   * @test_Strategy: Assert that JsonbVisibility annotation can be used to
   * customize field visibility
   */
  @Test
  public void testJsonbVisibility() {
    String jsonString = jsonb.toJson(new SimpleAnnotatedPropertyVisibilityContainer());
    assertThat("Failed to customize fields visibility using JsonbVisibility annotation.",
               jsonString, matchesPattern("\\{\\s*\"secondInstance\"\\s*:\\s*\"Second String\"\\s*}"));
  }
}
