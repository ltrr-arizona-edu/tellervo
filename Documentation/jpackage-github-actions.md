# Desktop Packaging with `jpackage`

Tellervo now has a GitHub Actions based desktop packaging path that builds a
standard Maven package first and then wraps it with `jpackage`.

## What it does

- regenerates JAXB sources with `mvn generate-sources`
- builds the desktop jar with `mvn -DskipTests package`
- collects the main jar plus runtime dependencies from `target/dependency`
- includes the 64-bit native DLLs required by the Windows launcher
- runs `jpackage` to create a desktop application image
- uploads the resulting artifacts from `target/jpackage/dist`
- publishes version-tag builds as public GitHub Release assets

## Local usage

From the repository root:

```bash
mvn -q -DskipTests package
bash scripts/package-desktop.sh
```

Or through Maven:

```bash
mvn -q -Pdesktop-jpackage -DskipTests package
```

The packaged output is written to:

```text
target/jpackage/dist
```

## GitHub Actions workflow

The workflow is defined in:

```text
.github/workflows/desktop-packaging.yml
```

Pushing a tag beginning with `v`, such as `v2.0.1`, builds all three desktop
packages and publishes them on the matching GitHub Release. Release assets are
publicly downloadable when the repository is public. Manually dispatched
workflow runs create Actions artifacts only and do not publish a release.

It currently produces native artifacts for:

- macOS: `dmg`
- Windows: `msi`
- Linux: `deb`

Linux `deb` packages also declare runtime package dependencies on:

- `librxtx-java`
- `libjogl2-java`

Linux `deb` packages also install a `/usr/bin/tellervo` launcher symlink so the
application can be started directly from a shell.

The Windows package includes `Native/Libraries/windows-amd64/rxtxSerial.dll`
in the application directory and adds that directory to `java.library.path`.
JOGL and GlueGen load their version-matched native libraries from their Maven
dependency jars; the older loose graphics DLLs are deliberately not packaged.
The MSI installs per-user under `%LOCALAPPDATA%\Tellervo`, so administrator
privileges are not required.

GitHub Actions also silently installs the generated Windows MSI package and
checks that the launcher and serial DLL were installed before uploading it.
The Windows Installer log from this smoke test is uploaded as a separate
artifact for troubleshooting.

## Notes

- The legacy desktop Install4J flow has been retired in favor of the new
  `jpackage` path.
- Server-specific Linux packaging remains separate in the Maven
  `server-binaries` profile.
