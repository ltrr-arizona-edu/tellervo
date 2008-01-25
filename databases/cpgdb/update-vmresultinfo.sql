-- First parameter: VMeasurementResultID
CREATE OR REPLACE FUNCTION cpgdb.qupdVMeasurementResultInfo(varchar) RETURNS void AS $$
DECLARE
   ResultID ALIAS FOR $1;
   vmid tblVMeasurement.VMeasurementID%TYPE;
   n tblVMeasurement.Name%TYPE;
   desc tblVMeasurement.Description%TYPE;
   ispub tblVMeasurement.isPublished%TYPE;
   createTS tblVMeasurement.CreatedTimestamp%TYPE;
   modTS tblVMeasurement.LastModifiedTimestamp%TYPE;
BEGIN
   SELECT VMeasurementID INTO vmid FROM tblVMeasurementResult WHERE VMeasurementResultID = ResultID;

   IF NOT FOUND THEN
      RETURN;
   END IF;

   SELECT Name, Description, isPublished, CreatedTimestamp, LastModifiedTimestamp
     INTO n, desc, ispub, createTS, modTS
     FROM tblVMeasurement WHERE VMeasurementID = VMeasurementID;

   IF NOT FOUND THEN
      RETURN;
   END IF;

   UPDATE tblVMeasurementResult SET (Name, Description, isPublished, CreatedTimestamp, LastModifiedTimestamp) =
      (n, desc, ispub, createTS, modTS) 
      WHERE VMeasurementResultID = ResultID;
END;
$$ LANGUAGE plpgsql VOLATILE;
