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

package jakarta.json.bind.tck.defaultmapping.polymorphictypes;

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
import jakarta.json.bind.tck.defaultmapping.polymorphictypes.model.StringContainer;
import jakarta.json.bind.tck.defaultmapping.polymorphictypes.model.StringContainerSubClass;

/**
 * @test
 * @sources PolymorphicMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.polymorphictypes.PolymorphicMappingTest
 **/
@RunWith(Arquillian.class)
public class DefaultPolymorphicMappingTest {
    
    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, MethodHandles.lookup().lookupClass().getPackage().getName());
    }

  private final Jsonb jsonb = JsonbBuilder.create();

  /*
   * @testName: testPolymorphicTypes
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.8-4
   *
   * @test_Strategy: Assert that unmarshalling into polymorphic types is not
   * supported when polymorphic information is missing
   */
  @Test
  public void testPolymorphicTypes() {
    String jsonString = jsonb.toJson(new StringContainerSubClass());
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*,\\s*\"newInstance\"\\s*:\\s*\"SubClass Test String\"\\s*\\}")) {
      fail("Failed to get attribute value from subclass.");
    }

    StringContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : \"New Test String\", \"newInstance\" : \"New SubClass Test String\" }",
        StringContainer.class);
    if (StringContainerSubClass.class
        .isAssignableFrom(unmarshalledObject.getClass())) {
      fail("Polymorphic types support is not expected for classes with no polymorphic information.");
    }
    return; // passed
  }
}
