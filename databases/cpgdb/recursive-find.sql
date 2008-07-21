CREATE OR REPLACE FUNCTION cpgdb.getSearchResultForID(tblVSeries.VSeriesID%TYPE)
RETURNS typVSeriesSearchResult AS $$
DECLARE
    _vmid ALIAS FOR $1;
    
    res typVSeriesSearchResult;
    meta tblVSeriesMetaCache%ROWTYPE;
BEGIN
    SELECT o.Name, vm.Name, vm.Description, vm.LastModifiedTimestamp
    INTO res.Op, res.Name, res.Description, res.Modified
    FROM tblVSeries AS vm
      INNER JOIN tlkpVSeriesOp AS o ON o.VSeriesOpID = vm.VSeriesOpID
    WHERE vm.VSeriesID = _vmid;

    SELECT * INTO meta FROM cpgdb.GetMetaCache(_vmid);
    IF FOUND THEN
       res.StartYear := meta.StartYear;
       res.ValueCount := meta.ValueCount;
       res.SeriesCount := meta.SeriesCount;
    END IF;

    res.RecursionLevel = 0;
    res.VSeriesID = _vmid;
    RETURN res;
END;
$$ LANGUAGE 'plpgsql' VOLATILE;

CREATE OR REPLACE FUNCTION cpgdb.recurseFindVMChildren(tblVSeries.VSeriesID%TYPE, integer) 
RETURNS SETOF typVSeriesSearchResult AS $$
DECLARE
   _vmid ALIAS FOR $1;
   _recursionlevel INTEGER := $2;

   res typVSeriesSearchResult;
   meta tblVSeriesMetaCache%ROWTYPE;
   ref refcursor;

   VSeriesID tblVSeries.VSeriesID%TYPE;
BEGIN
   IF _recursionlevel > 50 THEN
      RAISE EXCEPTION 'Maximum recursion exceeded';
   END IF;

   -- Tricky kludge: If we start with a recursion level of -1, show the search base, too.
   IF _recursionlevel = -1 THEN
      SELECT * INTO res FROM cpgdb.getSearchResultForID(_vmid);
      RETURN NEXT res;
      _recursionlevel := 1;
   END IF;

   -- Loop through each of my direct descendents

   OPEN ref FOR SELECT
      vm.VSeriesID, o.Name, vm.Name,
      vm.Description, vm.LastModifiedTimestamp
      FROM tblVSeries AS vm
         INNER JOIN tblVSeriesGroup AS g ON g.VSeriesID = vm.VSeriesID
         INNER JOIN tlkpVSeriesOp AS o ON o.VSeriesOpID = vm.VSeriesOpID
      WHERE g.MemberVSeriesID = _vmid
      ORDER BY vm.LastModifiedTimestamp DESC;

   LOOP
      FETCH ref INTO VSeriesID, res.Op, res.Name, res.Description, res.Modified;

      -- No children? Ok; just drop out.
      IF NOT FOUND THEN
         EXIT;
      END IF;

      -- Get the metacache row. We could inner join in our query above,
      -- but calling this function ensures that the cache is created 
      -- if it doesn't exist.      
      SELECT * INTO meta FROM cpgdb.GetMetaCache(VSeriesID);
      IF FOUND THEN
         res.StartYear := meta.StartYear;
         res.ValueCount := meta.ValueCount;
         res.SeriesCount := meta.SeriesCount;
      END IF;

      res.RecursionLevel = _recursionlevel;
      res.VSeriesID = VSeriesID;
      RETURN NEXT res;
 
      -- Loop through any descendents of descendents
      FOR res IN SELECT * from cpgdb.recurseFindVMChildren(VSeriesID, _recursionlevel + 1)
      LOOP
         RETURN NEXT res;
      END LOOP;
   END LOOP;
END;
$$ LANGUAGE 'plpgsql' VOLATILE;

CREATE OR REPLACE FUNCTION cpgdb.recurseFindVMParents(tblVSeries.VSeriesID%TYPE, integer) 
RETURNS SETOF typVSeriesSearchResult AS $$
DECLARE
   _vmid ALIAS FOR $1;
   _recursionlevel INTEGER := $2;

   res typVSeriesSearchResult;
   meta tblVSeriesMetaCache%ROWTYPE;
   ref refcursor;

   VSeriesID tblVSeries.VSeriesID%TYPE;
BEGIN
   IF _recursionlevel > 50 THEN
      RAISE EXCEPTION 'Maximum recursion exceeded';
   END IF;

   -- Tricky kludge: If we start with a recursion level of -1, show the search base, too.
   IF _recursionlevel = -1 THEN
      SELECT * INTO res FROM cpgdb.getSearchResultForID(_vmid);
      RETURN NEXT res;
      _recursionlevel := 1;
   END IF;

   -- Loop through each of my direct parents

   OPEN ref FOR SELECT
      vm.VSeriesID, o.Name, vm.Name,
      vm.Description, vm.LastModifiedTimestamp
      FROM tblVSeries as vm
         INNER JOIN tblVSeriesGroup AS g ON g.MemberVSeriesID = vm.VSeriesID
         INNER JOIN tlkpVSeriesOp AS o ON o.VSeriesOpID = vm.VSeriesOpID
      WHERE g.VSeriesID = _vmid
      ORDER BY vm.LastModifiedTimestamp DESC;

   LOOP
      FETCH ref INTO VSeriesID, res.Op, res.Name, res.Description, res.Modified;

      -- No parents? Ok; just drop out.
      IF NOT FOUND THEN
         EXIT;
      END IF;

      -- Get the metacache row. We could inner join in our query above,
      -- but calling this function ensures that the cache is created 
      -- if it doesn't exist.      
      SELECT * INTO meta FROM cpgdb.GetMetaCache(VSeriesID);
      IF FOUND THEN
         res.StartYear := meta.StartYear;
         res.ValueCount := meta.ValueCount;
         res.SeriesCount := meta.SeriesCount;
      END IF;

      res.RecursionLevel = _recursionlevel;
      res.VSeriesID = VSeriesID;
      RETURN NEXT res;

      -- No sense in looking for parents of a direct VM
      IF res.Op = 'Direct' THEN
         CONTINUE;
      END IF;

      -- Loop through any grandparents
      FOR res IN SELECT * from cpgdb.recurseFindVMParents(VSeriesID, _recursionlevel + 1)
      LOOP
         RETURN NEXT res;
      END LOOP;
   END LOOP;
END;
$$ LANGUAGE 'plpgsql' VOLATILE;

CREATE OR REPLACE FUNCTION cpgdb.FindVMChildren(tblVSeries.VSeriesID%TYPE, boolean)
RETURNS SETOF typVSeriesSearchResult AS '
  SELECT * FROM cpgdb.recurseFindVMChildren($1, (SELECT CASE WHEN $2=TRUE THEN -1 ELSE 0 END))
' LANGUAGE SQL;

CREATE OR REPLACE FUNCTION cpgdb.FindVMParents(tblVSeries.VSeriesID%TYPE, boolean)
RETURNS SETOF typVSeriesSearchResult AS '
  SELECT * FROM cpgdb.recurseFindVMParents($1, (SELECT CASE WHEN $2=TRUE THEN -1 ELSE 0 END))
' LANGUAGE SQL;

CREATE OR REPLACE FUNCTION cpgdb.FindVMParentSeriess(tblVSeries.VSeriesID%TYPE)
RETURNS SETOF tblSeries AS $$
  SELECT series.* FROM cpgdb.FindVMParents($1, true) parents 
  INNER JOIN tblVSeries vm ON parents.VSeriesID = vm.VSeriesID 
  INNER JOIN tblSeries series ON series.SeriesID = vm.SeriesID 
  WHERE parents.op='Direct';
$$ LANGUAGE SQL;
