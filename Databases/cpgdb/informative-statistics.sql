
create type typnamenumber as (
name character varying,
number bigint);

CREATE OR REPLACE FUNCTION cpgdb.infoseriesperuserbydate(startdate date, enddate date)
  RETURNS SETOF typnamenumber AS
$BODY$SELECT (su.lastname::text || ', '::text) || su.firstname::text AS name, count(m.*) 
   FROM tblvmeasurement vm
   LEFT JOIN tblsecurityuser su ON vm.owneruserid = su.securityuserid
   LEFT JOIN tblmeasurement m ON vm.measurementid = m.measurementid
  WHERE vm.vmeasurementopid = 5 and vm.createdtimestamp>=$1 and vm.createdtimestamp<=$2
  GROUP BY (su.lastname::text || ', '::text) || su.firstname::text
  ORDER BY count(m.*) DESC;
$BODY$
  LANGUAGE 'sql' VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION cpgdb.infoseriesperuserbydate(date, date) OWNER TO aps03pwb;
COMMENT ON FUNCTION cpgdb.infoseriesperuserbydate(date, date) IS 'Scoreboard of users showing number of series measured by each between two dates';

CREATE OR REPLACE FUNCTION cpgdb.inforingsperuserbydate(startdate date, enddate date)
  RETURNS SETOF typnamenumber AS
$BODY$SELECT (su.lastname::text || ', '::text) || su.firstname::text AS name, count(r.*) 
   FROM tblvmeasurement vm
   LEFT JOIN tblsecurityuser su ON vm.owneruserid = su.securityuserid
   LEFT JOIN tblmeasurement m ON vm.measurementid = m.measurementid
   LEFT JOIN tblreading r ON m.measurementid = r.measurementid
  WHERE vm.vmeasurementopid = 5 and vm.createdtimestamp>=$1 and vm.createdtimestamp<=$2
  GROUP BY (su.lastname::text || ', '::text) || su.firstname::text, su.securityuserid
  ORDER BY count(r.*) DESC;
$BODY$
  LANGUAGE 'sql' VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION cpgdb.inforingsperuserbydate(date, date) OWNER TO aps03pwb;
COMMENT ON FUNCTION cpgdb.inforingsperuserbydate(date, date) IS 'Scoreboard of users showing number of rings measured between two dates';

CREATE OR REPLACE FUNCTION cpgdb.infosumringwidthforuserbydate(startdate date, enddate date)
  RETURNS SETOF typnamenumber AS
$BODY$SELECT (su.lastname::text || ', '::text) || su.firstname::text AS name, sum(r.reading) AS totalringwidth
 FROM tblvmeasurement vm
 LEFT JOIN tblsecurityuser su ON vm.owneruserid = su.securityuserid
 LEFT JOIN tblmeasurement m ON vm.measurementid = m.measurementid
 LEFT JOIN tblreading r ON m.measurementid = r.measurementid
  WHERE vm.vmeasurementopid = 5 and vm.createdtimestamp>=$1 and vm.createdtimestamp<=$2
GROUP BY (su.lastname::text || ', '::text) || su.firstname::text, su.securityuserid
ORDER BY sum(r.reading) DESC;$BODY$
  LANGUAGE 'sql' VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION cpgdb.infosumringwidthforuserbydate(date, date) OWNER TO aps03pwb;
COMMENT ON FUNCTION cpgdb.infosumringwidthforuserbydate(date, date) IS 'Scoreboard of users showing total ring lengths measured between two dates';


CREATE OR REPLACE FUNCTION cpgdb.infomeasuredsamplecountforsite(sitecode character varying)
  RETURNS bigint AS
'select count(distinct(s.sampleid)) from tblmeasurement m, tblradius r, tblsample s where m.radiusid=r.radiusid and r.sampleid=s.sampleid and s.sampleid in  (select s.sampleid from tblelement e, tblsample s where e.elementid=s.elementid and e.objectid in (select objectid from cpgdb.FindObjectDescendants((select objectid from tblobject where code=$1), true)));'
  LANGUAGE 'sql' VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.infomeasuredsamplecountforsite(character varying) OWNER TO aps03pwb;
COMMENT ON FUNCTION cpgdb.infomeasuredsamplecountforsite(character varying) IS 'Calculates the total number of samples from this site that have already been measured.  Handles traversal of object hierarchy.';

CREATE OR REPLACE FUNCTION cpgdb.inforingcountforsite(sitecode character varying)
  RETURNS bigint AS
'select count(rd.readingid) from tblreading rd, tblmeasurement m where rd.measurementid=m.measurementid and m.radiusid in ( select r.radiusid from tblradius r where r.sampleid in  (select s.sampleid from tblelement e, tblsample s where e.elementid=s.elementid and e.objectid in (select objectid from cpgdb.FindObjectDescendants((select objectid from tblobject where code=$1), true))));'
  LANGUAGE 'sql' VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.inforingcountforsite(character varying) OWNER TO aps03pwb;
COMMENT ON FUNCTION cpgdb.inforingcountforsite(character varying) IS 'Calculate the number of rings that have been measured for a site';

CREATE OR REPLACE FUNCTION cpgdb.infosamplecountforsite(sitecode character varying)
  RETURNS bigint AS
'select count(s.*) from tblelement e, tblsample s where e.elementid=s.elementid and e.objectid in (select objectid from cpgdb.FindObjectDescendants((select objectid from tblobject where code=$1), true));'
  LANGUAGE 'sql' VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.infosamplecountforsite(character varying) OWNER TO aps03pwb;
COMMENT ON FUNCTION cpgdb.infosamplecountforsite(character varying) IS 'Count of samples in the database for a site.  Note that some of these samples may not have associated series etc';

CREATE OR REPLACE FUNCTION cpgdb.infoseriescountforsite(sitecode character varying)
  RETURNS bigint AS
'select count(m.measurementid) from tblmeasurement m where m.radiusid in ( select r.radiusid from tblradius r where r.sampleid in  (select s.sampleid from tblelement e, tblsample s where e.elementid=s.elementid and e.objectid in (select objectid from cpgdb.FindObjectDescendants((select objectid from tblobject where code=$1), true))));'
  LANGUAGE 'sql' VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.infoseriescountforsite(character varying) OWNER TO aps03pwb;
COMMENT ON FUNCTION cpgdb.infoseriescountforsite(character varying) IS 'Calculated the the number of series that have been measured for a site.';

CREATE OR REPLACE FUNCTION cpgdb.infosumringlengthforsite(sitecode character varying)
  RETURNS bigint AS
'select sum(rd.reading) from tblreading rd, tblmeasurement m where rd.measurementid=m.measurementid and m.radiusid in ( select r.radiusid from tblradius r where r.sampleid in  (select s.sampleid from tblelement e, tblsample s where e.elementid=s.elementid and e.objectid in (select objectid from cpgdb.FindObjectDescendants((select objectid from tblobject where code=$1), true))));'
  LANGUAGE 'sql' VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.infosumringlengthforsite(character varying) OWNER TO aps03pwb;
COMMENT ON FUNCTION cpgdb.infosumringlengthforsite(character varying) IS 'Calculate the total length of rings that have been measured for a site.  Result is returned in microns.';


