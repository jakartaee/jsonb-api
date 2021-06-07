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

package jakarta.json.bind.tck.customizedmapping.ijson;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Pattern;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.JsonbException;
import jakarta.json.bind.tck.customizedmapping.ijson.model.BinaryDataContainer;
import jakarta.json.bind.tck.customizedmapping.ijson.model.CalendarContainer;
import jakarta.json.bind.tck.customizedmapping.ijson.model.DateContainer;
import jakarta.json.bind.tck.customizedmapping.ijson.model.DurationContainer;
import jakarta.json.bind.tck.customizedmapping.ijson.model.GregorianCalendarContainer;
import jakarta.json.bind.tck.customizedmapping.ijson.model.InstantContainer;
import jakarta.json.bind.tck.customizedmapping.ijson.model.LocalDateContainer;
import jakarta.json.bind.tck.customizedmapping.ijson.model.LocalDateTimeContainer;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @test
 * @sources IJsonSupportTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.ijson.IJsonSupportTest
 **/
public class IJsonSupportTest {

  private final Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withStrictIJSON(true));

  /*
   * @testName: testStrictNonObjectOrArrayTopLevel
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.4-1
   *
   * @test_Strategy: Assert that top level JSON texts that are neither objects
   * or arrays are restricted during marshalling if JsonbConfig.withStrictIJSON
   * is used
   */
  @Test
  public void testStrictNonObjectOrArrayTopLevel() {
    assertThrows(JsonbException.class,
                 () -> jsonb.toJson("Test String"),
                 "Failed to restrict serialization of top-level JSON texts that are neither objects "
                         + "nor arrays when JsonbConfig.withStrictIJSON is used.");
  }

  /*
   * @testName: testStrictBinaryDataEncoding
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.4-1
   *
   * @test_Strategy: Assert that binary data is correctly encoded using
   * BASE_64_URL binary data encoding if JsonbConfig.withStrictIJSON is used
   */
  @Test
  public void testStrictBinaryDataEncoding() {
    String jsonString = jsonb.toJson(new BinaryDataContainer());
    assertThat("Failed to correctly marshal binary data using BASE_64_URL binary data encoding when "
                       + "JsonbConfig.withStrictIJSON is used.",
               jsonString, matchesPattern("\\{\\s*\"data\"\\s*:\\s*\"VGVzdCBTdHJpbmc=\"\\s*}"));
  }

  /*
   * @testName: testStrictDate
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.4-1; JSONB:SPEC:JSB-4.4.1-1;
   * JSONB:SPEC:JSB-4.4.1-2; JSONB:SPEC:JSB-4.4.1-3
   *
   * @test_Strategy: Assert that java.util.Date is serialized in the same format
   * as java.time.ZonedDateTime if JsonbConfig.withStrictIJSON is used
   */
  @Test
  public void testStrictDate() {
    final Calendar instance = Calendar.getInstance();
    instance.clear();
    instance.set(1970, Calendar.JANUARY, 1);
    instance.setTimeZone(TimeZone.getTimeZone("UTC"));
    String jsonString = jsonb.toJson(new DateContainer() {
      {
        setInstance(instance.getTime());
      }
    });
    assertThat("Failed to serialize java.util.Date in the same format as java.time.ZonedDateTime when "
                       + "JsonbConfig.withStrictIJSON is used.",
               jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\"1970-01-01T00:00:00Z\\+00:00\"\\s*}"));
  }

  /*
   * @testName: testStrictCalendar
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.4-1; JSONB:SPEC:JSB-4.4.1-1;
   * JSONB:SPEC:JSB-4.4.1-2; JSONB:SPEC:JSB-4.4.1-3
   *
   * @test_Strategy: Assert that java.util.Calendar is serialized in the same
   * format as java.time.ZonedDateTime if JsonbConfig.withStrictIJSON is used
   */
  @Test
  public void testStrictCalendar() {
    Calendar calendarProperty = Calendar.getInstance();
    calendarProperty.clear();
    calendarProperty.set(1970, Calendar.JANUARY, 1);
    calendarProperty.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

    String jsonString = jsonb.toJson(new CalendarContainer() {
      {
        setInstance(calendarProperty);
      }
    });
    assertThat("Failed to serialize java.util.Calendar in the same format as java.time.ZonedDateTime when "
                       + "JsonbConfig.withStrictIJSON is used.",
               jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\"1970-01-01T00:00:00Z\\+01:00\"\\s*}"));
  }

  /*
   * @testName: testStrictGregorianCalendar
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.4-1; JSONB:SPEC:JSB-4.4.1-1;
   * JSONB:SPEC:JSB-4.4.1-2; JSONB:SPEC:JSB-4.4.1-3
   *
   * @test_Strategy: Assert that java.util.GregorianCalendar is serialized in
   * the same format as java.time.ZonedDateTime if JsonbConfig.withStrictIJSON
   * is used
   */
  @Test
  public void testStrictGregorianCalendar() {
    GregorianCalendar gregorianCalendar = new GregorianCalendar();
    gregorianCalendar.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
    gregorianCalendar.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

    String jsonString = jsonb.toJson(new GregorianCalendarContainer() {
      {
        setInstance(gregorianCalendar);
      }
    });
    assertThat("Failed to serialize java.util.GregorianCalendar in the same format as java.time.ZonedDateTime when "
                       + "JsonbConfig.withStrictIJSON is used.",
               jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\"1970-01-01T00:00:00Z\\+01:00\"\\s*}"));
  }

  /*
   * @testName: testStrictLocalDate
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.4-1; JSONB:SPEC:JSB-4.4.1-1;
   * JSONB:SPEC:JSB-4.4.1-2; JSONB:SPEC:JSB-4.4.1-3
   *
   * @test_Strategy: Assert that java.time.LocalDate is serialized in the same
   * format as java.time.ZonedDateTime if JsonbConfig.withStrictIJSON is used
   */
  @Test
  public void testStrictLocalDate() {
    String jsonString = jsonb.toJson(new LocalDateContainer() {
      {
        setInstance(LocalDate.of(1970, 1, 1));
      }
    });
    assertThat( "Failed to serialize java.time.LocalDate in the same format as java.time.ZonedDateTime when "
                        + "JsonbConfig.withStrictIJSON is used.",
               jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\"1970-01-01T00:00:00Z\\+00:00\"\\s*}"));
  }

  /*
   * @testName: testStrictLocalDateTime
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.4-1; JSONB:SPEC:JSB-4.4.1-1;
   * JSONB:SPEC:JSB-4.4.1-2; JSONB:SPEC:JSB-4.4.1-3
   *
   * @test_Strategy: Assert that java.time.LocalDate is serialized in the same
   * format as java.time.ZonedDateTime if JsonbConfig.withStrictIJSON is used
   */
  @Test
  public void testStrictLocalDateTime() {
    String jsonString = jsonb.toJson(new LocalDateTimeContainer() {
      {
        setInstance(LocalDateTime.of(1970, 1, 1, 1, 1, 1));
      }
    });
    assertThat( "Failed to serialize java.time.LocalDate in the same format as java.time.ZonedDateTime when "
                        + "JsonbConfig.withStrictIJSON is used.",
                jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\"1970-01-01T01:01:01Z\\+00:00\"\\s*}"));
  }

  /*
   * @testName: testStrictInstant
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.4-1; JSONB:SPEC:JSB-4.4.1-1;
   * JSONB:SPEC:JSB-4.4.1-2; JSONB:SPEC:JSB-4.4.1-3
   *
   * @test_Strategy: Assert that java.time.Instant is serialized in the same
   * format as java.time.ZonedDateTime if JsonbConfig.withStrictIJSON is used
   */
  @Test
  public void testStrictInstant() {
    String jsonString = jsonb.toJson(new InstantContainer() {
      {
        setInstance(Instant.ofEpochMilli(0));
      }
    });
    assertThat( "Failed to serialize java.time.Instant in the same format as java.time.ZonedDateTime when "
                        + "JsonbConfig.withStrictIJSON is used.",
                jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\"1970-01-01T00:00:00Z\\+00:00\"\\s*}"));
  }

  /*
   * @testName: testStrictDuration
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.4-1; JSONB:SPEC:JSB-4.4.1-1;
   * JSONB:SPEC:JSB-4.4.1-4
   *
   * @test_Strategy: Assert that java.time.Duration is serialized in the same
   * format as in Appendix A of RFC 3339
   */
  @Test
  public void testStrictDuration() {
    String jsonString = jsonb.toJson(new DurationContainer() {
      {
        setInstance(Duration.ofDays(1).plus(Duration.ofHours(1))
            .plus(Duration.ofSeconds(1)));
      }
    });
    assertThat( "Failed to serialize java.time.Duration in the same format as in Appendix A of RFC 3339.",
                jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\"PT25H1S\"\\s*}"));
  }
}
