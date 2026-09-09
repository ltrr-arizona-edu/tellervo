# Tellervo Server 2.1

Tellervo Server 2.1 gives the current server code and database updates a distinct
release version. New server packages use Debian version `2.1`, superseding `2.0`.

## Included in this release

- Split packages for the webservice, shared administration tools, and database
  dependencies, with a single-host `tellervo-server` meta package.
- PostgreSQL 17 and 18 dependency bundles, subject to availability of matching
  PostGIS and PL/Java packages.
- Named webservice instances and an instance registry.
- Database initialisation, validated custom-format backups, and restore into a
  new database through `tellervo-server-db`.
- Database patches through `database_upgrade-2.1.0c.sql`, including the PL/Java
  measurement retrieval and REDATE fixes, and protection against new NULL
  element taxon links.

## Installation and migration

Follow the [installation and migration guide](server-installation-details.md)
for prerequisites, package installation, configuration, and validation. See
[server packaging](server-packaging.md) for build and APT publishing commands.

The build produces these files in `target/binaries/server/2.1/Linux/`:

```text
tellervo-server-2.1.deb
tellervo-server-common-2.1.deb
tellervo-server-webservice-2.1.deb
tellervo-server-db-2.1.deb
tellervo-server-db-pg17-2.1.deb
tellervo-server-db-pg18-2.1.deb
```

Install the provider matching your selected PostgreSQL major version. All
Tellervo components on the web and database hosts must come from the same build.

Back up the database, media, and configuration before installing packages or
running the configuration wizard: both package upgrade hooks and configuration
can apply database patches. Rehearse migration on a separate restored database.

Existing SQL patch filenames and the `2.0` database template retain their
historical names. The upgrade runner uses exact filenames in `tblupgradelog`
to avoid applying a patch twice; renaming them for this release would be unsafe.
After a successful upgrade, `tblconfig.wsversion` reports `2.1`. Verify the
individual patch history and application behaviour as well as this version.

## Known migration limitations

- Historical upgrades on split hosts still contain local PostgreSQL/PL/Java
  checks and a local nontransactional SQL runner. See the installation guide
  before configuring a web-only host against an older remote database.
- The database helper currently grants the application PostgreSQL login
  superuser privileges and adjusts cluster-wide PL/Java configuration.
- Patch `2.1.0c` prevents new missing taxon links but does not recover historical
  damage. Use the [taxon audit and repair guide](taxon-dictionary-fleet-repair.md)
  where needed.
- A package downgrade does not undo database patches. Retain a tested backup and
  a compatible previous server environment for rollback.

## Release scope

This version applies to the server packages via the root POM's `serverversion`
property. The desktop application's Maven version is managed separately.
The current GitHub `v*` tag workflow builds desktop installers; it does not
build or publish these server packages. Publish the server artifacts using the
server packaging workflow and record their checksums with the release.
