--
-- CreateNewVMeasurement(VMeasurementOp, VMeasurementOpParameter, OwnerUserID, Name, 
--                       Description, MeasurementID, Constituents)
-- VMeasurementOp - Varchar - From tlkpVMeasurementOp 
-- VMeasurementOpParameter - Integer - Must be specified for REDATE or INDEX; otherwise NULL
-- Name - Varchar - Must be specified
-- Description - Varchar - May be NULL
-- MeasurementID - Integer - For direct only; the measurement derived from.
-- Constituents - Array of VMeasurementID - Must be NULL for DIRECT type, an array of one value for any type
--                other than SUM and DIRECT, and an array of one or more values for SUM
-- RETURNS: A new VMeasurementID

CREATE OR REPLACE FUNCTION cpgdb.CreateNewVMeasurement(varchar, integer, integer, varchar, 
varchar, integer, integer[])
RETURNS tblVMeasurement.VMeasurementID%TYPE AS $$
DECLARE
   OP ALIAS FOR $1;
   OPParam ALIAS FOR $2;
   V_OwnerUserID ALIAS FOR $3;
   V_Name ALIAS FOR $4;
   V_Description ALIAS FOR $5;
   BaseMeasurement ALIAS FOR $6;
   Constituents ALIAS FOR $7;

   newID tblVMeasurement.VMeasurementID%TYPE;
   OPName VARCHAR;
   OPId tblVMeasurement.VMeasurementOpID%TYPE;
   ConstituentSize INTEGER;
   CVMId INTEGER;
BEGIN
   -- Get a new sequence number while we're getting the Op
   SELECT 
      nextval('tblvmeasurement_vmeasurementid_seq'::regclass), VMeasurementOpID, Name
   INTO 
      newID, OPId, OPName
   FROM tlkpVMeasurementOp WHERE tlkpVMeasurementOp.Name = OP;

   -- Do some basic input validation
   IF NOT FOUND THEN
      RAISE EXCEPTION 'Invalid VMeasurementOP %', OP;
   END IF;

   IF V_Name IS NULL THEN
      RAISE EXCEPTION 'Name must not be NULL';
   END IF;

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
      INSERT INTO tblVMeasurement(VMeasurementID, MeasurementID, VMeasurementOpID, Name, Description, OwnerUserID)
         VALUES (newID, BaseMeasurement, OpID, V_Name, V_Description, V_OwnerUserID);

      RETURN newID;

   ELSIF OPName = 'Sum' THEN
      IF ConstituentSize < 1 THEN
         RAISE EXCEPTION 'Sums must have at least one constituent';
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

   ELSIF OPName = 'Clean' THEN
      IF ConstituentSize <> 1 THEN
         RAISE EXCEPTION 'Cleans may only be comprised of one constituent';
      END IF;
   END IF;

   -- Create the VMeasurement
   INSERT INTO tblVMeasurement(VMeasurementID, VMeasurementOpID, Name, Description, OwnerUserID, VMeasurementOpParameter, isGenerating)
      VALUES (newID, OpID, V_Name, V_Description, V_OwnerUserID, OPParam, TRUE);

   -- Create the grouping
   FOR CVMId IN array_lower(Constituents, 1)..array_upper(Constituents,1) LOOP   
      INSERT INTO tblVMeasurementGroup(VMeasurementID, MemberVMeasurementID) 
         VALUES (newID, CVMId);
   END LOOP;

   -- Mark the VMeasurement as completed
   UPDATE tblVMeasurement SET isGenerating = FALSE WHERE VMeasurementID = newID;

   RETURN newID;
END;
$$ LANGUAGE PLPGSQL VOLATILE;
