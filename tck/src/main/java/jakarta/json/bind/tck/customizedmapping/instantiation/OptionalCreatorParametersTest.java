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
import jakarta.json.bind.tck.customizedmapping.instantiation.model.OptionalTypeContainer;
import jakarta.json.bind.tck.customizedmapping.instantiation.model.PrimitiveTypeContainer;
import jakarta.json.bind.tck.customizedmapping.instantiation.model.SimpleCreatorParamContainer;

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
    private final Jsonb jsonbRequired = JsonbBuilder.create(new JsonbConfig().withCreatorParametersRequired(true));

    @Test
    public void testCreatorMethodWithOptionalParameter() {
        SimpleCreatorParamContainer unmarshalledObject = jsonb.fromJson("{ \"paramTwo\" : 1 }",
                                                                        SimpleCreatorParamContainer.class);

        if (unmarshalledObject.getParamOne() != null || unmarshalledObject.getParamTwo() != 1) {
            fail("Failed to instantiate type using JsonbCreator annotated factory method with optional parameter");
        }
        unmarshalledObject = jsonb.fromJson("{ \"paramOne\" : \"some value\", \"paramTwo\" : 2 }",
                                            SimpleCreatorParamContainer.class);

        if (!"some value".equals(unmarshalledObject.getParamOne()) || unmarshalledObject.getParamTwo() != 2) {
            fail("Failed to instantiate type using JsonbCreator annotated factory method with optional parameter");
        }
        unmarshalledObject = jsonb.fromJson("{ }", SimpleCreatorParamContainer.class);

        if (unmarshalledObject.getParamOne() != null || unmarshalledObject.getParamTwo() != null) {
            fail("Failed to instantiate type using JsonbCreator annotated factory method with all optional parameters "
                         + "set by creator property");
        }
    }

    @Test
    public void testAllParametersRequiredSetByMethodAnnotation() {
        try {
            jsonbRequired.fromJson("{ }", SimpleCreatorParamContainer.class);
            fail("Instantiation of the type should have failed, because required creator parameters were missing");
        } catch (JsonbException ignored) {
            //Everything worked as expected
        }
        try {
            jsonbRequired.fromJson("{ \"paramOne\" : \"some value\" }", SimpleCreatorParamContainer.class);
            fail("Instantiation of the type should have failed, because required creator parameter was missing");
        } catch (JsonbException ignored) {
            //Everything worked as expected
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
            fail("Failed to instantiate type using JsonbCreator annotated factory method with proper optional default values");
        }
    }

    @Test
    public void testOptionalTypesDefaultValues() {
        OptionalTypeContainer unmarshalledObject = jsonb.fromJson("{ }", OptionalTypeContainer.class);

        if (unmarshalledObject.getIntOptional() != -1
                || unmarshalledObject.getLongOptional() != -1L
                || unmarshalledObject.getDoubleOptional() != -1.0
                || unmarshalledObject.getStringOptional() != null) {
            fail("Failed to instantiate type using JsonbCreator annotated factory method with Optional type default values");
        }
        unmarshalledObject = jsonb.fromJson("{ \"stringOptional\":\"stringValue\","
                                                    + "\"intOptional\":1,"
                                                    + "\"longOptional\":2,"
                                                    + "\"doubleOptional\":3.0 }", OptionalTypeContainer.class);

        if (unmarshalledObject.getIntOptional() != 1
                || unmarshalledObject.getLongOptional() != 2L
                || unmarshalledObject.getDoubleOptional() != 3.0
                || "stringValue".equals(unmarshalledObject.getStringOptional())) {
            fail("Failed to instantiate type using JsonbCreator annotated factory method with Optional type parameters");
        }
    }

}
