#!/usr/bin/env bash
set -euo pipefail

SERVER_VERSION=""
POSTGRES_VERSION=""
POSTGRES_VERSIONS=()
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
    --postgres-version)
      POSTGRES_VERSIONS+=("$2")
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
  DEST_DIR="$REPO_ROOT/target/server/$SERVER_VERSION/Linux"
fi

if [[ -z "$STAGING_DIR" ]]; then
  STAGING_DIR="$REPO_ROOT/target/server-packaging/staging"
fi

if [[ ${#POSTGRES_VERSIONS[@]} -eq 0 ]]; then
  POSTGRES_VERSIONS=("18" "17")
fi

META_ROOT="$STAGING_DIR/tellervo-server"
COMMON_ROOT="$STAGING_DIR/tellervo-server-common"
WEBSERVICE_ROOT="$STAGING_DIR/tellervo-server-webservice"
DB_ROOT="$STAGING_DIR/tellervo-server-db"

rm -rf "$META_ROOT" "$COMMON_ROOT" "$WEBSERVICE_ROOT" "$DB_ROOT" "$STAGING_DIR"/tellervo-server-db-pg*
mkdir -p "$DEST_DIR"

render_template() {
  local src="$1"
  local dest="$2"
  sed \
    -e "s/{{SERVER_VERSION}}/$SERVER_VERSION/g" \
    -e "s/{{POSTGRES_VERSION}}/$POSTGRES_VERSION/g" \
    "$src" > "$dest"
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

install_common_payload() {
  local package_root="$1"
  local share_dir="$package_root/usr/share/tellervo-server"

  install_file "$REPO_ROOT/Native/BuildResources/LinBuild/tellervo-server" \
    "$package_root/usr/bin/tellervo-server" 0755
  install_file "$REPO_ROOT/Native/BuildResources/LinBuild/create-tellervo-instance" \
    "$package_root/usr/bin/create-tellervo-instance" 0755
  install_file "$PLJAVA_JAR" \
    "$share_dir/tellervo-pljava.jar" 0755
  install_file "$REPO_ROOT/Native/BuildResources/LinBuild/pljava-pg14-amd64-Linux-gpp.jar" \
    "$share_dir/pljava-pg14-amd64-Linux-gpp.jar" 0755
  install_file "$REPO_ROOT/Native/BuildResources/LinBuild/pljava-pg9.5-i386-Linux-gpp.jar" \
    "$share_dir/pljava-pg9.5-i386-Linux-gpp.jar" 0755
  install_file "$REPO_ROOT/Native/BuildResources/LinBuild/pljava-pg9.5-amd64-Linux-gpp.jar" \
    "$share_dir/pljava-pg9.5-amd64-Linux-gpp.jar" 0755
  install_file "$REPO_ROOT/Native/BuildResources/LinBuild/tui.php" \
    "$share_dir/tui.php" 0777
  install_file "$REPO_ROOT/Native/BuildResources/LinBuild/firstrun.template" \
    "$share_dir/firstrun.template" 0777
  install_file "$REPO_ROOT/src/main/php/config.php.template" \
    "$share_dir/config.php.template" 0644

  install_dir_copy "$REPO_ROOT/Databases/db-upgrade-patches" "$share_dir/db-upgrade-patches"
  install_dir_copy "$REPO_ROOT/Databases/db-templates" "$share_dir/db-templates"
  install_dir_copy "$REPO_ROOT/Databases/db-options" "$share_dir/db-options"
  mkdir -p "$share_dir/mediastore"
}

install_webservice_payload() {
  local package_root="$1"
  local web_dir="$package_root/var/www/tellervo"

  install_file "$REPO_ROOT/Native/BuildResources/LinBuild/tellervo-apache.conf" \
    "$package_root/etc/apache2/sites-available/tellervo-apache.conf" 0755
  install_dir_copy "$REPO_ROOT/src/main/php" "$web_dir"
  find "$web_dir" -type d -exec chmod 0755 {} +
  find "$web_dir" -type f -exec chmod 0644 {} +
}

install_db_payload() {
  local package_root="$1"
  local package_name="$2"
  local postgres_version="${3:-}"

  mkdir -p "$package_root/usr/share/doc/$package_name"
  if [[ -n "$postgres_version" ]]; then
    printf '%s\n' \
      "This package installs PostgreSQL $postgres_version-side dependencies for a Tellervo database host." \
      'Install tellervo-server-webservice on the PHP/Apache host and configure it to use this database host.' \
      > "$package_root/usr/share/doc/$package_name/README.Debian"
  else
    printf '%s\n' \
      'This package selects an installable PostgreSQL dependency bundle for a Tellervo database host.' \
      'Install tellervo-server-webservice on the PHP/Apache host and configure it to use this database host.' \
      > "$package_root/usr/share/doc/$package_name/README.Debian"
  fi
  chmod 0644 "$package_root/usr/share/doc/$package_name/README.Debian"
}

install_control() {
  local package_root="$1"
  local control_template="$2"
  shift 2

  local debian_dir="$package_root/DEBIAN"
  mkdir -p "$debian_dir"
  render_template "$REPO_ROOT/packaging/server/linux/$control_template" "$debian_dir/control"

  local maintainer_script
  local source_script
  local dest_script
  for maintainer_script in "$@"; do
    source_script="${maintainer_script%%:*}"
    dest_script="${maintainer_script##*:}"
    render_template "$REPO_ROOT/packaging/server/linux/$source_script" "$debian_dir/$dest_script"
    chmod 0755 "$debian_dir/$dest_script"
  done
}

build_deb() {
  local package_root="$1"
  local package_name="$2"
  local output_deb="$DEST_DIR/$package_name-$SERVER_VERSION.deb"

  rm -f "$output_deb"
  find "$package_root" -type d -exec chmod 0755 {} +

  local dpkg_args=(--build --root-owner-group "$package_root" "$output_deb")
  if [[ "$VERBOSE" == "true" ]]; then
    echo "Building $package_name Debian package"
    echo "dpkg-deb ${dpkg_args[*]}"
  fi

  dpkg-deb "${dpkg_args[@]}"
  echo "Created $output_deb"
}

install_control "$META_ROOT" control

install_common_payload "$COMMON_ROOT"
install_control "$COMMON_ROOT" control-common \
  postinst-common:postinst postrm-common:postrm

install_webservice_payload "$WEBSERVICE_ROOT"
install_control "$WEBSERVICE_ROOT" control-webservice \
  preinst-webservice:preinst postinst-webservice:postinst prerm:prerm postrm-webservice:postrm

install_db_payload "$DB_ROOT" tellervo-server-db
install_control "$DB_ROOT" control-db

DB_PROVIDER_PACKAGES=()
for postgres_version in "${POSTGRES_VERSIONS[@]}"; do
  POSTGRES_VERSION="$postgres_version"
  db_provider_package="tellervo-server-db-pg$postgres_version"
  db_provider_root="$STAGING_DIR/$db_provider_package"
  DB_PROVIDER_PACKAGES+=("$db_provider_package")
  install_db_payload "$db_provider_root" "$db_provider_package" "$postgres_version"
  install_control "$db_provider_root" control-db-provider \
    postinst-db:postinst
done

build_deb "$META_ROOT" tellervo-server
build_deb "$COMMON_ROOT" tellervo-server-common
build_deb "$WEBSERVICE_ROOT" tellervo-server-webservice
build_deb "$DB_ROOT" tellervo-server-db
for db_provider_package in "${DB_PROVIDER_PACKAGES[@]}"; do
  build_deb "$STAGING_DIR/$db_provider_package" "$db_provider_package"
done

cat <<EOF

PostgreSQL package majors: ${POSTGRES_VERSIONS[*]}

Local install command for a single-host server:
  sudo apt install \\
    $DEST_DIR/tellervo-server-common-$SERVER_VERSION.deb \\
    $DEST_DIR/tellervo-server-webservice-$SERVER_VERSION.deb \\
    $DEST_DIR/tellervo-server-db-pg17-$SERVER_VERSION.deb \\
    $DEST_DIR/tellervo-server-db-$SERVER_VERSION.deb \\
    $DEST_DIR/tellervo-server-$SERVER_VERSION.deb

Use apt, not dpkg --install, so Debian can resolve PostgreSQL, PostGIS, PL/Java
and PHP dependencies from the configured repositories.
If these packages are published in an apt repository, apt can choose
tellervo-server-db-pg18 when PostgreSQL 18 PL/Java is available, otherwise
fall back to tellervo-server-db-pg17.
EOF
