
CREATE OR REPLACE VIEW vwcomprehensivevm AS 
 SELECT vm.vmeasurementid, vm.measurementid, vm.vmeasurementopid, vm.vmeasurementopparameter, 
 vm.code, vm.comments, vm.ispublished, vm.owneruserid, vm.createdtimestamp, vm.lastmodifiedtimestamp, 
 vm.isgenerating, vm.objective, vm.version, vm.birthdate, mc.startyear, mc.readingcount, 
 mc.measurementcount, mc.objectcode AS objectcodesummary, mc.objectcount, mc.commontaxonname, 
 mc.taxoncount, mc.prefix, m.radiusid, m.isreconciled, m.startyear AS measurementstartyear, 
 m.islegacycleaned, m.importtablename, m.measuredbyid, m.datingtypeid, m.datingerrorpositive, 
 m.datingerrornegative, m.measurementvariableid, m.unitid, m.power, m.provenance, m.measuringmethodid, 
 m.supervisedbyid, su.username, op.name AS opname, dt.datingtype, mc.vmextent AS extentgeometry, 
 cd.crossdateid, cd.mastervmeasurementid, cd.startyear AS newstartyear, cd.justification, cd.confidence, 
 der.objectid, der.objecttitle, der.objectcode, der.elementcode, der.samplecode, der.radiuscode, 
 dcc.directchildcount
   FROM tblvmeasurement vm
   JOIN tlkpvmeasurementop op ON vm.vmeasurementopid = op.vmeasurementopid
   LEFT JOIN vwderivedtitles der ON vm.vmeasurementid = der.vmeasurementid
   LEFT JOIN vwdirectchildcount dcc ON vm.vmeasurementid = dcc.vmeasurementid
   LEFT JOIN tblmeasurement m ON vm.measurementid = m.measurementid
   LEFT JOIN tblvmeasurementmetacache mc ON vm.vmeasurementid = mc.vmeasurementid
   LEFT JOIN tlkpdatingtype dt ON m.datingtypeid = dt.datingtypeid
   LEFT JOIN tblsecurityuser su ON vm.owneruserid = su.securityuserid
   LEFT JOIN tblcrossdate cd ON vm.vmeasurementid = cd.vmeasurementid;

ALTER TABLE vwcomprehensivevm OWNER TO lucasm;
GRANT ALL ON TABLE vwcomprehensivevm TO lucasm;
GRANT SELECT ON TABLE vwcomprehensivevm TO webuser;
GRANT ALL on vwcomprehensivevm to "Webgroup";

