DROP VIEW vwtblsample;

CREATE OR REPLACE VIEW vwtblsample AS 
 SELECT s.externalid, s.sampleid, s.code, s.comments, s.code AS title, s.elementid, 
 s.samplingdate, s.createdtimestamp, s.lastmodifiedtimestamp, st.sampletypeid, 
 st.sampletype, array_to_string(s.file, '><'::text) AS file, s."position", s.state, s.knots, s.description, dc.datecertainty, s.boxid, lc.objectcode, lc.elementcode, c.curationstatusid, cl.curationstatus, s.samplingmonth, s.samplingyear, s.samplestatusid, ss.samplestatus
   FROM tblsample s
   LEFT JOIN tlkpsamplestatus ss ON s.samplestatusid = ss.samplestatusid
   LEFT JOIN tlkpdatecertainty dc ON s.datecertaintyid = dc.datecertaintyid
   LEFT JOIN tlkpsampletype st ON s.typeid = st.sampletypeid
   LEFT JOIN vwtblcurationmostrecent c ON s.sampleid = c.sampleid
   LEFT JOIN tlkpcurationstatus cl ON c.curationstatusid = cl.curationstatusid
   LEFT JOIN vwlabcodesforsamples lc ON s.sampleid = lc.sampleid;
