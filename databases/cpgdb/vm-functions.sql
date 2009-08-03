--
-- CreateNewVMeasurement(VMeasurementOp, VMeasurementOpParameter, OwnerUserID, Code, 
--                       Comments, MeasurementID, Constituents, Objective, Version, Date)
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
  varchar, -- Objective
  varchar,  -- Version
  date     -- measuringDate or derivationDate
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
 				  Objective, Version, Birthdate, IsGenerating)
         VALUES (newID, BaseMeasurement, OpID, V_Code, V_Comments, V_OwnerUserID, 
		 V_Objective, V_Version, V_Birthdate, FALSE);

      RETURN newID;

   ELSIF OPName = 'Sum' THEN
      IF ConstituentSize < 1 THEN
         RAISE EXCEPTION 'Sums must have at least one constituent';
      END IF;

      -- throws an exception if it fails, so don't do anything
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

   -- Create the VMeasurement
   INSERT INTO tblVMeasurement(VMeasurementID, VMeasurementOpID, Code, Comments, OwnerUserID, VMeasurementOpParameter, 
			       isGenerating, Objective, Version, Birthdate)
      VALUES (newID, OpID, V_Code, V_Comments, V_OwnerUserID, OPParam, TRUE,
 	      V_Objective, V_Version, V_Birthdate);

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

   myParentVMID tblVMeasurement.VMeasurementID%TYPE;
   masterDatingClass DatingTypeClass;
   childDatingClass DatingTypeClass;
BEGIN
   -- Find the VMeasurement we're being derived from
   SELECT MemberVMeasurementID INTO myParentVMID FROM tblVMeasurementGroup WHERE VMeasurementID = XVMID;

   -- Check to see if our vmeasurement exists and get its dating class
   SELECT DatingClass INTO childDatingClass FROM cpgdb.getMetaCache(myParentVMID) LEFT JOIN tlkpDatingType USING (datingTypeID);

   IF NOT FOUND THEN
      RAISE EXCEPTION 'VMeasurement for Crossdate does not exist or is invalid (%)', XVMID;
   END IF;

   -- Check to see if our MASTER vmeasurement exists and get its dating class
   SELECT DatingClass INTO masterDatingClass FROM cpgdb.getMetaCache(XMasterVMID) LEFT JOIN tlkpDatingType USING (datingTypeID);

   IF NOT FOUND THEN
      RAISE EXCEPTION 'VMeasurement Master for Crossdate does not exist or is invalid (%)', XMasterVMID;
   END IF;

   -- Check for sanity
   IF XStartYear IS NULL OR XConfidence IS NULL THEN
      RAISE EXCEPTION 'Invalid arguments to cpgdb.FinishCrossdate';
   END IF;

   IF childDatingClass = 'inferred'::DatingTypeClass THEN
      RAISE EXCEPTION 'You cannot crossdate a series with inferred/absolute dating.';
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

--
-- Finishes a truncate made with CreateVMeasurement
-- 1: VMeasurementID
-- 2: Relative start year
-- 3: Relative end year
-- 4: Justification
--
CREATE OR REPLACE FUNCTION cpgdb.FinishTruncate(tblVMeasurement.VMeasurementID%TYPE, 
   tblTruncate.startrelyear%TYPE, tblTruncate.endrelyear%TYPE, tbltruncate.justification%TYPE)
RETURNS tblTruncate.TruncateID%TYPE AS $$
DECLARE
   XVMID ALIAS FOR $1;
   XStartRelYear ALIAS FOR $2;
   XEndRelYear ALIAS FOR $3;
   XJustification ALIAS FOR $4;

   myParentVMID tblVMeasurement.VMeasurementID%TYPE;
   CStart integer;
   CCount integer;
BEGIN
   -- Find the VMeasurement we're being derived from
   SELECT MemberVMeasurementID INTO myParentVMID FROM tblVMeasurementGroup WHERE VMeasurementID = XVMID;

   IF NOT FOUND THEN
      RAISE EXCEPTION 'VMeasurement for Truncate does not exist or is invalid (%)', XVMID;
   END IF;

   -- now, check for continuity and similar dating types
   SELECT StartYear,ReadingCount INTO CStart, CCount from cpgdb.getMetaCache(myParentVMID);

   IF NOT FOUND THEN
      RAISE EXCEPTION 'Parent measurement does not exist or is malformed for truncate (%/%)', XVMID, myParentVMID;
   END IF;

   -- Check for sanity
   IF XStartRelYear IS NULL OR XEndRelYear IS NULL THEN
      RAISE EXCEPTION 'Invalid arguments to cpgdb.FinishTruncate';
   END IF;

   -- Make sure the year parameters are within the range of the VM we're truncating
   IF XStartRelYear < 0 OR XStartRelYear > CCount THEN
      RAISE EXCEPTION 'Truncate StartYear is out of range (% <> [%,%])', XStartRelYear, 0, CCount;
   END IF;
   IF XEndRelYear < XStartRelYear OR XEndRelYear > CCount THEN
      RAISE EXCEPTION 'Truncate EndYear is out of range (% <> [%,%])', XEndRelYear, XStartRelYear, CCount;
   END IF;

   -- Create the actual truncate
   INSERT INTO tblTruncate(VMeasurementID, StartRelYear, EndRelYear, Justification)
      VALUES(XVMID, XStartRelYear, XEndRelYear, XJustification);

   -- Now we're done, so mark it as no longer being generated
   UPDATE tblVMeasurement SET isGenerating = FALSE WHERE VMeasurementID = XVMID;

   -- Get our truncate id
   RETURN (SELECT TruncateID from tblTruncate WHERE VMeasurementID=XVMID);
END;
$$ LANGUAGE PLPGSQL VOLATILE;

--
-- Finishes a redate made with CreateVMeasurement
-- 1: VMeasurementID
-- 2: new start year
-- 3: tlkpdatingtype identifier (or NULL, to inherit)
-- 4: Justification
--
CREATE OR REPLACE FUNCTION cpgdb.FinishRedate(tblVMeasurement.VMeasurementID%TYPE, 
   tblredate.startyear%TYPE, tblredate.redatingtypeid%TYPE, tblredate.justification%TYPE)
RETURNS tblredate.redateID%TYPE AS $$
DECLARE
   XVMID ALIAS FOR $1;
   XStartYear ALIAS FOR $2;
   XRedatingTypeID ALIAS FOR $3;
   XJustification ALIAS FOR $4;

   dummy tblVMeasurement.VMeasurementID%TYPE;
BEGIN
   -- Check to see if our vmeasurement exists
   SELECT VMeasurementID INTO dummy FROM tblVMeasurement
      WHERE VMeasurementID=XVMID;

   IF NOT FOUND THEN
      RAISE EXCEPTION 'VMeasurement for redate does not exist (%)', XVMID;
   END IF;

   -- Check for sanity
   IF XStartYear IS NULL THEN
      RAISE EXCEPTION 'Invalid arguments to cpgdb.Finishredate';
   END IF;

   -- Create the actual redate
   INSERT INTO tblredate(VMeasurementID, StartYear, RedatingTypeID, Justification)
      VALUES(XVMID, XStartYear, XRedatingTypeID, XJustification);

   -- Now we're done, so mark it as no longer being generated
   UPDATE tblVMeasurement SET isGenerating = FALSE WHERE VMeasurementID = XVMID;

   -- Get our redate id
   RETURN (SELECT redateID from tblredate WHERE VMeasurementID=XVMID);
END;
$$ LANGUAGE PLPGSQL VOLATILE;

CREATE OR REPLACE FUNCTION cpgdb.VerifySumsAreContiguousAndDated(uuid[]) 
RETURNS BOOLEAN AS $$
DECLARE
   Constituents ALIAS FOR $1;
   CVMId INTEGER;

   SumStart INTEGER;
   SumEnd INTEGER;

   CStart INTEGER;
   CEnd INTEGER;
   VMID tblVMeasurement.VMeasurementID%TYPE;

   CurDatingTypeID INTEGER;
   
   CurDatingClass DatingTypeClass;
   LastDatingClass DatingTypeClass;
BEGIN
   FOR CVMId IN array_lower(Constituents, 1)..array_upper(Constituents,1) LOOP
      -- first, verify it exists
      SELECT VMeasurementID INTO VMID FROM tblVMeasurement WHERE VMeasurementID=Constituents[CVMId];

      IF NOT FOUND THEN
         RAISE EXCEPTION 'Sum constituent does not exist (id:%)', Constituents[CVMId];
      END IF;
      
      -- now, check for continuity and similar dating types
      SELECT StartYear,StartYear+ReadingCount,DatingClass INTO CStart, CEnd, CurDatingClass from cpgdb.getMetaCache(Constituents[CVMId])
             LEFT JOIN tlkpDatingType USING (datingTypeID);
      
      -- Ensure dating classes are the same
      IF LastDatingClass IS NOT NULL AND CurDatingClass <> LastDatingClass THEN
         RAISE EXCEPTION 'Sum contains elements from different dating classes, which is not valid (%, %)', CurDatingClass, LastDatingClass;
      ELSE
         LastDatingClass := CurDatingClass;
      END IF;

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
