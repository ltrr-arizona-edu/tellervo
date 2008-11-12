CREATE OR REPLACE FUNCTION cpgdb.update_siteextent(siteid integer)
RETURNS geometry AS
$BODY$DECLARE
myrec RECORD;
mysiteid ALIAS FOR $1;
BEGIN
    SELECT INTO myrec setsrid(extent(location)::geometry,4326) as myextent 
    FROM tbltree, tblsubsite 
    WHERE tbltree.subsiteid=tblsubsite.subsiteid
    AND tblsubsite.siteid=mysiteid;

    UPDATE tblsite SET siteextent=myrec.myextent where tblsite.siteid=mysiteid; 
    RETURN myrec.myextent;
END;$BODY$
LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION update_siteextent(siteid integer) OWNER TO aps03pwb;
GRANT EXECUTE ON FUNCTION cpgdb.update_siteextent(siteid integer) TO public;
GRANT EXECUTE ON FUNCTION cpgdb.update_siteextent(siteid integer) TO aps03pwb;
GRANT EXECUTE ON FUNCTION cpgdb.update_siteextent(siteid integer) TO "Webgroup";

