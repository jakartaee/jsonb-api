/*
 * Copyright (c) 2015, 2026 Oracle and/or its affiliates. All rights reserved.
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
 * Provides the JSON Binding API, which enables binding Java objects to and
 * from JSON documents.
 *
 * <p>The main entry point to the API is {@link JsonbBuilder}, which builds
 * a {@link jakarta.json.bind.Jsonb Jsonb} instance that you use to read and
 * write JSON documents. For example,
 *
 * <pre>{@code
 *   public record Lake(String name, int area, int maxDepth) {
 *   }
 *
 *   String json = """
 *             [
 *              { "name": "Lake Superior", "area": 31700, "maxDepth": 1333 },
 *              { "name": "Lake Michigan", "area": 22300, "maxDepth": 925  },
 *              { "name": "Lake Huron",    "area": 23000, "maxDepth": 750  }
 *             ]
 *             """;
 *
 *   Jsonb jsonb = JsonbBuilder.create();
 *
 *   Lake[] lakes = jsonb.fromJson(json, Lake[].class);
 *
 *   String name = lakes[0].name();     // "Lake Superior"
 *   int   depth = lakes[0].maxDepth(); // 1333
 *
 *   String json_superior = jsonb.toJson(lakes[0]);
 *
 *   // Close if no longer needed; a Jsonb instance should be reused throughout the application
 *   jsonb.close();
 * }</pre>
 *
 * <p>The {@linkplain jakarta.json.bind/ module Javadoc} has additional examples.
 *
 * @see jakarta.json.bind.Jsonb
 * @see jakarta.json.bind.JsonbBuilder
 * @see jakarta.json.bind.JsonbConfig
 * @since JSON Binding 1.0
 */
package jakarta.json.bind;
