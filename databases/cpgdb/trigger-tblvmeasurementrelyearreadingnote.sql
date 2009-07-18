CREATE OR REPLACE FUNCTION cpgdb.VMeasurementRelYearNoteTrigger() RETURNS trigger AS $$
DECLARE
   OpName text;
BEGIN
   SELECT op.name INTO OpName
      FROM tblVMeasurement 
      INNER JOIN tlkpvmeasurementop op USING(VMeasurementOpID)
      WHERE VMeasurementID=NEW.VMeasurementID;

   IF NOT FOUND THEN
      RAISE EXCEPTION 'Invalid or nonexistent VMeasurement %. Cannot attach reading notes.', NEW.VMeasurementID;
   END IF;

   IF OpName = 'Direct' THEN
      RAISE EXCEPTION 'Cannot attach RelYearReadingNotes to a direct VMeasurement. Attach directly to readings instead.';
   END IF;

   RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

DROP TRIGGER check_vm_is_derived ON tblVMeasurementRelYearReadingNote;
CREATE TRIGGER check_vm_is_derived BEFORE INSERT OR UPDATE ON tblVMeasurementRelYearReadingNote
   FOR EACH ROW EXECUTE PROCEDURE cpgdb.VMeasurementRelYearNoteTrigger();

