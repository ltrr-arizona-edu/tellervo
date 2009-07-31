--
-- cpgdbj.qupdVMeasurementResultOpRedate
--
-- RETURNS void
-- IN PARAMETERS
---- paramVMeasurementID(uuid)
---- paramRedate(integer)
---- paramCurrentVMeasurementResultID(uuid)

CREATE OR REPLACE FUNCTION cpgdbj.qupdVMeasurementResultOpRedate(
  IN paramVMeasurementID uuid,
  IN paramRedate integer,
  IN paramCurrentVMeasurementResultID uuid
) RETURNS void AS $$
DECLARE
  chosenDatingTypeID tblRedate.RedatingTypeID%TYPE;
BEGIN
  -- Update tblVMeasurementResult with the redating information
  UPDATE tblVMeasurementResult
   SET VMeasurementID=$1, StartYear=$2
   WHERE VMeasurementResultID=$3;

  -- Get the new dating type id
  SELECT redatingTypeID INTO chosenDatingTypeID FROM tblRedate
   WHERE VMeasurementID=$1;

  -- Update to the new dating type, if it's not null
  IF chosenDatingTypeID IS NOT NULL THEN
     UPDATE tblVMeasurementResult SET DatingTypeID=chosenDatingTypeID
      WHERE VMeasurementResultID=$3;
  END IF;
END;
$$ LANGUAGE PLPGSQL VOLATILE;
