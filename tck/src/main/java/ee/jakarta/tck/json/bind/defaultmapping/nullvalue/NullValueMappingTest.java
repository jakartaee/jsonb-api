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

package ee.jakarta.tck.json.bind.defaultmapping.nullvalue;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import ee.jakarta.tck.json.bind.defaultmapping.nullvalue.model.NullArrayContainer;
import ee.jakarta.tck.json.bind.defaultmapping.nullvalue.model.NullValueContainer;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.nullValue;

/**
 * @test
 * @sources NullValueMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.nullvalue.NullValueMappingTest
 **/
public class NullValueMappingTest {

    private final Jsonb jsonb = JsonbBuilder.create();

    /*
     * @testName: testNullAttributeValue
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.14.1-1; JSONB:SPEC:JSB-3.14.1-2;
     * JSONB:SPEC:JSB-3.14.1-3
     *
     * @test_Strategy: Assert that fields with null value are ignored during
     * marshalling and that during unmarshalling missing attributes are not set,
     * maintaining original value, and null attributes are correctly unmarshalled
     */
    @Test
    public void testNullAttributeValue() {
        String jsonString = jsonb.toJson(new NullValueContainer() {{
            setInstance(null);
        }});
        assertThat("Failed to ignore displaying property with null value.", jsonString, matchesPattern("\\{\\s*\\}"));

        NullValueContainer unmarshalledObject = jsonb.fromJson("{ }", NullValueContainer.class);
        assertThat("Failed to ignore calling setter of absent property during unmarshalling.",
                   unmarshalledObject.getInstance(), is(("Test String")));

        unmarshalledObject = jsonb.fromJson("{ \"instance\" : null }", NullValueContainer.class);
        assertThat("Failed to set property to null.", unmarshalledObject.getInstance(), nullValue());
    }

    /*
     * @testName: testNullArrayValue
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.14.2-1; JSONB:SPEC:JSB-3.14.2-2;
     * JSONB:SPEC:JSB-3.14.2-3
     *
     * @test_Strategy: Assert that a null array value is marked as null during
     * marshalling and a null value is set to the appropriate array index during
     * unmrashalling of an array containing a null value
     */
    @Test
    public void testNullArrayValue() {
        String jsonString = jsonb.toJson(new NullArrayContainer() {{
            setInstance(new String[] {"Test 1", null, "Test 2"});
        }});
        assertThat("Failed to correctly display null array value.",
                   jsonString,
                   matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test 1\"\\s*,\\s*null\\s*,\\s*\"Test 2\"\\s*\\]\\s*\\}"));

        NullArrayContainer unmarshalledObject = jsonb.fromJson("{ \"instance\" : [ \"Test 1\", null, \"Test 2\" ] }",
                                                               NullArrayContainer.class);
        assertThat("Failed to correctly set null array value.",
                   unmarshalledObject.getInstance(), arrayContaining("Test 1", null, "Test 2"));
    }
}
