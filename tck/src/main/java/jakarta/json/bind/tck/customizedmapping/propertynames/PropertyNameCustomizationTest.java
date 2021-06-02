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

package jakarta.json.bind.tck.customizedmapping.propertynames;

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
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @test
 * @sources PropertyNameCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.propertynames.PropertyNameCustomizationTest
 **/
public class PropertyNameCustomizationTest {
    
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
    assertThat("Failed to ignore transient property during marshalling.", jsonString, matchesPattern("\\{\\s*\\}"));

    TransientPropertyContainer unmarshalledObject = jsonb.fromJson("{ \"instance\" : \"Test String\" }",
                                                                   TransientPropertyContainer.class);
    assertThat("Failed to ignore transient property during unmarshalling.", unmarshalledObject.getInstance(), nullValue());
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
    assertThat("Failed to ignore JsonbTransient property during marshalling.", jsonString, matchesPattern("\\{\\s*\\}"));

    TransientAnnotatedPropertyContainer unmarshalledObject = jsonb.fromJson("{ \"instance\" : \"Test String\" }",
                                                                            TransientAnnotatedPropertyContainer.class);
    assertThat("Failed to ignore JsonbTransient property during unmarshalling.", unmarshalledObject.getInstance(), nullValue());
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
    String jsonString = jsonb.toJson(new TransientGetterAnnotatedPropertyContainer() {
          {
            setInstance("String Value");
          }
        });
    assertThat("Failed to ignore @JsonbTransient on getter during marshalling.", jsonString, matchesPattern("\\{\\s*\\}"));
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
    TransientSetterAnnotatedPropertyContainer unmarshalledObject = jsonb.fromJson("{ \"instance\" : \"Test String\" }",
                                                                                  TransientSetterAnnotatedPropertyContainer.class);
    assertThat("Failed to ignore @JsonbTransient on setter during unmarshalling.", unmarshalledObject.getInstance(), nullValue());
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
    String message = "JsonbException not thrown for property annotated with both "
            + "JsonbTransient and other Jsonb customization annotation.";
    assertThrows(JsonbException.class, () -> jsonb.toJson(new TransientPlusCustomizationAnnotatedPropertyContainer()), message);
    assertThrows(JsonbException.class,
                 () -> jsonb.fromJson("{ \"instance\" : \"Test String\" }",
                                      TransientPlusCustomizationAnnotatedPropertyContainer.class),
                 message);
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
    String message = "JsonbException not thrown for property annotated with JsonbTransient and getter with other "
            + "Jsonb customization annotation.";
    assertThrows(JsonbException.class, () -> jsonb.toJson(new TransientPlusCustomizationAnnotatedGetterContainer()), message);
    assertThrows(JsonbException.class,
                 () -> jsonb.fromJson("{ \"instance\" : \"Test String\" }",
                                      TransientPlusCustomizationAnnotatedGetterContainer.class),
                 message);
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
    String message = "JsonbException not thrown for property annotated with JsonbTransient and setter with other "
            + "Jsonb customization annotation.";
    assertThrows(JsonbException.class, () -> jsonb.toJson(new TransientPlusCustomizationAnnotatedSetterContainer()), message);
    assertThrows(JsonbException.class,
                 () -> jsonb.fromJson("{ \"instance\" : \"Test String\" }",
                                      TransientPlusCustomizationAnnotatedSetterContainer.class),
                 message);
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
  public void testTransientGetterPlusCustomizationAnnotatedField() {
    String message = "JsonbException not thrown for getter annotated with JsonbTransient and property with other "
            + "Jsonb customization annotation.";
    assertThrows(JsonbException.class, () -> jsonb.toJson(new TransientGetterPlusCustomizationAnnotatedFieldContainer()), message);
    assertThrows(JsonbException.class,
                 () -> jsonb.fromJson("{ \"instance\" : \"Test String\" }",
                                      TransientGetterPlusCustomizationAnnotatedFieldContainer.class),
                 message);
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
  public void testTransientGetterPlusCustomizationAnnotatedGetter() {
    String message = "JsonbException not thrown for property annotated with JsonbTransient and getter with other "
            + "Jsonb customization annotation.";
    assertThrows(JsonbException.class, () -> jsonb.toJson(new TransientGetterPlusCustomizationAnnotatedGetterContainer()), message);
    assertThrows(JsonbException.class,
                 () -> jsonb.fromJson("{ \"instance\" : \"Test String\" }",
                                      TransientGetterPlusCustomizationAnnotatedGetterContainer.class),
                 message);
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
  public void testTransientSetterPlusCustomizationAnnotatedSetter() {
    String message = "JsonbException not thrown for property annotated with JsonbTransient and setter with other "
            + "Jsonb customization annotation.";
    assertThrows(JsonbException.class, () -> jsonb.toJson(new TransientSetterPlusCustomizationAnnotatedSetterContainer()), message);
    assertThrows(JsonbException.class,
                 () -> jsonb.fromJson("{ \"instance\" : \"Test String\" }",
                                      TransientSetterPlusCustomizationAnnotatedSetterContainer.class),
                 message);
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
  public void testTransientSetterPlusCustomizationAnnotatedField() {
    String message = "JsonbException not thrown for setter annotated with JsonbTransient and property with other "
            + "Jsonb customization annotation.";
    assertThrows(JsonbException.class, () -> jsonb.toJson(new TransientSetterPlusCustomizationAnnotatedFieldContainer()), message);
    assertThrows(JsonbException.class,
                 () -> jsonb.fromJson("{ \"instance\" : \"Test String\" }",
                                      TransientSetterPlusCustomizationAnnotatedFieldContainer.class),
                 message);
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
    assertThat("Failed to customize property name during marshalling using JsonbProperty annotation.",
               jsonString, matchesPattern("\\{\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}"));

    PropertyNameCustomizationContainer unmarshalledObject = jsonb.fromJson("{ \"stringInstance\" : \"Test String\" }",
                                                                           PropertyNameCustomizationContainer.class);
    assertThat("Failed to customize property name during unmarshalling using JsonbProperty annotation.",
               unmarshalledObject.getInstance(), is("Test String"));
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
    String jsonString = jsonb.toJson(new PropertyNameCustomizationAccessorsContainer() {
          {
            setInstance("Test String");
          }
        });
    assertThat("Failed to customize property name during marshalling using JsonbProperty annotation on getter.",
               jsonString, matchesPattern("\\{\\s*\"getterInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}"));

    PropertyNameCustomizationAccessorsContainer unmarshalledObject = jsonb.fromJson("{ \"setterInstance\" : \"Test String\" }",
                                                                                    PropertyNameCustomizationAccessorsContainer.class);
    assertThat("Failed to customize property name during unmarshalling using JsonbProperty annotation on setter.",
               unmarshalledObject.getInstance(), is("Test String"));
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
    JsonbConfig config = new JsonbConfig().setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY, PropertyNamingStrategy.IDENTITY);
    Jsonb jsonb = JsonbBuilder.create(config);
    String jsonString = jsonb.toJson(new StringContainer() {
      {
        setStringInstance("Test String");
      }
    });
    assertThat("Failed to correctly marshal property using PropertyNamingStrategy.IDENTITY.",
               jsonString, matchesPattern("\\{\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}"));

    StringContainer unmarshalledObject = jsonb.fromJson("{ \"stringInstance\" : \"Test String\" }", StringContainer.class);
    assertThat("Failed to correctly unmarshal property using PropertyNamingStrategy.IDENTITY.",
               unmarshalledObject.getStringInstance(), is("Test String"));
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
    JsonbConfig config = new JsonbConfig().setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY,
                                                       PropertyNamingStrategy.LOWER_CASE_WITH_DASHES);
    Jsonb jsonb = JsonbBuilder.create(config);
    String jsonString = jsonb.toJson(new StringContainer() {
      {
        setStringInstance("Test String");
      }
    });
    assertThat("Failed to correctly marshal property using PropertyNamingStrategy.LOWER_CASE_WITH_DASHES.",
               jsonString, matchesPattern("\\{\\s*\"string-instance\"\\s*\\:\\s*\"Test String\"\\s*\\}"));

    StringContainer unmarshalledObject = jsonb.fromJson("{ \"string-instance\" : \"Test String\" }", StringContainer.class);
    assertThat("Failed to correctly unmarshal property using PropertyNamingStrategy.LOWER_CASE_WITH_DASHES.",
               unmarshalledObject.getStringInstance(), is("Test String"));
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
  public void testLowerCaseWithUnderscoresPropertyNamingStrategy() {
    JsonbConfig config = new JsonbConfig().setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY,
                                                       PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES);
    Jsonb jsonb = JsonbBuilder.create(config);
    String jsonString = jsonb.toJson(new StringContainer() {
      {
        setStringInstance("Test String");
      }
    });
    assertThat("Failed to correctly marshal property using PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES.",
               jsonString, matchesPattern("\\{\\s*\"string_instance\"\\s*\\:\\s*\"Test String\"\\s*\\}"));

    StringContainer unmarshalledObject = jsonb.fromJson("{ \"string_instance\" : \"Test String\" }", StringContainer.class);
    assertThat("Failed to correctly unmarshal property using PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES.",
               unmarshalledObject.getStringInstance(), is("Test String"));
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
    JsonbConfig config = new JsonbConfig().setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY,
                                                       PropertyNamingStrategy.UPPER_CAMEL_CASE);
    Jsonb jsonb = JsonbBuilder.create(config);
    String jsonString = jsonb.toJson(new StringContainer() {
      {
        setStringInstance("Test String");
      }
    });
    assertThat("Failed to correctly marshal property using PropertyNamingStrategy.UPPER_CAMEL_CASE.",
               jsonString, matchesPattern("\\{\\s*\"StringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}"));

    StringContainer unmarshalledObject = jsonb.fromJson("{ \"StringInstance\" : \"Test String\" }", StringContainer.class);
    assertThat("Failed to correctly unmarshal property using PropertyNamingStrategy.UPPER_CAMEL_CASE.",
               unmarshalledObject.getStringInstance(), is("Test String"));
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
  public void testUpperCamelCaseWithSpacesPropertyNamingStrategy() {
    JsonbConfig config = new JsonbConfig().setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY,
                                                       PropertyNamingStrategy.UPPER_CAMEL_CASE_WITH_SPACES);
    Jsonb jsonb = JsonbBuilder.create(config);
    String jsonString = jsonb.toJson(new StringContainer() {
      {
        setStringInstance("Test String");
      }
    });
    assertThat("Failed to correctly marshal property using PropertyNamingStrategy.UPPER_CAMEL_CASE_WITH_SPACES.",
               jsonString, matchesPattern("\\{\\s*\"String Instance\"\\s*\\:\\s*\"Test String\"\\s*\\}"));

    StringContainer unmarshalledObject = jsonb.fromJson("{ \"String Instance\" : \"Test String\" }", StringContainer.class);
    assertThat("Failed to correctly unmarshal property using PropertyNamingStrategy.UPPER_CAMEL_CASE_WITH_SPACES.",
               unmarshalledObject.getStringInstance(), is("Test String"));
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
    JsonbConfig config = new JsonbConfig().setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY,
                                                       PropertyNamingStrategy.CASE_INSENSITIVE);
    Jsonb jsonb = JsonbBuilder.create(config);
    String jsonString = jsonb.toJson(new StringContainer() {
      {
        setStringInstance("Test String");
      }
    });
    assertThat("Failed to correctly marshal property using PropertyNamingStrategy.CASE_INSENSITIVE.",
               jsonString, matchesPattern("\\{\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}"));

    StringContainer unmarshalledObject = jsonb.fromJson("{ \"stringInstance\" : \"Test String\" }", StringContainer.class);
    assertThat("Failed to correctly unmarshal property using PropertyNamingStrategy.CASE_INSENSITIVE.",
               unmarshalledObject.getStringInstance(), is("Test String"));
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
    String message = "JsonbException not thrown for property name duplication as a result of property name customization.";
    assertThrows(JsonbException.class, () -> jsonb.toJson(new DuplicateNameContainer()), message);
    assertThrows(JsonbException.class,
                 () -> jsonb.fromJson("{ \"instance\" : \"Test String\" }", DuplicateNameContainer.class),
                 message);
  }
}
