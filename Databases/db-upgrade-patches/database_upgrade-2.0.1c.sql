CREATE OR REPLACE FUNCTION ST_Donut (geom Geometry, dough numeric, nut numeric) RETURNS Geometry AS 
$$
    DECLARE
        donut Geometry;
    BEGIN  
        donut = ST_Difference(ST_Buffer(geom::geography, dough)::geometry, ST_Buffer(geom::geography, nut)::geometry);
        RETURN donut;
    END;

$$ LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION cpgdb.obfuscatePoint(thegeom Geometry) RETURNS Geometry AS 
$$

    SELECT ST_GeometryN(ST_GeneratePoints(ST_Donut(thegeom, 5000, 1000), 10),1);
    
$$ LANGUAGE sql;


