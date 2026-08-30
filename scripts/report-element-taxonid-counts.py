#!/usr/bin/env python3
"""Report tblelement.taxonid null/not-null counts across databases."""

from __future__ import annotations

import argparse
from dataclasses import asdict, dataclass
import json
import os
import subprocess
import sys
from typing import Optional, Sequence


@dataclass
class CountResult:
    database: str
    null_taxonid: Optional[int]
    not_null_taxonid: Optional[int]
    status: str
    detail: str = ""


class PsqlError(RuntimeError):
    pass


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
        try:
            completed = subprocess.run(
                command,
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
        return [line for line in completed.stdout.splitlines() if line]


def parse_args(argv: Optional[Sequence[str]] = None) -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description=("Report tblelement.taxonid null/not-null row counts "
                     "for all tellervo* databases."))
    parser.add_argument(
        "--databases", nargs="+", metavar="DATABASE",
        help="report only the named databases instead of discovering the fleet",
    )
    parser.add_argument(
        "--prefix", default="tellervo",
        help="database prefix used during discovery (default: tellervo)",
    )
    parser.add_argument(
        "--maintenance-db", default="postgres",
        help="database used to discover targets (default: postgres)",
    )
    parser.add_argument("--host", help="PostgreSQL host or socket directory")
    parser.add_argument("--port", type=int, help="PostgreSQL port")
    parser.add_argument("--username", help="PostgreSQL user")
    parser.add_argument("--psql", default="psql", help="path to psql")
    parser.add_argument(
        "--statement-timeout", type=int, default=120, metavar="SECONDS",
        help="server-side timeout for each count query (default: 120)",
    )
    parser.add_argument(
        "--command-timeout", type=int, default=135, metavar="SECONDS",
        help="client-side timeout for each psql process (default: 135)",
    )
    parser.add_argument("--json", action="store_true", help="write JSON instead of a table")
    args = parser.parse_args(argv)
    if args.statement_timeout <= 0 or args.command_timeout <= 0:
        parser.error("timeouts must be positive")
    return args


def scalar(psql: Psql, database: str, sql: str) -> str:
    rows = psql.query(database, sql)
    if len(rows) != 1:
        raise PsqlError(f"expected one result row, received {len(rows)}")
    return rows[0]


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


def count_database(psql: Psql, database: str) -> CountResult:
    try:
        has_table = scalar(
            psql,
            database,
            "SELECT (to_regclass('public.tblelement') IS NOT NULL)::text;",
        )
        if has_table != "true":
            return CountResult(
                database, None, None, "MISSING", "missing public.tblelement")

        counts = scalar(
            psql,
            database,
            """
SELECT concat_ws(E'\\t',
    (SELECT count(*) FROM public.tblelement WHERE taxonid IS NULL)::text,
    (SELECT count(*) FROM public.tblelement WHERE taxonid IS NOT NULL)::text);
""",
        ).split("\t")
        if len(counts) != 2:
            raise PsqlError(f"expected two counts, received {len(counts)}")
        return CountResult(database, int(counts[0]), int(counts[1]), "OK")
    except (PsqlError, ValueError) as exc:
        return CountResult(database, None, None, "ERROR", str(exc))


def write_table(results: list[CountResult]) -> None:
    headers = ("DATABASE", "NULL_TAXONID", "NOT_NULL_TAXONID", "STATUS", "DETAIL")
    rows = [
        (
            result.database,
            "-" if result.null_taxonid is None else str(result.null_taxonid),
            "-" if result.not_null_taxonid is None else str(result.not_null_taxonid),
            result.status,
            result.detail,
        )
        for result in results
    ]
    widths = [
        max(len(headers[index]), *(len(row[index]) for row in rows))
        for index in range(len(headers))
    ] if rows else [len(header) for header in headers]

    def line(values: tuple[str, ...]) -> str:
        return "  ".join(
            value.rjust(widths[index]) if index in (1, 2)
            else value.ljust(widths[index])
            for index, value in enumerate(values)
        ).rstrip()

    print(line(headers))
    print(line(tuple("-" * width for width in widths)))
    for row in rows:
        print(line(row))


def main(argv: Optional[Sequence[str]] = None) -> int:
    args = parse_args(argv)
    psql = Psql(args)
    try:
        databases = (sorted(set(args.databases)) if args.databases
                     else discover_databases(psql, args.maintenance_db, args.prefix))
    except PsqlError as exc:
        print(f"database discovery failed: {exc}", file=sys.stderr)
        return 2

    if not databases:
        print(f"no databases found with prefix {args.prefix!r}", file=sys.stderr)
        return 2

    results = [count_database(psql, database) for database in databases]
    if args.json:
        print(json.dumps([asdict(result) for result in results], indent=2))
    else:
        write_table(results)
    return 1 if any(result.status != "OK" for result in results) else 0


if __name__ == "__main__":
    raise SystemExit(main())
