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
   BEGIN
      SELECT * INTO vmresult FROM cpgdb.GetVMeasurementResult(vmid);
   EXCEPTION WHEN OTHERS THEN
      RAISE NOTICE 'CreateMetaCache(%) failed: VMeasurement is malformed or does not exist', vmid;
      RETURN NULL;
   END;

   IF NOT FOUND THEN
      RETURN NULL;
   END IF;

   ret.VMeasurementID := vmid;
   ret.StartYear := vmresult.StartYear;

   -- Calculate number of readings
   SELECT COUNT(*) INTO ret.ReadingCount
      FROM tblVMeasurementReadingResult WHERE VMeasurementResultID = vmresult.VMeasurementResultID;

   -- Calculate number of measurements
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

   -- Delete and populate the cache
   DELETE FROM tblVMeasurementMetaCache WHERE VMeasurementID = vmid;
   INSERT INTO tblVMeasurementMetaCache(VMeasurementID, StartYear, ReadingCount, MeasurementCount)
      VALUES (ret.VMeasurementID, ret.StartYear, ret.ReadingCount, ret.MeasurementCount);

   -- Clean up
   DELETE FROM tblVMeasurementResult WHERE VMeasurementResultID = vmresult.VMeasurementResultID;

   -- Clear out our tblVMeasurementDerivedCache for this VMeasurement
   DELETE FROM tblVMeasurementDerivedCache WHERE VMeasurementID = vmid;
   -- Then, populate it.
   INSERT INTO tblVMeasurementDerivedCache(VMeasurementID,MeasurementID) 
      SELECT vmid,* FROM cpgdb.FindVMParentMeasurements(vmid);

   -- Calculate extent of vmeasurement by looking up locations of all associated direct measurements
   SELECT setsrid(extent(tbltree.location)::geometry,4326)
      INTO  ret.vmextent
      FROM  tbltree, tblspecimen, tblradius, tblmeasurement, tblvmeasurement
      WHERE tblvmeasurement.measurementid=tblmeasurement.measurementid
      AND   tblmeasurement.radiusid=tblradius.radiusid
      AND   tblradius.specimenid=tblspecimen.specimenid
      AND   tblspecimen.treeid=tbltree.treeid
      AND   tblvmeasurement.vmeasurementid
            IN (SELECT vmeasurementid
                   FROM  cpgdb.FindVMParents(vmid, true)
                   WHERE op='Direct');

   --RAISE NOTICE 'Extent is %', ret.vmextent;

   -- Store extent info
   UPDATE tblVMeasurementMetaCache SET vmextent = ret.vmextent WHERE VMeasurementID = ret.VMeasurementID;

   -- Return result
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

CREATE OR REPLACE FUNCTION cpgdb.GetDerivedCache(tblVMeasurement.VMeasurementID%TYPE) 
RETURNS SETOF tblVMeasurementDerivedCache AS $$
   SELECT * FROM cpgdb.GetMetaCache($1);
   SELECT * FROM tblVMeasurementDerivedCache WHERE VMeasurementID = $1;
$$ LANGUAGE SQL;
