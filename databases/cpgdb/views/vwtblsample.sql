DROP VIEW vwtblsample;

CREATE VIEW vwtblsample AS
 SELECT s.sampleid, s.code, s.comments, s.code as title, s.elementid, s.samplingdate, s.createdtimestamp, 
        s.lastmodifiedtimestamp, st.sampletypeid, st.sampletype, s.file, 
        s."position", s.state, s.knots, s.description, dc.datecertainty
   FROM tblsample s
   LEFT JOIN tlkpdatecertainty dc ON s.datecertaintyid = dc.datecertaintyid
   LEFT JOIN tlkpsampletype st ON s.typeid = st.sampletypeid;

GRANT ALL on vwtblsample to "Webgroup";

