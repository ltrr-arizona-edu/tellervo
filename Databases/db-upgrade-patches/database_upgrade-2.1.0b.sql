-- Refresh the Tellervo PL/Java code with the corrected REDATE query. The old
-- prepared statement declared three parameters but supplied only two, which
-- caused REDATE series loading to fail before results could be returned.
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
