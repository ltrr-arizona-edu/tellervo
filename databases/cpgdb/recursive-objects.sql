CREATE OR REPLACE FUNCTION cpgdb.FindObjectTopLevelAncestor(objid tblobject.objectid%TYPE)
RETURNS tblObject AS $$
DECLARE
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
	
CREATE OR REPLACE FUNCTION cpgdb.recurseFindObjectAncestors(objid tblobject.objectid%TYPE, _recursionlevel integer)
RETURNS SETOF tblObject AS $$
DECLARE
   parentid tblobject.objectid%TYPE;
   lastRow tblObject;
   ref refcursor;
   recursionlevel integer := _recursionlevel;
BEGIN
   IF recursionlevel > 50 THEN
      RAISE EXCEPTION 'Maximum recursion exceeded';
   END IF;

   SELECT * INTO lastRow FROM tblObject WHERE objectId = objid;

   -- This shouldn't happen!
   IF NOT FOUND THEN
      RAISE EXCEPTION 'Object ID % not found. Either original object does not exist or object tree is broken.', objid;
   END IF;

   -- Return myself?
   IF recursionlevel <> 0 THEN
      RETURN NEXT lastRow;
   END IF;

   parentid := lastRow.parentObjectId;

   -- We hit the toplevel ancestor, bail!
   IF parentid IS NULL THEN
      RETURN;
   END IF;

   FOR lastRow IN SELECT * FROM cpgdb.recurseFindObjectAncestors(parentid, recursionlevel + 1) LOOP
      RETURN NEXT lastRow;
   END LOOP;
END;
$$ LANGUAGE 'plpgsql' STABLE;

CREATE OR REPLACE FUNCTION cpgdb.recurseFindObjectDescendants(objid tblobject.objectid%TYPE, _recursionlevel integer)
RETURNS SETOF tblObject AS $$
DECLARE
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


CREATE OR REPLACE FUNCTION cpgdb.FindElementObjectAncestors(tblElement.elementId%TYPE)
RETURNS SETOF tblObject AS '
   SELECT * from cpgdb.recurseFindObjectAncestors((SELECT objectId FROM tblElement WHERE elementID=$1), 1)
' LANGUAGE SQL;

CREATE OR REPLACE FUNCTION cpgdb._internalFindObjectsAndDescendantsWhere(whereclause text)
RETURNS SETOF tblObject AS $$
DECLARE
   ref refcursor;

   obj tblObject;
   child tblObject;
   objid tblObject.objectId%TYPE;
BEGIN

   -- Start the query
   OPEN ref FOR EXECUTE 'SELECT tblObject.* FROM tblObject ' ||
	'LEFT JOIN tlkpObjectType USING(objectTypeID) ' ||
	'LEFT JOIN tlkpLocationType USING(locationTypeID) ' ||
	'LEFT JOIN tlkpCoverageTemporal USING(coverageTemporalID) ' ||
	'LEFT JOIN tlkpCoverageTemporalFoundation USING (coverageTemporalFoundationID) ' ||
	'WHERE ' || whereclause;

   LOOP
      FETCH ref INTO obj;

      -- Done?
      IF NOT FOUND THEN
         CLOSE ref;
         EXIT;
      END IF;

      -- Return this object
      RETURN NEXT obj;

      -- Return this object's descendant tree
      objid = obj.objectId;
      FOR child IN SELECT * from cpgdb.recurseFindObjectDescendants(objid, 1) LOOP
         RETURN NEXT child;
      END LOOP;
   END LOOP;
END;
$$ LANGUAGE 'plpgsql' STABLE;

CREATE OR REPLACE FUNCTION cpgdb.FindObjectsAndDescendantsWhere(whereclause text) 
RETURNS SETOF tblObject AS $$
  SELECT DISTINCT * FROM cpgdb._internalFindObjectsAndDescendantsWhere($1);
$$ LANGUAGE SQL;

CREATE OR REPLACE FUNCTION cpgdb.findobjectdescendantsfromcode(thiscode character varying, includeself boolean)
  RETURNS SETOF tblobject AS
$BODY$DECLARE
thiscode ALIAS FOR $1;
includeself ALIAS FOR $2;
thisuuid uuid;
query VARCHAR;
res tblobject%ROWTYPE;
BEGIN

-- Extract UUID for this code;
SELECT objectid INTO thisuuid FROM tblobject where code=thiscode;
IF NOT FOUND THEN
   RAISE EXCEPTION 'No objects match this code';
END IF;

FOR res IN SELECT * FROM cpgdb.findobjectdescendants(thisuuid, includeself) LOOP
  RETURN NEXT res;
END LOOP;

END;$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION cpgdb.findobjectdescendantsfromcode(character varying, boolean) OWNER TO aps03pwb;


CREATE OR REPLACE FUNCTION cpgdb.findobjectancestorsfromcode(thiscode character varying, includeself boolean)
  RETURNS SETOF tblobject AS
$BODY$DECLARE
thiscode ALIAS FOR $1;
includeself ALIAS FOR $2;
thisuuid uuid;
query VARCHAR;
res tblobject%ROWTYPE;
BEGIN

-- Extract UUID for this code;
SELECT objectid INTO thisuuid FROM tblobject where code=thiscode;
IF NOT FOUND THEN
   RAISE EXCEPTION 'No objects match this code';
END IF;

FOR res IN SELECT * FROM cpgdb.findobjectancestors(thisuuid, includeself) LOOP
  RETURN NEXT res;
END LOOP;

END;$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION cpgdb.findobjectancestorsfromcode(character varying, boolean) OWNER TO aps03pwb;

