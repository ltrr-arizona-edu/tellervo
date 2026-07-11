#!/usr/bin/env bash
set -euo pipefail

SERVER_VERSION=""
PACKAGE_DIR=""
REPO_DIR=""
GPG_KEY=""
SUITES=("trixie" "resolute")
COMPONENT="main"
ARCHITECTURE="amd64"
RELEASE_ARCHITECTURES="amd64 all"
ORIGIN="Tellervo"
LABEL="Tellervo"

usage() {
  cat <<EOF
Usage: $0 --repo-dir DIR [options]

Publishes Tellervo server .deb files into a simple static APT repository.

Options:
  --repo-dir DIR          Repository root to update, for example /srv/apt/tellervo
  --server-version VER    Server version. Defaults to pom.xml <serverversion>
  --package-dir DIR       Directory containing .deb files. Defaults to
                          target/binaries/server/VERSION/Linux
  --suite NAME            Suite/codename to publish. May be repeated.
                          Defaults: trixie resolute
  --component NAME        Repository component. Default: main
  --architecture ARCH     Package index directory architecture. Default: amd64
  --gpg-key KEY           GPG key id/email/name used to sign Release metadata
  --origin NAME           Release Origin field. Default: Tellervo
  --label NAME            Release Label field. Default: Tellervo
  --help                  Show this help

Required tools:
  dpkg-scanpackages from dpkg-dev
  apt-ftparchive from apt-utils
  gpg when --gpg-key is provided
EOF
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --repo-dir)
      REPO_DIR="$2"
      shift 2
      ;;
    --server-version|--version)
      SERVER_VERSION="$2"
      shift 2
      ;;
    --package-dir)
      PACKAGE_DIR="$2"
      shift 2
      ;;
    --suite)
      if [[ "${SUITES[*]}" == "trixie resolute" ]]; then
        SUITES=()
      fi
      SUITES+=("$2")
      shift 2
      ;;
    --component)
      COMPONENT="$2"
      shift 2
      ;;
    --architecture)
      ARCHITECTURE="$2"
      shift 2
      ;;
    --gpg-key)
      GPG_KEY="$2"
      shift 2
      ;;
    --origin)
      ORIGIN="$2"
      shift 2
      ;;
    --label)
      LABEL="$2"
      shift 2
      ;;
    --help|-h)
      usage
      exit 0
      ;;
    *)
      echo "Unknown argument: $1" >&2
      usage >&2
      exit 1
      ;;
  esac
done

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$REPO_ROOT"

if [[ -z "$REPO_DIR" ]]; then
  echo "--repo-dir is required." >&2
  usage >&2
  exit 1
fi

if [[ -z "$SERVER_VERSION" ]]; then
  SERVER_VERSION="$(sed -n 's:.*<serverversion>\(.*\)</serverversion>.*:\1:p' pom.xml | head -n 1)"
fi

if [[ -z "$SERVER_VERSION" ]]; then
  echo "Unable to determine server version." >&2
  exit 1
fi

if [[ -z "$PACKAGE_DIR" ]]; then
  PACKAGE_DIR="$REPO_ROOT/target/binaries/server/$SERVER_VERSION/Linux"
fi

if [[ ! -d "$PACKAGE_DIR" ]]; then
  echo "Package directory not found: $PACKAGE_DIR" >&2
  exit 1
fi

if [[ ${#SUITES[@]} -eq 0 ]]; then
  echo "At least one --suite is required." >&2
  exit 1
fi

for tool in dpkg-scanpackages apt-ftparchive; do
  if ! command -v "$tool" >/dev/null 2>&1; then
    echo "Unable to locate $tool. Install dpkg-dev and apt-utils." >&2
    exit 1
  fi
done

if [[ -n "$GPG_KEY" ]] && ! command -v gpg >/dev/null 2>&1; then
  echo "Unable to locate gpg, required when --gpg-key is used." >&2
  exit 1
fi

REPO_DIR="$(mkdir -p "$REPO_DIR" && cd "$REPO_DIR" && pwd)"
POOL_DIR="$REPO_DIR/pool/$COMPONENT/t/tellervo"

mkdir -p "$POOL_DIR"
find "$PACKAGE_DIR" -maxdepth 1 -type f -name 'tellervo-server*.deb' -exec cp -f {} "$POOL_DIR"/ \;

package_count="$(find "$POOL_DIR" -maxdepth 1 -type f -name 'tellervo-server*.deb' | wc -l)"
if [[ "$package_count" -eq 0 ]]; then
  echo "No tellervo-server .deb files found in $PACKAGE_DIR" >&2
  exit 1
fi

for suite in "${SUITES[@]}"; do
  binary_dir="$REPO_DIR/dists/$suite/$COMPONENT/binary-$ARCHITECTURE"
  mkdir -p "$binary_dir"

  (
    cd "$REPO_DIR"
    dpkg-scanpackages "pool/$COMPONENT" /dev/null > "$binary_dir/Packages"
    gzip -9c "$binary_dir/Packages" > "$binary_dir/Packages.gz"
  )

  release_file="$REPO_DIR/dists/$suite/Release"
  {
    echo "Origin: $ORIGIN"
    echo "Label: $LABEL"
    echo "Suite: $suite"
    echo "Codename: $suite"
    echo "Architectures: $RELEASE_ARCHITECTURES"
    echo "Components: $COMPONENT"
    echo "Description: Tellervo server packages"
    echo "Date: $(date -Ru)"
    apt-ftparchive release "$REPO_DIR/dists/$suite"
  } > "$release_file"

  if [[ -n "$GPG_KEY" ]]; then
    gpg --batch --yes --default-key "$GPG_KEY" \
      --clearsign --output "$REPO_DIR/dists/$suite/InRelease" "$release_file"
    gpg --batch --yes --default-key "$GPG_KEY" \
      --detach-sign --armor --output "$REPO_DIR/dists/$suite/Release.gpg" "$release_file"
  fi

  echo "Published $suite metadata in $REPO_DIR/dists/$suite"
done

cat <<EOF

Published $package_count package(s) to:
  $REPO_DIR

Client source example:
  deb [signed-by=/usr/share/keyrings/tellervo-archive-keyring.asc] https://your-server.example/apt/tellervo trixie $COMPONENT

EOF
