CREATE OR REPLACE FUNCTION cpgdbj.qupdVMeasurementResultOpTruncate( 
  IN paramVMeasurementID uuid,
  IN paramVMeasurementResultID uuid
) RETURNS void AS $$
DECLARE
   XStartRelYear tblTruncate.StartRelYear%TYPE;
   XEndRelYear tblTruncate.EndRelYear%TYPE;
BEGIN
  -- Get the truncate info
  SELECT StartRelYear, EndRelYear INTO XStartRelYear, XEndRelYear
     FROM tblTruncate WHERE VMeasurementID=$1;

  IF NOT FOUND THEN
     RAISE EXCEPTION 'No tblTruncate entry for VMID %', paramVMeasurementID;
  END IF;

  -- Delete the readings we no longer care about
  DELETE FROM tblVMeasurementReadingResult 
    WHERE VMeasurementResultID=$2 AND (RelYear < XStartRelYear OR RelYear > XEndRelYear);

  -- 'redate' the relyears
  -- 
  -- Ugh, we have to do this in two steps
  -- Otherwise, we violate the UNIQUE constraint because it's checked for every row
  -- So, since a RelYear can never be negative... we cheat, and use that as an ugly workaround
  --
  
  -- Step 1: Relyear = - (Relyear - XStartRelYear)
  UPDATE tblVMeasurementReadingResult SET RelYear=-(RelYear-XStartRelYear)
    WHERE VMeasurementResultID=$2;

  -- Step 2: Relyear = -Relyear
  UPDATE tblVMeasurementReadingResult SET RelYear=-RelYear
    WHERE VMeasurementResultID=$2;

  -- Now, effectively, -(-(Relyear-XStartRelYear) cancels out the first two negatives
  -- and we were unique the whole time!

  -- Mark the redating in vmeasurementresult (increment startyear)
  UPDATE tblVMeasurementResult SET StartYear=StartYear+XStartRelYear
    WHERE VMeasurementResultID=$2;

END;
$$ LANGUAGE PLPGSQL VOLATILE;
