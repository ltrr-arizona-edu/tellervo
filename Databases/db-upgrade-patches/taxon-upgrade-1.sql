ALTER sequence tlkprank_rankid_seq restart 100;
INSERT INTO tlkptaxonrank (taxonrank, rankorder) VALUES ('subgenus', 72); 
INSERT INTO tlkptaxonrank (taxonrank, rankorder) VALUES ('section', 74);
INSERT INTO tlkptaxonrank (taxonrank, rankorder) VALUES ('subsection', 76);

DROP FUNCTION cpgdb.qrytaxonflat2(integer);
DROP FUNCTION cpgdb.qrytaxonflat1(integer);
DROP FUNCTION cpgdb.qrytaxonomy(integer);
DROP FUNCTION cpgdb._gettaxonfordepth(integer, typfulltaxonomy);
DROP TYPE typtaxonflat2;
DROP TYPE typfulltaxonomy;

  CREATE TYPE typfulltaxonomy AS
   (taxonid integer,
    kingdom character varying(128),
    subkingdom character varying(128),
    phylum character varying(128),
    division character varying(128),
    class character varying(128),
    txorder character varying(128),
    family character varying(128),
    genus character varying(128),
    subgenus character varying(128),
    section character varying(128),
    subsection character varying(128),
    species character varying(128),
    subspecies character varying(128),
    race character varying(128),
    variety character varying(128),
    subvariety character varying(128),
    form character varying(128),
    subform character varying(128));

  CREATE TYPE typtaxonflat2 AS
   (taxonid integer,
    taxonrankid integer,
    taxonname character varying(128),
    taxonrank character varying(30),
    rankorder double precision);

 CREATE OR REPLACE FUNCTION cpgdb._gettaxonfordepth(integer, typfulltaxonomy)
  RETURNS text AS
$BODY$
   SELECT CASE $1
     WHEN 1 THEN $2.kingdom 
     WHEN 2 THEN $2.subkingdom
     WHEN 3 THEN $2.phylum
     WHEN 4 THEN $2.division
     WHEN 5 THEN $2.class
     WHEN 6 THEN $2.txorder
     WHEN 7 THEN $2.family
     WHEN 8 THEN $2.genus
     WHEN 9 THEN $2.subgenus
     WHEN 10 THEN $2.section
     WHEN 11 THEN $2.subsection
     WHEN 12 THEN $2.species
     WHEN 13 THEN $2.subspecies
     WHEN 14 THEN $2.race
     WHEN 15 THEN $2.variety
     WHEN 16 THEN $2.subvariety
     WHEN 17 THEN $2.form
     WHEN 18 THEN $2.subform
     ELSE 'invalid'
   END;
$BODY$
  LANGUAGE sql IMMUTABLE
  COST 100; 

CREATE OR REPLACE FUNCTION cpgdb.qrytaxonflat1(taxonid integer)
  RETURNS SETOF typtaxonrankname AS
$BODY$SELECT $1 as taxonid, tlkptaxon.taxonrankid, tlkptaxon.label AS col0
FROM (((((((((((((tlkptaxon 
LEFT JOIN tlkptaxon AS tlkptaxon_1 ON tlkptaxon.taxonid = tlkptaxon_1.parenttaxonid) 
LEFT JOIN tlkptaxon AS tlkptaxon_2 ON tlkptaxon_1.taxonid = tlkptaxon_2.parenttaxonid) 
LEFT JOIN tlkptaxon AS tlkptaxon_3 ON tlkptaxon_2.taxonid = tlkptaxon_3.parenttaxonid) 
LEFT JOIN tlkptaxon AS tlkptaxon_4 ON tlkptaxon_3.taxonid = tlkptaxon_4.parenttaxonid) 
LEFT JOIN tlkptaxon AS tlkptaxon_5 ON tlkptaxon_4.taxonid = tlkptaxon_5.parenttaxonid) 
LEFT JOIN tlkptaxon AS tlkptaxon_6 ON tlkptaxon_5.taxonid = tlkptaxon_6.parenttaxonid) 
LEFT JOIN tlkptaxon AS tlkptaxon_7 ON tlkptaxon_6.taxonid = tlkptaxon_7.parenttaxonid) 
LEFT JOIN tlkptaxon AS tlkptaxon_8 ON tlkptaxon_7.taxonid = tlkptaxon_8.parenttaxonid) 
LEFT JOIN tlkptaxon AS tlkptaxon_9 ON tlkptaxon_8.taxonid = tlkptaxon_9.parenttaxonid) 
LEFT JOIN tlkptaxon AS tlkptaxon_10 ON tlkptaxon_9.taxonid = tlkptaxon_10.parenttaxonid)
LEFT JOIN tlkptaxon AS tlkptaxon_11 ON tlkptaxon_10.taxonid = tlkptaxon_11.parenttaxonid) 
LEFT JOIN tlkptaxon AS tlkptaxon_12 ON tlkptaxon_11.taxonid = tlkptaxon_12.parenttaxonid) 
LEFT JOIN tlkptaxon AS tlkptaxon_13 ON tlkptaxon_12.taxonid = tlkptaxon_13.parenttaxonid)  
LEFT JOIN tlkptaxon AS tlkptaxon_14 ON tlkptaxon_13.taxonid = tlkptaxon_14.parenttaxonid
WHERE 
   (((tlkptaxon.taxonid)=$1)) 
OR (((tlkptaxon_1.taxonid)=$1)) 
OR (((tlkptaxon_2.taxonid)=$1)) 
OR (((tlkptaxon_3.taxonid)=$1)) 
OR (((tlkptaxon_4.taxonid)=$1)) 
OR (((tlkptaxon_5.taxonid)=$1)) 
OR (((tlkptaxon_6.taxonid)=$1)) 
OR (((tlkptaxon_7.taxonid)=$1)) 
OR (((tlkptaxon_8.taxonid)=$1)) 
OR (((tlkptaxon_9.taxonid)=$1)) 
OR (((tlkptaxon_10.taxonid)=$1)) 
OR (((tlkptaxon_11.taxonid)=$1))
OR (((tlkptaxon_12.taxonid)=$1))
OR (((tlkptaxon_13.taxonid)=$1))
OR (((tlkptaxon_14.taxonid)=$1))
$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;
  
  CREATE OR REPLACE FUNCTION cpgdb.qrytaxonflat2(taxonid integer)
  RETURNS SETOF typtaxonflat2 AS
$BODY$SELECT qrytaxonflat1.taxonid, 
qrytaxonflat1.taxonrankid, 
qrytaxonflat1.taxonname, 
tlkptaxonrank.taxonrank, 
tlkptaxonrank.rankorder
FROM cpgdb.qrytaxonflat1($1) qrytaxonflat1 
RIGHT JOIN tlkptaxonrank ON qrytaxonflat1.taxonrankid = tlkptaxonrank.taxonrankid 
ORDER BY tlkptaxonrank.rankorder ASC;$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;
  
  

  

CREATE OR REPLACE FUNCTION cpgdb.qrytaxonomy(taxonid integer)
  RETURNS typfulltaxonomy AS
$BODY$
SELECT * FROM crosstab(
'select qrytaxonflat2.taxonid, qrytaxonflat2.taxonrank, qrytaxonflat2.taxonname 
from cpgdb.qrytaxonflat2('||$1||') 
order by 1
', 
'select taxonrank
from tlkptaxonrank 
order by rankorder asc'
) 
as 
(taxonid int, 
kingdom text, 
subkingdom text, 
phylum text, 
division text, 
class text, 
txorder text, 
family text, 
genus text, 
subgenus text,
section text,
subsection text,
species text, 
subspecies text, 
race text, 
variety text, 
subvariety text, 
form text, 
subform text);$BODY$
  LANGUAGE sql VOLATILE
  COST 100;

  
  

  