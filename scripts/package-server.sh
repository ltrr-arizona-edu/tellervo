#!/usr/bin/env bash
set -euo pipefail

SERVER_VERSION=""
PLJAVA_JAR=""
DEST_DIR=""
STAGING_DIR=""
VERBOSE="false"

while [[ $# -gt 0 ]]; do
  case "$1" in
    --server-version|--version)
      SERVER_VERSION="$2"
      shift 2
      ;;
    --pljava-jar)
      PLJAVA_JAR="$2"
      shift 2
      ;;
    --dest-dir)
      DEST_DIR="$2"
      shift 2
      ;;
    --staging-dir)
      STAGING_DIR="$2"
      shift 2
      ;;
    --verbose)
      VERBOSE="true"
      shift
      ;;
    *)
      echo "Unknown argument: $1" >&2
      exit 1
      ;;
  esac
done

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$REPO_ROOT"

if [[ "$(uname -s)" != "Linux" ]]; then
  echo "Server Debian packaging is only supported on Linux." >&2
  exit 1
fi

if ! command -v dpkg-deb >/dev/null 2>&1; then
  echo "Unable to locate dpkg-deb. Install dpkg tooling or run the packaging flow on Debian/Ubuntu." >&2
  exit 1
fi

if [[ -z "$SERVER_VERSION" ]]; then
  SERVER_VERSION="$(sed -n 's:.*<serverversion>\(.*\)</serverversion>.*:\1:p' pom.xml | head -n 1)"
fi

if [[ -z "$SERVER_VERSION" ]]; then
  echo "Unable to determine server version." >&2
  exit 1
fi

if [[ -z "$PLJAVA_JAR" ]]; then
  PLJAVA_JAR="$REPO_ROOT/target/tellervo-pljava.jar"
fi

if [[ ! -f "$PLJAVA_JAR" ]]; then
  echo "PL/Java jar not found at $PLJAVA_JAR. Run 'mvn -DskipTests package' first." >&2
  exit 1
fi

if [[ -z "$DEST_DIR" ]]; then
  DEST_DIR="$REPO_ROOT/target/server-packaging/dist"
fi

if [[ -z "$STAGING_DIR" ]]; then
  STAGING_DIR="$REPO_ROOT/target/server-packaging/staging"
fi

PACKAGE_ROOT="$STAGING_DIR/tellervo-server"
DEBIAN_DIR="$PACKAGE_ROOT/DEBIAN"
SHARE_DIR="$PACKAGE_ROOT/usr/share/tellervo-server"
WEB_DIR="$PACKAGE_ROOT/var/www/tellervo"

rm -rf "$PACKAGE_ROOT"
mkdir -p "$DEBIAN_DIR" "$DEST_DIR"

render_template() {
  local src="$1"
  local dest="$2"
  sed "s/{{SERVER_VERSION}}/$SERVER_VERSION/g" "$src" > "$dest"
}

install_file() {
  local src="$1"
  local dest="$2"
  local mode="$3"
  install -D -m "$mode" "$src" "$dest"
}

install_dir_copy() {
  local src="$1"
  local dest="$2"
  mkdir -p "$dest"
  cp -a "$src"/. "$dest"/
}

render_template "$REPO_ROOT/packaging/server/linux/control" "$DEBIAN_DIR/control"
for maintainer_script in preinst postinst prerm postrm; do
  render_template "$REPO_ROOT/packaging/server/linux/$maintainer_script" "$DEBIAN_DIR/$maintainer_script"
  chmod 0755 "$DEBIAN_DIR/$maintainer_script"
done

install_file "$REPO_ROOT/Native/BuildResources/LinBuild/tellervo-server" \
  "$PACKAGE_ROOT/usr/bin/tellervo-server" 0755
install_file "$REPO_ROOT/Native/BuildResources/LinBuild/create-tellervo-instance" \
  "$PACKAGE_ROOT/usr/bin/create-tellervo-instance" 0755
install_file "$PLJAVA_JAR" \
  "$SHARE_DIR/tellervo-pljava.jar" 0755
install_file "$REPO_ROOT/Native/BuildResources/LinBuild/tellervo-apache.conf" \
  "$PACKAGE_ROOT/etc/apache2/sites-available/tellervo-apache.conf" 0755
install_file "$REPO_ROOT/Native/BuildResources/LinBuild/pljava-pg14-amd64-Linux-gpp.jar" \
  "$SHARE_DIR/pljava-pg14-amd64-Linux-gpp.jar" 0755
install_file "$REPO_ROOT/Native/BuildResources/LinBuild/pljava-pg9.5-i386-Linux-gpp.jar" \
  "$SHARE_DIR/pljava-pg9.5-i386-Linux-gpp.jar" 0755
install_file "$REPO_ROOT/Native/BuildResources/LinBuild/pljava-pg9.5-amd64-Linux-gpp.jar" \
  "$SHARE_DIR/pljava-pg9.5-amd64-Linux-gpp.jar" 0755
install_file "$REPO_ROOT/Native/BuildResources/LinBuild/tui.php" \
  "$SHARE_DIR/tui.php" 0777
install_file "$REPO_ROOT/Native/BuildResources/LinBuild/firstrun.template" \
  "$SHARE_DIR/firstrun.template" 0777
install_file "$REPO_ROOT/src/main/php/config.php.template" \
  "$SHARE_DIR/config.php.template" 0644

install_dir_copy "$REPO_ROOT/Databases/db-upgrade-patches" "$SHARE_DIR/db-upgrade-patches"
install_dir_copy "$REPO_ROOT/Databases/db-templates" "$SHARE_DIR/db-templates"
install_dir_copy "$REPO_ROOT/Databases/db-options" "$SHARE_DIR/db-options"
install_dir_copy "$REPO_ROOT/src/main/php" "$WEB_DIR"
find "$WEB_DIR" -type d -exec chmod 0755 {} +
find "$WEB_DIR" -type f -exec chmod 0644 {} +

mkdir -p "$SHARE_DIR/mediastore"

OUTPUT_DEB="$DEST_DIR/tellervo-server-$SERVER_VERSION.deb"
rm -f "$OUTPUT_DEB"

DPKG_ARGS=(--build --root-owner-group "$PACKAGE_ROOT" "$OUTPUT_DEB")
if [[ "$VERBOSE" == "true" ]]; then
  echo "Building Tellervo server Debian package"
  echo "dpkg-deb ${DPKG_ARGS[*]}"
fi

dpkg-deb "${DPKG_ARGS[@]}"

echo "Created $OUTPUT_DEB"
