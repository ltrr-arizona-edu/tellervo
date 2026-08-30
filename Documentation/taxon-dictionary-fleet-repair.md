# Taxon Dictionary Fleet Audit and Repair

This runbook detects and repairs the known `tlkptaxon` corruption caused by
rerunning taxon upgrade 6. Detection is read-only. Repair is deliberately a
separate, explicit operation performed on one backed-up database at a time.
The audit script is standalone and does not require the repair SQL or the rest
of the source tree to be installed beside it.

## Known Corruption Signature

The supported repair applies only when all of these are present:

- 864 `taxonid` values beginning with `legacy-`, all with `n/a` labels.
- The three displaced legacy IDs `legacy-5KWC`, `legacy-6R6H5`, and
  `legacy-63SMF`.
- Three falsely repurposed IDs whose correct records also exist under matching
  `legacy-*` IDs. In the complete known dataset these are `5KWC`, `6R6H5`, and
  `63SMF`; the audit detects this relationship rather than relying on labels.

The audit reports this exact shape as `BROKEN`. Any partial or differing shape
is reported as `REVIEW` and must not be processed with the standard repair.

## Audit

Run as a PostgreSQL role that can connect to and select from every target
database. Authentication can use the usual `PGHOST`, `PGPORT`, `PGUSER`,
`PGPASSWORD`, or `.pgpass` mechanisms.

Audit every database beginning with `tellervo`:

```bash
python3 scripts/audit-corrupt-tlkptaxon.py --all --prefix tellervo
```

Audit an explicit list:

```bash
python3 scripts/audit-corrupt-tlkptaxon.py \
  --databases tellervoone tellervotwo tellervothree
```

For a remote server:

```bash
python3 scripts/audit-corrupt-tlkptaxon.py \
  --host database.example.org --username postgres \
  --all --prefix tellervo
```

Use `--json` for a machine-readable report. Exit status is `0` when all
applicable databases are clean, `2` when exact supported corruption is found,
and `1` when an error or review-only variant is found.

Statuses:

- `OK`: the known corruption signature is absent.
- `BROKEN`: the exact signature supported by the standard repair is present.
- `REVIEW`: suspicious data exists but does not match the supported repair, or
  element-to-taxon links are already orphaned.
- `NOT_APPLICABLE`: the database lacks the table or has an older taxon schema.
- `ERROR`: the database could not be audited reliably.

## Back Up Each Broken Database

Do not repair a live database without a verified backup. Stop application
writes or schedule a maintenance window before taking the final backup.

```bash
pg_dump --format=custom --dbname=DATABASE --file=DATABASE-before-taxon-repair.dump
pg_restore --list DATABASE-before-taxon-repair.dump >/dev/null
sha256sum DATABASE-before-taxon-repair.dump \
  > DATABASE-before-taxon-repair.dump.sha256
```

Store the backup and checksum somewhere independent of the database server.

## Generate or Review the Repair

Generate a separate repair from each database's current taxon dump:

```bash
scripts/dump-tlkptaxon-for-repair.sh DATABASE
python3 scripts/generate-corrupt-tlkptaxon-repair.py \
  DATABASE-tlkptaxon.sql > DATABASE-taxon-repair.sql
```

The dump script takes the database name as its only parameter and creates
`DATABASE-tlkptaxon.sql` in the current directory. It refuses to overwrite an
existing dump; move or remove an older dump before taking a fresh one.

Never reuse generated SQL for another database, even when audit counts look
the same. The generated preflight records the exact source row count, IDs,
ranks, labels, HTML labels, and parent fields from that dump.

When running outside a full source checkout, copy
`dump-tlkptaxon-for-repair.sh`, the generator, and
`database_upgrade-1.9-taxon-upgrade-6.sql` to the database server. Then run:

```bash
./dump-tlkptaxon-for-repair.sh DATABASE
python3 generate-corrupt-tlkptaxon-repair.py \
  DATABASE-tlkptaxon.sql \
  > DATABASE-taxon-repair.sql
```

The generator automatically finds the upgrade SQL when it is beside the
generator or in the current directory. Use `--upgrade-sql FILE` when it is
stored elsewhere.

The generator handles the observed fleet variants:

- Restoring any supported subset of `legacy-*` IDs.
- Moving genuinely displaced taxa before restoring their occupied IDs.
- Consolidating duplicate taxa inserted by later upgrade patches.
- Merging a displaced row when its correct target already exists.
- Removing the unusable `legacy-` empty placeholder only when no element
  references it.

It refuses unknown taxa, ambiguous mappings, changed source content,
unexpected foreign keys, occupied targets, and referenced empty placeholders.
Do not weaken these checks to force a repair through.

The older static repair remains available for historical reference:

```text
Databases/scripts/repair-corrupt-tlkptaxon.sql
```

Do not use that static repair for the newly audited fleet: the database subsets
and duplicate rows differ from its original source database.

### Custom taxa

If generation reports missing authoritative canonical data, obtain a clean
pre-corruption dump containing those taxa and pass it as a reference:

```bash
python3 scripts/generate-corrupt-tlkptaxon-repair.py \
  DATABASE-tlkptaxon.sql \
  --reference-dump DATABASE-clean-tlkptaxon.sql \
  > DATABASE-taxon-repair.sql
```

Reference rows are accepted only when their `taxonid` and `colid` agree and
their labels are populated. Built-in mappings cannot be overridden with
conflicting reference data.

## Apply One Database at a Time

Ensure no webservice or desktop client is writing to the database. The repair
takes exclusive locks on `tlkptaxon` and updates related `tblelement` links.

```bash
psql --no-psqlrc --set=ON_ERROR_STOP=1 --dbname=DATABASE \
  --file=DATABASE-taxon-repair.sql
```

The SQL performs its own transaction and post-repair verification. A failed
preflight or verification causes the transaction to stop; inspect the error
before retrying.

Immediately audit the database again:

```bash
python3 scripts/audit-corrupt-tlkptaxon.py --databases DATABASE
```

The expected result is `OK`, with zero legacy rows, collisions, and orphaned
element links. Then perform an application smoke test that loads the taxon
dictionary and opens elements using previously affected taxa.

## Fleet Rollout

1. Save the initial JSON audit report.
2. Separate `BROKEN`, `REVIEW`, and inaccessible databases.
3. Investigate every `REVIEW` database individually.
4. For each `BROKEN` database: stop writes, back up, verify the backup, apply
   the repair, rerun the audit, and smoke test.
5. Resume writes only after that database passes verification.
6. Save the final JSON audit report alongside the backup manifest.

Do not run the repair across the whole fleet in one shell loop. Handling one
database at a time keeps failures contained and ensures every database has a
verified recovery point.
