CREATE OR REPLACE FUNCTION cpgdb.VSeriesModifiedCacheTrigger() RETURNS trigger AS $$
BEGIN
   -- If this VSeries is being generated, ignore it
   IF NEW.isGenerating = TRUE THEN
      RETURN NEW;
   END IF;

   PERFORM cpgdb.CreateMetaCache(NEW.VSeriesID);
   RETURN NEW;
EXCEPTION
   WHEN internal_error THEN
      RAISE NOTICE 'WARNING: Failed to create metacache for %', NEW.VSeriesID;
      RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

DROP TRIGGER update_vseriesmetacache ON tblVSeries;
CREATE TRIGGER update_vseriesmetacache AFTER INSERT OR UPDATE ON tblVSeries
   FOR EACH ROW EXECUTE PROCEDURE cpgdb.VSeriesModifiedCacheTrigger();
