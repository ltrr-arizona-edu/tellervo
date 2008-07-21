-- First parameter: VSeriesResultID
CREATE OR REPLACE FUNCTION cpgdb.qupdVSeriesResultInfo(varchar) RETURNS boolean AS $$
DECLARE
   ResultID ALIAS FOR $1;
   vmid tblVSeries.VSeriesID%TYPE;
   n tblVSeries.Name%TYPE;
   desc tblVSeries.Description%TYPE;
   ispub tblVSeries.isPublished%TYPE;
   createTS tblVSeries.CreatedTimestamp%TYPE;
   modTS tblVSeries.LastModifiedTimestamp%TYPE;
BEGIN
   SELECT VSeriesID INTO vmid FROM tblVSeriesResult WHERE VSeriesResultID = ResultID;

   IF NOT FOUND THEN
      RETURN FALSE;
   END IF;

   SELECT Name, Description, isPublished, CreatedTimestamp, LastModifiedTimestamp
     INTO n, desc, ispub, createTS, modTS
     FROM tblVSeries WHERE VSeriesID = vmid;

   IF NOT FOUND THEN
      RETURN FALSE;
   END IF;

   UPDATE tblVSeriesResult SET (Name, Description, isPublished, CreatedTimestamp, LastModifiedTimestamp) =
      (n, desc, ispub, createTS, modTS) 
      WHERE VSeriesResultID = ResultID;

   RETURN TRUE;
END;
$$ LANGUAGE plpgsql VOLATILE;
