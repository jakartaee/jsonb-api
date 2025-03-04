# TCK Reference Guide for Jakarta JSON Binding

## Preface

This guide describes how to download, install, configure, and run the Technology Compatibility Kit (TCK) used to verify the compatibility of an implementation of Jakarta JSON Binding (JSON-B) 1.0.

The Jakarta JSON Binding TCK is built atop Arquillian, a portable and configurable automated test suite for authoring unit and integration tests in a Jakarta EE environment.

The Jakarta JSON Binding TCK is provided under the Eclipse Public License v2.0.

## Who Should Use This Guide

This guide is for implementors of the Jakarta JSON Binding specification to assist in running the test suite that verifies the compatibility of their implementation.

## About the Jakarta JSON Binding TCK

The Jakarta JSON Binding TCK is designed as a portable, configurable and automated test suite for verifying the compatibility of an implementation of Jakarta JSON-B. The test suite is built atop JUnit and provides a series of extensions that allow runtime packaging and deployment of Jakarta EE artifacts for in-container testing (Arquillian).

Each test class in the suite acts as a deployable unit. The deployable units, or artifacts, are defined in a declarative way using annotations.

The declarative approach allows many of the tests to be executed in a standalone implementation of Jakarta JSON-B, accounting for a boost in developer productivity. However, an implementation is only valid if all tests pass using the in-container execution mode. The standalone mode is merely a developer convenience.

The reason the Jakarta Bean Validation TCK must pass running in a Jakarta EE container is that Jakarta Bean Validation is part of Jakarta EE 8 itself.

### TCK Components

The Jakarta JSON-B TCK includes the following components:

- **The test suite**, which is a collection of JUnit tests

- **TCK documentation** accompanied by release notes identifying updates between versions.

### Passing the Jakarta JSON Binding TCK

In order to pass the Jakarta JSON-B TCK (which is one requirement for becoming a certified Jakarta JSON-B provider), you need to:

- Pass the Jakarta JSON-B signature tests (see Running the Signature Test) asserting the correctness of the JSON-B API used.

- Run and pass the test suite (see Running the TCK test suite). The test must be run within a Jakarta EE 8 container and pass with an unmodified set of tests.

## Appeals process

Any test case (i.e. `@Test` method), test case configuration (e.g. `@Deployment`), test entities, annotations and other resources may be challenged by an appeal.

To initiate a challenge, refer to the [Jakarta EE TCK Process 1.0](https://jakarta.ee/committees/specification/tckprocess/)

## Installation

This chapter explains how to obtain the TCK and supporting software and provides recommendations for how to install/extract it on your system.

### Obtaining the Software

You can obtain a release of the Jakarta JSON-B TCK from Maven Central at the following coordinates:

```xml
<dependency>
  <groupId>jakarta.json.bind</groupId>
  <artifactId>jakarta.json.bind-tck</artifactId>
  <version>${jsonb.version}</version>
  <scope>test</scope>
</dependency>
```

### The TCK Environment

To successfully run the JSON-B TCK, the following components are required:

- [Apache Maven](https://maven.apache.org/install.html)
- Java 8 or newer
- An implementation of JSON-B
- An implementation of CDI

The rest of the TCK software can simply be obtained via Maven dependency described in the "Obtaining the Software" section.

## Configuration

To configure the JSON-B TCK, the following elements are required:

1. A Maven `<dependency>` that pulls in the `jakarta.json.bind-tck` as described in the "Obtaining the Software" section
2. A Maven `<dependency>` that pulls in an implementation of JSON-B
3. A Maven `<dependency>` that pulls in an implementation of CDI (may be from the same depenency as (2))
4. A Maven `<dependency>` that pulls in an appropriate Arquillian container adapter
5. Configuration of the `maven-surefire-plugin` to include running the JSON-B TCK tests:

    ```
    <plugin>
      <artifactId>maven-surefire-plugin</artifactId>
      <version>3.0.0-M4</version>
      <configuration>
        <dependenciesToScan>
          <dependency>jakarta.json.bind:jakarta.json.bind-tck</dependency>
        </dependenciesToScan>
      </configuration>
    </plugin>
    ```
    
6. Any runtime-specific configuration needed in order to enable JSON-B functionality

## Reports

Since tests are run using the `maven-surefire-plugin`, test reports can also be produced and configured using the [`maven-surefire-report-plugin`](https://maven.apache.org/surefire/maven-surefire-report-plugin/)

## Running the TCK tests

Once the TCK has been configured for your runtime and all required software has been installed, the TCKs can be executed by running:

```
$ mvn test
```

All test execution follows standard Maven Surefire practices, such as:
 - Run a single test class with `mvn test -Dtest=MyTestClass
 - Run a singl etest method with `mvn test -Dtest-MyTestClass#myTestMethod
 
## Example setup

See the [Eclipse Yasson repository](https://github.com/eclipse-ee4j/yasson/tree/master/yasson-tck) for a working example of how a JSON-B implementation can consume and run these TCK tests.
