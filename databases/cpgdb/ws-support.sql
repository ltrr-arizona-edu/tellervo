DROP VIEW vwDerivedCount CASCADE;
DROP VIEW vwDerivedTitles CASCADE;
DROP VIEW vwComprehensiveVM CASCADE;

CREATE VIEW vwDerivedCount AS
    SELECT vmeasurementid,COUNT(measurementid) as count,FIRST(measurementid) as firstMeasurementID FROM tblVMeasurementDerivedCache GROUP BY vmeasurementid;

CREATE VIEW vwDirectChildCount AS
	SELECT membervmeasurementid AS vmeasurementid, 
		COUNT(tblvmeasurementgroup.vmeasurementid) as directChildCount 
	FROM tblVMeasurementGroup 
	GROUP BY tblvmeasurementGroup.membervmeasurementid;
    
CREATE VIEW vwDerivedTitles AS
    SELECT dc.vmeasurementid, dc.count as derivedCount, dc.firstMeasurementID,
    (CASE WHEN dc.count = 1 THEN o.objectID ELSE NULL END) as objectID,
    (CASE WHEN dc.count = 1 THEN o.title ELSE NULL END) as objectTitle,
    (CASE WHEN dc.count = 1 THEN o.code ELSE NULL END) as objectCode,
    (CASE WHEN dc.count = 1 THEN e.code ELSE NULL END) as elementCode,
    (CASE WHEN dc.count = 1 THEN s.code ELSE NULL END) as sampleCode,
    (CASE WHEN dc.count = 1 THEN r.code ELSE NULL END) as radiusCode
    FROM vwDerivedCount dc
    LEFT JOIN tblMeasurement m ON m.measurementId = dc.firstMeasurementId
    LEFT JOIN tblRadius r ON r.radiusId = m.radiusId
    LEFT JOIN tblSample s ON s.sampleId = r.sampleId
    LEFT JOIN tblElement e ON e.elementId = s.elementId
    LEFT JOIN tblObject o ON o.objectId = e.objectId;

CREATE VIEW vwComprehensiveVM AS
    SELECT 
	vm.*, 
	mc.startyear, mc.readingcount, mc.measurementcount, mc.objectcode as objectCodeSummary, mc.objectcount, mc.commontaxonname, mc.taxoncount, mc.prefix, 
	m.radiusid, m.isreconciled, m.startyear as measurementStartYear, m.islegacycleaned, m.importtablename, m.measuredbyid, 
		m.datingtypeid, m.datingerrorpositive, m.datingerrornegative, m.measurementvariableid, m.unitid, m.power, m.provenance, 
		m.measuringmethodid, m.supervisedbyid,
	su.username, op.name as opname, dt.datingtype, 
	mc.vmextent AS extentgeometry,
	cd.crossdateid, cd.mastervmeasurementid, cd.startyear AS newstartyear, cd.justification, cd.confidence,
        der.objectID, der.objectTitle, der.objectCode, der.elementCode, der.sampleCode, der.radiusCode, dcc.directChildCount
    FROM tblvmeasurement vm
	INNER JOIN tlkpvmeasurementop op ON vm.vmeasurementopid=op.vmeasurementopid
	LEFT JOIN vwDerivedTitles der ON vm.vmeasurementid=der.vmeasurementid
	LEFT JOIN vwDirectChildCount dcc ON vm.vmeasurementid=dcc.vmeasurementid
	LEFT JOIN tblmeasurement m ON vm.measurementid=m.measurementid
	LEFT JOIN tblvmeasurementmetacache mc ON vm.vmeasurementid=mc.vmeasurementid
	LEFT JOIN tlkpdatingtype dt ON m.datingtypeid=dt.datingtypeid
	LEFT JOIN tblsecurityuser su ON vm.owneruserid=su.securityuserid
	LEFT JOIN tblcrossdate cd ON vm.vmeasurementid = cd.vmeasurementid;

GRANT SELECT on vwcomprehensivevm to "webuser";
