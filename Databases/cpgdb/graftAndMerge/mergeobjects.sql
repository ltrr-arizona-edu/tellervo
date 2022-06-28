-- Function: cpgdb.mergeobjects(uuid, uuid)

-- DROP FUNCTION cpgdb.mergeobjects(uuid, uuid);

CREATE OR REPLACE FUNCTION cpgdb.mergeobjects(goodobject uuid, badobject uuid)
  RETURNS tblobject AS
$BODY$DECLARE

goodobjectid ALIAS FOR $1;      -- UUID of the object with correct entities
goodobject tblobject%ROWTYPE;   -- The object itself
badobjectid ALIAS FOR $2;       -- UUID of the object with incorrect entities
badobject tblobject%ROWTYPE;    -- The object itself
commentstr varchar;             -- Used to compile comments for indicating discrepancies 
discrepstr varchar;             -- Basic comment to highlight discrepancies
discrepancycount integer;       -- Count of number of fields that aren't the same

lookup varchar;                 -- Temporary variable for storing values from lookup tables
badchild RECORD;                -- Temporary variable for storing associated subobjects/elements
goodchildid uuid;               -- Temporary variable for storing uuid of good associated subobjects/elements
doupdate boolean;               -- Temporary variable for flagging whether to update this record

BEGIN

-- Initialize vars
discrepancycount:=0;
commentstr:= '';
discrepstr:='*** Another record was automatically merged with this one and discrepancies were detected.  Please check the following issues: ';
doupdate:=false;


-- Get the two objects in question
SELECT * INTO goodobject FROM tblobject WHERE objectid=goodobjectid;
IF NOT FOUND THEN
   RAISE NOTICE 'The good object with id % was not found', goodobjectid;
   RETURN NULL;
END IF;
SELECT * INTO badobject FROM tblobject WHERE objectid=badobjectid;
IF NOT FOUND THEN
   RAISE NOTICE 'The bad object with id % was not found', badobjectid;
   RETURN NULL;
END IF;

-- Check objects are different
IF goodobject.objectid=badobject.objectid THEN 
   RAISE NOTICE 'Objects are the same!';
   RETURN goodobject;
END IF;

-- Before we go any further we need to check that any associated subobjects don't need merging first
FOR badchild IN SELECT * FROM tblobject WHERE parentobjectid=badobjectid ORDER BY code LOOP 
   SELECT objectid INTO goodchildid FROM tblobject WHERE objectid=goodobjectid AND code=badchild.code;
   IF goodchildid IS NOT NULL THEN
       --RAISE NOTICE 'Recursing into mergeSample() as mergeElement() will cause conflict with sample % (code %)', badsamp.sampleid, badsamp.code; 
       PERFORM cpgdb.mergeobjects(goodchildid, badchild.objectid);
   ELSE
       --RAISE NOTICE 'No conflict here so just go ahead a relink subobject to new object';
       UPDATE tblobject SET parentobjectid=goodobjectid WHERE objectid=badchild.objectid;
   END IF;
END LOOP;

-- Then we need to check whether any associated elements need merging next
FOR badchild IN SELECT * FROM tblelement WHERE objectid=badobjectid ORDER BY code LOOP 
   SELECT elementid INTO goodchildid FROM tblelement WHERE objectid=goodobjectid AND code=badchild.code;
   IF goodchildid IS NOT NULL THEN
       --RAISE NOTICE 'Recursing into mergeSample() as mergeElement() will cause conflict with sample % (code %)', badsamp.sampleid, badsamp.code; 
       PERFORM cpgdb.mergeelements(goodchildid, badchild.elementid);
   ELSE
       --RAISE NOTICE 'No conflict here so just go ahead a relink element to new object';
       UPDATE tblelement SET objectid=goodobjectid WHERE elementid=badchild.elementid;
   END IF;
END LOOP;


-- COMPARE EACH FIELD TO CHECK FOR DISPARITIES


-- Location precision 
IF goodobject.locationprecision IS NULL THEN
   goodobject.locationprecision=badobject.locationprecision;
   doupdate:=true;
END IF;
IF goodobject.locationprecision!=badobject.locationprecision THEN
   commentstr:= commentstr || 'Location precision differs (other record = ' || badobject.locationprecision::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

--locationgeometry
IF goodobject.locationgeometry IS NULL THEN
   goodobject.locationgeometry=badobject.locationgeometry;
   doupdate:=true;
END IF;
IF goodobject.locationgeometry!=badobject.locationgeometry THEN
   SELECT 'Long: ' || x(locationgeometry) || ', Lat: ' ||  y(locationgeometry) INTO lookup FROM tblobject WHERE objectid=badobject.objectid;   
   commentstr:= commentstr || 'Location geometry differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- locationtypeid
IF goodobject.locationtypeid IS NULL THEN
   goodobject.locationtypeid=badobject.locationtypeid;
   doupdate:=true;
END IF;
IF goodobject.locationtypeid!=badobject.locationtypeid THEN
   SELECT locationtype INTO lookup FROM tlkplocationtype WHERE locationtypeid=badobject.locationtypeid;
   commentstr:= commentstr || 'Location type differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- locationcomment
IF goodobject.locationcomment IS NULL THEN
   goodobject.locationcomment=badobject.locationcomment;
   doupdate:=true;
END IF;
IF goodobject.locationcomment!=badobject.locationcomment THEN
   commentstr:= commentstr || 'Location comments differ (other record = ' || badobject.locationcomment::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- file
IF goodobject.file IS NULL THEN
   goodobject.file=badobject.file;
   doupdate:=true;
END IF;
IF goodobject.file!=badobject.file THEN
   commentstr:= commentstr || 'File links differ (other record = ' || badobject.file::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- creator
IF goodobject.creator IS NULL THEN
   goodobject.creator=badobject.creator;
   doupdate:=true;
END IF;
IF goodobject.creator!=badobject.creator THEN
   commentstr:= commentstr || 'Creator differs (other record = ' || badobject.creator::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- owner
IF goodobject.owner IS NULL THEN
   goodobject.owner=badobject.owner;
   doupdate:=true;
END IF;
IF goodobject.owner!=badobject.owner THEN
   commentstr:= commentstr || 'Owner differs (other record = ' || badobject.owner::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

-- description
IF goodobject.description IS NULL THEN
   goodobject.description=badobject.description;
   doupdate:=true;
END IF;
IF goodobject.description!=badobject.description THEN
   commentstr:= commentstr || 'Description differs (other record = ' || badobject.file::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;
 
-- objectypeid
IF goodobject.objecttypeid IS NULL THEN
   goodobject.objecttypeid=badobject.objecttypeid;
   doupdate:=true;
END IF;
IF goodobject.objecttypeid!=badobject.objecttypeid THEN
   SELECT objecttype INTO lookup FROM tlkpobjecttype WHERE objecttypeid=badobject.objecttypeid;
   commentstr:= commentstr || 'Object type differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- coveragetemporalid
IF goodobject.coveragetemporalid IS NULL THEN
   goodobject.coveragetemporalid=badobject.coveragetemporalid;
   doupdate:=true;
END IF;
IF goodobject.coveragetemporalid!=badobject.coveragetemporalid THEN
   SELECT coveragetemporal INTO lookup FROM tlkpcoveragetemporal WHERE coveragetemporalid=badobject.coveragetemporalid;
   commentstr:= commentstr || 'Temporal coverage differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

-- coveragetemporalfoundationid
IF goodobject.coveragetemporalfoundationid IS NULL THEN
   goodobject.coveragetemporalfoundationid=badobject.coveragetemporalfoundationid;
   doupdate:=true;
END IF;
IF goodobject.coveragetemporalfoundationid!=badobject.coveragetemporalfoundationid THEN
   SELECT coveragetemporalfoundation INTO lookup FROM tlkpcoveragetemporalfoundation WHERE coveragetemporalfoundationid=badobject.coveragetemporalfoundationid;
   commentstr:= commentstr || 'Temporal coverage foundation differs (other record = ' || lookup::varchar || '). ';
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
IF (discrepancycount>0 OR doupdate=true) THEN
  IF (discrepancycount>0 AND goodobject.comments IS NOT NULL) THEN
    --RAISE NOTICE 'Adding discrepency description to comments field';
    commentstr := goodobject.comments || discrepstr || commentstr || '***';
  ELSIF (discrepancycount>0) THEN
    --RAISE NOTICE 'Putting discrepency description in comments field';
    commentstr := discrepstr || commentstr || '***';
  ELSE
    commentstr := goodobject.comments;
  END IF;

  UPDATE tblobject SET
    title=goodobject.title,
    code=goodobject.code,
    locationgeometry=goodobject.locationgeometry,
    locationtypeid=goodobject.locationtypeid,
    locationprecision=goodobject.locationprecision,
    locationcomment=goodobject.locationcomment,
    file=goodobject.file,
    creator=goodobject.creator,
    owner=goodobject.owner,
    description=goodobject.description,
    objecttypeid=goodobject.objecttypeid,
    coveragetemporalid=goodobject.coveragetemporalid,
    coveragetemporalfoundation=goodobject.coveragetemporalfoundation,
    comments= commentstr
    WHERE objectid=goodobjectid;
END IF;

-- Delete old object record
DELETE FROM tblobject WHERE objectid=badobjectid;

-- Get updated object info and return it
SELECT * INTO goodobject FROM tblobject WHERE objectid=goodobjectid;
RETURN goodobject;
END;$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.mergeobjects(uuid, uuid) OWNER TO aps03pwb;
