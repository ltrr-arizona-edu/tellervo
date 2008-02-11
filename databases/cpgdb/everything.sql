-- Set up our basic functions
\i misc.sql

-- our functions that the java library uses
\i sum.sql
\i update-vmresultinfo.sql

-- Make sure our types and tables are ready
\i typesandtables.sql

-- Now, our find functions
\i recursive-find.sql
\i findchildrenof.sql

-- Now, our cache functions
\i vmeasurementmetacache.sql
\i trigger-tblvmeasurementmetacache.sql

-- Now, our functions for creating and changing vmeasurements
\i vm-functions.sql

-- Populate the cache and other setup.
\i setup-db.sql

-- Load GIS functions
\i rastergisfunctions.sql

