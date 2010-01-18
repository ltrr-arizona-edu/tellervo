-- Function: cpgdb.graftseries(uuid, uuid, cpgdb.graftpoint)

-- DROP FUNCTION cpgdb.graftseries(uuid, uuid, cpgdb.graftpoint);

CREATE OR REPLACE FUNCTION cpgdb.graftseries(goodseriesid uuid, badseriesid uuid, graftpoint cpgdb.graftpoint)
  RETURNS vwcomprehensivevm AS
$BODY$DECLARE
gooduuids cpgdb.typfulluuids%ROWTYPE;
goodseries vwcomprehensivevm%ROWTYPE;
goodseriesid ALIAS FOR $1;

baduuids cpgdb.typfulluuids%ROWTYPE;
badseries vwcomprehensivevm%ROWTYPE;
badseriesid ALIAS FOR $2;

graftpoint ALIAS FOR $3;

BEGIN

-- Get the two series in question
SELECT * INTO goodseries FROM vwcomprehensivevm WHERE vmeasurementid=goodseriesid;
IF NOT FOUND THEN
   RAISE NOTICE 'The good series with id % was not found', goodseriesid;
   RETURN NULL;
END IF;
SELECT * INTO badseries FROM vwcomprehensivevm WHERE vmeasurementid=badseriesid;
IF NOT FOUND THEN
   RAISE NOTICE 'The bad series with id % was not found', badseriesid;
   RETURN NULL;
END IF;

-- Get good and bad id's for all entities
SELECT e.elementid, s.sampleid, r.radiusid, vm.vmeasurementid as seriesid INTO gooduuids
FROM tblvmeasurement vm 
LEFT JOIN tblmeasurement m ON vm.measurementid=m.measurementid
LEFT JOIN tblradius r ON m.radiusid=r.radiusid
LEFT JOIN tblsample s ON r.sampleid = s.sampleid
LEFT JOIN tblelement e ON s.elementid = e.elementid
WHERE vm.vmeasurementid=goodseriesid;
SELECT e.elementid, s.sampleid, r.radiusid, vm.vmeasurementid as seriesid INTO baduuids
FROM tblvmeasurement vm 
LEFT JOIN tblmeasurement m ON vm.measurementid=m.measurementid
LEFT JOIN tblradius r ON m.radiusid=r.radiusid
LEFT JOIN tblsample s ON r.sampleid = s.sampleid
LEFT JOIN tblelement e ON s.elementid = e.elementid
WHERE vm.vmeasurementid=badseriesid;

RAISE NOTICE 'Grafting series % at %', badseriesid, graftpoint;
RAISE NOTICE 'Bad  series: elementid = %', baduuids.elementid;
RAISE NOTICE 'Bad  series: sampleid  = %', baduuids.sampleid;
RAISE NOTICE 'Bad  series: radiusid  = %', baduuids.radiusid;
RAISE NOTICE 'Bad  series: seriesid  = %', baduuids.seriesid;
RAISE NOTICE 'Good series: elementid = %', gooduuids.elementid;
RAISE NOTICE 'Good series: sampleid  = %', gooduuids.sampleid;
RAISE NOTICE 'Good series: radiusid  = %', gooduuids.radiusid;
RAISE NOTICE 'Good series: seriesid  = %', gooduuids.seriesid;

-- Cascade through relevant functions 
IF ((graftpoint='radius'::cpgdb.graftpoint) OR (graftpoint='sample') OR (graftpoint='element')) THEN
   PERFORM cpgdb.mergeradii(gooduuids.radiusid, baduuids.radiusid);
END IF;
IF (graftpoint='sample' OR graftpoint='element') THEN
   PERFORM cpgdb.mergesamples(gooduuids.sampleid, baduuids.sampleid);
END IF;
IF (graftpoint='element') THEN
   PERFORM cpgdb.mergeelements(gooduuids.elementid, baduuids.elementid);
END IF;

SELECT * INTO goodseries FROM vwcomprehensivevm WHERE vmeasurementid=goodseriesid;

RETURN goodseries;
END;$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.graftseries(uuid, uuid, cpgdb.graftpoint) OWNER TO aps03pwb;


-- Function: cpgdb.mergeelements(uuid, uuid)

-- DROP FUNCTION cpgdb.mergeelements(uuid, uuid);

CREATE OR REPLACE FUNCTION cpgdb.mergeelements(goodelementid uuid, badelementid uuid)
  RETURNS tblelement AS
$BODY$DECLARE
goodelementid ALIAS FOR $1;
badelementid ALIAS FOR $2;
goodelement tblelement%ROWTYPE;
badelement tblelement%ROWTYPE;
commentstr varchar;
lookup varchar;
discrepstr varchar;
discrepancycount integer;
BEGIN

-- Initialize vars
discrepancycount:=0;
commentstr:= '';
discrepstr:='*** Another record was automatically merged with this one and discrepancies were detected.  Please check the following issues: ';

-- Get the two samples in question
SELECT * INTO goodelement FROM tblelement WHERE elementid=goodelementid;
IF NOT FOUND THEN
   RAISE NOTICE 'The good element with id % was not found', goodelementid;
   RETURN NULL;
END IF;
SELECT * INTO badelement FROM tblelement WHERE elementid=badelementid;
IF NOT FOUND THEN
   RAISE NOTICE 'The bad element with id % was not found', badelementid;
   RETURN NULL;
END IF;

-- Check elements are different
IF goodelement.elementid=badelement.elementid THEN 
   RAISE NOTICE 'Elements are the same!';
   RETURN goodelement;
END IF;

-- Update sample records relinking to the good element where applicable
UPDATE tblsample SET elementid=goodelementid WHERE elementid=badelementid;


-- COMPARE EACH FIELD TO CHECK FOR DISPARITIES

-- Taxon
IF goodelement.taxonid!=badelement.taxonid THEN
   SELECT label INTO lookup FROM tlkptaxon WHERE taxonid=badelement.taxonid;
   commentstr:= commentstr || 'Taxon differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- Location precision 
IF goodelement.locationprecision!=badelement.locationprecision THEN
   commentstr:= commentstr || 'Location precision differs (other record = ' || badelement.locationprecision::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

--locationgeometry
-- islivetree
-- originaltaxonname
-- locationtypeid
-- locationcomment
-- file
-- description 
-- processing
-- marks
-- diameter
-- width
-- height
-- depth
-- units
-- elementtypeid
-- elementauthenticityid
-- elementshapeid
-- altitude
-- slopeangle
-- slopeazimuth
-- soildescription
-- soildepth
-- bedrockdescription


IF discrepancycount>0 THEN
  IF goodelement.comments IS NOT NULL THEN
    --RAISE NOTICE 'Adding discrepency description to comments field';
    UPDATE tblelement SET comments=goodelement.comments || discrepstr || commentstr || '***' WHERE elementid=goodelementid;
  ELSE
    --RAISE NOTICE 'Putting discrepency description in comments field';
    UPDATE tblelement SET comments=discrepstr || commentstr || '***' WHERE elementid=goodelementid;
  END IF;
END IF;

-- Get updated element info
SELECT * INTO goodelement FROM tblelement WHERE elementid=goodelementid;

-- Delete old element record
DELETE FROM tblelement WHERE elementid=badelementid;

RETURN goodelement;
END;$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.mergeelements(uuid, uuid) OWNER TO aps03pwb;

-- Function: cpgdb.mergeradii(uuid, uuid)

-- DROP FUNCTION cpgdb.mergeradii(uuid, uuid);

CREATE OR REPLACE FUNCTION cpgdb.mergeradii(goodradiusid uuid, badradiusid uuid)
  RETURNS tblradius AS
$BODY$DECLARE
goodradiusid ALIAS FOR $1;
badradiusid ALIAS FOR $2;
goodradius tblradius%ROWTYPE;
badradius tblradius%ROWTYPE;
commentstr varchar;
lookup varchar;
discrepstr varchar;
discrepancycount integer;
BEGIN

-- Initialize vars
discrepancycount:=0;
commentstr:= '';
discrepstr:='*** Another record was automatically merged with this one and discrepancies were detected.  Please check the following issues: ';

-- Get the two radii in question
SELECT * INTO goodradius FROM tblradius WHERE radiusid=goodradiusid;
IF NOT FOUND THEN
   RAISE EXCEPTION 'The good radius with id % was not found', goodradiusid;
   RETURN NULL;
END IF;
SELECT * INTO badradius FROM tblradius WHERE radiusid=badradiusid;
IF NOT FOUND THEN
   RAISE EXCEPTION 'The bad radius with id % was not found', badradiusid;
   RETURN NULL;
END IF;

-- Check radii are different
IF goodradius.radiusid=badradius.radiusid THEN 
   RAISE NOTICE 'Radii are the same!';
   RETURN goodradius;
END IF;

-- Update measurement records relinking to the good radius where applicable
UPDATE tblmeasurement SET radiusid=goodradiusid WHERE radiusid=badradiusid;

-- COMPARE EACH FIELD TO CHECK FOR DISPARITIES

-- Sapwood count
IF goodradius.numberofsapwoodrings!=badradius.numberofsapwoodrings THEN
   commentstr:= commentstr || 'Sapwood rings differ (other record = ' || badradius.numberofsapwoodrings::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- Pith id
IF goodradius.pithid!=badradius.pithid THEN
   SELECT complexpresenceabsence INTO lookup FROM tlkpcomplexpresenceabsence WHERE complexpresenceabsenceid=badradius.pithid;
   commentstr:= commentstr || 'Pith presence differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- Bark present
IF goodradius.barkpresent!=badradius.barkpresent THEN
   commentstr:= commentstr || 'Bark presence differs (other record = ' || badradius.barkpresent::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- lastringunderbark
IF goodradius.lastringunderbark!=badradius.lastringunderbark THEN
   commentstr:= commentstr || 'Last ring under bark differs (other record = ' || badradius.lastringunderbark::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- missingheartwoodringstopith
IF goodradius.missingheartwoodringstopith!=badradius.missingheartwoodringstopith THEN
   commentstr:= commentstr || 'Missing heartwood rings differ (other record = ' || badradius.missingheartwoodringstopith::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- missingsapwoodringstobark
IF goodradius.missingsapwoodringstobark!=badradius.missingsapwoodringstobark THEN
   commentstr:= commentstr || 'Missing sapwood rings differ (other record = ' || badradius.missingsapwoodringstobark::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- missingheartwoodringstopithfoundation
IF goodradius.missingheartwoodringstopithfoundation!=badradius.missingheartwoodringstopithfoundation THEN
   commentstr:= commentstr || 'Missing heartwood rings foundation differs (other record = ' || badradius.missingheartwoodringstopithfoundation::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- missingsapwoodringstobarkfoundation
IF goodradius.missingsapwoodringstobarkfoundation!=badradius.missingsapwoodringstobarkfoundation THEN
   commentstr:= commentstr || 'Missing sapwood rings foundation differs (other record = ' || badradius.missingsapwoodringstobarkfoundation::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- Sapwood id
IF goodradius.sapwoodid!=badradius.sapwoodid THEN
   SELECT complexpresenceabsence INTO lookup FROM tlkpcomplexpresenceabsence WHERE complexpresenceabsenceid=badradius.sapwoodid;
   commentstr:= commentstr || 'Sapwood presence differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- Heartwood id
IF goodradius.heartwoodid!=badradius.heartwoodid THEN
   SELECT complexpresenceabsence INTO lookup FROM tlkpcomplexpresenceabsence WHERE complexpresenceabsenceid=badradius.heartwoodid;
   commentstr:= commentstr || 'Heartwood presence differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- Azimuth
IF goodradius.azimuth!=badradius.azimuth THEN
   commentstr:= commentstr || 'Azimuth differs (other record = ' || badradius.azimuth::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;


IF discrepancycount=1 THEN
  RAISE NOTICE '1 discrepancy detected';
ELSIF discrepancycount > 1 THEN
  RAISE NOTICE '% discrepancies detected', discrepancycount;
ELSE 
--  RAISE NOTICE 'No discrepancies detected';
END IF;

IF discrepancycount>0 THEN
  IF goodradius.comments IS NOT NULL THEN
    UPDATE tblradius SET comments=goodradius.comments || discrepstr || commentstr || '***' WHERE radiusid=goodradiusid;
  ELSE
    UPDATE tblradius SET comments=discrepstr || commentstr || '***' WHERE radiusid=goodradiusid;
  END IF;
END IF;

-- Get updated radius info
SELECT * INTO goodradius FROM tblradius WHERE radiusid=goodradiusid;

-- Delete old radius record
DELETE FROM tblradius WHERE radiusid=badradiusid;

RETURN goodradius;
END;$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.mergeradii(uuid, uuid) OWNER TO aps03pwb;
COMMENT ON FUNCTION cpgdb.mergeradii(uuid, uuid) IS 'This function merges a ''bad'' radius with a ''good'' radius.  References to the bad radius are changed to point to the new radius, any discrepancies between fields are marked in the comments field and then the ''bad'' radius is deleted.';

-- Function: cpgdb.mergesamples(uuid, uuid)

-- DROP FUNCTION cpgdb.mergesamples(uuid, uuid);

CREATE OR REPLACE FUNCTION cpgdb.mergesamples(goodsample uuid, badsample uuid)
  RETURNS tblsample AS
$BODY$DECLARE
goodsampleid ALIAS FOR $1;
badsampleid ALIAS FOR $2;
goodsample tblsample%ROWTYPE;
badsample tblsample%ROWTYPE;
commentstr varchar;
lookup varchar;
discrepstr varchar;
discrepancycount integer;
BEGIN

-- Initialize vars
discrepancycount:=0;
commentstr:= '';
discrepstr:='*** Another record was automatically merged with this one and discrepancies were detected.  Please check the following issues: ';

-- Get the two samples in question
SELECT * INTO goodsample FROM tblsample WHERE sampleid=goodsampleid;
IF NOT FOUND THEN
   RAISE NOTICE 'The good sample with id % was not found', goodsampleid;
   RETURN NULL;
END IF;
SELECT * INTO badsample FROM tblsample WHERE sampleid=badsampleid;
IF NOT FOUND THEN
   RAISE NOTICE 'The bad sample with id % was not found', badsampleid;
   RETURN NULL;
END IF;

-- Check samples are different
IF goodsample.sampleid=badsample.sampleid THEN 
   RAISE NOTICE 'Samples are the same!';
   RETURN goodsample;
END IF;


-- Update radius records relinking to the good sample where applicable
UPDATE tblradius SET sampleid=goodsampleid WHERE sampleid=badsampleid;


-- COMPARE EACH FIELD TO CHECK FOR DISPARITIES

-- Sampling date
IF goodsample.samplingdate!=badsample.samplingdate THEN
   commentstr:= commentstr || 'Sampling date differs (other record = ' || badsample.samplingdate::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- Identifier domain
IF goodsample.identifierdomain!=badsample.identifierdomain THEN
   commentstr:= commentstr || 'Identifier domain differs (other record = ' || badsample.identifierdomain::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- File
IF goodsample.file!=badsample.file THEN
   commentstr:= commentstr || 'File links differ (other record = ' || badsample.file::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- Position
IF goodsample.position!=badsample.position THEN
   commentstr:= commentstr || 'Position of sample differs (other record = ' || badsample.position::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- State
IF goodsample.state!=badsample.state THEN
   commentstr:= commentstr || 'State of sample differs (other record = ' || badsample.state::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- Knots
IF goodsample.knots!=badsample.knots THEN
   commentstr:= commentstr || 'Presence of knots differs (other record = ' || badsample.knots::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- Description differs
IF goodsample.description!=badsample.description THEN
   commentstr:= commentstr || 'Description differs (other record = ''' || badsample.description::varchar || ''')';
   discrepancycount:=discrepancycount+1;
END IF;

-- Date certainty
IF goodsample.datecertaintyid!=badsample.datecertaintyid THEN
   SELECT datecertainty INTO lookup FROM tlkpdatecertainty WHERE datecertaintyid=badsample.datecertaintyid;
   commentstr:= commentstr || 'Date certainty differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- Type
IF goodsample.typeid!=badsample.typeid THEN
   SELECT sampletype INTO lookup FROM tlkpsampletype WHERE sampletypeid=badsample.typeid;
   commentstr:= commentstr || 'Sample type differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- Box
IF goodsample.boxid!=badsample.boxid THEN
   SELECT title INTO lookup FROM tblbox WHERE boxid=badsample.boxid;
   commentstr:= commentstr || 'Box differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;


IF discrepancycount>0 THEN
  IF goodsample.comments IS NOT NULL THEN
    --RAISE NOTICE 'Adding discrepency description to comments field';
    UPDATE tblsample SET comments=goodsample.comments || discrepstr || commentstr || '***' WHERE sampleid=goodsampleid;
  ELSE
    --RAISE NOTICE 'Putting discrepency description in comments field';
    UPDATE tblsample SET comments=discrepstr || commentstr || '***' WHERE sampleid=goodsampleid;
  END IF;
END IF;

-- Get updated sample info
SELECT * INTO goodsample FROM tblsample WHERE sampleid=goodsampleid;

-- Delete old sample record
DELETE FROM tblsample WHERE sampleid=badsampleid;

RETURN goodsample;
END;$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.mergesamples(uuid, uuid) OWNER TO aps03pwb;


-- Function: cpgdb.getallentityuuids(uuid)

-- DROP FUNCTION cpgdb.getallentityuuids(uuid);

CREATE OR REPLACE FUNCTION cpgdb.getallentityuuids(seriesuuid uuid)
  RETURNS cpgdb.typfulluuids AS
$BODY$DECLARE
seriesuuid ALIAS FOR $1;
uuids cpgdb.typfulluuids;
BEGIN

SELECT e.elementid, s.sampleid, r.radiusid, vm.vmeasurementid as seriesid INTO uuids
FROM tblvmeasurement vm 
LEFT JOIN tblmeasurement m ON vm.measurementid=m.measurementid
LEFT JOIN tblradius r ON m.radiusid=r.radiusid
LEFT JOIN tblsample s ON r.sampleid = s.sampleid
LEFT JOIN tblelement e ON s.elementid = e.elementid
WHERE vm.vmeasurementid=seriesuuid;

RETURN uuids;
END;$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.getallentityuuids(uuid) OWNER TO aps03pwb;

create type cpgdb.graftpoint as ENUM ('element', 'sample', 'radius');
create type cpgdb.typfulluuids as (elementid uuid, sampleid uuid, radiusid uuid, seriesid uuid);

