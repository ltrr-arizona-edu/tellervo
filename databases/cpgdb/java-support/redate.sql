--
-- cpgdbj.qupdVMeasurementResultOpRedate
--
-- RETURNS void
-- IN PARAMETERS
---- paramVMeasurementID(uuid)
---- paramCurrentVMeasurementResultID(uuid)

CREATE OR REPLACE FUNCTION cpgdbj.qupdVMeasurementResultOpRedate(
  IN paramVMeasurementID uuid,
  IN paramCurrentVMeasurementResultID uuid
) RETURNS void AS $$
DECLARE
  chosenDatingTypeID tblRedate.RedatingTypeID%TYPE;
  chosenStartYear tblRedate.StartYear%TYPE;
BEGIN
  -- Get the new start year and dating type id
  SELECT startYear,redatingTypeID INTO chosenStartYear,chosenDatingTypeID 
   FROM tblRedate
   WHERE VMeasurementID=$1;

  -- Update tblVMeasurementResult with the redating information
  UPDATE tblVMeasurementResult
   SET VMeasurementID=$1, StartYear=chosenStartYear
   WHERE VMeasurementResultID=$2;

  -- Update to the new dating type, if it's not null
  IF chosenDatingTypeID IS NOT NULL THEN
     UPDATE tblVMeasurementResult SET DatingTypeID=chosenDatingTypeID
      WHERE VMeasurementResultID=$2;
  END IF;
END;
$$ LANGUAGE PLPGSQL VOLATILE;
