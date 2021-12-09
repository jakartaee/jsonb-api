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

import static jakarta.json.bind.tck.RegexMatcher.matches;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

/**
 * Tests for verification of proper polymorphism handling based on annotation with property format.
 */
@RunWith(Arquillian.class)
public class AnnotationPolymorphismTest {

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
        if (!jsonString.matches("\\{\\s*\"@type\"\\s*:\\s*\"dog\"\\s*,\\s*\"isDog\"\\s*:\\s*true\\s*\\}")) {
            fail("Failed to serialize Dog class correctly in the polymorphic format: "
                         + JsonbPolymorphicType.Format.PROPERTY.name());
        }
        Cat cat = new Cat();
        jsonString = jsonb.toJson(cat);
        if (!jsonString.matches("\\{\\s*\"@type\"\\s*:\\s*\"cat\"\\s*,\\s*\"isCat\"\\s*:\\s*true\\s*\\}")) {
            fail("Failed to serialize Cat class correctly in the polymorphic format: "
                         + JsonbPolymorphicType.Format.PROPERTY.name());
        }
    }

    @Test
    public void testBasicDeserialization() {
        Animal dog = jsonb.fromJson("{\"@type\":\"dog\",\"isDog\":false}", Animal.class);
        if (!(dog instanceof Dog)) {
            fail("Incorrectly deserialized to the polymorphic type. Expected was Dog instance. "
                         + "Got instance of class " + dog.getClass());
            return;
        }
        if (((Dog) dog).isDog) {
            fail("Incorrectly deserialized field of the Dog instance. Field \"isDog\" should have been false.");
            return;
        }
        Animal cat = jsonb.fromJson("{\"@type\":\"cat\",\"isCat\":false}", Animal.class);
        if (!(cat instanceof Cat)) {
            fail("Incorrectly deserialized to the polymorphic type. Expected was Cat instance. "
                         + "Got instance of class " + cat.getClass());
            return;
        }
        if (((Cat) cat).isCat) {
            fail("Incorrectly deserialized field of the Cat instance. Field \"isCat\" should have been false.");
        }
    }

    @Test
    public void testUnknownAliasDeserialization() {
        try {
            jsonb.fromJson("{\"@type\":\"rat\",\"isRat\":false}", Animal.class);
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
                .fromJson("{\"@dateType\":\"constructor\",\"localDate\":\"26-02-2021\"}", SomeDateType.class);

        if (!(creator instanceof DateConstructor)) {
            fail("Incorrectly deserialized to the polymorphic type. Expected was DateConstructor instance. "
                         + "Got instance of class " + creator.getClass());
        }
    }

    @Test
    public void testArraySerialization() {
        String expected = "\\["
                + "\\s*\\{\\s*\"@type\"\\s*:\\s*\"dog\"\\s*,\\s*\"isDog\"\\s*:\\s*true\\s*\\}\\s*,"
                + "\\s*\\{\\s*\"@type\"\\s*:\\s*\"cat\"\\s*,\\s*\"isCat\"\\s*:\\s*true\\s*\\}\\s*,"
                + "\\s*\\{\\s*\"@type\"\\s*:\\s*\"dog\"\\s*,\\s*\"isDog\"\\s*:\\s*true\\s*\\}\\s*"
                + "\\]";
        Animal[] animals = new Animal[] {new Dog(), new Cat(), new Dog()};
        String jsonString = jsonb.toJson(animals);
        if (!jsonString.matches(expected)) {
            fail("Array values were not properly serialized with polymorphic information.");
        }
    }

    @Test
    public void testArrayDeserialization() {
        String array = "[{\"@type\":\"dog\",\"isDog\":true},{\"@type\":\"cat\",\"isCat\":true},"
                + "{\"@type\":\"dog\",\"isDog\":true}]";
        Animal[] deserialized = jsonb.fromJson(array, Animal[].class);
        if (deserialized.length != 3) {
            fail("Array should have exactly 3 values.");
        } else if (!(deserialized[0] instanceof Dog)) {
            fail("Array value at index 0 was incorrectly deserialized to the polymorphic type. Expected was Dog instance. "
                         + "Got instance of class " + deserialized[0].getClass());
        } else if (!(deserialized[1] instanceof Cat)) {
            fail("Array value at index 1 was incorrectly deserialized to the polymorphic type. Expected was Cat instance. "
                         + "Got instance of class " + deserialized[1].getClass());
        } else if (!(deserialized[2] instanceof Dog)) {
            fail("Array value at index 2 was incorrectly deserialized to the polymorphic type. Expected was Dog instance. "
                         + "Got instance of class " + deserialized[2].getClass());
        }
    }

    @Test
    public void testSerializationClassNamesWithCorrectAllowedPackages() {
        String expected = "\\{\\s*\"@type\"\\s*:\\s*\"jakarta.json.bind.tck.defaultmapping.polymorphictypes"
                + ".AnnotationPolymorphismTest\\$ChildClassNamesWithCorrectAllowed\"\\s*,"
                + "\\s*\"parent\"\\s*:\\s*1\\s*,"
                + "\\s*\"child\"\\s*:\\s*2\\s*\\}";
        assertThat(jsonb.toJson(new ChildClassNamesWithCorrectAllowed()), matches(expected));
    }

    @Test
    public void testDeserializationClassNamesWithCorrectAllowedPackages() {
        String json = "{\"@type\":\"jakarta.json.bind.tck.defaultmapping.polymorphictypes"
                + ".AnnotationPolymorphismTest$ChildClassNamesWithCorrectAllowed\",\"parent\":3,\"child\":4}";
        ParentClassNamesWithCorrectAllowed deserialized = jsonb.fromJson(json, ParentClassNamesWithCorrectAllowed.class);
        assertThat(deserialized, instanceOf(ChildClassNamesWithCorrectAllowed.class));
        assertThat(deserialized.parent, is(3));
        assertThat(((ChildClassNamesWithCorrectAllowed)deserialized).child, is(4));
    }

    @Test
    public void testDeserializationClassNamesWithIncorrectAllowedPackages() {
        String json = "{\"@type\":\"jakarta.json.bind.tck.defaultmapping.polymorphictypes."
                + "AnnotationPolymorphismTest$ChildClassNamesWithIncorrectAllowed\",\"parent\":1,\"child\":2}";
        assertThrows(JsonbException.class, () -> jsonb.fromJson(json, ParentClassNamesWithIncorrectAllowed.class));
    }

    @JsonbPolymorphicType({
            @JsonbSubtype(alias = "dog", type = Dog.class),
            @JsonbSubtype(alias = "cat", type = Cat.class),
            @JsonbSubtype(alias = "elephant", type = Elephant.class)
    })
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

    public static class Elephant implements Animal {

        public boolean isElephant = true;
        public String testProperty = "value";

    }

    @JsonbPolymorphicType(key = "@dateType", value = {
            @JsonbSubtype(alias = "constructor", type = DateConstructor.class)
    })
    public interface SomeDateType {

    }

    public static final class DateConstructor implements SomeDateType {

        public LocalDate localDate;

        @JsonbCreator
        public DateConstructor(@JsonbProperty("localDate") @JsonbDateFormat(value = "dd-MM-yyyy", locale = "nl-NL") LocalDate localDate) {
            this.localDate = localDate;
        }

    }

    @JsonbPolymorphicType(classNames = true, allowedPackages = {"jakarta.json.bind.tck.defaultmapping.polymorphictypes"})
    public static class ParentClassNamesWithCorrectAllowed {
        public int parent = 1;
    }

    public static class ChildClassNamesWithCorrectAllowed extends ParentClassNamesWithCorrectAllowed {
        public int child = 2;
    }

    @JsonbPolymorphicType(classNames = true, allowedPackages = {"jakarta.jsonb.incorrect"})
    public static class ParentClassNamesWithIncorrectAllowed {
        public int parent = 1;
    }

    public static class ChildClassNamesWithIncorrectAllowed extends ParentClassNamesWithIncorrectAllowed {
        public int child = 2;
    }

}
