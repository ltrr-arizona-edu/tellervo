PGDMP                     	    {            tellervo "   13.12 (Ubuntu 13.12-1.pgdg22.04+1) "   13.12 (Ubuntu 13.12-1.pgdg22.04+1) �   �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    63322    tellervo    DATABASE     Y   CREATE DATABASE tellervo WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'C.UTF-8';
    DROP DATABASE tellervo;
                postgres    false                        2615    63414    cpgdb    SCHEMA        CREATE SCHEMA cpgdb;
    DROP SCHEMA cpgdb;
                postgres    false            �           0    0    SCHEMA cpgdb    ACL     )   GRANT ALL ON SCHEMA cpgdb TO "Webgroup";
                   postgres    false    7                        2615    63415    cpgdbj    SCHEMA        CREATE SCHEMA cpgdbj;
    DROP SCHEMA cpgdbj;
                postgres    false            �           0    0    SCHEMA cpgdbj    ACL     *   GRANT ALL ON SCHEMA cpgdbj TO "Webgroup";
                   postgres    false    11                        2615    66834    portal    SCHEMA        CREATE SCHEMA portal;
    DROP SCHEMA portal;
                tellervo    false            �           0    0    SCHEMA public    ACL     *   GRANT ALL ON SCHEMA public TO "Webgroup";
                   postgres    false    5            	            2615    63323    sqlj    SCHEMA        CREATE SCHEMA sqlj;
    DROP SCHEMA sqlj;
                postgres    false            �           0    0    SCHEMA sqlj    ACL     &   GRANT USAGE ON SCHEMA sqlj TO PUBLIC;
                   postgres    false    9                        3079    63324    pljava 	   EXTENSION     8   CREATE EXTENSION IF NOT EXISTS pljava WITH SCHEMA sqlj;
    DROP EXTENSION pljava;
                   false    9            �           0    0    EXTENSION pljava    COMMENT     _   COMMENT ON EXTENSION pljava IS 'PL/Java procedural language (https://tada.github.io/pljava/)';
                        false    2                        3079    63416    postgis 	   EXTENSION     ;   CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;
    DROP EXTENSION postgis;
                   false            �           0    0    EXTENSION postgis    COMMENT     g   COMMENT ON EXTENSION postgis IS 'PostGIS geometry, geography, and raster spatial types and functions';
                        false    3            �           0    0    FUNCTION box2d_in(cstring)    ACL     y   GRANT ALL ON FUNCTION public.box2d_in(cstring) TO pbrewer;
GRANT ALL ON FUNCTION public.box2d_in(cstring) TO "Webgroup";
          public          postgres    false    478            �           0    0     FUNCTION box2d_out(public.box2d)    ACL     �   GRANT ALL ON FUNCTION public.box2d_out(public.box2d) TO pbrewer;
GRANT ALL ON FUNCTION public.box2d_out(public.box2d) TO "Webgroup";
          public          postgres    false    479            �           0    0    FUNCTION box3d_in(cstring)    ACL     y   GRANT ALL ON FUNCTION public.box3d_in(cstring) TO pbrewer;
GRANT ALL ON FUNCTION public.box3d_in(cstring) TO "Webgroup";
          public          postgres    false    476            �           0    0     FUNCTION box3d_out(public.box3d)    ACL     �   GRANT ALL ON FUNCTION public.box3d_out(public.box3d) TO pbrewer;
GRANT ALL ON FUNCTION public.box3d_out(public.box3d) TO "Webgroup";
          public          postgres    false    477            �           1247    64495 	   date_prec    TYPE     M   CREATE TYPE public.date_prec AS ENUM (
    'day',
    'month',
    'year'
);
    DROP TYPE public.date_prec;
       public          tellervo    false            �           1247    64502    datingtypeclass    TYPE     P   CREATE TYPE public.datingtypeclass AS ENUM (
    'arbitrary',
    'inferred'
);
 "   DROP TYPE public.datingtypeclass;
       public          postgres    false            �           0    0 #   FUNCTION geometry_analyze(internal)    ACL     �   GRANT ALL ON FUNCTION public.geometry_analyze(internal) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_analyze(internal) TO "Webgroup";
          public          postgres    false    462            �           0    0    FUNCTION geometry_in(cstring)    ACL        GRANT ALL ON FUNCTION public.geometry_in(cstring) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_in(cstring) TO "Webgroup";
          public          postgres    false    458            �           0    0 &   FUNCTION geometry_out(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_out(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_out(public.geometry) TO "Webgroup";
          public          postgres    false    459            �           0    0     FUNCTION geometry_recv(internal)    ACL     �   GRANT ALL ON FUNCTION public.geometry_recv(internal) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_recv(internal) TO "Webgroup";
          public          postgres    false    463            �           0    0 '   FUNCTION geometry_send(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_send(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_send(public.geometry) TO "Webgroup";
          public          postgres    false    464            �           1247    64509    securityuserandobjectid    TYPE     ^   CREATE TYPE public.securityuserandobjectid AS (
	securityuserid integer,
	objectid integer
);
 *   DROP TYPE public.securityuserandobjectid;
       public          postgres    false            �           1247    64512    securityuseruuidandentityid    TYPE     _   CREATE TYPE public.securityuseruuidandentityid AS (
	securityuserid uuid,
	entityid integer
);
 .   DROP TYPE public.securityuseruuidandentityid;
       public          tellervo    false            �           1247    64515    securityuseruuidandobjectuuid    TYPE     ^   CREATE TYPE public.securityuseruuidandobjectuuid AS (
	securityuserid uuid,
	objectid uuid
);
 0   DROP TYPE public.securityuseruuidandobjectuuid;
       public          tellervo    false            �           0    0    FUNCTION spheroid_in(cstring)    ACL        GRANT ALL ON FUNCTION public.spheroid_in(cstring) TO pbrewer;
GRANT ALL ON FUNCTION public.spheroid_in(cstring) TO "Webgroup";
          public          postgres    false    456            �           0    0 &   FUNCTION spheroid_out(public.spheroid)    ACL     �   GRANT ALL ON FUNCTION public.spheroid_out(public.spheroid) TO pbrewer;
GRANT ALL ON FUNCTION public.spheroid_out(public.spheroid) TO "Webgroup";
          public          postgres    false    457            �           1247    64518    tablefunc_crosstab_11    TYPE       CREATE TYPE public.tablefunc_crosstab_11 AS (
	row_name integer,
	category_1 text,
	category_2 text,
	category_3 text,
	category_4 text,
	category_5 text,
	category_6 text,
	category_7 text,
	category_8 text,
	category_9 text,
	category_10 text,
	category_11 text
);
 (   DROP TYPE public.tablefunc_crosstab_11;
       public          postgres    false            �           1247    64521    tablefunc_crosstab_2    TYPE     c   CREATE TYPE public.tablefunc_crosstab_2 AS (
	row_name text,
	category_1 text,
	category_2 text
);
 '   DROP TYPE public.tablefunc_crosstab_2;
       public          postgres    false            �           1247    64524    tablefunc_crosstab_3    TYPE     u   CREATE TYPE public.tablefunc_crosstab_3 AS (
	row_name text,
	category_1 text,
	category_2 text,
	category_3 text
);
 '   DROP TYPE public.tablefunc_crosstab_3;
       public          postgres    false            �           1247    64527    tablefunc_crosstab_4    TYPE     �   CREATE TYPE public.tablefunc_crosstab_4 AS (
	row_name text,
	category_1 text,
	category_2 text,
	category_3 text,
	category_4 text
);
 '   DROP TYPE public.tablefunc_crosstab_4;
       public          postgres    false            �           1247    64530    tablefunc_crosstab_9    TYPE     �   CREATE TYPE public.tablefunc_crosstab_9 AS (
	row_name integer,
	category_1 text,
	category_2 text,
	category_3 text,
	category_4 text,
	category_5 text,
	category_6 text,
	category_7 text,
	category_8 text,
	category_9 text
);
 '   DROP TYPE public.tablefunc_crosstab_9;
       public          postgres    false            �
           1247    66883    typfulltaxonomy    TYPE     �  CREATE TYPE public.typfulltaxonomy AS (
	taxonid character varying,
	kingdom character varying(128),
	subkingdom character varying(128),
	phylum character varying(128),
	division character varying(128),
	class character varying(128),
	txorder character varying(128),
	family character varying(128),
	subfamily character varying(128),
	genus character varying(128),
	subgenus character varying(128),
	section character varying(128),
	subsection character varying(128),
	species character varying(128),
	subspecies character varying(128),
	race character varying(128),
	variety character varying(128),
	subvariety character varying(128),
	form character varying(128),
	subform character varying(128)
);
 "   DROP TYPE public.typfulltaxonomy;
       public          tellervo    false            �           1247    64536    typnamenumber    TYPE     Q   CREATE TYPE public.typnamenumber AS (
	name character varying,
	number bigint
);
     DROP TYPE public.typnamenumber;
       public          postgres    false            �           1247    64539    typpermissionset    TYPE     �   CREATE TYPE public.typpermissionset AS (
	denied boolean,
	cancreate boolean,
	canread boolean,
	canupdate boolean,
	candelete boolean,
	decidedby text
);
 #   DROP TYPE public.typpermissionset;
       public          postgres    false            �
           1247    66880    typtaxonflat2    TYPE     �   CREATE TYPE public.typtaxonflat2 AS (
	taxonid character varying,
	taxonrankid integer,
	taxonname character varying(128),
	taxonrank character varying(30),
	rankorder double precision
);
     DROP TYPE public.typtaxonflat2;
       public          tellervo    false            �
           1247    66877    typtaxonrankname    TYPE     �   CREATE TYPE public.typtaxonrankname AS (
	taxonid character varying,
	taxonrankid integer,
	taxonname character varying(128)
);
 #   DROP TYPE public.typtaxonrankname;
       public          tellervo    false            �           1247    64548    typvmeasurementsearchresult    TYPE       CREATE TYPE public.typvmeasurementsearchresult AS (
	recursionlevel integer,
	vmeasurementid uuid,
	op character varying,
	code character varying,
	startyear integer,
	readingcount integer,
	measurementcount integer,
	comments character varying,
	modified timestamp without time zone
);
 .   DROP TYPE public.typvmeasurementsearchresult;
       public          postgres    false            �           1247    64551    typvmeasurementsummaryinfo    TYPE     �   CREATE TYPE public.typvmeasurementsummaryinfo AS (
	vmeasurementid uuid,
	objectcode text,
	objectcount integer,
	commontaxonname text,
	taxoncount integer
);
 -   DROP TYPE public.typvmeasurementsummaryinfo;
       public          postgres    false            �           0    0    FUNCTION box3d(public.box2d)    ACL     }   GRANT ALL ON FUNCTION public.box3d(public.box2d) TO pbrewer;
GRANT ALL ON FUNCTION public.box3d(public.box2d) TO "Webgroup";
          public          postgres    false    723            �           0    0    FUNCTION geometry(public.box2d)    ACL     �   GRANT ALL ON FUNCTION public.geometry(public.box2d) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry(public.box2d) TO "Webgroup";
          public          postgres    false    727            �           0    0    FUNCTION box(public.box3d)    ACL     y   GRANT ALL ON FUNCTION public.box(public.box3d) TO pbrewer;
GRANT ALL ON FUNCTION public.box(public.box3d) TO "Webgroup";
          public          postgres    false    724            �           0    0    FUNCTION box2d(public.box3d)    ACL     }   GRANT ALL ON FUNCTION public.box2d(public.box3d) TO pbrewer;
GRANT ALL ON FUNCTION public.box2d(public.box3d) TO "Webgroup";
          public          postgres    false    722            �           0    0    FUNCTION geometry(public.box3d)    ACL     �   GRANT ALL ON FUNCTION public.geometry(public.box3d) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry(public.box3d) TO "Webgroup";
          public          postgres    false    728            �           0    0    FUNCTION geometry(bytea)    ACL     u   GRANT ALL ON FUNCTION public.geometry(bytea) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry(bytea) TO "Webgroup";
          public          postgres    false    730            �           0    0    FUNCTION box(public.geometry)    ACL        GRANT ALL ON FUNCTION public.box(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.box(public.geometry) TO "Webgroup";
          public          postgres    false    721            �           0    0    FUNCTION box2d(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.box2d(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.box2d(public.geometry) TO "Webgroup";
          public          postgres    false    719            �           0    0    FUNCTION box3d(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.box3d(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.box3d(public.geometry) TO "Webgroup";
          public          postgres    false    720                        0    0    FUNCTION bytea(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.bytea(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.bytea(public.geometry) TO "Webgroup";
          public          postgres    false    731                       0    0    FUNCTION text(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.text(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.text(public.geometry) TO "Webgroup";
          public          postgres    false    725                       0    0    FUNCTION geometry(text)    ACL     s   GRANT ALL ON FUNCTION public.geometry(text) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry(text) TO "Webgroup";
          public          postgres    false    729            �           1255    64552 ,   _fcojc(character varying, character varying)    FUNCTION       CREATE FUNCTION cpgdb._fcojc(character varying, character varying) RETURNS character varying
    LANGUAGE plpgsql IMMUTABLE
    AS $_$
BEGIN
   RETURN ' INNER JOIN tbl' || $1 || ' ON tbl' || $1 || '.' || $1 || 'ID=tbl' || $2 || '.' || $1 || 'ID';
END;
$_$;
 B   DROP FUNCTION cpgdb._fcojc(character varying, character varying);
       cpgdb          postgres    false    7                       0    0 5   FUNCTION _fcojc(character varying, character varying)    ACL     X   GRANT ALL ON FUNCTION cpgdb._fcojc(character varying, character varying) TO "Webgroup";
          cpgdb          postgres    false    1210            �           1255    66884 2   _gettaxonfordepth(integer, public.typfulltaxonomy)    FUNCTION     �  CREATE FUNCTION cpgdb._gettaxonfordepth(integer, public.typfulltaxonomy) RETURNS text
    LANGUAGE sql IMMUTABLE
    AS $_$
   SELECT CASE $1
     WHEN 1 THEN $2.kingdom 
     WHEN 2 THEN $2.subkingdom
     WHEN 3 THEN $2.phylum
     WHEN 4 THEN $2.division
     WHEN 5 THEN $2.class
     WHEN 6 THEN $2.txorder
     WHEN 7 THEN $2.family
     WHEN 8 THEN $2.subfamily
     WHEN 9 THEN $2.genus
     WHEN 10 THEN $2.subgenus
     WHEN 11 THEN $2.section
     WHEN 12 THEN $2.subsection
     WHEN 13 THEN $2.species
     WHEN 14 THEN $2.subspecies
     WHEN 15 THEN $2.race
     WHEN 16 THEN $2.variety
     WHEN 17 THEN $2.subvariety
     WHEN 18 THEN $2.form
     WHEN 19 THEN $2.subform
     ELSE 'invalid'
   END;
$_$;
 H   DROP FUNCTION cpgdb._gettaxonfordepth(integer, public.typfulltaxonomy);
       cpgdb          tellervo    false    2753    7                       0    0 &   FUNCTION st_srid(geom public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_srid(geom public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_srid(geom public.geometry) TO "Webgroup";
          public          postgres    false    688            �           1255    64554    uuid_generate_v1mc()    FUNCTION     �   CREATE FUNCTION public.uuid_generate_v1mc() RETURNS uuid
    LANGUAGE c STRICT
    AS '$libdir/uuid-ossp', 'uuid_generate_v1mc';
 +   DROP FUNCTION public.uuid_generate_v1mc();
       public          postgres    false            �            1259    64555 	   tblobject    TABLE     N  CREATE TABLE public.tblobject (
    objectid uuid DEFAULT public.uuid_generate_v1mc() NOT NULL,
    title character varying(100) NOT NULL,
    code character varying(20) NOT NULL,
    createdtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifiedtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    locationgeometry public.geometry,
    locationtypeid integer,
    locationprecision integer,
    locationcomment character varying,
    file character varying[],
    creator character varying,
    owner character varying,
    parentobjectid uuid,
    description character varying,
    objecttypeid integer,
    coveragetemporalid integer,
    coveragetemporalfoundationid integer,
    comments character varying,
    coveragetemporal character varying,
    coveragetemporalfoundation character varying,
    locationaddressline1 character varying,
    locationaddressline2 character varying,
    locationcityortown character varying,
    locationstateprovinceregion character varying,
    locationpostalcode character varying,
    locationcountry character varying,
    vegetationtype character varying,
    domainid integer DEFAULT 0,
    projectid uuid,
    CONSTRAINT enforce_dims_objectextent CHECK ((public.st_ndims(locationgeometry) = 2)),
    CONSTRAINT enforce_obj_uniqueparents CHECK ((parentobjectid <> objectid)),
    CONSTRAINT enforce_srid_objectextent CHECK ((public.st_srid(locationgeometry) = 4326)),
    CONSTRAINT enforceprojectfortopobject CHECK ((((parentobjectid IS NULL) AND (projectid IS NOT NULL)) OR ((parentobjectid IS NOT NULL) AND (projectid IS NULL))))
);
    DROP TABLE public.tblobject;
       public         heap    postgres    false    1211    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3                       0    0 #   COLUMN tblobject.coveragetemporalid    COMMENT     a   COMMENT ON COLUMN public.tblobject.coveragetemporalid IS 'deprecated. - now allowing free text';
          public          postgres    false    231                       0    0 -   COLUMN tblobject.coveragetemporalfoundationid    COMMENT     k   COMMENT ON COLUMN public.tblobject.coveragetemporalfoundationid IS 'deprecated. - now allowing free text';
          public          postgres    false    231                       0    0    TABLE tblobject    ACL     3   GRANT ALL ON TABLE public.tblobject TO "Webgroup";
          public          postgres    false    231            �           1255    64569 -   _internalfindobjectsanddescendantswhere(text)    FUNCTION     �  CREATE FUNCTION cpgdb._internalfindobjectsanddescendantswhere(whereclause text) RETURNS SETOF public.tblobject
    LANGUAGE plpgsql STABLE
    AS $$
DECLARE
   ref refcursor;

   obj tblObject;
   child tblObject;
   objid tblObject.objectId%TYPE;
BEGIN

   OPEN ref FOR EXECUTE 'SELECT tblObject.* FROM tblObject ' ||
	'LEFT JOIN tlkpObjectType USING(objectTypeID) ' ||
	'LEFT JOIN tlkpLocationType USING(locationTypeID) ' ||
	'LEFT JOIN tlkpCoverageTemporal USING(coverageTemporalID) ' ||
	'LEFT JOIN tlkpCoverageTemporalFoundation USING (coverageTemporalFoundationID) ' ||
	'WHERE ' || whereclause;

   LOOP
      FETCH ref INTO obj;

      IF NOT FOUND THEN
         CLOSE ref;
         EXIT;
      END IF;

      RETURN NEXT obj;

      objid = obj.objectId;
      FOR child IN SELECT * from cpgdb.recurseFindObjectDescendants(objid, 1) LOOP
         RETURN NEXT child;
      END LOOP;
   END LOOP;
END;
$$;
 O   DROP FUNCTION cpgdb._internalfindobjectsanddescendantswhere(whereclause text);
       cpgdb          postgres    false    231    7            �            1259    64570    tlkpreadingnote    TABLE     �  CREATE TABLE public.tlkpreadingnote (
    readingnoteid integer NOT NULL,
    note character varying(1000) NOT NULL,
    vocabularyid integer,
    standardisedid character varying(100),
    parentreadingid integer,
    parentvmrelyearreadingnoteid integer,
    CONSTRAINT tlkpreadingnote_check CHECK ((((vocabularyid <> 0) AND (parentreadingid IS NULL) AND (parentvmrelyearreadingnoteid IS NULL)) OR (vocabularyid = 0)))
);
 #   DROP TABLE public.tlkpreadingnote;
       public         heap    postgres    false                       0    0    TABLE tlkpreadingnote    ACL     9   GRANT ALL ON TABLE public.tlkpreadingnote TO "Webgroup";
          public          postgres    false    232            �           1255    64577 )   addcustomreadingnote(uuid, integer, text)    FUNCTION     '  CREATE FUNCTION cpgdb.addcustomreadingnote(uuid, integer, text) RETURNS public.tlkpreadingnote
    LANGUAGE plpgsql
    AS $_$
DECLARE
   _VMID ALIAS FOR $1;
   _RelYear ALIAS FOR $2;
   _NoteText ALIAS FOR $3;

   VMOp text;
   myReadingID tblReading.ReadingID%TYPE;
   myOverride boolean;
   VMReadingCount int;
   myReadingNoteID tlkpReadingNote.ReadingNoteID%TYPE;
   myVMRYRN tblVMeasurementRelYearReadingNote.RelYearReadingNoteID%TYPE;

   outrow tlkpReadingNote%ROWTYPE;
BEGIN

   SELECT ReadingNoteID INTO myReadingNoteID FROM tlkpReadingNote WHERE note ILIKE _NoteText;

   IF FOUND THEN
      PERFORM cpgdb.AddReadingNote(_VMID, _RelYear, myReadingNoteID, null);
      SELECT * INTO outrow FROM tlkpReadingNote WHERE ReadingNoteID=myReadingNoteID;
      RETURN outrow;
   END IF;

   SELECT op.name INTO VMOp FROM tblVMeasurement 
      INNER JOIN tlkpVMeasurementOp op USING (vmeasurementOpID)
      WHERE vmeasurementID=_VMID;

   IF NOT FOUND THEN
      RAISE EXCEPTION 'Invalid/nonexistent vmeasurement %', _VMID;
   END IF;

   myReadingNoteID := nextval('tlkpreadingnote_readingnoteid_seq'::regclass);

   IF VMOp = 'Direct' THEN
      IF _Override IS NOT NULL THEN
         RAISE EXCEPTION 'Override must not be set for Direct VMs';
      END IF;

      SELECT readingID INTO myReadingID 
         FROM tblVMeasurement 
         INNER JOIN tblMeasurement USING (measurementID) 
         INNER JOIN tblReading USING (measurementID) 
         WHERE VMeasurementID=_VMID AND RelYear=_RelYear;

      IF NOT FOUND THEN
         RAISE EXCEPTION 'No reading found for relyear % on vmeasurementid %. Year must exist.', _RelYear, _VMID;
      END IF;

      INSERT INTO tlkpReadingNote(ReadingNoteID, note, vocabularyid, parentReadingID) VALUES (myReadingNoteID, _NoteText, 0, myReadingID);
      INSERT INTO tblReadingReadingNote (ReadingReadingNoteID, ReadingID, ReadingNoteID) VALUES (myRRNID, myReadingID, _ReadingNoteID);
   ELSE
      SELECT readingCount INTO VMReadingCount FROM tblVMeasurementMetaCache WHERE VMeasurementID=_VMID;
      IF NOT FOUND OR VMReadingCount <= _RelYear THEN
         RAISE EXCEPTION 'VMeasurement % does not exist, is malformed, or relyear % is out of range.', _VMID, _RelYear;
      END IF;


      INSERT INTO tlkpReadingNote(ReadingNoteID, note, vocabularyid) VALUES (myReadingNoteID, _NoteText, 0);

      myVMRYRN := nextval('tblvmeasurementrelyearreadingnote_relyearreadingnoteid_seq'::regclass);
      INSERT INTO tblVMeasurementRelYearReadingNote (RelYearReadingNoteID, VMeasurementID, RelYear, ReadingNoteID)
         VALUES (myVMRYRN, _VMID, _RelYear, myReadingNoteID);

      UPDATE tlkpReadingNote SET parentVMRelYearReadingNoteID=myVMRYRN WHERE ReadingNoteID=myReadingNoteID;
   END IF;

   SELECT * INTO outrow FROM tlkpReadingNote WHERE ReadingNoteID=myReadingNoteID;
   RETURN outrow;
END;
$_$;
 ?   DROP FUNCTION cpgdb.addcustomreadingnote(uuid, integer, text);
       cpgdb          postgres    false    232    7            �           1255    64578 /   addreadingnote(uuid, integer, integer, boolean)    FUNCTION     3  CREATE FUNCTION cpgdb.addreadingnote(uuid, integer, integer, boolean) RETURNS void
    LANGUAGE plpgsql
    AS $_$
DECLARE
   _VMID ALIAS FOR $1;
   _RelYear ALIAS FOR $2;
   _ReadingNoteID ALIAS FOR $3;
   _Override ALIAS FOR $4;

   VMOp text;
   myReadingID tblReading.ReadingID%TYPE;
   myOverride boolean;
   VMReadingCount int;
BEGIN

   SELECT op.name INTO VMOp FROM tblVMeasurement 
      INNER JOIN tlkpVMeasurementOp op USING (vmeasurementOpID)
      WHERE vmeasurementID=_VMID;

   IF NOT FOUND THEN
      RAISE EXCEPTION 'Invalid/nonexistent vmeasurement %', _VMID;
   END IF;

   IF VMOp = 'Direct' THEN
      IF _Override IS NOT NULL THEN
         RAISE EXCEPTION 'Override must not be set for Direct VMs';
      END IF;

      SELECT readingID INTO myReadingID 
         FROM tblVMeasurement 
         INNER JOIN tblMeasurement USING (measurementID) 
         INNER JOIN tblReading USING (measurementID) 
         WHERE VMeasurementID=_VMID AND RelYear=_RelYear;

      IF NOT FOUND THEN
         RAISE EXCEPTION 'No reading found for relyear % on vmeasurementid %. Year must exist.', _RelYear, _VMID;
      END IF;

      INSERT INTO tblReadingReadingNote (ReadingID, ReadingNoteID) VALUES (myReadingID, _ReadingNoteID);
   ELSE
      myOverride := _Override;

      IF myOverride IS NULL THEN
         myOverride := FALSE;
      END IF;

      SELECT readingCount INTO VMReadingCount FROM tblVMeasurementMetaCache WHERE VMeasurementID=_VMID;
      IF NOT FOUND OR VMReadingCount <= _RelYear THEN
         RAISE EXCEPTION 'VMeasurement % does not exist, is malformed, or relyear % is out of range.', _VMID, _RelYear;
      END IF;

      INSERT INTO tblVMeasurementRelYearReadingNote (VMeasurementID, RelYear, ReadingNoteID, DisabledOverride)
         VALUES (_VMID, _RelYear, _ReadingNoteID, myOverride);
   END IF;
END;
$_$;
 E   DROP FUNCTION cpgdb.addreadingnote(uuid, integer, integer, boolean);
       cpgdb          postgres    false    7            �           1255    64579 C   andpermissionsets(public.typpermissionset, public.typpermissionset)    FUNCTION       CREATE FUNCTION cpgdb.andpermissionsets(origset public.typpermissionset, newset public.typpermissionset) RETURNS public.typpermissionset
    LANGUAGE plpgsql IMMUTABLE
    AS $$
DECLARE
   res typPermissionSet;
BEGIN
   IF newset.denied THEN
      res.denied := true;
      res.canCreate := false;
      res.canRead := false;
      res.canUpdate := false;
      res.canDelete := false;
      res.decidedBy := newset.denied;
      
      RETURN res;
   ELSIF origset.denied THEN
      res.denied := true;
      res.canCreate := false;
      res.canRead := false;
      res.canUpdate := false;
      res.canDelete := false;
      res.decidedBy := origset.denied;
      
      RETURN res;
   END IF;

   res.denied := false;
   res.canCreate := origset.canCreate AND newset.canCreate;
   res.canRead := origset.canRead AND newset.canRead;
   res.canUpdate := origset.canUpdate AND newset.canUpdate;
   res.canDelete := origset.canDelete AND newset.canDelete;
   res.decidedBy := origset.decidedBy || ', ' || newset.decidedBy;

   RETURN res;
END;
$$;
 h   DROP FUNCTION cpgdb.andpermissionsets(origset public.typpermissionset, newset public.typpermissionset);
       cpgdb          postgres    false    2251    7    2251    2251            �           1255    64580    clearreadingnotes(uuid)    FUNCTION     �  CREATE FUNCTION cpgdb.clearreadingnotes(uuid) RETURNS integer
    LANGUAGE plpgsql
    AS $_$
DECLARE
   _VMID ALIAS FOR $1;

   VMOp text;
   ret integer;
BEGIN

   SELECT op.name INTO VMOp FROM tblVMeasurement 
      INNER JOIN tlkpVMeasurementOp op USING (vmeasurementOpID)
      WHERE vmeasurementID=_VMID;

   IF NOT FOUND THEN
      RAISE EXCEPTION 'Invalid/nonexistent vmeasurement %', _VMID;
   END IF;

   IF VMOp = 'Direct' THEN
      DELETE FROM tblReadingReadingNote WHERE ReadingID IN
         (SELECT readingID FROM tblVMeasurement
            INNER JOIN tblMeasurement USING (measurementID)
            INNER JOIN tblReading USING (measurementID)
            INNER JOIN tblReadingReadingNote USING (readingID)
            WHERE VMeasurementID=_VMID);
   ELSE
      DELETE FROM tblVMeasurementRelYearReadingNote WHERE VMeasurementID=_VMID;
   END IF;

   GET DIAGNOSTICS ret = ROW_COUNT;   
   RETURN ret;
END;
$_$;
 -   DROP FUNCTION cpgdb.clearreadingnotes(uuid);
       cpgdb          postgres    false    7            �           1255    64581    countSamplesPerObject(text)    FUNCTION     �  CREATE FUNCTION cpgdb."countSamplesPerObject"(objcode text) RETURNS integer
    LANGUAGE plpgsql
    AS $_$DECLARE
  objcode ALIAS FOR $1;
BEGIN

select count(tblsample.*) 
from tblelement, tblsample 
where tblelement.objectid 
in (select objectid from cpgdb.findobjectdescendants(
(select objectid from tblobject where code=objcode), true))

and tblelement.elementid=tblsample.elementid ; 

END$_$;
 ;   DROP FUNCTION cpgdb."countSamplesPerObject"(objcode text);
       cpgdb          postgres    false    7            	           0    0 .   FUNCTION "countSamplesPerObject"(objcode text)    ACL     Q   GRANT ALL ON FUNCTION cpgdb."countSamplesPerObject"(objcode text) TO "Webgroup";
          cpgdb          postgres    false    1217            �            1259    64582    tblvmeasurementmetacache    TABLE     E  CREATE TABLE public.tblvmeasurementmetacache (
    vmeasurementid uuid NOT NULL,
    startyear integer NOT NULL,
    readingcount integer NOT NULL,
    measurementcount integer DEFAULT 1 NOT NULL,
    vmextent public.geometry,
    vmeasurementmetacacheid integer NOT NULL,
    objectcode text,
    objectcount integer,
    commontaxonname text,
    taxoncount integer,
    prefix text,
    datingtypeid integer NOT NULL,
    CONSTRAINT enforce_dims_vmextent CHECK ((public.st_ndims(vmextent) = 2)),
    CONSTRAINT enforce_srid_vmextent CHECK ((public.st_srid(vmextent) = 4326))
);
 ,   DROP TABLE public.tblvmeasurementmetacache;
       public         heap    postgres    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            
           0    0    TABLE tblvmeasurementmetacache    ACL     Z   GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.tblvmeasurementmetacache TO "Webgroup";
          public          postgres    false    233            �           1255    64591    createmetacache(uuid)    FUNCTION     E  CREATE FUNCTION cpgdb.createmetacache(uuid) RETURNS public.tblvmeasurementmetacache
    LANGUAGE plpgsql
    AS $_$
DECLARE
   vmid ALIAS FOR $1;
   op varchar;
   vmresult tblVMeasurementResult%ROWTYPE;
   ret tblVMeasurementMetaCache%ROWTYPE;
BEGIN
   -- RAISE NOTICE 'Creating metacache for %', vmid;

   -- acquire the vMeasurementresult
   BEGIN
      SELECT * INTO vmresult FROM cpgdb.GetVMeasurementResult(vmid);
   EXCEPTION WHEN OTHERS THEN
      RAISE NOTICE 'CreateMetaCache(%) failed: VMeasurement is malformed or does not exist', vmid;
      RETURN NULL;
   END;

   IF NOT FOUND THEN
      RETURN NULL;
   END IF;

   ret.VMeasurementID := vmid;
   ret.StartYear := vmresult.StartYear;
   ret.datingTypeID := vmresult.datingTypeID;

   -- Calculate number of readings
   SELECT COUNT(*) INTO ret.ReadingCount
      FROM tblVMeasurementReadingResult WHERE VMeasurementResultID = vmresult.VMeasurementResultID;

   -- Calculate number of measurements
   SELECT FIRST(tlkpVMeasurementOp.Name), COUNT(tblVMeasurementGroup.VMeasurementID) 
      INTO op, ret.MeasurementCount
      FROM tblVMeasurement 
      INNER JOIN tlkpVMeasurementOp ON tblVMeasurement.VMeasurementOpID = tlkpVMeasurementOp.VMeasurementOpID 
      LEFT JOIN tblVMeasurementGroup ON tblVMeasurement.VMeasurementID = tblVMeasurementGroup.VMeasurementID 
      WHERE tblVMeasurement.VMeasurementID = vmid;

   -- For a Direct VMeasurement, force 1.
   IF op = 'Direct' THEN
      ret.MeasurementCount := 1;
   END IF;

   -- Delete and populate the cache
   DELETE FROM tblVMeasurementMetaCache WHERE VMeasurementID = vmid;
   INSERT INTO tblVMeasurementMetaCache(VMeasurementID, StartYear, ReadingCount, MeasurementCount, DatingTypeID)
      VALUES (ret.VMeasurementID, ret.StartYear, ret.ReadingCount, ret.MeasurementCount, ret.datingTypeID);

   -- Clean up
   DELETE FROM tblVMeasurementResult WHERE VMeasurementResultID = vmresult.VMeasurementResultID;

   -- Clear out our tblVMeasurementDerivedCache for this VMeasurement
   DELETE FROM tblVMeasurementDerivedCache WHERE VMeasurementID = vmid;
   -- Then, populate it.
   INSERT INTO tblVMeasurementDerivedCache(VMeasurementID,MeasurementID) 
      SELECT vmid,Measurement.MeasurementID FROM cpgdb.FindVMParentMeasurements(vmid) Measurement;

   -- Calculate extent of vmeasurement by looking up locations of all associated direct Measurements
  -- SELECT st_setsrid(extent(tblelement.locationgeometry)::geometry,4326)
   --   INTO  ret.vmextent
    --  FROM  tblelement, tblsample, tblradius, tblMeasurement, tblvmeasurement
    --  WHERE tblvmeasurement.measurementid=tblmeasurement.measurementid
    --  AND   tblmeasurement.radiusid=tblradius.radiusid
    --  AND   tblradius.sampleid=tblsample.sampleid
    --  AND   tblsample.elementid=tblelement.elementid
    --  AND   tblvmeasurement.vmeasurementid
      --      IN (SELECT vMeasurementid
      --             FROM  cpgdb.FindVMParents(vmid, true)
       --            WHERE op='Direct');

   -- Calculate extent using all associated element and object geometries
   IF op = 'Direct' THEN
   RAISE NOTICE 'Getting extents from element and object.  Measurement count is %', ret.MeasurementCount;
	   SELECT st_setsrid(extent(st_collect(tblelement.locationgeometry, tblobject.locationgeometry)), 4326)
	      INTO  ret.vmextent
	      FROM  tblobject, tblelement, tblsample, tblradius, tblMeasurement, tblvmeasurement
	      WHERE tblvmeasurement.measurementid=tblmeasurement.measurementid
	      AND   tblmeasurement.radiusid=tblradius.radiusid
	      AND   tblradius.sampleid=tblsample.sampleid
	      AND   tblsample.elementid=tblelement.elementid
	      AND   tblelement.objectid=tblobject.objectid
	      AND   tblvmeasurement.vmeasurementid
		    IN (SELECT vMeasurementid
			   FROM  cpgdb.FindVMParents(vmid, true)
			   WHERE FindVMParents.op='Direct');
   
   -- Calculate extent of vmeasurement by looking up locations of all associated direct VMeasurements
   ELSE 
	RAISE NOTICE 'Getting extent from component vmeasurements';
	RAISE NOTICE 'Find all parents of %', vmid;
	RAISE NOTICE 'Number of measurements is %',ret.MeasurementCount;
	   SELECT st_setsrid(extent(tblvmeasurementmetacache.vmextent)::geometry, 4326)
	   INTO  ret.vmextent
	   FROM  tblvmeasurementmetacache
	   WHERE tblvmeasurementmetacache.vmeasurementid
		 IN (SELECT vMeasurementid
		   FROM  cpgdb.FindVMParents(vmid, false));
   END IF;


   RAISE NOTICE 'Extent is %', ret.vmextent;

   -- Store extent info
   UPDATE tblVMeasurementMetaCache SET vmextent = ret.vmextent WHERE VMeasurementID = ret.VMeasurementID;

   -- Now, get taxon and label data and update that
   SELECT INTO ret.objectCode, ret.objectCount, ret.CommonTaxonName, ret.taxonCount, ret.prefix 
       s.objectCode,s.objectCount,s.commonTaxonName,s.taxonCount,cpgdb.GetVMeasurementPrefix(vmid) AS prefix
       FROM cpgdb.getVMeasurementSummaryInfo(vmid) AS s;
   UPDATE tblVMeasurementMetaCache SET (objectCode, objectCount, commonTaxonName, taxonCount, prefix) =
       (ret.objectCode, ret.objectCount, ret.CommonTaxonName, ret.taxonCount, ret.prefix)
       WHERE VMeasurementID = vmid;

   -- Return result
   RETURN ret;
END;

$_$;
 +   DROP FUNCTION cpgdb.createmetacache(uuid);
       cpgdb          postgres    false    233    7            �           1255    64592 �   createnewvmeasurement(character varying, integer, uuid, character varying, character varying, integer, uuid[], character varying, character varying, date)    FUNCTION     �  CREATE FUNCTION cpgdb.createnewvmeasurement(character varying, integer, uuid, character varying, character varying, integer, uuid[], character varying, character varying, date) RETURNS uuid
    LANGUAGE plpgsql
    AS $_$
DECLARE
   OP ALIAS FOR $1;
   OPParam ALIAS FOR $2;
   V_OwnerUserID ALIAS FOR $3;
   V_Code ALIAS FOR $4;
   V_Comments ALIAS FOR $5;
   BaseMeasurement ALIAS FOR $6;
   Constituents ALIAS FOR $7;
   V_Objective ALIAS FOR $8;
   V_Version ALIAS FOR $9;
   V_Birthdate ALIAS FOR $10;

   newID tblVMeasurement.VMeasurementID%TYPE;
   OPName VARCHAR;
   OPId tblVMeasurement.VMeasurementOpID%TYPE;
   ConstituentSize INTEGER;
   CVMId INTEGER;

   DoneCreating BOOLEAN;
BEGIN
   SELECT 
      uuid_generate_v1mc(), VMeasurementOpID, Name
   INTO 
      newID, OPId, OPName
   FROM tlkpVMeasurementOp WHERE tlkpVMeasurementOp.Name = OP;

   IF NOT FOUND THEN
      RAISE EXCEPTION 'Invalid VMeasurementOP %', OP;
   END IF;

   IF V_Code IS NULL THEN
      RAISE EXCEPTION 'Code must not be NULL';
   END IF;

   DoneCreating := TRUE;

   IF OpName <> 'Direct' THEN
      IF Constituents IS NULL THEN
         RAISE EXCEPTION 'Constituents must not be NULL for non-Direct VMeasurements';
      ELSE
         ConstituentSize := 1 + array_upper(Constituents, 1) - array_lower(Constituents, 1);
      END IF;

      IF V_Version IS NULL THEN
         SELECT COUNT(*) INTO CVMId FROM cpgdb.getUsedVersionsForConstituents(Constituents) vname 
            WHERE vname IS NULL;
         IF CVMId > 0 THEN
            RAISE EXCEPTION 'EVERSIONALREADYEXISTS: An unversioned derived measurement already exists for this parent';
         END IF;
      ELSE
         SELECT COUNT(*) INTO CVMId FROM cpgdb.getUsedVersionsForConstituents(Constituents) vname 
            WHERE vname=V_Version;
         IF CVMId > 0 THEN
            RAISE EXCEPTION 'EVERSIONALREADYEXISTS: A derived measurement already exists for this parent with the given version';
         END IF;
      END IF;

   ELSIF V_Version IS NOT NULL THEN
      RAISE EXCEPTION 'Version must be null for direct VMeasurements!';
   END IF;

   IF OPName = 'Direct' THEN
      IF Constituents IS NOT NULL THEN
         RAISE EXCEPTION 'A Direct VMeasurement may not have any Constituents';
      END IF;

      IF BaseMeasurement IS NULL THEN
         RAISE EXCEPTION 'A Direct VMeasurement must have a Base MeasurementID';
      END IF;

      INSERT INTO tblVMeasurement(VMeasurementID, MeasurementID, VMeasurementOpID, Code, Comments, OwnerUserID, 
 				  Objective, Version, Birthdate, IsGenerating)
         VALUES (newID, BaseMeasurement, OpID, V_Code, V_Comments, V_OwnerUserID, 
		 V_Objective, V_Version, V_Birthdate, FALSE);

      RETURN newID;

   ELSIF OPName = 'Sum' THEN
      IF ConstituentSize < 1 THEN
         RAISE EXCEPTION 'Sums must have at least one constituent';
      END IF;

      IF cpgdb.VerifySumsAreContiguousAndDated(Constituents) = FALSE THEN
         NULL;      
      END IF;

   ELSIF OPName = 'Index' THEN
      IF OPParam IS NULL THEN
         RAISE EXCEPTION 'Indexes must have an index type parameter';
      END IF;

      IF ConstituentSize <> 1 THEN
         RAISE EXCEPTION 'Indexes may only be comprised of one constituent';
      END IF;

   ELSIF OPName = 'Redate' THEN
      IF OPParam IS NOT NULL THEN
         RAISE EXCEPTION 'Redates must not have a redate parameter - specify in finishRedate!';
      END IF;

      IF ConstituentSize <> 1 THEN
         RAISE EXCEPTION 'Redates may only be comprised of one constituent';
      END IF;

      DoneCreating := FALSE; -- we have to finish in another function

   ELSIF OPName = 'Crossdate' THEN
      IF OPParam IS NOT NULL THEN
         RAISE EXCEPTION 'Crossdates must not have a redate parameter - specify in finishCrossdate!';
      END IF;

      IF ConstituentSize <> 1 THEN
         RAISE EXCEPTION 'Crossdates may only be comprised of one constituent';
      END IF;

      DoneCreating := FALSE; -- we have to finish in another function

   ELSIF OPName = 'Truncate' THEN
      IF OPParam IS NOT NULL THEN
         RAISE EXCEPTION 'Truncates do not have a paramater - specify in finishTruncate';
      END IF;

      IF ConstituentSize <> 1 THEN
         RAISE EXCEPTION 'Truncates may only be comprised of one constituent';
      END IF;

      DoneCreating := FALSE; -- we have to finish in another function

   ELSIF OPName = 'Clean' THEN
      IF ConstituentSize <> 1 THEN
         RAISE EXCEPTION 'Cleans may only be comprised of one constituent';
      END IF;

   ELSE
      RAISE EXCEPTION 'Unsupported vmeasurement type / internal error: %', OPName;
   END IF;

   INSERT INTO tblVMeasurement(VMeasurementID, VMeasurementOpID, Code, Comments, OwnerUserID, VMeasurementOpParameter, 
			       isGenerating, Objective, Version, Birthdate)
      VALUES (newID, OpID, V_Code, V_Comments, V_OwnerUserID, OPParam, TRUE,
 	      V_Objective, V_Version, V_Birthdate);

   FOR CVMId IN array_lower(Constituents, 1)..array_upper(Constituents,1) LOOP   
      INSERT INTO tblVMeasurementGroup(VMeasurementID, MemberVMeasurementID) 
         VALUES (newID, Constituents[CVMId]);
   END LOOP;

   IF DoneCreating = TRUE THEN
      UPDATE tblVMeasurement SET isGenerating = FALSE WHERE VMeasurementID = newID;
   END IF;

   RETURN newID;
END;
$_$;
 �   DROP FUNCTION cpgdb.createnewvmeasurement(character varying, integer, uuid, character varying, character varying, integer, uuid[], character varying, character varying, date);
       cpgdb          tellervo    false    7            �           1255    64593 �   createnewvmeasurement(character varying, integer, uuid, character varying, character varying, integer, uuid[], character varying, character varying, character varying, character varying)    FUNCTION     G  CREATE FUNCTION cpgdb.createnewvmeasurement(character varying, integer, uuid, character varying, character varying, integer, uuid[], character varying, character varying, character varying, character varying) RETURNS uuid
    LANGUAGE plpgsql
    AS $_$
DECLARE
   OP ALIAS FOR $1;
   OPParam ALIAS FOR $2;
   V_OwnerUserID ALIAS FOR $3;
   V_Code ALIAS FOR $4;
   V_Comments ALIAS FOR $5;
   BaseMeasurement ALIAS FOR $6;
   Constituents ALIAS FOR $7;
   V_Usage ALIAS FOR $8;
   V_UsageComments ALIAS FOR $9;
   V_Objective ALIAS FOR $10;
   V_Version ALIAS FOR $11;

   newID tblVMeasurement.VMeasurementID%TYPE;
   OPName VARCHAR;
   OPId tblVMeasurement.VMeasurementOpID%TYPE;
   ConstituentSize INTEGER;
   CVMId INTEGER;

   DoneCreating BOOLEAN;
BEGIN
   SELECT 
      uuid_generate_v1mc(), VMeasurementOpID, Name
   INTO 
      newID, OPId, OPName
   FROM tlkpVMeasurementOp WHERE tlkpVMeasurementOp.Name = OP;

   IF NOT FOUND THEN
      RAISE EXCEPTION 'Invalid VMeasurementOP %', OP;
   END IF;

   IF V_Code IS NULL THEN
      RAISE EXCEPTION 'Code must not be NULL';
   END IF;

   DoneCreating := TRUE;

   IF OpName <> 'Direct' THEN
      IF Constituents IS NULL THEN
         RAISE EXCEPTION 'Constituents must not be NULL for non-Direct VMeasurements';
      ELSE
         ConstituentSize := 1 + array_upper(Constituents, 1) - array_lower(Constituents, 1);
      END IF;
   END IF;

   IF OPName = 'Direct' THEN
      IF Constituents IS NOT NULL THEN
         RAISE EXCEPTION 'A Direct VMeasurement may not have any Constituents';
      END IF;

      IF BaseMeasurement IS NULL THEN
         RAISE EXCEPTION 'A Direct VMeasurement must have a Base MeasurementID';
      END IF;

      INSERT INTO tblVMeasurement(VMeasurementID, MeasurementID, VMeasurementOpID, Code, Comments, OwnerUserID, 
 				  Usage, UsageComments, Objective, Version)
         VALUES (newID, BaseMeasurement, OpID, V_Code, V_Comments, V_OwnerUserID, 
		 V_Usage, V_UsageComments, V_Objective, V_Version);

      RETURN newID;

   ELSIF OPName = 'Sum' THEN
      IF ConstituentSize < 1 THEN
         RAISE EXCEPTION 'Sums must have at least one constituent';
      END IF;

      IF cpgdb.VerifySumsAreContiguous(Constituents) = FALSE THEN
         NULL;      
      END IF;

   ELSIF OPName = 'Index' THEN
      IF OPParam IS NULL THEN
         RAISE EXCEPTION 'Indexes must have an index type parameter';
      END IF;

      IF ConstituentSize <> 1 THEN
         RAISE EXCEPTION 'Indexes may only be comprised of one constituent';
      END IF;

   ELSIF OPName = 'Redate' THEN
      IF OPParam IS NULL THEN
         RAISE EXCEPTION 'Redates must have a redate parameter';
      END IF;

      IF ConstituentSize <> 1 THEN
         RAISE EXCEPTION 'Redates may only be comprised of one constituent';
      END IF;

   ELSIF OPName = 'Crossdate' THEN
      IF ConstituentSize <> 1 THEN
         RAISE EXCEPTION 'Crossdates may only be comprised of one constituent';
      END IF;

      DoneCreating := FALSE; -- we have to finish in another function

   ELSIF OPName = 'Clean' THEN
      IF ConstituentSize <> 1 THEN
         RAISE EXCEPTION 'Cleans may only be comprised of one constituent';
      END IF;

   END IF;

   INSERT INTO tblVMeasurement(VMeasurementID, VMeasurementOpID, Code, Comments, OwnerUserID, VMeasurementOpParameter, 
			       isGenerating, Usage, UsageComments, Objective, Version)
      VALUES (newID, OpID, V_Code, V_Comments, V_OwnerUserID, OPParam, TRUE,
 	      V_Usage, V_UsageComments, V_Objective, V_Version);

   FOR CVMId IN array_lower(Constituents, 1)..array_upper(Constituents,1) LOOP   
      INSERT INTO tblVMeasurementGroup(VMeasurementID, MemberVMeasurementID) 
         VALUES (newID, Constituents[CVMId]);
   END LOOP;

   IF DoneCreating = TRUE THEN
      UPDATE tblVMeasurement SET isGenerating = FALSE WHERE VMeasurementID = newID;
   END IF;

   RETURN newID;
END;
$_$;
 �   DROP FUNCTION cpgdb.createnewvmeasurement(character varying, integer, uuid, character varying, character varying, integer, uuid[], character varying, character varying, character varying, character varying);
       cpgdb          tellervo    false    7            �           1255    64594    elementlocationchangedtrigger()    FUNCTION     �  CREATE FUNCTION cpgdb.elementlocationchangedtrigger() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
   vmid tblVMeasurement.VMeasurementID%TYPE;
   newextent geometry;
BEGIN
   -- no changes? don't bother!
   IF NEW.locationgeometry = OLD.locationgeometry THEN
      RETURN NEW;
   END IF;

   -- update all child vmeasurement extents
   FOR vmid IN SELECT vmeasurementID FROM cpgdb.FindChildrenOf('Element', NEW.elementID) LOOP
      -- Calculate extent of vmeasurement by looking up locations of all associated direct Measurements
      SELECT st_setsrid(extent(tblelement.locationgeometry)::geometry,4326)
         INTO  newextent
         FROM  tblelement, tblsample, tblradius, tblMeasurement, tblvmeasurement
         WHERE tblvmeasurement.measurementid=tblmeasurement.measurementid
         AND   tblmeasurement.radiusid=tblradius.radiusid
         AND   tblradius.sampleid=tblsample.sampleid
         AND   tblsample.elementid=tblelement.elementid
         AND   tblvmeasurement.vmeasurementid
            IN (SELECT vMeasurementid
                   FROM  cpgdb.FindVMParents(vmid, true)
                   WHERE FindVMParents.op='Direct');

      -- set the extent in the metacache
      UPDATE tblVMeasurementMetaCache SET vmextent=newextent WHERE vMeasurementID=vmid;

   END LOOP;

   RETURN NEW;
EXCEPTION
   WHEN internal_error THEN
      RAISE NOTICE 'WARNING: Failed to update child extents for %', NEW.elementID;
      RETURN NEW;
END;
$$;
 5   DROP FUNCTION cpgdb.elementlocationchangedtrigger();
       cpgdb          postgres    false    7            �           1255    64595 -   findchildrenof(character varying, anyelement)    FUNCTION     �  CREATE FUNCTION cpgdb.findchildrenof(character varying, anyelement) RETURNS SETOF public.typvmeasurementsearchresult
    LANGUAGE plpgsql
    AS $_$
DECLARE
   stype ALIAS FOR $1;
   id ALIAS FOR $2;

   i INTEGER;
   searches VARCHAR[] := array['VMeasurement','Measurement','Radius','Sample','Element','Object'];

   whereClause VARCHAR;
   joinClause VARCHAR;
   query VARCHAR;
   vsid uuid;
   res typVMeasurementSearchResult;
BEGIN
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
            || joinClause || ' WHERE ' || whereClause || '=''' || id || '''';


   FOR vsid IN EXECUTE query LOOP
      FOR res IN SELECT * FROM cpgdb.FindVMChildren(vsid, true) LOOP
         RETURN NEXT res;
      END LOOP;
   END LOOP;
END;
$_$;
 C   DROP FUNCTION cpgdb.findchildrenof(character varying, anyelement);
       cpgdb          postgres    false    2254    7            �           1255    64596 "   findchildrenofobjectancestor(uuid)    FUNCTION     �  CREATE FUNCTION cpgdb.findchildrenofobjectancestor(parentid uuid) RETURNS SETOF public.typvmeasurementsearchresult
    LANGUAGE plpgsql STABLE
    AS $$
DECLARE
   id tblobject.objectid%TYPE;
   res typVMeasurementSearchResult;
BEGIN
   FOR id IN SELECT objectId FROM cpgdb.FindObjectDescendants(parentid, true) LOOP
      FOR res in SELECT * FROM cpgdb.FindChildrenOf('object', id::UUID) LOOP
         RETURN NEXT res;
      END LOOP;
   END LOOP;
END;
$$;
 A   DROP FUNCTION cpgdb.findchildrenofobjectancestor(parentid uuid);
       cpgdb          postgres    false    2254    7            �           1255    64597     findelementobjectancestors(uuid)    FUNCTION     �   CREATE FUNCTION cpgdb.findelementobjectancestors(uuid) RETURNS SETOF public.tblobject
    LANGUAGE sql
    AS $_$
   SELECT * from cpgdb.recurseFindObjectAncestors((SELECT objectId FROM tblElement WHERE elementID=$1), 1)
$_$;
 6   DROP FUNCTION cpgdb.findelementobjectancestors(uuid);
       cpgdb          postgres    false    231    7            �           1255    64598 "   findobjectancestors(uuid, boolean)    FUNCTION     �   CREATE FUNCTION cpgdb.findobjectancestors(uuid, boolean) RETURNS SETOF public.tblobject
    LANGUAGE sql
    AS $_$
  SELECT * FROM cpgdb.recurseFindObjectAncestors($1, (SELECT CASE WHEN $2=TRUE THEN 1 ELSE 0 END))
$_$;
 8   DROP FUNCTION cpgdb.findobjectancestors(uuid, boolean);
       cpgdb          postgres    false    7    231            �           1255    64599 7   findobjectancestorsfromcode(character varying, boolean)    FUNCTION       CREATE FUNCTION cpgdb.findobjectancestorsfromcode(thiscode character varying, includeself boolean) RETURNS SETOF public.tblobject
    LANGUAGE plpgsql
    AS $_$DECLARE
thiscode ALIAS FOR $1;
includeself ALIAS FOR $2;
thisuuid uuid;
query VARCHAR;
res tblobject%ROWTYPE;
BEGIN

SELECT objectid INTO thisuuid FROM tblobject where code=thiscode;
IF NOT FOUND THEN
   RAISE EXCEPTION 'No objects match this code';
END IF;

FOR res IN SELECT * FROM cpgdb.findobjectancestors(thisuuid, includeself) LOOP
  RETURN NEXT res;
END LOOP;

END;$_$;
 b   DROP FUNCTION cpgdb.findobjectancestorsfromcode(thiscode character varying, includeself boolean);
       cpgdb          postgres    false    7    231            �           1255    64600 $   findobjectdescendants(uuid, boolean)    FUNCTION     �   CREATE FUNCTION cpgdb.findobjectdescendants(uuid, boolean) RETURNS SETOF public.tblobject
    LANGUAGE sql
    AS $_$
  SELECT * FROM cpgdb.recurseFindObjectDescendants($1, (SELECT CASE WHEN $2=TRUE THEN -1 ELSE 0 END))
$_$;
 :   DROP FUNCTION cpgdb.findobjectdescendants(uuid, boolean);
       cpgdb          postgres    false    7    231            �           1255    64601 9   findobjectdescendantsfromcode(character varying, boolean)    FUNCTION       CREATE FUNCTION cpgdb.findobjectdescendantsfromcode(thiscode character varying, includeself boolean) RETURNS SETOF public.tblobject
    LANGUAGE plpgsql
    AS $_$DECLARE
thiscode ALIAS FOR $1;
includeself ALIAS FOR $2;
thisuuid uuid;
query VARCHAR;
res tblobject%ROWTYPE;
BEGIN

SELECT objectid INTO thisuuid FROM tblobject where code=thiscode;
IF NOT FOUND THEN
   RAISE EXCEPTION 'No objects match this code';
END IF;

FOR res IN SELECT * FROM cpgdb.findobjectdescendants(thisuuid, includeself) LOOP
  RETURN NEXT res;
END LOOP;

END;$_$;
 d   DROP FUNCTION cpgdb.findobjectdescendantsfromcode(thiscode character varying, includeself boolean);
       cpgdb          postgres    false    7    231            �           1255    64602 $   findobjectsanddescendantswhere(text)    FUNCTION     �   CREATE FUNCTION cpgdb.findobjectsanddescendantswhere(whereclause text) RETURNS SETOF public.tblobject
    LANGUAGE sql
    AS $_$
  SELECT DISTINCT * FROM cpgdb._internalFindObjectsAndDescendantsWhere($1);
$_$;
 F   DROP FUNCTION cpgdb.findobjectsanddescendantswhere(whereclause text);
       cpgdb          postgres    false    7    231            �           1255    64603     findobjecttoplevelancestor(uuid)    FUNCTION     <  CREATE FUNCTION cpgdb.findobjecttoplevelancestor(objid uuid) RETURNS public.tblobject
    LANGUAGE plpgsql STABLE
    AS $$
DECLARE
   lastRow tblObject;
BEGIN
   SELECT * INTO lastRow FROM tblObject WHERE objectId = objid;
   
   IF NOT FOUND THEN
      RAISE EXCEPTION 'Object ID % not found. Either original object does not exist or object tree is broken.', objid;
   END IF;

   IF lastRow.parentObjectID IS NULL THEN
      RETURN lastRow;
   END IF;

   SELECT * INTO lastRow FROM cpgdb.FindObjectTopLevelAncestor(lastRow.parentObjectID);
   RETURN lastRow;
END;
$$;
 <   DROP FUNCTION cpgdb.findobjecttoplevelancestor(objid uuid);
       cpgdb          postgres    false    7    231            �           1255    64604    findvmchildren(uuid, boolean)    FUNCTION     �   CREATE FUNCTION cpgdb.findvmchildren(uuid, boolean) RETURNS SETOF public.typvmeasurementsearchresult
    LANGUAGE sql
    AS $_$
  SELECT * FROM cpgdb.recurseFindVMChildren($1, (SELECT CASE WHEN $2=TRUE THEN -1 ELSE 0 END))
$_$;
 3   DROP FUNCTION cpgdb.findvmchildren(uuid, boolean);
       cpgdb          postgres    false    7    2254            �            1259    64605    tblmeasurement    TABLE     �  CREATE TABLE public.tblmeasurement (
    measurementid integer NOT NULL,
    radiusid uuid,
    isreconciled boolean DEFAULT false,
    startyear integer DEFAULT 1001 NOT NULL,
    islegacycleaned boolean DEFAULT false,
    importtablename character varying(511),
    createdtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifiedtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    datingtypeid integer DEFAULT 3 NOT NULL,
    datingerrorpositive integer DEFAULT 0 NOT NULL,
    datingerrornegative integer DEFAULT 0 NOT NULL,
    measurementvariableid integer DEFAULT 1 NOT NULL,
    unitid integer DEFAULT 2 NOT NULL,
    power integer DEFAULT '-6'::integer NOT NULL,
    provenance character varying,
    measuringmethodid integer DEFAULT 1 NOT NULL,
    measuredbyid uuid,
    supervisedbyid uuid,
    CONSTRAINT "chk_powerIsMicrons" CHECK ((power = '-6'::integer)),
    CONSTRAINT "chk_unitsAreMicrons" CHECK ((unitid = 2))
);
 "   DROP TABLE public.tblmeasurement;
       public         heap    postgres    false                       0    0    TABLE tblmeasurement    ACL     8   GRANT ALL ON TABLE public.tblmeasurement TO "Webgroup";
          public          postgres    false    234            �           1255    64625    findvmparentmeasurements(uuid)    FUNCTION     �  CREATE FUNCTION cpgdb.findvmparentmeasurements(uuid) RETURNS SETOF public.tblmeasurement
    LANGUAGE sql
    AS $_$
  SELECT DISTINCT ON (Measurement.MeasurementID) Measurement.* FROM cpgdb.FindVMParents($1, true) parents 
  INNER JOIN tblVMeasurement vs ON parents.VMeasurementID = vs.VMeasurementID 
  INNER JOIN tblMeasurement Measurement ON Measurement.MeasurementID = vs.MeasurementID 
  WHERE parents.op='Direct';
$_$;
 4   DROP FUNCTION cpgdb.findvmparentmeasurements(uuid);
       cpgdb          postgres    false    7    234            �           1255    64626    findvmparents(uuid, boolean)    FUNCTION     �   CREATE FUNCTION cpgdb.findvmparents(uuid, boolean) RETURNS SETOF public.typvmeasurementsearchresult
    LANGUAGE sql
    AS $_$
  SELECT * FROM cpgdb.recurseFindVMParents($1, (SELECT CASE WHEN $2=TRUE THEN -1 ELSE 0 END))
$_$;
 2   DROP FUNCTION cpgdb.findvmparents(uuid, boolean);
       cpgdb          postgres    false    7    2254            �           1255    64627 -   finishcrossdate(uuid, integer, integer, text)    FUNCTION     �  CREATE FUNCTION cpgdb.finishcrossdate(uuid, integer, integer, text) RETURNS integer
    LANGUAGE plpgsql
    AS $_$
DECLARE
   XVMID ALIAS FOR $1;
   XStartRelYear ALIAS FOR $2;
   XEndRelYear ALIAS FOR $3;
   XJustification ALIAS FOR $4;

   dummy tblVMeasurement.VMeasurementID%TYPE;
BEGIN
   SELECT VMeasurementID INTO dummy FROM tblVMeasurement
      WHERE VMeasurementID=XVMID;

   IF NOT FOUND THEN
      RAISE EXCEPTION 'VMeasurement for Truncate does not exist (%)', XVMID;
   END IF;

   IF XStartRelYear IS NULL OR XEndRelYear IS NULL THEN
      RAISE EXCEPTION 'Invalid arguments to cpgdb.FinishTruncate';
   END IF;

   INSERT INTO tblCrossdate(VMeasurementID, StartRelYear, EndRelYear, Justification)
      VALUES(XVMID, XStartRelYear, XEndRelYear, XJustification);

   UPDATE tblVMeasurement SET isGenerating = FALSE WHERE VMeasurementID = XVMID;

   RETURN (SELECT TruncateID from tblTruncate WHERE VMeasurementID=XVMID);
END;
$_$;
 C   DROP FUNCTION cpgdb.finishcrossdate(uuid, integer, integer, text);
       cpgdb          postgres    false    7            �           1255    64628 3   finishcrossdate(uuid, integer, uuid, text, integer)    FUNCTION     �  CREATE FUNCTION cpgdb.finishcrossdate(uuid, integer, uuid, text, integer) RETURNS integer
    LANGUAGE plpgsql
    AS $_$
DECLARE
   XVMID ALIAS FOR $1;
   XStartYear ALIAS FOR $2;
   XMasterVMID ALIAS FOR $3;
   XJustification ALIAS FOR $4;
   XConfidence ALIAS FOR $5;

   myParentVMID tblVMeasurement.VMeasurementID%TYPE;
   masterDatingClass DatingTypeClass;
   childDatingClass DatingTypeClass;
BEGIN
   SELECT MemberVMeasurementID INTO myParentVMID FROM tblVMeasurementGroup WHERE VMeasurementID = XVMID;

   SELECT DatingClass INTO childDatingClass FROM cpgdb.getMetaCache(myParentVMID) LEFT JOIN tlkpDatingType USING (datingTypeID);

   IF NOT FOUND THEN
      RAISE EXCEPTION 'VMeasurement for Crossdate does not exist or is invalid (%)', XVMID;
   END IF;

   SELECT DatingClass INTO masterDatingClass FROM cpgdb.getMetaCache(XMasterVMID) LEFT JOIN tlkpDatingType USING (datingTypeID);

   IF NOT FOUND THEN
      RAISE EXCEPTION 'VMeasurement Master for Crossdate does not exist or is invalid (%)', XMasterVMID;
   END IF;

   IF XStartYear IS NULL OR XConfidence IS NULL THEN
      RAISE EXCEPTION 'Invalid arguments to cpgdb.FinishCrossdate';
   END IF;

   IF childDatingClass = 'inferred'::DatingTypeClass THEN
      RAISE EXCEPTION 'You cannot crossdate a series with inferred/absolute dating.';
   END IF;

   INSERT INTO tblCrossdate(VMeasurementID, MasterVMeasurementID, StartYear, Justification, Confidence)
      VALUES(XVMID, XMasterVMID, XStartYear, XJustification, XConfidence);

   UPDATE tblVMeasurement SET isGenerating = FALSE WHERE VMeasurementID = XVMID;

   RETURN (SELECT CrossdateID from tblCrossdate WHERE VMeasurementID=XVMID);
END;
$_$;
 I   DROP FUNCTION cpgdb.finishcrossdate(uuid, integer, uuid, text, integer);
       cpgdb          postgres    false    7            �           1255    64629 *   finishredate(uuid, integer, integer, text)    FUNCTION     �  CREATE FUNCTION cpgdb.finishredate(uuid, integer, integer, text) RETURNS integer
    LANGUAGE plpgsql
    AS $_$
DECLARE
   XVMID ALIAS FOR $1;
   XStartYear ALIAS FOR $2;
   XRedatingTypeID ALIAS FOR $3;
   XJustification ALIAS FOR $4;

   dummy tblVMeasurement.VMeasurementID%TYPE;
BEGIN
   SELECT VMeasurementID INTO dummy FROM tblVMeasurement
      WHERE VMeasurementID=XVMID;

   IF NOT FOUND THEN
      RAISE EXCEPTION 'VMeasurement for redate does not exist (%)', XVMID;
   END IF;

   IF XStartYear IS NULL THEN
      RAISE EXCEPTION 'Invalid arguments to cpgdb.Finishredate';
   END IF;

   INSERT INTO tblredate(VMeasurementID, StartYear, RedatingTypeID, Justification)
      VALUES(XVMID, XStartYear, XRedatingTypeID, XJustification);

   UPDATE tblVMeasurement SET isGenerating = FALSE WHERE VMeasurementID = XVMID;

   RETURN (SELECT redateID from tblredate WHERE VMeasurementID=XVMID);
END;
$_$;
 @   DROP FUNCTION cpgdb.finishredate(uuid, integer, integer, text);
       cpgdb          postgres    false    7            �           1255    64630 ,   finishtruncate(uuid, integer, integer, text)    FUNCTION     F  CREATE FUNCTION cpgdb.finishtruncate(uuid, integer, integer, text) RETURNS integer
    LANGUAGE plpgsql
    AS $_$
DECLARE
   XVMID ALIAS FOR $1;
   XStartRelYear ALIAS FOR $2;
   XEndRelYear ALIAS FOR $3;
   XJustification ALIAS FOR $4;

   myParentVMID tblVMeasurement.VMeasurementID%TYPE;
   CStart integer;
   CCount integer;
BEGIN
   SELECT MemberVMeasurementID INTO myParentVMID FROM tblVMeasurementGroup WHERE VMeasurementID = XVMID;

   IF NOT FOUND THEN
      RAISE EXCEPTION 'VMeasurement for Truncate does not exist or is invalid (%)', XVMID;
   END IF;

   SELECT StartYear,ReadingCount INTO CStart, CCount from cpgdb.getMetaCache(myParentVMID);

   IF NOT FOUND THEN
      RAISE EXCEPTION 'Parent measurement does not exist or is malformed for truncate (%/%)', XVMID, myParentVMID;
   END IF;

   IF XStartRelYear IS NULL OR XEndRelYear IS NULL THEN
      RAISE EXCEPTION 'Invalid arguments to cpgdb.FinishTruncate';
   END IF;

   IF XStartRelYear < 0 OR XStartRelYear > CCount THEN
      RAISE EXCEPTION 'Truncate StartYear is out of range (% <> [%,%])', XStartRelYear, 0, CCount;
   END IF;
   IF XEndRelYear < XStartRelYear OR XEndRelYear > CCount THEN
      RAISE EXCEPTION 'Truncate EndYear is out of range (% <> [%,%])', XEndRelYear, XStartRelYear, CCount;
   END IF;

   INSERT INTO tblTruncate(VMeasurementID, StartRelYear, EndRelYear, Justification)
      VALUES(XVMID, XStartRelYear, XEndRelYear, XJustification);

   UPDATE tblVMeasurement SET isGenerating = FALSE WHERE VMeasurementID = XVMID;

   RETURN (SELECT TruncateID from tblTruncate WHERE VMeasurementID=XVMID);
END;
$_$;
 B   DROP FUNCTION cpgdb.finishtruncate(uuid, integer, integer, text);
       cpgdb          postgres    false    7            �           1255    66925    getbestgeometry(uuid)    FUNCTION     �	  CREATE FUNCTION cpgdb.getbestgeometry(uuid) RETURNS public.geometry
    LANGUAGE plpgsql STABLE
    AS $_$
DECLARE                                                           
  theelementid ALIAS FOR $1;                                      
  objectgeom geometry;                                            
  elementgeom geometry;                                           
  objectrow tblobject;                                            
  parentobjectrow tblobject;                                      
BEGIN                                                                                                               
  
  IF exists(SELECT 1 FROM tblelement WHERE elementid=theelementid) THEN
  
	  SELECT locationgeometry INTO elementgeom                        
	  FROM tblelement e                                               
	  WHERE e.elementid=theelementid;                                 
                                                   
	  IF elementgeom IS NOT NULL THEN                                 
	    RETURN elementgeom;                                             
	  END IF;
  ELSE
  	  RETURN NULL;
  END IF;                                                         
	                                                                  
  SELECT tblobject.* INTO objectrow                               
  FROM tblelement                                                 
    LEFT JOIN tblobject ON tblelement.objectid=tblobject.objectid 
  WHERE tblelement.elementid=theelementid;                        
           
  IF objectrow.locationgeometry IS NOT NULL THEN
  	RETURN objectrow.locationgeometry;
  END IF;
                                                                        
  IF objectrow.parentobjectid IS NOT NULL THEN                    
    SELECT tblobject.* INTO parentobjectrow                         
    FROM tblobject                                                  
    WHERE tblobject.objectid=objectrow.parentobjectid;              
                                                                    
    IF objectrow.locationgeometry IS NOT NULL THEN                  
        RETURN objectrow.locationgeometry;                              
    ELSE                                                            
        RETURN NULL;                        
    END IF;          
    
  END IF;                                                         
  RETURN NULL;                                                                               
END;$_$;
 +   DROP FUNCTION cpgdb.getbestgeometry(uuid);
       cpgdb          tellervo    false    3    3    3    3    3    3    3    3    7            �           1255    66836    getbestgeometryid(uuid)    FUNCTION     t	  CREATE FUNCTION cpgdb.getbestgeometryid(uuid) RETURNS uuid
    LANGUAGE plpgsql STABLE
    AS $_$
DECLARE                                                           
  theelementid ALIAS FOR $1;                                      
  objectgeom geometry;                                            
  elementgeom geometry;                                           
  objectrow tblobject;                                            
  parentobjectrow tblobject;                                      
BEGIN                                                                                                               
  SELECT locationgeometry INTO elementgeom                        
  FROM tblelement e                                               
  WHERE e.elementid=theelementid;                                 
                                                                  
  IF elementgeom IS NOT NULL THEN                                 
    RETURN theelementid;                                             
  END IF;                                                         
                                                                  
  SELECT tblobject.* INTO objectrow                               
  FROM tblelement                                                 
    LEFT JOIN tblobject ON tblelement.objectid=tblobject.objectid 
  WHERE tblelement.elementid=theelementid;                        
           
  IF objectrow.locationgeometry IS NOT NULL THEN
  	RETURN objectrow.objectid;
  END IF;
                                                                        
  IF objectrow.parentobjectid IS NOT NULL THEN                    
    SELECT tblobject.* INTO parentobjectrow                         
    FROM tblobject                                                  
    WHERE tblobject.objectid=objectrow.parentobjectid;              
                                                                    
    IF objectrow.locationgeometry IS NOT NULL THEN                  
        RETURN objectrow.objectid;                              
    ELSE                                                            
        RETURN parentobjectrow.objectid;                        
    END IF;          
    
  END IF;                                                         
  RETURN objectrow.objectid;                                                                               
END;$_$;
 -   DROP FUNCTION cpgdb.getbestgeometryid(uuid);
       cpgdb          tellervo    false    7            �           1255    66933 "   getdateday(date, public.date_prec)    FUNCTION     �  CREATE FUNCTION cpgdb.getdateday(thedate date, thedateprec public.date_prec) RETURNS character varying
    LANGUAGE plpgsql STABLE
    AS $_$
DECLARE                                                           
  thedate ALIAS FOR $1;                                                                                
  thedateprec ALIAS FOR $2;                                            
BEGIN                                                                                                               
  
  IF thedate IS NULL THEN
  	RETURN NULL;
  END IF;
  
  IF(thedateprec = 'year' OR thedateprec = 'month') THEN 
  	RETURN NULL;
  END IF;
  	
  RETURN to_char(thedate, 'FMDD');
  	                                         
END;$_$;
 L   DROP FUNCTION cpgdb.getdateday(thedate date, thedateprec public.date_prec);
       cpgdb          tellervo    false    2218    7            �           1255    64631 !   getdatefromstr(character varying)    FUNCTION       CREATE FUNCTION cpgdb.getdatefromstr(datestr character varying) RETURNS date
    LANGUAGE plpgsql
    AS $_$DECLARE

 datestr ALIAS FOR $1;

BEGIN

IF regexp_matches(datestr, '((18|19|20)\d\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01]))$') IS NOT NULL THEN
   --RAISE NOTICE 'YYYY-MM-DD';
   RETURN to_date(datestr, 'YYYY-MM-DD');     

ELSIF regexp_matches(datestr, '((18|19|20)\d\d[- /.](0[1-9]|1[012]))$') IS NOT NULL THEN
   --RAISE NOTICE 'YYYY-MM';
   RETURN to_date(datestr, 'YYYY-MM');

ELSIF regexp_matches(datestr, '((18|19|20)\d\d)$') IS NOT NULL THEN
   --RAISE NOTICE 'YYYY';
   RETURN to_date(datestr, 'YYYY');

ELSE
   RAISE EXCEPTION 'String ''%'' is not a valid date representation.  Must be YYYY, YYYY-MM, or YYYY-MM-DD', datestr;

END IF;

RETURN NULL;


END;$_$;
 ?   DROP FUNCTION cpgdb.getdatefromstr(datestr character varying);
       cpgdb          tellervo    false    7            �           1255    66932 $   getdatemonth(date, public.date_prec)    FUNCTION     �  CREATE FUNCTION cpgdb.getdatemonth(thedate date, thedateprec public.date_prec) RETURNS character varying
    LANGUAGE plpgsql STABLE
    AS $_$
DECLARE                                                           
  thedate ALIAS FOR $1;                                                                                
  thedateprec ALIAS FOR $2;                                            
BEGIN                                                                                                               
  
  IF thedate IS NULL THEN
  	RETURN NULL;
  END IF;
  
  IF(thedateprec = 'year') THEN 
  	RETURN NULL;
  END IF;
  	
  RETURN to_char(thedate, 'FMMM');
  	                                         
END;$_$;
 N   DROP FUNCTION cpgdb.getdatemonth(thedate date, thedateprec public.date_prec);
       cpgdb          tellervo    false    2218    7            �           1255    64632 %   getdateprecfromstr(character varying)    FUNCTION     �  CREATE FUNCTION cpgdb.getdateprecfromstr(datestr character varying) RETURNS public.date_prec
    LANGUAGE plpgsql
    AS $_$DECLARE

 datestr ALIAS FOR $1;

BEGIN

IF regexp_matches(datestr, '((18|19|20)\d\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01]))$') IS NOT NULL THEN
   --RAISE NOTICE 'YYYY-MM-DD';
   RETURN 'day';   

ELSIF regexp_matches(datestr, '((18|19|20)\d\d[- /.](0[1-9]|1[012]))$') IS NOT NULL THEN
   --RAISE NOTICE 'YYYY-MM';
   RETURN 'month';

ELSIF regexp_matches(datestr, '((18|19|20)\d\d)$') IS NOT NULL THEN
   --RAISE NOTICE 'YYYY';
   RETURN 'year';

ELSE
   RAISE EXCEPTION 'String ''%'' is not a valid date representation.  Must be YYYY, YYYY-MM, or YYYY-MM-DD', datestr;

END IF;

RETURN NULL;


END;$_$;
 C   DROP FUNCTION cpgdb.getdateprecfromstr(datestr character varying);
       cpgdb          tellervo    false    7    2218            �           1255    64633 %   getdatestring(date, public.date_prec)    FUNCTION     (  CREATE FUNCTION cpgdb.getdatestring(thedate date, thedateprec public.date_prec) RETURNS character varying
    LANGUAGE plpgsql
    AS $_$DECLARE

  thedate ALIAS FOR $1;
  thedateprec ALIAS FOR $2;

BEGIN

  IF thedate IS NULL THEN
      RETURN NULL;
  END IF;

  IF thedateprec = 'year' THEN
      RETURN to_char(thedate, 'YYYY');
  ELSIF thedateprec = 'month' THEN
      RETURN to_char(thedate, 'YYYY-MM');
  ELSIF thedateprec = 'day' THEN
      RETURN to_char(thedate, 'YYYY-MM-DD');
  END IF;

  RAISE EXCEPTION 'Invalid date precision';

END;$_$;
 O   DROP FUNCTION cpgdb.getdatestring(thedate date, thedateprec public.date_prec);
       cpgdb          tellervo    false    7    2218            �           1255    66931 #   getdateyear(date, public.date_prec)    FUNCTION     �  CREATE FUNCTION cpgdb.getdateyear(thedate date, thedateprec public.date_prec) RETURNS character varying
    LANGUAGE plpgsql STABLE
    AS $_$
DECLARE                                                           
  thedate ALIAS FOR $1;                                                                                
  thedateprec ALIAS FOR $2;                                            
BEGIN                                                                                                               
  
  IF thedate IS NULL THEN
  	RETURN NULL;
  END IF;
  
  RETURN to_char(thedate, 'YYYY');
  	                                         
END;$_$;
 M   DROP FUNCTION cpgdb.getdateyear(thedate date, thedateprec public.date_prec);
       cpgdb          tellervo    false    7    2218            �            1259    64634    tblvmeasurementderivedcache    TABLE     �   CREATE TABLE public.tblvmeasurementderivedcache (
    derivedcacheid integer NOT NULL,
    vmeasurementid uuid NOT NULL,
    measurementid integer NOT NULL
);
 /   DROP TABLE public.tblvmeasurementderivedcache;
       public         heap    postgres    false                       0    0 !   TABLE tblvmeasurementderivedcache    COMMENT     �   COMMENT ON TABLE public.tblvmeasurementderivedcache IS 'A non-recursive cache for breaking down vmeasurement derivations. Provides a map from vmeasurementid (one) to measurementid (many) and vice versa. Updated by metacache functions.';
          public          postgres    false    235                       0    0 !   TABLE tblvmeasurementderivedcache    ACL     E   GRANT ALL ON TABLE public.tblvmeasurementderivedcache TO "Webgroup";
          public          postgres    false    235            �           1255    64637    getderivedcache(uuid)    FUNCTION     �   CREATE FUNCTION cpgdb.getderivedcache(uuid) RETURNS SETOF public.tblvmeasurementderivedcache
    LANGUAGE sql
    AS $_$
   SELECT * FROM cpgdb.GetMetaCache($1);
   SELECT * FROM tblVMeasurementDerivedCache WHERE VMeasurementID = $1;
$_$;
 +   DROP FUNCTION cpgdb.getderivedcache(uuid);
       cpgdb          postgres    false    7    235                       0    0 &   FUNCTION geometrytype(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometrytype(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometrytype(public.geometry) TO "Webgroup";
          public          postgres    false    927            �            1259    64638 
   tblelement    TABLE     �	  CREATE TABLE public.tblelement (
    elementid uuid DEFAULT public.uuid_generate_v1mc() NOT NULL,
    locationprecision double precision DEFAULT 10000,
    code character varying(100) NOT NULL,
    createdtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifiedtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    locationgeometry public.geometry,
    islivetree boolean DEFAULT false,
    originaltaxonname character varying(100),
    locationtypeid integer,
    locationcomment character varying,
    file character varying[],
    description character varying,
    processing character varying,
    marks character varying,
    diameter double precision,
    width double precision,
    height double precision,
    depth double precision,
    unsupportedxml character varying,
    unitsold character varying,
    objectid uuid NOT NULL,
    elementtypeid integer NOT NULL,
    elementauthenticityid integer,
    elementshapeid integer,
    altitudeint integer,
    slopeangle integer,
    slopeazimuth integer,
    soildescription character varying,
    soildepth double precision,
    bedrockdescription character varying,
    comments character varying,
    locationaddressline2 character varying,
    locationcityortown character varying,
    locationstateprovinceregion character varying,
    locationpostalcode character varying,
    locationcountry character varying,
    locationaddressline1 character varying,
    altitude double precision,
    gispkey integer NOT NULL,
    units integer,
    authenticity character varying,
    domainid integer DEFAULT 0,
    taxonid character varying,
    CONSTRAINT check_precisionpositive CHECK ((locationprecision >= (0)::double precision)),
    CONSTRAINT enforce_dims_location CHECK ((public.st_ndims(locationgeometry) = 2)),
    CONSTRAINT enforce_geotype_location CHECK (((public.geometrytype(locationgeometry) = 'POINT'::text) OR (locationgeometry IS NULL))),
    CONSTRAINT enforce_srid_location CHECK ((public.st_srid(locationgeometry) = 4326)),
    CONSTRAINT enforce_units CHECK ((((height IS NULL) AND (units IS NULL)) OR ((height IS NOT NULL) AND (units IS NOT NULL)))),
    CONSTRAINT enforce_valid_dimensions CHECK ((((diameter IS NULL) AND (width IS NULL) AND (depth IS NULL) AND (height IS NULL)) OR ((diameter IS NOT NULL) AND (height IS NOT NULL) AND (depth IS NULL) AND (width IS NULL)) OR ((diameter IS NULL) AND (height IS NOT NULL) AND (width IS NOT NULL) AND (depth IS NOT NULL))))
);
    DROP TABLE public.tblelement;
       public         heap    postgres    false    1211    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3                       0    0 #   COLUMN tblelement.locationprecision    COMMENT     \   COMMENT ON COLUMN public.tblelement.locationprecision IS 'precision of lat/long in meters';
          public          postgres    false    236                       0    0    COLUMN tblelement.description    COMMENT     Z   COMMENT ON COLUMN public.tblelement.description IS 'Other information about the element';
          public          postgres    false    236                       0    0    COLUMN tblelement.processing    COMMENT     a   COMMENT ON COLUMN public.tblelement.processing IS 'Processing (carved, sawn etc) rafting marks';
          public          postgres    false    236                       0    0    COLUMN tblelement.marks    COMMENT     M   COMMENT ON COLUMN public.tblelement.marks IS 'Carpenter marks inscriptions';
          public          postgres    false    236                       0    0     COLUMN tblelement.unsupportedxml    COMMENT     w   COMMENT ON COLUMN public.tblelement.unsupportedxml IS 'Raw XML for fields that are not currently supported in Corina';
          public          postgres    false    236                       0    0    TABLE tblelement    ACL     4   GRANT ALL ON TABLE public.tblelement TO "Webgroup";
          public          postgres    false    236            �           1255    64656 +   getelementsforobjectcode(character varying)    FUNCTION       CREATE FUNCTION cpgdb.getelementsforobjectcode(objectcode character varying) RETURNS SETOF public.tblelement
    LANGUAGE plpgsql
    AS $_$DECLARE
  objcode ALIAS FOR $1;
  query VARCHAR;
  el tblelement%ROWTYPE;
BEGIN
query := 'SELECT tblelement.* 
	FROM tblelement INNER JOIN tblobject ON (tblelement.objectid = tblobject.objectid)
	WHERE tblelement.objectid 
	  IN ( SELECT objectid FROM cpgdb.findobjectdescendantsfromcode('''|| objcode || ''', true))';

FOR el IN EXECUTE query LOOP
   RETURN NEXT el;
END LOOP;
	  
END;$_$;
 L   DROP FUNCTION cpgdb.getelementsforobjectcode(objectcode character varying);
       cpgdb          postgres    false    7    236            �            1259    64657    tblsecuritygroup    TABLE     �   CREATE TABLE public.tblsecuritygroup (
    securitygroupid integer NOT NULL,
    name character varying(31) NOT NULL,
    description character varying(255) NOT NULL,
    isactive boolean DEFAULT true NOT NULL
);
 $   DROP TABLE public.tblsecuritygroup;
       public         heap    postgres    false                       0    0    TABLE tblsecuritygroup    ACL     :   GRANT ALL ON TABLE public.tblsecuritygroup TO "Webgroup";
          public          postgres    false    237            �           1255    64661    getgroupmembership(uuid)    FUNCTION     �  CREATE FUNCTION cpgdb.getgroupmembership(uuid) RETURNS SETOF public.tblsecuritygroup
    LANGUAGE plpgsql
    AS $_$
DECLARE
   _securityUserID ALIAS FOR $1;

   res tblSecurityGroup%ROWTYPE;
   parentRes tblSecurityGroup%ROWTYPE;
BEGIN
   FOR res IN SELECT g.* FROM tblSecurityGroup g
              INNER JOIN tblSecurityUserMembership m ON m.securityGroupID = g.securityGroupID
              WHERE m.securityUserID = _securityUserID AND g.isActive = true
   LOOP
      RETURN NEXT res;

      FOR parentRes IN SELECT * FROM cpgdb.recurseGetParentGroups(res.securityGroupID, 0) 
      LOOP
         RETURN NEXT parentRes;
      END LOOP;
   END LOOP;
END;
$_$;
 .   DROP FUNCTION cpgdb.getgroupmembership(uuid);
       cpgdb          postgres    false    7    237                       0    0 !   FUNCTION getgroupmembership(uuid)    ACL     D   GRANT ALL ON FUNCTION cpgdb.getgroupmembership(uuid) TO "Webgroup";
          cpgdb          postgres    false    1243            �           1255    64662    getgroupmembershiparray(uuid)    FUNCTION     �   CREATE FUNCTION cpgdb.getgroupmembershiparray(uuid) RETURNS integer[]
    LANGUAGE sql IMMUTABLE
    AS $_$
   SELECT ARRAY(SELECT DISTINCT SecurityGroupID FROM cpgdb.GetGroupMembership($1) ORDER BY SecurityGroupID ASC);
$_$;
 3   DROP FUNCTION cpgdb.getgroupmembershiparray(uuid);
       cpgdb          postgres    false    7                       0    0 &   FUNCTION getgroupmembershiparray(uuid)    ACL     I   GRANT ALL ON FUNCTION cpgdb.getgroupmembershiparray(uuid) TO "Webgroup";
          cpgdb          postgres    false    1244            �           1255    64663 :   getgrouppermissions(integer[], character varying, integer)    FUNCTION     �  CREATE FUNCTION cpgdb.getgrouppermissions(integer[], character varying, integer) RETURNS character varying[]
    LANGUAGE plpgsql
    AS $_$
DECLARE
   _groupIDs ALIAS FOR $1;
   _ptype ALIAS FOR $2;
   _pid ALIAS FOR $3;

   permissionType varchar := lower(_ptype);
   query varchar;
   i integer;
   objectid integer;
   res refcursor;
   perm varchar;
   perms varchar[];

   stypes varchar[] := array['vmeasurement','tree','object','default'];
BEGIN
   IF NOT (permissionType = ANY(stypes)) THEN
      RAISE EXCEPTION 'Invalid permission type: %. Should be one of vmeasurement, tree, object, default.', _ptype;
   END IF;

   IF permissionType = 'default' THEN
      RAISE NOTICE 'Fell back to read only defaults; implement defaults??';
      RETURN ARRAY['Read'];
   END IF;

   query := 'SELECT DISTINCT perm.name FROM tblSecurity' || permissionType || ' obj ' ||
            'INNER JOIN ArrayToRows(ARRAY[' || array_to_string(_groupIDs, ',') || ']) AS membership ON obj.securityGroupID = membership ' ||
            'INNER JOIN tlkpSecurityPermission perm ON perm.securityPermissionID = obj.securityPermissionID ' || 
            'WHERE ' || permissionType || 'Id =' || _pid;

   OPEN res FOR EXECUTE query;
   FETCH res INTO perm;

   IF NOT FOUND THEN
      IF permissionType = 'vmeasurement' THEN
         RETURN cpgdb.GetGroupPermissions(_groupIDs, 'default', 0);

      ELSEIF permissionType = 'tree' THEN
         SELECT tblSubobject.objectID INTO objectid FROM tblTree, tblSubobject 
                WHERE tblTree.subobjectID=tblSubobject.SubobjectID
                AND tblTree.elementid = _pid;

         RETURN cpgdb.GetGroupPermissions(_groupIDs, 'object', objectid);

      ELSEIF permissionType = 'object' THEN

         RETURN cpgdb.GetGroupPermissions(_groupIDs, 'default', 0);
      END IF;
   END IF;

   i := 0;
   LOOP
      IF perm = 'No permissions' THEN
         CLOSE res;
         RETURN ARRAY['No permissions'];
      END IF;

      perms[i] = perm;
      i := i + 1;

      FETCH res INTO perm;

      IF NOT FOUND THEN
         EXIT; -- We've reached the end of our list of permissions
      END IF;
   END LOOP;

   CLOSE res;
   RETURN perms;
END;
$_$;
 P   DROP FUNCTION cpgdb.getgrouppermissions(integer[], character varying, integer);
       cpgdb          postgres    false    7                       0    0 C   FUNCTION getgrouppermissions(integer[], character varying, integer)    ACL     f   GRANT ALL ON FUNCTION cpgdb.getgrouppermissions(integer[], character varying, integer) TO "Webgroup";
          cpgdb          postgres    false    1245            �           1255    64664 <   getgrouppermissionset(integer[], character varying, integer)    FUNCTION     �  CREATE FUNCTION cpgdb.getgrouppermissionset(_groupids integer[], _permtype character varying, _pid integer) RETURNS public.typpermissionset
    LANGUAGE plpgsql
    AS $$
DECLARE
   query varchar;
   whereClause varchar;
   objid integer;
   res refcursor;
   perm varchar;
   vmtype varchar;
   perms typPermissionSet;
   childPerms typPermissionSet;
   setSize integer;

   stypes varchar[] := array['vmeasurement','tree','object','default'];
BEGIN
   IF NOT (_permtype = ANY(stypes)) THEN
      RAISE EXCEPTION 'Invalid permission type: %. Should be one of vmeasurement, tree, object, default (case matters!).', _permtype;
   END IF;

   IF _permtype = 'default' THEN
      whereClause := '';
   ELSE
      whereClause := ' WHERE ' || _permType || 'Id = ' || _pid;
   END IF;
   
   query := 'SELECT DISTINCT perm.name FROM tblSecurity' || _permType || ' obj ' ||
            'INNER JOIN ArrayToRows(ARRAY[' || array_to_string(_groupIDs, ',') || ']) AS membership ON obj.securityGroupID = membership ' ||
            'INNER JOIN tlkpSecurityPermission perm ON perm.securityPermissionID = obj.securityPermissionID' || whereClause;


   OPEN res FOR EXECUTE query;
   FETCH res INTO perm;

   IF NOT FOUND THEN
      CLOSE res;
      
      IF _permtype = 'vmeasurement' THEN
         SELECT op.Name,tr.TreeID INTO vmtype,objid FROM tblVMeasurement vm 
            INNER JOIN tlkpVMeasurementOp op ON vm.VMeasurementOpID = op.VMeasurementOpID
            LEFT JOIN tblMeasurement t1 ON vm.MeasurementID = t1.MeasurementID 
            LEFT JOIN tblRadius t2 ON t2.RadiusID = t1.RadiusID 
            LEFT JOIN tblSpecimen t3 on t3.SpecimenID = t2.SpecimenID 
            LEFT JOIN tblTree tr ON tr.TreeID = t3.TreeID
            WHERE vm.VMeasurementID = _pid;

         IF NOT FOUND THEN
            RAISE EXCEPTION 'Could not determine security: vmeasurement % -> tree does not exist', _pid;
         END IF;

         IF vmtype = 'Direct' THEN
            perms := cpgdb.GetGroupPermissionSet(_groupIDs, 'tree', objid);
	    RETURN perms;
         ELSE
            setSize := 0;

            FOR objid IN SELECT MemberVMeasurementID FROM tblVMeasurementGroup WHERE VMeasurementID = _pid LOOP
               childPerms := cpgdb.GetGroupPermissionSet(_groupIDs, 'vmeasurement', objid);

               IF setSize = 0 THEN
                  perms := childperms;
               ELSE
                  perms := cpgdb.AndPermissionSets(perms, childPerms);
               END IF;

               setSize := setSize + 1;
               
            END LOOP;
            RETURN perms;
         END IF;
         
      ELSIF _permType = 'tree' THEN
         SELECT tblSubobject.objectID INTO objid FROM tblTree, tblSubobject
                WHERE tblTree.subobjectID=tblSubobject.SubobjectID
                AND tblTree.elementid = _pid;

         IF NOT FOUND THEN
            RAISE EXCEPTION 'Could not determine security: tree % -> object does not exist', _pid;
         END IF;
         
         perms := cpgdb.GetGroupPermissionSet(_groupIDs, 'object', objid);
         RETURN perms;
         
      ELSIF _permType = 'object' THEN
         perms := cpgdb.GetGroupPermissionSet(_groupIDs, 'default', 0);
         RETURN perms;

      ELSE
	 -- No defaults?!?! Ahh!
	 perms.denied := true;
	 perms.canRead := false;
	 perms.canDelete := false;
	 perms.canUpdate := false;
	 perms.canCreate := false;
	 perms.decidedBy := 'No defaults';
	 RETURN perms;

      END IF;

   ELSE
      perms.decidedBy := _permType || ' ' || _pid;
      perms.denied := false;
      perms.canRead := false;
      perms.canDelete := false;
      perms.canUpdate := false;
      perms.canCreate := false;

      LOOP
         IF perm = 'No permission' THEN
 	    perms.denied = true;
         ELSIF perm = 'Read' THEN
            perms.canRead := true;
         ELSIF perm = 'Create' THEN
            perms.canCreate := true;
         ELSIF perm = 'Update' THEN
            perms.canUpdate := true;
         ELSIF perm = 'Delete' THEN
            perms.canDelete := true;
         END IF; 

         FETCH res INTO perm;

         IF NOT FOUND THEN
            EXIT; -- at the end of our permission list
         END IF;
      END LOOP;
   END IF;
   
   CLOSE res;
   RETURN perms;
END;
$$;
 k   DROP FUNCTION cpgdb.getgrouppermissionset(_groupids integer[], _permtype character varying, _pid integer);
       cpgdb          postgres    false    7    2251            �           1255    64665 9   getgrouppermissionset(integer[], character varying, uuid)    FUNCTION       CREATE FUNCTION cpgdb.getgrouppermissionset(_groupids integer[], _permtype character varying, _pid uuid) RETURNS public.typpermissionset
    LANGUAGE plpgsql
    AS $$
DECLARE
   query varchar;
   whereClause varchar;
   objid uuid;
   res refcursor;
   perm varchar;
   vstype varchar;
   perms typPermissionSet;
   childPerms typPermissionSet;
   setSize integer;

   stypes varchar[] := array['measurementSeries', 'derivedSeries', 'vmeasurement','element','object','default'];
BEGIN


   IF NOT (_permtype = ANY(stypes)) THEN
      RAISE EXCEPTION 'Invalid permission type: %. Should be one of vmeasurement, element, object, default (case matters!).', _permtype;
   END IF;

   IF _permtype = 'measurementSeries' OR _permtype = 'derivedSeries' THEN
      _permtype = 'vmeasurement';
   END IF;


   IF _permtype = 'default' THEN
      whereClause := '';
   ELSE
      whereClause := ' WHERE ' || _permType || 'Id = ' ||  '''' || _pid ||'''' ;
   END IF;
   
   query := 'SELECT DISTINCT perm.name FROM tblSecurity' || _permType || ' obj ' ||
            'INNER JOIN ArrayToRows(ARRAY[' || array_to_string(_groupIDs, ',') || ']) AS membership ON obj.securityGroupID = membership ' ||
            'INNER JOIN tlkpSecurityPermission perm ON perm.securityPermissionID = obj.securityPermissionID' || whereClause;


   OPEN res FOR EXECUTE query;
   FETCH res INTO perm;

   IF NOT FOUND THEN
      CLOSE res;
      
      IF _permtype = 'vmeasurement' OR _permtype='measurementSeries' OR _permtype='derivedSeries'  THEN
         SELECT op.Name,tr.ElementID INTO vstype,objid FROM tblVMeasurement vs 
            INNER JOIN tlkpVMeasurementOp op ON vs.VMeasurementOpID = op.VMeasurementOpID
            LEFT JOIN tblMeasurement t1 ON vs.MeasurementID = t1.MeasurementID 
            LEFT JOIN tblRadius t2 ON t2.RadiusID = t1.RadiusID 
            LEFT JOIN tblSample t3 on t3.SampleID = t2.SampleID 
            LEFT JOIN tblElement tr ON tr.ElementID = t3.ElementID
            WHERE vs.VMeasurementID =   _pid  ;

         IF NOT FOUND THEN
            RAISE EXCEPTION 'Could not determine security: vmeasurement % -> element does not exist', _pid;
         END IF;

         IF vstype = 'Direct' THEN
            perms := cpgdb.GetGroupPermissionSet(_groupIDs, 'element', objid);
    RETURN perms;
         ELSE
            setSize := 0;

            FOR objid IN SELECT MemberVMeasurementID FROM tblVMeasurementGroup WHERE VMeasurementID = _pid  LOOP
               childPerms := cpgdb.GetGroupPermissionSet(_groupIDs, 'vmeasurement', objid);

               IF setSize = 0 THEN
                  perms := childperms;
               ELSE
                  perms := cpgdb.AndPermissionSets(perms, childPerms);
               END IF;

               setSize := setSize + 1;
               
            END LOOP;
            RETURN perms;
         END IF;
         
      ELSIF _permType = 'element' THEN
         SELECT tblelement.objectID INTO objid FROM tblElement
                WHERE tblElement.elementid = _pid;

         IF NOT FOUND THEN
            RAISE EXCEPTION 'Could not determine security: element % -> object does not exist', _pid;
         END IF;
         
         perms := cpgdb.GetGroupPermissionSet(_groupIDs, 'object', objid);
         RETURN perms;
         
      ELSIF _permType = 'object' THEN
         perms := cpgdb.GetGroupPermissionSet(_groupIDs, 'default', 'ae68d6d2-2294-11e1-9c20-4ffbb19115a7'::uuid);
         RETURN perms;

      ELSE
 perms.denied := true;
 perms.canRead := false;
 perms.canDelete := false;
 perms.canUpdate := false;
 perms.canCreate := false;
 perms.decidedBy := 'No defaults';
 RETURN perms;

      END IF;

   ELSE

      IF _permType = 'default' THEN
         perms.decidedBy := 'Database defaults';
      ELSE
         perms.decidedBy := 'Permissions overriden at the ' || _permType || ' level';
      END IF;
   
      
      perms.denied := false;
      perms.canRead := false;
      perms.canDelete := false;
      perms.canUpdate := false;
      perms.canCreate := false;

      LOOP
         IF perm = 'No permission' THEN
     perms.denied = true;
         ELSIF perm = 'Read' THEN
            perms.canRead := true;
         ELSIF perm = 'Create' THEN
            perms.canCreate := true;
         ELSIF perm = 'Update' THEN
            perms.canUpdate := true;
         ELSIF perm = 'Delete' THEN
            perms.canDelete := true;
         END IF; 

         FETCH res INTO perm;

         IF NOT FOUND THEN
            EXIT; -- at the end of our permission list
         END IF;
      END LOOP;
   END IF;
   
   CLOSE res;
   RETURN perms;
END;
$$;
 h   DROP FUNCTION cpgdb.getgrouppermissionset(_groupids integer[], _permtype character varying, _pid uuid);
       cpgdb          postgres    false    7    2251            �           1255    64666    getlabel(text, uuid, boolean)    FUNCTION     i  CREATE FUNCTION cpgdb.getlabel(text, uuid, boolean) RETURNS text
    LANGUAGE plpgsql STABLE
    AS $_$
DECLARE
   OBJID ALIAS FOR $2;
   PrefixOnly ALIAS FOR $3;
   labelfor text;
   
   queryLevel integer;
   query text;
   selection text;
   whereClause text;

   rec record;

   objectn text;
   elementn text;
   samplen text;
   radiusn text;
   measurementn text;
   ret text;
   suffix text;

   count integer;

BEGIN
   labelfor := lower($1);
   
   IF labelfor = 'vmeasurement' THEN 
      count := 0;
      FOR rec IN SELECT s.code as a,t.code as c,sp.code as d,r.code as e,vm.code as f 
	FROM tblVMeasurementDerivedCache d
	INNER JOIN tblMeasurement m ON m.MeasurementID = d.MeasurementID
	INNER JOIN tblRadius r on r.radiusID = m.radiusID
	INNER JOIN tblSample sp on sp.sampleID = r.sampleID
	INNER JOIN tblElement t on t.elementID = sp.elementID
	INNER JOIN tblObject s on s.objectID = t.objectID
	INNER JOIN tblVMeasurement vm on vm.vmeasurementid = d.vmeasurementid
	WHERE d.VMeasurementID = OBJID
      LOOP
         count := count + 1;

         objectn := rec.a;
         elementn := rec.c;
         samplen := rec.d;
         radiusn := rec.e;
         measurementn := rec.f;
      END LOOP;

      IF count = 0 THEN
         RAISE EXCEPTION 'No data found for vmeasurement %', OBJID;
      ELSIF COUNT > 1 THEN

         IF PrefixOnly THEN
            RETURN '';
         END IF;

         RETURN measurementn;
      END IF;

      ret := 'C-' || objectn;

      ret := ret || '-' || elementn || '-' || samplen || '-' || radiusn || '-';

      IF NOT PrefixOnly THEN
         ret := ret || measurementn;
      END IF;

      RETURN ret;
   END IF; -- VMeasurement special case

   IF labelfor = 'element' THEN
      queryLevel := 1;
      whereClause := ' WHERE t.elementid=' || OBJID;
   ELSIF labelfor = 'sample' THEN
      queryLevel := 2;      
      whereClause := ' WHERE sp.sampleid=' || OBJID;
   ELSIF labelfor = 'radius' THEN
      queryLevel := 3;      
      whereClause := ' WHERE r.radiusid=' || OBJID;
   ELSE
      RAISE EXCEPTION 'Invalid usage: label must be for vmeasurement, element, sample, or radius';
   END IF;

   selection := 's.code as a,t.code as c';
   query := ' FROM tblobject s INNER JOIN tblelement t ON t.objectid = s.objectid';

   IF queryLevel > 1 THEN
      query := query || ' INNER JOIN tblsample sp ON sp.elementid = t.elementid';
      selection := selection || ',sp.code as d';
   END IF;

   IF queryLevel > 2 THEN
      query := query || ' INNER JOIN tblradius r ON r.sampleid = sp.sampleid';
      selection := selection || ',r.code as e';
   END IF;

   FOR rec IN EXECUTE 'SELECT ' || selection || query || whereClause LOOP
      ret := 'C-' || rec.a;

      IF PrefixOnly THEN
         IF queryLevel = 1 THEN
           suffix := '-';
         ELSIF queryLevel = 2 THEN
           suffix := '-' || rec.c || '-';
         ELSIF queryLevel = 3 THEN
           suffix := '-' || rec.c || '-' || rec.d || '-';
         END IF;
      ELSE
         IF queryLevel = 1 THEN
           suffix := '-' || rec.c;
         ELSIF queryLevel = 2 THEN
           suffix := '-' || rec.c || '-' || rec.d;
         ELSIF queryLevel = 3 THEN
           suffix := '-' || rec.c || '-' || rec.d || '-' || rec.e;
         END IF;
      END IF;

      RETURN ret || suffix;
   END LOOP; -- we only get here if nothing was found!

   RAISE EXCEPTION 'No results for % %', $1, $2;
END;
$_$;
 3   DROP FUNCTION cpgdb.getlabel(text, uuid, boolean);
       cpgdb          postgres    false    7            �            1259    64667    tblloan    TABLE     i  CREATE TABLE public.tblloan (
    loanid uuid NOT NULL,
    firstname character varying,
    lastname character varying,
    organisation character varying,
    duedate timestamp with time zone,
    issuedate timestamp with time zone DEFAULT now() NOT NULL,
    returndate timestamp with time zone,
    files character varying[],
    notes character varying
);
    DROP TABLE public.tblloan;
       public         heap    tellervo    false            �           1255    64674    getloanfromboxid(uuid)    FUNCTION       CREATE FUNCTION cpgdb.getloanfromboxid(boxid uuid) RETURNS public.tblloan
    LANGUAGE sql
    AS $_$
SELECT * from tblloan where loanid=(SELECT loanid FROM tblcurationevent WHERE boxid=$1 AND loanid IS NOT NULL ORDER BY createdtimestamp DESC LIMIT 1);
$_$;
 2   DROP FUNCTION cpgdb.getloanfromboxid(boxid uuid);
       cpgdb          tellervo    false    238    7            �           1255    64675    getmetacache(uuid)    FUNCTION     �  CREATE FUNCTION cpgdb.getmetacache(uuid) RETURNS public.tblvmeasurementmetacache
    LANGUAGE plpgsql
    AS $_$
DECLARE
   vmid ALIAS FOR $1;
   ret tblVMeasurementMetaCache%ROWTYPE;
BEGIN
   SELECT * INTO ret FROM tblVMeasurementMetaCache WHERE VMeasurementID = vmid;
   IF FOUND THEN
      RETURN ret;
   END IF;

   RAISE NOTICE 'Cache miss on %', vmid;
   SELECT * INTO ret FROM cpgdb.CreateMetaCache(vmid);
   RETURN ret;
END;
$_$;
 (   DROP FUNCTION cpgdb.getmetacache(uuid);
       cpgdb          postgres    false    233    7            �           1255    64676    getnote(text, text)    FUNCTION     �  CREATE FUNCTION cpgdb.getnote(text, text) RETURNS public.tlkpreadingnote
    LANGUAGE plpgsql STABLE
    AS $_$
DECLARE
   noterec tlkpReadingNote;
BEGIN
   SELECT rnote.* INTO noterec
      FROM tlkpReadingNote rnote 
      LEFT JOIN tlkpVocabulary voc USING (vocabularyid) 
      WHERE voc.name=$1 AND rnote.note=$2;

   IF NOT FOUND THEN
      RAISE EXCEPTION 'Note "%" not found for vocabulary "%"', $2, $1;
   END IF;

   RETURN noterec;
END;
$_$;
 )   DROP FUNCTION cpgdb.getnote(text, text);
       cpgdb          postgres    false    7    232            �            1259    64677 	   tblradius    TABLE     �  CREATE TABLE public.tblradius (
    radiusid uuid DEFAULT public.uuid_generate_v1mc() NOT NULL,
    sampleid uuid NOT NULL,
    code character varying(255) NOT NULL,
    createdtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifiedtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    numberofsapwoodrings integer,
    pithid integer DEFAULT 5 NOT NULL,
    barkpresent boolean,
    lastringunderbark character varying,
    missingheartwoodringstopith integer,
    missingheartwoodringstopithfoundation character varying,
    missingsapwoodringstobark integer,
    missingsapwoodringstobarkfoundation character varying,
    sapwoodid integer DEFAULT 5 NOT NULL,
    heartwoodid integer DEFAULT 5 NOT NULL,
    azimuth double precision,
    comments character varying,
    lastringunderbarkpresent boolean,
    nrofunmeasuredinnerrings integer,
    nrofunmeasuredouterrings integer,
    lrubpa integer,
    domainid integer DEFAULT 0
);
    DROP TABLE public.tblradius;
       public         heap    postgres    false    1211                       0    0    TABLE tblradius    ACL     3   GRANT ALL ON TABLE public.tblradius TO "Webgroup";
          public          postgres    false    239            �           1255    64690 (   getradiiforobjectcode(character varying)    FUNCTION     �  CREATE FUNCTION cpgdb.getradiiforobjectcode(objectcode character varying) RETURNS SETOF public.tblradius
    LANGUAGE plpgsql
    AS $_$DECLARE
  objcode ALIAS FOR $1;
  query VARCHAR;
  rad tblradius%ROWTYPE;
BEGIN
query := 'SELECT tblradius.* 
	FROM tblradius WHERE tblradius.sampleid IN 
	(SELECT sampleid from cpgdb.getsamplesforobjectcode('''||objcode|| '''))';

FOR rad IN EXECUTE query LOOP
   RETURN NEXT rad;
END LOOP;
	  
END;$_$;
 I   DROP FUNCTION cpgdb.getradiiforobjectcode(objectcode character varying);
       cpgdb          postgres    false    7    239            �           1255    64691    getrequestxml(integer)    FUNCTION     �   CREATE FUNCTION cpgdb.getrequestxml(requestid integer) RETURNS character varying
    LANGUAGE plpgsql
    AS $_$DECLARE

xml varchar;

BEGIN

SELECT request into xml from tblrequestlog where requestlogid=$1;
RETURN xml;
END;$_$;
 6   DROP FUNCTION cpgdb.getrequestxml(requestid integer);
       cpgdb          postgres    false    7            �            1259    64692 	   tblsample    TABLE     J  CREATE TABLE public.tblsample (
    sampleid uuid DEFAULT public.uuid_generate_v1mc() NOT NULL,
    code character varying(255) NOT NULL,
    elementid uuid NOT NULL,
    samplingdate date,
    createdtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifiedtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    type character varying(32),
    identifierdomain character varying(256),
    file character varying[],
    "position" character varying,
    state character varying,
    knots boolean DEFAULT false,
    description character varying,
    datecertaintyid integer,
    typeid integer NOT NULL,
    boxid uuid,
    comments character varying,
    externalid character varying,
    domainid integer DEFAULT 0,
    samplestatusid integer,
    samplingyear integer,
    samplingmonth integer,
    samplingdateprec public.date_prec DEFAULT 'day'::public.date_prec NOT NULL,
    userdefinedfielddata character varying[],
    dendrochronologist character varying,
    startyear integer,
    endyear integer,
    haspith boolean,
    hasbark boolean,
    eventyears character varying,
    measuredby character varying,
    CONSTRAINT enforce_samplingyearwithmonth CHECK ((((samplingmonth IS NOT NULL) AND (samplingyear IS NOT NULL)) OR (samplingmonth IS NULL))),
    CONSTRAINT enforce_tblsample_samplemonthbound CHECK (((samplingmonth >= 1) AND (samplingmonth <= 12))),
    CONSTRAINT enforce_tblsample_sampleyearbound CHECK ((samplingyear > 1900)),
    CONSTRAINT enforce_tblsample_sampleyearbound2 CHECK (((samplingyear)::double precision <= date_part('year'::text, now())))
);
    DROP TABLE public.tblsample;
       public         heap    postgres    false    1211    2218    2218                       0    0    COLUMN tblsample.sampleid    COMMENT     M   COMMENT ON COLUMN public.tblsample.sampleid IS 'Unique sample indentifier
';
          public          postgres    false    240                       0    0    COLUMN tblsample.samplingdate    COMMENT     [   COMMENT ON COLUMN public.tblsample.samplingdate IS 'Year of dendrochronological sampling';
          public          postgres    false    240                       0    0 !   COLUMN tblsample.identifierdomain    COMMENT     l   COMMENT ON COLUMN public.tblsample.identifierdomain IS 'Domain from which the identifier-value was issued';
          public          postgres    false    240                       0    0    COLUMN tblsample.file    COMMENT     W   COMMENT ON COLUMN public.tblsample.file IS 'Digital photo or scanned image filenames';
          public          postgres    false    240                       0    0    COLUMN tblsample."position"    COMMENT     R   COMMENT ON COLUMN public.tblsample."position" IS 'Position of sample in element';
          public          postgres    false    240                       0    0    COLUMN tblsample.state    COMMENT     �   COMMENT ON COLUMN public.tblsample.state IS 'State of material (dry / wet / conserved / burned / woodworm/ rot / cracks) things that indicate the quality of the measurements';
          public          postgres    false    240                        0    0    COLUMN tblsample.knots    COMMENT     A   COMMENT ON COLUMN public.tblsample.knots IS 'Presence of knots';
          public          postgres    false    240            !           0    0    COLUMN tblsample.description    COMMENT     W   COMMENT ON COLUMN public.tblsample.description IS 'More information about the sample';
          public          postgres    false    240            "           0    0    TABLE tblsample    ACL     3   GRANT ALL ON TABLE public.tblsample TO "Webgroup";
          public          postgres    false    240            �           1255    64708 *   getsamplesforobjectcode(character varying)    FUNCTION     �  CREATE FUNCTION cpgdb.getsamplesforobjectcode(objectcode character varying) RETURNS SETOF public.tblsample
    LANGUAGE plpgsql
    AS $_$DECLARE
  objcode ALIAS FOR $1;
  query VARCHAR;
  smpl tblsample%ROWTYPE;
BEGIN
query := 'SELECT tblsample.* 
	FROM tblsample WHERE tblsample.elementid IN 
	(SELECT elementid from cpgdb.getelementsforobjectcode('''||objcode|| '''))';

FOR smpl IN EXECUTE query LOOP
   RETURN NEXT smpl;
END LOOP;
	  
END;$_$;
 K   DROP FUNCTION cpgdb.getsamplesforobjectcode(objectcode character varying);
       cpgdb          postgres    false    7    240            �           1255    64709    getsearchresultforid(uuid)    FUNCTION     A  CREATE FUNCTION cpgdb.getsearchresultforid(uuid) RETURNS public.typvmeasurementsearchresult
    LANGUAGE plpgsql
    AS $_$
DECLARE
    _vsid ALIAS FOR $1;
    
    res typVMeasurementSearchResult;
    meta tblVMeasurementMetaCache%ROWTYPE;
BEGIN
    SELECT o.Name, vs.Code, vs.Comments, vs.LastModifiedTimestamp
    INTO res.Op, res.Code, res.Comments, res.Modified
    FROM tblVMeasurement AS vs
      INNER JOIN tlkpVMeasurementOp AS o ON o.VMeasurementOpID = vs.VMeasurementOpID
    WHERE vs.VMeasurementID = _vsid;

    SELECT * INTO meta FROM cpgdb.GetMetaCache(_vsid);
    IF FOUND THEN
       res.StartYear := meta.StartYear;
       res.ReadingCount := meta.ReadingCount;
       res.MeasurementCount := meta.MeasurementCount;
    END IF;

    res.RecursionLevel = 0;
    res.VMeasurementID = _vsid;
    RETURN res;
END;
$_$;
 0   DROP FUNCTION cpgdb.getsearchresultforid(uuid);
       cpgdb          postgres    false    7    2254            �           1255    66913 %   getspecificepithet(character varying)    FUNCTION     �  CREATE FUNCTION cpgdb.getspecificepithet(character varying) RETURNS character varying
    LANGUAGE plpgsql STABLE
    AS $_$
DECLARE                                                           
  thetaxonid ALIAS FOR $1;                                                                                
  taxonrow tlkptaxon;                                            
BEGIN                                                                                                               
  
  SELECT tlkptaxon.* INTO taxonrow FROM tlkptaxon WHERE taxonid=thetaxonid;

  IF taxonrow.taxonrankid=9 THEN  	   
  	   RETURN split_part(taxonrow.label,' ',2 );
  END IF;
  
  RETURN NULL;
                                                                           
END;$_$;
 ;   DROP FUNCTION cpgdb.getspecificepithet(character varying);
       cpgdb          tellervo    false    7            �           1255    64710 &   getusedversionsforconstituents(uuid[])    FUNCTION       CREATE FUNCTION cpgdb.getusedversionsforconstituents(uuid[]) RETURNS SETOF text
    LANGUAGE sql STABLE
    AS $_$
   SELECT DISTINCT version FROM tblVMeasurement
      INNER JOIN tblVMeasurementDerivedCache dc USING (vMeasurementID)
      INNER JOIN tlkpVMeasurementOp op USING (vMeasurementOpID)
      WHERE 
         op.name <> 'Direct' AND
         dc.measurementID IN (
            SELECT DISTINCT measurementID 
               FROM tblVMeasurementDerivedCache 
               WHERE vMeasurementID = ANY($1)
         )
$_$;
 <   DROP FUNCTION cpgdb.getusedversionsforconstituents(uuid[]);
       cpgdb          postgres    false    7            �           1255    64711 4   getuserpermissions(uuid, character varying, integer)    FUNCTION     2  CREATE FUNCTION cpgdb.getuserpermissions(uuid, character varying, integer) RETURNS character varying[]
    LANGUAGE plpgsql
    AS $_$
DECLARE
   _securityUserID ALIAS FOR $1;
   _ptype ALIAS FOR $2;
   _pid ALIAS FOR $3;

   groupMembership int[];
BEGIN
   SELECT * INTO groupMembership FROM cpgdb.GetGroupMembershipArray($1);

   IF 1 = ANY(groupMembership) THEN
      RETURN ARRAY(SELECT name FROM tlkpSecurityPermission WHERE name <> 'No permission');
   END IF;

   RETURN (SELECT * FROM cpgdb.GetGroupPermissions(groupMembership, _ptype, _pid));
END;
$_$;
 J   DROP FUNCTION cpgdb.getuserpermissions(uuid, character varying, integer);
       cpgdb          postgres    false    7            #           0    0 =   FUNCTION getuserpermissions(uuid, character varying, integer)    ACL     `   GRANT ALL ON FUNCTION cpgdb.getuserpermissions(uuid, character varying, integer) TO "Webgroup";
          cpgdb          postgres    false    1257            �           1255    64712 6   getuserpermissionset(uuid, character varying, integer)    FUNCTION     �  CREATE FUNCTION cpgdb.getuserpermissionset(uuid, character varying, integer) RETURNS public.typpermissionset
    LANGUAGE plpgsql
    AS $_$
DECLARE
   _securityUserID ALIAS FOR $1;
   _ptype ALIAS FOR $2;
   _pid ALIAS FOR $3;

   groupMembership int[];
   perms typPermissionSet;
BEGIN
   SELECT * INTO groupMembership FROM cpgdb.GetGroupMembershipArray($1);

   IF 1 = ANY(groupMembership) THEN
      perms.denied = false;
      perms.canRead = true;
      perms.canUpdate = true;
      perms.canCreate = true;
      perms.canDelete = true;
      perms.decidedBy = 'Administrative user';
      RETURN perms;
   ELSIF array_upper(groupMembership, 1) IS NULL THEN
      perms.denied = true;
      perms.canRead = false;
      perms.canUpdate = false;
      perms.canCreate = false;
      perms.canDelete = false;
      perms.decidedBy = 'User is not a member of any groups';
      RETURN perms;      
   END IF;

   perms := cpgdb.GetGroupPermissionSet(groupMembership, _ptype, _pid);
   RETURN perms;
END;
$_$;
 L   DROP FUNCTION cpgdb.getuserpermissionset(uuid, character varying, integer);
       cpgdb          postgres    false    7    2251            �           1255    64713 3   getuserpermissionset(uuid, character varying, uuid)    FUNCTION     �  CREATE FUNCTION cpgdb.getuserpermissionset(uuid, character varying, uuid) RETURNS public.typpermissionset
    LANGUAGE plpgsql
    AS $_$
DECLARE
   _securityUserID ALIAS FOR $1;
   _ptype ALIAS FOR $2;
   _pid ALIAS FOR $3;

   groupMembership int[];
   perms typPermissionSet;
BEGIN

   SELECT * INTO groupMembership FROM cpgdb.GetGroupMembershipArray($1);

   IF 1 = ANY(groupMembership) THEN
      perms.denied = false;
      perms.canRead = true;
      perms.canUpdate = true;
      perms.canCreate = true;
      perms.canDelete = true;
      perms.decidedBy = 'Administrative user';
      RETURN perms;
   ELSIF array_upper(groupMembership, 1) IS NULL THEN
      perms.denied = true;
      perms.canRead = false;
      perms.canUpdate = false;
      perms.canCreate = false;
      perms.canDelete = false;
      perms.decidedBy = 'User is not a member of any groups';
      RETURN perms;      
   END IF;

   perms := cpgdb.GetGroupPermissionSet(groupMembership, _ptype, _pid);
   RETURN perms;
END;
$_$;
 I   DROP FUNCTION cpgdb.getuserpermissionset(uuid, character varying, uuid);
       cpgdb          postgres    false    2251    7            �           1255    64714    getvmeasurementlabel(uuid)    FUNCTION     �   CREATE FUNCTION cpgdb.getvmeasurementlabel(uuid) RETURNS text
    LANGUAGE sql STABLE
    AS $_$
   SELECT cpgdb.GetLabel('vmeasurement', $1, false);
$_$;
 0   DROP FUNCTION cpgdb.getvmeasurementlabel(uuid);
       cpgdb          postgres    false    7            �           1255    64715    getvmeasurementprefix(uuid)    FUNCTION     �   CREATE FUNCTION cpgdb.getvmeasurementprefix(uuid) RETURNS text
    LANGUAGE sql STABLE
    AS $_$
   SELECT cpgdb.GetLabel('vmeasurement', $1, true);
$_$;
 1   DROP FUNCTION cpgdb.getvmeasurementprefix(uuid);
       cpgdb          postgres    false    7            �            1259    64716    tblvmeasurementreadingresult    TABLE     X  CREATE TABLE public.tblvmeasurementreadingresult (
    vmeasurementreadingresultid integer NOT NULL,
    vmeasurementresultid uuid NOT NULL,
    relyear integer NOT NULL,
    reading integer NOT NULL,
    wjinc integer,
    wjdec integer,
    count integer DEFAULT 1 NOT NULL,
    readingid integer,
    ewwidth integer,
    lwwidth integer
);
 0   DROP TABLE public.tblvmeasurementreadingresult;
       public         heap    postgres    false            $           0    0 "   TABLE tblvmeasurementreadingresult    ACL     F   GRANT ALL ON TABLE public.tblvmeasurementreadingresult TO "Webgroup";
          public          postgres    false    241            �           1255    66946 "   getvmeasurementreadingresult(uuid)    FUNCTION     �   CREATE FUNCTION cpgdb.getvmeasurementreadingresult(uuid) RETURNS SETOF public.tblvmeasurementreadingresult
    LANGUAGE sql STABLE
    AS $_$SELECT * from tblVMeasurementReadingResult WHERE VMeasurementResultID = $1 ORDER BY relyear$_$;
 8   DROP FUNCTION cpgdb.getvmeasurementreadingresult(uuid);
       cpgdb          tellervo    false    241    7            �           1255    64720 /   getvmeasurementreadingresult(character varying)    FUNCTION     �   CREATE FUNCTION cpgdb.getvmeasurementreadingresult(character varying) RETURNS SETOF public.tblvmeasurementreadingresult
    LANGUAGE sql STABLE
    AS $_$SELECT * from tblVMeasurementReadingResult WHERE VMeasurementResultID = $1 ORDER BY relyear$_$;
 E   DROP FUNCTION cpgdb.getvmeasurementreadingresult(character varying);
       cpgdb          postgres    false    7    241            %           0    0 8   FUNCTION getvmeasurementreadingresult(character varying)    ACL     [   GRANT ALL ON FUNCTION cpgdb.getvmeasurementreadingresult(character varying) TO "Webgroup";
          cpgdb          postgres    false    1262            >           1259    65524    tblvmeasurementresult    TABLE     �  CREATE TABLE public.tblvmeasurementresult (
    vmeasurementresultid uuid NOT NULL,
    vmeasurementid uuid NOT NULL,
    radiusid uuid,
    isreconciled boolean,
    startyear integer NOT NULL,
    islegacycleaned boolean,
    createdtimestamp timestamp with time zone NOT NULL,
    lastmodifiedtimestamp timestamp with time zone NOT NULL,
    vmeasurementresultmasterid uuid NOT NULL,
    owneruserid integer NOT NULL,
    vmeasurementresultgroupid uuid,
    datingtypeid integer DEFAULT 1 NOT NULL,
    datingerrorpositive integer DEFAULT 0 NOT NULL,
    datingerrornegative integer DEFAULT 0 NOT NULL,
    code character varying(255) NOT NULL,
    comments character varying(1000),
    ispublished boolean DEFAULT false NOT NULL
);
 )   DROP TABLE public.tblvmeasurementresult;
       public         heap    postgres    false            &           0    0    TABLE tblvmeasurementresult    ACL     ?   GRANT ALL ON TABLE public.tblvmeasurementresult TO "Webgroup";
          public          postgres    false    318            �           1255    66945    getvmeasurementresult(uuid)    FUNCTION     �   CREATE FUNCTION cpgdb.getvmeasurementresult(uuid) RETURNS SETOF public.tblvmeasurementresult
    LANGUAGE javau
    AS $$org.tellervo.cpgdb.VMeasurementResultSet.getVMeasurementResultSet$$;
 1   DROP FUNCTION cpgdb.getvmeasurementresult(uuid);
       cpgdb          tellervo    false    7    318    2    2    9    2    9            �           1255    64721     getvmeasurementsummaryinfo(uuid)    FUNCTION     �  CREATE FUNCTION cpgdb.getvmeasurementsummaryinfo(uuid) RETURNS public.typvmeasurementsummaryinfo
    LANGUAGE plpgsql STABLE
    AS $_$
DECLARE
   VMID ALIAS FOR $1;
   numrows integer;
   taxondepth integer;

   curtf text;
   prevtf text;

   rec record;
   curtaxa typfulltaxonomy;
   prevtaxa typfulltaxonomy;

   ret typVMeasurementSummaryInfo;   
BEGIN
   ret.VMeasurementID := VMID;
   numrows := 0;

   FOR rec IN SELECT s.code 
	FROM tblVMeasurementDerivedCache d
	INNER JOIN tblMeasurement m ON m.MeasurementID = d.MeasurementID
	INNER JOIN tblRadius r on r.radiusID = m.radiusID
	INNER JOIN tblSample sp on sp.sampleID = r.sampleID
	INNER JOIN tblElement t on t.elementID = sp.elementID
	INNER JOIN tblObject s on s.objectID = t.objectID
	WHERE d.VMeasurementID = VMID
	GROUP BY(s.code)   
   LOOP
      IF numrows = 0 THEN
         ret.objectCode = rec.code;
      ELSE
         ret.objectCode = ret.objectCode || ';' || rec.code;
      END IF;
      numrows := numrows + 1;
   END LOOP;
   ret.objectCount = numrows;   

   numrows := 0;
   FOR rec IN SELECT txbasic.*, cpgdb.qryTaxonomy(txbasic.taxonID) as tx FROM
 	(SELECT t.taxonID::varchar FROM tblVMeasurementDerivedCache d
	INNER JOIN tblMeasurement m ON m.MeasurementID = d.MeasurementID
	INNER JOIN tblRadius r on r.radiusID = m.radiusID
	INNER JOIN tblSample sp on sp.sampleID = r.sampleID
	INNER JOIN tblElement t on t.elementID = sp.elementID
	INNER JOIN tlkpTaxon tx on tx.taxonID = t.taxonID
	WHERE d.VMeasurementID = VMID
	GROUP BY(t.taxonID)) as txbasic
   LOOP
      curtaxa := rec.tx;

      IF numrows = 0 THEN
         taxondepth := 15;
         LOOP
            curtf := cpgdb._getTaxonForDepth(taxondepth, curtaxa);
            EXIT WHEN curtf IS NOT NULL;
            taxondepth := taxondepth - 1;
         END LOOP;
      ELSE
         LOOP
            curtf := cpgdb._getTaxonForDepth(taxondepth, curtaxa);
            prevtf := cpgdb._getTaxonForDepth(taxondepth, prevtaxa);            
            EXIT WHEN curtf = prevtf OR taxondepth = 1;
            taxondepth := taxondepth - 1;
         END LOOP;
      END IF;

      numrows := numrows + 1;
      prevtaxa := curtaxa;
   END LOOP;

   ret.taxonCount := numrows;
   ret.commonTaxonName := curtf;
   
   RETURN ret;
END;
$_$;
 6   DROP FUNCTION cpgdb.getvmeasurementsummaryinfo(uuid);
       cpgdb          postgres    false    7    2257            �           1255    64722 1   infomeasuredsamplecountforsite(character varying)    FUNCTION     �  CREATE FUNCTION cpgdb.infomeasuredsamplecountforsite(sitecode character varying) RETURNS bigint
    LANGUAGE sql
    AS $_$select count(distinct(s.sampleid)) from tblmeasurement m, tblradius r, tblsample s where m.radiusid=r.radiusid and r.sampleid=s.sampleid and s.sampleid in  (select s.sampleid from tblelement e, tblsample s where e.elementid=s.elementid and e.objectid in (select objectid from cpgdb.FindObjectDescendants((select objectid from tblobject where code=$1), true)));$_$;
 P   DROP FUNCTION cpgdb.infomeasuredsamplecountforsite(sitecode character varying);
       cpgdb          postgres    false    7            '           0    0 C   FUNCTION infomeasuredsamplecountforsite(sitecode character varying)    COMMENT     �   COMMENT ON FUNCTION cpgdb.infomeasuredsamplecountforsite(sitecode character varying) IS 'Calculates the total number of samples from this site that have already been measured.  Handles traversal of object hierarchy.';
          cpgdb          postgres    false    1264            �           1255    64723 '   inforingcountforsite(character varying)    FUNCTION     �  CREATE FUNCTION cpgdb.inforingcountforsite(sitecode character varying) RETURNS bigint
    LANGUAGE sql
    AS $_$select count(rd.readingid) from tblreading rd, tblmeasurement m where rd.measurementid=m.measurementid and m.radiusid in ( select r.radiusid from tblradius r where r.sampleid in  (select s.sampleid from tblelement e, tblsample s where e.elementid=s.elementid and e.objectid in (select objectid from cpgdb.FindObjectDescendants((select objectid from tblobject where code=$1), true))));$_$;
 F   DROP FUNCTION cpgdb.inforingcountforsite(sitecode character varying);
       cpgdb          postgres    false    7            (           0    0 9   FUNCTION inforingcountforsite(sitecode character varying)    COMMENT     �   COMMENT ON FUNCTION cpgdb.inforingcountforsite(sitecode character varying) IS 'Calculate the number of rings that have been measured for a site';
          cpgdb          postgres    false    1265            �           1255    64724 "   inforingsperuserbydate(date, date)    FUNCTION     �  CREATE FUNCTION cpgdb.inforingsperuserbydate(startdate date, enddate date) RETURNS SETOF public.typnamenumber
    LANGUAGE sql
    AS $_$SELECT (su.lastname::text || ', '::text) || su.firstname::text AS name, count(r.*) 
   FROM tblvmeasurement vm
   LEFT JOIN tblsecurityuser su ON vm.owneruserid = su.securityuserid
   LEFT JOIN tblmeasurement m ON vm.measurementid = m.measurementid
   LEFT JOIN tblreading r ON m.measurementid = r.measurementid
  WHERE vm.vmeasurementopid = 5 and vm.createdtimestamp>=$1 and vm.createdtimestamp<=$2
  GROUP BY (su.lastname::text || ', '::text) || su.firstname::text, su.securityuserid
  ORDER BY count(r.*) DESC;
$_$;
 J   DROP FUNCTION cpgdb.inforingsperuserbydate(startdate date, enddate date);
       cpgdb          postgres    false    2248    7            )           0    0 =   FUNCTION inforingsperuserbydate(startdate date, enddate date)    COMMENT     �   COMMENT ON FUNCTION cpgdb.inforingsperuserbydate(startdate date, enddate date) IS 'Scoreboard of users showing number of rings measured between two dates';
          cpgdb          postgres    false    1266            �           1255    64725 )   infosamplecountforsite(character varying)    FUNCTION     B  CREATE FUNCTION cpgdb.infosamplecountforsite(sitecode character varying) RETURNS bigint
    LANGUAGE sql
    AS $_$select count(s.*) from tblelement e, tblsample s where e.elementid=s.elementid and e.objectid in (select objectid from cpgdb.FindObjectDescendants((select objectid from tblobject where code=$1), true));$_$;
 H   DROP FUNCTION cpgdb.infosamplecountforsite(sitecode character varying);
       cpgdb          postgres    false    7            *           0    0 ;   FUNCTION infosamplecountforsite(sitecode character varying)    COMMENT     �   COMMENT ON FUNCTION cpgdb.infosamplecountforsite(sitecode character varying) IS 'Count of samples in the database for a site.  Note that some of these samples may not have associated series etc';
          cpgdb          postgres    false    1267            �           1255    64726 )   infoseriescountforsite(character varying)    FUNCTION     �  CREATE FUNCTION cpgdb.infoseriescountforsite(sitecode character varying) RETURNS bigint
    LANGUAGE sql
    AS $_$select count(m.measurementid) from tblmeasurement m where m.radiusid in ( select r.radiusid from tblradius r where r.sampleid in  (select s.sampleid from tblelement e, tblsample s where e.elementid=s.elementid and e.objectid in (select objectid from cpgdb.FindObjectDescendants((select objectid from tblobject where code=$1), true))));$_$;
 H   DROP FUNCTION cpgdb.infoseriescountforsite(sitecode character varying);
       cpgdb          postgres    false    7            +           0    0 ;   FUNCTION infoseriescountforsite(sitecode character varying)    COMMENT     �   COMMENT ON FUNCTION cpgdb.infoseriescountforsite(sitecode character varying) IS 'Calculated the the number of series that have been measured for a site.';
          cpgdb          postgres    false    1268            �           1255    64727 #   infoseriesperuserbydate(date, date)    FUNCTION     ?  CREATE FUNCTION cpgdb.infoseriesperuserbydate(startdate date, enddate date) RETURNS SETOF public.typnamenumber
    LANGUAGE sql
    AS $_$SELECT (su.lastname::text || ', '::text) || su.firstname::text AS name, count(m.*) 
   FROM tblvmeasurement vm
   LEFT JOIN tblsecurityuser su ON vm.owneruserid = su.securityuserid
   LEFT JOIN tblmeasurement m ON vm.measurementid = m.measurementid
  WHERE vm.vmeasurementopid = 5 and vm.createdtimestamp>=$1 and vm.createdtimestamp<=$2
  GROUP BY (su.lastname::text || ', '::text) || su.firstname::text
  ORDER BY count(m.*) DESC;
$_$;
 K   DROP FUNCTION cpgdb.infoseriesperuserbydate(startdate date, enddate date);
       cpgdb          postgres    false    2248    7            ,           0    0 >   FUNCTION infoseriesperuserbydate(startdate date, enddate date)    COMMENT     �   COMMENT ON FUNCTION cpgdb.infoseriesperuserbydate(startdate date, enddate date) IS 'Scoreboard of users showing number of series measured by each between two dates';
          cpgdb          postgres    false    1269            �           1255    64728 +   infosumringlengthforsite(character varying)    FUNCTION     �  CREATE FUNCTION cpgdb.infosumringlengthforsite(sitecode character varying) RETURNS bigint
    LANGUAGE sql
    AS $_$select sum(rd.reading) from tblreading rd, tblmeasurement m where rd.measurementid=m.measurementid and m.radiusid in ( select r.radiusid from tblradius r where r.sampleid in  (select s.sampleid from tblelement e, tblsample s where e.elementid=s.elementid and e.objectid in (select objectid from cpgdb.FindObjectDescendants((select objectid from tblobject where code=$1), true))));$_$;
 J   DROP FUNCTION cpgdb.infosumringlengthforsite(sitecode character varying);
       cpgdb          postgres    false    7            -           0    0 =   FUNCTION infosumringlengthforsite(sitecode character varying)    COMMENT     �   COMMENT ON FUNCTION cpgdb.infosumringlengthforsite(sitecode character varying) IS 'Calculate the total length of rings that have been measured for a site.  Result is returned in microns.';
          cpgdb          postgres    false    1270            �           1255    64729 )   infosumringwidthforuserbydate(date, date)    FUNCTION     �  CREATE FUNCTION cpgdb.infosumringwidthforuserbydate(startdate date, enddate date) RETURNS SETOF public.typnamenumber
    LANGUAGE sql
    AS $_$SELECT (su.lastname::text || ', '::text) || su.firstname::text AS name, sum(r.reading) AS totalringwidth
 FROM tblvmeasurement vm
 LEFT JOIN tblsecurityuser su ON vm.owneruserid = su.securityuserid
 LEFT JOIN tblmeasurement m ON vm.measurementid = m.measurementid
 LEFT JOIN tblreading r ON m.measurementid = r.measurementid
  WHERE vm.vmeasurementopid = 5 and vm.createdtimestamp>=$1 and vm.createdtimestamp<=$2
GROUP BY (su.lastname::text || ', '::text) || su.firstname::text, su.securityuserid
ORDER BY sum(r.reading) DESC;$_$;
 Q   DROP FUNCTION cpgdb.infosumringwidthforuserbydate(startdate date, enddate date);
       cpgdb          postgres    false    7    2248            .           0    0 D   FUNCTION infosumringwidthforuserbydate(startdate date, enddate date)    COMMENT     �   COMMENT ON FUNCTION cpgdb.infosumringwidthforuserbydate(startdate date, enddate date) IS 'Scoreboard of users showing total ring lengths measured between two dates';
          cpgdb          postgres    false    1271            �           1255    64730    isadmin(integer)    FUNCTION     �   CREATE FUNCTION cpgdb.isadmin(securityuserid integer) RETURNS boolean
    LANGUAGE sql
    AS $_$select 
case when securitygroupsbyuser=1 
then true 
else null 
end 
as isadmin 
from securitygroupsbyuser($1) 
where securitygroupsbyuser=1;$_$;
 5   DROP FUNCTION cpgdb.isadmin(securityuserid integer);
       cpgdb          postgres    false    7            /           0    0 (   FUNCTION isadmin(securityuserid integer)    ACL     K   GRANT ALL ON FUNCTION cpgdb.isadmin(securityuserid integer) TO "Webgroup";
          cpgdb          postgres    false    1272            �           1255    64731    isadmin(uuid)    FUNCTION     �   CREATE FUNCTION cpgdb.isadmin(securityuserid uuid) RETURNS boolean
    LANGUAGE sql
    AS $_$select 
case when securitygroupsbyuser=1 
then true 
else null 
end 
as isadmin 
from securitygroupsbyuser($1) 
where securitygroupsbyuser=1;$_$;
 2   DROP FUNCTION cpgdb.isadmin(securityuserid uuid);
       cpgdb          tellervo    false    7            �           1255    64732    loanhasoutstandingitems(uuid)    FUNCTION     Q  CREATE FUNCTION cpgdb.loanhasoutstandingitems(theloanid uuid) RETURNS boolean
    LANGUAGE plpgsql
    AS $_$DECLARE
  totalboxcount integer;
  returnedboxcount integer;
 

BEGIN

SELECT count(boxid) INTO totalboxcount FROM tblcurationevent WHERE loanid=$1 AND curationstatusid=2;
SELECT count(boxid) INTO returnedboxcount FROM tblcurationevent WHERE loanid=$1 AND curationstatusid=10;

RAISE NOTICE 'Total box count = %', totalboxcount;
RAISE NOTICE 'Returned box count = %', returnedboxcount;

IF totalboxcount <= returnedboxcount THEN
	RETURN FALSE;
ELSE 
	RETURN TRUE;
END IF;

END;

$_$;
 =   DROP FUNCTION cpgdb.loanhasoutstandingitems(theloanid uuid);
       cpgdb          tellervo    false    7            �           1255    64733    lookupenvdata(integer, integer)    FUNCTION     r  CREATE FUNCTION cpgdb.lookupenvdata(theelementid integer, therasterlayerid integer) RETURNS double precision
    LANGUAGE plpgsql
    AS $$DECLARE
reftree CURSOR FOR SELECT X(location) AS longitude, Y(location) AS latitude FROM tblelement where elementid=theelementid;
reflayers CURSOR FOR SELECT filename FROM tblrasterlayer WHERE rasterlayerid=therasterlayerid;
refvalue CURSOR (myfilename varchar, mylat double precision, mylong double precision) FOR SELECT rasteratpoint from cpgdb.rasteratpoint(myfilename, mylat, mylong);
myfilename varchar;
mylat double precision;
mylong double precision;
myvalue double precision;
BEGIN

    OPEN reftree;
    FETCH reftree INTO mylong, mylat;
    CLOSE reftree;

    OPEN reflayers;
    FETCH reflayers INTO myfilename;
    CLOSE reflayers;

    OPEN refvalue(myfilename, mylat, mylong);
    FETCH refvalue INTO myvalue;
    CLOSE refvalue;

    DELETE FROM tblenvironmentaldata where rasterlayerid=therasterlayerid and elementid=theelementid;

    INSERT INTO tblenvironmentaldata(rasterlayerid, elementid, value) VALUES (therasterlayerid, theelementid, myvalue);

    RETURN myvalue;

END;$$;
 S   DROP FUNCTION cpgdb.lookupenvdata(theelementid integer, therasterlayerid integer);
       cpgdb          postgres    false    7            0           0    0 F   FUNCTION lookupenvdata(theelementid integer, therasterlayerid integer)    ACL     i   GRANT ALL ON FUNCTION cpgdb.lookupenvdata(theelementid integer, therasterlayerid integer) TO "Webgroup";
          cpgdb          postgres    false    1275            �           1255    64734    lookupenvdatabylayer(integer)    FUNCTION       CREATE FUNCTION cpgdb.lookupenvdatabylayer(therasterlayerid integer) RETURNS void
    LANGUAGE plpgsql
    AS $$DECLARE
reftrees CURSOR FOR SELECT elementid FROM tblelement WHERE location IS NOT NULL;
myrasterlayerid integer;
myelementid integer;
mybin double precision;
BEGIN

    OPEN reftrees;
    FETCH reftrees INTO myelementid;
    WHILE FOUND LOOP

        SELECT * FROM cpgdb.lookupenvdata(myelementid, therasterlayerid) INTO mybin;

        FETCH reftrees INTO myelementid;
    END LOOP;
    CLOSE reftrees;

    RETURN;

END;$$;
 D   DROP FUNCTION cpgdb.lookupenvdatabylayer(therasterlayerid integer);
       cpgdb          postgres    false    7            1           0    0 7   FUNCTION lookupenvdatabylayer(therasterlayerid integer)    ACL     Z   GRANT ALL ON FUNCTION cpgdb.lookupenvdatabylayer(therasterlayerid integer) TO "Webgroup";
          cpgdb          postgres    false    1276            �           1255    64735    lookupenvdatabytree(integer)    FUNCTION       CREATE FUNCTION cpgdb.lookupenvdatabytree(theelementid integer) RETURNS void
    LANGUAGE plpgsql
    AS $$DECLARE
reflayers2 CURSOR FOR SELECT rasterlayerid FROM tblrasterlayer WHERE issystemlayer=true;
myrasterlayerid integer;
mybin double precision;
BEGIN

    OPEN reflayers2;
    FETCH reflayers2 INTO myrasterlayerid;
    WHILE FOUND LOOP

        SELECT * FROM cpgdb.lookupenvdata(theelementid, myrasterlayerid) INTO mybin;

        FETCH reflayers2 INTO myrasterlayerid;
     END LOOP;
     CLOSE reflayers2;

     RETURN;

END;$$;
 ?   DROP FUNCTION cpgdb.lookupenvdatabytree(theelementid integer);
       cpgdb          postgres    false    7            2           0    0 2   FUNCTION lookupenvdatabytree(theelementid integer)    ACL     U   GRANT ALL ON FUNCTION cpgdb.lookupenvdatabytree(theelementid integer) TO "Webgroup";
          cpgdb          postgres    false    1277            �           1255    64736    mergeelements(uuid, uuid)    FUNCTION     �.  CREATE FUNCTION cpgdb.mergeelements(goodelementid uuid, badelementid uuid) RETURNS public.tblelement
    LANGUAGE plpgsql
    AS $_$DECLARE

goodelementid ALIAS FOR $1;      -- UUID of the element with correct entities
goodelement tblelement%ROWTYPE;  -- The series itself
badelementid ALIAS FOR $2;       -- UUID of the element with incorrect entities
badelement tblelement%ROWTYPE;   -- The series itself
commentstr varchar;              -- Used to compile comments for indicating discrepancies 
discrepstr varchar;              -- Basic comment to highlight discrepancies
discrepancycount integer;        -- Count of number of fields that aren't the same

lookup varchar;                  -- Temporary variable for storing values from lookup tables
badsamp RECORD;                  -- Temporary variable for storing associated samples
goodsampleid uuid;               -- Temporary variable for storing uuid of good associated sample
doupdate boolean;                -- Temporary variable for flagging whether to update this record
BEGIN

discrepancycount:=0;
commentstr:= '';
discrepstr:='*** Another record was automatically merged with this one and discrepancies were detected.  Please check the following issues: ';
doupdate:=false;

SELECT * INTO goodelement FROM tblelement WHERE elementid=goodelementid;
IF NOT FOUND THEN
   RAISE NOTICE 'The good element with id % was not found', goodelementid;
   RETURN NULL;
END IF;
SELECT * INTO badelement FROM tblelement WHERE elementid=badelementid;
IF NOT FOUND THEN
   RAISE NOTICE 'The bad element with id % was not found', badelementid;
   RETURN NULL;
END IF;

IF goodelement.elementid=badelement.elementid THEN 
   RAISE NOTICE 'Elements are the same!';
   RETURN goodelement;
END IF;

FOR badsamp IN SELECT * FROM tblsample WHERE elementid=badelementid ORDER BY code LOOP 
   SELECT sampleid INTO goodsampleid FROM tblsample WHERE elementid=goodelementid AND code=badsamp.code;
   IF goodsampleid IS NOT NULL THEN
       PERFORM cpgdb.mergesamples(goodsampleid, badsamp.sampleid);
   ELSE
       UPDATE tblsample SET elementid=goodelementid WHERE sampleid=badsamp.sampleid;
   END IF;
END LOOP;


IF goodelement.taxonid IS NULL THEN
   goodelement.taxonid=badelement.taxonid;
   doupdate:=true;
END IF;
IF goodelement.taxonid!=badelement.taxonid THEN
   SELECT label INTO lookup FROM tlkptaxon WHERE taxonid=badelement.taxonid;
   commentstr:= commentstr || 'Taxon differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodelement.locationprecision IS NULL THEN 
   goodelement.locationprecision:=badelement.locationprecision;
   doupdate:=true;
END IF;
IF goodelement.locationprecision!=badelement.locationprecision THEN
   commentstr:= commentstr || 'Location precision differs (other record = ' || badelement.locationprecision::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodelement.locationgeometry IS NULL THEN 
   goodelement.locationgeometry:=badelement.locationgeometry;
   doupdate:=true;
END IF;
IF goodelement.locationgeometry!=badelement.locationgeometry THEN
   SELECT 'Long: ' || x(locationgeometry) || ', Lat: ' ||  y(locationgeometry) INTO lookup FROM tblelement WHERE elementid=badelement.elementid;   
   commentstr:= commentstr || 'Location geometry differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodelement.locationtypeid IS NULL THEN 
   goodelement.locationtypeid:=badelement.locationtypeid;
   doupdate:=true;
END IF;
IF goodelement.locationtypeid!=badelement.locationtypeid THEN
   SELECT locationtype INTO lookup FROM tlkplocationtype WHERE locationtypeid=badelement.locationtypeid;
   commentstr:= commentstr || 'Location type differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodelement.locationcomment IS NULL THEN 
   goodelement.locationcomment:=badelement.locationcomment;
   doupdate:=true;
END IF;
IF goodelement.locationcomment!=badelement.locationcomment THEN
   commentstr:= commentstr || 'Location comments differ (other record = ' || badelement.locationcomment::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodelement.file IS NULL THEN 
   goodelement.file:=badelement.file;
   doupdate:=true;
END IF;
IF goodelement.file!=badelement.file THEN
   commentstr:= commentstr || 'File links differ (other record = ' || badelement.file::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodelement.description IS NULL THEN 
   goodelement.description:=badelement.description;
   doupdate:=true;
END IF;
IF goodelement.description!=badelement.description THEN
   commentstr:= commentstr || 'Description differs (other record = ' || badelement.file::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;
 
IF goodelement.processing IS NULL THEN 
   goodelement.processing:=badelement.processing;
   doupdate:=true;
END IF;
IF goodelement.processing!=badelement.processing THEN
   commentstr:= commentstr || 'Processing field differs (other record = ' || badelement.processing::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodelement.marks IS NULL THEN 
   goodelement.marks:=badelement.marks;
   doupdate:=true;
END IF;
IF goodelement.marks!=badelement.marks THEN
   commentstr:= commentstr || 'Marks field differs (other record = ' || badelement.marks::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodelement.diameter IS NULL THEN 
   goodelement.diameter:=badelement.diameter;
   doupdate:=true;
END IF;
IF goodelement.diameter!=badelement.diameter THEN
   commentstr:= commentstr || 'Diameter differs (other record = ' || badelement.diameter::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodelement.width IS NULL THEN
   goodelement.width:=badelement.width;
   doupdate:=true;
END IF;
IF goodelement.width!=badelement.width THEN
   commentstr:= commentstr || 'Width differs (other record = ' || badelement.width::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodelement.height IS NULL THEN 
   goodelement.height:=badelement.height;
   doupdate:=true;
END IF;
IF goodelement.height!=badelement.height THEN
   commentstr:= commentstr || 'Height differs (other record = ' || badelement.height::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodelement.depth IS NULL THEN 
   goodelement.depth:=badelement.depth;
   doupdate:=true;
END IF;
IF goodelement.depth!=badelement.depth THEN
   commentstr:= commentstr || 'Depth differs (other record = ' || badelement.depth::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodelement.units IS NULL THEN 
   goodelement.units:=badelement.units;
   doupdate:=true;
END IF;
IF goodelement.units!=badelement.units THEN
   commentstr:= commentstr || 'Units differ (other record = ' || badelement.units::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodelement.elementtypeid IS NULL THEN 
   goodelement.elementtypeid:=badelement.elementtypeid;
   doupdate:=true;
END IF;
IF goodelement.elementtypeid!=badelement.elementtypeid THEN
   SELECT elementtype INTO lookup FROM tlkpelementtype WHERE elementtypeid=badelement.elementtypeid;
   commentstr:= commentstr || 'Element type differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodelement.elementauthenticityid IS NULL THEN 
   goodelement.elementauthenticityid:=badelement.elementauthenticityid;
   doupdate:=true;
END IF;
IF goodelement.elementauthenticityid!=badelement.elementauthenticityid THEN
   SELECT elementauthenticity INTO lookup FROM tlkpelementauthenticity WHERE elementauthenticityid=badelement.elementauthenticityid;
   commentstr:= commentstr || 'Element authenticity differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodelement.elementshapeid IS NULL THEN 
   goodelement.elementshapeid:=badelement.elementshapeid;
   doupdate:=true;
END IF;
IF goodelement.elementshapeid!=badelement.elementshapeid THEN
   SELECT elementshape INTO lookup FROM tlkpelementshape WHERE elementshapeid=badelement.elementshapeid;
   commentstr:= commentstr || 'Element shape differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodelement.altitude IS NULL THEN 
   goodelement.altitude:=badelement.altitude;
   doupdate:=true;
END IF;
IF goodelement.altitude!=badelement.altitude THEN
   commentstr:= commentstr || 'Altitude differs (other record = ' || badelement.altitude::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodelement.slopeangle IS NULL THEN 
   goodelement.slopeangle:=badelement.slopeangle;
   doupdate:=true;
END IF;
IF goodelement.slopeangle!=badelement.slopeangle THEN
   commentstr:= commentstr || 'Slope angle differs (other record = ' || badelement.slopeangle::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodelement.slopeazimuth IS NULL THEN 
   goodelement.slopeazimuth:=badelement.slopeazimuth;
   doupdate:=true;
END IF;
IF goodelement.slopeazimuth!=badelement.slopeazimuth THEN
   commentstr:= commentstr || 'Slope azimuth differs (other record = ' || badelement.slopeazimuth::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodelement.soildescription IS NULL THEN 
   goodelement.soildescription:=badelement.soildescription;
   doupdate:=true;
END IF;
IF goodelement.soildescription!=badelement.soildescription THEN
   commentstr:= commentstr || 'Soil description differs (other record = ' || badelement.soildescription::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodelement.soildepth IS NULL THEN 
   goodelement.soildepth:=badelement.soildepth;
   doupdate:=true;
END IF;
IF goodelement.soildepth!=badelement.soildepth THEN
   commentstr:= commentstr || 'Soil depth differs (other record = ' || badelement.soildepth::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodelement.bedrockdescription IS NULL THEN 
   goodelement.bedrockdescription:=badelement.bedrockdescription;
   doupdate:=true;
END IF;
IF goodelement.bedrockdescription!=badelement.bedrockdescription THEN
   commentstr:= commentstr || 'Bedrock description differs (other record = ' || badelement.bedrockdescription::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;


IF (discrepancycount>0 OR doupdate=true) THEN
  IF (discrepancycount>0 AND goodelement.comments IS NOT NULL) THEN
    commentstr := goodelement.comments || discrepstr || commentstr || '***';
  ELSIF (discrepancycount>0) THEN
    commentstr := discrepstr || commentstr || '***';
  ELSE
    commentstr := goodelement.comments;
  END IF;
  
    UPDATE tblelement SET 
    taxonid=goodelement.taxonid,
    locationprecision=goodelement.locationprecision,
    code=goodelement.code,
    locationgeometry=goodelement.locationgeometry,
    locationtypeid=goodelement.locationtypeid,
    locationcomment=goodelement.locationcomment,
    file=goodelement.file,
    description=goodelement.description,
    processing=goodelement.processing,
    marks=goodelement.marks,
    diameter=goodelement.diameter,
    width=goodelement.width,
    height=goodelement.height,
    depth=goodelement.depth,
    units=goodelement.units,
    elementtypeid=goodelement.elementtypeid,
    elementauthenticityid=goodelement.elementauthenticityid,
    elementshapeid=goodelement.elementshapeid,
    altitude=goodelement.altitude,
    slopeangle=goodelement.slopeangle,
    slopeazimuth=goodelement.slopeazimuth,
    soildescription=goodelement.soildescription,
    soildepth=goodelement.soildepth,
    bedrockdescription=goodelement.bedrockdescription,
    comments=commentstr
    WHERE elementid=goodelementid;

END IF;

DELETE FROM tblelement WHERE elementid=badelementid;

SELECT * INTO goodelement FROM tblelement WHERE elementid=goodelementid;
RETURN goodelement;
END;$_$;
 J   DROP FUNCTION cpgdb.mergeelements(goodelementid uuid, badelementid uuid);
       cpgdb          postgres    false    7    236            �           1255    64738    mergeobjects(uuid, uuid)    FUNCTION     V  CREATE FUNCTION cpgdb.mergeobjects(goodobject uuid, badobject uuid) RETURNS public.tblobject
    LANGUAGE plpgsql
    AS $_$DECLARE

goodobjectid ALIAS FOR $1;      -- UUID of the object with correct entities
goodobject tblobject%ROWTYPE;   -- The object itself
badobjectid ALIAS FOR $2;       -- UUID of the object with incorrect entities
badobject tblobject%ROWTYPE;    -- The object itself
commentstr varchar;             -- Used to compile comments for indicating discrepancies 
discrepstr varchar;             -- Basic comment to highlight discrepancies
discrepancycount integer;       -- Count of number of fields that aren't the same

lookup varchar;                 -- Temporary variable for storing values from lookup tables
badchild RECORD;                -- Temporary variable for storing associated subobjects/elements
goodchildid uuid;               -- Temporary variable for storing uuid of good associated subobjects/elements
doupdate boolean;               -- Temporary variable for flagging whether to update this record

BEGIN

discrepancycount:=0;
commentstr:= '';
discrepstr:='*** Another record was automatically merged with this one and discrepancies were detected.  Please check the following issues: ';
doupdate:=false;


SELECT * INTO goodobject FROM tblobject WHERE objectid=goodobjectid;
IF NOT FOUND THEN
   RAISE NOTICE 'The good object with id % was not found', goodobjectid;
   RETURN NULL;
END IF;
SELECT * INTO badobject FROM tblobject WHERE objectid=badobjectid;
IF NOT FOUND THEN
   RAISE NOTICE 'The bad object with id % was not found', badobjectid;
   RETURN NULL;
END IF;

IF goodobject.objectid=badobject.objectid THEN 
   RAISE NOTICE 'Objects are the same!';
   RETURN goodobject;
END IF;

FOR badchild IN SELECT * FROM tblobject WHERE parentobjectid=badobjectid ORDER BY code LOOP 
   SELECT objectid INTO goodchildid FROM tblobject WHERE objectid=goodobjectid AND code=badchild.code;
   IF goodchildid IS NOT NULL THEN
       PERFORM cpgdb.mergeobjects(goodchildid, badchild.objectid);
   ELSE
       UPDATE tblobject SET parentobjectid=goodobjectid WHERE objectid=badchild.objectid;
   END IF;
END LOOP;

FOR badchild IN SELECT * FROM tblelement WHERE objectid=badobjectid ORDER BY code LOOP 
   SELECT elementid INTO goodchildid FROM tblelement WHERE objectid=goodobjectid AND code=badchild.code;
   IF goodchildid IS NOT NULL THEN
       PERFORM cpgdb.mergeelements(goodchildid, badchild.elementid);
   ELSE
       UPDATE tblelement SET objectid=goodobjectid WHERE elementid=badchild.elementid;
   END IF;
END LOOP;




IF goodobject.locationprecision IS NULL THEN
   goodobject.locationprecision=badobject.locationprecision;
   doupdate:=true;
END IF;
IF goodobject.locationprecision!=badobject.locationprecision THEN
   commentstr:= commentstr || 'Location precision differs (other record = ' || badobject.locationprecision::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodobject.locationgeometry IS NULL THEN
   goodobject.locationgeometry=badobject.locationgeometry;
   doupdate:=true;
END IF;
IF goodobject.locationgeometry!=badobject.locationgeometry THEN
   SELECT 'Long: ' || x(locationgeometry) || ', Lat: ' ||  y(locationgeometry) INTO lookup FROM tblobject WHERE objectid=badobject.objectid;   
   commentstr:= commentstr || 'Location geometry differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodobject.locationtypeid IS NULL THEN
   goodobject.locationtypeid=badobject.locationtypeid;
   doupdate:=true;
END IF;
IF goodobject.locationtypeid!=badobject.locationtypeid THEN
   SELECT locationtype INTO lookup FROM tlkplocationtype WHERE locationtypeid=badobject.locationtypeid;
   commentstr:= commentstr || 'Location type differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodobject.locationcomment IS NULL THEN
   goodobject.locationcomment=badobject.locationcomment;
   doupdate:=true;
END IF;
IF goodobject.locationcomment!=badobject.locationcomment THEN
   commentstr:= commentstr || 'Location comments differ (other record = ' || badobject.locationcomment::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodobject.file IS NULL THEN
   goodobject.file=badobject.file;
   doupdate:=true;
END IF;
IF goodobject.file!=badobject.file THEN
   commentstr:= commentstr || 'File links differ (other record = ' || badobject.file::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodobject.creator IS NULL THEN
   goodobject.creator=badobject.creator;
   doupdate:=true;
END IF;
IF goodobject.creator!=badobject.creator THEN
   commentstr:= commentstr || 'Creator differs (other record = ' || badobject.creator::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodobject.owner IS NULL THEN
   goodobject.owner=badobject.owner;
   doupdate:=true;
END IF;
IF goodobject.owner!=badobject.owner THEN
   commentstr:= commentstr || 'Owner differs (other record = ' || badobject.owner::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodobject.description IS NULL THEN
   goodobject.description=badobject.description;
   doupdate:=true;
END IF;
IF goodobject.description!=badobject.description THEN
   commentstr:= commentstr || 'Description differs (other record = ' || badobject.file::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;
 
IF goodobject.objecttypeid IS NULL THEN
   goodobject.objecttypeid=badobject.objecttypeid;
   doupdate:=true;
END IF;
IF goodobject.objecttypeid!=badobject.objecttypeid THEN
   SELECT objecttype INTO lookup FROM tlkpobjecttype WHERE objecttypeid=badobject.objecttypeid;
   commentstr:= commentstr || 'Object type differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodobject.coveragetemporalid IS NULL THEN
   goodobject.coveragetemporalid=badobject.coveragetemporalid;
   doupdate:=true;
END IF;
IF goodobject.coveragetemporalid!=badobject.coveragetemporalid THEN
   SELECT coveragetemporal INTO lookup FROM tlkpcoveragetemporal WHERE coveragetemporalid=badobject.coveragetemporalid;
   commentstr:= commentstr || 'Temporal coverage differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodobject.coveragetemporalfoundationid IS NULL THEN
   goodobject.coveragetemporalfoundationid=badobject.coveragetemporalfoundationid;
   doupdate:=true;
END IF;
IF goodobject.coveragetemporalfoundationid!=badobject.coveragetemporalfoundationid THEN
   SELECT coveragetemporalfoundation INTO lookup FROM tlkpcoveragetemporalfoundation WHERE coveragetemporalfoundationid=badobject.coveragetemporalfoundationid;
   commentstr:= commentstr || 'Temporal coverage foundation differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;



IF (discrepancycount>0 OR doupdate=true) THEN
  IF (discrepancycount>0 AND goodobject.comments IS NOT NULL) THEN
    commentstr := goodobject.comments || discrepstr || commentstr || '***';
  ELSIF (discrepancycount>0) THEN
    commentstr := discrepstr || commentstr || '***';
  ELSE
    commentstr := goodobject.comments;
  END IF;

  UPDATE tblobject SET
    title=goodobject.title,
    code=goodobject.code,
    locationgeometry=goodobject.locationgeometry,
    locationtypeid=goodobject.locationtypeid,
    locationprecision=goodobject.locationprecision,
    locationcomment=goodobject.locationcomment,
    file=goodobject.file,
    creator=goodobject.creator,
    owner=goodobject.owner,
    description=goodobject.description,
    objecttypeid=goodobject.objecttypeid,
    coveragetemporalid=goodobject.coveragetemporalid,
    coveragetemporalfoundation=goodobject.coveragetemporalfoundation,
    comments= commentstr
    WHERE objectid=goodobjectid;
END IF;

DELETE FROM tblobject WHERE objectid=badobjectid;

SELECT * INTO goodobject FROM tblobject WHERE objectid=goodobjectid;
RETURN goodobject;
END;$_$;
 C   DROP FUNCTION cpgdb.mergeobjects(goodobject uuid, badobject uuid);
       cpgdb          postgres    false    7    231                        1255    64740    mergeradii(uuid, uuid)    FUNCTION     �  CREATE FUNCTION cpgdb.mergeradii(goodradiusid uuid, badradiusid uuid) RETURNS public.tblradius
    LANGUAGE plpgsql
    AS $_$DECLARE
goodradiusid ALIAS FOR $1;    -- ID of radius with correct entities
goodradius tblradius%ROWTYPE; -- The radius itself
badradiusid ALIAS FOR $2;     -- ID of radius with incorrect entities
badradius tblradius%ROWTYPE;  -- The radius itself
commentstr varchar;           -- Used to compile comments for indicating discrepancies 
lookup varchar;               -- Temporary variable for storing values from lookup tables
discrepstr varchar;           -- Basic comment to highlight discrepancies
discrepancycount integer;     -- Count of number of fields that aren't the same
doupdate boolean;             -- Temporary variable for flagging whether to update this record

BEGIN

discrepancycount:=0;
commentstr:= '';
discrepstr:='*** Another record was automatically merged with this one and discrepancies were detected.  Please check the following issues: ';
doupdate:=false;

SELECT * INTO goodradius FROM tblradius WHERE radiusid=goodradiusid;
IF NOT FOUND THEN
   RAISE EXCEPTION 'The good radius with id % was not found', goodradiusid;
   RETURN NULL;
END IF;
SELECT * INTO badradius FROM tblradius WHERE radiusid=badradiusid;
IF NOT FOUND THEN
   RAISE EXCEPTION 'The bad radius with id % was not found', badradiusid;
   RETURN NULL;
END IF;

IF goodradius.radiusid=badradius.radiusid THEN 
   RAISE NOTICE 'Radii are the same!';
   RETURN goodradius;
END IF;

UPDATE tblmeasurement SET radiusid=goodradiusid WHERE radiusid=badradiusid;


IF goodradius.numberofsapwoodrings IS NULL THEN
   goodradius.numberofsapwoodrings=badradius.numberofsapwoodrings;
   doupdate:=true;
END IF;
IF goodradius.numberofsapwoodrings!=badradius.numberofsapwoodrings THEN
   commentstr:= commentstr || 'Sapwood rings differ (other record = ' || badradius.numberofsapwoodrings::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodradius.pithid IS NULL THEN
   goodradius.pithid=badradius.pithid;
   doupdate:=true;
END IF;
IF goodradius.pithid!=badradius.pithid THEN
   SELECT complexpresenceabsence INTO lookup FROM tlkpcomplexpresenceabsence WHERE complexpresenceabsenceid=badradius.pithid;
   commentstr:= commentstr || 'Pith presence differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodradius.barkpresent IS NULL THEN
   goodradius.barkpresent=badradius.barkpresent;
   doupdate:=true;
END IF;
IF goodradius.barkpresent!=badradius.barkpresent THEN
   commentstr:= commentstr || 'Bark presence differs (other record = ' || badradius.barkpresent::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodradius.lastringunderbark IS NULL THEN
   goodradius.lastringunderbark=badradius.lastringunderbark;
   doupdate:=true;
END IF;
IF goodradius.lastringunderbark!=badradius.lastringunderbark THEN
   commentstr:= commentstr || 'Last ring under bark differs (other record = ' || badradius.lastringunderbark::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodradius.missingheartwoodringstopith IS NULL THEN
   goodradius.missingheartwoodringstopith=badradius.missingheartwoodringstopith;
   doupdate:=true;
END IF;
IF goodradius.missingheartwoodringstopith!=badradius.missingheartwoodringstopith THEN
   commentstr:= commentstr || 'Missing heartwood rings differ (other record = ' || badradius.missingheartwoodringstopith::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodradius.missingsapwoodringstobark IS NULL THEN
   goodradius.missingsapwoodringstobark=badradius.missingsapwoodringstobark;
   doupdate:=true;
END IF;
IF goodradius.missingsapwoodringstobark!=badradius.missingsapwoodringstobark THEN
   commentstr:= commentstr || 'Missing sapwood rings differ (other record = ' || badradius.missingsapwoodringstobark::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodradius.missingheartwoodringstopithfoundation IS NULL THEN
   goodradius.missingheartwoodringstopithfoundation=badradius.missingheartwoodringstopithfoundation;
   doupdate:=true;
END IF;
IF goodradius.missingheartwoodringstopithfoundation!=badradius.missingheartwoodringstopithfoundation THEN
   commentstr:= commentstr || 'Missing heartwood rings foundation differs (other record = ' || badradius.missingheartwoodringstopithfoundation::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodradius.missingsapwoodringstobarkfoundation IS NULL THEN
   goodradius.missingsapwoodringstobarkfoundation=badradius.missingsapwoodringstobarkfoundation;
   doupdate:=true;
END IF;
IF goodradius.missingsapwoodringstobarkfoundation!=badradius.missingsapwoodringstobarkfoundation THEN
   commentstr:= commentstr || 'Missing sapwood rings foundation differs (other record = ' || badradius.missingsapwoodringstobarkfoundation::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodradius.sapwoodid IS NULL THEN
   goodradius.sapwoodid=badradius.sapwoodid;
   doupdate:=true;
END IF;
IF goodradius.sapwoodid!=badradius.sapwoodid THEN
   SELECT complexpresenceabsence INTO lookup FROM tlkpcomplexpresenceabsence WHERE complexpresenceabsenceid=badradius.sapwoodid;
   commentstr:= commentstr || 'Sapwood presence differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodradius.heartwoodid IS NULL THEN
   goodradius.heartwoodid=badradius.heartwoodid;
   doupdate:=true;
END IF;
IF goodradius.heartwoodid!=badradius.heartwoodid THEN
   SELECT complexpresenceabsence INTO lookup FROM tlkpcomplexpresenceabsence WHERE complexpresenceabsenceid=badradius.heartwoodid;
   commentstr:= commentstr || 'Heartwood presence differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodradius.azimuth IS NULL THEN
   goodradius.azimuth=badradius.azimuth;
   doupdate:=true;
END IF;
IF goodradius.azimuth!=badradius.azimuth THEN
   commentstr:= commentstr || 'Azimuth differs (other record = ' || badradius.azimuth::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;


IF (discrepancycount>0 OR doupdate=true) THEN
  IF (discrepancycount>0 AND goodradius.comments IS NOT NULL) THEN
    commentstr := goodradius.comments || discrepstr || commentstr || '***';
  ELSIF (discrepancycount>0) THEN
    commentstr := discrepstr || commentstr || '***';
  ELSE
    commentstr := goodradius.comments;
  END IF;
  
  UPDATE tblradius SET
    code=goodradius.code,
    numberofsapwoodrings=goodradius.numberofsapwoodrings,
    pithid=goodradius.pithid,
    barkpresent=goodradius.barkpresent,
    lastringunderbark=goodradius.lastringunderbark,
    missingheartwoodringstopith=goodradius.missingheartwoodringstopith,
    missingheartwoodringstopithfoundation=goodradius.missingheartwoodringstopithfoundation,
    missingsapwoodringstobark=goodradius.missingsapwoodringstobark,
    missingsapwoodringstobarkfoundation=goodradius.missingsapwoodringstobarkfoundation,
    sapwoodid=goodradius.sapwoodid,
    heartwoodid=goodradius.heartwoodid,
    azimuth=goodradius.azimuth,
    comments = commentstr
    WHERE radiusid=goodradius.radiusid;
  
END IF;

DELETE FROM tblradius WHERE radiusid=badradiusid;

SELECT * INTO goodradius FROM tblradius WHERE radiusid=goodradiusid;
RETURN goodradius;
END;$_$;
 E   DROP FUNCTION cpgdb.mergeradii(goodradiusid uuid, badradiusid uuid);
       cpgdb          postgres    false    7    239            3           0    0 8   FUNCTION mergeradii(goodradiusid uuid, badradiusid uuid)    COMMENT     >  COMMENT ON FUNCTION cpgdb.mergeradii(goodradiusid uuid, badradiusid uuid) IS 'This function merges a ''bad'' radius with a ''good'' radius.  References to the bad radius are changed to point to the new radius, any discrepancies between fields are marked in the comments field and then the ''bad'' radius is deleted.';
          cpgdb          postgres    false    1280                       1255    64741    mergesamples(uuid, uuid)    FUNCTION     �  CREATE FUNCTION cpgdb.mergesamples(goodsample uuid, badsample uuid) RETURNS public.tblsample
    LANGUAGE plpgsql
    AS $_$DECLARE
goodsampleid ALIAS FOR $1;    -- ID of sample with correct entities
goodsample tblsample%ROWTYPE; -- The series itself
badsampleid ALIAS FOR $2;     -- ID of sample with incorrect entities
badsample tblsample%ROWTYPE;  -- The series itself
commentstr varchar;           -- Used to compile comments for indicating discrepancies 
discrepstr varchar;           -- Basic comment to highlight discrepancies
discrepancycount integer;     -- Count of number of fields that aren't the same

lookup varchar;               -- Temporary variable for storing values from lookup tables
badrad RECORD;                -- Temporary variable for storing associated radii
goodradiusid uuid;            -- Temporary variable for storing uuid of good associatedradii
doupdate boolean;             -- Temporary variable for flagging whether to update this record

BEGIN

discrepancycount:=0;
commentstr:= '';
discrepstr:='*** Another record was automatically merged with this one and discrepancies were detected.  Please check the following issues: ';
doupdate:=false;

SELECT * INTO goodsample FROM tblsample WHERE sampleid=goodsampleid;
IF NOT FOUND THEN
   RAISE NOTICE 'The good sample with id % was not found', goodsampleid;
   RETURN NULL;
END IF;
SELECT * INTO badsample FROM tblsample WHERE sampleid=badsampleid;
IF NOT FOUND THEN
   RAISE NOTICE 'The bad sample with id % was not found', badsampleid;
   RETURN NULL;
END IF;

IF goodsample.sampleid=badsample.sampleid THEN 
   RAISE NOTICE 'Samples are the same!';
   RETURN goodsample;
END IF;

FOR badrad IN SELECT * FROM tblradius WHERE sampleid=badsampleid ORDER BY code LOOP 
   SELECT radiusid INTO goodradiusid FROM tblradius WHERE sampleid=goodsampleid AND code=badrad.code;
   IF goodradiusid IS NOT NULL THEN
       PERFORM cpgdb.mergeradii(goodradiusid, badrad.radiusid);
   ELSE
       UPDATE tblradius SET sampleid=goodsampleid WHERE radiusid=badrad.radiusid;
   END IF;
END LOOP;



IF goodsample.samplingdate IS NULL THEN
   goodsample.samplingdate=badsample.samplingdate;
   doupdate:=true;
END IF;
IF goodsample.samplingdate!=badsample.samplingdate THEN
   commentstr:= commentstr || 'Sampling date differs (other record = ' || badsample.samplingdate::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodsample.file IS NULL THEN
   goodsample.file=badsample.file;
   doupdate:=true;
END IF;
IF goodsample.file!=badsample.file THEN
   commentstr:= commentstr || 'File links differ (other record = ' || badsample.file::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodsample.position IS NULL THEN
   goodsample.position=badsample.position;
   doupdate:=true;
END IF;
IF goodsample.position!=badsample.position THEN
   commentstr:= commentstr || 'Position of sample differs (other record = ' || badsample.position::varchar || ')';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodsample.state IS NULL THEN
   goodsample.state=badsample.state;
   doupdate:=true;
END IF;
IF goodsample.state!=badsample.state THEN
   commentstr:= commentstr || 'State of sample differs (other record = ' || badsample.state::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodsample.knots IS NULL THEN
   goodsample.knots=badsample.knots;
   doupdate:=true;
END IF;
IF goodsample.knots!=badsample.knots THEN
   commentstr:= commentstr || 'Presence of knots differs (other record = ' || badsample.knots::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodsample.description IS NULL THEN
   goodsample.description=badsample.description;
   doupdate:=true;
END IF;
IF goodsample.description!=badsample.description THEN
   commentstr:= commentstr || 'Description differs (other record = ''' || badsample.description::varchar || '''). ';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodsample.datecertaintyid IS NULL THEN
   goodsample.datecertaintyid=badsample.datecertaintyid;
   doupdate:=true;
END IF;
IF goodsample.datecertaintyid!=badsample.datecertaintyid THEN
   SELECT datecertainty INTO lookup FROM tlkpdatecertainty WHERE datecertaintyid=badsample.datecertaintyid;
   commentstr:= commentstr || 'Date certainty differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodsample.typeid IS NULL THEN
   goodsample.typeid=badsample.typeid;
   doupdate:=true;
END IF;
IF goodsample.typeid!=badsample.typeid THEN
   SELECT sampletype INTO lookup FROM tlkpsampletype WHERE sampletypeid=badsample.typeid;
   commentstr:= commentstr || 'Sample type differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;

IF goodsample.boxid IS NULL THEN
   goodsample.boxid=badsample.boxid;
   doupdate:=true;
END IF;
IF goodsample.boxid!=badsample.boxid THEN
   SELECT title INTO lookup FROM tblbox WHERE boxid=badsample.boxid;
   commentstr:= commentstr || 'Box differs (other record = ' || lookup::varchar || '). ';
   discrepancycount:=discrepancycount+1;
END IF;


IF (discrepancycount>0 OR doupdate=true) THEN
  IF (discrepancycount>0 AND goodsample.comments IS NOT NULL) THEN
    commentstr := goodsample.comments || discrepstr || commentstr || '***';
  ELSIF (discrepancycount>0) THEN
    commentstr := discrepstr || commentstr || '***';
  ELSE
    commentstr := goodsample.comments;
  END IF;
  
  UPDATE tblsample SET
    code=goodsample.code,
    samplingdate=goodsample.samplingdate,
    file=goodsample.file,
    position=goodsample.position,
    state=goodsample.state,
    knots=goodsample.knots,
    description=goodsample.description,
    datecertaintyid=goodsample.datecertaintyid,
    typeid=goodsample.typeid,
    boxid=goodsample.boxid,
    comments=commentstr
    WHERE sampleid=goodsample.sampleid;
  
END IF;

DELETE FROM tblsample WHERE sampleid=badsampleid;

SELECT * INTO goodsample FROM tblsample WHERE sampleid=goodsampleid;
RETURN goodsample;
END;$_$;
 C   DROP FUNCTION cpgdb.mergesamples(goodsample uuid, badsample uuid);
       cpgdb          postgres    false    7    240                       1255    64742    mpsobject(integer)    FUNCTION     �  CREATE FUNCTION cpgdb.mpsobject(securityuserid integer) RETURNS SETOF public.tblobject
    LANGUAGE plpgsql
    AS $_$DECLARE
  suserid ALIAS FOR $1;
  query VARCHAR;
  objectid INTEGER;
  permres typpermissionset;
  objectres  tblobject%ROWTYPE;
BEGIN

  query := 'SELECT * from tblobject';

  FOR objectres IN EXECUTE query LOOP	
    FOR permres IN SELECT * FROM cpgdb.getuserpermissionset(suserid, 'object', objectres.objectid) LOOP
	IF permres.canread= true THEN
		--RAISE NOTICE 'Permission granted on object %', objectres.objectid;
		RETURN NEXT objectres;
	ELSE
		--RAISE NOTICE 'Permission denied on object %', objectres.objectid;
	END IF;
    END LOOP;
  END LOOP;


END;$_$;
 7   DROP FUNCTION cpgdb.mpsobject(securityuserid integer);
       cpgdb          postgres    false    231    7                       1255    64743    mpstree(integer)    FUNCTION     �  CREATE FUNCTION cpgdb.mpstree(securityuserid integer) RETURNS SETOF public.tblelement
    LANGUAGE plpgsql
    AS $_$DECLARE
  suserid ALIAS FOR $1;
  query VARCHAR;
  elementid INTEGER;
  permres typpermissionset;
  treeres  tblelement%ROWTYPE;
BEGIN

  query := 'SELECT * from tblelement';

  FOR treeres IN EXECUTE query LOOP	
    FOR permres IN SELECT * FROM cpgdb.getuserpermissionset(suserid, 'tree', treeres.elementid) LOOP
	IF permres.canread= true THEN
		--RAISE NOTICE 'Permission granted on tree %', treeres.elementid;
		RETURN NEXT treeres;
	ELSE
		--RAISE NOTICE 'Permission denied on tree %', treeres.elementid;
	END IF;
    END LOOP;
  END LOOP;


END;$_$;
 5   DROP FUNCTION cpgdb.mpstree(securityuserid integer);
       cpgdb          postgres    false    7    236                       1255    64744 !   mpsvmeasurementmetacache(integer)    FUNCTION     �  CREATE FUNCTION cpgdb.mpsvmeasurementmetacache(securityuserid integer) RETURNS SETOF public.tblvmeasurementmetacache
    LANGUAGE plpgsql
    AS $_$DECLARE
  suserid ALIAS FOR $1;
  query VARCHAR;
  vmmcid INTEGER;
  permres typpermissionset;
  vmmcres  tblvmeasurementmetacache%ROWTYPE;
BEGIN

  query := 'SELECT * from tblvmeasurementmetacache';

  FOR vmmcres IN EXECUTE query LOOP	
    FOR permres IN SELECT * FROM cpgdb.getuserpermissionset(suserid, 'vmeasurement', vmmcres.vmeasurementid) LOOP
	IF permres.canread= true THEN
		--RAISE NOTICE 'Permission granted on vm %', vmmcres.vmeasurementid;
		RETURN NEXT vmmcres;
	ELSE
		--RAISE NOTICE 'Permission denied on vm %', vmmcres.vmeasurementid;
	END IF;
    END LOOP;
  END LOOP;


END;$_$;
 F   DROP FUNCTION cpgdb.mpsvmeasurementmetacache(securityuserid integer);
       cpgdb          postgres    false    7    233                       1255    64745    objectregionmodified()    FUNCTION     �   CREATE FUNCTION cpgdb.objectregionmodified() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN

PERFORM cpgdb.update_objectregions(NEW.regionid, 'region');
RETURN NEW;


END;$$;
 ,   DROP FUNCTION cpgdb.objectregionmodified();
       cpgdb          postgres    false    7                       1255    64746    populate_cache()    FUNCTION       CREATE FUNCTION cpgdb.populate_cache() RETURNS text
    LANGUAGE plpgsql
    AS $$
DECLARE
   vsid tblVMeasurement.VMeasurementID%TYPE;
   ret text;
   cnt integer;
   badcnt integer;
   dummy text;
BEGIN
   cnt := 0;
   badcnt := 0;

   FOR vsid in SELECT VMeasurementID FROM tblVMeasurement ORDER BY CreatedTimestamp LOOP
      RAISE NOTICE 'Populating VMeasurement % cache...', vsid;
      cnt := cnt + 1;

      SELECT vMeasurementid INTO dummy FROM cpgdb.CreateMetaCache(vsid);

      IF dummy IS NULL THEN
         IF badcnt = 0 THEN
            ret := 'Bad: ';
         ELSE
            ret := ret || ', ';
         END IF;

         badcnt := badcnt + 1;
         ret := ret || vsid; 

         RAISE NOTICE 'Well, that didn\'t work for %', vsid;
      END IF;
   END LOOP;

   return 'Attempted to populate ' || cnt || ' vmeasurement caches. ' || badcnt || ' failed. ' || ret;
END;
$$;
 &   DROP FUNCTION cpgdb.populate_cache();
       cpgdb          postgres    false    7                       1255    64747    populate_cache_gaps()    FUNCTION     �   CREATE FUNCTION cpgdb.populate_cache_gaps() RETURNS text
    LANGUAGE sql
    AS $$
   SELECT * from cpgdb.populate_cache_gaps(-1);
$$;
 +   DROP FUNCTION cpgdb.populate_cache_gaps();
       cpgdb          postgres    false    7                       1255    64748    populate_cache_gaps(integer)    FUNCTION     �  CREATE FUNCTION cpgdb.populate_cache_gaps(maxcnt integer) RETURNS text
    LANGUAGE plpgsql
    AS $$
DECLARE
   vsid tblVMeasurement.VMeasurementID%TYPE;
   ret text;
   cnt integer;
   badcnt integer;
   dummy text;
BEGIN
   cnt := 0;
   badcnt := 0;

   FOR vsid in SELECT vm.VMeasurementID FROM tblVMeasurement vm LEFT JOIN tblVMeasurementMetaCache vmc USING (vmeasurementid) WHERE vmc.StartYear IS NULL ORDER BY CreatedTimestamp LOOP
      RAISE NOTICE 'Populating VMeasurement % cache...', vsid;
      cnt := cnt + 1;

      SELECT vMeasurementid INTO dummy FROM cpgdb.CreateMetaCache(vsid);

      IF dummy IS NULL THEN
         IF badcnt = 0 THEN
            ret := 'Bad: ';
         ELSE
            ret := ret || ', ';
         END IF;

         badcnt := badcnt + 1;
         ret := ret || vsid; 

         RAISE NOTICE 'Well, that didn\'t work for %', vsid;
      END IF;

      IF maxcnt > 0 AND cnt >= maxcnt THEN
         return 'Attempted to populate ' || cnt || ' vmeasurement caches. ' || badcnt || ' failed. ' || ret;
      END IF;
   END LOOP;

   return 'Attempted to populate ' || cnt || ' vmeasurement caches. ' || badcnt || ' failed. ' || ret;
END;
$$;
 9   DROP FUNCTION cpgdb.populate_cache_gaps(maxcnt integer);
       cpgdb          postgres    false    7            �           1255    66835 E   preferreddouble(double precision, double precision, double precision)    FUNCTION     A  CREATE FUNCTION cpgdb.preferreddouble(in1 double precision, in2 double precision, in3 double precision) RETURNS double precision
    LANGUAGE plpgsql STABLE
    AS $$
BEGIN

IF in1 IS NOT NULL THEN RETURN in1; 
ELSIF in2 IS NOT NULL THEN RETURN in2;
ELSIF in3 IS NOT NULL THEN RETURN in3;
END IF;

RETURN NULL; 

END;$$;
 g   DROP FUNCTION cpgdb.preferreddouble(in1 double precision, in2 double precision, in3 double precision);
       cpgdb          tellervo    false    7            	           1255    64749    purgecachetables()    FUNCTION     �   CREATE FUNCTION cpgdb.purgecachetables() RETURNS void
    LANGUAGE plpgsql
    AS $$BEGIN
DELETE FROM tblvmeasurementresult;
DELETE FROM tblvmeasurementmetacache;
END;$$;
 (   DROP FUNCTION cpgdb.purgecachetables();
       cpgdb          postgres    false    7            
           1255    64750 .   qappvmeasurementresultreadingopsum(uuid, uuid)    FUNCTION     �
  CREATE FUNCTION cpgdb.qappvmeasurementresultreadingopsum(uuid, uuid) RETURNS integer
    LANGUAGE plpgsql
    AS $_$
DECLARE
   ref refcursor;
   v_year integer;    -- the record's year
   v_reading integer; -- the record's reading
   v_id varchar;      -- the record's vMeasurementresultid

   startyear integer; -- the minimum year in the data
   endyear integer;   --     maximum

   idx integer;
   avg double precision;

   sum integer[];
   samplecount integer[];
   wj_inc integer[];
   wj_dec integer[];

   lastid varchar := '';
   lastval integer;
BEGIN
   OPEN ref FOR SELECT
      MIN(tblVMeasurementResult.StartYear + tblVMeasurementReadingResult.RelYear) as MinYear,
      MAX(tblVMeasurementResult.StartYear + tblVMeasurementReadingResult.RelYear) as MaxYear
      FROM tblVMeasurementResult
      INNER JOIN tblVMeasurementReadingResult ON
      tblVMeasurementResult.VMeasurementResultID =
      tblVMeasurementReadingResult.VMeasurementResultID
      WHERE tblVMeasurementResult.VMeasurementResultGroupID = $1;

   FETCH ref INTO startyear, endyear;
   CLOSE ref;

   OPEN ref FOR SELECT
      tblVMeasurementResult.VMeasurementResultID as ID, 
      tblVMeasurementResult.StartYear + tblVMeasurementReadingResult.RelYear as Year,
      tblVMeasurementReadingResult.Reading as Reading
      FROM tblVMeasurementResult
      INNER JOIN tblVMeasurementReadingResult ON
      tblVMeasurementResult.VMeasurementResultID =
      tblVMeasurementReadingResult.VMeasurementResultID
      WHERE tblVMeasurementResult.VMeasurementResultGroupID = $1
      ORDER BY ID, Year;

   FETCH ref INTO v_id, v_year, v_reading;
   WHILE FOUND LOOP
      idx := v_year - startyear;

      IF samplecount[idx] IS NULL THEN
         samplecount[idx] := 0;
         sum[idx] := 0;
         wj_dec[idx] := 0;
         wj_inc[idx] := 0;
      END IF;

      IF lastid <> v_id THEN
         lastid := v_id;
      ELSE
         IF v_reading < lastval THEN
            wj_dec[idx] := wj_dec[idx] + 1;
         ELSIF v_reading > lastval THEN
            wj_inc[idx] := wj_inc[idx] + 1;
         END IF;
      END IF;

      lastval := v_reading;
      sum[idx] := sum[idx] + v_reading;
      samplecount[idx] := samplecount[idx] + 1;

      FETCH ref INTO v_id, v_year, v_reading;
   END LOOP;

   FOR i IN startyear..endyear LOOP
      idx := i - startyear;
      IF samplecount[idx] IS NULL THEN
         RAISE EXCEPTION 'Sum is not contiguous at year %', i;
      END IF;

      avg := round(cast(sum[idx] as numeric) / samplecount[idx]);

      INSERT INTO tblVMeasurementReadingResult(VMeasurementResultID, RelYear, Reading, WJInc, WJDec, Count) VALUES
         ($2, idx, avg, wj_inc[idx], wj_dec[idx], samplecount[idx]);

   END LOOP;

   RETURN idx;
END;
$_$;
 D   DROP FUNCTION cpgdb.qappvmeasurementresultreadingopsum(uuid, uuid);
       cpgdb          postgres    false    7            �           1255    66885     qrytaxonflat1(character varying)    FUNCTION       CREATE FUNCTION cpgdb.qrytaxonflat1(taxonid character varying) RETURNS SETOF public.typtaxonrankname
    LANGUAGE sql
    AS $_$SELECT $1 as taxonid, tlkptaxon.taxonrankid, tlkptaxon.label AS col0
FROM ((((((((((tlkptaxon 
                                        LEFT JOIN tlkptaxon AS tlkptaxon_1 ON tlkptaxon.taxonid = tlkptaxon_1.parenttaxonid) 
                                    LEFT JOIN tlkptaxon AS tlkptaxon_2 ON tlkptaxon_1.taxonid = tlkptaxon_2.parenttaxonid) 
                                LEFT JOIN tlkptaxon AS tlkptaxon_3 ON tlkptaxon_2.taxonid = tlkptaxon_3.parenttaxonid) 
                            LEFT JOIN tlkptaxon AS tlkptaxon_4 ON tlkptaxon_3.taxonid = tlkptaxon_4.parenttaxonid) 
                        LEFT JOIN tlkptaxon AS tlkptaxon_5 ON tlkptaxon_4.taxonid = tlkptaxon_5.parenttaxonid) 
                    LEFT JOIN tlkptaxon AS tlkptaxon_6 ON tlkptaxon_5.taxonid = tlkptaxon_6.parenttaxonid) 
                LEFT JOIN tlkptaxon AS tlkptaxon_7 ON tlkptaxon_6.taxonid = tlkptaxon_7.parenttaxonid) 
            LEFT JOIN tlkptaxon AS tlkptaxon_8 ON tlkptaxon_7.taxonid = tlkptaxon_8.parenttaxonid) 
        LEFT JOIN tlkptaxon AS tlkptaxon_9 ON tlkptaxon_8.taxonid = tlkptaxon_9.parenttaxonid) 
    LEFT JOIN tlkptaxon AS tlkptaxon_10 ON tlkptaxon_9.taxonid = tlkptaxon_10.parenttaxonid) 
LEFT JOIN tlkptaxon AS tlkptaxon_11 ON tlkptaxon_10.taxonid = tlkptaxon_11.parenttaxonid
WHERE 
   (((tlkptaxon.taxonid)=$1)) 
OR (((tlkptaxon_1.taxonid)=$1)) 
OR (((tlkptaxon_2.taxonid)=$1)) 
OR (((tlkptaxon_3.taxonid)=$1)) 
OR (((tlkptaxon_4.taxonid)=$1)) 
OR (((tlkptaxon_5.taxonid)=$1)) 
OR (((tlkptaxon_6.taxonid)=$1)) 
OR (((tlkptaxon_7.taxonid)=$1)) 
OR (((tlkptaxon_8.taxonid)=$1)) 
OR (((tlkptaxon_9.taxonid)=$1)) 
OR (((tlkptaxon_10.taxonid)=$1)) 
OR (((tlkptaxon_11.taxonid)=$1))
$_$;
 >   DROP FUNCTION cpgdb.qrytaxonflat1(taxonid character varying);
       cpgdb          tellervo    false    7    2747            �           1255    66886     qrytaxonflat2(character varying)    FUNCTION     �  CREATE FUNCTION cpgdb.qrytaxonflat2(taxonid character varying) RETURNS SETOF public.typtaxonflat2
    LANGUAGE sql
    AS $_$SELECT qrytaxonflat1.taxonid, 
qrytaxonflat1.taxonrankid, 
qrytaxonflat1.taxonname, 
tlkptaxonrank.taxonrank, 
tlkptaxonrank.rankorder
FROM cpgdb.qrytaxonflat1($1) qrytaxonflat1 
RIGHT JOIN tlkptaxonrank ON qrytaxonflat1.taxonrankid = tlkptaxonrank.taxonrankid 
ORDER BY tlkptaxonrank.rankorder ASC;$_$;
 >   DROP FUNCTION cpgdb.qrytaxonflat2(taxonid character varying);
       cpgdb          tellervo    false    7    2750            �           1255    66912    qrytaxonomy(character varying)    FUNCTION     3  CREATE FUNCTION cpgdb.qrytaxonomy(thetaxonid character varying) RETURNS public.typfulltaxonomy
    LANGUAGE sql
    AS $_$  
SELECT *
FROM crosstab(
'select '''||$1||'''::varchar as taxonid, taxonrank, taxonname from cpgdb.qrytaxonflat2('''||$1||''') ORDER BY taxonrank'

, 
    'select taxonrank
    from tlkptaxonrank 
    order by rankorder asc'

)
     AS taxonomylist(taxonid varchar, 
     kingdom varchar, 
    subkingdom varchar, 
    phylum varchar, 
    division varchar, 
    class varchar, 
    txorder varchar, 
    family varchar, 
    subfamily varchar,
    genus varchar,
    subgenus varchar, 
    section varchar,
    subsection varchar,
    species varchar, 
    subspecies varchar, 
    race varchar, 
    variety varchar, 
    subvariety varchar, 
    form varchar, 
    subform varchar);
    $_$;
 ?   DROP FUNCTION cpgdb.qrytaxonomy(thetaxonid character varying);
       cpgdb          tellervo    false    7    2753                       1255    64754     qupdvmeasurementresultinfo(uuid)    FUNCTION     �  CREATE FUNCTION cpgdb.qupdvmeasurementresultinfo(uuid) RETURNS boolean
    LANGUAGE plpgsql
    AS $_$
DECLARE
   ResultID ALIAS FOR $1;
   vmid tblVMeasurement.VMeasurementID%TYPE;
   c tblVMeasurement.Code%TYPE;
   comm tblVMeasurement.Comments%TYPE;
   ispub tblVMeasurement.isPublished%TYPE;
   createTS tblVMeasurement.CreatedTimestamp%TYPE;
   modTS tblVMeasurement.LastModifiedTimestamp%TYPE;
BEGIN
   SELECT VMeasurementID INTO vmid FROM tblVMeasurementResult WHERE VMeasurementResultID = ResultID;

   IF NOT FOUND THEN
      RETURN FALSE;
   END IF;

   SELECT Code, Comments, isPublished, CreatedTimestamp, LastModifiedTimestamp
     INTO c, comm, ispub, createTS, modTS
     FROM tblVMeasurement WHERE VMeasurementID = vmid;

   IF NOT FOUND THEN
      RETURN FALSE;
   END IF;

   UPDATE tblVMeasurementResult SET (Code, Comments, isPublished, CreatedTimestamp, LastModifiedTimestamp) =
      (c, comm, ispub, createTS, modTS) 
      WHERE VMeasurementResultID = ResultID;

   RETURN TRUE;
END;
$_$;
 6   DROP FUNCTION cpgdb.qupdvmeasurementresultinfo(uuid);
       cpgdb          postgres    false    7                       1255    64755    rebuildmetacache()    FUNCTION       CREATE FUNCTION cpgdb.rebuildmetacache() RETURNS void
    LANGUAGE plpgsql
    AS $$DECLARE
    uid uuid;
BEGIN
PERFORM cpgdb.purgecachetables();
FOR uid IN SELECT vmeasurementid FROM tblvmeasurement
    LOOP
        PERFORM cpgdb.createmetacache(uid);
    END LOOP;
RETURN;
END;$$;
 (   DROP FUNCTION cpgdb.rebuildmetacache();
       cpgdb          postgres    false    7                       1255    64756    rebuildmetacacheforelement()    FUNCTION       CREATE FUNCTION cpgdb.rebuildmetacacheforelement() RETURNS trigger
    LANGUAGE plpgsql
    AS $$DECLARE
  uid uuid;
BEGIN
FOR uid IN SELECT vmeasurementid FROM cpgdb.FindChildrenOf('Element', NEW.elementid)
  LOOP
    PERFORM cpgdb.createmetacache(uid);
  END LOOP;
RETURN new;
END;$$;
 2   DROP FUNCTION cpgdb.rebuildmetacacheforelement();
       cpgdb          postgres    false    7                       1255    64757    rebuildmetacacheforobject()    FUNCTION       CREATE FUNCTION cpgdb.rebuildmetacacheforobject() RETURNS trigger
    LANGUAGE plpgsql
    AS $$DECLARE
  uid uuid;
BEGIN
FOR uid IN SELECT vmeasurementid FROM cpgdb.findchildrenof('Object', NEW.objectid)
  LOOP
    PERFORM cpgdb.createmetacache(uid);
  END LOOP;
RETURN new;
END;$$;
 1   DROP FUNCTION cpgdb.rebuildmetacacheforobject();
       cpgdb          postgres    false    7                       1255    64758    rebuildmetacacheforradius()    FUNCTION       CREATE FUNCTION cpgdb.rebuildmetacacheforradius() RETURNS trigger
    LANGUAGE plpgsql
    AS $$DECLARE
  uid uuid;
BEGIN
FOR uid IN SELECT vmeasurementid FROM cpgdb.findchildrenof('Radius', NEW.radiusid)
  LOOP
    PERFORM cpgdb.createmetacache(uid);
  END LOOP;
RETURN new;
END;$$;
 1   DROP FUNCTION cpgdb.rebuildmetacacheforradius();
       cpgdb          postgres    false    7                       1255    64759    rebuildmetacacheforsample()    FUNCTION       CREATE FUNCTION cpgdb.rebuildmetacacheforsample() RETURNS trigger
    LANGUAGE plpgsql
    AS $$DECLARE
  uid uuid;
BEGIN
FOR uid IN SELECT vmeasurementid FROM cpgdb.findchildrenof('Sample', NEW.sampleid)
  LOOP
    PERFORM cpgdb.createmetacache(uid);
  END LOOP;
RETURN new;
END;$$;
 1   DROP FUNCTION cpgdb.rebuildmetacacheforsample();
       cpgdb          postgres    false    7                       1255    64760 )   recursefindobjectancestors(uuid, integer)    FUNCTION     �  CREATE FUNCTION cpgdb.recursefindobjectancestors(objid uuid, _recursionlevel integer) RETURNS SETOF public.tblobject
    LANGUAGE plpgsql STABLE
    AS $$
DECLARE
   parentid tblobject.objectid%TYPE;
   lastRow tblObject;
   ref refcursor;
   recursionlevel integer := _recursionlevel;
BEGIN
   IF recursionlevel > 50 THEN
      RAISE EXCEPTION 'Maximum recursion exceeded';
   END IF;

   SELECT * INTO lastRow FROM tblObject WHERE objectId = objid;

   IF NOT FOUND THEN
      RAISE EXCEPTION 'Object ID % not found. Either original object does not exist or object tree is broken.', objid;
   END IF;

   IF recursionlevel <> 0 THEN
      RETURN NEXT lastRow;
   END IF;

   parentid := lastRow.parentObjectId;

   IF parentid IS NULL THEN
      RETURN;
   END IF;

   FOR lastRow IN SELECT * FROM cpgdb.recurseFindObjectAncestors(parentid, recursionlevel + 1) LOOP
      RETURN NEXT lastRow;
   END LOOP;
END;
$$;
 U   DROP FUNCTION cpgdb.recursefindobjectancestors(objid uuid, _recursionlevel integer);
       cpgdb          postgres    false    7    231                       1255    64761 +   recursefindobjectdescendants(uuid, integer)    FUNCTION     �  CREATE FUNCTION cpgdb.recursefindobjectdescendants(objid uuid, _recursionlevel integer) RETURNS SETOF public.tblobject
    LANGUAGE plpgsql STABLE
    AS $$
DECLARE
   lastRow tblObject;
   lastParent tblObject.parentObjectId%TYPE;
   ref refcursor;
   recursionlevel integer := _recursionlevel;
BEGIN
   IF recursionlevel > 50 THEN
      RAISE EXCEPTION 'Maximum recursion exceeded';
   END IF;

   IF recursionlevel = -1 THEN
      SELECT * INTO lastRow FROM tblObject WHERE objectId = objid;
      RETURN NEXT lastRow;

      recursionlevel := 1;
   END IF;

   OPEN ref FOR SELECT * FROM tblObject WHERE parentObjectID = objid;

   LOOP
      FETCH ref INTO lastRow;

      IF NOT FOUND THEN
         CLOSE ref;
         EXIT;
      END IF;

      RETURN NEXT lastRow;

      lastParent := lastRow.objectId;
      FOR lastRow IN SELECT * from cpgdb.recurseFindObjectDescendants(lastParent, recursionlevel + 1) LOOP
         RETURN NEXT lastRow;
      END LOOP;
   END LOOP;
END;
$$;
 W   DROP FUNCTION cpgdb.recursefindobjectdescendants(objid uuid, _recursionlevel integer);
       cpgdb          postgres    false    7    231                       1255    64762 $   recursefindvmchildren(uuid, integer)    FUNCTION     �  CREATE FUNCTION cpgdb.recursefindvmchildren(uuid, integer) RETURNS SETOF public.typvmeasurementsearchresult
    LANGUAGE plpgsql
    AS $_$
DECLARE
   _vsid ALIAS FOR $1;
   _recursionlevel INTEGER := $2;

   res typVMeasurementSearchResult;
   meta tblVMeasurementMetaCache%ROWTYPE;
   ref refcursor;

   VMeasurementID tblVMeasurement.VMeasurementID%TYPE;
BEGIN
   IF _recursionlevel > 50 THEN
      RAISE EXCEPTION 'Maximum recursion exceeded';
   END IF;

   IF _recursionlevel = -1 THEN
      SELECT * INTO res FROM cpgdb.getSearchResultForID(_vsid);
      RETURN NEXT res;
      _recursionlevel := 1;
   END IF;


   OPEN ref FOR SELECT
      vs.VMeasurementID, o.Name, vs.Code,
      vs.Comments, vs.LastModifiedTimestamp
      FROM tblVMeasurement AS vs
         INNER JOIN tblVMeasurementGroup AS g ON g.VMeasurementID = vs.VMeasurementID
         INNER JOIN tlkpVMeasurementOp AS o ON o.VMeasurementOpID = vs.VMeasurementOpID
      WHERE g.MemberVMeasurementID = _vsid
      ORDER BY vs.LastModifiedTimestamp DESC;

   LOOP
      FETCH ref INTO VMeasurementID, res.Op, res.Code, res.Comments, res.Modified;

      IF NOT FOUND THEN
         EXIT;
      END IF;

      SELECT * INTO meta FROM cpgdb.GetMetaCache(VMeasurementID);
      IF FOUND THEN
         res.StartYear := meta.StartYear;
         res.ReadingCount := meta.ReadingCount;
         res.MeasurementCount := meta.MeasurementCount;
      END IF;

      res.RecursionLevel = _recursionlevel;
      res.VMeasurementID = VMeasurementID;
      RETURN NEXT res;
 
      FOR res IN SELECT * from cpgdb.recurseFindVMChildren(VMeasurementID, _recursionlevel + 1)
      LOOP
         RETURN NEXT res;
      END LOOP;
   END LOOP;
END;
$_$;
 :   DROP FUNCTION cpgdb.recursefindvmchildren(uuid, integer);
       cpgdb          postgres    false    7    2254                       1255    64763 #   recursefindvmparents(uuid, integer)    FUNCTION     �  CREATE FUNCTION cpgdb.recursefindvmparents(uuid, integer) RETURNS SETOF public.typvmeasurementsearchresult
    LANGUAGE plpgsql
    AS $_$
DECLARE
   _vsid ALIAS FOR $1;
   _recursionlevel INTEGER := $2;

   res typVMeasurementSearchResult;
   meta tblVMeasurementMetaCache%ROWTYPE;
   ref refcursor;

   VMeasurementID tblVMeasurement.VMeasurementID%TYPE;
BEGIN
   IF _recursionlevel > 50 THEN
      RAISE EXCEPTION 'Maximum recursion exceeded';
   END IF;

   IF _recursionlevel = -1 THEN
      SELECT * INTO res FROM cpgdb.getSearchResultForID(_vsid);
      RETURN NEXT res;
      _recursionlevel := 1;
   END IF;


   OPEN ref FOR SELECT
      vs.VMeasurementID, o.Name, vs.Code,
      vs.Comments, vs.LastModifiedTimestamp
      FROM tblVMeasurement as vs
         INNER JOIN tblVMeasurementGroup AS g ON g.MemberVMeasurementID = vs.VMeasurementID
         INNER JOIN tlkpVMeasurementOp AS o ON o.VMeasurementOpID = vs.VMeasurementOpID
      WHERE g.VMeasurementID = _vsid
      ORDER BY vs.LastModifiedTimestamp DESC;

   LOOP
      FETCH ref INTO VMeasurementID, res.Op, res.Code, res.Comments, res.Modified;

      IF NOT FOUND THEN
         EXIT;
      END IF;

      SELECT * INTO meta FROM cpgdb.GetMetaCache(VMeasurementID);
      IF FOUND THEN
         res.StartYear := meta.StartYear;
         res.ReadingCount := meta.ReadingCount;
         res.MeasurementCount := meta.MeasurementCount;
      END IF;

      res.RecursionLevel = _recursionlevel;
      res.VMeasurementID = VMeasurementID;
      RETURN NEXT res;

      IF res.Op = 'Direct' THEN
         CONTINUE;
      END IF;

      FOR res IN SELECT * from cpgdb.recurseFindVMParents(VMeasurementID, _recursionlevel + 1)
      LOOP
         RETURN NEXT res;
      END LOOP;
   END LOOP;
END;
$_$;
 9   DROP FUNCTION cpgdb.recursefindvmparents(uuid, integer);
       cpgdb          postgres    false    7    2254                       1255    64764 (   recursegetparentgroups(integer, integer)    FUNCTION     T  CREATE FUNCTION cpgdb.recursegetparentgroups(integer, integer) RETURNS SETOF public.tblsecuritygroup
    LANGUAGE plpgsql
    AS $_$
DECLARE
   _securityGroupID ALIAS FOR $1;
   _recursionLevel ALIAS FOR $2;

   res tblSecurityGroup%ROWTYPE;
   parentRes tblSecurityGroup%ROWTYPE;
BEGIN
   IF _recursionLevel > 50 THEN
      RAISE EXCEPTION 'Infinite recursion in getParentGroups!';
   END IF;

   FOR res IN SELECT g.* FROM tblSecurityGroup g
              INNER JOIN tblSecurityGroupMembership m ON m.parentSecurityGroupID = g.securityGroupID
              WHERE m.childSecurityGroupID = _securityGroupID and g.isActive = true
   LOOP
      RETURN NEXT res;

      FOR parentRes IN SELECT * FROM cpgdb.recurseGetParentGroups(res.securityGroupID, _recursionLevel + 1) 
      LOOP
         RETURN NEXT parentRes;
      END LOOP;
   END LOOP;
END;
$_$;
 >   DROP FUNCTION cpgdb.recursegetparentgroups(integer, integer);
       cpgdb          postgres    false    7    237            4           0    0 1   FUNCTION recursegetparentgroups(integer, integer)    ACL     T   GRANT ALL ON FUNCTION cpgdb.recursegetparentgroups(integer, integer) TO "Webgroup";
          cpgdb          postgres    false    1301                       1255    64765 )   removereadingnote(uuid, integer, integer)    FUNCTION     �  CREATE FUNCTION cpgdb.removereadingnote(uuid, integer, integer) RETURNS integer
    LANGUAGE plpgsql
    AS $_$
DECLARE
   _VMID ALIAS FOR $1;
   _RelYear ALIAS FOR $2;
   _ReadingNoteID ALIAS FOR $3;

   VMOp text;
   myReadingID tblReading.ReadingID%TYPE;
   ret integer;
BEGIN

   SELECT op.name INTO VMOp FROM tblVMeasurement 
      INNER JOIN tlkpVMeasurementOp op USING (vmeasurementOpID)
      WHERE vmeasurementID=_VMID;

   IF NOT FOUND THEN
      RAISE EXCEPTION 'Invalid/nonexistent vmeasurement %', _VMID;
   END IF;

   IF VMOp = 'Direct' THEN
      SELECT readingID INTO myReadingID 
         FROM tblVMeasurement 
         INNER JOIN tblMeasurement USING (measurementID) 
         INNER JOIN tblReading USING (measurementID) 
         WHERE VMeasurementID=_VMID AND RelYear=_RelYear;

      IF NOT FOUND THEN
         RAISE EXCEPTION 'No reading found for relyear % on vmeasurementid %. Year must exist.', _RelYear, _VMID;
      END IF;

      DELETE FROM tblReadingReadingNote WHERE ReadingID=myReadingID AND ReadingNoteID=_ReadingNoteID;
   ELSE
      DELETE FROM tblVMeasurementRelYearReadingNote WHERE VMeasurementID=_VMID AND RelYear=_RelYear AND ReadingNoteID=_ReadingNoteID;
   END IF;

   GET DIAGNOSTICS ret = ROW_COUNT;   
   RETURN ret;
END;
$_$;
 ?   DROP FUNCTION cpgdb.removereadingnote(uuid, integer, integer);
       cpgdb          postgres    false    7                       1255    64766 '   resultnotestojson(integer[], integer[])    FUNCTION     �  CREATE FUNCTION cpgdb.resultnotestojson(integer[], integer[]) RETURNS text
    LANGUAGE plpgsql STABLE
    AS $_$
DECLARE
   ids ALIAS FOR $1;
   counts ALIAS FOR $2;

   idx integer;
   id integer;
   count integer;

   outnote text[];
   v1 text;
   v2 text;
   v3 text;
   v4 text;
   v5 text; 

   note text;
   dbid integer;
   stdid varchar;
   vocname text;

BEGIN
   IF ids IS NULL OR counts IS NULL THEN 
      RETURN NULL;
   END IF;

   FOR idx in array_lower(ids, 1)..array_upper(ids, 1) LOOP
      id := ids[idx];
      count := counts[idx];

      SELECT replace(rnote.note, '"', E'\\"'),
	     rnote.readingnoteid,
             rnote.standardisedid,
             replace(voc.name, '"', E'\\"') 
          INTO note,dbid,stdid,vocname
          FROM tlkpReadingNote rnote
          INNER JOIN tlkpVocabulary voc ON rnote.vocabularyid=voc.vocabularyid
          WHERE rnote.readingnoteid=id;

      IF NOT FOUND THEN
         RAISE NOTICE 'Reading note id % not found', id;
         CONTINUE;
      END IF;

      v1 := '"note":"' || note || '"';



      IF stdid IS NOT NULL THEN
         v2 := '"stdid":"' || stdid || '"';
      ELSE
         v2 := '"stdid":null';
      END IF;

      v3 := '"std":"' || vocname || '"';

      v4 := '"icnt":' || count;

      v5 := '"dbid":' || dbid;

      outnote := array_append(outnote, '{' || array_to_string(ARRAY[v1, v2, v3, v4, v5], ',') || '}');
   END LOOP;

   return '[' || array_to_string(outnote, ',') || ']';
END;
$_$;
 =   DROP FUNCTION cpgdb.resultnotestojson(integer[], integer[]);
       cpgdb          postgres    false    7                       1255    64767    text_to_uuid(text)    FUNCTION     k   CREATE FUNCTION cpgdb.text_to_uuid(text) RETURNS uuid
    LANGUAGE sql
    AS $_$
   SELECT $1::uuid;
$_$;
 (   DROP FUNCTION cpgdb.text_to_uuid(text);
       cpgdb          postgres    false    7                       1255    64768    tloid(public.tblelement)    FUNCTION     �   CREATE FUNCTION cpgdb.tloid(public.tblelement) RETURNS uuid
    LANGUAGE sql
    AS $_$
select objectid from cpgdb.findobjecttoplevelancestor($1.objectid);
$_$;
 .   DROP FUNCTION cpgdb.tloid(public.tblelement);
       cpgdb          postgres    false    7    236                       1255    64769    update_allobjectextents()    FUNCTION     W  CREATE FUNCTION cpgdb.update_allobjectextents() RETURNS boolean
    LANGUAGE plpgsql
    AS $$DECLARE
objectres tblobject%ROWTYPE;
BEGIN

  UPDATE tblobject set objectextent=NULL;

  FOR objectres in SELECT tblobject.* from tblobject LOOP
     PERFORM cpgdb.update_objectextentbyobject(objectres.objectid);
  END LOOP;
  return true;

END;$$;
 /   DROP FUNCTION cpgdb.update_allobjectextents();
       cpgdb          postgres    false    7                       1255    64770    update_allobjectregions()    FUNCTION     t  CREATE FUNCTION cpgdb.update_allobjectregions() RETURNS boolean
    LANGUAGE plpgsql
    AS $$DECLARE
objectres tblobject%ROWTYPE;
BEGIN

  DELETE FROM tblobjectregion;

  FOR objectres in SELECT tblobject.* from tblobject where tblobject.location is not null LOOP
     PERFORM cpgdb.update_objectregions(objectres.objectid, 'object');
  END LOOP;
  return true;

END;$$;
 /   DROP FUNCTION cpgdb.update_allobjectregions();
       cpgdb          postgres    false    7                       1255    64771 $   update_objectextentbyobject(integer)    FUNCTION     D  CREATE FUNCTION cpgdb.update_objectextentbyobject(theobjectid integer) RETURNS public.geometry
    LANGUAGE plpgsql
    AS $_$DECLARE
locationtypeid INT;
myrec RECORD;
myobjectid ALIAS FOR $1;
regionres tblregion%ROWTYPE;
myquery VARCHAR;

BEGIN

    SELECT INTO myrec locationtypeid 
    FROM tblobject 
    WHERE objectid=myobjectid;
    IF myrec != 6 THEN
	RETURN NULL;
    END IF;

    SELECT INTO myrec setsrid(extent(tblelement.location)::geometry,4326) as myextent, area(setsrid(extent(tblelement.location)::geometry,4326)) as myextentarea, tblobject.objectid
    FROM tblelement, tblsubobject, tblobject
    WHERE tblelement.subobjectid=tblsubobject.subobjectid
    AND tblsubobject.objectid=tblobject.objectid
    AND tblobject.objectid=myobjectid 
    GROUP BY tblobject.objectid;

    UPDATE tblobject SET location=myrec.myextent where tblobject.objectid=myobjectid;

    IF myrec.myextentarea = 0 THEN
        RAISE NOTICE 'Kludge alert!: Extent area is zero so expanding a bit'; 
        myquery := 'SELECT tblregion.* FROM tblregion, tblobject WHERE intersects(expand(tblobject.location,0.00001), tblregion.the_geom) AND tblobject.objectid=' || myobjectid;
    ELSE
        myquery := 'SELECT tblregion.* FROM tblregion, tblobject WHERE intersects(tblobject.location, tblregion.the_geom) AND tblobject.objectid=' || myobjectid;
    END IF;

  FOR regionres IN EXECUTE myquery LOOP
        DELETE FROM tblobjectregion where objectid=myobjectid;
        INSERT INTO tblobjectregion (objectid, regionid) values (myobjectid, regionres.regionid);
  END LOOP;


  RETURN myrec.myextent;
END;$_$;
 F   DROP FUNCTION cpgdb.update_objectextentbyobject(theobjectid integer);
       cpgdb          postgres    false    7    3    3    3    3    3    3    3    3            5           0    0 9   FUNCTION update_objectextentbyobject(theobjectid integer)    ACL     \   GRANT ALL ON FUNCTION cpgdb.update_objectextentbyobject(theobjectid integer) TO "Webgroup";
          cpgdb          postgres    false    1308                       1255    64772 '   update_objectextentbysubobject(integer)    FUNCTION     �  CREATE FUNCTION cpgdb.update_objectextentbysubobject(subobjectid integer) RETURNS public.geometry
    LANGUAGE plpgsql
    AS $_$DECLARE
mysubobjectid ALIAS FOR $1;
myobjectid INTEGER;
mygeometry geometry;
BEGIN
   SELECT INTO myobjectid tblsubobject.objectid FROM tblsubobject WHERE tblsubobject.subobjectid=mysubobjectid;
   SELECT INTO mygeometry cpgdb.update_objectextentbyobject(myobjectid);
   RETURN mygeometry;
END;$_$;
 I   DROP FUNCTION cpgdb.update_objectextentbysubobject(subobjectid integer);
       cpgdb          postgres    false    7    3    3    3    3    3    3    3    3            6           0    0 <   FUNCTION update_objectextentbysubobject(subobjectid integer)    ACL     _   GRANT ALL ON FUNCTION cpgdb.update_objectextentbysubobject(subobjectid integer) TO "Webgroup";
          cpgdb          postgres    false    1309                       1255    64773 0   update_objectregions(integer, character varying)    FUNCTION     �  CREATE FUNCTION cpgdb.update_objectregions(pkeyid integer, type character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $_$DECLARE
regionres tblregion%ROWTYPE;
objectres tblobject%ROWTYPE;
pkeyid ALIAS for $1;
pkeytype ALIAS for $2;

BEGIN

  RAISE NOTICE 'Site is %', pkeyid;

  IF pkeytype = 'object' THEN
          DELETE FROM tblobjectregion where objectid=pkeyid;

          FOR regionres IN SELECT tblregion.* from tblregion, tblobject where intersects(tblobject.location, tblregion.the_geom) and tblobject.objectid=pkeyid LOOP 

                INSERT INTO tblobjectregion (objectid, regionid) values (pkeyid, regionres.regionid);
          END LOOP;
          return true; 
  END IF;     

  IF pkeytype = 'region' THEN
          DELETE FROM tblobjectregion where regionid=pkeyid;

          FOR objectres IN SELECT tblobject.* from tblregion, tblobject where intersects(tblobject.location, tblregion.the_geom) and tblregion.regionid=pkeyid LOOP 
 
                INSERT INTO tblobjectregion (objectid, regionid) values (objectres.objectid, pkeyid);
          END LOOP;
          return true;  
  END IF;

  RAISE EXCEPTION 'object type not recognised';
  return false;

END;$_$;
 R   DROP FUNCTION cpgdb.update_objectregions(pkeyid integer, type character varying);
       cpgdb          postgres    false    7                       1255    64774    updatelocs()    FUNCTION     �  CREATE FUNCTION cpgdb.updatelocs() RETURNS void
    LANGUAGE plpgsql
    AS $$DECLARE
gisrow yenikapi;
thecode character varying;
BEGIN
FOR gisrow IN SELECT * FROM yenikapi where sitecode='YMT' LOOP
   thecode = trim(both to_char(gisrow.samplecode, '9999'));
   UPDATE tblelement SET locationgeometry=gisrow.the_geom WHERE code=thecode
     AND objectid IN (SELECT objectid FROM cpgdb.findobjectdescendantsfromcode('YMT', true));
END LOOP;
END;$$;
 "   DROP FUNCTION cpgdb.updatelocs();
       cpgdb          postgres    false    7                        1255    64775 '   verifysumsarecontiguousanddated(uuid[])    FUNCTION     �  CREATE FUNCTION cpgdb.verifysumsarecontiguousanddated(uuid[]) RETURNS boolean
    LANGUAGE plpgsql
    AS $_$
DECLARE
   Constituents ALIAS FOR $1;
   CVMId INTEGER;

   SumStart INTEGER;
   SumEnd INTEGER;

   CStart INTEGER;
   CEnd INTEGER;
   VMID tblVMeasurement.VMeasurementID%TYPE;

   CurDatingTypeID INTEGER;
   
   CurDatingClass DatingTypeClass;
   LastDatingClass DatingTypeClass;
BEGIN
   FOR CVMId IN array_lower(Constituents, 1)..array_upper(Constituents,1) LOOP
      SELECT VMeasurementID INTO VMID FROM tblVMeasurement WHERE VMeasurementID=Constituents[CVMId];

      IF NOT FOUND THEN
         RAISE EXCEPTION 'Sum constituent does not exist (id:%)', Constituents[CVMId];
      END IF;
      
      SELECT StartYear,StartYear+ReadingCount,DatingClass INTO CStart, CEnd, CurDatingClass from cpgdb.getMetaCache(Constituents[CVMId])
             LEFT JOIN tlkpDatingType USING (datingTypeID);
      
      IF LastDatingClass IS NOT NULL AND CurDatingClass <> LastDatingClass THEN
         RAISE EXCEPTION 'Sum contains elements from different dating classes, which is not valid (%, %)', CurDatingClass, LastDatingClass;
      ELSE
         LastDatingClass := CurDatingClass;
      END IF;

      IF SumStart IS NULL THEN
         SumStart := CStart;
         SumEnd := CEnd;
      ELSE
         IF CStart > SumEnd OR CEnd < SumStart THEN
            RAISE EXCEPTION 'Sum constituents are not contiguous [(%,%) is not in (%, %)]', CStart, CEnd, SumStart, SumEnd;
         END IF;

         IF CStart < SumStart THEN
            SumStart := CStart;
         END IF;

         IF CEnd > SumEnd THEN
            SumEnd := CEnd;
         END IF;
      END IF;
   END LOOP;

   RETURN TRUE;
END;
$_$;
 =   DROP FUNCTION cpgdb.verifysumsarecontiguousanddated(uuid[]);
       cpgdb          postgres    false    7            !           1255    64776 "   vmeasurementmodifiedcachetrigger()    FUNCTION     I  CREATE FUNCTION cpgdb.vmeasurementmodifiedcachetrigger() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN

   PERFORM cpgdb.createmetacache(NEW.VMeasurementID);
   RETURN NEW;
EXCEPTION
   WHEN internal_error THEN
      RAISE NOTICE 'WARNING: Failed to create metacache for %', NEW.VMeasurementID;
      RETURN NEW;
END;
$$;
 8   DROP FUNCTION cpgdb.vmeasurementmodifiedcachetrigger();
       cpgdb          postgres    false    7            7           0    0 +   FUNCTION vmeasurementmodifiedcachetrigger()    ACL     N   GRANT ALL ON FUNCTION cpgdb.vmeasurementmodifiedcachetrigger() TO "Webgroup";
          cpgdb          postgres    false    1313            "           1255    64777    vmeasurementmodifiedtrigger()    FUNCTION     �  CREATE FUNCTION cpgdb.vmeasurementmodifiedtrigger() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
   IF NEW.isGenerating = TRUE THEN
      RETURN NEW;
   END IF;

   NEW.lastModifiedTimestamp = current_timestamp;

   PERFORM cpgdb.CreateMetaCache(NEW.VMeasurementID);

   RETURN NEW;
EXCEPTION
   WHEN internal_error THEN
      RAISE NOTICE 'WARNING: Failed to create metacache for %', NEW.VMeasurementID;
      RETURN NEW;
END;
$$;
 3   DROP FUNCTION cpgdb.vmeasurementmodifiedtrigger();
       cpgdb          postgres    false    7            #           1255    64778     vmeasurementrelyearnotetrigger()    FUNCTION     z  CREATE FUNCTION cpgdb.vmeasurementrelyearnotetrigger() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
   OpName text;
BEGIN
   SELECT op.name INTO OpName
      FROM tblVMeasurement 
      INNER JOIN tlkpvmeasurementop op USING(VMeasurementOpID)
      WHERE VMeasurementID=NEW.VMeasurementID;

   IF NOT FOUND THEN
      RAISE EXCEPTION 'Invalid or nonexistent VMeasurement %. Cannot attach reading notes.', NEW.VMeasurementID;
   END IF;

   IF OpName = 'Direct' THEN
      RAISE EXCEPTION 'Cannot attach RelYearReadingNotes to a direct VMeasurement. Attach directly to readings instead.';
   END IF;

   RETURN NEW;
END;
$$;
 6   DROP FUNCTION cpgdb.vmeasurementrelyearnotetrigger();
       cpgdb          postgres    false    7            $           1255    64779 0   applyderivedreadingnotes(uuid, uuid, uuid, uuid)    FUNCTION     F  CREATE FUNCTION cpgdbj.applyderivedreadingnotes(uuid, uuid, uuid, uuid) RETURNS void
    LANGUAGE plpgsql
    AS $_$
DECLARE
	inVMeasurementID ALIAS FOR $1;
	inVMeasurementResultID ALIAS FOR $2;
	oldVMeasurementResultID ALIAS FOR $3;
	inVMeasurementResultGroupID ALIAS FOR $4;

	curs refcursor;
	vmrn tblVMeasurementRelYearReadingNote%ROWTYPE;
BEGIN

	IF inVMeasurementResultGroupID IS NOT NULL THEN
		-- SUM
		-- Aggregate everything in this group, setting derivedCount properly
		
		INSERT INTO tblVMeasurementReadingNoteResult ( VMeasurementResultID, RelYear, ReadingNoteID, InheritedCount )
		SELECT inVMeasurementResultID AS VMeasurementResultID, RelYear, ReadingNoteID, COUNT(note.VMeasurementResultID)
			FROM tblVMeasurementResult res
			INNER JOIN tblVMeasurementReadingNoteResult note ON res.VMeasurementResultID=note.VMeasurementResultID
			WHERE VMeasurementResultGroupID=inVMeasurementResultGroupID
			GROUP BY note.RelYear, note.ReadingNoteID;
			
	ELSIF oldVMeasurementResultID IS NOT NULL THEN
		-- INDEX
	    -- Steal the notes from our child VMeasurement
		
		UPDATE tblVMeasurementReadingNoteResult SET inheritedCount=1,VMeasurementResultID=inVMeasurementResultID WHERE VMeasurementResultID=oldVMeasurementResultID;

	ELSE
		-- CLEAN, REDATE, CROSSDATE
		-- We work in place, so just mark everything as inherited
		UPDATE tblVMeasurementReadingNoteResult SET inheritedCount=1 WHERE VMeasurementResultID=inVMeasurementResultID;
		
	END IF;

	-- Now, find any directly applied "derived ring notes"
	FOR vmrn IN SELECT * FROM tblVMeasurementRelYearReadingNote
		WHERE VMeasurementID=inVMeasurementID
	LOOP
		-- Delete any notes that already exist - we're overriding!
		DELETE FROM tblVMeasurementReadingNoteResult WHERE VMeasurementResultID=inVMeasurementResultID AND RelYear=vmrn.RelYear;
	
		-- If we're not overriding for the sake of deleting, insert our notes
		IF vmrn.disabledOverride = FALSE THEN
			INSERT INTO tblVMeasurementReadingNoteResult (VMeasurementResultID, RelYear, ReadingNoteID) VALUES 
				(inVMeasurementResultID, vmrn.RelYear, vmrn.ReadingNoteID);
		END IF;
	END LOOP;
END;
$_$;
 G   DROP FUNCTION cpgdbj.applyderivedreadingnotes(uuid, uuid, uuid, uuid);
       cpgdbj          postgres    false    11            %           1255    64780 #   qacqvmeasurementreadingresult(uuid)    FUNCTION     <  CREATE FUNCTION cpgdbj.qacqvmeasurementreadingresult(paramcurrentvmeasurementresultid uuid, OUT relyear integer, OUT reading integer) RETURNS SETOF record
    LANGUAGE sql STABLE
    AS $_$

  SELECT RelYear, Reading from tblVMeasurementReadingResult 
   WHERE VMeasurementResultID=$1 
   ORDER BY RelYear ASC

$_$;
 �   DROP FUNCTION cpgdbj.qacqvmeasurementreadingresult(paramcurrentvmeasurementresultid uuid, OUT relyear integer, OUT reading integer);
       cpgdbj          postgres    false    11            8           0    0 w   FUNCTION qacqvmeasurementreadingresult(paramcurrentvmeasurementresultid uuid, OUT relyear integer, OUT reading integer)    ACL     �   GRANT ALL ON FUNCTION cpgdbj.qacqvmeasurementreadingresult(paramcurrentvmeasurementresultid uuid, OUT relyear integer, OUT reading integer) TO "Webgroup";
          cpgdbj          postgres    false    1317            &           1255    64781 '   qappvmeasurementreadingnoteresult(uuid)    FUNCTION     �  CREATE FUNCTION cpgdbj.qappvmeasurementreadingnoteresult(paramvmeasurementresultid uuid) RETURNS void
    LANGUAGE sql
    AS $_$

  INSERT INTO tblVMeasurementReadingNoteResult ( VMeasurementResultID, RelYear, ReadingNoteID ) 
  SELECT vmrr.VMeasurementResultID, vmrr.RelYear, rrn.ReadingNoteID 
   FROM tblVMeasurementReadingResult vmrr 
   INNER JOIN tblReadingReadingNote rrn 
      ON vmrr.readingID=rrn.readingID 
   WHERE vmrr.vMeasurementResultID=$1;

$_$;
 X   DROP FUNCTION cpgdbj.qappvmeasurementreadingnoteresult(paramvmeasurementresultid uuid);
       cpgdbj          postgres    false    11            '           1255    64782 ,   qappvmeasurementreadingresult(uuid, integer)    FUNCTION     �  CREATE FUNCTION cpgdbj.qappvmeasurementreadingresult(paramvmeasurementresultid uuid, parammeasurementid integer) RETURNS void
    LANGUAGE sql
    AS $_$

  INSERT INTO tblVMeasurementReadingResult ( RelYear, Reading, VMeasurementResultID, ReadingID, ewwidth, lwwidth ) 
  SELECT tblReading.RelYear, tblReading.Reading, $1 AS Expr1, tblReading.readingID, tblReading.ewwidth, tblReading.lwwidth
   FROM tblReading 
   WHERE tblReading.MeasurementID=$2

$_$;
 p   DROP FUNCTION cpgdbj.qappvmeasurementreadingresult(paramvmeasurementresultid uuid, parammeasurementid integer);
       cpgdbj          postgres    false    11            9           0    0 b   FUNCTION qappvmeasurementreadingresult(paramvmeasurementresultid uuid, parammeasurementid integer)    ACL     �   GRANT ALL ON FUNCTION cpgdbj.qappvmeasurementreadingresult(paramvmeasurementresultid uuid, parammeasurementid integer) TO "Webgroup";
          cpgdbj          postgres    false    1319            (           1255    64783 @   qappvmeasurementresult(uuid, uuid, uuid, uuid, integer, integer)    FUNCTION       CREATE FUNCTION cpgdbj.qappvmeasurementresult(paramvmeasurementresultid uuid, paramvmeasurementid uuid, paramvmeasurementresultgroupid uuid, paramvmeasurementresultmasterid uuid, owneruserid integer, parammeasurementid integer) RETURNS void
    LANGUAGE sql
    AS $_$

  INSERT INTO tblVMeasurementResult ( VMeasurementResultID, VMeasurementID, RadiusID, IsReconciled, StartYear, DatingTypeID, DatingErrorPositive, DatingErrorNegative, IsLegacyCleaned, CreatedTimestamp, LastModifiedTimestamp, VMeasurementResultGroupID, VMeasurementResultMasterID, OwnerUserID, Code, Comments, isPublished ) 
  SELECT $1 AS Expr1, $2 AS Expr2, m.RadiusID, m.IsReconciled, m.StartYear, m.DatingTypeID, m.DatingErrorPositive, m.DatingErrorNegative, m.IsLegacyCleaned, vm.CreatedTimestamp, vm.LastModifiedTimestamp, $3 AS Expr5, $4 AS Expr6, $5 AS Expr7, vm.code, vm.comments, vm.isPublished 
   FROM tblMeasurement m 
   INNER JOIN tblVMeasurement AS vm 
      ON vm.MeasurementID = m.MeasurementID 
   WHERE m.MeasurementID=$6 and vm.VMeasurementID=$2

$_$;
 �   DROP FUNCTION cpgdbj.qappvmeasurementresult(paramvmeasurementresultid uuid, paramvmeasurementid uuid, paramvmeasurementresultgroupid uuid, paramvmeasurementresultmasterid uuid, owneruserid integer, parammeasurementid integer);
       cpgdbj          postgres    false    11            :           0    0 �   FUNCTION qappvmeasurementresult(paramvmeasurementresultid uuid, paramvmeasurementid uuid, paramvmeasurementresultgroupid uuid, paramvmeasurementresultmasterid uuid, owneruserid integer, parammeasurementid integer)    ACL     �   GRANT ALL ON FUNCTION cpgdbj.qappvmeasurementresult(paramvmeasurementresultid uuid, paramvmeasurementid uuid, paramvmeasurementresultgroupid uuid, paramvmeasurementresultmasterid uuid, owneruserid integer, parammeasurementid integer) TO "Webgroup";
          cpgdbj          postgres    false    1320            )           1255    64784 >   qappvmeasurementresultopindex(uuid, uuid, uuid, integer, uuid)    FUNCTION     �  CREATE FUNCTION cpgdbj.qappvmeasurementresultopindex(paramnewvmeasurementresultid uuid, paramvmeasurementid uuid, paramvmeasurementresultmasterid uuid, owneruserid integer, paramcurrentvmeasurementresultid uuid) RETURNS void
    LANGUAGE sql
    AS $_$

  INSERT INTO tblVMeasurementResult ( VMeasurementResultID, VMeasurementID, RadiusID, IsReconciled, StartYear, DatingTypeID, DatingErrorPositive, DatingErrorNegative, IsLegacyCleaned, CreatedTimestamp, LastModifiedTimestamp, VMeasurementResultMasterID, OwnerUserID, Code, Comments, isPublished ) 
  SELECT $1 AS Expr1, $2 AS Expr2, r.RadiusID, r.IsReconciled, r.StartYear, r.DatingTypeID, r.DatingErrorPositive, r.DatingErrorNegative, r.IsLegacyCleaned, v.CreatedTimestamp, v.LastModifiedTimestamp, $3 AS Expr5, $4 AS Expr6, v.Code, v.Comments, v.isPublished 
   FROM tblVMeasurementResult r 
   INNER JOIN tblVMeasurement AS v 
      ON v.VMeasurementID = r.VMeasurementID 
   WHERE r.VMeasurementResultID=$5

$_$;
 �   DROP FUNCTION cpgdbj.qappvmeasurementresultopindex(paramnewvmeasurementresultid uuid, paramvmeasurementid uuid, paramvmeasurementresultmasterid uuid, owneruserid integer, paramcurrentvmeasurementresultid uuid);
       cpgdbj          postgres    false    11            ;           0    0 �   FUNCTION qappvmeasurementresultopindex(paramnewvmeasurementresultid uuid, paramvmeasurementid uuid, paramvmeasurementresultmasterid uuid, owneruserid integer, paramcurrentvmeasurementresultid uuid)    ACL     �   GRANT ALL ON FUNCTION cpgdbj.qappvmeasurementresultopindex(paramnewvmeasurementresultid uuid, paramvmeasurementid uuid, paramvmeasurementresultmasterid uuid, owneruserid integer, paramcurrentvmeasurementresultid uuid) TO "Webgroup";
          cpgdbj          postgres    false    1321            *           1255    64785 <   qappvmeasurementresultopsum(uuid, uuid, uuid, integer, uuid)    FUNCTION     �  CREATE FUNCTION cpgdbj.qappvmeasurementresultopsum(paramnewvmeasurementresultid uuid, paramvmeasurementid uuid, paramvmeasurementresultmasterid uuid, owneruserid integer, paramvmeasurementresultgroupid uuid) RETURNS void
    LANGUAGE sql
    AS $_$

  INSERT INTO tblVMeasurementResult ( VMeasurementResultID, VMeasurementID, StartYear, DatingTypeID, CreatedTimestamp, LastModifiedTimestamp, VMeasurementResultMasterID, OwnerUserID, Code) 
  SELECT $1 AS Expr1, $2 AS Expr2, Min(r.StartYear) AS MinOfStartYear, Max(r.DatingTypeID) AS MaxOfDatingTypeID, now() AS CreatedTimestamp, now() AS LastModifiedTimestamp, $3 AS Expr5, $4 AS Expr6, 'SUM' AS Code 
   FROM tblVMeasurementResult r 
   WHERE r.VMeasurementResultGroupID=$5

$_$;
 �   DROP FUNCTION cpgdbj.qappvmeasurementresultopsum(paramnewvmeasurementresultid uuid, paramvmeasurementid uuid, paramvmeasurementresultmasterid uuid, owneruserid integer, paramvmeasurementresultgroupid uuid);
       cpgdbj          postgres    false    11            <           0    0 �   FUNCTION qappvmeasurementresultopsum(paramnewvmeasurementresultid uuid, paramvmeasurementid uuid, paramvmeasurementresultmasterid uuid, owneruserid integer, paramvmeasurementresultgroupid uuid)    ACL     �   GRANT ALL ON FUNCTION cpgdbj.qappvmeasurementresultopsum(paramnewvmeasurementresultid uuid, paramvmeasurementid uuid, paramvmeasurementresultmasterid uuid, owneruserid integer, paramvmeasurementresultgroupid uuid) TO "Webgroup";
          cpgdbj          postgres    false    1322            +           1255    64786 .   qappvmeasurementresultreadingopsum(uuid, uuid)    FUNCTION     �   CREATE FUNCTION cpgdbj.qappvmeasurementresultreadingopsum(paramnewvmeasurementresultgroupid uuid, paramnewvmeasurementresultid uuid) RETURNS integer
    LANGUAGE sql
    AS $_$

  SELECT * from cpgdb.qappVMeasurementResultReadingOpSum($1, $2)

$_$;
 �   DROP FUNCTION cpgdbj.qappvmeasurementresultreadingopsum(paramnewvmeasurementresultgroupid uuid, paramnewvmeasurementresultid uuid);
       cpgdbj          postgres    false    11            =           0    0 v   FUNCTION qappvmeasurementresultreadingopsum(paramnewvmeasurementresultgroupid uuid, paramnewvmeasurementresultid uuid)    ACL     �   GRANT ALL ON FUNCTION cpgdbj.qappvmeasurementresultreadingopsum(paramnewvmeasurementresultgroupid uuid, paramnewvmeasurementresultid uuid) TO "Webgroup";
          cpgdbj          postgres    false    1323            ,           1255    64787 0   qdelvmeasurementresultremovemasterid(uuid, uuid)    FUNCTION       CREATE FUNCTION cpgdbj.qdelvmeasurementresultremovemasterid(paramvmeasurementresultmasterid uuid, paramvmeasurementresultid uuid) RETURNS void
    LANGUAGE sql
    AS $_$

  DELETE 
   FROM tblVMeasurementResult 
   WHERE VMeasurementResultMasterID=$1 AND VMeasurementResultID<>$2

$_$;
 �   DROP FUNCTION cpgdbj.qdelvmeasurementresultremovemasterid(paramvmeasurementresultmasterid uuid, paramvmeasurementresultid uuid);
       cpgdbj          postgres    false    11            >           0    0 s   FUNCTION qdelvmeasurementresultremovemasterid(paramvmeasurementresultmasterid uuid, paramvmeasurementresultid uuid)    ACL     �   GRANT ALL ON FUNCTION cpgdbj.qdelvmeasurementresultremovemasterid(paramvmeasurementresultmasterid uuid, paramvmeasurementresultid uuid) TO "Webgroup";
          cpgdbj          postgres    false    1324            -           1255    64788    qryvmeasurementmembers(uuid)    FUNCTION     �  CREATE FUNCTION cpgdbj.qryvmeasurementmembers(paramvmeasurementid uuid, OUT vmeasurementid uuid, OUT membervmeasurementid uuid) RETURNS SETOF record
    LANGUAGE sql STABLE
    AS $_$

  SELECT tblVMeasurement.VMeasurementID, tblVMeasurementGroup.MemberVMeasurementID 
   FROM tblVMeasurement 
   INNER JOIN tblVMeasurementGroup 
      ON tblVMeasurement.VMeasurementID = tblVMeasurementGroup.VMeasurementID 
   WHERE tblVMeasurement.VMeasurementID=$1

$_$;
    DROP FUNCTION cpgdbj.qryvmeasurementmembers(paramvmeasurementid uuid, OUT vmeasurementid uuid, OUT membervmeasurementid uuid);
       cpgdbj          postgres    false    11            ?           0    0 q   FUNCTION qryvmeasurementmembers(paramvmeasurementid uuid, OUT vmeasurementid uuid, OUT membervmeasurementid uuid)    ACL     �   GRANT ALL ON FUNCTION cpgdbj.qryvmeasurementmembers(paramvmeasurementid uuid, OUT vmeasurementid uuid, OUT membervmeasurementid uuid) TO "Webgroup";
          cpgdbj          postgres    false    1325            .           1255    64789    qryvmeasurementtype(uuid)    FUNCTION     m  CREATE FUNCTION cpgdbj.qryvmeasurementtype(vmeasurementid uuid, OUT vmeasurementid uuid, OUT op text, OUT vmeasurementsingroup bigint, OUT measurementid integer, OUT vmeasurementopparameter integer) RETURNS record
    LANGUAGE sql STABLE
    AS $_$

  SELECT tblVMeasurement.VMeasurementID, First(tlkpVMeasurementOp.Name) AS Op, Count(tblVMeasurementGroup.VMeasurementID) AS VMeasurementsInGroup, First(tblVMeasurement.MeasurementID) AS MeasurementID, First(tblVMeasurement.VMeasurementOpParameter) AS VMeasurementOpParameter 
   FROM tlkpVMeasurementOp 
   INNER JOIN (tblVMeasurement 
   LEFT JOIN tblVMeasurementGroup 
      ON tblVMeasurement.VMeasurementID = tblVMeasurementGroup.VMeasurementID) 
      ON tlkpVMeasurementOp.VMeasurementOpID = tblVMeasurement.VMeasurementOpID 
   GROUP BY tblVMeasurement.VMeasurementID 
   HAVING tblVMeasurement.VMeasurementID=$1

$_$;
 �   DROP FUNCTION cpgdbj.qryvmeasurementtype(vmeasurementid uuid, OUT vmeasurementid uuid, OUT op text, OUT vmeasurementsingroup bigint, OUT measurementid integer, OUT vmeasurementopparameter integer);
       cpgdbj          postgres    false    11            @           0    0 �   FUNCTION qryvmeasurementtype(vmeasurementid uuid, OUT vmeasurementid uuid, OUT op text, OUT vmeasurementsingroup bigint, OUT measurementid integer, OUT vmeasurementopparameter integer)    ACL     �   GRANT ALL ON FUNCTION cpgdbj.qryvmeasurementtype(vmeasurementid uuid, OUT vmeasurementid uuid, OUT op text, OUT vmeasurementsingroup bigint, OUT measurementid integer, OUT vmeasurementopparameter integer) TO "Webgroup";
          cpgdbj          postgres    false    1326            /           1255    64790 /   qupdvmeasurementresultattachgroupid(uuid, uuid)    FUNCTION       CREATE FUNCTION cpgdbj.qupdvmeasurementresultattachgroupid(paramvmeasurementresultgroupid uuid, paramvmeasurementresultid uuid) RETURNS void
    LANGUAGE sql
    AS $_$

  UPDATE tblVMeasurementResult 
   SET VMeasurementResultGroupID=$1 
   WHERE VMeasurementResultID=$2

$_$;
    DROP FUNCTION cpgdbj.qupdvmeasurementresultattachgroupid(paramvmeasurementresultgroupid uuid, paramvmeasurementresultid uuid);
       cpgdbj          postgres    false    11            A           0    0 q   FUNCTION qupdvmeasurementresultattachgroupid(paramvmeasurementresultgroupid uuid, paramvmeasurementresultid uuid)    ACL     �   GRANT ALL ON FUNCTION cpgdbj.qupdvmeasurementresultattachgroupid(paramvmeasurementresultgroupid uuid, paramvmeasurementresultid uuid) TO "Webgroup";
          cpgdbj          postgres    false    1327            0           1255    64791 (   qupdvmeasurementresultcleargroupid(uuid)    FUNCTION     �   CREATE FUNCTION cpgdbj.qupdvmeasurementresultcleargroupid(paramvmeasurementresultgroupid uuid) RETURNS void
    LANGUAGE sql
    AS $_$

  UPDATE tblVMeasurementResult 
   SET VMeasurementResultGroupID=NULL 
   WHERE VMeasurementResultGroupID=$1

$_$;
 ^   DROP FUNCTION cpgdbj.qupdvmeasurementresultcleargroupid(paramvmeasurementresultgroupid uuid);
       cpgdbj          postgres    false    11            B           0    0 P   FUNCTION qupdvmeasurementresultcleargroupid(paramvmeasurementresultgroupid uuid)    ACL     t   GRANT ALL ON FUNCTION cpgdbj.qupdvmeasurementresultcleargroupid(paramvmeasurementresultgroupid uuid) TO "Webgroup";
          cpgdbj          postgres    false    1328            1           1255    64792     qupdvmeasurementresultinfo(uuid)    FUNCTION     �  CREATE FUNCTION cpgdbj.qupdvmeasurementresultinfo(uuid) RETURNS boolean
    LANGUAGE plpgsql
    AS $_$
DECLARE
   ResultID ALIAS FOR $1;
   vmid tblVMeasurement.VMeasurementID%TYPE;
   c tblVMeasurement.Code%TYPE;
   comm tblVMeasurement.Comments%TYPE;
   ispub tblVMeasurement.isPublished%TYPE;
   createTS tblVMeasurement.CreatedTimestamp%TYPE;
   modTS tblVMeasurement.LastModifiedTimestamp%TYPE;
BEGIN
   SELECT VMeasurementID INTO vmid FROM tblVMeasurementResult WHERE VMeasurementResultID = ResultID;

   IF NOT FOUND THEN
      RETURN FALSE;
   END IF;

   SELECT Code, Comments, isPublished, CreatedTimestamp, LastModifiedTimestamp
     INTO c, comm, ispub, createTS, modTS
     FROM tblVMeasurement WHERE VMeasurementID = vmid;

   IF NOT FOUND THEN
      RETURN FALSE;
   END IF;

   UPDATE tblVMeasurementResult SET (Code, Comments, isPublished, CreatedTimestamp, LastModifiedTimestamp) =
      (c, comm, ispub, createTS, modTS) 
      WHERE VMeasurementResultID = ResultID;

   RETURN TRUE;
END;
$_$;
 7   DROP FUNCTION cpgdbj.qupdvmeasurementresultinfo(uuid);
       cpgdbj          postgres    false    11            C           0    0 )   FUNCTION qupdvmeasurementresultinfo(uuid)    ACL     M   GRANT ALL ON FUNCTION cpgdbj.qupdvmeasurementresultinfo(uuid) TO "Webgroup";
          cpgdbj          postgres    false    1329            2           1255    64793 )   qupdvmeasurementresultopclean(uuid, uuid)    FUNCTION       CREATE FUNCTION cpgdbj.qupdvmeasurementresultopclean(paramvmeasurementid uuid, paramcurrentvmeasurementresultid uuid) RETURNS void
    LANGUAGE sql
    AS $_$

  UPDATE tblVMeasurementResult 
   SET VMeasurementID=$1 
   WHERE VMeasurementResultID=$2

$_$;
 u   DROP FUNCTION cpgdbj.qupdvmeasurementresultopclean(paramvmeasurementid uuid, paramcurrentvmeasurementresultid uuid);
       cpgdbj          postgres    false    11            D           0    0 g   FUNCTION qupdvmeasurementresultopclean(paramvmeasurementid uuid, paramcurrentvmeasurementresultid uuid)    ACL     �   GRANT ALL ON FUNCTION cpgdbj.qupdvmeasurementresultopclean(paramvmeasurementid uuid, paramcurrentvmeasurementresultid uuid) TO "Webgroup";
          cpgdbj          postgres    false    1330            3           1255    64794 -   qupdvmeasurementresultopcrossdate(uuid, uuid)    FUNCTION     �  CREATE FUNCTION cpgdbj.qupdvmeasurementresultopcrossdate(paramvmeasurementid uuid, paramcurrentvmeasurementresultid uuid) RETURNS void
    LANGUAGE plpgsql
    AS $_$
DECLARE
  myStartYear tblCrossdate.StartYear%TYPE;
  myMasterVMeasurementID tblVMeasurement.VMeasurementID%TYPE;
  myDatingTypeID tlkpDatingType.DatingTypeID%TYPE;
BEGIN
  SELECT StartYear, MasterVMeasurementID INTO myStartYear, myMasterVMeasurementID
    FROM tblCrossdate WHERE VMeasurementID=$1;

  SELECT DatingTypeID INTO myDatingTypeID FROM cpgdb.getMetaCache(myMasterVMeasurementID);

  UPDATE tblVMeasurementResult
   SET VMeasurementID=$1, StartYear=myStartYear, DatingTypeID=myDatingTypeID
   WHERE VMeasurementResultID=$2;

END;
$_$;
 y   DROP FUNCTION cpgdbj.qupdvmeasurementresultopcrossdate(paramvmeasurementid uuid, paramcurrentvmeasurementresultid uuid);
       cpgdbj          postgres    false    11            E           0    0 k   FUNCTION qupdvmeasurementresultopcrossdate(paramvmeasurementid uuid, paramcurrentvmeasurementresultid uuid)    ACL     �   GRANT ALL ON FUNCTION cpgdbj.qupdvmeasurementresultopcrossdate(paramvmeasurementid uuid, paramcurrentvmeasurementresultid uuid) TO "Webgroup";
          cpgdbj          postgres    false    1331            4           1255    64795 *   qupdvmeasurementresultopredate(uuid, uuid)    FUNCTION     �  CREATE FUNCTION cpgdbj.qupdvmeasurementresultopredate(paramvmeasurementid uuid, paramcurrentvmeasurementresultid uuid) RETURNS void
    LANGUAGE plpgsql
    AS $_$
DECLARE
  chosenDatingTypeID tblRedate.RedatingTypeID%TYPE;
  chosenStartYear tblRedate.StartYear%TYPE;
BEGIN
  SELECT startYear,redatingTypeID INTO chosenStartYear,chosenDatingTypeID 
   FROM tblRedate
   WHERE VMeasurementID=$1;

  UPDATE tblVMeasurementResult
   SET VMeasurementID=$1, StartYear=chosenStartYear
   WHERE VMeasurementResultID=$2;

  IF chosenDatingTypeID IS NOT NULL THEN
     UPDATE tblVMeasurementResult SET DatingTypeID=chosenDatingTypeID
      WHERE VMeasurementResultID=$2;
  END IF;
END;
$_$;
 v   DROP FUNCTION cpgdbj.qupdvmeasurementresultopredate(paramvmeasurementid uuid, paramcurrentvmeasurementresultid uuid);
       cpgdbj          postgres    false    11            5           1255    64796 ,   qupdvmeasurementresultoptruncate(uuid, uuid)    FUNCTION     �  CREATE FUNCTION cpgdbj.qupdvmeasurementresultoptruncate(paramvmeasurementid uuid, paramvmeasurementresultid uuid) RETURNS void
    LANGUAGE plpgsql
    AS $_$
DECLARE
   XStartRelYear tblTruncate.StartRelYear%TYPE;
   XEndRelYear tblTruncate.EndRelYear%TYPE;
BEGIN
  SELECT StartRelYear, EndRelYear INTO XStartRelYear, XEndRelYear
     FROM tblTruncate WHERE VMeasurementID=$1;

  IF NOT FOUND THEN
     RAISE EXCEPTION 'No tblTruncate entry for VMID %', paramVMeasurementID;
  END IF;

  DELETE FROM tblVMeasurementReadingResult 
    WHERE VMeasurementResultID=$2 AND (RelYear < XStartRelYear OR RelYear > XEndRelYear);

  
  UPDATE tblVMeasurementReadingResult SET RelYear=-(RelYear-XStartRelYear)
    WHERE VMeasurementResultID=$2;

  UPDATE tblVMeasurementReadingResult SET RelYear=-RelYear
    WHERE VMeasurementResultID=$2;


  UPDATE tblVMeasurementResult SET StartYear=StartYear+XStartRelYear
    WHERE VMeasurementResultID=$2;

END;
$_$;
 q   DROP FUNCTION cpgdbj.qupdvmeasurementresultoptruncate(paramvmeasurementid uuid, paramvmeasurementresultid uuid);
       cpgdbj          postgres    false    11            F           0    0    FUNCTION addauth(text)    ACL     q   GRANT ALL ON FUNCTION public.addauth(text) TO pbrewer;
GRANT ALL ON FUNCTION public.addauth(text) TO "Webgroup";
          public          postgres    false    1006            6           1255    64797    addbbox(public.geometry)    FUNCTION     �   CREATE FUNCTION public.addbbox(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_addBBOX';
 /   DROP FUNCTION public.addbbox(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            7           1255    64798 *   addpoint(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.addpoint(public.geometry, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_addpoint';
 A   DROP FUNCTION public.addpoint(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            8           1255    64799 3   addpoint(public.geometry, public.geometry, integer)    FUNCTION     �   CREATE FUNCTION public.addpoint(public.geometry, public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_addpoint';
 J   DROP FUNCTION public.addpoint(public.geometry, public.geometry, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            9           1255    64800 �   affine(public.geometry, double precision, double precision, double precision, double precision, double precision, double precision)    FUNCTION     &  CREATE FUNCTION public.affine(public.geometry, double precision, double precision, double precision, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_affine($1,  $2, $3, 0,  $4, $5, 0,  0, 0, 1,  $6, $7, 0)$_$;
 �   DROP FUNCTION public.affine(public.geometry, double precision, double precision, double precision, double precision, double precision, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            :           1255    64801 �   affine(public.geometry, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision)    FUNCTION     l  CREATE FUNCTION public.affine(public.geometry, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_affine';
   DROP FUNCTION public.affine(public.geometry, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            ;           1255    64802 !   agg_first(anyelement, anyelement)    FUNCTION     �   CREATE FUNCTION public.agg_first(state anyelement, value anyelement) RETURNS anyelement
    LANGUAGE plpgsql IMMUTABLE
    AS $$
BEGIN

    IF (state IS NULL) THEN
        RETURN value;
    ELSE
        RETURN state;
    END IF;

END;
$$;
 D   DROP FUNCTION public.agg_first(state anyelement, value anyelement);
       public          postgres    false            G           0    0 6   FUNCTION agg_first(state anyelement, value anyelement)    ACL     Z   GRANT ALL ON FUNCTION public.agg_first(state anyelement, value anyelement) TO "Webgroup";
          public          postgres    false    1339            <           1255    64803    area(public.geometry)    FUNCTION     �   CREATE FUNCTION public.area(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'ST_Area';
 ,   DROP FUNCTION public.area(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            =           1255    64804    area2d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.area2d(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'ST_Area';
 .   DROP FUNCTION public.area2d(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            >           1255    64805    arraytorows(anyarray)    FUNCTION     �   CREATE FUNCTION public.arraytorows(arr anyarray) RETURNS SETOF anyelement
    LANGUAGE plpgsql IMMUTABLE
    AS $$
DECLARE
   idx integer;
BEGIN
   FOR idx IN array_lower(arr, 1)..array_upper(arr, 1) LOOP
      RETURN NEXT arr[idx];
   END LOOP;
END;
$$;
 0   DROP FUNCTION public.arraytorows(arr anyarray);
       public          postgres    false            ?           1255    64806    asbinary(public.geometry)    FUNCTION     �   CREATE FUNCTION public.asbinary(public.geometry) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_asBinary';
 0   DROP FUNCTION public.asbinary(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            @           1255    64807    asbinary(public.geometry, text)    FUNCTION     �   CREATE FUNCTION public.asbinary(public.geometry, text) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_asBinary';
 6   DROP FUNCTION public.asbinary(public.geometry, text);
       public          tellervo    false    3    3    3    3    3    3    3    3            A           1255    64808    asdf()    FUNCTION     t   CREATE FUNCTION public.asdf() RETURNS record
    LANGUAGE sql
    AS $$ 
SELECT * from tblvmeasurement limit 5;
$$;
    DROP FUNCTION public.asdf();
       public          postgres    false            B           1255    64809    asewkb(public.geometry)    FUNCTION     �   CREATE FUNCTION public.asewkb(public.geometry) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'WKBFromLWGEOM';
 .   DROP FUNCTION public.asewkb(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            C           1255    64810    asewkb(public.geometry, text)    FUNCTION     �   CREATE FUNCTION public.asewkb(public.geometry, text) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'WKBFromLWGEOM';
 4   DROP FUNCTION public.asewkb(public.geometry, text);
       public          tellervo    false    3    3    3    3    3    3    3    3            D           1255    64811    asewkt(public.geometry)    FUNCTION     �   CREATE FUNCTION public.asewkt(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_asEWKT';
 .   DROP FUNCTION public.asewkt(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            E           1255    64812    asgml(public.geometry)    FUNCTION     �   CREATE FUNCTION public.asgml(public.geometry) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT _ST_AsGML(2, $1, 15, 0, null, null)$_$;
 -   DROP FUNCTION public.asgml(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            F           1255    64813    asgml(public.geometry, integer)    FUNCTION     �   CREATE FUNCTION public.asgml(public.geometry, integer) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT _ST_AsGML(2, $1, $2, 0, null, null)$_$;
 6   DROP FUNCTION public.asgml(public.geometry, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            G           1255    64814    ashexewkb(public.geometry)    FUNCTION     �   CREATE FUNCTION public.ashexewkb(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_asHEXEWKB';
 1   DROP FUNCTION public.ashexewkb(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            H           1255    64815     ashexewkb(public.geometry, text)    FUNCTION     �   CREATE FUNCTION public.ashexewkb(public.geometry, text) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_asHEXEWKB';
 7   DROP FUNCTION public.ashexewkb(public.geometry, text);
       public          tellervo    false    3    3    3    3    3    3    3    3            I           1255    64816    askml(public.geometry)    FUNCTION     �   CREATE FUNCTION public.askml(public.geometry) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_AsKML(ST_Transform($1,4326), 15, null)$_$;
 -   DROP FUNCTION public.askml(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            J           1255    64817    askml(public.geometry, integer)    FUNCTION     �   CREATE FUNCTION public.askml(public.geometry, integer) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_AsKML(ST_transform($1,4326), $2, null)$_$;
 6   DROP FUNCTION public.askml(public.geometry, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            K           1255    64818 (   askml(integer, public.geometry, integer)    FUNCTION     �   CREATE FUNCTION public.askml(integer, public.geometry, integer) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_AsKML(ST_Transform($2,4326), $3, null)$_$;
 ?   DROP FUNCTION public.askml(integer, public.geometry, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            L           1255    64819    assvg(public.geometry)    FUNCTION     �   CREATE FUNCTION public.assvg(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_asSVG';
 -   DROP FUNCTION public.assvg(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            M           1255    64820    assvg(public.geometry, integer)    FUNCTION     �   CREATE FUNCTION public.assvg(public.geometry, integer) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_asSVG';
 6   DROP FUNCTION public.assvg(public.geometry, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            N           1255    64821 (   assvg(public.geometry, integer, integer)    FUNCTION     �   CREATE FUNCTION public.assvg(public.geometry, integer, integer) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_asSVG';
 ?   DROP FUNCTION public.assvg(public.geometry, integer, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            O           1255    64822    astext(public.geometry)    FUNCTION     �   CREATE FUNCTION public.astext(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_asText';
 .   DROP FUNCTION public.astext(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            P           1255    64823 )   azimuth(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.azimuth(public.geometry, public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_azimuth';
 @   DROP FUNCTION public.azimuth(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            Q           1255    64824    bdmpolyfromtext(text, integer)    FUNCTION     �  CREATE FUNCTION public.bdmpolyfromtext(text, integer) RETURNS public.geometry
    LANGUAGE plpgsql IMMUTABLE STRICT
    AS $_$
DECLARE
	geomtext alias for $1;
	srid alias for $2;
	mline geometry;
	geom geometry;
BEGIN
	mline := ST_MultiLineStringFromText(geomtext, srid);

	IF mline IS NULL
	THEN
		RAISE EXCEPTION 'Input is not a MultiLinestring';
	END IF;

	geom := ST_Multi(ST_BuildArea(mline));

	RETURN geom;
END;
$_$;
 5   DROP FUNCTION public.bdmpolyfromtext(text, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            R           1255    64825    bdpolyfromtext(text, integer)    FUNCTION     2  CREATE FUNCTION public.bdpolyfromtext(text, integer) RETURNS public.geometry
    LANGUAGE plpgsql IMMUTABLE STRICT
    AS $_$
DECLARE
	geomtext alias for $1;
	srid alias for $2;
	mline geometry;
	geom geometry;
BEGIN
	mline := ST_MultiLineStringFromText(geomtext, srid);

	IF mline IS NULL
	THEN
		RAISE EXCEPTION 'Input is not a MultiLinestring';
	END IF;

	geom := ST_BuildArea(mline);

	IF GeometryType(geom) != 'POLYGON'
	THEN
		RAISE EXCEPTION 'Input returns more then a single polygon, try using BdMPolyFromText instead';
	END IF;

	RETURN geom;
END;
$_$;
 4   DROP FUNCTION public.bdpolyfromtext(text, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            S           1255    64826    boundary(public.geometry)    FUNCTION     �   CREATE FUNCTION public.boundary(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'boundary';
 0   DROP FUNCTION public.boundary(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            H           0    0 !   FUNCTION box3dtobox(public.box3d)    ACL     �   GRANT ALL ON FUNCTION public.box3dtobox(public.box3d) TO pbrewer;
GRANT ALL ON FUNCTION public.box3dtobox(public.box3d) TO "Webgroup";
          public          postgres    false    726            T           1255    64827 )   buffer(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.buffer(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'buffer';
 @   DROP FUNCTION public.buffer(public.geometry, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            U           1255    64828 2   buffer(public.geometry, double precision, integer)    FUNCTION     �   CREATE FUNCTION public.buffer(public.geometry, double precision, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_Buffer($1, $2, $3)$_$;
 I   DROP FUNCTION public.buffer(public.geometry, double precision, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            V           1255    64829    buildarea(public.geometry)    FUNCTION     �   CREATE FUNCTION public.buildarea(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'ST_BuildArea';
 1   DROP FUNCTION public.buildarea(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            W           1255    64830    centroid(public.geometry)    FUNCTION     �   CREATE FUNCTION public.centroid(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'centroid';
 0   DROP FUNCTION public.centroid(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            X           1255    64831    checkGroupIsDeletable()    FUNCTION     �   CREATE FUNCTION public."checkGroupIsDeletable"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
IF OLD.securitygroupid = 1 THEN 
	RAISE EXCEPTION 'The administrator group can not be deleted';
	RETURN NULL;
END IF;
RETURN NEW;
END;$$;
 0   DROP FUNCTION public."checkGroupIsDeletable"();
       public          tellervo    false            Y           1255    64832    check_datingerrors()    FUNCTION     �  CREATE FUNCTION public.check_datingerrors() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
	IF NEW.datingtypeid = 2 THEN
		-- Check errors have been specified if dating is uncertain --
		IF NEW.datingerrorpositive = 0 THEN 
			RAISE EXCEPTION 'Dating error cannot be zero for a measurement with uncertainty!'; 
		END IF;
		IF NEW.datingerrornegative = 0 THEN 
			RAISE EXCEPTION 'Dating error cannot be zero for a measurement with uncertainty!'; 
		END IF;
	ELSE
		-- Override the dating error to zero when tree is absolutely or relatively dated --
		IF NEW.datingerrorpositive > 0 THEN 
			RAISE WARNING 'Dating error cannot be greater than zero for an absolutely dated measurement! Defaulting to zero.'; 
		END IF;
		IF NEW.datingerrornegative > 0 THEN 
			RAISE WARNING 'Dating error cannot be greater than zero for an absolutely dated measurement! Defaulting to zero.'; 
		END IF;
		NEW.datingerrorpositive := 0;
		NEW.datingerrornegative := 0;
	END IF;
	RETURN new;
END;$$;
 +   DROP FUNCTION public.check_datingerrors();
       public          postgres    false            I           0    0    FUNCTION check_datingerrors()    ACL     A   GRANT ALL ON FUNCTION public.check_datingerrors() TO "Webgroup";
          public          postgres    false    1369            Z           1255    64833 2   check_tblcuration_loanid_is_not_null_when_loaned()    FUNCTION     �  CREATE FUNCTION public.check_tblcuration_loanid_is_not_null_when_loaned() RETURNS trigger
    LANGUAGE plpgsql
    AS $$DECLARE
BEGIN
IF NEW.curationstatusid=2 OR NEW.curationstatusid=10 THEN
  IF NEW.loanid IS NULL THEN
	RAISE EXCEPTION 'Loan information not specified for loan curation record';
	RETURN NULL;
  END IF;
ELSE
  IF NEW.loanid IS NOT NULL THEN
	RAISE EXCEPTION 'Loan information can only be provided when loaning a sample/box';
	RETURN NULL;
  END IF;
END IF;
RETURN NEW;
END;$$;
 I   DROP FUNCTION public.check_tblcuration_loanid_is_not_null_when_loaned();
       public          tellervo    false            J           0    0    FUNCTION checkauth(text, text)    ACL     �   GRANT ALL ON FUNCTION public.checkauth(text, text) TO pbrewer;
GRANT ALL ON FUNCTION public.checkauth(text, text) TO "Webgroup";
          public          postgres    false    1008            K           0    0 $   FUNCTION checkauth(text, text, text)    ACL     �   GRANT ALL ON FUNCTION public.checkauth(text, text, text) TO pbrewer;
GRANT ALL ON FUNCTION public.checkauth(text, text, text) TO "Webgroup";
          public          postgres    false    1007            L           0    0    FUNCTION checkauthtrigger()    ACL     {   GRANT ALL ON FUNCTION public.checkauthtrigger() TO pbrewer;
GRANT ALL ON FUNCTION public.checkauthtrigger() TO "Webgroup";
          public          postgres    false    1009            [           1255    64834 )   collect(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.collect(public.geometry, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE
    AS '$libdir/postgis-3', 'LWGEOM_collect';
 @   DROP FUNCTION public.collect(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            \           1255    64835 +   combine_bbox(public.box2d, public.geometry)    FUNCTION     �   CREATE FUNCTION public.combine_bbox(public.box2d, public.geometry) RETURNS public.box2d
    LANGUAGE c IMMUTABLE
    AS '$libdir/postgis-3', 'BOX2D_combine';
 B   DROP FUNCTION public.combine_bbox(public.box2d, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3            ]           1255    64836 +   combine_bbox(public.box3d, public.geometry)    FUNCTION     �   CREATE FUNCTION public.combine_bbox(public.box3d, public.geometry) RETURNS public.box3d
    LANGUAGE c IMMUTABLE
    AS '$libdir/postgis-3', 'BOX3D_combine';
 B   DROP FUNCTION public.combine_bbox(public.box3d, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3            ^           1255    64837 *   connectby(text, text, text, text, integer)    FUNCTION     �   CREATE FUNCTION public.connectby(text, text, text, text, integer) RETURNS SETOF record
    LANGUAGE c STABLE STRICT
    AS '$libdir/tablefunc', 'connectby_text';
 A   DROP FUNCTION public.connectby(text, text, text, text, integer);
       public          postgres    false            _           1255    64838 0   connectby(text, text, text, text, integer, text)    FUNCTION     �   CREATE FUNCTION public.connectby(text, text, text, text, integer, text) RETURNS SETOF record
    LANGUAGE c STABLE STRICT
    AS '$libdir/tablefunc', 'connectby_text';
 G   DROP FUNCTION public.connectby(text, text, text, text, integer, text);
       public          postgres    false            `           1255    64839 0   connectby(text, text, text, text, text, integer)    FUNCTION     �   CREATE FUNCTION public.connectby(text, text, text, text, text, integer) RETURNS SETOF record
    LANGUAGE c STABLE STRICT
    AS '$libdir/tablefunc', 'connectby_text_serial';
 G   DROP FUNCTION public.connectby(text, text, text, text, text, integer);
       public          postgres    false            a           1255    64840 6   connectby(text, text, text, text, text, integer, text)    FUNCTION     �   CREATE FUNCTION public.connectby(text, text, text, text, text, integer, text) RETURNS SETOF record
    LANGUAGE c STABLE STRICT
    AS '$libdir/tablefunc', 'connectby_text_serial';
 M   DROP FUNCTION public.connectby(text, text, text, text, text, integer, text);
       public          postgres    false            b           1255    64841 *   contains(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.contains(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'contains';
 A   DROP FUNCTION public.contains(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    66896 %   convert_to_integer(character varying)    FUNCTION     D  CREATE FUNCTION public.convert_to_integer(character varying) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
   v_int_value INTEGER DEFAULT NULL;
BEGIN
	BEGIN 
    	v_int_value := v_input::INTEGER; 
    EXCEPTION WHEN OTHERS THEN           
    	RETURN NULL;          
    END; 
    
RETURN v_int_value;    
END;
$$;
 <   DROP FUNCTION public.convert_to_integer(character varying);
       public          tellervo    false            c           1255    64842    convexhull(public.geometry)    FUNCTION     �   CREATE FUNCTION public.convexhull(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'convexhull';
 2   DROP FUNCTION public.convexhull(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            d           1255    64843 )   create_defaultreadingnotestandardisedid()    FUNCTION     l  CREATE FUNCTION public.create_defaultreadingnotestandardisedid() RETURNS trigger
    LANGUAGE plpgsql
    AS $$DECLARE

newuuid uuid;


BEGIN


IF NEW.vocabularyid<2 THEN
  NEW.standardisedid=NEW.readingnoteid;
  RETURN NEW;
END IF;

IF NEW.standardisedid IS NULL THEN
	newuuid = uuid_generate_v1mc();
  NEW.standardisedid=newuuid;
END IF;


RETURN NEW;


END;$$;
 @   DROP FUNCTION public.create_defaultreadingnotestandardisedid();
       public          postgres    false            M           0    0 2   FUNCTION create_defaultreadingnotestandardisedid()    ACL     V   GRANT ALL ON FUNCTION public.create_defaultreadingnotestandardisedid() TO "Webgroup";
          public          postgres    false    1380            e           1255    64844 &   create_defaultsecurityrecordforgroup()    FUNCTION     �   CREATE FUNCTION public.create_defaultsecurityrecordforgroup() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
INSERT INTO tblsecuritydefault (securitygroupid, securitypermissionid) values (NEW.securitygroupid, 6);
RETURN NULL;
END;$$;
 =   DROP FUNCTION public.create_defaultsecurityrecordforgroup();
       public          postgres    false            N           0    0 /   FUNCTION create_defaultsecurityrecordforgroup()    ACL     S   GRANT ALL ON FUNCTION public.create_defaultsecurityrecordforgroup() TO "Webgroup";
          public          postgres    false    1381            f           1255    64845 '   create_objectsecurity(integer, integer)    FUNCTION     #  CREATE FUNCTION public.create_objectsecurity(objectid integer, securityuserid integer) RETURNS boolean
    LANGUAGE sql
    AS $_$insert into tblsecurityobject (objectid, securitygroupid, securitypermissionid) values ($1, $2, 2);
insert into tblsecurityobject (objectid, securitygroupid, securitypermissionid) values ($1, $2, 3);
insert into tblsecurityobject (objectid, securitygroupid, securitypermissionid) values ($1, $2, 4);
insert into tblsecurityobject (objectid, securitygroupid, securitypermissionid) values ($1, $2, 5);
select true;$_$;
 V   DROP FUNCTION public.create_objectsecurity(objectid integer, securityuserid integer);
       public          postgres    false            O           0    0 H   FUNCTION create_objectsecurity(objectid integer, securityuserid integer)    ACL     l   GRANT ALL ON FUNCTION public.create_objectsecurity(objectid integer, securityuserid integer) TO "Webgroup";
          public          postgres    false    1382            g           1255    64846    create_subobjectforobject()    FUNCTION     �   CREATE FUNCTION public.create_subobjectforobject() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
INSERT INTO tblsubobject(name, objectid) VALUES('Main', NEW.objectid);
RETURN NULL;
END;$$;
 2   DROP FUNCTION public.create_subobjectforobject();
       public          postgres    false            P           0    0 $   FUNCTION create_subobjectforobject()    COMMENT     ?   COMMENT ON FUNCTION public.create_subobjectforobject() IS '
';
          public          postgres    false    1383            Q           0    0 $   FUNCTION create_subobjectforobject()    ACL     H   GRANT ALL ON FUNCTION public.create_subobjectforobject() TO "Webgroup";
          public          postgres    false    1383            h           1255    64847 %   create_treesecurity(integer, integer)    FUNCTION       CREATE FUNCTION public.create_treesecurity(elementid integer, securityuserid integer) RETURNS boolean
    LANGUAGE sql
    AS $_$insert into tblsecuritytree (elementid, securitygroupid, securitypermissionid) values ($1, $2, 2);
insert into tblsecuritytree (elementid, securitygroupid, securitypermissionid) values ($1, $2, 3);
insert into tblsecuritytree (elementid, securitygroupid, securitypermissionid) values ($1, $2, 4);
insert into tblsecuritytree (elementid, securitygroupid, securitypermissionid) values ($1, $2, 5);
select true;$_$;
 U   DROP FUNCTION public.create_treesecurity(elementid integer, securityuserid integer);
       public          postgres    false            R           0    0 G   FUNCTION create_treesecurity(elementid integer, securityuserid integer)    ACL     k   GRANT ALL ON FUNCTION public.create_treesecurity(elementid integer, securityuserid integer) TO "Webgroup";
          public          postgres    false    1384            i           1255    64848 -   create_vmeasurementsecurity(integer, integer)    FUNCTION     _  CREATE FUNCTION public.create_vmeasurementsecurity(vmeasurementid integer, securityuserid integer) RETURNS boolean
    LANGUAGE sql
    AS $_$insert into tblsecurityvmeasurement (vmeasurementid, securitygroupid, securitypermissionid) values ($1, $2, 2);
insert into tblsecurityvmeasurement (vmeasurementid, securitygroupid, securitypermissionid) values ($1, $2, 3);
insert into tblsecurityvmeasurement (vmeasurementid, securitygroupid, securitypermissionid) values ($1, $2, 4);
insert into tblsecurityvmeasurement (vmeasurementid, securitygroupid, securitypermissionid) values ($1, $2, 5);
select true;$_$;
 b   DROP FUNCTION public.create_vmeasurementsecurity(vmeasurementid integer, securityuserid integer);
       public          postgres    false            S           0    0 T   FUNCTION create_vmeasurementsecurity(vmeasurementid integer, securityuserid integer)    ACL     x   GRANT ALL ON FUNCTION public.create_vmeasurementsecurity(vmeasurementid integer, securityuserid integer) TO "Webgroup";
          public          postgres    false    1385            j           1255    64849 )   crosses(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.crosses(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'crosses';
 @   DROP FUNCTION public.crosses(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            k           1255    64850    crosstab(text)    FUNCTION     �   CREATE FUNCTION public.crosstab(text) RETURNS SETOF record
    LANGUAGE c STABLE STRICT
    AS '$libdir/tablefunc', 'crosstab';
 %   DROP FUNCTION public.crosstab(text);
       public          postgres    false            l           1255    64851    crosstab(text, integer)    FUNCTION     �   CREATE FUNCTION public.crosstab(text, integer) RETURNS SETOF record
    LANGUAGE c STABLE STRICT
    AS '$libdir/tablefunc', 'crosstab';
 .   DROP FUNCTION public.crosstab(text, integer);
       public          postgres    false            m           1255    64852    crosstab(text, text)    FUNCTION     �   CREATE FUNCTION public.crosstab(text, text) RETURNS SETOF record
    LANGUAGE c STABLE STRICT
    AS '$libdir/tablefunc', 'crosstab_hash';
 +   DROP FUNCTION public.crosstab(text, text);
       public          postgres    false            n           1255    64853    crosstab11(text)    FUNCTION     �   CREATE FUNCTION public.crosstab11(text) RETURNS SETOF public.tablefunc_crosstab_11
    LANGUAGE c STABLE STRICT
    AS '$libdir/tablefunc', 'crosstab';
 '   DROP FUNCTION public.crosstab11(text);
       public          postgres    false    2233            o           1255    64854    crosstab2(text)    FUNCTION     �   CREATE FUNCTION public.crosstab2(text) RETURNS SETOF public.tablefunc_crosstab_2
    LANGUAGE c STABLE STRICT
    AS '$libdir/tablefunc', 'crosstab';
 &   DROP FUNCTION public.crosstab2(text);
       public          postgres    false    2236            p           1255    64855    crosstab3(text)    FUNCTION     �   CREATE FUNCTION public.crosstab3(text) RETURNS SETOF public.tablefunc_crosstab_3
    LANGUAGE c STABLE STRICT
    AS '$libdir/tablefunc', 'crosstab';
 &   DROP FUNCTION public.crosstab3(text);
       public          postgres    false    2239            q           1255    64856    crosstab4(text)    FUNCTION     �   CREATE FUNCTION public.crosstab4(text) RETURNS SETOF public.tablefunc_crosstab_4
    LANGUAGE c STABLE STRICT
    AS '$libdir/tablefunc', 'crosstab';
 &   DROP FUNCTION public.crosstab4(text);
       public          postgres    false    2242            r           1255    64857    crosstab9(text)    FUNCTION     �   CREATE FUNCTION public.crosstab9(text) RETURNS SETOF public.tablefunc_crosstab_9
    LANGUAGE c STABLE STRICT
    AS '$libdir/tablefunc', 'crosstab';
 &   DROP FUNCTION public.crosstab9(text);
       public          postgres    false    2245            s           1255    64858 ,   difference(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.difference(public.geometry, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'ST_Difference';
 C   DROP FUNCTION public.difference(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            t           1255    64859    dimension(public.geometry)    FUNCTION     �   CREATE FUNCTION public.dimension(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_dimension';
 1   DROP FUNCTION public.dimension(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            T           0    0 "   FUNCTION disablelongtransactions()    ACL     �   GRANT ALL ON FUNCTION public.disablelongtransactions() TO pbrewer;
GRANT ALL ON FUNCTION public.disablelongtransactions() TO "Webgroup";
          public          postgres    false    1013            u           1255    64860 *   disjoint(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.disjoint(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'disjoint';
 A   DROP FUNCTION public.disjoint(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            v           1255    64861 *   distance(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.distance(public.geometry, public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'ST_Distance';
 A   DROP FUNCTION public.distance(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            w           1255    64862 1   distance_sphere(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.distance_sphere(public.geometry, public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'LWGEOM_distance_sphere';
 H   DROP FUNCTION public.distance_sphere(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            x           1255    64863 D   distance_spheroid(public.geometry, public.geometry, public.spheroid)    FUNCTION     �   CREATE FUNCTION public.distance_spheroid(public.geometry, public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'LWGEOM_distance_ellipsoid';
 [   DROP FUNCTION public.distance_spheroid(public.geometry, public.geometry, public.spheroid);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            y           1255    64864    dropbbox(public.geometry)    FUNCTION     �   CREATE FUNCTION public.dropbbox(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_dropBBOX';
 0   DROP FUNCTION public.dropbbox(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            U           0    0 X   FUNCTION dropgeometrycolumn(table_name character varying, column_name character varying)    ACL     �   GRANT ALL ON FUNCTION public.dropgeometrycolumn(table_name character varying, column_name character varying) TO pbrewer;
GRANT ALL ON FUNCTION public.dropgeometrycolumn(table_name character varying, column_name character varying) TO "Webgroup";
          public          postgres    false    678            V           0    0 w   FUNCTION dropgeometrycolumn(schema_name character varying, table_name character varying, column_name character varying)    ACL     3  GRANT ALL ON FUNCTION public.dropgeometrycolumn(schema_name character varying, table_name character varying, column_name character varying) TO pbrewer;
GRANT ALL ON FUNCTION public.dropgeometrycolumn(schema_name character varying, table_name character varying, column_name character varying) TO "Webgroup";
          public          postgres    false    677            W           0    0 �   FUNCTION dropgeometrycolumn(catalog_name character varying, schema_name character varying, table_name character varying, column_name character varying)    ACL     s  GRANT ALL ON FUNCTION public.dropgeometrycolumn(catalog_name character varying, schema_name character varying, table_name character varying, column_name character varying) TO pbrewer;
GRANT ALL ON FUNCTION public.dropgeometrycolumn(catalog_name character varying, schema_name character varying, table_name character varying, column_name character varying) TO "Webgroup";
          public          postgres    false    676            X           0    0 8   FUNCTION dropgeometrytable(table_name character varying)    ACL     �   GRANT ALL ON FUNCTION public.dropgeometrytable(table_name character varying) TO pbrewer;
GRANT ALL ON FUNCTION public.dropgeometrytable(table_name character varying) TO "Webgroup";
          public          postgres    false    681            Y           0    0 W   FUNCTION dropgeometrytable(schema_name character varying, table_name character varying)    ACL     �   GRANT ALL ON FUNCTION public.dropgeometrytable(schema_name character varying, table_name character varying) TO pbrewer;
GRANT ALL ON FUNCTION public.dropgeometrytable(schema_name character varying, table_name character varying) TO "Webgroup";
          public          postgres    false    680            Z           0    0 w   FUNCTION dropgeometrytable(catalog_name character varying, schema_name character varying, table_name character varying)    ACL     3  GRANT ALL ON FUNCTION public.dropgeometrytable(catalog_name character varying, schema_name character varying, table_name character varying) TO pbrewer;
GRANT ALL ON FUNCTION public.dropgeometrytable(catalog_name character varying, schema_name character varying, table_name character varying) TO "Webgroup";
          public          postgres    false    679            z           1255    64865    dump(public.geometry)    FUNCTION     �   CREATE FUNCTION public.dump(public.geometry) RETURNS SETOF public.geometry_dump
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_dump';
 ,   DROP FUNCTION public.dump(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            {           1255    64866    dumprings(public.geometry)    FUNCTION     �   CREATE FUNCTION public.dumprings(public.geometry) RETURNS SETOF public.geometry_dump
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_dump_rings';
 1   DROP FUNCTION public.dumprings(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            [           0    0 !   FUNCTION enablelongtransactions()    ACL     �   GRANT ALL ON FUNCTION public.enablelongtransactions() TO pbrewer;
GRANT ALL ON FUNCTION public.enablelongtransactions() TO "Webgroup";
          public          postgres    false    1011            |           1255    64867    endpoint(public.geometry)    FUNCTION     �   CREATE FUNCTION public.endpoint(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_endpoint_linestring';
 0   DROP FUNCTION public.endpoint(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            }           1255    64868 #   enforce_atleastoneadminuserdelete()    FUNCTION     �  CREATE FUNCTION public.enforce_atleastoneadminuserdelete() RETURNS trigger
    LANGUAGE plpgsql
    AS $$DECLARE
 usrrow tblsecurityuser%ROWTYPE;
 grprow int;
 cnt int;
BEGIN
cnt := 0;
FOR usrrow IN SELECT * from tblsecurityuser WHERE isactive='t' LOOP
   RAISE NOTICE 'Checking details of user %', usrrow.securityuserid;
   FOR grprow IN SELECT securitygroupid from cpgdb.getgroupmembership(usrrow.securityuserid) LOOP
      IF grprow=1 THEN
         cnt = cnt +1;
      END IF;
   END LOOP;   
END LOOP;
RAISE NOTICE 'Number of remaining admin users is %', cnt;
IF(cnt=0) THEN
	RAISE EXCEPTION 'Cannot delete the last user with administrative privileges';
	RETURN NULL;
END IF;
RETURN OLD;
END;$$;
 :   DROP FUNCTION public.enforce_atleastoneadminuserdelete();
       public          postgres    false            ~           1255    64869 #   enforce_atleastoneadminuserupdate()    FUNCTION     �  CREATE FUNCTION public.enforce_atleastoneadminuserupdate() RETURNS trigger
    LANGUAGE plpgsql
    AS $$DECLARE
 usrrow tblsecurityuser%ROWTYPE;
 grprow int;
 cnt int;
BEGIN
cnt := 0;
FOR usrrow IN SELECT * from tblsecurityuser WHERE isactive='t' LOOP
   RAISE NOTICE 'Checking details of user %', usrrow.securityuserid;
   FOR grprow IN SELECT securitygroupid from cpgdb.getgroupmembership(usrrow.securityuserid) LOOP
      IF grprow=1 THEN
         cnt = cnt +1;
      END IF;
   END LOOP;   
END LOOP;
RAISE NOTICE 'Number of remaining admin users is %', cnt;
IF(cnt=0) THEN
	RAISE EXCEPTION 'Cannot delete the last user with administrative privileges';
	RETURN NULL;
END IF;
RETURN NEW;
END;$$;
 :   DROP FUNCTION public.enforce_atleastoneadminuserupdate();
       public          postgres    false                       1255    64870    enforce_no_loan_when_on_loan()    FUNCTION     �  CREATE FUNCTION public.enforce_no_loan_when_on_loan() RETURNS trigger
    LANGUAGE plpgsql
    AS $$DECLARE

prevcurationstatusid integer; 

BEGIN

-- If we're not trying to loan the sample then all is fine
IF NEW.curationstatusid<>2 AND NEW.curationstatusid<>3 THEN
	RETURN NEW;
END IF;

-- Otherwise we need to check it's previous status is not incompatable with loaning
FOR prevcurationstatusid IN SELECT curationstatusid FROM tblcurationevent WHERE sampleid = NEW.sampleid ORDER BY createdtimestamp desc LIMIT 1 LOOP

	IF prevcurationstatusid = 2 THEN
		RAISE EXCEPTION 'Cannot loan a sample that is already on loan';
		RETURN NULL;
	END IF;

	IF prevcurationstatusid = 6 THEN
		RAISE EXCEPTION 'Cannot loan a sample that has been destroyed';
		RETURN NULL;
	END IF;

	IF prevcurationstatusid = 7 THEN
		RAISE EXCEPTION 'Cannot loan a sample that has been returned to its owner';
		RETURN NULL;
	END IF;
	
END LOOP;

RETURN NEW;
END;$$;
 5   DROP FUNCTION public.enforce_no_loan_when_on_loan();
       public          tellervo    false            �           1255    64871 &   enforce_no_status_update_on_curation()    FUNCTION     [  CREATE FUNCTION public.enforce_no_status_update_on_curation() RETURNS trigger
    LANGUAGE plpgsql
    AS $$DECLARE

BEGIN

IF NEW.curationstatusid <> OLD.curationstatusid THEN
	RAISE EXCEPTION 'Changing the status of a curation record is not allowed.  A new curation record should be created instead';
	RETURN NULL;
END IF;

RETURN NEW;

END;$$;
 =   DROP FUNCTION public.enforce_no_status_update_on_curation();
       public          tellervo    false            �           1255    64872 "   enforce_noadminpermeditsondelete()    FUNCTION     �   CREATE FUNCTION public.enforce_noadminpermeditsondelete() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
IF OLD.securitygroupid=1 THEN
   RAISE EXCEPTION 'Administrator permissions cannot be changed';
   RETURN NULL;
END IF;
RETURN OLD;
END;$$;
 9   DROP FUNCTION public.enforce_noadminpermeditsondelete();
       public          tellervo    false            �           1255    64873 (   enforce_noadminpermeditsonupdatecreate()    FUNCTION        CREATE FUNCTION public.enforce_noadminpermeditsonupdatecreate() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
IF NEW.securitygroupid=1 THEN
   RAISE EXCEPTION 'Administrator permissions cannot be changed';
   RETURN NULL;
END IF;
RETURN NEW;
END;$$;
 ?   DROP FUNCTION public.enforce_noadminpermeditsonupdatecreate();
       public          tellervo    false            �           1255    64874    enforce_object-parent()    FUNCTION     1  CREATE FUNCTION public."enforce_object-parent"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN

IF NEW.parentobjectid IS NULL AND NEW.projectid IS NULL THEN
  RAISE EXCEPTION 'Objects must have either a project or parent object assigned';
  RETURN NULL;
ELSIF NEW.parentobjectid IS NOT NULL AND NEW.projectid IS NOT NULL THEN
  RAISE NOTICE 'Sub-objects cannot be assigned to a project.  Project membership is inherited through its parent object, so requested project for this object will be ignored';
  NEW.projectid:=NULL;
END IF;

RETURN NEW;
END;$$;
 0   DROP FUNCTION public."enforce_object-parent"();
       public          tellervo    false            �           1255    64875    envelope(public.geometry)    FUNCTION     �   CREATE FUNCTION public.envelope(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_envelope';
 0   DROP FUNCTION public.envelope(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            \           0    0 =   FUNCTION equals(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.equals(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.equals(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    870            �           1255    64876    estimated_extent(text, text)    FUNCTION     �   CREATE FUNCTION public.estimated_extent(text, text) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT SECURITY DEFINER
    AS '$libdir/postgis-3', 'geometry_estimated_extent';
 3   DROP FUNCTION public.estimated_extent(text, text);
       public          tellervo    false    3    3    3            �           1255    64877 "   estimated_extent(text, text, text)    FUNCTION     �   CREATE FUNCTION public.estimated_extent(text, text, text) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT SECURITY DEFINER
    AS '$libdir/postgis-3', 'geometry_estimated_extent';
 9   DROP FUNCTION public.estimated_extent(text, text, text);
       public          tellervo    false    3    3    3            �           1255    64878 &   expand(public.box2d, double precision)    FUNCTION     �   CREATE FUNCTION public.expand(public.box2d, double precision) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX2D_expand';
 =   DROP FUNCTION public.expand(public.box2d, double precision);
       public          tellervo    false    3    3    3    3    3    3            �           1255    64879 &   expand(public.box3d, double precision)    FUNCTION     �   CREATE FUNCTION public.expand(public.box3d, double precision) RETURNS public.box3d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_expand';
 =   DROP FUNCTION public.expand(public.box3d, double precision);
       public          tellervo    false    3    3    3    3    3    3            �           1255    64880 )   expand(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.expand(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_expand';
 @   DROP FUNCTION public.expand(public.geometry, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64881    exteriorring(public.geometry)    FUNCTION     �   CREATE FUNCTION public.exteriorring(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_exteriorring_polygon';
 4   DROP FUNCTION public.exteriorring(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64882    find_extent(text, text)    FUNCTION     ]  CREATE FUNCTION public.find_extent(text, text) RETURNS public.box2d
    LANGUAGE plpgsql IMMUTABLE STRICT
    AS $_$
DECLARE
	tablename alias for $1;
	columnname alias for $2;
	myrec RECORD;

BEGIN
	FOR myrec IN EXECUTE 'SELECT ST_Extent("' || columnname || '") As extent FROM "' || tablename || '"' LOOP
		return myrec.extent;
	END LOOP;
END;
$_$;
 .   DROP FUNCTION public.find_extent(text, text);
       public          tellervo    false    3    3    3            �           1255    64883    find_extent(text, text, text)    FUNCTION     �  CREATE FUNCTION public.find_extent(text, text, text) RETURNS public.box2d
    LANGUAGE plpgsql IMMUTABLE STRICT
    AS $_$
DECLARE
	schemaname alias for $1;
	tablename alias for $2;
	columnname alias for $3;
	myrec RECORD;

BEGIN
	FOR myrec IN EXECUTE 'SELECT ST_Extent("' || columnname || '") FROM "' || schemaname || '"."' || tablename || '" As extent ' LOOP
		return myrec.extent;
	END LOOP;
END;
$_$;
 4   DROP FUNCTION public.find_extent(text, text, text);
       public          tellervo    false    3    3    3            ]           0    0 K   FUNCTION find_srid(character varying, character varying, character varying)    ACL     �   GRANT ALL ON FUNCTION public.find_srid(character varying, character varying, character varying) TO pbrewer;
GRANT ALL ON FUNCTION public.find_srid(character varying, character varying, character varying) TO "Webgroup";
          public          postgres    false    685            �           1255    64884    fix_geometry_columns()    FUNCTION     (  CREATE FUNCTION public.fix_geometry_columns() RETURNS text
    LANGUAGE plpgsql
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
	
	return 'This function is obsolete now that geometry_columns is a view';

END;
$$;
 -   DROP FUNCTION public.fix_geometry_columns();
       public          tellervo    false            �           1255    64885    force_2d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.force_2d(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_force_2d';
 0   DROP FUNCTION public.force_2d(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64886    force_3d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.force_3d(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_force_3dz';
 0   DROP FUNCTION public.force_3d(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64887    force_3dm(public.geometry)    FUNCTION     �   CREATE FUNCTION public.force_3dm(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_force_3dm';
 1   DROP FUNCTION public.force_3dm(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64888    force_3dz(public.geometry)    FUNCTION     �   CREATE FUNCTION public.force_3dz(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_force_3dz';
 1   DROP FUNCTION public.force_3dz(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64889    force_4d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.force_4d(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_force_4d';
 0   DROP FUNCTION public.force_4d(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64890 !   force_collection(public.geometry)    FUNCTION     �   CREATE FUNCTION public.force_collection(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_force_collection';
 8   DROP FUNCTION public.force_collection(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64891    forcerhr(public.geometry)    FUNCTION     �   CREATE FUNCTION public.forcerhr(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_force_clockwise_poly';
 0   DROP FUNCTION public.forcerhr(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64892    geomcollfromtext(text)    FUNCTION     �   CREATE FUNCTION public.geomcollfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE
	WHEN geometrytype(GeomFromText($1)) = 'GEOMETRYCOLLECTION'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$;
 -   DROP FUNCTION public.geomcollfromtext(text);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64893    geomcollfromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.geomcollfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE
	WHEN geometrytype(GeomFromText($1, $2)) = 'GEOMETRYCOLLECTION'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$;
 6   DROP FUNCTION public.geomcollfromtext(text, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64894    geomcollfromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.geomcollfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE
	WHEN geometrytype(GeomFromWKB($1)) = 'GEOMETRYCOLLECTION'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;
 -   DROP FUNCTION public.geomcollfromwkb(bytea);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64895    geomcollfromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.geomcollfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE
	WHEN geometrytype(GeomFromWKB($1, $2)) = 'GEOMETRYCOLLECTION'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;
 6   DROP FUNCTION public.geomcollfromwkb(bytea, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            ^           0    0 E   FUNCTION geometry_above(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_above(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_above(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    522            _           0    0 E   FUNCTION geometry_below(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_below(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_below(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    517            `           0    0 C   FUNCTION geometry_cmp(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_cmp(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_cmp(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    489            a           0    0 B   FUNCTION geometry_eq(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_eq(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_eq(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    488            b           0    0 B   FUNCTION geometry_ge(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_ge(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_ge(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    487            c           0    0 B   FUNCTION geometry_gt(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_gt(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_gt(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    486            d           0    0 B   FUNCTION geometry_le(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_le(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_le(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    485            e           0    0 D   FUNCTION geometry_left(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_left(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_left(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    515            f           0    0 B   FUNCTION geometry_lt(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_lt(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_lt(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    484            g           0    0 I   FUNCTION geometry_overabove(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_overabove(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_overabove(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    521            h           0    0 I   FUNCTION geometry_overbelow(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_overbelow(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_overbelow(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    518            i           0    0 H   FUNCTION geometry_overleft(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_overleft(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_overleft(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    516            j           0    0 I   FUNCTION geometry_overright(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_overright(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_overright(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    519            k           0    0 E   FUNCTION geometry_right(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_right(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_right(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    520            l           0    0 D   FUNCTION geometry_same(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_same(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_same(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    510            �           1255    64896    geometryfromtext(text)    FUNCTION     �   CREATE FUNCTION public.geometryfromtext(text) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_from_text';
 -   DROP FUNCTION public.geometryfromtext(text);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64897    geometryfromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.geometryfromtext(text, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_from_text';
 6   DROP FUNCTION public.geometryfromtext(text, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64898 #   geometryn(public.geometry, integer)    FUNCTION     �   CREATE FUNCTION public.geometryn(public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_geometryn_collection';
 :   DROP FUNCTION public.geometryn(public.geometry, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            m           0    0    FUNCTION geomfromewkb(bytea)    ACL     }   GRANT ALL ON FUNCTION public.geomfromewkb(bytea) TO pbrewer;
GRANT ALL ON FUNCTION public.geomfromewkb(bytea) TO "Webgroup";
          public          postgres    false    621            n           0    0    FUNCTION geomfromewkt(text)    ACL     {   GRANT ALL ON FUNCTION public.geomfromewkt(text) TO pbrewer;
GRANT ALL ON FUNCTION public.geomfromewkt(text) TO "Webgroup";
          public          postgres    false    624            �           1255    64899    geomfromtext(text)    FUNCTION     �   CREATE FUNCTION public.geomfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_GeomFromText($1)$_$;
 )   DROP FUNCTION public.geomfromtext(text);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64900    geomfromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.geomfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_GeomFromText($1, $2)$_$;
 2   DROP FUNCTION public.geomfromtext(text, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64901    geomfromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.geomfromwkb(bytea) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_from_WKB';
 )   DROP FUNCTION public.geomfromwkb(bytea);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64902    geomfromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.geomfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_SetSRID(ST_GeomFromWKB($1), $2)$_$;
 2   DROP FUNCTION public.geomfromwkb(bytea, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64903 +   geomunion(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.geomunion(public.geometry, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'ST_Union';
 B   DROP FUNCTION public.geomunion(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            o           0    0 %   FUNCTION get_proj4_from_srid(integer)    ACL     �   GRANT ALL ON FUNCTION public.get_proj4_from_srid(integer) TO pbrewer;
GRANT ALL ON FUNCTION public.get_proj4_from_srid(integer) TO "Webgroup";
          public          postgres    false    686            �           1255    64904    getbbox(public.geometry)    FUNCTION     �   CREATE FUNCTION public.getbbox(public.geometry) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_to_BOX2D';
 /   DROP FUNCTION public.getbbox(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3            �           1255    64905    getsrid(public.geometry)    FUNCTION     �   CREATE FUNCTION public.getsrid(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_get_srid';
 /   DROP FUNCTION public.getsrid(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            p           0    0    FUNCTION gettransactionid()    ACL     {   GRANT ALL ON FUNCTION public.gettransactionid() TO pbrewer;
GRANT ALL ON FUNCTION public.gettransactionid() TO "Webgroup";
          public          postgres    false    1010            �           1255    64906    hasbbox(public.geometry)    FUNCTION     �   CREATE FUNCTION public.hasbbox(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_hasBBOX';
 /   DROP FUNCTION public.hasbbox(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64907 '   interiorringn(public.geometry, integer)    FUNCTION     �   CREATE FUNCTION public.interiorringn(public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_interiorringn_polygon';
 >   DROP FUNCTION public.interiorringn(public.geometry, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64908 .   intersection(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.intersection(public.geometry, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'ST_Intersection';
 E   DROP FUNCTION public.intersection(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64909 ,   intersects(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.intersects(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'ST_Intersects';
 C   DROP FUNCTION public.intersects(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64910    isadmin(integer)    FUNCTION     �   CREATE FUNCTION public.isadmin(securityuserid integer) RETURNS boolean
    LANGUAGE sql
    AS $_$select 
case when securitygroupsbyuser=1 
then true 
else null 
end 
as isadmin 
from securitygroupsbyuser($1) 
where securitygroupsbyuser=1;$_$;
 6   DROP FUNCTION public.isadmin(securityuserid integer);
       public          postgres    false            q           0    0 (   FUNCTION isadmin(securityuserid integer)    ACL     L   GRANT ALL ON FUNCTION public.isadmin(securityuserid integer) TO "Webgroup";
          public          postgres    false    1447            �           1255    64911    isclosed(public.geometry)    FUNCTION     �   CREATE FUNCTION public.isclosed(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_isclosed';
 0   DROP FUNCTION public.isclosed(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64912    isempty(public.geometry)    FUNCTION     �   CREATE FUNCTION public.isempty(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_isempty';
 /   DROP FUNCTION public.isempty(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64913    isring(public.geometry)    FUNCTION     �   CREATE FUNCTION public.isring(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'isring';
 .   DROP FUNCTION public.isring(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64914    issimple(public.geometry)    FUNCTION     �   CREATE FUNCTION public.issimple(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'issimple';
 0   DROP FUNCTION public.issimple(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64915    isvalid(public.geometry)    FUNCTION     �   CREATE FUNCTION public.isvalid(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'isvalid';
 /   DROP FUNCTION public.isvalid(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64916    length(public.geometry)    FUNCTION     �   CREATE FUNCTION public.length(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_length_linestring';
 .   DROP FUNCTION public.length(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64917    length2d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.length2d(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_length2d_linestring';
 0   DROP FUNCTION public.length2d(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64918 3   length2d_spheroid(public.geometry, public.spheroid)    FUNCTION     �   CREATE FUNCTION public.length2d_spheroid(public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'LWGEOM_length2d_ellipsoid';
 J   DROP FUNCTION public.length2d_spheroid(public.geometry, public.spheroid);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3            �           1255    64919    length3d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.length3d(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_length_linestring';
 0   DROP FUNCTION public.length3d(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64920 3   length3d_spheroid(public.geometry, public.spheroid)    FUNCTION     �   CREATE FUNCTION public.length3d_spheroid(public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_length_ellipsoid_linestring';
 J   DROP FUNCTION public.length3d_spheroid(public.geometry, public.spheroid);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3            �           1255    64921 1   length_spheroid(public.geometry, public.spheroid)    FUNCTION     �   CREATE FUNCTION public.length_spheroid(public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'LWGEOM_length_ellipsoid_linestring';
 H   DROP FUNCTION public.length_spheroid(public.geometry, public.spheroid);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3            �           1255    64922 9   line_interpolate_point(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.line_interpolate_point(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_line_interpolate_point';
 P   DROP FUNCTION public.line_interpolate_point(public.geometry, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64923 3   line_locate_point(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.line_locate_point(public.geometry, public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_line_locate_point';
 J   DROP FUNCTION public.line_locate_point(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64924 C   line_substring(public.geometry, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.line_substring(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_line_substring';
 Z   DROP FUNCTION public.line_substring(public.geometry, double precision, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64925 #   linefrommultipoint(public.geometry)    FUNCTION     �   CREATE FUNCTION public.linefrommultipoint(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_line_from_mpoint';
 :   DROP FUNCTION public.linefrommultipoint(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64926    linefromtext(text)    FUNCTION     �   CREATE FUNCTION public.linefromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1)) = 'LINESTRING'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$;
 )   DROP FUNCTION public.linefromtext(text);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64927    linefromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.linefromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1, $2)) = 'LINESTRING'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$;
 2   DROP FUNCTION public.linefromtext(text, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64928    linefromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.linefromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'LINESTRING'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;
 )   DROP FUNCTION public.linefromwkb(bytea);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64929    linefromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.linefromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'LINESTRING'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;
 2   DROP FUNCTION public.linefromwkb(bytea, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64930    linemerge(public.geometry)    FUNCTION     �   CREATE FUNCTION public.linemerge(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'linemerge';
 1   DROP FUNCTION public.linemerge(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64931    linestringfromtext(text)    FUNCTION     �   CREATE FUNCTION public.linestringfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT LineFromText($1)$_$;
 /   DROP FUNCTION public.linestringfromtext(text);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64932 !   linestringfromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.linestringfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT LineFromText($1, $2)$_$;
 8   DROP FUNCTION public.linestringfromtext(text, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64933    linestringfromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.linestringfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'LINESTRING'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;
 /   DROP FUNCTION public.linestringfromwkb(bytea);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64934 !   linestringfromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.linestringfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'LINESTRING'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;
 8   DROP FUNCTION public.linestringfromwkb(bytea, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64935 7   locate_along_measure(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.locate_along_measure(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$ SELECT ST_LocateBetween($1, $2, $2) $_$;
 N   DROP FUNCTION public.locate_along_measure(public.geometry, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64936 L   locate_between_measures(public.geometry, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.locate_between_measures(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_locate_between_m';
 c   DROP FUNCTION public.locate_between_measures(public.geometry, double precision, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            r           0    0 "   FUNCTION lockrow(text, text, text)    ACL     �   GRANT ALL ON FUNCTION public.lockrow(text, text, text) TO pbrewer;
GRANT ALL ON FUNCTION public.lockrow(text, text, text) TO "Webgroup";
          public          postgres    false    1004            s           0    0 (   FUNCTION lockrow(text, text, text, text)    ACL     �   GRANT ALL ON FUNCTION public.lockrow(text, text, text, text) TO pbrewer;
GRANT ALL ON FUNCTION public.lockrow(text, text, text, text) TO "Webgroup";
          public          postgres    false    1003            t           0    0 ?   FUNCTION lockrow(text, text, text, timestamp without time zone)    ACL     �   GRANT ALL ON FUNCTION public.lockrow(text, text, text, timestamp without time zone) TO pbrewer;
GRANT ALL ON FUNCTION public.lockrow(text, text, text, timestamp without time zone) TO "Webgroup";
          public          postgres    false    1005            u           0    0 E   FUNCTION lockrow(text, text, text, text, timestamp without time zone)    ACL     �   GRANT ALL ON FUNCTION public.lockrow(text, text, text, text, timestamp without time zone) TO pbrewer;
GRANT ALL ON FUNCTION public.lockrow(text, text, text, text, timestamp without time zone) TO "Webgroup";
          public          postgres    false    1002            v           0    0 "   FUNCTION longtransactionsenabled()    ACL     �   GRANT ALL ON FUNCTION public.longtransactionsenabled() TO pbrewer;
GRANT ALL ON FUNCTION public.longtransactionsenabled() TO "Webgroup";
          public          postgres    false    1012            �           1255    64937    m(public.geometry)    FUNCTION     �   CREATE FUNCTION public.m(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_m_point';
 )   DROP FUNCTION public.m(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64938 +   makebox2d(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.makebox2d(public.geometry, public.geometry) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX2D_construct';
 B   DROP FUNCTION public.makebox2d(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64939 +   makebox3d(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.makebox3d(public.geometry, public.geometry) RETURNS public.box3d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_construct';
 B   DROP FUNCTION public.makebox3d(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64940 *   makeline(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.makeline(public.geometry, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_makeline';
 A   DROP FUNCTION public.makeline(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64941 "   makeline_garray(public.geometry[])    FUNCTION     �   CREATE FUNCTION public.makeline_garray(public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_makeline_garray';
 9   DROP FUNCTION public.makeline_garray(public.geometry[]);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64942 -   makepoint(double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.makepoint(double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_makepoint';
 D   DROP FUNCTION public.makepoint(double precision, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64943 ?   makepoint(double precision, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.makepoint(double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_makepoint';
 V   DROP FUNCTION public.makepoint(double precision, double precision, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64944 Q   makepoint(double precision, double precision, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.makepoint(double precision, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_makepoint';
 h   DROP FUNCTION public.makepoint(double precision, double precision, double precision, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64945 @   makepointm(double precision, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.makepointm(double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_makepoint3dm';
 W   DROP FUNCTION public.makepointm(double precision, double precision, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64946    makepolygon(public.geometry)    FUNCTION     �   CREATE FUNCTION public.makepolygon(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_makepoly';
 3   DROP FUNCTION public.makepolygon(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64947 /   makepolygon(public.geometry, public.geometry[])    FUNCTION     �   CREATE FUNCTION public.makepolygon(public.geometry, public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_makepoly';
 F   DROP FUNCTION public.makepolygon(public.geometry, public.geometry[]);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64948 .   max_distance(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.max_distance(public.geometry, public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_maxdistance2d_linestring';
 E   DROP FUNCTION public.max_distance(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64949    mem_size(public.geometry)    FUNCTION     �   CREATE FUNCTION public.mem_size(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_mem_size';
 0   DROP FUNCTION public.mem_size(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64950    mlinefromtext(text)    FUNCTION     �   CREATE FUNCTION public.mlinefromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1)) = 'MULTILINESTRING'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$;
 *   DROP FUNCTION public.mlinefromtext(text);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64951    mlinefromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.mlinefromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE
	WHEN geometrytype(GeomFromText($1, $2)) = 'MULTILINESTRING'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$;
 3   DROP FUNCTION public.mlinefromtext(text, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64952    mlinefromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.mlinefromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'MULTILINESTRING'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;
 *   DROP FUNCTION public.mlinefromwkb(bytea);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64953    mlinefromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.mlinefromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'MULTILINESTRING'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;
 3   DROP FUNCTION public.mlinefromwkb(bytea, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64954    mpointfromtext(text)    FUNCTION     �   CREATE FUNCTION public.mpointfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1)) = 'MULTIPOINT'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$;
 +   DROP FUNCTION public.mpointfromtext(text);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64955    mpointfromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.mpointfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1,$2)) = 'MULTIPOINT'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$;
 4   DROP FUNCTION public.mpointfromtext(text, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64956    mpointfromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.mpointfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'MULTIPOINT'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;
 +   DROP FUNCTION public.mpointfromwkb(bytea);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64957    mpointfromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.mpointfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1,$2)) = 'MULTIPOINT'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;
 4   DROP FUNCTION public.mpointfromwkb(bytea, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64958    mpolyfromtext(text)    FUNCTION     �   CREATE FUNCTION public.mpolyfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1)) = 'MULTIPOLYGON'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$;
 *   DROP FUNCTION public.mpolyfromtext(text);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64959    mpolyfromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.mpolyfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1, $2)) = 'MULTIPOLYGON'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$;
 3   DROP FUNCTION public.mpolyfromtext(text, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64960    mpolyfromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.mpolyfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'MULTIPOLYGON'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;
 *   DROP FUNCTION public.mpolyfromwkb(bytea);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64961    mpolyfromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.mpolyfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'MULTIPOLYGON'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;
 3   DROP FUNCTION public.mpolyfromwkb(bytea, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64962    multi(public.geometry)    FUNCTION     �   CREATE FUNCTION public.multi(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_force_multi';
 -   DROP FUNCTION public.multi(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64963    multilinefromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.multilinefromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'MULTILINESTRING'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;
 .   DROP FUNCTION public.multilinefromwkb(bytea);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64964     multilinefromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.multilinefromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'MULTILINESTRING'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;
 7   DROP FUNCTION public.multilinefromwkb(bytea, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64965    multilinestringfromtext(text)    FUNCTION     �   CREATE FUNCTION public.multilinestringfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_MLineFromText($1)$_$;
 4   DROP FUNCTION public.multilinestringfromtext(text);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64966 &   multilinestringfromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.multilinestringfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT MLineFromText($1, $2)$_$;
 =   DROP FUNCTION public.multilinestringfromtext(text, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64967    multipointfromtext(text)    FUNCTION     �   CREATE FUNCTION public.multipointfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT MPointFromText($1)$_$;
 /   DROP FUNCTION public.multipointfromtext(text);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64968 !   multipointfromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.multipointfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT MPointFromText($1, $2)$_$;
 8   DROP FUNCTION public.multipointfromtext(text, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64969    multipointfromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.multipointfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'MULTIPOINT'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;
 /   DROP FUNCTION public.multipointfromwkb(bytea);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64970 !   multipointfromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.multipointfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1,$2)) = 'MULTIPOINT'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;
 8   DROP FUNCTION public.multipointfromwkb(bytea, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64971    multipolyfromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.multipolyfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'MULTIPOLYGON'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;
 .   DROP FUNCTION public.multipolyfromwkb(bytea);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64972     multipolyfromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.multipolyfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'MULTIPOLYGON'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;
 7   DROP FUNCTION public.multipolyfromwkb(bytea, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64973    multipolygonfromtext(text)    FUNCTION     �   CREATE FUNCTION public.multipolygonfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT MPolyFromText($1)$_$;
 1   DROP FUNCTION public.multipolygonfromtext(text);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64974 #   multipolygonfromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.multipolygonfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT MPolyFromText($1, $2)$_$;
 :   DROP FUNCTION public.multipolygonfromtext(text, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64975    ndims(public.geometry)    FUNCTION     �   CREATE FUNCTION public.ndims(public.geometry) RETURNS smallint
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_ndims';
 -   DROP FUNCTION public.ndims(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64976    noop(public.geometry)    FUNCTION     �   CREATE FUNCTION public.noop(public.geometry) RETURNS public.geometry
    LANGUAGE c STRICT
    AS '$libdir/postgis-3', 'LWGEOM_noop';
 ,   DROP FUNCTION public.noop(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64977 8   normal_rand(integer, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.normal_rand(integer, double precision, double precision) RETURNS SETOF double precision
    LANGUAGE c STRICT
    AS '$libdir/tablefunc', 'normal_rand';
 O   DROP FUNCTION public.normal_rand(integer, double precision, double precision);
       public          postgres    false            �           1255    64978    npoints(public.geometry)    FUNCTION     �   CREATE FUNCTION public.npoints(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_npoints';
 /   DROP FUNCTION public.npoints(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64979    nrings(public.geometry)    FUNCTION     �   CREATE FUNCTION public.nrings(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_nrings';
 .   DROP FUNCTION public.nrings(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64980    numgeometries(public.geometry)    FUNCTION     �   CREATE FUNCTION public.numgeometries(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_numgeometries_collection';
 5   DROP FUNCTION public.numgeometries(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64981     numinteriorring(public.geometry)    FUNCTION     �   CREATE FUNCTION public.numinteriorring(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_numinteriorrings_polygon';
 7   DROP FUNCTION public.numinteriorring(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64982 !   numinteriorrings(public.geometry)    FUNCTION     �   CREATE FUNCTION public.numinteriorrings(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_numinteriorrings_polygon';
 8   DROP FUNCTION public.numinteriorrings(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64983    numpoints(public.geometry)    FUNCTION     �   CREATE FUNCTION public.numpoints(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_numpoints_linestring';
 1   DROP FUNCTION public.numpoints(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64984 *   overlaps(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public."overlaps"(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'overlaps';
 C   DROP FUNCTION public."overlaps"(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64985    perimeter2d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.perimeter2d(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_perimeter2d_poly';
 3   DROP FUNCTION public.perimeter2d(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64986    perimeter3d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.perimeter3d(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_perimeter_poly';
 3   DROP FUNCTION public.perimeter3d(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64987 (   plpgsql_test_external(character varying)    FUNCTION     �   CREATE FUNCTION public.plpgsql_test_external(character varying) RETURNS character varying
    LANGUAGE plpgsql
    AS $_$
BEGIN
   return $1 || ' from plpgsql!';
END;
$_$;
 ?   DROP FUNCTION public.plpgsql_test_external(character varying);
       public          postgres    false            �           1255    64988 Z   point_inside_circle(public.geometry, double precision, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.point_inside_circle(public.geometry, double precision, double precision, double precision) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_inside_circle_point';
 q   DROP FUNCTION public.point_inside_circle(public.geometry, double precision, double precision, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64989    pointfromtext(text)    FUNCTION     �   CREATE FUNCTION public.pointfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1)) = 'POINT'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$;
 *   DROP FUNCTION public.pointfromtext(text);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64990    pointfromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.pointfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1, $2)) = 'POINT'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$;
 3   DROP FUNCTION public.pointfromtext(text, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64991    pointfromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.pointfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'POINT'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;
 *   DROP FUNCTION public.pointfromwkb(bytea);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64992    pointfromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.pointfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromWKB($1, $2)) = 'POINT'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;
 3   DROP FUNCTION public.pointfromwkb(bytea, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64993     pointn(public.geometry, integer)    FUNCTION     �   CREATE FUNCTION public.pointn(public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_pointn_linestring';
 7   DROP FUNCTION public.pointn(public.geometry, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64994    pointonsurface(public.geometry)    FUNCTION     �   CREATE FUNCTION public.pointonsurface(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'pointonsurface';
 6   DROP FUNCTION public.pointonsurface(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    64995    polyfromtext(text)    FUNCTION     �   CREATE FUNCTION public.polyfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1)) = 'POLYGON'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$;
 )   DROP FUNCTION public.polyfromtext(text);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64996    polyfromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.polyfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1, $2)) = 'POLYGON'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$;
 2   DROP FUNCTION public.polyfromtext(text, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64997    polyfromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.polyfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'POLYGON'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;
 )   DROP FUNCTION public.polyfromwkb(bytea);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    64998    polyfromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.polyfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'POLYGON'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;
 2   DROP FUNCTION public.polyfromwkb(bytea, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3                        1255    64999    polygonfromtext(text)    FUNCTION     �   CREATE FUNCTION public.polygonfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT PolyFromText($1)$_$;
 ,   DROP FUNCTION public.polygonfromtext(text);
       public          tellervo    false    3    3    3    3    3    3    3    3                       1255    65000    polygonfromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.polygonfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT PolyFromText($1, $2)$_$;
 5   DROP FUNCTION public.polygonfromtext(text, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3                       1255    65001    polygonfromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.polygonfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'POLYGON'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;
 ,   DROP FUNCTION public.polygonfromwkb(bytea);
       public          tellervo    false    3    3    3    3    3    3    3    3                       1255    65002    polygonfromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.polygonfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1,$2)) = 'POLYGON'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;
 5   DROP FUNCTION public.polygonfromwkb(bytea, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3                       1255    65003 $   polygonize_garray(public.geometry[])    FUNCTION     �   CREATE FUNCTION public.polygonize_garray(public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'polygonize_garray';
 ;   DROP FUNCTION public.polygonize_garray(public.geometry[]);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            w           0    0    FUNCTION postgis_full_version()    ACL     �   GRANT ALL ON FUNCTION public.postgis_full_version() TO pbrewer;
GRANT ALL ON FUNCTION public.postgis_full_version() TO "Webgroup";
          public          postgres    false    718            x           0    0    FUNCTION postgis_geos_version()    ACL     �   GRANT ALL ON FUNCTION public.postgis_geos_version() TO pbrewer;
GRANT ALL ON FUNCTION public.postgis_geos_version() TO "Webgroup";
          public          postgres    false    708            y           0    0 !   FUNCTION postgis_lib_build_date()    ACL     �   GRANT ALL ON FUNCTION public.postgis_lib_build_date() TO pbrewer;
GRANT ALL ON FUNCTION public.postgis_lib_build_date() TO "Webgroup";
          public          postgres    false    714            z           0    0    FUNCTION postgis_lib_version()    ACL     �   GRANT ALL ON FUNCTION public.postgis_lib_version() TO pbrewer;
GRANT ALL ON FUNCTION public.postgis_lib_version() TO "Webgroup";
          public          postgres    false    706            {           0    0    FUNCTION postgis_proj_version()    ACL     �   GRANT ALL ON FUNCTION public.postgis_proj_version() TO pbrewer;
GRANT ALL ON FUNCTION public.postgis_proj_version() TO "Webgroup";
          public          postgres    false    703            |           0    0 %   FUNCTION postgis_scripts_build_date()    ACL     �   GRANT ALL ON FUNCTION public.postgis_scripts_build_date() TO pbrewer;
GRANT ALL ON FUNCTION public.postgis_scripts_build_date() TO "Webgroup";
          public          postgres    false    713            }           0    0 $   FUNCTION postgis_scripts_installed()    ACL     �   GRANT ALL ON FUNCTION public.postgis_scripts_installed() TO pbrewer;
GRANT ALL ON FUNCTION public.postgis_scripts_installed() TO "Webgroup";
          public          postgres    false    705            ~           0    0 #   FUNCTION postgis_scripts_released()    ACL     �   GRANT ALL ON FUNCTION public.postgis_scripts_released() TO pbrewer;
GRANT ALL ON FUNCTION public.postgis_scripts_released() TO "Webgroup";
          public          postgres    false    707                       0    0    FUNCTION postgis_version()    ACL     y   GRANT ALL ON FUNCTION public.postgis_version() TO pbrewer;
GRANT ALL ON FUNCTION public.postgis_version() TO "Webgroup";
          public          postgres    false    701                       1255    65004    probe_geometry_columns()    FUNCTION       CREATE FUNCTION public.probe_geometry_columns() RETURNS text
    LANGUAGE plpgsql
    AS $$
DECLARE
	inserted integer;
	oldcount integer;
	probed integer;
	stale integer;
BEGIN


	RETURN 'This function is obsolete now that geometry_columns is a view';
END

$$;
 /   DROP FUNCTION public.probe_geometry_columns();
       public          tellervo    false                       1255    65005 ?   qappvmeasurementresultreadingopsum1(character varying, integer)    FUNCTION     �  CREATE FUNCTION public.qappvmeasurementresultreadingopsum1(character varying, integer) RETURNS SETOF record
    LANGUAGE sql
    AS $_$
   SELECT tblVMeasurementReadingResult.*, $2 + 1 AS RelYearPlusOne 
      FROM tblVMeasurementResult INNER JOIN tblVMeasurementReadingResult ON
      tblVMeasurementResult.VMeasurementResultID = tblVMeasurementReadingResult.VMeasurementResultID
      WHERE tblVMeasurementResult.VMeasurementResultGroupID=$1
      ORDER BY tblVMeasurementReadingResult.RelYear;
$_$;
 V   DROP FUNCTION public.qappvmeasurementresultreadingopsum1(character varying, integer);
       public          postgres    false                       1255    65006 (   relate(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.relate(public.geometry, public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'relate_full';
 ?   DROP FUNCTION public.relate(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3                       1255    65007 .   relate(public.geometry, public.geometry, text)    FUNCTION     �   CREATE FUNCTION public.relate(public.geometry, public.geometry, text) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'relate_pattern';
 E   DROP FUNCTION public.relate(public.geometry, public.geometry, text);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            	           1255    65008 %   removepoint(public.geometry, integer)    FUNCTION     �   CREATE FUNCTION public.removepoint(public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_removepoint';
 <   DROP FUNCTION public.removepoint(public.geometry, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            
           1255    65009 #   rename_geometry_table_constraints()    FUNCTION     �   CREATE FUNCTION public.rename_geometry_table_constraints() RETURNS text
    LANGUAGE sql IMMUTABLE
    AS $$
SELECT 'rename_geometry_table_constraint() is obsoleted'::text
$$;
 :   DROP FUNCTION public.rename_geometry_table_constraints();
       public          tellervo    false                       1255    65010    reverse(public.geometry)    FUNCTION     �   CREATE FUNCTION public.reverse(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_reverse';
 /   DROP FUNCTION public.reverse(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3                       1255    65011 )   rotate(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.rotate(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_rotateZ($1, $2)$_$;
 @   DROP FUNCTION public.rotate(public.geometry, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3                       1255    65012 *   rotatex(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.rotatex(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_affine($1, 1, 0, 0, 0, cos($2), -sin($2), 0, sin($2), cos($2), 0, 0, 0)$_$;
 A   DROP FUNCTION public.rotatex(public.geometry, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3                       1255    65013 *   rotatey(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.rotatey(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_affine($1,  cos($2), 0, sin($2),  0, 1, 0,  -sin($2), 0, cos($2), 0,  0, 0)$_$;
 A   DROP FUNCTION public.rotatey(public.geometry, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3                       1255    65014 *   rotatez(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.rotatez(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_affine($1,  cos($2), -sin($2), 0,  sin($2), cos($2), 0,  0, 0, 1,  0, 0, 0)$_$;
 A   DROP FUNCTION public.rotatez(public.geometry, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3                       1255    65015 :   scale(public.geometry, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.scale(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_scale($1, $2, $3, 1)$_$;
 Q   DROP FUNCTION public.scale(public.geometry, double precision, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3                       1255    65016 L   scale(public.geometry, double precision, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.scale(public.geometry, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_affine($1,  $2, 0, 0,  0, $3, 0,  0, 0, $4,  0, 0, 0)$_$;
 c   DROP FUNCTION public.scale(public.geometry, double precision, double precision, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3                       1255    65017 7   se_envelopesintersect(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.se_envelopesintersect(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT $1 && $2
	$_$;
 N   DROP FUNCTION public.se_envelopesintersect(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3                       1255    65018    se_is3d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.se_is3d(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_hasz';
 /   DROP FUNCTION public.se_is3d(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3                       1255    65019    se_ismeasured(public.geometry)    FUNCTION     �   CREATE FUNCTION public.se_ismeasured(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_hasm';
 5   DROP FUNCTION public.se_ismeasured(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3                       1255    65020 1   se_locatealong(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.se_locatealong(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$ SELECT SE_LocateBetween($1, $2, $2) $_$;
 H   DROP FUNCTION public.se_locatealong(public.geometry, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3                       1255    65021 E   se_locatebetween(public.geometry, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.se_locatebetween(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_locate_between_m';
 \   DROP FUNCTION public.se_locatebetween(public.geometry, double precision, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3                       1255    65022    se_m(public.geometry)    FUNCTION     �   CREATE FUNCTION public.se_m(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_m_point';
 ,   DROP FUNCTION public.se_m(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3                       1255    65023    se_z(public.geometry)    FUNCTION     �   CREATE FUNCTION public.se_z(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_z_point';
 ,   DROP FUNCTION public.se_z(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3                       1255    65024 )   securitygroupelementmaster(integer, uuid)    FUNCTION     �  CREATE FUNCTION public.securitygroupelementmaster(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select securitygrouppermissiveelementcombined.* 
from securitygrouppermissiveelementcombined($1, $2) left join securitygrouprestrictiveelementcombined($2) on securitygrouppermissiveelementcombined.objectid = securitygrouprestrictiveelementcombined.objectid 
where (((securitygrouprestrictiveelementcombined.objectid) is null));$_$;
 d   DROP FUNCTION public.securitygroupelementmaster(securitypermissionid integer, securityuserid uuid);
       public          tellervo    false    2230                       1255    65025 +   securitygroupobjectmaster(integer, integer)    FUNCTION     �  CREATE FUNCTION public.securitygroupobjectmaster(securitypermissionid integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select securitygrouppermissiveobjectcombined.* 
from securitygrouppermissiveobjectcombined($1, $2) left join securitygrouprestrictiveobjectcombined($2) on securitygrouppermissiveobjectcombined.objectid = securitygrouprestrictiveobjectcombined.objectid 
where (((securitygrouprestrictiveobjectcombined.objectid) is null));$_$;
 f   DROP FUNCTION public.securitygroupobjectmaster(securitypermissionid integer, securityuserid integer);
       public          postgres    false    2224            �           0    0 X   FUNCTION securitygroupobjectmaster(securitypermissionid integer, securityuserid integer)    ACL     |   GRANT ALL ON FUNCTION public.securitygroupobjectmaster(securitypermissionid integer, securityuserid integer) TO "Webgroup";
          public          postgres    false    1562                       1255    65026 (   securitygroupobjectmaster(integer, uuid)    FUNCTION     �  CREATE FUNCTION public.securitygroupobjectmaster(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select securitygrouppermissiveobjectcombined.* 
from securitygrouppermissiveobjectcombined($1, $2) left join securitygrouprestrictiveobjectcombined($2) on securitygrouppermissiveobjectcombined.objectid = securitygrouprestrictiveobjectcombined.objectid 
where (((securitygrouprestrictiveobjectcombined.objectid) is null));$_$;
 c   DROP FUNCTION public.securitygroupobjectmaster(securitypermissionid integer, securityuserid uuid);
       public          tellervo    false    2230                       1255    65027 .   securitygrouppermissivedefault1(integer, uuid)    FUNCTION     e  CREATE FUNCTION public.securitygrouppermissivedefault1(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandentityid
    LANGUAGE sql
    AS $_$SELECT tblsecurityuser.securityuserid, tblsecuritydefault.securitydefaultid
FROM tblsecurityuser 
INNER JOIN ((tblsecuritygroup 
INNER JOIN tblsecurityusermembership ON tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) 
INNER JOIN tblsecuritydefault ON tblsecuritygroup.securitygroupid = tblsecuritydefault.securitygroupid) ON tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecuritydefault.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
GROUP BY tblsecurityuser.securityuserid, tblsecuritydefault.securitydefaultid
HAVING (((tblsecurityuser.securityuserid)=$2));$_$;
 i   DROP FUNCTION public.securitygrouppermissivedefault1(securitypermissionid integer, securityuserid uuid);
       public          tellervo    false    2227                       1255    65028 .   securitygrouppermissivedefault2(integer, uuid)    FUNCTION     r  CREATE FUNCTION public.securitygrouppermissivedefault2(securitypermission integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandentityid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecuritydefault.securitydefaultid
from tblsecurityuser 
inner join ((tblsecuritygroup 
inner join ((tblsecuritygroupmembership 
inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) 
inner join tblsecuritydefault on tblsecuritygroup_1.securitygroupid = tblsecuritydefault.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) 
inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecuritydefault.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
group by tblsecurityuser.securityuserid, tblsecuritydefault.securitydefaultid
having (((tblsecurityuser.securityuserid)=$2));$_$;
 g   DROP FUNCTION public.securitygrouppermissivedefault2(securitypermission integer, securityuserid uuid);
       public          tellervo    false    2227                       1255    65029 .   securitygrouppermissivedefault3(integer, uuid)    FUNCTION     �  CREATE FUNCTION public.securitygrouppermissivedefault3(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandentityid
    LANGUAGE sql
    AS $_$
SELECT tblsecurityuser.securityuserid, tblsecuritydefault.securitydefaultid
FROM tblsecurityuser 
inner join ((tblsecuritydefault 
inner join (tblsecuritygroup 
inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 
inner join (tblsecuritygroupmembership 
inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) 
inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecuritydefault.securitygroupid = tblsecuritygroup_2.securitygroupid) 
inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecuritydefault.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
group by tblsecurityuser.securityuserid, tblsecuritydefault.securitydefaultid
having (((tblsecurityuser.securityuserid)=$2));$_$;
 i   DROP FUNCTION public.securitygrouppermissivedefault3(securitypermissionid integer, securityuserid uuid);
       public          tellervo    false    2227                       1255    65030 8   securitygrouppermissivedefaultcombined(integer, integer)    FUNCTION     U  CREATE FUNCTION public.securitygrouppermissivedefaultcombined(securitypermissionid integer, securityuserid integer) RETURNS SETOF integer
    LANGUAGE sql
    AS $_$SELECT * from securitygrouppermissivedefault1($1,$2)
UNION
SELECT * from securitygrouppermissivedefault2($1,$2)
UNION SELECT * from securitygrouppermissivedefault3($1,$2);$_$;
 s   DROP FUNCTION public.securitygrouppermissivedefaultcombined(securitypermissionid integer, securityuserid integer);
       public          postgres    false            �           0    0 e   FUNCTION securitygrouppermissivedefaultcombined(securitypermissionid integer, securityuserid integer)    ACL     �   GRANT ALL ON FUNCTION public.securitygrouppermissivedefaultcombined(securitypermissionid integer, securityuserid integer) TO "Webgroup";
          public          postgres    false    1567                        1255    65031 5   securitygrouppermissivedefaultcombined(integer, uuid)    FUNCTION     m  CREATE FUNCTION public.securitygrouppermissivedefaultcombined(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandentityid
    LANGUAGE sql
    AS $_$SELECT * from securitygrouppermissivedefault1($1,$2)
UNION
SELECT * from securitygrouppermissivedefault2($1,$2)
UNION SELECT * from securitygrouppermissivedefault3($1,$2);$_$;
 p   DROP FUNCTION public.securitygrouppermissivedefaultcombined(securitypermissionid integer, securityuserid uuid);
       public          tellervo    false    2227            !           1255    65032 .   securitygrouppermissiveelement1(integer, uuid)    FUNCTION     W  CREATE FUNCTION public.securitygrouppermissiveelement1(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$SELECT tblsecurityuser.securityuserid, tblsecurityelement.elementid
FROM tblsecurityuser 
INNER JOIN ((tblsecuritygroup 
INNER JOIN tblsecurityusermembership ON tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) 
INNER JOIN tblsecurityelement ON tblsecuritygroup.securitygroupid = tblsecurityelement.securitygroupid) ON tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityelement.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
GROUP BY tblsecurityuser.securityuserid, tblsecurityelement.elementid
HAVING (((tblsecurityuser.securityuserid)=$2));$_$;
 i   DROP FUNCTION public.securitygrouppermissiveelement1(securitypermissionid integer, securityuserid uuid);
       public          tellervo    false    2230            "           1255    65033 .   securitygrouppermissiveelement2(integer, uuid)    FUNCTION     e  CREATE FUNCTION public.securitygrouppermissiveelement2(securitypermission integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$
select tblsecurityuser.securityuserid, tblsecurityelement.elementid
from tblsecurityuser 
inner join ((tblsecuritygroup 
inner join ((tblsecuritygroupmembership 
inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) 
inner join tblsecurityelement on tblsecuritygroup_1.securitygroupid = tblsecurityelement.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) 
inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityelement.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
group by tblsecurityuser.securityuserid, tblsecurityelement.elementid
having (((tblsecurityuser.securityuserid)=$2));$_$;
 g   DROP FUNCTION public.securitygrouppermissiveelement2(securitypermission integer, securityuserid uuid);
       public          tellervo    false    2230            #           1255    65034 .   securitygrouppermissiveelement3(integer, uuid)    FUNCTION     �  CREATE FUNCTION public.securitygrouppermissiveelement3(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityelement.elementid
from tblsecurityuser 
inner join ((tblsecurityelement 
inner join (tblsecuritygroup 
inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 
inner join (tblsecuritygroupmembership 
inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) 
inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecurityelement.securitygroupid = tblsecuritygroup_2.securitygroupid) 
inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityelement.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
group by tblsecurityuser.securityuserid, tblsecurityelement.elementid
having (((tblsecurityuser.securityuserid)=$2));$_$;
 i   DROP FUNCTION public.securitygrouppermissiveelement3(securitypermissionid integer, securityuserid uuid);
       public          tellervo    false    2230            $           1255    65035 5   securitygrouppermissiveelementcombined(integer, uuid)    FUNCTION     o  CREATE FUNCTION public.securitygrouppermissiveelementcombined(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$SELECT * from securitygrouppermissiveelement1($1,$2)
UNION
SELECT * from securitygrouppermissiveelement2($1,$2)
UNION SELECT * from securitygrouppermissiveelement3($1,$2);$_$;
 p   DROP FUNCTION public.securitygrouppermissiveelementcombined(securitypermissionid integer, securityuserid uuid);
       public          tellervo    false    2230            %           1255    65036 0   securitygrouppermissiveobject1(integer, integer)    FUNCTION     L  CREATE FUNCTION public.securitygrouppermissiveobject1(securitypermissionid integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$SELECT tblsecurityuser.securityuserid, tblsecurityobject.objectid
FROM tblsecurityuser 
INNER JOIN ((tblsecuritygroup 
INNER JOIN tblsecurityusermembership ON tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) 
INNER JOIN tblsecurityobject ON tblsecuritygroup.securitygroupid = tblsecurityobject.securitygroupid) ON tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityobject.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
GROUP BY tblsecurityuser.securityuserid, tblsecurityobject.objectid
HAVING (((tblsecurityuser.securityuserid)=$2));$_$;
 k   DROP FUNCTION public.securitygrouppermissiveobject1(securitypermissionid integer, securityuserid integer);
       public          postgres    false    2224            �           0    0 ]   FUNCTION securitygrouppermissiveobject1(securitypermissionid integer, securityuserid integer)    COMMENT     �  COMMENT ON FUNCTION public.securitygrouppermissiveobject1(securitypermissionid integer, securityuserid integer) IS 'This is part of a series of functions:
securitygrouppermissiveobject1, securitygrouppermissiveobject2, securitygrouppermissiveobject3 and securitygrouppermissiveobjectcombined.  Together they work like a 2 tier recursive function and as such will probably be replaced in the future.  Together they bring together all the objects that a specified user has the specified permission type on.  See also securitygrouprestrictiveobject... and securitygroupobjectmaster.  Similar functions have been created for trees and vmeasurements.  ';
          public          postgres    false    1573            �           0    0 ]   FUNCTION securitygrouppermissiveobject1(securitypermissionid integer, securityuserid integer)    ACL     �   GRANT ALL ON FUNCTION public.securitygrouppermissiveobject1(securitypermissionid integer, securityuserid integer) TO "Webgroup";
          public          postgres    false    1573            &           1255    65037 -   securitygrouppermissiveobject1(integer, uuid)    FUNCTION     O  CREATE FUNCTION public.securitygrouppermissiveobject1(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$SELECT tblsecurityuser.securityuserid, tblsecurityobject.objectid
FROM tblsecurityuser 
INNER JOIN ((tblsecuritygroup 
INNER JOIN tblsecurityusermembership ON tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) 
INNER JOIN tblsecurityobject ON tblsecuritygroup.securitygroupid = tblsecurityobject.securitygroupid) ON tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityobject.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
GROUP BY tblsecurityuser.securityuserid, tblsecurityobject.objectid
HAVING (((tblsecurityuser.securityuserid)=$2));$_$;
 h   DROP FUNCTION public.securitygrouppermissiveobject1(securitypermissionid integer, securityuserid uuid);
       public          tellervo    false    2230            '           1255    65038 0   securitygrouppermissiveobject2(integer, integer)    FUNCTION     Y  CREATE FUNCTION public.securitygrouppermissiveobject2(securitypermission integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityobject.objectid
from tblsecurityuser 
inner join ((tblsecuritygroup 
inner join ((tblsecuritygroupmembership 
inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) 
inner join tblsecurityobject on tblsecuritygroup_1.securitygroupid = tblsecurityobject.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) 
inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityobject.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
group by tblsecurityuser.securityuserid, tblsecurityobject.objectid
having (((tblsecurityuser.securityuserid)=$2));$_$;
 i   DROP FUNCTION public.securitygrouppermissiveobject2(securitypermission integer, securityuserid integer);
       public          postgres    false    2224            �           0    0 [   FUNCTION securitygrouppermissiveobject2(securitypermission integer, securityuserid integer)    COMMENT     �  COMMENT ON FUNCTION public.securitygrouppermissiveobject2(securitypermission integer, securityuserid integer) IS 'This is part of a series of functions:
securitygrouppermissiveobject1, securitygrouppermissiveobject2, securitygrouppermissiveobject3 and securitygrouppermissiveobjectcombined.  Together they work like a 2 tier recursive function and as such will probably be replaced in the future.  Together they bring together all the objects that a specified user has the specified permission type on.  See also securitygrouprestrictiveobject... and securitygroupobjectmaster.  Similar functions have been created for trees and vmeasurements.  ';
          public          postgres    false    1575            �           0    0 [   FUNCTION securitygrouppermissiveobject2(securitypermission integer, securityuserid integer)    ACL        GRANT ALL ON FUNCTION public.securitygrouppermissiveobject2(securitypermission integer, securityuserid integer) TO "Webgroup";
          public          postgres    false    1575            (           1255    65039 -   securitygrouppermissiveobject2(integer, uuid)    FUNCTION     \  CREATE FUNCTION public.securitygrouppermissiveobject2(securitypermission integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityobject.objectid
from tblsecurityuser 
inner join ((tblsecuritygroup 
inner join ((tblsecuritygroupmembership 
inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) 
inner join tblsecurityobject on tblsecuritygroup_1.securitygroupid = tblsecurityobject.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) 
inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityobject.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
group by tblsecurityuser.securityuserid, tblsecurityobject.objectid
having (((tblsecurityuser.securityuserid)=$2));$_$;
 f   DROP FUNCTION public.securitygrouppermissiveobject2(securitypermission integer, securityuserid uuid);
       public          tellervo    false    2230            )           1255    65040 0   securitygrouppermissiveobject3(integer, integer)    FUNCTION     �  CREATE FUNCTION public.securitygrouppermissiveobject3(securitypermissionid integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityobject.objectid
from tblsecurityuser 
inner join ((tblsecurityobject 
inner join (tblsecuritygroup 
inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 
inner join (tblsecuritygroupmembership 
inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) 
inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecurityobject.securitygroupid = tblsecuritygroup_2.securitygroupid) 
inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityobject.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
group by tblsecurityuser.securityuserid, tblsecurityobject.objectid
having (((tblsecurityuser.securityuserid)=$2));$_$;
 k   DROP FUNCTION public.securitygrouppermissiveobject3(securitypermissionid integer, securityuserid integer);
       public          postgres    false    2224            �           0    0 ]   FUNCTION securitygrouppermissiveobject3(securitypermissionid integer, securityuserid integer)    COMMENT     �  COMMENT ON FUNCTION public.securitygrouppermissiveobject3(securitypermissionid integer, securityuserid integer) IS 'This is part of a series of functions:
securitygrouppermissiveobject1, securitygrouppermissiveobject2, securitygrouppermissiveobject3 and securitygrouppermissiveobjectcombined.  Together they work like a 2 tier recursive function and as such will probably be replaced in the future.  Together they bring together all the objects that a specified user has the specified permission type on.  See also securitygrouprestrictiveobject... and securitygroupobjectmaster.  Similar functions have been created for trees and vmeasurements.  ';
          public          postgres    false    1577            �           0    0 ]   FUNCTION securitygrouppermissiveobject3(securitypermissionid integer, securityuserid integer)    ACL     �   GRANT ALL ON FUNCTION public.securitygrouppermissiveobject3(securitypermissionid integer, securityuserid integer) TO "Webgroup";
          public          postgres    false    1577            *           1255    65041 -   securitygrouppermissiveobject3(integer, uuid)    FUNCTION     �  CREATE FUNCTION public.securitygrouppermissiveobject3(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$
SELECT tblsecurityuser.securityuserid, tblsecurityobject.objectid
FROM tblsecurityuser 
inner join ((tblsecurityobject 
inner join (tblsecuritygroup 
inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 
inner join (tblsecuritygroupmembership 
inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) 
inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecurityobject.securitygroupid = tblsecuritygroup_2.securitygroupid) 
inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityobject.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
group by tblsecurityuser.securityuserid, tblsecurityobject.objectid
having (((tblsecurityuser.securityuserid)=$2));$_$;
 h   DROP FUNCTION public.securitygrouppermissiveobject3(securitypermissionid integer, securityuserid uuid);
       public          tellervo    false    2230            +           1255    65042 7   securitygrouppermissiveobjectcombined(integer, integer)    FUNCTION     h  CREATE FUNCTION public.securitygrouppermissiveobjectcombined(securitypermissionid integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$SELECT * from securitygrouppermissiveobject1($1,$2)
UNION
SELECT * from securitygrouppermissiveobject2($1,$2)
UNION SELECT * from securitygrouppermissiveobject3($1,$2);$_$;
 r   DROP FUNCTION public.securitygrouppermissiveobjectcombined(securitypermissionid integer, securityuserid integer);
       public          postgres    false    2224            �           0    0 d   FUNCTION securitygrouppermissiveobjectcombined(securitypermissionid integer, securityuserid integer)    COMMENT     �  COMMENT ON FUNCTION public.securitygrouppermissiveobjectcombined(securitypermissionid integer, securityuserid integer) IS 'This is part of a series of functions:
securitygrouppermissiveobject1, securitygrouppermissiveobject2, securitygrouppermissiveobject3 and securitygrouppermissiveobjectcombined.  Together they work like a 2 tier recursive function and as such will probably be replaced in the future.  Together they bring together all the objects that a specified user has the specified permission type on.  See also securitygrouprestrictiveobject... and securitygroupobjectmaster.  Similar functions have been created for trees and vmeasurements.  ';
          public          postgres    false    1579            �           0    0 d   FUNCTION securitygrouppermissiveobjectcombined(securitypermissionid integer, securityuserid integer)    ACL     �   GRANT ALL ON FUNCTION public.securitygrouppermissiveobjectcombined(securitypermissionid integer, securityuserid integer) TO "Webgroup";
          public          postgres    false    1579            ,           1255    65043 4   securitygrouppermissiveobjectcombined(integer, uuid)    FUNCTION     k  CREATE FUNCTION public.securitygrouppermissiveobjectcombined(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$SELECT * from securitygrouppermissiveobject1($1,$2)
UNION
SELECT * from securitygrouppermissiveobject2($1,$2)
UNION SELECT * from securitygrouppermissiveobject3($1,$2);$_$;
 o   DROP FUNCTION public.securitygrouppermissiveobjectcombined(securitypermissionid integer, securityuserid uuid);
       public          tellervo    false    2230            -           1255    65044 .   securitygrouppermissivetree1(integer, integer)    FUNCTION     B  CREATE FUNCTION public.securitygrouppermissivetree1(securitypermissionid integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$SELECT tblsecurityuser.securityuserid, tblsecuritytree.elementid
FROM tblsecurityuser 
INNER JOIN ((tblsecuritygroup 
INNER JOIN tblsecurityusermembership ON tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) 
INNER JOIN tblsecuritytree ON tblsecuritygroup.securitygroupid = tblsecuritytree.securitygroupid) ON tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecuritytree.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
GROUP BY tblsecurityuser.securityuserid, tblsecuritytree.elementid
HAVING (((tblsecurityuser.securityuserid)=$2));$_$;
 i   DROP FUNCTION public.securitygrouppermissivetree1(securitypermissionid integer, securityuserid integer);
       public          postgres    false    2224            �           0    0 [   FUNCTION securitygrouppermissivetree1(securitypermissionid integer, securityuserid integer)    ACL        GRANT ALL ON FUNCTION public.securitygrouppermissivetree1(securitypermissionid integer, securityuserid integer) TO "Webgroup";
          public          postgres    false    1581            .           1255    65045 .   securitygrouppermissivetree2(integer, integer)    FUNCTION     O  CREATE FUNCTION public.securitygrouppermissivetree2(securitypermission integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecuritytree.elementid
from tblsecurityuser 
inner join ((tblsecuritygroup 
inner join ((tblsecuritygroupmembership 
inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) 
inner join tblsecuritytree on tblsecuritygroup_1.securitygroupid = tblsecuritytree.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) 
inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecuritytree.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
group by tblsecurityuser.securityuserid, tblsecuritytree.elementid
having (((tblsecurityuser.securityuserid)=$2));$_$;
 g   DROP FUNCTION public.securitygrouppermissivetree2(securitypermission integer, securityuserid integer);
       public          postgres    false    2224            �           0    0 Y   FUNCTION securitygrouppermissivetree2(securitypermission integer, securityuserid integer)    ACL     }   GRANT ALL ON FUNCTION public.securitygrouppermissivetree2(securitypermission integer, securityuserid integer) TO "Webgroup";
          public          postgres    false    1582            /           1255    65046 .   securitygrouppermissivetree3(integer, integer)    FUNCTION     �  CREATE FUNCTION public.securitygrouppermissivetree3(securitypermissionid integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecuritytree.elementid
from tblsecurityuser 
inner join ((tblsecuritytree 
inner join (tblsecuritygroup 
inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 
inner join (tblsecuritygroupmembership 
inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) 
inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecuritytree.securitygroupid = tblsecuritygroup_2.securitygroupid) 
inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecuritytree.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
group by tblsecurityuser.securityuserid, tblsecuritytree.elementid
having (((tblsecurityuser.securityuserid)=$2));$_$;
 i   DROP FUNCTION public.securitygrouppermissivetree3(securitypermissionid integer, securityuserid integer);
       public          postgres    false    2224            �           0    0 [   FUNCTION securitygrouppermissivetree3(securitypermissionid integer, securityuserid integer)    ACL        GRANT ALL ON FUNCTION public.securitygrouppermissivetree3(securitypermissionid integer, securityuserid integer) TO "Webgroup";
          public          postgres    false    1583            0           1255    65047 5   securitygrouppermissivetreecombined(integer, integer)    FUNCTION     `  CREATE FUNCTION public.securitygrouppermissivetreecombined(securitypermissionid integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$SELECT * from securitygrouppermissivetree1($1,$2)
UNION
SELECT * from securitygrouppermissivetree2($1,$2)
UNION SELECT * from securitygrouppermissivetree3($1,$2);$_$;
 p   DROP FUNCTION public.securitygrouppermissivetreecombined(securitypermissionid integer, securityuserid integer);
       public          postgres    false    2224            �           0    0 b   FUNCTION securitygrouppermissivetreecombined(securitypermissionid integer, securityuserid integer)    ACL     �   GRANT ALL ON FUNCTION public.securitygrouppermissivetreecombined(securitypermissionid integer, securityuserid integer) TO "Webgroup";
          public          postgres    false    1584            1           1255    65048 6   securitygrouppermissivevmeasurement1(integer, integer)    FUNCTION     y  CREATE FUNCTION public.securitygrouppermissivevmeasurement1(securitypermissionid integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$SELECT tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
FROM tblsecurityuser INNER JOIN ((tblsecuritygroup INNER JOIN tblsecurityusermembership ON tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) INNER JOIN tblsecurityvmeasurement ON tblsecuritygroup.securitygroupid = tblsecurityvmeasurement.securitygroupid) ON tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityvmeasurement.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
GROUP BY tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
HAVING (((tblsecurityuser.securityuserid)=$2));$_$;
 q   DROP FUNCTION public.securitygrouppermissivevmeasurement1(securitypermissionid integer, securityuserid integer);
       public          postgres    false    2224            �           0    0 c   FUNCTION securitygrouppermissivevmeasurement1(securitypermissionid integer, securityuserid integer)    ACL     �   GRANT ALL ON FUNCTION public.securitygrouppermissivevmeasurement1(securitypermissionid integer, securityuserid integer) TO "Webgroup";
          public          postgres    false    1585            2           1255    65049 3   securitygrouppermissivevmeasurement1(integer, uuid)    FUNCTION     |  CREATE FUNCTION public.securitygrouppermissivevmeasurement1(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$SELECT tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
FROM tblsecurityuser INNER JOIN ((tblsecuritygroup INNER JOIN tblsecurityusermembership ON tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) INNER JOIN tblsecurityvmeasurement ON tblsecuritygroup.securitygroupid = tblsecurityvmeasurement.securitygroupid) ON tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityvmeasurement.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
GROUP BY tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
HAVING (((tblsecurityuser.securityuserid)=$2));$_$;
 n   DROP FUNCTION public.securitygrouppermissivevmeasurement1(securitypermissionid integer, securityuserid uuid);
       public          tellervo    false    2230            3           1255    65050 6   securitygrouppermissivevmeasurement2(integer, integer)    FUNCTION     �  CREATE FUNCTION public.securitygrouppermissivevmeasurement2(securitypermission integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
from tblsecurityuser inner join ((tblsecuritygroup inner join ((tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecurityvmeasurement on tblsecuritygroup_1.securitygroupid = tblsecurityvmeasurement.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityvmeasurement.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
group by tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
having (((tblsecurityuser.securityuserid)=$2));$_$;
 o   DROP FUNCTION public.securitygrouppermissivevmeasurement2(securitypermission integer, securityuserid integer);
       public          postgres    false    2224            �           0    0 a   FUNCTION securitygrouppermissivevmeasurement2(securitypermission integer, securityuserid integer)    ACL     �   GRANT ALL ON FUNCTION public.securitygrouppermissivevmeasurement2(securitypermission integer, securityuserid integer) TO "Webgroup";
          public          postgres    false    1587            4           1255    65051 3   securitygrouppermissivevmeasurement2(integer, uuid)    FUNCTION     �  CREATE FUNCTION public.securitygrouppermissivevmeasurement2(securitypermission integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
from tblsecurityuser inner join ((tblsecuritygroup inner join ((tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecurityvmeasurement on tblsecuritygroup_1.securitygroupid = tblsecurityvmeasurement.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityvmeasurement.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
group by tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
having (((tblsecurityuser.securityuserid)=$2));$_$;
 l   DROP FUNCTION public.securitygrouppermissivevmeasurement2(securitypermission integer, securityuserid uuid);
       public          tellervo    false    2230            5           1255    65052 6   securitygrouppermissivevmeasurement3(integer, integer)    FUNCTION     �  CREATE FUNCTION public.securitygrouppermissivevmeasurement3(securitypermissionid integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
from tblsecurityuser 
inner join ((tblsecurityvmeasurement 
inner join (tblsecuritygroup 
inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 
inner join (tblsecuritygroupmembership 
inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) 
inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecurityvmeasurement.securitygroupid = tblsecuritygroup_2.securitygroupid) 
inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityvmeasurement.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
group by tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
having (((tblsecurityuser.securityuserid)=$2));$_$;
 q   DROP FUNCTION public.securitygrouppermissivevmeasurement3(securitypermissionid integer, securityuserid integer);
       public          postgres    false    2224            �           0    0 c   FUNCTION securitygrouppermissivevmeasurement3(securitypermissionid integer, securityuserid integer)    ACL     �   GRANT ALL ON FUNCTION public.securitygrouppermissivevmeasurement3(securitypermissionid integer, securityuserid integer) TO "Webgroup";
          public          postgres    false    1589            6           1255    65053 3   securitygrouppermissivevmeasurement3(integer, uuid)    FUNCTION     �  CREATE FUNCTION public.securitygrouppermissivevmeasurement3(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
from tblsecurityuser 
inner join ((tblsecurityvmeasurement 
inner join (tblsecuritygroup 
inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 
inner join (tblsecuritygroupmembership 
inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) 
inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecurityvmeasurement.securitygroupid = tblsecuritygroup_2.securitygroupid) 
inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityvmeasurement.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
group by tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
having (((tblsecurityuser.securityuserid)=$2));$_$;
 n   DROP FUNCTION public.securitygrouppermissivevmeasurement3(securitypermissionid integer, securityuserid uuid);
       public          tellervo    false    2230            7           1255    65054 =   securitygrouppermissivevmeasurementcombined(integer, integer)    FUNCTION     �  CREATE FUNCTION public.securitygrouppermissivevmeasurementcombined(securitypermissionid integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$SELECT * from securitygrouppermissivevmeasurement1($1,$2)
UNION
SELECT * from securitygrouppermissivevmeasurement2($1,$2)
UNION SELECT * from securitygrouppermissivevmeasurement3($1,$2);$_$;
 x   DROP FUNCTION public.securitygrouppermissivevmeasurementcombined(securitypermissionid integer, securityuserid integer);
       public          postgres    false    2224            �           0    0 j   FUNCTION securitygrouppermissivevmeasurementcombined(securitypermissionid integer, securityuserid integer)    ACL     �   GRANT ALL ON FUNCTION public.securitygrouppermissivevmeasurementcombined(securitypermissionid integer, securityuserid integer) TO "Webgroup";
          public          postgres    false    1591            8           1255    65055 :   securitygrouppermissivevmeasurementcombined(integer, uuid)    FUNCTION     �  CREATE FUNCTION public.securitygrouppermissivevmeasurementcombined(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$SELECT * from securitygrouppermissivevmeasurement1($1,$2)
UNION
SELECT * from securitygrouppermissivevmeasurement2($1,$2)
UNION SELECT * from securitygrouppermissivevmeasurement3($1,$2);$_$;
 u   DROP FUNCTION public.securitygrouppermissivevmeasurementcombined(securitypermissionid integer, securityuserid uuid);
       public          tellervo    false    2230            9           1255    65056 &   securitygrouprestrictiveelement1(uuid)    FUNCTION     �  CREATE FUNCTION public.securitygrouprestrictiveelement1(securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityelement.elementid
from tblsecurityuser inner join ((tblsecuritygroup inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) inner join tblsecurityelement on tblsecuritygroup.securitygroupid = tblsecurityelement.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityelement.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityelement.elementid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 L   DROP FUNCTION public.securitygrouprestrictiveelement1(securityuserid uuid);
       public          tellervo    false    2230            :           1255    65057 &   securitygrouprestrictiveelement2(uuid)    FUNCTION     �  CREATE FUNCTION public.securitygrouprestrictiveelement2(securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityelement.elementid
from tblsecurityuser inner join ((tblsecuritygroup inner join ((tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecurityelement on tblsecuritygroup_1.securitygroupid = tblsecurityelement.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityelement.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityelement.elementid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 L   DROP FUNCTION public.securitygrouprestrictiveelement2(securityuserid uuid);
       public          tellervo    false    2230            ;           1255    65058 &   securitygrouprestrictiveelement3(uuid)    FUNCTION     +  CREATE FUNCTION public.securitygrouprestrictiveelement3(securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityelement.elementid
from tblsecurityuser inner join ((tblsecurityelement inner join (tblsecuritygroup inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 inner join (tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecurityelement.securitygroupid = tblsecuritygroup_2.securitygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityelement.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityelement.elementid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 L   DROP FUNCTION public.securitygrouprestrictiveelement3(securityuserid uuid);
       public          tellervo    false    2230            <           1255    65059 -   securitygrouprestrictiveelementcombined(uuid)    FUNCTION     L  CREATE FUNCTION public.securitygrouprestrictiveelementcombined(securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select * from securitygrouprestrictiveelement1($1)
UNION
select * from securitygrouprestrictiveelement2($1)
UNION SELECT * from securitygrouprestrictiveelement3($1);$_$;
 S   DROP FUNCTION public.securitygrouprestrictiveelementcombined(securityuserid uuid);
       public          tellervo    false    2230            =           1255    65060 (   securitygrouprestrictiveobject1(integer)    FUNCTION     �  CREATE FUNCTION public.securitygrouprestrictiveobject1(securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityobject.objectid
from tblsecurityuser inner join ((tblsecuritygroup inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) inner join tblsecurityobject on tblsecuritygroup.securitygroupid = tblsecurityobject.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityobject.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityobject.objectid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 N   DROP FUNCTION public.securitygrouprestrictiveobject1(securityuserid integer);
       public          postgres    false    2224            �           0    0 @   FUNCTION securitygrouprestrictiveobject1(securityuserid integer)    ACL     d   GRANT ALL ON FUNCTION public.securitygrouprestrictiveobject1(securityuserid integer) TO "Webgroup";
          public          postgres    false    1597            >           1255    65061 %   securitygrouprestrictiveobject1(uuid)    FUNCTION     �  CREATE FUNCTION public.securitygrouprestrictiveobject1(securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityobject.objectid
from tblsecurityuser inner join ((tblsecuritygroup inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) inner join tblsecurityobject on tblsecuritygroup.securitygroupid = tblsecurityobject.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityobject.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityobject.objectid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 K   DROP FUNCTION public.securitygrouprestrictiveobject1(securityuserid uuid);
       public          tellervo    false    2230            ?           1255    65062 (   securitygrouprestrictiveobject2(integer)    FUNCTION     �  CREATE FUNCTION public.securitygrouprestrictiveobject2(securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityobject.objectid
from tblsecurityuser inner join ((tblsecuritygroup inner join ((tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecurityobject on tblsecuritygroup_1.securitygroupid = tblsecurityobject.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityobject.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityobject.objectid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 N   DROP FUNCTION public.securitygrouprestrictiveobject2(securityuserid integer);
       public          postgres    false    2224            �           0    0 @   FUNCTION securitygrouprestrictiveobject2(securityuserid integer)    ACL     d   GRANT ALL ON FUNCTION public.securitygrouprestrictiveobject2(securityuserid integer) TO "Webgroup";
          public          postgres    false    1599            @           1255    65063 %   securitygrouprestrictiveobject2(uuid)    FUNCTION     �  CREATE FUNCTION public.securitygrouprestrictiveobject2(securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityobject.objectid
from tblsecurityuser inner join ((tblsecuritygroup inner join ((tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecurityobject on tblsecuritygroup_1.securitygroupid = tblsecurityobject.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityobject.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityobject.objectid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 K   DROP FUNCTION public.securitygrouprestrictiveobject2(securityuserid uuid);
       public          tellervo    false    2230            A           1255    65064 (   securitygrouprestrictiveobject3(integer)    FUNCTION        CREATE FUNCTION public.securitygrouprestrictiveobject3(securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityobject.objectid
from tblsecurityuser inner join ((tblsecurityobject inner join (tblsecuritygroup inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 inner join (tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecurityobject.securitygroupid = tblsecuritygroup_2.securitygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityobject.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityobject.objectid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 N   DROP FUNCTION public.securitygrouprestrictiveobject3(securityuserid integer);
       public          postgres    false    2224            �           0    0 @   FUNCTION securitygrouprestrictiveobject3(securityuserid integer)    ACL     d   GRANT ALL ON FUNCTION public.securitygrouprestrictiveobject3(securityuserid integer) TO "Webgroup";
          public          postgres    false    1601            B           1255    65065 %   securitygrouprestrictiveobject3(uuid)    FUNCTION     #  CREATE FUNCTION public.securitygrouprestrictiveobject3(securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityobject.objectid
from tblsecurityuser inner join ((tblsecurityobject inner join (tblsecuritygroup inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 inner join (tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecurityobject.securitygroupid = tblsecuritygroup_2.securitygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityobject.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityobject.objectid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 K   DROP FUNCTION public.securitygrouprestrictiveobject3(securityuserid uuid);
       public          tellervo    false    2230            C           1255    65066 /   securitygrouprestrictiveobjectcombined(integer)    FUNCTION     E  CREATE FUNCTION public.securitygrouprestrictiveobjectcombined(securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select * from securitygrouprestrictiveobject1($1)
UNION
select * from securitygrouprestrictiveobject2($1)
UNION SELECT * from securitygrouprestrictiveobject3($1);$_$;
 U   DROP FUNCTION public.securitygrouprestrictiveobjectcombined(securityuserid integer);
       public          postgres    false    2224            �           0    0 G   FUNCTION securitygrouprestrictiveobjectcombined(securityuserid integer)    ACL     k   GRANT ALL ON FUNCTION public.securitygrouprestrictiveobjectcombined(securityuserid integer) TO "Webgroup";
          public          postgres    false    1603            D           1255    65067 ,   securitygrouprestrictiveobjectcombined(uuid)    FUNCTION     H  CREATE FUNCTION public.securitygrouprestrictiveobjectcombined(securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select * from securitygrouprestrictiveobject1($1)
UNION
select * from securitygrouprestrictiveobject2($1)
UNION SELECT * from securitygrouprestrictiveobject3($1);$_$;
 R   DROP FUNCTION public.securitygrouprestrictiveobjectcombined(securityuserid uuid);
       public          tellervo    false    2230            E           1255    65068 &   securitygrouprestrictivetree1(integer)    FUNCTION     �  CREATE FUNCTION public.securitygrouprestrictivetree1(securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecuritytree.elementid
from tblsecurityuser inner join ((tblsecuritygroup inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) inner join tblsecuritytree on tblsecuritygroup.securitygroupid = tblsecuritytree.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecuritytree.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecuritytree.elementid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 L   DROP FUNCTION public.securitygrouprestrictivetree1(securityuserid integer);
       public          postgres    false    2224            �           0    0 >   FUNCTION securitygrouprestrictivetree1(securityuserid integer)    ACL     b   GRANT ALL ON FUNCTION public.securitygrouprestrictivetree1(securityuserid integer) TO "Webgroup";
          public          postgres    false    1605            F           1255    65069 &   securitygrouprestrictivetree2(integer)    FUNCTION     �  CREATE FUNCTION public.securitygrouprestrictivetree2(securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecuritytree.elementid
from tblsecurityuser inner join ((tblsecuritygroup inner join ((tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecuritytree on tblsecuritygroup_1.securitygroupid = tblsecuritytree.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecuritytree.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecuritytree.elementid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 L   DROP FUNCTION public.securitygrouprestrictivetree2(securityuserid integer);
       public          postgres    false    2224            �           0    0 >   FUNCTION securitygrouprestrictivetree2(securityuserid integer)    ACL     b   GRANT ALL ON FUNCTION public.securitygrouprestrictivetree2(securityuserid integer) TO "Webgroup";
          public          postgres    false    1606            G           1255    65070 &   securitygrouprestrictivetree3(integer)    FUNCTION       CREATE FUNCTION public.securitygrouprestrictivetree3(securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecuritytree.elementid
from tblsecurityuser inner join ((tblsecuritytree inner join (tblsecuritygroup inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 inner join (tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecuritytree.securitygroupid = tblsecuritygroup_2.securitygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecuritytree.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecuritytree.elementid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 L   DROP FUNCTION public.securitygrouprestrictivetree3(securityuserid integer);
       public          postgres    false    2224            �           0    0 >   FUNCTION securitygrouprestrictivetree3(securityuserid integer)    ACL     b   GRANT ALL ON FUNCTION public.securitygrouprestrictivetree3(securityuserid integer) TO "Webgroup";
          public          postgres    false    1607            H           1255    65071 -   securitygrouprestrictivetreecombined(integer)    FUNCTION     =  CREATE FUNCTION public.securitygrouprestrictivetreecombined(securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select * from securitygrouprestrictivetree1($1)
UNION
select * from securitygrouprestrictivetree2($1)
UNION SELECT * from securitygrouprestrictivetree3($1);$_$;
 S   DROP FUNCTION public.securitygrouprestrictivetreecombined(securityuserid integer);
       public          postgres    false    2224            �           0    0 E   FUNCTION securitygrouprestrictivetreecombined(securityuserid integer)    ACL     i   GRANT ALL ON FUNCTION public.securitygrouprestrictivetreecombined(securityuserid integer) TO "Webgroup";
          public          postgres    false    1608            I           1255    65072 .   securitygrouprestrictivevmeasurement1(integer)    FUNCTION       CREATE FUNCTION public.securitygrouprestrictivevmeasurement1(securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
from tblsecurityuser inner join ((tblsecuritygroup inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) inner join tblsecurityvmeasurement on tblsecuritygroup.securitygroupid = tblsecurityvmeasurement.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityvmeasurement.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 T   DROP FUNCTION public.securitygrouprestrictivevmeasurement1(securityuserid integer);
       public          postgres    false    2224            �           0    0 F   FUNCTION securitygrouprestrictivevmeasurement1(securityuserid integer)    ACL     j   GRANT ALL ON FUNCTION public.securitygrouprestrictivevmeasurement1(securityuserid integer) TO "Webgroup";
          public          postgres    false    1609            J           1255    65073 +   securitygrouprestrictivevmeasurement1(uuid)    FUNCTION       CREATE FUNCTION public.securitygrouprestrictivevmeasurement1(securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
from tblsecurityuser inner join ((tblsecuritygroup inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) inner join tblsecurityvmeasurement on tblsecuritygroup.securitygroupid = tblsecurityvmeasurement.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityvmeasurement.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 Q   DROP FUNCTION public.securitygrouprestrictivevmeasurement1(securityuserid uuid);
       public          tellervo    false    2230            K           1255    65074 .   securitygrouprestrictivevmeasurement2(integer)    FUNCTION       CREATE FUNCTION public.securitygrouprestrictivevmeasurement2(securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
from tblsecurityuser inner join ((tblsecuritygroup inner join ((tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecurityvmeasurement on tblsecuritygroup_1.securitygroupid = tblsecurityvmeasurement.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityvmeasurement.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 T   DROP FUNCTION public.securitygrouprestrictivevmeasurement2(securityuserid integer);
       public          postgres    false    2224            �           0    0 F   FUNCTION securitygrouprestrictivevmeasurement2(securityuserid integer)    ACL     j   GRANT ALL ON FUNCTION public.securitygrouprestrictivevmeasurement2(securityuserid integer) TO "Webgroup";
          public          postgres    false    1611            L           1255    65075 +   securitygrouprestrictivevmeasurement2(uuid)    FUNCTION     "  CREATE FUNCTION public.securitygrouprestrictivevmeasurement2(securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
from tblsecurityuser inner join ((tblsecuritygroup inner join ((tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecurityvmeasurement on tblsecuritygroup_1.securitygroupid = tblsecurityvmeasurement.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityvmeasurement.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 Q   DROP FUNCTION public.securitygrouprestrictivevmeasurement2(securityuserid uuid);
       public          tellervo    false    2230            M           1255    65076 .   securitygrouprestrictivevmeasurement3(integer)    FUNCTION     P  CREATE FUNCTION public.securitygrouprestrictivevmeasurement3(securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
from tblsecurityuser inner join ((tblsecurityvmeasurement inner join (tblsecuritygroup inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 inner join (tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecurityvmeasurement.securitygroupid = tblsecuritygroup_2.securitygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityvmeasurement.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 T   DROP FUNCTION public.securitygrouprestrictivevmeasurement3(securityuserid integer);
       public          postgres    false    2224            �           0    0 F   FUNCTION securitygrouprestrictivevmeasurement3(securityuserid integer)    ACL     j   GRANT ALL ON FUNCTION public.securitygrouprestrictivevmeasurement3(securityuserid integer) TO "Webgroup";
          public          postgres    false    1613            N           1255    65077 +   securitygrouprestrictivevmeasurement3(uuid)    FUNCTION     S  CREATE FUNCTION public.securitygrouprestrictivevmeasurement3(securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
from tblsecurityuser inner join ((tblsecurityvmeasurement inner join (tblsecuritygroup inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 inner join (tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecurityvmeasurement.securitygroupid = tblsecuritygroup_2.securitygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityvmeasurement.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 Q   DROP FUNCTION public.securitygrouprestrictivevmeasurement3(securityuserid uuid);
       public          tellervo    false    2230            O           1255    65078 5   securitygrouprestrictivevmeasurementcombined(integer)    FUNCTION     ]  CREATE FUNCTION public.securitygrouprestrictivevmeasurementcombined(securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select * from securitygrouprestrictivevmeasurement1($1)
UNION
select * from securitygrouprestrictivevmeasurement2($1)
UNION SELECT * from securitygrouprestrictivevmeasurement3($1);$_$;
 [   DROP FUNCTION public.securitygrouprestrictivevmeasurementcombined(securityuserid integer);
       public          postgres    false    2224            �           0    0 M   FUNCTION securitygrouprestrictivevmeasurementcombined(securityuserid integer)    ACL     q   GRANT ALL ON FUNCTION public.securitygrouprestrictivevmeasurementcombined(securityuserid integer) TO "Webgroup";
          public          postgres    false    1615            P           1255    65079 2   securitygrouprestrictivevmeasurementcombined(uuid)    FUNCTION     `  CREATE FUNCTION public.securitygrouprestrictivevmeasurementcombined(securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select * from securitygrouprestrictivevmeasurement1($1)
UNION
select * from securitygrouprestrictivevmeasurement2($1)
UNION SELECT * from securitygrouprestrictivevmeasurement3($1);$_$;
 X   DROP FUNCTION public.securitygrouprestrictivevmeasurementcombined(securityuserid uuid);
       public          tellervo    false    2230            Q           1255    65080    securitygroupsbyuser(integer)    FUNCTION     �   CREATE FUNCTION public.securitygroupsbyuser(securityuserid integer) RETURNS SETOF integer
    LANGUAGE sql
    AS $_$SELECT * from securitygroupsbyuser1($1)
UNION
SELECT * from securitygroupsbyuser2($1)
UNION SELECT * from securitygroupsbyuser3($1)$_$;
 C   DROP FUNCTION public.securitygroupsbyuser(securityuserid integer);
       public          postgres    false            �           0    0 5   FUNCTION securitygroupsbyuser(securityuserid integer)    ACL     Y   GRANT ALL ON FUNCTION public.securitygroupsbyuser(securityuserid integer) TO "Webgroup";
          public          postgres    false    1617            R           1255    65081    securitygroupsbyuser(uuid)    FUNCTION     �   CREATE FUNCTION public.securitygroupsbyuser(securityuserid uuid) RETURNS SETOF integer
    LANGUAGE sql
    AS $_$SELECT * from securitygroupsbyuser1($1)
UNION
SELECT * from securitygroupsbyuser2($1)
UNION SELECT * from securitygroupsbyuser3($1)$_$;
 @   DROP FUNCTION public.securitygroupsbyuser(securityuserid uuid);
       public          tellervo    false            S           1255    65082    securitygroupsbyuser1(integer)    FUNCTION     �  CREATE FUNCTION public.securitygroupsbyuser1(securityuserid integer) RETURNS SETOF integer
    LANGUAGE sql
    AS $_$SELECT tblsecuritygroup.securitygroupid
FROM tblsecurityuser INNER JOIN (tblsecuritygroup INNER JOIN
tblsecurityusermembership ON
tblsecuritygroup.securitygroupid=tblsecurityusermembership.securitygroupid)
ON tblsecurityuser.securityuserid=tblsecurityusermembership.securityuserid
WHERE (((tblsecurityuser.securityuserid)=$1));$_$;
 D   DROP FUNCTION public.securitygroupsbyuser1(securityuserid integer);
       public          postgres    false            �           0    0 6   FUNCTION securitygroupsbyuser1(securityuserid integer)    ACL     Z   GRANT ALL ON FUNCTION public.securitygroupsbyuser1(securityuserid integer) TO "Webgroup";
          public          postgres    false    1619            T           1255    65083    securitygroupsbyuser1(uuid)    FUNCTION     �  CREATE FUNCTION public.securitygroupsbyuser1(securityuserid uuid) RETURNS SETOF integer
    LANGUAGE sql
    AS $_$SELECT tblsecuritygroup.securitygroupid
FROM tblsecurityuser INNER JOIN (tblsecuritygroup INNER JOIN
tblsecurityusermembership ON
tblsecuritygroup.securitygroupid=tblsecurityusermembership.securitygroupid)
ON tblsecurityuser.securityuserid=tblsecurityusermembership.securityuserid
WHERE (((tblsecurityuser.securityuserid)=$1));$_$;
 A   DROP FUNCTION public.securitygroupsbyuser1(securityuserid uuid);
       public          tellervo    false            U           1255    65084    securitygroupsbyuser2(integer)    FUNCTION     �  CREATE FUNCTION public.securitygroupsbyuser2(securityuserid integer) RETURNS SETOF integer
    LANGUAGE sql
    AS $_$SELECT tblSecurityGroup_1.SecurityGroupID
FROM tblsecurityuser INNER JOIN ((tblsecuritygroup INNER JOIN
(tblsecuritygroupmembership INNER JOIN tblsecuritygroup AS
tblsecuritygroup_1 ON tblsecuritygroupmembership.parentsecuritygroupid=tblsecuritygroup_1.securitygroupid) ON
tblsecuritygroup.securitygroupid=tblsecuritygroupmembership.childsecuritygroupid) INNER JOIN tblsecurityusermembership ON
tblsecuritygroup.securitygroupid=tblsecurityusermembership.securitygroupid) ON 
tblsecurityuser.securityuserid=tblsecurityusermembership.securityuserid
WHERE (((tblsecurityuser.securityuserid)=$1));$_$;
 D   DROP FUNCTION public.securitygroupsbyuser2(securityuserid integer);
       public          postgres    false            �           0    0 6   FUNCTION securitygroupsbyuser2(securityuserid integer)    ACL     Z   GRANT ALL ON FUNCTION public.securitygroupsbyuser2(securityuserid integer) TO "Webgroup";
          public          postgres    false    1621            V           1255    65085    securitygroupsbyuser2(uuid)    FUNCTION     �  CREATE FUNCTION public.securitygroupsbyuser2(securityuserid uuid) RETURNS SETOF integer
    LANGUAGE sql
    AS $_$SELECT tblSecurityGroup_1.SecurityGroupID
FROM tblsecurityuser INNER JOIN ((tblsecuritygroup INNER JOIN
(tblsecuritygroupmembership INNER JOIN tblsecuritygroup AS
tblsecuritygroup_1 ON tblsecuritygroupmembership.parentsecuritygroupid=tblsecuritygroup_1.securitygroupid) ON
tblsecuritygroup.securitygroupid=tblsecuritygroupmembership.childsecuritygroupid) INNER JOIN tblsecurityusermembership ON
tblsecuritygroup.securitygroupid=tblsecurityusermembership.securitygroupid) ON 
tblsecurityuser.securityuserid=tblsecurityusermembership.securityuserid
WHERE (((tblsecurityuser.securityuserid)=$1));$_$;
 A   DROP FUNCTION public.securitygroupsbyuser2(securityuserid uuid);
       public          tellervo    false            W           1255    65086    securitygroupsbyuser3(integer)    FUNCTION       CREATE FUNCTION public.securitygroupsbyuser3(securityuserid integer) RETURNS SETOF integer
    LANGUAGE sql
    AS $_$select tblsecuritygroup_2.securitygroupid 
from tblsecurityuser 
inner join ((tblsecuritygroup 
inner join
((tblsecuritygroupmembership as tblsecuritygroupmembership_1 
inner join (tblsecuritygroupmembership 
inner join tblsecuritygroup as tblsecuritygroup_1 
on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) 
on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) 
inner join tblsecuritygroup as tblsecuritygroup_2 
on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) 
on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) 
inner join tblsecurityusermembership 
on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) 
on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityuser.securityuserid)=$1));$_$;
 D   DROP FUNCTION public.securitygroupsbyuser3(securityuserid integer);
       public          postgres    false            �           0    0 6   FUNCTION securitygroupsbyuser3(securityuserid integer)    ACL     Z   GRANT ALL ON FUNCTION public.securitygroupsbyuser3(securityuserid integer) TO "Webgroup";
          public          postgres    false    1623            X           1255    65087    securitygroupsbyuser3(uuid)    FUNCTION       CREATE FUNCTION public.securitygroupsbyuser3(securityuserid uuid) RETURNS SETOF integer
    LANGUAGE sql
    AS $_$select tblsecuritygroup_2.securitygroupid 
from tblsecurityuser 
inner join ((tblsecuritygroup 
inner join
((tblsecuritygroupmembership as tblsecuritygroupmembership_1 
inner join (tblsecuritygroupmembership 
inner join tblsecuritygroup as tblsecuritygroup_1 
on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) 
on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) 
inner join tblsecuritygroup as tblsecuritygroup_2 
on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) 
on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) 
inner join tblsecurityusermembership 
on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) 
on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityuser.securityuserid)=$1));$_$;
 A   DROP FUNCTION public.securitygroupsbyuser3(securityuserid uuid);
       public          tellervo    false            Y           1255    65088 )   securitygrouptreemaster(integer, integer)    FUNCTION     �  CREATE FUNCTION public.securitygrouptreemaster(securitypermissionid integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select securitygrouppermissivetreecombined.* 
from securitygrouppermissivetreecombined($1, $2) left join securitygrouprestrictivetreecombined($2) on securitygrouppermissivetreecombined.objectid = securitygrouprestrictivetreecombined.objectid 
where (((securitygrouprestrictivetreecombined.objectid) is null));$_$;
 d   DROP FUNCTION public.securitygrouptreemaster(securitypermissionid integer, securityuserid integer);
       public          postgres    false    2224            �           0    0 V   FUNCTION securitygrouptreemaster(securitypermissionid integer, securityuserid integer)    ACL     z   GRANT ALL ON FUNCTION public.securitygrouptreemaster(securitypermissionid integer, securityuserid integer) TO "Webgroup";
          public          postgres    false    1625            Z           1255    65089 1   securitygroupvmeasurementmaster(integer, integer)    FUNCTION       CREATE FUNCTION public.securitygroupvmeasurementmaster(securitypermissionid integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select securitygrouppermissivevmeasurementcombined.* 
from securitygrouppermissivevmeasurementcombined($1, $2) left join securitygrouprestrictivevmeasurementcombined($2) on securitygrouppermissivevmeasurementcombined.objectid = securitygrouprestrictivevmeasurementcombined.objectid 
where (((securitygrouprestrictivevmeasurementcombined.objectid) is null));$_$;
 l   DROP FUNCTION public.securitygroupvmeasurementmaster(securitypermissionid integer, securityuserid integer);
       public          postgres    false    2224            �           0    0 ^   FUNCTION securitygroupvmeasurementmaster(securitypermissionid integer, securityuserid integer)    ACL     �   GRANT ALL ON FUNCTION public.securitygroupvmeasurementmaster(securitypermissionid integer, securityuserid integer) TO "Webgroup";
          public          postgres    false    1626            [           1255    65090 .   securitygroupvmeasurementmaster(integer, uuid)    FUNCTION     "  CREATE FUNCTION public.securitygroupvmeasurementmaster(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select securitygrouppermissivevmeasurementcombined.* 
from securitygrouppermissivevmeasurementcombined($1, $2) left join securitygrouprestrictivevmeasurementcombined($2) on securitygrouppermissivevmeasurementcombined.objectid = securitygrouprestrictivevmeasurementcombined.objectid 
where (((securitygrouprestrictivevmeasurementcombined.objectid) is null));$_$;
 i   DROP FUNCTION public.securitygroupvmeasurementmaster(securitypermissionid integer, securityuserid uuid);
       public          tellervo    false    2230            \           1255    65091 ,   securitypermselement(uuid, integer, integer)    FUNCTION     �  CREATE FUNCTION public.securitypermselement(securityuserid uuid, securitypermissionid integer, elementid integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $_$DECLARE
  myelementid integer;
  myobjectid integer;
  mybin integer;
  myboolbin boolean;
BEGIN
	-- Check if user is in admin group
	SELECT isadmin into myboolbin from isadmin($1) where isadmin=true;
	IF NOT FOUND THEN
		-- User not in admin group so continue checking permissions
		-- Next check that a 'no permission' record isn't overiding
		 SELECT objectid INTO mybin from securitygrouprestrictiveelementcombined($1) where objectid=$3;

		 IF NOT FOUND THEN
			-- There is no 'no permission' record so continue checking permissions
			-- Check for specified permission, on specified tree, for specified user
			SELECT objectid 
			INTO myelementid 
			FROM securitygroupelementmaster($2, $1) 
			WHERE objectid=$3;

			IF NOT FOUND THEN
				-- No tree permissions specified so move up to object to check permissions
				SELECT objectid
				INTO myobjectid
				FROM tblsubobject, tblelement
				WHERE tblelement.subobjectid=tblsubobject.subobjectid 
				AND tblelement.elementid=$3;
				-- Return true/false for permission from object 
				return securitypermsobject($1, $2, myobjectid);
			ELSE
				-- Tree permission record found so return true 
				return true;
			END IF;
		 ELSE
			-- 'No permission' specified for tree so return false
			return false;
		 END IF;
	ELSE
		-- User is in admin group so return true
		return true;
	END IF;
END;
$_$;
 q   DROP FUNCTION public.securitypermselement(securityuserid uuid, securitypermissionid integer, elementid integer);
       public          tellervo    false            ]           1255    65092 .   securitypermsobject(integer, integer, integer)    FUNCTION     �  CREATE FUNCTION public.securitypermsobject(securityuserid integer, securitypermissionid integer, securityobjectid integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $_$DECLARE
  myobjectid integer;
  mydatabaseid integer;
  mybin integer;
  myboolbin boolean;
BEGIN

 SELECT isadmin into myboolbin from isadmin($1) where isadmin=true;
 IF NOT FOUND THEN
	-- User not in admin group so continue checking permissions
	-- Next check that a 'no permission' record isn't overiding
	 SELECT objectid INTO mybin from securitygrouprestrictiveobjectcombined($1) where objectid=$3;

	 IF NOT FOUND THEN
		-- No 'no permssion' record for the object so continue checking permissions
		-- Check for specified permission, on specified object, for specified user
		SELECT objectid 
		INTO myobjectid 
		FROM securitygroupobjectmaster($2, $1) 
		WHERE objectid=$3;

		IF NOT FOUND THEN
			-- -- No permissions specified so move up to database level to check default permissions
			-- SELECT databaseid
			-- INTO mydatabaseid
			-- FROM tblobject
			-- WHERE tblobject.objectid=myobjectid
			-- -- Return true/false for permission from object 
			-- return securitypermsobject($1, $2, mydatabaseid); 
			return false;
		 ELSE
			-- Site permission record found so return true
			return true;
		 END IF;
	 ELSE
		-- 'No permission' specified for tree
		return false;
	 END IF;
ELSE
	-- User is in admin group so return true;
	return true;
END IF;

END;
$_$;
 z   DROP FUNCTION public.securitypermsobject(securityuserid integer, securitypermissionid integer, securityobjectid integer);
       public          postgres    false            �           0    0 l   FUNCTION securitypermsobject(securityuserid integer, securitypermissionid integer, securityobjectid integer)    ACL     �   GRANT ALL ON FUNCTION public.securitypermsobject(securityuserid integer, securitypermissionid integer, securityobjectid integer) TO "Webgroup";
          public          postgres    false    1629            ^           1255    65093 +   securitypermsobject(uuid, integer, integer)    FUNCTION     �  CREATE FUNCTION public.securitypermsobject(securityuserid uuid, securitypermissionid integer, securityobjectid integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $_$DECLARE
  myobjectid integer;
  mydatabaseid integer;
  mybin integer;
  myboolbin boolean;
BEGIN

 SELECT isadmin into myboolbin from isadmin($1) where isadmin=true;
 IF NOT FOUND THEN
	-- User not in admin group so continue checking permissions
	-- Next check that a 'no permission' record isn't overiding
	 SELECT objectid INTO mybin from securitygrouprestrictiveobjectcombined($1) where objectid=$3;

	 IF NOT FOUND THEN
		-- No 'no permssion' record for the object so continue checking permissions
		-- Check for specified permission, on specified object, for specified user
		SELECT objectid 
		INTO myobjectid 
		FROM securitygroupobjectmaster($2, $1) 
		WHERE objectid=$3;

		IF NOT FOUND THEN
			-- -- No permissions specified so move up to database level to check default permissions
			-- SELECT databaseid
			-- INTO mydatabaseid
			-- FROM tblobject
			-- WHERE tblobject.objectid=myobjectid
			-- -- Return true/false for permission from object 
			-- return securitypermsobject($1, $2, mydatabaseid); 
			return false;
		 ELSE
			-- Site permission record found so return true
			return true;
		 END IF;
	 ELSE
		-- 'No permission' specified for tree
		return false;
	 END IF;
ELSE
	-- User is in admin group so return true;
	return true;
END IF;

END;
$_$;
 w   DROP FUNCTION public.securitypermsobject(securityuserid uuid, securitypermissionid integer, securityobjectid integer);
       public          tellervo    false            _           1255    65094 ,   securitypermstree(integer, integer, integer)    FUNCTION     �  CREATE FUNCTION public.securitypermstree(securityuserid integer, securitypermissionid integer, elementid integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $_$DECLARE
  myelementid integer;
  myobjectid integer;
  mybin integer;
  myboolbin boolean;
BEGIN
	-- Check if user is in admin group
	SELECT isadmin into myboolbin from isadmin($1) where isadmin=true;
	IF NOT FOUND THEN
		-- User not in admin group so continue checking permissions
		-- Next check that a 'no permission' record isn't overiding
		 SELECT objectid INTO mybin from securitygrouprestrictivetreecombined($1) where objectid=$3;

		 IF NOT FOUND THEN
			-- There is no 'no permission' record so continue checking permissions
			-- Check for specified permission, on specified tree, for specified user
			SELECT objectid 
			INTO myelementid 
			FROM securitygrouptreemaster($2, $1) 
			WHERE objectid=$3;

			IF NOT FOUND THEN
				-- No tree permissions specified so move up to object to check permissions
				SELECT objectid
				INTO myobjectid
				FROM tblsubobject, tblelement
				WHERE tblelement.subobjectid=tblsubobject.subobjectid 
				AND tblelement.elementid=$3;
				-- Return true/false for permission from object 
				return securitypermsobject($1, $2, myobjectid);
			ELSE
				-- Tree permission record found so return true 
				return true;
			END IF;
		 ELSE
			-- 'No permission' specified for tree so return false
			return false;
		 END IF;
	ELSE
		-- User is in admin group so return true
		return true;
	END IF;
END;
$_$;
 q   DROP FUNCTION public.securitypermstree(securityuserid integer, securitypermissionid integer, elementid integer);
       public          postgres    false            �           0    0 c   FUNCTION securitypermstree(securityuserid integer, securitypermissionid integer, elementid integer)    ACL     �   GRANT ALL ON FUNCTION public.securitypermstree(securityuserid integer, securitypermissionid integer, elementid integer) TO "Webgroup";
          public          postgres    false    1631            `           1255    65095 -   segmentize(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.segmentize(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_segmentize2d';
 D   DROP FUNCTION public.segmentize(public.geometry, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            a           1255    65096 3   setpoint(public.geometry, integer, public.geometry)    FUNCTION     �   CREATE FUNCTION public.setpoint(public.geometry, integer, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_setpoint_linestring';
 J   DROP FUNCTION public.setpoint(public.geometry, integer, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            b           1255    65097 !   setsrid(public.geometry, integer)    FUNCTION     �   CREATE FUNCTION public.setsrid(public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_set_srid';
 8   DROP FUNCTION public.setsrid(public.geometry, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            c           1255    65098     shift_longitude(public.geometry)    FUNCTION     �   CREATE FUNCTION public.shift_longitude(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_longitude_shift';
 7   DROP FUNCTION public.shift_longitude(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            d           1255    65099 +   simplify(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.simplify(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_simplify2d';
 B   DROP FUNCTION public.simplify(public.geometry, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            e           1255    65100 -   snaptogrid(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.snaptogrid(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_SnapToGrid($1, 0, 0, $2, $2)$_$;
 D   DROP FUNCTION public.snaptogrid(public.geometry, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            f           1255    65101 ?   snaptogrid(public.geometry, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.snaptogrid(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_SnapToGrid($1, 0, 0, $2, $3)$_$;
 V   DROP FUNCTION public.snaptogrid(public.geometry, double precision, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            g           1255    65102 c   snaptogrid(public.geometry, double precision, double precision, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.snaptogrid(public.geometry, double precision, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_snaptogrid';
 z   DROP FUNCTION public.snaptogrid(public.geometry, double precision, double precision, double precision, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            h           1255    65103 t   snaptogrid(public.geometry, public.geometry, double precision, double precision, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.snaptogrid(public.geometry, public.geometry, double precision, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_snaptogrid_pointoff';
 �   DROP FUNCTION public.snaptogrid(public.geometry, public.geometry, double precision, double precision, double precision, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            i           1255    65104    srid(public.geometry)    FUNCTION     �   CREATE FUNCTION public.srid(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_get_srid';
 ,   DROP FUNCTION public.srid(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            j           1255    65105 6   st_3dlength_spheroid(public.geometry, public.spheroid)    FUNCTION     �   CREATE FUNCTION public.st_3dlength_spheroid(public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_LengthSpheroid($1,$2);$_$;
 M   DROP FUNCTION public.st_3dlength_spheroid(public.geometry, public.spheroid);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3            �           0    0 !   FUNCTION st_area(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_area(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_area(public.geometry) TO "Webgroup";
          public          postgres    false    571            k           1255    65106    st_asbinary(text)    FUNCTION     �   CREATE FUNCTION public.st_asbinary(text) RETURNS bytea
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$ SELECT ST_AsBinary($1::geometry);$_$;
 (   DROP FUNCTION public.st_asbinary(text);
       public          tellervo    false            �           0    0 %   FUNCTION st_asbinary(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_asbinary(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_asbinary(public.geometry) TO "Webgroup";
          public          postgres    false    937            l           1255    65107 9   st_asgeojson(integer, public.geography, integer, integer)    FUNCTION     �   CREATE FUNCTION public.st_asgeojson(version integer, geog public.geography, maxdecimaldigits integer DEFAULT 15, options integer DEFAULT 0) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$ SELECT ST_AsGeoJson($2::geometry,$3,$4); $_$;
 v   DROP FUNCTION public.st_asgeojson(version integer, geog public.geography, maxdecimaldigits integer, options integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            m           1255    65108 8   st_asgeojson(integer, public.geometry, integer, integer)    FUNCTION     �   CREATE FUNCTION public.st_asgeojson(version integer, geog public.geometry, maxdecimaldigits integer DEFAULT 15, options integer DEFAULT 0) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$ SELECT ST_AsGeoJson($2::geometry,15,0); $_$;
 u   DROP FUNCTION public.st_asgeojson(version integer, geog public.geometry, maxdecimaldigits integer, options integer);
       public          tellervo    false    3    3    3    3    3    3    3    3            n           1255    65109 2   st_askml(integer, public.geography, integer, text)    FUNCTION     �   CREATE FUNCTION public.st_askml(version integer, geom public.geography, maxdecimaldigits integer DEFAULT 15, nprefix text DEFAULT ''::text) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$ SELECT ST_AsKML($2::geometry,$3,$4); $_$;
 o   DROP FUNCTION public.st_askml(version integer, geom public.geography, maxdecimaldigits integer, nprefix text);
       public          tellervo    false    3    3    3    3    3    3    3    3            o           1255    65110 1   st_askml(integer, public.geometry, integer, text)    FUNCTION     �   CREATE FUNCTION public.st_askml(version integer, geom public.geometry, maxdecimaldigits integer DEFAULT 15, nprefix text DEFAULT ''::text) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$ SELECT ST_AsKML($2::geometry,$3,$4); $_$;
 n   DROP FUNCTION public.st_askml(version integer, geom public.geometry, maxdecimaldigits integer, nprefix text);
       public          tellervo    false    3    3    3    3    3    3    3    3            p           1255    65111    st_astext(bytea)    FUNCTION     �   CREATE FUNCTION public.st_astext(bytea) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$ SELECT ST_AsText($1::geometry);$_$;
 '   DROP FUNCTION public.st_astext(bytea);
       public          pbrewer    false            �           0    0 #   FUNCTION st_astext(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_astext(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_astext(public.geometry) TO "Webgroup";
          public          postgres    false    938            �           0    0 %   FUNCTION st_boundary(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_boundary(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_boundary(public.geometry) TO "Webgroup";
          public          postgres    false    773            q           1255    65112    st_box(public.box3d)    FUNCTION     �   CREATE FUNCTION public.st_box(public.box3d) RETURNS box
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_to_BOX';
 +   DROP FUNCTION public.st_box(public.box3d);
       public          tellervo    false    3    3    3            r           1255    65113    st_box(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_box(public.geometry) RETURNS box
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_to_BOX';
 .   DROP FUNCTION public.st_box(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            s           1255    65114    st_box2d(public.box3d)    FUNCTION     �   CREATE FUNCTION public.st_box2d(public.box3d) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_to_BOX2D';
 -   DROP FUNCTION public.st_box2d(public.box3d);
       public          tellervo    false    3    3    3    3    3    3            t           1255    65115    st_box2d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_box2d(public.geometry) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_to_BOX2D';
 0   DROP FUNCTION public.st_box2d(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3            u           1255    65116    st_box3d(public.box2d)    FUNCTION     �   CREATE FUNCTION public.st_box3d(public.box2d) RETURNS public.box3d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX2D_to_BOX3D';
 -   DROP FUNCTION public.st_box3d(public.box2d);
       public          tellervo    false    3    3    3    3    3    3            v           1255    65117    st_box3d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_box3d(public.geometry) RETURNS public.box3d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_to_BOX3D';
 0   DROP FUNCTION public.st_box3d(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3            w           1255    65118    st_box3d_in(cstring)    FUNCTION     �   CREATE FUNCTION public.st_box3d_in(cstring) RETURNS public.box3d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_in';
 +   DROP FUNCTION public.st_box3d_in(cstring);
       public          tellervo    false    3    3    3            x           1255    65119    st_box3d_out(public.box3d)    FUNCTION     �   CREATE FUNCTION public.st_box3d_out(public.box3d) RETURNS cstring
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_out';
 1   DROP FUNCTION public.st_box3d_out(public.box3d);
       public          tellervo    false    3    3    3            y           1255    65120    st_bytea(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_bytea(public.geometry) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_to_bytea';
 0   DROP FUNCTION public.st_bytea(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           0    0 %   FUNCTION st_centroid(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_centroid(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_centroid(public.geometry) TO "Webgroup";
          public          postgres    false    864            z           1255    65121 .   st_combine_bbox(public.box2d, public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_combine_bbox(public.box2d, public.geometry) RETURNS public.box2d
    LANGUAGE sql IMMUTABLE
    AS $_$SELECT ST_CombineBbox($1,$2);$_$;
 E   DROP FUNCTION public.st_combine_bbox(public.box2d, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3            {           1255    65122 .   st_combine_bbox(public.box3d, public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_combine_bbox(public.box3d, public.geometry) RETURNS public.box3d
    LANGUAGE sql IMMUTABLE
    AS $_$SELECT ST_CombineBbox($1,$2);$_$;
 E   DROP FUNCTION public.st_combine_bbox(public.box3d, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           0    0 B   FUNCTION st_contains(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_contains(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_contains(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    849            �           0    0 '   FUNCTION st_convexhull(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_convexhull(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_convexhull(public.geometry) TO "Webgroup";
          public          postgres    false    761            �           0    0 .   FUNCTION st_coorddim(geometry public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_coorddim(geometry public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_coorddim(geometry public.geometry) TO "Webgroup";
          public          postgres    false    1140            �           0    0 A   FUNCTION st_crosses(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_crosses(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_crosses(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    848            �           0    0 &   FUNCTION st_dimension(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_dimension(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_dimension(public.geometry) TO "Webgroup";
          public          postgres    false    922            �           0    0 B   FUNCTION st_disjoint(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_disjoint(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_disjoint(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    825            �           0    0 B   FUNCTION st_distance(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_distance(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_distance(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    576            |           1255    65123 4   st_distance_sphere(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_distance_sphere(geom1 public.geometry, geom2 public.geometry) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_DistanceSphere($1,$2);$_$;
 W   DROP FUNCTION public.st_distance_sphere(geom1 public.geometry, geom2 public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            }           1255    65124 G   st_distance_spheroid(public.geometry, public.geometry, public.spheroid)    FUNCTION     �   CREATE FUNCTION public.st_distance_spheroid(geom1 public.geometry, geom2 public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_DistanceSpheroid($1,$2,$3);$_$;
 j   DROP FUNCTION public.st_distance_spheroid(geom1 public.geometry, geom2 public.geometry, public.spheroid);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           0    0 %   FUNCTION st_endpoint(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_endpoint(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_endpoint(public.geometry) TO "Webgroup";
          public          postgres    false    933            �           0    0 %   FUNCTION st_envelope(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_envelope(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_envelope(public.geometry) TO "Webgroup";
          public          postgres    false    600            �           0    0 @   FUNCTION st_equals(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_equals(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_equals(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    860            ~           1255    65125    st_estimated_extent(text, text)    FUNCTION     R  CREATE FUNCTION public.st_estimated_extent(text, text) RETURNS public.box2d
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	-- We use security invoker instead of security definer
	-- to prevent malicious injection of a same named different function
	-- that would be run under elevated permissions
	SELECT ST_EstimatedExtent($1, $2);
	$_$;
 6   DROP FUNCTION public.st_estimated_extent(text, text);
       public          tellervo    false    3    3    3                       1255    65126 %   st_estimated_extent(text, text, text)    FUNCTION     +  CREATE FUNCTION public.st_estimated_extent(text, text, text) RETURNS public.box2d
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	-- We use security invoker instead of security definer
	-- to prevent malicious injection of a different same named function
	SELECT ST_EstimatedExtent($1, $2, $3);
	$_$;
 <   DROP FUNCTION public.st_estimated_extent(text, text, text);
       public          tellervo    false    3    3    3            �           0    0 )   FUNCTION st_exteriorring(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_exteriorring(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_exteriorring(public.geometry) TO "Webgroup";
          public          postgres    false    923            �           1255    65127    st_find_extent(text, text)    FUNCTION     �   CREATE FUNCTION public.st_find_extent(text, text) RETURNS public.box2d
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_FindExtent($1,$2);$_$;
 1   DROP FUNCTION public.st_find_extent(text, text);
       public          tellervo    false    3    3    3            �           1255    65128     st_find_extent(text, text, text)    FUNCTION     �   CREATE FUNCTION public.st_find_extent(text, text, text) RETURNS public.box2d
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_FindExtent($1,$2,$3);$_$;
 7   DROP FUNCTION public.st_find_extent(text, text, text);
       public          tellervo    false    3    3    3            �           1255    65129    st_force_2d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_force_2d(public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_Force2D($1);$_$;
 3   DROP FUNCTION public.st_force_2d(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65130    st_force_3d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_force_3d(public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_Force3D($1);$_$;
 3   DROP FUNCTION public.st_force_3d(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65131    st_force_3dm(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_force_3dm(public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_Force3DM($1);$_$;
 4   DROP FUNCTION public.st_force_3dm(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65132    st_force_3dz(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_force_3dz(public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_Force3DZ($1);$_$;
 4   DROP FUNCTION public.st_force_3dz(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65133    st_force_4d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_force_4d(public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_Force4D($1);$_$;
 3   DROP FUNCTION public.st_force_4d(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65134 $   st_force_collection(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_force_collection(public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_ForceCollection($1);$_$;
 ;   DROP FUNCTION public.st_force_collection(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65135    st_geometry(bytea)    FUNCTION     �   CREATE FUNCTION public.st_geometry(bytea) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_from_bytea';
 )   DROP FUNCTION public.st_geometry(bytea);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    65136    st_geometry(text)    FUNCTION     �   CREATE FUNCTION public.st_geometry(text) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'parse_WKT_lwgeom';
 (   DROP FUNCTION public.st_geometry(text);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    65137    st_geometry(public.box2d)    FUNCTION     �   CREATE FUNCTION public.st_geometry(public.box2d) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX2D_to_LWGEOM';
 0   DROP FUNCTION public.st_geometry(public.box2d);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3            �           1255    65138    st_geometry(public.box3d)    FUNCTION     �   CREATE FUNCTION public.st_geometry(public.box3d) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_to_LWGEOM';
 0   DROP FUNCTION public.st_geometry(public.box3d);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3            �           1255    65139 1   st_geometry_cmp(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_geometry_cmp(public.geometry, public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'lwgeom_cmp';
 H   DROP FUNCTION public.st_geometry_cmp(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65140 0   st_geometry_eq(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_geometry_eq(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'lwgeom_eq';
 G   DROP FUNCTION public.st_geometry_eq(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65141 0   st_geometry_ge(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_geometry_ge(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'lwgeom_ge';
 G   DROP FUNCTION public.st_geometry_ge(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65142 0   st_geometry_gt(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_geometry_gt(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'lwgeom_gt';
 G   DROP FUNCTION public.st_geometry_gt(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65143 0   st_geometry_le(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_geometry_le(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'lwgeom_le';
 G   DROP FUNCTION public.st_geometry_le(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65144 0   st_geometry_lt(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_geometry_lt(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'lwgeom_lt';
 G   DROP FUNCTION public.st_geometry_lt(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           0    0 /   FUNCTION st_geometryn(public.geometry, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_geometryn(public.geometry, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_geometryn(public.geometry, integer) TO "Webgroup";
          public          postgres    false    921            �           0    0 )   FUNCTION st_geometrytype(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_geometrytype(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_geometrytype(public.geometry) TO "Webgroup";
          public          postgres    false    928            �           0    0 '   FUNCTION st_geomfromtext(text, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_geomfromtext(text, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_geomfromtext(text, integer) TO "Webgroup";
          public          postgres    false    943            �           0    0 '   FUNCTION st_geomfromwkb(bytea, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_geomfromwkb(bytea, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_geomfromwkb(bytea, integer) TO "Webgroup";
          public          postgres    false    967            �           0    0 3   FUNCTION st_interiorringn(public.geometry, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_interiorringn(public.geometry, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_interiorringn(public.geometry, integer) TO "Webgroup";
          public          postgres    false    926            �           0    0 D   FUNCTION st_intersects(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_intersects(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_intersects(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    847            �           0    0 %   FUNCTION st_isclosed(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_isclosed(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_isclosed(public.geometry) TO "Webgroup";
          public          postgres    false    934            �           0    0 $   FUNCTION st_isempty(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_isempty(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_isempty(public.geometry) TO "Webgroup";
          public          postgres    false    935            �           0    0 #   FUNCTION st_isring(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_isring(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_isring(public.geometry) TO "Webgroup";
          public          postgres    false    866            �           0    0 %   FUNCTION st_issimple(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_issimple(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_issimple(public.geometry) TO "Webgroup";
          public          postgres    false    868            �           0    0 $   FUNCTION st_isvalid(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_isvalid(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_isvalid(public.geometry) TO "Webgroup";
          public          postgres    false    861            �           0    0 #   FUNCTION st_length(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_length(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_length(public.geometry) TO "Webgroup";
          public          postgres    false    564            �           1255    65145 6   st_length2d_spheroid(public.geometry, public.spheroid)    FUNCTION     �   CREATE FUNCTION public.st_length2d_spheroid(public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_Length2DSpheroid($1,$2);$_$;
 M   DROP FUNCTION public.st_length2d_spheroid(public.geometry, public.spheroid);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3            �           1255    65146    st_length3d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_length3d(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_length_linestring';
 3   DROP FUNCTION public.st_length3d(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    65147 4   st_length_spheroid(public.geometry, public.spheroid)    FUNCTION     �   CREATE FUNCTION public.st_length_spheroid(public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_LengthSpheroid($1,$2);$_$;
 K   DROP FUNCTION public.st_length_spheroid(public.geometry, public.spheroid);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3            �           1255    65148 6   st_length_spheroid3d(public.geometry, public.spheroid)    FUNCTION     �   CREATE FUNCTION public.st_length_spheroid3d(public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'LWGEOM_length_ellipsoid_linestring';
 M   DROP FUNCTION public.st_length_spheroid3d(public.geometry, public.spheroid);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3            �           1255    65149 <   st_line_interpolate_point(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.st_line_interpolate_point(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_LineInterpolatePoint($1, $2);$_$;
 S   DROP FUNCTION public.st_line_interpolate_point(public.geometry, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65150 6   st_line_locate_point(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_line_locate_point(geom1 public.geometry, geom2 public.geometry) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_LineLocatePoint($1, $2);$_$;
 Y   DROP FUNCTION public.st_line_locate_point(geom1 public.geometry, geom2 public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65151 F   st_line_substring(public.geometry, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.st_line_substring(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_LineSubstring($1, $2, $3);$_$;
 ]   DROP FUNCTION public.st_line_substring(public.geometry, double precision, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           0    0 '   FUNCTION st_linefromtext(text, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_linefromtext(text, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_linefromtext(text, integer) TO "Webgroup";
          public          postgres    false    948            �           0    0 '   FUNCTION st_linefromwkb(bytea, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_linefromwkb(bytea, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_linefromwkb(bytea, integer) TO "Webgroup";
          public          postgres    false    970            �           1255    65152 :   st_locate_along_measure(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.st_locate_along_measure(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_LocateBetween($1, $2, $2);$_$;
 Q   DROP FUNCTION public.st_locate_along_measure(public.geometry, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65153 O   st_locate_between_measures(public.geometry, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.st_locate_between_measures(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_LocateBetween($1, $2, $2);$_$;
 f   DROP FUNCTION public.st_locate_between_measures(public.geometry, double precision, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65154 .   st_makebox3d(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_makebox3d(public.geometry, public.geometry) RETURNS public.box3d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_construct';
 E   DROP FUNCTION public.st_makebox3d(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65155 %   st_makeline_garray(public.geometry[])    FUNCTION     �   CREATE FUNCTION public.st_makeline_garray(public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_makeline_garray';
 <   DROP FUNCTION public.st_makeline_garray(public.geometry[]);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65156    st_mem_size(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_mem_size(public.geometry) RETURNS integer
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_MemSize($1);$_$;
 3   DROP FUNCTION public.st_mem_size(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           0    0 (   FUNCTION st_mlinefromtext(text, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_mlinefromtext(text, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_mlinefromtext(text, integer) TO "Webgroup";
          public          postgres    false    953            �           0    0 (   FUNCTION st_mlinefromwkb(bytea, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_mlinefromwkb(bytea, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_mlinefromwkb(bytea, integer) TO "Webgroup";
          public          postgres    false    983            �           0    0 )   FUNCTION st_mpointfromtext(text, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_mpointfromtext(text, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_mpointfromtext(text, integer) TO "Webgroup";
          public          postgres    false    957            �           0    0 )   FUNCTION st_mpointfromwkb(bytea, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_mpointfromwkb(bytea, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_mpointfromwkb(bytea, integer) TO "Webgroup";
          public          postgres    false    978            �           0    0 (   FUNCTION st_mpolyfromtext(text, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_mpolyfromtext(text, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_mpolyfromtext(text, integer) TO "Webgroup";
          public          postgres    false    960            �           0    0 (   FUNCTION st_mpolyfromwkb(bytea, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_mpolyfromwkb(bytea, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_mpolyfromwkb(bytea, integer) TO "Webgroup";
          public          postgres    false    985            �           0    0 *   FUNCTION st_numgeometries(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_numgeometries(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_numgeometries(public.geometry) TO "Webgroup";
          public          postgres    false    920            �           0    0 ,   FUNCTION st_numinteriorring(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_numinteriorring(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_numinteriorring(public.geometry) TO "Webgroup";
          public          postgres    false    925            �           0    0 &   FUNCTION st_numpoints(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_numpoints(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_numpoints(public.geometry) TO "Webgroup";
          public          postgres    false    919            �           0    0 H   FUNCTION st_orderingequals(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_orderingequals(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_orderingequals(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    859            �           0    0 B   FUNCTION st_overlaps(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_overlaps(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_overlaps(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    854            �           0    0 &   FUNCTION st_perimeter(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_perimeter(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_perimeter(public.geometry) TO "Webgroup";
          public          postgres    false    569            �           1255    65157    st_perimeter3d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_perimeter3d(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_perimeter_poly';
 6   DROP FUNCTION public.st_perimeter3d(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           0    0 5   FUNCTION st_point(double precision, double precision)    ACL     �   GRANT ALL ON FUNCTION public.st_point(double precision, double precision) TO pbrewer;
GRANT ALL ON FUNCTION public.st_point(double precision, double precision) TO "Webgroup";
          public          postgres    false    1144            �           1255    65158 ]   st_point_inside_circle(public.geometry, double precision, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.st_point_inside_circle(public.geometry, double precision, double precision, double precision) RETURNS boolean
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_PointInsideCircle($1,$2,$3,$4);$_$;
 t   DROP FUNCTION public.st_point_inside_circle(public.geometry, double precision, double precision, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           0    0 (   FUNCTION st_pointfromtext(text, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_pointfromtext(text, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_pointfromtext(text, integer) TO "Webgroup";
          public          postgres    false    946            �           0    0 (   FUNCTION st_pointfromwkb(bytea, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_pointfromwkb(bytea, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_pointfromwkb(bytea, integer) TO "Webgroup";
          public          postgres    false    968            �           0    0 +   FUNCTION st_pointonsurface(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_pointonsurface(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_pointonsurface(public.geometry) TO "Webgroup";
          public          postgres    false    867            �           0    0 '   FUNCTION st_polyfromtext(text, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_polyfromtext(text, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_polyfromtext(text, integer) TO "Webgroup";
          public          postgres    false    950            �           0    0 '   FUNCTION st_polyfromwkb(bytea, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_polyfromwkb(bytea, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_polyfromwkb(bytea, integer) TO "Webgroup";
          public          postgres    false    974            �           0    0 -   FUNCTION st_polygon(public.geometry, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_polygon(public.geometry, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_polygon(public.geometry, integer) TO "Webgroup";
          public          postgres    false    1149            �           1255    65159 '   st_polygonize_garray(public.geometry[])    FUNCTION     �   CREATE FUNCTION public.st_polygonize_garray(public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'polygonize_garray';
 >   DROP FUNCTION public.st_polygonize_garray(public.geometry[]);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           0    0 F   FUNCTION st_relate(geom1 public.geometry, geom2 public.geometry, text)    ACL     �   GRANT ALL ON FUNCTION public.st_relate(geom1 public.geometry, geom2 public.geometry, text) TO pbrewer;
GRANT ALL ON FUNCTION public.st_relate(geom1 public.geometry, geom2 public.geometry, text) TO "Webgroup";
          public          postgres    false    824            �           1255    65160 #   st_shift_longitude(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_shift_longitude(public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_ShiftLongitude($1);$_$;
 :   DROP FUNCTION public.st_shift_longitude(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           0    0 '   FUNCTION st_startpoint(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_startpoint(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_startpoint(public.geometry) TO "Webgroup";
          public          postgres    false    932            �           1255    65161    st_text(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_text(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_to_text';
 /   DROP FUNCTION public.st_text(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           0    0 A   FUNCTION st_touches(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_touches(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_touches(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    846            �           0    0 /   FUNCTION st_transform(public.geometry, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_transform(public.geometry, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_transform(public.geometry, integer) TO "Webgroup";
          public          postgres    false    694            �           0    0 ?   FUNCTION st_union(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_union(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_union(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    777            �           1255    65162 "   st_unite_garray(public.geometry[])    FUNCTION     �   CREATE FUNCTION public.st_unite_garray(public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'pgis_union_geometry_array';
 9   DROP FUNCTION public.st_unite_garray(public.geometry[]);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           0    0 @   FUNCTION st_within(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_within(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_within(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
          public          postgres    false    851            �           0    0    FUNCTION st_wkbtosql(wkb bytea)    ACL     �   GRANT ALL ON FUNCTION public.st_wkbtosql(wkb bytea) TO pbrewer;
GRANT ALL ON FUNCTION public.st_wkbtosql(wkb bytea) TO "Webgroup";
          public          postgres    false    1150            �           0    0    FUNCTION st_wkttosql(text)    ACL     y   GRANT ALL ON FUNCTION public.st_wkttosql(text) TO pbrewer;
GRANT ALL ON FUNCTION public.st_wkttosql(text) TO "Webgroup";
          public          postgres    false    944            �           0    0    FUNCTION st_x(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_x(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_x(public.geometry) TO "Webgroup";
          public          postgres    false    472            �           0    0    FUNCTION st_y(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_y(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_y(public.geometry) TO "Webgroup";
          public          postgres    false    473            �           1255    65163    startpoint(public.geometry)    FUNCTION     �   CREATE FUNCTION public.startpoint(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_startpoint_linestring';
 2   DROP FUNCTION public.startpoint(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65164    summary(public.geometry)    FUNCTION     �   CREATE FUNCTION public.summary(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_summary';
 /   DROP FUNCTION public.summary(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    65165 /   symdifference(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.symdifference(public.geometry, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'symdifference';
 F   DROP FUNCTION public.symdifference(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65166 5   symmetricdifference(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.symmetricdifference(public.geometry, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'symdifference';
 L   DROP FUNCTION public.symmetricdifference(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65167 )   touches(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.touches(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'touches';
 @   DROP FUNCTION public.touches(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65168 #   transform(public.geometry, integer)    FUNCTION     �   CREATE FUNCTION public.transform(public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'transform';
 :   DROP FUNCTION public.transform(public.geometry, integer);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65169 >   translate(public.geometry, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.translate(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_translate($1, $2, $3, 0)$_$;
 U   DROP FUNCTION public.translate(public.geometry, double precision, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65170 P   translate(public.geometry, double precision, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.translate(public.geometry, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_affine($1, 1, 0, 0, 0, 1, 0, 0, 0, 1, $2, $3, $4)$_$;
 g   DROP FUNCTION public.translate(public.geometry, double precision, double precision, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65171 c   transscale(public.geometry, double precision, double precision, double precision, double precision)    FUNCTION       CREATE FUNCTION public.transscale(public.geometry, double precision, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_affine($1,  $4, 0, 0,  0, $5, 0,
		0, 0, 1,  $2 * $4, $3 * $5, 0)$_$;
 z   DROP FUNCTION public.transscale(public.geometry, double precision, double precision, double precision, double precision);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65172    unite_garray(public.geometry[])    FUNCTION     �   CREATE FUNCTION public.unite_garray(public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'pgis_union_geometry_array';
 6   DROP FUNCTION public.unite_garray(public.geometry[]);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           0    0    FUNCTION unlockrows(text)    ACL     w   GRANT ALL ON FUNCTION public.unlockrows(text) TO pbrewer;
GRANT ALL ON FUNCTION public.unlockrows(text) TO "Webgroup";
          public          postgres    false    1001            �           1255    65173    update_lastmodifiedtimestamp()    FUNCTION     �   CREATE FUNCTION public.update_lastmodifiedtimestamp() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
NEW.lastmodifiedtimestamp = current_timestamp;
return new;
End;$$;
 5   DROP FUNCTION public.update_lastmodifiedtimestamp();
       public          postgres    false            �           0    0 '   FUNCTION update_lastmodifiedtimestamp()    ACL     K   GRANT ALL ON FUNCTION public.update_lastmodifiedtimestamp() TO "Webgroup";
          public          postgres    false    1710            �           1255    65174    update_layerenvdata()    FUNCTION     �   CREATE FUNCTION public.update_layerenvdata() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
  EXECUTE cpgdb.lookupenvdatabylayer(NEW.rasterlayerid);
  RETURN NULL;
END;$$;
 ,   DROP FUNCTION public.update_layerenvdata();
       public          postgres    false            �           1255    65175    update_objectextentontreeedit()    FUNCTION     	  CREATE FUNCTION public.update_objectextentontreeedit() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
        IF NEW.location is not null THEN
                PERFORM cpgdb.update_objectextentbyobject(NEW.objectid);
        END IF;
        RETURN NEW;
END;$$;
 6   DROP FUNCTION public.update_objectextentontreeedit();
       public          postgres    false            �           1255    65176 $   update_odkformdef-versionincrement()    FUNCTION     �   CREATE FUNCTION public."update_odkformdef-versionincrement"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
NEW.version = OLD.version+1;
RETURN NEW;
END;$$;
 =   DROP FUNCTION public."update_odkformdef-versionincrement"();
       public          tellervo    false            �           1255    65177 !   update_samplingdateatomicfields()    FUNCTION     $  CREATE FUNCTION public.update_samplingdateatomicfields() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
IF NEW.samplingdate IS NOT NULL THEN
  NEW.samplingyear = date_part('year', NEW.samplingdate);
  NEW.samplingmonth = date_part('month', NEW.samplingdate);
END IF;
RETURN new;
END;$$;
 8   DROP FUNCTION public.update_samplingdateatomicfields();
       public          tellervo    false            �           1255    65178    update_treeenvdata()    FUNCTION     u  CREATE FUNCTION public.update_treeenvdata() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
  IF TG_OP = 'UPDATE' THEN
    IF OLD.location != NEW.location THEN
      EXECUTE cpgdb.lookupenvdatabytree(NEW.elementid);
    END IF;
  ELSE
    IF NEW.location IS NOT NULL THEN
	EXECUTE cpgdb.lookupenvdatabytree(NEW.elementid);
    END IF;
  END IF;

  RETURN NULL;
END;$$;
 +   DROP FUNCTION public.update_treeenvdata();
       public          postgres    false            �           0    0 J   FUNCTION updategeometrysrid(character varying, character varying, integer)    ACL     �   GRANT ALL ON FUNCTION public.updategeometrysrid(character varying, character varying, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.updategeometrysrid(character varying, character varying, integer) TO "Webgroup";
          public          postgres    false    684            �           0    0 ]   FUNCTION updategeometrysrid(character varying, character varying, character varying, integer)    ACL     �   GRANT ALL ON FUNCTION public.updategeometrysrid(character varying, character varying, character varying, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.updategeometrysrid(character varying, character varying, character varying, integer) TO "Webgroup";
          public          postgres    false    683            �           0    0 �   FUNCTION updategeometrysrid(catalogn_name character varying, schema_name character varying, table_name character varying, column_name character varying, new_srid_in integer)    ACL     �  GRANT ALL ON FUNCTION public.updategeometrysrid(catalogn_name character varying, schema_name character varying, table_name character varying, column_name character varying, new_srid_in integer) TO pbrewer;
GRANT ALL ON FUNCTION public.updategeometrysrid(catalogn_name character varying, schema_name character varying, table_name character varying, column_name character varying, new_srid_in integer) TO "Webgroup";
          public          postgres    false    682            �           1255    65179    uuid_generate_v1()    FUNCTION     }   CREATE FUNCTION public.uuid_generate_v1() RETURNS uuid
    LANGUAGE c STRICT
    AS '$libdir/uuid-ossp', 'uuid_generate_v1';
 )   DROP FUNCTION public.uuid_generate_v1();
       public          postgres    false            �           1255    65180    uuid_generate_v3(uuid, text)    FUNCTION     �   CREATE FUNCTION public.uuid_generate_v3(namespace uuid, name text) RETURNS uuid
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/uuid-ossp', 'uuid_generate_v3';
 B   DROP FUNCTION public.uuid_generate_v3(namespace uuid, name text);
       public          postgres    false            �           1255    65181    uuid_generate_v4()    FUNCTION     }   CREATE FUNCTION public.uuid_generate_v4() RETURNS uuid
    LANGUAGE c STRICT
    AS '$libdir/uuid-ossp', 'uuid_generate_v4';
 )   DROP FUNCTION public.uuid_generate_v4();
       public          postgres    false            �           1255    65182    uuid_generate_v5(uuid, text)    FUNCTION     �   CREATE FUNCTION public.uuid_generate_v5(namespace uuid, name text) RETURNS uuid
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/uuid-ossp', 'uuid_generate_v5';
 B   DROP FUNCTION public.uuid_generate_v5(namespace uuid, name text);
       public          postgres    false            �           1255    65183 
   uuid_nil()    FUNCTION     w   CREATE FUNCTION public.uuid_nil() RETURNS uuid
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/uuid-ossp', 'uuid_nil';
 !   DROP FUNCTION public.uuid_nil();
       public          postgres    false            �           1255    65184    uuid_ns_dns()    FUNCTION     }   CREATE FUNCTION public.uuid_ns_dns() RETURNS uuid
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/uuid-ossp', 'uuid_ns_dns';
 $   DROP FUNCTION public.uuid_ns_dns();
       public          postgres    false            �           1255    65185    uuid_ns_oid()    FUNCTION     }   CREATE FUNCTION public.uuid_ns_oid() RETURNS uuid
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/uuid-ossp', 'uuid_ns_oid';
 $   DROP FUNCTION public.uuid_ns_oid();
       public          postgres    false            �           1255    65186    uuid_ns_url()    FUNCTION     }   CREATE FUNCTION public.uuid_ns_url() RETURNS uuid
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/uuid-ossp', 'uuid_ns_url';
 $   DROP FUNCTION public.uuid_ns_url();
       public          postgres    false            �           1255    65187    uuid_ns_x500()    FUNCTION        CREATE FUNCTION public.uuid_ns_x500() RETURNS uuid
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/uuid-ossp', 'uuid_ns_x500';
 %   DROP FUNCTION public.uuid_ns_x500();
       public          postgres    false            �           1255    65188 (   within(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.within(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_Within($1, $2)$_$;
 ?   DROP FUNCTION public.within(public.geometry, public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1255    65189    x(public.geometry)    FUNCTION     �   CREATE FUNCTION public.x(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_x_point';
 )   DROP FUNCTION public.x(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    65190    xmax(public.box3d)    FUNCTION     �   CREATE FUNCTION public.xmax(public.box3d) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_xmax';
 )   DROP FUNCTION public.xmax(public.box3d);
       public          tellervo    false    3    3    3            �           1255    65191    xmin(public.box3d)    FUNCTION     �   CREATE FUNCTION public.xmin(public.box3d) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_xmin';
 )   DROP FUNCTION public.xmin(public.box3d);
       public          tellervo    false    3    3    3            �           1255    65192    y(public.geometry)    FUNCTION     �   CREATE FUNCTION public.y(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_y_point';
 )   DROP FUNCTION public.y(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    65193    ymax(public.box3d)    FUNCTION     �   CREATE FUNCTION public.ymax(public.box3d) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_ymax';
 )   DROP FUNCTION public.ymax(public.box3d);
       public          tellervo    false    3    3    3            �           1255    65194    ymin(public.box3d)    FUNCTION     �   CREATE FUNCTION public.ymin(public.box3d) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_ymin';
 )   DROP FUNCTION public.ymin(public.box3d);
       public          tellervo    false    3    3    3            �           1255    65195    z(public.geometry)    FUNCTION     �   CREATE FUNCTION public.z(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_z_point';
 )   DROP FUNCTION public.z(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    65196    zmax(public.box3d)    FUNCTION     �   CREATE FUNCTION public.zmax(public.box3d) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_zmax';
 )   DROP FUNCTION public.zmax(public.box3d);
       public          tellervo    false    3    3    3            �           1255    65197    zmflag(public.geometry)    FUNCTION     �   CREATE FUNCTION public.zmflag(public.geometry) RETURNS smallint
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_zmflag';
 .   DROP FUNCTION public.zmflag(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3            �           1255    65198    zmin(public.box3d)    FUNCTION     �   CREATE FUNCTION public.zmin(public.box3d) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_zmin';
 )   DROP FUNCTION public.zmin(public.box3d);
       public          tellervo    false    3    3    3            �
           1255    65199    array_accum(anyelement) 	   AGGREGATE     y   CREATE AGGREGATE public.array_accum(anyelement) (
    SFUNC = array_append,
    STYPE = anyarray,
    INITCOND = '{}'
);
 /   DROP AGGREGATE public.array_accum(anyelement);
       public          postgres    false            �
           1255    65200    extent(public.geometry) 	   AGGREGATE     �   CREATE AGGREGATE public.extent(public.geometry) (
    SFUNC = public.st_combinebbox,
    STYPE = public.box3d,
    FINALFUNC = public.box2d
);
 /   DROP AGGREGATE public.extent(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �
           1255    65201    extent3d(public.geometry) 	   AGGREGATE     q   CREATE AGGREGATE public.extent3d(public.geometry) (
    SFUNC = public.combine_bbox,
    STYPE = public.box3d
);
 1   DROP AGGREGATE public.extent3d(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    1373                        1255    65202    first(anyelement) 	   AGGREGATE     d   CREATE AGGREGATE public.first(anyelement) (
    SFUNC = public.agg_first,
    STYPE = anyelement
);
 )   DROP AGGREGATE public.first(anyelement);
       public          postgres    false    1339                       1255    65203    makeline(public.geometry) 	   AGGREGATE     �   CREATE AGGREGATE public.makeline(public.geometry) (
    SFUNC = public.pgis_geometry_accum_transfn,
    STYPE = internal,
    FINALFUNC = public.pgis_geometry_makeline_finalfn
);
 1   DROP AGGREGATE public.makeline(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3                       1255    65204    memcollect(public.geometry) 	   AGGREGATE     t   CREATE AGGREGATE public.memcollect(public.geometry) (
    SFUNC = public.st_collect,
    STYPE = public.geometry
);
 3   DROP AGGREGATE public.memcollect(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3                       1255    65205    memgeomunion(public.geometry) 	   AGGREGATE     u   CREATE AGGREGATE public.memgeomunion(public.geometry) (
    SFUNC = public.geomunion,
    STYPE = public.geometry
);
 5   DROP AGGREGATE public.memgeomunion(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    1440    3    3    3    3    3    3    3    3                       1255    65206    st_extent3d(public.geometry) 	   AGGREGATE     v   CREATE AGGREGATE public.st_extent3d(public.geometry) (
    SFUNC = public.st_combinebbox,
    STYPE = public.box3d
);
 4   DROP AGGREGATE public.st_extent3d(public.geometry);
       public          tellervo    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            Z           2753    65207    gist_geometry_ops    OPERATOR FAMILY     <   CREATE OPERATOR FAMILY public.gist_geometry_ops USING gist;
 :   DROP OPERATOR FAMILY public.gist_geometry_ops USING gist;
       public          postgres    false            �            1259    65235    tblbox    TABLE     �  CREATE TABLE public.tblbox (
    boxid uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    title character varying NOT NULL,
    curationlocation character varying,
    trackinglocation character varying,
    createdtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifiedtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    comments character varying
);
    DROP TABLE public.tblbox;
       public         heap    postgres    false    1718            �           0    0    TABLE tblbox    ACL     0   GRANT ALL ON TABLE public.tblbox TO "Webgroup";
          public          postgres    false    246                       1259    65338 
   tblproject    TABLE     �  CREATE TABLE public.tblproject (
    projectid uuid DEFAULT public.uuid_generate_v1mc() NOT NULL,
    domainid integer DEFAULT 0,
    title character varying NOT NULL,
    createdtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifiedtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    comments character varying,
    description character varying,
    file character varying[],
    projectcategoryid integer,
    investigator character varying,
    period character varying,
    requestdate date,
    commissioner character varying,
    reference character varying[],
    research integer,
    projecttypes character varying[],
    laboratories character varying[]
);
    DROP TABLE public.tblproject;
       public         heap    tellervo    false    1211            3           1259    65478    tbluserdefinedfieldvalue    TABLE     �   CREATE TABLE public.tbluserdefinedfieldvalue (
    userdefinedfieldvalueid uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    userdefinedfieldid uuid NOT NULL,
    value character varying,
    entityid uuid NOT NULL
);
 ,   DROP TABLE public.tbluserdefinedfieldvalue;
       public         heap    tellervo    false    1718            j           1259    65700    tlkpsampletype    TABLE     y   CREATE TABLE public.tlkpsampletype (
    sampletypeid integer NOT NULL,
    sampletype character varying(32) NOT NULL
);
 "   DROP TABLE public.tlkpsampletype;
       public         heap    postgres    false            �           0    0    TABLE tlkpsampletype    ACL     8   GRANT ALL ON TABLE public.tlkpsampletype TO "Webgroup";
          public          postgres    false    362            o           1259    65712 	   tlkptaxon    TABLE     .  CREATE TABLE public.tlkptaxon (
    colid character varying(36) NOT NULL,
    colparentid character varying(36),
    taxonrankid integer NOT NULL,
    label character varying(128) NOT NULL,
    htmllabel character varying,
    taxonid character varying NOT NULL,
    parenttaxonid character varying
);
    DROP TABLE public.tlkptaxon;
       public         heap    postgres    false            �           0    0    TABLE tlkptaxon    ACL     3   GRANT ALL ON TABLE public.tlkptaxon TO "Webgroup";
          public          postgres    false    367            s           1259    65728    tlkpuserdefinedfield    TABLE     �  CREATE TABLE public.tlkpuserdefinedfield (
    userdefinedfieldid uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    fieldname character varying NOT NULL,
    description character varying NOT NULL,
    attachedto integer NOT NULL,
    datatype character varying DEFAULT 'xs:string'::character varying NOT NULL,
    longfieldname character varying NOT NULL,
    dictionarykey character varying,
    CONSTRAINT check_userdefinedfields_validattachedto CHECK (((attachedto >= 1) AND (attachedto <= 6)))
);
 (   DROP TABLE public.tlkpuserdefinedfield;
       public         heap    tellervo    false    1718            �           0    0 &   COLUMN tlkpuserdefinedfield.attachedto    COMMENT     �   COMMENT ON COLUMN public.tlkpuserdefinedfield.attachedto IS 'Integer indicated at what tridas level this field is attached.  1=project, 2=object, 3=element, 4=sample, 5=radius, 6=measurementseries';
          public          tellervo    false    371            �           1259    66888    vwfirstyear    VIEW     �  CREATE VIEW public.vwfirstyear AS
 SELECT tbluserdefinedfieldvalue.entityid,
    tbluserdefinedfieldvalue.value
   FROM public.tbluserdefinedfieldvalue,
    public.tlkpuserdefinedfield
  WHERE ((tbluserdefinedfieldvalue.userdefinedfieldid = tlkpuserdefinedfield.userdefinedfieldid) AND (tlkpuserdefinedfield.userdefinedfieldid = '454f7ae1-cbdf-43c9-bf64-92ebd7842632'::uuid) AND (tbluserdefinedfieldvalue.value IS NOT NULL) AND (bit_length((tbluserdefinedfieldvalue.value)::text) > 0));
    DROP VIEW public.vwfirstyear;
       public          tellervo    false    371    307    307    307            �           1259    66892 
   vwlastyear    VIEW     �  CREATE VIEW public.vwlastyear AS
 SELECT tbluserdefinedfieldvalue.entityid,
    tbluserdefinedfieldvalue.value
   FROM public.tbluserdefinedfieldvalue,
    public.tlkpuserdefinedfield
  WHERE ((tbluserdefinedfieldvalue.userdefinedfieldid = tlkpuserdefinedfield.userdefinedfieldid) AND (tlkpuserdefinedfield.userdefinedfieldid = '41aba235-a06c-45f0-a07d-8e2000a93a5f'::uuid) AND (tbluserdefinedfieldvalue.value IS NOT NULL) AND (bit_length((tbluserdefinedfieldvalue.value)::text) > 0));
    DROP VIEW public.vwlastyear;
       public          tellervo    false    371    307    307    307            �           1259    66897    vwportaldata1    VIEW       CREATE VIEW portal.vwportaldata1 AS
 SELECT p.projectid,
    p.lastmodifiedtimestamp AS projectlastmodifiedtimestamp,
    p.title AS projecttitle,
    p.investigator,
    o.objectid,
    o.lastmodifiedtimestamp AS objectlastmodifiedtimestamp,
    o.title AS objecttitle,
    o.code AS objectcode,
    public.xmin((o.locationgeometry)::public.box3d) AS objectlongitude,
    public.ymin((o.locationgeometry)::public.box3d) AS objectlatitude,
    o.locationprecision AS objectlocationprecision,
    NULL::uuid AS object2id,
    NULL::timestamp with time zone AS object2lastmodifiedtimestamp,
    NULL::character varying AS object2title,
    NULL::character varying AS object2code,
    NULL::double precision AS object2longitude,
    NULL::double precision AS object2latitude,
    NULL::integer AS object2locationprecision,
    e.elementid,
    e.lastmodifiedtimestamp AS elementlastmodifiedtimestamp,
    e.code AS elementcode,
    e.taxonid,
    t.label AS taxonlabel,
    t.htmllabel AS taxonlabelhtml,
    public.xmin((e.locationgeometry)::public.box3d) AS elementlongitude,
    public.ymin((e.locationgeometry)::public.box3d) AS elementlatitude,
    e.locationprecision AS elementlocationprecision,
    s.sampleid,
    s.lastmodifiedtimestamp AS samplelastmodifiedtimestamp,
    s.code AS samplecode,
    s.externalid,
    s.samplingdate,
    s.samplingyear,
    s.samplingmonth,
    s.samplingdateprec,
    st.sampletype,
    public.convert_to_integer(((fy.value)::text)::character varying) AS firstyear,
    public.convert_to_integer(((ly.value)::text)::character varying) AS lastyear,
    s.dendrochronologist,
    b.boxid,
    b.title AS boxtitle,
    b.curationlocation,
    b.trackinglocation,
    cpgdb.preferreddouble(public.xmin((e.locationgeometry)::public.box3d), NULL::double precision, public.xmin((o.locationgeometry)::public.box3d)) AS preferredlongitude,
    cpgdb.preferreddouble(public.ymin((e.locationgeometry)::public.box3d), NULL::double precision, public.ymin((o.locationgeometry)::public.box3d)) AS preferredlatitude,
    cpgdb.preferreddouble(e.locationprecision, NULL::double precision, (o.locationprecision)::double precision) AS preferredlocationprecision
   FROM ((((((((public.tblproject p
     LEFT JOIN public.tblobject o ON ((p.projectid = o.projectid)))
     LEFT JOIN public.tblelement e ON ((o.objectid = e.objectid)))
     LEFT JOIN public.tlkptaxon t ON (((e.taxonid)::text = (t.taxonid)::text)))
     LEFT JOIN public.tblsample s ON ((e.elementid = s.elementid)))
     LEFT JOIN public.tlkpsampletype st ON ((s.typeid = st.sampletypeid)))
     LEFT JOIN public.tblbox b ON ((s.boxid = b.boxid)))
     LEFT JOIN public.vwfirstyear fy ON ((s.sampleid = fy.entityid)))
     LEFT JOIN public.vwlastyear ly ON ((s.sampleid = ly.entityid)))
  WHERE ((o.parentobjectid IS NULL) AND (s.sampleid IS NOT NULL));
     DROP VIEW portal.vwportaldata1;
       portal          tellervo    false    1741    421    421    420    420    1736    367    367    367    362    362    269    269    269    269    246    246    246    246    1731    1728    240    240    240    240    240    240    240    240    240    240    240    240    236    236    236    236    236    236    236    231    231    231    231    231    231    231    231    3    3    3    3    3    3    3    3    3    3    3    3    6    2218            �           1259    66902    vwportaldata2    VIEW     �  CREATE VIEW portal.vwportaldata2 AS
 SELECT p.projectid,
    p.lastmodifiedtimestamp AS projectlastmodifiedtimestamp,
    p.title AS projecttitle,
    p.investigator,
    o.objectid,
    o.lastmodifiedtimestamp AS objectlastmodifiedtimestamp,
    o.title AS objecttitle,
    o.code AS objectcode,
    public.xmin((o.locationgeometry)::public.box3d) AS objectlongitude,
    public.ymin((o.locationgeometry)::public.box3d) AS objectlatitude,
    o.locationprecision AS objectlocationprecision,
    o2.objectid AS object2id,
    o2.lastmodifiedtimestamp AS object2lastmodifiedtimestamp,
    o2.title AS object2title,
    o2.code AS object2code,
    public.xmin((o2.locationgeometry)::public.box3d) AS object2longitude,
    public.ymin((o2.locationgeometry)::public.box3d) AS object2latitude,
    o2.locationprecision AS object2locationprecision,
    e.elementid,
    e.lastmodifiedtimestamp AS elementlastmodifiedtimestamp,
    e.code AS elementcode,
    e.taxonid,
    t.label AS taxonlabel,
    t.htmllabel AS taxonlabelhtml,
    public.xmin((e.locationgeometry)::public.box3d) AS elementlongitude,
    public.ymin((e.locationgeometry)::public.box3d) AS elementlatitude,
    e.locationprecision AS elementlocationprecision,
    s.sampleid,
    s.lastmodifiedtimestamp AS samplelastmodifiedtimestamp,
    s.code AS samplecode,
    s.externalid,
    s.samplingdate,
    s.samplingyear,
    s.samplingmonth,
    s.samplingdateprec,
    st.sampletype,
    public.convert_to_integer(((fy.value)::text)::character varying) AS firstyear,
    public.convert_to_integer(((ly.value)::text)::character varying) AS lastyear,
    s.dendrochronologist,
    b.boxid,
    b.title AS boxtitle,
    b.curationlocation,
    b.trackinglocation,
    cpgdb.preferreddouble(public.xmin((e.locationgeometry)::public.box3d), public.xmin((o2.locationgeometry)::public.box3d), public.xmin((o.locationgeometry)::public.box3d)) AS preferredlongitude,
    cpgdb.preferreddouble(public.ymin((e.locationgeometry)::public.box3d), public.ymin((o2.locationgeometry)::public.box3d), public.ymin((o.locationgeometry)::public.box3d)) AS preferredlatitude,
    cpgdb.preferreddouble(e.locationprecision, (o2.locationprecision)::double precision, (o.locationprecision)::double precision) AS preferredlocationprecision
   FROM (((((((((public.tblproject p
     LEFT JOIN public.tblobject o ON ((p.projectid = o.projectid)))
     LEFT JOIN public.tblobject o2 ON ((o.objectid = o2.parentobjectid)))
     LEFT JOIN public.tblelement e ON ((o.objectid = e.objectid)))
     LEFT JOIN public.tlkptaxon t ON (((e.taxonid)::text = (t.taxonid)::text)))
     LEFT JOIN public.tblsample s ON ((e.elementid = s.elementid)))
     LEFT JOIN public.tlkpsampletype st ON ((s.typeid = st.sampletypeid)))
     LEFT JOIN public.tblbox b ON ((s.boxid = b.boxid)))
     LEFT JOIN public.vwfirstyear fy ON ((s.sampleid = fy.entityid)))
     LEFT JOIN public.vwlastyear ly ON ((s.sampleid = ly.entityid)))
  WHERE ((o2.parentobjectid IS NOT NULL) AND (s.sampleid IS NOT NULL));
     DROP VIEW portal.vwportaldata2;
       portal          tellervo    false    1741    421    421    420    420    1736    367    367    367    362    362    269    269    269    269    246    246    246    246    1731    1728    240    240    240    240    240    240    240    240    240    240    240    240    236    236    236    236    236    236    236    231    231    231    231    231    231    231    231    3    3    3    3    3    3    3    3    3    3    3    3    6    2218            �           1259    66907    vwportalcomb    VIEW        CREATE VIEW portal.vwportalcomb AS
 SELECT vwportaldata1.projectid,
    vwportaldata1.projectlastmodifiedtimestamp,
    vwportaldata1.projecttitle,
    vwportaldata1.investigator,
    vwportaldata1.objectid,
    vwportaldata1.objectlastmodifiedtimestamp,
    vwportaldata1.objecttitle,
    vwportaldata1.objectcode,
    vwportaldata1.objectlongitude,
    vwportaldata1.objectlatitude,
    vwportaldata1.objectlocationprecision,
    vwportaldata1.object2id,
    vwportaldata1.object2lastmodifiedtimestamp,
    vwportaldata1.object2title,
    vwportaldata1.object2code,
    vwportaldata1.object2longitude,
    vwportaldata1.object2latitude,
    vwportaldata1.object2locationprecision,
    vwportaldata1.elementid,
    vwportaldata1.elementlastmodifiedtimestamp,
    vwportaldata1.elementcode,
    vwportaldata1.taxonid,
    vwportaldata1.taxonlabel,
    vwportaldata1.taxonlabelhtml,
    vwportaldata1.elementlongitude,
    vwportaldata1.elementlatitude,
    vwportaldata1.elementlocationprecision,
    vwportaldata1.sampleid,
    vwportaldata1.samplelastmodifiedtimestamp,
    vwportaldata1.samplecode,
    vwportaldata1.externalid,
    vwportaldata1.samplingdate,
    vwportaldata1.samplingyear,
    vwportaldata1.samplingmonth,
    vwportaldata1.samplingdateprec,
    vwportaldata1.sampletype,
    vwportaldata1.firstyear,
    vwportaldata1.lastyear,
    vwportaldata1.dendrochronologist,
    vwportaldata1.boxid,
    vwportaldata1.boxtitle,
    vwportaldata1.curationlocation,
    vwportaldata1.trackinglocation,
    vwportaldata1.preferredlongitude,
    vwportaldata1.preferredlatitude,
    vwportaldata1.preferredlocationprecision
   FROM portal.vwportaldata1 vwportaldata1
UNION
 SELECT vwportaldata2.projectid,
    vwportaldata2.projectlastmodifiedtimestamp,
    vwportaldata2.projecttitle,
    vwportaldata2.investigator,
    vwportaldata2.objectid,
    vwportaldata2.objectlastmodifiedtimestamp,
    vwportaldata2.objecttitle,
    vwportaldata2.objectcode,
    vwportaldata2.objectlongitude,
    vwportaldata2.objectlatitude,
    vwportaldata2.objectlocationprecision,
    vwportaldata2.object2id,
    vwportaldata2.object2lastmodifiedtimestamp,
    vwportaldata2.object2title,
    vwportaldata2.object2code,
    vwportaldata2.object2longitude,
    vwportaldata2.object2latitude,
    vwportaldata2.object2locationprecision,
    vwportaldata2.elementid,
    vwportaldata2.elementlastmodifiedtimestamp,
    vwportaldata2.elementcode,
    vwportaldata2.taxonid,
    vwportaldata2.taxonlabel,
    vwportaldata2.taxonlabelhtml,
    vwportaldata2.elementlongitude,
    vwportaldata2.elementlatitude,
    vwportaldata2.elementlocationprecision,
    vwportaldata2.sampleid,
    vwportaldata2.samplelastmodifiedtimestamp,
    vwportaldata2.samplecode,
    vwportaldata2.externalid,
    vwportaldata2.samplingdate,
    vwportaldata2.samplingyear,
    vwportaldata2.samplingmonth,
    vwportaldata2.samplingdateprec,
    vwportaldata2.sampletype,
    vwportaldata2.firstyear,
    vwportaldata2.lastyear,
    vwportaldata2.dendrochronologist,
    vwportaldata2.boxid,
    vwportaldata2.boxtitle,
    vwportaldata2.curationlocation,
    vwportaldata2.trackinglocation,
    vwportaldata2.preferredlongitude,
    vwportaldata2.preferredlatitude,
    vwportaldata2.preferredlocationprecision
   FROM portal.vwportaldata2 vwportaldata2;
    DROP VIEW portal.vwportalcomb;
       portal          tellervo    false    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    423    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    422    2218    6            �            1259    65208    elementimporterror    TABLE     �  CREATE TABLE public.elementimporterror (
    id integer,
    boxname character varying(50),
    regioncode character varying(5),
    elementstartnum integer,
    elementendnum integer,
    samplecode character varying(1),
    taxa character varying(50),
    sitecode character varying(50),
    missing character varying(255),
    notes text,
    sampletype character varying(50),
    currentelement character varying,
    error character varying,
    fixed boolean DEFAULT false
);
 &   DROP TABLE public.elementimporterror;
       public         heap    postgres    false            �           0    0    TABLE geography_columns    ACL     8   GRANT ALL ON TABLE public.geography_columns TO pbrewer;
          public          postgres    false    217            �           0    0    TABLE geometry_columns    ACL     �   REVOKE SELECT ON TABLE public.geometry_columns FROM PUBLIC;
GRANT ALL ON TABLE public.geometry_columns TO pbrewer;
GRANT ALL ON TABLE public.geometry_columns TO "Webgroup";
          public          postgres    false    218            �            1259    65215 	   inventory    TABLE     u  CREATE TABLE public.inventory (
    id integer,
    boxname character varying(50),
    regioncode character varying(5),
    elementstartnum integer,
    elementendnum integer,
    samplecode character varying(2),
    taxa character varying(50),
    sitecode character varying(50),
    missing character varying(255),
    notes text,
    sampletype character varying(50)
);
    DROP TABLE public.inventory;
       public         heap    postgres    false            �            1259    65221    sampleimporterror    TABLE     �  CREATE TABLE public.sampleimporterror (
    id integer,
    boxname character varying(50),
    regioncode character varying(5),
    elementstartnum integer,
    elementendnum integer,
    samplecode character varying(1),
    taxa character varying(50),
    sitecode character varying(50),
    missing character varying(255),
    notes text,
    sampletype character varying(50),
    currentelement character varying,
    error character varying,
    fixed boolean DEFAULT false
);
 %   DROP TABLE public.sampleimporterror;
       public         heap    postgres    false            �            1259    65228    sampleimportfixerror    TABLE     �  CREATE TABLE public.sampleimportfixerror (
    id integer,
    boxname character varying(50),
    regioncode character varying(5),
    elementstartnum integer,
    elementendnum integer,
    samplecode character varying(1),
    taxa character varying(50),
    sitecode character varying(50),
    missing character varying(255),
    notes text,
    sampletype character varying(50),
    currentelement character varying,
    error character varying,
    fixed boolean DEFAULT false
);
 (   DROP TABLE public.sampleimportfixerror;
       public         heap    postgres    false            �           0    0    TABLE spatial_ref_sys    ACL     �   REVOKE SELECT ON TABLE public.spatial_ref_sys FROM PUBLIC;
GRANT ALL ON TABLE public.spatial_ref_sys TO pbrewer;
GRANT ALL ON TABLE public.spatial_ref_sys TO "Webgroup";
          public          postgres    false    215            �           1259    66939    staticvwipt    TABLE     �  CREATE TABLE public.staticvwipt (
    projectid uuid,
    ptitle character varying,
    pcreatedtimestamp timestamp with time zone,
    plastmodifiedtimestamp timestamp with time zone,
    pcomments character varying,
    pdescription character varying,
    pfile text,
    projectcategory character varying,
    recordedby character varying,
    pperiod character varying,
    projectreference character varying[],
    o1objectid uuid,
    o1title character varying(100),
    o1code character varying(20),
    elementid uuid,
    ecode character varying(100),
    acceptednameusageid character varying,
    decimallongitude double precision,
    decimallatitude double precision,
    coordinateuncertaintyinmeters double precision,
    locationremarks character varying,
    country character varying,
    stateprovince character varying,
    verbatimelevation double precision,
    kingdom character varying(128),
    phylum character varying(128),
    class character varying(128),
    "order" character varying(128),
    family character varying(128),
    subfamily character varying(128),
    genus character varying(128),
    subgenus character varying(128),
    infragenericepithet character varying(128),
    species character varying(128),
    infraspecificepithet character varying(128),
    cultivarepithet character varying(128),
    scientificname character varying(128),
    parentnameusageid character varying,
    taxonrank character varying(30),
    taxonomicstatus text,
    nomenclaturalcode text,
    occurrenceid uuid,
    recordnumber character varying,
    identifiedby character varying,
    day character varying,
    month character varying,
    year character varying,
    eventdate character varying,
    type character varying,
    modified text,
    language character varying,
    rightsholder character varying,
    institutionid character varying,
    institutioncode character varying,
    datasetname character varying,
    basisofrecord character varying,
    individualcount character varying,
    organismquantity character varying,
    organismquantitytype character varying,
    occurrencestatus character varying,
    preparations character varying,
    geodeticdatum character varying
);
    DROP TABLE public.staticvwipt;
       public         heap    tellervo    false            �           1259    66914    stblflattenedtaxonomy    TABLE     I  CREATE TABLE public.stblflattenedtaxonomy (
    taxonid character varying,
    taxonrank character varying(30),
    kingdom character varying(128),
    subkingdom character varying(128),
    phylum character varying(128),
    division character varying(128),
    class character varying(128),
    txorder character varying(128),
    family character varying(128),
    subfamily character varying(128),
    genus character varying(128),
    subgenus character varying(128),
    section character varying(128),
    subsection character varying(128),
    species character varying(128),
    specificepithet character varying,
    subspecies character varying(128),
    race character varying(128),
    variety character varying(128),
    subvariety character varying(128),
    form character varying(128),
    subform character varying(128)
);
 )   DROP TABLE public.stblflattenedtaxonomy;
       public         heap    tellervo    false            �            1259    65244 	   tblconfig    TABLE     �   CREATE TABLE public.tblconfig (
    configid integer NOT NULL,
    key character varying,
    value character varying,
    description character varying
);
    DROP TABLE public.tblconfig;
       public         heap    postgres    false            �            1259    65250    tblconfig_configid_seq    SEQUENCE        CREATE SEQUENCE public.tblconfig_configid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.tblconfig_configid_seq;
       public          postgres    false    247            �           0    0    tblconfig_configid_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.tblconfig_configid_seq OWNED BY public.tblconfig.configid;
          public          postgres    false    248            �            1259    65252    tblcrossdate    TABLE     r  CREATE TABLE public.tblcrossdate (
    crossdateid integer NOT NULL,
    vmeasurementid uuid NOT NULL,
    mastervmeasurementid uuid NOT NULL,
    startyear integer NOT NULL,
    justification text,
    confidence integer DEFAULT 1 NOT NULL,
    CONSTRAINT "chk_confidence-max" CHECK ((confidence <= 5)),
    CONSTRAINT "chk_confidence-min" CHECK ((confidence >= 0))
);
     DROP TABLE public.tblcrossdate;
       public         heap    postgres    false            �           0    0    TABLE tblcrossdate    ACL     6   GRANT ALL ON TABLE public.tblcrossdate TO "Webgroup";
          public          postgres    false    249            �            1259    65261    tblcrossdate_crossdateid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblcrossdate_crossdateid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 3   DROP SEQUENCE public.tblcrossdate_crossdateid_seq;
       public          postgres    false    249            �           0    0    tblcrossdate_crossdateid_seq    SEQUENCE OWNED BY     ]   ALTER SEQUENCE public.tblcrossdate_crossdateid_seq OWNED BY public.tblcrossdate.crossdateid;
          public          postgres    false    250            �           0    0 %   SEQUENCE tblcrossdate_crossdateid_seq    ACL     I   GRANT ALL ON SEQUENCE public.tblcrossdate_crossdateid_seq TO "Webgroup";
          public          postgres    false    250            �            1259    65263    tblcurationevent    TABLE     G  CREATE TABLE public.tblcurationevent (
    curationeventid bigint NOT NULL,
    curationstatusid integer NOT NULL,
    sampleid uuid,
    createdtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    loanid uuid,
    notes character varying,
    storagelocation character varying,
    curatorid uuid,
    boxid uuid
);
 $   DROP TABLE public.tblcurationevent;
       public         heap    tellervo    false            �            1259    65270    tblcuration_curationid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblcuration_curationid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 1   DROP SEQUENCE public.tblcuration_curationid_seq;
       public          tellervo    false    251            �           0    0    tblcuration_curationid_seq    SEQUENCE OWNED BY     c   ALTER SEQUENCE public.tblcuration_curationid_seq OWNED BY public.tblcurationevent.curationeventid;
          public          tellervo    false    252            �            1259    65272    tlkptrackinglocation    TABLE        CREATE TABLE public.tlkptrackinglocation (
    trackinglocationid integer NOT NULL,
    location character varying NOT NULL
);
 (   DROP TABLE public.tlkptrackinglocation;
       public         heap    postgres    false            �            1259    65278 *   tblcurationlocation_curationlocationid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblcurationlocation_curationlocationid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 A   DROP SEQUENCE public.tblcurationlocation_curationlocationid_seq;
       public          postgres    false    253            �           0    0 *   tblcurationlocation_curationlocationid_seq    SEQUENCE OWNED BY     z   ALTER SEQUENCE public.tblcurationlocation_curationlocationid_seq OWNED BY public.tlkptrackinglocation.trackinglocationid;
          public          postgres    false    254            �            1259    65280    tblcustomvocabterm    TABLE       CREATE TABLE public.tblcustomvocabterm (
    customvocabtermid integer NOT NULL,
    "table" character varying NOT NULL,
    field character varying NOT NULL,
    id integer NOT NULL,
    customvocabterm character varying NOT NULL,
    dictionaryname character varying
);
 &   DROP TABLE public.tblcustomvocabterm;
       public         heap    postgres    false                        1259    65286 (   tblcustomvocabterm_customvocabtermid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblcustomvocabterm_customvocabtermid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ?   DROP SEQUENCE public.tblcustomvocabterm_customvocabtermid_seq;
       public          postgres    false    255            �           0    0 (   tblcustomvocabterm_customvocabtermid_seq    SEQUENCE OWNED BY     u   ALTER SEQUENCE public.tblcustomvocabterm_customvocabtermid_seq OWNED BY public.tblcustomvocabterm.customvocabtermid;
          public          postgres    false    256                       1259    65288    tblelement_elementid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblelement_elementid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.tblelement_elementid_seq;
       public          postgres    false            �           0    0 !   SEQUENCE tblelement_elementid_seq    ACL     E   GRANT ALL ON SEQUENCE public.tblelement_elementid_seq TO "Webgroup";
          public          postgres    false    257                       1259    65290    tblelement_gispkey_seq    SEQUENCE        CREATE SEQUENCE public.tblelement_gispkey_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.tblelement_gispkey_seq;
       public          postgres    false    236            �           0    0    tblelement_gispkey_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.tblelement_gispkey_seq OWNED BY public.tblelement.gispkey;
          public          postgres    false    258            �           0    0    SEQUENCE tblelement_gispkey_seq    ACL     C   GRANT ALL ON SEQUENCE public.tblelement_gispkey_seq TO "Webgroup";
          public          postgres    false    258                       1259    65292    tblenvironmentaldata    TABLE     �   CREATE TABLE public.tblenvironmentaldata (
    environmentaldataid integer NOT NULL,
    elementid uuid NOT NULL,
    rasterlayerid integer NOT NULL,
    value double precision
);
 (   DROP TABLE public.tblenvironmentaldata;
       public         heap    postgres    false            �           0    0    TABLE tblenvironmentaldata    ACL     >   GRANT ALL ON TABLE public.tblenvironmentaldata TO "Webgroup";
          public          postgres    false    259                       1259    65295 ,   tblenvironmentaldata_environmentaldataid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblenvironmentaldata_environmentaldataid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 C   DROP SEQUENCE public.tblenvironmentaldata_environmentaldataid_seq;
       public          postgres    false    259            �           0    0 ,   tblenvironmentaldata_environmentaldataid_seq    SEQUENCE OWNED BY     }   ALTER SEQUENCE public.tblenvironmentaldata_environmentaldataid_seq OWNED BY public.tblenvironmentaldata.environmentaldataid;
          public          postgres    false    260            �           0    0 5   SEQUENCE tblenvironmentaldata_environmentaldataid_seq    ACL     Y   GRANT ALL ON SEQUENCE public.tblenvironmentaldata_environmentaldataid_seq TO "Webgroup";
          public          postgres    false    260                       1259    65297    tbliptracking    TABLE     �   CREATE TABLE public.tbliptracking (
    ipaddr inet NOT NULL,
    "timestamp" timestamp with time zone DEFAULT now() NOT NULL,
    securityuserid uuid
);
 !   DROP TABLE public.tbliptracking;
       public         heap    postgres    false            �           0    0    TABLE tbliptracking    ACL     7   GRANT ALL ON TABLE public.tbliptracking TO "Webgroup";
          public          postgres    false    261                       1259    65304    tbllaboratory    TABLE     h  CREATE TABLE public.tbllaboratory (
    laboratoryid uuid DEFAULT public.uuid_generate_v1mc() NOT NULL,
    name character varying NOT NULL,
    acronym character varying,
    address1 character varying,
    address2 character varying,
    city character varying,
    state character varying,
    postalcode character varying,
    country character varying
);
 !   DROP TABLE public.tbllaboratory;
       public         heap    tellervo    false    1211                       1259    65311     tblmeasurement_measurementid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblmeasurement_measurementid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 7   DROP SEQUENCE public.tblmeasurement_measurementid_seq;
       public          postgres    false    234            �           0    0     tblmeasurement_measurementid_seq    SEQUENCE OWNED BY     e   ALTER SEQUENCE public.tblmeasurement_measurementid_seq OWNED BY public.tblmeasurement.measurementid;
          public          postgres    false    263            �           0    0 )   SEQUENCE tblmeasurement_measurementid_seq    ACL     M   GRANT ALL ON SEQUENCE public.tblmeasurement_measurementid_seq TO "Webgroup";
          public          postgres    false    263                       1259    65313    tblobject_objectid_seq    SEQUENCE        CREATE SEQUENCE public.tblobject_objectid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.tblobject_objectid_seq;
       public          postgres    false            �           0    0    SEQUENCE tblobject_objectid_seq    ACL     C   GRANT ALL ON SEQUENCE public.tblobject_objectid_seq TO "Webgroup";
          public          postgres    false    264            	           1259    65315    tblobjectregion    TABLE     �   CREATE TABLE public.tblobjectregion (
    objectregionid integer NOT NULL,
    objectid uuid NOT NULL,
    regionid integer NOT NULL
);
 #   DROP TABLE public.tblobjectregion;
       public         heap    postgres    false            �           0    0    TABLE tblobjectregion    ACL     9   GRANT ALL ON TABLE public.tblobjectregion TO "Webgroup";
          public          postgres    false    265            
           1259    65318 "   tblobjectregion_objectregionid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblobjectregion_objectregionid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 9   DROP SEQUENCE public.tblobjectregion_objectregionid_seq;
       public          postgres    false    265            �           0    0 "   tblobjectregion_objectregionid_seq    SEQUENCE OWNED BY     i   ALTER SEQUENCE public.tblobjectregion_objectregionid_seq OWNED BY public.tblobjectregion.objectregionid;
          public          postgres    false    266                        0    0 +   SEQUENCE tblobjectregion_objectregionid_seq    ACL     O   GRANT ALL ON SEQUENCE public.tblobjectregion_objectregionid_seq TO "Webgroup";
          public          postgres    false    266                       1259    65320    tblodkdefinition    TABLE     m  CREATE TABLE public.tblodkdefinition (
    odkdefinitionid uuid DEFAULT public.uuid_generate_v1mc() NOT NULL,
    createdtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    name character varying NOT NULL,
    definition character varying NOT NULL,
    ownerid uuid NOT NULL,
    ispublic boolean DEFAULT false,
    version integer DEFAULT 1 NOT NULL
);
 $   DROP TABLE public.tblodkdefinition;
       public         heap    tellervo    false    1211                       1259    65330    tblodkinstance    TABLE     c  CREATE TABLE public.tblodkinstance (
    odkinstanceid uuid DEFAULT public.uuid_generate_v1mc() NOT NULL,
    createdtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    ownerid uuid NOT NULL,
    name character varying NOT NULL,
    instance character varying NOT NULL,
    files character varying[],
    deviceid character varying NOT NULL
);
 "   DROP TABLE public.tblodkinstance;
       public         heap    tellervo    false    1211                       1259    65348    tblprojectprojecttype    TABLE     �   CREATE TABLE public.tblprojectprojecttype (
    projectprojecttypeid integer NOT NULL,
    projectid uuid NOT NULL,
    projecttypeid integer NOT NULL
);
 )   DROP TABLE public.tblprojectprojecttype;
       public         heap    tellervo    false                       1259    65351 .   tblprojectprojecttype_projectprojecttypeid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblprojectprojecttype_projectprojecttypeid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 E   DROP SEQUENCE public.tblprojectprojecttype_projectprojecttypeid_seq;
       public          tellervo    false    270                       0    0 .   tblprojectprojecttype_projectprojecttypeid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tblprojectprojecttype_projectprojecttypeid_seq OWNED BY public.tblprojectprojecttype.projectprojecttypeid;
          public          tellervo    false    271                       1259    65353    tblradius_radiusid_seq    SEQUENCE        CREATE SEQUENCE public.tblradius_radiusid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.tblradius_radiusid_seq;
       public          postgres    false    239                       0    0    tblradius_radiusid_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.tblradius_radiusid_seq OWNED BY public.tblradius.radiusid;
          public          postgres    false    272                       0    0    SEQUENCE tblradius_radiusid_seq    ACL     C   GRANT ALL ON SEQUENCE public.tblradius_radiusid_seq TO "Webgroup";
          public          postgres    false    272                       1259    65355    tblrasterlayer    TABLE     �   CREATE TABLE public.tblrasterlayer (
    rasterlayerid integer NOT NULL,
    name character varying(255) NOT NULL,
    filename character varying(1024) NOT NULL,
    issystemlayer boolean DEFAULT true NOT NULL,
    description character varying(5000)
);
 "   DROP TABLE public.tblrasterlayer;
       public         heap    postgres    false                       0    0    TABLE tblrasterlayer    ACL     8   GRANT ALL ON TABLE public.tblrasterlayer TO "Webgroup";
          public          postgres    false    273                       1259    65362     tblrasterlayer_rasterlayerid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblrasterlayer_rasterlayerid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 7   DROP SEQUENCE public.tblrasterlayer_rasterlayerid_seq;
       public          postgres    false    273                       0    0     tblrasterlayer_rasterlayerid_seq    SEQUENCE OWNED BY     e   ALTER SEQUENCE public.tblrasterlayer_rasterlayerid_seq OWNED BY public.tblrasterlayer.rasterlayerid;
          public          postgres    false    274                       1259    65364 
   tblreading    TABLE     �   CREATE TABLE public.tblreading (
    readingid integer NOT NULL,
    measurementid integer NOT NULL,
    relyear integer NOT NULL,
    reading integer,
    ewwidth integer,
    lwwidth integer
);
    DROP TABLE public.tblreading;
       public         heap    postgres    false                       0    0    TABLE tblreading    ACL     4   GRANT ALL ON TABLE public.tblreading TO "Webgroup";
          public          postgres    false    275                       1259    65367    tblreading_readingid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblreading_readingid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.tblreading_readingid_seq;
       public          postgres    false    275                       0    0    tblreading_readingid_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.tblreading_readingid_seq OWNED BY public.tblreading.readingid;
          public          postgres    false    276                       0    0 !   SEQUENCE tblreading_readingid_seq    ACL     E   GRANT ALL ON SEQUENCE public.tblreading_readingid_seq TO "Webgroup";
          public          postgres    false    276                       1259    65369    tblreadingreadingnote    TABLE     .  CREATE TABLE public.tblreadingreadingnote (
    readingid integer NOT NULL,
    readingnoteid integer NOT NULL,
    createdtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifiedtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    readingreadingnoteid integer NOT NULL
);
 )   DROP TABLE public.tblreadingreadingnote;
       public         heap    postgres    false            	           0    0    TABLE tblreadingreadingnote    ACL     ?   GRANT ALL ON TABLE public.tblreadingreadingnote TO "Webgroup";
          public          postgres    false    277                       1259    65374 .   tblreadingreadingnote_readingreadingnoteid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblreadingreadingnote_readingreadingnoteid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 E   DROP SEQUENCE public.tblreadingreadingnote_readingreadingnoteid_seq;
       public          postgres    false    277            
           0    0 .   tblreadingreadingnote_readingreadingnoteid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tblreadingreadingnote_readingreadingnoteid_seq OWNED BY public.tblreadingreadingnote.readingreadingnoteid;
          public          postgres    false    278                       0    0 7   SEQUENCE tblreadingreadingnote_readingreadingnoteid_seq    ACL     [   GRANT ALL ON SEQUENCE public.tblreadingreadingnote_readingreadingnoteid_seq TO "Webgroup";
          public          postgres    false    278                       1259    65376 	   tblredate    TABLE     �   CREATE TABLE public.tblredate (
    redateid integer NOT NULL,
    vmeasurementid uuid NOT NULL,
    startyear integer NOT NULL,
    redatingtypeid integer,
    justification text
);
    DROP TABLE public.tblredate;
       public         heap    postgres    false                       0    0    TABLE tblredate    ACL     3   GRANT ALL ON TABLE public.tblredate TO "Webgroup";
          public          postgres    false    279                       1259    65382    tblredate_redateid_seq    SEQUENCE        CREATE SEQUENCE public.tblredate_redateid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.tblredate_redateid_seq;
       public          postgres    false    279                       0    0    tblredate_redateid_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.tblredate_redateid_seq OWNED BY public.tblredate.redateid;
          public          postgres    false    280                       0    0    SEQUENCE tblredate_redateid_seq    ACL     C   GRANT ALL ON SEQUENCE public.tblredate_redateid_seq TO "Webgroup";
          public          postgres    false    280                       1259    65384 	   tblregion    TABLE     |  CREATE TABLE public.tblregion (
    regionid integer NOT NULL,
    fips_cntry character varying(2),
    gmi_cntry character varying(3),
    regionname character varying(40),
    sovereign character varying(40),
    pop_cntry integer,
    sqkm_cntry double precision,
    sqmi_cntry double precision,
    curr_type character varying(16),
    curr_code character varying(4),
    landlocked character varying(1),
    color_map character varying(1),
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);
    DROP TABLE public.tblregion;
       public         heap    postgres    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3                       1259    65392    tblregion_regionid_seq    SEQUENCE        CREATE SEQUENCE public.tblregion_regionid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.tblregion_regionid_seq;
       public          postgres    false                       0    0    SEQUENCE tblregion_regionid_seq    ACL     C   GRANT ALL ON SEQUENCE public.tblregion_regionid_seq TO "Webgroup";
          public          postgres    false    282                       1259    65394    tblrequestlog    TABLE     /  CREATE TABLE public.tblrequestlog (
    requestlogid integer NOT NULL,
    request character varying,
    ipaddr inet,
    createdtimestamp timestamp with time zone DEFAULT now(),
    wsversion character varying,
    page character varying,
    client character varying(255),
    securityuserid uuid
);
 !   DROP TABLE public.tblrequestlog;
       public         heap    postgres    false                       0    0    TABLE tblrequestlog    ACL     7   GRANT ALL ON TABLE public.tblrequestlog TO "Webgroup";
          public          postgres    false    283                       1259    65401    tblrequestlog_requestlogid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblrequestlog_requestlogid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 5   DROP SEQUENCE public.tblrequestlog_requestlogid_seq;
       public          postgres    false    283                       0    0    tblrequestlog_requestlogid_seq    SEQUENCE OWNED BY     a   ALTER SEQUENCE public.tblrequestlog_requestlogid_seq OWNED BY public.tblrequestlog.requestlogid;
          public          postgres    false    284                       0    0 '   SEQUENCE tblrequestlog_requestlogid_seq    ACL     K   GRANT ALL ON SEQUENCE public.tblrequestlog_requestlogid_seq TO "Webgroup";
          public          postgres    false    284                       1259    65403    tblsample_sampleid_seq    SEQUENCE        CREATE SEQUENCE public.tblsample_sampleid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.tblsample_sampleid_seq;
       public          postgres    false    240                       0    0    tblsample_sampleid_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.tblsample_sampleid_seq OWNED BY public.tblsample.sampleid;
          public          postgres    false    285                       0    0    SEQUENCE tblsample_sampleid_seq    ACL     C   GRANT ALL ON SEQUENCE public.tblsample_sampleid_seq TO "Webgroup";
          public          postgres    false    285                       1259    65405    tblsecuritydefault    TABLE     �   CREATE TABLE public.tblsecuritydefault (
    securitydefaultid integer NOT NULL,
    securitygroupid integer NOT NULL,
    securitypermissionid integer NOT NULL
);
 &   DROP TABLE public.tblsecuritydefault;
       public         heap    postgres    false                       0    0    TABLE tblsecuritydefault    ACL     <   GRANT ALL ON TABLE public.tblsecuritydefault TO "Webgroup";
          public          postgres    false    286                       1259    65408 (   tblsecuritydefault_securitydefaultid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblsecuritydefault_securitydefaultid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ?   DROP SEQUENCE public.tblsecuritydefault_securitydefaultid_seq;
       public          postgres    false    286                       0    0 (   tblsecuritydefault_securitydefaultid_seq    SEQUENCE OWNED BY     u   ALTER SEQUENCE public.tblsecuritydefault_securitydefaultid_seq OWNED BY public.tblsecuritydefault.securitydefaultid;
          public          postgres    false    287                       0    0 1   SEQUENCE tblsecuritydefault_securitydefaultid_seq    ACL     U   GRANT ALL ON SEQUENCE public.tblsecuritydefault_securitydefaultid_seq TO "Webgroup";
          public          postgres    false    287                        1259    65410    tblsecurityelement    TABLE     �   CREATE TABLE public.tblsecurityelement (
    elementid uuid NOT NULL,
    securitygroupid integer NOT NULL,
    securitypermissionid integer NOT NULL,
    securityelementid integer NOT NULL
);
 &   DROP TABLE public.tblsecurityelement;
       public         heap    postgres    false                       0    0    TABLE tblsecurityelement    ACL     <   GRANT ALL ON TABLE public.tblsecurityelement TO "Webgroup";
          public          postgres    false    288            !           1259    65413 $   tblsecuritygroup_securitygroupid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblsecuritygroup_securitygroupid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ;   DROP SEQUENCE public.tblsecuritygroup_securitygroupid_seq;
       public          postgres    false    237                       0    0 $   tblsecuritygroup_securitygroupid_seq    SEQUENCE OWNED BY     m   ALTER SEQUENCE public.tblsecuritygroup_securitygroupid_seq OWNED BY public.tblsecuritygroup.securitygroupid;
          public          postgres    false    289                       0    0 -   SEQUENCE tblsecuritygroup_securitygroupid_seq    ACL     Q   GRANT ALL ON SEQUENCE public.tblsecuritygroup_securitygroupid_seq TO "Webgroup";
          public          postgres    false    289            "           1259    65415    tblsecuritygroupmembership    TABLE     �   CREATE TABLE public.tblsecuritygroupmembership (
    securitygroupmembershipid integer NOT NULL,
    parentsecuritygroupid integer NOT NULL,
    childsecuritygroupid integer NOT NULL
);
 .   DROP TABLE public.tblsecuritygroupmembership;
       public         heap    postgres    false                       0    0     TABLE tblsecuritygroupmembership    ACL     D   GRANT ALL ON TABLE public.tblsecuritygroupmembership TO "Webgroup";
          public          postgres    false    290            #           1259    65418 8   tblsecuritygroupmembership_securitygroupmembershipid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblsecuritygroupmembership_securitygroupmembershipid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 O   DROP SEQUENCE public.tblsecuritygroupmembership_securitygroupmembershipid_seq;
       public          postgres    false    290                       0    0 8   tblsecuritygroupmembership_securitygroupmembershipid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tblsecuritygroupmembership_securitygroupmembershipid_seq OWNED BY public.tblsecuritygroupmembership.securitygroupmembershipid;
          public          postgres    false    291                       0    0 A   SEQUENCE tblsecuritygroupmembership_securitygroupmembershipid_seq    ACL     e   GRANT ALL ON SEQUENCE public.tblsecuritygroupmembership_securitygroupmembershipid_seq TO "Webgroup";
          public          postgres    false    291            $           1259    65420    tblsecurityobject    TABLE     �   CREATE TABLE public.tblsecurityobject (
    securityobjectid integer NOT NULL,
    objectid uuid NOT NULL,
    securitygroupid integer NOT NULL,
    securitypermissionid integer NOT NULL
);
 %   DROP TABLE public.tblsecurityobject;
       public         heap    postgres    false                       0    0    TABLE tblsecurityobject    ACL     ;   GRANT ALL ON TABLE public.tblsecurityobject TO "Webgroup";
          public          postgres    false    292            %           1259    65423 &   tblsecurityobject_securityobjectid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblsecurityobject_securityobjectid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 =   DROP SEQUENCE public.tblsecurityobject_securityobjectid_seq;
       public          postgres    false    292                       0    0 &   tblsecurityobject_securityobjectid_seq    SEQUENCE OWNED BY     q   ALTER SEQUENCE public.tblsecurityobject_securityobjectid_seq OWNED BY public.tblsecurityobject.securityobjectid;
          public          postgres    false    293                        0    0 /   SEQUENCE tblsecurityobject_securityobjectid_seq    ACL     S   GRANT ALL ON SEQUENCE public.tblsecurityobject_securityobjectid_seq TO "Webgroup";
          public          postgres    false    293            &           1259    65425 %   tblsecuritytree_securityelementid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblsecuritytree_securityelementid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 <   DROP SEQUENCE public.tblsecuritytree_securityelementid_seq;
       public          postgres    false    288            !           0    0 %   tblsecuritytree_securityelementid_seq    SEQUENCE OWNED BY     r   ALTER SEQUENCE public.tblsecuritytree_securityelementid_seq OWNED BY public.tblsecurityelement.securityelementid;
          public          postgres    false    294            "           0    0 .   SEQUENCE tblsecuritytree_securityelementid_seq    ACL     R   GRANT ALL ON SEQUENCE public.tblsecuritytree_securityelementid_seq TO "Webgroup";
          public          postgres    false    294            '           1259    65427    tblsecurityuser    TABLE     h  CREATE TABLE public.tblsecurityuser (
    username character varying(31) NOT NULL,
    password character varying(132) NOT NULL,
    firstname character varying(31) NOT NULL,
    lastname character varying(31) NOT NULL,
    isactive boolean DEFAULT true,
    securityuserid uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    odkpassword character varying
);
 #   DROP TABLE public.tblsecurityuser;
       public         heap    postgres    false    1718            #           0    0    TABLE tblsecurityuser    ACL     9   GRANT ALL ON TABLE public.tblsecurityuser TO "Webgroup";
          public          postgres    false    295            (           1259    65435    tblsecurityusermembership    TABLE     �   CREATE TABLE public.tblsecurityusermembership (
    tblsecurityusermembershipid integer NOT NULL,
    securitygroupid integer NOT NULL,
    securityuserid uuid
);
 -   DROP TABLE public.tblsecurityusermembership;
       public         heap    postgres    false            $           0    0    TABLE tblsecurityusermembership    ACL     C   GRANT ALL ON TABLE public.tblsecurityusermembership TO "Webgroup";
          public          postgres    false    296            )           1259    65438 9   tblsecurityusermembership_tblsecurityusermembershipid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblsecurityusermembership_tblsecurityusermembershipid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 P   DROP SEQUENCE public.tblsecurityusermembership_tblsecurityusermembershipid_seq;
       public          postgres    false    296            %           0    0 9   tblsecurityusermembership_tblsecurityusermembershipid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tblsecurityusermembership_tblsecurityusermembershipid_seq OWNED BY public.tblsecurityusermembership.tblsecurityusermembershipid;
          public          postgres    false    297            &           0    0 B   SEQUENCE tblsecurityusermembership_tblsecurityusermembershipid_seq    ACL     f   GRANT ALL ON SEQUENCE public.tblsecurityusermembership_tblsecurityusermembershipid_seq TO "Webgroup";
          public          postgres    false    297            *           1259    65440    tblsecurityvmeasurement    TABLE     �   CREATE TABLE public.tblsecurityvmeasurement (
    securityvmeasurementid integer NOT NULL,
    vmeasurementid uuid NOT NULL,
    securitygroupid integer NOT NULL,
    securitypermissionid integer NOT NULL
);
 +   DROP TABLE public.tblsecurityvmeasurement;
       public         heap    postgres    false            '           0    0    TABLE tblsecurityvmeasurement    ACL     A   GRANT ALL ON TABLE public.tblsecurityvmeasurement TO "Webgroup";
          public          postgres    false    298            +           1259    65443 2   tblsecurityvmeasurement_securityvmeasurementid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblsecurityvmeasurement_securityvmeasurementid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 I   DROP SEQUENCE public.tblsecurityvmeasurement_securityvmeasurementid_seq;
       public          postgres    false    298            (           0    0 2   tblsecurityvmeasurement_securityvmeasurementid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tblsecurityvmeasurement_securityvmeasurementid_seq OWNED BY public.tblsecurityvmeasurement.securityvmeasurementid;
          public          postgres    false    299            )           0    0 ;   SEQUENCE tblsecurityvmeasurement_securityvmeasurementid_seq    ACL     _   GRANT ALL ON SEQUENCE public.tblsecurityvmeasurement_securityvmeasurementid_seq TO "Webgroup";
          public          postgres    false    299            ,           1259    65445    tblsupportedclient    TABLE     �   CREATE TABLE public.tblsupportedclient (
    supportclientid integer NOT NULL,
    client character varying NOT NULL,
    minversion character varying NOT NULL
);
 &   DROP TABLE public.tblsupportedclient;
       public         heap    postgres    false            -           1259    65451 '   tblsupportedclients_supportclientid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblsupportedclients_supportclientid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 >   DROP SEQUENCE public.tblsupportedclients_supportclientid_seq;
       public          postgres    false    300            *           0    0 '   tblsupportedclients_supportclientid_seq    SEQUENCE OWNED BY     r   ALTER SEQUENCE public.tblsupportedclients_supportclientid_seq OWNED BY public.tblsupportedclient.supportclientid;
          public          postgres    false    301            .           1259    65453    tbltag    TABLE     �   CREATE TABLE public.tbltag (
    tagid uuid DEFAULT public.uuid_generate_v1mc() NOT NULL,
    tag character varying NOT NULL,
    ownerid uuid,
    global boolean DEFAULT false NOT NULL
);
    DROP TABLE public.tbltag;
       public         heap    tellervo    false    1211            /           1259    65461    tbltruncate    TABLE     �   CREATE TABLE public.tbltruncate (
    truncateid integer NOT NULL,
    vmeasurementid uuid NOT NULL,
    startrelyear integer NOT NULL,
    endrelyear integer NOT NULL,
    justification text
);
    DROP TABLE public.tbltruncate;
       public         heap    postgres    false            +           0    0    TABLE tbltruncate    ACL     5   GRANT ALL ON TABLE public.tbltruncate TO "Webgroup";
          public          postgres    false    303            0           1259    65467    tbltruncate_truncateid_seq    SEQUENCE     �   CREATE SEQUENCE public.tbltruncate_truncateid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 1   DROP SEQUENCE public.tbltruncate_truncateid_seq;
       public          postgres    false    303            ,           0    0    tbltruncate_truncateid_seq    SEQUENCE OWNED BY     Y   ALTER SEQUENCE public.tbltruncate_truncateid_seq OWNED BY public.tbltruncate.truncateid;
          public          postgres    false    304            -           0    0 #   SEQUENCE tbltruncate_truncateid_seq    ACL     G   GRANT ALL ON SEQUENCE public.tbltruncate_truncateid_seq TO "Webgroup";
          public          postgres    false    304            1           1259    65469    tblupgradelog    TABLE     �   CREATE TABLE public.tblupgradelog (
    upgradelogid integer NOT NULL,
    filename character varying NOT NULL,
    "timestamp" timestamp with time zone DEFAULT now() NOT NULL
);
 !   DROP TABLE public.tblupgradelog;
       public         heap    postgres    false            .           0    0    TABLE tblupgradelog    ACL     7   GRANT ALL ON TABLE public.tblupgradelog TO "Webgroup";
          public          postgres    false    305            2           1259    65476    tblupgradelog_upgradelogid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblupgradelog_upgradelogid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 5   DROP SEQUENCE public.tblupgradelog_upgradelogid_seq;
       public          postgres    false    305            /           0    0    tblupgradelog_upgradelogid_seq    SEQUENCE OWNED BY     a   ALTER SEQUENCE public.tblupgradelog_upgradelogid_seq OWNED BY public.tblupgradelog.upgradelogid;
          public          postgres    false    306            4           1259    65485    tblvmeasurement    TABLE     �  CREATE TABLE public.tblvmeasurement (
    vmeasurementid uuid DEFAULT public.uuid_generate_v1mc() NOT NULL,
    measurementid integer,
    vmeasurementopid integer,
    vmeasurementopparameter integer,
    code character varying(255) NOT NULL,
    comments character varying(1000),
    ispublished boolean DEFAULT false NOT NULL,
    createdtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifiedtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    isgenerating boolean DEFAULT true NOT NULL,
    objective character varying,
    version character varying,
    birthdate date DEFAULT now(),
    owneruserid uuid,
    domainid integer DEFAULT 0
);
 #   DROP TABLE public.tblvmeasurement;
       public         heap    postgres    false    1211            0           0    0     COLUMN tblvmeasurement.birthdate    COMMENT     �   COMMENT ON COLUMN public.tblvmeasurement.birthdate IS 'maps to TRiDaS measurementSeries.measuringDate and derivedSeries.derivationDate';
          public          postgres    false    308            1           0    0    TABLE tblvmeasurement    ACL     9   GRANT ALL ON TABLE public.tblvmeasurement TO "Webgroup";
          public          postgres    false    308            5           1259    65498 "   tblvmeasurement_vmeasurementid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblvmeasurement_vmeasurementid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 9   DROP SEQUENCE public.tblvmeasurement_vmeasurementid_seq;
       public          postgres    false    308            2           0    0 "   tblvmeasurement_vmeasurementid_seq    SEQUENCE OWNED BY     i   ALTER SEQUENCE public.tblvmeasurement_vmeasurementid_seq OWNED BY public.tblvmeasurement.vmeasurementid;
          public          postgres    false    309            3           0    0 +   SEQUENCE tblvmeasurement_vmeasurementid_seq    ACL     O   GRANT ALL ON SEQUENCE public.tblvmeasurement_vmeasurementid_seq TO "Webgroup";
          public          postgres    false    309            6           1259    65500 .   tblvmeasurementderivedcache_derivedcacheid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblvmeasurementderivedcache_derivedcacheid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 E   DROP SEQUENCE public.tblvmeasurementderivedcache_derivedcacheid_seq;
       public          postgres    false    235            4           0    0 .   tblvmeasurementderivedcache_derivedcacheid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tblvmeasurementderivedcache_derivedcacheid_seq OWNED BY public.tblvmeasurementderivedcache.derivedcacheid;
          public          postgres    false    310            5           0    0 7   SEQUENCE tblvmeasurementderivedcache_derivedcacheid_seq    ACL     [   GRANT ALL ON SEQUENCE public.tblvmeasurementderivedcache_derivedcacheid_seq TO "Webgroup";
          public          postgres    false    310            7           1259    65502    tblvmeasurementgroup    TABLE     �   CREATE TABLE public.tblvmeasurementgroup (
    vmeasurementid uuid NOT NULL,
    membervmeasurementid uuid NOT NULL,
    vmeasurementgroupid integer NOT NULL
);
 (   DROP TABLE public.tblvmeasurementgroup;
       public         heap    postgres    false            6           0    0    TABLE tblvmeasurementgroup    ACL     >   GRANT ALL ON TABLE public.tblvmeasurementgroup TO "Webgroup";
          public          postgres    false    311            8           1259    65505 ,   tblvmeasurementgroup_vmeasurementgroupid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblvmeasurementgroup_vmeasurementgroupid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 C   DROP SEQUENCE public.tblvmeasurementgroup_vmeasurementgroupid_seq;
       public          postgres    false    311            7           0    0 ,   tblvmeasurementgroup_vmeasurementgroupid_seq    SEQUENCE OWNED BY     }   ALTER SEQUENCE public.tblvmeasurementgroup_vmeasurementgroupid_seq OWNED BY public.tblvmeasurementgroup.vmeasurementgroupid;
          public          postgres    false    312            8           0    0 5   SEQUENCE tblvmeasurementgroup_vmeasurementgroupid_seq    ACL     Y   GRANT ALL ON SEQUENCE public.tblvmeasurementgroup_vmeasurementgroupid_seq TO "Webgroup";
          public          postgres    false    312            9           1259    65507 4   tblvmeasurementmetacache_vmeasurementmetacacheid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblvmeasurementmetacache_vmeasurementmetacacheid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 K   DROP SEQUENCE public.tblvmeasurementmetacache_vmeasurementmetacacheid_seq;
       public          postgres    false    233            9           0    0 4   tblvmeasurementmetacache_vmeasurementmetacacheid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tblvmeasurementmetacache_vmeasurementmetacacheid_seq OWNED BY public.tblvmeasurementmetacache.vmeasurementmetacacheid;
          public          postgres    false    313            :           0    0 =   SEQUENCE tblvmeasurementmetacache_vmeasurementmetacacheid_seq    ACL     a   GRANT ALL ON SEQUENCE public.tblvmeasurementmetacache_vmeasurementmetacacheid_seq TO "Webgroup";
          public          postgres    false    313            :           1259    65509     tblvmeasurementreadingnoteresult    TABLE     �   CREATE TABLE public.tblvmeasurementreadingnoteresult (
    vmeasurementresultid uuid NOT NULL,
    relyear integer NOT NULL,
    readingnoteid integer NOT NULL,
    inheritedcount integer DEFAULT 0 NOT NULL
);
 4   DROP TABLE public.tblvmeasurementreadingnoteresult;
       public         heap    postgres    false            ;           0    0 &   TABLE tblvmeasurementreadingnoteresult    ACL     J   GRANT ALL ON TABLE public.tblvmeasurementreadingnoteresult TO "Webgroup";
          public          postgres    false    314            ;           1259    65513 <   tblvmeasurementreadingresult_vmeasurementreadingresultid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblvmeasurementreadingresult_vmeasurementreadingresultid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 S   DROP SEQUENCE public.tblvmeasurementreadingresult_vmeasurementreadingresultid_seq;
       public          postgres    false    241            <           0    0 <   tblvmeasurementreadingresult_vmeasurementreadingresultid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tblvmeasurementreadingresult_vmeasurementreadingresultid_seq OWNED BY public.tblvmeasurementreadingresult.vmeasurementreadingresultid;
          public          postgres    false    315            =           0    0 E   SEQUENCE tblvmeasurementreadingresult_vmeasurementreadingresultid_seq    ACL     i   GRANT ALL ON SEQUENCE public.tblvmeasurementreadingresult_vmeasurementreadingresultid_seq TO "Webgroup";
          public          postgres    false    315            <           1259    65515 :   tblvmeasurementrelyearreadingnote_relyearreadingnoteid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblvmeasurementrelyearreadingnote_relyearreadingnoteid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 Q   DROP SEQUENCE public.tblvmeasurementrelyearreadingnote_relyearreadingnoteid_seq;
       public          postgres    false            =           1259    65517 !   tblvmeasurementrelyearreadingnote    TABLE     �  CREATE TABLE public.tblvmeasurementrelyearreadingnote (
    vmeasurementid uuid NOT NULL,
    relyear integer NOT NULL,
    readingnoteid integer NOT NULL,
    disabledoverride boolean DEFAULT false NOT NULL,
    createdtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifiedtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    relyearreadingnoteid integer DEFAULT nextval('public.tblvmeasurementrelyearreadingnote_relyearreadingnoteid_seq'::regclass) NOT NULL
);
 5   DROP TABLE public.tblvmeasurementrelyearreadingnote;
       public         heap    postgres    false    316            >           0    0 '   TABLE tblvmeasurementrelyearreadingnote    ACL     K   GRANT ALL ON TABLE public.tblvmeasurementrelyearreadingnote TO "Webgroup";
          public          postgres    false    317            ?           1259    65534    tblvmeasurementtotag    TABLE     �   CREATE TABLE public.tblvmeasurementtotag (
    vmeasurementtotagid integer NOT NULL,
    tagid uuid NOT NULL,
    vmeasurementid uuid NOT NULL
);
 (   DROP TABLE public.tblvmeasurementtotag;
       public         heap    tellervo    false            @           1259    65537 ,   tblvmeasurementtotag_vmeasurementtotagid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblvmeasurementtotag_vmeasurementtotagid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 C   DROP SEQUENCE public.tblvmeasurementtotag_vmeasurementtotagid_seq;
       public          tellervo    false    319            ?           0    0 ,   tblvmeasurementtotag_vmeasurementtotagid_seq    SEQUENCE OWNED BY     }   ALTER SEQUENCE public.tblvmeasurementtotag_vmeasurementtotagid_seq OWNED BY public.tblvmeasurementtotag.vmeasurementtotagid;
          public          tellervo    false    320            A           1259    65539    tlkpcomplexpresenceabsence    TABLE     �   CREATE TABLE public.tlkpcomplexpresenceabsence (
    complexpresenceabsenceid integer NOT NULL,
    complexpresenceabsence character varying NOT NULL
);
 .   DROP TABLE public.tlkpcomplexpresenceabsence;
       public         heap    postgres    false            @           0    0     TABLE tlkpcomplexpresenceabsence    ACL     D   GRANT ALL ON TABLE public.tlkpcomplexpresenceabsence TO "Webgroup";
          public          postgres    false    321            B           1259    65545 7   tlkpcomplexpresenceabsence_complexpresenceabsenceid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpcomplexpresenceabsence_complexpresenceabsenceid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 N   DROP SEQUENCE public.tlkpcomplexpresenceabsence_complexpresenceabsenceid_seq;
       public          postgres    false    321            A           0    0 7   tlkpcomplexpresenceabsence_complexpresenceabsenceid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tlkpcomplexpresenceabsence_complexpresenceabsenceid_seq OWNED BY public.tlkpcomplexpresenceabsence.complexpresenceabsenceid;
          public          postgres    false    322            C           1259    65547    tlkpcoveragetemporal    TABLE     �   CREATE TABLE public.tlkpcoveragetemporal (
    coveragetemporalid integer NOT NULL,
    coveragetemporal character varying NOT NULL
);
 (   DROP TABLE public.tlkpcoveragetemporal;
       public         heap    postgres    false            B           0    0    TABLE tlkpcoveragetemporal    ACL     >   GRANT ALL ON TABLE public.tlkpcoveragetemporal TO "Webgroup";
          public          postgres    false    323            D           1259    65553 +   tlkpcoveragetemporal_coveragetemporalid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpcoveragetemporal_coveragetemporalid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 B   DROP SEQUENCE public.tlkpcoveragetemporal_coveragetemporalid_seq;
       public          postgres    false    323            C           0    0 +   tlkpcoveragetemporal_coveragetemporalid_seq    SEQUENCE OWNED BY     {   ALTER SEQUENCE public.tlkpcoveragetemporal_coveragetemporalid_seq OWNED BY public.tlkpcoveragetemporal.coveragetemporalid;
          public          postgres    false    324            E           1259    65555    tlkpcoveragetemporalfoundation    TABLE     �   CREATE TABLE public.tlkpcoveragetemporalfoundation (
    coveragetemporalfoundationid integer NOT NULL,
    coveragetemporalfoundation character varying NOT NULL
);
 2   DROP TABLE public.tlkpcoveragetemporalfoundation;
       public         heap    postgres    false            D           0    0 $   TABLE tlkpcoveragetemporalfoundation    ACL     H   GRANT ALL ON TABLE public.tlkpcoveragetemporalfoundation TO "Webgroup";
          public          postgres    false    325            F           1259    65561 ?   tlkpcoveragetemporalfoundation_coveragetemporalfoundationid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpcoveragetemporalfoundation_coveragetemporalfoundationid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 V   DROP SEQUENCE public.tlkpcoveragetemporalfoundation_coveragetemporalfoundationid_seq;
       public          postgres    false    325            E           0    0 ?   tlkpcoveragetemporalfoundation_coveragetemporalfoundationid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tlkpcoveragetemporalfoundation_coveragetemporalfoundationid_seq OWNED BY public.tlkpcoveragetemporalfoundation.coveragetemporalfoundationid;
          public          postgres    false    326            G           1259    65563    tlkpcurationstatus    TABLE     x   CREATE TABLE public.tlkpcurationstatus (
    curationstatusid integer NOT NULL,
    curationstatus character varying
);
 &   DROP TABLE public.tlkpcurationstatus;
       public         heap    tellervo    false            H           1259    65569 '   tlkpcurationstatus_curationstatusid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpcurationstatus_curationstatusid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 >   DROP SEQUENCE public.tlkpcurationstatus_curationstatusid_seq;
       public          tellervo    false    327            F           0    0 '   tlkpcurationstatus_curationstatusid_seq    SEQUENCE OWNED BY     s   ALTER SEQUENCE public.tlkpcurationstatus_curationstatusid_seq OWNED BY public.tlkpcurationstatus.curationstatusid;
          public          tellervo    false    328            I           1259    65571    tlkpdatatype    TABLE     N   CREATE TABLE public.tlkpdatatype (
    datatype character varying NOT NULL
);
     DROP TABLE public.tlkpdatatype;
       public         heap    tellervo    false            J           1259    65577    tlkpdatecertainty    TABLE     ~   CREATE TABLE public.tlkpdatecertainty (
    datecertaintyid integer NOT NULL,
    datecertainty character varying NOT NULL
);
 %   DROP TABLE public.tlkpdatecertainty;
       public         heap    postgres    false            K           1259    65583 %   tlkpdatecertainty_datecertaintyid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpdatecertainty_datecertaintyid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 <   DROP SEQUENCE public.tlkpdatecertainty_datecertaintyid_seq;
       public          postgres    false    330            G           0    0 %   tlkpdatecertainty_datecertaintyid_seq    SEQUENCE OWNED BY     o   ALTER SEQUENCE public.tlkpdatecertainty_datecertaintyid_seq OWNED BY public.tlkpdatecertainty.datecertaintyid;
          public          postgres    false    331            L           1259    65585    tlkpdatingtype    TABLE     �   CREATE TABLE public.tlkpdatingtype (
    datingtypeid integer NOT NULL,
    datingtype character varying NOT NULL,
    datingclass public.datingtypeclass NOT NULL
);
 "   DROP TABLE public.tlkpdatingtype;
       public         heap    postgres    false    2221            H           0    0    TABLE tlkpdatingtype    ACL     8   GRANT ALL ON TABLE public.tlkpdatingtype TO "Webgroup";
          public          postgres    false    332            M           1259    65591    tlkpdatingtype_datingtypeid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpdatingtype_datingtypeid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 6   DROP SEQUENCE public.tlkpdatingtype_datingtypeid_seq;
       public          postgres    false    332            I           0    0    tlkpdatingtype_datingtypeid_seq    SEQUENCE OWNED BY     c   ALTER SEQUENCE public.tlkpdatingtype_datingtypeid_seq OWNED BY public.tlkpdatingtype.datingtypeid;
          public          postgres    false    333            J           0    0 (   SEQUENCE tlkpdatingtype_datingtypeid_seq    ACL     L   GRANT ALL ON SEQUENCE public.tlkpdatingtype_datingtypeid_seq TO "Webgroup";
          public          postgres    false    333            N           1259    65593 
   tlkpdomain    TABLE     �   CREATE TABLE public.tlkpdomain (
    domainid integer NOT NULL,
    domain character varying NOT NULL,
    prefix character varying
);
    DROP TABLE public.tlkpdomain;
       public         heap    tellervo    false            O           1259    65599    tlkpdomain_domainid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpdomain_domainid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 .   DROP SEQUENCE public.tlkpdomain_domainid_seq;
       public          tellervo    false    334            K           0    0    tlkpdomain_domainid_seq    SEQUENCE OWNED BY     S   ALTER SEQUENCE public.tlkpdomain_domainid_seq OWNED BY public.tlkpdomain.domainid;
          public          tellervo    false    335            P           1259    65601    tlkpelementauthenticity    TABLE     �   CREATE TABLE public.tlkpelementauthenticity (
    elementauthenticityid integer NOT NULL,
    elementauthenticity character varying NOT NULL
);
 +   DROP TABLE public.tlkpelementauthenticity;
       public         heap    postgres    false            L           0    0    TABLE tlkpelementauthenticity    ACL     A   GRANT ALL ON TABLE public.tlkpelementauthenticity TO "Webgroup";
          public          postgres    false    336            Q           1259    65607 1   tlkpelementauthenticity_elementauthenticityid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpelementauthenticity_elementauthenticityid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 H   DROP SEQUENCE public.tlkpelementauthenticity_elementauthenticityid_seq;
       public          postgres    false    336            M           0    0 1   tlkpelementauthenticity_elementauthenticityid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tlkpelementauthenticity_elementauthenticityid_seq OWNED BY public.tlkpelementauthenticity.elementauthenticityid;
          public          postgres    false    337            R           1259    65609    tlkpelementshape    TABLE     {   CREATE TABLE public.tlkpelementshape (
    elementshapeid integer NOT NULL,
    elementshape character varying NOT NULL
);
 $   DROP TABLE public.tlkpelementshape;
       public         heap    postgres    false            N           0    0    TABLE tlkpelementshape    ACL     :   GRANT ALL ON TABLE public.tlkpelementshape TO "Webgroup";
          public          postgres    false    338            S           1259    65615 #   tlkpelementshape_elementshapeid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpelementshape_elementshapeid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 :   DROP SEQUENCE public.tlkpelementshape_elementshapeid_seq;
       public          postgres    false    338            O           0    0 #   tlkpelementshape_elementshapeid_seq    SEQUENCE OWNED BY     k   ALTER SEQUENCE public.tlkpelementshape_elementshapeid_seq OWNED BY public.tlkpelementshape.elementshapeid;
          public          postgres    false    339            T           1259    65617    tlkpelementtype    TABLE     �   CREATE TABLE public.tlkpelementtype (
    elementtypeid integer NOT NULL,
    elementtype character varying NOT NULL,
    vocabularyid integer
);
 #   DROP TABLE public.tlkpelementtype;
       public         heap    postgres    false            P           0    0    TABLE tlkpelementtype    ACL     9   GRANT ALL ON TABLE public.tlkpelementtype TO "Webgroup";
          public          postgres    false    340            U           1259    65623 !   tlkpelementtype_elementtypeid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpelementtype_elementtypeid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 8   DROP SEQUENCE public.tlkpelementtype_elementtypeid_seq;
       public          postgres    false    340            Q           0    0 !   tlkpelementtype_elementtypeid_seq    SEQUENCE OWNED BY     g   ALTER SEQUENCE public.tlkpelementtype_elementtypeid_seq OWNED BY public.tlkpelementtype.elementtypeid;
          public          postgres    false    341            V           1259    65625    tlkpindextype    TABLE     r   CREATE TABLE public.tlkpindextype (
    indexid integer NOT NULL,
    indexname character varying(30) NOT NULL
);
 !   DROP TABLE public.tlkpindextype;
       public         heap    postgres    false            R           0    0    TABLE tlkpindextype    ACL     7   GRANT ALL ON TABLE public.tlkpindextype TO "Webgroup";
          public          postgres    false    342            W           1259    65628    tlkplaboratory_laboratoryid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkplaboratory_laboratoryid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 6   DROP SEQUENCE public.tlkplaboratory_laboratoryid_seq;
       public          tellervo    false            X           1259    65630    tlkplocationtype    TABLE     r   CREATE TABLE public.tlkplocationtype (
    locationtypeid integer NOT NULL,
    locationtype character varying
);
 $   DROP TABLE public.tlkplocationtype;
       public         heap    postgres    false            S           0    0    TABLE tlkplocationtype    ACL     :   GRANT ALL ON TABLE public.tlkplocationtype TO "Webgroup";
          public          postgres    false    344            Y           1259    65636 #   tlkplocationtype_locationtypeid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkplocationtype_locationtypeid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 :   DROP SEQUENCE public.tlkplocationtype_locationtypeid_seq;
       public          postgres    false    344            T           0    0 #   tlkplocationtype_locationtypeid_seq    SEQUENCE OWNED BY     k   ALTER SEQUENCE public.tlkplocationtype_locationtypeid_seq OWNED BY public.tlkplocationtype.locationtypeid;
          public          postgres    false    345            Z           1259    65638    tlkpmeasurementvariable    TABLE     �   CREATE TABLE public.tlkpmeasurementvariable (
    measurementvariableid integer NOT NULL,
    measurementvariable character varying NOT NULL
);
 +   DROP TABLE public.tlkpmeasurementvariable;
       public         heap    postgres    false            U           0    0    TABLE tlkpmeasurementvariable    ACL     A   GRANT ALL ON TABLE public.tlkpmeasurementvariable TO "Webgroup";
          public          postgres    false    346            [           1259    65644 1   tlkpmeasurementvariable_measurementvariableid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpmeasurementvariable_measurementvariableid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 H   DROP SEQUENCE public.tlkpmeasurementvariable_measurementvariableid_seq;
       public          postgres    false    346            V           0    0 1   tlkpmeasurementvariable_measurementvariableid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tlkpmeasurementvariable_measurementvariableid_seq OWNED BY public.tlkpmeasurementvariable.measurementvariableid;
          public          postgres    false    347            \           1259    65646    tlkpmeasuringmethod    TABLE     �   CREATE TABLE public.tlkpmeasuringmethod (
    measuringmethodid integer NOT NULL,
    measuringmethod character varying NOT NULL
);
 '   DROP TABLE public.tlkpmeasuringmethod;
       public         heap    postgres    false            W           0    0    TABLE tlkpmeasuringmethod    ACL     =   GRANT ALL ON TABLE public.tlkpmeasuringmethod TO "Webgroup";
          public          postgres    false    348            ]           1259    65652 )   tlkpmeasuringmethod_measuringmethodid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpmeasuringmethod_measuringmethodid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 @   DROP SEQUENCE public.tlkpmeasuringmethod_measuringmethodid_seq;
       public          postgres    false    348            X           0    0 )   tlkpmeasuringmethod_measuringmethodid_seq    SEQUENCE OWNED BY     w   ALTER SEQUENCE public.tlkpmeasuringmethod_measuringmethodid_seq OWNED BY public.tlkpmeasuringmethod.measuringmethodid;
          public          postgres    false    349            ^           1259    65654    tlkpobjecttype    TABLE     �   CREATE TABLE public.tlkpobjecttype (
    objecttype character varying NOT NULL,
    vocabularyid integer,
    objecttypeid integer NOT NULL
);
 "   DROP TABLE public.tlkpobjecttype;
       public         heap    postgres    false            Y           0    0    TABLE tlkpobjecttype    ACL     8   GRANT ALL ON TABLE public.tlkpobjecttype TO "Webgroup";
          public          postgres    false    350            _           1259    65660    tlkpobjecttype_objecttype_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpobjecttype_objecttype_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 4   DROP SEQUENCE public.tlkpobjecttype_objecttype_seq;
       public          postgres    false    350            Z           0    0    tlkpobjecttype_objecttype_seq    SEQUENCE OWNED BY     a   ALTER SEQUENCE public.tlkpobjecttype_objecttype_seq OWNED BY public.tlkpobjecttype.objecttypeid;
          public          postgres    false    351            `           1259    65662    tlkppresenceabsence    TABLE     �   CREATE TABLE public.tlkppresenceabsence (
    presenceabsenceid integer NOT NULL,
    presenceabsence character varying NOT NULL
);
 '   DROP TABLE public.tlkppresenceabsence;
       public         heap    postgres    false            a           1259    65668 )   tlkppresenceabsence_presenceabsenceid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkppresenceabsence_presenceabsenceid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 @   DROP SEQUENCE public.tlkppresenceabsence_presenceabsenceid_seq;
       public          postgres    false    352            [           0    0 )   tlkppresenceabsence_presenceabsenceid_seq    SEQUENCE OWNED BY     w   ALTER SEQUENCE public.tlkppresenceabsence_presenceabsenceid_seq OWNED BY public.tlkppresenceabsence.presenceabsenceid;
          public          postgres    false    353            b           1259    65670 '   tlkpprojectcategory_projectcategory_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpprojectcategory_projectcategory_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 >   DROP SEQUENCE public.tlkpprojectcategory_projectcategory_seq;
       public          tellervo    false            c           1259    65672    tlkpprojectcategory    TABLE     �   CREATE TABLE public.tlkpprojectcategory (
    projectcategory character varying NOT NULL,
    vocabularyid integer,
    projectcategoryid integer DEFAULT nextval('public.tlkpprojectcategory_projectcategory_seq'::regclass) NOT NULL
);
 '   DROP TABLE public.tlkpprojectcategory;
       public         heap    tellervo    false    354            d           1259    65679    tlkpprojecttype_projecttype_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpprojecttype_projecttype_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 6   DROP SEQUENCE public.tlkpprojecttype_projecttype_seq;
       public          tellervo    false            e           1259    65681    tlkpprojecttype    TABLE     �   CREATE TABLE public.tlkpprojecttype (
    projecttype character varying NOT NULL,
    vocabularyid integer,
    projecttypeid integer DEFAULT nextval('public.tlkpprojecttype_projecttype_seq'::regclass) NOT NULL
);
 #   DROP TABLE public.tlkpprojecttype;
       public         heap    tellervo    false    356            f           1259    65688    tlkprank_rankid_seq    SEQUENCE     |   CREATE SEQUENCE public.tlkprank_rankid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.tlkprank_rankid_seq;
       public          postgres    false            \           0    0    SEQUENCE tlkprank_rankid_seq    ACL     @   GRANT ALL ON SEQUENCE public.tlkprank_rankid_seq TO "Webgroup";
          public          postgres    false    358            g           1259    65690 !   tlkpreadingnote_readingnoteid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpreadingnote_readingnoteid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 8   DROP SEQUENCE public.tlkpreadingnote_readingnoteid_seq;
       public          postgres    false    232            ]           0    0 !   tlkpreadingnote_readingnoteid_seq    SEQUENCE OWNED BY     g   ALTER SEQUENCE public.tlkpreadingnote_readingnoteid_seq OWNED BY public.tlkpreadingnote.readingnoteid;
          public          postgres    false    359            ^           0    0 *   SEQUENCE tlkpreadingnote_readingnoteid_seq    ACL     N   GRANT ALL ON SEQUENCE public.tlkpreadingnote_readingnoteid_seq TO "Webgroup";
          public          postgres    false    359            h           1259    65692    tlkpsamplestatus    TABLE     r   CREATE TABLE public.tlkpsamplestatus (
    samplestatusid integer NOT NULL,
    samplestatus character varying
);
 $   DROP TABLE public.tlkpsamplestatus;
       public         heap    tellervo    false            i           1259    65698 #   tlkpsamplestatus_samplestatusid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpsamplestatus_samplestatusid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 :   DROP SEQUENCE public.tlkpsamplestatus_samplestatusid_seq;
       public          tellervo    false    360            _           0    0 #   tlkpsamplestatus_samplestatusid_seq    SEQUENCE OWNED BY     k   ALTER SEQUENCE public.tlkpsamplestatus_samplestatusid_seq OWNED BY public.tlkpsamplestatus.samplestatusid;
          public          tellervo    false    361            k           1259    65703    tlkpsampletype_sampletypeid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpsampletype_sampletypeid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 6   DROP SEQUENCE public.tlkpsampletype_sampletypeid_seq;
       public          postgres    false    362            `           0    0    tlkpsampletype_sampletypeid_seq    SEQUENCE OWNED BY     c   ALTER SEQUENCE public.tlkpsampletype_sampletypeid_seq OWNED BY public.tlkpsampletype.sampletypeid;
          public          postgres    false    363            a           0    0 (   SEQUENCE tlkpsampletype_sampletypeid_seq    ACL     L   GRANT ALL ON SEQUENCE public.tlkpsampletype_sampletypeid_seq TO "Webgroup";
          public          postgres    false    363            l           1259    65705    tlkpsecuritypermission    TABLE     �   CREATE TABLE public.tlkpsecuritypermission (
    securitypermissionid integer NOT NULL,
    name character varying(31) NOT NULL
);
 *   DROP TABLE public.tlkpsecuritypermission;
       public         heap    postgres    false            b           0    0    TABLE tlkpsecuritypermission    ACL     @   GRANT ALL ON TABLE public.tlkpsecuritypermission TO "Webgroup";
          public          postgres    false    364            m           1259    65708 /   tlkpsecuritypermission_securitypermissionid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpsecuritypermission_securitypermissionid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 F   DROP SEQUENCE public.tlkpsecuritypermission_securitypermissionid_seq;
       public          postgres    false    364            c           0    0 /   tlkpsecuritypermission_securitypermissionid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tlkpsecuritypermission_securitypermissionid_seq OWNED BY public.tlkpsecuritypermission.securitypermissionid;
          public          postgres    false    365            d           0    0 8   SEQUENCE tlkpsecuritypermission_securitypermissionid_seq    ACL     \   GRANT ALL ON SEQUENCE public.tlkpsecuritypermission_securitypermissionid_seq TO "Webgroup";
          public          postgres    false    365            n           1259    65710    tlkptaxon_taxonid_seq    SEQUENCE     ~   CREATE SEQUENCE public.tlkptaxon_taxonid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.tlkptaxon_taxonid_seq;
       public          postgres    false            e           0    0    SEQUENCE tlkptaxon_taxonid_seq    ACL     B   GRANT ALL ON SEQUENCE public.tlkptaxon_taxonid_seq TO "Webgroup";
          public          postgres    false    366            p           1259    65716    tlkptaxonrank    TABLE     �   CREATE TABLE public.tlkptaxonrank (
    taxonrankid integer DEFAULT nextval('public.tlkprank_rankid_seq'::regclass) NOT NULL,
    taxonrank character varying(30) NOT NULL,
    rankorder double precision
);
 !   DROP TABLE public.tlkptaxonrank;
       public         heap    postgres    false    358            f           0    0    TABLE tlkptaxonrank    ACL     7   GRANT ALL ON TABLE public.tlkptaxonrank TO "Webgroup";
          public          postgres    false    368            q           1259    65720    tlkpunit    TABLE     c   CREATE TABLE public.tlkpunit (
    unitid integer NOT NULL,
    unit character varying NOT NULL
);
    DROP TABLE public.tlkpunit;
       public         heap    postgres    false            g           0    0    TABLE tlkpunit    ACL     2   GRANT ALL ON TABLE public.tlkpunit TO "Webgroup";
          public          postgres    false    369            r           1259    65726    tlkpunits_unitsid_seq    SEQUENCE     ~   CREATE SEQUENCE public.tlkpunits_unitsid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.tlkpunits_unitsid_seq;
       public          postgres    false    369            h           0    0    tlkpunits_unitsid_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.tlkpunits_unitsid_seq OWNED BY public.tlkpunit.unitid;
          public          postgres    false    370            t           1259    65737    tlkpuserdefinedterm    TABLE     �   CREATE TABLE public.tlkpuserdefinedterm (
    userdefinedtermid uuid DEFAULT public.uuid_generate_v1mc() NOT NULL,
    term character varying NOT NULL,
    dictionarykey character varying NOT NULL
);
 '   DROP TABLE public.tlkpuserdefinedterm;
       public         heap    tellervo    false    1211            u           1259    65744    tlkpvmeasurementop    TABLE     �   CREATE TABLE public.tlkpvmeasurementop (
    vmeasurementopid integer NOT NULL,
    name character varying(31) NOT NULL,
    legacycode integer
);
 &   DROP TABLE public.tlkpvmeasurementop;
       public         heap    postgres    false            i           0    0    TABLE tlkpvmeasurementop    ACL     <   GRANT ALL ON TABLE public.tlkpvmeasurementop TO "Webgroup";
          public          postgres    false    373            v           1259    65747 '   tlkpvmeasurementop_vmeasurementopid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpvmeasurementop_vmeasurementopid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 >   DROP SEQUENCE public.tlkpvmeasurementop_vmeasurementopid_seq;
       public          postgres    false    373            j           0    0 '   tlkpvmeasurementop_vmeasurementopid_seq    SEQUENCE OWNED BY     s   ALTER SEQUENCE public.tlkpvmeasurementop_vmeasurementopid_seq OWNED BY public.tlkpvmeasurementop.vmeasurementopid;
          public          postgres    false    374            k           0    0 0   SEQUENCE tlkpvmeasurementop_vmeasurementopid_seq    ACL     T   GRANT ALL ON SEQUENCE public.tlkpvmeasurementop_vmeasurementopid_seq TO "Webgroup";
          public          postgres    false    374            w           1259    65749    tlkpvocabulary    TABLE     �   CREATE TABLE public.tlkpvocabulary (
    vocabularyid integer NOT NULL,
    name character varying NOT NULL,
    url character varying
);
 "   DROP TABLE public.tlkpvocabulary;
       public         heap    postgres    false            l           0    0    TABLE tlkpvocabulary    ACL     8   GRANT ALL ON TABLE public.tlkpvocabulary TO "Webgroup";
          public          postgres    false    375            x           1259    65755    tlkpvocabulary_vocabularyid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpvocabulary_vocabularyid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 6   DROP SEQUENCE public.tlkpvocabulary_vocabularyid_seq;
       public          postgres    false    375            m           0    0    tlkpvocabulary_vocabularyid_seq    SEQUENCE OWNED BY     c   ALTER SEQUENCE public.tlkpvocabulary_vocabularyid_seq OWNED BY public.tlkpvocabulary.vocabularyid;
          public          postgres    false    376            y           1259    65757    tlkpwmsserver    TABLE     �   CREATE TABLE public.tlkpwmsserver (
    wmsserverid integer NOT NULL,
    name character varying NOT NULL,
    url character varying NOT NULL
);
 !   DROP TABLE public.tlkpwmsserver;
       public         heap    postgres    false            n           0    0    TABLE tlkpwmsserver    ACL     7   GRANT ALL ON TABLE public.tlkpwmsserver TO "Webgroup";
          public          postgres    false    377            z           1259    65763    tlkpwmsserver_wmsserverid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpwmsserver_wmsserverid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 4   DROP SEQUENCE public.tlkpwmsserver_wmsserverid_seq;
       public          postgres    false    377            o           0    0    tlkpwmsserver_wmsserverid_seq    SEQUENCE OWNED BY     _   ALTER SEQUENCE public.tlkpwmsserver_wmsserverid_seq OWNED BY public.tlkpwmsserver.wmsserverid;
          public          postgres    false    378            {           1259    65765    vlkpvmeasurement    VIEW     G  CREATE VIEW public.vlkpvmeasurement AS
 SELECT tblvmeasurement.vmeasurementid,
    tblvmeasurement.code AS name,
    tlkpvmeasurementop.name AS op
   FROM (public.tlkpvmeasurementop
     JOIN public.tblvmeasurement ON ((tlkpvmeasurementop.vmeasurementopid = tblvmeasurement.vmeasurementopid)))
  ORDER BY tblvmeasurement.code;
 #   DROP VIEW public.vlkpvmeasurement;
       public          postgres    false    308    373    373    308    308            |           1259    65769    vw_bigbrothertracking    VIEW     E  CREATE VIEW public.vw_bigbrothertracking AS
 SELECT tblsecurityuser.firstname,
    tblsecurityuser.lastname,
    tblsecurityuser.username,
    tbliptracking.ipaddr,
    tbliptracking."timestamp"
   FROM public.tbliptracking,
    public.tblsecurityuser
  WHERE (tbliptracking.securityuserid = tblsecurityuser.securityuserid);
 (   DROP VIEW public.vw_bigbrothertracking;
       public          postgres    false    295    295    261    295    295    261    261            �           1259    66865    vw_elementtoradius    VIEW     �  CREATE VIEW public.vw_elementtoradius AS
 SELECT cpgdb.tloid(e.*) AS tlo_objectid,
    e.elementid AS e_elementid,
    e.taxonid AS e_taxonid,
    e.locationprecision AS e_locationprecision,
    e.code AS e_code,
    e.createdtimestamp AS e_createdtimestamp,
    e.lastmodifiedtimestamp AS e_lastmodifiedtimestamp,
    e.locationgeometry AS e_locationgeometry,
    e.islivetree AS e_islivetree,
    e.originaltaxonname AS e_originaltaxonname,
    e.locationtypeid AS e_locationtypeid,
    e.locationcomment AS e_locationcomment,
    e.file AS e_file,
    e.description AS e_description,
    e.processing AS e_processing,
    e.marks AS e_marks,
    e.diameter AS e_diameter,
    e.width AS e_width,
    e.height AS e_height,
    e.depth AS e_depth,
    e.unsupportedxml AS e_unsupportedxml,
    e.unitsold AS e_units,
    e.elementtypeid AS e_elementtypeid,
    e.authenticity AS e_authenticity,
    e.elementshapeid AS e_elementshapeid,
    e.altitudeint AS e_altitudeint,
    e.slopeangle AS e_slopeangle,
    e.slopeazimuth AS e_slopeazimuth,
    e.soildescription AS e_soildescription,
    e.soildepth AS e_soildepth,
    e.bedrockdescription AS e_bedrockdescription,
    e.comments AS e_comments,
    e.locationaddressline2 AS e_locationaddressline2,
    e.locationcityortown AS e_locationcityortown,
    e.locationstateprovinceregion AS e_locationstateprovinceregion,
    e.locationpostalcode AS e_locationpostalcode,
    e.locationcountry AS e_locationcountry,
    e.locationaddressline1 AS e_locationaddressline1,
    e.altitude AS e_altitude,
    e.gispkey AS e_gispkey,
    s.sampleid AS s_sampleid,
    s.code AS s_code,
    s.samplingdate AS s_samplingdate,
    s.createdtimestamp AS s_createdtimestamp,
    s.lastmodifiedtimestamp AS s_lastmodifiedtimestamp,
    s.type AS s_type,
    s.identifierdomain AS s_identifierdomain,
    s.file AS s_file,
    s."position" AS s_position,
    s.state AS s_state,
    s.knots AS s_knots,
    s.description AS s_description,
    s.datecertaintyid AS s_datecertaintyid,
    s.typeid AS s_typeid,
    s.boxid AS s_boxid,
    s.comments AS s_comments,
    r.radiusid AS r_radiusid,
    r.code AS r_code,
    r.createdtimestamp AS r_createdtimestamp,
    r.lastmodifiedtimestamp AS r_lastmodifiedtimestamp,
    r.numberofsapwoodrings AS r_numberofsapwoodrings,
    r.pithid AS r_pithid,
    r.barkpresent AS r_barkpresent,
    r.lastringunderbark AS r_lastringunderbark,
    r.missingheartwoodringstopith AS r_missingheartwoodringstopith,
    r.missingheartwoodringstopithfoundation AS r_missingheartwoodringstopithfoundation,
    r.missingsapwoodringstobark AS r_missingsapwoodringstobark,
    r.missingsapwoodringstobarkfoundation AS r_missingsapwoodringstobarkfoundation,
    r.sapwoodid AS r_sapwoodid,
    r.heartwoodid AS r_heartwoodid,
    r.azimuth AS r_azimuth,
    r.comments AS r_comments,
    r.lastringunderbarkpresent AS r_lastringunderbarkpresent,
    r.nrofunmeasuredinnerrings AS r_nrofunmeasuredinnerrings,
    r.nrofunmeasuredouterrings AS r_nrofunmeasuredouterrings
   FROM ((public.tblelement e
     LEFT JOIN public.tblsample s ON ((e.elementid = s.elementid)))
     LEFT JOIN public.tblradius r ON ((s.sampleid = r.sampleid)));
 %   DROP VIEW public.vw_elementtoradius;
       public          tellervo    false    240    236    236    236    236    240    240    1305    236    236    236    236    236    236    236    236    236    236    236    236    239    236    236    236    236    239    236    236    236    239    239    236    239    239    236    236    236    236    239    239    239    239    240    240    239    239    239    239    239    239    239    239    240    240    240    239    240    236    236    236    240    240    240    236    236    236    236    240    240    239    240    236    236    236    236    240    240    3    3    3    3    3    3    3    3            }           1259    65778    vw_requestlogsummary    VIEW     A  CREATE VIEW public.vw_requestlogsummary AS
 SELECT tblrequestlog.requestlogid,
    (((tblsecurityuser.firstname)::text || ' '::text) || (tblsecurityuser.lastname)::text) AS name,
    tblrequestlog.ipaddr,
    tblrequestlog.createdtimestamp,
    tblrequestlog.wsversion,
    tblrequestlog.page,
    tblrequestlog.client,
    "substring"((tblrequestlog.request)::text, 53, 300) AS requesttrimmed
   FROM public.tblrequestlog,
    public.tblsecurityuser
  WHERE (tblrequestlog.securityuserid = tblsecurityuser.securityuserid)
  ORDER BY tblrequestlog.requestlogid DESC
 LIMIT 50;
 '   DROP VIEW public.vw_requestlogsummary;
       public          postgres    false    283    283    283    295    283    295    283    295    283    283    283            �           1259    66870    vw_tlotoradius    VIEW     }  CREATE VIEW public.vw_tlotoradius AS
 SELECT tlo.title AS tlo_title,
    tlo.code AS tlo_code,
    tlo.createdtimestamp AS tlo_createdtimestamp,
    tlo.lastmodifiedtimestamp AS tlo_lastmodifiedtimestamp,
    tlo.locationgeometry AS tlo_locationgeometry,
    tlo.locationtypeid AS tlo_locationtypeid,
    tlo.locationprecision AS tlo_locationprecision,
    tlo.locationcomment AS tlo_locationcomment,
    tlo.file AS tlo_file,
    tlo.creator AS tlo_creator,
    tlo.owner AS tlo_owner,
    tlo.parentobjectid AS tlo_parentobjectid,
    tlo.description AS tlo_description,
    tlo.objecttypeid AS tlo_objecttypeid,
    tlo.coveragetemporalid AS tlo_coveragetemporalid,
    tlo.coveragetemporalfoundationid AS tlo_coveragetemporalfoundationid,
    tlo.comments AS tlo_comments,
    tlo.coveragetemporal AS tlo_coveragetemporal,
    tlo.coveragetemporalfoundation AS tlo_coveragetemporalfoundation,
    tlo.locationaddressline1 AS tlo_locationaddressline1,
    tlo.locationaddressline2 AS tlo_locationaddressline2,
    tlo.locationcityortown AS tlo_locationcityortown,
    tlo.locationstateprovinceregion AS tlo_locationstateprovinceregion,
    tlo.locationpostalcode AS tlo_locationpostalcode,
    tlo.locationcountry AS tlo_locationcountry,
    others.tlo_objectid,
    others.e_elementid,
    others.e_taxonid,
    others.e_locationprecision,
    others.e_code,
    others.e_createdtimestamp,
    others.e_lastmodifiedtimestamp,
    others.e_locationgeometry,
    others.e_islivetree,
    others.e_originaltaxonname,
    others.e_locationtypeid,
    others.e_locationcomment,
    others.e_file,
    others.e_description,
    others.e_processing,
    others.e_marks,
    others.e_diameter,
    others.e_width,
    others.e_height,
    others.e_depth,
    others.e_unsupportedxml,
    others.e_units,
    others.e_elementtypeid,
    others.e_authenticity,
    others.e_elementshapeid,
    others.e_altitudeint,
    others.e_slopeangle,
    others.e_slopeazimuth,
    others.e_soildescription,
    others.e_soildepth,
    others.e_bedrockdescription,
    others.e_comments,
    others.e_locationaddressline2,
    others.e_locationcityortown,
    others.e_locationstateprovinceregion,
    others.e_locationpostalcode,
    others.e_locationcountry,
    others.e_locationaddressline1,
    others.e_altitude,
    others.e_gispkey,
    others.s_sampleid,
    others.s_code,
    others.s_samplingdate,
    others.s_createdtimestamp,
    others.s_lastmodifiedtimestamp,
    others.s_type,
    others.s_identifierdomain,
    others.s_file,
    others.s_position,
    others.s_state,
    others.s_knots,
    others.s_description,
    others.s_datecertaintyid,
    others.s_typeid,
    others.s_boxid,
    others.s_comments,
    others.r_radiusid,
    others.r_code,
    others.r_createdtimestamp,
    others.r_lastmodifiedtimestamp,
    others.r_numberofsapwoodrings,
    others.r_pithid,
    others.r_barkpresent,
    others.r_lastringunderbark,
    others.r_missingheartwoodringstopith,
    others.r_missingheartwoodringstopithfoundation,
    others.r_missingsapwoodringstobark,
    others.r_missingsapwoodringstobarkfoundation,
    others.r_sapwoodid,
    others.r_heartwoodid,
    others.r_azimuth,
    others.r_comments,
    others.r_lastringunderbarkpresent,
    others.r_nrofunmeasuredinnerrings,
    others.r_nrofunmeasuredouterrings
   FROM (public.tblobject tlo
     LEFT JOIN public.vw_elementtoradius others ON ((others.tlo_objectid = tlo.objectid)));
 !   DROP VIEW public.vw_tlotoradius;
       public          tellervo    false    415    231    231    231    231    231    231    231    231    231    231    231    231    231    231    231    231    231    231    231    231    231    231    231    231    231    231    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    415    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            ~           1259    65787    vwderivedcount    VIEW     L  CREATE VIEW public.vwderivedcount AS
 SELECT tblvmeasurementderivedcache.vmeasurementid,
    count(tblvmeasurementderivedcache.measurementid) AS count,
    public.first(tblvmeasurementderivedcache.measurementid) AS firstmeasurementid
   FROM public.tblvmeasurementderivedcache
  GROUP BY tblvmeasurementderivedcache.vmeasurementid;
 !   DROP VIEW public.vwderivedcount;
       public          postgres    false    2816    235    235                       1259    65791    vwderivedtitles    VIEW     �  CREATE VIEW public.vwderivedtitles AS
 SELECT dc.vmeasurementid,
    dc.count AS derivedcount,
    dc.firstmeasurementid,
        CASE
            WHEN (dc.count = 1) THEN o.objectid
            ELSE NULL::uuid
        END AS objectid,
        CASE
            WHEN (dc.count = 1) THEN o.title
            ELSE NULL::character varying
        END AS objecttitle,
        CASE
            WHEN (dc.count = 1) THEN o.code
            ELSE NULL::character varying
        END AS objectcode,
        CASE
            WHEN (dc.count = 1) THEN e.code
            ELSE NULL::character varying
        END AS elementcode,
        CASE
            WHEN (dc.count = 1) THEN s.code
            ELSE NULL::character varying
        END AS samplecode,
        CASE
            WHEN (dc.count = 1) THEN r.code
            ELSE NULL::character varying
        END AS radiuscode
   FROM (((((public.vwderivedcount dc
     LEFT JOIN public.tblmeasurement m ON ((m.measurementid = dc.firstmeasurementid)))
     LEFT JOIN public.tblradius r ON ((r.radiusid = m.radiusid)))
     LEFT JOIN public.tblsample s ON ((s.sampleid = r.sampleid)))
     LEFT JOIN public.tblelement e ON ((e.elementid = s.elementid)))
     LEFT JOIN public.tblobject o ON ((o.objectid = e.objectid)));
 "   DROP VIEW public.vwderivedtitles;
       public          postgres    false    236    236    239    239    239    234    234    240    231    231    240    231    382    382    236    240    382            �           1259    65796    vwdirectchildcount    VIEW       CREATE VIEW public.vwdirectchildcount AS
 SELECT tblvmeasurementgroup.membervmeasurementid AS vmeasurementid,
    count(tblvmeasurementgroup.vmeasurementid) AS directchildcount
   FROM public.tblvmeasurementgroup
  GROUP BY tblvmeasurementgroup.membervmeasurementid;
 %   DROP VIEW public.vwdirectchildcount;
       public          postgres    false    311    311            �           1259    65800    vwcomprehensivevm    VIEW     N	  CREATE VIEW public.vwcomprehensivevm AS
 SELECT dom.domainid,
    dom.domain,
    vm.vmeasurementid,
    vm.measurementid,
    vm.vmeasurementopid,
    vm.vmeasurementopparameter,
    vm.code,
    vm.comments,
    vm.ispublished,
    vm.owneruserid,
    vm.createdtimestamp,
    vm.lastmodifiedtimestamp,
    vm.isgenerating,
    vm.objective,
    vm.version,
    vm.birthdate,
    mc.startyear,
    mc.readingcount,
    mc.measurementcount,
    mc.objectcode AS objectcodesummary,
    mc.objectcount,
    mc.commontaxonname,
    mc.taxoncount,
    mc.prefix,
    m.radiusid,
    m.isreconciled,
    m.startyear AS measurementstartyear,
    m.islegacycleaned,
    m.importtablename,
    m.measuredbyid,
    concat(anly.firstname, ' ', anly.lastname) AS measuredby,
    mc.datingtypeid,
    m.datingerrorpositive,
    m.datingerrornegative,
    m.measurementvariableid,
    m.unitid,
    m.power,
    m.provenance,
    m.measuringmethodid,
    m.supervisedbyid,
    concat(spvr.firstname, ' ', spvr.lastname) AS supervisedby,
    su.username,
    op.name AS opname,
    dt.datingtype,
    mc.vmextent AS extentgeometry,
    cd.crossdateid,
    cd.mastervmeasurementid,
    cd.startyear AS newstartyear,
    cd.justification,
    cd.confidence,
    der.objectid,
    der.objecttitle,
    der.objectcode,
    der.elementcode,
    der.samplecode,
    der.radiuscode,
    dcc.directchildcount
   FROM (((((((((((public.tblvmeasurement vm
     JOIN public.tlkpvmeasurementop op ON ((vm.vmeasurementopid = op.vmeasurementopid)))
     LEFT JOIN public.tlkpdomain dom ON ((vm.domainid = dom.domainid)))
     LEFT JOIN public.vwderivedtitles der ON ((vm.vmeasurementid = der.vmeasurementid)))
     LEFT JOIN public.vwdirectchildcount dcc ON ((vm.vmeasurementid = dcc.vmeasurementid)))
     LEFT JOIN public.tblmeasurement m ON ((vm.measurementid = m.measurementid)))
     LEFT JOIN public.tblvmeasurementmetacache mc ON ((vm.vmeasurementid = mc.vmeasurementid)))
     LEFT JOIN public.tlkpdatingtype dt ON ((mc.datingtypeid = dt.datingtypeid)))
     LEFT JOIN public.tblsecurityuser su ON ((vm.owneruserid = su.securityuserid)))
     LEFT JOIN public.tblsecurityuser anly ON ((m.measuredbyid = anly.securityuserid)))
     LEFT JOIN public.tblsecurityuser spvr ON ((m.supervisedbyid = spvr.securityuserid)))
     LEFT JOIN public.tblcrossdate cd ON ((vm.vmeasurementid = cd.vmeasurementid)));
 $   DROP VIEW public.vwcomprehensivevm;
       public          tellervo    false    308    233    233    233    233    233    233    233    233    233    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    249    249    249    249    249    249    295    295    295    295    308    308    308    308    308    308    308    308    308    233    308    308    308    308    308    332    332    334    334    373    373    383    383    383    383    383    383    383    384    384    233    3    3    3    3    3    3    3    3            �           1259    65805    vwcomprehensivevm2    VIEW     �  CREATE VIEW public.vwcomprehensivevm2 AS
 SELECT vm.vmeasurementid,
    vm.measurementid,
    vm.vmeasurementopid,
    vm.vmeasurementopparameter,
    vm.code,
    vm.comments,
    vm.ispublished,
    vm.owneruserid,
    vm.createdtimestamp,
    vm.lastmodifiedtimestamp,
    vm.isgenerating,
    vm.objective,
    vm.version,
    vm.birthdate,
    mc.startyear,
    mc.readingcount,
    mc.measurementcount,
    mc.objectcode AS objectcodesummary,
    mc.objectcount,
    mc.commontaxonname,
    mc.taxoncount,
    mc.prefix,
    m.radiusid,
    m.isreconciled,
    m.startyear AS measurementstartyear,
    m.islegacycleaned,
    m.importtablename,
    m.measuredbyid,
    mc.datingtypeid,
    m.datingerrorpositive,
    m.datingerrornegative,
    m.measurementvariableid,
    m.unitid,
    m.power,
    m.provenance,
    m.measuringmethodid,
    m.supervisedbyid,
    su.username,
    op.name AS opname,
    dt.datingtype,
    mc.vmextent AS extentgeometry,
    cd.crossdateid,
    cd.mastervmeasurementid,
    cd.startyear AS newstartyear,
    cd.justification,
    cd.confidence,
    der.objectid,
    der.objecttitle,
    der.objectcode,
    der.elementcode,
    der.samplecode,
    der.radiuscode,
    dcc.directchildcount
   FROM ((((((((public.tblvmeasurement vm
     JOIN public.tlkpvmeasurementop op ON ((vm.vmeasurementopid = op.vmeasurementopid)))
     LEFT JOIN public.vwderivedtitles der ON ((vm.vmeasurementid = der.vmeasurementid)))
     LEFT JOIN public.vwdirectchildcount dcc ON ((vm.vmeasurementid = dcc.vmeasurementid)))
     LEFT JOIN public.tblmeasurement m ON ((vm.measurementid = m.measurementid)))
     LEFT JOIN public.tblvmeasurementmetacache mc ON ((vm.vmeasurementid = mc.vmeasurementid)))
     LEFT JOIN public.tlkpdatingtype dt ON ((mc.datingtypeid = dt.datingtypeid)))
     LEFT JOIN public.tblsecurityuser su ON ((vm.owneruserid = su.securityuserid)))
     LEFT JOIN public.tblcrossdate cd ON ((vm.vmeasurementid = cd.vmeasurementid)));
 %   DROP VIEW public.vwcomprehensivevm2;
       public          tellervo    false    384    234    234    234    234    234    234    234    234    233    234    233    249    249    233    249    233    249    233    234    234    233    249    233    249    295    295    233    308    233    308    308    233    308    234    308    234    308    234    308    234    308    308    233    308    308    308    308    308    332    332    373    373    383    383    383    383    383    383    383    384    3    3    3    3    3    3    3    3            �           1259    65810    vwcountperpersonperobject    VIEW     G  CREATE VIEW public.vwcountperpersonperobject AS
 SELECT o.code AS objectcode,
    (((seu.lastname)::text || ', '::text) || (seu.firstname)::text) AS name,
    count(vm.owneruserid) AS count
   FROM ((((((public.tblvmeasurement vm
     LEFT JOIN public.tblmeasurement m ON ((vm.measurementid = m.measurementid)))
     LEFT JOIN public.tblsecurityuser seu ON ((seu.securityuserid = vm.owneruserid)))
     LEFT JOIN public.tblradius r ON ((m.radiusid = r.radiusid)))
     LEFT JOIN public.tblsample s ON ((r.sampleid = s.sampleid)))
     LEFT JOIN public.tblelement e ON ((s.elementid = e.elementid)))
     LEFT JOIN public.tblobject o ON ((e.objectid = o.objectid)))
  WHERE (vm.vmeasurementopid = 5)
  GROUP BY vm.owneruserid, o.code, (((seu.lastname)::text || ', '::text) || (seu.firstname)::text)
  ORDER BY (count(vm.owneruserid)) DESC;
 ,   DROP VIEW public.vwcountperpersonperobject;
       public          postgres    false    236    239    236    234    234    231    231    295    308    295    308    308    295    240    240    239            �           1259    65823    vwlabcodesforsamples    VIEW     S  CREATE VIEW public.vwlabcodesforsamples AS
 SELECT o.objectid,
    o.code AS objectcode,
    e.elementid,
    e.code AS elementcode,
    s.sampleid,
    s.code AS samplecode
   FROM ((public.tblsample s
     LEFT JOIN public.tblelement e ON ((e.elementid = s.elementid)))
     LEFT JOIN public.tblobject o ON ((o.objectid = e.objectid)));
 '   DROP VIEW public.vwlabcodesforsamples;
       public          postgres    false    236    240    240    240    231    231    236    236            p           0    0    TABLE vwlabcodesforsamples    ACL     >   GRANT ALL ON TABLE public.vwlabcodesforsamples TO "Webgroup";
          public          postgres    false    390            �           1259    66920 
   vwlocation    VIEW     ]  CREATE VIEW public.vwlocation AS
 SELECT tblobject.objectid AS entityid,
    tblobject.locationgeometry,
    tblobject.locationprecision,
    tblobject.locationcomment,
    tblobject.locationcountry,
    tblobject.locationpostalcode,
    tblobject.locationstateprovinceregion,
    tblobject.locationcityortown,
    tblobject.locationaddressline2,
    tblobject.locationaddressline1,
    tblobject.locationtypeid
   FROM public.tblobject
UNION
 SELECT tblelement.elementid AS entityid,
    tblelement.locationgeometry,
    tblelement.locationprecision,
    tblelement.locationcomment,
    tblelement.locationcountry,
    tblelement.locationpostalcode,
    tblelement.locationstateprovinceregion,
    tblelement.locationcityortown,
    tblelement.locationaddressline2,
    tblelement.locationaddressline1,
    tblelement.locationtypeid
   FROM public.tblelement;
    DROP VIEW public.vwlocation;
       public          tellervo    false    236    236    236    236    236    236    236    236    236    236    236    231    231    231    231    231    231    231    231    231    231    231    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1259    65855    vwtblcurationevent    VIEW     T  CREATE VIEW public.vwtblcurationevent AS
 SELECT c.curationeventid,
    c.curationstatusid,
    c.sampleid,
    c.boxid,
    c.createdtimestamp,
    c.curatorid,
    c.loanid,
    c.notes,
    cl.curationstatus
   FROM (public.tblcurationevent c
     LEFT JOIN public.tlkpcurationstatus cl ON ((c.curationstatusid = cl.curationstatusid)));
 %   DROP VIEW public.vwtblcurationevent;
       public          tellervo    false    251    327    251    251    251    251    251    327    251    251            �           1259    65859    vwtblcurationeventmostrecent    VIEW     ~  CREATE VIEW public.vwtblcurationeventmostrecent AS
 SELECT c.curationeventid,
    c.curationstatusid,
    c.curationstatus,
    c.sampleid,
    c.boxid,
    c.createdtimestamp,
    c.curatorid,
    c.loanid,
    c.notes
   FROM public.vwtblcurationevent c
  WHERE (NOT (EXISTS ( SELECT c2.curationeventid,
            c2.curationstatusid,
            c2.sampleid,
            c2.createdtimestamp,
            c2.curatorid,
            c2.loanid,
            c2.notes,
            c2.storagelocation
           FROM public.tblcurationevent c2
          WHERE ((c2.createdtimestamp > c.createdtimestamp) AND (c.sampleid = c2.sampleid)))));
 /   DROP VIEW public.vwtblcurationeventmostrecent;
       public          tellervo    false    397    397    397    397    397    397    397    397    397    251    251    251    251    251    251    251    251            �           1259    66846    vwtlkptaxon    VIEW       CREATE VIEW public.vwtlkptaxon AS
 SELECT taxon.taxonid,
    taxon.label AS taxonlabel,
    taxon.parenttaxonid,
    taxon.colid,
    taxon.colparentid,
    rank.taxonrank
   FROM (public.tlkptaxon taxon
     JOIN public.tlkptaxonrank rank ON ((rank.taxonrankid = taxon.taxonrankid)));
    DROP VIEW public.vwtlkptaxon;
       public          tellervo    false    367    367    368    367    367    368    367    367            �           1259    66855    vwtblelement    VIEW     �	  CREATE VIEW public.vwtblelement AS
 SELECT ( SELECT findobjecttoplevelancestor.code
           FROM cpgdb.findobjecttoplevelancestor(e.objectid) findobjecttoplevelancestor(objectid, title, code, createdtimestamp, lastmodifiedtimestamp, locationgeometry, locationtypeid, locationprecision, locationcomment, creator, owner, parentobjectid, description, objecttypeid, coveragetemporalid, coveragetemporalfoundationid, comments, coveragetemporal, coveragetemporalfoundation, locationaddressline1, locationaddressline2, locationcityortown, locationstateprovinceregion, locationpostalcode, locationcountry, locationcountry_1, vegetationtype, domainid, projectid)) AS objectcode,
    e.comments,
    dom.domainid,
    dom.domain,
    e.elementid,
    e.locationprecision,
    e.code AS title,
    e.code,
    e.createdtimestamp,
    e.lastmodifiedtimestamp,
    e.locationgeometry,
    ( SELECT public.st_asgml(3, e.locationgeometry, 15, 1) AS st_asgml) AS gml,
    public.st_xmin((e.locationgeometry)::public.box3d) AS longitude,
    public.st_ymin((e.locationgeometry)::public.box3d) AS latitude,
    e.islivetree,
    e.originaltaxonname,
    e.locationtypeid,
    e.locationcomment,
    e.locationaddressline1,
    e.locationaddressline2,
    e.locationcityortown,
    e.locationstateprovinceregion,
    e.locationpostalcode,
    e.locationcountry,
    array_to_string(e.file, '><'::text) AS file,
    e.description,
    e.processing,
    e.marks,
    e.diameter,
    e.width,
    e.height,
    e.depth,
    e.unsupportedxml,
    e.objectid,
    e.elementtypeid,
    e.authenticity,
    e.elementshapeid,
    shape.elementshape,
    tbltype.elementtype,
    loctype.locationtype,
    e.altitude,
    e.slopeangle,
    e.slopeazimuth,
    e.soildescription,
    e.soildepth,
    e.bedrockdescription,
    vwt.taxonid,
    vwt.taxonlabel,
    vwt.parenttaxonid,
    vwt.colid,
    vwt.colparentid,
    vwt.taxonrank,
    unit.unit AS units,
    unit.unitid,
    cpgdb.getbestgeometryid(e.elementid) AS bestgeometryid
   FROM ((((((public.tblelement e
     LEFT JOIN public.tlkpdomain dom ON ((e.domainid = dom.domainid)))
     LEFT JOIN public.tlkpelementshape shape ON ((e.elementshapeid = shape.elementshapeid)))
     LEFT JOIN public.tlkpelementtype tbltype ON ((e.elementtypeid = tbltype.elementtypeid)))
     LEFT JOIN public.tlkplocationtype loctype ON ((e.locationtypeid = loctype.locationtypeid)))
     LEFT JOIN public.tlkpunit unit ON ((e.units = unit.unitid)))
     LEFT JOIN public.vwtlkptaxon vwt ON (((e.taxonid)::text = (vwt.taxonid)::text)));
    DROP VIEW public.vwtblelement;
       public          tellervo    false    236    412    412    412    412    412    369    369    344    344    340    340    338    338    334    334    236    236    236    236    236    236    236    236    236    236    236    236    236    236    236    236    236    236    412    236    236    236    1737    236    236    236    236    236    236    236    236    236    236    236    236    236    236    236    236    236    1230    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1259    65876    vwtblobject    VIEW     *  CREATE VIEW public.vwtblobject AS
 SELECT cquery.countofchildvmeasurements,
    o.projectid,
    o.vegetationtype,
    o.comments,
    o.objectid,
    dom.domainid,
    dom.domain,
    o.title,
    o.code,
    o.createdtimestamp,
    o.lastmodifiedtimestamp,
    o.locationgeometry,
    ( SELECT public.st_asgml(3, o.locationgeometry, 15, 1) AS st_asgml) AS gml,
    public.xmin((o.locationgeometry)::public.box3d) AS longitude,
    public.ymin((o.locationgeometry)::public.box3d) AS latitude,
    o.locationtypeid,
    o.locationprecision,
    o.locationcomment,
    o.locationaddressline1,
    o.locationaddressline2,
    o.locationcityortown,
    o.locationstateprovinceregion,
    o.locationpostalcode,
    o.locationcountry,
    array_to_string(o.file, '><'::text) AS file,
    o.creator,
    o.owner,
    o.parentobjectid,
    o.description,
    o.objecttypeid,
    loctype.locationtype,
    objtype.objecttype,
    covtemp.coveragetemporal,
    covtempfound.coveragetemporalfoundation
   FROM ((((((public.tblobject o
     LEFT JOIN public.tlkpdomain dom ON ((o.domainid = dom.domainid)))
     LEFT JOIN public.tlkplocationtype loctype ON ((o.locationtypeid = loctype.locationtypeid)))
     LEFT JOIN public.tlkpobjecttype objtype ON ((o.objecttypeid = objtype.objecttypeid)))
     LEFT JOIN public.tlkpcoveragetemporal covtemp ON ((o.coveragetemporalid = covtemp.coveragetemporalid)))
     LEFT JOIN public.tlkpcoveragetemporalfoundation covtempfound ON ((o.coveragetemporalfoundationid = covtempfound.coveragetemporalfoundationid)))
     LEFT JOIN ( SELECT e.objectid AS masterobjectid,
            count(e.objectid) AS countofchildvmeasurements
           FROM ((((public.tblelement e
             JOIN public.tblsample s ON ((s.elementid = e.elementid)))
             JOIN public.tblradius r ON ((r.sampleid = s.sampleid)))
             JOIN public.tblmeasurement m ON ((m.radiusid = r.radiusid)))
             JOIN public.tblvmeasurementderivedcache vc ON ((vc.measurementid = m.measurementid)))
          GROUP BY e.objectid) cquery ON ((cquery.masterobjectid = o.objectid)));
    DROP VIEW public.vwtblobject;
       public          tellervo    false    231    231    231    231    240    240    231    231    231    1728    231    231    1731    323    323    350    325    325    334    231    231    231    350    231    231    334    344    344    231    231    231    234    231    231    231    231    234    235    236    231    231    231    231    236    231    231    239    239    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1259    65881    vwtblproject    VIEW     �  CREATE VIEW public.vwtblproject AS
 SELECT p.projectid,
    dom.domainid,
    dom.domain,
    p.title,
    array_to_string(p.projecttypes, '><'::text) AS types,
    p.createdtimestamp,
    p.lastmodifiedtimestamp,
    p.comments,
    p.description,
    array_to_string(p.file, '><'::text) AS file,
    p.investigator,
    p.period,
    p.requestdate,
    p.commissioner,
    p.reference,
    p.research,
    p.laboratories,
    p.projectcategoryid,
    pc.projectcategory
   FROM ((public.tblproject p
     LEFT JOIN public.tlkpdomain dom ON ((p.domainid = dom.domainid)))
     LEFT JOIN public.tlkpprojectcategory pc ON ((p.projectcategoryid = pc.projectcategoryid)));
    DROP VIEW public.vwtblproject;
       public          tellervo    false    269    269    269    269    269    334    269    269    334    269    269    269    269    269    269    355    355    269    269    269    269            �           1259    66926    vwtblsample2    VIEW     �  CREATE VIEW public.vwtblsample2 AS
 SELECT s.externalid,
    s.sampleid,
    s.code,
    s.comments,
    s.code AS title,
    s.elementid,
    s.samplingdate,
    s.samplingdateprec,
    s.createdtimestamp,
    s.lastmodifiedtimestamp,
    st.sampletypeid,
    st.sampletype,
    array_to_string(s.file, '><'::text) AS file,
    s."position",
    s.state,
    s.knots,
    s.description,
    array_to_string(s.userdefinedfielddata, '><'::text) AS userdefinedfielddata,
    dc.datecertainty,
    s.boxid,
    lc.objectcode,
    lc.objectid,
    lc.elementcode,
    c.curationstatusid,
    c.curationstatus,
    s.samplingmonth,
    s.samplingyear,
    s.samplestatusid,
    s.dendrochronologist,
    ss.samplestatus
   FROM (((((public.tblsample s
     LEFT JOIN public.tlkpsamplestatus ss ON ((s.samplestatusid = ss.samplestatusid)))
     LEFT JOIN public.tlkpdatecertainty dc ON ((s.datecertaintyid = dc.datecertaintyid)))
     LEFT JOIN public.tlkpsampletype st ON ((s.typeid = st.sampletypeid)))
     LEFT JOIN public.vwtblcurationeventmostrecent c ON ((s.sampleid = c.sampleid)))
     LEFT JOIN public.vwlabcodesforsamples lc ON ((s.sampleid = lc.sampleid)));
    DROP VIEW public.vwtblsample2;
       public          tellervo    false    398    398    398    390    390    390    390    362    362    360    360    330    330    240    240    240    240    240    240    240    240    240    240    240    240    240    240    240    240    240    240    240    240    240    240    2218            �           1259    66934    vwipt    VIEW     n  CREATE VIEW public.vwipt AS
 SELECT p.projectid,
    p.title AS ptitle,
    p.createdtimestamp AS pcreatedtimestamp,
    p.lastmodifiedtimestamp AS plastmodifiedtimestamp,
    p.comments AS pcomments,
    p.description AS pdescription,
    p.file AS pfile,
    p.projectcategory,
    p.investigator AS recordedby,
    p.period AS pperiod,
    p.reference AS projectreference,
    o1.objectid AS o1objectid,
    o1.title AS o1title,
    o1.code AS o1code,
    e.elementid,
    e.code AS ecode,
    e.taxonid AS acceptednameusageid,
    public.st_x(public.st_centroid(l.locationgeometry)) AS decimallongitude,
    public.st_y(public.st_centroid(l.locationgeometry)) AS decimallatitude,
    e.locationprecision AS coordinateuncertaintyinmeters,
    e.locationcomment AS locationremarks,
    e.locationcountry AS country,
    e.locationstateprovinceregion AS stateprovince,
    e.altitude AS verbatimelevation,
    t.kingdom,
    t.phylum,
    t.class,
    t.txorder AS "order",
    t.family,
    t.subfamily,
    t.genus,
    t.subgenus,
    t.section AS infragenericepithet,
    t.species,
    t.subspecies AS infraspecificepithet,
    t.variety AS cultivarepithet,
    td.label AS scientificname,
    td.parenttaxonid AS parentnameusageid,
    tr.taxonrank,
    'accepted'::text AS taxonomicstatus,
    'icn'::text AS nomenclaturalcode,
    s.sampleid AS occurrenceid,
    s.externalid AS recordnumber,
    s.dendrochronologist AS identifiedby,
    cpgdb.getdateday(s.samplingdate, s.samplingdateprec) AS day,
    cpgdb.getdatemonth(s.samplingdate, s.samplingdateprec) AS month,
    cpgdb.getdateyear(s.samplingdate, s.samplingdateprec) AS year,
    cpgdb.getdatestring(s.samplingdate, s.samplingdateprec) AS eventdate,
    'Physical object'::character varying AS type,
    to_char(s.lastmodifiedtimestamp, 'YYYY-MM-DD"T"HH24:MI:SSTZHTZM'::text) AS modified,
    'en'::character varying AS language,
    'Arizona Board of Regents'::character varying AS rightsholder,
    'http://grscicoll.org/cool/mhy6-8hem'::character varying AS institutionid,
    'LTRR'::character varying AS institutioncode,
    'Laboratory of Tree-Ring Research Natural History Collection'::character varying AS datasetname,
    'PreservedSpecimen'::character varying AS basisofrecord,
    '1'::character varying AS individualcount,
    '1'::character varying AS organismquantity,
    'individuals'::character varying AS organismquantitytype,
    'present'::character varying AS occurrencestatus,
    'wood specimen'::character varying AS preparations,
    'WGS84'::character varying AS geodeticdatum
   FROM (((((((public.vwtblproject p
     LEFT JOIN public.vwtblobject o1 ON ((p.projectid = o1.projectid)))
     LEFT JOIN public.vwtblelement e ON ((e.objectid = o1.objectid)))
     LEFT JOIN public.stblflattenedtaxonomy t ON (((e.taxonid)::text = (t.taxonid)::text)))
     LEFT JOIN public.tlkptaxon td ON (((e.taxonid)::text = (td.taxonid)::text)))
     LEFT JOIN public.tlkptaxonrank tr ON ((tr.taxonrankid = td.taxonrankid)))
     LEFT JOIN public.vwtblsample2 s ON ((s.elementid = e.elementid)))
     LEFT JOIN public.vwlocation l ON ((l.entityid = e.bestgeometryid)))
  WHERE (s.sampleid IS NOT NULL);
    DROP VIEW public.vwipt;
       public          tellervo    false    425    1747    1746    1745    427    427    427    427    427    427    427    426    426    425    425    425    425    425    425    425    425    425    425    425    425    413    413    413    413    413    413    413    413    413    413    403    403    403    403    403    403    403    403    403    403    403    402    402    402    402    368    368    367    367    367    367    1240    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1259    65815    vwresultnotesasarray    VIEW     �  CREATE VIEW public.vwresultnotesasarray AS
 SELECT tblvmeasurementreadingnoteresult.relyear,
    tblvmeasurementreadingnoteresult.vmeasurementresultid,
    public.array_accum(tblvmeasurementreadingnoteresult.readingnoteid) AS noteids,
    public.array_accum(tblvmeasurementreadingnoteresult.inheritedcount) AS inheritedcounts
   FROM public.tblvmeasurementreadingnoteresult
  GROUP BY tblvmeasurementreadingnoteresult.relyear, tblvmeasurementreadingnoteresult.vmeasurementresultid;
 '   DROP VIEW public.vwresultnotesasarray;
       public          postgres    false    314    314    2813    314    314            q           0    0    TABLE vwresultnotesasarray    ACL     >   GRANT ALL ON TABLE public.vwresultnotesasarray TO "Webgroup";
          public          postgres    false    388            �           1259    65819    vwjsonnotedreadingresult    VIEW       CREATE VIEW public.vwjsonnotedreadingresult AS
 SELECT res.vmeasurementreadingresultid,
    res.vmeasurementresultid,
    res.relyear,
    res.reading,
    res.wjinc,
    res.wjdec,
    res.count,
    res.readingid,
    res.ewwidth,
    res.lwwidth,
    cpgdb.resultnotestojson(notes.noteids, notes.inheritedcounts) AS jsonnotes
   FROM (public.tblvmeasurementreadingresult res
     LEFT JOIN public.vwresultnotesasarray notes ON (((res.vmeasurementresultid = notes.vmeasurementresultid) AND (res.relyear = notes.relyear))));
 +   DROP VIEW public.vwjsonnotedreadingresult;
       public          postgres    false    241    388    241    241    241    241    241    241    241    388    241    241    1303    388    388            r           0    0    TABLE vwjsonnotedreadingresult    ACL     B   GRANT ALL ON TABLE public.vwjsonnotedreadingresult TO "Webgroup";
          public          postgres    false    389            �           1259    65828    vwringsleaderboard    VIEW     [  CREATE VIEW public.vwringsleaderboard AS
 SELECT su.securityuserid,
    (((su.lastname)::text || ', '::text) || (su.firstname)::text) AS name,
    count(r.*) AS rings
   FROM (((public.tblvmeasurement vm
     LEFT JOIN public.tblsecurityuser su ON ((vm.owneruserid = su.securityuserid)))
     LEFT JOIN public.tblmeasurement m ON ((vm.measurementid = m.measurementid)))
     LEFT JOIN public.tblreading r ON ((m.measurementid = r.measurementid)))
  WHERE (vm.vmeasurementopid = 5)
  GROUP BY (((su.lastname)::text || ', '::text) || (su.firstname)::text), su.securityuserid
  ORDER BY (count(r.*)) DESC;
 %   DROP VIEW public.vwringsleaderboard;
       public          postgres    false    308    295    295    295    275    234    308    308            �           1259    65833    vwringwidthleaderboard    VIEW     p  CREATE VIEW public.vwringwidthleaderboard AS
 SELECT su.securityuserid,
    (((su.lastname)::text || ', '::text) || (su.firstname)::text) AS name,
    sum(r.reading) AS totalringwidth
   FROM (((public.tblvmeasurement vm
     LEFT JOIN public.tblsecurityuser su ON ((vm.owneruserid = su.securityuserid)))
     LEFT JOIN public.tblmeasurement m ON ((vm.measurementid = m.measurementid)))
     LEFT JOIN public.tblreading r ON ((m.measurementid = r.measurementid)))
  WHERE (vm.vmeasurementopid = 5)
  GROUP BY (((su.lastname)::text || ', '::text) || (su.firstname)::text), su.securityuserid
  ORDER BY (sum(r.reading)) DESC;
 )   DROP VIEW public.vwringwidthleaderboard;
       public          postgres    false    308    308    308    295    295    295    275    275    234            �           1259    65838    vwsampleleaderboard    VIEW       CREATE VIEW public.vwsampleleaderboard AS
 SELECT su.securityuserid,
    (((su.lastname)::text || ', '::text) || (su.firstname)::text) AS name,
    count(m.*) AS samples
   FROM ((public.tblvmeasurement vm
     LEFT JOIN public.tblsecurityuser su ON ((vm.owneruserid = su.securityuserid)))
     LEFT JOIN public.tblmeasurement m ON ((vm.measurementid = m.measurementid)))
  WHERE (vm.vmeasurementopid = 5)
  GROUP BY (((su.lastname)::text || ', '::text) || (su.firstname)::text), su.securityuserid
  ORDER BY (count(m.*)) DESC;
 &   DROP VIEW public.vwsampleleaderboard;
       public          postgres    false    308    234    295    295    295    308    308            �           1259    65843    vwleaderboard    VIEW     \  CREATE VIEW public.vwleaderboard AS
 SELECT slb.securityuserid,
    slb.name,
    slb.samples,
    rlb.rings,
    rwlb.totalringwidth
   FROM public.vwsampleleaderboard slb,
    public.vwringsleaderboard rlb,
    public.vwringwidthleaderboard rwlb
  WHERE ((slb.securityuserid = rlb.securityuserid) AND (slb.securityuserid = rwlb.securityuserid));
     DROP VIEW public.vwleaderboard;
       public          postgres    false    391    391    392    392    393    393    393            �           1259    65847    vwnotedreadingresult    VIEW       CREATE VIEW public.vwnotedreadingresult AS
 SELECT res.vmeasurementreadingresultid,
    res.vmeasurementresultid,
    res.relyear,
    res.reading,
    res.wjinc,
    res.wjdec,
    res.count,
    res.readingid,
    array_to_string(notes.noteids, ','::text) AS noteids,
    array_to_string(notes.inheritedcounts, ','::text) AS inheritedcounts
   FROM (public.tblvmeasurementreadingresult res
     LEFT JOIN public.vwresultnotesasarray notes ON (((res.vmeasurementresultid = notes.vmeasurementresultid) AND (res.relyear = notes.relyear))));
 '   DROP VIEW public.vwnotedreadingresult;
       public          postgres    false    388    241    241    241    241    241    241    388    388    388    241    241            s           0    0    TABLE vwnotedreadingresult    ACL     >   GRANT ALL ON TABLE public.vwnotedreadingresult TO "Webgroup";
          public          postgres    false    395            �           1259    65851    vwstartyear    VIEW     �   CREATE VIEW public.vwstartyear AS
 SELECT tblreading.measurementid,
    min(tblreading.relyear) AS minofrelyear
   FROM public.tblreading
  GROUP BY tblreading.measurementid;
    DROP VIEW public.vwstartyear;
       public          postgres    false    275    275            t           0    0    TABLE vwstartyear    ACL     5   GRANT ALL ON TABLE public.vwstartyear TO "Webgroup";
          public          postgres    false    396            �           1259    65863    vwtblbox    VIEW     "  CREATE VIEW public.vwtblbox AS
 SELECT b.boxid,
    b.title,
    b.curationlocation,
    c.curationstatus,
    b.trackinglocation,
    b.createdtimestamp,
    b.lastmodifiedtimestamp,
    b.comments,
    count(s.sampleid) AS samplecount
   FROM ((public.tblbox b
     LEFT JOIN public.tblsample s ON ((b.boxid = s.boxid)))
     LEFT JOIN public.vwtblcurationeventmostrecent c ON ((b.boxid = c.boxid)))
  GROUP BY b.boxid, b.title, b.curationlocation, c.curationstatus, b.trackinglocation, b.createdtimestamp, b.lastmodifiedtimestamp, b.comments;
    DROP VIEW public.vwtblbox;
       public          postgres    false    246    398    398    246    246    246    246    246    246    240    240            u           0    0    TABLE vwtblbox    ACL     2   GRANT ALL ON TABLE public.vwtblbox TO "Webgroup";
          public          postgres    false    399            �           1259    66860    vwtblelement2    VIEW     �	  CREATE VIEW public.vwtblelement2 AS
 SELECT ( SELECT findobjecttoplevelancestor.objectid
           FROM cpgdb.findobjecttoplevelancestor(e.objectid) findobjecttoplevelancestor(objectid, title, code, createdtimestamp, lastmodifiedtimestamp, locationgeometry, locationtypeid, locationprecision, locationcomment, creator, owner, parentobjectid, description, objecttypeid, coveragetemporalid, coveragetemporalfoundationid, comments, coveragetemporal, coveragetemporalfoundation, locationaddressline1, locationaddressline2, locationcityortown, locationstateprovinceregion, locationpostalcode, locationcountry, locationcountry_1, vegetationtype, domainid, projectid)) AS tlobjectid,
    e.comments,
    dom.domainid,
    dom.domain,
    e.elementid,
    e.locationprecision,
    e.code AS title,
    e.code,
    e.createdtimestamp,
    e.lastmodifiedtimestamp,
    e.locationgeometry,
    ( SELECT public.st_asgml(3, e.locationgeometry, 15, 1) AS st_asgml) AS gml,
    public.st_xmin((e.locationgeometry)::public.box3d) AS longitude,
    public.st_ymin((e.locationgeometry)::public.box3d) AS latitude,
    e.islivetree,
    e.originaltaxonname,
    e.locationtypeid,
    e.locationcomment,
    e.locationaddressline1,
    e.locationaddressline2,
    e.locationcityortown,
    e.locationstateprovinceregion,
    e.locationpostalcode,
    e.locationcountry,
    array_to_string(e.file, '><'::text) AS file,
    e.description,
    e.processing,
    e.marks,
    e.diameter,
    e.width,
    e.height,
    e.depth,
    e.unsupportedxml,
    e.objectid,
    e.elementtypeid,
    e.authenticity,
    e.elementshapeid,
    shape.elementshape,
    tbltype.elementtype,
    loctype.locationtype,
    e.altitude,
    e.slopeangle,
    e.slopeazimuth,
    e.soildescription,
    e.soildepth,
    e.bedrockdescription,
    vwt.taxonid,
    vwt.taxonlabel,
    vwt.parenttaxonid,
    vwt.colid,
    vwt.colparentid,
    vwt.taxonrank,
    unit.unit AS units,
    unit.unitid,
    cpgdb.getbestgeometryid(e.elementid) AS bestgeometryid
   FROM ((((((public.tblelement e
     LEFT JOIN public.tlkpdomain dom ON ((e.domainid = dom.domainid)))
     LEFT JOIN public.tlkpelementshape shape ON ((e.elementshapeid = shape.elementshapeid)))
     LEFT JOIN public.tlkpelementtype tbltype ON ((e.elementtypeid = tbltype.elementtypeid)))
     LEFT JOIN public.tlkplocationtype loctype ON ((e.locationtypeid = loctype.locationtypeid)))
     LEFT JOIN public.tlkpunit unit ON ((e.units = unit.unitid)))
     LEFT JOIN public.vwtlkptaxon vwt ON (((e.taxonid)::text = (vwt.taxonid)::text)));
     DROP VIEW public.vwtblelement2;
       public          tellervo    false    340    3    3    3    3    3    3    3    3    3    1230    236    236    236    236    236    236    236    236    236    236    236    236    3    3    3    3    3    3    3    3    3    3    3    3    236    340    344    236    236    236    236    236    236    236    236    236    236    236    236    236    334    344    236    236    334    369    369    236    236    236    236    412    412    412    236    236    236    338    1737    338    412    236    236    3    3    3    3    412    412    3    3    3    3    236    236    3    3    3    3    3    3    3    3            �           1259    65868 	   vwtblloan    VIEW     !  CREATE VIEW public.vwtblloan AS
 SELECT l.loanid,
    l.firstname,
    l.lastname,
    l.organisation,
    l.duedate,
    l.issuedate,
    l.returndate,
    age(l.duedate, l.issuedate) AS loanperiod,
    array_to_string(l.files, '><'::text) AS files,
    l.notes
   FROM public.tblloan l;
    DROP VIEW public.vwtblloan;
       public          tellervo    false    238    238    238    238    238    238    238    238    238            �           1259    65872    vwtblmeasurement    VIEW     �  CREATE VIEW public.vwtblmeasurement AS
 SELECT tblmeasurement.measurementid,
    tblmeasurement.radiusid,
    tblmeasurement.isreconciled AS measurementidreconciled,
    tblmeasurement.islegacycleaned,
    tblmeasurement.importtablename,
    tblmeasurement.measuredbyid,
    tblsecurityuser.username AS measuredbyusername,
    tlkpdatingtype.datingtype,
    tblmeasurement.datingerrorpositive,
    tblmeasurement.datingerrornegative
   FROM public.tblmeasurement,
    public.tblsecurityuser,
    public.tlkpdatingtype
  WHERE ((tblmeasurement.measuredbyid = tblsecurityuser.securityuserid) AND (tblmeasurement.datingtypeid = tlkpdatingtype.datingtypeid));
 #   DROP VIEW public.vwtblmeasurement;
       public          postgres    false    332    332    295    295    234    234    234    234    234    234    234    234    234            �           1259    65886    vwtblradius    VIEW     �  CREATE VIEW public.vwtblradius AS
 SELECT tblradius.comments,
    dom.domainid,
    dom.domain,
    tblradius.radiusid,
    tblradius.sampleid,
    tblradius.code AS radiuscode,
    tblradius.azimuth,
    tblradius.createdtimestamp AS radiuscreated,
    tblradius.lastmodifiedtimestamp AS radiuslastmodified,
    tblradius.numberofsapwoodrings,
    tblradius.barkpresent,
    tblradius.lastringunderbark,
    tblradius.lastringunderbarkpresent,
    tblradius.missingheartwoodringstopith,
    tblradius.missingheartwoodringstopithfoundation,
    tblradius.missingsapwoodringstobark,
    tblradius.missingsapwoodringstobarkfoundation,
    tblradius.nrofunmeasuredouterrings,
    tblradius.nrofunmeasuredinnerrings,
    spw.complexpresenceabsenceid AS sapwoodid,
    spw.complexpresenceabsence AS sapwood,
    hwd.complexpresenceabsenceid AS heartwoodid,
    hwd.complexpresenceabsence AS heartwood,
    pth.complexpresenceabsenceid AS pithid,
    pth.complexpresenceabsence AS pith
   FROM ((((public.tblradius
     LEFT JOIN public.tlkpdomain dom ON ((tblradius.domainid = dom.domainid)))
     LEFT JOIN public.tlkpcomplexpresenceabsence spw ON ((tblradius.sapwoodid = spw.complexpresenceabsenceid)))
     LEFT JOIN public.tlkpcomplexpresenceabsence hwd ON ((tblradius.heartwoodid = hwd.complexpresenceabsenceid)))
     LEFT JOIN public.tlkpcomplexpresenceabsence pth ON ((tblradius.pithid = pth.complexpresenceabsenceid)));
    DROP VIEW public.vwtblradius;
       public          tellervo    false    239    239    239    239    239    239    239    239    239    239    239    239    239    239    239    239    239    239    239    239    239    321    321    334    334            �           1259    65891    vwtblsample    VIEW     `  CREATE VIEW public.vwtblsample AS
 SELECT s.externalid,
    s.sampleid,
    s.code,
    s.comments,
    s.code AS title,
    s.elementid,
    s.samplingdate,
    s.samplingdateprec,
    s.createdtimestamp,
    s.lastmodifiedtimestamp,
    st.sampletypeid,
    st.sampletype,
    array_to_string(s.file, '><'::text) AS file,
    s."position",
    s.state,
    s.knots,
    s.description,
    array_to_string(s.userdefinedfielddata, '><'::text) AS userdefinedfielddata,
    dc.datecertainty,
    s.boxid,
    lc.objectcode,
    lc.elementcode,
    c.curationstatusid,
    c.curationstatus,
    s.samplingmonth,
    s.samplingyear,
    s.samplestatusid,
    ss.samplestatus
   FROM (((((public.tblsample s
     LEFT JOIN public.tlkpsamplestatus ss ON ((s.samplestatusid = ss.samplestatusid)))
     LEFT JOIN public.tlkpdatecertainty dc ON ((s.datecertaintyid = dc.datecertaintyid)))
     LEFT JOIN public.tlkpsampletype st ON ((s.typeid = st.sampletypeid)))
     LEFT JOIN public.vwtblcurationeventmostrecent c ON ((s.sampleid = c.sampleid)))
     LEFT JOIN public.vwlabcodesforsamples lc ON ((s.sampleid = lc.sampleid)));
    DROP VIEW public.vwtblsample;
       public          pbrewer    false    330    360    360    240    240    362    362    240    240    240    240    240    390    240    240    240    390    390    398    240    240    240    240    398    398    240    240    330    240    240    240    240    240    2218            �           1259    65896    vwtbluserdefinedfieldandvalue    VIEW     �  CREATE VIEW public.vwtbluserdefinedfieldandvalue AS
 SELECT udfv.userdefinedfieldvalueid,
    udfv.value,
    udfv.entityid,
    udf.userdefinedfieldid,
    udf.fieldname,
    udf.description,
    udf.attachedto,
    udf.datatype,
    udf.longfieldname,
    udf.dictionarykey
   FROM public.tbluserdefinedfieldvalue udfv,
    public.tlkpuserdefinedfield udf
  WHERE (udfv.userdefinedfieldid = udf.userdefinedfieldid);
 0   DROP VIEW public.vwtbluserdefinedfieldandvalue;
       public          tellervo    false    371    307    307    307    307    371    371    371    371    371    371            �           1259    65900    vwtblvmeasurement    VIEW     |  CREATE VIEW public.vwtblvmeasurement AS
 SELECT dom.domainid,
    dom.domain,
    tblvmeasurement.vmeasurementid,
    tblvmeasurement.measurementid,
    tblvmeasurement.vmeasurementopid,
    tlkpvmeasurementop.name AS measurementoperator,
    tblvmeasurement.vmeasurementopparameter AS operatorparameter,
    tblvmeasurement.code AS measurementname,
    tblvmeasurement.comments AS measurementdescription,
    tblvmeasurement.ispublished AS measurementispublished,
    tblvmeasurement.owneruserid AS measurementowneruserid,
    tblvmeasurement.createdtimestamp AS measurementcreated,
    tblvmeasurement.lastmodifiedtimestamp AS measurementlastmodified
   FROM ((public.tblvmeasurement
     LEFT JOIN public.tlkpdomain dom ON ((tblvmeasurement.domainid = dom.domainid)))
     LEFT JOIN public.tlkpvmeasurementop ON ((tblvmeasurement.vmeasurementopid = tlkpvmeasurementop.vmeasurementopid)));
 $   DROP VIEW public.vwtblvmeasurement;
       public          tellervo    false    308    334    308    308    308    308    308    308    308    308    308    308    373    373    334            �           1259    65905    vwtblvmeasurementresult    VIEW       CREATE VIEW public.vwtblvmeasurementresult AS
 SELECT tblvmeasurementresult.vmeasurementresultid,
    tblvmeasurementresult.vmeasurementid,
    tblvmeasurementresult.isreconciled AS measurementisreconciled,
    tblvmeasurementresult.islegacycleaned,
    tblvmeasurementresult.datingerrorpositive,
    tblvmeasurementresult.datingerrornegative,
    tlkpdatingtype.datingtype
   FROM public.tblvmeasurementresult,
    public.tlkpdatingtype
  WHERE (tblvmeasurementresult.datingtypeid = tlkpdatingtype.datingtypeid);
 *   DROP VIEW public.vwtblvmeasurementresult;
       public          postgres    false    318    318    318    332    318    318    318    318    332            �           1259    65913    vwvmeasurement    VIEW       CREATE VIEW public.vwvmeasurement AS
 SELECT tblvmeasurementresult.vmeasurementid,
    tblvmeasurement.measurementid,
    tblvmeasurementresult.radiusid,
    tblvmeasurementresult.isreconciled,
    tblvmeasurementresult.startyear,
    tblvmeasurementresult.createdtimestamp AS measurementcreated,
    tblvmeasurementresult.lastmodifiedtimestamp AS measurementlastmodified
   FROM public.tblvmeasurementresult,
    public.tblvmeasurement
  WHERE (tblvmeasurementresult.vmeasurementid = tblvmeasurement.vmeasurementid);
 !   DROP VIEW public.vwvmeasurement;
       public          postgres    false    318    308    308    318    318    318    318    318            �           1259    65917    yenikapi    TABLE     �  CREATE TABLE public.yenikapi (
    gid integer NOT NULL,
    sitecode character varying(3),
    samplecode integer,
    "Dock" character varying(30),
    "Year" integer,
    "Top" double precision,
    "Length" double precision,
    "CADMissing" integer,
    "Notes" character varying(254),
    "Phase" character varying(10),
    "Species" character varying(20),
    the_geom public.geometry,
    CONSTRAINT enforce_dims_the_geom CHECK ((public.st_ndims(the_geom) = 2)),
    CONSTRAINT enforce_geotype_the_geom CHECK (((public.geometrytype(the_geom) = 'POINT'::text) OR (the_geom IS NULL))),
    CONSTRAINT enforce_srid_the_geom CHECK ((public.st_srid(the_geom) = 4326))
);
    DROP TABLE public.yenikapi;
       public         heap    postgres    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3            �           1259    65926    yenikapi_gid_seq    SEQUENCE     y   CREATE SEQUENCE public.yenikapi_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.yenikapi_gid_seq;
       public          postgres    false    410            v           0    0    yenikapi_gid_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE public.yenikapi_gid_seq OWNED BY public.yenikapi.gid;
          public          postgres    false    411            r           2604    65928    tblconfig configid    DEFAULT     x   ALTER TABLE ONLY public.tblconfig ALTER COLUMN configid SET DEFAULT nextval('public.tblconfig_configid_seq'::regclass);
 A   ALTER TABLE public.tblconfig ALTER COLUMN configid DROP DEFAULT;
       public          postgres    false    248    247            t           2604    65929    tblcrossdate crossdateid    DEFAULT     �   ALTER TABLE ONLY public.tblcrossdate ALTER COLUMN crossdateid SET DEFAULT nextval('public.tblcrossdate_crossdateid_seq'::regclass);
 G   ALTER TABLE public.tblcrossdate ALTER COLUMN crossdateid DROP DEFAULT;
       public          postgres    false    250    249            x           2604    65930     tblcurationevent curationeventid    DEFAULT     �   ALTER TABLE ONLY public.tblcurationevent ALTER COLUMN curationeventid SET DEFAULT nextval('public.tblcuration_curationid_seq'::regclass);
 O   ALTER TABLE public.tblcurationevent ALTER COLUMN curationeventid DROP DEFAULT;
       public          tellervo    false    252    251            z           2604    65931 $   tblcustomvocabterm customvocabtermid    DEFAULT     �   ALTER TABLE ONLY public.tblcustomvocabterm ALTER COLUMN customvocabtermid SET DEFAULT nextval('public.tblcustomvocabterm_customvocabtermid_seq'::regclass);
 S   ALTER TABLE public.tblcustomvocabterm ALTER COLUMN customvocabtermid DROP DEFAULT;
       public          postgres    false    256    255            O           2604    65932    tblelement gispkey    DEFAULT     x   ALTER TABLE ONLY public.tblelement ALTER COLUMN gispkey SET DEFAULT nextval('public.tblelement_gispkey_seq'::regclass);
 A   ALTER TABLE public.tblelement ALTER COLUMN gispkey DROP DEFAULT;
       public          postgres    false    258    236            {           2604    65933 (   tblenvironmentaldata environmentaldataid    DEFAULT     �   ALTER TABLE ONLY public.tblenvironmentaldata ALTER COLUMN environmentaldataid SET DEFAULT nextval('public.tblenvironmentaldata_environmentaldataid_seq'::regclass);
 W   ALTER TABLE public.tblenvironmentaldata ALTER COLUMN environmentaldataid DROP DEFAULT;
       public          postgres    false    260    259            E           2604    65934    tblmeasurement measurementid    DEFAULT     �   ALTER TABLE ONLY public.tblmeasurement ALTER COLUMN measurementid SET DEFAULT nextval('public.tblmeasurement_measurementid_seq'::regclass);
 K   ALTER TABLE public.tblmeasurement ALTER COLUMN measurementid DROP DEFAULT;
       public          postgres    false    263    234            ~           2604    65935    tblobjectregion objectregionid    DEFAULT     �   ALTER TABLE ONLY public.tblobjectregion ALTER COLUMN objectregionid SET DEFAULT nextval('public.tblobjectregion_objectregionid_seq'::regclass);
 M   ALTER TABLE public.tblobjectregion ALTER COLUMN objectregionid DROP DEFAULT;
       public          postgres    false    266    265            �           2604    65936 *   tblprojectprojecttype projectprojecttypeid    DEFAULT     �   ALTER TABLE ONLY public.tblprojectprojecttype ALTER COLUMN projectprojecttypeid SET DEFAULT nextval('public.tblprojectprojecttype_projectprojecttypeid_seq'::regclass);
 Y   ALTER TABLE public.tblprojectprojecttype ALTER COLUMN projectprojecttypeid DROP DEFAULT;
       public          tellervo    false    271    270            �           2604    65937    tblrasterlayer rasterlayerid    DEFAULT     �   ALTER TABLE ONLY public.tblrasterlayer ALTER COLUMN rasterlayerid SET DEFAULT nextval('public.tblrasterlayer_rasterlayerid_seq'::regclass);
 K   ALTER TABLE public.tblrasterlayer ALTER COLUMN rasterlayerid DROP DEFAULT;
       public          postgres    false    274    273            �           2604    65938    tblreading readingid    DEFAULT     |   ALTER TABLE ONLY public.tblreading ALTER COLUMN readingid SET DEFAULT nextval('public.tblreading_readingid_seq'::regclass);
 C   ALTER TABLE public.tblreading ALTER COLUMN readingid DROP DEFAULT;
       public          postgres    false    276    275            �           2604    65939 *   tblreadingreadingnote readingreadingnoteid    DEFAULT     �   ALTER TABLE ONLY public.tblreadingreadingnote ALTER COLUMN readingreadingnoteid SET DEFAULT nextval('public.tblreadingreadingnote_readingreadingnoteid_seq'::regclass);
 Y   ALTER TABLE public.tblreadingreadingnote ALTER COLUMN readingreadingnoteid DROP DEFAULT;
       public          postgres    false    278    277            �           2604    65940    tblredate redateid    DEFAULT     x   ALTER TABLE ONLY public.tblredate ALTER COLUMN redateid SET DEFAULT nextval('public.tblredate_redateid_seq'::regclass);
 A   ALTER TABLE public.tblredate ALTER COLUMN redateid DROP DEFAULT;
       public          postgres    false    280    279            �           2604    65941    tblrequestlog requestlogid    DEFAULT     �   ALTER TABLE ONLY public.tblrequestlog ALTER COLUMN requestlogid SET DEFAULT nextval('public.tblrequestlog_requestlogid_seq'::regclass);
 I   ALTER TABLE public.tblrequestlog ALTER COLUMN requestlogid DROP DEFAULT;
       public          postgres    false    284    283            �           2604    65942 $   tblsecuritydefault securitydefaultid    DEFAULT     �   ALTER TABLE ONLY public.tblsecuritydefault ALTER COLUMN securitydefaultid SET DEFAULT nextval('public.tblsecuritydefault_securitydefaultid_seq'::regclass);
 S   ALTER TABLE public.tblsecuritydefault ALTER COLUMN securitydefaultid DROP DEFAULT;
       public          postgres    false    287    286            �           2604    65943 $   tblsecurityelement securityelementid    DEFAULT     �   ALTER TABLE ONLY public.tblsecurityelement ALTER COLUMN securityelementid SET DEFAULT nextval('public.tblsecuritytree_securityelementid_seq'::regclass);
 S   ALTER TABLE public.tblsecurityelement ALTER COLUMN securityelementid DROP DEFAULT;
       public          postgres    false    294    288            W           2604    65944     tblsecuritygroup securitygroupid    DEFAULT     �   ALTER TABLE ONLY public.tblsecuritygroup ALTER COLUMN securitygroupid SET DEFAULT nextval('public.tblsecuritygroup_securitygroupid_seq'::regclass);
 O   ALTER TABLE public.tblsecuritygroup ALTER COLUMN securitygroupid DROP DEFAULT;
       public          postgres    false    289    237            �           2604    65945 4   tblsecuritygroupmembership securitygroupmembershipid    DEFAULT     �   ALTER TABLE ONLY public.tblsecuritygroupmembership ALTER COLUMN securitygroupmembershipid SET DEFAULT nextval('public.tblsecuritygroupmembership_securitygroupmembershipid_seq'::regclass);
 c   ALTER TABLE public.tblsecuritygroupmembership ALTER COLUMN securitygroupmembershipid DROP DEFAULT;
       public          postgres    false    291    290            �           2604    65946 "   tblsecurityobject securityobjectid    DEFAULT     �   ALTER TABLE ONLY public.tblsecurityobject ALTER COLUMN securityobjectid SET DEFAULT nextval('public.tblsecurityobject_securityobjectid_seq'::regclass);
 Q   ALTER TABLE public.tblsecurityobject ALTER COLUMN securityobjectid DROP DEFAULT;
       public          postgres    false    293    292            �           2604    65947 5   tblsecurityusermembership tblsecurityusermembershipid    DEFAULT     �   ALTER TABLE ONLY public.tblsecurityusermembership ALTER COLUMN tblsecurityusermembershipid SET DEFAULT nextval('public.tblsecurityusermembership_tblsecurityusermembershipid_seq'::regclass);
 d   ALTER TABLE public.tblsecurityusermembership ALTER COLUMN tblsecurityusermembershipid DROP DEFAULT;
       public          postgres    false    297    296            �           2604    65948 .   tblsecurityvmeasurement securityvmeasurementid    DEFAULT     �   ALTER TABLE ONLY public.tblsecurityvmeasurement ALTER COLUMN securityvmeasurementid SET DEFAULT nextval('public.tblsecurityvmeasurement_securityvmeasurementid_seq'::regclass);
 ]   ALTER TABLE public.tblsecurityvmeasurement ALTER COLUMN securityvmeasurementid DROP DEFAULT;
       public          postgres    false    299    298            �           2604    65949 "   tblsupportedclient supportclientid    DEFAULT     �   ALTER TABLE ONLY public.tblsupportedclient ALTER COLUMN supportclientid SET DEFAULT nextval('public.tblsupportedclients_supportclientid_seq'::regclass);
 Q   ALTER TABLE public.tblsupportedclient ALTER COLUMN supportclientid DROP DEFAULT;
       public          postgres    false    301    300            �           2604    65950    tbltruncate truncateid    DEFAULT     �   ALTER TABLE ONLY public.tbltruncate ALTER COLUMN truncateid SET DEFAULT nextval('public.tbltruncate_truncateid_seq'::regclass);
 E   ALTER TABLE public.tbltruncate ALTER COLUMN truncateid DROP DEFAULT;
       public          postgres    false    304    303            �           2604    65951    tblupgradelog upgradelogid    DEFAULT     �   ALTER TABLE ONLY public.tblupgradelog ALTER COLUMN upgradelogid SET DEFAULT nextval('public.tblupgradelog_upgradelogid_seq'::regclass);
 I   ALTER TABLE public.tblupgradelog ALTER COLUMN upgradelogid DROP DEFAULT;
       public          postgres    false    306    305            H           2604    65952 *   tblvmeasurementderivedcache derivedcacheid    DEFAULT     �   ALTER TABLE ONLY public.tblvmeasurementderivedcache ALTER COLUMN derivedcacheid SET DEFAULT nextval('public.tblvmeasurementderivedcache_derivedcacheid_seq'::regclass);
 Y   ALTER TABLE public.tblvmeasurementderivedcache ALTER COLUMN derivedcacheid DROP DEFAULT;
       public          postgres    false    310    235            �           2604    65953 (   tblvmeasurementgroup vmeasurementgroupid    DEFAULT     �   ALTER TABLE ONLY public.tblvmeasurementgroup ALTER COLUMN vmeasurementgroupid SET DEFAULT nextval('public.tblvmeasurementgroup_vmeasurementgroupid_seq'::regclass);
 W   ALTER TABLE public.tblvmeasurementgroup ALTER COLUMN vmeasurementgroupid DROP DEFAULT;
       public          postgres    false    312    311            6           2604    65954 0   tblvmeasurementmetacache vmeasurementmetacacheid    DEFAULT     �   ALTER TABLE ONLY public.tblvmeasurementmetacache ALTER COLUMN vmeasurementmetacacheid SET DEFAULT nextval('public.tblvmeasurementmetacache_vmeasurementmetacacheid_seq'::regclass);
 _   ALTER TABLE public.tblvmeasurementmetacache ALTER COLUMN vmeasurementmetacacheid DROP DEFAULT;
       public          postgres    false    313    233            k           2604    65955 8   tblvmeasurementreadingresult vmeasurementreadingresultid    DEFAULT     �   ALTER TABLE ONLY public.tblvmeasurementreadingresult ALTER COLUMN vmeasurementreadingresultid SET DEFAULT nextval('public.tblvmeasurementreadingresult_vmeasurementreadingresultid_seq'::regclass);
 g   ALTER TABLE public.tblvmeasurementreadingresult ALTER COLUMN vmeasurementreadingresultid DROP DEFAULT;
       public          postgres    false    315    241            �           2604    65956 (   tblvmeasurementtotag vmeasurementtotagid    DEFAULT     �   ALTER TABLE ONLY public.tblvmeasurementtotag ALTER COLUMN vmeasurementtotagid SET DEFAULT nextval('public.tblvmeasurementtotag_vmeasurementtotagid_seq'::regclass);
 W   ALTER TABLE public.tblvmeasurementtotag ALTER COLUMN vmeasurementtotagid DROP DEFAULT;
       public          tellervo    false    320    319            �           2604    65957 3   tlkpcomplexpresenceabsence complexpresenceabsenceid    DEFAULT     �   ALTER TABLE ONLY public.tlkpcomplexpresenceabsence ALTER COLUMN complexpresenceabsenceid SET DEFAULT nextval('public.tlkpcomplexpresenceabsence_complexpresenceabsenceid_seq'::regclass);
 b   ALTER TABLE public.tlkpcomplexpresenceabsence ALTER COLUMN complexpresenceabsenceid DROP DEFAULT;
       public          postgres    false    322    321            �           2604    65958 '   tlkpcoveragetemporal coveragetemporalid    DEFAULT     �   ALTER TABLE ONLY public.tlkpcoveragetemporal ALTER COLUMN coveragetemporalid SET DEFAULT nextval('public.tlkpcoveragetemporal_coveragetemporalid_seq'::regclass);
 V   ALTER TABLE public.tlkpcoveragetemporal ALTER COLUMN coveragetemporalid DROP DEFAULT;
       public          postgres    false    324    323            �           2604    65959 ;   tlkpcoveragetemporalfoundation coveragetemporalfoundationid    DEFAULT     �   ALTER TABLE ONLY public.tlkpcoveragetemporalfoundation ALTER COLUMN coveragetemporalfoundationid SET DEFAULT nextval('public.tlkpcoveragetemporalfoundation_coveragetemporalfoundationid_seq'::regclass);
 j   ALTER TABLE public.tlkpcoveragetemporalfoundation ALTER COLUMN coveragetemporalfoundationid DROP DEFAULT;
       public          postgres    false    326    325            �           2604    65960 #   tlkpcurationstatus curationstatusid    DEFAULT     �   ALTER TABLE ONLY public.tlkpcurationstatus ALTER COLUMN curationstatusid SET DEFAULT nextval('public.tlkpcurationstatus_curationstatusid_seq'::regclass);
 R   ALTER TABLE public.tlkpcurationstatus ALTER COLUMN curationstatusid DROP DEFAULT;
       public          tellervo    false    328    327            �           2604    65961 !   tlkpdatecertainty datecertaintyid    DEFAULT     �   ALTER TABLE ONLY public.tlkpdatecertainty ALTER COLUMN datecertaintyid SET DEFAULT nextval('public.tlkpdatecertainty_datecertaintyid_seq'::regclass);
 P   ALTER TABLE public.tlkpdatecertainty ALTER COLUMN datecertaintyid DROP DEFAULT;
       public          postgres    false    331    330            �           2604    65962    tlkpdatingtype datingtypeid    DEFAULT     �   ALTER TABLE ONLY public.tlkpdatingtype ALTER COLUMN datingtypeid SET DEFAULT nextval('public.tlkpdatingtype_datingtypeid_seq'::regclass);
 J   ALTER TABLE public.tlkpdatingtype ALTER COLUMN datingtypeid DROP DEFAULT;
       public          postgres    false    333    332            �           2604    65963    tlkpdomain domainid    DEFAULT     z   ALTER TABLE ONLY public.tlkpdomain ALTER COLUMN domainid SET DEFAULT nextval('public.tlkpdomain_domainid_seq'::regclass);
 B   ALTER TABLE public.tlkpdomain ALTER COLUMN domainid DROP DEFAULT;
       public          tellervo    false    335    334            �           2604    65964 -   tlkpelementauthenticity elementauthenticityid    DEFAULT     �   ALTER TABLE ONLY public.tlkpelementauthenticity ALTER COLUMN elementauthenticityid SET DEFAULT nextval('public.tlkpelementauthenticity_elementauthenticityid_seq'::regclass);
 \   ALTER TABLE public.tlkpelementauthenticity ALTER COLUMN elementauthenticityid DROP DEFAULT;
       public          postgres    false    337    336            �           2604    65965    tlkpelementshape elementshapeid    DEFAULT     �   ALTER TABLE ONLY public.tlkpelementshape ALTER COLUMN elementshapeid SET DEFAULT nextval('public.tlkpelementshape_elementshapeid_seq'::regclass);
 N   ALTER TABLE public.tlkpelementshape ALTER COLUMN elementshapeid DROP DEFAULT;
       public          postgres    false    339    338            �           2604    65966    tlkpelementtype elementtypeid    DEFAULT     �   ALTER TABLE ONLY public.tlkpelementtype ALTER COLUMN elementtypeid SET DEFAULT nextval('public.tlkpelementtype_elementtypeid_seq'::regclass);
 L   ALTER TABLE public.tlkpelementtype ALTER COLUMN elementtypeid DROP DEFAULT;
       public          postgres    false    341    340            �           2604    65967    tlkplocationtype locationtypeid    DEFAULT     �   ALTER TABLE ONLY public.tlkplocationtype ALTER COLUMN locationtypeid SET DEFAULT nextval('public.tlkplocationtype_locationtypeid_seq'::regclass);
 N   ALTER TABLE public.tlkplocationtype ALTER COLUMN locationtypeid DROP DEFAULT;
       public          postgres    false    345    344            �           2604    65968 -   tlkpmeasurementvariable measurementvariableid    DEFAULT     �   ALTER TABLE ONLY public.tlkpmeasurementvariable ALTER COLUMN measurementvariableid SET DEFAULT nextval('public.tlkpmeasurementvariable_measurementvariableid_seq'::regclass);
 \   ALTER TABLE public.tlkpmeasurementvariable ALTER COLUMN measurementvariableid DROP DEFAULT;
       public          postgres    false    347    346            �           2604    65969 %   tlkpmeasuringmethod measuringmethodid    DEFAULT     �   ALTER TABLE ONLY public.tlkpmeasuringmethod ALTER COLUMN measuringmethodid SET DEFAULT nextval('public.tlkpmeasuringmethod_measuringmethodid_seq'::regclass);
 T   ALTER TABLE public.tlkpmeasuringmethod ALTER COLUMN measuringmethodid DROP DEFAULT;
       public          postgres    false    349    348            �           2604    65970    tlkpobjecttype objecttypeid    DEFAULT     �   ALTER TABLE ONLY public.tlkpobjecttype ALTER COLUMN objecttypeid SET DEFAULT nextval('public.tlkpobjecttype_objecttype_seq'::regclass);
 J   ALTER TABLE public.tlkpobjecttype ALTER COLUMN objecttypeid DROP DEFAULT;
       public          postgres    false    351    350            �           2604    65971 %   tlkppresenceabsence presenceabsenceid    DEFAULT     �   ALTER TABLE ONLY public.tlkppresenceabsence ALTER COLUMN presenceabsenceid SET DEFAULT nextval('public.tlkppresenceabsence_presenceabsenceid_seq'::regclass);
 T   ALTER TABLE public.tlkppresenceabsence ALTER COLUMN presenceabsenceid DROP DEFAULT;
       public          postgres    false    353    352            3           2604    65972    tlkpreadingnote readingnoteid    DEFAULT     �   ALTER TABLE ONLY public.tlkpreadingnote ALTER COLUMN readingnoteid SET DEFAULT nextval('public.tlkpreadingnote_readingnoteid_seq'::regclass);
 L   ALTER TABLE public.tlkpreadingnote ALTER COLUMN readingnoteid DROP DEFAULT;
       public          postgres    false    359    232            �           2604    65973    tlkpsamplestatus samplestatusid    DEFAULT     �   ALTER TABLE ONLY public.tlkpsamplestatus ALTER COLUMN samplestatusid SET DEFAULT nextval('public.tlkpsamplestatus_samplestatusid_seq'::regclass);
 N   ALTER TABLE public.tlkpsamplestatus ALTER COLUMN samplestatusid DROP DEFAULT;
       public          tellervo    false    361    360            �           2604    65974    tlkpsampletype sampletypeid    DEFAULT     �   ALTER TABLE ONLY public.tlkpsampletype ALTER COLUMN sampletypeid SET DEFAULT nextval('public.tlkpsampletype_sampletypeid_seq'::regclass);
 J   ALTER TABLE public.tlkpsampletype ALTER COLUMN sampletypeid DROP DEFAULT;
       public          postgres    false    363    362            �           2604    65975 +   tlkpsecuritypermission securitypermissionid    DEFAULT     �   ALTER TABLE ONLY public.tlkpsecuritypermission ALTER COLUMN securitypermissionid SET DEFAULT nextval('public.tlkpsecuritypermission_securitypermissionid_seq'::regclass);
 Z   ALTER TABLE public.tlkpsecuritypermission ALTER COLUMN securitypermissionid DROP DEFAULT;
       public          postgres    false    365    364            y           2604    65976 '   tlkptrackinglocation trackinglocationid    DEFAULT     �   ALTER TABLE ONLY public.tlkptrackinglocation ALTER COLUMN trackinglocationid SET DEFAULT nextval('public.tblcurationlocation_curationlocationid_seq'::regclass);
 V   ALTER TABLE public.tlkptrackinglocation ALTER COLUMN trackinglocationid DROP DEFAULT;
       public          postgres    false    254    253            �           2604    65977    tlkpunit unitid    DEFAULT     t   ALTER TABLE ONLY public.tlkpunit ALTER COLUMN unitid SET DEFAULT nextval('public.tlkpunits_unitsid_seq'::regclass);
 >   ALTER TABLE public.tlkpunit ALTER COLUMN unitid DROP DEFAULT;
       public          postgres    false    370    369            �           2604    65978 #   tlkpvmeasurementop vmeasurementopid    DEFAULT     �   ALTER TABLE ONLY public.tlkpvmeasurementop ALTER COLUMN vmeasurementopid SET DEFAULT nextval('public.tlkpvmeasurementop_vmeasurementopid_seq'::regclass);
 R   ALTER TABLE public.tlkpvmeasurementop ALTER COLUMN vmeasurementopid DROP DEFAULT;
       public          postgres    false    374    373            �           2604    65979    tlkpvocabulary vocabularyid    DEFAULT     �   ALTER TABLE ONLY public.tlkpvocabulary ALTER COLUMN vocabularyid SET DEFAULT nextval('public.tlkpvocabulary_vocabularyid_seq'::regclass);
 J   ALTER TABLE public.tlkpvocabulary ALTER COLUMN vocabularyid DROP DEFAULT;
       public          postgres    false    376    375            �           2604    65980    tlkpwmsserver wmsserverid    DEFAULT     �   ALTER TABLE ONLY public.tlkpwmsserver ALTER COLUMN wmsserverid SET DEFAULT nextval('public.tlkpwmsserver_wmsserverid_seq'::regclass);
 H   ALTER TABLE public.tlkpwmsserver ALTER COLUMN wmsserverid DROP DEFAULT;
       public          postgres    false    378    377            �           2604    65981    yenikapi gid    DEFAULT     l   ALTER TABLE ONLY public.yenikapi ALTER COLUMN gid SET DEFAULT nextval('public.yenikapi_gid_seq'::regclass);
 ;   ALTER TABLE public.yenikapi ALTER COLUMN gid DROP DEFAULT;
       public          postgres    false    411    410            S          0    65208    elementimporterror 
   TABLE DATA           �   COPY public.elementimporterror (id, boxname, regioncode, elementstartnum, elementendnum, samplecode, taxa, sitecode, missing, notes, sampletype, currentelement, error, fixed) FROM stdin;
    public          postgres    false    242   F�      T          0    65215 	   inventory 
   TABLE DATA           �   COPY public.inventory (id, boxname, regioncode, elementstartnum, elementendnum, samplecode, taxa, sitecode, missing, notes, sampletype) FROM stdin;
    public          postgres    false    243   c�      U          0    65221    sampleimporterror 
   TABLE DATA           �   COPY public.sampleimporterror (id, boxname, regioncode, elementstartnum, elementendnum, samplecode, taxa, sitecode, missing, notes, sampletype, currentelement, error, fixed) FROM stdin;
    public          postgres    false    244   ��      V          0    65228    sampleimportfixerror 
   TABLE DATA           �   COPY public.sampleimportfixerror (id, boxname, regioncode, elementstartnum, elementendnum, samplecode, taxa, sitecode, missing, notes, sampletype, currentelement, error, fixed) FROM stdin;
    public          postgres    false    245   ��      &          0    63733    spatial_ref_sys 
   TABLE DATA           X   COPY public.spatial_ref_sys (srid, auth_name, auth_srid, srtext, proj4text) FROM stdin;
    public          postgres    false    215   ��      �          0    66939    staticvwipt 
   TABLE DATA           l  COPY public.staticvwipt (projectid, ptitle, pcreatedtimestamp, plastmodifiedtimestamp, pcomments, pdescription, pfile, projectcategory, recordedby, pperiod, projectreference, o1objectid, o1title, o1code, elementid, ecode, acceptednameusageid, decimallongitude, decimallatitude, coordinateuncertaintyinmeters, locationremarks, country, stateprovince, verbatimelevation, kingdom, phylum, class, "order", family, subfamily, genus, subgenus, infragenericepithet, species, infraspecificepithet, cultivarepithet, scientificname, parentnameusageid, taxonrank, taxonomicstatus, nomenclaturalcode, occurrenceid, recordnumber, identifiedby, day, month, year, eventdate, type, modified, language, rightsholder, institutionid, institutioncode, datasetname, basisofrecord, individualcount, organismquantity, organismquantitytype, occurrencestatus, preparations, geodeticdatum) FROM stdin;
    public          tellervo    false    429   ז      �          0    66914    stblflattenedtaxonomy 
   TABLE DATA             COPY public.stblflattenedtaxonomy (taxonid, taxonrank, kingdom, subkingdom, phylum, division, class, txorder, family, subfamily, genus, subgenus, section, subsection, species, specificepithet, subspecies, race, variety, subvariety, form, subform) FROM stdin;
    public          tellervo    false    425   o�      W          0    65235    tblbox 
   TABLE DATA           �   COPY public.tblbox (boxid, title, curationlocation, trackinglocation, createdtimestamp, lastmodifiedtimestamp, comments) FROM stdin;
    public          postgres    false    246   o�      X          0    65244 	   tblconfig 
   TABLE DATA           F   COPY public.tblconfig (configid, key, value, description) FROM stdin;
    public          postgres    false    247   ��      Z          0    65252    tblcrossdate 
   TABLE DATA              COPY public.tblcrossdate (crossdateid, vmeasurementid, mastervmeasurementid, startyear, justification, confidence) FROM stdin;
    public          postgres    false    249   >�      \          0    65263    tblcurationevent 
   TABLE DATA           �   COPY public.tblcurationevent (curationeventid, curationstatusid, sampleid, createdtimestamp, loanid, notes, storagelocation, curatorid, boxid) FROM stdin;
    public          tellervo    false    251   [�      `          0    65280    tblcustomvocabterm 
   TABLE DATA           t   COPY public.tblcustomvocabterm (customvocabtermid, "table", field, id, customvocabterm, dictionaryname) FROM stdin;
    public          postgres    false    255   ��      M          0    64638 
   tblelement 
   TABLE DATA           �  COPY public.tblelement (elementid, locationprecision, code, createdtimestamp, lastmodifiedtimestamp, locationgeometry, islivetree, originaltaxonname, locationtypeid, locationcomment, file, description, processing, marks, diameter, width, height, depth, unsupportedxml, unitsold, objectid, elementtypeid, elementauthenticityid, elementshapeid, altitudeint, slopeangle, slopeazimuth, soildescription, soildepth, bedrockdescription, comments, locationaddressline2, locationcityortown, locationstateprovinceregion, locationpostalcode, locationcountry, locationaddressline1, altitude, gispkey, units, authenticity, domainid, taxonid) FROM stdin;
    public          postgres    false    236   �      d          0    65292    tblenvironmentaldata 
   TABLE DATA           d   COPY public.tblenvironmentaldata (environmentaldataid, elementid, rasterlayerid, value) FROM stdin;
    public          postgres    false    259   ��      f          0    65297    tbliptracking 
   TABLE DATA           L   COPY public.tbliptracking (ipaddr, "timestamp", securityuserid) FROM stdin;
    public          postgres    false    261   ��      g          0    65304    tbllaboratory 
   TABLE DATA           z   COPY public.tbllaboratory (laboratoryid, name, acronym, address1, address2, city, state, postalcode, country) FROM stdin;
    public          tellervo    false    262   �      O          0    64667    tblloan 
   TABLE DATA           z   COPY public.tblloan (loanid, firstname, lastname, organisation, duedate, issuedate, returndate, files, notes) FROM stdin;
    public          tellervo    false    238   :�      K          0    64605    tblmeasurement 
   TABLE DATA           @  COPY public.tblmeasurement (measurementid, radiusid, isreconciled, startyear, islegacycleaned, importtablename, createdtimestamp, lastmodifiedtimestamp, datingtypeid, datingerrorpositive, datingerrornegative, measurementvariableid, unitid, power, provenance, measuringmethodid, measuredbyid, supervisedbyid) FROM stdin;
    public          postgres    false    234   ��      H          0    64555 	   tblobject 
   TABLE DATA           �  COPY public.tblobject (objectid, title, code, createdtimestamp, lastmodifiedtimestamp, locationgeometry, locationtypeid, locationprecision, locationcomment, file, creator, owner, parentobjectid, description, objecttypeid, coveragetemporalid, coveragetemporalfoundationid, comments, coveragetemporal, coveragetemporalfoundation, locationaddressline1, locationaddressline2, locationcityortown, locationstateprovinceregion, locationpostalcode, locationcountry, vegetationtype, domainid, projectid) FROM stdin;
    public          postgres    false    231   ��      j          0    65315    tblobjectregion 
   TABLE DATA           M   COPY public.tblobjectregion (objectregionid, objectid, regionid) FROM stdin;
    public          postgres    false    265   )�      l          0    65320    tblodkdefinition 
   TABLE DATA           {   COPY public.tblodkdefinition (odkdefinitionid, createdtimestamp, name, definition, ownerid, ispublic, version) FROM stdin;
    public          tellervo    false    267   F�      m          0    65330    tblodkinstance 
   TABLE DATA           s   COPY public.tblodkinstance (odkinstanceid, createdtimestamp, ownerid, name, instance, files, deviceid) FROM stdin;
    public          tellervo    false    268   c�      n          0    65338 
   tblproject 
   TABLE DATA           �   COPY public.tblproject (projectid, domainid, title, createdtimestamp, lastmodifiedtimestamp, comments, description, file, projectcategoryid, investigator, period, requestdate, commissioner, reference, research, projecttypes, laboratories) FROM stdin;
    public          tellervo    false    269   ��      o          0    65348    tblprojectprojecttype 
   TABLE DATA           _   COPY public.tblprojectprojecttype (projectprojecttypeid, projectid, projecttypeid) FROM stdin;
    public          tellervo    false    270   ��      P          0    64677 	   tblradius 
   TABLE DATA           �  COPY public.tblradius (radiusid, sampleid, code, createdtimestamp, lastmodifiedtimestamp, numberofsapwoodrings, pithid, barkpresent, lastringunderbark, missingheartwoodringstopith, missingheartwoodringstopithfoundation, missingsapwoodringstobark, missingsapwoodringstobarkfoundation, sapwoodid, heartwoodid, azimuth, comments, lastringunderbarkpresent, nrofunmeasuredinnerrings, nrofunmeasuredouterrings, lrubpa, domainid) FROM stdin;
    public          postgres    false    239   �      r          0    65355    tblrasterlayer 
   TABLE DATA           c   COPY public.tblrasterlayer (rasterlayerid, name, filename, issystemlayer, description) FROM stdin;
    public          postgres    false    273   ��      t          0    65364 
   tblreading 
   TABLE DATA           b   COPY public.tblreading (readingid, measurementid, relyear, reading, ewwidth, lwwidth) FROM stdin;
    public          postgres    false    275   ��      v          0    65369    tblreadingreadingnote 
   TABLE DATA           �   COPY public.tblreadingreadingnote (readingid, readingnoteid, createdtimestamp, lastmodifiedtimestamp, readingreadingnoteid) FROM stdin;
    public          postgres    false    277   &�      x          0    65376 	   tblredate 
   TABLE DATA           g   COPY public.tblredate (redateid, vmeasurementid, startyear, redatingtypeid, justification) FROM stdin;
    public          postgres    false    279   C�      z          0    65384 	   tblregion 
   TABLE DATA           �   COPY public.tblregion (regionid, fips_cntry, gmi_cntry, regionname, sovereign, pop_cntry, sqkm_cntry, sqmi_cntry, curr_type, curr_code, landlocked, color_map, the_geom) FROM stdin;
    public          postgres    false    281   `�      |          0    65394    tblrequestlog 
   TABLE DATA           �   COPY public.tblrequestlog (requestlogid, request, ipaddr, createdtimestamp, wsversion, page, client, securityuserid) FROM stdin;
    public          postgres    false    283   }�      Q          0    64692 	   tblsample 
   TABLE DATA           �  COPY public.tblsample (sampleid, code, elementid, samplingdate, createdtimestamp, lastmodifiedtimestamp, type, identifierdomain, file, "position", state, knots, description, datecertaintyid, typeid, boxid, comments, externalid, domainid, samplestatusid, samplingyear, samplingmonth, samplingdateprec, userdefinedfielddata, dendrochronologist, startyear, endyear, haspith, hasbark, eventyears, measuredby) FROM stdin;
    public          postgres    false    240   >�                0    65405    tblsecuritydefault 
   TABLE DATA           f   COPY public.tblsecuritydefault (securitydefaultid, securitygroupid, securitypermissionid) FROM stdin;
    public          postgres    false    286   ��      �          0    65410    tblsecurityelement 
   TABLE DATA           q   COPY public.tblsecurityelement (elementid, securitygroupid, securitypermissionid, securityelementid) FROM stdin;
    public          postgres    false    288   @�      N          0    64657    tblsecuritygroup 
   TABLE DATA           X   COPY public.tblsecuritygroup (securitygroupid, name, description, isactive) FROM stdin;
    public          postgres    false    237   ]�      �          0    65415    tblsecuritygroupmembership 
   TABLE DATA           |   COPY public.tblsecuritygroupmembership (securitygroupmembershipid, parentsecuritygroupid, childsecuritygroupid) FROM stdin;
    public          postgres    false    290   ��      �          0    65420    tblsecurityobject 
   TABLE DATA           n   COPY public.tblsecurityobject (securityobjectid, objectid, securitygroupid, securitypermissionid) FROM stdin;
    public          postgres    false    292   ��      �          0    65427    tblsecurityuser 
   TABLE DATA           y   COPY public.tblsecurityuser (username, password, firstname, lastname, isactive, securityuserid, odkpassword) FROM stdin;
    public          postgres    false    295          �          0    65435    tblsecurityusermembership 
   TABLE DATA           q   COPY public.tblsecurityusermembership (tblsecurityusermembershipid, securitygroupid, securityuserid) FROM stdin;
    public          postgres    false    296   �       �          0    65440    tblsecurityvmeasurement 
   TABLE DATA           �   COPY public.tblsecurityvmeasurement (securityvmeasurementid, vmeasurementid, securitygroupid, securitypermissionid) FROM stdin;
    public          postgres    false    298   �       �          0    65445    tblsupportedclient 
   TABLE DATA           Q   COPY public.tblsupportedclient (supportclientid, client, minversion) FROM stdin;
    public          postgres    false    300   �       �          0    65453    tbltag 
   TABLE DATA           =   COPY public.tbltag (tagid, tag, ownerid, global) FROM stdin;
    public          tellervo    false    302   <      �          0    65461    tbltruncate 
   TABLE DATA           j   COPY public.tbltruncate (truncateid, vmeasurementid, startrelyear, endrelyear, justification) FROM stdin;
    public          postgres    false    303   Y      �          0    65469    tblupgradelog 
   TABLE DATA           L   COPY public.tblupgradelog (upgradelogid, filename, "timestamp") FROM stdin;
    public          postgres    false    305   v      �          0    65478    tbluserdefinedfieldvalue 
   TABLE DATA           p   COPY public.tbluserdefinedfieldvalue (userdefinedfieldvalueid, userdefinedfieldid, value, entityid) FROM stdin;
    public          tellervo    false    307   �      �          0    65485    tblvmeasurement 
   TABLE DATA           �   COPY public.tblvmeasurement (vmeasurementid, measurementid, vmeasurementopid, vmeasurementopparameter, code, comments, ispublished, createdtimestamp, lastmodifiedtimestamp, isgenerating, objective, version, birthdate, owneruserid, domainid) FROM stdin;
    public          postgres    false    308   �      L          0    64634    tblvmeasurementderivedcache 
   TABLE DATA           d   COPY public.tblvmeasurementderivedcache (derivedcacheid, vmeasurementid, measurementid) FROM stdin;
    public          postgres    false    235   �      �          0    65502    tblvmeasurementgroup 
   TABLE DATA           i   COPY public.tblvmeasurementgroup (vmeasurementid, membervmeasurementid, vmeasurementgroupid) FROM stdin;
    public          postgres    false    311   >      J          0    64582    tblvmeasurementmetacache 
   TABLE DATA           �   COPY public.tblvmeasurementmetacache (vmeasurementid, startyear, readingcount, measurementcount, vmextent, vmeasurementmetacacheid, objectcode, objectcount, commontaxonname, taxoncount, prefix, datingtypeid) FROM stdin;
    public          postgres    false    233   [      �          0    65509     tblvmeasurementreadingnoteresult 
   TABLE DATA           x   COPY public.tblvmeasurementreadingnoteresult (vmeasurementresultid, relyear, readingnoteid, inheritedcount) FROM stdin;
    public          postgres    false    314   �      R          0    64716    tblvmeasurementreadingresult 
   TABLE DATA           �   COPY public.tblvmeasurementreadingresult (vmeasurementreadingresultid, vmeasurementresultid, relyear, reading, wjinc, wjdec, count, readingid, ewwidth, lwwidth) FROM stdin;
    public          postgres    false    241   �      �          0    65517 !   tblvmeasurementrelyearreadingnote 
   TABLE DATA           �   COPY public.tblvmeasurementrelyearreadingnote (vmeasurementid, relyear, readingnoteid, disabledoverride, createdtimestamp, lastmodifiedtimestamp, relyearreadingnoteid) FROM stdin;
    public          postgres    false    317   �      �          0    65524    tblvmeasurementresult 
   TABLE DATA           K  COPY public.tblvmeasurementresult (vmeasurementresultid, vmeasurementid, radiusid, isreconciled, startyear, islegacycleaned, createdtimestamp, lastmodifiedtimestamp, vmeasurementresultmasterid, owneruserid, vmeasurementresultgroupid, datingtypeid, datingerrorpositive, datingerrornegative, code, comments, ispublished) FROM stdin;
    public          postgres    false    318   �      �          0    65534    tblvmeasurementtotag 
   TABLE DATA           Z   COPY public.tblvmeasurementtotag (vmeasurementtotagid, tagid, vmeasurementid) FROM stdin;
    public          tellervo    false    319   �      �          0    65539    tlkpcomplexpresenceabsence 
   TABLE DATA           f   COPY public.tlkpcomplexpresenceabsence (complexpresenceabsenceid, complexpresenceabsence) FROM stdin;
    public          postgres    false    321   �      �          0    65547    tlkpcoveragetemporal 
   TABLE DATA           T   COPY public.tlkpcoveragetemporal (coveragetemporalid, coveragetemporal) FROM stdin;
    public          postgres    false    323   ?      �          0    65555    tlkpcoveragetemporalfoundation 
   TABLE DATA           r   COPY public.tlkpcoveragetemporalfoundation (coveragetemporalfoundationid, coveragetemporalfoundation) FROM stdin;
    public          postgres    false    325   �      �          0    65563    tlkpcurationstatus 
   TABLE DATA           N   COPY public.tlkpcurationstatus (curationstatusid, curationstatus) FROM stdin;
    public          tellervo    false    327   )      �          0    65571    tlkpdatatype 
   TABLE DATA           0   COPY public.tlkpdatatype (datatype) FROM stdin;
    public          tellervo    false    329   �      �          0    65577    tlkpdatecertainty 
   TABLE DATA           K   COPY public.tlkpdatecertainty (datecertaintyid, datecertainty) FROM stdin;
    public          postgres    false    330   �      �          0    65585    tlkpdatingtype 
   TABLE DATA           O   COPY public.tlkpdatingtype (datingtypeid, datingtype, datingclass) FROM stdin;
    public          postgres    false    332   >      �          0    65593 
   tlkpdomain 
   TABLE DATA           >   COPY public.tlkpdomain (domainid, domain, prefix) FROM stdin;
    public          tellervo    false    334   �      �          0    65601    tlkpelementauthenticity 
   TABLE DATA           ]   COPY public.tlkpelementauthenticity (elementauthenticityid, elementauthenticity) FROM stdin;
    public          postgres    false    336   �      �          0    65609    tlkpelementshape 
   TABLE DATA           H   COPY public.tlkpelementshape (elementshapeid, elementshape) FROM stdin;
    public          postgres    false    338   �      �          0    65617    tlkpelementtype 
   TABLE DATA           S   COPY public.tlkpelementtype (elementtypeid, elementtype, vocabularyid) FROM stdin;
    public          postgres    false    340         �          0    65625    tlkpindextype 
   TABLE DATA           ;   COPY public.tlkpindextype (indexid, indexname) FROM stdin;
    public          postgres    false    342   �0      �          0    65630    tlkplocationtype 
   TABLE DATA           H   COPY public.tlkplocationtype (locationtypeid, locationtype) FROM stdin;
    public          postgres    false    344   31      �          0    65638    tlkpmeasurementvariable 
   TABLE DATA           ]   COPY public.tlkpmeasurementvariable (measurementvariableid, measurementvariable) FROM stdin;
    public          postgres    false    346   �1      �          0    65646    tlkpmeasuringmethod 
   TABLE DATA           Q   COPY public.tlkpmeasuringmethod (measuringmethodid, measuringmethod) FROM stdin;
    public          postgres    false    348   2      �          0    65654    tlkpobjecttype 
   TABLE DATA           P   COPY public.tlkpobjecttype (objecttype, vocabularyid, objecttypeid) FROM stdin;
    public          postgres    false    350   |2      �          0    65662    tlkppresenceabsence 
   TABLE DATA           Q   COPY public.tlkppresenceabsence (presenceabsenceid, presenceabsence) FROM stdin;
    public          postgres    false    352   �L      �          0    65672    tlkpprojectcategory 
   TABLE DATA           _   COPY public.tlkpprojectcategory (projectcategory, vocabularyid, projectcategoryid) FROM stdin;
    public          tellervo    false    355   5M      �          0    65681    tlkpprojecttype 
   TABLE DATA           S   COPY public.tlkpprojecttype (projecttype, vocabularyid, projecttypeid) FROM stdin;
    public          tellervo    false    357   �M      I          0    64570    tlkpreadingnote 
   TABLE DATA           �   COPY public.tlkpreadingnote (readingnoteid, note, vocabularyid, standardisedid, parentreadingid, parentvmrelyearreadingnoteid) FROM stdin;
    public          postgres    false    232   �N      �          0    65692    tlkpsamplestatus 
   TABLE DATA           H   COPY public.tlkpsamplestatus (samplestatusid, samplestatus) FROM stdin;
    public          tellervo    false    360   TP      �          0    65700    tlkpsampletype 
   TABLE DATA           B   COPY public.tlkpsampletype (sampletypeid, sampletype) FROM stdin;
    public          postgres    false    362   �P      �          0    65705    tlkpsecuritypermission 
   TABLE DATA           L   COPY public.tlkpsecuritypermission (securitypermissionid, name) FROM stdin;
    public          postgres    false    364   DQ      �          0    65712 	   tlkptaxon 
   TABLE DATA           n   COPY public.tlkptaxon (colid, colparentid, taxonrankid, label, htmllabel, taxonid, parenttaxonid) FROM stdin;
    public          postgres    false    367   �Q      �          0    65716    tlkptaxonrank 
   TABLE DATA           J   COPY public.tlkptaxonrank (taxonrankid, taxonrank, rankorder) FROM stdin;
    public          postgres    false    368   0�      ^          0    65272    tlkptrackinglocation 
   TABLE DATA           L   COPY public.tlkptrackinglocation (trackinglocationid, location) FROM stdin;
    public          postgres    false    253   �      �          0    65720    tlkpunit 
   TABLE DATA           0   COPY public.tlkpunit (unitid, unit) FROM stdin;
    public          postgres    false    369   �      �          0    65728    tlkpuserdefinedfield 
   TABLE DATA           �   COPY public.tlkpuserdefinedfield (userdefinedfieldid, fieldname, description, attachedto, datatype, longfieldname, dictionarykey) FROM stdin;
    public          tellervo    false    371   \�      �          0    65737    tlkpuserdefinedterm 
   TABLE DATA           U   COPY public.tlkpuserdefinedterm (userdefinedtermid, term, dictionarykey) FROM stdin;
    public          tellervo    false    372   y�      �          0    65744    tlkpvmeasurementop 
   TABLE DATA           P   COPY public.tlkpvmeasurementop (vmeasurementopid, name, legacycode) FROM stdin;
    public          postgres    false    373   ��      �          0    65749    tlkpvocabulary 
   TABLE DATA           A   COPY public.tlkpvocabulary (vocabularyid, name, url) FROM stdin;
    public          postgres    false    375   �      �          0    65757    tlkpwmsserver 
   TABLE DATA           ?   COPY public.tlkpwmsserver (wmsserverid, name, url) FROM stdin;
    public          postgres    false    377   ��      �          0    65917    yenikapi 
   TABLE DATA           �   COPY public.yenikapi (gid, sitecode, samplecode, "Dock", "Year", "Top", "Length", "CADMissing", "Notes", "Phase", "Species", the_geom) FROM stdin;
    public          postgres    false    410   դ      !          0    63339    jar_repository 
   TABLE DATA           X   COPY sqlj.jar_repository (jarid, jarname, jarorigin, jarowner, jarmanifest) FROM stdin;
    sqlj          postgres    false    207   �      $          0    63383    classpath_entry 
   TABLE DATA           C   COPY sqlj.classpath_entry (schemaname, ordinal, jarid) FROM stdin;
    sqlj          postgres    false    211   ��      "          0    63352 	   jar_entry 
   TABLE DATA           H   COPY sqlj.jar_entry (entryid, entryname, jarid, entryimage) FROM stdin;
    sqlj          postgres    false    209   ��      #          0    63368    jar_descriptor 
   TABLE DATA           ?   COPY sqlj.jar_descriptor (jarid, ordinal, entryid) FROM stdin;
    sqlj          postgres    false    210   80      %          0    63395    typemap_entry 
   TABLE DATA           ?   COPY sqlj.typemap_entry (mapid, javaname, sqlname) FROM stdin;
    sqlj          postgres    false    213   U0      w           0    0    tblconfig_configid_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.tblconfig_configid_seq', 52, true);
          public          postgres    false    248            x           0    0    tblcrossdate_crossdateid_seq    SEQUENCE SET     K   SELECT pg_catalog.setval('public.tblcrossdate_crossdateid_seq', 1, false);
          public          postgres    false    250            y           0    0    tblcuration_curationid_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('public.tblcuration_curationid_seq', 1, true);
          public          tellervo    false    252            z           0    0 *   tblcurationlocation_curationlocationid_seq    SEQUENCE SET     Y   SELECT pg_catalog.setval('public.tblcurationlocation_curationlocationid_seq', 1, false);
          public          postgres    false    254            {           0    0 (   tblcustomvocabterm_customvocabtermid_seq    SEQUENCE SET     W   SELECT pg_catalog.setval('public.tblcustomvocabterm_customvocabtermid_seq', 1, false);
          public          postgres    false    256            |           0    0    tblelement_elementid_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.tblelement_elementid_seq', 1, false);
          public          postgres    false    257            }           0    0    tblelement_gispkey_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.tblelement_gispkey_seq', 1, true);
          public          postgres    false    258            ~           0    0 ,   tblenvironmentaldata_environmentaldataid_seq    SEQUENCE SET     [   SELECT pg_catalog.setval('public.tblenvironmentaldata_environmentaldataid_seq', 1, false);
          public          postgres    false    260                       0    0     tblmeasurement_measurementid_seq    SEQUENCE SET     N   SELECT pg_catalog.setval('public.tblmeasurement_measurementid_seq', 5, true);
          public          postgres    false    263            �           0    0    tblobject_objectid_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.tblobject_objectid_seq', 1, false);
          public          postgres    false    264            �           0    0 "   tblobjectregion_objectregionid_seq    SEQUENCE SET     Q   SELECT pg_catalog.setval('public.tblobjectregion_objectregionid_seq', 1, false);
          public          postgres    false    266            �           0    0 .   tblprojectprojecttype_projectprojecttypeid_seq    SEQUENCE SET     ]   SELECT pg_catalog.setval('public.tblprojectprojecttype_projectprojecttypeid_seq', 1, false);
          public          tellervo    false    271            �           0    0    tblradius_radiusid_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.tblradius_radiusid_seq', 1, false);
          public          postgres    false    272            �           0    0     tblrasterlayer_rasterlayerid_seq    SEQUENCE SET     O   SELECT pg_catalog.setval('public.tblrasterlayer_rasterlayerid_seq', 1, false);
          public          postgres    false    274            �           0    0    tblreading_readingid_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.tblreading_readingid_seq', 85, true);
          public          postgres    false    276            �           0    0 .   tblreadingreadingnote_readingreadingnoteid_seq    SEQUENCE SET     ]   SELECT pg_catalog.setval('public.tblreadingreadingnote_readingreadingnoteid_seq', 1, false);
          public          postgres    false    278            �           0    0    tblredate_redateid_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.tblredate_redateid_seq', 1, false);
          public          postgres    false    280            �           0    0    tblregion_regionid_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.tblregion_regionid_seq', 1, false);
          public          postgres    false    282            �           0    0    tblrequestlog_requestlogid_seq    SEQUENCE SET     N   SELECT pg_catalog.setval('public.tblrequestlog_requestlogid_seq', 145, true);
          public          postgres    false    284            �           0    0    tblsample_sampleid_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.tblsample_sampleid_seq', 1, false);
          public          postgres    false    285            �           0    0 (   tblsecuritydefault_securitydefaultid_seq    SEQUENCE SET     W   SELECT pg_catalog.setval('public.tblsecuritydefault_securitydefaultid_seq', 17, true);
          public          postgres    false    287            �           0    0 $   tblsecuritygroup_securitygroupid_seq    SEQUENCE SET     R   SELECT pg_catalog.setval('public.tblsecuritygroup_securitygroupid_seq', 6, true);
          public          postgres    false    289            �           0    0 8   tblsecuritygroupmembership_securitygroupmembershipid_seq    SEQUENCE SET     g   SELECT pg_catalog.setval('public.tblsecuritygroupmembership_securitygroupmembershipid_seq', 1, false);
          public          postgres    false    291            �           0    0 &   tblsecurityobject_securityobjectid_seq    SEQUENCE SET     U   SELECT pg_catalog.setval('public.tblsecurityobject_securityobjectid_seq', 1, false);
          public          postgres    false    293            �           0    0 %   tblsecuritytree_securityelementid_seq    SEQUENCE SET     T   SELECT pg_catalog.setval('public.tblsecuritytree_securityelementid_seq', 1, false);
          public          postgres    false    294            �           0    0 9   tblsecurityusermembership_tblsecurityusermembershipid_seq    SEQUENCE SET     g   SELECT pg_catalog.setval('public.tblsecurityusermembership_tblsecurityusermembershipid_seq', 1, true);
          public          postgres    false    297            �           0    0 2   tblsecurityvmeasurement_securityvmeasurementid_seq    SEQUENCE SET     a   SELECT pg_catalog.setval('public.tblsecurityvmeasurement_securityvmeasurementid_seq', 1, false);
          public          postgres    false    299            �           0    0 '   tblsupportedclients_supportclientid_seq    SEQUENCE SET     U   SELECT pg_catalog.setval('public.tblsupportedclients_supportclientid_seq', 6, true);
          public          postgres    false    301            �           0    0    tbltruncate_truncateid_seq    SEQUENCE SET     I   SELECT pg_catalog.setval('public.tbltruncate_truncateid_seq', 1, false);
          public          postgres    false    304            �           0    0    tblupgradelog_upgradelogid_seq    SEQUENCE SET     M   SELECT pg_catalog.setval('public.tblupgradelog_upgradelogid_seq', 55, true);
          public          postgres    false    306            �           0    0 "   tblvmeasurement_vmeasurementid_seq    SEQUENCE SET     Q   SELECT pg_catalog.setval('public.tblvmeasurement_vmeasurementid_seq', 1, false);
          public          postgres    false    309            �           0    0 .   tblvmeasurementderivedcache_derivedcacheid_seq    SEQUENCE SET     \   SELECT pg_catalog.setval('public.tblvmeasurementderivedcache_derivedcacheid_seq', 2, true);
          public          postgres    false    310            �           0    0 ,   tblvmeasurementgroup_vmeasurementgroupid_seq    SEQUENCE SET     [   SELECT pg_catalog.setval('public.tblvmeasurementgroup_vmeasurementgroupid_seq', 1, false);
          public          postgres    false    312            �           0    0 4   tblvmeasurementmetacache_vmeasurementmetacacheid_seq    SEQUENCE SET     b   SELECT pg_catalog.setval('public.tblvmeasurementmetacache_vmeasurementmetacacheid_seq', 2, true);
          public          postgres    false    313            �           0    0 <   tblvmeasurementreadingresult_vmeasurementreadingresultid_seq    SEQUENCE SET     l   SELECT pg_catalog.setval('public.tblvmeasurementreadingresult_vmeasurementreadingresultid_seq', 323, true);
          public          postgres    false    315            �           0    0 :   tblvmeasurementrelyearreadingnote_relyearreadingnoteid_seq    SEQUENCE SET     i   SELECT pg_catalog.setval('public.tblvmeasurementrelyearreadingnote_relyearreadingnoteid_seq', 1, false);
          public          postgres    false    316            �           0    0 ,   tblvmeasurementtotag_vmeasurementtotagid_seq    SEQUENCE SET     [   SELECT pg_catalog.setval('public.tblvmeasurementtotag_vmeasurementtotagid_seq', 1, false);
          public          tellervo    false    320            �           0    0 7   tlkpcomplexpresenceabsence_complexpresenceabsenceid_seq    SEQUENCE SET     e   SELECT pg_catalog.setval('public.tlkpcomplexpresenceabsence_complexpresenceabsenceid_seq', 5, true);
          public          postgres    false    322            �           0    0 +   tlkpcoveragetemporal_coveragetemporalid_seq    SEQUENCE SET     Y   SELECT pg_catalog.setval('public.tlkpcoveragetemporal_coveragetemporalid_seq', 7, true);
          public          postgres    false    324            �           0    0 ?   tlkpcoveragetemporalfoundation_coveragetemporalfoundationid_seq    SEQUENCE SET     m   SELECT pg_catalog.setval('public.tlkpcoveragetemporalfoundation_coveragetemporalfoundationid_seq', 6, true);
          public          postgres    false    326            �           0    0 '   tlkpcurationstatus_curationstatusid_seq    SEQUENCE SET     V   SELECT pg_catalog.setval('public.tlkpcurationstatus_curationstatusid_seq', 33, true);
          public          tellervo    false    328            �           0    0 %   tlkpdatecertainty_datecertaintyid_seq    SEQUENCE SET     S   SELECT pg_catalog.setval('public.tlkpdatecertainty_datecertaintyid_seq', 5, true);
          public          postgres    false    331            �           0    0    tlkpdatingtype_datingtypeid_seq    SEQUENCE SET     M   SELECT pg_catalog.setval('public.tlkpdatingtype_datingtypeid_seq', 4, true);
          public          postgres    false    333            �           0    0    tlkpdomain_domainid_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.tlkpdomain_domainid_seq', 1, false);
          public          tellervo    false    335            �           0    0 1   tlkpelementauthenticity_elementauthenticityid_seq    SEQUENCE SET     `   SELECT pg_catalog.setval('public.tlkpelementauthenticity_elementauthenticityid_seq', 1, false);
          public          postgres    false    337            �           0    0 #   tlkpelementshape_elementshapeid_seq    SEQUENCE SET     R   SELECT pg_catalog.setval('public.tlkpelementshape_elementshapeid_seq', 19, true);
          public          postgres    false    339            �           0    0 !   tlkpelementtype_elementtypeid_seq    SEQUENCE SET     Q   SELECT pg_catalog.setval('public.tlkpelementtype_elementtypeid_seq', 994, true);
          public          postgres    false    341            �           0    0    tlkplaboratory_laboratoryid_seq    SEQUENCE SET     N   SELECT pg_catalog.setval('public.tlkplaboratory_laboratoryid_seq', 1, false);
          public          tellervo    false    343            �           0    0 #   tlkplocationtype_locationtypeid_seq    SEQUENCE SET     Q   SELECT pg_catalog.setval('public.tlkplocationtype_locationtypeid_seq', 6, true);
          public          postgres    false    345            �           0    0 1   tlkpmeasurementvariable_measurementvariableid_seq    SEQUENCE SET     _   SELECT pg_catalog.setval('public.tlkpmeasurementvariable_measurementvariableid_seq', 1, true);
          public          postgres    false    347            �           0    0 )   tlkpmeasuringmethod_measuringmethodid_seq    SEQUENCE SET     W   SELECT pg_catalog.setval('public.tlkpmeasuringmethod_measuringmethodid_seq', 3, true);
          public          postgres    false    349            �           0    0    tlkpobjecttype_objecttype_seq    SEQUENCE SET     M   SELECT pg_catalog.setval('public.tlkpobjecttype_objecttype_seq', 995, true);
          public          postgres    false    351            �           0    0 )   tlkppresenceabsence_presenceabsenceid_seq    SEQUENCE SET     X   SELECT pg_catalog.setval('public.tlkppresenceabsence_presenceabsenceid_seq', 1, false);
          public          postgres    false    353            �           0    0 '   tlkpprojectcategory_projectcategory_seq    SEQUENCE SET     V   SELECT pg_catalog.setval('public.tlkpprojectcategory_projectcategory_seq', 33, true);
          public          tellervo    false    354            �           0    0    tlkpprojecttype_projecttype_seq    SEQUENCE SET     N   SELECT pg_catalog.setval('public.tlkpprojecttype_projecttype_seq', 33, true);
          public          tellervo    false    356            �           0    0    tlkprank_rankid_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.tlkprank_rankid_seq', 135, true);
          public          postgres    false    358            �           0    0 !   tlkpreadingnote_readingnoteid_seq    SEQUENCE SET     Q   SELECT pg_catalog.setval('public.tlkpreadingnote_readingnoteid_seq', 199, true);
          public          postgres    false    359            �           0    0 #   tlkpsamplestatus_samplestatusid_seq    SEQUENCE SET     R   SELECT pg_catalog.setval('public.tlkpsamplestatus_samplestatusid_seq', 37, true);
          public          tellervo    false    361            �           0    0    tlkpsampletype_sampletypeid_seq    SEQUENCE SET     M   SELECT pg_catalog.setval('public.tlkpsampletype_sampletypeid_seq', 4, true);
          public          postgres    false    363            �           0    0 /   tlkpsecuritypermission_securitypermissionid_seq    SEQUENCE SET     ^   SELECT pg_catalog.setval('public.tlkpsecuritypermission_securitypermissionid_seq', 1, false);
          public          postgres    false    365            �           0    0    tlkptaxon_taxonid_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.tlkptaxon_taxonid_seq', 1, false);
          public          postgres    false    366            �           0    0    tlkpunits_unitsid_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.tlkpunits_unitsid_seq', 6, true);
          public          postgres    false    370            �           0    0 '   tlkpvmeasurementop_vmeasurementopid_seq    SEQUENCE SET     U   SELECT pg_catalog.setval('public.tlkpvmeasurementop_vmeasurementopid_seq', 7, true);
          public          postgres    false    374            �           0    0    tlkpvocabulary_vocabularyid_seq    SEQUENCE SET     M   SELECT pg_catalog.setval('public.tlkpvocabulary_vocabularyid_seq', 2, true);
          public          postgres    false    376            �           0    0    tlkpwmsserver_wmsserverid_seq    SEQUENCE SET     L   SELECT pg_catalog.setval('public.tlkpwmsserver_wmsserverid_seq', 1, false);
          public          postgres    false    378            �           0    0    yenikapi_gid_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.yenikapi_gid_seq', 1, false);
          public          postgres    false    411            �           2606    65994 3   tblvmeasurementderivedcache only_unique_derivations 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementderivedcache
    ADD CONSTRAINT only_unique_derivations UNIQUE (vmeasurementid, measurementid);
 ]   ALTER TABLE ONLY public.tblvmeasurementderivedcache DROP CONSTRAINT only_unique_derivations;
       public            postgres    false    235    235            �           2606    65996 6   tlkpcomplexpresenceabsence pkey_complexpresenceabsence 
   CONSTRAINT     �   ALTER TABLE ONLY public.tlkpcomplexpresenceabsence
    ADD CONSTRAINT pkey_complexpresenceabsence PRIMARY KEY (complexpresenceabsenceid);
 `   ALTER TABLE ONLY public.tlkpcomplexpresenceabsence DROP CONSTRAINT pkey_complexpresenceabsence;
       public            postgres    false    321                       2606    65998    tblconfig pkey_config 
   CONSTRAINT     Y   ALTER TABLE ONLY public.tblconfig
    ADD CONSTRAINT pkey_config PRIMARY KEY (configid);
 ?   ALTER TABLE ONLY public.tblconfig DROP CONSTRAINT pkey_config;
       public            postgres    false    247            �           2606    66000 *   tlkpcoveragetemporal pkey_coveragetemporal 
   CONSTRAINT     x   ALTER TABLE ONLY public.tlkpcoveragetemporal
    ADD CONSTRAINT pkey_coveragetemporal PRIMARY KEY (coveragetemporalid);
 T   ALTER TABLE ONLY public.tlkpcoveragetemporal DROP CONSTRAINT pkey_coveragetemporal;
       public            postgres    false    323            �           2606    66002 >   tlkpcoveragetemporalfoundation pkey_coveragetemporalfoundation 
   CONSTRAINT     �   ALTER TABLE ONLY public.tlkpcoveragetemporalfoundation
    ADD CONSTRAINT pkey_coveragetemporalfoundation PRIMARY KEY (coveragetemporalfoundationid);
 h   ALTER TABLE ONLY public.tlkpcoveragetemporalfoundation DROP CONSTRAINT pkey_coveragetemporalfoundation;
       public            postgres    false    325            '           2606    66004 *   tlkptrackinglocation pkey_curationlocation 
   CONSTRAINT     x   ALTER TABLE ONLY public.tlkptrackinglocation
    ADD CONSTRAINT pkey_curationlocation PRIMARY KEY (trackinglocationid);
 T   ALTER TABLE ONLY public.tlkptrackinglocation DROP CONSTRAINT pkey_curationlocation;
       public            postgres    false    253            �           2606    66006 &   tlkpcurationstatus pkey_curationstatus 
   CONSTRAINT     r   ALTER TABLE ONLY public.tlkpcurationstatus
    ADD CONSTRAINT pkey_curationstatus PRIMARY KEY (curationstatusid);
 P   ALTER TABLE ONLY public.tlkpcurationstatus DROP CONSTRAINT pkey_curationstatus;
       public            tellervo    false    327            )           2606    66008 '   tblcustomvocabterm pkey_customvocabterm 
   CONSTRAINT     t   ALTER TABLE ONLY public.tblcustomvocabterm
    ADD CONSTRAINT pkey_customvocabterm PRIMARY KEY (customvocabtermid);
 Q   ALTER TABLE ONLY public.tblcustomvocabterm DROP CONSTRAINT pkey_customvocabterm;
       public            postgres    false    255            �           2606    66010    tlkpdatatype pkey_datatype 
   CONSTRAINT     ^   ALTER TABLE ONLY public.tlkpdatatype
    ADD CONSTRAINT pkey_datatype PRIMARY KEY (datatype);
 D   ALTER TABLE ONLY public.tlkpdatatype DROP CONSTRAINT pkey_datatype;
       public            tellervo    false    329            �           2606    66012 $   tlkpdatecertainty pkey_datecertainty 
   CONSTRAINT     o   ALTER TABLE ONLY public.tlkpdatecertainty
    ADD CONSTRAINT pkey_datecertainty PRIMARY KEY (datecertaintyid);
 N   ALTER TABLE ONLY public.tlkpdatecertainty DROP CONSTRAINT pkey_datecertainty;
       public            postgres    false    330            �           2606    66014    tlkpdatingtype pkey_datingtype 
   CONSTRAINT     f   ALTER TABLE ONLY public.tlkpdatingtype
    ADD CONSTRAINT pkey_datingtype PRIMARY KEY (datingtypeid);
 H   ALTER TABLE ONLY public.tlkpdatingtype DROP CONSTRAINT pkey_datingtype;
       public            postgres    false    332            �           2606    66016 0   tlkpelementauthenticity pkey_elementauthenticity 
   CONSTRAINT     �   ALTER TABLE ONLY public.tlkpelementauthenticity
    ADD CONSTRAINT pkey_elementauthenticity PRIMARY KEY (elementauthenticityid);
 Z   ALTER TABLE ONLY public.tlkpelementauthenticity DROP CONSTRAINT pkey_elementauthenticity;
       public            postgres    false    336            �           2606    66018 "   tlkpelementshape pkey_elementshape 
   CONSTRAINT     l   ALTER TABLE ONLY public.tlkpelementshape
    ADD CONSTRAINT pkey_elementshape PRIMARY KEY (elementshapeid);
 L   ALTER TABLE ONLY public.tlkpelementshape DROP CONSTRAINT pkey_elementshape;
       public            postgres    false    338            �           2606    66020     tlkpelementtype pkey_elementtype 
   CONSTRAINT     i   ALTER TABLE ONLY public.tlkpelementtype
    ADD CONSTRAINT pkey_elementtype PRIMARY KEY (elementtypeid);
 J   ALTER TABLE ONLY public.tlkpelementtype DROP CONSTRAINT pkey_elementtype;
       public            postgres    false    340            -           2606    66022 +   tblenvironmentaldata pkey_environmentaldata 
   CONSTRAINT     z   ALTER TABLE ONLY public.tblenvironmentaldata
    ADD CONSTRAINT pkey_environmentaldata PRIMARY KEY (environmentaldataid);
 U   ALTER TABLE ONLY public.tblenvironmentaldata DROP CONSTRAINT pkey_environmentaldata;
       public            postgres    false    259            1           2606    66024    tbliptracking pkey_iptracking 
   CONSTRAINT     _   ALTER TABLE ONLY public.tbliptracking
    ADD CONSTRAINT pkey_iptracking PRIMARY KEY (ipaddr);
 G   ALTER TABLE ONLY public.tbliptracking DROP CONSTRAINT pkey_iptracking;
       public            postgres    false    261            3           2606    66026    tbllaboratory pkey_laboratory 
   CONSTRAINT     e   ALTER TABLE ONLY public.tbllaboratory
    ADD CONSTRAINT pkey_laboratory PRIMARY KEY (laboratoryid);
 G   ALTER TABLE ONLY public.tbllaboratory DROP CONSTRAINT pkey_laboratory;
       public            tellervo    false    262            �           2606    66028 "   tlkplocationtype pkey_locationtype 
   CONSTRAINT     l   ALTER TABLE ONLY public.tlkplocationtype
    ADD CONSTRAINT pkey_locationtype PRIMARY KEY (locationtypeid);
 L   ALTER TABLE ONLY public.tlkplocationtype DROP CONSTRAINT pkey_locationtype;
       public            postgres    false    344            �           2606    66030    tblmeasurement pkey_measurement 
   CONSTRAINT     h   ALTER TABLE ONLY public.tblmeasurement
    ADD CONSTRAINT pkey_measurement PRIMARY KEY (measurementid);
 I   ALTER TABLE ONLY public.tblmeasurement DROP CONSTRAINT pkey_measurement;
       public            postgres    false    234            �           2606    66032 0   tlkpmeasurementvariable pkey_measurementvariable 
   CONSTRAINT     �   ALTER TABLE ONLY public.tlkpmeasurementvariable
    ADD CONSTRAINT pkey_measurementvariable PRIMARY KEY (measurementvariableid);
 Z   ALTER TABLE ONLY public.tlkpmeasurementvariable DROP CONSTRAINT pkey_measurementvariable;
       public            postgres    false    346            �           2606    66034 (   tlkpmeasuringmethod pkey_measuringmethod 
   CONSTRAINT     u   ALTER TABLE ONLY public.tlkpmeasuringmethod
    ADD CONSTRAINT pkey_measuringmethod PRIMARY KEY (measuringmethodid);
 R   ALTER TABLE ONLY public.tlkpmeasuringmethod DROP CONSTRAINT pkey_measuringmethod;
       public            postgres    false    348            �           2606    66036    tblobject pkey_object 
   CONSTRAINT     Y   ALTER TABLE ONLY public.tblobject
    ADD CONSTRAINT pkey_object PRIMARY KEY (objectid);
 ?   ALTER TABLE ONLY public.tblobject DROP CONSTRAINT pkey_object;
       public            postgres    false    231            9           2606    66038 !   tblobjectregion pkey_objectregion 
   CONSTRAINT     k   ALTER TABLE ONLY public.tblobjectregion
    ADD CONSTRAINT pkey_objectregion PRIMARY KEY (objectregionid);
 K   ALTER TABLE ONLY public.tblobjectregion DROP CONSTRAINT pkey_objectregion;
       public            postgres    false    265            �           2606    66040    tlkpobjecttype pkey_objectype 
   CONSTRAINT     e   ALTER TABLE ONLY public.tlkpobjecttype
    ADD CONSTRAINT pkey_objectype PRIMARY KEY (objecttypeid);
 G   ALTER TABLE ONLY public.tlkpobjecttype DROP CONSTRAINT pkey_objectype;
       public            postgres    false    350            ;           2606    66042 #   tblodkdefinition pkey_odkdefinition 
   CONSTRAINT     n   ALTER TABLE ONLY public.tblodkdefinition
    ADD CONSTRAINT pkey_odkdefinition PRIMARY KEY (odkdefinitionid);
 M   ALTER TABLE ONLY public.tblodkdefinition DROP CONSTRAINT pkey_odkdefinition;
       public            tellervo    false    267            =           2606    66044    tblodkinstance pkey_odkinstance 
   CONSTRAINT     h   ALTER TABLE ONLY public.tblodkinstance
    ADD CONSTRAINT pkey_odkinstance PRIMARY KEY (odkinstanceid);
 I   ALTER TABLE ONLY public.tblodkinstance DROP CONSTRAINT pkey_odkinstance;
       public            tellervo    false    268            �           2606    66046 (   tlkppresenceabsence pkey_presenceabsence 
   CONSTRAINT     u   ALTER TABLE ONLY public.tlkppresenceabsence
    ADD CONSTRAINT pkey_presenceabsence PRIMARY KEY (presenceabsenceid);
 R   ALTER TABLE ONLY public.tlkppresenceabsence DROP CONSTRAINT pkey_presenceabsence;
       public            postgres    false    352            ?           2606    66048    tblproject pkey_project 
   CONSTRAINT     \   ALTER TABLE ONLY public.tblproject
    ADD CONSTRAINT pkey_project PRIMARY KEY (projectid);
 A   ALTER TABLE ONLY public.tblproject DROP CONSTRAINT pkey_project;
       public            tellervo    false    269            �           2606    66050 (   tlkpprojectcategory pkey_projectcategory 
   CONSTRAINT     u   ALTER TABLE ONLY public.tlkpprojectcategory
    ADD CONSTRAINT pkey_projectcategory PRIMARY KEY (projectcategoryid);
 R   ALTER TABLE ONLY public.tlkpprojectcategory DROP CONSTRAINT pkey_projectcategory;
       public            tellervo    false    355            C           2606    66052 -   tblprojectprojecttype pkey_projectprojecttype 
   CONSTRAINT     }   ALTER TABLE ONLY public.tblprojectprojecttype
    ADD CONSTRAINT pkey_projectprojecttype PRIMARY KEY (projectprojecttypeid);
 W   ALTER TABLE ONLY public.tblprojectprojecttype DROP CONSTRAINT pkey_projectprojecttype;
       public            tellervo    false    270            �           2606    66054     tlkpprojecttype pkey_projecttype 
   CONSTRAINT     i   ALTER TABLE ONLY public.tlkpprojecttype
    ADD CONSTRAINT pkey_projecttype PRIMARY KEY (projecttypeid);
 J   ALTER TABLE ONLY public.tlkpprojecttype DROP CONSTRAINT pkey_projecttype;
       public            tellervo    false    357                       2606    66056    tblradius pkey_radius 
   CONSTRAINT     Y   ALTER TABLE ONLY public.tblradius
    ADD CONSTRAINT pkey_radius PRIMARY KEY (radiusid);
 ?   ALTER TABLE ONLY public.tblradius DROP CONSTRAINT pkey_radius;
       public            postgres    false    239            N           2606    66058    tblreading pkey_reading 
   CONSTRAINT     \   ALTER TABLE ONLY public.tblreading
    ADD CONSTRAINT pkey_reading PRIMARY KEY (readingid);
 A   ALTER TABLE ONLY public.tblreading DROP CONSTRAINT pkey_reading;
       public            postgres    false    275            �           2606    66060     tlkpreadingnote pkey_readingnote 
   CONSTRAINT     i   ALTER TABLE ONLY public.tlkpreadingnote
    ADD CONSTRAINT pkey_readingnote PRIMARY KEY (readingnoteid);
 J   ALTER TABLE ONLY public.tlkpreadingnote DROP CONSTRAINT pkey_readingnote;
       public            postgres    false    232            R           2606    66062 -   tblreadingreadingnote pkey_readingreadingnote 
   CONSTRAINT     }   ALTER TABLE ONLY public.tblreadingreadingnote
    ADD CONSTRAINT pkey_readingreadingnote PRIMARY KEY (readingreadingnoteid);
 W   ALTER TABLE ONLY public.tblreadingreadingnote DROP CONSTRAINT pkey_readingreadingnote;
       public            postgres    false    277            ]           2606    66064    tblrequestlog pkey_requestlog 
   CONSTRAINT     e   ALTER TABLE ONLY public.tblrequestlog
    ADD CONSTRAINT pkey_requestlog PRIMARY KEY (requestlogid);
 G   ALTER TABLE ONLY public.tblrequestlog DROP CONSTRAINT pkey_requestlog;
       public            postgres    false    283                       2606    66066    tblsample pkey_sample 
   CONSTRAINT     Y   ALTER TABLE ONLY public.tblsample
    ADD CONSTRAINT pkey_sample PRIMARY KEY (sampleid);
 ?   ALTER TABLE ONLY public.tblsample DROP CONSTRAINT pkey_sample;
       public            postgres    false    240            �           2606    66068 &   tlkpsamplestatus pkey_samplestatustype 
   CONSTRAINT     p   ALTER TABLE ONLY public.tlkpsamplestatus
    ADD CONSTRAINT pkey_samplestatustype PRIMARY KEY (samplestatusid);
 P   ALTER TABLE ONLY public.tlkpsamplestatus DROP CONSTRAINT pkey_samplestatustype;
       public            tellervo    false    360            �           2606    66070    tlkpsampletype pkey_sampletype 
   CONSTRAINT     f   ALTER TABLE ONLY public.tlkpsampletype
    ADD CONSTRAINT pkey_sampletype PRIMARY KEY (sampletypeid);
 H   ALTER TABLE ONLY public.tlkpsampletype DROP CONSTRAINT pkey_sampletype;
       public            postgres    false    362            a           2606    66072 '   tblsecuritydefault pkey_securitydefault 
   CONSTRAINT     t   ALTER TABLE ONLY public.tblsecuritydefault
    ADD CONSTRAINT pkey_securitydefault PRIMARY KEY (securitydefaultid);
 Q   ALTER TABLE ONLY public.tblsecuritydefault DROP CONSTRAINT pkey_securitydefault;
       public            postgres    false    286                       2606    66074 #   tblsecuritygroup pkey_securitygroup 
   CONSTRAINT     n   ALTER TABLE ONLY public.tblsecuritygroup
    ADD CONSTRAINT pkey_securitygroup PRIMARY KEY (securitygroupid);
 M   ALTER TABLE ONLY public.tblsecuritygroup DROP CONSTRAINT pkey_securitygroup;
       public            postgres    false    237            n           2606    66076 7   tblsecuritygroupmembership pkey_securitygroupmembership 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblsecuritygroupmembership
    ADD CONSTRAINT pkey_securitygroupmembership PRIMARY KEY (securitygroupmembershipid);
 a   ALTER TABLE ONLY public.tblsecuritygroupmembership DROP CONSTRAINT pkey_securitygroupmembership;
       public            postgres    false    290            t           2606    66078 %   tblsecurityobject pkey_securityobject 
   CONSTRAINT     q   ALTER TABLE ONLY public.tblsecurityobject
    ADD CONSTRAINT pkey_securityobject PRIMARY KEY (securityobjectid);
 O   ALTER TABLE ONLY public.tblsecurityobject DROP CONSTRAINT pkey_securityobject;
       public            postgres    false    292            �           2606    66080 .   tlkpsecuritypermission pkey_securitypermission 
   CONSTRAINT     ~   ALTER TABLE ONLY public.tlkpsecuritypermission
    ADD CONSTRAINT pkey_securitypermission PRIMARY KEY (securitypermissionid);
 X   ALTER TABLE ONLY public.tlkpsecuritypermission DROP CONSTRAINT pkey_securitypermission;
       public            postgres    false    364            h           2606    66082 $   tblsecurityelement pkey_securitytree 
   CONSTRAINT     q   ALTER TABLE ONLY public.tblsecurityelement
    ADD CONSTRAINT pkey_securitytree PRIMARY KEY (securityelementid);
 N   ALTER TABLE ONLY public.tblsecurityelement DROP CONSTRAINT pkey_securitytree;
       public            postgres    false    288            x           2606    66084 !   tblsecurityuser pkey_securityuser 
   CONSTRAINT     k   ALTER TABLE ONLY public.tblsecurityuser
    ADD CONSTRAINT pkey_securityuser PRIMARY KEY (securityuserid);
 K   ALTER TABLE ONLY public.tblsecurityuser DROP CONSTRAINT pkey_securityuser;
       public            postgres    false    295            }           2606    66086 5   tblsecurityusermembership pkey_securityusermembership 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityusermembership
    ADD CONSTRAINT pkey_securityusermembership PRIMARY KEY (tblsecurityusermembershipid);
 _   ALTER TABLE ONLY public.tblsecurityusermembership DROP CONSTRAINT pkey_securityusermembership;
       public            postgres    false    296            �           2606    66088 1   tblsecurityvmeasurement pkey_securityvmeasurement 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityvmeasurement
    ADD CONSTRAINT pkey_securityvmeasurement PRIMARY KEY (securityvmeasurementid);
 [   ALTER TABLE ONLY public.tblsecurityvmeasurement DROP CONSTRAINT pkey_securityvmeasurement;
       public            postgres    false    298            �           2606    66090 %   tblsupportedclient pkey_supportclient 
   CONSTRAINT     p   ALTER TABLE ONLY public.tblsupportedclient
    ADD CONSTRAINT pkey_supportclient PRIMARY KEY (supportclientid);
 O   ALTER TABLE ONLY public.tblsupportedclient DROP CONSTRAINT pkey_supportclient;
       public            postgres    false    300            �           2606    66845    tlkptaxon pkey_taxon 
   CONSTRAINT     W   ALTER TABLE ONLY public.tlkptaxon
    ADD CONSTRAINT pkey_taxon PRIMARY KEY (taxonid);
 >   ALTER TABLE ONLY public.tlkptaxon DROP CONSTRAINT pkey_taxon;
       public            postgres    false    367            �           2606    66094    tlkptaxonrank pkey_taxonrank 
   CONSTRAINT     c   ALTER TABLE ONLY public.tlkptaxonrank
    ADD CONSTRAINT pkey_taxonrank PRIMARY KEY (taxonrankid);
 F   ALTER TABLE ONLY public.tlkptaxonrank DROP CONSTRAINT pkey_taxonrank;
       public            postgres    false    368                       2606    66096    tblbox pkey_tblbox 
   CONSTRAINT     S   ALTER TABLE ONLY public.tblbox
    ADD CONSTRAINT pkey_tblbox PRIMARY KEY (boxid);
 <   ALTER TABLE ONLY public.tblbox DROP CONSTRAINT pkey_tblbox;
       public            postgres    false    246            !           2606    66098    tblcrossdate pkey_tblcrossdate 
   CONSTRAINT     e   ALTER TABLE ONLY public.tblcrossdate
    ADD CONSTRAINT pkey_tblcrossdate PRIMARY KEY (crossdateid);
 H   ALTER TABLE ONLY public.tblcrossdate DROP CONSTRAINT pkey_tblcrossdate;
       public            postgres    false    249            %           2606    66100 !   tblcurationevent pkey_tblcuration 
   CONSTRAINT     l   ALTER TABLE ONLY public.tblcurationevent
    ADD CONSTRAINT pkey_tblcuration PRIMARY KEY (curationeventid);
 K   ALTER TABLE ONLY public.tblcurationevent DROP CONSTRAINT pkey_tblcuration;
       public            tellervo    false    251            	           2606    66102    tblloan pkey_tblloan 
   CONSTRAINT     V   ALTER TABLE ONLY public.tblloan
    ADD CONSTRAINT pkey_tblloan PRIMARY KEY (loanid);
 >   ALTER TABLE ONLY public.tblloan DROP CONSTRAINT pkey_tblloan;
       public            tellervo    false    238            G           2606    66104 "   tblrasterlayer pkey_tblrasterlayer 
   CONSTRAINT     k   ALTER TABLE ONLY public.tblrasterlayer
    ADD CONSTRAINT pkey_tblrasterlayer PRIMARY KEY (rasterlayerid);
 L   ALTER TABLE ONLY public.tblrasterlayer DROP CONSTRAINT pkey_tblrasterlayer;
       public            postgres    false    273            V           2606    66106    tblredate pkey_tblredate 
   CONSTRAINT     \   ALTER TABLE ONLY public.tblredate
    ADD CONSTRAINT pkey_tblredate PRIMARY KEY (redateid);
 B   ALTER TABLE ONLY public.tblredate DROP CONSTRAINT pkey_tblredate;
       public            postgres    false    279            �           2606    66108    tbltag pkey_tbltag 
   CONSTRAINT     S   ALTER TABLE ONLY public.tbltag
    ADD CONSTRAINT pkey_tbltag PRIMARY KEY (tagid);
 <   ALTER TABLE ONLY public.tbltag DROP CONSTRAINT pkey_tbltag;
       public            tellervo    false    302            �           2606    66110    tbltruncate pkey_tbltruncate 
   CONSTRAINT     b   ALTER TABLE ONLY public.tbltruncate
    ADD CONSTRAINT pkey_tbltruncate PRIMARY KEY (truncateid);
 F   ALTER TABLE ONLY public.tbltruncate DROP CONSTRAINT pkey_tbltruncate;
       public            postgres    false    303            �           2606    66112    tlkpdomain pkey_tlkpdomain 
   CONSTRAINT     ^   ALTER TABLE ONLY public.tlkpdomain
    ADD CONSTRAINT pkey_tlkpdomain PRIMARY KEY (domainid);
 D   ALTER TABLE ONLY public.tlkpdomain DROP CONSTRAINT pkey_tlkpdomain;
       public            tellervo    false    334                       2606    66114    tblelement pkey_tree 
   CONSTRAINT     Y   ALTER TABLE ONLY public.tblelement
    ADD CONSTRAINT pkey_tree PRIMARY KEY (elementid);
 >   ALTER TABLE ONLY public.tblelement DROP CONSTRAINT pkey_tree;
       public            postgres    false    236            �           2606    66116    tlkpunit pkey_unit 
   CONSTRAINT     T   ALTER TABLE ONLY public.tlkpunit
    ADD CONSTRAINT pkey_unit PRIMARY KEY (unitid);
 <   ALTER TABLE ONLY public.tlkpunit DROP CONSTRAINT pkey_unit;
       public            postgres    false    369            �           2606    66118    tblupgradelog pkey_upgradelog 
   CONSTRAINT     e   ALTER TABLE ONLY public.tblupgradelog
    ADD CONSTRAINT pkey_upgradelog PRIMARY KEY (upgradelogid);
 G   ALTER TABLE ONLY public.tblupgradelog DROP CONSTRAINT pkey_upgradelog;
       public            postgres    false    305            �           2606    66120 *   tlkpuserdefinedfield pkey_userdefinedfield 
   CONSTRAINT     x   ALTER TABLE ONLY public.tlkpuserdefinedfield
    ADD CONSTRAINT pkey_userdefinedfield PRIMARY KEY (userdefinedfieldid);
 T   ALTER TABLE ONLY public.tlkpuserdefinedfield DROP CONSTRAINT pkey_userdefinedfield;
       public            tellervo    false    371            �           2606    66122 3   tbluserdefinedfieldvalue pkey_userdefinedfieldvalue 
   CONSTRAINT     �   ALTER TABLE ONLY public.tbluserdefinedfieldvalue
    ADD CONSTRAINT pkey_userdefinedfieldvalue PRIMARY KEY (userdefinedfieldvalueid);
 ]   ALTER TABLE ONLY public.tbluserdefinedfieldvalue DROP CONSTRAINT pkey_userdefinedfieldvalue;
       public            tellervo    false    307            �           2606    66124 (   tlkpuserdefinedterm pkey_userdefinedterm 
   CONSTRAINT     u   ALTER TABLE ONLY public.tlkpuserdefinedterm
    ADD CONSTRAINT pkey_userdefinedterm PRIMARY KEY (userdefinedtermid);
 R   ALTER TABLE ONLY public.tlkpuserdefinedterm DROP CONSTRAINT pkey_userdefinedterm;
       public            tellervo    false    372            �           2606    66126 +   tblvmeasurementgroup pkey_vmeasurementgroup 
   CONSTRAINT     z   ALTER TABLE ONLY public.tblvmeasurementgroup
    ADD CONSTRAINT pkey_vmeasurementgroup PRIMARY KEY (vmeasurementgroupid);
 U   ALTER TABLE ONLY public.tblvmeasurementgroup DROP CONSTRAINT pkey_vmeasurementgroup;
       public            postgres    false    311            �           2606    66128 3   tblvmeasurementmetacache pkey_vmeasurementmetacache 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementmetacache
    ADD CONSTRAINT pkey_vmeasurementmetacache PRIMARY KEY (vmeasurementmetacacheid);
 ]   ALTER TABLE ONLY public.tblvmeasurementmetacache DROP CONSTRAINT pkey_vmeasurementmetacache;
       public            postgres    false    233            �           2606    66130 &   tlkpvmeasurementop pkey_vmeasurementop 
   CONSTRAINT     r   ALTER TABLE ONLY public.tlkpvmeasurementop
    ADD CONSTRAINT pkey_vmeasurementop PRIMARY KEY (vmeasurementopid);
 P   ALTER TABLE ONLY public.tlkpvmeasurementop DROP CONSTRAINT pkey_vmeasurementop;
       public            postgres    false    373                       2606    66132 ;   tblvmeasurementreadingresult pkey_vmeasurementreadingresult 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementreadingresult
    ADD CONSTRAINT pkey_vmeasurementreadingresult PRIMARY KEY (vmeasurementreadingresultid);
 e   ALTER TABLE ONLY public.tblvmeasurementreadingresult DROP CONSTRAINT pkey_vmeasurementreadingresult;
       public            postgres    false    241            �           2606    66134 -   tblvmeasurementresult pkey_vmeasurementresult 
   CONSTRAINT     }   ALTER TABLE ONLY public.tblvmeasurementresult
    ADD CONSTRAINT pkey_vmeasurementresult PRIMARY KEY (vmeasurementresultid);
 W   ALTER TABLE ONLY public.tblvmeasurementresult DROP CONSTRAINT pkey_vmeasurementresult;
       public            postgres    false    318            �           2606    66136 +   tblvmeasurementtotag pkey_vmeasurementtotag 
   CONSTRAINT     z   ALTER TABLE ONLY public.tblvmeasurementtotag
    ADD CONSTRAINT pkey_vmeasurementtotag PRIMARY KEY (vmeasurementtotagid);
 U   ALTER TABLE ONLY public.tblvmeasurementtotag DROP CONSTRAINT pkey_vmeasurementtotag;
       public            tellervo    false    319                       2606    66138    tlkpvocabulary pkey_vocabulary 
   CONSTRAINT     f   ALTER TABLE ONLY public.tlkpvocabulary
    ADD CONSTRAINT pkey_vocabulary PRIMARY KEY (vocabularyid);
 H   ALTER TABLE ONLY public.tlkpvocabulary DROP CONSTRAINT pkey_vocabulary;
       public            postgres    false    375            A           2606    66140    tblproject tblproject-uniqtitle 
   CONSTRAINT     ]   ALTER TABLE ONLY public.tblproject
    ADD CONSTRAINT "tblproject-uniqtitle" UNIQUE (title);
 K   ALTER TABLE ONLY public.tblproject DROP CONSTRAINT "tblproject-uniqtitle";
       public            tellervo    false    269            [           2606    66142    tblregion tblregion_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.tblregion
    ADD CONSTRAINT tblregion_pkey PRIMARY KEY (regionid);
 B   ALTER TABLE ONLY public.tblregion DROP CONSTRAINT tblregion_pkey;
       public            postgres    false    281            �           2606    66144 <   tblvmeasurementderivedcache tblvmeasurementderivedcache_pkey 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementderivedcache
    ADD CONSTRAINT tblvmeasurementderivedcache_pkey PRIMARY KEY (derivedcacheid);
 f   ALTER TABLE ONLY public.tblvmeasurementderivedcache DROP CONSTRAINT tblvmeasurementderivedcache_pkey;
       public            postgres    false    235            �           2606    66146 H   tblvmeasurementrelyearreadingnote tblvmeasurementrelyearreadingnote_pkey 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementrelyearreadingnote
    ADD CONSTRAINT tblvmeasurementrelyearreadingnote_pkey PRIMARY KEY (relyearreadingnoteid);
 r   ALTER TABLE ONLY public.tblvmeasurementrelyearreadingnote DROP CONSTRAINT tblvmeasurementrelyearreadingnote_pkey;
       public            postgres    false    317            �           2606    66148     tlkpindextype tlkpindextype_pkey 
   CONSTRAINT     c   ALTER TABLE ONLY public.tlkpindextype
    ADD CONSTRAINT tlkpindextype_pkey PRIMARY KEY (indexid);
 J   ALTER TABLE ONLY public.tlkpindextype DROP CONSTRAINT tlkpindextype_pkey;
       public            postgres    false    342            �           2606    66150 +   tlkpobjecttype tlkpobjecttype-nodupsinvocab 
   CONSTRAINT     |   ALTER TABLE ONLY public.tlkpobjecttype
    ADD CONSTRAINT "tlkpobjecttype-nodupsinvocab" UNIQUE (objecttype, vocabularyid);
 W   ALTER TABLE ONLY public.tlkpobjecttype DROP CONSTRAINT "tlkpobjecttype-nodupsinvocab";
       public            postgres    false    350    350            �           2606    66152 5   tlkpprojectcategory tlkpprojectcategory-nodupsinvocab 
   CONSTRAINT     �   ALTER TABLE ONLY public.tlkpprojectcategory
    ADD CONSTRAINT "tlkpprojectcategory-nodupsinvocab" UNIQUE (projectcategory, vocabularyid);
 a   ALTER TABLE ONLY public.tlkpprojectcategory DROP CONSTRAINT "tlkpprojectcategory-nodupsinvocab";
       public            tellervo    false    355    355            �           2606    66154 -   tlkpprojecttype tlkpprojecttype-nodupsinvocab 
   CONSTRAINT        ALTER TABLE ONLY public.tlkpprojecttype
    ADD CONSTRAINT "tlkpprojecttype-nodupsinvocab" UNIQUE (projecttype, vocabularyid);
 Y   ALTER TABLE ONLY public.tlkpprojecttype DROP CONSTRAINT "tlkpprojecttype-nodupsinvocab";
       public            tellervo    false    357    357            j           2606    66156 0   tblsecurityelement uniq-element-group-permission 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityelement
    ADD CONSTRAINT "uniq-element-group-permission" UNIQUE (securitygroupid, securitypermissionid, securityelementid);
 \   ALTER TABLE ONLY public.tblsecurityelement DROP CONSTRAINT "uniq-element-group-permission";
       public            postgres    false    288    288    288            v           2606    66158 .   tblsecurityobject uniq-object-group-permission 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityobject
    ADD CONSTRAINT "uniq-object-group-permission" UNIQUE (objectid, securitygroupid, securitypermissionid);
 Z   ALTER TABLE ONLY public.tblsecurityobject DROP CONSTRAINT "uniq-object-group-permission";
       public            postgres    false    292    292    292                       2606    66160    tblbox uniq_boxtitle 
   CONSTRAINT     P   ALTER TABLE ONLY public.tblbox
    ADD CONSTRAINT uniq_boxtitle UNIQUE (title);
 >   ALTER TABLE ONLY public.tblbox DROP CONSTRAINT uniq_boxtitle;
       public            postgres    false    246            �           2606    66841    tlkptaxon uniq_colid 
   CONSTRAINT     P   ALTER TABLE ONLY public.tlkptaxon
    ADD CONSTRAINT uniq_colid UNIQUE (colid);
 >   ALTER TABLE ONLY public.tlkptaxon DROP CONSTRAINT uniq_colid;
       public            postgres    false    367                       2606    66162    tblconfig uniq_config-key 
   CONSTRAINT     U   ALTER TABLE ONLY public.tblconfig
    ADD CONSTRAINT "uniq_config-key" UNIQUE (key);
 E   ALTER TABLE ONLY public.tblconfig DROP CONSTRAINT "uniq_config-key";
       public            postgres    false    247            �           2606    66164 $   tlkpdatecertainty uniq_datecertainty 
   CONSTRAINT     h   ALTER TABLE ONLY public.tlkpdatecertainty
    ADD CONSTRAINT uniq_datecertainty UNIQUE (datecertainty);
 N   ALTER TABLE ONLY public.tlkpdatecertainty DROP CONSTRAINT uniq_datecertainty;
       public            postgres    false    330            c           2606    66166 #   tblsecuritydefault uniq_defaultperm 
   CONSTRAINT        ALTER TABLE ONLY public.tblsecuritydefault
    ADD CONSTRAINT uniq_defaultperm UNIQUE (securitygroupid, securitypermissionid);
 M   ALTER TABLE ONLY public.tblsecuritydefault DROP CONSTRAINT uniq_defaultperm;
       public            postgres    false    286    286            /           2606    66168 C   tblenvironmentaldata uniq_environmentaldata_elementid_rasterlayerid 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblenvironmentaldata
    ADD CONSTRAINT uniq_environmentaldata_elementid_rasterlayerid UNIQUE (elementid, rasterlayerid);
 m   ALTER TABLE ONLY public.tblenvironmentaldata DROP CONSTRAINT uniq_environmentaldata_elementid_rasterlayerid;
       public            postgres    false    259    259            5           2606    66170    tbllaboratory uniq_labname 
   CONSTRAINT     U   ALTER TABLE ONLY public.tbllaboratory
    ADD CONSTRAINT uniq_labname UNIQUE (name);
 D   ALTER TABLE ONLY public.tbllaboratory DROP CONSTRAINT uniq_labname;
       public            tellervo    false    262            E           2606    66172 -   tblprojectprojecttype uniq_projectprojecttype 
   CONSTRAINT     |   ALTER TABLE ONLY public.tblprojectprojecttype
    ADD CONSTRAINT uniq_projectprojecttype UNIQUE (projectid, projecttypeid);
 W   ALTER TABLE ONLY public.tblprojectprojecttype DROP CONSTRAINT uniq_projectprojecttype;
       public            tellervo    false    270    270            �           2606    66174    tlkptaxonrank uniq_rankorder 
   CONSTRAINT     \   ALTER TABLE ONLY public.tlkptaxonrank
    ADD CONSTRAINT uniq_rankorder UNIQUE (rankorder);
 F   ALTER TABLE ONLY public.tlkptaxonrank DROP CONSTRAINT uniq_rankorder;
       public            postgres    false    368            I           2606    66176 '   tblrasterlayer uniq_rasterlayerfilename 
   CONSTRAINT     f   ALTER TABLE ONLY public.tblrasterlayer
    ADD CONSTRAINT uniq_rasterlayerfilename UNIQUE (filename);
 Q   ALTER TABLE ONLY public.tblrasterlayer DROP CONSTRAINT uniq_rasterlayerfilename;
       public            postgres    false    273            K           2606    66178 #   tblrasterlayer uniq_rasterlayername 
   CONSTRAINT     ^   ALTER TABLE ONLY public.tblrasterlayer
    ADD CONSTRAINT uniq_rasterlayername UNIQUE (name);
 M   ALTER TABLE ONLY public.tblrasterlayer DROP CONSTRAINT uniq_rasterlayername;
       public            postgres    false    273            T           2606    66180 =   tblreadingreadingnote uniq_readingreadingnote_notesperreading 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblreadingreadingnote
    ADD CONSTRAINT uniq_readingreadingnote_notesperreading UNIQUE (readingid, readingnoteid);
 g   ALTER TABLE ONLY public.tblreadingreadingnote DROP CONSTRAINT uniq_readingreadingnote_notesperreading;
       public            postgres    false    277    277            X           2606    66182 "   tblredate uniq_redate_vmeasurement 
   CONSTRAINT     g   ALTER TABLE ONLY public.tblredate
    ADD CONSTRAINT uniq_redate_vmeasurement UNIQUE (vmeasurementid);
 L   ALTER TABLE ONLY public.tblredate DROP CONSTRAINT uniq_redate_vmeasurement;
       public            postgres    false    279            �           2606    66184 $   tlkpsampletype uniq_sampletype_label 
   CONSTRAINT     e   ALTER TABLE ONLY public.tlkpsampletype
    ADD CONSTRAINT uniq_sampletype_label UNIQUE (sampletype);
 N   ALTER TABLE ONLY public.tlkpsampletype DROP CONSTRAINT uniq_sampletype_label;
       public            postgres    false    362            p           2606    66186 Q   tblsecuritygroupmembership uniq_securitygroupmembership_parentchildsecuritygroups 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblsecuritygroupmembership
    ADD CONSTRAINT uniq_securitygroupmembership_parentchildsecuritygroups UNIQUE (parentsecuritygroupid, childsecuritygroupid);
 {   ALTER TABLE ONLY public.tblsecuritygroupmembership DROP CONSTRAINT uniq_securitygroupmembership_parentchildsecuritygroups;
       public            postgres    false    290    290            z           2606    66188 *   tblsecurityuser uniq_securityuser_username 
   CONSTRAINT     i   ALTER TABLE ONLY public.tblsecurityuser
    ADD CONSTRAINT uniq_securityuser_username UNIQUE (username);
 T   ALTER TABLE ONLY public.tblsecurityuser DROP CONSTRAINT uniq_securityuser_username;
       public            postgres    false    295            �           2606    66190 1   tblsecurityvmeasurement uniq_securityvmeasurement 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityvmeasurement
    ADD CONSTRAINT uniq_securityvmeasurement UNIQUE (vmeasurementid, securitygroupid, securitypermissionid);
 [   ALTER TABLE ONLY public.tblsecurityvmeasurement DROP CONSTRAINT uniq_securityvmeasurement;
       public            postgres    false    298    298    298            �           2606    66192 %   tblsupportedclient uniq_supportclient 
   CONSTRAINT     b   ALTER TABLE ONLY public.tblsupportedclient
    ADD CONSTRAINT uniq_supportclient UNIQUE (client);
 O   ALTER TABLE ONLY public.tblsupportedclient DROP CONSTRAINT uniq_supportclient;
       public            postgres    false    300            �           2606    66194 ,   tblvmeasurementtotag uniq_tagforvmeasurement 
   CONSTRAINT     x   ALTER TABLE ONLY public.tblvmeasurementtotag
    ADD CONSTRAINT uniq_tagforvmeasurement UNIQUE (tagid, vmeasurementid);
 V   ALTER TABLE ONLY public.tblvmeasurementtotag DROP CONSTRAINT uniq_tagforvmeasurement;
       public            tellervo    false    319    319            �           2606    66198    tlkptaxonrank uniq_taxonrank 
   CONSTRAINT     \   ALTER TABLE ONLY public.tlkptaxonrank
    ADD CONSTRAINT uniq_taxonrank UNIQUE (taxonrank);
 F   ALTER TABLE ONLY public.tlkptaxonrank DROP CONSTRAINT uniq_taxonrank;
       public            postgres    false    368            �           2606    66200    tlkpdomain uniq_tlkpdomain 
   CONSTRAINT     W   ALTER TABLE ONLY public.tlkpdomain
    ADD CONSTRAINT uniq_tlkpdomain UNIQUE (domain);
 D   ALTER TABLE ONLY public.tlkpdomain DROP CONSTRAINT uniq_tlkpdomain;
       public            tellervo    false    334            �           2606    66202 &   tbltruncate uniq_truncate_vmeasurement 
   CONSTRAINT     k   ALTER TABLE ONLY public.tbltruncate
    ADD CONSTRAINT uniq_truncate_vmeasurement UNIQUE (vmeasurementid);
 P   ALTER TABLE ONLY public.tbltruncate DROP CONSTRAINT uniq_truncate_vmeasurement;
       public            postgres    false    303            �           2606    66204    tlkpunit uniq_unit 
   CONSTRAINT     M   ALTER TABLE ONLY public.tlkpunit
    ADD CONSTRAINT uniq_unit UNIQUE (unit);
 <   ALTER TABLE ONLY public.tlkpunit DROP CONSTRAINT uniq_unit;
       public            postgres    false    369            �           2606    66206 7   tbluserdefinedfieldvalue uniq_userdefinedfieldperentity 
   CONSTRAINT     �   ALTER TABLE ONLY public.tbluserdefinedfieldvalue
    ADD CONSTRAINT uniq_userdefinedfieldperentity UNIQUE (userdefinedfieldid, entityid);
 a   ALTER TABLE ONLY public.tbluserdefinedfieldvalue DROP CONSTRAINT uniq_userdefinedfieldperentity;
       public            tellervo    false    307    307            �           2606    66208 5   tlkpuserdefinedfield uniq_userdefinedfields_fieldname 
   CONSTRAINT     �   ALTER TABLE ONLY public.tlkpuserdefinedfield
    ADD CONSTRAINT uniq_userdefinedfields_fieldname UNIQUE (fieldname, attachedto);
 _   ALTER TABLE ONLY public.tlkpuserdefinedfield DROP CONSTRAINT uniq_userdefinedfields_fieldname;
       public            tellervo    false    371    371            �           2606    66210 .   tlkpuserdefinedterm uniq_userdefinedtermindict 
   CONSTRAINT     x   ALTER TABLE ONLY public.tlkpuserdefinedterm
    ADD CONSTRAINT uniq_userdefinedtermindict UNIQUE (term, dictionarykey);
 X   ALTER TABLE ONLY public.tlkpuserdefinedterm DROP CONSTRAINT uniq_userdefinedtermindict;
       public            tellervo    false    372    372            #           2606    66212    tblcrossdate uniq_vmeasurement 
   CONSTRAINT     c   ALTER TABLE ONLY public.tblcrossdate
    ADD CONSTRAINT uniq_vmeasurement UNIQUE (vmeasurementid);
 H   ALTER TABLE ONLY public.tblcrossdate DROP CONSTRAINT uniq_vmeasurement;
       public            postgres    false    249            �           2606    66214 3   tblvmeasurementgroup uniq_vmeasurementgroup_members 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementgroup
    ADD CONSTRAINT uniq_vmeasurementgroup_members UNIQUE (membervmeasurementid, vmeasurementid);
 ]   ALTER TABLE ONLY public.tblvmeasurementgroup DROP CONSTRAINT uniq_vmeasurementgroup_members;
       public            postgres    false    311    311                        2606    66216 +   tlkpvmeasurementop uniq_vmeasurementop_name 
   CONSTRAINT     f   ALTER TABLE ONLY public.tlkpvmeasurementop
    ADD CONSTRAINT uniq_vmeasurementop_name UNIQUE (name);
 U   ALTER TABLE ONLY public.tlkpvmeasurementop DROP CONSTRAINT uniq_vmeasurementop_name;
       public            postgres    false    373            �           2606    66218 !   tlkpsamplestatus uniqsamplestatus 
   CONSTRAINT     d   ALTER TABLE ONLY public.tlkpsamplestatus
    ADD CONSTRAINT uniqsamplestatus UNIQUE (samplestatus);
 K   ALTER TABLE ONLY public.tlkpsamplestatus DROP CONSTRAINT uniqsamplestatus;
       public            tellervo    false    360                       2606    66220    tblelement unique_gispkey 
   CONSTRAINT     W   ALTER TABLE ONLY public.tblelement
    ADD CONSTRAINT unique_gispkey UNIQUE (gispkey);
 C   ALTER TABLE ONLY public.tblelement DROP CONSTRAINT unique_gispkey;
       public            postgres    false    236            �           2606    66222 %   tblobject unique_objectcode_perparent 
   CONSTRAINT     p   ALTER TABLE ONLY public.tblobject
    ADD CONSTRAINT unique_objectcode_perparent UNIQUE (code, parentobjectid);
 O   ALTER TABLE ONLY public.tblobject DROP CONSTRAINT unique_objectcode_perparent;
       public            postgres    false    231    231            �           2606    66224    tblobject unique_parent-title 
   CONSTRAINT     k   ALTER TABLE ONLY public.tblobject
    ADD CONSTRAINT "unique_parent-title" UNIQUE (parentobjectid, title);
 I   ALTER TABLE ONLY public.tblobject DROP CONSTRAINT "unique_parent-title";
       public            postgres    false    231    231                       2606    66226 #   tblsample unique_parentelement-code 
   CONSTRAINT     k   ALTER TABLE ONLY public.tblsample
    ADD CONSTRAINT "unique_parentelement-code" UNIQUE (elementid, code);
 O   ALTER TABLE ONLY public.tblsample DROP CONSTRAINT "unique_parentelement-code";
       public            postgres    false    240    240                       2606    66228 #   tblelement unique_parentobject-code 
   CONSTRAINT     j   ALTER TABLE ONLY public.tblelement
    ADD CONSTRAINT "unique_parentobject-code" UNIQUE (objectid, code);
 O   ALTER TABLE ONLY public.tblelement DROP CONSTRAINT "unique_parentobject-code";
       public            postgres    false    236    236                       2606    66230 "   tblradius unique_parentsample-code 
   CONSTRAINT     i   ALTER TABLE ONLY public.tblradius
    ADD CONSTRAINT "unique_parentsample-code" UNIQUE (sampleid, code);
 N   ALTER TABLE ONLY public.tblradius DROP CONSTRAINT "unique_parentsample-code";
       public            postgres    false    239    239            �           2606    66232    tblvmeasurement vmeasurementid 
   CONSTRAINT     h   ALTER TABLE ONLY public.tblvmeasurement
    ADD CONSTRAINT vmeasurementid PRIMARY KEY (vmeasurementid);
 H   ALTER TABLE ONLY public.tblvmeasurement DROP CONSTRAINT vmeasurementid;
       public            postgres    false    308                       2606    66234    tlkpwmsserver wmsserver-pkey 
   CONSTRAINT     e   ALTER TABLE ONLY public.tlkpwmsserver
    ADD CONSTRAINT "wmsserver-pkey" PRIMARY KEY (wmsserverid);
 H   ALTER TABLE ONLY public.tlkpwmsserver DROP CONSTRAINT "wmsserver-pkey";
       public            postgres    false    377                       2606    66236     tlkpwmsserver wmsserver-uniqname 
   CONSTRAINT     ]   ALTER TABLE ONLY public.tlkpwmsserver
    ADD CONSTRAINT "wmsserver-uniqname" UNIQUE (name);
 L   ALTER TABLE ONLY public.tlkpwmsserver DROP CONSTRAINT "wmsserver-uniqname";
       public            postgres    false    377                       2606    66238    tlkpwmsserver wmsserver-uniqurl 
   CONSTRAINT     [   ALTER TABLE ONLY public.tlkpwmsserver
    ADD CONSTRAINT "wmsserver-uniqurl" UNIQUE (url);
 K   ALTER TABLE ONLY public.tlkpwmsserver DROP CONSTRAINT "wmsserver-uniqurl";
       public            postgres    false    377            
           2606    66240    yenikapi yenikapi_pkey 
   CONSTRAINT     U   ALTER TABLE ONLY public.yenikapi
    ADD CONSTRAINT yenikapi_pkey PRIMARY KEY (gid);
 @   ALTER TABLE ONLY public.yenikapi DROP CONSTRAINT yenikapi_pkey;
       public            postgres    false    410            �           1259    66241    fki_fkey_element_units    INDEX     N   CREATE INDEX fki_fkey_element_units ON public.tblelement USING btree (units);
 *   DROP INDEX public.fki_fkey_element_units;
       public            postgres    false    236            �           1259    66242     idx_standardisiedid-vocabularyid    INDEX     }   CREATE UNIQUE INDEX "idx_standardisiedid-vocabularyid" ON public.tlkpreadingnote USING btree (standardisedid, vocabularyid);
 6   DROP INDEX public."idx_standardisiedid-vocabularyid";
       public            postgres    false    232    232            *           1259    66243    ind_environmentaldata-elementid    INDEX     g   CREATE INDEX "ind_environmentaldata-elementid" ON public.tblenvironmentaldata USING btree (elementid);
 5   DROP INDEX public."ind_environmentaldata-elementid";
       public            postgres    false    259            +           1259    66244 #   ind_environmentaldata-rasterlayerid    INDEX     o   CREATE INDEX "ind_environmentaldata-rasterlayerid" ON public.tblenvironmentaldata USING btree (rasterlayerid);
 9   DROP INDEX public."ind_environmentaldata-rasterlayerid";
       public            postgres    false    259            �           1259    66245    ind_measurement-datingtype    INDEX     _   CREATE INDEX "ind_measurement-datingtype" ON public.tblmeasurement USING btree (datingtypeid);
 0   DROP INDEX public."ind_measurement-datingtype";
       public            postgres    false    234            �           1259    66246    ind_measurement-radius    INDEX     W   CREATE INDEX "ind_measurement-radius" ON public.tblmeasurement USING btree (radiusid);
 ,   DROP INDEX public."ind_measurement-radius";
       public            postgres    false    234            6           1259    66247    ind_objectregion-object    INDEX     Y   CREATE INDEX "ind_objectregion-object" ON public.tblobjectregion USING btree (objectid);
 -   DROP INDEX public."ind_objectregion-object";
       public            postgres    false    265            7           1259    66248    ind_objectregion-region    INDEX     Y   CREATE INDEX "ind_objectregion-region" ON public.tblobjectregion USING btree (regionid);
 -   DROP INDEX public."ind_objectregion-region";
       public            postgres    false    265            
           1259    66249    ind_radius-sample    INDEX     M   CREATE INDEX "ind_radius-sample" ON public.tblradius USING btree (sampleid);
 '   DROP INDEX public."ind_radius-sample";
       public            postgres    false    239            L           1259    66250    ind_reading-measurement    INDEX     Y   CREATE INDEX "ind_reading-measurement" ON public.tblreading USING btree (measurementid);
 -   DROP INDEX public."ind_reading-measurement";
       public            postgres    false    275            O           1259    66251    ind_readingreadingnote-reading    INDEX     g   CREATE INDEX "ind_readingreadingnote-reading" ON public.tblreadingreadingnote USING btree (readingid);
 4   DROP INDEX public."ind_readingreadingnote-reading";
       public            postgres    false    277            P           1259    66252 "   ind_readingreadingnote-readingnote    INDEX     o   CREATE INDEX "ind_readingreadingnote-readingnote" ON public.tblreadingreadingnote USING btree (readingnoteid);
 8   DROP INDEX public."ind_readingreadingnote-readingnote";
       public            postgres    false    277            �           1259    66253    ind_result-relyear-noteid    INDEX     �   CREATE INDEX "ind_result-relyear-noteid" ON public.tblvmeasurementreadingnoteresult USING btree (vmeasurementresultid, relyear, readingnoteid);
 /   DROP INDEX public."ind_result-relyear-noteid";
       public            postgres    false    314    314    314                       1259    66254    ind_sample-sampletype    INDEX     M   CREATE INDEX "ind_sample-sampletype" ON public.tblsample USING btree (type);
 +   DROP INDEX public."ind_sample-sampletype";
       public            postgres    false    240                       1259    66255    ind_sample-tree    INDEX     L   CREATE INDEX "ind_sample-tree" ON public.tblsample USING btree (elementid);
 %   DROP INDEX public."ind_sample-tree";
       public            postgres    false    240            ^           1259    66256 !   ind_securitydefault-securitygroup    INDEX     m   CREATE INDEX "ind_securitydefault-securitygroup" ON public.tblsecuritydefault USING btree (securitygroupid);
 7   DROP INDEX public."ind_securitydefault-securitygroup";
       public            postgres    false    286            _           1259    66257 &   ind_securitydefault-securitypermission    INDEX     w   CREATE INDEX "ind_securitydefault-securitypermission" ON public.tblsecuritydefault USING btree (securitypermissionid);
 <   DROP INDEX public."ind_securitydefault-securitypermission";
       public            postgres    false    286            k           1259    66258 /   ind_securitygroupmembereship-childsecuritygroup    INDEX     �   CREATE INDEX "ind_securitygroupmembereship-childsecuritygroup" ON public.tblsecuritygroupmembership USING btree (childsecuritygroupid);
 E   DROP INDEX public."ind_securitygroupmembereship-childsecuritygroup";
       public            postgres    false    290            l           1259    66259 /   ind_securitygroupmembership-parentsecuritygroup    INDEX     �   CREATE INDEX "ind_securitygroupmembership-parentsecuritygroup" ON public.tblsecuritygroupmembership USING btree (parentsecuritygroupid);
 E   DROP INDEX public."ind_securitygroupmembership-parentsecuritygroup";
       public            postgres    false    290            q           1259    66260     ind_securityobject-securitygroup    INDEX     k   CREATE INDEX "ind_securityobject-securitygroup" ON public.tblsecurityobject USING btree (securitygroupid);
 6   DROP INDEX public."ind_securityobject-securitygroup";
       public            postgres    false    292            r           1259    66261 %   ind_securityobject-securitypermission    INDEX     u   CREATE INDEX "ind_securityobject-securitypermission" ON public.tblsecurityobject USING btree (securitypermissionid);
 ;   DROP INDEX public."ind_securityobject-securitypermission";
       public            postgres    false    292            d           1259    66262    ind_securitytree-securitygroup    INDEX     j   CREATE INDEX "ind_securitytree-securitygroup" ON public.tblsecurityelement USING btree (securitygroupid);
 4   DROP INDEX public."ind_securitytree-securitygroup";
       public            postgres    false    288            e           1259    66263 #   ind_securitytree-securitypermission    INDEX     t   CREATE INDEX "ind_securitytree-securitypermission" ON public.tblsecurityelement USING btree (securitypermissionid);
 9   DROP INDEX public."ind_securitytree-securitypermission";
       public            postgres    false    288            f           1259    66264    ind_securitytree-tree    INDEX     [   CREATE INDEX "ind_securitytree-tree" ON public.tblsecurityelement USING btree (elementid);
 +   DROP INDEX public."ind_securitytree-tree";
       public            postgres    false    288            {           1259    66265 (   ind_securityusermembership-securitygroup    INDEX     {   CREATE INDEX "ind_securityusermembership-securitygroup" ON public.tblsecurityusermembership USING btree (securitygroupid);
 >   DROP INDEX public."ind_securityusermembership-securitygroup";
       public            postgres    false    296            ~           1259    66266 &   ind_securityvmeasurement-securitygroup    INDEX     w   CREATE INDEX "ind_securityvmeasurement-securitygroup" ON public.tblsecurityvmeasurement USING btree (securitygroupid);
 <   DROP INDEX public."ind_securityvmeasurement-securitygroup";
       public            postgres    false    298                       1259    66267 +   ind_securityvmeasurement-securitypermission    INDEX     �   CREATE INDEX "ind_securityvmeasurement-securitypermission" ON public.tblsecurityvmeasurement USING btree (securitypermissionid);
 A   DROP INDEX public."ind_securityvmeasurement-securitypermission";
       public            postgres    false    298            �           1259    66268 %   ind_securityvmeasurement-vmeasurement    INDEX     u   CREATE INDEX "ind_securityvmeasurement-vmeasurement" ON public.tblsecurityvmeasurement USING btree (vmeasurementid);
 ;   DROP INDEX public."ind_securityvmeasurement-vmeasurement";
       public            postgres    false    298            �           1259    66269    ind_taxon-taxonrank    INDEX     R   CREATE INDEX "ind_taxon-taxonrank" ON public.tlkptaxon USING btree (taxonrankid);
 )   DROP INDEX public."ind_taxon-taxonrank";
       public            postgres    false    367            �           1259    66270    ind_vmeasurement-measurement    INDEX     c   CREATE INDEX "ind_vmeasurement-measurement" ON public.tblvmeasurement USING btree (measurementid);
 2   DROP INDEX public."ind_vmeasurement-measurement";
       public            postgres    false    308            �           1259    66271    ind_vmeasurement-vmeasurementop    INDEX     i   CREATE INDEX "ind_vmeasurement-vmeasurementop" ON public.tblvmeasurement USING btree (vmeasurementopid);
 5   DROP INDEX public."ind_vmeasurement-vmeasurementop";
       public            postgres    false    308            �           1259    66272 (   ind_vmeasurementderivedcache-measurement    INDEX     {   CREATE INDEX "ind_vmeasurementderivedcache-measurement" ON public.tblvmeasurementderivedcache USING btree (measurementid);
 >   DROP INDEX public."ind_vmeasurementderivedcache-measurement";
       public            postgres    false    235            �           1259    66273 )   ind_vmeasurementderivedcache-vmeasurement    INDEX     }   CREATE INDEX "ind_vmeasurementderivedcache-vmeasurement" ON public.tblvmeasurementderivedcache USING btree (vmeasurementid);
 ?   DROP INDEX public."ind_vmeasurementderivedcache-vmeasurement";
       public            postgres    false    235            �           1259    66274 #   ind_vmeasurementgroup-vmeasurement1    INDEX     p   CREATE INDEX "ind_vmeasurementgroup-vmeasurement1" ON public.tblvmeasurementgroup USING btree (vmeasurementid);
 9   DROP INDEX public."ind_vmeasurementgroup-vmeasurement1";
       public            postgres    false    311            �           1259    66275 #   ind_vmeasurementgroup-vmeasurement2    INDEX     v   CREATE INDEX "ind_vmeasurementgroup-vmeasurement2" ON public.tblvmeasurementgroup USING btree (membervmeasurementid);
 9   DROP INDEX public."ind_vmeasurementgroup-vmeasurement2";
       public            postgres    false    311            �           1259    66276 &   ind_vmeasurementmetacache-vmeasurement    INDEX     w   CREATE INDEX "ind_vmeasurementmetacache-vmeasurement" ON public.tblvmeasurementmetacache USING btree (vmeasurementid);
 <   DROP INDEX public."ind_vmeasurementmetacache-vmeasurement";
       public            postgres    false    233                       1259    66277 8   ind_vmeasurementreadingresult-vmeasurementresult_relyear    INDEX     �   CREATE UNIQUE INDEX "ind_vmeasurementreadingresult-vmeasurementresult_relyear" ON public.tblvmeasurementreadingresult USING btree (vmeasurementresultid, relyear);
 N   DROP INDEX public."ind_vmeasurementreadingresult-vmeasurementresult_relyear";
       public            postgres    false    241    241            �           1259    66278 '   ind_vmrelyearreadingnote-vmeasurementid    INDEX     �   CREATE INDEX "ind_vmrelyearreadingnote-vmeasurementid" ON public.tblvmeasurementrelyearreadingnote USING btree (vmeasurementid);
 =   DROP INDEX public."ind_vmrelyearreadingnote-vmeasurementid";
       public            postgres    false    317            �           1259    66279    index_globaltags    INDEX     a   CREATE UNIQUE INDEX index_globaltags ON public.tbltag USING btree (tag) WHERE (ownerid IS NULL);
 $   DROP INDEX public.index_globaltags;
       public            tellervo    false    302    302            �           1259    66280    index_privatetags    INDEX     o   CREATE UNIQUE INDEX index_privatetags ON public.tbltag USING btree (tag, ownerid) WHERE (ownerid IS NOT NULL);
 %   DROP INDEX public.index_privatetags;
       public            tellervo    false    302    302    302            �           1259    66281    parent_object_index    INDEX     S   CREATE INDEX parent_object_index ON public.tblobject USING btree (parentobjectid);
 '   DROP INDEX public.parent_object_index;
       public            postgres    false    231            �           1259    66282    pkey_indexname    INDEX     T   CREATE UNIQUE INDEX pkey_indexname ON public.tlkpindextype USING btree (indexname);
 "   DROP INDEX public.pkey_indexname;
       public            postgres    false    342            �           1259    66283    postgis_object_extent    INDEX     �   CREATE INDEX postgis_object_extent ON public.tblobject USING gist (locationgeometry);

ALTER TABLE public.tblobject CLUSTER ON postgis_object_extent;
 )   DROP INDEX public.postgis_object_extent;
       public            postgres    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    231            Y           1259    66284    postgis_region_the_geom    INDEX     �   CREATE INDEX postgis_region_the_geom ON public.tblregion USING gist (the_geom);

ALTER TABLE public.tblregion CLUSTER ON postgis_region_the_geom;
 +   DROP INDEX public.postgis_region_the_geom;
       public            postgres    false    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    3    281            :           2618    66285    tblsecuritygroup protectadmin    RULE     |   CREATE RULE protectadmin AS
    ON UPDATE TO public.tblsecuritygroup
   WHERE (old.securitygroupid = 1) DO INSTEAD NOTHING;
 3   DROP RULE protectadmin ON public.tblsecuritygroup;
       public          postgres    false    237    237    237    237            y           2620    66286 &   tblsecuritygroup checkGroupIsDeletable    TRIGGER     �   CREATE TRIGGER "checkGroupIsDeletable" BEFORE DELETE ON public.tblsecuritygroup FOR EACH ROW EXECUTE FUNCTION public."checkGroupIsDeletable"();
 A   DROP TRIGGER "checkGroupIsDeletable" ON public.tblsecuritygroup;
       public          postgres    false    237    1368            t           2620    66287 ,   tblmeasurement check_measurementdatingerrors    TRIGGER     �   CREATE TRIGGER check_measurementdatingerrors BEFORE INSERT OR UPDATE ON public.tblmeasurement FOR EACH ROW EXECUTE FUNCTION public.check_datingerrors();
 E   DROP TRIGGER check_measurementdatingerrors ON public.tblmeasurement;
       public          postgres    false    234    1369            �           2620    66288 5   tblvmeasurementrelyearreadingnote check_vm_is_derived    TRIGGER     �   CREATE TRIGGER check_vm_is_derived BEFORE INSERT OR UPDATE ON public.tblvmeasurementrelyearreadingnote FOR EACH ROW EXECUTE FUNCTION cpgdb.vmeasurementrelyearnotetrigger();
 N   DROP TRIGGER check_vm_is_derived ON public.tblvmeasurementrelyearreadingnote;
       public          postgres    false    317    1315            s           2620    66289 &   tlkpreadingnote default_standardisedid    TRIGGER     �   CREATE TRIGGER default_standardisedid BEFORE INSERT OR UPDATE ON public.tlkpreadingnote FOR EACH ROW EXECUTE FUNCTION public.create_defaultreadingnotestandardisedid();
 ?   DROP TRIGGER default_standardisedid ON public.tlkpreadingnote;
       public          postgres    false    232    1380            �           2620    66290 +   tblsecurityuser enforce_atleastoneadminuser    TRIGGER     �   CREATE TRIGGER enforce_atleastoneadminuser AFTER UPDATE ON public.tblsecurityuser FOR EACH ROW EXECUTE FUNCTION public.enforce_atleastoneadminuserupdate();
 D   DROP TRIGGER enforce_atleastoneadminuser ON public.tblsecurityuser;
       public          postgres    false    1406    295            �           2620    66291 5   tblsecurityusermembership enforce_atleastoneadminuser    TRIGGER     �   CREATE TRIGGER enforce_atleastoneadminuser AFTER UPDATE ON public.tblsecurityusermembership FOR EACH ROW EXECUTE FUNCTION public.enforce_atleastoneadminuserupdate();
 N   DROP TRIGGER enforce_atleastoneadminuser ON public.tblsecurityusermembership;
       public          postgres    false    1406    296            �           2620    66292 .   tblsecurityuser enforce_atleastoneadminuserdel    TRIGGER     �   CREATE TRIGGER enforce_atleastoneadminuserdel BEFORE DELETE ON public.tblsecurityuser FOR EACH ROW EXECUTE FUNCTION public.enforce_atleastoneadminuserdelete();
 G   DROP TRIGGER enforce_atleastoneadminuserdel ON public.tblsecurityuser;
       public          postgres    false    295    1405            �           2620    66293 8   tblsecurityusermembership enforce_atleastoneadminuserdel    TRIGGER     �   CREATE TRIGGER enforce_atleastoneadminuserdel BEFORE DELETE ON public.tblsecurityusermembership FOR EACH ROW EXECUTE FUNCTION public.enforce_atleastoneadminuserdelete();
 Q   DROP TRIGGER enforce_atleastoneadminuserdel ON public.tblsecurityusermembership;
       public          postgres    false    296    1405            �           2620    66294 +   tblsecuritydefault enforce_noadminpermedits    TRIGGER     �   CREATE TRIGGER enforce_noadminpermedits AFTER INSERT OR UPDATE ON public.tblsecuritydefault FOR EACH ROW EXECUTE FUNCTION public.enforce_noadminpermeditsonupdatecreate();
 D   DROP TRIGGER enforce_noadminpermedits ON public.tblsecuritydefault;
       public          postgres    false    286    1410            �           2620    66295 +   tblsecurityelement enforce_noadminpermedits    TRIGGER     �   CREATE TRIGGER enforce_noadminpermedits AFTER INSERT OR UPDATE ON public.tblsecurityelement FOR EACH ROW EXECUTE FUNCTION public.enforce_noadminpermeditsonupdatecreate();
 D   DROP TRIGGER enforce_noadminpermedits ON public.tblsecurityelement;
       public          postgres    false    288    1410            �           2620    66296 *   tblsecurityobject enforce_noadminpermedits    TRIGGER     �   CREATE TRIGGER enforce_noadminpermedits AFTER INSERT OR UPDATE ON public.tblsecurityobject FOR EACH ROW EXECUTE FUNCTION public.enforce_noadminpermeditsonupdatecreate();
 C   DROP TRIGGER enforce_noadminpermedits ON public.tblsecurityobject;
       public          postgres    false    1410    292            �           2620    66297 0   tblsecurityvmeasurement enforce_noadminpermedits    TRIGGER     �   CREATE TRIGGER enforce_noadminpermedits AFTER INSERT OR UPDATE ON public.tblsecurityvmeasurement FOR EACH ROW EXECUTE FUNCTION public.enforce_noadminpermeditsonupdatecreate();
 I   DROP TRIGGER enforce_noadminpermedits ON public.tblsecurityvmeasurement;
       public          postgres    false    1410    298            �           2620    66298 .   tblsecuritydefault enforce_noadminpermeditsdel    TRIGGER     �   CREATE TRIGGER enforce_noadminpermeditsdel BEFORE DELETE ON public.tblsecuritydefault FOR EACH ROW EXECUTE FUNCTION public.enforce_noadminpermeditsondelete();
 G   DROP TRIGGER enforce_noadminpermeditsdel ON public.tblsecuritydefault;
       public          postgres    false    286    1409            �           2620    66299 .   tblsecurityelement enforce_noadminpermeditsdel    TRIGGER     �   CREATE TRIGGER enforce_noadminpermeditsdel BEFORE DELETE ON public.tblsecurityelement FOR EACH ROW EXECUTE FUNCTION public.enforce_noadminpermeditsondelete();
 G   DROP TRIGGER enforce_noadminpermeditsdel ON public.tblsecurityelement;
       public          postgres    false    288    1409            �           2620    66300 -   tblsecurityobject enforce_noadminpermeditsdel    TRIGGER     �   CREATE TRIGGER enforce_noadminpermeditsdel BEFORE DELETE ON public.tblsecurityobject FOR EACH ROW EXECUTE FUNCTION public.enforce_noadminpermeditsondelete();
 F   DROP TRIGGER enforce_noadminpermeditsdel ON public.tblsecurityobject;
       public          postgres    false    292    1409            �           2620    66301 3   tblsecurityvmeasurement enforce_noadminpermeditsdel    TRIGGER     �   CREATE TRIGGER enforce_noadminpermeditsdel BEFORE DELETE ON public.tblsecurityvmeasurement FOR EACH ROW EXECUTE FUNCTION public.enforce_noadminpermeditsondelete();
 L   DROP TRIGGER enforce_noadminpermeditsdel ON public.tblsecurityvmeasurement;
       public          postgres    false    298    1409            p           2620    66302    tblobject enforce_object-parent    TRIGGER     �   CREATE TRIGGER "enforce_object-parent" BEFORE INSERT OR UPDATE ON public.tblobject FOR EACH ROW EXECUTE FUNCTION public."enforce_object-parent"();
 :   DROP TRIGGER "enforce_object-parent" ON public.tblobject;
       public          postgres    false    1411    231            ~           2620    66303 (   tblcurationevent trig_loanid_when_loaned    TRIGGER     �   CREATE TRIGGER trig_loanid_when_loaned BEFORE INSERT OR UPDATE ON public.tblcurationevent FOR EACH ROW EXECUTE FUNCTION public.check_tblcuration_loanid_is_not_null_when_loaned();
 A   DROP TRIGGER trig_loanid_when_loaned ON public.tblcurationevent;
       public          tellervo    false    251    1370                       2620    66304 &   tblcurationevent trig_no_status_update    TRIGGER     �   CREATE TRIGGER trig_no_status_update BEFORE UPDATE ON public.tblcurationevent FOR EACH ROW EXECUTE FUNCTION public.enforce_no_status_update_on_curation();
 ?   DROP TRIGGER trig_no_status_update ON public.tblcurationevent;
       public          tellervo    false    1408    251            �           2620    66305 "   tblcurationevent trig_nodoubleloan    TRIGGER     �   CREATE TRIGGER trig_nodoubleloan BEFORE INSERT ON public.tblcurationevent FOR EACH ROW EXECUTE FUNCTION public.enforce_no_loan_when_on_loan();
 ;   DROP TRIGGER trig_nodoubleloan ON public.tblcurationevent;
       public          tellervo    false    1407    251            �           2620    66306 7   tblodkdefinition trigger_updateodkformversion-increment    TRIGGER     �   CREATE TRIGGER "trigger_updateodkformversion-increment" BEFORE UPDATE ON public.tblodkdefinition FOR EACH ROW EXECUTE FUNCTION public."update_odkformdef-versionincrement"();
 R   DROP TRIGGER "trigger_updateodkformversion-increment" ON public.tblodkdefinition;
       public          tellervo    false    267    1713            x           2620    66843 *   tblelement update_element_rebuildmetacache    TRIGGER     �   CREATE TRIGGER update_element_rebuildmetacache AFTER INSERT OR UPDATE ON public.tblelement FOR EACH ROW EXECUTE FUNCTION cpgdb.rebuildmetacacheforelement();
 C   DROP TRIGGER update_element_rebuildmetacache ON public.tblelement;
       public          postgres    false    1293    236            �           2620    66308 "   tblrasterlayer update_layerenvdata    TRIGGER     �   CREATE TRIGGER update_layerenvdata AFTER INSERT OR UPDATE ON public.tblrasterlayer FOR EACH ROW EXECUTE FUNCTION public.update_layerenvdata();
 ;   DROP TRIGGER update_layerenvdata ON public.tblrasterlayer;
       public          postgres    false    1711    273            u           2620    66309 ?   tblmeasurement update_measurement_lastmodifiedtimestamp_trigger    TRIGGER     �   CREATE TRIGGER update_measurement_lastmodifiedtimestamp_trigger BEFORE UPDATE ON public.tblmeasurement FOR EACH ROW EXECUTE FUNCTION public.update_lastmodifiedtimestamp();
 X   DROP TRIGGER update_measurement_lastmodifiedtimestamp_trigger ON public.tblmeasurement;
       public          postgres    false    1710    234            q           2620    66310 5   tblobject update_object_lastmodifiedtimestamp_trigger    TRIGGER     �   CREATE TRIGGER update_object_lastmodifiedtimestamp_trigger BEFORE UPDATE ON public.tblobject FOR EACH ROW EXECUTE FUNCTION public.update_lastmodifiedtimestamp();
 N   DROP TRIGGER update_object_lastmodifiedtimestamp_trigger ON public.tblobject;
       public          postgres    false    1710    231            r           2620    66311 (   tblobject update_object_rebuildmetacache    TRIGGER     �   CREATE TRIGGER update_object_rebuildmetacache AFTER INSERT OR UPDATE ON public.tblobject FOR EACH ROW EXECUTE FUNCTION cpgdb.rebuildmetacacheforobject();
 A   DROP TRIGGER update_object_rebuildmetacache ON public.tblobject;
       public          postgres    false    231    1294            �           2620    66312 %   tblregion update_objectregion_trigger    TRIGGER     �   CREATE TRIGGER update_objectregion_trigger AFTER INSERT OR UPDATE ON public.tblregion FOR EACH ROW EXECUTE FUNCTION cpgdb.objectregionmodified();
 >   DROP TRIGGER update_objectregion_trigger ON public.tblregion;
       public          postgres    false    1285    281            z           2620    66313 5   tblradius update_radius_lastmodifiedtimestamp_trigger    TRIGGER     �   CREATE TRIGGER update_radius_lastmodifiedtimestamp_trigger BEFORE UPDATE ON public.tblradius FOR EACH ROW EXECUTE FUNCTION public.update_lastmodifiedtimestamp();
 N   DROP TRIGGER update_radius_lastmodifiedtimestamp_trigger ON public.tblradius;
       public          postgres    false    1710    239            {           2620    66314 (   tblradius update_radius_rebuildmetacache    TRIGGER     �   CREATE TRIGGER update_radius_rebuildmetacache AFTER INSERT OR UPDATE ON public.tblradius FOR EACH ROW EXECUTE FUNCTION cpgdb.rebuildmetacacheforradius();
 A   DROP TRIGGER update_radius_rebuildmetacache ON public.tblradius;
       public          postgres    false    1295    239            |           2620    66315 -   tblsample update_sample-lastmodifiedtimestamp    TRIGGER     �   CREATE TRIGGER "update_sample-lastmodifiedtimestamp" BEFORE UPDATE ON public.tblsample FOR EACH ROW EXECUTE FUNCTION public.update_lastmodifiedtimestamp();
 H   DROP TRIGGER "update_sample-lastmodifiedtimestamp" ON public.tblsample;
       public          postgres    false    1710    240            }           2620    66316 (   tblsample update_sample_rebuildmetacache    TRIGGER     �   CREATE TRIGGER update_sample_rebuildmetacache AFTER INSERT OR UPDATE ON public.tblsample FOR EACH ROW EXECUTE FUNCTION cpgdb.rebuildmetacacheforsample();
 A   DROP TRIGGER update_sample_rebuildmetacache ON public.tblsample;
       public          postgres    false    1296    240            v           2620    66317 4   tblelement update_tree_lastmodifiedtimestamp_trigger    TRIGGER     �   CREATE TRIGGER update_tree_lastmodifiedtimestamp_trigger BEFORE UPDATE ON public.tblelement FOR EACH ROW EXECUTE FUNCTION public.update_lastmodifiedtimestamp();
 M   DROP TRIGGER update_tree_lastmodifiedtimestamp_trigger ON public.tblelement;
       public          postgres    false    236    1710            �           2620    66318 #   tblvmeasurement update_vmeasurement    TRIGGER     �   CREATE TRIGGER update_vmeasurement AFTER INSERT OR UPDATE ON public.tblvmeasurement FOR EACH ROW EXECUTE FUNCTION cpgdb.vmeasurementmodifiedtrigger();
 <   DROP TRIGGER update_vmeasurement ON public.tblvmeasurement;
       public          postgres    false    1314    308            �           2620    66319 A   tblvmeasurement update_vmeasurement_lastmodifiedtimestamp_trigger    TRIGGER     �   CREATE TRIGGER update_vmeasurement_lastmodifiedtimestamp_trigger BEFORE UPDATE ON public.tblvmeasurement FOR EACH ROW EXECUTE FUNCTION public.update_lastmodifiedtimestamp();
 Z   DROP TRIGGER update_vmeasurement_lastmodifiedtimestamp_trigger ON public.tblvmeasurement;
       public          postgres    false    1710    308            w           2620    66842 +   tblelement update_vmeasurementcache_extents    TRIGGER     �   CREATE TRIGGER update_vmeasurementcache_extents AFTER UPDATE ON public.tblelement FOR EACH ROW EXECUTE FUNCTION cpgdb.elementlocationchangedtrigger();
 D   DROP TRIGGER update_vmeasurementcache_extents ON public.tblelement;
       public          postgres    false    236    1221            �           2620    66321 M   tblvmeasurementresult update_vmeasurementresult_lastmodifiedtimestamp_trigger    TRIGGER     �   CREATE TRIGGER update_vmeasurementresult_lastmodifiedtimestamp_trigger BEFORE UPDATE ON public.tblvmeasurementresult FOR EACH ROW EXECUTE FUNCTION public.update_lastmodifiedtimestamp();
 f   DROP TRIGGER update_vmeasurementresult_lastmodifiedtimestamp_trigger ON public.tblvmeasurementresult;
       public          postgres    false    318    1710                       2606    66322 +   tblelement fkey_element-elementauthenticity    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblelement
    ADD CONSTRAINT "fkey_element-elementauthenticity" FOREIGN KEY (elementauthenticityid) REFERENCES public.tlkpelementauthenticity(elementauthenticityid);
 W   ALTER TABLE ONLY public.tblelement DROP CONSTRAINT "fkey_element-elementauthenticity";
       public          postgres    false    236    336    5568                        2606    66327 $   tblelement fkey_element-elementshape    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblelement
    ADD CONSTRAINT "fkey_element-elementshape" FOREIGN KEY (elementshapeid) REFERENCES public.tlkpelementshape(elementshapeid);
 P   ALTER TABLE ONLY public.tblelement DROP CONSTRAINT "fkey_element-elementshape";
       public          postgres    false    236    338    5570            !           2606    66332 #   tblelement fkey_element-elementtype    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblelement
    ADD CONSTRAINT "fkey_element-elementtype" FOREIGN KEY (elementtypeid) REFERENCES public.tlkpelementtype(elementtypeid);
 O   ALTER TABLE ONLY public.tblelement DROP CONSTRAINT "fkey_element-elementtype";
       public          postgres    false    340    5572    236            "           2606    66337 $   tblelement fkey_element-locationtype    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblelement
    ADD CONSTRAINT "fkey_element-locationtype" FOREIGN KEY (locationtypeid) REFERENCES public.tlkplocationtype(locationtypeid);
 P   ALTER TABLE ONLY public.tblelement DROP CONSTRAINT "fkey_element-locationtype";
       public          postgres    false    344    5577    236            &           2606    66850    tblelement fkey_element_taxon    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblelement
    ADD CONSTRAINT fkey_element_taxon FOREIGN KEY (taxonid) REFERENCES public.tlkptaxon(taxonid);
 G   ALTER TABLE ONLY public.tblelement DROP CONSTRAINT fkey_element_taxon;
       public          postgres    false    367    236    5608            #           2606    66342    tblelement fkey_element_units    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblelement
    ADD CONSTRAINT fkey_element_units FOREIGN KEY (units) REFERENCES public.tlkpunit(unitid) ON UPDATE RESTRICT ON DELETE RESTRICT;
 G   ALTER TABLE ONLY public.tblelement DROP CONSTRAINT fkey_element_units;
       public          postgres    false    369    236    5618            j           2606    66347 +   tlkpelementtype fkey_elementtype-vocabulary    FK CONSTRAINT     �   ALTER TABLE ONLY public.tlkpelementtype
    ADD CONSTRAINT "fkey_elementtype-vocabulary" FOREIGN KEY (vocabularyid) REFERENCES public.tlkpvocabulary(vocabularyid);
 W   ALTER TABLE ONLY public.tlkpelementtype DROP CONSTRAINT "fkey_elementtype-vocabulary";
       public          postgres    false    375    5634    340            ;           2606    66352 7   tblenvironmentaldata fkey_environmentaldata_rasterlayer    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblenvironmentaldata
    ADD CONSTRAINT fkey_environmentaldata_rasterlayer FOREIGN KEY (rasterlayerid) REFERENCES public.tblrasterlayer(rasterlayerid);
 a   ALTER TABLE ONLY public.tblenvironmentaldata DROP CONSTRAINT fkey_environmentaldata_rasterlayer;
       public          postgres    false    5447    273    259            <           2606    66357 0   tblenvironmentaldata fkey_environmentaldata_tree    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblenvironmentaldata
    ADD CONSTRAINT fkey_environmentaldata_tree FOREIGN KEY (elementid) REFERENCES public.tblelement(elementid) ON UPDATE CASCADE ON DELETE CASCADE;
 Z   ALTER TABLE ONLY public.tblenvironmentaldata DROP CONSTRAINT fkey_environmentaldata_tree;
       public          postgres    false    236    5377    259            =           2606    66362 *   tbliptracking fkey_iptracking_securityuser    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbliptracking
    ADD CONSTRAINT fkey_iptracking_securityuser FOREIGN KEY (securityuserid) REFERENCES public.tblsecurityuser(securityuserid);
 T   ALTER TABLE ONLY public.tbliptracking DROP CONSTRAINT fkey_iptracking_securityuser;
       public          postgres    false    5496    295    261                       2606    66367 *   tblmeasurement fkey_measurement-datingtype    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblmeasurement
    ADD CONSTRAINT "fkey_measurement-datingtype" FOREIGN KEY (datingtypeid) REFERENCES public.tlkpdatingtype(datingtypeid) ON UPDATE CASCADE ON DELETE CASCADE;
 V   ALTER TABLE ONLY public.tblmeasurement DROP CONSTRAINT "fkey_measurement-datingtype";
       public          postgres    false    332    234    5562                       2606    66372 3   tblmeasurement fkey_measurement-measurementvariable    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblmeasurement
    ADD CONSTRAINT "fkey_measurement-measurementvariable" FOREIGN KEY (measurementvariableid) REFERENCES public.tlkpmeasurementvariable(measurementvariableid);
 _   ALTER TABLE ONLY public.tblmeasurement DROP CONSTRAINT "fkey_measurement-measurementvariable";
       public          postgres    false    346    5579    234                       2606    66377 /   tblmeasurement fkey_measurement-measuringmethod    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblmeasurement
    ADD CONSTRAINT "fkey_measurement-measuringmethod" FOREIGN KEY (measuringmethodid) REFERENCES public.tlkpmeasuringmethod(measuringmethodid);
 [   ALTER TABLE ONLY public.tblmeasurement DROP CONSTRAINT "fkey_measurement-measuringmethod";
       public          postgres    false    348    234    5581                       2606    66382 &   tblmeasurement fkey_measurement-radius    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblmeasurement
    ADD CONSTRAINT "fkey_measurement-radius" FOREIGN KEY (radiusid) REFERENCES public.tblradius(radiusid) ON UPDATE CASCADE ON DELETE RESTRICT;
 R   ALTER TABLE ONLY public.tblmeasurement DROP CONSTRAINT "fkey_measurement-radius";
       public          postgres    false    5388    234    239                       2606    66387 ,   tblmeasurement fkey_measurement-securityuser    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblmeasurement
    ADD CONSTRAINT "fkey_measurement-securityuser" FOREIGN KEY (measuredbyid) REFERENCES public.tblsecurityuser(securityuserid);
 X   ALTER TABLE ONLY public.tblmeasurement DROP CONSTRAINT "fkey_measurement-securityuser";
       public          postgres    false    234    295    5496                       2606    66392 ,   tblmeasurement fkey_measurement-supervisedby    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblmeasurement
    ADD CONSTRAINT "fkey_measurement-supervisedby" FOREIGN KEY (supervisedbyid) REFERENCES public.tblsecurityuser(securityuserid);
 X   ALTER TABLE ONLY public.tblmeasurement DROP CONSTRAINT "fkey_measurement-supervisedby";
       public          postgres    false    5496    295    234                       2606    66397 $   tblmeasurement fkey_measurement-unit    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblmeasurement
    ADD CONSTRAINT "fkey_measurement-unit" FOREIGN KEY (unitid) REFERENCES public.tlkpunit(unitid);
 P   ALTER TABLE ONLY public.tblmeasurement DROP CONSTRAINT "fkey_measurement-unit";
       public          postgres    false    234    5618    369            c           2606    66402 >   tblvmeasurementreadingnoteresult fkey_noteresult-readingresult    FK CONSTRAINT       ALTER TABLE ONLY public.tblvmeasurementreadingnoteresult
    ADD CONSTRAINT "fkey_noteresult-readingresult" FOREIGN KEY (vmeasurementresultid, relyear) REFERENCES public.tblvmeasurementreadingresult(vmeasurementresultid, relyear) ON UPDATE CASCADE ON DELETE CASCADE;
 j   ALTER TABLE ONLY public.tblvmeasurementreadingnoteresult DROP CONSTRAINT "fkey_noteresult-readingresult";
       public          postgres    false    241    314    314    5397    241                       2606    66407 &   tblobject fkey_object-coveragetemporal    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblobject
    ADD CONSTRAINT "fkey_object-coveragetemporal" FOREIGN KEY (coveragetemporalid) REFERENCES public.tlkpcoveragetemporal(coveragetemporalid);
 R   ALTER TABLE ONLY public.tblobject DROP CONSTRAINT "fkey_object-coveragetemporal";
       public          postgres    false    5550    231    323                       2606    66412 0   tblobject fkey_object-coveragetemporalfoundation    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblobject
    ADD CONSTRAINT "fkey_object-coveragetemporalfoundation" FOREIGN KEY (coveragetemporalfoundationid) REFERENCES public.tlkpcoveragetemporalfoundation(coveragetemporalfoundationid);
 \   ALTER TABLE ONLY public.tblobject DROP CONSTRAINT "fkey_object-coveragetemporalfoundation";
       public          postgres    false    325    231    5552                       2606    66417     tblobject fkey_object-objecttype    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblobject
    ADD CONSTRAINT "fkey_object-objecttype" FOREIGN KEY (objecttypeid) REFERENCES public.tlkpobjecttype(objecttypeid);
 L   ALTER TABLE ONLY public.tblobject DROP CONSTRAINT "fkey_object-objecttype";
       public          postgres    false    231    5583    350                       2606    66422    tblobject fkey_object-project    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblobject
    ADD CONSTRAINT "fkey_object-project" FOREIGN KEY (projectid) REFERENCES public.tblproject(projectid);
 I   ALTER TABLE ONLY public.tblobject DROP CONSTRAINT "fkey_object-project";
       public          postgres    false    269    5439    231            >           2606    66427 (   tblobjectregion fkey_objectregion-object    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblobjectregion
    ADD CONSTRAINT "fkey_objectregion-object" FOREIGN KEY (objectid) REFERENCES public.tblobject(objectid) ON UPDATE CASCADE ON DELETE CASCADE;
 T   ALTER TABLE ONLY public.tblobjectregion DROP CONSTRAINT "fkey_objectregion-object";
       public          postgres    false    231    5353    265            ?           2606    66432 (   tblobjectregion fkey_objectregion_region    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblobjectregion
    ADD CONSTRAINT fkey_objectregion_region FOREIGN KEY (regionid) REFERENCES public.tblregion(regionid);
 R   ALTER TABLE ONLY public.tblobjectregion DROP CONSTRAINT fkey_objectregion_region;
       public          postgres    false    5467    281    265            k           2606    66437 )   tlkpobjecttype fkey_objecttype-vocabulary    FK CONSTRAINT     �   ALTER TABLE ONLY public.tlkpobjecttype
    ADD CONSTRAINT "fkey_objecttype-vocabulary" FOREIGN KEY (vocabularyid) REFERENCES public.tlkpvocabulary(vocabularyid);
 U   ALTER TABLE ONLY public.tlkpobjecttype DROP CONSTRAINT "fkey_objecttype-vocabulary";
       public          postgres    false    350    375    5634            @           2606    66442 0   tblodkdefinition fkey_odkdefinition-securityuser    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblodkdefinition
    ADD CONSTRAINT "fkey_odkdefinition-securityuser" FOREIGN KEY (ownerid) REFERENCES public.tblsecurityuser(securityuserid);
 \   ALTER TABLE ONLY public.tblodkdefinition DROP CONSTRAINT "fkey_odkdefinition-securityuser";
       public          tellervo    false    267    5496    295            A           2606    66447 ,   tblodkinstance fkey_odkinstance-securityuser    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblodkinstance
    ADD CONSTRAINT "fkey_odkinstance-securityuser" FOREIGN KEY (ownerid) REFERENCES public.tblsecurityuser(securityuserid);
 X   ALTER TABLE ONLY public.tblodkinstance DROP CONSTRAINT "fkey_odkinstance-securityuser";
       public          tellervo    false    268    5496    295            B           2606    66452 '   tblproject fkey_project-projectcategory    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblproject
    ADD CONSTRAINT "fkey_project-projectcategory" FOREIGN KEY (projectcategoryid) REFERENCES public.tlkpprojectcategory(projectcategoryid);
 S   ALTER TABLE ONLY public.tblproject DROP CONSTRAINT "fkey_project-projectcategory";
       public          tellervo    false    5589    355    269            l           2606    66457 4   tlkpprojectcategory fkey_projectcategory-vocabulary2    FK CONSTRAINT     �   ALTER TABLE ONLY public.tlkpprojectcategory
    ADD CONSTRAINT "fkey_projectcategory-vocabulary2" FOREIGN KEY (vocabularyid) REFERENCES public.tlkpvocabulary(vocabularyid);
 `   ALTER TABLE ONLY public.tlkpprojectcategory DROP CONSTRAINT "fkey_projectcategory-vocabulary2";
       public          tellervo    false    375    355    5634            D           2606    66462 4   tblprojectprojecttype fkey_projectprojectype-project    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblprojectprojecttype
    ADD CONSTRAINT "fkey_projectprojectype-project" FOREIGN KEY (projectid) REFERENCES public.tblproject(projectid);
 `   ALTER TABLE ONLY public.tblprojectprojecttype DROP CONSTRAINT "fkey_projectprojectype-project";
       public          tellervo    false    270    5439    269            E           2606    66467 8   tblprojectprojecttype fkey_projectprojectype-projecttype    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblprojectprojecttype
    ADD CONSTRAINT "fkey_projectprojectype-projecttype" FOREIGN KEY (projecttypeid) REFERENCES public.tlkpprojecttype(projecttypeid);
 d   ALTER TABLE ONLY public.tblprojectprojecttype DROP CONSTRAINT "fkey_projectprojectype-projecttype";
       public          tellervo    false    270    5593    357            m           2606    66472 +   tlkpprojecttype fkey_projecttype-vocabulary    FK CONSTRAINT     �   ALTER TABLE ONLY public.tlkpprojecttype
    ADD CONSTRAINT "fkey_projecttype-vocabulary" FOREIGN KEY (vocabularyid) REFERENCES public.tlkpvocabulary(vocabularyid);
 W   ALTER TABLE ONLY public.tlkpprojecttype DROP CONSTRAINT "fkey_projecttype-vocabulary";
       public          tellervo    false    357    5634    375            '           2606    66477    tblradius fkey_radius-heartwood    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblradius
    ADD CONSTRAINT "fkey_radius-heartwood" FOREIGN KEY (heartwoodid) REFERENCES public.tlkpcomplexpresenceabsence(complexpresenceabsenceid);
 K   ALTER TABLE ONLY public.tblradius DROP CONSTRAINT "fkey_radius-heartwood";
       public          postgres    false    5548    239    321            (           2606    66482    tblradius fkey_radius-pith    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblradius
    ADD CONSTRAINT "fkey_radius-pith" FOREIGN KEY (pithid) REFERENCES public.tlkpcomplexpresenceabsence(complexpresenceabsenceid);
 F   ALTER TABLE ONLY public.tblradius DROP CONSTRAINT "fkey_radius-pith";
       public          postgres    false    5548    321    239            )           2606    66487    tblradius fkey_radius-sample    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblradius
    ADD CONSTRAINT "fkey_radius-sample" FOREIGN KEY (sampleid) REFERENCES public.tblsample(sampleid) ON UPDATE CASCADE ON DELETE RESTRICT;
 H   ALTER TABLE ONLY public.tblradius DROP CONSTRAINT "fkey_radius-sample";
       public          postgres    false    5394    240    239            *           2606    66492    tblradius fkey_radius-sapwood    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblradius
    ADD CONSTRAINT "fkey_radius-sapwood" FOREIGN KEY (sapwoodid) REFERENCES public.tlkpcomplexpresenceabsence(complexpresenceabsenceid);
 I   ALTER TABLE ONLY public.tblradius DROP CONSTRAINT "fkey_radius-sapwood";
       public          postgres    false    321    239    5548            +           2606    66497 6   tblradius fkey_radius_lastringunderbarkpresenceabsence    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblradius
    ADD CONSTRAINT fkey_radius_lastringunderbarkpresenceabsence FOREIGN KEY (lrubpa) REFERENCES public.tlkppresenceabsence(presenceabsenceid);
 `   ALTER TABLE ONLY public.tblradius DROP CONSTRAINT fkey_radius_lastringunderbarkpresenceabsence;
       public          postgres    false    239    5587    352            F           2606    66502 #   tblreading fkey_reading-measurement    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblreading
    ADD CONSTRAINT "fkey_reading-measurement" FOREIGN KEY (measurementid) REFERENCES public.tblmeasurement(measurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 O   ALTER TABLE ONLY public.tblreading DROP CONSTRAINT "fkey_reading-measurement";
       public          postgres    false    234    275    5368                       2606    66507 +   tlkpreadingnote fkey_readingnote-vocabulary    FK CONSTRAINT     �   ALTER TABLE ONLY public.tlkpreadingnote
    ADD CONSTRAINT "fkey_readingnote-vocabulary" FOREIGN KEY (vocabularyid) REFERENCES public.tlkpvocabulary(vocabularyid);
 W   ALTER TABLE ONLY public.tlkpreadingnote DROP CONSTRAINT "fkey_readingnote-vocabulary";
       public          postgres    false    5634    375    232            G           2606    66512 5   tblreadingreadingnote fkey_readingreadingnote-reading    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblreadingreadingnote
    ADD CONSTRAINT "fkey_readingreadingnote-reading" FOREIGN KEY (readingid) REFERENCES public.tblreading(readingid) ON UPDATE CASCADE ON DELETE CASCADE;
 a   ALTER TABLE ONLY public.tblreadingreadingnote DROP CONSTRAINT "fkey_readingreadingnote-reading";
       public          postgres    false    275    277    5454            H           2606    66517 9   tblreadingreadingnote fkey_readingreadingnote-readingnote    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblreadingreadingnote
    ADD CONSTRAINT "fkey_readingreadingnote-readingnote" FOREIGN KEY (readingnoteid) REFERENCES public.tlkpreadingnote(readingnoteid) ON UPDATE CASCADE;
 e   ALTER TABLE ONLY public.tblreadingreadingnote DROP CONSTRAINT "fkey_readingreadingnote-readingnote";
       public          postgres    false    277    232    5361            e           2606    66522 E   tblvmeasurementrelyearreadingnote fkey_relyearreadingnote-readingnote    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementrelyearreadingnote
    ADD CONSTRAINT "fkey_relyearreadingnote-readingnote" FOREIGN KEY (readingnoteid) REFERENCES public.tlkpreadingnote(readingnoteid) ON UPDATE CASCADE ON DELETE RESTRICT;
 q   ALTER TABLE ONLY public.tblvmeasurementrelyearreadingnote DROP CONSTRAINT "fkey_relyearreadingnote-readingnote";
       public          postgres    false    232    5361    317            f           2606    66527 F   tblvmeasurementrelyearreadingnote fkey_relyearreadingnote-vmeasurement    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementrelyearreadingnote
    ADD CONSTRAINT "fkey_relyearreadingnote-vmeasurement" FOREIGN KEY (vmeasurementid) REFERENCES public.tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 r   ALTER TABLE ONLY public.tblvmeasurementrelyearreadingnote DROP CONSTRAINT "fkey_relyearreadingnote-vmeasurement";
       public          postgres    false    308    317    5530            K           2606    66532 *   tblrequestlog fkey_requestlog-securityuser    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblrequestlog
    ADD CONSTRAINT "fkey_requestlog-securityuser" FOREIGN KEY (securityuserid) REFERENCES public.tblsecurityuser(securityuserid);
 V   ALTER TABLE ONLY public.tblrequestlog DROP CONSTRAINT "fkey_requestlog-securityuser";
       public          postgres    false    5496    283    295            g           2606    66537 1   tblvmeasurementresult fkey_result-vmeasurement_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementresult
    ADD CONSTRAINT "fkey_result-vmeasurement_id" FOREIGN KEY (vmeasurementid) REFERENCES public.tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 ]   ALTER TABLE ONLY public.tblvmeasurementresult DROP CONSTRAINT "fkey_result-vmeasurement_id";
       public          postgres    false    318    308    5530            -           2606    66542    tblsample fkey_sample-box    FK CONSTRAINT     |   ALTER TABLE ONLY public.tblsample
    ADD CONSTRAINT "fkey_sample-box" FOREIGN KEY (boxid) REFERENCES public.tblbox(boxid);
 E   ALTER TABLE ONLY public.tblsample DROP CONSTRAINT "fkey_sample-box";
       public          postgres    false    246    5401    240            .           2606    66547 #   tblsample fkey_sample-datecertainty    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsample
    ADD CONSTRAINT "fkey_sample-datecertainty" FOREIGN KEY (datecertaintyid) REFERENCES public.tlkpdatecertainty(datecertaintyid);
 O   ALTER TABLE ONLY public.tblsample DROP CONSTRAINT "fkey_sample-datecertainty";
       public          postgres    false    240    330    5558            /           2606    66552 "   tblsample fkey_sample-samplestatus    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsample
    ADD CONSTRAINT "fkey_sample-samplestatus" FOREIGN KEY (samplestatusid) REFERENCES public.tlkpsamplestatus(samplestatusid);
 N   ALTER TABLE ONLY public.tblsample DROP CONSTRAINT "fkey_sample-samplestatus";
       public          postgres    false    5597    240    360            0           2606    66557     tblsample fkey_sample-sampletype    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsample
    ADD CONSTRAINT "fkey_sample-sampletype" FOREIGN KEY (typeid) REFERENCES public.tlkpsampletype(sampletypeid);
 L   ALTER TABLE ONLY public.tblsample DROP CONSTRAINT "fkey_sample-sampletype";
       public          postgres    false    362    240    5601            1           2606    66562    tblsample fkey_sample-tree    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsample
    ADD CONSTRAINT "fkey_sample-tree" FOREIGN KEY (elementid) REFERENCES public.tblelement(elementid) ON UPDATE CASCADE ON DELETE RESTRICT;
 F   ALTER TABLE ONLY public.tblsample DROP CONSTRAINT "fkey_sample-tree";
       public          postgres    false    236    240    5377            L           2606    66567 5   tblsecuritydefault fkey_securitydefault-securitygroup    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecuritydefault
    ADD CONSTRAINT "fkey_securitydefault-securitygroup" FOREIGN KEY (securitygroupid) REFERENCES public.tblsecuritygroup(securitygroupid) ON UPDATE CASCADE ON DELETE CASCADE;
 a   ALTER TABLE ONLY public.tblsecuritydefault DROP CONSTRAINT "fkey_securitydefault-securitygroup";
       public          postgres    false    237    286    5383            M           2606    66572 :   tblsecuritydefault fkey_securitydefault-securitypermission    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecuritydefault
    ADD CONSTRAINT "fkey_securitydefault-securitypermission" FOREIGN KEY (securitypermissionid) REFERENCES public.tlkpsecuritypermission(securitypermissionid) ON UPDATE CASCADE;
 f   ALTER TABLE ONLY public.tblsecuritydefault DROP CONSTRAINT "fkey_securitydefault-securitypermission";
       public          postgres    false    5605    364    286            Q           2606    66577 F   tblsecuritygroupmembership fkey_securitygroupmembership-securitygroup1    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecuritygroupmembership
    ADD CONSTRAINT "fkey_securitygroupmembership-securitygroup1" FOREIGN KEY (parentsecuritygroupid) REFERENCES public.tblsecuritygroup(securitygroupid) ON UPDATE CASCADE ON DELETE CASCADE;
 r   ALTER TABLE ONLY public.tblsecuritygroupmembership DROP CONSTRAINT "fkey_securitygroupmembership-securitygroup1";
       public          postgres    false    290    237    5383            R           2606    66582 F   tblsecuritygroupmembership fkey_securitygroupmembership-securitygroup2    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecuritygroupmembership
    ADD CONSTRAINT "fkey_securitygroupmembership-securitygroup2" FOREIGN KEY (childsecuritygroupid) REFERENCES public.tblsecuritygroup(securitygroupid) ON UPDATE CASCADE ON DELETE CASCADE;
 r   ALTER TABLE ONLY public.tblsecuritygroupmembership DROP CONSTRAINT "fkey_securitygroupmembership-securitygroup2";
       public          postgres    false    5383    290    237            S           2606    66587 ,   tblsecurityobject fkey_securityobject-object    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityobject
    ADD CONSTRAINT "fkey_securityobject-object" FOREIGN KEY (objectid) REFERENCES public.tblobject(objectid) ON UPDATE CASCADE ON DELETE CASCADE;
 X   ALTER TABLE ONLY public.tblsecurityobject DROP CONSTRAINT "fkey_securityobject-object";
       public          postgres    false    5353    292    231            T           2606    66592 3   tblsecurityobject fkey_securityobject-securitygroup    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityobject
    ADD CONSTRAINT "fkey_securityobject-securitygroup" FOREIGN KEY (securitygroupid) REFERENCES public.tblsecuritygroup(securitygroupid) ON UPDATE CASCADE;
 _   ALTER TABLE ONLY public.tblsecurityobject DROP CONSTRAINT "fkey_securityobject-securitygroup";
       public          postgres    false    5383    292    237            U           2606    66597 8   tblsecurityobject fkey_securityobject-securitypermission    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityobject
    ADD CONSTRAINT "fkey_securityobject-securitypermission" FOREIGN KEY (securitypermissionid) REFERENCES public.tlkpsecuritypermission(securitypermissionid) ON UPDATE CASCADE;
 d   ALTER TABLE ONLY public.tblsecurityobject DROP CONSTRAINT "fkey_securityobject-securitypermission";
       public          postgres    false    5605    364    292            N           2606    66602 2   tblsecurityelement fkey_securitytree-securitygroup    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityelement
    ADD CONSTRAINT "fkey_securitytree-securitygroup" FOREIGN KEY (securitygroupid) REFERENCES public.tblsecuritygroup(securitygroupid) ON UPDATE CASCADE;
 ^   ALTER TABLE ONLY public.tblsecurityelement DROP CONSTRAINT "fkey_securitytree-securitygroup";
       public          postgres    false    237    288    5383            O           2606    66607 7   tblsecurityelement fkey_securitytree-securitypermission    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityelement
    ADD CONSTRAINT "fkey_securitytree-securitypermission" FOREIGN KEY (securitypermissionid) REFERENCES public.tlkpsecuritypermission(securitypermissionid) ON UPDATE CASCADE;
 c   ALTER TABLE ONLY public.tblsecurityelement DROP CONSTRAINT "fkey_securitytree-securitypermission";
       public          postgres    false    5605    364    288            P           2606    66612 )   tblsecurityelement fkey_securitytree-tree    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityelement
    ADD CONSTRAINT "fkey_securitytree-tree" FOREIGN KEY (elementid) REFERENCES public.tblelement(elementid) ON UPDATE CASCADE ON DELETE CASCADE;
 U   ALTER TABLE ONLY public.tblsecurityelement DROP CONSTRAINT "fkey_securitytree-tree";
       public          postgres    false    5377    288    236            V           2606    66617 C   tblsecurityusermembership fkey_securityusermembership-securitygroup    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityusermembership
    ADD CONSTRAINT "fkey_securityusermembership-securitygroup" FOREIGN KEY (securitygroupid) REFERENCES public.tblsecuritygroup(securitygroupid) ON UPDATE CASCADE ON DELETE CASCADE;
 o   ALTER TABLE ONLY public.tblsecurityusermembership DROP CONSTRAINT "fkey_securityusermembership-securitygroup";
       public          postgres    false    296    5383    237            W           2606    66622 ?   tblsecurityvmeasurement fkey_securityvmeasurement-securitygroup    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityvmeasurement
    ADD CONSTRAINT "fkey_securityvmeasurement-securitygroup" FOREIGN KEY (securitygroupid) REFERENCES public.tblsecuritygroup(securitygroupid) ON UPDATE CASCADE;
 k   ALTER TABLE ONLY public.tblsecurityvmeasurement DROP CONSTRAINT "fkey_securityvmeasurement-securitygroup";
       public          postgres    false    298    5383    237            X           2606    66627 D   tblsecurityvmeasurement fkey_securityvmeasurement-securitypermission    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityvmeasurement
    ADD CONSTRAINT "fkey_securityvmeasurement-securitypermission" FOREIGN KEY (securitypermissionid) REFERENCES public.tlkpsecuritypermission(securitypermissionid) ON UPDATE CASCADE;
 p   ALTER TABLE ONLY public.tblsecurityvmeasurement DROP CONSTRAINT "fkey_securityvmeasurement-securitypermission";
       public          postgres    false    364    5605    298            Y           2606    66632 >   tblsecurityvmeasurement fkey_securityvmeasurement-vmeasurement    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityvmeasurement
    ADD CONSTRAINT "fkey_securityvmeasurement-vmeasurement" FOREIGN KEY (vmeasurementid) REFERENCES public.tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 j   ALTER TABLE ONLY public.tblsecurityvmeasurement DROP CONSTRAINT "fkey_securityvmeasurement-vmeasurement";
       public          postgres    false    5530    308    298            Z           2606    66637 !   tbltag fkey_tagowner-securityuser    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbltag
    ADD CONSTRAINT "fkey_tagowner-securityuser" FOREIGN KEY (ownerid) REFERENCES public.tblsecurityuser(securityuserid) ON UPDATE CASCADE ON DELETE CASCADE;
 M   ALTER TABLE ONLY public.tbltag DROP CONSTRAINT "fkey_tagowner-securityuser";
       public          tellervo    false    295    5496    302            n           2606    66642    tlkptaxon fkey_taxon-taxonrank    FK CONSTRAINT     �   ALTER TABLE ONLY public.tlkptaxon
    ADD CONSTRAINT "fkey_taxon-taxonrank" FOREIGN KEY (taxonrankid) REFERENCES public.tlkptaxonrank(taxonrankid) ON UPDATE CASCADE;
 J   ALTER TABLE ONLY public.tlkptaxon DROP CONSTRAINT "fkey_taxon-taxonrank";
       public          postgres    false    5612    367    368            4           2606    66647 .   tblcrossdate fkey_tblcrossdate-tblvmeasurement    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblcrossdate
    ADD CONSTRAINT "fkey_tblcrossdate-tblvmeasurement" FOREIGN KEY (vmeasurementid) REFERENCES public.tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 Z   ALTER TABLE ONLY public.tblcrossdate DROP CONSTRAINT "fkey_tblcrossdate-tblvmeasurement";
       public          postgres    false    5530    249    308            5           2606    66652 5   tblcrossdate fkey_tblcrossdate-tblvmeasurement_master    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblcrossdate
    ADD CONSTRAINT "fkey_tblcrossdate-tblvmeasurement_master" FOREIGN KEY (mastervmeasurementid) REFERENCES public.tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE RESTRICT;
 a   ALTER TABLE ONLY public.tblcrossdate DROP CONSTRAINT "fkey_tblcrossdate-tblvmeasurement_master";
       public          postgres    false    249    5530    308            6           2606    66657 (   tblcurationevent fkey_tblcuration-tblbox    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblcurationevent
    ADD CONSTRAINT "fkey_tblcuration-tblbox" FOREIGN KEY (boxid) REFERENCES public.tblbox(boxid) ON DELETE CASCADE;
 T   ALTER TABLE ONLY public.tblcurationevent DROP CONSTRAINT "fkey_tblcuration-tblbox";
       public          tellervo    false    251    5401    246            7           2606    66662 )   tblcurationevent fkey_tblcuration-tblloan    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblcurationevent
    ADD CONSTRAINT "fkey_tblcuration-tblloan" FOREIGN KEY (loanid) REFERENCES public.tblloan(loanid);
 U   ALTER TABLE ONLY public.tblcurationevent DROP CONSTRAINT "fkey_tblcuration-tblloan";
       public          tellervo    false    238    5385    251            8           2606    66667 +   tblcurationevent fkey_tblcuration-tblsample    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblcurationevent
    ADD CONSTRAINT "fkey_tblcuration-tblsample" FOREIGN KEY (sampleid) REFERENCES public.tblsample(sampleid);
 W   ALTER TABLE ONLY public.tblcurationevent DROP CONSTRAINT "fkey_tblcuration-tblsample";
       public          tellervo    false    5394    240    251            9           2606    66672 1   tblcurationevent fkey_tblcuration-tblsecurityuser    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblcurationevent
    ADD CONSTRAINT "fkey_tblcuration-tblsecurityuser" FOREIGN KEY (curatorid) REFERENCES public.tblsecurityuser(securityuserid);
 ]   ALTER TABLE ONLY public.tblcurationevent DROP CONSTRAINT "fkey_tblcuration-tblsecurityuser";
       public          tellervo    false    5496    295    251            :           2606    66677 4   tblcurationevent fkey_tblcuration-tlkpcurationstatus    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblcurationevent
    ADD CONSTRAINT "fkey_tblcuration-tlkpcurationstatus" FOREIGN KEY (curationstatusid) REFERENCES public.tlkpcurationstatus(curationstatusid);
 `   ALTER TABLE ONLY public.tblcurationevent DROP CONSTRAINT "fkey_tblcuration-tlkpcurationstatus";
       public          tellervo    false    251    327    5554            $           2606    66682 %   tblelement fkey_tblelement_tlkpdomain    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblelement
    ADD CONSTRAINT fkey_tblelement_tlkpdomain FOREIGN KEY (domainid) REFERENCES public.tlkpdomain(domainid);
 O   ALTER TABLE ONLY public.tblelement DROP CONSTRAINT fkey_tblelement_tlkpdomain;
       public          postgres    false    334    5564    236                       2606    66687 #   tblobject fkey_tblobject_tlkpdomain    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblobject
    ADD CONSTRAINT fkey_tblobject_tlkpdomain FOREIGN KEY (domainid) REFERENCES public.tlkpdomain(domainid);
 M   ALTER TABLE ONLY public.tblobject DROP CONSTRAINT fkey_tblobject_tlkpdomain;
       public          postgres    false    231    5564    334            C           2606    66692 %   tblproject fkey_tblproject_tlkpdomain    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblproject
    ADD CONSTRAINT fkey_tblproject_tlkpdomain FOREIGN KEY (domainid) REFERENCES public.tlkpdomain(domainid);
 O   ALTER TABLE ONLY public.tblproject DROP CONSTRAINT fkey_tblproject_tlkpdomain;
       public          tellervo    false    334    5564    269            ,           2606    66697 #   tblradius fkey_tblradius_tlkpdomain    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblradius
    ADD CONSTRAINT fkey_tblradius_tlkpdomain FOREIGN KEY (domainid) REFERENCES public.tlkpdomain(domainid);
 M   ALTER TABLE ONLY public.tblradius DROP CONSTRAINT fkey_tblradius_tlkpdomain;
       public          postgres    false    5564    239    334            I           2606    66702 (   tblredate fkey_tblredate-tblvmeasurement    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblredate
    ADD CONSTRAINT "fkey_tblredate-tblvmeasurement" FOREIGN KEY (vmeasurementid) REFERENCES public.tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 T   ALTER TABLE ONLY public.tblredate DROP CONSTRAINT "fkey_tblredate-tblvmeasurement";
       public          postgres    false    279    5530    308            J           2606    66707 '   tblredate fkey_tblredate-tlkpdatingtype    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblredate
    ADD CONSTRAINT "fkey_tblredate-tlkpdatingtype" FOREIGN KEY (redatingtypeid) REFERENCES public.tlkpdatingtype(datingtypeid) ON UPDATE CASCADE ON DELETE RESTRICT;
 S   ALTER TABLE ONLY public.tblredate DROP CONSTRAINT "fkey_tblredate-tlkpdatingtype";
       public          postgres    false    5562    332    279            2           2606    66712 #   tblsample fkey_tblsample_tlkpdomain    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsample
    ADD CONSTRAINT fkey_tblsample_tlkpdomain FOREIGN KEY (domainid) REFERENCES public.tlkpdomain(domainid);
 M   ALTER TABLE ONLY public.tblsample DROP CONSTRAINT fkey_tblsample_tlkpdomain;
       public          postgres    false    334    5564    240            [           2606    66717 ,   tbltruncate fkey_tbltruncate-tblvmeasurement    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbltruncate
    ADD CONSTRAINT "fkey_tbltruncate-tblvmeasurement" FOREIGN KEY (vmeasurementid) REFERENCES public.tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 X   ALTER TABLE ONLY public.tbltruncate DROP CONSTRAINT "fkey_tbltruncate-tblvmeasurement";
       public          postgres    false    5530    308    303            ]           2606    66722 /   tblvmeasurement fkey_tblvmeasurement_tlkpdomain    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurement
    ADD CONSTRAINT fkey_tblvmeasurement_tlkpdomain FOREIGN KEY (domainid) REFERENCES public.tlkpdomain(domainid);
 Y   ALTER TABLE ONLY public.tblvmeasurement DROP CONSTRAINT fkey_tblvmeasurement_tlkpdomain;
       public          postgres    false    308    334    5564            \           2606    66727 D   tbluserdefinedfieldvalue fkey_userdefinedfieldvalue-userdefinedfield    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbluserdefinedfieldvalue
    ADD CONSTRAINT "fkey_userdefinedfieldvalue-userdefinedfield" FOREIGN KEY (userdefinedfieldid) REFERENCES public.tlkpuserdefinedfield(userdefinedfieldid);
 p   ALTER TABLE ONLY public.tbluserdefinedfieldvalue DROP CONSTRAINT "fkey_userdefinedfieldvalue-userdefinedfield";
       public          tellervo    false    5622    371    307            ^           2606    66732 -   tblvmeasurement fkey_vmeasurement-measurement    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurement
    ADD CONSTRAINT "fkey_vmeasurement-measurement" FOREIGN KEY (measurementid) REFERENCES public.tblmeasurement(measurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 Y   ALTER TABLE ONLY public.tblvmeasurement DROP CONSTRAINT "fkey_vmeasurement-measurement";
       public          postgres    false    308    234    5368            _           2606    66737 .   tblvmeasurement fkey_vmeasurement-securityuser    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurement
    ADD CONSTRAINT "fkey_vmeasurement-securityuser" FOREIGN KEY (owneruserid) REFERENCES public.tblsecurityuser(securityuserid);
 Z   ALTER TABLE ONLY public.tblvmeasurement DROP CONSTRAINT "fkey_vmeasurement-securityuser";
       public          postgres    false    308    295    5496            `           2606    66742 0   tblvmeasurement fkey_vmeasurement-vmeasurementop    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurement
    ADD CONSTRAINT "fkey_vmeasurement-vmeasurementop" FOREIGN KEY (vmeasurementopid) REFERENCES public.tlkpvmeasurementop(vmeasurementopid) ON UPDATE CASCADE;
 \   ALTER TABLE ONLY public.tblvmeasurement DROP CONSTRAINT "fkey_vmeasurement-vmeasurementop";
       public          postgres    false    308    5630    373            a           2606    66747 9   tblvmeasurementgroup fkey_vmeasurementgroup-vmeasurement1    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementgroup
    ADD CONSTRAINT "fkey_vmeasurementgroup-vmeasurement1" FOREIGN KEY (vmeasurementid) REFERENCES public.tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 e   ALTER TABLE ONLY public.tblvmeasurementgroup DROP CONSTRAINT "fkey_vmeasurementgroup-vmeasurement1";
       public          postgres    false    308    5530    311            b           2606    66752 9   tblvmeasurementgroup fkey_vmeasurementgroup-vmeasurement2    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementgroup
    ADD CONSTRAINT "fkey_vmeasurementgroup-vmeasurement2" FOREIGN KEY (membervmeasurementid) REFERENCES public.tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE RESTRICT;
 e   ALTER TABLE ONLY public.tblvmeasurementgroup DROP CONSTRAINT "fkey_vmeasurementgroup-vmeasurement2";
       public          postgres    false    5530    311    308            3           2606    66757 N   tblvmeasurementreadingresult fkey_vmeasurementreadingresult-vmeasurementresult    FK CONSTRAINT       ALTER TABLE ONLY public.tblvmeasurementreadingresult
    ADD CONSTRAINT "fkey_vmeasurementreadingresult-vmeasurementresult" FOREIGN KEY (vmeasurementresultid) REFERENCES public.tblvmeasurementresult(vmeasurementresultid) ON UPDATE CASCADE ON DELETE CASCADE;
 z   ALTER TABLE ONLY public.tblvmeasurementreadingresult DROP CONSTRAINT "fkey_vmeasurementreadingresult-vmeasurementresult";
       public          postgres    false    241    5542    318            h           2606    66762 /   tblvmeasurementtotag fkey_vmeasurementtotag-tag    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementtotag
    ADD CONSTRAINT "fkey_vmeasurementtotag-tag" FOREIGN KEY (tagid) REFERENCES public.tbltag(tagid) ON UPDATE CASCADE ON DELETE CASCADE;
 [   ALTER TABLE ONLY public.tblvmeasurementtotag DROP CONSTRAINT "fkey_vmeasurementtotag-tag";
       public          tellervo    false    319    5516    302            i           2606    66767 8   tblvmeasurementtotag fkey_vmeasurementtotag-vmeasurement    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementtotag
    ADD CONSTRAINT "fkey_vmeasurementtotag-vmeasurement" FOREIGN KEY (vmeasurementid) REFERENCES public.tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 d   ALTER TABLE ONLY public.tblvmeasurementtotag DROP CONSTRAINT "fkey_vmeasurementtotag-vmeasurement";
       public          tellervo    false    5530    308    319            %           2606    66772 #   tblelement tblelement_objectid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblelement
    ADD CONSTRAINT tblelement_objectid_fkey FOREIGN KEY (objectid) REFERENCES public.tblobject(objectid) ON UPDATE CASCADE ON DELETE RESTRICT;
 M   ALTER TABLE ONLY public.tblelement DROP CONSTRAINT tblelement_objectid_fkey;
       public          postgres    false    236    5353    231                       2606    66777 '   tblobject tblobject_parentobjectid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblobject
    ADD CONSTRAINT tblobject_parentobjectid_fkey FOREIGN KEY (parentobjectid) REFERENCES public.tblobject(objectid) ON UPDATE CASCADE ON DELETE RESTRICT;
 Q   ALTER TABLE ONLY public.tblobject DROP CONSTRAINT tblobject_parentobjectid_fkey;
       public          postgres    false    231    231    5353                       2606    66782 J   tblvmeasurementderivedcache tblvmeasurementderivedcache_measurementid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementderivedcache
    ADD CONSTRAINT tblvmeasurementderivedcache_measurementid_fkey FOREIGN KEY (measurementid) REFERENCES public.tblmeasurement(measurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 t   ALTER TABLE ONLY public.tblvmeasurementderivedcache DROP CONSTRAINT tblvmeasurementderivedcache_measurementid_fkey;
       public          postgres    false    235    5368    234                       2606    66787 K   tblvmeasurementderivedcache tblvmeasurementderivedcache_vmeasurementid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementderivedcache
    ADD CONSTRAINT tblvmeasurementderivedcache_vmeasurementid_fkey FOREIGN KEY (vmeasurementid) REFERENCES public.tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 u   ALTER TABLE ONLY public.tblvmeasurementderivedcache DROP CONSTRAINT tblvmeasurementderivedcache_vmeasurementid_fkey;
       public          postgres    false    235    308    5530                       2606    66792 C   tblvmeasurementmetacache tblvmeasurementmetacache_datingtypeid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementmetacache
    ADD CONSTRAINT tblvmeasurementmetacache_datingtypeid_fkey FOREIGN KEY (datingtypeid) REFERENCES public.tlkpdatingtype(datingtypeid) ON UPDATE CASCADE ON DELETE RESTRICT;
 m   ALTER TABLE ONLY public.tblvmeasurementmetacache DROP CONSTRAINT tblvmeasurementmetacache_datingtypeid_fkey;
       public          postgres    false    233    332    5562                       2606    66797 E   tblvmeasurementmetacache tblvmeasurementmetacache_vmeasurementid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementmetacache
    ADD CONSTRAINT tblvmeasurementmetacache_vmeasurementid_fkey FOREIGN KEY (vmeasurementid) REFERENCES public.tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 o   ALTER TABLE ONLY public.tblvmeasurementmetacache DROP CONSTRAINT tblvmeasurementmetacache_vmeasurementid_fkey;
       public          postgres    false    233    5530    308            d           2606    66802 T   tblvmeasurementreadingnoteresult tblvmeasurementreadingnoteresult_readingnoteid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementreadingnoteresult
    ADD CONSTRAINT tblvmeasurementreadingnoteresult_readingnoteid_fkey FOREIGN KEY (readingnoteid) REFERENCES public.tlkpreadingnote(readingnoteid) ON UPDATE CASCADE ON DELETE CASCADE;
 ~   ALTER TABLE ONLY public.tblvmeasurementreadingnoteresult DROP CONSTRAINT tblvmeasurementreadingnoteresult_readingnoteid_fkey;
       public          postgres    false    314    5361    232                       2606    66807 4   tlkpreadingnote tlkpreadingnote_parentreadingid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tlkpreadingnote
    ADD CONSTRAINT tlkpreadingnote_parentreadingid_fkey FOREIGN KEY (parentreadingid) REFERENCES public.tblreading(readingid) ON UPDATE CASCADE ON DELETE CASCADE;
 ^   ALTER TABLE ONLY public.tlkpreadingnote DROP CONSTRAINT tlkpreadingnote_parentreadingid_fkey;
       public          postgres    false    232    275    5454                       2606    66812 A   tlkpreadingnote tlkpreadingnote_parentvmrelyearreadingnoteid_fkey    FK CONSTRAINT       ALTER TABLE ONLY public.tlkpreadingnote
    ADD CONSTRAINT tlkpreadingnote_parentvmrelyearreadingnoteid_fkey FOREIGN KEY (parentvmrelyearreadingnoteid) REFERENCES public.tblvmeasurementrelyearreadingnote(relyearreadingnoteid) ON UPDATE CASCADE ON DELETE CASCADE;
 k   ALTER TABLE ONLY public.tlkpreadingnote DROP CONSTRAINT tlkpreadingnote_parentvmrelyearreadingnoteid_fkey;
       public          postgres    false    317    5540    232            o           2606    66817 3   tlkpuserdefinedfield tlkpuserdefinedfields-datatype    FK CONSTRAINT     �   ALTER TABLE ONLY public.tlkpuserdefinedfield
    ADD CONSTRAINT "tlkpuserdefinedfields-datatype" FOREIGN KEY (datatype) REFERENCES public.tlkpdatatype(datatype);
 _   ALTER TABLE ONLY public.tlkpuserdefinedfield DROP CONSTRAINT "tlkpuserdefinedfields-datatype";
       public          tellervo    false    329    371    5556            S      x������ � �      T      x������ � �      U      x������ � �      V      x������ � �      &      x������ � �      �   �  x��P�n!<��^af�]ߜHmIٖr�����R��
p*���&�U�*�@�fތ���֨F2�X1!�i�bu��� hr@�	��3�0�P�w\�D�֕�Z|��?�קW[�+U[V��,�kZ�Y�����2�'�Wz��e$��{FV-WjQ��Q�Ym�����$�hY�|�ct����0'�����kp��+�?�P��&g��D�Z�3v�YO�sml+?3 �V7-Vp+�pM��H���6*��v[^,�'��~�.@�h��K��Ȑ��l�1Yg�8�C<ol�f��5N��t8�0!B��O��?�E	!ځ>A�Ē�K�����r���XH����M%�(��ν��c"�������/��۱Q���j��;˲�      �      x��}�r"˒����\uKfsY��R@��A@I6� 	A%��|�D�����E/Ƭ��������I	u��#+�#2��ɼYXS;�
�s����v,��<�"��1{E,�����2��57~��7�_4&.���1�*�+��n��������kS�/<R���3����߷v�[x'[f���r\H,���'��|�z}}6�~}����[���l9�i�h��#P�ʃr���_�Z>ӛ���#{�lE2�%Iv��\8'�V;���lm�/؟4
^�̜�߰�tF[+�d��s9Q_z���OO��J��m�5�?�������W�K��ſe31fl�x��/�Z1���Q�7ZE��/^TB�C�){|w�)&���������e�N��k���'�9/C��lY�{�VZ����3Ɓ�����c���E����&�p��s�h�'��o!��r���w#���)YY�BG�)��
���n�ա� Qg>���g0AWb�ib�>��^9[�h���d�W5�'��]�SE�xw��?�q7	\osoLƏr��L��h��Y��s�톒� D����Vr�(�A�a��ĘP�Om�D�xU���G�&��b�k�o�ƉU�v^g��4X&������o��a�Pc����L7�ȑ{cȻ~��:_̟��Z��M�VL�u0;�i0[L�ݐ�ɑCmsS� �K�~[�[�Ŷ�f���"���vO2��J?7���68f���Q�c�H������R�ܚ�n9F>��(T����?�㒛�R��j�m���7B$! ��c��,-�W .w�W�|��łc�K�j�c��N� �,qW}m���L'@�z��Ͷ�r������q�(t�xZm�]0ǝ��>�&JL�Y�E;��Z����\-��>�S��/gup��c�8bn�/q�L��b!"t�J��AK�gW����Ԫ�3Gt��$ģ�u?��� t'���b�(|�#I'ϝK�5����ĆK�d����L��S�Y+��+fy�}l�s�,H�r���r+a�X�n���Q5���X�!�45��P��oB~i�Ȟ�l1ڿ����u�$�<v���'���`���Ll�V���L`�2��(p]�7�����(�ϓ[`ӥ��y7t�$&p��)+$�p2�ۑ�۸�Ew@Ɯ�s���p�j�We�1tMt�7#(ز|��v�@N��.���]Z�q2��"�����goQ �]4�p$@A��T�nH���B��0��TA��ϧۜ�G(Sz��%�'b� �Ѣ\.����M�"���s0-7�;;��K��8������.5����*j�xovF�\v[3�>�.��3sc�+�*��F���rVR8ײ!�ա�))|7��jN@���a:qbؒ� k�.9n�F�y;��5>Y2� <�]Ht��uty��\�6^�a�:|�g*�ϫLb2��'�O�V]�m.P�c���(��U,�6B�C��#�c?��`0	gz=ߥ�5�GCL�ϧ:��>T:��M�Mׁ�r�C�D�������?��'���e:�} @��٪� �B"]���������`����C:�~���!��ҕ+G��I�E�ɜ#�,��lIw������g�I�㜍U�@��Jg�����ѿ���3\b��&���4ɿ��=P�"��+΃���AK%#�Ư��(��ۤ>�|��w���9�L�k뼺�i�.�g�Ѹ�a�j������1606�WG�W@����nAL���F"�"H:���6�*�s&������s� ����:?\c�K_[�W-sޱn��k����[˦���X��Y/��c���F�b FK�4�����
`l���ڻ!_<V1}]��<�uu���8.e'�hʜ�ޜG�D�ZEc�a�=3�, hA@,vR�$�Z��o�k�q�e�S�/4q)�h4	�辠�Ehk����^�ʂ��k�����UaÏ�6�ޓ�J�A�S���{���f�-Z�zڧ5�z�O <�`��g�y�G0�}�����h���
�4�;#�1��t_� l>�/1�^;���>��3���	���ɿM>����-:�E���?k�[,V�YJ��|�a�Y�qb�/��I��<_y�+��~O�����2�6LGL�$�����f7{��%>�v�@��m���qn��$�\�p�:��Ό���L!�	�f�GE:~��oo3��ҹ<B��LN�p�|x"�A�5�|�;�߸㇯��	J����O�"duk���J��Έ��	'�hsS{pjto�Qc��/��0S�cDx_��(��8Y�y�sdm�OcFpF,��Kx8�d/N�5��*�=�2pw�L �[ܣ���	!:۳Vj��ds���lN`���1�����]���C4$/]�-|P�L����-�4ݗ���_C9����P��	/e�|��A��-���J�I�Y��ב+W����Gk6��B���^���pn=��{�7�㓯Q"��H<.u���WH�pb�y51�Ț��$a1�Z��i��o����3=�󋮡]Qj�.�I�shp�0�q�݂��d?�H!z����s&�9������*�_����w��0Mt�j/E��FM���+=�^��z��l�Z�����������Z��4ס�Ǯ���(g��D�FE���'��@/o��Ezo�Iz���'�-���m헩��i#���?���b4�/����[����\2)����5r�.H��z��A�{��F~&�Z�P2|΃H����z��~�>����Y!|��:|�=`Z�"y��J���J�(��'�ٞﯦC�'���Z�;���8ۥ�TwYH@���8�ƣ�HՖ�&-�e���\u`뿥����m����[+u�ټ�^\K�&����8�fz��}^9��]�`GlO���� X�����ᤁ�\�v�V3F����8��䒜 #36�4�/�l�v��&~�H[z~ ݑ��wp���bN�x�^�����)���}��]���	q&�7�X��(�.�:�@��"�cA�-S�k��C���%�d��f9bb��&p��k�C�|ii)F��[2ާ@&eK�1����Ro:��٪�^��Nӂ	4���K�C�a�_�:&��FW�	�>��^2lF�5x<K�غ�7��P�u`��(�&�\V�d��l9�n	�i��c�<�'�:���Ӣd/�CsHX� :Kϓ�ʫ�d�G�D�HP[�Aљ��q\���⹕WE��a�
"1D����q]�z�q�]�:�A����5�";V#$�nIǁWM1��U:#���_޲R�wr�ك�M��A�7J ���KvCE3k\�fm���t���/H ��J�QE{$C�[»��h<�#�� 	!��6�C�����t���_HGdvy�{.����jq�����</"�#>��]�H���6x]N��Gh��g0��o��?q%�,�б哸�kӽ��R����Z[��E��S��+ElG��0V��^���K��j��!	B��/�́�\���	��;[]�m��v.�P�{�.g�ʅZ�ć��ʔ�[��t��;�u�;�~�6��b�w�=tR�Z�R����f�
����uVĕR`��J+����Ĝ�����c3���l�����^+�Su��r:��@��l�H)�����W�5�A�``l���o��v
\�'mq���H[����j�-]2�,tمj�S�}�ߋ��p����s�v�e��sM���_z"�� j�7�e�UxFJ�)��q�P�[ǖ�VS���vZ�Uu�V�f��(�?"V?���"����苬\t��34>K����Ǫ!�lȖPm?�y��[l�1��w���U.Ե#>bpJ�p�t��\Y��a9H�qoL�˙�����Z��'��wG���!�B� N�nփ\����t��J��?��O]�0L�5_��b!|�{|�g��7H����O�֑S������-�o�K���)}��yTť̛`MB!
�v<��~#    �e�FE�ǲ���V5�'Ol���Ǚ�<Hd�x�J0��'�x�cL�����I���̎狼$�ݣJ��>U�,�ZX� ��%���7��v����ܓg�8웠�-$ �<�����_�Ť�9��Ÿ��"w�;�������Vu3�h�I �?Ɏ_� U�7?ޥ��x�w�������i��l�,�r���&���)��u)�2t��X�M�w����)�08��-,�ᔼ�f��Q�;n`��G}����P�G��8��䷹�i��R"gtkC�5�'��A����&�I��V*O/�`L*5���'�)�����E�8�C��-��¸�9;�̱d�hp])BH��1��p�%�^n���'Ե�Rwܭ�:M�ZyL�~_�,����[+��5^�$�ZB�l�SVR�����X�S������r�d�*]�3��LY�{�癶*�?l�w��~���U/�KA�)8���<���>��P� ����nV��=U��}d��ٱӆ�hd��"�/b0 �ゅ:�L����K�+.# �+��P�c�2&ƒ��6�D����w\��'f>�B��1 !��/����ȸ0_o�\�b�])�筽A��RV�]B��E�r���)�&�907E����XhTt� FgG���\'�J�7���&���O-4��IP4z�ry����RO
ôD�����rn�i��U����_`eh3�_N��PX�>��<_/���u�E��}�z������W߰�W�&K�9�n�::f�v��㔮�(��Z3�<�6nacm4_R/����F��-��h��]��{rMQ�����`pΫ���3?ī0��!Ħ����umꇟd��+��~��E5���0k`����o������{���Ni���~�j��|pP��ޯh�|�ug#���}s�oE-��iE��x�ǟC��>+ɧ��kNR�RrP����tN�L�"�ZP�O)	[s ����%j>�����㉐���'QR����!_�}�"�U��VVM c>؀R��z ��~��Y�~6Y��# K��.�@��!��UC�3����3��6�{��^飑��:��aՕ���۳��D�hk��+t%���%A5�8P�E�Gl�}�1�?��X��_���C ����0_�l��������Iӯ���N�����Qƕ��}�41��f��Ky���hL�K���k��TG
�_18����ǄW�	��D��&HFtEqUiq]��r�w��I���<_��1lbb��%(�@����i�G�*:��K�E �lB�z�z�D������R���w��i~%g_� q�p�S�K��+Q@[�	6Op�Y�N�S;;�^$j�74�R���R��r�Zd��n��ɼ�J�:�е�#8�6��?��`l�fϚ>��z��hjUj��gݐ#-D1̀�kw�"���0�.��j�+P\H�s?���*|�B��اX,�ݏ��=���Q��:�8!Mc���f�xwR{-S�	�Խ1���|V!n/�,Fei��xʿ����9���N����D�c����Sc�y�D7�Ä� [U�?��=�d��a�ȰlkCvP'1=W$���BqzB�xrɵ�}z�T���鈒���Q~�β�D�C�ɿ�+]��OtewF�g6��h~�b��v�K�2�����=�/?[��6�Q���q��[PNM�~��1��7�_�H�E�Wn n�HR&!o��Y"�<]��ߚ*U�g��k�!����$��*��α�و��w*��1'�E������g	~w��V*���'a�^|��~���c;���V�0�P�0?��	z�M�yA��ӈ�0^Gz$�R(�-���ګXl0���
t3�^D����Ɓ
���SV�o9nv�O��Xp�gA� ��R��!
y�̃�i�37�V`B+�>,|$�S���+�R%�f�)�[�l�q]Ͱ�"JO��[Nf�����8�Z�B��<*���9��0��c�S����5`>��Af.on��{��,J#�\$ ��ͼq��e`^��)�Gs!A	��oO��	T&PQ��
pSە\`�><��Srv*pQo���'z��'�!Yׁ4�K-��{���: �.�?,�_[�\����n�}i凶^��d��+{ܣ�D#GgB�����B���}����d<�P~y~�.���D�!;�-�H16r,��B8BF���'M������P�\]����Z��?n{P��ݛ4W蠩�꽶^�������lkG�j����G�8���S�z����xBxKl�+󧰿 �����b�3��� �dv^5�i
�:�ݫ�.x�^_s�T���YnSz(*o�t�K%`�Y%�h�{���o�9�E�\�U�EUAZ��2ϒ6�q�↧I�?.�x?�|3àŰ�R2j�x�D�K�#
nF��5�T�F��Z����r�Pԉ�I�b!���ϓ:��Y�w�o8!�S�a��d�~�4!�6��Gdl�)e��C3i�$�&��w���:,.�����8{����	�� ��_1��k��;����4~{��8e��cK�
w �%f�F�6�SC��G�x(C�Ɏ��¯�Ol��4;�]z= �1����[��
!`(s4q�/d����5Գ���{t�V��Z��b�Y��e�![j�ȬM���=�f,���Et�F�J�3(�Y�Vd�+�H���s��Q���m>dPLUN��u��T�b����5q�5Z���v����񯀓�]W�0~�Oq��[�p���C��[��͌��HZ�e��UD���d��YQ{�j�|n��]�1	5��b�X���	�v05���C\�z������ϼ2~A�3�Jǌ��?�fg997�a[c\/����e�BH�O��%�8-�D�9i�T�����?�{otؚ󎅸'k�s:�$�%r��pYy�c24���[`�J��#�g�M�x��6��t�>�7o� ���n	w�ֆ~�8=A�sM'Ѣt��.�����B�&���;�N��T���Ԇȃ;�H�U9���_��9i=�:M��j
2�Jg����/!"U�05Evuћ�hu�]_�=�F���K�*�cq<c���~���$�?�je�=�c.O8����W�W��u�f����v�,U6�?����)t�r7�����)w�2Z5�*��){�$��R�Db�00�Yq�df���}�*"�.EXF9d��K�_J�{��g4��T	0BN��{gj��N�����k}K���MQJּtI͢Kɲ��%w��$�?���^0@�־=����9=�B��)��ٺoC��@�-��av"Ԫ��Q���>�K��W��Q�0mZHD3{�������{�l�����Z!���E�|텀��Z������,; =��	��<����S!�p��|�g�e�����#[Wڍ�^�Hᡫ�]��7�����Nw�����Y�}���8%���d�X1�t,�5�ap��XbB8���f�W`�>���S�g����F��	�<4tX��R��|�-E���/w|B���b3W����*P�1-rYA�Zy4��U�� -��W�v��F����Y|Ç��T��}lc8|"�H�j#�����q�Ė�|�%� �Vu��ޒ*�g�}[�k���C�v�x�=����&ͮ��Ɯ~��[���׋�ADY�E{Ec@��D�c�w3Ƀ%@3'�چM�SJ!�h�������]b�pG~�C��?Et���Ώ�~�~+B����ق����MД��m�9�"��1�/_w��F��L�C��7���E�\G�!��������G���nl8}TfYu���dҙX�s/|�U����| !����٥~>�(9��,dU���J��E~�,�uPƈ��nŐL��D���7��9-2Q����=Ut�2�p�Χ(&�cT3���g��Cġ�&3�5���`���c�3�dB}���l�"��fG�.0:�T;�n�Z�����
�)hu��2���⃕.��r<��{DQz�    ��b�!�l�n|�U<+OJ��P(���ȭ�0�C�l.�:��J�̈́Yk��=Tz�n$�.�C�|�it��v��`n,Yӯ�U�R��~"���%3���������!��I�E,�:�#*%�?����'�cη��1�[��`��k! A�T��fj������r����:�.t��y����{Y��������<�-H.�{YLC�<��xh��Z�/�1;�󇴻a)��xo�d��T~���|�3cI�w'��_����[IA���B���tՁR�������`������ψ1���;e�ک�31S#P)$�n�8�
n�ˁ�tl߁h�i[<d�+~W�]8	՛-5�	,��Y���v
ޛ}l~�{��D�R��q�OE�����qND�f���&�	K�r�LA�O�Юx�L�ҺI��Z��!{F�7����ӆ!��-��Ĉ�F�l�?	!���.�����ĕu�?��Ew��fGˆ?�S�X�:�8�������k��sR0�lkm��r)3ED s��Д�~��O����=S�v�����[A.���2��-**T��	NA~�1�3S�'<>T�"�iZ",ΪP��N]��,e����c�)S,&([]Q����� ����ď�qX�ٵn9���O(�7�d�̢�B�?F�fgϖ��#�9%�*|>�]�fZ�pZ7�P���\ N�V���R�ȧ�È֎1�����}��𹡗�'��*�/v�ƣ���qW���_JDr�w��ê�]:��f�����	G���)�>�{��E��!T*��v�v�g�����/�*t�BQf�I��Q�Eֆ�)h�Y-p�2�.��1����1y;�^QW	�wy��%W(�^��S��d��0V��L%�)�o7��P ��3f��f{�%+Q�$L�&�8���v��|-wܝ�D u�f'�H�&�e�$'�3��$�<�T�
�vrޛ1V>����:JRPk��d�v\�<�5��[����]Z�:q*�y�2�H���K�l�Z�i�y��Z#9a�p@ӊ�}uq��?��&�"�0ύj�3����~E�'���+��9���H|���i�M=p?��z}��N�|ߧ
��L��W���T�@=$�m��.��/�[�0G¬W�ݠ�����r�����^���4ǔ��~L:�m�ª�"�α9+h(O�JD1�0�%S�e ՞��:�����&Md��@)���#Ŧn��.㓀������\J��\LJA�Rh�Vs\HÎ3|{��ۀE��SRז�/��w|����I�Nj\e�f˓�Lgy�=�{��S���7�����z�y�ozm6d
Q�ߤN<	JTvI�s8������H4qt�����Э��Kw���\�%[XQ�Q� �$�.��w�P.1�� >��0;��9���
S����
���, �F��sh3+XpS�CJ���-�U�J����R�.Nu>�u<g����2Z��B^/BB��*z�V�6:ꍇ����������{a}��Q3���N]v)g���N��V{�a�D�r�-��{�R{~i��jaJ �|�c��	�\8���.t+jI����Y���Bz@�������B�0R.�K�đ�T��C6P8�e�㝁"8�p�_/O�=�|/XkD���V.� �$4�$;G�n�a.��aM�<�.®rrYA����Uv��g��=%��B�V����x�IM8�21�E�l�|��Q5���Ѷ�c�� �['6y�Oq��_*
��6|�A�<F-�ޘ�;����Й_m�S}'��乫�(�R�	 0�B��׉�����T���B�*#�Q��Dǽ�t�P�L� ��,g
�1����Yt/t%�q�S��[(s���|L��;3{!�w�X��<������\I�[d�&�/s�u2VE������Yy�|�� ¸,�/>>�1
��P�^Z:[�`OBp|G�M�O7��l��3��X��zD������eUl���F�E��Z�=����%�tQ�*la����5&.۪�\��=s#v��~l�7eu1�D��{7,����zy����������&PJSJ�
��O���O}IRKUN���"Y���&��z:�<4��4�i���󢻗�>fc�l�U����n��ym"����+p.]"�_��S5q콛c����DT� ���{z`t
՝Q[���%y�4�{>�	��0dt)j'��<���^A�J:�D����b!�t<�;��;���N:���=�6
�Zn�B�y	�.�6��F0�\�5U��wb�	e�j��_o>�.�W��;2�'j���)��W�_�	��l�53:]�[8�C�Y��g>��v!	�N	.��Zb�;5}�nt�X�Q�2�����]��z*�~��狂��cq@�iro�(�8��
���;�7P�P��iS.%`جUK];��|E4��I
D��4��?�Oc����׼ч;3�	5�EA7p���=�ZSN�+8����O����O᭾��
�������%�K�(��`|�W� 1��߶��Ȭ�'�����_�
|H
���2��z��;�D��P� �QfA�
k���]�
�6��|����	&mxsNjЍEC����� �L_��R����6�N�)�W`�Q�%������R�J��3�Oz��K�p��ݠ(�`�&>������:������v�1gƽW���%�r��z�`���Z/ApRBb�!����>�M��\�%��~��G\��6x������nt}�f��ǳ� ��)v3I��T+���u���L�Β�%���_�N{�M�:[�%k5>�.��rpw�[�#��3t�M�b��E3c݀-�Kϧ��5��;}����~��^�Z�!2��$�C+�غNSx$q������� +i�fWL��h�/d�G����u�EyzG��:����d5�����Z�����3f����z��G�b>�����QL�l����:8T��a1=/"�C��.*�BQթ�â��ё4�����q�^���䔄�9����xL	W*tI��lo�4d[a�PNF �8���ۭ����2�9�R@�
�R�����=Ý�uE9�g��ьp��O��^�!DM��9��5��%2.'.G����s�
vYE���Kf�*]*�P��j�2y��/��]&=����N���@R$�Rv��=c5�4�
g䋮�:�N2�.g�R��8ó��"�o�~�LzZ�A:����n��.����Lʤ�r7��HH��K>����l���v��+p�����)���	0<��Q���0��(�h%���y�j��),��&%�fn��������RMI���N����K��uaK���,=���o����,�j��Y��"t!��0��I��ǭ�q��1�;�f���(�'�_�ZT�T'Z��;��[e}���ׅ,uvU-��vl&u�����.F7͓8'��6����~��Cv�k	�@=A���7t��)�A8e��,�6�%_z�rҜ��e�5��R�a�9+�S�Z-�t3Pdc	!�6�[�\���(�m��Zy>�+_q��>��0�d��Q3�P�(D~X|? _��k�>��M�hf8��-��@� /H��l��4AG�h��y�y�?un��X;\
Ѽ4l�:�/�DFJ	v6֢Z[L�Unsf�.����qjK�B�4)��󮊏��[D��pX�t��z'hB���:C��{��$��}�����\��PB�*H֨�Row`[����'�%n3L#�r�DѾ�J7������|�mo��w���"8H����#�P
� ���������@����|�+kJ(����?��B�*g�hw2.#m�"c��f��>i1�Z�[���/�0���������c&t0,Dml���4+�ǣ�|M�]�6����^��~��<����u�rZE=�r���E�X�f>����J]�tЙ��Œ�ڸ���~��\^ԓ1A^�iM���d��    ��� y@N�a�I&s ߮�B��&ɕ���Xk�z����.u��ـ��4ɗ�~ڛeێ@0
MDG@��Ôiw�E���	W�D7�����e����G��(� �~��'��Y��P�`zY�^���<���@���-���'_��	E�?�]�:�a�˥v��E��������E�c[��x���t���v�x���lfc)w�M|I�D$��*�-!���C�hb��p�'�~OC��Rxos^09xfƴ�%���{e�o7`K'p�
'���p[ߕc?�,�p�6Q]xH�P�
�Nl]��h���_�lɖZ�7G�߫d'���Ci�Y9ǝW�Q����I����IѭBt)��CmQm��a#�]��2
����"\�-.���Q�ݛG�"�?%BU�����'Y����âi�Mc3�iX��WA�)i?a��g��r��b;�W�Ti]]��]1,��1�nC,���(ҵ,�_Hvag�K�����L�	�'���f �
�b��B�i��.	�iR_E��3x��,N ��s+,|�\Ϟa�����'��yqe@�9��8X�pVm�B�����yL0�[�Y:�X��J̝�"�O�Z��ªZxS����5t���E�iA��{�C�?ʨ�K9˗"4��`	~�#ǥ�! �@|���&�.]�c��t" ��{�Z?.[��������w��"�Tv��(��D73�V�xxR�U��9_�u�T�Ni��`�ą*&# BH�<�e3ոn�X{�dD�f8L�p̉(�.3�J�/���ԗ ��+$��7���_�2흺n��	�N�&���4>�52'�f������q!�A5��R~�y�o|g++�����&�\)��_8��̓[�U����l�p*�7�
4�����V�W�Jq2n_���HV6Q�Ձ�?�ှ(�&��U�^~�e�;�u�i{�y��sL;.���H�C����ڻ�OLmҾ���������A��Ti���SN�"DW+������ZC�{�`�>��Y��</{���w�*�{�v"C��P�7�J���3�G�dU��D����E�����`H���s���� �e��b��ϿOBȖ�2��ް���j������eK0��I�0|/h�X�˒���1i��7�\�@��e�YGp2Qo���J����B�zyU��QQ����)��A	�x#�=)4�њ����B���6@���8}�x�j�߃$?q����b�����u9�X�O�z��1"_th��p�/e:nGG��dӱ�;37�W$<{��(�����7%��Љ ٓBE�I(�����]ӱ�;#ы���-�s�<�@%�s�oFkTF�AEg*����=Jh�!�)�۝�X7E/!k�@��^��D�$�,����I;�1!�Y1��8Y��!K��)E��q�&��2P8-�C�6z�`���2pm�ضj���흌�2B�+�!���Z���Em\`<yvb�����S�%�;�V.�ע�����jkh5Q�����������pI�!�_~H?H��ku�m4�X~O���+��T�]]����'�:�͐ +�-��%TG���  l���]]�"���vT�zV�5QmluU���Vs��H�#�"C�i�,4�P�ْ��S����Nv��G��:��l�aBG�w�ҏ������=)�n���֒D�) ��咚�w#�%!e���5�l>Z�6K��;�� 3Y��o7�e�XCӣ��@XnE��{cJN���*=]��|vX��s� ��v��p�`�,((���%��`�>{�7��Ltfv�y�3\���$�.!�pi�I��&NKw�-�;�LU �	�DHN:JD���V� ��w��S�
�_�z.�&#p?���6Ȼ��HG���}���[�R�w��y�6,�$6��zʇ_L���K?���2�7�l�E�&�ZΖ��ԡC�uJTߦM�p��{㞵�|~<�sB]�� �~Gп�L��][` ��*!�t�������,j#�ʕ���\m�� 9pA2�;>��Ա��=�|��&�O�LA=�W)M�RK�PF-�'~���tz)n�`S ~��YdZ�� ac.ϳ)T@�j���Zk<�;�.BB ����5�,V��PHta_������N�Hj ��'J�B����7�d���J7�z/�۳!��炙���Pw�\1Lڎ�'1!��bkC?��|�2���\�g���\]��-(b�<.!��t����W�FV��Nv�V�%�'��wb��s�@��$e��(;�Ґ���pz/���[4Qd�)%,r8|�K�s�;p�7�4 ���yK�o��-g�$�L���7�{�ꭃ�R��]R��IH.��%����fPa�T`��zm6��I��������Z"��I>`��(~P��`S�{*�s�C;��?e�8�Γ�XGO!�T��[7S�@�G	w��6?�8߱,�mB<���.�B�����|�ʵ/Y� �����aw�A �C\�^��p��Ӧ?!�FOӦVɧL��4Q֕��atE�x�?-%��ֆ���&��tzݰM@����dwo�6����j6n��v��_(�#�W�\�)����������_�b[�~@���ŵY��5^���c����������1q�n'���*�(���C���S�LfUR��`��aW�bͨM�����u\�gec�a���P#u(���Q������66�����钞E�^Ѫ�CV���d0i�U��Q��P�p�+,�J���d�ͩx��E�շl#S�[#��.�e��q��Gr=Nȿ��y>#�=.�t`�X������M���Hc����Dy[Ec��Z��������\���4���$�� 	<_e�8Jr� �L����5����*=ᆌ���P��\�l}!��x�NɫԽ�[��{¶���4T�~_7R�Xwl��~w��� l8K��	�\�������m7ڻ~�����y���1\d96U�@������8%�zo3'��6/"к|_zԆl��2����y��Q��j���Y	����+&%��Pɐ঒`;���N����ʓ��F�Gȗ�iNR�9���Al�w"k̿�~�#��}&�8�*�T���8 b�5�!�r��C]�h&H>1Du��j!�+�ƕ �\L���QO'_ �+�!�?,ct�ס�B G����O�)�3��R8 ctޞs����u�%İD*��вg܀���S�l�lv�j�}卅:c�~�q�M7��e�����Ϗ�3^h�鰡��;�M�{T��C�,�z�t9#�b`��A��^FYE�S��i3^��g�����[A���M`Yea��jr�%:0�^zu����nr�vQ>*�
�b�@}T��Z��W��z]~_���y�:���0�z��:Rd�QOq?>��q\����������ᬩ��Ƣ�$.�c���޼���\]�; �m\�B�� ���vv�		,�x['uG�-�}!1D���Wԃ2l`�6W��S�A�TʓyU�=�B����Z)qRKZ��qBj7��_���<Q�)N@q��ȘG_l7�����&�!?�p=�P�K�B܋���#������
3���$8���e��c:|E�ӛ�g���<I�I: �"�qV�8�}m��P,�Mau0\��|�&�.��ú�oL�_a�0Kߩ��KS�H�{d�����%���-�z��N	�񜃈�;P��7�s�O���L�{WsQY9ɧ*S��_�q�(jc+�R���MS$�������C�r�dΉ�8R�y>�W�������1(B�*TG�>=�m��M���0Y��|Pk���_ �>�&7��l��xI5m��k���1(��X�ď�r_
3����B2�~��\�y�e�W�|��	$ 8��ͮ��c���p븿�����Ai�����W@V��'ĕl�,X�(]؞YL�t�.L��r�Ԇ@�(K�tS{���Vk�)�
���#r��ڏE�T<��n^��LU���-%E��.Ur�a�T �  	3	�Z]� �<$Q[/�~v���:��2����f�`��v�+*� �Z�� �ӛ6�UO,�7�����N�/��=�M��V�n�ٚ�\�w��{~m̋P�xÙ���>Hh�g^��~����R뒥����K�Q[Ù*+0m�1/��{�VB��]K(�cä 2�8&��0̫�C�HD c�x׸�8>@�yZz-z�����cIV(�'�IE0�Sv�z��XIbn�lȰ��HS|�I�_�{�n@��Ţ����G��iN�W�ca��1,�v�1����2oT[�܁z/|_s���y 6l��?���C��O�'�G�3j�)��� %����B�[k����S�ߪ�s��nK���	F����]��^����DRd�j��ч0w��΃��뀱�AH(|��#OQ
^��p
\b�T�l��Ry}NH؇.�f�4xM��Vf�:��_�	P��;_d���a����8>g�IW�c�\��>ũql�%P���`��VW�5i>z�AK� J&-��v%vT�B���:�<x�F?�Wߐm�,�6�t�f,TS3�~
7-$�:���bx�W��d��n��o���	q�w�*�����@�t�p�n9�
�y+�,j��h�:Bk|���߷֋���eQ6:h�d�Q %M�ἃ*�-!����1�����`�T�w�b���RK��=g[.ԇ�Y�~Y'��Q-�yZ��u���Z�2W�(�o�~�&�t�M�aj10�w�"���! ����Y��\H�^��Ai�ct�f��y?��՚��xHaZ���C����_��~Ђj"3Q�<]�8��;��|~���.W'm�(�<8���ݐ%\���SJ�l::�j�|_�������W$��^���y%8��N&E���C��u�)�X��˶�]�����1 ��Q=+��K\S�0���yx��2_-����;6�L��ȉ�{q��ڨa����`C��#G���[yZ{�.��V	�d�F(�R*BE'{1f���a��T8B'�!�4�FYe����������[y�ig�hϼ8�u8������rXքV��e�Sb�Glx����Vʽ�wND!D�_+����H Bj�N�G�������*�櫞Q�"t!��H1��+|(�����Z�дpЇNĤ:�s���M��'�S���_�����Un�Q�.� �sJ����9�!�����50�S'�7IP���	�5�< �\�n@���r^y�6���4�u����?���s�s�U#���Qf�2����}	+�IY/��eq����FL8DIl�Q�������_��_I<�}�J��e<<q�q�O��~v�M�َ]��V�c��yp&�������	��l�*�@��j)��R�/+4��v�H2YՠNX�VyF�A��| #��M�I�e����v|mM�vzL�D0��j���J$Ӟ�����5��j����긓n�<	:��y���[ۥz�M˿%^h��Q����cT���73"��!�w�w�?+ Q�4�_�ab�c�=p06�� ?0� J\���g���@U�\�tL�z !�2}���K��}�W��
�=�:D#E�lcNZ/�+r�����:�+r��c�6�++�ɜ����"���|<�(��*�'�ˉCN��һ�p�GXΊm�p�(Z:^�Bb/�e�31�Is���w����+�3W`s�.�)�:�*�=���U���a�_0]�yL��,~�������@      W   Y   x���!�  �����ۀ#z�dV�������y���=N R�ZN,�C�v�h6��^�1& 1� EG���(y�Z1.�?kwo�� J�      X   V  x��TKs�0>K�b�����[H˅R��������D�$�Ϳ��NB�.ew��ۧ�YZQ#��U�Y۴���
��7��P~ŀ
�;k��`f�0��;G�*[��8���"�ܱ&��C��}�����:/�i6e���R����&��n�yqʞ���cl2���y>���5��ƞ���c�k��*dcN�?���(��uFU�.6Ϋ���**g�RD�]�!�5\�5���NhX6Xm�
��Ɍ�7�	+Gp�D��A��(a��޹^\0g�v���?[�ۥVhc��WW�+�@T�6����L�����+�(��h=�s��)#IU�" ���2@tP"<
�$�z"U"��.!�?��O�kF
��m�]��X�6�H��Hٮ��W���e����Pv9�����>9-ѿ\�!!���P��0#���Gɋ�E4�doR�w��9���0�T�������rS/f�ך
vw�G�jЈ����S�tU�Im���P��H^��Na�K���-�t����Ӿ5�y���,m�0������!�����4�G�!���T��o��)����
�[Zd���۠����qΟ?R�j      Z      x������ � �      \   z   x�M̱1@�ڞ"}D�sCd�k01���K�O���j+�A�H �k�S�N���Ё���v�x���?[7���a�,	+��v�IiԬ��Ca0�MW���j�O�]�^��/�&E      `      x������ � �      M   �   x����
�0Dg�+���lٕ?�c�.Y"7��O�C c(:��[]Ԑ3�:1oJ�7P��Jq�"8X	F�&�Mjc��5q~ #�DL��A�f�#]�a?�?*���k?��z�Xo��X����#ഛM��E���9���6�      d      x������ � �      f   O   x�ȱ�0�Z����>$�Hf�I��?���i7QQQ=F��	���o�8Z����o���ls�d/me�J����      g      x������ � �      O   h   x�U�1
�0 ���I��XGgG�R����U�������*'��H��r�$EXJjͬ�f8�z4��bR�|E�;�}W#���C#!��nӇ�k�fb?      K   �   x���;�� ���"�jм=p�=A��aY)MJ�Xŏ��(�)�.+X��6��\`i�*]�SO�H�:�&F2�$'��bU�^�X�TI8%N��Q��G_a+��+��|���������_QЁ��\Q+�l��Wʋz$EvS��d�@z"��GBt/�ƺ��Ќ%��H���H�r���rW����4Meu��      H   �   x�m��
1E��+��,s7�I:��FK�dM��m�{�� (�pOs�R�sq!_l%�y��q�;'��tuz�k{���X���jfx�D�1C2�$�혇��Xb�3#[;�/i����g����V���Nfj<jGr���(�,����o�����2�      j      x������ � �      l      x������ � �      m      x������ � �      n   Z   x���1� �^Ao��"��#,�m 9:��H�����\��Қ��,������{�k��4�� (���xF=�*7`������U�!\      o      x������ � �      P   v   x�����0Dkj���#%��.d7-�?Bb�pk��w�Z�~Dg�q��4m���VO��LCs�����F·�{�mZ�)��%���	��%U��7������	�p�7�B��^'�      r      x������ � �      t   l  x�U����0Ϧ�?ɒi�W����x��!C�ll۰q�y�����6zõ1}�l���'7Vcn\���n\�l���0��c5��A�dg�3�M�E��A�V��9�����_g�V�n�]��������ټ��|���Yr_y��R48���~�ei�ti<]�Q����9�a��3��f��	��Bkaa^(+~a"�&��
�!C�r|�	Y2̔&�Ly2����0S���3F0̜#f����-^�����ä�$¤�^a�1�bJ�aR��0)K�Ii2Lʓa�w3�Tof�)Z�(SZ�Xʂ����1�T6S}�0L��f=�P\Rd�%�W�%K�Y�d�%O�����U1�      v      x������ � �      x      x������ � �      z      x������ � �      |      x��][s�F�~��[��E��&Ҕb���qR�3��F�!qC�
����� I�#��U�D�}������������Y��yy<�)%���|Z��~��ϱ���S����Nj7����yu�������rrt����t}3�WgG{�.�Q�\[��bv���ҕg�EZ��}�M��o�L�������.ZW��,n���ԖOIJF'�����t�:��^��Q9/�%G'�]��+�I��JU��~E�c�ǘ&XL�� �J�����+�<zu�n�hZ��Kzy~���z���Ï���;M>�?��ÿ~����瞧W.a�w�%���ofSW���]R/�r9��<Τ�c�C��z���k��������|Yw��|��O���i|�pvY���lZ6wͲ>�y�ZS�d�pUi.���/�@b��2f�ӆH��SF8�����4�1K���S�x�c'�U!�GɹY�����c��͋� HJ̸ryƹSТ��x��f�����~�������l��i�|���I3���k��k&��Z^S��3B�X�j�
ŵ¯y|���ɛ����0fSM�b������N�-�[�3�=on��b*s�H*W/��������G���#�{v��ݛ��,�LP]-]S��WKZ�â�u��(��U��Wǣ�b��ifK(���w��r�����O	��a&�Y��T
k�'(Q~@�#*z�G:>��S���y���Dɒ+5+'�IFy��.DNm��!N(��Fb�fa�i�g�QMmf2�̴r������b�fQP���Z�h($@|�O�¨���;O���p�q|��+��h�抇,qwjٌ�d�z��Q,j	��p�kˀ4����"�eBi���ȳT!�7k�(A�r��&�
+( #'[�"��@>%U[���}�ڗ
��bO��v3|��Ov���(I A��P��3�9�ՖDz��uI��0�u�%ڦ�;S�Ո墟I�����@F�B8�IJq�y�Z�����H%���H��"��Fv�l9�,fEf4�Zr�L���2'���צ~��N�j��8�{uI�㠫��t?`�9���v@\��&�Y�����z����\�����V�UD�S�]cRp5�؉���� Mt�&yn'�H�1J�`:�4e�I��
�X	"�ć�C"_�$RD��r�f�zi�r�Ą�"J��铰n��fD�ў�O����6nH�hH*ms�K⮴qm�f4�2��E���EHӜ��V�`�2��RVq�Q�IK1%���f+m+���F_c���>�q�C�t^#��)v����A`�Ծ0�m�U|�}8��gJp�� �n8��x�_���bAy���k�aBԝ�n���@�Oע�+Q �M��c����a6��J�E�sa��C�Ze�ܸ�-���(#�T�ZC�ΔD�����X`��ֵ \ 6���A�����;/����
�(z�uJa�7iw
ڎC-�=�(-H�3��4ZD��~��qQ��()������2�3������",�,4s�f�I�sML�((m"��6�lo���Ş��� �رRФ�W��byqa��-�p��t���!D.gu�70N�\�dL�f�o���4fE�eXc̍lP��G���S͗�Me2Z��)�Q������u3�o]�W�df�d�o'���	��_�ًS�<�3jv�O�"��;��3��k��mq}P�w	�LSڃ
:��FH���gz�g �e���7��v����^aLX�s-X�!��3��ƿB?�y9�zǛ�^�c�R�(�A;�p��EօS�aI�H��J�P5�:RJ�XI�0`���<��~�{C��F\�u/7��ŷ��V�m�m�Ik���I5s'?}M>�*�����ƭMAh-)��}�A��{���#\��p�BK�V��\骩���������K�L�f�COOx����j'���1�O��*�):$�Eh�ܽ�V���RMp�ԀE���KJ%�y���ﶤ����}�V�%�Ň��o!����B"�2�_KKfY��Ē<C:���X<ARk �6+�͘��mF
A�g�TV�� "ʠ�v&�J
��<�{� W��T�~Ȏ9Jx�� U>�D���"dd����rXu4�"4Įp��a��8���ܬIL�1S~}e��	P2i����[��N4��<G�3s� d4�+63j73�F��O�ӕ�7���]VetWu,�բ~�\9m�����/|��z��U.��]��s��d��&���E���[f��X,�O�l��fq_�ޡ�r_C��e���o�إB5�G+��Gh����榼�w��m��۶��jK��1��Pt�5��l�2�D@:i$Q�p��.S2�J!���#��B;���ܹ+�.=Ņ����5	@��E�L�G�^٩zҿ+�aJd�p�������=6��p��v��&�d�[�.x71.+w#ƥ6_����oLmf�K�E�nZ��,�f��9w���K��(^k�S&�q�5��9��}ٌh�c�jEi��3<B�_O���Q���C�d�kĘ�I��Ish,3���2"ٽ��0r�H؉[<B�C�L62���-y{���B׸»��/����~|;Z��*�g]��K�5���������9�V����l�K���M��jW�]�|
0�-�[�<$�a���jr}Of�EA��Bi���k��S$�$lSz�՗h��
�K���$�zf"�}JozB�ʰm�"^�!S!�S.��eMbp��*�Qʙ�0i��\>;q�	@4V��GD����ډ�M
��yrpҾ8�/�G���1��<��N0��w�]��^]�m���?T�s{s	nCM9]4Ɨ�w���q
���-N��U�U_����5��t�b�$�_�iye��t�$�v�Z�4��j�qz��^�\m��\��Ǉ������ �ﯷ����kH����g5�ݶ��4��4��ld��i�6��L��}�6�6v����7c<>}$�����݆[	[��O���m�"B'�s��.���i��+1�s��F��g8�Y
�v�������̆��X����D�ė'5A��D�p��٨&���	v8�|jO�fk2{?�Zһ{4Q��=�xЪ�\j*�(O�B��]�3�y�Y��Ie$B���x]c}���_����6X�4!z��*�8��E������:�b&���h�C�e�E�L�F:� �����Ii�i�p��p��k�R�Ao~���%H3J�|u���%��!J3?�JB��x}��8A���"&tض^5�.q�Şr� T��5q�C��]L�vL�x��8�Pb��ΚU�: ���Up�} ��r��.�=��{&8���Н���o|�m�qy�9.'?��ԥ{�A�N9y맷�A���m��@Z	�38*^�V��](l��a�g=���~A��I��;X뉘`�d�AJ*B�ٵ��l���h2��y�T\תa6*c�iz7����^]+��'j����8�p�fEB�ӝb�i�Ձ�����=�M��$d�fR~G����WzH¿<�e�e�y���h�,lm�2���m!xL�T���`w��� h�����;��	��҃����>E)G"�7���c}�|߿gW������}
�Ƞ����Y=x~������j?�T=�G��sRi�sFK4ߚ��o|���~�|֧�6�P���w�AP�t¸
:\_�C$������X���O&RM�aK���qD� ��T@"t��O.�Y,��� T�z�ӄ�}^�܆04A2%�*�Y�Q�ި}B�Zi��E�z�{����Q�U�:V?�����?�jW��U��i}���������r�����������bs�@�F�_aQ�U?���Z<���~�x���<�5]�l)"SƸ�A+}E��zhK)ΰ)hcq�-S6&0cJY�s*� zG&��]���b��@�+�D�`X����!�p��s���4�Ws{^���l�^������� �  ����Rw�����a�n���%��M.g��
�#Fu ��4���ka��v4
����jj���5�f��y}~���rZ���V�WW�������}�R4�<��G�0us�ݐ	�!���+tp&F�����+'��];V��ll&
f���m��s�J&a1�����+"%p�Q���а�'��Tr�����(B�]�X�JѰ�G�Q�.��eJ +���]bABE
�d����=��w��2X��m�x�k�E�J����2�"�N|Fs9���x
�v�h�"UB��G�-���g��c���8�HS�B�6�Z��yi�z�YS1Fb�QB�Q��D �W
�k�R�W��gg.O�˺�H$�L_���{���"4^.�9����u̲>�����5	�\�0Jl����Z8Am�*�UK>J��S���H�3-]VX�@j��p\3�GɹY����9+r�2YpVh�/v�9ʍ�мp�8F��`w�A%߼q�[���{��\cxZ&�}�V3����tZ)�VF���fݾ զd�-�l��7��Y^9��\-�F�?������� I`� �ș�|�$m���@8I�7��V�Dh%�
�+��ǷO�<�RD��ƧbG�6�ms�CŜd
� 17�#��:���S.H�rn����c����z�
S���D���郔a$�"$=�w*܄�)��p$�"4�~��:�Ͳ�Vp{$�n���=d���Jon��/��g�I�)w��R
�ێD�=��A��͗��7��bBiJ5�(L���w����=��/��o)w1iL��)����FBrw���p��M�.���T`db�36	��t*�(L��I�o1hP"Hgb��g��(�p�� �D��th>{9�rL�
҆I�Ф��{<���I�]��G�}�����7�vZ5�5;�-I9��D#�n
�e"w �',H+!��J�/��r��&T�>���1(�U�)�!Bi�rR��8�0�3\HW�L�����#G���hL(WVY ���SA�֪;�p�1 ��1Rc��&TM��L�/�$�p� ӷ��S�h~)C��>�e�T�G��\�"d��%�=�h���������?|?�JҢ5ކ��x��#躬���0��=�j�7������#$q��k`�/�3� �D�0�<H�E��/� �0(��'��Bܧ�������!      Q   �   x�u�=�0���ݑ+�q���]�Խ���@zÓ���Շ1��E�N���,JO�ԝ���wM�z7UwV��#@�3ՕO��Y���LP�W�5�Ҋ��gz��R��C8���Eo��J�f���1������tږi���:S         H   x����0�3SL��c;���:�>�FB E�Bucn�K<Q�m��d�b�����W�݌Sh9��3|�ݔp      �      x������ � �      N   t   x�E�A
1 ��+�A@=z�6���J�
�{+�{����T��͑����*�n�%N=�wpI�Nb�)�)���
U~t�ed���"���>`d?�x��p�f���_�6���0K      �      x������ � �      �      x������ � �      �   ]   x�ʹ�0 ���%(��,�i��<�#q��8��\2��,Qm�{Ƀ:y�	ֿ������$���,�L���2�
bSSN��낈Fc
      �   6   x�3�4�4M3KL�HK�M�41�512JֵLIJ�53N40MJ�0��0����� �
�      �      x������ � �      �   A   x�3�t�,JM˯�4�3�2�NLK,�sL8�S����l3ΐԜ�Ԣ�|��`O����W� -:�      �      x������ � �      �      x������ � �      �   %  x�}�ێ�0�kx
�Xs�!�R�J��V�`�P��_�fAfV\v������4O�㏿o����8@������� e@ޡ���p d�����)�?�
��e�����H�$�e�ZU���ZE�����}�f\lr�޵��,6;���DPrZlq�ѷ�s��"8���E�;F��8C$ho	�XU��Ņ�-�h��6���F3Y�E�KL��Y�\�����.� �W��pɑJ/���{�� q@��Gʁ%nGd���ݓ�9wk6����9IO1����� � y��#���9)a���������6KP�1�����t���_瓳��%���$f%�R/.#c�QZBĬ��֍����ϼ�;�d��HZ!�,D���BV)��M̰�cb~�yDLڇ��Y�{�4I�P}ƛY��|�K*�7v�&{+1�vΫN��r/���Ϩ��߭�
��J�B�,����2|^)~�ĵ��2Gj#ADl�353����.%�XZ�Ԉ�26�~��8S#a�:c��P��U[��U;�ΐܕd�j������fV�      �      x������ � �      �   	  x����m�0�=E�>DJ�� J���24��E
��'�L�c�.���W	P{���0Ѥ��}|��0Ʌ�p�Ą����K���ݻ&��=�
-��\!o���QKO�M8����ف��j�V;X�)uj��O�|AЀ�¼bX9/ʬoГ�z��+4G�x;BI�Fj���	�V�%d����=|��nI1��L���R}(E�p¤���b���c��{��`HG(u�V2oi0O��
���zH_�^�y�? �ƺ�      L   M   x�ʹ�@ �z��=�^L��7^��K�,�Hk�,s��5�\��
g�&��\���d�,����x�����      �      x������ � �      J   q   x�}�1�0Eg|*0�8c��'�B(�:��R��K�����׳UZB�\���Cch��
L��0����_<?����td��@�JnBڱ�W�����E,ϵ�����G)��$�      �      x������ � �      R   �  x���[��:E�+sQ�)���A~,?�?��]�e�6��$����Z���>��,�t6-����˷r��N~U��4�����������������+ap���3�/�_�b�����_ؾ�b����7a�`����<^�З�c_�**��2�j&�f�򌿢	(�L�j��/��+�����6��&�n���I�h��ک��m�y����:����ڽ�m���18l�8�m���a�08l�2�mЊ�aT08l�*�mPP���J�AA͒6((Z�UK��@ْ64P��m	'�q�<K���m�6
ϭ;�1MθM08lCS���mh��aZ���18lC����6cp�K�`�dq�,i���%m0P��ʖ��@ݒ6����q���m͎����^�����W܆N��3�m���6t��]18lCo���m���6t��T,i����mpP������AՒ68([�uK������go\�ཨ���Y�b�6vm�F������6����08l�`�0*�m��a�bp؆�08n� K�0P��6P��-i�Z&P��L�qI��#�DI��Zyٻ������ڷ��yp������atX&��0L��F09F���[V�����L�zAt�	^o"��C�ʖ���-N3(\�
fP�,J�łA��X0(^��>���M=�h犍\T\v+� 9.�N[M�cQ��XԊ�q,�`t��Ǣ6��cQ��XԎ�q,�ct�:0:����e�T�$
��B@�X(]��b!�xY,���۶�}}B)Z}�qN)K���<O�=��8FǱ���q,�0:��2FǱЊ�q,T0:��*FǱІ�q,�0:����e�PT�$

�Ţ��e�h�tY,�]���Ţ�gW�N��bӨ��ś��t���s}@�c�FǱh��q,Z��8�1:�E����Xct�����X([CuKba�pY,T.����e�0P�,/��}�����R�<�Z&�e�q��l;�>89������X����>x@�8��BǱ��:��}���q,����cq> t�����X8([GuKb�pY,T.����e�pP�,�����s�m�^x�d�m[|�r�v�k�FG�1�FǱ��q,ct�Q1:�Ū/DǱ��q,F��8�0:�����b��e��nq,*��%��*�Ģ(]�J�vI,*��%���>��ek���Q����Z�T�wt�>W��XT2�Nn��ߟM�����a(B�wh/�!:�E{���=��d��o�^Ct|�����۴�-�O�QݒX0(\��b��tY,*�]�
���BƇ�mo�I��,�T6>���q���unw,�G��%�_�?�(cx}�<Z1\^��
���Q����s�a���G5�/��gJ:���Q���d)hݪ�����]C�{��x�_��c^�[�����^��|���@��e�?�5�>~��~�>��賗k����z����S����f���#�u��GFs��GF���̀�xdct<2�bt<2L0:yL1:�0:ʖ�CuKF���e#�@岑�A鲑�A����A񲑱��[��Vi���+c-��(�e�\3b�'#c]3!<�	���X�LOFƺfBx22�w�������u���dd��%�'#c]-!<Z��G�KF���e#�A�ґ�{��pP�ld8h_62�Zy>��v��E��+ͳ�m�3v�N|�dd��xd����ȸ~:C1:�at<2�at<2F��xd��xd�����eKF��[��9(\��9��e�(]��9��e��(^��9-��ﴗ��V��z,��q���'#C��@x<2�~���!K*�G�,� <����xd�2
��!K(�G�,� <�t��xd��e#C�. x�Ȑ
�����{�Ȑ
ʗ���}�����?���G�      �      x������ � �      �   �  x��WA�$7;��"�@ɒmy��Œ��?!ll	Г �B�eW��H�Rz��)�����7U�d�m���h<Өo=$r:�T��6�mN=��n����Ū����Ӥt��K�K'^$����?�}����������{��]��9iK*U˭�Ƙ/ij����������f=�&7�A��hw�b���������#&��Ev�Ҷm4p�3�r����:��$��s�wb��}���4���hJ���˴�M��Gd��іΨ3/�YG'GR�Dk��p��OC�i���&���!�V���ho��삥�d>E�M㬻ŭ/^OC{Δ���2��i�*�k�t��h��Zg�������$���m�S�4�s���mo�A��/Z^S-Ў�ϵ��n��V���
�&\�X��=��ih�)�#�aU6��5�,��]�	��6�ҹwRB����NXLҼ<���4�:Y怹x$[Gɥ	u�����g������;�-0�ր�a]r��ҧ�������VQ�0Z�*�l[�ż���Ôf�[e�WQ��l���q�̽����mk��;�'L��5�Mޟў��Hz�ha鱚����\���Q�L��G���1��Ε�Bʢl��K�u��K&vv�c	�5�8�OC@��v �"K-q���IT�[�"]���a�T��i���,�ݳ�k��(�S���@0�F^�f�~���� L��Z�!�C廼�����<Ý�S�j�H�����6���O����5a�7;�`w��t;�i�� H���X��h���b|�v�ywcL��@���]�h���4n{��.�9��%Ǖ蝃cC�_p++NhpǍO�eC������4n�PDĄ	G[�[*t{@X-e�3����	��Da.#[CM}!Z�v0�n�q���� +��1w�(g�m�2��h���۟߿}��'r��4      �      x������ � �      �   M   x�3�LL*N�+�2���K��-�I-I�2���/QH,(��LNL�I�2�,����/��2��v.-.�ύ�2�k����� �+      �   Z   x�3��v.-.�ύ�2�t*�ϫJUHLO�2��r�LcN�ʪļ�̼T.N���Ԣ<.S΀�ԌL�΢�d.3NӜ3(?71�+F��� k#�      �   p   x��;B!�z�*X���+��Z�0y�0İ{_{r�t?�f��Ċ.�Lt`M�1�x���5Y��+�T=�l�fʖ&�o��y�3t�锴��\�{���&�c�u{%�      �   y   x�e̽
1 �9y�>�x�;�*��K�F-��H�ҷ������u��x�OJ��C6�|�59�lS�70��Jmd'�2���Ť��~�%��2��=�Rq�D�����r����n	0      �   ,   x��(�*.)��K�(���+Qi9��`FR~~NjbW� Ou=      �   @   x�3�,����/��2�L�HL.�2�L,((ʯ��M,Iͩ�2�LL+I-�2�LJM�/J����� D@      �   W   x�E��	�0 �s2E'���%m"J
1*�^o�>���V�P;�]`
��j��"�}�\*�>�Y��;n����/���'D� f�#�      �      x�3���ON���/.������� A�h      �      x������ � �      �     x���]n� ǟ�)|��Y��w�m4��JLF��H�fK�I������{�/%K��U���0S+>�z��y2� �'3��(:	�ɳ���%%¤�/}���*'N3�>��;W�[R����C�8���?>8yko��p"�a���uBL#�SG����8I�^�s�ΕhV�L��Y ��i���/��uЬaF���K�X�����<Xz��ڂh����(��x�W��:%Ҧ�A�I+����Wf�Ӆ:{��W?�_B�檉�մΏ;��#�pX�=)�>zE��      �      x�m�K��Hn�ǪUp�s*]"������أv(��XI�*>R�5�6���w���DHyۓ{E"^���w�6wݮ����eh�H���Ǐqz����/ӲB�v�ҵ7��_�u[�������z�����?���V?5��p� y���u��W�z��J�ߵgr
ݜ��#_���_���۵���K�}w��j����r���z׎��$�6&g:�O�iX�)�B��&���c�E��.�H�an�<���-W���_�uh�,zVT����k�����#{[�S�3^��EP}�&z���.k�u9�j��T���н��v>�}�������<����VT�qs{�\�M���|ݙ?~����X�X�g�H��G3�W�>K�Όvbc�.*읲M�.��"?���Ӄe�'y/}X�N�`r?^�V�8�V��ڷU!R֍'j�l'��u�aо���nh��5u��k�N�<-V���t��sp�[��+5��J����B�청:�jg49^���l���g�B]S8���ް�>�����=�8��Q�rt5pf��IO69-��MAq��V��NFc��U8ÁC�RQ����Li*�S/f�4C�a����9�|N�?�p@��N���'U�e4l��5_��C��Ӆ͑�6�⸧��OG�U�L���,�}��%���GKj�P���<L7=|P�g�@��}���u�0	;��@�2]�#F� �,b������rjZRP���ޙ�]����ܡ���	)r�1v�S86���*�)ٵkObi��哚�3���^��m��yN/�]���v�4l"$�~���#,��#~Q�C4��Y��P�Q�G�ܷ&�B �`�̝����b�k�!��A=@H�ro�^��L}�v�7����hdxW��4�;k�c;�ܨl�<GT�>����/�ݺ�D��w{��Ow�i���G�e�>Q	z��?��+1T��o�7��?�w�6����̂�܋���y��mV#�<����)�& ,���p%}�w��gM�2/k�?4yI����%}B���eP��V퓚��a���W+�D�-_/�c]�P��u��o�>� �ii���ǎ�H�F�Id8��������|J�=�>��~-���G��M�x����H/ViDxZ�	w��N�F���1�<��M��Nm4-D-�Q&�����b4@ď�G;t�ۙ�y�#Px�7��뤽z�P�z��V�~Z$h�����f�t�4A-��O�L�&m_���`h��Y~��Ҕuzh#��,_��\8���#�@1��W��3\d���)���Q�Ҝ��uP �-@��4
��Ԫ��䛍H� Z6��/&Mq���t>K�+ ��t)��1�nL6��@�ȔDP�}c$ݹݦ1��b_G�,i�ؚ�6&2g)���2i�Q�����|/6d�G�g�Ӌ<�V�	�tGKኜ5�|���Y��SS�"gM��"�Lyf�9�0��~�G��x�S����/��7��I�^��6��՞I=b����Y!4�_&���1�u����O��e1�*H����$Ф�$Sf���Y*M�̶4!Qf$?i,�'jH��,\���VC�Ic�31(��W��	C�=C�P��I�MÂ���@�\��M��A󺂠�N�/���6����7Y������힚��ـ�ӳ��M̰��y
Ȝ����(��%�v>���� �ͽv(B��R��Z&1]l����� I�J��"�`-@���/ωnК��qU ��� ��κT����W�4��k���r�W8@	���ȩ�^ 7N�@�]�(q�4� #(f繽铥,( ��D�U��]0�:�	k Շ��
��4m�.Y���=�w��4��[.���f7��"� 1N3���� 6N�aZ8�C�ir\ 'D��������A�q�W-�(@�"��Q�6��d�Y�%@"I�̠S '��W��(@�n�_�I�n��ql?���s{��Y�@1 �3�*k
��K�L9��� m���t�ʹI�c�9�%�blu��!\k�@-]u: �ܟ;Y��]��OZ�;�匐lOý~��Dv`���������@�3�bvj?%}r�����3^>:��q�eN/��C�2����~��o�E�l�����obn^�&)/w��8B���G���ZX�T�o�G���=�TW8��.)J��8���"u.j3)��7.[��u�-������}a��@��^j9�+����c]�m��α6z�n�,FVkT9�T����Ӵ�x� ��4K��T�Cb��!��JFb]'}�O����d��"���bQ|\�����h���;��bȕ��|���J��ڟ����J�I\��R����)�\�������'�� �뷪��բ���(�DN{S�r���gb;e/Kr���[�)4 l���ю԰@���B��Ղhpi&qQt{]`:p��[e��9��e�m��\����Q��+�V�$c��%XF��%�Ȩɞ��TY��}7H/i�6�(�.�xQ�8@��st>w~����iJ��o�\?�� �񾝵��fن֠}�+��YrP�E�-�n���(�v�z��v��e�~��]�K�3��s~�PM6���L<�h�Ͱ�u�l�"i�1�a��~�ʨ���A�=���6�'\(UΞ� ���_��Ȩ��o��K� ���ּ� o���@����(fq1[kb��3B�*&�&�P	���"`	��bJ-�`�C	�A��U^�(pOS�2ߋ(io�;Ce���|��� �����l2�A��	S�a����T�dbP,� �u��yJ�W���ZJ�|���]	BY{� �L��u`��'��4}���t�d����w>$�-��+'2=�	T^e����^	�\��H�e��Y�*��M	��!M�l�JJW�*�Q�@�~��)A�~�j���%Xҏ�(�4ՠU�"=�w��W��>:��b�a��W���y_��
z����Hh+�ˊK���`��%haZ����}�è_;�_��x��A	(�lN��!��٩�Y�(���`�G��ת��hX�U� h�z @��+�E�쨡D��{����{p%"�4��ԓ
�4)�)rѸ^��u	|�HBE����T��IGb��(��)*����l���� �`YR��J��i�q�^zJ�TP%�ɝXjXƣ��S�r�Y�Y����o	DDƏ bh�/� �a���&O<����&���M�J�ȕA
4�(0݅�ЦM�Xz��0��6X�2�A�A�ð�c�Ej(  ���ŝ�	�P�����"��5�J%`�m�4d���sT:�lF �P<�(=T���V�ä煎R&G	��v�¯	�7�ټI�+`A
�V�R:D!h�M�c�6b�YXT�.�g�A6.��e�^�i}��K��Y�郛�q���Y'��������X���<M,��&��2|�*V�7�- U ���ԼU�d�k���-U�@��1߰	Y7R�i|�D��4�|~,�\��bQz�L��4�*�R���*��Y�Q�cU�UN�nU��c'��is���:$+�.�B�8+�T���+�p���2ՂQ�j5X�P�Fu���W}�>4˨\0�tH�W�c�bz_�tq�_R!�c��ό��nݟ��F�
��"���WvTNV@�-�*��b�B�@�[��i�ء�P��H	������6�@/3~: �ٗUL* �L6f�����Q��ca��|)L��^���NUǒdA-mւ����s�!�M��� �����D�
4��}�F�p(vn`�iϧ�7���R^�?�1���۴�+o�&^U7*ǫp|Kn�7v��*��e��M0i��H1{[xV'n��j�g7��T[ܧ��C1XnY���^��qLuuT�c�fo��AM���S�S�RN)�A��F��`p�/�lV�߄�.V��;ƦҧS����T@��,� D�n}�9V #ldl�Ҁ
`d�0H�_�w9��%��S�A�{��*0Q������N s
  ﭥ@�_,ރ��Vg1(HQ�T�7�@��I�k<���̳���ޙbD��e4ĸS�+1u�g�ӌlQ���)]Ci��O=n\�{�(Y�V���:ߋI�,�Яs����^��弴��X$� Nڡ����1.Lk�N�ْV��V��
�Rpw�x���l���qϮӠ �b�/2�/���K�[fw�����n����~�o�v11�A��]�����
�sUT�<��(ssJTx1M�����IQ�P3M�j�ITKk �����r�\�IhS����÷T�ϭ@�� �G�h�;�R\���]��(z����d( I?��Q��fB5�ŚT�o����4�A���4h_<P�G ����螄����;��t�Z�+��1WѥUs��}�`��[M�2�MW%�;	4�&���i	���b����' ��[��� G~�&�$V�fުԧ�ݳ��;���B���/��X�X��`��ֿ���k6:����k�����Kz�u�����G��Zw���o��dc�K���B['��	sǷ�D�N�ҫ�t�UO�>�����`��C+�5�wHj� ޤ��HRݯ��~��"Ub��1	j| pAMB�v� �wK'k@Aqs�tU� �Ջ�ց�J�|�֗8>I^��|�_�Y6�ɖG� �£�Q,��FK<P�^o�b�a� bg��դ��fߨ�Y��jH�h��,�o��Fm�c6�8}�����&�M.^k�8����u�B����M�|3�n�5�����h�M>[�͇U����x���7Kb�Fnc���`j�2��p���&n ��=����Kh,:o�K��=MH��q�=�i��^������u�,�=��� Y�.>�;��q��� &�g ��)9m���xՔ�KTy=�h����/�4?/)4`��Q�J���,G�ƚ2�noJ����ږ��bc Y:Kr`cAf�Q.g6�lH�,4 ����_���k����e~�>���.B�4D�[��!��!���K4 ����j��yK��8�����ܧ��*45ݒ�.=�yV"f�r�S��ez)����-Y�Z]�5Kh�Zl�'�Z���'��N�z8iW5tp��æ��-� $~䐴:��6@��7���E�� �G��\�ر||�~��ۙ'�\��H��x�R�	p��9g��%F�X�ᮧ%��62���(h����]%�� ��)�O\�4�̂U[���&1�у�iL *b�6P���n�����j�d��s��5��&}�R9�� �,W�H��/�$YP�/�A�FGdIa h�E�G�^��i㓯��M|N~�Æ��DW��o����$=����^���R��~_i�w#k�!IY{�x�,L�8������=8���NŦ� ��i΃<b�[��I�Pz].xpzڹ��Q��l����m�4��=��~F����s��S���\���[����<V��/���{�&3�OS\���6=9Q&��,QD��^�^�z��I ���N��go�^��<��[�s�
��7��Z��rhPCj�J�aq���UfTw��k3���b�ИͰ�Wû(A� ��{��0ф���R��LHj�Q�'�hW�'Ky=��;���Ru�HZg%�'��I��{9|�d�������'���@$�V	��DZu�䉢ue��?� �6�
�Ҙb)�'�Đ�Gm�pCCw<I���D��,Ƭh�_�ϡN^=���U��ϔ<Q�5���b�V�4SA'���F���[�J<�38{�)���c��JVd{m�w��U*���@ѝ��t��A1���=&�*p��=��%5�V[e{�k���F� �hZ�,�Rio�P�|۔� :A$G)�<HU��̺@���9�j�y��gG�Z��.B�V�3�����o�E�R�=L!�>����B<P�ك�#Z�{�՝��v���V
� ֪�ʚx�jջ�����y0Jtۃ
`�:}�_� >�o�> �@¯)��'��K �$~tw��j�'�1-{C*��ؚd�>��K�(�>fLo ���̲{y4�*|��
�	9]�P�s=�t��=ސW;��(
�x�)^9��-�ܫ�����.O(�`_����ڸ��,첍̿b�
��
��{v�(UN4UTf�m�P�f�1@�U���L����O�����|�Z�K�1#����KH�l��ᤸ�賶��3Ŀ�`���f�kO����4 G���sh�͎ �|�*$ 7��6���>�k�� �P]�@���Y�b0���6{�գx�|��Њ\�,��y`ň��~�'�zر� �ĉ�PL�o r�`0�;j��|�q����� �@�\,T�h��D7T^�?֫��l-j:���B���E��7�d`6m����l�Iڵ�L�8���^VQ�n�`�WC�����P�����٫E��wc]U3�ڻ ��a�,y��^j< %~@y\5; Jr$�<H�"kh0�b�� dAڊ��S\!��zl
p���y$ȡJ<`�C/�����ҏ�Faɬ9���e9�K��A1����տ�9�&�J�6�-슟�����zv�8      �   ~   x�Mλ
1��zλ({WKe��&���������L��p��te��ջ��I�[5hK��Jv�K�J�Ѕ�I������ܱ����/�e�h�
f�QWtx?�y.�AE��������O67      �   �   x�e�91Ek�.'b� c���&��8��Y$4�_��K����3���M�e����$I�������U�'v���U��|���kO>XW��=��>�Y�#%�B�5	��4�H��ytZ �g
>      �   :   x�3��v.-.�ύ�2�,��KW(�L)��2�LM,ʩ,��O��s�$��"	��qqq ���      �   ^   x�=˹�0 �ڞ� �(,0)qP�0?����Z��j�ӊ$a�%�Iwd?rI��źS5�p���(8@Vۊ���p�'X�Hb��� �
�"}      �      x�m�K��Jn�ǥUp�s*}E#H��=iϺ{��(����#u������x%� RV{R (�x�_��-�t�۷���������Q�w��S�ݟ.�|�|˿�ݿMs��4���0��^ֹ�^���6���®}{�>!U*e}��Vð��n\�4�����=�nΎ݁���]�/CG�ص�������;HQ�k��q�����ߵ��2ɣ���k�4T�0>E[mh�.��w�\R��\��mn�<���-���_�eh�,zV��2-�|[��@1����{�����;�b��Z�y��yш��-����Q��n��C=�����mj�#��
��A��9�ñ=\��jI>�G~�y�l
G�������#ÿ��R�]֏�ӣ��+�������h'6fYD��S:���n,�:�H9=X�~��҇u�D�M��3U~�U������Eʺ�H��D��.{�o=>�������������h:��9���{�Í��c~�4]�U��۪C,fA���jsf���<��pTc�ް����mc{�q�Q�rt83pȤ'�=-��UNq|�VL�NFc��U*8ÁC�TQ���l�T����	�H����0���cr�{*�I��);`�Ij۪ѡû4GMf4G�s���~,2rj:����������`-7֝�f-����L?��0I�h��=�s�6t���^"�O&aǑ��)���.�#N�,b4����u{j�A��)�R�|_��=����ܡ.���(r�>v�S�5�ǳ*ޔ�ҵG���wUIM����b�午m��yN/�]���vx4j""�m�`�$GT~ۆ��]e�w����fݙ#(C�Ems����1� �H<��-��6�񡽾���Pn��"
їKw|�[��袑�I\EXVӤ��P�v9�l�<GP�>��k�o��Э�Ј.�n�nG�n��R9G���}�6�rG�!>NWb�̝�o�7��?�w�)lt��џE�lD�N�<��6+������M6�`Y��W�'ygI�,�ҋ�n��K���N.�:��-kU�ne��������?[$zmy�?�;�u��/�4���؂�禥9�#�:�#Q�(��p���;��9��8�9��9z4}t3P�T�@?�8E�m��+��å�Gz�Js �ӢyH��Љӈ�P�8F��6��ݩ���Q�l�	Cd�!�ǲ���]�r�l^%�ȁ �����:i���*򊞢�U��	���cpT�Y ">U����3����M� u�2j��!:d�a^;S��nIZi�O�L,�U�<��]��؀.2\����w}#P`Z��Xp���m	� ʶ�Q�V� _mD�b�x�0i���Ic�p��T@ʘÊ
7&�x�JDJ"��1����\�Ә�z���4KV-����<�$ts\&M=�uԲ�}3[#6���g�Ӌ|�� f���9k��d�]��6�&�EΚ�o2$��4�@��<�^�A	���T��[o���3��>#����1m����P�)�/�m�m~D��g�I��� D�x��Em��C� "��,fViB�����4�+���S� U K�ɔYeA���}�X O��@����11x5����AA�<ۤ�H�3��?�c�4,H�����\�l:%�����v<~�7�ae��Œ��QW48��R�5�w|ԓ��	vA�@1O�#�Q"������o�q0��es�� �P�>U�Q�$��-��>	�
��t(b����Ⱥ�xLDp���� ����8v� `Жw����x2���i����]����/6p�����co� n>�?����P�YpFP�Ns{�'�,( ��e�{��r̠��C��A�.��8N�KcCE���;`q���-WM�f�؀U�P��m���d��aZ8�C�ir\ GD����?�'-�
�㈯ZFQ��D~ͣ
�m~ɪ�DK�D�v�A� Nh�O/��Q�&�v�̒?�I����hclSN�A�gx�\ PN����)@�S;,]�s0��>Jkh�5u'�rjA���q|��[ sB�Z:PCK��9��AV>�@�SƓ��l9!$��p���8�X"0�����8儡��I�p"6<uP�:�当���^��"*<�)����x�0q���vp�bfk�����&��lF0Iy�E�Ȁ��꯿<������-q�� ��G���l�K
g�'\Q>[�΅7��({᪵?_V��	��~�.L�l���Zj9�+j�'���u��93�:��l蝛5�Y�MP�\�r�X0t��5�k��Y⧥������V0�:�{���1�0�O��T��֤��4�MFqz�{���bȕ��|���J��ڟ����J�I\��R����1n\�������'����o5�V�b�J��9mi8`L-�-����=-��vbn1��� ���q�h�jX tha��Ղhpi&qQt{^`:p��;e��9��i�m��\����Q��+�V�$c��%XF��%�Ȩɖ��TY^O}7H/i�6�(���xV�8@��st>6~���橜��7p���i��x��Z�Y��5h��Z�,��Ѣ;�z��F_�\;o��o;p���\���v������aT�M�8l�4��(Z�@3�<��,{�H�u�h����2*1�cPe�(=�]Z�'\�T�� ���-F��Nn ����ݗ.M��z2<�̻�~.����୒��(fq1[kb��3B�*�BMV�x=E����/�`�C	�A��U��Q���Z�Q��Lw��|/6۰������th6� ��)�0D��|*A21(�J�2��*J�g���ZJ�|���]	BY{� �L�����cO��iz7na���vQ	4]&�|H�[K�Ndz��,*��b�C��J��1O,���&��U(�͛hC��%�r���U���&�qS�"���<�#.�K��1Qti�A�Ez��tï9D�w2Ei9�8�J�B��{�%�!��G??�|FB[	nXV\?�7��.AӲ�=�3���z�K�X19(��Ip�'r�O��3%p�n�x��Rt�zjn ��E[�&� �m��_��NJ�~1��i�����W"�A#\��O=��K�q�"��%�\� ���$T�2��
��8�B�e_:E������$,K���V	<��.�K`a@Oi?�
�d<���e<�(����x��:�0-�|K "�0~4� C{{�@�`�Hj򤦣�j���tq��h re�)�Cwf/�iӧ�^�&=���jPaP�0���c��H �߰��:j��> ��_D���T�,�-��,p1p�Jg���!FM�����l�̓�t�� !{ޤ�Bx8���qAy6oRi<H��� J'������~HV/V�{���$	���]ag�`Ǘ"L��\�5k3�s�1�y�=��d����5	��>ژ���Դ܃+�笢�]�ma�,�?r��ɒV���%A���X	Ϙe�4����i��������{���"��=h���E2='ﾰJYt��U*��F���|TN{m�����&����`u�C�`��1�B�r�㶧w�N��2;S-yW��2���]P}��5��2�t���p����{Gט�y�
�;u}�Ժv�ю:�=p������t� �-�<XE1my��/r�2vh>Tr<.R��?��$�ˌ_�Xf�e�c������0�:j@�.��틧���d�2v|K���Y
^�c��p�:�r�-���ᕫ@��84�a�u�F��(vn@�i�����w)/����~^^�U�5`y�6��|�*�p %�[r��b�K���H(r��K�3 y �N1<8I1{YxB'n�j�Vg7��P[ܝ�@�,���⨷abG]y@q������Vjz��Г8N�J�,Y�p�k4:S��i>K���	�\�8�7�Me�S��/�x�PM��z��[;�h��d��=��`����79��������-�v�T�1� ������[kɀ��� n
  {����,)�*��jU�N�x8�7S��<K���)��/�!Ɲ�wb��Ч9�6j��)]Ci؇���Þ.
�_���.�ZL�]�6~��Տ4� w�S� �ݰ4�A��/���1.Gh'�kI&��(���I���!�e	��m� �w���3~��a~��^��f7��#���H8 [��z�� Z�t8�TO{i����P�A/Z�*�" �2��D��t����M� BQ�4a���� �����2� .�$���%���-�J�� 	��p�塿q(�eP �nl���a��ew�� $�b�G-���m�	�5/�t�^��5 �M��� ���F�ס������L:J��*i�y���>[���a�k}��2�B��h���D��p�S`U���uc����[�a7�r(��f̼I�O�ݣ��;���B���O��X�X��`�Q�wֿ����5:����k��m� L�%=Ϻ7�����pG��Zw���o��dc�K/�
��O߉�`���m2�������
5�rE{��S]\z��"� 魕=� (�}� ��S ��S ������7�+Re !���Pcm�� Ԅ 4kWbz�t2 
b�[�����]=��lh�D���|)����d�'��#��<��1٢(��/<`�� a��(f�W A������	�3协�j��v����hV�R-�}e��n�.�ڌ���8}��W��&�U.^k�8+���?D�OF�нm<f��`��/����*V�U^�e���ͻU=���x���7K*b�Fn^��[�`j�2��p���� ��=���Kh,:o�s��MH��]���e��^�*��u�=�
 Y�[f�@�`]�f�#P�բB�@�eL�$"��K��ES�
,Q���V�C�,����P�-G�*�@�e9h6V�F�!�*��v�i[�'�� d�,ɩ���G��Y��!��xP� ��#2|��Z�>D�+���[�"6��
�9n$W���k���2�R.QO+�#�iw���IA+TN�=��gv�Py�%�]4z��,�E�R�B�����B�����Z�������%T�[�ɸV��{R�褿���vU���nV��a�� ��	�C��xn[bx\[?ECx,�rs�`���9p/PT�1�/'��r	2"q��5J�&��2<��"�}+`����T��2��RҢ�O�RT��BC�o ����'.l*@f���>�,s~�R=�	@E_
����27xY^�,�~n���ФO:�㚺Ph�r%K��[�2�A���D�tD���X$� ���E���;�uzN~���F��]�r��U����$=\�Y�e]��]��{�_��ن$e���k�ga�šŭ˴\�CK�䬁��t5�#rڄ�A��]��I�P5��\��!�i����g[�B٦�7%��3� t�D�:�?�YqY���k�.F���R+���OȨ��a��T<Lq�Zt������2ɴe�"Zd��db�Ä��ͮ�'o��e9�ϋ��7$�۫�� ��R���@��+̆�yj[�̨�ߗf�M�E�D�y��k��*J��U�T�=��0���K�5Ʉ�6 ׄ�*�d)oM:��47�C]M$���&��I�j�t�T�Iv�&���j�	�D2i��[�H���j�h�F�0��0ȣ�g��xS,Ū	#1��F8�_���O��p�".0%fEk�l}u���L5Qu��$�&�>��<��Y�K3t�U�Qנuk0@��o�`�j����Z?���ڸ�PW�@p����s�8f�A1���k0LTU��	��F���Zm�]\�,�5�v 0E�b'�5 ���k J�/�5H�N)��Z��*�rf] fG�|�Z{���QD��JWB�V�#���)3��)�j�J��)x:��gJqߗL��������=����g;DHՠV
� ֪�ʚ��ժ7Nk��b��W�Q�ߣ��ED�5��P�Q	$��r���Dbpi� ��M���y���1-{A*�]l>پ��ԉ%iԀ>f|��V�2���Q�J��:c��.c(Ź��t��=�&w;��(
�x�)^nr��[h��f�WI}��49�o맽�~m��av�F�_1�j
���tU�욢P9��T��`� MQ�A�x�r��h�D�vr���X���:B &]�l�YHd_�HBRe�.�$�m�G��]��1�����!	A*�u��]<Ej���x��pTyj>0�6��h��Y�4��G�*7������5 F��t@���Y�b`���m�b�G���觡�Y6'��#V^�'������5@�M�ġ�2��Q�E��Q�u�s׍��A�J�l�薋5�-���6>���z�>���7b��cz����1^4��q#Lfˣm��]_
�]&i��L�8ӄ��^VQM�f�K�M�`YS*����M�բk�&��VV�U1'���� L�v,߀%w��K����(��f���� I Һ������4�� mE��1��"�A=6�AU�&�P%�7`�]������ү�F0y�W׻�� ˹�d�;(�����l�O1A��IGFc�&���H��nôB�X�w���n����      �   &   x�3�,(J-N�+�2�LL3�9K�����b���� �`
      �   �   x�U�I�0E��)rS�ҍIMj)u*�)���b��=��qB*��O�wGx6�&R6L������t�3̭r�X�i�I��d1���;d�ܭ��и��+ԉ��{7��2z.�UWwx�2F�����b~g;w�a _�8d      �   �   x�EOK� ]wN�h�W���F �P�p{i*�n�7o�'9�e����@X�0U܃�d�*� $*=�Į�	^iI���K���9��ȕ@���4��׿����t-���(̾�:˸�Z$>��B�y�b��rdQ��k�/�l�G��1��>l3<� ���Zn      I   �  x�}����0E㞯Phl!ބ��ٛ9�D#5�Z�D�QS��fG��*"�=��m	Z�0H�D���"Pxy^��:��nW��'/�6Z$F��O�s*R�z�Z+�"�E��Ph%8��Ĝ�Dx�l*W�����A� R�{sM5�0ӈd�J�zB��؆�r>;��]*���T��0!�%�M��9��<�����@a�!��,����û��Ē���aq��ޔ��"����ԫ�̌���,�x4
X��چT��k&�M�eŊ�4�(�";׼�������U��/��rfHFf>b9^	th&����_�߯�u�#���R���/�C����EW�V�LL�8B�~��r[q�-��J+���{�F=�[���[d�m�F�I;MR�p�w��j��aSĺm�G���H��#h�A]���\����g������t:�� ,�w�      �   ^   x�340��+(J-(HM�240����9}S�K��N��0˔3 ��$31'�R!*f4�6���rR�\ΐ�|���r��̼�b�=... @^"�      �   r   x�3�NM.����2�t�H,J�O��2�t�/J�2��v.-.�ύ�2�����/��2����2���/��2ҕřɉ9
��E�y%\��ɉy\���E%�@�b�1z\\\ �o"%      �   ;   x�3�JML�2�-HI,I�2�tI�I2�9��RA"f�~�
�E���ř�y\1z\\\ �"�      �      x�ͽ�n#M�.��?W���-�)q��I*I*N�Л"�D2XA23�/ү�gыzyv���M>F(���H��nf�yD�hnn>]v*�i�Ү|M����z��o+�=�:�����`֔�}�_yYVZ����9^'q��b�j-&�Z�L��|���H������o�3EF�F�~����u��%���.��R��s�yW�ݣ��)OtQ���R�v��2�� ?�Ň�y^Ii�O���=�*�*��t�����t�e�\�cr���"�y�z�A����49�[����0�z���V�ŢU&�>]�q5��N��z���~�vo�_'ɷp�c�pZdg�n�o��k�_��e誹zxZV����3�����r`;dP����r��_���kv����@N�����^2"R�]/I���g�$���$�<�<����y��=>��j/��~W�P�}r���Ay��:��N���Gbq�[��g�x����m�>5�4���S����x��/ST{ԝ�׏�bT����V��^���Ku�����.�̏�X,�l#�A=ܷ_��U�����W�I~fȀB�Lu �lҨ4'�����K���O��� z{O��Y�����*��s=T_����Cu��}�~�R�����O�p�X>.W���	�챦�Gz��1|�I��;�vh���=5[�!���������i�[@�J��� ��>���� ��\�{x��}����49��g���������5���V/�N��AQ��u%�I4+uo̝y7˥��_LT���	o�>e�e���[x��>�וf�
��Pg.��Ȥ[��hqU}xW��3�b������{���Z�3h�XL�(�5�ww�ͷ*��On���C���B��V�y����j� �+��n��b���&X?��5$YY�h�5A��9�e�3�`�ޤ�K���/��s�MUd���,�#��Qeա%��~���0��S�� �H������ޠ+�4���ǇG�cz����*η����8�����R3�'�v��n���tΎ_�K�R��qzu:3 �$��o�O�l�T���x
z�]��N��=4�	6[�'�$R$dZ��<�{������A�u����?�_lx�B>��
���,O�:�� 3�p]��/�90����A_�z[�\�<����vb�gr �y5���k�m��ތ��k�c��%I��I��� ���zA�i�g��J�y|�|���/�o���eu�=z�P���n��m���bt�<O�����Q)���f���Ɓ�Sz�������>A����=Vwʰ��Q�G9��GU���s|ʪ�,=�o=d�籇iV���#g�>@#Q�=�4�������&�0�egh��l���d�t�� �}bK�OV�����J]����	�IVa¶�'�k���I,�[@���V��V��4k���ft��Η�b��_6{��̨��K���jA���B����}z�vٻE ���/��x����m5��~#T���8����M��b�&Zyj��S��į���b�*M���p�}>K�5���P�Uu��Q���Z���-q��a�E�����A��%#ᄙT��ڻ�\����J
�T��WQ��R��i5�>0F�/y���^����BIY�#��ќ� zzW�1�^����"YVK�h�}�ìs��#X��A̓�%$.���Ͷ,�=�\7�I�E]�� �hx�����α�̏�WZ4�������@4Ĩ &1,��aĐ5�j�{㖇u��x������� ���Y�<<�a�8-���6�`�G*�C��B�a����0�ZFXմ5�WMI������ �ޱ���EZ�H�#���G��B�9H���p��Y7�t��a����T�K/������1���O���ϟ�{\e*ޝ���"k��YÔ���D�v�0�m�ͱ��1���0�CF�g���+O����'��4��2XT���b�/�*�/>��C�,�ZaiTn�;0�ټ[.�(i�LMֹ^	܌a��u��"��S=N��-�BL��ߡC)����?�MBn`�;A���$ݣ�c��F��u�F�����K�0�^��Pe�	a�Pu[���>�pU�XQ�'�F��t;�ʼIeH�F��*������@�q+3�"�64x�Y!:���g#|���n=<B�l#��6�AmRچmWK�f��^��Ҳ.�S�ޤ#m�lHi/&AmEs���I�QP��I~I�)vX�c\��i�<)���Z�j�0���=�*�w����c������|�����'(���B��z��3�N*E�,��p%�N��?�.�Is���)� ��!�(	ku��)�~���^�O`�'�8t��>��h���Qt�/��A5[��Jґ0�����+t��Mj�O �Lt j�ƕ� �U��)t���׶YX���ru��(LP��lH�ȲW���m��a�zӁZ�aǯ��X�H���lu����'�q�A�:��^[`�&�jdT1.�X_�Gh�;��PM�]��j����m~��-�v�%���Rg\������|].+?WȮ5��UQ��x�$)�yG�@�����o�4��w̌W-/e���w�Qe=!ְ�&\1>,�i�&Ep	�"�h�U�3I2
��{T^�1�(t�K�Ǣa	0�kR��_�Cz�L���QaV����$#Gz]�V�����`Ĥ��1y�M�^��%�V��n9�,���>S�ۡK���ڼb�}rv~M{�^�ָ�g������y������E2=A9�|�e zT}N��6=����P՚�_�[A��&���I� (�j�z�s*��L*�ܡ��tm �8e~�%.����1�\بW�h��u�;_�w��4��p��d:�t�ə]�Nv<�oCo{WX�j�Z���{?Ԍ��BW��K���f����x���k�׋_��0T���4�<ͳ�.�7�fUXF�ؒN��	�_�ޛ'��ZnX��r8���:��G����
x�I54);�-L!��^���S|C��$�4 ���E4U{�w���hKo���q�)�h��l�x:��Ve�����*]H��$��V/��Go�q_o�w09�������|~�ά�c��	��7j<E~1~&c���x֌$��%�eF�Ǜo&-��)��1?;Y|��⟔�N)5�m�p���L��}�|�^ebD����:$��P���P��<���\f��0��8��Û��S��H��W�	��&�<iJ3EF�1�m��^��e�A�ƭa�;���۔�����W���k�utݸ{aE�5��(��|q�]���p�{�gg�&i��7 �r]��}of��u�d� ��
���6ד7K���wG��)>�S���k�+P}��EB�Y49n��<��\4���$YY��/8	����y����v��}BC�E�Z ��C�+7���{υ�j�!��,�PSX���B{L]���,�W����~؁6�ԟ��)L�Ԥ�\q*���$}�=)�2vp-�;��E��_�{~�9�����Xo�S6ɺ$#����r22(h�ȡ3��E�o]4C����b���[�ѻ�����V���]�&��`ͻ���D��5�x��Gv(4�EUͨ5���>�?���?��,�ف*JXM�T�i���h
J���k�$�n��Ϡ1T��hY;�3�)��0�j<��sNF�Hu�;��[q���������cY6���X-6Lk�c鯻q~BF��\&�Q��]�S�ᮒ��l�ٺQ���R�g����(�u�S��NE"�u=���`k8�8 -�>��^s���$�������h��Om����I��[�=æ���dDF������wɘ�;̋
̢S�
_`����|�r��i�U㩻z,j9Ǘ�;�֦ziHt �->P����O�"��I (���2���_����$���V�#�T���q~L>�8-9ߎ�dp5A�o��V?>C�2�ȷ8>fd���Œ�Va��mP�J:r�9�pi�	j��G'T�q�Tm��� Mh�X�M���glُc�p��vIC�rj�    ��td�M���� �4x-��������N�p���/_8#�j|A�9���|�[���ח$O,������M-�����M�m���hC���`,���C�A ����1E����(�G��@5&��neQ��q�,���8��n�{м7���Z���o�0�I��>�&�V�9<[U���y֑)��I�'6|j�E���D���}1�`���S������N����B^qK�Z,s�2�?J�3XΜ���F�%���D�RP�ŐS�'�O6���A���qage�����������<EG����i�-%���q��N�fq�ۅ�aFz��L,>S�kc�*�[U���lh�����2���Z��"_������l��r1\�$�#f4���\�>�q־�����4��%ڬ���ˤ�BT=*W}Jϼ޻	��/�oښ].!ߣ\�(D��r՗��_i6%������$����v�_K��%�\�7Ҧ ��t�pT9Y�ԝHR�>���T�Y������]�'??��9i�I«V�F�S�'��*��l�Jiz�3 �E���ݎ<�(05���=��D��~��P�s��d$��7��ò�e��X9���B�38�@L��ѲP��^d9�]ꏽ)����Y�!�HsW�︶;::�l���L� s�
L���F�!�ٚ,>�`T6��Z���)g8�Rîj�M�I��bӻ��j%����ȩȕ��3>AW����
5���7hvc�L@�O��:U{�uuYy�E>�Np9�K��z�'NFV�
-����'���T*�pX$}�A7��}<��A�=�a&����Q����=��X �CG5�'#���ʃ#̐���ṱ�K�[�Ʒ��j�
C\����)H���-�S8�KK�x���(ٚ���ڴ7ǵ��}\�c�%;��-ٞOťr�Ҹ7����;�xx���1�~�&�>y�I�Uw���ݫ\�NՃ&$����w.�^ܫh�0�����*�����+�Rs���C*�0�<t�D�s`��$�O�Ķ�U��S��o�~��2|�K?{���:��tIT=��6�S�����(�J?�K��*�m�fJH��Ox�a����7p�f��	;�������MS�e��c[FYXn�/�9������T�P���Qq�d�FJ�CB6 �X`�zͻ����Gѣ�P�μ�(�f0����>��J��`e���|z)����*'+��Q��]#��q6���эKCp�34��Ն�{IGF�N�8a	 \�)��P�[oԊ55q�(�2����hkpſ���/`P�����X��Z��"=-R[F��Y��@�ٺ��C7�郌��^�l٭U�/�f�}���#8��$���O���gDV�
S�X�ھλ���Cq�<2�ES����ƒd��G���	.�p��}�~"I+P��Myq�գ�d*��P�v����?Og~_�����]��J8,�q4jsF�A�G<����"�&��b�T���<+���K6�Y(?�-	�B�Z��3���;��C!W�-�H��'�U�w7����u�a�����m���Q���K��S��b��d�Bl�}���f+.��%:ᾳC�'2��O(g��Zg
Co� �}�ʛxVg<�!�S	�����"�!�JG��C�ih�P�Q��\�U��?|��z_�G8�L8�TsP��(���f��D���[]���a�ZhS�=���fr�	��Y6��aY-���������r=��S¥��v�l���l8��ܶ]���%gD�Z��0�'�s] 9[��>\���eV����Β�Ր��F�5��{���q�H��(�j��^~��u6�7��Q�a�y��Ȱ�~}�=�?�~ȼ�;�R~ߥ֗��T}|�P�a����z�Q��R
�r2��ddث0�������Ι��Pv�p�+|��Ya1�vW����t�7{8��}c0T�a57�|�Ǘ3��&�����hPosFdEЋ4�9u�瀥43�U����<���$�?Y�p�Z���43�^��γ�l��5�����c�p��?&��fU�g��ʠ��rf���}��٢�s�S��W���ꁓ���D*�7�T���"�ͲYLs���Mp{��Am�ߣᄯ��)#r�����w��h}�ή���c&n���%��i������L��ژ?�(�Xm�����^^G�7������xy_�$3Va]yN����g�FOf�/���ʿ��KQH/��z[R�c�E�b2��ƃ)��i+WU���&��_W��98�N�isV-9�ćY�^�1q1$�t�VH5'�(
 ��ѧ��O�=��8pX�ޓ��h�)K?�?�kp��j4W+qa[��$��P��lJFFd�"7�� ׼$#	ߪ7�B�7�˳�IK!�C;ȓmP� R���z�^�)�Ĺc0	j���uNFV >�� 	��B�լ���X�����g6]�z�i�D�A�����o�T���<�j����x,��Xq�=e{Z�� w#gx�T������֍׊sU!�b�sN����%�t#�	�q���51Y<�R/�E�Cv��0FZBg�;q�������k�Q��(�ɹѤ7������w`^�N�J^V��C���Zk(N�y��p
`����e��,�>�!�j�,��p�%?����Qv1-����a�tVJA��r0PCz^�(��-n'����fE��T>UJ�j�
�ć[���'���V�m5`蔴�<��&e��Z�S�fQ��Oj��zQ�O�fǷ$GG�m��K~,G�sT$e������ⓞ����0hBI3�T~Ni|�@;%��!E�n=�h��%�l�X,��)�,�P�]<lj�b��F �f�`�+#�pM���l`�20v\�#�T�o�L�VӦ�8��-,��<�+��ى6P�e{%9�/����{:�ȓc�O5S�Ϙ�ٴ#�(R�Zt�2nn�8O2 ��Z��9L�?�۝>M�&�E5FOC�&�1���'��f8T�i5�pF$��b�N[�Y��|�}X����"�{X�ƴ��M����©�������_bbH�V�\��>aK��!4�/ME�fZ�0��r8E푭TG �W���8����~�$��t9&��
"����˯8JL�,�SƯ��i8��5�r�8{��nGI^��?��74������V呬����L����5�{������8�5`�(*�5r�Ɵ1�-���������c	�]�V�F�+�}z:��?����aUs�P[LB1:y�m/Z$�>�ȣu�3)@�|6���,�������/oK���SN��%G�5j�&|&���Cph����u� "AH5��O����"*�Z����f��ˮ8�X�VOA����m7���<K��>�������|�1���zل�$GXՇ�O�,�K�f2M��W�<�Y�i��$��O`&: ���S2m<>_s���©�[\.�������Đ�љ��!i9�˓��_�p���1���$��g�4Ky�X�� �EQ��xl�*�)�d��(N���i�#�8}u�ٔ�o�Q5���/����k���R-��X	C��, ����O�+E�6g��ķ�$M�ct�ᡟ����S���lP'䑅J���I:�"���=L״U��oz>+���s����]a|9�ƹA09��$��9�ǜ�<���j|����E�j�c��4�Q�ۍ�4y��K79/��4��U�U���d�U/�}zr'��P�9� �F�7�#<�8F�C��'��e8�dd�~b8�Kb�E�ծ�a"�x��������<�y`�;�48v�Q������n��p�f�bh	�o^��{��%�zyڲq�X-6��
j
��SfAP��8t��ɟ�x��]��?ݿǹ�\I��e.՜7NFV �}|���}�L��]��փ�l�:V����]�6�ReyB�Eٕ��$���25��׻�3NFF��8�өփ�څdk�f���δ�yf�R���{�2��O�����#:    2�"��_5K�������$\��h�?p2�����H(T�Λ���h<QDs|:��9E�fo�5�5(�����m��ké��:�1�%H��4KN'���G�Jc�;qK?�zH�s�J������o�-�3����حT��(QG�^���o�[>�,b�kt�\t�N�"/�^���*�
��z�y�����ɽ}���o��0�j�˟g��L�Ls�+���e���N�O��ϟ�T�%�b����-�7�]J�E<�ɮ7&��>��%���!ݧY�u�+����Z����ݽ�?}���W��U����>m��p>$�FESU�ݚ�81���zk4�]â�fmP�����đ�d��X�Y珕��Y~Ќ
f8��dsH������-^$������k(@���ǁ�R��6d4��Sz��  F+/GɈ��}{9��(ז���;:���cյ��̎�<>�E}7E|�E3P����3"a��+�L᪬�

�]0�"��p�硛�i�n��1-ݦ0���	�/~����- B}N��]c�fV�^ԞK:҂�_ɡ�g&'xu[�Нً�8psk4��N<����ı
�7<�d2�����c���
d�U}�x�JN�V��i�C������/���7�)G%�)W�i�.;ą�q���˜�jMzQ]r�~�[=d]�y�����Ο���)�w�)�J[]��0����t��N:���K�3"f��@�*��:�u/�Pm���@�(n{���|�5�R��j>�+,�w�{I�+�k��֟����|�T�誹޵|E�]�����H��������Y�%�b���C6�����5��� �D�K��|N�LJV��ky�WN}����ق;Q-}�D�O!h�;�X�R�Ǹ~|��x�+ا�q�������.租�j���\H�֢:��`"U�\���&J�Ϫ�)���������2r:@�t!�N�N(�PY��+�^9��5�z����JP�R�C����~\�k葮���t����X4n��e�:�)F�u�I�8�1Y<����J����^����mc��C��%��M�{]��&�����dl�n��4`%�u}��z�w�F��$�O=I:��q(:�N	��ǘ럹�'�K��"�p�k��{��I�Z<���J�O8���w4��DZ��mD�OX}��[�êX��-���r9\������WXw\�2{�Χ��Π��p({��x�N���\���p����O	"K����6ЩK��I(\9�jԬQ3�|:S�0��è�kv���}GW.g�&Ο20�Z=���8����?���J��ɑ�.���r�[�j���7��-�=�r���J��~��u���2v����X ?p-�u���-��ҁ��vH�H"�� �Vw|?�a�`BGP$Zy�j��� *�bp�l'���A+G�y �����Z��G��'0Y<�
�9�
2;bp��t�=��Bv��N���5B�Ϣ��F=Tp���Ag�����'��M��v����v�#K0�"�l��-���հEE͠�$0g��svY��;�c+M@Q �)�F9?�BE���C �ߧ][��$�b`�Z5�m����0	ƇĎ��h�l1`(7&>�QR*8����f�����E���|]�I�#���R��Ҳ����ˏd�҆�(�7���	�Ue��7y+aܷƈyz�8��̻ֆ�b����?���<�HîP�;�����E}�=9�q�܁��, �y�X4J�MKN�|�-�g�0rJ���k��W�VI�e��۩U,��e ���&
X殺d�����C~.�od�6���fЇ��6N�c���{/�O���rX$|A��Q�����C<hFe[�B�Xl�D�%�Ut�ڃ���5L��Q#lن)^�е��;T\�Yu���D�㡕�j�ƫ6f�St��s�Q����x��1���9/Ş�q�P9m��d���Ak�����X���Y]�X\R�䫀�H�4�y��� *���jDwjD��IV;�� ����B��<~>A1�JШ d�>99f�i$���7�F:{u�{S�g�T�/���ע���qq���<4N�.U��D����>����{3G�9/!�y���a۔�`�ֳ��T�<��zͷ8�9�{��.���*K����ga��Ré� !�0�qg,����m}�,�����%���Z)��E �� ,�aT�>��?��.�Rj9-2*�}%��M��6�՜=Ni�� �zIIY�.惱s�s22"�t���V\Sx!���?�$�\�VW�����e��	O-;�����k.�+Ҹ�ݢ@�3��߭���g;-��wͻi�����)]����%tP�Rh��dI՜��d[����4�l����x�=pFd�J�7��lk�(P�ɸ2춥�	qW���:���_5����R�!1.�[����fh��]@�|��G*�~h6�	Uu���}�#O���w'-c���q'5xPiʡ����[��~�r�t��4`(~�W�~��I�	A�f[�(�,<A��/�DSb�i��#��B�ְ��]�[�O���r/��E�ע�fmJ�۷'3�Y''$��Lð��>^aO9�s���,��|�^m�����q	�g���@Q���x������P�R.,�hU��$���>�!���߮�KR��w9��F5[���JXz�d?�� ɮC�	�aq��Y��0 �Es�����2������{&��s޴C��]�ϥ��es�A�4�j>���{�ى}��o�3��O�sΈ� �f�4�eo����Xy�jG�v�b^r�Pץ�ȋ0b�8�5^$7)K@uFI�މ��dW%���l�q'�� t��Wh��/ϓ6gD�X��ӗy�9V�:d��T���8�ʔ�LT\��Lܖ�b wv�3�S��o�}��쑓�8�h�z�u���h9����5�t;�[�w�\� 2��R�ڳ=*:��ޗ�u�_�&�3^����3"W�	/�إy�1M:x���K8����R6,SFX�b�f��Z�I����,�rY-6����T����X{�i)�� wy-�j���%�,���W�i~����j��<1
8q�A��#1i�?�j���m/�V�v�٭(��χw�:�C��2>G�����+�#m&V���j�=��Z�Q$�$P4�����4B���|
��9��5�&����}��U�C򃻃���l1F��8��v��-d���F�'.Qd��^�*�m�G)\��pYD՚N�\'�̞rg�Rb�e��_z�Q��`g0���(�]|
?�y)��Oa8�/w##HHwH�[՘�����O����)�rlVŁE�3����(�J�J�8�J�Wq�)�B�$�B��\��O|{JX?u�:0&ª�K�5�5u1P�&�D	�x���`NhO�� ����r^�h��o]�-��Q��y�2|���'b�����`$Z<78�Y��KK)��6�t��ز�]x9���V���l�)�#�d�+�ًv��և�j�<mNE��)]���p�ixT}^o��N+~�s��N�[(�|^��J�K�.��,3u62(���yL���P�}6��`��+�e��(*���9,�%ES��"+�;�*Ҫ��"���G��|�4y=�]�x,�p9�7l�6�b���m8C�pYD_�Bp�;>a�˘7������8dOX�,"�>��@j/d(�ʖqX$}���o�~����3�\(�[�x�Vf�;
��a �Q�`[�3�K>=�on��>�hCma'��$ߓ��y��,��2�n!=�%�Fʫ�o��O��|ʦH���h�1ZB����2h�R�%h���z;J�.��9I���o��8��L�__Th�x���5��c�>&��p�b-&����6s�#t#\�?�y>A3(��0`)��v@�5v���[t.o�"��7�OX���/{�	��;Sį�0[2|��f�r�0��د�A�"[���C8tƅ?-��    ����<`�I6g]F��*��R>����^����D����y�����K�#yA]=�������zL/9� |-,� �)>ݸ0��	,��V���V���<�|��^�k:���Ha��.�\�8�+;/
����� �ů�eqĝ����nu$����V�Z,��C�g�j�G�<;�k)x)�������S�f��w�:H���]�C
D^	��A�f��X�tY�۔&�7�0X�l�m�'ʳ���T�6��d�on���׈jU_<�;�#fg���l��#z�'c��g��(��<߶L��l=�$'��cS����[B��q8P�*�rd�x�l������ ��y�M�!��鷱�.h��Q�3"�[�˱1�+zD�����`�ǢA��v:>�,�q���㿶�?��xv���n	�EŒw��7X���h��Ϣ �}��#���n�y2�f���O>P��.l{����߀ .?H��X&4�yP?��չS��u�(+I�� r�ʩ�I�1�ad�U�o��2�~�Ăh[�h�$��̇�k�.~I8˖D��ۘ���T�>��lHC�-=أw #�X�1��� �T�pa�����m��V�h�����`sqP=2���b����/�>�9��;��=-�%��}���tZ�� ��ZR��V����,�{�^W�%�
e�x���y=����_�c`����-�&<�N�7"��>�ф�����QδoCˁ�՝�uZ�?�-���#�N�-2�W�y\�����7�s$����U���J�&2�8IV%�
�/NF�����'��-�t�◲U�gB<�L��šv��"��5�V~ꁖYT�q1�K:���_�L�9Q�&���Z�YP(瘖��oT�U�OQ��t�bg�~ ���C�r��S�n�����>@?�7{t 4��B��5��?`��K^���B.ը�8#2|!ot9�c��y���c����?��B���_�4:K� 0��@�F�gΈqZ��v��:J�[�eО#�d���zo�c���b9�]D2��S��	�hZƸ�\�3S�%9̬Z�hE��£�e�2����Dn�~�ddD�l�Xu$����l1T����8��'w-懡�ֲ9�v�L_�s�4�4���;,&��� ?�ɬ�f�fE�5K�f����H?!d�Lqz��`,�@t�·�[v�
����F-���VM_��ą�爬�&Y�f�ㇾ�#W��׀��W�n�)/g�;ǖ��ҵ��iR�j�IM�~H����<�A�Y˼���]bȦ�(a̞xѮ�ӪƁ�=N�?>հx)N��Y���Oy��>��U�Q�n�y���+�vX��s�X�ɖ'�W7�[ �>���N)|q6)<^l>�Z@��S�����m q���>�`M�E�����;�����~>�h�9� ��'�<��Iz�CL�k�w��P�U�`�.���A�3�H1����|&^��_�eW�~&&N��j�jV������)_5']�ժ|ET���LV�f�WyY��c�VBSv��
[$�m��FQͻ�|\6x�8�^��CRf���ӆ$##c/����g�Х&m/�-����ʰ����~�F�I�)��M�X�)_˨Qo���ȑ�����wpI��"��c^�3����˲�K�t��\�Ζ8�%y"�j�x%ĩ���w���ekN5u�����]-�|�b��+��a���B�lpֹ$K��`Q��Ox�(���\���q_�p�6o��my�j<����ӃU���{���(T�2��4�Ňw1�*�8�k�7ƺ�	[߅M=w0�'����6x��f~M��cǨk��q���:��ݽ����||w�N��C�T�aLn�sهiޖ����ZU��:5Έ�:�}f�ͽ<S��bE���"/�6��Q��YBW'd�土LV��7�E�9�&��}�տ��0���}&�Ol8Um0�,9#r��s����6N`��o|D�U6=v�>_cު'�t��-Ԥ�9Y�Um8��$9����e$�,]޳,�	ȝ��A������n��eɑ���HC1OmlTz�n=�bх��6�$:[��F.Q}��\��|�������fUX�+���^�w��Ha�;1�C����쀨��lhomI`�N��G���`�T��pFd�{�����.���[r�}L/.��&Ü�j�1�r�[���:@��=�vSHeAR��}4����ătk��dw�
<�	��9��5�"��Cgo�(>�����!����M����;{N���ۡ�����&&8i�K��5�,ɍ\#�!�y�'{��껆ca�@#�v[���O�K���t�]��t�a~�R ���� �W�P��Љg@Sx�/T�@`���urx�y(8�o)g�����~�B�m�����L�H�q�����X'��0# ����"׻���q�.�MJ8-2���Ժ��0���%d���`�y'�K<���8�S�n�2)6U�W3IGV�z��׵>���F��:�������L�Q�v�Rv��-�k%���M�x��G(��ෲV;$j͂��$��vbs�u@��ND+��O�Y���6u��*�dc���0�\6*8q��1�T��t�
E!�� �$?βV�Rh��s�FBЇ���:���wo����wɰtB�<��.[�ɱ/s���˦n�!�I�$�/O���)��h/O��p	Iƀy|���g���2#C�f�6�I��E|>�o94,�E�C�ˆq
L�V4w$9 ]m����}�>$|������r U�>�6��c��SGgc�d�
��";�*�C�С��O��1P���ǁ#� �|!�ڤn8"��P}�&�I�������]q=t�`���W��5G=���m�ck�4v}�AK�CϷ��we��D�%U�۽[� '���>E��f`PT�鱣������'��C5Z��{NF��{48P�h��K��BDN�-�9��9!�0�F4�4%'bf��ވ�+�_[��oAIA���:�8��ћ7:@�A�n�'�a6.x	��Z�Ѫ(ڤߓ��g ��g���3Q`Ҿv.�LOSPܞ�-]���<�2��� x? �aT�%x3���끋,�(p�PR0@c��K�uP';n�k��&f���W�ɲR[b��%���go�í�0CxU�>YT8r�����t��	4|��������U���l�Kʙb%J쁜�.4r`<���b�j�
�8"���ap�$.2$>���g�x_9��땕S���<�p�u��ի�]'�k4����FϜY1b��0���Fwgwƞ�ʮ�:>��$nRz7Q��v�I�A>��M��_ż���P�gX)Q2�a���4-���/B������~�EkQ���@}~��JSs}}�sEJ�;�/��w��6N��<¯j�֠]i4���8gPV�n���=�v�8}��՗�D�!OQ=.��'��%�o��)����sM�f�G5����?��᜽�b�'}���n��Y�$=��� �f�wW��0��Wز�%;�շq���6<�	�5:j^�:�֪2vh��5%����5Y��j}��e�E#���I�$������*�U�7xU�w�UOt��3�UȘ�ΰ�'s���R����؏DLF	G�jUݻYD��D�.��WԵ8�d�LNV��e��h$Uk�ͥ���`�	�+פ�h>�x�=?Vj��%��]wT�������Ϧ�xRc ��w}�8���F��M��7j<m�6z_v��8���@��:	X������'��k��W*������c1��1yziUw���|w�>}��b�~͓�2!rc�x��a�
8�}I�&U[���!.
*�� ��Y�M.���2��2�99�����B����$g'H��eL�1Z\��4�K0#Rq�����7�͢�@r�C���y��B�'���`\�W��̟�l��|��ƅc����_)6-pKS�D�jR����f�>��F���Oމ2<P�A}��f�V�M���    �z~�a�T�Fwm�2��M��ҿ��K5��gDF I��C��7(Y<������j�U��|?�]��nd��O������<��R�
��?��o9,�ы�����v��+�a�^�nvI�J���t�o�{7+�d�8-�j�V5��7�k��k����]ls�6��T���� �l�w��1�/���,A�%�z��g��0�x��ͼO�6�Z����VB޳��4D�������h,kE�c[s`Tm0�K����?����01��[��F}o�W�)�G��K�d�@g���{�0�M�������\L�*�q/G�����:0��)�o:1]q:I���*(���k��N��V�&�2Y�.�_�h����"bu�ƨ�~�����ɑy�^�f�����m�1JNi��`��ΣBw��0Nq(�y>�=^3<��,_��%��j�v����M�-
n�uf��l�9��y�ϹS�Fl>_��]�ê
�C�]�ྥ@�Q�_�Ӗظ�k����-����B:�
�����vn}/���ZT>b(x��5�@7�5ߜ��������gG�� }�e�x���X����[�>U}���;G,�O��GG^S>�U�Z���`调��Y$�d���I���0��T��=m\P��!�����j�ݏG���3� 0���dW$��{}�g��l�?n��N�1�p*����#�&k^��Of�+b� ;�q�6��fG��Qё�Xd�����G�9��P���cJ��
�k�^���Cs�= ��v��ok�ܟjF	A[��z:"����ˌ����i$��0'o���u�-}�V�R�nT��r�lVf|�����Cu���%��!�o�F�Ѐ�+��	��Â+�;o字7�1��	���j��hի����b�WކK�:�HТ�.?�[�����/��'�y�f����ۜ_*��d�g�L����Y�d�,oey"���kwV���L��8��_��i�e�&��M��99�q��+��21o��c.�N!T���`���rr�HE���M*WN�I�?|��ZKd�6E+X���+So�ʕ��G�m[}3'~؃7L;ܪ֙�f�z��|������B�9�x���Pv�N���"]8�YLX=<�p�Ԣ�]����%�R�^p9��G$+�h�_�]���5^�NXQM�J}L�a��S.8:�$����,zR�lS5G/�A��Xu�[s��9�o��;����KuvM�G��,
8��!�O��Y�ǼX��W����S���l����&�MO.16�Y�����҈fZ�4����'�&}�a���Y,���ꨋ;²0-�U�U�Q���bQit�cr�<�<{�oM���ǰ�}�����U�5,�J��dN����4�ɿ�7�Y�u�nu�.��pu�ⅰ��ôp���p�m��>Ia��~�4�$dNh�*�x�Mֻ��x�I g������c�?݇��J�	xm,!:VA@W�-�J:r.��d��x�A�6[+%l��Eo�lE�wHԬ���:�V�Iޠ���|��5�$�����Y,�v+�ZtZ����kn.h-Y�:�`]j�膟��ڠ�a��/����vm1�m�^��\�	Chٸ�/�Zl��"���]�^�=)��¡�C{��0�ƽ$�:����.[f�y�X=���>������ ���=ʗ��~��|Ь1`9>~����/�ۮ]���k��#��]��:��Y�
>	��8<[,i��9��Y!���R��M�_�K��xe�V�o�`����,Lo��!�+�L%�p��/��ba�ZI4���Kˢ�i"D�`q`�_�θ�ݞR�9�L�����vF�/|V���J1��E�R���'����J	�#«,;�X�S�_Cp����T�����߸�Y�Y�'�"��:F����ZI"���E�/�oP�Lb �`�
N�g
g\�6a�]}{�a7��Q[�t������jY���e��t�RP�]㍄E曀�O�n~8��q�Ϣ��#���f��ù�%͎h�$����Eo��s!���)�*�����>�L����)�Wtԕ`xr13�+j.�.~W�Ƞ��yVi���T��|q_vt�ބ�*�e��c�a#��^���o�_��"� X�G{��K'<�*�*�8!��Q-���q��4����O��;�R:?<�X4��2��&Q
��M��xO�ۘ�T���[.����/6k���l�T��$?�;�'F��QG�vcd8����H���]gB�$Ȟa"Nl�Ҷ��/��}x��vV�s�u$,y�G��Γw
�Z�h�$u"�n2�ó!w�~��4�N��;-���S rw|�����`"� �V���Uڍ1�ӭbXr�v�1�g�s�a�o��*�&S����v�U*c��Fg�����\��9^�_�A����8(��6�0ūG}�ן��|K,r�,���ٷ3W��q,(� �7�����oE�=�a�LHl�3�R��q"�W������^�����-@,�����@Oa��/�&ڏ-�z���z���Lӿ�g��/�7եʤ�8(���a�x}I�1��(�|�a2�����I�at:��'����xݢP����������ߪ&L�ט}~���N�Q��2	5gG�3�'�w�)F��A:wc���>���0K/P�}+�/�w�@Sx� T��`�,œ�%�C�e�b�(0J-�=���5��8�6���h�]&��{��j�:�c�A�Ǣ����k��K���n���oj��Z��Rڏ�f�?z���'�ߔ��d��_�wJ��P��<5�P��CWݧI�4�e9/F��u�i���0�)��Yf[�ȨN��8�n�Y���}�C��]�������j�[�0ktU:�f�1e�U����x��b¬�F�d�~ͫ/����<p�����'g	�U�Jb���Ѱ&P	K`������r������[��F��i��t��SXd�:a@�m���2������=���C`�9&: �^�9�>0�����iD�2x���Zcz_�����!��c��]�RV;z�V���ڰ�bH�o���7"��L�8+{�/����G��.u�˟����1[|��/�k�,��→�`}�/�IV�H��>i�+��F��uj��I�xNU�E�'#+����Kv�*�^��[���}���F�V����]�A� l�<�ћ �k%Tm�|ZpF$�g��3���h
���>��V�EkP�.��Оx������7L�'��IQ1N�'�0^�=D'��A�r����78#� �W@�>�C�ƪz\v�C5�Q_�L����&6E�oޙU�0
&#����8�>!eD�bq`VЫMLg��0�w�o��1l���<�HNdD�;���ފs��/��
�(������xx%�x0;Pt���� �᜴�ҰcP���������8L�$?�X�b��Wi���*M���Y�o�-ݒ�]����r�e<�\N��`J=�ɣyz���f�(βа����$Q���x��g�,�p�|>V�����)�`r�n s�[������E�͌��/4��\I���b��+�z�����ڢ��V5��&,�����zc�����U���P�iO�n�9��k���'��9L�#hF}�A�7.>a�m}vft��F׃l�o��Ý�M�J$�&<�Uk�����#'�k+>���^�(Fadp�T�Y�W/�1�{@t�{��<��+�d3�!iI�ڳ;NFHr�+�n`e>�1B�!j~�>�E�[��^��U�y��3��i�9+�� ބ�*���M��7	|"F�A�XQ�`�+�N_~��I�eK�	�	'UOOE�:v�M皓o�o���?������2�Ԣ��
'�ߎ��a�%��A�ZL�D+d	��6�‫��T��f�FNjq0��^/tܜwלc5�q�������n�U�<�|P F��?�2�Rݻ��O������?�P1
�M���_w�L�����x���͙}����O� x   #�`�L�����Y���cI��A���]�k�X6��q�_����h��{Z����%��O��V,����tK�u�G�0���f���=�S�~eZ�A�m�/q� ��*��?2��      �   �   x�=�K� D�Շ1��r��8�������e���H|�<t��I�����X?�OP�4�p˜T���[f�����-�F�Na<Q	j0�ygԂ,x�.xF#HJ��y4�d��6�����F��	6�*�[�"+^4�K!y
�&.�oJ�^�{�I��`ﶼ�΅W�s1�*���iF�      ^      x������ � �      �   @   x�3�4�740(�P������M-)J-�2�2�8s3���<�b4����<3��Լ/F��� .
�      �      x������ � �      �      x������ � �      �   L   x�3�t�IM����2�.�1�9=�RR+@LΠԔĒT۔�%�(5��6�t.�/.�I�s���%Cy1z\\\ :�-      �   �   x�U���0���)������I��0��C��zI[ ���H��Η�#�ݤ*'M�I�i�xp�R�����=�<K�8f�V,+��IT�� 5��|�B�팖��<��vo��9_ۈv��kE�\��"ox��j�`�&���7��矷�I*e����a�8�8_�p�SJ�� V      �      x������ � �      �      x������ � �      !   �   x�M�1�0���Wdl�$F�7u.t�$�k=1�\4��d�vx�}���sHqy�@l�k��H�	��eH���n�*��n��a��`_���z��?֬�P$��U�\�a�A��Z��*/�|F��dc�ݭι��{�9���?�      $      x�K.HOI�4�4�����  �'      "      x��K����'�n}�^��An�c{��p{���̆�;���{C�v�|{�����*�yH���K�*�3��w�������_�?�y�����?L��<������,?���e������w������iX�q����o��61}��i3�k�m�k�mh��_�6��j��Ŧ��o�]��6�������7�КTߤ�Sk�m����<���i[��R��.�����?���P�Y���CS�N��9�5nd�>�TSO&�TǶ�.<}w��n#i����x��[ts�i�4�5=Yd��V��9��úi4'����M-V���;��8��>����v�'���+{2�񦽒�7C���n3��6}��^�����a�������H��i?��G�4�^��2ʞs,c���Ĵ��]�v��eG�����I��e� !��f��B�5�g���X�~��{lP�����|5�_AϾ��|���Y���_Ҍ��<�z���S���N:��l
�E��>?�<��9<�|�NmZ}�v�9�?�\0~z|�P��
iv&��۠{��.A#q���� nhp��4�uZ��7-Q�zSZ�=;�i���`�NpW{�j��T�z� ~-f�Z����@����d��{5/p� ���|ڭ�%͘X�`5��M��a�g<s��^_�늋@�{�>j�Ns�]��ޠ�X_���-�p��*�мm�+��J=K�kl�A/�Y�_�ż��;=c^�t����{�nA��%n���g�	D���N{�'�{��û��~r]��{dȭ⻐�+��@j=j9�i~�M,���HB(�Lྲྀ��s�5�&�mx�l3�p��ٚno��7_�G�%�x�ӧ27���;u�͛#�d�n:q�l�r_wgwֻŝ�k[������W��^�����h���!F~8���K+�p|Z���9���;���v�Fh��H�[�_�A1��qi{�)��#��Ϭ���f_ߢ������M�͆{�Zhx�'j��o��&�fiWҺ�Ԧ�ɲK�&E�}���&gZ�tv敳$�<��}O�ף�O�e��M����]��>yʰ��F�O�a?|F�y��F�!b��2����[���z�ё~�,X��A�����>oa���e��w�k]��v�fޕ�+����g0�	��e�+��������n��\w3��0�U8�M�
��UpX�nq���^� ��H��:�^��P���{2n�
=F�+\�q$�G��m����Us���=%�x9oJ�{w��u���ql+x����Ő,�S�W�a"��g4�Ɍg��U6
�[��|��r�;������_�*2��M����';��	'�b�R"?s)#v�y�&K�HjS��
7\�S�y7�֟��ibc�A���Ne�h+sWcx�^�FE�
�Dcﬃ��h���ck�Vl:ˤ�Ǝ��#]��o�bGS6!�8ʻ��C����$�e:�#��p�;���z��L�æD�M�F)0s���M���x��i�3�H?����y�4+Os�Y�rp��Pl��D̯�ϔ�����֭?�9��~��	?�/�5���{!�<J:�Ћ�Ji !Ayn:o�� a/���M�Y�~MX�٨�v�K~3};+?�����W�{\J���G���Tð�/[���)����zK+�ȭa���<�Noȡ7*8�M���Ɗ�&4�j��������/Q�M�z	c�����[ҹ2�i/ӷE��O{�/��&���Gd�N���.3�<��3�l�IȿQ��w�wfy:���G�ZK�G��@'%Z �8cM[���c���x7��l#�A[�(�G�meTv�Y�Q��������N˽<�}5�V���v�����8b����A��g��mgd՜��dںm����l���+m��s�_��u�v_��TXf�ӱۘ\�1w�:Nu��1��d����<���X[�E�<~�c��J�C�%�\8��S��}��E��,�1�c7���'����7��[jE�'���6��6����R��f�tѠ��O�cѓ C� ��+�T� �a�N�3����u��O�����ܴC9��	kq� Kk��	���ڦ�s��%}�Y���>�6��:�0gӷ����'�jd;s��ڙT�E�]��G䧄�R��5���f]��S[L��f�Ҫ�2*���t��������?�jݶ�ܝ^�����jR���1
:L�v�~��$�K4��˻t�d�O(�����*��Ve%�ہrЦO���@��z9���:Ft��'����.� �0`�up��[W�#@a�t��(�+e�b�O0��f��yz���7C��rv�[d����j=}�f�Vc�!��$�M4ό������(�	��
/��	J}Rq�Io�����W=�:bwgCS�=.U'�"���أm儷Q�i��V����pY>P�R��-͆��GxC��X�a�ne���٦��+�X��p&���G��
�9�������g��.��W�,L�����G��ɾ����Ю�wQ�;:Y�8��9�s��d/��^4'{�g�^�T���ﬤ�VrV��ǟЯޮ\Aܢ�p��M��j�>��8S����	�0��wE�������X�\�~y'Ⱦ��vi�f����9�P�#Dh�����{�'织��������{(��z�DٽV��)��N�_g�@NVbɴ�J0��j7g_v�p�n{W�����c>��vu��ܦ��I��s�M�>{���P:BRkʵ�[���p�P�e�I�c��F�Ө������1.^FE���� �̀��ӱ��X�yiϠ�����hQj�h{�[Y����A8�^��>��� ��V P�5��Q�d��A�pۥG�C��PV �neKThr2��ك���`����:�xn�}�g�h��gp���a���q)�z'�?*=O��ڭ�p�3�G�a��[<MP�Xx����9�!���,��K�l����Oj�q��h/ƿ��_V�gXl�l�WfY�UZ����A������?�S������/�n��}e�<޵�凐dt�Z-�c)G8���ۭ�+�Y��R΃u�|�:r�
�{���ޫ�3m��XRg�j-W'��Ir,�U.tiWjd�߱+�[2ƻ�5w�<Ƚg5F,(��@Or��]v"![�z��\��H*�����W��m�mA؂ؓ�<z�����aߌ��٪�d���Yw��:Y�I1��<�=׳h�i��O����#�5VlR�i_�IK��XJ �ٲ�VV�M���gb�9����)]ױ9�v٪��v��:�B���[�s�\�Bt�O�,%�x��mn�Ȭ�U�.���y��Adݰ���w��ݕ���R���ɺ]K��#������F?׼Q%�>�u�l''-R�����sGZ�ߕ~!�)U�U˴��FCz$4G���O�֧Qu:��HII}�c�?[cǉ-��m��]���C"���R��W@}���3�('.����I��֣���l
�(}�l[�8@������Q2l�g�.5Bջ���F�1���)�vG�ܝ����M�2��]'!�t|����Ǟ�1o�y��>�>��odc}�=S��.������y�����1�\�p��-E�{������z9�5��]��k(6zՙ�I��X���VR��~��.��R�=��v8w�93��@���ӈ�p���O3�M��Ȧ�?F�s��%�A����@f�}M��:�hOF'G�>��m����m@��-w���v��n+������3�A�A/]K���}�@1�|�$ ��^�/�Ĺ��q��fŔuck�e�,�'^����w�ڝo�<�xyj?�$bܽ3��\���S�Ix�K��gZ+/f/�A�]���煣�}T�?��.ڋ�� @�aj���Tɕ���㭑���Եc��{[�����Vo���0}�;��s[�W�㩔�[�5�s~sa��n�u�1e���z��:��r-� ŻkF6����P��(�^����\�23y!�7�x��喳��F܊�چ���E���d�w��f1�'�3ƃ72�^����R@ʇ�    T�*'�Wq�
b����SA�����q��j�~P�<������
�<�2��*��Ԕ���I"8,z�噠[{μ���]0{�I0},���qt�P�4���r�������ot�-�u:���x�����-4�[��6��ӗ��ݒ�8�MtSS+!�Vh�uţh���B%��	QK�>�mQ�e��@�5J�^�*(�L �����ؠ]���ۗ�[i�8�����bL��楴����ݪ�b�7ХJƘ]��s*{�n��טݩ`��^�Dp�|�K�[���q}�΅Mѽq�5����L1�����V�G.1ܽܣ#�������.pU~��y�T�=e��	�U��rh
l��u�ޓ�;��,���Q��V�q��zG�O� ��C9�!��z&�Qg�)l�y����=3�!ӷ��5l�m[S-�'���t���j}�|�[�:�����bΫFU^C��M�����O�E���|��
����V{����x�=���W��0��<#r��-h�X�*7����LmƉ�<�ʻOI�6	�Կ?ZJd�ؓb���2ڎ�6y�� �b�z?��b�[-"pFC�v�_��\�H#$�D�Ӯ�S�tJ�@��-��ؑ��x �� &�b���qvߺ�[F%?l�e�p����:�}�N}�}�ܤ����t^J��)���jO<���.������=�t��� �w7�`C�A�=F��W]����P���Tnvb;S�Y����n�;d��O	9��ж%-��'��(�7�ѽ}��o�'�݋���A��%d�	���0�Q�9�Nս�S�����]�:�`<���^���|�j2��y�k���|��xtZb���(��G���k0Siy,-O�����nMW�l�[O4n[��4�ӹ��� �S�5�/xB�z*�p�5��b�{.��tz��j���`7�e�|���>�y�	=-b%�>	_�Q�n���J�<:iF�*�����Lߐ�.\���e�Pn�r�~#��3Ƕ��R��5*o,������u
ϳ�VU���T��eU�������_�D�ܙjsu��T���j1�5�HW��c�~��W6R�MQ_7�¦9�z���*�k5��/�F��lm��:��kׇ��FfY��-M�MVjQ�l����b�S���(t��槵��o3|0
�gh�)Ыݳt:��"�N�6�TL\���>Fʴ�����{�i�i�q�Ʌ�bg�W�y�w���Z�Q����/w�6Â6[����t�r��󓎳��~G:F�SWĘ��tL�i�a��[b� ;�EAI���$���l��F�O����-*�#�bץ�Ԇ��t4Hѻ5]k��C���S[dٔ����P��ߡ,�X�n��0y_�l��c`.�V5a���Ѵ<3F����ي�9�4۩��vv�1�jt��@��y�1:�Y1`��N	4
8�0��q	$�pI���R�U\�TFM�!Hc[�=�����-�N4Hh8�VZqZ��n�8�����gx>�Ņ�E#��05݆� ؄��*�[	��x;A�k`����v�3��gw���'3�������Ūc��nX'�����˚ͽ�ǈ?��L�U�D�%���U��u�[ݨ���a/����oq=����e�m�����=^�	PN�U���fsޭ�J�/����C'��)�u���*�H��M�2��3��q�H��on���im��o6��t�͂����s�k��Q���Yq%��.G|1)6��O�z���do����M�����3�s����A�&d�?�o��|�a�)�೤CI���d.uW)��\��,%��Y�K�V.�$u�o�3��>�G�-�J��Z�>���q��N��]�_ņF4(8�t���
�e���~EGW+N8�(D	�FO�Q\?q"pr=�w���a<ej���q3��Y���a��z�p]��~�?�0#`Iv�z�^G�݀���)8��Χ[����X����Dú��;�pT�M�vp���eVc���.-���G���꼵���坿��C�o�sv�.�+f`d�vwi��thΨ���m��=��Ca�
#P8�`��-��d��%N�*����r�rg@�	����cuo�ܷ�����_5�ʲ�AQ�Ӳ�N0�Q^������`2b!"c��ZY����[�R����T[1����������h��<�'\Ϝ�O��b«���O�#ԮIK�nk��{3��F=���V�/ ��?
-by����Ap:�;[�7��A��<u�.0��NVLw'/��!h��n��v��?A�����c2�9h��M���
{�=��A���A�O8�N?�3��~��?���?��?�ˏ�����?�e^�{��*��7��-��Js��n���`��?�.
[�ބo�O�<�$s3��o�d��+G���yrUw͢�e����]D9ׅ�<�Y�'m1��jw��K�Q}�Yl� ���@K��T�ržo�4�K�9�R�@U)De��}[�i�i!6/�2��!��-a������ÿ���R�d���{�6@���}B��1�+����洏�u��gy1�o�s%+�ul��}��I����K<�,�������%��"��P�����=�������ha9}�X$�-�.�;f�'�����fy�Ht2�n�b�~���Kf��*�r��_t/D�[���'����<\^��N��PŌ�=עວZ�uTٳ�o��þ�,��3i9��D���N,����c�J[����&
��*�q���f"*�;@y"p6Ny�����δ����w��"}6E�M�W������tW�Vt�W��w=�c�pj�Gay��L�}?��%: a�ʕ�;%�X�K4\b�}�仰���[8dY�����ը��T.�Dy\8�]�r��j�x�_�
#�����m�Y��۳I|-��@~y���7�^���1S�A�oj-;�ي��e�r8�eňM��=����m�,Mxc'���*L+&.�l��X]���^��>Gx�N�bt÷r��;�OΌ�]v�> o9�H.�Wc��+�w/��AÙ��UG0���(f�Qn.Q7��%R&[ӌj���ޏ�6N�ٛofn�}��h����]��f	BGE��A��4��)����օt�$�P�E�glqJC��f�p[9-�����k8� �G�Y�8�V��%�y�7ph������z&(�`fyy(;#�r$+�=�G�¬X��+��H����jqhJ4�B�<@�q#M�����8B�d�]�яaCs��l)����DN�A�	����&16?���@������͡��l�����A�׳p}�_�`�K��C��4�,�+������>�(��핣�9��a�Z��Q��6�س"x�=i��]J�.�2� ��r�W�'��%Q�}�K�>�j�=����\��gMd}l�#����q�j�es�
-
��p^*� �8��(�VGg8�Be���EP����T%3�@0��,1� �p���A��^r�����t�Y�L��B��Bō�rQ��|>��L�c��[��@^a�uf5����[o��)n5�]n��Z�:�����<��<0�`�.Qy����*�iis�u���<���A�,�Np�=�P��5*s�<,l5���+��s�L�_�	B��r���;%,c�%Up+�p:�D$Lr��I��$̇����KtӋl>F������"��f�V��g+R1O%`���ɟк�h�VL[�g'&EvLSdM�Ǚ���L��4���������_~����/H� ��$%��e�$�k$�!�*�,��1�M̼AVy�S+��0S'��
Q�Le� ����j"#�$&�LM�/�D�R�Ia_�P�E[;Ӂ��r�i'Dq�&Q|g���|Նk��Ai$DF��P�Mr������Ql;�P�" �)���y�S]��}э5����g>r7�U�h��oWwa�^)��{���b��Yͦ:IE�+�RyS)�U���fSUɓ�z҉%�>�=�^e�*�Q�ρ�{U�8�Y��2pU �R�(�Y�h}�����    溍����su��J]�XŽ��dl<Y�U�d�LW�L]4������*u�mr���]c:i�Ma?)0�0��Y��"Ƴ�z"K-�����{�h:��ɮtMo!����P�hE!>&���+�֝�R�ca)�r��%u�/\�&�m�a���Ѝ�����2H�uI��F�g·����"�s���2E{�D��0��ڿ�}��5���ì]%9vr�7m�~�s�Y��2|!3>�����F�a_z�e)5k�y@�o��C	_�=���?���Nǳg��8S�VB(�+b��E��sb{�zb;��^|%��~�Z�r�Q��+]�ܼͦ+1͞�7�mę-w�<�*DyM򚪥L�JD7̓+*�dV�/�b�s�Iͬ��5 �]r��0Ԛ��a�0~��y�癟�]N�jܻ���ZN�aIb|.m��� r�<�ڌ���y7rY�S��]�UY�u�@����z[��5����./CF'���3��l��~�I��Y(���)���|��E�O�☬��t�:A��L0�ݱ賶���j����j��5���ָ�'Ǖٞ�fX(�сZ&/�r��qU�Z3�%�;>��MS����q�K��:|��,Xw��^ȹ�o`�ޓs������)�A\�a�.��\��~�O~M	x����Q�5~��}P.%�ݥ|�$���򓥐��	�\SbH�cfI�o!Y6.aB�^
#d�b�+b����P��%�8����C6��5�)ﾚ\g���l\�����ʳ�T��'du��\龒���xF��WOL+�r}�'�����P=���z27� E��=VR�E�<�D}G��ş!{zBh�}�'����Hԭ{���D�7�N�^;>���%��K�z<�f<!��
�O��H�]I��G����\%1���'��)L�R�$����?�2�?���_���7�'��!b�j�{���s�M�]\8����W΅�n������"�O��7q�r�3zo�2��j��?|���k���;��3������j�;��X%��d�K��q$�Mܰ��_�n�����ﰛ0�k2�y�Ķ���������g�dˡ$����ˀ��R�kĢ��A�A�y�����i�[x��� ���&�m����ū]�r'2�4�і�T(�֓q�H?�������qU+:��9=]�;}���ۍ]�D�P��g���D�τ��{�0��d��Cz3}G���i�-�p{���'�y]�|4n~��װ�Gt�<{zwlwZI����+fC���]�)I&?�V��w.m�E3+�m��A���2����R��U#	��h�����P��!�ɕ�#�@Hj�Z���\W�LBH}�J�����FuZ�Y[�������_�-�c,}�%
�#)T��D�L�a��I�0�9��B+�\�̿	�0
l�tCT�Ϙ�B���!����uZs��|o�VK�Ymu�2��'�8J[G�tG�?�m�Xeed���YO�y�5�&����q�L4�o������͖��kj�hZ������I��>e�%��Nʠ:I�5�|L����.�LL�)�>:&rLm�D��J}I{aSo%��m|Nݙ�5�`����� ����}�	�_��a��jJ"YI"���I�9@���;�Vt���	�+��="��QJ���ڷV���W�{k�qP��Wm��|��G��(�\>�Bڡ��P�U��6L
��A����̢M%�؇'��vN�|3(7|�v���9>C׵��(��9,��z7 Jk�Wi���Z}����$<<���
F����x�����O�ڸ��_r�?G�9IY���:�b�0^�%G�n�ĥ��<��<��Y�(���(d ��v��N(�T���Q�*{��������^ǘR�9�fc;r�o�s�ulId���Op���r_O�k&���!h�_��˱��|�ZhOS2�7�w1���VJ�FPK�D�wo����ޖ��x���=-�cyRo7��{켅x2�#�;���)����]���h]�3<xb�'���p��6�ߦ�k�L�?��T���{�k�Z]�}9k=���#�^߮������)l�$Q�½�1UL����z"�N��C��q�+u|�!0���´�s
����Lg 0� (��+��	d��yEY��.ܽ�I�ܷ���]��7��E]Nk�[��+���Gz&�����rs�5��>���;
m��Z��H���q�:��0�6+��ש�Jdz�{�R��g<�^;��ڴ"�;dU������;��x�*y�0��u�{�[�����V*)K�
N��?d^��e^C�+ٝ�>ʟA �;����ϟC�u�3�ȣ~%��+��5$W2�Z:��dS�%S���<>%�ڤT��^�E����}F�R'��T�S�%,Gӆ����):�1���K�BӶ����^^I2>L  �W����a��}O.�Ӽ���'�zRF�'B��ަ��h�=�c3-�˛����o���;'��V�vwHm5˼5!��U�:i�r���Rm��T��@�
�gy�����������&n#�]��}�!�ȗ�]�G��t�%{�r���ƙ��l��~Ӟ�
��š�X,�cxHǸ���m�%G�|�����0a��2�>>�7_�{��K	y�Ί}�&��<}�ґ�Qz��;���..cxNk=��8�2�J7(�{�߻9��L��;�[+oQ
��<8�Y��p��$�ǻYx:����_;؅��[�/V��A��7sx��u@��Q��Cƞ��v�Q�����f����D�L�L�í1�_�+�=j�k5�4IM&��5	�}�@�J4�a7�PC�:��Yt'�:56CZ�Y~K:�ɪ��>�t��'ul��:A���&�R+�hi4��D��i��=���q��H� 7r:���"�Kw�˚���7����-�5K�R�m���5�m�(�k�(�<���P�bz�:ߏF��-re桴�E��4�5���m��E��c���NW#�&��m}�Z��cXhո��VR�n�l-}>����<M��E�MZ:���{��bjO,3�D����H2U�x�"g����<�Us�}1VR sX����u_%��38�]���_U��7�I�P��F�NoK��v�^�Fb�U�"%m�������_Ө�e�r]9�9C���"~{bG�S6	ˤ�X���c���={��mJlF��oV�:]п���v��[�R�Y�R�$��4�S�!�Q�^4�[^�p�܍M�{Z,��P`�����h�v�f#��ү�:�!VdH�>��[��bt�}��T{MЄ�<'�W. �F�5&���.L�֦a�p�6�=y���l�u�D�g�Xx�3��''b8���<�۹�'�6���
We���K �?���ڐ������a�Zו���d^m���r��P�9>�ytG1>S7���Y�Y�?H�l=�RG�b\W�0��`~S��,k>��~��f`�9c��v��i�R�yY���:<Ռm�e�n^����H�W��)U�0D���H��3%\��'-�G���jG���F������Oz;���=��ÿc|Y9Y|2��W�E��!�h"��3�ݾY ���}���Eh*~ZL��t��`~{�R	DR���/��4��؞UiL�K���K���u+FєgM#A�d��|�F��^��ׇ�4�`J[�6(Mca��v� v(��u̲U�8��g�W�M�F��A۲�'���m=�}�e+�8��~;��Z�>��y�i���~c�!����<[���U�i6q�V�U�����O������2���o�vi:���G�y�jz��͗�v�i��+�&_�B7�������ׇs�F؛��0է�p�"��p}�W�=]�x
��9N���GH��銜�Y�{?��=aءy��s̶a�e�@"Y8[l�l�^�_G�楌"�[���u��g�f��%�$�%3��*WK���nm�AɖW���9�B%�dG�C�iYmfby${dyc꫃��H{Ȥ�:�Y)k�`l_?Q�Ü�3�J-X	��rA����2�5�k�8^H/�}�$�o��xT6%�Ǖ�dce�Sq��ʤt���RV���M}�6�    dT� �r��(Q/q�H��+�S @Oc4q�ā�!o�z����K}�Ś���\L�\��S��R;�A�VY�>ew�>�t+M�I�B�/tȑ��@��g�%`�*��E�JLl�/!��ӧ�X�!���X%�S'G���'5���������B
QM�1~7Q�4�o��m���TVii�VIS}�vz�<�9�0���J���:����=��o�${d'�Mj2�6�;��aچ�=�X��{�z�K2ݵ:��fA4�<΁��ִ�c#��Cf�!�Z�:@�0HVс�6G@�s-������ ��G��OXi��_�Q㰵6���Җ��]��"�>�?�Kjd/tvF.��Φ�}���dN�Ѕ̗��� xl��m�e(g�ٽ5��v���To���Z���|��le����O�'�:�=5�m��6��6���y���Z���@���aWs�����>Y�kvI��2��z��K>�g�G��43g�hx�|�����om�㛺<�<��G���t^�-C�I[�.i���vG�]�����y�S]Z����]im�j�������5�jN�6늠,���obWnb�aӃ��m��|.Ѝ!G��O7�����.�|[� ��@��a���65�a�
�Ffč:�� �n`�ni9r:/�9�)d`�_����������g`��#l��I~R��=�}��<�=���¸qCg��j�c�|{��e�t2o�����2[���e悥l�dPR�x�Cno�Y8c����!�ZY{��ՌRG�ܖ(�5�����pKJt2�s�@Q�{��&p��?re�o\��ykG���#9��Uy�wۉ2�-3�#E 3Ze��Ϸ��o�5-�������]�e:��}~U	;��5�[��pU��Ŏ~N��3�JTS.�����P���=��{lA~/�R��Юc��Юc��Ю�߇v=���v=���v=���v��>��n�ڭ�#f��m>�H�#�0Ȟw0�� �ظ��h�(�Q��J�GP��K/�D���E�RDzP����a<�I��٦��<�2P��T䆇zR�&�:K��d	���	�&Q1��x�<ҙ�B�[h;����g�2��r2��ЄE����W��H�(D������d�1�2���y��_F�e��O���?�����<%"�X���V!R�J��-�K�۫�]��7#Vu�&���ړ�Xo�e��������wU�<n����Y8g�׋������mA��jk{m�L�^�e'F�m��~�r��8�~����w�r���$.iNnJ�ɩ�w9}��}�l��iO�Uv'y-�*�)��Fk3��GO^֦1R��)Z��Vo������&��iB؏m�:��Fs�Yb*+�iwz�Q�@���?���Y�+�L�[�-�C�c66�c�*&>�?c�5�|���B�f.��l����9xg�o��,��ivy�l����\�V7ږp����M���+_���As-6��K$��4�b�bm��.з�&�n($�Ù-"3JX�8ȐʹM���옃`k�1�bI֓c@�O�6XҪ���4��>t�;lkNs&�:�װ�~��7���Њ]VK��m���V^�Ij"�%d��6�Z㌫Y�akW��x�B�����-��M��3������e��ubHI��<��y�޸
v�i��u@s����ƾ�+�����H�O�wC��L���EU���0�K��f������t����.����1�!�����&�vy�7�ۗ����ĝ����:w�"�".^\�;��bj;�Y�_-{8# ;G���A��A|�?��׵�z�G��/��?�޿і�ݚ� +�9��.=1l�W��J�^��нd����t��#�ɿ�нn=wC?)�#l�Kh�H�ޓ���E�{?���N��t�t��J�l0VK��������W�z���9����o���|�s�������_�S`Eӽ~2Ȧ#���A6e%q�>�Hg�
�wg�s�2 �#�E�_i���׬���P%���[���0�w�����{�
�tomwc�?��B�e�[E�غ�"0���ׅo���/a�VI���o��S,�r_���y���}o���Э�>oи;,�����?$��%#��?��^���A[ߕ[����7y+`�b�/	�*II��o'�*a�@����AS���L����w4�]|����I����*xP\|]�T�qӯ*Uxsp�U���-�m�����{5���~?��vR�qd�3Nк�0�� ��{w��o��6��2��f�g�p�k;X��3_ۂ1qX��\�W��7��BvYx0�.-H�4Α��H~�>��>�݉.����H��Xވ$��_�/#��l�����w��XX �q>o�N��$T�>�`^�H|�`J*!I ���.���Yx�1��B����@�(��Nt"�����D��3i���[Z��j=��MV��Ʒ�o����6��}�	�N�c������n�c�W&��:E�,`��gs����w���N�P�ݪ0� �1����(s%eD�47H��n��Z\JZ�w���>h��� �0GA�{	��� Ã�p�&��!u��د��d3Jؖ������;��]�.���X��[�/a����6CD_M�`ڛ<Z�¥T��x���x��:�",��}7�����6�y�B���E�ql����[�>� F*���&*p�s��_H%��>X���R�M��쌒=l�wo"]Q=ˎ>Ž��f'	Q��l�(���O����M��:y����09���=ƚ1�0��a�-�!l�����W�S}[�_Mze�����z�!�a���p��"F��]�n����zߧ�}��_��|�AͶ�gV�F ��kWP�"�p�,' ��/���-�I����=��s���"B�g*ҙp�j ��D>�m'��m�;�F@I�3��O,��#��PJ]#����G	'�>�}"��e���f�'~������I��p�o�|�X@ �t  N��'NpAS���]����cF�p���#� $;��ւ(��ݽ~�p��*��K���R�� ]�
�˸�F ��f�r�t����y���fk�2�1�D,�'hHԄ�Ig��h���;�
A�h�'�Z��J?��A�ߠ��f�?V��Zz�0���턲5��*�R׸���fuX�U\uN��NwApC��w�e���=u��n�6��u�H���~ V���Z��D�xX�zu;��qd��%0�X7!R�0�^#�z���#��q�PF��4Þ��"�Wp��Z��w(�S'=�ˑ�Bl�\'��k������������K͞a�d?C��7�`�uD@1_�gfXahu]\�F^g��%�m�-�U˦�!���e��[��N�VS�.L��ޝǓ� ���*S�I���E0`�p�L)���cs��:IUB�ȰƓ��,}��Y7�����)Z�ͬ~/qC����l���P� ���r����c�]�g�M�Ĩðq��7�qMQv\S��S��,S���1:���S��pDug���e���Y��_��˝�&>l����I�^��>���q�W�7/d����/޾��xI�ȑ�~�2�-M�p+ly��_�j�h�J>\���q�=Nw�p����75��iS	&25�`"� ���u1�:0��+D����i_��沱�����L� 7����{�f���%����[�Ha�����9����-�H��R�P�cjeC��(� ,Ü�u��3�FC�j:�V�?r�dU!�`��A(o'!1/��疡;|�!)�R��'��t��\�,��-m�D��F18�|.�{��y����&f�iv�ljl�o�uH�U�h�{=5^�&���̻{Z�鏼��Oyk_N�������l�34�Q�Q�/S	�w/8���U�r���Dr��i�@�ߧ]��l�GHߓC]dW�0m4�a&jN�dA�|�)#X���۽�7�O����:'}+IV�d�J@��1�L���l �B/|Ot)(��bZ�&<�G~@���I�]ЏN�I�    9�筜?�཭����r��8Og�
���(e�W�eʋ�Mҙ�`y|���<Yi���9�%�� ���f*:S"�t"A(����Y`t�<���?�������/?3��_�_]~~��/?� �*�x󺻣h�4
�52"�����U!\�TM��.=��91�p��k��ǭz�� ��:��h�`�H�i9ڜ��(ULc��en5hnW�i\�Q�*SK�j0Ni�PO��-od����y���[��=�)�L쑈 ��^�7s����t�`&	��$g,O��xTV�L����5��5U�<뫫O��$�X}�d*O�Ldo�p"��շ|1y�I{�H�,2A%%�����4�XM[��[�G���٪���8;6�a��V��2I���'�)R��V����M�����.����˸�eƫxk�{EӅ0��/�2�v;QDO�	�R��Q
�s�^���A���o*���"��߻����}8|ߧ��H���0S[L_����H�1?�}#q�!�Nd(�������{�'C�djB�d���M�=�U?�ȋ�W=I7b����0�~��7�w���%�PԜ<�<l�~̼�<b�R�,�g�Y��-���}��X�643��(Ѻlu�_��wk�2�dX�`�3���������,�U�����3�E�2�1�~L�=z7�Mt�3#��&~6��lzš�����lA~w`f�r#���C�9Uh�_�S���OmK�$�v��b<e&��f�@%rzW���#j��>g��Mc��}	$��n
9�v{� K�)1g�Q;{̹
�~�*S�U����)�z]��=Y"V�B9&&*M�waMM���b�e����f|���-��Dn�Ŀ�����/7��BJ|N>^�
%��d���F��R����K�Y?�'�1`�������6��=�]i�ؕ&H�b���t��y#��F���h�
@BO_�@������-�_��<��b�b����#x�k�p��ug��
�N�ZJ����[P��-�=m�pݩ�� q���9��s�ǘًZ�T1	��k(?�'??c���珻�����ɳB�U��xe�z�v���˘#���w�E"�-Y�.�'׶�bN�$
<�W�w�%�q��a��Ȉl 6�7Y��u��n~�y釲��Y��V�"݋�=�rk�o�������w�3�}�u����M#:sY=�?ۿt�PK������ �\{x����q����<��2?\�	ǯ�<[I *�1�Ķm�O�U���Tn���[[�����䒆h�ꆨ���
r��C�_���;�q�����akAk5��~׵�i�(4������>�m]�esJ��eo/ss��Ǔo+��/c���m��(��zxG�⊴�#�wO�]�|}^��{��q�Ǖ`���zDJS�����;�+�;�x$G�֪���b�h�W�7l#f���}��M'iq�����)c��4!�vJ���zH/T�-OT�'��rG-����i��+����Ձ��I0)��i���^��T"
Ϛ�f�Ոݟxtn7i�dɲ��b�G�%�z.b�TXO:)�{Q����Y����<X.kU��Z��V�_i�a�/��S���9��3���x2Y�ץ7���zU�߬'\���B6�q�~!Aޠ��T�*��oB5�IS�k�y`]�4���ʭ{�����<��G���c>��;���<���#�ϻ���}f�G�f	�P��Ј�=|�X�E��;�o�I{��FS�6���ߗ���Dl1m�:I���jk��Հ7a�Eހ��N8������̽6��:3����lF�o��ZÝaE�E�?$��Zg�.!}J�G��\���<��ل��J)I�%���{��^GDx"�!S��4�b��,���^�,�ʢ%�B�Ӻ�t��UlQT�F�A��V���uY24-b�����P�_=�&�n�Ҫ%�r�r
�\Ez�v\�5��VO�J=��Z�ǯ��#���%y�c�G�������3�{�^V����X�j5��D*Wm^SA�>[�F�C��y��>U�mwi|�}4�o�ۦ��#�a�[2ݲ�ʫ걢�8��d��p�#TnHz�+c%���c����c��ܦ�dT��qˎ����z�b���7�v�:L����B"eCR�I5ƨ����
S���H�nT7e[��ϕ�4ok�Ts37z�?`���gs�.��h�KNú���Y{%�,�6<�E�|}U�/��T�F�c$w��`?ݨ����7�?"�Q�U:��]Ֆ�N����{�`C�O{\cR4n»���C��R��):�YH�զeD�X8�7gxc^ב��h��0.�qU��f�h~�2*�Q��陦G�=�}1�[�^��mE���"3�ݤҰ��a���/�Q�n�)�|j���ꦜU$��-s�"A��xgx{UZ��}G�g�A٠����[�쨗l��Z���\Z�|:�K�9�CG��1�ƾM=��ȺGʺC�9R�P�#����} &�r��cd�'}9g��҈�sC\_j�&/�t6N��ℱ7��r2��~	�o�A6sJ��F��y������z��|S<�6�}s!���R�L�nS��&��?5M�����������35�VA�B�P�}$q_��oQ�7o�/6�~�����C1���.��/3�g��?�ZO�7���V���Ř��w��~������ѳ��i�쥈���Q�.J���*�1����7�6ԭ���pL6�e9:��ʪ4�e��9,9&�zAz+^���!��gO8�Ő5]O�6���]���1��3�ٯs������=�D<�A��tpaм�?u�1oz@���:�9���w�i�i,�@\��2êߡU�D���H�A�m��oٗ�׍X
����}���/ZF+,��ˑ6i]u�bKm�/M�6��X$��*Sd�����{y>��s��v�;���m��`�#ڿ��&H�X�0�ld��4A;Yk��q�YvXk��e���)�jM�j��K�:�֘	�\��޲B����ۘ�E�/^�}s#���ݴ�=��YM�!�	�u��	�z��4��(�SV1ì��+�X=Q
��a�������� z�iڧ ��D������;F����f0X@b�1|�� �e_�F,������� cԥ=2vv�W��[���H	kn�x�ƭ�'マFcV�E4��ѽ����dV���a'�c'��;��Dc׶=��q)!�t�K�+|�Fb.�3!Al]��16ָAXz�1�ɉQ�:Gk�6�v�|�o��	wѲ�o�Eט�hee�'��O��݀�K�Yc�6���poI������wc]�H7j�H9���G���t?�4�$z��TG��h���hA��b�t���^K[�,�c^����n,��'']�a1p�������P������;3.eg�g;c�h������#g���S�r�1��٨s�ԏMieB+�Mx	�C�@���g1�g�=>KH~��B_�u�g~,������Z�u�u��d$p��ۭ�B��Kxx3���_=����{������.C����Mu7�x	��_���N<S��)�K��)>>k�v��[��I[muҠ�$���In����4�u{�J��ß��� �vJ��z_�B/�K��AW;KYf�z�ĥ� w:��tJ��C�]Z��Vx�|
�!v�ݲ����z/��'�AK_�t24�Ը_[�G|��4��FH^�-�	|����;t�dq��|'��ǉ2\��v��?���W�n��M��q\�켯�{�kQ��@,��q%pY�O��<��y%����v�g��i��������3�>��t��N���p+ߍ�Sg��8;�<q��gx�����A ���s���{���5z�7��=z~�^��:��s��|�6��W�x�c����=�ƽ	�,>�t����C(|g.��o=>��սCg�Ị�@�{|4��a�J>�֠��/���:���=l\�Ͷn��:�qf���,1�i3�r��	H!J/p	��b�92"ԇ젓�o����1$�9cćo�+��G���5W    ������.�G���5�(2.��2ԗ����s!��Dv7nd�`�k`�hU�`�g��a��mC#	.�@���@&���~<	2a�1ȋ�Ah.�"���5'B	d�|rP��HZZԍ�̋Hw����v� �:�T��(�"̂�]R9�@k��I�ubeAK�܋}���nM�b���m ���=��l��9���>��D2��-�(�s�R=Z�v0gPL�B[$㢫+�>�ia�Y;���H<�=�A��5e�Iڅ�[�Q������J�zq���3i)���:������V�n�	��lZ+K��ݼ��ˎ��B}��'�!�Vz�!�Nz�!�V�i��I����Jj�ř���_���-	K8dKq���i�`�.��K��F��M�f�P�-���������a�|�o������%1i����K0a��(Ϥ3��B���\ �u9��Ѣ3Z�ߕ:�%�]������U#u'�6�ֵu��`������+ϕ��c}ә���kS�б�^�)���S%�b$��(B��x�ol�ɝ��q"2y�ԃ�<���zC��p�逯S�	yX��:g*�#?�<���%�:�� $�"U����~?֯gd��_0e	��f�G얙����� ~
���^#�3��l�AϷD���S �lr���R@��&$i�	/�#�<���%���x!F�1���nF�=�<������=�/�?��;���wLZ�� #g�֯	g��)��Z���q%�1��.%�
�J�ߖ���Z�E9�����c7�6�	����H0J�W곅�]Z{�m��E�
��`�$\�b�Ӟ���������Jl(|�vJ�^�ɲ���]HSY檲�ӫ�{�9��hߐ���|�2��P�&VьB
U
w`���7(k�rǰ�U���.���h��r/��2p�	�>ϭ�=�H�"e=��|��@��`b�↤9�t�HFL�`�X���+@�X��N�[����bW�Ok��><y'��>R�5�[J�ݼ�����K[��x�A#Q��zRL;�b:��7�=���;�.]p]�/Qdne-�W����'�*{ek�D �����Y�a����(�+g$�I��Z��O�����ج������_*��#�K[�`pg�9s�Wxv}G�m\�,^��g`Z�&�Ty�Z����� �N�U�=>Hd�Mk\n�>�CP���˟9��;��H���8�؞��x�����ˁ�q�yʁ��X�L
TT@AHld��ڇ�uh���^�N^$�馥�7Qo��J'�~��gj�-��C���>��t�O�e.+�w�;P�feq������d�8�j����Iy�rR��Ve�ۢ�[jh}<9,;����F����Ʋr���pwc���1w
4�Ӥ��!�8K6���Ӝ1G�z��)��!�ȭ�p�.�Y�gnJ�e��lJ�4}��LCϼ6~D�О͸�Sb�癚��&��At-�'��lj�-��d�'�=�O�ÓQ�tO�2��[	��Lɪf��+e�Mh��sfwE*�]����UD����1Jna~�LK����	*��]�:Y���`�D�eZ�����S��?�7?�7دic�AD�"��>z�����������<���b���v_gۦ~���k�ϝ���u�K��M{��X�Oa}���ث�,ȹ�/c]݌%w���">ˉU,iهXh��C@�=�5�$V�*"��pu��މc��*p��
���n�= �������¶s�6�/���]�NbGH�4� �c�@L!�Bh���&��Lb,�R��X���ۗ��4��$4�z]�~���jJ�6�o�v̭������-�͍hR�<�>�ܗ�h���h�9R�(��A �sM#j�� �������J�ʺ~��luc�O�4J� 7�dA��*�~�����5�)q鉅6��Vd=��/�AO�'��q���q�!��@���n�"�eRr���ОD{��c*Zz���D��\�"]��g|�n!?�i_E�#����8f���֝ܜ��Mޙ7�	��~��A��k}Ve}��y�����Q���y0ҫʥ^�;�}l�J����w�t�@�C���{�[�a�n����� V�Nq���s�y�^f=��}{q�<���H�Az�:�d>�����uB���cq����;���>$e�s�{O�YHC���^f�ӊ��mCO���{\�����.�Bc;��r�x?X掝D���r���4��_�V��>'�hT;�[�|��
�_�7�ݞ8��y&)�?������wb ~&6�&˹�.��O��*o��њ����jjO�F뙂c�ʣB�FC�,�� ����=(��ZZX�E�#v���}�m������9�]�y��;�WF�Sl�������g���Y��N���,r����V�p��yRv�~>'��q6ٞ��Ʃ�m�w��m���5B�$o�bJr���8�1W/��<�,�8fC����������"�uA�,���7L�D'�(S������ v�9� �G����æ)3ƍ���y=hVk���Wf���k�s^+z2o��P1�:�����{�51�.�jV�<GE�5�u"hg���(�!�j�t5(��6%EB�p��GY��N��r䰢iP��Gc�մ�<�۪���ڰ[+�߇��ԥ���CCޞ��);�T�}ꈓ����p�C8�K��r�.c�a8.?������Ӽ��?����������h��E{�	Y����0�@�@�Z�V�0Ϻ�Z+%���;�h�W�ɩԭ�r�տZS	�A��]>4�L�=�������{?�6U�s��c�&���e��E5�����l&t)�R%�ϯ�$�bWUR-$b|N���J��i ��.�5��E��X��.�*֣�TC���1E=�hB؁,�����il^X]�J�ᷤڣ"`�$,ˍ�c�)��ء���S��[%+��S�KF^��!���c��/D��Y(I�K��*��`N���Je�/���没�������ʖw��d�(��%���r�RY����=��=+;0���0�ݖ^���0#+�6�*f����s�|R\*H���(�@Փ���>ݶm~W���6�����=m�/}���\�(ձY�&s�s嬐�>Êh��<��f_��a��j��|0L���H�2Hk��2���]�������l�kk{8�8���6caP?�>�#�:�i��ۋE�a�f�.��q�4G�Y\�(��v�ѠS��w�|2���,]��Z�
��6�׵���i	n�q7�[B��b�D>�q	ʲ�3��;��꜆biT�E�
�щK0Z�����&*?r���߇�������T�'����d��p���p��y&�BP���:���w��ӓs=+��'��H���9��`�F������	1�ø5��o���L!�̇�.�x+����zN�Џ�����)�� K�>��=,C�*qfJ�L��:5�n.���Yp��te�܌���fE`#8�������svנ%L�
B��~��sS*�2�	��ُ�o��۸�6�g7�P>��S,���/���	f��쮛>�pG�t��w������5�tЧ(��%L�7.,�>��i�ֿg����ln��t�78C��s�^ՙį:8��Ik�t����A��1}뵅��4��A*b����~z�����tM�+{E���Y����ٕ���oiI�~ns�i2�!0�y���YZ���k?����X�Om3ʟ�شu��y���cby�ʃ�6���j����I���І�R�t�*,3.b7�z��R��so�:^�bՇ�����nW��Ϋ��N{h� kl5֭�[Qv����[9�oá�][�yi9���s���o�4�q�<
��~�>:X��D<���9��+s���p
]y^�~^�95yN�y]��i��:eL����:��g��G����X�N0�� N������=���I�t�S����0ǂ3����y{�5�+��k��}��2�2��w    =�V`�Y�s2Zb93*�+ذ|V��P�Ҹ[�q�Je�p�s�ܝ#�xU����uY�r>Qvvڇ.�Z�Ve��R��u�`ݹ�շDgR\��Nn���w�������;_�?G������3Iy���m=�^����-��Nt��B�i-}�G��{;��9g�ޟ�v<��2Ğ�G[�<_�<��*��d��黠�L9�K�bhC�M��,�\�0KP��!��Y$4�p;���JY��p�Y��A'��� eA��ҩ�'Z��8m�&�A�����Y�d:�R6�C���VU��:����kn�H0Q�|+C�B�
E
�W��DqJd�R�E�aB�da|tf[$�:��x��u-�ǚ��S�L��f���Y���rM�Np-,��{Z��}ˤI�bqt@�0���1����Jsw���c*������E�e=�-{d�^��8�0�wPV�K㼸�т;��IW�J�@�bGdG�!��졕���<���	�M�ԟ�8��3����L�HW8x��lvD������5H=�r'�u�����R0qAԑ&|�{Q�y8]#N6�,[���3 �r1����.PΎ�����s��K�{�������<_|�h���Yk4 ���h�@z�*�x�����=��$�}G�k���p.�C�e#a�aBv<�y���J85��L���,Ѕ�0Ɍ��`�@�F�8R��L'8��\�[�σ�+�Zy6<c�,Z��p���3�L������1g1S�2��-be���E�#���^���X��kޗ>��Ue��~_|����v_V���eT�6��M@���fl�~�C������0�T�F>�Q�~�=���2��76)�����sJb�St8l�I�Gw�o���I���D�-t��v�};��Ht8��2�h��?�I��v���r~U!�;QW���̤����-Q��Fb�����
�1W��z��1��(��ŉ���č�(�o���23���\�$AGe+~`���g2�.�L��&��h����c�;-�@ ���|B�_N�lr��&uцa�l�S=�r2Q���-�J��X�	g2��a�%i�7J���蘧����2z5����IM������[���z	m��:Gu��P<��������G����Hh�uQ��s���@�u�K�6Ka�� Jܹ�e	k��5o7���簎���T�N{]��u�9���\q���2'�LwaN���_�����/?~nN���� �eC�qQ&������$B�Pu�N�PU���䟛��f�B��ȩ�F����x��]㦲n���8,F�R�۵��r�<�96��I=�Qn�ňݤ������8��l�SF~ٴf3q��+�R�*��2߈���R[}_Ey��k���=�ĜBgV���;M�Ը�R�
����qM�H����%�}V�ˬ��X�ԔNY����H��^���5�:����Q����!���l
�\�˱A���??Q!g��|�*���UC�ڠJ#�{�Ë��L�6ߧ�61zf�rnb�7g971b�4�Q�Y�ćM��31s(f���W��k�AH���r�:m���%9�����\�׌�r%��M,��.cnE�r��g�Y�z��
G��l�B����}oi~�v²�3.���JN�.��<z�0��*b��7י��7Ɉ/���D��g ��GV��hVM��V{(紘�#����PK�vG�G1�Z߆�7f�7����M��Z��7��b��`b�E�S9�N��S�����@ļ.u^s��2�7P����*E�=sjtW���U�7f���4&��y���U�y��P��;��E��3曻������S(��T�BFb�m~+�_���|�x��s�^��L�񑩘�Bm~��9O�cͦ5!���-!��n��$�8��V�����zSέf!�W��G�FW����uU�6}���TBt4>�4j����(��2��*x�y+��8m3?��ec��a�m��h��n��Z7V�H�l�n$F��^O�W�1��ٓȴ�#�m�W��$�l4�a4���z4���$mc���&�]�>���_,K$g4ͪ�+Um�[5��X��T0(J��REC ��0���&��+���iB�H��F�X�n!�${jzD�V*�;32���6� Ü�L�q�:w�6� ��V"�-�d�z���t���1���H�֠n5��w���!��������]���g�x���SM��{��Vg��\M-��*\�'�Z��}��|�1�(8T�3���H�0	�9���L�
*(V�C9��<���B�g�̌(gI��B�����+?w*����+�I�ڋ�2#C�H�.}+��+̈��� �E��'��A�#A�>m��D\��{9MX#*�;-S�o��yY�I]J��B|=l��5b$���(똣G�^V�y��%�c��s#k���x��ҞF��J���Q�x;fL0�hT���"��Dk�Vk��5�j��=٨�Kv��R�0ط�[�S.��;b$.��w�w#�	��*�.+Q �6N�A�@�]���B�-��s���������C��K������r���LFv���;��ƲD\4��&q8�r��6��͛��N2�F�n�
@��g�h/' ��a��z�����n���d���B	�2���:�,Cc�q@ ] 4V>���3K�O;��z�[��,�:�(�h6�|��ye;'�٠�|��n:��+.7EgBwF��v[�A�DC/�T�;��a9���	��-���F�Q�'T��7i�ܡ���-2��{���V�~0��B���_�_҇����#���%Gk+J��E�����"�k�1�8�%�Q����+�&ƪ�(]E��T��]".b�}�,G_��$���8w��s�7j�7Gz��ot��Ʊ�n�
 #SS���^u4�NB<h��l�6Z�e��w���j<���5D~,���S������k�W�y.�9�E�"2�)�rB�Ve���[���W�����S���st�a��&6ŧ�&A��d''�]�zQ"qm����x� ��}�[E�����v��Af����Ե%+��OH�T��s�Y�D�{.!K�����Nu9nL�Tgr<�|���25&J>+e���/rɋ�Z3��*�x�[+��>�Ybj���ʹۨ�L�Wc�����>f)h?C� ӝ��v��8$^G�L�f[��n~�x���$�Fv�V��63�X$e���*l�{0�Gg��;��do'�2k�Zu�=4��/G3\�f}�c���}�2˥������A~�,��ĩW"89u�;����1��8���.[�	dr��9�FR��{�ީ���ނA�����l��j��-��D����o@��0�������~����D翈R�?L�������>�>+�ö�����N?��@���`#�=��WTi�?dm�aTM�MQ�%��WP����F���,�~%jO���Y�ǩ��:���nF#Dyp.���vG��rv���o��FZ�]�c3/�*����B'69h�![��_��[�Hy[r�?X聗{Xh�-�D�u��g�ӺeoF�.�W��X�(�Vݦ�\���Y�t�IxO�8F՜��ڋ��?�{��E��nM޷�0/m9��!��״��ZI��j��r���Ly�f�'K	ǘ��"8�º�]*���-��J���	��S��K]i�}�=ގ I��%?�����F��C_�	���E-��L���{?�1O�����!q2��Ϫ�
5���wwe;�#���>F����l���/�\l��a02d��{o0�dVfuW���FWW2�d,w_��4�'F�����������×~�=��[�=Y�L���"˰�j�?)ov����K�x��l��nVa�>���R{|���EQFF��sU3\���L)��3�qG=��1��w��O�9�Z�t�8{��\a����=�zEu\�DS�4?�\�鰒���<���\�XLu���s�ᛜ̖%r�'�����[�i��@�~Y����?�1u�XV���hIcz�+�nD    8@L�;2��,ٳ�(:��x� {k�.�a�?� vM���u���J�Z��)dY��h��2�8��4B�L���V4��٥q�3n��I���%\=J'�(���%�s{H4��b@t�����SX����0���'���u�
|�R)�Cw�%���'��v�M(]ý�ҨQ�9�(��%��؋Dm�J��}'~gP�I�2382͛�F�t�\�Wp6Vk��h���Z���F++Sg�wN��Śe��U���:ً��s+&Ӣ�K4�i��q9\[$����N��It�a(�u��4Mש�I�Q��2�_յ�V���ڞ$+�U�5iR_R8�o�6�!�w�G����SIK�� ������o��i���>}
��Q���<�&�з��TQ�婡k^���֛�� wBޚ&��b\�4I��sX���B�I��Dڷ�t��i�z$#�nR���ݶ�:�c�x�A�:�X���Z�����0V�o�#�%���D}d�1� ��,�Bw��@��٣�w"�'���
䯬�W�ʨ�O�U�������^�P�,�-�GV�_~������o�r(S�r(�m\��o��(�����.��-��O�����)�h��#��rn�\�IuC�I6ƛI�_ac���Ob��k��I9Y�*�E�v�۟nY]�e��pa��hsd<��6��6ymc��JH�Oe.��
����~Ÿ�c�.6����º�z٥�7��7�]��>�� [�Ε����>����~|�t�}YL�S�B)\�3~w֤DJ�h?m��)d�&4qi�V�,��Z����������w;ߙ/�]I��o������K>��}����k4-J3Z��Dg���pGQ2�j��gF�
[u�G�����\�������"'����]�Z`����̮�޶�Q߆���{�����g�"��Z�E���l[k-���pn��w��Ѽ1˾�Vϻ��^|��Zo�ذ�z�J�E��U�m�Xw(\����"-*ȉ�i�����E�u�PB�������/b=���d��M:%��F���ж�N~�����7�-�����e�ӈ�B:�?�ԶE���rlf�4\��h>�ԻL���E���V�ylQw����;��*��2�ƩIk�q˫o��2��գ��(�i߇��S�{H�-��5�u���hyW�\�3\�s}q�۳�2��ŕ�jF)���@������y��O������J����6�6S�J�]�.�J$;�-�H�9�����RހχF�k{Gݝ6�Q��Ẹ�T�iN�K����{r.AQV��~�������Y/��,Y�I��}>K���W��&VW{Zj�=��b猐v�V��)�����-!�y{v��AU[���+p,ᛡjD�qo
���+�K#� �b�	]y�plH�qE��HY_�oC�y�!\�Ф�;��.��L��� �w���q�󻓘o}Cj7rW��1�Vk����	Y�զT��Sq����Ԧ􎉓֕���q[�V��A铦dR�]iDa�#�׊����j�_k_hk3�
N��p1�Z�	�=�����y��Pϫ��S��"l-i:�TfY����)5�S��Nƅ�J)�2��p�|�9��y���]�9��bµ)qҀ�nՌ�S�"t���J���0b�Gd�Ea�N�6� �r�X�sg�10�-�sN����/��v���z/-x|�Q�|(s��sߞWQD�΍o!�'���?=i-��U<��ڐ?(�~���=���ŉw	�[�;S� Vm��{d�q�K<��=8�������??�*9����J����ic���_u@��9�%�yag5�^��閃0�w��:�CL�AV7非�Hb�V�>3�ؔ-
��`�|�r�ٕ7�������I��{��B�g�D5���w�뀓��u�N�o�ɮ|W�JZ,��g�JU�:ɓf0sYժ���G�gz��"��Ϭb����Z�����X�Rhϗת,-�w���Te����{F�O�\Z��s9\�b�E��P��_�������o=��hU�����'�VɽIV���Bg��+YX��IDO�JB���5͒Qii�!��=���Y��*�35�ej�uHLE�g�ȥ$��ΦW�LG����R���NC[y���Ү�[�޲�%۵�G1��B�)��P�5ՏX?G<�U�4flY	�'�jդ>i���[�r�O�_3xT�ڠ��%?ڮY^���V@�f?X5�Εn1)!S��1f��6Mno���{';�nU\c�*q�O���������~����ohW3�ه`Z����	/�W�����ko�ժ�s	X��ӌ�^�S&�<����V�[�|�J�������$ˡGE��ӥ2f7�جI��cF�j�{�:�-mݝU'G�moniy'8O�G02������R���&�q/$���y0n]j|����)i��z$�)ڥ";���i�M`7�'G5��o�i�1<�|�V��;I�`�A����nk��8��Ս��-��f�}���|FyW�[��zn�7�`[5�v�ٺNZ�)���Zd:��Mk���N�`�*�O�*��e��I��J��D>'�#��[�{����^�g��H�6��a��u�����cY��q��^i@�V"e~�ĉͷ���' -�$��Y�����S�d��ʎ~�+;z� ��Hu��y`.ws�pQ뮯*����5�x�u��ۛ/���n��[�o�o��o������-��p{6�!<�͑�y��q6�"�u��x>�8�0ӯwW�xe��M�
O�<��?�W<��dVyrPH�2�a��0��e�Z;e��cX�Bz`E����?�~����_����ۼ��y��O�I�+����/�Ku0�]�gwR���}ֵLY����������ks`1~Q9?%�EE��J#�Pr�u�=�Yʰ���w����n�b�=�Y|�mi��`�?T��!�7�H�w�ђ�Ve�|��G���O����$��rX|���W�����o7ҙ�:?0!�@����t/�:1���*%���t^�J�%�lD��/Ce�d:�x����-F x~=�&�7\ۓc a؋i�׼�2qQ�HjmِX��^�jo3�'p�a�ؤ�!��lyVKk�{��Ě�p�k�0LU&�����j��qG\��j��o)��y�U��!��;}��e�0��o��(�=�G�0�1��[�~E���C�Ұk]q2��L�XB������%D���K/&��ȸ;���&p�P�����+m�=]w!�I
�H���s�F�+���fb�&8v����3׮�������}����>M��_��<�	�&�mj@��*P;�6P�����@a���Bgj��wC����QC������L�P3|��l�o�΂������/~鶶s�6؈Y
<H���/�J��C�L3Zt�0�`���h����T��-3�V<���RB���7ϒ-U+�}�O��	G��RþK���F"MC{�ܝ���LI@7{�������4�o���o)��#�\K���dtٍ2ӑf���9�Q����n?&��k�s�$*<����$�D�����f	w�$���4�+�9���F5k���z,�|�ٳ�,&-���&r�<����]��m@�?LLPT1�ʌr0�����y1Ͱ����1{y6zRC�ƒ�yU��dޛ�V2���a����^�?Jv�=(�c_V� ��6����G�Rp��4w鵨��0�ω}�/����g{ޠ%B�Փ^���}�9����}�v�lMX��!#�uز�}�����lp�vۻix��0��kɢ�`����"�&�0�*��)���\M��3�I��B�f��8~>ҭL9�n��2ٓ��L)�&`)�uīNqR\(,�C��R7��c���
q/i��P'q��1��O����jn�[�+���=�X-Aȉ�ߡ���/q��o�5�8)4MI0zR�*>�0�g�Ip�M��S�V7B�5��TI��aF������˨.�B5�4��ؒvȣ!��d�>C�KxK�x�NZ(&��RcY����A���A������	��j|��\i��|��P4���S# C
  \��#��u��`���:,nRr�\pH6�"����N%>�0TIPn�;W0������;��'��5�a覔�U�70�
M�Oeէ�U�rg���%�ju���K>����B�Mqg0�6X�w�����YܔA�}����ǤT��Vm2�ߨ����Z.�����]j��Y8Ǧ�Y��������e�Z���JO���e��������7z����.Q9��Nͺ�^��Dɐ+�;�+v���F�=��hǐFh�߬��h�H[���%�L�tTKYh��Mc�����*ߣ��3��&T���g`8��dZ{��V`�*�1�5�����.|^���"c�UT����=V��x���Z��X?�]c7�~�*�)�>��ǑG�P���INbߋ6�w�0��[���T8�����A&��d?��{7��B����З�I�0�KY~��`��DU�ig���Vm�6�鲙�O�c����tJ*��D��f.R��5��*7	�{�n���oe>)\Oy9f?�7͘��F#'��	�س��>��ii Fi =c;���Bh]7���=D��nh�����pie�{��u��,�*ш�q+ō����iO���������vu���ymј�h�|��5�Q5�Yƅڪ��p�*wRE�ڠ�9?��0�Y@C\�)��ذ{��-T�9����!�Lw���)Vb!����]��*^�n���vw+ԟ5���ۉ�0��BBӹL��Ϣ�G�s�dN˪OՎ�<W9����:f�s��[�Zv����b=�����i�����L�漦fN��l�98��t���=��{�xf�-�tɮ��䓟�,%�!�e)�Y0��[z�qx� ������+� �������3nw���q�C�ͼ����Z���h}�P�_+t���
?Р���x]L\ԖaR+|$����H���򫨒9���z��i��x�2,eZ��)�}���4��M��_�F����F��C��i�=m��eu��%�4�o|�ǲzoa.㻣/x�&��Y�~Y����^x^�7�+��P�F/��P1�η0��e}����k_|������G)Y�}���8Ϲ�R.��0>��������������?��ƹ�w���2��:ߣ����b�ߎd��iG3�sX�id�~�,x���1�ߣ�VK������	N|<�Ad�K.=�(5}�I�=�W��#�4��J@��-R�+摎O��}�����5���#��z^R� ��V#@xy�9��BX| �y^��_$%Cb\0���ţ��y���RW 8]�u�'��4���Ӿ����_��LTb�݁�s�t>#ߓ$*����f�3�~����XU����(�CB��xֳ�[��o0��X]��O��#R!�a�x�<���΋�0(0�Tp����ﵝ���0p];�x���5�ۧfqa���ZNM� �� X
�L!S)졜(`Bϼ�IBl��8��� �B������j�@�����x�$�n�������ջ�H��oΣW�<Ėȼ��/����0��M|`=phJk��ռ�8'GD&>ZK���و�Ƞ*��媪��g(0�ޡ�����`�k1�	��y&��,����Ȋ~����lU$��98TR�Qef�O/������>�t+'.�>�=l�8��
��q_C^� {u��؁	�Ӧ��D�m���1���p�8�#L���hq��v, �6ӟVM��:@�#��H�1��k����É�z^��8sBP�>�ղ���Z�F�l
}�]��Ϲ���.�Vδp0k^c|��p��p�JZ���wVоcI=3�Ή�_x]d����Ya7ɯ�&g�8<t+w'���s�=8pw��sMkl�$�������>�\�� �+���:�f�{;#��-/ޥ���pz�㕎<�1&n*�,*�͔�Tz��I�����|�ݟ���<�W�UVw|�p�����<��Nw�T�O�����).�S����]�}</�8��p�=��\s�D\���%rmٺ托�A�l�e��<w��*=9�P�1�f�ʞ������3��[�]�}��������N~L��k�+����s�U�Rh��`�cO�^�{k(Rz#񏴺:�Z����}q���0�jL�H}o����.���x���O��h�S֊wGh�Y�#�A8y�}E�PT@$�a`�4��W�������2�MWj�D˂���P����DPG�a��H�&h]&��ܯ֓����h��21����u�B���d/4T��w��Y:皏�!o��A�ꁍ�N��7ҷ���}�V�Q��V/��8
�l4�4�{Ӯ�i\%��)���g��O"��(Q�]��d����V-��&{-�V�HD5���1���T�g��TR�(�<�(ܿH�V�P$^�U��=8��4{�t��D�x�+�w������Kx\�Y��VLf�V�9���,H�ϡ9Mۗ�:$vo�f(�j�Y&�=!�h(\�W5�	Y�I�:�E� {SQ��L�@������*��3�M��WY�l�*�1�n*RO�� /x�N�V=���L�w��ܓ��Z�E��luX�H�B�_��i�x��������x{{�?�O1�      #      x������ � �      %      x������ � �     