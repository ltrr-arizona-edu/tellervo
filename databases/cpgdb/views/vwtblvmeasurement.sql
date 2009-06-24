DROP VIEW vwtblvmeasurement;

CREATE VIEW vwtblvmeasurement AS
 SELECT tblvmeasurement.vmeasurementid, tblvmeasurement.measurementid, tblvmeasurement.vmeasurementopid, tlkpvmeasurementop.name AS measurementoperator, tblvmeasurement.vmeasurementopparameter AS 
        operatorparameter, tblvmeasurement.code AS measurementname, tblvmeasurement.comments AS measurementdescription, tblvmeasurement.ispublished AS measurementispublished, 
        tblvmeasurement.owneruserid AS measurementowneruserid, tblvmeasurement.createdtimestamp AS measurementcreated, tblvmeasurement.lastmodifiedtimestamp AS measurementlastmodified
   FROM tblvmeasurement, tlkpvmeasurementop
  WHERE tblvmeasurement.vmeasurementopid = tlkpvmeasurementop.vmeasurementopid;

