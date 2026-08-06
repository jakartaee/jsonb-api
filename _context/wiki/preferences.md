# Working Preferences

## Jakarta EE conventions

These apply to every file touched in this repository.

### Java / API (`api/`)
- All public types, methods, and fields **must have Javadoc**
- Javadoc must describe the contract, not the implementation
- Tag new API with `@since <version>` (e.g. `@since 3.1`)
- Deprecated API must carry `@deprecated` with a migration note; **never delete** deprecated API in the same release it was deprecated
- Backward compatibility is the default — breaking changes require explicit spec committee approval
- Follow existing package structure under `jakarta.json.bind.*`

### Specification (`spec/`)
- AsciiDoc source lives in `spec/src/main/asciidoc/`
- Normative statements use RFC 2119 language (MUST, SHOULD, MAY, etc.)
- Every API change that alters observable behaviour **must** update the spec text
- Spec text and API Javadoc must be consistent — they are both normative

### TCK (`tck/`)
- Every normative spec statement must have at least one TCK test
- Tests are Arquillian + JUnit 5; follow the patterns in existing test classes
- Test class names mirror the behaviour under test (e.g. `CustomizedMappingTest`)
- Add tests in the same PR as the API and spec changes — do not defer coverage

---

## AI interaction preferences

### Plan first
- **Always** produce a written plan and wait for explicit approval before modifying any file
- Plan files go in the `_context/plans/` directory (e.g. `_context/plans/my-feature-plan.md`)
- Plans must list affected modules (api, spec, tck) and why
- When a task is complete, ask the user whether to **delete** the plan file (one-off task) or **archive** it by recording the completed work in the relevant `_context/releases/` file (see `wiki/releases.md` for release plan format)

### Stay concise
- Skip pleasantries and filler phrases — get straight to code, spec text, or analysis
- Use bullet points and tables over prose paragraphs
- Only include context that directly affects a decision

### Ask when uncertain
- If a request is ambiguous (e.g. scope of a spec change, whether to deprecate vs remove), ask before proceeding
- Surface trade-offs explicitly; do not silently pick one path
- Prefer one focused question over several at once

---

## Communication preferences

- Use **plan mode** for design and strategy; switch to **agent mode** only after the plan is approved
- Prefix feedback or questions with `✏️` in plan files to make review edits easy to spot
- Keep pull request descriptions concise: what changed, why, which modules were affected

---

## Additional preferences
<!-- Add any preferences discovered through working sessions here -->
_Nothing captured yet._
