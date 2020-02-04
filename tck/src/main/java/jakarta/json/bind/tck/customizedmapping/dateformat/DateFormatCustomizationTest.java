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

package jakarta.json.bind.tck.customizedmapping.dateformat;

import static org.junit.Assert.fail;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.tck.customizedmapping.dateformat.model.AnnotatedAccessorsDateContainer;
import jakarta.json.bind.tck.customizedmapping.dateformat.model.AnnotatedFieldDateContainer;
import jakarta.json.bind.tck.customizedmapping.dateformat.model.AnnotatedTypeDateContainer;
import jakarta.json.bind.tck.customizedmapping.dateformat.model.AnnotatedTypeFieldOverrideDateContainer;
import jakarta.json.bind.tck.customizedmapping.dateformat.model.DateContainer;
import jakarta.json.bind.tck.customizedmapping.dateformat.model.customized.CustomizedPackageDateContainer;
import jakarta.json.bind.tck.customizedmapping.dateformat.model.customized.CustomizedPackageTypeOverrideDateContainer;
import jakarta.json.bind.tck.customizedmapping.dateformat.model.customized.CustomizedPackageTypeOverrideFieldOverrideDateContainer;

/**
 * @test
 * @sources DateFormatCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.dateformat.DateFormatCustomizationTest
 **/
@RunWith(Arquillian.class)
public class DateFormatCustomizationTest {
    
    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, MethodHandles.lookup().lookupClass().getPackage().getName());
    }
    
  private final Jsonb jsonb = JsonbBuilder.create();

  /*
   * @testName: testDateFormatConfig
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.8-3
   *
   * @test_Strategy: Assert that JsonbConfig.withDateFormat customization is
   * correctly applied
   */
  @Test
  public void testDateFormatConfig() {
    String format = "E DD MMM yyyy HH:mm:ss z";
    Locale locale = Locale.GERMAN;
    Jsonb jsonb = JsonbBuilder
        .create(new JsonbConfig().withDateFormat(format, locale));

    Date instance = new Date(0);
    String jsonString = jsonb.toJson(new DateContainer() {
      {
        setInstance(instance);
      }
    });
    String toMatch = completeByFormat("\\{\\s*\"instance\"\\s*:\\s*", "\\s*\\}",
        format, locale, instance);
    if (!jsonString.matches(toMatch)) {
      System.out.append("jsonString").println(jsonString);
      System.out.append("does not match").println(toMatch);
      fail(
          "Failed to correctly customize date format during marshalling using JsonbConfig.withDateFormat.");
    }

    String json = String.format("{ \"instance\" : \"%s 1970 01:00:00 MEZ\" }",
        getDay("19700001", Locale.GERMAN));
    DateContainer unmarshalledObject = jsonb.fromJson(json,
        DateContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      System.out.append("unmarshalled Object")
          .println(unmarshalledObject.getInstance());
      System.out.append("does not match").println(instance);
      fail(
          "Failed to correctly customize date format during unmarshalling using JsonbConfig.withDateFormat.");
    }

    return; // passed
  }

  /*
   * @testName: testDateFormatPackage
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.8-1
   *
   * @test_Strategy: Assert that date customization using JsonbDateFormat
   * annotation on package is correctly applied
   */
  @Test
  public void testDateFormatPackage() {
    Date instance = new Date(0);
    String jsonString = jsonb.toJson(new CustomizedPackageDateContainer() {
      {
        setInstance(instance);
      }
    });
    JsonbDateFormat format = getAnnotationOnPackage(
        CustomizedPackageDateContainer.class, JsonbDateFormat.class);
    Locale locale = new Locale.Builder().setLanguage(format.locale()).build();
    String toMatch = completeByFormat("\\{\\s*\"instance\"\\s*:\\s*", "\\s*\\}",
        format.value(), locale, instance);
    if (!jsonString.matches(toMatch)) {
      System.out.append("jsonString").println(jsonString);
      System.out.append("does not match").println(toMatch);
      fail(
          "Failed to correctly customize date format during marshalling using JsonbDateFormat annotation on package.");
    }

    String json = String.format("{ \"instance\" : \"%s 1970 01:00:00 CET\" }",
        getDay("19700001", Locale.ITALY));
    CustomizedPackageDateContainer unmarshalledObject = jsonb.fromJson(json,
        CustomizedPackageDateContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      System.out.append("unmarshalledObject")
          .println(unmarshalledObject.getInstance());
      System.out.append("does not match").println(instance);
      fail(
          "Failed to correctly customize date format during unmarshalling using JsonbDateFormat annotation on package.");
    }

    return; // passed
  }

  /*
   * @testName: testDateFormatType
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.8-1
   *
   * @test_Strategy: Assert that date customization using JsonbDateFormat
   * annotation on type is correctly applied
   */
  @Test
  public void testDateFormatType() {
    Date instance = new Date(0);
    String jsonString = jsonb.toJson(new AnnotatedTypeDateContainer() {
      {
        setInstance(instance);
      }
    });
    JsonbDateFormat format = getAnnotation(AnnotatedTypeDateContainer.class,
        JsonbDateFormat.class);
    Locale locale = new Locale.Builder().setLanguage(format.locale()).build();
    String toMatch = completeByFormat("\\{\\s*\"instance\"\\s*:\\s*", "\\s*\\}",
        format.value(), locale, instance);
    if (!jsonString.matches(toMatch)) {
      System.out.append("jsonString").println(jsonString);
      System.out.append("does not match").println(toMatch);
      fail(
          "Failed to correctly customize date format during marshalling using JsonbDateFormat annotation on type.");
    }

    String json = String.format("{ \"instance\" : \"%s 1970 01:00:00 MEZ\" }",
        getDay("19700001", Locale.GERMAN));
    AnnotatedTypeDateContainer unmarshalledObject = jsonb.fromJson(json,
        AnnotatedTypeDateContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      System.out.append("unmarshalled Object")
          .println(unmarshalledObject.getInstance());
      System.out.append("does not match").println(instance);
      fail(
          "Failed to correctly customize date format during unmarshalling using JsonbDateFormat annotation on type.");
    }

    return; // passed
  }

  /*
   * @testName: testDateFormatField
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.8-1
   *
   * @test_Strategy: Assert that date customization using JsonbDateFormat
   * annotation on field is correctly applied
   */
  @Test
  public void testDateFormatField() {
    Date instance = new Date(0);
    String jsonString = jsonb.toJson(new AnnotatedFieldDateContainer() {
      {
        setInstance(instance);
      }
    });
    JsonbDateFormat format = getAnnotationOnField(
        AnnotatedFieldDateContainer.class, JsonbDateFormat.class, "instance");
    Locale locale = new Locale.Builder().setLanguage(format.locale()).build();
    String toMatch = completeByFormat("\\{\\s*\"instance\"\\s*:\\s*", "\\s*\\}",
        format.value(), locale, instance);
    if (!jsonString.matches(toMatch)) {
      System.out.append("jsonString").println(jsonString);
      System.out.append("does not match").println(toMatch);
      fail(
          "Failed to correctly customize date format during marshalling using JsonbDateFormat annotation on field.");
    }

    String json = String.format("{ \"instance\" : \"%s 1970 01:00:00 MEZ\" }",
        getDay("19700001", Locale.GERMAN));
    AnnotatedFieldDateContainer unmarshalledObject = jsonb.fromJson(json,
        AnnotatedFieldDateContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      System.out.append("unmarshalled Object")
          .println(unmarshalledObject.getInstance());
      System.out.append("does not match").println(instance);
      fail(
          "Failed to correctly customize date format during unmarshalling using JsonbDateFormat annotation on field.");
    }

    return; // passed
  }

  /*
   * @testName: testDateFormatAccessors
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.8-1
   *
   * @test_Strategy: Assert that date customization using JsonbDateFormat
   * annotation on accessors is individually applied on marshalling and
   * unmarshalling
   */
  @Test
  public void testDateFormatAccessors() {
    Date instance = new Date(0);
    String jsonString = jsonb.toJson(new AnnotatedAccessorsDateContainer() {
      {
        setInstance(instance);
      }
    });
    JsonbDateFormat format = getAnnotationOnMethod(
        AnnotatedAccessorsDateContainer.class, JsonbDateFormat.class,
        "getInstance");
    Locale locale = new Locale.Builder().setLanguage(format.locale()).build();
    String toMatch = completeByFormat("\\{\\s*\"instance\"\\s*:\\s*", "\\s*\\}",
        format.value(), locale, instance);
    if (!jsonString.matches(toMatch)) {
      System.out.append("jsonString").println(jsonString);
      System.out.append("does not match").println(toMatch);
      fail(
          "Failed to correctly customize date format during marshalling using JsonbDateFormat annotation on getter.");
    }

    String json = String.format("{ \"instance\" : \"%s 1970 01:00:00 MEZ\" }",
        getDay("19700001", Locale.GERMAN));
    AnnotatedAccessorsDateContainer unmarshalledObject = jsonb.fromJson(json,
        AnnotatedAccessorsDateContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      System.out.append("unmarshalled Object")
          .println(unmarshalledObject.getInstance());
      System.out.append("does not match").println(instance);
      fail(
          "Failed to correctly customize date format during unmarshalling using JsonbDateFormat annotation on setter.");
    }

    return; // passed
  }

  /*
   * @testName: testDateFormatConfigPackageOverride
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.8-1; JSONB:SPEC:JSB-4.8-2;
   * JSONB:SPEC:JSB-4.8-3
   *
   * @test_Strategy: Assert that JsonbConfig.withDateFormat customization is
   * correctly overridden by annotating package with JsonbDateFormat
   */
  @Test
  public void testDateFormatConfigPackageOverride() {
    Jsonb jsonb = JsonbBuilder.create(new JsonbConfig()
        .withDateFormat("E DD MMM yyyy HH:mm:ss z", Locale.GERMAN));

    Date instance = new Date(0);
    String jsonString = jsonb.toJson(new CustomizedPackageDateContainer() {
      {
        setInstance(instance);
      }
    });
    JsonbDateFormat format = getAnnotationOnPackage(
        CustomizedPackageDateContainer.class, JsonbDateFormat.class);
    Locale locale = new Locale.Builder().setLanguage(format.locale()).build();
    String toMatch = completeByFormat("\\{\\s*\"instance\"\\s*:\\s*", "\\s*\\}",
        format.value(), locale, instance);
    if (!jsonString.matches(toMatch)) {
      System.out.append("jsonString").println(jsonString);
      System.out.append("does not match").println(toMatch);
      fail(
          "Failed to correctly override date format customization using JsonbConfig.withDateFormat during marshalling by annotating package with JsonbDateFormat.");
    }

    String json = String.format("{ \"instance\" : \"%s 1970 01:00:00 CET\" }",
        getDay("19700001", Locale.ITALY));
    CustomizedPackageDateContainer unmarshalledObject = jsonb.fromJson(json,
        CustomizedPackageDateContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      System.out.append("unmarshalled Object")
          .println(unmarshalledObject.getInstance());
      System.out.append("does not match").println(instance);
      fail(
          "Failed to correctly override date format customization using JsonbConfig.withDateFormat during unmarshalling by annotating package with JsonbDateFormat.");
    }

    return; // passed
  }

  /*
   * @testName: testDateFormatPackageTypeOverride
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.8-1; JSONB:SPEC:JSB-4.8-2
   *
   * @test_Strategy: Assert that date customization using JsonbDateFormat
   * annotation on package is correctly overridden by annotating type using
   * JsonbDateFormat
   */
  @Test
  public void testDateFormatPackageTypeOverride() {
    Date instance = new Date(0);
    String jsonString = jsonb
        .toJson(new CustomizedPackageTypeOverrideDateContainer() {
          {
            setInstance(instance);
          }
        });
    JsonbDateFormat format = getAnnotation(
        CustomizedPackageTypeOverrideDateContainer.class,
        JsonbDateFormat.class);
    Locale locale = new Locale.Builder().setLanguage(format.locale()).build();
    String toMatch = completeByFormat("\\{\\s*\"instance\"\\s*:\\s*", "\\s*\\}",
        format.value(), locale, instance);
    if (!jsonString.matches(toMatch)) {
      System.out.append("jsonString").println(jsonString);
      System.out.append("does not match").println(toMatch);
      fail(
          "Failed to correctly override date format customization using JsonbDateFormat annotation on package during marshalling using JsonbDateFormat annotation on type.");
    }

    toMatch = completeByFormat("{ \"instance\" : ", " }", format.value(),
        locale, instance);
    CustomizedPackageTypeOverrideDateContainer unmarshalledObject = jsonb
        .fromJson(toMatch, CustomizedPackageTypeOverrideDateContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      System.out.append("unmarshalled Object")
          .println(unmarshalledObject.getInstance());
      System.out.append("does not match").println(instance);
      fail(
          "Failed to correctly override date format customization using JsonbDateFormat annotation on package during unmarshalling using JsonbDateFormat annotation on type.");
    }

    return; // passed
  }

  /*
   * @testName: testDateFormatTypeFieldOverride
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.8-1; JSONB:SPEC:JSB-4.8-2
   *
   * @test_Strategy: Assert that date customization using JsonbDateFormat
   * annotation on type is correctly overridden by annotating field using
   * JsonbDateFormat
   */
  @Test
  public void testDateFormatTypeFieldOverride() {
    Date instance = new Date(0);
    String jsonString = jsonb
        .toJson(new AnnotatedTypeFieldOverrideDateContainer() {
          {
            setInstance(instance);
          }
        });
    JsonbDateFormat format = getAnnotationOnField(
        AnnotatedTypeFieldOverrideDateContainer.class, JsonbDateFormat.class,
        "instance");
    Locale locale = new Locale.Builder().setLanguage(format.locale()).build();
    String toMatch = completeByFormat("\\{\\s*\"instance\"\\s*:\\s*", "\\s*\\}",
        format.value(), locale, instance);
    if (!jsonString.matches(toMatch)) {
      System.out.append("jsonString").println(jsonString);
      System.out.append("does not match").println(toMatch);
      fail(
          "Failed to correctly customize date format during marshalling using JsonbDateFormat annotation on type.");
    }

    String json = String.format("{ \"instance\" : \"%s 1970 01:00:00 CET\" }",
        getDay("19700001", Locale.ITALY));
    AnnotatedTypeFieldOverrideDateContainer unmarshalledObject = jsonb
        .fromJson(json, AnnotatedTypeFieldOverrideDateContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      System.out.append("unmarshalled Object")
          .println(unmarshalledObject.getInstance());
      System.out.append("does not match").println(instance);
      fail(
          "Failed to correctly customize date format during unmarshalling using JsonbDateFormat annotation on type.");
    }

    return; // passed
  }

  /*
   * @testName: testDateFormatConfigPackageOverrideTypeOverride
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.8-1; JSONB:SPEC:JSB-4.8-2;
   * JSONB:SPEC:JSB-4.8-3
   *
   * @test_Strategy: Assert that JsonbConfig.withDateFormat customization and
   * package annotating with JsonbDateFormat is correctly overridden by
   * annotating type with JsonbDateFormat
   */
  @Test
  public void testDateFormatConfigPackageOverrideTypeOverride() {
    Jsonb jsonb = JsonbBuilder.create(new JsonbConfig()
        .withDateFormat("E DD MMM yyyy HH:mm:ss z", Locale.CANADA));

    Date instance = new Date(0);
    String jsonString = jsonb
        .toJson(new CustomizedPackageTypeOverrideDateContainer() {
          {
            setInstance(instance);
          }
        });
    JsonbDateFormat format = getAnnotation(
        CustomizedPackageTypeOverrideDateContainer.class,
        JsonbDateFormat.class);
    Locale locale = new Locale.Builder().setLanguage(format.locale()).build();
    String toMatch = completeByFormat("\\{\\s*\"instance\"\\s*:\\s*", "\\s*\\}",
        format.value(), locale, instance);
    if (!jsonString.matches(toMatch)) {
      System.out.append("jsonString").println(jsonString);
      System.out.append("does not match").println(toMatch);
      fail(
          "Failed to correctly override date format customization using JsonbConfig.withDateFormat and package annotating with JsonbDateFormat during marshalling by annotating type with JsonbDateFormat.");
    }

    toMatch = completeByFormat("{ \"instance\" : ", " }", format.value(),
        locale, instance);
    CustomizedPackageTypeOverrideDateContainer unmarshalledObject = jsonb
        .fromJson(toMatch, CustomizedPackageTypeOverrideDateContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      System.out.append("unmarshalled Object")
          .println(unmarshalledObject.getInstance());
      System.out.append("does not match").println(instance);
      fail(
          "Failed to correctly override date format customization using JsonbConfig.withDateFormat and package annotating with JsonbDateFormat during unmarshalling by annotating type with JsonbDateFormat.");
    }

    return; // passed
  }

  /*
   * @testName: testDateFormatPackageTypeOverrideFieldOverride
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.8-1; JSONB:SPEC:JSB-4.8-2
   *
   * @test_Strategy: Assert that date customization using JsonbDateFormat
   * annotation on package and type is correctly overridden by annotating field
   * using JsonbDateFormat
   */
  @Test
  public void testDateFormatPackageTypeOverrideFieldOverride() {
    Date instance = new Date(0);
    String jsonString = jsonb
        .toJson(new CustomizedPackageTypeOverrideFieldOverrideDateContainer() {
          {
            setInstance(instance);
          }
        });
    JsonbDateFormat format = getAnnotationOnField(
        CustomizedPackageTypeOverrideFieldOverrideDateContainer.class,
        JsonbDateFormat.class, "instance");
    Locale locale = new Locale.Builder().setLanguage(format.locale()).build();
    String toMatch = completeByFormat("\\{\\s*\"instance\"\\s*:\\s*", "\\s*\\}",
        format.value(), locale, instance);
    if (!jsonString.matches(toMatch)) {
      System.out.append("jsonString").println(jsonString);
      System.out.append("does not match").println(toMatch);
      fail(
          "Failed to correctly override date format customization using JsonbDateFormat annotation on package and type during marshalling using JsonbDateFormat annotation on field.");
    }

    toMatch = completeByFormat("{ \"instance\" : ", " }", format.value(),
        locale, instance);
    CustomizedPackageTypeOverrideFieldOverrideDateContainer unmarshalledObject = jsonb
        .fromJson(toMatch,
            CustomizedPackageTypeOverrideFieldOverrideDateContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      System.out.append("unmarshalled Object")
          .println(unmarshalledObject.getInstance());
      System.out.append("does not match").println(instance);
      fail(
          "Failed to correctly override date format customization using JsonbDateFormat annotation on package and type during unmarshalling using JsonbDateFormat annotation on field.");
    }

    return; // passed
  }

  /*
   * @testName: testDateFormatConfigPackageOverrideTypeOverrideFieldOverride
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.8-1; JSONB:SPEC:JSB-4.8-2;
   * JSONB:SPEC:JSB-4.8-3
   *
   * @test_Strategy: Assert that JsonbConfig.withDateFormat customization and
   * package and type annotating with JsonbDateFormat is correctly overridden by
   * annotating field with JsonbDateFormat
   */
  @Test
  public void testDateFormatConfigPackageOverrideTypeOverrideFieldOverride() {
    Jsonb jsonb = JsonbBuilder.create(new JsonbConfig()
        .withDateFormat("E DD MMM yyyy HH:mm:ss z", Locale.GERMAN));

    Date instance = new Date(0);
    JsonbDateFormat format = getAnnotationOnField(
        CustomizedPackageTypeOverrideFieldOverrideDateContainer.class,
        JsonbDateFormat.class, "instance");
    Locale locale = new Locale.Builder().setLanguage(format.locale()).build();
    String toMatch = completeByFormat("\\{\\s*\"instance\"\\s*:\\s*", "\\s*\\}",
        format.value(), locale, instance);
    String jsonString = jsonb
        .toJson(new CustomizedPackageTypeOverrideFieldOverrideDateContainer() {
          {
            setInstance(instance);
          }
        });
    if (!jsonString.matches(toMatch)) {
      System.out.append("jsonString").println(jsonString);
      System.out.append("does not match").println(toMatch);
      fail(
          "Failed to correctly override date format customization using JsonbConfig.withDateFormat and package and type annotating with JsonbDateFormat during marshalling by annotating field with JsonbDateFormat.");
    }

    toMatch = completeByFormat("{ \"instance\" : ", " }", format.value(),
        locale, instance);
    CustomizedPackageTypeOverrideFieldOverrideDateContainer unmarshalledObject = jsonb
        .fromJson(toMatch,
            CustomizedPackageTypeOverrideFieldOverrideDateContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      System.out.append("unmarshalled Object")
          .println(unmarshalledObject.getInstance());
      System.out.append("does not match").println(instance);
      fail(
          "Failed to correctly override date format customization using JsonbConfig.withDateFormat and package and type annotating with JsonbDateFormat during unmarshalling by annotating field with JsonbDateFormat.");
    }

    return; // passed
  }

  private String completeByFormat(String prefix, String suffix, String mask,
      Locale locale, Date value) {
    StringBuilder sb = new StringBuilder();
    sb.append(prefix).append('"').append(format(mask, locale, value))
        .append('"').append(suffix);
    return sb.toString();
  }

  private String format(String mask, Locale locale, Date value) {
    DateTimeFormatter dtf = new DateTimeFormatterBuilder().appendPattern(mask)
        .toFormatter(locale);
    return dtf
        .format(ZonedDateTime.ofInstant(value.toInstant(), ZoneId.of("UTC")));
  }

  private static <T extends Annotation> T getAnnotation(Class<?> clz,
      Class<T> annotation) {
    return getAnnotationOnElement(clz, annotation);
  }

  private static <T extends Annotation> T getAnnotationOnMethod(Class<?> clz,
      Class<T> annotation, String methodName, Class<?>... params) {
    try {
      Method m = clz.getDeclaredMethod(methodName, params);
      return getAnnotationOnElement(m, annotation);
    } catch (NoSuchMethodException | SecurityException e) {
      fail(e.getMessage());
      return null;
    }
  }

  private static <T extends Annotation> T getAnnotationOnField(Class<?> clz,
      Class<T> annotation, String fieldName) {
    try {
      Field f = clz.getDeclaredField(fieldName);
      return getAnnotationOnElement(f, annotation);
    } catch (NoSuchFieldException | SecurityException e) {
      fail(e.getMessage());
      return null;
    }
    
  }

  private static <T extends Annotation> T getAnnotationOnPackage(Class<?> clz,
      Class<T> annotation) {
    Package p = clz.getPackage();
    return getAnnotationOnElement(p, annotation);
  }

  private static <T extends Annotation> T getAnnotationOnElement(
      AnnotatedElement e, Class<T> annotation) {
    T[] a = e.getAnnotationsByType(annotation);
    if (a == null || a.length == 0) {
      fail(
          "No " + annotation.getName() + " annotation on " + e.toString());
    }
    if (a.length != 1) {
      fail("Too many " + annotation.getName() + " annotation on "
          + e.toString());
    }
    return a[0];
  }

  private static String getDay(String yyyymmdd, Locale locale) {
    Calendar c = Calendar.getInstance(locale);
    c.set(Calendar.YEAR, Integer.parseInt(yyyymmdd.substring(0, 4)));
    c.set(Calendar.MONTH, Integer.parseInt(yyyymmdd.substring(4, 6)));
    c.set(Calendar.DATE, Integer.parseInt(yyyymmdd.substring(6, 8)));
    SimpleDateFormat sdf = new SimpleDateFormat("EEE' 'dd' 'MMM", locale);
    return sdf.format(c.getTime());
  }

}
