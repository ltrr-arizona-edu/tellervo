INSERT INTO tlkpreadingnote (note, vocabularyid, standardisedid) VALUES ('micro ring', 2, 168);
INSERT INTO tlkpreadingnote (note, vocabularyid, standardisedid) VALUES ('locally absent ring', 2, 168);
UPDATE tlkpvocabulary SET name='Tellervo', url='http://www.tellervo.org' WHERE name='Corina';

CREATE EXTENSION pgcrypto;
ALTER TABLE tblsecurityuser ALTER COLUMN password TYPE varchar(132);


CREATE OR REPLACE FUNCTION cpgdb.updatepwd()
  RETURNS void AS
$BODY$DECLARE
  mviews RECORD;
  query VARCHAR;
BEGIN
   FOR mviews IN SELECT * FROM tblsecurityuser LOOP
     IF left(mviews.password, 1) != '\' THEN
	     query := 'UPDATE tblsecurityuser set password=''\xc' || mviews.password || ''' WHERE securityuserid='|| mviews.securityuserid;
	     EXECUTE query;
     END IF;
   END LOOP;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.updatepwd()
  OWNER TO pwb48;

INSERT INTO tblconfig (key, value, description) VALUES ('hashAlgorithm', 'sha512', 'Algorithm to use for hashing of passwords');

UPDATE tblsupportedclient set value='1.0.3' where client='Tellervo WSI';


  