# Tellervo Server Packaging

The Debian packaging flow builds one top-level meta package and split-role
packages:

- `tellervo-server`: meta package for a single host running Apache, PHP,
  PostgreSQL, PostGIS and PL/Java. It depends on the three split-role packages
  and owns no application files itself.
- `tellervo-server-common`: shared administration commands, database templates,
  upgrade scripts and configuration templates.
- `tellervo-server-web`: Apache/PHP webservice files. Install this on the
  application server.
- `tellervo-server-db`: database dependency selector package. It prefers the
  PostgreSQL 18 dependency bundle when it is installable, otherwise apt can fall
  back to the PostgreSQL 17 bundle.
- `tellervo-server-db-pg18`: PostgreSQL 18, PostGIS and PL/Java dependency
  bundle.
- `tellervo-server-db-pg17`: PostgreSQL 17, PostGIS and PL/Java dependency
  bundle.

The split packages are intended for installations where the PHP/Apache webservice
and PostgreSQL run on separate hosts. The `tellervo-server` meta package keeps
the existing single-server install name without maintaining a second parallel
payload.

## Build

Build the server package in the usual way, or run the package script directly
after `target/tellervo-pljava.jar` exists:

```bash
scripts/package-server.sh
```

The generated packages are written to `target/server/VERSION/Linux/`, for
example `target/server/2.0/Linux/`:

```text
tellervo-server-2.0.deb
tellervo-server-common-2.0.deb
tellervo-server-web-2.0.deb
tellervo-server-db-2.0.deb
tellervo-server-db-pg18-2.0.deb
tellervo-server-db-pg17-2.0.deb
```

By default the build emits PostgreSQL 18 and PostgreSQL 17 dependency bundles:

```bash
scripts/package-server.sh
```

The selector package depends on `tellervo-server-db-pg18 |
tellervo-server-db-pg17`, so apt will prefer PostgreSQL 18 when the complete
PostgreSQL 18 bundle is installable. On Debian Trixie today, PGDG provides
`postgresql-17-pljava` but not `postgresql-18-pljava`, so apt should fall back
to the PostgreSQL 17 bundle.

To build only one PostgreSQL dependency bundle, pass `--postgres-version`. The
option may be repeated:

```bash
scripts/package-server.sh --postgres-version 17
scripts/package-server.sh --postgres-version 18
```

## Single-Host Install

For the traditional single-host deployment from an apt repository, install the
meta package:

```bash
sudo apt install tellervo-server
```

When installing directly from locally built `.deb` files, pass the meta package
and its local dependencies together. Raw local `.deb` files are not an apt
repository, so include the PostgreSQL provider package you want to use:

```bash
sudo apt install \
  ./target/server/2.0/Linux/tellervo-server-common-2.0.deb \
  ./target/server/2.0/Linux/tellervo-server-web-2.0.deb \
  ./target/server/2.0/Linux/tellervo-server-db-pg17-2.0.deb \
  ./target/server/2.0/Linux/tellervo-server-db-2.0.deb \
  ./target/server/2.0/Linux/tellervo-server-2.0.deb
```

Use `apt install ./package.deb` for local packages rather than `dpkg --install`.
`dpkg` can unpack local `.deb` files, but it will not resolve missing external
dependencies such as PostgreSQL, PostGIS, PL/Java or PHP modules.

If a system is left half-configured after a `dpkg --install` attempt, configure
the PostgreSQL package repository if necessary, then let apt repair the
installation:

```bash
sudo apt install -y postgresql-common
sudo /usr/share/postgresql-common/pgdg/apt.postgresql.org.sh
sudo apt update
sudo apt install -f
```

## Split Web and Database Install

On the database host, install the common and database packages together:

```bash
sudo apt install \
  ./target/server/2.0/Linux/tellervo-server-common-2.0.deb \
  ./target/server/2.0/Linux/tellervo-server-db-pg17-2.0.deb \
  ./target/server/2.0/Linux/tellervo-server-db-2.0.deb
```

On the web host, install the common and web packages together:

```bash
sudo apt install \
  ./target/server/2.0/Linux/tellervo-server-common-2.0.deb \
  ./target/server/2.0/Linux/tellervo-server-web-2.0.deb
```

The split package dependency graph is deliberately simple:

- web host: `tellervo-server-web` depends on `tellervo-server-common`
- database host: `tellervo-server-db` depends on `tellervo-server-db-pg18` or
  `tellervo-server-db-pg17`; each provider depends on
  `tellervo-server-common`
- single host: `tellervo-server` depends on `tellervo-server-common`,
  `tellervo-server-web` and `tellervo-server-db`
- PostgreSQL is selected by the database provider package

After package installation, configure the web host with `tellervo-server
--configure` or the named-instance flow, using the database host name or address
when prompted for the PostgreSQL server.

## Publishing An APT Repository

The easiest repeatable publishing flow is:

```bash
scripts/package-server.sh
scripts/publish-apt-repo.sh \
  --repo-dir /srv/apt/tellervo \
  --gpg-key "Tellervo APT Repository"
```

By default this publishes the packages from `target/server/VERSION/Linux/` into
a static APT repository with two suites:

```text
trixie
resolute
```

The repository layout is:

```text
/srv/apt/tellervo/
  pool/main/t/tellervo/*.deb
  dists/trixie/main/binary-amd64/Packages.gz
  dists/trixie/Release
  dists/trixie/InRelease
  dists/resolute/main/binary-amd64/Packages.gz
  dists/resolute/Release
  dists/resolute/InRelease
```

Install the publishing tools on the machine that builds or stages the repository:

```bash
sudo apt install dpkg-dev apt-utils gnupg
```

Create a dedicated signing key once:

```bash
gpg --quick-generate-key "Tellervo APT Repository <packages@tellervo.org>" rsa4096 sign 3y
gpg --armor --export "Tellervo APT Repository" > tellervo-archive-keyring.asc
```

If the public web server is separate from the build machine, publish locally and
sync the generated repository:

```bash
scripts/publish-apt-repo.sh \
  --repo-dir target/apt-repo \
  --gpg-key "Tellervo APT Repository"

rsync -av --delete target/apt-repo/ webhost:/srv/apt/tellervo/
```

Client setup for Debian Trixie:

```bash
curl -fsSL https://your-server.example/tellervo-archive-keyring.asc \
  | sudo tee /usr/share/keyrings/tellervo-archive-keyring.asc >/dev/null

echo "deb [signed-by=/usr/share/keyrings/tellervo-archive-keyring.asc] https://your-server.example/apt/tellervo trixie main" \
  | sudo tee /etc/apt/sources.list.d/tellervo.list

sudo apt update
sudo apt install tellervo-server
```

Client setup for Ubuntu 26.04 uses the `resolute` suite:

```bash
echo "deb [signed-by=/usr/share/keyrings/tellervo-archive-keyring.asc] https://your-server.example/apt/tellervo resolute main" \
  | sudo tee /etc/apt/sources.list.d/tellervo.list
```

To publish a single suite or add another suite:

```bash
scripts/publish-apt-repo.sh --repo-dir /srv/apt/tellervo --suite trixie
scripts/publish-apt-repo.sh --repo-dir /srv/apt/tellervo --suite trixie --suite resolute
```
