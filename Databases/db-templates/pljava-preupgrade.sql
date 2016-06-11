SET pljava.libjvm_location TO '/usr/lib/jvm/java-8-openjdk-amd64/server/libjvm.so';
CREATE EXTENSION pljava; 
ALTER DATABASE tellervo SET pljava.libjvm_location FROM current;
