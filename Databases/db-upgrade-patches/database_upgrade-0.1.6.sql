CREATE TABLE tblupgradelog
(
  upgradelogid serial NOT NULL,
  filename character varying NOT NULL,
  "timestamp" timestamp with time zone NOT NULL DEFAULT now(),
  CONSTRAINT pkey_upgradelog PRIMARY KEY (upgradelogid)
)
WITH (
  OIDS=FALSE
);
GRANT ALL ON TABLE tblupgradelog TO "Webgroup";


DROP VIEW vwtblelement;

CREATE VIEW vwtblelement AS 
SELECT ( SELECT findobjecttoplevelancestor.code
           FROM cpgdb.findobjecttoplevelancestor(e.objectid) findobjecttoplevelancestor(objectid, title, code, createdtimestamp, lastmodifiedtimestamp, locationgeometry, locationtypeid, locationprecision, locationcomment, file, creator, owner, parentobjectid, description, objecttypeid, coveragetemporalid, coveragetemporalfoundationid, comments, coveragetemporal, coveragetemporalfoundation, locationaddressline1, locationaddressline2, locationcityortown, locationstateprovinceregion, locationpostalcode, locationcountry)) AS objectcode, e.comments, e.elementid, e.locationprecision, e.code AS title, e.code, e.createdtimestamp, e.lastmodifiedtimestamp, e.locationgeometry, e.islivetree, e.originaltaxonname, e.locationtypeid, e.locationcomment, e.locationaddressline1, e.locationaddressline2, e.locationcityortown, e.locationstateprovinceregion, e.locationpostalcode, e.locationcountry, e.file, e.description, e.processing, e.marks, e.diameter, e.width, e.height, e.depth, e.unsupportedxml, e.objectid, e.elementtypeid, e.elementauthenticityid, e.elementshapeid, auth.elementauthenticity, shape.elementshape, tbltype.elementtype, loctype.locationtype, e.altitude, e.slopeangle, e.slopeazimuth, e.soildescription, e.soildepth, e.bedrockdescription, vwt.taxonid, vwt.taxonlabel, vwt.parenttaxonid, vwt.colid, vwt.colparentid, vwt.taxonrank, unit.unit AS units, unit.unitid as unitid
   FROM tblelement e
   LEFT JOIN tlkpelementauthenticity auth ON e.elementauthenticityid = auth.elementauthenticityid
   LEFT JOIN tlkpelementshape shape ON e.elementshapeid = shape.elementshapeid
   LEFT JOIN tlkpelementtype tbltype ON e.elementtypeid = tbltype.elementtypeid
   LEFT JOIN tlkplocationtype loctype ON e.locationtypeid = loctype.locationtypeid
   LEFT JOIN tlkpunit unit ON e.units = unit.unitid
   LEFT JOIN vwtlkptaxon vwt ON e.taxonid = vwt.taxonid;
GRANT ALL ON vwtblelement TO "Webgroup";

-- Add early and late wood fields to tables

ALTER TABLE tblvmeasurementreadingresult ADD COLUMN ewwidth INTEGER;
ALTER TABLE tblvmeasurementreadingresult ADD COLUMN lwwidth INTEGER;
ALTER TABLE tblreading ADD COLUMN ewwidth INTEGER;
ALTER TABLE tblreading ADD COLUMN lwwidth INTEGER;



-- Modify function to include the early and late wood fields in generated tables

DROP FUNCTION cpgdbj.qappvmeasurementreadingresult(uuid, integer);
CREATE OR REPLACE FUNCTION cpgdbj.qappvmeasurementreadingresult(paramvmeasurementresultid uuid, parammeasurementid integer)
  RETURNS void AS
$BODY$

  INSERT INTO tblVMeasurementReadingResult ( RelYear, Reading, VMeasurementResultID, ReadingID, ewwidth, lwwidth ) 
  SELECT tblReading.RelYear, tblReading.Reading, $1 AS Expr1, tblReading.readingID, tblReading.ewwidth, tblReading.lwwidth
   FROM tblReading 
   WHERE tblReading.MeasurementID=$2

$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
GRANT EXECUTE ON FUNCTION cpgdbj.qappvmeasurementreadingresult(uuid, integer) TO "Webgroup";


DROP VIEW vwjsonnotedreadingresult;
CREATE OR REPLACE VIEW vwjsonnotedreadingresult AS 
 SELECT res.vmeasurementreadingresultid, res.vmeasurementresultid, res.relyear, res.reading, res.wjinc, res.wjdec, res.count, res.readingid, res.ewwidth, res.lwwidth, cpgdb.resultnotestojson(notes.noteids, notes.inheritedcounts) AS jsonnotes
   FROM tblvmeasurementreadingresult res
   LEFT JOIN vwresultnotesasarray notes ON res.vmeasurementresultid = notes.vmeasurementresultid AND res.relyear = notes.relyear;
GRANT ALL ON TABLE vwjsonnotedreadingresult TO "Webgroup";

-- Add new variables to lookup table

INSERT INTO tlkpmeasurementvariable (measurementvariableid, measurementvariable) values (2, 'earlywood width');
INSERT INTO tlkpmeasurementvariable (measurementvariableid, measurementvariable) values (3, 'latewood width');

--
--Update the the minimum version of the Corina client that this server supports
--
DELETE FROM tblsupportedclient where client='Corina WSI';
INSERT INTO tblsupportedclient (client, minversion) VALUES ('Corina WSI', '2.13');

--
-- Add unique constraints to security tables
--
ALTER TABLE tblsecurityobject ADD CONSTRAINT "uniq-object-group-permission" UNIQUE (objectid, securitygroupid, securitypermissionid);
ALTER TABLE tblsecurityelement ADD CONSTRAINT "uniq-element-group-permission" UNIQUE (securitygroupid, securitypermissionid, securityelementid)

