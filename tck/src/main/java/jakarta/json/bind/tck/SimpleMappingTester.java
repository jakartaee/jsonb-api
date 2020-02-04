/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.json.bind.tck;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

public class SimpleMappingTester<T> {
  private final Jsonb jsonb = JsonbBuilder.create();

  private final Class<T> typeClass;

  public SimpleMappingTester(Class<T> typeClass) {
    this.typeClass = typeClass;
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
    if (jsonString.matches(expectedRepresentation)) {
      return; // passed
    } else {
      fail("[testMarshalling] - Failed to correctly marshal "
          + value.getClass().getName() + " object");
    }
  }

  private void testMarshallingToStream(T value,
      String expectedRepresentation) {
    try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
      jsonb.toJson(value, stream);
      String jsonString = new String(stream.toByteArray(),
          StandardCharsets.UTF_8);
      if (jsonString.matches(expectedRepresentation)) {
        return; // passed
      } else {
        fail("[testMarshallingToStream] - Failed to correctly marshal "
                + value.getClass().getName() + " object");
      }
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  private void testMarshallingToWriter(T value,
      String expectedRepresentation) {
    try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(stream)) {
      jsonb.toJson(value, writer);
      String jsonString = new String(stream.toByteArray(),
          StandardCharsets.UTF_8);
      if (jsonString.matches(expectedRepresentation)) {
        return; // passed
      } else {
        fail("[testMarshallingToWriter] - Failed to correctly marshal "
                + value.getClass().getName() + " object");
      }
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  private void testMarshallingByType(T value, String expectedRepresentation) {
    String jsonString = jsonb.toJson(value, TypeContainer.class);
    if (jsonString.matches(expectedRepresentation)) {
      return; // passed
    } else {
      fail("[testMarshallingByType] - Failed to correctly marshal "
              + value.getClass().getName() + " object");
    }
  }

  private void testMarshallingByTypeToStream(T value,
      String expectedRepresentation) {
    try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
      jsonb.toJson(value, TypeContainer.class, stream);
      String jsonString = new String(stream.toByteArray(),
          StandardCharsets.UTF_8);
      if (jsonString.matches(expectedRepresentation)) {
        return; // passed
      } else {
        fail(
            "[testMarshallingByTypeToStream] - Failed to correctly marshal "
                + value.getClass().getName() + " object");
      }
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  private void testMarshallingByTypeToWriter(T value,
      String expectedRepresentation) {
    try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(stream)) {

      jsonb.toJson(value, TypeContainer.class, writer);
      String jsonString = new String(stream.toByteArray(),
          StandardCharsets.UTF_8);
      if (jsonString.matches(expectedRepresentation)) {
        return; // passed
      } else {
        fail(
            "[testMarshallingByTypeToWriter] - Failed to correctly marshal "
                + value.getClass().getName() + " object");
      }
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  private void testUnmarshallingByClass(String expectedRepresentation,
      Object value) {
    Object unmarshalledObject = jsonb.fromJson(expectedRepresentation,
        typeClass);
    if (value.equals(unmarshalledObject)) {
      return; // passed
    } else {
      fail("[testUnmarshallingByClass] - Failed to correctly unmarshal "
              + value.getClass().getName() + " object");
    }
  }

  private void testUnmarshallingByClassFromStream(
      String expectedRepresentation, Object value) {
    try (ByteArrayInputStream stream = new ByteArrayInputStream(
        expectedRepresentation.getBytes(StandardCharsets.UTF_8))) {
      Object unmarshalledObject = jsonb.fromJson(stream, typeClass);
      if (value.equals(unmarshalledObject)) {
        return; // passed
      } else {
        fail(
            "[testUnmarshallingByClassFromStream] - Failed to correctly unmarshal "
                + value.getClass().getName() + " object");
      }
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  private void testUnmarshallingByClassFromReader(
      String expectedRepresentation, Object value) {
    try (
        ByteArrayInputStream stream = new ByteArrayInputStream(
            expectedRepresentation.getBytes(StandardCharsets.UTF_8));
        InputStreamReader reader = new InputStreamReader(stream)) {

      Object unmarshalledObject = jsonb.fromJson(reader, typeClass);
      if (value.equals(unmarshalledObject)) {
        return; // passed
      } else {
        fail(
            "[testUnmarshallingByClassFromReader] - Failed to correctly unmarshal "
                + value.getClass().getName() + " object");
      }
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  private void testUnmarshallingByType(String expectedRepresentation,
      Object value) {
    Object unmarshalledObject = jsonb.fromJson(expectedRepresentation,
        (Type) typeClass);
    if (value.equals(unmarshalledObject)) {
      return; // passed
    } else {
      fail("[testUnmarshallingByType] - Failed to correctly unmarshal "
              + value.getClass().getName() + " object");
    }
  }

  private void testUnmarshallingByTypeFromStream(
      String expectedRepresentation, Object value) {
    try (ByteArrayInputStream stream = new ByteArrayInputStream(
        expectedRepresentation.getBytes(StandardCharsets.UTF_8))) {
      Object unmarshalledObject = jsonb.fromJson(stream, (Type) typeClass);
      if (value.equals(unmarshalledObject)) {
        return; // passed
      } else {
        fail(
            "[testUnmarshallingByTypeFromStream] - Failed to correctly unmarshal "
                + value.getClass().getName() + " object");
      }
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  private void testUnmarshallingByTypeFromReader(
      String expectedRepresentation, Object value) {
    try (
        ByteArrayInputStream stream = new ByteArrayInputStream(
            expectedRepresentation.getBytes(StandardCharsets.UTF_8));
        InputStreamReader reader = new InputStreamReader(stream)) {

      Object unmarshalledObject = jsonb.fromJson(reader, (Type) typeClass);
      if (value.equals(unmarshalledObject)) {
        return; // passed
      } else {
        fail(
            "[testUnmarshallingByTypeFromReader] - Failed to correctly unmarshal "
                + value.getClass().getName() + " object");
      }
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }
}
