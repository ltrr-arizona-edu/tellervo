drop schema cpgdb cascade;
create schema cpgdb;

--
-- functions for getting tblvmeasurementresults
--

CREATE OR REPLACE FUNCTION cpgdb.GetVMeasurementResultID(integer)
  RETURNS "varchar" AS
    'edu.cornell.dendro.cpgdb.Dispatch.GetVMeasurementResult'
  LANGUAGE 'javaU' VOLATILE;  

CREATE OR REPLACE FUNCTION cpgdb.GetVMeasurementResult(integer)
  RETURNS SETOF tblVMeasurementResult AS
    'edu.cornell.dendro.cpgdb.VMeasurementResultSet.getVMeasurementResultSet'
  LANGUAGE 'javaU' VOLATILE;

--
-- functions for dealing with tblvmeasurementreadingresults
--

CREATE OR REPLACE FUNCTION cpgdb.GetVMeasurementReadingResult(varchar)
   RETURNS SETOF tblVMeasurementReadingResult AS 
      'SELECT * from tblVMeasurementReadingResult WHERE VMeasurementResultID = $1 ORDER BY relyear'
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

