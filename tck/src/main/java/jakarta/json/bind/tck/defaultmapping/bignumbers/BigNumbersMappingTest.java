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

package jakarta.json.bind.tck.defaultmapping.bignumbers;

import static org.junit.Assert.fail;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

/**
 * @test
 * @sources BigNumbersMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.bignumbers.BigNumbersMappingTest
 **/
@RunWith(Arquillian.class)
public class BigNumbersMappingTest {
    
    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, MethodHandles.lookup().lookupClass().getPackage().getName());
    }

  private final Jsonb jsonb = JsonbBuilder.create();

  /*
   * @testName: testBigNumberMarshalling
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.16-1
   *
   * @test_Strategy: Assert that numbers of greater magnitude or precision than
   * IEEE 754 are serialized as strings as specified by IJSON RFC 7493 (default
   * to JSON-B)
   */
  @Test
  @Ignore("See https://github.com/eclipse-ee4j/jsonb-api/issues/180")
  public void testBigNumberMarshalling() {
    String jsonString = jsonb.toJson(new Object() {
      @SuppressWarnings("unused")
      public Number number = new BigDecimal("0.10000000000000001");
    });
    if (!jsonString
        .matches("\\{\\s*\"number\"\\s*:\\s*\"0.10000000000000001\"\\s*\\}")) {
      fail(
          "Failed to correctly marshal number of greater precision than IEEE 754 as string.");
    }
  }
}
