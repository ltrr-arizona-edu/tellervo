
CREATE OR REPLACE VIEW vwtlkptaxon AS 
 SELECT taxon.taxonid, taxon.label AS taxonlabel, taxon.parenttaxonid, taxon.colid, taxon.colparentid, rank.taxonrank
   FROM tlkptaxon taxon
   JOIN tlkptaxonrank rank ON rank.taxonrankid = taxon.taxonrankid;

ALTER TABLE vwtlkptaxon OWNER TO aps03pwb;
GRANT ALL ON TABLE vwtlkptaxon TO aps03pwb;
GRANT ALL ON TABLE vwtlkptaxon TO "Webgroup";
