DROP TYPE typVMeasurementSearchResult CASCADE;
CREATE TYPE typVMeasurementSearchResult AS (
   RecursionLevel integer,
   VMeasurementID integer,
   Op varchar,
   Name varchar,
   StartYear integer,
   ReadingCount integer,
   MeasurementCount integer,
   Description varchar,
   Modified timestamp
);

DROP TABLE tblVMeasurementMetaCache CASCADE;
CREATE TABLE tblVMeasurementMetaCache (
    VMeasurementID integer NOT NULL REFERENCES tblVMeasurement ON DELETE CASCADE ON UPDATE CASCADE,
    StartYear integer NOT NULL,
    ReadingCount integer NOT NULL,
    MeasurementCount integer DEFAULT 1 NOT NULL
);

