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

/*
 * $Id$
 */

package jakarta.json.bind.api.config;

import static org.junit.Assert.fail;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;

import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.adapter.JsonbAdapter;
import jakarta.json.bind.api.model.SimpleContainerDeserializer;
import jakarta.json.bind.api.model.SimpleContainerSerializer;
import jakarta.json.bind.api.model.SimpleIntegerAdapter;
import jakarta.json.bind.api.model.SimpleIntegerDeserializer;
import jakarta.json.bind.api.model.SimpleIntegerSerializer;
import jakarta.json.bind.api.model.SimplePropertyNamingStrategy;
import jakarta.json.bind.api.model.SimplePropertyVisibilityStrategy;
import jakarta.json.bind.api.model.SimpleStringAdapter;
import jakarta.json.bind.config.BinaryDataStrategy;
import jakarta.json.bind.config.PropertyNamingStrategy;
import jakarta.json.bind.config.PropertyOrderStrategy;
import jakarta.json.bind.serializer.JsonbDeserializer;
import jakarta.json.bind.serializer.JsonbSerializer;

/**
 * @test
 * @sources JsonbConfigTest.java
 * @executeClass com.sun.ts.tests.jsonb.api.JsonbConfigTest
 **/
public class JsonbConfigTest {

  /*
   * @testName: testGetAsMap
   *
   * @assertion_ids: JSONB:JAVADOC:33; JSONB:JAVADOC:35
   *
   * @test_Strategy: Assert that JsonbConfig.getAsMap returns all configuration
   * properties as an unmodifiable map
   */
  @Test
  public void testGetAsMap() {
    JsonbConfig jsonbConfig = new JsonbConfig().withFormatting(true)
        .withNullValues(true);
    Map<String, Object> configMap = jsonbConfig.getAsMap();
    if (configMap.size() != 2 || !configMap.containsKey(JsonbConfig.FORMATTING)
        || !configMap.containsKey(JsonbConfig.NULL_VALUES)) {
      fail(
          "Failed to get configuration properties as a map using JsonbConfig.getAsMap method.");
    }

    try {
      configMap.put(JsonbConfig.BINARY_DATA_STRATEGY,
          BinaryDataStrategy.BASE_64);
      fail(
          "Failed to get configuration properties as an unmodifiable map using JsonbConfig.getAsMap method.");
    } catch (UnsupportedOperationException x) {
    }
  }

  /*
   * @testName: testGetProperty
   *
   * @assertion_ids: JSONB:JAVADOC:34; JSONB:JAVADOC:35
   *
   * @test_Strategy: Assert that JsonbConfig.getProperty returns the value of a
   * specific configuration property
   */
  @Test
  public void testGetProperty() {
    JsonbConfig jsonbConfig = new JsonbConfig().withFormatting(true)
        .withNullValues(true);
    Optional<Object> property = jsonbConfig.getProperty(JsonbConfig.FORMATTING);

    if (!property.isPresent() || !(boolean) property.get()) {
      fail(
          "Failed to get a configuration property using JsonbConfig.getProperty method.");
    }
  }

  /*
   * @testName: testGetUnsetProperty
   *
   * @assertion_ids: JSONB:JAVADOC:34; JSONB:JAVADOC:35
   *
   * @test_Strategy: Assert that JsonbConfig.getProperty returns the value of a
   * specific configuration property
   */
  @Test
  public void testGetUnsetProperty() {
    JsonbConfig jsonbConfig = new JsonbConfig().withFormatting(true)
        .withNullValues(true);
    Optional<Object> property = jsonbConfig.getProperty(JsonbConfig.ADAPTERS);

    if (property.isPresent()) {
      fail(
          "Failed to get Optional.empty for an unset configuration property using JsonbConfig.getProperty method.");
    }
  }

  /*
   * @testName: testSetProperty
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:36
   *
   * @test_Strategy: Assert that JsonbConfig.setProperty sets the value of a
   * specific configuration property
   */
  @Test
  public void testSetProperty() {
    JsonbConfig jsonbConfig = new JsonbConfig().setProperty(
        JsonbConfig.PROPERTY_NAMING_STRATEGY,
        PropertyNamingStrategy.UPPER_CAMEL_CASE_WITH_SPACES);

    if (!jsonbConfig.getProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY).get()
        .equals(PropertyNamingStrategy.UPPER_CAMEL_CASE_WITH_SPACES)) {
      fail(
          "Failed to set a property value using JsonbConfig.setProperty method.");
    }
  }

  /*
   * @testName: testWithAdapters
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:37
   *
   * @test_Strategy: Assert that JsonbConfig.withAdapters configures custom
   * mapping adapters
   */
  @Test
  public void testWithAdapters() {
    SimpleStringAdapter simpleStringAdapter = new SimpleStringAdapter();
    JsonbConfig jsonbConfig = new JsonbConfig()
        .withAdapters(simpleStringAdapter);

    Object adapters = jsonbConfig.getProperty(JsonbConfig.ADAPTERS).get();
    if (!JsonbAdapter[].class.isAssignableFrom(adapters.getClass())
        || simpleStringAdapter != ((JsonbAdapter[]) adapters)[0]) {
      fail(
          "Failed to configure a custom adapter using JsonbConfig.withAdapters method.");
    }
  }

  /*
   * @testName: testWithAdaptersMultipleCalls
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:37
   *
   * @test_Strategy: Assert that multiple JsonbConfig.withAdapters calls result
   * in a merge of adapter values configured
   */
  @Test
  public void testWithAdaptersMultipleCalls() {
    JsonbConfig jsonbConfig = new JsonbConfig()
        .withAdapters(new SimpleIntegerAdapter())
        .withAdapters(new SimpleStringAdapter());

    Object adapters = jsonbConfig.getProperty(JsonbConfig.ADAPTERS).get();
    if (!JsonbAdapter[].class.isAssignableFrom(adapters.getClass())) {
      fail(
          "Not expected JsobAdapter array but " + adapters.getClass());
    }

    if (((JsonbAdapter[]) adapters).length != 2) {
      fail(
          "Failed to configure multiple custom adapters using multiple JsonbConfig.withAdapters method calls.");
    }
  }

  /*
   * @testName: testWithMultipleAdapters
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:37
   *
   * @test_Strategy: Assert that multiple JsonbConfig.withAdapters calls result
   * in a merge of adapter values configured
   */
  @Test
  public void testWithMultipleAdapters() {
    JsonbConfig jsonbConfig = new JsonbConfig()
        .withAdapters(new SimpleIntegerAdapter(), new SimpleStringAdapter());

    Object adapters = jsonbConfig.getProperty(JsonbConfig.ADAPTERS).get();
    if (!JsonbAdapter[].class.isAssignableFrom(adapters.getClass())) {
      fail(
          "Not expected JsobAdapter array but " + adapters.getClass());
    }
    if (((JsonbAdapter[]) adapters).length != 2) {
      fail(
          "Failed to configure multiple custom adapters using multiple JsonbConfig.withAdapters method calls.");
    }
  }

  /*
   * @testName: testWithBinaryDataStrategy
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:38
   *
   * @test_Strategy: Assert that JsonbConfig.withBinaryDataStrategy configures
   * custom binary data strategy
   */
  @Test
  public void testWithBinaryDataStrategy() {
    JsonbConfig jsonbConfig = new JsonbConfig()
        .withBinaryDataStrategy(BinaryDataStrategy.BASE_64_URL);

    Optional<Object> property = jsonbConfig
        .getProperty(JsonbConfig.BINARY_DATA_STRATEGY);
    if (!property.isPresent()
        || !BinaryDataStrategy.BASE_64_URL.equals(property.get())) {
      fail(
          "Failed to configure a custom binary data strategy using JsonbConfig.withBinaryDataStrategy method.");
    }
  }

  /*
   * @testName: testWithDateFormat
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:39
   *
   * @test_Strategy: Assert that JsonbConfig.withDateFormat configures custom
   * date format
   */
  @Test
  public void testWithDateFormat() {
    JsonbConfig jsonbConfig = new JsonbConfig().withDateFormat("YYYYMMDD",
        Locale.GERMAN);

    Optional<Object> property = jsonbConfig
        .getProperty(JsonbConfig.DATE_FORMAT);
    if (!property.isPresent() || !"YYYYMMDD".equals(property.get())) {
      fail(
          "Failed to configure a custom date format using JsonbConfig.withDateFormat method.");
    }
  }

  /*
   * @testName: testWithDeserializers
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:40
   *
   * @test_Strategy: Assert that JsonbConfig.withDeserializers configures custom
   * deserializers
   */
  @Test
  public void testWithDeserializers() {
    SimpleContainerDeserializer deserializer = new SimpleContainerDeserializer();
    JsonbConfig jsonbConfig = new JsonbConfig().withDeserializers(deserializer);

    Object deserializers = jsonbConfig.getProperty(JsonbConfig.DESERIALIZERS)
        .get();
    if (!JsonbDeserializer[].class.isAssignableFrom(deserializers.getClass())
        || deserializer != ((JsonbDeserializer[]) deserializers)[0]) {
      fail(
          "Failed to configure a custom deserializer using JsonbConfig.withDeserializers method.");
    }
  }

  /*
   * @testName: testWithDeserializersMultipleCalls
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:40
   *
   * @test_Strategy: Assert that multiple JsonbConfig.withDeserializers calls
   * result in a merge of deserializer values configured
   */
  @Test
  public void testWithDeserializersMultipleCalls() {
    JsonbConfig jsonbConfig = new JsonbConfig()
        .withDeserializers(new SimpleIntegerDeserializer())
        .withDeserializers(new SimpleContainerDeserializer());

    Object deserializers = jsonbConfig.getProperty(JsonbConfig.DESERIALIZERS)
        .get();
    if (!JsonbDeserializer[].class.isAssignableFrom(deserializers.getClass())) {
      fail("Not expected JsonbDeserializer array but "
          + deserializers.getClass());
    }

    if (((JsonbDeserializer[]) deserializers).length != 2) {
      fail(
          "Failed to configure multiple custom deserializers using multiple JsonbConfig.withDeserializers method calls.");
    }
  }

  /*
   * @testName: testWithEncoding
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:41
   *
   * @test_Strategy: Assert that JsonbConfig.withEncoding configures custom
   * character encoding
   */
  @Test
  public void testWithEncoding() {
    JsonbConfig jsonbConfig = new JsonbConfig().withEncoding("UCS2");

    Optional<Object> property = jsonbConfig.getProperty(JsonbConfig.ENCODING);
    if (!property.isPresent() || !"UCS2".equals(property.get())) {
      fail(
          "Failed to configure a custom character encoding using JsonbConfig.withEncoding method.");
    }
  }

  /*
   * @testName: testWithFormatting
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:42
   *
   * @test_Strategy: Assert that JsonbConfig.withFormatting configures whether
   * JSON string formatting is enabled
   */
  @Test
  public void testWithFormatting() {
    JsonbConfig jsonbConfig = new JsonbConfig().withFormatting(true);

    Optional<Object> property = jsonbConfig.getProperty(JsonbConfig.FORMATTING);
    if (!property.isPresent() || !(boolean) property.get()) {
      fail(
          "Failed to configure JSON string formatting using JsonbConfig.withFormatting method.");
    }
  }

  /*
   * @testName: testWithLocale
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:43
   *
   * @test_Strategy: Assert that JsonbConfig.withLocale configures custom locale
   */
  @Test
  public void testWithLocale() {
    JsonbConfig jsonbConfig = new JsonbConfig().withLocale(Locale.GERMAN);

    Optional<Object> property = jsonbConfig.getProperty(JsonbConfig.LOCALE);
    if (!property.isPresent() || !Locale.GERMAN.equals(property.get())) {
      fail(
          "Failed to configure a custom locale using JsonbConfig.withLocale method.");
    }
  }

  /*
   * @testName: testWithNullValues
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:44
   *
   * @test_Strategy: Assert that JsonbConfig.withNullValues configures
   * serialization of null values
   */
  @Test
  public void testWithNullValues() {
    JsonbConfig jsonbConfig = new JsonbConfig().withNullValues(true);

    Optional<Object> property = jsonbConfig
        .getProperty(JsonbConfig.NULL_VALUES);
    if (!property.isPresent() || !(boolean) property.get()) {
      fail(
          "Failed to configure serialization of null values using JsonbConfig.withNullValues method.");
    }
  }

  /*
   * @testName: testWithPropertyNamingStrategy
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:45; JSONB:JAVADOC:69
   *
   * @test_Strategy: Assert that JsonbConfig.withPropertyNamingStrategy
   * configures custom property naming strategy
   */
  @Test
  public void testWithPropertyNamingStrategy() {
    SimplePropertyNamingStrategy propertyNamingStrategy = new SimplePropertyNamingStrategy();
    JsonbConfig jsonbConfig = new JsonbConfig()
        .withPropertyNamingStrategy(propertyNamingStrategy);

    Optional<Object> property = jsonbConfig
        .getProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY);
    if (!property.isPresent() || propertyNamingStrategy != property.get()) {
      fail(
          "Failed to configure a custom property naming strategy using JsonbConfig.withPropertyNamingStrategy method.");
    }
  }

  /*
   * @testName: testWithPropertyNamingStrategyString
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:46
   *
   * @test_Strategy: Assert that JsonbConfig.withPropertyNamingStrategy with
   * String argument configures custom property naming strategy
   */
  @Test
  public void testWithPropertyNamingStrategyString() {
    JsonbConfig jsonbConfig = new JsonbConfig()
        .withPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);

    Optional<Object> property = jsonbConfig
        .getProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY);
    if (!property.isPresent()
        || !PropertyNamingStrategy.UPPER_CAMEL_CASE.equals(property.get())) {
      fail(
          "Failed to configure a custom property naming strategy using JsonbConfig.withPropertyNamingStrategy method with String argument.");
    }
  }

  /*
   * @testName: testWithPropertyOrderStrategy
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:47
   *
   * @test_Strategy: Assert that JsonbConfig.withPropertyOrderStrategy
   * configures custom property order strategy
   */
  @Test
  public void testWithPropertyOrderStrategy() {
    JsonbConfig jsonbConfig = new JsonbConfig()
        .withPropertyOrderStrategy(PropertyOrderStrategy.LEXICOGRAPHICAL);

    Optional<Object> property = jsonbConfig
        .getProperty(JsonbConfig.PROPERTY_ORDER_STRATEGY);
    if (!property.isPresent()
        || !PropertyOrderStrategy.LEXICOGRAPHICAL.equals(property.get())) {
      fail(
          "Failed to configure a custom property order strategy using JsonbConfig.withPropertyOrderStrategy method.");
    }
  }

  /*
   * @testName: testWithPropertyVisibilityStrategy
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:48
   *
   * @test_Strategy: Assert that JsonbConfig.withPropertyVisibilityStrategy
   * configures custom property visibility
   */
  @Test
  public void testWithPropertyVisibilityStrategy() {
    SimplePropertyVisibilityStrategy propertyVisibilityStrategy = new SimplePropertyVisibilityStrategy();
    JsonbConfig jsonbConfig = new JsonbConfig()
        .withPropertyVisibilityStrategy(propertyVisibilityStrategy);

    Optional<Object> property = jsonbConfig
        .getProperty(JsonbConfig.PROPERTY_VISIBILITY_STRATEGY);
    if (!property.isPresent() || propertyVisibilityStrategy != property.get()) {
      fail(
          "Failed to configure a custom property visibility strategy using JsonbConfig.withPropertyVisibilityStrategy method.");
    }
  }

  /*
   * @testName: testWithSerializers
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:49
   *
   * @test_Strategy: Assert that JsonbConfig.withSerializers configures custom
   * serializers
   */
  @Test
  public void testWithSerializers() {
    SimpleContainerSerializer serializer = new SimpleContainerSerializer();
    JsonbConfig jsonbConfig = new JsonbConfig().withSerializers(serializer);

    Object serializers = jsonbConfig.getProperty(JsonbConfig.SERIALIZERS).get();
    if (!JsonbSerializer[].class.isAssignableFrom(serializers.getClass())
        || serializer != ((JsonbSerializer[]) serializers)[0]) {
      fail(
          "Failed to configure a custom serializer using JsonbConfig.withSerializers method.");
    }
  }

  /*
   * @testName: testWithSerializersMultipleCalls
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:49
   *
   * @test_Strategy: Assert that multiple JsonbConfig.withSerializers calls
   * result in a merge of serializer values configured
   */
  @Test
  public void testWithSerializersMultipleCalls() {
    JsonbConfig jsonbConfig = new JsonbConfig()
        .withSerializers(new SimpleIntegerSerializer())
        .withSerializers(new SimpleContainerSerializer());

    Object serializers = jsonbConfig.getProperty(JsonbConfig.SERIALIZERS).get();
    if (!JsonbSerializer[].class.isAssignableFrom(serializers.getClass())) {
      fail(
          "Not expected JsonbSerializer array but " + serializers.getClass());
    }

    if (((JsonbSerializer[]) serializers).length != 2) {
      fail(
          "Failed to configure multiple custom serializers using multiple JsonbConfig.withSerializers method calls.");
    }
  }

  /*
   * @testName: testWithStrictIJson
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:50
   *
   * @test_Strategy: Assert that JsonbConfig.withStrictIJSON configures strict
   * I-JSON support
   */
  @Test
  public void testWithStrictIJson() {
    JsonbConfig jsonbConfig = new JsonbConfig().withStrictIJSON(true);

    Optional<Object> property = jsonbConfig
        .getProperty(JsonbConfig.STRICT_IJSON);
    if (!property.isPresent() || !(boolean) property.get()) {
      fail(
          "Failed to configure strict I-JSON support using JsonbConfig.withStrictIJSON method.");
    }
  }
}
