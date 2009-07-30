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
  UPDATE tblVMeasurementReadingResult SET RelYear=RelYear-XStartRelYear
    WHERE VMeasurementResultID=$2;

END;
$$ LANGUAGE PLPGSQL VOLATILE;
