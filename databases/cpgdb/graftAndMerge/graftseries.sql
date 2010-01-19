-- Function: cpgdb.graftseries(uuid, uuid, cpgdb.graftpoint)

-- DROP FUNCTION cpgdb.graftseries(uuid, uuid, cpgdb.graftpoint);

CREATE OR REPLACE FUNCTION cpgdb.graftseries(goodseriesid uuid, badseriesid uuid, graftpoint cpgdb.graftpoint)
  RETURNS vwcomprehensivevm AS
$BODY$DECLARE
goodseriesid ALIAS FOR $1;               -- The ID of the series with correct entities 
gooduuids cpgdb.typfulluuids%ROWTYPE;    -- The UUIDs of the entities for this series
badseriesid ALIAS FOR $2;                -- The ID of the series with incorrect entities
baduuids cpgdb.typfulluuids%ROWTYPE;     -- The UUIDs of the entities for this series
graftpoint ALIAS FOR $3;                 -- The entity at which point the graft should be done
cleanedseries vwcomprehensivevm%ROWTYPE; -- The cleaned up series

BEGIN

-- Get good and bad id's for all entities associated with these series
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

-- Requery to get updated details of the 'bad' series once it has been merged 
SELECT * INTO cleanedseries FROM vwcomprehensivevm WHERE vmeasurementid=badseriesid;

RETURN cleanedseries;
END;$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.graftseries(uuid, uuid, cpgdb.graftpoint) OWNER TO aps03pwb;
