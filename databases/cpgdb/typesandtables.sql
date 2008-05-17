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
    vmextent geometry,
    CONSTRAINT enforce_dims_vmextent CHECK ((ndims(vmextent) = 2)),
    CONSTRAINT enforce_geotype_vmextent CHECK (((geometrytype(vmextent) = 'POLYGON'::text) OR (vmextent IS NULL))),
    CONSTRAINT enforce_srid_vmextent CHECK ((srid(vmextent) = 4326))
);

DROP TABLE tblVMeasurementDerivedCache CASCADE;
CREATE TABLE tblVMeasurementDerivedCache
(
  derivedCacheID serial NOT NULL,
  VMeasurementID integer NOT NULL,
  measurementID integer NOT NULL,
  CONSTRAINT tblvmeasurementderivedcache_pkey PRIMARY KEY (derivedcacheid),
  CONSTRAINT tblvmeasurementderivedcache_measurementid_fkey FOREIGN KEY (measurementid)
      REFERENCES tblmeasurement (measurementid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT tblvmeasurementderivedcache_vmeasurementid_fkey FOREIGN KEY (vmeasurementid)
      REFERENCES tblvmeasurement (vmeasurementid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
);
COMMENT ON TABLE tblvmeasurementderivedcache IS 'A non-recursive cache for breaking down vmeasurement 
derivations. Provides a map from vmeasurementid (one) to measurementid (many) and vice versa. Updated by 
metacache functions.';


DROP TYPE typPermissionSet CASCADE;
CREATE TYPE typPermissionSet AS (
   denied boolean,      -- Explicit no permissions
   canCreate boolean,
   canRead boolean,
   canUpdate boolean,
   canDelete boolean,
   decidedBy text
);

