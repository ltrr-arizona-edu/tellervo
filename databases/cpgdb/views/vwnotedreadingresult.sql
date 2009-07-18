DROP VIEW vwResultNotesAsArray CASCADE;
CREATE VIEW vwResultNotesAsArray AS
   SELECT RelYear, VMeasurementResultID,
      array_accum(ReadingNoteID) as NoteIDs, 
      array_accum(InheritedCount) as InheritedCounts
   FROM tblVMeasurementReadingNoteResult
   GROUP BY RelYear,VMeasurementResultID;
   
CREATE VIEW vwNotedReadingResult AS
   SELECT res.*,
          array_to_string(notes.NoteIDs,',') as NoteIDs,
          array_to_string(notes.InheritedCounts,',') as InheritedCounts
   FROM tblVMeasurementReadingResult res
   LEFT JOIN vwResultNotesAsArray notes ON 
       (res.VMeasurementResultID=notes.VMeasurementResultID AND res.RelYear=notes.relYear);

CREATE VIEW vwJSONNotedReadingResult AS
   SELECT res.*,cpgdb.resultNotesToJSON(notes.NoteIDs,notes.InheritedCounts) AS JSONnotes
   FROM tblVMeasurementReadingResult res
   LEFT JOIN vwResultNotesAsArray notes ON 
       (res.VMeasurementResultID=notes.VMeasurementResultID AND res.RelYear=notes.relYear);

GRANT ALL ON vwResultNotesAsArray TO "Webgroup";
GRANT ALL ON vwNotedReadingResult TO "Webgroup";
GRANT ALL ON vwJSONNotedReadingResult TO "Webgroup";

-- Converts vwResultNotesArray to JSON array
CREATE OR REPLACE FUNCTION cpgdb.resultNotesToJSON(integer[], integer[]) RETURNS text AS $$
DECLARE
   ids ALIAS FOR $1;
   counts ALIAS FOR $2;

   o text;
   idx integer;
   id integer;
   count integer;
   total integer := 0;

   note text;
   stdid integer;
   vocname text;

BEGIN
   IF ids IS NULL OR counts IS NULL THEN 
      RETURN NULL;
   END IF;

   o := '[';

   FOR idx in array_lower(ids, 1)..array_upper(ids, 1) LOOP
      id := ids[idx];
      count := counts[idx];

      SELECT rnote.note,rnote.standardisedid,voc.name INTO note,stdid,vocname
          FROM tlkpReadingNote rnote
          INNER JOIN tlkpVocabulary voc ON rnote.vocabularyid=voc.vocabularyid
          WHERE rnote.readingnoteid=id;

      IF NOT FOUND THEN
         RAISE NOTICE 'Reading note id % not found', id;
         CONTINUE;
      END IF;

      IF total > 0 THEN
         o := o || ',';
      END IF;

      o := o || '{' || '"note":"' || note || '"';
      IF stdid IS NOT NULL THEN
         o := o || ',"stdid":' || stdid;
      END IF;

      o := o || ',"std":"' || vocname || '"';

      o := o || ',"icnt":' || count;

      o := o || '}';

      total := total + 1;
   END LOOP;

   o := o || ']';
   return o;
END;
$$ LANGUAGE PLPGSQL IMMUTABLE;
