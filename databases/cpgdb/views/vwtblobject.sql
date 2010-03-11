DROP VIEW vwtblObject CASCADE;

CREATE VIEW vwtblObject AS
  SELECT cquery.countofchildvmeasurements, o.comments, o.objectid, o.title, o.code, o.createdtimestamp, o.lastmodifiedtimestamp, o.locationgeometry, 
       o.locationtypeid, o.locationprecision, o.locationcomment, 
       o.locationaddressline1, o.locationaddressline2, o.locationcityortown,
o.locationstateprovinceregion, o.locationpostalcode, o.locationcountry,
       
       array_to_string(o.file, '><') as file, o.creator, o.owner, o.parentobjectid, o.description, 
       o.objecttypeid, loctype.locationtype, objtype.objecttype, covtemp.coveragetemporal, covtempfound.coveragetemporalfoundation
   FROM tblobject o
   LEFT JOIN tlkplocationtype loctype ON o.locationtypeid = loctype.locationtypeid
   LEFT JOIN tlkpobjecttype objtype ON o.objecttypeid = objtype.objecttypeid
   LEFT JOIN tlkpcoveragetemporal covtemp ON o.coveragetemporalid = covtemp.coveragetemporalid
   LEFT JOIN tlkpcoveragetemporalfoundation covtempfound ON o.coveragetemporalfoundationid = covtempfound.coveragetemporalfoundationid
   LEFT JOIN (SELECT e.objectid AS masterobjectid, count(e.objectid) AS countofchildvmeasurements
              FROM tblelement e
              JOIN tblsample s ON s.elementid = e.elementid
              JOIN tblradius r ON r.sampleid = s.sampleid
              JOIN tblmeasurement m ON m.radiusid = r.radiusid
              JOIN tblvmeasurementderivedcache vc ON vc.measurementid = m.measurementid
              GROUP BY e.objectid) cquery 
        ON cquery.masterobjectid = o.objectid;

GRANT ALL on vwtblobject to "Webgroup";

