SET pljava.libjvm_location TO '/usr/lib/jvm/default-java/lib/server/libjvm.so';
SET pljava.vmoptions TO '-Djava.security.manager=allow';
CREATE EXTENSION IF NOT EXISTS pljava;
DELETE FROM sqlj.jar_repository WHERE jarname='tellervo_jar';
SELECT sqlj.install_jar('file:///usr/share/tellervo-server/tellervo-pljava.jar', 'tellervo_jar', false);
SELECT sqlj.set_classpath('cpgdb', 'tellervo_jar');
UPDATE sqlj.jar_repository SET jarowner='tellervo' WHERE jarname='tellervo_jar';

CREATE OR REPLACE FUNCTION cpgdb.getvmeasurementresult(uuid) RETURNS SETOF public.tblvmeasurementresult
    LANGUAGE java
    AS $$org.tellervo.cpgdb.VMeasurementResultSet.getVMeasurementResultSet$$; 

CREATE OR REPLACE FUNCTION cpgdb.GetVMeasurementReadingResult(uuid)
   RETURNS SETOF tblVMeasurementReadingResult AS 
      'SELECT * from tblVMeasurementReadingResult WHERE VMeasurementResultID = $1 ORDER BY relyear'
   LANGUAGE SQL STABLE;
