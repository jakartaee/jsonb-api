/*
 * Copyright (c) 2016, 2018 Oracle and/or its affiliates. All rights reserved.
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

package javax.json.bind.serializer;

import javax.json.stream.JsonGenerator;

/**
 * <p>Interface representing a custom serializer for given type. Unlike {@link javax.json.bind.adapter.JsonbAdapter}
 * serializer provides more fine grained control over serialization process by writing java object directly into
 * JSON stream using {@link JsonGenerator}. {@link SerializationContext} acts as JSONB runtime, able to serialize
 * any java object provided.</p>
 *
 * <p>Serializers are registered using {@link javax.json.bind.JsonbConfig#withSerializers(JsonbSerializer[])}
 * method or using {@link javax.json.bind.annotation.JsonbTypeSerializer} annotation on type</p>
 *
 * <p>Sample of custom Serializer:</p>
 * <pre>
 * class Box {
 *     public BoxInner boxInnerObject;
 *     public String name;
 * }
 *
 * class BoxSerializer implements JsonbSerializer&lt;Box&gt; {
 *      public void serialize(Box box, JsonGenerator generator, SerializationContext ctx) {
 *          generator.write("name", box.name);
 *          ctx.serialize("boxInnerObject", generator);
 *      }
 * }
 * </pre>
 *
 * @param <T> Type to bind serializer for.
 * @see javax.json.bind.JsonbConfig
 * @see javax.json.bind.annotation.JsonbTypeSerializer
 * @see JsonbDeserializer
 * @see javax.json.bind.adapter.JsonbAdapter
 * @since JSON Binding 1.0
 */
public interface JsonbSerializer<T> {

    /**
     * Serializes object into JSON stream.
     *
     * @param obj
     *      Object to serialize.
     * @param generator
     *      JSON generator used to write java object to JSON stream.
     * @param ctx
     *      JSONB mapper context. Use it to serialize sub-objects.
     */
    void serialize(T obj, JsonGenerator generator, SerializationContext ctx);
}
