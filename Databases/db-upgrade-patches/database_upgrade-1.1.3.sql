CREATE VIEW vwtblloan AS
SELECT l.loanid, l.firstname, l.lastname, l.organisation, l.duedate, l.issuedate, l.returndate, age(l.duedate, l.issuedate) as loanperiod, array_to_string(l.files, '><'::text) AS files, l.notes
FROM tblloan l;

ALTER TABLE tblloan ALTER COLUMN issuedate SET DEFAULT now();
ALTER TABLE tblloan ALTER COLUMN issuedate SET NOT NULL;
ALTER TABLE tblloan ADD COLUMN returndate timestamp with time zone;

CREATE VIEW vwtblcurationmostrecent as 
select c.curationid, c.curationstatusid, c.sampleid, c.createdtimestamp, c.curatorid, c.loanid, c.notes 
from tblcuration c where not exists (select * from tblcuration c2 where c2.createdtimestamp > c.createdtimestamp 
and c.sampleid=c2.sampleid);


DROP VIEW vwtblsample;

CREATE OR REPLACE VIEW vwtblsample AS 
 SELECT s.externalid, s.sampleid, s.code, s.comments, s.code AS title, s.elementid, s.samplingdate, s.createdtimestamp, s.lastmodifiedtimestamp, st.sampletypeid, st.sampletype, s.file, s."position", s.state, s.knots, s.description, dc.datecertainty, s.boxid, lc.objectcode, lc.elementcode, c.curationstatusid, cl.curationstatus
FROM tblsample s
   LEFT JOIN tlkpdatecertainty dc ON s.datecertaintyid = dc.datecertaintyid
   LEFT JOIN tlkpsampletype st ON s.typeid = st.sampletypeid
   LEFT JOIN vwtblcurationmostrecent c ON s.sampleid = c.sampleid
   LEFT JOIN tlkpcurationstatus cl ON c.curationstatusid = cl.curationstatusid
   LEFT JOIN vwlabcodesforsamples lc ON s.sampleid = lc.sampleid;

 


CREATE VIEW vwtblcuration AS
   SELECT c.curationid, c.curationstatusid, c.sampleid, c.createdtimestamp, c.curatorid, c.loanid, c.notes, cl.curationstatus
FROM (tblcuration c
LEFT JOIN tlkpcurationstatus cl ON ((c.curationstatusid = cl.curationstatusid)));

-- Function: enforce_no_loan_when_on_loan()

-- DROP FUNCTION enforce_no_loan_when_on_loan();

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
FOR prevcurationstatusid IN SELECT curationstatusid FROM tblcuration WHERE sampleid = NEW.sampleid ORDER BY createdtimestamp desc LIMIT 1 LOOP

	IF prevcurationstatusid = 2 OR prevcurationstatusid = 3 THEN
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


  -- Function: enforce_no_status_update_on_curation()

-- DROP FUNCTION enforce_no_status_update_on_curation();

CREATE OR REPLACE FUNCTION enforce_no_status_update_on_curation()
  RETURNS trigger AS
$BODY$DECLARE

BEGIN

IF NEW.curationstatusid <> OLD.curationstatusid THEN
	RAISE EXCEPTION 'Changing the status of a curation record is not allowed.  A new curation record should be created instead';
	RETURN NULL;
END IF;

RETURN NEW;

END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

CREATE OR REPLACE FUNCTION check_tblcuration_loanid_is_not_null_when_loaned()
  RETURNS trigger AS
$BODY$DECLARE

BEGIN

IF NEW.curationstatusid=2 OR NEW.curationstatusid=3 THEN

  IF NEW.loanid IS NULL THEN
	RAISE EXCEPTION 'Loan information not specified for loan curation record';
	RETURN NULL;
  END IF;

ELSE

  IF NEW.loanid IS NOT NULL THEN
	RAISE EXCEPTION 'Loan information can only be provided when loaning a sample';
	RETURN NULL;
  END IF;
  
END IF;



CREATE TRIGGER trig_no_status_update
  BEFORE UPDATE
  ON tblcuration
  FOR EACH ROW
  EXECUTE PROCEDURE enforce_no_status_update_on_curation();

CREATE TRIGGER trig_nodoubleloan
  BEFORE INSERT
  ON tblcuration
  FOR EACH ROW
  EXECUTE PROCEDURE enforce_no_loan_when_on_loan();
  
CREATE TRIGGER trig_loanid_when_loaned
  BEFORE INSERT OR UPDATE
  ON tblcuration
  FOR EACH ROW
  EXECUTE PROCEDURE check_tblcuration_loanid_is_not_null_when_loaned();
  