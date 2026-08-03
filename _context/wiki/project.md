# Project Overview — Jakarta JSON Binding (JSON-B)

## What this project is

Jakarta JSON Binding (JSON-B) is a standard binding layer for the Jakarta EE platform that converts Java objects to and from JSON documents. It defines a set of Java interfaces, annotations, and an SPI that any compliant implementation must satisfy.

- **Repository:** <https://github.com/jakartaee/jsonb-api>
- **Java package root:** `jakarta.json.bind`
- **Current development version:** 3.1.0-SNAPSHOT
- **License:** EPL 2.0 / GPL 2.0 with Classpath Exception

---

## Main goals and objectives

Goals are organised into **releases**. Each release targets a defined subset of objectives. Capture active release goals below as they are established.

### Standing goals (every release)
- Evolve the specification and API surface in a backward-compatible way
- Keep TCK coverage complete — every normative statement must have at least one test
- Keep spec text and API Javadoc in sync
- Coordinate compatible implementation alignment with Eclipse Yasson before a release

### Active release goals

Are found in the `_context/releases/` directory and are organized into individual plans based on the release version. For example the plan for release version 3.1.0 would be in the `_context/releases/3.1.0.md` file.

---

## Key stakeholders and collaborators

| Stakeholder | Role |
|-------------|------|
| Spec leads (Nathan Rauh & Dmitry Kornilov) | Final authority on specification decisions |
| Fellow spec committers | Co-authors of API changes and spec text |
| Eclipse Yasson team | Eclipse managed compatible implementation |
| Jakarta EE platform committee | Coordinates inclusion of JSON-B releases into platform releases |
| Community contributors | Submit issues and pull requests via GitHub |

---

## Repository modules

Every significant change typically touches **all three** modules. They must stay in sync.

| Module | Path | Purpose |
|--------|------|---------|
| **API** | [`api/`](../api/) | Java interfaces, annotations, and SPI classes; the public contract |
| **Specification** | [`spec/`](../spec/) | Normative AsciiDoc document; must reflect every API change |
| **TCK** | [`tck/`](../tck/) | Technology Compatibility Kit; Arquillian + JUnit 5 tests that verify conformance |
| TCK distribution | [`tck-dist/`](../tck-dist/) | Packaged TCK for third-party implementors |
| Docs | [`docs/`](../docs/) | Generated documentation using Asciidoctor |

### Change rule
> An API change is not complete until matching spec text and TCK coverage exist.

---

## Important workflows

### Feature / change workflow
1. Open or link a GitHub issue
2. Draft API change in `api/` with full Javadoc
3. Write or update the corresponding spec chapter in `spec/`
4. Add TCK tests in `tck/` that cover the new normative behaviour
5. Coordinate with implementation teams to confirm implementability
6. Open a pull request; get spec committer review
7. Merge after approval; tag release when the milestone is complete

### Build
```bash
mvn clean install        # full build: API + spec + TCK
mvn -pl api clean install  # API module only
mvn -pl tck clean install  # TCK module only
```

---

## Additional context
<!-- Add architecture decisions, recurring patterns, or insights from past tasks here -->
_Nothing captured yet._
