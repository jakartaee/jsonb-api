# Jakarta JSON-B — Project

Quick navigation for AI-assisted work on this repository.

## Directories in context

| Directory | Contents |
| --------- | -------- |
| plans/    | Plans for individual tasks that are temporary and discarded after completion |
| releases/ | Plans for a single release which include descriptions of goals, individual tasks, and completion criteria |
| wiki/     | A knowledge center for AI reference that encompases the scope of the entire project |

### Files in the wiki

| File | Contents |
|------|----------|
| [project.md](wiki/project.md) | Project overview, goals, key modules, stakeholders, release workflow |
| [preferences.md](wiki/preferences.md) | Working standards, AI interaction preferences, Jakarta EE conventions |
| [planning.md](wiki/planning.md) | Standard format for release plans: section structure, status values, and work-tracking rules |

## When to reference context

- **Always** read `wiki/project.md` when the task touches the api, spec, or tck — the triple-module constraint matters.
- **Always** read `wiki/preferences.md` before generating code, spec text, or a plan so the output matches project conventions.
- **Always** search `releases/` directory for a corresponding release plan, and read the plan, when you know which release is being worked on.
- **When starting a task** check `plans/` for an existing plan file for the current task before creating a new one.
- **Only when writing or updating a release plan** read `wiki/planning.md` for the required section structure, status values, and work-tracking rules.
- Skip full reads of the `plans`, `releases`, and `wiki` directories; follow links only when relevant to the current task.
