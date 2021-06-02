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

/*
 * $Id$
 */

package jakarta.json.bind.tck.api.config;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.adapter.JsonbAdapter;
import jakarta.json.bind.config.BinaryDataStrategy;
import jakarta.json.bind.config.PropertyNamingStrategy;
import jakarta.json.bind.config.PropertyOrderStrategy;
import jakarta.json.bind.serializer.JsonbDeserializer;
import jakarta.json.bind.serializer.JsonbSerializer;
import jakarta.json.bind.tck.api.model.SimpleContainerDeserializer;
import jakarta.json.bind.tck.api.model.SimpleContainerSerializer;
import jakarta.json.bind.tck.api.model.SimpleIntegerAdapter;
import jakarta.json.bind.tck.api.model.SimpleIntegerDeserializer;
import jakarta.json.bind.tck.api.model.SimpleIntegerSerializer;
import jakarta.json.bind.tck.api.model.SimplePropertyNamingStrategy;
import jakarta.json.bind.tck.api.model.SimplePropertyVisibilityStrategy;
import jakarta.json.bind.tck.api.model.SimpleStringAdapter;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    String validationMessage = "Failed to get configuration properties as a map using JsonbConfig.getAsMap method.";
    assertThat(validationMessage, configMap.size(), is(2));
    assertThat(validationMessage, configMap, hasKey(JsonbConfig.FORMATTING));
    assertThat(validationMessage, configMap, hasKey(JsonbConfig.NULL_VALUES));

    assertThrows(UnsupportedOperationException.class,
                 () -> configMap.put(JsonbConfig.BINARY_DATA_STRATEGY, BinaryDataStrategy.BASE_64),
                 "Failed to get configuration properties as an unmodifiable map using JsonbConfig.getAsMap method.");

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

    String validationMessage = "Failed to get a configuration property using JsonbConfig.getProperty method.";
    assertTrue(property.isPresent(), validationMessage);
    assertTrue((Boolean) property.get(), validationMessage);
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

    assertTrue(property.isPresent(),
               "Failed to get Optional.empty for an unset configuration property using JsonbConfig.getProperty method.");
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
    JsonbConfig jsonbConfig = new JsonbConfig().setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY,
                                                            PropertyNamingStrategy.UPPER_CAMEL_CASE_WITH_SPACES);
    assertThat("Failed to set a property value using JsonbConfig.setProperty method.",
               jsonbConfig.getProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY).get(),
               is(PropertyNamingStrategy.UPPER_CAMEL_CASE_WITH_SPACES));
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
    String validationMessage = "Failed to configure a custom adapter using JsonbConfig.withAdapters method.";
    Object adapters = jsonbConfig.getProperty(JsonbConfig.ADAPTERS).get();
    assertThat(validationMessage, adapters, instanceOf(JsonbAdapter[].class));
    assertThat(validationMessage, ((JsonbAdapter<?, ?>[]) adapters)[0], is(simpleStringAdapter));
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
    assertThat("Not expected JsonbAdapter array but " + adapters.getClass(), adapters, instanceOf(JsonbAdapter[].class));
    assertThat("Failed to configure multiple custom adapters using multiple JsonbConfig.withAdapters method calls.",
               ((JsonbAdapter<?,?>[]) adapters).length, is(2));
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
    JsonbConfig jsonbConfig = new JsonbConfig().withAdapters(new SimpleIntegerAdapter(), new SimpleStringAdapter());

    Object adapters = jsonbConfig.getProperty(JsonbConfig.ADAPTERS).get();
    assertThat("Not expected JsonbAdapter array but " + adapters.getClass(), adapters, instanceOf(JsonbAdapter[].class));
    assertThat("Failed to configure multiple custom adapters using multiple JsonbConfig.withAdapters method calls.",
               ((JsonbAdapter<?,?>[]) adapters).length, is(2));
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
    JsonbConfig jsonbConfig = new JsonbConfig().withBinaryDataStrategy(BinaryDataStrategy.BASE_64_URL);

    Optional<Object> property = jsonbConfig.getProperty(JsonbConfig.BINARY_DATA_STRATEGY);
    String validationMessage = "Failed to configure a custom binary data strategy using JsonbConfig.withBinaryDataStrategy method.";
    assertTrue(property.isPresent(), validationMessage);
    assertThat(validationMessage, property.get(), is(BinaryDataStrategy.BASE_64_URL));
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
    JsonbConfig jsonbConfig = new JsonbConfig().withDateFormat("YYYYMMDD", Locale.GERMAN);

    Optional<Object> property = jsonbConfig.getProperty(JsonbConfig.DATE_FORMAT);
    String validationMessage = "Failed to configure a custom date format using JsonbConfig.withDateFormat method.";
    assertTrue(property.isPresent(), validationMessage);
    assertThat(validationMessage, property.get(), is("YYYYMMDD"));
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

    Object deserializers = jsonbConfig.getProperty(JsonbConfig.DESERIALIZERS).get();
    String validationMessage = "Failed to configure a custom deserializer using JsonbConfig.withDeserializers method.";
    assertThat(validationMessage, deserializers, instanceOf(JsonbDeserializer[].class));
    assertThat(validationMessage, ((JsonbDeserializer<?>[]) deserializers)[0], is(deserializer));
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

    Object deserializers = jsonbConfig.getProperty(JsonbConfig.DESERIALIZERS).get();
    assertThat("Not expected JsonbDeserializer array but " + deserializers.getClass(),
               deserializers, instanceOf(JsonbDeserializer[].class));
    assertThat("Failed to configure multiple custom deserializers using multiple JsonbConfig.withDeserializers method calls.",
               ((JsonbDeserializer<?>[]) deserializers).length, is(2));
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
    String validationMessage = "Failed to configure a custom character encoding using JsonbConfig.withEncoding method.";
    assertTrue(property.isPresent(), validationMessage);
    assertThat(validationMessage, property.get(), is("UCS2"));
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
    String validationMessage = "Failed to configure JSON string formatting using JsonbConfig.withFormatting method.";
    assertTrue(property.isPresent(), validationMessage);
    assertTrue((boolean) property.get(), validationMessage);
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
    String validationMessage = "Failed to configure a custom locale using JsonbConfig.withLocale method.";
    assertTrue(property.isPresent(), validationMessage);
    assertThat(validationMessage, property.get(), is(Locale.GERMAN));
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

    Optional<Object> property = jsonbConfig.getProperty(JsonbConfig.NULL_VALUES);
    String validationMessage = "Failed to configure serialization of null values using JsonbConfig.withNullValues method.";
    assertTrue(property.isPresent(), validationMessage);
    assertTrue((boolean) property.get(), validationMessage);
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
    JsonbConfig jsonbConfig = new JsonbConfig().withPropertyNamingStrategy(propertyNamingStrategy);

    Optional<Object> property = jsonbConfig.getProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY);
    String validationMessage = "Failed to configure a custom property naming strategy using "
            + "JsonbConfig.withPropertyNamingStrategy method.";
    assertTrue(property.isPresent(), validationMessage);
    assertThat(validationMessage, property.get(), is(propertyNamingStrategy));
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
    JsonbConfig jsonbConfig = new JsonbConfig().withPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);

    Optional<Object> property = jsonbConfig.getProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY);
    String validationMessage = "Failed to configure a custom property naming strategy using "
            + "JsonbConfig.withPropertyNamingStrategy method with String argument.";
    assertTrue(property.isPresent(), validationMessage);
    assertThat(validationMessage, property.get(), is(PropertyNamingStrategy.UPPER_CAMEL_CASE));
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
    JsonbConfig jsonbConfig = new JsonbConfig().withPropertyOrderStrategy(PropertyOrderStrategy.LEXICOGRAPHICAL);

    Optional<Object> property = jsonbConfig.getProperty(JsonbConfig.PROPERTY_ORDER_STRATEGY);
    String validationMessage = "Failed to configure a custom property order strategy using "
            + "JsonbConfig.withPropertyOrderStrategy method.";
    assertTrue(property.isPresent(), validationMessage);
    assertThat(validationMessage, property.get(), is(PropertyOrderStrategy.LEXICOGRAPHICAL));
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
    JsonbConfig jsonbConfig = new JsonbConfig().withPropertyVisibilityStrategy(propertyVisibilityStrategy);

    Optional<Object> property = jsonbConfig.getProperty(JsonbConfig.PROPERTY_VISIBILITY_STRATEGY);
    String validationMessage = "Failed to configure a custom property visibility strategy using "
            + "JsonbConfig.withPropertyVisibilityStrategy method.";
    assertTrue(property.isPresent(), validationMessage);
    assertThat(validationMessage, property.get(), is(JsonbConfig.PROPERTY_VISIBILITY_STRATEGY));
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
    String validationMessage = "Failed to configure a custom serializer using JsonbConfig.withSerializers method.";
    assertThat(validationMessage, serializers, instanceOf(JsonbSerializer[].class));
    assertThat(validationMessage, ((JsonbSerializer<?>[]) serializers)[0], is(serializer));
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
    assertThat("Not expected JsonbSerializer array but " + serializers.getClass(),
               serializers, instanceOf(JsonbSerializer[].class));
    assertThat("Failed to configure multiple custom serializers using multiple JsonbConfig.withSerializers method calls.",
               ((JsonbSerializer<?>[]) serializers).length, is(2));
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

    Optional<Object> property = jsonbConfig.getProperty(JsonbConfig.STRICT_IJSON);
    String validationMessage = "Failed to configure strict I-JSON support using JsonbConfig.withStrictIJSON method.";
    assertTrue(property.isPresent(), validationMessage);
    assertTrue((boolean) property.get(), validationMessage);
  }

}
