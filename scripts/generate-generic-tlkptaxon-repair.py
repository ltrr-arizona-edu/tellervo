#!/usr/bin/env python3
"""Build a self-contained, data-shape-independent tlkptaxon repair.

The canonical snapshot is reconstructed from a known upgrade-6-rerun dump
using the authoritative mappings already consumed by
generate-corrupt-tlkptaxon-repair.py.  The emitted SQL does not depend on that
source database's damaged shape: at execution time it identifies rows by
legacy ID, exact canonical content, or canonical ID (in that order), retargets
element references, and replaces tlkptaxon with the embedded snapshot.
"""

from __future__ import annotations

import argparse
import csv
import hashlib
import importlib.util
from pathlib import Path
import sys
from typing import Optional, Sequence


ROOT = Path(__file__).resolve().parents[1]
SPECIFIC_GENERATOR = ROOT / "scripts/generate-corrupt-tlkptaxon-repair.py"
DEFAULT_SUPPLEMENT = (
    ROOT / "Databases/scripts/tlkptaxon-authoritative-supplement.csv")


def load_specific_generator():
    spec = importlib.util.spec_from_file_location(
        "corrupt_tlkptaxon_repair_generator", SPECIFIC_GENERATOR)
    if spec is None or spec.loader is None:
        raise SystemExit(f"cannot load {SPECIFIC_GENERATOR}")
    module = importlib.util.module_from_spec(spec)
    sys.modules[spec.name] = module
    spec.loader.exec_module(module)
    return module


def parse_args(argv: Optional[Sequence[str]] = None) -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Generate a generic, canonical tlkptaxon repair SQL file.")
    parser.add_argument(
        "source_dump", type=Path,
        help="known upgrade-6-rerun tlkptaxon dump used to reconstruct the snapshot")
    parser.add_argument("output", type=Path, help="SQL file to create")
    parser.add_argument(
        "--supplement", action="append", type=Path, default=None, metavar="CSV",
        help=("authoritative supplemental taxa CSV; repeatable (default: "
              "Databases/scripts/tlkptaxon-authoritative-supplement.csv)"),
    )
    return parser.parse_args(argv)


def reconstruct_rows(module, source_dump: Path):
    rows = module.read_taxon_dump(source_dump)
    canonical, new_to_old = module.parse_upgrade(
        module.resolve_upgrade_sql(None))
    actions, discard_ids = module.build_actions(rows, canonical, new_to_old)
    actions_by_source = {action.source_id: action for action in actions}
    discards = set(discard_ids)
    repaired = {}

    for row in rows:
        source_id = row["taxonid"]
        if source_id in discards:
            continue
        action = actions_by_source.get(source_id)
        if action is None:
            candidate = dict(row)
        elif action.action in ("deduplicate", "merge"):
            continue
        else:
            candidate = {
                "taxonid": action.target_id,
                "colid": action.target_id,
                "taxonrankid": str(action.rank_id),
                "label": action.label,
                "htmllabel": action.htmllabel,
                "parenttaxonid": action.parent_id,
                "colparentid": action.parent_id,
            }

        taxon_id = candidate["taxonid"]
        if not taxon_id or taxon_id.startswith("legacy-"):
            raise SystemExit(f"canonical reconstruction left invalid ID {taxon_id!r}")
        if taxon_id in repaired:
            raise SystemExit(f"canonical reconstruction produced duplicate ID {taxon_id}")
        if candidate["colid"] != taxon_id:
            raise SystemExit(f"canonical reconstruction left mismatched colid for {taxon_id}")
        if candidate["parenttaxonid"] != candidate["colparentid"]:
            raise SystemExit(f"canonical reconstruction left mismatched parent for {taxon_id}")
        repaired[taxon_id] = candidate

    return [repaired[taxon_id] for taxon_id in sorted(repaired)]


def add_supplements(rows, paths):
    required = {
        "taxonid", "colid", "colparentid", "taxonrankid", "label",
        "htmllabel", "parenttaxonid",
    }
    by_id = {row["taxonid"]: row for row in rows}
    for path in paths:
        try:
            stream = path.open(encoding="utf-8", newline="")
        except OSError as exc:
            raise SystemExit(f"cannot read supplement {path}: {exc}") from exc
        with stream:
            reader = csv.DictReader(stream)
            missing = required.difference(reader.fieldnames or [])
            if missing:
                raise SystemExit(
                    f"{path}: missing columns {', '.join(sorted(missing))}")
            for line_number, source in enumerate(reader, start=2):
                row = {field: source[field] or None for field in required}
                taxon_id = row["taxonid"]
                if not taxon_id or taxon_id.startswith("legacy-"):
                    raise SystemExit(f"{path}:{line_number}: invalid taxonid")
                if row["colid"] != taxon_id:
                    raise SystemExit(
                        f"{path}:{line_number}: taxonid/colid mismatch")
                if row["parenttaxonid"] != row["colparentid"]:
                    raise SystemExit(
                        f"{path}:{line_number}: parent ID mismatch")
                if not row["label"] or not row["htmllabel"]:
                    raise SystemExit(f"{path}:{line_number}: missing label")
                try:
                    int(row["taxonrankid"])
                except (TypeError, ValueError) as exc:
                    raise SystemExit(
                        f"{path}:{line_number}: invalid taxonrankid") from exc
                existing = by_id.get(taxon_id)
                if existing is not None and existing != row:
                    raise SystemExit(
                        f"{path}:{line_number}: conflicting taxon {taxon_id}")
                by_id[taxon_id] = row
    return [by_id[taxon_id] for taxon_id in sorted(by_id)]


def sql(value):
    if value is None:
        return "NULL"
    return "'" + str(value).replace("'", "''") + "'"


def canonical_digest(rows) -> str:
    fields = ("taxonid", "colid", "colparentid", "taxonrankid", "label",
              "htmllabel", "parenttaxonid")
    payload = "\n".join(
        "\x1f".join("\\N" if row[field] is None else str(row[field])
                     for field in fields)
        for row in rows
    ).encode("utf-8")
    return hashlib.sha256(payload).hexdigest()


def emit(rows, source_dump: Path, supplements) -> str:
    digest = canonical_digest(rows)
    values = []
    for row in rows:
        rendered = (
            sql(row["taxonid"]), sql(row["colid"]),
            sql(row["colparentid"]), str(int(row["taxonrankid"])),
            sql(row["label"]), sql(row["htmllabel"]),
            sql(row["parenttaxonid"]),
        )
        values.append("    (" + ", ".join(rendered) + ")")
    value_sql = ",\n".join(values)

    supplement_names = ", ".join(path.name for path in supplements)
    return f"""-- Generic repair for tlkptaxon corruption caused by rerunning taxon upgrade 6.
-- Canonical snapshot reconstructed from: {source_dump.name}
-- Authoritative supplements: {supplement_names}
-- Canonical rows: {len(rows)}; SHA-256: {digest}
-- This script is transactional, idempotent, and deliberately refuses unknown taxa.
-- Take and verify a database backup before execution.

\\set ON_ERROR_STOP on
BEGIN;

LOCK TABLE public.tlkptaxon IN ACCESS EXCLUSIVE MODE;
LOCK TABLE public.tblelement IN SHARE ROW EXCLUSIVE MODE;

CREATE TEMP TABLE canonical_taxon (
    taxonid varchar PRIMARY KEY,
    colid varchar NOT NULL UNIQUE,
    colparentid varchar,
    taxonrankid integer NOT NULL,
    label varchar NOT NULL,
    htmllabel varchar NOT NULL,
    parenttaxonid varchar
) ON COMMIT DROP;

INSERT INTO canonical_taxon
    (taxonid, colid, colparentid, taxonrankid, label, htmllabel, parenttaxonid)
VALUES
{value_sql};

CREATE TEMP TABLE taxon_source_map (
    source_id varchar PRIMARY KEY,
    target_id varchar NOT NULL REFERENCES canonical_taxon(taxonid),
    resolution varchar NOT NULL
) ON COMMIT DROP;

-- Resolution order matters.  Exact same-ID rows are already canonical.  A
-- legacy prefix records the pre-corruption target.  Exact content identifies
-- displaced collision rows.  Same-ID fallback repairs damaged row contents.
INSERT INTO taxon_source_map (source_id, target_id, resolution)
SELECT source.taxonid,
       COALESCE(same_exact.taxonid, legacy.taxonid,
                content_exact.taxonid, same_id.taxonid),
       CASE
         WHEN same_exact.taxonid IS NOT NULL THEN 'canonical'
         WHEN legacy.taxonid IS NOT NULL THEN 'legacy-id'
         WHEN content_exact.taxonid IS NOT NULL THEN 'canonical-content'
         ELSE 'same-id-fallback'
       END
FROM public.tlkptaxon AS source
LEFT JOIN canonical_taxon AS same_exact
  ON same_exact.taxonid = source.taxonid
 AND same_exact.colid = source.colid
 AND same_exact.taxonrankid = source.taxonrankid
 AND same_exact.label = source.label
 AND same_exact.htmllabel = source.htmllabel
 AND same_exact.parenttaxonid IS NOT DISTINCT FROM source.parenttaxonid
 AND same_exact.colparentid IS NOT DISTINCT FROM source.colparentid
LEFT JOIN canonical_taxon AS legacy
  ON source.taxonid LIKE 'legacy-%'
 AND legacy.taxonid = substring(source.taxonid FROM 8)
LEFT JOIN LATERAL (
    SELECT min(candidate.taxonid) AS taxonid
    FROM canonical_taxon AS candidate
    WHERE candidate.taxonrankid = source.taxonrankid
      AND candidate.label = source.label
      AND candidate.htmllabel = source.htmllabel
      AND candidate.parenttaxonid IS NOT DISTINCT FROM source.parenttaxonid
      AND candidate.colparentid IS NOT DISTINCT FROM source.colparentid
    HAVING count(*) = 1
) AS content_exact ON TRUE
LEFT JOIN canonical_taxon AS same_id ON same_id.taxonid = source.taxonid
WHERE source.taxonid <> 'legacy-'
  AND COALESCE(same_exact.taxonid, legacy.taxonid,
               content_exact.taxonid, same_id.taxonid) IS NOT NULL;

DO $preflight$
DECLARE
    unknown_taxa text;
    unknown_elements text;
    unexpected_fk text;
BEGIN
    IF (SELECT count(*) FROM canonical_taxon) <> {len(rows)} THEN
        RAISE EXCEPTION 'Repair internal error: canonical snapshot did not load';
    END IF;

    SELECT string_agg(taxon.taxonid, ', ' ORDER BY taxon.taxonid)
    INTO unknown_taxa
    FROM public.tlkptaxon AS taxon
    LEFT JOIN taxon_source_map AS mapping ON mapping.source_id = taxon.taxonid
    WHERE mapping.source_id IS NULL
      AND NOT (taxon.taxonid = 'legacy-'
               AND taxon.colid = 'legacy-'
               AND taxon.label = 'n/a'
               AND taxon.htmllabel = 'n/a');

    IF unknown_taxa IS NOT NULL THEN
        RAISE EXCEPTION 'Repair aborted: unknown/custom taxa require review: %',
            unknown_taxa;
    END IF;

    IF EXISTS (
        SELECT 1 FROM public.tblelement WHERE taxonid = 'legacy-'
    ) THEN
        RAISE EXCEPTION 'Repair aborted: empty legacy placeholder is referenced';
    END IF;

    SELECT string_agg(element.taxonid, ', ' ORDER BY element.taxonid)
    INTO unknown_elements
    FROM (SELECT DISTINCT taxonid FROM public.tblelement
          WHERE taxonid IS NOT NULL) AS element
    LEFT JOIN taxon_source_map AS mapping
      ON mapping.source_id = element.taxonid
    LEFT JOIN canonical_taxon AS canonical
      ON canonical.taxonid = element.taxonid
    WHERE mapping.source_id IS NULL AND canonical.taxonid IS NULL;

    IF unknown_elements IS NOT NULL THEN
        RAISE EXCEPTION 'Repair aborted: element taxon IDs cannot be mapped: %',
            unknown_elements;
    END IF;

    SELECT string_agg(conrelid::regclass::text || '.' || conname, ', ')
    INTO unexpected_fk
    FROM pg_constraint
    WHERE contype = 'f'
      AND confrelid = 'public.tlkptaxon'::regclass
      AND NOT (conrelid = 'public.tblelement'::regclass
               AND conname = 'fkey_element_taxon');

    IF unexpected_fk IS NOT NULL THEN
        RAISE EXCEPTION 'Repair aborted: unexpected foreign keys reference tlkptaxon: %',
            unexpected_fk;
    END IF;
END
$preflight$;

ALTER TABLE public.tblelement DROP CONSTRAINT IF EXISTS fkey_element_taxon;

UPDATE public.tblelement AS element
SET taxonid = mapping.target_id
FROM taxon_source_map AS mapping
WHERE element.taxonid = mapping.source_id
  AND element.taxonid IS DISTINCT FROM mapping.target_id;

-- Converge every supported input shape on the exact same dictionary.
DELETE FROM public.tlkptaxon;
INSERT INTO public.tlkptaxon
    (taxonid, colid, colparentid, taxonrankid, label, htmllabel, parenttaxonid)
SELECT taxonid, colid, colparentid, taxonrankid, label, htmllabel, parenttaxonid
FROM canonical_taxon;

ALTER TABLE public.tblelement
    ADD CONSTRAINT fkey_element_taxon
    FOREIGN KEY (taxonid) REFERENCES public.tlkptaxon(taxonid)
    ON UPDATE NO ACTION ON DELETE NO ACTION;

DO $verify$
BEGIN
    IF EXISTS (
        SELECT taxonid, colid, colparentid, taxonrankid, label,
               htmllabel, parenttaxonid FROM public.tlkptaxon
        EXCEPT
        SELECT taxonid, colid, colparentid, taxonrankid, label,
               htmllabel, parenttaxonid FROM canonical_taxon
    ) OR EXISTS (
        SELECT taxonid, colid, colparentid, taxonrankid, label,
               htmllabel, parenttaxonid FROM canonical_taxon
        EXCEPT
        SELECT taxonid, colid, colparentid, taxonrankid, label,
               htmllabel, parenttaxonid FROM public.tlkptaxon
    ) THEN
        RAISE EXCEPTION 'Repair verification failed: tlkptaxon is not canonical';
    END IF;

    IF EXISTS (
        SELECT 1 FROM public.tblelement AS element
        LEFT JOIN public.tlkptaxon AS taxon ON taxon.taxonid = element.taxonid
        WHERE element.taxonid IS NOT NULL AND taxon.taxonid IS NULL
    ) THEN
        RAISE EXCEPTION 'Repair verification failed: orphaned element taxon links remain';
    END IF;
END
$verify$;

COMMIT;
"""


def main(argv: Optional[Sequence[str]] = None) -> int:
    args = parse_args(argv)
    if args.output.exists():
        raise SystemExit(f"refusing to overwrite existing output: {args.output}")
    module = load_specific_generator()
    rows = reconstruct_rows(module, args.source_dump)
    supplements = args.supplement or [DEFAULT_SUPPLEMENT]
    rows = add_supplements(rows, supplements)
    args.output.write_text(
        emit(rows, args.source_dump, supplements), encoding="utf-8")
    print(f"Created {args.output} with {len(rows)} canonical rows")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
