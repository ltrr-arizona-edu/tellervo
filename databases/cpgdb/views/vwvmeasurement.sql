DROP VIEW vwvmeasurement;

CREATE VIEW vwvmeasurement AS
 SELECT tblvmeasurementresult.vmeasurementid, tblvmeasurement.measurementid, tblvmeasurementresult.radiusid, tblvmeasurementresult.isreconciled, tblvmeasurementresult.startyear, 
        tblvmeasurementresult.createdtimestamp AS measurementcreated, tblvmeasurementresult.lastmodifiedtimestamp AS measurementlastmodified
   FROM tblvmeasurementresult, tblvmeasurement
  WHERE tblvmeasurementresult.vmeasurementid = tblvmeasurement.vmeasurementid;

GRANT ALL on vwtblradius to "Webgroup";

