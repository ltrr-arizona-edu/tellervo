
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
insert into tlkpsamplestatus (samplestatus) values ('Partially dated');


-- 
-- Enforce that an object must have either a parentobjectid or projectid
--

-- create a project that all old objects and be assigned to 
INSERT INTO tblproject (title) values ('Rename me');

-- create a dummy default project to handle old default projectid.  Deleting this later
-- INSERT INTO tblproject (title, projectid) VALUES ('do not use', '08f7f052-580f-11e5-9ab7-fb88913ca6e1');
INSERT INTO tblproject (projectid, title)
SELECT '08f7f052-580f-11e5-9ab7-fb88913ca6e1', 'do not use'
WHERE
  NOT EXISTS (
  	SELECT projectid FROM tblproject WHERE projectid='08f7f052-580f-11e5-9ab7-fb88913ca6e1'
  );


-- Set projectids to the 'rename me' project
DROP TRIGGER update_object_rebuildmetacache ON public.tblobject;
UPDATE tblobject SET projectid=(SELECT projectid FROM tblproject WHERE title='Rename me') WHERE parentobjectid IS NULL AND projectid IS NULL;
UPDATE tblobject SET projectid=(SELECT projectid FROM tblproject WHERE title='Rename me') WHERE projectid='08f7f052-580f-11e5-9ab7-fb88913ca6e1';


-- delete any unused projects
DELETE FROM tblproject WHERE projectid NOT IN (SELECT DISTINCT projectid FROM tblobject WHERE projectid IS NOT NULL);

CREATE TRIGGER update_object_rebuildmetacache                                                                                                                  
  AFTER INSERT OR UPDATE                                                                                                                                       
  ON public.tblobject                                                                                                                                          
  FOR EACH ROW                                                                                                                                                 
  EXECUTE PROCEDURE cpgdb.rebuildmetacacheforobject();


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
    st_xmin(o.locationgeometry::box3d) AS longitude,
    st_ymin(o.locationgeometry::box3d) AS latitude,
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

  
ALTER SEQUENCE tlkpelementtype_elementtypeid_seq RESTART WITH 6;
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, '(tree-)trunk');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'abbey');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'abbey church');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'abutment');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'aft');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'after deck');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'aisle');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'altar');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'altarpiece');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'amidships');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'anchor');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'anchor beam');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'anchor bolster');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'anchor stock');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'annex');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'arch');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'arch brace');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'arrow shaft');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'ashlar piece');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'ashlar post');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'attic');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'back (side)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'back aisle');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'back room');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'backstay');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bad bevel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'baffle(-board)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bailey');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'baldachin');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'balustrade');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bar');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'barge');	
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bark, barque');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'barkentine');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'barn');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'barquetine');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'barrack');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'barracks');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'barrel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'base');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bass');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bastion');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'batten');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'battening');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bay');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'beam');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'beam end');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bed');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bee block');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'belaying');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'belaying pin');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'belaying rack');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'belfry');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'best room');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bibb');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bilge');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bilge gutter');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bilge stringer');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bilge water');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bin');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'binder');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'binding strake');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'binnacle');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bitt');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'black strake');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'blade');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'blanket chest');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'block');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'board');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'boarding');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'boaster');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'boat');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'boat boom');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bollard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bolster');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bolt');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'boom');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'boom cradle');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'boom crutch');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'boomkin');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'border joist');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bosse');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bottom');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bottom first');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bottom plank');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bottom strake');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bottom, floor');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bow');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bowl');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bowsprit');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bowsprit bees');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'box');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'box beam');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'brace');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bracket');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'brake');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'branch');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'branching');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'breasthook, breast knee');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bridge');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bridge head');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bridging');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bridging joist');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'brig');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'brigantine');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'brook');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'buckler');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'building');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bulkhead');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bulwark');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bumkin');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'bumpkin');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'burial pit');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'buttress');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cabinet');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'camber');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'campanile');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'campshed(ding)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'campsheeting');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'campshot');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'canal');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'canalboat');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'canoe');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'canopy');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cap');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'capping');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'capstan');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cargo vessel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'carling');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'carrack');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'carriage');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'carvel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'carvelbuilt');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'case');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'case furniture');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'castel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'castellum');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'castle');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cat');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cat block');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cat kevel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cathead');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'caulkage');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'caulking');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'caulking mallet');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'caulking material');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'caulking method');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'caulking nail');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'ceiling');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'ceiling plank');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cellar');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cello');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cemetery');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'central post');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'central vertical post');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cesspit');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cesspool');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'chain plate');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'chair');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'chairleg');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'chamber');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'chancel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'channel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'channel capping strip');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'channel knee');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'channel wale (frigate)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'chapel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cheek');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'chest');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'chevet');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'chine');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'chine block');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'chine cleat');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'chine-block');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'chock');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'choir');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'choir stall');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'church');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'churchtower');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'churchyard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'city');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'city hall');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'clamp');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cleat');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'clinker');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'clinker built');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'clock');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'closet');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'clout nail');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'coach');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'coaming');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'coating');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cockloft');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'coffin');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cog');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cog ship');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'collar');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'collar beam');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'collarpurlin');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'common joist');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'common rafter');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'common rafter roof');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'console');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'console table');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'construction cleat');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'constructional section');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'contrabass');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'convent');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'corbel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'corbel piece');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'corn-mill');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'corner rafter');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cornice');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'corona');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'corridor');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'counter');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'counter brace');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'counter timber');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'country estate');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'country house');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cove');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'covering board');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'crafts house');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'crate');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'creek');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'creel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'crock');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cross beam');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cross brace');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cross tree');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'crossbeam');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'crossbridging');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'crossbridgings');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'crossing');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'crossjack yard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'crown');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'crown of beam');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'crutch');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cuddy');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cult place');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'culvert');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cupboard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'curved brace');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cutter');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'cutting');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'dais');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'dam');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'darby');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'dead rise');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'deadeye');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'deadeye chain');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'deadwood');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'deal');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'deck');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'deckbeam, joist');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'deckhouse');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'deckplank');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'defence wall');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'defensive wall');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'defensive work');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'dike');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'disc wheel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'dish');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'ditch');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'dollhouse');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'dolphin');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'door');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'door frame');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'door head');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'door jamb');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'door post');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'doorstep');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'doorway');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'double bass');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'dowel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'drainage sluice');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'drawer');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'driven pile');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'dugout');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'dull edge');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'dutch bombé chest');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'dwelling');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'enclosure');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'ensign staff');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'entrance hall');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'entrenchment');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'euphroe');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'excavation');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'facade');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'facing');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'faintail');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'falsework');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'fantail');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'farmhouse');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'fashion frame');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'fashion piece');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'feature');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'fender');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'fife rail');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'figurehead');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'filler');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'fireplace');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'first futtock');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'fish davit');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'fish well');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'fishing weir');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'fittings');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'flank');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'flanking aisle');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'flat');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'flat bottom [gb]');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'flat bottomed vessel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'flat roof');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'flat-bottomed vessel/boat');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'flatboat');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'floor');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'floor ceiling');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'floor frame');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'floor joist');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'floor joists');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'floor timber (straight)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'floor timber (v-shaped)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'floor timber knee');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'floortimber');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'flour mill');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'flush planking');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'flute');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'flute ship');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'footboard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'fore channel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'fore lower mast');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'fore top');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'fore topgallant mast');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'fore topgallant yard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'fore topmast');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'fore topsail yard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'fore yard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'forecastle');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'formwork');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'fortification');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'fortress');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'forward deck');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'foundation');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'foundation hole');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'foundation pile');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'frame');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'frame first');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'frame spacing');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'framed building');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'fraterhouse');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'freighter');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'frieze');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'frigate');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'front facade');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'furniture');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'furring rafter');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'futtock');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'futtock rider');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'gaff');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'galeas');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'galley');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'galliot');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'gallow');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'gallows-field');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'gallows-lee');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'gangway');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'garboard strake');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'gatehouse');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'gateway');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'girder');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'granary');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'grating');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'grave');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'grave field');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'graving piece');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'guard post');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'guitar');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'gunwale');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hagboat');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'half frame');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'half timber work');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hall');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hallway');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hammerbeam truss');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'handle');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'handrail');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hanging truss');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hatch');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hatch coaming');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hauling');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'haunche');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hawse hole');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hawse hole bolster');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hawse hole lining');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'head');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'head cheek');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'head parapet');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'head rail');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'head timber');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'header');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'headledge');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'heart');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hearth');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'heel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'heel beam cap');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'heeling');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'helve');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'herve');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hillock');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hip rafter');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hip roof');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hog');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hogged vessel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hold');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hold rider');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hole');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'homeward bounder');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hooker');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'horreum');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hospital');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hound');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'house');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'house plan');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'howker');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hub');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hull');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hull plank');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hull strake');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'hut');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'icon');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'inboard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'inclined clamp');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'infrastructure');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'internal');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'interweaving');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'inwale');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'inwale, stringer');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'jack rafter');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'jackstaff');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'jamb');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'jibboom');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'jibboom saddle');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'jig');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'joint');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'joist');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'joisting');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'jolly');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'jolly boat');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'keel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'keel beam');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'keel plank');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'keelson');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'keep');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'kevel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'kevel bitt');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'kevel-head');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'king plank');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'king post');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'kingpost');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'knee');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'knee of the head');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'knife');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'knighthead');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'knoll');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'knuckle frame');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'knuckle framed ship');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'l-shaped chine');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'l-shaped covering board');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'ladder');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'ladder side');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'lance');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'land abutment');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'landing');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'landing place');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'landing stage');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'lap strake');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'larboard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'larboard side');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'lath');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'latrine');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'launch');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'leaf');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'ledge');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'leg of a table');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'lid');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'lierne rib');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'limber hole, watercourse');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'limberpassage');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'limbers');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'line of flotation');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'link beam');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'lintel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'list');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'lock');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'locker');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'loft');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'log');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'log cabin construction');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'long floating rule');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'long tackle block');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'long-case clock');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'longboat');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'longitudinal bracing');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'longitudinal stiffener');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'lookout tower');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'lower façade');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'lower front');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'lute');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'lyre');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'main boom');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'main channel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'main deck');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'main lower mast');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'main rail timber head');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'main top');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'main topgallant mast');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'main topgallant yard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'main topmast');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'main topsail yard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'main wale');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'main yard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'manor');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'manor house');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'manorial farm');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mansion');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mast');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mast beam');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mast cleat');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mast fid');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mast frame');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mast heel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mast step');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mast thwart');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mast truck');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mast woolding');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mat work');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'merchant vessel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mezzanine');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'military camp');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mill');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mill wheel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'miscellaneous finds');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mizzen channel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mizzen lower mast');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mizzen stool');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mizzen top');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mizzen topgallant mast');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mizzen topgallant yard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mizzen topmast');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mizzen topsail yard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mizzen yard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'moat');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'modillion');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'monastery');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mortice');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mortice-and-tenon');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mortise');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mortise-and-tenon (joint)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mortise-and-tenon joint');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'motte');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mould');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'moulding');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'mullion');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'museum');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'musical instrument');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'nail');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'nail (small)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'nail (wooden)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'nail dowel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'nail hole');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'narrow boat');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'nave');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'newel (post)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'newel-post stair');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'oar');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'oar lock');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'oar port');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'organ');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'ornament');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'orphanage');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'outer bailey');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'outer hull');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'outlet sluice');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'outside ladder');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'overlap');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'packet, packet boat');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'paddle');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'paddle wheel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'painting');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'palace');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'palisade');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'pane');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'panel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'paneling');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'panelling');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'pantry');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'parral');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'parral rib');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'parral truck');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'parrel rib');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'parsonage');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'partition');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'partition wall');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'passage(way)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'passing brace');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'pavillion');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'peg');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'philibert roof');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'philibert truss');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'piece of furniture');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'pier');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'pilaster');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'pile');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'piling');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'pillar, stanchion');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'pink');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'pinnace');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'pit');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'plaiting');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'plank');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'planking');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'planksheer');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'plaque');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'plate');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'platform');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'poop deck');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'port');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'port lid');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'port side');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'portal');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'post');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'post hole');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'post-mill');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'praam');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'pram');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'principal rafter');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'prow block');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'prow block (stem)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'prow block (stern)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'pulley');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'pulpit');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'pump');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'pump dale');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'puncheon');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'punt');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'punt-like vessel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'punting');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'purlin');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'purlin roof');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'putlog');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'quarter');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'quarter bar');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'quarter deck');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'quarter gallery');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'quay');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'queenpost');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'quiver');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'quoin');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'rabbet');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'rafter');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'rafter single roof');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'rail timber');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'railing');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'raking');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'ram');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'rampart');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'rear (side)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'rear part');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'rebate');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'redoubt');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'refectory');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'refuge hill');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'refugium');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'relief');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'reredos');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'residence');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'retabel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'revetment');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'rib');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'ribband');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'rider');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'ridge');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'ridge (pole/beam/piece)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'ridge (purlin)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'ridge purlin');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'ridge turret');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'ridge-beam');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'ridgepiece');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'rig');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'rigging');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'rising floor timber');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'road');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'rod');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'rood gallery');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'rood loft');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'rood screen');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'roof');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'roof boarding');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'roof construction');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'roof framing');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'roof hip');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'roof plate');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'roof timber');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'roof truss(es)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'roof turret');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'roof-beam');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'rooftop');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'room');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'root');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'root system');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'round bottom');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'round bottomed vessel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'round house');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'roundup');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'rowing');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'rowlock');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'rudder');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'running-board');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'rustic work');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'rustication');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sacristy');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sailing gear');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'saint andrew''s cross');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sanctuary');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sap wood');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'scabbard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'scantling');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'scarf');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'scarf joint');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'school');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'school building');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'scissors brace');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sconse');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'scoop');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'scull');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sculpture');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'scupper');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'seam');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sentry');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'set of frames');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'settlement');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'shape');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sheat');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sheathing');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'shed');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sheer');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sheer rail');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sheers');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sheet piling');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sheeting');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sheeting board');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'shell');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'shell first');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'shield');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'shingle');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'ship');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'ship shape');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'ship type');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'ship’s shell');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'ship’s side');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'shipbuilding yard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'shipyard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'shoe');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'shore');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'shovel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'shroud');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'side');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'side aisle');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'side strake');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sill');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'skeleton');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'skylight');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'slide (for carronade carriage)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sling brace');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'slingpiece');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'slipway');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sloop');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sluice');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sole piece');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'soleplate');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'solid newel stair');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'soulace');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sound box');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'soundboard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sounding board');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'spade');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'spar');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'spiral stair');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'spire');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'spire mast');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'spirketting');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'spits');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'splayed indent scarf');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'splayed joint with part abutments');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'splayed scarf-joint');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'splayed-and-tabled');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'spoon');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sprit');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sprit topsail yard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'spritsail yard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'spur (tie)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'square sterned ship');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stable');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stair');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stair tower');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'staircase');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'staircase turret');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stanchion');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'starboard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'starboard side');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'statue');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'statue of the virgin mary');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stave');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stay');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stealer');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'steeple');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'steering gear');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'steering oar');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'steering wheel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stem');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stem (fore)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stem post (fore)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stemson');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'step');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'step (in ladder)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stern');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stern (aft)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stern balcony');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stern outboard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stern post (aft)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stern transom');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stern wing');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stern-');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sternson');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stiffening');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stop log');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'storehouse');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'storey');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'story');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'strair');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'strake');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stream');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'string');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stringer');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'strip');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'structure');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'strut');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stud');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stump');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'stunsail boom');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'summer');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'summerbeam');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'summerhouse');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'superimposed tiebeam');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'supporting beam');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'supporting statue');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'sweep');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'swim-head');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'synagogue');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'table');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'table-leg');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'tabling');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'taffrail');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'taffrail figure');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'taffrail medallion');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'taffrail side figure');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'tapered cheek');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'template');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'temple');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'tenon');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'terp');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'thole');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'three-decker');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'threshold');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'thwart');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'thwart stanchion');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'tie');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'tie beam');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'tie-beam support');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'tier');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'tile lath');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'tiller');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'timber framing');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'timber stockade');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'timbering');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'tomb');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'tool mark');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'top rim');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'topgallant royal mast');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'tower');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'tower mill');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'town');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'town hall');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'town wall');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'toys');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'trader');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'trading ship/vessel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'trailboard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'transept');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'transom');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'transom (bar)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'transom side figure');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'transom timber');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'transom(bar)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'transverse timber');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'tray');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'tread');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'tree-stump');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'trenail');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'trench');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'trestle tree');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'trimmer');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'trimming');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'trimming rafter');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'triptych');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'truck (for gun cariage)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'trunk');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'truss');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'truss beam');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'truss post');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'truss tie');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'tub');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'tun');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'underrafter');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'unknown');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'uphroe');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'upper channel wale (ship of the line)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'upper deck');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'upper door');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'upright extension');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'valley');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'valley rafter');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'vallum');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'vat');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'vault');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'vehicle');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'vestry');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'vicarage');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'villa (rustica)');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'viola');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'violin');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'wainscot');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'wainscoting');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'wale');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'wall');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'wall piece');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'wall plate');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'wall post');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'wane');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'warehouse');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'warship');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'wash board');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'wash strake');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'waste pit');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'watch');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'watchtower');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'water carrier');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'water conduit');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'water hoy');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'water mill');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'water pipe');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'water well');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'water wheel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'watercastle');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'waterway');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'wattle');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'way');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'weapon');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'wedge');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'weighhouse');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'well');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'wharf');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'wheel');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'whelp');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'whip');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'whipstaff');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'winch');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'windbrace');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'windmill');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'window');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'window head');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'wing transom');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'wooden construction');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'woodroll');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'yacht');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'yard');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'yard cleat');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'yawl');
INSERT INTO tlkpelementtype (vocabularyid, elementtype) VALUES (5, 'yoke');
  
  
update tblconfig set value='1.3.2' where key='wsversion';
update tblsupportedclient set minversion='1.3.2' where client ='Tellervo WSI';
