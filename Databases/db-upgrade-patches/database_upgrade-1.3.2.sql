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
INSERT INTO tlkpdatatype (datatype) VALUES ('xs:integer');
INSERT INTO tlkpdatatype (datatype) VALUES ('xs:double');


CREATE TABLE public.tlkpuserdefinedfield
(
  userdefinedfieldid uuid NOT NULL DEFAULT uuid_generate_v4(),
  fieldname character varying NOT NULL,
  description character varying NOT NULL,
  orderseq integer NOT NULL,
  attachedto integer NOT NULL, -- Integer indicated at what tridas level this field is attached.  1=project, 2=object, 3=element, 4=sample, 5=radius, 6=measurementseries
  datatype character varying NOT NULL DEFAULT 'xs:string'::character varying,
  CONSTRAINT pkey_userdefinedfield PRIMARY KEY (userdefinedfieldid),
  CONSTRAINT "tlkpuserdefinedfields-datatype" FOREIGN KEY (datatype)
      REFERENCES public.tlkpdatatype (datatype) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uniq_userdefinedfields_fieldname UNIQUE (fieldname, attachedto),
  CONSTRAINT uniq_userdefinedfields_order_index UNIQUE (orderseq, attachedto),
  CONSTRAINT check_userdefinedfields_validattachedto CHECK (attachedto >= 1 AND attachedto <= 6)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.tlkpuserdefinedfield
  OWNER TO tellervo;
COMMENT ON COLUMN public.tlkpuserdefinedfield.attachedto IS 'Integer indicated at what tridas level this field is attached.  1=project, 2=object, 3=element, 4=sample, 5=radius, 6=measurementseries';

ALTER TABLE public.tblsample
  ADD COLUMN userdefinedfielddata character varying[];

  
  CREATE OR REPLACE FUNCTION public."checkSampleUserDefinedFieldData"()
  RETURNS trigger AS
$BODY$DECLARE

expectedlength integer;

BEGIN


SELECT count(*) INTO expectedlength FROM tlkpuserdefinedfield WHERE attachedto=4;

IF(array_length(NEW.userdefinedfielddata, 1)==expectedlength) THEN
	RETURN NEW;

ELSE
	RAISE EXCEPTION 'Invalid number of user defined fields specified';
	RETURN NULL;
END IF;


CREATE TRIGGER "check_UserDefinedField"
  BEFORE INSERT OR UPDATE
  ON public.tblsample
  FOR EACH ROW
  EXECUTE PROCEDURE public."checkSampleUserDefinedFieldData"();
  
  
  DROP VIEW vwtblsample;

CREATE OR REPLACE VIEW public.vwtblsample AS 
 SELECT s.externalid,
    s.sampleid,
    s.code,
    s.comments,
    s.code AS title,
    s.elementid,
    s.samplingdate,
    s.createdtimestamp,
    s.lastmodifiedtimestamp,
    st.sampletypeid,
    st.sampletype,
    array_to_string(s.file, '><'::text) AS file,
    s."position",
    s.state,
    s.knots,
    s.description,
    array_to_string(s.userdefinedfielddata, '><'::text) AS userdefinedfielddata,
    dc.datecertainty,
    s.boxid,
    lc.objectcode,
    lc.elementcode,
    c.curationstatusid,
    cl.curationstatus,
    s.samplingmonth,
    s.samplingyear,
    s.samplestatusid,
    ss.samplestatus
   FROM tblsample s
     LEFT JOIN tlkpsamplestatus ss ON s.samplestatusid = ss.samplestatusid
     LEFT JOIN tlkpdatecertainty dc ON s.datecertaintyid = dc.datecertaintyid
     LEFT JOIN tlkpsampletype st ON s.typeid = st.sampletypeid
     LEFT JOIN vwtblcurationmostrecent c ON s.sampleid = c.sampleid
     LEFT JOIN tlkpcurationstatus cl ON c.curationstatusid = cl.curationstatusid
     LEFT JOIN vwlabcodesforsamples lc ON s.sampleid = lc.sampleid;

ALTER TABLE public.vwtblsample
  OWNER TO tellervo;
