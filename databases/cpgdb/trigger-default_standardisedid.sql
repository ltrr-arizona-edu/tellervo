CREATE OR REPLACE FUNCTION create_defaultreadingnotestandardisedid()
  RETURNS trigger AS
$BODY$BEGIN
RAISE NOTICE 'BEFORE: %,%,%', NEW.vocabularyid, NEW.standardisedid, NEW.note;
-- If entry is a TRiDaS type then make sure standardisedid is null 
IF NEW.vocabularyid=1 THEN
  NEW.standardisedid=NULL;
  RETURN NEW;
END IF;

-- IF entry does not include a standardisedid then default to the pkey
IF NEW.standardisedid IS NULL THEN
  NEW.standardisedid=NEW.readingnoteid;
END IF;
RAISE NOTICE 'AFTER: %,%,%', NEW.vocabularyid, NEW.standardisedid, NEW.note;
RETURN NEW;


END;$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION create_defaultreadingnotestandardisedid() OWNER TO aps03pwb;
GRANT EXECUTE ON FUNCTION create_defaultreadingnotestandardisedid() TO public;
GRANT EXECUTE ON FUNCTION create_defaultreadingnotestandardisedid() TO aps03pwb;
GRANT EXECUTE ON FUNCTION create_defaultreadingnotestandardisedid() TO "Webgroup";


CREATE TRIGGER default_standardisedid
  BEFORE INSERT OR UPDATE
  ON tlkpreadingnote
  FOR EACH ROW
  EXECUTE PROCEDURE create_defaultreadingnotestandardisedid();