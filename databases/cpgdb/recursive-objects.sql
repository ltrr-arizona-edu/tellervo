CREATE OR REPLACE FUNCTION cpgdb.FindObjectTopLevelAncestor(objid integer)
RETURNS tblObject AS $$
DECLARE
   parentid integer;
   lastRow tblObject;
BEGIN
   SELECT * INTO lastRow FROM tblObject WHERE objectId = objid;
   
   IF NOT FOUND THEN
      RAISE EXCEPTION 'Object ID % not found. Either original object does not exist or object tree is broken.', objid;
   END IF;

   IF lastRow.parentObjectID IS NULL THEN
      RETURN lastRow;
   END IF;

   SELECT * INTO lastRow FROM cpgdb.FindObjectTopLevelAncestor(lastRow.parentObjectID);
   RETURN lastRow;
END;
$$ LANGUAGE 'plpgsql' STABLE;
	
CREATE OR REPLACE FUNCTION cpgdb.recurseFindObjectAncestors(objid integer, _recursionlevel integer)
RETURNS SETOF tblObject AS $$
DECLARE
   parentid integer;
   lastRow tblObject;
   lastParent tblObject.parentObjectId%TYPE;
   ref refcursor;
   recursionlevel integer := _recursionlevel;
BEGIN
   IF recursionlevel > 50 THEN
      RAISE EXCEPTION 'Maximum recursion exceeded';
   END IF;

   SELECT * INTO lastRow FROM tblObject WHERE objectId = objid;

   -- Not found? End of the line.
   IF NOT FOUND THEN
      EXIT;
   END IF;

   -- Return myself?
   IF recursionlevel <> 0 THEN
      RETURN NEXT lastRow;
   END IF;

   parentid := lastRow.parentObjectId;

   FOR lastRow IN SELECT * FROM cpgdb.recurseFindObjectAncestors(parentid, recursionlevel + 1) LOOP
      RETURN NEXT lastRow;
   END LOOP;
END;
$$ LANGUAGE 'plpgsql' STABLE;

CREATE OR REPLACE FUNCTION cpgdb.recurseFindObjectDescendants(objid integer, _recursionlevel integer)
RETURNS SETOF tblObject AS $$
DECLARE
   parentid integer;
   lastRow tblObject;
   lastParent tblObject.parentObjectId%TYPE;
   ref refcursor;
   recursionlevel integer := _recursionlevel;
BEGIN
   IF recursionlevel > 50 THEN
      RAISE EXCEPTION 'Maximum recursion exceeded';
   END IF;

   -- Return myself?
   IF recursionlevel = -1 THEN
      SELECT * INTO lastRow FROM tblObject WHERE objectId = objid;
      RETURN NEXT lastRow;

      recursionlevel := 1;
   END IF;

   -- Find all one level descendant objects
   OPEN ref FOR SELECT * FROM tblObject WHERE parentObjectID = objid;

   -- Now, recursively find their descendants.
   LOOP
      FETCH ref INTO lastRow;

      -- Done with this part of the tree?
      IF NOT FOUND THEN
         CLOSE ref;
         EXIT;
      END IF;

      RETURN NEXT lastRow;

      lastParent := lastRow.objectId;
      FOR lastRow IN SELECT * from cpgdb.recurseFindObjectDescendants(lastParent, recursionlevel + 1) LOOP
         RETURN NEXT lastRow;
      END LOOP;
   END LOOP;
END;
$$ LANGUAGE 'plpgsql' STABLE;

CREATE OR REPLACE FUNCTION cpgdb.FindObjectAncestors(tblObject.ObjectId%TYPE, boolean)
RETURNS SETOF tblObject AS '
  SELECT * FROM cpgdb.recurseFindObjectAncestors($1, (SELECT CASE WHEN $2=TRUE THEN 1 ELSE 0 END))
' LANGUAGE SQL;

CREATE OR REPLACE FUNCTION cpgdb.FindObjectDescendants(tblObject.ObjectId%TYPE, boolean)
RETURNS SETOF tblObject AS '
  SELECT * FROM cpgdb.recurseFindObjectDescendants($1, (SELECT CASE WHEN $2=TRUE THEN -1 ELSE 0 END))
' LANGUAGE SQL;


