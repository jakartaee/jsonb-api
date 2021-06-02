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

package jakarta.json.bind.tck.defaultmapping.ignore;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.tck.defaultmapping.ignore.model.StringContainer;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * @test
 * @sources MustIgnoreMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.ignore.MustIgnoreMappingTest
 **/
public class MustIgnoreMappingTest {

  /*
   * @testName: testIgnoreUnknownAttribute
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.18-1
   *
   * @test_Strategy: Assert that unknown attributes are ignored during
   * unmarshalling
   */
  @Test
  public void testIgnoreUnknownAttribute() {
      Jsonb jsonb = JsonbBuilder.create();
      String toDeserialize = "{ \"instance\" : \"Test String\", \"newInstance\" : 0 }";
      StringContainer container = assertDoesNotThrow(()->jsonb.fromJson(toDeserialize, StringContainer.class),
                                                     "An exception is not expected when coming across "
                                                             + "a non existent attribute during deserialization.");

      assertThat("Failed to deserialize into a class with less attributes than exist in the JSON string.",
                 container.getInstance(), is("Test String"));
  }
}
