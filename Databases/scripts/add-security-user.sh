#!/usr/bin/env bash
set -uo pipefail

PROGRAM_NAME="${0##*/}"
DEFAULT_SOURCE_DB="tellervoltrr"

usage() {
  cat <<EOF
Usage: $PROGRAM_NAME USERNAME (TARGET_DATABASE|all) [SOURCE_DATABASE]

Look up USERNAME's tblsecurityuser record in SOURCE_DATABASE (default:
$DEFAULT_SOURCE_DB) and add it, along with its security group memberships,
to TARGET_DATABASE. If TARGET_DATABASE is "all", this is done to every other
database on the server whose name starts with "tellervo".

securityuserid is a uuid (not a per-database serial), so the record is
inserted with the *same* securityuserid it has in SOURCE_DATABASE in every
target -- this is the point of it being a uuid: the same person keeps one
stable identity across every Tellervo database, the same way the existing
database-merge tooling relies on. A target database that already has a row
with that securityuserid is left untouched. If a target database already
has a *different* user under the same username, that database is flagged
as a conflict and skipped entirely (including its group memberships) rather
than guessed at.

For each security group the user belongs to in SOURCE_DATABASE, this script
adds the same membership in the target database's group of the same name
(this is where permissions in Tellervo actually come from -- permissions are
granted to groups, not users). Membership sync runs whether or not the user
row already existed, so it is safe to re-run to pick up new groups. A group
that already has the user as a member is left untouched; a target database
that has no group with that name is skipped with a warning -- group
definitions and their permissions themselves are per-database configuration
and are never created or modified by this script.

Database arguments may be plain PostgreSQL database names or anything else
accepted by psql's --dbname, and connections use your normal psql
environment (PGHOST/PGUSER/.pgpass/connection service file, etc). No
database password is ever read or written by this script.
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

[[ $# -ge 2 && $# -le 3 ]] || {
  usage >&2
  exit 1
}

USERNAME="$1"
TARGET="$2"
SOURCE_DB="${3:-$DEFAULT_SOURCE_DB}"

[[ -n "$USERNAME" ]] || fail "username must not be empty"

command -v psql >/dev/null 2>&1 || fail "required command 'psql' was not found"

valid_db_name() {
  [[ "$1" =~ ^[A-Za-z_][A-Za-z0-9_]{0,62}$ ]]
}

valid_db_name "$SOURCE_DB" \
  || fail "source database name '$SOURCE_DB' looks invalid"
if [[ "$TARGET" != "all" ]]; then
  valid_db_name "$TARGET" || fail "target database name '$TARGET' looks invalid"
fi

WORK_DIR="$(mktemp -d "${TMPDIR:-/tmp}/tellervo-add-user.XXXXXXXX")" \
  || fail "could not create a temporary working directory"
trap 'rm -rf -- "$WORK_DIR"' EXIT

# psql only expands :'variable' placeholders when reading a script (-f), not
# in a -c string, so the username is bound through a script file rather than
# being interpolated into SQL text by hand.
# Tellervo databases vary in schema history (some still lack constraints
# that later upgrade patches assume, e.g. no unique constraint on
# tblsecurityuser.username or tblsecurityusermembership(securityuserid,
# securitygroupid)), so this uses plain NOT EXISTS guards rather than
# ON CONFLICT, which requires a matching constraint to target.
#
# securityuserid is copied verbatim (see usage text above), so three
# outcomes are possible against a given target and are reported without
# needing PL/pgSQL: 'added', 'exists' (that securityuserid is already
# there), or 'username-conflict' (a *different* securityuserid already
# holds that username there). The source securityuserid is returned
# alongside the statement (\x1f-separated) purely for display.
FETCH_SQL="$WORK_DIR/fetch.sql"
cat > "$FETCH_SQL" <<'SQL'
SELECT securityuserid,
    'WITH ins AS (INSERT INTO tblsecurityuser (securityuserid, username, password, firstname, lastname, isactive) '
    || 'SELECT ' || quote_literal(securityuserid) || ', ' || quote_nullable(username) || ', '
    || quote_nullable(password) || ', ' || quote_nullable(firstname) || ', ' || quote_nullable(lastname) || ', '
    || quote_nullable(isactive)
    || ' WHERE NOT EXISTS (SELECT 1 FROM tblsecurityuser WHERE securityuserid = ' || quote_literal(securityuserid) || ')'
    || ' AND NOT EXISTS (SELECT 1 FROM tblsecurityuser WHERE username = ' || quote_literal(username) || ')'
    || ' RETURNING securityuserid) '
    || 'SELECT CASE WHEN EXISTS (SELECT 1 FROM ins) THEN ''added'' '
    || 'WHEN EXISTS (SELECT 1 FROM tblsecurityuser WHERE securityuserid = ' || quote_literal(securityuserid) || ') THEN ''exists'' '
    || 'ELSE ''username-conflict'' END;'
FROM tblsecurityuser
WHERE username = :'uname';
SQL

# One row per distinct security group name the user belongs to in the source
# database (DISTINCT ON collapses duplicate group names, since tblsecuritygroup
# has no unique constraint on name and some databases have more than one
# group row with the same name). Each generated statement is self-contained:
# it resolves the group by name and the user by their stable securityuserid
# (not username -- if the user step above reported 'username-conflict', no
# row exists under this securityuserid in the target, so target_user
# correctly resolves to nothing and no membership gets attached to the
# unrelated user squatting on that username) *in whichever database it is
# run against*, so it works unmodified against every target regardless of
# that target's own ID numbering, uses NOT EXISTS rather than ON CONFLICT
# for the same constraint-drift reason as above, and cleanly reports one of
# three outcomes without needing PL/pgSQL: 'added', 'exists' (already a
# member), or 'missing-group' (the target has no group with that name).
# Fields are separated with \x1f since group names may contain spaces.
FETCH_MEMBERSHIPS_SQL="$WORK_DIR/fetch_memberships.sql"
cat > "$FETCH_MEMBERSHIPS_SQL" <<'SQL'
SELECT DISTINCT ON (g.name) g.name,
    'WITH target_user AS (SELECT securityuserid FROM tblsecurityuser WHERE securityuserid = '
    || quote_literal(u.securityuserid) || '), '
    || 'target_group AS (SELECT securitygroupid FROM tblsecuritygroup WHERE name = '
    || quote_literal(g.name) || '), '
    || 'ins AS (INSERT INTO tblsecurityusermembership (securityuserid, securitygroupid) '
    || 'SELECT target_user.securityuserid, target_group.securitygroupid FROM target_user, target_group '
    || 'WHERE NOT EXISTS (SELECT 1 FROM tblsecurityusermembership existing '
    || 'WHERE existing.securityuserid = target_user.securityuserid '
    || 'AND existing.securitygroupid = target_group.securitygroupid) '
    || 'RETURNING securitygroupid) '
    || 'SELECT CASE WHEN NOT EXISTS (SELECT 1 FROM target_group) THEN ''missing-group'' '
    || 'WHEN EXISTS (SELECT 1 FROM ins) THEN ''added'' ELSE ''exists'' END;'
FROM tblsecurityusermembership m
JOIN tblsecuritygroup g ON g.securitygroupid = m.securitygroupid
JOIN tblsecurityuser u ON u.securityuserid = m.securityuserid
WHERE u.username = :'uname'
ORDER BY g.name;
SQL

echo "Looking up '$USERNAME' in '$SOURCE_DB'..."
FETCH_FILE="$WORK_DIR/fetch_result.txt"
psql --no-psqlrc --quiet --tuples-only --no-align --field-separator=$'\x1f' \
  --set=ON_ERROR_STOP=1 --dbname="$SOURCE_DB" -v uname="$USERNAME" \
  -f "$FETCH_SQL" > "$FETCH_FILE" \
  || fail "could not query '$SOURCE_DB' for user '$USERNAME'"
mapfile -t FETCH_ROWS < "$FETCH_FILE"

(( ${#FETCH_ROWS[@]} > 0 )) \
  || fail "no tblsecurityuser record for username '$USERNAME' was found in '$SOURCE_DB'"
(( ${#FETCH_ROWS[@]} == 1 )) \
  || fail "'$USERNAME' matches more than one tblsecurityuser record in '$SOURCE_DB'; resolve that first"

IFS=$'\x1f' read -r SOURCE_UUID INSERT_SQL <<< "${FETCH_ROWS[0]}"

MEMBERSHIPS_FILE="$WORK_DIR/memberships.txt"
psql --no-psqlrc --quiet --tuples-only --no-align --field-separator=$'\x1f' \
  --set=ON_ERROR_STOP=1 --dbname="$SOURCE_DB" -v uname="$USERNAME" \
  -f "$FETCH_MEMBERSHIPS_SQL" > "$MEMBERSHIPS_FILE" \
  || fail "could not query '$SOURCE_DB' for '$USERNAME' group memberships"
mapfile -t MEMBERSHIP_ROWS < "$MEMBERSHIPS_FILE"

if [[ "$TARGET" == "all" ]]; then
  echo "Discovering other tellervo databases on the server..."
  TARGETS_FILE="$WORK_DIR/targets.txt"
  # SOURCE_DB was already checked against valid_db_name, so it is safe to
  # interpolate directly here.
  psql --no-psqlrc --quiet --tuples-only --no-align \
    --set=ON_ERROR_STOP=1 --dbname=postgres -c \
    "SELECT datname FROM pg_database WHERE datname LIKE 'tellervo%' AND datname <> '$SOURCE_DB' ORDER BY datname;" \
    > "$TARGETS_FILE" \
    || fail "could not list databases from the server"
  mapfile -t TARGETS < "$TARGETS_FILE"
  (( ${#TARGETS[@]} > 0 )) || fail "no other tellervo databases were found"
else
  TARGETS=("$TARGET")
fi

echo
echo "User '$USERNAME' (securityuserid=$SOURCE_UUID) will be added to ${#TARGETS[@]} database(s):"
printf '  %s\n' "${TARGETS[@]}"
if (( ${#MEMBERSHIP_ROWS[@]} > 0 )); then
  echo "Security group memberships that will be synced:"
  for row in "${MEMBERSHIP_ROWS[@]}"; do
    echo "  ${row%%$'\x1f'*}"
  done
else
  echo "'$USERNAME' has no security group memberships in '$SOURCE_DB' to sync."
fi
echo
read -r -p "Continue? (y/N) " REPLY
[[ "$REPLY" =~ ^[Yy]$ ]] || fail "aborted"

added_count=0
skipped_count=0
conflict_count=0
failed_count=0
membership_added=0
membership_skipped=0
membership_missing=0
membership_failed=0

echo
for db in "${TARGETS[@]}"; do
  result="$(psql --no-psqlrc --quiet --tuples-only --no-align \
    --set=ON_ERROR_STOP=1 --dbname="$db" -c "$INSERT_SQL" 2>&1)"
  status=$?
  if (( status != 0 )); then
    echo "  $db: FAILED -- $result" >&2
    ((failed_count += 1))
    continue
  fi
  case "$result" in
    added)
      echo "  $db: added"
      ((added_count += 1))
      ;;
    exists)
      echo "  $db: already present"
      ((skipped_count += 1))
      ;;
    username-conflict)
      echo "  $db: CONFLICT -- a different user already has the username '$USERNAME' here, skipping (including group sync)" >&2
      ((conflict_count += 1))
      continue
      ;;
    *)
      echo "  $db: unexpected result '$result'" >&2
      ((failed_count += 1))
      continue
      ;;
  esac

  for row in "${MEMBERSHIP_ROWS[@]}"; do
    IFS=$'\x1f' read -r group_name group_sql <<< "$row"
    mresult="$(psql --no-psqlrc --quiet --tuples-only --no-align \
      --set=ON_ERROR_STOP=1 --dbname="$db" -c "$group_sql" 2>&1)"
    mstatus=$?
    if (( mstatus != 0 )); then
      echo "    group '$group_name': FAILED -- $mresult" >&2
      ((membership_failed += 1))
      continue
    fi
    case "$mresult" in
      added)
        echo "    group '$group_name': added"
        ((membership_added += 1))
        ;;
      exists)
        echo "    group '$group_name': already a member"
        ((membership_skipped += 1))
        ;;
      missing-group)
        echo "    group '$group_name': no group named '$group_name' in '$db', skipped" >&2
        ((membership_missing += 1))
        ;;
      *)
        echo "    group '$group_name': unexpected result '$mresult'" >&2
        ((membership_failed += 1))
        ;;
    esac
  done
done

echo
echo "Users   -- added: $added_count  already present: $skipped_count  username conflicts: $conflict_count  failed: $failed_count"
echo "Groups  -- added: $membership_added  already a member: $membership_skipped  missing in target: $membership_missing  failed: $membership_failed"

if (( failed_count > 0 || conflict_count > 0 || membership_failed > 0 )); then
  exit 1
fi
