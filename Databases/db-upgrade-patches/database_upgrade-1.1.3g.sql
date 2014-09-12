--
--  IMPLEMENT the TRIDAS PROJECT CONCEPT
--

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
CONSTRAINT "fkey_project-projecttype" FOREIGN KEY (projecttypeid)
      REFERENCES tlkpprojecttype (projecttypeid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
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




