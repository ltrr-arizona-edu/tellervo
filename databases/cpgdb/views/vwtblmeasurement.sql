DROP VIEW vwtblmeasurement;

CREATE VIEW vwtblmeasurement AS
 SELECT tblmeasurement.measurementid, tblmeasurement.radiusid, tblmeasurement.isreconciled AS measurementidreconciled, tblmeasurement.islegacycleaned, tblmeasurement.importtablename, 
        tblmeasurement.measuredbyid, tblsecurityuser.username AS measuredbyusername, tlkpdatingtype.datingtype, tblmeasurement.datingerrorpositive, tblmeasurement.datingerrornegative
   FROM tblmeasurement, tblsecurityuser, tlkpdatingtype
  WHERE tblmeasurement.measuredbyid = tblsecurityuser.securityuserid AND tblmeasurement.datingtypeid = tlkpdatingtype.datingtypeid;

GRANT ALL on vwtblradius to "Webgroup";

