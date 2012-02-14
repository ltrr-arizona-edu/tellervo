-- Erase all reading notes for this VMeasurementID
CREATE OR REPLACE FUNCTION cpgdb.ClearReadingNotes(
   tblVMeasurement.VMeasurementID%TYPE 	-- the vmeasurement to tie this to
) RETURNS integer AS $_$
DECLARE
   _VMID ALIAS FOR $1;

   VMOp text;
   ret integer;
BEGIN

   -- Get the VMeasurementOp
   SELECT op.name INTO VMOp FROM tblVMeasurement 
      INNER JOIN tlkpVMeasurementOp op USING (vmeasurementOpID)
      WHERE vmeasurementID=_VMID;

   IF NOT FOUND THEN
      RAISE EXCEPTION 'Invalid/nonexistent vmeasurement %', _VMID;
   END IF;

   -- Just delete all the notes!
   IF VMOp = 'Direct' THEN
      DELETE FROM tblReadingReadingNote WHERE ReadingID IN
         (SELECT readingID FROM tblVMeasurement
            INNER JOIN tblMeasurement USING (measurementID)
            INNER JOIN tblReading USING (measurementID)
            INNER JOIN tblReadingReadingNote USING (readingID)
            WHERE VMeasurementID=_VMID);
   ELSE
      DELETE FROM tblVMeasurementRelYearReadingNote WHERE VMeasurementID=_VMID;
   END IF;

   GET DIAGNOSTICS ret = ROW_COUNT;   
   RETURN ret;
END;
$_$ LANGUAGE PLPGSQL VOLATILE;

-- Add a reading note to this vmeasurement
CREATE OR REPLACE FUNCTION cpgdb.AddReadingNote(
   tblVMeasurement.VMeasurementID%TYPE, -- the vmeasurement to tie this to
   int,					-- The year to bind this note to (relative, starts at zero)
   int,                                 -- The reading note ID to add
   boolean)                             -- Is this a force disabled override (only for non-direct, MUST BE NULL for direct)
RETURNS void AS $_$
DECLARE
   _VMID ALIAS FOR $1;
   _RelYear ALIAS FOR $2;
   _ReadingNoteID ALIAS FOR $3;
   _Override ALIAS FOR $4;

   VMOp text;
   myReadingID tblReading.ReadingID%TYPE;
   myOverride boolean;
   VMReadingCount int;
BEGIN

   -- Get the VMeasurementOp
   SELECT op.name INTO VMOp FROM tblVMeasurement 
      INNER JOIN tlkpVMeasurementOp op USING (vmeasurementOpID)
      WHERE vmeasurementID=_VMID;

   IF NOT FOUND THEN
      RAISE EXCEPTION 'Invalid/nonexistent vmeasurement %', _VMID;
   END IF;

   -- Direct VM -> Attach to Reading
   IF VMOp = 'Direct' THEN
      IF _Override IS NOT NULL THEN
         RAISE EXCEPTION 'Override must not be set for Direct VMs';
      END IF;

      SELECT readingID INTO myReadingID 
         FROM tblVMeasurement 
         INNER JOIN tblMeasurement USING (measurementID) 
         INNER JOIN tblReading USING (measurementID) 
         WHERE VMeasurementID=_VMID AND RelYear=_RelYear;

      IF NOT FOUND THEN
         RAISE EXCEPTION 'No reading found for relyear % on vmeasurementid %. Year must exist.', _RelYear, _VMID;
      END IF;

      INSERT INTO tblReadingReadingNote (ReadingID, ReadingNoteID) VALUES (myReadingID, _ReadingNoteID);
   ELSE
      -- Derived VM -> Attach to vmeasurement
      myOverride := _Override;

      -- null override means false
      IF myOverride IS NULL THEN
         myOverride := FALSE;
      END IF;

      -- Validate relyear
      SELECT readingCount INTO VMReadingCount FROM tblVMeasurementMetaCache WHERE VMeasurementID=_VMID;
      IF NOT FOUND OR VMReadingCount <= _RelYear THEN
         RAISE EXCEPTION 'VMeasurement % does not exist, is malformed, or relyear % is out of range.', _VMID, _RelYear;
      END IF;

      INSERT INTO tblVMeasurementRelYearReadingNote (VMeasurementID, RelYear, ReadingNoteID, DisabledOverride)
         VALUES (_VMID, _RelYear, _ReadingNoteID, myOverride);
   END IF;
END;
$_$ LANGUAGE PLPGSQL VOLATILE;

-- Add a CUSTOM reading note to this vmeasurement
-- Tries to find an existing match first
CREATE OR REPLACE FUNCTION cpgdb.AddCustomReadingNote(
   tblVMeasurement.VMeasurementID%TYPE, -- the vmeasurement to tie this to
   int,					-- The year to bind this note to (relative, starts at zero)
   text)                                -- The custom reading note to add
RETURNS tlkpReadingNote AS $_$
DECLARE
   _VMID ALIAS FOR $1;
   _RelYear ALIAS FOR $2;
   _NoteText ALIAS FOR $3;

   VMOp text;
   myReadingID tblReading.ReadingID%TYPE;
   myOverride boolean;
   VMReadingCount int;
   myReadingNoteID tlkpReadingNote.ReadingNoteID%TYPE;
   myVMRYRN tblVMeasurementRelYearReadingNote.RelYearReadingNoteID%TYPE;

   outrow tlkpReadingNote%ROWTYPE;
BEGIN

   -- Look for existing vocabulary case insensitively
   SELECT ReadingNoteID INTO myReadingNoteID FROM tlkpReadingNote WHERE note ILIKE _NoteText;

   IF FOUND THEN
      PERFORM cpgdb.AddReadingNote(_VMID, _RelYear, myReadingNoteID, null);
      SELECT * INTO outrow FROM tlkpReadingNote WHERE ReadingNoteID=myReadingNoteID;
      RETURN outrow;
   END IF;

   -- Get the VMeasurementOp
   SELECT op.name INTO VMOp FROM tblVMeasurement 
      INNER JOIN tlkpVMeasurementOp op USING (vmeasurementOpID)
      WHERE vmeasurementID=_VMID;

   IF NOT FOUND THEN
      RAISE EXCEPTION 'Invalid/nonexistent vmeasurement %', _VMID;
   END IF;

   -- Create a new reading note
   myReadingNoteID := nextval('tlkpreadingnote_readingnoteid_seq'::regclass);

   -- Direct VM -> Attach to Reading
   IF VMOp = 'Direct' THEN
      IF _Override IS NOT NULL THEN
         RAISE EXCEPTION 'Override must not be set for Direct VMs';
      END IF;

      SELECT readingID INTO myReadingID 
         FROM tblVMeasurement 
         INNER JOIN tblMeasurement USING (measurementID) 
         INNER JOIN tblReading USING (measurementID) 
         WHERE VMeasurementID=_VMID AND RelYear=_RelYear;

      IF NOT FOUND THEN
         RAISE EXCEPTION 'No reading found for relyear % on vmeasurementid %. Year must exist.', _RelYear, _VMID;
      END IF;

      INSERT INTO tlkpReadingNote(ReadingNoteID, note, vocabularyid, parentReadingID) VALUES (myReadingNoteID, _NoteText, 0, myReadingID);
      INSERT INTO tblReadingReadingNote (ReadingReadingNoteID, ReadingID, ReadingNoteID) VALUES (myRRNID, myReadingID, _ReadingNoteID);
   ELSE
      -- Validate relyear
      SELECT readingCount INTO VMReadingCount FROM tblVMeasurementMetaCache WHERE VMeasurementID=_VMID;
      IF NOT FOUND OR VMReadingCount <= _RelYear THEN
         RAISE EXCEPTION 'VMeasurement % does not exist, is malformed, or relyear % is out of range.', _VMID, _RelYear;
      END IF;

      -- Complicated circular reference here :<

      -- First, insert the new readingNote
      INSERT INTO tlkpReadingNote(ReadingNoteID, note, vocabularyid) VALUES (myReadingNoteID, _NoteText, 0);

      -- Now, insert a new tblVMeasurementRelYearReadingNote row
      myVMRYRN := nextval('tblvmeasurementrelyearreadingnote_relyearreadingnoteid_seq'::regclass);
      INSERT INTO tblVMeasurementRelYearReadingNote (RelYearReadingNoteID, VMeasurementID, RelYear, ReadingNoteID)
         VALUES (myVMRYRN, _VMID, _RelYear, myReadingNoteID);

      -- Now, tie the readingNote directly to the new row
      UPDATE tlkpReadingNote SET parentVMRelYearReadingNoteID=myVMRYRN WHERE ReadingNoteID=myReadingNoteID;
   END IF;

   SELECT * INTO outrow FROM tlkpReadingNote WHERE ReadingNoteID=myReadingNoteID;
   RETURN outrow;
END;
$_$ LANGUAGE PLPGSQL VOLATILE;



-- Remove a reading note from this vmeasurement
CREATE OR REPLACE FUNCTION cpgdb.RemoveReadingNote(
   tblVMeasurement.VMeasurementID%TYPE, -- the vmeasurement to tie this to
   int,					-- The year to bind this note to (relative, starts at zero)
   int)                                 -- The reading note ID to add
RETURNS integer AS $_$
DECLARE
   _VMID ALIAS FOR $1;
   _RelYear ALIAS FOR $2;
   _ReadingNoteID ALIAS FOR $3;

   VMOp text;
   myReadingID tblReading.ReadingID%TYPE;
   ret integer;
BEGIN

   -- Get the VMeasurementOp
   SELECT op.name INTO VMOp FROM tblVMeasurement 
      INNER JOIN tlkpVMeasurementOp op USING (vmeasurementOpID)
      WHERE vmeasurementID=_VMID;

   IF NOT FOUND THEN
      RAISE EXCEPTION 'Invalid/nonexistent vmeasurement %', _VMID;
   END IF;

   -- Direct VM -> Remove from Reading
   IF VMOp = 'Direct' THEN
      SELECT readingID INTO myReadingID 
         FROM tblVMeasurement 
         INNER JOIN tblMeasurement USING (measurementID) 
         INNER JOIN tblReading USING (measurementID) 
         WHERE VMeasurementID=_VMID AND RelYear=_RelYear;

      IF NOT FOUND THEN
         RAISE EXCEPTION 'No reading found for relyear % on vmeasurementid %. Year must exist.', _RelYear, _VMID;
      END IF;

      DELETE FROM tblReadingReadingNote WHERE ReadingID=myReadingID AND ReadingNoteID=_ReadingNoteID;
   ELSE
      -- Remove from RelYearReadingNote
      DELETE FROM tblVMeasurementRelYearReadingNote WHERE VMeasurementID=_VMID AND RelYear=_RelYear AND ReadingNoteID=_ReadingNoteID;
   END IF;

   GET DIAGNOSTICS ret = ROW_COUNT;   
   RETURN ret;
END;
$_$ LANGUAGE PLPGSQL VOLATILE;


-- Param 1: Vocabulary name
-- Param 2: Note text
CREATE OR REPLACE FUNCTION cpgdb.GetNote(text, text) RETURNS tlkpReadingNote AS $_$
DECLARE
   noterec tlkpReadingNote;
BEGIN
   SELECT rnote.* INTO noterec
      FROM tlkpReadingNote rnote 
      LEFT JOIN tlkpVocabulary voc USING (vocabularyid) 
      WHERE voc.name=$1 AND rnote.note=$2;

   IF NOT FOUND THEN
      RAISE EXCEPTION 'Note "%" not found for vocabulary "%"', $2, $1;
   END IF;

   RETURN noterec;
END;
$_$ LANGUAGE PLPGSQL STABLE;
