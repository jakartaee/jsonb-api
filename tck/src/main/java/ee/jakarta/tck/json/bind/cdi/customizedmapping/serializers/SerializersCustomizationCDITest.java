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

package ee.jakarta.tck.json.bind.cdi.customizedmapping.serializers;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import ee.jakarta.tck.json.bind.cdi.customizedmapping.serializers.model.AnimalShelterWithInjectedSerializer;
import ee.jakarta.tck.json.bind.customizedmapping.serializers.model.Animal;
import ee.jakarta.tck.json.bind.customizedmapping.serializers.model.Cat;
import ee.jakarta.tck.json.bind.customizedmapping.serializers.model.Dog;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

/**
 * @test
 * @sources SerializersCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.serializers.SerializersCustomizationTest
 **/
public class SerializersCustomizationCDITest {

    private static SeContainer container;

    @BeforeAll
    private static void startContainer() {
        try {
            //Verify that CDI container is already running
            CDI.current();
        } catch (IllegalStateException exception) {
            container = SeContainerInitializer.newInstance().initialize();
        }
    }

    @AfterAll
    private static void stopContainer() {
        if (container != null) {
            container.close();
        }
    }

    /*
     * @testName: testCDISupport
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.7.2-3
     *
     * @test_Strategy: Assert that CDI injection is supported in serializers and
     * deserializers
     */
    @Test
    public void testCDISupport() {
        Jsonb jsonb = JsonbBuilder.create();
        String validationPattern = "\\{\\s*\"animals\"\\s*:\\s*\\[\\s*"
                + "\\{\\s*\"type\"\\s*:\\s*\"cat\"\\s*,\\s*\"cuddly\"\\s*:\\s*true\\s*,\\s*\"age\"\\s*:\\s*5\\s*,"
                + "\\s*\"furry\"\\s*:\\s*true\\s*,\\s*\"name\"\\s*:\\s*\"Garfield\"\\s*,\\s*\"weight\"\\s*:\\s*10.5\\s*}\\s*,\\s*"
                + "\\{\\s*\"type\"\\s*:\\s*\"dog\"\\s*,\\s*\"barking\"\\s*:\\s*true\\s*,\\s*\"age\"\\s*:\\s*3\\s*,"
                + "\\s*\"furry\"\\s*:\\s*false\\s*,\\s*\"name\"\\s*:\\s*\"Milo\"\\s*,\\s*\"weight\"\\s*:\\s*5.5\\s*}\\s*,\\s*"
                + "\\{\\s*\"type\"\\s*:\\s*\"animal\"\\s*,\\s*\"age\"\\s*:\\s*6\\s*,\\s*\"furry\"\\s*:\\s*false\\s*,"
                + "\\s*\"name\"\\s*:\\s*\"Tweety\"\\s*,\\s*\"weight\"\\s*:\\s*0.5\\s*}\\s*"
                + "]\\s*}";
        AnimalShelterWithInjectedSerializer animalShelter = new AnimalShelterWithInjectedSerializer();
        animalShelter.addAnimal(new Cat(5, "Garfield", 10.5f, true, true));
        animalShelter.addAnimal(new Dog(3, "Milo", 5.5f, false, true));
        animalShelter.addAnimal(new Animal(6, "Tweety", 0.5f, false));

        String validationMessage = "Failed to correctly marshall complex type hierarchy using a serializer configured using "
                + "JsonbTypeSerializer annotation and a deserializer with a CDI managed field configured using "
                + "JsonbTypeDeserializer annotation.";

        String jsonString = jsonb.toJson(animalShelter);
        assertThat(validationMessage, jsonString, matchesPattern(validationPattern));

        String toSerialize = "{ \"animals\" : [ "
                + "{ \"type\" : \"cat\", \"cuddly\" : true, \"age\" : 5, \"furry\" : true, \"name\" : \"Garfield\" , \"weight\""
                + " : 10.5}, "
                + "{ \"type\" : \"dog\", \"barking\" : true, \"age\" : 3, \"furry\" : false, \"name\" : \"Milo\", \"weight\" : "
                + "5.5}, "
                + "{ \"type\" : \"animal\", \"age\" : 6, \"furry\" : false, \"name\" : \"Tweety\", \"weight\" : 0.5}"
                + " ] }";
        validationMessage = "Failed to correctly unmarshall complex type hierarchy using a serializer configured using "
                + "JsonbTypeSerializer annotation and a deserializer with a CDI managed field configured using "
                + "JsonbTypeDeserializer annotation.";
        AnimalShelterWithInjectedSerializer unmarshalledObject = jsonb
                .fromJson(toSerialize, AnimalShelterWithInjectedSerializer.class);
        assertThat(validationMessage, unmarshalledObject, is((animalShelter)));
    }
}
