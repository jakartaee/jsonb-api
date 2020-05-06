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

package jakarta.json.bind.tck.defaultmapping.uniqueness;

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
import jakarta.json.bind.tck.defaultmapping.uniqueness.model.SimpleContainer;

/**
 * @test
 * @sources PropertyUniquenessTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.uniqueness.PropertyUniquenessTest
 **/
@RunWith(Arquillian.class)
public class PropertyUniquenessTest {
    
    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, MethodHandles.lookup().lookupClass().getPackage().getName());
    }
    
  private final Jsonb jsonb = JsonbBuilder.create();

  /*
   * @testName: testUniqueProperties
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.19-1; JSONB:SPEC:JSB-3.19-2
   *
   * @test_Strategy: Assert that an exception is thrown when duplicate property
   * names exist
   */
  @Test
  public void testUniqueProperties() {
    try {
      jsonb.toJson(new SimpleContainer());
      fail(
          "An exception is expected when marshalling a class with duplicate attribute names.");
    } catch (JsonbException x) {
      return; // passed
    }
  }
}
