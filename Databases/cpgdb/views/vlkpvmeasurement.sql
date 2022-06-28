DROP VIEW vlkpvmeasurement;

CREATE VIEW vlkpvmeasurement AS
 SELECT tblvmeasurement.vmeasurementid, tblvmeasurement.code AS name, tlkpvmeasurementop.name AS op
   FROM tlkpvmeasurementop
   JOIN tblvmeasurement ON tlkpvmeasurementop.vmeasurementopid = tblvmeasurement.vmeasurementopid
  ORDER BY tblvmeasurement.code;

