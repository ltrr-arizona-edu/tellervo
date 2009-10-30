-- Function: cpgdb.createmetacache(uuid)

-- DROP FUNCTION cpgdb.createmetacache(uuid);

CREATE OR REPLACE FUNCTION cpgdb.createmetacache(uuid)
  RETURNS tblvmeasurementmetacache AS
$BODY$
DECLARE
   vmid ALIAS FOR $1;
   op varchar;
   vmresult tblVMeasurementResult%ROWTYPE;
   ret tblVMeasurementMetaCache%ROWTYPE;
BEGIN
   -- RAISE NOTICE 'Creating metacache for %', vmid;

   -- acquire the vMeasurementresult
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
   ret.datingTypeID := vmresult.datingTypeID;

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
   INSERT INTO tblVMeasurementMetaCache(VMeasurementID, StartYear, ReadingCount, MeasurementCount, DatingTypeID)
      VALUES (ret.VMeasurementID, ret.StartYear, ret.ReadingCount, ret.MeasurementCount, ret.datingTypeID);

   -- Clean up
   DELETE FROM tblVMeasurementResult WHERE VMeasurementResultID = vmresult.VMeasurementResultID;

   -- Clear out our tblVMeasurementDerivedCache for this VMeasurement
   DELETE FROM tblVMeasurementDerivedCache WHERE VMeasurementID = vmid;
   -- Then, populate it.
   INSERT INTO tblVMeasurementDerivedCache(VMeasurementID,MeasurementID) 
      SELECT vmid,Measurement.MeasurementID FROM cpgdb.FindVMParentMeasurements(vmid) Measurement;

   -- Calculate extent of vmeasurement by looking up locations of all associated direct Measurements
  -- SELECT setsrid(extent(tblelement.locationgeometry)::geometry,4326)
   --   INTO  ret.vmextent
    --  FROM  tblelement, tblsample, tblradius, tblMeasurement, tblvmeasurement
    --  WHERE tblvmeasurement.measurementid=tblmeasurement.measurementid
    --  AND   tblmeasurement.radiusid=tblradius.radiusid
    --  AND   tblradius.sampleid=tblsample.sampleid
    --  AND   tblsample.elementid=tblelement.elementid
    --  AND   tblvmeasurement.vmeasurementid
      --      IN (SELECT vMeasurementid
      --             FROM  cpgdb.FindVMParents(vmid, true)
       --            WHERE op='Direct');

   -- Calculate extent of vmeasurement by looking up locations of all associated direct Measurements
   IF op = 'Direct' THEN
   RAISE NOTICE 'Getting extents from element and object.  Measurement count is %', ret.MeasurementCount;
	   SELECT setsrid(extent(st_collect(tblelement.locationgeometry, tblobject.locationgeometry)), 4326)
	      INTO  ret.vmextent
	      FROM  tblobject, tblelement, tblsample, tblradius, tblMeasurement, tblvmeasurement
	      WHERE tblvmeasurement.measurementid=tblmeasurement.measurementid
	      AND   tblmeasurement.radiusid=tblradius.radiusid
	      AND   tblradius.sampleid=tblsample.sampleid
	      AND   tblsample.elementid=tblelement.elementid
	      AND   tblelement.objectid=tblobject.objectid
	      AND   tblvmeasurement.vmeasurementid
		    IN (SELECT vMeasurementid
			   FROM  cpgdb.FindVMParents(vmid, true)
			   WHERE op='Direct');
   ELSE 
	RAISE NOTICE 'Getting extent from component vmeasurements';
	RAISE NOTICE 'Find all parents of %', vmid;
	RAISE NOTICE 'Number of measurements is %',ret.MeasurementCount;
	   SELECT setsrid(extent(tblvmeasurementmetacache.vmextent)::geometry, 4326)
	   INTO  ret.vmextent
	   FROM  tblvmeasurementmetacache
	   WHERE tblvmeasurementmetacache.vmeasurementid
		 IN (SELECT vMeasurementid
		   FROM  cpgdb.FindVMParents(vmid, false)
		   WHERE op='Direct');
   END IF;


   RAISE NOTICE 'Extent is %', ret.vmextent;

   -- Store extent info
   UPDATE tblVMeasurementMetaCache SET vmextent = ret.vmextent WHERE VMeasurementID = ret.VMeasurementID;

   -- Now, get taxon and label data and update that
   SELECT INTO ret.objectCode, ret.objectCount, ret.CommonTaxonName, ret.taxonCount, ret.prefix 
       s.objectCode,s.objectCount,s.commonTaxonName,s.taxonCount,cpgdb.GetVMeasurementPrefix(vmid) AS prefix
       FROM cpgdb.getVMeasurementSummaryInfo(vmid) AS s;
   UPDATE tblVMeasurementMetaCache SET (objectCode, objectCount, commonTaxonName, taxonCount, prefix) =
       (ret.objectCode, ret.objectCount, ret.CommonTaxonName, ret.taxonCount, ret.prefix)
       WHERE VMeasurementID = vmid;

   -- Return result
   RETURN ret;
END;

$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.createmetacache(uuid) OWNER TO lucasm;
