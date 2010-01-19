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

badrad RECORD;
matchCount integer;
goodradiusid uuid;
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


-- Before we go any further we need to check that any associated radii don't need merging first
FOR badrad IN SELECT * FROM tblradius WHERE sampleid=badsampleid ORDER BY code LOOP 
   SELECT radiusid INTO goodradiusid FROM tblradius WHERE sampleid=goodsampleid AND code=badrad.code;
   IF goodradiusid IS NOT NULL THEN
       RAISE NOTICE 'Recursing into mergeRadii() as mergeSample() will cause conflict with radius % (code %)', 
                     badrad.radiusid, badrad.code; 
       PERFORM cpgdb.mergeradii(goodradiusid, badrad.radiusid);
   ELSE
       RAISE NOTICE 'No conflict here so just go ahead a relink radius to new sample';
       UPDATE tblradius SET sampleid=goodsampleid WHERE radiusid=badrad.radiusid;
   END IF;
END LOOP;


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
   commentstr:= commentstr || 'File links differ (other record = ' || badsample.file::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- Position
IF goodsample.position!=badsample.position THEN
   commentstr:= commentstr || 'Position of sample differs (other record = ' || badsample.position::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- State
IF goodsample.state!=badsample.state THEN
   commentstr:= commentstr || 'State of sample differs (other record = ' || badsample.state::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- Knots
IF goodsample.knots!=badsample.knots THEN
   commentstr:= commentstr || 'Presence of knots differs (other record = ' || badsample.knots::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- Description differs
IF goodsample.description!=badsample.description THEN
   commentstr:= commentstr || 'Description differs (other record = ''' || badsample.description::varchar || '''). ';
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

-- Box
IF goodsample.comments!=badsample.comments THEN
   commentstr:= commentstr || 'Comments differ (other record = ' || badsample.comments::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;


IF discrepancycount=1 THEN
  RAISE NOTICE '1 discrepancy detected when merging samples';
ELSIF discrepancycount > 1 THEN
  RAISE NOTICE '% discrepancies detected when merging samples', discrepancycount;
ELSE 
--  RAISE NOTICE 'No discrepancies detected';
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
