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

package ee.jakarta.tck.json.bind.defaultmapping.dates;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.function.BiPredicate;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;

import ee.jakarta.tck.json.bind.MappingTester;
import ee.jakarta.tck.json.bind.defaultmapping.dates.model.CalendarContainer;
import ee.jakarta.tck.json.bind.defaultmapping.dates.model.DateContainer;
import ee.jakarta.tck.json.bind.defaultmapping.dates.model.DurationContainer;
import ee.jakarta.tck.json.bind.defaultmapping.dates.model.GregorianCalendarContainer;
import ee.jakarta.tck.json.bind.defaultmapping.dates.model.InstantContainer;
import ee.jakarta.tck.json.bind.defaultmapping.dates.model.LocalDateContainer;
import ee.jakarta.tck.json.bind.defaultmapping.dates.model.LocalDateTimeContainer;
import ee.jakarta.tck.json.bind.defaultmapping.dates.model.LocalTimeContainer;
import ee.jakarta.tck.json.bind.defaultmapping.dates.model.OffsetDateTimeContainer;
import ee.jakarta.tck.json.bind.defaultmapping.dates.model.OffsetTimeContainer;
import ee.jakarta.tck.json.bind.defaultmapping.dates.model.PeriodContainer;
import ee.jakarta.tck.json.bind.defaultmapping.dates.model.SimpleTimeZoneContainer;
import ee.jakarta.tck.json.bind.defaultmapping.dates.model.TimeZoneContainer;
import ee.jakarta.tck.json.bind.defaultmapping.dates.model.ZoneIdContainer;
import ee.jakarta.tck.json.bind.defaultmapping.dates.model.ZoneOffsetContainer;
import ee.jakarta.tck.json.bind.defaultmapping.dates.model.ZonedDateTimeContainer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @test
 * @sources DatesMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.dates.DatesMappingTest
 **/
public class DatesMappingTest {

    private static final String OFFSET_HOURS = getHoursFromUTCRegExp(new GregorianCalendar(1970, 0, 1));

    private <T extends TimeZone> BiPredicate<T, T> timezoneTest(final boolean canBeSaving) {
        return (a, b) -> {
            // Depending on when the timezone is created, it can differ by one hour
            // when serialized/deserialized
            long diff = Math.abs(a.getRawOffset() - b.getRawOffset());
            return a.useDaylightTime() && canBeSaving
                    ? diff == 0 || diff == a.getDSTSavings()
                    : diff == 0;
        };
    }

    /*
     * @testName: testDate
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.5-1;
     *
     * @test_Strategy: Assert that serializing and deserializing Date result in
     * the same value;
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testDate() {
        Date date = new Date(70, Calendar.JANUARY, 1);
        Jsonb jsonb = JsonbBuilder.create();
        String json = jsonb.toJson(jsonb.fromJson(jsonb.toJson(date), Date.class));
        Date mixin = jsonb.fromJson(json, Date.class);
        assertThat("Serializing and deserializing Date results in different value", mixin, is(date));
    }

    /*
     * @testName: testDateNoTimeMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5-3;
     * JSONB:SPEC:JSB-3.5.1-1; JSONB:SPEC:JSB-3.5.1-3
     *
     * @test_Strategy: Assert that Date with no time is still marshalled as and
     * unmarshalled from ISO_DATE_TIME
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testDateNoTimeMapping() {
        // Date takes time zone from the default timezone which is set by user
        // environment
        Date date = new Date(70, Calendar.JANUARY, 1);

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE_TIME;
        String toMatch = dtf.format(calendar.toZonedDateTime())
                .replace("]", "\\]")
                .replace("[", "\\[")
                .replace("+", "\\+");
        new MappingTester<>(DateContainer.class) //
                .setMarshallExpectedRegExp("\"" + toMatch + "\"")
                .setUnmarshallTestPredicate((a, b) -> {
                    Calendar orig = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
                    orig.clear();
                    orig.set(1970, Calendar.JANUARY, 1);
                    return b.equals(orig.getTime());
                }).test(date, "\"1970-01-01T00:00:00\"");
    }

    /*
     * @testName: testCalendarNoTimeMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.1-1;
     * JSONB:SPEC:JSB-3.5.1-3
     *
     * @test_Strategy: Assert that Calendar with no time is marshalled as and
     * unmarshalled from ISO_DATE
     */
    @Test
    public void testCalendarNoTimeMapping() {
        Calendar calendarProperty = Calendar.getInstance();
        calendarProperty.clear();
        new MappingTester<>(CalendarContainer.class)//
                .setMarshallExpectedRegExp("\"1970-01-01" + OFFSET_HOURS + "\"") //
                .setUnmarshallTestPredicate((a, b) -> {
                    Calendar orig = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
                    orig.clear();
                    orig.set(1970, Calendar.JANUARY, 1);
                    return b.getTime().equals(orig.getTime())
                            && getHoursFromUTC(orig).equals(getHoursFromUTC(b));
                }).test(calendarProperty, "\"1970-01-01\"");
    }

    /*
     * @testName: testGregorianCalendarNoTimeMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5-2;
     * JSONB:SPEC:JSB-3.5.1-1; JSONB:SPEC:JSB-3.5.1-3
     *
     * @test_Strategy: Assert that GregorianCalendar with no time is marshalled as
     * and unmarshalled from ISO_DATE
     */
    @Test
    public void testGregorianCalendarNoTimeMapping() {
        GregorianCalendar calendar = new GregorianCalendar(1970, Calendar.JANUARY, 1);
        for (int i = Calendar.DATE + 1; i != Calendar.MILLISECOND + 1; i++) {
            calendar.clear(i);
        }
        DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE;
        String toMatch = "\"" + dtf.format(calendar.toZonedDateTime()).replace("+", "\\+") + "\"";
        new MappingTester<>(GregorianCalendarContainer.class) //
                .setMarshallExpectedRegExp(toMatch) //
                .setUnmarshallTestPredicate((a, b) -> {
                    Calendar orig = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
                    orig.clear();
                    orig.set(1970, Calendar.JANUARY, 1);
                    return b.getTime().equals(orig.getTime())
                            && getHoursFromUTC(orig).equals(getHoursFromUTC(b));
                }).test(calendar, "\"1970-01-01\"");
    }

    /*
     * @testName: testDateWithTimeMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5-3;
     * JSONB:SPEC:JSB-3.5.1-2; JSONB:SPEC:JSB-3.5.1-3
     *
     * @test_Strategy: Assert that Date with time information is marshalled as and
     * unmarshalled from ISO_DATE_TIME
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testDateWithTimeMapping() {
        Date date = new Date(70, Calendar.JANUARY, 1, 0, 0, 0);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE_TIME;
        String toMatch = dtf.format(calendar.toZonedDateTime())
                .replace("]", "\\]")
                .replace("[", "\\[");
        new MappingTester<>(DateContainer.class) //
                .setMarshallExpectedRegExp("\"" + toMatch + "\"")
                .setUnmarshallTestPredicate((a, b) -> {
                    Calendar orig = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
                    orig.clear();
                    orig.set(1970, Calendar.JANUARY, 1);
                    return b.equals(orig.getTime());
                }).test(date, "\"1970-01-01T00:00:00\"");
    }

    /*
     * @testName: testCalendarWithTimeMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.1-2;
     * JSONB:SPEC:JSB-3.5.1-3
     *
     * @test_Strategy: Assert that Calendar with time information is marshalled as
     * and unmarshalled from ISO_DATE_TIME
     */
    @Test
    public void testCalendarWithTimeMapping() {
        Calendar calendarProperty = Calendar.getInstance();
        calendarProperty.set(1970, Calendar.JANUARY, 1, 1, 0, 0);
        calendarProperty.set(Calendar.MILLISECOND, 0);
        calendarProperty.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        new MappingTester<>(CalendarContainer.class)
                .setMarshallExpectedRegExp(
                        "\"1970-01-01T01:00:00(\\.\\d{1,3})?\\+01:00\\[Europe/Paris\\]\"")
                .setUnmarshallTestPredicate((a, b) -> {
                    return a.getTime().equals(b.getTime())
                            && getHoursFromUTC(calendarProperty).equals(getHoursFromUTC(b));
                }).test(calendarProperty,
                        "\"1970-01-01T01:00:00.00+01:00[Europe/Paris]\"");
    }

    /*
     * @testName: testGregorianCalendarWithTimeMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5-2;
     * JSONB:SPEC:JSB-3.5.1-2; JSONB:SPEC:JSB-3.5.1-3
     *
     * @test_Strategy: Assert that GregorianCalendar with time information is
     * marshalled as and unmarshalled from ISO_DATE_TIME
     */
    @Test
    @Disabled("See: https://github.com/eclipse-ee4j/jakartaee-tck/issues/102")
    public void testGregorianCalendarWithTimeMapping() {
        GregorianCalendar calendar = GregorianCalendar.from(
                ZonedDateTime.of(LocalDateTime.of(1970, Month.FEBRUARY, 1, 1, 0, 0),
                                 ZoneId.of("GMT")));
        new MappingTester<>(GregorianCalendarContainer.class).test(calendar,
                                                                   "\"1970-01-01T01:00:00Z[GMT]\"");
    }

    /*
     * @testName: testShortTimeZoneMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.2-1;
     * JSONB:SPEC:JSB-3.5.2-3
     *
     * @test_Strategy: Assert that java.util.TimeZone is correctly handled
     */
    @Test
    public void testShortTimeZoneMapping() {
        new MappingTester<>(TimeZoneContainer.class) //
                .setUnmarshallTestPredicate(timezoneTest(false)) //
                .test(TimeZone.getTimeZone("GMT+10"), "\"GMT+10:00\"");
    }

    /*
     * @testName: testLongTimeZoneMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.2-1;
     * JSONB:SPEC:JSB-3.5.2-3
     *
     * @test_Strategy: Assert that java.util.TimeZone is correctly handled
     */
    @Test
    public void testLongTimeZoneMapping() {
        new MappingTester<>(TimeZoneContainer.class) //
                .setUnmarshallTestPredicate(timezoneTest(true)) //
                .test(TimeZone.getTimeZone("America/Los_Angeles"),
                      "\"America/Los_Angeles\"");
    }

    /*
     * @testName: testSimpleTimeZoneMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.2-1;
     * JSONB:SPEC:JSB-3.5.2-3
     *
     * @test_Strategy: Assert that java.util.SimpleTimeZone is correctly handled
     */
    @Test
    public void testSimpleTimeZoneMapping() {
        new MappingTester<>(SimpleTimeZoneContainer.class) //
                .setUnmarshallTestPredicate(timezoneTest(false)) //
                .test(new SimpleTimeZone(75 * 60 * 1000, "GMT+01:15"), "\"GMT+01:15\"");
    }

    /*
     * @testName: testInstantMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-1;
     * JSONB:SPEC:JSB-3.5.3-2; JSONB:SPEC:JSB-3.5.3-3
     *
     * @test_Strategy: Assert that java.time.Instant is correctly marshalled as
     * and unmarshalled from ISO_INSTANT
     */
    @Test
    public void testInstantMapping() {
        new MappingTester<>(InstantContainer.class)
                .test(Instant.ofEpochMilli(0), "\"1970-01-01T00:00:00Z\"");
    }

    /*
     * @testName: testDurationMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-8
     *
     * @test_Strategy: Assert that Duration is correctly marshalled as and
     * umarshalled from ISO 8601 seconds based representation
     */
    @Test
    public void testDurationMapping() {
        new MappingTester<>(DurationContainer.class)
                .test(Duration.ofHours(1), "\"PT1H\"");
    }

    /*
     * @testName: testDurationWithSecondsMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-8;
     * JSONB:SPEC:JSB-3.5.3-9
     *
     * @test_Strategy: Assert that Duration is correctly marshalled as and
     * umarshalled from ISO 8601 seconds based representation
     */
    @Test
    public void testDurationWithSecondsMapping() {
        new MappingTester<>(DurationContainer.class)
                .test(Duration.ofHours(1).plus(Duration.ofSeconds(1)), "\"PT1H1S\"");
    }

    /*
     * @testName: testPeriodMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-10;
     * JSONB:SPEC:JSB-3.5.3-11
     *
     * @test_Strategy: Assert that Period is correctly marshalled as and
     * umarshalled from ISO 8601 period representation
     */
    @Test
    public void testPeriodMapping() {
        new MappingTester<>(PeriodContainer.class).test(Period.of(1, Calendar.FEBRUARY, 1),
                                                        "\"P1Y1M1D\"");
    }

    /*
     * @testName: testZeroDaysPeriodMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-10;
     * JSONB:SPEC:JSB-3.5.3-11; JSONB:SPEC:JSB-3.5.3-12
     *
     * @test_Strategy: Assert that a zero length Period is correctly marshalled as
     * and umarshalled from "P0D"
     */
    @Test
    public void testZeroDaysPeriodMapping() {
        new MappingTester<>(PeriodContainer.class).test(Period.of(0, 0, 0),
                                                        "\"P0D\"");
    }

    /*
     * @testName: testLocalDateMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-3
     *
     * @test_Strategy: Assert that java.time.LocalDate is correctly marshalled as
     * and umarshalled from ISO_LOCAL_DATE
     */
    @Test
    public void testLocalDateMapping() {
        new MappingTester<>(LocalDateContainer.class)
                .test(LocalDate.of(2000, Calendar.FEBRUARY, 1), "\"2000-01-01\"");
    }

    /*
     * @testName: testLocalTimeMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-3
     *
     * @test_Strategy: Assert that java.time.LocalTime is correctly marshalled as
     * and umarshalled from ISO_LOCAL_TIME
     */
    @Test
    public void testLocalTimeMapping() {
        new MappingTester<>(LocalTimeContainer.class)
                .test(LocalTime.of(1, 1, 1), "\"01:01:01\"");
    }

    /*
     * @testName: testLocalDateTimeMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-3
     *
     * @test_Strategy: Assert that java.time.LocalDateTime is correctly marshalled
     * as and umarshalled from ISO_LOCAL_DATE_TIME
     */
    @Test
    public void testLocalDateTimeMapping() {
        new MappingTester<>(LocalDateTimeContainer.class)
                .test(LocalDateTime.of(2000, Calendar.FEBRUARY, 1, 1, 1, 1), "\"2000-01-01T01:01:01\"");
    }

    /*
     * @testName: testZonedDateTimeMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-3
     *
     * @test_Strategy: Assert that java.time.ZonedDateTime is correctly marshalled
     * as and umarshalled from ISO_ZONED_DATE_TIME
     */
    @Test
    public void testZonedDateTimeMapping() {
        new MappingTester<>(ZonedDateTimeContainer.class).test(
                ZonedDateTime.of(2000, Calendar.FEBRUARY, 1, 1, 1, 1, 0,
                                 ZoneId.of("Europe/Paris")),
                "\"2000-01-01T01:01:01+01:00[Europe/Paris]\"");
    }

    /*
     * @testName: testZoneIdMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-4;
     * JSONB:SPEC:JSB-3.5.3-5
     *
     * @test_Strategy: Assert that java.time.ZoneId is correctly handled
     */
    @Test
    public void testZoneIdMapping() {
        new MappingTester<>(ZoneIdContainer.class).test(ZoneId.of("UTC"),
                                                        "\"UTC\"");
    }

    /*
     * @testName: testZoneOffsetMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-6;
     * JSONB:SPEC:JSB-3.5.3-7
     *
     * @test_Strategy: Assert that java.time.ZoneOffset is correctly handled
     */
    @Test
    public void testZoneOffsetMapping() {
        new MappingTester<>(ZoneOffsetContainer.class)
                .test(ZoneOffset.of("+01:00"), "\"+01:00\"");
    }

    /*
     * @testName: testOffsetDateTimeMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-3
     *
     * @test_Strategy: Assert that java.time.OffsetDateTime is correctly
     * marshalled as and umarshalled from ISO_OFFSET_DATE_TIME
     */
    @Test
    public void testOffsetDateTimeMapping() {
        new MappingTester<>(OffsetDateTimeContainer.class)
                .test(OffsetDateTime.of(LocalDateTime.of(2000, Calendar.FEBRUARY, 1, 1, 1, 1),
                                        ZoneOffset.of("+01:00")), "\"2000-01-01T01:01:01+01:00\"");
    }

    /*
     * @testName: testOffsetTimeMapping
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-3
     *
     * @test_Strategy: Assert that java.time.OffsetTime is correctly marshalled as
     * and umarshalled from ISO_OFFSET_TIME
     */
    @Test
    public void testOffsetTimeMapping() {
        new MappingTester<>(OffsetTimeContainer.class).test(
                OffsetTime.of(LocalTime.of(1, 1, 1), ZoneOffset.of("+01:00")),
                "\"01:01:01+01:00\"");
    }

    /*
     * @testName: testUnmarshallingUnknownFormat
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.5-4
     *
     * @test_Strategy: Assert that an error is reported if the date/time string
     * does not correspond to the expected datetime format
     */
    @Test
    public void testUnmarshallingUnknownFormat() {
        assertThrows(JsonbException.class,
                     () -> JsonbBuilder.create().fromJson("{ \"instance\" : \"01/01/1970 00:00:00\" }", DateContainer.class),
                     "An exception is expected if the date/time string does not correspond to the expected datetime format.");
    }

    /*
     * @testName: testUnmarshallingDeprecatedTimezoneIds
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.5.2-2
     *
     * @test_Strategy: Assert that error is reported for deprecated three-letter
     * time zone IDs as specified in java.util.Timezone
     */
    @Test
    public void testUnmarshallingDeprecatedTimezoneIds() {
        Jsonb jsonb = JsonbBuilder.create();
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"instance\" : \"CST\" }", TimeZoneContainer.class),
                     "An exception is expected for deprecated three-letter time zone IDs.");
        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"instance\" : \"CST\" }", SimpleTimeZoneContainer.class),
                     "An exception is expected for deprecated three-letter time zone IDs.");
    }

    private static String getHoursFromUTCRegExp(Calendar calendar) {
        String hours = getHoursFromUTC(calendar);
        if ("+00:00".equals(hours)) {
            return "(Z|\\+00:00)";
        } else {
            return "\\" + hours;
        }
    }

    private static String getHoursFromUTC(Calendar calendar) {
        int offsetInTenMins = getMinutesFromUTC(calendar);
        String offset = String.format("%s%02d:%02d",
                                      offsetInTenMins >= 0 ? "+" : "-", Math.abs(offsetInTenMins / 60),
                                      Math.abs((offsetInTenMins) % 60));
        return offset;
    }

    private static int getMinutesFromUTC(Calendar calendar) {
        TimeZone tz = TimeZone.getDefault();
        Calendar def = (Calendar) calendar.clone();
        def.get(Calendar.HOUR); // setFields
        return tz.getOffset(def.getTimeInMillis()) / 60000;
    }
}
