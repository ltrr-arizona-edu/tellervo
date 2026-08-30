#!/usr/bin/env bash
set -euo pipefail

PROGRAM_NAME="${0##*/}"

usage() {
  cat <<EOF
Usage: $PROGRAM_NAME DATABASE

Dump public.tlkptaxon from DATABASE into DATABASE-tlkptaxon.sql in the current
directory. The output file is suitable for generate-corrupt-tlkptaxon-repair.py.

The script refuses to overwrite an existing dump.
EOF
}

fail() {
  echo "$PROGRAM_NAME: $*" >&2
  exit 1
}

if [[ $# -eq 1 && ( "$1" == "-h" || "$1" == "--help" ) ]]; then
  usage
  exit 0
fi

[[ $# -eq 1 ]] || {
  usage >&2
  exit 1
}

command -v pg_dump >/dev/null 2>&1 || fail "required command 'pg_dump' was not found"

DATABASE="$1"
[[ "$DATABASE" =~ ^[A-Za-z0-9_][A-Za-z0-9_.-]*$ ]] \
  || fail "database name contains characters unsafe for an output filename: '$DATABASE'"

OUTPUT_FILE="${DATABASE}-tlkptaxon.sql"
[[ ! -e "$OUTPUT_FILE" ]] \
  || fail "output file already exists: '$OUTPUT_FILE'"

echo "Dumping public.tlkptaxon from '$DATABASE' to '$OUTPUT_FILE'..."
if ! pg_dump \
  --data-only \
  --table=public.tlkptaxon \
  --dbname="$DATABASE" \
  --file="$OUTPUT_FILE"; then
  rm -f -- "$OUTPUT_FILE"
  fail "pg_dump failed for database '$DATABASE'"
fi

echo "Created $OUTPUT_FILE"
