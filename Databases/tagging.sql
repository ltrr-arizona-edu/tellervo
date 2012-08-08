--- CREATE TAGGING TABLES
----------------------------

CREATE TABLE tlkptag
(
  tagid uuid NOT NULL DEFAULT uuid_generate_v1mc(),
  name character varying NOT NULL,
  ownerid integer NOT NULL,
  isprivate boolean NOT NULL DEFAULT false,
  CONSTRAINT pkey_tbltag PRIMARY KEY (tagid ),
  CONSTRAINT fkey_tbltag_ownerid FOREIGN KEY (ownerid)
      REFERENCES tblsecurityuser (securityuserid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
  CONSTRAINT uniq_tag UNIQUE (name )
)
WITH (
  OIDS=FALSE
);

CREATE TABLE tblvmeasurementtag
(
  vmeasurementtagid serial NOT NULL,
  vmeasurementid uuid NOT NULL,
  tagid uuid NOT NULL,
  CONSTRAINT pkey_tblvmeasurementtag PRIMARY KEY (vmeasurementtagid ),
  CONSTRAINT "fkey_tblvmeasurementtag-tblvmeasurement" FOREIGN KEY (vmeasurementid)
      REFERENCES tblvmeasurement (vmeasurementid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fkey_tblvmeasurementtag_tlkptag FOREIGN KEY (tagid)
      REFERENCES tlkptag (tagid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);