DROP VIEW vwtblelement;

CREATE VIEW vwtblelement AS
SELECT e.comments, e.elementid, e.locationprecision, 
e.code AS title, e.code, e.createdtimestamp, e.lastmodifiedtimestamp, 
e.locationgeometry, e.islivetree, e.originaltaxonname, e.locationtypeid, 
e.locationcomment, e.locationaddressline1, e.locationaddressline2, e.locationcityortown,
e.locationstateprovinceregion, e.locationpostalcode, e.locationcountry, e.file, e.description, 
e.processing, e.marks,
e.diameter, e.width, e.height, e.depth, e.unsupportedxml, 
e.units, e.objectid, e.elementtypeid, e.elementauthenticityid, 
e.elementshapeid, auth.elementauthenticity, shape.elementshape, tbltype.elementtype, loctype.locationtype, 
e.altitude, e.slopeangle, e.slopeazimuth, e.soildescription, e.soildepth, 
e.bedrockdescription, vwt.*
   FROM tblelement e
   LEFT JOIN tlkpelementauthenticity auth ON e.elementauthenticityid = auth.elementauthenticityid
   LEFT JOIN tlkpelementshape shape ON e.elementshapeid = shape.elementshapeid
   LEFT JOIN tlkpelementtype tbltype ON e.elementtypeid = tbltype.elementtypeid
   LEFT JOIN tlkplocationtype loctype ON e.locationtypeid = loctype.locationtypeid
   LEFT JOIN vwtlkptaxon vwt ON e.taxonid=vwt.taxonid;

GRANT ALL on vwtblelement to "Webgroup";
GRANT ALL on vwtblelement to aps03pwb;
