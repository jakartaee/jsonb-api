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

package jakarta.json.bind.tck.customizedmapping.propertynames;

import static org.junit.Assert.fail;

import java.lang.invoke.MethodHandles;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.JsonbException;
import jakarta.json.bind.config.PropertyNamingStrategy;
import jakarta.json.bind.tck.customizedmapping.propertynames.model.DuplicateNameContainer;
import jakarta.json.bind.tck.customizedmapping.propertynames.model.PropertyNameCustomizationAccessorsContainer;
import jakarta.json.bind.tck.customizedmapping.propertynames.model.PropertyNameCustomizationContainer;
import jakarta.json.bind.tck.customizedmapping.propertynames.model.StringContainer;
import jakarta.json.bind.tck.customizedmapping.propertynames.model.TransientAnnotatedPropertyContainer;
import jakarta.json.bind.tck.customizedmapping.propertynames.model.TransientGetterAnnotatedPropertyContainer;
import jakarta.json.bind.tck.customizedmapping.propertynames.model.TransientGetterPlusCustomizationAnnotatedFieldContainer;
import jakarta.json.bind.tck.customizedmapping.propertynames.model.TransientGetterPlusCustomizationAnnotatedGetterContainer;
import jakarta.json.bind.tck.customizedmapping.propertynames.model.TransientPlusCustomizationAnnotatedGetterContainer;
import jakarta.json.bind.tck.customizedmapping.propertynames.model.TransientPlusCustomizationAnnotatedPropertyContainer;
import jakarta.json.bind.tck.customizedmapping.propertynames.model.TransientPlusCustomizationAnnotatedSetterContainer;
import jakarta.json.bind.tck.customizedmapping.propertynames.model.TransientPropertyContainer;
import jakarta.json.bind.tck.customizedmapping.propertynames.model.TransientSetterAnnotatedPropertyContainer;
import jakarta.json.bind.tck.customizedmapping.propertynames.model.TransientSetterPlusCustomizationAnnotatedFieldContainer;
import jakarta.json.bind.tck.customizedmapping.propertynames.model.TransientSetterPlusCustomizationAnnotatedSetterContainer;

/**
 * @test
 * @sources PropertyNameCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.propertynames.PropertyNameCustomizationTest
 **/
@RunWith(Arquillian.class)
public class PropertyNameCustomizationTest {
    
    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, MethodHandles.lookup().lookupClass().getPackage().getName());
    }
    
  private final Jsonb jsonb = JsonbBuilder.create();

  /*
   * @testName: testTransientField
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.1-1
   *
   * @test_Strategy: Assert that transient fields are ignored during
   * marshalling/unmarshalling
   */
  @Test
  public void testTransientField() {
    String jsonString = jsonb.toJson(new TransientPropertyContainer() {
      {
        setInstance("String Value");
      }
    });
    if (!jsonString.matches("\\{\\s*\\}")) {
      fail(
          "Failed to ignore transient property during marshalling.");
    }

    TransientPropertyContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : \"Test String\" }", TransientPropertyContainer.class);
    if (unmarshalledObject.getInstance() != null) {
      fail(
          "Failed to ignore transient property during unmarshalling.");
    }

    return; // passed
  }

  /*
   * @testName: testTransientAnnotatedField
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.1-1
   *
   * @test_Strategy: Assert that fields annotated as JsonbTransient are ignored
   * during marshalling/unmarshalling
   */
  @Test
  public void testTransientAnnotatedField() {
    String jsonString = jsonb.toJson(new TransientAnnotatedPropertyContainer() {
      {
        setInstance("String Value");
      }
    });
    if (!jsonString.matches("\\{\\s*\\}")) {
      fail(
          "Failed to ignore JsonbTransient property during marshalling.");
    }

    TransientAnnotatedPropertyContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : \"Test String\" }",
        TransientAnnotatedPropertyContainer.class);
    if (unmarshalledObject.getInstance() != null) {
      fail(
          "Failed to ignore JsonbTransient property during unmarshalling.");
    }

    return; // passed
  }

  /*
   * @testName: testTransientAnnotatedGetter
   * 
   * @assertion_ids: JSONB:SPEC:JSB-4.1.1-1
   * 
   * @test_Strategy: Assert that fields with getters annotated as JsonbTransient
   * are ignored during marshalling
   */
  @Test
  public void testTransientAnnotatedGetter() {
    String jsonString = jsonb
        .toJson(new TransientGetterAnnotatedPropertyContainer() {
          {
            setInstance("String Value");
          }
        });
    if (!jsonString.matches("\\{\\s*\\}")) {
      fail(
          "Failed to ignore @JsonbTransient on getter during marshalling.");
    }
  }

  /*
   * @testName: testTransientAnnotatedSetter
   * 
   * @assertion_ids: JSONB:SPEC:JSB-4.1.1-1
   * 
   * @test_Strategy: Assert that fields with setters annotated as JsonbTransient
   * are ignored during unmarshalling
   */
  @Test
  public void testTransientAnnotatedSetter() {
    TransientSetterAnnotatedPropertyContainer unmarshalledObject = jsonb
        .fromJson("{ \"instance\" : \"Test String\" }",
            TransientSetterAnnotatedPropertyContainer.class);
    if (unmarshalledObject.getInstance() != null) {
      fail(
          "Failed to ignore @JsonbTransient on setter during unmarshalling.");
    }
  }

  /*
   * @testName: testTransientPlusCustomizationAnnotatedField
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.1-2
   *
   * @test_Strategy: Assert that JsonbException is thrown for fields annotated
   * as both JsonbTransient and other Jsonb customization annotations
   */
  @Test
  public void testTransientPlusCustomizationAnnotatedField() {
    try {
      jsonb.toJson(new TransientPlusCustomizationAnnotatedPropertyContainer());
      fail(
          "JsonbException not thrown for property annotated with both JsonbTransient and other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        fail(
            "JsonbException expected for property annotated with both JsonbTransient and other Jsonb customization annotation.");
      }
    }

    try {
      jsonb.fromJson("{ \"instance\" : \"Test String\" }",
          TransientPlusCustomizationAnnotatedPropertyContainer.class);
      fail(
          "JsonbException not thrown for property annotated with both JsonbTransient and other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        fail(
            "JsonbException expected for property annotated with both JsonbTransient and other Jsonb customization annotation.");
      }
    }

    return; // passed
  }

  /*
   * @testName: testTransientPlusCustomizationAnnotatedGetter
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.1-2
   *
   * @test_Strategy: Assert that JsonbException is thrown for fields annotated
   * as both JsonbTransient and other Jsonb customization annotations
   */
  @Test
  public void testTransientPlusCustomizationAnnotatedGetter() {
    try {
      jsonb.toJson(new TransientPlusCustomizationAnnotatedGetterContainer());
      fail(
          "JsonbException not thrown for property annotated with JsonbTransient and getter with other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        fail(
            "JsonbException expected for property annotated with JsonbTransient and getter with other Jsonb customization annotation.");
      }
    }

    try {
      jsonb.fromJson("{ \"instance\" : \"Test String\" }",
          TransientPlusCustomizationAnnotatedGetterContainer.class);
      fail(
          "JsonbException not thrown for property annotated with JsonbTransient and getter with other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        fail(
            "JsonbException expected for property annotated with JsonbTransient and getter with other Jsonb customization annotation.");
      }
    }
  }

  /*
   * @testName: testTransientPlusCustomizationAnnotatedSetter
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.1-2
   *
   * @test_Strategy: Assert that JsonbException is thrown for fields annotated
   * as both JsonbTransient and other Jsonb customization annotations
   */
  @Test
  public void testTransientPlusCustomizationAnnotatedSetter() {
    try {
      jsonb.toJson(new TransientPlusCustomizationAnnotatedSetterContainer());
      fail(
          "JsonbException not thrown for property annotated with JsonbTransient and setter with other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        fail(
            "JsonbException expected for property annotated with JsonbTransient and setter with other Jsonb customization annotation.");
      }
    }

    try {
      jsonb.fromJson("{ \"instance\" : \"Test String\" }",
          TransientPlusCustomizationAnnotatedSetterContainer.class);
      fail(
          "JsonbException not thrown for property annotated with JsonbTransient and setter with other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        fail(
            "JsonbException expected for property annotated with JsonbTransient and setter with other Jsonb customization annotation.");
      }
    }
  }

  /*
   * @testName: testTransientGetterPlusCustomizationAnnotatedField
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.1-3
   *
   * @test_Strategy: Assert that JsonbException is thrown for fields annotated
   * as both JsonbTransient and other Jsonb customization annotations
   */
  @Test
  public void testTransientGetterPlusCustomizationAnnotatedField()
       {
    try {
      jsonb.toJson(
          new TransientGetterPlusCustomizationAnnotatedFieldContainer());
      fail(
          "JsonbException not thrown for getter annotated with JsonbTransient and property with other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        fail(
            "JsonbException expected for getter annotated with JsonbTransient and property with other Jsonb customization annotation.");
      }
    }

    try {
      jsonb.fromJson("{ \"instance\" : \"Test String\" }",
          TransientGetterPlusCustomizationAnnotatedFieldContainer.class);
      fail(
          "JsonbException not thrown for getter annotated with JsonbTransient and property with other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        fail(
            "JsonbException expected for getter annotated with JsonbTransient and property with other Jsonb customization annotation.");
      }
    }
  }

  /*
   * @testName: testTransientGetterPlusCustomizationAnnotatedGetter
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.1-3
   *
   * @test_Strategy: Assert that JsonbException is thrown for fields annotated
   * as both JsonbTransient and other Jsonb customization annotations
   */
  @Test
  public void testTransientGetterPlusCustomizationAnnotatedGetter()
       {
    try {
      jsonb.toJson(
          new TransientGetterPlusCustomizationAnnotatedGetterContainer());
      fail(
          "JsonbException not thrown for property annotated with JsonbTransient and getter with other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        fail(
            "JsonbException expected for property annotated with JsonbTransient and getter with other Jsonb customization annotation.");
      }
    }

    try {
      jsonb.fromJson("{ \"instance\" : \"Test String\" }",
          TransientGetterPlusCustomizationAnnotatedGetterContainer.class);
      fail(
          "JsonbException not thrown for property annotated with JsonbTransient and getter with other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        fail(
            "JsonbException expected for property annotated with JsonbTransient and getter with other Jsonb customization annotation.");
      }
    }
  }

  /*
   * @testName: testTransientSetterPlusCustomizationAnnotatedSetter
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.1-4
   *
   * @test_Strategy: Assert that JsonbException is thrown for fields annotated
   * as both JsonbTransient and other Jsonb customization annotations
   */
  @Test
  public void testTransientSetterPlusCustomizationAnnotatedSetter()
      {
    try {
      jsonb.toJson(
          new TransientSetterPlusCustomizationAnnotatedSetterContainer());
      fail(
          "JsonbException not thrown for property annotated with JsonbTransient and setter with other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        fail(
            "JsonbException expected for property annotated with JsonbTransient and setter with other Jsonb customization annotation.");
      }
    }

    try {
      jsonb.fromJson("{ \"instance\" : \"Test String\" }",
          TransientSetterPlusCustomizationAnnotatedSetterContainer.class);
      fail(
          "JsonbException not thrown for property annotated with JsonbTransient and setter with other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        fail(
            "JsonbException expected for property annotated with JsonbTransient and setter with other Jsonb customization annotation.");
      }
    }
  }

  /*
   * @testName: testTransientSetterPlusCustomizationAnnotatedField
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.1-4
   *
   * @test_Strategy: Assert that JsonbException is thrown for fields annotated
   * as both JsonbTransient and other Jsonb customization annotations
   */
  @Test
  public void testTransientSetterPlusCustomizationAnnotatedField()
      {
    try {
      jsonb.toJson(
          new TransientSetterPlusCustomizationAnnotatedFieldContainer());
      fail(
          "JsonbException not thrown for setter annotated with JsonbTransient and property with other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        fail(
            "JsonbException expected for setter annotated with JsonbTransient and property with other Jsonb customization annotation.");
      }
    }

    try {
      jsonb.fromJson("{ \"instance\" : \"Test String\" }",
          TransientSetterPlusCustomizationAnnotatedFieldContainer.class);
      fail(
          "JsonbException not thrown for setter annotated with JsonbTransient and property with other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        fail(
            "JsonbException expected for setter annotated with JsonbTransient and property with other Jsonb customization annotation.");
      }
    }
  }

  /*
   * @testName: testPropertyNameCustomization
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.2-1
   *
   * @test_Strategy: Assert that property name can be customized using
   * JsonbProperty annotation
   */
  @Test
  public void testPropertyNameCustomization() {
    String jsonString = jsonb.toJson(new PropertyNameCustomizationContainer() {
      {
        setInstance("Test String");
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}")) {
      fail(
          "Failed to customize property name during marshalling using JsonbProperty annotation.");
    }

    PropertyNameCustomizationContainer unmarshalledObject = jsonb.fromJson(
        "{ \"stringInstance\" : \"Test String\" }",
        PropertyNameCustomizationContainer.class);
    if (!"Test String".equals(unmarshalledObject.getInstance())) {
      fail(
          "Failed to customize property name during unmarshalling using JsonbProperty annotation.");
    }

    return; // passed
  }

  /*
   * @testName: testPropertyNameCustomizationAccessors
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.2-1; JSONB:SPEC:JSB-4.1.2-2;
   * JSONB:SPEC:JSB-4.1.2-3; JSONB:SPEC:JSB-4.1.2-4
   *
   * @test_Strategy: Assert that property name can be individually customized
   * for marshalling and unmarshalling using JsonbProperty annotation on
   * accessors
   */
  @Test
  public void testPropertyNameCustomizationAccessors() {
    String jsonString = jsonb
        .toJson(new PropertyNameCustomizationAccessorsContainer() {
          {
            setInstance("Test String");
          }
        });
    if (!jsonString.matches(
        "\\{\\s*\"getterInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}")) {
      fail(
          "Failed to customize property name during marshalling using JsonbProperty annotation on getter.");
    }

    PropertyNameCustomizationAccessorsContainer unmarshalledObject = jsonb
        .fromJson("{ \"setterInstance\" : \"Test String\" }",
            PropertyNameCustomizationAccessorsContainer.class);
    if (!"Test String".equals(unmarshalledObject.getInstance())) {
      fail(
          "Failed to customize property name during unmarshalling using JsonbProperty annotation on setter.");
    }

    return; // passed
  }

  /*
   * @testName: testIdentityPropertyNamingStrategy
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.3-1
   *
   * @test_Strategy: Assert that property name is the same as the field name
   * when using PropertyNamingStrategy.IDENTITY
   */
  @Test
  public void testIdentityPropertyNamingStrategy() {
    JsonbConfig config = new JsonbConfig();
    config.setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY,
        PropertyNamingStrategy.IDENTITY);
    Jsonb jsonb = JsonbBuilder.create(config);

    String jsonString = jsonb.toJson(new StringContainer() {
      {
        setStringInstance("Test String");
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}")) {
      fail(
          "Failed to correctly marshal property using PropertyNamingStrategy.IDENTITY.");
    }

    StringContainer unmarshalledObject = jsonb.fromJson(
        "{ \"stringInstance\" : \"Test String\" }", StringContainer.class);
    if (!"Test String".equals(unmarshalledObject.getStringInstance())) {
      fail(
          "Failed to correctly unmarshal property using PropertyNamingStrategy.IDENTITY.");
    }

    return; // passed
  }

  /*
   * @testName: testLowerCaseWithDashesPropertyNamingStrategy
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.3-1
   *
   * @test_Strategy: Assert that property name is the same as the field name
   * when using PropertyNamingStrategy.LOWER_CASE_WITH_DASHES
   */
  @Test
  public void testLowerCaseWithDashesPropertyNamingStrategy() {
    JsonbConfig config = new JsonbConfig();
    config.setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY,
        PropertyNamingStrategy.LOWER_CASE_WITH_DASHES);
    Jsonb jsonb = JsonbBuilder.create(config);

    String jsonString = jsonb.toJson(new StringContainer() {
      {
        setStringInstance("Test String");
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"string-instance\"\\s*\\:\\s*\"Test String\"\\s*\\}")) {
      fail(
          "Failed to correctly marshal property using PropertyNamingStrategy.LOWER_CASE_WITH_DASHES.");
    }

    StringContainer unmarshalledObject = jsonb.fromJson(
        "{ \"string-instance\" : \"Test String\" }", StringContainer.class);
    if (!"Test String".equals(unmarshalledObject.getStringInstance())) {
      fail(
          "Failed to correctly unmarshal property using PropertyNamingStrategy.LOWER_CASE_WITH_DASHES.");
    }

    return; // passed
  }

  /*
   * @testName: testLowerCaseWithUnderscoresPropertyNamingStrategy
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.3-1
   *
   * @test_Strategy: Assert that property name is the same as the field name
   * when using PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES
   */
  @Test
  public void testLowerCaseWithUnderscoresPropertyNamingStrategy()
       {
    JsonbConfig config = new JsonbConfig();
    config.setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY,
        PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES);
    Jsonb jsonb = JsonbBuilder.create(config);

    String jsonString = jsonb.toJson(new StringContainer() {
      {
        setStringInstance("Test String");
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"string_instance\"\\s*\\:\\s*\"Test String\"\\s*\\}")) {
      fail(
          "Failed to correctly marshal property using PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES.");
    }

    StringContainer unmarshalledObject = jsonb.fromJson(
        "{ \"string_instance\" : \"Test String\" }", StringContainer.class);
    if (!"Test String".equals(unmarshalledObject.getStringInstance())) {
      fail(
          "Failed to correctly unmarshal property using PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES.");
    }

    return; // passed
  }

  /*
   * @testName: testUpperCamelCasePropertyNamingStrategy
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.3-1
   *
   * @test_Strategy: Assert that property name is the same as the field name
   * when using PropertyNamingStrategy.UPPER_CAMEL_CASE
   */
  @Test
  public void testUpperCamelCasePropertyNamingStrategy() {
    JsonbConfig config = new JsonbConfig();
    config.setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY,
        PropertyNamingStrategy.UPPER_CAMEL_CASE);
    Jsonb jsonb = JsonbBuilder.create(config);

    String jsonString = jsonb.toJson(new StringContainer() {
      {
        setStringInstance("Test String");
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"StringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}")) {
      fail(
          "Failed to correctly marshal property using PropertyNamingStrategy.UPPER_CAMEL_CASE.");
    }

    StringContainer unmarshalledObject = jsonb.fromJson(
        "{ \"StringInstance\" : \"Test String\" }", StringContainer.class);
    if (!"Test String".equals(unmarshalledObject.getStringInstance())) {
      fail(
          "Failed to correctly unmarshal property using PropertyNamingStrategy.UPPER_CAMEL_CASE.");
    }

    return; // passed
  }

  /*
   * @testName: testUpperCamelCaseWithSpacesPropertyNamingStrategy
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.3-1
   *
   * @test_Strategy: Assert that property name is the same as the field name
   * when using PropertyNamingStrategy.UPPER_CAMEL_CASE_WITH_SPACES
   */
  @Test
  public void testUpperCamelCaseWithSpacesPropertyNamingStrategy()
      {
    JsonbConfig config = new JsonbConfig();
    config.setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY,
        PropertyNamingStrategy.UPPER_CAMEL_CASE_WITH_SPACES);
    Jsonb jsonb = JsonbBuilder.create(config);

    String jsonString = jsonb.toJson(new StringContainer() {
      {
        setStringInstance("Test String");
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"String Instance\"\\s*\\:\\s*\"Test String\"\\s*\\}")) {
      fail(
          "Failed to correctly marshal property using PropertyNamingStrategy.UPPER_CAMEL_CASE_WITH_SPACES.");
    }

    StringContainer unmarshalledObject = jsonb.fromJson(
        "{ \"String Instance\" : \"Test String\" }", StringContainer.class);
    if (!"Test String".equals(unmarshalledObject.getStringInstance())) {
      fail(
          "Failed to correctly unmarshal property using PropertyNamingStrategy.UPPER_CAMEL_CASE_WITH_SPACES.");
    }

    return; // passed
  }

  /*
   * @testName: testCaseInsensitivePropertyNamingStrategy
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.3-1
   *
   * @test_Strategy: Assert that property name is the same as the field name
   * when using PropertyNamingStrategy.CASE_INSENSITIVE
   */
  @Test
  public void testCaseInsensitivePropertyNamingStrategy() {
    JsonbConfig config = new JsonbConfig();
    config.setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY,
        PropertyNamingStrategy.CASE_INSENSITIVE);
    Jsonb jsonb = JsonbBuilder.create(config);

    String jsonString = jsonb.toJson(new StringContainer() {
      {
        setStringInstance("Test String");
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}")) {
      fail(
          "Failed to correctly marshal property using PropertyNamingStrategy.CASE_INSENSITIVE.");
    }

    StringContainer unmarshalledObject = jsonb.fromJson(
        "{ \"stringInstance\" : \"Test String\" }", StringContainer.class);
    if (!"Test String".equals(unmarshalledObject.getStringInstance())) {
      fail(
          "Failed to correctly unmarshal property using PropertyNamingStrategy.CASE_INSENSITIVE.");
    }

    return; // passed
  }

  /*
   * @testName: testDuplicateName
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.4-1
   *
   * @test_Strategy: Assert that JsonbException is thrown for property name
   * duplication as a result of property name customization
   */
  @Test
  public void testDuplicateName() {
    try {
      jsonb.toJson(new DuplicateNameContainer());
      fail(
          "JsonbException not thrown for property name duplication as a result of property name customization.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        fail(
            "JsonbException expected for property name duplication as a result of property name customization.");
      }
    }

    try {
      jsonb.fromJson("{ \"instance\" : \"Test String\" }",
          DuplicateNameContainer.class);
      fail(
          "JsonbException not thrown for property name duplication as a result of property name customization.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        fail(
            "JsonbException expected for property name duplication as a result of property name customization.");
      }
    }

    return; // passed
  }
}
