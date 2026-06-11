#!/usr/bin/env bash
set -euo pipefail

VERSION=""
APP_VERSION=""
TYPE=""
JAVA_HOME_ARG="${JAVA_HOME:-}"
MAIN_JAR=""
MAIN_CLASS="org.tellervo.desktop.gui.Startup"
INPUT_DIR=""
DEST_DIR=""
VERBOSE="false"

while [[ $# -gt 0 ]]; do
  case "$1" in
    --version)
      VERSION="$2"
      shift 2
      ;;
    --app-version)
      APP_VERSION="$2"
      shift 2
      ;;
    --type)
      TYPE="$2"
      shift 2
      ;;
    --java-home)
      JAVA_HOME_ARG="$2"
      shift 2
      ;;
    --main-jar)
      MAIN_JAR="$2"
      shift 2
      ;;
    --main-class)
      MAIN_CLASS="$2"
      shift 2
      ;;
    --input-dir)
      INPUT_DIR="$2"
      shift 2
      ;;
    --dest-dir)
      DEST_DIR="$2"
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

if [[ -z "$VERSION" ]]; then
  VERSION="$(sed -n 's:.*<version>\(.*\)</version>.*:\1:p' pom.xml | tail -n 1)"
fi

if [[ -z "$APP_VERSION" ]]; then
  APP_VERSION="$VERSION"
fi

if [[ -z "$INPUT_DIR" ]]; then
  INPUT_DIR="$REPO_ROOT/target/jpackage/input"
fi

if [[ -z "$DEST_DIR" ]]; then
  DEST_DIR="$REPO_ROOT/target/jpackage/dist"
fi

if [[ -n "$JAVA_HOME_ARG" && -x "$JAVA_HOME_ARG/bin/jpackage" ]]; then
  JPACKAGE_BIN="$JAVA_HOME_ARG/bin/jpackage"
elif command -v jpackage >/dev/null 2>&1; then
  JPACKAGE_BIN="$(command -v jpackage)"
else
  echo "Unable to locate jpackage. Set --java-home or ensure jpackage is on PATH." >&2
  exit 1
fi

PLATFORM="$(uname -s)"
ICON=""
INSTALL_DIR=""
RESOURCE_DIR=""
NATIVE_LIB_DIR=""
EXTRA_ARGS=()

case "$PLATFORM" in
  Linux*)
    DEFAULT_TYPE="deb"
    ICON="$REPO_ROOT/src/main/resources/Icons/256x256/tellervo-application.png"
    INSTALL_DIR="/opt/tellervo"
    RESOURCE_DIR="$REPO_ROOT/packaging/jpackage/linux"
    EXTRA_ARGS+=(
      --linux-shortcut
      --linux-menu-group Science
      --linux-app-category Science
      --linux-package-name tellervo
      --linux-deb-maintainer p.brewer@ltrr.arizona.edu
      --linux-package-deps "librxtx-java, libjogl2-java"
    )
    ;;
  Darwin*)
    DEFAULT_TYPE="dmg"
    ICON="$REPO_ROOT/src/main/resources/Icons/tellervo-application.icns"
    INSTALL_DIR="/Applications/Tellervo"
    ;;
  MINGW*|MSYS*|CYGWIN*)
    DEFAULT_TYPE="exe"
    ICON="$REPO_ROOT/src/main/resources/Icons/tellervo-application.ico"
    INSTALL_DIR="Tellervo"
    NATIVE_LIB_DIR="$REPO_ROOT/Native/Libraries/windows-amd64"
    EXTRA_ARGS+=(--win-shortcut --win-menu --win-dir-chooser)
    ;;
  *)
    echo "Unsupported platform for jpackage script: $PLATFORM" >&2
    exit 1
    ;;
esac

if [[ -z "$TYPE" ]]; then
  TYPE="$DEFAULT_TYPE"
fi

if [[ -z "$MAIN_JAR" ]]; then
  MAIN_JAR="tellervo-${VERSION}.jar"
fi

if [[ ! -f "$REPO_ROOT/target/$MAIN_JAR" ]]; then
  echo "Main application jar not found at target/$MAIN_JAR. Run 'mvn -DskipTests package' first." >&2
  exit 1
fi

rm -rf "$INPUT_DIR" "$DEST_DIR"
mkdir -p "$INPUT_DIR" "$DEST_DIR"

cp "$REPO_ROOT/target/$MAIN_JAR" "$INPUT_DIR/"
if [[ -d "$REPO_ROOT/target/dependency" ]]; then
  cp "$REPO_ROOT"/target/dependency/* "$INPUT_DIR/" || true
fi

if [[ -n "$NATIVE_LIB_DIR" ]]; then
  if [[ ! -d "$NATIVE_LIB_DIR" ]]; then
    echo "Native library directory not found: $NATIVE_LIB_DIR" >&2
    exit 1
  fi
  if [[ ! -f "$NATIVE_LIB_DIR/rxtxSerial.dll" ]]; then
    echo "Required Windows serial library not found: $NATIVE_LIB_DIR/rxtxSerial.dll" >&2
    exit 1
  fi
  cp "$NATIVE_LIB_DIR"/* "$INPUT_DIR/"
fi

ARGS=(
  --type "$TYPE"
  --dest "$DEST_DIR"
  --input "$INPUT_DIR"
  --name Tellervo
  --app-version "$APP_VERSION"
  --vendor "Laboratory of Tree-Ring Research, University of Arizona"
  --description "Tellervo dendrochronology desktop application"
  --copyright "Copyright (C) Tellervo contributors"
  --main-jar "$MAIN_JAR"
  --main-class "$MAIN_CLASS"
  --java-options "-Dfile.encoding=UTF-8"
  --java-options "-Djava.awt.headless=false"
  --java-options "-Dcom.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize=true"
)

if [[ -n "$NATIVE_LIB_DIR" ]]; then
  ARGS+=(--java-options '-Djava.library.path=$APPDIR')
fi

if [[ -n "$ICON" && -f "$ICON" ]]; then
  ARGS+=(--icon "$ICON")
fi

if [[ -n "$RESOURCE_DIR" && -d "$RESOURCE_DIR" ]]; then
  ARGS+=(--resource-dir "$RESOURCE_DIR")
fi

if [[ "$TYPE" != "app-image" ]]; then
  ARGS+=(--install-dir "$INSTALL_DIR")
  ARGS+=("${EXTRA_ARGS[@]}")
fi

if [[ "$VERBOSE" == "true" ]]; then
  ARGS+=(--verbose)
fi

echo "Packaging Tellervo with jpackage on $PLATFORM"
"$JPACKAGE_BIN" "${ARGS[@]}"
