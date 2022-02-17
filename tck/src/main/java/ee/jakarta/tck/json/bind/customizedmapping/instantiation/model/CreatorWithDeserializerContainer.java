/*
 * Copyright (c) 2021, 2022 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.json.bind.customizedmapping.instantiation.model;

import java.lang.reflect.Type;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTypeDeserializer;
import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.bind.serializer.JsonbDeserializer;
import jakarta.json.stream.JsonParser;

public class CreatorWithDeserializerContainer {
    private final String stringInstance;

    @JsonbCreator
    public CreatorWithDeserializerContainer(
            @JsonbProperty("instance") @JsonbTypeDeserializer(SimpleStringDeserializer.class) String stringInstance) {
        this.stringInstance = stringInstance;
    }

    public String getStringInstance() {
        return stringInstance;
    }

    public static class SimpleStringDeserializer implements JsonbDeserializer<String> {

        public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext, Type type) {
            if (jsonParser.hasNext()) {
                JsonParser.Event event = jsonParser.next();
                if (event != JsonParser.Event.VALUE_STRING) {
                    throw new IllegalStateException("Unexpected event: " + event);
                }
            }
            return jsonParser.getString() + " Deserialized";
        }

    }

}
