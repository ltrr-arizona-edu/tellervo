DROP VIEW vwtblObject CASCADE;

CREATE VIEW vwtblObject AS
  SELECT cquery.countofchildvmeasurements, tblobject.comments, tblobject.objectid, tblobject.title, tblobject.code, tblobject.createdtimestamp, tblobject.lastmodifiedtimestamp, tblobject.locationgeometry, 
       tblobject.locationtypeid, tblobject.locationprecision, tblobject.locationcomment, array_to_string(tblobject.file, '><') as file, tblobject.creator, tblobject.owner, tblobject.parentobjectid, tblobject.description, 
       tblobject.objecttypeid, loctype.locationtype, objtype.objecttype, covtemp.coveragetemporal, covtempfound.coveragetemporalfoundation
   FROM tblobject
   LEFT JOIN tlkplocationtype loctype ON tblobject.locationtypeid = loctype.locationtypeid
   LEFT JOIN tlkpobjecttype objtype ON tblobject.objecttypeid = objtype.objecttypeid
   LEFT JOIN tlkpcoveragetemporal covtemp ON tblobject.coveragetemporalid = covtemp.coveragetemporalid
   LEFT JOIN tlkpcoveragetemporalfoundation covtempfound ON tblobject.coveragetemporalfoundationid = covtempfound.coveragetemporalfoundationid
   LEFT JOIN (SELECT e.objectid AS masterobjectid, count(e.objectid) AS countofchildvmeasurements
              FROM tblelement e
              JOIN tblsample s ON s.elementid = e.elementid
              JOIN tblradius r ON r.sampleid = s.sampleid
              JOIN tblmeasurement m ON m.radiusid = r.radiusid
              JOIN tblvmeasurementderivedcache vc ON vc.measurementid = m.measurementid
              GROUP BY e.objectid) cquery 
        ON cquery.masterobjectid = tblobject.objectid;

GRANT ALL on vwtblobject to "Webgroup";

