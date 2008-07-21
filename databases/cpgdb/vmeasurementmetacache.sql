CREATE OR REPLACE FUNCTION cpgdb.CreateMetaCache(tblVSeries.VSeriesID%TYPE) 
RETURNS tblVSeriesMetaCache AS $$
DECLARE
   vmid ALIAS FOR $1;
   op varchar;
   vmresult tblVSeriesResult%ROWTYPE;
   ret tblVSeriesMetaCache%ROWTYPE;
BEGIN
   -- RAISE NOTICE 'Creating metacache for %', vmid;

   -- acquire the vseriesresult
   BEGIN
      SELECT * INTO vmresult FROM cpgdb.GetVSeriesResult(vmid);
   EXCEPTION WHEN OTHERS THEN
      RAISE NOTICE 'CreateMetaCache(%) failed: VSeries is malformed or does not exist', vmid;
      RETURN NULL;
   END;

   IF NOT FOUND THEN
      RETURN NULL;
   END IF;

   ret.VSeriesID := vmid;
   ret.StartYear := vmresult.StartYear;

   -- Calculate number of values
   SELECT COUNT(*) INTO ret.ValueCount
      FROM tblVSeriesValueResult WHERE VSeriesResultID = vmresult.VSeriesResultID;

   -- Calculate number of seriess
   SELECT FIRST(tlkpVSeriesOp.Name), COUNT(tblVSeriesGroup.VSeriesID) 
      INTO op, ret.SeriesCount
      FROM tblVSeries 
      INNER JOIN tlkpVSeriesOp ON tblVSeries.VSeriesOpID = tlkpVSeriesOp.VSeriesOpID 
      LEFT JOIN tblVSeriesGroup ON tblVSeries.VSeriesID = tblVSeriesGroup.VSeriesID 
      WHERE tblVSeries.VSeriesID = vmid;

   -- For a Direct VSeries, force 1.
   IF op = 'Direct' THEN
      ret.SeriesCount := 1;
   END IF;

   -- Delete and populate the cache
   DELETE FROM tblVSeriesMetaCache WHERE VSeriesID = vmid;
   INSERT INTO tblVSeriesMetaCache(VSeriesID, StartYear, ValueCount, SeriesCount)
      VALUES (ret.VSeriesID, ret.StartYear, ret.ValueCount, ret.SeriesCount);

   -- Clean up
   DELETE FROM tblVSeriesResult WHERE VSeriesResultID = vmresult.VSeriesResultID;

   -- Clear out our tblVSeriesDerivedCache for this VSeries
   DELETE FROM tblVSeriesDerivedCache WHERE VSeriesID = vmid;
   -- Then, populate it.
   INSERT INTO tblVSeriesDerivedCache(VSeriesID,SeriesID) 
      SELECT vmid,series.seriesID FROM cpgdb.FindVMParentSeriess(vmid) series;

   -- Calculate extent of vseries by looking up locations of all associated direct seriess
   SELECT setsrid(extent(tblelement.location)::geometry,4326)
      INTO  ret.vmextent
      FROM  tblelement, tblsample, tblradius, tblseries, tblvseries
      WHERE tblvseries.seriesid=tblseries.seriesid
      AND   tblseries.radiusid=tblradius.radiusid
      AND   tblradius.sampleid=tblsample.sampleid
      AND   tblsample.elementid=tblelement.elementid
      AND   tblvseries.vseriesid
            IN (SELECT vseriesid
                   FROM  cpgdb.FindVMParents(vmid, true)
                   WHERE op='Direct');

   --RAISE NOTICE 'Extent is %', ret.vmextent;

   -- Store extent info
   UPDATE tblVSeriesMetaCache SET vmextent = ret.vmextent WHERE VSeriesID = ret.VSeriesID;


   -- Now, get taxon and label data and update that
   SELECT INTO ret.siteCode, ret.siteCount, ret.CommonTaxonName, ret.taxonCount, ret.label 
       s.siteCode,s.siteCount,s.commonTaxonName,s.taxonCount,cpgdb.GetVSeriesLabel(vmid) AS label 
       FROM cpgdb.getVSeriesSummaryInfo(vmid) AS s;
   UPDATE tblVSeriesMetaCache SET (siteCode, siteCount, commonTaxonName, taxonCount, label) =
       (ret.siteCode, ret.siteCount, ret.CommonTaxonName, ret.taxonCount, ret.label)
       WHERE VSeriesID = vmid;

   -- Return result
   RETURN ret;
END;
$$ LANGUAGE 'plpgsql' VOLATILE;

CREATE OR REPLACE FUNCTION cpgdb.GetMetaCache(tblVSeries.VSeriesID%TYPE) 
RETURNS tblVSeriesMetaCache AS $$
DECLARE
   vmid ALIAS FOR $1;
   ret tblVSeriesMetaCache%ROWTYPE;
BEGIN
   -- Do we already have it cached?
   SELECT * INTO ret FROM tblVSeriesMetaCache WHERE VSeriesID = vmid;
   IF FOUND THEN
      -- RAISE NOTICE 'Cache hit on %', vmid;
      RETURN ret;
   END IF;

   -- Not cached, so we have to make it
   RAISE NOTICE 'Cache miss on %', vmid;
   SELECT * INTO ret FROM cpgdb.CreateMetaCache(vmid);
   RETURN ret;
END;
$$ LANGUAGE 'plpgsql' VOLATILE;

CREATE OR REPLACE FUNCTION cpgdb.GetDerivedCache(tblVSeries.VSeriesID%TYPE) 
RETURNS SETOF tblVSeriesDerivedCache AS $$
   SELECT * FROM cpgdb.GetMetaCache($1);
   SELECT * FROM tblVSeriesDerivedCache WHERE VSeriesID = $1;
$$ LANGUAGE SQL;
