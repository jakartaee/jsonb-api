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

package jakarta.json.bind.tck.cdi.customizedmapping.adapters;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.tck.cdi.customizedmapping.adapters.model.AnimalShelterInjectedAdapter;
import jakarta.json.bind.tck.customizedmapping.adapters.model.Animal;
import jakarta.json.bind.tck.customizedmapping.adapters.model.Cat;
import jakarta.json.bind.tck.customizedmapping.adapters.model.Dog;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

/**
 * @test
 * @sources AdaptersCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.adapters.AdaptersCustomizationTest
 **/
/*
 * @class.setup_props: webServerHost; webServerPort; ts_home;
 */
public class AdaptersCustomizationCDITest {

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
   * @assertion_ids: JSONB:SPEC:JSB-4.7.1-3
   *
   * @test_Strategy: Assert that CDI injection is supported in adapters
   */
  @Test
  public void testCDISupport() {
    Jsonb jsonb = JsonbBuilder.create();
    String validationPattern = "\\{\\s*\"animals\"\\s*:\\s*\\[\\s*"
            + "\\{\\s*\"age\"\\s*:\\s*5\\s*,\\s*\"cuddly\"\\s*:\\s*true\\s*,\\s*\"furry\"\\s*:\\s*true\\s*,\\s*\"name\"\\s*:\\s*\"Garfield\"\\s*,\\s*\"type\"\\s*:\\s*\"CAT\"\\s*,\\s*\"weight\"\\s*:\\s*10.5\\s*}\\s*,\\s*"
            + "\\{\\s*\"age\"\\s*:\\s*3\\s*,\\s*\"barking\"\\s*:\\s*true\\s*,\\s*\"furry\"\\s*:\\s*false\\s*,\\s*\"name\"\\s*:\\s*\"Milo\"\\s*,\\s*\"type\"\\s*:\\s*\"DOG\"\\s*,\\s*\"weight\"\\s*:\\s*5.5\\s*}\\s*,\\s*"
            + "\\{\\s*\"age\"\\s*:\\s*6\\s*,\\s*\"furry\"\\s*:\\s*false\\s*,\\s*\"name\"\\s*:\\s*\"Tweety\"\\s*,\\s*\"type\"\\s*:\\s*\"GENERIC\"\\s*,\\s*\"weight\"\\s*:\\s*0.5\\s*}\\s*"
            + "]\\s*}";
    AnimalShelterInjectedAdapter animalShelter = new AnimalShelterInjectedAdapter();
    animalShelter.addAnimal(new Cat(5, "Garfield", 10.5f, true, true));
    animalShelter.addAnimal(new Dog(3, "Milo", 5.5f, false, true));
    animalShelter.addAnimal(new Animal(6, "Tweety", 0.5f, false));

    String jsonString = jsonb.toJson(animalShelter);
    assertThat("Failed to correctly marshall complex type hierarchy using an adapter with a CDI managed field "
                       + "configured using JsonbTypeAdapter annotation to a simpler class.",
               jsonString, matchesPattern(validationPattern));

    String toSerialize = "{ \"animals\" : [ "
            + "{ \"age\" : 5, \"cuddly\" : true, \"furry\" : true, \"name\" : \"Garfield\" , \"type\" : \"CAT\", \"weight\" : 10.5}, "
            + "{ \"age\" : 3, \"barking\" : true, \"furry\" : false, \"name\" : \"Milo\", \"type\" : \"DOG\", \"weight\" : 5.5}, "
            + "{ \"age\" : 6, \"furry\" : false, \"name\" : \"Tweety\", \"type\" : \"GENERIC\", \"weight\" : 0.5}"
            + " ] }";
    AnimalShelterInjectedAdapter unmarshalledObject = jsonb.fromJson(toSerialize, AnimalShelterInjectedAdapter.class);
    assertThat("Failed to correctly unmarshall complex type hierarchy using an adapter with a CDI managed "
                       + "field configured using JsonbTypeAdapter annotation to a simpler class.",
               unmarshalledObject, is(animalShelter));
  }
}
