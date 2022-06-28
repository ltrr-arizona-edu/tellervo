--- Patch for PLJava upgrade/installation
--- Note ALTER SYSTEM command cannot be run in a transaction

\set ON_ERROR_STOP true

SET pljava.libjvm_location TO '/usr/lib/jvm/java-11-openjdk-amd64/lib/server/libjvm.so';
ALTER SYSTEM SET pljava.libjvm_location TO '/usr/lib/jvm/java-11-openjdk-amd64/lib/server/libjvm.so';
CREATE EXTENSION IF NOT EXISTS pljava;