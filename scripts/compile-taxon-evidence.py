#!/usr/bin/env python3
"""Compile element taxon evidence across every Tellervo database.

Source databases are queried read-only.  Evidence can be written to CSV and is
loaded into a separate PostgreSQL table by default.  Request-log create records
are accepted only when they match an existing element and its creation time;
update records must contain an existing element UUID.
"""

from __future__ import annotations

import argparse
import csv
from dataclasses import dataclass
import html
import io
import json
import os
from pathlib import Path
import re
import subprocess
import sys
import time
from typing import Iterable, Optional, Sequence


DEFAULT_ALIASES = Path(__file__).with_name("taxon-evidence-aliases.csv")
IDENTIFIER = re.compile(r"^[A-Za-z_][A-Za-z0-9_]*$")


class PsqlError(RuntimeError):
    pass


@dataclass(frozen=True, order=True)
class Evidence:
    elementid: str
    taxonid: str
    source: str
    evidence_timestamp: str


@dataclass(frozen=True)
class RawRequestEvidence:
    database: str
    elementid: str
    raw_taxonid: str
    normal: str
    source: str
    evidence_timestamp: str


@dataclass
class DatabaseResult:
    database: str
    direct: list[Evidence]
    requests: list[RawRequestEvidence]
    dictionary: list[tuple[str, str]]
    status: str = "OK"
    detail: str = ""


class Psql:
    def __init__(self, args: argparse.Namespace):
        self.command = [
            args.psql,
            "--no-psqlrc",
            "--no-align",
            "--tuples-only",
            "--quiet",
            "--set=ON_ERROR_STOP=1",
        ]
        if args.host:
            self.command.extend(["--host", args.host])
        if args.port:
            self.command.extend(["--port", str(args.port)])
        if args.username:
            self.command.extend(["--username", args.username])
        self.command_timeout = args.command_timeout
        self.statement_timeout_ms = args.statement_timeout * 1000

    def query(self, database: str, sql: str) -> list[str]:
        command = self.command + [
            "--dbname", database,
            "--command",
            ("SET default_transaction_read_only = on;\n"
             f"SET statement_timeout = {self.statement_timeout_ms};\n{sql}"),
        ]
        completed = self._run(command)
        return [line for line in completed.stdout.splitlines() if line]

    def write(self, database: str, sql: str) -> None:
        command = self.command + ["--dbname", database, "--file=-"]
        self._run(command, input_text=sql)

    def _run(self, command: list[str], input_text: Optional[str] = None) -> subprocess.CompletedProcess[str]:
        try:
            completed = subprocess.run(
                command,
                input=input_text,
                check=False,
                capture_output=True,
                text=True,
                timeout=self.command_timeout,
                env=os.environ.copy(),
            )
        except FileNotFoundError as exc:
            raise PsqlError(f"psql executable not found: {self.command[0]}") from exc
        except subprocess.TimeoutExpired as exc:
            raise PsqlError(
                f"psql timed out after {self.command_timeout} seconds") from exc
        if completed.returncode != 0:
            message = completed.stderr.strip() or completed.stdout.strip()
            raise PsqlError(message or f"psql exited with status {completed.returncode}")
        return completed


def parse_args(argv: Optional[Sequence[str]] = None) -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description=("Compile non-null element taxon evidence from tblelement and "
                     "tblrequestlog across all tellervo* databases."))
    parser.add_argument(
        "--databases", nargs="+", metavar="DATABASE",
        help="use only these databases instead of discovering tellervo* databases",
    )
    parser.add_argument("--prefix", default="tellervo", help="discovery prefix (default: tellervo)")
    parser.add_argument(
        "--maintenance-db", default="postgres",
        help="database used for discovery (default: postgres)",
    )
    parser.add_argument(
        "--destination-db",
        help="database receiving the evidence table (default: maintenance database)",
    )
    parser.add_argument(
        "--table", default="taxon_recovery.element_taxon_evidence",
        help="destination schema.table (default: taxon_recovery.element_taxon_evidence)",
    )
    parser.add_argument(
        "--replace", action="store_true",
        help="atomically replace an existing evidence table and its views",
    )
    parser.add_argument("--output-csv", type=Path, help="also write the evidence to a CSV file")
    parser.add_argument(
        "--aliases", type=Path, default=DEFAULT_ALIASES,
        help="reviewed raw_taxonid,taxonid aliases (default: bundled aliases)",
    )
    parser.add_argument(
        "--create-window-seconds", type=float, default=5.0, metavar="SECONDS",
        help="maximum create-request/element timestamp difference (default: 5)",
    )
    parser.add_argument("--host", help="PostgreSQL host or socket directory")
    parser.add_argument("--port", type=int, help="PostgreSQL port")
    parser.add_argument("--username", help="PostgreSQL user")
    parser.add_argument("--psql", default="psql", help="path to psql")
    parser.add_argument(
        "--statement-timeout", type=int, default=300, metavar="SECONDS",
        help="server-side timeout for each source query (default: 300)",
    )
    parser.add_argument(
        "--command-timeout", type=int, default=330, metavar="SECONDS",
        help="client-side timeout for each psql process (default: 330)",
    )
    parser.add_argument(
        "-v", "--verbose", action="count", default=0,
        help="show live progress; repeat for configuration details",
    )
    args = parser.parse_args(argv)
    if args.create_window_seconds <= 0:
        parser.error("--create-window-seconds must be positive")
    if args.statement_timeout <= 0 or args.command_timeout <= 0:
        parser.error("timeouts must be positive")
    parse_qualified_name(args.table, parser)
    return args


def parse_qualified_name(value: str, parser: Optional[argparse.ArgumentParser] = None) -> tuple[str, str]:
    parts = value.split(".")
    valid = len(parts) == 2 and all(IDENTIFIER.fullmatch(part) for part in parts)
    if not valid:
        message = "table must be an unquoted schema.table using letters, digits, and underscores"
        if parser:
            parser.error(message)
        raise ValueError(message)
    return parts[0], parts[1]


def sql_literal(value: str) -> str:
    return "'" + value.replace("'", "''") + "'"


def normalize_name(value: str) -> str:
    return " ".join(html.unescape(value).split()).casefold()


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
    return [database for database in rows if database.startswith(prefix)]


def relation_columns(psql: Psql, database: str) -> dict[str, set[str]]:
    rows = psql.query(
        database,
        """
SELECT json_build_object(
    'table', table_name,
    'column', column_name
)::text
FROM information_schema.columns
WHERE table_schema = 'public'
  AND table_name IN ('tblelement', 'tblrequestlog', 'tlkptaxon')
ORDER BY table_name, ordinal_position;
""",
    )
    result: dict[str, set[str]] = {}
    for row in rows:
        parsed = json.loads(row)
        result.setdefault(parsed["table"], set()).add(parsed["column"])
    return result


def parse_json_rows(rows: Iterable[str]) -> list[dict[str, object]]:
    return [json.loads(row) for row in rows]


def collect_database(psql: Psql, database: str, create_window: float) -> DatabaseResult:
    try:
        columns = relation_columns(psql, database)
        element_columns = columns.get("tblelement", set())
        required_element = {"elementid", "taxonid"}
        if not required_element.issubset(element_columns):
            missing = sorted(required_element.difference(element_columns))
            return DatabaseResult(database, [], [], [], "SKIPPED", "missing tblelement." + ", tblelement.".join(missing))

        timestamps = [
            column for column in ("lastmodifiedtimestamp", "createdtimestamp")
            if column in element_columns
        ]
        if not timestamps:
            return DatabaseResult(database, [], [], [], "SKIPPED", "tblelement has no timestamp column")
        timestamp_expression = "coalesce(" + ", ".join(
            f"element.{column}" for column in timestamps) + ")"
        database_literal = sql_literal(database)

        direct_rows = parse_json_rows(psql.query(
            database,
            f"""
SELECT json_build_object(
    'elementid', element.elementid,
    'taxonid', element.taxonid,
    'source', {database_literal} || ':tblelement',
    'timestamp', {timestamp_expression}
)::text
FROM public.tblelement AS element
WHERE element.taxonid IS NOT NULL
  AND {timestamp_expression} IS NOT NULL;
""",
        ))
        direct = [
            Evidence(
                str(row["elementid"]), str(row["taxonid"]),
                str(row["source"]), str(row["timestamp"]),
            )
            for row in direct_rows
        ]

        dictionary: list[tuple[str, str]] = []
        taxon_columns = columns.get("tlkptaxon", set())
        if {"taxonid", "label"}.issubset(taxon_columns):
            dictionary_rows = parse_json_rows(psql.query(
                database,
                """
SELECT json_build_object('taxonid', taxonid, 'label', label)::text
FROM public.tlkptaxon
WHERE taxonid IS NOT NULL
  AND nullif(btrim(label), '') IS NOT NULL;
""",
            ))
            dictionary = [
                (str(row["taxonid"]), str(row["label"]))
                for row in dictionary_rows
            ]

        request_columns = columns.get("tblrequestlog", set())
        request_required = {"requestlogid", "request", "createdtimestamp"}
        element_request_required = {"elementid", "objectid", "code", "createdtimestamp"}
        if not request_required.issubset(request_columns) or not element_request_required.issubset(element_columns):
            return DatabaseResult(
                database, direct, [], dictionary, "PARTIAL",
                "request-log correlation columns are missing",
            )

        request_rows = parse_json_rows(psql.query(
            database,
            f"""
WITH raw_request AS (
    SELECT requestlogid,
           createdtimestamp,
           substring(request FROM '<c:request type="([^"]+)"') AS kind,
           substring(request FROM 'parentEntityID="([^"]+)"') AS parent_id,
           replace(
               substring(request FROM '<tridas:title>([^<]*)</tridas:title>'),
               '&amp;', '&'
           ) AS title,
           substring(
               request FROM '<tridas:identifier[^>]*>([^<]+)</tridas:identifier>'
           ) AS identifier,
           substring(
               request FROM '<tridas:taxon[^>]*normalId="([^"]+)"'
           ) AS raw_taxonid,
           substring(
               request FROM '<tridas:taxon[^>]*normal="([^"]*)"'
           ) AS normal
    FROM public.tblrequestlog
    WHERE request ~* '<tridas:taxon'
), update_evidence AS (
    SELECT request.requestlogid,
           request.createdtimestamp,
           request.raw_taxonid,
           request.normal,
           element.elementid,
           'update'::text AS kind
    FROM raw_request AS request
    JOIN public.tblelement AS element
      ON request.identifier = element.elementid::text
    WHERE request.kind = 'update'
      AND nullif(request.raw_taxonid, '') IS NOT NULL
), create_match AS (
    SELECT request.requestlogid,
           request.createdtimestamp,
           request.raw_taxonid,
           request.normal,
           element.elementid,
           'create'::text AS kind,
           abs(extract(epoch FROM
               (request.createdtimestamp - element.createdtimestamp))) AS seconds_apart,
           row_number() OVER (
               PARTITION BY element.elementid
               ORDER BY abs(extract(epoch FROM
                            (request.createdtimestamp - element.createdtimestamp))),
                        request.requestlogid
           ) AS create_rank
    FROM raw_request AS request
    JOIN public.tblelement AS element
      ON request.parent_id = element.objectid::text
     AND request.title = element.code
    WHERE request.kind = 'create'
      AND nullif(request.raw_taxonid, '') IS NOT NULL
), create_evidence AS (
    SELECT requestlogid, createdtimestamp, raw_taxonid, normal, elementid, kind
    FROM create_match
    WHERE create_rank = 1
      AND seconds_apart <= {create_window}
), evidence AS (
    SELECT * FROM update_evidence
    UNION ALL
    SELECT * FROM create_evidence
)
SELECT json_build_object(
    'database', {database_literal},
    'elementid', elementid,
    'raw_taxonid', raw_taxonid,
    'normal', normal,
    'source', {database_literal} || ':tblrequestlog:' || kind || ':' || requestlogid,
    'timestamp', createdtimestamp
)::text
FROM evidence;
""",
        ))
        requests = [
            RawRequestEvidence(
                database=str(row["database"]),
                elementid=str(row["elementid"]),
                raw_taxonid=str(row["raw_taxonid"]),
                normal="" if row["normal"] is None else str(row["normal"]),
                source=str(row["source"]),
                evidence_timestamp=str(row["timestamp"]),
            )
            for row in request_rows
        ]
        return DatabaseResult(database, direct, requests, dictionary)
    except (PsqlError, ValueError, json.JSONDecodeError) as exc:
        return DatabaseResult(database, [], [], [], "ERROR", str(exc))


def load_aliases(path: Path) -> dict[str, str]:
    aliases: dict[str, str] = {}
    try:
        with path.open(newline="", encoding="utf-8") as handle:
            reader = csv.DictReader(
                line for line in handle if not line.lstrip().startswith("#"))
            if reader.fieldnames != ["raw_taxonid", "taxonid"]:
                raise ValueError("expected CSV header raw_taxonid,taxonid")
            for row in reader:
                raw_id = row["raw_taxonid"].strip()
                taxon_id = row["taxonid"].strip()
                if not raw_id or not taxon_id:
                    raise ValueError("alias IDs must not be empty")
                existing = aliases.get(raw_id)
                if existing is not None and existing != taxon_id:
                    raise ValueError(f"conflicting aliases for {raw_id}")
                aliases[raw_id] = taxon_id
    except OSError as exc:
        raise ValueError(f"cannot read aliases {path}: {exc}") from exc
    return aliases


def unique_name_map(pairs: Iterable[tuple[str, str]]) -> dict[str, str]:
    candidates: dict[str, set[str]] = {}
    for name, taxonid in pairs:
        key = normalize_name(name)
        if key:
            candidates.setdefault(key, set()).add(taxonid)
    return {
        name: next(iter(taxonids))
        for name, taxonids in candidates.items()
        if len(taxonids) == 1
    }


def resolve_requests(
    requests: Iterable[RawRequestEvidence],
    dictionaries: Iterable[tuple[str, str]],
    aliases: dict[str, str],
) -> tuple[list[Evidence], list[RawRequestEvidence]]:
    requests = list(requests)
    dictionaries = list(dictionaries)
    valid_ids = {taxonid for taxonid, _label in dictionaries}
    label_names = unique_name_map((label, taxonid) for taxonid, label in dictionaries)
    request_names = unique_name_map(
        (request.normal, request.raw_taxonid)
        for request in requests
        if request.raw_taxonid in valid_ids and request.normal
    )

    resolved: list[Evidence] = []
    unresolved: list[RawRequestEvidence] = []
    for request in requests:
        taxonid: Optional[str] = None
        if request.raw_taxonid in valid_ids:
            taxonid = request.raw_taxonid
        elif request.raw_taxonid in aliases and aliases[request.raw_taxonid] in valid_ids:
            taxonid = aliases[request.raw_taxonid]
        else:
            name = normalize_name(request.normal)
            candidates = {
                candidate for candidate in (label_names.get(name), request_names.get(name))
                if candidate is not None
            }
            if len(candidates) == 1:
                taxonid = next(iter(candidates))
        if taxonid is None:
            unresolved.append(request)
        else:
            resolved.append(Evidence(
                request.elementid, taxonid, request.source,
                request.evidence_timestamp,
            ))
    return resolved, unresolved


def csv_text(evidence: Iterable[Evidence], include_header: bool) -> str:
    output = io.StringIO(newline="")
    writer = csv.writer(output, lineterminator="\n")
    if include_header:
        writer.writerow(("elementid", "taxonid", "source", "evidence_timestamp"))
    for row in evidence:
        writer.writerow((row.elementid, row.taxonid, row.source, row.evidence_timestamp))
    return output.getvalue()


def destination_sql(
    schema: str,
    table: str,
    evidence: list[Evidence],
    replace: bool,
) -> str:
    latest_view = table + "_latest"
    conflict_view = table + "_conflicts"
    statements = ["\\set ON_ERROR_STOP on", "BEGIN;", f"CREATE SCHEMA IF NOT EXISTS {schema};"]
    if replace:
        statements.extend([
            f"DROP VIEW IF EXISTS {schema}.{conflict_view};",
            f"DROP VIEW IF EXISTS {schema}.{latest_view};",
            f"DROP TABLE IF EXISTS {schema}.{table};",
        ])
    statements.extend([
        f"""CREATE TABLE {schema}.{table} (
    elementid uuid NOT NULL,
    taxonid varchar NOT NULL,
    source text NOT NULL,
    evidence_timestamp timestamptz NOT NULL,
    PRIMARY KEY (elementid, taxonid, source, evidence_timestamp)
);""",
        f"COPY {schema}.{table} (elementid, taxonid, source, evidence_timestamp) FROM STDIN WITH (FORMAT csv);",
        csv_text(evidence, include_header=False).rstrip("\n"),
        "\\.",
        f"CREATE INDEX {table}_element_timestamp_idx ON {schema}.{table} (elementid, evidence_timestamp DESC);",
        f"CREATE INDEX {table}_taxon_idx ON {schema}.{table} (taxonid);",
        f"""CREATE VIEW {schema}.{latest_view} AS
WITH ranked AS (
    SELECT evidence.*,
           row_number() OVER (
               PARTITION BY elementid
               ORDER BY evidence_timestamp DESC,
                        CASE
                          WHEN source LIKE '%:tblelement' THEN 0
                          WHEN source LIKE '%:tblrequestlog:create:%' THEN 1
                          ELSE 2
                        END,
                        source
           ) AS evidence_rank
    FROM {schema}.{table} AS evidence
)
SELECT elementid, taxonid, source, evidence_timestamp
FROM ranked
WHERE evidence_rank = 1;""",
        f"""CREATE VIEW {schema}.{conflict_view} AS
WITH latest_timestamp AS (
    SELECT elementid, max(evidence_timestamp) AS evidence_timestamp
    FROM {schema}.{table}
    GROUP BY elementid
)
SELECT evidence.elementid,
       evidence.evidence_timestamp,
       array_agg(DISTINCT evidence.taxonid ORDER BY evidence.taxonid) AS taxonids,
       array_agg(evidence.source ORDER BY evidence.source) AS sources
FROM {schema}.{table} AS evidence
JOIN latest_timestamp USING (elementid, evidence_timestamp)
GROUP BY evidence.elementid, evidence.evidence_timestamp
HAVING count(DISTINCT evidence.taxonid) > 1;""",
        "COMMIT;",
        "",
    ])
    return "\n".join(statements)


def print_summary(
    results: list[DatabaseResult], resolved_count: int,
    unresolved: list[RawRequestEvidence], total_evidence: int,
) -> None:
    print("DATABASE\tDIRECT\tREQUESTS\tSTATUS\tDETAIL", file=sys.stderr)
    for result in results:
        print(
            f"{result.database}\t{len(result.direct)}\t{len(result.requests)}\t"
            f"{result.status}\t{result.detail}",
            file=sys.stderr,
        )
    print(
        f"Compiled {total_evidence} unique evidence rows; resolved "
        f"{resolved_count} request rows; skipped {len(unresolved)} unresolved request rows.",
        file=sys.stderr,
    )
    if unresolved:
        counts: dict[tuple[str, str], int] = {}
        for request in unresolved:
            key = (request.raw_taxonid, html.unescape(request.normal))
            counts[key] = counts.get(key, 0) + 1
        print("Unresolved request taxa (top 20):", file=sys.stderr)
        for (raw_id, normal), count in sorted(
                counts.items(), key=lambda item: (-item[1], item[0]))[:20]:
            print(f"  {raw_id}\t{normal}\t{count}", file=sys.stderr)


def verbose_log(args: argparse.Namespace, message: str, level: int = 1) -> None:
    if args.verbose >= level:
        print(message, file=sys.stderr, flush=True)


def main(argv: Optional[Sequence[str]] = None) -> int:
    args = parse_args(argv)
    psql = Psql(args)
    destination = args.destination_db or args.maintenance_db
    schema, table = parse_qualified_name(args.table)
    verbose_log(
        args,
        f"Connecting through {args.host or 'the local PostgreSQL socket'} as "
        f"{args.username or 'the operating-system user'}.",
        level=2,
    )
    verbose_log(
        args,
        f"Discovery database: {args.maintenance_db}; destination: "
        f"{destination}.{schema}.{table}.",
        level=2,
    )
    try:
        verbose_log(args, "Discovering source databases...")
        databases = (sorted(set(args.databases)) if args.databases
                     else discover_databases(psql, args.maintenance_db, args.prefix))
        aliases = load_aliases(args.aliases)
    except (PsqlError, ValueError) as exc:
        print(f"setup failed: {exc}", file=sys.stderr)
        return 2
    if not databases:
        print(f"no databases found with prefix {args.prefix!r}", file=sys.stderr)
        return 2

    verbose_log(
        args,
        f"Found {len(databases)} source database(s); loaded {len(aliases)} "
        f"reviewed alias(es) from {args.aliases}.",
    )
    verbose_log(
        args,
        f"A create request must be within {args.create_window_seconds:g} second(s) "
        "of the element creation timestamp.",
        level=2,
    )

    results: list[DatabaseResult] = []
    collection_started = time.monotonic()
    for index, database in enumerate(databases, start=1):
        verbose_log(args, f"[{index}/{len(databases)}] Reading {database}...")
        started = time.monotonic()
        result = collect_database(psql, database, args.create_window_seconds)
        results.append(result)
        elapsed = time.monotonic() - started
        detail = f"; {result.detail}" if result.detail else ""
        verbose_log(
            args,
            f"[{index}/{len(databases)}] {database}: {result.status}; "
            f"{len(result.direct)} direct, {len(result.requests)} request, "
            f"{len(result.dictionary)} dictionary rows in {elapsed:.1f}s{detail}",
        )

    verbose_log(
        args,
        f"Source collection completed in {time.monotonic() - collection_started:.1f}s. "
        "Resolving request taxon IDs...",
    )
    direct = [row for result in results for row in result.direct]
    raw_requests = [row for result in results for row in result.requests]
    dictionaries = [row for result in results for row in result.dictionary]
    resolved, unresolved = resolve_requests(raw_requests, dictionaries, aliases)
    evidence = sorted(set(direct + resolved))
    verbose_log(
        args,
        f"Resolution complete: {len(direct)} direct rows, {len(resolved)} resolved "
        f"request rows, {len(unresolved)} unresolved request rows, and "
        f"{len(evidence)} unique evidence rows.",
    )

    if args.output_csv:
        verbose_log(args, f"Writing CSV to {args.output_csv}...")
        try:
            args.output_csv.write_text(csv_text(evidence, include_header=True), encoding="utf-8")
        except OSError as exc:
            print(f"could not write {args.output_csv}: {exc}", file=sys.stderr)
            return 2
        verbose_log(args, f"Wrote {len(evidence)} CSV data rows to {args.output_csv}.")

    action = "Replacing" if args.replace else "Creating"
    verbose_log(
        args,
        f"{action} {destination}.{schema}.{table} and loading "
        f"{len(evidence)} rows...",
    )
    load_started = time.monotonic()
    try:
        psql.write(destination, destination_sql(schema, table, evidence, args.replace))
    except PsqlError as exc:
        print(f"could not load {schema}.{table} in {destination}: {exc}", file=sys.stderr)
        return 2
    verbose_log(
        args,
        f"Destination load and view creation completed in "
        f"{time.monotonic() - load_started:.1f}s.",
    )

    print_summary(results, len(resolved), unresolved, len(evidence))
    print(f"Loaded {len(evidence)} rows into {destination}.{schema}.{table}.", file=sys.stderr)
    return 1 if any(result.status == "ERROR" for result in results) else 0


if __name__ == "__main__":
    raise SystemExit(main())
