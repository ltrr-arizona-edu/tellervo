drop schema cpgdb cascade;
create schema cpgdb;

--
-- functions for getting tblvseasurementresults
--

CREATE OR REPLACE FUNCTION cpgdb.GetVSeriesResultID(integer)
  RETURNS "varchar" AS
    'edu.cornell.dendro.cpgdb.Dispatch.GetVSeriesResult'
  LANGUAGE 'javaU' VOLATILE;  

CREATE OR REPLACE FUNCTION cpgdb.GetVSeriesResult(integer)
  RETURNS SETOF tblVSeriesResult AS
    'edu.cornell.dendro.cpgdb.VSeriesResultSet.getVSeriesResultSet'
  LANGUAGE 'javaU' VOLATILE;

--
-- functions for dealing with tblvseasurementreadingresults
--

CREATE OR REPLACE FUNCTION cpgdb.GetVSeriesReadingResult(varchar)
   RETURNS SETOF tblVSeriesReadingResult AS 
      'SELECT * from tblVSeriesReadingResult WHERE VSeriesResultID = $1 ORDER BY relyear'
   LANGUAGE SQL STABLE;

--
-- An SQL 'first' command to return the first item in a GROUP BY statement
--

CREATE OR REPLACE FUNCTION agg_first(state anyelement, value anyelement) RETURNS anyelement
    AS $$
BEGIN

    IF (state IS NULL) THEN
        RETURN value;
    ELSE
        RETURN state;
    END IF;

END;
$$
    LANGUAGE plpgsql IMMUTABLE;

DROP AGGREGATE first(anyelement);
CREATE AGGREGATE first(anyelement) (
    SFUNC = agg_first,
    STYPE = anyelement
);

-- An SQL function that turns an array into rows
-- Makes it possible to JOIN arrays
CREATE OR REPLACE FUNCTION ArrayToRows(arr anyarray) RETURNS SETOF anyelement AS $$
DECLARE
   idx integer;
BEGIN
   FOR idx IN array_lower(arr, 1)..array_upper(arr, 1) LOOP
      RETURN NEXT arr[idx];
   END LOOP;
END;
$$ LANGUAGE PLPGSQL IMMUTABLE;

