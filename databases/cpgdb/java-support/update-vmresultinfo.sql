-- First parameter: VMeasurementResultID
CREATE OR REPLACE FUNCTION cpgdbj.qupdVMeasurementResultInfo(uuid) RETURNS boolean AS $$
DECLARE
   ResultID ALIAS FOR $1;
   vmid tblVMeasurement.VMeasurementID%TYPE;
   c tblVMeasurement.Code%TYPE;
   comm tblVMeasurement.Comments%TYPE;
   ispub tblVMeasurement.isPublished%TYPE;
   createTS tblVMeasurement.CreatedTimestamp%TYPE;
   modTS tblVMeasurement.LastModifiedTimestamp%TYPE;
BEGIN
   SELECT VMeasurementID INTO vmid FROM tblVMeasurementResult WHERE VMeasurementResultID = ResultID;

   IF NOT FOUND THEN
      RETURN FALSE;
   END IF;

   SELECT Code, Comments, isPublished, CreatedTimestamp, LastModifiedTimestamp
     INTO c, comm, ispub, createTS, modTS
     FROM tblVMeasurement WHERE VMeasurementID = vmid;

   IF NOT FOUND THEN
      RETURN FALSE;
   END IF;

   UPDATE tblVMeasurementResult SET (Code, Comments, isPublished, CreatedTimestamp, LastModifiedTimestamp) =
      (c, comm, ispub, createTS, modTS) 
      WHERE VMeasurementResultID = ResultID;

   RETURN TRUE;
END;
$$ LANGUAGE plpgsql VOLATILE;
