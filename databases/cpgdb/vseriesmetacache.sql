CREATE OR REPLACE FUNCTION cpgdb.CreateMetaCache(tblVSeries.VSeriesID%TYPE) 
RETURNS tblVSeriesMetaCache AS $$
DECLARE
   vsid ALIAS FOR $1;
   op varchar;
   vsresult tblVSeriesResult%ROWTYPE;
   ret tblVSeriesMetaCache%ROWTYPE;
BEGIN
   -- RAISE NOTICE 'Creating metacache for %', vsid;

   -- acquire the vseriesresult
   BEGIN
      SELECT * INTO vsresult FROM cpgdb.GetVSeriesResult(vsid);
   EXCEPTION WHEN OTHERS THEN
      RAISE NOTICE 'CreateMetaCache(%) failed: VSeries is malformed or does not exist', vsid;
      RETURN NULL;
   END;

   IF NOT FOUND THEN
      RETURN NULL;
   END IF;

   ret.VSeriesID := vsid;
   ret.StartYear := vsresult.StartYear;

   -- Calculate number of values
   SELECT COUNT(*) INTO ret.ValueCount
      FROM tblVSeriesValueResult WHERE VSeriesResultID = vsresult.VSeriesResultID;

   -- Calculate number of seriess
   SELECT FIRST(tlkpVSeriesOp.Name), COUNT(tblVSeriesGroup.VSeriesID) 
      INTO op, ret.SeriesCount
      FROM tblVSeries 
      INNER JOIN tlkpVSeriesOp ON tblVSeries.VSeriesOpID = tlkpVSeriesOp.VSeriesOpID 
      LEFT JOIN tblVSeriesGroup ON tblVSeries.VSeriesID = tblVSeriesGroup.VSeriesID 
      WHERE tblVSeries.VSeriesID = vsid;

   -- For a Direct VSeries, force 1.
   IF op = 'Direct' THEN
      ret.SeriesCount := 1;
   END IF;

   -- Delete and populate the cache
   DELETE FROM tblVSeriesMetaCache WHERE VSeriesID = vsid;
   INSERT INTO tblVSeriesMetaCache(VSeriesID, StartYear, ValueCount, SeriesCount)
      VALUES (ret.VSeriesID, ret.StartYear, ret.ValueCount, ret.SeriesCount);

   -- Clean up
   DELETE FROM tblVSeriesResult WHERE VSeriesResultID = vsresult.VSeriesResultID;

   -- Clear out our tblVSeriesDerivedCache for this VSeries
   DELETE FROM tblVSeriesDerivedCache WHERE VSeriesID = vsid;
   -- Then, populate it.
   INSERT INTO tblVSeriesDerivedCache(VSeriesID,SeriesID) 
      SELECT vsid,series.seriesID FROM cpgdb.FindVMParentSeriess(vsid) series;

   -- Calculate extent of vseries by looking up locations of all associated direct seriess
   SELECT setsrid(extent(tblelement.location)::geometry,4326)
      INTO  ret.vsextent
      FROM  tblelement, tblsample, tblradius, tblseries, tblvseries
      WHERE tblvseries.seriesid=tblseries.seriesid
      AND   tblseries.radiusid=tblradius.radiusid
      AND   tblradius.sampleid=tblsample.sampleid
      AND   tblsample.elementid=tblelement.elementid
      AND   tblvseries.vseriesid
            IN (SELECT vseriesid
                   FROM  cpgdb.FindVMParents(vsid, true)
                   WHERE op='Direct');

   --RAISE NOTICE 'Extent is %', ret.vsextent;

   -- Store extent info
   UPDATE tblVSeriesMetaCache SET vsextent = ret.vsextent WHERE VSeriesID = ret.VSeriesID;


   -- Now, get taxon and label data and update that
   SELECT INTO ret.siteCode, ret.siteCount, ret.CommonTaxonName, ret.taxonCount, ret.label 
       s.siteCode,s.siteCount,s.commonTaxonName,s.taxonCount,cpgdb.GetVSeriesLabel(vsid) AS label 
       FROM cpgdb.getVSeriesSummaryInfo(vsid) AS s;
   UPDATE tblVSeriesMetaCache SET (siteCode, siteCount, commonTaxonName, taxonCount, label) =
       (ret.siteCode, ret.siteCount, ret.CommonTaxonName, ret.taxonCount, ret.label)
       WHERE VSeriesID = vsid;

   -- Return result
   RETURN ret;
END;
$$ LANGUAGE 'plpgsql' VOLATILE;

CREATE OR REPLACE FUNCTION cpgdb.GetMetaCache(tblVSeries.VSeriesID%TYPE) 
RETURNS tblVSeriesMetaCache AS $$
DECLARE
   vsid ALIAS FOR $1;
   ret tblVSeriesMetaCache%ROWTYPE;
BEGIN
   -- Do we already have it cached?
   SELECT * INTO ret FROM tblVSeriesMetaCache WHERE VSeriesID = vsid;
   IF FOUND THEN
      -- RAISE NOTICE 'Cache hit on %', vsid;
      RETURN ret;
   END IF;

   -- Not cached, so we have to make it
   RAISE NOTICE 'Cache miss on %', vsid;
   SELECT * INTO ret FROM cpgdb.CreateMetaCache(vsid);
   RETURN ret;
END;
$$ LANGUAGE 'plpgsql' VOLATILE;

CREATE OR REPLACE FUNCTION cpgdb.GetDerivedCache(tblVSeries.VSeriesID%TYPE) 
RETURNS SETOF tblVSeriesDerivedCache AS $$
   SELECT * FROM cpgdb.GetMetaCache($1);
   SELECT * FROM tblVSeriesDerivedCache WHERE VSeriesID = $1;
$$ LANGUAGE SQL;
