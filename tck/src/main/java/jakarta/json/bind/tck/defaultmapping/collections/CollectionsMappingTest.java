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

package jakarta.json.bind.tck.defaultmapping.collections;

import static org.junit.Assert.fail;

import java.lang.invoke.MethodHandles;
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

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

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

/**
 * @test
 * @sources CollectionsMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.collections.CollectionsMappingTest
 **/
@RunWith(Arquillian.class)
public class CollectionsMappingTest {
    
    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, MethodHandles.lookup().lookupClass().getPackage().getName());
    }
    
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
    try {
      String jsonString = jsonb.toJson(new CollectionContainer() {{ setInstance(Arrays.asList("Test 1", "Test 2")); }});
      if (!jsonString.matches("\\{\\s*\"instance\"\\s*\\:\\s*\\[\\s*\"Test 1\"\\s*,\\s*\"Test 2\"\\s*\\]\\s*\\}")) {
        fail("Failed to get Collection attribute value.");
      }
    } catch (JsonbException x) {
      fail("An exception is not expected when marshalling a class with a Collection attribute.");
    }

    CollectionContainer unmarshalledObject = null;
    try {
      unmarshalledObject = jsonb.fromJson("{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }", CollectionContainer.class);
      if (!unmarshalledObject.getInstance().contains("Test 1") || !unmarshalledObject.getInstance().contains("Test 2")) {
        fail("Failed to marshal object with Collection attribute.");
      }
    } catch (JsonbException x) {
      System.out.append("Received:").println(Arrays.toString(unmarshalledObject.getInstance().toArray()));
      fail("An exception is not expected when unmarshalling a class with a Collection attribute.");
    }
    return; // passed
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
    @SuppressWarnings("serial")
    Map<String, String> instance = new HashMap<String, String>() {
      {
        put("string1", "Test 1");
        put("string2", "Test 2");
      }
    };
    String jsonString = jsonb.toJson(new MapContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"string1\"\\s*:\\s*\"Test 1\"\\s*,\\s*\"string2\"\\s*:\\s*\"Test 2\"\\s*\\}\\s*\\}")) {
      fail("Failed to get Map attribute value.");
    }

    MapContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : { \"string1\" : \"Test 1\", \"string2\" : \"Test 2\" } }",
        MapContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      fail("Failed to marshal object with Map attribute.");
    }

    return; // passed
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
    @SuppressWarnings("serial")
    HashSet<String> instance = new HashSet<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new SetContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      fail("Failed to get Set attribute value.");
    }

    SetContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }", SetContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      fail("Failed to marshal object with Set attribute.");
    }

    return; // passed
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
    @SuppressWarnings("serial")
    HashSet<String> instance = new HashSet<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new HashSetContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      fail("Failed to get HashSet attribute value.");
    }

    HashSetContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }",
        HashSetContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      fail("Failed to marshal object with HashSet attribute.");
    }

    return; // passed
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
    @SuppressWarnings("serial")
    NavigableSet<String> instance = new TreeSet<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new NavigableSetContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      fail("Failed to get NavigableSet attribute value.");
    }

    NavigableSetContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }",
        NavigableSetContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      fail("Failed to marshal object with NavigableSet attribute.");
    }

    return; // passed
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
    @SuppressWarnings("serial")
    SortedSet<String> instance = new TreeSet<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new SortedSetContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      fail("Failed to get SortedSet attribute value.");
    }

    SortedSetContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }",
        SortedSetContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      fail("Failed to marshal object with SortedSet attribute.");
    }

    return; // passed
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
    @SuppressWarnings("serial")
    TreeSet<String> instance = new TreeSet<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new TreeSetContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      fail("Failed to get TreeSet attribute value.");
    }

    TreeSetContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }",
        TreeSetContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      fail("Failed to marshal object with TreeSet attribute.");
    }

    return; // passed
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
    @SuppressWarnings("serial")
    LinkedHashSet<String> instance = new LinkedHashSet<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new LinkedHashSetContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      fail("Failed to get LinkedHashSet attribute value.");
    }

    LinkedHashSetContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }",
        LinkedHashSetContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      fail("Failed to marshal object with LinkedHashSet attribute.");
    }

    return; // passed
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
    @SuppressWarnings("serial")
    HashMap<String, String> instance = new HashMap<String, String>() {
      {
        put("string1", "Test 1");
        put("string2", "Test 2");
      }
    };
    String jsonString = jsonb.toJson(new MapContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"string1\"\\s*:\\s*\"Test 1\"\\s*,\\s*\"string2\"\\s*:\\s*\"Test 2\"\\s*\\}\\s*\\}")) {
      fail("Failed to get HashMap attribute value.");
    }

    MapContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : { \"string1\" : \"Test 1\", \"string2\" : \"Test 2\" } }",
        MapContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      fail("Failed to marshal object with HashMap attribute.");
    }

    return; // passed
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
    @SuppressWarnings("serial")
    NavigableMap<String, String> instance = new TreeMap<String, String>() {
      {
        put("string1", "Test 1");
        put("string2", "Test 2");
      }
    };
    String jsonString = jsonb.toJson(new NavigableMapContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"string1\"\\s*:\\s*\"Test 1\"\\s*,\\s*\"string2\"\\s*:\\s*\"Test 2\"\\s*\\}\\s*\\}")) {
      fail("Failed to get NavigableMap attribute value.");
    }

    NavigableMapContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : { \"string1\" : \"Test 1\", \"string2\" : \"Test 2\" } }",
        NavigableMapContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      fail("Failed to marshal object with NavigableMap attribute.");
    }

    return; // passed
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
    @SuppressWarnings("serial")
    SortedMap<String, String> instance = new TreeMap<String, String>() {
      {
        put("string1", "Test 1");
        put("string2", "Test 2");
      }
    };
    String jsonString = jsonb.toJson(new SortedMapContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"string1\"\\s*:\\s*\"Test 1\"\\s*,\\s*\"string2\"\\s*:\\s*\"Test 2\"\\s*\\}\\s*\\}")) {
      fail("Failed to get SortedMap attribute value.");
    }

    SortedMapContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : { \"string1\" : \"Test 1\", \"string2\" : \"Test 2\" } }",
        SortedMapContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      fail("Failed to marshal object with SortedMap attribute.");
    }

    return; // passed
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
    @SuppressWarnings("serial")
    TreeMap<String, String> instance = new TreeMap<String, String>() {
      {
        put("string1", "Test 1");
        put("string2", "Test 2");
      }
    };
    String jsonString = jsonb.toJson(new TreeMapContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"string1\"\\s*:\\s*\"Test 1\"\\s*,\\s*\"string2\"\\s*:\\s*\"Test 2\"\\s*\\}\\s*\\}")) {
      fail("Failed to get TreeMap attribute value.");
    }

    TreeMapContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : { \"string1\" : \"Test 1\", \"string2\" : \"Test 2\" } }",
        TreeMapContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      fail("Failed to marshal object with TreeMap attribute.");
    }

    return; // passed
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
    @SuppressWarnings("serial")
    LinkedHashMap<String, String> instance = new LinkedHashMap<String, String>() {
      {
        put("string1", "Test 1");
        put("string2", "Test 2");
      }
    };
    String jsonString = jsonb.toJson(new LinkedHashMapContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"string1\"\\s*:\\s*\"Test 1\"\\s*,\\s*\"string2\"\\s*:\\s*\"Test 2\"\\s*\\}\\s*\\}")) {
      fail("Failed to get LinkedHashMap attribute value.");
    }

    LinkedHashMapContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : { \"string1\" : \"Test 1\", \"string2\" : \"Test 2\" } }",
        LinkedHashMapContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      fail("Failed to marshal object with LinkedHashMap attribute.");
    }

    return; // passed
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
    @SuppressWarnings("serial")
    List<String> instance = new ArrayList<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new ListContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      fail("Failed to get List attribute value.");
    }

    ListContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }", ListContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      fail("Failed to marshal object with List attribute.");
    }

    return; // passed
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
    @SuppressWarnings("serial")
    ArrayList<String> instance = new ArrayList<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new ArrayListContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      fail("Failed to get ArrayList attribute value.");
    }

    ArrayListContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }",
        ArrayListContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      fail("Failed to marshal object with ArrayList attribute.");
    }

    return; // passed
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
    @SuppressWarnings("serial")
    LinkedList<String> instance = new LinkedList<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new LinkedListContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      fail("Failed to get LinkedList attribute value.");
    }

    LinkedListContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }",
        LinkedListContainer.class);
    if (!instance.equals(unmarshalledObject.getInstance())) {
      fail("Failed to marshal object with LinkedList attribute.");
    }

    return; // passed
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
    @SuppressWarnings("serial")
    Deque<String> instance = new ArrayDeque<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new DequeContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      fail("Failed to get Deque attribute value.");
    }

    DequeContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }", DequeContainer.class);
    if (!Arrays.asList(instance.toArray())
        .equals(Arrays.asList(unmarshalledObject.getInstance().toArray()))) {
      fail("Failed to marshal object with Deque attribute.");
    }

    return; // passed
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
    @SuppressWarnings("serial")
    ArrayDeque<String> instance = new ArrayDeque<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new ArrayDequeContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      fail("Failed to get ArrayDeque attribute value.");
    }

    ArrayDequeContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }",
        ArrayDequeContainer.class);
    if (!Arrays.asList(instance.toArray())
        .equals(Arrays.asList(unmarshalledObject.getInstance().toArray()))) {
      fail("Failed to marshal object with ArrayDeque attribute.");
    }

    return; // passed
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
    @SuppressWarnings("serial")
    Queue<String> instance = new LinkedList<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new QueueContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      fail("Failed to get Queue attribute value.");
    }

    QueueContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }", QueueContainer.class);
    if (!Arrays.asList(instance.toArray())
        .equals(Arrays.asList(unmarshalledObject.getInstance().toArray()))) {
      fail("Failed to marshal object with Queue attribute.");
    }

    return; // passed
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
    @SuppressWarnings("serial")
    PriorityQueue<String> instance = new PriorityQueue<String>() {
      {
        add("Test 1");
        add("Test 2");
      }
    };
    String jsonString = jsonb.toJson(new PriorityQueueContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test \\d\"\\s*,\\s*\"Test \\d\"\\s*\\]\\s*\\}")) {
      fail("Failed to get PriorityQueue attribute value.");
    }

    PriorityQueueContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", \"Test 2\" ] }",
        PriorityQueueContainer.class);
    if (!Arrays.asList(instance.toArray())
        .equals(Arrays.asList(unmarshalledObject.getInstance().toArray()))) {
      fail("Failed to marshal object with PriorityQueue attribute.");
    }

    return; // passed
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
  @Ignore("See: https://github.com/eclipse-ee4j/jakartaee-tck/issues/103")
  public void testEnumSet() {
    EnumSet<EnumSetContainer.Enum> instance = EnumSet
        .allOf(EnumSetContainer.Enum.class);
    String jsonString = jsonb.toJson(new EnumSetContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"ONE\"\\s*,\\s*\"TWO\"\\s*\\]\\s*\\}")) {
      fail("Failed to get EnumSet attribute value.");
    }

    try {
      jsonb.fromJson("{ \"instance\" : [ \"ONE\", \"TWO\" ] }",
          EnumSetContainer.class);
      fail(
          "An exception is expected when unmarshalling a class with an EnumSet attribute.");
    } catch (JsonbException x) {
      return; // passed
    }
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
  @Ignore("See: https://github.com/eclipse-ee4j/jakartaee-tck/issues/103")
  public void testEnumMap() {
    @SuppressWarnings("serial")
    EnumMap<EnumSetContainer.Enum, String> instance = new EnumMap<EnumSetContainer.Enum, String>(
        EnumSetContainer.Enum.class) {
      {
        put(EnumSetContainer.Enum.ONE, "Test 1");
        put(EnumSetContainer.Enum.TWO, "Test 2");
      }
    };
    String jsonString = jsonb.toJson(new EnumMapContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"ONE\"\\s*:\\s*\"Test 1\"\\s*,\\s*\"TWO\"\\s*:\\s*\"Test 2\"\\s*\\}\\s*\\}")) {
      fail("Failed to get EnumMap attribute value.");
    }

    try {
      jsonb.fromJson(
          "{ \"instance\" : { \"ONE\" : \"Test 1\", \"TWO\" : \"Test 2\" } }",
          EnumMapContainer.class);
      fail(
          "An exception is expected when unmarshalling a class with an EnumMap attribute.");
    } catch (JsonbException x) {
      return; // passed
    }
  }
}
