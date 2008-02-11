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



CREATE OR REPLACE FUNCTION cpgdb.lookupenvdata(thetreeid integer, therasterlayerid integer)
RETURNS double precision AS
$BODY$DECLARE
reftree CURSOR FOR SELECT X(location) AS longitude, Y(location) AS latitude FROM tbltree where treeid=thetreeid;
reflayers CURSOR FOR SELECT filename FROM tblrasterlayer WHERE rasterlayerid=therasterlayerid;
refvalue CURSOR (myfilename varchar, mylat double precision, mylong double precision) FOR SELECT rasteratpoint from cpgdb.rasteratpoint(myfilename, mylat, mylong);
myfilename varchar;
mylat double precision;
mylong double precision;
myvalue double precision;
BEGIN

    -- Lookup latitude and longitude for specified treeid
    OPEN reftree;
    FETCH reftree INTO mylong, mylat;
    CLOSE reftree;

    -- Lookup filename for specified rasterlayer
    OPEN reflayers;
    FETCH reflayers INTO myfilename;
    CLOSE reflayers;

    -- Lookup value in current raster layer
    OPEN refvalue(myfilename, mylat, mylong);
    FETCH refvalue INTO myvalue;
    CLOSE refvalue;

    -- Delete existing value
    DELETE FROM tblenvironmentaldata where rasterlayerid=therasterlayerid and treeid=thetreeid;

    -- Insert value in tblenvironmentaldata
    INSERT INTO tblenvironmentaldata(rasterlayerid, treeid, value) VALUES (therasterlayerid, thetreeid, myvalue);

    RETURN myvalue;

END;$BODY$
LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION cpgdb.lookupenvdata(thetreeid integer, therasterlayerid integer) OWNER TO aps03pwb;
GRANT EXECUTE ON FUNCTION cpgdb.lookupenvdata(thetreeid integer, therasterlayerid integer) TO public;
GRANT EXECUTE ON FUNCTION cpgdb.lookupenvdata(thetreeid integer, therasterlayerid integer) TO aps03pwb;
GRANT EXECUTE ON FUNCTION cpgdb.lookupenvdata(thetreeid integer, therasterlayerid integer) TO "Webgroup";




CREATE OR REPLACE FUNCTION cpgdb.lookupenvdatabylayer(therasterlayerid integer)
RETURNS void AS
$BODY$DECLARE
reftrees CURSOR FOR SELECT treeid FROM tbltree WHERE location IS NOT NULL;
myrasterlayerid integer;
mytreeid integer;
mybin double precision;
BEGIN

    -- Loop through all trees with a location
    OPEN reftrees;
    FETCH reftrees INTO mytreeid;
    WHILE FOUND LOOP

        -- Do lookup
        SELECT * FROM cpgdb.lookupenvdata(mytreeid, therasterlayerid) INTO mybin;

        -- Continue to next rasterlayer    
        FETCH reftrees INTO mytreeid;
    END LOOP;
    CLOSE reftrees;

    RETURN;

END;$BODY$
LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION cpgdb.lookupenvdatabylayer(therasterlayerid integer) OWNER TO aps03pwb;
GRANT EXECUTE ON FUNCTION cpgdb.lookupenvdatabylayer(therasterlayerid integer) TO public;
GRANT EXECUTE ON FUNCTION cpgdb.lookupenvdatabylayer(therasterlayerid integer) TO aps03pwb;
GRANT EXECUTE ON FUNCTION cpgdb.lookupenvdatabylayer(therasterlayerid integer) TO "Webgroup";





CREATE OR REPLACE FUNCTION cpgdb.lookupenvdatabytree(thetreeid integer)
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
        SELECT * FROM cpgdb.lookupenvdata(thetreeid, myrasterlayerid) INTO mybin;

        -- Continue to next rasterlayer    
        FETCH reflayers2 INTO myrasterlayerid;
     END LOOP;
     CLOSE reflayers2;

     RETURN;

END;$BODY$
LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION cpgdb.lookupenvdatabytree(thetreeid integer) OWNER TO aps03pwb;
GRANT EXECUTE ON FUNCTION cpgdb.lookupenvdatabytree(thetreeid integer) TO public;
GRANT EXECUTE ON FUNCTION cpgdb.lookupenvdatabytree(thetreeid integer) TO aps03pwb;
GRANT EXECUTE ON FUNCTION cpgdb.lookupenvdatabytree(thetreeid integer) TO "Webgroup";

