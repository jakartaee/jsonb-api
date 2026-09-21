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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.json.bind.customizedmapping.nullhandling.model.NillablePropertyContainer;
import ee.jakarta.tck.json.bind.customizedmapping.nullhandling.model.NillablePropertyNonNillableContainer;
import ee.jakarta.tck.json.bind.customizedmapping.nullhandling.model.NonNillableAndNillablePropertyContainer;
import ee.jakarta.tck.json.bind.customizedmapping.nullhandling.model.NonNillablePropertyContainer;
import ee.jakarta.tck.json.bind.customizedmapping.nullhandling.model.NonNillablePropertyNillableContainer;
import ee.jakarta.tck.json.bind.customizedmapping.nullhandling.model.nillable.NillablePackageNillablePropertyNonNillableContainer;
import ee.jakarta.tck.json.bind.customizedmapping.nullhandling.model.nillable.NillablePackageNonNillablePropertyContainer;
import ee.jakarta.tck.json.bind.customizedmapping.nullhandling.model.nonnillable.NonNillablePackageNonNillablePropertyNillableContainer;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

/**
 * @test
 * @sources NullHandlingCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.nullhandling.NullHandlingCustomizationTest
 **/
public class NullHandlingCustomizationTest {

    private final static Pattern PATTERN_NULL = Pattern.compile("\\{\\s*\"stringInstance\"\\s*\\:\\s*null\\s*\\}");

    private final Jsonb jsonb = JsonbBuilder.create();

    /*
     * @testName: testNillableProperty
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.3.1-2
     *
     * @test_Strategy: Assert that property annotated as JsonbProperty with
     * nillable = true having null value is included in marshalling
     */
    @Test
    public void testNillableProperty() {
        String jsonString = jsonb.toJson(new NillablePropertyContainer());
        assertThat("Failed to correctly marshal null property annotated as JsonbProperty with nillable = true.",
                   jsonString, matchesPattern("\\{\\s*\"nillableStringInstance\"\\s*\\:\\s*null\\s*\\}"));
    }

    /*
     * @testName: testNillableTypeNonNillableProperty
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.3-1; JSONB:SPEC:JSB-4.3.1-1;
     * JSONB:SPEC:JSB-4.3.1-2; JSONB:SPEC:JSB-4.3.1-3
     *
     * @test_Strategy: Assert that null property annotated as JsonbProperty with
     * nillable = false of type annotated as JsonbNillable is ignored in
     * marshalling
     */
    @Test
    public void testNillableTypeNonNillableProperty() {
        String jsonString = jsonb.toJson(new NonNillablePropertyNillableContainer());
        assertThat("Failed to correctly ignore null property annotated as JsonbProperty with nillable = false of type "
                           + "annotated as JsonbNillable.",
                   jsonString, matchesPattern("\\{\\s*\\}"));
    }

    /*
     * @testName: testNillablePackageNonNillableProperty
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.3-1; JSONB:SPEC:JSB-4.3.1-1;
     * JSONB:SPEC:JSB-4.3.1-2; JSONB:SPEC:JSB-4.3.1-3
     *
     * @test_Strategy: Assert that property annotated as JsonbProperty with
     * nillable = false of type under package annotated as JsonbNillable is
     * ignored in marshalling
     */
    @Test
    public void testNillablePackageNonNillableProperty() {
        String jsonString = jsonb.toJson(new NillablePackageNonNillablePropertyContainer());
        assertThat("Failed to correctly ignore null property annotated as JsonbProperty(nillable = false) of type under "
                           + "package annotated as JsonbNillable.",
                   jsonString, matchesPattern("\\{\\s*\\}"));
    }

    /*
     * @testName: testNillablePackageNonNillableTypeNillableProperty
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.3-1; JSONB:SPEC:JSB-4.3.1-1;
     * JSONB:SPEC:JSB-4.3.1-2; JSONB:SPEC:JSB-4.3.1-3
     *
     * @test_Strategy: Assert that property annotated as JsonbProperty with
     * nillable =
     * true of type annotated as JsonbNillable(false) under package
     * annotated as JsonbNillable is included in marshalling
     */
    @Test
    public void testNillablePackageNonNillableTypeNillableProperty() {
        String jsonString = jsonb.toJson(new NillablePackageNillablePropertyNonNillableContainer());
        assertThat("Failed to correctly marshal null property annotated as JsonbProperty(nillable = true) of type "
                           + "annotated as JsonbNillable(false) under package annotated as JsonbNillable.",
                   jsonString, matchesPattern("\\{\\s*\"nillableStringInstance\"\\s*\\:\\s*null\\s*\\}"));
    }

    /*
     * @testName:
     * testNullValuesConfigNonNillablePackageNillableTypeNonNillableProperty
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.3-1; JSONB:SPEC:JSB-4.3.1-1;
     * JSONB:SPEC:JSB-4.3.1-2; JSONB:SPEC:JSB-4.3.1-3; JSONB:SPEC:JSB-4.3.2-1
     *
     * @test_Strategy: Assert that null properties annotated as JsonbProperty with
     * nillable = false are ignored in marshalling when using
     * JsonbConfig().withNullValues(true) and type annotated as JsonbNillable
     * under package annotated as JsonbNillable(false)
     */
    @Test
    public void testNullValuesConfigNonNillablePackageNillableTypeNonNillableProperty() {
        String validationMessage = "Failed to correctly ignore null property annotated as JsonbProperty with nillable = false "
                + "when using JsonbConfig().withNullValues(true) and type annotated as JsonbNillable under package annotated "
                + "as JsonbNillable(false).";
        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withNullValues(true));
        String jsonString = jsonb.toJson(new NonNillablePackageNonNillablePropertyNillableContainer());
        assertThat(validationMessage, jsonString, matchesPattern("\\{\\s*\\}"));
    }

    /*
     * @testName: testNullValuesConfigNonNillableTypeNillableProperty
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.3-1; JSONB:SPEC:JSB-4.3.1-1;
     * JSONB:SPEC:JSB-4.3.1-2; JSONB:SPEC:JSB-4.3.1-3; JSONB:SPEC:JSB-4.3.2-1
     *
     * @test_Strategy: Assert that null properties annotated as JsonbProperty with
     * nillable = true are included in marshalling when using
     * JsonbConfig().withNullValues(true) and type annotated as
     * JsonbNillable(false)
     */
    @Test
    public void testNullValuesConfigNonNillableTypeNillableProperty() {
        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withNullValues(true));
        String jsonString = jsonb.toJson(new NillablePropertyNonNillableContainer());
        assertThat("Failed to correctly include null property annotated as JsonbProperty with nillable = true when "
                           + "using JsonbConfig().withNullValues(true) and type annotated as JsonbNillable(false).",
                   jsonString, matchesPattern("\\{\\s*\"nillableStringInstance\"\\s*\\:\\s*null\\s*\\}"));
    }

    /*
     * @testName: testNullValuesConfigNonNillableProperty
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.3-1; JSONB:SPEC:JSB-4.3.1-2;
     * JSONB:SPEC:JSB-4.3.1-3; JSONB:SPEC:JSB-4.3.2-1
     *
     * @test_Strategy: Assert that null properties annotated as JsonbProperty with
     * nillable = false are ignored in marshalling when using
     * JsonbConfig().withNullValues(true)
     */
    @Test
    public void testNullValuesConfigNonNillableProperty() {
        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withNullValues(true));
        String jsonString = jsonb.toJson(new NonNillablePropertyContainer());
        assertThat("Failed to correctly ignore null property annotated as JsonbProperty with nillable = false when "
                           + "using JsonbConfig().withNullValues(true).",
                   jsonString, matchesPattern("\\{\\s*\\}"));
    }

    /*
     * @testName: testNullValuesConfigNonAndNillableProperty
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.3-1; JSONB:SPEC:JSB-4.3-3;
     * JSONB:SPEC:JSB-4.3.1-2; JSONB:SPEC:JSB-4.3.2-1
     *
     * @test_Strategy: Assert that null properties annotated as JsonbProperty with
     * nillable = false are ignored in marshalling when using
     * JsonbConfig().withNullValues(true)
     */
    @Test
    public void testNullValuesConfigNonAndNillableProperty() {
        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withNullValues(true));
        String jsonString = jsonb.toJson(new NonNillableAndNillablePropertyContainer());
        assertThat("Failed to correctly ignore null property annotated both JsonbProperty with nillable = true and "
                           + "JsonbNillable(false) when using JsonbConfig().withNullValues(true)."
                           + "JsonbNillable annotation should take precedence over the JsonbProperty",
                   jsonString, matchesPattern("\\{\\s*\\}"));
    }
}
