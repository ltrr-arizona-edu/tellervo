
ALTER TABLE tblobject ALTER COLUMN projectid SET DEFAULT '08f7f052-580f-11e5-9ab7-fb88913ca6e1';

DROP FUNCTION cpgdb.qrytaxonomy(integer);
DROP FUNCTION cpgdb._gettaxonfordepth(integer, typfulltaxonomy);
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
    species character varying(128),
    subspecies character varying(128),
    race character varying(128),
    variety character varying(128),
    subvariety character varying(128),
    form character varying(128),
    subform character varying(128));
ALTER TYPE typfulltaxonomy
  OWNER TO postgres;

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
species text, 
subspecies text, 
race text, 
variety text, 
subvariety text, 
form text, 
subform text);$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.qrytaxonomy(integer)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION cpgdb.qrytaxonomy(integer) TO postgres;
GRANT EXECUTE ON FUNCTION cpgdb.qrytaxonomy(integer) TO public;
GRANT EXECUTE ON FUNCTION cpgdb.qrytaxonomy(integer) TO "Webgroup";
COMMENT ON FUNCTION cpgdb.qrytaxonomy(integer) IS 'This is a cross tab query that builds on qrytaxonflat1 and 2 to flatten out the entire taxonomic tree for a given taxonid. ';


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
     WHEN 10 THEN $2.species
     WHEN 11 THEN $2.subspecies
     WHEN 12 THEN $2.race
     WHEN 13 THEN $2.variety
     WHEN 14 THEN $2.subvariety
     WHEN 15 THEN $2.form
     WHEN 16 THEN $2.subform
     ELSE 'invalid'
   END;
$BODY$
  LANGUAGE sql IMMUTABLE
  COST 100;
ALTER FUNCTION cpgdb._gettaxonfordepth(integer, typfulltaxonomy)
  OWNER TO postgres;




UPDATE tblsupportedclient SET minversion='1.2.0' WHERE client='Tellervo WSI';