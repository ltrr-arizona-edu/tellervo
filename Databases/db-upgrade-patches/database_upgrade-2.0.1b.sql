CREATE OR REPLACE FUNCTION cpgdb.getvmeasurementresult(uuid) RETURNS SETOF public.tblvmeasurementresult
    LANGUAGE javau
    AS $$org.tellervo.cpgdb.VMeasurementResultSet.getVMeasurementResultSet$$; 

CREATE OR REPLACE FUNCTION cpgdb.GetVMeasurementReadingResult(uuid)
   RETURNS SETOF tblVMeasurementReadingResult AS 
      'SELECT * from tblVMeasurementReadingResult WHERE VMeasurementResultID = $1 ORDER BY relyear'
   LANGUAGE SQL STABLE;