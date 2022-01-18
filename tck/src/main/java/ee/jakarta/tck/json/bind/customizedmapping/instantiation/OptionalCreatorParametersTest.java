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

package ee.jakarta.tck.json.bind.customizedmapping.instantiation;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.JsonbException;

import ee.jakarta.tck.json.bind.customizedmapping.instantiation.model.OptionalTypeContainer;
import ee.jakarta.tck.json.bind.customizedmapping.instantiation.model.SimpleCreatorParamContainer;
import ee.jakarta.tck.json.bind.customizedmapping.instantiation.model.PrimitiveTypeContainer;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OptionalCreatorParametersTest {

    private final Jsonb jsonb = JsonbBuilder.create();
    private final Jsonb jsonbRequired = JsonbBuilder.create(new JsonbConfig().withCreatorParametersRequired(true));

    @Test
    public void testCreatorMethodWithOptionalParameter() {
        SimpleCreatorParamContainer unmarshalledObject = jsonb.fromJson("{ \"paramTwo\" : 1 }",
                                                                        SimpleCreatorParamContainer.class);
        assertThat("Failed to instantiate type using JsonbCreator annotated factory method with optional parameter",
                   unmarshalledObject.getParamOne(), nullValue());
        assertThat("Failed to instantiate type using JsonbCreator annotated factory method with optional parameter",
                   unmarshalledObject.getParamTwo(), is(1));

        unmarshalledObject = jsonb.fromJson("{ \"paramOne\" : \"some value\", \"paramTwo\" : 2 }",
                                            SimpleCreatorParamContainer.class);
        assertThat("Failed to instantiate type using JsonbCreator annotated factory method with optional parameter",
                   unmarshalledObject.getParamOne(), is("some value"));
        assertThat("Failed to instantiate type using JsonbCreator annotated factory method with optional parameter",
                   unmarshalledObject.getParamTwo(), is(2));

        unmarshalledObject = jsonb.fromJson("{ }", SimpleCreatorParamContainer.class);
        assertThat("Failed to instantiate type using JsonbCreator annotated factory method with optional parameter",
                   unmarshalledObject.getParamOne(), nullValue());
        assertThat("Failed to instantiate type using JsonbCreator annotated factory method with optional parameter",
                   unmarshalledObject.getParamTwo(), nullValue());
    }

    @Test
    public void testAllParametersRequiredSetByMethodAnnotation() {
        assertThrows(JsonbException.class, () -> jsonbRequired.fromJson("{ }", SimpleCreatorParamContainer.class),
                     "Instantiation of the type should have failed, because required creator parameters were missing");
        assertThrows(JsonbException.class,
                     () -> jsonbRequired.fromJson("{ \"paramOne\" : \"some value\" }", SimpleCreatorParamContainer.class),
                     "Instantiation of the type should have failed, because required creator parameter was missing");
    }

    @Test
    public void testPrimitiveTypesDefaultValues() {
        PrimitiveTypeContainer unmarshalledObject = jsonb.fromJson("{ }", PrimitiveTypeContainer.class);
        assertThat("Failed to set proper default byte value to the optional creator parameter",
                   unmarshalledObject.getByteType(), is(0));
        assertThat("Failed to set proper default short value to the optional creator parameter",
                   unmarshalledObject.getShortType(), is(0));
        assertThat("Failed to set proper default int value to the optional creator parameter.",
                   unmarshalledObject.getIntType(), is(0));
        assertThat("Failed to set proper default long value to the optional creator parameter.",
                   unmarshalledObject.getLongType(), is(0L));
        assertThat("Failed to set proper default float value to the optional creator parameter.",
                   unmarshalledObject.getFloatType(), is(0.0F));
        assertThat("Failed to set proper default double value to the optional creator parameter.",
                   unmarshalledObject.getDoubleType(), is(0.0));
        assertThat("Failed to set proper default boolean value to the optional creator parameter.",
                   unmarshalledObject.getBooleanType(), is(false));
        assertThat("Failed to set proper default char value to the optional creator parameter.",
                   unmarshalledObject.getCharType(), is('\u0000'));
    }

    @Test
    public void testOptionalTypesDefaultValues() {
        OptionalTypeContainer unmarshalledObject = jsonb.fromJson("{ }", OptionalTypeContainer.class);
        assertThat("Failed to set empty OptionalInt instance the the optional creator parameter",
                   unmarshalledObject.getIntOptional(), is(-1));
        assertThat("Failed to set empty OptionalLong instance the the optional creator parameter",
                   unmarshalledObject.getLongOptional(), is(-1));
        assertThat("Failed to set empty OptionalDouble instance the the optional creator parameter",
                   unmarshalledObject.getDoubleOptional(), is(-1));
        assertThat("Failed to set empty Optional instance the the optional creator parameter",
                   unmarshalledObject.getStringOptional(), nullValue());
    }

    @Test
    public void testOptionalTypesInCreator() {
        String json = "{ \"stringOptional\":\"stringValue\"," + "\"intOptional\":1," + "\"longOptional\":2," +
                "\"doubleOptional\":3.0 }";
        OptionalTypeContainer unmarshalledObject = jsonb.fromJson(json, OptionalTypeContainer.class);
        assertThat("Failed to set OptionalInt instance with provided value from the JSON document",
                   unmarshalledObject.getIntOptional(), is(1));
        assertThat("Failed to set OptionalLong instance with provided value from the JSON document",
                   unmarshalledObject.getLongOptional(), is(2L));
        assertThat("Failed to set OptionalDouble instance with provided value from the JSON document",
                   unmarshalledObject.getDoubleOptional(), is(3.0));
        assertThat("Failed to set Optional instance with provided value from the JSON document",
                   unmarshalledObject.getStringOptional(), is("stringValue"));
    }

}
