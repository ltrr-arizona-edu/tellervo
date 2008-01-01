-- Set up our basic functions
\i misc.sql
\i sum.sql

-- Make sure our types and tables are ready
\i typesandtables.sql

-- Now, our find functions
\i recursive-find.sql
\i findchildrenof.sql

-- Now, our cache functions
\i vmeasurementmetacache.sql
\i trigger-tblvmeasurementmetacache.sql

-- Populate the cache and other setup.
\i setup-db.sql
