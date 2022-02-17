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

package ee.jakarta.tck.json.bind.defaultmapping.polymorphictypes;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TypeInfoExceptionsTest {

    private final Jsonb jsonb = JsonbBuilder.create();

    @Test
    public void testSerializeTypeInfoMultiInheritance() {
        assertThrows(JsonbException.class, () -> jsonb.toJson(new Dog()),
                     "Serialization of @JsonbTypeInfo multi inheritance is not supported.");
    }

    @Test
    public void testDeserializeTypeInfoMultiInheritance() {
        assertThrows(JsonbException.class, () -> jsonb.fromJson("{\"@animal\":\"dog\",\"@livingThing\":\"dog\"}", Dog.class),
                     "Deserialization of @JsonbTypeInfo multi inheritance is not supported.");
    }

    @Test
    public void testInvalidAlias() {
        assertThrows(JsonbException.class, () -> jsonb.toJson(new InvalidAlias()),
                     "Serialization should have failed since set alias is not subtype of the class it is defined on.");
    }

    @Test
    public void testNameCollision() {
        assertThrows(JsonbException.class, () -> jsonb.toJson(new PropertyNameCollision()),
                     "Serialization of the type information to the property with the name which collides "
                             + "with the class property, is not supported");
    }

    //--------------

    @JsonbTypeInfo(key = "@animal", value = {
            @JsonbSubtype(alias = "dog", type = Dog.class)
    })
    public interface Animal { }

    @JsonbTypeInfo(key = "@livingThing", value = {
            @JsonbSubtype(alias = "dog", type = Dog.class)
    })
    public interface LivingEntity { }

    public static final class Dog implements Animal, LivingEntity { }

    //--------------

    @JsonbTypeInfo(key = "keyName", value = {
            @JsonbSubtype(alias = "test", type = PropertyNameCollision.class)
    })
    public static class PropertyNameCollision {
        public String keyName;
    }

    //--------------

    @JsonbTypeInfo({
            @JsonbSubtype(alias = "integer", type = Integer.class),
            @JsonbSubtype(alias = "invalid", type = InvalidAlias.class)
    })
    public static class InvalidAlias { }

}
