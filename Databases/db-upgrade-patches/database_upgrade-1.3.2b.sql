-- New terms
INSERT INTO tlkpobjecttype (objecttypeid, vocabularyid, objecttype)
VALUES (2000, 2, 'plot')
ON CONFLICT DO NOTHING;
INSERT INTO tlkpobjecttype (objecttypeid, vocabularyid, objecttype)
VALUES (2001, 2, 'watershed')
ON CONFLICT DO NOTHING;
INSERT INTO tlkpelementtype (elementtypeid, vocabularyid, elementtype)
VALUES (1000, 2, 'snag')
ON CONFLICT DO NOTHING;
INSERT INTO tlkpsampletype (sampletypeid, sampletype)
VALUES (300, 'Partial section')
ON CONFLICT DO NOTHING;



-- Sort out duplicates and standardise the samplestatusid
INSERT INTO tlkpsamplestatus (samplestatusid, samplestatus)
VALUES (101, 'Unprepped')
ON CONFLICT DO NOTHING;
INSERT INTO tlkpsamplestatus (samplestatusid, samplestatus)
VALUES (102, 'Prepped')
ON CONFLICT DO NOTHING;
INSERT INTO tlkpsamplestatus (samplestatusid, samplestatus)
VALUES (103, 'Measured')
ON CONFLICT DO NOTHING;
INSERT INTO tlkpsamplestatus (samplestatusid, samplestatus)
VALUES (104, 'Dated')
ON CONFLICT DO NOTHING;
INSERT INTO tlkpsamplestatus (samplestatusid, samplestatus)
VALUES (105, 'Partially dated')
ON CONFLICT DO NOTHING;
INSERT INTO tlkpsamplestatus (samplestatusid, samplestatus)
VALUES (106, 'Undated')
ON CONFLICT DO NOTHING;
INSERT INTO tlkpsamplestatus (samplestatusid, samplestatus)
VALUES (107, 'Undateable')
ON CONFLICT DO NOTHING;
INSERT INTO tlkpsamplestatus (samplestatusid, samplestatus)
VALUES (108, 'Too few rings')
ON CONFLICT DO NOTHING;
UPDATE tblsample SET samplestatusid=101 WHERE samplestatusid IN (SELECT samplestatusid FROM tlkpsamplestatus WHERE samplestatusid < 100 AND samplestatus='Unprepped');
UPDATE tblsample SET samplestatusid=102 WHERE samplestatusid IN (SELECT samplestatusid FROM tlkpsamplestatus WHERE samplestatusid < 100 AND samplestatus='Prepped');
UPDATE tblsample SET samplestatusid=103 WHERE samplestatusid IN (SELECT samplestatusid FROM tlkpsamplestatus WHERE samplestatusid < 100 AND samplestatus='Measured');
UPDATE tblsample SET samplestatusid=104 WHERE samplestatusid IN (SELECT samplestatusid FROM tlkpsamplestatus WHERE samplestatusid < 100 AND samplestatus='Dated');
UPDATE tblsample SET samplestatusid=105 WHERE samplestatusid IN (SELECT samplestatusid FROM tlkpsamplestatus WHERE samplestatusid < 100 AND samplestatus='Partially dated');
UPDATE tblsample SET samplestatusid=106 WHERE samplestatusid IN (SELECT samplestatusid FROM tlkpsamplestatus WHERE samplestatusid < 100 AND samplestatus='Undated');
UPDATE tblsample SET samplestatusid=107 WHERE samplestatusid IN (SELECT samplestatusid FROM tlkpsamplestatus WHERE samplestatusid < 100 AND samplestatus='Undateable');
UPDATE tblsample SET samplestatusid=108 WHERE samplestatusid IN (SELECT samplestatusid FROM tlkpsamplestatus WHERE samplestatusid < 100 AND samplestatus='Too few rings');
DELETE FROM tlkpsamplestatus WHERE samplestatusid<100;
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM pg_constraint
    WHERE conrelid = 'tlkpsamplestatus'::regclass
      AND conname = 'uniqsamplestatus'
  ) THEN
    ALTER TABLE tlkpsamplestatus
      ADD CONSTRAINT uniqsamplestatus UNIQUE (samplestatus);
  END IF;
END
$$;
