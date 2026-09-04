/*
 * Copyright (c) 2017, 2026 Oracle and/or its affiliates. All rights reserved.
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

/**
 * Jakarta JSON Binding (JSON-B) defines a standard binding layer for
 * converting Java objects to and from JSON documents. It provides a
 * default mapping that covers the most common Java types and a rich
 * set of annotations and configuration options for customising that
 * mapping.
 *
 * <p>{@link jakarta.json.bind.JsonbBuilder} is the starting point to
 * create a {@link jakarta.json.bind.Jsonb} instance, which is then used
 * to serialise and deserialise Java objects.
 *
 * <h2>JSON property names</h2>
 *
 * <h3>Names from fields and record components</h3>
 *
 * <p>The examples below demonstrate how JSON-B maps a string, a boolean,
 * a number, an array, a nested object, and an enum:
 *
 * <pre>{@code
 *   public class Planet {
 *       public String       name;
 *       public boolean      isHabitable;
 *       public long         mass;
 *       public List<String> moons;
 *       public Star         star;
 *   }
 *
 *   public record Star(String name, StarType type) {}
 *
 *   public enum StarType { MAIN_SEQUENCE, GIANT, DWARF }
 * }</pre>
 *
 * <p>Conversion of a Java object to JSON and then back to a Java object:
 *
 * <pre>{@code
 *   Jsonb jsonb = JsonbBuilder.create();
 *
 *   Planet mars = new Planet();
 *   mars.name = "Mars";
 *   mars.isHabitable = false;
 *   mars.mass = 641693L;
 *   mars.moons = List.of("Phobos", "Deimos");
 *   mars.star = new Star("Sol", StarType.MAIN_SEQUENCE);
 *
 *   String json = jsonb.toJson(mars);
 *
 *   // Generated JSON:
 *   // {
 *   //  "name": "Mars",
 *   //  "isHabitable": false,
 *   //  "mass": 641693,
 *   //  "moons": ["Phobos", "Deimos"],
 *   //  "star": {
 *   //           "name": "Sol",
 *   //           "type": "MAIN_SEQUENCE"
 *   //          }
 *   // }
 *
 *   Planet planet = jsonb.fromJson(json, Planet.class);
 *
 *   // Close if no longer needed; a Jsonb instance should be reused throughout the application
 *   jsonb.close();
 * }</pre>
 *
 * <h3>Names from accessor methods</h3>
 *
 * <p>JSON property can be derived from names of accessor methods with
 * {@code public} visibility. In the following class the {@code period} field
 * is package-private, so it does not determine the name of a JSON-B property.
 * The field is exposed through accessor methods named {@code getOrbitalPeriod}
 * and {@code setOrbitalPeriod}, so the JSON property name is
 * {@code "orbitalPeriod"}:
 *
 * <pre>{@code
 *   public class Comet {
 *       String name;
 *       int period;
 *
 *       public String getName()                   { return name; }
 *       public int    getOrbitalPeriod()          { return period; }
 *
 *       public void   setName(String value)       { name = value; }
 *       public void   setOrbitalPeriod(int value) { period = value; }
 *   }
 * }</pre>
 *
 * <pre>{@code    String json = """
 *           {
 *             "name": "Halley",
 *             "orbitalPeriod": 75
 *           }
 *           """;
 *
 *   Comet comet = jsonb.fromJson(json, Comet.class);
 *   String name   = comet.getName();          // "Halley"
 *   int    period = comet.getOrbitalPeriod(); // 75
 * }</pre>
 *
 * <p>For advanced configuration (custom serializers, date formats, property
 * naming strategies, etc.) see {@link jakarta.json.bind.JsonbConfig}.
 *
 * @see jakarta.json.bind.Jsonb
 * @see jakarta.json.bind.JsonbBuilder
 * @see jakarta.json.bind.JsonbConfig
 */
module jakarta.json.bind {
    exports jakarta.json.bind;
    exports jakarta.json.bind.adapter;
    exports jakarta.json.bind.annotation;
    exports jakarta.json.bind.config;
    exports jakarta.json.bind.serializer;
    exports jakarta.json.bind.spi;

    requires jakarta.json;
    requires java.logging;

    uses jakarta.json.bind.spi.JsonbProvider;
}
