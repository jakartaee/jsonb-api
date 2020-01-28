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

package jakarta.json.bind.customizedmapping.instantiation;

import static org.junit.Assert.fail;

import org.junit.Test;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.json.bind.customizedmapping.instantiation.model.CreatorPlusFactoryContainer;
import jakarta.json.bind.customizedmapping.instantiation.model.IllegalInstanceFactoryCreatorContainer;
import jakarta.json.bind.customizedmapping.instantiation.model.MultipleCreatorsContainer;
import jakarta.json.bind.customizedmapping.instantiation.model.MultipleFactoryCreatorsContainer;
import jakarta.json.bind.customizedmapping.instantiation.model.SimpleCreatorContainer;
import jakarta.json.bind.customizedmapping.instantiation.model.SimpleCreatorPlusFieldsContainer;
import jakarta.json.bind.customizedmapping.instantiation.model.SimpleCreatorRenameContainer;
import jakarta.json.bind.customizedmapping.instantiation.model.SimpleFactoryCreatorContainer;

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
    SimpleCreatorContainer unmarshalledObject = jsonb.fromJson(
        "{ \"stringInstance\" : \"Test String\", \"integerInstance\" : 1, \"floatInstance\" : 1.0 }",
        SimpleCreatorContainer.class);
    if (!"Constructor String".equals(unmarshalledObject.getStringInstance())
        || unmarshalledObject.getIntegerInstance() != 2
        || unmarshalledObject.getFloatInstance() != 2) {
      fail(
          "Failed to instantiate type using JsonbCreator annotated constructor during unmarshalling.");
    }
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
    SimpleCreatorPlusFieldsContainer unmarshalledObject = jsonb.fromJson(
        "{ \"stringInstance\" : \"Test String\", \"integerInstance\" : 1, \"floatInstance\" : 1.0 }",
        SimpleCreatorPlusFieldsContainer.class);
    if (!"Constructor String".equals(unmarshalledObject.getStringInstance())
        || unmarshalledObject.getIntegerInstance() != 2
        || unmarshalledObject.getFloatInstance() != 1) {
      fail(
          "Failed to instantiate type using JsonbCreator annotated constructor and set remaining fields as normally during unmarshalling.");
    }
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
    SimpleFactoryCreatorContainer unmarshalledObject = jsonb.fromJson(
        "{ \"constructorString\" : \"Test String\" }",
        SimpleFactoryCreatorContainer.class);
    if (!"Factory String".equals(unmarshalledObject.getStringInstance())
        || unmarshalledObject.getIntegerInstance() != 2
        || unmarshalledObject.getFloatInstance() != 3f) {
      fail(
          "Failed to instantiate type using JsonbCreator annotated method during unmarshalling.");
    }
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
    try {
      jsonb.fromJson(
          "{ \"stringInstance\" : \"Test String\", \"integerInstance\" : 1, \"floatInstance\" : 1.0 }",
          MultipleCreatorsContainer.class);
    } catch (JsonbException x) {
      return;
    }
    fail(
        "A JsonbException is expected when unmarshalling to a class with multiple constructors annotated with JsonbCreator.");
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
    try {
      jsonb.fromJson(
          "{ \"stringInstance\" : \"Test String\", \"integerInstance\" : 1, \"floatInstance\" : 1.0 }",
          MultipleFactoryCreatorsContainer.class);
    } catch (JsonbException x) {
      return;
    }
    fail(
        "A JsonbException is expected when unmarshalling to a class with multiple methods annotated with JsonbCreator.");
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
    try {
      jsonb.fromJson(
          "{ \"stringInstance\" : \"Test String\", \"integerInstance\" : 1, \"floatInstance\" : 1.0 }",
          CreatorPlusFactoryContainer.class);
    } catch (JsonbException x) {
      return;
    }
    fail(
        "A JsonbException is expected when unmarshalling to a class with multiple JsonbCreator annotation instances.");
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
    try {
      jsonb.fromJson(
          "{ \"stringInstance\" : \"Test String\", \"integerInstance\" : 1, \"floatInstance\" : 1.0 }",
          IllegalInstanceFactoryCreatorContainer.class);
    } catch (JsonbException x) {
      return;
    }
    fail(
        "A JsonbException is expected when unmarshalling to a class with a method annotated with JsonbCreator returning a type different than the class type.");
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
    SimpleCreatorRenameContainer unmarshalledObject = jsonb.fromJson(
        "{ \"stringInstance\" : \"Test String\", \"intInstance\" : 1, \"floatInstance\" : 1.0 }",
        SimpleCreatorRenameContainer.class);
    if (!"Constructor String".equals(unmarshalledObject.getStringInstance())
        || unmarshalledObject.getIntegerInstance() != 1
        || unmarshalledObject.getFloatInstance() != 2) {
      fail(
          "Failed to instantiate type using JsonbCreator annotated constructor having a JsonbProperty annotated argument during unmarshalling.");
    }
  }

  /*
   * @testName: testUnmappablePropertyName
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.5-3
   *
   * @test_Strategy: Assert that a JsonbException is thrown when unmarshalling
   * to a class with a constructor annotated with JsonbCreator and there exists
   * an unmappable property name
   */
  @Test
  public void testUnmappablePropertyName() {
    try {
      SimpleCreatorContainer c = jsonb.fromJson(
          "{ \"instance\" : \"Test String\", \"integerInstance\" : 1, \"floatInstance\" : 1.0 }",
          SimpleCreatorContainer.class);
      System.out.println(c);
    } catch (JsonbException x) {
      return;
    }
    fail(
        "A JsonbException is expected when unmarshalling to a class with a constructor annotated with JsonbCreator and there exists an unmappable property name.");
  }
}
