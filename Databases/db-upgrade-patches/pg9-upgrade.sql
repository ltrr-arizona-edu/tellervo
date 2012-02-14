

CREATE OR REPLACE FUNCTION cpgdb.createmetacache(uuid)
  RETURNS tblvmeasurementmetacache AS
$BODY$
#variable_conflict use_variable
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
   SELECT setsrid(extent(tblelement.locationgeometry)::geometry,4326)
      INTO  ret.vmextent
      FROM  tblelement, tblsample, tblradius, tblMeasurement, tblvmeasurement
      WHERE tblvmeasurement.measurementid=tblmeasurement.measurementid
      AND   tblmeasurement.radiusid=tblradius.radiusid
      AND   tblradius.sampleid=tblsample.sampleid
      AND   tblsample.elementid=tblelement.elementid
      AND   tblvmeasurement.vmeasurementid
            IN (SELECT vMeasurementid
                   FROM  cpgdb.FindVMParents(vmid, true)
                   WHERE op='Direct');

   --RAISE NOTICE 'Extent is %', ret.vmextent;

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
  LANGUAGE plpgsql VOLATILE
  COST 100;




-- Ensure no conflicts between column and variable names
-- Function: cpgdb.createmetacache(uuid)

-- DROP FUNCTION cpgdb.createmetacache(uuid);

CREATE OR REPLACE FUNCTION cpgdb.createmetacache(uuid)
  RETURNS tblvmeasurementmetacache AS
$BODY$
DECLARE
   vmid ALIAS FOR $1;
   col_op varchar;
   vmresult tblVMeasurementResult%ROWTYPE;
   ret tblVMeasurementMetaCache%ROWTYPE;
BEGIN

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

   SELECT COUNT(*) INTO ret.ReadingCount
      FROM tblVMeasurementReadingResult WHERE VMeasurementResultID = vmresult.VMeasurementResultID;

   SELECT FIRST(tlkpVMeasurementOp.Name), COUNT(tblVMeasurementGroup.VMeasurementID) 
      INTO col_op, ret.MeasurementCount
      FROM tblVMeasurement 
      INNER JOIN tlkpVMeasurementOp ON tblVMeasurement.VMeasurementOpID = tlkpVMeasurementOp.VMeasurementOpID 
      LEFT JOIN tblVMeasurementGroup ON tblVMeasurement.VMeasurementID = tblVMeasurementGroup.VMeasurementID 
      WHERE tblVMeasurement.VMeasurementID = vmid;

   IF col_op = 'Direct' THEN
      ret.MeasurementCount := 1;
   END IF;

   DELETE FROM tblVMeasurementMetaCache WHERE VMeasurementID = vmid;
   INSERT INTO tblVMeasurementMetaCache(VMeasurementID, StartYear, ReadingCount, MeasurementCount, DatingTypeID)
      VALUES (ret.VMeasurementID, ret.StartYear, ret.ReadingCount, ret.MeasurementCount, ret.datingTypeID);

   DELETE FROM tblVMeasurementResult WHERE VMeasurementResultID = vmresult.VMeasurementResultID;

   DELETE FROM tblVMeasurementDerivedCache WHERE VMeasurementID = vmid;
   INSERT INTO tblVMeasurementDerivedCache(VMeasurementID,MeasurementID) 
      SELECT vmid,Measurement.MeasurementID FROM cpgdb.FindVMParentMeasurements(vmid) Measurement;

   SELECT setsrid(extent(tblelement.locationgeometry)::geometry,4326)
      INTO  ret.vmextent
      FROM  tblelement, tblsample, tblradius, tblMeasurement, tblvmeasurement
      WHERE tblvmeasurement.measurementid=tblmeasurement.measurementid
      AND   tblmeasurement.radiusid=tblradius.radiusid
      AND   tblradius.sampleid=tblsample.sampleid
      AND   tblsample.elementid=tblelement.elementid
      AND   tblvmeasurement.vmeasurementid
            IN (SELECT vMeasurementid
                   FROM  cpgdb.FindVMParents(vmid, true)
                   WHERE col_op='Direct');


   UPDATE tblVMeasurementMetaCache SET vmextent = ret.vmextent WHERE VMeasurementID = ret.VMeasurementID;

   SELECT INTO ret.objectCode, ret.objectCount, ret.CommonTaxonName, ret.taxonCount, ret.prefix 
       s.objectCode,s.objectCount,s.commonTaxonName,s.taxonCount,cpgdb.GetVMeasurementPrefix(vmid) AS prefix
       FROM cpgdb.getVMeasurementSummaryInfo(vmid) AS s;
   UPDATE tblVMeasurementMetaCache SET (objectCode, objectCount, commonTaxonName, taxonCount, prefix) =
       (ret.objectCode, ret.objectCount, ret.CommonTaxonName, ret.taxonCount, ret.prefix)
       WHERE VMeasurementID = vmid;

   RETURN ret;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;



-- INSTALL pljava updates
UPDATE sqlj.jar_repository SET jarowner='corina';
SELECT sqlj.remove_jar('stupid_jar', false);
SELECT sqlj.remove_jar('indexing_jar', false);
SELECT sqlj.remove_jar('cpgdb_jar', false);
SELECT sqlj.install_jar('file:///usr/share/corina-server/corina-pljava.jar', 'corina_jar', false);
SELECT sqlj.set_classpath('cpgdb', 'corina_jar');


CREATE OR REPLACE FUNCTION cpgdb.purgecachetables()
  RETURNS void AS
$BODY$BEGIN
DELETE FROM tblvmeasurementresult;
DELETE FROM tblvmeasurementmetacache;
-- tblvmeasurementrelyearreadingnote?
-- tblvmeasurementreadingnoteresult?
-- tblvmeasurementreadingresult?
-- tblvmeasurementderivedcache?
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;




CREATE OR REPLACE FUNCTION cpgdb.rebuildmetacache()
  RETURNS void AS
$BODY$DECLARE
    uid uuid;
BEGIN
-- Empty cache tables first
PERFORM cpgdb.purgecachetables();
FOR uid IN SELECT vmeasurementid FROM tblvmeasurement
    LOOP
        -- Build metacache on row
        PERFORM cpgdb.createmetacache(uid);
    END LOOP;
RETURN;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;




CREATE OR REPLACE FUNCTION cpgdb.rebuildmetacacheforobject()
  RETURNS trigger AS
$BODY$DECLARE
  uid uuid;
BEGIN
FOR uid IN SELECT vmeasurementid FROM cpgdb.findchildrenof('Object', NEW.objectid)
  LOOP
    PERFORM cpgdb.createmetacache(uid);
  END LOOP;
RETURN new;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
CREATE TRIGGER update_object_rebuildmetacache
  AFTER INSERT OR UPDATE
  ON tblobject
  FOR EACH ROW
  EXECUTE PROCEDURE cpgdb.rebuildmetacacheforobject();



CREATE OR REPLACE FUNCTION cpgdb.rebuildmetacacheforelement()
  RETURNS trigger AS
$BODY$DECLARE
  uid uuid;
BEGIN
FOR uid IN SELECT vmeasurementid FROM cpgdb.findchildrenof('Element', NEW.elementid)
  LOOP
    PERFORM cpgdb.createmetacache(uid);
  END LOOP;
RETURN new;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
CREATE TRIGGER update_element_rebuildmetacache
  AFTER INSERT OR UPDATE
  ON tblelement
  FOR EACH ROW
  EXECUTE PROCEDURE cpgdb.rebuildmetacacheforelement();





CREATE OR REPLACE FUNCTION cpgdb.rebuildmetacacheforsample()
  RETURNS trigger AS
$BODY$DECLARE
  uid uuid;
BEGIN
FOR uid IN SELECT vmeasurementid FROM cpgdb.findchildrenof('Sample', NEW.sampleid)
  LOOP
    PERFORM cpgdb.createmetacache(uid);
  END LOOP;
RETURN new;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
CREATE TRIGGER update_sample_rebuildmetacache
  AFTER INSERT OR UPDATE
  ON tblsample
  FOR EACH ROW
  EXECUTE PROCEDURE cpgdb.rebuildmetacacheforsample();



CREATE OR REPLACE FUNCTION cpgdb.rebuildmetacacheforradius()
  RETURNS trigger AS
$BODY$DECLARE
  uid uuid;
BEGIN
FOR uid IN SELECT vmeasurementid FROM cpgdb.findchildrenof('Radius', NEW.radiusid)
  LOOP
    PERFORM cpgdb.createmetacache(uid);
  END LOOP;
RETURN new;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
CREATE TRIGGER update_radius_rebuildmetacache
  AFTER INSERT OR UPDATE
  ON tblradius
  FOR EACH ROW
  EXECUTE PROCEDURE cpgdb.rebuildmetacacheforradius();