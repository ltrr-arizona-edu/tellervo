DROP VIEW vwResultNotesAsArray CASCADE;
CREATE VIEW vwResultNotesAsArray AS
   SELECT RelYear, VMeasurementResultID,
      array_to_string(array_accum(ReadingNoteID), ',') as NoteIDs, 
      array_to_string(array_accum(InheritedCount), ',') as InheritedCounts
   FROM tblVMeasurementReadingNoteResult
   GROUP BY RelYear,VMeasurementResultID;
   
CREATE VIEW vwNotedReadingResult AS
   SELECT res.*,notes.NoteIDs,notes.InheritedCounts FROM tblVMeasurementReadingResult res
   LEFT JOIN vwResultNotesAsArray notes ON 
       (res.VMeasurementResultID=notes.VMeasurementResultID AND res.RelYear=notes.relYear);

GRANT ALL ON vwResultNotesAsArray TO "Webgroup";
GRANT ALL ON vwNotedReadingResult TO "Webgroup";

