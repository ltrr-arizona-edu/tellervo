DECLARE
goodseriesid ALIAS FOR $1;             -- The ID of the series with correct entities 
gooduuids cpgdb.typfulluuids%ROWTYPE;  -- The UUIDs of the entities for this series

badseriesid ALIAS FOR $2;              -- The ID of the series with incorrect entities
baduuids cpgdb.typfulluuids%ROWTYPE;   -- The UUIDs of the entities for this series
badseries vwcomprehensivevm%ROWTYPE;   -- The series itself

graftpoint ALIAS FOR $3;               -- The entity at which the graft should be performed


BEGIN

-- Get good and bad id's for all entities
SELECT e.elementid, s.sampleid, r.radiusid, vm.vmeasurementid as seriesid INTO gooduuids
FROM tblvmeasurement vm 
LEFT JOIN tblmeasurement m ON vm.measurementid=m.measurementid
LEFT JOIN tblradius r ON m.radiusid=r.radiusid
LEFT JOIN tblsample s ON r.sampleid = s.sampleid
LEFT JOIN tblelement e ON s.elementid = e.elementid
WHERE vm.vmeasurementid=goodseriesid;
SELECT e.elementid, s.sampleid, r.radiusid, vm.vmeasurementid as seriesid INTO baduuids
FROM tblvmeasurement vm 
LEFT JOIN tblmeasurement m ON vm.measurementid=m.measurementid
LEFT JOIN tblradius r ON m.radiusid=r.radiusid
LEFT JOIN tblsample s ON r.sampleid = s.sampleid
LEFT JOIN tblelement e ON s.elementid = e.elementid
WHERE vm.vmeasurementid=badseriesid;

-- Depending on graftpoint start merging
IF (graftpoint='element'::cpgdb.graftpoint) THEN
   PERFORM cpgdb.mergeelements(gooduuids.elementid, baduuids.elementid);
ELSIF (graftpoint='sample'::cpgdb.graftpoint) THEN
   PERFORM cpgdb.mergesamples(gooduuids.sampleid, baduuids.sampleid);
ELSIF (graftpoint='radius'::cpgdb.graftpoint) THEN
   PERFORM cpgdb.mergeradii(gooduuids.radiusid, baduuids.radiusid); 
END IF;

-- Requiry to get updated details of the 'bad' series
SELECT * INTO badseries FROM vwcomprehensivevm WHERE vmeasurementid=badseriesid;

RETURN badseries;
END;