----------------------------------------------------
-- Protect sensitive locations by obfuscation 
----------------------------------------------------

-- Add fields to tblobject and tblelement


ALTER TABLE tblobject ADD COLUMN islocsensitive boolean default false;
ALTER TABLE tblobject ADD COLUMN safelocationgeometry geometry;
ALTER TABLE tblobject ADD COLUMN safelocationprecision integer;

ALTER TABLE tblelement ADD COLUMN islocsensitive boolean default false;
ALTER TABLE tblelement ADD COLUMN safelocationgeometry geometry;
ALTER TABLE tblelement ADD COLUMN safelocationprecision integer;


CREATE OR REPLACE FUNCTION cpgdb.obfuscatePoint(thegeom Geometry) RETURNS Geometry AS 
$$
    SELECT ST_GeometryN(ST_GeneratePoints(ST_Donut(thegeom, 5000, 1000), 10),1);
$$ LANGUAGE sql;

CREATE OR REPLACE FUNCTION update_safelocation() RETURNS trigger
    LANGUAGE plpgsql
    AS 
$$
BEGIN
    
    IF NEW.locationgeometry IS NULL THEN
    	--RAISE NOTICE 'No location information so no need to consider obfuscating';
     	NEW.safelocationgeometry=NULL;
    	NEW.safelocationprecision=NULL;  
    	RETURN new;	
    END IF;
    
    IF NEW.islocsensitive IS TRUE THEN
    	--RAISE NOTICE 'Location is sensitive so it should be obfuscated';
    	IF OLD.safelocationgeometry IS NULL THEN
    		IF NEW.locationprecision>5000 THEN
	    		RAISE NOTICE 'Location is already very imprecise so no need to obfuscate further (%m)', NEW.locationprecision;
	    		NEW.safelocationgeometry=NEW.locationgeometry; 
	    		NEW.safelocationprecision=NEW.locationprecision;
	    	ELSE
  				RAISE NOTICE 'Location not previously obfuscated so calculating obfuscation now...';
	    		NEW.safelocationgeometry=cpgdb.obfuscatePoint(NEW.locationgeometry);
	    		
	    		IF NEW.locationprecision IS NULL THEN
	    			NEW.safelocationprecision=5000;
	    		ELSE
	    			NEW.safelocationprecision=5000+OLD.locationprecision;
	    		END IF;	    		
	    	END IF;
    	ELSE
    		--RAISE NOTICE 'Location is already obfuscated so leaving as it is';
    		NEW.safelocationgeometry=OLD.safelocationgeometry;
    		NEW.safelocationprecision=OLD.safelocationprecision;
    	END IF;
    ELSE
    	--RAISE NOTICE 'Location is NOT sensitive so obfuscation is not necessary';    
    	NEW.safelocationgeometry=NULL;
    	NEW.safelocationprecision=NULL;
    END IF;
    
RETURN new;
END;
$$;


CREATE TRIGGER "trig_update_object-safelocation"
  BEFORE INSERT OR UPDATE
  ON tblobject
  FOR EACH ROW
  EXECUTE PROCEDURE update_safelocation();
 
CREATE TRIGGER "trig_update_element-safelocation"
  BEFORE INSERT OR UPDATE
  ON tblelement
  FOR EACH ROW
  EXECUTE PROCEDURE update_safelocation();

DROP VIEW portal.vwportalcomb;
DROP VIEW portal.vwportaldata2;  
DROP VIEW portal.vwportaldata1;

CREATE OR REPLACE VIEW portal.vwportaldata1 AS 
 SELECT p.projectid,
    p.lastmodifiedtimestamp AS projectlastmodifiedtimestamp,
    p.title AS projecttitle,
    p.investigator,
    o.objectid,
    o.lastmodifiedtimestamp AS objectlastmodifiedtimestamp,
    o.title AS objecttitle,
    o.code AS objectcode,
    NULL::uuid AS object2id,
    NULL::timestamp with time zone AS object2lastmodifiedtimestamp,
    NULL::character varying AS object2title,
    NULL::character varying AS object2code,
    e.elementid,
    e.lastmodifiedtimestamp AS elementlastmodifiedtimestamp,
    e.code AS elementcode,
    e.taxonid,
    t.label AS taxonlabel,
    t.htmllabel AS taxonlabelhtml,
    s.sampleid,
    s.lastmodifiedtimestamp AS samplelastmodifiedtimestamp,
    s.code AS samplecode,
    s.externalid,
    s.samplingdate,
    s.samplingyear,
    s.samplingmonth,
    s.samplingdateprec,
    s.sampletype,
    convert_to_integer(fy.value::text) AS firstyear,
    convert_to_integer(ly.value::text) AS lastyear,
    s.dendrochronologist,
    b.boxid,
    b.title AS boxtitle,
    b.curationlocation,
    b.trackinglocation,
    s.userdefinedfieldsastext,
    cpgdb.preferreddouble(xmin(e.locationgeometry::box3d), NULL::double precision, xmin(o.locationgeometry::box3d)) AS preferredlongitude,
    cpgdb.preferreddouble(ymin(e.locationgeometry::box3d), NULL::double precision, ymin(o.locationgeometry::box3d)) AS preferredlatitude,
    cpgdb.preferreddouble(e.locationprecision, NULL::double precision, o.locationprecision::double precision) AS preferredlocationprecision,
    cpgdb.preferreddouble(xmin(e.safelocationgeometry::box3d), NULL::double precision, xmin(o.safelocationgeometry::box3d)) AS safepreferredlongitude,
    cpgdb.preferreddouble(ymin(e.safelocationgeometry::box3d), NULL::double precision, ymin(o.safelocationgeometry::box3d)) AS safepreferredlatitude,
    cpgdb.preferreddouble(e.safelocationprecision, NULL::double precision, o.safelocationprecision::double precision) AS safepreferredlocationprecision,
    (e.safelocationgeometry IS NOT NULL OR o.safelocationgeometry IS NOT NULL)::boolean AS islocsensitive
   FROM tblproject p
     LEFT JOIN tblobject o ON p.projectid = o.projectid
     LEFT JOIN tblelement e ON o.objectid = e.objectid
     LEFT JOIN tlkptaxon t ON e.taxonid::text = t.taxonid::text
     LEFT JOIN vwtblsample2 s ON e.elementid = s.elementid
     LEFT JOIN tblbox b ON s.boxid = b.boxid
     LEFT JOIN vwfirstyear fy ON s.sampleid = fy.entityid
     LEFT JOIN vwlastyear ly ON s.sampleid = ly.entityid
  WHERE o.parentobjectid IS NULL AND s.sampleid IS NOT NULL;

CREATE OR REPLACE VIEW portal.vwportaldata2 AS 
 SELECT p.projectid,
    p.lastmodifiedtimestamp AS projectlastmodifiedtimestamp,
    p.title AS projecttitle,
    p.investigator,
    o.objectid,
    o.lastmodifiedtimestamp AS objectlastmodifiedtimestamp,
    o.title AS objecttitle,
    o.code AS objectcode,
    o2.objectid AS object2id,
    o2.lastmodifiedtimestamp AS object2lastmodifiedtimestamp,
    o2.title AS object2title,
    o2.code AS object2code,
    e.elementid,
    e.lastmodifiedtimestamp AS elementlastmodifiedtimestamp,
    e.code AS elementcode,
    e.taxonid,
    t.label AS taxonlabel,
    t.htmllabel AS taxonlabelhtml,
    s.sampleid,
    s.lastmodifiedtimestamp AS samplelastmodifiedtimestamp,
    s.code AS samplecode,
    s.externalid,
    s.samplingdate,
    s.samplingyear,
    s.samplingmonth,
    s.samplingdateprec,
    s.sampletype,
    convert_to_integer(fy.value::text) AS firstyear,
    convert_to_integer(ly.value::text) AS lastyear,
    s.dendrochronologist,
    b.boxid,
    b.title AS boxtitle,
    b.curationlocation,
    b.trackinglocation,
    s.userdefinedfieldsastext,
    cpgdb.preferreddouble(xmin(e.locationgeometry::box3d), xmin(o2.locationgeometry::box3d), xmin(o.locationgeometry::box3d)) AS preferredlongitude,
    cpgdb.preferreddouble(ymin(e.locationgeometry::box3d), ymin(o2.locationgeometry::box3d), ymin(o.locationgeometry::box3d)) AS preferredlatitude,
    cpgdb.preferreddouble(e.locationprecision, o2.locationprecision::double precision, o.locationprecision::double precision) AS preferredlocationprecision,
    cpgdb.preferreddouble(xmin(e.safelocationgeometry::box3d), xmin(o2.safelocationgeometry::box3d), xmin(o.safelocationgeometry::box3d)) AS safepreferredlongitude,
    cpgdb.preferreddouble(ymin(e.safelocationgeometry::box3d), ymin(o2.safelocationgeometry::box3d), ymin(o.safelocationgeometry::box3d)) AS safepreferredlatitude,
    cpgdb.preferreddouble(e.safelocationprecision, o2.safelocationprecision::double precision, o.safelocationprecision::double precision) AS safepreferredlocationprecision,
    (e.safelocationgeometry IS NOT NULL OR o2.safelocationgeometry IS NOT NULL OR o.safelocationgeometry IS NOT NULL)::boolean AS islocsensitive
   FROM tblproject p
     LEFT JOIN tblobject o ON p.projectid = o.projectid
     LEFT JOIN tblobject o2 ON o.objectid = o2.parentobjectid
     LEFT JOIN tblelement e ON o.objectid = e.objectid
     LEFT JOIN tlkptaxon t ON e.taxonid::text = t.taxonid::text
     LEFT JOIN vwtblsample2 s ON e.elementid = s.elementid
     LEFT JOIN tblbox b ON s.boxid = b.boxid
     LEFT JOIN vwfirstyear fy ON s.sampleid = fy.entityid
     LEFT JOIN vwlastyear ly ON s.sampleid = ly.entityid
  WHERE o2.parentobjectid IS NOT NULL AND s.sampleid IS NOT NULL;


CREATE OR REPLACE VIEW portal.vwportalcomb AS 
 SELECT vwportaldata1.projectid,
    vwportaldata1.projectlastmodifiedtimestamp,
    vwportaldata1.projecttitle,
    vwportaldata1.investigator,
    vwportaldata1.objectid,
    vwportaldata1.objectlastmodifiedtimestamp,
    vwportaldata1.objecttitle,
    vwportaldata1.objectcode,
    vwportaldata1.object2id,
    vwportaldata1.object2lastmodifiedtimestamp,
    vwportaldata1.object2title,
    vwportaldata1.object2code,
    vwportaldata1.elementid,
    vwportaldata1.elementlastmodifiedtimestamp,
    vwportaldata1.elementcode,
    vwportaldata1.taxonid,
    vwportaldata1.taxonlabel,
    vwportaldata1.taxonlabelhtml,    
    vwportaldata1.sampleid,
    vwportaldata1.samplelastmodifiedtimestamp,
    vwportaldata1.samplecode,
    vwportaldata1.externalid,
    vwportaldata1.samplingdate,
    vwportaldata1.samplingyear,
    vwportaldata1.samplingmonth,
    vwportaldata1.samplingdateprec,
    vwportaldata1.sampletype,
    vwportaldata1.firstyear,
    vwportaldata1.lastyear,
    vwportaldata1.dendrochronologist,
    vwportaldata1.boxid,
    vwportaldata1.boxtitle,
    vwportaldata1.curationlocation,
    vwportaldata1.trackinglocation,
    vwportaldata1.userdefinedfieldsastext,
    vwportaldata1.preferredlongitude,
    vwportaldata1.preferredlatitude,
    vwportaldata1.preferredlocationprecision,
    vwportaldata1.safepreferredlongitude,
    vwportaldata1.safepreferredlatitude,
    vwportaldata1.safepreferredlocationprecision,
    vwportaldata1.islocsensitive
   FROM portal.vwportaldata1 vwportaldata1
UNION
 SELECT vwportaldata2.projectid,
    vwportaldata2.projectlastmodifiedtimestamp,
    vwportaldata2.projecttitle,
    vwportaldata2.investigator,
    vwportaldata2.objectid,
    vwportaldata2.objectlastmodifiedtimestamp,
    vwportaldata2.objecttitle,
    vwportaldata2.objectcode,
    vwportaldata2.object2id,
    vwportaldata2.object2lastmodifiedtimestamp,
    vwportaldata2.object2title,
    vwportaldata2.object2code,
    vwportaldata2.elementid,
    vwportaldata2.elementlastmodifiedtimestamp,
    vwportaldata2.elementcode,
    vwportaldata2.taxonid,
    vwportaldata2.taxonlabel,
    vwportaldata2.taxonlabelhtml,
    vwportaldata2.sampleid,
    vwportaldata2.samplelastmodifiedtimestamp,
    vwportaldata2.samplecode,
    vwportaldata2.externalid,
    vwportaldata2.samplingdate,
    vwportaldata2.samplingyear,
    vwportaldata2.samplingmonth,
    vwportaldata2.samplingdateprec,
    vwportaldata2.sampletype,
    vwportaldata2.firstyear,
    vwportaldata2.lastyear,
    vwportaldata2.dendrochronologist,
    vwportaldata2.boxid,
    vwportaldata2.boxtitle,
    vwportaldata2.curationlocation,
    vwportaldata2.trackinglocation,
    vwportaldata2.userdefinedfieldsastext,    
    vwportaldata2.preferredlongitude,
    vwportaldata2.preferredlatitude,
    vwportaldata2.preferredlocationprecision,
    vwportaldata2.safepreferredlongitude,
    vwportaldata2.safepreferredlatitude,
    vwportaldata2.safepreferredlocationprecision,    
    vwportaldata2.islocsensitive
   FROM portal.vwportaldata2 vwportaldata2;

  
  
