CREATE OR REPLACE FUNCTION cpgdb.VMeasurementModifiedCacheTrigger() RETURNS trigger AS $$
BEGIN
   PERFORM cpgdb.CreateMetaCache(NEW.VMeasurementID);
   RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

DROP TRIGGER update_vmeasurementmetacache ON tblVMeasurement;
CREATE TRIGGER update_vmeasurementmetacache AFTER INSERT OR UPDATE ON tblVMeasurement
   FOR EACH ROW EXECUTE PROCEDURE cpgdb.VMeasurementModifiedCacheTrigger();
