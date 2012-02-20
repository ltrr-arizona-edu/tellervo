CREATE TABLE tblupgradelog
(
  upgradelogid serial NOT NULL,
  filename character varying NOT NULL,
  "timestamp" timestamp with time zone NOT NULL DEFAULT now(),
  CONSTRAINT pkey_upgradelog PRIMARY KEY (upgradelogid)
)
WITH (
  OIDS=FALSE
);
GRANT ALL ON TABLE tblupgradelog TO "Webgroup";


DROP VIEW vwtblelement;

CREATE VIEW vwtblelement AS 
SELECT ( SELECT findobjecttoplevelancestor.code
           FROM cpgdb.findobjecttoplevelancestor(e.objectid) findobjecttoplevelancestor(objectid, title, code, createdtimestamp, lastmodifiedtimestamp, locationgeometry, locationtypeid, locationprecision, locationcomment, file, creator, owner, parentobjectid, description, objecttypeid, coveragetemporalid, coveragetemporalfoundationid, comments, coveragetemporal, coveragetemporalfoundation, locationaddressline1, locationaddressline2, locationcityortown, locationstateprovinceregion, locationpostalcode, locationcountry)) AS objectcode, e.comments, e.elementid, e.locationprecision, e.code AS title, e.code, e.createdtimestamp, e.lastmodifiedtimestamp, e.locationgeometry, e.islivetree, e.originaltaxonname, e.locationtypeid, e.locationcomment, e.locationaddressline1, e.locationaddressline2, e.locationcityortown, e.locationstateprovinceregion, e.locationpostalcode, e.locationcountry, e.file, e.description, e.processing, e.marks, e.diameter, e.width, e.height, e.depth, e.unsupportedxml, e.objectid, e.elementtypeid, e.elementauthenticityid, e.elementshapeid, auth.elementauthenticity, shape.elementshape, tbltype.elementtype, loctype.locationtype, e.altitude, e.slopeangle, e.slopeazimuth, e.soildescription, e.soildepth, e.bedrockdescription, vwt.taxonid, vwt.taxonlabel, vwt.parenttaxonid, vwt.colid, vwt.colparentid, vwt.taxonrank, unit.unit AS units, unit.unitid as unitid
   FROM tblelement e
   LEFT JOIN tlkpelementauthenticity auth ON e.elementauthenticityid = auth.elementauthenticityid
   LEFT JOIN tlkpelementshape shape ON e.elementshapeid = shape.elementshapeid
   LEFT JOIN tlkpelementtype tbltype ON e.elementtypeid = tbltype.elementtypeid
   LEFT JOIN tlkplocationtype loctype ON e.locationtypeid = loctype.locationtypeid
   LEFT JOIN tlkpunit unit ON e.units = unit.unitid
   LEFT JOIN vwtlkptaxon vwt ON e.taxonid = vwt.taxonid;
GRANT ALL ON vwtblelement TO "Webgroup";

-- Add early and late wood fields to tables

ALTER TABLE tblvmeasurementreadingresult ADD COLUMN ewwidth INTEGER;
ALTER TABLE tblvmeasurementreadingresult ADD COLUMN lwwidth INTEGER;
ALTER TABLE tblreading ADD COLUMN ewwidth INTEGER;
ALTER TABLE tblreading ADD COLUMN lwwidth INTEGER;



-- Modify function to include the early and late wood fields in generated tables

DROP FUNCTION cpgdbj.qappvmeasurementreadingresult(uuid, integer);
CREATE OR REPLACE FUNCTION cpgdbj.qappvmeasurementreadingresult(paramvmeasurementresultid uuid, parammeasurementid integer)
  RETURNS void AS
$BODY$

  INSERT INTO tblVMeasurementReadingResult ( RelYear, Reading, VMeasurementResultID, ReadingID, ewwidth, lwwidth ) 
  SELECT tblReading.RelYear, tblReading.Reading, $1 AS Expr1, tblReading.readingID, tblReading.ewwidth, tblReading.lwwidth
   FROM tblReading 
   WHERE tblReading.MeasurementID=$2

$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
GRANT EXECUTE ON FUNCTION cpgdbj.qappvmeasurementreadingresult(uuid, integer) TO "Webgroup";


DROP VIEW vwjsonnotedreadingresult;
CREATE OR REPLACE VIEW vwjsonnotedreadingresult AS 
 SELECT res.vmeasurementreadingresultid, res.vmeasurementresultid, res.relyear, res.reading, res.wjinc, res.wjdec, res.count, res.readingid, res.ewwidth, res.lwwidth, cpgdb.resultnotestojson(notes.noteids, notes.inheritedcounts) AS jsonnotes
   FROM tblvmeasurementreadingresult res
   LEFT JOIN vwresultnotesasarray notes ON res.vmeasurementresultid = notes.vmeasurementresultid AND res.relyear = notes.relyear;
GRANT ALL ON TABLE vwjsonnotedreadingresult TO "Webgroup";

-- Add new variables to lookup table

INSERT INTO tlkpmeasurementvariable (measurementvariableid, measurementvariable) values (2, 'earlywood width');
INSERT INTO tlkpmeasurementvariable (measurementvariableid, measurementvariable) values (3, 'latewood width');

--
--Update the the minimum version of the Corina client that this server supports
--
DELETE FROM tblsupportedclient where client='Corina WSI';
INSERT INTO tblsupportedclient (client, minversion) VALUES ('Corina WSI', '2.13');

--
-- Add unique constraints to security tables
--
ALTER TABLE tblsecurityobject ADD CONSTRAINT "uniq-object-group-permission" UNIQUE (objectid, securitygroupid, securitypermissionid);
ALTER TABLE tblsecurityelement ADD CONSTRAINT "uniq-element-group-permission" UNIQUE (securitygroupid, securitypermissionid, securityelementid);

--
-- Add functions to maintain Admin group and at least one admin user
--

CREATE OR REPLACE FUNCTION "checkGroupIsDeletable"()
  RETURNS trigger AS
$BODY$BEGIN
IF OLD.securitygroupid = 1 THEN 
	RAISE EXCEPTION 'The administrator group can not be deleted';
END IF;
RETURN NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION "checkGroupIsDeletable"() OWNER TO tellervo;

CREATE TRIGGER "checkGroupIsDeletable"
  BEFORE DELETE
  ON tblsecuritygroup
  FOR EACH ROW
  EXECUTE PROCEDURE "checkGroupIsDeletable"();



CREATE OR REPLACE FUNCTION enforce_atleastoneadminuser()
  RETURNS trigger AS
$BODY$DECLARE
 usrrow tblsecurityuser%ROWTYPE;
 grprow int;
 cnt int;
BEGIN

cnt := 0;
FOR usrrow IN SELECT * from tblsecurityuser WHERE isactive='t' LOOP
   RAISE NOTICE 'Checking details of user %', usrrow.securityuserid;
   FOR grprow IN SELECT securitygroupid from cpgdb.getgroupmembership(usrrow.securityuserid) LOOP
      IF grprow=1 THEN
         cnt = cnt +1;
      END IF;
   END LOOP;   
END LOOP;

RAISE NOTICE 'Number of remaining admin users is %', cnt;

IF(cnt=0) THEN
	RAISE EXCEPTION 'Cannot delete the last user with administrative privileges';
END IF;

RETURN old;

END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION enforce_atleastoneadminuser() OWNER TO tellervo;

CREATE TRIGGER enforce_atleastoneadminuser
  AFTER UPDATE OR DELETE
  ON tblsecurityusermembership
  FOR EACH ROW
  EXECUTE PROCEDURE enforce_atleastoneadminuser();

CREATE TRIGGER enforce_atleastoneadminuser
  AFTER UPDATE OR DELETE
  ON tblsecurityuser
  FOR EACH ROW
  EXECUTE PROCEDURE enforce_atleastoneadminuser();



DELETE FROM tblsecuritydefault WHERE securitygroupid=1;
INSERT INTO tblsecuritydefault (securitygroupid, securitypermissionid) VALUES (1,2), (1,3), (1,4), (1,5);

ALTER TABLE tblsecuritydefault
  ADD CONSTRAINT uniq_defaultperm UNIQUE(securitygroupid, securitypermissionid);


CREATE OR REPLACE FUNCTION enforce_noadminpermedits()
  RETURNS trigger AS
$BODY$BEGIN

IF OLD.securitygroupid=1 THEN
   RAISE EXCEPTION 'Administrator permissions cannot be changed';
   RETURN OLD;
END IF;

RETURN OLD;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION enforce_noadminpermedits() OWNER TO tellervo;


CREATE TRIGGER enforce_noadminpermedits
  BEFORE UPDATE OR DELETE
  ON tblsecuritydefault
  FOR EACH ROW
  EXECUTE PROCEDURE enforce_noadminpermedits();

CREATE TRIGGER enforce_noadminpermedits
  BEFORE INSERT OR UPDATE OR DELETE
  ON tblsecurityobject
  FOR EACH ROW
  EXECUTE PROCEDURE enforce_noadminpermedits();

CREATE TRIGGER enforce_noadminpermedits
  BEFORE INSERT OR UPDATE OR DELETE
  ON tblsecurityelement
  FOR EACH ROW
  EXECUTE PROCEDURE enforce_noadminpermedits();

CREATE TRIGGER enforce_noadminpermedits
  BEFORE INSERT OR UPDATE OR DELETE
  ON tblsecurityvmeasurement
  FOR EACH ROW
  EXECUTE PROCEDURE enforce_noadminpermedits();



--
--  Tidy up 'decided by' field
--

CREATE OR REPLACE FUNCTION cpgdb.getgrouppermissionset(_groupids integer[], _permtype character varying, _pid uuid)
  RETURNS typpermissionset AS
$BODY$
DECLARE
   query varchar;
   whereClause varchar;
   objid uuid;
   res refcursor;
   perm varchar;
   vstype varchar;
   perms typPermissionSet;
   childPerms typPermissionSet;
   setSize integer;

   stypes varchar[] := array['vmeasurement','element','object','default'];
BEGIN


   IF NOT (_permtype = ANY(stypes)) THEN
      RAISE EXCEPTION 'Invalid permission type: %. Should be one of vmeasurement, element, object, default (case matters!).', _permtype;
   END IF;

   IF _permtype = 'default' THEN
      whereClause := '';
   ELSE
      whereClause := ' WHERE ' || _permType || 'Id = ' ||  '''' || _pid ||'''' ;
   END IF;
   
   query := 'SELECT DISTINCT perm.name FROM tblSecurity' || _permType || ' obj ' ||
            'INNER JOIN ArrayToRows(ARRAY[' || array_to_string(_groupIDs, ',') || ']) AS membership ON obj.securityGroupID = membership ' ||
            'INNER JOIN tlkpSecurityPermission perm ON perm.securityPermissionID = obj.securityPermissionID' || whereClause;


   OPEN res FOR EXECUTE query;
   FETCH res INTO perm;

   IF NOT FOUND THEN
      CLOSE res;
      
      IF _permtype = 'vmeasurement' THEN
         SELECT op.Name,tr.ElementID INTO vstype,objid FROM tblVMeasurement vs 
            INNER JOIN tlkpVMeasurementOp op ON vs.VMeasurementOpID = op.VMeasurementOpID
            LEFT JOIN tblMeasurement t1 ON vs.MeasurementID = t1.MeasurementID 
            LEFT JOIN tblRadius t2 ON t2.RadiusID = t1.RadiusID 
            LEFT JOIN tblSample t3 on t3.SampleID = t2.SampleID 
            LEFT JOIN tblElement tr ON tr.ElementID = t3.ElementID
            WHERE vs.VMeasurementID =   _pid  ;

         IF NOT FOUND THEN
            RAISE EXCEPTION 'Could not determine security: vmeasurement % -> element does not exist', _pid;
         END IF;

         IF vstype = 'Direct' THEN
            perms := cpgdb.GetGroupPermissionSet(_groupIDs, 'element', objid);
    RETURN perms;
         ELSE
            setSize := 0;

            FOR objid IN SELECT MemberVMeasurementID FROM tblVMeasurementGroup WHERE VMeasurementID = _pid  LOOP
               childPerms := cpgdb.GetGroupPermissionSet(_groupIDs, 'vmeasurement', objid);

               IF setSize = 0 THEN
                  perms := childperms;
               ELSE
                  perms := cpgdb.AndPermissionSets(perms, childPerms);
               END IF;

               setSize := setSize + 1;
               
            END LOOP;
            RETURN perms;
         END IF;
         
      ELSIF _permType = 'element' THEN
         SELECT tblelement.objectID INTO objid FROM tblElement
                WHERE tblElement.elementid = _pid;

         IF NOT FOUND THEN
            RAISE EXCEPTION 'Could not determine security: element % -> object does not exist', _pid;
         END IF;
         
         perms := cpgdb.GetGroupPermissionSet(_groupIDs, 'object', objid);
         RETURN perms;
         
      ELSIF _permType = 'object' THEN
         perms := cpgdb.GetGroupPermissionSet(_groupIDs, 'default', 'ae68d6d2-2294-11e1-9c20-4ffbb19115a7'::uuid);
         RETURN perms;

      ELSE
 perms.denied := true;
 perms.canRead := false;
 perms.canDelete := false;
 perms.canUpdate := false;
 perms.canCreate := false;
 perms.decidedBy := 'No defaults';
 RETURN perms;

      END IF;

   ELSE

      IF _permType = 'default' THEN
         perms.decidedBy := 'Database defaults';
      ELSE
         perms.decidedBy := 'Permissions overriden at the ' || _permType || ' level';
      END IF;
   
      
      perms.denied := false;
      perms.canRead := false;
      perms.canDelete := false;
      perms.canUpdate := false;
      perms.canCreate := false;

      LOOP
         IF perm = 'No permission' THEN
     perms.denied = true;
         ELSIF perm = 'Read' THEN
            perms.canRead := true;
         ELSIF perm = 'Create' THEN
            perms.canCreate := true;
         ELSIF perm = 'Update' THEN
            perms.canUpdate := true;
         ELSIF perm = 'Delete' THEN
            perms.canDelete := true;
         END IF; 

         FETCH res INTO perm;

         IF NOT FOUND THEN
            EXIT; -- at the end of our permission list
         END IF;
      END LOOP;
   END IF;
   
   CLOSE res;
   RETURN perms;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.getgrouppermissionset(integer[], character varying, uuid) OWNER TO postgres;


ALTER TABLE tblsecuritydefault
  DROP CONSTRAINT "fkey_securitydefault-securitygroup";
  
ALTER TABLE tblsecuritydefault
  ADD CONSTRAINT "fkey_securitydefault-securitygroup" FOREIGN KEY (securitygroupid)
      REFERENCES tblsecuritygroup (securitygroupid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;
      
DROP TRIGGER create_defaultsecurityrecordforgroup ON tblsecuritygroup

DROP FUNCTION "checkGroupIsDeletable"();

CREATE OR REPLACE RULE protectadmin AS
    ON UPDATE TO tblsecuritygroup
   WHERE old.securitygroupid = 1 DO INSTEAD NOTHING;


DELETE FROM tblconfig WHERE key='corinaXSD';
INSERT INTO tblconfig (key,value,description) VALUES ('tellervoXSD', '$baseFolder/schemas/tellervo.xsd', 'Path to Tellervo XSD');

DELETE FROM tblconfig WHERE key='corinaNS';
INSERT INTO tblconfig (key,value,description) VALUES ('tellervoNS', 'http://www.tellervo.org/schema/1.0', 'Tellervo namespace URL');

DELETE FROM tblsupportedclient WHERE client = 'Corina WSI';
INSERT INTO tblsupportedclient (client, minversion) VALUES ('Tellervo WSI', '0.9');


