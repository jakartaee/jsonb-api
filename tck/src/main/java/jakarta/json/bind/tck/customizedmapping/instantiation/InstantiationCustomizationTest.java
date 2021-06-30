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

package jakarta.json.bind.tck.customizedmapping.instantiation;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.json.bind.tck.customizedmapping.instantiation.model.CreatorPlusFactoryContainer;
import jakarta.json.bind.tck.customizedmapping.instantiation.model.CreatorWithAdapterContainer;
import jakarta.json.bind.tck.customizedmapping.instantiation.model.CreatorWithDeserializerContainer;
import jakarta.json.bind.tck.customizedmapping.instantiation.model.IllegalInstanceFactoryCreatorContainer;
import jakarta.json.bind.tck.customizedmapping.instantiation.model.MultipleCreatorsContainer;
import jakarta.json.bind.tck.customizedmapping.instantiation.model.MultipleFactoryCreatorsContainer;
import jakarta.json.bind.tck.customizedmapping.instantiation.model.SimpleCreatorContainer;
import jakarta.json.bind.tck.customizedmapping.instantiation.model.SimpleCreatorPlusFieldsContainer;
import jakarta.json.bind.tck.customizedmapping.instantiation.model.SimpleCreatorRenameContainer;
import jakarta.json.bind.tck.customizedmapping.instantiation.model.SimpleFactoryCreatorContainer;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @test
 * @sources InstantiationCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.instantiation.InstantiationCustomizationTest
 **/
public class InstantiationCustomizationTest {

    private final Jsonb jsonb = JsonbBuilder.create();

    /*
     * @testName: testCustomConstructor
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.5-1
     *
     * @test_Strategy: Assert that a constructor annotated with JsonbCreator
     * annotation can be used to customize class instantiation during
     * unmarshalling
     */
    @Test
    public void testCustomConstructor() {
        String toDeserialize = "{ \"stringInstance\" : \"Test String\", \"integerInstance\" : 1, \"floatInstance\" : 1.0 }";
        SimpleCreatorContainer unmarshalledObject = jsonb.fromJson(toDeserialize, SimpleCreatorContainer.class);

        String validationMessage = "Failed to instantiate type using JsonbCreator annotated constructor during unmarshalling.";
        assertThat(validationMessage, unmarshalledObject.getStringInstance(), is("Constructor String"));
        assertThat(validationMessage, unmarshalledObject.getIntegerInstance(), is(2));
        assertThat(validationMessage, unmarshalledObject.getFloatInstance(), is(2f));
    }

    /*
     * @testName: testCustomConstructorPlusFields
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.5-1
     *
     * @test_Strategy: Assert that a constructor annotated with JsonbCreator
     * annotation can be used to customize class instantiation during
     * unmarshalling and the rest of the fields can be initialized normally
     */
    @Test
    public void testCustomConstructorPlusFields() {
        String toDeserialize = "{ \"stringInstance\" : \"Test String\", \"integerInstance\" : 1, \"floatInstance\" : 1.0 }";
        SimpleCreatorPlusFieldsContainer unmarshalledObject = jsonb
                .fromJson(toDeserialize, SimpleCreatorPlusFieldsContainer.class);

        String validationMessage = "Failed to instantiate type using JsonbCreator annotated constructor and set remaining "
                + "fields as normally during unmarshalling.";
        assertThat(validationMessage, unmarshalledObject.getStringInstance(), is("Constructor String"));
        assertThat(validationMessage, unmarshalledObject.getIntegerInstance(), is(2));
        assertThat(validationMessage, unmarshalledObject.getFloatInstance(), is(1f));
    }

    /*
     * @testName: testFactoryMethod
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.5-1
     *
     * @test_Strategy: Assert that a method annotated with JsonbCreator annotation
     * can be used to customize class instantiation during unmarshalling
     */
    @Test
    public void testFactoryMethod() {
        String toDeserialize = "{ \"constructorString\" : \"Test String\" }";
        SimpleFactoryCreatorContainer unmarshalledObject = jsonb.fromJson(toDeserialize, SimpleFactoryCreatorContainer.class);

        String validationMessage = "Failed to instantiate type using JsonbCreator annotated method during unmarshalling.";
        assertThat(validationMessage, unmarshalledObject.getStringInstance(), is("Factory String"));
        assertThat(validationMessage, unmarshalledObject.getIntegerInstance(), is(2));
        assertThat(validationMessage, unmarshalledObject.getFloatInstance(), is(3f));
    }

    /*
     * @testName: testMultipleConstructors
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.5-1
     *
     * @test_Strategy: Assert that a JsonbException is thrown if multiple
     * constructors are annotated with JsonbCreator annotation
     */
    @Test
    public void testMultipleConstructors() {
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"stringInstance\" : \"Test String\", "
                                                  + "\"integerInstance\" : 1, \"floatInstance\" : 1.0 }",
                                          MultipleCreatorsContainer.class),
                     "A JsonbException is expected when unmarshalling to a class with multiple constructors annotated "
                             + "with JsonbCreator.");
    }

    /*
     * @testName: testMultipleFactories
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.5-1
     *
     * @test_Strategy: Assert that a JsonbException is thrown if multiple methods
     * are annotated with JsonbCreator annotation
     */
    @Test
    public void testMultipleFactories() {
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"stringInstance\" : \"Test String\", "
                                                  + "\"integerInstance\" : 1, \"floatInstance\" : 1.0 }",
                                          MultipleFactoryCreatorsContainer.class),
                     "A JsonbException is expected when unmarshalling to a class with multiple methods annotated "
                             + "with JsonbCreator.");
    }

    /*
     * @testName: testConstructorPlusFactory
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.5-1
     *
     * @test_Strategy: Assert that a JsonbException is thrown if JsonbCreator
     * annotation instances are used to instantiate a type
     */
    @Test
    public void testConstructorPlusFactory() {
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"stringInstance\" : \"Test String\", "
                                                  + "\"integerInstance\" : 1, \"floatInstance\" : 1.0 }",
                                          CreatorPlusFactoryContainer.class),
                     "A JsonbException is expected when unmarshalling to a class with multiple JsonbCreator "
                             + "annotation instances.");
    }

    /*
     * @testName: testIllegalFactoryType
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.5-2
     *
     * @test_Strategy: Assert that a JsonbException is thrown if the type returned
     * by the factory method annotated with JsonbCreator is not the type that the
     * annotation is used for
     */
    @Test
    public void testIllegalFactoryType() {
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"stringInstance\" : \"Test String\", "
                                                  + "\"integerInstance\" : 1, \"floatInstance\" : 1.0 }",
                                          IllegalInstanceFactoryCreatorContainer.class),
                     "A JsonbException is expected when unmarshalling to a class with a method annotated with JsonbCreator "
                             + "returning a type different than the class type.");
    }

    /*
     * @testName: testRenamedProperty
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.5-3
     *
     * @test_Strategy: Assert that JsonbProperty annotation can be used to rename
     * an argument of a constructor annotated as JsonbCreator
     */
    @Test
    public void testRenamedProperty() {
        String toDeserialize = "{ \"stringInstance\" : \"Test String\", \"intInstance\" : 1, \"floatInstance\" : 1.0 }";
        SimpleCreatorRenameContainer unmarshalledObject = jsonb.fromJson(toDeserialize, SimpleCreatorRenameContainer.class);

        String validationMessage = "Failed to instantiate type using JsonbCreator annotated constructor having a JsonbProperty "
                + "annotated argument during unmarshalling.";
        assertThat(validationMessage, unmarshalledObject.getStringInstance(), is("Constructor String"));
        assertThat(validationMessage, unmarshalledObject.getIntegerInstance(), is(1));
        assertThat(validationMessage, unmarshalledObject.getFloatInstance(), is(2f));
    }

  /*
   * @testName: testJsonbTypeDeserializerOnCreatorParameter
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.7.2-5
   *
   * @test_Strategy: Assert that object instance has been created with JsonbCreator
   * and parameter annotated with @JsonbTypeDeserializer annotation has been properly
   * deserialized.
   */
  @Test
  public void testJsonbDeserializerOnCreatorParameter() {
    CreatorWithDeserializerContainer c = jsonb.fromJson("{ \"instance\" : \"Test String\" }",
                                                        CreatorWithDeserializerContainer.class);
    String expected = "Test String Deserialized";
    assertEquals("JsonbDeserializer on the JsonbCreator parameter was not executed." , expected, c.getStringInstance());
  }

  @Test
  public void testJsonbAdapterOnCreatorParameter() {
    CreatorWithAdapterContainer c = jsonb.fromJson("{ \"instance\" : \"string value\" }",
                                                   CreatorWithAdapterContainer.class);
    String expected = "string value";
    assertNotNull("JsonbAdapter on the JsonbCreator parameter was not executed.", c.getStringWrapper());
    assertEquals("JsonbAdapter on the JsonbCreator parameter was not executed.",
                 expected, c.getStringWrapper().getWrapped());
  }
}
