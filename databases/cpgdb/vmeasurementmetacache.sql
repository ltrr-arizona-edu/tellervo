CREATE OR REPLACE FUNCTION cpgdb.CreateMetaCache(tblVMeasurement.VMeasurementID%TYPE) 
RETURNS tblVMeasurementMetaCache AS $$
DECLARE
   vmid ALIAS FOR $1;
   op varchar;
   vmresult tblVMeasurementResult%ROWTYPE;
   ret tblVMeasurementMetaCache%ROWTYPE;
BEGIN
   -- RAISE NOTICE 'Creating metacache for %', vmid;

   -- acquire the vmeasurementresult
   SELECT * INTO vmresult FROM cpgdb.GetVMeasurementResult(vmid);

   IF NOT FOUND THEN
      RETURN NULL;
   END IF;

   ret.VMeasurementID := vmid;
   ret.StartYear := vmresult.StartYear;

   SELECT COUNT(*) INTO ret.ReadingCount
      FROM tblVMeasurementReadingResult WHERE VMeasurementResultID = vmresult.VMeasurementResultID;

   SELECT FIRST(tlkpVMeasurementOp.Name), COUNT(tblVMeasurementGroup.VMeasurementID) 
      INTO op, ret.MeasurementCount
      FROM tblVMeasurement 
      INNER JOIN tlkpVMeasurementOp ON tblVMeasurement.VMeasurementOpID = tlkpVMeasurementOp.VMeasurementOpID 
      LEFT JOIN tblVMeasurementGroup ON tblVMeasurement.VMeasurementID = tblVMeasurementGroup.VMeasurementID 
      WHERE tblVMeasurement.VMeasurementID = vmid;

   -- For a Direct VMeasurement, force 1.
   IF op = 'Direct' THEN
      ret.MeasurementCount := 1;
   END IF;

   DELETE FROM tblVMeasurementMetaCache WHERE VMeasurementID = vmid;
   INSERT INTO tblVMeasurementMetaCache(VMeasurementID, StartYear, ReadingCount, MeasurementCount)
      VALUES (ret.VMeasurementID, ret.StartYear, ret.ReadingCount, ret.MeasurementCount);

   -- Clean up and return
   DELETE FROM tblVMeasurementResult WHERE VMeasurementResultID = vmresult.VMeasurementResultID;
   RETURN ret;
END;
$$ LANGUAGE 'plpgsql' VOLATILE;

CREATE OR REPLACE FUNCTION cpgdb.GetMetaCache(tblVMeasurement.VMeasurementID%TYPE) 
RETURNS tblVMeasurementMetaCache AS $$
DECLARE
   vmid ALIAS FOR $1;
   ret tblVMeasurementMetaCache%ROWTYPE;
BEGIN
   -- Do we already have it cached?
   SELECT * INTO ret FROM tblVMeasurementMetaCache WHERE VMeasurementID = vmid;
   IF FOUND THEN
      -- RAISE NOTICE 'Cache hit on %', vmid;
      RETURN ret;
   END IF;

   -- Not cached, so we have to make it
   RAISE NOTICE 'Cache miss on %', vmid;
   SELECT * INTO ret FROM cpgdb.CreateMetaCache(vmid);
   RETURN ret;
END;
$$ LANGUAGE 'plpgsql' VOLATILE;
