CREATE TYPE date_prec AS ENUM ('day', 'month', 'year');

CREATE OR REPLACE FUNCTION cpgdb.getdatefromstr(datestr character varying)
  RETURNS date AS
$BODY$DECLARE

 datestr ALIAS FOR $1;

BEGIN

IF regexp_matches(datestr, '((18|19|20)\d\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01]))$') IS NOT NULL THEN
   --RAISE NOTICE 'YYYY-MM-DD';
   RETURN to_date(datestr, 'YYYY-MM-DD');     

ELSIF regexp_matches(datestr, '((18|19|20)\d\d[- /.](0[1-9]|1[012]))$') IS NOT NULL THEN
   --RAISE NOTICE 'YYYY-MM';
   RETURN to_date(datestr, 'YYYY-MM');

ELSIF regexp_matches(datestr, '((18|19|20)\d\d)$') IS NOT NULL THEN
   --RAISE NOTICE 'YYYY';
   RETURN to_date(datestr, 'YYYY');

ELSE
   RAISE EXCEPTION 'String ''%'' is not a valid date representation.  Must be YYYY, YYYY-MM, or YYYY-MM-DD', datestr;

END IF;

RETURN NULL;


END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.getdatefromstr(character varying)
  OWNER TO tellervo;
  
CREATE OR REPLACE FUNCTION cpgdb.getdateprecfromstr(datestr character varying)
  RETURNS date_prec AS
$BODY$DECLARE

 datestr ALIAS FOR $1;

BEGIN

IF regexp_matches(datestr, '((18|19|20)\d\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01]))$') IS NOT NULL THEN
   --RAISE NOTICE 'YYYY-MM-DD';
   RETURN 'day';   

ELSIF regexp_matches(datestr, '((18|19|20)\d\d[- /.](0[1-9]|1[012]))$') IS NOT NULL THEN
   --RAISE NOTICE 'YYYY-MM';
   RETURN 'month';

ELSIF regexp_matches(datestr, '((18|19|20)\d\d)$') IS NOT NULL THEN
   --RAISE NOTICE 'YYYY';
   RETURN 'year';

ELSE
   RAISE EXCEPTION 'String ''%'' is not a valid date representation.  Must be YYYY, YYYY-MM, or YYYY-MM-DD', datestr;

END IF;

RETURN NULL;


END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.getdateprecfromstr(character varying)
  OWNER TO tellervo;

CREATE OR REPLACE FUNCTION cpgdb.getdatestring(thedate date, thedateprec date_prec)
  RETURNS character varying AS
$BODY$DECLARE

  thedate ALIAS FOR $1;
  thedateprec ALIAS FOR $2;

BEGIN

  IF thedate IS NULL THEN
      RETURN NULL;
  END IF;

  IF thedateprec = 'year' THEN
      RETURN to_char(thedate, 'YYYY');
  ELSIF thedateprec = 'month' THEN
      RETURN to_char(thedate, 'YYYY-MM');
  ELSIF thedateprec = 'day' THEN
      RETURN to_char(thedate, 'YYYY-MM-DD');
  END IF;

  RAISE EXCEPTION 'Invalid date precision';

END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.getdatestring(date, date_prec)
  OWNER TO tellervo;
  


  

ALTER TABLE tblsample ADD samplingdateprec date_prec NOT NULL DEFAULT 'day';
ALTER TABLE tblsample ADD userdefinedfielddata varchar[];

DROP VIEW vwtblsample;

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
    cl.curationstatus,
    s.samplingmonth,
    s.samplingyear,
    s.samplestatusid,
    ss.samplestatus
   FROM tblsample s
     LEFT JOIN tlkpsamplestatus ss ON s.samplestatusid = ss.samplestatusid
     LEFT JOIN tlkpdatecertainty dc ON s.datecertaintyid = dc.datecertaintyid
     LEFT JOIN tlkpsampletype st ON s.typeid = st.sampletypeid
     LEFT JOIN vwtblcurationmostrecent c ON s.sampleid = c.sampleid
     LEFT JOIN tlkpcurationstatus cl ON c.curationstatusid = cl.curationstatusid
     LEFT JOIN vwlabcodesforsamples lc ON s.sampleid = lc.sampleid;
     
  
