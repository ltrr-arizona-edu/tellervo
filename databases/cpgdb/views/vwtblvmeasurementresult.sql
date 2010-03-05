DROP VIEW vwtblvmeasurementresult;

CREATE VIEW vwtblvmeasurementresult AS
 SELECT tblvmeasurementresult.vmeasurementresultid, tblvmeasurementresult.vmeasurementid, tblvmeasurementresult.isreconciled AS measurementisreconciled, tblvmeasurementresult.islegacycleaned, 
        tblvmeasurementresult.datingerrorpositive, tblvmeasurementresult.datingerrornegative, tlkpdatingtype.datingtype
   FROM tblvmeasurementresult, tlkpdatingtype
  WHERE tblvmeasurementresult.datingtypeid = tlkpdatingtype.datingtypeid;

GRANT ALL on vwtblvmeasurementresult to "Webgroup";

