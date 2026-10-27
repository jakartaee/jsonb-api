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

package ee.jakarta.tck.json.bind.customizedmapping.records;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ee.jakarta.tck.json.bind.TypeToken;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.annotation.JsonbProperty;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;

/**
 * @test
 * @sources RecordGenericAndNestedCustomizationTest.java
 * @executeClass ee.jakarta.tck.json.bind.customizedmapping.records.RecordGenericAndNestedCustomizationTest
 *
 * Tests that customization annotations on a record's components are honoured
 * when the record is used as a component type of another record or as a generic
 * type parameter (e.g. in a {@link List} or {@link Map}).
 */
public class RecordGenericAndNestedCustomizationTest {

    /**
     * Record whose {@code city} component is renamed via {@code @JsonbProperty("town")},
     * used as the nested component of {@link CustomizedPerson}.
     */
    public record CustomizedAddress(@JsonbProperty("town") String city) {}

    /** Outer record that embeds a {@link CustomizedAddress} as a component. */
    public record CustomizedPerson(String name, CustomizedAddress address) {}

    private final Jsonb jsonb = JsonbBuilder.create();

    /*
     * @testName: testCustomizedNestedRecordSerialization
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.1-5; JSONB:SPEC:JSB-3.7.3-1;
     * JSONB:SPEC:JSB-4.1.2-1
     *
     * @test_Strategy: Assert that @JsonbProperty on a component of a nested record
     * is respected during serialization of the outer record, producing the
     * customized property name in the nested JSON object.
     */
    @Test
    public void testCustomizedNestedRecordSerialization() {
        CustomizedPerson person = new CustomizedPerson("Bob", new CustomizedAddress("Lyon"));
        String jsonString = jsonb.toJson(person);
        assertThat("@JsonbProperty on a nested record component must contain 'name' property.",
                   jsonString, matchesPattern(".*\"name\"\\s*:\\s*\"Bob\".*"));
        assertThat("@JsonbProperty on a nested record component must rename 'city' to 'town' during serialization.",
                   jsonString, matchesPattern(".*\"address\"\\s*:\\s*\\{\\s*\"town\"\\s*:\\s*\"Lyon\"\\s*\\}.*"));
        assertThat("Original component name 'city' must not appear in the serialized output.",
                   jsonString, not(containsString("\"city\"")));
    }

    /*
     * @testName: testCustomizedNestedRecordDeserialization
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.1-5; JSONB:SPEC:JSB-3.7.3-1;
     * JSONB:SPEC:JSB-4.1.2-1
     *
     * @test_Strategy: Assert that @JsonbProperty on a component of a nested record
     * is respected during deserialization of the outer record, mapping the
     * customized JSON property name back to the correct record component.
     */
    @Test
    public void testCustomizedNestedRecordDeserialization() {
        String json = "{\"name\":\"Bob\",\"address\":{\"town\":\"Lyon\"}}";
        CustomizedPerson result = jsonb.fromJson(json, CustomizedPerson.class);
        assertThat("Person name must be 'Bob'.", result.name(), is("Bob"));
        assertThat("Nested CustomizedAddress must not be null.", result.address() != null);
        assertThat("@JsonbProperty 'town' must map to the 'city' component during deserialization.",
                   result.address().city(), is("Lyon"));
    }

    /*
     * @testName: testCustomizedRecordInListSerialization
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.1-5; JSONB:SPEC:JSB-3.17-1;
     * JSONB:SPEC:JSB-3.17.1-1; JSONB:SPEC:JSB-4.1.2-1
     *
     * @test_Strategy: Assert that a List of customized records is serialized to a JSON
     * array whose elements honor @JsonbProperty on the record components.
     */
    @Test
    public void testCustomizedRecordInListSerialization() {
        List<CustomizedAddress> list = List.of(new CustomizedAddress("Lyon"), new CustomizedAddress("Paris"));
        String jsonString = jsonb.toJson(list);
        assertThat("List<CustomizedAddress> must serialize using the @JsonbProperty 'town' name.",
                   jsonString,
                   matchesPattern("\\[\\s*\\{\\s*\"town\"\\s*:\\s*\"Lyon\"\\s*\\}"
                                  + "\\s*,\\s*"
                                  + "\\{\\s*\"town\"\\s*:\\s*\"Paris\"\\s*\\}\\s*\\]"));
        assertThat("Original component name 'city' must not appear in the serialized list output.",
                   jsonString, not(containsString("\"city\"")));
    }

    /*
     * @testName: testCustomizedRecordInListDeserialization
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.1-5; JSONB:SPEC:JSB-3.17-1;
     * JSONB:SPEC:JSB-3.17.1-1; JSONB:SPEC:JSB-3.17.1-15; JSONB:SPEC:JSB-4.1.2-1
     *
     * @test_Strategy: Assert that a JSON array is deserialized into a List of
     * customized records by passing the generic type via java.lang.reflect.Type,
     * mapping customized property names back to the record components.
     */
    @Test
    public void testCustomizedRecordInListDeserialization() {
        String json = "[{\"town\":\"Lyon\"},{\"town\":\"Paris\"}]";
        Type listOfAddressType = new TypeToken<List<CustomizedAddress>>() { }.getType();
        List<CustomizedAddress> result = jsonb.fromJson(json, listOfAddressType);
        assertThat("Deserialized list must contain two elements.", result, hasSize(2));
        assertThat("First element @JsonbProperty 'town' must map to 'city' component.",
                   result.get(0).city(), is("Lyon"));
        assertThat("Second element @JsonbProperty 'town' must map to 'city' component.",
                   result.get(1).city(), is("Paris"));
    }

    /*
     * @testName: testCustomizedRecordInMapSerialization
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.1-5; JSONB:SPEC:JSB-3.17-1;
     * JSONB:SPEC:JSB-3.17.1-1; JSONB:SPEC:JSB-4.1.2-1
     *
     * @test_Strategy: Assert that a Map with a customized record value is serialized
     * to a JSON object whose value honors @JsonbProperty on the record components.
     */
    @Test
    public void testCustomizedRecordInMapSerialization() {
        Map<String, CustomizedAddress> map = new HashMap<>();
        map.put("home", new CustomizedAddress("Lyon"));
        String jsonString = jsonb.toJson(map);
        assertThat("Map<String,CustomizedAddress> must serialize using the @JsonbProperty 'town' name.",
                   jsonString,
                   matchesPattern("\\{\\s*\"home\"\\s*:\\s*\\{\\s*\"town\"\\s*:\\s*\"Lyon\"\\s*\\}\\s*\\}"));
        assertThat("Original component name 'city' must not appear in the serialized map output.",
                   jsonString, not(containsString("\"city\"")));
    }

    /*
     * @testName: testCustomizedRecordInMapDeserialization
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.1-5; JSONB:SPEC:JSB-3.17-1;
     * JSONB:SPEC:JSB-3.17.1-1; JSONB:SPEC:JSB-3.17.1-15; JSONB:SPEC:JSB-4.1.2-1
     *
     * @test_Strategy: Assert that a JSON object is deserialized into a Map of
     * customized records by passing the generic type via java.lang.reflect.Type,
     * mapping customized property names back to the record components.
     */
    @Test
    public void testCustomizedRecordInMapDeserialization() {
        String json = "{\"home\":{\"town\":\"Lyon\"}}";
        Type mapType = new TypeToken<Map<String, CustomizedAddress>>() { }.getType();
        Map<String, CustomizedAddress> result = jsonb.fromJson(json, mapType);
        assertThat("Deserialized map must contain the 'home' key.",
                   result, hasEntry("home", new CustomizedAddress("Lyon")));
    }

}
