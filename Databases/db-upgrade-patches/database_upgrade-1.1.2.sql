

CREATE EXTENSION pgcrypto;
ALTER TABLE tblsecurityuser ALTER COLUMN password TYPE varchar(132);



INSERT INTO tblconfig (key, value, description) VALUES ('hashAlgorithm', 'md5', 'Algorithm to use for hashing of passwords');

UPDATE tblsupportedclient set minversion='1.0.2' where client='Tellervo WSI';



--
--  Add support for FHX2 ring remarks
--
CREATE OR REPLACE FUNCTION create_defaultreadingnotestandardisedid()
  RETURNS trigger AS
$BODY$DECLARE

newuuid uuid;


BEGIN


IF NEW.vocabularyid<2 THEN
  NEW.standardisedid=NEW.readingnoteid;
  RETURN NEW;
END IF;

IF NEW.standardisedid IS NULL THEN
	newuuid = uuid_generate_v1mc();
  NEW.standardisedid=newuuid;
END IF;


RETURN NEW;


END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;



ALTER TABLE tlkpreadingnote ALTER COLUMN standardisedid TYPE varchar(100);


INSERT INTO tlkpreadingnote (note, vocabularyid, standardisedid) VALUES ('micro ring', 2, '066e34a2-a210-11e2-b5c3-9bdc8beec740');
INSERT INTO tlkpreadingnote (note, vocabularyid, standardisedid) VALUES ('locally absent ring', 2, '24ae1d4c-a210-11e2-8670-335ff0d06e0c');

Insert into tlkpvocabulary (vocabularyid, name, url) values (4, 'FHX', 'http://www.frames.gov/partner-sites/fhaes/fhaes-home/');
update tlkpvocabulary set name='Tellervo', url='http://www.tellervo.org' where name='Corina';


Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire scar - position undetermined', 4, 'U');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire injury - position undetermined', 4, 'u');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire scar in latewood', 4, 'A');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire injury in latewood', 4, 'a');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire scar in dormant position', 4, 'D');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire injury in dormant position', 4, 'd');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire scar in first third of earlywood', 4, 'E');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire injury in first third of earlywood', 4, 'e');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire scar in middle third of earlywood', 4, 'M');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire injury in middle third of earlywood', 4, 'm');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire scar in last third of earlywood', 4, 'L');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire injury in last third of earlywood', 4, 'l');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Not recording fires', 4, '.');



-- Function: cpgdb.resultnotestojson(integer[], integer[])

-- DROP FUNCTION cpgdb.resultnotestojson(integer[], integer[]);

CREATE OR REPLACE FUNCTION cpgdb.resultnotestojson(integer[], integer[])
  RETURNS text AS
$BODY$
DECLARE
   ids ALIAS FOR $1;
   counts ALIAS FOR $2;

   idx integer;
   id integer;
   count integer;

   outnote text[];
   v1 text;
   v2 text;
   v3 text;
   v4 text;
   v5 text; 

   note text;
   dbid integer;
   stdid varchar;
   vocname text;

BEGIN
   IF ids IS NULL OR counts IS NULL THEN 
      RETURN NULL;
   END IF;

   FOR idx in array_lower(ids, 1)..array_upper(ids, 1) LOOP
      id := ids[idx];
      count := counts[idx];

      SELECT replace(rnote.note, '"', E'\\"'),
	     rnote.readingnoteid,
             rnote.standardisedid,
             replace(voc.name, '"', E'\\"') 
          INTO note,dbid,stdid,vocname
          FROM tlkpReadingNote rnote
          INNER JOIN tlkpVocabulary voc ON rnote.vocabularyid=voc.vocabularyid
          WHERE rnote.readingnoteid=id;

      IF NOT FOUND THEN
         RAISE NOTICE 'Reading note id % not found', id;
         CONTINUE;
      END IF;

      v1 := '"note":"' || note || '"';



      IF stdid IS NOT NULL THEN
         v2 := '"stdid":"' || stdid || '"';
      ELSE
         v2 := '"stdid":null';
      END IF;

      v3 := '"std":"' || vocname || '"';

      v4 := '"icnt":' || count;

      v5 := '"dbid":' || dbid;

      outnote := array_append(outnote, '{' || array_to_string(ARRAY[v1, v2, v3, v4, v5], ',') || '}');
   END LOOP;

   return '[' || array_to_string(outnote, ',') || ']';
END;
$BODY$
  LANGUAGE plpgsql STABLE
  COST 100;

DROP VIEW vwtblsample;

CREATE VIEW vwtblsample AS
    SELECT s.sampleid, s.code, s.comments, s.code AS title, s.elementid, s.samplingdate, s.createdtimestamp, 
    s.lastmodifiedtimestamp, st.sampletypeid, st.sampletype, array_to_string(s.file, '><'::text) AS file, s."position", 
    s.state, s.knots, s.description, dc.datecertainty, s.boxid, lc.objectcode, lc.elementcode 
    FROM 
    (((tblsample s LEFT JOIN 
    tlkpdatecertainty dc ON ((s.datecertaintyid = dc.datecertaintyid))) LEFT JOIN tlkpsampletype st ON ((s.typeid = st.sampletypeid)))
     LEFT JOIN vwlabcodesforsamples lc ON ((s.sampleid = lc.sampleid)));

ALTER TABLE tblelement ADD COLUMN authenticity varchar;

DROP VIEW vw_tlotoradius;
DROP VIEW vw_elementtoradius;
DROP VIEW vwtblelement;

CREATE VIEW vwtblelement AS
    SELECT (SELECT findobjecttoplevelancestor.code FROM cpgdb.findobjecttoplevelancestor(e.objectid) 
    findobjecttoplevelancestor(objectid, title, code, createdtimestamp, lastmodifiedtimestamp, locationgeometry, 
    locationtypeid, locationprecision, locationcomment, creator, owner, 
    parentobjectid, description, objecttypeid, coveragetemporalid, coveragetemporalfoundationid, comments, coveragetemporal, 
    coveragetemporalfoundation, locationaddressline1, locationaddressline2, locationcityortown, locationstateprovinceregion, 
    locationpostalcode, locationcountry)) AS objectcode, e.comments, e.elementid, e.locationprecision, e.code AS title, e.code, 
    e.createdtimestamp, e.lastmodifiedtimestamp, e.locationgeometry, e.islivetree, e.originaltaxonname, e.locationtypeid, 
    e.locationcomment, e.locationaddressline1, e.locationaddressline2, e.locationcityortown, e.locationstateprovinceregion, 
    e.locationpostalcode, e.locationcountry, array_to_string(e.file, '><'::text) AS file, e.description, e.processing, e.marks, 
    e.diameter, e.width, e.height, e.depth, e.unsupportedxml, e.objectid, e.elementtypeid, e.authenticity, e.elementshapeid,  shape.elementshape, tbltype.elementtype, loctype.locationtype, e.altitude, e.slopeangle, e.slopeazimuth, 
    e.soildescription, e.soildepth, e.bedrockdescription, vwt.taxonid, vwt.taxonlabel, vwt.parenttaxonid, vwt.colid, vwt.colparentid, 
    vwt.taxonrank, unit.unit AS units, unit.unitid FROM (((((tblelement e LEFT JOIN tlkpelementshape shape ON ((e.elementshapeid = 
    shape.elementshapeid))) LEFT JOIN tlkpelementtype tbltype ON ((e.elementtypeid = tbltype.elementtypeid)))
     LEFT JOIN tlkplocationtype loctype ON ((e.locationtypeid = loctype.locationtypeid))) LEFT JOIN tlkpunit unit 
     ON ((e.units = unit.unitid))) LEFT JOIN vwtlkptaxon vwt ON ((e.taxonid = vwt.taxonid)));

CREATE VIEW vw_elementtoradius AS
    SELECT cpgdb.tloid(e.*) AS tlo_objectid, e.elementid AS e_elementid, e.taxonid AS e_taxonid, e.locationprecision AS e_locationprecision,
     e.code AS e_code, e.createdtimestamp AS e_createdtimestamp, e.lastmodifiedtimestamp AS e_lastmodifiedtimestamp, 
     e.locationgeometry AS e_locationgeometry, e.islivetree AS e_islivetree, e.originaltaxonname AS e_originaltaxonname, 
     e.locationtypeid AS e_locationtypeid, e.locationcomment AS e_locationcomment, e.file AS e_file, e.description AS e_description, 
     e.processing AS e_processing, e.marks AS e_marks, e.diameter AS e_diameter, e.width AS e_width, e.height AS e_height, 
     e.depth AS e_depth, e.unsupportedxml AS e_unsupportedxml, e.unitsold AS e_units, e.elementtypeid AS e_elementtypeid, 
     e.authenticity AS e_authenticity, e.elementshapeid AS e_elementshapeid, e.altitudeint AS e_altitudeint, e.slopeangle AS e_slopeangle, 
     e.slopeazimuth AS e_slopeazimuth, e.soildescription AS e_soildescription, e.soildepth AS e_soildepth, 
     e.bedrockdescription AS e_bedrockdescription, e.comments AS e_comments, e.locationaddressline2 AS e_locationaddressline2, 
     e.locationcityortown AS e_locationcityortown, e.locationstateprovinceregion AS e_locationstateprovinceregion, 
     e.locationpostalcode AS e_locationpostalcode, e.locationcountry AS e_locationcountry, e.locationaddressline1 AS e_locationaddressline1,
     e.altitude AS e_altitude, e.gispkey AS e_gispkey, s.sampleid AS s_sampleid, s.code AS s_code, s.samplingdate AS s_samplingdate, 
     s.createdtimestamp AS s_createdtimestamp, s.lastmodifiedtimestamp AS s_lastmodifiedtimestamp, s.type AS s_type, 
     s.identifierdomain AS s_identifierdomain, s.file AS s_file, s."position" AS s_position, s.state AS s_state, s.knots AS s_knots, 
     s.description AS s_description, s.datecertaintyid AS s_datecertaintyid, s.typeid AS s_typeid, s.boxid AS s_boxid, 
     s.comments AS s_comments, r.radiusid AS r_radiusid, r.code AS r_code, r.createdtimestamp AS r_createdtimestamp, 
     r.lastmodifiedtimestamp AS r_lastmodifiedtimestamp, r.numberofsapwoodrings AS r_numberofsapwoodrings, r.pithid AS r_pithid, 
     r.barkpresent AS r_barkpresent, r.lastringunderbark AS r_lastringunderbark, r.missingheartwoodringstopith AS r_missingheartwoodringstopith, 
     r.missingheartwoodringstopithfoundation AS r_missingheartwoodringstopithfoundation, 
     r.missingsapwoodringstobark AS r_missingsapwoodringstobark, 
     r.missingsapwoodringstobarkfoundation AS r_missingsapwoodringstobarkfoundation, r.sapwoodid AS r_sapwoodid, 
     r.heartwoodid AS r_heartwoodid, r.azimuth AS r_azimuth, r.comments AS r_comments, 
     r.lastringunderbarkpresent AS r_lastringunderbarkpresent, r.nrofunmeasuredinnerrings AS r_nrofunmeasuredinnerrings, 
     r.nrofunmeasuredouterrings AS r_nrofunmeasuredouterrings 
     FROM ((tblelement e 
     LEFT JOIN tblsample s ON ((e.elementid = s.elementid))) 
     LEFT JOIN tblradius r ON ((s.sampleid = r.sampleid)));

CREATE VIEW vw_tlotoradius AS
    SELECT tlo.title AS tlo_title, tlo.code AS tlo_code, tlo.createdtimestamp AS tlo_createdtimestamp, 
    tlo.lastmodifiedtimestamp AS tlo_lastmodifiedtimestamp, tlo.locationgeometry AS tlo_locationgeometry, 
    tlo.locationtypeid AS tlo_locationtypeid, tlo.locationprecision AS tlo_locationprecision, 
    tlo.locationcomment AS tlo_locationcomment, tlo.file AS tlo_file, tlo.creator AS tlo_creator, 
    tlo.owner AS tlo_owner, tlo.parentobjectid AS tlo_parentobjectid, tlo.description AS tlo_description, 
    tlo.objecttypeid AS tlo_objecttypeid, tlo.coveragetemporalid AS tlo_coveragetemporalid, 
    tlo.coveragetemporalfoundationid AS tlo_coveragetemporalfoundationid, tlo.comments AS tlo_comments, 
    tlo.coveragetemporal AS tlo_coveragetemporal, tlo.coveragetemporalfoundation AS tlo_coveragetemporalfoundation, 
    tlo.locationaddressline1 AS tlo_locationaddressline1, tlo.locationaddressline2 AS tlo_locationaddressline2, 
    tlo.locationcityortown AS tlo_locationcityortown, tlo.locationstateprovinceregion AS tlo_locationstateprovinceregion, 
    tlo.locationpostalcode AS tlo_locationpostalcode, tlo.locationcountry AS tlo_locationcountry, others.tlo_objectid, 
    others.e_elementid, others.e_taxonid, others.e_locationprecision, others.e_code, others.e_createdtimestamp, 
    others.e_lastmodifiedtimestamp, others.e_locationgeometry, others.e_islivetree, others.e_originaltaxonname, 
    others.e_locationtypeid, others.e_locationcomment, others.e_file, others.e_description, others.e_processing, 
    others.e_marks, others.e_diameter, others.e_width, others.e_height, others.e_depth, others.e_unsupportedxml, 
    others.e_units, others.e_elementtypeid, others.e_authenticity, others.e_elementshapeid, others.e_altitudeint, 
    others.e_slopeangle, others.e_slopeazimuth, others.e_soildescription, others.e_soildepth, others.e_bedrockdescription, 
    others.e_comments, others.e_locationaddressline2, others.e_locationcityortown, others.e_locationstateprovinceregion, 
    others.e_locationpostalcode, others.e_locationcountry, others.e_locationaddressline1, others.e_altitude, 
    others.e_gispkey, others.s_sampleid, others.s_code, others.s_samplingdate, others.s_createdtimestamp, 
    others.s_lastmodifiedtimestamp, others.s_type, others.s_identifierdomain, others.s_file, others.s_position, 
    others.s_state, others.s_knots, others.s_description, others.s_datecertaintyid, others.s_typeid, others.s_boxid, 
    others.s_comments, others.r_radiusid, others.r_code, others.r_createdtimestamp, others.r_lastmodifiedtimestamp, 
    others.r_numberofsapwoodrings, others.r_pithid, others.r_barkpresent, others.r_lastringunderbark, others.r_missingheartwoodringstopith, 
    others.r_missingheartwoodringstopithfoundation, others.r_missingsapwoodringstobark, others.r_missingsapwoodringstobarkfoundation, 
    others.r_sapwoodid, others.r_heartwoodid, others.r_azimuth, others.r_comments, others.r_lastringunderbarkpresent, 
    others.r_nrofunmeasuredinnerrings, others.r_nrofunmeasuredouterrings 
    	FROM (tblobject tlo LEFT JOIN vw_elementtoradius others ON ((others.tlo_objectid = tlo.objectid)));
     



ALTER TABLE tblsample ADD COLUMN externalid varchar;

DROP VIEW vwtblsample;

CREATE OR REPLACE VIEW vwtblsample AS 
 SELECT s.sampleid, s.code, s.comments, s.code AS title, s.elementid, s.samplingdate, s.createdtimestamp, s.lastmodifiedtimestamp, st.sampletypeid, st.sampletype, array_to_string(s.file, '><'::text) AS file, s."position", s.externalid, s.state, s.knots, s.description, dc.datecertainty, s.boxid, lc.objectcode, lc.elementcode
   FROM tblsample s
   LEFT JOIN tlkpdatecertainty dc ON s.datecertaintyid = dc.datecertaintyid
   LEFT JOIN tlkpsampletype st ON s.typeid = st.sampletypeid
   LEFT JOIN vwlabcodesforsamples lc ON s.sampleid = lc.sampleid;

ALTER TABLE tblobject ADD COLUMN vegetationtype varchar;

DROP VIEW vwtblobject;

CREATE OR REPLACE VIEW vwtblobject AS 
 SELECT cquery.countofchildvmeasurements, o.vegetationtype, o.comments, o.objectid, o.title, o.code, o.createdtimestamp, o.lastmodifiedtimestamp, o.locationgeometry, o.locationtypeid, o.locationprecision, o.locationcomment, o.locationaddressline1, o.locationaddressline2, o.locationcityortown, o.locationstateprovinceregion, o.locationpostalcode, o.locationcountry, array_to_string(o.file, '><'::text) AS file, o.creator, o.owner, o.parentobjectid, o.description, o.objecttypeid, loctype.locationtype, objtype.objecttype, covtemp.coveragetemporal, covtempfound.coveragetemporalfoundation
   FROM tblobject o
   LEFT JOIN tlkplocationtype loctype ON o.locationtypeid = loctype.locationtypeid
   LEFT JOIN tlkpobjecttype objtype ON o.objecttypeid = objtype.objecttypeid
   LEFT JOIN tlkpcoveragetemporal covtemp ON o.coveragetemporalid = covtemp.coveragetemporalid
   LEFT JOIN tlkpcoveragetemporalfoundation covtempfound ON o.coveragetemporalfoundationid = covtempfound.coveragetemporalfoundationid
   LEFT JOIN ( SELECT e.objectid AS masterobjectid, count(e.objectid) AS countofchildvmeasurements
   FROM tblelement e
   JOIN tblsample s ON s.elementid = e.elementid
   JOIN tblradius r ON r.sampleid = s.sampleid
   JOIN tblmeasurement m ON m.radiusid = r.radiusid
   JOIN tblvmeasurementderivedcache vc ON vc.measurementid = m.measurementid
  GROUP BY e.objectid) cquery ON cquery.masterobjectid = o.objectid;

INSERT INTO tlkpvocabulary (vocabularyid, name, url) VALUES (5, 'DCCD', 'http://www.dendrochronology.eu');

ALTER TABLE tlkpobjecttype ADD CONSTRAINT "tlkpobjecttype-nodupsinvocab" UNIQUE (objecttype, vocabularyid);

INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, '(tree-)trunk');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'abbey');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'abbey church');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'abutment');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'aft');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'after deck');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'aisle');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'altar');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'altarpiece');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'amidships');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'anchor');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'anchor beam');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'anchor bolster');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'anchor stock');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'annex');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'arch');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'arch brace');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'arrow shaft');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'ashlar piece');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'ashlar post');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'attic');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'back (side)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'back aisle');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'back room');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'backstay');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bad bevel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'baffle(-board)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bailey');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'baldachin');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'balustrade');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bar');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'barge');	
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bark, barque');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'barkentine');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'barn');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'barquetine');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'barrack');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'barracks');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'barrel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'base');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bass');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bastion');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'batten');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'battening');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bay');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'beam');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'beam end');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bed');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bee block');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'belaying');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'belaying pin');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'belaying rack');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'belfry');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'best room');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bibb');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bilge');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bilge gutter');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bilge stringer');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bilge water');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bin');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'binder');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'binding strake');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'binnacle');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bitt');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'black strake');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'blade');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'blanket chest');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'block');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'board');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'boarding');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'boaster');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'boat');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'boat boom');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bollard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bolster');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bolt');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'boom');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'boom cradle');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'boom crutch');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'boomkin');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'border joist');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bosse');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bottom');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bottom first');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bottom plank');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bottom strake');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bottom, floor');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bow');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bowl');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bowsprit');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bowsprit bees');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'box');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'box beam');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'brace');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bracket');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'brake');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'branch');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'branching');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'breasthook, breast knee');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bridge');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bridge head');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bridging');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bridging joist');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'brig');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'brigantine');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'brook');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'buckler');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'building');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bulkhead');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bulwark');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bumkin');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'bumpkin');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'burial pit');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'buttress');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cabinet');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'camber');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'campanile');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'campshed(ding)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'campsheeting');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'campshot');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'canal');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'canalboat');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'canoe');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'canopy');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cap');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'capping');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'capstan');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cargo vessel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'carling');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'carrack');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'carriage');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'carvel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'carvelbuilt');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'case');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'case furniture');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'castel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'castellum');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'castle');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cat');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cat block');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cat kevel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cathead');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'caulkage');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'caulking');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'caulking mallet');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'caulking material');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'caulking method');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'caulking nail');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'ceiling');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'ceiling plank');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cellar');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cello');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cemetery');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'central post');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'central vertical post');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cesspit');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cesspool');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'chain plate');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'chair');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'chairleg');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'chamber');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'chancel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'channel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'channel capping strip');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'channel knee');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'channel wale (frigate)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'chapel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cheek');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'chest');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'chevet');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'chine');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'chine block');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'chine cleat');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'chine-block');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'chock');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'choir');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'choir stall');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'church');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'churchtower');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'churchyard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'city');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'city hall');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'clamp');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cleat');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'clinker');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'clinker built');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'clock');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'closet');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'clout nail');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'coach');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'coaming');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'coating');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cockloft');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'coffin');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cog');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cog ship');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'collar');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'collar beam');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'collarpurlin');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'common joist');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'common rafter');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'common rafter roof');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'console');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'console table');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'construction cleat');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'constructional section');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'contrabass');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'convent');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'corbel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'corbel piece');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'corn-mill');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'corner rafter');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cornice');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'corona');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'corridor');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'counter');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'counter brace');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'counter timber');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'country estate');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'country house');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cove');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'covering board');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'crafts house');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'crate');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'creek');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'creel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'crock');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cross beam');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cross brace');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cross tree');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'crossbeam');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'crossbridging');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'crossbridgings');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'crossing');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'crossjack yard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'crown');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'crown of beam');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'crutch');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cuddy');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cult place');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'culvert');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cupboard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'curved brace');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cutter');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'cutting');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'dais');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'dam');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'darby');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'dead rise');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'deadeye');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'deadeye chain');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'deadwood');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'deal');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'deck');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'deckbeam, joist');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'deckhouse');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'deckplank');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'defence wall');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'defensive wall');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'defensive work');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'dike');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'disc wheel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'dish');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'ditch');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'dollhouse');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'dolphin');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'door');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'door frame');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'door head');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'door jamb');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'door post');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'doorstep');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'doorway');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'double bass');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'dowel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'drainage sluice');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'drawer');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'driven pile');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'dugout');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'dull edge');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'dutch bombé chest');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'dwelling');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'enclosure');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'ensign staff');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'entrance hall');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'entrenchment');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'euphroe');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'excavation');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'facade');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'facing');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'faintail');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'falsework');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'fantail');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'farmhouse');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'fashion frame');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'fashion piece');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'feature');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'fender');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'fife rail');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'figurehead');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'filler');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'fireplace');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'first futtock');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'fish davit');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'fish well');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'fishing weir');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'fittings');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'flank');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'flanking aisle');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'flat');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'flat bottom [gb]');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'flat bottomed vessel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'flat roof');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'flat-bottomed vessel/boat');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'flatboat');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'floor');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'floor ceiling');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'floor frame');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'floor joist');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'floor joists');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'floor timber (straight)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'floor timber (v-shaped)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'floor timber knee');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'floortimber');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'flour mill');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'flush planking');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'flute');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'flute ship');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'footboard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'fore channel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'fore lower mast');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'fore top');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'fore topgallant mast');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'fore topgallant yard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'fore topmast');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'fore topsail yard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'fore yard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'forecastle');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'formwork');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'fortification');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'fortress');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'forward deck');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'foundation');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'foundation hole');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'foundation pile');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'frame');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'frame first');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'frame spacing');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'framed building');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'fraterhouse');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'freighter');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'frieze');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'frigate');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'front facade');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'furniture');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'furring rafter');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'futtock');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'futtock rider');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'gaff');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'galeas');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'galley');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'galliot');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'gallow');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'gallows-field');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'gallows-lee');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'gangway');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'garboard strake');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'gatehouse');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'gateway');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'girder');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'granary');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'grating');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'grave');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'grave field');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'graving piece');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'guard post');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'guitar');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'gunwale');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hagboat');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'half frame');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'half timber work');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hall');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hallway');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hammerbeam truss');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'handle');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'handrail');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hanging truss');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hatch');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hatch coaming');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hauling');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'haunche');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hawse hole');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hawse hole bolster');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hawse hole lining');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'head');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'head cheek');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'head parapet');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'head rail');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'head timber');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'header');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'headledge');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'heart');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hearth');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'heel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'heel beam cap');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'heeling');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'helve');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'herve');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hillock');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hip rafter');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hip roof');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hog');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hogged vessel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hold');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hold rider');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hole');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'homeward bounder');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hooker');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'horreum');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hospital');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hound');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'house');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'house plan');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'howker');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hub');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hull');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hull plank');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hull strake');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'hut');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'icon');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'inboard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'inclined clamp');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'infrastructure');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'internal');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'interweaving');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'inwale');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'inwale, stringer');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'jack rafter');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'jackstaff');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'jamb');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'jibboom');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'jibboom saddle');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'jig');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'joint');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'joist');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'joisting');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'jolly');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'jolly boat');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'keel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'keel beam');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'keel plank');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'keelson');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'keep');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'kevel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'kevel bitt');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'kevel-head');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'king plank');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'king post');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'kingpost');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'knee');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'knee of the head');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'knife');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'knighthead');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'knoll');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'knuckle frame');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'knuckle framed ship');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'l-shaped chine');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'l-shaped covering board');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'ladder');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'ladder side');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'lance');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'land abutment');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'landing');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'landing place');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'landing stage');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'lap strake');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'larboard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'larboard side');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'lath');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'latrine');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'launch');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'leaf');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'ledge');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'leg of a table');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'lid');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'lierne rib');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'limber hole, watercourse');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'limberpassage');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'limbers');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'line of flotation');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'link beam');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'lintel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'list');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'lock');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'locker');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'loft');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'log cabin construction');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'long floating rule');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'long tackle block');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'long-case clock');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'longboat');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'longitudinal bracing');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'longitudinal stiffener');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'lookout tower');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'lower façade');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'lower front');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'lute');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'lyre');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'main boom');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'main channel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'main deck');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'main lower mast');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'main rail timber head');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'main top');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'main topgallant mast');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'main topgallant yard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'main topmast');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'main topsail yard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'main wale');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'main yard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'manor');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'manor house');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'manorial farm');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mansion');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mast');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mast beam');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mast cleat');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mast fid');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mast frame');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mast heel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mast step');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mast thwart');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mast truck');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mast woolding');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mat work');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'merchant vessel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mezzanine');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'military camp');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mill');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mill wheel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'miscellaneous finds');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mizzen channel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mizzen lower mast');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mizzen stool');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mizzen top');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mizzen topgallant mast');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mizzen topgallant yard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mizzen topmast');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mizzen topsail yard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mizzen yard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'moat');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'modillion');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'monastery');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mortice');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mortice-and-tenon');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mortise');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mortise-and-tenon (joint)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mortise-and-tenon joint');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'motte');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mould');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'moulding');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'mullion');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'museum');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'musical instrument');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'nail');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'nail (small)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'nail (wooden)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'nail dowel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'nail hole');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'narrow boat');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'nave');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'newel (post)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'newel-post stair');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'oar');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'oar lock');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'oar port');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'organ');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'ornament');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'orphanage');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'outer bailey');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'outer hull');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'outlet sluice');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'outside ladder');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'overlap');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'packet, packet boat');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'paddle');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'paddle wheel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'painting');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'palace');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'palisade');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'pane');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'panel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'paneling');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'panelling');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'pantry');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'parral');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'parral rib');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'parral truck');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'parrel rib');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'parsonage');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'partition');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'partition wall');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'passage(way)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'passing brace');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'pavillion');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'peg');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'philibert roof');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'philibert truss');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'piece of furniture');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'pier');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'pilaster');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'pile');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'piling');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'pillar, stanchion');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'pink');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'pinnace');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'pit');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'plaiting');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'plank');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'planking');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'planksheer');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'plaque');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'plate');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'platform');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'poop deck');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'port');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'port lid');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'port side');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'portal');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'post');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'post hole');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'post-mill');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'praam');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'pram');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'principal rafter');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'prow block');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'prow block (stem)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'prow block (stern)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'pulley');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'pulpit');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'pump');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'pump dale');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'puncheon');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'punt');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'punt-like vessel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'punting');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'purlin');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'purlin roof');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'putlog');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'quarter');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'quarter bar');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'quarter deck');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'quarter gallery');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'quay');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'queenpost');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'quiver');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'quoin');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'rabbet');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'rafter');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'rafter single roof');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'rail timber');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'railing');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'raking');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'ram');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'rampart');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'rear (side)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'rear part');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'rebate');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'redoubt');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'refectory');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'refuge hill');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'refugium');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'relief');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'reredos');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'residence');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'retabel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'revetment');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'rib');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'ribband');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'rider');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'ridge');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'ridge (pole/beam/piece)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'ridge (purlin)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'ridge purlin');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'ridge turret');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'ridge-beam');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'ridgepiece');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'rig');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'rigging');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'rising floor timber');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'road');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'rod');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'rood gallery');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'rood loft');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'rood screen');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'roof');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'roof boarding');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'roof construction');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'roof framing');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'roof hip');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'roof plate');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'roof timber');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'roof truss(es)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'roof turret');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'roof-beam');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'rooftop');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'room');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'root');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'root system');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'round bottom');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'round bottomed vessel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'round house');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'roundup');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'rowing');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'rowlock');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'rudder');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'running-board');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'rustic work');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'rustication');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sacristy');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sailing gear');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'saint andrew''s cross');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sanctuary');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sap wood');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'scabbard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'scantling');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'scarf');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'scarf joint');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'school');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'school building');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'scissors brace');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sconse');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'scoop');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'scull');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sculpture');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'scupper');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'seam');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sentry');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'set of frames');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'settlement');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'shape');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sheat');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sheathing');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'shed');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sheer');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sheer rail');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sheers');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sheet piling');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sheeting');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sheeting board');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'shell');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'shell first');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'shield');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'shingle');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'ship');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'ship shape');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'ship type');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'ship’s shell');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'ship’s side');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'shipbuilding yard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'shipyard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'shoe');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'shore');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'shovel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'shroud');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'side');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'side aisle');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'side strake');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sill');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'skeleton');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'skylight');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'slide (for carronade carriage)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sling brace');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'slingpiece');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'slipway');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sloop');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sluice');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sole piece');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'soleplate');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'solid newel stair');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'soulace');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sound box');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'soundboard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sounding board');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'spade');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'spar');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'spiral stair');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'spire');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'spire mast');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'spirketting');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'spits');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'splayed indent scarf');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'splayed joint with part abutments');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'splayed scarf-joint');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'splayed-and-tabled');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'spoon');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sprit');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sprit topsail yard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'spritsail yard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'spur (tie)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'square sterned ship');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stable');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stair');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stair tower');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'staircase');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'staircase turret');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stanchion');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'starboard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'starboard side');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'statue');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'statue of the virgin mary');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stave');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stay');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stealer');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'steeple');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'steering gear');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'steering oar');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'steering wheel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stem');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stem (fore)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stem post (fore)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stemson');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'step');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'step (in ladder)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stern');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stern (aft)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stern balcony');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stern outboard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stern post (aft)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stern transom');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stern wing');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stern-');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sternson');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stiffening');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stop log');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'storehouse');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'storey');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'story');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'strair');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'strake');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stream');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'string');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stringer');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'strip');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'structure');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'strut');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stud');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stump');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'stunsail boom');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'summer');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'summerbeam');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'summerhouse');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'superimposed tiebeam');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'supporting beam');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'supporting statue');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'sweep');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'swim-head');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'synagogue');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'table');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'table-leg');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'tabling');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'taffrail');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'taffrail figure');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'taffrail medallion');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'taffrail side figure');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'tapered cheek');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'template');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'temple');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'tenon');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'terp');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'thole');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'three-decker');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'threshold');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'thwart');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'thwart stanchion');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'tie');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'tie beam');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'tie-beam support');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'tier');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'tile lath');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'tiller');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'timber framing');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'timber stockade');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'timbering');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'tomb');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'tool mark');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'top rim');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'topgallant royal mast');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'tower');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'tower mill');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'town');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'town hall');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'town wall');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'toys');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'trader');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'trading ship/vessel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'trailboard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'transept');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'transom');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'transom (bar)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'transom side figure');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'transom timber');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'transom(bar)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'transverse timber');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'tray');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'tread');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'tree-stump');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'trenail');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'trench');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'trestle tree');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'trimmer');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'trimming');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'trimming rafter');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'triptych');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'truck (for gun cariage)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'trunk');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'truss');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'truss beam');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'truss post');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'truss tie');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'tub');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'tun');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'underrafter');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'unknown');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'uphroe');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'upper channel wale (ship of the line)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'upper deck');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'upper door');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'upright extension');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'valley');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'valley rafter');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'vallum');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'vat');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'vault');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'vehicle');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'vestry');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'vicarage');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'villa (rustica)');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'viola');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'violin');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'wainscot');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'wainscoting');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'wale');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'wall');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'wall piece');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'wall plate');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'wall post');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'wane');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'warehouse');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'warship');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'wash board');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'wash strake');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'waste pit');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'watch');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'watchtower');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'water carrier');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'water conduit');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'water hoy');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'water mill');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'water pipe');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'water well');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'water wheel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'watercastle');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'waterway');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'wattle');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'way');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'weapon');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'wedge');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'weighhouse');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'well');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'wharf');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'wheel');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'whelp');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'whip');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'whipstaff');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'winch');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'windbrace');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'windmill');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'window');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'window head');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'wing transom');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'wooden construction');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'woodroll');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'yacht');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'yard');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'yard cleat');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'yawl');
INSERT INTO tlkpobjecttype (vocabularyid, objecttype) VALUES (5, 'yoke');

ALTER TABLE tlkpelementtype ADD COLUMN vocabularyid INTEGER;
ALTER TABLE tlkpelementtype ADD CONSTRAINT "fkey_elementtype-vocabulary" FOREIGN KEY (vocabularyid) 
REFERENCES tlkpvocabulary (vocabularyid) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

UPDATE TABLE tlkpelementtype SET vocabularyid=2 where vocabularyid IS NULL;
UPDATE tlkpelementtype SET vocabularyid=0 where elementtype='[Custom]';



CREATE TABLE tblloan
(
  loanid uuid NOT NULL,
  firstname character varying,
  lastname character varying,
  organisation character varying,
  duedate timestamp with time zone,
  issuedate timestamp with time zone,
  files character varying[],
  notes character varying,
  CONSTRAINT pkey_tblloan PRIMARY KEY (loanid)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE tlkpcurationstatus
(
  curationstatusid serial NOT NULL,
  curationstatus character varying,
  CONSTRAINT pkey_curationstatus PRIMARY KEY (curationstatusid)
)
WITH (
  OIDS=FALSE
);


CREATE TABLE tblcuration
(
  curationid bigserial NOT NULL,
  curationstatusid integer NOT NULL,
  sampleid uuid NOT NULL,
  createdtimestamp timestamp with time zone NOT NULL DEFAULT now(),
  curatorid integer NOT NULL,
  loanid uuid,
  notes character varying,
  storagelocation character varying,
  CONSTRAINT pkey_tblcuration PRIMARY KEY (curationid),
  CONSTRAINT "fkey_tblcuration-tblloan" FOREIGN KEY (loanid)
      REFERENCES tblloan (loanid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "fkey_tblcuration-tblsample" FOREIGN KEY (sampleid)
      REFERENCES tblsample (sampleid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "fkey_tblcuration-tblsecurityuser" FOREIGN KEY (curatorid)
      REFERENCES tblsecurityuser (securityuserid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "fkey_tblcuration-tlkpcurationstatus" FOREIGN KEY (curationstatusid)
      REFERENCES tlkpcurationstatus (curationstatusid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
  CONSTRAINT "uniq_curationstatus" UNIQUE (curationstatus)
)
WITH (
  OIDS=FALSE
);

INSERT INTO tlkpcurationstatus (curationstatus) VALUES ('Archived');
INSERT INTO tlkpcurationstatus (curationstatus) VALUES ('On loan (internal)');
INSERT INTO tlkpcurationstatus (curationstatus) VALUES ('On loan (external)');
INSERT INTO tlkpcurationstatus (curationstatus) VALUES ('Missing');
INSERT INTO tlkpcurationstatus (curationstatus) VALUES ('On display');
INSERT INTO tlkpcurationstatus (curationstatus) VALUES ('Destroyed');
INSERT INTO tlkpcurationstatus (curationstatus) VALUES ('Returned to owner');
INSERT INTO tlkpcurationstatus (curationstatus) VALUES ('Active research');
INSERT INTO tlkpcurationstatus (curationstatus) VALUES ('Data only');

  