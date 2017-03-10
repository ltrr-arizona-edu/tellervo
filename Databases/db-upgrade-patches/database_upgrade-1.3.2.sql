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

