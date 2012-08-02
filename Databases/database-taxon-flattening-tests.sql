
CREATE OR REPLACE FUNCTION cpgdb.gettaxonparent(colid character varying, ranktoreturn integer)
  RETURNS vwtlkptaxon AS
$BODY$DECLARE

currentTaxon vwtlkptaxon;
rankrow tlkptaxonrank;
taxon vwtlkptaxon;
BEGIN

  SELECT * INTO rankrow FROM tlkptaxonrank WHERE tlkptaxonrank.rankorder=$2;

  IF NOT FOUND THEN
      RAISE EXCEPTION 'Invalid taxon rank specfied';
  END IF;

  SELECT * INTO currentTaxon FROM vwtlkptaxon WHERE vwtlkptaxon.colid=$1;

  IF NOT FOUND THEN
      RAISE EXCEPTION 'Taxon id % not found.', $1;
  END IF;

  IF CAST(currentTaxon.rankorder as integer)<$2 THEN
	RAISE WARNING 'The specified taxon is already higher in the hierarchy than the requested return rank.';
	RETURN NULL;
  ELSIF CAST(currentTaxon.rankorder as integer)=$2 THEN
        RETURN currentTaxon;
  ELSE
	SELECT * INTO taxon FROM cpgdb.gettaxonparent(currentTaxon.colparentid, $2);
	RETURN taxon; 
  END IF;

END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
  CREATE OR REPLACE FUNCTION cpgdb.gettaxonparent(colid character varying, returnrank character varying)
  RETURNS vwtlkptaxon AS
$BODY$DECLARE
  
rankrow tlkptaxonrank;
taxonrow vwtlkptaxon;

BEGIN

  SELECT * INTO rankrow FROM tlkptaxonrank WHERE tlkptaxonrank.taxonrank=$2;

  IF NOT FOUND THEN
      RAISE EXCEPTION 'Invalid taxon rank specfied';
  END IF;

  SELECT * INTO taxonrow FROM cpgdb.gettaxonparent($1, cast(rankrow.rankorder as integer));

  RETURN taxonrow;

END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

  
  
CREATE OR REPLACE VIEW vwtlkptaxon AS
SELECT taxon.taxonid, taxon.label AS taxonlabel, 
taxon.parenttaxonid, taxon.colid, taxon.colparentid, 
rank.taxonrank, rank.rankorder, FROM (tlkptaxon taxon 
JOIN tlkptaxonrank rank ON ((rank.taxonrankid = taxon.taxonrankid))
JOIN 
);

create type flattaxon as (colid varchar, label varchar, kingdom varchar, family varchar);


CREATE OR REPLACE FUNCTION cpgdb.gettaxonparents()
  RETURNS SETOF flattaxon AS
$BODY$DECLARE

returnrow flattaxon;
taxonrow tlkptaxon;
query varchar;

BEGIN

FOR taxonrow IN SELECT * FROM tlkptaxon LOOP

  query := 'SELECT '''|| taxonrow.colid || '''::varchar AS colid, ''' || 
                       taxonrow.label   || '''::varchar AS label, ' ||
                       '(SELECT taxonlabel FROM cpgdb.gettaxonparent('''||taxonrow.colid || ''', ''kingdom'')), ' ||
                       '(SELECT taxonlabel FROM cpgdb.gettaxonparent('''||taxonrow.colid || ''', ''family''))';

  RETURN QUERY EXECUTE query;
END LOOP;

END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION cpgdb.gettaxonparents()
  OWNER TO pwb48;