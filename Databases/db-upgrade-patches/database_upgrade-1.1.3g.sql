CREATE SEQUENCE tlkpprojecttype_projecttype_seq
  INCREMENT 1                                            
  MINVALUE 1                             
  MAXVALUE 9223372036854775807
  START 1                                                                                     
  CACHE 1;


CREATE SEQUENCE tlkpprojectcategory_projectcategory_seq
  INCREMENT 1                                            
  MINVALUE 1                             
  MAXVALUE 9223372036854775807
  START 1                                                                                     
  CACHE 1;


CREATE SEQUENCE tlkplaboratory_laboratoryid_seq
  INCREMENT 1                                            
  MINVALUE 1                             
  MAXVALUE 9223372036854775807
  START 1                                                                                     
  CACHE 1;


CREATE TABLE tlkpprojecttype  
(                                                        
  projecttype character varying NOT NULL,
  vocabularyid integer,
  projecttypeid integer NOT NULL DEFAULT nextval('tlkpprojecttype_projecttype_seq'::regclass),
  CONSTRAINT pkey_projecttype PRIMARY KEY (projecttypeid),
  CONSTRAINT "fkey_projecttype-vocabulary" FOREIGN KEY (vocabularyid)
      REFERENCES tlkpvocabulary (vocabularyid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "tlkpprojecttype-nodupsinvocab" UNIQUE (projecttype, vocabularyid)
)                                                                  
WITH (
  OIDS=FALSE
);

CREATE TABLE tlkpprojectcategory  
(                                                        
  projectcategory character varying NOT NULL,
  vocabularyid integer,
  projectcategoryid integer NOT NULL DEFAULT nextval('tlkpprojectcategory_projectcategory_seq'::regclass),
  CONSTRAINT pkey_projectcategory PRIMARY KEY (projectcategoryid),
  CONSTRAINT "fkey_projectcategory-vocabulary" FOREIGN KEY (projectcategoryid)
      REFERENCES tlkpvocabulary (vocabularyid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "tlkpprojectcategory-nodupsinvocab" UNIQUE (projectcategory, vocabularyid)
)                                                                  
WITH (
  OIDS=FALSE
);


CREATE TABLE tlkplaboratory
(
  laboratoryid integer  NOT NULL DEFAULT nextval('tlkplaboratory_laboratoryid_seq'::regclass),
  domainid integer default 0,
  title character varying NOT NULL,
  acronynm character varying,
  CONSTRAINT pkey_laboratory PRIMARY KEY (laboratoryid),
  CONSTRAINT fkey_tlkplaboratory_tlkpdomain FOREIGN KEY (domainid)
      REFERENCES tlkpdomain (domainid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "tlkplaboratory-nodomaindups" UNIQUE (title, domainid)
);



CREATE TABLE tblproject(
    projectid uuid NOT NULL default uuid_generate_v1mc(),
    domainid integer default 0,
    title character varying NOT NULL,
    createdtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifiedtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    comments character varying,
    description character varying,
    file character varying[],
    projectcategoryid integer,
    investigator character varying,
    period character varying,
    requestDate date,
    commissioner character varying,
    reference character varying[],
    research integer,
CONSTRAINT pkey_project PRIMARY KEY (projectid),
CONSTRAINT "fkey_project-projectcategory" FOREIGN KEY (projectcategoryid)
      REFERENCES tlkpprojectcategory (projectcategoryid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
CONSTRAINT fkey_tblproject_tlkpdomain FOREIGN KEY (domainid)
      REFERENCES tlkpdomain (domainid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
CONSTRAINT "tblproject-uniqtitle" UNIQUE (title)
    
);

CREATE TABLE tblprojectprojecttype
(
  projectprojecttypeid serial NOT NULL,
  projectid uuid NOT NULL,
  projecttypeid integer NOT NULL,
  CONSTRAINT pkey_projectprojecttype PRIMARY KEY (projectprojecttypeid),
  CONSTRAINT "fkey_projectprojectype-projecttype" FOREIGN KEY (projecttypeid)
      REFERENCES tlkpprojecttype (projecttypeid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT "fkey_projectprojectype-project" FOREIGN KEY (projectid)
      REFERENCES tblproject (projectid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uniq_projectprojecttype UNIQUE (projectid, projecttypeid)
);

INSERT INTO tlkpprojectcategory (projectcategory, vocabularyid) values ('archaeology', 5);
INSERT INTO tlkpprojectcategory (projectcategory, vocabularyid) values ('built heritage', 5);
INSERT INTO tlkpprojectcategory (projectcategory, vocabularyid) values ('furniture', 5);
INSERT INTO tlkpprojectcategory (projectcategory, vocabularyid) values ('musical instrument', 5);
INSERT INTO tlkpprojectcategory (projectcategory, vocabularyid) values ('painting', 5);
INSERT INTO tlkpprojectcategory (projectcategory, vocabularyid) values ('palaeo-vegetation', 5);
INSERT INTO tlkpprojectcategory (projectcategory, vocabularyid) values ('ship archaeology', 5);
INSERT INTO tlkpprojectcategory (projectcategory, vocabularyid) values ('standing trees', 5);
INSERT INTO tlkpprojectcategory (projectcategory, vocabularyid) values ('woodcarving', 5);
INSERT INTO tlkpprojectcategory (projectcategory, vocabularyid) values ('other', 5);

INSERT INTO tlkpprojecttype (projecttype, vocabularyid) values ('anthropology', 5);
INSERT INTO tlkpprojecttype (projecttype, vocabularyid) values ('climatology', 5);
INSERT INTO tlkpprojecttype (projecttype, vocabularyid) values ('dating', 5);
INSERT INTO tlkpprojecttype (projecttype, vocabularyid) values ('ecology', 5);
INSERT INTO tlkpprojecttype (projecttype, vocabularyid) values ('entomology', 5);
INSERT INTO tlkpprojecttype (projecttype, vocabularyid) values ('forest dynamics', 5);
INSERT INTO tlkpprojecttype (projecttype, vocabularyid) values ('forest management studies', 5);
INSERT INTO tlkpprojecttype (projecttype, vocabularyid) values ('forestry', 5);
INSERT INTO tlkpprojecttype (projecttype, vocabularyid) values ('geomorphology', 5);
INSERT INTO tlkpprojecttype (projecttype, vocabularyid) values ('glaciology', 5);
INSERT INTO tlkpprojecttype (projecttype, vocabularyid) values ('hydrology', 5);
INSERT INTO tlkpprojecttype (projecttype, vocabularyid) values ('palaeo-ecology', 5);
INSERT INTO tlkpprojecttype (projecttype, vocabularyid) values ('provenancing', 5);
INSERT INTO tlkpprojecttype (projecttype, vocabularyid) values ('pyrochronology', 5);
INSERT INTO tlkpprojecttype (projecttype, vocabularyid) values ('wood biology', 5);
INSERT INTO tlkpprojecttype (projecttype, vocabularyid) values ('wood technology', 5);

INSERT INTO tblproject (title) values ('Default project');
ALTER TABLE tblobject add column projectid uuid;
UPDATE tblobject set projectid=(SELECT projectid FROM tblproject WHERE title='Default project') WHERE parentobjectid is null;
ALTER TABLE tblobject ADD CONSTRAINT enforceprojectfortopobject CHECK ((parentobjectid IS NULL AND projectid IS NOT NULL) OR (parentobjectid IS NOT NULL AND projectid IS NULL));


DROP VIEW vwtblobject;
CREATE OR REPLACE VIEW vwtblobject AS 
 SELECT cquery.countofchildvmeasurements, o.vegetationtype, o.comments, o.objectid, dom.domainid, dom.domain, o.title, o.code, o.createdtimestamp, o.lastmodifiedtimestamp, o.locationgeometry, 
 (SELECT st_asgml(3, o.locationgeometry, 15, 1)) as gml, o.locationtypeid, o.locationprecision, o.locationcomment, o.locationaddressline1, o.locationaddressline2, o.locationcityortown, o.locationstateprovinceregion, o.locationpostalcode, o.locationcountry, array_to_string(o.file, '><'::text) AS file, o.creator, o.owner, o.parentobjectid, o.description, o.objecttypeid, loctype.locationtype, objtype.objecttype, covtemp.coveragetemporal, covtempfound.coveragetemporalfoundation
   FROM tblobject o
   LEFT JOIN tlkpdomain dom ON o.domainid = dom.domainid
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

DROP VIEW vwtblelement;
CREATE OR REPLACE VIEW vwtblelement AS 
 SELECT ( SELECT findobjecttoplevelancestor.code
           FROM cpgdb.findobjecttoplevelancestor(e.objectid) 
           findobjecttoplevelancestor(objectid, title, code, createdtimestamp, lastmodifiedtimestamp, locationgeometry, locationtypeid, locationprecision, locationcomment, creator, owner, parentobjectid, description, objecttypeid, coveragetemporalid, coveragetemporalfoundationid, comments, coveragetemporal, coveragetemporalfoundation, locationaddressline1, locationaddressline2, locationcityortown, locationstateprovinceregion, locationpostalcode, locationcountry, locationcountry, vegetationtype, domainid)) AS objectcode, 
           e.comments, dom.domainid, dom.domain, e.elementid, e.locationprecision, e.code AS title, e.code, e.createdtimestamp, e.lastmodifiedtimestamp, e.locationgeometry, 
           (SELECT st_asgml(3, e.locationgeometry, 15, 1)) as gml,
           e.islivetree, e.originaltaxonname, e.locationtypeid, e.locationcomment, e.locationaddressline1, e.locationaddressline2, e.locationcityortown, e.locationstateprovinceregion, e.locationpostalcode, e.locationcountry, array_to_string(e.file, '><'::text) AS file, e.description, e.processing, e.marks, e.diameter, e.width, e.height, e.depth, e.unsupportedxml, e.objectid, e.elementtypeid, e.authenticity, e.elementshapeid, shape.elementshape, tbltype.elementtype, loctype.locationtype, e.altitude, e.slopeangle, e.slopeazimuth, e.soildescription, e.soildepth, e.bedrockdescription, vwt.taxonid, vwt.taxonlabel, vwt.parenttaxonid, vwt.colid, vwt.colparentid, vwt.taxonrank, unit.unit AS units, unit.unitid
   FROM tblelement e
   LEFT JOIN tlkpdomain dom ON e.domainid = dom.domainid
   LEFT JOIN tlkpelementshape shape ON e.elementshapeid = shape.elementshapeid
   LEFT JOIN tlkpelementtype tbltype ON e.elementtypeid = tbltype.elementtypeid
   LEFT JOIN tlkplocationtype loctype ON e.locationtypeid = loctype.locationtypeid
   LEFT JOIN tlkpunit unit ON e.units = unit.unitid
   LEFT JOIN vwtlkptaxon vwt ON e.taxonid = vwt.taxonid;



ALTER TABLE tblsample ADD COLUMN samplingyear integer;
ALTER TABLE tblsample ADD COLUMN samplingmonth integer;
ALTER TABLE tblsample ADD CONSTRAINT enforce_tblsample_sampleyearbound CHECK (samplingyear >1900);
ALTER TABLE tblsample ADD CONSTRAINT enforce_tblsample_sampleyearbound2 CHECK (samplingyear <=date_part('year', current_timestamp));
ALTER TABLE tblsample ADD CONSTRAINT enforce_tblsample_samplemonthbound CHECK (samplingmonth >=1 AND samplingmonth <=12);
ALTER TABLE tblsample ADD CONSTRAINT enforce_samplingyearwithmonth CHECK (samplingmonth IS NOT NULL AND samplingyear IS NOT NULL OR samplingmonth IS NULL);

CREATE OR REPLACE FUNCTION update_samplingdateatomicfields()
  RETURNS trigger AS
$BODY$BEGIN
IF NEW.samplingdate IS NOT NULL THEN
  NEW.samplingyear = date_part('year', NEW.samplingdate);
  NEW.samplingmonth = date_part('month', NEW.samplingdate);
END IF;
RETURN new;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_samplingdateatomicfields()
  OWNER TO tellervo;

DROP VIEW vwtblsample;
CREATE OR REPLACE VIEW vwtblsample AS 
 SELECT s.externalid, s.sampleid, s.code, s.comments, s.code AS title, s.elementid, s.samplingdate, s.createdtimestamp, s.lastmodifiedtimestamp, st.sampletypeid, st.sampletype, s.file, s."position", s.state, s.knots, s.description, dc.datecertainty, s.boxid, lc.objectcode, lc.elementcode, c.curationstatusid, cl.curationstatus, s.samplingmonth, s.samplingyear
   FROM tblsample s
   LEFT JOIN tlkpdatecertainty dc ON s.datecertaintyid = dc.datecertaintyid
   LEFT JOIN tlkpsampletype st ON s.typeid = st.sampletypeid
   LEFT JOIN vwtblcurationmostrecent c ON s.sampleid = c.sampleid
   LEFT JOIN tlkpcurationstatus cl ON c.curationstatusid = cl.curationstatusid
   LEFT JOIN vwlabcodesforsamples lc ON s.sampleid = lc.sampleid;


