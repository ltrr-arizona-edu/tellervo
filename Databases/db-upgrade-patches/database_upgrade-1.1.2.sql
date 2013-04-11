

CREATE EXTENSION pgcrypto;
ALTER TABLE tblsecurityuser ALTER COLUMN password TYPE varchar(132);



INSERT INTO tblconfig (key, value, description) VALUES ('hashAlgorithm', 'md5', 'Algorithm to use for hashing of passwords');

UPDATE tblsupportedclient set minversion='1.0.2' where client='Tellervo WSI';



--
--  Add support for FHX2 ring remarks
--
CREATE OR REPLACE FUNCTION create_defaultreadingnotestandardisedid()
  RETURNS trigger AS
$BODY$DECLARE

newuuid uuid;


BEGIN


IF NEW.vocabularyid<2 THEN
  NEW.standardisedid=NEW.readingnoteid;
  RETURN NEW;
END IF;

IF NEW.standardisedid IS NULL THEN
	newuuid = uuid_generate_v1mc();
  NEW.standardisedid=newuuid;
END IF;


RETURN NEW;


END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;



ALTER TABLE tlkpreadingnote ALTER COLUMN standardisedid TYPE varchar(100);


INSERT INTO tlkpreadingnote (note, vocabularyid, standardisedid) VALUES ('micro ring', 2, '066e34a2-a210-11e2-b5c3-9bdc8beec740');
INSERT INTO tlkpreadingnote (note, vocabularyid, standardisedid) VALUES ('locally absent ring', 2, '24ae1d4c-a210-11e2-8670-335ff0d06e0c');

Insert into tlkpvocabulary (vocabularyid, name, url) values (4, 'FHX', 'http://www.frames.gov/partner-sites/fhaes/fhaes-home/');
update tlkpvocabulary set name='Tellervo', url='http://www.tellervo.org' where name='Corina';


Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire scar - position undetermined', 4, 'U');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire injury - position undetermined', 4, 'u');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire scar in latewood', 4, 'A');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire injury in latewood', 4, 'a');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire scar in dormant position', 4, 'D');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire injury in dormant position', 4, 'd');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire scar in first third of earlywood', 4, 'E');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire injury in first third of earlywood', 4, 'e');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire scar in middle third of earlywood', 4, 'M');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire injury in middle third of earlywood', 4, 'm');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire scar in last third of earlywood', 4, 'L');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire injury in last third of earlywood', 4, 'l');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Not recording fires', 4, '.');



-- Function: cpgdb.resultnotestojson(integer[], integer[])

-- DROP FUNCTION cpgdb.resultnotestojson(integer[], integer[]);

CREATE OR REPLACE FUNCTION cpgdb.resultnotestojson(integer[], integer[])
  RETURNS text AS
$BODY$
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
   v5 text; 

   note text;
   dbid integer;
   stdid varchar;
   vocname text;

BEGIN
   IF ids IS NULL OR counts IS NULL THEN 
      RETURN NULL;
   END IF;

   FOR idx in array_lower(ids, 1)..array_upper(ids, 1) LOOP
      id := ids[idx];
      count := counts[idx];

      SELECT replace(rnote.note, '"', E'\\"'),
	     rnote.readingnoteid,
             rnote.standardisedid,
             replace(voc.name, '"', E'\\"') 
          INTO note,dbid,stdid,vocname
          FROM tlkpReadingNote rnote
          INNER JOIN tlkpVocabulary voc ON rnote.vocabularyid=voc.vocabularyid
          WHERE rnote.readingnoteid=id;

      IF NOT FOUND THEN
         RAISE NOTICE 'Reading note id % not found', id;
         CONTINUE;
      END IF;

      v1 := '"note":"' || note || '"';



      IF stdid IS NOT NULL THEN
         v2 := '"stdid":"' || stdid || '"';
      ELSE
         v2 := '"stdid":null';
      END IF;

      v3 := '"std":"' || vocname || '"';

      v4 := '"icnt":' || count;

      v5 := '"dbid":' || dbid;

      outnote := array_append(outnote, '{' || array_to_string(ARRAY[v1, v2, v3, v4, v5], ',') || '}');
   END LOOP;

   return '[' || array_to_string(outnote, ',') || ']';
END;
$BODY$
  LANGUAGE plpgsql STABLE
  COST 100;




  