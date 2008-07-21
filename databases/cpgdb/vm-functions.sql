--
-- CreateNewVSeries(VSeriesOp, VSeriesOpParameter, OwnerUserID, Name, 
--                       Description, SeriesID, Constituents)
-- VSeriesOp - Varchar - From tlkpVSeriesOp 
-- VSeriesOpParameter - Integer - Must be specified for REDATE or INDEX; otherwise NULL
-- Name - Varchar - Must be specified
-- Description - Varchar - May be NULL
-- SeriesID - Integer - For direct only; the series derived from.
-- Constituents - Array of VSeriesID - Must be NULL for DIRECT type, an array of one value for any type
--                other than SUM and DIRECT, and an array of one or more values for SUM
-- RETURNS: A new VSeriesID

CREATE OR REPLACE FUNCTION cpgdb.CreateNewVSeries(varchar, integer, integer, varchar, 
varchar, integer, integer[])
RETURNS tblVSeries.VSeriesID%TYPE AS $$
DECLARE
   OP ALIAS FOR $1;
   OPParam ALIAS FOR $2;
   V_OwnerUserID ALIAS FOR $3;
   V_Name ALIAS FOR $4;
   V_Description ALIAS FOR $5;
   BaseSeries ALIAS FOR $6;
   Constituents ALIAS FOR $7;

   newID tblVSeries.VSeriesID%TYPE;
   OPName VARCHAR;
   OPId tblVSeries.VSeriesOpID%TYPE;
   ConstituentSize INTEGER;
   CVMId INTEGER;
BEGIN
   -- Get a new sequence number while we're getting the Op
   SELECT 
      nextval('tblvseries_vseriesid_seq'::regclass), VSeriesOpID, Name
   INTO 
      newID, OPId, OPName
   FROM tlkpVSeriesOp WHERE tlkpVSeriesOp.Name = OP;

   -- Do some basic input validation
   IF NOT FOUND THEN
      RAISE EXCEPTION 'Invalid VSeriesOP %', OP;
   END IF;

   IF V_Name IS NULL THEN
      RAISE EXCEPTION 'Name must not be NULL';
   END IF;

   IF OpName <> 'Direct' THEN
      IF Constituents IS NULL THEN
         RAISE EXCEPTION 'Constituents must not be NULL for non-Direct VSeriess';
      ELSE
         ConstituentSize := 1 + array_upper(Constituents, 1) - array_lower(Constituents, 1);
      END IF;
   END IF;

   IF OPName = 'Direct' THEN
      IF Constituents IS NOT NULL THEN
         RAISE EXCEPTION 'A Direct VSeries may not have any Constituents';
      END IF;

      IF BaseSeries IS NULL THEN
         RAISE EXCEPTION 'A Direct VSeries must have a Base SeriesID';
      END IF;

      -- Our Direct Case is easy; perform it here and leave.
      INSERT INTO tblVSeries(VSeriesID, SeriesID, VSeriesOpID, Name, Description, OwnerUserID)
         VALUES (newID, BaseSeries, OpID, V_Name, V_Description, V_OwnerUserID);

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

   -- Create the VSeries
   INSERT INTO tblVSeries(VSeriesID, VSeriesOpID, Name, Description, OwnerUserID, VSeriesOpParameter, isGenerating)
      VALUES (newID, OpID, V_Name, V_Description, V_OwnerUserID, OPParam, TRUE);

   -- Create the grouping
   FOR CVMId IN array_lower(Constituents, 1)..array_upper(Constituents,1) LOOP   
      INSERT INTO tblVSeriesGroup(VSeriesID, MemberVSeriesID) 
         VALUES (newID, Constituents[CVMId]);
   END LOOP;

   -- Mark the VSeries as completed
   UPDATE tblVSeries SET isGenerating = FALSE WHERE VSeriesID = newID;

   RETURN newID;
END;
$$ LANGUAGE PLPGSQL VOLATILE;
