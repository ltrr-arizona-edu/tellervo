SET pljava.libjvm_location TO '/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/amd64/server/libjvm.so';
CREATE EXTENSION IF NOT EXISTS pljava; 
ALTER DATABASE tellervo SET pljava.libjvm_location FROM current;
