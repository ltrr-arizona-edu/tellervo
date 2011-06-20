CREATE OR REPLACE FUNCTION cpgdb.update_objectextent(objectid integer)
RETURNS geometry AS
$BODY$DECLARE
myrec RECORD;
myobjectid ALIAS FOR $1;
BEGIN
    SELECT INTO myrec setsrid(extent(location)::geometry,4326) as myextent 
    FROM tblelement, tblsubobject 
    WHERE tblelement.subobjectid=tblsubobject.subobjectid
    AND tblsubobject.objectid=myobjectid;

    UPDATE tblobject SET objectextent=myrec.myextent where tblobject.objectid=myobjectid; 
    RETURN myrec.myextent;
END;$BODY$
LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION update_objectextent(objectid integer) OWNER TO aps03pwb;
GRANT EXECUTE ON FUNCTION cpgdb.update_objectextent(objectid integer) TO public;
GRANT EXECUTE ON FUNCTION cpgdb.update_objectextent(objectid integer) TO aps03pwb;
GRANT EXECUTE ON FUNCTION cpgdb.update_objectextent(objectid integer) TO "Webgroup";

