CREATE OR REPLACE FUNCTION cpgdbj.applyderivedreadingnotes(character varying, character varying, character varying, character varying)
  RETURNS void AS
$BODY$
  SELECT cpgdbj.applyderivedreadingnotes($1::uuid, $2::uuid, $3::uuid, $4::uuid);
$BODY$
  LANGUAGE sql VOLATILE
  COST 100;

CREATE OR REPLACE FUNCTION cpgdbj.qacqvmeasurementreadingresult(IN paramcurrentvmeasurementresultid character varying, OUT relyear integer, OUT reading integer)
  RETURNS SETOF record AS
$BODY$
  SELECT cpgdbj.qacqvmeasurementreadingresult($1::uuid);
$BODY$
  LANGUAGE sql STABLE
  COST 100
  ROWS 1000;

CREATE OR REPLACE FUNCTION cpgdbj.qappvmeasurementreadingnoteresult(paramvmeasurementresultid character varying)
  RETURNS void AS
$BODY$
  SELECT cpgdbj.qappvmeasurementreadingnoteresult($1::uuid);
$BODY$
  LANGUAGE sql VOLATILE
  COST 100;


CREATE OR REPLACE FUNCTION cpgdbj.qappvmeasurementreadingresult(paramvmeasurementresultid character varying, parammeasurementid integer)
  RETURNS void AS
$BODY$
  SELECT cpgdbj.qappvmeasurementreadingresult($1::uuid, $2);
$BODY$
  LANGUAGE sql VOLATILE
  COST 100;


CREATE OR REPLACE FUNCTION cpgdbj.qappvmeasurementresult(paramvmeasurementresultid character varying, paramvmeasurementid character varying, paramvmeasurementresultgroupid character varying, paramvmeasurementresultmasterid character varying, owneruserid integer, parammeasurementid integer)
RETURNS void AS
$BODY$
  SELECT cpgdbj.qappvmeasurementresult($1::uuid, $2::uuid, $3::uuid, $4::uuid, $5, $6);
$BODY$
  LANGUAGE sql VOLATILE
  COST 100;


CREATE OR REPLACE FUNCTION cpgdbj.qappvmeasurementresultopindex(paramnewvmeasurementresultid character varying, paramvmeasurementid character varying, paramvmeasurementresultmasterid character varying, owneruserid integer, paramcurrentvmeasurementresultid character varying)
  RETURNS void AS
$BODY$
  SELECT cpgdbj.qappvmeasurementresultopindex($1::uuid, $2::uuid, $3::uuid, $4, $5::uuid);
$BODY$
  LANGUAGE sql VOLATILE
  COST 100;


CREATE OR REPLACE FUNCTION cpgdbj.qappvmeasurementresultopsum(paramnewvmeasurementresultid character varying, paramvmeasurementid character varying, paramvmeasurementresultmasterid character varying, owneruserid integer, paramvmeasurementresultgroupid character varying)
  RETURNS void AS
$BODY$
  SELECT cpgdbj.qappvmeasurementresultopsum($1::uuid, $2::uuid, $3::uuid, $4, $5::uuid);
$BODY$
  LANGUAGE sql VOLATILE
  COST 100;


CREATE OR REPLACE FUNCTION cpgdbj.qappvmeasurementresultreadingopsum(paramnewvmeasurementresultgroupid character varying, paramnewvmeasurementresultid character varying)
  RETURNS integer AS
$BODY$
  SELECT cpgdbj.qappvmeasurementresultreadingopsum($1::uuid, $2::uuid)
$BODY$
  LANGUAGE sql VOLATILE
  COST 100;


CREATE OR REPLACE FUNCTION cpgdbj.qdelvmeasurementresultremovemasterid(paramvmeasurementresultmasterid uuid, paramvmeasurementresultid uuid)
  RETURNS void AS
$BODY$
  SELECT cpgdbj.qdelvmeasurementresultremovemasterid($1::uuid, $2::uuid);
$BODY$
  LANGUAGE sql VOLATILE
  COST 100;


CREATE OR REPLACE FUNCTION cpgdbj.qryvmeasurementmembers(IN paramvmeasurementid uuid, OUT vmeasurementid uuid, OUT membervmeasurementid uuid)
  RETURNS SETOF record AS
$BODY$
  SELECT cpgdbj.qryvmeasurementmembers($1::uuid);
$BODY$
  LANGUAGE sql STABLE
  COST 100
  ROWS 1000;


CREATE OR REPLACE FUNCTION cpgdbj.qryvmeasurementtype(IN vmeasurementid character varying, OUT vmeasurementid uuid, OUT op text, OUT vmeasurementsingroup bigint, OUT measurementid integer, OUT vmeasurementopparameter integer)
  RETURNS record AS
$BODY$
  SELECT cpgdbj.qryvmeasurementtype($1::uuid);
$BODY$
  LANGUAGE sql STABLE
  COST 100;

CREATE OR REPLACE FUNCTION cpgdbj.qupdvmeasurementresultattachgroupid(paramvmeasurementresultgroupid character varying, paramvmeasurementresultid character varying)
  RETURNS void AS
$BODY$
  SELECT cpgdbj.qupdvmeasurementresultattachgroupid($1::uuid, $2::uuid);
$BODY$
  LANGUAGE sql VOLATILE
  COST 100;


CREATE OR REPLACE FUNCTION cpgdbj.qupdvmeasurementresultcleargroupid(paramvmeasurementresultgroupid character varying)
  RETURNS void AS
$BODY$
  SELECT cpgdbj.qupdvmeasurementresultcleargroupid($1::uuid);
$BODY$
  LANGUAGE sql VOLATILE
  COST 100;


CREATE OR REPLACE FUNCTION cpgdbj.qupdvmeasurementresultinfo(idval character varying)
  RETURNS boolean AS
$BODY$
  SELECT cpgdbj.qupdvmeasurementresultinfo($1::uuid);
$BODY$
  LANGUAGE sql VOLATILE
  COST 100;


CREATE OR REPLACE FUNCTION cpgdbj.qupdvmeasurementresultopclean(paramvmeasurementid character varying, paramcurrentvmeasurementresultid character varying)
  RETURNS void AS
$BODY$
  SELECT cpgdbj.qupdvmeasurementresultopclean($1::uuid, $2::uuid);
$BODY$
  LANGUAGE sql VOLATILE
  COST 100;


CREATE OR REPLACE FUNCTION cpgdbj.qupdvmeasurementresultopcrossdate(paramvmeasurementid character varying, paramcurrentvmeasurementresultid character varying)
  RETURNS void AS
$BODY$
  SELECT cpgdbj.qupdvmeasurementresultopcrossdate($1::uuid, $2::uuid);
$BODY$
  LANGUAGE sql VOLATILE
  COST 100;


CREATE OR REPLACE FUNCTION cpgdbj.qupdvmeasurementresultopredate(paramvmeasurementid character varying, paramcurrentvmeasurementresultid character varying)
  RETURNS void AS
$BODY$
  SELECT cpgdbj.qupdvmeasurementresultopredate($1::uuid, $2::uuid);
$BODY$
  LANGUAGE sql VOLATILE
  COST 100;


CREATE OR REPLACE FUNCTION cpgdbj.qupdvmeasurementresultoptruncate(paramvmeasurementid character varying, paramvmeasurementresultid character varying)
  RETURNS void AS
$BODY$
  SELECT cpgdbj.qupdvmeasurementresultoptruncate($1::uuid, $2::uuid);
$BODY$
  LANGUAGE sql VOLATILE
  COST 100;



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



CREATE FUNCTION text_to_uuid(text) RETURNS uuid AS 'SELECT CAST($1 AS uuid)' LANGUAGE SQL IMMUTABLE;

CREATE CAST (text AS uuid) WITH FUNCTION text_to_uuid(text) AS IMPLICIT;




-- INSTALL pljava updates
UPDATE sqlj.jar_repository SET jarowner='corina';
SELECT sqlj.remove_jar('stupid_jar', false);
SELECT sqlj.remove_jar('indexing_jar', false);
SELECT sqlj.remove_jar('cpgdb_jar', false);
SELECT sqlj.install_jar('file:///usr/share/corina-server/corina-pljava.jar', 'corina_jar', false);
SELECT sqlj.set_classpath('cpgdb', 'corina_jar');


