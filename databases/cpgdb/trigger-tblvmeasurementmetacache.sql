CREATE OR REPLACE FUNCTION cpgdb.VMeasurementModifiedTrigger() RETURNS trigger AS $$
BEGIN
   -- If this VMeasurement is being generated, ignore it
   IF NEW.isGenerating = TRUE THEN
      RETURN NEW;
   END IF;

   -- update timestamp
   NEW.lastModifiedTimestamp = current_timestamp;

   -- update metacache
   PERFORM cpgdb.CreateMetaCache(NEW.VMeasurementID);

   RETURN NEW;
EXCEPTION
   WHEN internal_error THEN
      RAISE NOTICE 'WARNING: Failed to create metacache for %', NEW.VMeasurementID;
      RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

DROP TRIGGER update_vmeasurement ON tblVMeasurement;
CREATE TRIGGER update_vmeasurement AFTER INSERT OR UPDATE ON tblVMeasurement
   FOR EACH ROW EXECUTE PROCEDURE cpgdb.VMeasurementModifiedTrigger();
