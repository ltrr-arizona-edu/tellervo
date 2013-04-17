

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
     





  