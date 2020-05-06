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

package jakarta.json.bind.tck.defaultmapping.arrays;

import static org.junit.Assert.fail;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.tck.defaultmapping.arrays.model.MultiDimensionalArrayContainer;
import jakarta.json.bind.tck.defaultmapping.arrays.model.PrimitiveArrayContainer;

/**
 * @test
 * @sources ArraysMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.arrays.ArraysMappingTest
 **/
@RunWith(Arquillian.class)
public class ArraysMappingTest {
    
    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, MethodHandles.lookup().lookupClass().getPackage().getName());
    }
    
  private final Jsonb jsonb = JsonbBuilder.create();

  /*
   * @testName: testPrimitiveArray
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.12-1; JSONB:SPEC:JSB-3.12-2
   *
   * @test_Strategy: Assert that a simple array of primitives is handled
   * correctly
   */
  @Test
  public void testPrimitiveArray() {
    int[] instance = { Integer.MIN_VALUE, Integer.MAX_VALUE };
    String jsonString = jsonb.toJson(new PrimitiveArrayContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString
        .matches("\\{\\s*\"instance\"\\s*\\:\\s*\\[\\s*" + Integer.MIN_VALUE
            + "\\s*,\\s*" + Integer.MAX_VALUE + "\\s*\\]\\s*\\}")) {
      fail("Failed to marshal object with int[] attribute value.");
    }

    PrimitiveArrayContainer unmarshalledObject = jsonb
        .fromJson("{ \"instance\" : [ " + Integer.MIN_VALUE + ", "
            + Integer.MAX_VALUE + " ] }", PrimitiveArrayContainer.class);
    if (!Arrays.equals(instance, unmarshalledObject.getInstance())) {
      fail("Failed to unmarshal object with int[] attribute value.");
    }

    return; // passed
  }

  /*
   * @testName: testMultiDimensionalArray
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.12-1; JSONB:SPEC:JSB-3.12-2;
   * JSONB:SPEC:JSB-3.14.2-1; JSONB:SPEC:JSB-3.14.2-2; JSONB:SPEC:JSB-3.14.2-3
   *
   * @test_Strategy: Assert that a multi-dimensional array is serialized and
   * deserialized as a multi-dimensional array and null array values are
   * correctly serialized and deserialized
   */
  @Test
  public void testMultiDimensionalArray() {
    Integer[][] instance = { { 1, null, 3 },
        { Integer.MIN_VALUE, Integer.MAX_VALUE } };
    String jsonString = jsonb.toJson(new MultiDimensionalArrayContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*\\:\\s*\\[\\s*\\[\\s*1\\s*,\\s*null\\s*,\\s*3\\s*\\]\\s*,\\s*\\[\\s*"
            + Integer.MIN_VALUE + "\\s*,\\s*" + Integer.MAX_VALUE
            + "\\s*\\]\\s*\\]\\s*\\}")) {
      fail("Failed to marshal object with int[][] attribute value.");
    }

    MultiDimensionalArrayContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ [ 1, null, 3 ], [ " + Integer.MIN_VALUE + ", "
            + Integer.MAX_VALUE + " ] ] }",
        MultiDimensionalArrayContainer.class);
    if (!Arrays.deepEquals(instance, unmarshalledObject.getInstance())) {
      fail(
          "Failed to unmarshal object with int[][] attribute value.");
    }

    return; // passed
  }
}
