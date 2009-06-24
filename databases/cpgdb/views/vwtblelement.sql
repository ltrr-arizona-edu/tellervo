DROP VIEW vwtblelement;

CREATE VIEW vwtblelement AS
 SELECT tblelement.elementid, tblelement.taxonid, tblelement.locationprecision, tblelement.code, tblelement.createdtimestamp, tblelement.lastmodifiedtimestamp, 
        tblelement.locationgeometry, tblelement.islivetree, tblelement.originaltaxonname, tblelement.locationtypeid, tblelement.locationcomment, tblelement.file, 
        tblelement.description, tblelement.processing, tblelement.marks, tblelement.diameter, tblelement.width, tblelement.height, tblelement.depth, 
        tblelement.unsupportedxml, tblelement.units, tblelement.objectid, tblelement.elementtypeid, tblelement.elementauthenticityid, tblelement.elementshapeid, 
        auth.elementauthenticity, shape.elementshape, tbltype.elementtype, loctype.locationtype, tblelement.altitude, tblelement.slopeangle, 
        tblelement.slopeazimuth, tblelement.soildescription, tblelement.soildepth, tblelement.bedrockdescription
   FROM tblelement
   LEFT JOIN tlkpelementauthenticity auth ON tblelement.elementauthenticityid = auth.elementauthenticityid
   LEFT JOIN tlkpelementshape shape ON tblelement.elementshapeid = shape.elementshapeid
   LEFT JOIN tlkpelementtype tbltype ON tblelement.elementtypeid = tbltype.elementtypeid
   LEFT JOIN tlkplocationtype loctype ON tblelement.locationtypeid = loctype.locationtypeid;

