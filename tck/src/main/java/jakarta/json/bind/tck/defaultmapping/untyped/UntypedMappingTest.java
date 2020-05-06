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

package jakarta.json.bind.tck.defaultmapping.untyped;

import static org.junit.Assert.fail;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.tck.SimpleMappingTester;

/**
 * @test
 * @sources UntypedMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.untyped.UntypedMappingTest
 **/
@RunWith(Arquillian.class)
public class UntypedMappingTest {
    
    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, MethodHandles.lookup().lookupClass().getPackage().getName());
    }

  /*
   * @testName: testObjectMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.6-1; JSONB:SPEC:JSB-3.6-2
   *
   * @test_Strategy: Assert that object, string, number, boolean and null JSON
   * values are correctly mapped to java.util.Map<String,Object> implementation
   * with predictable iteration order, with java.lang.String,
   * java.math.BigDecimal, java.lang.Boolean and null values
   */
  @SuppressWarnings("serial")
  @Test
  public void testObjectMapping() {
    Jsonb jsonb = JsonbBuilder.create();
    @SuppressWarnings("unused")
    String jsonString = jsonb.toJson(new Object() {
      private String stringProperty = "Test String";

      public String getStringProperty() {
        return stringProperty;
      }

      private Number numericProperty = 0;

      public Number getNumericProperty() {
        return numericProperty;
      }

      private boolean booleanProperty = false;

      public boolean getBooleanProperty() {
        return booleanProperty;
      }

      private Object nullProperty = null;

      public Object getNullProperty() {
        return nullProperty;
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"booleanProperty\"\\s*:\\s*false\\s*\\,\\s*\"numericProperty\"\\s*:\\s*0[\\.0]?+\\s*,\\s*\"stringProperty\"\\s*:\\s*\"Test String\"\\s*}")) {
      fail(
          "Failed to correctly marshal object with String, Number, Boolean and null fields.");
    }

    Object unmarshalledObject = jsonb.fromJson(
        "{ \"numericProperty\" : 0.0, \"booleanProperty\" : false, \"stringProperty\" : \"Test String\" }",
        Object.class);
    if (!Map.class.isInstance(unmarshalledObject)
        || !new LinkedHashMap<String, Object>() {
          {
            put("numericProperty", BigDecimal.valueOf(0.0d));
            put("booleanProperty", false);
            put("stringProperty", "Test String");
          }
        }.equals(unmarshalledObject)) {
      fail(
          "Failed to correctly unmarshal object with string, number, boolean and null JSON values into a predictable order Map<String,Object> with java.lang.String, java.math.BigDecimal, java.lang.Boolean and null values.");
    }
  }

  /*
   * @testName: testArrayMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.6-1
   *
   * @test_Strategy: Assert that JSON arrays are correctly handled as
   * java.util.List<Object>
   */
  @Test
  public void testArrayMapping() {
    new SimpleMappingTester<>(List.class).test(
        Arrays.asList("Test String"), "\\[\\s*\"Test String\"s*\\]",
        "[ \"Test String\" ]", Arrays.asList("Test String"));
  }
}
