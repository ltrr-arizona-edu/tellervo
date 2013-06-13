CREATE VIEW vwtblloan AS
SELECT l.loanid, l.firstname, l.lastname, l.organisation, l.duedate, l.issuedate, l.returndate, array_to_string(l.files, '><'::text) AS files, l.notes
FROM tblloan l;

ALTER TABLE tblloan ALTER COLUMN issuedate SET DEFAULT now();
ALTER TABLE tblloan ALTER COLUMN issuedate SET NOT NULL;
ALTER TABLE tblloan ADD COLUMN returndate timestamp with time zone;


DROP VIEW vwtblsample;

CREATE VIEW vwtblsample AS
    SELECT s.externalid, s.sampleid, s.code, s.comments, s.code AS title, s.elementid, s.samplingdate, s.createdtimestamp, s.lastmodifiedtimestamp, st.sampletypeid, st.sampletype, s.file, s."position", s.state, s.knots, s.description, dc.datecertainty, s.boxid, lc.objectcode, lc.elementcode, c.curationstatusid, cl.curationstatus
FROM (((((tblsample s 
LEFT JOIN tlkpdatecertainty dc ON ((s.datecertaintyid = dc.datecertaintyid))) 
LEFT JOIN tlkpsampletype st ON ((s.typeid = st.sampletypeid))) 
LEFT JOIN tblcuration c ON ((s.sampleid = c.sampleid))) 
LEFT JOIN tlkpcurationstatus cl ON ((c.curationstatusid = cl.curationstatusid))) 
LEFT JOIN vwlabcodesforsamples lc ON ((s.sampleid = lc.sampleid)));


CREATE VIEW vwtblcuration AS
   SELECT c.curationid, c.curationstatusid, c.sampleid, c.createdtimestamp, c.curatorid, c.loanid, c.notes, cl.curationstatus
FROM (tblcuration c
LEFT JOIN tlkpcurationstatus cl ON ((c.curationstatusid = cl.curationstatusid)));