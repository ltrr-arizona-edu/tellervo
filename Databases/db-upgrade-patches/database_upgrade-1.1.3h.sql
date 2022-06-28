DROP VIEW vwtblsample;

CREATE OR REPLACE VIEW vwtblsample AS 
 SELECT s.externalid, s.sampleid, s.code, s.comments, s.code AS title, s.elementid, s.samplingdate, s.createdtimestamp, s.lastmodifiedtimestamp, st.sampletypeid, st.sampletype, s.file, s."position", s.state, s.knots, s.description, dc.datecertainty, s.boxid, lc.objectcode, lc.elementcode, c.curationstatusid, cl.curationstatus, s.samplingmonth, s.samplingyear, s.samplestatusid, ss.samplestatus
   FROM tblsample s
   LEFT JOIN tlkpsamplestatus ss ON s.samplestatusid = ss.samplestatusid
   LEFT JOIN tlkpdatecertainty dc ON s.datecertaintyid = dc.datecertaintyid
   LEFT JOIN tlkpsampletype st ON s.typeid = st.sampletypeid
   LEFT JOIN vwtblcurationmostrecent c ON s.sampleid = c.sampleid
   LEFT JOIN tlkpcurationstatus cl ON c.curationstatusid = cl.curationstatusid
   LEFT JOIN vwlabcodesforsamples lc ON s.sampleid = lc.sampleid;

  
DROP VIEW vwtblobject;

CREATE OR REPLACE VIEW vwtblobject AS 
 SELECT cquery.countofchildvmeasurements, o.vegetationtype, o.comments, o.objectid, dom.domainid, dom.domain, o.title, o.code, o.createdtimestamp, o.lastmodifiedtimestamp, o.locationgeometry, ( SELECT st_asgml(3, o.locationgeometry, 15, 1) AS st_asgml) AS gml, st_xmin(o.locationgeometry::box3d) AS longitude, ymin(o.locationgeometry::box3d) AS latitude, o.locationtypeid, o.locationprecision, o.locationcomment, o.locationaddressline1, o.locationaddressline2, o.locationcityortown, o.locationstateprovinceregion, o.locationpostalcode, o.locationcountry, array_to_string(o.file, '><'::text) AS file, o.creator, o.owner, o.parentobjectid, o.description, o.objecttypeid, loctype.locationtype, objtype.objecttype, covtemp.coveragetemporal, covtempfound.coveragetemporalfoundation
   FROM tblobject o
   LEFT JOIN tlkpdomain dom ON o.domainid = dom.domainid
   LEFT JOIN tlkplocationtype loctype ON o.locationtypeid = loctype.locationtypeid
   LEFT JOIN tlkpobjecttype objtype ON o.objecttypeid = objtype.objecttypeid
   LEFT JOIN tlkpcoveragetemporal covtemp ON o.coveragetemporalid = covtemp.coveragetemporalid
   LEFT JOIN tlkpcoveragetemporalfoundation covtempfound ON o.coveragetemporalfoundationid = covtempfound.coveragetemporalfoundationid
   LEFT JOIN ( SELECT e.objectid AS masterobjectid, count(e.objectid) AS countofchildvmeasurements
   FROM tblelement e
   JOIN tblsample s ON s.elementid = e.elementid
   JOIN tblradius r ON r.sampleid = s.sampleid
   JOIN tblmeasurement m ON m.radiusid = r.radiusid
   JOIN tblvmeasurementderivedcache vc ON vc.measurementid = m.measurementid
  GROUP BY e.objectid) cquery ON cquery.masterobjectid = o.objectid;

DROP VIEW vwtblelement;

CREATE OR REPLACE VIEW vwtblelement AS 
 SELECT ( SELECT findobjecttoplevelancestor.code
           FROM cpgdb.findobjecttoplevelancestor(e.objectid) findobjecttoplevelancestor(objectid, title, code, createdtimestamp, lastmodifiedtimestamp, locationgeometry, locationtypeid, locationprecision, locationcomment, creator, owner, parentobjectid, description, objecttypeid, coveragetemporalid, coveragetemporalfoundationid, comments, coveragetemporal, coveragetemporalfoundation, locationaddressline1, locationaddressline2, locationcityortown, locationstateprovinceregion, locationpostalcode, locationcountry, locationcountry, vegetationtype, domainid, projectid)) AS objectcode, e.comments, dom.domainid, dom.domain, e.elementid, e.locationprecision, e.code AS title, e.code, e.createdtimestamp, e.lastmodifiedtimestamp, e.locationgeometry, ( SELECT st_asgml(3, e.locationgeometry, 15, 1) AS st_asgml) AS gml, st_xmin(e.locationgeometry::box3d) AS longitude, ymin(e.locationgeometry::box3d) AS latitude, e.islivetree, e.originaltaxonname, e.locationtypeid, e.locationcomment, e.locationaddressline1, e.locationaddressline2, e.locationcityortown, e.locationstateprovinceregion, e.locationpostalcode, e.locationcountry, array_to_string(e.file, '><'::text) AS file, e.description, e.processing, e.marks, e.diameter, e.width, e.height, e.depth, e.unsupportedxml, e.objectid, e.elementtypeid, e.authenticity, e.elementshapeid, shape.elementshape, tbltype.elementtype, loctype.locationtype, e.altitude, e.slopeangle, e.slopeazimuth, e.soildescription, e.soildepth, e.bedrockdescription, vwt.taxonid, vwt.taxonlabel, vwt.parenttaxonid, vwt.colid, vwt.colparentid, vwt.taxonrank, unit.unit AS units, unit.unitid
   FROM tblelement e
   LEFT JOIN tlkpdomain dom ON e.domainid = dom.domainid
   LEFT JOIN tlkpelementshape shape ON e.elementshapeid = shape.elementshapeid
   LEFT JOIN tlkpelementtype tbltype ON e.elementtypeid = tbltype.elementtypeid
   LEFT JOIN tlkplocationtype loctype ON e.locationtypeid = loctype.locationtypeid
   LEFT JOIN tlkpunit unit ON e.units = unit.unitid
   LEFT JOIN vwtlkptaxon vwt ON e.taxonid = vwt.taxonid;

  