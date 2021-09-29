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

package jakarta.json.bind.tck.defaultmapping.polymorphictypes;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbPolymorphicType;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbSubtype;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.fail;

/**
 * Tests for verification of proper polymorphism handling based on annotation with array wrapping format.
 */
@RunWith(Arquillian.class)
public class AnnotationPolymorphismArrayTest {

    private final Jsonb jsonb = JsonbBuilder.create();

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, MethodHandles.lookup().lookupClass().getPackage().getName());
    }

    @Test
    public void testBasicSerialization() {
        Dog dog = new Dog();
        String jsonString = jsonb.toJson(dog);
        if (!jsonString.matches("\\[\\s*\"dog\"\\s*,\\s*\\{\\s*\"isDog\"\\s*:\\s*true\\s*\\}\\s*\\]")) {
            fail("Failed to serialize Dog class correctly in the polymorphic format: "
                         + JsonbPolymorphicType.Format.WRAPPING_ARRAY.name());
        }
        Cat cat = new Cat();
        jsonString = jsonb.toJson(cat);
        if (!jsonString.matches("\\[\\s*\"cat\"\\s*,\\s*\\{\\s*\"isCat\"\\s*:\\s*true\\s*\\}\\s*\\]")) {
            fail("Failed to serialize Cat class correctly in the polymorphic format: "
                         + JsonbPolymorphicType.Format.WRAPPING_OBJECT.name());
        }
    }

    @Test
    public void testBasicDeserialization() {
        Animal dog = jsonb.fromJson("[\"dog\",{\"isDog\":false}]", Animal.class);
        if (!(dog instanceof Dog)) {
            fail("Incorrectly deserialized to the polymorphic type. Expected was Dog instance. "
                         + "Got instance of the class " + dog.getClass());
            return;
        }
        if (((Dog) dog).isDog) {
            fail("Incorrectly deserialized field of the Dog instance. Field \"isDog\" should have been false.");
            return;
        }
        Animal cat = jsonb.fromJson("[\"cat\",{\"isCat\":false}]", Animal.class);
        if (!(cat instanceof Cat)) {
            fail("Incorrectly deserialized to the polymorphic type. Expected was Cat instance. "
                         + "Got instance of the class " + cat.getClass());
            return;
        }
        if (((Cat) cat).isCat) {
            fail("Incorrectly deserialized field of the Cat instance. Field \"isCat\" should have been false.");
        }
    }

    @Test
    public void testExactTypeDeserialization() {
        Dog dog = jsonb.fromJson("{\"isDog\":false}", Dog.class);
        if (dog.isDog) {
            fail("Incorrectly deserialized field of the Dog instance. Field \"isDog\" should have been false.");
            return;
        }
        dog = jsonb.fromJson("[\"dog\",{\"isDog\":false}]", Dog.class); //TODO??????
        if (dog.isDog) {
            fail("Incorrectly deserialized field of the Dog instance. Field \"isDog\" should have been false.");
        }
    }

    @Test
    public void testUnknownAliasDeserialization() {
        try {
            jsonb.fromJson("[\"rat\",{\"isRat\":false}]", Animal.class);
            fail("Deserialization should fail. Alias \"rat\" is not valid alias.");
        } catch (JsonbException ignored) {
            //Expected
        }
    }

    @Test
    public void testUnknownAliasSerialization() {
        try {
            jsonb.toJson(new Rat());
            fail("Serialization should fail. There is no known alias for a class Rat.");
        } catch (JsonbException ignored) {
            //Expected
        }
    }

    @Test
    public void testCreatorDeserialization() {
        SomeDateType creator = jsonb
                .fromJson("{\"constructor\":{\"localDate\":\"26-02-2021\"}}", SomeDateType.class);

        if (!(creator instanceof DateConstructor)) {
            fail("Incorrectly deserialized to the polymorphic type. Expected was DateConstructor instance. "
                         + "Got instance of the class " + creator.getClass());
        }
    }

    @Test
    public void testArraySerialization() {
        String expected = "\\["
                + "\\s*\\[\\s*\"dog\"\\s*,\\s*\\{\\s*\"isDog\"\\s*:\\s*true\\s*\\}\\s*\\]\\s*,"
                + "\\s*\\[\\s*\"cat\"\\s*,\\s*\\{\\s*\"isCat\"\\s*:\\s*true\\s*\\}\\s*\\]\\s*,"
                + "\\s*\\[\\s*\"dog\"\\s*,\\s*\\{\\s*\"isDog\"\\s*:\\s*true\\s*\\}\\s*\\]\\s*"
                + "\\]";
        Animal[] animals = new Animal[] {new Dog(), new Cat(), new Dog()};
        String jsonString = jsonb.toJson(animals);
        if (!jsonString.matches(expected)) {
            fail("Array values were not properly serialized with polymorphic information.");
        }
    }

    @Test
    public void testArrayDeserialization() {
        String array = "[[\"dog\",{\"isDog\":true}],[\"cat\",{\"isCat\":true}],[\"dog\",{\"isDog\":true}]]";
        Animal[] deserialized = jsonb.fromJson(array, Animal[].class);
        if (deserialized.length != 3) {
            fail("Array should have exactly 3 values.");
        } else if (!(deserialized[0] instanceof Dog)) {
            fail("Array value at index 0 was incorrectly deserialized to the polymorphic type. Expected was Dog instance. "
                         + "Got instance of the class " + deserialized[0].getClass());
        } else if (!(deserialized[1] instanceof Cat)) {
            fail("Array value at index 1 was incorrectly deserialized to the polymorphic type. Expected was Cat instance. "
                         + "Got instance of the class " + deserialized[1].getClass());
        } else if (!(deserialized[2] instanceof Dog)) {
            fail("Array value at index 2 was incorrectly deserialized to the polymorphic type. Expected was Dog instance. "
                         + "Got instance of the class " + deserialized[2].getClass());
        }
    }

    @JsonbPolymorphicType(format = JsonbPolymorphicType.Format.WRAPPING_ARRAY)
    @JsonbSubtype(alias = "dog", type = Dog.class)
    @JsonbSubtype(alias = "cat", type = Cat.class)
    public interface Animal {

    }

    public static class Dog implements Animal {

        public boolean isDog = true;

    }

    public static class Cat implements Animal {

        public boolean isCat = true;

    }

    public static class Rat implements Animal {

        public boolean isRat = true;

    }

    @JsonbPolymorphicType(format = JsonbPolymorphicType.Format.WRAPPING_ARRAY)
    @JsonbSubtype(alias = "constructor", type = DateConstructor.class)
    public interface SomeDateType {

    }

    public static final class DateConstructor implements SomeDateType {

        public LocalDate localDate;

        @JsonbCreator
        public DateConstructor(@JsonbProperty("localDate") @JsonbDateFormat(value = "dd-MM-yyyy", locale = "nl-NL") LocalDate localDate) {
            this.localDate = localDate;
        }

    }

}
