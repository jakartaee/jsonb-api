# Jakarta JSON Binding (JSON-B)

[![Maven Central](https://img.shields.io/maven-central/v/jakarta.json.bind/jakarta.json.bind-api.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:jakarta.json.bind%20a:jakarta.json.bind-api)
[![License](https://img.shields.io/badge/License-EPL%202.0-green.svg)](https://opensource.org/licenses/EPL-1.0)

JSON-B is a standard binding layer for converting Java objects to/from JSON messages. It defines a default mapping algorithm for converting existing Java classes to JSON, while enabling developers to customize the mapping process through the use of Java annotations.

## Get it

### Maven
```xml
<!-- https://mvnrepository.com/artifact/jakarta.json.bind/jakarta.json.bind-api -->
<dependency>
    <groupId>jakarta.json.bind</groupId>
    <artifactId>jakarta.json.bind-api</artifactId>
    <version>1.0.1</version>
</dependency>
```

## Mapping a simple class

Suppose we have the following Java object, which we want to represent with JSON data:
```java
public class User {
  public long id;
  public String name;
  public int age;
}
```

Using the default mapping, this class can be serialized (as-is) to a JSON string:
```java
Jsonb jsonb = JsonbBuilder.create();

User bob = new User();
bob.id = 1234;
bob.name = "Bob";
bob.age = 42;

String bobJson = jsonb.toJson(bob);
System.out.println(bobJson); // {"id":1234,"name":Bob","age":42}
```

Likewise, JSON data can be deserialized back into Java objects:
```java
Jsonb jsonb = JsonbBuilder.create();

String aliceJson = "{\"id\":5678,\"name\":Alice\",\"age\":42}";
User alice = jsonb.fromJson(aliceJson, User.class);
```

## Links

- Official web site: https://eclipse-ee4j.github.io/jsonb-api
- Jakarta JSON Binding: https://projects.eclipse.org/projects/ee4j.jsonb
- Discussion groups: jsonb-dev@eclipse.org
- Yasson (Reference Implementation): https://github.com/eclipse-ee4j/yasson
