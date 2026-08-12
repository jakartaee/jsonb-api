/*
 * Copyright (c) 2017, 2024 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.json.bind.defaultmapping.bignumbers;

import java.math.BigDecimal;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;

/**
 * @test
 * @sources BigNumbersMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.bignumbers.BigNumbersMappingTest
 **/
public class BigNumbersMappingTest {

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
    @Disabled("See https://github.com/jakartaee/jsonb-api/issues/180")
    public void testBigNumberMarshalling() {
        String jsonString = jsonb.toJson(new Object() {
            @SuppressWarnings("unused")
            public Number number = new BigDecimal("0.10000000000000001");
        });
        assertThat("Failed to correctly marshal number of greater precision than IEEE 754 as string.",
                   jsonString, matchesPattern("\\{\\s*\"number\"\\s*:\\s*\"0.10000000000000001\"\\s*\\}"));
    }
}
