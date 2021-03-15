-- New terms
insert into tlkpobjecttype (objecttypeid, vocabularyid, objecttype) values (2000, 2, 'plot');
insert into tlkpobjecttype (objecttypeid, vocabularyid, objecttype) values (2001, 2, 'watershed');
INSERT INTO tlkpelementtype (elementtypeid, vocabularyid, elementtype) values (1000, 2, 'snag');
INSERT INTO tlkpsampletype (sampletypeid, sampletype) values (300, 'Partial section');



-- Sort out duplicates and standardise the samplestatusid
INSERT INTO tlkpsamplestatus (samplestatusid, samplestatus) values (101, 'Unprepped');
INSERT INTO tlkpsamplestatus (samplestatusid, samplestatus) values (102, 'Prepped');
INSERT INTO tlkpsamplestatus (samplestatusid, samplestatus) values (103, 'Measured');
INSERT INTO tlkpsamplestatus (samplestatusid, samplestatus) values (104, 'Dated');
INSERT INTO tlkpsamplestatus (samplestatusid, samplestatus) values (105, 'Partially dated');
INSERT INTO tlkpsamplestatus (samplestatusid, samplestatus) values (106, 'Undated');
INSERT INTO tlkpsamplestatus (samplestatusid, samplestatus) values (107, 'Undateable');
INSERT INTO tlkpsamplestatus (samplestatusid, samplestatus) values (108, 'Too few rings');
UPDATE tblsample set samplestatusid=101 WHERE samplestatusid=(SELECT samplestatusid FROM tlkpsamplestatus WHERE samplestatusid < 100 AND samplestatus='Unprepped');
UPDATE tblsample set samplestatusid=102 WHERE samplestatusid=(SELECT samplestatusid FROM tlkpsamplestatus WHERE samplestatusid < 100 AND samplestatus='Prepped');
UPDATE tblsample set samplestatusid=103 WHERE samplestatusid=(SELECT samplestatusid FROM tlkpsamplestatus WHERE samplestatusid < 100 AND samplestatus='Measured');
UPDATE tblsample set samplestatusid=104 WHERE samplestatusid=(SELECT samplestatusid FROM tlkpsamplestatus WHERE samplestatusid < 100 AND samplestatus='Dated');
UPDATE tblsample set samplestatusid=105 WHERE samplestatusid=(SELECT samplestatusid FROM tlkpsamplestatus WHERE samplestatusid < 100 AND samplestatus='Partially dated');
UPDATE tblsample set samplestatusid=106 WHERE samplestatusid=(SELECT samplestatusid FROM tlkpsamplestatus WHERE samplestatusid < 100 AND samplestatus='Undated');
UPDATE tblsample set samplestatusid=107 WHERE samplestatusid=(SELECT samplestatusid FROM tlkpsamplestatus WHERE samplestatusid < 100 AND samplestatus='Undateable');
UPDATE tblsample set samplestatusid=108 WHERE samplestatusid=(SELECT samplestatusid FROM tlkpsamplestatus WHERE samplestatusid < 100 AND samplestatus='Too few rings');
DELETE FROM tlkpsamplestatus WHERE samplestatusid<100;
ALTER TABLE tlkpsamplestatus ADD CONSTRAINT uniqsamplestatus UNIQUE (samplestatus);

