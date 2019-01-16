ALTER TABLE tblcuration RENAME TO tblcurationevent;
DROP VIEW vwtblcuration;

ALTER TABLE tblcurationevent RENAME COLUMN curationid TO curationeventid;

CREATE OR REPLACE VIEW public.vwtblcurationevent AS 
 SELECT c.curationeventid,
    c.curationstatusid,
    c.sampleid,
    c.boxid,
    c.createdtimestamp,
    c.curatorid,
    c.loanid,
    c.notes,
    cl.curationstatus as curationstatus
   FROM tblcurationevent c
     LEFT JOIN tlkpcurationstatus cl ON c.curationstatusid = cl.curationstatusid;

ALTER TABLE public.vwtblcurationevent
  OWNER TO tellervo;

DROP VIEW vwtblsample;
DROP VIEW vwtblcurationmostrecent;

CREATE OR REPLACE VIEW public.vwtblcurationeventmostrecent AS 
 SELECT c.curationeventid,
    c.curationstatusid,
    c.curationstatus,
    c.sampleid,
    c.boxid,
    c.createdtimestamp,
    c.curatorid,
    c.loanid,
    c.notes
   FROM vwtblcurationevent c
  WHERE NOT (EXISTS ( SELECT c2.curationeventid,
            c2.curationstatusid,
            c2.sampleid,
            c2.createdtimestamp,
            c2.curatorid,
            c2.loanid,
            c2.notes,
            c2.storagelocation
           FROM tblcurationevent c2
          WHERE c2.createdtimestamp > c.createdtimestamp AND c.sampleid = c2.sampleid));

ALTER TABLE public.vwtblcurationeventmostrecent
  OWNER TO tellervo;

CREATE OR REPLACE VIEW public.vwtblsample AS 
 SELECT s.externalid,
    s.sampleid,
    s.code,
    s.comments,
    s.code AS title,
    s.elementid,
    s.samplingdate,
    s.samplingdateprec,
    s.createdtimestamp,
    s.lastmodifiedtimestamp,
    st.sampletypeid,
    st.sampletype,
    array_to_string(s.file, '><'::text) AS file,
    s."position",
    s.state,
    s.knots,
    s.description,
    array_to_string(s.userdefinedfielddata, '><'::text) AS userdefinedfielddata,
    dc.datecertainty,
    s.boxid,
    lc.objectcode,
    lc.elementcode,
    c.curationstatusid,
    c.curationstatus,
    s.samplingmonth,
    s.samplingyear,
    s.samplestatusid,
    ss.samplestatus
   FROM tblsample s
     LEFT JOIN tlkpsamplestatus ss ON s.samplestatusid = ss.samplestatusid
     LEFT JOIN tlkpdatecertainty dc ON s.datecertaintyid = dc.datecertaintyid
     LEFT JOIN tlkpsampletype st ON s.typeid = st.sampletypeid
     LEFT JOIN vwtblcurationeventmostrecent c ON s.sampleid = c.sampleid
     LEFT JOIN vwlabcodesforsamples lc ON s.sampleid = lc.sampleid;

ALTER TABLE public.vwtblsample
  OWNER TO pbrewer;


  CREATE OR REPLACE VIEW public.vwtblbox AS 
 SELECT b.boxid,
    b.title,
    b.curationlocation,
    c.curationstatus,
    b.trackinglocation,
    b.createdtimestamp,
    b.lastmodifiedtimestamp,
    b.comments,
    count(s.sampleid) AS samplecount
   FROM tblbox b
     LEFT JOIN tblsample s ON b.boxid = s.boxid
     LEFT JOIN vwtblcurationeventmostrecent c ON b.boxid = c.boxid
  GROUP BY b.boxid, b.title, b.curationlocation, c.curationstatus, b.trackinglocation, b.createdtimestamp, b.lastmodifiedtimestamp, b.comments;

ALTER TABLE public.vwtblbox
  OWNER TO postgres;
GRANT ALL ON TABLE public.vwtblbox TO postgres;
GRANT ALL ON TABLE public.vwtblbox TO "Webgroup";

  
  