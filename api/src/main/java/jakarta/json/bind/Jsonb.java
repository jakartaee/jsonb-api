/*
 * Copyright (c) 2016, 2026 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.json.bind;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

/**
 * <p>Converts Java objects to and from JSON documents.
 *
 * <p>{@code Jsonb} provides an abstraction over the JSON Binding framework operations:</p>
 *
 * <ul>
 * <li>{@code fromJson}: reads JSON input, deserializes to a Java objects content tree
 * <li>{@code toJson}: serializes a Java objects content tree to JSON output
 * </ul>
 *
 * <p>An instance of this class is created using a {@link JsonbBuilder}
 * builder method:</p>
 * <pre>{@code
 * // Example 1 - Creating Jsonb using default JsonbBuilder instance provided by default JsonbProvider
 * Jsonb jsonb = JsonbBuilder.create();
 *
 * // Example 2 - Creating Jsonb instance for a specific provider specified by a class name
 * Jsonb jsonb = JsonbBuilder.newBuilder("foo.bar.ProviderImpl").build();
 *
 * // Example 3 - Creating Jsonb instance from a custom provider implementation
 * Jsonb jsonb = new CustomJsonbBuilder().build();
 * }</pre>
 *
 * <h2>Deserializing (reading) JSON</h2>
 *
 * <p>You can deserialize JSON data that represents either an entire JSON
 * document or a subtree of a JSON document.
 *
 * <p>Reading (deserializing) an object content tree from a file:
 * <pre>{@code
 *   Jsonb jsonb = JsonbBuilder.create();
 *   try (FileReader reader = new FileReader("jsonfile.json")) {
 *       Book book = jsonb.fromJson(reader, Book.class);
 *   }
 * }</pre>
 *
 * <p>If the deserialization process is unable to deserialize the JSON content to an object
 * content tree, a fatal error is reported that terminates processing by
 * throwing {@link JsonbException}.
 *
 * <h2>Serializing (writing) to JSON</h2>
 *
 * <p>Serialization writes the representation of a Java object content tree into
 * JSON data.</p>
 *
 * <p>Writing (serializing) an object content tree to a file:</p>
 * <pre>{@code
 *   try (FileWriter writer = new FileWriter("foo.json")) {
 *       jsonb.toJson(object, writer);
 *   }
 * }</pre>
 *
 * <p>Writing (serializing) to a {@link java.io.Writer Writer}:</p>
 * <pre>{@code
 *   PrintWriter writer = new PrintWriter(System.out);
 *   jsonb.toJson(object, writer);
 *   writer.flush();
 * }</pre>
 *
 * <h2>Encoding</h2>
 *
 * <p>In deserialization operations ({@code fromJson}), encoding of JSON data
 * is detected automatically. You can use the {@link JsonbConfig} API to
 * manually configure the input encoding for deserialization operations.
 * Applications must supply a valid character encoding as defined in the
 * <a href="http://tools.ietf.org/html/rfc7159">RFC 7159</a> and which is
 * also supported by the Java Platform.
 *
 * <p>In serialization operations ({@code toJson}), UTF-8 encoding is used
 * by default for writing JSON data.
 * Use the {@link JsonbConfig} API to configure the
 * output encoding for serialization operations. Applications must supply
 * a valid character encoding as defined in the
 * <a href="http://tools.ietf.org/html/rfc7159">RFC 7159</a> and which is
 * also supported by Java Platform.
 *
 * <p>For optimal performance, reuse {@code JsonbBuilder} and {@code Jsonb}
 * instances. For a typical use-case, only one {@code Jsonb} instance is
 * required by an application.
 *
 * <p>All the methods in this class are safe for concurrent use by multiple
 * threads.
 *
 * <p>Calling the {@link #close()} method cleans up all CDI managed components
 * (such as adapters with CDI dependencies) created during interaction with
 * the respective {@code Jsonb} instance. Calling {@code close()} must be done
 * after all threads have finished interaction with the {@code Jsonb} instance.
 * If there are remaining threads working with the {@code Jsonb} instance and
 * {@code close()} is called, behaviour is undefined.
 *
 * @see JsonbBuilder
 * @see java.util.ServiceLoader
 * @since JSON Binding 1.0
 */
public interface Jsonb extends AutoCloseable {

    /**
     * Reads JSON data from the specified string and returns the resulting
     * content tree.
     *
     * @param str
     *      The string to deserialize JSON data from.
     * @param type
     *      Type of the content tree's root object.
     * @param <T>
     *      Type of the content tree's root object.
     *
     * @return the newly created root object of the java content tree
     *
     * @throws JsonbException
     *      If an unexpected error occurs during deserialization.
     * @throws NullPointerException
     *      If any of the parameters is {@code null}.
     */
    <T> T fromJson(String str, Class<T> type) throws JsonbException;

    /**
     * Reads JSON data from the specified string and returns the resulting
     * content tree.
     *
     * @param str
     *      The string to deserialize JSON data from.
     * @param runtimeType
     *      Runtime type of the content tree's root object.
     * @param <T>
     *      Type of the content tree's root object.
     *
     * @return the newly created root object of the java content tree
     *
     * @throws JsonbException
     *      If an unexpected error occurs during deserialization.
     * @throws NullPointerException
     *      If any of the parameters is {@code null}.
     */
    <T> T fromJson(String str, Type runtimeType) throws JsonbException;

    /**
     * Reads JSON data from the specified {@code reader} and returns the
     * resulting content tree.
     *
     * @param reader
     *      The character stream from which to read JSON data.
     * @param type
     *      Type of the content tree's root object.
     * @param <T>
     *      Type of the content tree's root object.
     *
     * @return the newly created root object of the java content tree
     *
     * @throws JsonbException
     *      If an unexpected error occurs during deserialization.
     * @throws NullPointerException
     *      If any of the parameters is {@code null}.
     */
    <T> T fromJson(Reader reader, Class<T> type) throws JsonbException;

    /**
     * Reads JSON data from the specified {@code reader} and returns the
     * resulting content tree.
     *
     * @param reader
     *      The character stream from which to read JSON data.
     *
     * @param runtimeType
     *      Runtime type of the content tree's root object.
     *
     * @param <T>
     *      Type of the content tree's root object.
     *
     * @return the newly created root object of the Java content tree
     *
     * @throws JsonbException
     *      If an unexpected error occurs during deserialization.
     * @throws NullPointerException
     *      If any of the parameters is {@code null}.
     */
    <T> T fromJson(Reader reader, Type runtimeType) throws JsonbException;

    /**
     * Reads JSON data from the specified {@code InputStream} and returns the
     * resulting content tree.
     *
     * @param stream
     *      The stream from which to read JSON data. Upon
     *      successful completion, the stream is closed by this method.
     * @param type
     *      Type of the content tree's root object.
     * @param <T>
     *      Type of the content tree's root object.
     *
     * @return the newly created root object of the Java content tree
     *
     * @throws JsonbException
     *      If an unexpected error occurs during deserialization.
     * @throws NullPointerException
     *      If any of the parameters is {@code null}.
     */
    <T> T fromJson(InputStream stream, Class<T> type) throws JsonbException;

    /**
     * Reads JSON data from the specified {@code InputStream} and returns the
     * resulting content tree.
     *
     * @param stream
     *      The stream from which to read JSON data. Upon
     *      successful completion, the stream is closed by this method.
     *
     * @param runtimeType
     *      Runtime type of the content tree's root object.
     *
     * @param <T>
     *      Type of the content tree's root object.
     *
     * @return the newly created root object of the Java content tree
     *
     * @throws JsonbException
     *      If an unexpected error occurs during deserialization.
     * @throws NullPointerException
     *      If any of the parameters is {@code null}.
     */
    <T> T fromJson(InputStream stream, Type runtimeType) throws JsonbException;

    /**
     * Writes the Java object tree with the given root object, {@code object},
     * to a {@code String} instance as JSON.
     *
     * @param object
     *      The root object of the object content tree to be serialized.
     *      Must not be {@code null}.
     *
     * @return String instance with serialized JSON data.
     *
     * @throws JsonbException If any unexpected problem occurs during the
     *      serialization, such as an I/O error.
     * @throws NullPointerException
     *      If any of the parameters is {@code null}.
     *
     * @since JSON Binding 1.0
     */
    String toJson(Object object) throws JsonbException;

    /**
     * Writes the Java object tree with the given root object, {@code object},
     * to a {@code String} instance as JSON.
     *
     * @param object
     *      The root object of the object content tree to be serialized.
     *      Must not be {@code null}.
     *
     * @param runtimeType
     *      Runtime type of the content tree's root object. The provided
     *      {@code runtimeType} must be a supertype of, or the same type as,
     *      the actual class of the provided {@code object}.
     *
     * @return String instance with serialized JSON data.
     *
     * @throws JsonbException If any unexpected problem occurs during the
     *      serialization, such as an I/O error.
     * @throws NullPointerException
     *      If any of the parameters is {@code null}.
     *
     * @since JSON Binding 1.0
     */
    String toJson(Object object, Type runtimeType) throws JsonbException;

    /**
     * Writes the object content tree into a {@code Writer} character stream.
     *
     * @param object
     *      The object content tree to be serialized.
     * @param writer
     *      The JSON will be sent as a character stream to the given
     *      {@link Writer}.
     *
     * @throws JsonbException If any unexpected problem occurs during the
     *      serialization.
     * @throws NullPointerException
     *      If any of the parameters is {@code null}.
     *
     * @since JSON Binding 1.0
     */
    void toJson(Object object, Writer writer) throws JsonbException;

    /**
     * Writes the object content tree into a {@code Writer} character stream.
     *
     * @param object
     *      The object content tree to be serialized.
     *
     * @param runtimeType
     *      Runtime type of the content tree's root object. The provided
     *      {@code runtimeType} must be a supertype of, or the same type as,
     *      the actual class of the provided {@code object}.
     *
     * @param writer
     *      The JSON will be sent as a character stream to the given
     *      {@link Writer}.
     *
     * @throws JsonbException If any unexpected problem occurs during the
     *      serialization.
     * @throws NullPointerException
     *      If any of the parameters is {@code null}.
     *
     * @since JSON Binding 1.0
     */
    void toJson(Object object, Type runtimeType, Writer writer) throws JsonbException;

    /**
     * Writes the object content tree to the given {@code stream}.
     *
     * @param object
     *      The object content tree to be serialized.
     * @param stream
     *      The JSON will be sent as a byte stream to the given
     *      {@link OutputStream}. Upon successful completion, the stream is
     *      closed by this method.
     *
     * @throws JsonbException If any unexpected problem occurs during the
     *      serialization.
     * @throws NullPointerException
     *      If any of the parameters is {@code null}.
     *
     * @since JSON Binding 1.0
     */
    void toJson(Object object, OutputStream stream) throws JsonbException;

    /**
     * Writes the object content tree to the given {@code stream}.
     *
     * @param object
     *      The object content tree to be serialized.
     *
     * @param runtimeType
     *      Runtime type of the content tree's root object. The provided
     *      {@code runtimeType} must be a supertype of, or the same type as,
     *      the actual class of the provided {@code object}.
     *
     * @param stream
     *      The JSON will be sent as a byte stream to the given
     *      {@link OutputStream}. Upon successful completion, the stream is
     *      closed by this method.
     *
     * @throws JsonbException If any unexpected problem occurs during the
     *      serialization.
     * @throws NullPointerException
     *      If any of the parameters is {@code null}.
     *
     * @since JSON Binding 1.0
     */
    void toJson(Object object, Type runtimeType, OutputStream stream) throws JsonbException;
}
