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

package jakarta.json.bind.tck.defaultmapping.interfaces;

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
import jakarta.json.bind.JsonbException;
import jakarta.json.bind.tck.TypeContainer;
import jakarta.json.bind.tck.defaultmapping.interfaces.model.InterfaceContainer;

/**
 * @test
 * @sources InterfaceMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.interfaces.InterfaceMappingTest
 **/
@RunWith(Arquillian.class)
public class InterfaceMappingTest {
    
    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, MethodHandles.lookup().lookupClass().getPackage().getName());
    }
    
  private final Jsonb jsonb = JsonbBuilder.create();

  /*
   * @testName: testDeserializationToInterface
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-2
   *
   * @test_Strategy: Assert that an error is reported when unmarshalling to an
   * arbitrary interface
   */
  @Test
  public void testDeserializationToInterface() {
    try {
      jsonb.fromJson("{ \"instance\" : \"Test String\" }", TypeContainer.class);
      fail(
          "An exception is expected when unmarshalling a class to an interface.");
    } catch (JsonbException x) {
      return; // passed
    }
  }

  /*
   * @testName: testInterfaceField
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-2
   *
   * @test_Strategy: Assert that an error is reported when unmarshalling to an
   * arbitrary interface
   */
  @Test
  public void testInterfaceField() {
    try {
      jsonb.fromJson("{ \"instance\" : { \"instance\" : \"Test String\" } }",
          InterfaceContainer.class);
      fail(
          "An exception is expected when unmarshalling a class with an interface property.");
    } catch (JsonbException x) {
      return; // passed
    }
  }
}
