#!/usr/bin/env bash
set -uo pipefail

PROGRAM_NAME="${0##*/}"

usage() {
  cat <<EOF
Usage: sudo ./$PROGRAM_NAME BACKUP_DIRECTORY [DATABASE_USER]

Restore every *.dump archive in BACKUP_DIRECTORY to a database with the same
name. DATABASE_USER defaults to webuser.

The script verifies SHA256SUMS before making any database changes, restores
through tellervo-server-db for PostgreSQL compatibility handling, skips
databases that already exist, and writes a timestamped restore report beneath
BACKUP_DIRECTORY.
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

[[ "$(id -u)" -eq 0 ]] || fail "run this script as root or with sudo"

BACKUP_DIR="${1%/}"
DB_USER="${2:-webuser}"

[[ -d "$BACKUP_DIR" ]] \
  || fail "backup directory '$BACKUP_DIR' does not exist"
[[ "$DB_USER" =~ ^[A-Za-z_][A-Za-z0-9_]{0,62}$ ]] \
  || fail "database user names must contain at most 63 letters, numbers or underscores and cannot start with a number"

for command in tellervo-server-db psql pg_restore runuser sha256sum date mkdir chown grep; do
  command -v "$command" >/dev/null 2>&1 \
    || fail "required command '$command' was not found"
done

CHECKSUM_FILE="$BACKUP_DIR/SHA256SUMS"
[[ -r "$CHECKSUM_FILE" ]] \
  || fail "checksum manifest '$CHECKSUM_FILE' is not readable"

if [[ -s "$BACKUP_DIR/FAILED_DATABASES" ]]; then
  echo "WARNING: the backup set records databases that were not backed up:" >&2
  while IFS= read -r failed_database; do
    echo "  $failed_database" >&2
  done < "$BACKUP_DIR/FAILED_DATABASES"
  echo "Only the available, verified archives will be restored." >&2
  echo >&2
fi

shopt -s nullglob
ARCHIVES=("$BACKUP_DIR"/*.dump)
shopt -u nullglob
(( ${#ARCHIVES[@]} > 0 )) \
  || fail "no .dump archives were found in '$BACKUP_DIR'"

echo "Verifying the backup set..."
(
  cd "$BACKUP_DIR" &&
    sha256sum --check --strict SHA256SUMS
) || fail "checksum verification failed; no databases were changed"

for archive in "${ARCHIVES[@]}"; do
  filename="${archive##*/}"
  database="${filename%.dump}"
  [[ "$database" =~ ^[A-Za-z_][A-Za-z0-9_]{0,62}$ ]] \
    || fail "archive filename '$filename' is not a valid database name"
  if ! grep -Eq "^[0-9a-fA-F]{64}  ${database}\\.dump$" "$CHECKSUM_FILE"; then
    fail "archive '$filename' is not listed in the checksum manifest"
  fi
  pg_restore --list "$archive" >/dev/null \
    || fail "archive '$archive' is not a readable custom-format PostgreSQL backup"
done

RUN_ID="$(date -u +%Y%m%dT%H%M%SZ)"
REPORT_DIR="$BACKUP_DIR/restore-report-$RUN_ID"
mkdir --mode=0700 -- "$REPORT_DIR" \
  || fail "could not create restore report directory '$REPORT_DIR'"

RESTORED_FILE="$REPORT_DIR/RESTORED_DATABASES"
SKIPPED_FILE="$REPORT_DIR/SKIPPED_EXISTING_DATABASES"
FAILED_FILE="$REPORT_DIR/FAILED_DATABASES"

database_exists() {
  local database="$1"
  local query_result

  if ! query_result="$(
    runuser -u postgres -- psql --dbname=postgres --tuples-only --no-align \
      --set=ON_ERROR_STOP=1 \
      --set=db_name="$database" <<'SQL'
SELECT 1 FROM pg_database WHERE datname = :'db_name';
SQL
  )"; then
    return 2
  fi

  [[ "$query_result" == "1" ]]
}

restored_count=0
skipped_count=0
failure_count=0

echo
echo "Restoring ${#ARCHIVES[@]} database archive(s) as role '$DB_USER'..."

for archive in "${ARCHIVES[@]}"; do
  filename="${archive##*/}"
  database="${filename%.dump}"

  if [[ ! "$database" =~ ^[A-Za-z_][A-Za-z0-9_]{0,62}$ ]]; then
    echo "$database: archive filename is not a valid database name" >&2
    printf '%s\n' "$database" >> "$FAILED_FILE"
    ((failure_count += 1))
    continue
  fi

  database_exists "$database"
  exists_status=$?
  if (( exists_status == 0 )); then
    echo "Skipping '$database': database already exists."
    printf '%s\n' "$database" >> "$SKIPPED_FILE"
    ((skipped_count += 1))
    continue
  elif (( exists_status == 2 )); then
    echo "$database: could not query the PostgreSQL server" >&2
    printf '%s\n' "$database" >> "$FAILED_FILE"
    ((failure_count += 1))
    continue
  fi

  echo
  echo "Restoring '$database'..."
  if tellervo-server-db restore \
    --dbname "$database" \
    --file "$archive" \
    --user "$DB_USER"; then
    printf '%s\n' "$database" >> "$RESTORED_FILE"
    ((restored_count += 1))
  else
    printf '%s\n' "$database" >> "$FAILED_FILE"
    ((failure_count += 1))
  fi
done

if [[ "${SUDO_UID:-}" =~ ^[0-9]+$ && "${SUDO_GID:-}" =~ ^[0-9]+$ ]]; then
  chown -R "$SUDO_UID:$SUDO_GID" -- "$REPORT_DIR"
fi

echo
echo "Restore report: $REPORT_DIR"
echo "Restored: $restored_count"
echo "Skipped because already present: $skipped_count"
echo "Failed: $failure_count"

if (( failure_count > 0 )); then
  exit 1
fi
