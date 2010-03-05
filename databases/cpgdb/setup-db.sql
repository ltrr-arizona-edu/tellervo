-- Set everything up nicely!
CREATE OR REPLACE FUNCTION cpgdb.populate_cache() RETURNS text AS $$
DECLARE
   vsid tblVMeasurement.VMeasurementID%TYPE;
   ret text;
   cnt integer;
   badcnt integer;
   dummy text;
BEGIN
   cnt := 0;
   badcnt := 0;

   -- For every VMeasurement...
   FOR vsid in SELECT VMeasurementID FROM tblVMeasurement ORDER BY CreatedTimestamp LOOP
      -- Populate the cache
      RAISE NOTICE 'Populating VMeasurement % cache...', vsid;
      cnt := cnt + 1;

      SELECT vMeasurementid INTO dummy FROM cpgdb.CreateMetaCache(vsid);

      IF dummy IS NULL THEN
         IF badcnt = 0 THEN
            ret := 'Bad: ';
         ELSE
            ret := ret || ', ';
         END IF;

         badcnt := badcnt + 1;
         ret := ret || vsid; 

         RAISE NOTICE 'Well, that didn\'t work for %', vsid;
      END IF;
   END LOOP;

   return 'Attempted to populate ' || cnt || ' vmeasurement caches. ' || badcnt || ' failed. ' || ret;
END;
$$ LANGUAGE 'plpgsql' VOLATILE;

-- SELECT cpgdb.populate_cache();
-- DROP FUNCTION cpgdb.populate_cache();

-- Fill in any gaps!
CREATE OR REPLACE FUNCTION cpgdb.populate_cache_gaps(maxcnt integer) RETURNS text AS $$
DECLARE
   vsid tblVMeasurement.VMeasurementID%TYPE;
   ret text;
   cnt integer;
   badcnt integer;
   dummy text;
BEGIN
   cnt := 0;
   badcnt := 0;

   -- For every VMeasurement...
   FOR vsid in SELECT vm.VMeasurementID FROM tblVMeasurement vm LEFT JOIN tblVMeasurementMetaCache vmc USING (vmeasurementid) WHERE vmc.StartYear IS NULL ORDER BY CreatedTimestamp LOOP
      -- Populate the cache
      RAISE NOTICE 'Populating VMeasurement % cache...', vsid;
      cnt := cnt + 1;

      SELECT vMeasurementid INTO dummy FROM cpgdb.CreateMetaCache(vsid);

      IF dummy IS NULL THEN
         IF badcnt = 0 THEN
            ret := 'Bad: ';
         ELSE
            ret := ret || ', ';
         END IF;

         badcnt := badcnt + 1;
         ret := ret || vsid; 

         RAISE NOTICE 'Well, that didn\'t work for %', vsid;
      END IF;

      IF maxcnt > 0 AND cnt >= maxcnt THEN
         return 'Attempted to populate ' || cnt || ' vmeasurement caches. ' || badcnt || ' failed. ' || ret;
      END IF;
   END LOOP;

   return 'Attempted to populate ' || cnt || ' vmeasurement caches. ' || badcnt || ' failed. ' || ret;
END;
$$ LANGUAGE 'plpgsql' VOLATILE;

CREATE OR REPLACE FUNCTION cpgdb.populate_cache_gaps() RETURNS text AS $$
   SELECT * from cpgdb.populate_cache_gaps(-1);
$$ LANGUAGE SQL VOLATILE;

