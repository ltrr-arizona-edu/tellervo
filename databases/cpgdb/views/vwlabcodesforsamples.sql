DROP VIEW vwlabcodesforsamples;

CREATE OR REPLACE VIEW vwlabcodesforsamples AS 
 SELECT o.objectid, o.code AS objectcode, e.elementid, e.code AS elementcode, s.sampleid, s.code AS samplecode
   FROM tblsample s
   LEFT JOIN tblelement e ON e.elementid = s.elementid
   LEFT JOIN tblobject o ON o.objectid = e.objectid;

ALTER TABLE vwlabcodesforsamples OWNER TO aps03pwb;

ALTER TABLE vwlabcodesforsamples OWNER TO aps03pwb;
GRANT ALL ON TABLE vwlabcodesforsamples TO aps03pwb;
GRANT ALL ON TABLE vwlabcodesforsamples TO "Webgroup";
