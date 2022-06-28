CREATE OR REPLACE FUNCTION cpgdb.ElementLocationChangedTrigger() RETURNS trigger AS $$
DECLARE
   vmid tblVMeasurement.VMeasurementID%TYPE;
   newextent geometry;
BEGIN
   -- no changes? don't bother!
   IF NEW.locationgeometry = OLD.locationgeometry THEN
      RETURN NEW;
   END IF;

   -- update all child vmeasurement extents
   FOR vmid IN SELECT vmeasurementID FROM cpgdb.FindChildrenOf('Element', NEW.elementID) LOOP
      -- Calculate extent of vmeasurement by looking up locations of all associated direct Measurements
      SELECT st_setsrid(extent(tblelement.locationgeometry)::geometry,4326)
         INTO  newextent
         FROM  tblelement, tblsample, tblradius, tblMeasurement, tblvmeasurement
         WHERE tblvmeasurement.measurementid=tblmeasurement.measurementid
         AND   tblmeasurement.radiusid=tblradius.radiusid
         AND   tblradius.sampleid=tblsample.sampleid
         AND   tblsample.elementid=tblelement.elementid
         AND   tblvmeasurement.vmeasurementid
            IN (SELECT vMeasurementid
                   FROM  cpgdb.FindVMParents(vmid, true)
                   WHERE op='Direct');

      -- set the extent in the metacache
      UPDATE tblVMeasurementMetaCache SET vmextent=newextent WHERE vMeasurementID=vmid;

   END LOOP;

   RETURN NEW;
EXCEPTION
   WHEN internal_error THEN
      RAISE NOTICE 'WARNING: Failed to update child extents for %', NEW.elementID;
      RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

DROP TRIGGER update_vmeasurementcache_extents ON tblElement;
CREATE TRIGGER update_vmeasurementcache_extents AFTER UPDATE ON tblElement
   FOR EACH ROW EXECUTE PROCEDURE cpgdb.ElementLocationChangedTrigger();
