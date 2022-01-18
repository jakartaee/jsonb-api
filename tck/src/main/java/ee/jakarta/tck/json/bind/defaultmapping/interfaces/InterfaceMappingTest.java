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

package ee.jakarta.tck.json.bind.defaultmapping.interfaces;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;

import ee.jakarta.tck.json.bind.TypeContainer;
import ee.jakarta.tck.json.bind.defaultmapping.interfaces.model.InterfaceContainer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @test
 * @sources InterfaceMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.interfaces.InterfaceMappingTest
 **/
public class InterfaceMappingTest {

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
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"instance\" : \"Test String\" }", TypeContainer.class),
                     "An exception is expected when unmarshalling a class to an interface.");
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
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"instance\" : { \"instance\" : \"Test String\" } }", InterfaceContainer.class),
                     "An exception is expected when unmarshalling a class with an interface property.");
    }
}
