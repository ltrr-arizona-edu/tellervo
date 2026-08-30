#!/usr/bin/env bash
set -euo pipefail

PROGRAM_NAME="${0##*/}"
SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd -- "$SCRIPT_DIR/.." && pwd)"
REPAIR_SQL="$ROOT_DIR/Databases/scripts/recover-missing-taxonids-from-evidence.sql"
DEFAULT_SNAPSHOT="$ROOT_DIR/Databases/scripts/element-taxon-evidence-latest.csv.gz"
DEFAULT_SNAPSHOT_SHA256="6b92e5bd6f2acd1f51a38d30c4e6e7e36c829ec2f5388a30228125392d33b8da"
LEGACY_MAP="$ROOT_DIR/Databases/scripts/legacy-database-taxonid-map.csv"
LEGACY_MAP_SHA256="9dd9432b6c61da2d637d3cc5a2beb9d5f4bee46c01f0a71325f3681372c1f1b8"

usage() {
  cat <<EOF
Usage: $PROGRAM_NAME TARGET_DATABASE [EVIDENCE_SNAPSHOT]

Recover null/blank tblelement.taxonid values in TARGET_DATABASE using a local
conflict-free evidence snapshot. The bundled snapshot is used by default.

Run the generic tlkptaxon repair on the target database first. Database
arguments may be PostgreSQL database names or connection strings accepted by
psql. EVIDENCE_SNAPSHOT may be a plain CSV or gzip-compressed CSV. The target
update is performed in one transaction; no taxonfix connection is required.
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

[[ $# -ge 1 && $# -le 2 ]] || {
  usage >&2
  exit 1
}

command -v psql >/dev/null 2>&1 || fail "required command 'psql' was not found"
command -v gzip >/dev/null 2>&1 || fail "required command 'gzip' was not found"
command -v sha256sum >/dev/null 2>&1 \
  || fail "required command 'sha256sum' was not found"
[[ -f "$REPAIR_SQL" ]] || fail "repair SQL was not found: $REPAIR_SQL"
[[ -f "$LEGACY_MAP" ]] || fail "legacy taxon-ID map was not found: $LEGACY_MAP"

TARGET_DATABASE="$1"
EVIDENCE_SNAPSHOT="${2:-$DEFAULT_SNAPSHOT}"
[[ -f "$EVIDENCE_SNAPSHOT" ]] \
  || fail "evidence snapshot was not found: $EVIDENCE_SNAPSHOT"
WORK_DIR="$(mktemp -d "${TMPDIR:-/tmp}/tellervo-taxon-recovery.XXXXXXXX")"
EVIDENCE_FILE="$WORK_DIR/evidence.csv"
LEGACY_MAP_FILE="$WORK_DIR/legacy-map.csv"
RUN_SQL="$WORK_DIR/recover.sql"

cleanup() {
  rm -rf -- "$WORK_DIR"
}
trap cleanup EXIT

if [[ "$EVIDENCE_SNAPSHOT" == "$DEFAULT_SNAPSHOT" ]]; then
  ACTUAL_SHA256="$(sha256sum "$EVIDENCE_SNAPSHOT" | awk '{print $1}')"
  [[ "$ACTUAL_SHA256" == "$DEFAULT_SNAPSHOT_SHA256" ]] \
    || fail "bundled evidence snapshot checksum mismatch"
fi

ACTUAL_LEGACY_MAP_SHA256="$(sha256sum "$LEGACY_MAP" | awk '{print $1}')"
[[ "$ACTUAL_LEGACY_MAP_SHA256" == "$LEGACY_MAP_SHA256" ]] \
  || fail "bundled legacy taxon-ID map checksum mismatch"
cp -- "$LEGACY_MAP" "$LEGACY_MAP_FILE"

echo "Loading local evidence snapshot '$EVIDENCE_SNAPSHOT'..."
if [[ "$EVIDENCE_SNAPSHOT" == *.gz ]]; then
  gzip --decompress --stdout -- "$EVIDENCE_SNAPSHOT" > "$EVIDENCE_FILE"
else
  cp -- "$EVIDENCE_SNAPSHOT" "$EVIDENCE_FILE"
fi

[[ -s "$EVIDENCE_FILE" ]] || fail "evidence snapshot was empty"
[[ "$(head -n 1 "$EVIDENCE_FILE")" == \
  "elementid,taxonid,source,evidence_timestamp" ]] \
  || fail "evidence snapshot has an unexpected CSV header"
[[ "$(head -n 1 "$LEGACY_MAP_FILE")" == "old_id,taxonid" ]] \
  || fail "legacy taxon-ID map has an unexpected CSV header"

# psql does not expand variables inside \copy filenames. The placeholder is
# replaced only with a path produced by mktemp, whose template contains no SQL
# metacharacters.
sed \
  -e "s|__EVIDENCE_FILE__|$EVIDENCE_FILE|g" \
  -e "s|__LEGACY_MAP_FILE__|$LEGACY_MAP_FILE|g" \
  "$REPAIR_SQL" > "$RUN_SQL"

echo "Recovering missing taxon IDs in '$TARGET_DATABASE'..."
psql \
  --no-psqlrc \
  --set=ON_ERROR_STOP=1 \
  --dbname="$TARGET_DATABASE" \
  --file="$RUN_SQL"
