/*
 * Copyright (c) 2017, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.json.bind.customizedmapping.binarydata;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.config.BinaryDataStrategy;

import ee.jakarta.tck.json.bind.customizedmapping.binarydata.model.BinaryDataContainer;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

import java.nio.charset.StandardCharsets;

/**
 * @test
 * @sources BinaryDataCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.binarydata.BinaryDataCustomizationTest
 **/
public class BinaryDataCustomizationTest {

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
        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withBinaryDataStrategy(BinaryDataStrategy.BYTE));
        String validationPattern = "\\{\\s*\"data\"\\s*:\\s*\\[\\s*84\\s*,\\s*101\\s*,\\s*115\\s*,\\s*116\\s*,\\s*32\\s*,"
                + "\\s*83\\s*,\\s*116\\s*,\\s*114\\s*,\\s*105\\s*,\\s*110\\s*,\\s*103\\s*]\\s*}";

        String jsonString = jsonb.toJson(new BinaryDataContainer());
        assertThat("Failed to correctly marshal binary data using BYTE binary data encoding.",
                   jsonString, matchesPattern(validationPattern));

        BinaryDataContainer container = jsonb.fromJson("{ \"data\" : [ 84, 101, 115, 116, 32, 83, 116, 114, 105, 110, 103 ] }",
                                                       BinaryDataContainer.class);
        assertThat("Failed to correctly unmarshal binary data using BYTE binary data encoding.",
                   new String(container.getData(), StandardCharsets.UTF_8), is("Test String")); //Data was encoded using UTF-8
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
        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withBinaryDataStrategy(BinaryDataStrategy.BASE_64));

        String jsonString = jsonb.toJson(new BinaryDataContainer());
        assertThat("Failed to correctly marshal binary data using BASE_64 binary data encoding.",
                   jsonString, matchesPattern("\\{\\s*\"data\"\\s*:\\s*\"VGVzdCBTdHJpbmc=\"\\s*}"));

        BinaryDataContainer unmarshalledObject = jsonb.fromJson("{ \"data\" : \"VGVzdCBTdHJpbmc\" }", BinaryDataContainer.class);
        assertThat("Failed to correctly unmarshal binary data using BASE_64 binary data encoding.",
                   new String(unmarshalledObject.getData(), StandardCharsets.UTF_8), is("Test String")); //Data was encoded using UTF-8
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
        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withBinaryDataStrategy(BinaryDataStrategy.BASE_64_URL));

        String jsonString = jsonb.toJson(new BinaryDataContainer());
        assertThat("Failed to correctly marshal binary data using BASE_64_URL binary data encoding.",
                   jsonString, matchesPattern("\\{\\s*\"data\"\\s*:\\s*\"VGVzdCBTdHJpbmc=\"\\s*}"));

        BinaryDataContainer unmarshalledObject = jsonb.fromJson("{ \"data\" : \"VGVzdCBTdHJpbmc=\" }", BinaryDataContainer.class);
        assertThat("Failed to correctly unmarshal binary data using BASE_64_URL binary data encoding.",
                   new String(unmarshalledObject.getData(), StandardCharsets.UTF_8), is("Test String")); //Data was encoded using UTF-8
    }
}
