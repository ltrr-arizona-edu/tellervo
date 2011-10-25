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
ALTER TABLE tblupgradelog OWNER TO webuser;


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