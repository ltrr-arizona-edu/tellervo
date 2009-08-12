
CREATE TABLE tblbox
(
  boxid uuid NOT NULL DEFAULT uuid_generate_v4(),
  title character varying NOT NULL,
  curationlocation character varying,
  trackinglocation character varying,
  createdtimestamp timestamp with time zone NOT NULL DEFAULT now(),
  lastmodifiedtimestamp timestamp with time zone NOT NULL DEFAULT now(),
  comments character varying,
  CONSTRAINT pkey_tblbox PRIMARY KEY (boxid),
  CONSTRAINT uniq_boxtitle UNIQUE (title)
)
WITH (OIDS=FALSE);
ALTER TABLE tblbox OWNER TO aps03pwb;
GRANT ALL ON TABLE tblbox TO aps03pwb;
GRANT ALL ON TABLE tblbox TO "Webgroup";
