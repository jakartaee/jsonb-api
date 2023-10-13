/*
 * Copyright (c) 2017, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.json.bind;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.fail;

public class SimpleMappingTester<T> {
    private final Jsonb jsonb = JsonbBuilder.create();

    private final Class<T> typeClass;
    private final Class<? super T> serializationType;

    public SimpleMappingTester(Class<T> typeClass, Class<? super T> serializationType) {
        this.typeClass = Objects.requireNonNull(typeClass);
        this.serializationType = Objects.requireNonNull(serializationType);
    }

    public void test(T value, String expectedRepresentationPattern,
                     String expectedRepresentation, Object expectedOutput) {
        testMarshalling(value, expectedRepresentationPattern);
        testMarshallingToStream(value, expectedRepresentationPattern);
        testMarshallingToWriter(value, expectedRepresentationPattern);
        testMarshallingByType(value, expectedRepresentationPattern);
        testMarshallingByTypeToStream(value, expectedRepresentationPattern);
        testMarshallingByTypeToWriter(value, expectedRepresentationPattern);
        testUnmarshallingByClass(expectedRepresentation, expectedOutput);
        testUnmarshallingByClassFromStream(expectedRepresentation,
                                           expectedOutput);
        testUnmarshallingByClassFromReader(expectedRepresentation,
                                           expectedOutput);
        testUnmarshallingByType(expectedRepresentation, expectedOutput);
        testUnmarshallingByTypeFromStream(expectedRepresentation,
                                          expectedOutput);
        testUnmarshallingByTypeFromReader(expectedRepresentation,
                                          expectedOutput);
    }

    private void testMarshalling(T value, String expectedRepresentation) {
        String jsonString = jsonb.toJson(value); // Assumes character encoding is Default when creating Writer
        assertThat("[testMarshalling] - Failed to correctly marshal " + value.getClass().getName() + " object",
                   jsonString, matchesPattern(expectedRepresentation));
    }

    private void testMarshallingToStream(T value, String expectedRepresentation) {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            jsonb.toJson(value, stream); // Assumes character encoding is UTF-8 when creating Writer
            String jsonString = new String(stream.toByteArray(), StandardCharsets.UTF_8);
            assertThat("[testMarshallingToStream] - Failed to correctly marshal " + value.getClass().getName() + " object",
                       jsonString, matchesPattern(expectedRepresentation));
        } catch (IOException e) {
            fail(e.getMessage(), e);
        }
    }

    private void testMarshallingToWriter(T value, String expectedRepresentation) {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(stream)) {
            jsonb.toJson(value, writer); // Writer determins character encoding (Default)
            String jsonString = new String(stream.toByteArray(), Charset.defaultCharset());
            assertThat("[testMarshallingToWriter] - Failed to correctly marshal " + value.getClass().getName() + " object",
                       jsonString, matchesPattern(expectedRepresentation));
        } catch (IOException e) {
            fail(e.getMessage(), e);
        }
    }

    private void testMarshallingByType(T value, String expectedRepresentation) {
        String jsonString = jsonb.toJson(value, serializationType); // Assumes character encoding is Default when creating Writer
        assertThat("[testMarshallingByType] - Failed to correctly marshal " + value.getClass().getName() + " object",
                   jsonString, matchesPattern(expectedRepresentation));
    }

    private void testMarshallingByTypeToStream(T value, String expectedRepresentation) {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            jsonb.toJson(value, serializationType, stream); // Assumes character encoding is UTF-8 when creating Writer
            String jsonString = new String(stream.toByteArray(), StandardCharsets.UTF_8);
            assertThat("[testMarshallingByTypeToStream] - Failed to correctly marshal " + value.getClass().getName() + " object",
                       jsonString, matchesPattern(expectedRepresentation));
        } catch (IOException e) {
            fail(e.getMessage(), e);
        }
    }

    private void testMarshallingByTypeToWriter(T value, String expectedRepresentation) {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(stream)) {
            jsonb.toJson(value, serializationType, writer); // Writer determins character encoding (Default)
            String jsonString = new String(stream.toByteArray(), Charset.defaultCharset());
            assertThat("[testMarshallingByTypeToWriter] - Failed to correctly marshal " + value.getClass().getName() + " object",
                       jsonString, matchesPattern(expectedRepresentation));
        } catch (IOException e) {
            fail(e.getMessage(), e);
        }
    }

    private void testUnmarshallingByClass(String expectedRepresentation, Object value) {
        Object unmarshalledObject = jsonb.fromJson(expectedRepresentation, typeClass); // Assumes character encoding is Default when creating Reader
        assertThat("[testUnmarshallingByClass] - Failed to correctly unmarshal " + value.getClass().getName() + " object",
                   unmarshalledObject, is(value));
    }

    private void testUnmarshallingByClassFromStream(String expectedRepresentation, Object value) {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(expectedRepresentation.getBytes(StandardCharsets.UTF_8))) {
            Object unmarshalledObject = jsonb.fromJson(stream, typeClass); // Assumes character encoding is UTF-8 when creating Reader
            assertThat("[testUnmarshallingByClassFromStream] - Failed to correctly unmarshal " + value.getClass()
                               .getName() + " object",
                       unmarshalledObject, is(value));
        } catch (IOException e) {
            fail(e.getMessage(), e);
        }
    }

    private void testUnmarshallingByClassFromReader(String expectedRepresentation, Object value) {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(expectedRepresentation.getBytes());
                InputStreamReader reader = new InputStreamReader(stream)) {
            Object unmarshalledObject = jsonb.fromJson(reader, typeClass); // Reader determins character encoding (Default)
            assertThat("[testUnmarshallingByClassFromReader] - Failed to correctly unmarshal " + value.getClass()
                               .getName() + " object",
                       unmarshalledObject, is(value));
        } catch (IOException e) {
            fail(e.getMessage(), e);
        }
    }

    private void testUnmarshallingByType(String expectedRepresentation, Object value) {
        Object unmarshalledObject = jsonb.fromJson(expectedRepresentation, (Type) typeClass); // Assumes character encoding is Default when creating Writer
        assertThat("[testUnmarshallingByType] - Failed to correctly unmarshal " + value.getClass().getName() + " object",
                   unmarshalledObject, is(value));
    }

    private void testUnmarshallingByTypeFromStream(String expectedRepresentation, Object value) {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(expectedRepresentation.getBytes(StandardCharsets.UTF_8))) {
            Object unmarshalledObject = jsonb.fromJson(stream, (Type) typeClass); // Assumes character encoding is UTF-8 when creating Writer
            assertThat("[testUnmarshallingByTypeFromStream] - Failed to correctly unmarshal " + value.getClass()
                               .getName() + " object",
                       unmarshalledObject, is(value));
        } catch (IOException e) {
            fail(e.getMessage(), e);
        }
    }

    private void testUnmarshallingByTypeFromReader(String expectedRepresentation, Object value) {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(expectedRepresentation.getBytes());
                InputStreamReader reader = new InputStreamReader(stream)) {
            Object unmarshalledObject = jsonb.fromJson(reader, (Type) typeClass);  // Reader determins character encoding (Default)
            assertThat("[testUnmarshallingByTypeFromReader] - Failed to correctly unmarshal " + value.getClass()
                               .getName() + " object",
                       unmarshalledObject, is(value));
        } catch (IOException e) {
            fail(e.getMessage(), e);
        }
    }
}
