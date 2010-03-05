CREATE OR REPLACE FUNCTION cpgdb.getSearchResultForID(tblVMeasurement.VMeasurementID%TYPE)
RETURNS typVMeasurementSearchResult AS $$
DECLARE
    _vsid ALIAS FOR $1;
    
    res typVMeasurementSearchResult;
    meta tblVMeasurementMetaCache%ROWTYPE;
BEGIN
    SELECT o.Name, vs.Code, vs.Comments, vs.LastModifiedTimestamp
    INTO res.Op, res.Code, res.Comments, res.Modified
    FROM tblVMeasurement AS vs
      INNER JOIN tlkpVMeasurementOp AS o ON o.VMeasurementOpID = vs.VMeasurementOpID
    WHERE vs.VMeasurementID = _vsid;

    SELECT * INTO meta FROM cpgdb.GetMetaCache(_vsid);
    IF FOUND THEN
       res.StartYear := meta.StartYear;
       res.ReadingCount := meta.ReadingCount;
       res.MeasurementCount := meta.MeasurementCount;
    END IF;

    res.RecursionLevel = 0;
    res.VMeasurementID = _vsid;
    RETURN res;
END;
$$ LANGUAGE 'plpgsql' VOLATILE;

CREATE OR REPLACE FUNCTION cpgdb.recurseFindVMChildren(tblVMeasurement.VMeasurementID%TYPE, integer) 
RETURNS SETOF typVMeasurementSearchResult AS $$
DECLARE
   _vsid ALIAS FOR $1;
   _recursionlevel INTEGER := $2;

   res typVMeasurementSearchResult;
   meta tblVMeasurementMetaCache%ROWTYPE;
   ref refcursor;

   VMeasurementID tblVMeasurement.VMeasurementID%TYPE;
BEGIN
   IF _recursionlevel > 50 THEN
      RAISE EXCEPTION 'Maximum recursion exceeded';
   END IF;

   -- Tricky kludge: If we start with a recursion level of -1, show the search base, too.
   IF _recursionlevel = -1 THEN
      SELECT * INTO res FROM cpgdb.getSearchResultForID(_vsid);
      RETURN NEXT res;
      _recursionlevel := 1;
   END IF;

   -- Loop through each of my direct descendents

   OPEN ref FOR SELECT
      vs.VMeasurementID, o.Name, vs.Code,
      vs.Comments, vs.LastModifiedTimestamp
      FROM tblVMeasurement AS vs
         INNER JOIN tblVMeasurementGroup AS g ON g.VMeasurementID = vs.VMeasurementID
         INNER JOIN tlkpVMeasurementOp AS o ON o.VMeasurementOpID = vs.VMeasurementOpID
      WHERE g.MemberVMeasurementID = _vsid
      ORDER BY vs.LastModifiedTimestamp DESC;

   LOOP
      FETCH ref INTO VMeasurementID, res.Op, res.Code, res.Comments, res.Modified;

      -- No children? Ok; just drop out.
      IF NOT FOUND THEN
         EXIT;
      END IF;

      -- Get the metacache row. We could inner join in our query above,
      -- but calling this function ensures that the cache is created 
      -- if it doesn't exist.      
      SELECT * INTO meta FROM cpgdb.GetMetaCache(VMeasurementID);
      IF FOUND THEN
         res.StartYear := meta.StartYear;
         res.ReadingCount := meta.ReadingCount;
         res.MeasurementCount := meta.MeasurementCount;
      END IF;

      res.RecursionLevel = _recursionlevel;
      res.VMeasurementID = VMeasurementID;
      RETURN NEXT res;
 
      -- Loop through any descendents of descendents
      FOR res IN SELECT * from cpgdb.recurseFindVMChildren(VMeasurementID, _recursionlevel + 1)
      LOOP
         RETURN NEXT res;
      END LOOP;
   END LOOP;
END;
$$ LANGUAGE 'plpgsql' VOLATILE;

CREATE OR REPLACE FUNCTION cpgdb.recurseFindVMParents(tblVMeasurement.VMeasurementID%TYPE, integer) 
RETURNS SETOF typVMeasurementSearchResult AS $$
DECLARE
   _vsid ALIAS FOR $1;
   _recursionlevel INTEGER := $2;

   res typVMeasurementSearchResult;
   meta tblVMeasurementMetaCache%ROWTYPE;
   ref refcursor;

   VMeasurementID tblVMeasurement.VMeasurementID%TYPE;
BEGIN
   IF _recursionlevel > 50 THEN
      RAISE EXCEPTION 'Maximum recursion exceeded';
   END IF;

   -- Tricky kludge: If we start with a recursion level of -1, show the search base, too.
   IF _recursionlevel = -1 THEN
      SELECT * INTO res FROM cpgdb.getSearchResultForID(_vsid);
      RETURN NEXT res;
      _recursionlevel := 1;
   END IF;

   -- Loop through each of my direct parents

   OPEN ref FOR SELECT
      vs.VMeasurementID, o.Name, vs.Code,
      vs.Comments, vs.LastModifiedTimestamp
      FROM tblVMeasurement as vs
         INNER JOIN tblVMeasurementGroup AS g ON g.MemberVMeasurementID = vs.VMeasurementID
         INNER JOIN tlkpVMeasurementOp AS o ON o.VMeasurementOpID = vs.VMeasurementOpID
      WHERE g.VMeasurementID = _vsid
      ORDER BY vs.LastModifiedTimestamp DESC;

   LOOP
      FETCH ref INTO VMeasurementID, res.Op, res.Code, res.Comments, res.Modified;

      -- No parents? Ok; just drop out.
      IF NOT FOUND THEN
         EXIT;
      END IF;

      -- Get the metacache row. We could inner join in our query above,
      -- but calling this function ensures that the cache is created 
      -- if it doesn't exist.      
      SELECT * INTO meta FROM cpgdb.GetMetaCache(VMeasurementID);
      IF FOUND THEN
         res.StartYear := meta.StartYear;
         res.ReadingCount := meta.ReadingCount;
         res.MeasurementCount := meta.MeasurementCount;
      END IF;

      res.RecursionLevel = _recursionlevel;
      res.VMeasurementID = VMeasurementID;
      RETURN NEXT res;

      -- No sense in looking for parents of a direct VM
      IF res.Op = 'Direct' THEN
         CONTINUE;
      END IF;

      -- Loop through any grandparents
      FOR res IN SELECT * from cpgdb.recurseFindVMParents(VMeasurementID, _recursionlevel + 1)
      LOOP
         RETURN NEXT res;
      END LOOP;
   END LOOP;
END;
$$ LANGUAGE 'plpgsql' VOLATILE;

CREATE OR REPLACE FUNCTION cpgdb.FindVMChildren(tblVMeasurement.VMeasurementID%TYPE, boolean)
RETURNS SETOF typVMeasurementSearchResult AS '
  SELECT * FROM cpgdb.recurseFindVMChildren($1, (SELECT CASE WHEN $2=TRUE THEN -1 ELSE 0 END))
' LANGUAGE SQL;

CREATE OR REPLACE FUNCTION cpgdb.FindVMParents(tblVMeasurement.VMeasurementID%TYPE, boolean)
RETURNS SETOF typVMeasurementSearchResult AS '
  SELECT * FROM cpgdb.recurseFindVMParents($1, (SELECT CASE WHEN $2=TRUE THEN -1 ELSE 0 END))
' LANGUAGE SQL;

CREATE OR REPLACE FUNCTION cpgdb.FindVMParentMeasurements(tblVMeasurement.VMeasurementID%TYPE)
RETURNS SETOF tblMeasurement AS $$
  SELECT DISTINCT ON (Measurement.MeasurementID) Measurement.* FROM cpgdb.FindVMParents($1, true) parents 
  INNER JOIN tblVMeasurement vs ON parents.VMeasurementID = vs.VMeasurementID 
  INNER JOIN tblMeasurement Measurement ON Measurement.MeasurementID = vs.MeasurementID 
  WHERE parents.op='Direct';
$$ LANGUAGE SQL;
