-- Table: tblsample

-- DROP TABLE tblsample;

CREATE TABLE tblsample
(
  sampleid uuid NOT NULL, -- Unique sample indentifier
  code character varying(255) NOT NULL,
  elementid uuid NOT NULL,
  samplingdate date, -- Year of dendrochronological sampling
  createdtimestamp timestamp with time zone NOT NULL DEFAULT now(),
  lastmodifiedtimestamp timestamp with time zone NOT NULL DEFAULT now(),
  "type" character varying(32),
  identifierdomain character varying(256), -- Domain from which the identifier-value was issued
  file character varying[], -- Digital photo or scanned image filenames
  "position" character varying, -- Position of sample in element
  state character varying, -- State of material (dry / wet / conserved / burned / woodworm/ rot / cracks) things that indicate the quality of the measurements
  knots boolean DEFAULT false, -- Presence of knots
  description character varying, -- More information about the sample
  datecertaintyid integer,
  typeid integer,
  boxid uuid,
  comments character varying,
  CONSTRAINT pkey_sample PRIMARY KEY (sampleid),
  CONSTRAINT "fkey_sample-box" FOREIGN KEY (boxid)
      REFERENCES tblbox (boxid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "fkey_sample-datecertainty" FOREIGN KEY (datecertaintyid)
      REFERENCES tlkpdatecertainty (datecertaintyid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "fkey_sample-sampletype" FOREIGN KEY (typeid)
      REFERENCES tlkpsampletype (sampletypeid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "fkey_sample-tree" FOREIGN KEY (elementid)
      REFERENCES tblelement (elementid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE RESTRICT
)
WITH (OIDS=FALSE);
ALTER TABLE tblsample OWNER TO aps03pwb;
GRANT ALL ON TABLE tblsample TO aps03pwb;
GRANT ALL ON TABLE tblsample TO "Webgroup";
COMMENT ON COLUMN tblsample.sampleid IS 'Unique sample indentifier
';
COMMENT ON COLUMN tblsample.samplingdate IS 'Year of dendrochronological sampling';
COMMENT ON COLUMN tblsample.identifierdomain IS 'Domain from which the identifier-value was issued';
COMMENT ON COLUMN tblsample.file IS 'Digital photo or scanned image filenames';
COMMENT ON COLUMN tblsample."position" IS 'Position of sample in element';
COMMENT ON COLUMN tblsample.state IS 'State of material (dry / wet / conserved / burned / woodworm/ rot / cracks) things that indicate the quality of the measurements';
COMMENT ON COLUMN tblsample.knots IS 'Presence of knots';
COMMENT ON COLUMN tblsample.description IS 'More information about the sample';


-- Index: "ind_sample-sampletype"

-- DROP INDEX "ind_sample-sampletype";

CREATE INDEX "ind_sample-sampletype"
  ON tblsample
  USING btree
  (type);

-- Index: "ind_sample-tree"

-- DROP INDEX "ind_sample-tree";

CREATE INDEX "ind_sample-tree"
  ON tblsample
  USING btree
  (elementid);


-- Trigger: update_sample-lastmodifiedtimestamp on tblsample

-- DROP TRIGGER "update_sample-lastmodifiedtimestamp" ON tblsample;

CREATE TRIGGER "update_sample-lastmodifiedtimestamp"
  BEFORE UPDATE
  ON tblsample
  FOR EACH ROW
  EXECUTE PROCEDURE update_lastmodifiedtimestamp();

