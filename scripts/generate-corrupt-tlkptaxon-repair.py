#!/usr/bin/env python3
"""Generate a guarded repair for taxon-upgrade-6 rerun corruption.

The damaged table can contain only a subset of the original dictionary and can
also contain rows reinserted by later upgrades.  This generator classifies each
legacy/current-ID overlap as either a duplicate to consolidate or a displaced
taxon to move before restoring the legacy row.

The output is self-contained SQL with database-shape preflight checks, a single
transaction, element-reference retargeting, and post-repair verification.
"""

from __future__ import annotations

import argparse
from dataclasses import dataclass
from pathlib import Path
import re
import sys
from typing import Dict, Iterable, Optional, Sequence, Tuple


ROOT = Path(__file__).resolve().parents[1]
UPGRADE_BASENAME = "database_upgrade-1.9-taxon-upgrade-6.sql"
UPGRADE = ROOT / "Databases/db-upgrade-patches" / UPGRADE_BASENAME
REQUIRED_COLUMNS = {
    "colid", "colparentid", "taxonrankid", "label", "htmllabel",
    "taxonid", "parenttaxonid",
}
Canonical = Tuple[str, str, Optional[str]]


@dataclass(frozen=True)
class RepairAction:
    source_id: str
    target_id: str
    label: str
    htmllabel: str
    parent_id: Optional[str]
    rank_id: int
    action: str
    source_colid: Optional[str]
    source_label: Optional[str]
    source_htmllabel: Optional[str]
    source_parent_id: Optional[str]
    source_colparent_id: Optional[str]


def parse_args(argv: Optional[Sequence[str]] = None) -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Generate SQL for a specific corrupt tlkptaxon data dump.")
    parser.add_argument("dump", type=Path,
                        help="plain pg_dump --data-only dump of public.tlkptaxon")
    parser.add_argument(
        "--upgrade-sql", type=Path, metavar="FILE",
        help=("authoritative taxon upgrade-6 SQL; otherwise searched for in "
              "the repository, beside this script, and in the current directory"),
    )
    parser.add_argument(
        "--reference-dump", action="append", type=Path, default=[], metavar="DUMP",
        help="clean tlkptaxon dump supplying authoritative custom taxa; repeatable",
    )
    return parser.parse_args(argv)


def resolve_upgrade_sql(explicit_path: Optional[Path]) -> Path:
    if explicit_path is not None:
        return explicit_path

    candidates = (
        UPGRADE,
        Path(__file__).resolve().with_name(UPGRADE_BASENAME),
        Path.cwd() / UPGRADE_BASENAME,
    )
    for candidate in candidates:
        if candidate.is_file():
            return candidate

    searched = "\n  ".join(str(candidate) for candidate in candidates)
    raise SystemExit(
        "cannot find authoritative taxon upgrade SQL. Searched:\n"
        f"  {searched}\n"
        f"Copy {UPGRADE_BASENAME} beside the generator or specify "
        "--upgrade-sql FILE."
    )


def sql(value: Optional[str]) -> str:
    if value is None:
        return "NULL"
    return "'" + value.replace("'", "''") + "'"


def decode_copy_value(value: str) -> Optional[str]:
    if value == r"\N":
        return None
    result = []
    index = 0
    escapes = {"b": "\b", "f": "\f", "n": "\n", "r": "\r",
               "t": "\t", "v": "\v", "\\": "\\"}
    while index < len(value):
        if value[index] != "\\":
            result.append(value[index])
            index += 1
            continue
        index += 1
        if index >= len(value):
            result.append("\\")
            break
        escaped = value[index]
        if escaped in escapes:
            result.append(escapes[escaped])
            index += 1
        elif escaped in "01234567":
            end = index
            while end < len(value) and end < index + 3 and value[end] in "01234567":
                end += 1
            result.append(chr(int(value[index:end], 8)))
            index = end
        elif escaped == "x":
            end = index + 1
            while end < len(value) and end < index + 3 and value[end] in "0123456789abcdefABCDEF":
                end += 1
            if end == index + 1:
                result.append("x")
                index += 1
            else:
                result.append(chr(int(value[index + 1:end], 16)))
                index = end
        else:
            result.append(escaped)
            index += 1
    return "".join(result)


def read_taxon_dump(path: Path) -> list[Dict[str, Optional[str]]]:
    try:
        dump = path.read_text(encoding="utf-8")
    except OSError as exc:
        raise SystemExit(f"cannot read dump {path}: {exc}") from exc

    header = re.search(
        r"^COPY\s+public\.tlkptaxon\s+\(([^)]+)\)\s+FROM\s+stdin;\s*$",
        dump,
        re.IGNORECASE | re.MULTILINE,
    )
    if not header:
        raise SystemExit(f"{path}: public.tlkptaxon COPY data was not found")
    columns = [column.strip().lower() for column in header.group(1).split(",")]
    missing = sorted(REQUIRED_COLUMNS.difference(columns))
    if missing:
        raise SystemExit(f"{path}: tlkptaxon COPY is missing {', '.join(missing)}")

    data_start = header.end()
    terminator = re.search(r"^\\\.\s*$", dump[data_start:], re.MULTILINE)
    if not terminator:
        raise SystemExit(f"{path}: tlkptaxon COPY terminator was not found")
    data = dump[data_start:data_start + terminator.start()].lstrip("\r\n")

    rows: list[Dict[str, Optional[str]]] = []
    for line_number, line in enumerate(data.splitlines(), start=1):
        values = line.split("\t")
        if len(values) != len(columns):
            raise SystemExit(
                f"{path}: COPY row {line_number} has {len(values)} values; "
                f"expected {len(columns)}")
        rows.append(dict(zip(columns, (decode_copy_value(value) for value in values))))
    return rows


def parse_upgrade(path: Path = UPGRADE) -> tuple[dict[str, Canonical], dict[str, list[str]]]:
    try:
        upgrade = path.read_text(encoding="utf-8")
    except OSError as exc:
        raise SystemExit(f"cannot read authoritative upgrade SQL {path}: {exc}") from exc
    quoted = r"'((?:[^']|'')*)'"
    canonical_pattern = re.compile(
        r"UPDATE\s+tlkptaxon\s+SET\s+newlabel=" + quoted
        + r",\s*htmllabel=" + quoted
        + r",\s*colparentid=" + quoted
        + r"\s+WHERE\s+colid=" + quoted + r";",
        re.IGNORECASE,
    )
    canonical: dict[str, Canonical] = {
        match.group(4).replace("''", "'"): tuple(
            match.group(index).replace("''", "'") for index in range(1, 4)
        )
        for match in canonical_pattern.finditer(upgrade)
        if match.group(4).replace("''", "'")
    }

    # These records appeared after, or independently of, the bulk 1.9 mapping.
    canonical.update({
        "949X": ("Acer glabrum Torr.", "<i>Acer glabrum</i> Torr.", "MLD"),
        "TG": ("Tracheophyta", "Tracheophyta", "P"),
        "TP": ("Tracheophyta", "Tracheophyta", "P"),
    })

    id_pattern = re.compile(
        r"UPDATE\s+tlkptaxon\s+SET\s+colid=" + quoted
        + r"\s+WHERE\s+taxonid=" + quoted + r";",
        re.IGNORECASE,
    )
    new_to_old: dict[str, list[str]] = {}
    for match in id_pattern.finditer(upgrade):
        new_id = match.group(1).replace("''", "'")
        old_id = match.group(2).replace("''", "'")
        new_to_old.setdefault(new_id, []).append(old_id)
    return canonical, new_to_old


def extend_canonical(canonical: dict[str, Canonical], paths: Iterable[Path]) -> None:
    for path in paths:
        for row in read_taxon_dump(path):
            taxon_id = row["taxonid"]
            if not taxon_id or taxon_id.startswith("legacy-"):
                continue
            if row["label"] in (None, "n/a") or row["htmllabel"] in (None, "n/a"):
                continue
            if row["colid"] != taxon_id:
                continue
            candidate = (row["label"], row["htmllabel"], row["parenttaxonid"])
            existing = canonical.get(taxon_id)
            if existing is not None and existing != candidate:
                raise SystemExit(
                    f"reference {path} conflicts with built-in canonical data for {taxon_id}")
            canonical[taxon_id] = candidate


def row_matches(row: Dict[str, Optional[str]], taxon_id: str,
                canonical: Canonical, rank_id: int) -> bool:
    label, htmllabel, parent_id = canonical
    return (
        row["taxonid"] == taxon_id
        and row["colid"] == taxon_id
        and row["label"] == label
        and row["htmllabel"] == htmllabel
        and row["parenttaxonid"] == parent_id
        and row["colparentid"] == parent_id
        and int(row["taxonrankid"]) == rank_id
    )


def make_action(source: Dict[str, Optional[str]], target_id: str,
                canonical: Canonical, rank_id: int, action: str) -> RepairAction:
    label, htmllabel, parent_id = canonical
    return RepairAction(
        source_id=source["taxonid"],
        target_id=target_id,
        label=label,
        htmllabel=htmllabel,
        parent_id=parent_id,
        rank_id=rank_id,
        action=action,
        source_colid=source["colid"],
        source_label=source["label"],
        source_htmllabel=source["htmllabel"],
        source_parent_id=source["parenttaxonid"],
        source_colparent_id=source["colparentid"],
    )


def build_actions(rows: list[Dict[str, Optional[str]]], canonical: dict[str, Canonical],
                  new_to_old: dict[str, list[str]]) -> tuple[list[RepairAction], list[str]]:
    by_id: dict[str, Dict[str, Optional[str]]] = {}
    for row in rows:
        taxon_id = row["taxonid"]
        if not taxon_id:
            raise SystemExit("dump contains a taxon with a null or empty taxonid")
        if taxon_id in by_id:
            raise SystemExit(f"dump contains duplicate taxonid {taxon_id}")
        by_id[taxon_id] = row

    legacy_rows = [row for row in rows if row["taxonid"].startswith("legacy-")]
    discard_ids = []
    for row in legacy_rows:
        if row["taxonid"] == "legacy-":
            if (row["colid"] != "legacy-" or row["label"] != "n/a"
                    or row["htmllabel"] != "n/a"):
                raise SystemExit("empty legacy placeholder has unexpected content")
            discard_ids.append(row["taxonid"])

    unknown = sorted(
        row["taxonid"].removeprefix("legacy-")
        for row in legacy_rows
        if row["taxonid"] not in discard_ids
        if row["taxonid"].removeprefix("legacy-") not in canonical
    )
    if unknown:
        preview = ", ".join(unknown[:20])
        suffix = "" if len(unknown) <= 20 else f", ... ({len(unknown)} total)"
        raise SystemExit(
            "no authoritative canonical data for legacy IDs: " + preview + suffix
            + ". Supply a clean pre-corruption dump with --reference-dump.")

    actions: list[RepairAction] = []
    for legacy in legacy_rows:
        source_id = legacy["taxonid"]
        if source_id in discard_ids:
            continue
        target_id = source_id.removeprefix("legacy-")
        target_canonical = canonical[target_id]
        legacy_rank = int(legacy["taxonrankid"])
        current = by_id.get(target_id)

        if current is None:
            actions.append(make_action(
                legacy, target_id, target_canonical, legacy_rank, "restore"))
            continue

        if row_matches(current, target_id, target_canonical, legacy_rank):
            actions.append(make_action(
                legacy, target_id, target_canonical, legacy_rank, "deduplicate"))
            continue

        candidates = [candidate for candidate in new_to_old.get(target_id, [])
                      if candidate in canonical]
        if len(candidates) != 1:
            raise SystemExit(
                f"cannot uniquely resolve displaced taxon {target_id}; "
                f"candidate prior IDs are {candidates or 'none'}")
        displaced_target = candidates[0]
        displaced_canonical = canonical[displaced_target]
        displaced_rank = int(current["taxonrankid"])
        existing_target = by_id.get(displaced_target)
        if existing_target is None:
            displacement_action = "move"
        elif row_matches(existing_target, displaced_target,
                         displaced_canonical, displaced_rank):
            displacement_action = "merge"
        else:
            raise SystemExit(
                f"cannot free {target_id}: intended displaced target {displaced_target} "
                "is occupied by a different row")

        actions.append(make_action(
            current, displaced_target, displaced_canonical,
            displaced_rank, displacement_action))
        actions.append(make_action(
            legacy, target_id, target_canonical, legacy_rank, "restore"))

    sources = [action.source_id for action in actions]
    targets = [action.target_id for action in actions]
    if len(set(sources)) != len(sources):
        raise SystemExit("generated repair has duplicate source IDs")
    if len(set(targets)) != len(targets):
        raise SystemExit("generated repair has duplicate target IDs")
    return actions, discard_ids


def emit_sql(dump: Path, rows: list[Dict[str, Optional[str]]],
             actions: list[RepairAction], discard_ids: list[str]) -> None:
    counts = {name: sum(action.action == name for action in actions)
              for name in ("restore", "deduplicate", "move", "merge")}
    legacy_count = sum(row["taxonid"].startswith("legacy-") for row in rows)
    final_count = (len(rows) - counts["deduplicate"] - counts["merge"]
                   - len(discard_ids))
    dump_name = dump.name.replace("\n", " ").replace("\r", " ")

    print(f"""-- GENERATED repair for tlkptaxon corrupted by rerunning taxon upgrade 6.
-- Source dump: {dump_name}
-- Initial rows: {len(rows)}; legacy rows: {legacy_count}; mapping rows: {len(actions)}.
-- Actions: restore={counts['restore']}, deduplicate={counts['deduplicate']},
--          move={counts['move']}, merge={counts['merge']}, discard={len(discard_ids)}.
-- Expected final rows: {final_count}.
-- Review and take a verified database backup before execution.

\\set ON_ERROR_STOP on
BEGIN;

LOCK TABLE public.tlkptaxon IN ACCESS EXCLUSIVE MODE;
LOCK TABLE public.tblelement IN SHARE ROW EXCLUSIVE MODE;

CREATE TEMP TABLE taxon_repair_map (
    source_id varchar PRIMARY KEY,
    target_id varchar NOT NULL UNIQUE,
    label varchar NOT NULL,
    htmllabel varchar NOT NULL,
    parent_id varchar,
    rank_id integer NOT NULL,
    action varchar NOT NULL CHECK (action IN ('restore', 'deduplicate', 'move', 'merge')),
    source_colid varchar,
    source_label varchar,
    source_htmllabel varchar,
    source_parent_id varchar,
    source_colparent_id varchar
) ON COMMIT DROP;

INSERT INTO taxon_repair_map
    (source_id, target_id, label, htmllabel, parent_id, rank_id, action,
     source_colid, source_label, source_htmllabel, source_parent_id,
     source_colparent_id)
VALUES""")
    for index, action in enumerate(sorted(actions, key=lambda item: item.source_id)):
        suffix = ";" if index == len(actions) - 1 else ","
        values = (
            action.source_id, action.target_id, action.label,
            action.htmllabel, action.parent_id, str(action.rank_id), action.action,
            action.source_colid, action.source_label, action.source_htmllabel,
            action.source_parent_id, action.source_colparent_id,
        )
        rendered = [sql(value) for value in values]
        rendered[5] = str(action.rank_id)
        print("    (" + ", ".join(rendered) + ")" + suffix)

    discard_sql = ", ".join(sql(value) for value in discard_ids) or "NULL"
    discard_preflight = "TRUE" if discard_ids else "FALSE"
    discard_exclusion = (f"\n          AND taxonid NOT IN ({discard_sql})"
                         if discard_ids else "")
    print(f"""

DO $preflight$
DECLARE
    matched_sources integer;
    unexpected_fk text;
BEGIN
    IF (SELECT count(*) FROM public.tlkptaxon) <> {len(rows)} THEN
        RAISE EXCEPTION 'Repair aborted: expected {len(rows)} initial taxa, found %',
            (SELECT count(*) FROM public.tlkptaxon);
    END IF;

    SELECT count(*) INTO matched_sources
    FROM public.tlkptaxon AS taxon
    JOIN taxon_repair_map AS repair ON repair.source_id = taxon.taxonid
    WHERE taxon.taxonrankid = repair.rank_id
      AND taxon.colid IS NOT DISTINCT FROM repair.source_colid
      AND taxon.label IS NOT DISTINCT FROM repair.source_label
      AND taxon.htmllabel IS NOT DISTINCT FROM repair.source_htmllabel
      AND taxon.parenttaxonid IS NOT DISTINCT FROM repair.source_parent_id
      AND taxon.colparentid IS NOT DISTINCT FROM repair.source_colparent_id;

    IF matched_sources <> {len(actions)} THEN
        RAISE EXCEPTION 'Repair aborted: expected {len(actions)} matching source rows, found %',
            matched_sources;
    END IF;

    IF EXISTS (
        SELECT 1 FROM public.tlkptaxon
        WHERE taxonid LIKE 'legacy-%'
          AND taxonid NOT IN (SELECT source_id FROM taxon_repair_map)
          {discard_exclusion}
    ) THEN
        RAISE EXCEPTION 'Repair aborted: an unmapped legacy taxon exists';
    END IF;

    IF EXISTS (
        SELECT 1
        FROM taxon_repair_map AS repair
        JOIN public.tlkptaxon AS target ON target.taxonid = repair.target_id
        WHERE repair.action = 'move'
    ) THEN
        RAISE EXCEPTION 'Repair aborted: a move target is already occupied';
    END IF;

    IF EXISTS (
        SELECT 1
        FROM taxon_repair_map AS repair
        JOIN public.tlkptaxon AS target ON target.taxonid = repair.target_id
        WHERE repair.action = 'restore'
          AND NOT EXISTS (
              SELECT 1 FROM taxon_repair_map AS releasing
              WHERE releasing.source_id = repair.target_id
                AND releasing.action IN ('move', 'merge')
          )
    ) THEN
        RAISE EXCEPTION 'Repair aborted: a restore target is unexpectedly occupied';
    END IF;

    IF EXISTS (
        SELECT 1
        FROM taxon_repair_map AS repair
        LEFT JOIN public.tlkptaxon AS target ON target.taxonid = repair.target_id
        WHERE repair.action IN ('deduplicate', 'merge')
          AND (target.taxonid IS NULL
            OR target.colid IS DISTINCT FROM repair.target_id
            OR target.taxonrankid IS DISTINCT FROM repair.rank_id
            OR target.label IS DISTINCT FROM repair.label
            OR target.htmllabel IS DISTINCT FROM repair.htmllabel
            OR target.parenttaxonid IS DISTINCT FROM repair.parent_id
            OR target.colparentid IS DISTINCT FROM repair.parent_id)
    ) THEN
        RAISE EXCEPTION 'Repair aborted: a merge/deduplication target is not canonical';
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

    IF {discard_preflight} AND (
        SELECT count(*) FROM public.tlkptaxon
        WHERE taxonid IN ({discard_sql})
          AND colid = taxonid
          AND label = 'n/a'
          AND htmllabel = 'n/a'
    ) <> {len(discard_ids)} THEN
        RAISE EXCEPTION 'Repair aborted: empty legacy placeholder content changed';
    END IF;

    IF {discard_preflight} AND EXISTS (
        SELECT 1 FROM public.tblelement
        WHERE taxonid IN ({discard_sql})
    ) THEN
        RAISE EXCEPTION 'Repair aborted: an empty legacy placeholder is referenced by an element';
    END IF;
END
$preflight$;

ALTER TABLE public.tblelement DROP CONSTRAINT IF EXISTS fkey_element_taxon;

-- Retarget element links while every original source ID is still present.
UPDATE public.tblelement AS element
SET taxonid = repair.target_id
FROM taxon_repair_map AS repair
WHERE element.taxonid = repair.source_id;

-- Remove an unusable empty-ID placeholder only when preflight proved that no
-- element references it.
DELETE FROM public.tlkptaxon
WHERE {discard_preflight}
  AND taxonid IN ({discard_sql});

-- Move displaced rows where their intended target ID is free.
UPDATE public.tlkptaxon AS taxon
SET taxonid = repair.target_id,
    colid = repair.target_id
FROM taxon_repair_map AS repair
WHERE taxon.taxonid = repair.source_id
  AND repair.action = 'move';

-- Remove rows whose canonical target already exists.
DELETE FROM public.tlkptaxon AS taxon
USING taxon_repair_map AS repair
WHERE taxon.taxonid = repair.source_id
  AND repair.action IN ('deduplicate', 'merge');

-- Restore legacy-prefixed primary keys after collisions have been freed.
UPDATE public.tlkptaxon AS taxon
SET taxonid = repair.target_id,
    colid = repair.target_id
FROM taxon_repair_map AS repair
WHERE taxon.taxonid = repair.source_id
  AND repair.action = 'restore';

-- Restore authoritative content and both copies of the parent relationship.
UPDATE public.tlkptaxon AS taxon
SET label = repair.label,
    htmllabel = repair.htmllabel,
    parenttaxonid = repair.parent_id,
    colparentid = repair.parent_id
FROM taxon_repair_map AS repair
WHERE taxon.taxonid = repair.target_id;

ALTER TABLE public.tblelement
    ADD CONSTRAINT fkey_element_taxon
    FOREIGN KEY (taxonid) REFERENCES public.tlkptaxon(taxonid)
    ON UPDATE NO ACTION ON DELETE NO ACTION;

DO $verify$
DECLARE
    repaired_count integer;
BEGIN
    IF (SELECT count(*) FROM public.tlkptaxon) <> {final_count} THEN
        RAISE EXCEPTION 'Repair verification failed: expected {final_count} final taxa, found %',
            (SELECT count(*) FROM public.tlkptaxon);
    END IF;

    SELECT count(*) INTO repaired_count
    FROM public.tlkptaxon AS taxon
    JOIN taxon_repair_map AS repair ON repair.target_id = taxon.taxonid
    WHERE taxon.colid = repair.target_id
      AND taxon.taxonrankid = repair.rank_id
      AND taxon.label = repair.label
      AND taxon.htmllabel = repair.htmllabel
      AND taxon.parenttaxonid IS NOT DISTINCT FROM repair.parent_id
      AND taxon.colparentid IS NOT DISTINCT FROM repair.parent_id;

    IF repaired_count <> {len(actions)} THEN
        RAISE EXCEPTION 'Repair verification failed: expected {len(actions)} canonical targets, found %',
            repaired_count;
    END IF;

    IF EXISTS (SELECT 1 FROM public.tlkptaxon WHERE taxonid LIKE 'legacy-%') THEN
        RAISE EXCEPTION 'Repair verification failed: legacy taxon IDs remain';
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
""")


def main(argv: Optional[Sequence[str]] = None) -> int:
    args = parse_args(argv)
    canonical, new_to_old = parse_upgrade(resolve_upgrade_sql(args.upgrade_sql))
    extend_canonical(canonical, args.reference_dump)
    rows = read_taxon_dump(args.dump)
    actions, discard_ids = build_actions(rows, canonical, new_to_old)
    if not actions:
        raise SystemExit(f"{args.dump}: no legacy taxon rows require repair")
    emit_sql(args.dump, rows, actions, discard_ids)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
