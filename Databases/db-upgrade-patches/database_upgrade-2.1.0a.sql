-- Refresh the Tellervo PL/Java code after removing a debug call to
-- System.getProperties(). Trusted PL/Java functions correctly reject that
-- call because it requests PropertyPermission "*" "read,write".
SELECT sqlj.replace_jar(
    'file:///usr/share/tellervo-server/tellervo-pljava.jar',
    'tellervo_jar',
    false
);

SELECT sqlj.set_classpath('cpgdb', 'tellervo_jar');

CREATE OR REPLACE FUNCTION cpgdb.getvmeasurementresult(uuid)
RETURNS SETOF public.tblvmeasurementresult
LANGUAGE java
AS $$org.tellervo.cpgdb.VMeasurementResultSet.getVMeasurementResultSet$$;
