/*
 * Copyright (c) 2017, 2021 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.json.bind.tck.defaultmapping.specifictypes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import jakarta.json.bind.tck.MappingTester;
import jakarta.json.bind.tck.SimpleMappingTester;
import jakarta.json.bind.tck.TypeContainer;
import jakarta.json.bind.tck.defaultmapping.specifictypes.model.BigDecimalContainer;
import jakarta.json.bind.tck.defaultmapping.specifictypes.model.BigIntegerContainer;
import jakarta.json.bind.tck.defaultmapping.specifictypes.model.OptionalArrayContainer;
import jakarta.json.bind.tck.defaultmapping.specifictypes.model.OptionalContainer;
import jakarta.json.bind.tck.defaultmapping.specifictypes.model.OptionalDoubleContainer;
import jakarta.json.bind.tck.defaultmapping.specifictypes.model.OptionalIntContainer;
import jakarta.json.bind.tck.defaultmapping.specifictypes.model.OptionalLongContainer;
import jakarta.json.bind.tck.defaultmapping.specifictypes.model.OptionalTypeContainer;
import jakarta.json.bind.tck.defaultmapping.specifictypes.model.SimpleContainer;
import jakarta.json.bind.tck.defaultmapping.specifictypes.model.URIContainer;
import jakarta.json.bind.tck.defaultmapping.specifictypes.model.URLContainer;
import org.junit.jupiter.api.Test;

/**
 * @test
 * @sources SpecificTypesMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.specifictypes.SpecificTypesMappingTest
 **/
public class SpecificTypesMappingTest {

    /*
     * @testName: testBigIntegerMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.1-1;
     * JSONB:SPEC:JSB-3.4.1-2
     *
     * @test_Strategy: Assert that marshalling and unmarshalling of
     * java.math.BigInteger type are performed according to the toString method
     * and applicable String argument constructor
     */
    @Test
    public void testBigIntegerMapping() {
        new MappingTester<>(BigIntegerContainer.class).test(new BigInteger("0"), "0");
    }

    /*
     * @testName: testBigDecimalMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.1-1;
     * JSONB:SPEC:JSB-3.4.1-2
     *
     * @test_Strategy: Assert that marshalling and unmarshalling of
     * java.math.BigDecimal type are performed according to the toString method
     * and applicable String argument constructor
     */
    @Test
    public void testBigDecimalMapping() {
        new MappingTester<>(BigDecimalContainer.class).test(new BigDecimal("0.0"), "0.0");
    }

    /*
     * @testName: testURLMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.2-1;
     * JSONB:SPEC:JSB-3.4.2-2
     *
     * @test_Strategy: Assert that marshalling and unmarshalling of java.net.URL
     * are performed according to the toString method and applicable String
     * argument constructor
     */
    @Test
    public void testURLMapping() throws Exception {
        new MappingTester<>(URLContainer.class).test(new URL("http://www.host.com:80"), "\"http://www.host.com:80\"");
    }

    /*
     * @testName: testURIMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.2-1;
     * JSONB:SPEC:JSB-3.4.2-2
     *
     * @test_Strategy: Assert that marshalling and unmarshalling of java.net.URI
     * type are performed according to the toString method and applicable String
     * argument constructor
     */
    @Test
    public void testURIMapping() throws URISyntaxException {
        new MappingTester<>(URIContainer.class).test(new URI("http://www.host.com:80"), "\"http://www.host.com:80\"");
    }

    /*
     * @testName: testOptionalMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.3-1;
     * JSONB:SPEC:JSB-3.4.3-5; JSONB:SPEC:JSB-3.4.3-6; JSONB:SPEC:JSB-3.4.3-7
     *
     * @test_Strategy: Assert that non-empty java.util.Optional is correctly
     * handled as defined for each type
     */
    @Test
    public void testOptionalMapping() {
        new MappingTester<>(OptionalContainer.class).test(Optional.of("String Value"), "\"String Value\"");
    }

    /*
     * @testName: testOptionalObjectMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.3-1;
     * JSONB:SPEC:JSB-3.4.3-5; JSONB:SPEC:JSB-3.4.3-6; JSONB:SPEC:JSB-3.4.3-7
     *
     * @test_Strategy: Assert that non-empty java.util.Optional of a complex type
     * is correctly handled
     */
    @Test
    public void testOptionalObjectMapping() {
        OptionalTypeContainer container = new OptionalTypeContainer();
        SimpleContainer simpleContainer = new SimpleContainer();
        simpleContainer.setStringInstance("String Value");
        container.setInstance(Optional.of(simpleContainer));

        new SimpleMappingTester<>(OptionalTypeContainer.class, TypeContainer.class).test(
                container,
                "\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"stringInstance\"\\s*:\\s*\"String Value\"\\s*}\\s*}",
                "{ \"instance\" : { \"stringInstance\" : \"String Value\" } }",
                container);
    }

    /*
     * @testName: testEmptyOptionalMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.3-2;
     * JSONB:SPEC:JSB-3.4.3-4; JSONB:SPEC:JSB-3.14.1-3
     *
     * @test_Strategy: Assert that empty java.util.Optional is ignored during
     * marshalling and null value is returned as empty Optional value during
     * unmarshalling
     */
    @Test
    public void testEmptyOptionalMapping() {
        OptionalContainer optionalContainer = new OptionalContainer();
        optionalContainer.setInstance(Optional.empty());
        new SimpleMappingTester<>(OptionalContainer.class, TypeContainer.class).test(
                optionalContainer,
                "\\{\\s*}",
                "{ \"instance\" : null }",
                optionalContainer);
    }

    /*
     * @testName: testEmptyOptionalArrayMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.3-2;
     * JSONB:SPEC:JSB-3.4.3-3; JSONB:SPEC:JSB-3.4.3-4; JSONB:SPEC:JSB-3.14.1-3
     *
     * @test_Strategy: Assert that empty java.util.Optional instances in array
     * items are serialized as null and null value is returned as empty Optional
     * value during unmarshalling
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testEmptyOptionalArrayMapping() {
        OptionalArrayContainer optionalContainer = new OptionalArrayContainer();
        optionalContainer.setInstance(new Optional[] {Optional.empty()});
        new SimpleMappingTester<>(OptionalArrayContainer.class, TypeContainer.class).test(
                optionalContainer,
                "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*null\\s*]\\s*}",
                "{ \"instance\" : [ null ] }",
                optionalContainer);
    }

    /*
     * @testName: testOptionalIntMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.3-1;
     * JSONB:SPEC:JSB-3.4.3-5
     *
     * @test_Strategy: Assert that non-empty java.util.OptionalInt is correctly
     * handled as defined for Integer type
     */
    @Test
    public void testOptionalIntMapping() {
        new MappingTester<>(OptionalIntContainer.class).test(OptionalInt.of(0), "0");
    }

    /*
     * @testName: testEmptyOptionalIntMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.3-2;
     * JSONB:SPEC:JSB-3.4.3-4; JSONB:SPEC:JSB-3.14.1-3
     *
     * @test_Strategy: Assert that empty java.util.OptionalInt is ignored during
     * marshalling and null value is returned as empty OptionalInt value during
     * unmarshalling
     */
    @Test
    public void testEmptyOptionalIntMapping() {
        OptionalIntContainer optionalContainer = new OptionalIntContainer();
        optionalContainer.setInstance(OptionalInt.empty());
        new SimpleMappingTester<>(OptionalIntContainer.class, TypeContainer.class).test(
                optionalContainer,
                "\\{\\s*}",
                "{ \"instance\" : null }",
                optionalContainer);
    }

    /*
     * @testName: testOptionalLongMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.3-1;
     * JSONB:SPEC:JSB-3.4.3-5
     *
     * @test_Strategy: Assert that non-empty java.util.OptionalLong is correctly
     * handled as defined for Long type
     */
    @Test
    public void testOptionalLongMapping() {
        new MappingTester<>(OptionalLongContainer.class).test(OptionalLong.of(0), "0");
    }

    /*
     * @testName: testEmptyOptionalLongMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.3-2;
     * JSONB:SPEC:JSB-3.4.3-4; JSONB:SPEC:JSB-3.14.1-3
     *
     * @test_Strategy: Assert that empty java.util.OptionalLong is ignored during
     * marshalling and null value is returned as empty OptionalLong value during
     * unmarshalling
     */
    @Test
    public void testEmptyOptionalLongMapping() {
        OptionalLongContainer optionalContainer = new OptionalLongContainer();
        optionalContainer.setInstance(OptionalLong.empty());
        new SimpleMappingTester<>(OptionalLongContainer.class, TypeContainer.class).test(
                optionalContainer,
                "\\{\\s*}",
                "{ \"instance\" : null }",
                optionalContainer);
    }

    /*
     * @testName: testOptionalDoubleMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.3-1;
     * JSONB:SPEC:JSB-3.4.3-5
     *
     * @test_Strategy: Assert that non-empty java.util.OptionalDouble is correctly
     * handled as defined for Double type
     */
    @Test
    public void testOptionalDoubleMapping() {
        new MappingTester<>(OptionalDoubleContainer.class).test(OptionalDouble.of(0.0), "0.0");
    }

    /*
     * @testName: testEmptyOptionalDoubleMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.4-1; JSONB:SPEC:JSB-3.4.3-2;
     * JSONB:SPEC:JSB-3.4.3-4; JSONB:SPEC:JSB-3.14.1-3
     *
     * @test_Strategy: Assert that empty java.util.OptionalDouble is ignored
     * during marshalling and null value is returned as empty OptionalDouble value
     * during unmarshalling
     */
    @Test
    public void testEmptyOptionalDoubleMapping() {
        OptionalDoubleContainer optionalContainer = new OptionalDoubleContainer();
        optionalContainer.setInstance(OptionalDouble.empty());
        new SimpleMappingTester<>(OptionalDoubleContainer.class, TypeContainer.class).test(
                optionalContainer,
                "\\{\\s*}",
                "{ \"instance\" : null }",
                optionalContainer);
    }
}
