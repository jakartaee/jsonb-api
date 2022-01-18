/*
 * Copyright (c) 2017, 2021 Oracle and/or its affiliates. All rights reserved.
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
        String jsonString = jsonb.toJson(value);
        assertThat("[testMarshalling] - Failed to correctly marshal " + value.getClass().getName() + " object",
                   jsonString, matchesPattern(expectedRepresentation));
    }

    private void testMarshallingToStream(T value, String expectedRepresentation) {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            jsonb.toJson(value, stream);
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
            jsonb.toJson(value, writer);
            String jsonString = new String(stream.toByteArray(), StandardCharsets.UTF_8);
            assertThat("[testMarshallingToWriter] - Failed to correctly marshal " + value.getClass().getName() + " object",
                       jsonString, matchesPattern(expectedRepresentation));
        } catch (IOException e) {
            fail(e.getMessage(), e);
        }
    }

    private void testMarshallingByType(T value, String expectedRepresentation) {
        String jsonString = jsonb.toJson(value, serializationType);
        assertThat("[testMarshallingByType] - Failed to correctly marshal " + value.getClass().getName() + " object",
                   jsonString, matchesPattern(expectedRepresentation));
    }

    private void testMarshallingByTypeToStream(T value, String expectedRepresentation) {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            jsonb.toJson(value, serializationType, stream);
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
            jsonb.toJson(value, serializationType, writer);
            String jsonString = new String(stream.toByteArray(), StandardCharsets.UTF_8);
            assertThat("[testMarshallingByTypeToWriter] - Failed to correctly marshal " + value.getClass().getName() + " object",
                       jsonString, matchesPattern(expectedRepresentation));
        } catch (IOException e) {
            fail(e.getMessage(), e);
        }
    }

    private void testUnmarshallingByClass(String expectedRepresentation, Object value) {
        Object unmarshalledObject = jsonb.fromJson(expectedRepresentation, typeClass);
        assertThat("[testUnmarshallingByClass] - Failed to correctly unmarshal " + value.getClass().getName() + " object",
                   unmarshalledObject, is(value));
    }

    private void testUnmarshallingByClassFromStream(String expectedRepresentation, Object value) {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(expectedRepresentation.getBytes(StandardCharsets.UTF_8))) {
            Object unmarshalledObject = jsonb.fromJson(stream, typeClass);
            assertThat("[testUnmarshallingByClassFromStream] - Failed to correctly unmarshal " + value.getClass()
                               .getName() + " object",
                       unmarshalledObject, is(value));
        } catch (IOException e) {
            fail(e.getMessage(), e);
        }
    }

    private void testUnmarshallingByClassFromReader(String expectedRepresentation, Object value) {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(expectedRepresentation.getBytes(StandardCharsets.UTF_8));
                InputStreamReader reader = new InputStreamReader(stream)) {
            Object unmarshalledObject = jsonb.fromJson(reader, typeClass);
            assertThat("[testUnmarshallingByClassFromReader] - Failed to correctly unmarshal " + value.getClass()
                               .getName() + " object",
                       unmarshalledObject, is(value));
        } catch (IOException e) {
            fail(e.getMessage(), e);
        }
    }

    private void testUnmarshallingByType(String expectedRepresentation, Object value) {
        Object unmarshalledObject = jsonb.fromJson(expectedRepresentation, (Type) typeClass);
        assertThat("[testUnmarshallingByType] - Failed to correctly unmarshal " + value.getClass().getName() + " object",
                   unmarshalledObject, is(value));
    }

    private void testUnmarshallingByTypeFromStream(String expectedRepresentation, Object value) {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(expectedRepresentation.getBytes(StandardCharsets.UTF_8))) {
            Object unmarshalledObject = jsonb.fromJson(stream, (Type) typeClass);
            assertThat("[testUnmarshallingByTypeFromStream] - Failed to correctly unmarshal " + value.getClass()
                               .getName() + " object",
                       unmarshalledObject, is(value));
        } catch (IOException e) {
            fail(e.getMessage(), e);
        }
    }

    private void testUnmarshallingByTypeFromReader(String expectedRepresentation, Object value) {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(expectedRepresentation.getBytes(StandardCharsets.UTF_8));
                InputStreamReader reader = new InputStreamReader(stream)) {
            Object unmarshalledObject = jsonb.fromJson(reader, (Type) typeClass);
            assertThat("[testUnmarshallingByTypeFromReader] - Failed to correctly unmarshal " + value.getClass()
                               .getName() + " object",
                       unmarshalledObject, is(value));
        } catch (IOException e) {
            fail(e.getMessage(), e);
        }
    }
}
