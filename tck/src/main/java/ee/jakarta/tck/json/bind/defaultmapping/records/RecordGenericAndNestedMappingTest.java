/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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

package ee.jakarta.tck.json.bind.defaultmapping.records;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

/**
 * @test
 * @sources RecordGenericAndNestedMappingTest.java
 * @executeClass ee.jakarta.tck.json.bind.defaultmapping.records.RecordGenericAndNestedMappingTest
 *
 * Tests for Java records used as generic type parameters and as components of
 * other records.
 */
public class RecordGenericAndNestedMappingTest {

    /** Simple two-component record used throughout this test class. */
    public record Point(int x, int y) {}

    /** Record used as a component type of another record to test nested mapping. */
    public record Address(String city) {}

    /** Record that contains another record as a component, testing nested record mapping. */
    public record Person(String name, Address address) {}

    private final Jsonb jsonb = JsonbBuilder.create();

    /*
     * @testName: testListOfRecordsSerialization
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.1-5; JSONB:SPEC:JSB-3.17-1;
     * JSONB:SPEC:JSB-3.17.1-1
     *
     * @test_Strategy: Assert that a List<Point> is serialized to a JSON array
     * whose elements are record objects following the default record mapping.
     */
    @Test
    public void testListOfRecordsSerialization() {
        List<Point> list = List.of(new Point(1, 2), new Point(3, 4));
        String jsonString = jsonb.toJson(list);
        assertThat("List<Point> must serialize to a JSON array of record objects in order.",
                   jsonString,
                   matchesPattern("\\[\\s*\\{\\s*\"x\"\\s*:\\s*1\\s*,\\s*\"y\"\\s*:\\s*2\\s*\\}"
                                  + "\\s*,\\s*"
                                  + "\\{\\s*\"x\"\\s*:\\s*3\\s*,\\s*\"y\"\\s*:\\s*4\\s*\\}\\s*\\]"));
    }

    /*
     * @testName: testListOfRecordsDeserialization
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.1-5; JSONB:SPEC:JSB-3.17-1;
     * JSONB:SPEC:JSB-3.17.1-1; JSONB:SPEC:JSB-3.17.1-15
     *
     * @test_Strategy: Assert that a JSON array is deserialized into a List<Point>
     * by passing the full generic type via java.lang.reflect.Type.
     */
    @Test
    public void testListOfRecordsDeserialization() {
        String json = "[{\"x\":1,\"y\":2},{\"x\":3,\"y\":4}]";
        Type listOfPointType = new ArrayList<Point>() {}.getClass().getGenericSuperclass();
        List<Point> result = jsonb.fromJson(json, listOfPointType);
        assertThat("Deserialized list must contain two elements.", result, hasSize(2));
        assertThat("First element x must be 1.", result.get(0).x(), is(1));
        assertThat("First element y must be 2.", result.get(0).y(), is(2));
        assertThat("Second element x must be 3.", result.get(1).x(), is(3));
        assertThat("Second element y must be 4.", result.get(1).y(), is(4));
    }

    /*
     * @testName: testMapWithRecordValueSerialization
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.1-5; JSONB:SPEC:JSB-3.17-1;
     * JSONB:SPEC:JSB-3.17.1-1
     *
     * @test_Strategy: Assert that a Map<String, Point> is serialized to a JSON
     * object where each value follows the default record mapping.
     */
    @Test
    public void testMapWithRecordValueSerialization() {
        Map<String, Point> map = new HashMap<>();
        map.put("origin", new Point(0, 0));
        String jsonString = jsonb.toJson(map);
        assertThat("Map<String,Point> must serialize the record value as a JSON object.",
                   jsonString,
                   matchesPattern("\\{\\s*\"origin\"\\s*:\\s*\\{\\s*\"x\"\\s*:\\s*0\\s*,\\s*\"y\"\\s*:\\s*0\\s*\\}\\s*\\}"));
    }

    /*
     * @testName: testMapWithRecordValueDeserialization
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.1-5; JSONB:SPEC:JSB-3.17-1;
     * JSONB:SPEC:JSB-3.17.1-1; JSONB:SPEC:JSB-3.17.1-15
     *
     * @test_Strategy: Assert that a JSON object is deserialized into a
     * Map<String, Point> by passing the full generic type via java.lang.reflect.Type.
     */
    @Test
    public void testMapWithRecordValueDeserialization() {
        String json = "{\"origin\":{\"x\":0,\"y\":0}}";
        Type mapType = new HashMap<String, Point>() {}.getClass().getGenericSuperclass();
        Map<String, Point> result = jsonb.fromJson(json, mapType);
        assertThat("Deserialized map must contain the 'origin' key.", result, hasEntry("origin", new Point(0, 0)));
    }

    /*
     * @testName: testNestedRecordSerialization
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.1-5; JSONB:SPEC:JSB-3.7.3-1
     *
     * @test_Strategy: Assert that a record whose component is itself a record is
     * serialized to the correct nested JSON object structure.
     */
    @Test
    public void testNestedRecordSerialization() {
        Person person = new Person("Alice", new Address("Paris"));
        String jsonString = jsonb.toJson(person);
        assertThat("Person with nested Address record must contain 'name' property.",
                   jsonString, matchesPattern(".*\"name\"\\s*:\\s*\"Alice\".*"));
        assertThat("Person with nested Address record must contain nested 'address' object with 'city'.",
                   jsonString, matchesPattern(".*\"address\"\\s*:\\s*\\{\\s*\"city\"\\s*:\\s*\"Paris\"\\s*\\}.*"));
    }

    /*
     * @testName: testNestedRecordDeserialization
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.1-5; JSONB:SPEC:JSB-3.7.3-1
     *
     * @test_Strategy: Assert that a nested JSON object is deserialized into a
     * record whose component is itself a record.
     */
    @Test
    public void testNestedRecordDeserialization() {
        String json = "{\"name\":\"Alice\",\"address\":{\"city\":\"Paris\"}}";
        Person result = jsonb.fromJson(json, Person.class);
        assertThat("Person name must be 'Alice'.", result.name(), is("Alice"));
        assertThat("Nested Address must not be null.", result.address() != null);
        assertThat("Nested Address city must be 'Paris'.", result.address().city(), is("Paris"));
    }

}
