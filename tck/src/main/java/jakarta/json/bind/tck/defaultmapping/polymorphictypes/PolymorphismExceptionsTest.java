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

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.json.bind.annotation.JsonbPolymorphicType;
import jakarta.json.bind.annotation.JsonbSubtype;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

public class PolymorphismExceptionsTest {

    private final Jsonb jsonb = JsonbBuilder.create();

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, MethodHandles.lookup().lookupClass().getPackage().getName());
    }

    @Test
    public void testSerializeMultiplePolymorphicTypesFromParallelSources() {
        try {
            jsonb.toJson(new Dog());
            fail("Serialization of multiple @JsonbPolymorphicType from more than one source is not supported.");
        } catch (JsonbException ignored) {
            //expected
        }
    }

    @Test
    public void testDeserializeMultiplePolymorphicTypesFromParallelSources() {
        try {
            jsonb.fromJson("{\"@animal\":\"dog\",\"@livingThing\":\"dog\"}", Dog.class);
            fail("Deserialization of multiple @JsonbPolymorphicType from more than one source is not supported.");
        } catch (JsonbException ignored) {
            //expected
        }
    }

    @Test
    public void testInvalidAlias() {
        try {
            jsonb.toJson(new InvalidAlias());
            fail("Serialization should have failed since set alias is not the same class as it is defined on or its subtype.");
        } catch (JsonbException ignored) {
            //Everything as expected
        }
    }

    @Test
    public void testSerializationDifferentFormatTypes() {
        try {
            jsonb.toJson(new Child());
            fail("Serialization of the polymorphic chain with not consistent polymorphic information format, is not supported.");
        } catch (JsonbException ignored) {
            //expected
        }
    }

    @Test
    public void testDeserializationDifferentFormatTypes() {
        try {
            jsonb.fromJson("{\"@type\":\"object\"}", PropertyPolyType.class);
            fail("Deserialization of the polymorphic chain with not consistent polymorphic information format, is not supported.");
        } catch (JsonbException ignored) {
            //expected
        }
    }

    @Test
    public void testNameCollision() {
        try {
            jsonb.toJson(new PropertyNameCollision());
            fail("Serialization of the polymorphic information to the property with the name which collides "
                         + "with the class property, is not supported");
        } catch (JsonbException ignored) {
            //expected
        }
    }

    @Test
    public void testDeserializationClassNamesWithoutAllowedPackages() {
        String json = "{\"@type\":\"jakarta.json.bind.tck.defaultmapping.polymorphictypes."
                + "PolymorphicExceptionTests$ChildClassNamesWithoutAllowed\",\"parent\":1,\"child\":2}";
        assertThrows("No allowed packages has been set. It is required to have allowed packages set "
                             + "if class name handling is turned on",
                     JsonbException.class, () -> jsonb.fromJson(json, ParentClassNamesWithoutAllowed.class));
    }

    //--------------

    @JsonbPolymorphicType(key = "@animal", value = {
            @JsonbSubtype(alias = "dog", type = Dog.class)
    })
    public interface Animal {}

    @JsonbPolymorphicType(key = "@livingThing", value = {
            @JsonbSubtype(alias = "dog", type = Dog.class)
    })
    public interface LivingEntity {}

    public static final class Dog implements Animal, LivingEntity {}

    //--------------

    @JsonbPolymorphicType(value = {
            @JsonbSubtype(alias = "object", type = ObjectPolyType.class)
    })
    public static class PropertyPolyType {}

    @JsonbPolymorphicType(format = JsonbPolymorphicType.Format.WRAPPING_OBJECT, value={
            @JsonbSubtype(alias = "child", type = Child.class)
    })
    public static class ObjectPolyType extends PropertyPolyType {}

    public static final class Child extends ObjectPolyType {}

    //--------------

    @JsonbPolymorphicType(key = "keyName", value = {
            @JsonbSubtype(alias = "test", type = PropertyNameCollision.class)
    })
    public static class PropertyNameCollision {
        public String keyName;
    }

    //--------------

    @JsonbPolymorphicType({
            @JsonbSubtype(alias = "integer", type = Integer.class),
            @JsonbSubtype(alias = "invalid", type = InvalidAlias.class)
    })
    public static class InvalidAlias {}

    //--------------

    @JsonbPolymorphicType(classNames = true)
    public static class ParentClassNamesWithoutAllowed {
        public int parent = 1;
    }

    public static class ChildClassNamesWithoutAllowed extends ParentClassNamesWithoutAllowed {
        public int child = 2;
    }
}
