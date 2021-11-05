/*
 * Copyright (c) 2021 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.json.bind.tck.customizedmapping.instantiation;

import java.lang.invoke.MethodHandles;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.JsonbException;
import jakarta.json.bind.tck.customizedmapping.instantiation.model.OptionalCreatorParamsContainer;
import jakarta.json.bind.tck.customizedmapping.instantiation.model.OptionalStringParamContainer;
import jakarta.json.bind.tck.customizedmapping.instantiation.model.ParameterTypeOptionalContainer;
import jakarta.json.bind.tck.customizedmapping.instantiation.model.PrimitiveTypeContainer;
import jakarta.json.bind.tck.customizedmapping.instantiation.model.RequiredCreatorParamsContainer;
import jakarta.json.bind.tck.customizedmapping.instantiation.model.RequiredStringParamContainer;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.fail;

@RunWith(Arquillian.class)
public class OptionalCreatorParametersTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, MethodHandles.lookup().lookupClass().getPackage().getName());
    }

    private final Jsonb jsonb = JsonbBuilder.create();

    @Test
    public void testCreatorMethodWithOptionalParameter() {
        OptionalStringParamContainer unmarshalledObject = jsonb.fromJson("{ \"paramTwo\" : 1 }",
                                                                         OptionalStringParamContainer.class);

        if (unmarshalledObject.getParamOne() != null || unmarshalledObject.getParamTwo() != 1) {
            fail("Failed to instantiate type using JsonbCreator annotated factory method with optional parameter");
        }
        unmarshalledObject = jsonb.fromJson("{ \"paramOne\" : \"some value\", \"paramTwo\" : 2 }",
                                            OptionalStringParamContainer.class);

        if (!"some value".equals(unmarshalledObject.getParamOne()) || unmarshalledObject.getParamTwo() != 2) {
            fail("Failed to instantiate type using JsonbCreator annotated factory method with optional parameter");
        }
    }

    @Test
    public void testPropertyOptionalityNotChanged() {
        OptionalCreatorParamsContainer unmarshalledObject = jsonb.fromJson("{ }", OptionalCreatorParamsContainer.class);

        if (unmarshalledObject.getParamOne() != null || unmarshalledObject.getParamTwo() != null) {
            fail("Failed to instantiate type using JsonbCreator annotated factory method with all optional parameters "
                         + "set by creator property");
        }
    }

    @Test
    public void testAllParametersRequiredSetByMethodAnnotation() {
        try {
            jsonb.fromJson("{ }", RequiredCreatorParamsContainer.class);
            fail("Instantiation of the type should have failed, because required creator parameters were missing");
        } catch (JsonbException ignored) {
            //Everything worked as expected
        }
        try {
            jsonb.fromJson("{ \"paramOne\" : \"some value\" }", RequiredCreatorParamsContainer.class);
            fail("Instantiation of the type should have failed, because required creator parameter was missing");
        } catch (JsonbException ignored) {
            //Everything worked as expected
        }
        RequiredCreatorParamsContainer unmarshalledObject = jsonb.fromJson("{ \"paramOne\" : \"some value\", "
                                                                                           + "\"paramTwo\" : 2 }",
                                                                                   RequiredCreatorParamsContainer.class);

        if (!"some value".equals(unmarshalledObject.getParamOne()) || unmarshalledObject.getParamTwo() != 2) {
            fail("Failed to instantiate type using JsonbCreator annotated factory method");
        }
    }

    @Test
    public void testRequiredCreatorParameterWithOptionalType() {
        ParameterTypeOptionalContainer unmarshalledObject = jsonb.fromJson("{ \"paramTwo\" : 1 }",
                                                                           ParameterTypeOptionalContainer.class);

        if (!"no value".equals(unmarshalledObject.getParamOne()) || unmarshalledObject.getParamTwo() != 1) {
            fail("Failed to instantiate type using JsonbCreator annotated factory method with parameter of type Optional");
        }

        unmarshalledObject = jsonb.fromJson("{ \"paramOne\" : \"some value\", \"paramTwo\" : 2 }",
                                            ParameterTypeOptionalContainer.class);

        if (!"some value".equals(unmarshalledObject.getParamOne()) || unmarshalledObject.getParamTwo() != 2) {
            fail("Failed to instantiate type using JsonbCreator annotated factory method with parameter of type Optional");
        }
    }

    @Test
    public void testRequiredParameterParameter() {
        try {
            jsonb.fromJson("{ \"paramTwo\" : 1 }", RequiredStringParamContainer.class);
            fail("Instantiation of the type should have failed, because required creator parameter is missing");
        } catch (JsonbException ignored) {
            //Everything worked as expected
        }

        RequiredStringParamContainer unmarshalledObject = jsonb.fromJson("{ \"paramOne\" : \"some value\" }",
                                                                                 RequiredStringParamContainer.class);

        if (!"some value".equals(unmarshalledObject.getParamOne()) || unmarshalledObject.getParamTwo() != null) {
            fail("Failed to instantiate type using JsonbCreator annotated factory method with one required parameter");
        }
    }

    @Test
    public void testPrimitiveTypesDefaultValues() {
        PrimitiveTypeContainer unmarshalledObject = jsonb.fromJson("{ }", PrimitiveTypeContainer.class);

        if (unmarshalledObject.getByteType() != 0
                || unmarshalledObject.getShortType() != 0
                || unmarshalledObject.getIntType() != 0
                || unmarshalledObject.getLongType() != 0L
                || unmarshalledObject.getFloatType() != 0.0F
                || unmarshalledObject.getDoubleType() != 0.0
                || unmarshalledObject.getBooleanType()
                || unmarshalledObject.getCharType() != '\u0000') {
            fail("Failed to instantiate type using JsonbCreator annotated factory method with parameter of type Optional");
        }
    }

}
