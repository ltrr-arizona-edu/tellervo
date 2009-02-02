CREATE VIEW vwComprehensiveVM AS
    SELECT 
	vm.*, 
	mc.startyear, mc.readingcount, mc.measurementcount, mc.objectcode, mc.objectcount, mc.commontaxonname, mc.taxoncount, mc.prefix, 
	m.radiusid, m.isreconciled, m.startyear as measurementStartYear, m.islegacycleaned, m.importtablename, m.measuredbyid, 
		m.datingtypeid, m.datingerrorpositive, m.datingerrornegative, m.measurementvariableid, m.unitid, m.power, m.provenance, 
		m.measuringmethodid, m.supervisedbyid,
	su.username, op.name as opname, dt.datingtype, 
	x(centroid(mc.vmextent)), y(centroid(mc.vmextent)), xmin(mc.vmextent), xmax(mc.vmextent), ymin(mc.vmextent), ymax(mc.vmextent) 
    FROM tblvmeasurement vm
	INNER JOIN tlkpvmeasurementop op ON vm.vmeasurementopid=op.vmeasurementopid
	LEFT JOIN tblmeasurement m ON vm.measurementid=m.measurementid
	LEFT JOIN tblvmeasurementmetacache mc ON vm.vmeasurementid=mc.vmeasurementid
	LEFT JOIN tlkpdatingtype dt ON m.datingtypeid=dt.datingtypeid
	LEFT JOIN tblsecurityuser su ON vm.owneruserid=su.securityuserid;
