#!/usr/bin/env python3
"""Audit PostgreSQL databases for the known taxon-upgrade-6 corruption.

This script is deliberately read-only.  It recognizes the exact database
shape handled by Databases/scripts/repair-corrupt-tlkptaxon.sql and reports
similar-but-different shapes as REVIEW instead of assuming they are safe to
repair.
"""

from __future__ import annotations

import argparse
import hashlib
import json
import os
from dataclasses import asdict, dataclass
import subprocess
import sys
from typing import Iterable, Optional, Sequence


SUPPORTED_LEGACY_COUNT = 864
SUPPORTED_COLLISION_COUNT = 3
SUPPORTED_LEGACY_FINGERPRINT = (
    "c72c710f5afac279a728336a7f0972564ab37b21b518060a67de002619fdca6d"
)


def legacy_fingerprint(ids: Iterable[str]) -> str:
    canonical = "".join(f"{taxon_id}\n" for taxon_id in sorted(ids))
    return hashlib.sha256(canonical.encode("utf-8")).hexdigest()


@dataclass
class AuditResult:
    database: str
    status: str
    legacy_rows: Optional[int] = None
    legacy_na_rows: Optional[int] = None
    collision_rows: Optional[int] = None
    orphaned_element_links: Optional[int] = None
    detail: str = ""


class PsqlError(RuntimeError):
    pass


class Psql:
    def __init__(self, args: argparse.Namespace):
        self.command = [args.psql, "--no-psqlrc", "--no-align", "--tuples-only", "--quiet",
                        "--set=ON_ERROR_STOP=1"]
        if args.host:
            self.command.extend(["--host", args.host])
        if args.port:
            self.command.extend(["--port", str(args.port)])
        if args.username:
            self.command.extend(["--username", args.username])
        self.timeout = args.command_timeout
        self.statement_timeout_ms = args.statement_timeout * 1000

    def query(self, database: str, sql: str) -> list[str]:
        command = self.command + ["--dbname", database, "--command",
                                  f"SET statement_timeout = {self.statement_timeout_ms};\n{sql}"]
        try:
            completed = subprocess.run(
                command,
                check=False,
                capture_output=True,
                text=True,
                timeout=self.timeout,
                env=os.environ.copy(),
            )
        except FileNotFoundError as exc:
            raise PsqlError(f"psql executable not found: {self.command[0]}") from exc
        except subprocess.TimeoutExpired as exc:
            raise PsqlError(f"psql timed out after {self.timeout} seconds") from exc

        if completed.returncode != 0:
            message = completed.stderr.strip() or completed.stdout.strip()
            raise PsqlError(message or f"psql exited with status {completed.returncode}")
        return [line for line in completed.stdout.splitlines() if line]


def parse_args(argv: Optional[Sequence[str]] = None) -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Read-only audit for the known tlkptaxon upgrade-6 corruption.")
    targets = parser.add_mutually_exclusive_group(required=True)
    targets.add_argument("--all", action="store_true",
                         help="audit all connectable, non-template databases")
    targets.add_argument("--databases", nargs="+", metavar="DATABASE",
                         help="audit only the named databases")
    parser.add_argument("--prefix", default="",
                        help="with --all, audit only database names with this prefix")
    parser.add_argument("--maintenance-db", default="postgres",
                        help="database used to discover --all targets (default: postgres)")
    parser.add_argument("--host", help="PostgreSQL host or socket directory")
    parser.add_argument("--port", type=int, help="PostgreSQL port")
    parser.add_argument("--username", help="PostgreSQL user")
    parser.add_argument("--psql", default="psql", help="path to psql")
    parser.add_argument("--statement-timeout", type=int, default=30, metavar="SECONDS",
                        help="server-side timeout for each audit query (default: 30)")
    parser.add_argument("--command-timeout", type=int, default=45, metavar="SECONDS",
                        help="client-side timeout for each psql process (default: 45)")
    parser.add_argument("--json", action="store_true", help="write JSON instead of a table")
    args = parser.parse_args(argv)
    if args.prefix and not args.all:
        parser.error("--prefix can only be used with --all")
    if args.statement_timeout <= 0 or args.command_timeout <= 0:
        parser.error("timeouts must be positive")
    return args


def discover_databases(psql: Psql, maintenance_db: str, prefix: str) -> list[str]:
    rows = psql.query(
        maintenance_db,
        """
SELECT datname
FROM pg_database
WHERE datallowconn
  AND NOT datistemplate
ORDER BY datname;
""",
    )
    return [name for name in rows if not prefix or name.startswith(prefix)]


def scalar(psql: Psql, database: str, sql: str) -> str:
    rows = psql.query(database, sql)
    if len(rows) != 1:
        raise PsqlError(f"expected one result row, received {len(rows)}")
    return rows[0]


def audit_database(psql: Psql, database: str) -> AuditResult:
    try:
        relation_state = scalar(
            psql,
            database,
            """
SELECT concat_ws(E'\\t',
    (to_regclass('public.tlkptaxon') IS NOT NULL)::text,
    (to_regclass('public.tblelement') IS NOT NULL)::text);
""",
        ).split("\t")
        has_taxa = relation_state[0] == "true"
        has_elements = len(relation_state) > 1 and relation_state[1] == "true"
        if not has_taxa:
            return AuditResult(database, "NOT_APPLICABLE", detail="public.tlkptaxon is absent")

        column_rows = psql.query(
            database,
            """
SELECT column_name || E'\\t' || data_type
FROM information_schema.columns
WHERE table_schema = 'public'
  AND table_name = 'tlkptaxon'
ORDER BY ordinal_position;
""",
        )
        columns = dict(row.split("\t", 1) for row in column_rows)
        required = {"taxonid", "colid", "label", "htmllabel",
                    "parenttaxonid", "colparentid"}
        missing = sorted(required.difference(columns))
        if missing:
            return AuditResult(
                database,
                "NOT_APPLICABLE",
                detail="taxon schema predates repair shape; missing " + ", ".join(missing),
            )
        if columns["taxonid"] not in {"character varying", "text", "character"}:
            return AuditResult(
                database,
                "NOT_APPLICABLE",
                detail=f"taxonid type is {columns['taxonid']}, not a post-upgrade text type",
            )

        metrics = scalar(
            psql,
            database,
            """
SELECT concat_ws(E'\\t',
    count(*)::text,
    count(*) FILTER (WHERE taxonid LIKE 'legacy-%')::text,
    count(*) FILTER (WHERE taxonid LIKE 'legacy-%' AND label = 'n/a')::text,
    count(*) FILTER (WHERE label = 'n/a')::text,
    count(*) FILTER (WHERE EXISTS (
        SELECT 1
        FROM public.tlkptaxon AS legacy
        WHERE legacy.taxonid = 'legacy-' || tlkptaxon.taxonid
    ))::text,
    count(*) FILTER (WHERE taxonid IN
        ('legacy-5KWC', 'legacy-6R6H5', 'legacy-63SMF'))::text,
    count(*) FILTER (WHERE COALESCE(colid, '') = '')::text,
    count(*) FILTER (WHERE taxonid IS DISTINCT FROM colid)::text)
FROM public.tlkptaxon;
""",
        ).split("\t")
        if len(metrics) != 8:
            raise PsqlError(f"expected eight taxon metrics, received {len(metrics)}")
        (total_rows, legacy_rows, legacy_na_rows, na_rows, collision_rows,
         required_legacy_rows, empty_colid_rows, mismatched_id_rows) = map(int, metrics)

        actual_legacy_ids = psql.query(
            database,
            """
SELECT taxonid
FROM public.tlkptaxon
WHERE taxonid LIKE 'legacy-%'
ORDER BY taxonid;
""",
        )
        legacy_ids_match = (
            legacy_fingerprint(actual_legacy_ids) == SUPPORTED_LEGACY_FINGERPRINT)

        orphaned_links: Optional[int] = None
        if has_elements:
            element_columns = psql.query(
                database,
                """
SELECT column_name
FROM information_schema.columns
WHERE table_schema = 'public'
  AND table_name = 'tblelement'
  AND column_name = 'taxonid';
""",
            )
            if element_columns:
                orphaned_links = int(scalar(
                    psql,
                    database,
                    """
SELECT count(*)
FROM public.tblelement AS element
LEFT JOIN public.tlkptaxon AS taxon ON taxon.taxonid = element.taxonid
WHERE element.taxonid IS NOT NULL
  AND taxon.taxonid IS NULL;
""",
                ))

        unexpected_fk_count = int(scalar(
            psql,
            database,
            """
SELECT count(*)
FROM pg_constraint
WHERE contype = 'f'
  AND confrelid = 'public.tlkptaxon'::regclass
  AND conrelid <> COALESCE(to_regclass('public.tblelement'), 0::oid);
""",
        ))

        details = [f"{total_rows} taxa"]
        if na_rows:
            details.append(f"{na_rows} n/a labels")
        if empty_colid_rows:
            details.append(f"{empty_colid_rows} blank colid values")
        if mismatched_id_rows:
            details.append(f"{mismatched_id_rows} taxonid/colid mismatches")
        if orphaned_links:
            details.append(f"{orphaned_links} orphaned element links")
        if unexpected_fk_count:
            details.append(f"{unexpected_fk_count} unexpected foreign keys")
        if legacy_rows and not legacy_ids_match:
            details.append("legacy ID set differs from supported repair")

        exact_corruption = (
            legacy_rows == SUPPORTED_LEGACY_COUNT
            and legacy_na_rows == SUPPORTED_LEGACY_COUNT
            and collision_rows == SUPPORTED_COLLISION_COUNT
            and required_legacy_rows == SUPPORTED_COLLISION_COUNT
            and legacy_ids_match
            and not orphaned_links
            and unexpected_fk_count == 0
        )
        has_corruption_indicators = legacy_rows > 0 or collision_rows > 0

        if exact_corruption:
            status = "BROKEN"
            details.insert(0, "exact supported upgrade-6 rerun signature")
        elif has_corruption_indicators:
            status = "REVIEW"
            details.insert(0, "corruption indicators differ from supported repair")
        elif (orphaned_links or unexpected_fk_count or empty_colid_rows
              or mismatched_id_rows):
            status = "REVIEW"
            details.insert(0, "database shape is incompatible with the standard repair")
        else:
            status = "OK"
            details.insert(0, "known corruption signature absent")

        return AuditResult(
            database=database,
            status=status,
            legacy_rows=legacy_rows,
            legacy_na_rows=legacy_na_rows,
            collision_rows=collision_rows,
            orphaned_element_links=orphaned_links,
            detail="; ".join(details),
        )
    except (PsqlError, ValueError, IndexError) as exc:
        return AuditResult(database, "ERROR", detail=str(exc).replace("\n", " "))


def display_value(value: Optional[int]) -> str:
    return "-" if value is None else str(value)


def print_table(results: Iterable[AuditResult]) -> None:
    rows = [[
        result.database,
        result.status,
        display_value(result.legacy_rows),
        display_value(result.collision_rows),
        display_value(result.orphaned_element_links),
        result.detail,
    ] for result in results]
    headers = ["DATABASE", "STATUS", "LEGACY", "COLLISIONS", "ORPHANS", "DETAIL"]
    widths = [len(header) for header in headers]
    for row in rows:
        for index, value in enumerate(row):
            widths[index] = max(widths[index], len(value))
    print("  ".join(header.ljust(widths[index]) for index, header in enumerate(headers)))
    print("  ".join("-" * width for width in widths))
    for row in rows:
        print("  ".join(value.ljust(widths[index]) for index, value in enumerate(row)))


def exit_status(results: Iterable[AuditResult]) -> int:
    statuses = {result.status for result in results}
    if "ERROR" in statuses or "REVIEW" in statuses:
        return 1
    if "BROKEN" in statuses:
        return 2
    return 0


def main(argv: Optional[Sequence[str]] = None) -> int:
    args = parse_args(argv)
    psql = Psql(args)
    try:
        databases = (discover_databases(psql, args.maintenance_db, args.prefix)
                     if args.all else args.databases)
    except PsqlError as exc:
        print(f"database discovery failed: {exc}", file=sys.stderr)
        return 1

    if not databases:
        print("no databases matched the requested scope", file=sys.stderr)
        return 1

    results = [audit_database(psql, database) for database in databases]
    if args.json:
        json.dump([asdict(result) for result in results], sys.stdout, indent=2)
        print()
    else:
        print_table(results)
    return exit_status(results)


if __name__ == "__main__":
    raise SystemExit(main())
