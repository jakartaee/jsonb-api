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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PolymorphismTest {

    private final Jsonb jsonb = JsonbBuilder.create();

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, MethodHandles.lookup().lookupClass().getPackage().getName());
    }

    @Test
    public void testSerializeMultiplePolymorphicTypesFromParallelSources() {
        Dog shouldFail = new Dog();
        try {
            jsonb.toJson(shouldFail);
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
    public void testSerializeMultiplePolyTypesInSingleChain() {
        String expected = "{"
                + "\"@machine\":\"vehicle\","
                + "\"@vehicle\":\"car\","
                + "\"machineProperty\":\"machineProperty\","
                + "\"vehicleProperty\":\"vehicleProperty\","
                + "\"carProperty\":\"carProperty\""
                + "}";
        Car car = new Car();
        String json = jsonb.toJson(car);
        assertEquals(expected, json);
    }

    @Test
    public void testDeserializeMultiplePolyTypesInSingleChain() {
        String json = "{"
                + "\"@machine\":\"vehicle\","
                + "\"@vehicle\":\"car\","
                + "\"machineProperty\":\"machineProperty\","
                + "\"vehicleProperty\":\"vehicleProperty\","
                + "\"carProperty\":\"carProperty\""
                + "}";
        Machine machine = jsonb.fromJson(json, Machine.class);
        assertTrue(machine instanceof Car);
        Vehicle vehicle = jsonb.fromJson(json, Vehicle.class);
        assertTrue(vehicle instanceof Car);
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

    @JsonbPolymorphicType(key = "@animal", value = {
            @JsonbSubtype(alias = "dog", type = Dog.class)
    })
    public interface Animal {}

    @JsonbPolymorphicType(key = "@livingThing", value = {
            @JsonbSubtype(alias = "dog", type = Dog.class)
    })
    public interface LivingEntity {}

    public static final class Dog implements Animal, LivingEntity {}

    @JsonbPolymorphicType(key = "@machine", value = {
            @JsonbSubtype(alias = "vehicle", type = Vehicle.class)
    })
    public static class Machine {
        public String machineProperty = "machineProperty";
    }

    @JsonbPolymorphicType(key = "@vehicle", value = {
            @JsonbSubtype(alias = "car", type = Car.class)
    })
    public static class Vehicle extends Machine {
        public String vehicleProperty = "vehicleProperty";
    }

    public static class Car extends Vehicle {
        public String carProperty = "carProperty";
    }

    @JsonbPolymorphicType({
            @JsonbSubtype(alias = "integer", type = Integer.class)
    })
    public static class InvalidAlias {}

}
