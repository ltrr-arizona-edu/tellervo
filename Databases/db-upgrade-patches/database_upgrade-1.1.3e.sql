
-- Auto create UUID pkeys

alter table tblobject alter column objectid set default uuid_generate_v1mc();
alter table tblelement alter column elementid set default uuid_generate_v1mc();
alter table tblsample alter column sampleid set default uuid_generate_v1mc();
alter table tblradius alter column radiusid set default uuid_generate_v1mc();
alter table tblvmeasurement alter column vmeasurementid set default uuid_generate_v1mc();


-- Adding sample status tables and values

CREATE TABLE tlkpsamplestatus
(
  samplestatusid serial NOT NULL,
  samplestatus character varying,
  CONSTRAINT pkey_samplestatustype PRIMARY KEY (samplestatusid)
)
WITH (
  OIDS=FALSE
);
insert into tlkpsamplestatus (samplestatus) values ('Unprepped');
insert into tlkpsamplestatus (samplestatus) values ('Prepped');
insert into tlkpsamplestatus (samplestatus) values ('Undateable');
insert into tlkpsamplestatus (samplestatus) values ('Dated');
insert into tlkpsamplestatus (samplestatus) values ('Measured');
insert into tlkpsamplestatus (samplestatus) values ('Too few rings');

alter table tblsample add column samplestatusid integer;
alter table tblsample add constraint "fkey_sample-samplestatus" 
FOREIGN KEY (samplestatusid) REFERENCES tlkpsamplestatus (samplestatusid) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
