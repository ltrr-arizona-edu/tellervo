-- Function: cpgdb.mergeelements(uuid, uuid)

-- DROP FUNCTION cpgdb.mergeelements(uuid, uuid);

CREATE OR REPLACE FUNCTION cpgdb.mergeelements(goodelementid uuid, badelementid uuid)
  RETURNS tblelement AS
$BODY$DECLARE

goodelementid ALIAS FOR $1;      -- UUID of the element with correct entities
goodelement tblelement%ROWTYPE;  -- The series itself
badelementid ALIAS FOR $2;       -- UUID of the element with incorrect entities
badelement tblelement%ROWTYPE;   -- The series itself
commentstr varchar;              -- Used to compile comments for indicating discrepancies 
discrepstr varchar;              -- Basic comment to highlight discrepancies
discrepancycount integer;        -- Count of number of fields that aren't the same

lookup varchar;                  -- Temporary variable for storing values from lookup tables
badsamp RECORD;                  -- Temporary variable for storing associated samples
goodsampleid uuid;               -- Temporary variable for storing uuid of good associated sample
BEGIN

-- Initialize vars
discrepancycount:=0;
commentstr:= '';
discrepstr:='*** Another record was automatically merged with this one and discrepancies were detected.  Please check the following issues: ';

-- Get the two elements in question
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

-- Before we go any further we need to check that any associated samples don't need merging first
FOR badsamp IN SELECT * FROM tblsample WHERE elementid=badelementid ORDER BY code LOOP 
   SELECT sampleid INTO goodsampleid FROM tblsample WHERE elementid=goodelementid AND code=badsamp.code;
   IF goodsampleid IS NOT NULL THEN
       --RAISE NOTICE 'Recursing into mergeSample() as mergeElement() will cause conflict with sample % (code %)', badsamp.sampleid, badsamp.code; 
       PERFORM cpgdb.mergesamples(goodsampleid, badsamp.sampleid);
   ELSE
       --RAISE NOTICE 'No conflict here so just go ahead a relink sample to new element';
       UPDATE tblsample SET elementid=goodelementid WHERE sampleid=badsamp.sampleid;
   END IF;
END LOOP;

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
IF goodelement.locationgeometry!=badelement.locationgeometry THEN
   SELECT 'Long: ' || x(locationgeometry) || ', Lat: ' ||  y(locationgeometry) INTO lookup FROM tblelement WHERE elementid=badelement.elementid;   
   commentstr:= commentstr || 'Location geometry differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- locationtypeid
IF goodelement.locationtypeid!=badelement.locationtypeid THEN
   SELECT locationtype INTO lookup FROM tlkplocationtype WHERE locationtypeid=badelement.locationtypeid;
   commentstr:= commentstr || 'Location type differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- locationcomment
IF goodelement.locationcomment!=badelement.locationcomment THEN
   commentstr:= commentstr || 'Location comments differ (other record = ' || badelement.locationcomment::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- file
IF goodelement.file!=badelement.file THEN
   commentstr:= commentstr || 'File links differ (other record = ' || badelement.file::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- description
IF goodelement.description!=badelement.description THEN
   commentstr:= commentstr || 'Description differs (other record = ' || badelement.file::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;
 
-- processing
IF goodelement.processing!=badelement.processing THEN
   commentstr:= commentstr || 'Processing field differs (other record = ' || badelement.processing::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- marks
IF goodelement.marks!=badelement.marks THEN
   commentstr:= commentstr || 'Marks field differs (other record = ' || badelement.marks::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- diameter
IF goodelement.diameter!=badelement.diameter THEN
   commentstr:= commentstr || 'Diameter differs (other record = ' || badelement.diameter::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- width
IF goodelement.width!=badelement.width THEN
   commentstr:= commentstr || 'Width differs (other record = ' || badelement.width::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- height
IF goodelement.height!=badelement.height THEN
   commentstr:= commentstr || 'Height differs (other record = ' || badelement.height::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- depth
IF goodelement.depth!=badelement.depth THEN
   commentstr:= commentstr || 'Depth differs (other record = ' || badelement.depth::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- units
IF goodelement.units!=badelement.units THEN
   commentstr:= commentstr || 'Units differ (other record = ' || badelement.units::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- elementtypeid
IF goodelement.elementtypeid!=badelement.elementtypeid THEN
   SELECT elementtype INTO lookup FROM tlkpelementtype WHERE elementtypeid=badelement.elementtypeid;
   commentstr:= commentstr || 'Element type differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- elementauthenticityid
IF goodelement.elementauthenticityid!=badelement.elementauthenticityid THEN
   SELECT elementauthenticity INTO lookup FROM tlkpelementauthenticity WHERE elementauthenticityid=badelement.elementauthenticityid;
   commentstr:= commentstr || 'Element authenticity differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- elementshapeid
IF goodelement.elementshapeid!=badelement.elementshapeid THEN
   SELECT elementshape INTO lookup FROM tlkpelementshape WHERE elementshapeid=badelement.elementshapeid;
   commentstr:= commentstr || 'Element shape differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- altitude
IF goodelement.altitude!=badelement.altitude THEN
   commentstr:= commentstr || 'Altitude differs (other record = ' || badelement.altitude::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- slopeangle
IF goodelement.slopeangle!=badelement.slopeangle THEN
   commentstr:= commentstr || 'Slope angle differs (other record = ' || badelement.slopeangle::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- slopeazimuth
IF goodelement.slopeazimuth!=badelement.slopeazimuth THEN
   commentstr:= commentstr || 'Slope azimuth differs (other record = ' || badelement.slopeazimuth::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- soildescription
IF goodelement.soildescription!=badelement.soildescription THEN
   commentstr:= commentstr || 'Soil description differs (other record = ' || badelement.soildescription::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- soildepth
IF goodelement.soildepth!=badelement.soildepth THEN
   commentstr:= commentstr || 'Soil depth differs (other record = ' || badelement.soildepth::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- bedrockdescription
IF goodelement.bedrockdescription!=badelement.bedrockdescription THEN
   commentstr:= commentstr || 'Bedrock description differs (other record = ' || badelement.bedrockdescription::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- Show notices for any discrepancies
--IF discrepancycount=1 THEN
--  RAISE NOTICE '1 discrepancy detected when merging elements';
--ELSIF discrepancycount > 1 THEN
--  RAISE NOTICE '% discrepancies detected when merging elements', discrepancycount;
--ELSE 
--  RAISE NOTICE 'No discrepancies detected';
--END IF;

-- If there are discrepancies either set or update the comments field to highlight issues
IF discrepancycount>0 THEN
  IF goodelement.comments IS NOT NULL THEN
    --RAISE NOTICE 'Adding discrepency description to comments field';
    UPDATE tblelement SET comments=goodelement.comments || discrepstr || commentstr || '***' WHERE elementid=goodelementid;
  ELSE
    --RAISE NOTICE 'Putting discrepency description in comments field';
    UPDATE tblelement SET comments=discrepstr || commentstr || '***' WHERE elementid=goodelementid;
  END IF;
END IF;

-- Delete old element record
DELETE FROM tblelement WHERE elementid=badelementid;

-- Get updated element info and return it
SELECT * INTO goodelement FROM tblelement WHERE elementid=goodelementid;
RETURN goodelement;
END;$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.mergeelements(uuid, uuid) OWNER TO aps03pwb;
