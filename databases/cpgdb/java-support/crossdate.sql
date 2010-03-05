--
-- cpgdbj.qupdVMeasurementResultOpCrossdate
--
-- RETURNS void
-- IN PARAMETERS
---- paramVMeasurementID(uuid)
---- paramCurrentVMeasurementResultID(uuid)

CREATE OR REPLACE FUNCTION cpgdbj.qupdVMeasurementResultOpCrossdate(
  IN paramVMeasurementID uuid,
  IN paramCurrentVMeasurementResultID uuid
) RETURNS void AS $$
DECLARE
  myStartYear tblCrossdate.StartYear%TYPE;
  myMasterVMeasurementID tblVMeasurement.VMeasurementID%TYPE;
  myDatingTypeID tlkpDatingType.DatingTypeID%TYPE;
BEGIN
  -- Get data we need for the crossdate
  SELECT StartYear, MasterVMeasurementID INTO myStartYear, myMasterVMeasurementID
    FROM tblCrossdate WHERE VMeasurementID=$1;

  -- Get the dating type id of our master
  SELECT DatingTypeID INTO myDatingTypeID FROM cpgdb.getMetaCache(myMasterVMeasurementID);

  -- Update with new start year and new datingtypeid
  UPDATE tblVMeasurementResult
   SET VMeasurementID=$1, StartYear=myStartYear, DatingTypeID=myDatingTypeID
   WHERE VMeasurementResultID=$2;

END;
$$ LANGUAGE PLPGSQL VOLATILE;
