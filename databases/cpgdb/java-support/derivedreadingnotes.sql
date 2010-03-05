-- Copy over VMeasurementRelYearReadingNotes,
-- Marking old ones as inherited and deleting overrides as we go along
--
-- 1: The VMeasurementID we're applying
-- 2: The VMeasurementResultID we're applying
-- 3: The old VMeasurementResultID, if we're stealing these notes (INDEX) or null (everything else)
-- 4: The VMeasurementResultGroupID, if we're a SUM and we want to aggregate, or null (everything else)
CREATE OR REPLACE FUNCTION cpgdbj.applyDerivedReadingNotes(
	tblVMeasurement.VMeasurementID%TYPE, 					-- VMeasurementID we're applying
	tblVMeasurementResult.VMeasurementResultID%TYPE, 		-- VMeasurementResultID we're applying
	tblVMeasurementResult.VMeasurementResultID%TYPE,		-- VMeasurementResultID we're stealing results from (INDEX only!)
	tblVMeasurementResult.VMeasurementResultGroupID%TYPE	-- VMeasurementResultGroupID we're aggregating (SUM only!)
	)
RETURNS void AS $$
DECLARE
	inVMeasurementID ALIAS FOR $1;
	inVMeasurementResultID ALIAS FOR $2;
	oldVMeasurementResultID ALIAS FOR $3;
	inVMeasurementResultGroupID ALIAS FOR $4;

	curs refcursor;
	vmrn tblVMeasurementRelYearReadingNote%ROWTYPE;
BEGIN

	IF inVMeasurementResultGroupID IS NOT NULL THEN
		-- SUM
		-- Aggregate everything in this group, setting derivedCount properly
		
		INSERT INTO tblVMeasurementReadingNoteResult ( VMeasurementResultID, RelYear, ReadingNoteID, InheritedCount )
		SELECT inVMeasurementResultID AS VMeasurementResultID, RelYear, ReadingNoteID, COUNT(note.VMeasurementResultID)
			FROM tblVMeasurementResult res
			INNER JOIN tblVMeasurementReadingNoteResult note ON res.VMeasurementResultID=note.VMeasurementResultID
			WHERE VMeasurementResultGroupID=inVMeasurementResultGroupID
			GROUP BY note.RelYear, note.ReadingNoteID;
			
	ELSIF oldVMeasurementResultID IS NOT NULL THEN
		-- INDEX
	    -- Steal the notes from our child VMeasurement
		
		UPDATE tblVMeasurementReadingNoteResult SET inheritedCount=1,VMeasurementResultID=inVMeasurementResultID WHERE VMeasurementResultID=oldVMeasurementResultID;

	ELSE
		-- CLEAN, REDATE, CROSSDATE
		-- We work in place, so just mark everything as inherited
		UPDATE tblVMeasurementReadingNoteResult SET inheritedCount=1 WHERE VMeasurementResultID=inVMeasurementResultID;
		
	END IF;

	-- Now, find any directly applied "derived ring notes"
	FOR vmrn IN SELECT * FROM tblVMeasurementRelYearReadingNote
		WHERE VMeasurementID=inVMeasurementID
	LOOP
		-- Delete any notes that already exist - we're overriding!
		DELETE FROM tblVMeasurementReadingNoteResult WHERE VMeasurementResultID=inVMeasurementResultID AND RelYear=vmrn.RelYear;
	
		-- If we're not overriding for the sake of deleting, insert our notes
		IF vmrn.disabledOverride = FALSE THEN
			INSERT INTO tblVMeasurementReadingNoteResult (VMeasurementResultID, RelYear, ReadingNoteID) VALUES 
				(inVMeasurementResultID, vmrn.RelYear, vmrn.ReadingNoteID);
		END IF;
	END LOOP;
END;
$$ LANGUAGE PLPGSQL VOLATILE;
