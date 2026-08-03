# Release Plan Format

This document defines the standard format for release plan files in `_context/releases/`.
Read this file whenever you are writing, updating, or interpreting a **release plan**.

> **Scope:** This file covers release planning only — goals, FRs, acceptance criteria, and
> progress tracking for a named version. It does **not** apply to individual task plans stored
> in `_context/plans/`. For task-level planning, refer to `wiki/preferences.md` and the task
> context directly.

---

## File location and naming

Release plans live at `_context/releases/<version>.md` (e.g. `3.1.0.md`).
One file per release. Do not create sub-files or split a plan across multiple documents.

---

## Standard sections

Every release plan must contain the sections below in this order.

### Section 0 — Work-Item Status *(required)*

A tracking table that an AI agent reads **before any other section** to determine what
work remains. It must appear immediately after the title block (target platform, milestone
tracker, public release page).

**Format:**

```markdown
## 0. Work-Item Status

> **AI agents: read this table first.** Each row corresponds to a functional requirement
> defined in section 4. Before acting on any FR, check its `Status` cell.
> Skip items marked `✅ DONE` — they are complete and must not be reopened or re-implemented.
> Only act on items marked `⬜ TODO` or `🔄 IN PROGRESS`.

| FR | Title | Modules | Status |
|----|-------|---------|--------|
| FR-1 | <title> | `api/`, `spec/`, `tck/` | ⬜ TODO |
```

**Status values — use exactly these strings:**

| Symbol | Meaning |
|--------|---------|
| `⬜ TODO` | Work has not started. Agent may begin this item. |
| `🔄 IN PROGRESS` | Work has started but is not complete. Agent may continue this item. |
| `✅ DONE` | Work is complete. Agent must skip this item entirely. |

**Rules for agents:**
- Read Section 0 first, every time, before reading the rest of the plan.
- Never change a `✅ DONE` row back to any other status.
- When completing an FR, update its status to `✅ DONE` in this table before finishing the task.
- When starting an FR, update its status to `🔄 IN PROGRESS` to signal active work.

---

### Section 1 — Goals and Expected Outcome *(required)*

A short numbered list of the high-level objectives for the release. Written for humans.
Each goal should be one to three sentences. This section does not change once the plan is approved.

---

### Section 2 — Target Users and Scenarios *(required)*

A Markdown table with columns `User` and `Scenario`. Describes who benefits and how.
Does not change once the plan is approved.

---

### Section 3 — Scope and Out of Scope *(required)*

Two subsections: **In scope** (bulleted list) and **Out of scope** (bulleted list).
Each in-scope item should link to the relevant GitHub issue or pull request where one exists.
Does not change once the plan is approved.

---

### Section 4 — Functional Requirements *(required)*

One `###` subsection per FR, ID formatted as `FR-N`. Each FR subsection contains:

- A plain-English description of the externally observable behavior.
- `**Linked issue:**` — GitHub issue or PR number, linked.
- `**Affected modules:**` — comma-separated list from `api/`, `spec/`, `tck/`, `docs/`.

FR IDs must match the rows in the Section 0 status table exactly.

---

### Section 5 — Non-Functional Requirements *(required)*

A Markdown table with columns `ID`, `Requirement`, and `Measurable Threshold`.
IDs formatted as `NFR-N`. Does not change once the plan is approved.

---

### Section 6 — Acceptance Criteria *(required)*

One or more `###` subsections per FR, formatted as `AC-FR-Na` (first criterion for FR-N),
`AC-FR-Nb` (second criterion), etc. Each criterion uses Given / When / Then format.
Does not change once the plan is approved.

---

### Section 7 — Assumptions and Open Questions *(required)*

Free-form. Record unresolved decisions here during drafting. Mark each resolved item so the
history is preserved. When all questions are resolved, replace the body with:
`All outstanding questions have been resolved.`

---

## How to track progress

1. **When starting work on an FR** — set its status to `🔄 IN PROGRESS` in the Section 0 table.
2. **When work on an FR is fully complete** (API change merged, spec text merged, TCK tests merged) — set its status to `✅ DONE`.
3. **Never** edit Section 0 for any other reason. All other sections are written once and remain stable.
4. The release is complete when every row in Section 0 shows `✅ DONE`.

---

## Creating a new release plan

1. Copy the section structure above.
2. Fill in the title block (target platform, milestone tracker, public release page).
3. Populate the Section 0 table with one row per FR, all starting at `⬜ TODO`.
4. Complete sections 1–7.
5. Get explicit approval from the spec lead before beginning implementation.

---

## Recording completed task work

When a task plan in `_context/plans/` is finished, the agent must ask the user whether to
**delete** the plan file or **archive** it into the release plan. If archiving:

1. Locate the matching FR row in Section 0 of the relevant `_context/releases/<version>.md`.
2. Set its status to `✅ DONE`.
3. Do not modify any other section — sections 1–7 are written once and remain stable.
4. Delete the task plan file from `_context/plans/` after the release plan is updated.
