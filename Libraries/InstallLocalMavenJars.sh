#!/usr/bin/env bash

set -euo pipefail

cd "$(dirname "$0")"

PROJECT_ROOT="$(cd .. && pwd)"
TARGET_REPO="${PROJECT_ROOT}/.mvn-repo"

# Install nonstandard jars that Tellervo still depends on and that are shipped with the repo.
# This is intended as a local fallback when external repositories are unavailable or undesirable.

mkdir -p "${TARGET_REPO}"

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
install_with_pom_file "com/dmurph/mvc" "java-simple-mvc" "1.4.2" "java-simple-mvc-1.4.2.jar" "java-simple-mvc-1.4.2.pom"
install_with_pom_file "com/dmurph/mvc" "java-simple-mvc" "1.4.4" "java-simple-mvc-1.4.4.jar" "java-simple-mvc-1.4.4.pom"
install_with_pom_file "com/dmurph" "JGoogleAnalyticsTracker" "1.2.0" "JGoogleAnalyticsTracker-1.2.0.jar" "JGoogleAnalyticsTracker-1.2.0.pom"
install_with_pom_file "org/tridas" "tridasjlib" "2.0.0" "tridasjlib-2.0.0.jar" "tridasjlib-2.0.0.pom"
install_with_pom_file "org/tridas" "dendrofileio" "2.0.0" "dendrofileio-2.0.0.jar" "dendrofileio-2.0.0.pom"
install_with_pom_file "org/tridas" "tricycle" "2.0.0" "tricycle-2.0.0.jar" "tricycle-2.0.0.pom"
