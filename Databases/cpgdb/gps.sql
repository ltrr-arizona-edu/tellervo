--
-- PostgreSQL database dump
--

SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'Standard public schema';


--
-- Name: plpgsql; Type: PROCEDURAL LANGUAGE; Schema: -; Owner: postgres
--

CREATE PROCEDURAL LANGUAGE plpgsql;


SET search_path = public, pg_catalog;

--
-- Name: box2d; Type: SHELL TYPE; Schema: public; Owner: aps03pwb
--

CREATE TYPE box2d;


--
-- Name: box2d_in(cstring); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION box2d_in(cstring) RETURNS box2d
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX2DFLOAT4_in'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.box2d_in(cstring) OWNER TO aps03pwb;

--
-- Name: box2d_out(box2d); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION box2d_out(box2d) RETURNS cstring
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX2DFLOAT4_out'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.box2d_out(box2d) OWNER TO aps03pwb;

--
-- Name: box2d; Type: TYPE; Schema: public; Owner: aps03pwb
--

CREATE TYPE box2d (
    INTERNALLENGTH = 16,
    INPUT = box2d_in,
    OUTPUT = box2d_out,
    ALIGNMENT = int4,
    STORAGE = plain
);


ALTER TYPE public.box2d OWNER TO aps03pwb;

--
-- Name: box3d; Type: SHELL TYPE; Schema: public; Owner: aps03pwb
--

CREATE TYPE box3d;


--
-- Name: box3d_in(cstring); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION box3d_in(cstring) RETURNS box3d
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX3D_in'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.box3d_in(cstring) OWNER TO aps03pwb;

--
-- Name: box3d_out(box3d); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION box3d_out(box3d) RETURNS cstring
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX3D_out'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.box3d_out(box3d) OWNER TO aps03pwb;

--
-- Name: box3d; Type: TYPE; Schema: public; Owner: aps03pwb
--

CREATE TYPE box3d (
    INTERNALLENGTH = 48,
    INPUT = box3d_in,
    OUTPUT = box3d_out,
    ALIGNMENT = double,
    STORAGE = plain
);


ALTER TYPE public.box3d OWNER TO aps03pwb;

--
-- Name: chip; Type: SHELL TYPE; Schema: public; Owner: aps03pwb
--

CREATE TYPE chip;


--
-- Name: chip_in(cstring); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION chip_in(cstring) RETURNS chip
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'CHIP_in'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.chip_in(cstring) OWNER TO aps03pwb;

--
-- Name: chip_out(chip); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION chip_out(chip) RETURNS cstring
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'CHIP_out'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.chip_out(chip) OWNER TO aps03pwb;

--
-- Name: chip; Type: TYPE; Schema: public; Owner: aps03pwb
--

CREATE TYPE chip (
    INTERNALLENGTH = variable,
    INPUT = chip_in,
    OUTPUT = chip_out,
    ALIGNMENT = double,
    STORAGE = extended
);


ALTER TYPE public.chip OWNER TO aps03pwb;

--
-- Name: geometry; Type: SHELL TYPE; Schema: public; Owner: aps03pwb
--

CREATE TYPE geometry;


--
-- Name: geometry_analyze(internal); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry_analyze(internal) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_analyze'
    LANGUAGE c STRICT;


ALTER FUNCTION public.geometry_analyze(internal) OWNER TO aps03pwb;

--
-- Name: geometry_in(cstring); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry_in(cstring) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_in'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry_in(cstring) OWNER TO aps03pwb;

--
-- Name: geometry_out(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry_out(geometry) RETURNS cstring
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_out'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry_out(geometry) OWNER TO aps03pwb;

--
-- Name: geometry_recv(internal); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry_recv(internal) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_recv'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry_recv(internal) OWNER TO aps03pwb;

--
-- Name: geometry_send(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry_send(geometry) RETURNS bytea
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_send'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry_send(geometry) OWNER TO aps03pwb;

--
-- Name: geometry; Type: TYPE; Schema: public; Owner: aps03pwb
--

CREATE TYPE geometry (
    INTERNALLENGTH = variable,
    INPUT = geometry_in,
    OUTPUT = geometry_out,
    RECEIVE = geometry_recv,
    SEND = geometry_send,
    ANALYZE = geometry_analyze,
    DELIMITER = ':',
    ALIGNMENT = int4,
    STORAGE = main
);


ALTER TYPE public.geometry OWNER TO aps03pwb;

--
-- Name: histogram2d; Type: SHELL TYPE; Schema: public; Owner: aps03pwb
--

CREATE TYPE histogram2d;


--
-- Name: histogram2d_in(cstring); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION histogram2d_in(cstring) RETURNS histogram2d
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'lwhistogram2d_in'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.histogram2d_in(cstring) OWNER TO aps03pwb;

--
-- Name: histogram2d_out(histogram2d); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION histogram2d_out(histogram2d) RETURNS cstring
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'lwhistogram2d_out'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.histogram2d_out(histogram2d) OWNER TO aps03pwb;

--
-- Name: histogram2d; Type: TYPE; Schema: public; Owner: aps03pwb
--

CREATE TYPE histogram2d (
    INTERNALLENGTH = variable,
    INPUT = histogram2d_in,
    OUTPUT = histogram2d_out,
    ALIGNMENT = double,
    STORAGE = main
);


ALTER TYPE public.histogram2d OWNER TO aps03pwb;

--
-- Name: spheroid; Type: SHELL TYPE; Schema: public; Owner: aps03pwb
--

CREATE TYPE spheroid;


--
-- Name: spheroid_in(cstring); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION spheroid_in(cstring) RETURNS spheroid
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'ellipsoid_in'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.spheroid_in(cstring) OWNER TO aps03pwb;

--
-- Name: spheroid_out(spheroid); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION spheroid_out(spheroid) RETURNS cstring
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'ellipsoid_out'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.spheroid_out(spheroid) OWNER TO aps03pwb;

--
-- Name: spheroid; Type: TYPE; Schema: public; Owner: aps03pwb
--

CREATE TYPE spheroid (
    INTERNALLENGTH = 65,
    INPUT = spheroid_in,
    OUTPUT = spheroid_out,
    ALIGNMENT = double,
    STORAGE = plain
);


ALTER TYPE public.spheroid OWNER TO aps03pwb;

--
-- Name: geometry_dump; Type: TYPE; Schema: public; Owner: aps03pwb
--

CREATE TYPE geometry_dump AS (
	path integer[],
	geom geometry
);


ALTER TYPE public.geometry_dump OWNER TO aps03pwb;

--
-- Name: gpxRecord; Type: TYPE; Schema: public; Owner: aps03pwb
--

CREATE TYPE "gpxRecord" AS (
	gid integer,
	name character varying(80),
	elevation double precision,
	symbol character varying(80),
	"comment" character varying(80),
	descriptio character varying(80),
	source character varying(80),
	url character varying(80),
	url_name character varying(80),
	the_geom geometry
);


ALTER TYPE public."gpxRecord" OWNER TO aps03pwb;

--
-- Name: addauth(text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION addauth(text) RETURNS boolean
    AS $_$
DECLARE
	lockid alias for $1;
	okay boolean;
	myrec record;
BEGIN
	-- check to see if table exists
	--  if not, CREATE TEMP TABLE mylock (transid xid, lockcode text)
	okay := 'f';
	FOR myrec IN SELECT * FROM pg_class WHERE relname = 'temp_lock_have_table' LOOP
		okay := 't';
	END LOOP; 
	IF (okay <> 't') THEN 
		CREATE TEMP TABLE temp_lock_have_table (transid xid, lockcode text);
			-- this will only work from pgsql7.4 up
			-- ON COMMIT DELETE ROWS;
	END IF;

	--  INSERT INTO mylock VALUES ( $1)
--	EXECUTE 'INSERT INTO temp_lock_have_table VALUES ( '||
--		quote_literal(getTransactionID()) || ',' ||
--		quote_literal(lockid) ||')';

	INSERT INTO temp_lock_have_table VALUES (getTransactionID(), lockid);

	RETURN true::boolean;
END;
$_$
    LANGUAGE plpgsql;


ALTER FUNCTION public.addauth(text) OWNER TO aps03pwb;

--
-- Name: addbbox(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION addbbox(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_addBBOX'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.addbbox(geometry) OWNER TO aps03pwb;

--
-- Name: addgeometrycolumn(character varying, character varying, character varying, character varying, integer, character varying, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION addgeometrycolumn(character varying, character varying, character varying, character varying, integer, character varying, integer) RETURNS text
    AS $_$
DECLARE
	catalog_name alias for $1;
	schema_name alias for $2;
	table_name alias for $3;
	column_name alias for $4;
	new_srid alias for $5;
	new_type alias for $6;
	new_dim alias for $7;
	rec RECORD;
	schema_ok bool;
	real_schema name;

BEGIN

	IF ( not ( (new_type ='GEOMETRY') or
		   (new_type ='GEOMETRYCOLLECTION') or
		   (new_type ='POINT') or 
		   (new_type ='MULTIPOINT') or
		   (new_type ='POLYGON') or
		   (new_type ='MULTIPOLYGON') or
		   (new_type ='LINESTRING') or
		   (new_type ='MULTILINESTRING') or
		   (new_type ='GEOMETRYCOLLECTIONM') or
		   (new_type ='POINTM') or 
		   (new_type ='MULTIPOINTM') or
		   (new_type ='POLYGONM') or
		   (new_type ='MULTIPOLYGONM') or
		   (new_type ='LINESTRINGM') or
		   (new_type ='MULTILINESTRINGM') or
                   (new_type = 'CIRCULARSTRING') or
                   (new_type = 'CIRCULARSTRINGM') or
                   (new_type = 'COMPOUNDCURVE') or
                   (new_type = 'COMPOUNDCURVEM') or
                   (new_type = 'CURVEPOLYGON') or
                   (new_type = 'CURVEPOLYGONM') or
                   (new_type = 'MULTICURVE') or
                   (new_type = 'MULTICURVEM') or
                   (new_type = 'MULTISURFACE') or
                   (new_type = 'MULTISURFACEM')) )
	THEN
		RAISE EXCEPTION 'Invalid type name - valid ones are: 
			GEOMETRY, GEOMETRYCOLLECTION, POINT, 
			MULTIPOINT, POLYGON, MULTIPOLYGON, 
			LINESTRING, MULTILINESTRING,
                        CIRCULARSTRING, COMPOUNDCURVE,
                        CURVEPOLYGON, MULTICURVE, MULTISURFACE,
			GEOMETRYCOLLECTIONM, POINTM, 
			MULTIPOINTM, POLYGONM, MULTIPOLYGONM, 
			LINESTRINGM, MULTILINESTRINGM 
                        CIRCULARSTRINGM, COMPOUNDCURVEM,
                        CURVEPOLYGONM, MULTICURVEM or MULTISURFACEM';
		return 'fail';
	END IF;

	IF ( (new_dim >4) or (new_dim <0) ) THEN
		RAISE EXCEPTION 'invalid dimension';
		return 'fail';
	END IF;

	IF ( (new_type LIKE '%M') and (new_dim!=3) ) THEN

		RAISE EXCEPTION 'TypeM needs 3 dimensions';
		return 'fail';
	END IF;

	IF ( schema_name != '' ) THEN
		schema_ok = 'f';
		FOR rec IN SELECT nspname FROM pg_namespace WHERE text(nspname) = schema_name LOOP
			schema_ok := 't';
		END LOOP;

		if ( schema_ok <> 't' ) THEN
			RAISE NOTICE 'Invalid schema name - using current_schema()';
			SELECT current_schema() into real_schema;
		ELSE
			real_schema = schema_name;
		END IF;

	ELSE
		SELECT current_schema() into real_schema;
	END IF;


	-- Add geometry column

	EXECUTE 'ALTER TABLE ' ||
		quote_ident(real_schema) || '.' || quote_ident(table_name)
		|| ' ADD COLUMN ' || quote_ident(column_name) || 
		' geometry ';


	-- Delete stale record in geometry_column (if any)

	EXECUTE 'DELETE FROM geometry_columns WHERE
		f_table_catalog = ' || quote_literal('') || 
		' AND f_table_schema = ' ||
		quote_literal(real_schema) || 
		' AND f_table_name = ' || quote_literal(table_name) ||
		' AND f_geometry_column = ' || quote_literal(column_name);


	-- Add record in geometry_column 

	EXECUTE 'INSERT INTO geometry_columns VALUES (' ||
		quote_literal('') || ',' ||
		quote_literal(real_schema) || ',' ||
		quote_literal(table_name) || ',' ||
		quote_literal(column_name) || ',' ||
		new_dim || ',' || new_srid || ',' ||
		quote_literal(new_type) || ')';

	-- Add table checks

	EXECUTE 'ALTER TABLE ' || 
		quote_ident(real_schema) || '.' || quote_ident(table_name)
		|| ' ADD CONSTRAINT ' 
		|| quote_ident('enforce_srid_' || column_name)
		|| ' CHECK (SRID(' || quote_ident(column_name) ||
		') = ' || new_srid || ')' ;

	EXECUTE 'ALTER TABLE ' || 
		quote_ident(real_schema) || '.' || quote_ident(table_name)
		|| ' ADD CONSTRAINT '
		|| quote_ident('enforce_dims_' || column_name)
		|| ' CHECK (ndims(' || quote_ident(column_name) ||
		') = ' || new_dim || ')' ;

	IF (not(new_type = 'GEOMETRY')) THEN
		EXECUTE 'ALTER TABLE ' || 
		quote_ident(real_schema) || '.' || quote_ident(table_name)
		|| ' ADD CONSTRAINT '
		|| quote_ident('enforce_geotype_' || column_name)
		|| ' CHECK (geometrytype(' ||
		quote_ident(column_name) || ')=' ||
		quote_literal(new_type) || ' OR (' ||
		quote_ident(column_name) || ') is null)';
	END IF;

	return 
		real_schema || '.' || 
		table_name || '.' || column_name ||
		' SRID:' || new_srid ||
		' TYPE:' || new_type || 
		' DIMS:' || new_dim || chr(10) || ' '; 
END;
$_$
    LANGUAGE plpgsql STRICT;


ALTER FUNCTION public.addgeometrycolumn(character varying, character varying, character varying, character varying, integer, character varying, integer) OWNER TO aps03pwb;

--
-- Name: addgeometrycolumn(character varying, character varying, character varying, integer, character varying, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION addgeometrycolumn(character varying, character varying, character varying, integer, character varying, integer) RETURNS text
    AS $_$
DECLARE
	ret  text;
BEGIN
	SELECT AddGeometryColumn('',$1,$2,$3,$4,$5,$6) into ret;
	RETURN ret;
END;
$_$
    LANGUAGE plpgsql STABLE STRICT;


ALTER FUNCTION public.addgeometrycolumn(character varying, character varying, character varying, integer, character varying, integer) OWNER TO aps03pwb;

--
-- Name: addgeometrycolumn(character varying, character varying, integer, character varying, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION addgeometrycolumn(character varying, character varying, integer, character varying, integer) RETURNS text
    AS $_$
DECLARE
	ret  text;
BEGIN
	SELECT AddGeometryColumn('','',$1,$2,$3,$4,$5) into ret;
	RETURN ret;
END;
$_$
    LANGUAGE plpgsql STRICT;


ALTER FUNCTION public.addgeometrycolumn(character varying, character varying, integer, character varying, integer) OWNER TO aps03pwb;

--
-- Name: addpoint(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION addpoint(geometry, geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_addpoint'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.addpoint(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: addpoint(geometry, geometry, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION addpoint(geometry, geometry, integer) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_addpoint'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.addpoint(geometry, geometry, integer) OWNER TO aps03pwb;

--
-- Name: affine(geometry, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION affine(geometry, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_affine'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.affine(geometry, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision) OWNER TO aps03pwb;

--
-- Name: affine(geometry, double precision, double precision, double precision, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION affine(geometry, double precision, double precision, double precision, double precision, double precision, double precision) RETURNS geometry
    AS $_$SELECT affine($1,  $2, $3, 0,  $4, $5, 0,  0, 0, 1,  $6, $7, 0)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.affine(geometry, double precision, double precision, double precision, double precision, double precision, double precision) OWNER TO aps03pwb;

--
-- Name: area(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION area(geometry) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_area_polygon'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.area(geometry) OWNER TO aps03pwb;

--
-- Name: area2d(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION area2d(geometry) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_area_polygon'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.area2d(geometry) OWNER TO aps03pwb;

--
-- Name: asbinary(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION asbinary(geometry) RETURNS bytea
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_asBinary'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.asbinary(geometry) OWNER TO aps03pwb;

--
-- Name: asbinary(geometry, text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION asbinary(geometry, text) RETURNS bytea
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_asBinary'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.asbinary(geometry, text) OWNER TO aps03pwb;

--
-- Name: asewkb(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION asewkb(geometry) RETURNS bytea
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'WKBFromLWGEOM'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.asewkb(geometry) OWNER TO aps03pwb;

--
-- Name: asewkb(geometry, text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION asewkb(geometry, text) RETURNS bytea
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'WKBFromLWGEOM'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.asewkb(geometry, text) OWNER TO aps03pwb;

--
-- Name: asewkt(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION asewkt(geometry) RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_asEWKT'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.asewkt(geometry) OWNER TO aps03pwb;

--
-- Name: asgml(geometry, integer, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION asgml(geometry, integer, integer) RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_asGML'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.asgml(geometry, integer, integer) OWNER TO aps03pwb;

--
-- Name: asgml(geometry, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION asgml(geometry, integer) RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_asGML'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.asgml(geometry, integer) OWNER TO aps03pwb;

--
-- Name: asgml(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION asgml(geometry) RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_asGML'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.asgml(geometry) OWNER TO aps03pwb;

--
-- Name: ashexewkb(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION ashexewkb(geometry) RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_asHEXEWKB'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.ashexewkb(geometry) OWNER TO aps03pwb;

--
-- Name: ashexewkb(geometry, text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION ashexewkb(geometry, text) RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_asHEXEWKB'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.ashexewkb(geometry, text) OWNER TO aps03pwb;

--
-- Name: askml(geometry, integer, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION askml(geometry, integer, integer) RETURNS text
    AS $_$SELECT AsUKML(transform($1,4326),$2,$3)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.askml(geometry, integer, integer) OWNER TO aps03pwb;

--
-- Name: askml(geometry, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION askml(geometry, integer) RETURNS text
    AS $_$SELECT AsUKML(transform($1,4326),$2)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.askml(geometry, integer) OWNER TO aps03pwb;

--
-- Name: askml(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION askml(geometry) RETURNS text
    AS $_$SELECT AsUKML(transform($1,4326))$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.askml(geometry) OWNER TO aps03pwb;

--
-- Name: assvg(geometry, integer, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION assvg(geometry, integer, integer) RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'assvg_geometry'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.assvg(geometry, integer, integer) OWNER TO aps03pwb;

--
-- Name: assvg(geometry, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION assvg(geometry, integer) RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'assvg_geometry'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.assvg(geometry, integer) OWNER TO aps03pwb;

--
-- Name: assvg(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION assvg(geometry) RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'assvg_geometry'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.assvg(geometry) OWNER TO aps03pwb;

--
-- Name: astext(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION astext(geometry) RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_asText'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.astext(geometry) OWNER TO aps03pwb;

--
-- Name: asukml(geometry, integer, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION asukml(geometry, integer, integer) RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_asKML'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.asukml(geometry, integer, integer) OWNER TO aps03pwb;

--
-- Name: asukml(geometry, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION asukml(geometry, integer) RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_asKML'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.asukml(geometry, integer) OWNER TO aps03pwb;

--
-- Name: asukml(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION asukml(geometry) RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_asKML'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.asukml(geometry) OWNER TO aps03pwb;

--
-- Name: azimuth(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION azimuth(geometry, geometry) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_azimuth'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.azimuth(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: bdmpolyfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION bdmpolyfromtext(text, integer) RETURNS geometry
    AS $_$
DECLARE
	geomtext alias for $1;
	srid alias for $2;
	mline geometry;
	geom geometry;
BEGIN
	mline := MultiLineStringFromText(geomtext, srid);

	IF mline IS NULL
	THEN
		RAISE EXCEPTION 'Input is not a MultiLinestring';
	END IF;

	geom := multi(BuildArea(mline));

	RETURN geom;
END;
$_$
    LANGUAGE plpgsql IMMUTABLE STRICT;


ALTER FUNCTION public.bdmpolyfromtext(text, integer) OWNER TO aps03pwb;

--
-- Name: bdpolyfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION bdpolyfromtext(text, integer) RETURNS geometry
    AS $_$
DECLARE
	geomtext alias for $1;
	srid alias for $2;
	mline geometry;
	geom geometry;
BEGIN
	mline := MultiLineStringFromText(geomtext, srid);

	IF mline IS NULL
	THEN
		RAISE EXCEPTION 'Input is not a MultiLinestring';
	END IF;

	geom := BuildArea(mline);

	IF GeometryType(geom) != 'POLYGON'
	THEN
		RAISE EXCEPTION 'Input returns more then a single polygon, try using BdMPolyFromText instead';
	END IF;

	RETURN geom;
END;
$_$
    LANGUAGE plpgsql IMMUTABLE STRICT;


ALTER FUNCTION public.bdpolyfromtext(text, integer) OWNER TO aps03pwb;

--
-- Name: boundary(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION boundary(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'boundary'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.boundary(geometry) OWNER TO aps03pwb;

--
-- Name: box(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION box(geometry) RETURNS box
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_to_BOX'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.box(geometry) OWNER TO aps03pwb;

--
-- Name: box(box3d); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION box(box3d) RETURNS box
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX3D_to_BOX'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.box(box3d) OWNER TO aps03pwb;

--
-- Name: box2d(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION box2d(geometry) RETURNS box2d
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_to_BOX2DFLOAT4'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.box2d(geometry) OWNER TO aps03pwb;

--
-- Name: box2d(box3d); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION box2d(box3d) RETURNS box2d
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX3D_to_BOX2DFLOAT4'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.box2d(box3d) OWNER TO aps03pwb;

--
-- Name: box2d_contain(box2d, box2d); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION box2d_contain(box2d, box2d) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX2D_contain'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.box2d_contain(box2d, box2d) OWNER TO aps03pwb;

--
-- Name: box2d_contained(box2d, box2d); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION box2d_contained(box2d, box2d) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX2D_contained'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.box2d_contained(box2d, box2d) OWNER TO aps03pwb;

--
-- Name: box2d_intersects(box2d, box2d); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION box2d_intersects(box2d, box2d) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX2D_intersects'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.box2d_intersects(box2d, box2d) OWNER TO aps03pwb;

--
-- Name: box2d_left(box2d, box2d); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION box2d_left(box2d, box2d) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX2D_left'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.box2d_left(box2d, box2d) OWNER TO aps03pwb;

--
-- Name: box2d_overlap(box2d, box2d); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION box2d_overlap(box2d, box2d) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX2D_overlap'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.box2d_overlap(box2d, box2d) OWNER TO aps03pwb;

--
-- Name: box2d_overleft(box2d, box2d); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION box2d_overleft(box2d, box2d) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX2D_overleft'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.box2d_overleft(box2d, box2d) OWNER TO aps03pwb;

--
-- Name: box2d_overright(box2d, box2d); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION box2d_overright(box2d, box2d) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX2D_overright'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.box2d_overright(box2d, box2d) OWNER TO aps03pwb;

--
-- Name: box2d_right(box2d, box2d); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION box2d_right(box2d, box2d) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX2D_right'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.box2d_right(box2d, box2d) OWNER TO aps03pwb;

--
-- Name: box2d_same(box2d, box2d); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION box2d_same(box2d, box2d) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX2D_same'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.box2d_same(box2d, box2d) OWNER TO aps03pwb;

--
-- Name: box3d(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION box3d(geometry) RETURNS box3d
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_to_BOX3D'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.box3d(geometry) OWNER TO aps03pwb;

--
-- Name: box3d(box2d); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION box3d(box2d) RETURNS box3d
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX2DFLOAT4_to_BOX3D'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.box3d(box2d) OWNER TO aps03pwb;

--
-- Name: box3dtobox(box3d); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION box3dtobox(box3d) RETURNS box
    AS $_$SELECT box($1)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.box3dtobox(box3d) OWNER TO aps03pwb;

--
-- Name: buffer(geometry, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION buffer(geometry, double precision) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'buffer'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.buffer(geometry, double precision) OWNER TO aps03pwb;

--
-- Name: buffer(geometry, double precision, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION buffer(geometry, double precision, integer) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'buffer'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.buffer(geometry, double precision, integer) OWNER TO aps03pwb;

--
-- Name: build_histogram2d(histogram2d, text, text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION build_histogram2d(histogram2d, text, text) RETURNS histogram2d
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'build_lwhistogram2d'
    LANGUAGE c STABLE STRICT;


ALTER FUNCTION public.build_histogram2d(histogram2d, text, text) OWNER TO aps03pwb;

--
-- Name: build_histogram2d(histogram2d, text, text, text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION build_histogram2d(histogram2d, text, text, text) RETURNS histogram2d
    AS $_$
BEGIN
	EXECUTE 'SET local search_path = '||$2||',public';
	RETURN public.build_histogram2d($1,$3,$4);
END
$_$
    LANGUAGE plpgsql STABLE STRICT;


ALTER FUNCTION public.build_histogram2d(histogram2d, text, text, text) OWNER TO aps03pwb;

--
-- Name: buildarea(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION buildarea(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_buildarea'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.buildarea(geometry) OWNER TO aps03pwb;

--
-- Name: bytea(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION bytea(geometry) RETURNS bytea
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_to_bytea'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.bytea(geometry) OWNER TO aps03pwb;

--
-- Name: cache_bbox(); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION cache_bbox() RETURNS "trigger"
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'cache_bbox'
    LANGUAGE c;


ALTER FUNCTION public.cache_bbox() OWNER TO aps03pwb;

--
-- Name: centroid(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION centroid(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'centroid'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.centroid(geometry) OWNER TO aps03pwb;

--
-- Name: checkauth(text, text, text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION checkauth(text, text, text) RETURNS integer
    AS $_$
DECLARE
	schema text;
BEGIN
	IF NOT LongTransactionsEnabled() THEN
		RAISE EXCEPTION 'Long transaction support disabled, use EnableLongTransaction() to enable.';
	END IF;

	if ( $1 != '' ) THEN
		schema = $1;
	ELSE
		SELECT current_schema() into schema;
	END IF;

	-- TODO: check for an already existing trigger ?

	EXECUTE 'CREATE TRIGGER check_auth BEFORE UPDATE OR DELETE ON ' 
		|| quote_ident(schema) || '.' || quote_ident($2)
		||' FOR EACH ROW EXECUTE PROCEDURE CheckAuthTrigger('
		|| quote_literal($3) || ')';

	RETURN 0;
END;
$_$
    LANGUAGE plpgsql;


ALTER FUNCTION public.checkauth(text, text, text) OWNER TO aps03pwb;

--
-- Name: checkauth(text, text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION checkauth(text, text) RETURNS integer
    AS $_$SELECT CheckAuth('', $1, $2)$_$
    LANGUAGE sql;


ALTER FUNCTION public.checkauth(text, text) OWNER TO aps03pwb;

--
-- Name: checkauthtrigger(); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION checkauthtrigger() RETURNS "trigger"
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'check_authorization'
    LANGUAGE c;


ALTER FUNCTION public.checkauthtrigger() OWNER TO aps03pwb;

--
-- Name: collect(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION collect(geometry, geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_collect'
    LANGUAGE c IMMUTABLE;


ALTER FUNCTION public.collect(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: collect_garray(geometry[]); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION collect_garray(geometry[]) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_collect_garray'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.collect_garray(geometry[]) OWNER TO aps03pwb;

--
-- Name: collector(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION collector(geometry, geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_collect'
    LANGUAGE c IMMUTABLE;


ALTER FUNCTION public.collector(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: combine_bbox(box2d, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION combine_bbox(box2d, geometry) RETURNS box2d
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX2DFLOAT4_combine'
    LANGUAGE c IMMUTABLE;


ALTER FUNCTION public.combine_bbox(box2d, geometry) OWNER TO aps03pwb;

--
-- Name: combine_bbox(box3d, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION combine_bbox(box3d, geometry) RETURNS box3d
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX3D_combine'
    LANGUAGE c IMMUTABLE;


ALTER FUNCTION public.combine_bbox(box3d, geometry) OWNER TO aps03pwb;

--
-- Name: compression(chip); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION compression(chip) RETURNS integer
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'CHIP_getCompression'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.compression(chip) OWNER TO aps03pwb;

--
-- Name: contains(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION contains(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'contains'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.contains(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: convexhull(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION convexhull(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'convexhull'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.convexhull(geometry) OWNER TO aps03pwb;

--
-- Name: create_histogram2d(box2d, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION create_histogram2d(box2d, integer) RETURNS histogram2d
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'create_lwhistogram2d'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.create_histogram2d(box2d, integer) OWNER TO aps03pwb;

--
-- Name: crosses(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION crosses(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'crosses'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.crosses(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: datatype(chip); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION datatype(chip) RETURNS integer
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'CHIP_getDatatype'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.datatype(chip) OWNER TO aps03pwb;

--
-- Name: difference(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION difference(geometry, geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'difference'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.difference(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: dimension(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION dimension(geometry) RETURNS integer
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_dimension'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.dimension(geometry) OWNER TO aps03pwb;

--
-- Name: disablelongtransactions(); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION disablelongtransactions() RETURNS text
    AS $$
DECLARE
	query text;
	exists bool;
	rec RECORD;

BEGIN

	--
	-- Drop all triggers applied by CheckAuth()
	--
	FOR rec IN
		SELECT c.relname, t.tgname, t.tgargs FROM pg_trigger t, pg_class c, pg_proc p
		WHERE p.proname = 'checkauthtrigger' and t.tgfoid = p.oid and t.tgrelid = c.oid
	LOOP
		EXECUTE 'DROP TRIGGER ' || quote_ident(rec.tgname) ||
			' ON ' || quote_ident(rec.relname);
	END LOOP;

	--
	-- Drop the authorization_table table
	--
	FOR rec IN SELECT * FROM pg_class WHERE relname = 'authorization_table' LOOP
		DROP TABLE authorization_table;
	END LOOP;

	--
	-- Drop the authorized_tables view
	--
	FOR rec IN SELECT * FROM pg_class WHERE relname = 'authorized_tables' LOOP
		DROP VIEW authorized_tables;
	END LOOP;

	RETURN 'Long transactions support disabled';
END;
$$
    LANGUAGE plpgsql;


ALTER FUNCTION public.disablelongtransactions() OWNER TO aps03pwb;

--
-- Name: disjoint(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION disjoint(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'disjoint'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.disjoint(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: distance(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION distance(geometry, geometry) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_mindistance2d'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.distance(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: distance_sphere(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION distance_sphere(geometry, geometry) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_distance_sphere'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.distance_sphere(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: distance_spheroid(geometry, geometry, spheroid); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION distance_spheroid(geometry, geometry, spheroid) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_distance_ellipsoid_point'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.distance_spheroid(geometry, geometry, spheroid) OWNER TO aps03pwb;

--
-- Name: dropbbox(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION dropbbox(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_dropBBOX'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.dropbbox(geometry) OWNER TO aps03pwb;

--
-- Name: dropgeometrycolumn(character varying, character varying, character varying, character varying); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION dropgeometrycolumn(character varying, character varying, character varying, character varying) RETURNS text
    AS $_$
DECLARE
	catalog_name alias for $1; 
	schema_name alias for $2;
	table_name alias for $3;
	column_name alias for $4;
	myrec RECORD;
	okay boolean;
	real_schema name;

BEGIN


	-- Find, check or fix schema_name
	IF ( schema_name != '' ) THEN
		okay = 'f';

		FOR myrec IN SELECT nspname FROM pg_namespace WHERE text(nspname) = schema_name LOOP
			okay := 't';
		END LOOP;

		IF ( okay <> 't' ) THEN
			RAISE NOTICE 'Invalid schema name - using current_schema()';
			SELECT current_schema() into real_schema;
		ELSE
			real_schema = schema_name;
		END IF;
	ELSE
		SELECT current_schema() into real_schema;
	END IF;

 	-- Find out if the column is in the geometry_columns table
	okay = 'f';
	FOR myrec IN SELECT * from geometry_columns where f_table_schema = text(real_schema) and f_table_name = table_name and f_geometry_column = column_name LOOP
		okay := 't';
	END LOOP; 
	IF (okay <> 't') THEN 
		RAISE EXCEPTION 'column not found in geometry_columns table';
		RETURN 'f';
	END IF;

	-- Remove ref from geometry_columns table
	EXECUTE 'delete from geometry_columns where f_table_schema = ' ||
		quote_literal(real_schema) || ' and f_table_name = ' ||
		quote_literal(table_name)  || ' and f_geometry_column = ' ||
		quote_literal(column_name);
	
	-- Remove table column
	EXECUTE 'ALTER TABLE ' || quote_ident(real_schema) || '.' ||
		quote_ident(table_name) || ' DROP COLUMN ' ||
		quote_ident(column_name);


	RETURN real_schema || '.' || table_name || '.' || column_name ||' effectively removed.';
	
END;
$_$
    LANGUAGE plpgsql STRICT;


ALTER FUNCTION public.dropgeometrycolumn(character varying, character varying, character varying, character varying) OWNER TO aps03pwb;

--
-- Name: dropgeometrycolumn(character varying, character varying, character varying); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION dropgeometrycolumn(character varying, character varying, character varying) RETURNS text
    AS $_$
DECLARE
	ret text;
BEGIN
	SELECT DropGeometryColumn('',$1,$2,$3) into ret;
	RETURN ret;
END;
$_$
    LANGUAGE plpgsql STRICT;


ALTER FUNCTION public.dropgeometrycolumn(character varying, character varying, character varying) OWNER TO aps03pwb;

--
-- Name: dropgeometrycolumn(character varying, character varying); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION dropgeometrycolumn(character varying, character varying) RETURNS text
    AS $_$
DECLARE
	ret text;
BEGIN
	SELECT DropGeometryColumn('','',$1,$2) into ret;
	RETURN ret;
END;
$_$
    LANGUAGE plpgsql STRICT;


ALTER FUNCTION public.dropgeometrycolumn(character varying, character varying) OWNER TO aps03pwb;

--
-- Name: dropgeometrytable(character varying, character varying, character varying); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION dropgeometrytable(character varying, character varying, character varying) RETURNS text
    AS $_$
DECLARE
	catalog_name alias for $1; 
	schema_name alias for $2;
	table_name alias for $3;
	real_schema name;

BEGIN

	IF ( schema_name = '' ) THEN
		SELECT current_schema() into real_schema;
	ELSE
		real_schema = schema_name;
	END IF;

	-- Remove refs from geometry_columns table
	EXECUTE 'DELETE FROM geometry_columns WHERE ' ||
		'f_table_schema = ' || quote_literal(real_schema) ||
		' AND ' ||
		' f_table_name = ' || quote_literal(table_name);
	
	-- Remove table 
	EXECUTE 'DROP TABLE '
		|| quote_ident(real_schema) || '.' ||
		quote_ident(table_name);

	RETURN
		real_schema || '.' ||
		table_name ||' dropped.';
	
END;
$_$
    LANGUAGE plpgsql STRICT;


ALTER FUNCTION public.dropgeometrytable(character varying, character varying, character varying) OWNER TO aps03pwb;

--
-- Name: dropgeometrytable(character varying, character varying); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION dropgeometrytable(character varying, character varying) RETURNS text
    AS $_$SELECT DropGeometryTable('',$1,$2)$_$
    LANGUAGE sql STRICT;


ALTER FUNCTION public.dropgeometrytable(character varying, character varying) OWNER TO aps03pwb;

--
-- Name: dropgeometrytable(character varying); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION dropgeometrytable(character varying) RETURNS text
    AS $_$SELECT DropGeometryTable('','',$1)$_$
    LANGUAGE sql STRICT;


ALTER FUNCTION public.dropgeometrytable(character varying) OWNER TO aps03pwb;

--
-- Name: dump(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION dump(geometry) RETURNS SETOF geometry_dump
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_dump'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.dump(geometry) OWNER TO aps03pwb;

--
-- Name: dumprings(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION dumprings(geometry) RETURNS SETOF geometry_dump
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_dump_rings'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.dumprings(geometry) OWNER TO aps03pwb;

--
-- Name: enablelongtransactions(); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION enablelongtransactions() RETURNS text
    AS $$
DECLARE
	query text;
	exists bool;
	rec RECORD;

BEGIN

	exists = 'f';
	FOR rec IN SELECT * FROM pg_class WHERE relname = 'authorization_table'
	LOOP
		exists = 't';
	END LOOP;

	IF NOT exists
	THEN
		query = 'CREATE TABLE authorization_table (
			toid oid, -- table oid
			rid text, -- row id
			expires timestamp,
			authid text
		)';
		EXECUTE query;
	END IF;

	exists = 'f';
	FOR rec IN SELECT * FROM pg_class WHERE relname = 'authorized_tables'
	LOOP
		exists = 't';
	END LOOP;

	IF NOT exists THEN
		query = 'CREATE VIEW authorized_tables AS ' ||
			'SELECT ' ||
			'n.nspname as schema, ' ||
			'c.relname as table, trim(' ||
			quote_literal(chr(92) || '000') ||
			' from t.tgargs) as id_column ' ||
			'FROM pg_trigger t, pg_class c, pg_proc p ' ||
			', pg_namespace n ' ||
			'WHERE p.proname = ' || quote_literal('checkauthtrigger') ||
			' AND c.relnamespace = n.oid' ||
			' AND t.tgfoid = p.oid and t.tgrelid = c.oid';
		EXECUTE query;
	END IF;

	RETURN 'Long transactions support enabled';
END;
$$
    LANGUAGE plpgsql;


ALTER FUNCTION public.enablelongtransactions() OWNER TO aps03pwb;

--
-- Name: endpoint(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION endpoint(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_endpoint_linestring'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.endpoint(geometry) OWNER TO aps03pwb;

--
-- Name: envelope(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION envelope(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_envelope'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.envelope(geometry) OWNER TO aps03pwb;

--
-- Name: equals(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION equals(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'geomequals'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.equals(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: estimate_histogram2d(histogram2d, box2d); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION estimate_histogram2d(histogram2d, box2d) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'estimate_lwhistogram2d'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.estimate_histogram2d(histogram2d, box2d) OWNER TO aps03pwb;

--
-- Name: estimated_extent(text, text, text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION estimated_extent(text, text, text) RETURNS box2d
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_estimated_extent'
    LANGUAGE c IMMUTABLE STRICT SECURITY DEFINER;


ALTER FUNCTION public.estimated_extent(text, text, text) OWNER TO aps03pwb;

--
-- Name: estimated_extent(text, text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION estimated_extent(text, text) RETURNS box2d
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_estimated_extent'
    LANGUAGE c IMMUTABLE STRICT SECURITY DEFINER;


ALTER FUNCTION public.estimated_extent(text, text) OWNER TO aps03pwb;

--
-- Name: expand(box3d, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION expand(box3d, double precision) RETURNS box3d
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX3D_expand'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.expand(box3d, double precision) OWNER TO aps03pwb;

--
-- Name: expand(box2d, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION expand(box2d, double precision) RETURNS box2d
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX2DFLOAT4_expand'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.expand(box2d, double precision) OWNER TO aps03pwb;

--
-- Name: expand(geometry, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION expand(geometry, double precision) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_expand'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.expand(geometry, double precision) OWNER TO aps03pwb;

--
-- Name: explode_histogram2d(histogram2d, text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION explode_histogram2d(histogram2d, text) RETURNS histogram2d
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'explode_lwhistogram2d'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.explode_histogram2d(histogram2d, text) OWNER TO aps03pwb;

--
-- Name: exteriorring(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION exteriorring(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_exteriorring_polygon'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.exteriorring(geometry) OWNER TO aps03pwb;

--
-- Name: factor(chip); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION factor(chip) RETURNS real
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'CHIP_getFactor'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.factor(chip) OWNER TO aps03pwb;

--
-- Name: find_extent(text, text, text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION find_extent(text, text, text) RETURNS box2d
    AS $_$
DECLARE
	schemaname alias for $1;
	tablename alias for $2;
	columnname alias for $3;
	myrec RECORD;

BEGIN
	FOR myrec IN EXECUTE 'SELECT extent("'||columnname||'") FROM "'||schemaname||'"."'||tablename||'"' LOOP
		return myrec.extent;
	END LOOP; 
END;
$_$
    LANGUAGE plpgsql IMMUTABLE STRICT;


ALTER FUNCTION public.find_extent(text, text, text) OWNER TO aps03pwb;

--
-- Name: find_extent(text, text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION find_extent(text, text) RETURNS box2d
    AS $_$
DECLARE
	tablename alias for $1;
	columnname alias for $2;
	myrec RECORD;

BEGIN
	FOR myrec IN EXECUTE 'SELECT extent("'||columnname||'") FROM "'||tablename||'"' LOOP
		return myrec.extent;
	END LOOP; 
END;
$_$
    LANGUAGE plpgsql IMMUTABLE STRICT;


ALTER FUNCTION public.find_extent(text, text) OWNER TO aps03pwb;

--
-- Name: find_srid(character varying, character varying, character varying); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION find_srid(character varying, character varying, character varying) RETURNS integer
    AS $_$DECLARE
   schem text;
   tabl text;
   sr int4;
BEGIN
   IF $1 IS NULL THEN
      RAISE EXCEPTION 'find_srid() - schema is NULL!';
   END IF;
   IF $2 IS NULL THEN
      RAISE EXCEPTION 'find_srid() - table name is NULL!';
   END IF;
   IF $3 IS NULL THEN
      RAISE EXCEPTION 'find_srid() - column name is NULL!';
   END IF;
   schem = $1;
   tabl = $2;
-- if the table contains a . and the schema is empty
-- split the table into a schema and a table
-- otherwise drop through to default behavior
   IF ( schem = '' and tabl LIKE '%.%' ) THEN
     schem = substr(tabl,1,strpos(tabl,'.')-1);
     tabl = substr(tabl,length(schem)+2);
   ELSE
     schem = schem || '%';
   END IF;

   select SRID into sr from geometry_columns where f_table_schema like schem and f_table_name = tabl and f_geometry_column = $3;
   IF NOT FOUND THEN
       RAISE EXCEPTION 'find_srid() - couldnt find the corresponding SRID - is the geometry registered in the GEOMETRY_COLUMNS table?  Is there an uppercase/lowercase missmatch?';
   END IF;
  return sr;
END;
$_$
    LANGUAGE plpgsql IMMUTABLE STRICT;


ALTER FUNCTION public.find_srid(character varying, character varying, character varying) OWNER TO aps03pwb;

--
-- Name: fix_geometry_columns(); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION fix_geometry_columns() RETURNS text
    AS $$
DECLARE
	mislinked record;
	result text;
	linked integer;
	deleted integer;
	foundschema integer;
BEGIN

	-- Since 7.3 schema support has been added.
	-- Previous postgis versions used to put the database name in
	-- the schema column. This needs to be fixed, so we try to 
	-- set the correct schema for each geometry_colums record
	-- looking at table, column, type and srid.
	UPDATE geometry_columns SET f_table_schema = n.nspname
		FROM pg_namespace n, pg_class c, pg_attribute a,
			pg_constraint sridcheck, pg_constraint typecheck
                WHERE ( f_table_schema is NULL
		OR f_table_schema = ''
                OR f_table_schema NOT IN (
                        SELECT nspname::varchar
                        FROM pg_namespace nn, pg_class cc, pg_attribute aa
                        WHERE cc.relnamespace = nn.oid
                        AND cc.relname = f_table_name::name
                        AND aa.attrelid = cc.oid
                        AND aa.attname = f_geometry_column::name))
                AND f_table_name::name = c.relname
                AND c.oid = a.attrelid
                AND c.relnamespace = n.oid
                AND f_geometry_column::name = a.attname

                AND sridcheck.conrelid = c.oid
		AND sridcheck.consrc LIKE '(srid(% = %)'
                AND sridcheck.consrc ~ textcat(' = ', srid::text)

                AND typecheck.conrelid = c.oid
		AND typecheck.consrc LIKE
	'((geometrytype(%) = ''%''::text) OR (% IS NULL))'
                AND typecheck.consrc ~ textcat(' = ''', type::text)

                AND NOT EXISTS (
                        SELECT oid FROM geometry_columns gc
                        WHERE c.relname::varchar = gc.f_table_name
                        AND n.nspname::varchar = gc.f_table_schema
                        AND a.attname::varchar = gc.f_geometry_column
                );

	GET DIAGNOSTICS foundschema = ROW_COUNT;

	-- no linkage to system table needed
	return 'fixed:'||foundschema::text;

	-- fix linking to system tables
	SELECT 0 INTO linked;
	FOR mislinked in
		SELECT gc.oid as gcrec,
			a.attrelid as attrelid, a.attnum as attnum
                FROM geometry_columns gc, pg_class c,
		pg_namespace n, pg_attribute a
                WHERE ( gc.attrelid IS NULL OR gc.attrelid != a.attrelid 
			OR gc.varattnum IS NULL OR gc.varattnum != a.attnum)
                AND n.nspname = gc.f_table_schema::name
                AND c.relnamespace = n.oid
                AND c.relname = gc.f_table_name::name
                AND a.attname = f_geometry_column::name
                AND a.attrelid = c.oid
	LOOP
		UPDATE geometry_columns SET
			attrelid = mislinked.attrelid,
			varattnum = mislinked.attnum,
			stats = NULL
			WHERE geometry_columns.oid = mislinked.gcrec;
		SELECT linked+1 INTO linked;
	END LOOP; 

	-- remove stale records
	DELETE FROM geometry_columns WHERE attrelid IS NULL;

	GET DIAGNOSTICS deleted = ROW_COUNT;

	result = 
		'fixed:' || foundschema::text ||
		' linked:' || linked::text || 
		' deleted:' || deleted::text;

	return result;

END;
$$
    LANGUAGE plpgsql;


ALTER FUNCTION public.fix_geometry_columns() OWNER TO aps03pwb;

--
-- Name: force_2d(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION force_2d(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_force_2d'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.force_2d(geometry) OWNER TO aps03pwb;

--
-- Name: force_3d(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION force_3d(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_force_3dz'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.force_3d(geometry) OWNER TO aps03pwb;

--
-- Name: force_3dm(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION force_3dm(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_force_3dm'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.force_3dm(geometry) OWNER TO aps03pwb;

--
-- Name: force_3dz(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION force_3dz(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_force_3dz'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.force_3dz(geometry) OWNER TO aps03pwb;

--
-- Name: force_4d(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION force_4d(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_force_4d'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.force_4d(geometry) OWNER TO aps03pwb;

--
-- Name: force_collection(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION force_collection(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_force_collection'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.force_collection(geometry) OWNER TO aps03pwb;

--
-- Name: forcerhr(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION forcerhr(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_forceRHR_poly'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.forcerhr(geometry) OWNER TO aps03pwb;

--
-- Name: geom_accum(geometry[], geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geom_accum(geometry[], geometry) RETURNS geometry[]
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_accum'
    LANGUAGE c IMMUTABLE;


ALTER FUNCTION public.geom_accum(geometry[], geometry) OWNER TO aps03pwb;

--
-- Name: geomcollfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geomcollfromtext(text, integer) RETURNS geometry
    AS $_$
	SELECT CASE
	WHEN geometrytype(GeomFromText($1, $2)) = 'GEOMETRYCOLLECTION'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.geomcollfromtext(text, integer) OWNER TO aps03pwb;

--
-- Name: geomcollfromtext(text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geomcollfromtext(text) RETURNS geometry
    AS $_$
	SELECT CASE
	WHEN geometrytype(GeomFromText($1)) = 'GEOMETRYCOLLECTION'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.geomcollfromtext(text) OWNER TO aps03pwb;

--
-- Name: geomcollfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geomcollfromwkb(bytea, integer) RETURNS geometry
    AS $_$
	SELECT CASE
	WHEN geometrytype(GeomFromWKB($1, $2)) = 'GEOMETRYCOLLECTION'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.geomcollfromwkb(bytea, integer) OWNER TO aps03pwb;

--
-- Name: geomcollfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geomcollfromwkb(bytea) RETURNS geometry
    AS $_$
	SELECT CASE
	WHEN geometrytype(GeomFromWKB($1)) = 'GEOMETRYCOLLECTION'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.geomcollfromwkb(bytea) OWNER TO aps03pwb;

--
-- Name: geometry(box2d); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry(box2d) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX2DFLOAT4_to_LWGEOM'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry(box2d) OWNER TO aps03pwb;

--
-- Name: geometry(box3d); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry(box3d) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX3D_to_LWGEOM'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry(box3d) OWNER TO aps03pwb;

--
-- Name: geometry(text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry(text) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'parse_WKT_lwgeom'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry(text) OWNER TO aps03pwb;

--
-- Name: geometry(chip); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry(chip) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'CHIP_to_LWGEOM'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry(chip) OWNER TO aps03pwb;

--
-- Name: geometry(bytea); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry(bytea) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_from_bytea'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry(bytea) OWNER TO aps03pwb;

--
-- Name: geometry_above(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry_above(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_above'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry_above(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: geometry_below(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry_below(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_below'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry_below(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: geometry_cmp(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry_cmp(geometry, geometry) RETURNS integer
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'lwgeom_cmp'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry_cmp(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: geometry_contain(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry_contain(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_contain'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry_contain(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: geometry_contained(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry_contained(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_contained'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry_contained(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: geometry_eq(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry_eq(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'lwgeom_eq'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry_eq(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: geometry_ge(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry_ge(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'lwgeom_ge'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry_ge(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: geometry_gt(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry_gt(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'lwgeom_gt'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry_gt(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: geometry_le(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry_le(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'lwgeom_le'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry_le(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: geometry_left(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry_left(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_left'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry_left(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: geometry_lt(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry_lt(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'lwgeom_lt'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry_lt(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: geometry_overabove(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry_overabove(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_overabove'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry_overabove(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: geometry_overbelow(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry_overbelow(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_overbelow'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry_overbelow(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: geometry_overlap(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry_overlap(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_overlap'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry_overlap(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: geometry_overleft(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry_overleft(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_overleft'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry_overleft(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: geometry_overright(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry_overright(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_overright'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry_overright(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: geometry_right(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry_right(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_right'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry_right(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: geometry_same(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometry_same(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_same'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometry_same(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: geometryfromtext(text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometryfromtext(text) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_from_text'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometryfromtext(text) OWNER TO aps03pwb;

--
-- Name: geometryfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometryfromtext(text, integer) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_from_text'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometryfromtext(text, integer) OWNER TO aps03pwb;

--
-- Name: geometryn(geometry, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometryn(geometry, integer) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_geometryn_collection'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometryn(geometry, integer) OWNER TO aps03pwb;

--
-- Name: geometrytype(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geometrytype(geometry) RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_getTYPE'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geometrytype(geometry) OWNER TO aps03pwb;

--
-- Name: geomfromewkb(bytea); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geomfromewkb(bytea) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOMFromWKB'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geomfromewkb(bytea) OWNER TO aps03pwb;

--
-- Name: geomfromewkt(text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geomfromewkt(text) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'parse_WKT_lwgeom'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geomfromewkt(text) OWNER TO aps03pwb;

--
-- Name: geomfromtext(text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geomfromtext(text) RETURNS geometry
    AS $_$SELECT geometryfromtext($1)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.geomfromtext(text) OWNER TO aps03pwb;

--
-- Name: geomfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geomfromtext(text, integer) RETURNS geometry
    AS $_$SELECT geometryfromtext($1, $2)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.geomfromtext(text, integer) OWNER TO aps03pwb;

--
-- Name: geomfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geomfromwkb(bytea) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_from_WKB'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geomfromwkb(bytea) OWNER TO aps03pwb;

--
-- Name: geomfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geomfromwkb(bytea, integer) RETURNS geometry
    AS $_$SELECT setSRID(GeomFromWKB($1), $2)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.geomfromwkb(bytea, integer) OWNER TO aps03pwb;

--
-- Name: geomunion(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geomunion(geometry, geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'geomunion'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.geomunion(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: geosnoop(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION geosnoop(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'GEOSnoop'
    LANGUAGE c STRICT;


ALTER FUNCTION public.geosnoop(geometry) OWNER TO aps03pwb;

--
-- Name: get_proj4_from_srid(integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION get_proj4_from_srid(integer) RETURNS text
    AS $_$
BEGIN
	RETURN proj4text::text FROM spatial_ref_sys WHERE srid= $1;
END;
$_$
    LANGUAGE plpgsql IMMUTABLE STRICT;


ALTER FUNCTION public.get_proj4_from_srid(integer) OWNER TO aps03pwb;

--
-- Name: getbbox(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION getbbox(geometry) RETURNS box2d
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_to_BOX2DFLOAT4'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.getbbox(geometry) OWNER TO aps03pwb;

--
-- Name: getsrid(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION getsrid(geometry) RETURNS integer
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_getSRID'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.getsrid(geometry) OWNER TO aps03pwb;

--
-- Name: gettransactionid(); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION gettransactionid() RETURNS xid
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'getTransactionID'
    LANGUAGE c;


ALTER FUNCTION public.gettransactionid() OWNER TO aps03pwb;

--
-- Name: gpsdatacopy(character varying); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION gpsdatacopy(tablename character varying) RETURNS boolean
    AS $_$DECLARE
thetable alias for $1;
sqlstr character varying;

BEGIN

sqlstr := 'insert into tblgpsmaster (name, elevation, symbol, descriptio, source, url, the_geom) 
select name, elevation, symbol, descriptio, source, url, the_geom from ' || thetable;

execute sqlstr;

return true;
END;$_$
    LANGUAGE plpgsql;


ALTER FUNCTION public.gpsdatacopy(tablename character varying) OWNER TO aps03pwb;

--
-- Name: hasbbox(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION hasbbox(geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_hasBBOX'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.hasbbox(geometry) OWNER TO aps03pwb;

--
-- Name: height(chip); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION height(chip) RETURNS integer
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'CHIP_getHeight'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.height(chip) OWNER TO aps03pwb;

--
-- Name: importGPSRecords(character varying); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION "importGPSRecords"(tablename character varying) RETURNS boolean
    AS $$DECLARE

query character varying;

BEGIN
query := 'INSERT INTO tblgpsmaster (name, elevation, symbol, comment, descriptio, source, url, "url name", the_geom) SELECT name, elevation, symbol, comment, descriptio, source, url, "url name", the_geom from ' || tablename || ' where the_geom not in (select the_geom from tblgpsmaster)';

EXECUTE query;
RETURN true;

END;
$$
    LANGUAGE plpgsql;


ALTER FUNCTION public."importGPSRecords"(tablename character varying) OWNER TO aps03pwb;

--
-- Name: interiorringn(geometry, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION interiorringn(geometry, integer) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_interiorringn_polygon'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.interiorringn(geometry, integer) OWNER TO aps03pwb;

--
-- Name: intersection(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION intersection(geometry, geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'intersection'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.intersection(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: intersects(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION intersects(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'intersects'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.intersects(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: isclosed(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION isclosed(geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_isclosed_linestring'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.isclosed(geometry) OWNER TO aps03pwb;

--
-- Name: isempty(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION isempty(geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_isempty'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.isempty(geometry) OWNER TO aps03pwb;

--
-- Name: isring(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION isring(geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'isring'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.isring(geometry) OWNER TO aps03pwb;

--
-- Name: issimple(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION issimple(geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'issimple'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.issimple(geometry) OWNER TO aps03pwb;

--
-- Name: isvalid(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION isvalid(geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'isvalid'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.isvalid(geometry) OWNER TO aps03pwb;

--
-- Name: jtsnoop(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION jtsnoop(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'JTSnoop'
    LANGUAGE c STRICT;


ALTER FUNCTION public.jtsnoop(geometry) OWNER TO aps03pwb;

--
-- Name: length(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION length(geometry) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_length_linestring'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.length(geometry) OWNER TO aps03pwb;

--
-- Name: length2d(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION length2d(geometry) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_length2d_linestring'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.length2d(geometry) OWNER TO aps03pwb;

--
-- Name: length2d_spheroid(geometry, spheroid); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION length2d_spheroid(geometry, spheroid) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_length2d_ellipsoid_linestring'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.length2d_spheroid(geometry, spheroid) OWNER TO aps03pwb;

--
-- Name: length3d(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION length3d(geometry) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_length_linestring'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.length3d(geometry) OWNER TO aps03pwb;

--
-- Name: length3d_spheroid(geometry, spheroid); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION length3d_spheroid(geometry, spheroid) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_length_ellipsoid_linestring'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.length3d_spheroid(geometry, spheroid) OWNER TO aps03pwb;

--
-- Name: length_spheroid(geometry, spheroid); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION length_spheroid(geometry, spheroid) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_length_ellipsoid_linestring'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.length_spheroid(geometry, spheroid) OWNER TO aps03pwb;

--
-- Name: line_interpolate_point(geometry, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION line_interpolate_point(geometry, double precision) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_line_interpolate_point'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.line_interpolate_point(geometry, double precision) OWNER TO aps03pwb;

--
-- Name: line_locate_point(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION line_locate_point(geometry, geometry) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_line_locate_point'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.line_locate_point(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: line_substring(geometry, double precision, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION line_substring(geometry, double precision, double precision) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_line_substring'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.line_substring(geometry, double precision, double precision) OWNER TO aps03pwb;

--
-- Name: linefrommultipoint(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION linefrommultipoint(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_line_from_mpoint'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.linefrommultipoint(geometry) OWNER TO aps03pwb;

--
-- Name: linefromtext(text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION linefromtext(text) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1)) = 'LINESTRING'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.linefromtext(text) OWNER TO aps03pwb;

--
-- Name: linefromtext(text, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION linefromtext(text, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1, $2)) = 'LINESTRING'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.linefromtext(text, integer) OWNER TO aps03pwb;

--
-- Name: linefromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION linefromwkb(bytea, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'LINESTRING'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.linefromwkb(bytea, integer) OWNER TO aps03pwb;

--
-- Name: linefromwkb(bytea); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION linefromwkb(bytea) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'LINESTRING'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.linefromwkb(bytea) OWNER TO aps03pwb;

--
-- Name: linemerge(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION linemerge(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'linemerge'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.linemerge(geometry) OWNER TO aps03pwb;

--
-- Name: linestringfromtext(text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION linestringfromtext(text) RETURNS geometry
    AS $_$SELECT LineFromText($1)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.linestringfromtext(text) OWNER TO aps03pwb;

--
-- Name: linestringfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION linestringfromtext(text, integer) RETURNS geometry
    AS $_$SELECT LineFromText($1, $2)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.linestringfromtext(text, integer) OWNER TO aps03pwb;

--
-- Name: linestringfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION linestringfromwkb(bytea, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'LINESTRING'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.linestringfromwkb(bytea, integer) OWNER TO aps03pwb;

--
-- Name: linestringfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION linestringfromwkb(bytea) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'LINESTRING'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.linestringfromwkb(bytea) OWNER TO aps03pwb;

--
-- Name: locate_along_measure(geometry, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION locate_along_measure(geometry, double precision) RETURNS geometry
    AS $_$SELECT locate_between_measures($1, $2, $2)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.locate_along_measure(geometry, double precision) OWNER TO aps03pwb;

--
-- Name: locate_between_measures(geometry, double precision, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION locate_between_measures(geometry, double precision, double precision) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_locate_between_m'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.locate_between_measures(geometry, double precision, double precision) OWNER TO aps03pwb;

--
-- Name: lockrow(text, text, text, text, timestamp without time zone); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION lockrow(text, text, text, text, timestamp without time zone) RETURNS integer
    AS $_$
DECLARE
	myschema alias for $1;
	mytable alias for $2;
	myrid   alias for $3;
	authid alias for $4;
	expires alias for $5;
	ret int;
	mytoid oid;
	myrec RECORD;
	
BEGIN

	IF NOT LongTransactionsEnabled() THEN
		RAISE EXCEPTION 'Long transaction support disabled, use EnableLongTransaction() to enable.';
	END IF;

	EXECUTE 'DELETE FROM authorization_table WHERE expires < now()'; 

	SELECT c.oid INTO mytoid FROM pg_class c, pg_namespace n
		WHERE c.relname = mytable
		AND c.relnamespace = n.oid
		AND n.nspname = myschema;

	-- RAISE NOTICE 'toid: %', mytoid;

	FOR myrec IN SELECT * FROM authorization_table WHERE 
		toid = mytoid AND rid = myrid
	LOOP
		IF myrec.authid != authid THEN
			RETURN 0;
		ELSE
			RETURN 1;
		END IF;
	END LOOP;

	EXECUTE 'INSERT INTO authorization_table VALUES ('||
		quote_literal(mytoid)||','||quote_literal(myrid)||
		','||quote_literal(expires)||
		','||quote_literal(authid) ||')';

	GET DIAGNOSTICS ret = ROW_COUNT;

	RETURN ret;
END;$_$
    LANGUAGE plpgsql STRICT;


ALTER FUNCTION public.lockrow(text, text, text, text, timestamp without time zone) OWNER TO aps03pwb;

--
-- Name: lockrow(text, text, text, text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION lockrow(text, text, text, text) RETURNS integer
    AS $_$SELECT LockRow($1, $2, $3, $4, now()::timestamp+'1:00');$_$
    LANGUAGE sql STRICT;


ALTER FUNCTION public.lockrow(text, text, text, text) OWNER TO aps03pwb;

--
-- Name: lockrow(text, text, text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION lockrow(text, text, text) RETURNS integer
    AS $_$SELECT LockRow(current_schema(), $1, $2, $3, now()::timestamp+'1:00');$_$
    LANGUAGE sql STRICT;


ALTER FUNCTION public.lockrow(text, text, text) OWNER TO aps03pwb;

--
-- Name: lockrow(text, text, text, timestamp without time zone); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION lockrow(text, text, text, timestamp without time zone) RETURNS integer
    AS $_$SELECT LockRow(current_schema(), $1, $2, $3, $4);$_$
    LANGUAGE sql STRICT;


ALTER FUNCTION public.lockrow(text, text, text, timestamp without time zone) OWNER TO aps03pwb;

--
-- Name: longtransactionsenabled(); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION longtransactionsenabled() RETURNS boolean
    AS $$
DECLARE
	rec RECORD;
BEGIN
	FOR rec IN SELECT oid FROM pg_class WHERE relname = 'authorized_tables'
	LOOP
		return 't';
	END LOOP;
	return 'f';
END;
$$
    LANGUAGE plpgsql;


ALTER FUNCTION public.longtransactionsenabled() OWNER TO aps03pwb;

--
-- Name: lwgeom_gist_compress(internal); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION lwgeom_gist_compress(internal) RETURNS internal
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_gist_compress'
    LANGUAGE c;


ALTER FUNCTION public.lwgeom_gist_compress(internal) OWNER TO aps03pwb;

--
-- Name: lwgeom_gist_consistent(internal, geometry, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION lwgeom_gist_consistent(internal, geometry, integer) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_gist_consistent'
    LANGUAGE c;


ALTER FUNCTION public.lwgeom_gist_consistent(internal, geometry, integer) OWNER TO aps03pwb;

--
-- Name: lwgeom_gist_decompress(internal); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION lwgeom_gist_decompress(internal) RETURNS internal
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_gist_decompress'
    LANGUAGE c;


ALTER FUNCTION public.lwgeom_gist_decompress(internal) OWNER TO aps03pwb;

--
-- Name: lwgeom_gist_penalty(internal, internal, internal); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION lwgeom_gist_penalty(internal, internal, internal) RETURNS internal
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_gist_penalty'
    LANGUAGE c;


ALTER FUNCTION public.lwgeom_gist_penalty(internal, internal, internal) OWNER TO aps03pwb;

--
-- Name: lwgeom_gist_picksplit(internal, internal); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION lwgeom_gist_picksplit(internal, internal) RETURNS internal
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_gist_picksplit'
    LANGUAGE c;


ALTER FUNCTION public.lwgeom_gist_picksplit(internal, internal) OWNER TO aps03pwb;

--
-- Name: lwgeom_gist_same(box2d, box2d, internal); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION lwgeom_gist_same(box2d, box2d, internal) RETURNS internal
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_gist_same'
    LANGUAGE c;


ALTER FUNCTION public.lwgeom_gist_same(box2d, box2d, internal) OWNER TO aps03pwb;

--
-- Name: lwgeom_gist_union(bytea, internal); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION lwgeom_gist_union(bytea, internal) RETURNS internal
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_gist_union'
    LANGUAGE c;


ALTER FUNCTION public.lwgeom_gist_union(bytea, internal) OWNER TO aps03pwb;

--
-- Name: m(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION m(geometry) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_m_point'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.m(geometry) OWNER TO aps03pwb;

--
-- Name: makebox2d(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION makebox2d(geometry, geometry) RETURNS box2d
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX2DFLOAT4_construct'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.makebox2d(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: makebox3d(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION makebox3d(geometry, geometry) RETURNS box3d
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX3D_construct'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.makebox3d(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: makeline(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION makeline(geometry, geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_makeline'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.makeline(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: makeline_garray(geometry[]); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION makeline_garray(geometry[]) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_makeline_garray'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.makeline_garray(geometry[]) OWNER TO aps03pwb;

--
-- Name: makepoint(double precision, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION makepoint(double precision, double precision) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_makepoint'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.makepoint(double precision, double precision) OWNER TO aps03pwb;

--
-- Name: makepoint(double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION makepoint(double precision, double precision, double precision) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_makepoint'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.makepoint(double precision, double precision, double precision) OWNER TO aps03pwb;

--
-- Name: makepoint(double precision, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION makepoint(double precision, double precision, double precision, double precision) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_makepoint'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.makepoint(double precision, double precision, double precision, double precision) OWNER TO aps03pwb;

--
-- Name: makepointm(double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION makepointm(double precision, double precision, double precision) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_makepoint3dm'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.makepointm(double precision, double precision, double precision) OWNER TO aps03pwb;

--
-- Name: makepolygon(geometry, geometry[]); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION makepolygon(geometry, geometry[]) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_makepoly'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.makepolygon(geometry, geometry[]) OWNER TO aps03pwb;

--
-- Name: makepolygon(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION makepolygon(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_makepoly'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.makepolygon(geometry) OWNER TO aps03pwb;

--
-- Name: max_distance(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION max_distance(geometry, geometry) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_maxdistance2d_linestring'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.max_distance(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: mem_size(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION mem_size(geometry) RETURNS integer
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_mem_size'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.mem_size(geometry) OWNER TO aps03pwb;

--
-- Name: mlinefromtext(text, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION mlinefromtext(text, integer) RETURNS geometry
    AS $_$
	SELECT CASE
	WHEN geometrytype(GeomFromText($1, $2)) = 'MULTILINESTRING'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.mlinefromtext(text, integer) OWNER TO aps03pwb;

--
-- Name: mlinefromtext(text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION mlinefromtext(text) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1)) = 'MULTILINESTRING'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.mlinefromtext(text) OWNER TO aps03pwb;

--
-- Name: mlinefromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION mlinefromwkb(bytea, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'MULTILINESTRING'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.mlinefromwkb(bytea, integer) OWNER TO aps03pwb;

--
-- Name: mlinefromwkb(bytea); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION mlinefromwkb(bytea) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'MULTILINESTRING'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.mlinefromwkb(bytea) OWNER TO aps03pwb;

--
-- Name: mpointfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION mpointfromtext(text, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1,$2)) = 'MULTIPOINT'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.mpointfromtext(text, integer) OWNER TO aps03pwb;

--
-- Name: mpointfromtext(text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION mpointfromtext(text) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1)) = 'MULTIPOINT'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.mpointfromtext(text) OWNER TO aps03pwb;

--
-- Name: mpointfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION mpointfromwkb(bytea, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1,$2)) = 'MULTIPOINT'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.mpointfromwkb(bytea, integer) OWNER TO aps03pwb;

--
-- Name: mpointfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION mpointfromwkb(bytea) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'MULTIPOINT'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.mpointfromwkb(bytea) OWNER TO aps03pwb;

--
-- Name: mpolyfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION mpolyfromtext(text, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1, $2)) = 'MULTIPOLYGON'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.mpolyfromtext(text, integer) OWNER TO aps03pwb;

--
-- Name: mpolyfromtext(text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION mpolyfromtext(text) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1)) = 'MULTIPOLYGON'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.mpolyfromtext(text) OWNER TO aps03pwb;

--
-- Name: mpolyfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION mpolyfromwkb(bytea, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'MULTIPOLYGON'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.mpolyfromwkb(bytea, integer) OWNER TO aps03pwb;

--
-- Name: mpolyfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION mpolyfromwkb(bytea) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'MULTIPOLYGON'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.mpolyfromwkb(bytea) OWNER TO aps03pwb;

--
-- Name: multi(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION multi(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_force_multi'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.multi(geometry) OWNER TO aps03pwb;

--
-- Name: multilinefromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION multilinefromwkb(bytea, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'MULTILINESTRING'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.multilinefromwkb(bytea, integer) OWNER TO aps03pwb;

--
-- Name: multilinefromwkb(bytea); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION multilinefromwkb(bytea) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'MULTILINESTRING'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.multilinefromwkb(bytea) OWNER TO aps03pwb;

--
-- Name: multilinestringfromtext(text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION multilinestringfromtext(text) RETURNS geometry
    AS $_$SELECT MLineFromText($1)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.multilinestringfromtext(text) OWNER TO aps03pwb;

--
-- Name: multilinestringfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION multilinestringfromtext(text, integer) RETURNS geometry
    AS $_$SELECT MLineFromText($1, $2)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.multilinestringfromtext(text, integer) OWNER TO aps03pwb;

--
-- Name: multipointfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION multipointfromtext(text, integer) RETURNS geometry
    AS $_$SELECT MPointFromText($1, $2)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.multipointfromtext(text, integer) OWNER TO aps03pwb;

--
-- Name: multipointfromtext(text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION multipointfromtext(text) RETURNS geometry
    AS $_$SELECT MPointFromText($1)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.multipointfromtext(text) OWNER TO aps03pwb;

--
-- Name: multipointfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION multipointfromwkb(bytea, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1,$2)) = 'MULTIPOINT'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.multipointfromwkb(bytea, integer) OWNER TO aps03pwb;

--
-- Name: multipointfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION multipointfromwkb(bytea) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'MULTIPOINT'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.multipointfromwkb(bytea) OWNER TO aps03pwb;

--
-- Name: multipolyfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION multipolyfromwkb(bytea, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'MULTIPOLYGON'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.multipolyfromwkb(bytea, integer) OWNER TO aps03pwb;

--
-- Name: multipolyfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION multipolyfromwkb(bytea) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'MULTIPOLYGON'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.multipolyfromwkb(bytea) OWNER TO aps03pwb;

--
-- Name: multipolygonfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION multipolygonfromtext(text, integer) RETURNS geometry
    AS $_$SELECT MPolyFromText($1, $2)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.multipolygonfromtext(text, integer) OWNER TO aps03pwb;

--
-- Name: multipolygonfromtext(text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION multipolygonfromtext(text) RETURNS geometry
    AS $_$SELECT MPolyFromText($1)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.multipolygonfromtext(text) OWNER TO aps03pwb;

--
-- Name: ndims(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION ndims(geometry) RETURNS smallint
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_ndims'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.ndims(geometry) OWNER TO aps03pwb;

--
-- Name: noop(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION noop(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_noop'
    LANGUAGE c STRICT;


ALTER FUNCTION public.noop(geometry) OWNER TO aps03pwb;

--
-- Name: npoints(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION npoints(geometry) RETURNS integer
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_npoints'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.npoints(geometry) OWNER TO aps03pwb;

--
-- Name: nrings(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION nrings(geometry) RETURNS integer
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_nrings'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.nrings(geometry) OWNER TO aps03pwb;

--
-- Name: numgeometries(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION numgeometries(geometry) RETURNS integer
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_numgeometries_collection'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.numgeometries(geometry) OWNER TO aps03pwb;

--
-- Name: numinteriorring(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION numinteriorring(geometry) RETURNS integer
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_numinteriorrings_polygon'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.numinteriorring(geometry) OWNER TO aps03pwb;

--
-- Name: numinteriorrings(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION numinteriorrings(geometry) RETURNS integer
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_numinteriorrings_polygon'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.numinteriorrings(geometry) OWNER TO aps03pwb;

--
-- Name: numpoints(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION numpoints(geometry) RETURNS integer
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_numpoints_linestring'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.numpoints(geometry) OWNER TO aps03pwb;

--
-- Name: overlaps(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION "overlaps"(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'overlaps'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public."overlaps"(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: perimeter(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION perimeter(geometry) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_perimeter_poly'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.perimeter(geometry) OWNER TO aps03pwb;

--
-- Name: perimeter2d(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION perimeter2d(geometry) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_perimeter2d_poly'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.perimeter2d(geometry) OWNER TO aps03pwb;

--
-- Name: perimeter3d(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION perimeter3d(geometry) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_perimeter_poly'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.perimeter3d(geometry) OWNER TO aps03pwb;

--
-- Name: point_inside_circle(geometry, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION point_inside_circle(geometry, double precision, double precision, double precision) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_inside_circle_point'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.point_inside_circle(geometry, double precision, double precision, double precision) OWNER TO aps03pwb;

--
-- Name: pointfromtext(text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION pointfromtext(text) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1)) = 'POINT'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.pointfromtext(text) OWNER TO aps03pwb;

--
-- Name: pointfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION pointfromtext(text, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1, $2)) = 'POINT'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.pointfromtext(text, integer) OWNER TO aps03pwb;

--
-- Name: pointfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION pointfromwkb(bytea, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'POINT'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.pointfromwkb(bytea, integer) OWNER TO aps03pwb;

--
-- Name: pointfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION pointfromwkb(bytea) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'POINT'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.pointfromwkb(bytea) OWNER TO aps03pwb;

--
-- Name: pointn(geometry, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION pointn(geometry, integer) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_pointn_linestring'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.pointn(geometry, integer) OWNER TO aps03pwb;

--
-- Name: pointonsurface(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION pointonsurface(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'pointonsurface'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.pointonsurface(geometry) OWNER TO aps03pwb;

--
-- Name: polyfromtext(text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION polyfromtext(text) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1)) = 'POLYGON'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.polyfromtext(text) OWNER TO aps03pwb;

--
-- Name: polyfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION polyfromtext(text, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1, $2)) = 'POLYGON'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.polyfromtext(text, integer) OWNER TO aps03pwb;

--
-- Name: polyfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION polyfromwkb(bytea, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'POLYGON'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.polyfromwkb(bytea, integer) OWNER TO aps03pwb;

--
-- Name: polyfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION polyfromwkb(bytea) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'POLYGON'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.polyfromwkb(bytea) OWNER TO aps03pwb;

--
-- Name: polygonfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION polygonfromtext(text, integer) RETURNS geometry
    AS $_$SELECT PolyFromText($1, $2)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.polygonfromtext(text, integer) OWNER TO aps03pwb;

--
-- Name: polygonfromtext(text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION polygonfromtext(text) RETURNS geometry
    AS $_$SELECT PolyFromText($1)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.polygonfromtext(text) OWNER TO aps03pwb;

--
-- Name: polygonfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION polygonfromwkb(bytea, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1,$2)) = 'POLYGON'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.polygonfromwkb(bytea, integer) OWNER TO aps03pwb;

--
-- Name: polygonfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION polygonfromwkb(bytea) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'POLYGON'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.polygonfromwkb(bytea) OWNER TO aps03pwb;

--
-- Name: polygonize_garray(geometry[]); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION polygonize_garray(geometry[]) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'polygonize_garray'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.polygonize_garray(geometry[]) OWNER TO aps03pwb;

--
-- Name: postgis_full_version(); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION postgis_full_version() RETURNS text
    AS $$
DECLARE
	libver text;
	projver text;
	geosver text;
	jtsver text;
	usestats bool;
	dbproc text;
	relproc text;
	fullver text;
BEGIN
	SELECT postgis_lib_version() INTO libver;
	SELECT postgis_proj_version() INTO projver;
	SELECT postgis_geos_version() INTO geosver;
	SELECT postgis_jts_version() INTO jtsver;
	SELECT postgis_uses_stats() INTO usestats;
	SELECT postgis_scripts_installed() INTO dbproc;
	SELECT postgis_scripts_released() INTO relproc;

	fullver = 'POSTGIS="' || libver || '"';

	IF  geosver IS NOT NULL THEN
		fullver = fullver || ' GEOS="' || geosver || '"';
	END IF;

	IF  jtsver IS NOT NULL THEN
		fullver = fullver || ' JTS="' || jtsver || '"';
	END IF;

	IF  projver IS NOT NULL THEN
		fullver = fullver || ' PROJ="' || projver || '"';
	END IF;

	IF usestats THEN
		fullver = fullver || ' USE_STATS';
	END IF;

	-- fullver = fullver || ' DBPROC="' || dbproc || '"';
	-- fullver = fullver || ' RELPROC="' || relproc || '"';

	IF dbproc != relproc THEN
		fullver = fullver || ' (procs from ' || dbproc || ' need upgrade)';
	END IF;

	RETURN fullver;
END
$$
    LANGUAGE plpgsql IMMUTABLE;


ALTER FUNCTION public.postgis_full_version() OWNER TO aps03pwb;

--
-- Name: postgis_geos_version(); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION postgis_geos_version() RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'postgis_geos_version'
    LANGUAGE c IMMUTABLE;


ALTER FUNCTION public.postgis_geos_version() OWNER TO aps03pwb;

--
-- Name: postgis_gist_joinsel(internal, oid, internal, smallint); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION postgis_gist_joinsel(internal, oid, internal, smallint) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_gist_joinsel'
    LANGUAGE c;


ALTER FUNCTION public.postgis_gist_joinsel(internal, oid, internal, smallint) OWNER TO aps03pwb;

--
-- Name: postgis_gist_sel(internal, oid, internal, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION postgis_gist_sel(internal, oid, internal, integer) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_gist_sel'
    LANGUAGE c;


ALTER FUNCTION public.postgis_gist_sel(internal, oid, internal, integer) OWNER TO aps03pwb;

--
-- Name: postgis_jts_version(); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION postgis_jts_version() RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'postgis_jts_version'
    LANGUAGE c IMMUTABLE;


ALTER FUNCTION public.postgis_jts_version() OWNER TO aps03pwb;

--
-- Name: postgis_lib_build_date(); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION postgis_lib_build_date() RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'postgis_lib_build_date'
    LANGUAGE c IMMUTABLE;


ALTER FUNCTION public.postgis_lib_build_date() OWNER TO aps03pwb;

--
-- Name: postgis_lib_version(); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION postgis_lib_version() RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'postgis_lib_version'
    LANGUAGE c IMMUTABLE;


ALTER FUNCTION public.postgis_lib_version() OWNER TO aps03pwb;

--
-- Name: postgis_proj_version(); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION postgis_proj_version() RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'postgis_proj_version'
    LANGUAGE c IMMUTABLE;


ALTER FUNCTION public.postgis_proj_version() OWNER TO aps03pwb;

--
-- Name: postgis_scripts_build_date(); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION postgis_scripts_build_date() RETURNS text
    AS $$SELECT '2007-06-17 15:13:51'::text AS version$$
    LANGUAGE sql IMMUTABLE;


ALTER FUNCTION public.postgis_scripts_build_date() OWNER TO aps03pwb;

--
-- Name: postgis_scripts_installed(); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION postgis_scripts_installed() RETURNS text
    AS $$SELECT '1.2.1'::text AS version$$
    LANGUAGE sql IMMUTABLE;


ALTER FUNCTION public.postgis_scripts_installed() OWNER TO aps03pwb;

--
-- Name: postgis_scripts_released(); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION postgis_scripts_released() RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'postgis_scripts_released'
    LANGUAGE c IMMUTABLE;


ALTER FUNCTION public.postgis_scripts_released() OWNER TO aps03pwb;

--
-- Name: postgis_uses_stats(); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION postgis_uses_stats() RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'postgis_uses_stats'
    LANGUAGE c IMMUTABLE;


ALTER FUNCTION public.postgis_uses_stats() OWNER TO aps03pwb;

--
-- Name: postgis_version(); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION postgis_version() RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'postgis_version'
    LANGUAGE c IMMUTABLE;


ALTER FUNCTION public.postgis_version() OWNER TO aps03pwb;

--
-- Name: probe_geometry_columns(); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION probe_geometry_columns() RETURNS text
    AS $$
DECLARE
	inserted integer;
	oldcount integer;
	probed integer;
	stale integer;
BEGIN

	SELECT count(*) INTO oldcount FROM geometry_columns;

	SELECT count(*) INTO probed
		FROM pg_class c, pg_attribute a, pg_type t, 
			pg_namespace n,
			pg_constraint sridcheck, pg_constraint typecheck

		WHERE t.typname = 'geometry'
		AND a.atttypid = t.oid
		AND a.attrelid = c.oid
		AND c.relnamespace = n.oid
		AND sridcheck.connamespace = n.oid
		AND typecheck.connamespace = n.oid

		AND sridcheck.conrelid = c.oid
		AND sridcheck.consrc LIKE '(srid('||a.attname||') = %)'
		AND typecheck.conrelid = c.oid
		AND typecheck.consrc LIKE
	'((geometrytype('||a.attname||') = ''%''::text) OR (% IS NULL))'
		;

	INSERT INTO geometry_columns SELECT
		''::varchar as f_table_catalogue,
		n.nspname::varchar as f_table_schema,
		c.relname::varchar as f_table_name,
		a.attname::varchar as f_geometry_column,
		2 as coord_dimension,
		trim(both  ' =)' from substr(sridcheck.consrc,
			strpos(sridcheck.consrc, '=')))::integer as srid,
		trim(both ' =)''' from substr(typecheck.consrc, 
			strpos(typecheck.consrc, '='),
			strpos(typecheck.consrc, '::')-
			strpos(typecheck.consrc, '=')
			))::varchar as type

		FROM pg_class c, pg_attribute a, pg_type t, 
			pg_namespace n,
			pg_constraint sridcheck, pg_constraint typecheck
		WHERE t.typname = 'geometry'
		AND a.atttypid = t.oid
		AND a.attrelid = c.oid
		AND c.relnamespace = n.oid
		AND sridcheck.connamespace = n.oid
		AND typecheck.connamespace = n.oid
		AND sridcheck.conrelid = c.oid
		AND sridcheck.consrc LIKE '(srid('||a.attname||') = %)'
		AND typecheck.conrelid = c.oid
		AND typecheck.consrc LIKE
	'((geometrytype('||a.attname||') = ''%''::text) OR (% IS NULL))'

                AND NOT EXISTS (
                        SELECT oid FROM geometry_columns gc
                        WHERE c.relname::varchar = gc.f_table_name
                        AND n.nspname::varchar = gc.f_table_schema
                        AND a.attname::varchar = gc.f_geometry_column
                );

	GET DIAGNOSTICS inserted = ROW_COUNT;

	IF oldcount > probed THEN
		stale = oldcount-probed;
	ELSE
		stale = 0;
	END IF;

        RETURN 'probed:'||probed||
		' inserted:'||inserted||
		' conflicts:'||probed-inserted||
		' stale:'||stale;
END

$$
    LANGUAGE plpgsql;


ALTER FUNCTION public.probe_geometry_columns() OWNER TO aps03pwb;

--
-- Name: relate(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION relate(geometry, geometry) RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'relate_full'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.relate(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: relate(geometry, geometry, text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION relate(geometry, geometry, text) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'relate_pattern'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.relate(geometry, geometry, text) OWNER TO aps03pwb;

--
-- Name: removepoint(geometry, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION removepoint(geometry, integer) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_removepoint'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.removepoint(geometry, integer) OWNER TO aps03pwb;

--
-- Name: rename_geometry_table_constraints(); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION rename_geometry_table_constraints() RETURNS text
    AS $$
SELECT 'rename_geometry_table_constraint() is obsoleted'::text
$$
    LANGUAGE sql IMMUTABLE;


ALTER FUNCTION public.rename_geometry_table_constraints() OWNER TO aps03pwb;

--
-- Name: reverse(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION reverse(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_reverse'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.reverse(geometry) OWNER TO aps03pwb;

--
-- Name: rotate(geometry, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION rotate(geometry, double precision) RETURNS geometry
    AS $_$SELECT rotateZ($1, $2)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.rotate(geometry, double precision) OWNER TO aps03pwb;

--
-- Name: rotatex(geometry, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION rotatex(geometry, double precision) RETURNS geometry
    AS $_$SELECT affine($1, 1, 0, 0, 0, cos($2), -sin($2), 0, sin($2), cos($2), 0, 0, 0)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.rotatex(geometry, double precision) OWNER TO aps03pwb;

--
-- Name: rotatey(geometry, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION rotatey(geometry, double precision) RETURNS geometry
    AS $_$SELECT affine($1,  cos($2), 0, sin($2),  0, 1, 0,  -sin($2), 0, cos($2), 0,  0, 0)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.rotatey(geometry, double precision) OWNER TO aps03pwb;

--
-- Name: rotatez(geometry, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION rotatez(geometry, double precision) RETURNS geometry
    AS $_$SELECT affine($1,  cos($2), -sin($2), 0,  sin($2), cos($2), 0,  0, 0, 1,  0, 0, 0)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.rotatez(geometry, double precision) OWNER TO aps03pwb;

--
-- Name: scale(geometry, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION scale(geometry, double precision, double precision, double precision) RETURNS geometry
    AS $_$SELECT affine($1,  $2, 0, 0,  0, $3, 0,  0, 0, $4,  0, 0, 0)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.scale(geometry, double precision, double precision, double precision) OWNER TO aps03pwb;

--
-- Name: scale(geometry, double precision, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION scale(geometry, double precision, double precision) RETURNS geometry
    AS $_$SELECT scale($1, $2, $3, 1)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.scale(geometry, double precision, double precision) OWNER TO aps03pwb;

--
-- Name: se_envelopesintersect(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION se_envelopesintersect(geometry, geometry) RETURNS boolean
    AS $_$
	SELECT $1 && $2
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.se_envelopesintersect(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: se_is3d(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION se_is3d(geometry) RETURNS boolean
    AS $_$
    SELECT CASE zmflag($1)
               WHEN 0 THEN false
               WHEN 1 THEN false
               WHEN 2 THEN true
               WHEN 3 THEN true
               ELSE false
           END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.se_is3d(geometry) OWNER TO aps03pwb;

--
-- Name: se_ismeasured(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION se_ismeasured(geometry) RETURNS boolean
    AS $_$
    SELECT CASE zmflag($1)
               WHEN 0 THEN false
               WHEN 1 THEN true
               WHEN 2 THEN false
               WHEN 3 THEN true
               ELSE false
           END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.se_ismeasured(geometry) OWNER TO aps03pwb;

--
-- Name: se_locatealong(geometry, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION se_locatealong(geometry, double precision) RETURNS geometry
    AS $_$SELECT locate_between_measures($1, $2, $2)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.se_locatealong(geometry, double precision) OWNER TO aps03pwb;

--
-- Name: se_locatebetween(geometry, double precision, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION se_locatebetween(geometry, double precision, double precision) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_locate_between_m'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.se_locatebetween(geometry, double precision, double precision) OWNER TO aps03pwb;

--
-- Name: se_m(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION se_m(geometry) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_m_point'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.se_m(geometry) OWNER TO aps03pwb;

--
-- Name: se_z(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION se_z(geometry) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_z_point'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.se_z(geometry) OWNER TO aps03pwb;

--
-- Name: segmentize(geometry, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION segmentize(geometry, double precision) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_segmentize2d'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.segmentize(geometry, double precision) OWNER TO aps03pwb;

--
-- Name: setfactor(chip, real); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION setfactor(chip, real) RETURNS chip
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'CHIP_setFactor'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.setfactor(chip, real) OWNER TO aps03pwb;

--
-- Name: setpoint(geometry, integer, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION setpoint(geometry, integer, geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_setpoint_linestring'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.setpoint(geometry, integer, geometry) OWNER TO aps03pwb;

--
-- Name: setsrid(chip, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION setsrid(chip, integer) RETURNS chip
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'CHIP_setSRID'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.setsrid(chip, integer) OWNER TO aps03pwb;

--
-- Name: setsrid(geometry, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION setsrid(geometry, integer) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_setSRID'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.setsrid(geometry, integer) OWNER TO aps03pwb;

--
-- Name: shift_longitude(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION shift_longitude(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_longitude_shift'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.shift_longitude(geometry) OWNER TO aps03pwb;

--
-- Name: simplify(geometry, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION simplify(geometry, double precision) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_simplify2d'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.simplify(geometry, double precision) OWNER TO aps03pwb;

--
-- Name: snaptogrid(geometry, double precision, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION snaptogrid(geometry, double precision, double precision, double precision, double precision) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_snaptogrid'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.snaptogrid(geometry, double precision, double precision, double precision, double precision) OWNER TO aps03pwb;

--
-- Name: snaptogrid(geometry, double precision, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION snaptogrid(geometry, double precision, double precision) RETURNS geometry
    AS $_$SELECT SnapToGrid($1, 0, 0, $2, $3)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.snaptogrid(geometry, double precision, double precision) OWNER TO aps03pwb;

--
-- Name: snaptogrid(geometry, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION snaptogrid(geometry, double precision) RETURNS geometry
    AS $_$SELECT SnapToGrid($1, 0, 0, $2, $2)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.snaptogrid(geometry, double precision) OWNER TO aps03pwb;

--
-- Name: snaptogrid(geometry, geometry, double precision, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION snaptogrid(geometry, geometry, double precision, double precision, double precision, double precision) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_snaptogrid_pointoff'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.snaptogrid(geometry, geometry, double precision, double precision, double precision, double precision) OWNER TO aps03pwb;

--
-- Name: srid(chip); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION srid(chip) RETURNS integer
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'CHIP_getSRID'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.srid(chip) OWNER TO aps03pwb;

--
-- Name: srid(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION srid(geometry) RETURNS integer
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_getSRID'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.srid(geometry) OWNER TO aps03pwb;

--
-- Name: st_area(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_area(geometry) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_area_polygon'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_area(geometry) OWNER TO aps03pwb;

--
-- Name: st_asbinary(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_asbinary(geometry) RETURNS bytea
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_asBinary'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_asbinary(geometry) OWNER TO aps03pwb;

--
-- Name: st_astext(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_astext(geometry) RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_asText'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_astext(geometry) OWNER TO aps03pwb;

--
-- Name: st_boundary(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_boundary(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'boundary'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_boundary(geometry) OWNER TO aps03pwb;

--
-- Name: st_buffer(geometry, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_buffer(geometry, double precision) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'buffer'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_buffer(geometry, double precision) OWNER TO aps03pwb;

--
-- Name: st_centroid(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_centroid(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'centroid'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_centroid(geometry) OWNER TO aps03pwb;

--
-- Name: st_contains(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_contains(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'contains'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_contains(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: st_convexhull(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_convexhull(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'convexhull'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_convexhull(geometry) OWNER TO aps03pwb;

--
-- Name: st_coorddim(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_coorddim(geometry) RETURNS smallint
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_ndims'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_coorddim(geometry) OWNER TO aps03pwb;

--
-- Name: st_crosses(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_crosses(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'crosses'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_crosses(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: st_difference(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_difference(geometry, geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'difference'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_difference(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: st_dimension(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_dimension(geometry) RETURNS integer
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_dimension'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_dimension(geometry) OWNER TO aps03pwb;

--
-- Name: st_disjoint(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_disjoint(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'disjoint'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_disjoint(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: st_distance(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_distance(geometry, geometry) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_mindistance2d'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_distance(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: st_endpoint(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_endpoint(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_endpoint_linestring'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_endpoint(geometry) OWNER TO aps03pwb;

--
-- Name: st_envelope(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_envelope(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_envelope'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_envelope(geometry) OWNER TO aps03pwb;

--
-- Name: st_equals(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_equals(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'geomequals'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_equals(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: st_exteriorring(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_exteriorring(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_exteriorring_polygon'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_exteriorring(geometry) OWNER TO aps03pwb;

--
-- Name: st_geometryn(geometry, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_geometryn(geometry, integer) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_geometryn_collection'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_geometryn(geometry, integer) OWNER TO aps03pwb;

--
-- Name: st_geometrytype(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_geometrytype(geometry) RETURNS text
    AS $_$
    DECLARE
        gtype text := geometrytype($1);
    BEGIN
        IF (gtype IN ('POINT', 'POINTM')) THEN
            gtype := 'Point';
        ELSIF (gtype IN ('LINESTRING', 'LINESTRINGM')) THEN
            gtype := 'LineString';
        ELSIF (gtype IN ('POLYGON', 'POLYGONM')) THEN
            gtype := 'Polygon';
        ELSIF (gtype IN ('MULTIPOINT', 'MULTIPOINTM')) THEN
            gtype := 'MultiPoint';
        ELSIF (gtype IN ('MULTILINESTRING', 'MULTILINESTRINGM')) THEN
            gtype := 'MultiLineString';
        ELSIF (gtype IN ('MULTIPOLYGON', 'MULTIPOLYGONM')) THEN
            gtype := 'MultiPolygon';
        ELSE
            gtype := 'Geometry';
        END IF;
        RETURN 'ST_' || gtype;
    END
	$_$
    LANGUAGE plpgsql IMMUTABLE STRICT;


ALTER FUNCTION public.st_geometrytype(geometry) OWNER TO aps03pwb;

--
-- Name: st_geomfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_geomfromtext(text, integer) RETURNS geometry
    AS $_$SELECT geometryfromtext($1, $2)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.st_geomfromtext(text, integer) OWNER TO aps03pwb;

--
-- Name: st_geomfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_geomfromwkb(bytea, integer) RETURNS geometry
    AS $_$SELECT setSRID(GeomFromWKB($1), $2)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.st_geomfromwkb(bytea, integer) OWNER TO aps03pwb;

--
-- Name: st_interiorringn(geometry, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_interiorringn(geometry, integer) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_interiorringn_polygon'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_interiorringn(geometry, integer) OWNER TO aps03pwb;

--
-- Name: st_intersection(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_intersection(geometry, geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'intersection'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_intersection(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: st_intersects(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_intersects(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'intersects'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_intersects(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: st_isclosed(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_isclosed(geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_isclosed_linestring'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_isclosed(geometry) OWNER TO aps03pwb;

--
-- Name: st_isempty(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_isempty(geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_isempty'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_isempty(geometry) OWNER TO aps03pwb;

--
-- Name: st_isring(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_isring(geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'isring'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_isring(geometry) OWNER TO aps03pwb;

--
-- Name: st_issimple(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_issimple(geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'issimple'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_issimple(geometry) OWNER TO aps03pwb;

--
-- Name: st_isvalid(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_isvalid(geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'isvalid'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_isvalid(geometry) OWNER TO aps03pwb;

--
-- Name: st_length(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_length(geometry) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_length2d_linestring'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_length(geometry) OWNER TO aps03pwb;

--
-- Name: st_linefromtext(text, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_linefromtext(text, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1, $2)) = 'LINESTRING'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.st_linefromtext(text, integer) OWNER TO aps03pwb;

--
-- Name: st_linefromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_linefromwkb(bytea, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'LINESTRING'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.st_linefromwkb(bytea, integer) OWNER TO aps03pwb;

--
-- Name: st_mlinefromtext(text, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_mlinefromtext(text, integer) RETURNS geometry
    AS $_$
	SELECT CASE
	WHEN geometrytype(GeomFromText($1, $2)) = 'MULTILINESTRING'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.st_mlinefromtext(text, integer) OWNER TO aps03pwb;

--
-- Name: st_mlinefromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_mlinefromwkb(bytea, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'MULTILINESTRING'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.st_mlinefromwkb(bytea, integer) OWNER TO aps03pwb;

--
-- Name: st_mpointfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_mpointfromtext(text, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1, $2)) = 'MULTIPOINT'
	THEN GeomFromText($1, $2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.st_mpointfromtext(text, integer) OWNER TO aps03pwb;

--
-- Name: st_mpointfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_mpointfromwkb(bytea, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'MULTIPOINT'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.st_mpointfromwkb(bytea, integer) OWNER TO aps03pwb;

--
-- Name: st_mpolyfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_mpolyfromtext(text, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1, $2)) = 'MULTIPOLYGON'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.st_mpolyfromtext(text, integer) OWNER TO aps03pwb;

--
-- Name: st_mpolyfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_mpolyfromwkb(bytea, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'MULTIPOLYGON'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.st_mpolyfromwkb(bytea, integer) OWNER TO aps03pwb;

--
-- Name: st_numgeometries(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_numgeometries(geometry) RETURNS integer
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_numgeometries_collection'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_numgeometries(geometry) OWNER TO aps03pwb;

--
-- Name: st_numinteriorring(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_numinteriorring(geometry) RETURNS integer
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_numinteriorrings_polygon'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_numinteriorring(geometry) OWNER TO aps03pwb;

--
-- Name: st_numpoints(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_numpoints(geometry) RETURNS integer
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_numpoints_linestring'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_numpoints(geometry) OWNER TO aps03pwb;

--
-- Name: st_orderingequals(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_orderingequals(geometry, geometry) RETURNS boolean
    AS $_$
    SELECT $1 && $2 AND $1 ~= $2
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.st_orderingequals(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: st_overlaps(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_overlaps(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'overlaps'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_overlaps(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: st_perimeter(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_perimeter(geometry) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_perimeter2d_poly'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_perimeter(geometry) OWNER TO aps03pwb;

--
-- Name: st_point(double precision, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_point(double precision, double precision) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_makepoint'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_point(double precision, double precision) OWNER TO aps03pwb;

--
-- Name: st_pointfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_pointfromtext(text, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1, $2)) = 'POINT'
	THEN GeomFromText($1, $2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.st_pointfromtext(text, integer) OWNER TO aps03pwb;

--
-- Name: st_pointfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_pointfromwkb(bytea, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'POINT'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.st_pointfromwkb(bytea, integer) OWNER TO aps03pwb;

--
-- Name: st_pointn(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_pointn(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_pointn_linestring'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_pointn(geometry) OWNER TO aps03pwb;

--
-- Name: st_pointonsurface(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_pointonsurface(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'pointonsurface'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_pointonsurface(geometry) OWNER TO aps03pwb;

--
-- Name: st_polyfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_polyfromtext(text, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1, $2)) = 'POLYGON'
	THEN GeomFromText($1, $2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.st_polyfromtext(text, integer) OWNER TO aps03pwb;

--
-- Name: st_polyfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_polyfromwkb(bytea, integer) RETURNS geometry
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'POLYGON'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.st_polyfromwkb(bytea, integer) OWNER TO aps03pwb;

--
-- Name: st_polygon(geometry, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_polygon(geometry, integer) RETURNS geometry
    AS $_$
	SELECT setSRID(makepolygon($1), $2)
	$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.st_polygon(geometry, integer) OWNER TO aps03pwb;

--
-- Name: st_relate(geometry, geometry, text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_relate(geometry, geometry, text) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'relate_pattern'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_relate(geometry, geometry, text) OWNER TO aps03pwb;

--
-- Name: st_srid(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_srid(geometry) RETURNS integer
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_getSRID'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_srid(geometry) OWNER TO aps03pwb;

--
-- Name: st_startpoint(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_startpoint(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_startpoint_linestring'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_startpoint(geometry) OWNER TO aps03pwb;

--
-- Name: st_symdifference(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_symdifference(geometry, geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'symdifference'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_symdifference(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: st_touches(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_touches(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'touches'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_touches(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: st_transform(geometry, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_transform(geometry, integer) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'transform'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_transform(geometry, integer) OWNER TO aps03pwb;

--
-- Name: st_union(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_union(geometry, geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'geomunion'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_union(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: st_within(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_within(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'within'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_within(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: st_wkbtosql(bytea); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_wkbtosql(bytea) RETURNS geometry
    AS $_$SELECT GeomFromWKB($1)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.st_wkbtosql(bytea) OWNER TO aps03pwb;

--
-- Name: st_wkttosql(text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_wkttosql(text) RETURNS geometry
    AS $_$SELECT geometryfromtext($1)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.st_wkttosql(text) OWNER TO aps03pwb;

--
-- Name: st_x(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_x(geometry) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_x_point'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_x(geometry) OWNER TO aps03pwb;

--
-- Name: st_y(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION st_y(geometry) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_y_point'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.st_y(geometry) OWNER TO aps03pwb;

--
-- Name: startpoint(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION startpoint(geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_startpoint_linestring'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.startpoint(geometry) OWNER TO aps03pwb;

--
-- Name: summary(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION summary(geometry) RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_summary'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.summary(geometry) OWNER TO aps03pwb;

--
-- Name: symdifference(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION symdifference(geometry, geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'symdifference'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.symdifference(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: symmetricdifference(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION symmetricdifference(geometry, geometry) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'symdifference'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.symmetricdifference(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: text(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION text(geometry) RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_to_text'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.text(geometry) OWNER TO aps03pwb;

--
-- Name: text(boolean); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION text(boolean) RETURNS text
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOOL_to_text'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.text(boolean) OWNER TO aps03pwb;

--
-- Name: touches(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION touches(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'touches'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.touches(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: transform(geometry, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION transform(geometry, integer) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'transform'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.transform(geometry, integer) OWNER TO aps03pwb;

--
-- Name: transform_geometry(geometry, text, text, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION transform_geometry(geometry, text, text, integer) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'transform_geom'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.transform_geometry(geometry, text, text, integer) OWNER TO aps03pwb;

--
-- Name: translate(geometry, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION translate(geometry, double precision, double precision, double precision) RETURNS geometry
    AS $_$SELECT affine($1, 1, 0, 0, 0, 1, 0, 0, 0, 1, $2, $3, $4)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.translate(geometry, double precision, double precision, double precision) OWNER TO aps03pwb;

--
-- Name: translate(geometry, double precision, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION translate(geometry, double precision, double precision) RETURNS geometry
    AS $_$SELECT translate($1, $2, $3, 0)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.translate(geometry, double precision, double precision) OWNER TO aps03pwb;

--
-- Name: transscale(geometry, double precision, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION transscale(geometry, double precision, double precision, double precision, double precision) RETURNS geometry
    AS $_$SELECT affine($1,  $4, 0, 0,  0, $5, 0, 
		0, 0, 1,  $2 * $4, $3 * $5, 0)$_$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public.transscale(geometry, double precision, double precision, double precision, double precision) OWNER TO aps03pwb;

--
-- Name: unite_garray(geometry[]); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION unite_garray(geometry[]) RETURNS geometry
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'unite_garray'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.unite_garray(geometry[]) OWNER TO aps03pwb;

--
-- Name: unlockrows(text); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION unlockrows(text) RETURNS integer
    AS $_$
DECLARE
	ret int;
BEGIN

	IF NOT LongTransactionsEnabled() THEN
		RAISE EXCEPTION 'Long transaction support disabled, use EnableLongTransaction() to enable.';
	END IF;

	EXECUTE 'DELETE FROM authorization_table where authid = ' ||
		quote_literal($1);

	GET DIAGNOSTICS ret = ROW_COUNT;

	RETURN ret;
END;
$_$
    LANGUAGE plpgsql STRICT;


ALTER FUNCTION public.unlockrows(text) OWNER TO aps03pwb;

--
-- Name: update_geometry_stats(); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION update_geometry_stats() RETURNS text
    AS $$ SELECT 'update_geometry_stats() has been obsoleted. Statistics are automatically built running the ANALYZE command'::text$$
    LANGUAGE sql;


ALTER FUNCTION public.update_geometry_stats() OWNER TO aps03pwb;

--
-- Name: update_geometry_stats(character varying, character varying); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION update_geometry_stats(character varying, character varying) RETURNS text
    AS $$SELECT update_geometry_stats();$$
    LANGUAGE sql;


ALTER FUNCTION public.update_geometry_stats(character varying, character varying) OWNER TO aps03pwb;

--
-- Name: updategeometrysrid(character varying, character varying, character varying, character varying, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION updategeometrysrid(character varying, character varying, character varying, character varying, integer) RETURNS text
    AS $_$
DECLARE
	catalog_name alias for $1; 
	schema_name alias for $2;
	table_name alias for $3;
	column_name alias for $4;
	new_srid alias for $5;
	myrec RECORD;
	okay boolean;
	cname varchar;
	real_schema name;

BEGIN


	-- Find, check or fix schema_name
	IF ( schema_name != '' ) THEN
		okay = 'f';

		FOR myrec IN SELECT nspname FROM pg_namespace WHERE text(nspname) = schema_name LOOP
			okay := 't';
		END LOOP;

		IF ( okay <> 't' ) THEN
			RAISE EXCEPTION 'Invalid schema name';
		ELSE
			real_schema = schema_name;
		END IF;
	ELSE
		SELECT INTO real_schema current_schema()::text;
	END IF;

 	-- Find out if the column is in the geometry_columns table
	okay = 'f';
	FOR myrec IN SELECT * from geometry_columns where f_table_schema = text(real_schema) and f_table_name = table_name and f_geometry_column = column_name LOOP
		okay := 't';
	END LOOP; 
	IF (okay <> 't') THEN 
		RAISE EXCEPTION 'column not found in geometry_columns table';
		RETURN 'f';
	END IF;

	-- Update ref from geometry_columns table
	EXECUTE 'UPDATE geometry_columns SET SRID = ' || new_srid || 
		' where f_table_schema = ' ||
		quote_literal(real_schema) || ' and f_table_name = ' ||
		quote_literal(table_name)  || ' and f_geometry_column = ' ||
		quote_literal(column_name);
	
	-- Make up constraint name
	cname = 'enforce_srid_'  || column_name;

	-- Drop enforce_srid constraint
	EXECUTE 'ALTER TABLE ' || quote_ident(real_schema) ||
		'.' || quote_ident(table_name) ||
		' DROP constraint ' || quote_ident(cname);

	-- Update geometries SRID
	EXECUTE 'UPDATE ' || quote_ident(real_schema) ||
		'.' || quote_ident(table_name) ||
		' SET ' || quote_ident(column_name) ||
		' = setSRID(' || quote_ident(column_name) ||
		', ' || new_srid || ')';

	-- Reset enforce_srid constraint
	EXECUTE 'ALTER TABLE ' || quote_ident(real_schema) ||
		'.' || quote_ident(table_name) ||
		' ADD constraint ' || quote_ident(cname) ||
		' CHECK (srid(' || quote_ident(column_name) ||
		') = ' || new_srid || ')';

	RETURN real_schema || '.' || table_name || '.' || column_name ||' SRID changed to ' || new_srid;
	
END;
$_$
    LANGUAGE plpgsql STRICT;


ALTER FUNCTION public.updategeometrysrid(character varying, character varying, character varying, character varying, integer) OWNER TO aps03pwb;

--
-- Name: updategeometrysrid(character varying, character varying, character varying, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION updategeometrysrid(character varying, character varying, character varying, integer) RETURNS text
    AS $_$
DECLARE
	ret  text;
BEGIN
	SELECT UpdateGeometrySRID('',$1,$2,$3,$4) into ret;
	RETURN ret;
END;
$_$
    LANGUAGE plpgsql STRICT;


ALTER FUNCTION public.updategeometrysrid(character varying, character varying, character varying, integer) OWNER TO aps03pwb;

--
-- Name: updategeometrysrid(character varying, character varying, integer); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION updategeometrysrid(character varying, character varying, integer) RETURNS text
    AS $_$
DECLARE
	ret  text;
BEGIN
	SELECT UpdateGeometrySRID('','',$1,$2,$3) into ret;
	RETURN ret;
END;
$_$
    LANGUAGE plpgsql STRICT;


ALTER FUNCTION public.updategeometrysrid(character varying, character varying, integer) OWNER TO aps03pwb;

--
-- Name: width(chip); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION width(chip) RETURNS integer
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'CHIP_getWidth'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.width(chip) OWNER TO aps03pwb;

--
-- Name: within(geometry, geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION within(geometry, geometry) RETURNS boolean
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'within'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.within(geometry, geometry) OWNER TO aps03pwb;

--
-- Name: x(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION x(geometry) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_x_point'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.x(geometry) OWNER TO aps03pwb;

--
-- Name: xmax(box3d); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION xmax(box3d) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX3D_xmax'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.xmax(box3d) OWNER TO aps03pwb;

--
-- Name: xmin(box3d); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION xmin(box3d) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX3D_xmin'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.xmin(box3d) OWNER TO aps03pwb;

--
-- Name: y(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION y(geometry) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_y_point'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.y(geometry) OWNER TO aps03pwb;

--
-- Name: ymax(box3d); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION ymax(box3d) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX3D_ymax'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.ymax(box3d) OWNER TO aps03pwb;

--
-- Name: ymin(box3d); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION ymin(box3d) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX3D_ymin'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.ymin(box3d) OWNER TO aps03pwb;

--
-- Name: z(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION z(geometry) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_z_point'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.z(geometry) OWNER TO aps03pwb;

--
-- Name: zmax(box3d); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION zmax(box3d) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX3D_zmax'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.zmax(box3d) OWNER TO aps03pwb;

--
-- Name: zmflag(geometry); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION zmflag(geometry) RETURNS smallint
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'LWGEOM_zmflag'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.zmflag(geometry) OWNER TO aps03pwb;

--
-- Name: zmin(box3d); Type: FUNCTION; Schema: public; Owner: aps03pwb
--

CREATE FUNCTION zmin(box3d) RETURNS double precision
    AS '/usr/lib/postgresql/8.2/lib/liblwgeom.so.1.2', 'BOX3D_zmin'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.zmin(box3d) OWNER TO aps03pwb;

--
-- Name: accum(geometry); Type: AGGREGATE; Schema: public; Owner: aps03pwb
--

CREATE AGGREGATE accum(geometry) (
    SFUNC = geom_accum,
    STYPE = geometry[]
);


ALTER AGGREGATE public.accum(geometry) OWNER TO aps03pwb;

--
-- Name: collect(geometry); Type: AGGREGATE; Schema: public; Owner: aps03pwb
--

CREATE AGGREGATE collect(geometry) (
    SFUNC = geom_accum,
    STYPE = geometry[],
    FINALFUNC = collect_garray
);


ALTER AGGREGATE public.collect(geometry) OWNER TO aps03pwb;

--
-- Name: extent(geometry); Type: AGGREGATE; Schema: public; Owner: aps03pwb
--

CREATE AGGREGATE extent(geometry) (
    SFUNC = public.combine_bbox,
    STYPE = box2d
);


ALTER AGGREGATE public.extent(geometry) OWNER TO aps03pwb;

--
-- Name: extent3d(geometry); Type: AGGREGATE; Schema: public; Owner: aps03pwb
--

CREATE AGGREGATE extent3d(geometry) (
    SFUNC = public.combine_bbox,
    STYPE = box3d
);


ALTER AGGREGATE public.extent3d(geometry) OWNER TO aps03pwb;

--
-- Name: geomunion(geometry); Type: AGGREGATE; Schema: public; Owner: aps03pwb
--

CREATE AGGREGATE geomunion(geometry) (
    SFUNC = geom_accum,
    STYPE = geometry[],
    FINALFUNC = unite_garray
);


ALTER AGGREGATE public.geomunion(geometry) OWNER TO aps03pwb;

--
-- Name: makeline(geometry); Type: AGGREGATE; Schema: public; Owner: aps03pwb
--

CREATE AGGREGATE makeline(geometry) (
    SFUNC = geom_accum,
    STYPE = geometry[],
    FINALFUNC = makeline_garray
);


ALTER AGGREGATE public.makeline(geometry) OWNER TO aps03pwb;

--
-- Name: memcollect(geometry); Type: AGGREGATE; Schema: public; Owner: aps03pwb
--

CREATE AGGREGATE memcollect(geometry) (
    SFUNC = public.collect,
    STYPE = geometry
);


ALTER AGGREGATE public.memcollect(geometry) OWNER TO aps03pwb;

--
-- Name: memgeomunion(geometry); Type: AGGREGATE; Schema: public; Owner: aps03pwb
--

CREATE AGGREGATE memgeomunion(geometry) (
    SFUNC = public.geomunion,
    STYPE = geometry
);


ALTER AGGREGATE public.memgeomunion(geometry) OWNER TO aps03pwb;

--
-- Name: polygonize(geometry); Type: AGGREGATE; Schema: public; Owner: aps03pwb
--

CREATE AGGREGATE polygonize(geometry) (
    SFUNC = geom_accum,
    STYPE = geometry[],
    FINALFUNC = polygonize_garray
);


ALTER AGGREGATE public.polygonize(geometry) OWNER TO aps03pwb;

--
-- Name: &&; Type: OPERATOR; Schema: public; Owner: aps03pwb
--

CREATE OPERATOR && (
    PROCEDURE = geometry_overlap,
    LEFTARG = geometry,
    RIGHTARG = geometry,
    COMMUTATOR = &&,
    RESTRICT = postgis_gist_sel,
    JOIN = postgis_gist_joinsel
);


ALTER OPERATOR public.&& (geometry, geometry) OWNER TO aps03pwb;

--
-- Name: &<; Type: OPERATOR; Schema: public; Owner: aps03pwb
--

CREATE OPERATOR &< (
    PROCEDURE = geometry_overleft,
    LEFTARG = geometry,
    RIGHTARG = geometry,
    COMMUTATOR = &>,
    RESTRICT = positionsel,
    JOIN = positionjoinsel
);


ALTER OPERATOR public.&< (geometry, geometry) OWNER TO aps03pwb;

--
-- Name: &<|; Type: OPERATOR; Schema: public; Owner: aps03pwb
--

CREATE OPERATOR &<| (
    PROCEDURE = geometry_overbelow,
    LEFTARG = geometry,
    RIGHTARG = geometry,
    COMMUTATOR = |&>,
    RESTRICT = positionsel,
    JOIN = positionjoinsel
);


ALTER OPERATOR public.&<| (geometry, geometry) OWNER TO aps03pwb;

--
-- Name: &>; Type: OPERATOR; Schema: public; Owner: aps03pwb
--

CREATE OPERATOR &> (
    PROCEDURE = geometry_overright,
    LEFTARG = geometry,
    RIGHTARG = geometry,
    COMMUTATOR = &<,
    RESTRICT = positionsel,
    JOIN = positionjoinsel
);


ALTER OPERATOR public.&> (geometry, geometry) OWNER TO aps03pwb;

--
-- Name: <; Type: OPERATOR; Schema: public; Owner: aps03pwb
--

CREATE OPERATOR < (
    PROCEDURE = geometry_lt,
    LEFTARG = geometry,
    RIGHTARG = geometry,
    COMMUTATOR = >,
    NEGATOR = >=,
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.< (geometry, geometry) OWNER TO aps03pwb;

--
-- Name: <<; Type: OPERATOR; Schema: public; Owner: aps03pwb
--

CREATE OPERATOR << (
    PROCEDURE = geometry_left,
    LEFTARG = geometry,
    RIGHTARG = geometry,
    COMMUTATOR = >>,
    RESTRICT = positionsel,
    JOIN = positionjoinsel
);


ALTER OPERATOR public.<< (geometry, geometry) OWNER TO aps03pwb;

--
-- Name: <<|; Type: OPERATOR; Schema: public; Owner: aps03pwb
--

CREATE OPERATOR <<| (
    PROCEDURE = geometry_below,
    LEFTARG = geometry,
    RIGHTARG = geometry,
    COMMUTATOR = |>>,
    RESTRICT = positionsel,
    JOIN = positionjoinsel
);


ALTER OPERATOR public.<<| (geometry, geometry) OWNER TO aps03pwb;

--
-- Name: <=; Type: OPERATOR; Schema: public; Owner: aps03pwb
--

CREATE OPERATOR <= (
    PROCEDURE = geometry_le,
    LEFTARG = geometry,
    RIGHTARG = geometry,
    COMMUTATOR = >=,
    NEGATOR = >,
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.<= (geometry, geometry) OWNER TO aps03pwb;

--
-- Name: =; Type: OPERATOR; Schema: public; Owner: aps03pwb
--

CREATE OPERATOR = (
    PROCEDURE = geometry_eq,
    LEFTARG = geometry,
    RIGHTARG = geometry,
    COMMUTATOR = =,
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.= (geometry, geometry) OWNER TO aps03pwb;

--
-- Name: >; Type: OPERATOR; Schema: public; Owner: aps03pwb
--

CREATE OPERATOR > (
    PROCEDURE = geometry_gt,
    LEFTARG = geometry,
    RIGHTARG = geometry,
    COMMUTATOR = <,
    NEGATOR = <=,
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.> (geometry, geometry) OWNER TO aps03pwb;

--
-- Name: >=; Type: OPERATOR; Schema: public; Owner: aps03pwb
--

CREATE OPERATOR >= (
    PROCEDURE = geometry_ge,
    LEFTARG = geometry,
    RIGHTARG = geometry,
    COMMUTATOR = <=,
    NEGATOR = <,
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.>= (geometry, geometry) OWNER TO aps03pwb;

--
-- Name: >>; Type: OPERATOR; Schema: public; Owner: aps03pwb
--

CREATE OPERATOR >> (
    PROCEDURE = geometry_right,
    LEFTARG = geometry,
    RIGHTARG = geometry,
    COMMUTATOR = <<,
    RESTRICT = positionsel,
    JOIN = positionjoinsel
);


ALTER OPERATOR public.>> (geometry, geometry) OWNER TO aps03pwb;

--
-- Name: @; Type: OPERATOR; Schema: public; Owner: aps03pwb
--

CREATE OPERATOR @ (
    PROCEDURE = geometry_contained,
    LEFTARG = geometry,
    RIGHTARG = geometry,
    COMMUTATOR = ~,
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.@ (geometry, geometry) OWNER TO aps03pwb;

--
-- Name: |&>; Type: OPERATOR; Schema: public; Owner: aps03pwb
--

CREATE OPERATOR |&> (
    PROCEDURE = geometry_overabove,
    LEFTARG = geometry,
    RIGHTARG = geometry,
    COMMUTATOR = &<|,
    RESTRICT = positionsel,
    JOIN = positionjoinsel
);


ALTER OPERATOR public.|&> (geometry, geometry) OWNER TO aps03pwb;

--
-- Name: |>>; Type: OPERATOR; Schema: public; Owner: aps03pwb
--

CREATE OPERATOR |>> (
    PROCEDURE = geometry_above,
    LEFTARG = geometry,
    RIGHTARG = geometry,
    COMMUTATOR = <<|,
    RESTRICT = positionsel,
    JOIN = positionjoinsel
);


ALTER OPERATOR public.|>> (geometry, geometry) OWNER TO aps03pwb;

--
-- Name: ~; Type: OPERATOR; Schema: public; Owner: aps03pwb
--

CREATE OPERATOR ~ (
    PROCEDURE = geometry_contain,
    LEFTARG = geometry,
    RIGHTARG = geometry,
    COMMUTATOR = @,
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.~ (geometry, geometry) OWNER TO aps03pwb;

--
-- Name: ~=; Type: OPERATOR; Schema: public; Owner: aps03pwb
--

CREATE OPERATOR ~= (
    PROCEDURE = geometry_same,
    LEFTARG = geometry,
    RIGHTARG = geometry,
    COMMUTATOR = ~=,
    RESTRICT = eqsel,
    JOIN = eqjoinsel
);


ALTER OPERATOR public.~= (geometry, geometry) OWNER TO aps03pwb;

--
-- Name: belement_geometry_ops; Type: OPERATOR CLASS; Schema: public; Owner: aps03pwb
--

CREATE OPERATOR CLASS belement_geometry_ops
    DEFAULT FOR TYPE geometry USING belement AS
    OPERATOR 1 <(geometry,geometry) ,
    OPERATOR 2 <=(geometry,geometry) ,
    OPERATOR 3 =(geometry,geometry) ,
    OPERATOR 4 >=(geometry,geometry) ,
    OPERATOR 5 >(geometry,geometry) ,
    FUNCTION 1 geometry_cmp(geometry,geometry);


ALTER OPERATOR CLASS public.belement_geometry_ops USING belement OWNER TO aps03pwb;

--
-- Name: gist_geometry_ops; Type: OPERATOR CLASS; Schema: public; Owner: aps03pwb
--

CREATE OPERATOR CLASS gist_geometry_ops
    DEFAULT FOR TYPE geometry USING gist AS
    STORAGE box2d ,
    OPERATOR 1 <<(geometry,geometry) RECHECK ,
    OPERATOR 2 &<(geometry,geometry) RECHECK ,
    OPERATOR 3 &&(geometry,geometry) RECHECK ,
    OPERATOR 4 &>(geometry,geometry) RECHECK ,
    OPERATOR 5 >>(geometry,geometry) RECHECK ,
    OPERATOR 6 ~=(geometry,geometry) RECHECK ,
    OPERATOR 7 ~(geometry,geometry) RECHECK ,
    OPERATOR 8 @(geometry,geometry) RECHECK ,
    OPERATOR 9 &<|(geometry,geometry) RECHECK ,
    OPERATOR 10 <<|(geometry,geometry) RECHECK ,
    OPERATOR 11 |>>(geometry,geometry) RECHECK ,
    OPERATOR 12 |&>(geometry,geometry) RECHECK ,
    FUNCTION 1 lwgeom_gist_consistent(internal,geometry,integer) ,
    FUNCTION 2 lwgeom_gist_union(bytea,internal) ,
    FUNCTION 3 lwgeom_gist_compress(internal) ,
    FUNCTION 4 lwgeom_gist_decompress(internal) ,
    FUNCTION 5 lwgeom_gist_penalty(internal,internal,internal) ,
    FUNCTION 6 lwgeom_gist_picksplit(internal,internal) ,
    FUNCTION 7 lwgeom_gist_same(box2d,box2d,internal);


ALTER OPERATOR CLASS public.gist_geometry_ops USING gist OWNER TO aps03pwb;

SET search_path = pg_catalog;

--
-- Name: CAST (boolean AS text); Type: CAST; Schema: pg_catalog; Owner: 
--

CREATE CAST (boolean AS text) WITH FUNCTION public.text(boolean) AS IMPLICIT;


--
-- Name: CAST (public.box2d AS public.box3d); Type: CAST; Schema: pg_catalog; Owner: 
--

CREATE CAST (public.box2d AS public.box3d) WITH FUNCTION public.box3d(public.box2d) AS IMPLICIT;


--
-- Name: CAST (public.box2d AS public.geometry); Type: CAST; Schema: pg_catalog; Owner: 
--

CREATE CAST (public.box2d AS public.geometry) WITH FUNCTION public.geometry(public.box2d) AS IMPLICIT;


--
-- Name: CAST (public.box3d AS box); Type: CAST; Schema: pg_catalog; Owner: 
--

CREATE CAST (public.box3d AS box) WITH FUNCTION public.box(public.box3d) AS IMPLICIT;


--
-- Name: CAST (public.box3d AS public.box2d); Type: CAST; Schema: pg_catalog; Owner: 
--

CREATE CAST (public.box3d AS public.box2d) WITH FUNCTION public.box2d(public.box3d) AS IMPLICIT;


--
-- Name: CAST (public.box3d AS public.geometry); Type: CAST; Schema: pg_catalog; Owner: 
--

CREATE CAST (public.box3d AS public.geometry) WITH FUNCTION public.geometry(public.box3d) AS IMPLICIT;


--
-- Name: CAST (bytea AS public.geometry); Type: CAST; Schema: pg_catalog; Owner: 
--

CREATE CAST (bytea AS public.geometry) WITH FUNCTION public.geometry(bytea) AS IMPLICIT;


--
-- Name: CAST (public.chip AS public.geometry); Type: CAST; Schema: pg_catalog; Owner: 
--

CREATE CAST (public.chip AS public.geometry) WITH FUNCTION public.geometry(public.chip) AS IMPLICIT;


--
-- Name: CAST (public.geometry AS box); Type: CAST; Schema: pg_catalog; Owner: 
--

CREATE CAST (public.geometry AS box) WITH FUNCTION public.box(public.geometry) AS IMPLICIT;


--
-- Name: CAST (public.geometry AS public.box2d); Type: CAST; Schema: pg_catalog; Owner: 
--

CREATE CAST (public.geometry AS public.box2d) WITH FUNCTION public.box2d(public.geometry) AS IMPLICIT;


--
-- Name: CAST (public.geometry AS public.box3d); Type: CAST; Schema: pg_catalog; Owner: 
--

CREATE CAST (public.geometry AS public.box3d) WITH FUNCTION public.box3d(public.geometry) AS IMPLICIT;


--
-- Name: CAST (public.geometry AS bytea); Type: CAST; Schema: pg_catalog; Owner: 
--

CREATE CAST (public.geometry AS bytea) WITH FUNCTION public.bytea(public.geometry) AS IMPLICIT;


--
-- Name: CAST (public.geometry AS text); Type: CAST; Schema: pg_catalog; Owner: 
--

CREATE CAST (public.geometry AS text) WITH FUNCTION public.text(public.geometry) AS IMPLICIT;


--
-- Name: CAST (text AS public.geometry); Type: CAST; Schema: pg_catalog; Owner: 
--

CREATE CAST (text AS public.geometry) WITH FUNCTION public.geometry(text) AS IMPLICIT;


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: VENTURE; Type: TABLE; Schema: public; Owner: webuser; Tablespace: 
--

CREATE TABLE "VENTURE" (
    gid integer NOT NULL,
    name character varying(80),
    elevation double precision,
    symbol character varying(80),
    "comment" character varying(80),
    descriptio character varying(80),
    source character varying(80),
    url character varying(80),
    "url name" character varying(80),
    the_geom geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((srid(the_geom) = -1))
);


ALTER TABLE public."VENTURE" OWNER TO webuser;

--
-- Name: brita; Type: TABLE; Schema: public; Owner: webuser; Tablespace: 
--

CREATE TABLE brita (
    gid integer NOT NULL,
    name character varying(80),
    elevation double precision,
    symbol character varying(80),
    "comment" character varying(80),
    descriptio character varying(80),
    source character varying(80),
    url character varying(80),
    "url name" character varying(80),
    the_geom geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((srid(the_geom) = -1))
);


ALTER TABLE public.brita OWNER TO webuser;

--
-- Name: brita2; Type: TABLE; Schema: public; Owner: webuser; Tablespace: 
--

CREATE TABLE brita2 (
    gid integer NOT NULL,
    name character varying(80),
    elevation double precision,
    symbol character varying(80),
    "comment" character varying(80),
    descriptio character varying(80),
    source character varying(80),
    url character varying(80),
    "url name" character varying(80),
    the_geom geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((srid(the_geom) = -1))
);


ALTER TABLE public.brita2 OWNER TO webuser;

--
-- Name: download1; Type: TABLE; Schema: public; Owner: webuser; Tablespace: 
--

CREATE TABLE download1 (
    gid integer NOT NULL,
    name character varying(80),
    elevation double precision,
    symbol character varying(80),
    "comment" character varying(80),
    descriptio character varying(80),
    source character varying(80),
    url character varying(80),
    "url name" character varying(80),
    the_geom geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((srid(the_geom) = -1))
);


ALTER TABLE public.download1 OWNER TO webuser;

--
-- Name: download2; Type: TABLE; Schema: public; Owner: webuser; Tablespace: 
--

CREATE TABLE download2 (
    gid integer NOT NULL,
    name character varying(80),
    elevation double precision,
    symbol character varying(80),
    "comment" character varying(80),
    descriptio character varying(80),
    source character varying(80),
    url character varying(80),
    "url name" character varying(80),
    the_geom geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((srid(the_geom) = -1))
);


ALTER TABLE public.download2 OWNER TO webuser;

--
-- Name: download3; Type: TABLE; Schema: public; Owner: webuser; Tablespace: 
--

CREATE TABLE download3 (
    gid integer NOT NULL,
    name character varying(80),
    elevation double precision,
    symbol character varying(80),
    "comment" character varying(80),
    descriptio character varying(80),
    source character varying(80),
    url character varying(80),
    "url name" character varying(80),
    the_geom geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((srid(the_geom) = -1))
);


ALTER TABLE public.download3 OWNER TO webuser;

--
-- Name: download4; Type: TABLE; Schema: public; Owner: webuser; Tablespace: 
--

CREATE TABLE download4 (
    gid integer NOT NULL,
    name character varying(80),
    elevation double precision,
    symbol character varying(80),
    "comment" character varying(80),
    descriptio character varying(80),
    source character varying(80),
    url character varying(80),
    "url name" character varying(80),
    the_geom geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((srid(the_geom) = -1))
);


ALTER TABLE public.download4 OWNER TO webuser;

--
-- Name: download5; Type: TABLE; Schema: public; Owner: webuser; Tablespace: 
--

CREATE TABLE download5 (
    gid integer NOT NULL,
    name character varying(80),
    elevation double precision,
    symbol character varying(80),
    "comment" character varying(80),
    descriptio character varying(80),
    source character varying(80),
    url character varying(80),
    "url name" character varying(80),
    the_geom geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((srid(the_geom) = -1))
);


ALTER TABLE public.download5 OWNER TO webuser;

SET default_with_oids = true;

--
-- Name: geometry_columns; Type: TABLE; Schema: public; Owner: aps03pwb; Tablespace: 
--

CREATE TABLE geometry_columns (
    f_table_catalog character varying(256) NOT NULL,
    f_table_schema character varying(256) NOT NULL,
    f_table_name character varying(256) NOT NULL,
    f_geometry_column character varying(256) NOT NULL,
    coord_dimension integer NOT NULL,
    srid integer NOT NULL,
    "type" character varying(30) NOT NULL
);


ALTER TABLE public.geometry_columns OWNER TO aps03pwb;

SET default_with_oids = false;

--
-- Name: spatial_ref_sys; Type: TABLE; Schema: public; Owner: aps03pwb; Tablespace: 
--

CREATE TABLE spatial_ref_sys (
    srid integer NOT NULL,
    auth_name character varying(256),
    auth_srid integer,
    srtext character varying(2048),
    proj4text character varying(2048)
);


ALTER TABLE public.spatial_ref_sys OWNER TO aps03pwb;

--
-- Name: sturtaug08; Type: TABLE; Schema: public; Owner: webuser; Tablespace: 
--

CREATE TABLE sturtaug08 (
    gid integer NOT NULL,
    name character varying(80),
    elevation double precision,
    symbol character varying(80),
    "comment" character varying(80),
    descriptio character varying(80),
    source character varying(80),
    url character varying(80),
    "url name" character varying(80),
    the_geom geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((srid(the_geom) = -1))
);


ALTER TABLE public.sturtaug08 OWNER TO webuser;

--
-- Name: tblgpsdata; Type: TABLE; Schema: public; Owner: aps03pwb; Tablespace: 
--

CREATE TABLE tblgpsdata (
    name character varying(80),
    elevation double precision,
    symbol character varying(80),
    "comment" character varying(80),
    descriptio character varying(80),
    source character varying(80),
    url character varying(80),
    "url name" character varying(80),
    the_geom geometry,
    gpsdataid integer NOT NULL,
    CONSTRAINT enforce_dims_the_geom CHECK ((ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((srid(the_geom) = -1))
);


ALTER TABLE public.tblgpsdata OWNER TO aps03pwb;

--
-- Name: tblgpsdata_gpsdataid_seq; Type: SEQUENCE; Schema: public; Owner: aps03pwb
--

CREATE SEQUENCE tblgpsdata_gpsdataid_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.tblgpsdata_gpsdataid_seq OWNER TO aps03pwb;

--
-- Name: tblgpsdata_gpsdataid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: aps03pwb
--

ALTER SEQUENCE tblgpsdata_gpsdataid_seq OWNED BY tblgpsdata.gpsdataid;


--
-- Name: tblgpsdata_gpsdataid_seq; Type: SEQUENCE SET; Schema: public; Owner: aps03pwb
--

SELECT pg_catalog.setval('tblgpsdata_gpsdataid_seq', 2798, true);


--
-- Name: tblgpsmaster; Type: TABLE; Schema: public; Owner: aps03pwb; Tablespace: 
--

CREATE TABLE tblgpsmaster (
    name character varying(80),
    elevation double precision,
    symbol character varying(80),
    "comment" character varying(80),
    descriptio character varying(80),
    source character varying(80),
    url character varying(80),
    "url name" character varying(80),
    the_geom geometry,
    gpsmasterid integer NOT NULL,
    CONSTRAINT enforce_dims_the_geom CHECK ((ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((srid(the_geom) = -1))
);


ALTER TABLE public.tblgpsmaster OWNER TO aps03pwb;

--
-- Name: tblgpsmaster_gpsmasterid_seq; Type: SEQUENCE; Schema: public; Owner: aps03pwb
--

CREATE SEQUENCE tblgpsmaster_gpsmasterid_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.tblgpsmaster_gpsmasterid_seq OWNER TO aps03pwb;

--
-- Name: tblgpsmaster_gpsmasterid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: aps03pwb
--

ALTER SEQUENCE tblgpsmaster_gpsmasterid_seq OWNED BY tblgpsmaster.gpsmasterid;


--
-- Name: tblgpsmaster_gpsmasterid_seq; Type: SEQUENCE SET; Schema: public; Owner: aps03pwb
--

SELECT pg_catalog.setval('tblgpsmaster_gpsmasterid_seq', 3972, true);


--
-- Name: gpsdataid; Type: DEFAULT; Schema: public; Owner: aps03pwb
--

ALTER TABLE tblgpsdata ALTER COLUMN gpsdataid SET DEFAULT nextval('tblgpsdata_gpsdataid_seq'::regclass);


--
-- Name: gpsmasterid; Type: DEFAULT; Schema: public; Owner: aps03pwb
--

ALTER TABLE tblgpsmaster ALTER COLUMN gpsmasterid SET DEFAULT nextval('tblgpsmaster_gpsmasterid_seq'::regclass);


--
-- Data for Name: VENTURE; Type: TABLE DATA; Schema: public; Owner: webuser
--

COPY "VENTURE" (gid, name, elevation, symbol, "comment", descriptio, source, url, "url name", the_geom) FROM stdin;
0	001	0	Waypoint	001	001	\N	\N	\N	01010000004BA633C556724040D251FF3AA6764140
1	002	1568.26	Waypoint	002	002	\N	\N	\N	0101000000BB3A532A59724040CC02D471C8764140
2	003	136.87200000000001	Waypoint	003	003	\N	\N	\N	01010000006A595CBB2BAC40400BA52CCC0D614140
3	004	121.011	Waypoint	004	004	\N	\N	\N	01010000001BED376122AC40401ED24797F9604140
4	ALAMSCV GP	298.613	Waypoint	ALAMSCV GP	ALAMSCV GP	\N	\N	\N	0101000000DC3287745DB34040F52B470F567D4140
5	CTC CO1	1726.8800000000001	Waypoint	CTC CO1	CTC CO1	\N	\N	\N	010100000082696C3D307040408BC14CA60D764140
6	CTC JEN	1638.4400000000001	Waypoint	CTC JEN	CTC JEN	\N	\N	\N	0101000000AA50D0327A724040D29ADF6342774140
7	CTC100	1853.77	Waypoint	CTC100	CTC100	\N	\N	\N	0101000000810269AB046E40402991C86747774140
8	CTC101	1853.77	Waypoint	CTC101	CTC101	\N	\N	\N	01010000001C376CC3F76D40409705BF7F46774140
9	CTC102	1853.77	Waypoint	CTC102	CTC102	\N	\N	\N	0101000000EF0CED78E06D4040BC1FF4C256774140
10	CTC12	788.64099999999996	Waypoint	CTC12	CTC12	\N	\N	\N	01010000001622B791F85240404BF7A08B397A4140
11	CTC13	812.43399999999997	Waypoint	CTC13	CTC13	\N	\N	\N	01010000005A24881BF95240402BF470EB367A4140
12	CTC130	1806.4300000000001	Waypoint	CTC130	CTC130	\N	\N	\N	0101000000FC0700AA126F404088014DAF4D784140
13	CTC131	1777.8299999999999	Waypoint	CTC131	CTC131	\N	\N	\N	010100000079F150A4136F4040770435514B784140
14	CTC132	1777.8299999999999	Waypoint	CTC132	CTC132	\N	\N	\N	010100000026AF2432D97440409E0607D9C4844140
15	CTC133	1792.49	Waypoint	CTC133	CTC133	\N	\N	\N	0101000000D0B53459116F4040590ABD0857784140
16	CTC135	1816.28	Waypoint	CTC135	CTC135	\N	\N	\N	01010000004C3943970E6F40403AEF3E4848784140
17	CTC136	1793.45	Waypoint	CTC136	CTC136	\N	\N	\N	010100000094F26CEC186F4040C8D8E0AF4F784140
18	CTC137	1809.55	Waypoint	CTC137	CTC137	\N	\N	\N	0101000000CE8C53B4FF6E4040807F87F840784140
19	CTC138	1810.03	Waypoint	CTC138	CTC138	\N	\N	\N	010100000050EC78FEFA6E4040AFCEC4834C784140
20	CTC139	1825.9000000000001	Waypoint	CTC139	CTC139	\N	\N	\N	010100000018394325F96E404029BEDC5247784140
21	CTC14	807.86699999999996	Waypoint	CTC14	CTC14	\N	\N	\N	010100000015EE6CFAF952404095AC6C1A397A4140
22	CTC140	1793.45	Waypoint	CTC140	CTC140	\N	\N	\N	010100000065E2C896F46E40400EDAB04841784140
23	CTC141	1840.8	Waypoint	CTC141	CTC141	\N	\N	\N	01010000005C678319016F404078F8B488FD774140
24	CTC142	1828.0599999999999	Waypoint	CTC142	CTC142	\N	\N	\N	0101000000F7076CFCF96E40403DADD0771F784140
25	CTC143	1842.24	Waypoint	CTC143	CTC143	\N	\N	\N	0101000000DE0F080C036F40402604E0DCF5774140
26	CTC144	1822.77	Waypoint	CTC144	CTC144	\N	\N	\N	0101000000367DF7400B6F40404B17DCBB38784140
27	CTC145	1830.46	Waypoint	CTC145	CTC145	\N	\N	\N	0101000000DAEDA6A00A6F4040C0688C5F03784140
28	CTC146	1830.46	Waypoint	CTC146	CTC146	\N	\N	\N	01010000009072BB8D096F4040F4C1DC35FB774140
29	CTC147	1841.52	Waypoint	CTC147	CTC147	\N	\N	\N	0101000000700FDF15036F404087D8006FF8774140
30	CTC15	1756.9200000000001	Waypoint	CTC15	CTC15	\N	\N	\N	0101000000FD00C745456F40404ADC7C8664784140
31	CTC150	1725.2	Waypoint	CTC150	CTC150	\N	\N	\N	010100000016A65CD23270404027EE50CB11764140
32	CTC151	1717.75	Waypoint	CTC151	CTC151	\N	\N	\N	01010000003D03CC770E70404056F01874FB754140
33	CTC152	1735.77	Waypoint	CTC152	CTC152	\N	\N	\N	0101000000AA411C142270404077135BC50B764140
34	CTC153	1720.3900000000001	Waypoint	CTC153	CTC153	\N	\N	\N	010100000095FB106000704040230B899500764140
35	CTC154	1711.74	Waypoint	CTC154	CTC154	\N	\N	\N	01010000003AD548B5097040404FD138A4F6754140
36	CTC155	1724.72	Waypoint	CTC155	CTC155	\N	\N	\N	01010000002EB2F8A81A704040BD92C8640F764140
37	CTC156	1715.3399999999999	Waypoint	CTC156	CTC156	\N	\N	\N	0101000000CA28DB2FFB6F40404F2EAC6CF6754140
38	CTC157	1715.3399999999999	Waypoint	CTC157	CTC157	\N	\N	\N	0101000000E763B32D077040400382E0C1F4754140
39	CTC158	1761.97	Waypoint	CTC158	CTC158	\N	\N	\N	0101000000D52F182902704040744328F61D764140
40	CTC159	1717.51	Waypoint	CTC159	CTC159	\N	\N	\N	01010000003FE2940FFA6F4040302B7CCCF3754140
41	CTC16	1755.24	Waypoint	CTC16	CTC16	\N	\N	\N	01010000001AEEAAA7596F404096253F8466784140
42	CTC160	1708.6199999999999	Waypoint	CTC160	CTC160	\N	\N	\N	010100000019F27252047040408ECEBCC6F5754140
43	CTC161	1748.03	Waypoint	CTC161	CTC161	\N	\N	\N	01010000008DB4085009704040359A3B7E18764140
44	CTC162	1768.22	Waypoint	CTC162	CTC162	\N	\N	\N	01010000006469173BF06F40406F35FF7016764140
45	CTC163	1702.6099999999999	Waypoint	CTC163	CTC163	\N	\N	\N	0101000000CA3DCC7A0B70404073CDA07EF0754140
46	CTC165	1787.4400000000001	Waypoint	CTC165	CTC165	\N	\N	\N	01010000008E9897CBDF6F4040562E1C5417764140
47	CTC166	1705.97	Waypoint	CTC166	CTC166	\N	\N	\N	0101000000D0EB246217704040EFBC9487F3754140
48	CTC167	1748.03	Waypoint	CTC167	CTC167	\N	\N	\N	0101000000B88437FCD26F404005B1647508764140
49	CTC168	1806.4300000000001	Waypoint	CTC168	CTC168	\N	\N	\N	01010000003F7F448DAE6F40406DB51C220D764140
50	CTC169	1775.6700000000001	Waypoint	CTC169	CTC169	\N	\N	\N	01010000005CDC0C58EE6F40406443B3C21F764140
51	CTC17	1749.71	Waypoint	CTC17	CTC17	\N	\N	\N	0101000000E37AEB2E566F4040E650DF6260784140
52	CTC170	1775.4300000000001	Waypoint	CTC170	CTC170	\N	\N	\N	0101000000C40B5744E36F4040ED03984B0D764140
53	CTC171	1848.01	Waypoint	CTC171	CTC171	\N	\N	\N	0101000000DD5ED7D1E66D404056CB386E3A774140
54	CTC172	1832.3800000000001	Waypoint	CTC172	CTC172	\N	\N	\N	01010000004377EB2AD46D40403380DB063A774140
55	CTC173	1775.4300000000001	Waypoint	CTC173	CTC173	\N	\N	\N	01010000007106D006D86D4040A7B19C084B774140
56	CTC174	1870.3599999999999	Waypoint	CTC174	CTC174	\N	\N	\N	010100000013923786E16D40402001857541774140
57	CTC175	1850.1700000000001	Waypoint	CTC175	CTC175	\N	\N	\N	01010000007D1DE71FC56D40406FAF883E38774140
58	CTC176	1775.4300000000001	Waypoint	CTC176	CTC176	\N	\N	\N	01010000007106D006D86D4040A7B19C084B774140
59	CTC177	1852.3299999999999	Waypoint	CTC177	CTC177	\N	\N	\N	01010000001F44DCE3DF6D404093FACE0943774140
60	CTC178	1841.04	Waypoint	CTC178	CTC178	\N	\N	\N	0101000000AFCE2C67C16D40401360BB2838774140
61	CTC179	1859.3	Waypoint	CTC179	CTC179	\N	\N	\N	0101000000E265A4E3CC6D4040AAEDB4A23E774140
62	CTC18	1754.04	Waypoint	CTC18	CTC18	\N	\N	\N	010100000071C1005F5D6F4040E5FFA48D62784140
63	CTC180	1867.71	Waypoint	CTC180	CTC180	\N	\N	\N	0101000000292BF08FE36D40403D071F1542774140
64	CTC181	1837.9100000000001	Waypoint	CTC181	CTC181	\N	\N	\N	0101000000EFF61E03B96D4040D31F7B6C34774140
65	CTC182	1859.54	Waypoint	CTC182	CTC182	\N	\N	\N	010100000023932B73B56D4040466E50723C774140
66	CTC183	1870.3599999999999	Waypoint	CTC183	CTC183	\N	\N	\N	0101000000E9704CCDE06D4040C708645347774140
67	CTC184	1848.97	Waypoint	CTC184	CTC184	\N	\N	\N	01010000005FD5B4E6226E40400771CC4F3D774140
68	CTC185	1851.3699999999999	Waypoint	CTC185	CTC185	\N	\N	\N	0101000000B4BBD0F4436E404011B8B0993C774140
69	CTC186	1867.95	Waypoint	CTC186	CTC186	\N	\N	\N	01010000003C6AC396DB6D4040757D179D49774140
70	CTC19	1754.28	Waypoint	CTC19	CTC19	\N	\N	\N	01010000004B0540D55B6F4040E23AB7EA69784140
71	CTC20	1761.73	Waypoint	CTC20	CTC20	\N	\N	\N	01010000000F891091396F40404885ACCC66784140
72	CTC2007 10	1689.6300000000001	Waypoint	CTC2007 10	CTC2007 10	\N	\N	\N	01010000003E0724CE7B7240403DE05480FC774140
73	CTC2007 11	1684.8199999999999	Waypoint	CTC2007 11	CTC2007 11	\N	\N	\N	010100000040BEA05F7D724040753EFFE7F7774140
74	CTC2007 12	1679.0599999999999	Waypoint	CTC2007 12	CTC2007 12	\N	\N	\N	0101000000085184F57B7240407FD634A3EA774140
75	CTC2007 9	1697.8	Waypoint	CTC2007 9	CTC2007 9	\N	\N	\N	0101000000B68B57C37F7240402E631F7103784140
76	CTC21	1756.9200000000001	Waypoint	CTC21	CTC21	\N	\N	\N	0101000000B90C8B93336F404028EB28E865784140
77	CTC22	1772.54	Waypoint	CTC22	CTC22	\N	\N	\N	010100000095732C66396F4040E68BC0CF6D784140
78	CTC23	1799.22	Waypoint	CTC23	CTC23	\N	\N	\N	0101000000E76DA420436F4040B5C3FC687A784140
79	CTC24	1789.8499999999999	Waypoint	CTC24	CTC24	\N	\N	\N	0101000000D3C54824416F4040C1A1103180784140
80	CTC25	1787.2	Waypoint	CTC25	CTC25	\N	\N	\N	0101000000775CC4DF456F40403C110B4F84784140
81	CTC26	1789.1300000000001	Waypoint	CTC26	CTC26	\N	\N	\N	010100000027CD30FF326F4040CD6234BB86784140
82	CTC27	1782.6400000000001	Waypoint	CTC27	CTC27	\N	\N	\N	0101000000E695F0262D6F40409549AFFB7E784140
83	CTC28	1785.04	Waypoint	CTC28	CTC28	\N	\N	\N	0101000000BC05E793226F40404156DB4086784140
84	CTC29	1784.0799999999999	Waypoint	CTC29	CTC29	\N	\N	\N	0101000000ADD6BCBC166F40405E8B34F687784140
85	CTC30	1798.74	Waypoint	CTC30	CTC30	\N	\N	\N	010100000090892C83FE6E4040A11C8BC191784140
86	CTC300	1800.1800000000001	Waypoint	CTC300	CTC300	\N	\N	\N	01010000008E6604D2406F40409EA6E03A10784140
87	CTC302	1699.72	Waypoint	CTC302	CTC302	\N	\N	\N	0101000000DBFC2C87376E4040B876AC96D7794140
88	CTC304	1726.4000000000001	Waypoint	CTC304	CTC304	\N	\N	\N	01010000007A7CA39F2E6E4040BED78F9BC1794140
89	CTC306	1734.8099999999999	Waypoint	CTC306	CTC306	\N	\N	\N	0101000000AE4F577C256E40401A35DBECBA794140
90	CTC31	1804.27	Waypoint	CTC31	CTC31	\N	\N	\N	01010000008D4F70CDF76E40406F71DB5E95784140
91	CTC310	1751.6300000000001	Waypoint	CTC310	CTC310	\N	\N	\N	0101000000AAAF6FA6106E4040D4535867C4794140
92	CTC311	1621.8599999999999	Waypoint	CTC311	CTC311	\N	\N	\N	0101000000772A18498671404096984BD03D764140
93	CTC313	1622.8199999999999	Waypoint	CTC313	CTC313	\N	\N	\N	0101000000F710B0FD7472404074D14463A3764140
94	CTC315	1622.8199999999999	Waypoint	CTC315	CTC315	\N	\N	\N	0101000000AA9B8BBF6D7240409E06443C9D764140
95	CTC317	1629.3099999999999	Waypoint	CTC317	CTC317	\N	\N	\N	0101000000F5E2087578724040516B53A290764140
96	CTC318	1630.27	Waypoint	CTC318	CTC318	\N	\N	\N	010100000043FE2233DA724040C064FC5E77764140
97	CTC319	1612.97	Waypoint	CTC319	CTC319	\N	\N	\N	01010000004FD313088E724040C907D5B7A0764140
98	CTC320	1628.8299999999999	Waypoint	CTC320	CTC320	\N	\N	\N	0101000000B466D32DD57240403F37B89D8B764140
99	CTC322	1629.3099999999999	Waypoint	CTC322	CTC322	\N	\N	\N	01010000006490604FA0724040DB16231A76764140
100	CTC324	1628.1099999999999	Waypoint	CTC324	CTC324	\N	\N	\N	01010000005BF1AA5A94724040D8D7C4467C764140
101	CTC326	1618.97	Waypoint	CTC326	CTC326	\N	\N	\N	0101000000868B0FC57A72404036D11C965C764140
102	CTC328	1619.9300000000001	Waypoint	CTC328	CTC328	\N	\N	\N	0101000000990D9F657772404031321F1561764140
103	CTC330	1623.0599999999999	Waypoint	CTC330	CTC330	\N	\N	\N	01010000008C32E8726D724040FA419FC062764140
104	CTC332	1611.76	Waypoint	CTC332	CTC332	\N	\N	\N	01010000006365946466724040FCD32C2176764140
105	CTC334	1623.0599999999999	Waypoint	CTC334	CTC334	\N	\N	\N	01010000008FDCAC5797724040D45337599B764140
106	CTC336	1617.77	Waypoint	CTC336	CTC336	\N	\N	\N	010100000067823C1676724040FC752303AB764140
107	CTC338	1567.54	Waypoint	CTC338	CTC338	\N	\N	\N	0101000000813D63B572724040CF95E08BB1764140
108	CTC340	1604.3099999999999	Waypoint	CTC340	CTC340	\N	\N	\N	010100000016FFEE52677240400E9364AEB0764140
109	CTC342	1584.1300000000001	Waypoint	CTC342	CTC342	\N	\N	\N	0101000000920E0CA96D7240405F47C3F9B9764140
110	CTC344	1579.5599999999999	Waypoint	CTC344	CTC344	\N	\N	\N	010100000080B4A44863724040DFB58B36B5764140
111	CTC346	1602.8699999999999	Waypoint	CTC346	CTC346	\N	\N	\N	01010000004BE13741B87240403B3BDCB84C764140
112	CTC51	1583.8900000000001	Waypoint	CTC51	CTC51	\N	\N	\N	0101000000F53834AE7D71404089A980BD3A764140
113	CTC52	1583.4100000000001	Waypoint	CTC52	CTC52	\N	\N	\N	01010000004E17ABCB7E714040AC88F85935764140
114	CTC54	1583.8900000000001	Waypoint	CTC54	CTC54	\N	\N	\N	01010000004DC8B8F646724040124504C0D1764140
115	CTC55A	1545.9100000000001	Waypoint	CTC55A	CTC55A	\N	\N	\N	010100000021CA84503F7240403884046ED8764140
116	CTC55B	1549.04	Waypoint	CTC55B	CTC55B	\N	\N	\N	01010000004834F3773E724040699503B7DA764140
117	CTC56	1574.03	Waypoint	CTC56	CTC56	\N	\N	\N	0101000000EFC584896072404063228C3CDC764140
118	CTC57	1549.04	Waypoint	CTC57	CTC57	\N	\N	\N	0101000000FDE8B0CF57724040B687E8D0DC764140
119	CTC58	1612.48	Waypoint	CTC58	CTC58	\N	\N	\N	0101000000BBD9BCFD3B724040DC64BCFCFA764140
120	CTC59	1645.1700000000001	Waypoint	CTC59	CTC59	\N	\N	\N	0101000000E072F7643E724040D16DB7941B774140
121	CTC60 JDW	1642.77	Waypoint	CTC60 JDW	CTC60 JDW	\N	\N	\N	010100000061DB1C9337724040C57B8FAC1C774140
122	CTC61	1563.9400000000001	Waypoint	CTC61	CTC61	\N	\N	\N	010100000021B67030467240402A9AFF3EC0764140
123	CTC62	1564.1800000000001	Waypoint	CTC62	CTC62	\N	\N	\N	01010000009076FFFF4F7240404F679BD9B6764140
124	CTC63	1586.77	Waypoint	CTC63	CTC63	\N	\N	\N	010100000005EDD87F527240405A6B9FC3B9764140
125	CTC64	1589.8900000000001	Waypoint	CTC64	CTC64	\N	\N	\N	0101000000512B637B6172404049B4A0D4B7764140
126	CTC65	1617.53	Waypoint	CTC65	CTC65	\N	\N	\N	010100000064B12C1C71724040E907A882A7764140
127	CTC67	423.58300000000003	Waypoint	CTC67	CTC67	\N	\N	\N	010100000015999041347140402B0C7836F7754140
128	CTC68A	1620.1800000000001	Waypoint	CTC68A	CTC68A	\N	\N	\N	01010000009974BC4B67714040BFEBF402FA754140
129	CTC68B	1622.0999999999999	Waypoint	CTC68B	CTC68B	\N	\N	\N	010100000078BD48296771404057C14C34F8754140
130	CTC69	1594.9400000000001	Waypoint	CTC69	CTC69	\N	\N	\N	0101000000008DACDE5C714040ECE028C503764140
131	CTC70	1619.21	Waypoint	CTC70	CTC70	\N	\N	\N	01010000005B3EB3A04771404058243F4502764140
132	CTC71	1596.3800000000001	Waypoint	CTC71	CTC71	\N	\N	\N	010100000073164CB57C71404063337B31F8754140
133	CTC72	1619.21	Waypoint	CTC72	CTC72	\N	\N	\N	0101000000CD922BB551714040D427476EFE754140
134	CTC73	1603.1099999999999	Waypoint	CTC73	CTC73	\N	\N	\N	0101000000208E5CD47A714040AC376013F6754140
135	CTC80	1842.24	Waypoint	CTC80	CTC80	\N	\N	\N	0101000000AF2B54A17B6F404024F73CCBE8764140
136	CTC81A	1850.4100000000001	Waypoint	CTC81A	CTC81A	\N	\N	\N	01010000000FA0C85B7A6F40402C58D843E3764140
137	CTC81B	1858.3399999999999	Waypoint	CTC81B	CTC81B	\N	\N	\N	0101000000EC4EC8E5776F404018769CCBE2764140
138	CTC82	1850.8900000000001	Waypoint	CTC82	CTC82	\N	\N	\N	0101000000C6B2546F716F40406D4C5C82E4764140
139	CTC83	1831.9000000000001	Waypoint	CTC83	CTC83	\N	\N	\N	010100000047082C0C936F404017C25849E5764140
140	CTC84	1837.6700000000001	Waypoint	CTC84	CTC84	\N	\N	\N	0101000000A73264F3736F4040ECF42801D6764140
141	CTC85	1839.5899999999999	Waypoint	CTC85	CTC85	\N	\N	\N	0101000000563F48AC8B6F4040F883473CD5764140
142	CTC86	1856.4200000000001	Waypoint	CTC86	CTC86	\N	\N	\N	010100000008BFDC40646E4040D360DB214C774140
143	CTC87	1867.95	Waypoint	CTC87	CTC87	\N	\N	\N	0101000000CB1D84200A6E404061FEE4D148774140
144	CTC88	1867.23	Waypoint	CTC88	CTC88	\N	\N	\N	01010000007FA060EF136E40404F47F30C49774140
145	CTC89	1861.46	Waypoint	CTC89	CTC89	\N	\N	\N	0101000000921914720A6E40404255CB244A774140
146	CTC90	1868.6700000000001	Waypoint	CTC90	CTC90	\N	\N	\N	01010000008C15C8FB176E4040A463CC324B774140
147	CTC91	1869.4000000000001	Waypoint	CTC91	CTC91	\N	\N	\N	0101000000690105D70F6E4040F075E7C352774140
148	CTC92	1862.4300000000001	Waypoint	CTC92	CTC92	\N	\N	\N	0101000000A97E6B48186E4040BD640C4148774140
149	CTC93	1869.1500000000001	Waypoint	CTC93	CTC93	\N	\N	\N	010100000021005904166E4040722748D45A774140
150	CTC95	1885.02	Waypoint	CTC95	CTC95	\N	\N	\N	0101000000C83FBC792D6E4040E88EAF5157774140
151	DST11	1402.2	Waypoint	DST11	DST11	\N	\N	\N	010100000063F3340A907B4040E91748F7437A4140
152	DST13	1414.9400000000001	Waypoint	DST13	DST13	\N	\N	\N	0101000000B293A3FB977B4040B9B1B012397A4140
153	DST17	1409.4100000000001	Waypoint	DST17	DST17	\N	\N	\N	010100000086B003AFA27B4040C80B60FF327A4140
154	DST2	1357.02	Waypoint	DST2	DST2	\N	\N	\N	01010000005DFAC2244F7B404074FFBE6A587A4140
155	DST23	1398.5899999999999	Waypoint	DST23	DST23	\N	\N	\N	0101000000AC306412C17B4040A2BFA7A4547A4140
156	DST25	1383.45	Waypoint	DST25	DST25	\N	\N	\N	0101000000F4361074EF7B404008B3CCCC537A4140
157	DST26	1423.1099999999999	Waypoint	DST26	DST26	\N	\N	\N	0101000000A5134BC2EC7B404053EBD77F587A4140
158	DST29	1431.04	Waypoint	DST29	DST29	\N	\N	\N	01010000009964DF73F27B4040559978F3537A4140
159	DST3	1347.4000000000001	Waypoint	DST3	DST3	\N	\N	\N	010100000009442CBB507B404044423306587A4140
160	DST32	1369.27	Waypoint	DST32	DST32	\N	\N	\N	010100000025C96452727B4040639C5726507A4140
161	DST5	1368.79	Waypoint	DST5	DST5	\N	\N	\N	01010000002568B7895F7B4040BD911B734E7A4140
162	DST7	1388.98	Waypoint	DST7	DST7	\N	\N	\N	010100000031D62094837B40405738A764497A4140
163	PSEMBIN	125.337	Waypoint	PSEMBIN	PSEMBIN	\N	\N	\N	010100000041BF8CB501AC4040B13AA79603614140
164	PSEMFLDWL	121.97199999999999	Waypoint	PSEMFLDWL	PSEMFLDWL	\N	\N	\N	0101000000162827AFD5AB40401AB1C35618614140
165	PSEMSHDS1	122.693	Waypoint	PSEMSHDS1	PSEMSHDS1	\N	\N	\N	010100000093BF3CE3C9AB4040FDF82A7D11614140
166	PSEMSOC+Q	111.398	Waypoint	PSEMSOC+Q	PSEMSOC+Q	\N	\N	\N	0101000000A5AB9C3F2BAC404038F6F62B00614140
167	PSEMSWHRL	121.251	Waypoint	PSEMSWHRL	PSEMSWHRL	\N	\N	\N	0101000000017F4CF9FDAB40409562B47FFF604140
168	PSEMTOMB	113.801	Waypoint	PSEMTOMB	PSEMTOMB	\N	\N	\N	0101000000EBEDCC8D34AC40408DC714C50E614140
169	PSEMUPPOT	123.17400000000001	Waypoint	PSEMUPPOT	PSEMUPPOT	\N	\N	\N	010100000063EF62EDF1AB4040C6A0FF5D20614140
170	PSEMWALL	125.81699999999999	Waypoint	PSEMWALL	PSEMWALL	\N	\N	\N	010100000080E6B8C2D7AB40404190F36623614140
171	PSEMWALL2	121.011	Waypoint	PSEMWALL2	PSEMWALL2	\N	\N	\N	01010000005A75B3C5C9AB40409F814CC314614140
172	SCV1	1182.3	Waypoint	SCV1	SCV1	\N	\N	\N	01010000008E792C9C755740408A8DDC472C804140
173	SCV10	1174.8499999999999	Waypoint	SCV10	SCV10	\N	\N	\N	010100000069EB700CB257404046DA004B32804140
174	SCV100	1109.96	Waypoint	SCV100	SCV100	\N	\N	\N	01010000004967C8C7DA574040EACDF487307E4140
175	SCV106	1115.73	Waypoint	SCV106	SCV106	\N	\N	\N	01010000005E46A76ADF57404001219422327E4140
176	SCV107	1103.71	Waypoint	SCV107	SCV107	\N	\N	\N	01010000008F89AB02E95740401CEFE48C0E7E4140
177	SCV108	1132.79	Waypoint	SCV108	SCV108	\N	\N	\N	010100000010808F2C265840400A146F6CCF7E4140
178	SCV11	1183.74	Waypoint	SCV11	SCV11	\N	\N	\N	0101000000EA318FD1815740404853636022804140
179	SCV110	1137.8399999999999	Waypoint	SCV110	SCV110	\N	\N	\N	01010000006600F1B8FD5740403F8BE764507E4140
180	SCV12	1103.71	Waypoint	SCV12	SCV12	\N	\N	\N	01010000004C0C2FAC8E57404054289B0A22804140
181	SCV120	1073.9100000000001	Waypoint	SCV120	SCV120	\N	\N	\N	0101000000A4AF803F05584040FFA778E28D7E4140
182	SCV121	1261.6099999999999	Waypoint	SCV121	SCV121	\N	\N	\N	0101000000DF28777423584040A908A76DD27E4140
183	SCV122	1127.5	Waypoint	SCV122	SCV122	\N	\N	\N	01010000008476938721584040C845DB4FCB7E4140
184	SCV123	1127.5	Waypoint	SCV123	SCV123	\N	\N	\N	0101000000B27BB80507584040D7074DA3AD7E4140
185	SCV125	1153.7	Waypoint	SCV125	SCV125	\N	\N	\N	0101000000A80FABDFCF574040E6A638D4907E4140
186	SCV126	1159.23	Waypoint	SCV126	SCV126	\N	\N	\N	01010000001EDD5419D05740404780FB83867E4140
187	SCV13	1190.71	Waypoint	SCV13	SCV13	\N	\N	\N	0101000000AF34C32293574040F95224D11E804140
188	SCV130	1242.3800000000001	Waypoint	SCV130	SCV130	\N	\N	\N	0101000000F9D734E7C75740405A7537FEED7E4140
189	SCV131	1264.73	Waypoint	SCV131	SCV131	\N	\N	\N	01010000006153178BC757404066844F24EC7E4140
190	SCV134	1251.03	Waypoint	SCV134	SCV134	\N	\N	\N	0101000000ED792734C3574040E28AB422E47E4140
191	SCV135	1242.1400000000001	Waypoint	SCV135	SCV135	\N	\N	\N	01010000003F1F0753C45740408593B8B7F07E4140
192	SCV136	1224.1199999999999	Waypoint	SCV136	SCV136	\N	\N	\N	0101000000CC31DF15CF574040CA06112AEA7E4140
193	SCV137	1216.6700000000001	Waypoint	SCV137	SCV137	\N	\N	\N	0101000000957DBFE7B3574040B011F372F97E4140
194	SCV138	1146.73	Waypoint	SCV138	SCV138	\N	\N	\N	010100000008153B6B0E584040FF95077CCD7E4140
195	SCV14	1228.6800000000001	Waypoint	SCV14	SCV14	\N	\N	\N	0101000000DF0563A78C5740407490303C11804140
196	SCV15	1184.9400000000001	Waypoint	SCV15	SCV15	\N	\N	\N	0101000000B04B105290574040B05CD4C60F804140
197	SCV16	1159.47	Waypoint	SCV16	SCV16	\N	\N	\N	0101000000D53763AAB657404053886BA81D804140
198	SCV17	1157.3	Waypoint	SCV17	SCV17	\N	\N	\N	0101000000333B7442B4574040E203A89A3A804140
199	SCV18	1157.3	Waypoint	SCV18	SCV18	\N	\N	\N	0101000000B389EF6BB4574040986BCC493A804140
200	SCV2	1211.3800000000001	Waypoint	SCV2	SCV2	\N	\N	\N	01010000009BFF6EB96E574040B3B124D628804140
201	SCV21	1187.3399999999999	Waypoint	SCV21	SCV21	\N	\N	\N	0101000000348D446D7D574040D0DCC4420D804140
202	SCV25	1162.3499999999999	Waypoint	SCV25	SCV25	\N	\N	\N	0101000000EB018BADF457404087C328C148804140
203	SCV26	1392.3399999999999	Waypoint	SCV26	SCV26	\N	\N	\N	0101000000DFEF40321757404012C4A4B85D7F4140
204	SCV27	1397.1500000000001	Waypoint	SCV27	SCV27	\N	\N	\N	0101000000AB16F77020574040AE7E20045A7F4140
205	SCV28	1400.52	Waypoint	SCV28	SCV28	\N	\N	\N	0101000000F6F18E5B25574040C414A0FE577F4140
206	SCV29	1389.46	Waypoint	SCV29	SCV29	\N	\N	\N	0101000000AB65ECFD265740408E4110E8587F4140
207	SCV3	1389.46	Waypoint	SCV3	SCV3	\N	\N	\N	01010000000502901F19724040F0D85854C7014240
208	SCV30	1399.0699999999999	Waypoint	SCV30	SCV30	\N	\N	\N	010100000011F940882A574040FD899795507F4140
209	SCV31	1413.49	Waypoint	SCV31	SCV31	\N	\N	\N	0101000000B4F2D21A31574040350E25F5467F4140
210	SCV32	1414.7	Waypoint	SCV32	SCV32	\N	\N	\N	0101000000AB57572638574040863668FE447F4140
211	SCV33	1411.0899999999999	Waypoint	SCV33	SCV33	\N	\N	\N	0101000000118E38E83B574040782DF3E6487F4140
212	SCV34	1425.75	Waypoint	SCV34	SCV34	\N	\N	\N	0101000000B19684093857404028FF0CCF487F4140
213	SCV35	1401.96	Waypoint	SCV35	SCV35	\N	\N	\N	0101000000100E3FFD3C574040E48D10413C7F4140
214	SCV36	1392.3399999999999	Waypoint	SCV36	SCV36	\N	\N	\N	0101000000567498ED12574040FE15B3D75B7F4140
215	SCV37	1394.51	Waypoint	SCV37	SCV37	\N	\N	\N	0101000000C82227F520574040F30C0DD0657F4140
216	SCV38	1405.5599999999999	Waypoint	SCV38	SCV38	\N	\N	\N	01010000008908B39422574040CDFE1080577F4140
217	SCV39	1388.02	Waypoint	SCV39	SCV39	\N	\N	\N	01010000009ECAB8951D5740402A0B0B605D7F4140
218	SCV4	1191.1900000000001	Waypoint	SCV4	SCV4	\N	\N	\N	0101000000B808590469574040CD1211BC29804140
219	SCV40	1396.9100000000001	Waypoint	SCV40	SCV40	\N	\N	\N	0101000000BF13C742335740404E291C323F7F4140
220	SCV41	1407.73	Waypoint	SCV41	SCV41	\N	\N	\N	0101000000A39A80C9C7564040C562DB6F9B7F4140
221	SCV42	1398.1099999999999	Waypoint	SCV42	SCV42	\N	\N	\N	0101000000BF460476C4564040431D602A997F4140
222	SCV43	1400.28	Waypoint	SCV43	SCV43	\N	\N	\N	0101000000D4FA749FD0564040C53333F68F7F4140
223	SCV44	1414.9400000000001	Waypoint	SCV44	SCV44	\N	\N	\N	0101000000482E9FAFD0564040073CCB548A7F4140
224	SCV45	1394.75	Waypoint	SCV45	SCV45	\N	\N	\N	010100000081B20849CF564040550BF95B937F4140
225	SCV46	1394.03	Waypoint	SCV46	SCV46	\N	\N	\N	0101000000ABEDE4DB145740405E7070635F7F4140
226	SCV47	1385.8599999999999	Waypoint	SCV47	SCV47	\N	\N	\N	0101000000E550243701574040326704BB647F4140
227	SCV48	1378.4100000000001	Waypoint	SCV48	SCV48	\N	\N	\N	01010000004C6914CAF6564040DF7B0B2D637F4140
228	SCV49	1380.3299999999999	Waypoint	SCV49	SCV49	\N	\N	\N	0101000000D301F5D8E9564040BEFF7877707F4140
229	SCV50	1369.03	Waypoint	SCV50	SCV50	\N	\N	\N	010100000064B234C7245740405F3F483F6F7F4140
230	SCV51	1390.6600000000001	Waypoint	SCV51	SCV51	\N	\N	\N	0101000000408224E4EA564040FEB0401C6D7F4140
231	SCV52	1401.48	Waypoint	SCV52	SCV52	\N	\N	\N	0101000000F9574FE0EF564040DFE7F0F7687F4140
232	SCV53	1401.48	Waypoint	SCV53	SCV53	\N	\N	\N	0101000000BC96071EDC5640407055744C9F7F4140
233	SCV54	1387.0599999999999	Waypoint	SCV54	SCV54	\N	\N	\N	0101000000F3E9901FDA56404085F72C3A9F7F4140
234	SCV55	1378.1700000000001	Waypoint	SCV55	SCV55	\N	\N	\N	01010000000AE0BCF1DB564040DAB0FCB2A17F4140
235	SCV56	1399.55	Waypoint	SCV56	SCV56	\N	\N	\N	0101000000C22ECC3A0A5740400418A369617F4140
236	SCV57	1382.97	Waypoint	SCV57	SCV57	\N	\N	\N	01010000008830FF9A0C574040FFCF8C3E597F4140
237	SCV58	1382.97	Waypoint	SCV58	SCV58	\N	\N	\N	0101000000289C546908574040D6023930527F4140
238	SCV59	1382.97	Waypoint	SCV59	SCV59	\N	\N	\N	0101000000280B806D14574040C6DDD4CB657F4140
239	SCV6	1123.1800000000001	Waypoint	SCV6	SCV6	\N	\N	\N	0101000000BF22801A855740407F5F307352804140
240	SCV60	1376	Waypoint	SCV60	SCV60	\N	\N	\N	01010000001EF6A0721C574040FECD305A6C7F4140
241	SCV61	1376	Waypoint	SCV61	SCV61	\N	\N	\N	010100000078C59863155740409AF49E9A707F4140
242	SCV62A	1376	Waypoint	SCV62A	SCV62A	\N	\N	\N	010100000093C6B4AB1A574040C20A69ED737F4140
243	SCV62B	1416.6199999999999	Waypoint	SCV62B	SCV62B	\N	\N	\N	0101000000B68B3B6ED0564040A41F4C0B907F4140
244	SCV63	1361.8199999999999	Waypoint	SCV63	SCV63	\N	\N	\N	010100000075D518812C5740407EEEE034787F4140
245	SCV65	1379.1300000000001	Waypoint	SCV65	SCV65	\N	\N	\N	01010000008BD1A08CE4564040BE1BD35FA47F4140
246	SCV66	1397.3900000000001	Waypoint	SCV66	SCV66	\N	\N	\N	010100000087DBE8363D5740407225840C397F4140
247	SCV67	1404.3599999999999	Waypoint	SCV67	SCV67	\N	\N	\N	01010000007C11E98E3057404037F36CD53C7F4140
248	SCV69	1237.3299999999999	Waypoint	SCV69	SCV69	\N	\N	\N	01010000000AA6A4CBBE574040279A6068107F4140
249	SCV69 A	1218.3499999999999	Waypoint	SCV69 A	SCV69 A	\N	\N	\N	0101000000B6E3D46EBE574040AD3E63CE0F7F4140
250	SCV7	1131.5899999999999	Waypoint	SCV7	SCV7	\N	\N	\N	0101000000C9EFDC978D574040AE85689354804140
251	SCV70	1211.3800000000001	Waypoint	SCV70	SCV70	\N	\N	\N	0101000000DF0A6336C1574040E1D4F0F5017F4140
252	SCV71	1182.78	Waypoint	SCV71	SCV71	\N	\N	\N	0101000000890FB44ED1574040CC72BB92027F4140
253	SCV72	1182.78	Waypoint	SCV72	SCV72	\N	\N	\N	0101000000A81BC00CDA57404075EB142FFF7E4140
254	SCV73	1231.0799999999999	Waypoint	SCV73	SCV73	\N	\N	\N	01010000002DBDA0FFCA574040578194B7E77E4140
255	SCV74	1234.21	Waypoint	SCV74	SCV74	\N	\N	\N	01010000003CC3B841CB57404033FC56D4E87E4140
256	SCV76	1373.5999999999999	Waypoint	SCV76	SCV76	\N	\N	\N	01010000000B73FCFCE957404022EB6401377F4140
257	SCV77	1225.5599999999999	Waypoint	SCV77	SCV77	\N	\N	\N	01010000003C129732DC57404056951373247F4140
258	SCV78	1207.53	Waypoint	SCV78	SCV78	\N	\N	\N	0101000000BC6F845EE2574040D12794B3277F4140
259	SCV79	1225.3199999999999	Waypoint	SCV79	SCV79	\N	\N	\N	0101000000AD14F846D1574040037C4F962E7F4140
260	SCV8	1208.97	Waypoint	SCV8	SCV8	\N	\N	\N	0101000000E3C674BBB35740405645678229804140
261	SCV80	1243.3399999999999	Waypoint	SCV80	SCV80	\N	\N	\N	0101000000CEC5C85ACF574040D0DCD826347F4140
262	SCV81	1263.05	Waypoint	SCV81	SCV81	\N	\N	\N	010100000033FE36F1B95740400990F763407F4140
263	SCV82	1229.8800000000001	Waypoint	SCV82	SCV82	\N	\N	\N	01010000002D851C68B95740408C520FBF457F4140
264	SCV83	1255.8399999999999	Waypoint	SCV83	SCV83	\N	\N	\N	0101000000BA7384B3AB574040ADAF48284A7F4140
265	SCV84	1265.45	Waypoint	SCV84	SCV84	\N	\N	\N	0101000000440F636FB5574040110005DF407F4140
266	SCV86	1109.48	Waypoint	SCV86	SCV86	\N	\N	\N	0101000000541F1034DB5740402A45DCB02E7E4140
267	SCV87	1081.3599999999999	Waypoint	SCV87	SCV87	\N	\N	\N	0101000000F7963FCDF35740405EE6E8AF5F7E4140
268	SCV88	1121.01	Waypoint	SCV88	SCV88	\N	\N	\N	0101000000AD19F8D5055840402B384713AB7E4140
269	SCV89	1134.47	Waypoint	SCV89	SCV89	\N	\N	\N	0101000000462D0C12D45740409167CC618D7E4140
270	SCV9	1191.6700000000001	Waypoint	SCV9	SCV9	\N	\N	\N	0101000000646963C9B5574040A796D3BC28804140
271	SCV91	1167.8800000000001	Waypoint	SCV91	SCV91	\N	\N	\N	0101000000D579A08AE557404096444C420F7E4140
272	SCV92	1143.3699999999999	Waypoint	SCV92	SCV92	\N	\N	\N	0101000000B19F287D1F584040E999389BC97E4140
273	SCV93	1146.97	Waypoint	SCV93	SCV93	\N	\N	\N	010100000088AC081313584040B252E88CCD7E4140
274	SCV94	1131.3499999999999	Waypoint	SCV94	SCV94	\N	\N	\N	01010000001792707A07584040C8FB9E7CAD7E4140
275	SCV95	1173.1700000000001	Waypoint	SCV95	SCV95	\N	\N	\N	0101000000F90BDDEF0758404047600CE2CC7E4140
276	SCV96	1127.98	Waypoint	SCV96	SCV96	\N	\N	\N	01010000007AFB0635E25740402138D8E22D7E4140
277	SCV97	1115.49	Waypoint	SCV97	SCV97	\N	\N	\N	01010000004428771EE3574040B61E53B1107E4140
278	SCV98	1117.6500000000001	Waypoint	SCV98	SCV98	\N	\N	\N	0101000000028A68A6015840405D03FDB3567E4140
279	SCV99	1143.3699999999999	Waypoint	SCV99	SCV99	\N	\N	\N	0101000000E1A4A8B477574040898AA33828804140
\.


--
-- Data for Name: brita; Type: TABLE DATA; Schema: public; Owner: webuser
--

COPY brita (gid, name, elevation, symbol, "comment", descriptio, source, url, "url name", the_geom) FROM stdin;
0	001	85.922899999999998	Flag	001	001	\N	\N	\N	010100000027991B5AC4A540403862D73D0C634140
1	002	1504.3399999999999	Flag	002	002	\N	\N	\N	01010000005B11FD26897040409B7F4796C4784140
2	ALAMSC	298.613	Waypoint	ALAMSC	ALAMSC	\N	\N	\N	0101000000DC3287745DB34040F52B470F567D4140
3	CHA111	509.13999999999999	Flag	CHA111	CHA111	\N	\N	\N	01010000001CCF206BB1844140A751B886E85E4040
4	CHA15	518.27200000000005	Flag	CHA15	CHA15	\N	\N	\N	0101000000436B94E1B784414055A09F74E55E4040
5	CHF1	521.39700000000005	Flag	CHF1	CHF1	\N	\N	\N	01010000007EF47BD2B1844140D1467BAA2D5F4040
6	CHF10	518.51300000000003	Flag	CHF10	CHF10	\N	\N	\N	0101000000FC7B7C84D2844140747A0455285F4040
7	CHF11	510.58199999999999	Flag	CHF11	CHF11	\N	\N	\N	0101000000E0ECE815D584414062DA83851D5F4040
8	CHF13	518.99400000000003	Flag	CHF13	CHF13	\N	\N	\N	010100000030885778A88441405979E80C235F4040
9	CHF14	516.59000000000003	Flag	CHF14	CHF14	\N	\N	\N	0101000000D40DF822B8844140200CF068195F4040
10	CHF15	515.14800000000002	Flag	CHF15	CHF15	\N	\N	\N	0101000000F7720CB9B38441403C78F08A155F4040
11	CHF16	517.31100000000004	Flag	CHF16	CHF16	\N	\N	\N	01010000001AE1FC6CB58441404D4F3C8D125F4040
12	CHF17	489.673	Flag	CHF17	CHF17	\N	\N	\N	0101000000D464ECA2AD84414046F9B450135F4040
13	CHF2	520.67600000000004	Flag	CHF2	CHF2	\N	\N	\N	010100000044D678BBAE84414061956B072C5F4040
14	CHF3	520.67600000000004	Flag	CHF3	CHF3	\N	\N	\N	01010000004A29C7E8A9844140B1434B0A2B5F4040
15	CHF4	528.60699999999997	Flag	CHF4	CHF4	\N	\N	\N	010100000001345409B28441402F07ACE22E5F4040
16	CHF5	520.67600000000004	Flag	CHF5	CHF5	\N	\N	\N	010100000012F1E8DCB38441405175C05C285F4040
17	CHF6	533.65300000000002	Flag	CHF6	CHF6	\N	\N	\N	0101000000761FFC9BC284414079BFD4462A5F4040
18	CHF7	518.99400000000003	Flag	CHF7	CHF7	\N	\N	\N	010100000024F7B892C4844140D60B45872E5F4040
19	CHF8	505.77600000000001	Flag	CHF8	CHF8	\N	\N	\N	01010000005EAAB309D98441404EC71FE92C5F4040
20	CHF9	505.77600000000001	Flag	CHF9	CHF9	\N	\N	\N	0101000000EC0A8068DB844140B66B2BBE2D5F4040
21	CTC CO	1726.8800000000001	Waypoint	CTC CO	CTC CO	\N	\N	\N	010100000082696C3D307040408BC14CA60D764140
22	CTC JE	1638.4400000000001	Waypoint	CTC JE	CTC JE	\N	\N	\N	0101000000AA50D0327A724040D29ADF6342774140
23	CTC100	1853.77	Waypoint	CTC100	CTC100	\N	\N	\N	0101000000810269AB046E40402991C86747774140
24	CTC101	1853.77	Waypoint	CTC101	CTC101	\N	\N	\N	01010000001C376CC3F76D40409705BF7F46774140
25	CTC102	1853.77	Waypoint	CTC102	CTC102	\N	\N	\N	0101000000EF0CED78E06D4040BC1FF4C256774140
26	CTC12	788.64099999999996	Waypoint	CTC12	CTC12	\N	\N	\N	01010000001622B791F85240404BF7A08B397A4140
27	CTC13	812.43399999999997	Waypoint	CTC13	CTC13	\N	\N	\N	01010000005A24881BF95240402BF470EB367A4140
28	CTC130	1806.4300000000001	Waypoint	CTC130	CTC130	\N	\N	\N	0101000000FC0700AA126F404088014DAF4D784140
29	CTC131	1777.8299999999999	Waypoint	CTC131	CTC131	\N	\N	\N	010100000079F150A4136F4040770435514B784140
30	CTC132	1777.8299999999999	Waypoint	CTC132	CTC132	\N	\N	\N	010100000026AF2432D97440409E0607D9C4844140
31	CTC133	1792.49	Waypoint	CTC133	CTC133	\N	\N	\N	0101000000D0B53459116F4040590ABD0857784140
32	CTC135	1816.28	Waypoint	CTC135	CTC135	\N	\N	\N	01010000004C3943970E6F40403AEF3E4848784140
33	CTC136	1793.45	Waypoint	CTC136	CTC136	\N	\N	\N	010100000094F26CEC186F4040C8D8E0AF4F784140
34	CTC137	1809.55	Waypoint	CTC137	CTC137	\N	\N	\N	0101000000CE8C53B4FF6E4040807F87F840784140
35	CTC138	1810.03	Waypoint	CTC138	CTC138	\N	\N	\N	010100000050EC78FEFA6E4040AFCEC4834C784140
36	CTC139	1825.9000000000001	Waypoint	CTC139	CTC139	\N	\N	\N	010100000018394325F96E404029BEDC5247784140
37	CTC14	807.86699999999996	Waypoint	CTC14	CTC14	\N	\N	\N	010100000015EE6CFAF952404095AC6C1A397A4140
38	CTC140	1793.45	Waypoint	CTC140	CTC140	\N	\N	\N	010100000065E2C896F46E40400EDAB04841784140
39	CTC141	1840.8	Waypoint	CTC141	CTC141	\N	\N	\N	01010000005C678319016F404078F8B488FD774140
40	CTC142	1828.0599999999999	Waypoint	CTC142	CTC142	\N	\N	\N	0101000000F7076CFCF96E40403DADD0771F784140
41	CTC143	1842.24	Waypoint	CTC143	CTC143	\N	\N	\N	0101000000DE0F080C036F40402604E0DCF5774140
42	CTC144	1822.77	Waypoint	CTC144	CTC144	\N	\N	\N	0101000000367DF7400B6F40404B17DCBB38784140
43	CTC145	1830.46	Waypoint	CTC145	CTC145	\N	\N	\N	0101000000DAEDA6A00A6F4040C0688C5F03784140
44	CTC146	1830.46	Waypoint	CTC146	CTC146	\N	\N	\N	01010000009072BB8D096F4040F4C1DC35FB774140
45	CTC147	1841.52	Waypoint	CTC147	CTC147	\N	\N	\N	0101000000700FDF15036F404087D8006FF8774140
46	CTC15	1756.9200000000001	Waypoint	CTC15	CTC15	\N	\N	\N	0101000000FD00C745456F40404ADC7C8664784140
47	CTC150	1725.2	Waypoint	CTC150	CTC150	\N	\N	\N	010100000016A65CD23270404027EE50CB11764140
48	CTC151	1717.75	Waypoint	CTC151	CTC151	\N	\N	\N	01010000003D03CC770E70404056F01874FB754140
49	CTC152	1735.77	Waypoint	CTC152	CTC152	\N	\N	\N	0101000000AA411C142270404077135BC50B764140
50	CTC153	1720.3900000000001	Waypoint	CTC153	CTC153	\N	\N	\N	010100000095FB106000704040230B899500764140
51	CTC154	1711.74	Waypoint	CTC154	CTC154	\N	\N	\N	01010000003AD548B5097040404FD138A4F6754140
52	CTC155	1724.72	Waypoint	CTC155	CTC155	\N	\N	\N	01010000002EB2F8A81A704040BD92C8640F764140
53	CTC156	1715.3399999999999	Waypoint	CTC156	CTC156	\N	\N	\N	0101000000CA28DB2FFB6F40404F2EAC6CF6754140
54	CTC157	1715.3399999999999	Waypoint	CTC157	CTC157	\N	\N	\N	0101000000E763B32D077040400382E0C1F4754140
55	CTC158	1761.97	Waypoint	CTC158	CTC158	\N	\N	\N	0101000000D52F182902704040744328F61D764140
56	CTC159	1717.51	Waypoint	CTC159	CTC159	\N	\N	\N	01010000003FE2940FFA6F4040302B7CCCF3754140
57	CTC16	1755.24	Waypoint	CTC16	CTC16	\N	\N	\N	01010000001AEEAAA7596F404096253F8466784140
58	CTC160	1708.6199999999999	Waypoint	CTC160	CTC160	\N	\N	\N	010100000019F27252047040408ECEBCC6F5754140
59	CTC161	1748.03	Waypoint	CTC161	CTC161	\N	\N	\N	01010000008DB4085009704040359A3B7E18764140
60	CTC162	1768.22	Waypoint	CTC162	CTC162	\N	\N	\N	01010000006469173BF06F40406F35FF7016764140
61	CTC163	1702.6099999999999	Waypoint	CTC163	CTC163	\N	\N	\N	0101000000CA3DCC7A0B70404073CDA07EF0754140
62	CTC165	1787.4400000000001	Waypoint	CTC165	CTC165	\N	\N	\N	01010000008E9897CBDF6F4040562E1C5417764140
63	CTC166	1705.97	Waypoint	CTC166	CTC166	\N	\N	\N	0101000000D0EB246217704040EFBC9487F3754140
64	CTC167	1748.03	Waypoint	CTC167	CTC167	\N	\N	\N	0101000000B88437FCD26F404005B1647508764140
65	CTC168	1806.4300000000001	Waypoint	CTC168	CTC168	\N	\N	\N	01010000003F7F448DAE6F40406DB51C220D764140
66	CTC169	1775.6700000000001	Waypoint	CTC169	CTC169	\N	\N	\N	01010000005CDC0C58EE6F40406443B3C21F764140
67	CTC17	1749.71	Waypoint	CTC17	CTC17	\N	\N	\N	0101000000E37AEB2E566F4040E650DF6260784140
68	CTC170	1775.4300000000001	Waypoint	CTC170	CTC170	\N	\N	\N	0101000000C40B5744E36F4040ED03984B0D764140
69	CTC171	1848.01	Waypoint	CTC171	CTC171	\N	\N	\N	0101000000DD5ED7D1E66D404056CB386E3A774140
70	CTC172	1832.3800000000001	Waypoint	CTC172	CTC172	\N	\N	\N	01010000004377EB2AD46D40403380DB063A774140
71	CTC173	1775.4300000000001	Waypoint	CTC173	CTC173	\N	\N	\N	01010000007106D006D86D4040A7B19C084B774140
72	CTC174	1870.3599999999999	Waypoint	CTC174	CTC174	\N	\N	\N	010100000013923786E16D40402001857541774140
73	CTC175	1850.1700000000001	Waypoint	CTC175	CTC175	\N	\N	\N	01010000007D1DE71FC56D40406FAF883E38774140
74	CTC176	1775.4300000000001	Waypoint	CTC176	CTC176	\N	\N	\N	01010000007106D006D86D4040A7B19C084B774140
75	CTC177	1852.3299999999999	Waypoint	CTC177	CTC177	\N	\N	\N	01010000001F44DCE3DF6D404093FACE0943774140
76	CTC178	1841.04	Waypoint	CTC178	CTC178	\N	\N	\N	0101000000AFCE2C67C16D40401360BB2838774140
77	CTC179	1859.3	Waypoint	CTC179	CTC179	\N	\N	\N	0101000000E265A4E3CC6D4040AAEDB4A23E774140
78	CTC18	1754.04	Waypoint	CTC18	CTC18	\N	\N	\N	010100000071C1005F5D6F4040E5FFA48D62784140
79	CTC180	1867.71	Waypoint	CTC180	CTC180	\N	\N	\N	0101000000292BF08FE36D40403D071F1542774140
80	CTC181	1837.9100000000001	Waypoint	CTC181	CTC181	\N	\N	\N	0101000000EFF61E03B96D4040D31F7B6C34774140
81	CTC182	1859.54	Waypoint	CTC182	CTC182	\N	\N	\N	010100000023932B73B56D4040466E50723C774140
82	CTC183	1870.3599999999999	Waypoint	CTC183	CTC183	\N	\N	\N	0101000000E9704CCDE06D4040C708645347774140
83	CTC184	1848.97	Waypoint	CTC184	CTC184	\N	\N	\N	01010000005FD5B4E6226E40400771CC4F3D774140
84	CTC185	1851.3699999999999	Waypoint	CTC185	CTC185	\N	\N	\N	0101000000B4BBD0F4436E404011B8B0993C774140
85	CTC186	1867.95	Waypoint	CTC186	CTC186	\N	\N	\N	01010000003C6AC396DB6D4040757D179D49774140
86	CTC19	1754.28	Waypoint	CTC19	CTC19	\N	\N	\N	01010000004B0540D55B6F4040E23AB7EA69784140
87	CTC20	1761.73	Waypoint	CTC20	CTC20	\N	\N	\N	01010000000F891091396F40404885ACCC66784140
88	CTC200	1697.8	Waypoint	CTC200	CTC200	\N	\N	\N	0101000000B68B57C37F7240402E631F7103784140
89	CTC21	1756.9200000000001	Waypoint	CTC21	CTC21	\N	\N	\N	0101000000B90C8B93336F404028EB28E865784140
90	CTC22	1772.54	Waypoint	CTC22	CTC22	\N	\N	\N	010100000095732C66396F4040E68BC0CF6D784140
91	CTC23	1799.22	Waypoint	CTC23	CTC23	\N	\N	\N	0101000000E76DA420436F4040B5C3FC687A784140
92	CTC24	1789.8499999999999	Waypoint	CTC24	CTC24	\N	\N	\N	0101000000D3C54824416F4040C1A1103180784140
93	CTC25	1787.2	Waypoint	CTC25	CTC25	\N	\N	\N	0101000000775CC4DF456F40403C110B4F84784140
94	CTC26	1789.1300000000001	Waypoint	CTC26	CTC26	\N	\N	\N	010100000027CD30FF326F4040CD6234BB86784140
95	CTC27	1782.6400000000001	Waypoint	CTC27	CTC27	\N	\N	\N	0101000000E695F0262D6F40409549AFFB7E784140
96	CTC28	1785.04	Waypoint	CTC28	CTC28	\N	\N	\N	0101000000BC05E793226F40404156DB4086784140
97	CTC29	1784.0799999999999	Waypoint	CTC29	CTC29	\N	\N	\N	0101000000ADD6BCBC166F40405E8B34F687784140
98	CTC30	1798.74	Waypoint	CTC30	CTC30	\N	\N	\N	010100000090892C83FE6E4040A11C8BC191784140
99	CTC31	1804.27	Waypoint	CTC31	CTC31	\N	\N	\N	01010000008D4F70CDF76E40406F71DB5E95784140
100	CTC51	1583.8900000000001	Waypoint	CTC51	CTC51	\N	\N	\N	0101000000F53834AE7D71404089A980BD3A764140
101	CTC52	1583.4100000000001	Waypoint	CTC52	CTC52	\N	\N	\N	01010000004E17ABCB7E714040AC88F85935764140
102	CTC54	1583.8900000000001	Waypoint	CTC54	CTC54	\N	\N	\N	01010000004DC8B8F646724040124504C0D1764140
103	CTC55A	1545.9100000000001	Waypoint	CTC55A	CTC55A	\N	\N	\N	010100000021CA84503F7240403884046ED8764140
104	CTC55B	1549.04	Waypoint	CTC55B	CTC55B	\N	\N	\N	01010000004834F3773E724040699503B7DA764140
105	CTC56	1574.03	Waypoint	CTC56	CTC56	\N	\N	\N	0101000000EFC584896072404063228C3CDC764140
106	CTC57	1549.04	Waypoint	CTC57	CTC57	\N	\N	\N	0101000000FDE8B0CF57724040B687E8D0DC764140
107	CTC58	1612.48	Waypoint	CTC58	CTC58	\N	\N	\N	0101000000BBD9BCFD3B724040DC64BCFCFA764140
108	CTC59	1645.1700000000001	Waypoint	CTC59	CTC59	\N	\N	\N	0101000000E072F7643E724040D16DB7941B774140
109	CTC60	1642.77	Waypoint	CTC60	CTC60	\N	\N	\N	010100000061DB1C9337724040C57B8FAC1C774140
110	CTC61	1563.9400000000001	Waypoint	CTC61	CTC61	\N	\N	\N	010100000021B67030467240402A9AFF3EC0764140
111	CTC62	1564.1800000000001	Waypoint	CTC62	CTC62	\N	\N	\N	01010000009076FFFF4F7240404F679BD9B6764140
112	CTC63	1586.77	Waypoint	CTC63	CTC63	\N	\N	\N	010100000005EDD87F527240405A6B9FC3B9764140
113	CTC64	1589.8900000000001	Waypoint	CTC64	CTC64	\N	\N	\N	0101000000512B637B6172404049B4A0D4B7764140
114	CTC65	1617.53	Waypoint	CTC65	CTC65	\N	\N	\N	010100000064B12C1C71724040E907A882A7764140
115	CTC67	423.58300000000003	Waypoint	CTC67	CTC67	\N	\N	\N	010100000015999041347140402B0C7836F7754140
116	CTC68A	1620.1800000000001	Waypoint	CTC68A	CTC68A	\N	\N	\N	01010000009974BC4B67714040BFEBF402FA754140
117	CTC68B	1622.0999999999999	Waypoint	CTC68B	CTC68B	\N	\N	\N	010100000078BD48296771404057C14C34F8754140
118	CTC69	1594.9400000000001	Waypoint	CTC69	CTC69	\N	\N	\N	0101000000008DACDE5C714040ECE028C503764140
119	CTC70	1619.21	Waypoint	CTC70	CTC70	\N	\N	\N	01010000005B3EB3A04771404058243F4502764140
120	CTC71	1596.3800000000001	Waypoint	CTC71	CTC71	\N	\N	\N	010100000073164CB57C71404063337B31F8754140
121	CTC72	1619.21	Waypoint	CTC72	CTC72	\N	\N	\N	0101000000CD922BB551714040D427476EFE754140
122	CTC73	1603.1099999999999	Waypoint	CTC73	CTC73	\N	\N	\N	0101000000208E5CD47A714040AC376013F6754140
123	CTC80	1842.24	Waypoint	CTC80	CTC80	\N	\N	\N	0101000000AF2B54A17B6F404024F73CCBE8764140
124	CTC81A	1850.4100000000001	Waypoint	CTC81A	CTC81A	\N	\N	\N	01010000000FA0C85B7A6F40402C58D843E3764140
125	CTC81B	1858.3399999999999	Waypoint	CTC81B	CTC81B	\N	\N	\N	0101000000EC4EC8E5776F404018769CCBE2764140
126	CTC82	1850.8900000000001	Waypoint	CTC82	CTC82	\N	\N	\N	0101000000C6B2546F716F40406D4C5C82E4764140
127	CTC83	1831.9000000000001	Waypoint	CTC83	CTC83	\N	\N	\N	010100000047082C0C936F404017C25849E5764140
128	CTC84	1837.6700000000001	Waypoint	CTC84	CTC84	\N	\N	\N	0101000000A73264F3736F4040ECF42801D6764140
129	CTC85	1839.5899999999999	Waypoint	CTC85	CTC85	\N	\N	\N	0101000000563F48AC8B6F4040F883473CD5764140
130	CTC86	1856.4200000000001	Waypoint	CTC86	CTC86	\N	\N	\N	010100000008BFDC40646E4040D360DB214C774140
131	CTC87	1867.95	Waypoint	CTC87	CTC87	\N	\N	\N	0101000000CB1D84200A6E404061FEE4D148774140
132	CTC88	1867.23	Waypoint	CTC88	CTC88	\N	\N	\N	01010000007FA060EF136E40404F47F30C49774140
133	CTC89	1861.46	Waypoint	CTC89	CTC89	\N	\N	\N	0101000000921914720A6E40404255CB244A774140
134	CTC90	1868.6700000000001	Waypoint	CTC90	CTC90	\N	\N	\N	01010000008C15C8FB176E4040A463CC324B774140
135	CTC91	1869.4000000000001	Waypoint	CTC91	CTC91	\N	\N	\N	0101000000690105D70F6E4040F075E7C352774140
136	CTC92	1862.4300000000001	Waypoint	CTC92	CTC92	\N	\N	\N	0101000000A97E6B48186E4040BD640C4148774140
137	CTC93	1869.1500000000001	Waypoint	CTC93	CTC93	\N	\N	\N	010100000021005904166E4040722748D45A774140
138	CTC95	1885.02	Waypoint	CTC95	CTC95	\N	\N	\N	0101000000C83FBC792D6E4040E88EAF5157774140
139	CTCA	1503.6199999999999	Flag	CTCA	CTCA	\N	\N	\N	01010000004C4B686F8970404066E697FBC3784140
140	EKO1	700.20100000000002	Flag	EKO1	EKO1	\N	\N	\N	010100000055FBF003028F41404490C72FE3B93F40
141	GARMIN	325.04899999999998	Flag	GARMIN	GARMIN	\N	\N	\N	01010000003581CE1623B357C013B87FA9826D4340
142	GRMEUR	35.934699999999999	Flag	GRMEUR	GRMEUR	\N	\N	\N	01010000001E909861226CF7BF229CA71ECF7D4940
143	GRMTWN	38.097700000000003	Flag	GRMTWN	GRMTWN	\N	\N	\N	01010000001E631221FA685E40183ACF08D10F3940
144	MAA1	352.20600000000002	Flag	MAA1	MAA1	\N	\N	\N	0101000000B212F3ACE49541405508F53E5F5D4040
145	MAA2	169.07599999999999	Flag	MAA2	MAA2	\N	\N	\N	01010000009DAD7710DF954140FB610FE3715D4040
146	MMR1	833.10199999999998	Flag	MMR1	MMR1	\N	\N	\N	0101000000F7621BFDD7B141400192C051F87B4040
147	MMR10	869.63199999999995	Flag	MMR10	MMR10	\N	\N	\N	01010000007BFAA018EDB14140BD319FD4E07B4040
148	MMR11	850.88599999999997	Flag	MMR11	MMR11	\N	\N	\N	010100000041BB73C3D7B1414083DCF450E37B4040
149	MMR12	852.32799999999997	Flag	MMR12	MMR12	\N	\N	\N	0101000000703E1FACD9B141405D7DA78FE17B4040
150	MMR2	835.505	Flag	MMR2	MMR2	\N	\N	\N	0101000000625830B7DCB14140896A0025F47B4040
151	MMR4	832.62099999999998	Flag	MMR4	MMR4	\N	\N	\N	010100000055CFB48ADFB141406BE46C9AF47B4040
152	MMR5	837.66800000000001	Flag	MMR5	MMR5	\N	\N	\N	010100000076E39B75DFB14140C9BE5401F17B4040
153	MMR6	831.65999999999997	Flag	MMR6	MMR6	\N	\N	\N	010100000080E79F5FE2B14140F707206EF47B4040
154	MMR7	851.60699999999997	Flag	MMR7	MMR7	\N	\N	\N	0101000000D65D8F78E8B141406F4BF3ADE07B4040
155	MMR8	859.298	Flag	MMR8	MMR8	\N	\N	\N	010100000041799454EAB141405BCCC0E2DF7B4040
156	MMR9	866.26700000000005	Flag	MMR9	MMR9	\N	\N	\N	0101000000132A3303E7B14140640D0F48DF7B4040
157	NHC1	435.60000000000002	Flag	NHC1	NHC1	\N	\N	\N	01010000000D296C2A3E834140C59AC7EA21C03F40
158	NHC2	430.79300000000001	Flag	NHC2	NHC2	\N	\N	\N	0101000000F5F85FDC3D834140708D60C725C03F40
159	NHC4	439.685	Flag	NHC4	NHC4	\N	\N	\N	0101000000271E73453983414077765FB951C03F40
160	NHC5	446.17399999999998	Flag	NHC5	NHC5	\N	\N	\N	0101000000E9A0E80D398341400C6D29B551C03F40
161	NHC6	438.964	Flag	NHC6	NHC6	\N	\N	\N	0101000000F89D243233834140E91B293F4FC03F40
162	NHC7	453.14400000000001	Flag	NHC7	NHC7	\N	\N	\N	0101000000B0939C413383414018FC47F050C03F40
163	NHC8	466.84199999999998	Flag	NHC8	NHC8	\N	\N	\N	0101000000C70F650D3683414041D2771C5EC03F40
164	NHC9	459.15199999999999	Flag	NHC9	NHC9	\N	\N	\N	0101000000BBFC12E724834140E974C0787DC03F40
165	NHM1	696.596	Flag	NHM1	NHM1	\N	\N	\N	01010000007258E7062D87414064E0CFDC82BF3F40
166	NHM2	693.952	Flag	NHM2	NHM2	\N	\N	\N	0101000000E47DB7A12B874140865AF98386BF3F40
167	NHM3	700.92200000000003	Flag	NHM3	NHM3	\N	\N	\N	010100000057982B743F87414088C2808881BF3F40
168	NHM4	699.96000000000004	Flag	NHM4	NHM4	\N	\N	\N	0101000000D8A080043D874140E87CF7158BBF3F40
169	NHM5	701.64300000000003	Flag	NHM5	NHM5	\N	\N	\N	01010000009F9C10563D874140426D194596BF3F40
170	NHM6	707.40999999999997	Flag	NHM6	NHM6	\N	\N	\N	01010000009E561BAD3487414079D166BB93BF3F40
171	NHM7	699.24000000000001	Flag	NHM7	NHM7	\N	\N	\N	0101000000AE079B532C874140695F804A83BF3F40
172	NHM8	711.01499999999999	Flag	NHM8	NHM8	\N	\N	\N	01010000005C1D474623884140324049514CBE3F40
173	NHM9	688.90499999999997	Flag	NHM9	NHM9	\N	\N	\N	0101000000D6F2A7E622884140827DA16B52BE3F40
174	NHS1	642.28200000000004	Flag	NHS1	NHS1	\N	\N	\N	01010000009115EF0C02864140FB45D608E7BD3F40
175	NHS2	643.00300000000004	Flag	NHS2	NHS2	\N	\N	\N	0101000000EAB9BF594E8641404A12EE0F40BE3F40
176	NHS3	648.28999999999996	Flag	NHS3	NHS3	\N	\N	\N	0101000000427800004A8641406D3BB91B4EBE3F40
177	NHS4	647.08799999999997	Flag	NHS4	NHS4	\N	\N	\N	01010000000B45C411478641401F78A0414FBE3F40
178	SCV1	1182.3	Waypoint	SCV1	SCV1	\N	\N	\N	01010000008E792C9C755740408A8DDC472C804140
179	SCV10	1174.8499999999999	Waypoint	SCV10	SCV10	\N	\N	\N	010100000069EB700CB257404046DA004B32804140
180	SCV100	1109.96	Waypoint	SCV100	SCV100	\N	\N	\N	01010000004967C8C7DA574040EACDF487307E4140
181	SCV106	1115.73	Waypoint	SCV106	SCV106	\N	\N	\N	01010000005E46A76ADF57404001219422327E4140
182	SCV107	1103.71	Waypoint	SCV107	SCV107	\N	\N	\N	01010000008F89AB02E95740401CEFE48C0E7E4140
183	SCV108	1132.79	Waypoint	SCV108	SCV108	\N	\N	\N	010100000010808F2C265840400A146F6CCF7E4140
184	SCV11	1183.74	Waypoint	SCV11	SCV11	\N	\N	\N	0101000000EA318FD1815740404853636022804140
185	SCV110	1137.8399999999999	Waypoint	SCV110	SCV110	\N	\N	\N	01010000006600F1B8FD5740403F8BE764507E4140
186	SCV12	1103.71	Waypoint	SCV12	SCV12	\N	\N	\N	01010000004C0C2FAC8E57404054289B0A22804140
187	SCV120	1073.9100000000001	Waypoint	SCV120	SCV120	\N	\N	\N	0101000000A4AF803F05584040FFA778E28D7E4140
188	SCV121	1261.6099999999999	Waypoint	SCV121	SCV121	\N	\N	\N	0101000000DF28777423584040A908A76DD27E4140
189	SCV122	1127.5	Waypoint	SCV122	SCV122	\N	\N	\N	01010000008476938721584040C845DB4FCB7E4140
190	SCV123	1127.5	Waypoint	SCV123	SCV123	\N	\N	\N	0101000000B27BB80507584040D7074DA3AD7E4140
191	SCV125	1153.7	Waypoint	SCV125	SCV125	\N	\N	\N	0101000000A80FABDFCF574040E6A638D4907E4140
192	SCV126	1159.23	Waypoint	SCV126	SCV126	\N	\N	\N	01010000001EDD5419D05740404780FB83867E4140
193	SCV13	1190.71	Waypoint	SCV13	SCV13	\N	\N	\N	0101000000AF34C32293574040F95224D11E804140
194	SCV130	1242.3800000000001	Waypoint	SCV130	SCV130	\N	\N	\N	0101000000F9D734E7C75740405A7537FEED7E4140
195	SCV131	1264.73	Waypoint	SCV131	SCV131	\N	\N	\N	01010000006153178BC757404066844F24EC7E4140
196	SCV134	1251.03	Waypoint	SCV134	SCV134	\N	\N	\N	0101000000ED792734C3574040E28AB422E47E4140
197	SCV135	1242.1400000000001	Waypoint	SCV135	SCV135	\N	\N	\N	01010000003F1F0753C45740408593B8B7F07E4140
198	SCV136	1224.1199999999999	Waypoint	SCV136	SCV136	\N	\N	\N	0101000000CC31DF15CF574040CA06112AEA7E4140
199	SCV137	1216.6700000000001	Waypoint	SCV137	SCV137	\N	\N	\N	0101000000957DBFE7B3574040B011F372F97E4140
200	SCV138	1146.73	Waypoint	SCV138	SCV138	\N	\N	\N	010100000008153B6B0E584040FF95077CCD7E4140
201	SCV14	1228.6800000000001	Waypoint	SCV14	SCV14	\N	\N	\N	0101000000DF0563A78C5740407490303C11804140
202	SCV15	1184.9400000000001	Waypoint	SCV15	SCV15	\N	\N	\N	0101000000B04B105290574040B05CD4C60F804140
203	SCV16	1159.47	Waypoint	SCV16	SCV16	\N	\N	\N	0101000000D53763AAB657404053886BA81D804140
204	SCV17	1157.3	Waypoint	SCV17	SCV17	\N	\N	\N	0101000000333B7442B4574040E203A89A3A804140
205	SCV18	1157.3	Waypoint	SCV18	SCV18	\N	\N	\N	0101000000B389EF6BB4574040986BCC493A804140
206	SCV2	1211.3800000000001	Waypoint	SCV2	SCV2	\N	\N	\N	01010000009BFF6EB96E574040B3B124D628804140
207	SCV21	1187.3399999999999	Waypoint	SCV21	SCV21	\N	\N	\N	0101000000348D446D7D574040D0DCC4420D804140
208	SCV25	1162.3499999999999	Waypoint	SCV25	SCV25	\N	\N	\N	0101000000EB018BADF457404087C328C148804140
209	SCV26	1392.3399999999999	Waypoint	SCV26	SCV26	\N	\N	\N	0101000000DFEF40321757404012C4A4B85D7F4140
210	SCV27	1397.1500000000001	Waypoint	SCV27	SCV27	\N	\N	\N	0101000000AB16F77020574040AE7E20045A7F4140
211	SCV28	1400.52	Waypoint	SCV28	SCV28	\N	\N	\N	0101000000F6F18E5B25574040C414A0FE577F4140
212	SCV29	1389.46	Waypoint	SCV29	SCV29	\N	\N	\N	0101000000AB65ECFD265740408E4110E8587F4140
213	SCV3	1389.46	Waypoint	SCV3	SCV3	\N	\N	\N	01010000000502901F19724040F0D85854C7014240
214	SCV30	1399.0699999999999	Waypoint	SCV30	SCV30	\N	\N	\N	010100000011F940882A574040FD899795507F4140
215	SCV31	1413.49	Waypoint	SCV31	SCV31	\N	\N	\N	0101000000B4F2D21A31574040350E25F5467F4140
216	SCV32	1414.7	Waypoint	SCV32	SCV32	\N	\N	\N	0101000000AB57572638574040863668FE447F4140
217	SCV33	1411.0899999999999	Waypoint	SCV33	SCV33	\N	\N	\N	0101000000118E38E83B574040782DF3E6487F4140
218	SCV34	1425.75	Waypoint	SCV34	SCV34	\N	\N	\N	0101000000B19684093857404028FF0CCF487F4140
219	SCV35	1401.96	Waypoint	SCV35	SCV35	\N	\N	\N	0101000000100E3FFD3C574040E48D10413C7F4140
220	SCV36	1392.3399999999999	Waypoint	SCV36	SCV36	\N	\N	\N	0101000000567498ED12574040FE15B3D75B7F4140
221	SCV37	1394.51	Waypoint	SCV37	SCV37	\N	\N	\N	0101000000C82227F520574040F30C0DD0657F4140
222	SCV38	1405.5599999999999	Waypoint	SCV38	SCV38	\N	\N	\N	01010000008908B39422574040CDFE1080577F4140
223	SCV39	1388.02	Waypoint	SCV39	SCV39	\N	\N	\N	01010000009ECAB8951D5740402A0B0B605D7F4140
224	SCV4	1191.1900000000001	Waypoint	SCV4	SCV4	\N	\N	\N	0101000000B808590469574040CD1211BC29804140
225	SCV40	1396.9100000000001	Waypoint	SCV40	SCV40	\N	\N	\N	0101000000BF13C742335740404E291C323F7F4140
226	SCV41	1407.73	Waypoint	SCV41	SCV41	\N	\N	\N	0101000000A39A80C9C7564040C562DB6F9B7F4140
227	SCV42	1398.1099999999999	Waypoint	SCV42	SCV42	\N	\N	\N	0101000000BF460476C4564040431D602A997F4140
228	SCV43	1400.28	Waypoint	SCV43	SCV43	\N	\N	\N	0101000000D4FA749FD0564040C53333F68F7F4140
229	SCV44	1414.9400000000001	Waypoint	SCV44	SCV44	\N	\N	\N	0101000000482E9FAFD0564040073CCB548A7F4140
230	SCV45	1394.75	Waypoint	SCV45	SCV45	\N	\N	\N	010100000081B20849CF564040550BF95B937F4140
231	SCV46	1394.03	Waypoint	SCV46	SCV46	\N	\N	\N	0101000000ABEDE4DB145740405E7070635F7F4140
232	SCV47	1385.8599999999999	Waypoint	SCV47	SCV47	\N	\N	\N	0101000000E550243701574040326704BB647F4140
233	SCV48	1378.4100000000001	Waypoint	SCV48	SCV48	\N	\N	\N	01010000004C6914CAF6564040DF7B0B2D637F4140
234	SCV49	1380.3299999999999	Waypoint	SCV49	SCV49	\N	\N	\N	0101000000D301F5D8E9564040BEFF7877707F4140
235	SCV50	1369.03	Waypoint	SCV50	SCV50	\N	\N	\N	010100000064B234C7245740405F3F483F6F7F4140
236	SCV51	1390.6600000000001	Waypoint	SCV51	SCV51	\N	\N	\N	0101000000408224E4EA564040FEB0401C6D7F4140
237	SCV52	1401.48	Waypoint	SCV52	SCV52	\N	\N	\N	0101000000F9574FE0EF564040DFE7F0F7687F4140
238	SCV53	1401.48	Waypoint	SCV53	SCV53	\N	\N	\N	0101000000BC96071EDC5640407055744C9F7F4140
239	SCV54	1387.0599999999999	Waypoint	SCV54	SCV54	\N	\N	\N	0101000000F3E9901FDA56404085F72C3A9F7F4140
240	SCV55	1378.1700000000001	Waypoint	SCV55	SCV55	\N	\N	\N	01010000000AE0BCF1DB564040DAB0FCB2A17F4140
241	SCV56	1399.55	Waypoint	SCV56	SCV56	\N	\N	\N	0101000000C22ECC3A0A5740400418A369617F4140
242	SCV57	1382.97	Waypoint	SCV57	SCV57	\N	\N	\N	01010000008830FF9A0C574040FFCF8C3E597F4140
243	SCV58	1382.97	Waypoint	SCV58	SCV58	\N	\N	\N	0101000000289C546908574040D6023930527F4140
244	SCV59	1382.97	Waypoint	SCV59	SCV59	\N	\N	\N	0101000000280B806D14574040C6DDD4CB657F4140
245	SCV6	1123.1800000000001	Waypoint	SCV6	SCV6	\N	\N	\N	0101000000BF22801A855740407F5F307352804140
246	SCV60	1376	Waypoint	SCV60	SCV60	\N	\N	\N	01010000001EF6A0721C574040FECD305A6C7F4140
247	SCV61	1376	Waypoint	SCV61	SCV61	\N	\N	\N	010100000078C59863155740409AF49E9A707F4140
248	SCV62A	1376	Waypoint	SCV62A	SCV62A	\N	\N	\N	010100000093C6B4AB1A574040C20A69ED737F4140
249	SCV62B	1416.6199999999999	Waypoint	SCV62B	SCV62B	\N	\N	\N	0101000000B68B3B6ED0564040A41F4C0B907F4140
250	SCV63	1361.8199999999999	Waypoint	SCV63	SCV63	\N	\N	\N	010100000075D518812C5740407EEEE034787F4140
251	SCV65	1379.1300000000001	Waypoint	SCV65	SCV65	\N	\N	\N	01010000008BD1A08CE4564040BE1BD35FA47F4140
252	SCV66	1397.3900000000001	Waypoint	SCV66	SCV66	\N	\N	\N	010100000087DBE8363D5740407225840C397F4140
253	SCV67	1404.3599999999999	Waypoint	SCV67	SCV67	\N	\N	\N	01010000007C11E98E3057404037F36CD53C7F4140
254	SCV69	1237.3299999999999	Waypoint	SCV69	SCV69	\N	\N	\N	01010000000AA6A4CBBE574040279A6068107F4140
255	SCV69	1218.3499999999999	Waypoint	SCV69	SCV69	\N	\N	\N	0101000000B6E3D46EBE574040AD3E63CE0F7F4140
256	SCV7	1131.5899999999999	Waypoint	SCV7	SCV7	\N	\N	\N	0101000000C9EFDC978D574040AE85689354804140
257	SCV70	1211.3800000000001	Waypoint	SCV70	SCV70	\N	\N	\N	0101000000DF0A6336C1574040E1D4F0F5017F4140
258	SCV71	1182.78	Waypoint	SCV71	SCV71	\N	\N	\N	0101000000890FB44ED1574040CC72BB92027F4140
259	SCV72	1182.78	Waypoint	SCV72	SCV72	\N	\N	\N	0101000000A81BC00CDA57404075EB142FFF7E4140
260	SCV73	1231.0799999999999	Waypoint	SCV73	SCV73	\N	\N	\N	01010000002DBDA0FFCA574040578194B7E77E4140
261	SCV74	1234.21	Waypoint	SCV74	SCV74	\N	\N	\N	01010000003CC3B841CB57404033FC56D4E87E4140
262	SCV76	1373.5999999999999	Waypoint	SCV76	SCV76	\N	\N	\N	01010000000B73FCFCE957404022EB6401377F4140
263	SCV77	1225.5599999999999	Waypoint	SCV77	SCV77	\N	\N	\N	01010000003C129732DC57404056951373247F4140
264	SCV78	1207.53	Waypoint	SCV78	SCV78	\N	\N	\N	0101000000BC6F845EE2574040D12794B3277F4140
265	SCV79	1225.3199999999999	Waypoint	SCV79	SCV79	\N	\N	\N	0101000000AD14F846D1574040037C4F962E7F4140
266	SCV8	1208.97	Waypoint	SCV8	SCV8	\N	\N	\N	0101000000E3C674BBB35740405645678229804140
267	SCV80	1243.3399999999999	Waypoint	SCV80	SCV80	\N	\N	\N	0101000000CEC5C85ACF574040D0DCD826347F4140
268	SCV81	1263.05	Waypoint	SCV81	SCV81	\N	\N	\N	010100000033FE36F1B95740400990F763407F4140
269	SCV82	1229.8800000000001	Waypoint	SCV82	SCV82	\N	\N	\N	01010000002D851C68B95740408C520FBF457F4140
270	SCV83	1255.8399999999999	Waypoint	SCV83	SCV83	\N	\N	\N	0101000000BA7384B3AB574040ADAF48284A7F4140
271	SCV84	1265.45	Waypoint	SCV84	SCV84	\N	\N	\N	0101000000440F636FB5574040110005DF407F4140
272	SCV86	1109.48	Waypoint	SCV86	SCV86	\N	\N	\N	0101000000541F1034DB5740402A45DCB02E7E4140
273	SCV87	1081.3599999999999	Waypoint	SCV87	SCV87	\N	\N	\N	0101000000F7963FCDF35740405EE6E8AF5F7E4140
274	SCV88	1121.01	Waypoint	SCV88	SCV88	\N	\N	\N	0101000000AD19F8D5055840402B384713AB7E4140
275	SCV89	1134.47	Waypoint	SCV89	SCV89	\N	\N	\N	0101000000462D0C12D45740409167CC618D7E4140
276	SCV9	1191.6700000000001	Waypoint	SCV9	SCV9	\N	\N	\N	0101000000646963C9B5574040A796D3BC28804140
277	SCV91	1167.8800000000001	Waypoint	SCV91	SCV91	\N	\N	\N	0101000000D579A08AE557404096444C420F7E4140
278	SCV92	1143.3699999999999	Waypoint	SCV92	SCV92	\N	\N	\N	0101000000B19F287D1F584040E999389BC97E4140
279	SCV93	1146.97	Waypoint	SCV93	SCV93	\N	\N	\N	010100000088AC081313584040B252E88CCD7E4140
280	SCV94	1131.3499999999999	Waypoint	SCV94	SCV94	\N	\N	\N	01010000001792707A07584040C8FB9E7CAD7E4140
281	SCV95	1173.1700000000001	Waypoint	SCV95	SCV95	\N	\N	\N	0101000000F90BDDEF0758404047600CE2CC7E4140
282	SCV96	1127.98	Waypoint	SCV96	SCV96	\N	\N	\N	01010000007AFB0635E25740402138D8E22D7E4140
283	SCV97	1115.49	Waypoint	SCV97	SCV97	\N	\N	\N	01010000004428771EE3574040B61E53B1107E4140
284	SCV98	1117.6500000000001	Waypoint	SCV98	SCV98	\N	\N	\N	0101000000028A68A6015840405D03FDB3567E4140
285	SCV99	1143.3699999999999	Waypoint	SCV99	SCV99	\N	\N	\N	0101000000E1A4A8B477574040898AA33828804140
\.


--
-- Data for Name: brita2; Type: TABLE DATA; Schema: public; Owner: webuser
--

COPY brita2 (gid, name, elevation, symbol, "comment", descriptio, source, url, "url name", the_geom) FROM stdin;
0	001	85.922899999999998	Flag	001	001	\N	\N	\N	010100000027991B5AC4A540403862D73D0C634140
1	002	1504.3399999999999	Flag	002	002	\N	\N	\N	01010000005B11FD26897040409B7F4796C4784140
2	ALAMSC	298.613	Waypoint	ALAMSC	ALAMSC	\N	\N	\N	0101000000DC3287745DB34040F52B470F567D4140
3	CHA111	509.13999999999999	Flag	CHA111	CHA111	\N	\N	\N	01010000001CCF206BB1844140A751B886E85E4040
4	CHA15	518.27200000000005	Flag	CHA15	CHA15	\N	\N	\N	0101000000436B94E1B784414055A09F74E55E4040
5	CHF1	521.39700000000005	Flag	CHF1	CHF1	\N	\N	\N	01010000007EF47BD2B1844140D1467BAA2D5F4040
6	CHF10	518.51300000000003	Flag	CHF10	CHF10	\N	\N	\N	0101000000FC7B7C84D2844140747A0455285F4040
7	CHF11	510.58199999999999	Flag	CHF11	CHF11	\N	\N	\N	0101000000E0ECE815D584414062DA83851D5F4040
8	CHF13	518.99400000000003	Flag	CHF13	CHF13	\N	\N	\N	010100000030885778A88441405979E80C235F4040
9	CHF14	516.59000000000003	Flag	CHF14	CHF14	\N	\N	\N	0101000000D40DF822B8844140200CF068195F4040
10	CHF15	515.14800000000002	Flag	CHF15	CHF15	\N	\N	\N	0101000000F7720CB9B38441403C78F08A155F4040
11	CHF16	517.31100000000004	Flag	CHF16	CHF16	\N	\N	\N	01010000001AE1FC6CB58441404D4F3C8D125F4040
12	CHF17	489.673	Flag	CHF17	CHF17	\N	\N	\N	0101000000D464ECA2AD84414046F9B450135F4040
13	CHF2	520.67600000000004	Flag	CHF2	CHF2	\N	\N	\N	010100000044D678BBAE84414061956B072C5F4040
14	CHF3	520.67600000000004	Flag	CHF3	CHF3	\N	\N	\N	01010000004A29C7E8A9844140B1434B0A2B5F4040
15	CHF4	528.60699999999997	Flag	CHF4	CHF4	\N	\N	\N	010100000001345409B28441402F07ACE22E5F4040
16	CHF5	520.67600000000004	Flag	CHF5	CHF5	\N	\N	\N	010100000012F1E8DCB38441405175C05C285F4040
17	CHF6	533.65300000000002	Flag	CHF6	CHF6	\N	\N	\N	0101000000761FFC9BC284414079BFD4462A5F4040
18	CHF7	518.99400000000003	Flag	CHF7	CHF7	\N	\N	\N	010100000024F7B892C4844140D60B45872E5F4040
19	CHF8	505.77600000000001	Flag	CHF8	CHF8	\N	\N	\N	01010000005EAAB309D98441404EC71FE92C5F4040
20	CHF9	505.77600000000001	Flag	CHF9	CHF9	\N	\N	\N	0101000000EC0A8068DB844140B66B2BBE2D5F4040
21	CTC CO	1726.8800000000001	Waypoint	CTC CO	CTC CO	\N	\N	\N	010100000082696C3D307040408BC14CA60D764140
22	CTC JE	1638.4400000000001	Waypoint	CTC JE	CTC JE	\N	\N	\N	0101000000AA50D0327A724040D29ADF6342774140
23	CTC100	1853.77	Waypoint	CTC100	CTC100	\N	\N	\N	0101000000810269AB046E40402991C86747774140
24	CTC101	1853.77	Waypoint	CTC101	CTC101	\N	\N	\N	01010000001C376CC3F76D40409705BF7F46774140
25	CTC102	1853.77	Waypoint	CTC102	CTC102	\N	\N	\N	0101000000EF0CED78E06D4040BC1FF4C256774140
26	CTC12	788.64099999999996	Waypoint	CTC12	CTC12	\N	\N	\N	01010000001622B791F85240404BF7A08B397A4140
27	CTC13	812.43399999999997	Waypoint	CTC13	CTC13	\N	\N	\N	01010000005A24881BF95240402BF470EB367A4140
28	CTC130	1806.4300000000001	Waypoint	CTC130	CTC130	\N	\N	\N	0101000000FC0700AA126F404088014DAF4D784140
29	CTC131	1777.8299999999999	Waypoint	CTC131	CTC131	\N	\N	\N	010100000079F150A4136F4040770435514B784140
30	CTC132	1777.8299999999999	Waypoint	CTC132	CTC132	\N	\N	\N	010100000026AF2432D97440409E0607D9C4844140
31	CTC133	1792.49	Waypoint	CTC133	CTC133	\N	\N	\N	0101000000D0B53459116F4040590ABD0857784140
32	CTC135	1816.28	Waypoint	CTC135	CTC135	\N	\N	\N	01010000004C3943970E6F40403AEF3E4848784140
33	CTC136	1793.45	Waypoint	CTC136	CTC136	\N	\N	\N	010100000094F26CEC186F4040C8D8E0AF4F784140
34	CTC137	1809.55	Waypoint	CTC137	CTC137	\N	\N	\N	0101000000CE8C53B4FF6E4040807F87F840784140
35	CTC138	1810.03	Waypoint	CTC138	CTC138	\N	\N	\N	010100000050EC78FEFA6E4040AFCEC4834C784140
36	CTC139	1825.9000000000001	Waypoint	CTC139	CTC139	\N	\N	\N	010100000018394325F96E404029BEDC5247784140
37	CTC14	807.86699999999996	Waypoint	CTC14	CTC14	\N	\N	\N	010100000015EE6CFAF952404095AC6C1A397A4140
38	CTC140	1793.45	Waypoint	CTC140	CTC140	\N	\N	\N	010100000065E2C896F46E40400EDAB04841784140
39	CTC141	1840.8	Waypoint	CTC141	CTC141	\N	\N	\N	01010000005C678319016F404078F8B488FD774140
40	CTC142	1828.0599999999999	Waypoint	CTC142	CTC142	\N	\N	\N	0101000000F7076CFCF96E40403DADD0771F784140
41	CTC143	1842.24	Waypoint	CTC143	CTC143	\N	\N	\N	0101000000DE0F080C036F40402604E0DCF5774140
42	CTC144	1822.77	Waypoint	CTC144	CTC144	\N	\N	\N	0101000000367DF7400B6F40404B17DCBB38784140
43	CTC145	1830.46	Waypoint	CTC145	CTC145	\N	\N	\N	0101000000DAEDA6A00A6F4040C0688C5F03784140
44	CTC146	1830.46	Waypoint	CTC146	CTC146	\N	\N	\N	01010000009072BB8D096F4040F4C1DC35FB774140
45	CTC147	1841.52	Waypoint	CTC147	CTC147	\N	\N	\N	0101000000700FDF15036F404087D8006FF8774140
46	CTC15	1756.9200000000001	Waypoint	CTC15	CTC15	\N	\N	\N	0101000000FD00C745456F40404ADC7C8664784140
47	CTC150	1725.2	Waypoint	CTC150	CTC150	\N	\N	\N	010100000016A65CD23270404027EE50CB11764140
48	CTC151	1717.75	Waypoint	CTC151	CTC151	\N	\N	\N	01010000003D03CC770E70404056F01874FB754140
49	CTC152	1735.77	Waypoint	CTC152	CTC152	\N	\N	\N	0101000000AA411C142270404077135BC50B764140
50	CTC153	1720.3900000000001	Waypoint	CTC153	CTC153	\N	\N	\N	010100000095FB106000704040230B899500764140
51	CTC154	1711.74	Waypoint	CTC154	CTC154	\N	\N	\N	01010000003AD548B5097040404FD138A4F6754140
52	CTC155	1724.72	Waypoint	CTC155	CTC155	\N	\N	\N	01010000002EB2F8A81A704040BD92C8640F764140
53	CTC156	1715.3399999999999	Waypoint	CTC156	CTC156	\N	\N	\N	0101000000CA28DB2FFB6F40404F2EAC6CF6754140
54	CTC157	1715.3399999999999	Waypoint	CTC157	CTC157	\N	\N	\N	0101000000E763B32D077040400382E0C1F4754140
55	CTC158	1761.97	Waypoint	CTC158	CTC158	\N	\N	\N	0101000000D52F182902704040744328F61D764140
56	CTC159	1717.51	Waypoint	CTC159	CTC159	\N	\N	\N	01010000003FE2940FFA6F4040302B7CCCF3754140
57	CTC16	1755.24	Waypoint	CTC16	CTC16	\N	\N	\N	01010000001AEEAAA7596F404096253F8466784140
58	CTC160	1708.6199999999999	Waypoint	CTC160	CTC160	\N	\N	\N	010100000019F27252047040408ECEBCC6F5754140
59	CTC161	1748.03	Waypoint	CTC161	CTC161	\N	\N	\N	01010000008DB4085009704040359A3B7E18764140
60	CTC162	1768.22	Waypoint	CTC162	CTC162	\N	\N	\N	01010000006469173BF06F40406F35FF7016764140
61	CTC163	1702.6099999999999	Waypoint	CTC163	CTC163	\N	\N	\N	0101000000CA3DCC7A0B70404073CDA07EF0754140
62	CTC165	1787.4400000000001	Waypoint	CTC165	CTC165	\N	\N	\N	01010000008E9897CBDF6F4040562E1C5417764140
63	CTC166	1705.97	Waypoint	CTC166	CTC166	\N	\N	\N	0101000000D0EB246217704040EFBC9487F3754140
64	CTC167	1748.03	Waypoint	CTC167	CTC167	\N	\N	\N	0101000000B88437FCD26F404005B1647508764140
65	CTC168	1806.4300000000001	Waypoint	CTC168	CTC168	\N	\N	\N	01010000003F7F448DAE6F40406DB51C220D764140
66	CTC169	1775.6700000000001	Waypoint	CTC169	CTC169	\N	\N	\N	01010000005CDC0C58EE6F40406443B3C21F764140
67	CTC17	1749.71	Waypoint	CTC17	CTC17	\N	\N	\N	0101000000E37AEB2E566F4040E650DF6260784140
68	CTC170	1775.4300000000001	Waypoint	CTC170	CTC170	\N	\N	\N	0101000000C40B5744E36F4040ED03984B0D764140
69	CTC171	1848.01	Waypoint	CTC171	CTC171	\N	\N	\N	0101000000DD5ED7D1E66D404056CB386E3A774140
70	CTC172	1832.3800000000001	Waypoint	CTC172	CTC172	\N	\N	\N	01010000004377EB2AD46D40403380DB063A774140
71	CTC173	1775.4300000000001	Waypoint	CTC173	CTC173	\N	\N	\N	01010000007106D006D86D4040A7B19C084B774140
72	CTC174	1870.3599999999999	Waypoint	CTC174	CTC174	\N	\N	\N	010100000013923786E16D40402001857541774140
73	CTC175	1850.1700000000001	Waypoint	CTC175	CTC175	\N	\N	\N	01010000007D1DE71FC56D40406FAF883E38774140
74	CTC176	1775.4300000000001	Waypoint	CTC176	CTC176	\N	\N	\N	01010000007106D006D86D4040A7B19C084B774140
75	CTC177	1852.3299999999999	Waypoint	CTC177	CTC177	\N	\N	\N	01010000001F44DCE3DF6D404093FACE0943774140
76	CTC178	1841.04	Waypoint	CTC178	CTC178	\N	\N	\N	0101000000AFCE2C67C16D40401360BB2838774140
77	CTC179	1859.3	Waypoint	CTC179	CTC179	\N	\N	\N	0101000000E265A4E3CC6D4040AAEDB4A23E774140
78	CTC18	1754.04	Waypoint	CTC18	CTC18	\N	\N	\N	010100000071C1005F5D6F4040E5FFA48D62784140
79	CTC180	1867.71	Waypoint	CTC180	CTC180	\N	\N	\N	0101000000292BF08FE36D40403D071F1542774140
80	CTC181	1837.9100000000001	Waypoint	CTC181	CTC181	\N	\N	\N	0101000000EFF61E03B96D4040D31F7B6C34774140
81	CTC182	1859.54	Waypoint	CTC182	CTC182	\N	\N	\N	010100000023932B73B56D4040466E50723C774140
82	CTC183	1870.3599999999999	Waypoint	CTC183	CTC183	\N	\N	\N	0101000000E9704CCDE06D4040C708645347774140
83	CTC184	1848.97	Waypoint	CTC184	CTC184	\N	\N	\N	01010000005FD5B4E6226E40400771CC4F3D774140
84	CTC185	1851.3699999999999	Waypoint	CTC185	CTC185	\N	\N	\N	0101000000B4BBD0F4436E404011B8B0993C774140
85	CTC186	1867.95	Waypoint	CTC186	CTC186	\N	\N	\N	01010000003C6AC396DB6D4040757D179D49774140
86	CTC19	1754.28	Waypoint	CTC19	CTC19	\N	\N	\N	01010000004B0540D55B6F4040E23AB7EA69784140
87	CTC20	1761.73	Waypoint	CTC20	CTC20	\N	\N	\N	01010000000F891091396F40404885ACCC66784140
88	CTC200	1697.8	Waypoint	CTC200	CTC200	\N	\N	\N	0101000000B68B57C37F7240402E631F7103784140
89	CTC21	1756.9200000000001	Waypoint	CTC21	CTC21	\N	\N	\N	0101000000B90C8B93336F404028EB28E865784140
90	CTC22	1772.54	Waypoint	CTC22	CTC22	\N	\N	\N	010100000095732C66396F4040E68BC0CF6D784140
91	CTC23	1799.22	Waypoint	CTC23	CTC23	\N	\N	\N	0101000000E76DA420436F4040B5C3FC687A784140
92	CTC24	1789.8499999999999	Waypoint	CTC24	CTC24	\N	\N	\N	0101000000D3C54824416F4040C1A1103180784140
93	CTC25	1787.2	Waypoint	CTC25	CTC25	\N	\N	\N	0101000000775CC4DF456F40403C110B4F84784140
94	CTC26	1789.1300000000001	Waypoint	CTC26	CTC26	\N	\N	\N	010100000027CD30FF326F4040CD6234BB86784140
95	CTC27	1782.6400000000001	Waypoint	CTC27	CTC27	\N	\N	\N	0101000000E695F0262D6F40409549AFFB7E784140
96	CTC28	1785.04	Waypoint	CTC28	CTC28	\N	\N	\N	0101000000BC05E793226F40404156DB4086784140
97	CTC29	1784.0799999999999	Waypoint	CTC29	CTC29	\N	\N	\N	0101000000ADD6BCBC166F40405E8B34F687784140
98	CTC30	1798.74	Waypoint	CTC30	CTC30	\N	\N	\N	010100000090892C83FE6E4040A11C8BC191784140
99	CTC31	1804.27	Waypoint	CTC31	CTC31	\N	\N	\N	01010000008D4F70CDF76E40406F71DB5E95784140
100	CTC51	1583.8900000000001	Waypoint	CTC51	CTC51	\N	\N	\N	0101000000F53834AE7D71404089A980BD3A764140
101	CTC52	1583.4100000000001	Waypoint	CTC52	CTC52	\N	\N	\N	01010000004E17ABCB7E714040AC88F85935764140
102	CTC54	1583.8900000000001	Waypoint	CTC54	CTC54	\N	\N	\N	01010000004DC8B8F646724040124504C0D1764140
103	CTC55A	1545.9100000000001	Waypoint	CTC55A	CTC55A	\N	\N	\N	010100000021CA84503F7240403884046ED8764140
104	CTC55B	1549.04	Waypoint	CTC55B	CTC55B	\N	\N	\N	01010000004834F3773E724040699503B7DA764140
105	CTC56	1574.03	Waypoint	CTC56	CTC56	\N	\N	\N	0101000000EFC584896072404063228C3CDC764140
106	CTC57	1549.04	Waypoint	CTC57	CTC57	\N	\N	\N	0101000000FDE8B0CF57724040B687E8D0DC764140
107	CTC58	1612.48	Waypoint	CTC58	CTC58	\N	\N	\N	0101000000BBD9BCFD3B724040DC64BCFCFA764140
108	CTC59	1645.1700000000001	Waypoint	CTC59	CTC59	\N	\N	\N	0101000000E072F7643E724040D16DB7941B774140
109	CTC60	1642.77	Waypoint	CTC60	CTC60	\N	\N	\N	010100000061DB1C9337724040C57B8FAC1C774140
110	CTC61	1563.9400000000001	Waypoint	CTC61	CTC61	\N	\N	\N	010100000021B67030467240402A9AFF3EC0764140
111	CTC62	1564.1800000000001	Waypoint	CTC62	CTC62	\N	\N	\N	01010000009076FFFF4F7240404F679BD9B6764140
112	CTC63	1586.77	Waypoint	CTC63	CTC63	\N	\N	\N	010100000005EDD87F527240405A6B9FC3B9764140
113	CTC64	1589.8900000000001	Waypoint	CTC64	CTC64	\N	\N	\N	0101000000512B637B6172404049B4A0D4B7764140
114	CTC65	1617.53	Waypoint	CTC65	CTC65	\N	\N	\N	010100000064B12C1C71724040E907A882A7764140
115	CTC67	423.58300000000003	Waypoint	CTC67	CTC67	\N	\N	\N	010100000015999041347140402B0C7836F7754140
116	CTC68A	1620.1800000000001	Waypoint	CTC68A	CTC68A	\N	\N	\N	01010000009974BC4B67714040BFEBF402FA754140
117	CTC68B	1622.0999999999999	Waypoint	CTC68B	CTC68B	\N	\N	\N	010100000078BD48296771404057C14C34F8754140
118	CTC69	1594.9400000000001	Waypoint	CTC69	CTC69	\N	\N	\N	0101000000008DACDE5C714040ECE028C503764140
119	CTC70	1619.21	Waypoint	CTC70	CTC70	\N	\N	\N	01010000005B3EB3A04771404058243F4502764140
120	CTC71	1596.3800000000001	Waypoint	CTC71	CTC71	\N	\N	\N	010100000073164CB57C71404063337B31F8754140
121	CTC72	1619.21	Waypoint	CTC72	CTC72	\N	\N	\N	0101000000CD922BB551714040D427476EFE754140
122	CTC73	1603.1099999999999	Waypoint	CTC73	CTC73	\N	\N	\N	0101000000208E5CD47A714040AC376013F6754140
123	CTC80	1842.24	Waypoint	CTC80	CTC80	\N	\N	\N	0101000000AF2B54A17B6F404024F73CCBE8764140
124	CTC81A	1850.4100000000001	Waypoint	CTC81A	CTC81A	\N	\N	\N	01010000000FA0C85B7A6F40402C58D843E3764140
125	CTC81B	1858.3399999999999	Waypoint	CTC81B	CTC81B	\N	\N	\N	0101000000EC4EC8E5776F404018769CCBE2764140
126	CTC82	1850.8900000000001	Waypoint	CTC82	CTC82	\N	\N	\N	0101000000C6B2546F716F40406D4C5C82E4764140
127	CTC83	1831.9000000000001	Waypoint	CTC83	CTC83	\N	\N	\N	010100000047082C0C936F404017C25849E5764140
128	CTC84	1837.6700000000001	Waypoint	CTC84	CTC84	\N	\N	\N	0101000000A73264F3736F4040ECF42801D6764140
129	CTC85	1839.5899999999999	Waypoint	CTC85	CTC85	\N	\N	\N	0101000000563F48AC8B6F4040F883473CD5764140
130	CTC86	1856.4200000000001	Waypoint	CTC86	CTC86	\N	\N	\N	010100000008BFDC40646E4040D360DB214C774140
131	CTC87	1867.95	Waypoint	CTC87	CTC87	\N	\N	\N	0101000000CB1D84200A6E404061FEE4D148774140
132	CTC88	1867.23	Waypoint	CTC88	CTC88	\N	\N	\N	01010000007FA060EF136E40404F47F30C49774140
133	CTC89	1861.46	Waypoint	CTC89	CTC89	\N	\N	\N	0101000000921914720A6E40404255CB244A774140
134	CTC90	1868.6700000000001	Waypoint	CTC90	CTC90	\N	\N	\N	01010000008C15C8FB176E4040A463CC324B774140
135	CTC91	1869.4000000000001	Waypoint	CTC91	CTC91	\N	\N	\N	0101000000690105D70F6E4040F075E7C352774140
136	CTC92	1862.4300000000001	Waypoint	CTC92	CTC92	\N	\N	\N	0101000000A97E6B48186E4040BD640C4148774140
137	CTC93	1869.1500000000001	Waypoint	CTC93	CTC93	\N	\N	\N	010100000021005904166E4040722748D45A774140
138	CTC95	1885.02	Waypoint	CTC95	CTC95	\N	\N	\N	0101000000C83FBC792D6E4040E88EAF5157774140
139	CTCA	1503.6199999999999	Flag	CTCA	CTCA	\N	\N	\N	01010000004C4B686F8970404066E697FBC3784140
140	EKO1	700.20100000000002	Flag	EKO1	EKO1	\N	\N	\N	010100000055FBF003028F41404490C72FE3B93F40
141	GARMIN	325.04899999999998	Flag	GARMIN	GARMIN	\N	\N	\N	01010000003581CE1623B357C013B87FA9826D4340
142	GRMEUR	35.934699999999999	Flag	GRMEUR	GRMEUR	\N	\N	\N	01010000001E909861226CF7BF229CA71ECF7D4940
143	GRMTWN	38.097700000000003	Flag	GRMTWN	GRMTWN	\N	\N	\N	01010000001E631221FA685E40183ACF08D10F3940
144	MAA1	352.20600000000002	Flag	MAA1	MAA1	\N	\N	\N	0101000000B212F3ACE49541405508F53E5F5D4040
145	MAA2	169.07599999999999	Flag	MAA2	MAA2	\N	\N	\N	01010000009DAD7710DF954140FB610FE3715D4040
146	MMR1	833.10199999999998	Flag	MMR1	MMR1	\N	\N	\N	0101000000F7621BFDD7B141400192C051F87B4040
147	MMR10	869.63199999999995	Flag	MMR10	MMR10	\N	\N	\N	01010000007BFAA018EDB14140BD319FD4E07B4040
148	MMR11	850.88599999999997	Flag	MMR11	MMR11	\N	\N	\N	010100000041BB73C3D7B1414083DCF450E37B4040
149	MMR12	852.32799999999997	Flag	MMR12	MMR12	\N	\N	\N	0101000000703E1FACD9B141405D7DA78FE17B4040
150	MMR2	835.505	Flag	MMR2	MMR2	\N	\N	\N	0101000000625830B7DCB14140896A0025F47B4040
151	MMR4	832.62099999999998	Flag	MMR4	MMR4	\N	\N	\N	010100000055CFB48ADFB141406BE46C9AF47B4040
152	MMR5	837.66800000000001	Flag	MMR5	MMR5	\N	\N	\N	010100000076E39B75DFB14140C9BE5401F17B4040
153	MMR6	831.65999999999997	Flag	MMR6	MMR6	\N	\N	\N	010100000080E79F5FE2B14140F707206EF47B4040
154	MMR7	851.60699999999997	Flag	MMR7	MMR7	\N	\N	\N	0101000000D65D8F78E8B141406F4BF3ADE07B4040
155	MMR8	859.298	Flag	MMR8	MMR8	\N	\N	\N	010100000041799454EAB141405BCCC0E2DF7B4040
156	MMR9	866.26700000000005	Flag	MMR9	MMR9	\N	\N	\N	0101000000132A3303E7B14140640D0F48DF7B4040
157	NHC1	435.60000000000002	Flag	NHC1	NHC1	\N	\N	\N	01010000000D296C2A3E834140C59AC7EA21C03F40
158	NHC2	430.79300000000001	Flag	NHC2	NHC2	\N	\N	\N	0101000000F5F85FDC3D834140708D60C725C03F40
159	NHC4	439.685	Flag	NHC4	NHC4	\N	\N	\N	0101000000271E73453983414077765FB951C03F40
160	NHC5	446.17399999999998	Flag	NHC5	NHC5	\N	\N	\N	0101000000E9A0E80D398341400C6D29B551C03F40
161	NHC6	438.964	Flag	NHC6	NHC6	\N	\N	\N	0101000000F89D243233834140E91B293F4FC03F40
162	NHC7	453.14400000000001	Flag	NHC7	NHC7	\N	\N	\N	0101000000B0939C413383414018FC47F050C03F40
163	NHC8	466.84199999999998	Flag	NHC8	NHC8	\N	\N	\N	0101000000C70F650D3683414041D2771C5EC03F40
164	NHC9	459.15199999999999	Flag	NHC9	NHC9	\N	\N	\N	0101000000BBFC12E724834140E974C0787DC03F40
165	NHM1	696.596	Flag	NHM1	NHM1	\N	\N	\N	01010000007258E7062D87414064E0CFDC82BF3F40
166	NHM2	693.952	Flag	NHM2	NHM2	\N	\N	\N	0101000000E47DB7A12B874140865AF98386BF3F40
167	NHM3	700.92200000000003	Flag	NHM3	NHM3	\N	\N	\N	010100000057982B743F87414088C2808881BF3F40
168	NHM4	699.96000000000004	Flag	NHM4	NHM4	\N	\N	\N	0101000000D8A080043D874140E87CF7158BBF3F40
169	NHM5	701.64300000000003	Flag	NHM5	NHM5	\N	\N	\N	01010000009F9C10563D874140426D194596BF3F40
170	NHM6	707.40999999999997	Flag	NHM6	NHM6	\N	\N	\N	01010000009E561BAD3487414079D166BB93BF3F40
171	NHM7	699.24000000000001	Flag	NHM7	NHM7	\N	\N	\N	0101000000AE079B532C874140695F804A83BF3F40
172	NHM8	711.01499999999999	Flag	NHM8	NHM8	\N	\N	\N	01010000005C1D474623884140324049514CBE3F40
173	NHM9	688.90499999999997	Flag	NHM9	NHM9	\N	\N	\N	0101000000D6F2A7E622884140827DA16B52BE3F40
174	NHS1	642.28200000000004	Flag	NHS1	NHS1	\N	\N	\N	01010000009115EF0C02864140FB45D608E7BD3F40
175	NHS2	643.00300000000004	Flag	NHS2	NHS2	\N	\N	\N	0101000000EAB9BF594E8641404A12EE0F40BE3F40
176	NHS3	648.28999999999996	Flag	NHS3	NHS3	\N	\N	\N	0101000000427800004A8641406D3BB91B4EBE3F40
177	NHS4	647.08799999999997	Flag	NHS4	NHS4	\N	\N	\N	01010000000B45C411478641401F78A0414FBE3F40
178	SCV1	1182.3	Waypoint	SCV1	SCV1	\N	\N	\N	01010000008E792C9C755740408A8DDC472C804140
179	SCV10	1174.8499999999999	Waypoint	SCV10	SCV10	\N	\N	\N	010100000069EB700CB257404046DA004B32804140
180	SCV100	1109.96	Waypoint	SCV100	SCV100	\N	\N	\N	01010000004967C8C7DA574040EACDF487307E4140
181	SCV106	1115.73	Waypoint	SCV106	SCV106	\N	\N	\N	01010000005E46A76ADF57404001219422327E4140
182	SCV107	1103.71	Waypoint	SCV107	SCV107	\N	\N	\N	01010000008F89AB02E95740401CEFE48C0E7E4140
183	SCV108	1132.79	Waypoint	SCV108	SCV108	\N	\N	\N	010100000010808F2C265840400A146F6CCF7E4140
184	SCV11	1183.74	Waypoint	SCV11	SCV11	\N	\N	\N	0101000000EA318FD1815740404853636022804140
185	SCV110	1137.8399999999999	Waypoint	SCV110	SCV110	\N	\N	\N	01010000006600F1B8FD5740403F8BE764507E4140
186	SCV12	1103.71	Waypoint	SCV12	SCV12	\N	\N	\N	01010000004C0C2FAC8E57404054289B0A22804140
187	SCV120	1073.9100000000001	Waypoint	SCV120	SCV120	\N	\N	\N	0101000000A4AF803F05584040FFA778E28D7E4140
188	SCV121	1261.6099999999999	Waypoint	SCV121	SCV121	\N	\N	\N	0101000000DF28777423584040A908A76DD27E4140
189	SCV122	1127.5	Waypoint	SCV122	SCV122	\N	\N	\N	01010000008476938721584040C845DB4FCB7E4140
190	SCV123	1127.5	Waypoint	SCV123	SCV123	\N	\N	\N	0101000000B27BB80507584040D7074DA3AD7E4140
191	SCV125	1153.7	Waypoint	SCV125	SCV125	\N	\N	\N	0101000000A80FABDFCF574040E6A638D4907E4140
192	SCV126	1159.23	Waypoint	SCV126	SCV126	\N	\N	\N	01010000001EDD5419D05740404780FB83867E4140
193	SCV13	1190.71	Waypoint	SCV13	SCV13	\N	\N	\N	0101000000AF34C32293574040F95224D11E804140
194	SCV130	1242.3800000000001	Waypoint	SCV130	SCV130	\N	\N	\N	0101000000F9D734E7C75740405A7537FEED7E4140
195	SCV131	1264.73	Waypoint	SCV131	SCV131	\N	\N	\N	01010000006153178BC757404066844F24EC7E4140
196	SCV134	1251.03	Waypoint	SCV134	SCV134	\N	\N	\N	0101000000ED792734C3574040E28AB422E47E4140
197	SCV135	1242.1400000000001	Waypoint	SCV135	SCV135	\N	\N	\N	01010000003F1F0753C45740408593B8B7F07E4140
198	SCV136	1224.1199999999999	Waypoint	SCV136	SCV136	\N	\N	\N	0101000000CC31DF15CF574040CA06112AEA7E4140
199	SCV137	1216.6700000000001	Waypoint	SCV137	SCV137	\N	\N	\N	0101000000957DBFE7B3574040B011F372F97E4140
200	SCV138	1146.73	Waypoint	SCV138	SCV138	\N	\N	\N	010100000008153B6B0E584040FF95077CCD7E4140
201	SCV14	1228.6800000000001	Waypoint	SCV14	SCV14	\N	\N	\N	0101000000DF0563A78C5740407490303C11804140
202	SCV15	1184.9400000000001	Waypoint	SCV15	SCV15	\N	\N	\N	0101000000B04B105290574040B05CD4C60F804140
203	SCV16	1159.47	Waypoint	SCV16	SCV16	\N	\N	\N	0101000000D53763AAB657404053886BA81D804140
204	SCV17	1157.3	Waypoint	SCV17	SCV17	\N	\N	\N	0101000000333B7442B4574040E203A89A3A804140
205	SCV18	1157.3	Waypoint	SCV18	SCV18	\N	\N	\N	0101000000B389EF6BB4574040986BCC493A804140
206	SCV2	1211.3800000000001	Waypoint	SCV2	SCV2	\N	\N	\N	01010000009BFF6EB96E574040B3B124D628804140
207	SCV21	1187.3399999999999	Waypoint	SCV21	SCV21	\N	\N	\N	0101000000348D446D7D574040D0DCC4420D804140
208	SCV25	1162.3499999999999	Waypoint	SCV25	SCV25	\N	\N	\N	0101000000EB018BADF457404087C328C148804140
209	SCV26	1392.3399999999999	Waypoint	SCV26	SCV26	\N	\N	\N	0101000000DFEF40321757404012C4A4B85D7F4140
210	SCV27	1397.1500000000001	Waypoint	SCV27	SCV27	\N	\N	\N	0101000000AB16F77020574040AE7E20045A7F4140
211	SCV28	1400.52	Waypoint	SCV28	SCV28	\N	\N	\N	0101000000F6F18E5B25574040C414A0FE577F4140
212	SCV29	1389.46	Waypoint	SCV29	SCV29	\N	\N	\N	0101000000AB65ECFD265740408E4110E8587F4140
213	SCV3	1389.46	Waypoint	SCV3	SCV3	\N	\N	\N	01010000000502901F19724040F0D85854C7014240
214	SCV30	1399.0699999999999	Waypoint	SCV30	SCV30	\N	\N	\N	010100000011F940882A574040FD899795507F4140
215	SCV31	1413.49	Waypoint	SCV31	SCV31	\N	\N	\N	0101000000B4F2D21A31574040350E25F5467F4140
216	SCV32	1414.7	Waypoint	SCV32	SCV32	\N	\N	\N	0101000000AB57572638574040863668FE447F4140
217	SCV33	1411.0899999999999	Waypoint	SCV33	SCV33	\N	\N	\N	0101000000118E38E83B574040782DF3E6487F4140
218	SCV34	1425.75	Waypoint	SCV34	SCV34	\N	\N	\N	0101000000B19684093857404028FF0CCF487F4140
219	SCV35	1401.96	Waypoint	SCV35	SCV35	\N	\N	\N	0101000000100E3FFD3C574040E48D10413C7F4140
220	SCV36	1392.3399999999999	Waypoint	SCV36	SCV36	\N	\N	\N	0101000000567498ED12574040FE15B3D75B7F4140
221	SCV37	1394.51	Waypoint	SCV37	SCV37	\N	\N	\N	0101000000C82227F520574040F30C0DD0657F4140
222	SCV38	1405.5599999999999	Waypoint	SCV38	SCV38	\N	\N	\N	01010000008908B39422574040CDFE1080577F4140
223	SCV39	1388.02	Waypoint	SCV39	SCV39	\N	\N	\N	01010000009ECAB8951D5740402A0B0B605D7F4140
224	SCV4	1191.1900000000001	Waypoint	SCV4	SCV4	\N	\N	\N	0101000000B808590469574040CD1211BC29804140
225	SCV40	1396.9100000000001	Waypoint	SCV40	SCV40	\N	\N	\N	0101000000BF13C742335740404E291C323F7F4140
226	SCV41	1407.73	Waypoint	SCV41	SCV41	\N	\N	\N	0101000000A39A80C9C7564040C562DB6F9B7F4140
227	SCV42	1398.1099999999999	Waypoint	SCV42	SCV42	\N	\N	\N	0101000000BF460476C4564040431D602A997F4140
228	SCV43	1400.28	Waypoint	SCV43	SCV43	\N	\N	\N	0101000000D4FA749FD0564040C53333F68F7F4140
229	SCV44	1414.9400000000001	Waypoint	SCV44	SCV44	\N	\N	\N	0101000000482E9FAFD0564040073CCB548A7F4140
230	SCV45	1394.75	Waypoint	SCV45	SCV45	\N	\N	\N	010100000081B20849CF564040550BF95B937F4140
231	SCV46	1394.03	Waypoint	SCV46	SCV46	\N	\N	\N	0101000000ABEDE4DB145740405E7070635F7F4140
232	SCV47	1385.8599999999999	Waypoint	SCV47	SCV47	\N	\N	\N	0101000000E550243701574040326704BB647F4140
233	SCV48	1378.4100000000001	Waypoint	SCV48	SCV48	\N	\N	\N	01010000004C6914CAF6564040DF7B0B2D637F4140
234	SCV49	1380.3299999999999	Waypoint	SCV49	SCV49	\N	\N	\N	0101000000D301F5D8E9564040BEFF7877707F4140
235	SCV50	1369.03	Waypoint	SCV50	SCV50	\N	\N	\N	010100000064B234C7245740405F3F483F6F7F4140
236	SCV51	1390.6600000000001	Waypoint	SCV51	SCV51	\N	\N	\N	0101000000408224E4EA564040FEB0401C6D7F4140
237	SCV52	1401.48	Waypoint	SCV52	SCV52	\N	\N	\N	0101000000F9574FE0EF564040DFE7F0F7687F4140
238	SCV53	1401.48	Waypoint	SCV53	SCV53	\N	\N	\N	0101000000BC96071EDC5640407055744C9F7F4140
239	SCV54	1387.0599999999999	Waypoint	SCV54	SCV54	\N	\N	\N	0101000000F3E9901FDA56404085F72C3A9F7F4140
240	SCV55	1378.1700000000001	Waypoint	SCV55	SCV55	\N	\N	\N	01010000000AE0BCF1DB564040DAB0FCB2A17F4140
241	SCV56	1399.55	Waypoint	SCV56	SCV56	\N	\N	\N	0101000000C22ECC3A0A5740400418A369617F4140
242	SCV57	1382.97	Waypoint	SCV57	SCV57	\N	\N	\N	01010000008830FF9A0C574040FFCF8C3E597F4140
243	SCV58	1382.97	Waypoint	SCV58	SCV58	\N	\N	\N	0101000000289C546908574040D6023930527F4140
244	SCV59	1382.97	Waypoint	SCV59	SCV59	\N	\N	\N	0101000000280B806D14574040C6DDD4CB657F4140
245	SCV6	1123.1800000000001	Waypoint	SCV6	SCV6	\N	\N	\N	0101000000BF22801A855740407F5F307352804140
246	SCV60	1376	Waypoint	SCV60	SCV60	\N	\N	\N	01010000001EF6A0721C574040FECD305A6C7F4140
247	SCV61	1376	Waypoint	SCV61	SCV61	\N	\N	\N	010100000078C59863155740409AF49E9A707F4140
248	SCV62A	1376	Waypoint	SCV62A	SCV62A	\N	\N	\N	010100000093C6B4AB1A574040C20A69ED737F4140
249	SCV62B	1416.6199999999999	Waypoint	SCV62B	SCV62B	\N	\N	\N	0101000000B68B3B6ED0564040A41F4C0B907F4140
250	SCV63	1361.8199999999999	Waypoint	SCV63	SCV63	\N	\N	\N	010100000075D518812C5740407EEEE034787F4140
251	SCV65	1379.1300000000001	Waypoint	SCV65	SCV65	\N	\N	\N	01010000008BD1A08CE4564040BE1BD35FA47F4140
252	SCV66	1397.3900000000001	Waypoint	SCV66	SCV66	\N	\N	\N	010100000087DBE8363D5740407225840C397F4140
253	SCV67	1404.3599999999999	Waypoint	SCV67	SCV67	\N	\N	\N	01010000007C11E98E3057404037F36CD53C7F4140
254	SCV69	1237.3299999999999	Waypoint	SCV69	SCV69	\N	\N	\N	01010000000AA6A4CBBE574040279A6068107F4140
255	SCV69	1218.3499999999999	Waypoint	SCV69	SCV69	\N	\N	\N	0101000000B6E3D46EBE574040AD3E63CE0F7F4140
256	SCV7	1131.5899999999999	Waypoint	SCV7	SCV7	\N	\N	\N	0101000000C9EFDC978D574040AE85689354804140
257	SCV70	1211.3800000000001	Waypoint	SCV70	SCV70	\N	\N	\N	0101000000DF0A6336C1574040E1D4F0F5017F4140
258	SCV71	1182.78	Waypoint	SCV71	SCV71	\N	\N	\N	0101000000890FB44ED1574040CC72BB92027F4140
259	SCV72	1182.78	Waypoint	SCV72	SCV72	\N	\N	\N	0101000000A81BC00CDA57404075EB142FFF7E4140
260	SCV73	1231.0799999999999	Waypoint	SCV73	SCV73	\N	\N	\N	01010000002DBDA0FFCA574040578194B7E77E4140
261	SCV74	1234.21	Waypoint	SCV74	SCV74	\N	\N	\N	01010000003CC3B841CB57404033FC56D4E87E4140
262	SCV76	1373.5999999999999	Waypoint	SCV76	SCV76	\N	\N	\N	01010000000B73FCFCE957404022EB6401377F4140
263	SCV77	1225.5599999999999	Waypoint	SCV77	SCV77	\N	\N	\N	01010000003C129732DC57404056951373247F4140
264	SCV78	1207.53	Waypoint	SCV78	SCV78	\N	\N	\N	0101000000BC6F845EE2574040D12794B3277F4140
265	SCV79	1225.3199999999999	Waypoint	SCV79	SCV79	\N	\N	\N	0101000000AD14F846D1574040037C4F962E7F4140
266	SCV8	1208.97	Waypoint	SCV8	SCV8	\N	\N	\N	0101000000E3C674BBB35740405645678229804140
267	SCV80	1243.3399999999999	Waypoint	SCV80	SCV80	\N	\N	\N	0101000000CEC5C85ACF574040D0DCD826347F4140
268	SCV81	1263.05	Waypoint	SCV81	SCV81	\N	\N	\N	010100000033FE36F1B95740400990F763407F4140
269	SCV82	1229.8800000000001	Waypoint	SCV82	SCV82	\N	\N	\N	01010000002D851C68B95740408C520FBF457F4140
270	SCV83	1255.8399999999999	Waypoint	SCV83	SCV83	\N	\N	\N	0101000000BA7384B3AB574040ADAF48284A7F4140
271	SCV84	1265.45	Waypoint	SCV84	SCV84	\N	\N	\N	0101000000440F636FB5574040110005DF407F4140
272	SCV86	1109.48	Waypoint	SCV86	SCV86	\N	\N	\N	0101000000541F1034DB5740402A45DCB02E7E4140
273	SCV87	1081.3599999999999	Waypoint	SCV87	SCV87	\N	\N	\N	0101000000F7963FCDF35740405EE6E8AF5F7E4140
274	SCV88	1121.01	Waypoint	SCV88	SCV88	\N	\N	\N	0101000000AD19F8D5055840402B384713AB7E4140
275	SCV89	1134.47	Waypoint	SCV89	SCV89	\N	\N	\N	0101000000462D0C12D45740409167CC618D7E4140
276	SCV9	1191.6700000000001	Waypoint	SCV9	SCV9	\N	\N	\N	0101000000646963C9B5574040A796D3BC28804140
277	SCV91	1167.8800000000001	Waypoint	SCV91	SCV91	\N	\N	\N	0101000000D579A08AE557404096444C420F7E4140
278	SCV92	1143.3699999999999	Waypoint	SCV92	SCV92	\N	\N	\N	0101000000B19F287D1F584040E999389BC97E4140
279	SCV93	1146.97	Waypoint	SCV93	SCV93	\N	\N	\N	010100000088AC081313584040B252E88CCD7E4140
280	SCV94	1131.3499999999999	Waypoint	SCV94	SCV94	\N	\N	\N	01010000001792707A07584040C8FB9E7CAD7E4140
281	SCV95	1173.1700000000001	Waypoint	SCV95	SCV95	\N	\N	\N	0101000000F90BDDEF0758404047600CE2CC7E4140
282	SCV96	1127.98	Waypoint	SCV96	SCV96	\N	\N	\N	01010000007AFB0635E25740402138D8E22D7E4140
283	SCV97	1115.49	Waypoint	SCV97	SCV97	\N	\N	\N	01010000004428771EE3574040B61E53B1107E4140
284	SCV98	1117.6500000000001	Waypoint	SCV98	SCV98	\N	\N	\N	0101000000028A68A6015840405D03FDB3567E4140
285	SCV99	1143.3699999999999	Waypoint	SCV99	SCV99	\N	\N	\N	0101000000E1A4A8B477574040898AA33828804140
\.


--
-- Data for Name: download1; Type: TABLE DATA; Schema: public; Owner: webuser
--

COPY download1 (gid, name, elevation, symbol, "comment", descriptio, source, url, "url name", the_geom) FROM stdin;
0	H	1333.95	Waypoint	H	H	\N	\N	\N	0101000000F33570AC30574040617DE083477F4140
1	001	1333.95	Waypoint	001	001	\N	\N	\N	0101000000B1225E2F511D53C0F587BC9E553A4540
2	002	1684.3399999999999	Waypoint	002	002	\N	\N	\N	01010000003777BC157E7240407C9A1C09F7774140
3	003	279.62700000000001	Waypoint	003	003	\N	\N	\N	01010000004278CB0D9A1E53C02FF87BFCFA3A4540
4	004	292.84500000000003	Waypoint	004	004	\N	\N	\N	0101000000D12DE87BB51E53C0FFEDD8261A3B4540
5	006	257.99799999999999	Waypoint	006	006	\N	\N	\N	01010000003928362691AB39406ED5EF6E17874140
6	007	257.99799999999999	Waypoint	007	007	\N	\N	\N	0101000000D571B8336C5338406D4FF302AAB64140
7	008	-6.6033900000000001	Waypoint	008	008	\N	\N	\N	0101000000A50770406C8F374046104C0D6BA24140
8	009	-6.6033900000000001	Waypoint	009	009	\N	\N	\N	01010000008C73D6D21C593B40BFD37B62D14D4140
9	010	-6.6033900000000001	Waypoint	010	010	\N	\N	\N	01010000000B20380BD4504040D11ED4EA50844140
10	ANG-1	1596.3800000000001	Waypoint	ANG-1	ANG-1	\N	\N	\N	0101000000E08D1E2262163840F84CEC5D20A24140
11	ANG-2	1606	Waypoint	ANG-2	ANG-2	\N	\N	\N	01010000007578AF2A601638402323F8C335A24140
12	ANG-3	1612.48	Waypoint	ANG-3	ANG-3	\N	\N	\N	01010000002FA34EFC75163840C257B34F39A24140
13	ANG-4	1590.1300000000001	Waypoint	ANG-4	ANG-4	\N	\N	\N	010100000064536F8C6B1638405EDEE40337A24140
14	ANG-6	1592.78	Waypoint	ANG-6	ANG-6	\N	\N	\N	0101000000CDC0AF2E7A1638401D82578901A24140
15	ANG-7	1590.8599999999999	Waypoint	ANG-7	ANG-7	\N	\N	\N	01010000002BC706007E1638402DA8981805A24140
16	ANG8	1591.5799999999999	Waypoint	ANG8	ANG8	\N	\N	\N	0101000000E77091A1811638404244BB2105A24140
17	APG10	29.2056	Waypoint	APG10	APG10	\N	\N	\N	0101000000B6288308102640407B57606F32834140
18	APG11	30.887899999999998	Waypoint	APG11	APG11	\N	\N	\N	0101000000FFEF2A99132640405B77C31B31834140
19	APG12	44.826900000000002	Waypoint	APG12	APG12	\N	\N	\N	010100000032318BC0402640404DC7FCD958834140
20	APG16	48.431800000000003	Waypoint	APG16	APG16	\N	\N	\N	0101000000A5CD78283826404083CBD7239C834140
21	APG18	60.207900000000002	Waypoint	APG18	APG18	\N	\N	\N	0101000000CBD43B1479264040464BF016A0834140
22	APG3	41.702500000000001	Waypoint	APG3	APG3	\N	\N	\N	01010000009B835BCF342640408BB86BCF4D834140
23	APG7	28.725000000000001	Waypoint	APG7	APG7	\N	\N	\N	0101000000869420D510264040C272C37027834140
24	APG8	43.625100000000003	Waypoint	APG8	APG8	\N	\N	\N	010100000058DEF0E3312640407D8FC0404C834140
25	APG9	45.547899999999998	Waypoint	APG9	APG9	\N	\N	\N	0101000000B0FDC7B64A264040B3B7C42C5C834140
26	BU5	45.547899999999998	Waypoint	BU5	BU5	\N	\N	\N	0101000000A5D66371CB1E53C09A48A89A123B4540
27	CFS10	1544.95	Waypoint	CFS10	CFS10	\N	\N	\N	0101000000CF6C5CA1BD7F4040BEFE7FF7297A4140
28	CFS11	1559.3699999999999	Waypoint	CFS11	CFS11	\N	\N	\N	0101000000B229E069357F4040672A87C0587A4140
29	CFS2	1556.97	Waypoint	CFS2	CFS2	\N	\N	\N	0101000000B8A3D71D4D7F4040FE7DA0BE607A4140
30	CFS3	1554.0899999999999	Waypoint	CFS3	CFS3	\N	\N	\N	0101000000620D9BF14B7F40401F9B63C7667A4140
31	CFS6	1559.8499999999999	Waypoint	CFS6	CFS6	\N	\N	\N	01010000002C11F945417F4040C53674765C7A4140
32	CFS7	1559.6099999999999	Waypoint	CFS7	CFS7	\N	\N	\N	0101000000F1F8983D407F4040429A28775C7A4140
33	CHF1	547.35199999999998	Waypoint	CHF1	CHF1	\N	\N	\N	01010000006878D9CD11A73940721C3B5204894140
34	CHF10	569.46199999999999	Waypoint	CHF10	CHF10	\N	\N	\N	01010000002692B0BBB5A639403BDCFFE1DD884140
35	CHF11	632.66899999999998	Waypoint	CHF11	CHF11	\N	\N	\N	0101000000135546C3ACA6394081431F61D5884140
36	CHF13	565.37699999999995	Waypoint	CHF13	CHF13	\N	\N	\N	0101000000989791B4FAA63940E258A0A8EF884140
37	CHF14	583.88199999999995	Waypoint	CHF14	CHF14	\N	\N	\N	0101000000A7459176A2A63940D9C85CB6E9884140
38	CHF2	551.678	Waypoint	CHF2	CHF2	\N	\N	\N	010100000051D4E8C11CA73940124B53CF05894140
39	CHF3	547.59299999999996	Waypoint	CHF3	CHF3	\N	\N	\N	01010000002BF7FDF908A73940F22A33F103894140
40	CHF4	542.06500000000005	Waypoint	CHF4	CHF4	\N	\N	\N	01010000009BEE260C0BA73940151387AB04894140
41	CHF5	550.95699999999999	Waypoint	CHF5	CHF5	\N	\N	\N	010100000045F377241BA739408815C4F901894140
42	CHF6	550.95699999999999	Waypoint	CHF6	CHF6	\N	\N	\N	010100000041166A9901A73940353C83A70C894140
43	CKF 17	1318.3199999999999	Waypoint	CKF 17	CKF 17	\N	\N	\N	0101000000355870F351D73840419030CA7B964140
44	CKF FUTURE	1335.1500000000001	Waypoint	CKF FUTURE	CKF FUTURE	\N	\N	\N	0101000000A372DEC2E4D73840731F6FA84E964140
45	CKF PARK	1220.75	Waypoint	CKF PARK	CKF PARK	\N	\N	\N	0101000000220D41EA03D7384063EFE62596954140
46	CKF-15	1298.1400000000001	Waypoint	CKF-15	CKF-15	\N	\N	\N	0101000000C64649DD08D7384045024D519C964140
47	CKF-16	1319.53	Waypoint	CKF-16	CKF-16	\N	\N	\N	0101000000DA1CB7FD4AD7384010FCDAC07E964140
48	CKF12	1319.53	Waypoint	CKF12	CKF12	\N	\N	\N	01010000000D4919B065D73840F334FB648E954140
49	CKF14	1226.76	Waypoint	CKF14	CKF14	\N	\N	\N	0101000000293D1FDA51D7384037F66B3977954140
50	CKF18	1315.9200000000001	Waypoint	CKF18	CKF18	\N	\N	\N	0101000000B592D7FC58D73840F5BA3BEE78964140
51	CKF2	1218.3499999999999	Waypoint	CKF2	CKF2	\N	\N	\N	01010000002B9C283288D738404DEA3B278C954140
52	CKF4	1214.26	Waypoint	CKF4	CKF4	\N	\N	\N	0101000000557D906088D738408EF5305B82954140
53	CKF6	1213.54	Waypoint	CKF6	CKF6	\N	\N	\N	01010000004AB390B87BD7384044D7B81081954140
54	CKF8	1206.5699999999999	Waypoint	CKF8	CKF8	\N	\N	\N	01010000008FAC6E8880D73840CE205C067E954140
55	CKL10	20.7941	Waypoint	CKL10	CKL10	\N	\N	\N	0101000000048A906E4F29404046DCC41254784140
56	CKL5	27.0426	Waypoint	CKL5	CKL5	\N	\N	\N	01010000006318BC5749294040BA84B00B60784140
57	CKL6	20.313400000000001	Waypoint	CKL6	CKL6	\N	\N	\N	010100000041411FE84529404040AF4F6B60784140
58	CKL7	18.871500000000001	Waypoint	CKL7	CKL7	\N	\N	\N	010100000010E564125029404022F2147466784140
59	CKL8	29.445900000000002	Waypoint	CKL8	CKL8	\N	\N	\N	0101000000745557404C294040F191209E5D784140
60	CKL9	11.180899999999999	Waypoint	CKL9	CKL9	\N	\N	\N	01010000001D8BD07C4C2940405805281566784140
61	CKM1-9	253.91200000000001	Waypoint	CKM1-9	CKM1-9	\N	\N	\N	0101000000BF19CF7CDBDB3940FFD5E006878F4140
62	CLA-1	20.7941	Waypoint	CLA-1	CLA-1	\N	\N	\N	0101000000C0B2D75DCE274040F7C26C1B297A4140
63	CLA-2	23.4376	Waypoint	CLA-2	CLA-2	\N	\N	\N	0101000000734F6B5BD32740408380D008237A4140
64	CLA-21	23.4376	Waypoint	CLA-21	CLA-21	\N	\N	\N	01010000009FD6740AE0274040744A3322007A4140
65	CLA-3	21.755400000000002	Waypoint	CLA-3	CLA-3	\N	\N	\N	01010000001783F405CE274040831DC75B237A4140
66	CLA-4	20.553699999999999	Waypoint	CLA-4	CLA-4	\N	\N	\N	010100000080419343D2274040F4FFDA5C1D7A4140
67	CLA10	15.2666	Waypoint	CLA10	CLA10	\N	\N	\N	010100000087DD0F29DA27404083023302117A4140
68	CLA11	19.592400000000001	Waypoint	CLA11	CLA11	\N	\N	\N	0101000000FF303FC0E527404049909840147A4140
69	CLA14	20.0731	Waypoint	CLA14	CLA14	\N	\N	\N	0101000000046D3394E1274040E9B5D49F0F7A4140
70	CLA15	20.7941	Waypoint	CLA15	CLA15	\N	\N	\N	010100000043107DFDE427404002914852077A4140
71	CLA16	10.460000000000001	Waypoint	CLA16	CLA16	\N	\N	\N	01010000001E7A88CFE827404003E8180C057A4140
72	CLA18	14.545500000000001	Waypoint	CLA18	CLA18	\N	\N	\N	0101000000F050F3D9EA2740403142BF89FD794140
73	CLA5	21.995699999999999	Waypoint	CLA5	CLA5	\N	\N	\N	01010000007E705F83D52740408BA78B051F7A4140
74	CLA6	19.832799999999999	Waypoint	CLA6	CLA6	\N	\N	\N	010100000098AB7F0DD12740403A4D43AD197A4140
75	CLA8	21.755400000000002	Waypoint	CLA8	CLA8	\N	\N	\N	0101000000FF819031D9274040B4DFE7B3147A4140
76	CLA9	19.111799999999999	Waypoint	CLA9	CLA9	\N	\N	\N	01010000000D023040D0274040DF7D6F82187A4140
77	CMM1	455.78699999999998	Waypoint	CMM1	CMM1	\N	\N	\N	0101000000A2AF283EA18D40403EBD03508D854140
78	CMM2	456.26799999999997	Waypoint	CMM2	CMM2	\N	\N	\N	0101000000CD0D2D82A48D40409AFD5E6387854140
79	CRV	185.178	Waypoint	CRV	CRV	\N	\N	\N	010100000083B5DE2D18C03940D55DD44C09884140
80	CSF PARK	938.60599999999999	Waypoint	CSF PARK	CSF PARK	\N	\N	\N	01010000001373E8ABE68939407E864495F28B4140
81	CSF1	1005.9	Waypoint	CSF1	CSF1	\N	\N	\N	010100000095BDF6E3AE8839400E549887248C4140
82	CSF10	1005.9	Waypoint	CSF10	CSF10	\N	\N	\N	01010000001B7547DB8588394006D48C26468C4140
83	CSF12	1009.98	Waypoint	CSF12	CSF12	\N	\N	\N	0101000000A055C1035E88394012DB6BAE0B8C4140
84	CSF13	1170.52	Waypoint	CSF13	CSF13	\N	\N	\N	0101000000787F381F09863940B9CEB7ECAD8C4140
85	CSF14	1182.54	Waypoint	CSF14	CSF14	\N	\N	\N	010100000059AEF631188639409F8ABB44AC8C4140
86	CSF15	1174.6099999999999	Waypoint	CSF15	CSF15	\N	\N	\N	0101000000F95C089A1886394033018C55AD8C4140
87	CSF16	1171.96	Waypoint	CSF16	CSF16	\N	\N	\N	0101000000FE21DFA01B863940D06770F6AF8C4140
88	CSF17	1184.46	Waypoint	CSF17	CSF17	\N	\N	\N	0101000000482B59760A8639403296A77BB68C4140
89	CSF18	1175.8099999999999	Waypoint	CSF18	CSF18	\N	\N	\N	0101000000643117DC028639407CAE7CB7B58C4140
90	CSF2	1175.8099999999999	Waypoint	CSF2	CSF2	\N	\N	\N	01010000006DCB8FDEC1883940250A41CF258C4140
91	CSF20	1185.6600000000001	Waypoint	CSF20	CSF20	\N	\N	\N	0101000000BB61E05B068639408401A71EB98C4140
92	CSF3	1053.24	Waypoint	CSF3	CSF3	\N	\N	\N	01010000004827BF038B88394019E4B7B3328C4140
93	CSF4	1053.24	Waypoint	CSF4	CSF4	\N	\N	\N	0101000000DD3A793D8A88394010E08F03388C4140
94	CSF5	1056.6099999999999	Waypoint	CSF5	CSF5	\N	\N	\N	0101000000F11E1EC479883940AAF200C0388C4140
95	CSF6	1053	Waypoint	CSF6	CSF6	\N	\N	\N	0101000000F181277179883940CA9227B33B8C4140
96	CSF7	1036.4200000000001	Waypoint	CSF7	CSF7	\N	\N	\N	01010000000BB20FF981883940E78167F93C8C4140
97	CSF8	1015.51	Waypoint	CSF8	CSF8	\N	\N	\N	0101000000D9CC7F1A87883940C9ED3E974E8C4140
98	CTC JEN	1638.4400000000001	Waypoint	CTC JEN	CTC JEN	\N	\N	\N	0101000000AA50D0327A7240404FFE936442774140
99	CTC-151	1717.75	Waypoint	CTC-151	CTC-151	\N	\N	\N	01010000003D03CC770E70404056F01874FB754140
100	CTC-154	1711.74	Waypoint	CTC-154	CTC-154	\N	\N	\N	01010000003AD548B5097040400B0FEBA4F6754140
101	CTC-157	1715.3399999999999	Waypoint	CTC-157	CTC-157	\N	\N	\N	010100000065C7672E077040400382E0C1F4754140
102	CTC-160	1708.6199999999999	Waypoint	CTC-160	CTC-160	\N	\N	\N	010100000096552753047040404A0C6FC7F5754140
103	CTC-163	1702.6099999999999	Waypoint	CTC-163	CTC-163	\N	\N	\N	0101000000CA3DCC7A0B70404073CDA07EF0754140
104	CTC-166	1705.97	Waypoint	CTC-166	CTC-166	\N	\N	\N	01010000008C29D76217704040EFBC9487F3754140
105	CTC-169	1775.6700000000001	Waypoint	CTC-169	CTC-169	\N	\N	\N	0101000000181ABF58EE6F4040E1A667C31F764140
106	CTC-58	1612.48	Waypoint	CTC-58	CTC-58	\N	\N	\N	0101000000BBD9BCFD3B724040DC64BCFCFA764140
107	CTC-67	423.58300000000003	Waypoint	CTC-67	CTC-67	\N	\N	\N	010100000015999041347140402B0C7836F7754140
108	CTC-69	1594.9400000000001	Waypoint	CTC-69	CTC-69	\N	\N	\N	0101000000008DACDE5C714040A81EDBC503764140
109	CTC100	1853.77	Waypoint	CTC100	CTC100	\N	\N	\N	01010000003C401BAC046E40402991C86747774140
110	CTC101	1853.77	Waypoint	CTC101	CTC101	\N	\N	\N	01010000001C376CC3F76D40401469738046774140
111	CTC102	1853.77	Waypoint	CTC102	CTC102	\N	\N	\N	0101000000EF0CED78E06D4040BC1FF4C256774140
112	CTC131	1777.8299999999999	Waypoint	CTC131	CTC131	\N	\N	\N	0101000000342F03A5136F4040770435514B784140
113	CTC132	1777.8299999999999	Waypoint	CTC132	CTC132	\N	\N	\N	010100000026AF2432D97440401B6ABBD9C4844140
114	CTC135	1816.28	Waypoint	CTC135	CTC135	\N	\N	\N	0101000000C99CF7970E6F4040B752F34848784140
115	CTC137	1809.55	Waypoint	CTC137	CTC137	\N	\N	\N	01010000004BF007B5FF6E4040FDE23BF940784140
116	CTC139	1825.9000000000001	Waypoint	CTC139	CTC139	\N	\N	\N	0101000000959CF725F96E4040E4FB8E5347784140
117	CTC140	1793.45	Waypoint	CTC140	CTC140	\N	\N	\N	010100000021207B97F46E40400EDAB04841784140
118	CTC145	1830.46	Waypoint	CTC145	CTC145	\N	\N	\N	010100000057515BA10A6F4040C0688C5F03784140
119	CTC146	1830.46	Waypoint	CTC146	CTC146	\N	\N	\N	01010000000DD66F8E096F4040F4C1DC35FB774140
120	CTC171	1848.01	Waypoint	CTC171	CTC171	\N	\N	\N	01010000005AC28BD2E66D404056CB386E3A774140
121	CTC174	1870.3599999999999	Waypoint	CTC174	CTC174	\N	\N	\N	010100000090F5EB86E16D40402001857541774140
122	CTC177	1852.3299999999999	Waypoint	CTC177	CTC177	\N	\N	\N	01010000001F44DCE3DF6D4040105E830A43774140
123	CTC180	1867.71	Waypoint	CTC180	CTC180	\N	\N	\N	0101000000292BF08FE36D4040BA6AD31542774140
124	CTC182	1859.54	Waypoint	CTC182	CTC182	\N	\N	\N	0101000000A0F6DF73B56D4040466E50723C774140
125	CTC183	1870.3599999999999	Waypoint	CTC183	CTC183	\N	\N	\N	0101000000E9704CCDE06D4040C708645347774140
126	CTC184	1848.97	Waypoint	CTC184	CTC184	\N	\N	\N	01010000001B1367E7226E40400771CC4F3D774140
127	CTC186	1867.95	Waypoint	CTC186	CTC186	\N	\N	\N	0101000000B9CD7797DB6D4040F2E0CB9D49774140
128	CTC2007 10	1689.6300000000001	Waypoint	CTC2007 10	CTC2007 10	\N	\N	\N	01010000003E0724CE7B724040F91D0781FC774140
129	CTC2007 11	1684.8199999999999	Waypoint	CTC2007 11	CTC2007 11	\N	\N	\N	010100000040BEA05F7D724040F2A1B3E8F7774140
130	CTC2007 12	1679.0599999999999	Waypoint	CTC2007 12	CTC2007 12	\N	\N	\N	0101000000085184F57B7240403B14E7A3EA774140
131	CTC2007 9	1697.8	Waypoint	CTC2007 9	CTC2007 9	\N	\N	\N	010100000033EF0BC47F724040ABC6D37103784140
132	CTC51	1583.8900000000001	Waypoint	CTC51	CTC51	\N	\N	\N	0101000000F53834AE7D71404089A980BD3A764140
133	CTC52	1583.4100000000001	Waypoint	CTC52	CTC52	\N	\N	\N	0101000000CB7A5FCC7E714040AC88F85935764140
134	CTC54	1583.8900000000001	Waypoint	CTC54	CTC54	\N	\N	\N	010100000008066BF746724040124504C0D1764140
135	CTC55	1549.04	Waypoint	CTC55	CTC55	\N	\N	\N	0101000000C597A7783E724040E6F8B7B7DA764140
136	CTC57	1549.04	Waypoint	CTC57	CTC57	\N	\N	\N	0101000000FDE8B0CF57724040B687E8D0DC764140
137	CTC59	1645.1700000000001	Waypoint	CTC59	CTC59	\N	\N	\N	01010000005DD6AB653E7240404ED16B951B774140
138	CTC60 JDW	1642.77	Waypoint	CTC60 JDW	CTC60 JDW	\N	\N	\N	010100000061DB1C933772404042DF43AD1C774140
139	CTC71	1596.3800000000001	Waypoint	CTC71	CTC71	\N	\N	\N	010100000073164CB57C714040E0962F32F8754140
140	CTC73	1603.1099999999999	Waypoint	CTC73	CTC73	\N	\N	\N	0101000000208E5CD47A714040AC376013F6754140
141	CTC86	1856.4200000000001	Waypoint	CTC86	CTC86	\N	\N	\N	0101000000C4FC8E41646E404050C48F224C774140
142	CTM1	161.86600000000001	Waypoint	CTM1	CTM1	\N	\N	\N	010100000008E0067F36373A40CB348879509C4140
143	CTM10	156.09899999999999	Waypoint	CTM10	CTM10	\N	\N	\N	01010000000A32115549373A409D89908A649C4140
144	CVP-1	1333.22	Waypoint	CVP-1	CVP-1	\N	\N	\N	010100000072AE9678161B3840100B61A7EBA04140
145	CVP-2	1335.1500000000001	Waypoint	CVP-2	CVP-2	\N	\N	\N	0101000000387FB8501E1B3840272420C6EEA04140
146	CVPSTUMP	1334.6700000000001	Waypoint	CVPSTUMP	CVPSTUMP	\N	\N	\N	010100000074161E7D111B38404B3D78DEE7A04140
147	GARMIN	325.04899999999998	Waypoint	GARMIN	GARMIN	\N	\N	\N	01010000003581CE1623B357C013B87FA9826D4340
148	GRMEUR	35.934699999999999	Waypoint	GRMEUR	GRMEUR	\N	\N	\N	01010000001E909861226CF7BF229CA71ECF7D4940
149	GRMPHX	361.09800000000001	Waypoint	GRMPHX	GRMPHX	\N	\N	\N	0101000000D0B1FD108DFC5BC022360CAA43AA4040
150	GRMTWN	38.097700000000003	Waypoint	GRMTWN	GRMTWN	\N	\N	\N	01010000001E631221FA685E40183ACF08D10F3940
151	IIIIII	-7.0839800000000004	Waypoint	IIIIII	IIIIII	\N	\N	\N	01010000009F25B6256C8F3740230B08156BA24140
152	JUNIP	1690.1099999999999	Waypoint	JUNIP	JUNIP	\N	\N	\N	0101000000FE0744C78F724040BC4A0783DC774140
153	OKV1	260.88099999999997	Waypoint	OKV1	OKV1	\N	\N	\N	0101000000541D30178AAB3940E9DB546D0F874140
154	OKV2	259.68000000000001	Waypoint	OKV2	OKV2	\N	\N	\N	0101000000690848AD90AB39407E32D86A15874140
155	OKV3	259.68000000000001	Waypoint	OKV3	OKV3	\N	\N	\N	01010000006F41DFAB90AB394004C0807715874140
156	OKV4	258.959	Waypoint	OKV4	OKV4	\N	\N	\N	0101000000CD47295392AB39407839C4F615874140
157	SCV1	1182.3	Waypoint	SCV1	SCV1	\N	\N	\N	01010000008E792C9C755740408A8DDC472C804140
158	SCV106	1115.73	Waypoint	SCV106	SCV106	\N	\N	\N	0101000000DBA95B6BDF57404001219422327E4140
159	SCV107	1103.71	Waypoint	SCV107	SCV107	\N	\N	\N	01010000000CED5F03E9574040D82C978D0E7E4140
160	SCV12	1103.71	Waypoint	SCV12	SCV12	\N	\N	\N	0101000000C96FE3AC8E574040D18B4F0B22804140
161	SCV120	1073.9100000000001	Waypoint	SCV120	SCV120	\N	\N	\N	010100000060ED324005584040FFA778E28D7E4140
162	SCV121	1261.6099999999999	Waypoint	SCV121	SCV121	\N	\N	\N	01010000005C8C2B7523584040266C5B6ED27E4140
163	SCV122	1127.5	Waypoint	SCV122	SCV122	\N	\N	\N	010100000001DA47882158404045A98F50CB7E4140
164	SCV123	1127.5	Waypoint	SCV123	SCV123	\N	\N	\N	0101000000B27BB805075840409245FFA3AD7E4140
165	SCV125	1153.7	Waypoint	SCV125	SCV125	\N	\N	\N	010100000025735FE0CF574040E6A638D4907E4140
166	SCV126	1159.23	Waypoint	SCV126	SCV126	\N	\N	\N	0101000000DA1A071AD0574040C4E3AF84867E4140
167	SCV14	1228.6800000000001	Waypoint	SCV14	SCV14	\N	\N	\N	01010000005C6917A88C5740407490303C11804140
168	SCV25	1162.3499999999999	Waypoint	SCV25	SCV25	\N	\N	\N	010100000068653FAEF457404087C328C148804140
169	SCV27	1397.1500000000001	Waypoint	SCV27	SCV27	\N	\N	\N	0101000000287AAB7120574040AE7E20045A7F4140
170	SCV28	1400.52	Waypoint	SCV28	SCV28	\N	\N	\N	01010000007355435C25574040C414A0FE577F4140
171	SCV29	1389.46	Waypoint	SCV29	SCV29	\N	\N	\N	0101000000AB65ECFD265740408E4110E8587F4140
172	SCV3	1389.46	Waypoint	SCV3	SCV3	\N	\N	\N	01010000000502901F19724040F0D85854C7014240
173	SCV30	1399.0699999999999	Waypoint	SCV30	SCV30	\N	\N	\N	0101000000CC36F3882A5740407AED4B96507F4140
174	SCV31	1413.49	Waypoint	SCV31	SCV31	\N	\N	\N	01010000003156871B31574040F14BD7F5467F4140
175	SCV32	1414.7	Waypoint	SCV32	SCV32	\N	\N	\N	010100000028BB0B2738574040863668FE447F4140
176	SCV33	1411.0899999999999	Waypoint	SCV33	SCV33	\N	\N	\N	0101000000118E38E83B574040F590A7E7487F4140
177	SCV34	1425.75	Waypoint	SCV34	SCV34	\N	\N	\N	0101000000B19684093857404028FF0CCF487F4140
178	SCV35	1401.96	Waypoint	SCV35	SCV35	\N	\N	\N	01010000008D71F3FD3C574040E48D10413C7F4140
179	SCV45	1394.75	Waypoint	SCV45	SCV45	\N	\N	\N	01010000003DF0BA49CF5640401149AB5C937F4140
180	SCV66	1397.3900000000001	Waypoint	SCV66	SCV66	\N	\N	\N	010100000087DBE8363D5740407225840C397F4140
181	SCV67	1404.3599999999999	Waypoint	SCV67	SCV67	\N	\N	\N	0101000000384F9B8F3057404037F36CD53C7F4140
182	SCV76	1373.5999999999999	Waypoint	SCV76	SCV76	\N	\N	\N	01010000000B73FCFCE9574040DE281702377F4140
183	SCV79	1225.3199999999999	Waypoint	SCV79	SCV79	\N	\N	\N	0101000000AD14F846D157404080DF03972E7F4140
184	SCV80	1243.3399999999999	Waypoint	SCV80	SCV80	\N	\N	\N	0101000000CEC5C85ACF574040D0DCD826347F4140
185	SCV81	1263.05	Waypoint	SCV81	SCV81	\N	\N	\N	0101000000B061EBF1B957404086F3AB64407F4140
186	SCV84	1265.45	Waypoint	SCV84	SCV84	\N	\N	\N	0101000000C1721770B5574040CD3DB7DF407F4140
187	SCV88	1121.01	Waypoint	SCV88	SCV88	\N	\N	\N	0101000000AD19F8D505584040A89BFB13AB7E4140
188	SCV89	1134.47	Waypoint	SCV89	SCV89	\N	\N	\N	0101000000462D0C12D45740409167CC618D7E4140
189	SCV91	1167.8800000000001	Waypoint	SCV91	SCV91	\N	\N	\N	0101000000D579A08AE557404096444C420F7E4140
190	SCV92	1143.3699999999999	Waypoint	SCV92	SCV92	\N	\N	\N	0101000000B19F287D1F584040E999389BC97E4140
191	SCV99	1143.3699999999999	Waypoint	SCV99	SCV99	\N	\N	\N	0101000000E1A4A8B47757404006EE573928804140
192	SMH1	489.673	Waypoint	SMH1	SMH1	\N	\N	\N	0101000000C27C7CB9C48B4040D1467BAAAD834140
193	SMH10	500.00799999999998	Waypoint	SMH10	SMH10	\N	\N	\N	010100000013EFFB95E08B40408AFB920496834140
194	SMH11	512.745	Waypoint	SMH11	SMH11	\N	\N	\N	010100000050BDFB04CC8B4040CB2378768B834140
195	SMH12	507.21699999999998	Waypoint	SMH12	SMH12	\N	\N	\N	0101000000784AF04ECA8B4040387EDB2587834140
196	SMH13	513.947	Waypoint	SMH13	SMH13	\N	\N	\N	0101000000BBCCC7EDCB8B40409D7ADC6B8C834140
197	SMH2	502.17099999999999	Waypoint	SMH2	SMH2	\N	\N	\N	0101000000446E6054CD8B4040344EFC7E95834140
198	SMH4	489.91399999999999	Waypoint	SMH4	SMH4	\N	\N	\N	01010000006FCC6498D08B4040B319388490834140
199	SMH6	514.42700000000002	Waypoint	SMH6	SMH6	\N	\N	\N	0101000000EA0FF04BCD8B4040A855B7248E834140
200	SMH9	505.29500000000002	Waypoint	SMH9	SMH9	\N	\N	\N	01010000000FA48895DC8B404048872C778B834140
201	STV-10	1087.6099999999999	Waypoint	STV-10	STV-10	\N	\N	\N	01010000005D0E77D2DB50404079F7E68853844140
202	STV-12	1070.0599999999999	Waypoint	STV-12	STV-12	\N	\N	\N	01010000006FBC8C79D5504040BA7440D059844140
203	STV-14	1079.4400000000001	Waypoint	STV-14	STV-14	\N	\N	\N	0101000000E2BB6CF2D6504040F115CB9751844140
204	STV-16	1079.4400000000001	Waypoint	STV-16	STV-16	\N	\N	\N	01010000000B20380BD4504040D11ED4EA50844140
205	STV-20	1074.1500000000001	Waypoint	STV-20	STV-20	\N	\N	\N	01010000004C31AC87D45040406B71C83152844140
206	STV-49	1037.8599999999999	Waypoint	STV-49	STV-49	\N	\N	\N	01010000002158E89250514040FE40E7A50A834140
207	STV-52	968.40599999999995	Waypoint	STV-52	STV-52	\N	\N	\N	010100000051AF24114B51404079CB68F31E834140
208	STV-55	960.95600000000002	Waypoint	STV-55	STV-55	\N	\N	\N	0101000000CA50F444405840404AC438D8CB8A4140
209	STV-8	1072.23	Waypoint	STV-8	STV-8	\N	\N	\N	0101000000C467C71AE95040401D9DD4465E844140
210	STV17	1074.6300000000001	Waypoint	STV17	STV17	\N	\N	\N	010100000021273FEECA5040402F7CFB7552844140
211	STV19	1070.0599999999999	Waypoint	STV19	STV19	\N	\N	\N	010100000058E017D6CE504040EE50D0CA56844140
212	STV21	1079.9200000000001	Waypoint	STV21	STV21	\N	\N	\N	0101000000A17274DEC4504040AD50375F4B844140
213	STV22	1079.9200000000001	Waypoint	STV22	STV22	\N	\N	\N	0101000000B1FE0454CE5040402D4578CF4F844140
214	STV24	1080.1600000000001	Waypoint	STV24	STV24	\N	\N	\N	0101000000BFDB172BC5504040E962F4584A844140
215	STV25	1093.1400000000001	Waypoint	STV25	STV25	\N	\N	\N	0101000000EF4F44E7BE504040DE9E73F947844140
216	STV26	1083.04	Waypoint	STV26	STV26	\N	\N	\N	01010000000DB34425BD5040402E90335C43844140
217	STV32	1083.04	Waypoint	STV32	STV32	\N	\N	\N	01010000002C73B82BBB504040F91374FF41844140
218	STV33	1115.01	Waypoint	STV33	STV33	\N	\N	\N	01010000005FE6BF30AE504040D6108CEB2E844140
219	STV35	1115.25	Waypoint	STV35	STV35	\N	\N	\N	0101000000561C9CC2A9504040366810682E844140
220	STV36	1114.29	Waypoint	STV36	STV36	\N	\N	\N	01010000005ECC2CC8AA5040405D10990B3F844140
221	STV40	1114.29	Waypoint	STV40	STV40	\N	\N	\N	01010000009F7A9797AB5040403352C42940844140
222	STV63	889.09799999999996	Waypoint	STV63	STV63	\N	\N	\N	01010000007D2B2BB0B44F4040A2921CABF27F4140
223	STV64	899.19200000000001	Waypoint	STV64	STV64	\N	\N	\N	0101000000C3DB9CADB04F4040B4578CABEB7F4140
224	STV66	895.58699999999999	Waypoint	STV66	STV66	\N	\N	\N	0101000000599ACB75A94F4040C6BF88E3E47F4140
225	STV68	891.26099999999997	Waypoint	STV68	STV68	\N	\N	\N	0101000000BEDB0800984F40406497F46CE07F4140
226	STV7	1055.4100000000001	Waypoint	STV7	STV7	\N	\N	\N	0101000000E38A44CEE65040407E9FC02755844140
227	TGC10	335.86399999999998	Waypoint	TGC10	TGC10	\N	\N	\N	01010000009C7809793D1D53C0D7FD4C85843A4540
228	TGC11	315.67599999999999	Waypoint	TGC11	TGC11	\N	\N	\N	01010000003434CE41381D53C0CAA81BF0853A4540
229	TGC12	282.99200000000002	Waypoint	TGC12	TGC12	\N	\N	\N	01010000003C839D9AFF1D53C041008587A43A4540
230	TGC13	284.91399999999999	Waypoint	TGC13	TGC13	\N	\N	\N	01010000009C27E840001E53C009F0DBE5A23A4540
231	TGC14	294.76799999999997	Waypoint	TGC14	TGC14	\N	\N	\N	0101000000B78F93C4FD1D53C025FD0C5BB23A4540
232	TGC15	293.08499999999998	Waypoint	TGC15	TGC15	\N	\N	\N	0101000000E7B5A71E081E53C023EA9BBA863A4540
233	TGC3	287.31799999999998	Waypoint	TGC3	TGC3	\N	\N	\N	0101000000EC332A1A121E53C066D51F158A3A4540
234	TGC4	290.92200000000003	Waypoint	TGC4	TGC4	\N	\N	\N	0101000000B5975353091E53C0544A90908F3A4540
235	TGC5	306.78399999999999	Waypoint	TGC5	TGC5	\N	\N	\N	010100000046AAF76D4B1D53C026C6077D763A4540
236	TGC6	306.78399999999999	Waypoint	TGC6	TGC6	\N	\N	\N	0101000000901E24E34F1D53C03121DC20773A4540
237	TGC7	306.78399999999999	Waypoint	TGC7	TGC7	\N	\N	\N	0101000000901E24E34F1D53C03121DC20773A4540
238	TGC8	391.37900000000002	Waypoint	TGC8	TGC8	\N	\N	\N	0101000000C869FC808F6753C0D0E8671AAFE54540
239	TGC9	274.33999999999997	Waypoint	TGC9	TGC9	\N	\N	\N	0101000000B512CC2E3E1D53C085F89CC8873A4540
240	THB-11	-10.208299999999999	Waypoint	THB-11	THB-11	\N	\N	\N	0101000000EDF7F92C258F37405DC13B9B83A24140
241	THB-13	-8.2856500000000004	Waypoint	THB-13	THB-13	\N	\N	\N	010100000052FFF8C9D38E37404CD05C3083A24140
242	THB-14	25.360199999999999	Waypoint	THB-14	THB-14	\N	\N	\N	01010000003617FE5AFE8E374062DA883E57A34140
243	THB-2	-32.318399999999997	Waypoint	THB-2	THB-2	\N	\N	\N	0101000000570A6AC06C8F374055D6E0C46AA24140
244	THB-3	-10.9292	Waypoint	THB-3	THB-3	\N	\N	\N	0101000000147F9F676F8F374047671CC768A24140
245	THB-6	-7.8049299999999997	Waypoint	THB-6	THB-6	\N	\N	\N	01010000003D83B17E668F3740B4C8FFAF7DA24140
246	THB-9	-8.7662300000000002	Waypoint	THB-9	THB-9	\N	\N	\N	010100000007FAFFC9438F3740466B63F17DA24140
247	THB1	-15.014799999999999	Waypoint	THB1	THB1	\N	\N	\N	0101000000AB8466583E8F3740584DC32F76A24140
248	THB5	-13.0922	Waypoint	THB5	THB5	\N	\N	\N	0101000000A9FBE907C1A03740B874649611A64140
249	THB7	-12.3712	Waypoint	THB7	THB7	\N	\N	\N	0101000000AAD9389F908F3740EBFA5E7369A24140
250	TPW3	271.69600000000003	Waypoint	TPW3	TPW3	\N	\N	\N	01010000003CDCC737BF1E53C017AC5C9B123B4540
251	TPW4	282.02999999999997	Waypoint	TPW4	TPW4	\N	\N	\N	0101000000BFC4BDD1C51E53C0CDCE4406293B4540
252	TPW5	290.44200000000001	Waypoint	TPW5	TPW5	\N	\N	\N	0101000000EBB90377CB1E53C006EFC7C7103B4540
253	TPW6	278.90600000000001	Waypoint	TPW6	TPW6	\N	\N	\N	010100000082B54BCAC61E53C0792F08F6293B4540
\.


--
-- Data for Name: download2; Type: TABLE DATA; Schema: public; Owner: webuser
--

COPY download2 (gid, name, elevation, symbol, "comment", descriptio, source, url, "url name", the_geom) FROM stdin;
0	001	286.59699999999998	Waypoint	001	001	\N	\N	\N	010100000053F67765001E53C07C3CF7957C3A4540
1	002	280.82900000000001	Waypoint	002	002	\N	\N	\N	0101000000C9E37BDCFD1D53C0FD275CE87A3A4540
2	003	302.45800000000003	Waypoint	003	003	\N	\N	\N	0101000000EB5D003D741D53C06FEDA2BAC93A4540
3	004	299.334	Waypoint	004	004	\N	\N	\N	0101000000207A01DF751D53C041EAF0BCC63A4540
4	005	298.613	Waypoint	005	005	\N	\N	\N	0101000000ABCB969D5F3438401944CC6EEB944740
5	006	296.93099999999998	Waypoint	006	006	\N	\N	\N	0101000000A0A500586A343840003E8CD1B9944740
6	007	282.75099999999998	Waypoint	007	007	\N	\N	\N	0101000000DEE54D3E70343840FC29C03DB0944740
7	008	304.14100000000002	Waypoint	008	008	\N	\N	\N	0101000000891E008A7434384029EACC3DA4944740
8	009	294.28699999999998	Waypoint	009	009	\N	\N	\N	010100000021C9E9417A343840F05C87869F944740
9	010	292.84500000000003	Waypoint	010	010	\N	\N	\N	01010000005DDB8F9F8334384064B6948E9A944740
10	011	301.01600000000002	Waypoint	011	011	\N	\N	\N	0101000000FD4FC18B853438407966CBB793944740
11	012	298.13200000000001	Waypoint	012	012	\N	\N	\N	0101000000D023CFC08B343840C3BE237E93944740
12	013	292.84500000000003	Waypoint	013	013	\N	\N	\N	0101000000834BFF46C6343840638733EADA944740
13	014	288.75999999999999	Waypoint	014	014	\N	\N	\N	0101000000D90CC188B534384051F6241DD6944740
14	015	299.815	Waypoint	015	015	\N	\N	\N	01010000008EF0C8E898343840A99198CB86944740
15	016	296.93099999999998	Waypoint	016	016	\N	\N	\N	0101000000027C6632643438402636F456EA944740
16	017	288.03899999999999	Waypoint	017	017	\N	\N	\N	0101000000F2B82E5060343840FF856CC0EA944740
17	018	313.51299999999998	Waypoint	018	018	\N	\N	\N	0101000000D3061676073438405D68531AD6944740
18	019	298.613	Waypoint	019	019	\N	\N	\N	0101000000DCACE008E9333840A989E0ADE3944740
19	020	310.38900000000001	Waypoint	020	020	\N	\N	\N	0101000000C3C89038EB333840910D3CA8D8944740
20	021	269.29300000000001	Waypoint	021	021	\N	\N	\N	01010000009503A86FBA3338407561B8FBDB944740
21	022	310.38900000000001	Waypoint	022	022	\N	\N	\N	0101000000B831FEFCC5333840B86FABDCE8944740
22	023	304.14100000000002	Waypoint	023	023	\N	\N	\N	0101000000D59AB8E5BB333840FE90C888D5944740
23	024	307.024	Waypoint	024	024	\N	\N	\N	0101000000BE525E77AF3338401EA80C49D1944740
24	025	578.83500000000004	Waypoint	025	025	\N	\N	\N	0101000000923018CB10A73940C78CAB2200894140
25	026	573.06700000000001	Waypoint	026	026	\N	\N	\N	01010000004496282213A73940FCFF8E61FB884140
26	027	612.48099999999999	Waypoint	027	027	\N	\N	\N	01010000008D0F2AA68FA639400B6D8CDFCC884140
27	028	1113.8	Waypoint	028	028	\N	\N	\N	0101000000E6C250A0F257404066ED5699607E4140
28	029	1113.8	Waypoint	029	029	\N	\N	\N	01010000005DBF0FF0ECF240404610475471D14140
29	APG14	43.144500000000001	Waypoint	APG14	APG14	\N	\N	\N	0101000000508077DE452640403C7213D149834140
30	APG19	37.3767	Waypoint	APG19	APG19	\N	\N	\N	0101000000A56B4CA64F2640406F7493ED43834140
31	APG20	54.680300000000003	Waypoint	APG20	APG20	\N	\N	\N	010100000043377F3C492640409B089C1E74834140
32	APG23	47.710799999999999	Waypoint	APG23	APG23	\N	\N	\N	010100000091936B052B2640407142C84986834140
33	APG4	28.003900000000002	Waypoint	APG4	APG4	\N	\N	\N	0101000000DD262301FF25404097925C3336834140
34	APG5	23.918299999999999	Waypoint	APG5	APG5	\N	\N	\N	0101000000FF1AB0AE01264040E934034333834140
35	APG6	40.741199999999999	Waypoint	APG6	APG6	\N	\N	\N	0101000000C73830CD352640401D78BF4E4D834140
36	AVALMSTNBW	324.56799999999998	Waypoint	AVALMSTNBW	AVALMSTNBW	\N	\N	\N	010100000000A8F4A969AB404009833FB7687F4140
37	AVAPESTLE	318.80099999999999	Waypoint	AVAPESTLE	AVAPESTLE	\N	\N	\N	01010000002575904460AB4040A0EDB20E707F4140
38	AVATR11NE	320.00200000000001	Waypoint	AVATR11NE	AVATR11NE	\N	\N	\N	0101000000006BB7586FAB40400F1FE062687F4140
39	AVATR11NW	320.964	Waypoint	AVATR11NW	AVATR11NW	\N	\N	\N	0101000000776CAB296EAB404062843CF7687F4140
40	AVATR11SE	320.964	Waypoint	AVATR11SE	AVATR11SE	\N	\N	\N	01010000004735BCBE6EAB40409526EC75677F4140
41	AVATR11SW	320.964	Waypoint	AVATR11SW	AVATR11SW	\N	\N	\N	0101000000FBC8739E6DAB4040E3AF24D4677F4140
42	AVATR12NE	320.483	Waypoint	AVATR12NE	AVATR12NE	\N	\N	\N	010100000013FCB8FB71AB40405F7FCBC96F7F4140
43	AVATR12NW	320.964	Waypoint	AVATR12NW	AVATR12NW	\N	\N	\N	010100000026FE4EB16FAB4040CCA2870C717F4140
44	AVATR12SE	322.16500000000002	Waypoint	AVATR12SE	AVATR12SE	\N	\N	\N	010100000040255B1B72AB40404407858A6F7F4140
45	AVATR12SW	321.68400000000003	Waypoint	AVATR12SW	AVATR12SW	\N	\N	\N	01010000009A9A18536FAB4040FC7C03AF707F4140
46	CFS-1	1564.4200000000001	Waypoint	CFS-1	CFS-1	\N	\N	\N	0101000000EDCE38DF587F404000BEFF82697A4140
47	CFS-4	1557.45	Waypoint	CFS-4	CFS-4	\N	\N	\N	0101000000EAA5573A477F4040E33F4725617A4140
48	CFS-5	1561.78	Waypoint	CFS-5	CFS-5	\N	\N	\N	01010000005729E41A447F4040DC40B468577A4140
49	CFS-8	1560.0899999999999	Waypoint	CFS-8	CFS-8	\N	\N	\N	010100000098AE3C55397F40409E1A0715577A4140
50	CFS-9	1549.76	Waypoint	CFS-9	CFS-9	\N	\N	\N	0101000000F8133F0D327F40402AF0B8225D7A4140
51	CKL12	24.398900000000001	Waypoint	CKL12	CKL12	\N	\N	\N	01010000001F9D64F260294040BF0EBD414B784140
52	CKL3	24.1586	Waypoint	CKL3	CKL3	\N	\N	\N	01010000004CB52B016C294040232B18C54D784140
53	CKL4	23.4376	Waypoint	CKL4	CKL4	\N	\N	\N	01010000004A2FB3CD6229404075D354B94A784140
54	CLA07	16.4681	Waypoint	CLA07	CLA07	\N	\N	\N	0101000000E69D4061DB274040A0C860B3067A4140
55	CLA12	17.429600000000001	Waypoint	CLA12	CLA12	\N	\N	\N	0101000000840C002FD52740409807F59C107A4140
56	CLA13	17.9102	Waypoint	CLA13	CLA13	\N	\N	\N	0101000000C6DDF020D52740407077C7430E7A4140
57	CLA19	25.840900000000001	Waypoint	CLA19	CLA19	\N	\N	\N	01010000005AAD586B042840404FD6E7EBEB794140
58	CLA2	24.639299999999999	Waypoint	CLA2	CLA2	\N	\N	\N	01010000001A540400D3274040182BEBB0227A4140
59	CLA20	16.4681	Waypoint	CLA20	CLA20	\N	\N	\N	010100000097F282D1EA274040F75187C1EE794140
60	CLA7	21.755400000000002	Waypoint	CLA7	CLA7	\N	\N	\N	010100000023DB6BD4D2274040B351708D1C7A4140
61	CPL1	1123.4200000000001	Waypoint	CPL1	CPL1	\N	\N	\N	0101000000C0F75A77C37640402A8A437534794140
62	CPL10	1116.6900000000001	Waypoint	CPL10	CPL10	\N	\N	\N	0101000000B967F3BEC57640409BEF969A33794140
63	CPL11	1111.8800000000001	Waypoint	CPL11	CPL11	\N	\N	\N	0101000000217A4043B97640407A32A42F29794140
64	CPL12	1130.8699999999999	Waypoint	CPL12	CPL12	\N	\N	\N	0101000000FCC0B8C8BB764040D3101B4D2A794140
65	CPL2	1133.75	Waypoint	CPL2	CPL2	\N	\N	\N	01010000009C6FD75ABE7640400BF09BCA3B794140
66	CPL3	1125.5799999999999	Waypoint	CPL3	CPL3	\N	\N	\N	0101000000D20BC004C3764040B1E0204F42794140
67	CTC-172	1832.3800000000001	Waypoint	CTC-172	CTC-172	\N	\N	\N	0101000000C0DA9F2BD46D4040B0E38F073A774140
68	CTC-175	1850.1700000000001	Waypoint	CTC-175	CTC-175	\N	\N	\N	0101000000FA809B20C56D40402BED3A3F38774140
69	CTC-178	1841.04	Waypoint	CTC-178	CTC-178	\N	\N	\N	01010000006A0CDF67C16D404090C36F2938774140
70	CTC-181	1837.9100000000001	Waypoint	CTC-181	CTC-181	\N	\N	\N	01010000006C5AD303B96D404050832F6D34774140
71	CTC130	1806.4300000000001	Waypoint	CTC130	CTC130	\N	\N	\N	0101000000FC0700AA126F404088014DAF4D784140
72	CTC133	1792.49	Waypoint	CTC133	CTC133	\N	\N	\N	0101000000D0B53459116F4040590ABD0857784140
73	CTC136	1793.45	Waypoint	CTC136	CTC136	\N	\N	\N	010100000094F26CEC186F4040C8D8E0AF4F784140
74	CTC138	1810.03	Waypoint	CTC138	CTC138	\N	\N	\N	010100000050EC78FEFA6E4040AFCEC4834C784140
75	CTC141	1840.8	Waypoint	CTC141	CTC141	\N	\N	\N	0101000000D9CA371A016F404078F8B488FD774140
76	CTC144	1822.77	Waypoint	CTC144	CTC144	\N	\N	\N	0101000000B3E0AB410B6F40404B17DCBB38784140
77	CTC150	1725.2	Waypoint	CTC150	CTC150	\N	\N	\N	010100000016A65CD232704040E22B03CC11764140
78	CTC153	1720.3900000000001	Waypoint	CTC153	CTC153	\N	\N	\N	010100000095FB106000704040230B899500764140
79	CTC156	1715.3399999999999	Waypoint	CTC156	CTC156	\N	\N	\N	0101000000478C8F30FB6F40404F2EAC6CF6754140
80	CTC159	1717.51	Waypoint	CTC159	CTC159	\N	\N	\N	01010000003FE2940FFA6F4040302B7CCCF3754140
81	CTC162	1768.22	Waypoint	CTC162	CTC162	\N	\N	\N	0101000000E1CCCB3BF06F4040EC98B37116764140
82	CTC165	1787.4400000000001	Waypoint	CTC165	CTC165	\N	\N	\N	01010000000BFC4BCCDF6F4040562E1C5417764140
83	CTC168	1806.4300000000001	Waypoint	CTC168	CTC168	\N	\N	\N	01010000003F7F448DAE6F40406DB51C220D764140
84	CTC68	1622.0999999999999	Waypoint	CTC68	CTC68	\N	\N	\N	010100000033FBFA296771404013FFFE34F8754140
85	CTC70	1619.21	Waypoint	CTC70	CTC70	\N	\N	\N	0101000000D8A167A147714040D587F34502764140
86	CTC72	1619.21	Waypoint	CTC72	CTC72	\N	\N	\N	01010000004AF6DFB551714040518BFB6EFE754140
87	CTC81	1858.3399999999999	Waypoint	CTC81	CTC81	\N	\N	\N	0101000000EC4EC8E5776F404018769CCBE2764140
88	CTC82	1850.8900000000001	Waypoint	CTC82	CTC82	\N	\N	\N	010100000081F00670716F40406D4C5C82E4764140
89	CTC83	1831.9000000000001	Waypoint	CTC83	CTC83	\N	\N	\N	010100000047082C0C936F4040D2FF0A4AE5764140
90	CTC84	1837.6700000000001	Waypoint	CTC84	CTC84	\N	\N	\N	0101000000A73264F3736F4040A732DB01D6764140
91	CTC87	1867.95	Waypoint	CTC87	CTC87	\N	\N	\N	0101000000CB1D84200A6E40401C3C97D248774140
92	CTC89	1861.46	Waypoint	CTC89	CTC89	\N	\N	\N	0101000000921914720A6E4040BFB87F254A774140
93	CTC91	1869.4000000000001	Waypoint	CTC91	CTC91	\N	\N	\N	0101000000253FB7D70F6E40406DD99BC452774140
94	CTC93	1869.1500000000001	Waypoint	CTC93	CTC93	\N	\N	\N	0101000000DD3D0B05166E4040722748D45A774140
95	CTC95	1885.02	Waypoint	CTC95	CTC95	\N	\N	\N	0101000000C83FBC792D6E404065F2635257774140
96	CTV42	1189.75	Waypoint	CTV42	CTV42	\N	\N	\N	01010000009A9CF3B6C65040407AAC073668844140
97	GARMIN	325.04899999999998	Waypoint	GARMIN	GARMIN	\N	\N	\N	01010000003581CE1623B357C013B87FA9826D4340
98	GRMEUR	35.934699999999999	Waypoint	GRMEUR	GRMEUR	\N	\N	\N	01010000001E909861226CF7BF229CA71ECF7D4940
99	GRMPHX	361.09800000000001	Waypoint	GRMPHX	GRMPHX	\N	\N	\N	0101000000D0B1FD108DFC5BC022360CAA43AA4040
100	GRMTWN	38.097700000000003	Waypoint	GRMTWN	GRMTWN	\N	\N	\N	01010000001E631221FA685E40183ACF08D10F3940
101	SCV-100	1109.96	Waypoint	SCV-100	SCV-100	\N	\N	\N	01010000004967C8C7DA574040A60BA788307E4140
102	SCV-86	1109.48	Waypoint	SCV-86	SCV-86	\N	\N	\N	0101000000541F1034DB5740402A45DCB02E7E4140
103	SCV-87	1081.3599999999999	Waypoint	SCV-87	SCV-87	\N	\N	\N	010100000074FAF3CDF357404019249BB05F7E4140
104	SCV108	1132.79	Waypoint	SCV108	SCV108	\N	\N	\N	01010000008DE3432D265840408777236DCF7E4140
105	SCV11	1183.74	Waypoint	SCV11	SCV11	\N	\N	\N	0101000000679543D281574040C5B6176122804140
106	SCV13	1190.71	Waypoint	SCV13	SCV13	\N	\N	\N	01010000002C98772393574040F95224D11E804140
107	SCV130	1242.3800000000001	Waypoint	SCV130	SCV130	\N	\N	\N	0101000000B515E7E7C7574040D7D8EBFEED7E4140
108	SCV136	1224.1199999999999	Waypoint	SCV136	SCV136	\N	\N	\N	010100000049959316CF5740408544C32AEA7E4140
109	SCV138	1146.73	Waypoint	SCV138	SCV138	\N	\N	\N	01010000008578EF6B0E5840407CF9BB7CCD7E4140
110	SCV15	1184.9400000000001	Waypoint	SCV15	SCV15	\N	\N	\N	0101000000B04B105290574040B05CD4C60F804140
111	SCV2	1211.3800000000001	Waypoint	SCV2	SCV2	\N	\N	\N	0101000000186323BA6E5740406EEFD6D628804140
112	SCV21	1187.3399999999999	Waypoint	SCV21	SCV21	\N	\N	\N	0101000000348D446D7D574040D0DCC4420D804140
113	SCV36	1392.3399999999999	Waypoint	SCV36	SCV36	\N	\N	\N	0101000000567498ED125740407B7967D85B7F4140
114	SCV37	1394.51	Waypoint	SCV37	SCV37	\N	\N	\N	01010000004586DBF520574040F30C0DD0657F4140
115	SCV38	1405.5599999999999	Waypoint	SCV38	SCV38	\N	\N	\N	0101000000066C679522574040CDFE1080577F4140
116	SCV39	1388.02	Waypoint	SCV39	SCV39	\N	\N	\N	01010000009ECAB8951D574040A76EBF605D7F4140
117	SCV4	1191.1900000000001	Waypoint	SCV4	SCV4	\N	\N	\N	0101000000B808590469574040CD1211BC29804140
118	SCV40	1396.9100000000001	Waypoint	SCV40	SCV40	\N	\N	\N	01010000003C777B43335740404E291C323F7F4140
119	SCV41	1407.73	Waypoint	SCV41	SCV41	\N	\N	\N	0101000000A39A80C9C756404042C68F709B7F4140
120	SCV42	1398.1099999999999	Waypoint	SCV42	SCV42	\N	\N	\N	0101000000BF460476C4564040431D602A997F4140
121	SCV43	1400.28	Waypoint	SCV43	SCV43	\N	\N	\N	0101000000D4FA749FD05640404297E7F68F7F4140
122	SCV44	1414.9400000000001	Waypoint	SCV44	SCV44	\N	\N	\N	0101000000C59153B0D0564040849F7F558A7F4140
123	SCV62	1416.6199999999999	Waypoint	SCV62	SCV62	\N	\N	\N	010100000033EFEF6ED0564040A41F4C0B907F4140
124	SCV77	1225.5599999999999	Waypoint	SCV77	SCV77	\N	\N	\N	0101000000B9754B33DC574040D3F8C773247F4140
125	SCV78	1207.53	Waypoint	SCV78	SCV78	\N	\N	\N	0101000000BC6F845EE2574040D12794B3277F4140
126	SCV82	1229.8800000000001	Waypoint	SCV82	SCV82	\N	\N	\N	01010000002D851C68B957404009B6C3BF457F4140
127	SCV83	1255.8399999999999	Waypoint	SCV83	SCV83	\N	\N	\N	0101000000BA7384B3AB57404069EDFA284A7F4140
128	SCV93	1146.97	Waypoint	SCV93	SCV93	\N	\N	\N	010100000088AC081313584040B252E88CCD7E4140
129	SCV94	1131.3499999999999	Waypoint	SCV94	SCV94	\N	\N	\N	01010000001792707A07584040455F537DAD7E4140
130	SCV95	1173.1700000000001	Waypoint	SCV95	SCV95	\N	\N	\N	0101000000F90BDDEF0758404047600CE2CC7E4140
131	STV-15	1079.6800000000001	Waypoint	STV-15	STV-15	\N	\N	\N	010100000031396C5DD55040403387EBEB55844140
132	STV-30	1091.45	Waypoint	STV-30	STV-30	\N	\N	\N	0101000000158F4495C55040401492502342844140
133	STV-31	1096.5	Waypoint	STV-31	STV-31	\N	\N	\N	0101000000AF9B1F6DC65040408D50649444844140
134	STV-34	1080.4000000000001	Waypoint	STV-34	STV-34	\N	\N	\N	0101000000A64B5F05C15040401C70AC7737844140
135	STV-39	1099.1400000000001	Waypoint	STV-39	STV-39	\N	\N	\N	010100000087259C18BD504040B09AE4D02D844140
136	STV11	1076.0699999999999	Waypoint	STV11	STV11	\N	\N	\N	0101000000555FC3F7F1504040A532588055844140
137	STV23	1053.24	Waypoint	STV23	STV23	\N	\N	\N	010100000024635788BE5040409D308CB447844140
138	STV29	1063.3399999999999	Waypoint	STV29	STV29	\N	\N	\N	010100000012524FD0BA5040402BAE485149844140
139	STV41	1097.9400000000001	Waypoint	STV41	STV41	\N	\N	\N	01010000008C7B471BB4504040C06EF7C326844140
140	STV45	1117.4100000000001	Waypoint	STV45	STV45	\N	\N	\N	01010000005A96B73CB9504040859577C658844140
141	STV48	969.36800000000005	Waypoint	STV48	STV48	\N	\N	\N	0101000000F345C49247514040C21D4F9B16834140
142	STV51	991.71799999999996	Waypoint	STV51	STV51	\N	\N	\N	0101000000E18930B054514040517878EB16834140
143	STV56	994.12199999999996	Waypoint	STV56	STV56	\N	\N	\N	0101000000725B3C6B6051404023929F8F1D834140
144	STV58	980.18299999999999	Waypoint	STV58	STV58	\N	\N	\N	0101000000D537382A5A5140408249F4A918834140
145	STV61	885.97400000000005	Waypoint	STV61	STV61	\N	\N	\N	0101000000F7DABFF4AE4F4040A5C09F6DF77F4140
146	STV62	891.50199999999995	Waypoint	STV62	STV62	\N	\N	\N	0101000000F6C02C8CAB4F40404E505363F37F4140
147	STV65	888.85799999999995	Waypoint	STV65	STV65	\N	\N	\N	0101000000189E4844B94F40400D53F306EC7F4140
148	STV9	1072.95	Waypoint	STV9	STV9	\N	\N	\N	01010000001020CCB8EC50404050BC44A157844140
\.


--
-- Data for Name: download3; Type: TABLE DATA; Schema: public; Owner: webuser
--

COPY download3 (gid, name, elevation, symbol, "comment", descriptio, source, url, "url name", the_geom) FROM stdin;
0	031	23.4376	Waypoint	031	031	\N	\N	\N	0101000000953498E9CBB040406B64EF76915F4140
1	032	279.14600000000002	Waypoint	032	032	\N	\N	\N	01010000001806480EFAAD4040DA0DD3A55C634140
2	033	97.218299999999999	Waypoint	033	033	\N	\N	\N	0101000000DBD0671C37AC4040197F9BF8BC604140
3	034	123.17400000000001	Waypoint	034	034	\N	\N	\N	010100000025CC4C1A37AC40408A6A8B17BD604140
4	035	122.93300000000001	Waypoint	035	035	\N	\N	\N	0101000000B702CB9031AC404052830BA7BC604140
5	036	122.93300000000001	Waypoint	036	036	\N	\N	\N	0101000000B702CB9031AC404052830BA7BC604140
6	037	122.93300000000001	Waypoint	037	037	\N	\N	\N	0101000000B702CB9031AC404052830BA7BC604140
7	038	89.768100000000004	Waypoint	038	038	\N	\N	\N	0101000000E8ACFB3958AC404055F12890B7604140
8	039	56.602899999999998	Waypoint	039	039	\N	\N	\N	0101000000400E10EDDF052D40E0115C7FA7F24140
9	040	56.602899999999998	Waypoint	040	040	\N	\N	\N	0101000000C1C63CC5860B2D40C60B548C94F14140
10	041	56.602899999999998	Waypoint	041	041	\N	\N	\N	0101000000C1C63CC5860B2D40C60B548C94F14140
11	042	471.40899999999999	Waypoint	042	042	\N	\N	\N	0101000000DB9958CC075A404087F3A8AC31774140
12	043	473.09100000000001	Waypoint	043	043	\N	\N	\N	010100000086C39815065A40402F89FFB02F774140
13	044	472.37	Waypoint	044	044	\N	\N	\N	01010000008834FC37075A4040367C70162D774140
14	045	464.43900000000002	Waypoint	045	045	\N	\N	\N	01010000008D33B3BA085A404032F186B52A774140
15	046	618.97000000000003	Waypoint	046	046	\N	\N	\N	0101000000AD03CCEE9C5B404048D42F0405794140
16	047	788.40099999999995	Waypoint	047	047	\N	\N	\N	0101000000EFD438A8F85240402A3A9784397A4140
17	048	808.34799999999996	Waypoint	048	048	\N	\N	\N	0101000000C51664C6F95240405F19608E3A7A4140
18	049	809.06899999999996	Waypoint	049	049	\N	\N	\N	010100000000A927D5F9524040B0AA4F533A7A4140
19	050	328.41399999999999	Waypoint	050	050	\N	\N	\N	010100000085FBBC3A6BAB404025397CAC827F4140
20	051	328.654	Waypoint	051	051	\N	\N	\N	0101000000140A37376BAB4040DB3D97AE827F4140
21	052	311.83100000000002	Waypoint	052	052	\N	\N	\N	01010000007E295F778AAB40402CF7AEB3747F4140
22	053	311.35000000000002	Waypoint	053	053	\N	\N	\N	01010000002BFEE25E88AB404071F97F3D757F4140
23	054	311.35000000000002	Waypoint	054	054	\N	\N	\N	0101000000CC09688F88AB4040F7A31888747F4140
24	055	311.35000000000002	Waypoint	055	055	\N	\N	\N	01010000003AA787D888AB4040C72610AE747F4140
25	056	311.35000000000002	Waypoint	056	056	\N	\N	\N	01010000009C2FECEC88AB404056989357747F4140
26	057	312.31200000000001	Waypoint	057	057	\N	\N	\N	0101000000FA6F233A8BAB404009B2E730747F4140
27	058	313.27300000000002	Waypoint	058	058	\N	\N	\N	01010000009B5EB82C8CAB404059E6632D747F4140
28	059	313.75400000000002	Waypoint	059	059	\N	\N	\N	01010000002C5E8F368CAB4040E54F3070747F4140
29	060	313.75400000000002	Waypoint	060	060	\N	\N	\N	01010000003B0734B08CAB40409EE834B7747F4140
30	061	313.75400000000002	Waypoint	061	061	\N	\N	\N	01010000003BAAC0E78CAB404095BE40AB747F4140
31	062	313.51299999999998	Waypoint	062	062	\N	\N	\N	0101000000AC0F140D8CAB4040517CEC96737F4140
32	063	313.27300000000002	Waypoint	063	063	\N	\N	\N	0101000000D3A2AB658CAB40408FFF0CB3737F4140
33	064	320.00200000000001	Waypoint	064	064	\N	\N	\N	0101000000D3DE0B8C6FAB40402BC5E4C65A7F4140
34	065	318.80099999999999	Waypoint	065	065	\N	\N	\N	01010000004939BB5C94AB40404C7AD83E767F4140
35	066	319.041	Waypoint	066	066	\N	\N	\N	010100000066B1D0AB9AAB404017E128A4757F4140
36	067	302.93900000000002	Waypoint	067	067	\N	\N	\N	01010000002A9A5086BFB34040E2008828577D4140
37	068	302.93900000000002	Waypoint	068	068	\N	\N	\N	01010000002A9A5086BFB34040E2008828577D4140
38	069	473.09100000000001	Waypoint	069	069	\N	\N	\N	01010000009DFB10F3A39340402227DCC34F864140
39	070	355.08999999999997	Waypoint	070	070	\N	\N	\N	01010000007D0B41C7B49C404007AFBB4B72834140
40	071	355.08999999999997	Waypoint	071	071	\N	\N	\N	0101000000E29A7F179D9C4040BBB0B4DA63834140
41	072	355.08999999999997	Waypoint	072	072	\N	\N	\N	0101000000F6CF8CAB759C404079C78C644D834140
42	073	355.08999999999997	Waypoint	073	073	\N	\N	\N	01010000004ECB17CD6D9C404023EE939E47834140
43	074	355.08999999999997	Waypoint	074	074	\N	\N	\N	010100000081AA049D669C4040A493DF8145834140
44	075	355.08999999999997	Waypoint	075	075	\N	\N	\N	01010000001CE1875F3E9C4040133F70DC3C834140
45	076	355.32999999999998	Waypoint	076	076	\N	\N	\N	0101000000693FF4D2049C40404C14381131834140
46	077	355.32999999999998	Waypoint	077	077	\N	\N	\N	01010000007EF4E3B5E69B4040340479D62B834140
47	078	370.23099999999999	Waypoint	078	078	\N	\N	\N	01010000001570EB48979B4040DF5F60FDEF824140
48	079	378.88200000000001	Waypoint	079	079	\N	\N	\N	0101000000ABFF646D829B4040BADBB86FBC824140
49	080	381.286	Waypoint	080	080	\N	\N	\N	01010000004077B437599B404033F31C45A1824140
50	081	381.286	Waypoint	081	081	\N	\N	\N	0101000000A2E6049D2B9B404016899CCD89824140
51	082	383.68900000000002	Waypoint	082	082	\N	\N	\N	01010000007705C134239B404020F2CB9D2F824140
52	083	387.05399999999997	Waypoint	083	083	\N	\N	\N	010100000098D048771C9B404014A253C219824140
53	084	387.29399999999998	Waypoint	084	084	\N	\N	\N	0101000000310314711A9B4040F38490B913824140
54	085	387.53399999999999	Waypoint	085	085	\N	\N	\N	01010000007D17AFAC069B40404F4B88C60E824140
55	086	390.41800000000001	Waypoint	086	086	\N	\N	\N	01010000002D02DD43349B404070A1074BB0814140
56	087	406.75999999999999	Waypoint	087	087	\N	\N	\N	0101000000A20A7514449B40402DB683F0AC814140
57	088	407.00099999999998	Waypoint	088	088	\N	\N	\N	01010000005E677337309B40402D62EC45B3814140
58	089	408.44299999999998	Waypoint	089	089	\N	\N	\N	010100000011E3F392189B4040C83720B1B9814140
59	090	408.44299999999998	Waypoint	090	090	\N	\N	\N	01010000008DB75342099B4040D68F73B9C6814140
60	091	408.44299999999998	Waypoint	091	091	\N	\N	\N	0101000000CBDD0096099B4040D2535B1FD3814140
61	092	409.64400000000001	Waypoint	092	092	\N	\N	\N	0101000000911FB7800C9B4040260E2C89E4814140
62	093	409.88499999999999	Waypoint	093	093	\N	\N	\N	0101000000F38A2B570D9B4040B8B3C8D9E8814140
63	094	409.88499999999999	Waypoint	094	094	\N	\N	\N	0101000000F06584B20E9B404061071CA901824140
64	095	6.37439	Waypoint	095	095	\N	\N	\N	0101000000B5AA80D657AA4040B478EBDB8D5C4140
65	096	417.57499999999999	Waypoint	096	096	\N	\N	\N	01010000004B2FE8BF329C4040CCC3285925844140
66	097	396.18599999999998	Waypoint	097	097	\N	\N	\N	01010000002E36A85B08A74040790FF38C0D7E4140
67	098	395.22500000000002	Waypoint	098	098	\N	\N	\N	01010000008B2DA4C6FBA64040775F77B5BA7E4140
68	099	309.66800000000001	Waypoint	099	099	\N	\N	\N	010100000027501FDC38AB4040DEED93066B7F4140
69	100	322.64600000000002	Waypoint	100	100	\N	\N	\N	0101000000FE2A589466AB4040A2B2C477607F4140
70	101	299.09399999999999	Waypoint	101	101	\N	\N	\N	0101000000E9B5A46639AB404043213CB96A7F4140
71	102	302.21800000000002	Waypoint	102	102	\N	\N	\N	0101000000ECBD5BCD38AB4040290669426A7F4140
72	103	406.27999999999997	Waypoint	103	103	\N	\N	\N	0101000000AB6A639B3D944040B4646FD81F864140
73	104	404.35700000000003	Waypoint	104	104	\N	\N	\N	0101000000EF261BB63D9440401C266BEB1F864140
74	105	404.83800000000002	Waypoint	105	105	\N	\N	\N	010100000063A6F4193E944040E1F6B0891F864140
75	106	404.83800000000002	Waypoint	106	106	\N	\N	\N	0101000000ACA8E7FB6EAB40401B349B97687F4140
76	107	-7.8049299999999997	Waypoint	107	107	\N	\N	\N	01010000007C46F7B3258F3740BA8190997CA24140
77	108	323.36700000000002	Waypoint	108	108	\N	\N	\N	01010000008E7C47551F51404085C41F4064844140
78	109	323.36700000000002	Waypoint	109	109	\N	\N	\N	01010000008E7C47551F51404085C41F4064844140
79	110	323.36700000000002	Waypoint	110	110	\N	\N	\N	01010000008E7C47551F51404085C41F4064844140
80	111	1119.0899999999999	Waypoint	111	111	\N	\N	\N	0101000000E1A5FCEDF050404048584C5361844140
81	112	28.003900000000002	Waypoint	112	112	\N	\N	\N	0101000000B1981898432940400EF08BE86A784140
82	113	28.965299999999999	Waypoint	113	113	\N	\N	\N	010100000022C48B41442940400E16584470784140
83	114	27.763500000000001	Waypoint	114	114	\N	\N	\N	0101000000831210DA452940407F1E38A16F784140
84	115	27.763500000000001	Waypoint	115	115	\N	\N	\N	010100000072A470934A29404015ECD86B6E784140
85	116	26.081199999999999	Waypoint	116	116	\N	\N	\N	01010000005CC8D7294C294040B0CC44D967784140
86	117	27.523199999999999	Waypoint	117	117	\N	\N	\N	010100000024FE47F74A29404088BC106B64784140
87	118	27.523199999999999	Waypoint	118	118	\N	\N	\N	01010000004D1F33B04B294040C488B4F562784140
88	119	46.268900000000002	Waypoint	119	119	\N	\N	\N	01010000008FE004AE442640400FE90BAF91834140
89	120	46.028599999999997	Waypoint	120	120	\N	\N	\N	01010000007A61DF0C462640401A44E05292834140
90	121	47.2301	Waypoint	121	121	\N	\N	\N	01010000001C2A84DD49264040D50A8C228F834140
91	122	1854.98	Waypoint	122	122	\N	\N	\N	01010000008ED520AB1F6E4040D226FBA54D774140
92	15311SW	92.892499999999998	Waypoint	15311SW	15311SW	\N	\N	\N	010100000073C10CD2FBAB40406E57DB59A3604140
93	15316SW	98.900599999999997	Waypoint	15316SW	15316SW	\N	\N	\N	01010000001BA6586300AC40401BE1A3B4AD604140
94	15320NE	89.768100000000004	Waypoint	15320NE	15320NE	\N	\N	\N	01010000001A15CB3C58AC404004BDAC93B7604140
95	15325NW	86.403599999999997	Waypoint	15325NW	15325NW	\N	\N	\N	01010000004091083C59AC4040C56ED7FFC4604140
96	15325NWBIS	88.085800000000006	Waypoint	15325NWBIS	15325NWBIS	\N	\N	\N	010100000009C7780958AC40406808B090C5604140
97	247 START	503.85300000000001	Waypoint	247 START	247 START	\N	\N	\N	0101000000CF529B004F9A40405671576DC6804140
98	63403WALL	117.166	Waypoint	63403WALL	63403WALL	\N	\N	\N	0101000000F570B38CD5AB404087C06B7920614140
99	63405T4	122.693	Waypoint	63405T4	63405T4	\N	\N	\N	0101000000A69DC092F0AB40404E68642027614140
100	65203SDS	116.20399999999999	Waypoint	65203SDS	65203SDS	\N	\N	\N	01010000006DD5B00AD4AB40407D59170DD2604140
101	65212WALL	100.583	Waypoint	65212WALL	65212WALL	\N	\N	\N	0101000000B63BABD2C4AB40407607B334F0604140
102	65420TC1	108.273	Waypoint	65420TC1	65420TC1	\N	\N	\N	0101000000430A247CFDAB40402283C3A8B7604140
103	65420TC2	107.55200000000001	Waypoint	65420TC2	65420TC2	\N	\N	\N	0101000000F34F0B86FCAB404004BDAC93B7604140
104	65420TC3	107.312	Waypoint	65420TC3	65420TC3	\N	\N	\N	010100000099DA4024FDAB4040E4053971B7604140
105	654TC4	107.55200000000001	Waypoint	654TC4	654TC4	\N	\N	\N	01010000005749D3BCFDAB4040F24BD43DB8604140
106	AG06 1	405.31799999999998	Waypoint	AG06 1	AG06 1	\N	\N	\N	010100000045F4FE4E399440405C37032E18864140
107	AG06 2	406.03899999999999	Waypoint	AG06 2	AG06 2	\N	\N	\N	0101000000EB04D1E63A944040F800F17B1A864140
108	AG06 3	406.51999999999998	Waypoint	AG06 3	AG06 3	\N	\N	\N	0101000000FA2143823A944040C7F2376C1E864140
109	AG06 4	391.13900000000001	Waypoint	AG06 4	AG06 4	\N	\N	\N	0101000000EB24075E40944040D17CD84F22864140
110	AG06 5	406.03899999999999	Waypoint	AG06 5	AG06 5	\N	\N	\N	010100000003D50C973F9440400B2CB06219864140
111	AG06 6	404.59800000000001	Waypoint	AG06 6	AG06 6	\N	\N	\N	01010000003E0724CE3B9440401915D8661A864140
112	AG06 7	402.19400000000002	Waypoint	AG06 7	AG06 7	\N	\N	\N	0101000000A8FC5C4E38944040C8C36B2C1B864140
113	AG06 8	400.27199999999999	Waypoint	AG06 8	AG06 8	\N	\N	\N	01010000006511D9F3349440403F111C7B1C864140
114	AGRO KF1	398.589	Waypoint	AGRO KF1	AGRO KF1	\N	\N	\N	0101000000A040935535944040A980B80118864140
115	AGRO KF2	402.19400000000002	Waypoint	AGRO KF2	AGRO KF2	\N	\N	\N	0101000000992034CC5A9440404ACCC0BC18864140
116	AGRO KF3	401.47300000000001	Waypoint	AGRO KF3	AGRO KF3	\N	\N	\N	01010000006515FC5752944040656D0C6722864140
117	AGRO KF4	398.82999999999998	Waypoint	AGRO KF4	AGRO KF4	\N	\N	\N	0101000000624648423A9440403CA0B8581B864140
118	AGRO3CORE1	466.84199999999998	Waypoint	AGRO3CORE1	AGRO3CORE1	\N	\N	\N	010100000026AB27959E934040FD1E5FBC4B864140
119	AGRO3CORE2	478.61900000000003	Waypoint	AGRO3CORE2	AGRO3CORE2	\N	\N	\N	0101000000856B58CD9F934040B8E8439B4C864140
120	AGROCAVE	472.61000000000001	Waypoint	AGROCAVE	AGROCAVE	\N	\N	\N	0101000000C22BC3649C9340406971E73E50864140
121	AGROCAVE2	449.05799999999999	Waypoint	AGROCAVE2	AGROCAVE2	\N	\N	\N	0101000000CF136CAFC793404019A2D0D33C864140
122	AGROCORE1	398.82999999999998	Waypoint	AGROCORE1	AGROCORE1	\N	\N	\N	010100000000AD081D45944040001498581D864140
123	AGROCORE2	401.233	Waypoint	AGROCORE2	AGROCORE2	\N	\N	\N	0101000000D92713C635944040BBFA6C751D864140
124	AGROCORE3	405.31799999999998	Waypoint	AGROCORE3	AGROCORE3	\N	\N	\N	0101000000066360F73F94404042DCAC2C17864140
125	AGROCORE4	404.59800000000001	Waypoint	AGROCORE4	AGROCORE4	\N	\N	\N	0101000000E665D3653F944040981219BB1C864140
126	AGROK1	401.95400000000001	Waypoint	AGROK1	AGROK1	\N	\N	\N	0101000000F191FF8F349440402D034DD21C864140
127	AGROQUERN	476.45600000000002	Waypoint	AGROQUERN	AGROQUERN	\N	\N	\N	0101000000DD8FE859A1934040B8A8C0104C864140
128	AGRORCTOMB	403.63600000000002	Waypoint	AGRORCTOMB	AGRORCTOMB	\N	\N	\N	0101000000947313C9D89340406866686711864140
129	AGROSITE2	460.59399999999999	Waypoint	AGROSITE2	AGROSITE2	\N	\N	\N	010100000002B97803C293404048AB18B63F864140
130	AGROSITE3	473.09100000000001	Waypoint	AGROSITE3	AGROSITE3	\N	\N	\N	0101000000164E3F99A09340405D2D849044864140
131	AGROSITE4	465.88099999999997	Waypoint	AGROSITE4	AGROSITE4	\N	\N	\N	0101000000D5EEB2BAB0934040059AC8FF36864140
132	AGROTREE1	403.39600000000002	Waypoint	AGROTREE1	AGROTREE1	\N	\N	\N	0101000000F8DF87D94494404070C5A7FB1E864140
133	ALAM K CAV	296.69	Waypoint	ALAM K CAV	ALAM K CAV	\N	\N	\N	01010000009D55506559B340404905649B537D4140
134	ALAM K L2	282.99200000000002	Waypoint	ALAM K L2	ALAM K L2	\N	\N	\N	010100000049FBD019D9B34040E90DB0BC4F7D4140
135	ALAM K TR1	295.00799999999998	Waypoint	ALAM K TR1	ALAM K TR1	\N	\N	\N	01010000006E22A76DCBB340405466DB4D567D4140
136	ALAM K TR2	284.91399999999999	Waypoint	ALAM K TR2	ALAM K TR2	\N	\N	\N	01010000005F7D3CF4DDB34040E4B4CBAA547D4140
137	ALAM K1	282.02999999999997	Waypoint	ALAM K1	ALAM K1	\N	\N	\N	0101000000EAF8C072EAB34040D688C346577D4140
138	ALAM KL1	282.99200000000002	Waypoint	ALAM KL1	ALAM KL1	\N	\N	\N	0101000000D3393C0DE3B340405E27239E547D4140
139	ALAMS CVE1	297.892	Waypoint	ALAMS CVE1	ALAMS CVE1	\N	\N	\N	010100000021291F0B5CB3404048119D8E557D4140
140	ALAMSCV GP	298.613	Waypoint	ALAMSCV GP	ALAMSCV GP	\N	\N	\N	010100000059963B755DB34040728FFB0F567D4140
141	ANG-10	1597.3399999999999	Waypoint	ANG-10	ANG-10	\N	\N	\N	0101000000CF2FB76C8E1638407B3F4FB2FEA14140
142	ANG-11	1589.6500000000001	Waypoint	ANG-11	ANG-11	\N	\N	\N	0101000000460126996A16384053CAA8880CA24140
143	ANG-12	1588.45	Waypoint	ANG-12	ANG-12	\N	\N	\N	0101000000A6DE460F6B163840E30FBDC704A24140
144	ANG-13	1594.46	Waypoint	ANG-13	ANG-13	\N	\N	\N	01010000003ADAC9C4531638409D4E70B913A24140
145	ANG-9	1590.1300000000001	Waypoint	ANG-9	ANG-9	\N	\N	\N	010100000087B1615A8F1638406C5F03CC03A24140
146	ANG5	1606.48	Waypoint	ANG5	ANG5	\N	\N	\N	0101000000F71A9E386216384007C22FA42CA24140
147	APG1	16.4681	Waypoint	APG1	APG1	\N	\N	\N	0101000000626457C7222640407CD1C37531834140
148	APG13	45.067300000000003	Waypoint	APG13	APG13	\N	\N	\N	01010000005083D4B3412640403E04A1315D834140
149	APG15	44.346299999999999	Waypoint	APG15	APG15	\N	\N	\N	010100000079643CE2412640403147C4D16B834140
150	APG2	33.531500000000001	Waypoint	APG2	APG2	\N	\N	\N	0101000000186F08AE222640401A8C1BFB35834140
151	APG22	48.191499999999998	Waypoint	APG22	APG22	\N	\N	\N	01010000003105401C3126404085763F8893834140
152	AS13P1	394.50400000000002	Waypoint	AS13P1	AS13P1	\N	\N	\N	010100000059BABFD05CA740403F15BBA6957D4140
153	AS19A	383.68900000000002	Waypoint	AS19A	AS19A	\N	\N	\N	0101000000AE2D20DAE5A5404043F53CA3A07D4140
154	AS19D	386.81299999999999	Waypoint	AS19D	AS19D	\N	\N	\N	010100000092EA2423F3A54040AD279CD8A17D4140
155	AS20A	372.39400000000001	Waypoint	AS20A	AS20A	\N	\N	\N	010100000085B587763BA640406D691C87CD7D4140
156	AS20D	384.17000000000002	Waypoint	AS20D	AS20D	\N	\N	\N	010100000072CDEFC484A64040205A472FCC7D4140
157	AS21A	389.93799999999999	Waypoint	AS21A	AS21A	\N	\N	\N	0101000000745C20509CA64040ED3AD7D4D27D4140
158	AS21P1	383.92899999999997	Waypoint	AS21P1	AS21P1	\N	\N	\N	0101000000AAFDCEDD8BA64040D493C455CF7D4140
159	AS22A	385.61200000000002	Waypoint	AS22A	AS22A	\N	\N	\N	0101000000A92BF89DC1A640402BA3875DB87D4140
160	AS22D	412.76900000000001	Waypoint	AS22D	AS22D	\N	\N	\N	01010000006CF612F304A740404FDAACDEC77D4140
161	AS23A	390.178	Waypoint	AS23A	AS23A	\N	\N	\N	01010000009A50F31B47A740402DCA0C1EDD7D4140
162	AS24A	413.73000000000002	Waypoint	AS24A	AS24A	\N	\N	\N	0101000000305297A8F8A64040BDE7D456EB7D4140
163	AS24B	400.03100000000001	Waypoint	AS24B	AS24B	\N	\N	\N	0101000000589F0868F6A640403AF238F3087E4140
164	AS24D	407.48099999999999	Waypoint	AS24D	AS24D	\N	\N	\N	0101000000E74B0F0DC2A640409210339B0A7E4140
165	AS25B	387.77499999999998	Waypoint	AS25B	AS25B	\N	\N	\N	0101000000A23387A98FA640402915570C0C7E4140
166	AS26A	384.89100000000002	Waypoint	AS26A	AS26A	\N	\N	\N	0101000000C36AF4B68EA6404097016CE2127E4140
167	AS26C	408.68299999999999	Waypoint	AS26C	AS26C	\N	\N	\N	0101000000E05E348CC4A64040D353AC66127E4140
168	AS27A	384.41000000000003	Waypoint	AS27A	AS27A	\N	\N	\N	0101000000C6B42FD308A740403B2F5FA80D7E4140
169	AS27B	389.21699999999998	Waypoint	AS27B	AS27B	\N	\N	\N	0101000000741289C30BA74040170609AA367E4140
170	AS27EX5	391.62	Waypoint	AS27EX5	AS27EX5	\N	\N	\N	0101000000EA239F40E0A64040BFD53F2A337E4140
171	AS28A	386.57299999999998	Waypoint	AS28A	AS28A	\N	\N	\N	01010000007D99F0970BA740404FF0CE533D7E4140
172	AS28B	370.71100000000001	Waypoint	AS28B	AS28B	\N	\N	\N	010100000092E17BF611A74040873E7BD55A7E4140
173	AS28C	376.71899999999999	Waypoint	AS28C	AS28C	\N	\N	\N	010100000098BD5857C6A64040E6C704A1617E4140
174	AS29A	369.99000000000001	Waypoint	AS29A	AS29A	\N	\N	\N	0101000000C18E88F49EA64040530E5430647E4140
175	AS29F1	372.15300000000002	Waypoint	AS29F1	AS29F1	\N	\N	\N	0101000000C8D1B475C4A64040CCF7ECB6547E4140
176	AS30A	369.99000000000001	Waypoint	AS30A	AS30A	\N	\N	\N	01010000000B48301270A64040E67010216C7E4140
177	AS30EX6	363.74200000000002	Waypoint	AS30EX6	AS30EX6	\N	\N	\N	01010000000B3B786598A64040B85EEC20637E4140
178	AS31A	359.89600000000002	Waypoint	AS31A	AS31A	\N	\N	\N	0101000000C2C00B0C6DA64040F0FAB004707E4140
179	AS31C	382.48700000000002	Waypoint	AS31C	AS31C	\N	\N	\N	010100000052066720B1A64040428067D66D7E4140
180	AS32A	378.642	Waypoint	AS32A	AS32A	\N	\N	\N	0101000000BFF69E5AD5A64040C1B44F976A7E4140
181	AUG04T06	781.67200000000003	Waypoint	AUG04T06	AUG04T06	\N	\N	\N	01010000003FC18BDF905C4040206C885C36794140
182	AUG04T07	779.26800000000003	Waypoint	AUG04T07	AUG04T07	\N	\N	\N	0101000000814387448A5C4040DF4EDBEC33794140
183	AUG04T08	836.46699999999998	Waypoint	AUG04T08	AUG04T08	\N	\N	\N	01010000007E671B829A5C40409226505746794140
184	AUG04T09	798.97500000000002	Waypoint	AUG04T09	AUG04T09	\N	\N	\N	0101000000C831FC21A25C4040990250C74E794140
185	AUG04T10	850.40599999999995	Waypoint	AUG04T10	AUG04T10	\N	\N	\N	0101000000C942B36C9F5C4040DEFE9E5076784140
186	AUG04T11	851.12599999999998	Waypoint	AUG04T11	AUG04T11	\N	\N	\N	01010000008B7F0FC69E5C404023F536E774784140
187	AUG24T01	464.19900000000001	Waypoint	AUG24T01	AUG24T01	\N	\N	\N	01010000000809145B085A4040D8F51F5A2A774140
188	AUG24T02	632.90899999999999	Waypoint	AUG24T02	AUG24T02	\N	\N	\N	01010000000DAC436B4E5B4040F4599B4FA8784140
189	AUG24T03	642.28200000000004	Waypoint	AUG24T03	AUG24T03	\N	\N	\N	0101000000F8A7BC6CA75B4040FE0CAC39F9784140
190	AUG24T04	618.97000000000003	Waypoint	AUG24T04	AUG24T04	\N	\N	\N	01010000004A1EF4119D5B40409E2453C105794140
191	AUG24T05	653.096	Waypoint	AUG24T05	AUG24T05	\N	\N	\N	01010000000FF27FE9A25B40402BF027C0F6784140
192	AV06 4	405.31799999999998	Waypoint	AV06 4	AV06 4	\N	\N	\N	0101000000B722AB073E94404070052B861F864140
193	AV06 5	409.404	Waypoint	AV06 5	AV06 5	\N	\N	\N	0101000000CE7BE0863F944040289553AF19864140
194	AVA06-1	309.66800000000001	Waypoint	AVA06-1	AVA06-1	\N	\N	\N	010100000025C2CB7B38AB4040FFA407296B7F4140
195	AVA06-1BIS	303.42000000000002	Waypoint	AVA06-1BIS	AVA06-1BIS	\N	\N	\N	010100000093C2F47138AB4040322AC7696A7F4140
196	AVABM1	318.56	Waypoint	AVABM1	AVABM1	\N	\N	\N	0101000000F483C77263AB4040F774700E697F4140
197	AVABM1 E	322.40499999999997	Waypoint	AVABM1 E	AVABM1 E	\N	\N	\N	0101000000A640A88363AB404003ED34F0687F4140
198	AVABM1TRIS	324.08800000000002	Waypoint	AVABM1TRIS	AVABM1TRIS	\N	\N	\N	0101000000B83187EE63AB40401ABA3791697F4140
199	AVABM2	323.36700000000002	Waypoint	AVABM2	AVABM2	\N	\N	\N	01010000006926105D6DAB40401C037373787F4140
200	AVABM2 C	321.92500000000001	Waypoint	AVABM2 C	AVABM2 C	\N	\N	\N	01010000002179FB346DAB4040FE99CF26787F4140
201	AVAGPESTLE	316.15699999999998	Waypoint	AVAGPESTLE	AVAGPESTLE	\N	\N	\N	0101000000074C708260AB40409D02ECE56F7F4140
202	AVAGS 1 06	321.68400000000003	Waypoint	AVAGS 1 06	AVAGS 1 06	\N	\N	\N	01010000001BA6B31C73AB4040751884D3717F4140
203	AVAOATS1	321.20400000000001	Waypoint	AVAOATS1	AVAOATS1	\N	\N	\N	0101000000E013F00D73AB40402FE28C786A7F4140
204	AVAOATS1B	322.64600000000002	Waypoint	AVAOATS1B	AVAOATS1B	\N	\N	\N	01010000009367246371AB404063246C596D7F4140
205	AVAOATS1C	321.44400000000002	Waypoint	AVAOATS1C	AVAOATS1C	\N	\N	\N	010100000016CDC8F576AB4040C18193E46E7F4140
206	AVAOATS1D	321.20400000000001	Waypoint	AVAOATS1D	AVAOATS1D	\N	\N	\N	01010000009C54DB1D77AB40406401D90C6C7F4140
207	AVAOATS2A	321.92500000000001	Waypoint	AVAOATS2A	AVAOATS2A	\N	\N	\N	01010000007533CC9E7EAB4040486FE8C8727F4140
208	AVAOATS2B	320.00200000000001	Waypoint	AVAOATS2B	AVAOATS2B	\N	\N	\N	010100000081AB90807EAB4040E0A410986C7F4140
209	AVAOATS2C	320.72300000000001	Waypoint	AVAOATS2C	AVAOATS2C	\N	\N	\N	0101000000FFDF78417BAB40401FBC4BE9667F4140
210	AVAOATS2D	321.20400000000001	Waypoint	AVAOATS2D	AVAOATS2D	\N	\N	\N	01010000007F48AB9976AB4040ABBC6B70657F4140
211	AVATR11 SE	319.762	Waypoint	AVATR11 SE	AVATR11 SE	\N	\N	\N	01010000002D5A6CD26EAB40402BD79C02677F4140
212	AVATR11NE	317.11799999999999	Waypoint	AVATR11NE	AVATR11NE	\N	\N	\N	01010000007A809B836FAB4040AA659BB6677F4140
213	AVATR11NW	319.041	Waypoint	AVATR11NW	AVATR11NW	\N	\N	\N	01010000005C915B3D6EAB40403F39DF8F687F4140
214	AVATR11SE	318.31999999999999	Waypoint	AVATR11SE	AVATR11SE	\N	\N	\N	0101000000C4D8F3496FAB4040D4E98F1C687F4140
215	AVATR11SW	319.762	Waypoint	AVATR11SW	AVATR11SW	\N	\N	\N	010100000048AF1FC56DAB40405DE2F83C677F4140
216	AVATR1SW	317.35899999999998	Waypoint	AVATR1SW	AVATR1SW	\N	\N	\N	010100000042394B7573AB4040E4ACC7FE6B7F4140
217	AVATR1SW D	317.839	Waypoint	AVATR1SW D	AVATR1SW D	\N	\N	\N	0101000000331643F573AB404040D36BE36A7F4140
218	AVATR1SWE	320.24200000000002	Waypoint	AVATR1SWE	AVATR1SWE	\N	\N	\N	0101000000A70F805F73AB40403A37CB376B7F4140
219	AVATR2NE	318.56	Waypoint	AVATR2NE	AVATR2NE	\N	\N	\N	0101000000F3A1B8A171AB404060F38BC16C7F4140
220	AVATR2NW	320.483	Waypoint	AVATR2NW	AVATR2NW	\N	\N	\N	01010000001A6F307670AB4040C50944366D7F4140
221	AVATR2SW A	319.52100000000002	Waypoint	AVATR2SW A	AVATR2SW A	\N	\N	\N	010100000038B540766FAB4040739E44936A7F4140
222	AVATR2SW2	320.00200000000001	Waypoint	AVATR2SW2	AVATR2SW2	\N	\N	\N	0101000000FA8E93226FAB404022AA4B216B7F4140
223	AVATR3CHAR	322.64600000000002	Waypoint	AVATR3CHAR	AVATR3CHAR	\N	\N	\N	01010000000AACF8936CAB4040530D9DCC6F7F4140
224	AVATR3SW1	322.16500000000002	Waypoint	AVATR3SW1	AVATR3SW1	\N	\N	\N	0101000000812DF3796CAB4040F36FFFE06F7F4140
225	AVATR4SW1	319.28100000000001	Waypoint	AVATR4SW1	AVATR4SW1	\N	\N	\N	0101000000C6A9270A6CAB4040607928BB6D7F4140
226	AVATREEBM	321.20400000000001	Waypoint	AVATREEBM	AVATREEBM	\N	\N	\N	01010000008E798FC6B0AB4040110428435E7F4140
227	AYVAR JH1	319.52100000000002	Waypoint	AYVAR JH1	AYVAR JH1	\N	\N	\N	0101000000828D9F5170AB40402A8B044B5C7F4140
228	AYVAR JH1A	319.041	Waypoint	AYVAR JH1A	AYVAR JH1A	\N	\N	\N	010100000059E6179F6EAB4040FF6C8391597F4140
229	AYVAR JH2	317.59899999999999	Waypoint	AYVAR JH2	AYVAR JH2	\N	\N	\N	01010000005210977770AB4040B7CEE06B5F7F4140
230	AYVAR JH3	316.87799999999999	Waypoint	AYVAR JH3	AYVAR JH3	\N	\N	\N	01010000005AE950AE72AB4040320A91F2647F4140
231	AYVAR JH4	316.39699999999999	Waypoint	AYVAR JH4	AYVAR JH4	\N	\N	\N	010100000091D6732D75AB40402900D35D6A7F4140
232	AYVAR JH5	316.39699999999999	Waypoint	AYVAR JH5	AYVAR JH5	\N	\N	\N	0101000000AAD134F367AB4040B090B4796E7F4140
233	AYVAR PLAT	337.786	Waypoint	AYVAR PLAT	AYVAR PLAT	\N	\N	\N	010100000076FBD7B22FAD40406D7FF726F77D4140
234	AYVAR1	327.93299999999999	Waypoint	AYVAR1	AYVAR1	\N	\N	\N	010100000049C66CF46AAB4040702ECBC5827F4140
235	AYVAR2	316.63799999999998	Waypoint	AYVAR2	AYVAR2	\N	\N	\N	01010000006AA7CFD68DAB404035B01BD77B7F4140
236	AYVAR3	317.11799999999999	Waypoint	AYVAR3	AYVAR3	\N	\N	\N	01010000003759B70289AB4040329EAB275F7F4140
237	AYVAR4	319.28100000000001	Waypoint	AYVAR4	AYVAR4	\N	\N	\N	0101000000F7111BD363AB40406235476A627F4140
238	AYVAR8	319.28100000000001	Waypoint	AYVAR8	AYVAR8	\N	\N	\N	01010000007EFC7053ADAB40406F01A3F65B7F4140
239	BARLEY1	59.486800000000002	Waypoint	BARLEY1	BARLEY1	\N	\N	\N	0101000000E148FB7A59A640404CDDD779C2614140
240	BASEPT2	117.40600000000001	Waypoint	BASEPT2	BASEPT2	\N	\N	\N	01010000002C1DD4C701AC40402B283FBB19614140
241	CKF-10	1223.8699999999999	Waypoint	CKF-10	CKF-10	\N	\N	\N	010100000090C2DE8C66D738402024B0DE8D954140
242	CKF-11	1225.0799999999999	Waypoint	CKF-11	CKF-11	\N	\N	\N	01010000004E7D207967D7384081F8D07090954140
243	CKF-13	1226.28	Waypoint	CKF-13	CKF-13	\N	\N	\N	01010000009F397EB75FD73840722603C68E954140
244	CKF-5	1204.1700000000001	Waypoint	CKF-5	CKF-5	\N	\N	\N	0101000000276F8FC49CD738407AD0148385954140
245	CKF-9	1202.25	Waypoint	CKF-9	CKF-9	\N	\N	\N	0101000000C005FAC065D738409BDB1F507F954140
246	CKF1	1205.1300000000001	Waypoint	CKF1	CKF1	\N	\N	\N	01010000001C3741439BD73840E040E3EA89954140
247	CKF3	1210.9000000000001	Waypoint	CKF3	CKF3	\N	\N	\N	0101000000EA6EA1A29FD738401B33779785954140
248	CKF7	1209.21	Waypoint	CKF7	CKF7	\N	\N	\N	010100000067BA078393D7384049D66F9382954140
249	CLK1	25.120000000000001	Waypoint	CLK1	CLK1	\N	\N	\N	0101000000FD195F2D572940400DA4F33060784140
250	CLK11	22.957000000000001	Waypoint	CLK11	CLK11	\N	\N	\N	0101000000C3204B47422940403D6D94C26A784140
251	CLK2	26.081199999999999	Waypoint	CLK2	CLK2	\N	\N	\N	01010000007F65705759294040A1BA176A5D784140
252	CMH15	510.34199999999998	Waypoint	CMH15	CMH15	\N	\N	\N	0101000000A4746F99E18B4040B163981DA6834140
253	CMH17	498.80599999999998	Waypoint	CMH17	CMH17	\N	\N	\N	0101000000504C741CD38B404058951FE682834140
254	CMH3	501.69	Waypoint	CMH3	CMH3	\N	\N	\N	0101000000D80121A3CD8B40408457F365A7834140
255	CMH7	517.79200000000003	Waypoint	CMH7	CMH7	\N	\N	\N	010100000027CBB8C5D68B4040E7C7FC2F99834140
256	CPL4	517.79200000000003	Waypoint	CPL4	CPL4	\N	\N	\N	0101000000024A0BE3E37640407E3D94A52C794140
257	CPL5	1118.1300000000001	Waypoint	CPL5	CPL5	\N	\N	\N	0101000000912B50E6BA764040C31F181C32794140
258	CPL6	1110.9200000000001	Waypoint	CPL6	CPL6	\N	\N	\N	0101000000FB69E80EBE7640409B47684540794140
259	CPL7	1205.3699999999999	Waypoint	CPL7	CPL7	\N	\N	\N	010100000064DF27A4BB764040BA41BCC73C794140
260	CPL8	1118.3699999999999	Waypoint	CPL8	CPL8	\N	\N	\N	010100000031BA3801B8764040AD9DAC412D794140
261	CPL9	1132.55	Waypoint	CPL9	CPL9	\N	\N	\N	0101000000478A9877B4764040DEFDA01736794140
262	CSF-11	1051.3199999999999	Waypoint	CSF-11	CSF-11	\N	\N	\N	010100000093C1F638788839404F9D909B368C4140
263	CSF19	1189.75	Waypoint	CSF19	CSF19	\N	\N	\N	0101000000734C206913863940CEFC8B1CB98C4140
264	CSF9	1062.6099999999999	Waypoint	CSF9	CSF9	\N	\N	\N	0101000000A496D92C868839406F03B34C438C4140
265	CTC CO1	1726.8800000000001	Waypoint	CTC CO1	CTC CO1	\N	\N	\N	010100000082696C3D3070404046FFFEA60D764140
266	CTC-185	1851.3699999999999	Waypoint	CTC-185	CTC-185	\N	\N	\N	0101000000B4BBD0F4436E4040CCF5629A3C774140
267	CTC142	1828.0599999999999	Waypoint	CTC142	CTC142	\N	\N	\N	0101000000F7076CFCF96E40403DADD0771F784140
268	CTC143	1842.24	Waypoint	CTC143	CTC143	\N	\N	\N	0101000000DE0F080C036F40402604E0DCF5774140
269	CTC147	1841.52	Waypoint	CTC147	CTC147	\N	\N	\N	0101000000ED729316036F40404216B36FF8774140
270	CTC152	1735.77	Waypoint	CTC152	CTC152	\N	\N	\N	0101000000AA411C1422704040F4760FC60B764140
271	CTC155	1724.72	Waypoint	CTC155	CTC155	\N	\N	\N	01010000002EB2F8A81A704040BD92C8640F764140
272	CTC158	1761.97	Waypoint	CTC158	CTC158	\N	\N	\N	0101000000D52F182902704040744328F61D764140
273	CTC161	1748.03	Waypoint	CTC161	CTC161	\N	\N	\N	010100000049F2BA5009704040B2FDEF7E18764140
274	CTC167	1748.03	Waypoint	CTC167	CTC167	\N	\N	\N	010100000035E8EBFCD26F4040C0EE167608764140
275	CTC170	1775.4300000000001	Waypoint	CTC170	CTC170	\N	\N	\N	0101000000416F0B45E36F4040ED03984B0D764140
276	CTC173	1775.4300000000001	Waypoint	CTC173	CTC173	\N	\N	\N	01010000007106D006D86D4040A7B19C084B774140
277	CTC176	1775.4300000000001	Waypoint	CTC176	CTC176	\N	\N	\N	01010000007106D006D86D4040A7B19C084B774140
278	CTC179	1859.3	Waypoint	CTC179	CTC179	\N	\N	\N	0101000000E265A4E3CC6D4040AAEDB4A23E774140
279	CTC55	1545.9100000000001	Waypoint	CTC55	CTC55	\N	\N	\N	010100000021CA84503F7240403884046ED8764140
280	CTC56	1574.03	Waypoint	CTC56	CTC56	\N	\N	\N	0101000000EFC584896072404063228C3CDC764140
281	CTC61	1563.9400000000001	Waypoint	CTC61	CTC61	\N	\N	\N	0101000000DCF3223146724040A7FDB33FC0764140
282	CTC62	1564.1800000000001	Waypoint	CTC62	CTC62	\N	\N	\N	01010000000DDAB30050724040CCCA4FDAB6764140
283	CTC63	1586.77	Waypoint	CTC63	CTC63	\N	\N	\N	0101000000C12A8B8052724040D7CE53C4B9764140
284	CTC64	1589.8900000000001	Waypoint	CTC64	CTC64	\N	\N	\N	0101000000CE8E177C6172404004F252D5B7764140
285	CTC65	1617.53	Waypoint	CTC65	CTC65	\N	\N	\N	01010000001FEFDE1C71724040E907A882A7764140
286	CTC68	1620.1800000000001	Waypoint	CTC68	CTC68	\N	\N	\N	01010000009974BC4B677140407A29A703FA754140
287	CTC80	1842.24	Waypoint	CTC80	CTC80	\N	\N	\N	0101000000AF2B54A17B6F404024F73CCBE8764140
288	CTC81	1850.4100000000001	Waypoint	CTC81	CTC81	\N	\N	\N	01010000000FA0C85B7A6F40402C58D843E3764140
289	CTC85	1839.5899999999999	Waypoint	CTC85	CTC85	\N	\N	\N	0101000000563F48AC8B6F404075E7FB3CD5764140
290	CTC88	1867.23	Waypoint	CTC88	CTC88	\N	\N	\N	01010000007FA060EF136E4040CCAAA70D49774140
291	CTC90	1868.6700000000001	Waypoint	CTC90	CTC90	\N	\N	\N	01010000008C15C8FB176E4040A463CC324B774140
292	CTC92	1862.4300000000001	Waypoint	CTC92	CTC92	\N	\N	\N	010100000026E21F49186E4040BD640C4148774140
293	CVP PARK	1597.3399999999999	Waypoint	CVP PARK	CVP PARK	\N	\N	\N	01010000004474688E3516384060D5E41F39A24140
294	D1	116.925	Waypoint	D1	D1	\N	\N	\N	0101000000BF9241745C5BEFBFF84A23DD44544A40
295	D2	139.756	Waypoint	D2	D2	\N	\N	\N	0101000000AE9B4333FE36EFBF98C38B118B534A40
296	D3	162.34700000000001	Waypoint	D3	D3	\N	\N	\N	0101000000A25932F72B55EEBF8250588E42534A40
297	D4	159.22300000000001	Waypoint	D4	D4	\N	\N	\N	0101000000E4F41B44701EEEBF5C3B00CB12534A40
298	D5	133.02699999999999	Waypoint	D5	D5	\N	\N	\N	0101000000807AD3F791D0EDBF21A64C8385544A40
299	D69	152.25299999999999	Waypoint	D69	D69	\N	\N	\N	01010000005C564025D712EEBFA7EEFF20E8544A40
300	D7	111.157	Waypoint	D7	D7	\N	\N	\N	0101000000084032D664F1EEBF1A9AFC60AA564A40
301	DELETED	42.4236	Waypoint	DELETED	DELETED	\N	\N	\N	010100000059CAB8FD402640409E0D0D4C6D834140
302	F1	117.886	Waypoint	F1	F1	\N	\N	\N	0101000000F9D9F3F5EFAB4040B67B5424E8604140
303	F2	113.08	Waypoint	F2	F2	\N	\N	\N	0101000000A02FEB35E5AB404092FE080AD6604140
304	F29	62.130499999999998	Waypoint	F29	F29	\N	\N	\N	0101000000A1DD4C455DA64040B159DFD4C8614140
305	F3	115.24299999999999	Waypoint	F3	F3	\N	\N	\N	010100000085C95C32F1AB404060ED16EBD5604140
306	F4	115.964	Waypoint	F4	F4	\N	\N	\N	01010000001EF4283900AC4040F580D739D6604140
307	F5	119.569	Waypoint	F5	F5	\N	\N	\N	01010000007E6D3F11F2AB40404B063955E2604140
308	F6	119.08799999999999	Waypoint	F6	F6	\N	\N	\N	0101000000B506CBE5FAAB40402B269C01E1604140
309	GARMIN	325.04899999999998	Waypoint	GARMIN	GARMIN	\N	\N	\N	01010000003581CE1623B357C013B87FA9826D4340
310	GEORGOSTMB	99.381200000000007	Waypoint	GEORGOSTMB	GEORGOSTMB	\N	\N	\N	0101000000D8C3F7FB4EAC404081DF47B4EB604140
311	GMARAES	81.356700000000004	Waypoint	GMARAES	GMARAES	\N	\N	\N	01010000007288972BACAC4040C3F82C7943604140
312	GRMEUR	35.934699999999999	Waypoint	GRMEUR	GRMEUR	\N	\N	\N	01010000001E909861226CF7BF229CA71ECF7D4940
313	GRMPHX	361.09800000000001	Waypoint	GRMPHX	GRMPHX	\N	\N	\N	0101000000D0B1FD108DFC5BC022360CAA43AA4040
314	GRMTWN	38.097700000000003	Waypoint	GRMTWN	GRMTWN	\N	\N	\N	01010000001E631221FA685E40183ACF08D10F3940
315	GTOMB2	94.334500000000006	Waypoint	GTOMB2	GTOMB2	\N	\N	\N	01010000001781F8934DAC404023BAA4C0FB604140
316	J1	126.538	Waypoint	J1	J1	\N	\N	\N	01010000007D513EE1455FEFBFC8C51B1056544A40
317	J10	13.1036	Waypoint	J10	J10	\N	\N	\N	0101000000C4F3036B5D0B2D407CDAA44CB1F14140
318	J11	31.608899999999998	Waypoint	J11	J11	\N	\N	\N	01010000005D230D64F90B2D40ACBE34F140F14140
319	J12	31.608899999999998	Waypoint	J12	J12	\N	\N	\N	01010000002A7C8D8FDC042D40A06FEF40BBF24140
320	J13	55.401200000000003	Waypoint	J13	J13	\N	\N	\N	01010000009BBC5E205C062D404273170D8BF24140
321	J14	50.835099999999997	Waypoint	J14	J14	\N	\N	\N	0101000000BDAF8D22B2052D4037B94FF6A5F24140
322	J15	21.034400000000002	Waypoint	J15	J15	\N	\N	\N	0101000000A66F6C52DE052D4074A2E3BEA3F24140
323	J16	36.174999999999997	Waypoint	J16	J16	\N	\N	\N	01010000009766C248920B2D405560E7F794F14140
324	J2	54.920499999999997	Waypoint	J2	J2	\N	\N	\N	01010000001CA7029D32042D40FC553461B1F24140
325	J3	60.4482	Waypoint	J3	J3	\N	\N	\N	01010000001C8990ED8E032D40513E44704AEF4140
326	J4	25.840900000000001	Waypoint	J4	J4	\N	\N	\N	010100000023D36CE123042D404E70B45A95EF4140
327	J5	24.1586	Waypoint	J5	J5	\N	\N	\N	0101000000593673486A0B2D4053D4EF7B81F14140
328	J6	24.1586	Waypoint	J6	J6	\N	\N	\N	01010000004E4504C58A0B2D40F6C15FB77BF14140
329	J7	16.948899999999998	Waypoint	J7	J7	\N	\N	\N	0101000000BD398CE4530B2D40C272C829A1F14140
330	J8	15.747199999999999	Waypoint	J8	J8	\N	\N	\N	0101000000B1EE3E188F0A2D40C904F761CFF14140
331	J9	14.545500000000001	Waypoint	J9	J9	\N	\N	\N	01010000002839E3D8D30A2D40680ED9D896F14140
332	KATALION	417.09500000000003	Waypoint	KATALION	KATALION	\N	\N	\N	0101000000078E1FB8F7A640405B3F232FF07D4140
333	LINA MINE	462.75700000000001	Waypoint	LINA MINE	LINA MINE	\N	\N	\N	010100000054D25835A78D40409F4FE70161854140
334	LNIN2	431.03399999999999	Waypoint	LNIN2	LNIN2	\N	\N	\N	01010000008E058088DF8E4040F935ABA1C1844140
335	MF1	133.02699999999999	Waypoint	MF1	MF1	\N	\N	\N	0101000000E7106191D9AB404074BDF8980B614140
336	MTKEV	420.21899999999999	Waypoint	MTKEV	MTKEV	\N	\N	\N	010100000004DAFFFBF1A640404C5F7C8F827E4140
337	OAKS	871.31399999999996	Waypoint	OAKS	OAKS	\N	\N	\N	0101000000CB7B7E13E8E338405063D32E0CA34140
338	PFR	3.7308300000000001	Waypoint	PFR	PFR	\N	\N	\N	0101000000B686316D089251C0D7122FA5E7D24540
339	PINETREE1	264.48599999999999	Waypoint	PINETREE1	PINETREE1	\N	\N	\N	01010000008C5B1B16E2AD4040F0ECA41E5F634140
340	PL1	283.95299999999997	Waypoint	PL1	PL1	\N	\N	\N	01010000003D32BBC6E5B340408CB940895C7D4140
341	PL2	282.99200000000002	Waypoint	PL2	PL2	\N	\N	\N	0101000000FB6C03C8E7B3404028F4C2E9597D4140
342	PL3	281.55000000000001	Waypoint	PL3	PL3	\N	\N	\N	01010000002E357FA2EBB3404091EF9E78587D4140
343	PL4	283.95299999999997	Waypoint	PL4	PL4	\N	\N	\N	010100000064C88B2EEAB3404096F748B5557D4140
344	PL5	281.06900000000002	Waypoint	PL5	PL5	\N	\N	\N	01010000005231E77CE5B3404013617CFE5F7D4140
345	PM1	295.72899999999998	Waypoint	PM1	PM1	\N	\N	\N	010100000053BFAFEA1CB040402D7DC77411824140
346	PM2	296.93099999999998	Waypoint	PM2	PM2	\N	\N	\N	0101000000AEE8D46A0FB0404014DC333E18824140
347	PM3	289.96100000000001	Waypoint	PM3	PM3	\N	\N	\N	0101000000B233486B13B040404813DB1C28824140
348	PM4	290.202	Waypoint	PM4	PM4	\N	\N	\N	01010000008559843C24B0404006A5F3D727824140
349	PM5	292.60500000000002	Waypoint	PM5	PM5	\N	\N	\N	01010000008A7888D220B04040EA6378CB19824140
350	POL KTREE1	395.46499999999997	Waypoint	POL KTREE1	POL KTREE1	\N	\N	\N	010100000070808CC51E9D4040612E3FF68E834140
351	POL KTREE2	395.46499999999997	Waypoint	POL KTREE2	POL KTREE2	\N	\N	\N	010100000010A46C402D9D40408A3E4F9E9A834140
352	POL KTREE3	396.66699999999997	Waypoint	POL KTREE3	POL KTREE3	\N	\N	\N	0101000000D9EA9358299D404003B4036796834140
353	POLSA1	447.37599999999998	Waypoint	POLSA1	POLSA1	\N	\N	\N	0101000000174320340E9C40403CB0BBF7F2834140
354	POLSA1A	423.82400000000001	Waypoint	POLSA1A	POLSA1A	\N	\N	\N	01010000009B701C690A9C40401A8664080D844140
355	POLSA1B	420.459	Waypoint	POLSA1B	POLSA1B	\N	\N	\N	010100000001ADA039109C4040584D2B132B844140
356	POLSA1C	411.08600000000001	Waypoint	POLSA1C	POLSA1C	\N	\N	\N	0101000000435568FA0E9C40406AEB4FFE48844140
357	POLSA2	443.05000000000001	Waypoint	POLSA2	POLSA2	\N	\N	\N	0101000000E98493DEFE9B404032F2D07CF0834140
358	POLSA2A	419.25799999999998	Waypoint	POLSA2A	POLSA2A	\N	\N	\N	0101000000FEFD229BFD9B40406253AFA712844140
359	POLSA2B	411.327	Waypoint	POLSA2B	POLSA2B	\N	\N	\N	01010000005EC98BD5F19B4040DF31A4D928844140
360	POLSA2C	407.00099999999998	Waypoint	POLSA2C	POLSA2C	\N	\N	\N	010100000034CD8F4DDF9B4040338CA4A53E844140
361	POLSALITH	404.59800000000001	Waypoint	POLSALITH	POLSALITH	\N	\N	\N	010100000017734767EF9B4040528C9B363D844140
362	POLSAWS	405.07799999999997	Waypoint	POLSAWS	POLSAWS	\N	\N	\N	01010000006A4150B7F19B40403087337845844140
363	POLTREE1	500.00799999999998	Waypoint	POLTREE1	POLTREE1	\N	\N	\N	01010000008B19230A549A40401B7FC3C0CA804140
364	POLTREE2	500.72899999999998	Waypoint	POLTREE2	POLTREE2	\N	\N	\N	0101000000EA1334BE539A4040C1A6EFB1CB804140
365	POLTREE3	500.72899999999998	Waypoint	POLTREE3	POLTREE3	\N	\N	\N	0101000000C5A567D0499A4040F1398C56A9804140
366	POLTREE4	507.69799999999998	Waypoint	POLTREE4	POLTREE4	\N	\N	\N	010100000064A5FB99379A4040ECFD9782AD804140
367	PT15MSWAR2	122.93300000000001	Waypoint	PT15MSWAR2	PT15MSWAR2	\N	\N	\N	010100000051A39064D6AB40406B856FB51C614140
368	PTAREA2SW	122.93300000000001	Waypoint	PTAREA2SW	PTAREA2SW	\N	\N	\N	01010000005D0807B5FEAB40407525A463FE604140
369	PTAREA3SW	122.212	Waypoint	PTAREA3SW	PTAREA3SW	\N	\N	\N	01010000007776AB47D7AB4040D700216922614140
370	PTBASE1	116.925	Waypoint	PTBASE1	PTBASE1	\N	\N	\N	0101000000945833E100AC404075E9FC67CF604140
371	PTM	24.8796	Waypoint	PTM	PTM	\N	\N	\N	0101000000FA895177289351C0B20BED39BCE94540
372	PTPIVOT+WL	118.367	Waypoint	PTPIVOT+WL	PTPIVOT+WL	\N	\N	\N	010100000061F2E388E5AB4040E20F5D5518614140
373	RCHRCH	248.625	Waypoint	RCHRCH	RCHRCH	\N	\N	\N	0101000000E9E750FD38B4404067CB7F694D7B4140
374	RCHRCH2	252.47	Waypoint	RCHRCH2	RCHRCH2	\N	\N	\N	0101000000CE8408334BB440402820370D5B7B4140
375	SBD1	93.613399999999999	Waypoint	SBD1	SBD1	\N	\N	\N	01010000005867A02514AC404041D0E8468C604140
376	SBD2	91.210099999999997	Waypoint	SBD2	SBD2	\N	\N	\N	01010000002E97130809AC4040C80B3FF189604140
377	SBD3	75.108199999999997	Waypoint	SBD3	SBD3	\N	\N	\N	0101000000C281C8D6FEAB4040FDA328FD68604140
378	SBD4	79.914699999999996	Waypoint	SBD4	SBD4	\N	\N	\N	0101000000C777A33BFAAB404093DD97F677604140
379	SBD5	80.6357	Waypoint	SBD5	SBD5	\N	\N	\N	0101000000DFFC235F0BAC404033EFBF357A604140
380	SBD6	89.287499999999994	Waypoint	SBD6	SBD6	\N	\N	\N	01010000004022A38C03AC404076E658BD87604140
381	SCV-6	1123.1800000000001	Waypoint	SCV-6	SCV-6	\N	\N	\N	0101000000BF22801A855740407F5F307352804140
382	SCV-73	1231.0799999999999	Waypoint	SCV-73	SCV-73	\N	\N	\N	01010000002DBDA0FFCA574040578194B7E77E4140
383	SCV-74	1234.21	Waypoint	SCV-74	SCV-74	\N	\N	\N	01010000003CC3B841CB574040B05F0BD5E87E4140
384	SCV10	1174.8499999999999	Waypoint	SCV10	SCV10	\N	\N	\N	010100000069EB700CB257404046DA004B32804140
385	SCV110	1137.8399999999999	Waypoint	SCV110	SCV110	\N	\N	\N	01010000006600F1B8FD574040BCEE9B65507E4140
386	SCV131	1264.73	Waypoint	SCV131	SCV131	\N	\N	\N	0101000000DEB6CB8BC7574040E3E70325EC7E4140
387	SCV134	1251.03	Waypoint	SCV134	SCV134	\N	\N	\N	01010000006ADDDB34C3574040E28AB422E47E4140
388	SCV135	1242.1400000000001	Waypoint	SCV135	SCV135	\N	\N	\N	0101000000BC82BB53C45740408593B8B7F07E4140
389	SCV137	1216.6700000000001	Waypoint	SCV137	SCV137	\N	\N	\N	010100000012E173E8B35740402D75A773F97E4140
390	SCV16	1159.47	Waypoint	SCV16	SCV16	\N	\N	\N	0101000000529B17ABB6574040D0EB1FA91D804140
391	SCV17	1157.3	Waypoint	SCV17	SCV17	\N	\N	\N	0101000000333B7442B4574040E203A89A3A804140
392	SCV18	1157.3	Waypoint	SCV18	SCV18	\N	\N	\N	010100000030EDA36CB4574040986BCC493A804140
393	SCV26	1392.3399999999999	Waypoint	SCV26	SCV26	\N	\N	\N	0101000000DFEF40321757404012C4A4B85D7F4140
394	SCV46	1394.03	Waypoint	SCV46	SCV46	\N	\N	\N	0101000000662B97DC145740405E7070635F7F4140
395	SCV47	1385.8599999999999	Waypoint	SCV47	SCV47	\N	\N	\N	0101000000E550243701574040326704BB647F4140
396	SCV48	1378.4100000000001	Waypoint	SCV48	SCV48	\N	\N	\N	01010000004C6914CAF65640405CDFBF2D637F4140
397	SCV49	1380.3299999999999	Waypoint	SCV49	SCV49	\N	\N	\N	01010000008E3FA7D9E9564040BEFF7877707F4140
398	SCV50	1369.03	Waypoint	SCV50	SCV50	\N	\N	\N	010100000064B234C7245740405F3F483F6F7F4140
399	SCV51	1390.6600000000001	Waypoint	SCV51	SCV51	\N	\N	\N	0101000000408224E4EA564040FEB0401C6D7F4140
400	SCV52	1401.48	Waypoint	SCV52	SCV52	\N	\N	\N	010100000076BB03E1EF564040DFE7F0F7687F4140
401	SCV53	1401.48	Waypoint	SCV53	SCV53	\N	\N	\N	010100000039FABB1EDC5640407055744C9F7F4140
402	SCV54	1387.0599999999999	Waypoint	SCV54	SCV54	\N	\N	\N	0101000000AE274320DA56404085F72C3A9F7F4140
403	SCV55	1378.1700000000001	Waypoint	SCV55	SCV55	\N	\N	\N	0101000000C51D6FF2DB564040DAB0FCB2A17F4140
404	SCV56	1399.55	Waypoint	SCV56	SCV56	\N	\N	\N	0101000000C22ECC3A0A574040817B576A617F4140
405	SCV57	1382.97	Waypoint	SCV57	SCV57	\N	\N	\N	01010000000594B39B0C574040BB0D3F3F597F4140
406	SCV58	1382.97	Waypoint	SCV58	SCV58	\N	\N	\N	0101000000289C546908574040D6023930527F4140
407	SCV59	1382.97	Waypoint	SCV59	SCV59	\N	\N	\N	0101000000280B806D14574040C6DDD4CB657F4140
408	SCV60	1376	Waypoint	SCV60	SCV60	\N	\N	\N	01010000001EF6A0721C574040BA0BE35A6C7F4140
409	SCV61	1376	Waypoint	SCV61	SCV61	\N	\N	\N	010100000078C59863155740401758539B707F4140
410	SCV62	1376	Waypoint	SCV62	SCV62	\N	\N	\N	010100000093C6B4AB1A574040C20A69ED737F4140
411	SCV63	1361.8199999999999	Waypoint	SCV63	SCV63	\N	\N	\N	010100000075D518812C5740407EEEE034787F4140
412	SCV65	1379.1300000000001	Waypoint	SCV65	SCV65	\N	\N	\N	0101000000470F538DE45640403B7F8760A47F4140
413	SCV69	1237.3299999999999	Waypoint	SCV69	SCV69	\N	\N	\N	01010000000AA6A4CBBE574040279A6068107F4140
414	SCV69 A	1218.3499999999999	Waypoint	SCV69 A	SCV69 A	\N	\N	\N	01010000007221876FBE5740402BA217CF0F7F4140
415	SCV7	1131.5899999999999	Waypoint	SCV7	SCV7	\N	\N	\N	0101000000852D8F988D574040AE85689354804140
416	SCV70	1211.3800000000001	Waypoint	SCV70	SCV70	\N	\N	\N	01010000005C6E1737C1574040E1D4F0F5017F4140
417	SCV71	1182.78	Waypoint	SCV71	SCV71	\N	\N	\N	0101000000890FB44ED157404049D66F93027F4140
418	SCV72	1182.78	Waypoint	SCV72	SCV72	\N	\N	\N	0101000000A81BC00CDA57404075EB142FFF7E4140
419	SCV8	1208.97	Waypoint	SCV8	SCV8	\N	\N	\N	01010000009E0427BCB3574040D3A81B8329804140
420	SCV9	1191.6700000000001	Waypoint	SCV9	SCV9	\N	\N	\N	0101000000E2CC17CAB557404024FA87BD28804140
421	SCV96	1127.98	Waypoint	SCV96	SCV96	\N	\N	\N	0101000000F75EBB35E25740402138D8E22D7E4140
422	SCV97	1115.49	Waypoint	SCV97	SCV97	\N	\N	\N	0101000000C18B2B1FE3574040338207B2107E4140
423	SCV98	1117.6500000000001	Waypoint	SCV98	SCV98	\N	\N	\N	0101000000028A68A6015840401841AFB4567E4140
424	SEDGEPT	83.759900000000002	Waypoint	SEDGEPT	SEDGEPT	\N	\N	\N	0101000000B555C07201AC4040ABCD20BA77604140
425	SIASPILIOS	240.45400000000001	Waypoint	SIASPILIOS	SIASPILIOS	\N	\N	\N	0101000000AD3FBF7811B4404061EEDC79777B4140
426	SKOURIOTIS	337.30599999999998	Waypoint	SKOURIOTIS	SKOURIOTIS	\N	\N	\N	0101000000B6588CE556724040C6AA0435838C4140
427	STV13	1072.23	Waypoint	STV13	STV13	\N	\N	\N	010100000056D383EFEE50404008B85FBF59844140
428	STV18	1082.5599999999999	Waypoint	STV18	STV18	\N	\N	\N	0101000000413AA71FF5504040216508235D844140
429	STV27	1088.5699999999999	Waypoint	STV27	STV27	\N	\N	\N	01010000002F468FA5F0504040123FA3CD61844140
430	STV28	1100.5899999999999	Waypoint	STV28	STV28	\N	\N	\N	010100000047D0C882EA504040FD05979B63844140
431	STV37	1125.8199999999999	Waypoint	STV37	STV37	\N	\N	\N	010100000099067D9DDF50404011F1AE316A844140
432	STV38	1115.49	Waypoint	STV38	STV38	\N	\N	\N	01010000004DD337CEC55040403C95CCE46D844140
433	STV43	1132.3099999999999	Waypoint	STV43	STV43	\N	\N	\N	010100000033AF8839BF504040A37CB8196B844140
434	STV47	977.05799999999999	Waypoint	STV47	STV47	\N	\N	\N	0101000000265120CD475140401EAD9F3B17834140
435	STV53	1010.7	Waypoint	STV53	STV53	\N	\N	\N	0101000000348484A4665140406C19C49520834140
436	STV54	982.10500000000002	Waypoint	STV54	STV54	\N	\N	\N	0101000000EFBEF06B60514040D9306BAB17834140
437	STV57	1003.73	Waypoint	STV57	STV57	\N	\N	\N	01010000003E426F1F69514040508154091D834140
438	STV59	1027.29	Waypoint	STV59	STV59	\N	\N	\N	01010000000BD843387C5140405B5F8C971A834140
439	STV6	1069.8199999999999	Waypoint	STV6	STV6	\N	\N	\N	0101000000D82C1355EA5040402C54F7FB57844140
440	STV60	1035.9400000000001	Waypoint	STV60	STV60	\N	\N	\N	0101000000BE88EB557A514040D66B70DE1C834140
441	SWALL	91.930999999999997	Waypoint	SWALL	SWALL	\N	\N	\N	01010000009DE3D4B513AC4040BBA88F2092604140
442	TEST1	251.50899999999999	Waypoint	TEST1	TEST1	\N	\N	\N	0101000000B704F73B08B3404050041D1F807E4140
443	TEST2	250.06700000000001	Waypoint	TEST2	TEST2	\N	\N	\N	01010000007C15C06408B34040A9FF837A807E4140
444	THB10	-15.7357	Waypoint	THB10	THB10	\N	\N	\N	010100000064397188248F37409639900778A24140
445	THB12	-7.5645699999999998	Waypoint	THB12	THB12	\N	\N	\N	0101000000959D41EDFE8E37400F84BFBA85A24140
446	THB4	-15.014799999999999	Waypoint	THB4	THB4	\N	\N	\N	0101000000C35F387C778F3740490DCB7177A24140
447	THB8	-8.7662300000000002	Waypoint	THB8	THB8	\N	\N	\N	01010000002307313F538F374063DA9C227EA24140
448	TODD2	96.737700000000004	Waypoint	TODD2	TODD2	\N	\N	\N	0101000000B4DBA3414EAC40405FAC14A7FF604140
449	TODDTOMB	98.900599999999997	Waypoint	TODDTOMB	TODDTOMB	\N	\N	\N	01010000009079EC804EAC4040091C7B8900614140
450	TREE A KD	177.488	Waypoint	TREE A KD	TREE A KD	\N	\N	\N	01010000007C45BC174DA14040840F677604664140
451	TREE B KD	174.364	Waypoint	TREE B KD	TREE B KD	\N	\N	\N	0101000000EADADC6D5EA14040814E4838DB654140
452	TREE C KD	155.137	Waypoint	TREE C KD	TREE C KD	\N	\N	\N	0101000000BB0DE1CD17A240401A588F4765664140
453	TREE D KD	147.68700000000001	Waypoint	TREE D KD	TREE D KD	\N	\N	\N	0101000000F13AAB9D12A24040E92BFCA450664140
454	TREE E KD	147.68700000000001	Waypoint	TREE E KD	TREE E KD	\N	\N	\N	01010000003EFB8A680DA24040C007B41654664140
455	TREE F MV	272.65800000000002	Waypoint	TREE F MV	TREE F MV	\N	\N	\N	010100000019BF444AE0AD4040FED5CC2260634140
456	TREE G MV	267.13	Waypoint	TREE G MV	TREE G MV	\N	\N	\N	0101000000DC6C288CD8AD40406C0707A15A634140
457	TREE H MV	267.851	Waypoint	TREE H MV	TREE H MV	\N	\N	\N	010100000094114F2AE5AD40402110A378B9634140
458	TREE12	788.64099999999996	Waypoint	TREE12	TREE12	\N	\N	\N	010100000093856B92F85240400635538C397A4140
459	TREE13	812.43399999999997	Waypoint	TREE13	TREE13	\N	\N	\N	01010000005A24881BF95240402BF470EB367A4140
460	TREE14	807.86699999999996	Waypoint	TREE14	TREE14	\N	\N	\N	0101000000D02B1FFBF952404095AC6C1A397A4140
461	TREE15	1756.9200000000001	Waypoint	TREE15	TREE15	\N	\N	\N	01010000007A647B46456F40404ADC7C8664784140
462	TREE16	1755.24	Waypoint	TREE16	TREE16	\N	\N	\N	010100000097515FA8596F40401389F38466784140
463	TREE17	1749.71	Waypoint	TREE17	TREE17	\N	\N	\N	010100000060DE9F2F566F404064B4936360784140
464	TREE18	1754.04	Waypoint	TREE18	TREE18	\N	\N	\N	01010000002DFFB25F5D6F4040E5FFA48D62784140
465	TREE19	1754.28	Waypoint	TREE19	TREE19	\N	\N	\N	01010000004B0540D55B6F40405F9E6BEB69784140
466	TREE20	1761.73	Waypoint	TREE20	TREE20	\N	\N	\N	01010000000F891091396F40404885ACCC66784140
467	TREE21	1756.9200000000001	Waypoint	TREE21	TREE21	\N	\N	\N	010100000036703F94336F4040E428DBE865784140
468	TREE22	1772.54	Waypoint	TREE22	TREE22	\N	\N	\N	010100000095732C66396F4040E68BC0CF6D784140
469	TREE23	1799.22	Waypoint	TREE23	TREE23	\N	\N	\N	0101000000E76DA420436F40407101AF697A784140
470	TREE24	1789.8499999999999	Waypoint	TREE24	TREE24	\N	\N	\N	01010000008E03FB24416F4040C1A1103180784140
471	TREE25	1787.2	Waypoint	TREE25	TREE25	\N	\N	\N	0101000000775CC4DF456F4040B974BF4F84784140
472	TREE26	1789.1300000000001	Waypoint	TREE26	TREE26	\N	\N	\N	0101000000E30AE3FF326F4040CD6234BB86784140
473	TREE27	1782.6400000000001	Waypoint	TREE27	TREE27	\N	\N	\N	0101000000E695F0262D6F404012AD63FC7E784140
474	TREE28	1785.04	Waypoint	TREE28	TREE28	\N	\N	\N	010100000039699B94226F4040BEB98F4186784140
475	TREE29	1784.0799999999999	Waypoint	TREE29	TREE29	\N	\N	\N	010100000068146FBD166F40405E8B34F687784140
476	TREE30	1798.74	Waypoint	TREE30	TREE30	\N	\N	\N	010100000090892C83FE6E40401E803FC291784140
477	TREE31	1804.27	Waypoint	TREE31	TREE31	\N	\N	\N	01010000008D4F70CDF76E4040ECD48F5F95784140
478	TREESMPL1	258.71899999999999	Waypoint	TREESMPL1	TREESMPL1	\N	\N	\N	0101000000BF2FD0E3E7AD4040766CB85370634140
479	V10	18.871500000000001	Waypoint	V10	V10	\N	\N	\N	01010000002C4DD07AC6B04040D64413E6855F4140
480	V11	16.948899999999998	Waypoint	V11	V11	\N	\N	\N	0101000000B049C851CAB04040AECE8F917C5F4140
481	V12	16.227900000000002	Waypoint	V12	V12	\N	\N	\N	01010000004CBF2B1FD5B0404094C7D03A755F4140
482	V13	14.7859	Waypoint	V13	V13	\N	\N	\N	0101000000936C4047D5B0404046C1FBC6715F4140
483	V2	23.197399999999998	Waypoint	V2	V2	\N	\N	\N	010100000059B48C16D8B040403D7BF4A7895F4140
484	V3	24.8796	Waypoint	V3	V3	\N	\N	\N	0101000000C902930CDAB04040323F6C26755F4140
485	V4	20.313400000000001	Waypoint	V4	V4	\N	\N	\N	0101000000DAB64BC2D5B040400145C736725F4140
486	V5	23.197399999999998	Waypoint	V5	V5	\N	\N	\N	0101000000C29DB069CAB040405E3DA0CC7C5F4140
487	V6	23.918299999999999	Waypoint	V6	V6	\N	\N	\N	0101000000B2776FDAC6B040400608352F865F4140
488	V7	21.755400000000002	Waypoint	V7	V7	\N	\N	\N	0101000000BB87ACB7CBB04040DC4FDF95915F4140
489	V8	16.4681	Waypoint	V8	V8	\N	\N	\N	01010000000244400CD4B04040546EC3A48F5F4140
490	V9	13.8246	Waypoint	V9	V9	\N	\N	\N	0101000000F25A3CCECAB040408FAF4CDE915F4140
491	VAVLA	492.077	Waypoint	VAVLA	VAVLA	\N	\N	\N	01010000005014F08441A2404071DDE8F1A86B4140
492	VGATE V1	19.592400000000001	Waypoint	VGATE V1	VGATE V1	\N	\N	\N	01010000002EB3FB9AD4B04040685C3810925F4140
493	WHEAT1	250.30699999999999	Waypoint	WHEAT1	WHEAT1	\N	\N	\N	01010000007229D7A9ECAD4040978558F858634140
494	WHEAT2	27.0426	Waypoint	WHEAT2	WHEAT2	\N	\N	\N	010100000049B417E3D9B04040EB264F5E865F4140
495	WHEAT3	58.765700000000002	Waypoint	WHEAT3	WHEAT3	\N	\N	\N	0101000000CE7D0C3256A64040356D48A1C1614140
496	ZP1	6.37439	Waypoint	ZP1	ZP1	\N	\N	\N	0101000000EE4BE7D757AA4040B478EBDB8D5C4140
\.


--
-- Data for Name: download4; Type: TABLE DATA; Schema: public; Owner: webuser
--

COPY download4 (gid, name, elevation, symbol, "comment", descriptio, source, url, "url name", the_geom) FROM stdin;
0	011	313.51299999999998	Flag	011	011	\N	\N	\N	0101000000381B905C75AB404040325F564F7F4140
1	012	251.749	Flag	012	012	\N	\N	\N	010100000082C3184C14B34040C47D609E807E4140
2	013	251.749	Flag	013	013	\N	\N	\N	0101000000FBD8FC7614B340401DDCD0A6807E4140
3	014	251.749	Flag	014	014	\N	\N	\N	0101000000F599CF9314B34040030181BA807E4140
4	015	247.18299999999999	Flag	015	015	\N	\N	\N	0101000000ED00B08308B34040CD444BFD807E4140
5	016	247.18299999999999	Flag	016	016	\N	\N	\N	0101000000CAEF329806B34040A65440DC807E4140
6	017	248.14400000000001	Flag	017	017	\N	\N	\N	0101000000B37FA3BF05B3404029F18BDB807E4140
7	018	288.51900000000001	Flag	018	018	\N	\N	\N	010100000020518C1F2FAB40406F71AB257F7F4140
8	019	407.48099999999999	Flag	019	019	\N	\N	\N	0101000000BF1B9BB5459440402370D0D012864140
9	020	469.48599999999999	Flag	020	020	\N	\N	\N	0101000000B0FDA6A8A1934040C791E8144D864140
10	021	468.52499999999998	Flag	021	021	\N	\N	\N	0101000000032B7FA590934040AF0469FE4C864140
11	022	464.43900000000002	Flag	022	022	\N	\N	\N	0101000000AE8C4386A0934040559107AB3C864140
12	023	474.29199999999997	Flag	023	023	\N	\N	\N	01010000003B44046598934040DEF89BCF47864140
13	024	453.86500000000001	Flag	024	024	\N	\N	\N	01010000007208154F8F93404022078FB054864140
14	025	475.49400000000003	Flag	025	025	\N	\N	\N	0101000000BE061CC09D9340405A0E5C3450864140
15	026	477.65699999999998	Flag	026	026	\N	\N	\N	0101000000DD0613519C9340404E10FB584F864140
16	027	477.41699999999997	Flag	027	027	\N	\N	\N	0101000000AA014DFB9B9340402B51D0CF4F864140
17	029	466.60199999999998	Flag	029	029	\N	\N	\N	0101000000F6DAB0C9419D40401A6F2BBD36834140
18	030	394.26299999999998	Flag	030	030	\N	\N	\N	010100000009B81079059D4040A55FB8F99A834140
19	031	396.18599999999998	Flag	031	031	\N	\N	\N	010100000031822B78089D40408B24986F9F834140
20	032	389.21699999999998	Flag	032	032	\N	\N	\N	0101000000F7FDD87AFF9C40403DA2DF4AB8834140
21	033	389.21699999999998	Flag	033	033	\N	\N	\N	01010000006F482CF4049D4040F91F08ACB6834140
22	034	381.52600000000001	Flag	034	034	\N	\N	\N	01010000009910A3822B9D4040358E5B439E834140
23	035	207.28800000000001	Flag	035	035	\N	\N	\N	010100000064113C1E70BB4040011B3BA14A7B4140
24	036	208.00899999999999	Flag	036	036	\N	\N	\N	0101000000C4E223A16EBB4040B75FCC03497B4140
25	037	222.18899999999999	Flag	037	037	\N	\N	\N	0101000000358B5CDF63B940406AD6B4B3717E4140
26	038	233.965	Flag	038	038	\N	\N	\N	0101000000F73FC05A75B94040424EC0F8A77E4140
27	039	239.012	Flag	039	039	\N	\N	\N	0101000000222E0345A1B94040DD370884A77E4140
28	040	261.60300000000001	Flag	040	040	\N	\N	\N	0101000000B84C6C8FF5B240400FABEC79467E4140
29	041	264.00599999999997	Flag	041	041	\N	\N	\N	01010000007C579FD3F5B240402CD40C3C467E4140
30	042	283.47199999999998	Flag	042	042	\N	\N	\N	0101000000E2C45FAC12B240407A71E764D77E4140
31	043	297.411	Flag	043	043	\N	\N	\N	01010000008625EB5E11B14040124FAB2533804140
32	044	319.52100000000002	Flag	044	044	\N	\N	\N	01010000009E0CDFD956B1404005B0D3D8767F4140
33	045	431.995	Flag	045	045	\N	\N	\N	0101000000158F60EAB4A4404008899BF01E7F4140
34	046	429.83199999999999	Flag	046	046	\N	\N	\N	0101000000A9DF649FB0A44040A18978370B7F4140
35	047	385.61200000000002	Flag	047	047	\N	\N	\N	010100000043E188F553A54040189D6B19627F4140
36	048	351.245	Flag	048	048	\N	\N	\N	010100000069F3D7E2D5A9404027ECE7BC627D4140
37	049	221.708	Flag	049	049	\N	\N	\N	01010000003C541BE856B2404087B9A7228A8A4140
38	056	407.00099999999998	Flag	056	056	\N	\N	\N	0101000000FEF8220C09A74040EB4A57F2E97D4140
39	057	225.31299999999999	Flag	057	057	\N	\N	\N	0101000000B3381F7B56B24040B6B370D8848A4140
40	058	215.69999999999999	Flag	058	058	\N	\N	\N	01010000001256C37B57B240405A819300848A4140
41	059	407.48099999999999	Flag	059	059	\N	\N	\N	0101000000AB3D243021984040C3B77B7CAC854140
42	060	404.59800000000001	Flag	060	060	\N	\N	\N	0101000000610ED1D42A9840402227377DC2854140
43	061	348.36099999999999	Flag	061	061	\N	\N	\N	0101000000DF08049AC5A94040F92F57D9D37F4140
44	062	319.762	Flag	062	062	\N	\N	\N	01010000000DE600D8B8A940401F1A9CDC7D7F4140
45	063	317.839	Flag	063	063	\N	\N	\N	0101000000ECE87346B8A9404010B710D27D7F4140
46	064	320.72300000000001	Flag	064	064	\N	\N	\N	010100000043DF5C4ABDA940404795D8EA6F7F4140
47	065	320.72300000000001	Flag	065	065	\N	\N	\N	0101000000886FECCF01AA40401B395451517F4140
48	066	317.11799999999999	Flag	066	066	\N	\N	\N	01010000008B57B49447AA4040BEC617B5477F4140
49	067	317.11799999999999	Flag	067	067	\N	\N	\N	0101000000E8401B284CAA4040861978C0457F4140
50	AAL1	361.339	Flag	AAL1	AAL1	\N	\N	\N	010100000012A5049763A54040E058870B8F804140
51	AAL2	359.89600000000002	Flag	AAL2	AAL2	\N	\N	\N	0101000000A10DAC225DA540409A4C389B5E804140
52	AAL3	367.58699999999999	Flag	AAL3	AAL3	\N	\N	\N	0101000000450F307E50A54040C786A8A151804140
53	AAL4	363.26100000000002	Flag	AAL4	AAL4	\N	\N	\N	0101000000A8BA03195AA540405B36A0C931804140
54	AAL5	375.99799999999999	Flag	AAL5	AAL5	\N	\N	\N	010100000082C8784D75A54040B37BFC2204804140
55	AAL6	378.642	Flag	AAL6	AAL6	\N	\N	\N	0101000000E477931294A54040A190C0C605804140
56	AAL7	374.07600000000002	Flag	AAL7	AAL7	\N	\N	\N	01010000008D1B5B2889A540409CA4870E32804140
57	AAL8	371.43200000000002	Flag	AAL8	AAL8	\N	\N	\N	0101000000BF0348DC7FA54040E409031D4D804140
58	AGH6	392.58100000000002	Flag	AGH6	AGH6	\N	\N	\N	0101000000B5AEC4481E974040B9A7B0F40F854140
59	AGM1	372.63400000000001	Flag	AGM1	AGM1	\N	\N	\N	01010000008E429CCBF0964040A9BDCF8BEF844140
60	AGM2	395.94600000000003	Flag	AGM2	AGM2	\N	\N	\N	010100000084CDEC32FD9640401DD1C324EA844140
61	AGM3	395.22500000000002	Flag	AGM3	AGM3	\N	\N	\N	0101000000BF173BEE0F974040A678DBD3F5844140
62	AGM4	394.74400000000003	Flag	AGM4	AGM4	\N	\N	\N	0101000000D1339F6EFE964040B01434F305854140
63	AGM5	391.13900000000001	Flag	AGM5	AGM5	\N	\N	\N	010100000033686CD804974040549AD49D15854140
64	AGM6	400.03100000000001	Flag	AGM6	AGM6	\N	\N	\N	0101000000B245A7EE57974040A9C6ABA9F5844140
65	AGM7	403.39600000000002	Flag	AGM7	AGM7	\N	\N	\N	0101000000E4D020DA4E974040D15ED8F5E6844140
66	AGM8	399.06999999999999	Flag	AGM8	AGM8	\N	\N	\N	01010000007F8BC0EB4297404043DB9C10DB844140
67	AGR1	393.78300000000002	Flag	AGR1	AGR1	\N	\N	\N	01010000001511A78E3394404043ED637714864140
68	AGR10	404.35700000000003	Flag	AGR10	AGR10	\N	\N	\N	01010000003B06C9DA5D944040197B274D20864140
69	AGR2	392.34100000000001	Flag	AGR2	AGR2	\N	\N	\N	0101000000407887F03C9440409C0F745931864140
70	AGR9	406.51999999999998	Flag	AGR9	AGR9	\N	\N	\N	0101000000FD9EBBD145944040DC6548E012864140
71	ALAMR	332.98000000000002	Flag	ALAMR	ALAMR	\N	\N	\N	0101000000F381BCD5F5B240409B0E37BC6D7D4140
72	ALAMS1	298.13200000000001	Flag	ALAMS1	ALAMS1	\N	\N	\N	01010000008890042B58B3404084B774BD4E7D4140
73	ALAMS2	291.40300000000002	Flag	ALAMS2	ALAMS2	\N	\N	\N	010100000065B18C8E5DB34040FD969BD06D7D4140
74	ALAMS4	294.04700000000003	Flag	ALAMS4	ALAMS4	\N	\N	\N	0101000000105D2FD149B34040BBEAB0AB517D4140
75	ALAMS5	292.36500000000001	Flag	ALAMS5	ALAMS5	\N	\N	\N	0101000000438CD7BC6AB34040D879441A4F7D4140
76	ALAMS6	289.721	Flag	ALAMS6	ALAMS6	\N	\N	\N	01010000006EEADB006EB340400C23500C6F7D4140
77	ALES1	269.053	Flag	ALES1	ALES1	\N	\N	\N	01010000009EDF67C41BB6404093251F2D21804140
78	ALES2	266.649	Flag	ALES2	ALES2	\N	\N	\N	0101000000EACE13CF19B64040B0E693242E804140
79	ALES3	268.572	Flag	ALES3	ALES3	\N	\N	\N	0101000000BDD75F622BB64040B117988226804140
80	ALES4	267.61099999999999	Flag	ALES4	ALES4	\N	\N	\N	0101000000F714D36112B640402F09C4A91E804140
81	ALES5	241.17500000000001	Flag	ALES5	ALES5	\N	\N	\N	010100000012588743B9B54040E0D423E726804140
82	ALESX1	260.40100000000001	Flag	ALESX1	ALESX1	\N	\N	\N	010100000063AFD03704B640400A87E39B1B804140
83	ALKO1	247.18299999999999	Flag	ALKO1	ALKO1	\N	\N	\N	0101000000B66044037AB540404C403F98C37F4140
84	ALKO2	262.32299999999998	Flag	ALKO2	ALKO2	\N	\N	\N	0101000000200BAB5A8DB5404023B284BFA37F4140
85	ALKO3	263.52499999999998	Flag	ALKO3	ALKO3	\N	\N	\N	01010000008493EBA895B54040FE1B9091A77F4140
86	ALKO4	261.60300000000001	Flag	ALKO4	ALKO4	\N	\N	\N	01010000001C0F2DE78FB540405E5FDC27B67F4140
87	ALKO5	256.79599999999999	Flag	ALKO5	ALKO5	\N	\N	\N	0101000000FC0BFD468DB540402C63DB53C67F4140
88	ALKS1	289.96100000000001	Flag	ALKS1	ALKS1	\N	\N	\N	0101000000952000916EB34040EADD64C3767D4140
89	ALKS2	288.03899999999999	Flag	ALKS2	ALKS2	\N	\N	\N	0101000000977D4F9376B3404034F300F0717D4140
90	ALKS3	282.27100000000002	Flag	ALKS3	ALKS3	\N	\N	\N	0101000000A9A13387E9B340400F7043B7577D4140
91	ALKS4	303.17899999999997	Flag	ALKS4	ALKS4	\N	\N	\N	010100000067DB10B33CB34040D212CB30657D4140
92	ALKS5	299.815	Flag	ALKS5	ALKS5	\N	\N	\N	0101000000FFEC0FE069B340408BE68C1E7B7D4140
93	ALKS6	282.27100000000002	Flag	ALKS6	ALKS6	\N	\N	\N	0101000000123E544D22B3404067D23495B67D4140
94	ALKS7	276.262	Flag	ALKS7	ALKS7	\N	\N	\N	0101000000C505B43561B34040936E3CB9D57D4140
95	ALKSX1	288.03899999999999	Flag	ALKSX1	ALKSX1	\N	\N	\N	0101000000F36ECCB55FB340407C32B0A2877D4140
96	ALKSX2	287.077	Flag	ALKSX2	ALKSX2	\N	\N	\N	0101000000A9AEA45E75B34040751BCFC5717D4140
97	ALKSX3	278.42500000000001	Flag	ALKSX3	ALKSX3	\N	\N	\N	010100000066EC900ABFB3404098306F15917D4140
98	ALL1	314.71499999999997	Flag	ALL1	ALL1	\N	\N	\N	0101000000E5CCCF3DC6B14040D0D07C24DE7E4140
99	ALL2	294.28699999999998	Flag	ALL2	ALL2	\N	\N	\N	01010000005059A3D70CB24040EEC1184FCC7E4140
100	ALL3	293.08499999999998	Flag	ALL3	ALL3	\N	\N	\N	010100000029AED4FAF5B14040A4F737AFC47E4140
101	ALL4	292.84500000000003	Flag	ALL4	ALL4	\N	\N	\N	0101000000102AF756B2B14040A97C8B2BC77E4140
102	ALL5	295.48899999999998	Flag	ALL5	ALL5	\N	\N	\N	01010000007A9993DDADB14040B31E6393E17E4140
103	ALM1	313.75400000000002	Flag	ALM1	ALM1	\N	\N	\N	0101000000FBDC1FDB31B340409592E323697D4140
104	ALM2	328.173	Flag	ALM2	ALM2	\N	\N	\N	01010000000EC35BA8FBB24040104254A26B7D4140
105	ALM3	316.15699999999998	Flag	ALM3	ALM3	\N	\N	\N	01010000005FA9786DC0B240403DD3FFFD9F7D4140
106	ALM4	332.01900000000001	Flag	ALM4	ALM4	\N	\N	\N	01010000002BB5537D6BB240400E2F8848CD7D4140
107	ALM5	306.06299999999999	Flag	ALM5	ALM5	\N	\N	\N	01010000006E6477B18BB24040A808DF17F17D4140
108	ALM6	257.27699999999999	Flag	ALM6	ALM6	\N	\N	\N	010100000061B48CA9BBB24040AA1774294B7E4140
109	ALM7	262.32299999999998	Flag	ALM7	ALM7	\N	\N	\N	0101000000C099F3E7F6B24040DC5977D0457E4140
110	ALM8	268.09100000000001	Flag	ALM8	ALM8	\N	\N	\N	0101000000F1FAC4E816B34040B04B0B99167E4140
111	ALM9	280.58800000000002	Flag	ALM9	ALM9	\N	\N	\N	01010000000F4DF73F22B34040E7121BE7C77D4140
112	ALN1	325.52999999999997	Flag	ALN1	ALN1	\N	\N	\N	010100000003D100CFD7B14040E8F74EE3967E4140
113	ALN2	334.66199999999998	Flag	ALN2	ALN2	\N	\N	\N	0101000000CBB47CAB15B240409FE5A4F0537E4140
114	ALN3	345.23599999999999	Flag	ALN3	ALN3	\N	\N	\N	0101000000978DD3B2E3B14040FF9DBF99707E4140
115	ALN4	314.47399999999999	Flag	ALN4	ALN4	\N	\N	\N	01010000009A651C11B6B140406F4BF3ADA07E4140
116	ALN5	298.13200000000001	Flag	ALN5	ALN5	\N	\N	\N	0101000000BFAB9CF8D5B1404039163715B97E4140
117	ALN6	290.44200000000001	Flag	ALN6	ALN6	\N	\N	\N	0101000000C50F00E20FB24040F7F0AE78BF7E4140
118	ALN7	282.51100000000002	Flag	ALN7	ALN7	\N	\N	\N	0101000000F4F1D8E83FB240405A9603CBBE7E4140
119	ALS1	287.077	Flag	ALS1	ALS1	\N	\N	\N	01010000008E4AA07799B640401C5FE80238804140
120	ALS10	252.95099999999999	Flag	ALS10	ALS10	\N	\N	\N	01010000006F01576896B640402E2A7C92C8804140
121	ALS11	235.887	Flag	ALS11	ALS11	\N	\N	\N	0101000000041D576AD0B64040BA926BAAB1804140
122	ALS2	233.72399999999999	Flag	ALS2	ALS2	\N	\N	\N	01010000000D4EA7E9F1B64040848757FCA0804140
123	ALS3	243.33799999999999	Flag	ALS3	ALS3	\N	\N	\N	0101000000CD3AB3C20CB74040F78A130474804140
124	ALS4	246.46199999999999	Flag	ALS4	ALS4	\N	\N	\N	0101000000CFBA88E713B740409141B47742804140
125	ALS5	238.77099999999999	Flag	ALS5	ALS5	\N	\N	\N	01010000009D364860EAB64040D91A5B199E804140
126	ALS6	236.60900000000001	Flag	ALS6	ALS6	\N	\N	\N	01010000003BE16754C7B64040977293589F804140
127	ALS7	239.733	Flag	ALS7	ALS7	\N	\N	\N	0101000000CDB22C3A87B640407C31F48599804140
128	ALS8	243.81800000000001	Flag	ALS8	ALS8	\N	\N	\N	0101000000ED10092359B64040A65D17418D804140
129	ALS9	241.41499999999999	Flag	ALS9	ALS9	\N	\N	\N	0101000000154B2C517FB640408846AFBA99804140
130	ANA1	426.46699999999998	Flag	ANA1	ANA1	\N	\N	\N	010100000074D42C2BA8A4404036F5EC7F217F4140
131	ANA2	437.76299999999998	Flag	ANA2	ANA2	\N	\N	\N	01010000001512A00EBAA44040AD76E765217F4140
132	ANA3	441.608	Flag	ANA3	ANA3	\N	\N	\N	0101000000C25A08B4ACA440408F23D8E3FE7E4140
133	ANA4	429.59199999999998	Flag	ANA4	ANA4	\N	\N	\N	01010000004512E80CBFA440402DFAA0ED2C7F4140
134	ANA5	429.351	Flag	ANA5	ANA5	\N	\N	\N	010100000035D2EF4EC0A4404016706710337F4140
135	ANA6	413.49000000000001	Flag	ANA6	ANA6	\N	\N	\N	01010000004F1EEBE9B0A44040CF22FFBF367F4140
136	ANK1	383.92899999999997	Flag	ANK1	ANK1	\N	\N	\N	01010000007800D58955A540402B9CBB95597F4140
137	ANK2	377.68099999999998	Flag	ANK2	ANK2	\N	\N	\N	010100000065E137FA62A54040ABC624B9827F4140
138	ANK3	385.85199999999998	Flag	ANK3	ANK3	\N	\N	\N	0101000000491A20F453A540409B39B718627F4140
139	ANK4	406.27999999999997	Flag	ANK4	ANK4	\N	\N	\N	010100000081CC78A23EA54040A5C7338B377F4140
140	ANK5	375.51799999999997	Flag	ANK5	ANK5	\N	\N	\N	010100000017400FED97A540403047DB6D617F4140
141	ANK6	378.642	Flag	ANK6	ANK6	\N	\N	\N	0101000000BE5E5FC092A540401C7A9040717F4140
142	ANKV1	388.73599999999999	Flag	ANKV1	ANKV1	\N	\N	\N	01010000005B9C1FE98DA34040CEE1E89739804140
143	ANKV2	382.00700000000001	Flag	ANKV2	ANKV2	\N	\N	\N	01010000001432C75A92A3404099DF8C4137804140
144	ANKV3	384.64999999999998	Flag	ANKV3	ANKV3	\N	\N	\N	0101000000EDE10F628EA340405257674A25804140
145	ANKV4	385.85199999999998	Flag	ANKV4	ANKV4	\N	\N	\N	01010000008DC7C83689A340401229DFC92D804140
146	ANKV5	386.33300000000003	Flag	ANKV5	ANKV5	\N	\N	\N	010100000081FF483961A34040A44803E728804140
147	ANKV6	386.57299999999998	Flag	ANKV6	ANKV6	\N	\N	\N	0101000000D69B287464A34040639A981728804140
148	ANKV7	384.41000000000003	Flag	ANKV7	ANKV7	\N	\N	\N	0101000000FC4FD36E81A34040B80D4F212A804140
149	ANP1	353.16699999999997	Flag	ANP1	ANP1	\N	\N	\N	01010000008E2DE07270A4404014C98FACC7804140
150	ANP2	368.30799999999999	Flag	ANP2	ANP2	\N	\N	\N	01010000007A63DB7E86A440402E0969F0AB804140
151	ANP3	370.471	Flag	ANP3	ANP3	\N	\N	\N	0101000000B2F5E61976A4404073E88F91B5804140
152	ANV1	364.46300000000002	Flag	ANV1	ANV1	\N	\N	\N	01010000003F4D7CA1FFA44040DA2CEF8EB2804140
153	ANV2	363.74200000000002	Flag	ANV2	ANV2	\N	\N	\N	010100000040B10408D5A44040F7E48768B9804140
154	ANV4	361.09800000000001	Flag	ANV4	ANV4	\N	\N	\N	0101000000AA2C7C1091A440400CA6B03E9D804140
155	ARC1	408.68299999999999	Flag	ARC1	ARC1	\N	\N	\N	01010000007BDD0B9420984040F5C2D7B6AC854140
156	ARC2	409.404	Flag	ARC2	ARC2	\N	\N	\N	01010000002A2B500250984040A3F9CEF5DE854140
157	ARC4	407.00099999999998	Flag	ARC4	ARC4	\N	\N	\N	0101000000CC132B4A5998404094DB7F2FC8854140
158	ARC5	415.17200000000003	Flag	ARC5	ARC5	\N	\N	\N	0101000000AEC1F8F24D9840407033AFFFC7854140
159	AS1	373.59500000000003	Flag	AS1	AS1	\N	\N	\N	0101000000311384ACA0A540400805EC3D71804140
160	AS2	371.19200000000001	Flag	AS2	AS2	\N	\N	\N	01010000001867B873B4A54040A1EE578F68804140
161	AS3	376.71899999999999	Flag	AS3	AS3	\N	\N	\N	0101000000A68B24ACD3A54040B8E060FD4C804140
162	ATMB1	321.20400000000001	Flag	ATMB1	ATMB1	\N	\N	\N	01010000002AC264E24AA94040ACE81486B67F4140
163	ATMB2	321.92500000000001	Flag	ATMB2	ATMB2	\N	\N	\N	010100000042B6100A68A940408AB66032A07F4140
164	ATMB3	321.92500000000001	Flag	ATMB3	ATMB3	\N	\N	\N	010100000089835BA96DA94040D80D9441997F4140
165	ATMB4	320.00200000000001	Flag	ATMB4	ATMB4	\N	\N	\N	010100000001ACF80089A9404060ACF398967F4140
166	ATMB5	318.31999999999999	Flag	ATMB5	ATMB5	\N	\N	\N	0101000000B173A075B7A940403E3783AB7B7F4140
167	ATMB6	321.44400000000002	Flag	ATMB6	ATMB6	\N	\N	\N	0101000000F016F708BDA9404049F560FC7B7F4140
168	ATV1	307.745	Flag	ATV1	ATV1	\N	\N	\N	01010000008EED8768CCAA40406067707F617F4140
169	ATV10	334.90199999999999	Flag	ATV10	ATV10	\N	\N	\N	01010000007C446325DAA94040397064A4B27E4140
170	ATV2	300.77600000000001	Flag	ATV2	ATV2	\N	\N	\N	010100000044CD6C6492AA404088D0F451477F4140
171	ATV3	309.428	Flag	ATV3	ATV3	\N	\N	\N	0101000000B628074174AA40408D180B7D4F7F4140
172	ATV4	310.149	Flag	ATV4	ATV4	\N	\N	\N	01010000001203136E68AA404073FADA30537F4140
173	ATV5	304.38099999999997	Flag	ATV5	ATV5	\N	\N	\N	0101000000E9A0C7FF4FAA4040408C8CE5487F4140
174	ATV6	317.839	Flag	ATV6	ATV6	\N	\N	\N	0101000000BC99942CEEA94040C3012F5E2C7F4140
175	ATV7	314.95499999999998	Flag	ATV7	ATV7	\N	\N	\N	0101000000761E031CBCA94040FB863B77F87E4140
176	ATV8	335.62299999999999	Flag	ATV8	ATV8	\N	\N	\N	0101000000D05798D7F4A84040A9B4C8ED8C7E4140
177	ATV9	323.12599999999998	Flag	ATV9	ATV9	\N	\N	\N	0101000000FB8B3B066DA94040C972BC49A67E4140
178	AV-P1	298.85300000000001	Flag	AV-P1	AV-P1	\N	\N	\N	01010000009D726B2375AE40402FF49AB4EF804140
179	AV-P2	312.79199999999997	Flag	AV-P2	AV-P2	\N	\N	\N	01010000001DA0D3AA58AE40404225F07FEE804140
180	AV-P3	309.90800000000002	Flag	AV-P3	AV-P3	\N	\N	\N	0101000000D0491F5535AE40403C8150E1FF804140
181	AV12	309.66800000000001	Flag	AV12	AV12	\N	\N	\N	0101000000F20E41EC44AB4040879A373A667F4140
182	AV13	305.10199999999998	Flag	AV13	AV13	\N	\N	\N	01010000003A335C4548AB4040508EA4D27F7F4140
183	AV14	327.69299999999998	Flag	AV14	AV14	\N	\N	\N	0101000000A090F3B76AAB4040FBF767A67E7F4140
184	AV15	305.58300000000003	Flag	AV15	AV15	\N	\N	\N	01010000009D15D74C4CAB4040643A23339F7F4140
185	AV3	318.07999999999998	Flag	AV3	AV3	\N	\N	\N	010100000030A6BCFD89AB40404007CD165F7F4140
186	AV4	318.56	Flag	AV4	AV4	\N	\N	\N	01010000004729A79164AB404062D8D3A1627F4140
187	AV4A	321.44400000000002	Flag	AV4A	AV4A	\N	\N	\N	0101000000436173B565AB404087BA844D617F4140
188	AV5	327.69299999999998	Flag	AV5	AV5	\N	\N	\N	01010000003211A1D780AB40409F772B97427F4140
189	AV6	317.839	Flag	AV6	AV6	\N	\N	\N	01010000003AFA58F460AB4040010633BA447F4140
190	AVK1	296.69	Flag	AVK1	AVK1	\N	\N	\N	01010000002A7F406511B14040369A088D33804140
191	AVK10	307.745	Flag	AVK10	AVK10	\N	\N	\N	01010000008006C4B9C0B04040A4F55C4B6D7F4140
192	AVK11	300.29500000000002	Flag	AVK11	AVK11	\N	\N	\N	0101000000F4C77C8CAEB04040FE531429B97F4140
193	AVK12	295.00799999999998	Flag	AVK12	AVK12	\N	\N	\N	0101000000B16C37D8D3B0404037E0C38AF27F4140
194	AVK2	279.62700000000001	Flag	AVK2	AVK2	\N	\N	\N	01010000003C4CAF58F9B04040C4317C5830804140
195	AVK3	276.98399999999998	Flag	AVK3	AVK3	\N	\N	\N	0101000000133F1F953DB1404077C234D474804140
196	AVK4	267.851	Flag	AVK4	AVK4	\N	\N	\N	01010000005BA7F77854B140407CDC38DB3C804140
197	AVK5	265.928	Flag	AVK5	AVK5	\N	\N	\N	01010000005BE0583B7DB140404322A78E19804140
198	AVK6	283.95299999999997	Flag	AVK6	AVK6	\N	\N	\N	0101000000CA1B283C9DB14040E591B425EF7F4140
199	AVK7	293.80599999999998	Flag	AVK7	AVK7	\N	\N	\N	0101000000BD21C7B565B1404094149FD59E7F4140
200	AVK8	323.36700000000002	Flag	AVK8	AVK8	\N	\N	\N	0101000000278BE4F356B14040CCAB632A777F4140
201	AVK9	329.375	Flag	AVK9	AVK9	\N	\N	\N	010100000053C6F77917B14040D51CF05E4D7F4140
202	AVKX1	276.74299999999999	Flag	AVKX1	AVKX1	\N	\N	\N	0101000000D4F2BC812DB140406D78177B71804140
203	AVP1	335.38299999999998	Flag	AVP1	AVP1	\N	\N	\N	01010000004390CFA02BAD4040652F13F9F97D4140
204	AVP2	338.74799999999999	Flag	AVP2	AVP2	\N	\N	\N	0101000000B60D4D203FAD40407CCD84BCE47D4140
205	AVP3	344.03500000000003	Flag	AVP3	AVP3	\N	\N	\N	01010000005D082D7C21AD40402BABC0FBF07D4140
206	AVP4	331.29700000000003	Flag	AVP4	AVP4	\N	\N	\N	0101000000852FBB4324AD4040978588312F7E4140
207	AVP5	316.39699999999999	Flag	AVP5	AVP5	\N	\N	\N	0101000000BA9DC4BA0DAE4040BE3134395D7E4140
208	AVP6	318.80099999999999	Flag	AVP6	AVP6	\N	\N	\N	010100000030E41896EDAD4040C9FCD660377E4140
209	AVP7	314.71499999999997	Flag	AVP7	AVP7	\N	\N	\N	0101000000770D116FD1AD4040A8FF606B2C7E4140
210	AVT1	301.49700000000001	Flag	AVT1	AVT1	\N	\N	\N	0101000000062D40B5A3B04040ACECB86A69804140
211	AVT2	285.63499999999999	Flag	AVT2	AVT2	\N	\N	\N	01010000009E2988420AB140402BD88858AB804140
212	AVT3	276.50299999999999	Flag	AVT3	AVT3	\N	\N	\N	01010000002AF95C96C4B04040E261F88D51804140
213	AVT4	302.45800000000003	Flag	AVT4	AVT4	\N	\N	\N	01010000003C0A9DF8E6AF4040D28E881A26804140
214	AVT5	309.18700000000001	Flag	AVT5	AVT5	\N	\N	\N	0101000000C999D725ABAF4040D7A0D4034B804140
215	AVT6	308.226	Flag	AVT6	AVT6	\N	\N	\N	010100000047ECB015B6AF404023022CF7A4804140
216	AYPET	406.75999999999999	Flag	AYPET	AYPET	\N	\N	\N	01010000003D6CB79753974040609D90C1BD844140
217	AYVAR2	311.59100000000001	Flag	AYVAR2	AYVAR2	\N	\N	\N	01010000002EB898FF8DAB404088B804A37C7F4140
218	CAVE	317.59899999999999	Flag	CAVE	CAVE	\N	\N	\N	0101000000D589C437E6B24040413407C9817D4140
219	CHUR	250.547	Flag	CHUR	CHUR	\N	\N	\N	01010000007EE0501603B340400649BBAB807E4140
220	CR0SSN	301.97800000000001	Flag	CR0SSN	CR0SSN	\N	\N	\N	010100000031CD071365AB4040B8A704F41D7E4140
221	DAT11A	262.56400000000002	Flag	DAT11A	DAT11A	\N	\N	\N	0101000000A57077B5E0B3404091EF9E78587D4140
222	K2-1	315.67599999999999	Flag	K2-1	K2-1	\N	\N	\N	0101000000B946783974AB4040BA0707CC9A7F4140
223	K2-2	313.99400000000003	Flag	K2-2	K2-2	\N	\N	\N	0101000000EF767B1873AB40405DB39798A77F4140
224	K2-3	307.26499999999999	Flag	K2-3	K2-3	\N	\N	\N	0101000000DA82876466AB404067FDB4F1AA7F4140
225	K2-4	312.79199999999997	Flag	K2-4	K2-4	\N	\N	\N	01010000006652A38D6CAB40405B307C3A9A7F4140
226	K2-5	314.95499999999998	Flag	K2-5	K2-5	\N	\N	\N	010100000027410B4B74AB404010F2DAA2957F4140
227	K3-1	311.11000000000001	Flag	K3-1	K3-1	\N	\N	\N	0101000000124D179767AB4040696E1814AC7F4140
228	K3-2	312.79199999999997	Flag	K3-2	K3-2	\N	\N	\N	0101000000A22A800B6DAB4040A58CF764B77F4140
229	K3-3	314.95499999999998	Flag	K3-3	K3-3	\N	\N	\N	0101000000E415046670AB40402B78F064CE7F4140
230	K3-4	315.67599999999999	Flag	K3-4	K3-4	\N	\N	\N	0101000000ABBDFC0C77AB404031B18763CE7F4140
231	K3-5	304.14100000000002	Flag	K3-5	K3-5	\N	\N	\N	0101000000D059944975AB404016F1CBD0E07F4140
232	K3-6	297.65199999999999	Flag	K3-6	K3-6	\N	\N	\N	0101000000038D18C467AB404098417F18D67F4140
233	K3-7	305.58300000000003	Flag	K3-7	K3-7	\N	\N	\N	01010000009A08672C64AB4040E9BA3759BF7F4140
234	K3-8	305.58300000000003	Flag	K3-8	K3-8	\N	\N	\N	0101000000D004207464AB40406A6BBB3EB07F4140
235	KELID1	390.178	Flag	KELID1	KELID1	\N	\N	\N	01010000007D2C9B3E1D9D4040E638A8DE89834140
236	KELID2	381.52600000000001	Flag	KELID2	KELID2	\N	\N	\N	0101000000E40B88802B9D40409425634A9E834140
237	KK1	410.846	Flag	KK1	KK1	\N	\N	\N	0101000000C7BF67D5FBA640409C16B72FF27D4140
238	KK2	407.72199999999998	Flag	KK2	KK2	\N	\N	\N	010100000027CB2FD4F8A64040BFF52ECCEC7D4140
239	KK3	403.87700000000001	Flag	KK3	KK3	\N	\N	\N	01010000006DFE04BCF1A640401F45B455FD7D4140
240	KK4	396.90699999999998	Flag	KK4	KK4	\N	\N	\N	0101000000228F3B00FDA64040364D98630B7E4140
241	KK5	405.79899999999998	Flag	KK5	KK5	\N	\N	\N	0101000000BD53945A0EA74040E82330A3067E4140
242	KK6	407.00099999999998	Flag	KK6	KK6	\N	\N	\N	0101000000A252781209A7404035463CF0E97D4140
243	KKA1	381.76600000000002	Flag	KKA1	KKA1	\N	\N	\N	01010000004902F89A6AA64040B23D0B26E47D4140
244	KKA2	391.86000000000001	Flag	KKA2	KKA2	\N	\N	\N	010100000098AE9F7F74A640404D018755DE7D4140
245	KL1	370.471	Flag	KL1	KL1	\N	\N	\N	01010000003C93339D68A6404027181F7D4B7E4140
246	KR1	368.06799999999998	Flag	KR1	KR1	\N	\N	\N	0101000000838603C327A64040269C27E8407F4140
247	LYP1	202.001	Flag	LYP1	LYP1	\N	\N	\N	010100000021E3FB2968BB404067CB7F694D7B4140
248	LYP10	225.07300000000001	Flag	LYP10	LYP10	\N	\N	\N	0101000000B93103B63FB94040F456FF15697E4140
249	LYP11	231.56200000000001	Flag	LYP11	LYP11	\N	\N	\N	0101000000A187936140B9404060EAE74D857E4140
250	LYP12	230.36000000000001	Flag	LYP12	LYP12	\N	\N	\N	0101000000A142571D57B94040423C08BD9B7E4140
251	LYP13	232.28200000000001	Flag	LYP13	LYP13	\N	\N	\N	0101000000E8DC345075B94040956D031EA87E4140
252	LYP14	240.21299999999999	Flag	LYP14	LYP14	\N	\N	\N	0101000000E062A837A1B94040FB03B57DA77E4140
253	LYP15	244.059	Flag	LYP15	LYP15	\N	\N	\N	01010000005D7EE7E4B3B94040B7FAAA8FD97E4140
254	LYP16	248.14400000000001	Flag	LYP16	LYP16	\N	\N	\N	010100000029720CF1DDB94040670C4802DA7E4140
255	LYP17	240.934	Flag	LYP17	LYP17	\N	\N	\N	01010000006FF4BFC9E7B940402D7A602DE27E4140
256	LYP18	230.36000000000001	Flag	LYP18	LYP18	\N	\N	\N	010100000075CE635503BA4040B8E9CF7EE47E4140
257	LYP2	229.87899999999999	Flag	LYP2	LYP2	\N	\N	\N	01010000007F24A30513BA4040F294842DBE7E4140
258	LYP3	233.48400000000001	Flag	LYP3	LYP3	\N	\N	\N	0101000000EA35EF98F7B94040439CB4949F7E4140
259	LYP4	235.40700000000001	Flag	LYP4	LYP4	\N	\N	\N	0101000000AA2034F2E1B9404000E9B4D1AD7E4140
260	LYP5	229.87899999999999	Flag	LYP5	LYP5	\N	\N	\N	0101000000463AA3B0C2B94040AC23AC65A97E4140
261	LYP6	232.76300000000001	Flag	LYP6	LYP6	\N	\N	\N	0101000000A5CD8753A5B940400696308EA27E4140
262	LYP7	226.51499999999999	Flag	LYP7	LYP7	\N	\N	\N	010100000017842CEA97B94040EF93EF8E967E4140
263	LYP8	227.23599999999999	Flag	LYP8	LYP8	\N	\N	\N	0101000000E70481565FB9404092815FCA907E4140
264	LYP9	223.631	Flag	LYP9	LYP9	\N	\N	\N	0101000000085C24DB63B94040BD526BA1717E4140
265	M-P1	325.28899999999999	Flag	M-P1	M-P1	\N	\N	\N	0101000000FFB3CF2B2AAB4040C8CC68580A7E4140
266	M-VS1	325.28899999999999	Flag	M-VS1	M-VS1	\N	\N	\N	01010000005C50A0CEE3AB40401D02D5ACA47D4140
267	M-VS10	321.68400000000003	Flag	M-VS10	M-VS10	\N	\N	\N	01010000009FD4B0427EAB4040F0BAF3CE257E4140
268	M-VS11	320.00200000000001	Flag	M-VS11	M-VS11	\N	\N	\N	010100000064506B6F77AB404019FF64AA257E4140
269	M-VS12	327.21199999999999	Flag	M-VS12	M-VS12	\N	\N	\N	0101000000510CDFAE96AB40400B185FDFC77D4140
270	M-VS2	358.935	Flag	M-VS2	M-VS2	\N	\N	\N	0101000000950EF70DE3AB40407902F40AEA7D4140
271	M-VS3	364.22300000000001	Flag	M-VS3	M-VS3	\N	\N	\N	0101000000D1CB3F32D1AB4040C25E93FBFE7D4140
272	M-VS4	360.61700000000002	Flag	M-VS4	M-VS4	\N	\N	\N	0101000000622D43BEB9AB4040B481C7F9537E4140
273	M-VS5	353.40800000000002	Flag	M-VS5	M-VS5	\N	\N	\N	010100000042187FA8A2AB4040C1B02C334D7E4140
274	M-VS6	352.44600000000003	Flag	M-VS6	M-VS6	\N	\N	\N	01010000002F83944CCCAB4040F0C38817E07D4140
275	M-VS7	334.18200000000002	Flag	M-VS7	M-VS7	\N	\N	\N	01010000006E5170F5BFAB4040EBB39812A87D4140
276	M-VS8	334.42200000000003	Flag	M-VS8	M-VS8	\N	\N	\N	01010000002F2E2077BBAB4040EC6E5CCEBE7D4140
277	M-VS9	329.13499999999999	Flag	M-VS9	M-VS9	\N	\N	\N	0101000000C80DD37F95AB40403284875CF57D4140
278	MA1	397.14699999999999	Flag	MA1	MA1	\N	\N	\N	01010000007BF6F4C271A8404012C05B8D1D7E4140
279	P-AY1	314.95499999999998	Flag	P-AY1	P-AY1	\N	\N	\N	0101000000B06A5F2C4BAE404008176CCF5E814140
280	P-AY10	298.613	Flag	P-AY10	P-AY10	\N	\N	\N	0101000000ADEAA0A339AE4040912E8CAD8D814140
281	P-AY11	300.536	Flag	P-AY11	P-AY11	\N	\N	\N	0101000000D7FB76DA66AE404059A6DBE979814140
282	P-AY12	311.83100000000002	Flag	P-AY12	P-AY12	\N	\N	\N	01010000006BE4CFC46FAE4040674824F058814140
283	P-AY13	304.62099999999998	Flag	P-AY13	P-AY13	\N	\N	\N	0101000000B67FE42474AE4040349D985354814140
284	P-AY14	299.815	Flag	P-AY14	P-AY14	\N	\N	\N	010100000083DAD7D079AE4040D43ADCD461814140
285	P-AY15	318.07999999999998	Flag	P-AY15	P-AY15	\N	\N	\N	0101000000FE8FA48832AE404023349B2A4C814140
286	P-AY16	324.32799999999997	Flag	P-AY16	P-AY16	\N	\N	\N	01010000007F5F985607AE4040FAD4AC912F814140
287	P-AY17	333.70100000000002	Flag	P-AY17	P-AY17	\N	\N	\N	01010000006F4233E509AE40406109B0370D814140
288	P-AY3	309.90800000000002	Flag	P-AY3	P-AY3	\N	\N	\N	01010000004BA03F6F55AE40408321F43174814140
289	P-AY4	307.024	Flag	P-AY4	P-AY4	\N	\N	\N	0101000000EBB167AE57AE404063138C8F7E814140
290	P-AY5	308.226	Flag	P-AY5	P-AY5	\N	\N	\N	0101000000671BBFBD59AE4040EBA68F1E91814140
291	P-AY6	313.75400000000002	Flag	P-AY6	P-AY6	\N	\N	\N	010100000040456BCB54AE40408B5F202465814140
292	P-AY7	292.36500000000001	Flag	P-AY7	P-AY7	\N	\N	\N	0101000000F42098FE40AE40408CA05C1385814140
293	P-AY8	298.37299999999999	Flag	P-AY8	P-AY8	\N	\N	\N	0101000000030DF91146AE40406CBA407779814140
294	P-AY9	301.97800000000001	Flag	P-AY9	P-AY9	\N	\N	\N	010100000022C490FA3DAE4040A10A916973814140
295	PSK1	199.358	Flag	PSK1	PSK1	\N	\N	\N	0101000000704F208471BB40407E9BDFDF497B4140
296	PSK2	208.00899999999999	Flag	PSK2	PSK2	\N	\N	\N	01010000007434449E6FBB4040643AF3F9487B4140
297	PSK3	205.125	Flag	PSK3	PSK3	\N	\N	\N	01010000007D5BDBD473BB404043079C26657B4140
298	PSK4	206.08699999999999	Flag	PSK4	PSK4	\N	\N	\N	01010000005C45980587BB4040388BB4E0477B4140
299	PSK5	203.68299999999999	Flag	PSK5	PSK5	\N	\N	\N	0101000000E3BD2B017FBB4040E12E7CF63C7B4140
300	PSK6	206.80799999999999	Flag	PSK6	PSK6	\N	\N	\N	01010000005BF3800572BB40402A1314C53F7B4140
301	PSK7	202.96199999999999	Flag	PSK7	PSK7	\N	\N	\N	0101000000042B635061BB4040B6656F124B7B4140
302	SPIL1	289.48000000000002	Flag	SPIL1	SPIL1	\N	\N	\N	01010000008685F0EE5CB340405466DB4D567D4140
303	SPIL2	296.20999999999998	Flag	SPIL2	SPIL2	\N	\N	\N	01010000009086BBC95BB3404067D11095537D4140
304	SPIL3	298.613	Flag	SPIL3	SPIL3	\N	\N	\N	0101000000C1D4F76358B34040D5AEB368547D4140
305	SPIL4	299.815	Flag	SPIL4	SPIL4	\N	\N	\N	010100000004771C1655B3404027F71FBF557D4140
306	SPIL5	298.13200000000001	Flag	SPIL5	SPIL5	\N	\N	\N	0101000000FDA09BEE56B340405024446B587D4140
307	SPIL6	296.69	Flag	SPIL6	SPIL6	\N	\N	\N	01010000009CDBEC5E5AB340407A7F0FA0577D4140
308	SPIL7	299.334	Flag	SPIL7	SPIL7	\N	\N	\N	0101000000B53F434459B34040E9168CDA557D4140
309	TE	252.71000000000001	Flag	TE	TE	\N	\N	\N	010100000049ADF06108B340401800AD70807E4140
310	TE2	252.71000000000001	Flag	TE2	TE2	\N	\N	\N	010100000046BC935408B340402F8D2C87807E4140
311	TOMB1	397.62799999999999	Flag	TOMB1	TOMB1	\N	\N	\N	0101000000B1C55CBCD99340402772E42810864140
312	VOUNOS	390.41800000000001	Flag	VOUNOS	VOUNOS	\N	\N	\N	01010000001C278FEB82A54040828C7B51CD804140
\.


--
-- Data for Name: download5; Type: TABLE DATA; Schema: public; Owner: webuser
--

COPY download5 (gid, name, elevation, symbol, "comment", descriptio, source, url, "url name", the_geom) FROM stdin;
0	010	325.52999999999997	Flag	010	010	\N	\N	\N	01010000001806F7C6BAB140402C611C455E7A4140
1	011	449.05799999999999	Flag	011	011	\N	\N	\N	01010000000BFEDF5AEB9B40408A13CF41A6814140
2	012	484.62700000000001	Flag	012	012	\N	\N	\N	010100000096F6B718849A4040FBFD9A6043814140
3	013	500.24799999999999	Flag	013	013	\N	\N	\N	01010000007B95CB32839A4040A58CABD671814140
4	014	407.72199999999998	Flag	014	014	\N	\N	\N	0101000000742AC047E29B4040A21FD8B4FC824140
5	015	392.10000000000002	Flag	015	015	\N	\N	\N	0101000000EEF8521F589C40409631D3301B834140
6	016	398.10899999999998	Flag	016	016	\N	\N	\N	0101000000D810985E4D9C404048D67CBD04834140
7	017	346.43799999999999	Flag	017	017	\N	\N	\N	0101000000B3D58723BFB14040A5931474557A4140
8	018	323.84699999999998	Flag	018	018	\N	\N	\N	01010000006900440168AB40405E0568C3707F4140
9	019	316.63799999999998	Flag	019	019	\N	\N	\N	0101000000C669A47F6BAB4040E1FE268B707F4140
10	020	313.51299999999998	Flag	020	020	\N	\N	\N	01010000005C2B0C5768AB4040BE79DC7D6F7F4140
11	021	346.678	Flag	021	021	\N	\N	\N	0101000000F1A1DC6769AB40409726C8AF6F7F4140
12	022	319.762	Flag	022	022	\N	\N	\N	01010000003C4B6F0367AB40406E4560816F7F4140
13	023	153.45500000000001	Flag	023	023	\N	\N	\N	0101000000FCE4BBA3A5AD4040AFC4942C4D954140
14	024	166.91300000000001	Flag	024	024	\N	\N	\N	0101000000A5D41B71A5AD4040D07B084F4D954140
15	025	168.596	Flag	025	025	\N	\N	\N	01010000009C30C45EA6AD4040F97960BB4C954140
16	027	254.15199999999999	Flag	027	027	\N	\N	\N	0101000000125480348BAF40405811D0A541824140
17	028	321.92500000000001	Flag	028	028	\N	\N	\N	0101000000313C7FA5B6A94040A7660743BA7F4140
18	029	324.32799999999997	Flag	029	029	\N	\N	\N	010100000001FD569C7EA94040FA6A1480E97F4140
19	030	317.35899999999998	Flag	030	030	\N	\N	\N	0101000000176B43BB46A94040C74C23DFC57F4140
20	AAM1	380.565	Flag	AAM1	AAM1	\N	\N	\N	010100000096E77096DAA44040CB4068B48A834140
21	AAM2	349.322	Flag	AAM2	AAM2	\N	\N	\N	0101000000F6D70FD708A54040A8210F1C4E834140
22	AAY1	326.25099999999998	Flag	AAY1	AAY1	\N	\N	\N	0101000000A61AF3C353A74040CAF7060B59824140
23	AAY2	329.375	Flag	AAY2	AAY2	\N	\N	\N	010100000098CEB4E850A740405A928F1F62824140
24	AAY3	326.97199999999998	Flag	AAY3	AAY3	\N	\N	\N	0101000000ABF097B149A740402B34D3674E824140
25	AAY4	331.53800000000001	Flag	AAY4	AAY4	\N	\N	\N	01010000007A012BC439A740404250CB9555824140
26	AAY5	330.096	Flag	AAY5	AAY5	\N	\N	\N	01010000000896585630A7404060EDCF154A824140
27	ASP1	334.66199999999998	Flag	ASP1	ASP1	\N	\N	\N	010100000072FE98690AA74040A81149E08E824140
28	ASP2	351.48500000000001	Flag	ASP2	ASP2	\N	\N	\N	01010000003DF5277532A74040FED5505B84824140
29	ASP3	372.15300000000002	Flag	ASP3	ASP3	\N	\N	\N	0101000000BEF67B4B81A64040565B303F57824140
30	ASP4	377.44	Flag	ASP4	ASP4	\N	\N	\N	01010000007704E4094CA64040B2AA442AA3824140
31	ASP5	371.673	Flag	ASP5	ASP5	\N	\N	\N	0101000000CE7853786DA64040139D3F4FBE824140
32	ATMA1	307.26499999999999	Flag	ATMA1	ATMA1	\N	\N	\N	0101000000DC6707EF7AAA4040A594AF825A7F4140
33	ATMA2	321.92500000000001	Flag	ATMA2	ATMA2	\N	\N	\N	01010000002F51B87CB6A940400F280356BA7F4140
34	ATMA3	326.97199999999998	Flag	ATMA3	ATMA3	\N	\N	\N	010100000081F4F4E17EA94040F71C44AAE97F4140
35	ATMA4	331.05700000000002	Flag	ATMA4	ATMA4	\N	\N	\N	0101000000C4D0BF6430A940400F4FF3B122804140
36	ATMA5	322.64600000000002	Flag	ATMA5	ATMA5	\N	\N	\N	0101000000C82F47852DA94040666EA775E77F4140
37	ATMA6	318.07999999999998	Flag	ATMA6	ATMA6	\N	\N	\N	010100000094CEF7BB46A94040CA3D80ECC57F4140
38	ATMA7	328.654	Flag	ATMA7	ATMA7	\N	\N	\N	0101000000F7BD88E163A9404034F3420CC47F4140
39	ATMA8	323.12599999999998	Flag	ATMA8	ATMA8	\N	\N	\N	010100000024F83FBDA2A9404023959481A47F4140
40	ATMA9	310.38900000000001	Flag	ATMA9	ATMA9	\N	\N	\N	01010000001CEBBC6F37AA40405184EB89627F4140
41	ATV4	327.69299999999998	Flag	ATV4	ATV4	\N	\N	\N	0101000000877FB00A96AD40402D2C8FA03E7F4140
42	AVAP1	319.762	Flag	AVAP1	AVAP1	\N	\N	\N	0101000000A3F7D2839EAD4040A784D4ABD07F4140
43	AVAP2	311.11000000000001	Flag	AVAP2	AVAP2	\N	\N	\N	010100000005F0869C0DAE40405DC9ACE31A804140
44	AVP1	328.654	Flag	AVP1	AVP1	\N	\N	\N	0101000000B845F4C6A4AD404075E35C115C7F4140
45	AVP2	322.16500000000002	Flag	AVP2	AVP2	\N	\N	\N	01010000006835C47BC5AD4040AF3008A26A7F4140
46	AVPT1	318.07999999999998	Flag	AVPT1	AVPT1	\N	\N	\N	0101000000D5B897310EAE40409AFBFA0D927F4140
47	AVPT2	317.11799999999999	Flag	AVPT2	AVPT2	\N	\N	\N	01010000000F98F4E827AE4040BEC35F26D97F4140
48	AVQ1	318.07999999999998	Flag	AVQ1	AVQ1	\N	\N	\N	0101000000D8FAB24C70AB4040195A8BB9657F4140
49	AVR1	311.35000000000002	Flag	AVR1	AVR1	\N	\N	\N	0101000000273DE8E656AB404010886858727F4140
50	AVR10	317.35899999999998	Flag	AVR10	AVR10	\N	\N	\N	01010000001503B7FD51AB4040D1CE434F457F4140
51	AVR11	310.149	Flag	AVR11	AVR11	\N	\N	\N	0101000000A49FBFBC3FAB4040881078DC477F4140
52	AVR12	313.27300000000002	Flag	AVR12	AVR12	\N	\N	\N	0101000000C140E7A051AB4040FBC49CC8557F4140
53	AVR13	311.83100000000002	Flag	AVR13	AVR13	\N	\N	\N	01010000009D89BB0A41AB4040B56BFB84577F4140
54	AVR14	310.149	Flag	AVR14	AVR14	\N	\N	\N	01010000001D5ED32D42AB404052A414D7657F4140
55	AVR15	317.839	Flag	AVR15	AVR15	\N	\N	\N	0101000000A85C97D053AB4040E76B1FBD647F4140
56	AVR16	325.52999999999997	Flag	AVR16	AVR16	\N	\N	\N	0101000000A5CCE78B66AB4040B3CCCC13627F4140
57	AVR17	320.00200000000001	Flag	AVR17	AVR17	\N	\N	\N	01010000004D85C4B263AB404005E357E1537F4140
58	AVR2	320.00200000000001	Flag	AVR2	AVR2	\N	\N	\N	0101000000F586008267AB404055DB73B7707F4140
59	AVR3	304.38099999999997	Flag	AVR3	AVR3	\N	\N	\N	01010000001979AF1344AB4040072137FB777F4140
60	AVR4	303.89999999999998	Flag	AVR4	AVR4	\N	\N	\N	0101000000B733079948AB4040BB2653EE867F4140
61	AVR5	325.04899999999998	Flag	AVR5	AVR5	\N	\N	\N	0101000000F6A093EA6AAB404025994C4A7E7F4140
62	AVT1	334.18200000000002	Flag	AVT1	AVT1	\N	\N	\N	01010000007C3D04FAE9AC40403294AB09F67E4140
63	AVT2	327.21199999999999	Flag	AVT2	AVT2	\N	\N	\N	01010000007734DB0317AD40401BFCCF2A0B7F4140
64	AVT3	327.93299999999999	Flag	AVT3	AVT3	\N	\N	\N	0101000000DD08904332AD404003810850177F4140
65	AVY1	298.13200000000001	Flag	AVY1	AVY1	\N	\N	\N	01010000001CF328FF94AB40403F889C7250804140
66	CR1	320.00200000000001	Flag	CR1	CR1	\N	\N	\N	0101000000F79AF0DB68AB4040A3445F02767F4140
67	CR2	321.68400000000003	Flag	CR2	CR2	\N	\N	\N	0101000000B27B2F1469AB4040AEC85CD7777F4140
68	CR3	328.654	Flag	CR3	CR3	\N	\N	\N	01010000000E2E13016BAB4040DADAA49D787F4140
69	CR4	316.15699999999998	Flag	CR4	CR4	\N	\N	\N	0101000000DA10235156AB404015CD384A747F4140
70	CR4A	316.15699999999998	Flag	CR4A	CR4A	\N	\N	\N	01010000008304BD1E69AB4040DB220355707F4140
71	CR5	316.87799999999999	Flag	CR5	CR5	\N	\N	\N	01010000006E7FF46E68AB40407A770BF46E7F4140
72	CR6	316.15699999999998	Flag	CR6	CR6	\N	\N	\N	0101000000189520986DAB4040F4EFF8CB6E7F4140
73	DR1	219.30500000000001	Flag	DR1	DR1	\N	\N	\N	0101000000A840548455B2404042FDEF078A8A4140
74	K2	316.63799999999998	Flag	K2	K2	\N	\N	\N	01010000000F7408AA73AB4040BAD05F5FA07F4140
75	K3	314.23399999999998	Flag	K3	K3	\N	\N	\N	01010000009C2E0FC271AB4040ADF41217CB7F4140
76	K4	326.49099999999999	Flag	K4	K4	\N	\N	\N	0101000000525EAFD95FAB404092F2D2CE627F4140
77	K5	339.46899999999999	Flag	K5	K5	\N	\N	\N	010100000004B02C91FEAB4040665FA7C8497F4140
78	K6	331.29700000000003	Flag	K6	K6	\N	\N	\N	01010000007907A3521FAC4040AB178354787F4140
79	KAM1	467.56299999999999	Flag	KAM1	KAM1	\N	\N	\N	01010000002655C4B1BAA04040F093ECD6C7814140
80	KAM2	432.476	Flag	KAM2	KAM2	\N	\N	\N	0101000000BFC15B4390A14040FE88A3CE43834140
81	KKK1	324.80900000000003	Flag	KKK1	KKK1	\N	\N	\N	01010000004CE728DF6AAA40405B59FB6B14814140
82	MG1	352.68700000000001	Flag	MG1	MG1	\N	\N	\N	01010000007FF95F0CF7A94040A1596F5A84814140
83	PAI1	404.59800000000001	Flag	PAI1	PAI1	\N	\N	\N	01010000000D59D0C0379F40403FC63FE0FF824140
84	PAI2	399.06999999999999	Flag	PAI2	PAI2	\N	\N	\N	0101000000E07BD382449F404040FA7C4DFC824140
85	PAI3	423.82400000000001	Flag	PAI3	PAI3	\N	\N	\N	010100000024C327267A9F404057C5230A10834140
86	PAM1	384.89100000000002	Flag	PAM1	PAM1	\N	\N	\N	0101000000CA5FEF38649E4040E9F9C61CB3834140
87	PM6	396.42599999999999	Flag	PM6	PM6	\N	\N	\N	0101000000CC36A7FA649C404054ABB46704834140
88	PM6B	398.10899999999998	Flag	PM6B	PM6B	\N	\N	\N	01010000001CB05F3B4E9C4040984A7C4405834140
89	PM6B3	398.589	Flag	PM6B3	PM6B3	\N	\N	\N	010100000076EB3CF74C9C4040A5FC20A203834140
90	PM6B4	393.06200000000001	Flag	PM6B4	PM6B4	\N	\N	\N	0101000000D74763913F9C4040C8A1B8C2FF824140
91	PM6B5	392.34100000000001	Flag	PM6B5	PM6B5	\N	\N	\N	0101000000E2746CE64B9C404089C124DEFF824140
92	PMB1	259.68000000000001	Flag	PMB1	PMB1	\N	\N	\N	010100000039002C3CB9AF404051B433CB6C824140
93	PMB2	260.64100000000002	Flag	PMB2	PMB2	\N	\N	\N	01010000009CF83A0E9BAF4040EEF1A76522824140
94	PMB3	270.495	Flag	PMB3	PMB3	\N	\N	\N	01010000006C0E400528AF4040501C3B0676814140
95	PMB4	269.53300000000002	Flag	PMB4	PMB4	\N	\N	\N	01010000008327DB5D33AF4040A2E1673872814140
96	PMB5-	256.315	Flag	PMB5-	PMB5-	\N	\N	\N	01010000001FC5C4DC71AF4040E7C8B3938D824140
97	PMB6	259.92000000000002	Flag	PMB6	PMB6	\N	\N	\N	0101000000B3CAAF9338AF4040DE040F6E93824140
98	PMD11	499.04599999999999	Flag	PMD11	PMD11	\N	\N	\N	0101000000ABFE78173E9A40401AB348BAB6804140
99	PMD1A1	516.35000000000002	Flag	PMD1A1	PMD1A1	\N	\N	\N	0101000000A202505A329A4040B2B66B3AA9804140
100	PMD1A2	497.36399999999998	Flag	PMD1A2	PMD1A2	\N	\N	\N	0101000000727090E06B9A40401148F4F89E804140
101	PMD1A3	508.89999999999998	Flag	PMD1A3	PMD1A3	\N	\N	\N	01010000004FC0C0BD7C9A4040EF30D4FE9A804140
102	PMD1A4	501.93000000000001	Flag	PMD1A4	PMD1A4	\N	\N	\N	01010000001C6DEF2F8F9A40403ADDC36F94804140
103	PMD1A5	492.077	Flag	PMD1A5	PMD1A5	\N	\N	\N	010100000085E30BF0A39A404078C0B42990804140
104	PMD1B1	496.64299999999997	Flag	PMD1B1	PMD1B1	\N	\N	\N	01010000009E0C512F3F9A404053B45B93BA804140
105	PMD1B2	490.154	Flag	PMD1B2	PMD1B2	\N	\N	\N	01010000008D37CCAC729A40407ECFA33DB9804140
106	PMD1B3	497.84500000000003	Flag	PMD1B3	PMD1B3	\N	\N	\N	0101000000496D7FBA839A40406B7E18FBB4804140
107	PMD1B4	486.54899999999998	Flag	PMD1B4	PMD1B4	\N	\N	\N	0101000000EDDE0B459A9A40405CAFA725AF804140
108	PMD1B5	486.30900000000003	Flag	PMD1B5	PMD1B5	\N	\N	\N	0101000000CD392C98AE9A40407B26BC83A6804140
109	PMD1C1	498.80599999999998	Flag	PMD1C1	PMD1C1	\N	\N	\N	0101000000EB2748DE4C9A4040453260A0D6804140
110	PMD1C2	489.43299999999999	Flag	PMD1C2	PMD1C2	\N	\N	\N	0101000000D71687FB949A4040E31A8374CF804140
111	PMD1C3	464.43900000000002	Flag	PMD1C3	PMD1C3	\N	\N	\N	01010000001F3A239BC29A4040DCBE7CEFC5804140
112	PMD1C4	444.97300000000001	Flag	PMD1C4	PMD1C4	\N	\N	\N	010100000045A66FDEE79A4040C38E9467BD804140
113	PMD1D1	500.00799999999998	Flag	PMD1D1	PMD1D1	\N	\N	\N	0101000000EB3B5CFE459A4040CE696F20BF804140
114	PMD1D2	493.03800000000001	Flag	PMD1D2	PMD1D2	\N	\N	\N	01010000007D4110C2919A404024F7CF2EBA804140
115	PMD1D3	464.43900000000002	Flag	PMD1D3	PMD1D3	\N	\N	\N	0101000000361818E9AD9A40407311B9C2B6804140
116	PMD1D4	453.86500000000001	Flag	PMD1D4	PMD1D4	\N	\N	\N	010100000079366721DA9A40409C9DAC1BA6804140
117	PMD1E1	452.423	Flag	PMD1E1	PMD1E1	\N	\N	\N	0101000000FBD6EC20ED9A4040460ACDC4A0804140
118	PMD1E2	451.94200000000001	Flag	PMD1E2	PMD1E2	\N	\N	\N	010100000097B4FBB8EA9A40409612E7809B804140
119	PMD1E3	454.58600000000001	Flag	PMD1E3	PMD1E3	\N	\N	\N	0101000000300970AAD09A4040B6B2004A9C804140
120	PMD1E4	462.75700000000001	Flag	PMD1E4	PMD1E4	\N	\N	\N	0101000000EC069F20D09A40406FEE7A2CA7804140
121	PMD1E5	481.50200000000001	Flag	PMD1E5	PMD1E5	\N	\N	\N	010100000011971406C29A404003D13008AE804140
122	PMD1E6	485.34800000000001	Flag	PMD1E6	PMD1E6	\N	\N	\N	010100000037C45C78BC9A4040A148CCF3AD804140
123	PMD1E7	495.20100000000002	Flag	PMD1E7	PMD1E7	\N	\N	\N	0101000000BB124940819A404035E22F51B0804140
124	PMD1E8	484.86700000000002	Flag	PMD1E8	PMD1E8	\N	\N	\N	0101000000023B64EE4D9A40408FC6188DB9804140
125	PMD1E9	491.11500000000001	Flag	PMD1E9	PMD1E9	\N	\N	\N	01010000004C50001B499A4040F2D1BCC5BE804140
126	PMF1	501.93000000000001	Flag	PMF1	PMF1	\N	\N	\N	0101000000C35AD0094E9A4040F3B8CB25E5804140
127	PMFAR1	390.65800000000002	Flag	PMFAR1	PMFAR1	\N	\N	\N	01010000003B7D8BEE239C404057FDA7A121834140
128	PMFAR2	394.50400000000002	Flag	PMFAR2	PMFAR2	\N	\N	\N	010100000083D3AB962E9C404013060F1A14834140
129	PMFE2	482.94400000000002	Flag	PMFE2	PMFE2	\N	\N	\N	0101000000C3BC4361829A40402CF5BF6BF6804140
130	PMFEN	488.23200000000003	Flag	PMFEN	PMFEN	\N	\N	\N	0101000000B078B7A0A19A4040FEB03B6373814140
131	PMG1	494.24000000000001	Flag	PMG1	PMG1	\N	\N	\N	0101000000212A806E579A4040D2C5F7DC01814140
132	PMG2	471.40899999999999	Flag	PMG2	PMG2	\N	\N	\N	01010000005C15DBB6859A404035F68A46F5804140
133	PMG3	500.96899999999999	Flag	PMG3	PMG3	\N	\N	\N	01010000002D74C0D6AE9A40406D1A4808F0804140
134	PMG4	474.29199999999997	Flag	PMG4	PMG4	\N	\N	\N	0101000000109DB714C49A40402A5257FAED804140
135	PMH1	462.75700000000001	Flag	PMH1	PMH1	\N	\N	\N	0101000000490D0D8EC99A4040D141C7A9BE814140
136	PMH2	450.74000000000001	Flag	PMH2	PMH2	\N	\N	\N	0101000000D70051A2F89A40400F30F065AD814140
137	PMH3	440.887	Flag	PMH3	PMH3	\N	\N	\N	010100000000ED5B6E2F9B40400278CCBFA4814140
138	PMH4	425.50599999999997	Flag	PMH4	PMH4	\N	\N	\N	01010000001A4E8F297C9B4040F8FBC0B38F814140
139	PMH5	423.82400000000001	Flag	PMH5	PMH5	\N	\N	\N	0101000000270790FA9B9B404068C787858C814140
140	PMH6	418.77699999999999	Flag	PMH6	PMH6	\N	\N	\N	0101000000F65BE0979F9B4040E8780C5C8C814140
141	PMH7	437.28199999999998	Flag	PMH7	PMH7	\N	\N	\N	0101000000DE793381DA9B404064D0AB2F82814140
142	PMI6	441.84800000000001	Flag	PMI6	PMI6	\N	\N	\N	0101000000393ECFA9E89B40408F09C14297814140
143	PMJ1	449.05799999999999	Flag	PMJ1	PMJ1	\N	\N	\N	010100000028CA8C54EB9B40407577AC38A6814140
144	PMJ4	415.65300000000002	Flag	PMJ4	PMJ4	\N	\N	\N	0101000000BA7D84D1949B4040954758D0CB814140
145	PMJ5	441.84800000000001	Flag	PMJ5	PMJ5	\N	\N	\N	01010000005E81E390DF9A4040FE78384CF7814140
146	PMK1	434.87900000000002	Flag	PMK1	PMK1	\N	\N	\N	0101000000987064F5F99B4040E9D4DCA4BE814140
147	PMK3	409.64400000000001	Flag	PMK3	PMK3	\N	\N	\N	010100000058FFD805B29B4040F4FF7279E8814140
148	PMK6	420.21899999999999	Flag	PMK6	PMK6	\N	\N	\N	0101000000CD48731A989B4040A6E57CBBE9814140
149	PMK7	432.95600000000002	Flag	PMK7	PMK7	\N	\N	\N	010100000093A52FB4159B4040FC13F78002824140
150	PMK8	432.71600000000001	Flag	PMK8	PMK8	\N	\N	\N	0101000000063EE31BFA9A40400162A3900A824140
151	PML1	487.75099999999998	Flag	PML1	PML1	\N	\N	\N	01010000003DF5BAD8839A40407C1229E442814140
152	PML11	499.767	Flag	PML11	PML11	\N	\N	\N	010100000022375B2A839A4040962920CC71814140
153	PML13	484.62700000000001	Flag	PML13	PML13	\N	\N	\N	0101000000B2088BAB869A40408818ACC186814140
154	PML15	482.22300000000001	Flag	PML15	PML15	\N	\N	\N	01010000005086BC7B869A40402D3267A190814140
155	PML17	485.34800000000001	Flag	PML17	PML17	\N	\N	\N	01010000008212FCF59E9A40408B6A9FFBA3814140
156	PML3	492.077	Flag	PML3	PML3	\N	\N	\N	01010000007D864C067B9A4040ED0C746953814140
157	PML5	490.63499999999999	Flag	PML5	PML5	\N	\N	\N	0101000000EECEAFED7A9A4040C9508F195A814140
158	PML7	487.99099999999999	Flag	PML7	PML7	\N	\N	\N	01010000006E3A1B557A9A4040FB2AE7F561814140
159	PML9	492.077	Flag	PML9	PML9	\N	\N	\N	010100000011B703A87B9A4040F346E8926A814140
160	PMR1	498.56599999999997	Flag	PMR1	PMR1	\N	\N	\N	0101000000DED0F40FAB9A40407BAC9F52F3804140
161	PMR3	486.54899999999998	Flag	PMR3	PMR3	\N	\N	\N	010100000078A54B509A9A40406E3DB78EF9804140
162	PMR5	481.262	Flag	PMR5	PMR5	\N	\N	\N	0101000000667A8C699B9A404070325BC60E814140
163	PMR7	481.262	Flag	PMR7	PMR7	\N	\N	\N	0101000000D9B1F079AE9A40407446275A18814140
164	PMR9	486.30900000000003	Flag	PMR9	PMR9	\N	\N	\N	0101000000E81D58A2B49A404024279CA828814140
165	PT1	413.97000000000003	Flag	PT1	PT1	\N	\N	\N	0101000000A60EDBDE3A9F404037B69767B7824140
166	PY1	267.61099999999999	Flag	PY1	PY1	\N	\N	\N	01010000001E075B75E8AD404012BA6737DC814140
167	PY2	254.393	Flag	PY2	PY2	\N	\N	\N	010100000095F0CB338BAF4040206AD3BF41824140
168	PY3	257.75700000000001	Flag	PY3	PY3	\N	\N	\N	0101000000F46F9CB674AF40407146373CE9814140
169	PY4	261.84300000000002	Flag	PY4	PY4	\N	\N	\N	0101000000352BBF324DAF4040CB7C1469B3814140
170	ROCK1	363.98200000000003	Flag	ROCK1	ROCK1	\N	\N	\N	01010000009BCA3F8650AB4040E568C39E0C7F4140
171	ROCK2	289.48000000000002	Flag	ROCK2	ROCK2	\N	\N	\N	01010000006430D78630AB404095C4BFF37E7F4140
172	SA1	330.577	Flag	SA1	SA1	\N	\N	\N	01010000009FE0742849AF4040651C7420A37A4140
173	SA2	336.34399999999999	Flag	SA2	SA2	\N	\N	\N	01010000005C3E50764CAF4040C07AC062AB7A4140
174	SA3	332.01900000000001	Flag	SA3	SA3	\N	\N	\N	0101000000089F13664DAF40401A45FF99BB7A4140
175	SAL	583.88199999999995	Flag	SAL	SAL	\N	\N	\N	0101000000F0E613F398314040EDF420900D7C4140
\.


--
-- Data for Name: geometry_columns; Type: TABLE DATA; Schema: public; Owner: aps03pwb
--

COPY geometry_columns (f_table_catalog, f_table_schema, f_table_name, f_geometry_column, coord_dimension, srid, "type") FROM stdin;
	public	gps2	the_geom	2	-1	POINT
	public	gps3	the_geom	2	-1	POINT
	public	tblgpsmaster	the_geom	2	-1	POINT
	public	pik	the_geom	2	-1	POINT
	public	download2	the_geom	2	-1	POINT
	public	download3	the_geom	2	-1	POINT
	public	download4	the_geom	2	-1	POINT
	public	download5	the_geom	2	-1	POINT
	public	tblgpsdata	the_geom	2	-1	POINT
	public	VENTURE	the_geom	2	-1	POINT
	public	brita	the_geom	2	-1	POINT
	public	brita2	the_geom	2	-1	POINT
	public	sturtaug08	the_geom	2	-1	POINT
\.


--
-- Data for Name: spatial_ref_sys; Type: TABLE DATA; Schema: public; Owner: aps03pwb
--

COPY spatial_ref_sys (srid, auth_name, auth_srid, srtext, proj4text) FROM stdin;
\.


--
-- Data for Name: sturtaug08; Type: TABLE DATA; Schema: public; Owner: webuser
--

COPY sturtaug08 (gid, name, elevation, symbol, "comment", descriptio, source, url, "url name", the_geom) FROM stdin;
0	001	0	Waypoint	001	001	\N	\N	\N	01010000004BA633C556724040D251FF3AA6764140
1	002	1568.27	Waypoint	002	002	\N	\N	\N	0101000000BB3A532A59724040CC02D471C8764140
2	003	136.87200000000001	Waypoint	003	003	\N	\N	\N	01010000006A595CBB2BAC40400BA52CCC0D614140
3	004	121.011	Waypoint	004	004	\N	\N	\N	01010000001BED376122AC40401ED24797F9604140
4	005	628.10299999999995	Waypoint	005	005	\N	\N	\N	0101000000A94133B05785414095C9276668CB3F40
5	006	434.87900000000002	Waypoint	006	006	\N	\N	\N	01010000001FA66B904381414065B66B0FA9614040
6	ALAMSCV GP	298.613	Waypoint	ALAMSCV GP	ALAMSCV GP	\N	\N	\N	0101000000DC3287745DB34040F52B470F567D4140
7	CANG50	1590.6199999999999	Waypoint	CANG50	CANG50	\N	\N	\N	0101000000CFE69E99D3163840D80EFF1608A24140
8	CANG51	1589.6500000000001	Waypoint	CANG51	CANG51	\N	\N	\N	01010000009DC0AE05C1163840625223C4FAA14140
9	CANG52	1583.4000000000001	Waypoint	CANG52	CANG52	\N	\N	\N	0101000000EC0B79E8611638401BE328180CA24140
10	CANG54	1578.8399999999999	Waypoint	CANG54	CANG54	\N	\N	\N	01010000004BA5C8EF2717384013ADFB180AA24140
11	CANG56	1595.4200000000001	Waypoint	CANG56	CANG56	\N	\N	\N	0101000000C119699A911738404F0E97031AA24140
12	CANG57	1574.51	Waypoint	CANG57	CANG57	\N	\N	\N	0101000000F4C2517D5D1738408D9337CAFEA14140
13	CHA 1	391.13900000000001	Waypoint	CHA 1	CHA 1	\N	\N	\N	01010000009303AB2789824140E62683068F5D4040
14	CHA 10	368.30799999999999	Waypoint	CHA 10	CHA 10	\N	\N	\N	010100000058E727BB6A824140984B33A8795D4040
15	CHA 2	365.66399999999999	Waypoint	CHA 2	CHA 2	\N	\N	\N	0101000000AAECD77767824140D5636FEA825D4040
16	CHA 3	374.07600000000002	Waypoint	CHA 3	CHA 3	\N	\N	\N	010100000051F4B6556D8241402B60FBFC895D4040
17	CHA 4	399.791	Waypoint	CHA 4	CHA 4	\N	\N	\N	0101000000B1425FB466824140C05368238E5D4040
18	CHA 5	380.084	Waypoint	CHA 5	CHA 5	\N	\N	\N	0101000000D4B04F68688241406C7A4B97905D4040
19	CHA10	364.46300000000002	Waypoint	CHA10	CHA10	\N	\N	\N	01010000006BC1880674824140579DC8D8785D4040
20	CHA12	473.33100000000002	Waypoint	CHA12	CHA12	\N	\N	\N	0101000000FD76D7AE10844140DA47375AFF5D4040
21	CHA13	471.40899999999999	Waypoint	CHA13	CHA13	\N	\N	\N	0101000000826AF3670E844140F4C5137EFF5D4040
22	CHA14	475.49400000000003	Waypoint	CHA14	CHA14	\N	\N	\N	0101000000CB4E8B361184414017CEB44BFB5D4040
23	CHA6	368.06799999999998	Waypoint	CHA6	CHA6	\N	\N	\N	0101000000E00E5D1B6D824140D3E130B78C5D4040
24	CHA7	359.65600000000001	Waypoint	CHA7	CHA7	\N	\N	\N	0101000000F2FF3B866D8241408B057415815D4040
25	CHA8	358.69499999999999	Waypoint	CHA8	CHA8	\N	\N	\N	01010000005157C0026D824140BD2A877E7C5D4040
26	CHA9	357.97399999999999	Waypoint	CHA9	CHA9	\N	\N	\N	01010000004BE1EBB2728241401CA2580E775D4040
27	CSM2	1317.5999999999999	Waypoint	CSM2	CSM2	\N	\N	\N	0101000000207CE6B440EC3740655F1CD680A84140
28	CSM4	1451.22	Waypoint	CSM4	CSM4	\N	\N	\N	01010000000F1C020D57ED3740B1E51B257DA84140
29	CSM6	1422.8699999999999	Waypoint	CSM6	CSM6	\N	\N	\N	01010000004AC459E634ED3740F90C94537CA84140
30	CSM7	1432.72	Waypoint	CSM7	CSM7	\N	\N	\N	01010000001C564E0104ED3740091B87C273A84140
31	CTC CO1	1726.8800000000001	Waypoint	CTC CO1	CTC CO1	\N	\N	\N	010100000082696C3D307040408BC14CA60D764140
32	CTC JEN	1638.4400000000001	Waypoint	CTC JEN	CTC JEN	\N	\N	\N	0101000000AA50D0327A724040D29ADF6342774140
33	CTC100	1853.77	Waypoint	CTC100	CTC100	\N	\N	\N	0101000000810269AB046E40402991C86747774140
34	CTC101	1853.77	Waypoint	CTC101	CTC101	\N	\N	\N	01010000001C376CC3F76D40409705BF7F46774140
35	CTC102	1853.77	Waypoint	CTC102	CTC102	\N	\N	\N	0101000000EF0CED78E06D4040BC1FF4C256774140
36	CTC12	788.64099999999996	Waypoint	CTC12	CTC12	\N	\N	\N	01010000001622B791F85240404BF7A08B397A4140
37	CTC13	812.43399999999997	Waypoint	CTC13	CTC13	\N	\N	\N	01010000005A24881BF95240402BF470EB367A4140
38	CTC130	1806.4300000000001	Waypoint	CTC130	CTC130	\N	\N	\N	0101000000FC0700AA126F404088014DAF4D784140
39	CTC131	1777.8299999999999	Waypoint	CTC131	CTC131	\N	\N	\N	010100000079F150A4136F4040770435514B784140
40	CTC132	1777.8299999999999	Waypoint	CTC132	CTC132	\N	\N	\N	010100000026AF2432D97440409E0607D9C4844140
41	CTC133	1792.49	Waypoint	CTC133	CTC133	\N	\N	\N	0101000000D0B53459116F4040590ABD0857784140
42	CTC135	1816.28	Waypoint	CTC135	CTC135	\N	\N	\N	01010000004C3943970E6F40403AEF3E4848784140
43	CTC136	1793.45	Waypoint	CTC136	CTC136	\N	\N	\N	010100000094F26CEC186F4040C8D8E0AF4F784140
44	CTC137	1809.55	Waypoint	CTC137	CTC137	\N	\N	\N	0101000000CE8C53B4FF6E4040807F87F840784140
45	CTC138	1810.03	Waypoint	CTC138	CTC138	\N	\N	\N	010100000050EC78FEFA6E4040AFCEC4834C784140
46	CTC139	1825.9000000000001	Waypoint	CTC139	CTC139	\N	\N	\N	010100000018394325F96E404029BEDC5247784140
47	CTC14	807.86699999999996	Waypoint	CTC14	CTC14	\N	\N	\N	010100000015EE6CFAF952404095AC6C1A397A4140
48	CTC140	1793.45	Waypoint	CTC140	CTC140	\N	\N	\N	010100000065E2C896F46E40400EDAB04841784140
49	CTC141	1840.8	Waypoint	CTC141	CTC141	\N	\N	\N	01010000005C678319016F404078F8B488FD774140
50	CTC142	1828.0599999999999	Waypoint	CTC142	CTC142	\N	\N	\N	0101000000F7076CFCF96E40403DADD0771F784140
51	CTC143	1842.24	Waypoint	CTC143	CTC143	\N	\N	\N	0101000000DE0F080C036F40402604E0DCF5774140
52	CTC144	1822.77	Waypoint	CTC144	CTC144	\N	\N	\N	0101000000367DF7400B6F40404B17DCBB38784140
53	CTC145	1830.46	Waypoint	CTC145	CTC145	\N	\N	\N	0101000000DAEDA6A00A6F4040C0688C5F03784140
54	CTC146	1830.46	Waypoint	CTC146	CTC146	\N	\N	\N	01010000009072BB8D096F4040F4C1DC35FB774140
55	CTC147	1841.52	Waypoint	CTC147	CTC147	\N	\N	\N	0101000000700FDF15036F404087D8006FF8774140
56	CTC15	1756.9200000000001	Waypoint	CTC15	CTC15	\N	\N	\N	0101000000FD00C745456F40404ADC7C8664784140
57	CTC150	1725.2	Waypoint	CTC150	CTC150	\N	\N	\N	010100000016A65CD23270404027EE50CB11764140
58	CTC151	1717.75	Waypoint	CTC151	CTC151	\N	\N	\N	01010000003D03CC770E70404056F01874FB754140
59	CTC152	1735.77	Waypoint	CTC152	CTC152	\N	\N	\N	0101000000AA411C142270404077135BC50B764140
60	CTC153	1720.3900000000001	Waypoint	CTC153	CTC153	\N	\N	\N	010100000095FB106000704040230B899500764140
61	CTC154	1711.74	Waypoint	CTC154	CTC154	\N	\N	\N	01010000003AD548B5097040404FD138A4F6754140
62	CTC155	1724.72	Waypoint	CTC155	CTC155	\N	\N	\N	01010000002EB2F8A81A704040BD92C8640F764140
63	CTC156	1715.3499999999999	Waypoint	CTC156	CTC156	\N	\N	\N	0101000000CA28DB2FFB6F40404F2EAC6CF6754140
64	CTC157	1715.3499999999999	Waypoint	CTC157	CTC157	\N	\N	\N	0101000000E763B32D077040400382E0C1F4754140
65	CTC158	1761.97	Waypoint	CTC158	CTC158	\N	\N	\N	0101000000D52F182902704040744328F61D764140
66	CTC159	1717.51	Waypoint	CTC159	CTC159	\N	\N	\N	01010000003FE2940FFA6F4040302B7CCCF3754140
67	CTC16	1755.24	Waypoint	CTC16	CTC16	\N	\N	\N	01010000001AEEAAA7596F404096253F8466784140
68	CTC160	1708.6199999999999	Waypoint	CTC160	CTC160	\N	\N	\N	010100000019F27252047040408ECEBCC6F5754140
69	CTC161	1748.03	Waypoint	CTC161	CTC161	\N	\N	\N	01010000008DB4085009704040359A3B7E18764140
70	CTC162	1768.22	Waypoint	CTC162	CTC162	\N	\N	\N	01010000006469173BF06F40406F35FF7016764140
71	CTC163	1702.6099999999999	Waypoint	CTC163	CTC163	\N	\N	\N	0101000000CA3DCC7A0B70404073CDA07EF0754140
72	CTC165	1787.4400000000001	Waypoint	CTC165	CTC165	\N	\N	\N	01010000008E9897CBDF6F4040562E1C5417764140
73	CTC166	1705.97	Waypoint	CTC166	CTC166	\N	\N	\N	0101000000D0EB246217704040EFBC9487F3754140
74	CTC167	1748.03	Waypoint	CTC167	CTC167	\N	\N	\N	0101000000B88437FCD26F404005B1647508764140
75	CTC168	1806.4300000000001	Waypoint	CTC168	CTC168	\N	\N	\N	01010000003F7F448DAE6F40406DB51C220D764140
76	CTC169	1775.6700000000001	Waypoint	CTC169	CTC169	\N	\N	\N	01010000005CDC0C58EE6F40406443B3C21F764140
77	CTC17	1749.71	Waypoint	CTC17	CTC17	\N	\N	\N	0101000000E37AEB2E566F4040E650DF6260784140
78	CTC170	1775.4300000000001	Waypoint	CTC170	CTC170	\N	\N	\N	0101000000C40B5744E36F4040ED03984B0D764140
79	CTC171	1848.01	Waypoint	CTC171	CTC171	\N	\N	\N	0101000000DD5ED7D1E66D404056CB386E3A774140
80	CTC172	1832.3800000000001	Waypoint	CTC172	CTC172	\N	\N	\N	01010000004377EB2AD46D40403380DB063A774140
81	CTC173	1775.4300000000001	Waypoint	CTC173	CTC173	\N	\N	\N	01010000007106D006D86D4040A7B19C084B774140
82	CTC174	1870.3599999999999	Waypoint	CTC174	CTC174	\N	\N	\N	010100000013923786E16D40402001857541774140
83	CTC175	1850.1700000000001	Waypoint	CTC175	CTC175	\N	\N	\N	01010000007D1DE71FC56D40406FAF883E38774140
84	CTC176	1775.4300000000001	Waypoint	CTC176	CTC176	\N	\N	\N	01010000007106D006D86D4040A7B19C084B774140
85	CTC177	1852.3299999999999	Waypoint	CTC177	CTC177	\N	\N	\N	01010000001F44DCE3DF6D404093FACE0943774140
86	CTC178	1841.04	Waypoint	CTC178	CTC178	\N	\N	\N	0101000000AFCE2C67C16D40401360BB2838774140
87	CTC179	1859.3	Waypoint	CTC179	CTC179	\N	\N	\N	0101000000E265A4E3CC6D4040AAEDB4A23E774140
88	CTC18	1754.04	Waypoint	CTC18	CTC18	\N	\N	\N	010100000071C1005F5D6F4040E5FFA48D62784140
89	CTC180	1867.71	Waypoint	CTC180	CTC180	\N	\N	\N	0101000000292BF08FE36D40403D071F1542774140
90	CTC181	1837.9100000000001	Waypoint	CTC181	CTC181	\N	\N	\N	0101000000EFF61E03B96D4040D31F7B6C34774140
91	CTC182	1859.54	Waypoint	CTC182	CTC182	\N	\N	\N	010100000023932B73B56D4040466E50723C774140
92	CTC183	1870.3599999999999	Waypoint	CTC183	CTC183	\N	\N	\N	0101000000E9704CCDE06D4040C708645347774140
93	CTC184	1848.97	Waypoint	CTC184	CTC184	\N	\N	\N	01010000005FD5B4E6226E40400771CC4F3D774140
94	CTC185	1851.3699999999999	Waypoint	CTC185	CTC185	\N	\N	\N	0101000000B4BBD0F4436E404011B8B0993C774140
95	CTC186	1867.95	Waypoint	CTC186	CTC186	\N	\N	\N	01010000003C6AC396DB6D4040757D179D49774140
96	CTC19	1754.28	Waypoint	CTC19	CTC19	\N	\N	\N	01010000004B0540D55B6F4040E23AB7EA69784140
97	CTC20	1761.73	Waypoint	CTC20	CTC20	\N	\N	\N	01010000000F891091396F40404885ACCC66784140
98	CTC2007 10	1689.6300000000001	Waypoint	CTC2007 10	CTC2007 10	\N	\N	\N	01010000003E0724CE7B7240403DE05480FC774140
99	CTC2007 11	1684.8199999999999	Waypoint	CTC2007 11	CTC2007 11	\N	\N	\N	010100000040BEA05F7D724040753EFFE7F7774140
100	CTC2007 12	1679.0599999999999	Waypoint	CTC2007 12	CTC2007 12	\N	\N	\N	0101000000085184F57B7240407FD634A3EA774140
101	CTC2007 9	1697.8	Waypoint	CTC2007 9	CTC2007 9	\N	\N	\N	0101000000B68B57C37F7240402E631F7103784140
102	CTC21	1756.9200000000001	Waypoint	CTC21	CTC21	\N	\N	\N	0101000000B90C8B93336F404028EB28E865784140
103	CTC22	1772.54	Waypoint	CTC22	CTC22	\N	\N	\N	010100000095732C66396F4040E68BC0CF6D784140
104	CTC23	1799.22	Waypoint	CTC23	CTC23	\N	\N	\N	0101000000E76DA420436F4040B5C3FC687A784140
105	CTC24	1789.8499999999999	Waypoint	CTC24	CTC24	\N	\N	\N	0101000000D3C54824416F4040C1A1103180784140
106	CTC25	1787.2	Waypoint	CTC25	CTC25	\N	\N	\N	0101000000775CC4DF456F40403C110B4F84784140
107	CTC26	1789.1300000000001	Waypoint	CTC26	CTC26	\N	\N	\N	010100000027CD30FF326F4040CD6234BB86784140
108	CTC27	1782.6400000000001	Waypoint	CTC27	CTC27	\N	\N	\N	0101000000E695F0262D6F40409549AFFB7E784140
109	CTC28	1785.04	Waypoint	CTC28	CTC28	\N	\N	\N	0101000000BC05E793226F40404156DB4086784140
110	CTC29	1784.0799999999999	Waypoint	CTC29	CTC29	\N	\N	\N	0101000000ADD6BCBC166F40405E8B34F687784140
111	CTC30	1798.74	Waypoint	CTC30	CTC30	\N	\N	\N	010100000090892C83FE6E4040A11C8BC191784140
112	CTC300	1800.1800000000001	Waypoint	CTC300	CTC300	\N	\N	\N	01010000008E6604D2406F40409EA6E03A10784140
113	CTC302	1699.72	Waypoint	CTC302	CTC302	\N	\N	\N	0101000000DBFC2C87376E4040B876AC96D7794140
114	CTC304	1726.4000000000001	Waypoint	CTC304	CTC304	\N	\N	\N	01010000007A7CA39F2E6E4040BED78F9BC1794140
115	CTC306	1734.8099999999999	Waypoint	CTC306	CTC306	\N	\N	\N	0101000000AE4F577C256E40401A35DBECBA794140
116	CTC31	1804.27	Waypoint	CTC31	CTC31	\N	\N	\N	01010000008D4F70CDF76E40406F71DB5E95784140
117	CTC310	1751.6300000000001	Waypoint	CTC310	CTC310	\N	\N	\N	0101000000AAAF6FA6106E4040D4535867C4794140
118	CTC311	1621.8599999999999	Waypoint	CTC311	CTC311	\N	\N	\N	0101000000772A18498671404096984BD03D764140
119	CTC313	1622.8199999999999	Waypoint	CTC313	CTC313	\N	\N	\N	0101000000F710B0FD7472404074D14463A3764140
120	CTC315	1622.8199999999999	Waypoint	CTC315	CTC315	\N	\N	\N	0101000000AA9B8BBF6D7240409E06443C9D764140
121	CTC317	1629.3099999999999	Waypoint	CTC317	CTC317	\N	\N	\N	0101000000F5E2087578724040516B53A290764140
122	CTC318	1630.27	Waypoint	CTC318	CTC318	\N	\N	\N	010100000043FE2233DA724040C064FC5E77764140
123	CTC319	1612.96	Waypoint	CTC319	CTC319	\N	\N	\N	01010000004FD313088E724040C907D5B7A0764140
124	CTC320	1628.8299999999999	Waypoint	CTC320	CTC320	\N	\N	\N	0101000000B466D32DD57240403F37B89D8B764140
125	CTC322	1629.3099999999999	Waypoint	CTC322	CTC322	\N	\N	\N	01010000006490604FA0724040DB16231A76764140
126	CTC324	1628.1099999999999	Waypoint	CTC324	CTC324	\N	\N	\N	01010000005BF1AA5A94724040D8D7C4467C764140
127	CTC326	1618.97	Waypoint	CTC326	CTC326	\N	\N	\N	0101000000868B0FC57A72404036D11C965C764140
128	CTC328	1619.9300000000001	Waypoint	CTC328	CTC328	\N	\N	\N	0101000000990D9F657772404031321F1561764140
129	CTC330	1623.0599999999999	Waypoint	CTC330	CTC330	\N	\N	\N	01010000008C32E8726D724040FA419FC062764140
130	CTC332	1611.76	Waypoint	CTC332	CTC332	\N	\N	\N	01010000006365946466724040FCD32C2176764140
131	CTC334	1623.0599999999999	Waypoint	CTC334	CTC334	\N	\N	\N	01010000008FDCAC5797724040D45337599B764140
132	CTC336	1617.77	Waypoint	CTC336	CTC336	\N	\N	\N	010100000067823C1676724040FC752303AB764140
133	CTC338	1567.54	Waypoint	CTC338	CTC338	\N	\N	\N	0101000000813D63B572724040CF95E08BB1764140
134	CTC340	1604.3099999999999	Waypoint	CTC340	CTC340	\N	\N	\N	010100000016FFEE52677240400E9364AEB0764140
135	CTC342	1584.1300000000001	Waypoint	CTC342	CTC342	\N	\N	\N	0101000000920E0CA96D7240405F47C3F9B9764140
136	CTC344	1579.5599999999999	Waypoint	CTC344	CTC344	\N	\N	\N	010100000080B4A44863724040DFB58B36B5764140
137	CTC346	1602.8699999999999	Waypoint	CTC346	CTC346	\N	\N	\N	01010000004BE13741B87240403B3BDCB84C764140
138	CTC51	1583.8900000000001	Waypoint	CTC51	CTC51	\N	\N	\N	0101000000F53834AE7D71404089A980BD3A764140
139	CTC52	1583.4000000000001	Waypoint	CTC52	CTC52	\N	\N	\N	01010000004E17ABCB7E714040AC88F85935764140
140	CTC54	1583.8900000000001	Waypoint	CTC54	CTC54	\N	\N	\N	01010000004DC8B8F646724040124504C0D1764140
141	CTC55A	1545.9100000000001	Waypoint	CTC55A	CTC55A	\N	\N	\N	010100000021CA84503F7240403884046ED8764140
142	CTC55B	1549.04	Waypoint	CTC55B	CTC55B	\N	\N	\N	01010000004834F3773E724040699503B7DA764140
143	CTC56	1574.03	Waypoint	CTC56	CTC56	\N	\N	\N	0101000000EFC584896072404063228C3CDC764140
144	CTC57	1549.04	Waypoint	CTC57	CTC57	\N	\N	\N	0101000000FDE8B0CF57724040B687E8D0DC764140
145	CTC58	1612.48	Waypoint	CTC58	CTC58	\N	\N	\N	0101000000BBD9BCFD3B724040DC64BCFCFA764140
146	CTC59	1645.1700000000001	Waypoint	CTC59	CTC59	\N	\N	\N	0101000000E072F7643E724040D16DB7941B774140
147	CTC60 JDW	1642.77	Waypoint	CTC60 JDW	CTC60 JDW	\N	\N	\N	010100000061DB1C9337724040C57B8FAC1C774140
148	CTC61	1563.9400000000001	Waypoint	CTC61	CTC61	\N	\N	\N	010100000021B67030467240402A9AFF3EC0764140
149	CTC62	1564.1800000000001	Waypoint	CTC62	CTC62	\N	\N	\N	01010000009076FFFF4F7240404F679BD9B6764140
150	CTC63	1586.77	Waypoint	CTC63	CTC63	\N	\N	\N	010100000005EDD87F527240405A6B9FC3B9764140
151	CTC64	1589.8900000000001	Waypoint	CTC64	CTC64	\N	\N	\N	0101000000512B637B6172404049B4A0D4B7764140
152	CTC65	1617.53	Waypoint	CTC65	CTC65	\N	\N	\N	010100000064B12C1C71724040E907A882A7764140
153	CTC67	423.58300000000003	Waypoint	CTC67	CTC67	\N	\N	\N	010100000015999041347140402B0C7836F7754140
154	CTC68A	1620.1800000000001	Waypoint	CTC68A	CTC68A	\N	\N	\N	01010000009974BC4B67714040BFEBF402FA754140
155	CTC68B	1622.0999999999999	Waypoint	CTC68B	CTC68B	\N	\N	\N	010100000078BD48296771404057C14C34F8754140
156	CTC69	1594.9400000000001	Waypoint	CTC69	CTC69	\N	\N	\N	0101000000008DACDE5C714040ECE028C503764140
157	CTC70	1619.21	Waypoint	CTC70	CTC70	\N	\N	\N	01010000005B3EB3A04771404058243F4502764140
158	CTC71	1596.3800000000001	Waypoint	CTC71	CTC71	\N	\N	\N	010100000073164CB57C71404063337B31F8754140
159	CTC72	1619.21	Waypoint	CTC72	CTC72	\N	\N	\N	0101000000CD922BB551714040D427476EFE754140
160	CTC73	1603.1099999999999	Waypoint	CTC73	CTC73	\N	\N	\N	0101000000208E5CD47A714040AC376013F6754140
161	CTC80	1842.24	Waypoint	CTC80	CTC80	\N	\N	\N	0101000000AF2B54A17B6F404024F73CCBE8764140
162	CTC81A	1850.4100000000001	Waypoint	CTC81A	CTC81A	\N	\N	\N	01010000000FA0C85B7A6F40402C58D843E3764140
163	CTC81B	1858.3399999999999	Waypoint	CTC81B	CTC81B	\N	\N	\N	0101000000EC4EC8E5776F404018769CCBE2764140
164	CTC82	1850.8900000000001	Waypoint	CTC82	CTC82	\N	\N	\N	0101000000C6B2546F716F40406D4C5C82E4764140
165	CTC83	1831.9000000000001	Waypoint	CTC83	CTC83	\N	\N	\N	010100000047082C0C936F404017C25849E5764140
166	CTC84	1837.6700000000001	Waypoint	CTC84	CTC84	\N	\N	\N	0101000000A73264F3736F4040ECF42801D6764140
167	CTC85	1839.5899999999999	Waypoint	CTC85	CTC85	\N	\N	\N	0101000000563F48AC8B6F4040F883473CD5764140
168	CTC86	1856.4200000000001	Waypoint	CTC86	CTC86	\N	\N	\N	010100000008BFDC40646E4040D360DB214C774140
169	CTC87	1867.95	Waypoint	CTC87	CTC87	\N	\N	\N	0101000000CB1D84200A6E404061FEE4D148774140
170	CTC88	1867.23	Waypoint	CTC88	CTC88	\N	\N	\N	01010000007FA060EF136E40404F47F30C49774140
171	CTC89	1861.46	Waypoint	CTC89	CTC89	\N	\N	\N	0101000000921914720A6E40404255CB244A774140
172	CTC90	1868.6700000000001	Waypoint	CTC90	CTC90	\N	\N	\N	01010000008C15C8FB176E4040A463CC324B774140
173	CTC91	1869.3900000000001	Waypoint	CTC91	CTC91	\N	\N	\N	0101000000690105D70F6E4040F075E7C352774140
174	CTC92	1862.4300000000001	Waypoint	CTC92	CTC92	\N	\N	\N	0101000000A97E6B48186E4040BD640C4148774140
175	CTC93	1869.1500000000001	Waypoint	CTC93	CTC93	\N	\N	\N	010100000021005904166E4040722748D45A774140
176	CTC95	1885.02	Waypoint	CTC95	CTC95	\N	\N	\N	0101000000C83FBC792D6E4040E88EAF5157774140
177	CTR1	1055.1600000000001	Waypoint	CTR1	CTR1	\N	\N	\N	01010000009AAAAE55982B38403D2A8C9A20A74140
178	CTR10	1107.0799999999999	Waypoint	CTR10	CTR10	\N	\N	\N	0101000000D4A45891782B38407ECCD41215A74140
179	CTR12	1125.5799999999999	Waypoint	CTR12	CTR12	\N	\N	\N	0101000000521F67EB772B38409C3D775204A74140
180	CTR16	1157.79	Waypoint	CTR16	CTR16	\N	\N	\N	0101000000714C3F76512B38404412678CE9A64140
181	CTR17	1080.4000000000001	Waypoint	CTR17	CTR17	\N	\N	\N	010100000085A1E0F2702B38405E72D4B814A74140
182	CTR2	1071.75	Waypoint	CTR2	CTR2	\N	\N	\N	0101000000BD5EB8789A2B384091C9C8AA1FA74140
183	CTR3	1068.1400000000001	Waypoint	CTR3	CTR3	\N	\N	\N	01010000007C2AB1AF982B3840649A90A61FA74140
184	CTR4	1065.5	Waypoint	CTR4	CTR4	\N	\N	\N	0101000000EEDABF618B2B3840E3885FF81BA74140
185	CTR5	1073.1900000000001	Waypoint	CTR5	CTR5	\N	\N	\N	01010000009AC6D804762B3840C6394F141FA74140
186	CTR8	1095.54	Waypoint	CTR8	CTR8	\N	\N	\N	0101000000C2ED66CC782B384011A6BBFA17A74140
187	CVP20	1346.4400000000001	Waypoint	CVP20	CVP20	\N	\N	\N	0101000000ED8E69C6521B3840D24FBCF3D9A04140
188	CVP22	1337.55	Waypoint	CVP22	CVP22	\N	\N	\N	0101000000F7AF81B44C1B3840594B8F13D7A04140
189	CVP24	1328.9000000000001	Waypoint	CVP24	CVP24	\N	\N	\N	01010000009ED789DF551B384041558707CDA04140
190	CVP26	1330.5799999999999	Waypoint	CVP26	CVP26	\N	\N	\N	010100000034143F126B1B38406822FFDBCBA04140
191	CVP30	1337.79	Waypoint	CVP30	CVP30	\N	\N	\N	01010000009DACE1BA131B384031C2D4C9EBA04140
192	CVP31	1348.6099999999999	Waypoint	CVP31	CVP31	\N	\N	\N	01010000006E7938C3851B3840541454F9C3A04140
193	CVP32	1334.6700000000001	Waypoint	CVP32	CVP32	\N	\N	\N	0101000000A7C73FFE151B3840955BF028E9A04140
194	CVP34	1331.78	Waypoint	CVP34	CVP34	\N	\N	\N	01010000000A4D473D1A1B38406C1AB85CEDA04140
195	CYP 1SPOT	1090.49	Waypoint	CYP 1SPOT	CYP 1SPOT	\N	\N	\N	01010000006F4156BA321B3840B0000937979F4140
196	DST11	1402.2	Waypoint	DST11	DST11	\N	\N	\N	010100000063F3340A907B4040E91748F7437A4140
197	DST13	1414.9300000000001	Waypoint	DST13	DST13	\N	\N	\N	0101000000B293A3FB977B4040B9B1B012397A4140
198	DST17	1409.4100000000001	Waypoint	DST17	DST17	\N	\N	\N	010100000086B003AFA27B4040C80B60FF327A4140
199	DST2	1357.02	Waypoint	DST2	DST2	\N	\N	\N	01010000005DFAC2244F7B404074FFBE6A587A4140
200	DST23	1398.5899999999999	Waypoint	DST23	DST23	\N	\N	\N	0101000000AC306412C17B4040A2BFA7A4547A4140
201	DST25	1383.45	Waypoint	DST25	DST25	\N	\N	\N	0101000000F4361074EF7B404008B3CCCC537A4140
202	DST26	1423.1099999999999	Waypoint	DST26	DST26	\N	\N	\N	0101000000A5134BC2EC7B404053EBD77F587A4140
203	DST29	1431.04	Waypoint	DST29	DST29	\N	\N	\N	01010000009964DF73F27B4040559978F3537A4140
204	DST3	1347.4000000000001	Waypoint	DST3	DST3	\N	\N	\N	010100000009442CBB507B404044423306587A4140
205	DST32	1369.27	Waypoint	DST32	DST32	\N	\N	\N	010100000025C96452727B4040639C5726507A4140
206	DST5	1368.79	Waypoint	DST5	DST5	\N	\N	\N	01010000002568B7895F7B4040BD911B734E7A4140
207	DST7	1388.98	Waypoint	DST7	DST7	\N	\N	\N	010100000031D62094837B40405738A764497A4140
208	GUL ROAD	1398.8299999999999	Waypoint	GUL ROAD	GUL ROAD	\N	\N	\N	0101000000506BB11392193840E5B15CF21CA14140
209	HAGANA	1398.8299999999999	Waypoint	HAGANA	HAGANA	\N	\N	\N	0101000000F2F536AF0A844140C2341B4AFE5D4040
210	ICHA11	482.464	Waypoint	ICHA11	ICHA11	\N	\N	\N	01010000001DF047F1F48341403B517FAE175E4040
211	ICHA12	481.02199999999999	Waypoint	ICHA12	ICHA12	\N	\N	\N	0101000000B979BFDEF883414096B228C61B5E4040
212	ICT 20	405.07799999999997	Waypoint	ICT 20	ICT 20	\N	\N	\N	010100000094E764237C814140B27B04948C614040
213	ICT10	395.22500000000002	Waypoint	ICT10	ICT10	\N	\N	\N	0101000000C6765C2C03814140731378D19E614040
214	ICT11	396.90699999999998	Waypoint	ICT11	ICT11	\N	\N	\N	01010000001AA2B47E0D814140423F9F3DA1614040
215	ICT12	417.57499999999999	Waypoint	ICT12	ICT12	\N	\N	\N	0101000000F10E88C150814140EB315CE09C614040
216	ICT13	416.61399999999998	Waypoint	ICT13	ICT13	\N	\N	\N	0101000000DECC7BAB54814140DF160BC79C614040
217	ICT14	404.35700000000003	Waypoint	ICT14	ICT14	\N	\N	\N	0101000000F291C7E555814140A829E8479A614040
218	ICT15	417.09500000000003	Waypoint	ICT15	ICT15	\N	\N	\N	010100000048B41F54628141403BCF84989E614040
219	ICT16	420.21899999999999	Waypoint	ICT16	ICT16	\N	\N	\N	010100000029511F16648141402ED4809299614040
220	ICT17	383.92899999999997	Waypoint	ICT17	ICT17	\N	\N	\N	0101000000A00F574D5E814140C2F380E99C614040
221	ICT18	402.19400000000002	Waypoint	ICT18	ICT18	\N	\N	\N	01010000009BD9F85D628141405062CB8398614040
222	ICT19	416.85399999999998	Waypoint	ICT19	ICT19	\N	\N	\N	0101000000D381DF987B81414005CD4C0894614040
223	ICT9	384.17000000000002	Waypoint	ICT9	ICT9	\N	\N	\N	010100000091FA9CCF018141408EEEC7BD9E614040
224	IJF1	563.69399999999996	Waypoint	IJF1	IJF1	\N	\N	\N	01010000003DC35F89438541402BCB5E56ABC93F40
225	IMR 2	633.63	Waypoint	IMR 2	IMR 2	\N	\N	\N	010100000069D5A74F44854140C53931BE44CB3F40
226	IMR 3	635.31200000000001	Waypoint	IMR 3	IMR 3	\N	\N	\N	01010000000790FFD44885414039647FB03ECB3F40
227	IMR 4	632.18799999999999	Waypoint	IMR 4	IMR 4	\N	\N	\N	01010000003ED5F3FE578541407C6A976454CB3F40
228	IMR 5	632.90899999999999	Waypoint	IMR 5	IMR 5	\N	\N	\N	01010000000CA7047856854140F0B2C95B5ACB3F40
229	IMR1	639.87800000000004	Waypoint	IMR1	IMR1	\N	\N	\N	0101000000645054D341854140C22208553FCB3F40
230	IMR10	639.87800000000004	Waypoint	IMR10	IMR10	\N	\N	\N	0101000000DBCCAC9B4E854140EBC98F0741CB3F40
231	IMR11	661.26800000000003	Waypoint	IMR11	IMR11	\N	\N	\N	0101000000862238895C854140D287EF432CCB3F40
232	IMR12	661.26800000000003	Waypoint	IMR12	IMR12	\N	\N	\N	01010000007E43FF095085414074B040EC33CB3F40
233	IMR13	638.91700000000003	Waypoint	IMR13	IMR13	\N	\N	\N	010100000091FD12425E85414022F4EFD73DCB3F40
234	IMR14	623.05499999999995	Waypoint	IMR14	IMR14	\N	\N	\N	01010000004A0959C785854140E360972A16CB3F40
235	IMR6	635.55200000000002	Waypoint	IMR6	IMR6	\N	\N	\N	01010000002CE414945785414055CF001965CB3F40
236	IMR7	617.04700000000003	Waypoint	IMR7	IMR7	\N	\N	\N	0101000000D45C7B5A5685414096A0FE3467CB3F40
237	IMR8	626.17999999999995	Waypoint	IMR8	IMR8	\N	\N	\N	0101000000C57353565585414038379EB663CB3F40
238	IMR9	638.43700000000001	Waypoint	IMR9	IMR9	\N	\N	\N	010100000034C813F74E854140B7186E863CCB3F40
239	IMT 1	420.459	Waypoint	IMT 1	IMT 1	\N	\N	\N	01010000007BF538A6438141406CF23B1DAD614040
240	IMT 2	400.27199999999999	Waypoint	IMT 2	IMT 2	\N	\N	\N	0101000000BC5BE74C0B8141406B01BB49B5614040
241	IMT 3	394.02300000000002	Waypoint	IMT 3	IMT 3	\N	\N	\N	01010000004315DBFD1A814140258D9C489A614040
242	IMT 4	392.34100000000001	Waypoint	IMT 4	IMT 4	\N	\N	\N	0101000000DF98AFDC1C8141406BC6F0789D614040
243	IMT 5	401.95400000000001	Waypoint	IMT 5	IMT 5	\N	\N	\N	0101000000E1EF7F961A814140F6EC002299614040
244	IMT 6	401.47300000000001	Waypoint	IMT 6	IMT 6	\N	\N	\N	01010000001922BB930E8141408CE3B381A3614040
245	IMT6	405.07799999999997	Waypoint	IMT6	IMT6	\N	\N	\N	01010000008F8FB8F50A814140364D7755A2614040
246	IMT7	399.55099999999999	Waypoint	IMT7	IMT7	\N	\N	\N	010100000016FD13EF0F814140A14B682DAD614040
247	JCF1	399.55099999999999	Waypoint	JCF1	JCF1	\N	\N	\N	01010000002A848C824B854140737C4F0D7DC93F40
248	JCM1	825.41099999999994	Waypoint	JCM1	JCM1	\N	\N	\N	0101000000F1A978305D9D4140A5A71E229BC63F40
249	JMC1	504.57400000000001	Waypoint	JMC1	JMC1	\N	\N	\N	0101000000CE7B0B075C9D4140B37D0F31BAC63F40
250	LOWCYPSITE	234.68600000000001	Waypoint	LOWCYPSITE	LOWCYPSITE	\N	\N	\N	0101000000ADF6B0170A343840B9CFE0A5CAAC4140
251	PSEMBIN	125.337	Waypoint	PSEMBIN	PSEMBIN	\N	\N	\N	010100000041BF8CB501AC4040B13AA79603614140
252	PSEMFLDWL	121.97199999999999	Waypoint	PSEMFLDWL	PSEMFLDWL	\N	\N	\N	0101000000162827AFD5AB40401AB1C35618614140
253	PSEMSHDS1	122.693	Waypoint	PSEMSHDS1	PSEMSHDS1	\N	\N	\N	010100000093BF3CE3C9AB4040FDF82A7D11614140
254	PSEMSOC+Q	111.398	Waypoint	PSEMSOC+Q	PSEMSOC+Q	\N	\N	\N	0101000000A5AB9C3F2BAC404038F6F62B00614140
255	PSEMSWHRL	121.251	Waypoint	PSEMSWHRL	PSEMSWHRL	\N	\N	\N	0101000000017F4CF9FDAB40409562B47FFF604140
256	PSEMTOMB	113.801	Waypoint	PSEMTOMB	PSEMTOMB	\N	\N	\N	0101000000EBEDCC8D34AC40408DC714C50E614140
257	PSEMUPPOT	123.17400000000001	Waypoint	PSEMUPPOT	PSEMUPPOT	\N	\N	\N	010100000063EF62EDF1AB4040C6A0FF5D20614140
258	PSEMWALL	125.81699999999999	Waypoint	PSEMWALL	PSEMWALL	\N	\N	\N	010100000080E6B8C2D7AB40404190F36623614140
259	PSEMWALL2	121.011	Waypoint	PSEMWALL2	PSEMWALL2	\N	\N	\N	01010000005A75B3C5C9AB40409F814CC314614140
260	RTM1	1071.27	Waypoint	RTM1	RTM1	\N	\N	\N	01010000002220E0C21A1B3840A56A5326899F4140
261	RTM2	1099.1400000000001	Waypoint	RTM2	RTM2	\N	\N	\N	0101000000789889314F1B384030A31B0B919F4140
262	RTM3	1097.9400000000001	Waypoint	RTM3	RTM3	\N	\N	\N	01010000000479806B4A1B38409A1B94AF929F4140
263	RTM5	1094.0999999999999	Waypoint	RTM5	RTM5	\N	\N	\N	01010000002CEFA54E121B3840EE60A330989F4140
264	SCV1	1182.3	Waypoint	SCV1	SCV1	\N	\N	\N	01010000008E792C9C755740408A8DDC472C804140
265	SCV10	1174.8499999999999	Waypoint	SCV10	SCV10	\N	\N	\N	010100000069EB700CB257404046DA004B32804140
266	SCV100	1109.96	Waypoint	SCV100	SCV100	\N	\N	\N	01010000004967C8C7DA574040EACDF487307E4140
267	SCV106	1115.73	Waypoint	SCV106	SCV106	\N	\N	\N	01010000005E46A76ADF57404001219422327E4140
268	SCV107	1103.71	Waypoint	SCV107	SCV107	\N	\N	\N	01010000008F89AB02E95740401CEFE48C0E7E4140
269	SCV108	1132.79	Waypoint	SCV108	SCV108	\N	\N	\N	010100000010808F2C265840400A146F6CCF7E4140
270	SCV11	1183.74	Waypoint	SCV11	SCV11	\N	\N	\N	0101000000EA318FD1815740404853636022804140
271	SCV110	1137.8399999999999	Waypoint	SCV110	SCV110	\N	\N	\N	01010000006600F1B8FD5740403F8BE764507E4140
272	SCV12	1103.71	Waypoint	SCV12	SCV12	\N	\N	\N	01010000004C0C2FAC8E57404054289B0A22804140
273	SCV120	1073.9100000000001	Waypoint	SCV120	SCV120	\N	\N	\N	0101000000A4AF803F05584040FFA778E28D7E4140
274	SCV121	1261.6099999999999	Waypoint	SCV121	SCV121	\N	\N	\N	0101000000DF28777423584040A908A76DD27E4140
275	SCV122	1127.5	Waypoint	SCV122	SCV122	\N	\N	\N	01010000008476938721584040C845DB4FCB7E4140
276	SCV123	1127.5	Waypoint	SCV123	SCV123	\N	\N	\N	0101000000B27BB80507584040D7074DA3AD7E4140
277	SCV125	1153.7	Waypoint	SCV125	SCV125	\N	\N	\N	0101000000A80FABDFCF574040E6A638D4907E4140
278	SCV126	1159.23	Waypoint	SCV126	SCV126	\N	\N	\N	01010000001EDD5419D05740404780FB83867E4140
279	SCV13	1190.71	Waypoint	SCV13	SCV13	\N	\N	\N	0101000000AF34C32293574040F95224D11E804140
280	SCV130	1242.3800000000001	Waypoint	SCV130	SCV130	\N	\N	\N	0101000000F9D734E7C75740405A7537FEED7E4140
281	SCV131	1264.73	Waypoint	SCV131	SCV131	\N	\N	\N	01010000006153178BC757404066844F24EC7E4140
282	SCV134	1251.03	Waypoint	SCV134	SCV134	\N	\N	\N	0101000000ED792734C3574040E28AB422E47E4140
283	SCV135	1242.1400000000001	Waypoint	SCV135	SCV135	\N	\N	\N	01010000003F1F0753C45740408593B8B7F07E4140
284	SCV136	1224.1199999999999	Waypoint	SCV136	SCV136	\N	\N	\N	0101000000CC31DF15CF574040CA06112AEA7E4140
285	SCV137	1216.6600000000001	Waypoint	SCV137	SCV137	\N	\N	\N	0101000000957DBFE7B3574040B011F372F97E4140
286	SCV138	1146.73	Waypoint	SCV138	SCV138	\N	\N	\N	010100000008153B6B0E584040FF95077CCD7E4140
287	SCV14	1228.6800000000001	Waypoint	SCV14	SCV14	\N	\N	\N	0101000000DF0563A78C5740407490303C11804140
288	SCV15	1184.9400000000001	Waypoint	SCV15	SCV15	\N	\N	\N	0101000000B04B105290574040B05CD4C60F804140
289	SCV16	1159.47	Waypoint	SCV16	SCV16	\N	\N	\N	0101000000D53763AAB657404053886BA81D804140
290	SCV17	1157.3	Waypoint	SCV17	SCV17	\N	\N	\N	0101000000333B7442B4574040E203A89A3A804140
291	SCV18	1157.3	Waypoint	SCV18	SCV18	\N	\N	\N	0101000000B389EF6BB4574040986BCC493A804140
292	SCV2	1211.3800000000001	Waypoint	SCV2	SCV2	\N	\N	\N	01010000009BFF6EB96E574040B3B124D628804140
293	SCV21	1187.3499999999999	Waypoint	SCV21	SCV21	\N	\N	\N	0101000000348D446D7D574040D0DCC4420D804140
294	SCV25	1162.3499999999999	Waypoint	SCV25	SCV25	\N	\N	\N	0101000000EB018BADF457404087C328C148804140
295	SCV26	1392.3399999999999	Waypoint	SCV26	SCV26	\N	\N	\N	0101000000DFEF40321757404012C4A4B85D7F4140
296	SCV27	1397.1500000000001	Waypoint	SCV27	SCV27	\N	\N	\N	0101000000AB16F77020574040AE7E20045A7F4140
297	SCV28	1400.52	Waypoint	SCV28	SCV28	\N	\N	\N	0101000000F6F18E5B25574040C414A0FE577F4140
298	SCV29	1389.46	Waypoint	SCV29	SCV29	\N	\N	\N	0101000000AB65ECFD265740408E4110E8587F4140
299	SCV3	1389.46	Waypoint	SCV3	SCV3	\N	\N	\N	01010000000502901F19724040F0D85854C7014240
300	SCV30	1399.0699999999999	Waypoint	SCV30	SCV30	\N	\N	\N	010100000011F940882A574040FD899795507F4140
301	SCV31	1413.49	Waypoint	SCV31	SCV31	\N	\N	\N	0101000000B4F2D21A31574040350E25F5467F4140
302	SCV32	1414.6900000000001	Waypoint	SCV32	SCV32	\N	\N	\N	0101000000AB57572638574040863668FE447F4140
303	SCV33	1411.0899999999999	Waypoint	SCV33	SCV33	\N	\N	\N	0101000000118E38E83B574040782DF3E6487F4140
304	SCV34	1425.75	Waypoint	SCV34	SCV34	\N	\N	\N	0101000000B19684093857404028FF0CCF487F4140
305	SCV35	1401.96	Waypoint	SCV35	SCV35	\N	\N	\N	0101000000100E3FFD3C574040E48D10413C7F4140
306	SCV36	1392.3399999999999	Waypoint	SCV36	SCV36	\N	\N	\N	0101000000567498ED12574040FE15B3D75B7F4140
307	SCV37	1394.51	Waypoint	SCV37	SCV37	\N	\N	\N	0101000000C82227F520574040F30C0DD0657F4140
308	SCV38	1405.5599999999999	Waypoint	SCV38	SCV38	\N	\N	\N	01010000008908B39422574040CDFE1080577F4140
309	SCV39	1388.02	Waypoint	SCV39	SCV39	\N	\N	\N	01010000009ECAB8951D5740402A0B0B605D7F4140
310	SCV4	1191.1900000000001	Waypoint	SCV4	SCV4	\N	\N	\N	0101000000B808590469574040CD1211BC29804140
311	SCV40	1396.9100000000001	Waypoint	SCV40	SCV40	\N	\N	\N	0101000000BF13C742335740404E291C323F7F4140
312	SCV41	1407.73	Waypoint	SCV41	SCV41	\N	\N	\N	0101000000A39A80C9C7564040C562DB6F9B7F4140
313	SCV42	1398.1099999999999	Waypoint	SCV42	SCV42	\N	\N	\N	0101000000BF460476C4564040431D602A997F4140
314	SCV43	1400.28	Waypoint	SCV43	SCV43	\N	\N	\N	0101000000D4FA749FD0564040C53333F68F7F4140
315	SCV44	1414.9300000000001	Waypoint	SCV44	SCV44	\N	\N	\N	0101000000482E9FAFD0564040073CCB548A7F4140
316	SCV45	1394.75	Waypoint	SCV45	SCV45	\N	\N	\N	010100000081B20849CF564040550BF95B937F4140
317	SCV46	1394.03	Waypoint	SCV46	SCV46	\N	\N	\N	0101000000ABEDE4DB145740405E7070635F7F4140
318	SCV47	1385.8599999999999	Waypoint	SCV47	SCV47	\N	\N	\N	0101000000E550243701574040326704BB647F4140
319	SCV48	1378.4100000000001	Waypoint	SCV48	SCV48	\N	\N	\N	01010000004C6914CAF6564040DF7B0B2D637F4140
320	SCV49	1380.3299999999999	Waypoint	SCV49	SCV49	\N	\N	\N	0101000000D301F5D8E9564040BEFF7877707F4140
321	SCV50	1369.03	Waypoint	SCV50	SCV50	\N	\N	\N	010100000064B234C7245740405F3F483F6F7F4140
322	SCV51	1390.6600000000001	Waypoint	SCV51	SCV51	\N	\N	\N	0101000000408224E4EA564040FEB0401C6D7F4140
323	SCV52	1401.48	Waypoint	SCV52	SCV52	\N	\N	\N	0101000000F9574FE0EF564040DFE7F0F7687F4140
324	SCV53	1401.48	Waypoint	SCV53	SCV53	\N	\N	\N	0101000000BC96071EDC5640407055744C9F7F4140
325	SCV54	1387.0599999999999	Waypoint	SCV54	SCV54	\N	\N	\N	0101000000F3E9901FDA56404085F72C3A9F7F4140
326	SCV55	1378.1600000000001	Waypoint	SCV55	SCV55	\N	\N	\N	01010000000AE0BCF1DB564040DAB0FCB2A17F4140
327	SCV56	1399.55	Waypoint	SCV56	SCV56	\N	\N	\N	0101000000C22ECC3A0A5740400418A369617F4140
328	SCV57	1382.97	Waypoint	SCV57	SCV57	\N	\N	\N	01010000008830FF9A0C574040FFCF8C3E597F4140
329	SCV58	1382.97	Waypoint	SCV58	SCV58	\N	\N	\N	0101000000289C546908574040D6023930527F4140
330	SCV59	1382.97	Waypoint	SCV59	SCV59	\N	\N	\N	0101000000280B806D14574040C6DDD4CB657F4140
331	SCV6	1123.1800000000001	Waypoint	SCV6	SCV6	\N	\N	\N	0101000000BF22801A855740407F5F307352804140
332	SCV60	1376	Waypoint	SCV60	SCV60	\N	\N	\N	01010000001EF6A0721C574040FECD305A6C7F4140
333	SCV61	1376	Waypoint	SCV61	SCV61	\N	\N	\N	010100000078C59863155740409AF49E9A707F4140
334	SCV62A	1376	Waypoint	SCV62A	SCV62A	\N	\N	\N	010100000093C6B4AB1A574040C20A69ED737F4140
335	SCV62B	1416.6199999999999	Waypoint	SCV62B	SCV62B	\N	\N	\N	0101000000B68B3B6ED0564040A41F4C0B907F4140
336	SCV63	1361.8199999999999	Waypoint	SCV63	SCV63	\N	\N	\N	010100000075D518812C5740407EEEE034787F4140
337	SCV65	1379.1300000000001	Waypoint	SCV65	SCV65	\N	\N	\N	01010000008BD1A08CE4564040BE1BD35FA47F4140
338	SCV66	1397.3900000000001	Waypoint	SCV66	SCV66	\N	\N	\N	010100000087DBE8363D5740407225840C397F4140
339	SCV67	1404.3599999999999	Waypoint	SCV67	SCV67	\N	\N	\N	01010000007C11E98E3057404037F36CD53C7F4140
340	SCV69	1237.3299999999999	Waypoint	SCV69	SCV69	\N	\N	\N	01010000000AA6A4CBBE574040279A6068107F4140
341	SCV69 A	1218.3499999999999	Waypoint	SCV69 A	SCV69 A	\N	\N	\N	0101000000B6E3D46EBE574040AD3E63CE0F7F4140
342	SCV7	1131.5899999999999	Waypoint	SCV7	SCV7	\N	\N	\N	0101000000C9EFDC978D574040AE85689354804140
343	SCV70	1211.3800000000001	Waypoint	SCV70	SCV70	\N	\N	\N	0101000000DF0A6336C1574040E1D4F0F5017F4140
344	SCV71	1182.78	Waypoint	SCV71	SCV71	\N	\N	\N	0101000000890FB44ED1574040CC72BB92027F4140
345	SCV72	1182.78	Waypoint	SCV72	SCV72	\N	\N	\N	0101000000A81BC00CDA57404075EB142FFF7E4140
346	SCV73	1231.0899999999999	Waypoint	SCV73	SCV73	\N	\N	\N	01010000002DBDA0FFCA574040578194B7E77E4140
347	SCV74	1234.21	Waypoint	SCV74	SCV74	\N	\N	\N	01010000003CC3B841CB57404033FC56D4E87E4140
348	SCV76	1373.5999999999999	Waypoint	SCV76	SCV76	\N	\N	\N	01010000000B73FCFCE957404022EB6401377F4140
349	SCV77	1225.5599999999999	Waypoint	SCV77	SCV77	\N	\N	\N	01010000003C129732DC57404056951373247F4140
350	SCV78	1207.53	Waypoint	SCV78	SCV78	\N	\N	\N	0101000000BC6F845EE2574040D12794B3277F4140
351	SCV79	1225.3199999999999	Waypoint	SCV79	SCV79	\N	\N	\N	0101000000AD14F846D1574040037C4F962E7F4140
352	SCV8	1208.97	Waypoint	SCV8	SCV8	\N	\N	\N	0101000000E3C674BBB35740405645678229804140
353	SCV80	1243.3399999999999	Waypoint	SCV80	SCV80	\N	\N	\N	0101000000CEC5C85ACF574040D0DCD826347F4140
354	SCV81	1263.05	Waypoint	SCV81	SCV81	\N	\N	\N	010100000033FE36F1B95740400990F763407F4140
355	SCV82	1229.8800000000001	Waypoint	SCV82	SCV82	\N	\N	\N	01010000002D851C68B95740408C520FBF457F4140
356	SCV83	1255.8399999999999	Waypoint	SCV83	SCV83	\N	\N	\N	0101000000BA7384B3AB574040ADAF48284A7F4140
357	SCV84	1265.45	Waypoint	SCV84	SCV84	\N	\N	\N	0101000000440F636FB5574040110005DF407F4140
358	SCV86	1109.48	Waypoint	SCV86	SCV86	\N	\N	\N	0101000000541F1034DB5740402A45DCB02E7E4140
359	SCV87	1081.3599999999999	Waypoint	SCV87	SCV87	\N	\N	\N	0101000000F7963FCDF35740405EE6E8AF5F7E4140
360	SCV88	1121.02	Waypoint	SCV88	SCV88	\N	\N	\N	0101000000AD19F8D5055840402B384713AB7E4140
361	SCV89	1134.47	Waypoint	SCV89	SCV89	\N	\N	\N	0101000000462D0C12D45740409167CC618D7E4140
362	SCV9	1191.6700000000001	Waypoint	SCV9	SCV9	\N	\N	\N	0101000000646963C9B5574040A796D3BC28804140
363	SCV91	1167.8800000000001	Waypoint	SCV91	SCV91	\N	\N	\N	0101000000D579A08AE557404096444C420F7E4140
364	SCV92	1143.3699999999999	Waypoint	SCV92	SCV92	\N	\N	\N	0101000000B19F287D1F584040E999389BC97E4140
365	SCV93	1146.97	Waypoint	SCV93	SCV93	\N	\N	\N	010100000088AC081313584040B252E88CCD7E4140
366	SCV94	1131.3499999999999	Waypoint	SCV94	SCV94	\N	\N	\N	01010000001792707A07584040C8FB9E7CAD7E4140
367	SCV95	1173.1700000000001	Waypoint	SCV95	SCV95	\N	\N	\N	0101000000F90BDDEF0758404047600CE2CC7E4140
368	SCV96	1127.98	Waypoint	SCV96	SCV96	\N	\N	\N	01010000007AFB0635E25740402138D8E22D7E4140
369	SCV97	1115.49	Waypoint	SCV97	SCV97	\N	\N	\N	01010000004428771EE3574040B61E53B1107E4140
370	SCV98	1117.6500000000001	Waypoint	SCV98	SCV98	\N	\N	\N	0101000000028A68A6015840405D03FDB3567E4140
371	SCV99	1143.3699999999999	Waypoint	SCV99	SCV99	\N	\N	\N	0101000000E1A4A8B477574040898AA33828804140
\.


--
-- Data for Name: tblgpsdata; Type: TABLE DATA; Schema: public; Owner: aps03pwb
--

COPY tblgpsdata (name, elevation, symbol, "comment", descriptio, source, url, "url name", the_geom, gpsdataid) FROM stdin;
009a	-6.6033900000000001	Waypoint	009	009	\N	\N	\N	01010000008C73D6D21C593B40BFD37B62D14D4140	1265
009b	108.033	Waypoint	009	009	\N	\N	\N	0101000000865CB8922EDC43405CCA7071D1744440	1266
009c	294.28699999999998	Waypoint	009	009	\N	\N	\N	010100000021C9E9417A343840F05C87869F944740	1267
010a	-6.6033900000000001	Waypoint	010	010	\N	\N	\N	01010000000B20380BD4504040D11ED4EA50844140	1268
010b	292.84500000000003	Waypoint	010	010	\N	\N	\N	01010000005DDB8F9F8334384064B6948E9A944740	1269
010c	325.52999999999997	Flag	010	010	\N	\N	\N	01010000001806F7C6BAB140402C611C455E7A4140	1270
010d	1610.8	Waypoint	010	010	\N	\N	\N	01010000006CE2A061CA3F4240CB996C8AA77C4240	1271
011a	301.01600000000002	Waypoint	011	011	\N	\N	\N	0101000000FD4FC18B853438407966CBB793944740	1272
011b	313.51299999999998	Flag	011	011	\N	\N	\N	0101000000381B905C75AB404040325F564F7F4140	1273
011c	449.05799999999999	Flag	011	011	\N	\N	\N	01010000000BFEDF5AEB9B40408A13CF41A6814140	1274
011d	1610.5599999999999	Waypoint	011	011	\N	\N	\N	0101000000D4B47785BF3F4240E138C8A2AB7C4240	1275
012a	-6.6033900000000001	Waypoint	012	012	\N	\N	\N	01010000009D6C3869F02D53C086727C96E2314540	1276
012b	251.749	Flag	012	012	\N	\N	\N	010100000082C3184C14B34040C47D609E807E4140	1277
012c	298.13200000000001	Waypoint	012	012	\N	\N	\N	0101000000D023CFC08B343840C3BE237E93944740	1278
012d	484.62700000000001	Flag	012	012	\N	\N	\N	010100000096F6B718849A4040FBFD9A6043814140	1279
012e	1610.5599999999999	Waypoint	012	012	\N	\N	\N	0101000000DA501831BF3F4240FF04759CAB7C4240	1280
013a	251.749	Flag	013	013	\N	\N	\N	0101000000FBD8FC7614B340401DDCD0A6807E4140	1281
013b	292.84500000000003	Waypoint	013	013	\N	\N	\N	0101000000834BFF46C6343840638733EADA944740	1282
013c	500.24799999999999	Flag	013	013	\N	\N	\N	01010000007B95CB32839A4040A58CABD671814140	1283
013d	1612.24	Waypoint	013	013	\N	\N	\N	01010000006CEDD863BD3F4240D49D943AA27C4240	1284
014a	251.749	Flag	014	014	\N	\N	\N	0101000000F599CF9314B34040030181BA807E4140	1285
014b	288.75999999999999	Waypoint	014	014	\N	\N	\N	0101000000D90CC188B534384051F6241DD6944740	1286
014c	407.72199999999998	Flag	014	014	\N	\N	\N	0101000000742AC047E29B4040A21FD8B4FC824140	1287
014d	1609.1199999999999	Waypoint	014	014	\N	\N	\N	0101000000A3FD8E2FC13F4240DB7994AAAA7C4240	1288
015a	247.18299999999999	Flag	015	015	\N	\N	\N	0101000000ED00B08308B34040CD444BFD807E4140	1289
015b	299.815	Waypoint	015	015	\N	\N	\N	01010000008EF0C8E898343840A99198CB86944740	1290
015c	392.10000000000002	Flag	015	015	\N	\N	\N	0101000000EEF8521F589C40409631D3301B834140	1291
015d	1691.55	Waypoint	015	015	\N	\N	\N	01010000004C9F716FEB693640FAC35F2B12F54240	1292
016a	247.18299999999999	Flag	016	016	\N	\N	\N	0101000000CAEF329806B34040A65440DC807E4140	1293
016b	296.93099999999998	Waypoint	016	016	\N	\N	\N	0101000000027C6632643438402636F456EA944740	1294
016c	398.10899999999998	Flag	016	016	\N	\N	\N	0101000000D810985E4D9C404048D67CBD04834140	1295
016d	1689.6300000000001	Waypoint	016	016	\N	\N	\N	010100000085CDD0DD0D434040F3537F31991B4240	1296
017a	248.14400000000001	Flag	017	017	\N	\N	\N	0101000000B37FA3BF05B3404029F18BDB807E4140	1297
017b	288.03899999999999	Waypoint	017	017	\N	\N	\N	0101000000F2B82E5060343840FF856CC0EA944740	1298
017c	346.43799999999999	Flag	017	017	\N	\N	\N	0101000000B3D58723BFB14040A5931474557A4140	1299
017d	1686.51	Waypoint	017	017	\N	\N	\N	0101000000D6BE6C7A11434040132B4067941B4240	1300
018a	288.51900000000001	Flag	018	018	\N	\N	\N	010100000020518C1F2FAB40406F71AB257F7F4140	1301
018b	313.51299999999998	Waypoint	018	018	\N	\N	\N	0101000000D3061676073438405D68531AD6944740	1302
018c	323.84699999999998	Flag	018	018	\N	\N	\N	01010000006900440168AB40405E0568C3707F4140	1303
018d	1686.75	Waypoint	018	018	\N	\N	\N	0101000000F199BC6611434040F9AC6343941B4240	1304
019a	298.613	Waypoint	019	019	\N	\N	\N	0101000000DCACE008E9333840A989E0ADE3944740	1305
019b	316.63799999999998	Flag	019	019	\N	\N	\N	0101000000C669A47F6BAB4040E1FE268B707F4140	1306
019c	407.48099999999999	Flag	019	019	\N	\N	\N	0101000000BF1B9BB5459440402370D0D012864140	1307
019d	1421.1800000000001	Waypoint	019	019	\N	\N	\N	0101000000443F8030A3A84240FBB74746B9E14240	1308
067b 	317.11799999999999	Flag	067	067	\N	\N	\N	0101000000E8401B284CAA4040861978C0457F4140	1417
067a	302.93900000000002	Waypoint	067	067	\N	\N	\N	01010000002A9A5086BFB34040E2008828577D4140	1416
066b	319.041	Waypoint	066	066	\N	\N	\N	010100000066B1D0AB9AAB404017E128A4757F4140	1415
066a	317.11799999999999	Flag	066	066	\N	\N	\N	01010000008B57B49447AA4040BEC617B5477F4140	1414
065b	320.72300000000001	Flag	065	065	\N	\N	\N	0101000000886FECCF01AA40401B395451517F4140	1413
065a	318.80099999999999	Waypoint	065	065	\N	\N	\N	01010000004939BB5C94AB40404C7AD83E767F4140	1412
064b	320.72300000000001	Flag	064	064	\N	\N	\N	010100000043DF5C4ABDA940404795D8EA6F7F4140	1411
064a	320.00200000000001	Waypoint	064	064	\N	\N	\N	0101000000D3DE0B8C6FAB40402BC5E4C65A7F4140	1410
063a	317.839	Flag	063	063	\N	\N	\N	0101000000ECE87346B8A9404010B710D27D7F4140	1409
063b	313.27300000000002	Waypoint	063	063	\N	\N	\N	0101000000D3A2AB658CAB40408FFF0CB3737F4140	1408
062a	319.762	Flag	062	062	\N	\N	\N	01010000000DE600D8B8A940401F1A9CDC7D7F4140	1407
062b	313.51299999999998	Waypoint	062	062	\N	\N	\N	0101000000AC0F140D8CAB4040517CEC96737F4140	1406
061a	348.36099999999999	Flag	061	061	\N	\N	\N	0101000000DF08049AC5A94040F92F57D9D37F4140	1405
061b	313.75400000000002	Waypoint	061	061	\N	\N	\N	01010000003BAAC0E78CAB404095BE40AB747F4140	1404
060a	404.59800000000001	Flag	060	060	\N	\N	\N	0101000000610ED1D42A9840402227377DC2854140	1403
060b	313.75400000000002	Waypoint	060	060	\N	\N	\N	01010000003B0734B08CAB40409EE834B7747F4140	1402
059a	407.48099999999999	Flag	059	059	\N	\N	\N	0101000000AB3D243021984040C3B77B7CAC854140	1401
059b	313.75400000000002	Waypoint	059	059	\N	\N	\N	01010000002C5E8F368CAB4040E54F3070747F4140	1400
058a	313.27300000000002	Waypoint	058	058	\N	\N	\N	01010000009B5EB82C8CAB404059E6632D747F4140	1399
058b	215.69999999999999	Flag	058	058	\N	\N	\N	01010000001256C37B57B240405A819300848A4140	1398
057a	312.31200000000001	Waypoint	057	057	\N	\N	\N	0101000000FA6F233A8BAB404009B2E730747F4140	1397
057b	225.31299999999999	Flag	057	057	\N	\N	\N	0101000000B3381F7B56B24040B6B370D8848A4140	1396
056a	407.00099999999998	Flag	056	056	\N	\N	\N	0101000000FEF8220C09A74040EB4A57F2E97D4140	1395
056b	311.35000000000002	Waypoint	056	056	\N	\N	\N	01010000009C2FECEC88AB404056989357747F4140	1394
055	311.35000000000002	Waypoint	055	055	\N	\N	\N	01010000003AA787D888AB4040C72610AE747F4140	1393
049b	809.06899999999996	Waypoint	049	049	\N	\N	\N	010100000000A927D5F9524040B0AA4F533A7A4140	1387
049a	221.708	Flag	049	049	\N	\N	\N	01010000003C541BE856B2404087B9A7228A8A4140	1386
048b	808.34799999999996	Waypoint	048	048	\N	\N	\N	0101000000C51664C6F95240405F19608E3A7A4140	1385
048a	351.245	Flag	048	048	\N	\N	\N	010100000069F3D7E2D5A9404027ECE7BC627D4140	1384
047b	788.40099999999995	Waypoint	047	047	\N	\N	\N	0101000000EFD438A8F85240402A3A9784397A4140	1383
047a	385.61200000000002	Flag	047	047	\N	\N	\N	010100000043E188F553A54040189D6B19627F4140	1382
046b	618.97000000000003	Waypoint	046	046	\N	\N	\N	0101000000AD03CCEE9C5B404048D42F0405794140	1381
046a	429.83199999999999	Flag	046	046	\N	\N	\N	0101000000A9DF649FB0A44040A18978370B7F4140	1380
045b	464.43900000000002	Waypoint	045	045	\N	\N	\N	01010000008D33B3BA085A404032F186B52A774140	1379
045a	431.995	Flag	045	045	\N	\N	\N	0101000000158F60EAB4A4404008899BF01E7F4140	1378
044b	472.37	Waypoint	044	044	\N	\N	\N	01010000008834FC37075A4040367C70162D774140	1377
044a	319.52100000000002	Flag	044	044	\N	\N	\N	01010000009E0CDFD956B1404005B0D3D8767F4140	1376
043b	473.09100000000001	Waypoint	043	043	\N	\N	\N	010100000086C39815065A40402F89FFB02F774140	1375
043a	297.411	Flag	043	043	\N	\N	\N	01010000008625EB5E11B14040124FAB2533804140	1374
042b	471.40899999999999	Waypoint	042	042	\N	\N	\N	0101000000DB9958CC075A404087F3A8AC31774140	1373
042a	283.47199999999998	Flag	042	042	\N	\N	\N	0101000000E2C45FAC12B240407A71E764D77E4140	1372
041b	264.00599999999997	Flag	041	041	\N	\N	\N	01010000007C579FD3F5B240402CD40C3C467E4140	1371
041a	56.602899999999998	Waypoint	041	041	\N	\N	\N	0101000000C1C63CC5860B2D40C60B548C94F14140	1370
040b	261.60300000000001	Flag	040	040	\N	\N	\N	0101000000B84C6C8FF5B240400FABEC79467E4140	1369
040a	56.602899999999998	Waypoint	040	040	\N	\N	\N	0101000000C1C63CC5860B2D40C60B548C94F14140	1368
039b	239.012	Flag	039	039	\N	\N	\N	0101000000222E0345A1B94040DD370884A77E4140	1367
039a	56.602899999999998	Waypoint	039	039	\N	\N	\N	0101000000400E10EDDF052D40E0115C7FA7F24140	1366
038b	233.965	Flag	038	038	\N	\N	\N	0101000000F73FC05A75B94040424EC0F8A77E4140	1365
038a	89.768100000000004	Waypoint	038	038	\N	\N	\N	0101000000E8ACFB3958AC404055F12890B7604140	1364
037b	222.18899999999999	Flag	037	037	\N	\N	\N	0101000000358B5CDF63B940406AD6B4B3717E4140	1363
037a	122.93300000000001	Waypoint	037	037	\N	\N	\N	0101000000B702CB9031AC404052830BA7BC604140	1362
036b	208.00899999999999	Flag	036	036	\N	\N	\N	0101000000C4E223A16EBB4040B75FCC03497B4140	1361
036a	122.93300000000001	Waypoint	036	036	\N	\N	\N	0101000000B702CB9031AC404052830BA7BC604140	1360
035b	207.28800000000001	Flag	035	035	\N	\N	\N	010100000064113C1E70BB4040011B3BA14A7B4140	1359
035a	122.93300000000001	Waypoint	035	035	\N	\N	\N	0101000000B702CB9031AC404052830BA7BC604140	1358
034b	381.52600000000001	Flag	034	034	\N	\N	\N	01010000009910A3822B9D4040358E5B439E834140	1357
034a	123.17400000000001	Waypoint	034	034	\N	\N	\N	010100000025CC4C1A37AC40408A6A8B17BD604140	1356
033b	389.21699999999998	Flag	033	033	\N	\N	\N	01010000006F482CF4049D4040F91F08ACB6834140	1355
033a	97.218299999999999	Waypoint	033	033	\N	\N	\N	0101000000DBD0671C37AC4040197F9BF8BC604140	1354
032b	389.21699999999998	Flag	032	032	\N	\N	\N	0101000000F7FDD87AFF9C40403DA2DF4AB8834140	1353
032a	279.14600000000002	Waypoint	032	032	\N	\N	\N	01010000001806480EFAAD4040DA0DD3A55C634140	1352
031b	396.18599999999998	Flag	031	031	\N	\N	\N	010100000031822B78089D40408B24986F9F834140	1351
031a	23.4376	Waypoint	031	031	\N	\N	\N	0101000000953498E9CBB040406B64EF76915F4140	1350
030b	1113.8	Waypoint	030	030	\N	\N	\N	0101000000A548984B258B41406B142FA26F804040	1349
030a	621.13300000000004	Waypoint	030	030	\N	\N	\N	01010000008CD1210DFAA821400AB7E4075A394740	1348
030c	394.26299999999998	Flag	030	030	\N	\N	\N	010100000009B81079059D4040A55FB8F99A834140	1347
030d	317.35899999999998	Flag	030	030	\N	\N	\N	0101000000176B43BB46A94040C74C23DFC57F4140	1346
029a	1113.8	Waypoint	029	029	\N	\N	\N	01010000005DBF0FF0ECF240404610475471D14140	1345
029b	466.60199999999998	Flag	029	029	\N	\N	\N	0101000000F6DAB0C9419D40401A6F2BBD36834140	1344
029c	324.32799999999997	Flag	029	029	\N	\N	\N	010100000001FD569C7EA94040FA6A1480E97F4140	1343
029d	179.65100000000001	Waypoint	029	029	\N	\N	\N	0101000000E1048D361ADF2240F1BE34899DCA4640	1342
028a	1352.9300000000001	Waypoint	028	028	\N	\N	\N	01010000007D6B4657002D35404CA770629AE84340	1341
028b	1113.8	Waypoint	028	028	\N	\N	\N	0101000000E6C250A0F257404066ED5699607E4140	1340
028c	321.92500000000001	Flag	028	028	\N	\N	\N	0101000000313C7FA5B6A94040A7660743BA7F4140	1339
027a	676.88900000000001	Waypoint	027	027	\N	\N	\N	010100000054E6F5467EB435406341BFC1E7274440	1338
027b	612.48099999999999	Waypoint	027	027	\N	\N	\N	01010000008D0F2AA68FA639400B6D8CDFCC884140	1337
027c	477.41699999999997	Flag	027	027	\N	\N	\N	0101000000AA014DFB9B9340402B51D0CF4F864140	1336
027d	254.15199999999999	Flag	027	027	\N	\N	\N	0101000000125480348BAF40405811D0A541824140	1335
026a	573.06700000000001	Waypoint	026	026	\N	\N	\N	01010000004496282213A73940FCFF8E61FB884140	1334
026b	477.65699999999998	Flag	026	026	\N	\N	\N	0101000000DD0613519C9340404E10FB584F864140	1333
026c	36.896000000000001	Waypoint	026	026	\N	\N	\N	01010000003C13B679456D3B40AD961824AD034340	1332
025a	814.11599999999999	Waypoint	025	025	\N	\N	\N	01010000005BA140862B763F40F1753352185C4440	1331
025b	578.83500000000004	Waypoint	025	025	\N	\N	\N	0101000000923018CB10A73940C78CAB2200894140	1330
025c	475.49400000000003	Flag	025	025	\N	\N	\N	0101000000BE061CC09D9340405A0E5C3450864140	1329
025d	168.596	Flag	025	025	\N	\N	\N	01010000009C30C45EA6AD4040F97960BB4C954140	1328
024a	453.86500000000001	Flag	024	024	\N	\N	\N	01010000007208154F8F93404022078FB054864140	1327
024b	439.44499999999999	Waypoint	024	024	\N	\N	\N	0101000000AD0F4EB8157D39407C0F50476B9D4440	1326
024c	307.024	Waypoint	024	024	\N	\N	\N	0101000000BE525E77AF3338401EA80C49D1944740	1325
024d	166.91300000000001	Flag	024	024	\N	\N	\N	0101000000A5D41B71A5AD4040D07B084F4D954140	1324
023a	474.29199999999997	Flag	023	023	\N	\N	\N	01010000003B44046598934040DEF89BCF47864140	1323
023b	421.90100000000001	Waypoint	023	023	\N	\N	\N	0101000000C5FFD67B157D3940BBCC50DF699D4440	1322
023c	304.14100000000002	Waypoint	023	023	\N	\N	\N	0101000000D59AB8E5BB333840FE90C888D5944740	1321
023d	153.45500000000001	Flag	023	023	\N	\N	\N	0101000000FCE4BBA3A5AD4040AFC4942C4D954140	1320
022a	464.43900000000002	Flag	022	022	\N	\N	\N	0101000000AE8C4386A0934040559107AB3C864140	1319
022b	422.38200000000001	Waypoint	022	022	\N	\N	\N	01010000003D5E3E15147D3940557CB8EE6A9D4440	1318
022c	319.762	Flag	022	022	\N	\N	\N	01010000003C4B6F0367AB40406E4560816F7F4140	1317
022d	310.38900000000001	Waypoint	022	022	\N	\N	\N	0101000000B831FEFCC5333840B86FABDCE8944740	1316
021e	468.52499999999998	Flag	021	021	\N	\N	\N	0101000000032B7FA590934040AF0469FE4C864140	1315
021f	346.678	Flag	021	021	\N	\N	\N	0101000000F1A1DC6769AB40409726C8AF6F7F4140	1314
021g	269.29300000000001	Waypoint	021	021	\N	\N	\N	01010000009503A86FBA3338407561B8FBDB944740	1313
020a	1418.54	Waypoint	020	020	\N	\N	\N	0101000000ACAFE3FC63A8424092EFEFBF97E14240	1312
020b	469.48599999999999	Flag	020	020	\N	\N	\N	0101000000B0FDA6A8A1934040C791E8144D864140	1311
020c	313.51299999999998	Flag	020	020	\N	\N	\N	01010000005C2B0C5768AB4040BE79DC7D6F7F4140	1310
020d	310.38900000000001	Waypoint	020	020	\N	\N	\N	0101000000C3C89038EB333840910D3CA8D8944740	1309
ATV4a	310.149	Flag	ATV4	ATV4	\N	\N	\N	01010000001203136E68AA404073FADA30537F4140	1744
ATV4b	327.69299999999998	Flag	ATV4	ATV4	\N	\N	\N	0101000000877FB00A96AD40402D2C8FA03E7F4140	1745
AVATR11NEa	317.11799999999999	Waypoint	AVATR11NE	AVATR11NE	\N	\N	\N	01010000007A809B836FAB4040AA659BB6677F4140	1794
AVATR11NEb	320.00200000000001	Waypoint	AVATR11NE	AVATR11NE	\N	\N	\N	0101000000006BB7586FAB40400F1FE062687F4140	1795
AVATR11NWa	319.041	Waypoint	AVATR11NW	AVATR11NW	\N	\N	\N	01010000005C915B3D6EAB40403F39DF8F687F4140	1796
AVATR11NWb	320.964	Waypoint	AVATR11NW	AVATR11NW	\N	\N	\N	0101000000776CAB296EAB404062843CF7687F4140	1797
AVATR11SEa	318.31999999999999	Waypoint	AVATR11SE	AVATR11SE	\N	\N	\N	0101000000C4D8F3496FAB4040D4E98F1C687F4140	1798
AVATR11SEb	320.964	Waypoint	AVATR11SE	AVATR11SE	\N	\N	\N	01010000004735BCBE6EAB40409526EC75677F4140	1799
AVATR11SWa	319.762	Waypoint	AVATR11SW	AVATR11SW	\N	\N	\N	010100000048AF1FC56DAB40405DE2F83C677F4140	1801
AVATR11SWb	320.964	Waypoint	AVATR11SW	AVATR11SW	\N	\N	\N	0101000000FBC8739E6DAB4040E3AF24D4677F4140	1802
AVP1a	328.654	Flag	AVP1	AVP1	\N	\N	\N	0101000000B845F4C6A4AD404075E35C115C7F4140	1831
AVP1b	335.38299999999998	Flag	AVP1	AVP1	\N	\N	\N	01010000004390CFA02BAD4040652F13F9F97D4140	1832
AVP2a	322.16500000000002	Flag	AVP2	AVP2	\N	\N	\N	01010000006835C47BC5AD4040AF3008A26A7F4140	1834
AVP2b	338.74799999999999	Flag	AVP2	AVP2	\N	\N	\N	0101000000B60D4D203FAD40407CCD84BCE47D4140	1835
AVT1a	301.49700000000001	Flag	AVT1	AVT1	\N	\N	\N	0101000000062D40B5A3B04040ACECB86A69804140	1859
AVT1b	334.18200000000002	Flag	AVT1	AVT1	\N	\N	\N	01010000007C3D04FAE9AC40403294AB09F67E4140	1860
AVT2a	285.63499999999999	Flag	AVT2	AVT2	\N	\N	\N	01010000009E2988420AB140402BD88858AB804140	1861
AVT2b	327.21199999999999	Flag	AVT2	AVT2	\N	\N	\N	01010000007734DB0317AD40401BFCCF2A0B7F4140	1862
AVT3a	276.50299999999999	Flag	AVT3	AVT3	\N	\N	\N	01010000002AF95C96C4B04040E261F88D51804140	1863
AVT3b	327.93299999999999	Flag	AVT3	AVT3	\N	\N	\N	0101000000DD08904332AD404003810850177F4140	1864
AYVAR2a	311.59100000000001	Flag	AYVAR2	AYVAR2	\N	\N	\N	01010000002EB898FF8DAB404088B804A37C7F4140	1872
AYVAR2b	316.63799999999998	Waypoint	AYVAR2	AYVAR2	\N	\N	\N	01010000006AA7CFD68DAB404035B01BD77B7F4140	1873
CTC55a	1545.9100000000001	Waypoint	CTC55	CTC55	\N	\N	\N	010100000021CA84503F7240403884046ED8764140	2087
CTC55b	1549.04	Waypoint	CTC55	CTC55	\N	\N	\N	0101000000C597A7783E724040E6F8B7B7DA764140	2088
CTC68a	1620.1800000000001	Waypoint	CTC68	CTC68	\N	\N	\N	01010000009974BC4B677140407A29A703FA754140	2100
CTC68b	1622.0999999999999	Waypoint	CTC68	CTC68	\N	\N	\N	010100000033FBFA296771404013FFFE34F8754140	2101
CTC81a	1850.4100000000001	Waypoint	CTC81	CTC81	\N	\N	\N	01010000000FA0C85B7A6F40402C58D843E3764140	2108
CTC81b	1858.3399999999999	Waypoint	CTC81	CTC81	\N	\N	\N	0101000000EC4EC8E5776F404018769CCBE2764140	2109
SCV62a	1376	Waypoint	SCV62	SCV62	\N	\N	\N	010100000093C6B4AB1A574040C20A69ED737F4140	2579
SCV62b	1416.6199999999999	Waypoint	SCV62	SCV62	\N	\N	\N	010100000033EFEF6ED0564040A41F4C0B907F4140	2580
CTC12	788.64099999999996	Waypoint	TREE12	TREE12	\N	\N	\N	010100000093856B92F85240400635538C397A4140	2739
CTC13	812.43399999999997	Waypoint	TREE13	TREE13	\N	\N	\N	01010000005A24881BF95240402BF470EB367A4140	2740
CTC14	807.86699999999996	Waypoint	TREE14	TREE14	\N	\N	\N	0101000000D02B1FFBF952404095AC6C1A397A4140	2741
CTC15	1756.9200000000001	Waypoint	TREE15	TREE15	\N	\N	\N	01010000007A647B46456F40404ADC7C8664784140	2742
CTC16	1755.24	Waypoint	TREE16	TREE16	\N	\N	\N	010100000097515FA8596F40401389F38466784140	2743
CTC17	1749.71	Waypoint	TREE17	TREE17	\N	\N	\N	010100000060DE9F2F566F404064B4936360784140	2744
CTC18	1754.04	Waypoint	TREE18	TREE18	\N	\N	\N	01010000002DFFB25F5D6F4040E5FFA48D62784140	2745
CTC19	1754.28	Waypoint	TREE19	TREE19	\N	\N	\N	01010000004B0540D55B6F40405F9E6BEB69784140	2746
CTC20	1761.73	Waypoint	TREE20	TREE20	\N	\N	\N	01010000000F891091396F40404885ACCC66784140	2747
CTC21	1756.9200000000001	Waypoint	TREE21	TREE21	\N	\N	\N	010100000036703F94336F4040E428DBE865784140	2748
CTC22	1772.54	Waypoint	TREE22	TREE22	\N	\N	\N	010100000095732C66396F4040E68BC0CF6D784140	2749
CTC23	1799.22	Waypoint	TREE23	TREE23	\N	\N	\N	0101000000E76DA420436F40407101AF697A784140	2750
CTC24	1789.8499999999999	Waypoint	TREE24	TREE24	\N	\N	\N	01010000008E03FB24416F4040C1A1103180784140	2751
CTC25	1787.2	Waypoint	TREE25	TREE25	\N	\N	\N	0101000000775CC4DF456F4040B974BF4F84784140	2752
CTC26	1789.1300000000001	Waypoint	TREE26	TREE26	\N	\N	\N	0101000000E30AE3FF326F4040CD6234BB86784140	2753
CTC27	1782.6400000000001	Waypoint	TREE27	TREE27	\N	\N	\N	0101000000E695F0262D6F404012AD63FC7E784140	2754
CTC28	1785.04	Waypoint	TREE28	TREE28	\N	\N	\N	010100000039699B94226F4040BEB98F4186784140	2755
CTC29	1784.0799999999999	Waypoint	TREE29	TREE29	\N	\N	\N	010100000068146FBD166F40405E8B34F687784140	2756
CTC30	1798.74	Waypoint	TREE30	TREE30	\N	\N	\N	010100000090892C83FE6E40401E803FC291784140	2757
CTC31	1804.27	Waypoint	TREE31	TREE31	\N	\N	\N	01010000008D4F70CDF76E4040ECD48F5F95784140	2758
001a	1333.95	Waypoint	001	001	\N	\N	\N	0101000000B1225E2F511D53C0F587BC9E553A4540	1230
001b	286.59699999999998	Waypoint	001	001	\N	\N	\N	010100000053F67765001E53C07C3CF7957C3A4540	1231
001c	286.59699999999998	Waypoint	001	001	\N	\N	\N	010100000053F67765001E53C07C3CF7957C3A4540	1232
001d	1333.95	Waypoint	001	001	\N	\N	\N	0101000000B1225E2F511D53C0F587BC9E553A4540	1233
002a	1684.3399999999999	Waypoint	002	002	\N	\N	\N	01010000003777BC157E7240407C9A1C09F7774140	1234
002b	1684.3399999999999	Waypoint	002	002	\N	\N	\N	01010000003777BC157E7240407C9A1C09F7774140	1235
002c	27.763500000000001	Waypoint	002	002	\N	\N	\N	010100000066DEAFA40A262140CBFE9C2984444740	1236
002d	280.82900000000001	Waypoint	002	002	\N	\N	\N	0101000000C9E37BDCFD1D53C0FD275CE87A3A4540	1237
002e	280.82900000000001	Waypoint	002	002	\N	\N	\N	0101000000C9E37BDCFD1D53C0FD275CE87A3A4540	1238
003a	302.45800000000003	Waypoint	003	003	\N	\N	\N	0101000000EB5D003D741D53C06FEDA2BAC93A4540	1239
003b	279.62700000000001	Waypoint	003	003	\N	\N	\N	01010000004278CB0D9A1E53C02FF87BFCFA3A4540	1240
003c	302.45800000000003	Waypoint	003	003	\N	\N	\N	0101000000EB5D003D741D53C06FEDA2BAC93A4540	1241
003d	279.62700000000001	Waypoint	003	003	\N	\N	\N	01010000004278CB0D9A1E53C02FF87BFCFA3A4540	1242
004a	292.84500000000003	Waypoint	004	004	\N	\N	\N	0101000000D12DE87BB51E53C0FFEDD8261A3B4540	1243
004b	292.84500000000003	Waypoint	004	004	\N	\N	\N	0101000000D12DE87BB51E53C0FFEDD8261A3B4540	1244
004c	1173.1700000000001	Waypoint	004	004	\N	\N	\N	010100000047AC37FD682E3540277EFC0D69E24340	1245
004d	299.334	Waypoint	004	004	\N	\N	\N	0101000000207A01DF751D53C041EAF0BCC63A4540	1246
004e	299.334	Waypoint	004	004	\N	\N	\N	0101000000207A01DF751D53C041EAF0BCC63A4540	1247
005a	1172.4400000000001	Waypoint	005	005	\N	\N	\N	010100000018F5417D682E35401239B7E868E24340	1248
005b	298.613	Waypoint	005	005	\N	\N	\N	0101000000ABCB969D5F3438401944CC6EEB944740	1249
005c	298.613	Waypoint	005	005	\N	\N	\N	0101000000ABCB969D5F3438401944CC6EEB944740	1250
006a	257.99799999999999	Waypoint	006	006	\N	\N	\N	01010000003928362691AB39406ED5EF6E17874140	1251
006b	257.99799999999999	Waypoint	006	006	\N	\N	\N	01010000003928362691AB39406ED5EF6E17874140	1252
006c	296.93099999999998	Waypoint	006	006	\N	\N	\N	0101000000A0A500586A343840003E8CD1B9944740	1253
006d	296.93099999999998	Waypoint	006	006	\N	\N	\N	0101000000A0A500586A343840003E8CD1B9944740	1254
007a	1947.02	Waypoint	007	007	\N	\N	\N	010100000048E93CA4C40A4440D5B0B4930E594440	1255
007b	257.99799999999999	Waypoint	007	007	\N	\N	\N	0101000000D571B8336C5338406D4FF302AAB64140	1256
007c	282.75099999999998	Waypoint	007	007	\N	\N	\N	0101000000DEE54D3E70343840FC29C03DB0944740	1257
007d	257.99799999999999	Waypoint	007	007	\N	\N	\N	0101000000D571B8336C5338406D4FF302AAB64140	1258
007e	282.75099999999998	Waypoint	007	007	\N	\N	\N	0101000000DEE54D3E70343840FC29C03DB0944740	1259
008	1947.02	Waypoint	008	008	\N	\N	\N	0101000000C837B8CDC40A44406D8CAFD30E594440	1260
008a	304.14100000000002	Waypoint	008	008	\N	\N	\N	0101000000891E008A7434384029EACC3DA4944740	1261
008b	-6.6033900000000001	Waypoint	008	008	\N	\N	\N	0101000000A50770406C8F374046104C0D6BA24140	1262
008c	304.14100000000002	Waypoint	008	008	\N	\N	\N	0101000000891E008A7434384029EACC3DA4944740	1263
008d	-6.6033900000000001	Waypoint	008	008	\N	\N	\N	0101000000A50770406C8F374046104C0D6BA24140	1264
050	328.41399999999999	Waypoint	050	050	\N	\N	\N	010100000085FBBC3A6BAB404025397CAC827F4140	1388
051	328.654	Waypoint	051	051	\N	\N	\N	0101000000140A37376BAB4040DB3D97AE827F4140	1389
052	311.83100000000002	Waypoint	052	052	\N	\N	\N	01010000007E295F778AAB40402CF7AEB3747F4140	1390
053	311.35000000000002	Waypoint	053	053	\N	\N	\N	01010000002BFEE25E88AB404071F97F3D757F4140	1391
054	311.35000000000002	Waypoint	054	054	\N	\N	\N	0101000000CC09688F88AB4040F7A31888747F4140	1392
068	302.93900000000002	Waypoint	068	068	\N	\N	\N	01010000002A9A5086BFB34040E2008828577D4140	1418
069	473.09100000000001	Waypoint	069	069	\N	\N	\N	01010000009DFB10F3A39340402227DCC34F864140	1419
070	355.08999999999997	Waypoint	070	070	\N	\N	\N	01010000007D0B41C7B49C404007AFBB4B72834140	1420
071	355.08999999999997	Waypoint	071	071	\N	\N	\N	0101000000E29A7F179D9C4040BBB0B4DA63834140	1421
072	355.08999999999997	Waypoint	072	072	\N	\N	\N	0101000000F6CF8CAB759C404079C78C644D834140	1422
073	355.08999999999997	Waypoint	073	073	\N	\N	\N	01010000004ECB17CD6D9C404023EE939E47834140	1423
074	355.08999999999997	Waypoint	074	074	\N	\N	\N	010100000081AA049D669C4040A493DF8145834140	1424
075	355.08999999999997	Waypoint	075	075	\N	\N	\N	01010000001CE1875F3E9C4040133F70DC3C834140	1425
076	355.32999999999998	Waypoint	076	076	\N	\N	\N	0101000000693FF4D2049C40404C14381131834140	1426
077	355.32999999999998	Waypoint	077	077	\N	\N	\N	01010000007EF4E3B5E69B4040340479D62B834140	1427
078	370.23099999999999	Waypoint	078	078	\N	\N	\N	01010000001570EB48979B4040DF5F60FDEF824140	1428
079	378.88200000000001	Waypoint	079	079	\N	\N	\N	0101000000ABFF646D829B4040BADBB86FBC824140	1429
080	381.286	Waypoint	080	080	\N	\N	\N	01010000004077B437599B404033F31C45A1824140	1430
081	381.286	Waypoint	081	081	\N	\N	\N	0101000000A2E6049D2B9B404016899CCD89824140	1431
082	383.68900000000002	Waypoint	082	082	\N	\N	\N	01010000007705C134239B404020F2CB9D2F824140	1432
083	387.05399999999997	Waypoint	083	083	\N	\N	\N	010100000098D048771C9B404014A253C219824140	1433
084	387.29399999999998	Waypoint	084	084	\N	\N	\N	0101000000310314711A9B4040F38490B913824140	1434
085	387.53399999999999	Waypoint	085	085	\N	\N	\N	01010000007D17AFAC069B40404F4B88C60E824140	1435
086	390.41800000000001	Waypoint	086	086	\N	\N	\N	01010000002D02DD43349B404070A1074BB0814140	1436
087	406.75999999999999	Waypoint	087	087	\N	\N	\N	0101000000A20A7514449B40402DB683F0AC814140	1437
088	407.00099999999998	Waypoint	088	088	\N	\N	\N	01010000005E677337309B40402D62EC45B3814140	1438
089	408.44299999999998	Waypoint	089	089	\N	\N	\N	010100000011E3F392189B4040C83720B1B9814140	1439
090	408.44299999999998	Waypoint	090	090	\N	\N	\N	01010000008DB75342099B4040D68F73B9C6814140	1440
091	408.44299999999998	Waypoint	091	091	\N	\N	\N	0101000000CBDD0096099B4040D2535B1FD3814140	1441
092	409.64400000000001	Waypoint	092	092	\N	\N	\N	0101000000911FB7800C9B4040260E2C89E4814140	1442
093	409.88499999999999	Waypoint	093	093	\N	\N	\N	0101000000F38A2B570D9B4040B8B3C8D9E8814140	1443
094	409.88499999999999	Waypoint	094	094	\N	\N	\N	0101000000F06584B20E9B404061071CA901824140	1444
095	6.37439	Waypoint	095	095	\N	\N	\N	0101000000B5AA80D657AA4040B478EBDB8D5C4140	1445
096	417.57499999999999	Waypoint	096	096	\N	\N	\N	01010000004B2FE8BF329C4040CCC3285925844140	1446
097	396.18599999999998	Waypoint	097	097	\N	\N	\N	01010000002E36A85B08A74040790FF38C0D7E4140	1447
098	395.22500000000002	Waypoint	098	098	\N	\N	\N	01010000008B2DA4C6FBA64040775F77B5BA7E4140	1448
099	309.66800000000001	Waypoint	099	099	\N	\N	\N	010100000027501FDC38AB4040DEED93066B7F4140	1449
100	322.64600000000002	Waypoint	100	100	\N	\N	\N	0101000000FE2A589466AB4040A2B2C477607F4140	1450
101	299.09399999999999	Waypoint	101	101	\N	\N	\N	0101000000E9B5A46639AB404043213CB96A7F4140	1451
102	302.21800000000002	Waypoint	102	102	\N	\N	\N	0101000000ECBD5BCD38AB4040290669426A7F4140	1452
103	406.27999999999997	Waypoint	103	103	\N	\N	\N	0101000000AB6A639B3D944040B4646FD81F864140	1453
104	404.35700000000003	Waypoint	104	104	\N	\N	\N	0101000000EF261BB63D9440401C266BEB1F864140	1454
105	404.83800000000002	Waypoint	105	105	\N	\N	\N	010100000063A6F4193E944040E1F6B0891F864140	1455
106	404.83800000000002	Waypoint	106	106	\N	\N	\N	0101000000ACA8E7FB6EAB40401B349B97687F4140	1456
107	-7.8049299999999997	Waypoint	107	107	\N	\N	\N	01010000007C46F7B3258F3740BA8190997CA24140	1457
108	323.36700000000002	Waypoint	108	108	\N	\N	\N	01010000008E7C47551F51404085C41F4064844140	1458
109	323.36700000000002	Waypoint	109	109	\N	\N	\N	01010000008E7C47551F51404085C41F4064844140	1459
110	323.36700000000002	Waypoint	110	110	\N	\N	\N	01010000008E7C47551F51404085C41F4064844140	1460
111	1119.0899999999999	Waypoint	111	111	\N	\N	\N	0101000000E1A5FCEDF050404048584C5361844140	1461
112	28.003900000000002	Waypoint	112	112	\N	\N	\N	0101000000B1981898432940400EF08BE86A784140	1462
113	28.965299999999999	Waypoint	113	113	\N	\N	\N	010100000022C48B41442940400E16584470784140	1463
114	27.763500000000001	Waypoint	114	114	\N	\N	\N	0101000000831210DA452940407F1E38A16F784140	1464
115	27.763500000000001	Waypoint	115	115	\N	\N	\N	010100000072A470934A29404015ECD86B6E784140	1465
116	26.081199999999999	Waypoint	116	116	\N	\N	\N	01010000005CC8D7294C294040B0CC44D967784140	1466
117	27.523199999999999	Waypoint	117	117	\N	\N	\N	010100000024FE47F74A29404088BC106B64784140	1467
118	27.523199999999999	Waypoint	118	118	\N	\N	\N	01010000004D1F33B04B294040C488B4F562784140	1468
119	46.268900000000002	Waypoint	119	119	\N	\N	\N	01010000008FE004AE442640400FE90BAF91834140	1469
120	46.028599999999997	Waypoint	120	120	\N	\N	\N	01010000007A61DF0C462640401A44E05292834140	1470
121	47.2301	Waypoint	121	121	\N	\N	\N	01010000001C2A84DD49264040D50A8C228F834140	1471
122	1854.98	Waypoint	122	122	\N	\N	\N	01010000008ED520AB1F6E4040D226FBA54D774140	1472
15311SW	92.892499999999998	Waypoint	15311SW	15311SW	\N	\N	\N	010100000073C10CD2FBAB40406E57DB59A3604140	1473
15316SW	98.900599999999997	Waypoint	15316SW	15316SW	\N	\N	\N	01010000001BA6586300AC40401BE1A3B4AD604140	1474
15320NE	89.768100000000004	Waypoint	15320NE	15320NE	\N	\N	\N	01010000001A15CB3C58AC404004BDAC93B7604140	1475
15325NW	86.403599999999997	Waypoint	15325NW	15325NW	\N	\N	\N	01010000004091083C59AC4040C56ED7FFC4604140	1476
15325NWBIS	88.085800000000006	Waypoint	15325NWBIS	15325NWBIS	\N	\N	\N	010100000009C7780958AC40406808B090C5604140	1477
247 START	503.85300000000001	Waypoint	247 START	247 START	\N	\N	\N	0101000000CF529B004F9A40405671576DC6804140	1478
63403WALL	117.166	Waypoint	63403WALL	63403WALL	\N	\N	\N	0101000000F570B38CD5AB404087C06B7920614140	1479
63405T4	122.693	Waypoint	63405T4	63405T4	\N	\N	\N	0101000000A69DC092F0AB40404E68642027614140	1480
65203SDS	116.20399999999999	Waypoint	65203SDS	65203SDS	\N	\N	\N	01010000006DD5B00AD4AB40407D59170DD2604140	1481
65212WALL	100.583	Waypoint	65212WALL	65212WALL	\N	\N	\N	0101000000B63BABD2C4AB40407607B334F0604140	1482
65420TC1	108.273	Waypoint	65420TC1	65420TC1	\N	\N	\N	0101000000430A247CFDAB40402283C3A8B7604140	1483
65420TC2	107.55200000000001	Waypoint	65420TC2	65420TC2	\N	\N	\N	0101000000F34F0B86FCAB404004BDAC93B7604140	1484
65420TC3	107.312	Waypoint	65420TC3	65420TC3	\N	\N	\N	010100000099DA4024FDAB4040E4053971B7604140	1485
654TC4	107.55200000000001	Waypoint	654TC4	654TC4	\N	\N	\N	01010000005749D3BCFDAB4040F24BD43DB8604140	1486
AAL1	361.339	Flag	AAL1	AAL1	\N	\N	\N	010100000012A5049763A54040E058870B8F804140	1487
AAL2	359.89600000000002	Flag	AAL2	AAL2	\N	\N	\N	0101000000A10DAC225DA540409A4C389B5E804140	1488
AAL3	367.58699999999999	Flag	AAL3	AAL3	\N	\N	\N	0101000000450F307E50A54040C786A8A151804140	1489
AAL4	363.26100000000002	Flag	AAL4	AAL4	\N	\N	\N	0101000000A8BA03195AA540405B36A0C931804140	1490
AAL5	375.99799999999999	Flag	AAL5	AAL5	\N	\N	\N	010100000082C8784D75A54040B37BFC2204804140	1491
AAL6	378.642	Flag	AAL6	AAL6	\N	\N	\N	0101000000E477931294A54040A190C0C605804140	1492
AAL7	374.07600000000002	Flag	AAL7	AAL7	\N	\N	\N	01010000008D1B5B2889A540409CA4870E32804140	1493
AAL8	371.43200000000002	Flag	AAL8	AAL8	\N	\N	\N	0101000000BF0348DC7FA54040E409031D4D804140	1494
AAM1	380.565	Flag	AAM1	AAM1	\N	\N	\N	010100000096E77096DAA44040CB4068B48A834140	1495
AAM2	349.322	Flag	AAM2	AAM2	\N	\N	\N	0101000000F6D70FD708A54040A8210F1C4E834140	1496
AAY1	326.25099999999998	Flag	AAY1	AAY1	\N	\N	\N	0101000000A61AF3C353A74040CAF7060B59824140	1497
AAY2	329.375	Flag	AAY2	AAY2	\N	\N	\N	010100000098CEB4E850A740405A928F1F62824140	1498
AAY3	326.97199999999998	Flag	AAY3	AAY3	\N	\N	\N	0101000000ABF097B149A740402B34D3674E824140	1499
AAY4	331.53800000000001	Flag	AAY4	AAY4	\N	\N	\N	01010000007A012BC439A740404250CB9555824140	1500
AAY5	330.096	Flag	AAY5	AAY5	\N	\N	\N	01010000000896585630A7404060EDCF154A824140	1501
ADS1	87.605099999999993	Waypoint	ADS1	ADS1	\N	\N	\N	0101000000CAF73BFDA884414076F13F78FB4A4040	1502
ADS2	99.621600000000001	Waypoint	ADS2	ADS2	\N	\N	\N	0101000000EF5C2CCDAC844140C385D49EE64A4040	1503
AG06 1	405.31799999999998	Waypoint	AG06 1	AG06 1	\N	\N	\N	010100000045F4FE4E399440405C37032E18864140	1504
AG06 2	406.03899999999999	Waypoint	AG06 2	AG06 2	\N	\N	\N	0101000000EB04D1E63A944040F800F17B1A864140	1505
AG06 3	406.51999999999998	Waypoint	AG06 3	AG06 3	\N	\N	\N	0101000000FA2143823A944040C7F2376C1E864140	1506
AG06 4	391.13900000000001	Waypoint	AG06 4	AG06 4	\N	\N	\N	0101000000EB24075E40944040D17CD84F22864140	1507
AG06 5	406.03899999999999	Waypoint	AG06 5	AG06 5	\N	\N	\N	010100000003D50C973F9440400B2CB06219864140	1508
AG06 6	404.59800000000001	Waypoint	AG06 6	AG06 6	\N	\N	\N	01010000003E0724CE3B9440401915D8661A864140	1509
AG06 7	402.19400000000002	Waypoint	AG06 7	AG06 7	\N	\N	\N	0101000000A8FC5C4E38944040C8C36B2C1B864140	1510
AG06 8	400.27199999999999	Waypoint	AG06 8	AG06 8	\N	\N	\N	01010000006511D9F3349440403F111C7B1C864140	1511
AGA02-BIG	1973.46	Waypoint	AGA02-BIG	AGA02-BIG	\N	\N	\N	01010000008E313310240B444009F06E4934584440	1512
AGA03-1	1946.54	Waypoint	AGA03-1	AGA03-1	\N	\N	\N	01010000001BBA04A0C40A4440847C38970E594440	1513
AGA03-4	1990.28	Waypoint	AGA03-4	AGA03-4	\N	\N	\N	01010000000BD4FFC5750A4440330995BA8F584440	1514
AGACBASI 1	1972.26	Waypoint	AGACBASI 1	AGACBASI 1	\N	\N	\N	01010000009D3127C38C0A44404C03C02AB7584440	1515
AGH6	392.58100000000002	Flag	AGH6	AGH6	\N	\N	\N	0101000000B5AEC4481E974040B9A7B0F40F854140	1516
AGM1	372.63400000000001	Flag	AGM1	AGM1	\N	\N	\N	01010000008E429CCBF0964040A9BDCF8BEF844140	1517
AGM2	395.94600000000003	Flag	AGM2	AGM2	\N	\N	\N	010100000084CDEC32FD9640401DD1C324EA844140	1518
AGM3	395.22500000000002	Flag	AGM3	AGM3	\N	\N	\N	0101000000BF173BEE0F974040A678DBD3F5844140	1519
AGM4	394.74400000000003	Flag	AGM4	AGM4	\N	\N	\N	0101000000D1339F6EFE964040B01434F305854140	1520
AGM5	391.13900000000001	Flag	AGM5	AGM5	\N	\N	\N	010100000033686CD804974040549AD49D15854140	1521
AGM6	400.03100000000001	Flag	AGM6	AGM6	\N	\N	\N	0101000000B245A7EE57974040A9C6ABA9F5844140	1522
AGM7	403.39600000000002	Flag	AGM7	AGM7	\N	\N	\N	0101000000E4D020DA4E974040D15ED8F5E6844140	1523
AGM8	399.06999999999999	Flag	AGM8	AGM8	\N	\N	\N	01010000007F8BC0EB4297404043DB9C10DB844140	1524
AGR1	393.78300000000002	Flag	AGR1	AGR1	\N	\N	\N	01010000001511A78E3394404043ED637714864140	1525
AGR10	404.35700000000003	Flag	AGR10	AGR10	\N	\N	\N	01010000003B06C9DA5D944040197B274D20864140	1526
AGR2	392.34100000000001	Flag	AGR2	AGR2	\N	\N	\N	0101000000407887F03C9440409C0F745931864140	1527
AGR9	406.51999999999998	Flag	AGR9	AGR9	\N	\N	\N	0101000000FD9EBBD145944040DC6548E012864140	1528
AGRO3CORE1	466.84199999999998	Waypoint	AGRO3CORE1	AGRO3CORE1	\N	\N	\N	010100000026AB27959E934040FD1E5FBC4B864140	1529
AGRO3CORE2	478.61900000000003	Waypoint	AGRO3CORE2	AGRO3CORE2	\N	\N	\N	0101000000856B58CD9F934040B8E8439B4C864140	1530
AGROCAVE	472.61000000000001	Waypoint	AGROCAVE	AGROCAVE	\N	\N	\N	0101000000C22BC3649C9340406971E73E50864140	1531
AGROCAVE2	449.05799999999999	Waypoint	AGROCAVE2	AGROCAVE2	\N	\N	\N	0101000000CF136CAFC793404019A2D0D33C864140	1532
AGROCORE1	398.82999999999998	Waypoint	AGROCORE1	AGROCORE1	\N	\N	\N	010100000000AD081D45944040001498581D864140	1533
AGROCORE2	401.233	Waypoint	AGROCORE2	AGROCORE2	\N	\N	\N	0101000000D92713C635944040BBFA6C751D864140	1534
AGROCORE3	405.31799999999998	Waypoint	AGROCORE3	AGROCORE3	\N	\N	\N	0101000000066360F73F94404042DCAC2C17864140	1535
AGROCORE4	404.59800000000001	Waypoint	AGROCORE4	AGROCORE4	\N	\N	\N	0101000000E665D3653F944040981219BB1C864140	1536
AGROK1	401.95400000000001	Waypoint	AGROK1	AGROK1	\N	\N	\N	0101000000F191FF8F349440402D034DD21C864140	1537
AGRO KF1	398.589	Waypoint	AGRO KF1	AGRO KF1	\N	\N	\N	0101000000A040935535944040A980B80118864140	1538
AGRO KF2	402.19400000000002	Waypoint	AGRO KF2	AGRO KF2	\N	\N	\N	0101000000992034CC5A9440404ACCC0BC18864140	1539
AGRO KF3	401.47300000000001	Waypoint	AGRO KF3	AGRO KF3	\N	\N	\N	01010000006515FC5752944040656D0C6722864140	1540
AGRO KF4	398.82999999999998	Waypoint	AGRO KF4	AGRO KF4	\N	\N	\N	0101000000624648423A9440403CA0B8581B864140	1541
AGROQUERN	476.45600000000002	Waypoint	AGROQUERN	AGROQUERN	\N	\N	\N	0101000000DD8FE859A1934040B8A8C0104C864140	1542
AGRORCTOMB	403.63600000000002	Waypoint	AGRORCTOMB	AGRORCTOMB	\N	\N	\N	0101000000947313C9D89340406866686711864140	1543
AGROSITE2	460.59399999999999	Waypoint	AGROSITE2	AGROSITE2	\N	\N	\N	010100000002B97803C293404048AB18B63F864140	1544
AGROSITE3	473.09100000000001	Waypoint	AGROSITE3	AGROSITE3	\N	\N	\N	0101000000164E3F99A09340405D2D849044864140	1545
AGROSITE4	465.88099999999997	Waypoint	AGROSITE4	AGROSITE4	\N	\N	\N	0101000000D5EEB2BAB0934040059AC8FF36864140	1546
AGROTREE1	403.39600000000002	Waypoint	AGROTREE1	AGROTREE1	\N	\N	\N	0101000000F8DF87D94494404070C5A7FB1E864140	1547
AIROLO	1094.5799999999999	Waypoint	AIROLO	AIROLO	\N	\N	\N	010100000058A22321E036214099665C6688434740	1548
ALACA HOYU	1066.46	Waypoint	ALACA HOYU	ALACA HOYU	\N	\N	\N	010100000012DAE4832D594140705A9F5BF01D4440	1549
ALACAHOYUK	1078.96	Waypoint	ALACAHOYUK	ALACAHOYUK	\N	\N	\N	0101000000FCFFCBC413594140C7E15C5BE91D4440	1550
ALALAKH1	97.698999999999998	Waypoint	ALALAKH1	ALALAKH1	\N	\N	\N	0101000000117A33F3EF3042405F113162B51E4240	1551
ALAM K1	282.02999999999997	Waypoint	ALAM K1	ALAM K1	\N	\N	\N	0101000000EAF8C072EAB34040D688C346577D4140	1552
ALAM K CAV	296.69	Waypoint	ALAM K CAV	ALAM K CAV	\N	\N	\N	01010000009D55506559B340404905649B537D4140	1553
ALAM KL1	282.99200000000002	Waypoint	ALAM KL1	ALAM KL1	\N	\N	\N	0101000000D3393C0DE3B340405E27239E547D4140	1554
ALAM K L2	282.99200000000002	Waypoint	ALAM K L2	ALAM K L2	\N	\N	\N	010100000049FBD019D9B34040E90DB0BC4F7D4140	1555
ALAM K TR1	295.00799999999998	Waypoint	ALAM K TR1	ALAM K TR1	\N	\N	\N	01010000006E22A76DCBB340405466DB4D567D4140	1556
ALAM K TR2	284.91399999999999	Waypoint	ALAM K TR2	ALAM K TR2	\N	\N	\N	01010000005F7D3CF4DDB34040E4B4CBAA547D4140	1557
ALAMR	332.98000000000002	Flag	ALAMR	ALAMR	\N	\N	\N	0101000000F381BCD5F5B240409B0E37BC6D7D4140	1558
ALAMS1	298.13200000000001	Flag	ALAMS1	ALAMS1	\N	\N	\N	01010000008890042B58B3404084B774BD4E7D4140	1559
ALAMS2	291.40300000000002	Flag	ALAMS2	ALAMS2	\N	\N	\N	010100000065B18C8E5DB34040FD969BD06D7D4140	1560
ALAMS4	294.04700000000003	Flag	ALAMS4	ALAMS4	\N	\N	\N	0101000000105D2FD149B34040BBEAB0AB517D4140	1561
ALAMS5	292.36500000000001	Flag	ALAMS5	ALAMS5	\N	\N	\N	0101000000438CD7BC6AB34040D879441A4F7D4140	1562
ALAMS6	289.721	Flag	ALAMS6	ALAMS6	\N	\N	\N	01010000006EEADB006EB340400C23500C6F7D4140	1563
ALAMS CVE1	297.892	Waypoint	ALAMS CVE1	ALAMS CVE1	\N	\N	\N	010100000021291F0B5CB3404048119D8E557D4140	1564
ALAMSCV GP	298.613	Waypoint	ALAMSCV GP	ALAMSCV GP	\N	\N	\N	010100000059963B755DB34040728FFB0F567D4140	1565
ALES1	269.053	Flag	ALES1	ALES1	\N	\N	\N	01010000009EDF67C41BB6404093251F2D21804140	1566
ALES2	266.649	Flag	ALES2	ALES2	\N	\N	\N	0101000000EACE13CF19B64040B0E693242E804140	1567
ALES3	268.572	Flag	ALES3	ALES3	\N	\N	\N	0101000000BDD75F622BB64040B117988226804140	1568
ALES4	267.61099999999999	Flag	ALES4	ALES4	\N	\N	\N	0101000000F714D36112B640402F09C4A91E804140	1569
ALES5	241.17500000000001	Flag	ALES5	ALES5	\N	\N	\N	010100000012588743B9B54040E0D423E726804140	1570
ALESX1	260.40100000000001	Flag	ALESX1	ALESX1	\N	\N	\N	010100000063AFD03704B640400A87E39B1B804140	1571
ALKO1	247.18299999999999	Flag	ALKO1	ALKO1	\N	\N	\N	0101000000B66044037AB540404C403F98C37F4140	1572
ALKO2	262.32299999999998	Flag	ALKO2	ALKO2	\N	\N	\N	0101000000200BAB5A8DB5404023B284BFA37F4140	1573
ALKO3	263.52499999999998	Flag	ALKO3	ALKO3	\N	\N	\N	01010000008493EBA895B54040FE1B9091A77F4140	1574
ALKO4	261.60300000000001	Flag	ALKO4	ALKO4	\N	\N	\N	01010000001C0F2DE78FB540405E5FDC27B67F4140	1575
ALKO5	256.79599999999999	Flag	ALKO5	ALKO5	\N	\N	\N	0101000000FC0BFD468DB540402C63DB53C67F4140	1576
ALKS1	289.96100000000001	Flag	ALKS1	ALKS1	\N	\N	\N	0101000000952000916EB34040EADD64C3767D4140	1577
ALKS2	288.03899999999999	Flag	ALKS2	ALKS2	\N	\N	\N	0101000000977D4F9376B3404034F300F0717D4140	1578
ALKS3	282.27100000000002	Flag	ALKS3	ALKS3	\N	\N	\N	0101000000A9A13387E9B340400F7043B7577D4140	1579
ALKS4	303.17899999999997	Flag	ALKS4	ALKS4	\N	\N	\N	010100000067DB10B33CB34040D212CB30657D4140	1580
ALKS5	299.815	Flag	ALKS5	ALKS5	\N	\N	\N	0101000000FFEC0FE069B340408BE68C1E7B7D4140	1581
ALKS6	282.27100000000002	Flag	ALKS6	ALKS6	\N	\N	\N	0101000000123E544D22B3404067D23495B67D4140	1582
ALKS7	276.262	Flag	ALKS7	ALKS7	\N	\N	\N	0101000000C505B43561B34040936E3CB9D57D4140	1583
ALKSX1	288.03899999999999	Flag	ALKSX1	ALKSX1	\N	\N	\N	0101000000F36ECCB55FB340407C32B0A2877D4140	1584
ALKSX2	287.077	Flag	ALKSX2	ALKSX2	\N	\N	\N	0101000000A9AEA45E75B34040751BCFC5717D4140	1585
ALKSX3	278.42500000000001	Flag	ALKSX3	ALKSX3	\N	\N	\N	010100000066EC900ABFB3404098306F15917D4140	1586
ALL1	314.71499999999997	Flag	ALL1	ALL1	\N	\N	\N	0101000000E5CCCF3DC6B14040D0D07C24DE7E4140	1587
ALL2	294.28699999999998	Flag	ALL2	ALL2	\N	\N	\N	01010000005059A3D70CB24040EEC1184FCC7E4140	1588
ALL3	293.08499999999998	Flag	ALL3	ALL3	\N	\N	\N	010100000029AED4FAF5B14040A4F737AFC47E4140	1589
ALL4	292.84500000000003	Flag	ALL4	ALL4	\N	\N	\N	0101000000102AF756B2B14040A97C8B2BC77E4140	1590
ALL5	295.48899999999998	Flag	ALL5	ALL5	\N	\N	\N	01010000007A9993DDADB14040B31E6393E17E4140	1591
ALLIANOI	108.754	Waypoint	ALLIANOI	ALLIANOI	\N	\N	\N	0101000000016FB6F6144E3B402659E715D89D4340	1592
ALM1	313.75400000000002	Flag	ALM1	ALM1	\N	\N	\N	0101000000FBDC1FDB31B340409592E323697D4140	1593
ALM2	328.173	Flag	ALM2	ALM2	\N	\N	\N	01010000000EC35BA8FBB24040104254A26B7D4140	1594
ALM3	316.15699999999998	Flag	ALM3	ALM3	\N	\N	\N	01010000005FA9786DC0B240403DD3FFFD9F7D4140	1595
ALM4	332.01900000000001	Flag	ALM4	ALM4	\N	\N	\N	01010000002BB5537D6BB240400E2F8848CD7D4140	1596
ALM5	306.06299999999999	Flag	ALM5	ALM5	\N	\N	\N	01010000006E6477B18BB24040A808DF17F17D4140	1597
ALM6	257.27699999999999	Flag	ALM6	ALM6	\N	\N	\N	010100000061B48CA9BBB24040AA1774294B7E4140	1598
ALM7	262.32299999999998	Flag	ALM7	ALM7	\N	\N	\N	0101000000C099F3E7F6B24040DC5977D0457E4140	1599
ALM8	268.09100000000001	Flag	ALM8	ALM8	\N	\N	\N	0101000000F1FAC4E816B34040B04B0B99167E4140	1600
ALM9	280.58800000000002	Flag	ALM9	ALM9	\N	\N	\N	01010000000F4DF73F22B34040E7121BE7C77D4140	1601
ALN1	325.52999999999997	Flag	ALN1	ALN1	\N	\N	\N	010100000003D100CFD7B14040E8F74EE3967E4140	1602
ALN2	334.66199999999998	Flag	ALN2	ALN2	\N	\N	\N	0101000000CBB47CAB15B240409FE5A4F0537E4140	1603
ALN3	345.23599999999999	Flag	ALN3	ALN3	\N	\N	\N	0101000000978DD3B2E3B14040FF9DBF99707E4140	1604
ALN4	314.47399999999999	Flag	ALN4	ALN4	\N	\N	\N	01010000009A651C11B6B140406F4BF3ADA07E4140	1605
ALN5	298.13200000000001	Flag	ALN5	ALN5	\N	\N	\N	0101000000BFAB9CF8D5B1404039163715B97E4140	1606
ALN6	290.44200000000001	Flag	ALN6	ALN6	\N	\N	\N	0101000000C50F00E20FB24040F7F0AE78BF7E4140	1607
ALN7	282.51100000000002	Flag	ALN7	ALN7	\N	\N	\N	0101000000F4F1D8E83FB240405A9603CBBE7E4140	1608
ALS1	287.077	Flag	ALS1	ALS1	\N	\N	\N	01010000008E4AA07799B640401C5FE80238804140	1609
ALS10	252.95099999999999	Flag	ALS10	ALS10	\N	\N	\N	01010000006F01576896B640402E2A7C92C8804140	1610
ALS11	235.887	Flag	ALS11	ALS11	\N	\N	\N	0101000000041D576AD0B64040BA926BAAB1804140	1611
ALS2	233.72399999999999	Flag	ALS2	ALS2	\N	\N	\N	01010000000D4EA7E9F1B64040848757FCA0804140	1612
ALS3	243.33799999999999	Flag	ALS3	ALS3	\N	\N	\N	0101000000CD3AB3C20CB74040F78A130474804140	1613
ALS4	246.46199999999999	Flag	ALS4	ALS4	\N	\N	\N	0101000000CFBA88E713B740409141B47742804140	1614
ALS5	238.77099999999999	Flag	ALS5	ALS5	\N	\N	\N	01010000009D364860EAB64040D91A5B199E804140	1615
ALS6	236.60900000000001	Flag	ALS6	ALS6	\N	\N	\N	01010000003BE16754C7B64040977293589F804140	1616
ALS7	239.733	Flag	ALS7	ALS7	\N	\N	\N	0101000000CDB22C3A87B640407C31F48599804140	1617
ALS8	243.81800000000001	Flag	ALS8	ALS8	\N	\N	\N	0101000000ED10092359B64040A65D17418D804140	1618
ALS9	241.41499999999999	Flag	ALS9	ALS9	\N	\N	\N	0101000000154B2C517FB640408846AFBA99804140	1619
ANA1	426.46699999999998	Flag	ANA1	ANA1	\N	\N	\N	010100000074D42C2BA8A4404036F5EC7F217F4140	1620
ANA2	437.76299999999998	Flag	ANA2	ANA2	\N	\N	\N	01010000001512A00EBAA44040AD76E765217F4140	1621
ANA3	441.608	Flag	ANA3	ANA3	\N	\N	\N	0101000000C25A08B4ACA440408F23D8E3FE7E4140	1622
ANA4	429.59199999999998	Flag	ANA4	ANA4	\N	\N	\N	01010000004512E80CBFA440402DFAA0ED2C7F4140	1623
ANA5	429.351	Flag	ANA5	ANA5	\N	\N	\N	010100000035D2EF4EC0A4404016706710337F4140	1624
ANA6	413.49000000000001	Flag	ANA6	ANA6	\N	\N	\N	01010000004F1EEBE9B0A44040CF22FFBF367F4140	1625
ANG-1	1596.3800000000001	Waypoint	ANG-1	ANG-1	\N	\N	\N	0101000000E08D1E2262163840F84CEC5D20A24140	1626
ANG-10	1597.3399999999999	Waypoint	ANG-10	ANG-10	\N	\N	\N	0101000000CF2FB76C8E1638407B3F4FB2FEA14140	1627
ANG-11	1589.6500000000001	Waypoint	ANG-11	ANG-11	\N	\N	\N	0101000000460126996A16384053CAA8880CA24140	1628
ANG-12	1588.45	Waypoint	ANG-12	ANG-12	\N	\N	\N	0101000000A6DE460F6B163840E30FBDC704A24140	1629
ANG-13	1594.46	Waypoint	ANG-13	ANG-13	\N	\N	\N	01010000003ADAC9C4531638409D4E70B913A24140	1630
ANG-2	1606	Waypoint	ANG-2	ANG-2	\N	\N	\N	01010000007578AF2A601638402323F8C335A24140	1631
ANG-3	1612.48	Waypoint	ANG-3	ANG-3	\N	\N	\N	01010000002FA34EFC75163840C257B34F39A24140	1632
ANG-4	1590.1300000000001	Waypoint	ANG-4	ANG-4	\N	\N	\N	010100000064536F8C6B1638405EDEE40337A24140	1633
ANG5	1606.48	Waypoint	ANG5	ANG5	\N	\N	\N	0101000000F71A9E386216384007C22FA42CA24140	1634
ANG-6	1592.78	Waypoint	ANG-6	ANG-6	\N	\N	\N	0101000000CDC0AF2E7A1638401D82578901A24140	1635
ANG-7	1590.8599999999999	Waypoint	ANG-7	ANG-7	\N	\N	\N	01010000002BC706007E1638402DA8981805A24140	1636
ANG8	1591.5799999999999	Waypoint	ANG8	ANG8	\N	\N	\N	0101000000E77091A1811638404244BB2105A24140	1637
ANG-9	1590.1300000000001	Waypoint	ANG-9	ANG-9	\N	\N	\N	010100000087B1615A8F1638406C5F03CC03A24140	1638
ANK1	383.92899999999997	Flag	ANK1	ANK1	\N	\N	\N	01010000007800D58955A540402B9CBB95597F4140	1639
ANK2	377.68099999999998	Flag	ANK2	ANK2	\N	\N	\N	010100000065E137FA62A54040ABC624B9827F4140	1640
ANK3	385.85199999999998	Flag	ANK3	ANK3	\N	\N	\N	0101000000491A20F453A540409B39B718627F4140	1641
ANK4	406.27999999999997	Flag	ANK4	ANK4	\N	\N	\N	010100000081CC78A23EA54040A5C7338B377F4140	1642
ANK5	375.51799999999997	Flag	ANK5	ANK5	\N	\N	\N	010100000017400FED97A540403047DB6D617F4140	1643
ANK6	378.642	Flag	ANK6	ANK6	\N	\N	\N	0101000000BE5E5FC092A540401C7A9040717F4140	1644
ANKV1	388.73599999999999	Flag	ANKV1	ANKV1	\N	\N	\N	01010000005B9C1FE98DA34040CEE1E89739804140	1645
ANKV2	382.00700000000001	Flag	ANKV2	ANKV2	\N	\N	\N	01010000001432C75A92A3404099DF8C4137804140	1646
ANKV3	384.64999999999998	Flag	ANKV3	ANKV3	\N	\N	\N	0101000000EDE10F628EA340405257674A25804140	1647
ANKV4	385.85199999999998	Flag	ANKV4	ANKV4	\N	\N	\N	01010000008DC7C83689A340401229DFC92D804140	1648
ANKV5	386.33300000000003	Flag	ANKV5	ANKV5	\N	\N	\N	010100000081FF483961A34040A44803E728804140	1649
ANKV6	386.57299999999998	Flag	ANKV6	ANKV6	\N	\N	\N	0101000000D69B287464A34040639A981728804140	1650
ANKV7	384.41000000000003	Flag	ANKV7	ANKV7	\N	\N	\N	0101000000FC4FD36E81A34040B80D4F212A804140	1651
ANP1	353.16699999999997	Flag	ANP1	ANP1	\N	\N	\N	01010000008E2DE07270A4404014C98FACC7804140	1652
ANP2	368.30799999999999	Flag	ANP2	ANP2	\N	\N	\N	01010000007A63DB7E86A440402E0969F0AB804140	1653
ANP3	370.471	Flag	ANP3	ANP3	\N	\N	\N	0101000000B2F5E61976A4404073E88F91B5804140	1654
ANT 40-52	1900.8800000000001	Waypoint	ANT 40-52	ANT 40-52	\N	\N	\N	0101000000D31F92082AC53D4002F7AED482404240	1655
ANT 53-61	1557.9300000000001	Waypoint	ANT 53-61	ANT 53-61	\N	\N	\N	01010000005E9D7F9525C53D401B6CD3A074404240	1656
ANT 62	1562.74	Waypoint	ANT 62	ANT 62	\N	\N	\N	0101000000E7E821B531BD3D40DD5A7F7BF9434240	1657
ANV1	364.46300000000002	Flag	ANV1	ANV1	\N	\N	\N	01010000003F4D7CA1FFA44040DA2CEF8EB2804140	1658
ANV2	363.74200000000002	Flag	ANV2	ANV2	\N	\N	\N	010100000040B10408D5A44040F7E48768B9804140	1659
ANV4	361.09800000000001	Flag	ANV4	ANV4	\N	\N	\N	0101000000AA2C7C1091A440400CA6B03E9D804140	1660
APG1	16.4681	Waypoint	APG1	APG1	\N	\N	\N	0101000000626457C7222640407CD1C37531834140	1661
APG10	29.2056	Waypoint	APG10	APG10	\N	\N	\N	0101000000B6288308102640407B57606F32834140	1662
APG11	30.887899999999998	Waypoint	APG11	APG11	\N	\N	\N	0101000000FFEF2A99132640405B77C31B31834140	1663
APG12	44.826900000000002	Waypoint	APG12	APG12	\N	\N	\N	010100000032318BC0402640404DC7FCD958834140	1664
APG13	45.067300000000003	Waypoint	APG13	APG13	\N	\N	\N	01010000005083D4B3412640403E04A1315D834140	1665
APG14	43.144500000000001	Waypoint	APG14	APG14	\N	\N	\N	0101000000508077DE452640403C7213D149834140	1666
APG15	44.346299999999999	Waypoint	APG15	APG15	\N	\N	\N	010100000079643CE2412640403147C4D16B834140	1667
APG16	48.431800000000003	Waypoint	APG16	APG16	\N	\N	\N	0101000000A5CD78283826404083CBD7239C834140	1668
APG18	60.207900000000002	Waypoint	APG18	APG18	\N	\N	\N	0101000000CBD43B1479264040464BF016A0834140	1669
APG19	37.3767	Waypoint	APG19	APG19	\N	\N	\N	0101000000A56B4CA64F2640406F7493ED43834140	1670
APG2	33.531500000000001	Waypoint	APG2	APG2	\N	\N	\N	0101000000186F08AE222640401A8C1BFB35834140	1671
APG20	54.680300000000003	Waypoint	APG20	APG20	\N	\N	\N	010100000043377F3C492640409B089C1E74834140	1672
APG22	48.191499999999998	Waypoint	APG22	APG22	\N	\N	\N	01010000003105401C3126404085763F8893834140	1673
APG23	47.710799999999999	Waypoint	APG23	APG23	\N	\N	\N	010100000091936B052B2640407142C84986834140	1674
APG3	41.702500000000001	Waypoint	APG3	APG3	\N	\N	\N	01010000009B835BCF342640408BB86BCF4D834140	1675
APG4	28.003900000000002	Waypoint	APG4	APG4	\N	\N	\N	0101000000DD262301FF25404097925C3336834140	1676
APG5	23.918299999999999	Waypoint	APG5	APG5	\N	\N	\N	0101000000FF1AB0AE01264040E934034333834140	1677
APG6	40.741199999999999	Waypoint	APG6	APG6	\N	\N	\N	0101000000C73830CD352640401D78BF4E4D834140	1678
APG7	28.725000000000001	Waypoint	APG7	APG7	\N	\N	\N	0101000000869420D510264040C272C37027834140	1679
APG8	43.625100000000003	Waypoint	APG8	APG8	\N	\N	\N	010100000058DEF0E3312640407D8FC0404C834140	1680
APG9	45.547899999999998	Waypoint	APG9	APG9	\N	\N	\N	0101000000B0FDC7B64A264040B3B7C42C5C834140	1681
APHRODISIA	556.24400000000003	Waypoint	APHRODISIA	APHRODISIA	\N	\N	\N	010100000031A5C836FDB93C40E1F5F46CB1DA4240	1682
ARC1	408.68299999999999	Flag	ARC1	ARC1	\N	\N	\N	01010000007BDD0B9420984040F5C2D7B6AC854140	1683
ARC2	409.404	Flag	ARC2	ARC2	\N	\N	\N	01010000002A2B500250984040A3F9CEF5DE854140	1684
ARC4	407.00099999999998	Flag	ARC4	ARC4	\N	\N	\N	0101000000CC132B4A5998404094DB7F2FC8854140	1685
ARC5	415.17200000000003	Flag	ARC5	ARC5	\N	\N	\N	0101000000AEC1F8F24D9840407033AFFFC7854140	1686
AS1	373.59500000000003	Flag	AS1	AS1	\N	\N	\N	0101000000311384ACA0A540400805EC3D71804140	1687
AS13P1	394.50400000000002	Waypoint	AS13P1	AS13P1	\N	\N	\N	010100000059BABFD05CA740403F15BBA6957D4140	1688
AS19A	383.68900000000002	Waypoint	AS19A	AS19A	\N	\N	\N	0101000000AE2D20DAE5A5404043F53CA3A07D4140	1689
AS19D	386.81299999999999	Waypoint	AS19D	AS19D	\N	\N	\N	010100000092EA2423F3A54040AD279CD8A17D4140	1690
AS2	371.19200000000001	Flag	AS2	AS2	\N	\N	\N	01010000001867B873B4A54040A1EE578F68804140	1691
AS20A	372.39400000000001	Waypoint	AS20A	AS20A	\N	\N	\N	010100000085B587763BA640406D691C87CD7D4140	1692
AS20D	384.17000000000002	Waypoint	AS20D	AS20D	\N	\N	\N	010100000072CDEFC484A64040205A472FCC7D4140	1693
AS21A	389.93799999999999	Waypoint	AS21A	AS21A	\N	\N	\N	0101000000745C20509CA64040ED3AD7D4D27D4140	1694
AS21P1	383.92899999999997	Waypoint	AS21P1	AS21P1	\N	\N	\N	0101000000AAFDCEDD8BA64040D493C455CF7D4140	1695
AS22A	385.61200000000002	Waypoint	AS22A	AS22A	\N	\N	\N	0101000000A92BF89DC1A640402BA3875DB87D4140	1696
AS22D	412.76900000000001	Waypoint	AS22D	AS22D	\N	\N	\N	01010000006CF612F304A740404FDAACDEC77D4140	1697
AS23A	390.178	Waypoint	AS23A	AS23A	\N	\N	\N	01010000009A50F31B47A740402DCA0C1EDD7D4140	1698
AS24A	413.73000000000002	Waypoint	AS24A	AS24A	\N	\N	\N	0101000000305297A8F8A64040BDE7D456EB7D4140	1699
AS24B	400.03100000000001	Waypoint	AS24B	AS24B	\N	\N	\N	0101000000589F0868F6A640403AF238F3087E4140	1700
AS24D	407.48099999999999	Waypoint	AS24D	AS24D	\N	\N	\N	0101000000E74B0F0DC2A640409210339B0A7E4140	1701
AS25B	387.77499999999998	Waypoint	AS25B	AS25B	\N	\N	\N	0101000000A23387A98FA640402915570C0C7E4140	1702
AS26A	384.89100000000002	Waypoint	AS26A	AS26A	\N	\N	\N	0101000000C36AF4B68EA6404097016CE2127E4140	1703
AS26C	408.68299999999999	Waypoint	AS26C	AS26C	\N	\N	\N	0101000000E05E348CC4A64040D353AC66127E4140	1704
AS27A	384.41000000000003	Waypoint	AS27A	AS27A	\N	\N	\N	0101000000C6B42FD308A740403B2F5FA80D7E4140	1705
AS27B	389.21699999999998	Waypoint	AS27B	AS27B	\N	\N	\N	0101000000741289C30BA74040170609AA367E4140	1706
AS27EX5	391.62	Waypoint	AS27EX5	AS27EX5	\N	\N	\N	0101000000EA239F40E0A64040BFD53F2A337E4140	1707
AS28A	386.57299999999998	Waypoint	AS28A	AS28A	\N	\N	\N	01010000007D99F0970BA740404FF0CE533D7E4140	1708
AS28B	370.71100000000001	Waypoint	AS28B	AS28B	\N	\N	\N	010100000092E17BF611A74040873E7BD55A7E4140	1709
AS28C	376.71899999999999	Waypoint	AS28C	AS28C	\N	\N	\N	010100000098BD5857C6A64040E6C704A1617E4140	1710
AS29A	369.99000000000001	Waypoint	AS29A	AS29A	\N	\N	\N	0101000000C18E88F49EA64040530E5430647E4140	1711
AS29F1	372.15300000000002	Waypoint	AS29F1	AS29F1	\N	\N	\N	0101000000C8D1B475C4A64040CCF7ECB6547E4140	1712
AS3	376.71899999999999	Flag	AS3	AS3	\N	\N	\N	0101000000A68B24ACD3A54040B8E060FD4C804140	1713
AS30A	369.99000000000001	Waypoint	AS30A	AS30A	\N	\N	\N	01010000000B48301270A64040E67010216C7E4140	1714
AS30EX6	363.74200000000002	Waypoint	AS30EX6	AS30EX6	\N	\N	\N	01010000000B3B786598A64040B85EEC20637E4140	1715
AS31A	359.89600000000002	Waypoint	AS31A	AS31A	\N	\N	\N	0101000000C2C00B0C6DA64040F0FAB004707E4140	1716
AS31C	382.48700000000002	Waypoint	AS31C	AS31C	\N	\N	\N	010100000052066720B1A64040428067D66D7E4140	1717
AS32A	378.642	Waypoint	AS32A	AS32A	\N	\N	\N	0101000000BFF69E5AD5A64040C1B44F976A7E4140	1718
ASM1	340.19	Waypoint	ASM1	ASM1	\N	\N	\N	01010000003EED67E6C68D414028FF585D8E424040	1719
ASP1	334.66199999999998	Flag	ASP1	ASP1	\N	\N	\N	010100000072FE98690AA74040A81149E08E824140	1720
ASP2	351.48500000000001	Flag	ASP2	ASP2	\N	\N	\N	01010000003DF5277532A74040FED5505B84824140	1721
ASP3	372.15300000000002	Flag	ASP3	ASP3	\N	\N	\N	0101000000BEF67B4B81A64040565B303F57824140	1722
ASP4	377.44	Flag	ASP4	ASP4	\N	\N	\N	01010000007704E4094CA64040B2AA442AA3824140	1723
ASP5	371.673	Flag	ASP5	ASP5	\N	\N	\N	0101000000CE7853786DA64040139D3F4FBE824140	1724
ATMA1	307.26499999999999	Flag	ATMA1	ATMA1	\N	\N	\N	0101000000DC6707EF7AAA4040A594AF825A7F4140	1725
ATMA2	321.92500000000001	Flag	ATMA2	ATMA2	\N	\N	\N	01010000002F51B87CB6A940400F280356BA7F4140	1726
ATMA3	326.97199999999998	Flag	ATMA3	ATMA3	\N	\N	\N	010100000081F4F4E17EA94040F71C44AAE97F4140	1727
ATMA4	331.05700000000002	Flag	ATMA4	ATMA4	\N	\N	\N	0101000000C4D0BF6430A940400F4FF3B122804140	1728
ATMA5	322.64600000000002	Flag	ATMA5	ATMA5	\N	\N	\N	0101000000C82F47852DA94040666EA775E77F4140	1729
ATMA6	318.07999999999998	Flag	ATMA6	ATMA6	\N	\N	\N	010100000094CEF7BB46A94040CA3D80ECC57F4140	1730
ATMA7	328.654	Flag	ATMA7	ATMA7	\N	\N	\N	0101000000F7BD88E163A9404034F3420CC47F4140	1731
ATMA8	323.12599999999998	Flag	ATMA8	ATMA8	\N	\N	\N	010100000024F83FBDA2A9404023959481A47F4140	1732
ATMA9	310.38900000000001	Flag	ATMA9	ATMA9	\N	\N	\N	01010000001CEBBC6F37AA40405184EB89627F4140	1733
ATMB1	321.20400000000001	Flag	ATMB1	ATMB1	\N	\N	\N	01010000002AC264E24AA94040ACE81486B67F4140	1734
ATMB2	321.92500000000001	Flag	ATMB2	ATMB2	\N	\N	\N	010100000042B6100A68A940408AB66032A07F4140	1735
ATMB3	321.92500000000001	Flag	ATMB3	ATMB3	\N	\N	\N	010100000089835BA96DA94040D80D9441997F4140	1736
ATMB4	320.00200000000001	Flag	ATMB4	ATMB4	\N	\N	\N	010100000001ACF80089A9404060ACF398967F4140	1737
ATMB5	318.31999999999999	Flag	ATMB5	ATMB5	\N	\N	\N	0101000000B173A075B7A940403E3783AB7B7F4140	1738
ATMB6	321.44400000000002	Flag	ATMB6	ATMB6	\N	\N	\N	0101000000F016F708BDA9404049F560FC7B7F4140	1739
ATV1	307.745	Flag	ATV1	ATV1	\N	\N	\N	01010000008EED8768CCAA40406067707F617F4140	1740
ATV10	334.90199999999999	Flag	ATV10	ATV10	\N	\N	\N	01010000007C446325DAA94040397064A4B27E4140	1741
ATV2	300.77600000000001	Flag	ATV2	ATV2	\N	\N	\N	010100000044CD6C6492AA404088D0F451477F4140	1742
ATV3	309.428	Flag	ATV3	ATV3	\N	\N	\N	0101000000B628074174AA40408D180B7D4F7F4140	1743
ATV5	304.38099999999997	Flag	ATV5	ATV5	\N	\N	\N	0101000000E9A0C7FF4FAA4040408C8CE5487F4140	1746
ATV6	317.839	Flag	ATV6	ATV6	\N	\N	\N	0101000000BC99942CEEA94040C3012F5E2C7F4140	1747
ATV7	314.95499999999998	Flag	ATV7	ATV7	\N	\N	\N	0101000000761E031CBCA94040FB863B77F87E4140	1748
ATV8	335.62299999999999	Flag	ATV8	ATV8	\N	\N	\N	0101000000D05798D7F4A84040A9B4C8ED8C7E4140	1749
ATV9	323.12599999999998	Flag	ATV9	ATV9	\N	\N	\N	0101000000FB8B3B066DA94040C972BC49A67E4140	1750
AUG04T06	781.67200000000003	Waypoint	AUG04T06	AUG04T06	\N	\N	\N	01010000003FC18BDF905C4040206C885C36794140	1751
AUG04T07	779.26800000000003	Waypoint	AUG04T07	AUG04T07	\N	\N	\N	0101000000814387448A5C4040DF4EDBEC33794140	1752
AUG04T08	836.46699999999998	Waypoint	AUG04T08	AUG04T08	\N	\N	\N	01010000007E671B829A5C40409226505746794140	1753
AUG04T09	798.97500000000002	Waypoint	AUG04T09	AUG04T09	\N	\N	\N	0101000000C831FC21A25C4040990250C74E794140	1754
AUG04T10	850.40599999999995	Waypoint	AUG04T10	AUG04T10	\N	\N	\N	0101000000C942B36C9F5C4040DEFE9E5076784140	1755
AUG04T11	851.12599999999998	Waypoint	AUG04T11	AUG04T11	\N	\N	\N	01010000008B7F0FC69E5C404023F536E774784140	1756
AUG24T01	464.19900000000001	Waypoint	AUG24T01	AUG24T01	\N	\N	\N	01010000000809145B085A4040D8F51F5A2A774140	1757
AUG24T02	632.90899999999999	Waypoint	AUG24T02	AUG24T02	\N	\N	\N	01010000000DAC436B4E5B4040F4599B4FA8784140	1758
AUG24T03	642.28200000000004	Waypoint	AUG24T03	AUG24T03	\N	\N	\N	0101000000F8A7BC6CA75B4040FE0CAC39F9784140	1759
AUG24T04	618.97000000000003	Waypoint	AUG24T04	AUG24T04	\N	\N	\N	01010000004A1EF4119D5B40409E2453C105794140	1760
AUG24T05	653.096	Waypoint	AUG24T05	AUG24T05	\N	\N	\N	01010000000FF27FE9A25B40402BF027C0F6784140	1761
AV06 4	405.31799999999998	Waypoint	AV06 4	AV06 4	\N	\N	\N	0101000000B722AB073E94404070052B861F864140	1762
AV06 5	409.404	Waypoint	AV06 5	AV06 5	\N	\N	\N	0101000000CE7BE0863F944040289553AF19864140	1763
AV12	309.66800000000001	Flag	AV12	AV12	\N	\N	\N	0101000000F20E41EC44AB4040879A373A667F4140	1764
AV13	305.10199999999998	Flag	AV13	AV13	\N	\N	\N	01010000003A335C4548AB4040508EA4D27F7F4140	1765
AV14	327.69299999999998	Flag	AV14	AV14	\N	\N	\N	0101000000A090F3B76AAB4040FBF767A67E7F4140	1766
AV15	305.58300000000003	Flag	AV15	AV15	\N	\N	\N	01010000009D15D74C4CAB4040643A23339F7F4140	1767
AV3	318.07999999999998	Flag	AV3	AV3	\N	\N	\N	010100000030A6BCFD89AB40404007CD165F7F4140	1768
AV4	318.56	Flag	AV4	AV4	\N	\N	\N	01010000004729A79164AB404062D8D3A1627F4140	1769
AV4A	321.44400000000002	Flag	AV4A	AV4A	\N	\N	\N	0101000000436173B565AB404087BA844D617F4140	1770
AV5	327.69299999999998	Flag	AV5	AV5	\N	\N	\N	01010000003211A1D780AB40409F772B97427F4140	1771
AV6	317.839	Flag	AV6	AV6	\N	\N	\N	01010000003AFA58F460AB4040010633BA447F4140	1772
AVA06-1	309.66800000000001	Waypoint	AVA06-1	AVA06-1	\N	\N	\N	010100000025C2CB7B38AB4040FFA407296B7F4140	1773
AVA06-1BIS	303.42000000000002	Waypoint	AVA06-1BIS	AVA06-1BIS	\N	\N	\N	010100000093C2F47138AB4040322AC7696A7F4140	1774
AVABM1	318.56	Waypoint	AVABM1	AVABM1	\N	\N	\N	0101000000F483C77263AB4040F774700E697F4140	1775
AVABM1 E	322.40499999999997	Waypoint	AVABM1 E	AVABM1 E	\N	\N	\N	0101000000A640A88363AB404003ED34F0687F4140	1776
AVABM1TRIS	324.08800000000002	Waypoint	AVABM1TRIS	AVABM1TRIS	\N	\N	\N	0101000000B83187EE63AB40401ABA3791697F4140	1777
AVABM2	323.36700000000002	Waypoint	AVABM2	AVABM2	\N	\N	\N	01010000006926105D6DAB40401C037373787F4140	1778
AVABM2 C	321.92500000000001	Waypoint	AVABM2 C	AVABM2 C	\N	\N	\N	01010000002179FB346DAB4040FE99CF26787F4140	1779
AVAGPESTLE	316.15699999999998	Waypoint	AVAGPESTLE	AVAGPESTLE	\N	\N	\N	0101000000074C708260AB40409D02ECE56F7F4140	1780
AVAGS 1 06	321.68400000000003	Waypoint	AVAGS 1 06	AVAGS 1 06	\N	\N	\N	01010000001BA6B31C73AB4040751884D3717F4140	1781
AVALMSTNBW	324.56799999999998	Waypoint	AVALMSTNBW	AVALMSTNBW	\N	\N	\N	010100000000A8F4A969AB404009833FB7687F4140	1782
AVAOATS1	321.20400000000001	Waypoint	AVAOATS1	AVAOATS1	\N	\N	\N	0101000000E013F00D73AB40402FE28C786A7F4140	1783
AVAOATS1B	322.64600000000002	Waypoint	AVAOATS1B	AVAOATS1B	\N	\N	\N	01010000009367246371AB404063246C596D7F4140	1784
AVAOATS1C	321.44400000000002	Waypoint	AVAOATS1C	AVAOATS1C	\N	\N	\N	010100000016CDC8F576AB4040C18193E46E7F4140	1785
AVAOATS1D	321.20400000000001	Waypoint	AVAOATS1D	AVAOATS1D	\N	\N	\N	01010000009C54DB1D77AB40406401D90C6C7F4140	1786
AVAOATS2A	321.92500000000001	Waypoint	AVAOATS2A	AVAOATS2A	\N	\N	\N	01010000007533CC9E7EAB4040486FE8C8727F4140	1787
AVAOATS2B	320.00200000000001	Waypoint	AVAOATS2B	AVAOATS2B	\N	\N	\N	010100000081AB90807EAB4040E0A410986C7F4140	1788
AVAOATS2C	320.72300000000001	Waypoint	AVAOATS2C	AVAOATS2C	\N	\N	\N	0101000000FFDF78417BAB40401FBC4BE9667F4140	1789
AVAOATS2D	321.20400000000001	Waypoint	AVAOATS2D	AVAOATS2D	\N	\N	\N	01010000007F48AB9976AB4040ABBC6B70657F4140	1790
AVAP1	319.762	Flag	AVAP1	AVAP1	\N	\N	\N	0101000000A3F7D2839EAD4040A784D4ABD07F4140	1791
AVAP2	311.11000000000001	Flag	AVAP2	AVAP2	\N	\N	\N	010100000005F0869C0DAE40405DC9ACE31A804140	1792
AVAPESTLE	318.80099999999999	Waypoint	AVAPESTLE	AVAPESTLE	\N	\N	\N	01010000002575904460AB4040A0EDB20E707F4140	1793
AVATR11 SE	319.762	Waypoint	AVATR11 SE	AVATR11 SE	\N	\N	\N	01010000002D5A6CD26EAB40402BD79C02677F4140	1800
AVATR12NE	320.483	Waypoint	AVATR12NE	AVATR12NE	\N	\N	\N	010100000013FCB8FB71AB40405F7FCBC96F7F4140	1803
AVATR12NW	320.964	Waypoint	AVATR12NW	AVATR12NW	\N	\N	\N	010100000026FE4EB16FAB4040CCA2870C717F4140	1804
AVATR12SE	322.16500000000002	Waypoint	AVATR12SE	AVATR12SE	\N	\N	\N	010100000040255B1B72AB40404407858A6F7F4140	1805
AVATR12SW	321.68400000000003	Waypoint	AVATR12SW	AVATR12SW	\N	\N	\N	01010000009A9A18536FAB4040FC7C03AF707F4140	1806
AVATR1SW	317.35899999999998	Waypoint	AVATR1SW	AVATR1SW	\N	\N	\N	010100000042394B7573AB4040E4ACC7FE6B7F4140	1807
AVATR1SW D	317.839	Waypoint	AVATR1SW D	AVATR1SW D	\N	\N	\N	0101000000331643F573AB404040D36BE36A7F4140	1808
AVATR1SWE	320.24200000000002	Waypoint	AVATR1SWE	AVATR1SWE	\N	\N	\N	0101000000A70F805F73AB40403A37CB376B7F4140	1809
AVATR2NE	318.56	Waypoint	AVATR2NE	AVATR2NE	\N	\N	\N	0101000000F3A1B8A171AB404060F38BC16C7F4140	1810
AVATR2NW	320.483	Waypoint	AVATR2NW	AVATR2NW	\N	\N	\N	01010000001A6F307670AB4040C50944366D7F4140	1811
AVATR2SW2	320.00200000000001	Waypoint	AVATR2SW2	AVATR2SW2	\N	\N	\N	0101000000FA8E93226FAB404022AA4B216B7F4140	1812
AVATR2SW A	319.52100000000002	Waypoint	AVATR2SW A	AVATR2SW A	\N	\N	\N	010100000038B540766FAB4040739E44936A7F4140	1813
AVATR3CHAR	322.64600000000002	Waypoint	AVATR3CHAR	AVATR3CHAR	\N	\N	\N	01010000000AACF8936CAB4040530D9DCC6F7F4140	1814
AVATR3SW1	322.16500000000002	Waypoint	AVATR3SW1	AVATR3SW1	\N	\N	\N	0101000000812DF3796CAB4040F36FFFE06F7F4140	1815
AVATR4SW1	319.28100000000001	Waypoint	AVATR4SW1	AVATR4SW1	\N	\N	\N	0101000000C6A9270A6CAB4040607928BB6D7F4140	1816
AVATREEBM	321.20400000000001	Waypoint	AVATREEBM	AVATREEBM	\N	\N	\N	01010000008E798FC6B0AB4040110428435E7F4140	1817
AVK1	296.69	Flag	AVK1	AVK1	\N	\N	\N	01010000002A7F406511B14040369A088D33804140	1818
AVK10	307.745	Flag	AVK10	AVK10	\N	\N	\N	01010000008006C4B9C0B04040A4F55C4B6D7F4140	1819
AVK11	300.29500000000002	Flag	AVK11	AVK11	\N	\N	\N	0101000000F4C77C8CAEB04040FE531429B97F4140	1820
AVK12	295.00799999999998	Flag	AVK12	AVK12	\N	\N	\N	0101000000B16C37D8D3B0404037E0C38AF27F4140	1821
AVK2	279.62700000000001	Flag	AVK2	AVK2	\N	\N	\N	01010000003C4CAF58F9B04040C4317C5830804140	1822
AVK3	276.98399999999998	Flag	AVK3	AVK3	\N	\N	\N	0101000000133F1F953DB1404077C234D474804140	1823
AVK4	267.851	Flag	AVK4	AVK4	\N	\N	\N	01010000005BA7F77854B140407CDC38DB3C804140	1824
AVK5	265.928	Flag	AVK5	AVK5	\N	\N	\N	01010000005BE0583B7DB140404322A78E19804140	1825
AVK6	283.95299999999997	Flag	AVK6	AVK6	\N	\N	\N	0101000000CA1B283C9DB14040E591B425EF7F4140	1826
AVK7	293.80599999999998	Flag	AVK7	AVK7	\N	\N	\N	0101000000BD21C7B565B1404094149FD59E7F4140	1827
AVK8	323.36700000000002	Flag	AVK8	AVK8	\N	\N	\N	0101000000278BE4F356B14040CCAB632A777F4140	1828
AVK9	329.375	Flag	AVK9	AVK9	\N	\N	\N	010100000053C6F77917B14040D51CF05E4D7F4140	1829
AVKX1	276.74299999999999	Flag	AVKX1	AVKX1	\N	\N	\N	0101000000D4F2BC812DB140406D78177B71804140	1830
AV-P1	298.85300000000001	Flag	AV-P1	AV-P1	\N	\N	\N	01010000009D726B2375AE40402FF49AB4EF804140	1833
AV-P2	312.79199999999997	Flag	AV-P2	AV-P2	\N	\N	\N	01010000001DA0D3AA58AE40404225F07FEE804140	1836
AVP3	344.03500000000003	Flag	AVP3	AVP3	\N	\N	\N	01010000005D082D7C21AD40402BABC0FBF07D4140	1837
AV-P3	309.90800000000002	Flag	AV-P3	AV-P3	\N	\N	\N	0101000000D0491F5535AE40403C8150E1FF804140	1838
AVP4	331.29700000000003	Flag	AVP4	AVP4	\N	\N	\N	0101000000852FBB4324AD4040978588312F7E4140	1839
AVP5	316.39699999999999	Flag	AVP5	AVP5	\N	\N	\N	0101000000BA9DC4BA0DAE4040BE3134395D7E4140	1840
AVP6	318.80099999999999	Flag	AVP6	AVP6	\N	\N	\N	010100000030E41896EDAD4040C9FCD660377E4140	1841
AVP7	314.71499999999997	Flag	AVP7	AVP7	\N	\N	\N	0101000000770D116FD1AD4040A8FF606B2C7E4140	1842
AVPT1	318.07999999999998	Flag	AVPT1	AVPT1	\N	\N	\N	0101000000D5B897310EAE40409AFBFA0D927F4140	1843
AVPT2	317.11799999999999	Flag	AVPT2	AVPT2	\N	\N	\N	01010000000F98F4E827AE4040BEC35F26D97F4140	1844
AVQ1	318.07999999999998	Flag	AVQ1	AVQ1	\N	\N	\N	0101000000D8FAB24C70AB4040195A8BB9657F4140	1845
AVR1	311.35000000000002	Flag	AVR1	AVR1	\N	\N	\N	0101000000273DE8E656AB404010886858727F4140	1846
AVR10	317.35899999999998	Flag	AVR10	AVR10	\N	\N	\N	01010000001503B7FD51AB4040D1CE434F457F4140	1847
AVR11	310.149	Flag	AVR11	AVR11	\N	\N	\N	0101000000A49FBFBC3FAB4040881078DC477F4140	1848
AVR12	313.27300000000002	Flag	AVR12	AVR12	\N	\N	\N	0101000000C140E7A051AB4040FBC49CC8557F4140	1849
AVR13	311.83100000000002	Flag	AVR13	AVR13	\N	\N	\N	01010000009D89BB0A41AB4040B56BFB84577F4140	1850
AVR14	310.149	Flag	AVR14	AVR14	\N	\N	\N	01010000001D5ED32D42AB404052A414D7657F4140	1851
AVR15	317.839	Flag	AVR15	AVR15	\N	\N	\N	0101000000A85C97D053AB4040E76B1FBD647F4140	1852
AVR16	325.52999999999997	Flag	AVR16	AVR16	\N	\N	\N	0101000000A5CCE78B66AB4040B3CCCC13627F4140	1853
AVR17	320.00200000000001	Flag	AVR17	AVR17	\N	\N	\N	01010000004D85C4B263AB404005E357E1537F4140	1854
AVR2	320.00200000000001	Flag	AVR2	AVR2	\N	\N	\N	0101000000F586008267AB404055DB73B7707F4140	1855
AVR3	304.38099999999997	Flag	AVR3	AVR3	\N	\N	\N	01010000001979AF1344AB4040072137FB777F4140	1856
AVR4	303.89999999999998	Flag	AVR4	AVR4	\N	\N	\N	0101000000B733079948AB4040BB2653EE867F4140	1857
AVR5	325.04899999999998	Flag	AVR5	AVR5	\N	\N	\N	0101000000F6A093EA6AAB404025994C4A7E7F4140	1858
AVT4	302.45800000000003	Flag	AVT4	AVT4	\N	\N	\N	01010000003C0A9DF8E6AF4040D28E881A26804140	1865
AVT5	309.18700000000001	Flag	AVT5	AVT5	\N	\N	\N	0101000000C999D725ABAF4040D7A0D4034B804140	1866
AVT6	308.226	Flag	AVT6	AVT6	\N	\N	\N	010100000047ECB015B6AF404023022CF7A4804140	1867
AVY1	298.13200000000001	Flag	AVY1	AVY1	\N	\N	\N	01010000001CF328FF94AB40403F889C7250804140	1868
AYANIS TEM	1860.02	Waypoint	AYANIS TEM	AYANIS TEM	\N	\N	\N	0101000000A3001C3E139B4540613AE786AA5A4340	1869
AYPET	406.75999999999999	Flag	AYPET	AYPET	\N	\N	\N	01010000003D6CB79753974040609D90C1BD844140	1870
AYVAR1	327.93299999999999	Waypoint	AYVAR1	AYVAR1	\N	\N	\N	010100000049C66CF46AAB4040702ECBC5827F4140	1871
AYVAR3	317.11799999999999	Waypoint	AYVAR3	AYVAR3	\N	\N	\N	01010000003759B70289AB4040329EAB275F7F4140	1874
AYVAR4	319.28100000000001	Waypoint	AYVAR4	AYVAR4	\N	\N	\N	0101000000F7111BD363AB40406235476A627F4140	1875
AYVAR8	319.28100000000001	Waypoint	AYVAR8	AYVAR8	\N	\N	\N	01010000007EFC7053ADAB40406F01A3F65B7F4140	1876
AYVAR JH1	319.52100000000002	Waypoint	AYVAR JH1	AYVAR JH1	\N	\N	\N	0101000000828D9F5170AB40402A8B044B5C7F4140	1877
AYVAR JH1A	319.041	Waypoint	AYVAR JH1A	AYVAR JH1A	\N	\N	\N	010100000059E6179F6EAB4040FF6C8391597F4140	1878
AYVAR JH2	317.59899999999999	Waypoint	AYVAR JH2	AYVAR JH2	\N	\N	\N	01010000005210977770AB4040B7CEE06B5F7F4140	1879
AYVAR JH3	316.87799999999999	Waypoint	AYVAR JH3	AYVAR JH3	\N	\N	\N	01010000005AE950AE72AB4040320A91F2647F4140	1880
AYVAR JH4	316.39699999999999	Waypoint	AYVAR JH4	AYVAR JH4	\N	\N	\N	010100000091D6732D75AB40402900D35D6A7F4140	1881
AYVAR JH5	316.39699999999999	Waypoint	AYVAR JH5	AYVAR JH5	\N	\N	\N	0101000000AAD134F367AB4040B090B4796E7F4140	1882
AYVAR PLAT	337.786	Waypoint	AYVAR PLAT	AYVAR PLAT	\N	\N	\N	010100000076FBD7B22FAD40406D7FF726F77D4140	1883
AZ04-24	1671.8499999999999	Waypoint	AZ04-24	AZ04-24	\N	\N	\N	01010000009AB1683AFB424040EE994BC8CC1B4240	1884
BARLEY1	59.486800000000002	Waypoint	BARLEY1	BARLEY1	\N	\N	\N	0101000000E148FB7A59A640404CDDD779C2614140	1885
BASEPT2	117.40600000000001	Waypoint	BASEPT2	BASEPT2	\N	\N	\N	01010000002C1DD4C701AC40402B283FBB19614140	1886
BAYRAMIC D	122.453	Waypoint	BAYRAMIC D	BAYRAMIC D	\N	\N	\N	0101000000BC1F1F43338B3A40EA286CDE6FE64340	1887
BU5	45.547899999999998	Waypoint	BU5	BU5	\N	\N	\N	0101000000A5D66371CB1E53C09A48A89A123B4540	1888
CAB 11-26	672.32299999999998	Waypoint	CAB 11-26	CAB 11-26	\N	\N	\N	01010000002C2241F364AA3740FAFB3E7CD63A4440	1889
CAGLAYANCE	1426.47	Waypoint	CAGLAYANCE	CAGLAYANCE	\N	\N	\N	0101000000C1223B46A4A84240DD31B4BBB9E14240	1890
CAT 16-19	1455.0699999999999	Waypoint	CAT 16-19	CAT 16-19	\N	\N	\N	010100000089A3C9CAD1213F405788C0F1F2FB4340	1891
CAT 20-39	1673.05	Waypoint	CAT 20-39	CAT 20-39	\N	\N	\N	01010000000EBC2F6EF0153F403816F8B075FA4340	1892
CAT CAMP	1569.23	Waypoint	CAT CAMP	CAT CAMP	\N	\N	\N	0101000000DB1448B6DE213F4061CC4766F6FB4340	1893
CAVE	317.59899999999999	Flag	CAVE	CAVE	\N	\N	\N	0101000000D589C437E6B24040413407C9817D4140	1894
CEMETARY	464.67899999999997	Waypoint	CEMETARY	CEMETARY	\N	\N	\N	01010000009C5242D6112C53C04099283D31364540	1895
CFS-1	1564.4200000000001	Waypoint	CFS-1	CFS-1	\N	\N	\N	0101000000EDCE38DF587F404000BEFF82697A4140	1896
CFS10	1544.95	Waypoint	CFS10	CFS10	\N	\N	\N	0101000000CF6C5CA1BD7F4040BEFE7FF7297A4140	1897
CFS11	1559.3699999999999	Waypoint	CFS11	CFS11	\N	\N	\N	0101000000B229E069357F4040672A87C0587A4140	1898
CFS2	1556.97	Waypoint	CFS2	CFS2	\N	\N	\N	0101000000B8A3D71D4D7F4040FE7DA0BE607A4140	1899
CFS3	1554.0899999999999	Waypoint	CFS3	CFS3	\N	\N	\N	0101000000620D9BF14B7F40401F9B63C7667A4140	1900
CFS-4	1557.45	Waypoint	CFS-4	CFS-4	\N	\N	\N	0101000000EAA5573A477F4040E33F4725617A4140	1901
CFS-5	1561.78	Waypoint	CFS-5	CFS-5	\N	\N	\N	01010000005729E41A447F4040DC40B468577A4140	1902
CFS6	1559.8499999999999	Waypoint	CFS6	CFS6	\N	\N	\N	01010000002C11F945417F4040C53674765C7A4140	1903
CFS7	1559.6099999999999	Waypoint	CFS7	CFS7	\N	\N	\N	0101000000F1F8983D407F4040429A28775C7A4140	1904
CFS-8	1560.0899999999999	Waypoint	CFS-8	CFS-8	\N	\N	\N	010100000098AE3C55397F40409E1A0715577A4140	1905
CFS-9	1549.76	Waypoint	CFS-9	CFS-9	\N	\N	\N	0101000000F8133F0D327F40402AF0B8225D7A4140	1906
CHF1	547.35199999999998	Waypoint	CHF1	CHF1	\N	\N	\N	01010000006878D9CD11A73940721C3B5204894140	1907
CHF10	569.46199999999999	Waypoint	CHF10	CHF10	\N	\N	\N	01010000002692B0BBB5A639403BDCFFE1DD884140	1908
CHF11	632.66899999999998	Waypoint	CHF11	CHF11	\N	\N	\N	0101000000135546C3ACA6394081431F61D5884140	1909
CHF13	565.37699999999995	Waypoint	CHF13	CHF13	\N	\N	\N	0101000000989791B4FAA63940E258A0A8EF884140	1910
CHF14	583.88199999999995	Waypoint	CHF14	CHF14	\N	\N	\N	0101000000A7459176A2A63940D9C85CB6E9884140	1911
CHF2	551.678	Waypoint	CHF2	CHF2	\N	\N	\N	010100000051D4E8C11CA73940124B53CF05894140	1912
CHF3	547.59299999999996	Waypoint	CHF3	CHF3	\N	\N	\N	01010000002BF7FDF908A73940F22A33F103894140	1913
CHF4	542.06500000000005	Waypoint	CHF4	CHF4	\N	\N	\N	01010000009BEE260C0BA73940151387AB04894140	1914
CHF5	550.95699999999999	Waypoint	CHF5	CHF5	\N	\N	\N	010100000045F377241BA739408815C4F901894140	1915
CHF6	550.95699999999999	Waypoint	CHF6	CHF6	\N	\N	\N	010100000041166A9901A73940353C83A70C894140	1916
CHUR	250.547	Flag	CHUR	CHUR	\N	\N	\N	01010000007EE0501603B340400649BBAB807E4140	1917
CHURCH	106.831	Building	CHURCH	CHURCH	\N	\N	\N	0101000000D3D41027406D3B40053BAFC5AF034340	1918
CIGLIKARA	1827.8199999999999	Waypoint	CIGLIKARA	CIGLIKARA	\N	\N	\N	0101000000A7F0215A0BC63D4005586FCA18404240	1919
CKB	45.307499999999997	Waypoint	CKB	CKB	\N	\N	\N	0101000000E414E0654D613A406F2E9B8CEC124440	1920
CKF1	1205.1300000000001	Waypoint	CKF1	CKF1	\N	\N	\N	01010000001C3741439BD73840E040E3EA89954140	1921
CKF-10	1223.8699999999999	Waypoint	CKF-10	CKF-10	\N	\N	\N	010100000090C2DE8C66D738402024B0DE8D954140	1922
CKF-11	1225.0799999999999	Waypoint	CKF-11	CKF-11	\N	\N	\N	01010000004E7D207967D7384081F8D07090954140	1923
CKF12	1319.53	Waypoint	CKF12	CKF12	\N	\N	\N	01010000000D4919B065D73840F334FB648E954140	1924
CKF-13	1226.28	Waypoint	CKF-13	CKF-13	\N	\N	\N	01010000009F397EB75FD73840722603C68E954140	1925
CKF14	1226.76	Waypoint	CKF14	CKF14	\N	\N	\N	0101000000293D1FDA51D7384037F66B3977954140	1926
CKF-15	1298.1400000000001	Waypoint	CKF-15	CKF-15	\N	\N	\N	0101000000C64649DD08D7384045024D519C964140	1927
CKF-16	1319.53	Waypoint	CKF-16	CKF-16	\N	\N	\N	0101000000DA1CB7FD4AD7384010FCDAC07E964140	1928
CKF 17	1318.3199999999999	Waypoint	CKF 17	CKF 17	\N	\N	\N	0101000000355870F351D73840419030CA7B964140	1929
CKF18	1315.9200000000001	Waypoint	CKF18	CKF18	\N	\N	\N	0101000000B592D7FC58D73840F5BA3BEE78964140	1930
CKF2	1218.3499999999999	Waypoint	CKF2	CKF2	\N	\N	\N	01010000002B9C283288D738404DEA3B278C954140	1931
CKF3	1210.9000000000001	Waypoint	CKF3	CKF3	\N	\N	\N	0101000000EA6EA1A29FD738401B33779785954140	1932
CKF4	1214.26	Waypoint	CKF4	CKF4	\N	\N	\N	0101000000557D906088D738408EF5305B82954140	1933
CKF-5	1204.1700000000001	Waypoint	CKF-5	CKF-5	\N	\N	\N	0101000000276F8FC49CD738407AD0148385954140	1934
CKF6	1213.54	Waypoint	CKF6	CKF6	\N	\N	\N	01010000004AB390B87BD7384044D7B81081954140	1935
CKF7	1209.21	Waypoint	CKF7	CKF7	\N	\N	\N	010100000067BA078393D7384049D66F9382954140	1936
CKF8	1206.5699999999999	Waypoint	CKF8	CKF8	\N	\N	\N	01010000008FAC6E8880D73840CE205C067E954140	1937
CKF-9	1202.25	Waypoint	CKF-9	CKF-9	\N	\N	\N	0101000000C005FAC065D738409BDB1F507F954140	1938
CKF FUTURE	1335.1500000000001	Waypoint	CKF FUTURE	CKF FUTURE	\N	\N	\N	0101000000A372DEC2E4D73840731F6FA84E964140	1939
CKF PARK	1220.75	Waypoint	CKF PARK	CKF PARK	\N	\N	\N	0101000000220D41EA03D7384063EFE62596954140	1940
CKL10	20.7941	Waypoint	CKL10	CKL10	\N	\N	\N	0101000000048A906E4F29404046DCC41254784140	1941
CKL12	24.398900000000001	Waypoint	CKL12	CKL12	\N	\N	\N	01010000001F9D64F260294040BF0EBD414B784140	1942
CKL3	24.1586	Waypoint	CKL3	CKL3	\N	\N	\N	01010000004CB52B016C294040232B18C54D784140	1943
CKL4	23.4376	Waypoint	CKL4	CKL4	\N	\N	\N	01010000004A2FB3CD6229404075D354B94A784140	1944
CKL5	27.0426	Waypoint	CKL5	CKL5	\N	\N	\N	01010000006318BC5749294040BA84B00B60784140	1945
CKL6	20.313400000000001	Waypoint	CKL6	CKL6	\N	\N	\N	010100000041411FE84529404040AF4F6B60784140	1946
CKL7	18.871500000000001	Waypoint	CKL7	CKL7	\N	\N	\N	010100000010E564125029404022F2147466784140	1947
CKL8	29.445900000000002	Waypoint	CKL8	CKL8	\N	\N	\N	0101000000745557404C294040F191209E5D784140	1948
CKL9	11.180899999999999	Waypoint	CKL9	CKL9	\N	\N	\N	01010000001D8BD07C4C2940405805281566784140	1949
CKM1-9	253.91200000000001	Waypoint	CKM1-9	CKM1-9	\N	\N	\N	0101000000BF19CF7CDBDB3940FFD5E006878F4140	1950
CLA07	16.4681	Waypoint	CLA07	CLA07	\N	\N	\N	0101000000E69D4061DB274040A0C860B3067A4140	1951
CLA-1	20.7941	Waypoint	CLA-1	CLA-1	\N	\N	\N	0101000000C0B2D75DCE274040F7C26C1B297A4140	1952
CLA10	15.2666	Waypoint	CLA10	CLA10	\N	\N	\N	010100000087DD0F29DA27404083023302117A4140	1953
CLA11	19.592400000000001	Waypoint	CLA11	CLA11	\N	\N	\N	0101000000FF303FC0E527404049909840147A4140	1954
CLA12	17.429600000000001	Waypoint	CLA12	CLA12	\N	\N	\N	0101000000840C002FD52740409807F59C107A4140	1955
CLA13	17.9102	Waypoint	CLA13	CLA13	\N	\N	\N	0101000000C6DDF020D52740407077C7430E7A4140	1956
CLA14	20.0731	Waypoint	CLA14	CLA14	\N	\N	\N	0101000000046D3394E1274040E9B5D49F0F7A4140	1957
CLA15	20.7941	Waypoint	CLA15	CLA15	\N	\N	\N	010100000043107DFDE427404002914852077A4140	1958
CLA16	10.460000000000001	Waypoint	CLA16	CLA16	\N	\N	\N	01010000001E7A88CFE827404003E8180C057A4140	1959
CLA18	14.545500000000001	Waypoint	CLA18	CLA18	\N	\N	\N	0101000000F050F3D9EA2740403142BF89FD794140	1960
CLA19	25.840900000000001	Waypoint	CLA19	CLA19	\N	\N	\N	01010000005AAD586B042840404FD6E7EBEB794140	1961
CLA2	24.639299999999999	Waypoint	CLA2	CLA2	\N	\N	\N	01010000001A540400D3274040182BEBB0227A4140	1962
CLA-2	23.4376	Waypoint	CLA-2	CLA-2	\N	\N	\N	0101000000734F6B5BD32740408380D008237A4140	1963
CLA20	16.4681	Waypoint	CLA20	CLA20	\N	\N	\N	010100000097F282D1EA274040F75187C1EE794140	1964
CLA-21	23.4376	Waypoint	CLA-21	CLA-21	\N	\N	\N	01010000009FD6740AE0274040744A3322007A4140	1965
CLA-3	21.755400000000002	Waypoint	CLA-3	CLA-3	\N	\N	\N	01010000001783F405CE274040831DC75B237A4140	1966
CLA-4	20.553699999999999	Waypoint	CLA-4	CLA-4	\N	\N	\N	010100000080419343D2274040F4FFDA5C1D7A4140	1967
CLA5	21.995699999999999	Waypoint	CLA5	CLA5	\N	\N	\N	01010000007E705F83D52740408BA78B051F7A4140	1968
CLA6	19.832799999999999	Waypoint	CLA6	CLA6	\N	\N	\N	010100000098AB7F0DD12740403A4D43AD197A4140	1969
CLA7	21.755400000000002	Waypoint	CLA7	CLA7	\N	\N	\N	010100000023DB6BD4D2274040B351708D1C7A4140	1970
CLA8	21.755400000000002	Waypoint	CLA8	CLA8	\N	\N	\N	0101000000FF819031D9274040B4DFE7B3147A4140	1971
CLA9	19.111799999999999	Waypoint	CLA9	CLA9	\N	\N	\N	01010000000D023040D0274040DF7D6F82187A4140	1972
CLK1	25.120000000000001	Waypoint	CLK1	CLK1	\N	\N	\N	0101000000FD195F2D572940400DA4F33060784140	1973
CLK11	22.957000000000001	Waypoint	CLK11	CLK11	\N	\N	\N	0101000000C3204B47422940403D6D94C26A784140	1974
CLK2	26.081199999999999	Waypoint	CLK2	CLK2	\N	\N	\N	01010000007F65705759294040A1BA176A5D784140	1975
CMH15	510.34199999999998	Waypoint	CMH15	CMH15	\N	\N	\N	0101000000A4746F99E18B4040B163981DA6834140	1976
CMH17	498.80599999999998	Waypoint	CMH17	CMH17	\N	\N	\N	0101000000504C741CD38B404058951FE682834140	1977
CMH3	501.69	Waypoint	CMH3	CMH3	\N	\N	\N	0101000000D80121A3CD8B40408457F365A7834140	1978
CMH7	517.79200000000003	Waypoint	CMH7	CMH7	\N	\N	\N	010100000027CBB8C5D68B4040E7C7FC2F99834140	1979
CMM1	455.78699999999998	Waypoint	CMM1	CMM1	\N	\N	\N	0101000000A2AF283EA18D40403EBD03508D854140	1980
CMM2	456.26799999999997	Waypoint	CMM2	CMM2	\N	\N	\N	0101000000CD0D2D82A48D40409AFD5E6387854140	1981
CPL1	1123.4200000000001	Waypoint	CPL1	CPL1	\N	\N	\N	0101000000C0F75A77C37640402A8A437534794140	1982
CPL10	1116.6900000000001	Waypoint	CPL10	CPL10	\N	\N	\N	0101000000B967F3BEC57640409BEF969A33794140	1983
CPL11	1111.8800000000001	Waypoint	CPL11	CPL11	\N	\N	\N	0101000000217A4043B97640407A32A42F29794140	1984
CPL12	1130.8699999999999	Waypoint	CPL12	CPL12	\N	\N	\N	0101000000FCC0B8C8BB764040D3101B4D2A794140	1985
CPL2	1133.75	Waypoint	CPL2	CPL2	\N	\N	\N	01010000009C6FD75ABE7640400BF09BCA3B794140	1986
CPL3	1125.5799999999999	Waypoint	CPL3	CPL3	\N	\N	\N	0101000000D20BC004C3764040B1E0204F42794140	1987
CPL4	517.79200000000003	Waypoint	CPL4	CPL4	\N	\N	\N	0101000000024A0BE3E37640407E3D94A52C794140	1988
CPL5	1118.1300000000001	Waypoint	CPL5	CPL5	\N	\N	\N	0101000000912B50E6BA764040C31F181C32794140	1989
CPL6	1110.9200000000001	Waypoint	CPL6	CPL6	\N	\N	\N	0101000000FB69E80EBE7640409B47684540794140	1990
CPL7	1205.3699999999999	Waypoint	CPL7	CPL7	\N	\N	\N	010100000064DF27A4BB764040BA41BCC73C794140	1991
CPL8	1118.3699999999999	Waypoint	CPL8	CPL8	\N	\N	\N	010100000031BA3801B8764040AD9DAC412D794140	1992
CPL9	1132.55	Waypoint	CPL9	CPL9	\N	\N	\N	0101000000478A9877B4764040DEFDA01736794140	1993
CR0SSN	301.97800000000001	Flag	CR0SSN	CR0SSN	\N	\N	\N	010100000031CD071365AB4040B8A704F41D7E4140	1994
CR1	320.00200000000001	Flag	CR1	CR1	\N	\N	\N	0101000000F79AF0DB68AB4040A3445F02767F4140	1995
CR2	321.68400000000003	Flag	CR2	CR2	\N	\N	\N	0101000000B27B2F1469AB4040AEC85CD7777F4140	1996
CR3	328.654	Flag	CR3	CR3	\N	\N	\N	01010000000E2E13016BAB4040DADAA49D787F4140	1997
CR4	316.15699999999998	Flag	CR4	CR4	\N	\N	\N	0101000000DA10235156AB404015CD384A747F4140	1998
CR4A	316.15699999999998	Flag	CR4A	CR4A	\N	\N	\N	01010000008304BD1E69AB4040DB220355707F4140	1999
CR5	316.87799999999999	Flag	CR5	CR5	\N	\N	\N	01010000006E7FF46E68AB40407A770BF46E7F4140	2000
CR6	316.15699999999998	Flag	CR6	CR6	\N	\N	\N	0101000000189520986DAB4040F4EFF8CB6E7F4140	2001
CRV	185.178	Waypoint	CRV	CRV	\N	\N	\N	010100000083B5DE2D18C03940D55DD44C09884140	2002
CSF1	1005.9	Waypoint	CSF1	CSF1	\N	\N	\N	010100000095BDF6E3AE8839400E549887248C4140	2003
CSF10	1005.9	Waypoint	CSF10	CSF10	\N	\N	\N	01010000001B7547DB8588394006D48C26468C4140	2004
CSF-11	1051.3199999999999	Waypoint	CSF-11	CSF-11	\N	\N	\N	010100000093C1F638788839404F9D909B368C4140	2005
CSF12	1009.98	Waypoint	CSF12	CSF12	\N	\N	\N	0101000000A055C1035E88394012DB6BAE0B8C4140	2006
CSF13	1170.52	Waypoint	CSF13	CSF13	\N	\N	\N	0101000000787F381F09863940B9CEB7ECAD8C4140	2007
CSF14	1182.54	Waypoint	CSF14	CSF14	\N	\N	\N	010100000059AEF631188639409F8ABB44AC8C4140	2008
CSF15	1174.6099999999999	Waypoint	CSF15	CSF15	\N	\N	\N	0101000000F95C089A1886394033018C55AD8C4140	2009
CSF16	1171.96	Waypoint	CSF16	CSF16	\N	\N	\N	0101000000FE21DFA01B863940D06770F6AF8C4140	2010
CSF17	1184.46	Waypoint	CSF17	CSF17	\N	\N	\N	0101000000482B59760A8639403296A77BB68C4140	2011
CSF18	1175.8099999999999	Waypoint	CSF18	CSF18	\N	\N	\N	0101000000643117DC028639407CAE7CB7B58C4140	2012
CSF19	1189.75	Waypoint	CSF19	CSF19	\N	\N	\N	0101000000734C206913863940CEFC8B1CB98C4140	2013
CSF2	1175.8099999999999	Waypoint	CSF2	CSF2	\N	\N	\N	01010000006DCB8FDEC1883940250A41CF258C4140	2014
CSF20	1185.6600000000001	Waypoint	CSF20	CSF20	\N	\N	\N	0101000000BB61E05B068639408401A71EB98C4140	2015
CSF3	1053.24	Waypoint	CSF3	CSF3	\N	\N	\N	01010000004827BF038B88394019E4B7B3328C4140	2016
CSF4	1053.24	Waypoint	CSF4	CSF4	\N	\N	\N	0101000000DD3A793D8A88394010E08F03388C4140	2017
CSF5	1056.6099999999999	Waypoint	CSF5	CSF5	\N	\N	\N	0101000000F11E1EC479883940AAF200C0388C4140	2018
CSF6	1053	Waypoint	CSF6	CSF6	\N	\N	\N	0101000000F181277179883940CA9227B33B8C4140	2019
CSF7	1036.4200000000001	Waypoint	CSF7	CSF7	\N	\N	\N	01010000000BB20FF981883940E78167F93C8C4140	2020
CSF8	1015.51	Waypoint	CSF8	CSF8	\N	\N	\N	0101000000D9CC7F1A87883940C9ED3E974E8C4140	2021
CSF9	1062.6099999999999	Waypoint	CSF9	CSF9	\N	\N	\N	0101000000A496D92C868839406F03B34C438C4140	2022
CSF PARK	938.60599999999999	Waypoint	CSF PARK	CSF PARK	\N	\N	\N	01010000001373E8ABE68939407E864495F28B4140	2023
CTC100	1853.77	Waypoint	CTC100	CTC100	\N	\N	\N	01010000003C401BAC046E40402991C86747774140	2024
CTC101	1853.77	Waypoint	CTC101	CTC101	\N	\N	\N	01010000001C376CC3F76D40401469738046774140	2025
CTC102	1853.77	Waypoint	CTC102	CTC102	\N	\N	\N	0101000000EF0CED78E06D4040BC1FF4C256774140	2026
CTC130	1806.4300000000001	Waypoint	CTC130	CTC130	\N	\N	\N	0101000000FC0700AA126F404088014DAF4D784140	2027
CTC131	1777.8299999999999	Waypoint	CTC131	CTC131	\N	\N	\N	0101000000342F03A5136F4040770435514B784140	2028
CTC132	1777.8299999999999	Waypoint	CTC132	CTC132	\N	\N	\N	010100000026AF2432D97440401B6ABBD9C4844140	2029
CTC133	1792.49	Waypoint	CTC133	CTC133	\N	\N	\N	0101000000D0B53459116F4040590ABD0857784140	2030
CTC135	1816.28	Waypoint	CTC135	CTC135	\N	\N	\N	0101000000C99CF7970E6F4040B752F34848784140	2031
CTC136	1793.45	Waypoint	CTC136	CTC136	\N	\N	\N	010100000094F26CEC186F4040C8D8E0AF4F784140	2032
CTC137	1809.55	Waypoint	CTC137	CTC137	\N	\N	\N	01010000004BF007B5FF6E4040FDE23BF940784140	2033
CTC138	1810.03	Waypoint	CTC138	CTC138	\N	\N	\N	010100000050EC78FEFA6E4040AFCEC4834C784140	2034
CTC139	1825.9000000000001	Waypoint	CTC139	CTC139	\N	\N	\N	0101000000959CF725F96E4040E4FB8E5347784140	2035
CTC140	1793.45	Waypoint	CTC140	CTC140	\N	\N	\N	010100000021207B97F46E40400EDAB04841784140	2036
CTC141	1840.8	Waypoint	CTC141	CTC141	\N	\N	\N	0101000000D9CA371A016F404078F8B488FD774140	2037
CTC142	1828.0599999999999	Waypoint	CTC142	CTC142	\N	\N	\N	0101000000F7076CFCF96E40403DADD0771F784140	2038
CTC143	1842.24	Waypoint	CTC143	CTC143	\N	\N	\N	0101000000DE0F080C036F40402604E0DCF5774140	2039
CTC144	1822.77	Waypoint	CTC144	CTC144	\N	\N	\N	0101000000B3E0AB410B6F40404B17DCBB38784140	2040
CTC145	1830.46	Waypoint	CTC145	CTC145	\N	\N	\N	010100000057515BA10A6F4040C0688C5F03784140	2041
CTC146	1830.46	Waypoint	CTC146	CTC146	\N	\N	\N	01010000000DD66F8E096F4040F4C1DC35FB774140	2042
CTC147	1841.52	Waypoint	CTC147	CTC147	\N	\N	\N	0101000000ED729316036F40404216B36FF8774140	2043
CTC150	1725.2	Waypoint	CTC150	CTC150	\N	\N	\N	010100000016A65CD232704040E22B03CC11764140	2044
CTC-151	1717.75	Waypoint	CTC-151	CTC-151	\N	\N	\N	01010000003D03CC770E70404056F01874FB754140	2045
CTC152	1735.77	Waypoint	CTC152	CTC152	\N	\N	\N	0101000000AA411C1422704040F4760FC60B764140	2046
CTC153	1720.3900000000001	Waypoint	CTC153	CTC153	\N	\N	\N	010100000095FB106000704040230B899500764140	2047
CTC-154	1711.74	Waypoint	CTC-154	CTC-154	\N	\N	\N	01010000003AD548B5097040400B0FEBA4F6754140	2048
CTC155	1724.72	Waypoint	CTC155	CTC155	\N	\N	\N	01010000002EB2F8A81A704040BD92C8640F764140	2049
CTC156	1715.3399999999999	Waypoint	CTC156	CTC156	\N	\N	\N	0101000000478C8F30FB6F40404F2EAC6CF6754140	2050
CTC-157	1715.3399999999999	Waypoint	CTC-157	CTC-157	\N	\N	\N	010100000065C7672E077040400382E0C1F4754140	2051
CTC158	1761.97	Waypoint	CTC158	CTC158	\N	\N	\N	0101000000D52F182902704040744328F61D764140	2052
CTC159	1717.51	Waypoint	CTC159	CTC159	\N	\N	\N	01010000003FE2940FFA6F4040302B7CCCF3754140	2053
CTC-160	1708.6199999999999	Waypoint	CTC-160	CTC-160	\N	\N	\N	010100000096552753047040404A0C6FC7F5754140	2054
CTC161	1748.03	Waypoint	CTC161	CTC161	\N	\N	\N	010100000049F2BA5009704040B2FDEF7E18764140	2055
CTC162	1768.22	Waypoint	CTC162	CTC162	\N	\N	\N	0101000000E1CCCB3BF06F4040EC98B37116764140	2056
CTC-163	1702.6099999999999	Waypoint	CTC-163	CTC-163	\N	\N	\N	0101000000CA3DCC7A0B70404073CDA07EF0754140	2057
CTC165	1787.4400000000001	Waypoint	CTC165	CTC165	\N	\N	\N	01010000000BFC4BCCDF6F4040562E1C5417764140	2058
CTC-166	1705.97	Waypoint	CTC-166	CTC-166	\N	\N	\N	01010000008C29D76217704040EFBC9487F3754140	2059
CTC167	1748.03	Waypoint	CTC167	CTC167	\N	\N	\N	010100000035E8EBFCD26F4040C0EE167608764140	2060
CTC168	1806.4300000000001	Waypoint	CTC168	CTC168	\N	\N	\N	01010000003F7F448DAE6F40406DB51C220D764140	2061
CTC-169	1775.6700000000001	Waypoint	CTC-169	CTC-169	\N	\N	\N	0101000000181ABF58EE6F4040E1A667C31F764140	2062
CTC170	1775.4300000000001	Waypoint	CTC170	CTC170	\N	\N	\N	0101000000416F0B45E36F4040ED03984B0D764140	2063
CTC171	1848.01	Waypoint	CTC171	CTC171	\N	\N	\N	01010000005AC28BD2E66D404056CB386E3A774140	2064
CTC-172	1832.3800000000001	Waypoint	CTC-172	CTC-172	\N	\N	\N	0101000000C0DA9F2BD46D4040B0E38F073A774140	2065
CTC173	1775.4300000000001	Waypoint	CTC173	CTC173	\N	\N	\N	01010000007106D006D86D4040A7B19C084B774140	2066
CTC174	1870.3599999999999	Waypoint	CTC174	CTC174	\N	\N	\N	010100000090F5EB86E16D40402001857541774140	2067
CTC-175	1850.1700000000001	Waypoint	CTC-175	CTC-175	\N	\N	\N	0101000000FA809B20C56D40402BED3A3F38774140	2068
CTC176	1775.4300000000001	Waypoint	CTC176	CTC176	\N	\N	\N	01010000007106D006D86D4040A7B19C084B774140	2069
CTC177	1852.3299999999999	Waypoint	CTC177	CTC177	\N	\N	\N	01010000001F44DCE3DF6D4040105E830A43774140	2070
CTC-178	1841.04	Waypoint	CTC-178	CTC-178	\N	\N	\N	01010000006A0CDF67C16D404090C36F2938774140	2071
CTC179	1859.3	Waypoint	CTC179	CTC179	\N	\N	\N	0101000000E265A4E3CC6D4040AAEDB4A23E774140	2072
CTC180	1867.71	Waypoint	CTC180	CTC180	\N	\N	\N	0101000000292BF08FE36D4040BA6AD31542774140	2073
CTC-181	1837.9100000000001	Waypoint	CTC-181	CTC-181	\N	\N	\N	01010000006C5AD303B96D404050832F6D34774140	2074
CTC182	1859.54	Waypoint	CTC182	CTC182	\N	\N	\N	0101000000A0F6DF73B56D4040466E50723C774140	2075
CTC183	1870.3599999999999	Waypoint	CTC183	CTC183	\N	\N	\N	0101000000E9704CCDE06D4040C708645347774140	2076
CTC184	1848.97	Waypoint	CTC184	CTC184	\N	\N	\N	01010000001B1367E7226E40400771CC4F3D774140	2077
CTC-185	1851.3699999999999	Waypoint	CTC-185	CTC-185	\N	\N	\N	0101000000B4BBD0F4436E4040CCF5629A3C774140	2078
CTC186	1867.95	Waypoint	CTC186	CTC186	\N	\N	\N	0101000000B9CD7797DB6D4040F2E0CB9D49774140	2079
CTC2007 10	1689.6300000000001	Waypoint	CTC2007 10	CTC2007 10	\N	\N	\N	01010000003E0724CE7B724040F91D0781FC774140	2080
CTC2007 11	1684.8199999999999	Waypoint	CTC2007 11	CTC2007 11	\N	\N	\N	010100000040BEA05F7D724040F2A1B3E8F7774140	2081
CTC2007 12	1679.0599999999999	Waypoint	CTC2007 12	CTC2007 12	\N	\N	\N	0101000000085184F57B7240403B14E7A3EA774140	2082
CTC2007 9	1697.8	Waypoint	CTC2007 9	CTC2007 9	\N	\N	\N	010100000033EF0BC47F724040ABC6D37103784140	2083
CTC51	1583.8900000000001	Waypoint	CTC51	CTC51	\N	\N	\N	0101000000F53834AE7D71404089A980BD3A764140	2084
CTC52	1583.4100000000001	Waypoint	CTC52	CTC52	\N	\N	\N	0101000000CB7A5FCC7E714040AC88F85935764140	2085
CTC54	1583.8900000000001	Waypoint	CTC54	CTC54	\N	\N	\N	010100000008066BF746724040124504C0D1764140	2086
CTC56	1574.03	Waypoint	CTC56	CTC56	\N	\N	\N	0101000000EFC584896072404063228C3CDC764140	2089
CTC57	1549.04	Waypoint	CTC57	CTC57	\N	\N	\N	0101000000FDE8B0CF57724040B687E8D0DC764140	2090
CTC-58	1612.48	Waypoint	CTC-58	CTC-58	\N	\N	\N	0101000000BBD9BCFD3B724040DC64BCFCFA764140	2091
CTC59	1645.1700000000001	Waypoint	CTC59	CTC59	\N	\N	\N	01010000005DD6AB653E7240404ED16B951B774140	2092
CTC60 JDW	1642.77	Waypoint	CTC60 JDW	CTC60 JDW	\N	\N	\N	010100000061DB1C933772404042DF43AD1C774140	2093
CTC61	1563.9400000000001	Waypoint	CTC61	CTC61	\N	\N	\N	0101000000DCF3223146724040A7FDB33FC0764140	2094
CTC62	1564.1800000000001	Waypoint	CTC62	CTC62	\N	\N	\N	01010000000DDAB30050724040CCCA4FDAB6764140	2095
CTC63	1586.77	Waypoint	CTC63	CTC63	\N	\N	\N	0101000000C12A8B8052724040D7CE53C4B9764140	2096
CTC64	1589.8900000000001	Waypoint	CTC64	CTC64	\N	\N	\N	0101000000CE8E177C6172404004F252D5B7764140	2097
CTC65	1617.53	Waypoint	CTC65	CTC65	\N	\N	\N	01010000001FEFDE1C71724040E907A882A7764140	2098
CTC-67	423.58300000000003	Waypoint	CTC-67	CTC-67	\N	\N	\N	010100000015999041347140402B0C7836F7754140	2099
CTC-69	1594.9400000000001	Waypoint	CTC-69	CTC-69	\N	\N	\N	0101000000008DACDE5C714040A81EDBC503764140	2102
CTC70	1619.21	Waypoint	CTC70	CTC70	\N	\N	\N	0101000000D8A167A147714040D587F34502764140	2103
CTC71	1596.3800000000001	Waypoint	CTC71	CTC71	\N	\N	\N	010100000073164CB57C714040E0962F32F8754140	2104
CTC72	1619.21	Waypoint	CTC72	CTC72	\N	\N	\N	01010000004AF6DFB551714040518BFB6EFE754140	2105
CTC73	1603.1099999999999	Waypoint	CTC73	CTC73	\N	\N	\N	0101000000208E5CD47A714040AC376013F6754140	2106
CTC80	1842.24	Waypoint	CTC80	CTC80	\N	\N	\N	0101000000AF2B54A17B6F404024F73CCBE8764140	2107
CTC82	1850.8900000000001	Waypoint	CTC82	CTC82	\N	\N	\N	010100000081F00670716F40406D4C5C82E4764140	2110
CTC83	1831.9000000000001	Waypoint	CTC83	CTC83	\N	\N	\N	010100000047082C0C936F4040D2FF0A4AE5764140	2111
CTC84	1837.6700000000001	Waypoint	CTC84	CTC84	\N	\N	\N	0101000000A73264F3736F4040A732DB01D6764140	2112
CTC85	1839.5899999999999	Waypoint	CTC85	CTC85	\N	\N	\N	0101000000563F48AC8B6F404075E7FB3CD5764140	2113
CTC86	1856.4200000000001	Waypoint	CTC86	CTC86	\N	\N	\N	0101000000C4FC8E41646E404050C48F224C774140	2114
CTC87	1867.95	Waypoint	CTC87	CTC87	\N	\N	\N	0101000000CB1D84200A6E40401C3C97D248774140	2115
CTC88	1867.23	Waypoint	CTC88	CTC88	\N	\N	\N	01010000007FA060EF136E4040CCAAA70D49774140	2116
CTC89	1861.46	Waypoint	CTC89	CTC89	\N	\N	\N	0101000000921914720A6E4040BFB87F254A774140	2117
CTC90	1868.6700000000001	Waypoint	CTC90	CTC90	\N	\N	\N	01010000008C15C8FB176E4040A463CC324B774140	2118
CTC91	1869.4000000000001	Waypoint	CTC91	CTC91	\N	\N	\N	0101000000253FB7D70F6E40406DD99BC452774140	2119
CTC92	1862.4300000000001	Waypoint	CTC92	CTC92	\N	\N	\N	010100000026E21F49186E4040BD640C4148774140	2120
CTC93	1869.1500000000001	Waypoint	CTC93	CTC93	\N	\N	\N	0101000000DD3D0B05166E4040722748D45A774140	2121
CTC95	1885.02	Waypoint	CTC95	CTC95	\N	\N	\N	0101000000C83FBC792D6E404065F2635257774140	2122
CTC CO1	1726.8800000000001	Waypoint	CTC CO1	CTC CO1	\N	\N	\N	010100000082696C3D3070404046FFFEA60D764140	2123
CTC JEN	1638.4400000000001	Waypoint	CTC JEN	CTC JEN	\N	\N	\N	0101000000AA50D0327A7240404FFE936442774140	2124
CTM1	161.86600000000001	Waypoint	CTM1	CTM1	\N	\N	\N	010100000008E0067F36373A40CB348879509C4140	2125
CTM10	156.09899999999999	Waypoint	CTM10	CTM10	\N	\N	\N	01010000000A32115549373A409D89908A649C4140	2126
CTV42	1189.75	Waypoint	CTV42	CTV42	\N	\N	\N	01010000009A9CF3B6C65040407AAC073668844140	2127
CVP-1	1333.22	Waypoint	CVP-1	CVP-1	\N	\N	\N	010100000072AE9678161B3840100B61A7EBA04140	2128
CVP-2	1335.1500000000001	Waypoint	CVP-2	CVP-2	\N	\N	\N	0101000000387FB8501E1B3840272420C6EEA04140	2129
CVP PARK	1597.3399999999999	Waypoint	CVP PARK	CVP PARK	\N	\N	\N	01010000004474688E3516384060D5E41F39A24140	2130
CVPSTUMP	1334.6700000000001	Waypoint	CVPSTUMP	CVPSTUMP	\N	\N	\N	010100000074161E7D111B38404B3D78DEE7A04140	2131
D1	116.925	Waypoint	D1	D1	\N	\N	\N	0101000000BF9241745C5BEFBFF84A23DD44544A40	2132
D2	139.756	Waypoint	D2	D2	\N	\N	\N	0101000000AE9B4333FE36EFBF98C38B118B534A40	2133
D3	162.34700000000001	Waypoint	D3	D3	\N	\N	\N	0101000000A25932F72B55EEBF8250588E42534A40	2134
D4	159.22300000000001	Waypoint	D4	D4	\N	\N	\N	0101000000E4F41B44701EEEBF5C3B00CB12534A40	2135
D5	133.02699999999999	Waypoint	D5	D5	\N	\N	\N	0101000000807AD3F791D0EDBF21A64C8385544A40	2136
D69	152.25299999999999	Waypoint	D69	D69	\N	\N	\N	01010000005C564025D712EEBFA7EEFF20E8544A40	2137
D7	111.157	Waypoint	D7	D7	\N	\N	\N	0101000000084032D664F1EEBF1A9AFC60AA564A40	2138
DAT11A	262.56400000000002	Flag	DAT11A	DAT11A	\N	\N	\N	0101000000A57077B5E0B3404091EF9E78587D4140	2139
DELETED	42.4236	Waypoint	DELETED	DELETED	\N	\N	\N	010100000059CAB8FD402640409E0D0D4C6D834140	2140
DR1	219.30500000000001	Flag	DR1	DR1	\N	\N	\N	0101000000A840548455B2404042FDEF078A8A4140	2141
F1	117.886	Waypoint	F1	F1	\N	\N	\N	0101000000F9D9F3F5EFAB4040B67B5424E8604140	2142
F2	113.08	Waypoint	F2	F2	\N	\N	\N	0101000000A02FEB35E5AB404092FE080AD6604140	2143
F29	62.130499999999998	Waypoint	F29	F29	\N	\N	\N	0101000000A1DD4C455DA64040B159DFD4C8614140	2144
F3	115.24299999999999	Waypoint	F3	F3	\N	\N	\N	010100000085C95C32F1AB404060ED16EBD5604140	2145
F4	115.964	Waypoint	F4	F4	\N	\N	\N	01010000001EF4283900AC4040F580D739D6604140	2146
F5	119.569	Waypoint	F5	F5	\N	\N	\N	01010000007E6D3F11F2AB40404B063955E2604140	2147
F6	119.08799999999999	Waypoint	F6	F6	\N	\N	\N	0101000000B506CBE5FAAB40402B269C01E1604140	2148
GA04-3	1640.8399999999999	Waypoint	GA04-3	GA04-3	\N	\N	\N	0101000000E819775AE942404056C22C179E1B4240	2149
GARMIN	325.04899999999998	Waypoint	GARMIN	GARMIN	\N	\N	\N	01010000003581CE1623B357C013B87FA9826D4340	2150
GAZ04-1	1640.3599999999999	Waypoint	GAZ04-1	GAZ04-1	\N	\N	\N	0101000000B4A0F00CEC4240400D0D3DC2A61B4240	2151
GAZ04-18	1677.1300000000001	Waypoint	GAZ04-18	GAZ04-18	\N	\N	\N	0101000000B0422F7B104340409CF36AB8BC1B4240	2152
GAZ04-21	1675.45	Waypoint	GAZ04-21	GAZ04-21	\N	\N	\N	0101000000D0D633170743404013968FDCCE1B4240	2153
GAZ04-22	1673.29	Waypoint	GAZ04-22	GAZ04-22	\N	\N	\N	010100000087F29B48044340402533D79CD51B4240	2154
GAZ04-25	1677.8499999999999	Waypoint	GAZ04-25	GAZ04-25	\N	\N	\N	01010000003230F0B1FB42404009928BF2CB1B4240	2155
GAZ04-26	1680.98	Waypoint	GAZ04-26	GAZ04-26	\N	\N	\N	0101000000825934AC0843404022D9E46FC91B4240	2156
GAZ04-32	1650.7	Waypoint	GAZ04-32	GAZ04-32	\N	\N	\N	01010000003C4A8866DC424040307B744BF41B4240	2157
GAZ04-35	1628.8299999999999	Waypoint	GAZ04-35	GAZ04-35	\N	\N	\N	01010000001D02D5ACA4424040AEAAA352081C4240	2158
GAZ04-36	1628.8299999999999	Waypoint	GAZ04-36	GAZ04-36	\N	\N	\N	0101000000D22D5F8A77424040A96EAF7E0C1C4240	2159
GAZ04-37	1188.3099999999999	Waypoint	GAZ04-37	GAZ04-37	\N	\N	\N	0101000000230133775E404040CA50EF8B461C4240	2160
GAZ04-6	1623.54	Waypoint	GAZ04-6	GAZ04-6	\N	\N	\N	0101000000CF4C987FE0424040DAC95CF0941B4240	2161
GAZ04-8-17	1654.54	Waypoint	GAZ04-8-17	GAZ04-8-17	\N	\N	\N	01010000001408AD1A1343404028B3077B4F1B4240	2162
GAZKTFIRET	1680.5	Waypoint	GAZKTFIRET	GAZKTFIRET	\N	\N	\N	0101000000BC40905611434040BB6323A3921B4240	2163
GEORGOSTMB	99.381200000000007	Waypoint	GEORGOSTMB	GEORGOSTMB	\N	\N	\N	0101000000D8C3F7FB4EAC404081DF47B4EB604140	2164
GMARAES	81.356700000000004	Waypoint	GMARAES	GMARAES	\N	\N	\N	01010000007288972BACAC4040C3F82C7943604140	2165
GME1	148.88900000000001	Waypoint	GME1	GME1	\N	\N	\N	0101000000D448509EE79F41406A50B0D67B564040	2166
GOTTHARD	2039.3099999999999	Waypoint	GOTTHARD	GOTTHARD	\N	\N	\N	0101000000332C53876D222140397410FA2D474740	2167
GOTTHARD 1	410.60599999999999	Waypoint	GOTTHARD 1	GOTTHARD 1	\N	\N	\N	0101000000A3FB3CBD874B2140AB11437071684740	2168
GRIGGS	425.02499999999998	Waypoint	GRIGGS	GRIGGS	\N	\N	\N	01010000006585A615F42D53C0AE9FA042E5314540	2169
GRMEUR	35.934699999999999	Waypoint	GRMEUR	GRMEUR	\N	\N	\N	01010000001E909861226CF7BF229CA71ECF7D4940	2170
GRMPHX	361.09800000000001	Waypoint	GRMPHX	GRMPHX	\N	\N	\N	0101000000D0B1FD108DFC5BC022360CAA43AA4040	2171
GRMTWN	38.097700000000003	Waypoint	GRMTWN	GRMTWN	\N	\N	\N	01010000001E631221FA685E40183ACF08D10F3940	2172
GTOMB2	94.334500000000006	Waypoint	GTOMB2	GTOMB2	\N	\N	\N	01010000001781F8934DAC404023BAA4C0FB604140	2173
GZ04-2	1642.53	Waypoint	GZ04-2	GZ04-2	\N	\N	\N	010100000035294CB2EA424040F6B30743A51B4240	2174
H	1333.95	Waypoint	H	H	\N	\N	\N	0101000000F33570AC30574040617DE083477F4140	2175
HERMES	1682.1800000000001	Waypoint	HERMES	HERMES	\N	\N	\N	01010000001247DF23696D364021692B87BAF74240	2176
HIN 12	1587.01	Waypoint	HIN 12	HIN 12	\N	\N	\N	010100000098108073173D42400E553396697D4240	2177
HIN 13AKCM	1569.47	Waypoint	HIN 13AKCM	HIN 13AKCM	\N	\N	\N	0101000000DE5DE8C3133D4240D40788055B7D4240	2178
HIN 14	1587.97	Waypoint	HIN 14	HIN 14	\N	\N	\N	0101000000C49F8815133D42407E6613D7667D4240	2179
HIN 15	1618.73	Waypoint	HIN 15	HIN 15	\N	\N	\N	0101000000584B2076BD3F4240F985C42EAB7C4240	2180
HIN 16	1618.01	Waypoint	HIN 16	HIN 16	\N	\N	\N	0101000000C39DA8F8C13F4240FF988FD1A57C4240	2181
HIN 17	1633.3900000000001	Waypoint	HIN 17	HIN 17	\N	\N	\N	01010000007B329705A73F4240571B7051BE7C4240	2182
HIN 18	1642.29	Waypoint	HIN 18	HIN 18	\N	\N	\N	01010000002A1E681CA23F4240B70CA5E7B77C4240	2183
HIN 19	1610.0799999999999	Waypoint	HIN 19	HIN 19	\N	\N	\N	0101000000B8FCBAE5C03F424076F7F66AA47C4240	2184
HIN 2	1418.54	Waypoint	HIN 2	HIN 2	\N	\N	\N	010100000024A0AB75AE3B4240D7C43C0A1B7D4240	2185
HIN 20	1612.97	Waypoint	HIN 20	HIN 20	\N	\N	\N	01010000005EE7C021BD3F424016ACABE1A67C4240	2186
HIN 3	1432	Waypoint	HIN 3	HIN 3	\N	\N	\N	0101000000BFE743F4C43B4240D94F575B177D4240	2187
HIN 4	1688.9100000000001	Waypoint	HIN 4	HIN 4	\N	\N	\N	0101000000E1FB18FCC83D4240596B07A7EE7C4240	2188
HIN 5	1678.8199999999999	Waypoint	HIN 5	HIN 5	\N	\N	\N	0101000000346FF3CBC23D4240ADD39C4AF37C4240	2189
HIN 6	1654.54	Waypoint	HIN 6	HIN 6	\N	\N	\N	0101000000B02E9CDBEC3D424047127D71FB7C4240	2190
HIN 7	1700.6900000000001	Waypoint	HIN 7	HIN 7	\N	\N	\N	01010000002D8B3B3E173E424052FFC890BD7C4240	2191
HIN 8	1706.21	Waypoint	HIN 8	HIN 8	\N	\N	\N	01010000005994A7E6113E42406B203F16C07C4240	2192
HIN 91011	1750.1900000000001	Waypoint	HIN 91011	HIN 91011	\N	\N	\N	0101000000EA5B58CA413E4240B62E5B09A27C4240	2193
HINZIRLI 1	1405.0799999999999	Waypoint	HINZIRLI 1	HINZIRLI 1	\N	\N	\N	01010000002A62187DB33B42400C656CDE347D4240	2194
IBO KURTKE	45.788200000000003	Waypoint	IBO KURTKE	IBO KURTKE	\N	\N	\N	010100000071DAB95418EB3C4018A0AC99EE984440	2195
IHA-1	101.544	Waypoint	IHA-1	IHA-1	\N	\N	\N	0101000000022AAFA4FB8E4140141261AAF6394040	2196
IHA-2	85.442300000000003	Waypoint	IHA-2	IHA-2	\N	\N	\N	01010000002E17BCAB888541403A078D68543D4040	2197
IIIIII	-7.0839800000000004	Waypoint	IIIIII	IIIIII	\N	\N	\N	01010000009F25B6256C8F3740230B08156BA24140	2198
IZM BURTHS	3.7308300000000001	Waypoint	IZM BURTHS	IZM BURTHS	\N	\N	\N	01010000003D9FBFD894C63A40A7FB1FB1742E4340	2199
J1	126.538	Waypoint	J1	J1	\N	\N	\N	01010000007D513EE1455FEFBFC8C51B1056544A40	2200
J10	13.1036	Waypoint	J10	J10	\N	\N	\N	0101000000C4F3036B5D0B2D407CDAA44CB1F14140	2201
J11	31.608899999999998	Waypoint	J11	J11	\N	\N	\N	01010000005D230D64F90B2D40ACBE34F140F14140	2202
J12	31.608899999999998	Waypoint	J12	J12	\N	\N	\N	01010000002A7C8D8FDC042D40A06FEF40BBF24140	2203
J13	55.401200000000003	Waypoint	J13	J13	\N	\N	\N	01010000009BBC5E205C062D404273170D8BF24140	2204
J14	50.835099999999997	Waypoint	J14	J14	\N	\N	\N	0101000000BDAF8D22B2052D4037B94FF6A5F24140	2205
J15	21.034400000000002	Waypoint	J15	J15	\N	\N	\N	0101000000A66F6C52DE052D4074A2E3BEA3F24140	2206
J16	36.174999999999997	Waypoint	J16	J16	\N	\N	\N	01010000009766C248920B2D405560E7F794F14140	2207
J2	54.920499999999997	Waypoint	J2	J2	\N	\N	\N	01010000001CA7029D32042D40FC553461B1F24140	2208
J3	60.4482	Waypoint	J3	J3	\N	\N	\N	01010000001C8990ED8E032D40513E44704AEF4140	2209
J4	25.840900000000001	Waypoint	J4	J4	\N	\N	\N	010100000023D36CE123042D404E70B45A95EF4140	2210
J5	24.1586	Waypoint	J5	J5	\N	\N	\N	0101000000593673486A0B2D4053D4EF7B81F14140	2211
J6	24.1586	Waypoint	J6	J6	\N	\N	\N	01010000004E4504C58A0B2D40F6C15FB77BF14140	2212
J7	16.948899999999998	Waypoint	J7	J7	\N	\N	\N	0101000000BD398CE4530B2D40C272C829A1F14140	2213
J8	15.747199999999999	Waypoint	J8	J8	\N	\N	\N	0101000000B1EE3E188F0A2D40C904F761CFF14140	2214
J9	14.545500000000001	Waypoint	J9	J9	\N	\N	\N	01010000002839E3D8D30A2D40680ED9D896F14140	2215
JKS-1	148.88900000000001	Waypoint	JKS-1	JKS-1	\N	\N	\N	010100000030373BF9849D414057829C629B554040	2216
JKS2	125.81699999999999	Waypoint	JKS2	JKS2	\N	\N	\N	0101000000090051DA629D414013F58CC1A6554040	2217
JKS3	149.12899999999999	Waypoint	JKS3	JKS3	\N	\N	\N	01010000002BF723C16B9D41402557F314A0554040	2218
JUNIP	1690.1099999999999	Waypoint	JUNIP	JUNIP	\N	\N	\N	0101000000FE0744C78F724040BC4A0783DC774140	2219
K2	316.63799999999998	Flag	K2	K2	\N	\N	\N	01010000000F7408AA73AB4040BAD05F5FA07F4140	2220
K2-1	315.67599999999999	Flag	K2-1	K2-1	\N	\N	\N	0101000000B946783974AB4040BA0707CC9A7F4140	2221
K2-2	313.99400000000003	Flag	K2-2	K2-2	\N	\N	\N	0101000000EF767B1873AB40405DB39798A77F4140	2222
K2-3	307.26499999999999	Flag	K2-3	K2-3	\N	\N	\N	0101000000DA82876466AB404067FDB4F1AA7F4140	2223
K2-4	312.79199999999997	Flag	K2-4	K2-4	\N	\N	\N	01010000006652A38D6CAB40405B307C3A9A7F4140	2224
K2-5	314.95499999999998	Flag	K2-5	K2-5	\N	\N	\N	010100000027410B4B74AB404010F2DAA2957F4140	2225
K3	314.23399999999998	Flag	K3	K3	\N	\N	\N	01010000009C2E0FC271AB4040ADF41217CB7F4140	2226
K3-1	311.11000000000001	Flag	K3-1	K3-1	\N	\N	\N	0101000000124D179767AB4040696E1814AC7F4140	2227
K3-2	312.79199999999997	Flag	K3-2	K3-2	\N	\N	\N	0101000000A22A800B6DAB4040A58CF764B77F4140	2228
K3-3	314.95499999999998	Flag	K3-3	K3-3	\N	\N	\N	0101000000E415046670AB40402B78F064CE7F4140	2229
K3-4	315.67599999999999	Flag	K3-4	K3-4	\N	\N	\N	0101000000ABBDFC0C77AB404031B18763CE7F4140	2230
K3-5	304.14100000000002	Flag	K3-5	K3-5	\N	\N	\N	0101000000D059944975AB404016F1CBD0E07F4140	2231
K3-6	297.65199999999999	Flag	K3-6	K3-6	\N	\N	\N	0101000000038D18C467AB404098417F18D67F4140	2232
K3-7	305.58300000000003	Flag	K3-7	K3-7	\N	\N	\N	01010000009A08672C64AB4040E9BA3759BF7F4140	2233
K3-8	305.58300000000003	Flag	K3-8	K3-8	\N	\N	\N	0101000000D004207464AB40406A6BBB3EB07F4140	2234
K4	326.49099999999999	Flag	K4	K4	\N	\N	\N	0101000000525EAFD95FAB404092F2D2CE627F4140	2235
K5	339.46899999999999	Flag	K5	K5	\N	\N	\N	010100000004B02C91FEAB4040665FA7C8497F4140	2236
K6	331.29700000000003	Flag	K6	K6	\N	\N	\N	01010000007907A3521FAC4040AB178354787F4140	2237
KAH BIGTRE	1058.29	Waypoint	KAH BIGTRE	KAH BIGTRE	\N	\N	\N	0101000000C5F0F223A7E44040D278C41672AE4340	2238
KAH MEWSAM	1058.53	Waypoint	KAH MEWSAM	KAH MEWSAM	\N	\N	\N	01010000000C93CF49B4E44040D259542E8EAE4340	2239
KALKIM HQ	280.58800000000002	Waypoint	KALKIM HQ	KALKIM HQ	\N	\N	\N	010100000082317650F6343B4032650849A4E74340	2240
KAM1	467.56299999999999	Flag	KAM1	KAM1	\N	\N	\N	01010000002655C4B1BAA04040F093ECD6C7814140	2241
KAM2	432.476	Flag	KAM2	KAM2	\N	\N	\N	0101000000BFC15B4390A14040FE88A3CE43834140	2242
KAMAN KALE	1052.52	Waypoint	KAMAN KALE	KAMAN KALE	\N	\N	\N	0101000000730B907AA5E44040C3C720AA82AE4340	2243
KASTORIA L	644.68499999999995	Waypoint	KASTORIA L	KASTORIA L	\N	\N	\N	01010000002522270CFD4035409BD234078C424440	2244
KAT 2005	1680.5	Waypoint	KAT 2005	KAT 2005	\N	\N	\N	0101000000EA4F9E56AA3A354031FDA80CF7E54340	2245
KATALION	417.09500000000003	Waypoint	KATALION	KATALION	\N	\N	\N	0101000000078E1FB8F7A640405B3F232FF07D4140	2246
KATARA	1672.5699999999999	Waypoint	KATARA	KATARA	\N	\N	\N	010100000001711F0544363540DBEBDF209AE74340	2247
KAT EAST	1111.8800000000001	Waypoint	KAT EAST	KAT EAST	\N	\N	\N	01010000008771D916553D35404CEF8FB54EEE4340	2248
KECIKALE	424.30399999999997	Waypoint	KECIKALE	KECIKALE	\N	\N	\N	0101000000BE69F07A0D6C3B40DF2433021D034340	2249
KELID1	390.178	Flag	KELID1	KELID1	\N	\N	\N	01010000007D2C9B3E1D9D4040E638A8DE89834140	2250
KELID2	381.52600000000001	Flag	KELID2	KELID2	\N	\N	\N	0101000000E40B88802B9D40409425634A9E834140	2251
KERKENES	1419.02	Waypoint	KERKENES	KERKENES	\N	\N	\N	010100000081D4AC877D8841402565EB160ADF4340	2252
KILLINI JN	1692.51	Waypoint	KILLINI JN	KILLINI JN	\N	\N	\N	0101000000A5FDE177EB693640C7E12C2213F54240	2253
KK1	410.846	Flag	KK1	KK1	\N	\N	\N	0101000000C7BF67D5FBA640409C16B72FF27D4140	2254
KK2	407.72199999999998	Flag	KK2	KK2	\N	\N	\N	010100000027CB2FD4F8A64040BFF52ECCEC7D4140	2255
KK3	403.87700000000001	Flag	KK3	KK3	\N	\N	\N	01010000006DFE04BCF1A640401F45B455FD7D4140	2256
KK4	396.90699999999998	Flag	KK4	KK4	\N	\N	\N	0101000000228F3B00FDA64040364D98630B7E4140	2257
KK5	405.79899999999998	Flag	KK5	KK5	\N	\N	\N	0101000000BD53945A0EA74040E82330A3067E4140	2258
KK6	407.00099999999998	Flag	KK6	KK6	\N	\N	\N	0101000000A252781209A7404035463CF0E97D4140	2259
KKA1	381.76600000000002	Flag	KKA1	KKA1	\N	\N	\N	01010000004902F89A6AA64040B23D0B26E47D4140	2260
KKA2	391.86000000000001	Flag	KKA2	KKA2	\N	\N	\N	010100000098AE9F7F74A640404D018755DE7D4140	2261
KKK1	324.80900000000003	Flag	KKK1	KKK1	\N	\N	\N	01010000004CE728DF6AAA40405B59FB6B14814140	2262
KL1	370.471	Flag	KL1	KL1	\N	\N	\N	01010000003C93339D68A6404027181F7D4B7E4140	2263
KLK SARIOT	938.36500000000001	Waypoint	KLK SARIOT	KLK SARIOT	\N	\N	\N	01010000006DD4875137013B40B3D8F8DC21E64340	2264
KMK	1663.9200000000001	Waypoint	KMK	KMK	\N	\N	\N	0101000000B6132F9384964240C1FADA5BD3E14240	2265
KOTYLI	1394.51	Waypoint	KOTYLI	KOTYLI	\N	\N	\N	010100000024C6EEDF15053540F096CFE5922A4440	2266
KR1	368.06799999999998	Flag	KR1	KR1	\N	\N	\N	0101000000838603C327A64040269C27E8407F4140	2267
KSK NW GAT	1580.76	Waypoint	KSK NW GAT	KSK NW GAT	\N	\N	\N	01010000006710294A257442405C4C57A3A3A74340	2268
KSK TEMPLE	1624.02	Waypoint	KSK TEMPLE	KSK TEMPLE	\N	\N	\N	010100000067602B3B99744240E5F02819A9A74340	2269
KYL-7 NVR	1823.97	Waypoint	KYL-7 NVR	KYL-7 NVR	\N	\N	\N	01010000009BF707E38A643640FB1CC4731BF44240	2270
KYLLINISW	1693.48	Waypoint	KYLLINISW	KYLLINISW	\N	\N	\N	0101000000BD0A68A3EC6936402114172416F54240	2271
LAVAGNONE	108.273	Waypoint	LAVAGNONE	LAVAGNONE	\N	\N	\N	010100000092822DCAFA13254090DAC4C9FDB74640	2272
LIMANTEPE	60.928800000000003	Waypoint	LIMANTEPE	LIMANTEPE	\N	\N	\N	0101000000AC3F183199C63A4076AF3FFB642E4340	2273
LINA MINE	462.75700000000001	Waypoint	LINA MINE	LINA MINE	\N	\N	\N	010100000054D25835A78D40409F4FE70161854140	2274
LNIN2	431.03399999999999	Waypoint	LNIN2	LNIN2	\N	\N	\N	01010000008E058088DF8E4040F935ABA1C1844140	2275
LYP1	202.001	Flag	LYP1	LYP1	\N	\N	\N	010100000021E3FB2968BB404067CB7F694D7B4140	2276
LYP10	225.07300000000001	Flag	LYP10	LYP10	\N	\N	\N	0101000000B93103B63FB94040F456FF15697E4140	2277
LYP11	231.56200000000001	Flag	LYP11	LYP11	\N	\N	\N	0101000000A187936140B9404060EAE74D857E4140	2278
LYP12	230.36000000000001	Flag	LYP12	LYP12	\N	\N	\N	0101000000A142571D57B94040423C08BD9B7E4140	2279
LYP13	232.28200000000001	Flag	LYP13	LYP13	\N	\N	\N	0101000000E8DC345075B94040956D031EA87E4140	2280
LYP14	240.21299999999999	Flag	LYP14	LYP14	\N	\N	\N	0101000000E062A837A1B94040FB03B57DA77E4140	2281
LYP15	244.059	Flag	LYP15	LYP15	\N	\N	\N	01010000005D7EE7E4B3B94040B7FAAA8FD97E4140	2282
LYP16	248.14400000000001	Flag	LYP16	LYP16	\N	\N	\N	010100000029720CF1DDB94040670C4802DA7E4140	2283
LYP17	240.934	Flag	LYP17	LYP17	\N	\N	\N	01010000006FF4BFC9E7B940402D7A602DE27E4140	2284
LYP18	230.36000000000001	Flag	LYP18	LYP18	\N	\N	\N	010100000075CE635503BA4040B8E9CF7EE47E4140	2285
LYP2	229.87899999999999	Flag	LYP2	LYP2	\N	\N	\N	01010000007F24A30513BA4040F294842DBE7E4140	2286
LYP3	233.48400000000001	Flag	LYP3	LYP3	\N	\N	\N	0101000000EA35EF98F7B94040439CB4949F7E4140	2287
LYP4	235.40700000000001	Flag	LYP4	LYP4	\N	\N	\N	0101000000AA2034F2E1B9404000E9B4D1AD7E4140	2288
LYP5	229.87899999999999	Flag	LYP5	LYP5	\N	\N	\N	0101000000463AA3B0C2B94040AC23AC65A97E4140	2289
LYP6	232.76300000000001	Flag	LYP6	LYP6	\N	\N	\N	0101000000A5CD8753A5B940400696308EA27E4140	2290
LYP7	226.51499999999999	Flag	LYP7	LYP7	\N	\N	\N	010100000017842CEA97B94040EF93EF8E967E4140	2291
LYP8	227.23599999999999	Flag	LYP8	LYP8	\N	\N	\N	0101000000E70481565FB9404092815FCA907E4140	2292
LYP9	223.631	Flag	LYP9	LYP9	\N	\N	\N	0101000000085C24DB63B94040BD526BA1717E4140	2293
MA1	397.14699999999999	Flag	MA1	MA1	\N	\N	\N	01010000007BF6F4C271A8404012C05B8D1D7E4140	2294
METSOVO	1172.6900000000001	Waypoint	METSOVO	METSOVO	\N	\N	\N	0101000000C5CF6873682E3540CD5F0F9069E24340	2295
MF1	133.02699999999999	Waypoint	MF1	MF1	\N	\N	\N	0101000000E7106191D9AB404074BDF8980B614140	2296
MG1	352.68700000000001	Flag	MG1	MG1	\N	\N	\N	01010000007FF95F0CF7A94040A1596F5A84814140	2297
MMT	705.96900000000005	Waypoint	MMT	MMT	\N	\N	\N	01010000007531D02C3EFF3F406C627485A6D34340	2298
M-P1	325.28899999999999	Flag	M-P1	M-P1	\N	\N	\N	0101000000FFB3CF2B2AAB4040C8CC68580A7E4140	2299
MTKEV	420.21899999999999	Waypoint	MTKEV	MTKEV	\N	\N	\N	010100000004DAFFFBF1A640404C5F7C8F827E4140	2300
M-VS1	325.28899999999999	Flag	M-VS1	M-VS1	\N	\N	\N	01010000005C50A0CEE3AB40401D02D5ACA47D4140	2301
M-VS10	321.68400000000003	Flag	M-VS10	M-VS10	\N	\N	\N	01010000009FD4B0427EAB4040F0BAF3CE257E4140	2302
M-VS11	320.00200000000001	Flag	M-VS11	M-VS11	\N	\N	\N	010100000064506B6F77AB404019FF64AA257E4140	2303
M-VS12	327.21199999999999	Flag	M-VS12	M-VS12	\N	\N	\N	0101000000510CDFAE96AB40400B185FDFC77D4140	2304
M-VS2	358.935	Flag	M-VS2	M-VS2	\N	\N	\N	0101000000950EF70DE3AB40407902F40AEA7D4140	2305
M-VS3	364.22300000000001	Flag	M-VS3	M-VS3	\N	\N	\N	0101000000D1CB3F32D1AB4040C25E93FBFE7D4140	2306
M-VS4	360.61700000000002	Flag	M-VS4	M-VS4	\N	\N	\N	0101000000622D43BEB9AB4040B481C7F9537E4140	2307
M-VS5	353.40800000000002	Flag	M-VS5	M-VS5	\N	\N	\N	010100000042187FA8A2AB4040C1B02C334D7E4140	2308
M-VS6	352.44600000000003	Flag	M-VS6	M-VS6	\N	\N	\N	01010000002F83944CCCAB4040F0C38817E07D4140	2309
M-VS7	334.18200000000002	Flag	M-VS7	M-VS7	\N	\N	\N	01010000006E5170F5BFAB4040EBB39812A87D4140	2310
M-VS8	334.42200000000003	Flag	M-VS8	M-VS8	\N	\N	\N	01010000002F2E2077BBAB4040EC6E5CCEBE7D4140	2311
M-VS9	329.13499999999999	Flag	M-VS9	M-VS9	\N	\N	\N	0101000000C80DD37F95AB40403284875CF57D4140	2312
MYC GCA	247.904	Waypoint	MYC GCA	MYC GCA	\N	\N	\N	0101000000BEE1E5B9A3C13640A6593BB27BDD4240	2313
NEMRUD ET	2144.5700000000002	Waypoint	NEMRUD ET	NEMRUD ET	\N	\N	\N	010100000014F17888F65E434075800C8F90FD4240	2314
NEMRUD WT	2159.71	Waypoint	NEMRUD WT	NEMRUD WT	\N	\N	\N	010100000000FE038EBF5E4340A00E380675FD4240	2315
OAKS	871.31399999999996	Waypoint	OAKS	OAKS	\N	\N	\N	0101000000CB7B7E13E8E338405063D32E0CA34140	2316
OKV1	260.88099999999997	Waypoint	OKV1	OKV1	\N	\N	\N	0101000000541D30178AAB3940E9DB546D0F874140	2317
OKV2	259.68000000000001	Waypoint	OKV2	OKV2	\N	\N	\N	0101000000690848AD90AB39407E32D86A15874140	2318
OKV3	259.68000000000001	Waypoint	OKV3	OKV3	\N	\N	\N	01010000006F41DFAB90AB394004C0807715874140	2319
OKV4	258.959	Waypoint	OKV4	OKV4	\N	\N	\N	0101000000CD47295392AB39407839C4F615874140	2320
OLTU	2142.8899999999999	Waypoint	OLTU	OLTU	\N	\N	\N	01010000005FB8781A9EF64440AEC5A44809504440	2321
ORT A	792.00599999999997	Waypoint	ORT A	ORT A	\N	\N	\N	0101000000B19A28EE2A9E41403AF114F3A5204440	2322
ORT B	784.55600000000004	Waypoint	ORT B	ORT B	\N	\N	\N	0101000000E38A5B6A5C9E4140226BB7A47D204440	2323
ORT C	780.71000000000004	Waypoint	ORT C	ORT C	\N	\N	\N	0101000000BE4338036F9E41402D6B982A63204440	2324
ORT D	782.63300000000004	Waypoint	ORT D	ORT D	\N	\N	\N	01010000005882182A779E4140C266044456204440	2325
ORTTEMENOS	786.47799999999995	Waypoint	ORTTEMENOS	ORTTEMENOS	\N	\N	\N	01010000009B228386459E4140862F9FEEB4204440	2326
PAI1	404.59800000000001	Flag	PAI1	PAI1	\N	\N	\N	01010000000D59D0C0379F40403FC63FE0FF824140	2327
PAI2	399.06999999999999	Flag	PAI2	PAI2	\N	\N	\N	0101000000E07BD382449F404040FA7C4DFC824140	2328
PAI3	423.82400000000001	Flag	PAI3	PAI3	\N	\N	\N	010100000024C327267A9F404057C5230A10834140	2329
PALEO KOTY	1635.8	Waypoint	PALEO KOTY	PALEO KOTY	\N	\N	\N	01010000007ED91F237EFC344089F4D366392C4440	2330
PAM1	384.89100000000002	Flag	PAM1	PAM1	\N	\N	\N	0101000000CA5FEF38649E4040E9F9C61CB3834140	2331
PATERMA KO	421.90100000000001	Waypoint	PATERMA KO	PATERMA KO	\N	\N	\N	0101000000C5FFD67B157D3940BBCC50DF699D4440	2332
P-AY1	314.95499999999998	Flag	P-AY1	P-AY1	\N	\N	\N	0101000000B06A5F2C4BAE404008176CCF5E814140	2333
P-AY10	298.613	Flag	P-AY10	P-AY10	\N	\N	\N	0101000000ADEAA0A339AE4040912E8CAD8D814140	2334
P-AY11	300.536	Flag	P-AY11	P-AY11	\N	\N	\N	0101000000D7FB76DA66AE404059A6DBE979814140	2335
P-AY12	311.83100000000002	Flag	P-AY12	P-AY12	\N	\N	\N	01010000006BE4CFC46FAE4040674824F058814140	2336
P-AY13	304.62099999999998	Flag	P-AY13	P-AY13	\N	\N	\N	0101000000B67FE42474AE4040349D985354814140	2337
P-AY14	299.815	Flag	P-AY14	P-AY14	\N	\N	\N	010100000083DAD7D079AE4040D43ADCD461814140	2338
P-AY15	318.07999999999998	Flag	P-AY15	P-AY15	\N	\N	\N	0101000000FE8FA48832AE404023349B2A4C814140	2339
P-AY16	324.32799999999997	Flag	P-AY16	P-AY16	\N	\N	\N	01010000007F5F985607AE4040FAD4AC912F814140	2340
P-AY17	333.70100000000002	Flag	P-AY17	P-AY17	\N	\N	\N	01010000006F4233E509AE40406109B0370D814140	2341
P-AY3	309.90800000000002	Flag	P-AY3	P-AY3	\N	\N	\N	01010000004BA03F6F55AE40408321F43174814140	2342
P-AY4	307.024	Flag	P-AY4	P-AY4	\N	\N	\N	0101000000EBB167AE57AE404063138C8F7E814140	2343
P-AY5	308.226	Flag	P-AY5	P-AY5	\N	\N	\N	0101000000671BBFBD59AE4040EBA68F1E91814140	2344
P-AY6	313.75400000000002	Flag	P-AY6	P-AY6	\N	\N	\N	010100000040456BCB54AE40408B5F202465814140	2345
P-AY7	292.36500000000001	Flag	P-AY7	P-AY7	\N	\N	\N	0101000000F42098FE40AE40408CA05C1385814140	2346
P-AY8	298.37299999999999	Flag	P-AY8	P-AY8	\N	\N	\N	0101000000030DF91146AE40406CBA407779814140	2347
P-AY9	301.97800000000001	Flag	P-AY9	P-AY9	\N	\N	\N	010100000022C490FA3DAE4040A10A916973814140	2348
PELLA	44.586500000000001	Waypoint	PELLA	PELLA	\N	\N	\N	010100000059AC464EDD843640A4F6E3758B604440	2349
PFR	3.7308300000000001	Waypoint	PFR	PFR	\N	\N	\N	0101000000B686316D089251C0D7122FA5E7D24540	2350
PHERRAI	102.986	Waypoint	PHERRAI	PHERRAI	\N	\N	\N	0101000000F8A7C6DE9A2B3A409A43A35264724440	2351
PINETREE1	264.48599999999999	Waypoint	PINETREE1	PINETREE1	\N	\N	\N	01010000008C5B1B16E2AD4040F0ECA41E5F634140	2352
PL1	283.95299999999997	Waypoint	PL1	PL1	\N	\N	\N	01010000003D32BBC6E5B340408CB940895C7D4140	2353
PL2	282.99200000000002	Waypoint	PL2	PL2	\N	\N	\N	0101000000FB6C03C8E7B3404028F4C2E9597D4140	2354
PL3	281.55000000000001	Waypoint	PL3	PL3	\N	\N	\N	01010000002E357FA2EBB3404091EF9E78587D4140	2355
PL4	283.95299999999997	Waypoint	PL4	PL4	\N	\N	\N	010100000064C88B2EEAB3404096F748B5557D4140	2356
PL5	281.06900000000002	Waypoint	PL5	PL5	\N	\N	\N	01010000005231E77CE5B3404013617CFE5F7D4140	2357
PM1	295.72899999999998	Waypoint	PM1	PM1	\N	\N	\N	010100000053BFAFEA1CB040402D7DC77411824140	2358
PM2	296.93099999999998	Waypoint	PM2	PM2	\N	\N	\N	0101000000AEE8D46A0FB0404014DC333E18824140	2359
PM3	289.96100000000001	Waypoint	PM3	PM3	\N	\N	\N	0101000000B233486B13B040404813DB1C28824140	2360
PM4	290.202	Waypoint	PM4	PM4	\N	\N	\N	01010000008559843C24B0404006A5F3D727824140	2361
PM5	292.60500000000002	Waypoint	PM5	PM5	\N	\N	\N	01010000008A7888D220B04040EA6378CB19824140	2362
PM6	396.42599999999999	Flag	PM6	PM6	\N	\N	\N	0101000000CC36A7FA649C404054ABB46704834140	2363
PM6B	398.10899999999998	Flag	PM6B	PM6B	\N	\N	\N	01010000001CB05F3B4E9C4040984A7C4405834140	2364
PM6B3	398.589	Flag	PM6B3	PM6B3	\N	\N	\N	010100000076EB3CF74C9C4040A5FC20A203834140	2365
PM6B4	393.06200000000001	Flag	PM6B4	PM6B4	\N	\N	\N	0101000000D74763913F9C4040C8A1B8C2FF824140	2366
PM6B5	392.34100000000001	Flag	PM6B5	PM6B5	\N	\N	\N	0101000000E2746CE64B9C404089C124DEFF824140	2367
PMB1	259.68000000000001	Flag	PMB1	PMB1	\N	\N	\N	010100000039002C3CB9AF404051B433CB6C824140	2368
PMB2	260.64100000000002	Flag	PMB2	PMB2	\N	\N	\N	01010000009CF83A0E9BAF4040EEF1A76522824140	2369
PMB3	270.495	Flag	PMB3	PMB3	\N	\N	\N	01010000006C0E400528AF4040501C3B0676814140	2370
PMB4	269.53300000000002	Flag	PMB4	PMB4	\N	\N	\N	01010000008327DB5D33AF4040A2E1673872814140	2371
PMB5-	256.315	Flag	PMB5-	PMB5-	\N	\N	\N	01010000001FC5C4DC71AF4040E7C8B3938D824140	2372
PMB6	259.92000000000002	Flag	PMB6	PMB6	\N	\N	\N	0101000000B3CAAF9338AF4040DE040F6E93824140	2373
PMD11	499.04599999999999	Flag	PMD11	PMD11	\N	\N	\N	0101000000ABFE78173E9A40401AB348BAB6804140	2374
PMD1A1	516.35000000000002	Flag	PMD1A1	PMD1A1	\N	\N	\N	0101000000A202505A329A4040B2B66B3AA9804140	2375
PMD1A2	497.36399999999998	Flag	PMD1A2	PMD1A2	\N	\N	\N	0101000000727090E06B9A40401148F4F89E804140	2376
PMD1A3	508.89999999999998	Flag	PMD1A3	PMD1A3	\N	\N	\N	01010000004FC0C0BD7C9A4040EF30D4FE9A804140	2377
PMD1A4	501.93000000000001	Flag	PMD1A4	PMD1A4	\N	\N	\N	01010000001C6DEF2F8F9A40403ADDC36F94804140	2378
PMD1A5	492.077	Flag	PMD1A5	PMD1A5	\N	\N	\N	010100000085E30BF0A39A404078C0B42990804140	2379
PMD1B1	496.64299999999997	Flag	PMD1B1	PMD1B1	\N	\N	\N	01010000009E0C512F3F9A404053B45B93BA804140	2380
PMD1B2	490.154	Flag	PMD1B2	PMD1B2	\N	\N	\N	01010000008D37CCAC729A40407ECFA33DB9804140	2381
PMD1B3	497.84500000000003	Flag	PMD1B3	PMD1B3	\N	\N	\N	0101000000496D7FBA839A40406B7E18FBB4804140	2382
PMD1B4	486.54899999999998	Flag	PMD1B4	PMD1B4	\N	\N	\N	0101000000EDDE0B459A9A40405CAFA725AF804140	2383
PMD1B5	486.30900000000003	Flag	PMD1B5	PMD1B5	\N	\N	\N	0101000000CD392C98AE9A40407B26BC83A6804140	2384
PMD1C1	498.80599999999998	Flag	PMD1C1	PMD1C1	\N	\N	\N	0101000000EB2748DE4C9A4040453260A0D6804140	2385
PMD1C2	489.43299999999999	Flag	PMD1C2	PMD1C2	\N	\N	\N	0101000000D71687FB949A4040E31A8374CF804140	2386
PMD1C3	464.43900000000002	Flag	PMD1C3	PMD1C3	\N	\N	\N	01010000001F3A239BC29A4040DCBE7CEFC5804140	2387
PMD1C4	444.97300000000001	Flag	PMD1C4	PMD1C4	\N	\N	\N	010100000045A66FDEE79A4040C38E9467BD804140	2388
PMD1D1	500.00799999999998	Flag	PMD1D1	PMD1D1	\N	\N	\N	0101000000EB3B5CFE459A4040CE696F20BF804140	2389
PMD1D2	493.03800000000001	Flag	PMD1D2	PMD1D2	\N	\N	\N	01010000007D4110C2919A404024F7CF2EBA804140	2390
PMD1D3	464.43900000000002	Flag	PMD1D3	PMD1D3	\N	\N	\N	0101000000361818E9AD9A40407311B9C2B6804140	2391
PMD1D4	453.86500000000001	Flag	PMD1D4	PMD1D4	\N	\N	\N	010100000079366721DA9A40409C9DAC1BA6804140	2392
PMD1E1	452.423	Flag	PMD1E1	PMD1E1	\N	\N	\N	0101000000FBD6EC20ED9A4040460ACDC4A0804140	2393
PMD1E2	451.94200000000001	Flag	PMD1E2	PMD1E2	\N	\N	\N	010100000097B4FBB8EA9A40409612E7809B804140	2394
PMD1E3	454.58600000000001	Flag	PMD1E3	PMD1E3	\N	\N	\N	0101000000300970AAD09A4040B6B2004A9C804140	2395
PMD1E4	462.75700000000001	Flag	PMD1E4	PMD1E4	\N	\N	\N	0101000000EC069F20D09A40406FEE7A2CA7804140	2396
PMD1E5	481.50200000000001	Flag	PMD1E5	PMD1E5	\N	\N	\N	010100000011971406C29A404003D13008AE804140	2397
PMD1E6	485.34800000000001	Flag	PMD1E6	PMD1E6	\N	\N	\N	010100000037C45C78BC9A4040A148CCF3AD804140	2398
PMD1E7	495.20100000000002	Flag	PMD1E7	PMD1E7	\N	\N	\N	0101000000BB124940819A404035E22F51B0804140	2399
PMD1E8	484.86700000000002	Flag	PMD1E8	PMD1E8	\N	\N	\N	0101000000023B64EE4D9A40408FC6188DB9804140	2400
PMD1E9	491.11500000000001	Flag	PMD1E9	PMD1E9	\N	\N	\N	01010000004C50001B499A4040F2D1BCC5BE804140	2401
PMF1	501.93000000000001	Flag	PMF1	PMF1	\N	\N	\N	0101000000C35AD0094E9A4040F3B8CB25E5804140	2402
PMFAR1	390.65800000000002	Flag	PMFAR1	PMFAR1	\N	\N	\N	01010000003B7D8BEE239C404057FDA7A121834140	2403
PMFAR2	394.50400000000002	Flag	PMFAR2	PMFAR2	\N	\N	\N	010100000083D3AB962E9C404013060F1A14834140	2404
PMFE2	482.94400000000002	Flag	PMFE2	PMFE2	\N	\N	\N	0101000000C3BC4361829A40402CF5BF6BF6804140	2405
PMFEN	488.23200000000003	Flag	PMFEN	PMFEN	\N	\N	\N	0101000000B078B7A0A19A4040FEB03B6373814140	2406
PMG1	494.24000000000001	Flag	PMG1	PMG1	\N	\N	\N	0101000000212A806E579A4040D2C5F7DC01814140	2407
PMG2	471.40899999999999	Flag	PMG2	PMG2	\N	\N	\N	01010000005C15DBB6859A404035F68A46F5804140	2408
PMG3	500.96899999999999	Flag	PMG3	PMG3	\N	\N	\N	01010000002D74C0D6AE9A40406D1A4808F0804140	2409
PMG4	474.29199999999997	Flag	PMG4	PMG4	\N	\N	\N	0101000000109DB714C49A40402A5257FAED804140	2410
PMH1	462.75700000000001	Flag	PMH1	PMH1	\N	\N	\N	0101000000490D0D8EC99A4040D141C7A9BE814140	2411
PMH2	450.74000000000001	Flag	PMH2	PMH2	\N	\N	\N	0101000000D70051A2F89A40400F30F065AD814140	2412
PMH3	440.887	Flag	PMH3	PMH3	\N	\N	\N	010100000000ED5B6E2F9B40400278CCBFA4814140	2413
PMH4	425.50599999999997	Flag	PMH4	PMH4	\N	\N	\N	01010000001A4E8F297C9B4040F8FBC0B38F814140	2414
PMH5	423.82400000000001	Flag	PMH5	PMH5	\N	\N	\N	0101000000270790FA9B9B404068C787858C814140	2415
PMH6	418.77699999999999	Flag	PMH6	PMH6	\N	\N	\N	0101000000F65BE0979F9B4040E8780C5C8C814140	2416
PMH7	437.28199999999998	Flag	PMH7	PMH7	\N	\N	\N	0101000000DE793381DA9B404064D0AB2F82814140	2417
PMI6	441.84800000000001	Flag	PMI6	PMI6	\N	\N	\N	0101000000393ECFA9E89B40408F09C14297814140	2418
PMJ1	449.05799999999999	Flag	PMJ1	PMJ1	\N	\N	\N	010100000028CA8C54EB9B40407577AC38A6814140	2419
PMJ4	415.65300000000002	Flag	PMJ4	PMJ4	\N	\N	\N	0101000000BA7D84D1949B4040954758D0CB814140	2420
PMJ5	441.84800000000001	Flag	PMJ5	PMJ5	\N	\N	\N	01010000005E81E390DF9A4040FE78384CF7814140	2421
PMK1	434.87900000000002	Flag	PMK1	PMK1	\N	\N	\N	0101000000987064F5F99B4040E9D4DCA4BE814140	2422
PMK3	409.64400000000001	Flag	PMK3	PMK3	\N	\N	\N	010100000058FFD805B29B4040F4FF7279E8814140	2423
PMK6	420.21899999999999	Flag	PMK6	PMK6	\N	\N	\N	0101000000CD48731A989B4040A6E57CBBE9814140	2424
PMK7	432.95600000000002	Flag	PMK7	PMK7	\N	\N	\N	010100000093A52FB4159B4040FC13F78002824140	2425
PMK8	432.71600000000001	Flag	PMK8	PMK8	\N	\N	\N	0101000000063EE31BFA9A40400162A3900A824140	2426
PML1	487.75099999999998	Flag	PML1	PML1	\N	\N	\N	01010000003DF5BAD8839A40407C1229E442814140	2427
PML11	499.767	Flag	PML11	PML11	\N	\N	\N	010100000022375B2A839A4040962920CC71814140	2428
PML13	484.62700000000001	Flag	PML13	PML13	\N	\N	\N	0101000000B2088BAB869A40408818ACC186814140	2429
PML15	482.22300000000001	Flag	PML15	PML15	\N	\N	\N	01010000005086BC7B869A40402D3267A190814140	2430
PML17	485.34800000000001	Flag	PML17	PML17	\N	\N	\N	01010000008212FCF59E9A40408B6A9FFBA3814140	2431
PML3	492.077	Flag	PML3	PML3	\N	\N	\N	01010000007D864C067B9A4040ED0C746953814140	2432
PML5	490.63499999999999	Flag	PML5	PML5	\N	\N	\N	0101000000EECEAFED7A9A4040C9508F195A814140	2433
PML7	487.99099999999999	Flag	PML7	PML7	\N	\N	\N	01010000006E3A1B557A9A4040FB2AE7F561814140	2434
PML9	492.077	Flag	PML9	PML9	\N	\N	\N	010100000011B703A87B9A4040F346E8926A814140	2435
PMR1	498.56599999999997	Flag	PMR1	PMR1	\N	\N	\N	0101000000DED0F40FAB9A40407BAC9F52F3804140	2436
PMR3	486.54899999999998	Flag	PMR3	PMR3	\N	\N	\N	010100000078A54B509A9A40406E3DB78EF9804140	2437
PMR5	481.262	Flag	PMR5	PMR5	\N	\N	\N	0101000000667A8C699B9A404070325BC60E814140	2438
PMR7	481.262	Flag	PMR7	PMR7	\N	\N	\N	0101000000D9B1F079AE9A40407446275A18814140	2439
PMR9	486.30900000000003	Flag	PMR9	PMR9	\N	\N	\N	0101000000E81D58A2B49A404024279CA828814140	2440
POGGIOMARI	50.354399999999998	Waypoint	POGGIOMARI	POGGIOMARI	\N	\N	\N	0101000000B583ACCF1E262D407E8B90B2AC654440	2441
POL KTREE1	395.46499999999997	Waypoint	POL KTREE1	POL KTREE1	\N	\N	\N	010100000070808CC51E9D4040612E3FF68E834140	2442
POL KTREE2	395.46499999999997	Waypoint	POL KTREE2	POL KTREE2	\N	\N	\N	010100000010A46C402D9D40408A3E4F9E9A834140	2443
POL KTREE3	396.66699999999997	Waypoint	POL KTREE3	POL KTREE3	\N	\N	\N	0101000000D9EA9358299D404003B4036796834140	2444
POLSA1	447.37599999999998	Waypoint	POLSA1	POLSA1	\N	\N	\N	0101000000174320340E9C40403CB0BBF7F2834140	2445
POLSA1A	423.82400000000001	Waypoint	POLSA1A	POLSA1A	\N	\N	\N	01010000009B701C690A9C40401A8664080D844140	2446
POLSA1B	420.459	Waypoint	POLSA1B	POLSA1B	\N	\N	\N	010100000001ADA039109C4040584D2B132B844140	2447
POLSA1C	411.08600000000001	Waypoint	POLSA1C	POLSA1C	\N	\N	\N	0101000000435568FA0E9C40406AEB4FFE48844140	2448
POLSA2	443.05000000000001	Waypoint	POLSA2	POLSA2	\N	\N	\N	0101000000E98493DEFE9B404032F2D07CF0834140	2449
POLSA2A	419.25799999999998	Waypoint	POLSA2A	POLSA2A	\N	\N	\N	0101000000FEFD229BFD9B40406253AFA712844140	2450
POLSA2B	411.327	Waypoint	POLSA2B	POLSA2B	\N	\N	\N	01010000005EC98BD5F19B4040DF31A4D928844140	2451
POLSA2C	407.00099999999998	Waypoint	POLSA2C	POLSA2C	\N	\N	\N	010100000034CD8F4DDF9B4040338CA4A53E844140	2452
POLSALITH	404.59800000000001	Waypoint	POLSALITH	POLSALITH	\N	\N	\N	010100000017734767EF9B4040528C9B363D844140	2453
POLSAWS	405.07799999999997	Waypoint	POLSAWS	POLSAWS	\N	\N	\N	01010000006A4150B7F19B40403087337845844140	2454
POLTREE1	500.00799999999998	Waypoint	POLTREE1	POLTREE1	\N	\N	\N	01010000008B19230A549A40401B7FC3C0CA804140	2455
POLTREE2	500.72899999999998	Waypoint	POLTREE2	POLTREE2	\N	\N	\N	0101000000EA1334BE539A4040C1A6EFB1CB804140	2456
POLTREE3	500.72899999999998	Waypoint	POLTREE3	POLTREE3	\N	\N	\N	0101000000C5A567D0499A4040F1398C56A9804140	2457
POLTREE4	507.69799999999998	Waypoint	POLTREE4	POLTREE4	\N	\N	\N	010100000064A5FB99379A4040ECFD9782AD804140	2458
PORSUK	1250.3099999999999	Waypoint	PORSUK	PORSUK	\N	\N	\N	0101000000FD451FDF1D4A4140305807C6D5C14240	2459
PPG 2003	1606.96	Waypoint	PPG 2003	PPG 2003	\N	\N	\N	0101000000427CB6C7B82A354081D63B5D4FF44340	2460
PPM PARMA	46.268900000000002	Waypoint	PPM PARMA	PPM PARMA	\N	\N	\N	010100000095A963448BAC2440B72423415A664640	2461
PRB1	422.14100000000002	Waypoint	PRB1	PRB1	\N	\N	\N	0101000000AC2FB267861053C07EEF2AFC3D394540	2462
PRB10	373.11500000000001	Waypoint	PRB10	PRB10	\N	\N	\N	010100000071216ED27D1053C07D3BE77940394540	2463
PRB11	365.90499999999997	Waypoint	PRB11	PRB11	\N	\N	\N	0101000000DD0A5C7C7C1053C095F4C8D045394540	2464
PRB12	365.90499999999997	Waypoint	PRB12	PRB12	\N	\N	\N	010100000049CE6BE7791053C0F92D149247394540	2465
PRB13	367.10599999999999	Waypoint	PRB13	PRB13	\N	\N	\N	0101000000214DB0907D1053C0971F138446394540	2466
PRB2	394.26299999999998	Waypoint	PRB2	PRB2	\N	\N	\N	0101000000D06DBA4C8A1053C0D6FC602F40394540	2467
PRB3	364.94299999999998	Waypoint	PRB3	PRB3	\N	\N	\N	0101000000818DB1348C1053C08EDEC41E47394540	2468
PRB4	359.416	Waypoint	PRB4	PRB4	\N	\N	\N	0101000000954262B38A1053C0201E1FB347394540	2469
PRB5	407.24099999999999	Waypoint	PRB5	PRB5	\N	\N	\N	0101000000E445EC348E1053C01D0A2F5946394540	2470
PRB6	408.202	Waypoint	PRB6	PRB6	\N	\N	\N	01010000007133142B8E1053C01AC2F46746394540	2471
PRB8	368.06799999999998	Waypoint	PRB8	PRB8	\N	\N	\N	010100000096C5A1D78F1053C0CC38E0CF3D394540	2472
PRB9	365.90499999999997	Waypoint	PRB9	PRB9	\N	\N	\N	0101000000573D30E57B1053C07608F38942394540	2473
PSK1	199.358	Flag	PSK1	PSK1	\N	\N	\N	0101000000704F208471BB40407E9BDFDF497B4140	2474
PSK2	208.00899999999999	Flag	PSK2	PSK2	\N	\N	\N	01010000007434449E6FBB4040643AF3F9487B4140	2475
PSK3	205.125	Flag	PSK3	PSK3	\N	\N	\N	01010000007D5BDBD473BB404043079C26657B4140	2476
PSK4	206.08699999999999	Flag	PSK4	PSK4	\N	\N	\N	01010000005C45980587BB4040388BB4E0477B4140	2477
PSK5	203.68299999999999	Flag	PSK5	PSK5	\N	\N	\N	0101000000E3BD2B017FBB4040E12E7CF63C7B4140	2478
PSK6	206.80799999999999	Flag	PSK6	PSK6	\N	\N	\N	01010000005BF3800572BB40402A1314C53F7B4140	2479
PSK7	202.96199999999999	Flag	PSK7	PSK7	\N	\N	\N	0101000000042B635061BB4040B6656F124B7B4140	2480
PT1	413.97000000000003	Flag	PT1	PT1	\N	\N	\N	0101000000A60EDBDE3A9F404037B69767B7824140	2481
PT15MSWAR2	122.93300000000001	Waypoint	PT15MSWAR2	PT15MSWAR2	\N	\N	\N	010100000051A39064D6AB40406B856FB51C614140	2482
PTAREA2SW	122.93300000000001	Waypoint	PTAREA2SW	PTAREA2SW	\N	\N	\N	01010000005D0807B5FEAB40407525A463FE604140	2483
PTAREA3SW	122.212	Waypoint	PTAREA3SW	PTAREA3SW	\N	\N	\N	01010000007776AB47D7AB4040D700216922614140	2484
PTBASE1	116.925	Waypoint	PTBASE1	PTBASE1	\N	\N	\N	0101000000945833E100AC404075E9FC67CF604140	2485
PTM	24.8796	Waypoint	PTM	PTM	\N	\N	\N	0101000000FA895177289351C0B20BED39BCE94540	2486
PTPIVOT+WL	118.367	Waypoint	PTPIVOT+WL	PTPIVOT+WL	\N	\N	\N	010100000061F2E388E5AB4040E20F5D5518614140	2487
PY1	267.61099999999999	Flag	PY1	PY1	\N	\N	\N	01010000001E075B75E8AD404012BA6737DC814140	2488
PY2	254.393	Flag	PY2	PY2	\N	\N	\N	010100000095F0CB338BAF4040206AD3BF41824140	2489
PY3	257.75700000000001	Flag	PY3	PY3	\N	\N	\N	0101000000F46F9CB674AF40407146373CE9814140	2490
PY4	261.84300000000002	Flag	PY4	PY4	\N	\N	\N	0101000000352BBF324DAF4040CB7C1469B3814140	2491
RCHRCH	248.625	Waypoint	RCHRCH	RCHRCH	\N	\N	\N	0101000000E9E750FD38B4404067CB7F694D7B4140	2492
RCHRCH2	252.47	Waypoint	RCHRCH2	RCHRCH2	\N	\N	\N	0101000000CE8408334BB440402820370D5B7B4140	2493
ROCK1	363.98200000000003	Flag	ROCK1	ROCK1	\N	\N	\N	01010000009BCA3F8650AB4040E568C39E0C7F4140	2494
ROCK2	289.48000000000002	Flag	ROCK2	ROCK2	\N	\N	\N	01010000006430D78630AB404095C4BFF37E7F4140	2495
SA1	330.577	Flag	SA1	SA1	\N	\N	\N	01010000009FE0742849AF4040651C7420A37A4140	2496
SA2	336.34399999999999	Flag	SA2	SA2	\N	\N	\N	01010000005C3E50764CAF4040C07AC062AB7A4140	2497
SA3	332.01900000000001	Flag	SA3	SA3	\N	\N	\N	0101000000089F13664DAF40401A45FF99BB7A4140	2498
SAL	583.88199999999995	Flag	SAL	SAL	\N	\N	\N	0101000000F0E613F398314040EDF420900D7C4140	2499
SANGOTTARD	2087.1300000000001	Waypoint	SANGOTTARD	SANGOTTARD	\N	\N	\N	01010000002F544FFDBB212140C3852F5819474740	2500
SBD1	93.613399999999999	Waypoint	SBD1	SBD1	\N	\N	\N	01010000005867A02514AC404041D0E8468C604140	2501
SBD2	91.210099999999997	Waypoint	SBD2	SBD2	\N	\N	\N	01010000002E97130809AC4040C80B3FF189604140	2502
SBD3	75.108199999999997	Waypoint	SBD3	SBD3	\N	\N	\N	0101000000C281C8D6FEAB4040FDA328FD68604140	2503
SBD4	79.914699999999996	Waypoint	SBD4	SBD4	\N	\N	\N	0101000000C777A33BFAAB404093DD97F677604140	2504
SBD5	80.6357	Waypoint	SBD5	SBD5	\N	\N	\N	0101000000DFFC235F0BAC404033EFBF357A604140	2505
SBD6	89.287499999999994	Waypoint	SBD6	SBD6	\N	\N	\N	01010000004022A38C03AC404076E658BD87604140	2506
SCO-23 ABI	1525.49	Waypoint	SCO-23 ABI	SCO-23 ABI	\N	\N	\N	0101000000CDDCCF6BE40635409CFCF0D5092B4440	2507
SCOTIDA	1184.9400000000001	Waypoint	SCOTIDA	SCOTIDA	\N	\N	\N	0101000000BC5299D91CF3344099AC54C7DF2C4440	2508
SCV1	1182.3	Waypoint	SCV1	SCV1	\N	\N	\N	01010000008E792C9C755740408A8DDC472C804140	2509
SCV10	1174.8499999999999	Waypoint	SCV10	SCV10	\N	\N	\N	010100000069EB700CB257404046DA004B32804140	2510
SCV-100	1109.96	Waypoint	SCV-100	SCV-100	\N	\N	\N	01010000004967C8C7DA574040A60BA788307E4140	2511
SCV106	1115.73	Waypoint	SCV106	SCV106	\N	\N	\N	0101000000DBA95B6BDF57404001219422327E4140	2512
SCV107	1103.71	Waypoint	SCV107	SCV107	\N	\N	\N	01010000000CED5F03E9574040D82C978D0E7E4140	2513
SCV108	1132.79	Waypoint	SCV108	SCV108	\N	\N	\N	01010000008DE3432D265840408777236DCF7E4140	2514
SCV11	1183.74	Waypoint	SCV11	SCV11	\N	\N	\N	0101000000679543D281574040C5B6176122804140	2515
SCV110	1137.8399999999999	Waypoint	SCV110	SCV110	\N	\N	\N	01010000006600F1B8FD574040BCEE9B65507E4140	2516
SCV12	1103.71	Waypoint	SCV12	SCV12	\N	\N	\N	0101000000C96FE3AC8E574040D18B4F0B22804140	2517
SCV120	1073.9100000000001	Waypoint	SCV120	SCV120	\N	\N	\N	010100000060ED324005584040FFA778E28D7E4140	2518
SCV121	1261.6099999999999	Waypoint	SCV121	SCV121	\N	\N	\N	01010000005C8C2B7523584040266C5B6ED27E4140	2519
SCV122	1127.5	Waypoint	SCV122	SCV122	\N	\N	\N	010100000001DA47882158404045A98F50CB7E4140	2520
SCV123	1127.5	Waypoint	SCV123	SCV123	\N	\N	\N	0101000000B27BB805075840409245FFA3AD7E4140	2521
SCV125	1153.7	Waypoint	SCV125	SCV125	\N	\N	\N	010100000025735FE0CF574040E6A638D4907E4140	2522
SCV126	1159.23	Waypoint	SCV126	SCV126	\N	\N	\N	0101000000DA1A071AD0574040C4E3AF84867E4140	2523
SCV13	1190.71	Waypoint	SCV13	SCV13	\N	\N	\N	01010000002C98772393574040F95224D11E804140	2524
SCV130	1242.3800000000001	Waypoint	SCV130	SCV130	\N	\N	\N	0101000000B515E7E7C7574040D7D8EBFEED7E4140	2525
SCV131	1264.73	Waypoint	SCV131	SCV131	\N	\N	\N	0101000000DEB6CB8BC7574040E3E70325EC7E4140	2526
SCV134	1251.03	Waypoint	SCV134	SCV134	\N	\N	\N	01010000006ADDDB34C3574040E28AB422E47E4140	2527
SCV135	1242.1400000000001	Waypoint	SCV135	SCV135	\N	\N	\N	0101000000BC82BB53C45740408593B8B7F07E4140	2528
SCV136	1224.1199999999999	Waypoint	SCV136	SCV136	\N	\N	\N	010100000049959316CF5740408544C32AEA7E4140	2529
SCV137	1216.6700000000001	Waypoint	SCV137	SCV137	\N	\N	\N	010100000012E173E8B35740402D75A773F97E4140	2530
SCV138	1146.73	Waypoint	SCV138	SCV138	\N	\N	\N	01010000008578EF6B0E5840407CF9BB7CCD7E4140	2531
SCV14	1228.6800000000001	Waypoint	SCV14	SCV14	\N	\N	\N	01010000005C6917A88C5740407490303C11804140	2532
SCV15	1184.9400000000001	Waypoint	SCV15	SCV15	\N	\N	\N	0101000000B04B105290574040B05CD4C60F804140	2533
SCV16	1159.47	Waypoint	SCV16	SCV16	\N	\N	\N	0101000000529B17ABB6574040D0EB1FA91D804140	2534
SCV17	1157.3	Waypoint	SCV17	SCV17	\N	\N	\N	0101000000333B7442B4574040E203A89A3A804140	2535
SCV18	1157.3	Waypoint	SCV18	SCV18	\N	\N	\N	010100000030EDA36CB4574040986BCC493A804140	2536
SCV2	1211.3800000000001	Waypoint	SCV2	SCV2	\N	\N	\N	0101000000186323BA6E5740406EEFD6D628804140	2537
SCV21	1187.3399999999999	Waypoint	SCV21	SCV21	\N	\N	\N	0101000000348D446D7D574040D0DCC4420D804140	2538
SCV25	1162.3499999999999	Waypoint	SCV25	SCV25	\N	\N	\N	010100000068653FAEF457404087C328C148804140	2539
SCV26	1392.3399999999999	Waypoint	SCV26	SCV26	\N	\N	\N	0101000000DFEF40321757404012C4A4B85D7F4140	2540
SCV27	1397.1500000000001	Waypoint	SCV27	SCV27	\N	\N	\N	0101000000287AAB7120574040AE7E20045A7F4140	2541
SCV28	1400.52	Waypoint	SCV28	SCV28	\N	\N	\N	01010000007355435C25574040C414A0FE577F4140	2542
SCV29	1389.46	Waypoint	SCV29	SCV29	\N	\N	\N	0101000000AB65ECFD265740408E4110E8587F4140	2543
SCV3	1389.46	Waypoint	SCV3	SCV3	\N	\N	\N	01010000000502901F19724040F0D85854C7014240	2544
SCV30	1399.0699999999999	Waypoint	SCV30	SCV30	\N	\N	\N	0101000000CC36F3882A5740407AED4B96507F4140	2545
SCV31	1413.49	Waypoint	SCV31	SCV31	\N	\N	\N	01010000003156871B31574040F14BD7F5467F4140	2546
SCV32	1414.7	Waypoint	SCV32	SCV32	\N	\N	\N	010100000028BB0B2738574040863668FE447F4140	2547
SCV33	1411.0899999999999	Waypoint	SCV33	SCV33	\N	\N	\N	0101000000118E38E83B574040F590A7E7487F4140	2548
SCV34	1425.75	Waypoint	SCV34	SCV34	\N	\N	\N	0101000000B19684093857404028FF0CCF487F4140	2549
SCV35	1401.96	Waypoint	SCV35	SCV35	\N	\N	\N	01010000008D71F3FD3C574040E48D10413C7F4140	2550
SCV36	1392.3399999999999	Waypoint	SCV36	SCV36	\N	\N	\N	0101000000567498ED125740407B7967D85B7F4140	2551
SCV37	1394.51	Waypoint	SCV37	SCV37	\N	\N	\N	01010000004586DBF520574040F30C0DD0657F4140	2552
SCV38	1405.5599999999999	Waypoint	SCV38	SCV38	\N	\N	\N	0101000000066C679522574040CDFE1080577F4140	2553
SCV39	1388.02	Waypoint	SCV39	SCV39	\N	\N	\N	01010000009ECAB8951D574040A76EBF605D7F4140	2554
SCV4	1191.1900000000001	Waypoint	SCV4	SCV4	\N	\N	\N	0101000000B808590469574040CD1211BC29804140	2555
SCV40	1396.9100000000001	Waypoint	SCV40	SCV40	\N	\N	\N	01010000003C777B43335740404E291C323F7F4140	2556
SCV41	1407.73	Waypoint	SCV41	SCV41	\N	\N	\N	0101000000A39A80C9C756404042C68F709B7F4140	2557
SCV42	1398.1099999999999	Waypoint	SCV42	SCV42	\N	\N	\N	0101000000BF460476C4564040431D602A997F4140	2558
SCV43	1400.28	Waypoint	SCV43	SCV43	\N	\N	\N	0101000000D4FA749FD05640404297E7F68F7F4140	2559
SCV44	1414.9400000000001	Waypoint	SCV44	SCV44	\N	\N	\N	0101000000C59153B0D0564040849F7F558A7F4140	2560
SCV45	1394.75	Waypoint	SCV45	SCV45	\N	\N	\N	01010000003DF0BA49CF5640401149AB5C937F4140	2561
SCV46	1394.03	Waypoint	SCV46	SCV46	\N	\N	\N	0101000000662B97DC145740405E7070635F7F4140	2562
SCV47	1385.8599999999999	Waypoint	SCV47	SCV47	\N	\N	\N	0101000000E550243701574040326704BB647F4140	2563
SCV48	1378.4100000000001	Waypoint	SCV48	SCV48	\N	\N	\N	01010000004C6914CAF65640405CDFBF2D637F4140	2564
SCV49	1380.3299999999999	Waypoint	SCV49	SCV49	\N	\N	\N	01010000008E3FA7D9E9564040BEFF7877707F4140	2565
SCV50	1369.03	Waypoint	SCV50	SCV50	\N	\N	\N	010100000064B234C7245740405F3F483F6F7F4140	2566
SCV51	1390.6600000000001	Waypoint	SCV51	SCV51	\N	\N	\N	0101000000408224E4EA564040FEB0401C6D7F4140	2567
SCV52	1401.48	Waypoint	SCV52	SCV52	\N	\N	\N	010100000076BB03E1EF564040DFE7F0F7687F4140	2568
SCV53	1401.48	Waypoint	SCV53	SCV53	\N	\N	\N	010100000039FABB1EDC5640407055744C9F7F4140	2569
SCV54	1387.0599999999999	Waypoint	SCV54	SCV54	\N	\N	\N	0101000000AE274320DA56404085F72C3A9F7F4140	2570
SCV55	1378.1700000000001	Waypoint	SCV55	SCV55	\N	\N	\N	0101000000C51D6FF2DB564040DAB0FCB2A17F4140	2571
SCV56	1399.55	Waypoint	SCV56	SCV56	\N	\N	\N	0101000000C22ECC3A0A574040817B576A617F4140	2572
SCV57	1382.97	Waypoint	SCV57	SCV57	\N	\N	\N	01010000000594B39B0C574040BB0D3F3F597F4140	2573
SCV58	1382.97	Waypoint	SCV58	SCV58	\N	\N	\N	0101000000289C546908574040D6023930527F4140	2574
SCV59	1382.97	Waypoint	SCV59	SCV59	\N	\N	\N	0101000000280B806D14574040C6DDD4CB657F4140	2575
SCV-6	1123.1800000000001	Waypoint	SCV-6	SCV-6	\N	\N	\N	0101000000BF22801A855740407F5F307352804140	2576
SCV60	1376	Waypoint	SCV60	SCV60	\N	\N	\N	01010000001EF6A0721C574040BA0BE35A6C7F4140	2577
SCV61	1376	Waypoint	SCV61	SCV61	\N	\N	\N	010100000078C59863155740401758539B707F4140	2578
SCV63	1361.8199999999999	Waypoint	SCV63	SCV63	\N	\N	\N	010100000075D518812C5740407EEEE034787F4140	2581
SCV65	1379.1300000000001	Waypoint	SCV65	SCV65	\N	\N	\N	0101000000470F538DE45640403B7F8760A47F4140	2582
SCV66	1397.3900000000001	Waypoint	SCV66	SCV66	\N	\N	\N	010100000087DBE8363D5740407225840C397F4140	2583
SCV67	1404.3599999999999	Waypoint	SCV67	SCV67	\N	\N	\N	0101000000384F9B8F3057404037F36CD53C7F4140	2584
SCV69	1237.3299999999999	Waypoint	SCV69	SCV69	\N	\N	\N	01010000000AA6A4CBBE574040279A6068107F4140	2585
SCV69 A	1218.3499999999999	Waypoint	SCV69 A	SCV69 A	\N	\N	\N	01010000007221876FBE5740402BA217CF0F7F4140	2586
SCV7	1131.5899999999999	Waypoint	SCV7	SCV7	\N	\N	\N	0101000000852D8F988D574040AE85689354804140	2587
SCV70	1211.3800000000001	Waypoint	SCV70	SCV70	\N	\N	\N	01010000005C6E1737C1574040E1D4F0F5017F4140	2588
SCV71	1182.78	Waypoint	SCV71	SCV71	\N	\N	\N	0101000000890FB44ED157404049D66F93027F4140	2589
SCV72	1182.78	Waypoint	SCV72	SCV72	\N	\N	\N	0101000000A81BC00CDA57404075EB142FFF7E4140	2590
SCV-73	1231.0799999999999	Waypoint	SCV-73	SCV-73	\N	\N	\N	01010000002DBDA0FFCA574040578194B7E77E4140	2591
SCV-74	1234.21	Waypoint	SCV-74	SCV-74	\N	\N	\N	01010000003CC3B841CB574040B05F0BD5E87E4140	2592
SCV76	1373.5999999999999	Waypoint	SCV76	SCV76	\N	\N	\N	01010000000B73FCFCE9574040DE281702377F4140	2593
SCV77	1225.5599999999999	Waypoint	SCV77	SCV77	\N	\N	\N	0101000000B9754B33DC574040D3F8C773247F4140	2594
SCV78	1207.53	Waypoint	SCV78	SCV78	\N	\N	\N	0101000000BC6F845EE2574040D12794B3277F4140	2595
SCV79	1225.3199999999999	Waypoint	SCV79	SCV79	\N	\N	\N	0101000000AD14F846D157404080DF03972E7F4140	2596
SCV8	1208.97	Waypoint	SCV8	SCV8	\N	\N	\N	01010000009E0427BCB3574040D3A81B8329804140	2597
SCV80	1243.3399999999999	Waypoint	SCV80	SCV80	\N	\N	\N	0101000000CEC5C85ACF574040D0DCD826347F4140	2598
SCV81	1263.05	Waypoint	SCV81	SCV81	\N	\N	\N	0101000000B061EBF1B957404086F3AB64407F4140	2599
SCV82	1229.8800000000001	Waypoint	SCV82	SCV82	\N	\N	\N	01010000002D851C68B957404009B6C3BF457F4140	2600
SCV83	1255.8399999999999	Waypoint	SCV83	SCV83	\N	\N	\N	0101000000BA7384B3AB57404069EDFA284A7F4140	2601
SCV84	1265.45	Waypoint	SCV84	SCV84	\N	\N	\N	0101000000C1721770B5574040CD3DB7DF407F4140	2602
SCV-86	1109.48	Waypoint	SCV-86	SCV-86	\N	\N	\N	0101000000541F1034DB5740402A45DCB02E7E4140	2603
SCV-87	1081.3599999999999	Waypoint	SCV-87	SCV-87	\N	\N	\N	010100000074FAF3CDF357404019249BB05F7E4140	2604
SCV88	1121.01	Waypoint	SCV88	SCV88	\N	\N	\N	0101000000AD19F8D505584040A89BFB13AB7E4140	2605
SCV89	1134.47	Waypoint	SCV89	SCV89	\N	\N	\N	0101000000462D0C12D45740409167CC618D7E4140	2606
SCV9	1191.6700000000001	Waypoint	SCV9	SCV9	\N	\N	\N	0101000000E2CC17CAB557404024FA87BD28804140	2607
SCV91	1167.8800000000001	Waypoint	SCV91	SCV91	\N	\N	\N	0101000000D579A08AE557404096444C420F7E4140	2608
SCV92	1143.3699999999999	Waypoint	SCV92	SCV92	\N	\N	\N	0101000000B19F287D1F584040E999389BC97E4140	2609
SCV93	1146.97	Waypoint	SCV93	SCV93	\N	\N	\N	010100000088AC081313584040B252E88CCD7E4140	2610
SCV94	1131.3499999999999	Waypoint	SCV94	SCV94	\N	\N	\N	01010000001792707A07584040455F537DAD7E4140	2611
SCV95	1173.1700000000001	Waypoint	SCV95	SCV95	\N	\N	\N	0101000000F90BDDEF0758404047600CE2CC7E4140	2612
SCV96	1127.98	Waypoint	SCV96	SCV96	\N	\N	\N	0101000000F75EBB35E25740402138D8E22D7E4140	2613
SCV97	1115.49	Waypoint	SCV97	SCV97	\N	\N	\N	0101000000C18B2B1FE3574040338207B2107E4140	2614
SCV98	1117.6500000000001	Waypoint	SCV98	SCV98	\N	\N	\N	0101000000028A68A6015840401841AFB4567E4140	2615
SCV99	1143.3699999999999	Waypoint	SCV99	SCV99	\N	\N	\N	0101000000E1A4A8B47757404006EE573928804140	2616
SEDGEPT	83.759900000000002	Waypoint	SEDGEPT	SEDGEPT	\N	\N	\N	0101000000B555C07201AC4040ABCD20BA77604140	2617
SIASPILIOS	240.45400000000001	Waypoint	SIASPILIOS	SIASPILIOS	\N	\N	\N	0101000000AD3FBF7811B4404061EEDC79777B4140	2618
SKOURIOTIS	337.30599999999998	Waypoint	SKOURIOTIS	SKOURIOTIS	\N	\N	\N	0101000000B6588CE556724040C6AA0435838C4140	2619
SMH1	489.673	Waypoint	SMH1	SMH1	\N	\N	\N	0101000000C27C7CB9C48B4040D1467BAAAD834140	2620
SMH10	500.00799999999998	Waypoint	SMH10	SMH10	\N	\N	\N	010100000013EFFB95E08B40408AFB920496834140	2621
SMH11	512.745	Waypoint	SMH11	SMH11	\N	\N	\N	010100000050BDFB04CC8B4040CB2378768B834140	2622
SMH12	507.21699999999998	Waypoint	SMH12	SMH12	\N	\N	\N	0101000000784AF04ECA8B4040387EDB2587834140	2623
SMH13	513.947	Waypoint	SMH13	SMH13	\N	\N	\N	0101000000BBCCC7EDCB8B40409D7ADC6B8C834140	2624
SMH2	502.17099999999999	Waypoint	SMH2	SMH2	\N	\N	\N	0101000000446E6054CD8B4040344EFC7E95834140	2625
SMH4	489.91399999999999	Waypoint	SMH4	SMH4	\N	\N	\N	01010000006FCC6498D08B4040B319388490834140	2626
SMH6	514.42700000000002	Waypoint	SMH6	SMH6	\N	\N	\N	0101000000EA0FF04BCD8B4040A855B7248E834140	2627
SMH9	505.29500000000002	Waypoint	SMH9	SMH9	\N	\N	\N	01010000000FA48895DC8B404048872C778B834140	2628
SMI LYGOS	18.871500000000001	Waypoint	SMI LYGOS	SMI LYGOS	\N	\N	\N	01010000002F69F97200E33A408517383108D64240	2629
SPIL1	289.48000000000002	Flag	SPIL1	SPIL1	\N	\N	\N	01010000008685F0EE5CB340405466DB4D567D4140	2630
SPIL2	296.20999999999998	Flag	SPIL2	SPIL2	\N	\N	\N	01010000009086BBC95BB3404067D11095537D4140	2631
SPIL3	298.613	Flag	SPIL3	SPIL3	\N	\N	\N	0101000000C1D4F76358B34040D5AEB368547D4140	2632
SPIL4	299.815	Flag	SPIL4	SPIL4	\N	\N	\N	010100000004771C1655B3404027F71FBF557D4140	2633
SPIL5	298.13200000000001	Flag	SPIL5	SPIL5	\N	\N	\N	0101000000FDA09BEE56B340405024446B587D4140	2634
SPIL6	296.69	Flag	SPIL6	SPIL6	\N	\N	\N	01010000009CDBEC5E5AB340407A7F0FA0577D4140	2635
SPIL7	299.334	Flag	SPIL7	SPIL7	\N	\N	\N	0101000000B53F434459B34040E9168CDA557D4140	2636
ST1	364.70299999999997	Waypoint	ST1	ST1	\N	\N	\N	0101000000EC0F34698A1053C0232390E040394540	2637
ST2	366.38499999999999	Waypoint	ST2	ST2	\N	\N	\N	01010000009C85F15E8B1053C087FC2ECA3E394540	2638
ST4	368.548	Waypoint	ST4	ST4	\N	\N	\N	0101000000AFE3D1238C1053C0858528993B394540	2639
STV-10	1087.6099999999999	Waypoint	STV-10	STV-10	\N	\N	\N	01010000005D0E77D2DB50404079F7E68853844140	2640
STV11	1076.0699999999999	Waypoint	STV11	STV11	\N	\N	\N	0101000000555FC3F7F1504040A532588055844140	2641
STV-12	1070.0599999999999	Waypoint	STV-12	STV-12	\N	\N	\N	01010000006FBC8C79D5504040BA7440D059844140	2642
STV13	1072.23	Waypoint	STV13	STV13	\N	\N	\N	010100000056D383EFEE50404008B85FBF59844140	2643
STV-14	1079.4400000000001	Waypoint	STV-14	STV-14	\N	\N	\N	0101000000E2BB6CF2D6504040F115CB9751844140	2644
STV-15	1079.6800000000001	Waypoint	STV-15	STV-15	\N	\N	\N	010100000031396C5DD55040403387EBEB55844140	2645
STV-16	1079.4400000000001	Waypoint	STV-16	STV-16	\N	\N	\N	01010000000B20380BD4504040D11ED4EA50844140	2646
STV17	1074.6300000000001	Waypoint	STV17	STV17	\N	\N	\N	010100000021273FEECA5040402F7CFB7552844140	2647
STV18	1082.5599999999999	Waypoint	STV18	STV18	\N	\N	\N	0101000000413AA71FF5504040216508235D844140	2648
STV19	1070.0599999999999	Waypoint	STV19	STV19	\N	\N	\N	010100000058E017D6CE504040EE50D0CA56844140	2649
STV-20	1074.1500000000001	Waypoint	STV-20	STV-20	\N	\N	\N	01010000004C31AC87D45040406B71C83152844140	2650
STV21	1079.9200000000001	Waypoint	STV21	STV21	\N	\N	\N	0101000000A17274DEC4504040AD50375F4B844140	2651
STV22	1079.9200000000001	Waypoint	STV22	STV22	\N	\N	\N	0101000000B1FE0454CE5040402D4578CF4F844140	2652
STV23	1053.24	Waypoint	STV23	STV23	\N	\N	\N	010100000024635788BE5040409D308CB447844140	2653
STV24	1080.1600000000001	Waypoint	STV24	STV24	\N	\N	\N	0101000000BFDB172BC5504040E962F4584A844140	2654
STV25	1093.1400000000001	Waypoint	STV25	STV25	\N	\N	\N	0101000000EF4F44E7BE504040DE9E73F947844140	2655
STV26	1083.04	Waypoint	STV26	STV26	\N	\N	\N	01010000000DB34425BD5040402E90335C43844140	2656
STV27	1088.5699999999999	Waypoint	STV27	STV27	\N	\N	\N	01010000002F468FA5F0504040123FA3CD61844140	2657
STV28	1100.5899999999999	Waypoint	STV28	STV28	\N	\N	\N	010100000047D0C882EA504040FD05979B63844140	2658
STV29	1063.3399999999999	Waypoint	STV29	STV29	\N	\N	\N	010100000012524FD0BA5040402BAE485149844140	2659
STV-30	1091.45	Waypoint	STV-30	STV-30	\N	\N	\N	0101000000158F4495C55040401492502342844140	2660
STV-31	1096.5	Waypoint	STV-31	STV-31	\N	\N	\N	0101000000AF9B1F6DC65040408D50649444844140	2661
STV32	1083.04	Waypoint	STV32	STV32	\N	\N	\N	01010000002C73B82BBB504040F91374FF41844140	2662
STV33	1115.01	Waypoint	STV33	STV33	\N	\N	\N	01010000005FE6BF30AE504040D6108CEB2E844140	2663
STV-34	1080.4000000000001	Waypoint	STV-34	STV-34	\N	\N	\N	0101000000A64B5F05C15040401C70AC7737844140	2664
STV35	1115.25	Waypoint	STV35	STV35	\N	\N	\N	0101000000561C9CC2A9504040366810682E844140	2665
STV36	1114.29	Waypoint	STV36	STV36	\N	\N	\N	01010000005ECC2CC8AA5040405D10990B3F844140	2666
STV37	1125.8199999999999	Waypoint	STV37	STV37	\N	\N	\N	010100000099067D9DDF50404011F1AE316A844140	2667
STV38	1115.49	Waypoint	STV38	STV38	\N	\N	\N	01010000004DD337CEC55040403C95CCE46D844140	2668
STV-39	1099.1400000000001	Waypoint	STV-39	STV-39	\N	\N	\N	010100000087259C18BD504040B09AE4D02D844140	2669
STV40	1114.29	Waypoint	STV40	STV40	\N	\N	\N	01010000009F7A9797AB5040403352C42940844140	2670
STV41	1097.9400000000001	Waypoint	STV41	STV41	\N	\N	\N	01010000008C7B471BB4504040C06EF7C326844140	2671
STV43	1132.3099999999999	Waypoint	STV43	STV43	\N	\N	\N	010100000033AF8839BF504040A37CB8196B844140	2672
STV45	1117.4100000000001	Waypoint	STV45	STV45	\N	\N	\N	01010000005A96B73CB9504040859577C658844140	2673
STV47	977.05799999999999	Waypoint	STV47	STV47	\N	\N	\N	0101000000265120CD475140401EAD9F3B17834140	2674
STV48	969.36800000000005	Waypoint	STV48	STV48	\N	\N	\N	0101000000F345C49247514040C21D4F9B16834140	2675
STV-49	1037.8599999999999	Waypoint	STV-49	STV-49	\N	\N	\N	01010000002158E89250514040FE40E7A50A834140	2676
STV51	991.71799999999996	Waypoint	STV51	STV51	\N	\N	\N	0101000000E18930B054514040517878EB16834140	2677
STV-52	968.40599999999995	Waypoint	STV-52	STV-52	\N	\N	\N	010100000051AF24114B51404079CB68F31E834140	2678
STV53	1010.7	Waypoint	STV53	STV53	\N	\N	\N	0101000000348484A4665140406C19C49520834140	2679
STV54	982.10500000000002	Waypoint	STV54	STV54	\N	\N	\N	0101000000EFBEF06B60514040D9306BAB17834140	2680
STV-55	960.95600000000002	Waypoint	STV-55	STV-55	\N	\N	\N	0101000000CA50F444405840404AC438D8CB8A4140	2681
STV56	994.12199999999996	Waypoint	STV56	STV56	\N	\N	\N	0101000000725B3C6B6051404023929F8F1D834140	2682
STV57	1003.73	Waypoint	STV57	STV57	\N	\N	\N	01010000003E426F1F69514040508154091D834140	2683
STV58	980.18299999999999	Waypoint	STV58	STV58	\N	\N	\N	0101000000D537382A5A5140408249F4A918834140	2684
STV59	1027.29	Waypoint	STV59	STV59	\N	\N	\N	01010000000BD843387C5140405B5F8C971A834140	2685
STV6	1069.8199999999999	Waypoint	STV6	STV6	\N	\N	\N	0101000000D82C1355EA5040402C54F7FB57844140	2686
STV60	1035.9400000000001	Waypoint	STV60	STV60	\N	\N	\N	0101000000BE88EB557A514040D66B70DE1C834140	2687
STV61	885.97400000000005	Waypoint	STV61	STV61	\N	\N	\N	0101000000F7DABFF4AE4F4040A5C09F6DF77F4140	2688
STV62	891.50199999999995	Waypoint	STV62	STV62	\N	\N	\N	0101000000F6C02C8CAB4F40404E505363F37F4140	2689
STV63	889.09799999999996	Waypoint	STV63	STV63	\N	\N	\N	01010000007D2B2BB0B44F4040A2921CABF27F4140	2690
STV64	899.19200000000001	Waypoint	STV64	STV64	\N	\N	\N	0101000000C3DB9CADB04F4040B4578CABEB7F4140	2691
STV65	888.85799999999995	Waypoint	STV65	STV65	\N	\N	\N	0101000000189E4844B94F40400D53F306EC7F4140	2692
STV66	895.58699999999999	Waypoint	STV66	STV66	\N	\N	\N	0101000000599ACB75A94F4040C6BF88E3E47F4140	2693
STV68	891.26099999999997	Waypoint	STV68	STV68	\N	\N	\N	0101000000BEDB0800984F40406497F46CE07F4140	2694
STV7	1055.4100000000001	Waypoint	STV7	STV7	\N	\N	\N	0101000000E38A44CEE65040407E9FC02755844140	2695
STV-8	1072.23	Waypoint	STV-8	STV-8	\N	\N	\N	0101000000C467C71AE95040401D9DD4465E844140	2696
STV9	1072.95	Waypoint	STV9	STV9	\N	\N	\N	01010000001020CCB8EC50404050BC44A157844140	2697
SULTAN MUR	2202.25	Waypoint	SULTAN MUR	SULTAN MUR	\N	\N	\N	0101000000B61834DB32134440725013945A524440	2698
SWALL	91.930999999999997	Waypoint	SWALL	SWALL	\N	\N	\N	01010000009DE3D4B513AC4040BBA88F2092604140	2699
TE	252.71000000000001	Flag	TE	TE	\N	\N	\N	010100000049ADF06108B340401800AD70807E4140	2700
TE2	252.71000000000001	Flag	TE2	TE2	\N	\N	\N	010100000046BC935408B340402F8D2C87807E4140	2701
TEST1	251.50899999999999	Waypoint	TEST1	TEST1	\N	\N	\N	0101000000B704F73B08B3404050041D1F807E4140	2702
TEST2	250.06700000000001	Waypoint	TEST2	TEST2	\N	\N	\N	01010000007C15C06408B34040A9FF837A807E4140	2703
TGC10	335.86399999999998	Waypoint	TGC10	TGC10	\N	\N	\N	01010000009C7809793D1D53C0D7FD4C85843A4540	2704
TGC11	315.67599999999999	Waypoint	TGC11	TGC11	\N	\N	\N	01010000003434CE41381D53C0CAA81BF0853A4540	2705
TGC12	282.99200000000002	Waypoint	TGC12	TGC12	\N	\N	\N	01010000003C839D9AFF1D53C041008587A43A4540	2706
TGC13	284.91399999999999	Waypoint	TGC13	TGC13	\N	\N	\N	01010000009C27E840001E53C009F0DBE5A23A4540	2707
TGC14	294.76799999999997	Waypoint	TGC14	TGC14	\N	\N	\N	0101000000B78F93C4FD1D53C025FD0C5BB23A4540	2708
TGC15	293.08499999999998	Waypoint	TGC15	TGC15	\N	\N	\N	0101000000E7B5A71E081E53C023EA9BBA863A4540	2709
TGC3	287.31799999999998	Waypoint	TGC3	TGC3	\N	\N	\N	0101000000EC332A1A121E53C066D51F158A3A4540	2710
TGC4	290.92200000000003	Waypoint	TGC4	TGC4	\N	\N	\N	0101000000B5975353091E53C0544A90908F3A4540	2711
TGC5	306.78399999999999	Waypoint	TGC5	TGC5	\N	\N	\N	010100000046AAF76D4B1D53C026C6077D763A4540	2712
TGC6	306.78399999999999	Waypoint	TGC6	TGC6	\N	\N	\N	0101000000901E24E34F1D53C03121DC20773A4540	2713
TGC7	306.78399999999999	Waypoint	TGC7	TGC7	\N	\N	\N	0101000000901E24E34F1D53C03121DC20773A4540	2714
TGC8	391.37900000000002	Waypoint	TGC8	TGC8	\N	\N	\N	0101000000C869FC808F6753C0D0E8671AAFE54540	2715
TGC9	274.33999999999997	Waypoint	TGC9	TGC9	\N	\N	\N	0101000000B512CC2E3E1D53C085F89CC8873A4540	2716
TGO	121.732	Waypoint	TGO	TGO	\N	\N	\N	010100000004550B3BF8904140A28487D3835B4040	2717
THB1	-15.014799999999999	Waypoint	THB1	THB1	\N	\N	\N	0101000000AB8466583E8F3740584DC32F76A24140	2718
THB10	-15.7357	Waypoint	THB10	THB10	\N	\N	\N	010100000064397188248F37409639900778A24140	2719
THB-11	-10.208299999999999	Waypoint	THB-11	THB-11	\N	\N	\N	0101000000EDF7F92C258F37405DC13B9B83A24140	2720
THB12	-7.5645699999999998	Waypoint	THB12	THB12	\N	\N	\N	0101000000959D41EDFE8E37400F84BFBA85A24140	2721
THB-13	-8.2856500000000004	Waypoint	THB-13	THB-13	\N	\N	\N	010100000052FFF8C9D38E37404CD05C3083A24140	2722
THB-14	25.360199999999999	Waypoint	THB-14	THB-14	\N	\N	\N	01010000003617FE5AFE8E374062DA883E57A34140	2723
THB-2	-32.318399999999997	Waypoint	THB-2	THB-2	\N	\N	\N	0101000000570A6AC06C8F374055D6E0C46AA24140	2724
THB-3	-10.9292	Waypoint	THB-3	THB-3	\N	\N	\N	0101000000147F9F676F8F374047671CC768A24140	2725
THB4	-15.014799999999999	Waypoint	THB4	THB4	\N	\N	\N	0101000000C35F387C778F3740490DCB7177A24140	2726
THB5	-13.0922	Waypoint	THB5	THB5	\N	\N	\N	0101000000A9FBE907C1A03740B874649611A64140	2727
THB-6	-7.8049299999999997	Waypoint	THB-6	THB-6	\N	\N	\N	01010000003D83B17E668F3740B4C8FFAF7DA24140	2728
THB7	-12.3712	Waypoint	THB7	THB7	\N	\N	\N	0101000000AAD9389F908F3740EBFA5E7369A24140	2729
THB8	-8.7662300000000002	Waypoint	THB8	THB8	\N	\N	\N	01010000002307313F538F374063DA9C227EA24140	2730
THB-9	-8.7662300000000002	Waypoint	THB-9	THB-9	\N	\N	\N	010100000007FAFFC9438F3740466B63F17DA24140	2731
TODD2	96.737700000000004	Waypoint	TODD2	TODD2	\N	\N	\N	0101000000B4DBA3414EAC40405FAC14A7FF604140	2732
TODDTOMB	98.900599999999997	Waypoint	TODDTOMB	TODDTOMB	\N	\N	\N	01010000009079EC804EAC4040091C7B8900614140	2733
TOMB1	397.62799999999999	Flag	TOMB1	TOMB1	\N	\N	\N	0101000000B1C55CBCD99340402772E42810864140	2734
TPW3	271.69600000000003	Waypoint	TPW3	TPW3	\N	\N	\N	01010000003CDCC737BF1E53C017AC5C9B123B4540	2735
TPW4	282.02999999999997	Waypoint	TPW4	TPW4	\N	\N	\N	0101000000BFC4BDD1C51E53C0CDCE4406293B4540	2736
TPW5	290.44200000000001	Waypoint	TPW5	TPW5	\N	\N	\N	0101000000EBB90377CB1E53C006EFC7C7103B4540	2737
TPW6	278.90600000000001	Waypoint	TPW6	TPW6	\N	\N	\N	010100000082B54BCAC61E53C0792F08F6293B4540	2738
TREE A KD	177.488	Waypoint	TREE A KD	TREE A KD	\N	\N	\N	01010000007C45BC174DA14040840F677604664140	2759
TREE B KD	174.364	Waypoint	TREE B KD	TREE B KD	\N	\N	\N	0101000000EADADC6D5EA14040814E4838DB654140	2760
TREE C KD	155.137	Waypoint	TREE C KD	TREE C KD	\N	\N	\N	0101000000BB0DE1CD17A240401A588F4765664140	2761
TREE D KD	147.68700000000001	Waypoint	TREE D KD	TREE D KD	\N	\N	\N	0101000000F13AAB9D12A24040E92BFCA450664140	2762
TREE E KD	147.68700000000001	Waypoint	TREE E KD	TREE E KD	\N	\N	\N	01010000003EFB8A680DA24040C007B41654664140	2763
TREE F MV	272.65800000000002	Waypoint	TREE F MV	TREE F MV	\N	\N	\N	010100000019BF444AE0AD4040FED5CC2260634140	2764
TREE G MV	267.13	Waypoint	TREE G MV	TREE G MV	\N	\N	\N	0101000000DC6C288CD8AD40406C0707A15A634140	2765
TREE H MV	267.851	Waypoint	TREE H MV	TREE H MV	\N	\N	\N	010100000094114F2AE5AD40402110A378B9634140	2766
TREESMPL1	258.71899999999999	Waypoint	TREESMPL1	TREESMPL1	\N	\N	\N	0101000000BF2FD0E3E7AD4040766CB85370634140	2767
TROY 1 TRE	-2.75806	Waypoint	TROY 1 TRE	TROY 1 TRE	\N	\N	\N	0101000000A15BCEF6FF3C3A40E7A878638EFA4340	2768
TROY MEG 2	50.114100000000001	Waypoint	TROY MEG 2	TROY MEG 2	\N	\N	\N	0101000000F29819E7033D3A400DCB88D395FA4340	2769
TROY MEG2A	28.725000000000001	Waypoint	TROY MEG2A	TROY MEG2A	\N	\N	\N	01010000008BA51885FC3C3A40AF87F4B097FA4340	2770
TRT-4	299.334	Waypoint	TRT-4	TRT-4	\N	\N	\N	0101000000F684133BB42553C0EE8567E1A9334540	2771
TRT-5	404.35700000000003	Waypoint	TRT-5	TRT-5	\N	\N	\N	0101000000E1ABB3E0B92553C0D920EC44A4334540	2772
TRT-6	267.61099999999999	Waypoint	TRT-6	TRT-6	\N	\N	\N	010100000060E209699F2553C063C183BA96334540	2773
TRT-7	264.96699999999998	Waypoint	TRT-7	TRT-7	\N	\N	\N	0101000000A62568299F2553C0D546002D99334540	2774
TUSAN	52.7577	Waypoint	TUSAN	TUSAN	\N	\N	\N	0101000000A6F981B0F5553A4096DA83F7F2044440	2775
UC AYAK	1162.5899999999999	Waypoint	UC AYAK	UC AYAK	\N	\N	\N	01010000001B9F78B7FA1E414021132BCE11B34340	2776
URGUP	1315.9200000000001	Waypoint	URGUP	URGUP	\N	\N	\N	01010000003D57A085E0504240F20AF8C004A74340	2777
V10	18.871500000000001	Waypoint	V10	V10	\N	\N	\N	01010000002C4DD07AC6B04040D64413E6855F4140	2778
V11	16.948899999999998	Waypoint	V11	V11	\N	\N	\N	0101000000B049C851CAB04040AECE8F917C5F4140	2779
V12	16.227900000000002	Waypoint	V12	V12	\N	\N	\N	01010000004CBF2B1FD5B0404094C7D03A755F4140	2780
V13	14.7859	Waypoint	V13	V13	\N	\N	\N	0101000000936C4047D5B0404046C1FBC6715F4140	2781
V2	23.197399999999998	Waypoint	V2	V2	\N	\N	\N	010100000059B48C16D8B040403D7BF4A7895F4140	2782
V3	24.8796	Waypoint	V3	V3	\N	\N	\N	0101000000C902930CDAB04040323F6C26755F4140	2783
V4	20.313400000000001	Waypoint	V4	V4	\N	\N	\N	0101000000DAB64BC2D5B040400145C736725F4140	2784
V5	23.197399999999998	Waypoint	V5	V5	\N	\N	\N	0101000000C29DB069CAB040405E3DA0CC7C5F4140	2785
V6	23.918299999999999	Waypoint	V6	V6	\N	\N	\N	0101000000B2776FDAC6B040400608352F865F4140	2786
V7	21.755400000000002	Waypoint	V7	V7	\N	\N	\N	0101000000BB87ACB7CBB04040DC4FDF95915F4140	2787
V8	16.4681	Waypoint	V8	V8	\N	\N	\N	01010000000244400CD4B04040546EC3A48F5F4140	2788
V9	13.8246	Waypoint	V9	V9	\N	\N	\N	0101000000F25A3CCECAB040408FAF4CDE915F4140	2789
VAVLA	492.077	Waypoint	VAVLA	VAVLA	\N	\N	\N	01010000005014F08441A2404071DDE8F1A86B4140	2790
VGATE V1	19.592400000000001	Waypoint	VGATE V1	VGATE V1	\N	\N	\N	01010000002EB3FB9AD4B04040685C3810925F4140	2791
VOUNOS	390.41800000000001	Flag	VOUNOS	VOUNOS	\N	\N	\N	01010000001C278FEB82A54040828C7B51CD804140	2792
WHEAT1	250.30699999999999	Waypoint	WHEAT1	WHEAT1	\N	\N	\N	01010000007229D7A9ECAD4040978558F858634140	2793
WHEAT2	27.0426	Waypoint	WHEAT2	WHEAT2	\N	\N	\N	010100000049B417E3D9B04040EB264F5E865F4140	2794
WHEAT3	58.765700000000002	Waypoint	WHEAT3	WHEAT3	\N	\N	\N	0101000000CE7D0C3256A64040356D48A1C1614140	2795
YETIMLER	56.362499999999997	Waypoint	YETIMLER	YETIMLER	\N	\N	\N	010100000011FBDE88E9743A401B0D481145E44340	2796
ZKB DEPOT	540.14200000000005	Waypoint	ZKB DEPOT	ZKB DEPOT	\N	\N	\N	010100000066225B4C624E4040DE0075FB939C4440	2797
ZP1	6.37439	Waypoint	ZP1	ZP1	\N	\N	\N	0101000000EE4BE7D757AA4040B478EBDB8D5C4140	2798
\.


--
-- Data for Name: tblgpsmaster; Type: TABLE DATA; Schema: public; Owner: aps03pwb
--

COPY tblgpsmaster (name, elevation, symbol, "comment", descriptio, source, url, "url name", the_geom, gpsmasterid) FROM stdin;
009a	-6.6033900000000001	Waypoint	009	009	\N	\N	\N	01010000008C73D6D21C593B40BFD37B62D14D4140	1265
009b	108.033	Waypoint	009	009	\N	\N	\N	0101000000865CB8922EDC43405CCA7071D1744440	1266
009c	294.28699999999998	Waypoint	009	009	\N	\N	\N	010100000021C9E9417A343840F05C87869F944740	1267
010a	-6.6033900000000001	Waypoint	010	010	\N	\N	\N	01010000000B20380BD4504040D11ED4EA50844140	1268
010b	292.84500000000003	Waypoint	010	010	\N	\N	\N	01010000005DDB8F9F8334384064B6948E9A944740	1269
010c	325.52999999999997	Flag	010	010	\N	\N	\N	01010000001806F7C6BAB140402C611C455E7A4140	1270
010d	1610.8	Waypoint	010	010	\N	\N	\N	01010000006CE2A061CA3F4240CB996C8AA77C4240	1271
011a	301.01600000000002	Waypoint	011	011	\N	\N	\N	0101000000FD4FC18B853438407966CBB793944740	1272
011b	313.51299999999998	Flag	011	011	\N	\N	\N	0101000000381B905C75AB404040325F564F7F4140	1273
011c	449.05799999999999	Flag	011	011	\N	\N	\N	01010000000BFEDF5AEB9B40408A13CF41A6814140	1274
011d	1610.5599999999999	Waypoint	011	011	\N	\N	\N	0101000000D4B47785BF3F4240E138C8A2AB7C4240	1275
012a	-6.6033900000000001	Waypoint	012	012	\N	\N	\N	01010000009D6C3869F02D53C086727C96E2314540	1276
012b	251.749	Flag	012	012	\N	\N	\N	010100000082C3184C14B34040C47D609E807E4140	1277
012c	298.13200000000001	Waypoint	012	012	\N	\N	\N	0101000000D023CFC08B343840C3BE237E93944740	1278
012d	484.62700000000001	Flag	012	012	\N	\N	\N	010100000096F6B718849A4040FBFD9A6043814140	1279
012e	1610.5599999999999	Waypoint	012	012	\N	\N	\N	0101000000DA501831BF3F4240FF04759CAB7C4240	1280
013a	251.749	Flag	013	013	\N	\N	\N	0101000000FBD8FC7614B340401DDCD0A6807E4140	1281
013b	292.84500000000003	Waypoint	013	013	\N	\N	\N	0101000000834BFF46C6343840638733EADA944740	1282
013c	500.24799999999999	Flag	013	013	\N	\N	\N	01010000007B95CB32839A4040A58CABD671814140	1283
013d	1612.24	Waypoint	013	013	\N	\N	\N	01010000006CEDD863BD3F4240D49D943AA27C4240	1284
014a	251.749	Flag	014	014	\N	\N	\N	0101000000F599CF9314B34040030181BA807E4140	1285
014b	288.75999999999999	Waypoint	014	014	\N	\N	\N	0101000000D90CC188B534384051F6241DD6944740	1286
014c	407.72199999999998	Flag	014	014	\N	\N	\N	0101000000742AC047E29B4040A21FD8B4FC824140	1287
014d	1609.1199999999999	Waypoint	014	014	\N	\N	\N	0101000000A3FD8E2FC13F4240DB7994AAAA7C4240	1288
015a	247.18299999999999	Flag	015	015	\N	\N	\N	0101000000ED00B08308B34040CD444BFD807E4140	1289
015b	299.815	Waypoint	015	015	\N	\N	\N	01010000008EF0C8E898343840A99198CB86944740	1290
015c	392.10000000000002	Flag	015	015	\N	\N	\N	0101000000EEF8521F589C40409631D3301B834140	1291
015d	1691.55	Waypoint	015	015	\N	\N	\N	01010000004C9F716FEB693640FAC35F2B12F54240	1292
016a	247.18299999999999	Flag	016	016	\N	\N	\N	0101000000CAEF329806B34040A65440DC807E4140	1293
016b	296.93099999999998	Waypoint	016	016	\N	\N	\N	0101000000027C6632643438402636F456EA944740	1294
016c	398.10899999999998	Flag	016	016	\N	\N	\N	0101000000D810985E4D9C404048D67CBD04834140	1295
016d	1689.6300000000001	Waypoint	016	016	\N	\N	\N	010100000085CDD0DD0D434040F3537F31991B4240	1296
017a	248.14400000000001	Flag	017	017	\N	\N	\N	0101000000B37FA3BF05B3404029F18BDB807E4140	1297
017b	288.03899999999999	Waypoint	017	017	\N	\N	\N	0101000000F2B82E5060343840FF856CC0EA944740	1298
017c	346.43799999999999	Flag	017	017	\N	\N	\N	0101000000B3D58723BFB14040A5931474557A4140	1299
017d	1686.51	Waypoint	017	017	\N	\N	\N	0101000000D6BE6C7A11434040132B4067941B4240	1300
018a	288.51900000000001	Flag	018	018	\N	\N	\N	010100000020518C1F2FAB40406F71AB257F7F4140	1301
018b	313.51299999999998	Waypoint	018	018	\N	\N	\N	0101000000D3061676073438405D68531AD6944740	1302
018c	323.84699999999998	Flag	018	018	\N	\N	\N	01010000006900440168AB40405E0568C3707F4140	1303
018d	1686.75	Waypoint	018	018	\N	\N	\N	0101000000F199BC6611434040F9AC6343941B4240	1304
019a	298.613	Waypoint	019	019	\N	\N	\N	0101000000DCACE008E9333840A989E0ADE3944740	1305
019b	316.63799999999998	Flag	019	019	\N	\N	\N	0101000000C669A47F6BAB4040E1FE268B707F4140	1306
019c	407.48099999999999	Flag	019	019	\N	\N	\N	0101000000BF1B9BB5459440402370D0D012864140	1307
019d	1421.1800000000001	Waypoint	019	019	\N	\N	\N	0101000000443F8030A3A84240FBB74746B9E14240	1308
067b 	317.11799999999999	Flag	067	067	\N	\N	\N	0101000000E8401B284CAA4040861978C0457F4140	1417
067a	302.93900000000002	Waypoint	067	067	\N	\N	\N	01010000002A9A5086BFB34040E2008828577D4140	1416
066b	319.041	Waypoint	066	066	\N	\N	\N	010100000066B1D0AB9AAB404017E128A4757F4140	1415
066a	317.11799999999999	Flag	066	066	\N	\N	\N	01010000008B57B49447AA4040BEC617B5477F4140	1414
065b	320.72300000000001	Flag	065	065	\N	\N	\N	0101000000886FECCF01AA40401B395451517F4140	1413
065a	318.80099999999999	Waypoint	065	065	\N	\N	\N	01010000004939BB5C94AB40404C7AD83E767F4140	1412
064b	320.72300000000001	Flag	064	064	\N	\N	\N	010100000043DF5C4ABDA940404795D8EA6F7F4140	1411
064a	320.00200000000001	Waypoint	064	064	\N	\N	\N	0101000000D3DE0B8C6FAB40402BC5E4C65A7F4140	1410
063a	317.839	Flag	063	063	\N	\N	\N	0101000000ECE87346B8A9404010B710D27D7F4140	1409
063b	313.27300000000002	Waypoint	063	063	\N	\N	\N	0101000000D3A2AB658CAB40408FFF0CB3737F4140	1408
062a	319.762	Flag	062	062	\N	\N	\N	01010000000DE600D8B8A940401F1A9CDC7D7F4140	1407
062b	313.51299999999998	Waypoint	062	062	\N	\N	\N	0101000000AC0F140D8CAB4040517CEC96737F4140	1406
061a	348.36099999999999	Flag	061	061	\N	\N	\N	0101000000DF08049AC5A94040F92F57D9D37F4140	1405
061b	313.75400000000002	Waypoint	061	061	\N	\N	\N	01010000003BAAC0E78CAB404095BE40AB747F4140	1404
060a	404.59800000000001	Flag	060	060	\N	\N	\N	0101000000610ED1D42A9840402227377DC2854140	1403
060b	313.75400000000002	Waypoint	060	060	\N	\N	\N	01010000003B0734B08CAB40409EE834B7747F4140	1402
059a	407.48099999999999	Flag	059	059	\N	\N	\N	0101000000AB3D243021984040C3B77B7CAC854140	1401
059b	313.75400000000002	Waypoint	059	059	\N	\N	\N	01010000002C5E8F368CAB4040E54F3070747F4140	1400
058a	313.27300000000002	Waypoint	058	058	\N	\N	\N	01010000009B5EB82C8CAB404059E6632D747F4140	1399
058b	215.69999999999999	Flag	058	058	\N	\N	\N	01010000001256C37B57B240405A819300848A4140	1398
057a	312.31200000000001	Waypoint	057	057	\N	\N	\N	0101000000FA6F233A8BAB404009B2E730747F4140	1397
057b	225.31299999999999	Flag	057	057	\N	\N	\N	0101000000B3381F7B56B24040B6B370D8848A4140	1396
056a	407.00099999999998	Flag	056	056	\N	\N	\N	0101000000FEF8220C09A74040EB4A57F2E97D4140	1395
056b	311.35000000000002	Waypoint	056	056	\N	\N	\N	01010000009C2FECEC88AB404056989357747F4140	1394
055	311.35000000000002	Waypoint	055	055	\N	\N	\N	01010000003AA787D888AB4040C72610AE747F4140	1393
049b	809.06899999999996	Waypoint	049	049	\N	\N	\N	010100000000A927D5F9524040B0AA4F533A7A4140	1387
049a	221.708	Flag	049	049	\N	\N	\N	01010000003C541BE856B2404087B9A7228A8A4140	1386
048b	808.34799999999996	Waypoint	048	048	\N	\N	\N	0101000000C51664C6F95240405F19608E3A7A4140	1385
048a	351.245	Flag	048	048	\N	\N	\N	010100000069F3D7E2D5A9404027ECE7BC627D4140	1384
047b	788.40099999999995	Waypoint	047	047	\N	\N	\N	0101000000EFD438A8F85240402A3A9784397A4140	1383
047a	385.61200000000002	Flag	047	047	\N	\N	\N	010100000043E188F553A54040189D6B19627F4140	1382
046b	618.97000000000003	Waypoint	046	046	\N	\N	\N	0101000000AD03CCEE9C5B404048D42F0405794140	1381
046a	429.83199999999999	Flag	046	046	\N	\N	\N	0101000000A9DF649FB0A44040A18978370B7F4140	1380
045b	464.43900000000002	Waypoint	045	045	\N	\N	\N	01010000008D33B3BA085A404032F186B52A774140	1379
045a	431.995	Flag	045	045	\N	\N	\N	0101000000158F60EAB4A4404008899BF01E7F4140	1378
044b	472.37	Waypoint	044	044	\N	\N	\N	01010000008834FC37075A4040367C70162D774140	1377
044a	319.52100000000002	Flag	044	044	\N	\N	\N	01010000009E0CDFD956B1404005B0D3D8767F4140	1376
043b	473.09100000000001	Waypoint	043	043	\N	\N	\N	010100000086C39815065A40402F89FFB02F774140	1375
043a	297.411	Flag	043	043	\N	\N	\N	01010000008625EB5E11B14040124FAB2533804140	1374
042b	471.40899999999999	Waypoint	042	042	\N	\N	\N	0101000000DB9958CC075A404087F3A8AC31774140	1373
042a	283.47199999999998	Flag	042	042	\N	\N	\N	0101000000E2C45FAC12B240407A71E764D77E4140	1372
041b	264.00599999999997	Flag	041	041	\N	\N	\N	01010000007C579FD3F5B240402CD40C3C467E4140	1371
041a	56.602899999999998	Waypoint	041	041	\N	\N	\N	0101000000C1C63CC5860B2D40C60B548C94F14140	1370
040b	261.60300000000001	Flag	040	040	\N	\N	\N	0101000000B84C6C8FF5B240400FABEC79467E4140	1369
040a	56.602899999999998	Waypoint	040	040	\N	\N	\N	0101000000C1C63CC5860B2D40C60B548C94F14140	1368
039b	239.012	Flag	039	039	\N	\N	\N	0101000000222E0345A1B94040DD370884A77E4140	1367
039a	56.602899999999998	Waypoint	039	039	\N	\N	\N	0101000000400E10EDDF052D40E0115C7FA7F24140	1366
038b	233.965	Flag	038	038	\N	\N	\N	0101000000F73FC05A75B94040424EC0F8A77E4140	1365
038a	89.768100000000004	Waypoint	038	038	\N	\N	\N	0101000000E8ACFB3958AC404055F12890B7604140	1364
037b	222.18899999999999	Flag	037	037	\N	\N	\N	0101000000358B5CDF63B940406AD6B4B3717E4140	1363
037a	122.93300000000001	Waypoint	037	037	\N	\N	\N	0101000000B702CB9031AC404052830BA7BC604140	1362
036b	208.00899999999999	Flag	036	036	\N	\N	\N	0101000000C4E223A16EBB4040B75FCC03497B4140	1361
036a	122.93300000000001	Waypoint	036	036	\N	\N	\N	0101000000B702CB9031AC404052830BA7BC604140	1360
035b	207.28800000000001	Flag	035	035	\N	\N	\N	010100000064113C1E70BB4040011B3BA14A7B4140	1359
035a	122.93300000000001	Waypoint	035	035	\N	\N	\N	0101000000B702CB9031AC404052830BA7BC604140	1358
034b	381.52600000000001	Flag	034	034	\N	\N	\N	01010000009910A3822B9D4040358E5B439E834140	1357
034a	123.17400000000001	Waypoint	034	034	\N	\N	\N	010100000025CC4C1A37AC40408A6A8B17BD604140	1356
033b	389.21699999999998	Flag	033	033	\N	\N	\N	01010000006F482CF4049D4040F91F08ACB6834140	1355
033a	97.218299999999999	Waypoint	033	033	\N	\N	\N	0101000000DBD0671C37AC4040197F9BF8BC604140	1354
032b	389.21699999999998	Flag	032	032	\N	\N	\N	0101000000F7FDD87AFF9C40403DA2DF4AB8834140	1353
032a	279.14600000000002	Waypoint	032	032	\N	\N	\N	01010000001806480EFAAD4040DA0DD3A55C634140	1352
031b	396.18599999999998	Flag	031	031	\N	\N	\N	010100000031822B78089D40408B24986F9F834140	1351
031a	23.4376	Waypoint	031	031	\N	\N	\N	0101000000953498E9CBB040406B64EF76915F4140	1350
030b	1113.8	Waypoint	030	030	\N	\N	\N	0101000000A548984B258B41406B142FA26F804040	1349
030a	621.13300000000004	Waypoint	030	030	\N	\N	\N	01010000008CD1210DFAA821400AB7E4075A394740	1348
030c	394.26299999999998	Flag	030	030	\N	\N	\N	010100000009B81079059D4040A55FB8F99A834140	1347
030d	317.35899999999998	Flag	030	030	\N	\N	\N	0101000000176B43BB46A94040C74C23DFC57F4140	1346
029b	466.60199999999998	Flag	029	029	\N	\N	\N	0101000000F6DAB0C9419D40401A6F2BBD36834140	1344
029c	324.32799999999997	Flag	029	029	\N	\N	\N	010100000001FD569C7EA94040FA6A1480E97F4140	1343
029d	179.65100000000001	Waypoint	029	029	\N	\N	\N	0101000000E1048D361ADF2240F1BE34899DCA4640	1342
028a	1352.9300000000001	Waypoint	028	028	\N	\N	\N	01010000007D6B4657002D35404CA770629AE84340	1341
028b	1113.8	Waypoint	028	028	\N	\N	\N	0101000000E6C250A0F257404066ED5699607E4140	1340
028c	321.92500000000001	Flag	028	028	\N	\N	\N	0101000000313C7FA5B6A94040A7660743BA7F4140	1339
027a	676.88900000000001	Waypoint	027	027	\N	\N	\N	010100000054E6F5467EB435406341BFC1E7274440	1338
027b	612.48099999999999	Waypoint	027	027	\N	\N	\N	01010000008D0F2AA68FA639400B6D8CDFCC884140	1337
027c	477.41699999999997	Flag	027	027	\N	\N	\N	0101000000AA014DFB9B9340402B51D0CF4F864140	1336
027d	254.15199999999999	Flag	027	027	\N	\N	\N	0101000000125480348BAF40405811D0A541824140	1335
026a	573.06700000000001	Waypoint	026	026	\N	\N	\N	01010000004496282213A73940FCFF8E61FB884140	1334
026b	477.65699999999998	Flag	026	026	\N	\N	\N	0101000000DD0613519C9340404E10FB584F864140	1333
026c	36.896000000000001	Waypoint	026	026	\N	\N	\N	01010000003C13B679456D3B40AD961824AD034340	1332
025a	814.11599999999999	Waypoint	025	025	\N	\N	\N	01010000005BA140862B763F40F1753352185C4440	1331
025b	578.83500000000004	Waypoint	025	025	\N	\N	\N	0101000000923018CB10A73940C78CAB2200894140	1330
025c	475.49400000000003	Flag	025	025	\N	\N	\N	0101000000BE061CC09D9340405A0E5C3450864140	1329
025d	168.596	Flag	025	025	\N	\N	\N	01010000009C30C45EA6AD4040F97960BB4C954140	1328
024a	453.86500000000001	Flag	024	024	\N	\N	\N	01010000007208154F8F93404022078FB054864140	1327
024b	439.44499999999999	Waypoint	024	024	\N	\N	\N	0101000000AD0F4EB8157D39407C0F50476B9D4440	1326
024c	307.024	Waypoint	024	024	\N	\N	\N	0101000000BE525E77AF3338401EA80C49D1944740	1325
024d	166.91300000000001	Flag	024	024	\N	\N	\N	0101000000A5D41B71A5AD4040D07B084F4D954140	1324
023a	474.29199999999997	Flag	023	023	\N	\N	\N	01010000003B44046598934040DEF89BCF47864140	1323
023b	421.90100000000001	Waypoint	023	023	\N	\N	\N	0101000000C5FFD67B157D3940BBCC50DF699D4440	1322
023c	304.14100000000002	Waypoint	023	023	\N	\N	\N	0101000000D59AB8E5BB333840FE90C888D5944740	1321
023d	153.45500000000001	Flag	023	023	\N	\N	\N	0101000000FCE4BBA3A5AD4040AFC4942C4D954140	1320
022a	464.43900000000002	Flag	022	022	\N	\N	\N	0101000000AE8C4386A0934040559107AB3C864140	1319
022b	422.38200000000001	Waypoint	022	022	\N	\N	\N	01010000003D5E3E15147D3940557CB8EE6A9D4440	1318
022c	319.762	Flag	022	022	\N	\N	\N	01010000003C4B6F0367AB40406E4560816F7F4140	1317
022d	310.38900000000001	Waypoint	022	022	\N	\N	\N	0101000000B831FEFCC5333840B86FABDCE8944740	1316
021e	468.52499999999998	Flag	021	021	\N	\N	\N	0101000000032B7FA590934040AF0469FE4C864140	1315
021f	346.678	Flag	021	021	\N	\N	\N	0101000000F1A1DC6769AB40409726C8AF6F7F4140	1314
021g	269.29300000000001	Waypoint	021	021	\N	\N	\N	01010000009503A86FBA3338407561B8FBDB944740	1313
020a	1418.54	Waypoint	020	020	\N	\N	\N	0101000000ACAFE3FC63A8424092EFEFBF97E14240	1312
020b	469.48599999999999	Flag	020	020	\N	\N	\N	0101000000B0FDA6A8A1934040C791E8144D864140	1311
020c	313.51299999999998	Flag	020	020	\N	\N	\N	01010000005C2B0C5768AB4040BE79DC7D6F7F4140	1310
020d	310.38900000000001	Waypoint	020	020	\N	\N	\N	0101000000C3C89038EB333840910D3CA8D8944740	1309
ATV4a	310.149	Flag	ATV4	ATV4	\N	\N	\N	01010000001203136E68AA404073FADA30537F4140	1744
ATV4b	327.69299999999998	Flag	ATV4	ATV4	\N	\N	\N	0101000000877FB00A96AD40402D2C8FA03E7F4140	1745
AVATR11NEa	317.11799999999999	Waypoint	AVATR11NE	AVATR11NE	\N	\N	\N	01010000007A809B836FAB4040AA659BB6677F4140	1794
AVATR11NEb	320.00200000000001	Waypoint	AVATR11NE	AVATR11NE	\N	\N	\N	0101000000006BB7586FAB40400F1FE062687F4140	1795
AVATR11NWa	319.041	Waypoint	AVATR11NW	AVATR11NW	\N	\N	\N	01010000005C915B3D6EAB40403F39DF8F687F4140	1796
AVATR11NWb	320.964	Waypoint	AVATR11NW	AVATR11NW	\N	\N	\N	0101000000776CAB296EAB404062843CF7687F4140	1797
AVATR11SEa	318.31999999999999	Waypoint	AVATR11SE	AVATR11SE	\N	\N	\N	0101000000C4D8F3496FAB4040D4E98F1C687F4140	1798
AVATR11SEb	320.964	Waypoint	AVATR11SE	AVATR11SE	\N	\N	\N	01010000004735BCBE6EAB40409526EC75677F4140	1799
AVATR11SWa	319.762	Waypoint	AVATR11SW	AVATR11SW	\N	\N	\N	010100000048AF1FC56DAB40405DE2F83C677F4140	1801
AVATR11SWb	320.964	Waypoint	AVATR11SW	AVATR11SW	\N	\N	\N	0101000000FBC8739E6DAB4040E3AF24D4677F4140	1802
AVP1a	328.654	Flag	AVP1	AVP1	\N	\N	\N	0101000000B845F4C6A4AD404075E35C115C7F4140	1831
AVP1b	335.38299999999998	Flag	AVP1	AVP1	\N	\N	\N	01010000004390CFA02BAD4040652F13F9F97D4140	1832
AVP2a	322.16500000000002	Flag	AVP2	AVP2	\N	\N	\N	01010000006835C47BC5AD4040AF3008A26A7F4140	1834
AVP2b	338.74799999999999	Flag	AVP2	AVP2	\N	\N	\N	0101000000B60D4D203FAD40407CCD84BCE47D4140	1835
AVT1a	301.49700000000001	Flag	AVT1	AVT1	\N	\N	\N	0101000000062D40B5A3B04040ACECB86A69804140	1859
AVT1b	334.18200000000002	Flag	AVT1	AVT1	\N	\N	\N	01010000007C3D04FAE9AC40403294AB09F67E4140	1860
AVT2a	285.63499999999999	Flag	AVT2	AVT2	\N	\N	\N	01010000009E2988420AB140402BD88858AB804140	1861
AVT2b	327.21199999999999	Flag	AVT2	AVT2	\N	\N	\N	01010000007734DB0317AD40401BFCCF2A0B7F4140	1862
AVT3a	276.50299999999999	Flag	AVT3	AVT3	\N	\N	\N	01010000002AF95C96C4B04040E261F88D51804140	1863
AVT3b	327.93299999999999	Flag	AVT3	AVT3	\N	\N	\N	0101000000DD08904332AD404003810850177F4140	1864
AYVAR2a	311.59100000000001	Flag	AYVAR2	AYVAR2	\N	\N	\N	01010000002EB898FF8DAB404088B804A37C7F4140	1872
AYVAR2b	316.63799999999998	Waypoint	AYVAR2	AYVAR2	\N	\N	\N	01010000006AA7CFD68DAB404035B01BD77B7F4140	1873
CTC55a	1545.9100000000001	Waypoint	CTC55	CTC55	\N	\N	\N	010100000021CA84503F7240403884046ED8764140	2087
CTC55b	1549.04	Waypoint	CTC55	CTC55	\N	\N	\N	0101000000C597A7783E724040E6F8B7B7DA764140	2088
CTC68a	1620.1800000000001	Waypoint	CTC68	CTC68	\N	\N	\N	01010000009974BC4B677140407A29A703FA754140	2100
CTC68b	1622.0999999999999	Waypoint	CTC68	CTC68	\N	\N	\N	010100000033FBFA296771404013FFFE34F8754140	2101
CTC81a	1850.4100000000001	Waypoint	CTC81	CTC81	\N	\N	\N	01010000000FA0C85B7A6F40402C58D843E3764140	2108
CTC81b	1858.3399999999999	Waypoint	CTC81	CTC81	\N	\N	\N	0101000000EC4EC8E5776F404018769CCBE2764140	2109
SCV62a	1376	Waypoint	SCV62	SCV62	\N	\N	\N	010100000093C6B4AB1A574040C20A69ED737F4140	2579
SCV62b	1416.6199999999999	Waypoint	SCV62	SCV62	\N	\N	\N	010100000033EFEF6ED0564040A41F4C0B907F4140	2580
001a	1333.95	Waypoint	001	001	\N	\N	\N	0101000000B1225E2F511D53C0F587BC9E553A4540	1230
001b	286.59699999999998	Waypoint	001	001	\N	\N	\N	010100000053F67765001E53C07C3CF7957C3A4540	1231
001c	286.59699999999998	Waypoint	001	001	\N	\N	\N	010100000053F67765001E53C07C3CF7957C3A4540	1232
001d	1333.95	Waypoint	001	001	\N	\N	\N	0101000000B1225E2F511D53C0F587BC9E553A4540	1233
002a	1684.3399999999999	Waypoint	002	002	\N	\N	\N	01010000003777BC157E7240407C9A1C09F7774140	1234
002b	1684.3399999999999	Waypoint	002	002	\N	\N	\N	01010000003777BC157E7240407C9A1C09F7774140	1235
002c	27.763500000000001	Waypoint	002	002	\N	\N	\N	010100000066DEAFA40A262140CBFE9C2984444740	1236
002d	280.82900000000001	Waypoint	002	002	\N	\N	\N	0101000000C9E37BDCFD1D53C0FD275CE87A3A4540	1237
002e	280.82900000000001	Waypoint	002	002	\N	\N	\N	0101000000C9E37BDCFD1D53C0FD275CE87A3A4540	1238
003a	302.45800000000003	Waypoint	003	003	\N	\N	\N	0101000000EB5D003D741D53C06FEDA2BAC93A4540	1239
003b	279.62700000000001	Waypoint	003	003	\N	\N	\N	01010000004278CB0D9A1E53C02FF87BFCFA3A4540	1240
003c	302.45800000000003	Waypoint	003	003	\N	\N	\N	0101000000EB5D003D741D53C06FEDA2BAC93A4540	1241
003d	279.62700000000001	Waypoint	003	003	\N	\N	\N	01010000004278CB0D9A1E53C02FF87BFCFA3A4540	1242
004a	292.84500000000003	Waypoint	004	004	\N	\N	\N	0101000000D12DE87BB51E53C0FFEDD8261A3B4540	1243
004b	292.84500000000003	Waypoint	004	004	\N	\N	\N	0101000000D12DE87BB51E53C0FFEDD8261A3B4540	1244
004c	1173.1700000000001	Waypoint	004	004	\N	\N	\N	010100000047AC37FD682E3540277EFC0D69E24340	1245
004d	299.334	Waypoint	004	004	\N	\N	\N	0101000000207A01DF751D53C041EAF0BCC63A4540	1246
004e	299.334	Waypoint	004	004	\N	\N	\N	0101000000207A01DF751D53C041EAF0BCC63A4540	1247
005a	1172.4400000000001	Waypoint	005	005	\N	\N	\N	010100000018F5417D682E35401239B7E868E24340	1248
005b	298.613	Waypoint	005	005	\N	\N	\N	0101000000ABCB969D5F3438401944CC6EEB944740	1249
005c	298.613	Waypoint	005	005	\N	\N	\N	0101000000ABCB969D5F3438401944CC6EEB944740	1250
006a	257.99799999999999	Waypoint	006	006	\N	\N	\N	01010000003928362691AB39406ED5EF6E17874140	1251
006b	257.99799999999999	Waypoint	006	006	\N	\N	\N	01010000003928362691AB39406ED5EF6E17874140	1252
006c	296.93099999999998	Waypoint	006	006	\N	\N	\N	0101000000A0A500586A343840003E8CD1B9944740	1253
006d	296.93099999999998	Waypoint	006	006	\N	\N	\N	0101000000A0A500586A343840003E8CD1B9944740	1254
007a	1947.02	Waypoint	007	007	\N	\N	\N	010100000048E93CA4C40A4440D5B0B4930E594440	1255
007b	257.99799999999999	Waypoint	007	007	\N	\N	\N	0101000000D571B8336C5338406D4FF302AAB64140	1256
007c	282.75099999999998	Waypoint	007	007	\N	\N	\N	0101000000DEE54D3E70343840FC29C03DB0944740	1257
007d	257.99799999999999	Waypoint	007	007	\N	\N	\N	0101000000D571B8336C5338406D4FF302AAB64140	1258
007e	282.75099999999998	Waypoint	007	007	\N	\N	\N	0101000000DEE54D3E70343840FC29C03DB0944740	1259
008	1947.02	Waypoint	008	008	\N	\N	\N	0101000000C837B8CDC40A44406D8CAFD30E594440	1260
008a	304.14100000000002	Waypoint	008	008	\N	\N	\N	0101000000891E008A7434384029EACC3DA4944740	1261
008b	-6.6033900000000001	Waypoint	008	008	\N	\N	\N	0101000000A50770406C8F374046104C0D6BA24140	1262
008c	304.14100000000002	Waypoint	008	008	\N	\N	\N	0101000000891E008A7434384029EACC3DA4944740	1263
008d	-6.6033900000000001	Waypoint	008	008	\N	\N	\N	0101000000A50770406C8F374046104C0D6BA24140	1264
050	328.41399999999999	Waypoint	050	050	\N	\N	\N	010100000085FBBC3A6BAB404025397CAC827F4140	1388
051	328.654	Waypoint	051	051	\N	\N	\N	0101000000140A37376BAB4040DB3D97AE827F4140	1389
052	311.83100000000002	Waypoint	052	052	\N	\N	\N	01010000007E295F778AAB40402CF7AEB3747F4140	1390
053	311.35000000000002	Waypoint	053	053	\N	\N	\N	01010000002BFEE25E88AB404071F97F3D757F4140	1391
054	311.35000000000002	Waypoint	054	054	\N	\N	\N	0101000000CC09688F88AB4040F7A31888747F4140	1392
068	302.93900000000002	Waypoint	068	068	\N	\N	\N	01010000002A9A5086BFB34040E2008828577D4140	1418
069	473.09100000000001	Waypoint	069	069	\N	\N	\N	01010000009DFB10F3A39340402227DCC34F864140	1419
070	355.08999999999997	Waypoint	070	070	\N	\N	\N	01010000007D0B41C7B49C404007AFBB4B72834140	1420
071	355.08999999999997	Waypoint	071	071	\N	\N	\N	0101000000E29A7F179D9C4040BBB0B4DA63834140	1421
072	355.08999999999997	Waypoint	072	072	\N	\N	\N	0101000000F6CF8CAB759C404079C78C644D834140	1422
073	355.08999999999997	Waypoint	073	073	\N	\N	\N	01010000004ECB17CD6D9C404023EE939E47834140	1423
074	355.08999999999997	Waypoint	074	074	\N	\N	\N	010100000081AA049D669C4040A493DF8145834140	1424
075	355.08999999999997	Waypoint	075	075	\N	\N	\N	01010000001CE1875F3E9C4040133F70DC3C834140	1425
076	355.32999999999998	Waypoint	076	076	\N	\N	\N	0101000000693FF4D2049C40404C14381131834140	1426
077	355.32999999999998	Waypoint	077	077	\N	\N	\N	01010000007EF4E3B5E69B4040340479D62B834140	1427
078	370.23099999999999	Waypoint	078	078	\N	\N	\N	01010000001570EB48979B4040DF5F60FDEF824140	1428
079	378.88200000000001	Waypoint	079	079	\N	\N	\N	0101000000ABFF646D829B4040BADBB86FBC824140	1429
080	381.286	Waypoint	080	080	\N	\N	\N	01010000004077B437599B404033F31C45A1824140	1430
081	381.286	Waypoint	081	081	\N	\N	\N	0101000000A2E6049D2B9B404016899CCD89824140	1431
082	383.68900000000002	Waypoint	082	082	\N	\N	\N	01010000007705C134239B404020F2CB9D2F824140	1432
083	387.05399999999997	Waypoint	083	083	\N	\N	\N	010100000098D048771C9B404014A253C219824140	1433
084	387.29399999999998	Waypoint	084	084	\N	\N	\N	0101000000310314711A9B4040F38490B913824140	1434
085	387.53399999999999	Waypoint	085	085	\N	\N	\N	01010000007D17AFAC069B40404F4B88C60E824140	1435
086	390.41800000000001	Waypoint	086	086	\N	\N	\N	01010000002D02DD43349B404070A1074BB0814140	1436
087	406.75999999999999	Waypoint	087	087	\N	\N	\N	0101000000A20A7514449B40402DB683F0AC814140	1437
088	407.00099999999998	Waypoint	088	088	\N	\N	\N	01010000005E677337309B40402D62EC45B3814140	1438
089	408.44299999999998	Waypoint	089	089	\N	\N	\N	010100000011E3F392189B4040C83720B1B9814140	1439
090	408.44299999999998	Waypoint	090	090	\N	\N	\N	01010000008DB75342099B4040D68F73B9C6814140	1440
091	408.44299999999998	Waypoint	091	091	\N	\N	\N	0101000000CBDD0096099B4040D2535B1FD3814140	1441
092	409.64400000000001	Waypoint	092	092	\N	\N	\N	0101000000911FB7800C9B4040260E2C89E4814140	1442
093	409.88499999999999	Waypoint	093	093	\N	\N	\N	0101000000F38A2B570D9B4040B8B3C8D9E8814140	1443
094	409.88499999999999	Waypoint	094	094	\N	\N	\N	0101000000F06584B20E9B404061071CA901824140	1444
095	6.37439	Waypoint	095	095	\N	\N	\N	0101000000B5AA80D657AA4040B478EBDB8D5C4140	1445
096	417.57499999999999	Waypoint	096	096	\N	\N	\N	01010000004B2FE8BF329C4040CCC3285925844140	1446
097	396.18599999999998	Waypoint	097	097	\N	\N	\N	01010000002E36A85B08A74040790FF38C0D7E4140	1447
098	395.22500000000002	Waypoint	098	098	\N	\N	\N	01010000008B2DA4C6FBA64040775F77B5BA7E4140	1448
099	309.66800000000001	Waypoint	099	099	\N	\N	\N	010100000027501FDC38AB4040DEED93066B7F4140	1449
100	322.64600000000002	Waypoint	100	100	\N	\N	\N	0101000000FE2A589466AB4040A2B2C477607F4140	1450
101	299.09399999999999	Waypoint	101	101	\N	\N	\N	0101000000E9B5A46639AB404043213CB96A7F4140	1451
102	302.21800000000002	Waypoint	102	102	\N	\N	\N	0101000000ECBD5BCD38AB4040290669426A7F4140	1452
103	406.27999999999997	Waypoint	103	103	\N	\N	\N	0101000000AB6A639B3D944040B4646FD81F864140	1453
104	404.35700000000003	Waypoint	104	104	\N	\N	\N	0101000000EF261BB63D9440401C266BEB1F864140	1454
105	404.83800000000002	Waypoint	105	105	\N	\N	\N	010100000063A6F4193E944040E1F6B0891F864140	1455
106	404.83800000000002	Waypoint	106	106	\N	\N	\N	0101000000ACA8E7FB6EAB40401B349B97687F4140	1456
107	-7.8049299999999997	Waypoint	107	107	\N	\N	\N	01010000007C46F7B3258F3740BA8190997CA24140	1457
108	323.36700000000002	Waypoint	108	108	\N	\N	\N	01010000008E7C47551F51404085C41F4064844140	1458
109	323.36700000000002	Waypoint	109	109	\N	\N	\N	01010000008E7C47551F51404085C41F4064844140	1459
110	323.36700000000002	Waypoint	110	110	\N	\N	\N	01010000008E7C47551F51404085C41F4064844140	1460
111	1119.0899999999999	Waypoint	111	111	\N	\N	\N	0101000000E1A5FCEDF050404048584C5361844140	1461
112	28.003900000000002	Waypoint	112	112	\N	\N	\N	0101000000B1981898432940400EF08BE86A784140	1462
113	28.965299999999999	Waypoint	113	113	\N	\N	\N	010100000022C48B41442940400E16584470784140	1463
114	27.763500000000001	Waypoint	114	114	\N	\N	\N	0101000000831210DA452940407F1E38A16F784140	1464
115	27.763500000000001	Waypoint	115	115	\N	\N	\N	010100000072A470934A29404015ECD86B6E784140	1465
116	26.081199999999999	Waypoint	116	116	\N	\N	\N	01010000005CC8D7294C294040B0CC44D967784140	1466
117	27.523199999999999	Waypoint	117	117	\N	\N	\N	010100000024FE47F74A29404088BC106B64784140	1467
118	27.523199999999999	Waypoint	118	118	\N	\N	\N	01010000004D1F33B04B294040C488B4F562784140	1468
119	46.268900000000002	Waypoint	119	119	\N	\N	\N	01010000008FE004AE442640400FE90BAF91834140	1469
120	46.028599999999997	Waypoint	120	120	\N	\N	\N	01010000007A61DF0C462640401A44E05292834140	1470
121	47.2301	Waypoint	121	121	\N	\N	\N	01010000001C2A84DD49264040D50A8C228F834140	1471
122	1854.98	Waypoint	122	122	\N	\N	\N	01010000008ED520AB1F6E4040D226FBA54D774140	1472
15311SW	92.892499999999998	Waypoint	15311SW	15311SW	\N	\N	\N	010100000073C10CD2FBAB40406E57DB59A3604140	1473
15316SW	98.900599999999997	Waypoint	15316SW	15316SW	\N	\N	\N	01010000001BA6586300AC40401BE1A3B4AD604140	1474
15320NE	89.768100000000004	Waypoint	15320NE	15320NE	\N	\N	\N	01010000001A15CB3C58AC404004BDAC93B7604140	1475
15325NW	86.403599999999997	Waypoint	15325NW	15325NW	\N	\N	\N	01010000004091083C59AC4040C56ED7FFC4604140	1476
15325NWBIS	88.085800000000006	Waypoint	15325NWBIS	15325NWBIS	\N	\N	\N	010100000009C7780958AC40406808B090C5604140	1477
247 START	503.85300000000001	Waypoint	247 START	247 START	\N	\N	\N	0101000000CF529B004F9A40405671576DC6804140	1478
63403WALL	117.166	Waypoint	63403WALL	63403WALL	\N	\N	\N	0101000000F570B38CD5AB404087C06B7920614140	1479
63405T4	122.693	Waypoint	63405T4	63405T4	\N	\N	\N	0101000000A69DC092F0AB40404E68642027614140	1480
65203SDS	116.20399999999999	Waypoint	65203SDS	65203SDS	\N	\N	\N	01010000006DD5B00AD4AB40407D59170DD2604140	1481
65212WALL	100.583	Waypoint	65212WALL	65212WALL	\N	\N	\N	0101000000B63BABD2C4AB40407607B334F0604140	1482
65420TC1	108.273	Waypoint	65420TC1	65420TC1	\N	\N	\N	0101000000430A247CFDAB40402283C3A8B7604140	1483
65420TC2	107.55200000000001	Waypoint	65420TC2	65420TC2	\N	\N	\N	0101000000F34F0B86FCAB404004BDAC93B7604140	1484
65420TC3	107.312	Waypoint	65420TC3	65420TC3	\N	\N	\N	010100000099DA4024FDAB4040E4053971B7604140	1485
654TC4	107.55200000000001	Waypoint	654TC4	654TC4	\N	\N	\N	01010000005749D3BCFDAB4040F24BD43DB8604140	1486
AAL1	361.339	Flag	AAL1	AAL1	\N	\N	\N	010100000012A5049763A54040E058870B8F804140	1487
AAL2	359.89600000000002	Flag	AAL2	AAL2	\N	\N	\N	0101000000A10DAC225DA540409A4C389B5E804140	1488
AAL3	367.58699999999999	Flag	AAL3	AAL3	\N	\N	\N	0101000000450F307E50A54040C786A8A151804140	1489
AAL4	363.26100000000002	Flag	AAL4	AAL4	\N	\N	\N	0101000000A8BA03195AA540405B36A0C931804140	1490
AAL5	375.99799999999999	Flag	AAL5	AAL5	\N	\N	\N	010100000082C8784D75A54040B37BFC2204804140	1491
AAL6	378.642	Flag	AAL6	AAL6	\N	\N	\N	0101000000E477931294A54040A190C0C605804140	1492
AAL7	374.07600000000002	Flag	AAL7	AAL7	\N	\N	\N	01010000008D1B5B2889A540409CA4870E32804140	1493
AAL8	371.43200000000002	Flag	AAL8	AAL8	\N	\N	\N	0101000000BF0348DC7FA54040E409031D4D804140	1494
AAM1	380.565	Flag	AAM1	AAM1	\N	\N	\N	010100000096E77096DAA44040CB4068B48A834140	1495
AAM2	349.322	Flag	AAM2	AAM2	\N	\N	\N	0101000000F6D70FD708A54040A8210F1C4E834140	1496
AAY1	326.25099999999998	Flag	AAY1	AAY1	\N	\N	\N	0101000000A61AF3C353A74040CAF7060B59824140	1497
AAY2	329.375	Flag	AAY2	AAY2	\N	\N	\N	010100000098CEB4E850A740405A928F1F62824140	1498
AAY3	326.97199999999998	Flag	AAY3	AAY3	\N	\N	\N	0101000000ABF097B149A740402B34D3674E824140	1499
AAY4	331.53800000000001	Flag	AAY4	AAY4	\N	\N	\N	01010000007A012BC439A740404250CB9555824140	1500
AAY5	330.096	Flag	AAY5	AAY5	\N	\N	\N	01010000000896585630A7404060EDCF154A824140	1501
ADS1	87.605099999999993	Waypoint	ADS1	ADS1	\N	\N	\N	0101000000CAF73BFDA884414076F13F78FB4A4040	1502
ADS2	99.621600000000001	Waypoint	ADS2	ADS2	\N	\N	\N	0101000000EF5C2CCDAC844140C385D49EE64A4040	1503
AG06 1	405.31799999999998	Waypoint	AG06 1	AG06 1	\N	\N	\N	010100000045F4FE4E399440405C37032E18864140	1504
AG06 2	406.03899999999999	Waypoint	AG06 2	AG06 2	\N	\N	\N	0101000000EB04D1E63A944040F800F17B1A864140	1505
AG06 3	406.51999999999998	Waypoint	AG06 3	AG06 3	\N	\N	\N	0101000000FA2143823A944040C7F2376C1E864140	1506
AG06 4	391.13900000000001	Waypoint	AG06 4	AG06 4	\N	\N	\N	0101000000EB24075E40944040D17CD84F22864140	1507
AG06 5	406.03899999999999	Waypoint	AG06 5	AG06 5	\N	\N	\N	010100000003D50C973F9440400B2CB06219864140	1508
AG06 6	404.59800000000001	Waypoint	AG06 6	AG06 6	\N	\N	\N	01010000003E0724CE3B9440401915D8661A864140	1509
AG06 7	402.19400000000002	Waypoint	AG06 7	AG06 7	\N	\N	\N	0101000000A8FC5C4E38944040C8C36B2C1B864140	1510
AG06 8	400.27199999999999	Waypoint	AG06 8	AG06 8	\N	\N	\N	01010000006511D9F3349440403F111C7B1C864140	1511
AGA02-BIG	1973.46	Waypoint	AGA02-BIG	AGA02-BIG	\N	\N	\N	01010000008E313310240B444009F06E4934584440	1512
AGA03-1	1946.54	Waypoint	AGA03-1	AGA03-1	\N	\N	\N	01010000001BBA04A0C40A4440847C38970E594440	1513
AGA03-4	1990.28	Waypoint	AGA03-4	AGA03-4	\N	\N	\N	01010000000BD4FFC5750A4440330995BA8F584440	1514
AGACBASI 1	1972.26	Waypoint	AGACBASI 1	AGACBASI 1	\N	\N	\N	01010000009D3127C38C0A44404C03C02AB7584440	1515
AGH6	392.58100000000002	Flag	AGH6	AGH6	\N	\N	\N	0101000000B5AEC4481E974040B9A7B0F40F854140	1516
AGM1	372.63400000000001	Flag	AGM1	AGM1	\N	\N	\N	01010000008E429CCBF0964040A9BDCF8BEF844140	1517
AGM2	395.94600000000003	Flag	AGM2	AGM2	\N	\N	\N	010100000084CDEC32FD9640401DD1C324EA844140	1518
AGM3	395.22500000000002	Flag	AGM3	AGM3	\N	\N	\N	0101000000BF173BEE0F974040A678DBD3F5844140	1519
AGM4	394.74400000000003	Flag	AGM4	AGM4	\N	\N	\N	0101000000D1339F6EFE964040B01434F305854140	1520
AGM5	391.13900000000001	Flag	AGM5	AGM5	\N	\N	\N	010100000033686CD804974040549AD49D15854140	1521
AGM6	400.03100000000001	Flag	AGM6	AGM6	\N	\N	\N	0101000000B245A7EE57974040A9C6ABA9F5844140	1522
AGM7	403.39600000000002	Flag	AGM7	AGM7	\N	\N	\N	0101000000E4D020DA4E974040D15ED8F5E6844140	1523
AGM8	399.06999999999999	Flag	AGM8	AGM8	\N	\N	\N	01010000007F8BC0EB4297404043DB9C10DB844140	1524
AGR1	393.78300000000002	Flag	AGR1	AGR1	\N	\N	\N	01010000001511A78E3394404043ED637714864140	1525
AGR10	404.35700000000003	Flag	AGR10	AGR10	\N	\N	\N	01010000003B06C9DA5D944040197B274D20864140	1526
AGR2	392.34100000000001	Flag	AGR2	AGR2	\N	\N	\N	0101000000407887F03C9440409C0F745931864140	1527
AGR9	406.51999999999998	Flag	AGR9	AGR9	\N	\N	\N	0101000000FD9EBBD145944040DC6548E012864140	1528
AGRO3CORE1	466.84199999999998	Waypoint	AGRO3CORE1	AGRO3CORE1	\N	\N	\N	010100000026AB27959E934040FD1E5FBC4B864140	1529
AGRO3CORE2	478.61900000000003	Waypoint	AGRO3CORE2	AGRO3CORE2	\N	\N	\N	0101000000856B58CD9F934040B8E8439B4C864140	1530
AGROCAVE	472.61000000000001	Waypoint	AGROCAVE	AGROCAVE	\N	\N	\N	0101000000C22BC3649C9340406971E73E50864140	1531
AGROCAVE2	449.05799999999999	Waypoint	AGROCAVE2	AGROCAVE2	\N	\N	\N	0101000000CF136CAFC793404019A2D0D33C864140	1532
AGROCORE1	398.82999999999998	Waypoint	AGROCORE1	AGROCORE1	\N	\N	\N	010100000000AD081D45944040001498581D864140	1533
AGROCORE2	401.233	Waypoint	AGROCORE2	AGROCORE2	\N	\N	\N	0101000000D92713C635944040BBFA6C751D864140	1534
AGROCORE3	405.31799999999998	Waypoint	AGROCORE3	AGROCORE3	\N	\N	\N	0101000000066360F73F94404042DCAC2C17864140	1535
AGROCORE4	404.59800000000001	Waypoint	AGROCORE4	AGROCORE4	\N	\N	\N	0101000000E665D3653F944040981219BB1C864140	1536
AGROK1	401.95400000000001	Waypoint	AGROK1	AGROK1	\N	\N	\N	0101000000F191FF8F349440402D034DD21C864140	1537
AGRO KF1	398.589	Waypoint	AGRO KF1	AGRO KF1	\N	\N	\N	0101000000A040935535944040A980B80118864140	1538
AGRO KF2	402.19400000000002	Waypoint	AGRO KF2	AGRO KF2	\N	\N	\N	0101000000992034CC5A9440404ACCC0BC18864140	1539
AGRO KF3	401.47300000000001	Waypoint	AGRO KF3	AGRO KF3	\N	\N	\N	01010000006515FC5752944040656D0C6722864140	1540
AGRO KF4	398.82999999999998	Waypoint	AGRO KF4	AGRO KF4	\N	\N	\N	0101000000624648423A9440403CA0B8581B864140	1541
AGROQUERN	476.45600000000002	Waypoint	AGROQUERN	AGROQUERN	\N	\N	\N	0101000000DD8FE859A1934040B8A8C0104C864140	1542
AGRORCTOMB	403.63600000000002	Waypoint	AGRORCTOMB	AGRORCTOMB	\N	\N	\N	0101000000947313C9D89340406866686711864140	1543
AGROSITE2	460.59399999999999	Waypoint	AGROSITE2	AGROSITE2	\N	\N	\N	010100000002B97803C293404048AB18B63F864140	1544
AGROSITE3	473.09100000000001	Waypoint	AGROSITE3	AGROSITE3	\N	\N	\N	0101000000164E3F99A09340405D2D849044864140	1545
AGROSITE4	465.88099999999997	Waypoint	AGROSITE4	AGROSITE4	\N	\N	\N	0101000000D5EEB2BAB0934040059AC8FF36864140	1546
AGROTREE1	403.39600000000002	Waypoint	AGROTREE1	AGROTREE1	\N	\N	\N	0101000000F8DF87D94494404070C5A7FB1E864140	1547
AIROLO	1094.5799999999999	Waypoint	AIROLO	AIROLO	\N	\N	\N	010100000058A22321E036214099665C6688434740	1548
ALACA HOYU	1066.46	Waypoint	ALACA HOYU	ALACA HOYU	\N	\N	\N	010100000012DAE4832D594140705A9F5BF01D4440	1549
ALACAHOYUK	1078.96	Waypoint	ALACAHOYUK	ALACAHOYUK	\N	\N	\N	0101000000FCFFCBC413594140C7E15C5BE91D4440	1550
ALALAKH1	97.698999999999998	Waypoint	ALALAKH1	ALALAKH1	\N	\N	\N	0101000000117A33F3EF3042405F113162B51E4240	1551
ALAM K1	282.02999999999997	Waypoint	ALAM K1	ALAM K1	\N	\N	\N	0101000000EAF8C072EAB34040D688C346577D4140	1552
ALAM K CAV	296.69	Waypoint	ALAM K CAV	ALAM K CAV	\N	\N	\N	01010000009D55506559B340404905649B537D4140	1553
ALAM KL1	282.99200000000002	Waypoint	ALAM KL1	ALAM KL1	\N	\N	\N	0101000000D3393C0DE3B340405E27239E547D4140	1554
ALAM K L2	282.99200000000002	Waypoint	ALAM K L2	ALAM K L2	\N	\N	\N	010100000049FBD019D9B34040E90DB0BC4F7D4140	1555
ALAM K TR1	295.00799999999998	Waypoint	ALAM K TR1	ALAM K TR1	\N	\N	\N	01010000006E22A76DCBB340405466DB4D567D4140	1556
ALAM K TR2	284.91399999999999	Waypoint	ALAM K TR2	ALAM K TR2	\N	\N	\N	01010000005F7D3CF4DDB34040E4B4CBAA547D4140	1557
ALAMR	332.98000000000002	Flag	ALAMR	ALAMR	\N	\N	\N	0101000000F381BCD5F5B240409B0E37BC6D7D4140	1558
ALAMS1	298.13200000000001	Flag	ALAMS1	ALAMS1	\N	\N	\N	01010000008890042B58B3404084B774BD4E7D4140	1559
ALAMS2	291.40300000000002	Flag	ALAMS2	ALAMS2	\N	\N	\N	010100000065B18C8E5DB34040FD969BD06D7D4140	1560
ALAMS4	294.04700000000003	Flag	ALAMS4	ALAMS4	\N	\N	\N	0101000000105D2FD149B34040BBEAB0AB517D4140	1561
ALAMS5	292.36500000000001	Flag	ALAMS5	ALAMS5	\N	\N	\N	0101000000438CD7BC6AB34040D879441A4F7D4140	1562
ALAMS6	289.721	Flag	ALAMS6	ALAMS6	\N	\N	\N	01010000006EEADB006EB340400C23500C6F7D4140	1563
ALAMS CVE1	297.892	Waypoint	ALAMS CVE1	ALAMS CVE1	\N	\N	\N	010100000021291F0B5CB3404048119D8E557D4140	1564
ALAMSCV GP	298.613	Waypoint	ALAMSCV GP	ALAMSCV GP	\N	\N	\N	010100000059963B755DB34040728FFB0F567D4140	1565
ALES1	269.053	Flag	ALES1	ALES1	\N	\N	\N	01010000009EDF67C41BB6404093251F2D21804140	1566
ALES2	266.649	Flag	ALES2	ALES2	\N	\N	\N	0101000000EACE13CF19B64040B0E693242E804140	1567
ALES3	268.572	Flag	ALES3	ALES3	\N	\N	\N	0101000000BDD75F622BB64040B117988226804140	1568
ALES4	267.61099999999999	Flag	ALES4	ALES4	\N	\N	\N	0101000000F714D36112B640402F09C4A91E804140	1569
ALES5	241.17500000000001	Flag	ALES5	ALES5	\N	\N	\N	010100000012588743B9B54040E0D423E726804140	1570
ALESX1	260.40100000000001	Flag	ALESX1	ALESX1	\N	\N	\N	010100000063AFD03704B640400A87E39B1B804140	1571
ALKO1	247.18299999999999	Flag	ALKO1	ALKO1	\N	\N	\N	0101000000B66044037AB540404C403F98C37F4140	1572
ALKO2	262.32299999999998	Flag	ALKO2	ALKO2	\N	\N	\N	0101000000200BAB5A8DB5404023B284BFA37F4140	1573
ALKO3	263.52499999999998	Flag	ALKO3	ALKO3	\N	\N	\N	01010000008493EBA895B54040FE1B9091A77F4140	1574
ALKO4	261.60300000000001	Flag	ALKO4	ALKO4	\N	\N	\N	01010000001C0F2DE78FB540405E5FDC27B67F4140	1575
ALKO5	256.79599999999999	Flag	ALKO5	ALKO5	\N	\N	\N	0101000000FC0BFD468DB540402C63DB53C67F4140	1576
ALKS1	289.96100000000001	Flag	ALKS1	ALKS1	\N	\N	\N	0101000000952000916EB34040EADD64C3767D4140	1577
ALKS2	288.03899999999999	Flag	ALKS2	ALKS2	\N	\N	\N	0101000000977D4F9376B3404034F300F0717D4140	1578
ALKS3	282.27100000000002	Flag	ALKS3	ALKS3	\N	\N	\N	0101000000A9A13387E9B340400F7043B7577D4140	1579
ALKS4	303.17899999999997	Flag	ALKS4	ALKS4	\N	\N	\N	010100000067DB10B33CB34040D212CB30657D4140	1580
ALKS5	299.815	Flag	ALKS5	ALKS5	\N	\N	\N	0101000000FFEC0FE069B340408BE68C1E7B7D4140	1581
ALKS6	282.27100000000002	Flag	ALKS6	ALKS6	\N	\N	\N	0101000000123E544D22B3404067D23495B67D4140	1582
ALKS7	276.262	Flag	ALKS7	ALKS7	\N	\N	\N	0101000000C505B43561B34040936E3CB9D57D4140	1583
ALKSX1	288.03899999999999	Flag	ALKSX1	ALKSX1	\N	\N	\N	0101000000F36ECCB55FB340407C32B0A2877D4140	1584
ALKSX2	287.077	Flag	ALKSX2	ALKSX2	\N	\N	\N	0101000000A9AEA45E75B34040751BCFC5717D4140	1585
ALKSX3	278.42500000000001	Flag	ALKSX3	ALKSX3	\N	\N	\N	010100000066EC900ABFB3404098306F15917D4140	1586
ALL1	314.71499999999997	Flag	ALL1	ALL1	\N	\N	\N	0101000000E5CCCF3DC6B14040D0D07C24DE7E4140	1587
ALL2	294.28699999999998	Flag	ALL2	ALL2	\N	\N	\N	01010000005059A3D70CB24040EEC1184FCC7E4140	1588
ALL3	293.08499999999998	Flag	ALL3	ALL3	\N	\N	\N	010100000029AED4FAF5B14040A4F737AFC47E4140	1589
ALL4	292.84500000000003	Flag	ALL4	ALL4	\N	\N	\N	0101000000102AF756B2B14040A97C8B2BC77E4140	1590
ALL5	295.48899999999998	Flag	ALL5	ALL5	\N	\N	\N	01010000007A9993DDADB14040B31E6393E17E4140	1591
ALLIANOI	108.754	Waypoint	ALLIANOI	ALLIANOI	\N	\N	\N	0101000000016FB6F6144E3B402659E715D89D4340	1592
ALM1	313.75400000000002	Flag	ALM1	ALM1	\N	\N	\N	0101000000FBDC1FDB31B340409592E323697D4140	1593
ALM2	328.173	Flag	ALM2	ALM2	\N	\N	\N	01010000000EC35BA8FBB24040104254A26B7D4140	1594
ALM3	316.15699999999998	Flag	ALM3	ALM3	\N	\N	\N	01010000005FA9786DC0B240403DD3FFFD9F7D4140	1595
ALM4	332.01900000000001	Flag	ALM4	ALM4	\N	\N	\N	01010000002BB5537D6BB240400E2F8848CD7D4140	1596
ALM5	306.06299999999999	Flag	ALM5	ALM5	\N	\N	\N	01010000006E6477B18BB24040A808DF17F17D4140	1597
ALM6	257.27699999999999	Flag	ALM6	ALM6	\N	\N	\N	010100000061B48CA9BBB24040AA1774294B7E4140	1598
ALM7	262.32299999999998	Flag	ALM7	ALM7	\N	\N	\N	0101000000C099F3E7F6B24040DC5977D0457E4140	1599
ALM8	268.09100000000001	Flag	ALM8	ALM8	\N	\N	\N	0101000000F1FAC4E816B34040B04B0B99167E4140	1600
ALM9	280.58800000000002	Flag	ALM9	ALM9	\N	\N	\N	01010000000F4DF73F22B34040E7121BE7C77D4140	1601
ALN1	325.52999999999997	Flag	ALN1	ALN1	\N	\N	\N	010100000003D100CFD7B14040E8F74EE3967E4140	1602
ALN2	334.66199999999998	Flag	ALN2	ALN2	\N	\N	\N	0101000000CBB47CAB15B240409FE5A4F0537E4140	1603
ALN3	345.23599999999999	Flag	ALN3	ALN3	\N	\N	\N	0101000000978DD3B2E3B14040FF9DBF99707E4140	1604
ALN4	314.47399999999999	Flag	ALN4	ALN4	\N	\N	\N	01010000009A651C11B6B140406F4BF3ADA07E4140	1605
ALN5	298.13200000000001	Flag	ALN5	ALN5	\N	\N	\N	0101000000BFAB9CF8D5B1404039163715B97E4140	1606
ALN6	290.44200000000001	Flag	ALN6	ALN6	\N	\N	\N	0101000000C50F00E20FB24040F7F0AE78BF7E4140	1607
ALN7	282.51100000000002	Flag	ALN7	ALN7	\N	\N	\N	0101000000F4F1D8E83FB240405A9603CBBE7E4140	1608
ALS1	287.077	Flag	ALS1	ALS1	\N	\N	\N	01010000008E4AA07799B640401C5FE80238804140	1609
ALS10	252.95099999999999	Flag	ALS10	ALS10	\N	\N	\N	01010000006F01576896B640402E2A7C92C8804140	1610
ALS11	235.887	Flag	ALS11	ALS11	\N	\N	\N	0101000000041D576AD0B64040BA926BAAB1804140	1611
ALS2	233.72399999999999	Flag	ALS2	ALS2	\N	\N	\N	01010000000D4EA7E9F1B64040848757FCA0804140	1612
ALS3	243.33799999999999	Flag	ALS3	ALS3	\N	\N	\N	0101000000CD3AB3C20CB74040F78A130474804140	1613
ALS4	246.46199999999999	Flag	ALS4	ALS4	\N	\N	\N	0101000000CFBA88E713B740409141B47742804140	1614
ALS5	238.77099999999999	Flag	ALS5	ALS5	\N	\N	\N	01010000009D364860EAB64040D91A5B199E804140	1615
ALS6	236.60900000000001	Flag	ALS6	ALS6	\N	\N	\N	01010000003BE16754C7B64040977293589F804140	1616
ALS7	239.733	Flag	ALS7	ALS7	\N	\N	\N	0101000000CDB22C3A87B640407C31F48599804140	1617
ALS8	243.81800000000001	Flag	ALS8	ALS8	\N	\N	\N	0101000000ED10092359B64040A65D17418D804140	1618
ALS9	241.41499999999999	Flag	ALS9	ALS9	\N	\N	\N	0101000000154B2C517FB640408846AFBA99804140	1619
ANA1	426.46699999999998	Flag	ANA1	ANA1	\N	\N	\N	010100000074D42C2BA8A4404036F5EC7F217F4140	1620
ANA2	437.76299999999998	Flag	ANA2	ANA2	\N	\N	\N	01010000001512A00EBAA44040AD76E765217F4140	1621
ANA3	441.608	Flag	ANA3	ANA3	\N	\N	\N	0101000000C25A08B4ACA440408F23D8E3FE7E4140	1622
ANA4	429.59199999999998	Flag	ANA4	ANA4	\N	\N	\N	01010000004512E80CBFA440402DFAA0ED2C7F4140	1623
ANA5	429.351	Flag	ANA5	ANA5	\N	\N	\N	010100000035D2EF4EC0A4404016706710337F4140	1624
ANA6	413.49000000000001	Flag	ANA6	ANA6	\N	\N	\N	01010000004F1EEBE9B0A44040CF22FFBF367F4140	1625
ANG-1	1596.3800000000001	Waypoint	ANG-1	ANG-1	\N	\N	\N	0101000000E08D1E2262163840F84CEC5D20A24140	1626
ANG-10	1597.3399999999999	Waypoint	ANG-10	ANG-10	\N	\N	\N	0101000000CF2FB76C8E1638407B3F4FB2FEA14140	1627
ANG-11	1589.6500000000001	Waypoint	ANG-11	ANG-11	\N	\N	\N	0101000000460126996A16384053CAA8880CA24140	1628
ANG-12	1588.45	Waypoint	ANG-12	ANG-12	\N	\N	\N	0101000000A6DE460F6B163840E30FBDC704A24140	1629
ANG-13	1594.46	Waypoint	ANG-13	ANG-13	\N	\N	\N	01010000003ADAC9C4531638409D4E70B913A24140	1630
ANG-2	1606	Waypoint	ANG-2	ANG-2	\N	\N	\N	01010000007578AF2A601638402323F8C335A24140	1631
ANG-3	1612.48	Waypoint	ANG-3	ANG-3	\N	\N	\N	01010000002FA34EFC75163840C257B34F39A24140	1632
ANG-4	1590.1300000000001	Waypoint	ANG-4	ANG-4	\N	\N	\N	010100000064536F8C6B1638405EDEE40337A24140	1633
ANG5	1606.48	Waypoint	ANG5	ANG5	\N	\N	\N	0101000000F71A9E386216384007C22FA42CA24140	1634
ANG-6	1592.78	Waypoint	ANG-6	ANG-6	\N	\N	\N	0101000000CDC0AF2E7A1638401D82578901A24140	1635
ANG-7	1590.8599999999999	Waypoint	ANG-7	ANG-7	\N	\N	\N	01010000002BC706007E1638402DA8981805A24140	1636
ANG8	1591.5799999999999	Waypoint	ANG8	ANG8	\N	\N	\N	0101000000E77091A1811638404244BB2105A24140	1637
ANG-9	1590.1300000000001	Waypoint	ANG-9	ANG-9	\N	\N	\N	010100000087B1615A8F1638406C5F03CC03A24140	1638
ANK1	383.92899999999997	Flag	ANK1	ANK1	\N	\N	\N	01010000007800D58955A540402B9CBB95597F4140	1639
ANK2	377.68099999999998	Flag	ANK2	ANK2	\N	\N	\N	010100000065E137FA62A54040ABC624B9827F4140	1640
ANK3	385.85199999999998	Flag	ANK3	ANK3	\N	\N	\N	0101000000491A20F453A540409B39B718627F4140	1641
ANK4	406.27999999999997	Flag	ANK4	ANK4	\N	\N	\N	010100000081CC78A23EA54040A5C7338B377F4140	1642
ANK5	375.51799999999997	Flag	ANK5	ANK5	\N	\N	\N	010100000017400FED97A540403047DB6D617F4140	1643
ANK6	378.642	Flag	ANK6	ANK6	\N	\N	\N	0101000000BE5E5FC092A540401C7A9040717F4140	1644
ANKV1	388.73599999999999	Flag	ANKV1	ANKV1	\N	\N	\N	01010000005B9C1FE98DA34040CEE1E89739804140	1645
ANKV2	382.00700000000001	Flag	ANKV2	ANKV2	\N	\N	\N	01010000001432C75A92A3404099DF8C4137804140	1646
ANKV3	384.64999999999998	Flag	ANKV3	ANKV3	\N	\N	\N	0101000000EDE10F628EA340405257674A25804140	1647
ANKV4	385.85199999999998	Flag	ANKV4	ANKV4	\N	\N	\N	01010000008DC7C83689A340401229DFC92D804140	1648
ANKV5	386.33300000000003	Flag	ANKV5	ANKV5	\N	\N	\N	010100000081FF483961A34040A44803E728804140	1649
ANKV6	386.57299999999998	Flag	ANKV6	ANKV6	\N	\N	\N	0101000000D69B287464A34040639A981728804140	1650
ANKV7	384.41000000000003	Flag	ANKV7	ANKV7	\N	\N	\N	0101000000FC4FD36E81A34040B80D4F212A804140	1651
ANP1	353.16699999999997	Flag	ANP1	ANP1	\N	\N	\N	01010000008E2DE07270A4404014C98FACC7804140	1652
ANP2	368.30799999999999	Flag	ANP2	ANP2	\N	\N	\N	01010000007A63DB7E86A440402E0969F0AB804140	1653
ANP3	370.471	Flag	ANP3	ANP3	\N	\N	\N	0101000000B2F5E61976A4404073E88F91B5804140	1654
ANT 40-52	1900.8800000000001	Waypoint	ANT 40-52	ANT 40-52	\N	\N	\N	0101000000D31F92082AC53D4002F7AED482404240	1655
ANT 53-61	1557.9300000000001	Waypoint	ANT 53-61	ANT 53-61	\N	\N	\N	01010000005E9D7F9525C53D401B6CD3A074404240	1656
ANT 62	1562.74	Waypoint	ANT 62	ANT 62	\N	\N	\N	0101000000E7E821B531BD3D40DD5A7F7BF9434240	1657
ANV1	364.46300000000002	Flag	ANV1	ANV1	\N	\N	\N	01010000003F4D7CA1FFA44040DA2CEF8EB2804140	1658
ANV2	363.74200000000002	Flag	ANV2	ANV2	\N	\N	\N	010100000040B10408D5A44040F7E48768B9804140	1659
ANV4	361.09800000000001	Flag	ANV4	ANV4	\N	\N	\N	0101000000AA2C7C1091A440400CA6B03E9D804140	1660
APG1	16.4681	Waypoint	APG1	APG1	\N	\N	\N	0101000000626457C7222640407CD1C37531834140	1661
APG10	29.2056	Waypoint	APG10	APG10	\N	\N	\N	0101000000B6288308102640407B57606F32834140	1662
APG11	30.887899999999998	Waypoint	APG11	APG11	\N	\N	\N	0101000000FFEF2A99132640405B77C31B31834140	1663
APG12	44.826900000000002	Waypoint	APG12	APG12	\N	\N	\N	010100000032318BC0402640404DC7FCD958834140	1664
APG13	45.067300000000003	Waypoint	APG13	APG13	\N	\N	\N	01010000005083D4B3412640403E04A1315D834140	1665
APG14	43.144500000000001	Waypoint	APG14	APG14	\N	\N	\N	0101000000508077DE452640403C7213D149834140	1666
APG15	44.346299999999999	Waypoint	APG15	APG15	\N	\N	\N	010100000079643CE2412640403147C4D16B834140	1667
APG16	48.431800000000003	Waypoint	APG16	APG16	\N	\N	\N	0101000000A5CD78283826404083CBD7239C834140	1668
APG18	60.207900000000002	Waypoint	APG18	APG18	\N	\N	\N	0101000000CBD43B1479264040464BF016A0834140	1669
APG19	37.3767	Waypoint	APG19	APG19	\N	\N	\N	0101000000A56B4CA64F2640406F7493ED43834140	1670
APG2	33.531500000000001	Waypoint	APG2	APG2	\N	\N	\N	0101000000186F08AE222640401A8C1BFB35834140	1671
APG20	54.680300000000003	Waypoint	APG20	APG20	\N	\N	\N	010100000043377F3C492640409B089C1E74834140	1672
APG22	48.191499999999998	Waypoint	APG22	APG22	\N	\N	\N	01010000003105401C3126404085763F8893834140	1673
APG23	47.710799999999999	Waypoint	APG23	APG23	\N	\N	\N	010100000091936B052B2640407142C84986834140	1674
APG3	41.702500000000001	Waypoint	APG3	APG3	\N	\N	\N	01010000009B835BCF342640408BB86BCF4D834140	1675
APG4	28.003900000000002	Waypoint	APG4	APG4	\N	\N	\N	0101000000DD262301FF25404097925C3336834140	1676
APG5	23.918299999999999	Waypoint	APG5	APG5	\N	\N	\N	0101000000FF1AB0AE01264040E934034333834140	1677
APG6	40.741199999999999	Waypoint	APG6	APG6	\N	\N	\N	0101000000C73830CD352640401D78BF4E4D834140	1678
APG7	28.725000000000001	Waypoint	APG7	APG7	\N	\N	\N	0101000000869420D510264040C272C37027834140	1679
APG8	43.625100000000003	Waypoint	APG8	APG8	\N	\N	\N	010100000058DEF0E3312640407D8FC0404C834140	1680
APG9	45.547899999999998	Waypoint	APG9	APG9	\N	\N	\N	0101000000B0FDC7B64A264040B3B7C42C5C834140	1681
APHRODISIA	556.24400000000003	Waypoint	APHRODISIA	APHRODISIA	\N	\N	\N	010100000031A5C836FDB93C40E1F5F46CB1DA4240	1682
ARC1	408.68299999999999	Flag	ARC1	ARC1	\N	\N	\N	01010000007BDD0B9420984040F5C2D7B6AC854140	1683
ARC2	409.404	Flag	ARC2	ARC2	\N	\N	\N	01010000002A2B500250984040A3F9CEF5DE854140	1684
ARC4	407.00099999999998	Flag	ARC4	ARC4	\N	\N	\N	0101000000CC132B4A5998404094DB7F2FC8854140	1685
ARC5	415.17200000000003	Flag	ARC5	ARC5	\N	\N	\N	0101000000AEC1F8F24D9840407033AFFFC7854140	1686
AS1	373.59500000000003	Flag	AS1	AS1	\N	\N	\N	0101000000311384ACA0A540400805EC3D71804140	1687
AS13P1	394.50400000000002	Waypoint	AS13P1	AS13P1	\N	\N	\N	010100000059BABFD05CA740403F15BBA6957D4140	1688
AS19A	383.68900000000002	Waypoint	AS19A	AS19A	\N	\N	\N	0101000000AE2D20DAE5A5404043F53CA3A07D4140	1689
AS19D	386.81299999999999	Waypoint	AS19D	AS19D	\N	\N	\N	010100000092EA2423F3A54040AD279CD8A17D4140	1690
AS2	371.19200000000001	Flag	AS2	AS2	\N	\N	\N	01010000001867B873B4A54040A1EE578F68804140	1691
AS20A	372.39400000000001	Waypoint	AS20A	AS20A	\N	\N	\N	010100000085B587763BA640406D691C87CD7D4140	1692
AS20D	384.17000000000002	Waypoint	AS20D	AS20D	\N	\N	\N	010100000072CDEFC484A64040205A472FCC7D4140	1693
AS21A	389.93799999999999	Waypoint	AS21A	AS21A	\N	\N	\N	0101000000745C20509CA64040ED3AD7D4D27D4140	1694
AS21P1	383.92899999999997	Waypoint	AS21P1	AS21P1	\N	\N	\N	0101000000AAFDCEDD8BA64040D493C455CF7D4140	1695
AS22A	385.61200000000002	Waypoint	AS22A	AS22A	\N	\N	\N	0101000000A92BF89DC1A640402BA3875DB87D4140	1696
AS22D	412.76900000000001	Waypoint	AS22D	AS22D	\N	\N	\N	01010000006CF612F304A740404FDAACDEC77D4140	1697
AS23A	390.178	Waypoint	AS23A	AS23A	\N	\N	\N	01010000009A50F31B47A740402DCA0C1EDD7D4140	1698
AS24A	413.73000000000002	Waypoint	AS24A	AS24A	\N	\N	\N	0101000000305297A8F8A64040BDE7D456EB7D4140	1699
AS24B	400.03100000000001	Waypoint	AS24B	AS24B	\N	\N	\N	0101000000589F0868F6A640403AF238F3087E4140	1700
AS24D	407.48099999999999	Waypoint	AS24D	AS24D	\N	\N	\N	0101000000E74B0F0DC2A640409210339B0A7E4140	1701
AS25B	387.77499999999998	Waypoint	AS25B	AS25B	\N	\N	\N	0101000000A23387A98FA640402915570C0C7E4140	1702
AS26A	384.89100000000002	Waypoint	AS26A	AS26A	\N	\N	\N	0101000000C36AF4B68EA6404097016CE2127E4140	1703
AS26C	408.68299999999999	Waypoint	AS26C	AS26C	\N	\N	\N	0101000000E05E348CC4A64040D353AC66127E4140	1704
AS27A	384.41000000000003	Waypoint	AS27A	AS27A	\N	\N	\N	0101000000C6B42FD308A740403B2F5FA80D7E4140	1705
AS27B	389.21699999999998	Waypoint	AS27B	AS27B	\N	\N	\N	0101000000741289C30BA74040170609AA367E4140	1706
AS27EX5	391.62	Waypoint	AS27EX5	AS27EX5	\N	\N	\N	0101000000EA239F40E0A64040BFD53F2A337E4140	1707
AS28A	386.57299999999998	Waypoint	AS28A	AS28A	\N	\N	\N	01010000007D99F0970BA740404FF0CE533D7E4140	1708
AS28B	370.71100000000001	Waypoint	AS28B	AS28B	\N	\N	\N	010100000092E17BF611A74040873E7BD55A7E4140	1709
AS28C	376.71899999999999	Waypoint	AS28C	AS28C	\N	\N	\N	010100000098BD5857C6A64040E6C704A1617E4140	1710
AS29A	369.99000000000001	Waypoint	AS29A	AS29A	\N	\N	\N	0101000000C18E88F49EA64040530E5430647E4140	1711
AS29F1	372.15300000000002	Waypoint	AS29F1	AS29F1	\N	\N	\N	0101000000C8D1B475C4A64040CCF7ECB6547E4140	1712
AS3	376.71899999999999	Flag	AS3	AS3	\N	\N	\N	0101000000A68B24ACD3A54040B8E060FD4C804140	1713
AS30A	369.99000000000001	Waypoint	AS30A	AS30A	\N	\N	\N	01010000000B48301270A64040E67010216C7E4140	1714
AS30EX6	363.74200000000002	Waypoint	AS30EX6	AS30EX6	\N	\N	\N	01010000000B3B786598A64040B85EEC20637E4140	1715
AS31A	359.89600000000002	Waypoint	AS31A	AS31A	\N	\N	\N	0101000000C2C00B0C6DA64040F0FAB004707E4140	1716
AS31C	382.48700000000002	Waypoint	AS31C	AS31C	\N	\N	\N	010100000052066720B1A64040428067D66D7E4140	1717
AS32A	378.642	Waypoint	AS32A	AS32A	\N	\N	\N	0101000000BFF69E5AD5A64040C1B44F976A7E4140	1718
ASM1	340.19	Waypoint	ASM1	ASM1	\N	\N	\N	01010000003EED67E6C68D414028FF585D8E424040	1719
ASP1	334.66199999999998	Flag	ASP1	ASP1	\N	\N	\N	010100000072FE98690AA74040A81149E08E824140	1720
ASP2	351.48500000000001	Flag	ASP2	ASP2	\N	\N	\N	01010000003DF5277532A74040FED5505B84824140	1721
ASP3	372.15300000000002	Flag	ASP3	ASP3	\N	\N	\N	0101000000BEF67B4B81A64040565B303F57824140	1722
ASP4	377.44	Flag	ASP4	ASP4	\N	\N	\N	01010000007704E4094CA64040B2AA442AA3824140	1723
ASP5	371.673	Flag	ASP5	ASP5	\N	\N	\N	0101000000CE7853786DA64040139D3F4FBE824140	1724
ATMA1	307.26499999999999	Flag	ATMA1	ATMA1	\N	\N	\N	0101000000DC6707EF7AAA4040A594AF825A7F4140	1725
ATMA2	321.92500000000001	Flag	ATMA2	ATMA2	\N	\N	\N	01010000002F51B87CB6A940400F280356BA7F4140	1726
ATMA3	326.97199999999998	Flag	ATMA3	ATMA3	\N	\N	\N	010100000081F4F4E17EA94040F71C44AAE97F4140	1727
ATMA4	331.05700000000002	Flag	ATMA4	ATMA4	\N	\N	\N	0101000000C4D0BF6430A940400F4FF3B122804140	1728
ATMA5	322.64600000000002	Flag	ATMA5	ATMA5	\N	\N	\N	0101000000C82F47852DA94040666EA775E77F4140	1729
ATMA6	318.07999999999998	Flag	ATMA6	ATMA6	\N	\N	\N	010100000094CEF7BB46A94040CA3D80ECC57F4140	1730
ATMA7	328.654	Flag	ATMA7	ATMA7	\N	\N	\N	0101000000F7BD88E163A9404034F3420CC47F4140	1731
ATMA8	323.12599999999998	Flag	ATMA8	ATMA8	\N	\N	\N	010100000024F83FBDA2A9404023959481A47F4140	1732
ATMA9	310.38900000000001	Flag	ATMA9	ATMA9	\N	\N	\N	01010000001CEBBC6F37AA40405184EB89627F4140	1733
ATMB1	321.20400000000001	Flag	ATMB1	ATMB1	\N	\N	\N	01010000002AC264E24AA94040ACE81486B67F4140	1734
ATMB2	321.92500000000001	Flag	ATMB2	ATMB2	\N	\N	\N	010100000042B6100A68A940408AB66032A07F4140	1735
ATMB3	321.92500000000001	Flag	ATMB3	ATMB3	\N	\N	\N	010100000089835BA96DA94040D80D9441997F4140	1736
ATMB4	320.00200000000001	Flag	ATMB4	ATMB4	\N	\N	\N	010100000001ACF80089A9404060ACF398967F4140	1737
ATMB5	318.31999999999999	Flag	ATMB5	ATMB5	\N	\N	\N	0101000000B173A075B7A940403E3783AB7B7F4140	1738
ATMB6	321.44400000000002	Flag	ATMB6	ATMB6	\N	\N	\N	0101000000F016F708BDA9404049F560FC7B7F4140	1739
ATV1	307.745	Flag	ATV1	ATV1	\N	\N	\N	01010000008EED8768CCAA40406067707F617F4140	1740
ATV10	334.90199999999999	Flag	ATV10	ATV10	\N	\N	\N	01010000007C446325DAA94040397064A4B27E4140	1741
ATV2	300.77600000000001	Flag	ATV2	ATV2	\N	\N	\N	010100000044CD6C6492AA404088D0F451477F4140	1742
ATV3	309.428	Flag	ATV3	ATV3	\N	\N	\N	0101000000B628074174AA40408D180B7D4F7F4140	1743
ATV5	304.38099999999997	Flag	ATV5	ATV5	\N	\N	\N	0101000000E9A0C7FF4FAA4040408C8CE5487F4140	1746
ATV6	317.839	Flag	ATV6	ATV6	\N	\N	\N	0101000000BC99942CEEA94040C3012F5E2C7F4140	1747
ATV7	314.95499999999998	Flag	ATV7	ATV7	\N	\N	\N	0101000000761E031CBCA94040FB863B77F87E4140	1748
ATV8	335.62299999999999	Flag	ATV8	ATV8	\N	\N	\N	0101000000D05798D7F4A84040A9B4C8ED8C7E4140	1749
ATV9	323.12599999999998	Flag	ATV9	ATV9	\N	\N	\N	0101000000FB8B3B066DA94040C972BC49A67E4140	1750
AUG04T06	781.67200000000003	Waypoint	AUG04T06	AUG04T06	\N	\N	\N	01010000003FC18BDF905C4040206C885C36794140	1751
AUG04T07	779.26800000000003	Waypoint	AUG04T07	AUG04T07	\N	\N	\N	0101000000814387448A5C4040DF4EDBEC33794140	1752
AUG04T08	836.46699999999998	Waypoint	AUG04T08	AUG04T08	\N	\N	\N	01010000007E671B829A5C40409226505746794140	1753
AUG04T09	798.97500000000002	Waypoint	AUG04T09	AUG04T09	\N	\N	\N	0101000000C831FC21A25C4040990250C74E794140	1754
AUG04T10	850.40599999999995	Waypoint	AUG04T10	AUG04T10	\N	\N	\N	0101000000C942B36C9F5C4040DEFE9E5076784140	1755
AUG04T11	851.12599999999998	Waypoint	AUG04T11	AUG04T11	\N	\N	\N	01010000008B7F0FC69E5C404023F536E774784140	1756
AUG24T01	464.19900000000001	Waypoint	AUG24T01	AUG24T01	\N	\N	\N	01010000000809145B085A4040D8F51F5A2A774140	1757
AUG24T02	632.90899999999999	Waypoint	AUG24T02	AUG24T02	\N	\N	\N	01010000000DAC436B4E5B4040F4599B4FA8784140	1758
AUG24T03	642.28200000000004	Waypoint	AUG24T03	AUG24T03	\N	\N	\N	0101000000F8A7BC6CA75B4040FE0CAC39F9784140	1759
AUG24T04	618.97000000000003	Waypoint	AUG24T04	AUG24T04	\N	\N	\N	01010000004A1EF4119D5B40409E2453C105794140	1760
AUG24T05	653.096	Waypoint	AUG24T05	AUG24T05	\N	\N	\N	01010000000FF27FE9A25B40402BF027C0F6784140	1761
AV06 4	405.31799999999998	Waypoint	AV06 4	AV06 4	\N	\N	\N	0101000000B722AB073E94404070052B861F864140	1762
AV06 5	409.404	Waypoint	AV06 5	AV06 5	\N	\N	\N	0101000000CE7BE0863F944040289553AF19864140	1763
AV12	309.66800000000001	Flag	AV12	AV12	\N	\N	\N	0101000000F20E41EC44AB4040879A373A667F4140	1764
AV13	305.10199999999998	Flag	AV13	AV13	\N	\N	\N	01010000003A335C4548AB4040508EA4D27F7F4140	1765
AV14	327.69299999999998	Flag	AV14	AV14	\N	\N	\N	0101000000A090F3B76AAB4040FBF767A67E7F4140	1766
AV15	305.58300000000003	Flag	AV15	AV15	\N	\N	\N	01010000009D15D74C4CAB4040643A23339F7F4140	1767
AV3	318.07999999999998	Flag	AV3	AV3	\N	\N	\N	010100000030A6BCFD89AB40404007CD165F7F4140	1768
AV4	318.56	Flag	AV4	AV4	\N	\N	\N	01010000004729A79164AB404062D8D3A1627F4140	1769
AV4A	321.44400000000002	Flag	AV4A	AV4A	\N	\N	\N	0101000000436173B565AB404087BA844D617F4140	1770
AV5	327.69299999999998	Flag	AV5	AV5	\N	\N	\N	01010000003211A1D780AB40409F772B97427F4140	1771
AV6	317.839	Flag	AV6	AV6	\N	\N	\N	01010000003AFA58F460AB4040010633BA447F4140	1772
AVA06-1	309.66800000000001	Waypoint	AVA06-1	AVA06-1	\N	\N	\N	010100000025C2CB7B38AB4040FFA407296B7F4140	1773
AVA06-1BIS	303.42000000000002	Waypoint	AVA06-1BIS	AVA06-1BIS	\N	\N	\N	010100000093C2F47138AB4040322AC7696A7F4140	1774
AVABM1	318.56	Waypoint	AVABM1	AVABM1	\N	\N	\N	0101000000F483C77263AB4040F774700E697F4140	1775
AVABM1 E	322.40499999999997	Waypoint	AVABM1 E	AVABM1 E	\N	\N	\N	0101000000A640A88363AB404003ED34F0687F4140	1776
AVABM1TRIS	324.08800000000002	Waypoint	AVABM1TRIS	AVABM1TRIS	\N	\N	\N	0101000000B83187EE63AB40401ABA3791697F4140	1777
AVABM2	323.36700000000002	Waypoint	AVABM2	AVABM2	\N	\N	\N	01010000006926105D6DAB40401C037373787F4140	1778
AVABM2 C	321.92500000000001	Waypoint	AVABM2 C	AVABM2 C	\N	\N	\N	01010000002179FB346DAB4040FE99CF26787F4140	1779
AVAGPESTLE	316.15699999999998	Waypoint	AVAGPESTLE	AVAGPESTLE	\N	\N	\N	0101000000074C708260AB40409D02ECE56F7F4140	1780
AVAGS 1 06	321.68400000000003	Waypoint	AVAGS 1 06	AVAGS 1 06	\N	\N	\N	01010000001BA6B31C73AB4040751884D3717F4140	1781
AVALMSTNBW	324.56799999999998	Waypoint	AVALMSTNBW	AVALMSTNBW	\N	\N	\N	010100000000A8F4A969AB404009833FB7687F4140	1782
AVAOATS1	321.20400000000001	Waypoint	AVAOATS1	AVAOATS1	\N	\N	\N	0101000000E013F00D73AB40402FE28C786A7F4140	1783
AVAOATS1B	322.64600000000002	Waypoint	AVAOATS1B	AVAOATS1B	\N	\N	\N	01010000009367246371AB404063246C596D7F4140	1784
AVAOATS1C	321.44400000000002	Waypoint	AVAOATS1C	AVAOATS1C	\N	\N	\N	010100000016CDC8F576AB4040C18193E46E7F4140	1785
AVAOATS1D	321.20400000000001	Waypoint	AVAOATS1D	AVAOATS1D	\N	\N	\N	01010000009C54DB1D77AB40406401D90C6C7F4140	1786
AVAOATS2A	321.92500000000001	Waypoint	AVAOATS2A	AVAOATS2A	\N	\N	\N	01010000007533CC9E7EAB4040486FE8C8727F4140	1787
AVAOATS2B	320.00200000000001	Waypoint	AVAOATS2B	AVAOATS2B	\N	\N	\N	010100000081AB90807EAB4040E0A410986C7F4140	1788
AVAOATS2C	320.72300000000001	Waypoint	AVAOATS2C	AVAOATS2C	\N	\N	\N	0101000000FFDF78417BAB40401FBC4BE9667F4140	1789
AVAOATS2D	321.20400000000001	Waypoint	AVAOATS2D	AVAOATS2D	\N	\N	\N	01010000007F48AB9976AB4040ABBC6B70657F4140	1790
AVAP1	319.762	Flag	AVAP1	AVAP1	\N	\N	\N	0101000000A3F7D2839EAD4040A784D4ABD07F4140	1791
AVAP2	311.11000000000001	Flag	AVAP2	AVAP2	\N	\N	\N	010100000005F0869C0DAE40405DC9ACE31A804140	1792
AVAPESTLE	318.80099999999999	Waypoint	AVAPESTLE	AVAPESTLE	\N	\N	\N	01010000002575904460AB4040A0EDB20E707F4140	1793
AVATR11 SE	319.762	Waypoint	AVATR11 SE	AVATR11 SE	\N	\N	\N	01010000002D5A6CD26EAB40402BD79C02677F4140	1800
AVATR12NE	320.483	Waypoint	AVATR12NE	AVATR12NE	\N	\N	\N	010100000013FCB8FB71AB40405F7FCBC96F7F4140	1803
AVATR12NW	320.964	Waypoint	AVATR12NW	AVATR12NW	\N	\N	\N	010100000026FE4EB16FAB4040CCA2870C717F4140	1804
AVATR12SE	322.16500000000002	Waypoint	AVATR12SE	AVATR12SE	\N	\N	\N	010100000040255B1B72AB40404407858A6F7F4140	1805
AVATR12SW	321.68400000000003	Waypoint	AVATR12SW	AVATR12SW	\N	\N	\N	01010000009A9A18536FAB4040FC7C03AF707F4140	1806
AVATR1SW	317.35899999999998	Waypoint	AVATR1SW	AVATR1SW	\N	\N	\N	010100000042394B7573AB4040E4ACC7FE6B7F4140	1807
AVATR1SW D	317.839	Waypoint	AVATR1SW D	AVATR1SW D	\N	\N	\N	0101000000331643F573AB404040D36BE36A7F4140	1808
AVATR1SWE	320.24200000000002	Waypoint	AVATR1SWE	AVATR1SWE	\N	\N	\N	0101000000A70F805F73AB40403A37CB376B7F4140	1809
AVATR2NE	318.56	Waypoint	AVATR2NE	AVATR2NE	\N	\N	\N	0101000000F3A1B8A171AB404060F38BC16C7F4140	1810
AVATR2NW	320.483	Waypoint	AVATR2NW	AVATR2NW	\N	\N	\N	01010000001A6F307670AB4040C50944366D7F4140	1811
AVATR2SW2	320.00200000000001	Waypoint	AVATR2SW2	AVATR2SW2	\N	\N	\N	0101000000FA8E93226FAB404022AA4B216B7F4140	1812
AVATR2SW A	319.52100000000002	Waypoint	AVATR2SW A	AVATR2SW A	\N	\N	\N	010100000038B540766FAB4040739E44936A7F4140	1813
AVATR3CHAR	322.64600000000002	Waypoint	AVATR3CHAR	AVATR3CHAR	\N	\N	\N	01010000000AACF8936CAB4040530D9DCC6F7F4140	1814
AVATR3SW1	322.16500000000002	Waypoint	AVATR3SW1	AVATR3SW1	\N	\N	\N	0101000000812DF3796CAB4040F36FFFE06F7F4140	1815
AVATR4SW1	319.28100000000001	Waypoint	AVATR4SW1	AVATR4SW1	\N	\N	\N	0101000000C6A9270A6CAB4040607928BB6D7F4140	1816
AVATREEBM	321.20400000000001	Waypoint	AVATREEBM	AVATREEBM	\N	\N	\N	01010000008E798FC6B0AB4040110428435E7F4140	1817
AVK1	296.69	Flag	AVK1	AVK1	\N	\N	\N	01010000002A7F406511B14040369A088D33804140	1818
AVK10	307.745	Flag	AVK10	AVK10	\N	\N	\N	01010000008006C4B9C0B04040A4F55C4B6D7F4140	1819
AVK11	300.29500000000002	Flag	AVK11	AVK11	\N	\N	\N	0101000000F4C77C8CAEB04040FE531429B97F4140	1820
AVK12	295.00799999999998	Flag	AVK12	AVK12	\N	\N	\N	0101000000B16C37D8D3B0404037E0C38AF27F4140	1821
AVK2	279.62700000000001	Flag	AVK2	AVK2	\N	\N	\N	01010000003C4CAF58F9B04040C4317C5830804140	1822
AVK3	276.98399999999998	Flag	AVK3	AVK3	\N	\N	\N	0101000000133F1F953DB1404077C234D474804140	1823
AVK4	267.851	Flag	AVK4	AVK4	\N	\N	\N	01010000005BA7F77854B140407CDC38DB3C804140	1824
AVK5	265.928	Flag	AVK5	AVK5	\N	\N	\N	01010000005BE0583B7DB140404322A78E19804140	1825
AVK6	283.95299999999997	Flag	AVK6	AVK6	\N	\N	\N	0101000000CA1B283C9DB14040E591B425EF7F4140	1826
AVK7	293.80599999999998	Flag	AVK7	AVK7	\N	\N	\N	0101000000BD21C7B565B1404094149FD59E7F4140	1827
AVK8	323.36700000000002	Flag	AVK8	AVK8	\N	\N	\N	0101000000278BE4F356B14040CCAB632A777F4140	1828
AVK9	329.375	Flag	AVK9	AVK9	\N	\N	\N	010100000053C6F77917B14040D51CF05E4D7F4140	1829
AVKX1	276.74299999999999	Flag	AVKX1	AVKX1	\N	\N	\N	0101000000D4F2BC812DB140406D78177B71804140	1830
AV-P1	298.85300000000001	Flag	AV-P1	AV-P1	\N	\N	\N	01010000009D726B2375AE40402FF49AB4EF804140	1833
AV-P2	312.79199999999997	Flag	AV-P2	AV-P2	\N	\N	\N	01010000001DA0D3AA58AE40404225F07FEE804140	1836
AVP3	344.03500000000003	Flag	AVP3	AVP3	\N	\N	\N	01010000005D082D7C21AD40402BABC0FBF07D4140	1837
AV-P3	309.90800000000002	Flag	AV-P3	AV-P3	\N	\N	\N	0101000000D0491F5535AE40403C8150E1FF804140	1838
AVP4	331.29700000000003	Flag	AVP4	AVP4	\N	\N	\N	0101000000852FBB4324AD4040978588312F7E4140	1839
AVP5	316.39699999999999	Flag	AVP5	AVP5	\N	\N	\N	0101000000BA9DC4BA0DAE4040BE3134395D7E4140	1840
AVP6	318.80099999999999	Flag	AVP6	AVP6	\N	\N	\N	010100000030E41896EDAD4040C9FCD660377E4140	1841
AVP7	314.71499999999997	Flag	AVP7	AVP7	\N	\N	\N	0101000000770D116FD1AD4040A8FF606B2C7E4140	1842
AVPT1	318.07999999999998	Flag	AVPT1	AVPT1	\N	\N	\N	0101000000D5B897310EAE40409AFBFA0D927F4140	1843
AVPT2	317.11799999999999	Flag	AVPT2	AVPT2	\N	\N	\N	01010000000F98F4E827AE4040BEC35F26D97F4140	1844
AVQ1	318.07999999999998	Flag	AVQ1	AVQ1	\N	\N	\N	0101000000D8FAB24C70AB4040195A8BB9657F4140	1845
AVR1	311.35000000000002	Flag	AVR1	AVR1	\N	\N	\N	0101000000273DE8E656AB404010886858727F4140	1846
AVR10	317.35899999999998	Flag	AVR10	AVR10	\N	\N	\N	01010000001503B7FD51AB4040D1CE434F457F4140	1847
AVR11	310.149	Flag	AVR11	AVR11	\N	\N	\N	0101000000A49FBFBC3FAB4040881078DC477F4140	1848
AVR12	313.27300000000002	Flag	AVR12	AVR12	\N	\N	\N	0101000000C140E7A051AB4040FBC49CC8557F4140	1849
AVR13	311.83100000000002	Flag	AVR13	AVR13	\N	\N	\N	01010000009D89BB0A41AB4040B56BFB84577F4140	1850
AVR14	310.149	Flag	AVR14	AVR14	\N	\N	\N	01010000001D5ED32D42AB404052A414D7657F4140	1851
AVR15	317.839	Flag	AVR15	AVR15	\N	\N	\N	0101000000A85C97D053AB4040E76B1FBD647F4140	1852
AVR16	325.52999999999997	Flag	AVR16	AVR16	\N	\N	\N	0101000000A5CCE78B66AB4040B3CCCC13627F4140	1853
AVR17	320.00200000000001	Flag	AVR17	AVR17	\N	\N	\N	01010000004D85C4B263AB404005E357E1537F4140	1854
AVR2	320.00200000000001	Flag	AVR2	AVR2	\N	\N	\N	0101000000F586008267AB404055DB73B7707F4140	1855
AVR3	304.38099999999997	Flag	AVR3	AVR3	\N	\N	\N	01010000001979AF1344AB4040072137FB777F4140	1856
AVR4	303.89999999999998	Flag	AVR4	AVR4	\N	\N	\N	0101000000B733079948AB4040BB2653EE867F4140	1857
AVR5	325.04899999999998	Flag	AVR5	AVR5	\N	\N	\N	0101000000F6A093EA6AAB404025994C4A7E7F4140	1858
AVT4	302.45800000000003	Flag	AVT4	AVT4	\N	\N	\N	01010000003C0A9DF8E6AF4040D28E881A26804140	1865
AVT5	309.18700000000001	Flag	AVT5	AVT5	\N	\N	\N	0101000000C999D725ABAF4040D7A0D4034B804140	1866
AVT6	308.226	Flag	AVT6	AVT6	\N	\N	\N	010100000047ECB015B6AF404023022CF7A4804140	1867
AVY1	298.13200000000001	Flag	AVY1	AVY1	\N	\N	\N	01010000001CF328FF94AB40403F889C7250804140	1868
AYANIS TEM	1860.02	Waypoint	AYANIS TEM	AYANIS TEM	\N	\N	\N	0101000000A3001C3E139B4540613AE786AA5A4340	1869
AYPET	406.75999999999999	Flag	AYPET	AYPET	\N	\N	\N	01010000003D6CB79753974040609D90C1BD844140	1870
AYVAR1	327.93299999999999	Waypoint	AYVAR1	AYVAR1	\N	\N	\N	010100000049C66CF46AAB4040702ECBC5827F4140	1871
AYVAR3	317.11799999999999	Waypoint	AYVAR3	AYVAR3	\N	\N	\N	01010000003759B70289AB4040329EAB275F7F4140	1874
AYVAR4	319.28100000000001	Waypoint	AYVAR4	AYVAR4	\N	\N	\N	0101000000F7111BD363AB40406235476A627F4140	1875
AYVAR8	319.28100000000001	Waypoint	AYVAR8	AYVAR8	\N	\N	\N	01010000007EFC7053ADAB40406F01A3F65B7F4140	1876
AYVAR JH1	319.52100000000002	Waypoint	AYVAR JH1	AYVAR JH1	\N	\N	\N	0101000000828D9F5170AB40402A8B044B5C7F4140	1877
AYVAR JH1A	319.041	Waypoint	AYVAR JH1A	AYVAR JH1A	\N	\N	\N	010100000059E6179F6EAB4040FF6C8391597F4140	1878
AYVAR JH2	317.59899999999999	Waypoint	AYVAR JH2	AYVAR JH2	\N	\N	\N	01010000005210977770AB4040B7CEE06B5F7F4140	1879
AYVAR JH3	316.87799999999999	Waypoint	AYVAR JH3	AYVAR JH3	\N	\N	\N	01010000005AE950AE72AB4040320A91F2647F4140	1880
AYVAR JH4	316.39699999999999	Waypoint	AYVAR JH4	AYVAR JH4	\N	\N	\N	010100000091D6732D75AB40402900D35D6A7F4140	1881
AYVAR JH5	316.39699999999999	Waypoint	AYVAR JH5	AYVAR JH5	\N	\N	\N	0101000000AAD134F367AB4040B090B4796E7F4140	1882
AYVAR PLAT	337.786	Waypoint	AYVAR PLAT	AYVAR PLAT	\N	\N	\N	010100000076FBD7B22FAD40406D7FF726F77D4140	1883
AZ04-24	1671.8499999999999	Waypoint	AZ04-24	AZ04-24	\N	\N	\N	01010000009AB1683AFB424040EE994BC8CC1B4240	1884
BARLEY1	59.486800000000002	Waypoint	BARLEY1	BARLEY1	\N	\N	\N	0101000000E148FB7A59A640404CDDD779C2614140	1885
BASEPT2	117.40600000000001	Waypoint	BASEPT2	BASEPT2	\N	\N	\N	01010000002C1DD4C701AC40402B283FBB19614140	1886
BAYRAMIC D	122.453	Waypoint	BAYRAMIC D	BAYRAMIC D	\N	\N	\N	0101000000BC1F1F43338B3A40EA286CDE6FE64340	1887
BU5	45.547899999999998	Waypoint	BU5	BU5	\N	\N	\N	0101000000A5D66371CB1E53C09A48A89A123B4540	1888
CAB 11-26	672.32299999999998	Waypoint	CAB 11-26	CAB 11-26	\N	\N	\N	01010000002C2241F364AA3740FAFB3E7CD63A4440	1889
CAGLAYANCE	1426.47	Waypoint	CAGLAYANCE	CAGLAYANCE	\N	\N	\N	0101000000C1223B46A4A84240DD31B4BBB9E14240	1890
CAT 16-19	1455.0699999999999	Waypoint	CAT 16-19	CAT 16-19	\N	\N	\N	010100000089A3C9CAD1213F405788C0F1F2FB4340	1891
CAT 20-39	1673.05	Waypoint	CAT 20-39	CAT 20-39	\N	\N	\N	01010000000EBC2F6EF0153F403816F8B075FA4340	1892
CAT CAMP	1569.23	Waypoint	CAT CAMP	CAT CAMP	\N	\N	\N	0101000000DB1448B6DE213F4061CC4766F6FB4340	1893
CAVE	317.59899999999999	Flag	CAVE	CAVE	\N	\N	\N	0101000000D589C437E6B24040413407C9817D4140	1894
CEMETARY	464.67899999999997	Waypoint	CEMETARY	CEMETARY	\N	\N	\N	01010000009C5242D6112C53C04099283D31364540	1895
CFS-1	1564.4200000000001	Waypoint	CFS-1	CFS-1	\N	\N	\N	0101000000EDCE38DF587F404000BEFF82697A4140	1896
CFS10	1544.95	Waypoint	CFS10	CFS10	\N	\N	\N	0101000000CF6C5CA1BD7F4040BEFE7FF7297A4140	1897
CFS11	1559.3699999999999	Waypoint	CFS11	CFS11	\N	\N	\N	0101000000B229E069357F4040672A87C0587A4140	1898
CFS2	1556.97	Waypoint	CFS2	CFS2	\N	\N	\N	0101000000B8A3D71D4D7F4040FE7DA0BE607A4140	1899
CFS3	1554.0899999999999	Waypoint	CFS3	CFS3	\N	\N	\N	0101000000620D9BF14B7F40401F9B63C7667A4140	1900
CFS-4	1557.45	Waypoint	CFS-4	CFS-4	\N	\N	\N	0101000000EAA5573A477F4040E33F4725617A4140	1901
CFS-5	1561.78	Waypoint	CFS-5	CFS-5	\N	\N	\N	01010000005729E41A447F4040DC40B468577A4140	1902
CFS6	1559.8499999999999	Waypoint	CFS6	CFS6	\N	\N	\N	01010000002C11F945417F4040C53674765C7A4140	1903
CFS7	1559.6099999999999	Waypoint	CFS7	CFS7	\N	\N	\N	0101000000F1F8983D407F4040429A28775C7A4140	1904
CFS-8	1560.0899999999999	Waypoint	CFS-8	CFS-8	\N	\N	\N	010100000098AE3C55397F40409E1A0715577A4140	1905
CFS-9	1549.76	Waypoint	CFS-9	CFS-9	\N	\N	\N	0101000000F8133F0D327F40402AF0B8225D7A4140	1906
CHF1	547.35199999999998	Waypoint	CHF1	CHF1	\N	\N	\N	01010000006878D9CD11A73940721C3B5204894140	1907
CHF10	569.46199999999999	Waypoint	CHF10	CHF10	\N	\N	\N	01010000002692B0BBB5A639403BDCFFE1DD884140	1908
CHF11	632.66899999999998	Waypoint	CHF11	CHF11	\N	\N	\N	0101000000135546C3ACA6394081431F61D5884140	1909
CHF13	565.37699999999995	Waypoint	CHF13	CHF13	\N	\N	\N	0101000000989791B4FAA63940E258A0A8EF884140	1910
CHF14	583.88199999999995	Waypoint	CHF14	CHF14	\N	\N	\N	0101000000A7459176A2A63940D9C85CB6E9884140	1911
CHF2	551.678	Waypoint	CHF2	CHF2	\N	\N	\N	010100000051D4E8C11CA73940124B53CF05894140	1912
CHF3	547.59299999999996	Waypoint	CHF3	CHF3	\N	\N	\N	01010000002BF7FDF908A73940F22A33F103894140	1913
CHF4	542.06500000000005	Waypoint	CHF4	CHF4	\N	\N	\N	01010000009BEE260C0BA73940151387AB04894140	1914
CHF5	550.95699999999999	Waypoint	CHF5	CHF5	\N	\N	\N	010100000045F377241BA739408815C4F901894140	1915
CHF6	550.95699999999999	Waypoint	CHF6	CHF6	\N	\N	\N	010100000041166A9901A73940353C83A70C894140	1916
CHUR	250.547	Flag	CHUR	CHUR	\N	\N	\N	01010000007EE0501603B340400649BBAB807E4140	1917
CHURCH	106.831	Building	CHURCH	CHURCH	\N	\N	\N	0101000000D3D41027406D3B40053BAFC5AF034340	1918
CIGLIKARA	1827.8199999999999	Waypoint	CIGLIKARA	CIGLIKARA	\N	\N	\N	0101000000A7F0215A0BC63D4005586FCA18404240	1919
CKB	45.307499999999997	Waypoint	CKB	CKB	\N	\N	\N	0101000000E414E0654D613A406F2E9B8CEC124440	1920
CKF1	1205.1300000000001	Waypoint	CKF1	CKF1	\N	\N	\N	01010000001C3741439BD73840E040E3EA89954140	1921
CKF-10	1223.8699999999999	Waypoint	CKF-10	CKF-10	\N	\N	\N	010100000090C2DE8C66D738402024B0DE8D954140	1922
CKF-11	1225.0799999999999	Waypoint	CKF-11	CKF-11	\N	\N	\N	01010000004E7D207967D7384081F8D07090954140	1923
CKF12	1319.53	Waypoint	CKF12	CKF12	\N	\N	\N	01010000000D4919B065D73840F334FB648E954140	1924
CKF-13	1226.28	Waypoint	CKF-13	CKF-13	\N	\N	\N	01010000009F397EB75FD73840722603C68E954140	1925
CKF14	1226.76	Waypoint	CKF14	CKF14	\N	\N	\N	0101000000293D1FDA51D7384037F66B3977954140	1926
CKF-15	1298.1400000000001	Waypoint	CKF-15	CKF-15	\N	\N	\N	0101000000C64649DD08D7384045024D519C964140	1927
CKF-16	1319.53	Waypoint	CKF-16	CKF-16	\N	\N	\N	0101000000DA1CB7FD4AD7384010FCDAC07E964140	1928
CKF 17	1318.3199999999999	Waypoint	CKF 17	CKF 17	\N	\N	\N	0101000000355870F351D73840419030CA7B964140	1929
CKF18	1315.9200000000001	Waypoint	CKF18	CKF18	\N	\N	\N	0101000000B592D7FC58D73840F5BA3BEE78964140	1930
CKF2	1218.3499999999999	Waypoint	CKF2	CKF2	\N	\N	\N	01010000002B9C283288D738404DEA3B278C954140	1931
CKF3	1210.9000000000001	Waypoint	CKF3	CKF3	\N	\N	\N	0101000000EA6EA1A29FD738401B33779785954140	1932
CKF4	1214.26	Waypoint	CKF4	CKF4	\N	\N	\N	0101000000557D906088D738408EF5305B82954140	1933
CKF-5	1204.1700000000001	Waypoint	CKF-5	CKF-5	\N	\N	\N	0101000000276F8FC49CD738407AD0148385954140	1934
CKF6	1213.54	Waypoint	CKF6	CKF6	\N	\N	\N	01010000004AB390B87BD7384044D7B81081954140	1935
CKF7	1209.21	Waypoint	CKF7	CKF7	\N	\N	\N	010100000067BA078393D7384049D66F9382954140	1936
CKF8	1206.5699999999999	Waypoint	CKF8	CKF8	\N	\N	\N	01010000008FAC6E8880D73840CE205C067E954140	1937
CKF-9	1202.25	Waypoint	CKF-9	CKF-9	\N	\N	\N	0101000000C005FAC065D738409BDB1F507F954140	1938
CKF FUTURE	1335.1500000000001	Waypoint	CKF FUTURE	CKF FUTURE	\N	\N	\N	0101000000A372DEC2E4D73840731F6FA84E964140	1939
CKF PARK	1220.75	Waypoint	CKF PARK	CKF PARK	\N	\N	\N	0101000000220D41EA03D7384063EFE62596954140	1940
CKL10	20.7941	Waypoint	CKL10	CKL10	\N	\N	\N	0101000000048A906E4F29404046DCC41254784140	1941
CKL12	24.398900000000001	Waypoint	CKL12	CKL12	\N	\N	\N	01010000001F9D64F260294040BF0EBD414B784140	1942
CKL3	24.1586	Waypoint	CKL3	CKL3	\N	\N	\N	01010000004CB52B016C294040232B18C54D784140	1943
CKL4	23.4376	Waypoint	CKL4	CKL4	\N	\N	\N	01010000004A2FB3CD6229404075D354B94A784140	1944
CKL5	27.0426	Waypoint	CKL5	CKL5	\N	\N	\N	01010000006318BC5749294040BA84B00B60784140	1945
CKL6	20.313400000000001	Waypoint	CKL6	CKL6	\N	\N	\N	010100000041411FE84529404040AF4F6B60784140	1946
CKL7	18.871500000000001	Waypoint	CKL7	CKL7	\N	\N	\N	010100000010E564125029404022F2147466784140	1947
CKL8	29.445900000000002	Waypoint	CKL8	CKL8	\N	\N	\N	0101000000745557404C294040F191209E5D784140	1948
CKL9	11.180899999999999	Waypoint	CKL9	CKL9	\N	\N	\N	01010000001D8BD07C4C2940405805281566784140	1949
CKM1-9	253.91200000000001	Waypoint	CKM1-9	CKM1-9	\N	\N	\N	0101000000BF19CF7CDBDB3940FFD5E006878F4140	1950
CLA07	16.4681	Waypoint	CLA07	CLA07	\N	\N	\N	0101000000E69D4061DB274040A0C860B3067A4140	1951
CLA-1	20.7941	Waypoint	CLA-1	CLA-1	\N	\N	\N	0101000000C0B2D75DCE274040F7C26C1B297A4140	1952
CLA10	15.2666	Waypoint	CLA10	CLA10	\N	\N	\N	010100000087DD0F29DA27404083023302117A4140	1953
CLA11	19.592400000000001	Waypoint	CLA11	CLA11	\N	\N	\N	0101000000FF303FC0E527404049909840147A4140	1954
CLA12	17.429600000000001	Waypoint	CLA12	CLA12	\N	\N	\N	0101000000840C002FD52740409807F59C107A4140	1955
CLA13	17.9102	Waypoint	CLA13	CLA13	\N	\N	\N	0101000000C6DDF020D52740407077C7430E7A4140	1956
CLA14	20.0731	Waypoint	CLA14	CLA14	\N	\N	\N	0101000000046D3394E1274040E9B5D49F0F7A4140	1957
CLA15	20.7941	Waypoint	CLA15	CLA15	\N	\N	\N	010100000043107DFDE427404002914852077A4140	1958
CLA16	10.460000000000001	Waypoint	CLA16	CLA16	\N	\N	\N	01010000001E7A88CFE827404003E8180C057A4140	1959
CLA18	14.545500000000001	Waypoint	CLA18	CLA18	\N	\N	\N	0101000000F050F3D9EA2740403142BF89FD794140	1960
CLA19	25.840900000000001	Waypoint	CLA19	CLA19	\N	\N	\N	01010000005AAD586B042840404FD6E7EBEB794140	1961
CLA2	24.639299999999999	Waypoint	CLA2	CLA2	\N	\N	\N	01010000001A540400D3274040182BEBB0227A4140	1962
CLA-2	23.4376	Waypoint	CLA-2	CLA-2	\N	\N	\N	0101000000734F6B5BD32740408380D008237A4140	1963
CLA20	16.4681	Waypoint	CLA20	CLA20	\N	\N	\N	010100000097F282D1EA274040F75187C1EE794140	1964
CLA-21	23.4376	Waypoint	CLA-21	CLA-21	\N	\N	\N	01010000009FD6740AE0274040744A3322007A4140	1965
CLA-3	21.755400000000002	Waypoint	CLA-3	CLA-3	\N	\N	\N	01010000001783F405CE274040831DC75B237A4140	1966
CLA-4	20.553699999999999	Waypoint	CLA-4	CLA-4	\N	\N	\N	010100000080419343D2274040F4FFDA5C1D7A4140	1967
CLA5	21.995699999999999	Waypoint	CLA5	CLA5	\N	\N	\N	01010000007E705F83D52740408BA78B051F7A4140	1968
CLA6	19.832799999999999	Waypoint	CLA6	CLA6	\N	\N	\N	010100000098AB7F0DD12740403A4D43AD197A4140	1969
CLA7	21.755400000000002	Waypoint	CLA7	CLA7	\N	\N	\N	010100000023DB6BD4D2274040B351708D1C7A4140	1970
CLA8	21.755400000000002	Waypoint	CLA8	CLA8	\N	\N	\N	0101000000FF819031D9274040B4DFE7B3147A4140	1971
CLA9	19.111799999999999	Waypoint	CLA9	CLA9	\N	\N	\N	01010000000D023040D0274040DF7D6F82187A4140	1972
CLK1	25.120000000000001	Waypoint	CLK1	CLK1	\N	\N	\N	0101000000FD195F2D572940400DA4F33060784140	1973
CLK11	22.957000000000001	Waypoint	CLK11	CLK11	\N	\N	\N	0101000000C3204B47422940403D6D94C26A784140	1974
CLK2	26.081199999999999	Waypoint	CLK2	CLK2	\N	\N	\N	01010000007F65705759294040A1BA176A5D784140	1975
CMH15	510.34199999999998	Waypoint	CMH15	CMH15	\N	\N	\N	0101000000A4746F99E18B4040B163981DA6834140	1976
CMH17	498.80599999999998	Waypoint	CMH17	CMH17	\N	\N	\N	0101000000504C741CD38B404058951FE682834140	1977
CMH3	501.69	Waypoint	CMH3	CMH3	\N	\N	\N	0101000000D80121A3CD8B40408457F365A7834140	1978
CMH7	517.79200000000003	Waypoint	CMH7	CMH7	\N	\N	\N	010100000027CBB8C5D68B4040E7C7FC2F99834140	1979
CMM1	455.78699999999998	Waypoint	CMM1	CMM1	\N	\N	\N	0101000000A2AF283EA18D40403EBD03508D854140	1980
CMM2	456.26799999999997	Waypoint	CMM2	CMM2	\N	\N	\N	0101000000CD0D2D82A48D40409AFD5E6387854140	1981
CPL1	1123.4200000000001	Waypoint	CPL1	CPL1	\N	\N	\N	0101000000C0F75A77C37640402A8A437534794140	1982
CPL10	1116.6900000000001	Waypoint	CPL10	CPL10	\N	\N	\N	0101000000B967F3BEC57640409BEF969A33794140	1983
CPL11	1111.8800000000001	Waypoint	CPL11	CPL11	\N	\N	\N	0101000000217A4043B97640407A32A42F29794140	1984
CPL12	1130.8699999999999	Waypoint	CPL12	CPL12	\N	\N	\N	0101000000FCC0B8C8BB764040D3101B4D2A794140	1985
CPL2	1133.75	Waypoint	CPL2	CPL2	\N	\N	\N	01010000009C6FD75ABE7640400BF09BCA3B794140	1986
CPL3	1125.5799999999999	Waypoint	CPL3	CPL3	\N	\N	\N	0101000000D20BC004C3764040B1E0204F42794140	1987
CPL4	517.79200000000003	Waypoint	CPL4	CPL4	\N	\N	\N	0101000000024A0BE3E37640407E3D94A52C794140	1988
CPL5	1118.1300000000001	Waypoint	CPL5	CPL5	\N	\N	\N	0101000000912B50E6BA764040C31F181C32794140	1989
CPL6	1110.9200000000001	Waypoint	CPL6	CPL6	\N	\N	\N	0101000000FB69E80EBE7640409B47684540794140	1990
CPL7	1205.3699999999999	Waypoint	CPL7	CPL7	\N	\N	\N	010100000064DF27A4BB764040BA41BCC73C794140	1991
CPL8	1118.3699999999999	Waypoint	CPL8	CPL8	\N	\N	\N	010100000031BA3801B8764040AD9DAC412D794140	1992
CPL9	1132.55	Waypoint	CPL9	CPL9	\N	\N	\N	0101000000478A9877B4764040DEFDA01736794140	1993
CR0SSN	301.97800000000001	Flag	CR0SSN	CR0SSN	\N	\N	\N	010100000031CD071365AB4040B8A704F41D7E4140	1994
CR1	320.00200000000001	Flag	CR1	CR1	\N	\N	\N	0101000000F79AF0DB68AB4040A3445F02767F4140	1995
CR2	321.68400000000003	Flag	CR2	CR2	\N	\N	\N	0101000000B27B2F1469AB4040AEC85CD7777F4140	1996
CR3	328.654	Flag	CR3	CR3	\N	\N	\N	01010000000E2E13016BAB4040DADAA49D787F4140	1997
CR4	316.15699999999998	Flag	CR4	CR4	\N	\N	\N	0101000000DA10235156AB404015CD384A747F4140	1998
CR4A	316.15699999999998	Flag	CR4A	CR4A	\N	\N	\N	01010000008304BD1E69AB4040DB220355707F4140	1999
CR5	316.87799999999999	Flag	CR5	CR5	\N	\N	\N	01010000006E7FF46E68AB40407A770BF46E7F4140	2000
CR6	316.15699999999998	Flag	CR6	CR6	\N	\N	\N	0101000000189520986DAB4040F4EFF8CB6E7F4140	2001
CRV	185.178	Waypoint	CRV	CRV	\N	\N	\N	010100000083B5DE2D18C03940D55DD44C09884140	2002
CSF1	1005.9	Waypoint	CSF1	CSF1	\N	\N	\N	010100000095BDF6E3AE8839400E549887248C4140	2003
CSF10	1005.9	Waypoint	CSF10	CSF10	\N	\N	\N	01010000001B7547DB8588394006D48C26468C4140	2004
CSF-11	1051.3199999999999	Waypoint	CSF-11	CSF-11	\N	\N	\N	010100000093C1F638788839404F9D909B368C4140	2005
CSF12	1009.98	Waypoint	CSF12	CSF12	\N	\N	\N	0101000000A055C1035E88394012DB6BAE0B8C4140	2006
CSF13	1170.52	Waypoint	CSF13	CSF13	\N	\N	\N	0101000000787F381F09863940B9CEB7ECAD8C4140	2007
CSF14	1182.54	Waypoint	CSF14	CSF14	\N	\N	\N	010100000059AEF631188639409F8ABB44AC8C4140	2008
CSF15	1174.6099999999999	Waypoint	CSF15	CSF15	\N	\N	\N	0101000000F95C089A1886394033018C55AD8C4140	2009
CSF16	1171.96	Waypoint	CSF16	CSF16	\N	\N	\N	0101000000FE21DFA01B863940D06770F6AF8C4140	2010
CSF17	1184.46	Waypoint	CSF17	CSF17	\N	\N	\N	0101000000482B59760A8639403296A77BB68C4140	2011
CSF18	1175.8099999999999	Waypoint	CSF18	CSF18	\N	\N	\N	0101000000643117DC028639407CAE7CB7B58C4140	2012
CSF19	1189.75	Waypoint	CSF19	CSF19	\N	\N	\N	0101000000734C206913863940CEFC8B1CB98C4140	2013
CSF2	1175.8099999999999	Waypoint	CSF2	CSF2	\N	\N	\N	01010000006DCB8FDEC1883940250A41CF258C4140	2014
CSF20	1185.6600000000001	Waypoint	CSF20	CSF20	\N	\N	\N	0101000000BB61E05B068639408401A71EB98C4140	2015
CSF3	1053.24	Waypoint	CSF3	CSF3	\N	\N	\N	01010000004827BF038B88394019E4B7B3328C4140	2016
CSF4	1053.24	Waypoint	CSF4	CSF4	\N	\N	\N	0101000000DD3A793D8A88394010E08F03388C4140	2017
CSF5	1056.6099999999999	Waypoint	CSF5	CSF5	\N	\N	\N	0101000000F11E1EC479883940AAF200C0388C4140	2018
CSF6	1053	Waypoint	CSF6	CSF6	\N	\N	\N	0101000000F181277179883940CA9227B33B8C4140	2019
CSF7	1036.4200000000001	Waypoint	CSF7	CSF7	\N	\N	\N	01010000000BB20FF981883940E78167F93C8C4140	2020
CSF8	1015.51	Waypoint	CSF8	CSF8	\N	\N	\N	0101000000D9CC7F1A87883940C9ED3E974E8C4140	2021
CSF9	1062.6099999999999	Waypoint	CSF9	CSF9	\N	\N	\N	0101000000A496D92C868839406F03B34C438C4140	2022
CSF PARK	938.60599999999999	Waypoint	CSF PARK	CSF PARK	\N	\N	\N	01010000001373E8ABE68939407E864495F28B4140	2023
CTC100	1853.77	Waypoint	CTC100	CTC100	\N	\N	\N	01010000003C401BAC046E40402991C86747774140	2024
CTC101	1853.77	Waypoint	CTC101	CTC101	\N	\N	\N	01010000001C376CC3F76D40401469738046774140	2025
CTC102	1853.77	Waypoint	CTC102	CTC102	\N	\N	\N	0101000000EF0CED78E06D4040BC1FF4C256774140	2026
CTC130	1806.4300000000001	Waypoint	CTC130	CTC130	\N	\N	\N	0101000000FC0700AA126F404088014DAF4D784140	2027
CTC131	1777.8299999999999	Waypoint	CTC131	CTC131	\N	\N	\N	0101000000342F03A5136F4040770435514B784140	2028
CTC132	1777.8299999999999	Waypoint	CTC132	CTC132	\N	\N	\N	010100000026AF2432D97440401B6ABBD9C4844140	2029
CTC133	1792.49	Waypoint	CTC133	CTC133	\N	\N	\N	0101000000D0B53459116F4040590ABD0857784140	2030
CTC135	1816.28	Waypoint	CTC135	CTC135	\N	\N	\N	0101000000C99CF7970E6F4040B752F34848784140	2031
CTC136	1793.45	Waypoint	CTC136	CTC136	\N	\N	\N	010100000094F26CEC186F4040C8D8E0AF4F784140	2032
CTC137	1809.55	Waypoint	CTC137	CTC137	\N	\N	\N	01010000004BF007B5FF6E4040FDE23BF940784140	2033
CTC138	1810.03	Waypoint	CTC138	CTC138	\N	\N	\N	010100000050EC78FEFA6E4040AFCEC4834C784140	2034
CTC139	1825.9000000000001	Waypoint	CTC139	CTC139	\N	\N	\N	0101000000959CF725F96E4040E4FB8E5347784140	2035
CTC140	1793.45	Waypoint	CTC140	CTC140	\N	\N	\N	010100000021207B97F46E40400EDAB04841784140	2036
CTC141	1840.8	Waypoint	CTC141	CTC141	\N	\N	\N	0101000000D9CA371A016F404078F8B488FD774140	2037
CTC142	1828.0599999999999	Waypoint	CTC142	CTC142	\N	\N	\N	0101000000F7076CFCF96E40403DADD0771F784140	2038
CTC143	1842.24	Waypoint	CTC143	CTC143	\N	\N	\N	0101000000DE0F080C036F40402604E0DCF5774140	2039
CTC144	1822.77	Waypoint	CTC144	CTC144	\N	\N	\N	0101000000B3E0AB410B6F40404B17DCBB38784140	2040
CTC145	1830.46	Waypoint	CTC145	CTC145	\N	\N	\N	010100000057515BA10A6F4040C0688C5F03784140	2041
CTC146	1830.46	Waypoint	CTC146	CTC146	\N	\N	\N	01010000000DD66F8E096F4040F4C1DC35FB774140	2042
CTC147	1841.52	Waypoint	CTC147	CTC147	\N	\N	\N	0101000000ED729316036F40404216B36FF8774140	2043
CTC150	1725.2	Waypoint	CTC150	CTC150	\N	\N	\N	010100000016A65CD232704040E22B03CC11764140	2044
CTC-151	1717.75	Waypoint	CTC-151	CTC-151	\N	\N	\N	01010000003D03CC770E70404056F01874FB754140	2045
CTC152	1735.77	Waypoint	CTC152	CTC152	\N	\N	\N	0101000000AA411C1422704040F4760FC60B764140	2046
CTC153	1720.3900000000001	Waypoint	CTC153	CTC153	\N	\N	\N	010100000095FB106000704040230B899500764140	2047
CTC-154	1711.74	Waypoint	CTC-154	CTC-154	\N	\N	\N	01010000003AD548B5097040400B0FEBA4F6754140	2048
CTC155	1724.72	Waypoint	CTC155	CTC155	\N	\N	\N	01010000002EB2F8A81A704040BD92C8640F764140	2049
CTC156	1715.3399999999999	Waypoint	CTC156	CTC156	\N	\N	\N	0101000000478C8F30FB6F40404F2EAC6CF6754140	2050
CTC-157	1715.3399999999999	Waypoint	CTC-157	CTC-157	\N	\N	\N	010100000065C7672E077040400382E0C1F4754140	2051
CTC158	1761.97	Waypoint	CTC158	CTC158	\N	\N	\N	0101000000D52F182902704040744328F61D764140	2052
CTC159	1717.51	Waypoint	CTC159	CTC159	\N	\N	\N	01010000003FE2940FFA6F4040302B7CCCF3754140	2053
CTC-160	1708.6199999999999	Waypoint	CTC-160	CTC-160	\N	\N	\N	010100000096552753047040404A0C6FC7F5754140	2054
CTC161	1748.03	Waypoint	CTC161	CTC161	\N	\N	\N	010100000049F2BA5009704040B2FDEF7E18764140	2055
CTC162	1768.22	Waypoint	CTC162	CTC162	\N	\N	\N	0101000000E1CCCB3BF06F4040EC98B37116764140	2056
CTC-163	1702.6099999999999	Waypoint	CTC-163	CTC-163	\N	\N	\N	0101000000CA3DCC7A0B70404073CDA07EF0754140	2057
CTC165	1787.4400000000001	Waypoint	CTC165	CTC165	\N	\N	\N	01010000000BFC4BCCDF6F4040562E1C5417764140	2058
CTC-166	1705.97	Waypoint	CTC-166	CTC-166	\N	\N	\N	01010000008C29D76217704040EFBC9487F3754140	2059
CTC167	1748.03	Waypoint	CTC167	CTC167	\N	\N	\N	010100000035E8EBFCD26F4040C0EE167608764140	2060
CTC168	1806.4300000000001	Waypoint	CTC168	CTC168	\N	\N	\N	01010000003F7F448DAE6F40406DB51C220D764140	2061
CTC-169	1775.6700000000001	Waypoint	CTC-169	CTC-169	\N	\N	\N	0101000000181ABF58EE6F4040E1A667C31F764140	2062
CTC170	1775.4300000000001	Waypoint	CTC170	CTC170	\N	\N	\N	0101000000416F0B45E36F4040ED03984B0D764140	2063
CTC171	1848.01	Waypoint	CTC171	CTC171	\N	\N	\N	01010000005AC28BD2E66D404056CB386E3A774140	2064
CTC-172	1832.3800000000001	Waypoint	CTC-172	CTC-172	\N	\N	\N	0101000000C0DA9F2BD46D4040B0E38F073A774140	2065
CTC173	1775.4300000000001	Waypoint	CTC173	CTC173	\N	\N	\N	01010000007106D006D86D4040A7B19C084B774140	2066
CTC174	1870.3599999999999	Waypoint	CTC174	CTC174	\N	\N	\N	010100000090F5EB86E16D40402001857541774140	2067
CTC-175	1850.1700000000001	Waypoint	CTC-175	CTC-175	\N	\N	\N	0101000000FA809B20C56D40402BED3A3F38774140	2068
CTC176	1775.4300000000001	Waypoint	CTC176	CTC176	\N	\N	\N	01010000007106D006D86D4040A7B19C084B774140	2069
CTC177	1852.3299999999999	Waypoint	CTC177	CTC177	\N	\N	\N	01010000001F44DCE3DF6D4040105E830A43774140	2070
CTC-178	1841.04	Waypoint	CTC-178	CTC-178	\N	\N	\N	01010000006A0CDF67C16D404090C36F2938774140	2071
CTC179	1859.3	Waypoint	CTC179	CTC179	\N	\N	\N	0101000000E265A4E3CC6D4040AAEDB4A23E774140	2072
CTC180	1867.71	Waypoint	CTC180	CTC180	\N	\N	\N	0101000000292BF08FE36D4040BA6AD31542774140	2073
CTC-181	1837.9100000000001	Waypoint	CTC-181	CTC-181	\N	\N	\N	01010000006C5AD303B96D404050832F6D34774140	2074
CTC182	1859.54	Waypoint	CTC182	CTC182	\N	\N	\N	0101000000A0F6DF73B56D4040466E50723C774140	2075
CTC183	1870.3599999999999	Waypoint	CTC183	CTC183	\N	\N	\N	0101000000E9704CCDE06D4040C708645347774140	2076
CTC184	1848.97	Waypoint	CTC184	CTC184	\N	\N	\N	01010000001B1367E7226E40400771CC4F3D774140	2077
CTC-185	1851.3699999999999	Waypoint	CTC-185	CTC-185	\N	\N	\N	0101000000B4BBD0F4436E4040CCF5629A3C774140	2078
CTC186	1867.95	Waypoint	CTC186	CTC186	\N	\N	\N	0101000000B9CD7797DB6D4040F2E0CB9D49774140	2079
CTC2007 10	1689.6300000000001	Waypoint	CTC2007 10	CTC2007 10	\N	\N	\N	01010000003E0724CE7B724040F91D0781FC774140	2080
CTC2007 11	1684.8199999999999	Waypoint	CTC2007 11	CTC2007 11	\N	\N	\N	010100000040BEA05F7D724040F2A1B3E8F7774140	2081
CTC2007 12	1679.0599999999999	Waypoint	CTC2007 12	CTC2007 12	\N	\N	\N	0101000000085184F57B7240403B14E7A3EA774140	2082
CTC2007 9	1697.8	Waypoint	CTC2007 9	CTC2007 9	\N	\N	\N	010100000033EF0BC47F724040ABC6D37103784140	2083
CTC51	1583.8900000000001	Waypoint	CTC51	CTC51	\N	\N	\N	0101000000F53834AE7D71404089A980BD3A764140	2084
CTC52	1583.4100000000001	Waypoint	CTC52	CTC52	\N	\N	\N	0101000000CB7A5FCC7E714040AC88F85935764140	2085
CTC54	1583.8900000000001	Waypoint	CTC54	CTC54	\N	\N	\N	010100000008066BF746724040124504C0D1764140	2086
CTC56	1574.03	Waypoint	CTC56	CTC56	\N	\N	\N	0101000000EFC584896072404063228C3CDC764140	2089
CTC57	1549.04	Waypoint	CTC57	CTC57	\N	\N	\N	0101000000FDE8B0CF57724040B687E8D0DC764140	2090
CTC-58	1612.48	Waypoint	CTC-58	CTC-58	\N	\N	\N	0101000000BBD9BCFD3B724040DC64BCFCFA764140	2091
CTC59	1645.1700000000001	Waypoint	CTC59	CTC59	\N	\N	\N	01010000005DD6AB653E7240404ED16B951B774140	2092
CTC60 JDW	1642.77	Waypoint	CTC60 JDW	CTC60 JDW	\N	\N	\N	010100000061DB1C933772404042DF43AD1C774140	2093
CTC61	1563.9400000000001	Waypoint	CTC61	CTC61	\N	\N	\N	0101000000DCF3223146724040A7FDB33FC0764140	2094
CTC62	1564.1800000000001	Waypoint	CTC62	CTC62	\N	\N	\N	01010000000DDAB30050724040CCCA4FDAB6764140	2095
CTC63	1586.77	Waypoint	CTC63	CTC63	\N	\N	\N	0101000000C12A8B8052724040D7CE53C4B9764140	2096
CTC64	1589.8900000000001	Waypoint	CTC64	CTC64	\N	\N	\N	0101000000CE8E177C6172404004F252D5B7764140	2097
CTC65	1617.53	Waypoint	CTC65	CTC65	\N	\N	\N	01010000001FEFDE1C71724040E907A882A7764140	2098
CTC-67	423.58300000000003	Waypoint	CTC-67	CTC-67	\N	\N	\N	010100000015999041347140402B0C7836F7754140	2099
CTC-69	1594.9400000000001	Waypoint	CTC-69	CTC-69	\N	\N	\N	0101000000008DACDE5C714040A81EDBC503764140	2102
CTC70	1619.21	Waypoint	CTC70	CTC70	\N	\N	\N	0101000000D8A167A147714040D587F34502764140	2103
CTC71	1596.3800000000001	Waypoint	CTC71	CTC71	\N	\N	\N	010100000073164CB57C714040E0962F32F8754140	2104
CTC72	1619.21	Waypoint	CTC72	CTC72	\N	\N	\N	01010000004AF6DFB551714040518BFB6EFE754140	2105
CTC73	1603.1099999999999	Waypoint	CTC73	CTC73	\N	\N	\N	0101000000208E5CD47A714040AC376013F6754140	2106
CTC80	1842.24	Waypoint	CTC80	CTC80	\N	\N	\N	0101000000AF2B54A17B6F404024F73CCBE8764140	2107
CTC82	1850.8900000000001	Waypoint	CTC82	CTC82	\N	\N	\N	010100000081F00670716F40406D4C5C82E4764140	2110
CTC83	1831.9000000000001	Waypoint	CTC83	CTC83	\N	\N	\N	010100000047082C0C936F4040D2FF0A4AE5764140	2111
CTC84	1837.6700000000001	Waypoint	CTC84	CTC84	\N	\N	\N	0101000000A73264F3736F4040A732DB01D6764140	2112
CTC85	1839.5899999999999	Waypoint	CTC85	CTC85	\N	\N	\N	0101000000563F48AC8B6F404075E7FB3CD5764140	2113
CTC86	1856.4200000000001	Waypoint	CTC86	CTC86	\N	\N	\N	0101000000C4FC8E41646E404050C48F224C774140	2114
CTC87	1867.95	Waypoint	CTC87	CTC87	\N	\N	\N	0101000000CB1D84200A6E40401C3C97D248774140	2115
CTC88	1867.23	Waypoint	CTC88	CTC88	\N	\N	\N	01010000007FA060EF136E4040CCAAA70D49774140	2116
CTC89	1861.46	Waypoint	CTC89	CTC89	\N	\N	\N	0101000000921914720A6E4040BFB87F254A774140	2117
CTC90	1868.6700000000001	Waypoint	CTC90	CTC90	\N	\N	\N	01010000008C15C8FB176E4040A463CC324B774140	2118
CTC91	1869.4000000000001	Waypoint	CTC91	CTC91	\N	\N	\N	0101000000253FB7D70F6E40406DD99BC452774140	2119
CTC92	1862.4300000000001	Waypoint	CTC92	CTC92	\N	\N	\N	010100000026E21F49186E4040BD640C4148774140	2120
CTC93	1869.1500000000001	Waypoint	CTC93	CTC93	\N	\N	\N	0101000000DD3D0B05166E4040722748D45A774140	2121
CTC95	1885.02	Waypoint	CTC95	CTC95	\N	\N	\N	0101000000C83FBC792D6E404065F2635257774140	2122
CTC CO1	1726.8800000000001	Waypoint	CTC CO1	CTC CO1	\N	\N	\N	010100000082696C3D3070404046FFFEA60D764140	2123
CTC JEN	1638.4400000000001	Waypoint	CTC JEN	CTC JEN	\N	\N	\N	0101000000AA50D0327A7240404FFE936442774140	2124
CTM1	161.86600000000001	Waypoint	CTM1	CTM1	\N	\N	\N	010100000008E0067F36373A40CB348879509C4140	2125
CTM10	156.09899999999999	Waypoint	CTM10	CTM10	\N	\N	\N	01010000000A32115549373A409D89908A649C4140	2126
CTV42	1189.75	Waypoint	CTV42	CTV42	\N	\N	\N	01010000009A9CF3B6C65040407AAC073668844140	2127
CVP-1	1333.22	Waypoint	CVP-1	CVP-1	\N	\N	\N	010100000072AE9678161B3840100B61A7EBA04140	2128
CVP-2	1335.1500000000001	Waypoint	CVP-2	CVP-2	\N	\N	\N	0101000000387FB8501E1B3840272420C6EEA04140	2129
CVP PARK	1597.3399999999999	Waypoint	CVP PARK	CVP PARK	\N	\N	\N	01010000004474688E3516384060D5E41F39A24140	2130
CVPSTUMP	1334.6700000000001	Waypoint	CVPSTUMP	CVPSTUMP	\N	\N	\N	010100000074161E7D111B38404B3D78DEE7A04140	2131
D1	116.925	Waypoint	D1	D1	\N	\N	\N	0101000000BF9241745C5BEFBFF84A23DD44544A40	2132
D2	139.756	Waypoint	D2	D2	\N	\N	\N	0101000000AE9B4333FE36EFBF98C38B118B534A40	2133
D3	162.34700000000001	Waypoint	D3	D3	\N	\N	\N	0101000000A25932F72B55EEBF8250588E42534A40	2134
D4	159.22300000000001	Waypoint	D4	D4	\N	\N	\N	0101000000E4F41B44701EEEBF5C3B00CB12534A40	2135
D5	133.02699999999999	Waypoint	D5	D5	\N	\N	\N	0101000000807AD3F791D0EDBF21A64C8385544A40	2136
D69	152.25299999999999	Waypoint	D69	D69	\N	\N	\N	01010000005C564025D712EEBFA7EEFF20E8544A40	2137
D7	111.157	Waypoint	D7	D7	\N	\N	\N	0101000000084032D664F1EEBF1A9AFC60AA564A40	2138
DAT11A	262.56400000000002	Flag	DAT11A	DAT11A	\N	\N	\N	0101000000A57077B5E0B3404091EF9E78587D4140	2139
DELETED	42.4236	Waypoint	DELETED	DELETED	\N	\N	\N	010100000059CAB8FD402640409E0D0D4C6D834140	2140
DR1	219.30500000000001	Flag	DR1	DR1	\N	\N	\N	0101000000A840548455B2404042FDEF078A8A4140	2141
F1	117.886	Waypoint	F1	F1	\N	\N	\N	0101000000F9D9F3F5EFAB4040B67B5424E8604140	2142
F2	113.08	Waypoint	F2	F2	\N	\N	\N	0101000000A02FEB35E5AB404092FE080AD6604140	2143
F29	62.130499999999998	Waypoint	F29	F29	\N	\N	\N	0101000000A1DD4C455DA64040B159DFD4C8614140	2144
F3	115.24299999999999	Waypoint	F3	F3	\N	\N	\N	010100000085C95C32F1AB404060ED16EBD5604140	2145
F4	115.964	Waypoint	F4	F4	\N	\N	\N	01010000001EF4283900AC4040F580D739D6604140	2146
F5	119.569	Waypoint	F5	F5	\N	\N	\N	01010000007E6D3F11F2AB40404B063955E2604140	2147
F6	119.08799999999999	Waypoint	F6	F6	\N	\N	\N	0101000000B506CBE5FAAB40402B269C01E1604140	2148
GA04-3	1640.8399999999999	Waypoint	GA04-3	GA04-3	\N	\N	\N	0101000000E819775AE942404056C22C179E1B4240	2149
GARMIN	325.04899999999998	Waypoint	GARMIN	GARMIN	\N	\N	\N	01010000003581CE1623B357C013B87FA9826D4340	2150
GAZ04-1	1640.3599999999999	Waypoint	GAZ04-1	GAZ04-1	\N	\N	\N	0101000000B4A0F00CEC4240400D0D3DC2A61B4240	2151
GAZ04-18	1677.1300000000001	Waypoint	GAZ04-18	GAZ04-18	\N	\N	\N	0101000000B0422F7B104340409CF36AB8BC1B4240	2152
GAZ04-21	1675.45	Waypoint	GAZ04-21	GAZ04-21	\N	\N	\N	0101000000D0D633170743404013968FDCCE1B4240	2153
GAZ04-22	1673.29	Waypoint	GAZ04-22	GAZ04-22	\N	\N	\N	010100000087F29B48044340402533D79CD51B4240	2154
GAZ04-25	1677.8499999999999	Waypoint	GAZ04-25	GAZ04-25	\N	\N	\N	01010000003230F0B1FB42404009928BF2CB1B4240	2155
GAZ04-26	1680.98	Waypoint	GAZ04-26	GAZ04-26	\N	\N	\N	0101000000825934AC0843404022D9E46FC91B4240	2156
GAZ04-32	1650.7	Waypoint	GAZ04-32	GAZ04-32	\N	\N	\N	01010000003C4A8866DC424040307B744BF41B4240	2157
GAZ04-35	1628.8299999999999	Waypoint	GAZ04-35	GAZ04-35	\N	\N	\N	01010000001D02D5ACA4424040AEAAA352081C4240	2158
GAZ04-36	1628.8299999999999	Waypoint	GAZ04-36	GAZ04-36	\N	\N	\N	0101000000D22D5F8A77424040A96EAF7E0C1C4240	2159
GAZ04-37	1188.3099999999999	Waypoint	GAZ04-37	GAZ04-37	\N	\N	\N	0101000000230133775E404040CA50EF8B461C4240	2160
GAZ04-6	1623.54	Waypoint	GAZ04-6	GAZ04-6	\N	\N	\N	0101000000CF4C987FE0424040DAC95CF0941B4240	2161
GAZ04-8-17	1654.54	Waypoint	GAZ04-8-17	GAZ04-8-17	\N	\N	\N	01010000001408AD1A1343404028B3077B4F1B4240	2162
GAZKTFIRET	1680.5	Waypoint	GAZKTFIRET	GAZKTFIRET	\N	\N	\N	0101000000BC40905611434040BB6323A3921B4240	2163
GEORGOSTMB	99.381200000000007	Waypoint	GEORGOSTMB	GEORGOSTMB	\N	\N	\N	0101000000D8C3F7FB4EAC404081DF47B4EB604140	2164
GMARAES	81.356700000000004	Waypoint	GMARAES	GMARAES	\N	\N	\N	01010000007288972BACAC4040C3F82C7943604140	2165
GME1	148.88900000000001	Waypoint	GME1	GME1	\N	\N	\N	0101000000D448509EE79F41406A50B0D67B564040	2166
GOTTHARD	2039.3099999999999	Waypoint	GOTTHARD	GOTTHARD	\N	\N	\N	0101000000332C53876D222140397410FA2D474740	2167
GOTTHARD 1	410.60599999999999	Waypoint	GOTTHARD 1	GOTTHARD 1	\N	\N	\N	0101000000A3FB3CBD874B2140AB11437071684740	2168
GRIGGS	425.02499999999998	Waypoint	GRIGGS	GRIGGS	\N	\N	\N	01010000006585A615F42D53C0AE9FA042E5314540	2169
GRMEUR	35.934699999999999	Waypoint	GRMEUR	GRMEUR	\N	\N	\N	01010000001E909861226CF7BF229CA71ECF7D4940	2170
GRMPHX	361.09800000000001	Waypoint	GRMPHX	GRMPHX	\N	\N	\N	0101000000D0B1FD108DFC5BC022360CAA43AA4040	2171
GRMTWN	38.097700000000003	Waypoint	GRMTWN	GRMTWN	\N	\N	\N	01010000001E631221FA685E40183ACF08D10F3940	2172
GTOMB2	94.334500000000006	Waypoint	GTOMB2	GTOMB2	\N	\N	\N	01010000001781F8934DAC404023BAA4C0FB604140	2173
GZ04-2	1642.53	Waypoint	GZ04-2	GZ04-2	\N	\N	\N	010100000035294CB2EA424040F6B30743A51B4240	2174
H	1333.95	Waypoint	H	H	\N	\N	\N	0101000000F33570AC30574040617DE083477F4140	2175
HERMES	1682.1800000000001	Waypoint	HERMES	HERMES	\N	\N	\N	01010000001247DF23696D364021692B87BAF74240	2176
HIN 12	1587.01	Waypoint	HIN 12	HIN 12	\N	\N	\N	010100000098108073173D42400E553396697D4240	2177
HIN 13AKCM	1569.47	Waypoint	HIN 13AKCM	HIN 13AKCM	\N	\N	\N	0101000000DE5DE8C3133D4240D40788055B7D4240	2178
HIN 14	1587.97	Waypoint	HIN 14	HIN 14	\N	\N	\N	0101000000C49F8815133D42407E6613D7667D4240	2179
HIN 15	1618.73	Waypoint	HIN 15	HIN 15	\N	\N	\N	0101000000584B2076BD3F4240F985C42EAB7C4240	2180
HIN 16	1618.01	Waypoint	HIN 16	HIN 16	\N	\N	\N	0101000000C39DA8F8C13F4240FF988FD1A57C4240	2181
HIN 17	1633.3900000000001	Waypoint	HIN 17	HIN 17	\N	\N	\N	01010000007B329705A73F4240571B7051BE7C4240	2182
HIN 18	1642.29	Waypoint	HIN 18	HIN 18	\N	\N	\N	01010000002A1E681CA23F4240B70CA5E7B77C4240	2183
HIN 19	1610.0799999999999	Waypoint	HIN 19	HIN 19	\N	\N	\N	0101000000B8FCBAE5C03F424076F7F66AA47C4240	2184
HIN 2	1418.54	Waypoint	HIN 2	HIN 2	\N	\N	\N	010100000024A0AB75AE3B4240D7C43C0A1B7D4240	2185
HIN 20	1612.97	Waypoint	HIN 20	HIN 20	\N	\N	\N	01010000005EE7C021BD3F424016ACABE1A67C4240	2186
HIN 3	1432	Waypoint	HIN 3	HIN 3	\N	\N	\N	0101000000BFE743F4C43B4240D94F575B177D4240	2187
HIN 4	1688.9100000000001	Waypoint	HIN 4	HIN 4	\N	\N	\N	0101000000E1FB18FCC83D4240596B07A7EE7C4240	2188
HIN 5	1678.8199999999999	Waypoint	HIN 5	HIN 5	\N	\N	\N	0101000000346FF3CBC23D4240ADD39C4AF37C4240	2189
HIN 6	1654.54	Waypoint	HIN 6	HIN 6	\N	\N	\N	0101000000B02E9CDBEC3D424047127D71FB7C4240	2190
HIN 7	1700.6900000000001	Waypoint	HIN 7	HIN 7	\N	\N	\N	01010000002D8B3B3E173E424052FFC890BD7C4240	2191
HIN 8	1706.21	Waypoint	HIN 8	HIN 8	\N	\N	\N	01010000005994A7E6113E42406B203F16C07C4240	2192
HIN 91011	1750.1900000000001	Waypoint	HIN 91011	HIN 91011	\N	\N	\N	0101000000EA5B58CA413E4240B62E5B09A27C4240	2193
HINZIRLI 1	1405.0799999999999	Waypoint	HINZIRLI 1	HINZIRLI 1	\N	\N	\N	01010000002A62187DB33B42400C656CDE347D4240	2194
IBO KURTKE	45.788200000000003	Waypoint	IBO KURTKE	IBO KURTKE	\N	\N	\N	010100000071DAB95418EB3C4018A0AC99EE984440	2195
IHA-1	101.544	Waypoint	IHA-1	IHA-1	\N	\N	\N	0101000000022AAFA4FB8E4140141261AAF6394040	2196
IHA-2	85.442300000000003	Waypoint	IHA-2	IHA-2	\N	\N	\N	01010000002E17BCAB888541403A078D68543D4040	2197
IIIIII	-7.0839800000000004	Waypoint	IIIIII	IIIIII	\N	\N	\N	01010000009F25B6256C8F3740230B08156BA24140	2198
IZM BURTHS	3.7308300000000001	Waypoint	IZM BURTHS	IZM BURTHS	\N	\N	\N	01010000003D9FBFD894C63A40A7FB1FB1742E4340	2199
J1	126.538	Waypoint	J1	J1	\N	\N	\N	01010000007D513EE1455FEFBFC8C51B1056544A40	2200
J10	13.1036	Waypoint	J10	J10	\N	\N	\N	0101000000C4F3036B5D0B2D407CDAA44CB1F14140	2201
J11	31.608899999999998	Waypoint	J11	J11	\N	\N	\N	01010000005D230D64F90B2D40ACBE34F140F14140	2202
J12	31.608899999999998	Waypoint	J12	J12	\N	\N	\N	01010000002A7C8D8FDC042D40A06FEF40BBF24140	2203
J13	55.401200000000003	Waypoint	J13	J13	\N	\N	\N	01010000009BBC5E205C062D404273170D8BF24140	2204
J14	50.835099999999997	Waypoint	J14	J14	\N	\N	\N	0101000000BDAF8D22B2052D4037B94FF6A5F24140	2205
J15	21.034400000000002	Waypoint	J15	J15	\N	\N	\N	0101000000A66F6C52DE052D4074A2E3BEA3F24140	2206
J16	36.174999999999997	Waypoint	J16	J16	\N	\N	\N	01010000009766C248920B2D405560E7F794F14140	2207
J2	54.920499999999997	Waypoint	J2	J2	\N	\N	\N	01010000001CA7029D32042D40FC553461B1F24140	2208
J3	60.4482	Waypoint	J3	J3	\N	\N	\N	01010000001C8990ED8E032D40513E44704AEF4140	2209
J4	25.840900000000001	Waypoint	J4	J4	\N	\N	\N	010100000023D36CE123042D404E70B45A95EF4140	2210
J5	24.1586	Waypoint	J5	J5	\N	\N	\N	0101000000593673486A0B2D4053D4EF7B81F14140	2211
J6	24.1586	Waypoint	J6	J6	\N	\N	\N	01010000004E4504C58A0B2D40F6C15FB77BF14140	2212
J7	16.948899999999998	Waypoint	J7	J7	\N	\N	\N	0101000000BD398CE4530B2D40C272C829A1F14140	2213
J8	15.747199999999999	Waypoint	J8	J8	\N	\N	\N	0101000000B1EE3E188F0A2D40C904F761CFF14140	2214
J9	14.545500000000001	Waypoint	J9	J9	\N	\N	\N	01010000002839E3D8D30A2D40680ED9D896F14140	2215
JKS-1	148.88900000000001	Waypoint	JKS-1	JKS-1	\N	\N	\N	010100000030373BF9849D414057829C629B554040	2216
JKS2	125.81699999999999	Waypoint	JKS2	JKS2	\N	\N	\N	0101000000090051DA629D414013F58CC1A6554040	2217
JKS3	149.12899999999999	Waypoint	JKS3	JKS3	\N	\N	\N	01010000002BF723C16B9D41402557F314A0554040	2218
JUNIP	1690.1099999999999	Waypoint	JUNIP	JUNIP	\N	\N	\N	0101000000FE0744C78F724040BC4A0783DC774140	2219
K2	316.63799999999998	Flag	K2	K2	\N	\N	\N	01010000000F7408AA73AB4040BAD05F5FA07F4140	2220
K2-1	315.67599999999999	Flag	K2-1	K2-1	\N	\N	\N	0101000000B946783974AB4040BA0707CC9A7F4140	2221
K2-2	313.99400000000003	Flag	K2-2	K2-2	\N	\N	\N	0101000000EF767B1873AB40405DB39798A77F4140	2222
K2-3	307.26499999999999	Flag	K2-3	K2-3	\N	\N	\N	0101000000DA82876466AB404067FDB4F1AA7F4140	2223
K2-4	312.79199999999997	Flag	K2-4	K2-4	\N	\N	\N	01010000006652A38D6CAB40405B307C3A9A7F4140	2224
K2-5	314.95499999999998	Flag	K2-5	K2-5	\N	\N	\N	010100000027410B4B74AB404010F2DAA2957F4140	2225
K3	314.23399999999998	Flag	K3	K3	\N	\N	\N	01010000009C2E0FC271AB4040ADF41217CB7F4140	2226
K3-1	311.11000000000001	Flag	K3-1	K3-1	\N	\N	\N	0101000000124D179767AB4040696E1814AC7F4140	2227
K3-2	312.79199999999997	Flag	K3-2	K3-2	\N	\N	\N	0101000000A22A800B6DAB4040A58CF764B77F4140	2228
K3-3	314.95499999999998	Flag	K3-3	K3-3	\N	\N	\N	0101000000E415046670AB40402B78F064CE7F4140	2229
K3-4	315.67599999999999	Flag	K3-4	K3-4	\N	\N	\N	0101000000ABBDFC0C77AB404031B18763CE7F4140	2230
K3-5	304.14100000000002	Flag	K3-5	K3-5	\N	\N	\N	0101000000D059944975AB404016F1CBD0E07F4140	2231
K3-6	297.65199999999999	Flag	K3-6	K3-6	\N	\N	\N	0101000000038D18C467AB404098417F18D67F4140	2232
K3-7	305.58300000000003	Flag	K3-7	K3-7	\N	\N	\N	01010000009A08672C64AB4040E9BA3759BF7F4140	2233
K3-8	305.58300000000003	Flag	K3-8	K3-8	\N	\N	\N	0101000000D004207464AB40406A6BBB3EB07F4140	2234
K4	326.49099999999999	Flag	K4	K4	\N	\N	\N	0101000000525EAFD95FAB404092F2D2CE627F4140	2235
K5	339.46899999999999	Flag	K5	K5	\N	\N	\N	010100000004B02C91FEAB4040665FA7C8497F4140	2236
K6	331.29700000000003	Flag	K6	K6	\N	\N	\N	01010000007907A3521FAC4040AB178354787F4140	2237
KAH BIGTRE	1058.29	Waypoint	KAH BIGTRE	KAH BIGTRE	\N	\N	\N	0101000000C5F0F223A7E44040D278C41672AE4340	2238
KAH MEWSAM	1058.53	Waypoint	KAH MEWSAM	KAH MEWSAM	\N	\N	\N	01010000000C93CF49B4E44040D259542E8EAE4340	2239
KALKIM HQ	280.58800000000002	Waypoint	KALKIM HQ	KALKIM HQ	\N	\N	\N	010100000082317650F6343B4032650849A4E74340	2240
KAM1	467.56299999999999	Flag	KAM1	KAM1	\N	\N	\N	01010000002655C4B1BAA04040F093ECD6C7814140	2241
KAM2	432.476	Flag	KAM2	KAM2	\N	\N	\N	0101000000BFC15B4390A14040FE88A3CE43834140	2242
KAMAN KALE	1052.52	Waypoint	KAMAN KALE	KAMAN KALE	\N	\N	\N	0101000000730B907AA5E44040C3C720AA82AE4340	2243
KASTORIA L	644.68499999999995	Waypoint	KASTORIA L	KASTORIA L	\N	\N	\N	01010000002522270CFD4035409BD234078C424440	2244
KAT 2005	1680.5	Waypoint	KAT 2005	KAT 2005	\N	\N	\N	0101000000EA4F9E56AA3A354031FDA80CF7E54340	2245
KATALION	417.09500000000003	Waypoint	KATALION	KATALION	\N	\N	\N	0101000000078E1FB8F7A640405B3F232FF07D4140	2246
KATARA	1672.5699999999999	Waypoint	KATARA	KATARA	\N	\N	\N	010100000001711F0544363540DBEBDF209AE74340	2247
KAT EAST	1111.8800000000001	Waypoint	KAT EAST	KAT EAST	\N	\N	\N	01010000008771D916553D35404CEF8FB54EEE4340	2248
KECIKALE	424.30399999999997	Waypoint	KECIKALE	KECIKALE	\N	\N	\N	0101000000BE69F07A0D6C3B40DF2433021D034340	2249
KELID1	390.178	Flag	KELID1	KELID1	\N	\N	\N	01010000007D2C9B3E1D9D4040E638A8DE89834140	2250
KELID2	381.52600000000001	Flag	KELID2	KELID2	\N	\N	\N	0101000000E40B88802B9D40409425634A9E834140	2251
KERKENES	1419.02	Waypoint	KERKENES	KERKENES	\N	\N	\N	010100000081D4AC877D8841402565EB160ADF4340	2252
KILLINI JN	1692.51	Waypoint	KILLINI JN	KILLINI JN	\N	\N	\N	0101000000A5FDE177EB693640C7E12C2213F54240	2253
KK1	410.846	Flag	KK1	KK1	\N	\N	\N	0101000000C7BF67D5FBA640409C16B72FF27D4140	2254
KK2	407.72199999999998	Flag	KK2	KK2	\N	\N	\N	010100000027CB2FD4F8A64040BFF52ECCEC7D4140	2255
KK3	403.87700000000001	Flag	KK3	KK3	\N	\N	\N	01010000006DFE04BCF1A640401F45B455FD7D4140	2256
KK4	396.90699999999998	Flag	KK4	KK4	\N	\N	\N	0101000000228F3B00FDA64040364D98630B7E4140	2257
KK5	405.79899999999998	Flag	KK5	KK5	\N	\N	\N	0101000000BD53945A0EA74040E82330A3067E4140	2258
KK6	407.00099999999998	Flag	KK6	KK6	\N	\N	\N	0101000000A252781209A7404035463CF0E97D4140	2259
KKA1	381.76600000000002	Flag	KKA1	KKA1	\N	\N	\N	01010000004902F89A6AA64040B23D0B26E47D4140	2260
KKA2	391.86000000000001	Flag	KKA2	KKA2	\N	\N	\N	010100000098AE9F7F74A640404D018755DE7D4140	2261
KKK1	324.80900000000003	Flag	KKK1	KKK1	\N	\N	\N	01010000004CE728DF6AAA40405B59FB6B14814140	2262
KL1	370.471	Flag	KL1	KL1	\N	\N	\N	01010000003C93339D68A6404027181F7D4B7E4140	2263
KLK SARIOT	938.36500000000001	Waypoint	KLK SARIOT	KLK SARIOT	\N	\N	\N	01010000006DD4875137013B40B3D8F8DC21E64340	2264
KMK	1663.9200000000001	Waypoint	KMK	KMK	\N	\N	\N	0101000000B6132F9384964240C1FADA5BD3E14240	2265
KOTYLI	1394.51	Waypoint	KOTYLI	KOTYLI	\N	\N	\N	010100000024C6EEDF15053540F096CFE5922A4440	2266
KR1	368.06799999999998	Flag	KR1	KR1	\N	\N	\N	0101000000838603C327A64040269C27E8407F4140	2267
KSK NW GAT	1580.76	Waypoint	KSK NW GAT	KSK NW GAT	\N	\N	\N	01010000006710294A257442405C4C57A3A3A74340	2268
KSK TEMPLE	1624.02	Waypoint	KSK TEMPLE	KSK TEMPLE	\N	\N	\N	010100000067602B3B99744240E5F02819A9A74340	2269
KYL-7 NVR	1823.97	Waypoint	KYL-7 NVR	KYL-7 NVR	\N	\N	\N	01010000009BF707E38A643640FB1CC4731BF44240	2270
KYLLINISW	1693.48	Waypoint	KYLLINISW	KYLLINISW	\N	\N	\N	0101000000BD0A68A3EC6936402114172416F54240	2271
LAVAGNONE	108.273	Waypoint	LAVAGNONE	LAVAGNONE	\N	\N	\N	010100000092822DCAFA13254090DAC4C9FDB74640	2272
LIMANTEPE	60.928800000000003	Waypoint	LIMANTEPE	LIMANTEPE	\N	\N	\N	0101000000AC3F183199C63A4076AF3FFB642E4340	2273
LINA MINE	462.75700000000001	Waypoint	LINA MINE	LINA MINE	\N	\N	\N	010100000054D25835A78D40409F4FE70161854140	2274
LNIN2	431.03399999999999	Waypoint	LNIN2	LNIN2	\N	\N	\N	01010000008E058088DF8E4040F935ABA1C1844140	2275
LYP1	202.001	Flag	LYP1	LYP1	\N	\N	\N	010100000021E3FB2968BB404067CB7F694D7B4140	2276
LYP10	225.07300000000001	Flag	LYP10	LYP10	\N	\N	\N	0101000000B93103B63FB94040F456FF15697E4140	2277
LYP11	231.56200000000001	Flag	LYP11	LYP11	\N	\N	\N	0101000000A187936140B9404060EAE74D857E4140	2278
LYP12	230.36000000000001	Flag	LYP12	LYP12	\N	\N	\N	0101000000A142571D57B94040423C08BD9B7E4140	2279
LYP13	232.28200000000001	Flag	LYP13	LYP13	\N	\N	\N	0101000000E8DC345075B94040956D031EA87E4140	2280
LYP14	240.21299999999999	Flag	LYP14	LYP14	\N	\N	\N	0101000000E062A837A1B94040FB03B57DA77E4140	2281
LYP15	244.059	Flag	LYP15	LYP15	\N	\N	\N	01010000005D7EE7E4B3B94040B7FAAA8FD97E4140	2282
LYP16	248.14400000000001	Flag	LYP16	LYP16	\N	\N	\N	010100000029720CF1DDB94040670C4802DA7E4140	2283
LYP17	240.934	Flag	LYP17	LYP17	\N	\N	\N	01010000006FF4BFC9E7B940402D7A602DE27E4140	2284
LYP18	230.36000000000001	Flag	LYP18	LYP18	\N	\N	\N	010100000075CE635503BA4040B8E9CF7EE47E4140	2285
LYP2	229.87899999999999	Flag	LYP2	LYP2	\N	\N	\N	01010000007F24A30513BA4040F294842DBE7E4140	2286
LYP3	233.48400000000001	Flag	LYP3	LYP3	\N	\N	\N	0101000000EA35EF98F7B94040439CB4949F7E4140	2287
LYP4	235.40700000000001	Flag	LYP4	LYP4	\N	\N	\N	0101000000AA2034F2E1B9404000E9B4D1AD7E4140	2288
LYP5	229.87899999999999	Flag	LYP5	LYP5	\N	\N	\N	0101000000463AA3B0C2B94040AC23AC65A97E4140	2289
LYP6	232.76300000000001	Flag	LYP6	LYP6	\N	\N	\N	0101000000A5CD8753A5B940400696308EA27E4140	2290
LYP7	226.51499999999999	Flag	LYP7	LYP7	\N	\N	\N	010100000017842CEA97B94040EF93EF8E967E4140	2291
LYP8	227.23599999999999	Flag	LYP8	LYP8	\N	\N	\N	0101000000E70481565FB9404092815FCA907E4140	2292
LYP9	223.631	Flag	LYP9	LYP9	\N	\N	\N	0101000000085C24DB63B94040BD526BA1717E4140	2293
MA1	397.14699999999999	Flag	MA1	MA1	\N	\N	\N	01010000007BF6F4C271A8404012C05B8D1D7E4140	2294
METSOVO	1172.6900000000001	Waypoint	METSOVO	METSOVO	\N	\N	\N	0101000000C5CF6873682E3540CD5F0F9069E24340	2295
MF1	133.02699999999999	Waypoint	MF1	MF1	\N	\N	\N	0101000000E7106191D9AB404074BDF8980B614140	2296
MG1	352.68700000000001	Flag	MG1	MG1	\N	\N	\N	01010000007FF95F0CF7A94040A1596F5A84814140	2297
MMT	705.96900000000005	Waypoint	MMT	MMT	\N	\N	\N	01010000007531D02C3EFF3F406C627485A6D34340	2298
M-P1	325.28899999999999	Flag	M-P1	M-P1	\N	\N	\N	0101000000FFB3CF2B2AAB4040C8CC68580A7E4140	2299
MTKEV	420.21899999999999	Waypoint	MTKEV	MTKEV	\N	\N	\N	010100000004DAFFFBF1A640404C5F7C8F827E4140	2300
M-VS1	325.28899999999999	Flag	M-VS1	M-VS1	\N	\N	\N	01010000005C50A0CEE3AB40401D02D5ACA47D4140	2301
M-VS10	321.68400000000003	Flag	M-VS10	M-VS10	\N	\N	\N	01010000009FD4B0427EAB4040F0BAF3CE257E4140	2302
M-VS11	320.00200000000001	Flag	M-VS11	M-VS11	\N	\N	\N	010100000064506B6F77AB404019FF64AA257E4140	2303
M-VS12	327.21199999999999	Flag	M-VS12	M-VS12	\N	\N	\N	0101000000510CDFAE96AB40400B185FDFC77D4140	2304
M-VS2	358.935	Flag	M-VS2	M-VS2	\N	\N	\N	0101000000950EF70DE3AB40407902F40AEA7D4140	2305
M-VS3	364.22300000000001	Flag	M-VS3	M-VS3	\N	\N	\N	0101000000D1CB3F32D1AB4040C25E93FBFE7D4140	2306
M-VS4	360.61700000000002	Flag	M-VS4	M-VS4	\N	\N	\N	0101000000622D43BEB9AB4040B481C7F9537E4140	2307
M-VS5	353.40800000000002	Flag	M-VS5	M-VS5	\N	\N	\N	010100000042187FA8A2AB4040C1B02C334D7E4140	2308
M-VS6	352.44600000000003	Flag	M-VS6	M-VS6	\N	\N	\N	01010000002F83944CCCAB4040F0C38817E07D4140	2309
M-VS7	334.18200000000002	Flag	M-VS7	M-VS7	\N	\N	\N	01010000006E5170F5BFAB4040EBB39812A87D4140	2310
M-VS8	334.42200000000003	Flag	M-VS8	M-VS8	\N	\N	\N	01010000002F2E2077BBAB4040EC6E5CCEBE7D4140	2311
M-VS9	329.13499999999999	Flag	M-VS9	M-VS9	\N	\N	\N	0101000000C80DD37F95AB40403284875CF57D4140	2312
MYC GCA	247.904	Waypoint	MYC GCA	MYC GCA	\N	\N	\N	0101000000BEE1E5B9A3C13640A6593BB27BDD4240	2313
NEMRUD ET	2144.5700000000002	Waypoint	NEMRUD ET	NEMRUD ET	\N	\N	\N	010100000014F17888F65E434075800C8F90FD4240	2314
NEMRUD WT	2159.71	Waypoint	NEMRUD WT	NEMRUD WT	\N	\N	\N	010100000000FE038EBF5E4340A00E380675FD4240	2315
OAKS	871.31399999999996	Waypoint	OAKS	OAKS	\N	\N	\N	0101000000CB7B7E13E8E338405063D32E0CA34140	2316
OKV1	260.88099999999997	Waypoint	OKV1	OKV1	\N	\N	\N	0101000000541D30178AAB3940E9DB546D0F874140	2317
OKV2	259.68000000000001	Waypoint	OKV2	OKV2	\N	\N	\N	0101000000690848AD90AB39407E32D86A15874140	2318
OKV3	259.68000000000001	Waypoint	OKV3	OKV3	\N	\N	\N	01010000006F41DFAB90AB394004C0807715874140	2319
OKV4	258.959	Waypoint	OKV4	OKV4	\N	\N	\N	0101000000CD47295392AB39407839C4F615874140	2320
OLTU	2142.8899999999999	Waypoint	OLTU	OLTU	\N	\N	\N	01010000005FB8781A9EF64440AEC5A44809504440	2321
ORT A	792.00599999999997	Waypoint	ORT A	ORT A	\N	\N	\N	0101000000B19A28EE2A9E41403AF114F3A5204440	2322
ORT B	784.55600000000004	Waypoint	ORT B	ORT B	\N	\N	\N	0101000000E38A5B6A5C9E4140226BB7A47D204440	2323
ORT C	780.71000000000004	Waypoint	ORT C	ORT C	\N	\N	\N	0101000000BE4338036F9E41402D6B982A63204440	2324
ORT D	782.63300000000004	Waypoint	ORT D	ORT D	\N	\N	\N	01010000005882182A779E4140C266044456204440	2325
ORTTEMENOS	786.47799999999995	Waypoint	ORTTEMENOS	ORTTEMENOS	\N	\N	\N	01010000009B228386459E4140862F9FEEB4204440	2326
PAI1	404.59800000000001	Flag	PAI1	PAI1	\N	\N	\N	01010000000D59D0C0379F40403FC63FE0FF824140	2327
PAI2	399.06999999999999	Flag	PAI2	PAI2	\N	\N	\N	0101000000E07BD382449F404040FA7C4DFC824140	2328
PAI3	423.82400000000001	Flag	PAI3	PAI3	\N	\N	\N	010100000024C327267A9F404057C5230A10834140	2329
PALEO KOTY	1635.8	Waypoint	PALEO KOTY	PALEO KOTY	\N	\N	\N	01010000007ED91F237EFC344089F4D366392C4440	2330
PAM1	384.89100000000002	Flag	PAM1	PAM1	\N	\N	\N	0101000000CA5FEF38649E4040E9F9C61CB3834140	2331
PATERMA KO	421.90100000000001	Waypoint	PATERMA KO	PATERMA KO	\N	\N	\N	0101000000C5FFD67B157D3940BBCC50DF699D4440	2332
P-AY1	314.95499999999998	Flag	P-AY1	P-AY1	\N	\N	\N	0101000000B06A5F2C4BAE404008176CCF5E814140	2333
P-AY10	298.613	Flag	P-AY10	P-AY10	\N	\N	\N	0101000000ADEAA0A339AE4040912E8CAD8D814140	2334
P-AY11	300.536	Flag	P-AY11	P-AY11	\N	\N	\N	0101000000D7FB76DA66AE404059A6DBE979814140	2335
P-AY12	311.83100000000002	Flag	P-AY12	P-AY12	\N	\N	\N	01010000006BE4CFC46FAE4040674824F058814140	2336
P-AY13	304.62099999999998	Flag	P-AY13	P-AY13	\N	\N	\N	0101000000B67FE42474AE4040349D985354814140	2337
P-AY14	299.815	Flag	P-AY14	P-AY14	\N	\N	\N	010100000083DAD7D079AE4040D43ADCD461814140	2338
P-AY15	318.07999999999998	Flag	P-AY15	P-AY15	\N	\N	\N	0101000000FE8FA48832AE404023349B2A4C814140	2339
P-AY16	324.32799999999997	Flag	P-AY16	P-AY16	\N	\N	\N	01010000007F5F985607AE4040FAD4AC912F814140	2340
P-AY17	333.70100000000002	Flag	P-AY17	P-AY17	\N	\N	\N	01010000006F4233E509AE40406109B0370D814140	2341
P-AY3	309.90800000000002	Flag	P-AY3	P-AY3	\N	\N	\N	01010000004BA03F6F55AE40408321F43174814140	2342
P-AY4	307.024	Flag	P-AY4	P-AY4	\N	\N	\N	0101000000EBB167AE57AE404063138C8F7E814140	2343
P-AY5	308.226	Flag	P-AY5	P-AY5	\N	\N	\N	0101000000671BBFBD59AE4040EBA68F1E91814140	2344
P-AY6	313.75400000000002	Flag	P-AY6	P-AY6	\N	\N	\N	010100000040456BCB54AE40408B5F202465814140	2345
P-AY7	292.36500000000001	Flag	P-AY7	P-AY7	\N	\N	\N	0101000000F42098FE40AE40408CA05C1385814140	2346
P-AY8	298.37299999999999	Flag	P-AY8	P-AY8	\N	\N	\N	0101000000030DF91146AE40406CBA407779814140	2347
P-AY9	301.97800000000001	Flag	P-AY9	P-AY9	\N	\N	\N	010100000022C490FA3DAE4040A10A916973814140	2348
PELLA	44.586500000000001	Waypoint	PELLA	PELLA	\N	\N	\N	010100000059AC464EDD843640A4F6E3758B604440	2349
PFR	3.7308300000000001	Waypoint	PFR	PFR	\N	\N	\N	0101000000B686316D089251C0D7122FA5E7D24540	2350
PHERRAI	102.986	Waypoint	PHERRAI	PHERRAI	\N	\N	\N	0101000000F8A7C6DE9A2B3A409A43A35264724440	2351
PINETREE1	264.48599999999999	Waypoint	PINETREE1	PINETREE1	\N	\N	\N	01010000008C5B1B16E2AD4040F0ECA41E5F634140	2352
PL1	283.95299999999997	Waypoint	PL1	PL1	\N	\N	\N	01010000003D32BBC6E5B340408CB940895C7D4140	2353
PL2	282.99200000000002	Waypoint	PL2	PL2	\N	\N	\N	0101000000FB6C03C8E7B3404028F4C2E9597D4140	2354
PL3	281.55000000000001	Waypoint	PL3	PL3	\N	\N	\N	01010000002E357FA2EBB3404091EF9E78587D4140	2355
PL4	283.95299999999997	Waypoint	PL4	PL4	\N	\N	\N	010100000064C88B2EEAB3404096F748B5557D4140	2356
PL5	281.06900000000002	Waypoint	PL5	PL5	\N	\N	\N	01010000005231E77CE5B3404013617CFE5F7D4140	2357
PM1	295.72899999999998	Waypoint	PM1	PM1	\N	\N	\N	010100000053BFAFEA1CB040402D7DC77411824140	2358
PM2	296.93099999999998	Waypoint	PM2	PM2	\N	\N	\N	0101000000AEE8D46A0FB0404014DC333E18824140	2359
PM3	289.96100000000001	Waypoint	PM3	PM3	\N	\N	\N	0101000000B233486B13B040404813DB1C28824140	2360
PM4	290.202	Waypoint	PM4	PM4	\N	\N	\N	01010000008559843C24B0404006A5F3D727824140	2361
PM5	292.60500000000002	Waypoint	PM5	PM5	\N	\N	\N	01010000008A7888D220B04040EA6378CB19824140	2362
PM6	396.42599999999999	Flag	PM6	PM6	\N	\N	\N	0101000000CC36A7FA649C404054ABB46704834140	2363
PM6B	398.10899999999998	Flag	PM6B	PM6B	\N	\N	\N	01010000001CB05F3B4E9C4040984A7C4405834140	2364
PM6B3	398.589	Flag	PM6B3	PM6B3	\N	\N	\N	010100000076EB3CF74C9C4040A5FC20A203834140	2365
PM6B4	393.06200000000001	Flag	PM6B4	PM6B4	\N	\N	\N	0101000000D74763913F9C4040C8A1B8C2FF824140	2366
PM6B5	392.34100000000001	Flag	PM6B5	PM6B5	\N	\N	\N	0101000000E2746CE64B9C404089C124DEFF824140	2367
PMB1	259.68000000000001	Flag	PMB1	PMB1	\N	\N	\N	010100000039002C3CB9AF404051B433CB6C824140	2368
PMB2	260.64100000000002	Flag	PMB2	PMB2	\N	\N	\N	01010000009CF83A0E9BAF4040EEF1A76522824140	2369
PMB3	270.495	Flag	PMB3	PMB3	\N	\N	\N	01010000006C0E400528AF4040501C3B0676814140	2370
PMB4	269.53300000000002	Flag	PMB4	PMB4	\N	\N	\N	01010000008327DB5D33AF4040A2E1673872814140	2371
PMB5-	256.315	Flag	PMB5-	PMB5-	\N	\N	\N	01010000001FC5C4DC71AF4040E7C8B3938D824140	2372
PMB6	259.92000000000002	Flag	PMB6	PMB6	\N	\N	\N	0101000000B3CAAF9338AF4040DE040F6E93824140	2373
PMD11	499.04599999999999	Flag	PMD11	PMD11	\N	\N	\N	0101000000ABFE78173E9A40401AB348BAB6804140	2374
PMD1A1	516.35000000000002	Flag	PMD1A1	PMD1A1	\N	\N	\N	0101000000A202505A329A4040B2B66B3AA9804140	2375
PMD1A2	497.36399999999998	Flag	PMD1A2	PMD1A2	\N	\N	\N	0101000000727090E06B9A40401148F4F89E804140	2376
PMD1A3	508.89999999999998	Flag	PMD1A3	PMD1A3	\N	\N	\N	01010000004FC0C0BD7C9A4040EF30D4FE9A804140	2377
PMD1A4	501.93000000000001	Flag	PMD1A4	PMD1A4	\N	\N	\N	01010000001C6DEF2F8F9A40403ADDC36F94804140	2378
PMD1A5	492.077	Flag	PMD1A5	PMD1A5	\N	\N	\N	010100000085E30BF0A39A404078C0B42990804140	2379
PMD1B1	496.64299999999997	Flag	PMD1B1	PMD1B1	\N	\N	\N	01010000009E0C512F3F9A404053B45B93BA804140	2380
PMD1B2	490.154	Flag	PMD1B2	PMD1B2	\N	\N	\N	01010000008D37CCAC729A40407ECFA33DB9804140	2381
PMD1B3	497.84500000000003	Flag	PMD1B3	PMD1B3	\N	\N	\N	0101000000496D7FBA839A40406B7E18FBB4804140	2382
PMD1B4	486.54899999999998	Flag	PMD1B4	PMD1B4	\N	\N	\N	0101000000EDDE0B459A9A40405CAFA725AF804140	2383
PMD1B5	486.30900000000003	Flag	PMD1B5	PMD1B5	\N	\N	\N	0101000000CD392C98AE9A40407B26BC83A6804140	2384
PMD1C1	498.80599999999998	Flag	PMD1C1	PMD1C1	\N	\N	\N	0101000000EB2748DE4C9A4040453260A0D6804140	2385
PMD1C2	489.43299999999999	Flag	PMD1C2	PMD1C2	\N	\N	\N	0101000000D71687FB949A4040E31A8374CF804140	2386
PMD1C3	464.43900000000002	Flag	PMD1C3	PMD1C3	\N	\N	\N	01010000001F3A239BC29A4040DCBE7CEFC5804140	2387
PMD1C4	444.97300000000001	Flag	PMD1C4	PMD1C4	\N	\N	\N	010100000045A66FDEE79A4040C38E9467BD804140	2388
PMD1D1	500.00799999999998	Flag	PMD1D1	PMD1D1	\N	\N	\N	0101000000EB3B5CFE459A4040CE696F20BF804140	2389
PMD1D2	493.03800000000001	Flag	PMD1D2	PMD1D2	\N	\N	\N	01010000007D4110C2919A404024F7CF2EBA804140	2390
PMD1D3	464.43900000000002	Flag	PMD1D3	PMD1D3	\N	\N	\N	0101000000361818E9AD9A40407311B9C2B6804140	2391
PMD1D4	453.86500000000001	Flag	PMD1D4	PMD1D4	\N	\N	\N	010100000079366721DA9A40409C9DAC1BA6804140	2392
PMD1E1	452.423	Flag	PMD1E1	PMD1E1	\N	\N	\N	0101000000FBD6EC20ED9A4040460ACDC4A0804140	2393
PMD1E2	451.94200000000001	Flag	PMD1E2	PMD1E2	\N	\N	\N	010100000097B4FBB8EA9A40409612E7809B804140	2394
PMD1E3	454.58600000000001	Flag	PMD1E3	PMD1E3	\N	\N	\N	0101000000300970AAD09A4040B6B2004A9C804140	2395
PMD1E4	462.75700000000001	Flag	PMD1E4	PMD1E4	\N	\N	\N	0101000000EC069F20D09A40406FEE7A2CA7804140	2396
PMD1E5	481.50200000000001	Flag	PMD1E5	PMD1E5	\N	\N	\N	010100000011971406C29A404003D13008AE804140	2397
PMD1E6	485.34800000000001	Flag	PMD1E6	PMD1E6	\N	\N	\N	010100000037C45C78BC9A4040A148CCF3AD804140	2398
PMD1E7	495.20100000000002	Flag	PMD1E7	PMD1E7	\N	\N	\N	0101000000BB124940819A404035E22F51B0804140	2399
PMD1E8	484.86700000000002	Flag	PMD1E8	PMD1E8	\N	\N	\N	0101000000023B64EE4D9A40408FC6188DB9804140	2400
PMD1E9	491.11500000000001	Flag	PMD1E9	PMD1E9	\N	\N	\N	01010000004C50001B499A4040F2D1BCC5BE804140	2401
PMF1	501.93000000000001	Flag	PMF1	PMF1	\N	\N	\N	0101000000C35AD0094E9A4040F3B8CB25E5804140	2402
PMFAR1	390.65800000000002	Flag	PMFAR1	PMFAR1	\N	\N	\N	01010000003B7D8BEE239C404057FDA7A121834140	2403
PMFAR2	394.50400000000002	Flag	PMFAR2	PMFAR2	\N	\N	\N	010100000083D3AB962E9C404013060F1A14834140	2404
PMFE2	482.94400000000002	Flag	PMFE2	PMFE2	\N	\N	\N	0101000000C3BC4361829A40402CF5BF6BF6804140	2405
PMFEN	488.23200000000003	Flag	PMFEN	PMFEN	\N	\N	\N	0101000000B078B7A0A19A4040FEB03B6373814140	2406
PMG1	494.24000000000001	Flag	PMG1	PMG1	\N	\N	\N	0101000000212A806E579A4040D2C5F7DC01814140	2407
PMG2	471.40899999999999	Flag	PMG2	PMG2	\N	\N	\N	01010000005C15DBB6859A404035F68A46F5804140	2408
PMG3	500.96899999999999	Flag	PMG3	PMG3	\N	\N	\N	01010000002D74C0D6AE9A40406D1A4808F0804140	2409
PMG4	474.29199999999997	Flag	PMG4	PMG4	\N	\N	\N	0101000000109DB714C49A40402A5257FAED804140	2410
PMH1	462.75700000000001	Flag	PMH1	PMH1	\N	\N	\N	0101000000490D0D8EC99A4040D141C7A9BE814140	2411
PMH2	450.74000000000001	Flag	PMH2	PMH2	\N	\N	\N	0101000000D70051A2F89A40400F30F065AD814140	2412
PMH3	440.887	Flag	PMH3	PMH3	\N	\N	\N	010100000000ED5B6E2F9B40400278CCBFA4814140	2413
PMH4	425.50599999999997	Flag	PMH4	PMH4	\N	\N	\N	01010000001A4E8F297C9B4040F8FBC0B38F814140	2414
PMH5	423.82400000000001	Flag	PMH5	PMH5	\N	\N	\N	0101000000270790FA9B9B404068C787858C814140	2415
PMH6	418.77699999999999	Flag	PMH6	PMH6	\N	\N	\N	0101000000F65BE0979F9B4040E8780C5C8C814140	2416
PMH7	437.28199999999998	Flag	PMH7	PMH7	\N	\N	\N	0101000000DE793381DA9B404064D0AB2F82814140	2417
PMI6	441.84800000000001	Flag	PMI6	PMI6	\N	\N	\N	0101000000393ECFA9E89B40408F09C14297814140	2418
PMJ1	449.05799999999999	Flag	PMJ1	PMJ1	\N	\N	\N	010100000028CA8C54EB9B40407577AC38A6814140	2419
PMJ4	415.65300000000002	Flag	PMJ4	PMJ4	\N	\N	\N	0101000000BA7D84D1949B4040954758D0CB814140	2420
PMJ5	441.84800000000001	Flag	PMJ5	PMJ5	\N	\N	\N	01010000005E81E390DF9A4040FE78384CF7814140	2421
PMK1	434.87900000000002	Flag	PMK1	PMK1	\N	\N	\N	0101000000987064F5F99B4040E9D4DCA4BE814140	2422
PMK3	409.64400000000001	Flag	PMK3	PMK3	\N	\N	\N	010100000058FFD805B29B4040F4FF7279E8814140	2423
PMK6	420.21899999999999	Flag	PMK6	PMK6	\N	\N	\N	0101000000CD48731A989B4040A6E57CBBE9814140	2424
PMK7	432.95600000000002	Flag	PMK7	PMK7	\N	\N	\N	010100000093A52FB4159B4040FC13F78002824140	2425
PMK8	432.71600000000001	Flag	PMK8	PMK8	\N	\N	\N	0101000000063EE31BFA9A40400162A3900A824140	2426
PML1	487.75099999999998	Flag	PML1	PML1	\N	\N	\N	01010000003DF5BAD8839A40407C1229E442814140	2427
PML11	499.767	Flag	PML11	PML11	\N	\N	\N	010100000022375B2A839A4040962920CC71814140	2428
PML13	484.62700000000001	Flag	PML13	PML13	\N	\N	\N	0101000000B2088BAB869A40408818ACC186814140	2429
PML15	482.22300000000001	Flag	PML15	PML15	\N	\N	\N	01010000005086BC7B869A40402D3267A190814140	2430
PML17	485.34800000000001	Flag	PML17	PML17	\N	\N	\N	01010000008212FCF59E9A40408B6A9FFBA3814140	2431
PML3	492.077	Flag	PML3	PML3	\N	\N	\N	01010000007D864C067B9A4040ED0C746953814140	2432
PML5	490.63499999999999	Flag	PML5	PML5	\N	\N	\N	0101000000EECEAFED7A9A4040C9508F195A814140	2433
PML7	487.99099999999999	Flag	PML7	PML7	\N	\N	\N	01010000006E3A1B557A9A4040FB2AE7F561814140	2434
PML9	492.077	Flag	PML9	PML9	\N	\N	\N	010100000011B703A87B9A4040F346E8926A814140	2435
PMR1	498.56599999999997	Flag	PMR1	PMR1	\N	\N	\N	0101000000DED0F40FAB9A40407BAC9F52F3804140	2436
PMR3	486.54899999999998	Flag	PMR3	PMR3	\N	\N	\N	010100000078A54B509A9A40406E3DB78EF9804140	2437
PMR5	481.262	Flag	PMR5	PMR5	\N	\N	\N	0101000000667A8C699B9A404070325BC60E814140	2438
PMR7	481.262	Flag	PMR7	PMR7	\N	\N	\N	0101000000D9B1F079AE9A40407446275A18814140	2439
PMR9	486.30900000000003	Flag	PMR9	PMR9	\N	\N	\N	0101000000E81D58A2B49A404024279CA828814140	2440
POGGIOMARI	50.354399999999998	Waypoint	POGGIOMARI	POGGIOMARI	\N	\N	\N	0101000000B583ACCF1E262D407E8B90B2AC654440	2441
POL KTREE1	395.46499999999997	Waypoint	POL KTREE1	POL KTREE1	\N	\N	\N	010100000070808CC51E9D4040612E3FF68E834140	2442
POL KTREE2	395.46499999999997	Waypoint	POL KTREE2	POL KTREE2	\N	\N	\N	010100000010A46C402D9D40408A3E4F9E9A834140	2443
POL KTREE3	396.66699999999997	Waypoint	POL KTREE3	POL KTREE3	\N	\N	\N	0101000000D9EA9358299D404003B4036796834140	2444
POLSA1	447.37599999999998	Waypoint	POLSA1	POLSA1	\N	\N	\N	0101000000174320340E9C40403CB0BBF7F2834140	2445
POLSA1A	423.82400000000001	Waypoint	POLSA1A	POLSA1A	\N	\N	\N	01010000009B701C690A9C40401A8664080D844140	2446
POLSA1B	420.459	Waypoint	POLSA1B	POLSA1B	\N	\N	\N	010100000001ADA039109C4040584D2B132B844140	2447
POLSA1C	411.08600000000001	Waypoint	POLSA1C	POLSA1C	\N	\N	\N	0101000000435568FA0E9C40406AEB4FFE48844140	2448
POLSA2	443.05000000000001	Waypoint	POLSA2	POLSA2	\N	\N	\N	0101000000E98493DEFE9B404032F2D07CF0834140	2449
POLSA2A	419.25799999999998	Waypoint	POLSA2A	POLSA2A	\N	\N	\N	0101000000FEFD229BFD9B40406253AFA712844140	2450
POLSA2B	411.327	Waypoint	POLSA2B	POLSA2B	\N	\N	\N	01010000005EC98BD5F19B4040DF31A4D928844140	2451
POLSA2C	407.00099999999998	Waypoint	POLSA2C	POLSA2C	\N	\N	\N	010100000034CD8F4DDF9B4040338CA4A53E844140	2452
POLSALITH	404.59800000000001	Waypoint	POLSALITH	POLSALITH	\N	\N	\N	010100000017734767EF9B4040528C9B363D844140	2453
POLSAWS	405.07799999999997	Waypoint	POLSAWS	POLSAWS	\N	\N	\N	01010000006A4150B7F19B40403087337845844140	2454
POLTREE1	500.00799999999998	Waypoint	POLTREE1	POLTREE1	\N	\N	\N	01010000008B19230A549A40401B7FC3C0CA804140	2455
POLTREE2	500.72899999999998	Waypoint	POLTREE2	POLTREE2	\N	\N	\N	0101000000EA1334BE539A4040C1A6EFB1CB804140	2456
POLTREE3	500.72899999999998	Waypoint	POLTREE3	POLTREE3	\N	\N	\N	0101000000C5A567D0499A4040F1398C56A9804140	2457
POLTREE4	507.69799999999998	Waypoint	POLTREE4	POLTREE4	\N	\N	\N	010100000064A5FB99379A4040ECFD9782AD804140	2458
PORSUK	1250.3099999999999	Waypoint	PORSUK	PORSUK	\N	\N	\N	0101000000FD451FDF1D4A4140305807C6D5C14240	2459
PPG 2003	1606.96	Waypoint	PPG 2003	PPG 2003	\N	\N	\N	0101000000427CB6C7B82A354081D63B5D4FF44340	2460
PPM PARMA	46.268900000000002	Waypoint	PPM PARMA	PPM PARMA	\N	\N	\N	010100000095A963448BAC2440B72423415A664640	2461
PRB1	422.14100000000002	Waypoint	PRB1	PRB1	\N	\N	\N	0101000000AC2FB267861053C07EEF2AFC3D394540	2462
PRB10	373.11500000000001	Waypoint	PRB10	PRB10	\N	\N	\N	010100000071216ED27D1053C07D3BE77940394540	2463
PRB11	365.90499999999997	Waypoint	PRB11	PRB11	\N	\N	\N	0101000000DD0A5C7C7C1053C095F4C8D045394540	2464
PRB12	365.90499999999997	Waypoint	PRB12	PRB12	\N	\N	\N	010100000049CE6BE7791053C0F92D149247394540	2465
PRB13	367.10599999999999	Waypoint	PRB13	PRB13	\N	\N	\N	0101000000214DB0907D1053C0971F138446394540	2466
PRB2	394.26299999999998	Waypoint	PRB2	PRB2	\N	\N	\N	0101000000D06DBA4C8A1053C0D6FC602F40394540	2467
PRB3	364.94299999999998	Waypoint	PRB3	PRB3	\N	\N	\N	0101000000818DB1348C1053C08EDEC41E47394540	2468
PRB4	359.416	Waypoint	PRB4	PRB4	\N	\N	\N	0101000000954262B38A1053C0201E1FB347394540	2469
PRB5	407.24099999999999	Waypoint	PRB5	PRB5	\N	\N	\N	0101000000E445EC348E1053C01D0A2F5946394540	2470
PRB6	408.202	Waypoint	PRB6	PRB6	\N	\N	\N	01010000007133142B8E1053C01AC2F46746394540	2471
PRB8	368.06799999999998	Waypoint	PRB8	PRB8	\N	\N	\N	010100000096C5A1D78F1053C0CC38E0CF3D394540	2472
PRB9	365.90499999999997	Waypoint	PRB9	PRB9	\N	\N	\N	0101000000573D30E57B1053C07608F38942394540	2473
PSK1	199.358	Flag	PSK1	PSK1	\N	\N	\N	0101000000704F208471BB40407E9BDFDF497B4140	2474
PSK2	208.00899999999999	Flag	PSK2	PSK2	\N	\N	\N	01010000007434449E6FBB4040643AF3F9487B4140	2475
PSK3	205.125	Flag	PSK3	PSK3	\N	\N	\N	01010000007D5BDBD473BB404043079C26657B4140	2476
PSK4	206.08699999999999	Flag	PSK4	PSK4	\N	\N	\N	01010000005C45980587BB4040388BB4E0477B4140	2477
PSK5	203.68299999999999	Flag	PSK5	PSK5	\N	\N	\N	0101000000E3BD2B017FBB4040E12E7CF63C7B4140	2478
PSK6	206.80799999999999	Flag	PSK6	PSK6	\N	\N	\N	01010000005BF3800572BB40402A1314C53F7B4140	2479
PSK7	202.96199999999999	Flag	PSK7	PSK7	\N	\N	\N	0101000000042B635061BB4040B6656F124B7B4140	2480
PT1	413.97000000000003	Flag	PT1	PT1	\N	\N	\N	0101000000A60EDBDE3A9F404037B69767B7824140	2481
PT15MSWAR2	122.93300000000001	Waypoint	PT15MSWAR2	PT15MSWAR2	\N	\N	\N	010100000051A39064D6AB40406B856FB51C614140	2482
PTAREA2SW	122.93300000000001	Waypoint	PTAREA2SW	PTAREA2SW	\N	\N	\N	01010000005D0807B5FEAB40407525A463FE604140	2483
PTAREA3SW	122.212	Waypoint	PTAREA3SW	PTAREA3SW	\N	\N	\N	01010000007776AB47D7AB4040D700216922614140	2484
PTBASE1	116.925	Waypoint	PTBASE1	PTBASE1	\N	\N	\N	0101000000945833E100AC404075E9FC67CF604140	2485
PTM	24.8796	Waypoint	PTM	PTM	\N	\N	\N	0101000000FA895177289351C0B20BED39BCE94540	2486
PTPIVOT+WL	118.367	Waypoint	PTPIVOT+WL	PTPIVOT+WL	\N	\N	\N	010100000061F2E388E5AB4040E20F5D5518614140	2487
PY1	267.61099999999999	Flag	PY1	PY1	\N	\N	\N	01010000001E075B75E8AD404012BA6737DC814140	2488
PY2	254.393	Flag	PY2	PY2	\N	\N	\N	010100000095F0CB338BAF4040206AD3BF41824140	2489
PY3	257.75700000000001	Flag	PY3	PY3	\N	\N	\N	0101000000F46F9CB674AF40407146373CE9814140	2490
PY4	261.84300000000002	Flag	PY4	PY4	\N	\N	\N	0101000000352BBF324DAF4040CB7C1469B3814140	2491
RCHRCH	248.625	Waypoint	RCHRCH	RCHRCH	\N	\N	\N	0101000000E9E750FD38B4404067CB7F694D7B4140	2492
RCHRCH2	252.47	Waypoint	RCHRCH2	RCHRCH2	\N	\N	\N	0101000000CE8408334BB440402820370D5B7B4140	2493
ROCK1	363.98200000000003	Flag	ROCK1	ROCK1	\N	\N	\N	01010000009BCA3F8650AB4040E568C39E0C7F4140	2494
ROCK2	289.48000000000002	Flag	ROCK2	ROCK2	\N	\N	\N	01010000006430D78630AB404095C4BFF37E7F4140	2495
SA1	330.577	Flag	SA1	SA1	\N	\N	\N	01010000009FE0742849AF4040651C7420A37A4140	2496
SA2	336.34399999999999	Flag	SA2	SA2	\N	\N	\N	01010000005C3E50764CAF4040C07AC062AB7A4140	2497
SA3	332.01900000000001	Flag	SA3	SA3	\N	\N	\N	0101000000089F13664DAF40401A45FF99BB7A4140	2498
SAL	583.88199999999995	Flag	SAL	SAL	\N	\N	\N	0101000000F0E613F398314040EDF420900D7C4140	2499
SANGOTTARD	2087.1300000000001	Waypoint	SANGOTTARD	SANGOTTARD	\N	\N	\N	01010000002F544FFDBB212140C3852F5819474740	2500
SBD1	93.613399999999999	Waypoint	SBD1	SBD1	\N	\N	\N	01010000005867A02514AC404041D0E8468C604140	2501
SBD2	91.210099999999997	Waypoint	SBD2	SBD2	\N	\N	\N	01010000002E97130809AC4040C80B3FF189604140	2502
SBD3	75.108199999999997	Waypoint	SBD3	SBD3	\N	\N	\N	0101000000C281C8D6FEAB4040FDA328FD68604140	2503
SBD4	79.914699999999996	Waypoint	SBD4	SBD4	\N	\N	\N	0101000000C777A33BFAAB404093DD97F677604140	2504
SBD5	80.6357	Waypoint	SBD5	SBD5	\N	\N	\N	0101000000DFFC235F0BAC404033EFBF357A604140	2505
SBD6	89.287499999999994	Waypoint	SBD6	SBD6	\N	\N	\N	01010000004022A38C03AC404076E658BD87604140	2506
SCO-23 ABI	1525.49	Waypoint	SCO-23 ABI	SCO-23 ABI	\N	\N	\N	0101000000CDDCCF6BE40635409CFCF0D5092B4440	2507
SCOTIDA	1184.9400000000001	Waypoint	SCOTIDA	SCOTIDA	\N	\N	\N	0101000000BC5299D91CF3344099AC54C7DF2C4440	2508
SCV1	1182.3	Waypoint	SCV1	SCV1	\N	\N	\N	01010000008E792C9C755740408A8DDC472C804140	2509
SCV10	1174.8499999999999	Waypoint	SCV10	SCV10	\N	\N	\N	010100000069EB700CB257404046DA004B32804140	2510
SCV-100	1109.96	Waypoint	SCV-100	SCV-100	\N	\N	\N	01010000004967C8C7DA574040A60BA788307E4140	2511
SCV106	1115.73	Waypoint	SCV106	SCV106	\N	\N	\N	0101000000DBA95B6BDF57404001219422327E4140	2512
SCV107	1103.71	Waypoint	SCV107	SCV107	\N	\N	\N	01010000000CED5F03E9574040D82C978D0E7E4140	2513
SCV108	1132.79	Waypoint	SCV108	SCV108	\N	\N	\N	01010000008DE3432D265840408777236DCF7E4140	2514
SCV11	1183.74	Waypoint	SCV11	SCV11	\N	\N	\N	0101000000679543D281574040C5B6176122804140	2515
SCV110	1137.8399999999999	Waypoint	SCV110	SCV110	\N	\N	\N	01010000006600F1B8FD574040BCEE9B65507E4140	2516
SCV12	1103.71	Waypoint	SCV12	SCV12	\N	\N	\N	0101000000C96FE3AC8E574040D18B4F0B22804140	2517
SCV120	1073.9100000000001	Waypoint	SCV120	SCV120	\N	\N	\N	010100000060ED324005584040FFA778E28D7E4140	2518
SCV121	1261.6099999999999	Waypoint	SCV121	SCV121	\N	\N	\N	01010000005C8C2B7523584040266C5B6ED27E4140	2519
SCV122	1127.5	Waypoint	SCV122	SCV122	\N	\N	\N	010100000001DA47882158404045A98F50CB7E4140	2520
SCV123	1127.5	Waypoint	SCV123	SCV123	\N	\N	\N	0101000000B27BB805075840409245FFA3AD7E4140	2521
SCV125	1153.7	Waypoint	SCV125	SCV125	\N	\N	\N	010100000025735FE0CF574040E6A638D4907E4140	2522
SCV126	1159.23	Waypoint	SCV126	SCV126	\N	\N	\N	0101000000DA1A071AD0574040C4E3AF84867E4140	2523
SCV13	1190.71	Waypoint	SCV13	SCV13	\N	\N	\N	01010000002C98772393574040F95224D11E804140	2524
SCV130	1242.3800000000001	Waypoint	SCV130	SCV130	\N	\N	\N	0101000000B515E7E7C7574040D7D8EBFEED7E4140	2525
SCV131	1264.73	Waypoint	SCV131	SCV131	\N	\N	\N	0101000000DEB6CB8BC7574040E3E70325EC7E4140	2526
SCV134	1251.03	Waypoint	SCV134	SCV134	\N	\N	\N	01010000006ADDDB34C3574040E28AB422E47E4140	2527
SCV135	1242.1400000000001	Waypoint	SCV135	SCV135	\N	\N	\N	0101000000BC82BB53C45740408593B8B7F07E4140	2528
SCV136	1224.1199999999999	Waypoint	SCV136	SCV136	\N	\N	\N	010100000049959316CF5740408544C32AEA7E4140	2529
SCV137	1216.6700000000001	Waypoint	SCV137	SCV137	\N	\N	\N	010100000012E173E8B35740402D75A773F97E4140	2530
SCV138	1146.73	Waypoint	SCV138	SCV138	\N	\N	\N	01010000008578EF6B0E5840407CF9BB7CCD7E4140	2531
SCV14	1228.6800000000001	Waypoint	SCV14	SCV14	\N	\N	\N	01010000005C6917A88C5740407490303C11804140	2532
SCV15	1184.9400000000001	Waypoint	SCV15	SCV15	\N	\N	\N	0101000000B04B105290574040B05CD4C60F804140	2533
SCV16	1159.47	Waypoint	SCV16	SCV16	\N	\N	\N	0101000000529B17ABB6574040D0EB1FA91D804140	2534
SCV17	1157.3	Waypoint	SCV17	SCV17	\N	\N	\N	0101000000333B7442B4574040E203A89A3A804140	2535
SCV18	1157.3	Waypoint	SCV18	SCV18	\N	\N	\N	010100000030EDA36CB4574040986BCC493A804140	2536
SCV2	1211.3800000000001	Waypoint	SCV2	SCV2	\N	\N	\N	0101000000186323BA6E5740406EEFD6D628804140	2537
SCV21	1187.3399999999999	Waypoint	SCV21	SCV21	\N	\N	\N	0101000000348D446D7D574040D0DCC4420D804140	2538
SCV25	1162.3499999999999	Waypoint	SCV25	SCV25	\N	\N	\N	010100000068653FAEF457404087C328C148804140	2539
SCV26	1392.3399999999999	Waypoint	SCV26	SCV26	\N	\N	\N	0101000000DFEF40321757404012C4A4B85D7F4140	2540
SCV27	1397.1500000000001	Waypoint	SCV27	SCV27	\N	\N	\N	0101000000287AAB7120574040AE7E20045A7F4140	2541
SCV28	1400.52	Waypoint	SCV28	SCV28	\N	\N	\N	01010000007355435C25574040C414A0FE577F4140	2542
SCV29	1389.46	Waypoint	SCV29	SCV29	\N	\N	\N	0101000000AB65ECFD265740408E4110E8587F4140	2543
SCV3	1389.46	Waypoint	SCV3	SCV3	\N	\N	\N	01010000000502901F19724040F0D85854C7014240	2544
SCV30	1399.0699999999999	Waypoint	SCV30	SCV30	\N	\N	\N	0101000000CC36F3882A5740407AED4B96507F4140	2545
SCV31	1413.49	Waypoint	SCV31	SCV31	\N	\N	\N	01010000003156871B31574040F14BD7F5467F4140	2546
SCV32	1414.7	Waypoint	SCV32	SCV32	\N	\N	\N	010100000028BB0B2738574040863668FE447F4140	2547
SCV33	1411.0899999999999	Waypoint	SCV33	SCV33	\N	\N	\N	0101000000118E38E83B574040F590A7E7487F4140	2548
SCV34	1425.75	Waypoint	SCV34	SCV34	\N	\N	\N	0101000000B19684093857404028FF0CCF487F4140	2549
SCV35	1401.96	Waypoint	SCV35	SCV35	\N	\N	\N	01010000008D71F3FD3C574040E48D10413C7F4140	2550
SCV36	1392.3399999999999	Waypoint	SCV36	SCV36	\N	\N	\N	0101000000567498ED125740407B7967D85B7F4140	2551
SCV37	1394.51	Waypoint	SCV37	SCV37	\N	\N	\N	01010000004586DBF520574040F30C0DD0657F4140	2552
SCV38	1405.5599999999999	Waypoint	SCV38	SCV38	\N	\N	\N	0101000000066C679522574040CDFE1080577F4140	2553
SCV39	1388.02	Waypoint	SCV39	SCV39	\N	\N	\N	01010000009ECAB8951D574040A76EBF605D7F4140	2554
SCV4	1191.1900000000001	Waypoint	SCV4	SCV4	\N	\N	\N	0101000000B808590469574040CD1211BC29804140	2555
SCV40	1396.9100000000001	Waypoint	SCV40	SCV40	\N	\N	\N	01010000003C777B43335740404E291C323F7F4140	2556
SCV41	1407.73	Waypoint	SCV41	SCV41	\N	\N	\N	0101000000A39A80C9C756404042C68F709B7F4140	2557
SCV42	1398.1099999999999	Waypoint	SCV42	SCV42	\N	\N	\N	0101000000BF460476C4564040431D602A997F4140	2558
SCV43	1400.28	Waypoint	SCV43	SCV43	\N	\N	\N	0101000000D4FA749FD05640404297E7F68F7F4140	2559
SCV44	1414.9400000000001	Waypoint	SCV44	SCV44	\N	\N	\N	0101000000C59153B0D0564040849F7F558A7F4140	2560
SCV45	1394.75	Waypoint	SCV45	SCV45	\N	\N	\N	01010000003DF0BA49CF5640401149AB5C937F4140	2561
SCV46	1394.03	Waypoint	SCV46	SCV46	\N	\N	\N	0101000000662B97DC145740405E7070635F7F4140	2562
SCV47	1385.8599999999999	Waypoint	SCV47	SCV47	\N	\N	\N	0101000000E550243701574040326704BB647F4140	2563
SCV48	1378.4100000000001	Waypoint	SCV48	SCV48	\N	\N	\N	01010000004C6914CAF65640405CDFBF2D637F4140	2564
SCV49	1380.3299999999999	Waypoint	SCV49	SCV49	\N	\N	\N	01010000008E3FA7D9E9564040BEFF7877707F4140	2565
SCV50	1369.03	Waypoint	SCV50	SCV50	\N	\N	\N	010100000064B234C7245740405F3F483F6F7F4140	2566
SCV51	1390.6600000000001	Waypoint	SCV51	SCV51	\N	\N	\N	0101000000408224E4EA564040FEB0401C6D7F4140	2567
SCV52	1401.48	Waypoint	SCV52	SCV52	\N	\N	\N	010100000076BB03E1EF564040DFE7F0F7687F4140	2568
SCV53	1401.48	Waypoint	SCV53	SCV53	\N	\N	\N	010100000039FABB1EDC5640407055744C9F7F4140	2569
SCV54	1387.0599999999999	Waypoint	SCV54	SCV54	\N	\N	\N	0101000000AE274320DA56404085F72C3A9F7F4140	2570
SCV55	1378.1700000000001	Waypoint	SCV55	SCV55	\N	\N	\N	0101000000C51D6FF2DB564040DAB0FCB2A17F4140	2571
SCV56	1399.55	Waypoint	SCV56	SCV56	\N	\N	\N	0101000000C22ECC3A0A574040817B576A617F4140	2572
SCV57	1382.97	Waypoint	SCV57	SCV57	\N	\N	\N	01010000000594B39B0C574040BB0D3F3F597F4140	2573
SCV58	1382.97	Waypoint	SCV58	SCV58	\N	\N	\N	0101000000289C546908574040D6023930527F4140	2574
SCV59	1382.97	Waypoint	SCV59	SCV59	\N	\N	\N	0101000000280B806D14574040C6DDD4CB657F4140	2575
SCV-6	1123.1800000000001	Waypoint	SCV-6	SCV-6	\N	\N	\N	0101000000BF22801A855740407F5F307352804140	2576
SCV60	1376	Waypoint	SCV60	SCV60	\N	\N	\N	01010000001EF6A0721C574040BA0BE35A6C7F4140	2577
SCV61	1376	Waypoint	SCV61	SCV61	\N	\N	\N	010100000078C59863155740401758539B707F4140	2578
SCV63	1361.8199999999999	Waypoint	SCV63	SCV63	\N	\N	\N	010100000075D518812C5740407EEEE034787F4140	2581
SCV65	1379.1300000000001	Waypoint	SCV65	SCV65	\N	\N	\N	0101000000470F538DE45640403B7F8760A47F4140	2582
SCV66	1397.3900000000001	Waypoint	SCV66	SCV66	\N	\N	\N	010100000087DBE8363D5740407225840C397F4140	2583
SCV67	1404.3599999999999	Waypoint	SCV67	SCV67	\N	\N	\N	0101000000384F9B8F3057404037F36CD53C7F4140	2584
SCV69	1237.3299999999999	Waypoint	SCV69	SCV69	\N	\N	\N	01010000000AA6A4CBBE574040279A6068107F4140	2585
SCV69 A	1218.3499999999999	Waypoint	SCV69 A	SCV69 A	\N	\N	\N	01010000007221876FBE5740402BA217CF0F7F4140	2586
SCV7	1131.5899999999999	Waypoint	SCV7	SCV7	\N	\N	\N	0101000000852D8F988D574040AE85689354804140	2587
SCV70	1211.3800000000001	Waypoint	SCV70	SCV70	\N	\N	\N	01010000005C6E1737C1574040E1D4F0F5017F4140	2588
SCV71	1182.78	Waypoint	SCV71	SCV71	\N	\N	\N	0101000000890FB44ED157404049D66F93027F4140	2589
SCV72	1182.78	Waypoint	SCV72	SCV72	\N	\N	\N	0101000000A81BC00CDA57404075EB142FFF7E4140	2590
SCV-73	1231.0799999999999	Waypoint	SCV-73	SCV-73	\N	\N	\N	01010000002DBDA0FFCA574040578194B7E77E4140	2591
SCV-74	1234.21	Waypoint	SCV-74	SCV-74	\N	\N	\N	01010000003CC3B841CB574040B05F0BD5E87E4140	2592
SCV76	1373.5999999999999	Waypoint	SCV76	SCV76	\N	\N	\N	01010000000B73FCFCE9574040DE281702377F4140	2593
SCV77	1225.5599999999999	Waypoint	SCV77	SCV77	\N	\N	\N	0101000000B9754B33DC574040D3F8C773247F4140	2594
SCV78	1207.53	Waypoint	SCV78	SCV78	\N	\N	\N	0101000000BC6F845EE2574040D12794B3277F4140	2595
SCV79	1225.3199999999999	Waypoint	SCV79	SCV79	\N	\N	\N	0101000000AD14F846D157404080DF03972E7F4140	2596
SCV8	1208.97	Waypoint	SCV8	SCV8	\N	\N	\N	01010000009E0427BCB3574040D3A81B8329804140	2597
SCV80	1243.3399999999999	Waypoint	SCV80	SCV80	\N	\N	\N	0101000000CEC5C85ACF574040D0DCD826347F4140	2598
SCV81	1263.05	Waypoint	SCV81	SCV81	\N	\N	\N	0101000000B061EBF1B957404086F3AB64407F4140	2599
SCV82	1229.8800000000001	Waypoint	SCV82	SCV82	\N	\N	\N	01010000002D851C68B957404009B6C3BF457F4140	2600
SCV83	1255.8399999999999	Waypoint	SCV83	SCV83	\N	\N	\N	0101000000BA7384B3AB57404069EDFA284A7F4140	2601
SCV84	1265.45	Waypoint	SCV84	SCV84	\N	\N	\N	0101000000C1721770B5574040CD3DB7DF407F4140	2602
SCV-86	1109.48	Waypoint	SCV-86	SCV-86	\N	\N	\N	0101000000541F1034DB5740402A45DCB02E7E4140	2603
SCV-87	1081.3599999999999	Waypoint	SCV-87	SCV-87	\N	\N	\N	010100000074FAF3CDF357404019249BB05F7E4140	2604
SCV88	1121.01	Waypoint	SCV88	SCV88	\N	\N	\N	0101000000AD19F8D505584040A89BFB13AB7E4140	2605
SCV89	1134.47	Waypoint	SCV89	SCV89	\N	\N	\N	0101000000462D0C12D45740409167CC618D7E4140	2606
SCV9	1191.6700000000001	Waypoint	SCV9	SCV9	\N	\N	\N	0101000000E2CC17CAB557404024FA87BD28804140	2607
SCV91	1167.8800000000001	Waypoint	SCV91	SCV91	\N	\N	\N	0101000000D579A08AE557404096444C420F7E4140	2608
SCV92	1143.3699999999999	Waypoint	SCV92	SCV92	\N	\N	\N	0101000000B19F287D1F584040E999389BC97E4140	2609
SCV93	1146.97	Waypoint	SCV93	SCV93	\N	\N	\N	010100000088AC081313584040B252E88CCD7E4140	2610
SCV94	1131.3499999999999	Waypoint	SCV94	SCV94	\N	\N	\N	01010000001792707A07584040455F537DAD7E4140	2611
SCV95	1173.1700000000001	Waypoint	SCV95	SCV95	\N	\N	\N	0101000000F90BDDEF0758404047600CE2CC7E4140	2612
SCV96	1127.98	Waypoint	SCV96	SCV96	\N	\N	\N	0101000000F75EBB35E25740402138D8E22D7E4140	2613
SCV97	1115.49	Waypoint	SCV97	SCV97	\N	\N	\N	0101000000C18B2B1FE3574040338207B2107E4140	2614
SCV98	1117.6500000000001	Waypoint	SCV98	SCV98	\N	\N	\N	0101000000028A68A6015840401841AFB4567E4140	2615
SCV99	1143.3699999999999	Waypoint	SCV99	SCV99	\N	\N	\N	0101000000E1A4A8B47757404006EE573928804140	2616
SEDGEPT	83.759900000000002	Waypoint	SEDGEPT	SEDGEPT	\N	\N	\N	0101000000B555C07201AC4040ABCD20BA77604140	2617
SIASPILIOS	240.45400000000001	Waypoint	SIASPILIOS	SIASPILIOS	\N	\N	\N	0101000000AD3FBF7811B4404061EEDC79777B4140	2618
SKOURIOTIS	337.30599999999998	Waypoint	SKOURIOTIS	SKOURIOTIS	\N	\N	\N	0101000000B6588CE556724040C6AA0435838C4140	2619
SMH1	489.673	Waypoint	SMH1	SMH1	\N	\N	\N	0101000000C27C7CB9C48B4040D1467BAAAD834140	2620
SMH10	500.00799999999998	Waypoint	SMH10	SMH10	\N	\N	\N	010100000013EFFB95E08B40408AFB920496834140	2621
SMH11	512.745	Waypoint	SMH11	SMH11	\N	\N	\N	010100000050BDFB04CC8B4040CB2378768B834140	2622
SMH12	507.21699999999998	Waypoint	SMH12	SMH12	\N	\N	\N	0101000000784AF04ECA8B4040387EDB2587834140	2623
SMH13	513.947	Waypoint	SMH13	SMH13	\N	\N	\N	0101000000BBCCC7EDCB8B40409D7ADC6B8C834140	2624
SMH2	502.17099999999999	Waypoint	SMH2	SMH2	\N	\N	\N	0101000000446E6054CD8B4040344EFC7E95834140	2625
SMH4	489.91399999999999	Waypoint	SMH4	SMH4	\N	\N	\N	01010000006FCC6498D08B4040B319388490834140	2626
SMH6	514.42700000000002	Waypoint	SMH6	SMH6	\N	\N	\N	0101000000EA0FF04BCD8B4040A855B7248E834140	2627
SMH9	505.29500000000002	Waypoint	SMH9	SMH9	\N	\N	\N	01010000000FA48895DC8B404048872C778B834140	2628
SMI LYGOS	18.871500000000001	Waypoint	SMI LYGOS	SMI LYGOS	\N	\N	\N	01010000002F69F97200E33A408517383108D64240	2629
SPIL1	289.48000000000002	Flag	SPIL1	SPIL1	\N	\N	\N	01010000008685F0EE5CB340405466DB4D567D4140	2630
SPIL2	296.20999999999998	Flag	SPIL2	SPIL2	\N	\N	\N	01010000009086BBC95BB3404067D11095537D4140	2631
SPIL3	298.613	Flag	SPIL3	SPIL3	\N	\N	\N	0101000000C1D4F76358B34040D5AEB368547D4140	2632
SPIL4	299.815	Flag	SPIL4	SPIL4	\N	\N	\N	010100000004771C1655B3404027F71FBF557D4140	2633
SPIL5	298.13200000000001	Flag	SPIL5	SPIL5	\N	\N	\N	0101000000FDA09BEE56B340405024446B587D4140	2634
SPIL6	296.69	Flag	SPIL6	SPIL6	\N	\N	\N	01010000009CDBEC5E5AB340407A7F0FA0577D4140	2635
SPIL7	299.334	Flag	SPIL7	SPIL7	\N	\N	\N	0101000000B53F434459B34040E9168CDA557D4140	2636
ST1	364.70299999999997	Waypoint	ST1	ST1	\N	\N	\N	0101000000EC0F34698A1053C0232390E040394540	2637
ST2	366.38499999999999	Waypoint	ST2	ST2	\N	\N	\N	01010000009C85F15E8B1053C087FC2ECA3E394540	2638
ST4	368.548	Waypoint	ST4	ST4	\N	\N	\N	0101000000AFE3D1238C1053C0858528993B394540	2639
STV-10	1087.6099999999999	Waypoint	STV-10	STV-10	\N	\N	\N	01010000005D0E77D2DB50404079F7E68853844140	2640
STV11	1076.0699999999999	Waypoint	STV11	STV11	\N	\N	\N	0101000000555FC3F7F1504040A532588055844140	2641
STV-12	1070.0599999999999	Waypoint	STV-12	STV-12	\N	\N	\N	01010000006FBC8C79D5504040BA7440D059844140	2642
STV13	1072.23	Waypoint	STV13	STV13	\N	\N	\N	010100000056D383EFEE50404008B85FBF59844140	2643
STV-14	1079.4400000000001	Waypoint	STV-14	STV-14	\N	\N	\N	0101000000E2BB6CF2D6504040F115CB9751844140	2644
STV-15	1079.6800000000001	Waypoint	STV-15	STV-15	\N	\N	\N	010100000031396C5DD55040403387EBEB55844140	2645
STV-16	1079.4400000000001	Waypoint	STV-16	STV-16	\N	\N	\N	01010000000B20380BD4504040D11ED4EA50844140	2646
STV17	1074.6300000000001	Waypoint	STV17	STV17	\N	\N	\N	010100000021273FEECA5040402F7CFB7552844140	2647
STV18	1082.5599999999999	Waypoint	STV18	STV18	\N	\N	\N	0101000000413AA71FF5504040216508235D844140	2648
STV19	1070.0599999999999	Waypoint	STV19	STV19	\N	\N	\N	010100000058E017D6CE504040EE50D0CA56844140	2649
STV-20	1074.1500000000001	Waypoint	STV-20	STV-20	\N	\N	\N	01010000004C31AC87D45040406B71C83152844140	2650
STV21	1079.9200000000001	Waypoint	STV21	STV21	\N	\N	\N	0101000000A17274DEC4504040AD50375F4B844140	2651
STV22	1079.9200000000001	Waypoint	STV22	STV22	\N	\N	\N	0101000000B1FE0454CE5040402D4578CF4F844140	2652
STV23	1053.24	Waypoint	STV23	STV23	\N	\N	\N	010100000024635788BE5040409D308CB447844140	2653
STV24	1080.1600000000001	Waypoint	STV24	STV24	\N	\N	\N	0101000000BFDB172BC5504040E962F4584A844140	2654
STV25	1093.1400000000001	Waypoint	STV25	STV25	\N	\N	\N	0101000000EF4F44E7BE504040DE9E73F947844140	2655
STV26	1083.04	Waypoint	STV26	STV26	\N	\N	\N	01010000000DB34425BD5040402E90335C43844140	2656
STV27	1088.5699999999999	Waypoint	STV27	STV27	\N	\N	\N	01010000002F468FA5F0504040123FA3CD61844140	2657
STV28	1100.5899999999999	Waypoint	STV28	STV28	\N	\N	\N	010100000047D0C882EA504040FD05979B63844140	2658
STV29	1063.3399999999999	Waypoint	STV29	STV29	\N	\N	\N	010100000012524FD0BA5040402BAE485149844140	2659
STV-30	1091.45	Waypoint	STV-30	STV-30	\N	\N	\N	0101000000158F4495C55040401492502342844140	2660
STV-31	1096.5	Waypoint	STV-31	STV-31	\N	\N	\N	0101000000AF9B1F6DC65040408D50649444844140	2661
STV32	1083.04	Waypoint	STV32	STV32	\N	\N	\N	01010000002C73B82BBB504040F91374FF41844140	2662
STV33	1115.01	Waypoint	STV33	STV33	\N	\N	\N	01010000005FE6BF30AE504040D6108CEB2E844140	2663
STV-34	1080.4000000000001	Waypoint	STV-34	STV-34	\N	\N	\N	0101000000A64B5F05C15040401C70AC7737844140	2664
STV35	1115.25	Waypoint	STV35	STV35	\N	\N	\N	0101000000561C9CC2A9504040366810682E844140	2665
STV36	1114.29	Waypoint	STV36	STV36	\N	\N	\N	01010000005ECC2CC8AA5040405D10990B3F844140	2666
STV37	1125.8199999999999	Waypoint	STV37	STV37	\N	\N	\N	010100000099067D9DDF50404011F1AE316A844140	2667
STV38	1115.49	Waypoint	STV38	STV38	\N	\N	\N	01010000004DD337CEC55040403C95CCE46D844140	2668
STV-39	1099.1400000000001	Waypoint	STV-39	STV-39	\N	\N	\N	010100000087259C18BD504040B09AE4D02D844140	2669
STV40	1114.29	Waypoint	STV40	STV40	\N	\N	\N	01010000009F7A9797AB5040403352C42940844140	2670
STV41	1097.9400000000001	Waypoint	STV41	STV41	\N	\N	\N	01010000008C7B471BB4504040C06EF7C326844140	2671
STV43	1132.3099999999999	Waypoint	STV43	STV43	\N	\N	\N	010100000033AF8839BF504040A37CB8196B844140	2672
STV45	1117.4100000000001	Waypoint	STV45	STV45	\N	\N	\N	01010000005A96B73CB9504040859577C658844140	2673
STV47	977.05799999999999	Waypoint	STV47	STV47	\N	\N	\N	0101000000265120CD475140401EAD9F3B17834140	2674
STV48	969.36800000000005	Waypoint	STV48	STV48	\N	\N	\N	0101000000F345C49247514040C21D4F9B16834140	2675
STV-49	1037.8599999999999	Waypoint	STV-49	STV-49	\N	\N	\N	01010000002158E89250514040FE40E7A50A834140	2676
STV51	991.71799999999996	Waypoint	STV51	STV51	\N	\N	\N	0101000000E18930B054514040517878EB16834140	2677
STV-52	968.40599999999995	Waypoint	STV-52	STV-52	\N	\N	\N	010100000051AF24114B51404079CB68F31E834140	2678
STV53	1010.7	Waypoint	STV53	STV53	\N	\N	\N	0101000000348484A4665140406C19C49520834140	2679
STV54	982.10500000000002	Waypoint	STV54	STV54	\N	\N	\N	0101000000EFBEF06B60514040D9306BAB17834140	2680
STV-55	960.95600000000002	Waypoint	STV-55	STV-55	\N	\N	\N	0101000000CA50F444405840404AC438D8CB8A4140	2681
STV56	994.12199999999996	Waypoint	STV56	STV56	\N	\N	\N	0101000000725B3C6B6051404023929F8F1D834140	2682
STV57	1003.73	Waypoint	STV57	STV57	\N	\N	\N	01010000003E426F1F69514040508154091D834140	2683
STV58	980.18299999999999	Waypoint	STV58	STV58	\N	\N	\N	0101000000D537382A5A5140408249F4A918834140	2684
STV59	1027.29	Waypoint	STV59	STV59	\N	\N	\N	01010000000BD843387C5140405B5F8C971A834140	2685
STV6	1069.8199999999999	Waypoint	STV6	STV6	\N	\N	\N	0101000000D82C1355EA5040402C54F7FB57844140	2686
STV60	1035.9400000000001	Waypoint	STV60	STV60	\N	\N	\N	0101000000BE88EB557A514040D66B70DE1C834140	2687
STV61	885.97400000000005	Waypoint	STV61	STV61	\N	\N	\N	0101000000F7DABFF4AE4F4040A5C09F6DF77F4140	2688
STV62	891.50199999999995	Waypoint	STV62	STV62	\N	\N	\N	0101000000F6C02C8CAB4F40404E505363F37F4140	2689
STV63	889.09799999999996	Waypoint	STV63	STV63	\N	\N	\N	01010000007D2B2BB0B44F4040A2921CABF27F4140	2690
STV64	899.19200000000001	Waypoint	STV64	STV64	\N	\N	\N	0101000000C3DB9CADB04F4040B4578CABEB7F4140	2691
STV65	888.85799999999995	Waypoint	STV65	STV65	\N	\N	\N	0101000000189E4844B94F40400D53F306EC7F4140	2692
STV66	895.58699999999999	Waypoint	STV66	STV66	\N	\N	\N	0101000000599ACB75A94F4040C6BF88E3E47F4140	2693
STV68	891.26099999999997	Waypoint	STV68	STV68	\N	\N	\N	0101000000BEDB0800984F40406497F46CE07F4140	2694
STV7	1055.4100000000001	Waypoint	STV7	STV7	\N	\N	\N	0101000000E38A44CEE65040407E9FC02755844140	2695
STV-8	1072.23	Waypoint	STV-8	STV-8	\N	\N	\N	0101000000C467C71AE95040401D9DD4465E844140	2696
STV9	1072.95	Waypoint	STV9	STV9	\N	\N	\N	01010000001020CCB8EC50404050BC44A157844140	2697
SULTAN MUR	2202.25	Waypoint	SULTAN MUR	SULTAN MUR	\N	\N	\N	0101000000B61834DB32134440725013945A524440	2698
SWALL	91.930999999999997	Waypoint	SWALL	SWALL	\N	\N	\N	01010000009DE3D4B513AC4040BBA88F2092604140	2699
TE	252.71000000000001	Flag	TE	TE	\N	\N	\N	010100000049ADF06108B340401800AD70807E4140	2700
TE2	252.71000000000001	Flag	TE2	TE2	\N	\N	\N	010100000046BC935408B340402F8D2C87807E4140	2701
TEST1	251.50899999999999	Waypoint	TEST1	TEST1	\N	\N	\N	0101000000B704F73B08B3404050041D1F807E4140	2702
TEST2	250.06700000000001	Waypoint	TEST2	TEST2	\N	\N	\N	01010000007C15C06408B34040A9FF837A807E4140	2703
TGC10	335.86399999999998	Waypoint	TGC10	TGC10	\N	\N	\N	01010000009C7809793D1D53C0D7FD4C85843A4540	2704
TGC11	315.67599999999999	Waypoint	TGC11	TGC11	\N	\N	\N	01010000003434CE41381D53C0CAA81BF0853A4540	2705
TGC12	282.99200000000002	Waypoint	TGC12	TGC12	\N	\N	\N	01010000003C839D9AFF1D53C041008587A43A4540	2706
TGC13	284.91399999999999	Waypoint	TGC13	TGC13	\N	\N	\N	01010000009C27E840001E53C009F0DBE5A23A4540	2707
TGC14	294.76799999999997	Waypoint	TGC14	TGC14	\N	\N	\N	0101000000B78F93C4FD1D53C025FD0C5BB23A4540	2708
TGC15	293.08499999999998	Waypoint	TGC15	TGC15	\N	\N	\N	0101000000E7B5A71E081E53C023EA9BBA863A4540	2709
TGC3	287.31799999999998	Waypoint	TGC3	TGC3	\N	\N	\N	0101000000EC332A1A121E53C066D51F158A3A4540	2710
TGC4	290.92200000000003	Waypoint	TGC4	TGC4	\N	\N	\N	0101000000B5975353091E53C0544A90908F3A4540	2711
TGC5	306.78399999999999	Waypoint	TGC5	TGC5	\N	\N	\N	010100000046AAF76D4B1D53C026C6077D763A4540	2712
TGC6	306.78399999999999	Waypoint	TGC6	TGC6	\N	\N	\N	0101000000901E24E34F1D53C03121DC20773A4540	2713
TGC7	306.78399999999999	Waypoint	TGC7	TGC7	\N	\N	\N	0101000000901E24E34F1D53C03121DC20773A4540	2714
TGC8	391.37900000000002	Waypoint	TGC8	TGC8	\N	\N	\N	0101000000C869FC808F6753C0D0E8671AAFE54540	2715
TGC9	274.33999999999997	Waypoint	TGC9	TGC9	\N	\N	\N	0101000000B512CC2E3E1D53C085F89CC8873A4540	2716
TGO	121.732	Waypoint	TGO	TGO	\N	\N	\N	010100000004550B3BF8904140A28487D3835B4040	2717
THB1	-15.014799999999999	Waypoint	THB1	THB1	\N	\N	\N	0101000000AB8466583E8F3740584DC32F76A24140	2718
THB10	-15.7357	Waypoint	THB10	THB10	\N	\N	\N	010100000064397188248F37409639900778A24140	2719
THB-11	-10.208299999999999	Waypoint	THB-11	THB-11	\N	\N	\N	0101000000EDF7F92C258F37405DC13B9B83A24140	2720
THB12	-7.5645699999999998	Waypoint	THB12	THB12	\N	\N	\N	0101000000959D41EDFE8E37400F84BFBA85A24140	2721
THB-13	-8.2856500000000004	Waypoint	THB-13	THB-13	\N	\N	\N	010100000052FFF8C9D38E37404CD05C3083A24140	2722
THB-14	25.360199999999999	Waypoint	THB-14	THB-14	\N	\N	\N	01010000003617FE5AFE8E374062DA883E57A34140	2723
THB-2	-32.318399999999997	Waypoint	THB-2	THB-2	\N	\N	\N	0101000000570A6AC06C8F374055D6E0C46AA24140	2724
THB-3	-10.9292	Waypoint	THB-3	THB-3	\N	\N	\N	0101000000147F9F676F8F374047671CC768A24140	2725
THB4	-15.014799999999999	Waypoint	THB4	THB4	\N	\N	\N	0101000000C35F387C778F3740490DCB7177A24140	2726
THB5	-13.0922	Waypoint	THB5	THB5	\N	\N	\N	0101000000A9FBE907C1A03740B874649611A64140	2727
THB-6	-7.8049299999999997	Waypoint	THB-6	THB-6	\N	\N	\N	01010000003D83B17E668F3740B4C8FFAF7DA24140	2728
THB7	-12.3712	Waypoint	THB7	THB7	\N	\N	\N	0101000000AAD9389F908F3740EBFA5E7369A24140	2729
THB8	-8.7662300000000002	Waypoint	THB8	THB8	\N	\N	\N	01010000002307313F538F374063DA9C227EA24140	2730
THB-9	-8.7662300000000002	Waypoint	THB-9	THB-9	\N	\N	\N	010100000007FAFFC9438F3740466B63F17DA24140	2731
TODD2	96.737700000000004	Waypoint	TODD2	TODD2	\N	\N	\N	0101000000B4DBA3414EAC40405FAC14A7FF604140	2732
TODDTOMB	98.900599999999997	Waypoint	TODDTOMB	TODDTOMB	\N	\N	\N	01010000009079EC804EAC4040091C7B8900614140	2733
TOMB1	397.62799999999999	Flag	TOMB1	TOMB1	\N	\N	\N	0101000000B1C55CBCD99340402772E42810864140	2734
TPW3	271.69600000000003	Waypoint	TPW3	TPW3	\N	\N	\N	01010000003CDCC737BF1E53C017AC5C9B123B4540	2735
TPW4	282.02999999999997	Waypoint	TPW4	TPW4	\N	\N	\N	0101000000BFC4BDD1C51E53C0CDCE4406293B4540	2736
TPW5	290.44200000000001	Waypoint	TPW5	TPW5	\N	\N	\N	0101000000EBB90377CB1E53C006EFC7C7103B4540	2737
TPW6	278.90600000000001	Waypoint	TPW6	TPW6	\N	\N	\N	010100000082B54BCAC61E53C0792F08F6293B4540	2738
TREE12	788.64099999999996	Waypoint	TREE12	TREE12	\N	\N	\N	010100000093856B92F85240400635538C397A4140	2739
TREE13	812.43399999999997	Waypoint	TREE13	TREE13	\N	\N	\N	01010000005A24881BF95240402BF470EB367A4140	2740
TREE14	807.86699999999996	Waypoint	TREE14	TREE14	\N	\N	\N	0101000000D02B1FFBF952404095AC6C1A397A4140	2741
TREE15	1756.9200000000001	Waypoint	TREE15	TREE15	\N	\N	\N	01010000007A647B46456F40404ADC7C8664784140	2742
TREE16	1755.24	Waypoint	TREE16	TREE16	\N	\N	\N	010100000097515FA8596F40401389F38466784140	2743
TREE17	1749.71	Waypoint	TREE17	TREE17	\N	\N	\N	010100000060DE9F2F566F404064B4936360784140	2744
TREE18	1754.04	Waypoint	TREE18	TREE18	\N	\N	\N	01010000002DFFB25F5D6F4040E5FFA48D62784140	2745
TREE19	1754.28	Waypoint	TREE19	TREE19	\N	\N	\N	01010000004B0540D55B6F40405F9E6BEB69784140	2746
TREE20	1761.73	Waypoint	TREE20	TREE20	\N	\N	\N	01010000000F891091396F40404885ACCC66784140	2747
TREE21	1756.9200000000001	Waypoint	TREE21	TREE21	\N	\N	\N	010100000036703F94336F4040E428DBE865784140	2748
TREE22	1772.54	Waypoint	TREE22	TREE22	\N	\N	\N	010100000095732C66396F4040E68BC0CF6D784140	2749
TREE23	1799.22	Waypoint	TREE23	TREE23	\N	\N	\N	0101000000E76DA420436F40407101AF697A784140	2750
TREE24	1789.8499999999999	Waypoint	TREE24	TREE24	\N	\N	\N	01010000008E03FB24416F4040C1A1103180784140	2751
TREE25	1787.2	Waypoint	TREE25	TREE25	\N	\N	\N	0101000000775CC4DF456F4040B974BF4F84784140	2752
TREE26	1789.1300000000001	Waypoint	TREE26	TREE26	\N	\N	\N	0101000000E30AE3FF326F4040CD6234BB86784140	2753
TREE27	1782.6400000000001	Waypoint	TREE27	TREE27	\N	\N	\N	0101000000E695F0262D6F404012AD63FC7E784140	2754
TREE28	1785.04	Waypoint	TREE28	TREE28	\N	\N	\N	010100000039699B94226F4040BEB98F4186784140	2755
TREE29	1784.0799999999999	Waypoint	TREE29	TREE29	\N	\N	\N	010100000068146FBD166F40405E8B34F687784140	2756
TREE30	1798.74	Waypoint	TREE30	TREE30	\N	\N	\N	010100000090892C83FE6E40401E803FC291784140	2757
TREE31	1804.27	Waypoint	TREE31	TREE31	\N	\N	\N	01010000008D4F70CDF76E4040ECD48F5F95784140	2758
TREE A KD	177.488	Waypoint	TREE A KD	TREE A KD	\N	\N	\N	01010000007C45BC174DA14040840F677604664140	2759
TREE B KD	174.364	Waypoint	TREE B KD	TREE B KD	\N	\N	\N	0101000000EADADC6D5EA14040814E4838DB654140	2760
TREE C KD	155.137	Waypoint	TREE C KD	TREE C KD	\N	\N	\N	0101000000BB0DE1CD17A240401A588F4765664140	2761
TREE D KD	147.68700000000001	Waypoint	TREE D KD	TREE D KD	\N	\N	\N	0101000000F13AAB9D12A24040E92BFCA450664140	2762
TREE E KD	147.68700000000001	Waypoint	TREE E KD	TREE E KD	\N	\N	\N	01010000003EFB8A680DA24040C007B41654664140	2763
TREE F MV	272.65800000000002	Waypoint	TREE F MV	TREE F MV	\N	\N	\N	010100000019BF444AE0AD4040FED5CC2260634140	2764
TREE G MV	267.13	Waypoint	TREE G MV	TREE G MV	\N	\N	\N	0101000000DC6C288CD8AD40406C0707A15A634140	2765
TREE H MV	267.851	Waypoint	TREE H MV	TREE H MV	\N	\N	\N	010100000094114F2AE5AD40402110A378B9634140	2766
TREESMPL1	258.71899999999999	Waypoint	TREESMPL1	TREESMPL1	\N	\N	\N	0101000000BF2FD0E3E7AD4040766CB85370634140	2767
TROY 1 TRE	-2.75806	Waypoint	TROY 1 TRE	TROY 1 TRE	\N	\N	\N	0101000000A15BCEF6FF3C3A40E7A878638EFA4340	2768
TROY MEG 2	50.114100000000001	Waypoint	TROY MEG 2	TROY MEG 2	\N	\N	\N	0101000000F29819E7033D3A400DCB88D395FA4340	2769
TROY MEG2A	28.725000000000001	Waypoint	TROY MEG2A	TROY MEG2A	\N	\N	\N	01010000008BA51885FC3C3A40AF87F4B097FA4340	2770
TRT-4	299.334	Waypoint	TRT-4	TRT-4	\N	\N	\N	0101000000F684133BB42553C0EE8567E1A9334540	2771
TRT-5	404.35700000000003	Waypoint	TRT-5	TRT-5	\N	\N	\N	0101000000E1ABB3E0B92553C0D920EC44A4334540	2772
TRT-6	267.61099999999999	Waypoint	TRT-6	TRT-6	\N	\N	\N	010100000060E209699F2553C063C183BA96334540	2773
TRT-7	264.96699999999998	Waypoint	TRT-7	TRT-7	\N	\N	\N	0101000000A62568299F2553C0D546002D99334540	2774
TUSAN	52.7577	Waypoint	TUSAN	TUSAN	\N	\N	\N	0101000000A6F981B0F5553A4096DA83F7F2044440	2775
UC AYAK	1162.5899999999999	Waypoint	UC AYAK	UC AYAK	\N	\N	\N	01010000001B9F78B7FA1E414021132BCE11B34340	2776
URGUP	1315.9200000000001	Waypoint	URGUP	URGUP	\N	\N	\N	01010000003D57A085E0504240F20AF8C004A74340	2777
V10	18.871500000000001	Waypoint	V10	V10	\N	\N	\N	01010000002C4DD07AC6B04040D64413E6855F4140	2778
V11	16.948899999999998	Waypoint	V11	V11	\N	\N	\N	0101000000B049C851CAB04040AECE8F917C5F4140	2779
V12	16.227900000000002	Waypoint	V12	V12	\N	\N	\N	01010000004CBF2B1FD5B0404094C7D03A755F4140	2780
V13	14.7859	Waypoint	V13	V13	\N	\N	\N	0101000000936C4047D5B0404046C1FBC6715F4140	2781
V2	23.197399999999998	Waypoint	V2	V2	\N	\N	\N	010100000059B48C16D8B040403D7BF4A7895F4140	2782
V3	24.8796	Waypoint	V3	V3	\N	\N	\N	0101000000C902930CDAB04040323F6C26755F4140	2783
V4	20.313400000000001	Waypoint	V4	V4	\N	\N	\N	0101000000DAB64BC2D5B040400145C736725F4140	2784
V5	23.197399999999998	Waypoint	V5	V5	\N	\N	\N	0101000000C29DB069CAB040405E3DA0CC7C5F4140	2785
V6	23.918299999999999	Waypoint	V6	V6	\N	\N	\N	0101000000B2776FDAC6B040400608352F865F4140	2786
V7	21.755400000000002	Waypoint	V7	V7	\N	\N	\N	0101000000BB87ACB7CBB04040DC4FDF95915F4140	2787
V8	16.4681	Waypoint	V8	V8	\N	\N	\N	01010000000244400CD4B04040546EC3A48F5F4140	2788
V9	13.8246	Waypoint	V9	V9	\N	\N	\N	0101000000F25A3CCECAB040408FAF4CDE915F4140	2789
VAVLA	492.077	Waypoint	VAVLA	VAVLA	\N	\N	\N	01010000005014F08441A2404071DDE8F1A86B4140	2790
VGATE V1	19.592400000000001	Waypoint	VGATE V1	VGATE V1	\N	\N	\N	01010000002EB3FB9AD4B04040685C3810925F4140	2791
VOUNOS	390.41800000000001	Flag	VOUNOS	VOUNOS	\N	\N	\N	01010000001C278FEB82A54040828C7B51CD804140	2792
WHEAT1	250.30699999999999	Waypoint	WHEAT1	WHEAT1	\N	\N	\N	01010000007229D7A9ECAD4040978558F858634140	2793
WHEAT2	27.0426	Waypoint	WHEAT2	WHEAT2	\N	\N	\N	010100000049B417E3D9B04040EB264F5E865F4140	2794
WHEAT3	58.765700000000002	Waypoint	WHEAT3	WHEAT3	\N	\N	\N	0101000000CE7D0C3256A64040356D48A1C1614140	2795
YETIMLER	56.362499999999997	Waypoint	YETIMLER	YETIMLER	\N	\N	\N	010100000011FBDE88E9743A401B0D481145E44340	2796
ZKB DEPOT	540.14200000000005	Waypoint	ZKB DEPOT	ZKB DEPOT	\N	\N	\N	010100000066225B4C624E4040DE0075FB939C4440	2797
ZP1	6.37439	Waypoint	ZP1	ZP1	\N	\N	\N	0101000000EE4BE7D757AA4040B478EBDB8D5C4140	2798
001	0	Waypoint	001	001	\N	\N	\N	01010000004BA633C556724040D251FF3AA6764140	2799
002	1568.26	Waypoint	002	002	\N	\N	\N	0101000000BB3A532A59724040CC02D471C8764140	2800
003	136.87200000000001	Waypoint	003	003	\N	\N	\N	01010000006A595CBB2BAC40400BA52CCC0D614140	2801
004	121.011	Waypoint	004	004	\N	\N	\N	01010000001BED376122AC40401ED24797F9604140	2802
CTC101	1853.77	Waypoint	CTC101	CTC101	\N	\N	\N	01010000001C376CC3F76D40409705BF7F46774140	2803
CTC175	1850.1700000000001	Waypoint	CTC175	CTC175	\N	\N	\N	01010000007D1DE71FC56D40406FAF883E38774140	2804
CTC300	1800.1800000000001	Waypoint	CTC300	CTC300	\N	\N	\N	01010000008E6604D2406F40409EA6E03A10784140	2805
CTC302	1699.72	Waypoint	CTC302	CTC302	\N	\N	\N	0101000000DBFC2C87376E4040B876AC96D7794140	2806
CTC304	1726.4000000000001	Waypoint	CTC304	CTC304	\N	\N	\N	01010000007A7CA39F2E6E4040BED78F9BC1794140	2807
CTC306	1734.8099999999999	Waypoint	CTC306	CTC306	\N	\N	\N	0101000000AE4F577C256E40401A35DBECBA794140	2808
CTC310	1751.6300000000001	Waypoint	CTC310	CTC310	\N	\N	\N	0101000000AAAF6FA6106E4040D4535867C4794140	2809
CTC311	1621.8599999999999	Waypoint	CTC311	CTC311	\N	\N	\N	0101000000772A18498671404096984BD03D764140	2810
CTC313	1622.8199999999999	Waypoint	CTC313	CTC313	\N	\N	\N	0101000000F710B0FD7472404074D14463A3764140	2811
CTC315	1622.8199999999999	Waypoint	CTC315	CTC315	\N	\N	\N	0101000000AA9B8BBF6D7240409E06443C9D764140	2812
CTC317	1629.3099999999999	Waypoint	CTC317	CTC317	\N	\N	\N	0101000000F5E2087578724040516B53A290764140	2813
CTC318	1630.27	Waypoint	CTC318	CTC318	\N	\N	\N	010100000043FE2233DA724040C064FC5E77764140	2814
CTC319	1612.97	Waypoint	CTC319	CTC319	\N	\N	\N	01010000004FD313088E724040C907D5B7A0764140	2815
CTC320	1628.8299999999999	Waypoint	CTC320	CTC320	\N	\N	\N	0101000000B466D32DD57240403F37B89D8B764140	2816
CTC322	1629.3099999999999	Waypoint	CTC322	CTC322	\N	\N	\N	01010000006490604FA0724040DB16231A76764140	2817
CTC324	1628.1099999999999	Waypoint	CTC324	CTC324	\N	\N	\N	01010000005BF1AA5A94724040D8D7C4467C764140	2818
CTC326	1618.97	Waypoint	CTC326	CTC326	\N	\N	\N	0101000000868B0FC57A72404036D11C965C764140	2819
CTC328	1619.9300000000001	Waypoint	CTC328	CTC328	\N	\N	\N	0101000000990D9F657772404031321F1561764140	2820
CTC330	1623.0599999999999	Waypoint	CTC330	CTC330	\N	\N	\N	01010000008C32E8726D724040FA419FC062764140	2821
CTC332	1611.76	Waypoint	CTC332	CTC332	\N	\N	\N	01010000006365946466724040FCD32C2176764140	2822
CTC334	1623.0599999999999	Waypoint	CTC334	CTC334	\N	\N	\N	01010000008FDCAC5797724040D45337599B764140	2823
CTC336	1617.77	Waypoint	CTC336	CTC336	\N	\N	\N	010100000067823C1676724040FC752303AB764140	2824
CTC338	1567.54	Waypoint	CTC338	CTC338	\N	\N	\N	0101000000813D63B572724040CF95E08BB1764140	2825
CTC340	1604.3099999999999	Waypoint	CTC340	CTC340	\N	\N	\N	010100000016FFEE52677240400E9364AEB0764140	2826
CTC342	1584.1300000000001	Waypoint	CTC342	CTC342	\N	\N	\N	0101000000920E0CA96D7240405F47C3F9B9764140	2827
CTC344	1579.5599999999999	Waypoint	CTC344	CTC344	\N	\N	\N	010100000080B4A44863724040DFB58B36B5764140	2828
CTC346	1602.8699999999999	Waypoint	CTC346	CTC346	\N	\N	\N	01010000004BE13741B87240403B3BDCB84C764140	2829
CTC62	1564.1800000000001	Waypoint	CTC62	CTC62	\N	\N	\N	01010000009076FFFF4F7240404F679BD9B6764140	2830
CTC63	1586.77	Waypoint	CTC63	CTC63	\N	\N	\N	010100000005EDD87F527240405A6B9FC3B9764140	2831
DST11	1402.2	Waypoint	DST11	DST11	\N	\N	\N	010100000063F3340A907B4040E91748F7437A4140	2832
DST13	1414.9400000000001	Waypoint	DST13	DST13	\N	\N	\N	0101000000B293A3FB977B4040B9B1B012397A4140	2833
DST17	1409.4100000000001	Waypoint	DST17	DST17	\N	\N	\N	010100000086B003AFA27B4040C80B60FF327A4140	2834
DST2	1357.02	Waypoint	DST2	DST2	\N	\N	\N	01010000005DFAC2244F7B404074FFBE6A587A4140	2835
DST23	1398.5899999999999	Waypoint	DST23	DST23	\N	\N	\N	0101000000AC306412C17B4040A2BFA7A4547A4140	2836
DST25	1383.45	Waypoint	DST25	DST25	\N	\N	\N	0101000000F4361074EF7B404008B3CCCC537A4140	2837
DST26	1423.1099999999999	Waypoint	DST26	DST26	\N	\N	\N	0101000000A5134BC2EC7B404053EBD77F587A4140	2838
DST29	1431.04	Waypoint	DST29	DST29	\N	\N	\N	01010000009964DF73F27B4040559978F3537A4140	2839
DST3	1347.4000000000001	Waypoint	DST3	DST3	\N	\N	\N	010100000009442CBB507B404044423306587A4140	2840
DST32	1369.27	Waypoint	DST32	DST32	\N	\N	\N	010100000025C96452727B4040639C5726507A4140	2841
DST5	1368.79	Waypoint	DST5	DST5	\N	\N	\N	01010000002568B7895F7B4040BD911B734E7A4140	2842
DST7	1388.98	Waypoint	DST7	DST7	\N	\N	\N	010100000031D62094837B40405738A764497A4140	2843
PSEMBIN	125.337	Waypoint	PSEMBIN	PSEMBIN	\N	\N	\N	010100000041BF8CB501AC4040B13AA79603614140	2844
PSEMFLDWL	121.97199999999999	Waypoint	PSEMFLDWL	PSEMFLDWL	\N	\N	\N	0101000000162827AFD5AB40401AB1C35618614140	2845
PSEMSHDS1	122.693	Waypoint	PSEMSHDS1	PSEMSHDS1	\N	\N	\N	010100000093BF3CE3C9AB4040FDF82A7D11614140	2846
PSEMSOC+Q	111.398	Waypoint	PSEMSOC+Q	PSEMSOC+Q	\N	\N	\N	0101000000A5AB9C3F2BAC404038F6F62B00614140	2847
PSEMSWHRL	121.251	Waypoint	PSEMSWHRL	PSEMSWHRL	\N	\N	\N	0101000000017F4CF9FDAB40409562B47FFF604140	2848
PSEMTOMB	113.801	Waypoint	PSEMTOMB	PSEMTOMB	\N	\N	\N	0101000000EBEDCC8D34AC40408DC714C50E614140	2849
PSEMUPPOT	123.17400000000001	Waypoint	PSEMUPPOT	PSEMUPPOT	\N	\N	\N	010100000063EF62EDF1AB4040C6A0FF5D20614140	2850
PSEMWALL	125.81699999999999	Waypoint	PSEMWALL	PSEMWALL	\N	\N	\N	010100000080E6B8C2D7AB40404190F36623614140	2851
PSEMWALL2	121.011	Waypoint	PSEMWALL2	PSEMWALL2	\N	\N	\N	01010000005A75B3C5C9AB40409F814CC314614140	2852
SCV120	1073.9100000000001	Waypoint	SCV120	SCV120	\N	\N	\N	0101000000A4AF803F05584040FFA778E28D7E4140	2853
SCV125	1153.7	Waypoint	SCV125	SCV125	\N	\N	\N	0101000000A80FABDFCF574040E6A638D4907E4140	2854
SCV54	1387.0599999999999	Waypoint	SCV54	SCV54	\N	\N	\N	0101000000F3E9901FDA56404085F72C3A9F7F4140	2855
SCV65	1379.1300000000001	Waypoint	SCV65	SCV65	\N	\N	\N	01010000008BD1A08CE4564040BE1BD35FA47F4140	2856
001	0	Waypoint	\N	001	\N	\N	\N	01010000004BA633C556724040D251FF3AA6764140	3601
002	1568.27	Waypoint	\N	002	\N	\N	\N	0101000000BB3A532A59724040CC02D471C8764140	3602
003	136.87200000000001	Waypoint	\N	003	\N	\N	\N	01010000006A595CBB2BAC40400BA52CCC0D614140	3603
004	121.011	Waypoint	\N	004	\N	\N	\N	01010000001BED376122AC40401ED24797F9604140	3604
005	628.10299999999995	Waypoint	\N	005	\N	\N	\N	0101000000A94133B05785414095C9276668CB3F40	3605
006	434.87900000000002	Waypoint	\N	006	\N	\N	\N	01010000001FA66B904381414065B66B0FA9614040	3606
ALAMSCV GP	298.613	Waypoint	\N	ALAMSCV GP	\N	\N	\N	0101000000DC3287745DB34040F52B470F567D4140	3607
CANG50	1590.6199999999999	Waypoint	\N	CANG50	\N	\N	\N	0101000000CFE69E99D3163840D80EFF1608A24140	3608
CANG51	1589.6500000000001	Waypoint	\N	CANG51	\N	\N	\N	01010000009DC0AE05C1163840625223C4FAA14140	3609
CANG52	1583.4000000000001	Waypoint	\N	CANG52	\N	\N	\N	0101000000EC0B79E8611638401BE328180CA24140	3610
CANG54	1578.8399999999999	Waypoint	\N	CANG54	\N	\N	\N	01010000004BA5C8EF2717384013ADFB180AA24140	3611
CANG56	1595.4200000000001	Waypoint	\N	CANG56	\N	\N	\N	0101000000C119699A911738404F0E97031AA24140	3612
CANG57	1574.51	Waypoint	\N	CANG57	\N	\N	\N	0101000000F4C2517D5D1738408D9337CAFEA14140	3613
CHA 1	391.13900000000001	Waypoint	\N	CHA 1	\N	\N	\N	01010000009303AB2789824140E62683068F5D4040	3614
CHA 10	368.30799999999999	Waypoint	\N	CHA 10	\N	\N	\N	010100000058E727BB6A824140984B33A8795D4040	3615
CHA 2	365.66399999999999	Waypoint	\N	CHA 2	\N	\N	\N	0101000000AAECD77767824140D5636FEA825D4040	3616
CHA 3	374.07600000000002	Waypoint	\N	CHA 3	\N	\N	\N	010100000051F4B6556D8241402B60FBFC895D4040	3617
CHA 4	399.791	Waypoint	\N	CHA 4	\N	\N	\N	0101000000B1425FB466824140C05368238E5D4040	3618
CHA 5	380.084	Waypoint	\N	CHA 5	\N	\N	\N	0101000000D4B04F68688241406C7A4B97905D4040	3619
CHA10	364.46300000000002	Waypoint	\N	CHA10	\N	\N	\N	01010000006BC1880674824140579DC8D8785D4040	3620
CHA12	473.33100000000002	Waypoint	\N	CHA12	\N	\N	\N	0101000000FD76D7AE10844140DA47375AFF5D4040	3621
CHA13	471.40899999999999	Waypoint	\N	CHA13	\N	\N	\N	0101000000826AF3670E844140F4C5137EFF5D4040	3622
CHA14	475.49400000000003	Waypoint	\N	CHA14	\N	\N	\N	0101000000CB4E8B361184414017CEB44BFB5D4040	3623
CHA6	368.06799999999998	Waypoint	\N	CHA6	\N	\N	\N	0101000000E00E5D1B6D824140D3E130B78C5D4040	3624
CHA7	359.65600000000001	Waypoint	\N	CHA7	\N	\N	\N	0101000000F2FF3B866D8241408B057415815D4040	3625
CHA8	358.69499999999999	Waypoint	\N	CHA8	\N	\N	\N	01010000005157C0026D824140BD2A877E7C5D4040	3626
CHA9	357.97399999999999	Waypoint	\N	CHA9	\N	\N	\N	01010000004BE1EBB2728241401CA2580E775D4040	3627
CSM2	1317.5999999999999	Waypoint	\N	CSM2	\N	\N	\N	0101000000207CE6B440EC3740655F1CD680A84140	3628
CSM4	1451.22	Waypoint	\N	CSM4	\N	\N	\N	01010000000F1C020D57ED3740B1E51B257DA84140	3629
CSM6	1422.8699999999999	Waypoint	\N	CSM6	\N	\N	\N	01010000004AC459E634ED3740F90C94537CA84140	3630
CSM7	1432.72	Waypoint	\N	CSM7	\N	\N	\N	01010000001C564E0104ED3740091B87C273A84140	3631
CTC CO1	1726.8800000000001	Waypoint	\N	CTC CO1	\N	\N	\N	010100000082696C3D307040408BC14CA60D764140	3632
CTC JEN	1638.4400000000001	Waypoint	\N	CTC JEN	\N	\N	\N	0101000000AA50D0327A724040D29ADF6342774140	3633
CTC100	1853.77	Waypoint	\N	CTC100	\N	\N	\N	0101000000810269AB046E40402991C86747774140	3634
CTC101	1853.77	Waypoint	\N	CTC101	\N	\N	\N	01010000001C376CC3F76D40409705BF7F46774140	3635
CTC102	1853.77	Waypoint	\N	CTC102	\N	\N	\N	0101000000EF0CED78E06D4040BC1FF4C256774140	3636
CTC12	788.64099999999996	Waypoint	\N	CTC12	\N	\N	\N	01010000001622B791F85240404BF7A08B397A4140	3637
CTC13	812.43399999999997	Waypoint	\N	CTC13	\N	\N	\N	01010000005A24881BF95240402BF470EB367A4140	3638
CTC130	1806.4300000000001	Waypoint	\N	CTC130	\N	\N	\N	0101000000FC0700AA126F404088014DAF4D784140	3639
CTC131	1777.8299999999999	Waypoint	\N	CTC131	\N	\N	\N	010100000079F150A4136F4040770435514B784140	3640
CTC132	1777.8299999999999	Waypoint	\N	CTC132	\N	\N	\N	010100000026AF2432D97440409E0607D9C4844140	3641
CTC133	1792.49	Waypoint	\N	CTC133	\N	\N	\N	0101000000D0B53459116F4040590ABD0857784140	3642
CTC135	1816.28	Waypoint	\N	CTC135	\N	\N	\N	01010000004C3943970E6F40403AEF3E4848784140	3643
CTC136	1793.45	Waypoint	\N	CTC136	\N	\N	\N	010100000094F26CEC186F4040C8D8E0AF4F784140	3644
CTC137	1809.55	Waypoint	\N	CTC137	\N	\N	\N	0101000000CE8C53B4FF6E4040807F87F840784140	3645
CTC138	1810.03	Waypoint	\N	CTC138	\N	\N	\N	010100000050EC78FEFA6E4040AFCEC4834C784140	3646
CTC139	1825.9000000000001	Waypoint	\N	CTC139	\N	\N	\N	010100000018394325F96E404029BEDC5247784140	3647
CTC14	807.86699999999996	Waypoint	\N	CTC14	\N	\N	\N	010100000015EE6CFAF952404095AC6C1A397A4140	3648
CTC140	1793.45	Waypoint	\N	CTC140	\N	\N	\N	010100000065E2C896F46E40400EDAB04841784140	3649
CTC141	1840.8	Waypoint	\N	CTC141	\N	\N	\N	01010000005C678319016F404078F8B488FD774140	3650
CTC142	1828.0599999999999	Waypoint	\N	CTC142	\N	\N	\N	0101000000F7076CFCF96E40403DADD0771F784140	3651
CTC143	1842.24	Waypoint	\N	CTC143	\N	\N	\N	0101000000DE0F080C036F40402604E0DCF5774140	3652
CTC144	1822.77	Waypoint	\N	CTC144	\N	\N	\N	0101000000367DF7400B6F40404B17DCBB38784140	3653
CTC145	1830.46	Waypoint	\N	CTC145	\N	\N	\N	0101000000DAEDA6A00A6F4040C0688C5F03784140	3654
CTC146	1830.46	Waypoint	\N	CTC146	\N	\N	\N	01010000009072BB8D096F4040F4C1DC35FB774140	3655
CTC147	1841.52	Waypoint	\N	CTC147	\N	\N	\N	0101000000700FDF15036F404087D8006FF8774140	3656
CTC15	1756.9200000000001	Waypoint	\N	CTC15	\N	\N	\N	0101000000FD00C745456F40404ADC7C8664784140	3657
CTC150	1725.2	Waypoint	\N	CTC150	\N	\N	\N	010100000016A65CD23270404027EE50CB11764140	3658
CTC151	1717.75	Waypoint	\N	CTC151	\N	\N	\N	01010000003D03CC770E70404056F01874FB754140	3659
CTC152	1735.77	Waypoint	\N	CTC152	\N	\N	\N	0101000000AA411C142270404077135BC50B764140	3660
CTC153	1720.3900000000001	Waypoint	\N	CTC153	\N	\N	\N	010100000095FB106000704040230B899500764140	3661
CTC154	1711.74	Waypoint	\N	CTC154	\N	\N	\N	01010000003AD548B5097040404FD138A4F6754140	3662
CTC155	1724.72	Waypoint	\N	CTC155	\N	\N	\N	01010000002EB2F8A81A704040BD92C8640F764140	3663
CTC156	1715.3499999999999	Waypoint	\N	CTC156	\N	\N	\N	0101000000CA28DB2FFB6F40404F2EAC6CF6754140	3664
CTC157	1715.3499999999999	Waypoint	\N	CTC157	\N	\N	\N	0101000000E763B32D077040400382E0C1F4754140	3665
CTC158	1761.97	Waypoint	\N	CTC158	\N	\N	\N	0101000000D52F182902704040744328F61D764140	3666
CTC159	1717.51	Waypoint	\N	CTC159	\N	\N	\N	01010000003FE2940FFA6F4040302B7CCCF3754140	3667
CTC16	1755.24	Waypoint	\N	CTC16	\N	\N	\N	01010000001AEEAAA7596F404096253F8466784140	3668
CTC160	1708.6199999999999	Waypoint	\N	CTC160	\N	\N	\N	010100000019F27252047040408ECEBCC6F5754140	3669
CTC161	1748.03	Waypoint	\N	CTC161	\N	\N	\N	01010000008DB4085009704040359A3B7E18764140	3670
CTC162	1768.22	Waypoint	\N	CTC162	\N	\N	\N	01010000006469173BF06F40406F35FF7016764140	3671
CTC163	1702.6099999999999	Waypoint	\N	CTC163	\N	\N	\N	0101000000CA3DCC7A0B70404073CDA07EF0754140	3672
CTC165	1787.4400000000001	Waypoint	\N	CTC165	\N	\N	\N	01010000008E9897CBDF6F4040562E1C5417764140	3673
CTC166	1705.97	Waypoint	\N	CTC166	\N	\N	\N	0101000000D0EB246217704040EFBC9487F3754140	3674
CTC167	1748.03	Waypoint	\N	CTC167	\N	\N	\N	0101000000B88437FCD26F404005B1647508764140	3675
CTC168	1806.4300000000001	Waypoint	\N	CTC168	\N	\N	\N	01010000003F7F448DAE6F40406DB51C220D764140	3676
CTC169	1775.6700000000001	Waypoint	\N	CTC169	\N	\N	\N	01010000005CDC0C58EE6F40406443B3C21F764140	3677
CTC17	1749.71	Waypoint	\N	CTC17	\N	\N	\N	0101000000E37AEB2E566F4040E650DF6260784140	3678
CTC170	1775.4300000000001	Waypoint	\N	CTC170	\N	\N	\N	0101000000C40B5744E36F4040ED03984B0D764140	3679
CTC171	1848.01	Waypoint	\N	CTC171	\N	\N	\N	0101000000DD5ED7D1E66D404056CB386E3A774140	3680
CTC172	1832.3800000000001	Waypoint	\N	CTC172	\N	\N	\N	01010000004377EB2AD46D40403380DB063A774140	3681
CTC173	1775.4300000000001	Waypoint	\N	CTC173	\N	\N	\N	01010000007106D006D86D4040A7B19C084B774140	3682
CTC174	1870.3599999999999	Waypoint	\N	CTC174	\N	\N	\N	010100000013923786E16D40402001857541774140	3683
CTC175	1850.1700000000001	Waypoint	\N	CTC175	\N	\N	\N	01010000007D1DE71FC56D40406FAF883E38774140	3684
CTC176	1775.4300000000001	Waypoint	\N	CTC176	\N	\N	\N	01010000007106D006D86D4040A7B19C084B774140	3685
CTC177	1852.3299999999999	Waypoint	\N	CTC177	\N	\N	\N	01010000001F44DCE3DF6D404093FACE0943774140	3686
CTC178	1841.04	Waypoint	\N	CTC178	\N	\N	\N	0101000000AFCE2C67C16D40401360BB2838774140	3687
CTC179	1859.3	Waypoint	\N	CTC179	\N	\N	\N	0101000000E265A4E3CC6D4040AAEDB4A23E774140	3688
CTC18	1754.04	Waypoint	\N	CTC18	\N	\N	\N	010100000071C1005F5D6F4040E5FFA48D62784140	3689
CTC180	1867.71	Waypoint	\N	CTC180	\N	\N	\N	0101000000292BF08FE36D40403D071F1542774140	3690
CTC181	1837.9100000000001	Waypoint	\N	CTC181	\N	\N	\N	0101000000EFF61E03B96D4040D31F7B6C34774140	3691
CTC182	1859.54	Waypoint	\N	CTC182	\N	\N	\N	010100000023932B73B56D4040466E50723C774140	3692
CTC183	1870.3599999999999	Waypoint	\N	CTC183	\N	\N	\N	0101000000E9704CCDE06D4040C708645347774140	3693
CTC184	1848.97	Waypoint	\N	CTC184	\N	\N	\N	01010000005FD5B4E6226E40400771CC4F3D774140	3694
CTC185	1851.3699999999999	Waypoint	\N	CTC185	\N	\N	\N	0101000000B4BBD0F4436E404011B8B0993C774140	3695
CTC186	1867.95	Waypoint	\N	CTC186	\N	\N	\N	01010000003C6AC396DB6D4040757D179D49774140	3696
CTC19	1754.28	Waypoint	\N	CTC19	\N	\N	\N	01010000004B0540D55B6F4040E23AB7EA69784140	3697
CTC20	1761.73	Waypoint	\N	CTC20	\N	\N	\N	01010000000F891091396F40404885ACCC66784140	3698
CTC2007 10	1689.6300000000001	Waypoint	\N	CTC2007 10	\N	\N	\N	01010000003E0724CE7B7240403DE05480FC774140	3699
CTC2007 11	1684.8199999999999	Waypoint	\N	CTC2007 11	\N	\N	\N	010100000040BEA05F7D724040753EFFE7F7774140	3700
CTC2007 12	1679.0599999999999	Waypoint	\N	CTC2007 12	\N	\N	\N	0101000000085184F57B7240407FD634A3EA774140	3701
CTC2007 9	1697.8	Waypoint	\N	CTC2007 9	\N	\N	\N	0101000000B68B57C37F7240402E631F7103784140	3702
CTC21	1756.9200000000001	Waypoint	\N	CTC21	\N	\N	\N	0101000000B90C8B93336F404028EB28E865784140	3703
CTC22	1772.54	Waypoint	\N	CTC22	\N	\N	\N	010100000095732C66396F4040E68BC0CF6D784140	3704
CTC23	1799.22	Waypoint	\N	CTC23	\N	\N	\N	0101000000E76DA420436F4040B5C3FC687A784140	3705
CTC24	1789.8499999999999	Waypoint	\N	CTC24	\N	\N	\N	0101000000D3C54824416F4040C1A1103180784140	3706
CTC25	1787.2	Waypoint	\N	CTC25	\N	\N	\N	0101000000775CC4DF456F40403C110B4F84784140	3707
CTC26	1789.1300000000001	Waypoint	\N	CTC26	\N	\N	\N	010100000027CD30FF326F4040CD6234BB86784140	3708
CTC27	1782.6400000000001	Waypoint	\N	CTC27	\N	\N	\N	0101000000E695F0262D6F40409549AFFB7E784140	3709
CTC28	1785.04	Waypoint	\N	CTC28	\N	\N	\N	0101000000BC05E793226F40404156DB4086784140	3710
CTC29	1784.0799999999999	Waypoint	\N	CTC29	\N	\N	\N	0101000000ADD6BCBC166F40405E8B34F687784140	3711
CTC30	1798.74	Waypoint	\N	CTC30	\N	\N	\N	010100000090892C83FE6E4040A11C8BC191784140	3712
CTC300	1800.1800000000001	Waypoint	\N	CTC300	\N	\N	\N	01010000008E6604D2406F40409EA6E03A10784140	3713
CTC302	1699.72	Waypoint	\N	CTC302	\N	\N	\N	0101000000DBFC2C87376E4040B876AC96D7794140	3714
CTC304	1726.4000000000001	Waypoint	\N	CTC304	\N	\N	\N	01010000007A7CA39F2E6E4040BED78F9BC1794140	3715
CTC306	1734.8099999999999	Waypoint	\N	CTC306	\N	\N	\N	0101000000AE4F577C256E40401A35DBECBA794140	3716
CTC31	1804.27	Waypoint	\N	CTC31	\N	\N	\N	01010000008D4F70CDF76E40406F71DB5E95784140	3717
CTC310	1751.6300000000001	Waypoint	\N	CTC310	\N	\N	\N	0101000000AAAF6FA6106E4040D4535867C4794140	3718
CTC311	1621.8599999999999	Waypoint	\N	CTC311	\N	\N	\N	0101000000772A18498671404096984BD03D764140	3719
CTC313	1622.8199999999999	Waypoint	\N	CTC313	\N	\N	\N	0101000000F710B0FD7472404074D14463A3764140	3720
CTC315	1622.8199999999999	Waypoint	\N	CTC315	\N	\N	\N	0101000000AA9B8BBF6D7240409E06443C9D764140	3721
CTC317	1629.3099999999999	Waypoint	\N	CTC317	\N	\N	\N	0101000000F5E2087578724040516B53A290764140	3722
CTC318	1630.27	Waypoint	\N	CTC318	\N	\N	\N	010100000043FE2233DA724040C064FC5E77764140	3723
CTC319	1612.96	Waypoint	\N	CTC319	\N	\N	\N	01010000004FD313088E724040C907D5B7A0764140	3724
CTC320	1628.8299999999999	Waypoint	\N	CTC320	\N	\N	\N	0101000000B466D32DD57240403F37B89D8B764140	3725
CTC322	1629.3099999999999	Waypoint	\N	CTC322	\N	\N	\N	01010000006490604FA0724040DB16231A76764140	3726
CTC324	1628.1099999999999	Waypoint	\N	CTC324	\N	\N	\N	01010000005BF1AA5A94724040D8D7C4467C764140	3727
CTC326	1618.97	Waypoint	\N	CTC326	\N	\N	\N	0101000000868B0FC57A72404036D11C965C764140	3728
CTC328	1619.9300000000001	Waypoint	\N	CTC328	\N	\N	\N	0101000000990D9F657772404031321F1561764140	3729
CTC330	1623.0599999999999	Waypoint	\N	CTC330	\N	\N	\N	01010000008C32E8726D724040FA419FC062764140	3730
CTC332	1611.76	Waypoint	\N	CTC332	\N	\N	\N	01010000006365946466724040FCD32C2176764140	3731
CTC334	1623.0599999999999	Waypoint	\N	CTC334	\N	\N	\N	01010000008FDCAC5797724040D45337599B764140	3732
CTC336	1617.77	Waypoint	\N	CTC336	\N	\N	\N	010100000067823C1676724040FC752303AB764140	3733
CTC338	1567.54	Waypoint	\N	CTC338	\N	\N	\N	0101000000813D63B572724040CF95E08BB1764140	3734
CTC340	1604.3099999999999	Waypoint	\N	CTC340	\N	\N	\N	010100000016FFEE52677240400E9364AEB0764140	3735
CTC342	1584.1300000000001	Waypoint	\N	CTC342	\N	\N	\N	0101000000920E0CA96D7240405F47C3F9B9764140	3736
CTC344	1579.5599999999999	Waypoint	\N	CTC344	\N	\N	\N	010100000080B4A44863724040DFB58B36B5764140	3737
CTC346	1602.8699999999999	Waypoint	\N	CTC346	\N	\N	\N	01010000004BE13741B87240403B3BDCB84C764140	3738
CTC51	1583.8900000000001	Waypoint	\N	CTC51	\N	\N	\N	0101000000F53834AE7D71404089A980BD3A764140	3739
CTC52	1583.4000000000001	Waypoint	\N	CTC52	\N	\N	\N	01010000004E17ABCB7E714040AC88F85935764140	3740
CTC54	1583.8900000000001	Waypoint	\N	CTC54	\N	\N	\N	01010000004DC8B8F646724040124504C0D1764140	3741
CTC55A	1545.9100000000001	Waypoint	\N	CTC55A	\N	\N	\N	010100000021CA84503F7240403884046ED8764140	3742
CTC55B	1549.04	Waypoint	\N	CTC55B	\N	\N	\N	01010000004834F3773E724040699503B7DA764140	3743
CTC56	1574.03	Waypoint	\N	CTC56	\N	\N	\N	0101000000EFC584896072404063228C3CDC764140	3744
CTC57	1549.04	Waypoint	\N	CTC57	\N	\N	\N	0101000000FDE8B0CF57724040B687E8D0DC764140	3745
CTC58	1612.48	Waypoint	\N	CTC58	\N	\N	\N	0101000000BBD9BCFD3B724040DC64BCFCFA764140	3746
CTC59	1645.1700000000001	Waypoint	\N	CTC59	\N	\N	\N	0101000000E072F7643E724040D16DB7941B774140	3747
CTC60 JDW	1642.77	Waypoint	\N	CTC60 JDW	\N	\N	\N	010100000061DB1C9337724040C57B8FAC1C774140	3748
CTC61	1563.9400000000001	Waypoint	\N	CTC61	\N	\N	\N	010100000021B67030467240402A9AFF3EC0764140	3749
CTC62	1564.1800000000001	Waypoint	\N	CTC62	\N	\N	\N	01010000009076FFFF4F7240404F679BD9B6764140	3750
CTC63	1586.77	Waypoint	\N	CTC63	\N	\N	\N	010100000005EDD87F527240405A6B9FC3B9764140	3751
CTC64	1589.8900000000001	Waypoint	\N	CTC64	\N	\N	\N	0101000000512B637B6172404049B4A0D4B7764140	3752
CTC65	1617.53	Waypoint	\N	CTC65	\N	\N	\N	010100000064B12C1C71724040E907A882A7764140	3753
CTC67	423.58300000000003	Waypoint	\N	CTC67	\N	\N	\N	010100000015999041347140402B0C7836F7754140	3754
CTC68A	1620.1800000000001	Waypoint	\N	CTC68A	\N	\N	\N	01010000009974BC4B67714040BFEBF402FA754140	3755
CTC68B	1622.0999999999999	Waypoint	\N	CTC68B	\N	\N	\N	010100000078BD48296771404057C14C34F8754140	3756
CTC69	1594.9400000000001	Waypoint	\N	CTC69	\N	\N	\N	0101000000008DACDE5C714040ECE028C503764140	3757
CTC70	1619.21	Waypoint	\N	CTC70	\N	\N	\N	01010000005B3EB3A04771404058243F4502764140	3758
CTC71	1596.3800000000001	Waypoint	\N	CTC71	\N	\N	\N	010100000073164CB57C71404063337B31F8754140	3759
CTC72	1619.21	Waypoint	\N	CTC72	\N	\N	\N	0101000000CD922BB551714040D427476EFE754140	3760
CTC73	1603.1099999999999	Waypoint	\N	CTC73	\N	\N	\N	0101000000208E5CD47A714040AC376013F6754140	3761
CTC80	1842.24	Waypoint	\N	CTC80	\N	\N	\N	0101000000AF2B54A17B6F404024F73CCBE8764140	3762
CTC81A	1850.4100000000001	Waypoint	\N	CTC81A	\N	\N	\N	01010000000FA0C85B7A6F40402C58D843E3764140	3763
CTC81B	1858.3399999999999	Waypoint	\N	CTC81B	\N	\N	\N	0101000000EC4EC8E5776F404018769CCBE2764140	3764
CTC82	1850.8900000000001	Waypoint	\N	CTC82	\N	\N	\N	0101000000C6B2546F716F40406D4C5C82E4764140	3765
CTC83	1831.9000000000001	Waypoint	\N	CTC83	\N	\N	\N	010100000047082C0C936F404017C25849E5764140	3766
CTC84	1837.6700000000001	Waypoint	\N	CTC84	\N	\N	\N	0101000000A73264F3736F4040ECF42801D6764140	3767
CTC85	1839.5899999999999	Waypoint	\N	CTC85	\N	\N	\N	0101000000563F48AC8B6F4040F883473CD5764140	3768
CTC86	1856.4200000000001	Waypoint	\N	CTC86	\N	\N	\N	010100000008BFDC40646E4040D360DB214C774140	3769
CTC87	1867.95	Waypoint	\N	CTC87	\N	\N	\N	0101000000CB1D84200A6E404061FEE4D148774140	3770
CTC88	1867.23	Waypoint	\N	CTC88	\N	\N	\N	01010000007FA060EF136E40404F47F30C49774140	3771
CTC89	1861.46	Waypoint	\N	CTC89	\N	\N	\N	0101000000921914720A6E40404255CB244A774140	3772
CTC90	1868.6700000000001	Waypoint	\N	CTC90	\N	\N	\N	01010000008C15C8FB176E4040A463CC324B774140	3773
CTC91	1869.3900000000001	Waypoint	\N	CTC91	\N	\N	\N	0101000000690105D70F6E4040F075E7C352774140	3774
CTC92	1862.4300000000001	Waypoint	\N	CTC92	\N	\N	\N	0101000000A97E6B48186E4040BD640C4148774140	3775
CTC93	1869.1500000000001	Waypoint	\N	CTC93	\N	\N	\N	010100000021005904166E4040722748D45A774140	3776
CTC95	1885.02	Waypoint	\N	CTC95	\N	\N	\N	0101000000C83FBC792D6E4040E88EAF5157774140	3777
CTR1	1055.1600000000001	Waypoint	\N	CTR1	\N	\N	\N	01010000009AAAAE55982B38403D2A8C9A20A74140	3778
CTR10	1107.0799999999999	Waypoint	\N	CTR10	\N	\N	\N	0101000000D4A45891782B38407ECCD41215A74140	3779
CTR12	1125.5799999999999	Waypoint	\N	CTR12	\N	\N	\N	0101000000521F67EB772B38409C3D775204A74140	3780
CTR16	1157.79	Waypoint	\N	CTR16	\N	\N	\N	0101000000714C3F76512B38404412678CE9A64140	3781
CTR17	1080.4000000000001	Waypoint	\N	CTR17	\N	\N	\N	010100000085A1E0F2702B38405E72D4B814A74140	3782
CTR2	1071.75	Waypoint	\N	CTR2	\N	\N	\N	0101000000BD5EB8789A2B384091C9C8AA1FA74140	3783
CTR3	1068.1400000000001	Waypoint	\N	CTR3	\N	\N	\N	01010000007C2AB1AF982B3840649A90A61FA74140	3784
CTR4	1065.5	Waypoint	\N	CTR4	\N	\N	\N	0101000000EEDABF618B2B3840E3885FF81BA74140	3785
CTR5	1073.1900000000001	Waypoint	\N	CTR5	\N	\N	\N	01010000009AC6D804762B3840C6394F141FA74140	3786
CTR8	1095.54	Waypoint	\N	CTR8	\N	\N	\N	0101000000C2ED66CC782B384011A6BBFA17A74140	3787
CVP20	1346.4400000000001	Waypoint	\N	CVP20	\N	\N	\N	0101000000ED8E69C6521B3840D24FBCF3D9A04140	3788
CVP22	1337.55	Waypoint	\N	CVP22	\N	\N	\N	0101000000F7AF81B44C1B3840594B8F13D7A04140	3789
CVP24	1328.9000000000001	Waypoint	\N	CVP24	\N	\N	\N	01010000009ED789DF551B384041558707CDA04140	3790
CVP26	1330.5799999999999	Waypoint	\N	CVP26	\N	\N	\N	010100000034143F126B1B38406822FFDBCBA04140	3791
CVP30	1337.79	Waypoint	\N	CVP30	\N	\N	\N	01010000009DACE1BA131B384031C2D4C9EBA04140	3792
CVP31	1348.6099999999999	Waypoint	\N	CVP31	\N	\N	\N	01010000006E7938C3851B3840541454F9C3A04140	3793
CVP32	1334.6700000000001	Waypoint	\N	CVP32	\N	\N	\N	0101000000A7C73FFE151B3840955BF028E9A04140	3794
CVP34	1331.78	Waypoint	\N	CVP34	\N	\N	\N	01010000000A4D473D1A1B38406C1AB85CEDA04140	3795
CYP 1SPOT	1090.49	Waypoint	\N	CYP 1SPOT	\N	\N	\N	01010000006F4156BA321B3840B0000937979F4140	3796
DST11	1402.2	Waypoint	\N	DST11	\N	\N	\N	010100000063F3340A907B4040E91748F7437A4140	3797
DST13	1414.9300000000001	Waypoint	\N	DST13	\N	\N	\N	0101000000B293A3FB977B4040B9B1B012397A4140	3798
DST17	1409.4100000000001	Waypoint	\N	DST17	\N	\N	\N	010100000086B003AFA27B4040C80B60FF327A4140	3799
DST2	1357.02	Waypoint	\N	DST2	\N	\N	\N	01010000005DFAC2244F7B404074FFBE6A587A4140	3800
DST23	1398.5899999999999	Waypoint	\N	DST23	\N	\N	\N	0101000000AC306412C17B4040A2BFA7A4547A4140	3801
DST25	1383.45	Waypoint	\N	DST25	\N	\N	\N	0101000000F4361074EF7B404008B3CCCC537A4140	3802
DST26	1423.1099999999999	Waypoint	\N	DST26	\N	\N	\N	0101000000A5134BC2EC7B404053EBD77F587A4140	3803
DST29	1431.04	Waypoint	\N	DST29	\N	\N	\N	01010000009964DF73F27B4040559978F3537A4140	3804
DST3	1347.4000000000001	Waypoint	\N	DST3	\N	\N	\N	010100000009442CBB507B404044423306587A4140	3805
DST32	1369.27	Waypoint	\N	DST32	\N	\N	\N	010100000025C96452727B4040639C5726507A4140	3806
DST5	1368.79	Waypoint	\N	DST5	\N	\N	\N	01010000002568B7895F7B4040BD911B734E7A4140	3807
DST7	1388.98	Waypoint	\N	DST7	\N	\N	\N	010100000031D62094837B40405738A764497A4140	3808
GUL ROAD	1398.8299999999999	Waypoint	\N	GUL ROAD	\N	\N	\N	0101000000506BB11392193840E5B15CF21CA14140	3809
HAGANA	1398.8299999999999	Waypoint	\N	HAGANA	\N	\N	\N	0101000000F2F536AF0A844140C2341B4AFE5D4040	3810
ICHA11	482.464	Waypoint	\N	ICHA11	\N	\N	\N	01010000001DF047F1F48341403B517FAE175E4040	3811
ICHA12	481.02199999999999	Waypoint	\N	ICHA12	\N	\N	\N	0101000000B979BFDEF883414096B228C61B5E4040	3812
ICT 20	405.07799999999997	Waypoint	\N	ICT 20	\N	\N	\N	010100000094E764237C814140B27B04948C614040	3813
ICT10	395.22500000000002	Waypoint	\N	ICT10	\N	\N	\N	0101000000C6765C2C03814140731378D19E614040	3814
ICT11	396.90699999999998	Waypoint	\N	ICT11	\N	\N	\N	01010000001AA2B47E0D814140423F9F3DA1614040	3815
ICT12	417.57499999999999	Waypoint	\N	ICT12	\N	\N	\N	0101000000F10E88C150814140EB315CE09C614040	3816
ICT13	416.61399999999998	Waypoint	\N	ICT13	\N	\N	\N	0101000000DECC7BAB54814140DF160BC79C614040	3817
ICT14	404.35700000000003	Waypoint	\N	ICT14	\N	\N	\N	0101000000F291C7E555814140A829E8479A614040	3818
ICT15	417.09500000000003	Waypoint	\N	ICT15	\N	\N	\N	010100000048B41F54628141403BCF84989E614040	3819
ICT16	420.21899999999999	Waypoint	\N	ICT16	\N	\N	\N	010100000029511F16648141402ED4809299614040	3820
ICT17	383.92899999999997	Waypoint	\N	ICT17	\N	\N	\N	0101000000A00F574D5E814140C2F380E99C614040	3821
ICT18	402.19400000000002	Waypoint	\N	ICT18	\N	\N	\N	01010000009BD9F85D628141405062CB8398614040	3822
ICT19	416.85399999999998	Waypoint	\N	ICT19	\N	\N	\N	0101000000D381DF987B81414005CD4C0894614040	3823
ICT9	384.17000000000002	Waypoint	\N	ICT9	\N	\N	\N	010100000091FA9CCF018141408EEEC7BD9E614040	3824
IJF1	563.69399999999996	Waypoint	\N	IJF1	\N	\N	\N	01010000003DC35F89438541402BCB5E56ABC93F40	3825
IMR 2	633.63	Waypoint	\N	IMR 2	\N	\N	\N	010100000069D5A74F44854140C53931BE44CB3F40	3826
IMR 3	635.31200000000001	Waypoint	\N	IMR 3	\N	\N	\N	01010000000790FFD44885414039647FB03ECB3F40	3827
IMR 4	632.18799999999999	Waypoint	\N	IMR 4	\N	\N	\N	01010000003ED5F3FE578541407C6A976454CB3F40	3828
IMR 5	632.90899999999999	Waypoint	\N	IMR 5	\N	\N	\N	01010000000CA7047856854140F0B2C95B5ACB3F40	3829
IMR1	639.87800000000004	Waypoint	\N	IMR1	\N	\N	\N	0101000000645054D341854140C22208553FCB3F40	3830
IMR10	639.87800000000004	Waypoint	\N	IMR10	\N	\N	\N	0101000000DBCCAC9B4E854140EBC98F0741CB3F40	3831
IMR11	661.26800000000003	Waypoint	\N	IMR11	\N	\N	\N	0101000000862238895C854140D287EF432CCB3F40	3832
IMR12	661.26800000000003	Waypoint	\N	IMR12	\N	\N	\N	01010000007E43FF095085414074B040EC33CB3F40	3833
IMR13	638.91700000000003	Waypoint	\N	IMR13	\N	\N	\N	010100000091FD12425E85414022F4EFD73DCB3F40	3834
IMR14	623.05499999999995	Waypoint	\N	IMR14	\N	\N	\N	01010000004A0959C785854140E360972A16CB3F40	3835
IMR6	635.55200000000002	Waypoint	\N	IMR6	\N	\N	\N	01010000002CE414945785414055CF001965CB3F40	3836
IMR7	617.04700000000003	Waypoint	\N	IMR7	\N	\N	\N	0101000000D45C7B5A5685414096A0FE3467CB3F40	3837
IMR8	626.17999999999995	Waypoint	\N	IMR8	\N	\N	\N	0101000000C57353565585414038379EB663CB3F40	3838
IMR9	638.43700000000001	Waypoint	\N	IMR9	\N	\N	\N	010100000034C813F74E854140B7186E863CCB3F40	3839
IMT 1	420.459	Waypoint	\N	IMT 1	\N	\N	\N	01010000007BF538A6438141406CF23B1DAD614040	3840
IMT 2	400.27199999999999	Waypoint	\N	IMT 2	\N	\N	\N	0101000000BC5BE74C0B8141406B01BB49B5614040	3841
IMT 3	394.02300000000002	Waypoint	\N	IMT 3	\N	\N	\N	01010000004315DBFD1A814140258D9C489A614040	3842
IMT 4	392.34100000000001	Waypoint	\N	IMT 4	\N	\N	\N	0101000000DF98AFDC1C8141406BC6F0789D614040	3843
IMT 5	401.95400000000001	Waypoint	\N	IMT 5	\N	\N	\N	0101000000E1EF7F961A814140F6EC002299614040	3844
IMT 6	401.47300000000001	Waypoint	\N	IMT 6	\N	\N	\N	01010000001922BB930E8141408CE3B381A3614040	3845
IMT6	405.07799999999997	Waypoint	\N	IMT6	\N	\N	\N	01010000008F8FB8F50A814140364D7755A2614040	3846
IMT7	399.55099999999999	Waypoint	\N	IMT7	\N	\N	\N	010100000016FD13EF0F814140A14B682DAD614040	3847
JCF1	399.55099999999999	Waypoint	\N	JCF1	\N	\N	\N	01010000002A848C824B854140737C4F0D7DC93F40	3848
JCM1	825.41099999999994	Waypoint	\N	JCM1	\N	\N	\N	0101000000F1A978305D9D4140A5A71E229BC63F40	3849
JMC1	504.57400000000001	Waypoint	\N	JMC1	\N	\N	\N	0101000000CE7B0B075C9D4140B37D0F31BAC63F40	3850
LOWCYPSITE	234.68600000000001	Waypoint	\N	LOWCYPSITE	\N	\N	\N	0101000000ADF6B0170A343840B9CFE0A5CAAC4140	3851
PSEMBIN	125.337	Waypoint	\N	PSEMBIN	\N	\N	\N	010100000041BF8CB501AC4040B13AA79603614140	3852
PSEMFLDWL	121.97199999999999	Waypoint	\N	PSEMFLDWL	\N	\N	\N	0101000000162827AFD5AB40401AB1C35618614140	3853
PSEMSHDS1	122.693	Waypoint	\N	PSEMSHDS1	\N	\N	\N	010100000093BF3CE3C9AB4040FDF82A7D11614140	3854
PSEMSOC+Q	111.398	Waypoint	\N	PSEMSOC+Q	\N	\N	\N	0101000000A5AB9C3F2BAC404038F6F62B00614140	3855
PSEMSWHRL	121.251	Waypoint	\N	PSEMSWHRL	\N	\N	\N	0101000000017F4CF9FDAB40409562B47FFF604140	3856
PSEMTOMB	113.801	Waypoint	\N	PSEMTOMB	\N	\N	\N	0101000000EBEDCC8D34AC40408DC714C50E614140	3857
PSEMUPPOT	123.17400000000001	Waypoint	\N	PSEMUPPOT	\N	\N	\N	010100000063EF62EDF1AB4040C6A0FF5D20614140	3858
PSEMWALL	125.81699999999999	Waypoint	\N	PSEMWALL	\N	\N	\N	010100000080E6B8C2D7AB40404190F36623614140	3859
PSEMWALL2	121.011	Waypoint	\N	PSEMWALL2	\N	\N	\N	01010000005A75B3C5C9AB40409F814CC314614140	3860
RTM1	1071.27	Waypoint	\N	RTM1	\N	\N	\N	01010000002220E0C21A1B3840A56A5326899F4140	3861
RTM2	1099.1400000000001	Waypoint	\N	RTM2	\N	\N	\N	0101000000789889314F1B384030A31B0B919F4140	3862
RTM3	1097.9400000000001	Waypoint	\N	RTM3	\N	\N	\N	01010000000479806B4A1B38409A1B94AF929F4140	3863
RTM5	1094.0999999999999	Waypoint	\N	RTM5	\N	\N	\N	01010000002CEFA54E121B3840EE60A330989F4140	3864
SCV1	1182.3	Waypoint	\N	SCV1	\N	\N	\N	01010000008E792C9C755740408A8DDC472C804140	3865
SCV10	1174.8499999999999	Waypoint	\N	SCV10	\N	\N	\N	010100000069EB700CB257404046DA004B32804140	3866
SCV100	1109.96	Waypoint	\N	SCV100	\N	\N	\N	01010000004967C8C7DA574040EACDF487307E4140	3867
SCV106	1115.73	Waypoint	\N	SCV106	\N	\N	\N	01010000005E46A76ADF57404001219422327E4140	3868
SCV107	1103.71	Waypoint	\N	SCV107	\N	\N	\N	01010000008F89AB02E95740401CEFE48C0E7E4140	3869
SCV108	1132.79	Waypoint	\N	SCV108	\N	\N	\N	010100000010808F2C265840400A146F6CCF7E4140	3870
SCV11	1183.74	Waypoint	\N	SCV11	\N	\N	\N	0101000000EA318FD1815740404853636022804140	3871
SCV110	1137.8399999999999	Waypoint	\N	SCV110	\N	\N	\N	01010000006600F1B8FD5740403F8BE764507E4140	3872
SCV12	1103.71	Waypoint	\N	SCV12	\N	\N	\N	01010000004C0C2FAC8E57404054289B0A22804140	3873
SCV120	1073.9100000000001	Waypoint	\N	SCV120	\N	\N	\N	0101000000A4AF803F05584040FFA778E28D7E4140	3874
SCV121	1261.6099999999999	Waypoint	\N	SCV121	\N	\N	\N	0101000000DF28777423584040A908A76DD27E4140	3875
SCV122	1127.5	Waypoint	\N	SCV122	\N	\N	\N	01010000008476938721584040C845DB4FCB7E4140	3876
SCV123	1127.5	Waypoint	\N	SCV123	\N	\N	\N	0101000000B27BB80507584040D7074DA3AD7E4140	3877
SCV125	1153.7	Waypoint	\N	SCV125	\N	\N	\N	0101000000A80FABDFCF574040E6A638D4907E4140	3878
SCV126	1159.23	Waypoint	\N	SCV126	\N	\N	\N	01010000001EDD5419D05740404780FB83867E4140	3879
SCV13	1190.71	Waypoint	\N	SCV13	\N	\N	\N	0101000000AF34C32293574040F95224D11E804140	3880
SCV130	1242.3800000000001	Waypoint	\N	SCV130	\N	\N	\N	0101000000F9D734E7C75740405A7537FEED7E4140	3881
SCV131	1264.73	Waypoint	\N	SCV131	\N	\N	\N	01010000006153178BC757404066844F24EC7E4140	3882
SCV134	1251.03	Waypoint	\N	SCV134	\N	\N	\N	0101000000ED792734C3574040E28AB422E47E4140	3883
SCV135	1242.1400000000001	Waypoint	\N	SCV135	\N	\N	\N	01010000003F1F0753C45740408593B8B7F07E4140	3884
SCV136	1224.1199999999999	Waypoint	\N	SCV136	\N	\N	\N	0101000000CC31DF15CF574040CA06112AEA7E4140	3885
SCV137	1216.6600000000001	Waypoint	\N	SCV137	\N	\N	\N	0101000000957DBFE7B3574040B011F372F97E4140	3886
SCV138	1146.73	Waypoint	\N	SCV138	\N	\N	\N	010100000008153B6B0E584040FF95077CCD7E4140	3887
SCV14	1228.6800000000001	Waypoint	\N	SCV14	\N	\N	\N	0101000000DF0563A78C5740407490303C11804140	3888
SCV15	1184.9400000000001	Waypoint	\N	SCV15	\N	\N	\N	0101000000B04B105290574040B05CD4C60F804140	3889
SCV16	1159.47	Waypoint	\N	SCV16	\N	\N	\N	0101000000D53763AAB657404053886BA81D804140	3890
SCV17	1157.3	Waypoint	\N	SCV17	\N	\N	\N	0101000000333B7442B4574040E203A89A3A804140	3891
SCV18	1157.3	Waypoint	\N	SCV18	\N	\N	\N	0101000000B389EF6BB4574040986BCC493A804140	3892
SCV2	1211.3800000000001	Waypoint	\N	SCV2	\N	\N	\N	01010000009BFF6EB96E574040B3B124D628804140	3893
SCV21	1187.3499999999999	Waypoint	\N	SCV21	\N	\N	\N	0101000000348D446D7D574040D0DCC4420D804140	3894
SCV25	1162.3499999999999	Waypoint	\N	SCV25	\N	\N	\N	0101000000EB018BADF457404087C328C148804140	3895
SCV26	1392.3399999999999	Waypoint	\N	SCV26	\N	\N	\N	0101000000DFEF40321757404012C4A4B85D7F4140	3896
SCV27	1397.1500000000001	Waypoint	\N	SCV27	\N	\N	\N	0101000000AB16F77020574040AE7E20045A7F4140	3897
SCV28	1400.52	Waypoint	\N	SCV28	\N	\N	\N	0101000000F6F18E5B25574040C414A0FE577F4140	3898
SCV29	1389.46	Waypoint	\N	SCV29	\N	\N	\N	0101000000AB65ECFD265740408E4110E8587F4140	3899
SCV3	1389.46	Waypoint	\N	SCV3	\N	\N	\N	01010000000502901F19724040F0D85854C7014240	3900
SCV30	1399.0699999999999	Waypoint	\N	SCV30	\N	\N	\N	010100000011F940882A574040FD899795507F4140	3901
SCV31	1413.49	Waypoint	\N	SCV31	\N	\N	\N	0101000000B4F2D21A31574040350E25F5467F4140	3902
SCV32	1414.6900000000001	Waypoint	\N	SCV32	\N	\N	\N	0101000000AB57572638574040863668FE447F4140	3903
SCV33	1411.0899999999999	Waypoint	\N	SCV33	\N	\N	\N	0101000000118E38E83B574040782DF3E6487F4140	3904
SCV34	1425.75	Waypoint	\N	SCV34	\N	\N	\N	0101000000B19684093857404028FF0CCF487F4140	3905
SCV35	1401.96	Waypoint	\N	SCV35	\N	\N	\N	0101000000100E3FFD3C574040E48D10413C7F4140	3906
SCV36	1392.3399999999999	Waypoint	\N	SCV36	\N	\N	\N	0101000000567498ED12574040FE15B3D75B7F4140	3907
SCV37	1394.51	Waypoint	\N	SCV37	\N	\N	\N	0101000000C82227F520574040F30C0DD0657F4140	3908
SCV38	1405.5599999999999	Waypoint	\N	SCV38	\N	\N	\N	01010000008908B39422574040CDFE1080577F4140	3909
SCV39	1388.02	Waypoint	\N	SCV39	\N	\N	\N	01010000009ECAB8951D5740402A0B0B605D7F4140	3910
SCV4	1191.1900000000001	Waypoint	\N	SCV4	\N	\N	\N	0101000000B808590469574040CD1211BC29804140	3911
SCV40	1396.9100000000001	Waypoint	\N	SCV40	\N	\N	\N	0101000000BF13C742335740404E291C323F7F4140	3912
SCV41	1407.73	Waypoint	\N	SCV41	\N	\N	\N	0101000000A39A80C9C7564040C562DB6F9B7F4140	3913
SCV42	1398.1099999999999	Waypoint	\N	SCV42	\N	\N	\N	0101000000BF460476C4564040431D602A997F4140	3914
SCV43	1400.28	Waypoint	\N	SCV43	\N	\N	\N	0101000000D4FA749FD0564040C53333F68F7F4140	3915
SCV44	1414.9300000000001	Waypoint	\N	SCV44	\N	\N	\N	0101000000482E9FAFD0564040073CCB548A7F4140	3916
SCV45	1394.75	Waypoint	\N	SCV45	\N	\N	\N	010100000081B20849CF564040550BF95B937F4140	3917
SCV46	1394.03	Waypoint	\N	SCV46	\N	\N	\N	0101000000ABEDE4DB145740405E7070635F7F4140	3918
SCV47	1385.8599999999999	Waypoint	\N	SCV47	\N	\N	\N	0101000000E550243701574040326704BB647F4140	3919
SCV48	1378.4100000000001	Waypoint	\N	SCV48	\N	\N	\N	01010000004C6914CAF6564040DF7B0B2D637F4140	3920
SCV49	1380.3299999999999	Waypoint	\N	SCV49	\N	\N	\N	0101000000D301F5D8E9564040BEFF7877707F4140	3921
SCV50	1369.03	Waypoint	\N	SCV50	\N	\N	\N	010100000064B234C7245740405F3F483F6F7F4140	3922
SCV51	1390.6600000000001	Waypoint	\N	SCV51	\N	\N	\N	0101000000408224E4EA564040FEB0401C6D7F4140	3923
SCV52	1401.48	Waypoint	\N	SCV52	\N	\N	\N	0101000000F9574FE0EF564040DFE7F0F7687F4140	3924
SCV53	1401.48	Waypoint	\N	SCV53	\N	\N	\N	0101000000BC96071EDC5640407055744C9F7F4140	3925
SCV54	1387.0599999999999	Waypoint	\N	SCV54	\N	\N	\N	0101000000F3E9901FDA56404085F72C3A9F7F4140	3926
SCV55	1378.1600000000001	Waypoint	\N	SCV55	\N	\N	\N	01010000000AE0BCF1DB564040DAB0FCB2A17F4140	3927
SCV56	1399.55	Waypoint	\N	SCV56	\N	\N	\N	0101000000C22ECC3A0A5740400418A369617F4140	3928
SCV57	1382.97	Waypoint	\N	SCV57	\N	\N	\N	01010000008830FF9A0C574040FFCF8C3E597F4140	3929
SCV58	1382.97	Waypoint	\N	SCV58	\N	\N	\N	0101000000289C546908574040D6023930527F4140	3930
SCV59	1382.97	Waypoint	\N	SCV59	\N	\N	\N	0101000000280B806D14574040C6DDD4CB657F4140	3931
SCV6	1123.1800000000001	Waypoint	\N	SCV6	\N	\N	\N	0101000000BF22801A855740407F5F307352804140	3932
SCV60	1376	Waypoint	\N	SCV60	\N	\N	\N	01010000001EF6A0721C574040FECD305A6C7F4140	3933
SCV61	1376	Waypoint	\N	SCV61	\N	\N	\N	010100000078C59863155740409AF49E9A707F4140	3934
SCV62A	1376	Waypoint	\N	SCV62A	\N	\N	\N	010100000093C6B4AB1A574040C20A69ED737F4140	3935
SCV62B	1416.6199999999999	Waypoint	\N	SCV62B	\N	\N	\N	0101000000B68B3B6ED0564040A41F4C0B907F4140	3936
SCV63	1361.8199999999999	Waypoint	\N	SCV63	\N	\N	\N	010100000075D518812C5740407EEEE034787F4140	3937
SCV65	1379.1300000000001	Waypoint	\N	SCV65	\N	\N	\N	01010000008BD1A08CE4564040BE1BD35FA47F4140	3938
SCV66	1397.3900000000001	Waypoint	\N	SCV66	\N	\N	\N	010100000087DBE8363D5740407225840C397F4140	3939
SCV67	1404.3599999999999	Waypoint	\N	SCV67	\N	\N	\N	01010000007C11E98E3057404037F36CD53C7F4140	3940
SCV69	1237.3299999999999	Waypoint	\N	SCV69	\N	\N	\N	01010000000AA6A4CBBE574040279A6068107F4140	3941
SCV69 A	1218.3499999999999	Waypoint	\N	SCV69 A	\N	\N	\N	0101000000B6E3D46EBE574040AD3E63CE0F7F4140	3942
SCV7	1131.5899999999999	Waypoint	\N	SCV7	\N	\N	\N	0101000000C9EFDC978D574040AE85689354804140	3943
SCV70	1211.3800000000001	Waypoint	\N	SCV70	\N	\N	\N	0101000000DF0A6336C1574040E1D4F0F5017F4140	3944
SCV71	1182.78	Waypoint	\N	SCV71	\N	\N	\N	0101000000890FB44ED1574040CC72BB92027F4140	3945
SCV72	1182.78	Waypoint	\N	SCV72	\N	\N	\N	0101000000A81BC00CDA57404075EB142FFF7E4140	3946
SCV73	1231.0899999999999	Waypoint	\N	SCV73	\N	\N	\N	01010000002DBDA0FFCA574040578194B7E77E4140	3947
SCV74	1234.21	Waypoint	\N	SCV74	\N	\N	\N	01010000003CC3B841CB57404033FC56D4E87E4140	3948
SCV76	1373.5999999999999	Waypoint	\N	SCV76	\N	\N	\N	01010000000B73FCFCE957404022EB6401377F4140	3949
SCV77	1225.5599999999999	Waypoint	\N	SCV77	\N	\N	\N	01010000003C129732DC57404056951373247F4140	3950
SCV78	1207.53	Waypoint	\N	SCV78	\N	\N	\N	0101000000BC6F845EE2574040D12794B3277F4140	3951
SCV79	1225.3199999999999	Waypoint	\N	SCV79	\N	\N	\N	0101000000AD14F846D1574040037C4F962E7F4140	3952
SCV8	1208.97	Waypoint	\N	SCV8	\N	\N	\N	0101000000E3C674BBB35740405645678229804140	3953
SCV80	1243.3399999999999	Waypoint	\N	SCV80	\N	\N	\N	0101000000CEC5C85ACF574040D0DCD826347F4140	3954
SCV81	1263.05	Waypoint	\N	SCV81	\N	\N	\N	010100000033FE36F1B95740400990F763407F4140	3955
SCV82	1229.8800000000001	Waypoint	\N	SCV82	\N	\N	\N	01010000002D851C68B95740408C520FBF457F4140	3956
SCV83	1255.8399999999999	Waypoint	\N	SCV83	\N	\N	\N	0101000000BA7384B3AB574040ADAF48284A7F4140	3957
SCV84	1265.45	Waypoint	\N	SCV84	\N	\N	\N	0101000000440F636FB5574040110005DF407F4140	3958
SCV86	1109.48	Waypoint	\N	SCV86	\N	\N	\N	0101000000541F1034DB5740402A45DCB02E7E4140	3959
SCV87	1081.3599999999999	Waypoint	\N	SCV87	\N	\N	\N	0101000000F7963FCDF35740405EE6E8AF5F7E4140	3960
SCV88	1121.02	Waypoint	\N	SCV88	\N	\N	\N	0101000000AD19F8D5055840402B384713AB7E4140	3961
SCV89	1134.47	Waypoint	\N	SCV89	\N	\N	\N	0101000000462D0C12D45740409167CC618D7E4140	3962
SCV9	1191.6700000000001	Waypoint	\N	SCV9	\N	\N	\N	0101000000646963C9B5574040A796D3BC28804140	3963
SCV91	1167.8800000000001	Waypoint	\N	SCV91	\N	\N	\N	0101000000D579A08AE557404096444C420F7E4140	3964
SCV92	1143.3699999999999	Waypoint	\N	SCV92	\N	\N	\N	0101000000B19F287D1F584040E999389BC97E4140	3965
SCV93	1146.97	Waypoint	\N	SCV93	\N	\N	\N	010100000088AC081313584040B252E88CCD7E4140	3966
SCV94	1131.3499999999999	Waypoint	\N	SCV94	\N	\N	\N	01010000001792707A07584040C8FB9E7CAD7E4140	3967
SCV95	1173.1700000000001	Waypoint	\N	SCV95	\N	\N	\N	0101000000F90BDDEF0758404047600CE2CC7E4140	3968
SCV96	1127.98	Waypoint	\N	SCV96	\N	\N	\N	01010000007AFB0635E25740402138D8E22D7E4140	3969
SCV97	1115.49	Waypoint	\N	SCV97	\N	\N	\N	01010000004428771EE3574040B61E53B1107E4140	3970
SCV98	1117.6500000000001	Waypoint	\N	SCV98	\N	\N	\N	0101000000028A68A6015840405D03FDB3567E4140	3971
SCV99	1143.3699999999999	Waypoint	\N	SCV99	\N	\N	\N	0101000000E1A4A8B477574040898AA33828804140	3972
\.


--
-- Name: VENTURE_pkey; Type: CONSTRAINT; Schema: public; Owner: webuser; Tablespace: 
--

ALTER TABLE ONLY "VENTURE"
    ADD CONSTRAINT "VENTURE_pkey" PRIMARY KEY (gid);


--
-- Name: brita2_pkey; Type: CONSTRAINT; Schema: public; Owner: webuser; Tablespace: 
--

ALTER TABLE ONLY brita2
    ADD CONSTRAINT brita2_pkey PRIMARY KEY (gid);


--
-- Name: brita_pkey; Type: CONSTRAINT; Schema: public; Owner: webuser; Tablespace: 
--

ALTER TABLE ONLY brita
    ADD CONSTRAINT brita_pkey PRIMARY KEY (gid);


--
-- Name: download1_pkey; Type: CONSTRAINT; Schema: public; Owner: webuser; Tablespace: 
--

ALTER TABLE ONLY download1
    ADD CONSTRAINT download1_pkey PRIMARY KEY (gid);


--
-- Name: download2_pkey; Type: CONSTRAINT; Schema: public; Owner: webuser; Tablespace: 
--

ALTER TABLE ONLY download2
    ADD CONSTRAINT download2_pkey PRIMARY KEY (gid);


--
-- Name: download3_pkey; Type: CONSTRAINT; Schema: public; Owner: webuser; Tablespace: 
--

ALTER TABLE ONLY download3
    ADD CONSTRAINT download3_pkey PRIMARY KEY (gid);


--
-- Name: download4_pkey; Type: CONSTRAINT; Schema: public; Owner: webuser; Tablespace: 
--

ALTER TABLE ONLY download4
    ADD CONSTRAINT download4_pkey PRIMARY KEY (gid);


--
-- Name: download5_pkey; Type: CONSTRAINT; Schema: public; Owner: webuser; Tablespace: 
--

ALTER TABLE ONLY download5
    ADD CONSTRAINT download5_pkey PRIMARY KEY (gid);


--
-- Name: geometry_columns_pk; Type: CONSTRAINT; Schema: public; Owner: aps03pwb; Tablespace: 
--

ALTER TABLE ONLY geometry_columns
    ADD CONSTRAINT geometry_columns_pk PRIMARY KEY (f_table_catalog, f_table_schema, f_table_name, f_geometry_column);


--
-- Name: pkey_gpsdata; Type: CONSTRAINT; Schema: public; Owner: aps03pwb; Tablespace: 
--

ALTER TABLE ONLY tblgpsdata
    ADD CONSTRAINT pkey_gpsdata PRIMARY KEY (gpsdataid);


--
-- Name: pkey_gpsmaster; Type: CONSTRAINT; Schema: public; Owner: aps03pwb; Tablespace: 
--

ALTER TABLE ONLY tblgpsmaster
    ADD CONSTRAINT pkey_gpsmaster PRIMARY KEY (gpsmasterid);


--
-- Name: spatial_ref_sys_pkey; Type: CONSTRAINT; Schema: public; Owner: aps03pwb; Tablespace: 
--

ALTER TABLE ONLY spatial_ref_sys
    ADD CONSTRAINT spatial_ref_sys_pkey PRIMARY KEY (srid);


--
-- Name: sturtaug08_pkey; Type: CONSTRAINT; Schema: public; Owner: webuser; Tablespace: 
--

ALTER TABLE ONLY sturtaug08
    ADD CONSTRAINT sturtaug08_pkey PRIMARY KEY (gid);


--
-- Name: uniq-namelocation; Type: CONSTRAINT; Schema: public; Owner: aps03pwb; Tablespace: 
--

ALTER TABLE ONLY tblgpsdata
    ADD CONSTRAINT "uniq-namelocation" UNIQUE (name, the_geom);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;
GRANT ALL ON SCHEMA public TO "Webgroup";
GRANT ALL ON SCHEMA public TO webuser;


--
-- Name: VENTURE; Type: ACL; Schema: public; Owner: webuser
--

REVOKE ALL ON TABLE "VENTURE" FROM PUBLIC;
REVOKE ALL ON TABLE "VENTURE" FROM webuser;
GRANT ALL ON TABLE "VENTURE" TO webuser;
GRANT ALL ON TABLE "VENTURE" TO aps03pwb;


--
-- Name: geometry_columns; Type: ACL; Schema: public; Owner: aps03pwb
--

REVOKE ALL ON TABLE geometry_columns FROM PUBLIC;
REVOKE ALL ON TABLE geometry_columns FROM aps03pwb;
GRANT ALL ON TABLE geometry_columns TO aps03pwb;
GRANT ALL ON TABLE geometry_columns TO webuser;


--
-- Name: tblgpsdata; Type: ACL; Schema: public; Owner: aps03pwb
--

REVOKE ALL ON TABLE tblgpsdata FROM PUBLIC;
REVOKE ALL ON TABLE tblgpsdata FROM aps03pwb;
GRANT ALL ON TABLE tblgpsdata TO aps03pwb;
GRANT ALL ON TABLE tblgpsdata TO webuser;


--
-- Name: tblgpsmaster; Type: ACL; Schema: public; Owner: aps03pwb
--

REVOKE ALL ON TABLE tblgpsmaster FROM PUBLIC;
REVOKE ALL ON TABLE tblgpsmaster FROM aps03pwb;
GRANT ALL ON TABLE tblgpsmaster TO aps03pwb;
GRANT ALL ON TABLE tblgpsmaster TO webuser;


--
-- PostgreSQL database dump complete
--

