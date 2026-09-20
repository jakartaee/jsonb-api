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

package ee.jakarta.tck.json.bind.customizedmapping.nullhandling;

import java.util.regex.Pattern;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

import ee.jakarta.tck.json.bind.customizedmapping.nullhandling.model.NillableContainer;
import ee.jakarta.tck.json.bind.customizedmapping.nullhandling.model.NonNillableContainer;
import ee.jakarta.tck.json.bind.customizedmapping.nullhandling.model.SimpleContainer;
import ee.jakarta.tck.json.bind.customizedmapping.nullhandling.model.nillable.NillablePackageSimpleContainer;
import ee.jakarta.tck.json.bind.customizedmapping.nullhandling.model.nonnillable.NonNillablePackageNillableContainer;
import ee.jakarta.tck.json.bind.customizedmapping.nullhandling.model.nonnillable.NonNillablePackageSimpleContainer;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;

/**
 * @test
 * @sources NullHandlingCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.nullhandling.NullHandlingCustomizationTest
 **/
public class NullHandlingCustomizationTest {

    private final static Pattern PATTERN_NULL = Pattern.compile("\\{\\s*\"stringInstance\"\\s*\\:\\s*null\\s*\\}");

    private final Jsonb jsonb = JsonbBuilder.create();

    /*
     * @testName: testNillableType
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.3.1-1
     *
     * @test_Strategy: Assert that type annotated as JsonbNillable includes null
     * properties in marshalling
     */
    @Test
    public void testNillableType() {
        String jsonString = jsonb.toJson(new NillableContainer());
        assertThat("Failed to correctly marshal null property of type annotated as JsonbNillable.",
                   jsonString, matchesPattern(PATTERN_NULL));
    }

    /*
     * @testName: testNillablePackage
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.3.1-1
     *
     * @test_Strategy: Assert that type under package annotated as JsonbNillable
     * includes null properties in marshalling
     */
    @Test
    public void testNillablePackage() {
        String jsonString = jsonb.toJson(new NillablePackageSimpleContainer());
        assertThat("Failed to correctly marshal null property of type under package annotated as JsonbNillable.",
                   jsonString, matchesPattern(PATTERN_NULL));
    }

    /*
     * @testName: testNullValuesConfig
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.3.2-1
     *
     * @test_Strategy: Assert that null properties are included in marshalling
     * when using JsonbConfig().withNullValues(true)
     */
    @Test
    public void testNullValuesConfig() {
        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withNullValues(true));
        String jsonString = jsonb.toJson(new SimpleContainer());
        assertThat("Failed to correctly marshal null properties when using JsonbConfig().withNullValues(true).",
                   jsonString, matchesPattern(PATTERN_NULL));
    }

    /*
     * @testName: testNullValuesConfigNonNillablePackage
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.3-1; JSONB:SPEC:JSB-4.3.1-1;
     * JSONB:SPEC:JSB-4.3.1-3; JSONB:SPEC:JSB-4.3.2-1
     *
     * @test_Strategy: Assert that null properties are ignored in marshalling when
     * using JsonbConfig().withNullValues(true) and type under package annotated
     * as JsonbNillable(false)
     */
    @Test
    public void testNullValuesConfigNonNillablePackage() {
        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withNullValues(true));
        String jsonString = jsonb.toJson(new NonNillablePackageSimpleContainer());
        assertThat("Failed to correctly ignore null properties when using JsonbConfig().withNullValues(true) and type under "
                           + "package annotated as JsonbNillable(false).",
                   jsonString, matchesPattern("\\{\\s*\\}"));
    }

    /*
     * @testName: testNullValuesConfigNonNillablePackageNillableType
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.3-1; JSONB:SPEC:JSB-4.3.1-1;
     * JSONB:SPEC:JSB-4.3.1-3; JSONB:SPEC:JSB-4.3.2-1
     *
     * @test_Strategy: Assert that null properties are included in marshalling
     * when using JsonbConfig().withNullValues(true) and type annotated as
     * JsonbNillable under package annotated as JsonbNillable(false)
     */
    @Test
    public void testNullValuesConfigNonNillablePackageNillableType() {
        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withNullValues(true));
        String jsonString = jsonb.toJson(new NonNillablePackageNillableContainer());
        assertThat("Failed to correctly marshal null properties when using JsonbConfig().withNullValues(true) and type "
                           + "annotated as JsonbNillable under package annotated as JsonbNillable(false).",
                   jsonString, matchesPattern("\\{\\s*\"stringInstance\"\\s*\\:\\s*null\\s*\\}"));
    }

    /*
     * @testName: testNullValuesConfigNonNillableType
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.3-1; JSONB:SPEC:JSB-4.3.1-1;
     * JSONB:SPEC:JSB-4.3.1-3; JSONB:SPEC:JSB-4.3.2-1
     *
     * @test_Strategy: Assert that null properties are ignored in marshalling when
     * using JsonbConfig().withNullValues(true) and type annotated as
     * JsonbNillable(false)
     */
    @Test
    public void testNullValuesConfigNonNillableType() {
        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withNullValues(true));
        String jsonString = jsonb.toJson(new NonNillableContainer());
        assertThat("Failed to correctly ignore null property when using JsonbConfig().withNullValues(true) and type "
                           + "annotated as JsonbNillable(false).",
                   jsonString, matchesPattern("\\{\\s*\\}"));
    }
}
