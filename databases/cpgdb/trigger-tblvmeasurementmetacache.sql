CREATE OR REPLACE FUNCTION cpgdb.VMeasurementModifiedCacheTrigger() RETURNS trigger AS $$
BEGIN
   -- If this VMeasurement is being generated, ignore it
   IF NEW.isGenerating = TRUE THEN
      RETURN NEW;
   END IF;

   PERFORM cpgdb.CreateMetaCache(NEW.VMeasurementID);
   RETURN NEW;
EXCEPTION
   WHEN internal_error THEN
      RAISE NOTICE 'WARNING: Failed to create metacache for %', NEW.VMeasurementID;
      RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

DROP TRIGGER update_vmeasurementmetacache ON tblVMeasurement;
CREATE TRIGGER update_vmeasurementmetacache AFTER INSERT OR UPDATE ON tblVMeasurement
   FOR EACH ROW EXECUTE PROCEDURE cpgdb.VMeasurementModifiedCacheTrigger();
