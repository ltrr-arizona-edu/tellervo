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

-- Comments
IF goodradius.comments!=badradius.comments THEN
   commentstr:= commentstr || 'Comments differ (other record = ' || badradius.comments::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

IF discrepancycount=1 THEN
  RAISE NOTICE '1 discrepancy detected when merging radii';
ELSIF discrepancycount > 1 THEN
  RAISE NOTICE '% discrepancies detected when mergin radii', discrepancycount;
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
