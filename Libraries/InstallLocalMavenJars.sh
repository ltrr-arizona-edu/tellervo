#!/usr/bin/env bash

set -euo pipefail

cd "$(dirname "$0")"

PROJECT_ROOT="$(cd .. && pwd)"
TARGET_REPO="${PROJECT_ROOT}/.mvn-repo"

# Install nonstandard jars that Tellervo still depends on and that are shipped with the repo.
# This is intended as a local fallback when external repositories are unavailable or undesirable.

mkdir -p "${TARGET_REPO}"

install_from_m2() {
  local group_path="$1"
  local artifact="$2"
  local version="$3"
  local src_dir="${HOME}/.m2/repository/${group_path}/${artifact}/${version}"
  local dest_dir="${TARGET_REPO}/${group_path}/${artifact}/${version}"

  if [[ ! -f "${src_dir}/${artifact}-${version}.jar" || ! -f "${src_dir}/${artifact}-${version}.pom" ]]; then
    echo "Skipping ${group_path}:${artifact}:${version}; source artifact not found in ~/.m2" >&2
    return
  fi

  mkdir -p "${dest_dir}"
  cp "${src_dir}/${artifact}-${version}.jar" "${dest_dir}/"
  cp "${src_dir}/${artifact}-${version}.pom" "${dest_dir}/"
}

install_with_generated_pom() {
  local group_path="$1"
  local group_id="$2"
  local artifact="$3"
  local version="$4"
  local jar_file="$5"
  local dest_dir="${TARGET_REPO}/${group_path}/${artifact}/${version}"

  mkdir -p "${dest_dir}"
  cp "${jar_file}" "${dest_dir}/${artifact}-${version}.jar"
  cat > "${dest_dir}/${artifact}-${version}.pom" <<EOF
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>${group_id}</groupId>
  <artifactId>${artifact}</artifactId>
  <version>${version}</version>
  <packaging>jar</packaging>
</project>
EOF
}

install_with_pom_file() {
  local group_path="$1"
  local artifact="$2"
  local version="$3"
  local jar_file="$4"
  local pom_file="$5"
  local dest_dir="${TARGET_REPO}/${group_path}/${artifact}/${version}"

  mkdir -p "${dest_dir}"
  cp "${jar_file}" "${dest_dir}/${artifact}-${version}.jar"
  cp "${pom_file}" "${dest_dir}/${artifact}-${version}.pom"
}

install_with_generated_pom "com/l2prod/common" "com.l2prod.common" "l2fprod-common-sheet" "6.9.1" "l2fprod-common-sheet.jar"
install_with_generated_pom "com/google/code" "com.google.code" "jsyntaxpane" "0.9.5" "jsyntaxpane-0.9.5-b17.jar"
install_with_generated_pom "org/netbeans/api" "org.netbeans.api" "org-netbeans-swing-outline" "1.0" "org-netbeans-swing-outline.jar"
install_with_generated_pom "postgresql" "postgresql" "pljava-public" "1.4.2" "pljava.jar"
install_with_generated_pom "org/jvnet/jaxb2_commons" "org.jvnet.jaxb2_commons" "xjc-if-ins" "0.5.2" "xjc-if-ins-0.5.2.jar"
install_with_generated_pom "org/tridas/schema" "org.tridas.schema" "tridasaandi" "1.0" "tridasaandi-1.0.jar"
install_with_generated_pom "org/tridas/schema" "org.tridas.schema" "tridas-annotations" "1.0" "tridas-annotations-1.0.jar"
install_with_generated_pom "com/sun/tools/xjc" "com.sun.tools.xjc" "collection-setter-injector" "0.1" "collection-setter-injector-0.1.jar"
install_with_generated_pom "jpedal" "jpedal" "jpedal" "4.45-b-105" "jpedal-4.45-b-105.jar"
install_with_generated_pom "org/osgeo" "org.osgeo" "gdal" "0.2" "gdal.jar"
install_with_generated_pom "org/rxtx" "org.rxtx" "rxtx" "2.2-20081207" "RXTXcomm.jar"
install_with_pom_file "gov/nasa/worldwind" "worldwindjava-tellervo" "2.0.0" "worldwindjava-tellervo-2.0.0.jar" "worldwindjava-pom.xml"

install_from_m2 "com/dmurph/mvc" "java-simple-mvc" "1.4.4"
install_from_m2 "com/dmurph" "JGoogleAnalyticsTracker" "1.2.0"
install_from_m2 "org/tridas" "tridasjlib" "2.0.0"
install_from_m2 "org/tridas" "dendrofileio" "2.0.0"
install_from_m2 "org/tridas" "tricycle" "2.0.0"
