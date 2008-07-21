DROP TYPE typVSeriesSearchResult CASCADE;
CREATE TYPE typVSeriesSearchResult AS (
   RecursionLevel integer,
   VSeriesID integer,
   Op varchar,
   Name varchar,
   StartYear integer,
   ReadingCount integer,
   SeriesCount integer,
   Description varchar,
   Modified timestamp
);

DROP TABLE tblVSeriesMetaCache CASCADE;
CREATE TABLE tblVSeriesMetaCache (
    VSeriesID integer NOT NULL REFERENCES tblVSeries ON DELETE CASCADE ON UPDATE CASCADE,
    StartYear integer NOT NULL,
    ReadingCount integer NOT NULL,
    SeriesCount integer DEFAULT 1 NOT NULL
    vmextent geometry,
    siteCode text,
    siteCount integer,   
    commonTaxonName text,
    taxonCount integer
    label text;
    CONSTRAINT enforce_dims_vmextent CHECK ((ndims(vmextent) = 2)),
    CONSTRAINT enforce_geotype_vmextent CHECK (((geometrytype(vmextent) = 'POLYGON'::text) OR (vmextent IS NULL))),
    CONSTRAINT enforce_srid_vmextent CHECK ((srid(vmextent) = 4326))
);

DROP TABLE tblVSeriesDerivedCache CASCADE;
CREATE TABLE tblVSeriesDerivedCache
(
  derivedCacheID serial NOT NULL,
  VSeriesID integer NOT NULL,
  seriesID integer NOT NULL,
  CONSTRAINT tblvseriesderivedcache_pkey PRIMARY KEY (derivedcacheid),
  CONSTRAINT tblvseriesderivedcache_seriesid_fkey FOREIGN KEY (seriesid)
      REFERENCES tblseries (seriesid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT tblvseriesderivedcache_vseriesid_fkey FOREIGN KEY (vseriesid)
      REFERENCES tblvseries (vseriesid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
);
COMMENT ON TABLE tblvseriesderivedcache IS 'A non-recursive cache for breaking down vseries 
derivations. Provides a map from vseriesid (one) to seriesid (many) and vice versa. Updated by 
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

DROP TYPE typVSeriesSummaryInfo CASCADE;
CREATE TYPE typVSeriesSummaryInfo AS (
   VSeriesID integer,
   siteCode text,
   siteCount integer,   
   commonTaxonName text,
   taxonCount integer
);

