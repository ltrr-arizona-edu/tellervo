
-- Recreate views that reference tblsecurityuser but this time they'll pick up the uuid pkey rather than the serial
CREATE OR REPLACE VIEW vw_bigbrothertracking AS 
 SELECT tblsecurityuser.firstname, tblsecurityuser.lastname, tblsecurityuser.username, tbliptracking.ipaddr, tbliptracking."timestamp"
   FROM tbliptracking, tblsecurityuser
  WHERE tbliptracking.securityuserid = tblsecurityuser.securityuserid;

ALTER TABLE vw_bigbrothertracking
  OWNER TO postgres;

CREATE OR REPLACE VIEW vw_requestlogsummary AS 
 SELECT tblrequestlog.requestlogid, (tblsecurityuser.firstname::text || ' '::text) || tblsecurityuser.lastname::text AS name, tblrequestlog.ipaddr, tblrequestlog.createdtimestamp, tblrequestlog.wsversion, tblrequestlog.page, tblrequestlog.client, "substring"(tblrequestlog.request::text, 53, 300) AS requesttrimmed
   FROM tblrequestlog, tblsecurityuser
  WHERE tblrequestlog.securityuserid = tblsecurityuser.securityuserid
  ORDER BY tblrequestlog.requestlogid DESC
 LIMIT 50;

ALTER TABLE vw_requestlogsummary
  OWNER TO postgres;

CREATE OR REPLACE VIEW vwcountperpersonperobject AS 
 SELECT o.code AS objectcode, (seu.lastname::text || ', '::text) || seu.firstname::text AS name, count(vm.owneruserid) AS count
   FROM tblvmeasurement vm
   LEFT JOIN tblmeasurement m ON vm.measurementid = m.measurementid
   LEFT JOIN tblsecurityuser seu ON seu.securityuserid = vm.owneruserid
   LEFT JOIN tblradius r ON m.radiusid = r.radiusid
   LEFT JOIN tblsample s ON r.sampleid = s.sampleid
   LEFT JOIN tblelement e ON s.elementid = e.elementid
   LEFT JOIN tblobject o ON e.objectid = o.objectid
  WHERE vm.vmeasurementopid = 5
  GROUP BY vm.owneruserid, o.code, (seu.lastname::text || ', '::text) || seu.firstname::text
  ORDER BY count(vm.owneruserid) DESC;

ALTER TABLE vwcountperpersonperobject
  OWNER TO postgres;




CREATE OR REPLACE VIEW vwringsleaderboard AS 
 SELECT su.securityuserid, (su.lastname::text || ', '::text) || su.firstname::text AS name, count(r.*) AS rings
   FROM tblvmeasurement vm
   LEFT JOIN tblsecurityuser su ON vm.owneruserid = su.securityuserid
   LEFT JOIN tblmeasurement m ON vm.measurementid = m.measurementid
   LEFT JOIN tblreading r ON m.measurementid = r.measurementid
  WHERE vm.vmeasurementopid = 5
  GROUP BY (su.lastname::text || ', '::text) || su.firstname::text, su.securityuserid
  ORDER BY count(r.*) DESC;

ALTER TABLE vwringsleaderboard
  OWNER TO postgres;


CREATE OR REPLACE VIEW vwringwidthleaderboard AS 
 SELECT su.securityuserid, (su.lastname::text || ', '::text) || su.firstname::text AS name, sum(r.reading) AS totalringwidth
   FROM tblvmeasurement vm
   LEFT JOIN tblsecurityuser su ON vm.owneruserid = su.securityuserid
   LEFT JOIN tblmeasurement m ON vm.measurementid = m.measurementid
   LEFT JOIN tblreading r ON m.measurementid = r.measurementid
  WHERE vm.vmeasurementopid = 5
  GROUP BY (su.lastname::text || ', '::text) || su.firstname::text, su.securityuserid
  ORDER BY sum(r.reading) DESC;

ALTER TABLE vwringwidthleaderboard
  OWNER TO postgres;


CREATE OR REPLACE VIEW vwsampleleaderboard AS 
 SELECT su.securityuserid, (su.lastname::text || ', '::text) || su.firstname::text AS name, count(m.*) AS samples
   FROM tblvmeasurement vm
   LEFT JOIN tblsecurityuser su ON vm.owneruserid = su.securityuserid
   LEFT JOIN tblmeasurement m ON vm.measurementid = m.measurementid
  WHERE vm.vmeasurementopid = 5
  GROUP BY (su.lastname::text || ', '::text) || su.firstname::text, su.securityuserid
  ORDER BY count(m.*) DESC;

ALTER TABLE vwsampleleaderboard
  OWNER TO postgres;

CREATE OR REPLACE VIEW vwtblcuration AS 
 SELECT c.curationid, c.curationstatusid, c.sampleid, c.createdtimestamp, c.curatorid, c.loanid, c.notes, cl.curationstatus
   FROM tblcuration c
   LEFT JOIN tlkpcurationstatus cl ON c.curationstatusid = cl.curationstatusid;


CREATE OR REPLACE VIEW vwtblcurationmostrecent AS 
 SELECT c.curationid, c.curationstatusid, c.sampleid, c.createdtimestamp, c.curatorid, c.loanid, c.notes
   FROM tblcuration c
  WHERE NOT (EXISTS ( SELECT c2.curationid, c2.curationstatusid, c2.sampleid, c2.createdtimestamp, c2.curatorid, c2.loanid, c2.notes, c2.storagelocation
           FROM tblcuration c2
          WHERE c2.createdtimestamp > c.createdtimestamp AND c.sampleid = c2.sampleid));


CREATE OR REPLACE VIEW vwtblmeasurement AS 
 SELECT tblmeasurement.measurementid, tblmeasurement.radiusid, tblmeasurement.isreconciled AS measurementidreconciled, tblmeasurement.islegacycleaned, tblmeasurement.importtablename, tblmeasurement.measuredbyid, tblsecurityuser.username AS measuredbyusername, tlkpdatingtype.datingtype, tblmeasurement.datingerrorpositive, tblmeasurement.datingerrornegative
   FROM tblmeasurement, tblsecurityuser, tlkpdatingtype
  WHERE tblmeasurement.measuredbyid = tblsecurityuser.securityuserid AND tblmeasurement.datingtypeid = tlkpdatingtype.datingtypeid;

ALTER TABLE vwtblmeasurement
  OWNER TO postgres;


CREATE OR REPLACE VIEW vwtblvmeasurement AS 
 SELECT tblvmeasurement.vmeasurementid, tblvmeasurement.measurementid, tblvmeasurement.vmeasurementopid, tlkpvmeasurementop.name AS measurementoperator, tblvmeasurement.vmeasurementopparameter AS operatorparameter, tblvmeasurement.code AS measurementname, tblvmeasurement.comments AS measurementdescription, tblvmeasurement.ispublished AS measurementispublished, tblvmeasurement.owneruserid AS measurementowneruserid, tblvmeasurement.createdtimestamp AS measurementcreated, tblvmeasurement.lastmodifiedtimestamp AS measurementlastmodified
   FROM tblvmeasurement, tlkpvmeasurementop
  WHERE tblvmeasurement.vmeasurementopid = tlkpvmeasurementop.vmeasurementopid;

ALTER TABLE vwtblvmeasurement
  OWNER TO postgres;
GRANT ALL ON TABLE vwtblvmeasurement TO postgres;
GRANT ALL ON TABLE vwtblvmeasurement TO "Webgroup";

CREATE OR REPLACE VIEW vwcomprehensivevm AS 
 SELECT vm.vmeasurementid, vm.measurementid, vm.vmeasurementopid, vm.vmeasurementopparameter, vm.code, vm.comments, vm.ispublished, vm.owneruserid, vm.createdtimestamp, vm.lastmodifiedtimestamp, vm.isgenerating, vm.objective, vm.version, vm.birthdate, mc.startyear, mc.readingcount, mc.measurementcount, mc.objectcode AS objectcodesummary, mc.objectcount, mc.commontaxonname, mc.taxoncount, mc.prefix, m.radiusid, m.isreconciled, m.startyear AS measurementstartyear, m.islegacycleaned, m.importtablename, m.measuredbyid, pg_catalog.concat(anly.firstname, ' ', anly.lastname) AS measuredby, mc.datingtypeid, m.datingerrorpositive, m.datingerrornegative, m.measurementvariableid, m.unitid, m.power, m.provenance, m.measuringmethodid, m.supervisedbyid, pg_catalog.concat(spvr.firstname, ' ', spvr.lastname) AS supervisedby, su.username, op.name AS opname, dt.datingtype, mc.vmextent AS extentgeometry, cd.crossdateid, cd.mastervmeasurementid, cd.startyear AS newstartyear, cd.justification, cd.confidence, der.objectid, der.objecttitle, der.objectcode, der.elementcode, der.samplecode, der.radiuscode, dcc.directchildcount
   FROM tblvmeasurement vm
   JOIN tlkpvmeasurementop op ON vm.vmeasurementopid = op.vmeasurementopid
   LEFT JOIN vwderivedtitles der ON vm.vmeasurementid = der.vmeasurementid
   LEFT JOIN vwdirectchildcount dcc ON vm.vmeasurementid = dcc.vmeasurementid
   LEFT JOIN tblmeasurement m ON vm.measurementid = m.measurementid
   LEFT JOIN tblvmeasurementmetacache mc ON vm.vmeasurementid = mc.vmeasurementid
   LEFT JOIN tlkpdatingtype dt ON mc.datingtypeid = dt.datingtypeid
   LEFT JOIN tblsecurityuser su ON vm.owneruserid = su.securityuserid
   LEFT JOIN tblsecurityuser anly ON m.measuredbyid = anly.securityuserid
   LEFT JOIN tblsecurityuser spvr ON m.supervisedbyid = spvr.securityuserid
   LEFT JOIN tblcrossdate cd ON vm.vmeasurementid = cd.vmeasurementid;

CREATE OR REPLACE VIEW vwcomprehensivevm2 AS 
 SELECT vm.vmeasurementid, vm.measurementid, vm.vmeasurementopid, vm.vmeasurementopparameter, vm.code, vm.comments, vm.ispublished, vm.owneruserid, vm.createdtimestamp, vm.lastmodifiedtimestamp, vm.isgenerating, vm.objective, vm.version, vm.birthdate, mc.startyear, mc.readingcount, mc.measurementcount, mc.objectcode AS objectcodesummary, mc.objectcount, mc.commontaxonname, mc.taxoncount, mc.prefix, m.radiusid, m.isreconciled, m.startyear AS measurementstartyear, m.islegacycleaned, m.importtablename, m.measuredbyid, mc.datingtypeid, m.datingerrorpositive, m.datingerrornegative, m.measurementvariableid, m.unitid, m.power, m.provenance, m.measuringmethodid, m.supervisedbyid, su.username, op.name AS opname, dt.datingtype, mc.vmextent AS extentgeometry, cd.crossdateid, cd.mastervmeasurementid, cd.startyear AS newstartyear, cd.justification, cd.confidence, der.objectid, der.objecttitle, der.objectcode, der.elementcode, der.samplecode, der.radiuscode, dcc.directchildcount
   FROM tblvmeasurement vm
   JOIN tlkpvmeasurementop op ON vm.vmeasurementopid = op.vmeasurementopid
   LEFT JOIN vwderivedtitles der ON vm.vmeasurementid = der.vmeasurementid
   LEFT JOIN vwdirectchildcount dcc ON vm.vmeasurementid = dcc.vmeasurementid
   LEFT JOIN tblmeasurement m ON vm.measurementid = m.measurementid
   LEFT JOIN tblvmeasurementmetacache mc ON vm.vmeasurementid = mc.vmeasurementid
   LEFT JOIN tlkpdatingtype dt ON mc.datingtypeid = dt.datingtypeid
   LEFT JOIN tblsecurityuser su ON vm.owneruserid = su.securityuserid
   LEFT JOIN tblcrossdate cd ON vm.vmeasurementid = cd.vmeasurementid;



CREATE OR REPLACE VIEW vwtblsample AS 
 SELECT s.externalid, s.sampleid, s.code, s.comments, s.code AS title, s.elementid, s.samplingdate, s.createdtimestamp, s.lastmodifiedtimestamp, st.sampletypeid, st.sampletype, s.file, s."position", s.state, s.knots, s.description, dc.datecertainty, s.boxid, lc.objectcode, lc.elementcode, c.curationstatusid, cl.curationstatus
   FROM tblsample s
   LEFT JOIN tlkpdatecertainty dc ON s.datecertaintyid = dc.datecertaintyid
   LEFT JOIN tlkpsampletype st ON s.typeid = st.sampletypeid
   LEFT JOIN vwtblcurationmostrecent c ON s.sampleid = c.sampleid
   LEFT JOIN tlkpcurationstatus cl ON c.curationstatusid = cl.curationstatusid
   LEFT JOIN vwlabcodesforsamples lc ON s.sampleid = lc.sampleid;


CREATE OR REPLACE VIEW vwleaderboard AS 
 SELECT slb.securityuserid, slb.name, slb.samples, rlb.rings, rwlb.totalringwidth
   FROM vwsampleleaderboard slb, vwringsleaderboard rlb, vwringwidthleaderboard rwlb
  WHERE slb.securityuserid = rlb.securityuserid AND slb.securityuserid = rwlb.securityuserid;

ALTER TABLE vwleaderboard
  OWNER TO postgres;


-- Drop CPGDB functions that refer to old integer fields
DROP FUNCTION cpgdb.getgroupmembership(integer);
DROP FUNCTION cpgdb.getgroupmembershiparray(integer);
DROP FUNCTION cpgdb.getuserpermissions(integer, character varying, integer);
DROP FUNCTION cpgdb.getuserpermissionset(integer, character varying, integer);
DROP FUNCTION cpgdb.getuserpermissionset(integer, character varying, uuid);



-- Create new UUID versions of the CPGDB functions
CREATE OR REPLACE FUNCTION cpgdb.getgroupmembership(uuid)
  RETURNS SETOF tblsecuritygroup AS
$BODY$
DECLARE
   _securityUserID ALIAS FOR $1;

   res tblSecurityGroup%ROWTYPE;
   parentRes tblSecurityGroup%ROWTYPE;
BEGIN
   FOR res IN SELECT g.* FROM tblSecurityGroup g
              INNER JOIN tblSecurityUserMembership m ON m.securityGroupID = g.securityGroupID
              WHERE m.securityUserID = _securityUserID AND g.isActive = true
   LOOP
      RETURN NEXT res;

      FOR parentRes IN SELECT * FROM cpgdb.recurseGetParentGroups(res.securityGroupID, 0) 
      LOOP
         RETURN NEXT parentRes;
      END LOOP;
   END LOOP;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION cpgdb.getgroupmembership(uuid)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION cpgdb.getgroupmembership(uuid) TO postgres;
GRANT EXECUTE ON FUNCTION cpgdb.getgroupmembership(uuid) TO public;
GRANT EXECUTE ON FUNCTION cpgdb.getgroupmembership(uuid) TO "Webgroup";

CREATE OR REPLACE FUNCTION cpgdb.getgroupmembershiparray(uuid) RETURNS integer[] AS
$BODY$
   SELECT ARRAY(SELECT DISTINCT SecurityGroupID FROM cpgdb.GetGroupMembership($1) ORDER BY SecurityGroupID ASC);
$BODY$
  LANGUAGE sql IMMUTABLE
  COST 100;
ALTER FUNCTION cpgdb.getgroupmembershiparray(uuid)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION cpgdb.getgroupmembershiparray(uuid) TO postgres;
GRANT EXECUTE ON FUNCTION cpgdb.getgroupmembershiparray(uuid) TO public;
GRANT EXECUTE ON FUNCTION cpgdb.getgroupmembershiparray(uuid) TO "Webgroup";


CREATE OR REPLACE FUNCTION cpgdb.getuserpermissions(uuid, character varying, integer)
  RETURNS character varying[] AS
$BODY$
DECLARE
   _securityUserID ALIAS FOR $1;
   _ptype ALIAS FOR $2;
   _pid ALIAS FOR $3;

   groupMembership int[];
BEGIN
   SELECT * INTO groupMembership FROM cpgdb.GetGroupMembershipArray($1);

   IF 1 = ANY(groupMembership) THEN
      RETURN ARRAY(SELECT name FROM tlkpSecurityPermission WHERE name <> 'No permission');
   END IF;

   RETURN (SELECT * FROM cpgdb.GetGroupPermissions(groupMembership, _ptype, _pid));
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.getuserpermissions(uuid, character varying, integer)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION cpgdb.getuserpermissions(uuid, character varying, integer) TO postgres;
GRANT EXECUTE ON FUNCTION cpgdb.getuserpermissions(uuid, character varying, integer) TO public;
GRANT EXECUTE ON FUNCTION cpgdb.getuserpermissions(uuid, character varying, integer) TO "Webgroup";

CREATE OR REPLACE FUNCTION cpgdb.getuserpermissionset(uuid, character varying, integer)
  RETURNS typpermissionset AS
$BODY$
DECLARE
   _securityUserID ALIAS FOR $1;
   _ptype ALIAS FOR $2;
   _pid ALIAS FOR $3;

   groupMembership int[];
   perms typPermissionSet;
BEGIN
   SELECT * INTO groupMembership FROM cpgdb.GetGroupMembershipArray($1);

   IF 1 = ANY(groupMembership) THEN
      perms.denied = false;
      perms.canRead = true;
      perms.canUpdate = true;
      perms.canCreate = true;
      perms.canDelete = true;
      perms.decidedBy = 'Administrative user';
      RETURN perms;
   ELSIF array_upper(groupMembership, 1) IS NULL THEN
      perms.denied = true;
      perms.canRead = false;
      perms.canUpdate = false;
      perms.canCreate = false;
      perms.canDelete = false;
      perms.decidedBy = 'User is not a member of any groups';
      RETURN perms;      
   END IF;

   perms := cpgdb.GetGroupPermissionSet(groupMembership, _ptype, _pid);
   RETURN perms;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.getuserpermissionset(uuid, character varying, integer)
  OWNER TO postgres;

CREATE OR REPLACE FUNCTION cpgdb.getuserpermissionset(uuid, character varying, uuid)
  RETURNS typpermissionset AS
$BODY$
DECLARE
   _securityUserID ALIAS FOR $1;
   _ptype ALIAS FOR $2;
   _pid ALIAS FOR $3;

   groupMembership int[];
   perms typPermissionSet;
BEGIN

   SELECT * INTO groupMembership FROM cpgdb.GetGroupMembershipArray($1);

   IF 1 = ANY(groupMembership) THEN
      perms.denied = false;
      perms.canRead = true;
      perms.canUpdate = true;
      perms.canCreate = true;
      perms.canDelete = true;
      perms.decidedBy = 'Administrative user';
      RETURN perms;
   ELSIF array_upper(groupMembership, 1) IS NULL THEN
      perms.denied = true;
      perms.canRead = false;
      perms.canUpdate = false;
      perms.canCreate = false;
      perms.canDelete = false;
      perms.decidedBy = 'User is not a member of any groups';
      RETURN perms;      
   END IF;

   perms := cpgdb.GetGroupPermissionSet(groupMembership, _ptype, _pid);
   RETURN perms;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.getuserpermissionset(uuid, character varying, uuid)
  OWNER TO postgres;


  
  
  
--
--  HANDLING DOMAINS
--

CREATE TABLE tlkpdomain
(
  domainid serial NOT NULL,
  domain character varying NOT NULL,
  prefix character varying,
  CONSTRAINT pkey_tlkpdomain PRIMARY KEY (domainid),
  CONSTRAINT uniq_tlkpdomain UNIQUE (domain)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE tlkpdomain
  OWNER TO tellervo;


INSERT INTO tlkpdomain (domainid, domain) VALUES ('0', 'localhost');

ALTER TABLE tblobject ADD COLUMN domainid integer default 0;
ALTER TABLE tblobject ADD CONSTRAINT fkey_tblobject_tlkpdomain FOREIGN KEY (domainid) REFERENCES tlkpdomain (domainid) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE tblelement ADD COLUMN domainid integer default 0;
ALTER TABLE tblelement ADD CONSTRAINT fkey_tblelement_tlkpdomain FOREIGN KEY (domainid) REFERENCES tlkpdomain (domainid) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE tblsample ADD COLUMN domainid integer default 0;
ALTER TABLE tblsample ADD CONSTRAINT fkey_tblsample_tlkpdomain FOREIGN KEY (domainid) REFERENCES tlkpdomain (domainid) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE tblradius ADD COLUMN domainid integer default 0;
ALTER TABLE tblradius ADD CONSTRAINT fkey_tblradius_tlkpdomain FOREIGN KEY (domainid) REFERENCES tlkpdomain (domainid) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE tblvmeasurement ADD COLUMN domainid integer default 0;
ALTER TABLE tblvmeasurement ADD CONSTRAINT fkey_tblvmeasurement_tlkpdomain FOREIGN KEY (domainid) REFERENCES tlkpdomain (domainid) ON UPDATE NO ACTION ON DELETE NO ACTION;


DROP VIEW vwtblobject;
CREATE VIEW vwtblobject AS SELECT cquery.countofchildvmeasurements, o.vegetationtype, o.comments, o.objectid, dom.domainid, dom.domain, o.title, o.code, o.createdtimestamp, o.lastmodifiedtimestamp, o.locationgeometry, o.locationtypeid, o.locationprecision, o.locationcomment, o.locationaddressline1, o.locationaddressline2, o.locationcityortown, o.locationstateprovinceregion, o.locationpostalcode, o.locationcountry, array_to_string(o.file, '><'::text) AS file, o.creator, o.owner, o.parentobjectid, o.description, o.objecttypeid, loctype.locationtype, objtype.objecttype, covtemp.coveragetemporal, covtempfound.coveragetemporalfoundation
   FROM tblobject o
   LEFT JOIN tlkpdomain dom ON o.domainid = dom.domainid
   LEFT JOIN tlkplocationtype loctype ON o.locationtypeid = loctype.locationtypeid
   LEFT JOIN tlkpobjecttype objtype ON o.objecttypeid = objtype.objecttypeid
   LEFT JOIN tlkpcoveragetemporal covtemp ON o.coveragetemporalid = covtemp.coveragetemporalid
   LEFT JOIN tlkpcoveragetemporalfoundation covtempfound ON o.coveragetemporalfoundationid = covtempfound.coveragetemporalfoundationid
   LEFT JOIN ( SELECT e.objectid AS masterobjectid, count(e.objectid) AS countofchildvmeasurements
   FROM tblelement e
   JOIN tblsample s ON s.elementid = e.elementid
   JOIN tblradius r ON r.sampleid = s.sampleid
   JOIN tblmeasurement m ON m.radiusid = r.radiusid
   JOIN tblvmeasurementderivedcache vc ON vc.measurementid = m.measurementid
  GROUP BY e.objectid) cquery ON cquery.masterobjectid = o.objectid;
  
DROP VIEW vwtblelement;
CREATE OR REPLACE VIEW vwtblelement AS 
 SELECT ( SELECT findobjecttoplevelancestor.code
           FROM cpgdb.findobjecttoplevelancestor(e.objectid) findobjecttoplevelancestor(objectid, title, code, createdtimestamp, lastmodifiedtimestamp, locationgeometry, locationtypeid, locationprecision, locationcomment, creator, owner, parentobjectid, description, objecttypeid, coveragetemporalid, coveragetemporalfoundationid, comments, coveragetemporal, coveragetemporalfoundation, locationaddressline1, locationaddressline2, locationcityortown, locationstateprovinceregion, locationpostalcode, locationcountry, locationcountry)) AS objectcode, e.comments, dom.domainid, dom.domain, e.elementid, e.locationprecision, e.code AS title, e.code, e.createdtimestamp, e.lastmodifiedtimestamp, e.locationgeometry, e.islivetree, e.originaltaxonname, e.locationtypeid, e.locationcomment, e.locationaddressline1, e.locationaddressline2, e.locationcityortown, e.locationstateprovinceregion, e.locationpostalcode, e.locationcountry, array_to_string(e.file, '><'::text) AS file, e.description, e.processing, e.marks, e.diameter, e.width, e.height, e.depth, e.unsupportedxml, e.objectid, e.elementtypeid, e.authenticity, e.elementshapeid, shape.elementshape, tbltype.elementtype, loctype.locationtype, e.altitude, e.slopeangle, e.slopeazimuth, e.soildescription, e.soildepth, e.bedrockdescription, vwt.taxonid, vwt.taxonlabel, vwt.parenttaxonid, vwt.colid, vwt.colparentid, vwt.taxonrank, unit.unit AS units, unit.unitid
   FROM tblelement e
   LEFT JOIN tlkpdomain dom ON e.domainid = dom.domainid
   LEFT JOIN tlkpelementshape shape ON e.elementshapeid = shape.elementshapeid
   LEFT JOIN tlkpelementtype tbltype ON e.elementtypeid = tbltype.elementtypeid
   LEFT JOIN tlkplocationtype loctype ON e.locationtypeid = loctype.locationtypeid
   LEFT JOIN tlkpunit unit ON e.units = unit.unitid
   LEFT JOIN vwtlkptaxon vwt ON e.taxonid = vwt.taxonid;

DROP VIEW vwtblsample;
CREATE OR REPLACE VIEW vwtblsample AS 
 SELECT s.externalid, dom.domainid, dom.domain, s.sampleid, s.code, s.comments, s.code AS title, s.elementid, s.samplingdate, s.createdtimestamp, s.lastmodifiedtimestamp, st.sampletypeid, st.sampletype, s.file, s."position", s.state, s.knots, s.description, dc.datecertainty, s.boxid, lc.objectcode, lc.elementcode, c.curationstatusid, cl.curationstatus
   FROM tblsample s
   LEFT JOIN tlkpdomain dom ON s.domainid = dom.domainid
   LEFT JOIN tlkpdatecertainty dc ON s.datecertaintyid = dc.datecertaintyid
   LEFT JOIN tlkpsampletype st ON s.typeid = st.sampletypeid
   LEFT JOIN vwtblcurationmostrecent c ON s.sampleid = c.sampleid
   LEFT JOIN tlkpcurationstatus cl ON c.curationstatusid = cl.curationstatusid
   LEFT JOIN vwlabcodesforsamples lc ON s.sampleid = lc.sampleid;

DROP VIEW vwtblradius;
CREATE OR REPLACE VIEW vwtblradius AS 
 SELECT tblradius.comments, dom.domainid, dom.domain, tblradius.radiusid, tblradius.sampleid, tblradius.code AS radiuscode, tblradius.azimuth, tblradius.createdtimestamp AS radiuscreated, tblradius.lastmodifiedtimestamp AS radiuslastmodified, tblradius.numberofsapwoodrings, tblradius.barkpresent, tblradius.lastringunderbark, tblradius.lastringunderbarkpresent, tblradius.missingheartwoodringstopith, tblradius.missingheartwoodringstopithfoundation, tblradius.missingsapwoodringstobark, tblradius.missingsapwoodringstobarkfoundation, tblradius.nrofunmeasuredouterrings, tblradius.nrofunmeasuredinnerrings, spw.complexpresenceabsenceid AS sapwoodid, spw.complexpresenceabsence AS sapwood, hwd.complexpresenceabsenceid AS heartwoodid, hwd.complexpresenceabsence AS heartwood, pth.complexpresenceabsenceid AS pithid, pth.complexpresenceabsence AS pith
   FROM tblradius
   LEFT JOIN tlkpdomain dom ON tblradius.domainid = dom.domainid
   LEFT JOIN tlkpcomplexpresenceabsence spw ON tblradius.sapwoodid = spw.complexpresenceabsenceid
   LEFT JOIN tlkpcomplexpresenceabsence hwd ON tblradius.heartwoodid = hwd.complexpresenceabsenceid
   LEFT JOIN tlkpcomplexpresenceabsence pth ON tblradius.pithid = pth.complexpresenceabsenceid;

DROP VIEW vwtblvmeasurement;
CREATE OR REPLACE VIEW vwtblvmeasurement AS 
 SELECT dom.domainid, dom.domain, tblvmeasurement.vmeasurementid, tblvmeasurement.measurementid, tblvmeasurement.vmeasurementopid, tlkpvmeasurementop.name AS measurementoperator, tblvmeasurement.vmeasurementopparameter AS operatorparameter, tblvmeasurement.code AS measurementname, tblvmeasurement.comments AS measurementdescription, tblvmeasurement.ispublished AS measurementispublished, tblvmeasurement.owneruserid AS measurementowneruserid, tblvmeasurement.createdtimestamp AS measurementcreated, tblvmeasurement.lastmodifiedtimestamp AS measurementlastmodified
   FROM tblvmeasurement
   LEFT JOIN tlkpdomain dom ON tblvmeasurement.domainid = dom.domainid
   LEFT JOIN tlkpvmeasurementop ON tblvmeasurement.vmeasurementopid = tlkpvmeasurementop.vmeasurementopid;
  
DROP VIEW vwcomprehensivevm;
CREATE OR REPLACE VIEW vwcomprehensivevm AS 
 SELECT dom.domainid, dom.domain, vm.vmeasurementid, vm.measurementid, 
 vm.vmeasurementopid, vm.vmeasurementopparameter, vm.code, vm.comments, 
 vm.ispublished, vm.owneruserid, vm.createdtimestamp, vm.lastmodifiedtimestamp, 
 vm.isgenerating, vm.objective, vm.version, vm.birthdate, mc.startyear, 
 mc.readingcount, mc.measurementcount, mc.objectcode AS objectcodesummary, 
 mc.objectcount, mc.commontaxonname, mc.taxoncount, mc.prefix, m.radiusid, 
 m.isreconciled, m.startyear AS measurementstartyear, m.islegacycleaned, 
 m.importtablename, m.measuredbyid, pg_catalog.concat(anly.firstname, ' ', anly.lastname) AS measuredby, mc.datingtypeid, m.datingerrorpositive, m.datingerrornegative, m.measurementvariableid, m.unitid, m.power, m.provenance, m.measuringmethodid, m.supervisedbyid, pg_catalog.concat(spvr.firstname, ' ', spvr.lastname) AS supervisedby, su.username, op.name AS opname, dt.datingtype, mc.vmextent AS extentgeometry, cd.crossdateid, cd.mastervmeasurementid, cd.startyear AS newstartyear, cd.justification, cd.confidence, der.objectid, der.objecttitle, der.objectcode, der.elementcode, der.samplecode, der.radiuscode, dcc.directchildcount
   FROM tblvmeasurement vm
   JOIN tlkpvmeasurementop op ON vm.vmeasurementopid = op.vmeasurementopid
   LEFT JOIN tlkpdomain dom ON vm.domainid = dom.domainid
   LEFT JOIN vwderivedtitles der ON vm.vmeasurementid = der.vmeasurementid
   LEFT JOIN vwdirectchildcount dcc ON vm.vmeasurementid = dcc.vmeasurementid
   LEFT JOIN tblmeasurement m ON vm.measurementid = m.measurementid
   LEFT JOIN tblvmeasurementmetacache mc ON vm.vmeasurementid = mc.vmeasurementid
   LEFT JOIN tlkpdatingtype dt ON mc.datingtypeid = dt.datingtypeid
   LEFT JOIN tblsecurityuser su ON vm.owneruserid = su.securityuserid
   LEFT JOIN tblsecurityuser anly ON m.measuredbyid = anly.securityuserid
   LEFT JOIN tblsecurityuser spvr ON m.supervisedbyid = spvr.securityuserid
   LEFT JOIN tblcrossdate cd ON vm.vmeasurementid = cd.vmeasurementid;
   

CREATE OR REPLACE FUNCTION cpgdb.getgroupmembership(uuid)
  RETURNS SETOF tblsecuritygroup AS
$BODY$
DECLARE
   _securityUserID ALIAS FOR $1;

   res tblSecurityGroup%ROWTYPE;
   parentRes tblSecurityGroup%ROWTYPE;
BEGIN
   FOR res IN SELECT g.* FROM tblSecurityGroup g
              INNER JOIN tblSecurityUserMembership m ON m.securityGroupID = g.securityGroupID
              WHERE m.securityUserID = _securityUserID AND g.isActive = true
   LOOP
      RETURN NEXT res;

      FOR parentRes IN SELECT * FROM cpgdb.recurseGetParentGroups(res.securityGroupID, 0) 
      LOOP
         RETURN NEXT parentRes;
      END LOOP;
   END LOOP;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
GRANT EXECUTE ON FUNCTION cpgdb.getgroupmembership(uuid) TO "Webgroup";
GRANT EXECUTE ON FUNCTION cpgdb.getgroupmembership(uuid) TO public;


CREATE OR REPLACE FUNCTION cpgdb.getgroupmembershiparray(uuid)
  RETURNS integer[] AS
$BODY$
   SELECT ARRAY(SELECT DISTINCT SecurityGroupID FROM cpgdb.GetGroupMembership($1) ORDER BY SecurityGroupID ASC);
$BODY$
  LANGUAGE sql IMMUTABLE
  COST 100;
GRANT EXECUTE ON FUNCTION cpgdb.getgroupmembershiparray(uuid) TO "Webgroup";
GRANT EXECUTE ON FUNCTION cpgdb.getgroupmembershiparray(uuid) TO public;

CREATE TYPE securityuseruuidandobjectuuid AS (
	securityuserid uuid,
	objectid uuid
);


CREATE TYPE securityuseruuidandentityid AS (
	securityuserid uuid,
	entityid integer
);

