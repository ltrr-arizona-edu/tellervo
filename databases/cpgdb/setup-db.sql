-- Set everything up nicely!
CREATE OR REPLACE FUNCTION cpgdb.populate_cache() RETURNS text AS $$
DECLARE
   vmid tblVSeries.VSeriesID%TYPE;
   ret text;
   cnt integer;
   badcnt integer;
   dummy text;
BEGIN
   cnt := 0;
   badcnt := 0;

   -- For every VSeries...
   FOR vmid in SELECT VSeriesID FROM tblVSeries ORDER BY CreatedTimestamp LOOP
      -- Populate the cache
      RAISE NOTICE 'Populating VSeries % cache...', vmid;
      cnt := cnt + 1;

      SELECT vseriesid INTO dummy FROM cpgdb.CreateMetaCache(vmid);

      IF dummy IS NULL THEN
         IF badcnt = 0 THEN
            ret := 'Bad: ';
         ELSE
            ret := ret || ', ';
         END IF;

         badcnt := badcnt + 1;
         ret := ret || vmid; 

         RAISE NOTICE 'Well, that didn\'t work for %', vmid;
      END IF;
   END LOOP;

   return 'Attempted to populate ' || cnt || ' vseries caches. ' || badcnt || ' failed. ' || ret;
END;
$$ LANGUAGE 'plpgsql' VOLATILE;

-- SELECT cpgdb.populate_cache();
-- DROP FUNCTION cpgdb.populate_cache();
