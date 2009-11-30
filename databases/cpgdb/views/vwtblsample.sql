DROP VIEW vwtblsample;

CREATE OR REPLACE VIEW vwtblsample AS 
 SELECT s.sampleid, s.code, s.comments, s.code AS title, s.elementid, s.samplingdate, s.createdtimestamp, s.lastmodifiedtimestamp, st.sampletypeid, st.sampletype, s.file, s."position", s.state, s.knots, s.description, dc.datecertainty, s.boxid
   FROM tblsample s
   LEFT JOIN tlkpdatecertainty dc ON s.datecertaintyid = dc.datecertaintyid
   LEFT JOIN tlkpsampletype st ON s.typeid = st.sampletypeid;

ALTER TABLE vwtblsample OWNER TO aps03pwb;
GRANT ALL ON TABLE vwtblsample TO aps03pwb;
GRANT ALL ON TABLE vwtblsample TO "Webgroup";

