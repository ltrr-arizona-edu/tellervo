DROP TYPE typVMeasurementSearchResult CASCADE;
CREATE TYPE typVMeasurementSearchResult AS (
   RecursionLevel integer,
   VMeasurementID uuid,
   Op varchar,
   Code varchar,
   StartYear integer,
   ReadingCount integer,
   MeasurementCount integer,
   Comments varchar,
   Modified timestamp
);

DROP TABLE tblVMeasurementMetaCache CASCADE;
CREATE TABLE tblVMeasurementMetaCache (
    VMeasurementID tblvmeasurement.vmeasurementid%TYPE NOT NULL REFERENCES tblVMeasurement ON DELETE CASCADE ON UPDATE CASCADE,
    StartYear integer NOT NULL,
    ReadingCount integer NOT NULL,
    MeasurementCount integer DEFAULT 1 NOT NULL
    vsextent geometry,
    objectCode text,
    objectCount integer,   
    commonTaxonName text,
    taxonCount integer,
    prefix text,
    datingTypeID integer NOT NULL,
    CONSTRAINT enforce_dims_vsextent CHECK ((ndims(vsextent) = 2)),
    CONSTRAINT enforce_geotype_vsextent CHECK (((geometrytype(vsextent) = 'POLYGON'::text) OR (vsextent IS NULL))),
    CONSTRAINT enforce_srid_vsextent CHECK ((srid(vsextent) = 4326))
);

DROP TABLE tblVMeasurementDerivedCache CASCADE;
CREATE TABLE tblVMeasurementDerivedCache
(
  derivedCacheID serial NOT NULL,
  VMeasurementID uuid NOT NULL,
  MeasurementID integer NOT NULL,
  CONSTRAINT tblvMeasurementderivedcache_pkey PRIMARY KEY (derivedcacheid),
  CONSTRAINT tblvMeasurementderivedcache_Measurementid_fkey FOREIGN KEY (Measurementid)
      REFERENCES tblMeasurement (Measurementid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT tblvMeasurementderivedcache_vMeasurementid_fkey FOREIGN KEY (vMeasurementid)
      REFERENCES tblvMeasurement (vMeasurementid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
);
COMMENT ON TABLE tblvMeasurementderivedcache IS 'A non-recursive cache for breaking down vMeasurement 
derivations. Provides a map from vMeasurementid (one) to Measurementid (many) and vice versa. Updated by 
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

DROP TYPE typVMeasurementSummaryInfo CASCADE;
CREATE TYPE typVMeasurementSummaryInfo AS (
   VMeasurementID uuid,
   objectCode text,
   objectCount integer,   
   commonTaxonName text,
   taxonCount integer
);

CREATE TYPE DatingTypeClass AS ENUM ('arbitrary', 'inferred');
