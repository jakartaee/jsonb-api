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

package jakarta.json.bind.tck.defaultmapping.collections;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.json.bind.tck.defaultmapping.collections.model.ArrayDequeContainer;
import jakarta.json.bind.tck.defaultmapping.collections.model.ArrayListContainer;
import jakarta.json.bind.tck.defaultmapping.collections.model.CollectionContainer;
import jakarta.json.bind.tck.defaultmapping.collections.model.DequeContainer;
import jakarta.json.bind.tck.defaultmapping.collections.model.EnumMapContainer;
import jakarta.json.bind.tck.defaultmapping.collections.model.EnumSetContainer;
import jakarta.json.bind.tck.defaultmapping.collections.model.HashSetContainer;
import jakarta.json.bind.tck.defaultmapping.collections.model.LinkedHashMapContainer;
import jakarta.json.bind.tck.defaultmapping.collections.model.LinkedHashSetContainer;
import jakarta.json.bind.tck.defaultmapping.collections.model.LinkedListContainer;
import jakarta.json.bind.tck.defaultmapping.collections.model.ListContainer;
import jakarta.json.bind.tck.defaultmapping.collections.model.MapContainer;
import jakarta.json.bind.tck.defaultmapping.collections.model.NavigableMapContainer;
import jakarta.json.bind.tck.defaultmapping.collections.model.NavigableSetContainer;
import jakarta.json.bind.tck.defaultmapping.collections.model.PriorityQueueContainer;
import jakarta.json.bind.tck.defaultmapping.collections.model.QueueContainer;
import jakarta.json.bind.tck.defaultmapping.collections.model.SetContainer;
import jakarta.json.bind.tck.defaultmapping.collections.model.SortedMapContainer;
import jakarta.json.bind.tck.defaultmapping.collections.model.SortedSetContainer;
import jakarta.json.bind.tck.defaultmapping.collections.model.TreeMapContainer;
import jakarta.json.bind.tck.defaultmapping.collections.model.TreeSetContainer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @test
 * @sources CollectionsMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.collections.CollectionsMappingTest
 **/
public class CollectionsMappingTest {

    private static final String COLLECTION_JSON = "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }";
    private static final String MAP_JSON = "{ \"instance\" : { \"string1\" : \"Test 1\", \"string2\" : \"Test 2\" } }";

    private static final Pattern COLLECTION_PATTERN =
            Pattern.compile("\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}");
    private static final Pattern MAP_PATTERN =
            Pattern.compile("\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"string1\"\\s*:\\s*\"Test 1\"\\s*,"
                                    + "\\s*\"string2\"\\s*:\\s*\"Test 2\"\\s*\\}\\s*\\}");

    private final Jsonb jsonb = JsonbBuilder.create();

    /*
     * @testName: testCollection
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.10-3;
     * JSONB:SPEC:JSB-3.11-1; JSONB:SPEC:JSB-3.11-2
     *
     * @test_Strategy: Assert that Collection interface is correctly marshalled
     * using the runtime type and reports an error when unmarshalling
     */
    @Test
    public void testCollection() {
        String jsonString = assertDoesNotThrow(() -> jsonb.toJson(new CollectionContainer() {{
                                                   setInstance(Arrays.asList("Test 1", "Test 2"));
                                               }}),
                                               "An exception is not expected when marshalling a class with a Collection "
                                                       + "attribute.");

        assertThat("Failed to get Collection attribute value.",
                   jsonString,
                   matchesPattern("\\{\\s*\"instance\"\\s*\\:\\s*\\[\\s*\"Test 1\"\\s*,\\s*\"Test 2\"\\s*\\]\\s*\\}"));

        CollectionContainer unmarshall = assertDoesNotThrow(() -> jsonb.fromJson(COLLECTION_JSON, CollectionContainer.class),
                                                            "An exception is not expected when unmarshalling a class with a "
                                                                    + "Collection attribute.");

        assertThat("Failed to marshal object with Collection attribute.",
                   unmarshall.getInstance(), contains("Test 1", "Test 2"));
    }

    /*
     * @testName: testMap
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
     * JSONB:SPEC:JSB-3.11-2
     *
     * @test_Strategy: Assert that Map interface is successfully marshalled and
     * unmarshalled
     */
    @Test
    public void testMap() {
        Map<String, String> instance = new HashMap<String, String>() {{
            put("string1", "Test 1");
            put("string2", "Test 2");
        }};
        String jsonString = jsonb.toJson(new MapContainer() {{
            setInstance(instance);
        }});
        assertThat("Failed to get Map attribute value.", jsonString, matchesPattern(MAP_PATTERN));

        MapContainer unmarshalledObject = jsonb.fromJson(MAP_JSON, MapContainer.class);
        assertThat("Failed to marshal object with Map attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    /*
     * @testName: testSet
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
     * JSONB:SPEC:JSB-3.11-2
     *
     * @test_Strategy: Assert that Set interface is successfully marshalled and
     * unmarshalled
     */
    @Test
    public void testSet() {
        HashSet<String> instance = new HashSet<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new SetContainer() {{
            setInstance(instance);
        }});
        assertThat("Failed to get Set attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        SetContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, SetContainer.class);
        assertThat("Failed to marshal object with Set attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    /*
     * @testName: testHashSet
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
     * JSONB:SPEC:JSB-3.11-2
     *
     * @test_Strategy: Assert that HashSet is successfully marshalled and
     * unmarshalled
     */
    @Test
    public void testHashSet() {
        HashSet<String> instance = new HashSet<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new HashSetContainer() {{
            setInstance(instance);
        }});
        assertThat("Failed to get HashSet attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        HashSetContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, HashSetContainer.class);
        assertThat("Failed to marshal object with HashSet attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    /*
     * @testName: testNavigableSet
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
     * JSONB:SPEC:JSB-3.11-2
     *
     * @test_Strategy: Assert that NavigableSet is successfully marshalled and
     * unmarshalled
     */
    @Test
    public void testNavigableSet() {
        NavigableSet<String> instance = new TreeSet<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new NavigableSetContainer() {{
            setInstance(instance);
        }});
        assertThat("Failed to get NavigableSet attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        NavigableSetContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, NavigableSetContainer.class);
        assertThat("Failed to marshal object with NavigableSet attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    /*
     * @testName: testSortedSet
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
     * JSONB:SPEC:JSB-3.11-2
     *
     * @test_Strategy: Assert that SortedSet interface is successfully marshalled
     * and unmarshalled
     */
    @Test
    public void testSortedSet() {
        SortedSet<String> instance = new TreeSet<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new SortedSetContainer() {{
            setInstance(instance);
        }});

        assertThat("Failed to get SortedSet attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        SortedSetContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, SortedSetContainer.class);
        assertThat("Failed to marshal object with SortedSet attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    /*
     * @testName: testTreeSet
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
     * JSONB:SPEC:JSB-3.11-2
     *
     * @test_Strategy: Assert that TreeSet is successfully marshalled and
     * unmarshalled
     */
    @Test
    public void testTreeSet() {
        TreeSet<String> instance = new TreeSet<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new TreeSetContainer() {{
            setInstance(instance);
        }});

        assertThat("Failed to get TreeSet attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        TreeSetContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, TreeSetContainer.class);
        assertThat("Failed to marshal object with TreeSet attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    /*
     * @testName: testLinkedHashSet
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
     * JSONB:SPEC:JSB-3.11-2
     *
     * @test_Strategy: Assert that LinkedHashSet is successfully marshalled and
     * unmarshalled
     */
    @Test
    public void testLinkedHashSet() {
        LinkedHashSet<String> instance = new LinkedHashSet<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new LinkedHashSetContainer() {{
            setInstance(instance);
        }});

        assertThat("Failed to get LinkedHashSet attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        LinkedHashSetContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, LinkedHashSetContainer.class);
        assertThat("Failed to marshal object with LinkedHashSet attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    /*
     * @testName: testHashMap
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
     * JSONB:SPEC:JSB-3.11-2
     *
     * @test_Strategy: Assert that HashMap is successfully marshalled and
     * unmarshalled
     */
    @Test
    public void testHashMap() {
        HashMap<String, String> instance = new HashMap<String, String>() {{
            put("string1", "Test 1");
            put("string2", "Test 2");
        }};
        String jsonString = jsonb.toJson(new MapContainer() {{
            setInstance(instance);
        }});

        assertThat("Failed to get HashMap attribute value.", jsonString, matchesPattern(MAP_PATTERN));

        MapContainer unmarshalledObject = jsonb.fromJson(MAP_JSON, MapContainer.class);
        assertThat("Failed to marshal object with HashMap attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    /*
     * @testName: testNavigableMap
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
     * JSONB:SPEC:JSB-3.11-2
     *
     * @test_Strategy: Assert that NavigableMap interface is successfully
     * marshalled and unmarshalled
     */
    @Test
    public void testNavigableMap() {
        NavigableMap<String, String> instance = new TreeMap<String, String>() {{
            put("string1", "Test 1");
            put("string2", "Test 2");
        }};
        String jsonString = jsonb.toJson(new NavigableMapContainer() {{
            setInstance(instance);
        }});

        assertThat("Failed to get NavigableMap attribute value.", jsonString, matchesPattern(MAP_PATTERN));

        NavigableMapContainer unmarshalledObject = jsonb.fromJson(MAP_JSON, NavigableMapContainer.class);
        assertThat("Failed to marshal object with NavigableMap attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    /*
     * @testName: testSortedMap
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
     * JSONB:SPEC:JSB-3.11-2
     *
     * @test_Strategy: Assert that SortedMap interface is successfully marshalled
     * and unmarshalled
     */
    @Test
    public void testSortedMap() {
        SortedMap<String, String> instance = new TreeMap<String, String>() {{
            put("string1", "Test 1");
            put("string2", "Test 2");
        }};
        String jsonString = jsonb.toJson(new SortedMapContainer() {{
            setInstance(instance);
        }});
        assertThat("Failed to get SortedMap attribute value.", jsonString, matchesPattern(MAP_PATTERN));

        SortedMapContainer unmarshalledObject = jsonb.fromJson(MAP_JSON, SortedMapContainer.class);
        assertThat("Failed to marshal object with SortedMap attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    /*
     * @testName: testTreeMap
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
     * JSONB:SPEC:JSB-3.11-2
     *
     * @test_Strategy: Assert that TreeMap is successfully marshalled and
     * unmarshalled
     */
    @Test
    public void testTreeMap() {
        TreeMap<String, String> instance = new TreeMap<String, String>() {{
            put("string1", "Test 1");
            put("string2", "Test 2");
        }};
        String jsonString = jsonb.toJson(new TreeMapContainer() {{
            setInstance(instance);
        }});

        assertThat("Failed to get TreeMap attribute value.", jsonString, matchesPattern(MAP_PATTERN));

        TreeMapContainer unmarshalledObject = jsonb.fromJson(MAP_JSON, TreeMapContainer.class);
        assertThat("Failed to marshal object with TreeMap attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    /*
     * @testName: testLinkedHashMap
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
     * JSONB:SPEC:JSB-3.11-2
     *
     * @test_Strategy: Assert that LinkedHashMap is successfully marshalled and
     * unmarshalled
     */
    @Test
    public void testLinkedHashMap() {
        LinkedHashMap<String, String> instance = new LinkedHashMap<String, String>() {{
            put("string1", "Test 1");
            put("string2", "Test 2");
        }};
        String jsonString = jsonb.toJson(new LinkedHashMapContainer() {{
            setInstance(instance);
        }});

        assertThat("Failed to get LinkedHashMap attribute value.", jsonString, matchesPattern(MAP_PATTERN));

        LinkedHashMapContainer unmarshalledObject = jsonb.fromJson(MAP_JSON, LinkedHashMapContainer.class);
        assertThat("Failed to marshal object with LinkedHashMap attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    /*
     * @testName: testList
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
     * JSONB:SPEC:JSB-3.11-2
     *
     * @test_Strategy: Assert that List interface is successfully marshalled and
     * unmarshalled
     */
    @Test
    public void testList() {
        List<String> instance = new ArrayList<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new ListContainer() {{
            setInstance(instance);
        }});

        assertThat("Failed to get List attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        ListContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, ListContainer.class);
        assertThat("Failed to marshal object with List attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    /*
     * @testName: testArrayList
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
     * JSONB:SPEC:JSB-3.11-2
     *
     * @test_Strategy: Assert that ArrayList is successfully marshalled and
     * unmarshalled
     */
    @Test
    public void testArrayList() {
        ArrayList<String> instance = new ArrayList<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new ArrayListContainer() {{
            setInstance(instance);
        }});

        assertThat("Failed to get ArrayList attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        ArrayListContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, ArrayListContainer.class);
        assertThat("Failed to marshal object with ArrayList attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    /*
     * @testName: testLinkedList
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
     * JSONB:SPEC:JSB-3.11-2
     *
     * @test_Strategy: Assert that LinkedList is successfully marshalled and
     * unmarshalled
     */
    @Test
    public void testLinkedList() {
        LinkedList<String> instance = new LinkedList<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new LinkedListContainer() {{
            setInstance(instance);
        }});

        assertThat("Failed to get LinkedList attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        LinkedListContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, LinkedListContainer.class);
        assertThat("Failed to marshal object with LinkedList attribute.", unmarshalledObject.getInstance(), is(instance));
    }

    /*
     * @testName: testDeque
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
     * JSONB:SPEC:JSB-3.11-2
     *
     * @test_Strategy: Assert that Deque interface is successfully marshalled and
     * unmarshalled
     */
    @Test
    public void testDeque() {
        Deque<String> instance = new ArrayDeque<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new DequeContainer() {{
            setInstance(instance);
        }});

        assertThat("Failed to get Deque attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        DequeContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, DequeContainer.class);
        assertThat("Failed to marshal object with Deque attribute.",
                   unmarshalledObject.getInstance(), contains(instance.toArray()));
    }

    /*
     * @testName: testArrayDeque
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
     * JSONB:SPEC:JSB-3.11-2
     *
     * @test_Strategy: Assert that ArrayDeque is successfully marshalled and
     * unmarshalled
     */
    @Test
    public void testArrayDeque() {
        ArrayDeque<String> instance = new ArrayDeque<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new ArrayDequeContainer() {{
            setInstance(instance);
        }});

        assertThat("Failed to get ArrayDeque attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        ArrayDequeContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, ArrayDequeContainer.class);
        assertThat("Failed to marshal object with ArrayDeque attribute.",
                   unmarshalledObject.getInstance(), contains(instance.toArray()));
    }

    /*
     * @testName: testQueue
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
     * JSONB:SPEC:JSB-3.11-2
     *
     * @test_Strategy: Assert that Queue interface is successfully marshalled and
     * unmarshalled
     */
    @Test
    public void testQueue() {
        Queue<String> instance = new LinkedList<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new QueueContainer() {{
            setInstance(instance);
        }});
        assertThat("Failed to get Queue attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        QueueContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, QueueContainer.class);
        assertThat("Failed to marshal object with Queue attribute.",
                   unmarshalledObject.getInstance(), contains(instance.toArray()));
    }

    /*
     * @testName: testPriorityQueue
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
     * JSONB:SPEC:JSB-3.11-2
     *
     * @test_Strategy: Assert that PriorityQueue is successfully marshalled and
     * unmarshalled
     */
    @Test
    public void testPriorityQueue() {
        PriorityQueue<String> instance = new PriorityQueue<String>() {{
            add("Test 1");
            add("Test 2");
        }};
        String jsonString = jsonb.toJson(new PriorityQueueContainer() {{
            setInstance(instance);
        }});
        assertThat("Failed to get PriorityQueue attribute value.", jsonString, matchesPattern(COLLECTION_PATTERN));

        PriorityQueueContainer unmarshalledObject = jsonb.fromJson(COLLECTION_JSON, PriorityQueueContainer.class);
        assertThat("Failed to marshal object with PriorityQueue attribute.",
                   unmarshalledObject.getInstance(), contains(instance.toArray()));
    }

    /*
     * @testName: testEnumSet
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
     * JSONB:SPEC:JSB-3.11-2
     *
     * @test_Strategy: Assert that EnumSet is successfully marshalled and reports
     * an error when unmarshalling
     */
    @Test
    @Disabled("See: https://github.com/eclipse-ee4j/jakartaee-tck/issues/103")
    public void testEnumSet() {
        EnumSet<EnumSetContainer.Enum> instance = EnumSet.allOf(EnumSetContainer.Enum.class);
        String jsonString = jsonb.toJson(new EnumSetContainer() {{
            setInstance(instance);
        }});
        assertThat("Failed to get EnumSet attribute value.",
                   jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"ONE\"\\s*,\\s*\"TWO\"\\s*\\]\\s*\\}"));

        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"instance\" : [ \"ONE\", \"TWO\" ] }", EnumSetContainer.class),
                     "An exception is expected when unmarshalling a class with an EnumSet attribute.");
    }

    /*
     * @testName: testEnumMap
     *
     * @assertion_ids: JSONB:SPEC:JSB-3.10-1; JSONB:SPEC:JSB-3.11-1;
     * JSONB:SPEC:JSB-3.11-2
     *
     * @test_Strategy: Assert that EnumMap is successfully marshalled and reports
     * an error when unmarshalling
     */
    @Test
    @Disabled("See: https://github.com/eclipse-ee4j/jakartaee-tck/issues/103")
    public void testEnumMap() {
        EnumMap<EnumSetContainer.Enum, String> instance =
                new EnumMap<EnumSetContainer.Enum, String>(EnumSetContainer.Enum.class) {{
            put(EnumSetContainer.Enum.ONE, "Test 1");
            put(EnumSetContainer.Enum.TWO, "Test 2");
        }};
        String jsonString = jsonb.toJson(new EnumMapContainer() {{
            setInstance(instance);
        }});
        assertThat("Failed to get EnumMap attribute value.",
                   jsonString, matchesPattern("\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"ONE\"\\s*:\\s*\"Test 1\"\\s*,"
                                                      + "\\s*\"TWO\"\\s*:\\s*\"Test 2\"\\s*\\}\\s*\\}"));

        assertThrows(JsonbException.class,
                     () -> jsonb.fromJson("{ \"instance\" : { \"ONE\" : \"Test 1\", \"TWO\" : \"Test 2\" } }",
                                          EnumSetContainer.class),
                     "An exception is expected when unmarshalling a class with an EnumMap attribute.");
    }
}
