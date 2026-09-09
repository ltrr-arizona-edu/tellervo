# Tellervo Server 2.1 installation and database migration

This guide describes installing the server implemented in this checkout and
migrating an existing Tellervo database to it. It covers a new Debian-based
server first, then migration, validation, and recovery.

See the [Server 2.1 release notes](server-release-2.1.md) for release scope.

## 1. Identify the version and deployment

In this checkout, the server package version is **2.1**, from `serverversion`
in the root [pom.xml](../pom.xml). The bundled database patches extend through
`database_upgrade-2.1.0c.sql`. These are separate identifiers: patch filenames
do not determine the Debian package version. The upgrade command writes the
installed package version into `tblconfig.wsversion` and tracks individual
patches in `tblupgradelog`.

Here, “latest” means the code and patches in this checkout, not a claim that
an external download or APT repository already contains this build. Record the
source commit and package checksums used for your installation. When working
with a later release, substitute its actual package version throughout.

The packaged server runs Apache/PHP with PostgreSQL, PostGIS, and PL/Java.
Use the root packaging workflow below; the separate `tellervo-server/pom.xml`
WAR project is not the deployment described here.

| Package | Purpose |
| --- | --- |
| `tellervo-server` | Single-host meta package selecting the web and database components |
| `tellervo-server-common` | Administration commands, SQL templates, patches, and Tellervo PL/Java JAR |
| `tellervo-server-webservice` | Apache configuration and PHP webservice |
| `tellervo-server-db` | Selects a PostgreSQL dependency bundle |
| `tellervo-server-db-pg17` / `tellervo-server-db-pg18` | Matching PostgreSQL, PostGIS, PL/Java, and Java dependencies |

The examples use PostgreSQL 17 and a named instance `lab-a`, with database and
PostgreSQL login role `tellervo_lab_a`. Substitute your names, addresses, and
paths. Run commands in Bash, using an account with `sudo` access. Commands marked
for a database host must run on that host.

## 2. Prepare the new host and packages

### Operating system and prerequisites

The repository's APT publishing workflow targets Debian Trixie and Ubuntu
Resolute. Confirm that your chosen distribution and architecture have all
dependencies before installing. The packages support PostgreSQL 17 and 18;
availability of the matching PL/Java package determines which you can use.

Allow space for the database, indexes, backup archives, a rehearsal restore,
and migration working space. Use a separate new host or VM when moving from
an old PostgreSQL installation. The database commands assume the intended
local PostgreSQL cluster is reachable through the default connection; they
have no `--host` or `--port` options.

On the **database host**, enable the PostgreSQL APT repository (PGDG):

```bash
sudo apt update
sudo apt install postgresql-common ca-certificates
sudo /usr/share/postgresql-common/pgdg/apt.postgresql.org.sh
sudo apt update
apt-cache policy postgresql-17 postgresql-17-postgis-3 postgresql-17-pljava
```

The repository helper is interactive. This is the procedure documented by
the [PostgreSQL Debian download page](https://www.postgresql.org/download/linux/debian/).
Do not proceed if a required package has `Candidate: (none)`. Check repository
setup and distribution support, or use the other provider only if its entire
dependency bundle is available. A web-only host does not need PGDG.

### Obtain or build matching packages

Use the complete matching set of server `.deb` files from your release
administrator, or build from the intended source revision on Linux. From the
repository root, with Maven, JDK 21, and `dpkg-deb` available:

```bash
git rev-parse HEAD
java -version
mvn -version
mvn -DskipTests package
bash scripts/package-server.sh
```

The Maven command creates `target/tellervo-pljava.jar`; the packaging script
requires it. The build skips tests in this example. Maven must be able to
resolve the project's dependencies, including the GitHub Packages repositories
declared in the root POM; configure repository credentials if access requires
them. Do not place credentials in this guide or commit them to Git.

The default output is `target/binaries/server/2.1/Linux/`. Copy the packages
to the target host, then change into the directory containing the `.deb` files:

```bash
sha256sum tellervo-server*-2.1.deb
dpkg-deb --field tellervo-server-common-2.1.deb Package Version
```

Compare checksums with those supplied by the builder. All following local
package commands assume this directory is the current working directory.

### Install on a single host

```bash
sudo apt install \
  ./tellervo-server-common-2.1.deb \
  ./tellervo-server-webservice-2.1.deb \
  ./tellervo-server-db-pg17-2.1.deb \
  ./tellervo-server-db-2.1.deb \
  ./tellervo-server-2.1.deb
```

For PostgreSQL 18, replace the `db-pg17` file with `db-pg18` after checking its
dependencies. Include just the provider you intend to use. `apt install` resolves
external dependencies; `dpkg --install` alone does not.

If your administrator already provides a configured, trusted Tellervo APT
repository, inspect `apt-cache policy tellervo-server` and install with
`sudo apt install tellervo-server` instead. The selector prefers PostgreSQL 18
when its full bundle is installable, with PostgreSQL 17 as the alternative.
See [server packaging](server-packaging.md) for repository setup and publishing.

Confirm the selected cluster and runtime:

```bash
pg_lsclusters
sudo -u postgres psql -d postgres -c 'SELECT version();'
sudo -u postgres psql -d postgres -c \
  "SELECT name, default_version FROM pg_available_extensions WHERE name IN ('postgis', 'pljava');"
ls -l /usr/lib/jvm/default-java/lib/server/libjvm.so
```

The PL/Java bootstrap uses this JVM path and sets
`pljava.vmoptions = '-Djava.security.manager=allow'`. Use a compatible Java
runtime; the source build targets Java 21. The bootstrap also changes these
settings at cluster level with `ALTER SYSTEM`, so consider other databases on
the same cluster when planning an existing-host installation.

## 3. Create and configure a fresh installation

**For an existing database, skip database creation and follow section 5 first.**
The configuration wizard can apply upgrades immediately, so take and validate
your backup before pointing it at existing data.

### Initialise the database

On the database host:

```bash
sudo tellervo-server-db init --dbname tellervo_lab_a --user tellervo_lab_a
```

Enter the PostgreSQL login password twice. This command creates an empty
Tellervo database from the packaged template, bootstraps PL/Java, installs the
Tellervo JAR, and applies compatibility roles and privileges. It refuses to
replace an existing database. Use a new role name: `init` resets the password
even when the specified role already exists.

The current helper grants the application login PostgreSQL **superuser**
privileges for the packaged functions and upgrade process. Keep the credential
private and restrict access to PostgreSQL. This password is distinct from a
Tellervo user's desktop login.

### Configure the webservice

On the web host (the same machine for a single-host installation):

```bash
sudo tellervo-server --instance lab-a --configure
```

Answer the wizard as follows:

1. Enter the laboratory name, optional acronym, and webservice domain.
2. Enter `localhost` and port `5432` for a single host, or the database host's
   address and actual port for a split installation.
3. If asked to create a new database, choose **No** because you already used
   `init`. Enter `tellervo_lab_a` as the existing database.
4. Choose **No** when asked to create a database user. Supply the existing
   `tellervo_lab_a` PostgreSQL role and its password.
5. Allow Apache configuration on a dedicated Tellervo host. On a shared web
   host, review the existing virtual hosts first: the wizard enables
   `tellervo-apache.conf` and disables `000-default.conf`.

The wizard writes configuration, generates `systemconfig.php`, runs outstanding
database patches, registers the instance, and tests the setup. Its setup path
runs upgrades without taking the interactive pre-upgrade backup.

| Item | Named instance `lab-a` |
| --- | --- |
| Web folder | `/var/www/lab-a/` |
| Administrator settings | `/var/www/lab-a/config.php` |
| Generated settings | `/var/www/lab-a/systemconfig.php` |
| Database credentials | `/usr/share/tellervo-server/server_credentials.lab-a` |
| Instance registry | `/etc/tellervo-server/instances.conf` |
| Generated Apache aliases | `/etc/apache2/conf-available/tellervo-instances.conf` |
| URL path | `/lab-a/` |
| Shared media files | `/usr/share/tellervo-server/mediastore/` |

For the legacy default instance, omit `--instance lab-a`; its web folder is
`/var/www/tellervo/`, credentials file is `server_credentials`, and the supplied
virtual host serves that folder at its document root. Use the same instance
selection consistently for every maintenance command.

### Validate and enable client access

```bash
sudo tellervo-server --instance lab-a --test
sudo tellervo-server --instance lab-a --version
sudo apache2ctl configtest
sudo systemctl reload apache2
```

Inspect the output and `/var/log/tellervo-installation.log`, not just the
command's exit status. Some legacy error paths do not return a failing status.
Public URL reachability can be reported as a warning while DNS, TLS, or a
reverse proxy is still being configured.

Configure DNS and HTTPS for your actual hostname. The supplied Apache site
provides HTTP, and the configuration template sets `$securehttp = FALSE`;
arrange the certificate/HTTPS virtual host or proxy and update the instance's
`config.php` accordingly. Restrict access during setup.

Open the instance URL, for example `https://trees.example.org/lab-a/`, and
configure a Tellervo desktop client to use that webservice. On a fresh database,
use the initial administrator login reported by setup and immediately change
its password in the desktop application's user administration. Confirm the new
password works before allowing users onto the server.

## 4. Separate web and database hosts

On the database host, install `common`, `db-pg17` (or `db-pg18`), and `db` using
the corresponding local files from section 2. On the web host, install only
`common` and `webservice`. Keep both hosts on the same Tellervo build: SQL
patches load the JAR from `/usr/share/tellervo-server/tellervo-pljava.jar` on
the **database host**.

Initialise or restore the database there before configuring the web host.
For example, with database host `192.0.2.10` and web host `192.0.2.20`, locate
the active PostgreSQL configuration files:

```bash
sudo -u postgres psql -Atqc 'SHOW config_file;'
sudo -u postgres psql -Atqc 'SHOW hba_file;'
```

Set the private listening address in `postgresql.conf`:

```text
listen_addresses = 'localhost,192.0.2.10'
```

Add an appropriate rule to `pg_hba.conf`, before any matching rejection:

```text
host    tellervo_lab_a    tellervo_lab_a    192.0.2.20/32    scram-sha-256
```

Restart PostgreSQL for the listening-address change:

```bash
sudo systemctl restart postgresql
```

Restrict port 5432 in the firewall to the web host and authorised administration
hosts. These example addresses must be replaced. Use an appropriately protected
network or configure PostgreSQL TLS when traffic crosses an untrusted network.
From the web host, test the connection before starting the wizard:

```bash
psql --host=192.0.2.10 --port=5432 --dbname=tellervo_lab_a \
  --username=tellervo_lab_a --password \
  --command='SELECT current_database(), current_user;'
```

**Current upgrade limitation:** `tellervo-server` checks for a local PL/Java
control file when visiting the `1.3.0e.notransaction` and `2.0.1b` patches,
even before checking whether they were applied. Its nontransactional runner
also executes `psql` locally as `postgres`. A web-only host can therefore fail
during configuration/upgrades even when its remote database connection works.
Do not treat the split-package dependency layout as proof that historical
upgrades can all run remotely. Rehearse this deployment first; the single-host
migration path below avoids that assumption. Resolve this limitation before
production cutover of a split installation rather than changing upgrade-log
entries to bypass it.

## 5. Migrate an older database

Migration has two parts: transferring the database to the selected PostgreSQL
runtime, and applying Tellervo's application patches. Installing a new
PostgreSQL package or running `--upgrade-db` does not convert an old PostgreSQL
data directory. This procedure uses a logical dump and restore, one of the
[PostgreSQL major-version upgrade methods](https://www.postgresql.org/docs/current/upgrading.html).

### 5.1 Inventory and rehearse

Record the old server's package versions, PostgreSQL version/cluster/port,
database name, PostgreSQL role, instance URL, and configuration paths. Retain
the old packages or a recoverable system snapshot. On the old database host:

```bash
pg_lsclusters
sudo -u postgres psql -d old_tellervo_database -c 'SELECT version();'
sudo -u postgres psql -d old_tellervo_database -c \
  "SELECT key, value FROM tblconfig WHERE key = 'wsversion';"
sudo -u postgres psql -d old_tellervo_database -c \
  'SELECT filename, timestamp FROM tblupgradelog ORDER BY timestamp, filename;'
```

If these Tellervo tables are missing, stop and identify the source schema before
running current patches. Very old or partially upgraded installations may need
individual repairs; this repository does not establish a guaranteed direct
upgrade path from every historical version.

Save representative record counts using section 6. Also inventory custom
PostgreSQL roles, tablespaces, extensions, and site-specific SQL. The restore
helper deliberately discards old ownership and ACL entries; custom grants and
dependencies need separate review.

Back up the web host's `config.php`, `systemconfig.php`, credential files,
instance registry (if present), Apache/TLS configuration, and media store.
Include any custom file locations. A PostgreSQL dump does **not** include these
files. Keep credentials and backups in restricted storage outside the public
web directory. Preserve old settings for reference and rollback; configure the
new webservice using its current template rather than copying old PHP code over
the new installation.

First perform the following restore and upgrade against an isolated test
instance on the new host. Allow time for comparison and application testing
before scheduling the final outage.

### 5.2 Create and transfer a validated backup

The current backup helper can run on the old host without installing new
Tellervo packages. From the new database host:

```bash
scp /usr/bin/tellervo-server-db old-db-host:/tmp/tellervo-server-db
```

On the old database host, create a restricted backup directory and use a unique
filename for each backup:

```bash
chmod 0755 /tmp/tellervo-server-db
sudo install -d -m 0700 /srv/backups/tellervo
sudo /tmp/tellervo-server-db backup \
  --dbname old_tellervo_database \
  --file /srv/backups/tellervo/old_tellervo_database-rehearsal.dump
sudo sha256sum /srv/backups/tellervo/old_tellervo_database-rehearsal.dump
```

This command uses the local PostgreSQL administrator, creates a custom-format
archive through a temporary file, validates its table of contents, and refuses
to overwrite an existing backup. It needs Bash, `runuser`, and PostgreSQL client
tools on the old host. Ensure the `pg_dump` client can read that server version;
do not replace its old server packages merely to obtain a backup.

Transfer the dump with an authorised account over SSH/SFTP to restricted storage
on the new database host, for example `/srv/backups/tellervo/`. The directory
above is root-only, so arrange access through your administrative account rather
than making the backup public. Compare the SHA-256 value on both hosts:

```bash
sudo sha256sum /srv/backups/tellervo/old_tellervo_database-rehearsal.dump
sudo pg_restore --list \
  /srv/backups/tellervo/old_tellervo_database-rehearsal.dump > /dev/null
```

A readable archive and matching checksum confirm transfer integrity; the test
restore and application checks establish whether it can be used successfully.

### 5.3 Restore into a new database

On the new database host:

```bash
sudo tellervo-server-db restore \
  --dbname tellervo_lab_a_test \
  --user tellervo_lab_a_test \
  --file /srv/backups/tellervo/old_tellervo_database-rehearsal.dump
```

Do not run `init` first: `restore` creates the target database itself and refuses
to replace an existing one. It prompts for a password if the login role is new;
an existing role is reused without changing its password, but its privileges
are adjusted by the helper. A dedicated test role avoids affecting production.

The helper bootstraps the current PL/Java extension, filters old PL/Java entries,
ACLs, and the incompatible `array_accum` aggregate from the restore list,
installs the current Tellervo JAR, and reapplies Tellervo privileges. It does
**not** run the complete Tellervo patch sequence. On failure it attempts to
remove only the newly created incomplete target database; role and cluster
configuration changes can remain. Review any failure before retrying.

Stage the backed-up media on the isolated new web host, preserving filenames
and paths and giving `www-data` the required access. The packaged media store
is shared by instances, so do not overwrite another installation's media during
a rehearsal.

### 5.4 Configure the test instance and apply patches

On the new single-host server:

```bash
sudo tellervo-server --instance lab-a-test \
  --dbname tellervo_lab_a_test --configure
```

Follow section 3's wizard answers, selecting the **existing** restored database
and role. Configuration can start applying patches immediately. Keep this URL
restricted to administrators until validation is complete.

For a configured instance, the explicit maintenance command is:

```bash
sudo tellervo-server --instance lab-a-test --upgrade-db
sudo tellervo-server --instance lab-a-test --sysconfig
sudo tellervo-server --instance lab-a-test --test
```

The upgrade command asks whether to make an additional backup. Keep the
independent validated dump regardless; the legacy interactive backup is not a
substitute, particularly for remote databases.

The runner sorts bundled patches naturally and skips exact filenames already
in `tblupgradelog`. Standard patches run in individual transactions; the
`.notransaction.sql` patch runs separately. The whole migration is not one
atomic transaction. Stop at the first error and investigate the log.

Never run all SQL files manually or delete upgrade-log rows to force them to
rerun. Historical taxon migrations can damage already migrated data. Check for
old patch names that differ from the current names before upgrading, especially
on databases with a history of manual repairs.

### 5.5 Check historical taxon damage

The checkout includes a read-only audit script. From its repository root, on
the database host, with Python 3 available:

```bash
sudo -u postgres python3 scripts/audit-corrupt-tlkptaxon.py \
  --databases tellervo_lab_a_test
```

Run the audit before and after upgrades where the schema supports it. The script
is a source-tree tool, not installed by the server packages; copy it to an
accessible directory if necessary. Follow the
[taxon audit and repair runbook](taxon-dictionary-fleet-repair.md) for `BROKEN`
or `REVIEW` results. Do not apply a generic or another database's generated
repair blindly.

Patch `2.1.0c` prevents new NULL element taxon links, but it does not recover
historical missing links. It may leave a `NOT VALID` constraint and emit a
warning when old NULLs exist. Record and resolve this data issue separately;
successful patch execution is not evidence that the taxonomy is repaired.

### 5.6 Final migration and cutover

After the rehearsal passes:

1. Schedule downtime and prevent all writes to the old instance, including
   desktop clients, imports, ODK submissions, and scheduled integrations. Keep
   PostgreSQL running for the dump. On a dedicated web host you can stop Apache;
   on a shared host restrict just the relevant site. Use an access restriction
   that package installation cannot undo by restarting Apache.
2. Take a new final database dump and a matching media/configuration backup.
   Use a new filename, transfer it, and compare checksums again. Keep the old
   instance closed to writes from this point.
3. Restore the final dump to a new production database, for example
   `tellervo_lab_a`, using the same `restore` command with the final filename
   and production role. Do not promote a stale rehearsal database.
4. Configure `lab-a` against this restored database, apply the same upgrades,
   restore the final media, and repeat section 6's checks.
5. Point the production DNS/proxy/client URL to the new instance, verify HTTPS,
   then permit users and integrations to reconnect. Ensure only the new
   instance accepts writes.
6. Retain the old host, final dump, media, configuration, and version record
   until the agreed rollback period has passed. Start scheduled backups on the
   new server and verify a restore before retiring the old one.

## 6. Validate data and application behaviour

Run these SQL checks on the source before the final dump and on the upgraded
target. Replace the database name as appropriate:

```bash
sudo -u postgres psql --dbname=tellervo_lab_a --set=ON_ERROR_STOP=1 <<'SQL'
SELECT current_database(), version();
SELECT key, value FROM tblconfig WHERE key = 'wsversion';
SELECT filename, timestamp FROM tblupgradelog ORDER BY timestamp, filename;

SELECT 'projects' AS entity, count(*) FROM tblproject
UNION ALL SELECT 'objects', count(*) FROM tblobject
UNION ALL SELECT 'elements', count(*) FROM tblelement
UNION ALL SELECT 'samples', count(*) FROM tblsample
UNION ALL SELECT 'radii', count(*) FROM tblradius
UNION ALL SELECT 'measurements', count(*) FROM tblmeasurement
UNION ALL SELECT 'virtual measurements', count(*) FROM tblvmeasurement;

SELECT count(*) AS elements_without_taxon
FROM tblelement WHERE taxonid IS NULL;
SELECT count(*) AS orphaned_taxon_links
FROM tblelement e LEFT JOIN tlkptaxon t ON t.taxonid = e.taxonid
WHERE e.taxonid IS NOT NULL AND t.taxonid IS NULL;
SQL
```

Investigate unexplained count changes. Compare measurements, dates, metadata,
taxon assignments, user accounts, and group permissions for representative
records; counts alone cannot detect incorrect values. Dictionary counts may
change legitimately as patches update reference data.

Check that `tblupgradelog` accounts for the installed patch files, including
`database_upgrade-2.1.0a.sql`, `database_upgrade-2.1.0b.sql`, and
`database_upgrade-2.1.0c.sql` for this checkout. The first two refresh PL/Java
measurement retrieval and REDATE handling. A `wsversion` value of `2.1` alone
does not prove these patches ran.

On the target, verify extensions and the installed JAR:

```bash
sudo -u postgres psql --dbname=tellervo_lab_a --set=ON_ERROR_STOP=1 <<'SQL'
SELECT extname, extversion FROM pg_extension ORDER BY extname;
SELECT jarname FROM sqlj.jar_repository WHERE jarname = 'tellervo_jar';
ANALYZE;
SQL
```

Confirm the expected PostGIS and PL/Java functionality. With the desktop client:

- Log in as an administrator and as a normal user; verify their permissions.
- Browse projects, objects, elements, samples, radii, and dictionaries.
- Load raw measurements and derived series, including a REDATE series, and
  compare dates and ring values with the old server.
- Check mapped locations, media attachments, and ODK workflows if used.
- On the test instance, create, edit, and reload a disposable record to verify
  writes as well as reads.

Finish with `sudo tellervo-server --instance lab-a --test`, Apache's
configuration test, and a review of installation, Apache, and PostgreSQL logs.

## 7. Existing-host upgrades and rollback

For an upgrade on the same host, first take the database and filesystem backups
from section 5 and rehearse on a clone. Keep users out during package changes.
The webservice package's upgrade hook can run database upgrades for the default
configured webservice and restart Apache. Do not wait until after `apt install`
to back up the database.

On split hosts, update the database host's common package/JAR before applying
patches from the web host, subject to the limitation in section 4. For named
instances, inspect `sudo tellervo-server --list-instances` and upgrade/test each
instance explicitly. Cluster-wide commands exist, but use them only after
individual migrations have been rehearsed and every database is backed up.

If migration fails, keep the new instance unavailable and retain its logs.
The first failed SQL patch and its PostgreSQL error are usually more useful
than later connection or version warnings. Do not continue by marking a failed
patch as applied.

Before the new server accepts writes, rollback can redirect clients to the
unchanged old server and reopen it. After the new server accepts writes,
switching back would lose those changes: stop writes, back up the new state,
and plan reconciliation before reopening the old server.

For a same-host rollback, restore the pre-upgrade database into a fresh target
using a runtime compatible with the old server, and restore the matching old
application/configuration/media or system snapshot. Downgrading `.deb` files
does not reverse SQL patches. Never use `tellervo-server --reconfigure` or
`--delete-instance` as troubleshooting shortcuts: they can delete data.

## 8. Troubleshooting reference

| Symptom | Check or next step |
| --- | --- |
| PL/Java package has no candidate | Check PGDG setup and availability of the complete provider bundle for the selected distribution and architecture. |
| Package installation is incomplete | Resolve repository/dependency problems, then run `sudo apt install -f`; inspect `/var/log/tellervo-installation.log`. |
| Commands connect to the wrong PostgreSQL server | Check `pg_lsclusters` and the default local connection; the database helper does not select a cluster by port. |
| PL/Java cannot load the JVM | Check the configured `default-java` JVM path, Java compatibility, matching PL/Java package, and PostgreSQL logs. |
| Remote connection succeeds but upgrades fail | Check the local PL/Java/nontransactional-runner limitation in section 4 and the JAR version on the database host. |
| Archive restore fails | Verify the checksum, inspect `pg_restore --list`, identify missing extensions/custom dependencies, and retry only into a new target after diagnosing the error. |
| Taxon warning or audit failure | Follow the database-specific taxon repair runbook; preserve upgrade history and backups. |
| Webservice is unreachable | Check Apache configuration, instance alias, firewall, DNS, TLS, and `/var/log/apache2/error.log`. |
| Client connects but series fail to load | Verify the PL/Java JAR and patch history, then exercise raw and REDATE series while inspecting server logs. |

Implementation references: [package builder](../scripts/package-server.sh),
[database helper](../Native/BuildResources/LinBuild/tellervo-server-db),
[webservice administrator](../Native/BuildResources/LinBuild/tellervo-server),
and [database patches](../Databases/db-upgrade-patches/).
