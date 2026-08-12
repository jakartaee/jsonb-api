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

package ee.jakarta.tck.json.bind.defaultmapping.attributeorder;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import ee.jakarta.tck.json.bind.defaultmapping.attributeorder.model.ExtendedContainer;
import ee.jakarta.tck.json.bind.defaultmapping.attributeorder.model.SimpleContainer;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

/**
 * @test
 * @sources AttributeOrderMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.attributeorder.AttributeOrderMappingTest
 **/
public class AttributeOrderMappingTest {

    private final Jsonb jsonb = JsonbBuilder.create();

    /*
     * @testName: testClassAttributeOrder
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.13-1; JSONB:SPEC:JSB-3.13-2
     *
     * @test_Strategy: Assert that declared fields are marshalled in
     * lexicographical order and unmarshalled in the order of appearance in the
     * JSON document
     */
    @Test
    public void testClassAttributeOrder() {
        String jsonString = jsonb.toJson(new SimpleContainer() {
            {
                setIntInstance(0);
                setStringInstance("Test String");
                setLongInstance(0L);
            }
        });
        String validationPattern = "\\{\\s*\"intInstance\"\\s*\\:\\s*0\\s*,\\s*\"longInstance\"\\s*\\:\\s*0\\s*,"
                + "\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}";
        assertThat("Failed to lexicographically order class attributes.", jsonString, matchesPattern(validationPattern));

        String toDeserialize = "{ \"intInstance\" : 1, \"stringInstance\" : \"Test String\", \"longInstance\" : 0 }";
        SimpleContainer unmarshalledObject = jsonb.fromJson(toDeserialize, SimpleContainer.class);
        assertThat("Failed to set class attributes in order of appearance.", unmarshalledObject.getIntInstance(), is(3));
    }

    /*
     * @testName: testExtendedClassAttributeOrder
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.13-1; JSONB:SPEC:JSB-3.13-2
     *
     * @test_Strategy: Assert that declared fields of super class are marshalled
     * before declared fields of child class and all are unmarshalled in the order
     * of appearance in the JSON document
     */
    @Test
    public void testExtendedClassAttributeOrder() {
        String jsonString = jsonb.toJson(new ExtendedContainer() {
            {
                setIntInstance(0);
                setStringInstance("Test String");
                setLongInstance(0L);
                setFloatInstance(0f);
                setShortInstance((short) 0);
            }
        });
        String validationPattern = "\\{\\s*\"intInstance\"\\s*\\:\\s*0\\s*,\\s*\"longInstance\"\\s*\\:\\s*0\\s*,"
                + "\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\,\\s*\"floatInstance\"\\s*\\:\\s*0.0\\s*,"
                + "\\s*\"shortInstance\"\\s*\\:\\s*0\\s*}";
        assertThat("Failed to correctly order extended class attributes.", jsonString, matchesPattern(validationPattern));

        String toDeserialize = "{ \"intInstance\" : 1, \"shortInstance\" : 0, \"stringInstance\" : \"Test String\", "
                + "\"floatInstance\" : 0.0, \"longInstance\" : 0 }";
        ExtendedContainer unmarshalledObject = jsonb.fromJson(toDeserialize, ExtendedContainer.class);
        assertThat("Failed to set extended class attributes in order of appearance.",
                   unmarshalledObject.getIntInstance(), is(5));
    }
}
