Jakarta JSON Binding Specification
===================================

This project generates the Jakarta JSON Binding Specification.

Building
--------

Prerequisites:

* JDK8+
* Maven 3.0.3+

Run the full build:

`mvn clean install`

Generate specification with given status:

`mvn clean install -Dstatus="Final Release"`

Locate the html files:
- target/generated-docs/jsonb-spec-XXX.html

Locate the PDF files:
- target/generated-docs/jsonb-spec-XXX.pdf
