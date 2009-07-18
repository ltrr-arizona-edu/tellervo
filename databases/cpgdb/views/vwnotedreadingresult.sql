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

   idx integer;
   id integer;
   count integer;

   outnote text[];
   v1 text;
   v2 text;
   v3 text;
   v4 text;

   note text;
   stdid integer;
   vocname text;

BEGIN
   IF ids IS NULL OR counts IS NULL THEN 
      RETURN NULL;
   END IF;

   FOR idx in array_lower(ids, 1)..array_upper(ids, 1) LOOP
      id := ids[idx];
      count := counts[idx];

      SELECT replace(rnote.note, '"', E'\\"'),
             rnote.standardisedid,
             replace(voc.name, '"', E'\\"') 
          INTO note,stdid,vocname
          FROM tlkpReadingNote rnote
          INNER JOIN tlkpVocabulary voc ON rnote.vocabularyid=voc.vocabularyid
          WHERE rnote.readingnoteid=id;

      IF NOT FOUND THEN
         RAISE NOTICE 'Reading note id % not found', id;
         CONTINUE;
      END IF;

      v1 := '"note":"' || note || '"';

      IF stdid IS NOT NULL THEN
         v2 := '"stdid":' || stdid;
      ELSE
         v2 := '"stdid":null';
      END IF;

      v3 := '"std":"' || vocname || '"';

      v4 := '"icnt":' || count;

      outnote := array_append(outnote, '{' || array_to_string(ARRAY[v1, v2, v3, v4], ',') || '}');
   END LOOP;

   return '[' || array_to_string(outnote, ',') || ']';
END;
$$ LANGUAGE PLPGSQL STABLE;
