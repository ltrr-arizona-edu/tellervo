
DROP FUNCTION cpgdb.createnewvmeasurement(character varying, integer, integer, character varying, character varying, integer, uuid[], character varying, character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION cpgdb.createnewvmeasurement(character varying, integer, uuid, character varying, character varying, integer, uuid[], character varying, character varying, character varying, character varying)
  RETURNS uuid AS
$BODY$
DECLARE
   OP ALIAS FOR $1;
   OPParam ALIAS FOR $2;
   V_OwnerUserID ALIAS FOR $3;
   V_Code ALIAS FOR $4;
   V_Comments ALIAS FOR $5;
   BaseMeasurement ALIAS FOR $6;
   Constituents ALIAS FOR $7;
   V_Usage ALIAS FOR $8;
   V_UsageComments ALIAS FOR $9;
   V_Objective ALIAS FOR $10;
   V_Version ALIAS FOR $11;

   newID tblVMeasurement.VMeasurementID%TYPE;
   OPName VARCHAR;
   OPId tblVMeasurement.VMeasurementOpID%TYPE;
   ConstituentSize INTEGER;
   CVMId INTEGER;

   DoneCreating BOOLEAN;
BEGIN
   SELECT 
      uuid_generate_v1mc(), VMeasurementOpID, Name
   INTO 
      newID, OPId, OPName
   FROM tlkpVMeasurementOp WHERE tlkpVMeasurementOp.Name = OP;

   IF NOT FOUND THEN
      RAISE EXCEPTION 'Invalid VMeasurementOP %', OP;
   END IF;

   IF V_Code IS NULL THEN
      RAISE EXCEPTION 'Code must not be NULL';
   END IF;

   DoneCreating := TRUE;

   IF OpName <> 'Direct' THEN
      IF Constituents IS NULL THEN
         RAISE EXCEPTION 'Constituents must not be NULL for non-Direct VMeasurements';
      ELSE
         ConstituentSize := 1 + array_upper(Constituents, 1) - array_lower(Constituents, 1);
      END IF;
   END IF;

   IF OPName = 'Direct' THEN
      IF Constituents IS NOT NULL THEN
         RAISE EXCEPTION 'A Direct VMeasurement may not have any Constituents';
      END IF;

      IF BaseMeasurement IS NULL THEN
         RAISE EXCEPTION 'A Direct VMeasurement must have a Base MeasurementID';
      END IF;

      INSERT INTO tblVMeasurement(VMeasurementID, MeasurementID, VMeasurementOpID, Code, Comments, OwnerUserID, 
 				  Usage, UsageComments, Objective, Version)
         VALUES (newID, BaseMeasurement, OpID, V_Code, V_Comments, V_OwnerUserID, 
		 V_Usage, V_UsageComments, V_Objective, V_Version);

      RETURN newID;

   ELSIF OPName = 'Sum' THEN
      IF ConstituentSize < 1 THEN
         RAISE EXCEPTION 'Sums must have at least one constituent';
      END IF;

      IF cpgdb.VerifySumsAreContiguous(Constituents) = FALSE THEN
         NULL;      
      END IF;

   ELSIF OPName = 'Index' THEN
      IF OPParam IS NULL THEN
         RAISE EXCEPTION 'Indexes must have an index type parameter';
      END IF;

      IF ConstituentSize <> 1 THEN
         RAISE EXCEPTION 'Indexes may only be comprised of one constituent';
      END IF;

   ELSIF OPName = 'Redate' THEN
      IF OPParam IS NULL THEN
         RAISE EXCEPTION 'Redates must have a redate parameter';
      END IF;

      IF ConstituentSize <> 1 THEN
         RAISE EXCEPTION 'Redates may only be comprised of one constituent';
      END IF;

   ELSIF OPName = 'Crossdate' THEN
      IF ConstituentSize <> 1 THEN
         RAISE EXCEPTION 'Crossdates may only be comprised of one constituent';
      END IF;

      DoneCreating := FALSE; -- we have to finish in another function

   ELSIF OPName = 'Clean' THEN
      IF ConstituentSize <> 1 THEN
         RAISE EXCEPTION 'Cleans may only be comprised of one constituent';
      END IF;

   END IF;

   INSERT INTO tblVMeasurement(VMeasurementID, VMeasurementOpID, Code, Comments, OwnerUserID, VMeasurementOpParameter, 
			       isGenerating, Usage, UsageComments, Objective, Version)
      VALUES (newID, OpID, V_Code, V_Comments, V_OwnerUserID, OPParam, TRUE,
 	      V_Usage, V_UsageComments, V_Objective, V_Version);

   FOR CVMId IN array_lower(Constituents, 1)..array_upper(Constituents,1) LOOP   
      INSERT INTO tblVMeasurementGroup(VMeasurementID, MemberVMeasurementID) 
         VALUES (newID, Constituents[CVMId]);
   END LOOP;

   IF DoneCreating = TRUE THEN
      UPDATE tblVMeasurement SET isGenerating = FALSE WHERE VMeasurementID = newID;
   END IF;

   RETURN newID;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.createnewvmeasurement(character varying, integer, integer, character varying, character varying, integer, uuid[], character varying, character varying, character varying, character varying)
  OWNER TO postgres;




DROP FUNCTION cpgdb.createnewvmeasurement(character varying, integer, integer, character varying, character varying, integer, uuid[], character varying, character varying, date)

CREATE OR REPLACE FUNCTION cpgdb.createnewvmeasurement(character varying, integer, uuid, character varying, character varying, integer, uuid[], character varying, character varying, date)
  RETURNS uuid AS
$BODY$
DECLARE
   OP ALIAS FOR $1;
   OPParam ALIAS FOR $2;
   V_OwnerUserID ALIAS FOR $3;
   V_Code ALIAS FOR $4;
   V_Comments ALIAS FOR $5;
   BaseMeasurement ALIAS FOR $6;
   Constituents ALIAS FOR $7;
   V_Objective ALIAS FOR $8;
   V_Version ALIAS FOR $9;
   V_Birthdate ALIAS FOR $10;

   newID tblVMeasurement.VMeasurementID%TYPE;
   OPName VARCHAR;
   OPId tblVMeasurement.VMeasurementOpID%TYPE;
   ConstituentSize INTEGER;
   CVMId INTEGER;

   DoneCreating BOOLEAN;
BEGIN
   SELECT 
      uuid_generate_v1mc(), VMeasurementOpID, Name
   INTO 
      newID, OPId, OPName
   FROM tlkpVMeasurementOp WHERE tlkpVMeasurementOp.Name = OP;

   IF NOT FOUND THEN
      RAISE EXCEPTION 'Invalid VMeasurementOP %', OP;
   END IF;

   IF V_Code IS NULL THEN
      RAISE EXCEPTION 'Code must not be NULL';
   END IF;

   DoneCreating := TRUE;

   IF OpName <> 'Direct' THEN
      IF Constituents IS NULL THEN
         RAISE EXCEPTION 'Constituents must not be NULL for non-Direct VMeasurements';
      ELSE
         ConstituentSize := 1 + array_upper(Constituents, 1) - array_lower(Constituents, 1);
      END IF;

      IF V_Version IS NULL THEN
         SELECT COUNT(*) INTO CVMId FROM cpgdb.getUsedVersionsForConstituents(Constituents) vname 
            WHERE vname IS NULL;
         IF CVMId > 0 THEN
            RAISE EXCEPTION 'EVERSIONALREADYEXISTS: An unversioned derived measurement already exists for this parent';
         END IF;
      ELSE
         SELECT COUNT(*) INTO CVMId FROM cpgdb.getUsedVersionsForConstituents(Constituents) vname 
            WHERE vname=V_Version;
         IF CVMId > 0 THEN
            RAISE EXCEPTION 'EVERSIONALREADYEXISTS: A derived measurement already exists for this parent with the given version';
         END IF;
      END IF;

   ELSIF V_Version IS NOT NULL THEN
      RAISE EXCEPTION 'Version must be null for direct VMeasurements!';
   END IF;

   IF OPName = 'Direct' THEN
      IF Constituents IS NOT NULL THEN
         RAISE EXCEPTION 'A Direct VMeasurement may not have any Constituents';
      END IF;

      IF BaseMeasurement IS NULL THEN
         RAISE EXCEPTION 'A Direct VMeasurement must have a Base MeasurementID';
      END IF;

      INSERT INTO tblVMeasurement(VMeasurementID, MeasurementID, VMeasurementOpID, Code, Comments, OwnerUserID, 
 				  Objective, Version, Birthdate, IsGenerating)
         VALUES (newID, BaseMeasurement, OpID, V_Code, V_Comments, V_OwnerUserID, 
		 V_Objective, V_Version, V_Birthdate, FALSE);

      RETURN newID;

   ELSIF OPName = 'Sum' THEN
      IF ConstituentSize < 1 THEN
         RAISE EXCEPTION 'Sums must have at least one constituent';
      END IF;

      IF cpgdb.VerifySumsAreContiguousAndDated(Constituents) = FALSE THEN
         NULL;      
      END IF;

   ELSIF OPName = 'Index' THEN
      IF OPParam IS NULL THEN
         RAISE EXCEPTION 'Indexes must have an index type parameter';
      END IF;

      IF ConstituentSize <> 1 THEN
         RAISE EXCEPTION 'Indexes may only be comprised of one constituent';
      END IF;

   ELSIF OPName = 'Redate' THEN
      IF OPParam IS NOT NULL THEN
         RAISE EXCEPTION 'Redates must not have a redate parameter - specify in finishRedate!';
      END IF;

      IF ConstituentSize <> 1 THEN
         RAISE EXCEPTION 'Redates may only be comprised of one constituent';
      END IF;

      DoneCreating := FALSE; -- we have to finish in another function

   ELSIF OPName = 'Crossdate' THEN
      IF OPParam IS NOT NULL THEN
         RAISE EXCEPTION 'Crossdates must not have a redate parameter - specify in finishCrossdate!';
      END IF;

      IF ConstituentSize <> 1 THEN
         RAISE EXCEPTION 'Crossdates may only be comprised of one constituent';
      END IF;

      DoneCreating := FALSE; -- we have to finish in another function

   ELSIF OPName = 'Truncate' THEN
      IF OPParam IS NOT NULL THEN
         RAISE EXCEPTION 'Truncates do not have a paramater - specify in finishTruncate';
      END IF;

      IF ConstituentSize <> 1 THEN
         RAISE EXCEPTION 'Truncates may only be comprised of one constituent';
      END IF;

      DoneCreating := FALSE; -- we have to finish in another function

   ELSIF OPName = 'Clean' THEN
      IF ConstituentSize <> 1 THEN
         RAISE EXCEPTION 'Cleans may only be comprised of one constituent';
      END IF;

   ELSE
      RAISE EXCEPTION 'Unsupported vmeasurement type / internal error: %', OPName;
   END IF;

   INSERT INTO tblVMeasurement(VMeasurementID, VMeasurementOpID, Code, Comments, OwnerUserID, VMeasurementOpParameter, 
			       isGenerating, Objective, Version, Birthdate)
      VALUES (newID, OpID, V_Code, V_Comments, V_OwnerUserID, OPParam, TRUE,
 	      V_Objective, V_Version, V_Birthdate);

   FOR CVMId IN array_lower(Constituents, 1)..array_upper(Constituents,1) LOOP   
      INSERT INTO tblVMeasurementGroup(VMeasurementID, MemberVMeasurementID) 
         VALUES (newID, Constituents[CVMId]);
   END LOOP;

   IF DoneCreating = TRUE THEN
      UPDATE tblVMeasurement SET isGenerating = FALSE WHERE VMeasurementID = newID;
   END IF;

   RETURN newID;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.createnewvmeasurement(character varying, integer, integer, character varying, character varying, integer, uuid[], character varying, character varying, date)
  OWNER TO pbrewer;







-- Auto create UUID pkeys

alter table tblobject alter column objectid set default uuid_generate_v1mc();
alter table tblelement alter column elementid set default uuid_generate_v1mc();
alter table tblsample alter column sampleid set default uuid_generate_v1mc();
alter table tblradius alter column radiusid set default uuid_generate_v1mc();
alter table tblvmeasurement alter column vmeasurementid set default uuid_generate_v1mc();


-- Adding sample status tables and values

CREATE TABLE tlkpsamplestatus
(
  samplestatusid serial NOT NULL,
  samplestatus character varying,
  CONSTRAINT pkey_samplestatustype PRIMARY KEY (samplestatusid)
)
WITH (
  OIDS=FALSE
);
insert into tlkpsamplestatus (samplestatus) values ('Unprepped');
insert into tlkpsamplestatus (samplestatus) values ('Prepped');
insert into tlkpsamplestatus (samplestatus) values ('Undateable');
insert into tlkpsamplestatus (samplestatus) values ('Dated');
insert into tlkpsamplestatus (samplestatus) values ('Measured');
insert into tlkpsamplestatus (samplestatus) values ('Too few rings');

alter table tblsample add column samplestatusid integer;
alter table tblsample add constraint "fkey_sample-samplestatus" 
FOREIGN KEY (samplestatusid) REFERENCES tlkpsamplestatus (samplestatusid) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;



-- ADDING TAGGING

CREATE TABLE tbltag
(
  tagid uuid NOT NULL DEFAULT uuid_generate_v1mc(),
  tag character varying NOT NULL,
  ownerid uuid,
  global boolean NOT NULL DEFAULT false,
  CONSTRAINT pkey_tbltag PRIMARY KEY (tagid),
  CONSTRAINT "fkey_tagowner-securityuser" FOREIGN KEY (ownerid)
      REFERENCES tblsecurityuser (securityuserid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
)
WITH (
  OIDS=FALSE
);

CREATE UNIQUE INDEX index_globaltags
ON tbltag (tag)
WHERE ownerid IS NULL;

CREATE UNIQUE INDEX index_privatetags
ON tbltag (tag, ownerid)
WHERE ownerid IS NOT NULL;


CREATE TABLE tblvmeasurementtotag
(
  vmeasurementtotagid serial NOT NULL,
  tagid uuid NOT NULL,
  vmeasurementid uuid NOT NULL,
  CONSTRAINT pkey_vmeasurementtotag PRIMARY KEY (vmeasurementtotagid),
  CONSTRAINT "fkey_vmeasurementtotag-tag" FOREIGN KEY (tagid)
      REFERENCES tbltag (tagid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT "fkey_vmeasurementtotag-vmeasurement" FOREIGN KEY (vmeasurementid)
      REFERENCES tblvmeasurement (vmeasurementid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT uniq_tagforvmeasurement UNIQUE (tagid, vmeasurementid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE tblvmeasurementtotag
  OWNER TO tellervo;




