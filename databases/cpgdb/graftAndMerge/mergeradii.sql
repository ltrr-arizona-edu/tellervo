-- Function: cpgdb.mergeradii(uuid, uuid)

-- DROP FUNCTION cpgdb.mergeradii(uuid, uuid);

CREATE OR REPLACE FUNCTION cpgdb.mergeradii(goodradiusid uuid, badradiusid uuid)
  RETURNS tblradius AS
$BODY$DECLARE
goodradiusid ALIAS FOR $1;    -- ID of radius with correct entities
goodradius tblradius%ROWTYPE; -- The radius itself
badradiusid ALIAS FOR $2;     -- ID of radius with incorrect entities
badradius tblradius%ROWTYPE;  -- The radius itself
commentstr varchar;           -- Used to compile comments for indicating discrepancies 
lookup varchar;               -- Temporary variable for storing values from lookup tables
discrepstr varchar;           -- Basic comment to highlight discrepancies
discrepancycount integer;     -- Count of number of fields that aren't the same
doupdate boolean;             -- Temporary variable for flagging whether to update this record

BEGIN

-- Initialize vars
discrepancycount:=0;
commentstr:= '';
discrepstr:='*** Another record was automatically merged with this one and discrepancies were detected.  Please check the following issues: ';
doupdate:=false;

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
IF goodradius.numberofsapwoodrings IS NULL THEN
   goodradius.numberofsapwoodrings=badradius.numberofsapwoodrings;
   doupdate:=true;
END IF;
IF goodradius.numberofsapwoodrings!=badradius.numberofsapwoodrings THEN
   commentstr:= commentstr || 'Sapwood rings differ (other record = ' || badradius.numberofsapwoodrings::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- Pith id
IF goodradius.pithid IS NULL THEN
   goodradius.pithid=badradius.pithid;
   doupdate:=true;
END IF;
IF goodradius.pithid!=badradius.pithid THEN
   SELECT complexpresenceabsence INTO lookup FROM tlkpcomplexpresenceabsence WHERE complexpresenceabsenceid=badradius.pithid;
   commentstr:= commentstr || 'Pith presence differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- Bark present
IF goodradius.barkpresent IS NULL THEN
   goodradius.barkpresent=badradius.barkpresent;
   doupdate:=true;
END IF;
IF goodradius.barkpresent!=badradius.barkpresent THEN
   commentstr:= commentstr || 'Bark presence differs (other record = ' || badradius.barkpresent::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- lastringunderbark
IF goodradius.lastringunderbark IS NULL THEN
   goodradius.lastringunderbark=badradius.lastringunderbark;
   doupdate:=true;
END IF;
IF goodradius.lastringunderbark!=badradius.lastringunderbark THEN
   commentstr:= commentstr || 'Last ring under bark differs (other record = ' || badradius.lastringunderbark::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- missingheartwoodringstopith
IF goodradius.missingheartwoodringstopith IS NULL THEN
   goodradius.missingheartwoodringstopith=badradius.missingheartwoodringstopith;
   doupdate:=true;
END IF;
IF goodradius.missingheartwoodringstopith!=badradius.missingheartwoodringstopith THEN
   commentstr:= commentstr || 'Missing heartwood rings differ (other record = ' || badradius.missingheartwoodringstopith::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- missingsapwoodringstobark
IF goodradius.missingsapwoodringstobark IS NULL THEN
   goodradius.missingsapwoodringstobark=badradius.missingsapwoodringstobark;
   doupdate:=true;
END IF;
IF goodradius.missingsapwoodringstobark!=badradius.missingsapwoodringstobark THEN
   commentstr:= commentstr || 'Missing sapwood rings differ (other record = ' || badradius.missingsapwoodringstobark::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- missingheartwoodringstopithfoundation
IF goodradius.missingheartwoodringstopithfoundation IS NULL THEN
   goodradius.missingheartwoodringstopithfoundation=badradius.missingheartwoodringstopithfoundation;
   doupdate:=true;
END IF;
IF goodradius.missingheartwoodringstopithfoundation!=badradius.missingheartwoodringstopithfoundation THEN
   commentstr:= commentstr || 'Missing heartwood rings foundation differs (other record = ' || badradius.missingheartwoodringstopithfoundation::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- missingsapwoodringstobarkfoundation
IF goodradius.missingsapwoodringstobarkfoundation IS NULL THEN
   goodradius.missingsapwoodringstobarkfoundation=badradius.missingsapwoodringstobarkfoundation;
   doupdate:=true;
END IF;
IF goodradius.missingsapwoodringstobarkfoundation!=badradius.missingsapwoodringstobarkfoundation THEN
   commentstr:= commentstr || 'Missing sapwood rings foundation differs (other record = ' || badradius.missingsapwoodringstobarkfoundation::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- Sapwood id
IF goodradius.sapwoodid IS NULL THEN
   goodradius.sapwoodid=badradius.sapwoodid;
   doupdate:=true;
END IF;
IF goodradius.sapwoodid!=badradius.sapwoodid THEN
   SELECT complexpresenceabsence INTO lookup FROM tlkpcomplexpresenceabsence WHERE complexpresenceabsenceid=badradius.sapwoodid;
   commentstr:= commentstr || 'Sapwood presence differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- Heartwood id
IF goodradius.heartwoodid IS NULL THEN
   goodradius.heartwoodid=badradius.heartwoodid;
   doupdate:=true;
END IF;
IF goodradius.heartwoodid!=badradius.heartwoodid THEN
   SELECT complexpresenceabsence INTO lookup FROM tlkpcomplexpresenceabsence WHERE complexpresenceabsenceid=badradius.heartwoodid;
   commentstr:= commentstr || 'Heartwood presence differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- Azimuth
IF goodradius.azimuth IS NULL THEN
   goodradius.azimuth=badradius.azimuth;
   doupdate:=true;
END IF;
IF goodradius.azimuth!=badradius.azimuth THEN
   commentstr:= commentstr || 'Azimuth differs (other record = ' || badradius.azimuth::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- Show notices for any discrepancies
--IF discrepancycount=1 THEN
--  RAISE NOTICE '1 discrepancy detected when merging radii';
--ELSIF discrepancycount > 1 THEN
--  RAISE NOTICE '% discrepancies detected when mergin radii', discrepancycount;
--ELSE 
--  RAISE NOTICE 'No discrepancies detected';
--END IF;

-- If there are discrepancies either set or update the comments field to highlight issues
IF (discrepancycount>0 OR doupdate=true) THEN
  IF (discrepancycount>0 AND goodradius.comments IS NOT NULL) THEN
    --RAISE NOTICE 'Adding discrepency description to comments field';
    commentstr := goodradius.comments || discrepstr || commentstr || '***';
  ELSIF (discrepancycount>0) THEN
    --RAISE NOTICE 'Putting discrepency description in comments field';
    commentstr := discrepstr || commentstr || '***';
  ELSE
    commentstr := goodradius.comments;
  END IF;
  
  UPDATE tblradius SET
    code=goodradius.code,
    numberofsapwoodrings=goodradius.numberofsapwoodrings,
    pithid=goodradius.pithid,
    barkpresent=goodradius.barkpresent,
    lastringunderbark=goodradius.lastringunderbark,
    missingheartwoodringstopith=goodradius.missingheartwoodringstopith,
    missingheartwoodringstopithfoundation=goodradius.missingheartwoodringstopithfoundation,
    missingsapwoodringstobark=goodradius.missingsapwoodringstobark,
    missingsapwoodringstobarkfoundation=goodradius.missingsapwoodringstobarkfoundation,
    sapwoodid=goodradius.sapwoodid,
    heartwoodid=goodradius.heartwoodid,
    azimuth=goodradius.azimuth,
    comments = commentstr
    WHERE radiusid=goodradius.radiusid;
  
END IF;

-- Delete old radius record
DELETE FROM tblradius WHERE radiusid=badradiusid;

-- Get updated radius info and return it
SELECT * INTO goodradius FROM tblradius WHERE radiusid=goodradiusid;
RETURN goodradius;
END;$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.mergeradii(uuid, uuid) OWNER TO aps03pwb;
COMMENT ON FUNCTION cpgdb.mergeradii(uuid, uuid) IS 'This function merges a ''bad'' radius with a ''good'' radius.  References to the bad radius are changed to point to the new radius, any discrepancies between fields are marked in the comments field and then the ''bad'' radius is deleted.';
