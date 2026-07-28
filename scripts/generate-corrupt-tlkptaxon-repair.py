#!/usr/bin/env python3
"""Generate the one-off SQL repair for a rerun of taxon upgrade 6.

The broken-table dump supplies the exact affected IDs and retained ranks.  The
original upgrade supplies the authoritative CoL labels, HTML labels, and
parent links.  Output is deliberately self-contained so it can be reviewed
and run on the affected server without this script or the source dump.
"""

from pathlib import Path
import re
import sys


ROOT = Path(__file__).resolve().parents[1]
UPGRADE = ROOT / "Databases/db-upgrade-patches/database_upgrade-1.9-taxon-upgrade-6.sql"


def sql(value: str) -> str:
    return "'" + value.replace("'", "''") + "'"


def main() -> None:
    if len(sys.argv) != 2:
        raise SystemExit(f"usage: {sys.argv[0]} /path/to/rmbcptaxon.sql")

    dump = Path(sys.argv[1]).read_text(encoding="utf-8")
    copy_data = dump.split("FROM stdin;\n", 1)[1].split("\n\\.\n", 1)[0]
    rows = [line.split("\t") for line in copy_data.splitlines()]

    upgrade = UPGRADE.read_text(encoding="utf-8")
    quoted = r"'((?:[^']|'')*)'"
    pattern = re.compile(
        r"UPDATE\s+tlkptaxon\s+SET\s+newlabel=" + quoted
        + r",\s*htmllabel=" + quoted
        + r",\s*colparentid=" + quoted
        + r"\s+WHERE\s+colid=" + quoted + r";",
        re.IGNORECASE,
    )
    canonical = {
        match.group(4).replace("''", "'"): tuple(
            match.group(i).replace("''", "'") for i in range(1, 4)
        )
        for match in pattern.finditer(upgrade)
    }

    # These appeared after, or independently of, the bulk 1.9 mapping.
    canonical.update({
        "949X": ("Acer glabrum Torr.", "<i>Acer glabrum</i> Torr.", "MLD"),
        "TG": ("Tracheophyta", "Tracheophyta", "P"),
        # TP is the CoL Tracheophyta record linked by the existing plant
        # classes.  P is consistent with the adjacent upgrade-7 TG record.
        "TP": ("Tracheophyta", "Tracheophyta", "P"),
    })

    repairs = []
    for row in rows:
        taxon_id = row[5]
        if taxon_id.startswith("legacy-"):
            target = taxon_id.removeprefix("legacy-")
            if target not in canonical:
                raise SystemExit(f"no canonical mapping for {taxon_id}")
            repairs.append((taxon_id, target, *canonical[target]))

    # A rerun accidentally repurposed these three surviving numeric CoL IDs.
    # Their ranks remained those of the intended target rows, so only their
    # IDs and descriptive fields need repair.
    for corrupt_id, target in (("5KWC", "383"), ("6R6H5", "384"), ("63SMF", "649")):
        if target not in canonical:
            raise SystemExit(f"no canonical mapping for collision target {target}")
        repairs.append((corrupt_id, target, *canonical[target]))

    if len(repairs) != 867:
        raise SystemExit(f"expected 867 repair mappings, found {len(repairs)}")
    if len({row[0] for row in repairs}) != len(repairs):
        raise SystemExit("duplicate corrupt IDs in generated mapping")
    if len({row[1] for row in repairs}) != len(repairs):
        raise SystemExit("duplicate target IDs in generated mapping")

    print("""-- MANUAL ONE-OFF repair for tlkptaxon corrupted by rerunning taxon upgrade 6.
-- Do not include this file in the automatic database upgrade patch sequence.
-- Generated from rmbcptaxon.sql and the authoritative upgrade-6 mappings.
-- Review and take a database backup before execution.

BEGIN;

LOCK TABLE public.tlkptaxon IN ACCESS EXCLUSIVE MODE;
LOCK TABLE public.tblelement IN SHARE ROW EXCLUSIVE MODE;

CREATE TEMP TABLE taxon_repair_map (
    corrupt_id varchar PRIMARY KEY,
    target_id varchar NOT NULL UNIQUE,
    label varchar(128) NOT NULL,
    htmllabel varchar NOT NULL,
    parent_id varchar NOT NULL
) ON COMMIT DROP;

INSERT INTO taxon_repair_map (corrupt_id, target_id, label, htmllabel, parent_id) VALUES""")
    for index, repair in enumerate(sorted(repairs)):
        suffix = ";" if index == len(repairs) - 1 else ","
        print("    (" + ", ".join(sql(value) for value in repair) + ")" + suffix)

    print("""
DO $preflight$
DECLARE
    matched_count integer;
    unexpected_fk text;
BEGIN
    SELECT count(*) INTO matched_count
    FROM public.tlkptaxon AS taxon
    JOIN taxon_repair_map AS repair ON repair.corrupt_id = taxon.taxonid
    WHERE taxon.label = 'n/a'
       OR taxon.taxonid IN ('5KWC', '6R6H5', '63SMF');

    IF matched_count <> 867 THEN
        RAISE EXCEPTION 'Repair aborted: expected 867 matching corrupt taxa, found %', matched_count;
    END IF;

    IF EXISTS (
        SELECT 1 FROM public.tlkptaxon
        WHERE taxonid LIKE 'legacy-%'
          AND taxonid NOT IN (SELECT corrupt_id FROM taxon_repair_map)
    ) THEN
        RAISE EXCEPTION 'Repair aborted: an unmapped legacy taxon exists';
    END IF;

    SELECT string_agg(conrelid::regclass::text || '.' || conname, ', ')
    INTO unexpected_fk
    FROM pg_constraint
    WHERE contype = 'f'
      AND confrelid = 'public.tlkptaxon'::regclass
      AND conrelid <> 'public.tblelement'::regclass;

    IF unexpected_fk IS NOT NULL THEN
        RAISE EXCEPTION 'Repair aborted: unexpected foreign keys reference tlkptaxon: %', unexpected_fk;
    END IF;
END
$preflight$;

ALTER TABLE public.tblelement DROP CONSTRAINT IF EXISTS fkey_element_taxon;

-- Move the three falsely repurposed rows first, freeing their real CoL IDs.
UPDATE public.tlkptaxon AS taxon
SET taxonid = repair.target_id,
    colid = repair.target_id
FROM taxon_repair_map AS repair
WHERE taxon.taxonid = repair.corrupt_id
  AND repair.corrupt_id IN ('5KWC', '6R6H5', '63SMF');

-- Restore every legacy-prefixed primary key in place, preserving its rank.
UPDATE public.tlkptaxon AS taxon
SET taxonid = repair.target_id,
    colid = repair.target_id
FROM taxon_repair_map AS repair
WHERE taxon.taxonid = repair.corrupt_id
  AND repair.corrupt_id LIKE 'legacy-%';

-- Restore display content and both copies of the parent relationship.
UPDATE public.tlkptaxon AS taxon
SET label = repair.label,
    htmllabel = repair.htmllabel,
    parenttaxonid = repair.parent_id,
    colparentid = repair.parent_id
FROM taxon_repair_map AS repair
WHERE taxon.taxonid = repair.target_id;

-- Retarget element links according to the row from which each ID originated.
UPDATE public.tblelement AS element
SET taxonid = repair.target_id
FROM taxon_repair_map AS repair
WHERE element.taxonid = repair.corrupt_id;

ALTER TABLE public.tblelement
    ADD CONSTRAINT fkey_element_taxon
    FOREIGN KEY (taxonid) REFERENCES public.tlkptaxon(taxonid)
    ON UPDATE NO ACTION ON DELETE NO ACTION;

DO $verify$
DECLARE
    repaired_count integer;
BEGIN
    SELECT count(*) INTO repaired_count
    FROM public.tlkptaxon AS taxon
    JOIN taxon_repair_map AS repair ON repair.target_id = taxon.taxonid
    WHERE taxon.colid = repair.target_id
      AND taxon.label = repair.label
      AND taxon.htmllabel = repair.htmllabel
      AND taxon.parenttaxonid = repair.parent_id
      AND taxon.colparentid = repair.parent_id;

    IF repaired_count <> 867 THEN
        RAISE EXCEPTION 'Repair verification failed: expected 867 repaired taxa, found %', repaired_count;
    END IF;

    IF EXISTS (SELECT 1 FROM public.tlkptaxon WHERE taxonid LIKE 'legacy-%') THEN
        RAISE EXCEPTION 'Repair verification failed: legacy taxon IDs remain';
    END IF;

    IF EXISTS (
        SELECT 1 FROM public.tblelement AS element
        LEFT JOIN public.tlkptaxon AS taxon ON taxon.taxonid = element.taxonid
        WHERE taxon.taxonid IS NULL
    ) THEN
        RAISE EXCEPTION 'Repair verification failed: orphaned element taxon links remain';
    END IF;
END
$verify$;

COMMIT;
""")


if __name__ == "__main__":
    main()
