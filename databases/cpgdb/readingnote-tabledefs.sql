THIS FILE IS A PLACEHOLDER FOR THE TIME BEING

Don''t run me! It makes errors!

CREATE TABLE tblVMeasurementReadingNoteResult (
    	vMeasurementResultID uuid NOT NULL,
    	relYear integer NOT NULL,
	readingNoteID integer NOT NULL,
	inheritedCount integer NOT NULL default 0
);

CREATE INDEX "ind_result-relyear-noteid" ON tblVMeasurementReadingNoteResult USING btree (vmeasurementResultID, relYear, readingNoteID);
ALTER TABLE ONLY tblVMeasurementReadingNoteResult 
    ADD CONSTRAINT "fkey_noteresult-vmresult" FOREIGN KEY (vMeasurementResultID) REFERENCES tblVMeasurementResult(vMeasurementResultID) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE ONLY tblVMeasurementReadingNoteResult 
    ADD CONSTRAINT "fkey_noteresult-readingnote" FOREIGN KEY (readingNoteID) REFERENCES tlkpReadingNote(readingNoteID) ON UPDATE CASCADE ON DELETE RESTRICT;	

CREATE TABLE tblVMeasurementRelYearReadingNote (
	VMeasurementID uuid NOT NULL,
	relYear integer NOT NULL,
	readingNoteID integer NOT NULL,
	disabledOverride boolean NOT NULL default false,
	createdTimestamp timestamp with time zone NOT NULL default now(),
	lastModifiedTimestamp timestamp with time zone NOT NULL default now()
);

CREATE INDEX "ind_vmrelyearreadingnote-vmeasurementid" ON tblVMeasurementRelYearReadingNote USING btree(VMeasurementID);
ALTER TABLE ONLY tblVMeasurementRelYearReadingNote 
    ADD CONSTRAINT "fkey_relyearreadingnote-vmeasurement" FOREIGN KEY (vmeasurementID) REFERENCES tblVMeasurement(vmeasurementID) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE ONLY tblVMeasurementRelYearReadingNote 
    ADD CONSTRAINT "fkey_relyearreadingnote-readingnote" FOREIGN KEY (readingNoteID) REFERENCES tlkpReadingNote(readingNoteID) ON UPDATE CASCADE ON DELETE RESTRICT;
