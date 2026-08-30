# Bulk Data Entry Improvement TODO

This document records the recommendations from the Bulk Data Entry interface
audit. The goals are to make imports feel immediate and predictable, keep the
interface responsive with thousands of rows, and make validation and progress
easy to understand.

## Priority 0: Correctness and Immediate Feedback

- [x] Make the table model listen for property changes on every row model.
  Subscribe when a row is added and unsubscribe when it is removed.
- [x] Fire a cell or row update immediately when imported, dirty, or selected
  state changes.
- [ ] Fire a cell or row update immediately when the planned validation state
  changes.
- [x] Ensure all table notifications are delivered on Swing's event dispatch
  thread.
- [x] Fix the collection `CHANGED` handler in
  `AbstractBulkImportTableModel`: it currently uses the row index as the column
  index.
- [x] Fix the `ADDED_ALL` handler so it reports the complete inserted range,
  rather than only the first inserted row.
- [ ] Remove the `Thread.sleep(100)` used when Tab or Enter adds a row. Select
  the new row after its insertion event instead of blocking the UI thread.
- [x] Preserve model/visible order when returning selected rows. Do not derive
  import order from `HashMap` key iteration.
- [ ] Ensure object imports preserve parent-before-child order where required.
- [ ] Replace the current three-state import status with explicit states:
  Ready, Invalid, Queued, Importing, Imported, Modified, and Failed.
- [ ] Rename the **Imported** column to **Status** and render in-progress and
  failed states clearly.

Relevant code:

- `src/main/java/org/tellervo/desktop/bulkdataentry/model/AbstractBulkImportTableModel.java`
- `src/main/java/org/tellervo/desktop/bulkdataentry/model/ImportStatus.java`
- `src/main/java/org/tellervo/desktop/bulkdataentry/AbstractBulkImportView.java`
- `src/main/java/org/tellervo/desktop/bulkdataentry/command/ImportSelectedObjectsCommand.java`
- `src/main/java/org/tellervo/desktop/bulkdataentry/command/ImportSelectedElementsCommand.java`
- `src/main/java/org/tellervo/desktop/bulkdataentry/command/ImportSelectedSamplesCommand.java`

## Priority 1: Large-Row Performance

### Bulk row changes

- [x] Replace the quadratic selection rebuilding in `recreateSelected()` with
  identity-based selection storage and model-order iteration.
- [ ] Add an `addRows(Collection)` model operation that performs one selection
  update and sends one inserted-range event.
- [ ] Update Add Row, spreadsheet paste, database population, project loading,
  and other loaders to use bulk insertion instead of adding rows individually.
- [ ] Apply very large model changes in bounded chunks so the event dispatch
  thread gets regular opportunities to paint and accept input.
- [ ] Temporarily suspend sorting and other expensive listeners while applying
  a bulk change, then restore them and refresh once.
- [ ] Correctly handle `ADDED_ALL`, removal, and replacement events without
  rebuilding unrelated state.

### Spreadsheet paste

- [ ] Read and parse clipboard data once. Remove the current simulation pass
  followed by a second parsing/application pass.
- [ ] Parse and validate clipboard content in a `SwingWorker` or equivalent
  background task.
- [ ] Produce a typed row matrix and validation results off the UI thread, then
  apply only the finished result to the model on the UI thread.
- [ ] Pre-index dictionaries, taxa, projects, objects, and other vocabularies
  by normalized lookup value. Avoid scanning whole lists for every cell.
- [ ] Add genuine paste progress and cancellation.
- [ ] Present paste errors together, with row and column locations, instead of
  stopping after an opaque failure.
- [ ] Make paste a single undoable action.

Relevant code:

- `src/main/java/org/tellervo/desktop/bulkdataentry/JTableSpreadsheetAdapter.java`
- `src/main/java/org/tellervo/desktop/bulkdataentry/command/AddRowCommand.java`
- `src/main/java/org/tellervo/desktop/bulkdataentry/command/PopulateFromDatabaseCommand.java`

### Loading data

- [ ] Replace progress-dialog visibility polling and `Thread.sleep()` loops
  with normal worker lifecycle callbacks.
- [ ] Have database/project loaders publish rows in chunks through the bulk
  insertion API.
- [ ] Keep scrolling, selection, and harmless editing responsive while a load
  is running.
- [ ] Provide Cancel and retain rows that have already loaded successfully.

## Priority 1: Import Pipeline

- [ ] Run network imports in a background worker rather than driving a serial
  modal-dialog workflow from the interface.
- [ ] Replace the per-row modal resource dialog with one persistent, non-modal
  progress panel.
- [ ] Update each row to Queued, Importing, Imported, or Failed as its state
  changes; do not wait until the complete batch finishes.
- [ ] Show overall progress such as `Importing 37 of 250`, including succeeded,
  failed, and remaining counts.
- [ ] Allow cancellation and **Retry failed** without resending successful rows.
- [ ] Keep the table scrollable and readable while importing. Disable editing
  only for rows actively being saved.
- [ ] Aggregate completion errors with row identifiers and provide Copy or
  Export errors actions.
- [ ] Cache validation lookup results, including negative results, for the
  duration of an import.
- [ ] Collect unique sample parent/object and element identifiers first and
  resolve each only once, rather than repeating lookups per row.
- [ ] Use maps for object and element resolution rather than repeated list
  scans.
- [ ] If API semantics permit, use a small bounded request concurrency while
  retaining required hierarchy ordering.
- [ ] Assess a server-side bulk endpoint that accepts batches of approximately
  25-100 records and returns a per-row result. Keep transactions scoped so one
  bad row does not hide the outcome of the rest of the batch.

## Priority 2: Validation and Recovery

- [ ] Validate continuously as cells change instead of waiting for Import.
- [ ] Add a leading **Issues** column with a row-level error indicator.
- [ ] Highlight invalid cells and show a concise message in a tooltip or detail
  panel; do not rely on color alone.
- [ ] Add filters for All, Ready, Errors, Importing, Imported, and Modified.
- [ ] Disable Import when no rows are ready and explain what must be fixed.
- [ ] Label the primary action with its scope, for example
  **Import 238 ready rows**.
- [ ] Support **Retry failed** and preserve the user's corrections after a
  failed attempt.
- [ ] Autosave a local draft and warn before closing when dirty rows remain.
- [ ] Add undo/redo for paste, delete, fill-down, and other bulk edits.
- [ ] Provide a clear empty state with Paste from spreadsheet, Load project,
  Load database, and Add row entry points.

## Priority 2: Interface and Workflow

- [ ] Add a sticky summary showing total, ready, invalid, importing, imported,
  failed, and modified row counts.
- [ ] Present Objects -> Elements -> Samples as a visible workflow while still
  allowing direct navigation between sections.
- [ ] Add counts and error badges to the Objects, Elements, and Samples tabs.
- [ ] Make handoff of newly assigned object IDs to elements, and element IDs to
  samples, visible and immediate.
- [ ] Freeze Status, selection, and primary identifying columns during
  horizontal scrolling.
- [ ] Clarify selection semantics. Prefer standard row selection or provide a
  checkbox header, Select all, Clear selection, and an explicit selected count.
- [ ] Group toolbar commands under labelled areas such as Edit, Add rows, Load
  data, and Columns.
- [ ] Add text labels or clear tooltips and keyboard shortcuts for important
  actions instead of relying on icons alone.
- [ ] Move uncommon or destructive operations, including ODK deletion, into an
  overflow menu and retain confirmation prompts.
- [ ] Replace the overlaying column popup with a searchable checklist.
- [ ] Add column presets such as Minimal, Field collection, Location, and All,
  plus Reset defaults.
- [ ] Continue persisting column visibility and order preferences.
- [ ] Support spreadsheet conveniences including fill-down and efficient
  multi-cell editing.
- [ ] Ensure status and validation cues remain understandable without color
  and are accessible by keyboard and screen readers.

## Priority 2: Help Pane Efficiency

- [ ] Build the column-help lookup map once rather than reconstructing it on
  every selection event.
- [ ] Cache documentation text and user-defined-field descriptions.
- [ ] Refresh help only when the selected column changes; ignore row-only
  selection changes.
- [ ] Debounce help rendering if selection can change repeatedly during
  keyboard navigation or paste.
- [ ] Consider making the help pane collapsible or replacing it with a compact
  contextual inspector.

Relevant code:

- `src/main/java/org/tellervo/desktop/bulkdataentry/AbstractBulkImportView.java`

## Instrumentation and Acceptance Criteria

- [ ] Add timings for clipboard parsing, validation, model application,
  rendering, server validation, and import requests.
- [ ] Add an event-dispatch-thread stall detector during development and tests.
- [ ] Record row count and lookup/network-call count in performance diagnostics.
- [ ] Add repeatable performance tests for 100, 1,000, and 5,000 rows.
- [ ] Add regression tests for row property notifications, inserted ranges,
  selected-row ordering, and status transitions.
- [ ] Verify that status changes appear within one repaint cycle (target under
  100 ms after a server response).
- [ ] Target visible feedback for scrolling, selection, and typing within
  100 ms while background work is active.
- [ ] Target a 1,000-row paste completing and appearing within 1-2 seconds on a
  representative workstation.
- [ ] Keep a 5,000-row table scrollable and editable without UI-thread stalls
  longer than 100 ms.
- [ ] Make cancellation respond within 250 ms, excluding an already-running
  network call that cannot safely be interrupted.
- [ ] Confirm that validation performs no more server lookups than the number
  of unique unresolved parent identifiers.

## Suggested Delivery Order

1. Correct table events, row listeners, status transitions, and selected-row
   ordering.
2. Remove UI-thread sleeps and modal-per-row progress behavior.
3. Introduce efficient selection bookkeeping and bulk row insertion.
4. Rebuild spreadsheet paste around one background parse and indexed lookups.
5. Add the non-modal import progress panel, cancellation, and retry support.
6. Cache or batch validation lookups and evaluate a server bulk endpoint.
7. Add inline validation, filters, workflow counts, toolbar cleanup, column
   presets, undo, and draft recovery.
8. Benchmark against the acceptance criteria and tune chunk and batch sizes
   using measured results.
