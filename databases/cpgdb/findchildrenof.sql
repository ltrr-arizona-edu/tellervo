CREATE OR REPLACE FUNCTION cpgdb.FindChildrenOf(varchar, integer) 
RETURNS SETOF typVMeasurementSearchResult AS $$
DECLARE
   stype ALIAS FOR $1;
   id ALIAS FOR $2;

   i INTEGER;
   searches VARCHAR[] := array['VMeasurement','Measurement','Radius','Specimen','Tree','Subsite','Site'];

   whereClause VARCHAR;
   joinClause VARCHAR;
   query VARCHAR;
   vmid INTEGER;
   res typVMeasurementSearchResult;
BEGIN
   -- Loop through our ordered array above, dynamically creating
   -- an SQL statement full of inner joins.
   joinClause := '';
   FOR i IN array_lower(searches, 1)+1..array_upper(searches,1) LOOP

      joinClause := joinClause || cpgdb._FCOJC(searches[i], searches[i-1]);

      IF lower(searches[i]) = lower(stype) THEN
         whereClause := 'tbl' || searches[i] || '.' || searches[i] || 'ID';
         EXIT;
      END IF;
   END LOOP;   

   IF whereClause IS NULL THEN
      RAISE EXCEPTION 'Invalid search type %', stype;
   END IF;

   query := 'SELECT DISTINCT tblVMeasurement.VMeasurementID FROM tblVMeasurement' 
            || joinClause || ' WHERE ' || whereClause || '=' || id;

   -- RAISE NOTICE 'Query: %', query;

   FOR vmid IN EXECUTE query LOOP
      FOR res IN SELECT * FROM cpgdb.FindVMChildren(vmid) LOOP
         RETURN NEXT res;
      END LOOP;
   END LOOP;
END;
$$ LANGUAGE 'plpgsql' VOLATILE;

CREATE OR REPLACE FUNCTION cpgdb._FCOJC(varchar, varchar)
RETURNS VARCHAR AS $$
BEGIN
   RETURN ' INNER JOIN tbl' || $1 || ' ON tbl' || $1 || '.' || $1 || 'ID=tbl' || $2 || '.' || $1 || 'ID';
END;
$$ LANGUAGE 'plpgsql' IMMUTABLE; 

