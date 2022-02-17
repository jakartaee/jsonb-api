/*
 * Copyright (c) 2017, 2022 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.json.bind.defaultmapping.classes;

import java.lang.reflect.Field;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;

import ee.jakarta.tck.json.bind.defaultmapping.classes.model.StringContainerAccessorsWithoutMatchingField;
import ee.jakarta.tck.json.bind.defaultmapping.classes.model.StringContainerFinalField;
import ee.jakarta.tck.json.bind.defaultmapping.classes.model.StringContainerFinalPublicField;
import ee.jakarta.tck.json.bind.defaultmapping.classes.model.StringContainerNoAccessorsPackagePrivateField;
import ee.jakarta.tck.json.bind.defaultmapping.classes.model.StringContainerNoAccessorsPrivateField;
import ee.jakarta.tck.json.bind.defaultmapping.classes.model.StringContainerNoAccessorsProtectedField;
import ee.jakarta.tck.json.bind.defaultmapping.classes.model.StringContainerNoAccessorsPublicField;
import ee.jakarta.tck.json.bind.defaultmapping.classes.model.StringContainerPackagePrivateAccessors;
import ee.jakarta.tck.json.bind.defaultmapping.classes.model.StringContainerPackagePrivateConstructor;
import ee.jakarta.tck.json.bind.defaultmapping.classes.model.StringContainerPrivateAccessors;
import ee.jakarta.tck.json.bind.defaultmapping.classes.model.StringContainerPrivateAccessorsPublicField;
import ee.jakarta.tck.json.bind.defaultmapping.classes.model.StringContainerPrivateConstructor;
import ee.jakarta.tck.json.bind.defaultmapping.classes.model.StringContainerProtectedAccessors;
import ee.jakarta.tck.json.bind.defaultmapping.classes.model.StringContainerProtectedConstructor;
import ee.jakarta.tck.json.bind.defaultmapping.classes.model.StringContainerProtectedStaticNestedClass;
import ee.jakarta.tck.json.bind.defaultmapping.classes.model.StringContainerPublicAccessors;
import ee.jakarta.tck.json.bind.defaultmapping.classes.model.StringContainerPublicAccessorsPublicField;
import ee.jakarta.tck.json.bind.defaultmapping.classes.model.StringContainerPublicConstructor;
import ee.jakarta.tck.json.bind.defaultmapping.classes.model.StringContainerPublicStaticNestedClass;
import ee.jakarta.tck.json.bind.defaultmapping.classes.model.StringContainerStaticField;
import ee.jakarta.tck.json.bind.defaultmapping.classes.model.StringContainerTransientField;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @test
 * @sources ClassesMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.classes.ClassesMappingTest
 **/
public class ClassesMappingTest {

    private final Jsonb jsonb = JsonbBuilder.create();

    /*
     * @testName: testPublicConstructorAccess
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7-1; JSONB:SPEC:JSB-3.7-2
     *
     * @test_Strategy: Assert that a class with a public constructor can be
     * marshalled and unmarshalled
     */
    @Test
    public void testPublicConstructorAccess() {
        String jsonString = assertDoesNotThrow(() -> jsonb.toJson(new StringContainerPublicConstructor()),
                                               "An exception is not expected when marshalling a class with a public constructor"
                                                       + ".");
        assertThat("Failed to get attribute value when marshalling a class with a public constructor.",
                   jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*\\}"));

        assertDoesNotThrow(() -> jsonb.fromJson("{ \"instance\" : \"Test String\" }", StringContainerPublicConstructor.class),
                           "An exception is not expected when unmarshalling a class with a public constructor.");
    }

    /*
     * @testName: testProtectedConstructorAccess
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7-1; JSONB:SPEC:JSB-3.7-2
     *
     * @test_Strategy: Assert that a class with a protected constructor can be
     * marshalled and unmarshalled
     */
    @Test
    public void testProtectedConstructorAccess() {
        String jsonString = assertDoesNotThrow(() -> jsonb.toJson(StringContainerProtectedConstructor.getClassInstance()),
                                               "An exception is not expected when marshalling a class with a protected "
                                                       + "constructor.");
        assertThat("Failed to get attribute value when marshalling a class with a protected constructor.",
                   jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*\\}"));

        assertDoesNotThrow(() -> jsonb.fromJson("{ \"instance\" : \"Test String\" }", StringContainerProtectedConstructor.class),
                           "An exception is not expected when unmarshalling a class with a protected constructor.");
    }

    /*
     * @testName: testPrivateConstructorAccess
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7-1; JSONB:SPEC:JSB-3.7-2
     *
     * @test_Strategy: Assert that a class with a private constructor can be
     * marshalled but not unmarshalled
     */
    @Test
    public void testPrivateConstructorAccess() {
        String jsonString = assertDoesNotThrow(() -> jsonb.toJson(StringContainerPrivateConstructor.getClassInstance()),
                                               "An exception is not expected when marshalling a class with a private "
                                                       + "constructor.");
        assertThat("Failed to get attribute value when marshalling a class with a private constructor.",
                   jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*\\}"));

        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"instance\" : \"Test String\" }", StringContainerPrivateConstructor.class),
                     "An exception is expected when unmarshalling a class with a private constructor.");
    }

    /*
     * @testName: testPackagePrivateConstructorAccess
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7-1; JSONB:SPEC:JSB-3.7-2
     *
     * @test_Strategy: Assert that a class with a package private constructor can
     * be marshalled but not unmarshalled
     */
    @Test
    public void testPackagePrivateConstructorAccess() {
        String jsonString = assertDoesNotThrow(() -> jsonb.toJson(StringContainerPackagePrivateConstructor.getClassInstance()),
                                               "An exception is not expected when marshalling a class with a package private "
                                                       + "constructor.");
        assertThat("Failed to get attribute value when marshalling a class with a package private constructor.",
                   jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*\\}"));

        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"instance\" : \"Test String\" }", StringContainerPackagePrivateConstructor.class),
                     "An exception is expected when unmarshalling a class with a package private constructor.");
    }

    /*
     * @testName: testPublicAccessors
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7.1-1; JSONB:SPEC:JSB-3.7.1-2
     *
     * @test_Strategy: Assert that private field with public accessors can be
     * managed and unmanaged
     */
    @Test
    public void testPublicAccessors() {
        String jsonString = jsonb.toJson(new StringContainerPublicAccessors());
        assertThat("Failed to get attribute value using public getter.",
                   jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*\\}"));
        try {
            StringContainerPublicAccessors unmarshalledObject = jsonb.fromJson("{ \"instance\" : \"New Test String\" }",
                                                                               StringContainerPublicAccessors.class);
            Field instanceField = StringContainerPublicAccessors.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            try {
                assertThat("Failed to set attribute value using public setter.",
                           instanceField.get(unmarshalledObject), is("New Test String"));
            } finally {
                instanceField.setAccessible(false);
            }
        } catch (Exception x) {
            fail(x.getMessage(), x);
        }
    }

    /*
     * @testName: testProtectedAccessors
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7.1-1; JSONB:SPEC:JSB-3.7.1-2
     *
     * @test_Strategy: Assert that private field with protected accessors is
     * ignored
     */
    @Test
    public void testProtectedAccessors() {
        String jsonString = jsonb.toJson(new StringContainerProtectedAccessors());
        assertThat("Failed to ignore attribute value using protected getter.", jsonString, matchesPattern("\\{\\s*\\}"));
        try {
            StringContainerProtectedAccessors unmarshalledObject = jsonb.fromJson("{ \"instance\" : \"New Test String\" }",
                                                                                  StringContainerProtectedAccessors.class);
            Field instanceField = StringContainerProtectedAccessors.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            try {
                assertThat("Failed to ignore setting attribute value using protected setter.",
                           instanceField.get(unmarshalledObject), is("Test String"));
            } finally {
                instanceField.setAccessible(false);
            }
        } catch (Exception x) {
            fail(x.getMessage(), x);
        }
    }

    /*
     * @testName: testPrivateAccessors
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7.1-1; JSONB:SPEC:JSB-3.7.1-2
     *
     * @test_Strategy: Assert that private field with private accessors is ignored
     */
    @Test
    public void testPrivateAccessors() {
        String jsonString = jsonb.toJson(new StringContainerPrivateAccessors());
        assertThat("Failed to ignore private value using private getter.", jsonString, matchesPattern("\\{\\s*\\}"));
        try {
            StringContainerPrivateAccessors unmarshalledObject = jsonb.fromJson("{ \"instance\" : \"New Test String\" }",
                                                                                StringContainerPrivateAccessors.class);
            Field instanceField = StringContainerPrivateAccessors.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            try {
                assertThat("Failed to ignore setting attribute value using private setter.",
                           instanceField.get(unmarshalledObject), is("Test String"));
            } finally {
                instanceField.setAccessible(false);
            }
        } catch (Exception x) {
            fail(x.getMessage(), x);
        }
    }

    /*
     * @testName: testPackagePrivateAccessors
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7.1-1; JSONB:SPEC:JSB-3.7.1-2
     *
     * @test_Strategy: Assert that private field with package private accessors is
     * ignored
     */
    @Test
    public void testPackagePrivateAccessors() {
        String jsonString = jsonb.toJson(new StringContainerPackagePrivateAccessors());
        assertThat("Failed to ignore private value using package private getter.", jsonString, matchesPattern("\\{\\s*\\}"));

        try {
            StringContainerPackagePrivateAccessors unmarshalledObject = jsonb.fromJson("{ \"instance\" : \"New Test String\" }",
                                                                                       StringContainerPackagePrivateAccessors.class);
            Field instanceField = StringContainerPackagePrivateAccessors.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            try {
                assertThat("Failed to ignore setting attribute value using package private setter.",
                           instanceField.get(unmarshalledObject), is("Test String"));
            } finally {
                instanceField.setAccessible(false);
            }
        } catch (Exception x) {
            fail(x.getMessage(), x);
        }
    }

    /*
     * @testName: testPublicAccessorsPublicField
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7.1-1; JSONB:SPEC:JSB-3.7.1-2
     *
     * @test_Strategy: Assert that public field with public accessors is
     * marshalled and unmarshalled using the accessor
     */
    @Test
    public void testPublicAccessorsPublicField() {
        String jsonString = jsonb.toJson(new StringContainerPublicAccessorsPublicField());
        assertThat("Failed to get attribute value using public getter.",
                   jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\"Getter String\"\\s*\\}"));

        StringContainerPublicAccessorsPublicField unmarshalledObject = jsonb.fromJson("{ \"instance\" : \"New Test String\" }",
                                                                                      StringContainerPublicAccessorsPublicField.class);
        assertThat("Failed to set attribute value using public setter.", unmarshalledObject.instance, is("Setter String"));
    }

    /*
     * @testName: testPrivateAccessorsPublicField
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7.1-1; JSONB:SPEC:JSB-3.7.1-2
     *
     * @test_Strategy: Assert that public field with private accessors is
     * marshalled and unmarshalled using direct field access
     */
    @Test
    public void testPrivateAccessorsPublicField() {
        String jsonString = jsonb.toJson(new StringContainerPrivateAccessorsPublicField());
        assertThat("Failed to ignore public value using private getter.", jsonString, matchesPattern("\\{\\s*\\}"));

        try {
            StringContainerPrivateAccessorsPublicField unmarshalledObject = jsonb
                    .fromJson("{ \"instance\" : \"New Test String\" }",
                              StringContainerPrivateAccessorsPublicField.class);
            Field instanceField = StringContainerPrivateAccessorsPublicField.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            try {
                assertThat("Failed to ignore setting public value using private setter.",
                           instanceField.get(unmarshalledObject), is("Test String"));
            } finally {
                instanceField.setAccessible(false);
            }
        } catch (Exception x) {
            fail(x.getMessage(), x);
        }
    }

    /*
     * @testName: testNoAccessorsPublicField
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7.1-1; JSONB:SPEC:JSB-3.7.1-2
     *
     * @test_Strategy: Assert that public field with no accessors is marshalled
     * and unmarshalled using direct field access
     */
    @Test
    public void testNoAccessorsPublicField() {
        String jsonString = jsonb.toJson(new StringContainerNoAccessorsPublicField());
        assertThat("Failed to get public field value.",
                   jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*\\}"));

        StringContainerNoAccessorsPublicField unmarshalledObject = jsonb.fromJson("{ \"instance\" : \"New Test String\" }",
                                                                                  StringContainerNoAccessorsPublicField.class);
        assertThat("Failed to set public field value.", unmarshalledObject.instance, is("New Test String"));
    }

    /*
     * @testName: testNoAccessorsProtectedField
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7.1-1; JSONB:SPEC:JSB-3.7.1-2
     *
     * @test_Strategy: Assert that protected field with no accessors is ignored
     */
    @Test
    public void testNoAccessorsProtectedField() {
        String jsonString = jsonb.toJson(new StringContainerNoAccessorsProtectedField());
        assertThat("Failed to ignore getting protected field value.", jsonString, matchesPattern("\\{\\s*\\}"));

        try {
            StringContainerNoAccessorsProtectedField unmarshalledObject = jsonb.fromJson("{ \"instance\" : \"New Test String\" }",
                                                                                         StringContainerNoAccessorsProtectedField.class);
            Field instanceField = StringContainerNoAccessorsProtectedField.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            try {
                assertThat("Failed to ignore setting public value using private setter.",
                           instanceField.get(unmarshalledObject), is("Test String"));
            } finally {
                instanceField.setAccessible(false);
            }
        } catch (Exception x) {
            fail(x.getMessage(), x);
        }
    }

    /*
     * @testName: testNoAccessorsPrivateField
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7.1-1; JSONB:SPEC:JSB-3.7.1-2
     *
     * @test_Strategy: Assert that private field with no accessors is ignored
     */
    @Test
    public void testNoAccessorsPrivateField() {
        String jsonString = jsonb.toJson(new StringContainerNoAccessorsPrivateField());
        assertThat("Failed to ignore getting private field value.", jsonString, matchesPattern("\\{\\s*\\}"));

        try {
            StringContainerNoAccessorsPrivateField unmarshalledObject = jsonb.fromJson("{ \"instance\" : \"New Test String\" }",
                                                                                       StringContainerNoAccessorsPrivateField.class);
            Field instanceField = StringContainerNoAccessorsPrivateField.class
                    .getDeclaredField("instance");
            instanceField.setAccessible(true);
            try {
                assertThat("Failed to ignore setting private field value.",
                           instanceField.get(unmarshalledObject), is("Test String"));
            } finally {
                instanceField.setAccessible(false);
            }
        } catch (Exception x) {
            fail(x.getMessage(), x);
        }
    }

    /*
     * @testName: testNoAccessorsPackagePrivateField
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7.1-1; JSONB:SPEC:JSB-3.7.1-2
     *
     * @test_Strategy: Assert that package private field with no accessors is
     * ignored
     */
    @Test
    public void testNoAccessorsPackagePrivateField() {
        String jsonString = jsonb.toJson(new StringContainerNoAccessorsPackagePrivateField());
        assertThat("Failed to ignore getting package private field value.", jsonString, matchesPattern("\\{\\s*\\}"));

        try {
            StringContainerNoAccessorsPackagePrivateField unmarshalledObject = jsonb
                    .fromJson("{ \"instance\" : \"New Test String\" }",
                              StringContainerNoAccessorsPackagePrivateField.class);
            Field instanceField = StringContainerNoAccessorsPackagePrivateField.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            try {
                assertThat("Failed to ignore setting package private field value.",
                           instanceField.get(unmarshalledObject), is("Test String"));
            } finally {
                instanceField.setAccessible(false);
            }
        } catch (Exception x) {
            fail(x.getMessage(), x);
        }
    }

    /*
     * @testName: testTransientField
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7.1-3
     *
     * @test_Strategy: Assert that transient fields are ignored during marshalling
     * and unmarshalling
     */
    @Test
    public void testTransientField() {
        String jsonString = jsonb.toJson(new StringContainerTransientField());
        assertThat("Failed to ignore getting transient field value.", jsonString, matchesPattern("\\{\\s*\\}"));

        try {
            StringContainerTransientField unmarshalledObject = jsonb.fromJson("{ \"instance\" : \"New Test String\" }",
                                                                              StringContainerTransientField.class);
            Field instanceField = StringContainerTransientField.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            try {
                assertThat("Failed to ignore setting transient field value.",
                           instanceField.get(unmarshalledObject), is("Test String"));
            } finally {
                instanceField.setAccessible(false);
            }
        } catch (Exception x) {
            fail(x.getMessage(), x);
        }
    }

    /*
     * @testName: testStaticField
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7.1-5
     *
     * @test_Strategy: Assert that static fields are ignored during marshalling
     * and unmarshalling
     */
    @Test
    public void testStaticField() {
        String jsonString = jsonb.toJson(new StringContainerStaticField());
        assertThat("Failed to ignore getting static field value.", jsonString, matchesPattern("\\{\\s*\\}"));

        try {
            StringContainerStaticField unmarshalledObject = jsonb.fromJson("{ \"instance\" : \"New Test String\" }",
                                                                           StringContainerStaticField.class);
            Field instanceField = StringContainerStaticField.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            try {
                assertThat("Failed to ignore setting static field value.",
                           instanceField.get(unmarshalledObject), is("Test String"));
            } finally {
                instanceField.setAccessible(false);
            }
        } catch (Exception x) {
            fail(x.getMessage(), x);
        }
    }

    /*
     * @testName: testFinalField
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7.1-4
     *
     * @test_Strategy: Assert that final fields are correctly marshalled but not
     * unmarshalled
     */
    @Test
    public void testFinalField() {
        String jsonString = jsonb.toJson(new StringContainerFinalField());
        assertThat("Failed to get final field value.",
                   jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*\\}"));

        try {
            StringContainerFinalField unmarshalledObject = jsonb.fromJson("{ \"instance\" : \"New Test String\" }",
                                                                          StringContainerFinalField.class);
            Field instanceField = StringContainerFinalField.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            try {
                assertThat("Failed to ignore setting final field value.",
                           instanceField.get(unmarshalledObject), is("Test String"));
            } finally {
                instanceField.setAccessible(false);
            }
        } catch (Exception x) {
            fail(x.getMessage(), x);
        }
    }

    /*
     * @testName: testFinalPublicField
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7.1-4
     *
     * @test_Strategy: Assert that final public fields are correctly marshalled
     * but not unmarshalled
     */
    @Test
    public void testFinalPublicField() {
        String jsonString = jsonb.toJson(new StringContainerFinalPublicField());
        assertThat("Failed to get final public field value.",
                   jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*\\}"));

        try {
            StringContainerFinalPublicField unmarshalledObject = jsonb.fromJson("{ \"instance\" : \"New Test String\" }",
                                                                                StringContainerFinalPublicField.class);
            Field instanceField = StringContainerFinalPublicField.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            try {
                assertThat("Failed to ignore setting final public field value.",
                           instanceField.get(unmarshalledObject), is("Test String"));
            } finally {
                instanceField.setAccessible(false);
            }
        } catch (Exception x) {
            fail(x.getMessage(), x);
        }
    }

    /*
     * @testName: testAccessorsWithoutCorrespondingField
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7.1-7
     *
     * @test_Strategy: Assert that public accessor methods without a corresponding
     * field are supported
     */
    @Test
    public void testAccessorsWithoutCorrespondingField() {
        String jsonString = jsonb.toJson(new StringContainerAccessorsWithoutMatchingField());
        assertThat("Failed to get value from getter without corresponding field.",
                   jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*\\}"));

        try {
            StringContainerAccessorsWithoutMatchingField unmarshalledObject = jsonb
                    .fromJson("{ \"instance\" : \"New Test String\" }",
                              StringContainerAccessorsWithoutMatchingField.class);
            Field instanceField = StringContainerAccessorsWithoutMatchingField.class.getDeclaredField("field");
            instanceField.setAccessible(true);
            try {
                assertThat("Failed to set value using setter without corresponding field.",
                           instanceField.get(unmarshalledObject), is("New Test String"));
            } finally {
                instanceField.setAccessible(false);
            }
        } catch (Exception x) {
            fail(x.getMessage(), x);
        }
    }

    /*
     * @testName: testDeserialisationOfNonExistentField
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7.1-6
     *
     * @test_Strategy: Assert that an attribute without a corresponding field or
     * accessor is ignored during unmarshalling
     */
    @Test
    public void testDeserialisationOfNonExistentField() {
        try {
            StringContainerPublicAccessors unmarshalledObject = jsonb.fromJson("{ \"field\" : \"New Test String\" }",
                                                                               StringContainerPublicAccessors.class);
            Field instanceField = StringContainerPublicAccessors.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            try {
                assertThat("Failed to ignore setting value to non existent field.",
                           instanceField.get(unmarshalledObject), is("Test String"));
            } finally {
                instanceField.setAccessible(false);
            }
        } catch (Exception x) {
            fail(x.getMessage(), x);
        }
    }

    /*
     * @testName: testPublicStaticNestedClass
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7.3-1; JSONB:SPEC:JSB-3.7.3-2
     *
     * @test_Strategy: Assert that public static nested class is correctly handled
     */
    @Test
    public void testPublicStaticNestedClass() {
        String jsonString = jsonb.toJson(new StringContainerPublicStaticNestedClass());
        assertThat("Failed to get attribute value from public nested class.",
                   jsonString,
                   matchesPattern("\\{\\s*\"nestedClass\"\\s*:\\s*\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*\\}\\s*\\}"));

        try {
            String toDeserialize = "{ \"nestedClass\" : { \"instance\" : \"New Test String\" } }";
            StringContainerPublicStaticNestedClass unmarshalledObject = jsonb.fromJson(toDeserialize,
                                                                                       StringContainerPublicStaticNestedClass.class);
            Field instanceField = StringContainerPublicStaticNestedClass.NestedClass.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            try {
                assertThat("Failed to set attribute value to public nested class.",
                           instanceField.get(unmarshalledObject.nestedClass), is("New Test String"));
            } finally {
                instanceField.setAccessible(false);
            }
        } catch (Exception x) {
            fail(x.getMessage(), x);
        }
    }

    /*
     * @testName: testProtectedStaticNestedClass
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7.3-1; JSONB:SPEC:JSB-3.7.3-2
     *
     * @test_Strategy: Assert that protected static nested class is correctly
     * handled
     */
    @Test
    public void testProtectedStaticNestedClass() {
        String jsonString = jsonb.toJson(new StringContainerProtectedStaticNestedClass());
        assertThat("Failed to get attribute value from protected nested class.",
                   jsonString,
                   matchesPattern("\\{\\s*\"nestedClass\"\\s*:\\s*\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*\\}\\s*\\}"));

        try {
            String toDeserialize = "{ \"nestedClass\" : { \"instance\" : \"New Test String\" } }";
            StringContainerProtectedStaticNestedClass unmarshalledObject = jsonb.fromJson(toDeserialize,
                                                                                          StringContainerProtectedStaticNestedClass.class);
            Class<?> nestedClass = StringContainerProtectedStaticNestedClass.class.getDeclaredClasses()[0];
            Field instanceField = nestedClass.getDeclaredField("instance");
            instanceField.setAccessible(true);
            try {
                assertThat("Failed to set attribute value to protected nested class.",
                           instanceField.get(unmarshalledObject.nestedClass), is("New Test String"));
            } finally {
                instanceField.setAccessible(false);
            }
        } catch (Exception x) {
            fail(x.getMessage(), x);
        }
    }

    /*
     * @testName: testAnonymousClass
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.7.4-1
     *
     * @test_Strategy: Assert that marshalling of an anonymous class is supported
     * using default object mapping. Unmarshalling is not supported by the spec so
     * not tested.
     */
    @Test
    public void testAnonymousClass() {
        Object anonymousInstance = new Object() {
            @SuppressWarnings("unused")
            public String newInstance = "Anonymous";
        };
        String jsonString = jsonb.toJson(anonymousInstance);
        assertThat("Failed to get attribute value from anonymous class.",
                   jsonString, matchesPattern("\\{\\s*\"newInstance\"\\s*:\\s*\"Anonymous\"\\s*\\}"));
    }
}
