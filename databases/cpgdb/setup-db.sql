-- Set everything up nicely!
CREATE FUNCTION cpgdb.set_it_all_up() RETURNS void AS $$
DECLARE
   vmid tblVMeasurement.VMeasurementID%TYPE;
BEGIN
   -- For every VMeasurement...
   FOR vmid in SELECT VMeasurementID FROM tblVMeasurement LOOP
      -- Populate the cache
      RAISE NOTICE 'Populating VMeasurement % cache...', vmid;
      PERFORM cpgdb.CreateMetaCache(vmid);
   END LOOP;
END;
$$ LANGUAGE 'plpgsql' VOLATILE;

SELECT cpgdb.set_it_all_up();
DROP FUNCTION cpgdb.set_it_all_up();
