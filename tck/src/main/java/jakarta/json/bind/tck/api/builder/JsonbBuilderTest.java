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

package jakarta.json.bind.tck.api.builder;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.spi.JsonbProvider;
import jakarta.json.bind.tck.api.model.SimpleContainer;
import jakarta.json.spi.JsonProvider;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @test
 * @sources JsonbBuilderTest.java
 * @executeClass com.sun.ts.tests.jsonb.api.JsonbBuilderTest
 **/
public class JsonbBuilderTest {

  /*
   * @testName: testBuild
   *
   * @assertion_ids: JSONB:JAVADOC:25
   *
   * @test_Strategy: Assert that JsonbBuilder.build returns a new instance of
   * jakarta.json.bind.Jsonb
   */
  @Test
  public void testBuild() {
    Jsonb jsonb = JsonbBuilder.newBuilder().build();
    assertThat("Failed to create a new Jsonb instance using JsonBuilder.build method.", jsonb, notNullValue());
  }

  /*
   * @testName: testCreate
   *
   * @assertion_ids: JSONB:JAVADOC:26
   *
   * @test_Strategy: Assert that JsonbBuilder.create returns a new instance of
   * jakarta.json.bind.Jsonb
   */
  @Test
  public void testCreate() {
    Jsonb jsonb = JsonbBuilder.create();
    assertThat("Failed to create a new Jsonb instance using JsonBuilder.create method.", jsonb, notNullValue());
  }

  /*
   * @testName: testCreateConfig
   *
   * @assertion_ids: JSONB:JAVADOC:27
   *
   * @test_Strategy: Assert that JsonbBuilder.create method with Config argument
   * returns a new instance of jakarta.json.bind.Jsonb configured with provided
   * configuration
   */
  @Test
  public void testCreateConfig() {
    JsonbConfig jsonbConfig = new JsonbConfig().withNullValues(true);
    Jsonb jsonb = JsonbBuilder.create(jsonbConfig);

    String jsonString = jsonb.toJson(new SimpleContainer() {
      {
        setInstance(null);
      }
    });
    assertThat("Failed to create a new Jsonb instance using JsonBuilder.create method "
                       + "with Config argument configured with provided configuration.",
               jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*null\\s*}"));
  }

  /*
   * @testName: testNewBuilder
   *
   * @assertion_ids: JSONB:JAVADOC:28; JSONB:JAVADOC:80
   *
   * @test_Strategy: Assert that JsonbBuilder.newBuilder returns a new
   * JsonbBuilder instance as returned by
   * jakarta.json.bind.spi.JsonbProvider#provider method
   */
  @Test
  public void testNewBuilder() {
    JsonbBuilder jsonbBuilder = JsonbBuilder.newBuilder();
    String validationMessage = "Failed to create a new Jsonb instance as returned by "
            + "jakarta.json.bind.spi.JsonbProvider#provider method using JsonBuilder.newBuilder method.";
    assertThat(validationMessage, jsonbBuilder, notNullValue());
    assertThat(validationMessage, jsonbBuilder.getClass(), is(JsonbProvider.provider().create().getClass()));
  }

  /*
   * @testName: testNewBuilderString
   *
   * @assertion_ids: JSONB:JAVADOC:29; JSONB:JAVADOC:80; JSONB:JAVADOC:81
   *
   * @test_Strategy: Assert that JsonbBuilder.newBuilder method with String
   * argument returns a new JsonbBuilder instance as returned by
   * jakarta.json.bind.spi.JsonbProvider#provider(String) method
   */
  @Test
  public void testNewBuilderString() {
    JsonbBuilder jsonbBuilder = JsonbBuilder.newBuilder(JsonbProvider.provider().getClass().getName());
    String validationMessage = "Failed to create a new Jsonb instance as returned by "
            + "jakarta.json.bind.spi.JsonbProvider#provider(String) method using JsonBuilder.newBuilder method with String argument.";
    assertThat(validationMessage, jsonbBuilder, notNullValue());

    Class<?> clazz = JsonbProvider.provider(JsonbProvider.provider().getClass().getName()).create().getClass();
    assertThat(validationMessage, jsonbBuilder.getClass(), is(clazz));
  }

  /*
   * @testName: testNewBuilderProvider
   *
   * @assertion_ids: JSONB:JAVADOC:30; JSONB:JAVADOC:78; JSONB:JAVADOC:80
   *
   * @test_Strategy: Assert that JsonbBuilder.newBuilder method with
   * JsonbProvider argument returns a new JsonbBuilder instance as returned by
   * provider#create method
   */
  @Test
  public void testNewBuilderProvider() {
    JsonbBuilder jsonbBuilder = JsonbBuilder.newBuilder(JsonbProvider.provider());

    String validationMessage = "Failed to create a new Jsonb instance as returned by provider#create "
            + "method using JsonBuilder.newBuilder method with JsonbProvider argument.";
    assertThat(validationMessage, jsonbBuilder, notNullValue());
    assertThat(validationMessage, jsonbBuilder.getClass(), is(JsonbProvider.provider().create().getClass()));
  }

  /*
   * @testName: testWithConfig
   *
   * @assertion_ids: JSONB:JAVADOC:31
   *
   * @test_Strategy: Assert that JsonbBuilder.withConfig method with JsonbConfig
   * argument returns a new JsonbBuilder instance configured with configuration
   * passed as an argument
   */
  @Test
  public void testWithConfig() {
    JsonbConfig jsonbConfig = new JsonbConfig().withNullValues(true);
    Jsonb jsonb = JsonbBuilder.newBuilder().withConfig(jsonbConfig).build();

    String jsonString = jsonb.toJson(new SimpleContainer() {
      {
        setInstance(null);
      }
    });
    assertThat("Failed to apply configuration passed as an argument to JsonbBuilder.withConfig method.",
               jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*null\\s*}"));
  }

  /*
   * @testName: testWithProvider
   *
   * @assertion_ids: JSONB:JAVADOC:32; JSONB:JAVADOC:80
   *
   * @test_Strategy: Assert that JsonbBuilder.withProvider method with
   * JsonProvider argument returns a new JsonbBuilder instance using the
   * JsonProvider passed as an argument
   */
  @Test
  public void testWithProvider() {
    JsonbBuilder jsonbBuilder = JsonbBuilder.newBuilder().withProvider(JsonProvider.provider());
    assertThat("Failed to create a new JsonbBuilder instance using a specific "
                       + "JsonProvider using JsonbBuilder.withProvider method.",
               jsonbBuilder, notNullValue());
  }
}
