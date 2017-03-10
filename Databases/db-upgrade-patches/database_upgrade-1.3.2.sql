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

DROP VIEW vwtblproject;
CREATE OR REPLACE VIEW vwtblproject AS 
SELECT p.projectid,
dom.domainid,
dom.domain,
p.title,
p.projecttypeid,
p.createdtimestamp,
p.lastmodifiedtimestamp,
p.comments,
p.description,
p.file,
p.projectcategoryid,
p.investigator,
p.period,
p.requestdate,
p.commissioner,
p.reference,
p.research,
projtype.projecttype
 FROM tblproject p
 LEFT JOIN tlkpdomain dom on p.domainid=dom.domainid
 LEFT JOIN tlkpprojecttype projtype ON p.projecttypeid = projtype.projecttypeid;
  ALTER VIEW public.vwtblproject
  OWNER TO tellervo;
  
DROP VIEW vwtblobject;
CREATE VIEW vwtblobject AS   
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
   ALTER VIEW public.vwtblobject
  OWNER TO tellervo;

