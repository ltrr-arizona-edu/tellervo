ALTER TABLE tblcuration RENAME TO tblcurationevent;
DROP VIEW vwtblcuration;

ALTER TABLE tblcurationevent RENAME COLUMN curationid TO curationeventid;

ALTER TABLE tblcurationevent ADD COLUMN boxid uuid;

ALTER TABLE tblcurationevent ADD CONSTRAINT "fkey_tblcuration-tblbox" FOREIGN KEY (boxid)
      REFERENCES public.tblbox (boxid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

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

DROP VIEW IF EXISTS vwtblsample;
DROP VIEW IF EXISTS  vwtblcurationmostrecent;

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

DROP VIEW vwtblbox;

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


CREATE OR REPLACE FUNCTION cpgdb.getloanfromboxid(boxid uuid)
  RETURNS tblloan AS
$BODY$
SELECT * from tblloan where loanid=(SELECT loanid FROM tblcurationevent WHERE boxid=$1 AND loanid IS NOT NULL ORDER BY createdtimestamp DESC LIMIT 1);
$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.getloanfromboxid(uuid)
  OWNER TO tellervo;
  
  
  
  
CREATE OR REPLACE FUNCTION cpgdb.loanhasoutstandingitems(theloanid uuid)
  RETURNS boolean AS
$BODY$DECLARE
  totalboxcount integer;
  returnedboxcount integer;
 

BEGIN

SELECT count(boxid) INTO totalboxcount FROM tblcurationevent WHERE loanid=$1 AND curationstatusid=2;
SELECT count(boxid) INTO returnedboxcount FROM tblcurationevent WHERE loanid=$1 AND curationstatusid=10;

RAISE NOTICE 'Total box count = %', totalboxcount;
RAISE NOTICE 'Returned box count = %', returnedboxcount;

IF totalboxcount <= returnedboxcount THEN
	RETURN FALSE;
ELSE 
	RETURN TRUE;
END IF;

END;

$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.loanhasoutstandingitems(uuid)
  OWNER TO tellervo;
  
 INSERT INTO tlkpcurationstatus (curationstatusid, curationstatus) VALUES (10, 'Returned from loan');
  
 
CREATE OR REPLACE FUNCTION enforce_no_loan_when_on_loan()
  RETURNS trigger AS
$BODY$DECLARE

prevcurationstatusid integer; 

BEGIN

-- If we're not trying to loan the sample then all is fine
IF NEW.curationstatusid<>2 AND NEW.curationstatusid<>3 THEN
	RETURN NEW;
END IF;

-- Otherwise we need to check it's previous status is not incompatable with loaning
FOR prevcurationstatusid IN SELECT curationstatusid FROM tblcurationevent WHERE sampleid = NEW.sampleid ORDER BY createdtimestamp desc LIMIT 1 LOOP

	IF prevcurationstatusid = 2 THEN
		RAISE EXCEPTION 'Cannot loan a sample that is already on loan';
		RETURN NULL;
	END IF;

	IF prevcurationstatusid = 6 THEN
		RAISE EXCEPTION 'Cannot loan a sample that has been destroyed';
		RETURN NULL;
	END IF;

	IF prevcurationstatusid = 7 THEN
		RAISE EXCEPTION 'Cannot loan a sample that has been returned to its owner';
		RETURN NULL;
	END IF;
	
END LOOP;

RETURN NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
alter table tblcurationevent alter column sampleid drop not null;

CREATE OR REPLACE FUNCTION public.check_tblcuration_loanid_is_not_null_when_loaned()
  RETURNS trigger AS
$BODY$DECLARE
BEGIN
IF NEW.curationstatusid=2 OR NEW.curationstatusid=10 THEN
  IF NEW.loanid IS NULL THEN
	RAISE EXCEPTION 'Loan information not specified for loan curation record';
	RETURN NULL;
  END IF;
ELSE
  IF NEW.loanid IS NOT NULL THEN
	RAISE EXCEPTION 'Loan information can only be provided when loaning a sample/box';
	RETURN NULL;
  END IF;
END IF;
RETURN NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public.check_tblcuration_loanid_is_not_null_when_loaned()
  OWNER TO tellervo;

