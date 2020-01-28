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

package jakarta.json.bind.customizedmapping.visibility;

import static org.junit.Assert.fail;

import org.junit.Test;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.customizedmapping.visibility.model.CustomFieldVisibilityStrategy;
import jakarta.json.bind.customizedmapping.visibility.model.CustomVisibilityAnnotatedContainer;
import jakarta.json.bind.customizedmapping.visibility.model.SimpleContainer;
import jakarta.json.bind.customizedmapping.visibility.model.customized.PackageCustomizedContainer;

/**
 * @test
 * @sources VisibilityCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.visibility.VisibilityCustomizationTest
 **/
public class VisibilityCustomizationTest {
  private final Jsonb jsonb = JsonbBuilder.create();

  /*
   * @testName: testCustomVisibilityConfig
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.6-1
   *
   * @test_Strategy: Assert that only fields allowed by custom
   * PropertyVisibilityStrategy are available for marshalling and unmarshalling
   * if JsonbConfig.withPropertyVisibilityStrategy is used
   */
  @Test
  public void testCustomVisibilityConfig() {
    Jsonb jsonb = JsonbBuilder.create(new JsonbConfig()
        .withPropertyVisibilityStrategy(new CustomFieldVisibilityStrategy()));

    String jsonString = jsonb.toJson(new SimpleContainer() {
      {
        setStringInstance("Test String");
      }
    });
    if (!jsonString.matches("\\{\\s*\"floatInstance\"\\s*:\\s*0.0\\s*}")) {
      fail(
          "Failed to hide fields during marshalling by applying custom visibility strategy using configuration.");
    }

    SimpleContainer unmarshalledObject = jsonb.fromJson(
        "{ \"stringInstance\" : \"Test String\", \"floatInstance\" : 1.0, \"integerInstance\" : 1 }",
        SimpleContainer.class);
    if (unmarshalledObject.getStringInstance() != null
        || unmarshalledObject.getIntegerInstance() != 1 
        || unmarshalledObject.getFloatInstance() != 1.0f) {
      fail(
          "Failed to ignore fields during unmarshalling by applying custom visibility strategy using configuration.");
    }

    return; // passed
  }

  /*
   * @testName: testCustomVisibilityAnnotation
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.6-1
   *
   * @test_Strategy: Assert that only fields allowed by custom
   * PropertyVisibilityStrategy are available for marshalling and unmarshalling
   * if JsonbVisibility annotation is used on a type
   */
  @Test
  public void testCustomVisibilityAnnotation() {
    String jsonString = jsonb.toJson(new CustomVisibilityAnnotatedContainer() {
      {
        setStringInstance("Test String");
      }
    });
    if (!jsonString.matches("\\{\\s*\"floatInstance\"\\s*:\\s*0.0\\s*}")) {
      fail(
          "Failed to hide fields during marshalling by applying custom visibility strategy using JsonbVisibility annotation.");
    }

    CustomVisibilityAnnotatedContainer unmarshalledObject = jsonb.fromJson(
        "{ \"stringInstance\" : \"Test String\", \"floatInstance\" : 1.0, \"integerInstance\" : 1 }",
        CustomVisibilityAnnotatedContainer.class);
    if (unmarshalledObject.getStringInstance() != null
        || unmarshalledObject.getIntegerInstance() != null
        || unmarshalledObject.getFloatInstance() != 0.0f) {
      fail(
          "Failed to ignore fields during unmarshalling by applying custom visibility strategy using JsonbVisibility annotation.");
    }

    return; // passed
  }

  /*
   * @testName: testCustomVisibilityPackage
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.6-1
   *
   * @test_Strategy: Assert that only fields allowed by custom
   * PropertyVisibilityStrategy are available for marshalling and unmarshalling
   * if JsonbVisibility annotation is used on a package
   */
  @Test
  public void testCustomVisibilityPackage() {
    String jsonString = jsonb.toJson(new PackageCustomizedContainer() {
      {
        setStringInstance("Test String");
      }
    });
    if (!jsonString.matches("\\{\\s*\"floatInstance\"\\s*:\\s*0.0\\s*}")) {
      fail(
          "Failed to hide fields during marshalling by applying custom visibility strategy using JsonbVisibility annotation on package.");
    }

    PackageCustomizedContainer unmarshalledObject = jsonb.fromJson(
        "{ \"stringInstance\" : \"Test String\", \"floatInstance\" : 1.0, \"integerInstance\" : 1 }",
        PackageCustomizedContainer.class);
    if (unmarshalledObject.getStringInstance() != null
        || unmarshalledObject.getIntegerInstance() != null
        || unmarshalledObject.getFloatInstance() != 0.0f) {
      fail(
          "Failed to ignore fields during unmarshalling by applying custom visibility strategy using JsonbVisibility annotation on package.");
    }

    return; // passed
  }
}
