--
-- CreateNewVMeasurement(VMeasurementOp, VMeasurementOpParameter, OwnerUserID, Code, 
--                       Comments, MeasurementID, Constituents, Usage, UsageComments, Objective, Version)
-- VMeasurementOp - Varchar - From tlkpVMeasurementOp 
-- VMeasurementOpParameter - Integer - Must be specified for REDATE or INDEX; otherwise NULL
-- Code - Varchar - Must be specified
-- Comments - Varchar - May be NULL
-- MeasurementID - Integer - For direct only; the Measurement derived from.
-- Constituents - Array of VMeasurementID - Must be NULL for DIRECT type, an array of one value for any type
--                other than SUM and DIRECT, and an array of one or more values for SUM
-- RETURNS: A new VMeasurementID

CREATE OR REPLACE FUNCTION cpgdb.CreateNewVMeasurement(
  varchar, -- vmeasurementop
  integer, -- vmeasurementopparameter
  integer, -- owner user id
  varchar, -- code (name)
  varchar, -- comments
  integer, -- base measurement (direct)
  uuid[],  -- constituent measurements (not direct)
  varchar, -- Usage
  varchar, -- Usage comments
  varchar, -- Objective
  varchar  -- Version
)
RETURNS tblVMeasurement.VMeasurementID%TYPE AS $$
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
   -- Get a new sequence number while we're getting the Op
   SELECT 
      uuid_generate_v1mc(), VMeasurementOpID, Name
   INTO 
      newID, OPId, OPName
   FROM tlkpVMeasurementOp WHERE tlkpVMeasurementOp.Name = OP;

   -- Do some basic input validation
   IF NOT FOUND THEN
      RAISE EXCEPTION 'Invalid VMeasurementOP %', OP;
   END IF;

   IF V_Code IS NULL THEN
      RAISE EXCEPTION 'Code must not be NULL';
   END IF;

   -- Default to being done at the end of this function
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

      -- Our Direct Case is easy; perform it here and leave.
      INSERT INTO tblVMeasurement(VMeasurementID, MeasurementID, VMeasurementOpID, Code, Comments, OwnerUserID, 
 				  Usage, UsageComments, Objective, Version)
         VALUES (newID, BaseMeasurement, OpID, V_Code, V_Comments, V_OwnerUserID, 
		 V_Usage, V_UsageComments, V_Objective, V_Version);

      RETURN newID;

   ELSIF OPName = 'Sum' THEN
      IF ConstituentSize < 1 THEN
         RAISE EXCEPTION 'Sums must have at least one constituent';
      END IF;

      -- throws an exception if it fails, so don't do anything
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

   -- Create the VMeasurement
   INSERT INTO tblVMeasurement(VMeasurementID, VMeasurementOpID, Code, Comments, OwnerUserID, VMeasurementOpParameter, 
			       isGenerating, Usage, UsageComments, Objective, Version)
      VALUES (newID, OpID, V_Code, V_Comments, V_OwnerUserID, OPParam, TRUE,
 	      V_Usage, V_UsageComments, V_Objective, V_Version);

   -- Create the grouping
   FOR CVMId IN array_lower(Constituents, 1)..array_upper(Constituents,1) LOOP   
      INSERT INTO tblVMeasurementGroup(VMeasurementID, MemberVMeasurementID) 
         VALUES (newID, Constituents[CVMId]);
   END LOOP;

   -- Mark the VMeasurement as completed if 'done creating'
   IF DoneCreating = TRUE THEN
      UPDATE tblVMeasurement SET isGenerating = FALSE WHERE VMeasurementID = newID;
   END IF;

   RETURN newID;
END;
$$ LANGUAGE PLPGSQL VOLATILE;

--
-- Finishes a crossdate made with CreateVMeasurement
-- 1: VMeasurementID
-- 2: Start Year
-- 3: MasterVMeasurementID
-- 4: Justification
-- 5: Confidence
--
CREATE OR REPLACE FUNCTION cpgdb.FinishCrossdate(tblVMeasurement.VMeasurementID%TYPE, 
   tblCrossdate.startyear%TYPE, tblVMeasurement.VMeasurementID%TYPE, 
   tblCrossdate.justification%TYPE, tblCrossdate.confidence%TYPE)
RETURNS tblCrossdate.CrossdateID%TYPE AS $$
DECLARE
   XVMID ALIAS FOR $1;
   XStartYear ALIAS FOR $2;
   XMasterVMID ALIAS FOR $3;
   XJustification ALIAS FOR $4;
   XConfidence ALIAS FOR $5;

   dummy tblVMeasurement.VMeasurementID%TYPE;
BEGIN
   -- Check to see if our vmeasurement exists
   SELECT VMeasurementID INTO dummy FROM tblVMeasurement
      WHERE VMeasurementID=XVMID;

   IF NOT FOUND THEN
      RAISE EXCEPTION 'VMeasurement for Crossdate does not exist (%)', XVMID;
   END IF;

   -- Check to see if our MASTER vmeasurement exists
   SELECT VMeasurementID INTO dummy FROM tblVMeasurement
      WHERE VMeasurementID=XMasterVMID;

   IF NOT FOUND THEN
      RAISE EXCEPTION 'VMeasurement Master for Crossdate does not exist (%)', XMasterVMID;
   END IF;

   -- Check for sanity
   IF XStartYear IS NULL OR XConfidence IS NULL THEN
      RAISE EXCEPTION 'Invalid arguments to cpgdb.FinishCrossdate';
   END IF;

   -- Create the actual crossdate
   INSERT INTO tblCrossdate(VMeasurementID, MasterVMeasurementID, StartYear, Justification, Confidence)
      VALUES(XVMID, XMasterVMID, XStartYear, XJustification, XConfidence);

   -- Now we're done, so mark it as no longer being generated
   UPDATE tblVMeasurement SET isGenerating = FALSE WHERE VMeasurementID = XVMID;

   -- Get our crossdate id
   RETURN (SELECT CrossdateID from tblCrossdate WHERE VMeasurementID=XVMID);
END;
$$ LANGUAGE PLPGSQL VOLATILE;

CREATE OR REPLACE FUNCTION cpgdb.VerifySumsAreContiguous(uuid[]) 
RETURNS BOOLEAN AS $$
DECLARE
   Constituents ALIAS FOR $1;
   CVMId INTEGER;

   SumStart INTEGER;
   SumEnd INTEGER;

   CStart INTEGER;
   CEnd INTEGER;
   VMID tblVMeasurement.VMeasurementID%TYPE;
BEGIN
   FOR CVMId IN array_lower(Constituents, 1)..array_upper(Constituents,1) LOOP
      -- first, verify it exists
      SELECT VMeasurementID INTO VMID FROM tblVMeasurement WHERE VMeasurementID=Constituents[CVMId];

      IF NOT FOUND THEN
         RAISE EXCEPTION 'Sum constituent does not exist (id:%)', Constituents[CVMId];
      END IF;
      
      -- now, check for continuity
      SELECT StartYear,StartYear+ReadingCount INTO CStart, CEnd from cpgdb.getMetaCache(Constituents[CVMId]);

      IF SumStart IS NULL THEN
         -- First part
         SumStart := CStart;
         SumEnd := CEnd;
      ELSE
         -- ok, check to see if it fits
         IF CStart > SumEnd OR CEnd < SumStart THEN
            RAISE EXCEPTION 'Sum constituents are not contiguous [(%,%) is not in (%, %)]', CStart, CEnd, SumStart, SumEnd;
         END IF;

         -- Update begin/end
         IF CStart < SumStart THEN
            SumStart := CStart;
         END IF;

         IF CEnd > SumEnd THEN
            SumEnd := CEnd;
         END IF;
      END IF;
   END LOOP;

   RETURN TRUE;
END;
$$ LANGUAGE PLPGSQL VOLATILE;
