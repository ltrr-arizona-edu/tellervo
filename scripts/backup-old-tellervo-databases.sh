#!/usr/bin/env bash
set -uo pipefail

PROGRAM_NAME="${0##*/}"

DATABASES=(
  tellervoadp
  tellervoazmd
  tellervobigio
  tellervobigioold
  tellervobioplots
  tellervobioplotsold
  tellervocatalinafire
  tellervocatalinafireold
  tellervochristy
  tellervocsbr
  tellervocsbrbackup
  tellervocsbrv2
  tellervocure2024
  tellervocwf
  tellervocwfold
  tellervofia
  tellervofiaold
  tellervoguatemala
  tellervohydroclim
  tellervohydroclimold
  tellervoicms
  tellervoimls
  tellervoinjest1
  tellervoinjest2
  tellervoinjest3
  tellervoinjest4
  tellervoinjest5
  tellervoisonet
  tellervokessler
  tellervokesslerold
  tellervolatam
  tellervolegacy
  tellervoltrr
  tellervoltrrbackup
  tellervoltrrdev
  tellervomexarch
  tellervonavajoall
  tellervonavajocfi
  tellervonavajosurfacewater
  tellervoneh
  tellervopinaleno
  tellervoredhills
  tellervormbcp
  tellervormbcp2
  tellervoseq
  tellervospice
  tellervoswarch
  tellervotemplate
  tellervotest
  tellervouzzle
)

usage() {
  cat <<EOF
Usage: sudo ./$PROGRAM_NAME OUTPUT_DIRECTORY

Create validated, custom-format PostgreSQL backups of the old Tellervo
databases. A timestamped directory containing the archives, SHA256SUMS, and
FAILED_DATABASES (when applicable) is created below OUTPUT_DIRECTORY.

The script must run as root so pg_dump can run as the local postgres account.
Existing files are never overwritten.
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

[[ "$(id -u)" -eq 0 ]] || fail "run this script as root or with sudo"

for command in pg_dump pg_restore runuser sha256sum date mkdir chmod chown mv rm; do
  command -v "$command" >/dev/null 2>&1 \
    || fail "required command '$command' was not found"
done

OUTPUT_PARENT="$1"
[[ -d "$OUTPUT_PARENT" ]] \
  || fail "output directory '$OUTPUT_PARENT' does not exist"

RUN_ID="$(date -u +%Y%m%dT%H%M%SZ)"
OUTPUT_DIR="${OUTPUT_PARENT%/}/tellervo-backups-$RUN_ID"
mkdir --mode=0700 -- "$OUTPUT_DIR" \
  || fail "could not create '$OUTPUT_DIR'"

CHECKSUM_FILE="$OUTPUT_DIR/SHA256SUMS"
FAILURE_FILE="$OUTPUT_DIR/FAILED_DATABASES"
CURRENT_PARTIAL=""

cleanup_partial() {
  if [[ -n "$CURRENT_PARTIAL" ]]; then
    rm -f -- "$CURRENT_PARTIAL"
  fi
}
trap cleanup_partial EXIT
trap 'exit 130' INT
trap 'exit 143' TERM

backup_database() {
  local database="$1"
  local archive="$OUTPUT_DIR/$database.dump"
  local partial="$archive.partial"

  CURRENT_PARTIAL="$partial"
  echo "Backing up '$database'..."

  if ! runuser -u postgres -- \
    pg_dump --format=custom --dbname="$database" > "$partial"; then
    echo "$database: pg_dump failed" >&2
    rm -f -- "$partial"
    CURRENT_PARTIAL=""
    return 1
  fi

  if ! pg_restore --list "$partial" >/dev/null; then
    echo "$database: archive validation failed" >&2
    rm -f -- "$partial"
    CURRENT_PARTIAL=""
    return 1
  fi

  chmod 0600 -- "$partial"
  mv -- "$partial" "$archive"
  CURRENT_PARTIAL=""

  if ! (
    cd "$OUTPUT_DIR" &&
      sha256sum -- "$database.dump" >> "$CHECKSUM_FILE"
  ); then
    echo "$database: could not record archive checksum" >&2
    rm -f -- "$archive"
    return 1
  fi
  echo "Completed '$database'."
}

echo "Writing backups to: $OUTPUT_DIR"
echo

failure_count=0
for database in "${DATABASES[@]}"; do
  if ! backup_database "$database"; then
    printf '%s\n' "$database" >> "$FAILURE_FILE"
    ((failure_count += 1))
  fi
done

if [[ "${SUDO_UID:-}" =~ ^[0-9]+$ && "${SUDO_GID:-}" =~ ^[0-9]+$ ]]; then
  chown -R "$SUDO_UID:$SUDO_GID" -- "$OUTPUT_DIR"
fi

echo
if (( failure_count > 0 )); then
  echo "Finished with $failure_count failed database(s)."
  echo "See: $FAILURE_FILE"
  exit 1
fi

echo "All ${#DATABASES[@]} database backups completed successfully."
echo "Checksums: $CHECKSUM_FILE"
