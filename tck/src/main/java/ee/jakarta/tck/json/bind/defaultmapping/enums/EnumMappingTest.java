/*
 * Copyright (c) 2017, 2022 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.json.bind.defaultmapping.enums;

import ee.jakarta.tck.json.bind.defaultmapping.enums.model.EnumContainer;
import ee.jakarta.tck.json.bind.MappingTester;
import org.junit.jupiter.api.Test;

/**
 * @test
 * @sources EnumMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.enums.EnumMappingTest
 **/
public class EnumMappingTest {

    /*
     * @testName: testEnum
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.9-1; JSONB:SPEC:JSB-3.9-2
     *
     * @test_Strategy: Assert that enum is correctly handled
     */
    @Test
    public void testEnum() {
        MappingTester<EnumContainer.Enumeration> enumMappingTester = new MappingTester<>(
                EnumContainer.class);
        enumMappingTester.test(EnumContainer.Enumeration.ONE, "\"ONE\"");
        enumMappingTester.test(EnumContainer.Enumeration.TWO, "\"TWO\"");
    }
}
