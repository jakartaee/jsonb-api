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

package jakarta.json.bind.tck.customizedmapping.binarydata;

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
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.config.BinaryDataStrategy;
import jakarta.json.bind.tck.customizedmapping.binarydata.model.BinaryDataContainer;

/**
 * @test
 * @sources BinaryDataCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.binarydata.BinaryDataCustomizationTest
 **/
@RunWith(Arquillian.class)
public class BinaryDataCustomizationTest {
    
    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, MethodHandles.lookup().lookupClass().getPackage().getName());
    }

  /*
   * @testName: testByteBinaryDataEncoding
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.10-1
   *
   * @test_Strategy: Assert that binary data is correctly encoded using BYTE
   * binary data encoding
   */
  @Test
  public void testByteBinaryDataEncoding() {
    Jsonb jsonb = JsonbBuilder.create(
        new JsonbConfig().withBinaryDataStrategy(BinaryDataStrategy.BYTE));

    String jsonString = jsonb.toJson(new BinaryDataContainer());
    if (!jsonString.matches(
        "\\{\\s*\"data\"\\s*:\\s*\\[\\s*84\\s*,\\s*101\\s*,\\s*115\\s*,\\s*116\\s*,\\s*32\\s*,\\s*83\\s*,\\s*116\\s*,\\s*114\\s*,\\s*105\\s*,\\s*110\\s*,\\s*103\\s*]\\s*}")) {
      fail(
          "Failed to correctly marshal binary data using BYTE binary data encoding.");
    }

    BinaryDataContainer unmarshalledObject = jsonb.fromJson(
        "{ \"data\" : [ 84, 101, 115, 116, 32, 83, 116, 114, 105, 110, 103 ] }",
        BinaryDataContainer.class);
    if (!"Test String".equals(new String(unmarshalledObject.getData()))) {
      fail(
          "Failed to correctly unmarshal binary data using BYTE binary data encoding.");
    }

    return; // passed
  }

  /*
   * @testName: testBase64BinaryDataEncoding
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.10-1
   *
   * @test_Strategy: Assert that binary data is correctly encoded using BASE_64
   * binary data encoding
   */
  @Test
  public void testBase64BinaryDataEncoding() {
    Jsonb jsonb = JsonbBuilder.create(
        new JsonbConfig().withBinaryDataStrategy(BinaryDataStrategy.BASE_64));

    String jsonString = jsonb.toJson(new BinaryDataContainer());
    if (!jsonString
        .matches("\\{\\s*\"data\"\\s*:\\s*\"VGVzdCBTdHJpbmc=\"\\s*}")) {
      fail(
          "Failed to correctly marshal binary data using BASE_64 binary data encoding.");
    }

    BinaryDataContainer unmarshalledObject = jsonb.fromJson(
        "{ \"data\" : \"VGVzdCBTdHJpbmc\" }", BinaryDataContainer.class);
    if (!"Test String".equals(new String(unmarshalledObject.getData()))) {
      fail(
          "Failed to correctly unmarshal binary data using BASE_64 binary data encoding.");
    }

    return; // passed
  }

  /*
   * @testName: testBase64UrlBinaryDataEncoding
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.10-1
   *
   * @test_Strategy: Assert that binary data is correctly encoded using
   * BASE_64_URL binary data encoding
   */
  @Test
  public void testBase64UrlBinaryDataEncoding() {
    Jsonb jsonb = JsonbBuilder.create(new JsonbConfig()
        .withBinaryDataStrategy(BinaryDataStrategy.BASE_64_URL));

    String jsonString = jsonb.toJson(new BinaryDataContainer());
    if (!jsonString
        .matches("\\{\\s*\"data\"\\s*:\\s*\"VGVzdCBTdHJpbmc=\"\\s*}")) {
      fail(
          "Failed to correctly marshal binary data using BASE_64_URL binary data encoding.");
    }

    BinaryDataContainer unmarshalledObject = jsonb.fromJson(
        "{ \"data\" : \"VGVzdCBTdHJpbmc=\" }", BinaryDataContainer.class);
    if (!"Test String".equals(new String(unmarshalledObject.getData()))) {
      fail(
          "Failed to correctly unmarshal binary data using BASE_64_URL binary data encoding.");
    }

    return; // passed
  }
}
