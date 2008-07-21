CREATE OR REPLACE FUNCTION cpgdb.rasteratpoint(rasterfile character varying, latitude double precision, longitude double precision)
RETURNS double precision AS
$BODY$import gdal
import os
from optparse import OptionParser 

myF = gdal.Open(rasterfile)
myA = myF.ReadAsArray()

# get X and Y lower-left corner
minX = myF.GetGeoTransform()[0]
minY = myF.GetGeoTransform()[3]
cellSizeX = myF.GetGeoTransform()[1]
cellSizeY = myF.GetGeoTransform()[5]
maxX = minX + (cellSizeX * myF.RasterXSize)
maxY = minY + (cellSizeY * myF.RasterYSize)

# work out difference from corner to chosen cell
myXdiff = longitude - minX
myYdiff = latitude - minY

# divide this difference by cell size to get cell position
myXcell = int(myXdiff / cellSizeX) 
myYcell = int(myYdiff / cellSizeY) 

myCellValue = myA[myYcell][myXcell]

return myCellValue$BODY$
LANGUAGE 'plpythonu' VOLATILE;
ALTER FUNCTION cpgdb.rasteratpoint(rasterfile character varying, latitude double precision, longitude double precision) OWNER TO aps03pwb;
GRANT EXECUTE ON FUNCTION cpgdb.rasteratpoint(rasterfile character varying, latitude double precision, longitude double precision) TO public;
GRANT EXECUTE ON FUNCTION cpgdb.rasteratpoint(rasterfile character varying, latitude double precision, longitude double precision) TO aps03pwb;
GRANT EXECUTE ON FUNCTION cpgdb.rasteratpoint(rasterfile character varying, latitude double precision, longitude double precision) TO "Webgroup";



CREATE OR REPLACE FUNCTION cpgdb.lookupenvdata(theelementid integer, therasterlayerid integer)
RETURNS double precision AS
$BODY$DECLARE
refelement CURSOR FOR SELECT X(location) AS longitude, Y(location) AS latitude FROM tblelement where elementid=theelementid;
reflayers CURSOR FOR SELECT filename FROM tblrasterlayer WHERE rasterlayerid=therasterlayerid;
refvalue CURSOR (myfilename varchar, mylat double precision, mylong double precision) FOR SELECT rasteratpoint from cpgdb.rasteratpoint(myfilename, mylat, mylong);
myfilename varchar;
mylat double precision;
mylong double precision;
myvalue double precision;
BEGIN

    -- Lookup latitude and longitude for specified elementid
    OPEN refelement;
    FETCH refelement INTO mylong, mylat;
    CLOSE refelement;

    -- Lookup filename for specified rasterlayer
    OPEN reflayers;
    FETCH reflayers INTO myfilename;
    CLOSE reflayers;

    -- Lookup value in current raster layer
    OPEN refvalue(myfilename, mylat, mylong);
    FETCH refvalue INTO myvalue;
    CLOSE refvalue;

    -- Delete existing value
    DELETE FROM tblenvironmentaldata where rasterlayerid=therasterlayerid and elementid=theelementid;

    -- Insert value in tblenvironmentaldata
    INSERT INTO tblenvironmentaldata(rasterlayerid, elementid, value) VALUES (therasterlayerid, theelementid, myvalue);

    RETURN myvalue;

END;$BODY$
LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION cpgdb.lookupenvdata(theelementid integer, therasterlayerid integer) OWNER TO aps03pwb;
GRANT EXECUTE ON FUNCTION cpgdb.lookupenvdata(theelementid integer, therasterlayerid integer) TO public;
GRANT EXECUTE ON FUNCTION cpgdb.lookupenvdata(theelementid integer, therasterlayerid integer) TO aps03pwb;
GRANT EXECUTE ON FUNCTION cpgdb.lookupenvdata(theelementid integer, therasterlayerid integer) TO "Webgroup";




CREATE OR REPLACE FUNCTION cpgdb.lookupenvdatabylayer(therasterlayerid integer)
RETURNS void AS
$BODY$DECLARE
refelements CURSOR FOR SELECT elementid FROM tblelement WHERE location IS NOT NULL;
myrasterlayerid integer;
myelementid integer;
mybin double precision;
BEGIN

    -- Loop through all elements with a location
    OPEN refelements;
    FETCH refelements INTO myelementid;
    WHILE FOUND LOOP

        -- Do lookup
        SELECT * FROM cpgdb.lookupenvdata(myelementid, therasterlayerid) INTO mybin;

        -- Continue to next rasterlayer    
        FETCH refelements INTO myelementid;
    END LOOP;
    CLOSE refelements;

    RETURN;

END;$BODY$
LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION cpgdb.lookupenvdatabylayer(therasterlayerid integer) OWNER TO aps03pwb;
GRANT EXECUTE ON FUNCTION cpgdb.lookupenvdatabylayer(therasterlayerid integer) TO public;
GRANT EXECUTE ON FUNCTION cpgdb.lookupenvdatabylayer(therasterlayerid integer) TO aps03pwb;
GRANT EXECUTE ON FUNCTION cpgdb.lookupenvdatabylayer(therasterlayerid integer) TO "Webgroup";





CREATE OR REPLACE FUNCTION cpgdb.lookupenvdatabyelement(theelementid integer)
RETURNS void AS
$BODY$DECLARE
reflayers2 CURSOR FOR SELECT rasterlayerid FROM tblrasterlayer WHERE issystemlayer=true;
myrasterlayerid integer;
mybin double precision;
BEGIN

    -- Loop through all system rasterlayers
    OPEN reflayers2;
    FETCH reflayers2 INTO myrasterlayerid;
    WHILE FOUND LOOP

        -- Do lookup
        SELECT * FROM cpgdb.lookupenvdata(theelementid, myrasterlayerid) INTO mybin;

        -- Continue to next rasterlayer    
        FETCH reflayers2 INTO myrasterlayerid;
     END LOOP;
     CLOSE reflayers2;

     RETURN;

END;$BODY$
LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION cpgdb.lookupenvdatabyelement(theelementid integer) OWNER TO aps03pwb;
GRANT EXECUTE ON FUNCTION cpgdb.lookupenvdatabyelement(theelementid integer) TO public;
GRANT EXECUTE ON FUNCTION cpgdb.lookupenvdatabyelement(theelementid integer) TO aps03pwb;
GRANT EXECUTE ON FUNCTION cpgdb.lookupenvdatabyelement(theelementid integer) TO "Webgroup";

