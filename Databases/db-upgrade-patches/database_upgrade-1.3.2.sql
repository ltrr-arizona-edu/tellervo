
--
-- USER DEFINED FIELDS
--

CREATE TABLE public.tlkpdatatype
(
  datatype character varying NOT NULL,
  CONSTRAINT pkey_datatype PRIMARY KEY (datatype)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.tlkpdatatype
  OWNER TO tellervo;

INSERT INTO tlkpdatatype (datatype) VALUES ('xs:string');
INSERT INTO tlkpdatatype (datatype) VALUES ('xs:int');
INSERT INTO tlkpdatatype (datatype) VALUES ('xs:float');
INSERT INTO tlkpdatatype (datatype) VALUES ('xs:boolean');

CREATE TABLE public.tlkpuserdefinedfield
(
  userdefinedfieldid uuid NOT NULL DEFAULT uuid_generate_v4(),
  fieldname character varying NOT NULL,
  description character varying NOT NULL,
  attachedto integer NOT NULL, -- Integer indicated at what tridas level this field is attached.  1=project, 2=object, 3=element, 4=sample, 5=radius, 6=measurementseries
  datatype character varying NOT NULL DEFAULT 'xs:string'::character varying,
  longfieldname character varying NOT NULL,
  dictionarykey character varying,
  CONSTRAINT pkey_userdefinedfield PRIMARY KEY (userdefinedfieldid),
  CONSTRAINT "tlkpuserdefinedfields-datatype" FOREIGN KEY (datatype)
      REFERENCES public.tlkpdatatype (datatype) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uniq_userdefinedfields_fieldname UNIQUE (fieldname, attachedto),
  CONSTRAINT check_userdefinedfields_validattachedto CHECK (attachedto >= 1 AND attachedto <= 6)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.tlkpuserdefinedfield
  OWNER TO tellervo;
COMMENT ON COLUMN public.tlkpuserdefinedfield.attachedto IS 'Integer indicated at what tridas level this field is attached.  1=project, 2=object, 3=element, 4=sample, 5=radius, 6=measurementseries';


CREATE TABLE public.tbluserdefinedfieldvalue
(
  userdefinedfieldvalueid uuid NOT NULL DEFAULT uuid_generate_v4(),
  userdefinedfieldid uuid NOT NULL,
  value character varying,
  entityid uuid NOT NULL,
  CONSTRAINT pkey_userdefinedfieldvalue PRIMARY KEY (userdefinedfieldvalueid),
  CONSTRAINT "fkey_userdefinedfieldvalue-userdefinedfield" FOREIGN KEY (userdefinedfieldid)
      REFERENCES public.tlkpuserdefinedfield (userdefinedfieldid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uniq_userdefinedfieldperentity UNIQUE (userdefinedfieldid, entityid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.tbluserdefinedfieldvalue
  OWNER TO tellervo;

CREATE OR REPLACE VIEW vwtbluserdefinedfieldandvalue AS 
SELECT udfv.userdefinedfieldvalueid,
    udfv.value,
    udfv.entityid,
    udf.userdefinedfieldid,
    udf.fieldname,
    udf.description,
    udf.attachedto,
    udf.datatype,
    udf.longfieldname
   FROM tbluserdefinedfieldvalue udfv,
    tlkpuserdefinedfield udf
  WHERE udfv.userdefinedfieldid = udf.userdefinedfieldid;
  ALTER VIEW public.vwtbluserdefinedfieldandvalue
  OWNER TO tellervo;


CREATE OR REPLACE VIEW public.vwtbluserdefinedfieldandvalue AS 
 SELECT udfv.userdefinedfieldvalueid,
    udfv.value,
    udfv.entityid,
    udf.userdefinedfieldid,
    udf.fieldname,
    udf.description,
    udf.attachedto,
    udf.datatype,
    udf.longfieldname,
    udf.dictionarykey
   FROM tbluserdefinedfieldvalue udfv,
    tlkpuserdefinedfield udf
  WHERE udfv.userdefinedfieldid = udf.userdefinedfieldid;

ALTER TABLE public.vwtbluserdefinedfieldandvalue
  OWNER TO tellervo;
  
  
  CREATE TABLE public.tlkpuserdefinedterm
(
  userdefinedtermid uuid NOT NULL DEFAULT uuid_generate_v1mc(),
  term character varying NOT NULL,
  dictionarykey character varying NOT NULL,
  CONSTRAINT pkey_userdefinedterm PRIMARY KEY (userdefinedtermid),
  CONSTRAINT uniq_userdefinedtermindict UNIQUE (term, dictionarykey)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.tlkpuserdefinedterm
  OWNER TO tellervo;
  
  

-- 
-- IMPLEMENTING PROJECTS
-- 

UPDATE tblproject SET projectid='08f7f052-580f-11e5-9ab7-fb88913ca6e1' WHERE projectid='b30edb64-5a2b-11e5-9dbb-7b3f84ef785a';


DROP VIEW IF EXISTS vwtblproject;
DROP VIEW IF EXISTS vwtblobject;
DROP TABLE tlkplaboratory;


CREATE TABLE public.tbllaboratory
(
  laboratoryid uuid NOT NULL DEFAULT uuid_generate_v1mc(),
  name character varying NOT NULL,
  acronym character varying,
  address1 character varying,
  address2 character varying,
  city character varying,
  state character varying,
  postalcode character varying,
  country character varying,
  CONSTRAINT pkey_laboratory PRIMARY KEY (laboratoryid),
  CONSTRAINT uniq_labname UNIQUE (name)
)
WITH (
  OIDS=FALSE
);

grant all on tbllaboratory to tellervo;





ALTER TABLE tblproject add column projecttypes character varying[];
ALTER TABLE tblproject drop column if exists projecttypeid;
ALTER TABLE tblproject add column laboratories character varying[];




insert into tlkpsamplestatus (samplestatus) values ('Undated');


-- 
-- Enforce that an object must have either a parentobjectid or projectid
--

-- create a project that all old objects and be assigned to 
INSERT INTO tblproject (title) values ('Rename me');

-- create a dummy default project to handle old default projectid.  Deleting this later
INSERT INTO tblproject (title, projectid) VALUES ('do not use', '08f7f052-580f-11e5-9ab7-fb88913ca6e1');

-- Set projectids to the 'rename me' project
UPDATE tblobject SET projectid=(SELECT projectid FROM tblproject WHERE title='Rename me') WHERE parentobjectid IS NULL AND projectid IS NULL;
UPDATE tblobject SET projectid=(SELECT projectid FROM tblproject WHERE title='Rename me') WHERE projectid='08f7f052-580f-11e5-9ab7-fb88913ca6e1';

-- delete any unused projects
DELETE FROM tblproject WHERE projectid NOT IN (SELECT DISTINCT projectid FROM tblobject WHERE projectid IS NOT NULL);


ALTER TABLE tblobject ADD CONSTRAINT "fkey_object-project" FOREIGN KEY (projectid)
      REFERENCES public.tblproject (projectid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


CREATE OR REPLACE FUNCTION public."enforce_object-parent"()
  RETURNS trigger AS
$BODY$
BEGIN

IF NEW.parentobjectid IS NULL AND NEW.projectid IS NULL THEN
  RAISE EXCEPTION 'Objects must have either a project or parent object assigned';
  RETURN NULL;
ELSIF NEW.parentobjectid IS NOT NULL AND NEW.projectid IS NOT NULL THEN
  RAISE NOTICE 'Sub-objects cannot be assigned to a project.  Project membership is inherited through its parent object, so requested project for this object will be ignored';
  NEW.projectid:=NULL;
END IF;

RETURN NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public."enforce_object-parent"()
  OWNER TO tellervo;


CREATE TRIGGER "enforce_object-parent"
  BEFORE INSERT OR UPDATE
  ON public.tblobject
  FOR EACH ROW
  EXECUTE PROCEDURE public."enforce_object-parent"();


CREATE OR REPLACE VIEW public.vwtblobject AS 
 SELECT cquery.countofchildvmeasurements,
    o.projectid,
    o.vegetationtype,
    o.comments,
    o.objectid,
    dom.domainid,
    dom.domain,
    o.title,
    o.code,
    o.createdtimestamp,
    o.lastmodifiedtimestamp,
    o.locationgeometry,
    ( SELECT st_asgml(3, o.locationgeometry, 15, 1) AS st_asgml) AS gml,
    xmin(o.locationgeometry::box3d) AS longitude,
    ymin(o.locationgeometry::box3d) AS latitude,
    o.locationtypeid,
    o.locationprecision,
    o.locationcomment,
    o.locationaddressline1,
    o.locationaddressline2,
    o.locationcityortown,
    o.locationstateprovinceregion,
    o.locationpostalcode,
    o.locationcountry,
    array_to_string(o.file, '><'::text) AS file,
    o.creator,
    o.owner,
    o.parentobjectid,
    o.description,
    o.objecttypeid,
    loctype.locationtype,
    objtype.objecttype,
    covtemp.coveragetemporal,
    covtempfound.coveragetemporalfoundation
   FROM tblobject o
     LEFT JOIN tlkpdomain dom ON o.domainid = dom.domainid
     LEFT JOIN tlkplocationtype loctype ON o.locationtypeid = loctype.locationtypeid
     LEFT JOIN tlkpobjecttype objtype ON o.objecttypeid = objtype.objecttypeid
     LEFT JOIN tlkpcoveragetemporal covtemp ON o.coveragetemporalid = covtemp.coveragetemporalid
     LEFT JOIN tlkpcoveragetemporalfoundation covtempfound ON o.coveragetemporalfoundationid = covtempfound.coveragetemporalfoundationid
     LEFT JOIN ( SELECT e.objectid AS masterobjectid,
            count(e.objectid) AS countofchildvmeasurements
           FROM tblelement e
             JOIN tblsample s ON s.elementid = e.elementid
             JOIN tblradius r ON r.sampleid = s.sampleid
             JOIN tblmeasurement m ON m.radiusid = r.radiusid
             JOIN tblvmeasurementderivedcache vc ON vc.measurementid = m.measurementid
          GROUP BY e.objectid) cquery ON cquery.masterobjectid = o.objectid;

ALTER TABLE public.vwtblobject
  OWNER TO tellervo;


CREATE OR REPLACE VIEW public.vwtblproject AS 
 SELECT p.projectid,
    dom.domainid,
    dom.domain,
    p.title,
    array_to_string(p.projecttypes, '><'::text) AS types,
    p.createdtimestamp,
    p.lastmodifiedtimestamp,
    p.comments,
    p.description,
    array_to_string(p.file, '><'::text) AS file,
    p.investigator,
    p.period,
    p.requestdate,
    p.commissioner,
    p.reference,
    p.research,
    p.laboratories,
    p.projectcategoryid,
    pc.projectcategory
   FROM tblproject p
     LEFT JOIN tlkpdomain dom ON p.domainid = dom.domainid
     LEFT JOIN tlkpprojectcategory pc ON p.projectcategoryid = pc.projectcategoryid;

ALTER TABLE public.vwtblproject
  OWNER TO tellervo;


update tblconfig set value='1.3.2' where key='wsversion';
update tblsupportedclient set minversion='1.3.2' where client ='Tellervo WSI';
