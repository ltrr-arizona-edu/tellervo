CREATE OR REPLACE FUNCTION cpgdb.qrytaxonflat1(taxonid integer)
  RETURNS SETOF typtaxonrankname AS
$BODY$SELECT $1 as taxonid, tlkptaxon.taxonrankid, tlkptaxon.label AS col0
FROM ((((((((((tlkptaxon 
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
LEFT JOIN tlkptaxon AS tlkptaxon_11 ON tlkptaxon_10.taxonid = tlkptaxon_11.parenttaxonid
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
$BODY$
  LANGUAGE 'sql' VOLATILE;
ALTER FUNCTION cpgdb.qrytaxonflat1(taxonid integer) OWNER TO aps03pwb;
GRANT EXECUTE ON FUNCTION cpgdb.qrytaxonflat1(taxonid integer) TO public;
GRANT EXECUTE ON FUNCTION cpgdb.qrytaxonflat1(taxonid integer) TO aps03pwb;
GRANT EXECUTE ON FUNCTION cpgdb.qrytaxonflat1(taxonid integer) TO "Webgroup";
GRANT EXECUTE ON FUNCTION cpgdb.qrytaxonflat1(taxonid integer) TO lucasm;



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
  LANGUAGE 'sql' VOLATILE;
ALTER FUNCTION cpgdb.qrytaxonflat2(taxonid integer) OWNER TO aps03pwb;
GRANT EXECUTE ON FUNCTION cpgdb.qrytaxonflat2(taxonid integer) TO public;
GRANT EXECUTE ON FUNCTION cpgdb.qrytaxonflat2(taxonid integer) TO aps03pwb;
GRANT EXECUTE ON FUNCTION cpgdb.qrytaxonflat2(taxonid integer) TO lucasm;
GRANT EXECUTE ON FUNCTION cpgdb.qrytaxonflat2(taxonid integer) TO "Webgroup";

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
    species text, 
    subspecies text, 
    race text, 
    variety text, 
    subvariety text, 
    form text, 
    subform text);$BODY$
  LANGUAGE 'sql' VOLATILE;
ALTER FUNCTION cpgdb.qrytaxonomy(taxonid integer) OWNER TO aps03pwb;
GRANT EXECUTE ON FUNCTION cpgdb.qrytaxonomy(taxonid integer) TO public;
GRANT EXECUTE ON FUNCTION cpgdb.qrytaxonomy(taxonid integer) TO aps03pwb;
GRANT EXECUTE ON FUNCTION cpgdb.qrytaxonomy(taxonid integer) TO "Webgroup";
GRANT EXECUTE ON FUNCTION cpgdb.qrytaxonomy(taxonid integer) TO lucasm;
COMMENT ON FUNCTION cpgdb.qrytaxonomy(taxonid integer) IS 'This is a cross tab query that builds on qrytaxonflat1 and 2 to flatten out the entire taxonomic element for a given taxonid. ';

