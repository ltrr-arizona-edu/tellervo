PGDMP                         x            tellervo    11.9 (Debian 11.9-1.pgdg100+1)    11.9 (Debian 11.9-1.pgdg100+1) �   �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                       false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                       false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                       false            �           1262    53043    tellervo    DATABASE     z   CREATE DATABASE tellervo WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'en_US.UTF-8' LC_CTYPE = 'en_US.UTF-8';
    DROP DATABASE tellervo;
             postgres    false                        2615    53132    cpgdb    SCHEMA        CREATE SCHEMA cpgdb;
    DROP SCHEMA cpgdb;
             postgres    false            �           0    0    SCHEMA cpgdb    ACL     )   GRANT ALL ON SCHEMA cpgdb TO "Webgroup";
                  postgres    false    11                        2615    53133    cpgdbj    SCHEMA        CREATE SCHEMA cpgdbj;
    DROP SCHEMA cpgdbj;
             postgres    false            �           0    0    SCHEMA cpgdbj    ACL     *   GRANT ALL ON SCHEMA cpgdbj TO "Webgroup";
                  postgres    false    12            �           0    0    SCHEMA public    ACL     *   GRANT ALL ON SCHEMA public TO "Webgroup";
                  postgres    false    5                        2615    53044    sqlj    SCHEMA        CREATE SCHEMA sqlj;
    DROP SCHEMA sqlj;
             postgres    false            �           0    0    SCHEMA sqlj    ACL     &   GRANT USAGE ON SCHEMA sqlj TO PUBLIC;
                  postgres    false    8                        3079    53045    pljava 	   EXTENSION     8   CREATE EXTENSION IF NOT EXISTS pljava WITH SCHEMA sqlj;
    DROP EXTENSION pljava;
                  false    8            �           0    0    EXTENSION pljava    COMMENT     _   COMMENT ON EXTENSION pljava IS 'PL/Java procedural language (https://tada.github.io/pljava/)';
                       false    3                        3079    53134    postgis 	   EXTENSION     ;   CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;
    DROP EXTENSION postgis;
                  false            �           0    0    EXTENSION postgis    COMMENT     g   COMMENT ON EXTENSION postgis IS 'PostGIS geometry, geography, and raster spatial types and functions';
                       false    2            �           0    0    FUNCTION box2d_in(cstring)    ACL     y   GRANT ALL ON FUNCTION public.box2d_in(cstring) TO pbrewer;
GRANT ALL ON FUNCTION public.box2d_in(cstring) TO "Webgroup";
            public       postgres    false    1547            �           0    0     FUNCTION box2d_out(public.box2d)    ACL     �   GRANT ALL ON FUNCTION public.box2d_out(public.box2d) TO pbrewer;
GRANT ALL ON FUNCTION public.box2d_out(public.box2d) TO "Webgroup";
            public       postgres    false    1548            �           0    0    FUNCTION box3d_in(cstring)    ACL     y   GRANT ALL ON FUNCTION public.box3d_in(cstring) TO pbrewer;
GRANT ALL ON FUNCTION public.box3d_in(cstring) TO "Webgroup";
            public       postgres    false    1549            �           0    0     FUNCTION box3d_out(public.box3d)    ACL     �   GRANT ALL ON FUNCTION public.box3d_out(public.box3d) TO pbrewer;
GRANT ALL ON FUNCTION public.box3d_out(public.box3d) TO "Webgroup";
            public       postgres    false    1550            '           1247    54135 	   date_prec    TYPE     M   CREATE TYPE public.date_prec AS ENUM (
    'day',
    'month',
    'year'
);
    DROP TYPE public.date_prec;
       public       tellervo    false            *           1247    54142    datingtypeclass    TYPE     P   CREATE TYPE public.datingtypeclass AS ENUM (
    'arbitrary',
    'inferred'
);
 "   DROP TYPE public.datingtypeclass;
       public       postgres    false                        0    0 #   FUNCTION geometry_analyze(internal)    ACL     �   GRANT ALL ON FUNCTION public.geometry_analyze(internal) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_analyze(internal) TO "Webgroup";
            public       postgres    false    1551                       0    0    FUNCTION geometry_in(cstring)    ACL        GRANT ALL ON FUNCTION public.geometry_in(cstring) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_in(cstring) TO "Webgroup";
            public       postgres    false    1552                       0    0 &   FUNCTION geometry_out(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_out(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_out(public.geometry) TO "Webgroup";
            public       postgres    false    451                       0    0     FUNCTION geometry_recv(internal)    ACL     �   GRANT ALL ON FUNCTION public.geometry_recv(internal) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_recv(internal) TO "Webgroup";
            public       postgres    false    1553                       0    0 '   FUNCTION geometry_send(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_send(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_send(public.geometry) TO "Webgroup";
            public       postgres    false    452            -           1247    54149    securityuserandobjectid    TYPE     ^   CREATE TYPE public.securityuserandobjectid AS (
	securityuserid integer,
	objectid integer
);
 *   DROP TYPE public.securityuserandobjectid;
       public       postgres    false            0           1247    54152    securityuseruuidandentityid    TYPE     _   CREATE TYPE public.securityuseruuidandentityid AS (
	securityuserid uuid,
	entityid integer
);
 .   DROP TYPE public.securityuseruuidandentityid;
       public       tellervo    false            3           1247    54155    securityuseruuidandobjectuuid    TYPE     ^   CREATE TYPE public.securityuseruuidandobjectuuid AS (
	securityuserid uuid,
	objectid uuid
);
 0   DROP TYPE public.securityuseruuidandobjectuuid;
       public       tellervo    false                       0    0    FUNCTION spheroid_in(cstring)    ACL        GRANT ALL ON FUNCTION public.spheroid_in(cstring) TO pbrewer;
GRANT ALL ON FUNCTION public.spheroid_in(cstring) TO "Webgroup";
            public       postgres    false    1554                       0    0 &   FUNCTION spheroid_out(public.spheroid)    ACL     �   GRANT ALL ON FUNCTION public.spheroid_out(public.spheroid) TO pbrewer;
GRANT ALL ON FUNCTION public.spheroid_out(public.spheroid) TO "Webgroup";
            public       postgres    false    1555            6           1247    54158    tablefunc_crosstab_11    TYPE       CREATE TYPE public.tablefunc_crosstab_11 AS (
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
       public       postgres    false            9           1247    54161    tablefunc_crosstab_2    TYPE     c   CREATE TYPE public.tablefunc_crosstab_2 AS (
	row_name text,
	category_1 text,
	category_2 text
);
 '   DROP TYPE public.tablefunc_crosstab_2;
       public       postgres    false            <           1247    54164    tablefunc_crosstab_3    TYPE     u   CREATE TYPE public.tablefunc_crosstab_3 AS (
	row_name text,
	category_1 text,
	category_2 text,
	category_3 text
);
 '   DROP TYPE public.tablefunc_crosstab_3;
       public       postgres    false            ?           1247    54167    tablefunc_crosstab_4    TYPE     �   CREATE TYPE public.tablefunc_crosstab_4 AS (
	row_name text,
	category_1 text,
	category_2 text,
	category_3 text,
	category_4 text
);
 '   DROP TYPE public.tablefunc_crosstab_4;
       public       postgres    false            B           1247    54170    tablefunc_crosstab_9    TYPE     �   CREATE TYPE public.tablefunc_crosstab_9 AS (
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
       public       postgres    false            E           1247    54173    typfulltaxonomy    TYPE     K  CREATE TYPE public.typfulltaxonomy AS (
	taxonid integer,
	kingdom character varying(128),
	subkingdom character varying(128),
	phylum character varying(128),
	division character varying(128),
	class character varying(128),
	txorder character varying(128),
	family character varying(128),
	genus character varying(128),
	subgenus character varying(128),
	species character varying(128),
	subspecies character varying(128),
	race character varying(128),
	variety character varying(128),
	subvariety character varying(128),
	form character varying(128),
	subform character varying(128)
);
 "   DROP TYPE public.typfulltaxonomy;
       public       postgres    false            H           1247    54176    typnamenumber    TYPE     Q   CREATE TYPE public.typnamenumber AS (
	name character varying,
	number bigint
);
     DROP TYPE public.typnamenumber;
       public       postgres    false            K           1247    54179    typpermissionset    TYPE     �   CREATE TYPE public.typpermissionset AS (
	denied boolean,
	cancreate boolean,
	canread boolean,
	canupdate boolean,
	candelete boolean,
	decidedby text
);
 #   DROP TYPE public.typpermissionset;
       public       postgres    false            N           1247    54182    typtaxonflat2    TYPE     �   CREATE TYPE public.typtaxonflat2 AS (
	taxonid integer,
	taxonrankid integer,
	taxonname character varying(128),
	taxonrank character varying(30),
	rankorder double precision
);
     DROP TYPE public.typtaxonflat2;
       public       postgres    false            Q           1247    54185    typtaxonrankname    TYPE     v   CREATE TYPE public.typtaxonrankname AS (
	taxonid integer,
	taxonrankid integer,
	taxonname character varying(128)
);
 #   DROP TYPE public.typtaxonrankname;
       public       postgres    false            T           1247    54188    typvmeasurementsearchresult    TYPE       CREATE TYPE public.typvmeasurementsearchresult AS (
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
       public       postgres    false            W           1247    54191    typvmeasurementsummaryinfo    TYPE     �   CREATE TYPE public.typvmeasurementsummaryinfo AS (
	vmeasurementid uuid,
	objectcode text,
	objectcount integer,
	commontaxonname text,
	taxoncount integer
);
 -   DROP TYPE public.typvmeasurementsummaryinfo;
       public       postgres    false                       1255    54192 ,   _fcojc(character varying, character varying)    FUNCTION       CREATE FUNCTION cpgdb._fcojc(character varying, character varying) RETURNS character varying
    LANGUAGE plpgsql IMMUTABLE
    AS $_$
BEGIN
   RETURN ' INNER JOIN tbl' || $1 || ' ON tbl' || $1 || '.' || $1 || 'ID=tbl' || $2 || '.' || $1 || 'ID';
END;
$_$;
 B   DROP FUNCTION cpgdb._fcojc(character varying, character varying);
       cpgdb       postgres    false    11                       0    0 5   FUNCTION _fcojc(character varying, character varying)    ACL     X   GRANT ALL ON FUNCTION cpgdb._fcojc(character varying, character varying) TO "Webgroup";
            cpgdb       postgres    false    1556                       1255    54193 2   _gettaxonfordepth(integer, public.typfulltaxonomy)    FUNCTION     v  CREATE FUNCTION cpgdb._gettaxonfordepth(integer, public.typfulltaxonomy) RETURNS text
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
     WHEN 8 THEN $2.genus
     WHEN 9 THEN $2.subgenus
     WHEN 10 THEN $2.species
     WHEN 11 THEN $2.subspecies
     WHEN 12 THEN $2.race
     WHEN 13 THEN $2.variety
     WHEN 14 THEN $2.subvariety
     WHEN 15 THEN $2.form
     WHEN 16 THEN $2.subform
     ELSE 'invalid'
   END;
$_$;
 H   DROP FUNCTION cpgdb._gettaxonfordepth(integer, public.typfulltaxonomy);
       cpgdb       postgres    false    11    2117                       0    0 &   FUNCTION st_srid(geom public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_srid(geom public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_srid(geom public.geometry) TO "Webgroup";
            public       postgres    false    1557                       1255    54194    uuid_generate_v1mc()    FUNCTION     �   CREATE FUNCTION public.uuid_generate_v1mc() RETURNS uuid
    LANGUAGE c STRICT
    AS '$libdir/uuid-ossp', 'uuid_generate_v1mc';
 +   DROP FUNCTION public.uuid_generate_v1mc();
       public       postgres    false            �            1259    54195 	   tblobject    TABLE     N  CREATE TABLE public.tblobject (
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
       public         postgres    false    1049    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            	           0    0 #   COLUMN tblobject.coveragetemporalid    COMMENT     a   COMMENT ON COLUMN public.tblobject.coveragetemporalid IS 'deprecated. - now allowing free text';
            public       postgres    false    229            
           0    0 -   COLUMN tblobject.coveragetemporalfoundationid    COMMENT     k   COMMENT ON COLUMN public.tblobject.coveragetemporalfoundationid IS 'deprecated. - now allowing free text';
            public       postgres    false    229                       0    0    TABLE tblobject    ACL     3   GRANT ALL ON TABLE public.tblobject TO "Webgroup";
            public       postgres    false    229                       1255    54209 -   _internalfindobjectsanddescendantswhere(text)    FUNCTION     �  CREATE FUNCTION cpgdb._internalfindobjectsanddescendantswhere(whereclause text) RETURNS SETOF public.tblobject
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
       cpgdb       postgres    false    229    11            �            1259    54210    tlkpreadingnote    TABLE     �  CREATE TABLE public.tlkpreadingnote (
    readingnoteid integer NOT NULL,
    note character varying(1000) NOT NULL,
    vocabularyid integer,
    standardisedid character varying(100),
    parentreadingid integer,
    parentvmrelyearreadingnoteid integer,
    CONSTRAINT tlkpreadingnote_check CHECK ((((vocabularyid <> 0) AND (parentreadingid IS NULL) AND (parentvmrelyearreadingnoteid IS NULL)) OR (vocabularyid = 0)))
);
 #   DROP TABLE public.tlkpreadingnote;
       public         postgres    false                       0    0    TABLE tlkpreadingnote    ACL     9   GRANT ALL ON TABLE public.tlkpreadingnote TO "Webgroup";
            public       postgres    false    230                       1255    54217 )   addcustomreadingnote(uuid, integer, text)    FUNCTION     '  CREATE FUNCTION cpgdb.addcustomreadingnote(uuid, integer, text) RETURNS public.tlkpreadingnote
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
       cpgdb       postgres    false    230    11                       1255    54218 /   addreadingnote(uuid, integer, integer, boolean)    FUNCTION     3  CREATE FUNCTION cpgdb.addreadingnote(uuid, integer, integer, boolean) RETURNS void
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
       cpgdb       postgres    false    11                       1255    54219 C   andpermissionsets(public.typpermissionset, public.typpermissionset)    FUNCTION       CREATE FUNCTION cpgdb.andpermissionsets(origset public.typpermissionset, newset public.typpermissionset) RETURNS public.typpermissionset
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
       cpgdb       postgres    false    2123    2123    2123    11                       1255    54220    clearreadingnotes(uuid)    FUNCTION     �  CREATE FUNCTION cpgdb.clearreadingnotes(uuid) RETURNS integer
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
       cpgdb       postgres    false    11                       1255    54221    countSamplesPerObject(text)    FUNCTION     �  CREATE FUNCTION cpgdb."countSamplesPerObject"(objcode text) RETURNS integer
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
       cpgdb       postgres    false    11                       0    0 .   FUNCTION "countSamplesPerObject"(objcode text)    ACL     Q   GRANT ALL ON FUNCTION cpgdb."countSamplesPerObject"(objcode text) TO "Webgroup";
            cpgdb       postgres    false    1558            �            1259    54222    tblvmeasurementmetacache    TABLE     E  CREATE TABLE public.tblvmeasurementmetacache (
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
       public         postgres    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2                       0    0    TABLE tblvmeasurementmetacache    ACL     Z   GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.tblvmeasurementmetacache TO "Webgroup";
            public       postgres    false    231                       1255    54231    createmetacache(uuid)    FUNCTION     N  CREATE FUNCTION cpgdb.createmetacache(uuid) RETURNS public.tblvmeasurementmetacache
    LANGUAGE plpgsql
    AS $_$
DECLARE
   vmid ALIAS FOR $1;
   col_op varchar;
   vmresult tblVMeasurementResult%ROWTYPE;
   ret tblVMeasurementMetaCache%ROWTYPE;
BEGIN

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

   SELECT COUNT(*) INTO ret.ReadingCount
      FROM tblVMeasurementReadingResult WHERE VMeasurementResultID = vmresult.VMeasurementResultID;

   SELECT FIRST(tlkpVMeasurementOp.Name), COUNT(tblVMeasurementGroup.VMeasurementID) 
      INTO col_op, ret.MeasurementCount
      FROM tblVMeasurement 
      INNER JOIN tlkpVMeasurementOp ON tblVMeasurement.VMeasurementOpID = tlkpVMeasurementOp.VMeasurementOpID 
      LEFT JOIN tblVMeasurementGroup ON tblVMeasurement.VMeasurementID = tblVMeasurementGroup.VMeasurementID 
      WHERE tblVMeasurement.VMeasurementID = vmid;

   IF col_op = 'Direct' THEN
      ret.MeasurementCount := 1;
   END IF;

   DELETE FROM tblVMeasurementMetaCache WHERE VMeasurementID = vmid;
   INSERT INTO tblVMeasurementMetaCache(VMeasurementID, StartYear, ReadingCount, MeasurementCount, DatingTypeID)
      VALUES (ret.VMeasurementID, ret.StartYear, ret.ReadingCount, ret.MeasurementCount, ret.datingTypeID);

   DELETE FROM tblVMeasurementResult WHERE VMeasurementResultID = vmresult.VMeasurementResultID;

   DELETE FROM tblVMeasurementDerivedCache WHERE VMeasurementID = vmid;
   INSERT INTO tblVMeasurementDerivedCache(VMeasurementID,MeasurementID) 
      SELECT vmid,Measurement.MeasurementID FROM cpgdb.FindVMParentMeasurements(vmid) Measurement;

   SELECT setsrid(extent(tblelement.locationgeometry)::geometry,4326)
      INTO  ret.vmextent
      FROM  tblelement, tblsample, tblradius, tblMeasurement, tblvmeasurement
      WHERE tblvmeasurement.measurementid=tblmeasurement.measurementid
      AND   tblmeasurement.radiusid=tblradius.radiusid
      AND   tblradius.sampleid=tblsample.sampleid
      AND   tblsample.elementid=tblelement.elementid
      AND   tblvmeasurement.vmeasurementid
            IN (SELECT vMeasurementid
                   FROM  cpgdb.FindVMParents(vmid, true)
                   WHERE col_op='Direct');


   UPDATE tblVMeasurementMetaCache SET vmextent = ret.vmextent WHERE VMeasurementID = ret.VMeasurementID;

   SELECT INTO ret.objectCode, ret.objectCount, ret.CommonTaxonName, ret.taxonCount, ret.prefix 
       s.objectCode,s.objectCount,s.commonTaxonName,s.taxonCount,cpgdb.GetVMeasurementPrefix(vmid) AS prefix
       FROM cpgdb.getVMeasurementSummaryInfo(vmid) AS s;
   UPDATE tblVMeasurementMetaCache SET (objectCode, objectCount, commonTaxonName, taxonCount, prefix) =
       (ret.objectCode, ret.objectCount, ret.CommonTaxonName, ret.taxonCount, ret.prefix)
       WHERE VMeasurementID = vmid;

   RETURN ret;
END;
$_$;
 +   DROP FUNCTION cpgdb.createmetacache(uuid);
       cpgdb       postgres    false    231    11            "           1255    54232 �   createnewvmeasurement(character varying, integer, uuid, character varying, character varying, integer, uuid[], character varying, character varying, date)    FUNCTION     �  CREATE FUNCTION cpgdb.createnewvmeasurement(character varying, integer, uuid, character varying, character varying, integer, uuid[], character varying, character varying, date) RETURNS uuid
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
       cpgdb       tellervo    false    11            #           1255    54233 �   createnewvmeasurement(character varying, integer, uuid, character varying, character varying, integer, uuid[], character varying, character varying, character varying, character varying)    FUNCTION     G  CREATE FUNCTION cpgdb.createnewvmeasurement(character varying, integer, uuid, character varying, character varying, integer, uuid[], character varying, character varying, character varying, character varying) RETURNS uuid
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
       cpgdb       tellervo    false    11            $           1255    54234    elementlocationchangedtrigger()    FUNCTION     �  CREATE FUNCTION cpgdb.elementlocationchangedtrigger() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
   vmid tblVMeasurement.VMeasurementID%TYPE;
   newextent geometry;
BEGIN
   IF NEW.locationgeometry = OLD.locationgeometry THEN
      RETURN NEW;
   END IF;

   FOR vmid IN SELECT vmeasurementID FROM cpgdb.FindChildrenOf('Element', NEW.elementID) LOOP
      SELECT setsrid(extent(tblelement.locationgeometry)::geometry,4326)
         INTO  newextent
         FROM  tblelement, tblsample, tblradius, tblMeasurement, tblvmeasurement
         WHERE tblvmeasurement.measurementid=tblmeasurement.measurementid
         AND   tblmeasurement.radiusid=tblradius.radiusid
         AND   tblradius.sampleid=tblsample.sampleid
         AND   tblsample.elementid=tblelement.elementid
         AND   tblvmeasurement.vmeasurementid
            IN (SELECT vMeasurementid
                   FROM  cpgdb.FindVMParents(vmid, true)
                   WHERE op='Direct');

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
       cpgdb       postgres    false    11            %           1255    54235 -   findchildrenof(character varying, anyelement)    FUNCTION     �  CREATE FUNCTION cpgdb.findchildrenof(character varying, anyelement) RETURNS SETOF public.typvmeasurementsearchresult
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
       cpgdb       postgres    false    11    2132            &           1255    54236 "   findchildrenofobjectancestor(uuid)    FUNCTION     �  CREATE FUNCTION cpgdb.findchildrenofobjectancestor(parentid uuid) RETURNS SETOF public.typvmeasurementsearchresult
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
       cpgdb       postgres    false    11    2132            '           1255    54237     findelementobjectancestors(uuid)    FUNCTION     �   CREATE FUNCTION cpgdb.findelementobjectancestors(uuid) RETURNS SETOF public.tblobject
    LANGUAGE sql
    AS $_$
   SELECT * from cpgdb.recurseFindObjectAncestors((SELECT objectId FROM tblElement WHERE elementID=$1), 1)
$_$;
 6   DROP FUNCTION cpgdb.findelementobjectancestors(uuid);
       cpgdb       postgres    false    229    11            (           1255    54238 "   findobjectancestors(uuid, boolean)    FUNCTION     �   CREATE FUNCTION cpgdb.findobjectancestors(uuid, boolean) RETURNS SETOF public.tblobject
    LANGUAGE sql
    AS $_$
  SELECT * FROM cpgdb.recurseFindObjectAncestors($1, (SELECT CASE WHEN $2=TRUE THEN 1 ELSE 0 END))
$_$;
 8   DROP FUNCTION cpgdb.findobjectancestors(uuid, boolean);
       cpgdb       postgres    false    11    229            )           1255    54239 7   findobjectancestorsfromcode(character varying, boolean)    FUNCTION       CREATE FUNCTION cpgdb.findobjectancestorsfromcode(thiscode character varying, includeself boolean) RETURNS SETOF public.tblobject
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
       cpgdb       postgres    false    229    11            *           1255    54240 $   findobjectdescendants(uuid, boolean)    FUNCTION     �   CREATE FUNCTION cpgdb.findobjectdescendants(uuid, boolean) RETURNS SETOF public.tblobject
    LANGUAGE sql
    AS $_$
  SELECT * FROM cpgdb.recurseFindObjectDescendants($1, (SELECT CASE WHEN $2=TRUE THEN -1 ELSE 0 END))
$_$;
 :   DROP FUNCTION cpgdb.findobjectdescendants(uuid, boolean);
       cpgdb       postgres    false    229    11                        1255    54241 9   findobjectdescendantsfromcode(character varying, boolean)    FUNCTION       CREATE FUNCTION cpgdb.findobjectdescendantsfromcode(thiscode character varying, includeself boolean) RETURNS SETOF public.tblobject
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
       cpgdb       postgres    false    229    11            !           1255    54242 $   findobjectsanddescendantswhere(text)    FUNCTION     �   CREATE FUNCTION cpgdb.findobjectsanddescendantswhere(whereclause text) RETURNS SETOF public.tblobject
    LANGUAGE sql
    AS $_$
  SELECT DISTINCT * FROM cpgdb._internalFindObjectsAndDescendantsWhere($1);
$_$;
 F   DROP FUNCTION cpgdb.findobjectsanddescendantswhere(whereclause text);
       cpgdb       postgres    false    11    229            ,           1255    54243     findobjecttoplevelancestor(uuid)    FUNCTION     <  CREATE FUNCTION cpgdb.findobjecttoplevelancestor(objid uuid) RETURNS public.tblobject
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
       cpgdb       postgres    false    229    11            -           1255    54244    findvmchildren(uuid, boolean)    FUNCTION     �   CREATE FUNCTION cpgdb.findvmchildren(uuid, boolean) RETURNS SETOF public.typvmeasurementsearchresult
    LANGUAGE sql
    AS $_$
  SELECT * FROM cpgdb.recurseFindVMChildren($1, (SELECT CASE WHEN $2=TRUE THEN -1 ELSE 0 END))
$_$;
 3   DROP FUNCTION cpgdb.findvmchildren(uuid, boolean);
       cpgdb       postgres    false    11    2132            �            1259    54245    tblmeasurement    TABLE     �  CREATE TABLE public.tblmeasurement (
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
       public         postgres    false                       0    0    TABLE tblmeasurement    ACL     8   GRANT ALL ON TABLE public.tblmeasurement TO "Webgroup";
            public       postgres    false    232            .           1255    54265    findvmparentmeasurements(uuid)    FUNCTION     �  CREATE FUNCTION cpgdb.findvmparentmeasurements(uuid) RETURNS SETOF public.tblmeasurement
    LANGUAGE sql
    AS $_$
  SELECT DISTINCT ON (Measurement.MeasurementID) Measurement.* FROM cpgdb.FindVMParents($1, true) parents 
  INNER JOIN tblVMeasurement vs ON parents.VMeasurementID = vs.VMeasurementID 
  INNER JOIN tblMeasurement Measurement ON Measurement.MeasurementID = vs.MeasurementID 
  WHERE parents.op='Direct';
$_$;
 4   DROP FUNCTION cpgdb.findvmparentmeasurements(uuid);
       cpgdb       postgres    false    11    232            /           1255    54266    findvmparents(uuid, boolean)    FUNCTION     �   CREATE FUNCTION cpgdb.findvmparents(uuid, boolean) RETURNS SETOF public.typvmeasurementsearchresult
    LANGUAGE sql
    AS $_$
  SELECT * FROM cpgdb.recurseFindVMParents($1, (SELECT CASE WHEN $2=TRUE THEN -1 ELSE 0 END))
$_$;
 2   DROP FUNCTION cpgdb.findvmparents(uuid, boolean);
       cpgdb       postgres    false    11    2132            0           1255    54267 -   finishcrossdate(uuid, integer, integer, text)    FUNCTION     �  CREATE FUNCTION cpgdb.finishcrossdate(uuid, integer, integer, text) RETURNS integer
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
       cpgdb       postgres    false    11            1           1255    54268 3   finishcrossdate(uuid, integer, uuid, text, integer)    FUNCTION     �  CREATE FUNCTION cpgdb.finishcrossdate(uuid, integer, uuid, text, integer) RETURNS integer
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
       cpgdb       postgres    false    11            2           1255    54269 *   finishredate(uuid, integer, integer, text)    FUNCTION     �  CREATE FUNCTION cpgdb.finishredate(uuid, integer, integer, text) RETURNS integer
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
       cpgdb       postgres    false    11            3           1255    54270 ,   finishtruncate(uuid, integer, integer, text)    FUNCTION     F  CREATE FUNCTION cpgdb.finishtruncate(uuid, integer, integer, text) RETURNS integer
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
       cpgdb       postgres    false    11            4           1255    54271 !   getdatefromstr(character varying)    FUNCTION       CREATE FUNCTION cpgdb.getdatefromstr(datestr character varying) RETURNS date
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
       cpgdb       tellervo    false    11            5           1255    54272 %   getdateprecfromstr(character varying)    FUNCTION     �  CREATE FUNCTION cpgdb.getdateprecfromstr(datestr character varying) RETURNS public.date_prec
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
       cpgdb       tellervo    false    2087    11            6           1255    54273 %   getdatestring(date, public.date_prec)    FUNCTION     (  CREATE FUNCTION cpgdb.getdatestring(thedate date, thedateprec public.date_prec) RETURNS character varying
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
       cpgdb       tellervo    false    2087    11            �            1259    54274    tblvmeasurementderivedcache    TABLE     �   CREATE TABLE public.tblvmeasurementderivedcache (
    derivedcacheid integer NOT NULL,
    vmeasurementid uuid NOT NULL,
    measurementid integer NOT NULL
);
 /   DROP TABLE public.tblvmeasurementderivedcache;
       public         postgres    false                       0    0 !   TABLE tblvmeasurementderivedcache    COMMENT     �   COMMENT ON TABLE public.tblvmeasurementderivedcache IS 'A non-recursive cache for breaking down vmeasurement derivations. Provides a map from vmeasurementid (one) to measurementid (many) and vice versa. Updated by metacache functions.';
            public       postgres    false    233                       0    0 !   TABLE tblvmeasurementderivedcache    ACL     E   GRANT ALL ON TABLE public.tblvmeasurementderivedcache TO "Webgroup";
            public       postgres    false    233            7           1255    54277    getderivedcache(uuid)    FUNCTION     �   CREATE FUNCTION cpgdb.getderivedcache(uuid) RETURNS SETOF public.tblvmeasurementderivedcache
    LANGUAGE sql
    AS $_$
   SELECT * FROM cpgdb.GetMetaCache($1);
   SELECT * FROM tblVMeasurementDerivedCache WHERE VMeasurementID = $1;
$_$;
 +   DROP FUNCTION cpgdb.getderivedcache(uuid);
       cpgdb       postgres    false    233    11                       0    0 &   FUNCTION geometrytype(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometrytype(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometrytype(public.geometry) TO "Webgroup";
            public       postgres    false    1559            �            1259    54278 
   tblelement    TABLE     �	  CREATE TABLE public.tblelement (
    elementid uuid DEFAULT public.uuid_generate_v1mc() NOT NULL,
    taxonid bigint NOT NULL,
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
    CONSTRAINT check_precisionpositive CHECK ((locationprecision >= (0)::double precision)),
    CONSTRAINT enforce_dims_location CHECK ((public.st_ndims(locationgeometry) = 2)),
    CONSTRAINT enforce_geotype_location CHECK (((public.geometrytype(locationgeometry) = 'POINT'::text) OR (locationgeometry IS NULL))),
    CONSTRAINT enforce_srid_location CHECK ((public.st_srid(locationgeometry) = 4326)),
    CONSTRAINT enforce_units CHECK ((((height IS NULL) AND (units IS NULL)) OR ((height IS NOT NULL) AND (units IS NOT NULL)))),
    CONSTRAINT enforce_valid_dimensions CHECK ((((diameter IS NULL) AND (width IS NULL) AND (depth IS NULL) AND (height IS NULL)) OR ((diameter IS NOT NULL) AND (height IS NOT NULL) AND (depth IS NULL) AND (width IS NULL)) OR ((diameter IS NULL) AND (height IS NOT NULL) AND (width IS NOT NULL) AND (depth IS NOT NULL))))
);
    DROP TABLE public.tblelement;
       public         postgres    false    1049    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2                       0    0 #   COLUMN tblelement.locationprecision    COMMENT     \   COMMENT ON COLUMN public.tblelement.locationprecision IS 'precision of lat/long in meters';
            public       postgres    false    234                       0    0    COLUMN tblelement.description    COMMENT     Z   COMMENT ON COLUMN public.tblelement.description IS 'Other information about the element';
            public       postgres    false    234                       0    0    COLUMN tblelement.processing    COMMENT     a   COMMENT ON COLUMN public.tblelement.processing IS 'Processing (carved, sawn etc) rafting marks';
            public       postgres    false    234                       0    0    COLUMN tblelement.marks    COMMENT     M   COMMENT ON COLUMN public.tblelement.marks IS 'Carpenter marks inscriptions';
            public       postgres    false    234                       0    0     COLUMN tblelement.unsupportedxml    COMMENT     w   COMMENT ON COLUMN public.tblelement.unsupportedxml IS 'Raw XML for fields that are not currently supported in Corina';
            public       postgres    false    234                       0    0    TABLE tblelement    ACL     4   GRANT ALL ON TABLE public.tblelement TO "Webgroup";
            public       postgres    false    234            8           1255    54296 +   getelementsforobjectcode(character varying)    FUNCTION       CREATE FUNCTION cpgdb.getelementsforobjectcode(objectcode character varying) RETURNS SETOF public.tblelement
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
       cpgdb       postgres    false    234    11            �            1259    54297    tblsecuritygroup    TABLE     �   CREATE TABLE public.tblsecuritygroup (
    securitygroupid integer NOT NULL,
    name character varying(31) NOT NULL,
    description character varying(255) NOT NULL,
    isactive boolean DEFAULT true NOT NULL
);
 $   DROP TABLE public.tblsecuritygroup;
       public         postgres    false                       0    0    TABLE tblsecuritygroup    ACL     :   GRANT ALL ON TABLE public.tblsecuritygroup TO "Webgroup";
            public       postgres    false    235                       1255    54301    getgroupmembership(uuid)    FUNCTION     �  CREATE FUNCTION cpgdb.getgroupmembership(uuid) RETURNS SETOF public.tblsecuritygroup
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
       cpgdb       postgres    false    235    11                       0    0 !   FUNCTION getgroupmembership(uuid)    ACL     D   GRANT ALL ON FUNCTION cpgdb.getgroupmembership(uuid) TO "Webgroup";
            cpgdb       postgres    false    1560            <           1255    54302    getgroupmembershiparray(uuid)    FUNCTION     �   CREATE FUNCTION cpgdb.getgroupmembershiparray(uuid) RETURNS integer[]
    LANGUAGE sql IMMUTABLE
    AS $_$
   SELECT ARRAY(SELECT DISTINCT SecurityGroupID FROM cpgdb.GetGroupMembership($1) ORDER BY SecurityGroupID ASC);
$_$;
 3   DROP FUNCTION cpgdb.getgroupmembershiparray(uuid);
       cpgdb       postgres    false    11                       0    0 &   FUNCTION getgroupmembershiparray(uuid)    ACL     I   GRANT ALL ON FUNCTION cpgdb.getgroupmembershiparray(uuid) TO "Webgroup";
            cpgdb       postgres    false    1084                       1255    54303 :   getgrouppermissions(integer[], character varying, integer)    FUNCTION     �  CREATE FUNCTION cpgdb.getgrouppermissions(integer[], character varying, integer) RETURNS character varying[]
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
       cpgdb       postgres    false    11                       0    0 C   FUNCTION getgrouppermissions(integer[], character varying, integer)    ACL     f   GRANT ALL ON FUNCTION cpgdb.getgrouppermissions(integer[], character varying, integer) TO "Webgroup";
            cpgdb       postgres    false    1561            >           1255    54304 <   getgrouppermissionset(integer[], character varying, integer)    FUNCTION     �  CREATE FUNCTION cpgdb.getgrouppermissionset(_groupids integer[], _permtype character varying, _pid integer) RETURNS public.typpermissionset
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
       cpgdb       postgres    false    2123    11            ?           1255    54305 9   getgrouppermissionset(integer[], character varying, uuid)    FUNCTION       CREATE FUNCTION cpgdb.getgrouppermissionset(_groupids integer[], _permtype character varying, _pid uuid) RETURNS public.typpermissionset
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
       cpgdb       postgres    false    11    2123            @           1255    54306    getlabel(text, uuid, boolean)    FUNCTION     i  CREATE FUNCTION cpgdb.getlabel(text, uuid, boolean) RETURNS text
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
       cpgdb       postgres    false    11            �            1259    54307    tblloan    TABLE     i  CREATE TABLE public.tblloan (
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
       public         tellervo    false            A           1255    54314    getloanfromboxid(uuid)    FUNCTION       CREATE FUNCTION cpgdb.getloanfromboxid(boxid uuid) RETURNS public.tblloan
    LANGUAGE sql
    AS $_$
SELECT * from tblloan where loanid=(SELECT loanid FROM tblcurationevent WHERE boxid=$1 AND loanid IS NOT NULL ORDER BY createdtimestamp DESC LIMIT 1);
$_$;
 2   DROP FUNCTION cpgdb.getloanfromboxid(boxid uuid);
       cpgdb       tellervo    false    11    236            B           1255    54315    getmetacache(uuid)    FUNCTION     �  CREATE FUNCTION cpgdb.getmetacache(uuid) RETURNS public.tblvmeasurementmetacache
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
       cpgdb       postgres    false    231    11            C           1255    54316    getnote(text, text)    FUNCTION     �  CREATE FUNCTION cpgdb.getnote(text, text) RETURNS public.tlkpreadingnote
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
       cpgdb       postgres    false    11    230            �            1259    54317 	   tblradius    TABLE     �  CREATE TABLE public.tblradius (
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
       public         postgres    false    1049                       0    0    TABLE tblradius    ACL     3   GRANT ALL ON TABLE public.tblradius TO "Webgroup";
            public       postgres    false    237            D           1255    54330 (   getradiiforobjectcode(character varying)    FUNCTION     �  CREATE FUNCTION cpgdb.getradiiforobjectcode(objectcode character varying) RETURNS SETOF public.tblradius
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
       cpgdb       postgres    false    237    11            E           1255    54331    getrequestxml(integer)    FUNCTION     �   CREATE FUNCTION cpgdb.getrequestxml(requestid integer) RETURNS character varying
    LANGUAGE plpgsql
    AS $_$DECLARE

xml varchar;

BEGIN

SELECT request into xml from tblrequestlog where requestlogid=$1;
RETURN xml;
END;$_$;
 6   DROP FUNCTION cpgdb.getrequestxml(requestid integer);
       cpgdb       postgres    false    11            �            1259    54332 	   tblsample    TABLE     �  CREATE TABLE public.tblsample (
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
    CONSTRAINT enforce_samplingyearwithmonth CHECK ((((samplingmonth IS NOT NULL) AND (samplingyear IS NOT NULL)) OR (samplingmonth IS NULL))),
    CONSTRAINT enforce_tblsample_samplemonthbound CHECK (((samplingmonth >= 1) AND (samplingmonth <= 12))),
    CONSTRAINT enforce_tblsample_sampleyearbound CHECK ((samplingyear > 1900)),
    CONSTRAINT enforce_tblsample_sampleyearbound2 CHECK (((samplingyear)::double precision <= date_part('year'::text, now())))
);
    DROP TABLE public.tblsample;
       public         postgres    false    1049    2087    2087                       0    0    COLUMN tblsample.sampleid    COMMENT     M   COMMENT ON COLUMN public.tblsample.sampleid IS 'Unique sample indentifier
';
            public       postgres    false    238                       0    0    COLUMN tblsample.samplingdate    COMMENT     [   COMMENT ON COLUMN public.tblsample.samplingdate IS 'Year of dendrochronological sampling';
            public       postgres    false    238                        0    0 !   COLUMN tblsample.identifierdomain    COMMENT     l   COMMENT ON COLUMN public.tblsample.identifierdomain IS 'Domain from which the identifier-value was issued';
            public       postgres    false    238            !           0    0    COLUMN tblsample.file    COMMENT     W   COMMENT ON COLUMN public.tblsample.file IS 'Digital photo or scanned image filenames';
            public       postgres    false    238            "           0    0    COLUMN tblsample."position"    COMMENT     R   COMMENT ON COLUMN public.tblsample."position" IS 'Position of sample in element';
            public       postgres    false    238            #           0    0    COLUMN tblsample.state    COMMENT     �   COMMENT ON COLUMN public.tblsample.state IS 'State of material (dry / wet / conserved / burned / woodworm/ rot / cracks) things that indicate the quality of the measurements';
            public       postgres    false    238            $           0    0    COLUMN tblsample.knots    COMMENT     A   COMMENT ON COLUMN public.tblsample.knots IS 'Presence of knots';
            public       postgres    false    238            %           0    0    COLUMN tblsample.description    COMMENT     W   COMMENT ON COLUMN public.tblsample.description IS 'More information about the sample';
            public       postgres    false    238            &           0    0    TABLE tblsample    ACL     3   GRANT ALL ON TABLE public.tblsample TO "Webgroup";
            public       postgres    false    238            F           1255    54348 *   getsamplesforobjectcode(character varying)    FUNCTION     �  CREATE FUNCTION cpgdb.getsamplesforobjectcode(objectcode character varying) RETURNS SETOF public.tblsample
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
       cpgdb       postgres    false    11    238            9           1255    54349    getsearchresultforid(uuid)    FUNCTION     A  CREATE FUNCTION cpgdb.getsearchresultforid(uuid) RETURNS public.typvmeasurementsearchresult
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
       cpgdb       postgres    false    2132    11            :           1255    54350 &   getusedversionsforconstituents(uuid[])    FUNCTION       CREATE FUNCTION cpgdb.getusedversionsforconstituents(uuid[]) RETURNS SETOF text
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
       cpgdb       postgres    false    11            =           1255    54351 4   getuserpermissions(uuid, character varying, integer)    FUNCTION     2  CREATE FUNCTION cpgdb.getuserpermissions(uuid, character varying, integer) RETURNS character varying[]
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
       cpgdb       postgres    false    11            '           0    0 =   FUNCTION getuserpermissions(uuid, character varying, integer)    ACL     `   GRANT ALL ON FUNCTION cpgdb.getuserpermissions(uuid, character varying, integer) TO "Webgroup";
            cpgdb       postgres    false    1085            G           1255    54352 6   getuserpermissionset(uuid, character varying, integer)    FUNCTION     �  CREATE FUNCTION cpgdb.getuserpermissionset(uuid, character varying, integer) RETURNS public.typpermissionset
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
       cpgdb       postgres    false    11    2123            H           1255    54353 3   getuserpermissionset(uuid, character varying, uuid)    FUNCTION     �  CREATE FUNCTION cpgdb.getuserpermissionset(uuid, character varying, uuid) RETURNS public.typpermissionset
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
       cpgdb       postgres    false    11    2123            I           1255    54354    getvmeasurementlabel(uuid)    FUNCTION     �   CREATE FUNCTION cpgdb.getvmeasurementlabel(uuid) RETURNS text
    LANGUAGE sql STABLE
    AS $_$
   SELECT cpgdb.GetLabel('vmeasurement', $1, false);
$_$;
 0   DROP FUNCTION cpgdb.getvmeasurementlabel(uuid);
       cpgdb       postgres    false    11            J           1255    54355    getvmeasurementprefix(uuid)    FUNCTION     �   CREATE FUNCTION cpgdb.getvmeasurementprefix(uuid) RETURNS text
    LANGUAGE sql STABLE
    AS $_$
   SELECT cpgdb.GetLabel('vmeasurement', $1, true);
$_$;
 1   DROP FUNCTION cpgdb.getvmeasurementprefix(uuid);
       cpgdb       postgres    false    11            �            1259    54356    tblvmeasurementreadingresult    TABLE     X  CREATE TABLE public.tblvmeasurementreadingresult (
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
       public         postgres    false            (           0    0 "   TABLE tblvmeasurementreadingresult    ACL     F   GRANT ALL ON TABLE public.tblvmeasurementreadingresult TO "Webgroup";
            public       postgres    false    239                       1255    54360 /   getvmeasurementreadingresult(character varying)    FUNCTION     �   CREATE FUNCTION cpgdb.getvmeasurementreadingresult(character varying) RETURNS SETOF public.tblvmeasurementreadingresult
    LANGUAGE sql STABLE
    AS $_$SELECT * from tblVMeasurementReadingResult WHERE VMeasurementResultID = $1 ORDER BY relyear$_$;
 E   DROP FUNCTION cpgdb.getvmeasurementreadingresult(character varying);
       cpgdb       postgres    false    11    239            )           0    0 8   FUNCTION getvmeasurementreadingresult(character varying)    ACL     [   GRANT ALL ON FUNCTION cpgdb.getvmeasurementreadingresult(character varying) TO "Webgroup";
            cpgdb       postgres    false    1562            K           1255    54361     getvmeasurementsummaryinfo(uuid)    FUNCTION     �  CREATE FUNCTION cpgdb.getvmeasurementsummaryinfo(uuid) RETURNS public.typvmeasurementsummaryinfo
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
   FOR rec IN SELECT txbasic.*,cpgdb.qryTaxonomy(txbasic.taxonID) as tx FROM
 	(SELECT t.taxonID::integer FROM tblVMeasurementDerivedCache d
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
       cpgdb       postgres    false    2135    11            L           1255    54362 1   infomeasuredsamplecountforsite(character varying)    FUNCTION     �  CREATE FUNCTION cpgdb.infomeasuredsamplecountforsite(sitecode character varying) RETURNS bigint
    LANGUAGE sql
    AS $_$select count(distinct(s.sampleid)) from tblmeasurement m, tblradius r, tblsample s where m.radiusid=r.radiusid and r.sampleid=s.sampleid and s.sampleid in  (select s.sampleid from tblelement e, tblsample s where e.elementid=s.elementid and e.objectid in (select objectid from cpgdb.FindObjectDescendants((select objectid from tblobject where code=$1), true)));$_$;
 P   DROP FUNCTION cpgdb.infomeasuredsamplecountforsite(sitecode character varying);
       cpgdb       postgres    false    11            *           0    0 C   FUNCTION infomeasuredsamplecountforsite(sitecode character varying)    COMMENT     �   COMMENT ON FUNCTION cpgdb.infomeasuredsamplecountforsite(sitecode character varying) IS 'Calculates the total number of samples from this site that have already been measured.  Handles traversal of object hierarchy.';
            cpgdb       postgres    false    1100            M           1255    54363 '   inforingcountforsite(character varying)    FUNCTION     �  CREATE FUNCTION cpgdb.inforingcountforsite(sitecode character varying) RETURNS bigint
    LANGUAGE sql
    AS $_$select count(rd.readingid) from tblreading rd, tblmeasurement m where rd.measurementid=m.measurementid and m.radiusid in ( select r.radiusid from tblradius r where r.sampleid in  (select s.sampleid from tblelement e, tblsample s where e.elementid=s.elementid and e.objectid in (select objectid from cpgdb.FindObjectDescendants((select objectid from tblobject where code=$1), true))));$_$;
 F   DROP FUNCTION cpgdb.inforingcountforsite(sitecode character varying);
       cpgdb       postgres    false    11            +           0    0 9   FUNCTION inforingcountforsite(sitecode character varying)    COMMENT     �   COMMENT ON FUNCTION cpgdb.inforingcountforsite(sitecode character varying) IS 'Calculate the number of rings that have been measured for a site';
            cpgdb       postgres    false    1101            N           1255    54364 "   inforingsperuserbydate(date, date)    FUNCTION     �  CREATE FUNCTION cpgdb.inforingsperuserbydate(startdate date, enddate date) RETURNS SETOF public.typnamenumber
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
       cpgdb       postgres    false    11    2120            ,           0    0 =   FUNCTION inforingsperuserbydate(startdate date, enddate date)    COMMENT     �   COMMENT ON FUNCTION cpgdb.inforingsperuserbydate(startdate date, enddate date) IS 'Scoreboard of users showing number of rings measured between two dates';
            cpgdb       postgres    false    1102            O           1255    54365 )   infosamplecountforsite(character varying)    FUNCTION     B  CREATE FUNCTION cpgdb.infosamplecountforsite(sitecode character varying) RETURNS bigint
    LANGUAGE sql
    AS $_$select count(s.*) from tblelement e, tblsample s where e.elementid=s.elementid and e.objectid in (select objectid from cpgdb.FindObjectDescendants((select objectid from tblobject where code=$1), true));$_$;
 H   DROP FUNCTION cpgdb.infosamplecountforsite(sitecode character varying);
       cpgdb       postgres    false    11            -           0    0 ;   FUNCTION infosamplecountforsite(sitecode character varying)    COMMENT     �   COMMENT ON FUNCTION cpgdb.infosamplecountforsite(sitecode character varying) IS 'Count of samples in the database for a site.  Note that some of these samples may not have associated series etc';
            cpgdb       postgres    false    1103            P           1255    54366 )   infoseriescountforsite(character varying)    FUNCTION     �  CREATE FUNCTION cpgdb.infoseriescountforsite(sitecode character varying) RETURNS bigint
    LANGUAGE sql
    AS $_$select count(m.measurementid) from tblmeasurement m where m.radiusid in ( select r.radiusid from tblradius r where r.sampleid in  (select s.sampleid from tblelement e, tblsample s where e.elementid=s.elementid and e.objectid in (select objectid from cpgdb.FindObjectDescendants((select objectid from tblobject where code=$1), true))));$_$;
 H   DROP FUNCTION cpgdb.infoseriescountforsite(sitecode character varying);
       cpgdb       postgres    false    11            .           0    0 ;   FUNCTION infoseriescountforsite(sitecode character varying)    COMMENT     �   COMMENT ON FUNCTION cpgdb.infoseriescountforsite(sitecode character varying) IS 'Calculated the the number of series that have been measured for a site.';
            cpgdb       postgres    false    1104            Q           1255    54367 #   infoseriesperuserbydate(date, date)    FUNCTION     ?  CREATE FUNCTION cpgdb.infoseriesperuserbydate(startdate date, enddate date) RETURNS SETOF public.typnamenumber
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
       cpgdb       postgres    false    2120    11            /           0    0 >   FUNCTION infoseriesperuserbydate(startdate date, enddate date)    COMMENT     �   COMMENT ON FUNCTION cpgdb.infoseriesperuserbydate(startdate date, enddate date) IS 'Scoreboard of users showing number of series measured by each between two dates';
            cpgdb       postgres    false    1105            R           1255    54368 +   infosumringlengthforsite(character varying)    FUNCTION     �  CREATE FUNCTION cpgdb.infosumringlengthforsite(sitecode character varying) RETURNS bigint
    LANGUAGE sql
    AS $_$select sum(rd.reading) from tblreading rd, tblmeasurement m where rd.measurementid=m.measurementid and m.radiusid in ( select r.radiusid from tblradius r where r.sampleid in  (select s.sampleid from tblelement e, tblsample s where e.elementid=s.elementid and e.objectid in (select objectid from cpgdb.FindObjectDescendants((select objectid from tblobject where code=$1), true))));$_$;
 J   DROP FUNCTION cpgdb.infosumringlengthforsite(sitecode character varying);
       cpgdb       postgres    false    11            0           0    0 =   FUNCTION infosumringlengthforsite(sitecode character varying)    COMMENT     �   COMMENT ON FUNCTION cpgdb.infosumringlengthforsite(sitecode character varying) IS 'Calculate the total length of rings that have been measured for a site.  Result is returned in microns.';
            cpgdb       postgres    false    1106            S           1255    54369 )   infosumringwidthforuserbydate(date, date)    FUNCTION     �  CREATE FUNCTION cpgdb.infosumringwidthforuserbydate(startdate date, enddate date) RETURNS SETOF public.typnamenumber
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
       cpgdb       postgres    false    11    2120            1           0    0 D   FUNCTION infosumringwidthforuserbydate(startdate date, enddate date)    COMMENT     �   COMMENT ON FUNCTION cpgdb.infosumringwidthforuserbydate(startdate date, enddate date) IS 'Scoreboard of users showing total ring lengths measured between two dates';
            cpgdb       postgres    false    1107                       1255    54370    isadmin(integer)    FUNCTION     �   CREATE FUNCTION cpgdb.isadmin(securityuserid integer) RETURNS boolean
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
       cpgdb       postgres    false    11            2           0    0 (   FUNCTION isadmin(securityuserid integer)    ACL     K   GRANT ALL ON FUNCTION cpgdb.isadmin(securityuserid integer) TO "Webgroup";
            cpgdb       postgres    false    1563            T           1255    54371    isadmin(uuid)    FUNCTION     �   CREATE FUNCTION cpgdb.isadmin(securityuserid uuid) RETURNS boolean
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
       cpgdb       tellervo    false    11            U           1255    54372    loanhasoutstandingitems(uuid)    FUNCTION     Q  CREATE FUNCTION cpgdb.loanhasoutstandingitems(theloanid uuid) RETURNS boolean
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
       cpgdb       tellervo    false    11                       1255    54373    lookupenvdata(integer, integer)    FUNCTION     r  CREATE FUNCTION cpgdb.lookupenvdata(theelementid integer, therasterlayerid integer) RETURNS double precision
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
       cpgdb       postgres    false    11            3           0    0 F   FUNCTION lookupenvdata(theelementid integer, therasterlayerid integer)    ACL     i   GRANT ALL ON FUNCTION cpgdb.lookupenvdata(theelementid integer, therasterlayerid integer) TO "Webgroup";
            cpgdb       postgres    false    1564            \           1255    54374    lookupenvdatabylayer(integer)    FUNCTION       CREATE FUNCTION cpgdb.lookupenvdatabylayer(therasterlayerid integer) RETURNS void
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
       cpgdb       postgres    false    11            4           0    0 7   FUNCTION lookupenvdatabylayer(therasterlayerid integer)    ACL     Z   GRANT ALL ON FUNCTION cpgdb.lookupenvdatabylayer(therasterlayerid integer) TO "Webgroup";
            cpgdb       postgres    false    1116            ]           1255    54375    lookupenvdatabytree(integer)    FUNCTION       CREATE FUNCTION cpgdb.lookupenvdatabytree(theelementid integer) RETURNS void
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
       cpgdb       postgres    false    11            5           0    0 2   FUNCTION lookupenvdatabytree(theelementid integer)    ACL     U   GRANT ALL ON FUNCTION cpgdb.lookupenvdatabytree(theelementid integer) TO "Webgroup";
            cpgdb       postgres    false    1117            V           1255    54376    mergeelements(uuid, uuid)    FUNCTION     �.  CREATE FUNCTION cpgdb.mergeelements(goodelementid uuid, badelementid uuid) RETURNS public.tblelement
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
       cpgdb       postgres    false    11    234            W           1255    54378    mergeobjects(uuid, uuid)    FUNCTION     V  CREATE FUNCTION cpgdb.mergeobjects(goodobject uuid, badobject uuid) RETURNS public.tblobject
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
       cpgdb       postgres    false    11    229            ^           1255    54380    mergeradii(uuid, uuid)    FUNCTION     �  CREATE FUNCTION cpgdb.mergeradii(goodradiusid uuid, badradiusid uuid) RETURNS public.tblradius
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
       cpgdb       postgres    false    11    237            6           0    0 8   FUNCTION mergeradii(goodradiusid uuid, badradiusid uuid)    COMMENT     >  COMMENT ON FUNCTION cpgdb.mergeradii(goodradiusid uuid, badradiusid uuid) IS 'This function merges a ''bad'' radius with a ''good'' radius.  References to the bad radius are changed to point to the new radius, any discrepancies between fields are marked in the comments field and then the ''bad'' radius is deleted.';
            cpgdb       postgres    false    1118            _           1255    54381    mergesamples(uuid, uuid)    FUNCTION     �  CREATE FUNCTION cpgdb.mergesamples(goodsample uuid, badsample uuid) RETURNS public.tblsample
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
       cpgdb       postgres    false    11    238            `           1255    54382    mpsobject(integer)    FUNCTION     �  CREATE FUNCTION cpgdb.mpsobject(securityuserid integer) RETURNS SETOF public.tblobject
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
       cpgdb       postgres    false    11    229            a           1255    54383    mpstree(integer)    FUNCTION     �  CREATE FUNCTION cpgdb.mpstree(securityuserid integer) RETURNS SETOF public.tblelement
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
       cpgdb       postgres    false    234    11            b           1255    54384 !   mpsvmeasurementmetacache(integer)    FUNCTION     �  CREATE FUNCTION cpgdb.mpsvmeasurementmetacache(securityuserid integer) RETURNS SETOF public.tblvmeasurementmetacache
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
       cpgdb       postgres    false    11    231            c           1255    54385    objectregionmodified()    FUNCTION     �   CREATE FUNCTION cpgdb.objectregionmodified() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN

PERFORM cpgdb.update_objectregions(NEW.regionid, 'region');
RETURN NEW;


END;$$;
 ,   DROP FUNCTION cpgdb.objectregionmodified();
       cpgdb       postgres    false    11            d           1255    54386    populate_cache()    FUNCTION       CREATE FUNCTION cpgdb.populate_cache() RETURNS text
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
       cpgdb       postgres    false    11            e           1255    54387    populate_cache_gaps()    FUNCTION     �   CREATE FUNCTION cpgdb.populate_cache_gaps() RETURNS text
    LANGUAGE sql
    AS $$
   SELECT * from cpgdb.populate_cache_gaps(-1);
$$;
 +   DROP FUNCTION cpgdb.populate_cache_gaps();
       cpgdb       postgres    false    11            X           1255    54388    populate_cache_gaps(integer)    FUNCTION     �  CREATE FUNCTION cpgdb.populate_cache_gaps(maxcnt integer) RETURNS text
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
       cpgdb       postgres    false    11            Y           1255    54389    purgecachetables()    FUNCTION     �   CREATE FUNCTION cpgdb.purgecachetables() RETURNS void
    LANGUAGE plpgsql
    AS $$BEGIN
DELETE FROM tblvmeasurementresult;
DELETE FROM tblvmeasurementmetacache;
END;$$;
 (   DROP FUNCTION cpgdb.purgecachetables();
       cpgdb       postgres    false    11            Z           1255    54390 .   qappvmeasurementresultreadingopsum(uuid, uuid)    FUNCTION     �
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
       cpgdb       postgres    false    11                       1255    54391    qrytaxonflat1(integer)    FUNCTION     5  CREATE FUNCTION cpgdb.qrytaxonflat1(taxonid integer) RETURNS SETOF public.typtaxonrankname
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
 4   DROP FUNCTION cpgdb.qrytaxonflat1(taxonid integer);
       cpgdb       postgres    false    11    2129            7           0    0 '   FUNCTION qrytaxonflat1(taxonid integer)    ACL     J   GRANT ALL ON FUNCTION cpgdb.qrytaxonflat1(taxonid integer) TO "Webgroup";
            cpgdb       postgres    false    1565            n           1255    54392    qrytaxonflat2(integer)    FUNCTION     �  CREATE FUNCTION cpgdb.qrytaxonflat2(taxonid integer) RETURNS SETOF public.typtaxonflat2
    LANGUAGE sql
    AS $_$SELECT qrytaxonflat1.taxonid, 
qrytaxonflat1.taxonrankid, 
qrytaxonflat1.taxonname, 
tlkptaxonrank.taxonrank, 
tlkptaxonrank.rankorder
FROM cpgdb.qrytaxonflat1($1) qrytaxonflat1 
RIGHT JOIN tlkptaxonrank ON qrytaxonflat1.taxonrankid = tlkptaxonrank.taxonrankid 
ORDER BY tlkptaxonrank.rankorder ASC;$_$;
 4   DROP FUNCTION cpgdb.qrytaxonflat2(taxonid integer);
       cpgdb       postgres    false    11    2126            8           0    0 '   FUNCTION qrytaxonflat2(taxonid integer)    ACL     J   GRANT ALL ON FUNCTION cpgdb.qrytaxonflat2(taxonid integer) TO "Webgroup";
            cpgdb       postgres    false    1134            o           1255    54393    qrytaxonomy(integer)    FUNCTION     P  CREATE FUNCTION cpgdb.qrytaxonomy(taxonid integer) RETURNS public.typfulltaxonomy
    LANGUAGE sql
    AS $_$
SELECT * FROM crosstab(
'select qrytaxonflat2.taxonid, qrytaxonflat2.taxonrank, qrytaxonflat2.taxonname 
from cpgdb.qrytaxonflat2('||$1||') 
order by 1
', 
'select taxonrank
from tlkptaxonrank 
order by rankorder asc'
) 
as 
(taxonid int, 
kingdom text, 
subkingdom text, 
phylum text, 
division text, 
class text, 
txorder text, 
family text, 
genus text, 
subgenus text,
species text, 
subspecies text, 
race text, 
variety text, 
subvariety text, 
form text, 
subform text);$_$;
 2   DROP FUNCTION cpgdb.qrytaxonomy(taxonid integer);
       cpgdb       postgres    false    11    2117            9           0    0 %   FUNCTION qrytaxonomy(taxonid integer)    COMMENT     �   COMMENT ON FUNCTION cpgdb.qrytaxonomy(taxonid integer) IS 'This is a cross tab query that builds on qrytaxonflat1 and 2 to flatten out the entire taxonomic tree for a given taxonid. ';
            cpgdb       postgres    false    1135            :           0    0 %   FUNCTION qrytaxonomy(taxonid integer)    ACL     H   GRANT ALL ON FUNCTION cpgdb.qrytaxonomy(taxonid integer) TO "Webgroup";
            cpgdb       postgres    false    1135            f           1255    54394     qupdvmeasurementresultinfo(uuid)    FUNCTION     �  CREATE FUNCTION cpgdb.qupdvmeasurementresultinfo(uuid) RETURNS boolean
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
       cpgdb       postgres    false    11            g           1255    54395    rebuildmetacache()    FUNCTION       CREATE FUNCTION cpgdb.rebuildmetacache() RETURNS void
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
       cpgdb       postgres    false    11            h           1255    54396    rebuildmetacacheforelement()    FUNCTION       CREATE FUNCTION cpgdb.rebuildmetacacheforelement() RETURNS trigger
    LANGUAGE plpgsql
    AS $$DECLARE
  uid uuid;
BEGIN
FOR uid IN SELECT vmeasurementid FROM cpgdb.findchildrenof('Element', NEW.elementid)
  LOOP
    PERFORM cpgdb.createmetacache(uid);
  END LOOP;
RETURN new;
END;$$;
 2   DROP FUNCTION cpgdb.rebuildmetacacheforelement();
       cpgdb       postgres    false    11            i           1255    54397    rebuildmetacacheforobject()    FUNCTION       CREATE FUNCTION cpgdb.rebuildmetacacheforobject() RETURNS trigger
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
       cpgdb       postgres    false    11            j           1255    54398    rebuildmetacacheforradius()    FUNCTION       CREATE FUNCTION cpgdb.rebuildmetacacheforradius() RETURNS trigger
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
       cpgdb       postgres    false    11            k           1255    54399    rebuildmetacacheforsample()    FUNCTION       CREATE FUNCTION cpgdb.rebuildmetacacheforsample() RETURNS trigger
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
       cpgdb       postgres    false    11            l           1255    54400 )   recursefindobjectancestors(uuid, integer)    FUNCTION     �  CREATE FUNCTION cpgdb.recursefindobjectancestors(objid uuid, _recursionlevel integer) RETURNS SETOF public.tblobject
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
       cpgdb       postgres    false    229    11            m           1255    54401 +   recursefindobjectdescendants(uuid, integer)    FUNCTION     �  CREATE FUNCTION cpgdb.recursefindobjectdescendants(objid uuid, _recursionlevel integer) RETURNS SETOF public.tblobject
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
       cpgdb       postgres    false    229    11            p           1255    54402 $   recursefindvmchildren(uuid, integer)    FUNCTION     �  CREATE FUNCTION cpgdb.recursefindvmchildren(uuid, integer) RETURNS SETOF public.typvmeasurementsearchresult
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
       cpgdb       postgres    false    11    2132            q           1255    54403 #   recursefindvmparents(uuid, integer)    FUNCTION     �  CREATE FUNCTION cpgdb.recursefindvmparents(uuid, integer) RETURNS SETOF public.typvmeasurementsearchresult
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
       cpgdb       postgres    false    11    2132                       1255    54404 (   recursegetparentgroups(integer, integer)    FUNCTION     T  CREATE FUNCTION cpgdb.recursegetparentgroups(integer, integer) RETURNS SETOF public.tblsecuritygroup
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
       cpgdb       postgres    false    235    11            ;           0    0 1   FUNCTION recursegetparentgroups(integer, integer)    ACL     T   GRANT ALL ON FUNCTION cpgdb.recursegetparentgroups(integer, integer) TO "Webgroup";
            cpgdb       postgres    false    1566            r           1255    54405 )   removereadingnote(uuid, integer, integer)    FUNCTION     �  CREATE FUNCTION cpgdb.removereadingnote(uuid, integer, integer) RETURNS integer
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
       cpgdb       postgres    false    11            s           1255    54406 '   resultnotestojson(integer[], integer[])    FUNCTION     �  CREATE FUNCTION cpgdb.resultnotestojson(integer[], integer[]) RETURNS text
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
       cpgdb       postgres    false    11            t           1255    54407    text_to_uuid(text)    FUNCTION     k   CREATE FUNCTION cpgdb.text_to_uuid(text) RETURNS uuid
    LANGUAGE sql
    AS $_$
   SELECT $1::uuid;
$_$;
 (   DROP FUNCTION cpgdb.text_to_uuid(text);
       cpgdb       postgres    false    11            u           1255    54408    tloid(public.tblelement)    FUNCTION     �   CREATE FUNCTION cpgdb.tloid(public.tblelement) RETURNS uuid
    LANGUAGE sql
    AS $_$
select objectid from cpgdb.findobjecttoplevelancestor($1.objectid);
$_$;
 .   DROP FUNCTION cpgdb.tloid(public.tblelement);
       cpgdb       postgres    false    11    234            ;           1255    54409    update_allobjectextents()    FUNCTION     W  CREATE FUNCTION cpgdb.update_allobjectextents() RETURNS boolean
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
       cpgdb       postgres    false    11            w           1255    54410    update_allobjectregions()    FUNCTION     t  CREATE FUNCTION cpgdb.update_allobjectregions() RETURNS boolean
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
       cpgdb       postgres    false    11            "           1255    54411 $   update_objectextentbyobject(integer)    FUNCTION     D  CREATE FUNCTION cpgdb.update_objectextentbyobject(theobjectid integer) RETURNS public.geometry
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
       cpgdb       postgres    false    2    2    2    2    2    2    2    2    11            <           0    0 9   FUNCTION update_objectextentbyobject(theobjectid integer)    ACL     \   GRANT ALL ON FUNCTION cpgdb.update_objectextentbyobject(theobjectid integer) TO "Webgroup";
            cpgdb       postgres    false    1570            }           1255    54412 '   update_objectextentbysubobject(integer)    FUNCTION     �  CREATE FUNCTION cpgdb.update_objectextentbysubobject(subobjectid integer) RETURNS public.geometry
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
       cpgdb       postgres    false    11    2    2    2    2    2    2    2    2            =           0    0 <   FUNCTION update_objectextentbysubobject(subobjectid integer)    ACL     _   GRANT ALL ON FUNCTION cpgdb.update_objectextentbysubobject(subobjectid integer) TO "Webgroup";
            cpgdb       postgres    false    1149            x           1255    54413 0   update_objectregions(integer, character varying)    FUNCTION     �  CREATE FUNCTION cpgdb.update_objectregions(pkeyid integer, type character varying) RETURNS boolean
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
       cpgdb       postgres    false    11            y           1255    54414    updatelocs()    FUNCTION     �  CREATE FUNCTION cpgdb.updatelocs() RETURNS void
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
       cpgdb       postgres    false    11            z           1255    54415 '   verifysumsarecontiguousanddated(uuid[])    FUNCTION     �  CREATE FUNCTION cpgdb.verifysumsarecontiguousanddated(uuid[]) RETURNS boolean
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
       cpgdb       postgres    false    11            ~           1255    54416 "   vmeasurementmodifiedcachetrigger()    FUNCTION     I  CREATE FUNCTION cpgdb.vmeasurementmodifiedcachetrigger() RETURNS trigger
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
       cpgdb       postgres    false    11            >           0    0 +   FUNCTION vmeasurementmodifiedcachetrigger()    ACL     N   GRANT ALL ON FUNCTION cpgdb.vmeasurementmodifiedcachetrigger() TO "Webgroup";
            cpgdb       postgres    false    1150            {           1255    54417    vmeasurementmodifiedtrigger()    FUNCTION     �  CREATE FUNCTION cpgdb.vmeasurementmodifiedtrigger() RETURNS trigger
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
       cpgdb       postgres    false    11            |           1255    54418     vmeasurementrelyearnotetrigger()    FUNCTION     z  CREATE FUNCTION cpgdb.vmeasurementrelyearnotetrigger() RETURNS trigger
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
       cpgdb       postgres    false    11                       1255    54419 0   applyderivedreadingnotes(uuid, uuid, uuid, uuid)    FUNCTION     F  CREATE FUNCTION cpgdbj.applyderivedreadingnotes(uuid, uuid, uuid, uuid) RETURNS void
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
       cpgdbj       postgres    false    12            #           1255    54420 #   qacqvmeasurementreadingresult(uuid)    FUNCTION     <  CREATE FUNCTION cpgdbj.qacqvmeasurementreadingresult(paramcurrentvmeasurementresultid uuid, OUT relyear integer, OUT reading integer) RETURNS SETOF record
    LANGUAGE sql STABLE
    AS $_$

  SELECT RelYear, Reading from tblVMeasurementReadingResult 
   WHERE VMeasurementResultID=$1 
   ORDER BY RelYear ASC

$_$;
 �   DROP FUNCTION cpgdbj.qacqvmeasurementreadingresult(paramcurrentvmeasurementresultid uuid, OUT relyear integer, OUT reading integer);
       cpgdbj       postgres    false    12            ?           0    0 w   FUNCTION qacqvmeasurementreadingresult(paramcurrentvmeasurementresultid uuid, OUT relyear integer, OUT reading integer)    ACL     �   GRANT ALL ON FUNCTION cpgdbj.qacqvmeasurementreadingresult(paramcurrentvmeasurementresultid uuid, OUT relyear integer, OUT reading integer) TO "Webgroup";
            cpgdbj       postgres    false    1571            �           1255    54421 '   qappvmeasurementreadingnoteresult(uuid)    FUNCTION     �  CREATE FUNCTION cpgdbj.qappvmeasurementreadingnoteresult(paramvmeasurementresultid uuid) RETURNS void
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
       cpgdbj       postgres    false    12            $           1255    54422 ,   qappvmeasurementreadingresult(uuid, integer)    FUNCTION     �  CREATE FUNCTION cpgdbj.qappvmeasurementreadingresult(paramvmeasurementresultid uuid, parammeasurementid integer) RETURNS void
    LANGUAGE sql
    AS $_$

  INSERT INTO tblVMeasurementReadingResult ( RelYear, Reading, VMeasurementResultID, ReadingID, ewwidth, lwwidth ) 
  SELECT tblReading.RelYear, tblReading.Reading, $1 AS Expr1, tblReading.readingID, tblReading.ewwidth, tblReading.lwwidth
   FROM tblReading 
   WHERE tblReading.MeasurementID=$2

$_$;
 p   DROP FUNCTION cpgdbj.qappvmeasurementreadingresult(paramvmeasurementresultid uuid, parammeasurementid integer);
       cpgdbj       postgres    false    12            @           0    0 b   FUNCTION qappvmeasurementreadingresult(paramvmeasurementresultid uuid, parammeasurementid integer)    ACL     �   GRANT ALL ON FUNCTION cpgdbj.qappvmeasurementreadingresult(paramvmeasurementresultid uuid, parammeasurementid integer) TO "Webgroup";
            cpgdbj       postgres    false    1572            %           1255    54423 @   qappvmeasurementresult(uuid, uuid, uuid, uuid, integer, integer)    FUNCTION       CREATE FUNCTION cpgdbj.qappvmeasurementresult(paramvmeasurementresultid uuid, paramvmeasurementid uuid, paramvmeasurementresultgroupid uuid, paramvmeasurementresultmasterid uuid, owneruserid integer, parammeasurementid integer) RETURNS void
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
       cpgdbj       postgres    false    12            A           0    0 �   FUNCTION qappvmeasurementresult(paramvmeasurementresultid uuid, paramvmeasurementid uuid, paramvmeasurementresultgroupid uuid, paramvmeasurementresultmasterid uuid, owneruserid integer, parammeasurementid integer)    ACL     �   GRANT ALL ON FUNCTION cpgdbj.qappvmeasurementresult(paramvmeasurementresultid uuid, paramvmeasurementid uuid, paramvmeasurementresultgroupid uuid, paramvmeasurementresultmasterid uuid, owneruserid integer, parammeasurementid integer) TO "Webgroup";
            cpgdbj       postgres    false    1573            �           1255    54424 >   qappvmeasurementresultopindex(uuid, uuid, uuid, integer, uuid)    FUNCTION     �  CREATE FUNCTION cpgdbj.qappvmeasurementresultopindex(paramnewvmeasurementresultid uuid, paramvmeasurementid uuid, paramvmeasurementresultmasterid uuid, owneruserid integer, paramcurrentvmeasurementresultid uuid) RETURNS void
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
       cpgdbj       postgres    false    12            B           0    0 �   FUNCTION qappvmeasurementresultopindex(paramnewvmeasurementresultid uuid, paramvmeasurementid uuid, paramvmeasurementresultmasterid uuid, owneruserid integer, paramcurrentvmeasurementresultid uuid)    ACL     �   GRANT ALL ON FUNCTION cpgdbj.qappvmeasurementresultopindex(paramnewvmeasurementresultid uuid, paramvmeasurementid uuid, paramvmeasurementresultmasterid uuid, owneruserid integer, paramcurrentvmeasurementresultid uuid) TO "Webgroup";
            cpgdbj       postgres    false    1153            �           1255    54425 <   qappvmeasurementresultopsum(uuid, uuid, uuid, integer, uuid)    FUNCTION     �  CREATE FUNCTION cpgdbj.qappvmeasurementresultopsum(paramnewvmeasurementresultid uuid, paramvmeasurementid uuid, paramvmeasurementresultmasterid uuid, owneruserid integer, paramvmeasurementresultgroupid uuid) RETURNS void
    LANGUAGE sql
    AS $_$

  INSERT INTO tblVMeasurementResult ( VMeasurementResultID, VMeasurementID, StartYear, DatingTypeID, CreatedTimestamp, LastModifiedTimestamp, VMeasurementResultMasterID, OwnerUserID, Code) 
  SELECT $1 AS Expr1, $2 AS Expr2, Min(r.StartYear) AS MinOfStartYear, Max(r.DatingTypeID) AS MaxOfDatingTypeID, now() AS CreatedTimestamp, now() AS LastModifiedTimestamp, $3 AS Expr5, $4 AS Expr6, 'SUM' AS Code 
   FROM tblVMeasurementResult r 
   WHERE r.VMeasurementResultGroupID=$5

$_$;
 �   DROP FUNCTION cpgdbj.qappvmeasurementresultopsum(paramnewvmeasurementresultid uuid, paramvmeasurementid uuid, paramvmeasurementresultmasterid uuid, owneruserid integer, paramvmeasurementresultgroupid uuid);
       cpgdbj       postgres    false    12            C           0    0 �   FUNCTION qappvmeasurementresultopsum(paramnewvmeasurementresultid uuid, paramvmeasurementid uuid, paramvmeasurementresultmasterid uuid, owneruserid integer, paramvmeasurementresultgroupid uuid)    ACL     �   GRANT ALL ON FUNCTION cpgdbj.qappvmeasurementresultopsum(paramnewvmeasurementresultid uuid, paramvmeasurementid uuid, paramvmeasurementresultmasterid uuid, owneruserid integer, paramvmeasurementresultgroupid uuid) TO "Webgroup";
            cpgdbj       postgres    false    1154            �           1255    54426 .   qappvmeasurementresultreadingopsum(uuid, uuid)    FUNCTION     �   CREATE FUNCTION cpgdbj.qappvmeasurementresultreadingopsum(paramnewvmeasurementresultgroupid uuid, paramnewvmeasurementresultid uuid) RETURNS integer
    LANGUAGE sql
    AS $_$

  SELECT * from cpgdb.qappVMeasurementResultReadingOpSum($1, $2)

$_$;
 �   DROP FUNCTION cpgdbj.qappvmeasurementresultreadingopsum(paramnewvmeasurementresultgroupid uuid, paramnewvmeasurementresultid uuid);
       cpgdbj       postgres    false    12            D           0    0 v   FUNCTION qappvmeasurementresultreadingopsum(paramnewvmeasurementresultgroupid uuid, paramnewvmeasurementresultid uuid)    ACL     �   GRANT ALL ON FUNCTION cpgdbj.qappvmeasurementresultreadingopsum(paramnewvmeasurementresultgroupid uuid, paramnewvmeasurementresultid uuid) TO "Webgroup";
            cpgdbj       postgres    false    1155            �           1255    54427 0   qdelvmeasurementresultremovemasterid(uuid, uuid)    FUNCTION       CREATE FUNCTION cpgdbj.qdelvmeasurementresultremovemasterid(paramvmeasurementresultmasterid uuid, paramvmeasurementresultid uuid) RETURNS void
    LANGUAGE sql
    AS $_$

  DELETE 
   FROM tblVMeasurementResult 
   WHERE VMeasurementResultMasterID=$1 AND VMeasurementResultID<>$2

$_$;
 �   DROP FUNCTION cpgdbj.qdelvmeasurementresultremovemasterid(paramvmeasurementresultmasterid uuid, paramvmeasurementresultid uuid);
       cpgdbj       postgres    false    12            E           0    0 s   FUNCTION qdelvmeasurementresultremovemasterid(paramvmeasurementresultmasterid uuid, paramvmeasurementresultid uuid)    ACL     �   GRANT ALL ON FUNCTION cpgdbj.qdelvmeasurementresultremovemasterid(paramvmeasurementresultmasterid uuid, paramvmeasurementresultid uuid) TO "Webgroup";
            cpgdbj       postgres    false    1156            �           1255    54428    qryvmeasurementmembers(uuid)    FUNCTION     �  CREATE FUNCTION cpgdbj.qryvmeasurementmembers(paramvmeasurementid uuid, OUT vmeasurementid uuid, OUT membervmeasurementid uuid) RETURNS SETOF record
    LANGUAGE sql STABLE
    AS $_$

  SELECT tblVMeasurement.VMeasurementID, tblVMeasurementGroup.MemberVMeasurementID 
   FROM tblVMeasurement 
   INNER JOIN tblVMeasurementGroup 
      ON tblVMeasurement.VMeasurementID = tblVMeasurementGroup.VMeasurementID 
   WHERE tblVMeasurement.VMeasurementID=$1

$_$;
    DROP FUNCTION cpgdbj.qryvmeasurementmembers(paramvmeasurementid uuid, OUT vmeasurementid uuid, OUT membervmeasurementid uuid);
       cpgdbj       postgres    false    12            F           0    0 q   FUNCTION qryvmeasurementmembers(paramvmeasurementid uuid, OUT vmeasurementid uuid, OUT membervmeasurementid uuid)    ACL     �   GRANT ALL ON FUNCTION cpgdbj.qryvmeasurementmembers(paramvmeasurementid uuid, OUT vmeasurementid uuid, OUT membervmeasurementid uuid) TO "Webgroup";
            cpgdbj       postgres    false    1157            �           1255    54429    qryvmeasurementtype(uuid)    FUNCTION     m  CREATE FUNCTION cpgdbj.qryvmeasurementtype(vmeasurementid uuid, OUT vmeasurementid uuid, OUT op text, OUT vmeasurementsingroup bigint, OUT measurementid integer, OUT vmeasurementopparameter integer) RETURNS record
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
       cpgdbj       postgres    false    12            G           0    0 �   FUNCTION qryvmeasurementtype(vmeasurementid uuid, OUT vmeasurementid uuid, OUT op text, OUT vmeasurementsingroup bigint, OUT measurementid integer, OUT vmeasurementopparameter integer)    ACL     �   GRANT ALL ON FUNCTION cpgdbj.qryvmeasurementtype(vmeasurementid uuid, OUT vmeasurementid uuid, OUT op text, OUT vmeasurementsingroup bigint, OUT measurementid integer, OUT vmeasurementopparameter integer) TO "Webgroup";
            cpgdbj       postgres    false    1158            &           1255    54430 /   qupdvmeasurementresultattachgroupid(uuid, uuid)    FUNCTION       CREATE FUNCTION cpgdbj.qupdvmeasurementresultattachgroupid(paramvmeasurementresultgroupid uuid, paramvmeasurementresultid uuid) RETURNS void
    LANGUAGE sql
    AS $_$

  UPDATE tblVMeasurementResult 
   SET VMeasurementResultGroupID=$1 
   WHERE VMeasurementResultID=$2

$_$;
    DROP FUNCTION cpgdbj.qupdvmeasurementresultattachgroupid(paramvmeasurementresultgroupid uuid, paramvmeasurementresultid uuid);
       cpgdbj       postgres    false    12            H           0    0 q   FUNCTION qupdvmeasurementresultattachgroupid(paramvmeasurementresultgroupid uuid, paramvmeasurementresultid uuid)    ACL     �   GRANT ALL ON FUNCTION cpgdbj.qupdvmeasurementresultattachgroupid(paramvmeasurementresultgroupid uuid, paramvmeasurementresultid uuid) TO "Webgroup";
            cpgdbj       postgres    false    1574            v           1255    54431 (   qupdvmeasurementresultcleargroupid(uuid)    FUNCTION     �   CREATE FUNCTION cpgdbj.qupdvmeasurementresultcleargroupid(paramvmeasurementresultgroupid uuid) RETURNS void
    LANGUAGE sql
    AS $_$

  UPDATE tblVMeasurementResult 
   SET VMeasurementResultGroupID=NULL 
   WHERE VMeasurementResultGroupID=$1

$_$;
 ^   DROP FUNCTION cpgdbj.qupdvmeasurementresultcleargroupid(paramvmeasurementresultgroupid uuid);
       cpgdbj       postgres    false    12            I           0    0 P   FUNCTION qupdvmeasurementresultcleargroupid(paramvmeasurementresultgroupid uuid)    ACL     t   GRANT ALL ON FUNCTION cpgdbj.qupdvmeasurementresultcleargroupid(paramvmeasurementresultgroupid uuid) TO "Webgroup";
            cpgdbj       postgres    false    1142            '           1255    54432     qupdvmeasurementresultinfo(uuid)    FUNCTION     �  CREATE FUNCTION cpgdbj.qupdvmeasurementresultinfo(uuid) RETURNS boolean
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
       cpgdbj       postgres    false    12            J           0    0 )   FUNCTION qupdvmeasurementresultinfo(uuid)    ACL     M   GRANT ALL ON FUNCTION cpgdbj.qupdvmeasurementresultinfo(uuid) TO "Webgroup";
            cpgdbj       postgres    false    1575            �           1255    54433 )   qupdvmeasurementresultopclean(uuid, uuid)    FUNCTION       CREATE FUNCTION cpgdbj.qupdvmeasurementresultopclean(paramvmeasurementid uuid, paramcurrentvmeasurementresultid uuid) RETURNS void
    LANGUAGE sql
    AS $_$

  UPDATE tblVMeasurementResult 
   SET VMeasurementID=$1 
   WHERE VMeasurementResultID=$2

$_$;
 u   DROP FUNCTION cpgdbj.qupdvmeasurementresultopclean(paramvmeasurementid uuid, paramcurrentvmeasurementresultid uuid);
       cpgdbj       postgres    false    12            K           0    0 g   FUNCTION qupdvmeasurementresultopclean(paramvmeasurementid uuid, paramcurrentvmeasurementresultid uuid)    ACL     �   GRANT ALL ON FUNCTION cpgdbj.qupdvmeasurementresultopclean(paramvmeasurementid uuid, paramcurrentvmeasurementresultid uuid) TO "Webgroup";
            cpgdbj       postgres    false    1176            �           1255    54434 -   qupdvmeasurementresultopcrossdate(uuid, uuid)    FUNCTION     �  CREATE FUNCTION cpgdbj.qupdvmeasurementresultopcrossdate(paramvmeasurementid uuid, paramcurrentvmeasurementresultid uuid) RETURNS void
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
       cpgdbj       postgres    false    12            L           0    0 k   FUNCTION qupdvmeasurementresultopcrossdate(paramvmeasurementid uuid, paramcurrentvmeasurementresultid uuid)    ACL     �   GRANT ALL ON FUNCTION cpgdbj.qupdvmeasurementresultopcrossdate(paramvmeasurementid uuid, paramcurrentvmeasurementresultid uuid) TO "Webgroup";
            cpgdbj       postgres    false    1177            �           1255    54435 *   qupdvmeasurementresultopredate(uuid, uuid)    FUNCTION     �  CREATE FUNCTION cpgdbj.qupdvmeasurementresultopredate(paramvmeasurementid uuid, paramcurrentvmeasurementresultid uuid) RETURNS void
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
       cpgdbj       postgres    false    12            �           1255    54436 ,   qupdvmeasurementresultoptruncate(uuid, uuid)    FUNCTION     �  CREATE FUNCTION cpgdbj.qupdvmeasurementresultoptruncate(paramvmeasurementid uuid, paramvmeasurementresultid uuid) RETURNS void
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
       cpgdbj       postgres    false    12            M           0    0    FUNCTION addauth(text)    ACL     q   GRANT ALL ON FUNCTION public.addauth(text) TO pbrewer;
GRANT ALL ON FUNCTION public.addauth(text) TO "Webgroup";
            public       postgres    false    1576            �           1255    54437    addbbox(public.geometry)    FUNCTION     �   CREATE FUNCTION public.addbbox(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_addBBOX';
 /   DROP FUNCTION public.addbbox(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54438 *   addpoint(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.addpoint(public.geometry, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_addpoint';
 A   DROP FUNCTION public.addpoint(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54439 3   addpoint(public.geometry, public.geometry, integer)    FUNCTION     �   CREATE FUNCTION public.addpoint(public.geometry, public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_addpoint';
 J   DROP FUNCTION public.addpoint(public.geometry, public.geometry, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54440 �   affine(public.geometry, double precision, double precision, double precision, double precision, double precision, double precision)    FUNCTION     &  CREATE FUNCTION public.affine(public.geometry, double precision, double precision, double precision, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_affine($1,  $2, $3, 0,  $4, $5, 0,  0, 0, 1,  $6, $7, 0)$_$;
 �   DROP FUNCTION public.affine(public.geometry, double precision, double precision, double precision, double precision, double precision, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54441 �   affine(public.geometry, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision)    FUNCTION     l  CREATE FUNCTION public.affine(public.geometry, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_affine';
   DROP FUNCTION public.affine(public.geometry, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54442 !   agg_first(anyelement, anyelement)    FUNCTION     �   CREATE FUNCTION public.agg_first(state anyelement, value anyelement) RETURNS anyelement
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
       public       postgres    false            N           0    0 6   FUNCTION agg_first(state anyelement, value anyelement)    ACL     Z   GRANT ALL ON FUNCTION public.agg_first(state anyelement, value anyelement) TO "Webgroup";
            public       postgres    false    1178            �           1255    54443    area(public.geometry)    FUNCTION     �   CREATE FUNCTION public.area(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'ST_Area';
 ,   DROP FUNCTION public.area(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54444    area2d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.area2d(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'ST_Area';
 .   DROP FUNCTION public.area2d(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54445    arraytorows(anyarray)    FUNCTION     �   CREATE FUNCTION public.arraytorows(arr anyarray) RETURNS SETOF anyelement
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
       public       postgres    false            �           1255    54446    asbinary(public.geometry)    FUNCTION     �   CREATE FUNCTION public.asbinary(public.geometry) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_asBinary';
 0   DROP FUNCTION public.asbinary(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54447    asbinary(public.geometry, text)    FUNCTION     �   CREATE FUNCTION public.asbinary(public.geometry, text) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_asBinary';
 6   DROP FUNCTION public.asbinary(public.geometry, text);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54448    asdf()    FUNCTION     t   CREATE FUNCTION public.asdf() RETURNS record
    LANGUAGE sql
    AS $$ 
SELECT * from tblvmeasurement limit 5;
$$;
    DROP FUNCTION public.asdf();
       public       postgres    false            �           1255    54449    asewkb(public.geometry)    FUNCTION     �   CREATE FUNCTION public.asewkb(public.geometry) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'WKBFromLWGEOM';
 .   DROP FUNCTION public.asewkb(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54450    asewkb(public.geometry, text)    FUNCTION     �   CREATE FUNCTION public.asewkb(public.geometry, text) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'WKBFromLWGEOM';
 4   DROP FUNCTION public.asewkb(public.geometry, text);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54451    asewkt(public.geometry)    FUNCTION     �   CREATE FUNCTION public.asewkt(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_asEWKT';
 .   DROP FUNCTION public.asewkt(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54452    asgml(public.geometry)    FUNCTION     �   CREATE FUNCTION public.asgml(public.geometry) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT _ST_AsGML(2, $1, 15, 0, null, null)$_$;
 -   DROP FUNCTION public.asgml(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            +           1255    54453    asgml(public.geometry, integer)    FUNCTION     �   CREATE FUNCTION public.asgml(public.geometry, integer) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT _ST_AsGML(2, $1, $2, 0, null, null)$_$;
 6   DROP FUNCTION public.asgml(public.geometry, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            [           1255    54454    ashexewkb(public.geometry)    FUNCTION     �   CREATE FUNCTION public.ashexewkb(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_asHEXEWKB';
 1   DROP FUNCTION public.ashexewkb(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54455     ashexewkb(public.geometry, text)    FUNCTION     �   CREATE FUNCTION public.ashexewkb(public.geometry, text) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_asHEXEWKB';
 7   DROP FUNCTION public.ashexewkb(public.geometry, text);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54456    askml(public.geometry)    FUNCTION     �   CREATE FUNCTION public.askml(public.geometry) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_AsKML(ST_Transform($1,4326), 15, null)$_$;
 -   DROP FUNCTION public.askml(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54457    askml(public.geometry, integer)    FUNCTION     �   CREATE FUNCTION public.askml(public.geometry, integer) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_AsKML(ST_transform($1,4326), $2, null)$_$;
 6   DROP FUNCTION public.askml(public.geometry, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54458 (   askml(integer, public.geometry, integer)    FUNCTION     �   CREATE FUNCTION public.askml(integer, public.geometry, integer) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_AsKML(ST_Transform($2,4326), $3, null)$_$;
 ?   DROP FUNCTION public.askml(integer, public.geometry, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54459    assvg(public.geometry)    FUNCTION     �   CREATE FUNCTION public.assvg(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_asSVG';
 -   DROP FUNCTION public.assvg(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54460    assvg(public.geometry, integer)    FUNCTION     �   CREATE FUNCTION public.assvg(public.geometry, integer) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_asSVG';
 6   DROP FUNCTION public.assvg(public.geometry, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54461 (   assvg(public.geometry, integer, integer)    FUNCTION     �   CREATE FUNCTION public.assvg(public.geometry, integer, integer) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_asSVG';
 ?   DROP FUNCTION public.assvg(public.geometry, integer, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54462    astext(public.geometry)    FUNCTION     �   CREATE FUNCTION public.astext(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_asText';
 .   DROP FUNCTION public.astext(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54463 )   azimuth(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.azimuth(public.geometry, public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_azimuth';
 @   DROP FUNCTION public.azimuth(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54464    bdmpolyfromtext(text, integer)    FUNCTION     �  CREATE FUNCTION public.bdmpolyfromtext(text, integer) RETURNS public.geometry
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
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54465    bdpolyfromtext(text, integer)    FUNCTION     2  CREATE FUNCTION public.bdpolyfromtext(text, integer) RETURNS public.geometry
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
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54466    boundary(public.geometry)    FUNCTION     �   CREATE FUNCTION public.boundary(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'boundary';
 0   DROP FUNCTION public.boundary(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            O           0    0    FUNCTION box(public.box3d)    ACL     y   GRANT ALL ON FUNCTION public.box(public.box3d) TO pbrewer;
GRANT ALL ON FUNCTION public.box(public.box3d) TO "Webgroup";
            public       postgres    false    1577            P           0    0    FUNCTION box(public.geometry)    ACL        GRANT ALL ON FUNCTION public.box(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.box(public.geometry) TO "Webgroup";
            public       postgres    false    1578            Q           0    0    FUNCTION box2d(public.box3d)    ACL     }   GRANT ALL ON FUNCTION public.box2d(public.box3d) TO pbrewer;
GRANT ALL ON FUNCTION public.box2d(public.box3d) TO "Webgroup";
            public       postgres    false    1579            R           0    0    FUNCTION box2d(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.box2d(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.box2d(public.geometry) TO "Webgroup";
            public       postgres    false    663            S           0    0    FUNCTION box3d(public.box2d)    ACL     }   GRANT ALL ON FUNCTION public.box3d(public.box2d) TO pbrewer;
GRANT ALL ON FUNCTION public.box3d(public.box2d) TO "Webgroup";
            public       postgres    false    664            T           0    0    FUNCTION box3d(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.box3d(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.box3d(public.geometry) TO "Webgroup";
            public       postgres    false    665            U           0    0 !   FUNCTION box3dtobox(public.box3d)    ACL     �   GRANT ALL ON FUNCTION public.box3dtobox(public.box3d) TO pbrewer;
GRANT ALL ON FUNCTION public.box3dtobox(public.box3d) TO "Webgroup";
            public       postgres    false    666            �           1255    54467 )   buffer(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.buffer(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'buffer';
 @   DROP FUNCTION public.buffer(public.geometry, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54468 2   buffer(public.geometry, double precision, integer)    FUNCTION     �   CREATE FUNCTION public.buffer(public.geometry, double precision, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_Buffer($1, $2, $3)$_$;
 I   DROP FUNCTION public.buffer(public.geometry, double precision, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54469    buildarea(public.geometry)    FUNCTION     �   CREATE FUNCTION public.buildarea(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'ST_BuildArea';
 1   DROP FUNCTION public.buildarea(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            V           0    0    FUNCTION bytea(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.bytea(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.bytea(public.geometry) TO "Webgroup";
            public       postgres    false    1580            �           1255    54470    centroid(public.geometry)    FUNCTION     �   CREATE FUNCTION public.centroid(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'centroid';
 0   DROP FUNCTION public.centroid(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54471    checkGroupIsDeletable()    FUNCTION     �   CREATE FUNCTION public."checkGroupIsDeletable"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
IF OLD.securitygroupid = 1 THEN 
	RAISE EXCEPTION 'The administrator group can not be deleted';
	RETURN NULL;
END IF;
RETURN NEW;
END;$$;
 0   DROP FUNCTION public."checkGroupIsDeletable"();
       public       tellervo    false            -           1255    54472    check_datingerrors()    FUNCTION     �  CREATE FUNCTION public.check_datingerrors() RETURNS trigger
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
       public       postgres    false            W           0    0    FUNCTION check_datingerrors()    ACL     A   GRANT ALL ON FUNCTION public.check_datingerrors() TO "Webgroup";
            public       postgres    false    1581            �           1255    54473 2   check_tblcuration_loanid_is_not_null_when_loaned()    FUNCTION     �  CREATE FUNCTION public.check_tblcuration_loanid_is_not_null_when_loaned() RETURNS trigger
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
       public       tellervo    false            X           0    0    FUNCTION checkauth(text, text)    ACL     �   GRANT ALL ON FUNCTION public.checkauth(text, text) TO pbrewer;
GRANT ALL ON FUNCTION public.checkauth(text, text) TO "Webgroup";
            public       postgres    false    894            Y           0    0 $   FUNCTION checkauth(text, text, text)    ACL     �   GRANT ALL ON FUNCTION public.checkauth(text, text, text) TO pbrewer;
GRANT ALL ON FUNCTION public.checkauth(text, text, text) TO "Webgroup";
            public       postgres    false    1582            Z           0    0    FUNCTION checkauthtrigger()    ACL     {   GRANT ALL ON FUNCTION public.checkauthtrigger() TO pbrewer;
GRANT ALL ON FUNCTION public.checkauthtrigger() TO "Webgroup";
            public       postgres    false    895            �           1255    54474 )   collect(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.collect(public.geometry, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE
    AS '$libdir/postgis-3', 'LWGEOM_collect';
 @   DROP FUNCTION public.collect(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54475 +   combine_bbox(public.box2d, public.geometry)    FUNCTION     �   CREATE FUNCTION public.combine_bbox(public.box2d, public.geometry) RETURNS public.box2d
    LANGUAGE c IMMUTABLE
    AS '$libdir/postgis-3', 'BOX2D_combine';
 B   DROP FUNCTION public.combine_bbox(public.box2d, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54476 +   combine_bbox(public.box3d, public.geometry)    FUNCTION     �   CREATE FUNCTION public.combine_bbox(public.box3d, public.geometry) RETURNS public.box3d
    LANGUAGE c IMMUTABLE
    AS '$libdir/postgis-3', 'BOX3D_combine';
 B   DROP FUNCTION public.combine_bbox(public.box3d, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54477 *   connectby(text, text, text, text, integer)    FUNCTION     �   CREATE FUNCTION public.connectby(text, text, text, text, integer) RETURNS SETOF record
    LANGUAGE c STABLE STRICT
    AS '$libdir/tablefunc', 'connectby_text';
 A   DROP FUNCTION public.connectby(text, text, text, text, integer);
       public       postgres    false            �           1255    54478 0   connectby(text, text, text, text, integer, text)    FUNCTION     �   CREATE FUNCTION public.connectby(text, text, text, text, integer, text) RETURNS SETOF record
    LANGUAGE c STABLE STRICT
    AS '$libdir/tablefunc', 'connectby_text';
 G   DROP FUNCTION public.connectby(text, text, text, text, integer, text);
       public       postgres    false            �           1255    54479 0   connectby(text, text, text, text, text, integer)    FUNCTION     �   CREATE FUNCTION public.connectby(text, text, text, text, text, integer) RETURNS SETOF record
    LANGUAGE c STABLE STRICT
    AS '$libdir/tablefunc', 'connectby_text_serial';
 G   DROP FUNCTION public.connectby(text, text, text, text, text, integer);
       public       postgres    false            �           1255    54480 6   connectby(text, text, text, text, text, integer, text)    FUNCTION     �   CREATE FUNCTION public.connectby(text, text, text, text, text, integer, text) RETURNS SETOF record
    LANGUAGE c STABLE STRICT
    AS '$libdir/tablefunc', 'connectby_text_serial';
 M   DROP FUNCTION public.connectby(text, text, text, text, text, integer, text);
       public       postgres    false            �           1255    54481 *   contains(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.contains(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'contains';
 A   DROP FUNCTION public.contains(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54482    convexhull(public.geometry)    FUNCTION     �   CREATE FUNCTION public.convexhull(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'convexhull';
 2   DROP FUNCTION public.convexhull(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            /           1255    54483 )   create_defaultreadingnotestandardisedid()    FUNCTION     l  CREATE FUNCTION public.create_defaultreadingnotestandardisedid() RETURNS trigger
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
       public       postgres    false            [           0    0 2   FUNCTION create_defaultreadingnotestandardisedid()    ACL     V   GRANT ALL ON FUNCTION public.create_defaultreadingnotestandardisedid() TO "Webgroup";
            public       postgres    false    1583            �           1255    54484 &   create_defaultsecurityrecordforgroup()    FUNCTION     �   CREATE FUNCTION public.create_defaultsecurityrecordforgroup() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
INSERT INTO tblsecuritydefault (securitygroupid, securitypermissionid) values (NEW.securitygroupid, 6);
RETURN NULL;
END;$$;
 =   DROP FUNCTION public.create_defaultsecurityrecordforgroup();
       public       postgres    false            \           0    0 /   FUNCTION create_defaultsecurityrecordforgroup()    ACL     S   GRANT ALL ON FUNCTION public.create_defaultsecurityrecordforgroup() TO "Webgroup";
            public       postgres    false    1228            0           1255    54485 '   create_objectsecurity(integer, integer)    FUNCTION     #  CREATE FUNCTION public.create_objectsecurity(objectid integer, securityuserid integer) RETURNS boolean
    LANGUAGE sql
    AS $_$insert into tblsecurityobject (objectid, securitygroupid, securitypermissionid) values ($1, $2, 2);
insert into tblsecurityobject (objectid, securitygroupid, securitypermissionid) values ($1, $2, 3);
insert into tblsecurityobject (objectid, securitygroupid, securitypermissionid) values ($1, $2, 4);
insert into tblsecurityobject (objectid, securitygroupid, securitypermissionid) values ($1, $2, 5);
select true;$_$;
 V   DROP FUNCTION public.create_objectsecurity(objectid integer, securityuserid integer);
       public       postgres    false            ]           0    0 H   FUNCTION create_objectsecurity(objectid integer, securityuserid integer)    ACL     l   GRANT ALL ON FUNCTION public.create_objectsecurity(objectid integer, securityuserid integer) TO "Webgroup";
            public       postgres    false    1584            �           1255    54486    create_subobjectforobject()    FUNCTION     �   CREATE FUNCTION public.create_subobjectforobject() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
INSERT INTO tblsubobject(name, objectid) VALUES('Main', NEW.objectid);
RETURN NULL;
END;$$;
 2   DROP FUNCTION public.create_subobjectforobject();
       public       postgres    false            ^           0    0 $   FUNCTION create_subobjectforobject()    COMMENT     ?   COMMENT ON FUNCTION public.create_subobjectforobject() IS '
';
            public       postgres    false    1229            _           0    0 $   FUNCTION create_subobjectforobject()    ACL     H   GRANT ALL ON FUNCTION public.create_subobjectforobject() TO "Webgroup";
            public       postgres    false    1229            �           1255    54487 %   create_treesecurity(integer, integer)    FUNCTION       CREATE FUNCTION public.create_treesecurity(elementid integer, securityuserid integer) RETURNS boolean
    LANGUAGE sql
    AS $_$insert into tblsecuritytree (elementid, securitygroupid, securitypermissionid) values ($1, $2, 2);
insert into tblsecuritytree (elementid, securitygroupid, securitypermissionid) values ($1, $2, 3);
insert into tblsecuritytree (elementid, securitygroupid, securitypermissionid) values ($1, $2, 4);
insert into tblsecuritytree (elementid, securitygroupid, securitypermissionid) values ($1, $2, 5);
select true;$_$;
 U   DROP FUNCTION public.create_treesecurity(elementid integer, securityuserid integer);
       public       postgres    false            `           0    0 G   FUNCTION create_treesecurity(elementid integer, securityuserid integer)    ACL     k   GRANT ALL ON FUNCTION public.create_treesecurity(elementid integer, securityuserid integer) TO "Webgroup";
            public       postgres    false    1230            �           1255    54488 -   create_vmeasurementsecurity(integer, integer)    FUNCTION     _  CREATE FUNCTION public.create_vmeasurementsecurity(vmeasurementid integer, securityuserid integer) RETURNS boolean
    LANGUAGE sql
    AS $_$insert into tblsecurityvmeasurement (vmeasurementid, securitygroupid, securitypermissionid) values ($1, $2, 2);
insert into tblsecurityvmeasurement (vmeasurementid, securitygroupid, securitypermissionid) values ($1, $2, 3);
insert into tblsecurityvmeasurement (vmeasurementid, securitygroupid, securitypermissionid) values ($1, $2, 4);
insert into tblsecurityvmeasurement (vmeasurementid, securitygroupid, securitypermissionid) values ($1, $2, 5);
select true;$_$;
 b   DROP FUNCTION public.create_vmeasurementsecurity(vmeasurementid integer, securityuserid integer);
       public       postgres    false            a           0    0 T   FUNCTION create_vmeasurementsecurity(vmeasurementid integer, securityuserid integer)    ACL     x   GRANT ALL ON FUNCTION public.create_vmeasurementsecurity(vmeasurementid integer, securityuserid integer) TO "Webgroup";
            public       postgres    false    1231            �           1255    54489 )   crosses(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.crosses(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'crosses';
 @   DROP FUNCTION public.crosses(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54490    crosstab(text)    FUNCTION     �   CREATE FUNCTION public.crosstab(text) RETURNS SETOF record
    LANGUAGE c STABLE STRICT
    AS '$libdir/tablefunc', 'crosstab';
 %   DROP FUNCTION public.crosstab(text);
       public       postgres    false            �           1255    54491    crosstab(text, integer)    FUNCTION     �   CREATE FUNCTION public.crosstab(text, integer) RETURNS SETOF record
    LANGUAGE c STABLE STRICT
    AS '$libdir/tablefunc', 'crosstab';
 .   DROP FUNCTION public.crosstab(text, integer);
       public       postgres    false            �           1255    54492    crosstab(text, text)    FUNCTION     �   CREATE FUNCTION public.crosstab(text, text) RETURNS SETOF record
    LANGUAGE c STABLE STRICT
    AS '$libdir/tablefunc', 'crosstab_hash';
 +   DROP FUNCTION public.crosstab(text, text);
       public       postgres    false            �           1255    54493    crosstab11(text)    FUNCTION     �   CREATE FUNCTION public.crosstab11(text) RETURNS SETOF public.tablefunc_crosstab_11
    LANGUAGE c STABLE STRICT
    AS '$libdir/tablefunc', 'crosstab';
 '   DROP FUNCTION public.crosstab11(text);
       public       postgres    false    2102            �           1255    54494    crosstab2(text)    FUNCTION     �   CREATE FUNCTION public.crosstab2(text) RETURNS SETOF public.tablefunc_crosstab_2
    LANGUAGE c STABLE STRICT
    AS '$libdir/tablefunc', 'crosstab';
 &   DROP FUNCTION public.crosstab2(text);
       public       postgres    false    2105            �           1255    54495    crosstab3(text)    FUNCTION     �   CREATE FUNCTION public.crosstab3(text) RETURNS SETOF public.tablefunc_crosstab_3
    LANGUAGE c STABLE STRICT
    AS '$libdir/tablefunc', 'crosstab';
 &   DROP FUNCTION public.crosstab3(text);
       public       postgres    false    2108            �           1255    54496    crosstab4(text)    FUNCTION     �   CREATE FUNCTION public.crosstab4(text) RETURNS SETOF public.tablefunc_crosstab_4
    LANGUAGE c STABLE STRICT
    AS '$libdir/tablefunc', 'crosstab';
 &   DROP FUNCTION public.crosstab4(text);
       public       postgres    false    2111            �           1255    54497    crosstab9(text)    FUNCTION     �   CREATE FUNCTION public.crosstab9(text) RETURNS SETOF public.tablefunc_crosstab_9
    LANGUAGE c STABLE STRICT
    AS '$libdir/tablefunc', 'crosstab';
 &   DROP FUNCTION public.crosstab9(text);
       public       postgres    false    2114            �           1255    54498 ,   difference(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.difference(public.geometry, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'ST_Difference';
 C   DROP FUNCTION public.difference(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54499    dimension(public.geometry)    FUNCTION     �   CREATE FUNCTION public.dimension(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_dimension';
 1   DROP FUNCTION public.dimension(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            b           0    0 "   FUNCTION disablelongtransactions()    ACL     �   GRANT ALL ON FUNCTION public.disablelongtransactions() TO pbrewer;
GRANT ALL ON FUNCTION public.disablelongtransactions() TO "Webgroup";
            public       postgres    false    1585            �           1255    54500 *   disjoint(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.disjoint(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'disjoint';
 A   DROP FUNCTION public.disjoint(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54501 *   distance(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.distance(public.geometry, public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'ST_Distance';
 A   DROP FUNCTION public.distance(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54502 1   distance_sphere(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.distance_sphere(public.geometry, public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'LWGEOM_distance_sphere';
 H   DROP FUNCTION public.distance_sphere(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54503 D   distance_spheroid(public.geometry, public.geometry, public.spheroid)    FUNCTION     �   CREATE FUNCTION public.distance_spheroid(public.geometry, public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'LWGEOM_distance_ellipsoid';
 [   DROP FUNCTION public.distance_spheroid(public.geometry, public.geometry, public.spheroid);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54504    dropbbox(public.geometry)    FUNCTION     �   CREATE FUNCTION public.dropbbox(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_dropBBOX';
 0   DROP FUNCTION public.dropbbox(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            c           0    0 X   FUNCTION dropgeometrycolumn(table_name character varying, column_name character varying)    ACL     �   GRANT ALL ON FUNCTION public.dropgeometrycolumn(table_name character varying, column_name character varying) TO pbrewer;
GRANT ALL ON FUNCTION public.dropgeometrycolumn(table_name character varying, column_name character varying) TO "Webgroup";
            public       postgres    false    1586            d           0    0 w   FUNCTION dropgeometrycolumn(schema_name character varying, table_name character varying, column_name character varying)    ACL     3  GRANT ALL ON FUNCTION public.dropgeometrycolumn(schema_name character varying, table_name character varying, column_name character varying) TO pbrewer;
GRANT ALL ON FUNCTION public.dropgeometrycolumn(schema_name character varying, table_name character varying, column_name character varying) TO "Webgroup";
            public       postgres    false    1587            e           0    0 �   FUNCTION dropgeometrycolumn(catalog_name character varying, schema_name character varying, table_name character varying, column_name character varying)    ACL     s  GRANT ALL ON FUNCTION public.dropgeometrycolumn(catalog_name character varying, schema_name character varying, table_name character varying, column_name character varying) TO pbrewer;
GRANT ALL ON FUNCTION public.dropgeometrycolumn(catalog_name character varying, schema_name character varying, table_name character varying, column_name character varying) TO "Webgroup";
            public       postgres    false    1591            f           0    0 8   FUNCTION dropgeometrytable(table_name character varying)    ACL     �   GRANT ALL ON FUNCTION public.dropgeometrytable(table_name character varying) TO pbrewer;
GRANT ALL ON FUNCTION public.dropgeometrytable(table_name character varying) TO "Webgroup";
            public       postgres    false    623            g           0    0 W   FUNCTION dropgeometrytable(schema_name character varying, table_name character varying)    ACL     �   GRANT ALL ON FUNCTION public.dropgeometrytable(schema_name character varying, table_name character varying) TO pbrewer;
GRANT ALL ON FUNCTION public.dropgeometrytable(schema_name character varying, table_name character varying) TO "Webgroup";
            public       postgres    false    624            h           0    0 w   FUNCTION dropgeometrytable(catalog_name character varying, schema_name character varying, table_name character varying)    ACL     3  GRANT ALL ON FUNCTION public.dropgeometrytable(catalog_name character varying, schema_name character varying, table_name character varying) TO pbrewer;
GRANT ALL ON FUNCTION public.dropgeometrytable(catalog_name character varying, schema_name character varying, table_name character varying) TO "Webgroup";
            public       postgres    false    622            �           1255    54505    dump(public.geometry)    FUNCTION     �   CREATE FUNCTION public.dump(public.geometry) RETURNS SETOF public.geometry_dump
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_dump';
 ,   DROP FUNCTION public.dump(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54506    dumprings(public.geometry)    FUNCTION     �   CREATE FUNCTION public.dumprings(public.geometry) RETURNS SETOF public.geometry_dump
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_dump_rings';
 1   DROP FUNCTION public.dumprings(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            i           0    0 !   FUNCTION enablelongtransactions()    ACL     �   GRANT ALL ON FUNCTION public.enablelongtransactions() TO pbrewer;
GRANT ALL ON FUNCTION public.enablelongtransactions() TO "Webgroup";
            public       postgres    false    1592            �           1255    54507    endpoint(public.geometry)    FUNCTION     �   CREATE FUNCTION public.endpoint(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_endpoint_linestring';
 0   DROP FUNCTION public.endpoint(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54508 #   enforce_atleastoneadminuserdelete()    FUNCTION     �  CREATE FUNCTION public.enforce_atleastoneadminuserdelete() RETURNS trigger
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
       public       postgres    false            �           1255    54509 #   enforce_atleastoneadminuserupdate()    FUNCTION     �  CREATE FUNCTION public.enforce_atleastoneadminuserupdate() RETURNS trigger
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
       public       postgres    false            �           1255    54510    enforce_no_loan_when_on_loan()    FUNCTION     �  CREATE FUNCTION public.enforce_no_loan_when_on_loan() RETURNS trigger
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
       public       tellervo    false            �           1255    54511 &   enforce_no_status_update_on_curation()    FUNCTION     [  CREATE FUNCTION public.enforce_no_status_update_on_curation() RETURNS trigger
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
       public       tellervo    false            �           1255    54512 "   enforce_noadminpermeditsondelete()    FUNCTION     �   CREATE FUNCTION public.enforce_noadminpermeditsondelete() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
IF OLD.securitygroupid=1 THEN
   RAISE EXCEPTION 'Administrator permissions cannot be changed';
   RETURN NULL;
END IF;
RETURN OLD;
END;$$;
 9   DROP FUNCTION public.enforce_noadminpermeditsondelete();
       public       tellervo    false            �           1255    54513 (   enforce_noadminpermeditsonupdatecreate()    FUNCTION        CREATE FUNCTION public.enforce_noadminpermeditsonupdatecreate() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
IF NEW.securitygroupid=1 THEN
   RAISE EXCEPTION 'Administrator permissions cannot be changed';
   RETURN NULL;
END IF;
RETURN NEW;
END;$$;
 ?   DROP FUNCTION public.enforce_noadminpermeditsonupdatecreate();
       public       tellervo    false            �           1255    54514    enforce_object-parent()    FUNCTION     1  CREATE FUNCTION public."enforce_object-parent"() RETURNS trigger
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
       public       tellervo    false            �           1255    54515    envelope(public.geometry)    FUNCTION     �   CREATE FUNCTION public.envelope(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_envelope';
 0   DROP FUNCTION public.envelope(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            j           0    0 =   FUNCTION equals(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.equals(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.equals(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    1593            �           1255    54516    estimated_extent(text, text)    FUNCTION     �   CREATE FUNCTION public.estimated_extent(text, text) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT SECURITY DEFINER
    AS '$libdir/postgis-3', 'geometry_estimated_extent';
 3   DROP FUNCTION public.estimated_extent(text, text);
       public       tellervo    false    2    2    2            �           1255    54517 "   estimated_extent(text, text, text)    FUNCTION     �   CREATE FUNCTION public.estimated_extent(text, text, text) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT SECURITY DEFINER
    AS '$libdir/postgis-3', 'geometry_estimated_extent';
 9   DROP FUNCTION public.estimated_extent(text, text, text);
       public       tellervo    false    2    2    2            �           1255    54518 &   expand(public.box2d, double precision)    FUNCTION     �   CREATE FUNCTION public.expand(public.box2d, double precision) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX2D_expand';
 =   DROP FUNCTION public.expand(public.box2d, double precision);
       public       tellervo    false    2    2    2    2    2    2            �           1255    54519 &   expand(public.box3d, double precision)    FUNCTION     �   CREATE FUNCTION public.expand(public.box3d, double precision) RETURNS public.box3d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_expand';
 =   DROP FUNCTION public.expand(public.box3d, double precision);
       public       tellervo    false    2    2    2    2    2    2            �           1255    54520 )   expand(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.expand(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_expand';
 @   DROP FUNCTION public.expand(public.geometry, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54521    exteriorring(public.geometry)    FUNCTION     �   CREATE FUNCTION public.exteriorring(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_exteriorring_polygon';
 4   DROP FUNCTION public.exteriorring(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54522    find_extent(text, text)    FUNCTION     ]  CREATE FUNCTION public.find_extent(text, text) RETURNS public.box2d
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
       public       tellervo    false    2    2    2            �           1255    54523    find_extent(text, text, text)    FUNCTION     �  CREATE FUNCTION public.find_extent(text, text, text) RETURNS public.box2d
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
       public       tellervo    false    2    2    2            k           0    0 K   FUNCTION find_srid(character varying, character varying, character varying)    ACL     �   GRANT ALL ON FUNCTION public.find_srid(character varying, character varying, character varying) TO pbrewer;
GRANT ALL ON FUNCTION public.find_srid(character varying, character varying, character varying) TO "Webgroup";
            public       postgres    false    625            �           1255    54524    fix_geometry_columns()    FUNCTION     (  CREATE FUNCTION public.fix_geometry_columns() RETURNS text
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
       public       tellervo    false            �           1255    54525    force_2d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.force_2d(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_force_2d';
 0   DROP FUNCTION public.force_2d(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54526    force_3d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.force_3d(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_force_3dz';
 0   DROP FUNCTION public.force_3d(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54527    force_3dm(public.geometry)    FUNCTION     �   CREATE FUNCTION public.force_3dm(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_force_3dm';
 1   DROP FUNCTION public.force_3dm(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54528    force_3dz(public.geometry)    FUNCTION     �   CREATE FUNCTION public.force_3dz(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_force_3dz';
 1   DROP FUNCTION public.force_3dz(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54529    force_4d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.force_4d(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_force_4d';
 0   DROP FUNCTION public.force_4d(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54530 !   force_collection(public.geometry)    FUNCTION     �   CREATE FUNCTION public.force_collection(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_force_collection';
 8   DROP FUNCTION public.force_collection(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54531    forcerhr(public.geometry)    FUNCTION     �   CREATE FUNCTION public.forcerhr(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_force_clockwise_poly';
 0   DROP FUNCTION public.forcerhr(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54532    geomcollfromtext(text)    FUNCTION     �   CREATE FUNCTION public.geomcollfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE
	WHEN geometrytype(GeomFromText($1)) = 'GEOMETRYCOLLECTION'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$;
 -   DROP FUNCTION public.geomcollfromtext(text);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54533    geomcollfromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.geomcollfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE
	WHEN geometrytype(GeomFromText($1, $2)) = 'GEOMETRYCOLLECTION'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$;
 6   DROP FUNCTION public.geomcollfromtext(text, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54534    geomcollfromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.geomcollfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE
	WHEN geometrytype(GeomFromWKB($1)) = 'GEOMETRYCOLLECTION'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;
 -   DROP FUNCTION public.geomcollfromwkb(bytea);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54535    geomcollfromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.geomcollfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE
	WHEN geometrytype(GeomFromWKB($1, $2)) = 'GEOMETRYCOLLECTION'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;
 6   DROP FUNCTION public.geomcollfromwkb(bytea, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            l           0    0    FUNCTION geometry(bytea)    ACL     u   GRANT ALL ON FUNCTION public.geometry(bytea) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry(bytea) TO "Webgroup";
            public       postgres    false    667            m           0    0    FUNCTION geometry(text)    ACL     s   GRANT ALL ON FUNCTION public.geometry(text) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry(text) TO "Webgroup";
            public       postgres    false    668            n           0    0    FUNCTION geometry(public.box2d)    ACL     �   GRANT ALL ON FUNCTION public.geometry(public.box2d) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry(public.box2d) TO "Webgroup";
            public       postgres    false    1594            o           0    0    FUNCTION geometry(public.box3d)    ACL     �   GRANT ALL ON FUNCTION public.geometry(public.box3d) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry(public.box3d) TO "Webgroup";
            public       postgres    false    669            p           0    0 E   FUNCTION geometry_above(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_above(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_above(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    1595            q           0    0 E   FUNCTION geometry_below(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_below(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_below(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    1596            r           0    0 C   FUNCTION geometry_cmp(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_cmp(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_cmp(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    1597            s           0    0 B   FUNCTION geometry_eq(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_eq(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_eq(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    1598            t           0    0 B   FUNCTION geometry_ge(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_ge(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_ge(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    1599            u           0    0 B   FUNCTION geometry_gt(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_gt(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_gt(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    479            v           0    0 B   FUNCTION geometry_le(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_le(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_le(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    480            w           0    0 D   FUNCTION geometry_left(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_left(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_left(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    1600            x           0    0 B   FUNCTION geometry_lt(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_lt(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_lt(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    481            y           0    0 I   FUNCTION geometry_overabove(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_overabove(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_overabove(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    509            z           0    0 I   FUNCTION geometry_overbelow(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_overbelow(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_overbelow(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    510            {           0    0 H   FUNCTION geometry_overleft(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_overleft(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_overleft(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    511            |           0    0 I   FUNCTION geometry_overright(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_overright(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_overright(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    1601            }           0    0 E   FUNCTION geometry_right(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_right(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_right(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    512            ~           0    0 D   FUNCTION geometry_same(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.geometry_same(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.geometry_same(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    1602            �           1255    54536    geometryfromtext(text)    FUNCTION     �   CREATE FUNCTION public.geometryfromtext(text) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_from_text';
 -   DROP FUNCTION public.geometryfromtext(text);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54537    geometryfromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.geometryfromtext(text, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_from_text';
 6   DROP FUNCTION public.geometryfromtext(text, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54538 #   geometryn(public.geometry, integer)    FUNCTION     �   CREATE FUNCTION public.geometryn(public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_geometryn_collection';
 :   DROP FUNCTION public.geometryn(public.geometry, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2                       0    0    FUNCTION geomfromewkb(bytea)    ACL     }   GRANT ALL ON FUNCTION public.geomfromewkb(bytea) TO pbrewer;
GRANT ALL ON FUNCTION public.geomfromewkb(bytea) TO "Webgroup";
            public       postgres    false    1603            �           0    0    FUNCTION geomfromewkt(text)    ACL     {   GRANT ALL ON FUNCTION public.geomfromewkt(text) TO pbrewer;
GRANT ALL ON FUNCTION public.geomfromewkt(text) TO "Webgroup";
            public       postgres    false    1604            �           1255    54539    geomfromtext(text)    FUNCTION     �   CREATE FUNCTION public.geomfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_GeomFromText($1)$_$;
 )   DROP FUNCTION public.geomfromtext(text);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54540    geomfromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.geomfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_GeomFromText($1, $2)$_$;
 2   DROP FUNCTION public.geomfromtext(text, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54541    geomfromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.geomfromwkb(bytea) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_from_WKB';
 )   DROP FUNCTION public.geomfromwkb(bytea);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54542    geomfromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.geomfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_SetSRID(ST_GeomFromWKB($1), $2)$_$;
 2   DROP FUNCTION public.geomfromwkb(bytea, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54543 +   geomunion(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.geomunion(public.geometry, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'ST_Union';
 B   DROP FUNCTION public.geomunion(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           0    0 %   FUNCTION get_proj4_from_srid(integer)    ACL     �   GRANT ALL ON FUNCTION public.get_proj4_from_srid(integer) TO pbrewer;
GRANT ALL ON FUNCTION public.get_proj4_from_srid(integer) TO "Webgroup";
            public       postgres    false    626            �           1255    54544    getbbox(public.geometry)    FUNCTION     �   CREATE FUNCTION public.getbbox(public.geometry) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_to_BOX2D';
 /   DROP FUNCTION public.getbbox(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2            �           1255    54545    getsrid(public.geometry)    FUNCTION     �   CREATE FUNCTION public.getsrid(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_get_srid';
 /   DROP FUNCTION public.getsrid(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           0    0    FUNCTION gettransactionid()    ACL     {   GRANT ALL ON FUNCTION public.gettransactionid() TO pbrewer;
GRANT ALL ON FUNCTION public.gettransactionid() TO "Webgroup";
            public       postgres    false    896            �           1255    54546    hasbbox(public.geometry)    FUNCTION     �   CREATE FUNCTION public.hasbbox(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_hasBBOX';
 /   DROP FUNCTION public.hasbbox(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54547 '   interiorringn(public.geometry, integer)    FUNCTION     �   CREATE FUNCTION public.interiorringn(public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_interiorringn_polygon';
 >   DROP FUNCTION public.interiorringn(public.geometry, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54548 .   intersection(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.intersection(public.geometry, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'ST_Intersection';
 E   DROP FUNCTION public.intersection(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54549 ,   intersects(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.intersects(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'ST_Intersects';
 C   DROP FUNCTION public.intersects(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            E           1255    54550    isadmin(integer)    FUNCTION     �   CREATE FUNCTION public.isadmin(securityuserid integer) RETURNS boolean
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
       public       postgres    false            �           0    0 (   FUNCTION isadmin(securityuserid integer)    ACL     L   GRANT ALL ON FUNCTION public.isadmin(securityuserid integer) TO "Webgroup";
            public       postgres    false    1605            �           1255    54551    isclosed(public.geometry)    FUNCTION     �   CREATE FUNCTION public.isclosed(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_isclosed';
 0   DROP FUNCTION public.isclosed(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2                        1255    54552    isempty(public.geometry)    FUNCTION     �   CREATE FUNCTION public.isempty(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_isempty';
 /   DROP FUNCTION public.isempty(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2                       1255    54553    isring(public.geometry)    FUNCTION     �   CREATE FUNCTION public.isring(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'isring';
 .   DROP FUNCTION public.isring(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2                       1255    54554    issimple(public.geometry)    FUNCTION     �   CREATE FUNCTION public.issimple(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'issimple';
 0   DROP FUNCTION public.issimple(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2                       1255    54555    isvalid(public.geometry)    FUNCTION     �   CREATE FUNCTION public.isvalid(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'isvalid';
 /   DROP FUNCTION public.isvalid(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54556    length(public.geometry)    FUNCTION     �   CREATE FUNCTION public.length(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_length_linestring';
 .   DROP FUNCTION public.length(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54557    length2d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.length2d(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_length2d_linestring';
 0   DROP FUNCTION public.length2d(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54558 3   length2d_spheroid(public.geometry, public.spheroid)    FUNCTION     �   CREATE FUNCTION public.length2d_spheroid(public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'LWGEOM_length2d_ellipsoid';
 J   DROP FUNCTION public.length2d_spheroid(public.geometry, public.spheroid);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2            �           1255    54559    length3d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.length3d(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_length_linestring';
 0   DROP FUNCTION public.length3d(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54560 3   length3d_spheroid(public.geometry, public.spheroid)    FUNCTION     �   CREATE FUNCTION public.length3d_spheroid(public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_length_ellipsoid_linestring';
 J   DROP FUNCTION public.length3d_spheroid(public.geometry, public.spheroid);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2            �           1255    54561 1   length_spheroid(public.geometry, public.spheroid)    FUNCTION     �   CREATE FUNCTION public.length_spheroid(public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'LWGEOM_length_ellipsoid_linestring';
 H   DROP FUNCTION public.length_spheroid(public.geometry, public.spheroid);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2            �           1255    54562 9   line_interpolate_point(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.line_interpolate_point(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_line_interpolate_point';
 P   DROP FUNCTION public.line_interpolate_point(public.geometry, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54563 3   line_locate_point(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.line_locate_point(public.geometry, public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_line_locate_point';
 J   DROP FUNCTION public.line_locate_point(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2                       1255    54564 C   line_substring(public.geometry, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.line_substring(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_line_substring';
 Z   DROP FUNCTION public.line_substring(public.geometry, double precision, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2                       1255    54565 #   linefrommultipoint(public.geometry)    FUNCTION     �   CREATE FUNCTION public.linefrommultipoint(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_line_from_mpoint';
 :   DROP FUNCTION public.linefrommultipoint(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2                       1255    54566    linefromtext(text)    FUNCTION     �   CREATE FUNCTION public.linefromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1)) = 'LINESTRING'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$;
 )   DROP FUNCTION public.linefromtext(text);
       public       tellervo    false    2    2    2    2    2    2    2    2                       1255    54567    linefromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.linefromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1, $2)) = 'LINESTRING'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$;
 2   DROP FUNCTION public.linefromtext(text, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2                       1255    54568    linefromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.linefromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'LINESTRING'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;
 )   DROP FUNCTION public.linefromwkb(bytea);
       public       tellervo    false    2    2    2    2    2    2    2    2            	           1255    54569    linefromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.linefromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'LINESTRING'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;
 2   DROP FUNCTION public.linefromwkb(bytea, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            
           1255    54570    linemerge(public.geometry)    FUNCTION     �   CREATE FUNCTION public.linemerge(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'linemerge';
 1   DROP FUNCTION public.linemerge(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2                       1255    54571    linestringfromtext(text)    FUNCTION     �   CREATE FUNCTION public.linestringfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT LineFromText($1)$_$;
 /   DROP FUNCTION public.linestringfromtext(text);
       public       tellervo    false    2    2    2    2    2    2    2    2                       1255    54572 !   linestringfromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.linestringfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT LineFromText($1, $2)$_$;
 8   DROP FUNCTION public.linestringfromtext(text, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2                       1255    54573    linestringfromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.linestringfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'LINESTRING'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;
 /   DROP FUNCTION public.linestringfromwkb(bytea);
       public       tellervo    false    2    2    2    2    2    2    2    2                       1255    54574 !   linestringfromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.linestringfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'LINESTRING'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;
 8   DROP FUNCTION public.linestringfromwkb(bytea, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2                       1255    54575 7   locate_along_measure(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.locate_along_measure(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$ SELECT ST_LocateBetween($1, $2, $2) $_$;
 N   DROP FUNCTION public.locate_along_measure(public.geometry, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2                       1255    54576 L   locate_between_measures(public.geometry, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.locate_between_measures(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_locate_between_m';
 c   DROP FUNCTION public.locate_between_measures(public.geometry, double precision, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           0    0 "   FUNCTION lockrow(text, text, text)    ACL     �   GRANT ALL ON FUNCTION public.lockrow(text, text, text) TO pbrewer;
GRANT ALL ON FUNCTION public.lockrow(text, text, text) TO "Webgroup";
            public       postgres    false    1606            �           0    0 (   FUNCTION lockrow(text, text, text, text)    ACL     �   GRANT ALL ON FUNCTION public.lockrow(text, text, text, text) TO pbrewer;
GRANT ALL ON FUNCTION public.lockrow(text, text, text, text) TO "Webgroup";
            public       postgres    false    1607            �           0    0 ?   FUNCTION lockrow(text, text, text, timestamp without time zone)    ACL     �   GRANT ALL ON FUNCTION public.lockrow(text, text, text, timestamp without time zone) TO pbrewer;
GRANT ALL ON FUNCTION public.lockrow(text, text, text, timestamp without time zone) TO "Webgroup";
            public       postgres    false    876            �           0    0 E   FUNCTION lockrow(text, text, text, text, timestamp without time zone)    ACL     �   GRANT ALL ON FUNCTION public.lockrow(text, text, text, text, timestamp without time zone) TO pbrewer;
GRANT ALL ON FUNCTION public.lockrow(text, text, text, text, timestamp without time zone) TO "Webgroup";
            public       postgres    false    1588            �           0    0 "   FUNCTION longtransactionsenabled()    ACL     �   GRANT ALL ON FUNCTION public.longtransactionsenabled() TO pbrewer;
GRANT ALL ON FUNCTION public.longtransactionsenabled() TO "Webgroup";
            public       postgres    false    897                       1255    54577    m(public.geometry)    FUNCTION     �   CREATE FUNCTION public.m(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_m_point';
 )   DROP FUNCTION public.m(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2                       1255    54578 +   makebox2d(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.makebox2d(public.geometry, public.geometry) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX2D_construct';
 B   DROP FUNCTION public.makebox2d(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2                       1255    54579 +   makebox3d(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.makebox3d(public.geometry, public.geometry) RETURNS public.box3d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_construct';
 B   DROP FUNCTION public.makebox3d(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2                       1255    54580 *   makeline(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.makeline(public.geometry, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_makeline';
 A   DROP FUNCTION public.makeline(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2                       1255    54581 "   makeline_garray(public.geometry[])    FUNCTION     �   CREATE FUNCTION public.makeline_garray(public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_makeline_garray';
 9   DROP FUNCTION public.makeline_garray(public.geometry[]);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2                       1255    54582 -   makepoint(double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.makepoint(double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_makepoint';
 D   DROP FUNCTION public.makepoint(double precision, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2                       1255    54583 ?   makepoint(double precision, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.makepoint(double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_makepoint';
 V   DROP FUNCTION public.makepoint(double precision, double precision, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2                       1255    54584 Q   makepoint(double precision, double precision, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.makepoint(double precision, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_makepoint';
 h   DROP FUNCTION public.makepoint(double precision, double precision, double precision, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2                       1255    54585 @   makepointm(double precision, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.makepointm(double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_makepoint3dm';
 W   DROP FUNCTION public.makepointm(double precision, double precision, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2                       1255    54586    makepolygon(public.geometry)    FUNCTION     �   CREATE FUNCTION public.makepolygon(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_makepoly';
 3   DROP FUNCTION public.makepolygon(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2                       1255    54587 /   makepolygon(public.geometry, public.geometry[])    FUNCTION     �   CREATE FUNCTION public.makepolygon(public.geometry, public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_makepoly';
 F   DROP FUNCTION public.makepolygon(public.geometry, public.geometry[]);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2                       1255    54588 .   max_distance(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.max_distance(public.geometry, public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_maxdistance2d_linestring';
 E   DROP FUNCTION public.max_distance(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2                       1255    54589    mem_size(public.geometry)    FUNCTION     �   CREATE FUNCTION public.mem_size(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_mem_size';
 0   DROP FUNCTION public.mem_size(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2                       1255    54590    mlinefromtext(text)    FUNCTION     �   CREATE FUNCTION public.mlinefromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1)) = 'MULTILINESTRING'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$;
 *   DROP FUNCTION public.mlinefromtext(text);
       public       tellervo    false    2    2    2    2    2    2    2    2                       1255    54591    mlinefromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.mlinefromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE
	WHEN geometrytype(GeomFromText($1, $2)) = 'MULTILINESTRING'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$;
 3   DROP FUNCTION public.mlinefromtext(text, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2                        1255    54592    mlinefromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.mlinefromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'MULTILINESTRING'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;
 *   DROP FUNCTION public.mlinefromwkb(bytea);
       public       tellervo    false    2    2    2    2    2    2    2    2            !           1255    54593    mlinefromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.mlinefromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'MULTILINESTRING'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;
 3   DROP FUNCTION public.mlinefromwkb(bytea, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            "           1255    54594    mpointfromtext(text)    FUNCTION     �   CREATE FUNCTION public.mpointfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1)) = 'MULTIPOINT'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$;
 +   DROP FUNCTION public.mpointfromtext(text);
       public       tellervo    false    2    2    2    2    2    2    2    2            #           1255    54595    mpointfromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.mpointfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1,$2)) = 'MULTIPOINT'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$;
 4   DROP FUNCTION public.mpointfromtext(text, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            $           1255    54596    mpointfromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.mpointfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'MULTIPOINT'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;
 +   DROP FUNCTION public.mpointfromwkb(bytea);
       public       tellervo    false    2    2    2    2    2    2    2    2            %           1255    54597    mpointfromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.mpointfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1,$2)) = 'MULTIPOINT'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;
 4   DROP FUNCTION public.mpointfromwkb(bytea, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            &           1255    54598    mpolyfromtext(text)    FUNCTION     �   CREATE FUNCTION public.mpolyfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1)) = 'MULTIPOLYGON'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$;
 *   DROP FUNCTION public.mpolyfromtext(text);
       public       tellervo    false    2    2    2    2    2    2    2    2            '           1255    54599    mpolyfromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.mpolyfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1, $2)) = 'MULTIPOLYGON'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$;
 3   DROP FUNCTION public.mpolyfromtext(text, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            (           1255    54600    mpolyfromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.mpolyfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'MULTIPOLYGON'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;
 *   DROP FUNCTION public.mpolyfromwkb(bytea);
       public       tellervo    false    2    2    2    2    2    2    2    2            )           1255    54601    mpolyfromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.mpolyfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'MULTIPOLYGON'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;
 3   DROP FUNCTION public.mpolyfromwkb(bytea, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            *           1255    54602    multi(public.geometry)    FUNCTION     �   CREATE FUNCTION public.multi(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_force_multi';
 -   DROP FUNCTION public.multi(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            +           1255    54603    multilinefromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.multilinefromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'MULTILINESTRING'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;
 .   DROP FUNCTION public.multilinefromwkb(bytea);
       public       tellervo    false    2    2    2    2    2    2    2    2            ,           1255    54604     multilinefromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.multilinefromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'MULTILINESTRING'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;
 7   DROP FUNCTION public.multilinefromwkb(bytea, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            -           1255    54605    multilinestringfromtext(text)    FUNCTION     �   CREATE FUNCTION public.multilinestringfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_MLineFromText($1)$_$;
 4   DROP FUNCTION public.multilinestringfromtext(text);
       public       tellervo    false    2    2    2    2    2    2    2    2            .           1255    54606 &   multilinestringfromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.multilinestringfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT MLineFromText($1, $2)$_$;
 =   DROP FUNCTION public.multilinestringfromtext(text, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            /           1255    54607    multipointfromtext(text)    FUNCTION     �   CREATE FUNCTION public.multipointfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT MPointFromText($1)$_$;
 /   DROP FUNCTION public.multipointfromtext(text);
       public       tellervo    false    2    2    2    2    2    2    2    2            0           1255    54608 !   multipointfromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.multipointfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT MPointFromText($1, $2)$_$;
 8   DROP FUNCTION public.multipointfromtext(text, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            1           1255    54609    multipointfromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.multipointfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'MULTIPOINT'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;
 /   DROP FUNCTION public.multipointfromwkb(bytea);
       public       tellervo    false    2    2    2    2    2    2    2    2            2           1255    54610 !   multipointfromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.multipointfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1,$2)) = 'MULTIPOINT'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;
 8   DROP FUNCTION public.multipointfromwkb(bytea, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            3           1255    54611    multipolyfromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.multipolyfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'MULTIPOLYGON'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;
 .   DROP FUNCTION public.multipolyfromwkb(bytea);
       public       tellervo    false    2    2    2    2    2    2    2    2            4           1255    54612     multipolyfromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.multipolyfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'MULTIPOLYGON'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;
 7   DROP FUNCTION public.multipolyfromwkb(bytea, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            5           1255    54613    multipolygonfromtext(text)    FUNCTION     �   CREATE FUNCTION public.multipolygonfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT MPolyFromText($1)$_$;
 1   DROP FUNCTION public.multipolygonfromtext(text);
       public       tellervo    false    2    2    2    2    2    2    2    2            6           1255    54614 #   multipolygonfromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.multipolygonfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT MPolyFromText($1, $2)$_$;
 :   DROP FUNCTION public.multipolygonfromtext(text, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            7           1255    54615    ndims(public.geometry)    FUNCTION     �   CREATE FUNCTION public.ndims(public.geometry) RETURNS smallint
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_ndims';
 -   DROP FUNCTION public.ndims(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            8           1255    54616    noop(public.geometry)    FUNCTION     �   CREATE FUNCTION public.noop(public.geometry) RETURNS public.geometry
    LANGUAGE c STRICT
    AS '$libdir/postgis-3', 'LWGEOM_noop';
 ,   DROP FUNCTION public.noop(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            9           1255    54617 8   normal_rand(integer, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.normal_rand(integer, double precision, double precision) RETURNS SETOF double precision
    LANGUAGE c STRICT
    AS '$libdir/tablefunc', 'normal_rand';
 O   DROP FUNCTION public.normal_rand(integer, double precision, double precision);
       public       postgres    false            :           1255    54618    npoints(public.geometry)    FUNCTION     �   CREATE FUNCTION public.npoints(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_npoints';
 /   DROP FUNCTION public.npoints(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            ;           1255    54619    nrings(public.geometry)    FUNCTION     �   CREATE FUNCTION public.nrings(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_nrings';
 .   DROP FUNCTION public.nrings(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            <           1255    54620    numgeometries(public.geometry)    FUNCTION     �   CREATE FUNCTION public.numgeometries(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_numgeometries_collection';
 5   DROP FUNCTION public.numgeometries(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            =           1255    54621     numinteriorring(public.geometry)    FUNCTION     �   CREATE FUNCTION public.numinteriorring(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_numinteriorrings_polygon';
 7   DROP FUNCTION public.numinteriorring(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            >           1255    54622 !   numinteriorrings(public.geometry)    FUNCTION     �   CREATE FUNCTION public.numinteriorrings(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_numinteriorrings_polygon';
 8   DROP FUNCTION public.numinteriorrings(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            ?           1255    54623    numpoints(public.geometry)    FUNCTION     �   CREATE FUNCTION public.numpoints(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_numpoints_linestring';
 1   DROP FUNCTION public.numpoints(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            @           1255    54624 *   overlaps(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public."overlaps"(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'overlaps';
 C   DROP FUNCTION public."overlaps"(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            A           1255    54625    perimeter2d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.perimeter2d(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_perimeter2d_poly';
 3   DROP FUNCTION public.perimeter2d(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            B           1255    54626    perimeter3d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.perimeter3d(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_perimeter_poly';
 3   DROP FUNCTION public.perimeter3d(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            C           1255    54627 (   plpgsql_test_external(character varying)    FUNCTION     �   CREATE FUNCTION public.plpgsql_test_external(character varying) RETURNS character varying
    LANGUAGE plpgsql
    AS $_$
BEGIN
   return $1 || ' from plpgsql!';
END;
$_$;
 ?   DROP FUNCTION public.plpgsql_test_external(character varying);
       public       postgres    false            D           1255    54628 Z   point_inside_circle(public.geometry, double precision, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.point_inside_circle(public.geometry, double precision, double precision, double precision) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_inside_circle_point';
 q   DROP FUNCTION public.point_inside_circle(public.geometry, double precision, double precision, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2            E           1255    54629    pointfromtext(text)    FUNCTION     �   CREATE FUNCTION public.pointfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1)) = 'POINT'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$;
 *   DROP FUNCTION public.pointfromtext(text);
       public       tellervo    false    2    2    2    2    2    2    2    2            F           1255    54630    pointfromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.pointfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1, $2)) = 'POINT'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$;
 3   DROP FUNCTION public.pointfromtext(text, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            G           1255    54631    pointfromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.pointfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'POINT'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;
 *   DROP FUNCTION public.pointfromwkb(bytea);
       public       tellervo    false    2    2    2    2    2    2    2    2            H           1255    54632    pointfromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.pointfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromWKB($1, $2)) = 'POINT'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;
 3   DROP FUNCTION public.pointfromwkb(bytea, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            I           1255    54633     pointn(public.geometry, integer)    FUNCTION     �   CREATE FUNCTION public.pointn(public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_pointn_linestring';
 7   DROP FUNCTION public.pointn(public.geometry, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            J           1255    54634    pointonsurface(public.geometry)    FUNCTION     �   CREATE FUNCTION public.pointonsurface(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'pointonsurface';
 6   DROP FUNCTION public.pointonsurface(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            K           1255    54635    polyfromtext(text)    FUNCTION     �   CREATE FUNCTION public.polyfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1)) = 'POLYGON'
	THEN GeomFromText($1)
	ELSE NULL END
	$_$;
 )   DROP FUNCTION public.polyfromtext(text);
       public       tellervo    false    2    2    2    2    2    2    2    2            L           1255    54636    polyfromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.polyfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromText($1, $2)) = 'POLYGON'
	THEN GeomFromText($1,$2)
	ELSE NULL END
	$_$;
 2   DROP FUNCTION public.polyfromtext(text, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            M           1255    54637    polyfromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.polyfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'POLYGON'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;
 )   DROP FUNCTION public.polyfromwkb(bytea);
       public       tellervo    false    2    2    2    2    2    2    2    2            N           1255    54638    polyfromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.polyfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1, $2)) = 'POLYGON'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;
 2   DROP FUNCTION public.polyfromwkb(bytea, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            O           1255    54639    polygonfromtext(text)    FUNCTION     �   CREATE FUNCTION public.polygonfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT PolyFromText($1)$_$;
 ,   DROP FUNCTION public.polygonfromtext(text);
       public       tellervo    false    2    2    2    2    2    2    2    2            P           1255    54640    polygonfromtext(text, integer)    FUNCTION     �   CREATE FUNCTION public.polygonfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT PolyFromText($1, $2)$_$;
 5   DROP FUNCTION public.polygonfromtext(text, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            Q           1255    54641    polygonfromwkb(bytea)    FUNCTION     �   CREATE FUNCTION public.polygonfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1)) = 'POLYGON'
	THEN GeomFromWKB($1)
	ELSE NULL END
	$_$;
 ,   DROP FUNCTION public.polygonfromwkb(bytea);
       public       tellervo    false    2    2    2    2    2    2    2    2            R           1255    54642    polygonfromwkb(bytea, integer)    FUNCTION     �   CREATE FUNCTION public.polygonfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT CASE WHEN geometrytype(GeomFromWKB($1,$2)) = 'POLYGON'
	THEN GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;
 5   DROP FUNCTION public.polygonfromwkb(bytea, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            S           1255    54643 $   polygonize_garray(public.geometry[])    FUNCTION     �   CREATE FUNCTION public.polygonize_garray(public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'polygonize_garray';
 ;   DROP FUNCTION public.polygonize_garray(public.geometry[]);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           0    0    FUNCTION postgis_full_version()    ACL     �   GRANT ALL ON FUNCTION public.postgis_full_version() TO pbrewer;
GRANT ALL ON FUNCTION public.postgis_full_version() TO "Webgroup";
            public       postgres    false    1608            �           0    0    FUNCTION postgis_geos_version()    ACL     �   GRANT ALL ON FUNCTION public.postgis_geos_version() TO pbrewer;
GRANT ALL ON FUNCTION public.postgis_geos_version() TO "Webgroup";
            public       postgres    false    1609            �           0    0 !   FUNCTION postgis_lib_build_date()    ACL     �   GRANT ALL ON FUNCTION public.postgis_lib_build_date() TO pbrewer;
GRANT ALL ON FUNCTION public.postgis_lib_build_date() TO "Webgroup";
            public       postgres    false    646            �           0    0    FUNCTION postgis_lib_version()    ACL     �   GRANT ALL ON FUNCTION public.postgis_lib_version() TO pbrewer;
GRANT ALL ON FUNCTION public.postgis_lib_version() TO "Webgroup";
            public       postgres    false    1610            �           0    0    FUNCTION postgis_proj_version()    ACL     �   GRANT ALL ON FUNCTION public.postgis_proj_version() TO pbrewer;
GRANT ALL ON FUNCTION public.postgis_proj_version() TO "Webgroup";
            public       postgres    false    647            �           0    0 %   FUNCTION postgis_scripts_build_date()    ACL     �   GRANT ALL ON FUNCTION public.postgis_scripts_build_date() TO pbrewer;
GRANT ALL ON FUNCTION public.postgis_scripts_build_date() TO "Webgroup";
            public       postgres    false    648            �           0    0 $   FUNCTION postgis_scripts_installed()    ACL     �   GRANT ALL ON FUNCTION public.postgis_scripts_installed() TO pbrewer;
GRANT ALL ON FUNCTION public.postgis_scripts_installed() TO "Webgroup";
            public       postgres    false    1611            �           0    0 #   FUNCTION postgis_scripts_released()    ACL     �   GRANT ALL ON FUNCTION public.postgis_scripts_released() TO pbrewer;
GRANT ALL ON FUNCTION public.postgis_scripts_released() TO "Webgroup";
            public       postgres    false    649            �           0    0    FUNCTION postgis_version()    ACL     y   GRANT ALL ON FUNCTION public.postgis_version() TO pbrewer;
GRANT ALL ON FUNCTION public.postgis_version() TO "Webgroup";
            public       postgres    false    650            T           1255    54644    probe_geometry_columns()    FUNCTION       CREATE FUNCTION public.probe_geometry_columns() RETURNS text
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
       public       tellervo    false            U           1255    54645 ?   qappvmeasurementresultreadingopsum1(character varying, integer)    FUNCTION     �  CREATE FUNCTION public.qappvmeasurementresultreadingopsum1(character varying, integer) RETURNS SETOF record
    LANGUAGE sql
    AS $_$
   SELECT tblVMeasurementReadingResult.*, $2 + 1 AS RelYearPlusOne 
      FROM tblVMeasurementResult INNER JOIN tblVMeasurementReadingResult ON
      tblVMeasurementResult.VMeasurementResultID = tblVMeasurementReadingResult.VMeasurementResultID
      WHERE tblVMeasurementResult.VMeasurementResultGroupID=$1
      ORDER BY tblVMeasurementReadingResult.RelYear;
$_$;
 V   DROP FUNCTION public.qappvmeasurementresultreadingopsum1(character varying, integer);
       public       postgres    false            V           1255    54646 (   relate(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.relate(public.geometry, public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'relate_full';
 ?   DROP FUNCTION public.relate(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            W           1255    54647 .   relate(public.geometry, public.geometry, text)    FUNCTION     �   CREATE FUNCTION public.relate(public.geometry, public.geometry, text) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'relate_pattern';
 E   DROP FUNCTION public.relate(public.geometry, public.geometry, text);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            X           1255    54648 %   removepoint(public.geometry, integer)    FUNCTION     �   CREATE FUNCTION public.removepoint(public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_removepoint';
 <   DROP FUNCTION public.removepoint(public.geometry, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            Y           1255    54649 #   rename_geometry_table_constraints()    FUNCTION     �   CREATE FUNCTION public.rename_geometry_table_constraints() RETURNS text
    LANGUAGE sql IMMUTABLE
    AS $$
SELECT 'rename_geometry_table_constraint() is obsoleted'::text
$$;
 :   DROP FUNCTION public.rename_geometry_table_constraints();
       public       tellervo    false            Z           1255    54650    reverse(public.geometry)    FUNCTION     �   CREATE FUNCTION public.reverse(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_reverse';
 /   DROP FUNCTION public.reverse(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            [           1255    54651 )   rotate(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.rotate(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_rotateZ($1, $2)$_$;
 @   DROP FUNCTION public.rotate(public.geometry, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            \           1255    54652 *   rotatex(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.rotatex(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_affine($1, 1, 0, 0, 0, cos($2), -sin($2), 0, sin($2), cos($2), 0, 0, 0)$_$;
 A   DROP FUNCTION public.rotatex(public.geometry, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            ]           1255    54653 *   rotatey(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.rotatey(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_affine($1,  cos($2), 0, sin($2),  0, 1, 0,  -sin($2), 0, cos($2), 0,  0, 0)$_$;
 A   DROP FUNCTION public.rotatey(public.geometry, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            ^           1255    54654 *   rotatez(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.rotatez(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_affine($1,  cos($2), -sin($2), 0,  sin($2), cos($2), 0,  0, 0, 1,  0, 0, 0)$_$;
 A   DROP FUNCTION public.rotatez(public.geometry, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            _           1255    54655 :   scale(public.geometry, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.scale(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_scale($1, $2, $3, 1)$_$;
 Q   DROP FUNCTION public.scale(public.geometry, double precision, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            `           1255    54656 L   scale(public.geometry, double precision, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.scale(public.geometry, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_affine($1,  $2, 0, 0,  0, $3, 0,  0, 0, $4,  0, 0, 0)$_$;
 c   DROP FUNCTION public.scale(public.geometry, double precision, double precision, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            a           1255    54657 7   se_envelopesintersect(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.se_envelopesintersect(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	SELECT $1 && $2
	$_$;
 N   DROP FUNCTION public.se_envelopesintersect(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            b           1255    54658    se_is3d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.se_is3d(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_hasz';
 /   DROP FUNCTION public.se_is3d(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            c           1255    54659    se_ismeasured(public.geometry)    FUNCTION     �   CREATE FUNCTION public.se_ismeasured(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_hasm';
 5   DROP FUNCTION public.se_ismeasured(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            d           1255    54660 1   se_locatealong(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.se_locatealong(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$ SELECT SE_LocateBetween($1, $2, $2) $_$;
 H   DROP FUNCTION public.se_locatealong(public.geometry, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            e           1255    54661 E   se_locatebetween(public.geometry, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.se_locatebetween(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_locate_between_m';
 \   DROP FUNCTION public.se_locatebetween(public.geometry, double precision, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            f           1255    54662    se_m(public.geometry)    FUNCTION     �   CREATE FUNCTION public.se_m(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_m_point';
 ,   DROP FUNCTION public.se_m(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            g           1255    54663    se_z(public.geometry)    FUNCTION     �   CREATE FUNCTION public.se_z(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_z_point';
 ,   DROP FUNCTION public.se_z(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            h           1255    54664 )   securitygroupelementmaster(integer, uuid)    FUNCTION     �  CREATE FUNCTION public.securitygroupelementmaster(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select securitygrouppermissiveelementcombined.* 
from securitygrouppermissiveelementcombined($1, $2) left join securitygrouprestrictiveelementcombined($2) on securitygrouppermissiveelementcombined.objectid = securitygrouprestrictiveelementcombined.objectid 
where (((securitygrouprestrictiveelementcombined.objectid) is null));$_$;
 d   DROP FUNCTION public.securitygroupelementmaster(securitypermissionid integer, securityuserid uuid);
       public       tellervo    false    2099            l           1255    54665 +   securitygroupobjectmaster(integer, integer)    FUNCTION     �  CREATE FUNCTION public.securitygroupobjectmaster(securitypermissionid integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select securitygrouppermissiveobjectcombined.* 
from securitygrouppermissiveobjectcombined($1, $2) left join securitygrouprestrictiveobjectcombined($2) on securitygrouppermissiveobjectcombined.objectid = securitygrouprestrictiveobjectcombined.objectid 
where (((securitygrouprestrictiveobjectcombined.objectid) is null));$_$;
 f   DROP FUNCTION public.securitygroupobjectmaster(securitypermissionid integer, securityuserid integer);
       public       postgres    false    2093            �           0    0 X   FUNCTION securitygroupobjectmaster(securitypermissionid integer, securityuserid integer)    ACL     |   GRANT ALL ON FUNCTION public.securitygroupobjectmaster(securitypermissionid integer, securityuserid integer) TO "Webgroup";
            public       postgres    false    1388            i           1255    54666 (   securitygroupobjectmaster(integer, uuid)    FUNCTION     �  CREATE FUNCTION public.securitygroupobjectmaster(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select securitygrouppermissiveobjectcombined.* 
from securitygrouppermissiveobjectcombined($1, $2) left join securitygrouprestrictiveobjectcombined($2) on securitygrouppermissiveobjectcombined.objectid = securitygrouprestrictiveobjectcombined.objectid 
where (((securitygrouprestrictiveobjectcombined.objectid) is null));$_$;
 c   DROP FUNCTION public.securitygroupobjectmaster(securitypermissionid integer, securityuserid uuid);
       public       tellervo    false    2099            j           1255    54667 .   securitygrouppermissivedefault1(integer, uuid)    FUNCTION     e  CREATE FUNCTION public.securitygrouppermissivedefault1(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandentityid
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
       public       tellervo    false    2096            m           1255    54668 .   securitygrouppermissivedefault2(integer, uuid)    FUNCTION     r  CREATE FUNCTION public.securitygrouppermissivedefault2(securitypermission integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandentityid
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
       public       tellervo    false    2096            n           1255    54669 .   securitygrouppermissivedefault3(integer, uuid)    FUNCTION     �  CREATE FUNCTION public.securitygrouppermissivedefault3(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandentityid
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
       public       tellervo    false    2096            L           1255    54670 8   securitygrouppermissivedefaultcombined(integer, integer)    FUNCTION     U  CREATE FUNCTION public.securitygrouppermissivedefaultcombined(securitypermissionid integer, securityuserid integer) RETURNS SETOF integer
    LANGUAGE sql
    AS $_$SELECT * from securitygrouppermissivedefault1($1,$2)
UNION
SELECT * from securitygrouppermissivedefault2($1,$2)
UNION SELECT * from securitygrouppermissivedefault3($1,$2);$_$;
 s   DROP FUNCTION public.securitygrouppermissivedefaultcombined(securitypermissionid integer, securityuserid integer);
       public       postgres    false            �           0    0 e   FUNCTION securitygrouppermissivedefaultcombined(securitypermissionid integer, securityuserid integer)    ACL     �   GRANT ALL ON FUNCTION public.securitygrouppermissivedefaultcombined(securitypermissionid integer, securityuserid integer) TO "Webgroup";
            public       postgres    false    1612            o           1255    54671 5   securitygrouppermissivedefaultcombined(integer, uuid)    FUNCTION     m  CREATE FUNCTION public.securitygrouppermissivedefaultcombined(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandentityid
    LANGUAGE sql
    AS $_$SELECT * from securitygrouppermissivedefault1($1,$2)
UNION
SELECT * from securitygrouppermissivedefault2($1,$2)
UNION SELECT * from securitygrouppermissivedefault3($1,$2);$_$;
 p   DROP FUNCTION public.securitygrouppermissivedefaultcombined(securitypermissionid integer, securityuserid uuid);
       public       tellervo    false    2096            p           1255    54672 .   securitygrouppermissiveelement1(integer, uuid)    FUNCTION     W  CREATE FUNCTION public.securitygrouppermissiveelement1(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
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
       public       tellervo    false    2099            q           1255    54673 .   securitygrouppermissiveelement2(integer, uuid)    FUNCTION     e  CREATE FUNCTION public.securitygrouppermissiveelement2(securitypermission integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
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
       public       tellervo    false    2099            |           1255    54674 .   securitygrouppermissiveelement3(integer, uuid)    FUNCTION     �  CREATE FUNCTION public.securitygrouppermissiveelement3(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
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
       public       tellervo    false    2099            }           1255    54675 5   securitygrouppermissiveelementcombined(integer, uuid)    FUNCTION     o  CREATE FUNCTION public.securitygrouppermissiveelementcombined(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$SELECT * from securitygrouppermissiveelement1($1,$2)
UNION
SELECT * from securitygrouppermissiveelement2($1,$2)
UNION SELECT * from securitygrouppermissiveelement3($1,$2);$_$;
 p   DROP FUNCTION public.securitygrouppermissiveelementcombined(securitypermissionid integer, securityuserid uuid);
       public       tellervo    false    2099            M           1255    54676 0   securitygrouppermissiveobject1(integer, integer)    FUNCTION     L  CREATE FUNCTION public.securitygrouppermissiveobject1(securitypermissionid integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
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
       public       postgres    false    2093            �           0    0 ]   FUNCTION securitygrouppermissiveobject1(securitypermissionid integer, securityuserid integer)    COMMENT     �  COMMENT ON FUNCTION public.securitygrouppermissiveobject1(securitypermissionid integer, securityuserid integer) IS 'This is part of a series of functions:
securitygrouppermissiveobject1, securitygrouppermissiveobject2, securitygrouppermissiveobject3 and securitygrouppermissiveobjectcombined.  Together they work like a 2 tier recursive function and as such will probably be replaced in the future.  Together they bring together all the objects that a specified user has the specified permission type on.  See also securitygrouprestrictiveobject... and securitygroupobjectmaster.  Similar functions have been created for trees and vmeasurements.  ';
            public       postgres    false    1613            �           0    0 ]   FUNCTION securitygrouppermissiveobject1(securitypermissionid integer, securityuserid integer)    ACL     �   GRANT ALL ON FUNCTION public.securitygrouppermissiveobject1(securitypermissionid integer, securityuserid integer) TO "Webgroup";
            public       postgres    false    1613            ~           1255    54677 -   securitygrouppermissiveobject1(integer, uuid)    FUNCTION     O  CREATE FUNCTION public.securitygrouppermissiveobject1(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
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
       public       tellervo    false    2099            �           1255    54678 0   securitygrouppermissiveobject2(integer, integer)    FUNCTION     Y  CREATE FUNCTION public.securitygrouppermissiveobject2(securitypermission integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
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
       public       postgres    false    2093            �           0    0 [   FUNCTION securitygrouppermissiveobject2(securitypermission integer, securityuserid integer)    COMMENT     �  COMMENT ON FUNCTION public.securitygrouppermissiveobject2(securitypermission integer, securityuserid integer) IS 'This is part of a series of functions:
securitygrouppermissiveobject1, securitygrouppermissiveobject2, securitygrouppermissiveobject3 and securitygrouppermissiveobjectcombined.  Together they work like a 2 tier recursive function and as such will probably be replaced in the future.  Together they bring together all the objects that a specified user has the specified permission type on.  See also securitygrouprestrictiveobject... and securitygroupobjectmaster.  Similar functions have been created for trees and vmeasurements.  ';
            public       postgres    false    1410            �           0    0 [   FUNCTION securitygrouppermissiveobject2(securitypermission integer, securityuserid integer)    ACL        GRANT ALL ON FUNCTION public.securitygrouppermissiveobject2(securitypermission integer, securityuserid integer) TO "Webgroup";
            public       postgres    false    1410                       1255    54679 -   securitygrouppermissiveobject2(integer, uuid)    FUNCTION     \  CREATE FUNCTION public.securitygrouppermissiveobject2(securitypermission integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
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
       public       tellervo    false    2099            N           1255    54680 0   securitygrouppermissiveobject3(integer, integer)    FUNCTION     �  CREATE FUNCTION public.securitygrouppermissiveobject3(securitypermissionid integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
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
       public       postgres    false    2093            �           0    0 ]   FUNCTION securitygrouppermissiveobject3(securitypermissionid integer, securityuserid integer)    COMMENT     �  COMMENT ON FUNCTION public.securitygrouppermissiveobject3(securitypermissionid integer, securityuserid integer) IS 'This is part of a series of functions:
securitygrouppermissiveobject1, securitygrouppermissiveobject2, securitygrouppermissiveobject3 and securitygrouppermissiveobjectcombined.  Together they work like a 2 tier recursive function and as such will probably be replaced in the future.  Together they bring together all the objects that a specified user has the specified permission type on.  See also securitygrouprestrictiveobject... and securitygroupobjectmaster.  Similar functions have been created for trees and vmeasurements.  ';
            public       postgres    false    1614            �           0    0 ]   FUNCTION securitygrouppermissiveobject3(securitypermissionid integer, securityuserid integer)    ACL     �   GRANT ALL ON FUNCTION public.securitygrouppermissiveobject3(securitypermissionid integer, securityuserid integer) TO "Webgroup";
            public       postgres    false    1614            �           1255    54681 -   securitygrouppermissiveobject3(integer, uuid)    FUNCTION     �  CREATE FUNCTION public.securitygrouppermissiveobject3(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
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
       public       tellervo    false    2099            �           1255    54682 7   securitygrouppermissiveobjectcombined(integer, integer)    FUNCTION     h  CREATE FUNCTION public.securitygrouppermissiveobjectcombined(securitypermissionid integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$SELECT * from securitygrouppermissiveobject1($1,$2)
UNION
SELECT * from securitygrouppermissiveobject2($1,$2)
UNION SELECT * from securitygrouppermissiveobject3($1,$2);$_$;
 r   DROP FUNCTION public.securitygrouppermissiveobjectcombined(securitypermissionid integer, securityuserid integer);
       public       postgres    false    2093            �           0    0 d   FUNCTION securitygrouppermissiveobjectcombined(securitypermissionid integer, securityuserid integer)    COMMENT     �  COMMENT ON FUNCTION public.securitygrouppermissiveobjectcombined(securitypermissionid integer, securityuserid integer) IS 'This is part of a series of functions:
securitygrouppermissiveobject1, securitygrouppermissiveobject2, securitygrouppermissiveobject3 and securitygrouppermissiveobjectcombined.  Together they work like a 2 tier recursive function and as such will probably be replaced in the future.  Together they bring together all the objects that a specified user has the specified permission type on.  See also securitygrouprestrictiveobject... and securitygroupobjectmaster.  Similar functions have been created for trees and vmeasurements.  ';
            public       postgres    false    1413            �           0    0 d   FUNCTION securitygrouppermissiveobjectcombined(securitypermissionid integer, securityuserid integer)    ACL     �   GRANT ALL ON FUNCTION public.securitygrouppermissiveobjectcombined(securitypermissionid integer, securityuserid integer) TO "Webgroup";
            public       postgres    false    1413            �           1255    54683 4   securitygrouppermissiveobjectcombined(integer, uuid)    FUNCTION     k  CREATE FUNCTION public.securitygrouppermissiveobjectcombined(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$SELECT * from securitygrouppermissiveobject1($1,$2)
UNION
SELECT * from securitygrouppermissiveobject2($1,$2)
UNION SELECT * from securitygrouppermissiveobject3($1,$2);$_$;
 o   DROP FUNCTION public.securitygrouppermissiveobjectcombined(securitypermissionid integer, securityuserid uuid);
       public       tellervo    false    2099            �           1255    54684 .   securitygrouppermissivetree1(integer, integer)    FUNCTION     B  CREATE FUNCTION public.securitygrouppermissivetree1(securitypermissionid integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
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
       public       postgres    false    2093            �           0    0 [   FUNCTION securitygrouppermissivetree1(securitypermissionid integer, securityuserid integer)    ACL        GRANT ALL ON FUNCTION public.securitygrouppermissivetree1(securitypermissionid integer, securityuserid integer) TO "Webgroup";
            public       postgres    false    1414            �           1255    54685 .   securitygrouppermissivetree2(integer, integer)    FUNCTION     O  CREATE FUNCTION public.securitygrouppermissivetree2(securitypermission integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
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
       public       postgres    false    2093            �           0    0 Y   FUNCTION securitygrouppermissivetree2(securitypermission integer, securityuserid integer)    ACL     }   GRANT ALL ON FUNCTION public.securitygrouppermissivetree2(securitypermission integer, securityuserid integer) TO "Webgroup";
            public       postgres    false    1415            �           1255    54686 .   securitygrouppermissivetree3(integer, integer)    FUNCTION     �  CREATE FUNCTION public.securitygrouppermissivetree3(securitypermissionid integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
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
       public       postgres    false    2093            �           0    0 [   FUNCTION securitygrouppermissivetree3(securitypermissionid integer, securityuserid integer)    ACL        GRANT ALL ON FUNCTION public.securitygrouppermissivetree3(securitypermissionid integer, securityuserid integer) TO "Webgroup";
            public       postgres    false    1416            �           1255    54687 5   securitygrouppermissivetreecombined(integer, integer)    FUNCTION     `  CREATE FUNCTION public.securitygrouppermissivetreecombined(securitypermissionid integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$SELECT * from securitygrouppermissivetree1($1,$2)
UNION
SELECT * from securitygrouppermissivetree2($1,$2)
UNION SELECT * from securitygrouppermissivetree3($1,$2);$_$;
 p   DROP FUNCTION public.securitygrouppermissivetreecombined(securitypermissionid integer, securityuserid integer);
       public       postgres    false    2093            �           0    0 b   FUNCTION securitygrouppermissivetreecombined(securitypermissionid integer, securityuserid integer)    ACL     �   GRANT ALL ON FUNCTION public.securitygrouppermissivetreecombined(securitypermissionid integer, securityuserid integer) TO "Webgroup";
            public       postgres    false    1417            U           1255    54688 6   securitygrouppermissivevmeasurement1(integer, integer)    FUNCTION     y  CREATE FUNCTION public.securitygrouppermissivevmeasurement1(securitypermissionid integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$SELECT tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
FROM tblsecurityuser INNER JOIN ((tblsecuritygroup INNER JOIN tblsecurityusermembership ON tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) INNER JOIN tblsecurityvmeasurement ON tblsecuritygroup.securitygroupid = tblsecurityvmeasurement.securitygroupid) ON tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityvmeasurement.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
GROUP BY tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
HAVING (((tblsecurityuser.securityuserid)=$2));$_$;
 q   DROP FUNCTION public.securitygrouppermissivevmeasurement1(securitypermissionid integer, securityuserid integer);
       public       postgres    false    2093            �           0    0 c   FUNCTION securitygrouppermissivevmeasurement1(securitypermissionid integer, securityuserid integer)    ACL     �   GRANT ALL ON FUNCTION public.securitygrouppermissivevmeasurement1(securitypermissionid integer, securityuserid integer) TO "Webgroup";
            public       postgres    false    1621            �           1255    54689 3   securitygrouppermissivevmeasurement1(integer, uuid)    FUNCTION     |  CREATE FUNCTION public.securitygrouppermissivevmeasurement1(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$SELECT tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
FROM tblsecurityuser INNER JOIN ((tblsecuritygroup INNER JOIN tblsecurityusermembership ON tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) INNER JOIN tblsecurityvmeasurement ON tblsecuritygroup.securitygroupid = tblsecurityvmeasurement.securitygroupid) ON tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityvmeasurement.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
GROUP BY tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
HAVING (((tblsecurityuser.securityuserid)=$2));$_$;
 n   DROP FUNCTION public.securitygrouppermissivevmeasurement1(securitypermissionid integer, securityuserid uuid);
       public       tellervo    false    2099            V           1255    54690 6   securitygrouppermissivevmeasurement2(integer, integer)    FUNCTION     �  CREATE FUNCTION public.securitygrouppermissivevmeasurement2(securitypermission integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
from tblsecurityuser inner join ((tblsecuritygroup inner join ((tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecurityvmeasurement on tblsecuritygroup_1.securitygroupid = tblsecurityvmeasurement.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityvmeasurement.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
group by tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
having (((tblsecurityuser.securityuserid)=$2));$_$;
 o   DROP FUNCTION public.securitygrouppermissivevmeasurement2(securitypermission integer, securityuserid integer);
       public       postgres    false    2093            �           0    0 a   FUNCTION securitygrouppermissivevmeasurement2(securitypermission integer, securityuserid integer)    ACL     �   GRANT ALL ON FUNCTION public.securitygrouppermissivevmeasurement2(securitypermission integer, securityuserid integer) TO "Webgroup";
            public       postgres    false    1622            �           1255    54691 3   securitygrouppermissivevmeasurement2(integer, uuid)    FUNCTION     �  CREATE FUNCTION public.securitygrouppermissivevmeasurement2(securitypermission integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
from tblsecurityuser inner join ((tblsecuritygroup inner join ((tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecurityvmeasurement on tblsecuritygroup_1.securitygroupid = tblsecurityvmeasurement.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityvmeasurement.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
group by tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
having (((tblsecurityuser.securityuserid)=$2));$_$;
 l   DROP FUNCTION public.securitygrouppermissivevmeasurement2(securitypermission integer, securityuserid uuid);
       public       tellervo    false    2099            �           1255    54692 6   securitygrouppermissivevmeasurement3(integer, integer)    FUNCTION     �  CREATE FUNCTION public.securitygrouppermissivevmeasurement3(securitypermissionid integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
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
       public       postgres    false    2093            �           0    0 c   FUNCTION securitygrouppermissivevmeasurement3(securitypermissionid integer, securityuserid integer)    ACL     �   GRANT ALL ON FUNCTION public.securitygrouppermissivevmeasurement3(securitypermissionid integer, securityuserid integer) TO "Webgroup";
            public       postgres    false    1426            �           1255    54693 3   securitygrouppermissivevmeasurement3(integer, uuid)    FUNCTION     �  CREATE FUNCTION public.securitygrouppermissivevmeasurement3(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
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
       public       tellervo    false    2099            W           1255    54694 =   securitygrouppermissivevmeasurementcombined(integer, integer)    FUNCTION     �  CREATE FUNCTION public.securitygrouppermissivevmeasurementcombined(securitypermissionid integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$SELECT * from securitygrouppermissivevmeasurement1($1,$2)
UNION
SELECT * from securitygrouppermissivevmeasurement2($1,$2)
UNION SELECT * from securitygrouppermissivevmeasurement3($1,$2);$_$;
 x   DROP FUNCTION public.securitygrouppermissivevmeasurementcombined(securitypermissionid integer, securityuserid integer);
       public       postgres    false    2093            �           0    0 j   FUNCTION securitygrouppermissivevmeasurementcombined(securitypermissionid integer, securityuserid integer)    ACL     �   GRANT ALL ON FUNCTION public.securitygrouppermissivevmeasurementcombined(securitypermissionid integer, securityuserid integer) TO "Webgroup";
            public       postgres    false    1623            �           1255    54695 :   securitygrouppermissivevmeasurementcombined(integer, uuid)    FUNCTION     �  CREATE FUNCTION public.securitygrouppermissivevmeasurementcombined(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$SELECT * from securitygrouppermissivevmeasurement1($1,$2)
UNION
SELECT * from securitygrouppermissivevmeasurement2($1,$2)
UNION SELECT * from securitygrouppermissivevmeasurement3($1,$2);$_$;
 u   DROP FUNCTION public.securitygrouppermissivevmeasurementcombined(securitypermissionid integer, securityuserid uuid);
       public       tellervo    false    2099            �           1255    54696 &   securitygrouprestrictiveelement1(uuid)    FUNCTION     �  CREATE FUNCTION public.securitygrouprestrictiveelement1(securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityelement.elementid
from tblsecurityuser inner join ((tblsecuritygroup inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) inner join tblsecurityelement on tblsecuritygroup.securitygroupid = tblsecurityelement.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityelement.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityelement.elementid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 L   DROP FUNCTION public.securitygrouprestrictiveelement1(securityuserid uuid);
       public       tellervo    false    2099            �           1255    54697 &   securitygrouprestrictiveelement2(uuid)    FUNCTION     �  CREATE FUNCTION public.securitygrouprestrictiveelement2(securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityelement.elementid
from tblsecurityuser inner join ((tblsecuritygroup inner join ((tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecurityelement on tblsecuritygroup_1.securitygroupid = tblsecurityelement.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityelement.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityelement.elementid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 L   DROP FUNCTION public.securitygrouprestrictiveelement2(securityuserid uuid);
       public       tellervo    false    2099            �           1255    54698 &   securitygrouprestrictiveelement3(uuid)    FUNCTION     +  CREATE FUNCTION public.securitygrouprestrictiveelement3(securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityelement.elementid
from tblsecurityuser inner join ((tblsecurityelement inner join (tblsecuritygroup inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 inner join (tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecurityelement.securitygroupid = tblsecuritygroup_2.securitygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityelement.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityelement.elementid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 L   DROP FUNCTION public.securitygrouprestrictiveelement3(securityuserid uuid);
       public       tellervo    false    2099            �           1255    54699 -   securitygrouprestrictiveelementcombined(uuid)    FUNCTION     L  CREATE FUNCTION public.securitygrouprestrictiveelementcombined(securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select * from securitygrouprestrictiveelement1($1)
UNION
select * from securitygrouprestrictiveelement2($1)
UNION SELECT * from securitygrouprestrictiveelement3($1);$_$;
 S   DROP FUNCTION public.securitygrouprestrictiveelementcombined(securityuserid uuid);
       public       tellervo    false    2099            X           1255    54700 (   securitygrouprestrictiveobject1(integer)    FUNCTION     �  CREATE FUNCTION public.securitygrouprestrictiveobject1(securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityobject.objectid
from tblsecurityuser inner join ((tblsecuritygroup inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) inner join tblsecurityobject on tblsecuritygroup.securitygroupid = tblsecurityobject.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityobject.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityobject.objectid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 N   DROP FUNCTION public.securitygrouprestrictiveobject1(securityuserid integer);
       public       postgres    false    2093            �           0    0 @   FUNCTION securitygrouprestrictiveobject1(securityuserid integer)    ACL     d   GRANT ALL ON FUNCTION public.securitygrouprestrictiveobject1(securityuserid integer) TO "Webgroup";
            public       postgres    false    1624            k           1255    54701 %   securitygrouprestrictiveobject1(uuid)    FUNCTION     �  CREATE FUNCTION public.securitygrouprestrictiveobject1(securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityobject.objectid
from tblsecurityuser inner join ((tblsecuritygroup inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) inner join tblsecurityobject on tblsecuritygroup.securitygroupid = tblsecurityobject.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityobject.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityobject.objectid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 K   DROP FUNCTION public.securitygrouprestrictiveobject1(securityuserid uuid);
       public       tellervo    false    2099            Y           1255    54702 (   securitygrouprestrictiveobject2(integer)    FUNCTION     �  CREATE FUNCTION public.securitygrouprestrictiveobject2(securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityobject.objectid
from tblsecurityuser inner join ((tblsecuritygroup inner join ((tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecurityobject on tblsecuritygroup_1.securitygroupid = tblsecurityobject.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityobject.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityobject.objectid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 N   DROP FUNCTION public.securitygrouprestrictiveobject2(securityuserid integer);
       public       postgres    false    2093            �           0    0 @   FUNCTION securitygrouprestrictiveobject2(securityuserid integer)    ACL     d   GRANT ALL ON FUNCTION public.securitygrouprestrictiveobject2(securityuserid integer) TO "Webgroup";
            public       postgres    false    1625            �           1255    54703 %   securitygrouprestrictiveobject2(uuid)    FUNCTION     �  CREATE FUNCTION public.securitygrouprestrictiveobject2(securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityobject.objectid
from tblsecurityuser inner join ((tblsecuritygroup inner join ((tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecurityobject on tblsecuritygroup_1.securitygroupid = tblsecurityobject.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityobject.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityobject.objectid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 K   DROP FUNCTION public.securitygrouprestrictiveobject2(securityuserid uuid);
       public       tellervo    false    2099            �           1255    54704 (   securitygrouprestrictiveobject3(integer)    FUNCTION        CREATE FUNCTION public.securitygrouprestrictiveobject3(securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityobject.objectid
from tblsecurityuser inner join ((tblsecurityobject inner join (tblsecuritygroup inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 inner join (tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecurityobject.securitygroupid = tblsecuritygroup_2.securitygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityobject.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityobject.objectid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 N   DROP FUNCTION public.securitygrouprestrictiveobject3(securityuserid integer);
       public       postgres    false    2093            �           0    0 @   FUNCTION securitygrouprestrictiveobject3(securityuserid integer)    ACL     d   GRANT ALL ON FUNCTION public.securitygrouprestrictiveobject3(securityuserid integer) TO "Webgroup";
            public       postgres    false    1442            �           1255    54705 %   securitygrouprestrictiveobject3(uuid)    FUNCTION     #  CREATE FUNCTION public.securitygrouprestrictiveobject3(securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityobject.objectid
from tblsecurityuser inner join ((tblsecurityobject inner join (tblsecuritygroup inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 inner join (tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecurityobject.securitygroupid = tblsecuritygroup_2.securitygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityobject.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityobject.objectid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 K   DROP FUNCTION public.securitygrouprestrictiveobject3(securityuserid uuid);
       public       tellervo    false    2099            �           1255    54706 /   securitygrouprestrictiveobjectcombined(integer)    FUNCTION     E  CREATE FUNCTION public.securitygrouprestrictiveobjectcombined(securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select * from securitygrouprestrictiveobject1($1)
UNION
select * from securitygrouprestrictiveobject2($1)
UNION SELECT * from securitygrouprestrictiveobject3($1);$_$;
 U   DROP FUNCTION public.securitygrouprestrictiveobjectcombined(securityuserid integer);
       public       postgres    false    2093            �           0    0 G   FUNCTION securitygrouprestrictiveobjectcombined(securityuserid integer)    ACL     k   GRANT ALL ON FUNCTION public.securitygrouprestrictiveobjectcombined(securityuserid integer) TO "Webgroup";
            public       postgres    false    1443            �           1255    54707 ,   securitygrouprestrictiveobjectcombined(uuid)    FUNCTION     H  CREATE FUNCTION public.securitygrouprestrictiveobjectcombined(securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select * from securitygrouprestrictiveobject1($1)
UNION
select * from securitygrouprestrictiveobject2($1)
UNION SELECT * from securitygrouprestrictiveobject3($1);$_$;
 R   DROP FUNCTION public.securitygrouprestrictiveobjectcombined(securityuserid uuid);
       public       tellervo    false    2099            �           1255    54708 &   securitygrouprestrictivetree1(integer)    FUNCTION     �  CREATE FUNCTION public.securitygrouprestrictivetree1(securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecuritytree.elementid
from tblsecurityuser inner join ((tblsecuritygroup inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) inner join tblsecuritytree on tblsecuritygroup.securitygroupid = tblsecuritytree.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecuritytree.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecuritytree.elementid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 L   DROP FUNCTION public.securitygrouprestrictivetree1(securityuserid integer);
       public       postgres    false    2093            �           0    0 >   FUNCTION securitygrouprestrictivetree1(securityuserid integer)    ACL     b   GRANT ALL ON FUNCTION public.securitygrouprestrictivetree1(securityuserid integer) TO "Webgroup";
            public       postgres    false    1444            �           1255    54709 &   securitygrouprestrictivetree2(integer)    FUNCTION     �  CREATE FUNCTION public.securitygrouprestrictivetree2(securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecuritytree.elementid
from tblsecurityuser inner join ((tblsecuritygroup inner join ((tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecuritytree on tblsecuritygroup_1.securitygroupid = tblsecuritytree.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecuritytree.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecuritytree.elementid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 L   DROP FUNCTION public.securitygrouprestrictivetree2(securityuserid integer);
       public       postgres    false    2093            �           0    0 >   FUNCTION securitygrouprestrictivetree2(securityuserid integer)    ACL     b   GRANT ALL ON FUNCTION public.securitygrouprestrictivetree2(securityuserid integer) TO "Webgroup";
            public       postgres    false    1445            Z           1255    54710 &   securitygrouprestrictivetree3(integer)    FUNCTION       CREATE FUNCTION public.securitygrouprestrictivetree3(securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecuritytree.elementid
from tblsecurityuser inner join ((tblsecuritytree inner join (tblsecuritygroup inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 inner join (tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecuritytree.securitygroupid = tblsecuritygroup_2.securitygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecuritytree.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecuritytree.elementid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 L   DROP FUNCTION public.securitygrouprestrictivetree3(securityuserid integer);
       public       postgres    false    2093            �           0    0 >   FUNCTION securitygrouprestrictivetree3(securityuserid integer)    ACL     b   GRANT ALL ON FUNCTION public.securitygrouprestrictivetree3(securityuserid integer) TO "Webgroup";
            public       postgres    false    1626            �           1255    54711 -   securitygrouprestrictivetreecombined(integer)    FUNCTION     =  CREATE FUNCTION public.securitygrouprestrictivetreecombined(securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select * from securitygrouprestrictivetree1($1)
UNION
select * from securitygrouprestrictivetree2($1)
UNION SELECT * from securitygrouprestrictivetree3($1);$_$;
 S   DROP FUNCTION public.securitygrouprestrictivetreecombined(securityuserid integer);
       public       postgres    false    2093            �           0    0 E   FUNCTION securitygrouprestrictivetreecombined(securityuserid integer)    ACL     i   GRANT ALL ON FUNCTION public.securitygrouprestrictivetreecombined(securityuserid integer) TO "Webgroup";
            public       postgres    false    1456            �           1255    54712 .   securitygrouprestrictivevmeasurement1(integer)    FUNCTION       CREATE FUNCTION public.securitygrouprestrictivevmeasurement1(securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
from tblsecurityuser inner join ((tblsecuritygroup inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) inner join tblsecurityvmeasurement on tblsecuritygroup.securitygroupid = tblsecurityvmeasurement.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityvmeasurement.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 T   DROP FUNCTION public.securitygrouprestrictivevmeasurement1(securityuserid integer);
       public       postgres    false    2093            �           0    0 F   FUNCTION securitygrouprestrictivevmeasurement1(securityuserid integer)    ACL     j   GRANT ALL ON FUNCTION public.securitygrouprestrictivevmeasurement1(securityuserid integer) TO "Webgroup";
            public       postgres    false    1457            �           1255    54713 +   securitygrouprestrictivevmeasurement1(uuid)    FUNCTION       CREATE FUNCTION public.securitygrouprestrictivevmeasurement1(securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
from tblsecurityuser inner join ((tblsecuritygroup inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) inner join tblsecurityvmeasurement on tblsecuritygroup.securitygroupid = tblsecurityvmeasurement.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityvmeasurement.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 Q   DROP FUNCTION public.securitygrouprestrictivevmeasurement1(securityuserid uuid);
       public       tellervo    false    2099            �           1255    54714 .   securitygrouprestrictivevmeasurement2(integer)    FUNCTION       CREATE FUNCTION public.securitygrouprestrictivevmeasurement2(securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
from tblsecurityuser inner join ((tblsecuritygroup inner join ((tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecurityvmeasurement on tblsecuritygroup_1.securitygroupid = tblsecurityvmeasurement.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityvmeasurement.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 T   DROP FUNCTION public.securitygrouprestrictivevmeasurement2(securityuserid integer);
       public       postgres    false    2093            �           0    0 F   FUNCTION securitygrouprestrictivevmeasurement2(securityuserid integer)    ACL     j   GRANT ALL ON FUNCTION public.securitygrouprestrictivevmeasurement2(securityuserid integer) TO "Webgroup";
            public       postgres    false    1458            �           1255    54715 +   securitygrouprestrictivevmeasurement2(uuid)    FUNCTION     "  CREATE FUNCTION public.securitygrouprestrictivevmeasurement2(securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
from tblsecurityuser inner join ((tblsecuritygroup inner join ((tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecurityvmeasurement on tblsecuritygroup_1.securitygroupid = tblsecurityvmeasurement.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityvmeasurement.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 Q   DROP FUNCTION public.securitygrouprestrictivevmeasurement2(securityuserid uuid);
       public       tellervo    false    2099            [           1255    54716 .   securitygrouprestrictivevmeasurement3(integer)    FUNCTION     P  CREATE FUNCTION public.securitygrouprestrictivevmeasurement3(securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
from tblsecurityuser inner join ((tblsecurityvmeasurement inner join (tblsecuritygroup inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 inner join (tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecurityvmeasurement.securitygroupid = tblsecuritygroup_2.securitygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityvmeasurement.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 T   DROP FUNCTION public.securitygrouprestrictivevmeasurement3(securityuserid integer);
       public       postgres    false    2093            �           0    0 F   FUNCTION securitygrouprestrictivevmeasurement3(securityuserid integer)    ACL     j   GRANT ALL ON FUNCTION public.securitygrouprestrictivevmeasurement3(securityuserid integer) TO "Webgroup";
            public       postgres    false    1627            �           1255    54717 +   securitygrouprestrictivevmeasurement3(uuid)    FUNCTION     S  CREATE FUNCTION public.securitygrouprestrictivevmeasurement3(securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
from tblsecurityuser inner join ((tblsecurityvmeasurement inner join (tblsecuritygroup inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 inner join (tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecurityvmeasurement.securitygroupid = tblsecuritygroup_2.securitygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityvmeasurement.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
having (((tblsecurityuser.securityuserid)=$1));$_$;
 Q   DROP FUNCTION public.securitygrouprestrictivevmeasurement3(securityuserid uuid);
       public       tellervo    false    2099            �           1255    54718 5   securitygrouprestrictivevmeasurementcombined(integer)    FUNCTION     ]  CREATE FUNCTION public.securitygrouprestrictivevmeasurementcombined(securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select * from securitygrouprestrictivevmeasurement1($1)
UNION
select * from securitygrouprestrictivevmeasurement2($1)
UNION SELECT * from securitygrouprestrictivevmeasurement3($1);$_$;
 [   DROP FUNCTION public.securitygrouprestrictivevmeasurementcombined(securityuserid integer);
       public       postgres    false    2093            �           0    0 M   FUNCTION securitygrouprestrictivevmeasurementcombined(securityuserid integer)    ACL     q   GRANT ALL ON FUNCTION public.securitygrouprestrictivevmeasurementcombined(securityuserid integer) TO "Webgroup";
            public       postgres    false    1468            �           1255    54719 2   securitygrouprestrictivevmeasurementcombined(uuid)    FUNCTION     `  CREATE FUNCTION public.securitygrouprestrictivevmeasurementcombined(securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select * from securitygrouprestrictivevmeasurement1($1)
UNION
select * from securitygrouprestrictivevmeasurement2($1)
UNION SELECT * from securitygrouprestrictivevmeasurement3($1);$_$;
 X   DROP FUNCTION public.securitygrouprestrictivevmeasurementcombined(securityuserid uuid);
       public       tellervo    false    2099            �           1255    54720    securitygroupsbyuser(integer)    FUNCTION     �   CREATE FUNCTION public.securitygroupsbyuser(securityuserid integer) RETURNS SETOF integer
    LANGUAGE sql
    AS $_$SELECT * from securitygroupsbyuser1($1)
UNION
SELECT * from securitygroupsbyuser2($1)
UNION SELECT * from securitygroupsbyuser3($1)$_$;
 C   DROP FUNCTION public.securitygroupsbyuser(securityuserid integer);
       public       postgres    false            �           0    0 5   FUNCTION securitygroupsbyuser(securityuserid integer)    ACL     Y   GRANT ALL ON FUNCTION public.securitygroupsbyuser(securityuserid integer) TO "Webgroup";
            public       postgres    false    1469            �           1255    54721    securitygroupsbyuser(uuid)    FUNCTION     �   CREATE FUNCTION public.securitygroupsbyuser(securityuserid uuid) RETURNS SETOF integer
    LANGUAGE sql
    AS $_$SELECT * from securitygroupsbyuser1($1)
UNION
SELECT * from securitygroupsbyuser2($1)
UNION SELECT * from securitygroupsbyuser3($1)$_$;
 @   DROP FUNCTION public.securitygroupsbyuser(securityuserid uuid);
       public       tellervo    false            �           1255    54722    securitygroupsbyuser1(integer)    FUNCTION     �  CREATE FUNCTION public.securitygroupsbyuser1(securityuserid integer) RETURNS SETOF integer
    LANGUAGE sql
    AS $_$SELECT tblsecuritygroup.securitygroupid
FROM tblsecurityuser INNER JOIN (tblsecuritygroup INNER JOIN
tblsecurityusermembership ON
tblsecuritygroup.securitygroupid=tblsecurityusermembership.securitygroupid)
ON tblsecurityuser.securityuserid=tblsecurityusermembership.securityuserid
WHERE (((tblsecurityuser.securityuserid)=$1));$_$;
 D   DROP FUNCTION public.securitygroupsbyuser1(securityuserid integer);
       public       postgres    false            �           0    0 6   FUNCTION securitygroupsbyuser1(securityuserid integer)    ACL     Z   GRANT ALL ON FUNCTION public.securitygroupsbyuser1(securityuserid integer) TO "Webgroup";
            public       postgres    false    1470            �           1255    54723    securitygroupsbyuser1(uuid)    FUNCTION     �  CREATE FUNCTION public.securitygroupsbyuser1(securityuserid uuid) RETURNS SETOF integer
    LANGUAGE sql
    AS $_$SELECT tblsecuritygroup.securitygroupid
FROM tblsecurityuser INNER JOIN (tblsecuritygroup INNER JOIN
tblsecurityusermembership ON
tblsecuritygroup.securitygroupid=tblsecurityusermembership.securitygroupid)
ON tblsecurityuser.securityuserid=tblsecurityusermembership.securityuserid
WHERE (((tblsecurityuser.securityuserid)=$1));$_$;
 A   DROP FUNCTION public.securitygroupsbyuser1(securityuserid uuid);
       public       tellervo    false            �           1255    54724    securitygroupsbyuser2(integer)    FUNCTION     �  CREATE FUNCTION public.securitygroupsbyuser2(securityuserid integer) RETURNS SETOF integer
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
       public       postgres    false            �           0    0 6   FUNCTION securitygroupsbyuser2(securityuserid integer)    ACL     Z   GRANT ALL ON FUNCTION public.securitygroupsbyuser2(securityuserid integer) TO "Webgroup";
            public       postgres    false    1471            �           1255    54725    securitygroupsbyuser2(uuid)    FUNCTION     �  CREATE FUNCTION public.securitygroupsbyuser2(securityuserid uuid) RETURNS SETOF integer
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
       public       tellervo    false            ]           1255    54726    securitygroupsbyuser3(integer)    FUNCTION       CREATE FUNCTION public.securitygroupsbyuser3(securityuserid integer) RETURNS SETOF integer
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
       public       postgres    false            �           0    0 6   FUNCTION securitygroupsbyuser3(securityuserid integer)    ACL     Z   GRANT ALL ON FUNCTION public.securitygroupsbyuser3(securityuserid integer) TO "Webgroup";
            public       postgres    false    1629            �           1255    54727    securitygroupsbyuser3(uuid)    FUNCTION       CREATE FUNCTION public.securitygroupsbyuser3(securityuserid uuid) RETURNS SETOF integer
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
       public       tellervo    false            �           1255    54728 )   securitygrouptreemaster(integer, integer)    FUNCTION     �  CREATE FUNCTION public.securitygrouptreemaster(securitypermissionid integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select securitygrouppermissivetreecombined.* 
from securitygrouppermissivetreecombined($1, $2) left join securitygrouprestrictivetreecombined($2) on securitygrouppermissivetreecombined.objectid = securitygrouprestrictivetreecombined.objectid 
where (((securitygrouprestrictivetreecombined.objectid) is null));$_$;
 d   DROP FUNCTION public.securitygrouptreemaster(securitypermissionid integer, securityuserid integer);
       public       postgres    false    2093            �           0    0 V   FUNCTION securitygrouptreemaster(securitypermissionid integer, securityuserid integer)    ACL     z   GRANT ALL ON FUNCTION public.securitygrouptreemaster(securitypermissionid integer, securityuserid integer) TO "Webgroup";
            public       postgres    false    1485            �           1255    54729 1   securitygroupvmeasurementmaster(integer, integer)    FUNCTION       CREATE FUNCTION public.securitygroupvmeasurementmaster(securitypermissionid integer, securityuserid integer) RETURNS SETOF public.securityuserandobjectid
    LANGUAGE sql
    AS $_$select securitygrouppermissivevmeasurementcombined.* 
from securitygrouppermissivevmeasurementcombined($1, $2) left join securitygrouprestrictivevmeasurementcombined($2) on securitygrouppermissivevmeasurementcombined.objectid = securitygrouprestrictivevmeasurementcombined.objectid 
where (((securitygrouprestrictivevmeasurementcombined.objectid) is null));$_$;
 l   DROP FUNCTION public.securitygroupvmeasurementmaster(securitypermissionid integer, securityuserid integer);
       public       postgres    false    2093            �           0    0 ^   FUNCTION securitygroupvmeasurementmaster(securitypermissionid integer, securityuserid integer)    ACL     �   GRANT ALL ON FUNCTION public.securitygroupvmeasurementmaster(securitypermissionid integer, securityuserid integer) TO "Webgroup";
            public       postgres    false    1486            �           1255    54730 .   securitygroupvmeasurementmaster(integer, uuid)    FUNCTION     "  CREATE FUNCTION public.securitygroupvmeasurementmaster(securitypermissionid integer, securityuserid uuid) RETURNS SETOF public.securityuseruuidandobjectuuid
    LANGUAGE sql
    AS $_$select securitygrouppermissivevmeasurementcombined.* 
from securitygrouppermissivevmeasurementcombined($1, $2) left join securitygrouprestrictivevmeasurementcombined($2) on securitygrouppermissivevmeasurementcombined.objectid = securitygrouprestrictivevmeasurementcombined.objectid 
where (((securitygrouprestrictivevmeasurementcombined.objectid) is null));$_$;
 i   DROP FUNCTION public.securitygroupvmeasurementmaster(securitypermissionid integer, securityuserid uuid);
       public       tellervo    false    2099            �           1255    54731 ,   securitypermselement(uuid, integer, integer)    FUNCTION     �  CREATE FUNCTION public.securitypermselement(securityuserid uuid, securitypermissionid integer, elementid integer) RETURNS boolean
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
       public       tellervo    false            ^           1255    54732 .   securitypermsobject(integer, integer, integer)    FUNCTION     �  CREATE FUNCTION public.securitypermsobject(securityuserid integer, securitypermissionid integer, securityobjectid integer) RETURNS boolean
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
       public       postgres    false            �           0    0 l   FUNCTION securitypermsobject(securityuserid integer, securitypermissionid integer, securityobjectid integer)    ACL     �   GRANT ALL ON FUNCTION public.securitypermsobject(securityuserid integer, securitypermissionid integer, securityobjectid integer) TO "Webgroup";
            public       postgres    false    1630            �           1255    54733 +   securitypermsobject(uuid, integer, integer)    FUNCTION     �  CREATE FUNCTION public.securitypermsobject(securityuserid uuid, securitypermissionid integer, securityobjectid integer) RETURNS boolean
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
       public       tellervo    false            �           1255    54734 ,   securitypermstree(integer, integer, integer)    FUNCTION     �  CREATE FUNCTION public.securitypermstree(securityuserid integer, securitypermissionid integer, elementid integer) RETURNS boolean
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
       public       postgres    false            �           0    0 c   FUNCTION securitypermstree(securityuserid integer, securitypermissionid integer, elementid integer)    ACL     �   GRANT ALL ON FUNCTION public.securitypermstree(securityuserid integer, securitypermissionid integer, elementid integer) TO "Webgroup";
            public       postgres    false    1502            �           1255    54735 -   segmentize(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.segmentize(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_segmentize2d';
 D   DROP FUNCTION public.segmentize(public.geometry, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54736 3   setpoint(public.geometry, integer, public.geometry)    FUNCTION     �   CREATE FUNCTION public.setpoint(public.geometry, integer, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_setpoint_linestring';
 J   DROP FUNCTION public.setpoint(public.geometry, integer, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54737 !   setsrid(public.geometry, integer)    FUNCTION     �   CREATE FUNCTION public.setsrid(public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_set_srid';
 8   DROP FUNCTION public.setsrid(public.geometry, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54738     shift_longitude(public.geometry)    FUNCTION     �   CREATE FUNCTION public.shift_longitude(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_longitude_shift';
 7   DROP FUNCTION public.shift_longitude(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54739 +   simplify(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.simplify(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_simplify2d';
 B   DROP FUNCTION public.simplify(public.geometry, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54740 -   snaptogrid(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.snaptogrid(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_SnapToGrid($1, 0, 0, $2, $2)$_$;
 D   DROP FUNCTION public.snaptogrid(public.geometry, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54741 ?   snaptogrid(public.geometry, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.snaptogrid(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_SnapToGrid($1, 0, 0, $2, $3)$_$;
 V   DROP FUNCTION public.snaptogrid(public.geometry, double precision, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54742 c   snaptogrid(public.geometry, double precision, double precision, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.snaptogrid(public.geometry, double precision, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_snaptogrid';
 z   DROP FUNCTION public.snaptogrid(public.geometry, double precision, double precision, double precision, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54743 t   snaptogrid(public.geometry, public.geometry, double precision, double precision, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.snaptogrid(public.geometry, public.geometry, double precision, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_snaptogrid_pointoff';
 �   DROP FUNCTION public.snaptogrid(public.geometry, public.geometry, double precision, double precision, double precision, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54744    srid(public.geometry)    FUNCTION     �   CREATE FUNCTION public.srid(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_get_srid';
 ,   DROP FUNCTION public.srid(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54745 6   st_3dlength_spheroid(public.geometry, public.spheroid)    FUNCTION     �   CREATE FUNCTION public.st_3dlength_spheroid(public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_LengthSpheroid($1,$2);$_$;
 M   DROP FUNCTION public.st_3dlength_spheroid(public.geometry, public.spheroid);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2            �           0    0 !   FUNCTION st_area(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_area(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_area(public.geometry) TO "Webgroup";
            public       postgres    false    1631            �           1255    54746    st_asbinary(text)    FUNCTION     �   CREATE FUNCTION public.st_asbinary(text) RETURNS bytea
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$ SELECT ST_AsBinary($1::geometry);$_$;
 (   DROP FUNCTION public.st_asbinary(text);
       public       tellervo    false            �           0    0 %   FUNCTION st_asbinary(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_asbinary(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_asbinary(public.geometry) TO "Webgroup";
            public       postgres    false    1632            �           1255    54747 9   st_asgeojson(integer, public.geography, integer, integer)    FUNCTION     �   CREATE FUNCTION public.st_asgeojson(version integer, geog public.geography, maxdecimaldigits integer DEFAULT 15, options integer DEFAULT 0) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$ SELECT ST_AsGeoJson($2::geometry,$3,$4); $_$;
 v   DROP FUNCTION public.st_asgeojson(version integer, geog public.geography, maxdecimaldigits integer, options integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54748 8   st_asgeojson(integer, public.geometry, integer, integer)    FUNCTION     �   CREATE FUNCTION public.st_asgeojson(version integer, geog public.geometry, maxdecimaldigits integer DEFAULT 15, options integer DEFAULT 0) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$ SELECT ST_AsGeoJson($2::geometry,15,0); $_$;
 u   DROP FUNCTION public.st_asgeojson(version integer, geog public.geometry, maxdecimaldigits integer, options integer);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54749 2   st_askml(integer, public.geography, integer, text)    FUNCTION     �   CREATE FUNCTION public.st_askml(version integer, geom public.geography, maxdecimaldigits integer DEFAULT 15, nprefix text DEFAULT ''::text) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$ SELECT ST_AsKML($2::geometry,$3,$4); $_$;
 o   DROP FUNCTION public.st_askml(version integer, geom public.geography, maxdecimaldigits integer, nprefix text);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54750 1   st_askml(integer, public.geometry, integer, text)    FUNCTION     �   CREATE FUNCTION public.st_askml(version integer, geom public.geometry, maxdecimaldigits integer DEFAULT 15, nprefix text DEFAULT ''::text) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$ SELECT ST_AsKML($2::geometry,$3,$4); $_$;
 n   DROP FUNCTION public.st_askml(version integer, geom public.geometry, maxdecimaldigits integer, nprefix text);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54751    st_astext(bytea)    FUNCTION     �   CREATE FUNCTION public.st_astext(bytea) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$ SELECT ST_AsText($1::geometry);$_$;
 '   DROP FUNCTION public.st_astext(bytea);
       public       pbrewer    false            �           0    0 #   FUNCTION st_astext(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_astext(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_astext(public.geometry) TO "Webgroup";
            public       postgres    false    1633            �           0    0 %   FUNCTION st_boundary(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_boundary(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_boundary(public.geometry) TO "Webgroup";
            public       postgres    false    1634            �           1255    54752    st_box(public.box3d)    FUNCTION     �   CREATE FUNCTION public.st_box(public.box3d) RETURNS box
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_to_BOX';
 +   DROP FUNCTION public.st_box(public.box3d);
       public       tellervo    false    2    2    2            �           1255    54753    st_box(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_box(public.geometry) RETURNS box
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_to_BOX';
 .   DROP FUNCTION public.st_box(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54754    st_box2d(public.box3d)    FUNCTION     �   CREATE FUNCTION public.st_box2d(public.box3d) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_to_BOX2D';
 -   DROP FUNCTION public.st_box2d(public.box3d);
       public       tellervo    false    2    2    2    2    2    2            �           1255    54755    st_box2d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_box2d(public.geometry) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_to_BOX2D';
 0   DROP FUNCTION public.st_box2d(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2            �           1255    54756    st_box3d(public.box2d)    FUNCTION     �   CREATE FUNCTION public.st_box3d(public.box2d) RETURNS public.box3d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX2D_to_BOX3D';
 -   DROP FUNCTION public.st_box3d(public.box2d);
       public       tellervo    false    2    2    2    2    2    2            �           1255    54757    st_box3d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_box3d(public.geometry) RETURNS public.box3d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_to_BOX3D';
 0   DROP FUNCTION public.st_box3d(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2            �           1255    54758    st_box3d_in(cstring)    FUNCTION     �   CREATE FUNCTION public.st_box3d_in(cstring) RETURNS public.box3d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_in';
 +   DROP FUNCTION public.st_box3d_in(cstring);
       public       tellervo    false    2    2    2            �           1255    54759    st_box3d_out(public.box3d)    FUNCTION     �   CREATE FUNCTION public.st_box3d_out(public.box3d) RETURNS cstring
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_out';
 1   DROP FUNCTION public.st_box3d_out(public.box3d);
       public       tellervo    false    2    2    2            �           1255    54760    st_bytea(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_bytea(public.geometry) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_to_bytea';
 0   DROP FUNCTION public.st_bytea(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           0    0 %   FUNCTION st_centroid(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_centroid(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_centroid(public.geometry) TO "Webgroup";
            public       postgres    false    1635            �           1255    54761 .   st_combine_bbox(public.box2d, public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_combine_bbox(public.box2d, public.geometry) RETURNS public.box2d
    LANGUAGE sql IMMUTABLE
    AS $_$SELECT ST_CombineBbox($1,$2);$_$;
 E   DROP FUNCTION public.st_combine_bbox(public.box2d, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54762 .   st_combine_bbox(public.box3d, public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_combine_bbox(public.box3d, public.geometry) RETURNS public.box3d
    LANGUAGE sql IMMUTABLE
    AS $_$SELECT ST_CombineBbox($1,$2);$_$;
 E   DROP FUNCTION public.st_combine_bbox(public.box3d, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           0    0 B   FUNCTION st_contains(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_contains(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_contains(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    1636            �           0    0 '   FUNCTION st_convexhull(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_convexhull(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_convexhull(public.geometry) TO "Webgroup";
            public       postgres    false    1637            �           0    0 .   FUNCTION st_coorddim(geometry public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_coorddim(geometry public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_coorddim(geometry public.geometry) TO "Webgroup";
            public       postgres    false    1638            �           0    0 A   FUNCTION st_crosses(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_crosses(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_crosses(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    1639            �           0    0 D   FUNCTION st_difference(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_difference(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_difference(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    1640            �           0    0 &   FUNCTION st_dimension(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_dimension(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_dimension(public.geometry) TO "Webgroup";
            public       postgres    false    1641            �           0    0 B   FUNCTION st_disjoint(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_disjoint(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_disjoint(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    1642            �           0    0 B   FUNCTION st_distance(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_distance(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_distance(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    1643            �           1255    54763 4   st_distance_sphere(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_distance_sphere(geom1 public.geometry, geom2 public.geometry) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_DistanceSphere($1,$2);$_$;
 W   DROP FUNCTION public.st_distance_sphere(geom1 public.geometry, geom2 public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54764 G   st_distance_spheroid(public.geometry, public.geometry, public.spheroid)    FUNCTION     �   CREATE FUNCTION public.st_distance_spheroid(geom1 public.geometry, geom2 public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_DistanceSpheroid($1,$2,$3);$_$;
 j   DROP FUNCTION public.st_distance_spheroid(geom1 public.geometry, geom2 public.geometry, public.spheroid);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           0    0 %   FUNCTION st_endpoint(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_endpoint(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_endpoint(public.geometry) TO "Webgroup";
            public       postgres    false    823            �           0    0 %   FUNCTION st_envelope(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_envelope(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_envelope(public.geometry) TO "Webgroup";
            public       postgres    false    1644            �           0    0 @   FUNCTION st_equals(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_equals(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_equals(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    757            �           1255    54765    st_estimated_extent(text, text)    FUNCTION     R  CREATE FUNCTION public.st_estimated_extent(text, text) RETURNS public.box2d
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	-- We use security invoker instead of security definer
	-- to prevent malicious injection of a same named different function
	-- that would be run under elevated permissions
	SELECT ST_EstimatedExtent($1, $2);
	$_$;
 6   DROP FUNCTION public.st_estimated_extent(text, text);
       public       tellervo    false    2    2    2            �           1255    54766 %   st_estimated_extent(text, text, text)    FUNCTION     +  CREATE FUNCTION public.st_estimated_extent(text, text, text) RETURNS public.box2d
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$
	-- We use security invoker instead of security definer
	-- to prevent malicious injection of a different same named function
	SELECT ST_EstimatedExtent($1, $2, $3);
	$_$;
 <   DROP FUNCTION public.st_estimated_extent(text, text, text);
       public       tellervo    false    2    2    2            �           0    0 )   FUNCTION st_exteriorring(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_exteriorring(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_exteriorring(public.geometry) TO "Webgroup";
            public       postgres    false    1645            �           1255    54767    st_find_extent(text, text)    FUNCTION     �   CREATE FUNCTION public.st_find_extent(text, text) RETURNS public.box2d
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_FindExtent($1,$2);$_$;
 1   DROP FUNCTION public.st_find_extent(text, text);
       public       tellervo    false    2    2    2            �           1255    54768     st_find_extent(text, text, text)    FUNCTION     �   CREATE FUNCTION public.st_find_extent(text, text, text) RETURNS public.box2d
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_FindExtent($1,$2,$3);$_$;
 7   DROP FUNCTION public.st_find_extent(text, text, text);
       public       tellervo    false    2    2    2            �           1255    54769    st_force_2d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_force_2d(public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_Force2D($1);$_$;
 3   DROP FUNCTION public.st_force_2d(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54770    st_force_3d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_force_3d(public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_Force3D($1);$_$;
 3   DROP FUNCTION public.st_force_3d(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54771    st_force_3dm(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_force_3dm(public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_Force3DM($1);$_$;
 4   DROP FUNCTION public.st_force_3dm(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54772    st_force_3dz(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_force_3dz(public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_Force3DZ($1);$_$;
 4   DROP FUNCTION public.st_force_3dz(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54773    st_force_4d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_force_4d(public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_Force4D($1);$_$;
 3   DROP FUNCTION public.st_force_4d(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54774 $   st_force_collection(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_force_collection(public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_ForceCollection($1);$_$;
 ;   DROP FUNCTION public.st_force_collection(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54775    st_geometry(bytea)    FUNCTION     �   CREATE FUNCTION public.st_geometry(bytea) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_from_bytea';
 )   DROP FUNCTION public.st_geometry(bytea);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54776    st_geometry(text)    FUNCTION     �   CREATE FUNCTION public.st_geometry(text) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'parse_WKT_lwgeom';
 (   DROP FUNCTION public.st_geometry(text);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54777    st_geometry(public.box2d)    FUNCTION     �   CREATE FUNCTION public.st_geometry(public.box2d) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX2D_to_LWGEOM';
 0   DROP FUNCTION public.st_geometry(public.box2d);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2            �           1255    54778    st_geometry(public.box3d)    FUNCTION     �   CREATE FUNCTION public.st_geometry(public.box3d) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_to_LWGEOM';
 0   DROP FUNCTION public.st_geometry(public.box3d);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2            �           1255    54779 1   st_geometry_cmp(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_geometry_cmp(public.geometry, public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'lwgeom_cmp';
 H   DROP FUNCTION public.st_geometry_cmp(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54780 0   st_geometry_eq(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_geometry_eq(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'lwgeom_eq';
 G   DROP FUNCTION public.st_geometry_eq(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54781 0   st_geometry_ge(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_geometry_ge(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'lwgeom_ge';
 G   DROP FUNCTION public.st_geometry_ge(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54782 0   st_geometry_gt(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_geometry_gt(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'lwgeom_gt';
 G   DROP FUNCTION public.st_geometry_gt(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54783 0   st_geometry_le(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_geometry_le(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'lwgeom_le';
 G   DROP FUNCTION public.st_geometry_le(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54784 0   st_geometry_lt(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_geometry_lt(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'lwgeom_lt';
 G   DROP FUNCTION public.st_geometry_lt(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           0    0 /   FUNCTION st_geometryn(public.geometry, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_geometryn(public.geometry, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_geometryn(public.geometry, integer) TO "Webgroup";
            public       postgres    false    1646            �           0    0 )   FUNCTION st_geometrytype(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_geometrytype(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_geometrytype(public.geometry) TO "Webgroup";
            public       postgres    false    1647            �           0    0 '   FUNCTION st_geomfromtext(text, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_geomfromtext(text, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_geomfromtext(text, integer) TO "Webgroup";
            public       postgres    false    824            �           0    0 '   FUNCTION st_geomfromwkb(bytea, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_geomfromwkb(bytea, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_geomfromwkb(bytea, integer) TO "Webgroup";
            public       postgres    false    1615            �           0    0 3   FUNCTION st_interiorringn(public.geometry, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_interiorringn(public.geometry, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_interiorringn(public.geometry, integer) TO "Webgroup";
            public       postgres    false    825            �           0    0 F   FUNCTION st_intersection(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_intersection(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_intersection(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    694            �           0    0 D   FUNCTION st_intersects(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_intersects(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_intersects(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    758            �           0    0 %   FUNCTION st_isclosed(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_isclosed(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_isclosed(public.geometry) TO "Webgroup";
            public       postgres    false    826            �           0    0 $   FUNCTION st_isempty(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_isempty(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_isempty(public.geometry) TO "Webgroup";
            public       postgres    false    827            �           0    0 #   FUNCTION st_isring(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_isring(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_isring(public.geometry) TO "Webgroup";
            public       postgres    false    759            �           0    0 %   FUNCTION st_issimple(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_issimple(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_issimple(public.geometry) TO "Webgroup";
            public       postgres    false    1616            �           0    0 $   FUNCTION st_isvalid(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_isvalid(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_isvalid(public.geometry) TO "Webgroup";
            public       postgres    false    760            �           0    0 #   FUNCTION st_length(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_length(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_length(public.geometry) TO "Webgroup";
            public       postgres    false    540            r           1255    54785 6   st_length2d_spheroid(public.geometry, public.spheroid)    FUNCTION     �   CREATE FUNCTION public.st_length2d_spheroid(public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_Length2DSpheroid($1,$2);$_$;
 M   DROP FUNCTION public.st_length2d_spheroid(public.geometry, public.spheroid);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2            s           1255    54786    st_length3d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_length3d(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_length_linestring';
 3   DROP FUNCTION public.st_length3d(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            t           1255    54787 4   st_length_spheroid(public.geometry, public.spheroid)    FUNCTION     �   CREATE FUNCTION public.st_length_spheroid(public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_LengthSpheroid($1,$2);$_$;
 K   DROP FUNCTION public.st_length_spheroid(public.geometry, public.spheroid);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2            u           1255    54788 6   st_length_spheroid3d(public.geometry, public.spheroid)    FUNCTION     �   CREATE FUNCTION public.st_length_spheroid3d(public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'LWGEOM_length_ellipsoid_linestring';
 M   DROP FUNCTION public.st_length_spheroid3d(public.geometry, public.spheroid);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2            v           1255    54789 <   st_line_interpolate_point(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.st_line_interpolate_point(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_LineInterpolatePoint($1, $2);$_$;
 S   DROP FUNCTION public.st_line_interpolate_point(public.geometry, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            w           1255    54790 6   st_line_locate_point(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_line_locate_point(geom1 public.geometry, geom2 public.geometry) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_LineLocatePoint($1, $2);$_$;
 Y   DROP FUNCTION public.st_line_locate_point(geom1 public.geometry, geom2 public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            x           1255    54791 F   st_line_substring(public.geometry, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.st_line_substring(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_LineSubstring($1, $2, $3);$_$;
 ]   DROP FUNCTION public.st_line_substring(public.geometry, double precision, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           0    0 '   FUNCTION st_linefromtext(text, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_linefromtext(text, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_linefromtext(text, integer) TO "Webgroup";
            public       postgres    false    1617            �           0    0 '   FUNCTION st_linefromwkb(bytea, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_linefromwkb(bytea, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_linefromwkb(bytea, integer) TO "Webgroup";
            public       postgres    false    1618            y           1255    54792 :   st_locate_along_measure(public.geometry, double precision)    FUNCTION     �   CREATE FUNCTION public.st_locate_along_measure(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_LocateBetween($1, $2, $2);$_$;
 Q   DROP FUNCTION public.st_locate_along_measure(public.geometry, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            z           1255    54793 O   st_locate_between_measures(public.geometry, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.st_locate_between_measures(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_LocateBetween($1, $2, $2);$_$;
 f   DROP FUNCTION public.st_locate_between_measures(public.geometry, double precision, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            {           1255    54794 .   st_makebox3d(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_makebox3d(public.geometry, public.geometry) RETURNS public.box3d
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_construct';
 E   DROP FUNCTION public.st_makebox3d(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54795 %   st_makeline_garray(public.geometry[])    FUNCTION     �   CREATE FUNCTION public.st_makeline_garray(public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_makeline_garray';
 <   DROP FUNCTION public.st_makeline_garray(public.geometry[]);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54796    st_mem_size(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_mem_size(public.geometry) RETURNS integer
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_MemSize($1);$_$;
 3   DROP FUNCTION public.st_mem_size(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           0    0 (   FUNCTION st_mlinefromtext(text, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_mlinefromtext(text, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_mlinefromtext(text, integer) TO "Webgroup";
            public       postgres    false    828            �           0    0 (   FUNCTION st_mlinefromwkb(bytea, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_mlinefromwkb(bytea, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_mlinefromwkb(bytea, integer) TO "Webgroup";
            public       postgres    false    1619            �           0    0 )   FUNCTION st_mpointfromtext(text, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_mpointfromtext(text, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_mpointfromtext(text, integer) TO "Webgroup";
            public       postgres    false    1620            �           0    0 )   FUNCTION st_mpointfromwkb(bytea, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_mpointfromwkb(bytea, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_mpointfromwkb(bytea, integer) TO "Webgroup";
            public       postgres    false    854            �           0    0 (   FUNCTION st_mpolyfromtext(text, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_mpolyfromtext(text, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_mpolyfromtext(text, integer) TO "Webgroup";
            public       postgres    false    855            �           0    0 (   FUNCTION st_mpolyfromwkb(bytea, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_mpolyfromwkb(bytea, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_mpolyfromwkb(bytea, integer) TO "Webgroup";
            public       postgres    false    877            �           0    0 *   FUNCTION st_numgeometries(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_numgeometries(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_numgeometries(public.geometry) TO "Webgroup";
            public       postgres    false    805            �           0    0 ,   FUNCTION st_numinteriorring(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_numinteriorring(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_numinteriorring(public.geometry) TO "Webgroup";
            public       postgres    false    829            �           0    0 &   FUNCTION st_numpoints(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_numpoints(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_numpoints(public.geometry) TO "Webgroup";
            public       postgres    false    806            �           0    0 P   FUNCTION st_orderingequals(geometrya public.geometry, geometryb public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_orderingequals(geometrya public.geometry, geometryb public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_orderingequals(geometrya public.geometry, geometryb public.geometry) TO "Webgroup";
            public       postgres    false    1628            �           0    0 B   FUNCTION st_overlaps(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_overlaps(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_overlaps(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    761            �           0    0 &   FUNCTION st_perimeter(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_perimeter(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_perimeter(public.geometry) TO "Webgroup";
            public       postgres    false    1567            �           1255    54797    st_perimeter3d(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_perimeter3d(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_perimeter_poly';
 6   DROP FUNCTION public.st_perimeter3d(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           0    0 5   FUNCTION st_point(double precision, double precision)    ACL     �   GRANT ALL ON FUNCTION public.st_point(double precision, double precision) TO pbrewer;
GRANT ALL ON FUNCTION public.st_point(double precision, double precision) TO "Webgroup";
            public       postgres    false    1568            �           1255    54798 ]   st_point_inside_circle(public.geometry, double precision, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.st_point_inside_circle(public.geometry, double precision, double precision, double precision) RETURNS boolean
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_PointInsideCircle($1,$2,$3,$4);$_$;
 t   DROP FUNCTION public.st_point_inside_circle(public.geometry, double precision, double precision, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           0    0 (   FUNCTION st_pointfromtext(text, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_pointfromtext(text, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_pointfromtext(text, integer) TO "Webgroup";
            public       postgres    false    830            �           0    0 (   FUNCTION st_pointfromwkb(bytea, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_pointfromwkb(bytea, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_pointfromwkb(bytea, integer) TO "Webgroup";
            public       postgres    false    856            �           0    0 +   FUNCTION st_pointonsurface(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_pointonsurface(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_pointonsurface(public.geometry) TO "Webgroup";
            public       postgres    false    762            �           0    0 '   FUNCTION st_polyfromtext(text, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_polyfromtext(text, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_polyfromtext(text, integer) TO "Webgroup";
            public       postgres    false    831            �           0    0 '   FUNCTION st_polyfromwkb(bytea, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_polyfromwkb(bytea, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_polyfromwkb(bytea, integer) TO "Webgroup";
            public       postgres    false    857            �           0    0 -   FUNCTION st_polygon(public.geometry, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_polygon(public.geometry, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_polygon(public.geometry, integer) TO "Webgroup";
            public       postgres    false    1569            �           1255    54799 '   st_polygonize_garray(public.geometry[])    FUNCTION     �   CREATE FUNCTION public.st_polygonize_garray(public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-3', 'polygonize_garray';
 >   DROP FUNCTION public.st_polygonize_garray(public.geometry[]);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           0    0 F   FUNCTION st_relate(geom1 public.geometry, geom2 public.geometry, text)    ACL     �   GRANT ALL ON FUNCTION public.st_relate(geom1 public.geometry, geom2 public.geometry, text) TO pbrewer;
GRANT ALL ON FUNCTION public.st_relate(geom1 public.geometry, geom2 public.geometry, text) TO "Webgroup";
            public       postgres    false    1589            �           1255    54800 #   st_shift_longitude(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_shift_longitude(public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_ShiftLongitude($1);$_$;
 :   DROP FUNCTION public.st_shift_longitude(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           0    0 '   FUNCTION st_startpoint(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_startpoint(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_startpoint(public.geometry) TO "Webgroup";
            public       postgres    false    832            �           0    0 G   FUNCTION st_symdifference(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_symdifference(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_symdifference(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    695            �           1255    54801    st_text(public.geometry)    FUNCTION     �   CREATE FUNCTION public.st_text(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_to_text';
 /   DROP FUNCTION public.st_text(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           0    0 A   FUNCTION st_touches(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_touches(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_touches(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    1590            �           0    0 /   FUNCTION st_transform(public.geometry, integer)    ACL     �   GRANT ALL ON FUNCTION public.st_transform(public.geometry, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.st_transform(public.geometry, integer) TO "Webgroup";
            public       postgres    false    651            �           0    0 ?   FUNCTION st_union(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_union(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_union(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    1648            �           1255    54802 "   st_unite_garray(public.geometry[])    FUNCTION     �   CREATE FUNCTION public.st_unite_garray(public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'pgis_union_geometry_array';
 9   DROP FUNCTION public.st_unite_garray(public.geometry[]);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           0    0 @   FUNCTION st_within(geom1 public.geometry, geom2 public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_within(geom1 public.geometry, geom2 public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_within(geom1 public.geometry, geom2 public.geometry) TO "Webgroup";
            public       postgres    false    763            �           0    0    FUNCTION st_wkbtosql(wkb bytea)    ACL     �   GRANT ALL ON FUNCTION public.st_wkbtosql(wkb bytea) TO pbrewer;
GRANT ALL ON FUNCTION public.st_wkbtosql(wkb bytea) TO "Webgroup";
            public       postgres    false    997            �           0    0    FUNCTION st_wkttosql(text)    ACL     y   GRANT ALL ON FUNCTION public.st_wkttosql(text) TO pbrewer;
GRANT ALL ON FUNCTION public.st_wkttosql(text) TO "Webgroup";
            public       postgres    false    833            �           0    0    FUNCTION st_x(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_x(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_x(public.geometry) TO "Webgroup";
            public       postgres    false    453            �           0    0    FUNCTION st_y(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.st_y(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.st_y(public.geometry) TO "Webgroup";
            public       postgres    false    454            �           1255    54803    startpoint(public.geometry)    FUNCTION     �   CREATE FUNCTION public.startpoint(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_startpoint_linestring';
 2   DROP FUNCTION public.startpoint(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54804    summary(public.geometry)    FUNCTION     �   CREATE FUNCTION public.summary(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_summary';
 /   DROP FUNCTION public.summary(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            �           1255    54805 /   symdifference(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.symdifference(public.geometry, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'symdifference';
 F   DROP FUNCTION public.symdifference(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54806 5   symmetricdifference(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.symmetricdifference(public.geometry, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'symdifference';
 L   DROP FUNCTION public.symmetricdifference(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           0    0    FUNCTION text(public.geometry)    ACL     �   GRANT ALL ON FUNCTION public.text(public.geometry) TO pbrewer;
GRANT ALL ON FUNCTION public.text(public.geometry) TO "Webgroup";
            public       postgres    false    670            �           1255    54807 )   touches(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.touches(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'touches';
 @   DROP FUNCTION public.touches(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54808 #   transform(public.geometry, integer)    FUNCTION     �   CREATE FUNCTION public.transform(public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'transform';
 :   DROP FUNCTION public.transform(public.geometry, integer);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54809 >   translate(public.geometry, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.translate(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_translate($1, $2, $3, 0)$_$;
 U   DROP FUNCTION public.translate(public.geometry, double precision, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54810 P   translate(public.geometry, double precision, double precision, double precision)    FUNCTION     �   CREATE FUNCTION public.translate(public.geometry, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_affine($1, 1, 0, 0, 0, 1, 0, 0, 0, 1, $2, $3, $4)$_$;
 g   DROP FUNCTION public.translate(public.geometry, double precision, double precision, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54811 c   transscale(public.geometry, double precision, double precision, double precision, double precision)    FUNCTION       CREATE FUNCTION public.transscale(public.geometry, double precision, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT st_affine($1,  $4, 0, 0,  0, $5, 0,
		0, 0, 1,  $2 * $4, $3 * $5, 0)$_$;
 z   DROP FUNCTION public.transscale(public.geometry, double precision, double precision, double precision, double precision);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1255    54812    unite_garray(public.geometry[])    FUNCTION     �   CREATE FUNCTION public.unite_garray(public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'pgis_union_geometry_array';
 6   DROP FUNCTION public.unite_garray(public.geometry[]);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           0    0    FUNCTION unlockrows(text)    ACL     w   GRANT ALL ON FUNCTION public.unlockrows(text) TO pbrewer;
GRANT ALL ON FUNCTION public.unlockrows(text) TO "Webgroup";
            public       postgres    false    875            �           1255    54813    update_lastmodifiedtimestamp()    FUNCTION     �   CREATE FUNCTION public.update_lastmodifiedtimestamp() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
NEW.lastmodifiedtimestamp = current_timestamp;
return new;
End;$$;
 5   DROP FUNCTION public.update_lastmodifiedtimestamp();
       public       postgres    false            �           0    0 '   FUNCTION update_lastmodifiedtimestamp()    ACL     K   GRANT ALL ON FUNCTION public.update_lastmodifiedtimestamp() TO "Webgroup";
            public       postgres    false    1535            �           1255    54814    update_layerenvdata()    FUNCTION     �   CREATE FUNCTION public.update_layerenvdata() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
  EXECUTE cpgdb.lookupenvdatabylayer(NEW.rasterlayerid);
  RETURN NULL;
END;$$;
 ,   DROP FUNCTION public.update_layerenvdata();
       public       postgres    false            �           1255    54815    update_objectextentontreeedit()    FUNCTION     	  CREATE FUNCTION public.update_objectextentontreeedit() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
        IF NEW.location is not null THEN
                PERFORM cpgdb.update_objectextentbyobject(NEW.objectid);
        END IF;
        RETURN NEW;
END;$$;
 6   DROP FUNCTION public.update_objectextentontreeedit();
       public       postgres    false            �           1255    54816 $   update_odkformdef-versionincrement()    FUNCTION     �   CREATE FUNCTION public."update_odkformdef-versionincrement"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
NEW.version = OLD.version+1;
RETURN NEW;
END;$$;
 =   DROP FUNCTION public."update_odkformdef-versionincrement"();
       public       tellervo    false            �           1255    54817 !   update_samplingdateatomicfields()    FUNCTION     $  CREATE FUNCTION public.update_samplingdateatomicfields() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
IF NEW.samplingdate IS NOT NULL THEN
  NEW.samplingyear = date_part('year', NEW.samplingdate);
  NEW.samplingmonth = date_part('month', NEW.samplingdate);
END IF;
RETURN new;
END;$$;
 8   DROP FUNCTION public.update_samplingdateatomicfields();
       public       tellervo    false            �           1255    54818    update_treeenvdata()    FUNCTION     u  CREATE FUNCTION public.update_treeenvdata() RETURNS trigger
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
       public       postgres    false            �           0    0 J   FUNCTION updategeometrysrid(character varying, character varying, integer)    ACL     �   GRANT ALL ON FUNCTION public.updategeometrysrid(character varying, character varying, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.updategeometrysrid(character varying, character varying, integer) TO "Webgroup";
            public       postgres    false    628            �           0    0 ]   FUNCTION updategeometrysrid(character varying, character varying, character varying, integer)    ACL     �   GRANT ALL ON FUNCTION public.updategeometrysrid(character varying, character varying, character varying, integer) TO pbrewer;
GRANT ALL ON FUNCTION public.updategeometrysrid(character varying, character varying, character varying, integer) TO "Webgroup";
            public       postgres    false    627            �           0    0 �   FUNCTION updategeometrysrid(catalogn_name character varying, schema_name character varying, table_name character varying, column_name character varying, new_srid_in integer)    ACL     �  GRANT ALL ON FUNCTION public.updategeometrysrid(catalogn_name character varying, schema_name character varying, table_name character varying, column_name character varying, new_srid_in integer) TO pbrewer;
GRANT ALL ON FUNCTION public.updategeometrysrid(catalogn_name character varying, schema_name character varying, table_name character varying, column_name character varying, new_srid_in integer) TO "Webgroup";
            public       postgres    false    1649            �           1255    54819    uuid_generate_v1()    FUNCTION     }   CREATE FUNCTION public.uuid_generate_v1() RETURNS uuid
    LANGUAGE c STRICT
    AS '$libdir/uuid-ossp', 'uuid_generate_v1';
 )   DROP FUNCTION public.uuid_generate_v1();
       public       postgres    false            �           1255    54820    uuid_generate_v3(uuid, text)    FUNCTION     �   CREATE FUNCTION public.uuid_generate_v3(namespace uuid, name text) RETURNS uuid
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/uuid-ossp', 'uuid_generate_v3';
 B   DROP FUNCTION public.uuid_generate_v3(namespace uuid, name text);
       public       postgres    false            �           1255    54821    uuid_generate_v4()    FUNCTION     }   CREATE FUNCTION public.uuid_generate_v4() RETURNS uuid
    LANGUAGE c STRICT
    AS '$libdir/uuid-ossp', 'uuid_generate_v4';
 )   DROP FUNCTION public.uuid_generate_v4();
       public       postgres    false            �           1255    54822    uuid_generate_v5(uuid, text)    FUNCTION     �   CREATE FUNCTION public.uuid_generate_v5(namespace uuid, name text) RETURNS uuid
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/uuid-ossp', 'uuid_generate_v5';
 B   DROP FUNCTION public.uuid_generate_v5(namespace uuid, name text);
       public       postgres    false            �           1255    54823 
   uuid_nil()    FUNCTION     w   CREATE FUNCTION public.uuid_nil() RETURNS uuid
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/uuid-ossp', 'uuid_nil';
 !   DROP FUNCTION public.uuid_nil();
       public       postgres    false            �           1255    54824    uuid_ns_dns()    FUNCTION     }   CREATE FUNCTION public.uuid_ns_dns() RETURNS uuid
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/uuid-ossp', 'uuid_ns_dns';
 $   DROP FUNCTION public.uuid_ns_dns();
       public       postgres    false            �           1255    54825    uuid_ns_oid()    FUNCTION     }   CREATE FUNCTION public.uuid_ns_oid() RETURNS uuid
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/uuid-ossp', 'uuid_ns_oid';
 $   DROP FUNCTION public.uuid_ns_oid();
       public       postgres    false            �           1255    54826    uuid_ns_url()    FUNCTION     }   CREATE FUNCTION public.uuid_ns_url() RETURNS uuid
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/uuid-ossp', 'uuid_ns_url';
 $   DROP FUNCTION public.uuid_ns_url();
       public       postgres    false            �           1255    54827    uuid_ns_x500()    FUNCTION        CREATE FUNCTION public.uuid_ns_x500() RETURNS uuid
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/uuid-ossp', 'uuid_ns_x500';
 %   DROP FUNCTION public.uuid_ns_x500();
       public       postgres    false                        1255    54828 (   within(public.geometry, public.geometry)    FUNCTION     �   CREATE FUNCTION public.within(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT ST_Within($1, $2)$_$;
 ?   DROP FUNCTION public.within(public.geometry, public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2                       1255    54829    x(public.geometry)    FUNCTION     �   CREATE FUNCTION public.x(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_x_point';
 )   DROP FUNCTION public.x(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2                       1255    54830    xmax(public.box3d)    FUNCTION     �   CREATE FUNCTION public.xmax(public.box3d) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_xmax';
 )   DROP FUNCTION public.xmax(public.box3d);
       public       tellervo    false    2    2    2                       1255    54831    xmin(public.box3d)    FUNCTION     �   CREATE FUNCTION public.xmin(public.box3d) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_xmin';
 )   DROP FUNCTION public.xmin(public.box3d);
       public       tellervo    false    2    2    2                       1255    54832    y(public.geometry)    FUNCTION     �   CREATE FUNCTION public.y(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_y_point';
 )   DROP FUNCTION public.y(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2                       1255    54833    ymax(public.box3d)    FUNCTION     �   CREATE FUNCTION public.ymax(public.box3d) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_ymax';
 )   DROP FUNCTION public.ymax(public.box3d);
       public       tellervo    false    2    2    2                       1255    54834    ymin(public.box3d)    FUNCTION     �   CREATE FUNCTION public.ymin(public.box3d) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_ymin';
 )   DROP FUNCTION public.ymin(public.box3d);
       public       tellervo    false    2    2    2                       1255    54835    z(public.geometry)    FUNCTION     �   CREATE FUNCTION public.z(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_z_point';
 )   DROP FUNCTION public.z(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2                       1255    54836    zmax(public.box3d)    FUNCTION     �   CREATE FUNCTION public.zmax(public.box3d) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_zmax';
 )   DROP FUNCTION public.zmax(public.box3d);
       public       tellervo    false    2    2    2            	           1255    54837    zmflag(public.geometry)    FUNCTION     �   CREATE FUNCTION public.zmflag(public.geometry) RETURNS smallint
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'LWGEOM_zmflag';
 .   DROP FUNCTION public.zmflag(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2            
           1255    54838    zmin(public.box3d)    FUNCTION     �   CREATE FUNCTION public.zmin(public.box3d) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-3', 'BOX3D_zmin';
 )   DROP FUNCTION public.zmin(public.box3d);
       public       tellervo    false    2    2    2            N
           1255    54839    array_accum(anyelement) 	   AGGREGATE     y   CREATE AGGREGATE public.array_accum(anyelement) (
    SFUNC = array_append,
    STYPE = anyarray,
    INITCOND = '{}'
);
 /   DROP AGGREGATE public.array_accum(anyelement);
       public       postgres    false            O
           1255    54840    extent(public.geometry) 	   AGGREGATE     �   CREATE AGGREGATE public.extent(public.geometry) (
    SFUNC = public.st_combinebbox,
    STYPE = public.box3d,
    FINALFUNC = public.box2d
);
 /   DROP AGGREGATE public.extent(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            P
           1255    54841    extent3d(public.geometry) 	   AGGREGATE     q   CREATE AGGREGATE public.extent3d(public.geometry) (
    SFUNC = public.combine_bbox,
    STYPE = public.box3d
);
 1   DROP AGGREGATE public.extent3d(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    1199            Q
           1255    54842    first(anyelement) 	   AGGREGATE     d   CREATE AGGREGATE public.first(anyelement) (
    SFUNC = public.agg_first,
    STYPE = anyelement
);
 )   DROP AGGREGATE public.first(anyelement);
       public       postgres    false    1178            R
           1255    54843    makeline(public.geometry) 	   AGGREGATE     �   CREATE AGGREGATE public.makeline(public.geometry) (
    SFUNC = public.pgis_geometry_accum_transfn,
    STYPE = internal,
    FINALFUNC = public.pgis_geometry_makeline_finalfn
);
 1   DROP AGGREGATE public.makeline(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            S
           1255    54844    memcollect(public.geometry) 	   AGGREGATE     t   CREATE AGGREGATE public.memcollect(public.geometry) (
    SFUNC = public.st_collect,
    STYPE = public.geometry
);
 3   DROP AGGREGATE public.memcollect(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            T
           1255    54845    memgeomunion(public.geometry) 	   AGGREGATE     u   CREATE AGGREGATE public.memgeomunion(public.geometry) (
    SFUNC = public.geomunion,
    STYPE = public.geometry
);
 5   DROP AGGREGATE public.memgeomunion(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    1272    2    2    2    2    2    2    2    2            U
           1255    54846    st_extent3d(public.geometry) 	   AGGREGATE     v   CREATE AGGREGATE public.st_extent3d(public.geometry) (
    SFUNC = public.st_combinebbox,
    STYPE = public.box3d
);
 4   DROP AGGREGATE public.st_extent3d(public.geometry);
       public       tellervo    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           2753    54847    gist_geometry_ops    OPERATOR FAMILY     <   CREATE OPERATOR FAMILY public.gist_geometry_ops USING gist;
 :   DROP OPERATOR FAMILY public.gist_geometry_ops USING gist;
       public       postgres    false            �            1259    54848    elementimporterror    TABLE     �  CREATE TABLE public.elementimporterror (
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
       public         postgres    false            �           0    0    TABLE geography_columns    ACL     8   GRANT ALL ON TABLE public.geography_columns TO pbrewer;
            public       postgres    false    212            �           0    0    TABLE geometry_columns    ACL     �   REVOKE SELECT ON TABLE public.geometry_columns FROM PUBLIC;
GRANT ALL ON TABLE public.geometry_columns TO pbrewer;
GRANT ALL ON TABLE public.geometry_columns TO "Webgroup";
            public       postgres    false    213            �            1259    54855 	   inventory    TABLE     u  CREATE TABLE public.inventory (
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
       public         postgres    false            �            1259    54861    sampleimporterror    TABLE     �  CREATE TABLE public.sampleimporterror (
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
       public         postgres    false            �            1259    54868    sampleimportfixerror    TABLE     �  CREATE TABLE public.sampleimportfixerror (
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
       public         postgres    false            �           0    0    TABLE spatial_ref_sys    ACL     �   REVOKE SELECT ON TABLE public.spatial_ref_sys FROM PUBLIC;
GRANT ALL ON TABLE public.spatial_ref_sys TO pbrewer;
GRANT ALL ON TABLE public.spatial_ref_sys TO "Webgroup";
            public       postgres    false    210            �            1259    54875    tblbox    TABLE     �  CREATE TABLE public.tblbox (
    boxid uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    title character varying NOT NULL,
    curationlocation character varying,
    trackinglocation character varying,
    createdtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifiedtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    comments character varying
);
    DROP TABLE public.tblbox;
       public         postgres    false    1528            �           0    0    TABLE tblbox    ACL     0   GRANT ALL ON TABLE public.tblbox TO "Webgroup";
            public       postgres    false    244            �            1259    54884 	   tblconfig    TABLE     �   CREATE TABLE public.tblconfig (
    configid integer NOT NULL,
    key character varying,
    value character varying,
    description character varying
);
    DROP TABLE public.tblconfig;
       public         postgres    false            �            1259    54890    tblconfig_configid_seq    SEQUENCE        CREATE SEQUENCE public.tblconfig_configid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.tblconfig_configid_seq;
       public       postgres    false    245                        0    0    tblconfig_configid_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.tblconfig_configid_seq OWNED BY public.tblconfig.configid;
            public       postgres    false    246            �            1259    54892    tblcrossdate    TABLE     r  CREATE TABLE public.tblcrossdate (
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
       public         postgres    false                       0    0    TABLE tblcrossdate    ACL     6   GRANT ALL ON TABLE public.tblcrossdate TO "Webgroup";
            public       postgres    false    247            �            1259    54901    tblcrossdate_crossdateid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblcrossdate_crossdateid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 3   DROP SEQUENCE public.tblcrossdate_crossdateid_seq;
       public       postgres    false    247                       0    0    tblcrossdate_crossdateid_seq    SEQUENCE OWNED BY     ]   ALTER SEQUENCE public.tblcrossdate_crossdateid_seq OWNED BY public.tblcrossdate.crossdateid;
            public       postgres    false    248                       0    0 %   SEQUENCE tblcrossdate_crossdateid_seq    ACL     I   GRANT ALL ON SEQUENCE public.tblcrossdate_crossdateid_seq TO "Webgroup";
            public       postgres    false    248            �            1259    54903    tblcurationevent    TABLE     G  CREATE TABLE public.tblcurationevent (
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
       public         tellervo    false            �            1259    54910    tblcuration_curationid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblcuration_curationid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 1   DROP SEQUENCE public.tblcuration_curationid_seq;
       public       tellervo    false    249                       0    0    tblcuration_curationid_seq    SEQUENCE OWNED BY     c   ALTER SEQUENCE public.tblcuration_curationid_seq OWNED BY public.tblcurationevent.curationeventid;
            public       tellervo    false    250            �            1259    54912    tlkptrackinglocation    TABLE        CREATE TABLE public.tlkptrackinglocation (
    trackinglocationid integer NOT NULL,
    location character varying NOT NULL
);
 (   DROP TABLE public.tlkptrackinglocation;
       public         postgres    false            �            1259    54918 *   tblcurationlocation_curationlocationid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblcurationlocation_curationlocationid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 A   DROP SEQUENCE public.tblcurationlocation_curationlocationid_seq;
       public       postgres    false    251                       0    0 *   tblcurationlocation_curationlocationid_seq    SEQUENCE OWNED BY     z   ALTER SEQUENCE public.tblcurationlocation_curationlocationid_seq OWNED BY public.tlkptrackinglocation.trackinglocationid;
            public       postgres    false    252            �            1259    54920    tblcustomvocabterm    TABLE       CREATE TABLE public.tblcustomvocabterm (
    customvocabtermid integer NOT NULL,
    "table" character varying NOT NULL,
    field character varying NOT NULL,
    id integer NOT NULL,
    customvocabterm character varying NOT NULL,
    dictionaryname character varying
);
 &   DROP TABLE public.tblcustomvocabterm;
       public         postgres    false            �            1259    54926 (   tblcustomvocabterm_customvocabtermid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblcustomvocabterm_customvocabtermid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ?   DROP SEQUENCE public.tblcustomvocabterm_customvocabtermid_seq;
       public       postgres    false    253                       0    0 (   tblcustomvocabterm_customvocabtermid_seq    SEQUENCE OWNED BY     u   ALTER SEQUENCE public.tblcustomvocabterm_customvocabtermid_seq OWNED BY public.tblcustomvocabterm.customvocabtermid;
            public       postgres    false    254            �            1259    54928    tblelement_elementid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblelement_elementid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.tblelement_elementid_seq;
       public       postgres    false                       0    0 !   SEQUENCE tblelement_elementid_seq    ACL     E   GRANT ALL ON SEQUENCE public.tblelement_elementid_seq TO "Webgroup";
            public       postgres    false    255                        1259    54930    tblelement_gispkey_seq    SEQUENCE        CREATE SEQUENCE public.tblelement_gispkey_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.tblelement_gispkey_seq;
       public       postgres    false    234                       0    0    tblelement_gispkey_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.tblelement_gispkey_seq OWNED BY public.tblelement.gispkey;
            public       postgres    false    256            	           0    0    SEQUENCE tblelement_gispkey_seq    ACL     C   GRANT ALL ON SEQUENCE public.tblelement_gispkey_seq TO "Webgroup";
            public       postgres    false    256                       1259    54932    tblenvironmentaldata    TABLE     �   CREATE TABLE public.tblenvironmentaldata (
    environmentaldataid integer NOT NULL,
    elementid uuid NOT NULL,
    rasterlayerid integer NOT NULL,
    value double precision
);
 (   DROP TABLE public.tblenvironmentaldata;
       public         postgres    false            
           0    0    TABLE tblenvironmentaldata    ACL     >   GRANT ALL ON TABLE public.tblenvironmentaldata TO "Webgroup";
            public       postgres    false    257                       1259    54935 ,   tblenvironmentaldata_environmentaldataid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblenvironmentaldata_environmentaldataid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 C   DROP SEQUENCE public.tblenvironmentaldata_environmentaldataid_seq;
       public       postgres    false    257                       0    0 ,   tblenvironmentaldata_environmentaldataid_seq    SEQUENCE OWNED BY     }   ALTER SEQUENCE public.tblenvironmentaldata_environmentaldataid_seq OWNED BY public.tblenvironmentaldata.environmentaldataid;
            public       postgres    false    258                       0    0 5   SEQUENCE tblenvironmentaldata_environmentaldataid_seq    ACL     Y   GRANT ALL ON SEQUENCE public.tblenvironmentaldata_environmentaldataid_seq TO "Webgroup";
            public       postgres    false    258                       1259    54937    tbliptracking    TABLE     �   CREATE TABLE public.tbliptracking (
    ipaddr inet NOT NULL,
    "timestamp" timestamp with time zone DEFAULT now() NOT NULL,
    securityuserid uuid
);
 !   DROP TABLE public.tbliptracking;
       public         postgres    false                       0    0    TABLE tbliptracking    ACL     7   GRANT ALL ON TABLE public.tbliptracking TO "Webgroup";
            public       postgres    false    259                       1259    54944    tbllaboratory    TABLE     h  CREATE TABLE public.tbllaboratory (
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
       public         tellervo    false    1049                       1259    54951     tblmeasurement_measurementid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblmeasurement_measurementid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 7   DROP SEQUENCE public.tblmeasurement_measurementid_seq;
       public       postgres    false    232                       0    0     tblmeasurement_measurementid_seq    SEQUENCE OWNED BY     e   ALTER SEQUENCE public.tblmeasurement_measurementid_seq OWNED BY public.tblmeasurement.measurementid;
            public       postgres    false    261                       0    0 )   SEQUENCE tblmeasurement_measurementid_seq    ACL     M   GRANT ALL ON SEQUENCE public.tblmeasurement_measurementid_seq TO "Webgroup";
            public       postgres    false    261                       1259    54953    tblobject_objectid_seq    SEQUENCE        CREATE SEQUENCE public.tblobject_objectid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.tblobject_objectid_seq;
       public       postgres    false                       0    0    SEQUENCE tblobject_objectid_seq    ACL     C   GRANT ALL ON SEQUENCE public.tblobject_objectid_seq TO "Webgroup";
            public       postgres    false    262                       1259    54955    tblobjectregion    TABLE     �   CREATE TABLE public.tblobjectregion (
    objectregionid integer NOT NULL,
    objectid uuid NOT NULL,
    regionid integer NOT NULL
);
 #   DROP TABLE public.tblobjectregion;
       public         postgres    false                       0    0    TABLE tblobjectregion    ACL     9   GRANT ALL ON TABLE public.tblobjectregion TO "Webgroup";
            public       postgres    false    263                       1259    54958 "   tblobjectregion_objectregionid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblobjectregion_objectregionid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 9   DROP SEQUENCE public.tblobjectregion_objectregionid_seq;
       public       postgres    false    263                       0    0 "   tblobjectregion_objectregionid_seq    SEQUENCE OWNED BY     i   ALTER SEQUENCE public.tblobjectregion_objectregionid_seq OWNED BY public.tblobjectregion.objectregionid;
            public       postgres    false    264                       0    0 +   SEQUENCE tblobjectregion_objectregionid_seq    ACL     O   GRANT ALL ON SEQUENCE public.tblobjectregion_objectregionid_seq TO "Webgroup";
            public       postgres    false    264            	           1259    54960    tblodkdefinition    TABLE     m  CREATE TABLE public.tblodkdefinition (
    odkdefinitionid uuid DEFAULT public.uuid_generate_v1mc() NOT NULL,
    createdtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    name character varying NOT NULL,
    definition character varying NOT NULL,
    ownerid uuid NOT NULL,
    ispublic boolean DEFAULT false,
    version integer DEFAULT 1 NOT NULL
);
 $   DROP TABLE public.tblodkdefinition;
       public         tellervo    false    1049            
           1259    54970    tblodkinstance    TABLE     c  CREATE TABLE public.tblodkinstance (
    odkinstanceid uuid DEFAULT public.uuid_generate_v1mc() NOT NULL,
    createdtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    ownerid uuid NOT NULL,
    name character varying NOT NULL,
    instance character varying NOT NULL,
    files character varying[],
    deviceid character varying NOT NULL
);
 "   DROP TABLE public.tblodkinstance;
       public         tellervo    false    1049                       1259    54978 
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
       public         tellervo    false    1049                       1259    54988    tblprojectprojecttype    TABLE     �   CREATE TABLE public.tblprojectprojecttype (
    projectprojecttypeid integer NOT NULL,
    projectid uuid NOT NULL,
    projecttypeid integer NOT NULL
);
 )   DROP TABLE public.tblprojectprojecttype;
       public         tellervo    false                       1259    54991 .   tblprojectprojecttype_projectprojecttypeid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblprojectprojecttype_projectprojecttypeid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 E   DROP SEQUENCE public.tblprojectprojecttype_projectprojecttypeid_seq;
       public       tellervo    false    268                       0    0 .   tblprojectprojecttype_projectprojecttypeid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tblprojectprojecttype_projectprojecttypeid_seq OWNED BY public.tblprojectprojecttype.projectprojecttypeid;
            public       tellervo    false    269                       1259    54993    tblradius_radiusid_seq    SEQUENCE        CREATE SEQUENCE public.tblradius_radiusid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.tblradius_radiusid_seq;
       public       postgres    false    237                       0    0    tblradius_radiusid_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.tblradius_radiusid_seq OWNED BY public.tblradius.radiusid;
            public       postgres    false    270                       0    0    SEQUENCE tblradius_radiusid_seq    ACL     C   GRANT ALL ON SEQUENCE public.tblradius_radiusid_seq TO "Webgroup";
            public       postgres    false    270                       1259    54995    tblrasterlayer    TABLE     �   CREATE TABLE public.tblrasterlayer (
    rasterlayerid integer NOT NULL,
    name character varying(255) NOT NULL,
    filename character varying(1024) NOT NULL,
    issystemlayer boolean DEFAULT true NOT NULL,
    description character varying(5000)
);
 "   DROP TABLE public.tblrasterlayer;
       public         postgres    false                       0    0    TABLE tblrasterlayer    ACL     8   GRANT ALL ON TABLE public.tblrasterlayer TO "Webgroup";
            public       postgres    false    271                       1259    55002     tblrasterlayer_rasterlayerid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblrasterlayer_rasterlayerid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 7   DROP SEQUENCE public.tblrasterlayer_rasterlayerid_seq;
       public       postgres    false    271                       0    0     tblrasterlayer_rasterlayerid_seq    SEQUENCE OWNED BY     e   ALTER SEQUENCE public.tblrasterlayer_rasterlayerid_seq OWNED BY public.tblrasterlayer.rasterlayerid;
            public       postgres    false    272                       1259    55004 
   tblreading    TABLE     �   CREATE TABLE public.tblreading (
    readingid integer NOT NULL,
    measurementid integer NOT NULL,
    relyear integer NOT NULL,
    reading integer,
    ewwidth integer,
    lwwidth integer
);
    DROP TABLE public.tblreading;
       public         postgres    false                       0    0    TABLE tblreading    ACL     4   GRANT ALL ON TABLE public.tblreading TO "Webgroup";
            public       postgres    false    273                       1259    55007    tblreading_readingid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblreading_readingid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.tblreading_readingid_seq;
       public       postgres    false    273                       0    0    tblreading_readingid_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.tblreading_readingid_seq OWNED BY public.tblreading.readingid;
            public       postgres    false    274                       0    0 !   SEQUENCE tblreading_readingid_seq    ACL     E   GRANT ALL ON SEQUENCE public.tblreading_readingid_seq TO "Webgroup";
            public       postgres    false    274                       1259    55009    tblreadingreadingnote    TABLE     .  CREATE TABLE public.tblreadingreadingnote (
    readingid integer NOT NULL,
    readingnoteid integer NOT NULL,
    createdtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifiedtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    readingreadingnoteid integer NOT NULL
);
 )   DROP TABLE public.tblreadingreadingnote;
       public         postgres    false                       0    0    TABLE tblreadingreadingnote    ACL     ?   GRANT ALL ON TABLE public.tblreadingreadingnote TO "Webgroup";
            public       postgres    false    275                       1259    55014 .   tblreadingreadingnote_readingreadingnoteid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblreadingreadingnote_readingreadingnoteid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 E   DROP SEQUENCE public.tblreadingreadingnote_readingreadingnoteid_seq;
       public       postgres    false    275                       0    0 .   tblreadingreadingnote_readingreadingnoteid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tblreadingreadingnote_readingreadingnoteid_seq OWNED BY public.tblreadingreadingnote.readingreadingnoteid;
            public       postgres    false    276                       0    0 7   SEQUENCE tblreadingreadingnote_readingreadingnoteid_seq    ACL     [   GRANT ALL ON SEQUENCE public.tblreadingreadingnote_readingreadingnoteid_seq TO "Webgroup";
            public       postgres    false    276                       1259    55016 	   tblredate    TABLE     �   CREATE TABLE public.tblredate (
    redateid integer NOT NULL,
    vmeasurementid uuid NOT NULL,
    startyear integer NOT NULL,
    redatingtypeid integer,
    justification text
);
    DROP TABLE public.tblredate;
       public         postgres    false                       0    0    TABLE tblredate    ACL     3   GRANT ALL ON TABLE public.tblredate TO "Webgroup";
            public       postgres    false    277                       1259    55022    tblredate_redateid_seq    SEQUENCE        CREATE SEQUENCE public.tblredate_redateid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.tblredate_redateid_seq;
       public       postgres    false    277                        0    0    tblredate_redateid_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.tblredate_redateid_seq OWNED BY public.tblredate.redateid;
            public       postgres    false    278            !           0    0    SEQUENCE tblredate_redateid_seq    ACL     C   GRANT ALL ON SEQUENCE public.tblredate_redateid_seq TO "Webgroup";
            public       postgres    false    278                       1259    55024 	   tblregion    TABLE     |  CREATE TABLE public.tblregion (
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
       public         postgres    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2                       1259    55032    tblregion_regionid_seq    SEQUENCE        CREATE SEQUENCE public.tblregion_regionid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.tblregion_regionid_seq;
       public       postgres    false            "           0    0    SEQUENCE tblregion_regionid_seq    ACL     C   GRANT ALL ON SEQUENCE public.tblregion_regionid_seq TO "Webgroup";
            public       postgres    false    280                       1259    55034    tblrequestlog    TABLE     /  CREATE TABLE public.tblrequestlog (
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
       public         postgres    false            #           0    0    TABLE tblrequestlog    ACL     7   GRANT ALL ON TABLE public.tblrequestlog TO "Webgroup";
            public       postgres    false    281                       1259    55041    tblrequestlog_requestlogid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblrequestlog_requestlogid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 5   DROP SEQUENCE public.tblrequestlog_requestlogid_seq;
       public       postgres    false    281            $           0    0    tblrequestlog_requestlogid_seq    SEQUENCE OWNED BY     a   ALTER SEQUENCE public.tblrequestlog_requestlogid_seq OWNED BY public.tblrequestlog.requestlogid;
            public       postgres    false    282            %           0    0 '   SEQUENCE tblrequestlog_requestlogid_seq    ACL     K   GRANT ALL ON SEQUENCE public.tblrequestlog_requestlogid_seq TO "Webgroup";
            public       postgres    false    282                       1259    55043    tblsample_sampleid_seq    SEQUENCE        CREATE SEQUENCE public.tblsample_sampleid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.tblsample_sampleid_seq;
       public       postgres    false    238            &           0    0    tblsample_sampleid_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.tblsample_sampleid_seq OWNED BY public.tblsample.sampleid;
            public       postgres    false    283            '           0    0    SEQUENCE tblsample_sampleid_seq    ACL     C   GRANT ALL ON SEQUENCE public.tblsample_sampleid_seq TO "Webgroup";
            public       postgres    false    283                       1259    55045    tblsecuritydefault    TABLE     �   CREATE TABLE public.tblsecuritydefault (
    securitydefaultid integer NOT NULL,
    securitygroupid integer NOT NULL,
    securitypermissionid integer NOT NULL
);
 &   DROP TABLE public.tblsecuritydefault;
       public         postgres    false            (           0    0    TABLE tblsecuritydefault    ACL     <   GRANT ALL ON TABLE public.tblsecuritydefault TO "Webgroup";
            public       postgres    false    284                       1259    55048 (   tblsecuritydefault_securitydefaultid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblsecuritydefault_securitydefaultid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ?   DROP SEQUENCE public.tblsecuritydefault_securitydefaultid_seq;
       public       postgres    false    284            )           0    0 (   tblsecuritydefault_securitydefaultid_seq    SEQUENCE OWNED BY     u   ALTER SEQUENCE public.tblsecuritydefault_securitydefaultid_seq OWNED BY public.tblsecuritydefault.securitydefaultid;
            public       postgres    false    285            *           0    0 1   SEQUENCE tblsecuritydefault_securitydefaultid_seq    ACL     U   GRANT ALL ON SEQUENCE public.tblsecuritydefault_securitydefaultid_seq TO "Webgroup";
            public       postgres    false    285                       1259    55050    tblsecurityelement    TABLE     �   CREATE TABLE public.tblsecurityelement (
    elementid uuid NOT NULL,
    securitygroupid integer NOT NULL,
    securitypermissionid integer NOT NULL,
    securityelementid integer NOT NULL
);
 &   DROP TABLE public.tblsecurityelement;
       public         postgres    false            +           0    0    TABLE tblsecurityelement    ACL     <   GRANT ALL ON TABLE public.tblsecurityelement TO "Webgroup";
            public       postgres    false    286                       1259    55053 $   tblsecuritygroup_securitygroupid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblsecuritygroup_securitygroupid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ;   DROP SEQUENCE public.tblsecuritygroup_securitygroupid_seq;
       public       postgres    false    235            ,           0    0 $   tblsecuritygroup_securitygroupid_seq    SEQUENCE OWNED BY     m   ALTER SEQUENCE public.tblsecuritygroup_securitygroupid_seq OWNED BY public.tblsecuritygroup.securitygroupid;
            public       postgres    false    287            -           0    0 -   SEQUENCE tblsecuritygroup_securitygroupid_seq    ACL     Q   GRANT ALL ON SEQUENCE public.tblsecuritygroup_securitygroupid_seq TO "Webgroup";
            public       postgres    false    287                        1259    55055    tblsecuritygroupmembership    TABLE     �   CREATE TABLE public.tblsecuritygroupmembership (
    securitygroupmembershipid integer NOT NULL,
    parentsecuritygroupid integer NOT NULL,
    childsecuritygroupid integer NOT NULL
);
 .   DROP TABLE public.tblsecuritygroupmembership;
       public         postgres    false            .           0    0     TABLE tblsecuritygroupmembership    ACL     D   GRANT ALL ON TABLE public.tblsecuritygroupmembership TO "Webgroup";
            public       postgres    false    288            !           1259    55058 8   tblsecuritygroupmembership_securitygroupmembershipid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblsecuritygroupmembership_securitygroupmembershipid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 O   DROP SEQUENCE public.tblsecuritygroupmembership_securitygroupmembershipid_seq;
       public       postgres    false    288            /           0    0 8   tblsecuritygroupmembership_securitygroupmembershipid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tblsecuritygroupmembership_securitygroupmembershipid_seq OWNED BY public.tblsecuritygroupmembership.securitygroupmembershipid;
            public       postgres    false    289            0           0    0 A   SEQUENCE tblsecuritygroupmembership_securitygroupmembershipid_seq    ACL     e   GRANT ALL ON SEQUENCE public.tblsecuritygroupmembership_securitygroupmembershipid_seq TO "Webgroup";
            public       postgres    false    289            "           1259    55060    tblsecurityobject    TABLE     �   CREATE TABLE public.tblsecurityobject (
    securityobjectid integer NOT NULL,
    objectid uuid NOT NULL,
    securitygroupid integer NOT NULL,
    securitypermissionid integer NOT NULL
);
 %   DROP TABLE public.tblsecurityobject;
       public         postgres    false            1           0    0    TABLE tblsecurityobject    ACL     ;   GRANT ALL ON TABLE public.tblsecurityobject TO "Webgroup";
            public       postgres    false    290            #           1259    55063 &   tblsecurityobject_securityobjectid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblsecurityobject_securityobjectid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 =   DROP SEQUENCE public.tblsecurityobject_securityobjectid_seq;
       public       postgres    false    290            2           0    0 &   tblsecurityobject_securityobjectid_seq    SEQUENCE OWNED BY     q   ALTER SEQUENCE public.tblsecurityobject_securityobjectid_seq OWNED BY public.tblsecurityobject.securityobjectid;
            public       postgres    false    291            3           0    0 /   SEQUENCE tblsecurityobject_securityobjectid_seq    ACL     S   GRANT ALL ON SEQUENCE public.tblsecurityobject_securityobjectid_seq TO "Webgroup";
            public       postgres    false    291            $           1259    55065 %   tblsecuritytree_securityelementid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblsecuritytree_securityelementid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 <   DROP SEQUENCE public.tblsecuritytree_securityelementid_seq;
       public       postgres    false    286            4           0    0 %   tblsecuritytree_securityelementid_seq    SEQUENCE OWNED BY     r   ALTER SEQUENCE public.tblsecuritytree_securityelementid_seq OWNED BY public.tblsecurityelement.securityelementid;
            public       postgres    false    292            5           0    0 .   SEQUENCE tblsecuritytree_securityelementid_seq    ACL     R   GRANT ALL ON SEQUENCE public.tblsecuritytree_securityelementid_seq TO "Webgroup";
            public       postgres    false    292            %           1259    55067    tblsecurityuser    TABLE     h  CREATE TABLE public.tblsecurityuser (
    username character varying(31) NOT NULL,
    password character varying(132) NOT NULL,
    firstname character varying(31) NOT NULL,
    lastname character varying(31) NOT NULL,
    isactive boolean DEFAULT true,
    securityuserid uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    odkpassword character varying
);
 #   DROP TABLE public.tblsecurityuser;
       public         postgres    false    1528            6           0    0    TABLE tblsecurityuser    ACL     9   GRANT ALL ON TABLE public.tblsecurityuser TO "Webgroup";
            public       postgres    false    293            &           1259    55075    tblsecurityusermembership    TABLE     �   CREATE TABLE public.tblsecurityusermembership (
    tblsecurityusermembershipid integer NOT NULL,
    securitygroupid integer NOT NULL,
    securityuserid uuid
);
 -   DROP TABLE public.tblsecurityusermembership;
       public         postgres    false            7           0    0    TABLE tblsecurityusermembership    ACL     C   GRANT ALL ON TABLE public.tblsecurityusermembership TO "Webgroup";
            public       postgres    false    294            '           1259    55078 9   tblsecurityusermembership_tblsecurityusermembershipid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblsecurityusermembership_tblsecurityusermembershipid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 P   DROP SEQUENCE public.tblsecurityusermembership_tblsecurityusermembershipid_seq;
       public       postgres    false    294            8           0    0 9   tblsecurityusermembership_tblsecurityusermembershipid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tblsecurityusermembership_tblsecurityusermembershipid_seq OWNED BY public.tblsecurityusermembership.tblsecurityusermembershipid;
            public       postgres    false    295            9           0    0 B   SEQUENCE tblsecurityusermembership_tblsecurityusermembershipid_seq    ACL     f   GRANT ALL ON SEQUENCE public.tblsecurityusermembership_tblsecurityusermembershipid_seq TO "Webgroup";
            public       postgres    false    295            (           1259    55080    tblsecurityvmeasurement    TABLE     �   CREATE TABLE public.tblsecurityvmeasurement (
    securityvmeasurementid integer NOT NULL,
    vmeasurementid uuid NOT NULL,
    securitygroupid integer NOT NULL,
    securitypermissionid integer NOT NULL
);
 +   DROP TABLE public.tblsecurityvmeasurement;
       public         postgres    false            :           0    0    TABLE tblsecurityvmeasurement    ACL     A   GRANT ALL ON TABLE public.tblsecurityvmeasurement TO "Webgroup";
            public       postgres    false    296            )           1259    55083 2   tblsecurityvmeasurement_securityvmeasurementid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblsecurityvmeasurement_securityvmeasurementid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 I   DROP SEQUENCE public.tblsecurityvmeasurement_securityvmeasurementid_seq;
       public       postgres    false    296            ;           0    0 2   tblsecurityvmeasurement_securityvmeasurementid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tblsecurityvmeasurement_securityvmeasurementid_seq OWNED BY public.tblsecurityvmeasurement.securityvmeasurementid;
            public       postgres    false    297            <           0    0 ;   SEQUENCE tblsecurityvmeasurement_securityvmeasurementid_seq    ACL     _   GRANT ALL ON SEQUENCE public.tblsecurityvmeasurement_securityvmeasurementid_seq TO "Webgroup";
            public       postgres    false    297            *           1259    55085    tblsupportedclient    TABLE     �   CREATE TABLE public.tblsupportedclient (
    supportclientid integer NOT NULL,
    client character varying NOT NULL,
    minversion character varying NOT NULL
);
 &   DROP TABLE public.tblsupportedclient;
       public         postgres    false            +           1259    55091 '   tblsupportedclients_supportclientid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblsupportedclients_supportclientid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 >   DROP SEQUENCE public.tblsupportedclients_supportclientid_seq;
       public       postgres    false    298            =           0    0 '   tblsupportedclients_supportclientid_seq    SEQUENCE OWNED BY     r   ALTER SEQUENCE public.tblsupportedclients_supportclientid_seq OWNED BY public.tblsupportedclient.supportclientid;
            public       postgres    false    299            ,           1259    55093    tbltag    TABLE     �   CREATE TABLE public.tbltag (
    tagid uuid DEFAULT public.uuid_generate_v1mc() NOT NULL,
    tag character varying NOT NULL,
    ownerid uuid,
    global boolean DEFAULT false NOT NULL
);
    DROP TABLE public.tbltag;
       public         tellervo    false    1049            -           1259    55101    tbltruncate    TABLE     �   CREATE TABLE public.tbltruncate (
    truncateid integer NOT NULL,
    vmeasurementid uuid NOT NULL,
    startrelyear integer NOT NULL,
    endrelyear integer NOT NULL,
    justification text
);
    DROP TABLE public.tbltruncate;
       public         postgres    false            >           0    0    TABLE tbltruncate    ACL     5   GRANT ALL ON TABLE public.tbltruncate TO "Webgroup";
            public       postgres    false    301            .           1259    55107    tbltruncate_truncateid_seq    SEQUENCE     �   CREATE SEQUENCE public.tbltruncate_truncateid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 1   DROP SEQUENCE public.tbltruncate_truncateid_seq;
       public       postgres    false    301            ?           0    0    tbltruncate_truncateid_seq    SEQUENCE OWNED BY     Y   ALTER SEQUENCE public.tbltruncate_truncateid_seq OWNED BY public.tbltruncate.truncateid;
            public       postgres    false    302            @           0    0 #   SEQUENCE tbltruncate_truncateid_seq    ACL     G   GRANT ALL ON SEQUENCE public.tbltruncate_truncateid_seq TO "Webgroup";
            public       postgres    false    302            /           1259    55109    tblupgradelog    TABLE     �   CREATE TABLE public.tblupgradelog (
    upgradelogid integer NOT NULL,
    filename character varying NOT NULL,
    "timestamp" timestamp with time zone DEFAULT now() NOT NULL
);
 !   DROP TABLE public.tblupgradelog;
       public         postgres    false            A           0    0    TABLE tblupgradelog    ACL     7   GRANT ALL ON TABLE public.tblupgradelog TO "Webgroup";
            public       postgres    false    303            0           1259    55116    tblupgradelog_upgradelogid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblupgradelog_upgradelogid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 5   DROP SEQUENCE public.tblupgradelog_upgradelogid_seq;
       public       postgres    false    303            B           0    0    tblupgradelog_upgradelogid_seq    SEQUENCE OWNED BY     a   ALTER SEQUENCE public.tblupgradelog_upgradelogid_seq OWNED BY public.tblupgradelog.upgradelogid;
            public       postgres    false    304            1           1259    55118    tbluserdefinedfieldvalue    TABLE     �   CREATE TABLE public.tbluserdefinedfieldvalue (
    userdefinedfieldvalueid uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    userdefinedfieldid uuid NOT NULL,
    value character varying,
    entityid uuid NOT NULL
);
 ,   DROP TABLE public.tbluserdefinedfieldvalue;
       public         tellervo    false    1528            2           1259    55125    tblvmeasurement    TABLE     �  CREATE TABLE public.tblvmeasurement (
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
       public         postgres    false    1049            C           0    0     COLUMN tblvmeasurement.birthdate    COMMENT     �   COMMENT ON COLUMN public.tblvmeasurement.birthdate IS 'maps to TRiDaS measurementSeries.measuringDate and derivedSeries.derivationDate';
            public       postgres    false    306            D           0    0    TABLE tblvmeasurement    ACL     9   GRANT ALL ON TABLE public.tblvmeasurement TO "Webgroup";
            public       postgres    false    306            3           1259    55138 "   tblvmeasurement_vmeasurementid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblvmeasurement_vmeasurementid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 9   DROP SEQUENCE public.tblvmeasurement_vmeasurementid_seq;
       public       postgres    false    306            E           0    0 "   tblvmeasurement_vmeasurementid_seq    SEQUENCE OWNED BY     i   ALTER SEQUENCE public.tblvmeasurement_vmeasurementid_seq OWNED BY public.tblvmeasurement.vmeasurementid;
            public       postgres    false    307            F           0    0 +   SEQUENCE tblvmeasurement_vmeasurementid_seq    ACL     O   GRANT ALL ON SEQUENCE public.tblvmeasurement_vmeasurementid_seq TO "Webgroup";
            public       postgres    false    307            4           1259    55140 .   tblvmeasurementderivedcache_derivedcacheid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblvmeasurementderivedcache_derivedcacheid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 E   DROP SEQUENCE public.tblvmeasurementderivedcache_derivedcacheid_seq;
       public       postgres    false    233            G           0    0 .   tblvmeasurementderivedcache_derivedcacheid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tblvmeasurementderivedcache_derivedcacheid_seq OWNED BY public.tblvmeasurementderivedcache.derivedcacheid;
            public       postgres    false    308            H           0    0 7   SEQUENCE tblvmeasurementderivedcache_derivedcacheid_seq    ACL     [   GRANT ALL ON SEQUENCE public.tblvmeasurementderivedcache_derivedcacheid_seq TO "Webgroup";
            public       postgres    false    308            5           1259    55142    tblvmeasurementgroup    TABLE     �   CREATE TABLE public.tblvmeasurementgroup (
    vmeasurementid uuid NOT NULL,
    membervmeasurementid uuid NOT NULL,
    vmeasurementgroupid integer NOT NULL
);
 (   DROP TABLE public.tblvmeasurementgroup;
       public         postgres    false            I           0    0    TABLE tblvmeasurementgroup    ACL     >   GRANT ALL ON TABLE public.tblvmeasurementgroup TO "Webgroup";
            public       postgres    false    309            6           1259    55145 ,   tblvmeasurementgroup_vmeasurementgroupid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblvmeasurementgroup_vmeasurementgroupid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 C   DROP SEQUENCE public.tblvmeasurementgroup_vmeasurementgroupid_seq;
       public       postgres    false    309            J           0    0 ,   tblvmeasurementgroup_vmeasurementgroupid_seq    SEQUENCE OWNED BY     }   ALTER SEQUENCE public.tblvmeasurementgroup_vmeasurementgroupid_seq OWNED BY public.tblvmeasurementgroup.vmeasurementgroupid;
            public       postgres    false    310            K           0    0 5   SEQUENCE tblvmeasurementgroup_vmeasurementgroupid_seq    ACL     Y   GRANT ALL ON SEQUENCE public.tblvmeasurementgroup_vmeasurementgroupid_seq TO "Webgroup";
            public       postgres    false    310            7           1259    55147 4   tblvmeasurementmetacache_vmeasurementmetacacheid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblvmeasurementmetacache_vmeasurementmetacacheid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 K   DROP SEQUENCE public.tblvmeasurementmetacache_vmeasurementmetacacheid_seq;
       public       postgres    false    231            L           0    0 4   tblvmeasurementmetacache_vmeasurementmetacacheid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tblvmeasurementmetacache_vmeasurementmetacacheid_seq OWNED BY public.tblvmeasurementmetacache.vmeasurementmetacacheid;
            public       postgres    false    311            M           0    0 =   SEQUENCE tblvmeasurementmetacache_vmeasurementmetacacheid_seq    ACL     a   GRANT ALL ON SEQUENCE public.tblvmeasurementmetacache_vmeasurementmetacacheid_seq TO "Webgroup";
            public       postgres    false    311            8           1259    55149     tblvmeasurementreadingnoteresult    TABLE     �   CREATE TABLE public.tblvmeasurementreadingnoteresult (
    vmeasurementresultid uuid NOT NULL,
    relyear integer NOT NULL,
    readingnoteid integer NOT NULL,
    inheritedcount integer DEFAULT 0 NOT NULL
);
 4   DROP TABLE public.tblvmeasurementreadingnoteresult;
       public         postgres    false            N           0    0 &   TABLE tblvmeasurementreadingnoteresult    ACL     J   GRANT ALL ON TABLE public.tblvmeasurementreadingnoteresult TO "Webgroup";
            public       postgres    false    312            9           1259    55153 <   tblvmeasurementreadingresult_vmeasurementreadingresultid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblvmeasurementreadingresult_vmeasurementreadingresultid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 S   DROP SEQUENCE public.tblvmeasurementreadingresult_vmeasurementreadingresultid_seq;
       public       postgres    false    239            O           0    0 <   tblvmeasurementreadingresult_vmeasurementreadingresultid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tblvmeasurementreadingresult_vmeasurementreadingresultid_seq OWNED BY public.tblvmeasurementreadingresult.vmeasurementreadingresultid;
            public       postgres    false    313            P           0    0 E   SEQUENCE tblvmeasurementreadingresult_vmeasurementreadingresultid_seq    ACL     i   GRANT ALL ON SEQUENCE public.tblvmeasurementreadingresult_vmeasurementreadingresultid_seq TO "Webgroup";
            public       postgres    false    313            :           1259    55155 :   tblvmeasurementrelyearreadingnote_relyearreadingnoteid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblvmeasurementrelyearreadingnote_relyearreadingnoteid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 Q   DROP SEQUENCE public.tblvmeasurementrelyearreadingnote_relyearreadingnoteid_seq;
       public       postgres    false            ;           1259    55157 !   tblvmeasurementrelyearreadingnote    TABLE     �  CREATE TABLE public.tblvmeasurementrelyearreadingnote (
    vmeasurementid uuid NOT NULL,
    relyear integer NOT NULL,
    readingnoteid integer NOT NULL,
    disabledoverride boolean DEFAULT false NOT NULL,
    createdtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifiedtimestamp timestamp with time zone DEFAULT now() NOT NULL,
    relyearreadingnoteid integer DEFAULT nextval('public.tblvmeasurementrelyearreadingnote_relyearreadingnoteid_seq'::regclass) NOT NULL
);
 5   DROP TABLE public.tblvmeasurementrelyearreadingnote;
       public         postgres    false    314            Q           0    0 '   TABLE tblvmeasurementrelyearreadingnote    ACL     K   GRANT ALL ON TABLE public.tblvmeasurementrelyearreadingnote TO "Webgroup";
            public       postgres    false    315            <           1259    55164    tblvmeasurementresult    TABLE     �  CREATE TABLE public.tblvmeasurementresult (
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
       public         postgres    false            R           0    0    TABLE tblvmeasurementresult    ACL     ?   GRANT ALL ON TABLE public.tblvmeasurementresult TO "Webgroup";
            public       postgres    false    316            =           1259    55174    tblvmeasurementtotag    TABLE     �   CREATE TABLE public.tblvmeasurementtotag (
    vmeasurementtotagid integer NOT NULL,
    tagid uuid NOT NULL,
    vmeasurementid uuid NOT NULL
);
 (   DROP TABLE public.tblvmeasurementtotag;
       public         tellervo    false            >           1259    55177 ,   tblvmeasurementtotag_vmeasurementtotagid_seq    SEQUENCE     �   CREATE SEQUENCE public.tblvmeasurementtotag_vmeasurementtotagid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 C   DROP SEQUENCE public.tblvmeasurementtotag_vmeasurementtotagid_seq;
       public       tellervo    false    317            S           0    0 ,   tblvmeasurementtotag_vmeasurementtotagid_seq    SEQUENCE OWNED BY     }   ALTER SEQUENCE public.tblvmeasurementtotag_vmeasurementtotagid_seq OWNED BY public.tblvmeasurementtotag.vmeasurementtotagid;
            public       tellervo    false    318            ?           1259    55179    tlkpcomplexpresenceabsence    TABLE     �   CREATE TABLE public.tlkpcomplexpresenceabsence (
    complexpresenceabsenceid integer NOT NULL,
    complexpresenceabsence character varying NOT NULL
);
 .   DROP TABLE public.tlkpcomplexpresenceabsence;
       public         postgres    false            T           0    0     TABLE tlkpcomplexpresenceabsence    ACL     D   GRANT ALL ON TABLE public.tlkpcomplexpresenceabsence TO "Webgroup";
            public       postgres    false    319            @           1259    55185 7   tlkpcomplexpresenceabsence_complexpresenceabsenceid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpcomplexpresenceabsence_complexpresenceabsenceid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 N   DROP SEQUENCE public.tlkpcomplexpresenceabsence_complexpresenceabsenceid_seq;
       public       postgres    false    319            U           0    0 7   tlkpcomplexpresenceabsence_complexpresenceabsenceid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tlkpcomplexpresenceabsence_complexpresenceabsenceid_seq OWNED BY public.tlkpcomplexpresenceabsence.complexpresenceabsenceid;
            public       postgres    false    320            A           1259    55187    tlkpcoveragetemporal    TABLE     �   CREATE TABLE public.tlkpcoveragetemporal (
    coveragetemporalid integer NOT NULL,
    coveragetemporal character varying NOT NULL
);
 (   DROP TABLE public.tlkpcoveragetemporal;
       public         postgres    false            V           0    0    TABLE tlkpcoveragetemporal    ACL     >   GRANT ALL ON TABLE public.tlkpcoveragetemporal TO "Webgroup";
            public       postgres    false    321            B           1259    55193 +   tlkpcoveragetemporal_coveragetemporalid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpcoveragetemporal_coveragetemporalid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 B   DROP SEQUENCE public.tlkpcoveragetemporal_coveragetemporalid_seq;
       public       postgres    false    321            W           0    0 +   tlkpcoveragetemporal_coveragetemporalid_seq    SEQUENCE OWNED BY     {   ALTER SEQUENCE public.tlkpcoveragetemporal_coveragetemporalid_seq OWNED BY public.tlkpcoveragetemporal.coveragetemporalid;
            public       postgres    false    322            C           1259    55195    tlkpcoveragetemporalfoundation    TABLE     �   CREATE TABLE public.tlkpcoveragetemporalfoundation (
    coveragetemporalfoundationid integer NOT NULL,
    coveragetemporalfoundation character varying NOT NULL
);
 2   DROP TABLE public.tlkpcoveragetemporalfoundation;
       public         postgres    false            X           0    0 $   TABLE tlkpcoveragetemporalfoundation    ACL     H   GRANT ALL ON TABLE public.tlkpcoveragetemporalfoundation TO "Webgroup";
            public       postgres    false    323            D           1259    55201 ?   tlkpcoveragetemporalfoundation_coveragetemporalfoundationid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpcoveragetemporalfoundation_coveragetemporalfoundationid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 V   DROP SEQUENCE public.tlkpcoveragetemporalfoundation_coveragetemporalfoundationid_seq;
       public       postgres    false    323            Y           0    0 ?   tlkpcoveragetemporalfoundation_coveragetemporalfoundationid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tlkpcoveragetemporalfoundation_coveragetemporalfoundationid_seq OWNED BY public.tlkpcoveragetemporalfoundation.coveragetemporalfoundationid;
            public       postgres    false    324            E           1259    55203    tlkpcurationstatus    TABLE     x   CREATE TABLE public.tlkpcurationstatus (
    curationstatusid integer NOT NULL,
    curationstatus character varying
);
 &   DROP TABLE public.tlkpcurationstatus;
       public         tellervo    false            F           1259    55209 '   tlkpcurationstatus_curationstatusid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpcurationstatus_curationstatusid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 >   DROP SEQUENCE public.tlkpcurationstatus_curationstatusid_seq;
       public       tellervo    false    325            Z           0    0 '   tlkpcurationstatus_curationstatusid_seq    SEQUENCE OWNED BY     s   ALTER SEQUENCE public.tlkpcurationstatus_curationstatusid_seq OWNED BY public.tlkpcurationstatus.curationstatusid;
            public       tellervo    false    326            G           1259    55211    tlkpdatatype    TABLE     N   CREATE TABLE public.tlkpdatatype (
    datatype character varying NOT NULL
);
     DROP TABLE public.tlkpdatatype;
       public         tellervo    false            H           1259    55217    tlkpdatecertainty    TABLE     ~   CREATE TABLE public.tlkpdatecertainty (
    datecertaintyid integer NOT NULL,
    datecertainty character varying NOT NULL
);
 %   DROP TABLE public.tlkpdatecertainty;
       public         postgres    false            I           1259    55223 %   tlkpdatecertainty_datecertaintyid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpdatecertainty_datecertaintyid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 <   DROP SEQUENCE public.tlkpdatecertainty_datecertaintyid_seq;
       public       postgres    false    328            [           0    0 %   tlkpdatecertainty_datecertaintyid_seq    SEQUENCE OWNED BY     o   ALTER SEQUENCE public.tlkpdatecertainty_datecertaintyid_seq OWNED BY public.tlkpdatecertainty.datecertaintyid;
            public       postgres    false    329            J           1259    55225    tlkpdatingtype    TABLE     �   CREATE TABLE public.tlkpdatingtype (
    datingtypeid integer NOT NULL,
    datingtype character varying NOT NULL,
    datingclass public.datingtypeclass NOT NULL
);
 "   DROP TABLE public.tlkpdatingtype;
       public         postgres    false    2090            \           0    0    TABLE tlkpdatingtype    ACL     8   GRANT ALL ON TABLE public.tlkpdatingtype TO "Webgroup";
            public       postgres    false    330            K           1259    55231    tlkpdatingtype_datingtypeid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpdatingtype_datingtypeid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 6   DROP SEQUENCE public.tlkpdatingtype_datingtypeid_seq;
       public       postgres    false    330            ]           0    0    tlkpdatingtype_datingtypeid_seq    SEQUENCE OWNED BY     c   ALTER SEQUENCE public.tlkpdatingtype_datingtypeid_seq OWNED BY public.tlkpdatingtype.datingtypeid;
            public       postgres    false    331            ^           0    0 (   SEQUENCE tlkpdatingtype_datingtypeid_seq    ACL     L   GRANT ALL ON SEQUENCE public.tlkpdatingtype_datingtypeid_seq TO "Webgroup";
            public       postgres    false    331            L           1259    55233 
   tlkpdomain    TABLE     �   CREATE TABLE public.tlkpdomain (
    domainid integer NOT NULL,
    domain character varying NOT NULL,
    prefix character varying
);
    DROP TABLE public.tlkpdomain;
       public         tellervo    false            M           1259    55239    tlkpdomain_domainid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpdomain_domainid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 .   DROP SEQUENCE public.tlkpdomain_domainid_seq;
       public       tellervo    false    332            _           0    0    tlkpdomain_domainid_seq    SEQUENCE OWNED BY     S   ALTER SEQUENCE public.tlkpdomain_domainid_seq OWNED BY public.tlkpdomain.domainid;
            public       tellervo    false    333            N           1259    55241    tlkpelementauthenticity    TABLE     �   CREATE TABLE public.tlkpelementauthenticity (
    elementauthenticityid integer NOT NULL,
    elementauthenticity character varying NOT NULL
);
 +   DROP TABLE public.tlkpelementauthenticity;
       public         postgres    false            `           0    0    TABLE tlkpelementauthenticity    ACL     A   GRANT ALL ON TABLE public.tlkpelementauthenticity TO "Webgroup";
            public       postgres    false    334            O           1259    55247 1   tlkpelementauthenticity_elementauthenticityid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpelementauthenticity_elementauthenticityid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 H   DROP SEQUENCE public.tlkpelementauthenticity_elementauthenticityid_seq;
       public       postgres    false    334            a           0    0 1   tlkpelementauthenticity_elementauthenticityid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tlkpelementauthenticity_elementauthenticityid_seq OWNED BY public.tlkpelementauthenticity.elementauthenticityid;
            public       postgres    false    335            P           1259    55249    tlkpelementshape    TABLE     {   CREATE TABLE public.tlkpelementshape (
    elementshapeid integer NOT NULL,
    elementshape character varying NOT NULL
);
 $   DROP TABLE public.tlkpelementshape;
       public         postgres    false            b           0    0    TABLE tlkpelementshape    ACL     :   GRANT ALL ON TABLE public.tlkpelementshape TO "Webgroup";
            public       postgres    false    336            Q           1259    55255 #   tlkpelementshape_elementshapeid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpelementshape_elementshapeid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 :   DROP SEQUENCE public.tlkpelementshape_elementshapeid_seq;
       public       postgres    false    336            c           0    0 #   tlkpelementshape_elementshapeid_seq    SEQUENCE OWNED BY     k   ALTER SEQUENCE public.tlkpelementshape_elementshapeid_seq OWNED BY public.tlkpelementshape.elementshapeid;
            public       postgres    false    337            R           1259    55257    tlkpelementtype    TABLE     �   CREATE TABLE public.tlkpelementtype (
    elementtypeid integer NOT NULL,
    elementtype character varying NOT NULL,
    vocabularyid integer
);
 #   DROP TABLE public.tlkpelementtype;
       public         postgres    false            d           0    0    TABLE tlkpelementtype    ACL     9   GRANT ALL ON TABLE public.tlkpelementtype TO "Webgroup";
            public       postgres    false    338            S           1259    55263 !   tlkpelementtype_elementtypeid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpelementtype_elementtypeid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 8   DROP SEQUENCE public.tlkpelementtype_elementtypeid_seq;
       public       postgres    false    338            e           0    0 !   tlkpelementtype_elementtypeid_seq    SEQUENCE OWNED BY     g   ALTER SEQUENCE public.tlkpelementtype_elementtypeid_seq OWNED BY public.tlkpelementtype.elementtypeid;
            public       postgres    false    339            T           1259    55265    tlkpindextype    TABLE     r   CREATE TABLE public.tlkpindextype (
    indexid integer NOT NULL,
    indexname character varying(30) NOT NULL
);
 !   DROP TABLE public.tlkpindextype;
       public         postgres    false            f           0    0    TABLE tlkpindextype    ACL     7   GRANT ALL ON TABLE public.tlkpindextype TO "Webgroup";
            public       postgres    false    340            U           1259    55268    tlkplaboratory_laboratoryid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkplaboratory_laboratoryid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 6   DROP SEQUENCE public.tlkplaboratory_laboratoryid_seq;
       public       tellervo    false            V           1259    55270    tlkplocationtype    TABLE     r   CREATE TABLE public.tlkplocationtype (
    locationtypeid integer NOT NULL,
    locationtype character varying
);
 $   DROP TABLE public.tlkplocationtype;
       public         postgres    false            g           0    0    TABLE tlkplocationtype    ACL     :   GRANT ALL ON TABLE public.tlkplocationtype TO "Webgroup";
            public       postgres    false    342            W           1259    55276 #   tlkplocationtype_locationtypeid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkplocationtype_locationtypeid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 :   DROP SEQUENCE public.tlkplocationtype_locationtypeid_seq;
       public       postgres    false    342            h           0    0 #   tlkplocationtype_locationtypeid_seq    SEQUENCE OWNED BY     k   ALTER SEQUENCE public.tlkplocationtype_locationtypeid_seq OWNED BY public.tlkplocationtype.locationtypeid;
            public       postgres    false    343            X           1259    55278    tlkpmeasurementvariable    TABLE     �   CREATE TABLE public.tlkpmeasurementvariable (
    measurementvariableid integer NOT NULL,
    measurementvariable character varying NOT NULL
);
 +   DROP TABLE public.tlkpmeasurementvariable;
       public         postgres    false            i           0    0    TABLE tlkpmeasurementvariable    ACL     A   GRANT ALL ON TABLE public.tlkpmeasurementvariable TO "Webgroup";
            public       postgres    false    344            Y           1259    55284 1   tlkpmeasurementvariable_measurementvariableid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpmeasurementvariable_measurementvariableid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 H   DROP SEQUENCE public.tlkpmeasurementvariable_measurementvariableid_seq;
       public       postgres    false    344            j           0    0 1   tlkpmeasurementvariable_measurementvariableid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tlkpmeasurementvariable_measurementvariableid_seq OWNED BY public.tlkpmeasurementvariable.measurementvariableid;
            public       postgres    false    345            Z           1259    55286    tlkpmeasuringmethod    TABLE     �   CREATE TABLE public.tlkpmeasuringmethod (
    measuringmethodid integer NOT NULL,
    measuringmethod character varying NOT NULL
);
 '   DROP TABLE public.tlkpmeasuringmethod;
       public         postgres    false            k           0    0    TABLE tlkpmeasuringmethod    ACL     =   GRANT ALL ON TABLE public.tlkpmeasuringmethod TO "Webgroup";
            public       postgres    false    346            [           1259    55292 )   tlkpmeasuringmethod_measuringmethodid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpmeasuringmethod_measuringmethodid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 @   DROP SEQUENCE public.tlkpmeasuringmethod_measuringmethodid_seq;
       public       postgres    false    346            l           0    0 )   tlkpmeasuringmethod_measuringmethodid_seq    SEQUENCE OWNED BY     w   ALTER SEQUENCE public.tlkpmeasuringmethod_measuringmethodid_seq OWNED BY public.tlkpmeasuringmethod.measuringmethodid;
            public       postgres    false    347            \           1259    55294    tlkpobjecttype    TABLE     �   CREATE TABLE public.tlkpobjecttype (
    objecttype character varying NOT NULL,
    vocabularyid integer,
    objecttypeid integer NOT NULL
);
 "   DROP TABLE public.tlkpobjecttype;
       public         postgres    false            m           0    0    TABLE tlkpobjecttype    ACL     8   GRANT ALL ON TABLE public.tlkpobjecttype TO "Webgroup";
            public       postgres    false    348            ]           1259    55300    tlkpobjecttype_objecttype_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpobjecttype_objecttype_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 4   DROP SEQUENCE public.tlkpobjecttype_objecttype_seq;
       public       postgres    false    348            n           0    0    tlkpobjecttype_objecttype_seq    SEQUENCE OWNED BY     a   ALTER SEQUENCE public.tlkpobjecttype_objecttype_seq OWNED BY public.tlkpobjecttype.objecttypeid;
            public       postgres    false    349            ^           1259    55302    tlkppresenceabsence    TABLE     �   CREATE TABLE public.tlkppresenceabsence (
    presenceabsenceid integer NOT NULL,
    presenceabsence character varying NOT NULL
);
 '   DROP TABLE public.tlkppresenceabsence;
       public         postgres    false            _           1259    55308 )   tlkppresenceabsence_presenceabsenceid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkppresenceabsence_presenceabsenceid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 @   DROP SEQUENCE public.tlkppresenceabsence_presenceabsenceid_seq;
       public       postgres    false    350            o           0    0 )   tlkppresenceabsence_presenceabsenceid_seq    SEQUENCE OWNED BY     w   ALTER SEQUENCE public.tlkppresenceabsence_presenceabsenceid_seq OWNED BY public.tlkppresenceabsence.presenceabsenceid;
            public       postgres    false    351            `           1259    55310 '   tlkpprojectcategory_projectcategory_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpprojectcategory_projectcategory_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 >   DROP SEQUENCE public.tlkpprojectcategory_projectcategory_seq;
       public       tellervo    false            a           1259    55312    tlkpprojectcategory    TABLE     �   CREATE TABLE public.tlkpprojectcategory (
    projectcategory character varying NOT NULL,
    vocabularyid integer,
    projectcategoryid integer DEFAULT nextval('public.tlkpprojectcategory_projectcategory_seq'::regclass) NOT NULL
);
 '   DROP TABLE public.tlkpprojectcategory;
       public         tellervo    false    352            b           1259    55319    tlkpprojecttype_projecttype_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpprojecttype_projecttype_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 6   DROP SEQUENCE public.tlkpprojecttype_projecttype_seq;
       public       tellervo    false            c           1259    55321    tlkpprojecttype    TABLE     �   CREATE TABLE public.tlkpprojecttype (
    projecttype character varying NOT NULL,
    vocabularyid integer,
    projecttypeid integer DEFAULT nextval('public.tlkpprojecttype_projecttype_seq'::regclass) NOT NULL
);
 #   DROP TABLE public.tlkpprojecttype;
       public         tellervo    false    354            d           1259    55328    tlkprank_rankid_seq    SEQUENCE     |   CREATE SEQUENCE public.tlkprank_rankid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.tlkprank_rankid_seq;
       public       postgres    false            p           0    0    SEQUENCE tlkprank_rankid_seq    ACL     @   GRANT ALL ON SEQUENCE public.tlkprank_rankid_seq TO "Webgroup";
            public       postgres    false    356            e           1259    55330 !   tlkpreadingnote_readingnoteid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpreadingnote_readingnoteid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 8   DROP SEQUENCE public.tlkpreadingnote_readingnoteid_seq;
       public       postgres    false    230            q           0    0 !   tlkpreadingnote_readingnoteid_seq    SEQUENCE OWNED BY     g   ALTER SEQUENCE public.tlkpreadingnote_readingnoteid_seq OWNED BY public.tlkpreadingnote.readingnoteid;
            public       postgres    false    357            r           0    0 *   SEQUENCE tlkpreadingnote_readingnoteid_seq    ACL     N   GRANT ALL ON SEQUENCE public.tlkpreadingnote_readingnoteid_seq TO "Webgroup";
            public       postgres    false    357            f           1259    55332    tlkpsamplestatus    TABLE     r   CREATE TABLE public.tlkpsamplestatus (
    samplestatusid integer NOT NULL,
    samplestatus character varying
);
 $   DROP TABLE public.tlkpsamplestatus;
       public         tellervo    false            g           1259    55338 #   tlkpsamplestatus_samplestatusid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpsamplestatus_samplestatusid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 :   DROP SEQUENCE public.tlkpsamplestatus_samplestatusid_seq;
       public       tellervo    false    358            s           0    0 #   tlkpsamplestatus_samplestatusid_seq    SEQUENCE OWNED BY     k   ALTER SEQUENCE public.tlkpsamplestatus_samplestatusid_seq OWNED BY public.tlkpsamplestatus.samplestatusid;
            public       tellervo    false    359            h           1259    55340    tlkpsampletype    TABLE     y   CREATE TABLE public.tlkpsampletype (
    sampletypeid integer NOT NULL,
    sampletype character varying(32) NOT NULL
);
 "   DROP TABLE public.tlkpsampletype;
       public         postgres    false            t           0    0    TABLE tlkpsampletype    ACL     8   GRANT ALL ON TABLE public.tlkpsampletype TO "Webgroup";
            public       postgres    false    360            i           1259    55343    tlkpsampletype_sampletypeid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpsampletype_sampletypeid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 6   DROP SEQUENCE public.tlkpsampletype_sampletypeid_seq;
       public       postgres    false    360            u           0    0    tlkpsampletype_sampletypeid_seq    SEQUENCE OWNED BY     c   ALTER SEQUENCE public.tlkpsampletype_sampletypeid_seq OWNED BY public.tlkpsampletype.sampletypeid;
            public       postgres    false    361            v           0    0 (   SEQUENCE tlkpsampletype_sampletypeid_seq    ACL     L   GRANT ALL ON SEQUENCE public.tlkpsampletype_sampletypeid_seq TO "Webgroup";
            public       postgres    false    361            j           1259    55345    tlkpsecuritypermission    TABLE     �   CREATE TABLE public.tlkpsecuritypermission (
    securitypermissionid integer NOT NULL,
    name character varying(31) NOT NULL
);
 *   DROP TABLE public.tlkpsecuritypermission;
       public         postgres    false            w           0    0    TABLE tlkpsecuritypermission    ACL     @   GRANT ALL ON TABLE public.tlkpsecuritypermission TO "Webgroup";
            public       postgres    false    362            k           1259    55348 /   tlkpsecuritypermission_securitypermissionid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpsecuritypermission_securitypermissionid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 F   DROP SEQUENCE public.tlkpsecuritypermission_securitypermissionid_seq;
       public       postgres    false    362            x           0    0 /   tlkpsecuritypermission_securitypermissionid_seq    SEQUENCE OWNED BY     �   ALTER SEQUENCE public.tlkpsecuritypermission_securitypermissionid_seq OWNED BY public.tlkpsecuritypermission.securitypermissionid;
            public       postgres    false    363            y           0    0 8   SEQUENCE tlkpsecuritypermission_securitypermissionid_seq    ACL     \   GRANT ALL ON SEQUENCE public.tlkpsecuritypermission_securitypermissionid_seq TO "Webgroup";
            public       postgres    false    363            l           1259    55350    tlkptaxon_taxonid_seq    SEQUENCE     ~   CREATE SEQUENCE public.tlkptaxon_taxonid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.tlkptaxon_taxonid_seq;
       public       postgres    false            z           0    0    SEQUENCE tlkptaxon_taxonid_seq    ACL     B   GRANT ALL ON SEQUENCE public.tlkptaxon_taxonid_seq TO "Webgroup";
            public       postgres    false    364            m           1259    55352 	   tlkptaxon    TABLE     2  CREATE TABLE public.tlkptaxon (
    taxonid bigint DEFAULT nextval('public.tlkptaxon_taxonid_seq'::regclass) NOT NULL,
    colid character varying(36) NOT NULL,
    colparentid character varying(36),
    taxonrankid integer NOT NULL,
    label character varying(128) NOT NULL,
    parenttaxonid integer
);
    DROP TABLE public.tlkptaxon;
       public         postgres    false    364            {           0    0    TABLE tlkptaxon    ACL     3   GRANT ALL ON TABLE public.tlkptaxon TO "Webgroup";
            public       postgres    false    365            n           1259    55356    tlkptaxonrank    TABLE     �   CREATE TABLE public.tlkptaxonrank (
    taxonrankid integer DEFAULT nextval('public.tlkprank_rankid_seq'::regclass) NOT NULL,
    taxonrank character varying(30) NOT NULL,
    rankorder double precision
);
 !   DROP TABLE public.tlkptaxonrank;
       public         postgres    false    356            |           0    0    TABLE tlkptaxonrank    ACL     7   GRANT ALL ON TABLE public.tlkptaxonrank TO "Webgroup";
            public       postgres    false    366            o           1259    55360    tlkpunit    TABLE     c   CREATE TABLE public.tlkpunit (
    unitid integer NOT NULL,
    unit character varying NOT NULL
);
    DROP TABLE public.tlkpunit;
       public         postgres    false            }           0    0    TABLE tlkpunit    ACL     2   GRANT ALL ON TABLE public.tlkpunit TO "Webgroup";
            public       postgres    false    367            p           1259    55366    tlkpunits_unitsid_seq    SEQUENCE     ~   CREATE SEQUENCE public.tlkpunits_unitsid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.tlkpunits_unitsid_seq;
       public       postgres    false    367            ~           0    0    tlkpunits_unitsid_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.tlkpunits_unitsid_seq OWNED BY public.tlkpunit.unitid;
            public       postgres    false    368            q           1259    55368    tlkpuserdefinedfield    TABLE     �  CREATE TABLE public.tlkpuserdefinedfield (
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
       public         tellervo    false    1528                       0    0 &   COLUMN tlkpuserdefinedfield.attachedto    COMMENT     �   COMMENT ON COLUMN public.tlkpuserdefinedfield.attachedto IS 'Integer indicated at what tridas level this field is attached.  1=project, 2=object, 3=element, 4=sample, 5=radius, 6=measurementseries';
            public       tellervo    false    369            r           1259    55377    tlkpuserdefinedterm    TABLE     �   CREATE TABLE public.tlkpuserdefinedterm (
    userdefinedtermid uuid DEFAULT public.uuid_generate_v1mc() NOT NULL,
    term character varying NOT NULL,
    dictionarykey character varying NOT NULL
);
 '   DROP TABLE public.tlkpuserdefinedterm;
       public         tellervo    false    1049            s           1259    55384    tlkpvmeasurementop    TABLE     �   CREATE TABLE public.tlkpvmeasurementop (
    vmeasurementopid integer NOT NULL,
    name character varying(31) NOT NULL,
    legacycode integer
);
 &   DROP TABLE public.tlkpvmeasurementop;
       public         postgres    false            �           0    0    TABLE tlkpvmeasurementop    ACL     <   GRANT ALL ON TABLE public.tlkpvmeasurementop TO "Webgroup";
            public       postgres    false    371            t           1259    55387 '   tlkpvmeasurementop_vmeasurementopid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpvmeasurementop_vmeasurementopid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 >   DROP SEQUENCE public.tlkpvmeasurementop_vmeasurementopid_seq;
       public       postgres    false    371            �           0    0 '   tlkpvmeasurementop_vmeasurementopid_seq    SEQUENCE OWNED BY     s   ALTER SEQUENCE public.tlkpvmeasurementop_vmeasurementopid_seq OWNED BY public.tlkpvmeasurementop.vmeasurementopid;
            public       postgres    false    372            �           0    0 0   SEQUENCE tlkpvmeasurementop_vmeasurementopid_seq    ACL     T   GRANT ALL ON SEQUENCE public.tlkpvmeasurementop_vmeasurementopid_seq TO "Webgroup";
            public       postgres    false    372            u           1259    55389    tlkpvocabulary    TABLE     �   CREATE TABLE public.tlkpvocabulary (
    vocabularyid integer NOT NULL,
    name character varying NOT NULL,
    url character varying
);
 "   DROP TABLE public.tlkpvocabulary;
       public         postgres    false            �           0    0    TABLE tlkpvocabulary    ACL     8   GRANT ALL ON TABLE public.tlkpvocabulary TO "Webgroup";
            public       postgres    false    373            v           1259    55395    tlkpvocabulary_vocabularyid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpvocabulary_vocabularyid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 6   DROP SEQUENCE public.tlkpvocabulary_vocabularyid_seq;
       public       postgres    false    373            �           0    0    tlkpvocabulary_vocabularyid_seq    SEQUENCE OWNED BY     c   ALTER SEQUENCE public.tlkpvocabulary_vocabularyid_seq OWNED BY public.tlkpvocabulary.vocabularyid;
            public       postgres    false    374            w           1259    55397    tlkpwmsserver    TABLE     �   CREATE TABLE public.tlkpwmsserver (
    wmsserverid integer NOT NULL,
    name character varying NOT NULL,
    url character varying NOT NULL
);
 !   DROP TABLE public.tlkpwmsserver;
       public         postgres    false            �           0    0    TABLE tlkpwmsserver    ACL     7   GRANT ALL ON TABLE public.tlkpwmsserver TO "Webgroup";
            public       postgres    false    375            x           1259    55403    tlkpwmsserver_wmsserverid_seq    SEQUENCE     �   CREATE SEQUENCE public.tlkpwmsserver_wmsserverid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 4   DROP SEQUENCE public.tlkpwmsserver_wmsserverid_seq;
       public       postgres    false    375            �           0    0    tlkpwmsserver_wmsserverid_seq    SEQUENCE OWNED BY     _   ALTER SEQUENCE public.tlkpwmsserver_wmsserverid_seq OWNED BY public.tlkpwmsserver.wmsserverid;
            public       postgres    false    376            y           1259    55405    vlkpvmeasurement    VIEW     G  CREATE VIEW public.vlkpvmeasurement AS
 SELECT tblvmeasurement.vmeasurementid,
    tblvmeasurement.code AS name,
    tlkpvmeasurementop.name AS op
   FROM (public.tlkpvmeasurementop
     JOIN public.tblvmeasurement ON ((tlkpvmeasurementop.vmeasurementopid = tblvmeasurement.vmeasurementopid)))
  ORDER BY tblvmeasurement.code;
 #   DROP VIEW public.vlkpvmeasurement;
       public       postgres    false    306    306    306    371    371            z           1259    55409    vw_bigbrothertracking    VIEW     E  CREATE VIEW public.vw_bigbrothertracking AS
 SELECT tblsecurityuser.firstname,
    tblsecurityuser.lastname,
    tblsecurityuser.username,
    tbliptracking.ipaddr,
    tbliptracking."timestamp"
   FROM public.tbliptracking,
    public.tblsecurityuser
  WHERE (tbliptracking.securityuserid = tblsecurityuser.securityuserid);
 (   DROP VIEW public.vw_bigbrothertracking;
       public       postgres    false    293    293    293    259    259    259    293            {           1259    55413    vw_elementtoradius    VIEW     �  CREATE VIEW public.vw_elementtoradius AS
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
       public       tellervo    false    237    1141    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    234    237    237    237    237    237    237    237    237    237    237    237    237    237    237    237    237    237    237    237    238    238    238    238    238    238    238    238    238    238    238    238    238    238    238    238    238    2    2    2    2    2    2    2    2            |           1259    55418    vw_requestlogsummary    VIEW     A  CREATE VIEW public.vw_requestlogsummary AS
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
       public       postgres    false    293    293    293    281    281    281    281    281    281    281    281            }           1259    55422    vw_tlotoradius    VIEW     }  CREATE VIEW public.vw_tlotoradius AS
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
       public       tellervo    false    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    229    229    229    229    229    229    229    229    229    229    229    229    229    229    229    229    229    229    229    229    229    229    229    229    229    229    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    379    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            ~           1259    55427    vwderivedcount    VIEW     L  CREATE VIEW public.vwderivedcount AS
 SELECT tblvmeasurementderivedcache.vmeasurementid,
    count(tblvmeasurementderivedcache.measurementid) AS count,
    public.first(tblvmeasurementderivedcache.measurementid) AS firstmeasurementid
   FROM public.tblvmeasurementderivedcache
  GROUP BY tblvmeasurementderivedcache.vmeasurementid;
 !   DROP VIEW public.vwderivedcount;
       public       postgres    false    233    233    2641                       1259    55431    vwderivedtitles    VIEW     �  CREATE VIEW public.vwderivedtitles AS
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
       public       postgres    false    382    238    382    382    229    229    229    232    232    234    234    234    237    237    237    238    238            �           1259    55436    vwdirectchildcount    VIEW       CREATE VIEW public.vwdirectchildcount AS
 SELECT tblvmeasurementgroup.membervmeasurementid AS vmeasurementid,
    count(tblvmeasurementgroup.vmeasurementid) AS directchildcount
   FROM public.tblvmeasurementgroup
  GROUP BY tblvmeasurementgroup.membervmeasurementid;
 %   DROP VIEW public.vwdirectchildcount;
       public       postgres    false    309    309            �           1259    55440    vwcomprehensivevm    VIEW     N	  CREATE VIEW public.vwcomprehensivevm AS
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
       public       tellervo    false    371    383    383    383    383    383    383    383    384    384    247    247    306    306    306    306    293    232    232    232    232    232    232    232    232    232    232    232    232    232    232    293    293    293    247    247    231    247    232    247    231    231    231    231    231    231    231    231    231    306    231    306    306    306    306    306    306    306    306    306    306    330    330    332    332    371    2    2    2    2    2    2    2    2            �           1259    55445    vwcomprehensivevm2    VIEW     �  CREATE VIEW public.vwcomprehensivevm2 AS
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
       public       tellervo    false    306    231    231    231    231    231    231    231    231    231    232    232    232    231    231    232    232    232    232    232    232    232    232    232    232    384    384    383    383    383    383    383    383    383    371    371    330    330    306    306    306    306    306    306    306    306    306    306    306    306    306    293    293    247    247    247    247    247    247    232    232    2    2    2    2    2    2    2    2            �           1259    55450    vwcountperpersonperobject    VIEW     G  CREATE VIEW public.vwcountperpersonperobject AS
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
       public       postgres    false    232    234    234    237    306    306    229    229    232    237    238    238    293    293    293    306            �           1259    55455    vwresultnotesasarray    VIEW     �  CREATE VIEW public.vwresultnotesasarray AS
 SELECT tblvmeasurementreadingnoteresult.relyear,
    tblvmeasurementreadingnoteresult.vmeasurementresultid,
    public.array_accum(tblvmeasurementreadingnoteresult.readingnoteid) AS noteids,
    public.array_accum(tblvmeasurementreadingnoteresult.inheritedcount) AS inheritedcounts
   FROM public.tblvmeasurementreadingnoteresult
  GROUP BY tblvmeasurementreadingnoteresult.relyear, tblvmeasurementreadingnoteresult.vmeasurementresultid;
 '   DROP VIEW public.vwresultnotesasarray;
       public       postgres    false    312    312    312    312    2638            �           0    0    TABLE vwresultnotesasarray    ACL     >   GRANT ALL ON TABLE public.vwresultnotesasarray TO "Webgroup";
            public       postgres    false    388            �           1259    55459    vwjsonnotedreadingresult    VIEW       CREATE VIEW public.vwjsonnotedreadingresult AS
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
       public       postgres    false    239    239    1139    388    388    388    388    239    239    239    239    239    239    239    239            �           0    0    TABLE vwjsonnotedreadingresult    ACL     B   GRANT ALL ON TABLE public.vwjsonnotedreadingresult TO "Webgroup";
            public       postgres    false    389            �           1259    55463    vwlabcodesforsamples    VIEW     S  CREATE VIEW public.vwlabcodesforsamples AS
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
       public       postgres    false    238    238    234    234    234    229    229    238            �           0    0    TABLE vwlabcodesforsamples    ACL     >   GRANT ALL ON TABLE public.vwlabcodesforsamples TO "Webgroup";
            public       postgres    false    390            �           1259    55468    vwringsleaderboard    VIEW     [  CREATE VIEW public.vwringsleaderboard AS
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
       public       postgres    false    306    306    293    293    293    273    232    306            �           1259    55473    vwringwidthleaderboard    VIEW     p  CREATE VIEW public.vwringwidthleaderboard AS
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
       public       postgres    false    306    306    293    293    293    273    273    232    306            �           1259    55478    vwsampleleaderboard    VIEW       CREATE VIEW public.vwsampleleaderboard AS
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
       public       postgres    false    306    306    306    293    293    293    232            �           1259    55483    vwleaderboard    VIEW     \  CREATE VIEW public.vwleaderboard AS
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
       public       postgres    false    392    393    393    393    391    391    392            �           1259    55487    vwnotedreadingresult    VIEW       CREATE VIEW public.vwnotedreadingresult AS
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
       public       postgres    false    239    388    388    388    388    239    239    239    239    239    239    239            �           0    0    TABLE vwnotedreadingresult    ACL     >   GRANT ALL ON TABLE public.vwnotedreadingresult TO "Webgroup";
            public       postgres    false    395            �           1259    55491    vwstartyear    VIEW     �   CREATE VIEW public.vwstartyear AS
 SELECT tblreading.measurementid,
    min(tblreading.relyear) AS minofrelyear
   FROM public.tblreading
  GROUP BY tblreading.measurementid;
    DROP VIEW public.vwstartyear;
       public       postgres    false    273    273            �           0    0    TABLE vwstartyear    ACL     5   GRANT ALL ON TABLE public.vwstartyear TO "Webgroup";
            public       postgres    false    396            �           1259    55495    vwtblcurationevent    VIEW     T  CREATE VIEW public.vwtblcurationevent AS
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
       public       tellervo    false    249    325    325    249    249    249    249    249    249    249            �           1259    55499    vwtblcurationeventmostrecent    VIEW     ~  CREATE VIEW public.vwtblcurationeventmostrecent AS
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
       public       tellervo    false    397    397    249    249    249    249    249    249    249    249    397    397    397    397    397    397    397            �           1259    55503    vwtblbox    VIEW     "  CREATE VIEW public.vwtblbox AS
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
       public       postgres    false    244    238    244    244    238    244    244    244    244    398    398            �           0    0    TABLE vwtblbox    ACL     2   GRANT ALL ON TABLE public.vwtblbox TO "Webgroup";
            public       postgres    false    399            �           1259    55508 	   vwtblloan    VIEW     !  CREATE VIEW public.vwtblloan AS
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
       public       tellervo    false    236    236    236    236    236    236    236    236    236            �           1259    55512    vwtblmeasurement    VIEW     �  CREATE VIEW public.vwtblmeasurement AS
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
       public       postgres    false    232    232    232    232    232    232    232    232    330    330    293    293    232            �           1259    55516    vwtblobject    VIEW     *  CREATE VIEW public.vwtblobject AS
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
       public       tellervo    false    232    348    348    342    342    332    332    323    323    321    321    238    238    237    237    234    234    233    232    229    229    229    229    229    229    229    229    229    229    229    229    229    229    229    229    229    229    229    229    229    229    229    229    229    229    229    1542    1539    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1259    55521    vwtblproject    VIEW     �  CREATE VIEW public.vwtblproject AS
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
       public       tellervo    false    267    267    267    267    267    267    353    353    332    332    267    267    267    267    267    267    267    267    267    267    267            �           1259    55526    vwtblradius    VIEW     �  CREATE VIEW public.vwtblradius AS
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
       public       tellervo    false    237    237    237    319    237    237    237    237    319    332    237    237    237    237    237    237    332    237    237    237    237    237    237    237    237            �           1259    55531    vwtblsample    VIEW     `  CREATE VIEW public.vwtblsample AS
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
       public       pbrewer    false    238    328    328    358    358    360    360    390    398    398    398    390    390    238    238    238    238    238    238    238    238    238    238    238    238    238    238    238    238    238    238    238    238    2087            �           1259    55536    vwtbluserdefinedfieldandvalue    VIEW     �  CREATE VIEW public.vwtbluserdefinedfieldandvalue AS
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
       public       tellervo    false    305    305    369    369    369    369    369    369    369    305    305            �           1259    55540    vwtblvmeasurement    VIEW     |  CREATE VIEW public.vwtblvmeasurement AS
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
       public       tellervo    false    371    306    306    306    306    306    306    306    306    306    306    306    332    332    371            �           1259    55545    vwtblvmeasurementresult    VIEW       CREATE VIEW public.vwtblvmeasurementresult AS
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
       public       postgres    false    316    316    316    316    330    316    330    316    316            �           1259    55549    vwtlkptaxon    VIEW       CREATE VIEW public.vwtlkptaxon AS
 SELECT taxon.taxonid,
    taxon.label AS taxonlabel,
    taxon.parenttaxonid,
    taxon.colid,
    taxon.colparentid,
    rank.taxonrank
   FROM (public.tlkptaxon taxon
     JOIN public.tlkptaxonrank rank ON ((rank.taxonrankid = taxon.taxonrankid)));
    DROP VIEW public.vwtlkptaxon;
       public       postgres    false    365    366    366    365    365    365    365    365            �           0    0    TABLE vwtlkptaxon    ACL     5   GRANT ALL ON TABLE public.vwtlkptaxon TO "Webgroup";
            public       postgres    false    409            �           1259    55553    vwvmeasurement    VIEW       CREATE VIEW public.vwvmeasurement AS
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
       public       postgres    false    316    316    316    306    306    316    316    316            �           1259    55557    yenikapi    TABLE     �  CREATE TABLE public.yenikapi (
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
       public         postgres    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2            �           1259    55566    yenikapi_gid_seq    SEQUENCE     y   CREATE SEQUENCE public.yenikapi_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.yenikapi_gid_seq;
       public       postgres    false    411            �           0    0    yenikapi_gid_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE public.yenikapi_gid_seq OWNED BY public.yenikapi.gid;
            public       postgres    false    412            �           2604    55568    tblconfig configid    DEFAULT     x   ALTER TABLE ONLY public.tblconfig ALTER COLUMN configid SET DEFAULT nextval('public.tblconfig_configid_seq'::regclass);
 A   ALTER TABLE public.tblconfig ALTER COLUMN configid DROP DEFAULT;
       public       postgres    false    246    245            �           2604    55569    tblcrossdate crossdateid    DEFAULT     �   ALTER TABLE ONLY public.tblcrossdate ALTER COLUMN crossdateid SET DEFAULT nextval('public.tblcrossdate_crossdateid_seq'::regclass);
 G   ALTER TABLE public.tblcrossdate ALTER COLUMN crossdateid DROP DEFAULT;
       public       postgres    false    248    247            �           2604    55570     tblcurationevent curationeventid    DEFAULT     �   ALTER TABLE ONLY public.tblcurationevent ALTER COLUMN curationeventid SET DEFAULT nextval('public.tblcuration_curationid_seq'::regclass);
 O   ALTER TABLE public.tblcurationevent ALTER COLUMN curationeventid DROP DEFAULT;
       public       tellervo    false    250    249            �           2604    55571 $   tblcustomvocabterm customvocabtermid    DEFAULT     �   ALTER TABLE ONLY public.tblcustomvocabterm ALTER COLUMN customvocabtermid SET DEFAULT nextval('public.tblcustomvocabterm_customvocabtermid_seq'::regclass);
 S   ALTER TABLE public.tblcustomvocabterm ALTER COLUMN customvocabtermid DROP DEFAULT;
       public       postgres    false    254    253            r           2604    55572    tblelement gispkey    DEFAULT     x   ALTER TABLE ONLY public.tblelement ALTER COLUMN gispkey SET DEFAULT nextval('public.tblelement_gispkey_seq'::regclass);
 A   ALTER TABLE public.tblelement ALTER COLUMN gispkey DROP DEFAULT;
       public       postgres    false    256    234            �           2604    55573 (   tblenvironmentaldata environmentaldataid    DEFAULT     �   ALTER TABLE ONLY public.tblenvironmentaldata ALTER COLUMN environmentaldataid SET DEFAULT nextval('public.tblenvironmentaldata_environmentaldataid_seq'::regclass);
 W   ALTER TABLE public.tblenvironmentaldata ALTER COLUMN environmentaldataid DROP DEFAULT;
       public       postgres    false    258    257            h           2604    55574    tblmeasurement measurementid    DEFAULT     �   ALTER TABLE ONLY public.tblmeasurement ALTER COLUMN measurementid SET DEFAULT nextval('public.tblmeasurement_measurementid_seq'::regclass);
 K   ALTER TABLE public.tblmeasurement ALTER COLUMN measurementid DROP DEFAULT;
       public       postgres    false    261    232            �           2604    55575    tblobjectregion objectregionid    DEFAULT     �   ALTER TABLE ONLY public.tblobjectregion ALTER COLUMN objectregionid SET DEFAULT nextval('public.tblobjectregion_objectregionid_seq'::regclass);
 M   ALTER TABLE public.tblobjectregion ALTER COLUMN objectregionid DROP DEFAULT;
       public       postgres    false    264    263            �           2604    55576 *   tblprojectprojecttype projectprojecttypeid    DEFAULT     �   ALTER TABLE ONLY public.tblprojectprojecttype ALTER COLUMN projectprojecttypeid SET DEFAULT nextval('public.tblprojectprojecttype_projectprojecttypeid_seq'::regclass);
 Y   ALTER TABLE public.tblprojectprojecttype ALTER COLUMN projectprojecttypeid DROP DEFAULT;
       public       tellervo    false    269    268            �           2604    55577    tblrasterlayer rasterlayerid    DEFAULT     �   ALTER TABLE ONLY public.tblrasterlayer ALTER COLUMN rasterlayerid SET DEFAULT nextval('public.tblrasterlayer_rasterlayerid_seq'::regclass);
 K   ALTER TABLE public.tblrasterlayer ALTER COLUMN rasterlayerid DROP DEFAULT;
       public       postgres    false    272    271            �           2604    55578    tblreading readingid    DEFAULT     |   ALTER TABLE ONLY public.tblreading ALTER COLUMN readingid SET DEFAULT nextval('public.tblreading_readingid_seq'::regclass);
 C   ALTER TABLE public.tblreading ALTER COLUMN readingid DROP DEFAULT;
       public       postgres    false    274    273            �           2604    55579 *   tblreadingreadingnote readingreadingnoteid    DEFAULT     �   ALTER TABLE ONLY public.tblreadingreadingnote ALTER COLUMN readingreadingnoteid SET DEFAULT nextval('public.tblreadingreadingnote_readingreadingnoteid_seq'::regclass);
 Y   ALTER TABLE public.tblreadingreadingnote ALTER COLUMN readingreadingnoteid DROP DEFAULT;
       public       postgres    false    276    275            �           2604    55580    tblredate redateid    DEFAULT     x   ALTER TABLE ONLY public.tblredate ALTER COLUMN redateid SET DEFAULT nextval('public.tblredate_redateid_seq'::regclass);
 A   ALTER TABLE public.tblredate ALTER COLUMN redateid DROP DEFAULT;
       public       postgres    false    278    277            �           2604    55581    tblrequestlog requestlogid    DEFAULT     �   ALTER TABLE ONLY public.tblrequestlog ALTER COLUMN requestlogid SET DEFAULT nextval('public.tblrequestlog_requestlogid_seq'::regclass);
 I   ALTER TABLE public.tblrequestlog ALTER COLUMN requestlogid DROP DEFAULT;
       public       postgres    false    282    281            �           2604    55582 $   tblsecuritydefault securitydefaultid    DEFAULT     �   ALTER TABLE ONLY public.tblsecuritydefault ALTER COLUMN securitydefaultid SET DEFAULT nextval('public.tblsecuritydefault_securitydefaultid_seq'::regclass);
 S   ALTER TABLE public.tblsecuritydefault ALTER COLUMN securitydefaultid DROP DEFAULT;
       public       postgres    false    285    284            �           2604    55583 $   tblsecurityelement securityelementid    DEFAULT     �   ALTER TABLE ONLY public.tblsecurityelement ALTER COLUMN securityelementid SET DEFAULT nextval('public.tblsecuritytree_securityelementid_seq'::regclass);
 S   ALTER TABLE public.tblsecurityelement ALTER COLUMN securityelementid DROP DEFAULT;
       public       postgres    false    292    286            z           2604    55584     tblsecuritygroup securitygroupid    DEFAULT     �   ALTER TABLE ONLY public.tblsecuritygroup ALTER COLUMN securitygroupid SET DEFAULT nextval('public.tblsecuritygroup_securitygroupid_seq'::regclass);
 O   ALTER TABLE public.tblsecuritygroup ALTER COLUMN securitygroupid DROP DEFAULT;
       public       postgres    false    287    235            �           2604    55585 4   tblsecuritygroupmembership securitygroupmembershipid    DEFAULT     �   ALTER TABLE ONLY public.tblsecuritygroupmembership ALTER COLUMN securitygroupmembershipid SET DEFAULT nextval('public.tblsecuritygroupmembership_securitygroupmembershipid_seq'::regclass);
 c   ALTER TABLE public.tblsecuritygroupmembership ALTER COLUMN securitygroupmembershipid DROP DEFAULT;
       public       postgres    false    289    288            �           2604    55586 "   tblsecurityobject securityobjectid    DEFAULT     �   ALTER TABLE ONLY public.tblsecurityobject ALTER COLUMN securityobjectid SET DEFAULT nextval('public.tblsecurityobject_securityobjectid_seq'::regclass);
 Q   ALTER TABLE public.tblsecurityobject ALTER COLUMN securityobjectid DROP DEFAULT;
       public       postgres    false    291    290            �           2604    55587 5   tblsecurityusermembership tblsecurityusermembershipid    DEFAULT     �   ALTER TABLE ONLY public.tblsecurityusermembership ALTER COLUMN tblsecurityusermembershipid SET DEFAULT nextval('public.tblsecurityusermembership_tblsecurityusermembershipid_seq'::regclass);
 d   ALTER TABLE public.tblsecurityusermembership ALTER COLUMN tblsecurityusermembershipid DROP DEFAULT;
       public       postgres    false    295    294            �           2604    55588 .   tblsecurityvmeasurement securityvmeasurementid    DEFAULT     �   ALTER TABLE ONLY public.tblsecurityvmeasurement ALTER COLUMN securityvmeasurementid SET DEFAULT nextval('public.tblsecurityvmeasurement_securityvmeasurementid_seq'::regclass);
 ]   ALTER TABLE public.tblsecurityvmeasurement ALTER COLUMN securityvmeasurementid DROP DEFAULT;
       public       postgres    false    297    296            �           2604    55589 "   tblsupportedclient supportclientid    DEFAULT     �   ALTER TABLE ONLY public.tblsupportedclient ALTER COLUMN supportclientid SET DEFAULT nextval('public.tblsupportedclients_supportclientid_seq'::regclass);
 Q   ALTER TABLE public.tblsupportedclient ALTER COLUMN supportclientid DROP DEFAULT;
       public       postgres    false    299    298            �           2604    55590    tbltruncate truncateid    DEFAULT     �   ALTER TABLE ONLY public.tbltruncate ALTER COLUMN truncateid SET DEFAULT nextval('public.tbltruncate_truncateid_seq'::regclass);
 E   ALTER TABLE public.tbltruncate ALTER COLUMN truncateid DROP DEFAULT;
       public       postgres    false    302    301            �           2604    55591    tblupgradelog upgradelogid    DEFAULT     �   ALTER TABLE ONLY public.tblupgradelog ALTER COLUMN upgradelogid SET DEFAULT nextval('public.tblupgradelog_upgradelogid_seq'::regclass);
 I   ALTER TABLE public.tblupgradelog ALTER COLUMN upgradelogid DROP DEFAULT;
       public       postgres    false    304    303            k           2604    55592 *   tblvmeasurementderivedcache derivedcacheid    DEFAULT     �   ALTER TABLE ONLY public.tblvmeasurementderivedcache ALTER COLUMN derivedcacheid SET DEFAULT nextval('public.tblvmeasurementderivedcache_derivedcacheid_seq'::regclass);
 Y   ALTER TABLE public.tblvmeasurementderivedcache ALTER COLUMN derivedcacheid DROP DEFAULT;
       public       postgres    false    308    233            �           2604    55593 (   tblvmeasurementgroup vmeasurementgroupid    DEFAULT     �   ALTER TABLE ONLY public.tblvmeasurementgroup ALTER COLUMN vmeasurementgroupid SET DEFAULT nextval('public.tblvmeasurementgroup_vmeasurementgroupid_seq'::regclass);
 W   ALTER TABLE public.tblvmeasurementgroup ALTER COLUMN vmeasurementgroupid DROP DEFAULT;
       public       postgres    false    310    309            Y           2604    55594 0   tblvmeasurementmetacache vmeasurementmetacacheid    DEFAULT     �   ALTER TABLE ONLY public.tblvmeasurementmetacache ALTER COLUMN vmeasurementmetacacheid SET DEFAULT nextval('public.tblvmeasurementmetacache_vmeasurementmetacacheid_seq'::regclass);
 _   ALTER TABLE public.tblvmeasurementmetacache ALTER COLUMN vmeasurementmetacacheid DROP DEFAULT;
       public       postgres    false    311    231            �           2604    55595 8   tblvmeasurementreadingresult vmeasurementreadingresultid    DEFAULT     �   ALTER TABLE ONLY public.tblvmeasurementreadingresult ALTER COLUMN vmeasurementreadingresultid SET DEFAULT nextval('public.tblvmeasurementreadingresult_vmeasurementreadingresultid_seq'::regclass);
 g   ALTER TABLE public.tblvmeasurementreadingresult ALTER COLUMN vmeasurementreadingresultid DROP DEFAULT;
       public       postgres    false    313    239            �           2604    55596 (   tblvmeasurementtotag vmeasurementtotagid    DEFAULT     �   ALTER TABLE ONLY public.tblvmeasurementtotag ALTER COLUMN vmeasurementtotagid SET DEFAULT nextval('public.tblvmeasurementtotag_vmeasurementtotagid_seq'::regclass);
 W   ALTER TABLE public.tblvmeasurementtotag ALTER COLUMN vmeasurementtotagid DROP DEFAULT;
       public       tellervo    false    318    317            �           2604    55597 3   tlkpcomplexpresenceabsence complexpresenceabsenceid    DEFAULT     �   ALTER TABLE ONLY public.tlkpcomplexpresenceabsence ALTER COLUMN complexpresenceabsenceid SET DEFAULT nextval('public.tlkpcomplexpresenceabsence_complexpresenceabsenceid_seq'::regclass);
 b   ALTER TABLE public.tlkpcomplexpresenceabsence ALTER COLUMN complexpresenceabsenceid DROP DEFAULT;
       public       postgres    false    320    319            �           2604    55598 '   tlkpcoveragetemporal coveragetemporalid    DEFAULT     �   ALTER TABLE ONLY public.tlkpcoveragetemporal ALTER COLUMN coveragetemporalid SET DEFAULT nextval('public.tlkpcoveragetemporal_coveragetemporalid_seq'::regclass);
 V   ALTER TABLE public.tlkpcoveragetemporal ALTER COLUMN coveragetemporalid DROP DEFAULT;
       public       postgres    false    322    321            �           2604    55599 ;   tlkpcoveragetemporalfoundation coveragetemporalfoundationid    DEFAULT     �   ALTER TABLE ONLY public.tlkpcoveragetemporalfoundation ALTER COLUMN coveragetemporalfoundationid SET DEFAULT nextval('public.tlkpcoveragetemporalfoundation_coveragetemporalfoundationid_seq'::regclass);
 j   ALTER TABLE public.tlkpcoveragetemporalfoundation ALTER COLUMN coveragetemporalfoundationid DROP DEFAULT;
       public       postgres    false    324    323            �           2604    55600 #   tlkpcurationstatus curationstatusid    DEFAULT     �   ALTER TABLE ONLY public.tlkpcurationstatus ALTER COLUMN curationstatusid SET DEFAULT nextval('public.tlkpcurationstatus_curationstatusid_seq'::regclass);
 R   ALTER TABLE public.tlkpcurationstatus ALTER COLUMN curationstatusid DROP DEFAULT;
       public       tellervo    false    326    325            �           2604    55601 !   tlkpdatecertainty datecertaintyid    DEFAULT     �   ALTER TABLE ONLY public.tlkpdatecertainty ALTER COLUMN datecertaintyid SET DEFAULT nextval('public.tlkpdatecertainty_datecertaintyid_seq'::regclass);
 P   ALTER TABLE public.tlkpdatecertainty ALTER COLUMN datecertaintyid DROP DEFAULT;
       public       postgres    false    329    328            �           2604    55602    tlkpdatingtype datingtypeid    DEFAULT     �   ALTER TABLE ONLY public.tlkpdatingtype ALTER COLUMN datingtypeid SET DEFAULT nextval('public.tlkpdatingtype_datingtypeid_seq'::regclass);
 J   ALTER TABLE public.tlkpdatingtype ALTER COLUMN datingtypeid DROP DEFAULT;
       public       postgres    false    331    330            �           2604    55603    tlkpdomain domainid    DEFAULT     z   ALTER TABLE ONLY public.tlkpdomain ALTER COLUMN domainid SET DEFAULT nextval('public.tlkpdomain_domainid_seq'::regclass);
 B   ALTER TABLE public.tlkpdomain ALTER COLUMN domainid DROP DEFAULT;
       public       tellervo    false    333    332            �           2604    55604 -   tlkpelementauthenticity elementauthenticityid    DEFAULT     �   ALTER TABLE ONLY public.tlkpelementauthenticity ALTER COLUMN elementauthenticityid SET DEFAULT nextval('public.tlkpelementauthenticity_elementauthenticityid_seq'::regclass);
 \   ALTER TABLE public.tlkpelementauthenticity ALTER COLUMN elementauthenticityid DROP DEFAULT;
       public       postgres    false    335    334            �           2604    55605    tlkpelementshape elementshapeid    DEFAULT     �   ALTER TABLE ONLY public.tlkpelementshape ALTER COLUMN elementshapeid SET DEFAULT nextval('public.tlkpelementshape_elementshapeid_seq'::regclass);
 N   ALTER TABLE public.tlkpelementshape ALTER COLUMN elementshapeid DROP DEFAULT;
       public       postgres    false    337    336            �           2604    55606    tlkpelementtype elementtypeid    DEFAULT     �   ALTER TABLE ONLY public.tlkpelementtype ALTER COLUMN elementtypeid SET DEFAULT nextval('public.tlkpelementtype_elementtypeid_seq'::regclass);
 L   ALTER TABLE public.tlkpelementtype ALTER COLUMN elementtypeid DROP DEFAULT;
       public       postgres    false    339    338            �           2604    55607    tlkplocationtype locationtypeid    DEFAULT     �   ALTER TABLE ONLY public.tlkplocationtype ALTER COLUMN locationtypeid SET DEFAULT nextval('public.tlkplocationtype_locationtypeid_seq'::regclass);
 N   ALTER TABLE public.tlkplocationtype ALTER COLUMN locationtypeid DROP DEFAULT;
       public       postgres    false    343    342            �           2604    55608 -   tlkpmeasurementvariable measurementvariableid    DEFAULT     �   ALTER TABLE ONLY public.tlkpmeasurementvariable ALTER COLUMN measurementvariableid SET DEFAULT nextval('public.tlkpmeasurementvariable_measurementvariableid_seq'::regclass);
 \   ALTER TABLE public.tlkpmeasurementvariable ALTER COLUMN measurementvariableid DROP DEFAULT;
       public       postgres    false    345    344            �           2604    55609 %   tlkpmeasuringmethod measuringmethodid    DEFAULT     �   ALTER TABLE ONLY public.tlkpmeasuringmethod ALTER COLUMN measuringmethodid SET DEFAULT nextval('public.tlkpmeasuringmethod_measuringmethodid_seq'::regclass);
 T   ALTER TABLE public.tlkpmeasuringmethod ALTER COLUMN measuringmethodid DROP DEFAULT;
       public       postgres    false    347    346            �           2604    55610    tlkpobjecttype objecttypeid    DEFAULT     �   ALTER TABLE ONLY public.tlkpobjecttype ALTER COLUMN objecttypeid SET DEFAULT nextval('public.tlkpobjecttype_objecttype_seq'::regclass);
 J   ALTER TABLE public.tlkpobjecttype ALTER COLUMN objecttypeid DROP DEFAULT;
       public       postgres    false    349    348            �           2604    55611 %   tlkppresenceabsence presenceabsenceid    DEFAULT     �   ALTER TABLE ONLY public.tlkppresenceabsence ALTER COLUMN presenceabsenceid SET DEFAULT nextval('public.tlkppresenceabsence_presenceabsenceid_seq'::regclass);
 T   ALTER TABLE public.tlkppresenceabsence ALTER COLUMN presenceabsenceid DROP DEFAULT;
       public       postgres    false    351    350            V           2604    55612    tlkpreadingnote readingnoteid    DEFAULT     �   ALTER TABLE ONLY public.tlkpreadingnote ALTER COLUMN readingnoteid SET DEFAULT nextval('public.tlkpreadingnote_readingnoteid_seq'::regclass);
 L   ALTER TABLE public.tlkpreadingnote ALTER COLUMN readingnoteid DROP DEFAULT;
       public       postgres    false    357    230            �           2604    55613    tlkpsamplestatus samplestatusid    DEFAULT     �   ALTER TABLE ONLY public.tlkpsamplestatus ALTER COLUMN samplestatusid SET DEFAULT nextval('public.tlkpsamplestatus_samplestatusid_seq'::regclass);
 N   ALTER TABLE public.tlkpsamplestatus ALTER COLUMN samplestatusid DROP DEFAULT;
       public       tellervo    false    359    358            �           2604    55614    tlkpsampletype sampletypeid    DEFAULT     �   ALTER TABLE ONLY public.tlkpsampletype ALTER COLUMN sampletypeid SET DEFAULT nextval('public.tlkpsampletype_sampletypeid_seq'::regclass);
 J   ALTER TABLE public.tlkpsampletype ALTER COLUMN sampletypeid DROP DEFAULT;
       public       postgres    false    361    360            �           2604    55615 +   tlkpsecuritypermission securitypermissionid    DEFAULT     �   ALTER TABLE ONLY public.tlkpsecuritypermission ALTER COLUMN securitypermissionid SET DEFAULT nextval('public.tlkpsecuritypermission_securitypermissionid_seq'::regclass);
 Z   ALTER TABLE public.tlkpsecuritypermission ALTER COLUMN securitypermissionid DROP DEFAULT;
       public       postgres    false    363    362            �           2604    55616 '   tlkptrackinglocation trackinglocationid    DEFAULT     �   ALTER TABLE ONLY public.tlkptrackinglocation ALTER COLUMN trackinglocationid SET DEFAULT nextval('public.tblcurationlocation_curationlocationid_seq'::regclass);
 V   ALTER TABLE public.tlkptrackinglocation ALTER COLUMN trackinglocationid DROP DEFAULT;
       public       postgres    false    252    251            �           2604    55617    tlkpunit unitid    DEFAULT     t   ALTER TABLE ONLY public.tlkpunit ALTER COLUMN unitid SET DEFAULT nextval('public.tlkpunits_unitsid_seq'::regclass);
 >   ALTER TABLE public.tlkpunit ALTER COLUMN unitid DROP DEFAULT;
       public       postgres    false    368    367            �           2604    55618 #   tlkpvmeasurementop vmeasurementopid    DEFAULT     �   ALTER TABLE ONLY public.tlkpvmeasurementop ALTER COLUMN vmeasurementopid SET DEFAULT nextval('public.tlkpvmeasurementop_vmeasurementopid_seq'::regclass);
 R   ALTER TABLE public.tlkpvmeasurementop ALTER COLUMN vmeasurementopid DROP DEFAULT;
       public       postgres    false    372    371            �           2604    55619    tlkpvocabulary vocabularyid    DEFAULT     �   ALTER TABLE ONLY public.tlkpvocabulary ALTER COLUMN vocabularyid SET DEFAULT nextval('public.tlkpvocabulary_vocabularyid_seq'::regclass);
 J   ALTER TABLE public.tlkpvocabulary ALTER COLUMN vocabularyid DROP DEFAULT;
       public       postgres    false    374    373            �           2604    55620    tlkpwmsserver wmsserverid    DEFAULT     �   ALTER TABLE ONLY public.tlkpwmsserver ALTER COLUMN wmsserverid SET DEFAULT nextval('public.tlkpwmsserver_wmsserverid_seq'::regclass);
 H   ALTER TABLE public.tlkpwmsserver ALTER COLUMN wmsserverid DROP DEFAULT;
       public       postgres    false    376    375            �           2604    55621    yenikapi gid    DEFAULT     l   ALTER TABLE ONLY public.yenikapi ALTER COLUMN gid SET DEFAULT nextval('public.yenikapi_gid_seq'::regclass);
 ;   ALTER TABLE public.yenikapi ALTER COLUMN gid DROP DEFAULT;
       public       postgres    false    412    411            e          0    54848    elementimporterror 
   TABLE DATA               �   COPY public.elementimporterror (id, boxname, regioncode, elementstartnum, elementendnum, samplecode, taxa, sitecode, missing, notes, sampletype, currentelement, error, fixed) FROM stdin;
    public       postgres    false    240   ��      f          0    54855 	   inventory 
   TABLE DATA               �   COPY public.inventory (id, boxname, regioncode, elementstartnum, elementendnum, samplecode, taxa, sitecode, missing, notes, sampletype) FROM stdin;
    public       postgres    false    241   �      g          0    54861    sampleimporterror 
   TABLE DATA               �   COPY public.sampleimporterror (id, boxname, regioncode, elementstartnum, elementendnum, samplecode, taxa, sitecode, missing, notes, sampletype, currentelement, error, fixed) FROM stdin;
    public       postgres    false    242   )�      h          0    54868    sampleimportfixerror 
   TABLE DATA               �   COPY public.sampleimportfixerror (id, boxname, regioncode, elementstartnum, elementendnum, samplecode, taxa, sitecode, missing, notes, sampletype, currentelement, error, fixed) FROM stdin;
    public       postgres    false    243   F�      D          0    53439    spatial_ref_sys 
   TABLE DATA               X   COPY public.spatial_ref_sys (srid, auth_name, auth_srid, srtext, proj4text) FROM stdin;
    public       postgres    false    210   c�      i          0    54875    tblbox 
   TABLE DATA               �   COPY public.tblbox (boxid, title, curationlocation, trackinglocation, createdtimestamp, lastmodifiedtimestamp, comments) FROM stdin;
    public       postgres    false    244   ��      j          0    54884 	   tblconfig 
   TABLE DATA               F   COPY public.tblconfig (configid, key, value, description) FROM stdin;
    public       postgres    false    245   ��      l          0    54892    tblcrossdate 
   TABLE DATA                  COPY public.tblcrossdate (crossdateid, vmeasurementid, mastervmeasurementid, startyear, justification, confidence) FROM stdin;
    public       postgres    false    247   O�      n          0    54903    tblcurationevent 
   TABLE DATA               �   COPY public.tblcurationevent (curationeventid, curationstatusid, sampleid, createdtimestamp, loanid, notes, storagelocation, curatorid, boxid) FROM stdin;
    public       tellervo    false    249   l�      r          0    54920    tblcustomvocabterm 
   TABLE DATA               t   COPY public.tblcustomvocabterm (customvocabtermid, "table", field, id, customvocabterm, dictionaryname) FROM stdin;
    public       postgres    false    253   ��      _          0    54278 
   tblelement 
   TABLE DATA               �  COPY public.tblelement (elementid, taxonid, locationprecision, code, createdtimestamp, lastmodifiedtimestamp, locationgeometry, islivetree, originaltaxonname, locationtypeid, locationcomment, file, description, processing, marks, diameter, width, height, depth, unsupportedxml, unitsold, objectid, elementtypeid, elementauthenticityid, elementshapeid, altitudeint, slopeangle, slopeazimuth, soildescription, soildepth, bedrockdescription, comments, locationaddressline2, locationcityortown, locationstateprovinceregion, locationpostalcode, locationcountry, locationaddressline1, altitude, gispkey, units, authenticity, domainid) FROM stdin;
    public       postgres    false    234   �      v          0    54932    tblenvironmentaldata 
   TABLE DATA               d   COPY public.tblenvironmentaldata (environmentaldataid, elementid, rasterlayerid, value) FROM stdin;
    public       postgres    false    257   ��      x          0    54937    tbliptracking 
   TABLE DATA               L   COPY public.tbliptracking (ipaddr, "timestamp", securityuserid) FROM stdin;
    public       postgres    false    259   ��      y          0    54944    tbllaboratory 
   TABLE DATA               z   COPY public.tbllaboratory (laboratoryid, name, acronym, address1, address2, city, state, postalcode, country) FROM stdin;
    public       tellervo    false    260   �      a          0    54307    tblloan 
   TABLE DATA               z   COPY public.tblloan (loanid, firstname, lastname, organisation, duedate, issuedate, returndate, files, notes) FROM stdin;
    public       tellervo    false    236   6�      ]          0    54245    tblmeasurement 
   TABLE DATA               @  COPY public.tblmeasurement (measurementid, radiusid, isreconciled, startyear, islegacycleaned, importtablename, createdtimestamp, lastmodifiedtimestamp, datingtypeid, datingerrorpositive, datingerrornegative, measurementvariableid, unitid, power, provenance, measuringmethodid, measuredbyid, supervisedbyid) FROM stdin;
    public       postgres    false    232   ��      Z          0    54195 	   tblobject 
   TABLE DATA               �  COPY public.tblobject (objectid, title, code, createdtimestamp, lastmodifiedtimestamp, locationgeometry, locationtypeid, locationprecision, locationcomment, file, creator, owner, parentobjectid, description, objecttypeid, coveragetemporalid, coveragetemporalfoundationid, comments, coveragetemporal, coveragetemporalfoundation, locationaddressline1, locationaddressline2, locationcityortown, locationstateprovinceregion, locationpostalcode, locationcountry, vegetationtype, domainid, projectid) FROM stdin;
    public       postgres    false    229   ��      |          0    54955    tblobjectregion 
   TABLE DATA               M   COPY public.tblobjectregion (objectregionid, objectid, regionid) FROM stdin;
    public       postgres    false    263   &�      ~          0    54960    tblodkdefinition 
   TABLE DATA               {   COPY public.tblodkdefinition (odkdefinitionid, createdtimestamp, name, definition, ownerid, ispublic, version) FROM stdin;
    public       tellervo    false    265   C�                0    54970    tblodkinstance 
   TABLE DATA               s   COPY public.tblodkinstance (odkinstanceid, createdtimestamp, ownerid, name, instance, files, deviceid) FROM stdin;
    public       tellervo    false    266   `�      �          0    54978 
   tblproject 
   TABLE DATA               �   COPY public.tblproject (projectid, domainid, title, createdtimestamp, lastmodifiedtimestamp, comments, description, file, projectcategoryid, investigator, period, requestdate, commissioner, reference, research, projecttypes, laboratories) FROM stdin;
    public       tellervo    false    267   }�      �          0    54988    tblprojectprojecttype 
   TABLE DATA               _   COPY public.tblprojectprojecttype (projectprojecttypeid, projectid, projecttypeid) FROM stdin;
    public       tellervo    false    268   ��      b          0    54317 	   tblradius 
   TABLE DATA               �  COPY public.tblradius (radiusid, sampleid, code, createdtimestamp, lastmodifiedtimestamp, numberofsapwoodrings, pithid, barkpresent, lastringunderbark, missingheartwoodringstopith, missingheartwoodringstopithfoundation, missingsapwoodringstobark, missingsapwoodringstobarkfoundation, sapwoodid, heartwoodid, azimuth, comments, lastringunderbarkpresent, nrofunmeasuredinnerrings, nrofunmeasuredouterrings, lrubpa, domainid) FROM stdin;
    public       postgres    false    237   �      �          0    54995    tblrasterlayer 
   TABLE DATA               c   COPY public.tblrasterlayer (rasterlayerid, name, filename, issystemlayer, description) FROM stdin;
    public       postgres    false    271   ��      �          0    55004 
   tblreading 
   TABLE DATA               b   COPY public.tblreading (readingid, measurementid, relyear, reading, ewwidth, lwwidth) FROM stdin;
    public       postgres    false    273   ��      �          0    55009    tblreadingreadingnote 
   TABLE DATA               �   COPY public.tblreadingreadingnote (readingid, readingnoteid, createdtimestamp, lastmodifiedtimestamp, readingreadingnoteid) FROM stdin;
    public       postgres    false    275   "�      �          0    55016 	   tblredate 
   TABLE DATA               g   COPY public.tblredate (redateid, vmeasurementid, startyear, redatingtypeid, justification) FROM stdin;
    public       postgres    false    277   ?�      �          0    55024 	   tblregion 
   TABLE DATA               �   COPY public.tblregion (regionid, fips_cntry, gmi_cntry, regionname, sovereign, pop_cntry, sqkm_cntry, sqmi_cntry, curr_type, curr_code, landlocked, color_map, the_geom) FROM stdin;
    public       postgres    false    279   \�      �          0    55034    tblrequestlog 
   TABLE DATA               �   COPY public.tblrequestlog (requestlogid, request, ipaddr, createdtimestamp, wsversion, page, client, securityuserid) FROM stdin;
    public       postgres    false    281   y�      c          0    54332 	   tblsample 
   TABLE DATA               O  COPY public.tblsample (sampleid, code, elementid, samplingdate, createdtimestamp, lastmodifiedtimestamp, type, identifierdomain, file, "position", state, knots, description, datecertaintyid, typeid, boxid, comments, externalid, domainid, samplestatusid, samplingyear, samplingmonth, samplingdateprec, userdefinedfielddata) FROM stdin;
    public       postgres    false    238   5�      �          0    55045    tblsecuritydefault 
   TABLE DATA               f   COPY public.tblsecuritydefault (securitydefaultid, securitygroupid, securitypermissionid) FROM stdin;
    public       postgres    false    284   ��      �          0    55050    tblsecurityelement 
   TABLE DATA               q   COPY public.tblsecurityelement (elementid, securitygroupid, securitypermissionid, securityelementid) FROM stdin;
    public       postgres    false    286   7�      `          0    54297    tblsecuritygroup 
   TABLE DATA               X   COPY public.tblsecuritygroup (securitygroupid, name, description, isactive) FROM stdin;
    public       postgres    false    235   T�      �          0    55055    tblsecuritygroupmembership 
   TABLE DATA               |   COPY public.tblsecuritygroupmembership (securitygroupmembershipid, parentsecuritygroupid, childsecuritygroupid) FROM stdin;
    public       postgres    false    288   ��      �          0    55060    tblsecurityobject 
   TABLE DATA               n   COPY public.tblsecurityobject (securityobjectid, objectid, securitygroupid, securitypermissionid) FROM stdin;
    public       postgres    false    290   ��      �          0    55067    tblsecurityuser 
   TABLE DATA               y   COPY public.tblsecurityuser (username, password, firstname, lastname, isactive, securityuserid, odkpassword) FROM stdin;
    public       postgres    false    293   �      �          0    55075    tblsecurityusermembership 
   TABLE DATA               q   COPY public.tblsecurityusermembership (tblsecurityusermembershipid, securitygroupid, securityuserid) FROM stdin;
    public       postgres    false    294   �      �          0    55080    tblsecurityvmeasurement 
   TABLE DATA               �   COPY public.tblsecurityvmeasurement (securityvmeasurementid, vmeasurementid, securitygroupid, securitypermissionid) FROM stdin;
    public       postgres    false    296   ��      �          0    55085    tblsupportedclient 
   TABLE DATA               Q   COPY public.tblsupportedclient (supportclientid, client, minversion) FROM stdin;
    public       postgres    false    298   ��      �          0    55093    tbltag 
   TABLE DATA               =   COPY public.tbltag (tagid, tag, ownerid, global) FROM stdin;
    public       tellervo    false    300   3�      �          0    55101    tbltruncate 
   TABLE DATA               j   COPY public.tbltruncate (truncateid, vmeasurementid, startrelyear, endrelyear, justification) FROM stdin;
    public       postgres    false    301   P�      �          0    55109    tblupgradelog 
   TABLE DATA               L   COPY public.tblupgradelog (upgradelogid, filename, "timestamp") FROM stdin;
    public       postgres    false    303   m�      �          0    55118    tbluserdefinedfieldvalue 
   TABLE DATA               p   COPY public.tbluserdefinedfieldvalue (userdefinedfieldvalueid, userdefinedfieldid, value, entityid) FROM stdin;
    public       tellervo    false    305   S�      �          0    55125    tblvmeasurement 
   TABLE DATA               �   COPY public.tblvmeasurement (vmeasurementid, measurementid, vmeasurementopid, vmeasurementopparameter, code, comments, ispublished, createdtimestamp, lastmodifiedtimestamp, isgenerating, objective, version, birthdate, owneruserid, domainid) FROM stdin;
    public       postgres    false    306   p�      ^          0    54274    tblvmeasurementderivedcache 
   TABLE DATA               d   COPY public.tblvmeasurementderivedcache (derivedcacheid, vmeasurementid, measurementid) FROM stdin;
    public       postgres    false    233   ��      �          0    55142    tblvmeasurementgroup 
   TABLE DATA               i   COPY public.tblvmeasurementgroup (vmeasurementid, membervmeasurementid, vmeasurementgroupid) FROM stdin;
    public       postgres    false    309   ��      \          0    54222    tblvmeasurementmetacache 
   TABLE DATA               �   COPY public.tblvmeasurementmetacache (vmeasurementid, startyear, readingcount, measurementcount, vmextent, vmeasurementmetacacheid, objectcode, objectcount, commontaxonname, taxoncount, prefix, datingtypeid) FROM stdin;
    public       postgres    false    231   �      �          0    55149     tblvmeasurementreadingnoteresult 
   TABLE DATA               x   COPY public.tblvmeasurementreadingnoteresult (vmeasurementresultid, relyear, readingnoteid, inheritedcount) FROM stdin;
    public       postgres    false    312   ��      d          0    54356    tblvmeasurementreadingresult 
   TABLE DATA               �   COPY public.tblvmeasurementreadingresult (vmeasurementreadingresultid, vmeasurementresultid, relyear, reading, wjinc, wjdec, count, readingid, ewwidth, lwwidth) FROM stdin;
    public       postgres    false    239   ��      �          0    55157 !   tblvmeasurementrelyearreadingnote 
   TABLE DATA               �   COPY public.tblvmeasurementrelyearreadingnote (vmeasurementid, relyear, readingnoteid, disabledoverride, createdtimestamp, lastmodifiedtimestamp, relyearreadingnoteid) FROM stdin;
    public       postgres    false    315   ��      �          0    55164    tblvmeasurementresult 
   TABLE DATA               K  COPY public.tblvmeasurementresult (vmeasurementresultid, vmeasurementid, radiusid, isreconciled, startyear, islegacycleaned, createdtimestamp, lastmodifiedtimestamp, vmeasurementresultmasterid, owneruserid, vmeasurementresultgroupid, datingtypeid, datingerrorpositive, datingerrornegative, code, comments, ispublished) FROM stdin;
    public       postgres    false    316   ��      �          0    55174    tblvmeasurementtotag 
   TABLE DATA               Z   COPY public.tblvmeasurementtotag (vmeasurementtotagid, tagid, vmeasurementid) FROM stdin;
    public       tellervo    false    317   i�      �          0    55179    tlkpcomplexpresenceabsence 
   TABLE DATA               f   COPY public.tlkpcomplexpresenceabsence (complexpresenceabsenceid, complexpresenceabsence) FROM stdin;
    public       postgres    false    319   ��      �          0    55187    tlkpcoveragetemporal 
   TABLE DATA               T   COPY public.tlkpcoveragetemporal (coveragetemporalid, coveragetemporal) FROM stdin;
    public       postgres    false    321   ��      �          0    55195    tlkpcoveragetemporalfoundation 
   TABLE DATA               r   COPY public.tlkpcoveragetemporalfoundation (coveragetemporalfoundationid, coveragetemporalfoundation) FROM stdin;
    public       postgres    false    323   M�      �          0    55203    tlkpcurationstatus 
   TABLE DATA               N   COPY public.tlkpcurationstatus (curationstatusid, curationstatus) FROM stdin;
    public       tellervo    false    325   ��      �          0    55211    tlkpdatatype 
   TABLE DATA               0   COPY public.tlkpdatatype (datatype) FROM stdin;
    public       tellervo    false    327   V�      �          0    55217    tlkpdatecertainty 
   TABLE DATA               K   COPY public.tlkpdatecertainty (datecertaintyid, datecertainty) FROM stdin;
    public       postgres    false    328   ��      �          0    55225    tlkpdatingtype 
   TABLE DATA               O   COPY public.tlkpdatingtype (datingtypeid, datingtype, datingclass) FROM stdin;
    public       postgres    false    330   ��      �          0    55233 
   tlkpdomain 
   TABLE DATA               >   COPY public.tlkpdomain (domainid, domain, prefix) FROM stdin;
    public       tellervo    false    332   I�      �          0    55241    tlkpelementauthenticity 
   TABLE DATA               ]   COPY public.tlkpelementauthenticity (elementauthenticityid, elementauthenticity) FROM stdin;
    public       postgres    false    334   u�      �          0    55249    tlkpelementshape 
   TABLE DATA               H   COPY public.tlkpelementshape (elementshapeid, elementshape) FROM stdin;
    public       postgres    false    336   ��      �          0    55257    tlkpelementtype 
   TABLE DATA               S   COPY public.tlkpelementtype (elementtypeid, elementtype, vocabularyid) FROM stdin;
    public       postgres    false    338   ��      �          0    55265    tlkpindextype 
   TABLE DATA               ;   COPY public.tlkpindextype (indexid, indexname) FROM stdin;
    public       postgres    false    340   I      �          0    55270    tlkplocationtype 
   TABLE DATA               H   COPY public.tlkplocationtype (locationtypeid, locationtype) FROM stdin;
    public       postgres    false    342   �      �          0    55278    tlkpmeasurementvariable 
   TABLE DATA               ]   COPY public.tlkpmeasurementvariable (measurementvariableid, measurementvariable) FROM stdin;
    public       postgres    false    344   h      �          0    55286    tlkpmeasuringmethod 
   TABLE DATA               Q   COPY public.tlkpmeasuringmethod (measuringmethodid, measuringmethod) FROM stdin;
    public       postgres    false    346   �      �          0    55294    tlkpobjecttype 
   TABLE DATA               P   COPY public.tlkpobjecttype (objecttype, vocabularyid, objecttypeid) FROM stdin;
    public       postgres    false    348          �          0    55302    tlkppresenceabsence 
   TABLE DATA               Q   COPY public.tlkppresenceabsence (presenceabsenceid, presenceabsence) FROM stdin;
    public       postgres    false    350   �6      �          0    55312    tlkpprojectcategory 
   TABLE DATA               _   COPY public.tlkpprojectcategory (projectcategory, vocabularyid, projectcategoryid) FROM stdin;
    public       tellervo    false    353   �6      �          0    55321    tlkpprojecttype 
   TABLE DATA               S   COPY public.tlkpprojecttype (projecttype, vocabularyid, projecttypeid) FROM stdin;
    public       tellervo    false    355   r7      [          0    54210    tlkpreadingnote 
   TABLE DATA               �   COPY public.tlkpreadingnote (readingnoteid, note, vocabularyid, standardisedid, parentreadingid, parentvmrelyearreadingnoteid) FROM stdin;
    public       postgres    false    230   (8      �          0    55332    tlkpsamplestatus 
   TABLE DATA               H   COPY public.tlkpsamplestatus (samplestatusid, samplestatus) FROM stdin;
    public       tellervo    false    358   �9      �          0    55340    tlkpsampletype 
   TABLE DATA               B   COPY public.tlkpsampletype (sampletypeid, sampletype) FROM stdin;
    public       postgres    false    360   f:      �          0    55345    tlkpsecuritypermission 
   TABLE DATA               L   COPY public.tlkpsecuritypermission (securitypermissionid, name) FROM stdin;
    public       postgres    false    362   �:      �          0    55352 	   tlkptaxon 
   TABLE DATA               c   COPY public.tlkptaxon (taxonid, colid, colparentid, taxonrankid, label, parenttaxonid) FROM stdin;
    public       postgres    false    365   3;      �          0    55356    tlkptaxonrank 
   TABLE DATA               J   COPY public.tlkptaxonrank (taxonrankid, taxonrank, rankorder) FROM stdin;
    public       postgres    false    366   �x      p          0    54912    tlkptrackinglocation 
   TABLE DATA               L   COPY public.tlkptrackinglocation (trackinglocationid, location) FROM stdin;
    public       postgres    false    251   �y      �          0    55360    tlkpunit 
   TABLE DATA               0   COPY public.tlkpunit (unitid, unit) FROM stdin;
    public       postgres    false    367   �y      �          0    55368    tlkpuserdefinedfield 
   TABLE DATA               �   COPY public.tlkpuserdefinedfield (userdefinedfieldid, fieldname, description, attachedto, datatype, longfieldname, dictionarykey) FROM stdin;
    public       tellervo    false    369   z      �          0    55377    tlkpuserdefinedterm 
   TABLE DATA               U   COPY public.tlkpuserdefinedterm (userdefinedtermid, term, dictionarykey) FROM stdin;
    public       tellervo    false    370   #z      �          0    55384    tlkpvmeasurementop 
   TABLE DATA               P   COPY public.tlkpvmeasurementop (vmeasurementopid, name, legacycode) FROM stdin;
    public       postgres    false    371   @z      �          0    55389    tlkpvocabulary 
   TABLE DATA               A   COPY public.tlkpvocabulary (vocabularyid, name, url) FROM stdin;
    public       postgres    false    373   �z      �          0    55397    tlkpwmsserver 
   TABLE DATA               ?   COPY public.tlkpwmsserver (wmsserverid, name, url) FROM stdin;
    public       postgres    false    375   b{      �          0    55557    yenikapi 
   TABLE DATA               �   COPY public.yenikapi (gid, sitecode, samplecode, "Dock", "Year", "Top", "Length", "CADMissing", "Notes", "Phase", "Species", the_geom) FROM stdin;
    public       postgres    false    411   {      E          0    53067    jar_repository 
   TABLE DATA               X   COPY sqlj.jar_repository (jarid, jarname, jarorigin, jarowner, jarmanifest) FROM stdin;
    sqlj       postgres    false    202   �{      H          0    53111    classpath_entry 
   TABLE DATA               C   COPY sqlj.classpath_entry (schemaname, ordinal, jarid) FROM stdin;
    sqlj       postgres    false    206   @|      F          0    53080 	   jar_entry 
   TABLE DATA               H   COPY sqlj.jar_entry (entryid, entryname, jarid, entryimage) FROM stdin;
    sqlj       postgres    false    204   g|      G          0    53096    jar_descriptor 
   TABLE DATA               ?   COPY sqlj.jar_descriptor (jarid, ordinal, entryid) FROM stdin;
    sqlj       postgres    false    205   �      I          0    53123    typemap_entry 
   TABLE DATA               ?   COPY sqlj.typemap_entry (mapid, javaname, sqlname) FROM stdin;
    sqlj       postgres    false    208   �      �           0    0    tblconfig_configid_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.tblconfig_configid_seq', 52, true);
            public       postgres    false    246            �           0    0    tblcrossdate_crossdateid_seq    SEQUENCE SET     K   SELECT pg_catalog.setval('public.tblcrossdate_crossdateid_seq', 1, false);
            public       postgres    false    248            �           0    0    tblcuration_curationid_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('public.tblcuration_curationid_seq', 1, true);
            public       tellervo    false    250            �           0    0 *   tblcurationlocation_curationlocationid_seq    SEQUENCE SET     Y   SELECT pg_catalog.setval('public.tblcurationlocation_curationlocationid_seq', 1, false);
            public       postgres    false    252            �           0    0 (   tblcustomvocabterm_customvocabtermid_seq    SEQUENCE SET     W   SELECT pg_catalog.setval('public.tblcustomvocabterm_customvocabtermid_seq', 1, false);
            public       postgres    false    254            �           0    0    tblelement_elementid_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.tblelement_elementid_seq', 1, false);
            public       postgres    false    255            �           0    0    tblelement_gispkey_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.tblelement_gispkey_seq', 1, true);
            public       postgres    false    256            �           0    0 ,   tblenvironmentaldata_environmentaldataid_seq    SEQUENCE SET     [   SELECT pg_catalog.setval('public.tblenvironmentaldata_environmentaldataid_seq', 1, false);
            public       postgres    false    258            �           0    0     tblmeasurement_measurementid_seq    SEQUENCE SET     N   SELECT pg_catalog.setval('public.tblmeasurement_measurementid_seq', 5, true);
            public       postgres    false    261            �           0    0    tblobject_objectid_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.tblobject_objectid_seq', 1, false);
            public       postgres    false    262            �           0    0 "   tblobjectregion_objectregionid_seq    SEQUENCE SET     Q   SELECT pg_catalog.setval('public.tblobjectregion_objectregionid_seq', 1, false);
            public       postgres    false    264            �           0    0 .   tblprojectprojecttype_projectprojecttypeid_seq    SEQUENCE SET     ]   SELECT pg_catalog.setval('public.tblprojectprojecttype_projectprojecttypeid_seq', 1, false);
            public       tellervo    false    269            �           0    0    tblradius_radiusid_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.tblradius_radiusid_seq', 1, false);
            public       postgres    false    270            �           0    0     tblrasterlayer_rasterlayerid_seq    SEQUENCE SET     O   SELECT pg_catalog.setval('public.tblrasterlayer_rasterlayerid_seq', 1, false);
            public       postgres    false    272            �           0    0    tblreading_readingid_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.tblreading_readingid_seq', 85, true);
            public       postgres    false    274            �           0    0 .   tblreadingreadingnote_readingreadingnoteid_seq    SEQUENCE SET     ]   SELECT pg_catalog.setval('public.tblreadingreadingnote_readingreadingnoteid_seq', 1, false);
            public       postgres    false    276            �           0    0    tblredate_redateid_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.tblredate_redateid_seq', 1, false);
            public       postgres    false    278            �           0    0    tblregion_regionid_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.tblregion_regionid_seq', 1, false);
            public       postgres    false    280            �           0    0    tblrequestlog_requestlogid_seq    SEQUENCE SET     N   SELECT pg_catalog.setval('public.tblrequestlog_requestlogid_seq', 145, true);
            public       postgres    false    282            �           0    0    tblsample_sampleid_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.tblsample_sampleid_seq', 1, false);
            public       postgres    false    283            �           0    0 (   tblsecuritydefault_securitydefaultid_seq    SEQUENCE SET     W   SELECT pg_catalog.setval('public.tblsecuritydefault_securitydefaultid_seq', 17, true);
            public       postgres    false    285            �           0    0 $   tblsecuritygroup_securitygroupid_seq    SEQUENCE SET     R   SELECT pg_catalog.setval('public.tblsecuritygroup_securitygroupid_seq', 6, true);
            public       postgres    false    287            �           0    0 8   tblsecuritygroupmembership_securitygroupmembershipid_seq    SEQUENCE SET     g   SELECT pg_catalog.setval('public.tblsecuritygroupmembership_securitygroupmembershipid_seq', 1, false);
            public       postgres    false    289            �           0    0 &   tblsecurityobject_securityobjectid_seq    SEQUENCE SET     U   SELECT pg_catalog.setval('public.tblsecurityobject_securityobjectid_seq', 1, false);
            public       postgres    false    291            �           0    0 %   tblsecuritytree_securityelementid_seq    SEQUENCE SET     T   SELECT pg_catalog.setval('public.tblsecuritytree_securityelementid_seq', 1, false);
            public       postgres    false    292            �           0    0 9   tblsecurityusermembership_tblsecurityusermembershipid_seq    SEQUENCE SET     g   SELECT pg_catalog.setval('public.tblsecurityusermembership_tblsecurityusermembershipid_seq', 1, true);
            public       postgres    false    295            �           0    0 2   tblsecurityvmeasurement_securityvmeasurementid_seq    SEQUENCE SET     a   SELECT pg_catalog.setval('public.tblsecurityvmeasurement_securityvmeasurementid_seq', 1, false);
            public       postgres    false    297            �           0    0 '   tblsupportedclients_supportclientid_seq    SEQUENCE SET     U   SELECT pg_catalog.setval('public.tblsupportedclients_supportclientid_seq', 6, true);
            public       postgres    false    299            �           0    0    tbltruncate_truncateid_seq    SEQUENCE SET     I   SELECT pg_catalog.setval('public.tbltruncate_truncateid_seq', 1, false);
            public       postgres    false    302            �           0    0    tblupgradelog_upgradelogid_seq    SEQUENCE SET     M   SELECT pg_catalog.setval('public.tblupgradelog_upgradelogid_seq', 50, true);
            public       postgres    false    304            �           0    0 "   tblvmeasurement_vmeasurementid_seq    SEQUENCE SET     Q   SELECT pg_catalog.setval('public.tblvmeasurement_vmeasurementid_seq', 1, false);
            public       postgres    false    307            �           0    0 .   tblvmeasurementderivedcache_derivedcacheid_seq    SEQUENCE SET     \   SELECT pg_catalog.setval('public.tblvmeasurementderivedcache_derivedcacheid_seq', 2, true);
            public       postgres    false    308            �           0    0 ,   tblvmeasurementgroup_vmeasurementgroupid_seq    SEQUENCE SET     [   SELECT pg_catalog.setval('public.tblvmeasurementgroup_vmeasurementgroupid_seq', 1, false);
            public       postgres    false    310            �           0    0 4   tblvmeasurementmetacache_vmeasurementmetacacheid_seq    SEQUENCE SET     b   SELECT pg_catalog.setval('public.tblvmeasurementmetacache_vmeasurementmetacacheid_seq', 2, true);
            public       postgres    false    311            �           0    0 <   tblvmeasurementreadingresult_vmeasurementreadingresultid_seq    SEQUENCE SET     l   SELECT pg_catalog.setval('public.tblvmeasurementreadingresult_vmeasurementreadingresultid_seq', 323, true);
            public       postgres    false    313            �           0    0 :   tblvmeasurementrelyearreadingnote_relyearreadingnoteid_seq    SEQUENCE SET     i   SELECT pg_catalog.setval('public.tblvmeasurementrelyearreadingnote_relyearreadingnoteid_seq', 1, false);
            public       postgres    false    314            �           0    0 ,   tblvmeasurementtotag_vmeasurementtotagid_seq    SEQUENCE SET     [   SELECT pg_catalog.setval('public.tblvmeasurementtotag_vmeasurementtotagid_seq', 1, false);
            public       tellervo    false    318            �           0    0 7   tlkpcomplexpresenceabsence_complexpresenceabsenceid_seq    SEQUENCE SET     e   SELECT pg_catalog.setval('public.tlkpcomplexpresenceabsence_complexpresenceabsenceid_seq', 5, true);
            public       postgres    false    320            �           0    0 +   tlkpcoveragetemporal_coveragetemporalid_seq    SEQUENCE SET     Y   SELECT pg_catalog.setval('public.tlkpcoveragetemporal_coveragetemporalid_seq', 7, true);
            public       postgres    false    322            �           0    0 ?   tlkpcoveragetemporalfoundation_coveragetemporalfoundationid_seq    SEQUENCE SET     m   SELECT pg_catalog.setval('public.tlkpcoveragetemporalfoundation_coveragetemporalfoundationid_seq', 6, true);
            public       postgres    false    324            �           0    0 '   tlkpcurationstatus_curationstatusid_seq    SEQUENCE SET     V   SELECT pg_catalog.setval('public.tlkpcurationstatus_curationstatusid_seq', 33, true);
            public       tellervo    false    326            �           0    0 %   tlkpdatecertainty_datecertaintyid_seq    SEQUENCE SET     S   SELECT pg_catalog.setval('public.tlkpdatecertainty_datecertaintyid_seq', 5, true);
            public       postgres    false    329            �           0    0    tlkpdatingtype_datingtypeid_seq    SEQUENCE SET     M   SELECT pg_catalog.setval('public.tlkpdatingtype_datingtypeid_seq', 4, true);
            public       postgres    false    331            �           0    0    tlkpdomain_domainid_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.tlkpdomain_domainid_seq', 1, false);
            public       tellervo    false    333            �           0    0 1   tlkpelementauthenticity_elementauthenticityid_seq    SEQUENCE SET     `   SELECT pg_catalog.setval('public.tlkpelementauthenticity_elementauthenticityid_seq', 1, false);
            public       postgres    false    335            �           0    0 #   tlkpelementshape_elementshapeid_seq    SEQUENCE SET     R   SELECT pg_catalog.setval('public.tlkpelementshape_elementshapeid_seq', 19, true);
            public       postgres    false    337            �           0    0 !   tlkpelementtype_elementtypeid_seq    SEQUENCE SET     Q   SELECT pg_catalog.setval('public.tlkpelementtype_elementtypeid_seq', 994, true);
            public       postgres    false    339            �           0    0    tlkplaboratory_laboratoryid_seq    SEQUENCE SET     N   SELECT pg_catalog.setval('public.tlkplaboratory_laboratoryid_seq', 1, false);
            public       tellervo    false    341            �           0    0 #   tlkplocationtype_locationtypeid_seq    SEQUENCE SET     Q   SELECT pg_catalog.setval('public.tlkplocationtype_locationtypeid_seq', 6, true);
            public       postgres    false    343            �           0    0 1   tlkpmeasurementvariable_measurementvariableid_seq    SEQUENCE SET     _   SELECT pg_catalog.setval('public.tlkpmeasurementvariable_measurementvariableid_seq', 1, true);
            public       postgres    false    345            �           0    0 )   tlkpmeasuringmethod_measuringmethodid_seq    SEQUENCE SET     W   SELECT pg_catalog.setval('public.tlkpmeasuringmethod_measuringmethodid_seq', 3, true);
            public       postgres    false    347            �           0    0    tlkpobjecttype_objecttype_seq    SEQUENCE SET     M   SELECT pg_catalog.setval('public.tlkpobjecttype_objecttype_seq', 995, true);
            public       postgres    false    349            �           0    0 )   tlkppresenceabsence_presenceabsenceid_seq    SEQUENCE SET     X   SELECT pg_catalog.setval('public.tlkppresenceabsence_presenceabsenceid_seq', 1, false);
            public       postgres    false    351            �           0    0 '   tlkpprojectcategory_projectcategory_seq    SEQUENCE SET     V   SELECT pg_catalog.setval('public.tlkpprojectcategory_projectcategory_seq', 33, true);
            public       tellervo    false    352            �           0    0    tlkpprojecttype_projecttype_seq    SEQUENCE SET     N   SELECT pg_catalog.setval('public.tlkpprojecttype_projecttype_seq', 33, true);
            public       tellervo    false    354            �           0    0    tlkprank_rankid_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.tlkprank_rankid_seq', 132, true);
            public       postgres    false    356            �           0    0 !   tlkpreadingnote_readingnoteid_seq    SEQUENCE SET     Q   SELECT pg_catalog.setval('public.tlkpreadingnote_readingnoteid_seq', 199, true);
            public       postgres    false    357            �           0    0 #   tlkpsamplestatus_samplestatusid_seq    SEQUENCE SET     R   SELECT pg_catalog.setval('public.tlkpsamplestatus_samplestatusid_seq', 37, true);
            public       tellervo    false    359            �           0    0    tlkpsampletype_sampletypeid_seq    SEQUENCE SET     M   SELECT pg_catalog.setval('public.tlkpsampletype_sampletypeid_seq', 4, true);
            public       postgres    false    361            �           0    0 /   tlkpsecuritypermission_securitypermissionid_seq    SEQUENCE SET     ^   SELECT pg_catalog.setval('public.tlkpsecuritypermission_securitypermissionid_seq', 1, false);
            public       postgres    false    363            �           0    0    tlkptaxon_taxonid_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.tlkptaxon_taxonid_seq', 1, false);
            public       postgres    false    364            �           0    0    tlkpunits_unitsid_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.tlkpunits_unitsid_seq', 6, true);
            public       postgres    false    368            �           0    0 '   tlkpvmeasurementop_vmeasurementopid_seq    SEQUENCE SET     U   SELECT pg_catalog.setval('public.tlkpvmeasurementop_vmeasurementopid_seq', 7, true);
            public       postgres    false    372            �           0    0    tlkpvocabulary_vocabularyid_seq    SEQUENCE SET     M   SELECT pg_catalog.setval('public.tlkpvocabulary_vocabularyid_seq', 2, true);
            public       postgres    false    374            �           0    0    tlkpwmsserver_wmsserverid_seq    SEQUENCE SET     L   SELECT pg_catalog.setval('public.tlkpwmsserver_wmsserverid_seq', 1, false);
            public       postgres    false    376            �           0    0    yenikapi_gid_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.yenikapi_gid_seq', 1, false);
            public       postgres    false    412                       2606    55632    tlkptaxon colid 
   CONSTRAINT     K   ALTER TABLE ONLY public.tlkptaxon
    ADD CONSTRAINT colid UNIQUE (colid);
 9   ALTER TABLE ONLY public.tlkptaxon DROP CONSTRAINT colid;
       public         postgres    false    365                        2606    55634 3   tblvmeasurementderivedcache only_unique_derivations 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementderivedcache
    ADD CONSTRAINT only_unique_derivations UNIQUE (vmeasurementid, measurementid);
 ]   ALTER TABLE ONLY public.tblvmeasurementderivedcache DROP CONSTRAINT only_unique_derivations;
       public         postgres    false    233    233            �           2606    55636 6   tlkpcomplexpresenceabsence pkey_complexpresenceabsence 
   CONSTRAINT     �   ALTER TABLE ONLY public.tlkpcomplexpresenceabsence
    ADD CONSTRAINT pkey_complexpresenceabsence PRIMARY KEY (complexpresenceabsenceid);
 `   ALTER TABLE ONLY public.tlkpcomplexpresenceabsence DROP CONSTRAINT pkey_complexpresenceabsence;
       public         postgres    false    319            A           2606    55638    tblconfig pkey_config 
   CONSTRAINT     Y   ALTER TABLE ONLY public.tblconfig
    ADD CONSTRAINT pkey_config PRIMARY KEY (configid);
 ?   ALTER TABLE ONLY public.tblconfig DROP CONSTRAINT pkey_config;
       public         postgres    false    245            �           2606    55640 *   tlkpcoveragetemporal pkey_coveragetemporal 
   CONSTRAINT     x   ALTER TABLE ONLY public.tlkpcoveragetemporal
    ADD CONSTRAINT pkey_coveragetemporal PRIMARY KEY (coveragetemporalid);
 T   ALTER TABLE ONLY public.tlkpcoveragetemporal DROP CONSTRAINT pkey_coveragetemporal;
       public         postgres    false    321            �           2606    55642 >   tlkpcoveragetemporalfoundation pkey_coveragetemporalfoundation 
   CONSTRAINT     �   ALTER TABLE ONLY public.tlkpcoveragetemporalfoundation
    ADD CONSTRAINT pkey_coveragetemporalfoundation PRIMARY KEY (coveragetemporalfoundationid);
 h   ALTER TABLE ONLY public.tlkpcoveragetemporalfoundation DROP CONSTRAINT pkey_coveragetemporalfoundation;
       public         postgres    false    323            K           2606    55644 *   tlkptrackinglocation pkey_curationlocation 
   CONSTRAINT     x   ALTER TABLE ONLY public.tlkptrackinglocation
    ADD CONSTRAINT pkey_curationlocation PRIMARY KEY (trackinglocationid);
 T   ALTER TABLE ONLY public.tlkptrackinglocation DROP CONSTRAINT pkey_curationlocation;
       public         postgres    false    251            �           2606    55646 &   tlkpcurationstatus pkey_curationstatus 
   CONSTRAINT     r   ALTER TABLE ONLY public.tlkpcurationstatus
    ADD CONSTRAINT pkey_curationstatus PRIMARY KEY (curationstatusid);
 P   ALTER TABLE ONLY public.tlkpcurationstatus DROP CONSTRAINT pkey_curationstatus;
       public         tellervo    false    325            M           2606    55648 '   tblcustomvocabterm pkey_customvocabterm 
   CONSTRAINT     t   ALTER TABLE ONLY public.tblcustomvocabterm
    ADD CONSTRAINT pkey_customvocabterm PRIMARY KEY (customvocabtermid);
 Q   ALTER TABLE ONLY public.tblcustomvocabterm DROP CONSTRAINT pkey_customvocabterm;
       public         postgres    false    253            �           2606    55650    tlkpdatatype pkey_datatype 
   CONSTRAINT     ^   ALTER TABLE ONLY public.tlkpdatatype
    ADD CONSTRAINT pkey_datatype PRIMARY KEY (datatype);
 D   ALTER TABLE ONLY public.tlkpdatatype DROP CONSTRAINT pkey_datatype;
       public         tellervo    false    327            �           2606    55652 $   tlkpdatecertainty pkey_datecertainty 
   CONSTRAINT     o   ALTER TABLE ONLY public.tlkpdatecertainty
    ADD CONSTRAINT pkey_datecertainty PRIMARY KEY (datecertaintyid);
 N   ALTER TABLE ONLY public.tlkpdatecertainty DROP CONSTRAINT pkey_datecertainty;
       public         postgres    false    328            �           2606    55654    tlkpdatingtype pkey_datingtype 
   CONSTRAINT     f   ALTER TABLE ONLY public.tlkpdatingtype
    ADD CONSTRAINT pkey_datingtype PRIMARY KEY (datingtypeid);
 H   ALTER TABLE ONLY public.tlkpdatingtype DROP CONSTRAINT pkey_datingtype;
       public         postgres    false    330            �           2606    55656 0   tlkpelementauthenticity pkey_elementauthenticity 
   CONSTRAINT     �   ALTER TABLE ONLY public.tlkpelementauthenticity
    ADD CONSTRAINT pkey_elementauthenticity PRIMARY KEY (elementauthenticityid);
 Z   ALTER TABLE ONLY public.tlkpelementauthenticity DROP CONSTRAINT pkey_elementauthenticity;
       public         postgres    false    334            �           2606    55658 "   tlkpelementshape pkey_elementshape 
   CONSTRAINT     l   ALTER TABLE ONLY public.tlkpelementshape
    ADD CONSTRAINT pkey_elementshape PRIMARY KEY (elementshapeid);
 L   ALTER TABLE ONLY public.tlkpelementshape DROP CONSTRAINT pkey_elementshape;
       public         postgres    false    336            �           2606    55660     tlkpelementtype pkey_elementtype 
   CONSTRAINT     i   ALTER TABLE ONLY public.tlkpelementtype
    ADD CONSTRAINT pkey_elementtype PRIMARY KEY (elementtypeid);
 J   ALTER TABLE ONLY public.tlkpelementtype DROP CONSTRAINT pkey_elementtype;
       public         postgres    false    338            Q           2606    55662 +   tblenvironmentaldata pkey_environmentaldata 
   CONSTRAINT     z   ALTER TABLE ONLY public.tblenvironmentaldata
    ADD CONSTRAINT pkey_environmentaldata PRIMARY KEY (environmentaldataid);
 U   ALTER TABLE ONLY public.tblenvironmentaldata DROP CONSTRAINT pkey_environmentaldata;
       public         postgres    false    257            U           2606    55664    tbliptracking pkey_iptracking 
   CONSTRAINT     _   ALTER TABLE ONLY public.tbliptracking
    ADD CONSTRAINT pkey_iptracking PRIMARY KEY (ipaddr);
 G   ALTER TABLE ONLY public.tbliptracking DROP CONSTRAINT pkey_iptracking;
       public         postgres    false    259            W           2606    55666    tbllaboratory pkey_laboratory 
   CONSTRAINT     e   ALTER TABLE ONLY public.tbllaboratory
    ADD CONSTRAINT pkey_laboratory PRIMARY KEY (laboratoryid);
 G   ALTER TABLE ONLY public.tbllaboratory DROP CONSTRAINT pkey_laboratory;
       public         tellervo    false    260            �           2606    55668 "   tlkplocationtype pkey_locationtype 
   CONSTRAINT     l   ALTER TABLE ONLY public.tlkplocationtype
    ADD CONSTRAINT pkey_locationtype PRIMARY KEY (locationtypeid);
 L   ALTER TABLE ONLY public.tlkplocationtype DROP CONSTRAINT pkey_locationtype;
       public         postgres    false    342                       2606    55670    tblmeasurement pkey_measurement 
   CONSTRAINT     h   ALTER TABLE ONLY public.tblmeasurement
    ADD CONSTRAINT pkey_measurement PRIMARY KEY (measurementid);
 I   ALTER TABLE ONLY public.tblmeasurement DROP CONSTRAINT pkey_measurement;
       public         postgres    false    232            �           2606    55672 0   tlkpmeasurementvariable pkey_measurementvariable 
   CONSTRAINT     �   ALTER TABLE ONLY public.tlkpmeasurementvariable
    ADD CONSTRAINT pkey_measurementvariable PRIMARY KEY (measurementvariableid);
 Z   ALTER TABLE ONLY public.tlkpmeasurementvariable DROP CONSTRAINT pkey_measurementvariable;
       public         postgres    false    344            �           2606    55674 (   tlkpmeasuringmethod pkey_measuringmethod 
   CONSTRAINT     u   ALTER TABLE ONLY public.tlkpmeasuringmethod
    ADD CONSTRAINT pkey_measuringmethod PRIMARY KEY (measuringmethodid);
 R   ALTER TABLE ONLY public.tlkpmeasuringmethod DROP CONSTRAINT pkey_measuringmethod;
       public         postgres    false    346                       2606    55676    tblobject pkey_object 
   CONSTRAINT     Y   ALTER TABLE ONLY public.tblobject
    ADD CONSTRAINT pkey_object PRIMARY KEY (objectid);
 ?   ALTER TABLE ONLY public.tblobject DROP CONSTRAINT pkey_object;
       public         postgres    false    229            ]           2606    55678 !   tblobjectregion pkey_objectregion 
   CONSTRAINT     k   ALTER TABLE ONLY public.tblobjectregion
    ADD CONSTRAINT pkey_objectregion PRIMARY KEY (objectregionid);
 K   ALTER TABLE ONLY public.tblobjectregion DROP CONSTRAINT pkey_objectregion;
       public         postgres    false    263            �           2606    55680    tlkpobjecttype pkey_objectype 
   CONSTRAINT     e   ALTER TABLE ONLY public.tlkpobjecttype
    ADD CONSTRAINT pkey_objectype PRIMARY KEY (objecttypeid);
 G   ALTER TABLE ONLY public.tlkpobjecttype DROP CONSTRAINT pkey_objectype;
       public         postgres    false    348            _           2606    55682 #   tblodkdefinition pkey_odkdefinition 
   CONSTRAINT     n   ALTER TABLE ONLY public.tblodkdefinition
    ADD CONSTRAINT pkey_odkdefinition PRIMARY KEY (odkdefinitionid);
 M   ALTER TABLE ONLY public.tblodkdefinition DROP CONSTRAINT pkey_odkdefinition;
       public         tellervo    false    265            a           2606    55684    tblodkinstance pkey_odkinstance 
   CONSTRAINT     h   ALTER TABLE ONLY public.tblodkinstance
    ADD CONSTRAINT pkey_odkinstance PRIMARY KEY (odkinstanceid);
 I   ALTER TABLE ONLY public.tblodkinstance DROP CONSTRAINT pkey_odkinstance;
       public         tellervo    false    266            �           2606    55686 (   tlkppresenceabsence pkey_presenceabsence 
   CONSTRAINT     u   ALTER TABLE ONLY public.tlkppresenceabsence
    ADD CONSTRAINT pkey_presenceabsence PRIMARY KEY (presenceabsenceid);
 R   ALTER TABLE ONLY public.tlkppresenceabsence DROP CONSTRAINT pkey_presenceabsence;
       public         postgres    false    350            c           2606    55688    tblproject pkey_project 
   CONSTRAINT     \   ALTER TABLE ONLY public.tblproject
    ADD CONSTRAINT pkey_project PRIMARY KEY (projectid);
 A   ALTER TABLE ONLY public.tblproject DROP CONSTRAINT pkey_project;
       public         tellervo    false    267            �           2606    55690 (   tlkpprojectcategory pkey_projectcategory 
   CONSTRAINT     u   ALTER TABLE ONLY public.tlkpprojectcategory
    ADD CONSTRAINT pkey_projectcategory PRIMARY KEY (projectcategoryid);
 R   ALTER TABLE ONLY public.tlkpprojectcategory DROP CONSTRAINT pkey_projectcategory;
       public         tellervo    false    353            g           2606    55692 -   tblprojectprojecttype pkey_projectprojecttype 
   CONSTRAINT     }   ALTER TABLE ONLY public.tblprojectprojecttype
    ADD CONSTRAINT pkey_projectprojecttype PRIMARY KEY (projectprojecttypeid);
 W   ALTER TABLE ONLY public.tblprojectprojecttype DROP CONSTRAINT pkey_projectprojecttype;
       public         tellervo    false    268            �           2606    55694     tlkpprojecttype pkey_projecttype 
   CONSTRAINT     i   ALTER TABLE ONLY public.tlkpprojecttype
    ADD CONSTRAINT pkey_projecttype PRIMARY KEY (projecttypeid);
 J   ALTER TABLE ONLY public.tlkpprojecttype DROP CONSTRAINT pkey_projecttype;
       public         tellervo    false    355            0           2606    55696    tblradius pkey_radius 
   CONSTRAINT     Y   ALTER TABLE ONLY public.tblradius
    ADD CONSTRAINT pkey_radius PRIMARY KEY (radiusid);
 ?   ALTER TABLE ONLY public.tblradius DROP CONSTRAINT pkey_radius;
       public         postgres    false    237            r           2606    55698    tblreading pkey_reading 
   CONSTRAINT     \   ALTER TABLE ONLY public.tblreading
    ADD CONSTRAINT pkey_reading PRIMARY KEY (readingid);
 A   ALTER TABLE ONLY public.tblreading DROP CONSTRAINT pkey_reading;
       public         postgres    false    273                       2606    55700     tlkpreadingnote pkey_readingnote 
   CONSTRAINT     i   ALTER TABLE ONLY public.tlkpreadingnote
    ADD CONSTRAINT pkey_readingnote PRIMARY KEY (readingnoteid);
 J   ALTER TABLE ONLY public.tlkpreadingnote DROP CONSTRAINT pkey_readingnote;
       public         postgres    false    230            v           2606    55702 -   tblreadingreadingnote pkey_readingreadingnote 
   CONSTRAINT     }   ALTER TABLE ONLY public.tblreadingreadingnote
    ADD CONSTRAINT pkey_readingreadingnote PRIMARY KEY (readingreadingnoteid);
 W   ALTER TABLE ONLY public.tblreadingreadingnote DROP CONSTRAINT pkey_readingreadingnote;
       public         postgres    false    275            �           2606    55704    tblrequestlog pkey_requestlog 
   CONSTRAINT     e   ALTER TABLE ONLY public.tblrequestlog
    ADD CONSTRAINT pkey_requestlog PRIMARY KEY (requestlogid);
 G   ALTER TABLE ONLY public.tblrequestlog DROP CONSTRAINT pkey_requestlog;
       public         postgres    false    281            6           2606    55706    tblsample pkey_sample 
   CONSTRAINT     Y   ALTER TABLE ONLY public.tblsample
    ADD CONSTRAINT pkey_sample PRIMARY KEY (sampleid);
 ?   ALTER TABLE ONLY public.tblsample DROP CONSTRAINT pkey_sample;
       public         postgres    false    238                       2606    55708 &   tlkpsamplestatus pkey_samplestatustype 
   CONSTRAINT     p   ALTER TABLE ONLY public.tlkpsamplestatus
    ADD CONSTRAINT pkey_samplestatustype PRIMARY KEY (samplestatusid);
 P   ALTER TABLE ONLY public.tlkpsamplestatus DROP CONSTRAINT pkey_samplestatustype;
       public         tellervo    false    358                       2606    55710    tlkpsampletype pkey_sampletype 
   CONSTRAINT     f   ALTER TABLE ONLY public.tlkpsampletype
    ADD CONSTRAINT pkey_sampletype PRIMARY KEY (sampletypeid);
 H   ALTER TABLE ONLY public.tlkpsampletype DROP CONSTRAINT pkey_sampletype;
       public         postgres    false    360            �           2606    55712 '   tblsecuritydefault pkey_securitydefault 
   CONSTRAINT     t   ALTER TABLE ONLY public.tblsecuritydefault
    ADD CONSTRAINT pkey_securitydefault PRIMARY KEY (securitydefaultid);
 Q   ALTER TABLE ONLY public.tblsecuritydefault DROP CONSTRAINT pkey_securitydefault;
       public         postgres    false    284            +           2606    55714 #   tblsecuritygroup pkey_securitygroup 
   CONSTRAINT     n   ALTER TABLE ONLY public.tblsecuritygroup
    ADD CONSTRAINT pkey_securitygroup PRIMARY KEY (securitygroupid);
 M   ALTER TABLE ONLY public.tblsecuritygroup DROP CONSTRAINT pkey_securitygroup;
       public         postgres    false    235            �           2606    55716 7   tblsecuritygroupmembership pkey_securitygroupmembership 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblsecuritygroupmembership
    ADD CONSTRAINT pkey_securitygroupmembership PRIMARY KEY (securitygroupmembershipid);
 a   ALTER TABLE ONLY public.tblsecuritygroupmembership DROP CONSTRAINT pkey_securitygroupmembership;
       public         postgres    false    288            �           2606    55718 %   tblsecurityobject pkey_securityobject 
   CONSTRAINT     q   ALTER TABLE ONLY public.tblsecurityobject
    ADD CONSTRAINT pkey_securityobject PRIMARY KEY (securityobjectid);
 O   ALTER TABLE ONLY public.tblsecurityobject DROP CONSTRAINT pkey_securityobject;
       public         postgres    false    290            	           2606    55720 .   tlkpsecuritypermission pkey_securitypermission 
   CONSTRAINT     ~   ALTER TABLE ONLY public.tlkpsecuritypermission
    ADD CONSTRAINT pkey_securitypermission PRIMARY KEY (securitypermissionid);
 X   ALTER TABLE ONLY public.tlkpsecuritypermission DROP CONSTRAINT pkey_securitypermission;
       public         postgres    false    362            �           2606    55722 $   tblsecurityelement pkey_securitytree 
   CONSTRAINT     q   ALTER TABLE ONLY public.tblsecurityelement
    ADD CONSTRAINT pkey_securitytree PRIMARY KEY (securityelementid);
 N   ALTER TABLE ONLY public.tblsecurityelement DROP CONSTRAINT pkey_securitytree;
       public         postgres    false    286            �           2606    55724 !   tblsecurityuser pkey_securityuser 
   CONSTRAINT     k   ALTER TABLE ONLY public.tblsecurityuser
    ADD CONSTRAINT pkey_securityuser PRIMARY KEY (securityuserid);
 K   ALTER TABLE ONLY public.tblsecurityuser DROP CONSTRAINT pkey_securityuser;
       public         postgres    false    293            �           2606    55726 5   tblsecurityusermembership pkey_securityusermembership 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityusermembership
    ADD CONSTRAINT pkey_securityusermembership PRIMARY KEY (tblsecurityusermembershipid);
 _   ALTER TABLE ONLY public.tblsecurityusermembership DROP CONSTRAINT pkey_securityusermembership;
       public         postgres    false    294            �           2606    55728 1   tblsecurityvmeasurement pkey_securityvmeasurement 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityvmeasurement
    ADD CONSTRAINT pkey_securityvmeasurement PRIMARY KEY (securityvmeasurementid);
 [   ALTER TABLE ONLY public.tblsecurityvmeasurement DROP CONSTRAINT pkey_securityvmeasurement;
       public         postgres    false    296            �           2606    55730 %   tblsupportedclient pkey_supportclient 
   CONSTRAINT     p   ALTER TABLE ONLY public.tblsupportedclient
    ADD CONSTRAINT pkey_supportclient PRIMARY KEY (supportclientid);
 O   ALTER TABLE ONLY public.tblsupportedclient DROP CONSTRAINT pkey_supportclient;
       public         postgres    false    298                       2606    55732    tlkptaxon pkey_taxon 
   CONSTRAINT     W   ALTER TABLE ONLY public.tlkptaxon
    ADD CONSTRAINT pkey_taxon PRIMARY KEY (taxonid);
 >   ALTER TABLE ONLY public.tlkptaxon DROP CONSTRAINT pkey_taxon;
       public         postgres    false    365                       2606    55734    tlkptaxonrank pkey_taxonrank 
   CONSTRAINT     c   ALTER TABLE ONLY public.tlkptaxonrank
    ADD CONSTRAINT pkey_taxonrank PRIMARY KEY (taxonrankid);
 F   ALTER TABLE ONLY public.tlkptaxonrank DROP CONSTRAINT pkey_taxonrank;
       public         postgres    false    366            =           2606    55736    tblbox pkey_tblbox 
   CONSTRAINT     S   ALTER TABLE ONLY public.tblbox
    ADD CONSTRAINT pkey_tblbox PRIMARY KEY (boxid);
 <   ALTER TABLE ONLY public.tblbox DROP CONSTRAINT pkey_tblbox;
       public         postgres    false    244            E           2606    55738    tblcrossdate pkey_tblcrossdate 
   CONSTRAINT     e   ALTER TABLE ONLY public.tblcrossdate
    ADD CONSTRAINT pkey_tblcrossdate PRIMARY KEY (crossdateid);
 H   ALTER TABLE ONLY public.tblcrossdate DROP CONSTRAINT pkey_tblcrossdate;
       public         postgres    false    247            I           2606    55740 !   tblcurationevent pkey_tblcuration 
   CONSTRAINT     l   ALTER TABLE ONLY public.tblcurationevent
    ADD CONSTRAINT pkey_tblcuration PRIMARY KEY (curationeventid);
 K   ALTER TABLE ONLY public.tblcurationevent DROP CONSTRAINT pkey_tblcuration;
       public         tellervo    false    249            -           2606    55742    tblloan pkey_tblloan 
   CONSTRAINT     V   ALTER TABLE ONLY public.tblloan
    ADD CONSTRAINT pkey_tblloan PRIMARY KEY (loanid);
 >   ALTER TABLE ONLY public.tblloan DROP CONSTRAINT pkey_tblloan;
       public         tellervo    false    236            k           2606    55744 "   tblrasterlayer pkey_tblrasterlayer 
   CONSTRAINT     k   ALTER TABLE ONLY public.tblrasterlayer
    ADD CONSTRAINT pkey_tblrasterlayer PRIMARY KEY (rasterlayerid);
 L   ALTER TABLE ONLY public.tblrasterlayer DROP CONSTRAINT pkey_tblrasterlayer;
       public         postgres    false    271            z           2606    55746    tblredate pkey_tblredate 
   CONSTRAINT     \   ALTER TABLE ONLY public.tblredate
    ADD CONSTRAINT pkey_tblredate PRIMARY KEY (redateid);
 B   ALTER TABLE ONLY public.tblredate DROP CONSTRAINT pkey_tblredate;
       public         postgres    false    277            �           2606    55748    tbltag pkey_tbltag 
   CONSTRAINT     S   ALTER TABLE ONLY public.tbltag
    ADD CONSTRAINT pkey_tbltag PRIMARY KEY (tagid);
 <   ALTER TABLE ONLY public.tbltag DROP CONSTRAINT pkey_tbltag;
       public         tellervo    false    300            �           2606    55750    tbltruncate pkey_tbltruncate 
   CONSTRAINT     b   ALTER TABLE ONLY public.tbltruncate
    ADD CONSTRAINT pkey_tbltruncate PRIMARY KEY (truncateid);
 F   ALTER TABLE ONLY public.tbltruncate DROP CONSTRAINT pkey_tbltruncate;
       public         postgres    false    301            �           2606    55752    tlkpdomain pkey_tlkpdomain 
   CONSTRAINT     ^   ALTER TABLE ONLY public.tlkpdomain
    ADD CONSTRAINT pkey_tlkpdomain PRIMARY KEY (domainid);
 D   ALTER TABLE ONLY public.tlkpdomain DROP CONSTRAINT pkey_tlkpdomain;
       public         tellervo    false    332            %           2606    55754    tblelement pkey_tree 
   CONSTRAINT     Y   ALTER TABLE ONLY public.tblelement
    ADD CONSTRAINT pkey_tree PRIMARY KEY (elementid);
 >   ALTER TABLE ONLY public.tblelement DROP CONSTRAINT pkey_tree;
       public         postgres    false    234                       2606    55756    tlkpunit pkey_unit 
   CONSTRAINT     T   ALTER TABLE ONLY public.tlkpunit
    ADD CONSTRAINT pkey_unit PRIMARY KEY (unitid);
 <   ALTER TABLE ONLY public.tlkpunit DROP CONSTRAINT pkey_unit;
       public         postgres    false    367            �           2606    55758    tblupgradelog pkey_upgradelog 
   CONSTRAINT     e   ALTER TABLE ONLY public.tblupgradelog
    ADD CONSTRAINT pkey_upgradelog PRIMARY KEY (upgradelogid);
 G   ALTER TABLE ONLY public.tblupgradelog DROP CONSTRAINT pkey_upgradelog;
       public         postgres    false    303                       2606    55760 *   tlkpuserdefinedfield pkey_userdefinedfield 
   CONSTRAINT     x   ALTER TABLE ONLY public.tlkpuserdefinedfield
    ADD CONSTRAINT pkey_userdefinedfield PRIMARY KEY (userdefinedfieldid);
 T   ALTER TABLE ONLY public.tlkpuserdefinedfield DROP CONSTRAINT pkey_userdefinedfield;
       public         tellervo    false    369            �           2606    55762 3   tbluserdefinedfieldvalue pkey_userdefinedfieldvalue 
   CONSTRAINT     �   ALTER TABLE ONLY public.tbluserdefinedfieldvalue
    ADD CONSTRAINT pkey_userdefinedfieldvalue PRIMARY KEY (userdefinedfieldvalueid);
 ]   ALTER TABLE ONLY public.tbluserdefinedfieldvalue DROP CONSTRAINT pkey_userdefinedfieldvalue;
       public         tellervo    false    305                        2606    55764 (   tlkpuserdefinedterm pkey_userdefinedterm 
   CONSTRAINT     u   ALTER TABLE ONLY public.tlkpuserdefinedterm
    ADD CONSTRAINT pkey_userdefinedterm PRIMARY KEY (userdefinedtermid);
 R   ALTER TABLE ONLY public.tlkpuserdefinedterm DROP CONSTRAINT pkey_userdefinedterm;
       public         tellervo    false    370            �           2606    55766 +   tblvmeasurementgroup pkey_vmeasurementgroup 
   CONSTRAINT     z   ALTER TABLE ONLY public.tblvmeasurementgroup
    ADD CONSTRAINT pkey_vmeasurementgroup PRIMARY KEY (vmeasurementgroupid);
 U   ALTER TABLE ONLY public.tblvmeasurementgroup DROP CONSTRAINT pkey_vmeasurementgroup;
       public         postgres    false    309                       2606    55768 3   tblvmeasurementmetacache pkey_vmeasurementmetacache 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementmetacache
    ADD CONSTRAINT pkey_vmeasurementmetacache PRIMARY KEY (vmeasurementmetacacheid);
 ]   ALTER TABLE ONLY public.tblvmeasurementmetacache DROP CONSTRAINT pkey_vmeasurementmetacache;
       public         postgres    false    231            $           2606    55770 &   tlkpvmeasurementop pkey_vmeasurementop 
   CONSTRAINT     r   ALTER TABLE ONLY public.tlkpvmeasurementop
    ADD CONSTRAINT pkey_vmeasurementop PRIMARY KEY (vmeasurementopid);
 P   ALTER TABLE ONLY public.tlkpvmeasurementop DROP CONSTRAINT pkey_vmeasurementop;
       public         postgres    false    371            ;           2606    55772 ;   tblvmeasurementreadingresult pkey_vmeasurementreadingresult 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementreadingresult
    ADD CONSTRAINT pkey_vmeasurementreadingresult PRIMARY KEY (vmeasurementreadingresultid);
 e   ALTER TABLE ONLY public.tblvmeasurementreadingresult DROP CONSTRAINT pkey_vmeasurementreadingresult;
       public         postgres    false    239            �           2606    55774 -   tblvmeasurementresult pkey_vmeasurementresult 
   CONSTRAINT     }   ALTER TABLE ONLY public.tblvmeasurementresult
    ADD CONSTRAINT pkey_vmeasurementresult PRIMARY KEY (vmeasurementresultid);
 W   ALTER TABLE ONLY public.tblvmeasurementresult DROP CONSTRAINT pkey_vmeasurementresult;
       public         postgres    false    316            �           2606    55776 +   tblvmeasurementtotag pkey_vmeasurementtotag 
   CONSTRAINT     z   ALTER TABLE ONLY public.tblvmeasurementtotag
    ADD CONSTRAINT pkey_vmeasurementtotag PRIMARY KEY (vmeasurementtotagid);
 U   ALTER TABLE ONLY public.tblvmeasurementtotag DROP CONSTRAINT pkey_vmeasurementtotag;
       public         tellervo    false    317            (           2606    55778    tlkpvocabulary pkey_vocabulary 
   CONSTRAINT     f   ALTER TABLE ONLY public.tlkpvocabulary
    ADD CONSTRAINT pkey_vocabulary PRIMARY KEY (vocabularyid);
 H   ALTER TABLE ONLY public.tlkpvocabulary DROP CONSTRAINT pkey_vocabulary;
       public         postgres    false    373            e           2606    55780    tblproject tblproject-uniqtitle 
   CONSTRAINT     ]   ALTER TABLE ONLY public.tblproject
    ADD CONSTRAINT "tblproject-uniqtitle" UNIQUE (title);
 K   ALTER TABLE ONLY public.tblproject DROP CONSTRAINT "tblproject-uniqtitle";
       public         tellervo    false    267                       2606    55782    tblregion tblregion_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.tblregion
    ADD CONSTRAINT tblregion_pkey PRIMARY KEY (regionid);
 B   ALTER TABLE ONLY public.tblregion DROP CONSTRAINT tblregion_pkey;
       public         postgres    false    279            "           2606    55784 <   tblvmeasurementderivedcache tblvmeasurementderivedcache_pkey 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementderivedcache
    ADD CONSTRAINT tblvmeasurementderivedcache_pkey PRIMARY KEY (derivedcacheid);
 f   ALTER TABLE ONLY public.tblvmeasurementderivedcache DROP CONSTRAINT tblvmeasurementderivedcache_pkey;
       public         postgres    false    233            �           2606    55786 H   tblvmeasurementrelyearreadingnote tblvmeasurementrelyearreadingnote_pkey 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementrelyearreadingnote
    ADD CONSTRAINT tblvmeasurementrelyearreadingnote_pkey PRIMARY KEY (relyearreadingnoteid);
 r   ALTER TABLE ONLY public.tblvmeasurementrelyearreadingnote DROP CONSTRAINT tblvmeasurementrelyearreadingnote_pkey;
       public         postgres    false    315            �           2606    55788     tlkpindextype tlkpindextype_pkey 
   CONSTRAINT     c   ALTER TABLE ONLY public.tlkpindextype
    ADD CONSTRAINT tlkpindextype_pkey PRIMARY KEY (indexid);
 J   ALTER TABLE ONLY public.tlkpindextype DROP CONSTRAINT tlkpindextype_pkey;
       public         postgres    false    340            �           2606    55790 +   tlkpobjecttype tlkpobjecttype-nodupsinvocab 
   CONSTRAINT     |   ALTER TABLE ONLY public.tlkpobjecttype
    ADD CONSTRAINT "tlkpobjecttype-nodupsinvocab" UNIQUE (objecttype, vocabularyid);
 W   ALTER TABLE ONLY public.tlkpobjecttype DROP CONSTRAINT "tlkpobjecttype-nodupsinvocab";
       public         postgres    false    348    348            �           2606    55792 5   tlkpprojectcategory tlkpprojectcategory-nodupsinvocab 
   CONSTRAINT     �   ALTER TABLE ONLY public.tlkpprojectcategory
    ADD CONSTRAINT "tlkpprojectcategory-nodupsinvocab" UNIQUE (projectcategory, vocabularyid);
 a   ALTER TABLE ONLY public.tlkpprojectcategory DROP CONSTRAINT "tlkpprojectcategory-nodupsinvocab";
       public         tellervo    false    353    353            �           2606    55794 -   tlkpprojecttype tlkpprojecttype-nodupsinvocab 
   CONSTRAINT        ALTER TABLE ONLY public.tlkpprojecttype
    ADD CONSTRAINT "tlkpprojecttype-nodupsinvocab" UNIQUE (projecttype, vocabularyid);
 Y   ALTER TABLE ONLY public.tlkpprojecttype DROP CONSTRAINT "tlkpprojecttype-nodupsinvocab";
       public         tellervo    false    355    355            �           2606    55796 0   tblsecurityelement uniq-element-group-permission 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityelement
    ADD CONSTRAINT "uniq-element-group-permission" UNIQUE (securitygroupid, securitypermissionid, securityelementid);
 \   ALTER TABLE ONLY public.tblsecurityelement DROP CONSTRAINT "uniq-element-group-permission";
       public         postgres    false    286    286    286            �           2606    55798 .   tblsecurityobject uniq-object-group-permission 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityobject
    ADD CONSTRAINT "uniq-object-group-permission" UNIQUE (objectid, securitygroupid, securitypermissionid);
 Z   ALTER TABLE ONLY public.tblsecurityobject DROP CONSTRAINT "uniq-object-group-permission";
       public         postgres    false    290    290    290            ?           2606    55800    tblbox uniq_boxtitle 
   CONSTRAINT     P   ALTER TABLE ONLY public.tblbox
    ADD CONSTRAINT uniq_boxtitle UNIQUE (title);
 >   ALTER TABLE ONLY public.tblbox DROP CONSTRAINT uniq_boxtitle;
       public         postgres    false    244            C           2606    55802    tblconfig uniq_config-key 
   CONSTRAINT     U   ALTER TABLE ONLY public.tblconfig
    ADD CONSTRAINT "uniq_config-key" UNIQUE (key);
 E   ALTER TABLE ONLY public.tblconfig DROP CONSTRAINT "uniq_config-key";
       public         postgres    false    245            �           2606    55804 $   tlkpdatecertainty uniq_datecertainty 
   CONSTRAINT     h   ALTER TABLE ONLY public.tlkpdatecertainty
    ADD CONSTRAINT uniq_datecertainty UNIQUE (datecertainty);
 N   ALTER TABLE ONLY public.tlkpdatecertainty DROP CONSTRAINT uniq_datecertainty;
       public         postgres    false    328            �           2606    55806 #   tblsecuritydefault uniq_defaultperm 
   CONSTRAINT        ALTER TABLE ONLY public.tblsecuritydefault
    ADD CONSTRAINT uniq_defaultperm UNIQUE (securitygroupid, securitypermissionid);
 M   ALTER TABLE ONLY public.tblsecuritydefault DROP CONSTRAINT uniq_defaultperm;
       public         postgres    false    284    284            S           2606    55808 C   tblenvironmentaldata uniq_environmentaldata_elementid_rasterlayerid 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblenvironmentaldata
    ADD CONSTRAINT uniq_environmentaldata_elementid_rasterlayerid UNIQUE (elementid, rasterlayerid);
 m   ALTER TABLE ONLY public.tblenvironmentaldata DROP CONSTRAINT uniq_environmentaldata_elementid_rasterlayerid;
       public         postgres    false    257    257            Y           2606    55810    tbllaboratory uniq_labname 
   CONSTRAINT     U   ALTER TABLE ONLY public.tbllaboratory
    ADD CONSTRAINT uniq_labname UNIQUE (name);
 D   ALTER TABLE ONLY public.tbllaboratory DROP CONSTRAINT uniq_labname;
       public         tellervo    false    260            i           2606    55812 -   tblprojectprojecttype uniq_projectprojecttype 
   CONSTRAINT     |   ALTER TABLE ONLY public.tblprojectprojecttype
    ADD CONSTRAINT uniq_projectprojecttype UNIQUE (projectid, projecttypeid);
 W   ALTER TABLE ONLY public.tblprojectprojecttype DROP CONSTRAINT uniq_projectprojecttype;
       public         tellervo    false    268    268                       2606    55814    tlkptaxonrank uniq_rankorder 
   CONSTRAINT     \   ALTER TABLE ONLY public.tlkptaxonrank
    ADD CONSTRAINT uniq_rankorder UNIQUE (rankorder);
 F   ALTER TABLE ONLY public.tlkptaxonrank DROP CONSTRAINT uniq_rankorder;
       public         postgres    false    366            m           2606    55816 '   tblrasterlayer uniq_rasterlayerfilename 
   CONSTRAINT     f   ALTER TABLE ONLY public.tblrasterlayer
    ADD CONSTRAINT uniq_rasterlayerfilename UNIQUE (filename);
 Q   ALTER TABLE ONLY public.tblrasterlayer DROP CONSTRAINT uniq_rasterlayerfilename;
       public         postgres    false    271            o           2606    55818 #   tblrasterlayer uniq_rasterlayername 
   CONSTRAINT     ^   ALTER TABLE ONLY public.tblrasterlayer
    ADD CONSTRAINT uniq_rasterlayername UNIQUE (name);
 M   ALTER TABLE ONLY public.tblrasterlayer DROP CONSTRAINT uniq_rasterlayername;
       public         postgres    false    271            x           2606    55820 =   tblreadingreadingnote uniq_readingreadingnote_notesperreading 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblreadingreadingnote
    ADD CONSTRAINT uniq_readingreadingnote_notesperreading UNIQUE (readingid, readingnoteid);
 g   ALTER TABLE ONLY public.tblreadingreadingnote DROP CONSTRAINT uniq_readingreadingnote_notesperreading;
       public         postgres    false    275    275            |           2606    55822 "   tblredate uniq_redate_vmeasurement 
   CONSTRAINT     g   ALTER TABLE ONLY public.tblredate
    ADD CONSTRAINT uniq_redate_vmeasurement UNIQUE (vmeasurementid);
 L   ALTER TABLE ONLY public.tblredate DROP CONSTRAINT uniq_redate_vmeasurement;
       public         postgres    false    277                       2606    55824 $   tlkpsampletype uniq_sampletype_label 
   CONSTRAINT     e   ALTER TABLE ONLY public.tlkpsampletype
    ADD CONSTRAINT uniq_sampletype_label UNIQUE (sampletype);
 N   ALTER TABLE ONLY public.tlkpsampletype DROP CONSTRAINT uniq_sampletype_label;
       public         postgres    false    360            �           2606    55826 Q   tblsecuritygroupmembership uniq_securitygroupmembership_parentchildsecuritygroups 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblsecuritygroupmembership
    ADD CONSTRAINT uniq_securitygroupmembership_parentchildsecuritygroups UNIQUE (parentsecuritygroupid, childsecuritygroupid);
 {   ALTER TABLE ONLY public.tblsecuritygroupmembership DROP CONSTRAINT uniq_securitygroupmembership_parentchildsecuritygroups;
       public         postgres    false    288    288            �           2606    55828 *   tblsecurityuser uniq_securityuser_username 
   CONSTRAINT     i   ALTER TABLE ONLY public.tblsecurityuser
    ADD CONSTRAINT uniq_securityuser_username UNIQUE (username);
 T   ALTER TABLE ONLY public.tblsecurityuser DROP CONSTRAINT uniq_securityuser_username;
       public         postgres    false    293            �           2606    55830 1   tblsecurityvmeasurement uniq_securityvmeasurement 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityvmeasurement
    ADD CONSTRAINT uniq_securityvmeasurement UNIQUE (vmeasurementid, securitygroupid, securitypermissionid);
 [   ALTER TABLE ONLY public.tblsecurityvmeasurement DROP CONSTRAINT uniq_securityvmeasurement;
       public         postgres    false    296    296    296            �           2606    55832 %   tblsupportedclient uniq_supportclient 
   CONSTRAINT     b   ALTER TABLE ONLY public.tblsupportedclient
    ADD CONSTRAINT uniq_supportclient UNIQUE (client);
 O   ALTER TABLE ONLY public.tblsupportedclient DROP CONSTRAINT uniq_supportclient;
       public         postgres    false    298            �           2606    55834 ,   tblvmeasurementtotag uniq_tagforvmeasurement 
   CONSTRAINT     x   ALTER TABLE ONLY public.tblvmeasurementtotag
    ADD CONSTRAINT uniq_tagforvmeasurement UNIQUE (tagid, vmeasurementid);
 V   ALTER TABLE ONLY public.tblvmeasurementtotag DROP CONSTRAINT uniq_tagforvmeasurement;
       public         tellervo    false    317    317                       2606    55836    tlkptaxon uniq_taxon-label 
   CONSTRAINT     g   ALTER TABLE ONLY public.tlkptaxon
    ADD CONSTRAINT "uniq_taxon-label" UNIQUE (label, parenttaxonid);
 F   ALTER TABLE ONLY public.tlkptaxon DROP CONSTRAINT "uniq_taxon-label";
       public         postgres    false    365    365                       2606    55838    tlkptaxonrank uniq_taxonrank 
   CONSTRAINT     \   ALTER TABLE ONLY public.tlkptaxonrank
    ADD CONSTRAINT uniq_taxonrank UNIQUE (taxonrank);
 F   ALTER TABLE ONLY public.tlkptaxonrank DROP CONSTRAINT uniq_taxonrank;
       public         postgres    false    366            �           2606    55840    tlkpdomain uniq_tlkpdomain 
   CONSTRAINT     W   ALTER TABLE ONLY public.tlkpdomain
    ADD CONSTRAINT uniq_tlkpdomain UNIQUE (domain);
 D   ALTER TABLE ONLY public.tlkpdomain DROP CONSTRAINT uniq_tlkpdomain;
       public         tellervo    false    332            �           2606    55842 &   tbltruncate uniq_truncate_vmeasurement 
   CONSTRAINT     k   ALTER TABLE ONLY public.tbltruncate
    ADD CONSTRAINT uniq_truncate_vmeasurement UNIQUE (vmeasurementid);
 P   ALTER TABLE ONLY public.tbltruncate DROP CONSTRAINT uniq_truncate_vmeasurement;
       public         postgres    false    301                       2606    55844    tlkpunit uniq_unit 
   CONSTRAINT     M   ALTER TABLE ONLY public.tlkpunit
    ADD CONSTRAINT uniq_unit UNIQUE (unit);
 <   ALTER TABLE ONLY public.tlkpunit DROP CONSTRAINT uniq_unit;
       public         postgres    false    367            �           2606    55846 7   tbluserdefinedfieldvalue uniq_userdefinedfieldperentity 
   CONSTRAINT     �   ALTER TABLE ONLY public.tbluserdefinedfieldvalue
    ADD CONSTRAINT uniq_userdefinedfieldperentity UNIQUE (userdefinedfieldid, entityid);
 a   ALTER TABLE ONLY public.tbluserdefinedfieldvalue DROP CONSTRAINT uniq_userdefinedfieldperentity;
       public         tellervo    false    305    305                       2606    55848 5   tlkpuserdefinedfield uniq_userdefinedfields_fieldname 
   CONSTRAINT     �   ALTER TABLE ONLY public.tlkpuserdefinedfield
    ADD CONSTRAINT uniq_userdefinedfields_fieldname UNIQUE (fieldname, attachedto);
 _   ALTER TABLE ONLY public.tlkpuserdefinedfield DROP CONSTRAINT uniq_userdefinedfields_fieldname;
       public         tellervo    false    369    369            "           2606    55850 .   tlkpuserdefinedterm uniq_userdefinedtermindict 
   CONSTRAINT     x   ALTER TABLE ONLY public.tlkpuserdefinedterm
    ADD CONSTRAINT uniq_userdefinedtermindict UNIQUE (term, dictionarykey);
 X   ALTER TABLE ONLY public.tlkpuserdefinedterm DROP CONSTRAINT uniq_userdefinedtermindict;
       public         tellervo    false    370    370            G           2606    55852    tblcrossdate uniq_vmeasurement 
   CONSTRAINT     c   ALTER TABLE ONLY public.tblcrossdate
    ADD CONSTRAINT uniq_vmeasurement UNIQUE (vmeasurementid);
 H   ALTER TABLE ONLY public.tblcrossdate DROP CONSTRAINT uniq_vmeasurement;
       public         postgres    false    247            �           2606    55854 3   tblvmeasurementgroup uniq_vmeasurementgroup_members 
   CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementgroup
    ADD CONSTRAINT uniq_vmeasurementgroup_members UNIQUE (membervmeasurementid, vmeasurementid);
 ]   ALTER TABLE ONLY public.tblvmeasurementgroup DROP CONSTRAINT uniq_vmeasurementgroup_members;
       public         postgres    false    309    309            &           2606    55856 +   tlkpvmeasurementop uniq_vmeasurementop_name 
   CONSTRAINT     f   ALTER TABLE ONLY public.tlkpvmeasurementop
    ADD CONSTRAINT uniq_vmeasurementop_name UNIQUE (name);
 U   ALTER TABLE ONLY public.tlkpvmeasurementop DROP CONSTRAINT uniq_vmeasurementop_name;
       public         postgres    false    371                       2606    55858 !   tlkpsamplestatus uniqsamplestatus 
   CONSTRAINT     d   ALTER TABLE ONLY public.tlkpsamplestatus
    ADD CONSTRAINT uniqsamplestatus UNIQUE (samplestatus);
 K   ALTER TABLE ONLY public.tlkpsamplestatus DROP CONSTRAINT uniqsamplestatus;
       public         tellervo    false    358            '           2606    55860    tblelement unique_gispkey 
   CONSTRAINT     W   ALTER TABLE ONLY public.tblelement
    ADD CONSTRAINT unique_gispkey UNIQUE (gispkey);
 C   ALTER TABLE ONLY public.tblelement DROP CONSTRAINT unique_gispkey;
       public         postgres    false    234                       2606    55862 %   tblobject unique_objectcode_perparent 
   CONSTRAINT     p   ALTER TABLE ONLY public.tblobject
    ADD CONSTRAINT unique_objectcode_perparent UNIQUE (code, parentobjectid);
 O   ALTER TABLE ONLY public.tblobject DROP CONSTRAINT unique_objectcode_perparent;
       public         postgres    false    229    229                       2606    55864    tblobject unique_parent-title 
   CONSTRAINT     k   ALTER TABLE ONLY public.tblobject
    ADD CONSTRAINT "unique_parent-title" UNIQUE (parentobjectid, title);
 I   ALTER TABLE ONLY public.tblobject DROP CONSTRAINT "unique_parent-title";
       public         postgres    false    229    229            8           2606    55866 #   tblsample unique_parentelement-code 
   CONSTRAINT     k   ALTER TABLE ONLY public.tblsample
    ADD CONSTRAINT "unique_parentelement-code" UNIQUE (elementid, code);
 O   ALTER TABLE ONLY public.tblsample DROP CONSTRAINT "unique_parentelement-code";
       public         postgres    false    238    238            )           2606    55868 #   tblelement unique_parentobject-code 
   CONSTRAINT     j   ALTER TABLE ONLY public.tblelement
    ADD CONSTRAINT "unique_parentobject-code" UNIQUE (objectid, code);
 O   ALTER TABLE ONLY public.tblelement DROP CONSTRAINT "unique_parentobject-code";
       public         postgres    false    234    234            2           2606    55870 "   tblradius unique_parentsample-code 
   CONSTRAINT     i   ALTER TABLE ONLY public.tblradius
    ADD CONSTRAINT "unique_parentsample-code" UNIQUE (sampleid, code);
 N   ALTER TABLE ONLY public.tblradius DROP CONSTRAINT "unique_parentsample-code";
       public         postgres    false    237    237            �           2606    55872    tblvmeasurement vmeasurementid 
   CONSTRAINT     h   ALTER TABLE ONLY public.tblvmeasurement
    ADD CONSTRAINT vmeasurementid PRIMARY KEY (vmeasurementid);
 H   ALTER TABLE ONLY public.tblvmeasurement DROP CONSTRAINT vmeasurementid;
       public         postgres    false    306            *           2606    55874    tlkpwmsserver wmsserver-pkey 
   CONSTRAINT     e   ALTER TABLE ONLY public.tlkpwmsserver
    ADD CONSTRAINT "wmsserver-pkey" PRIMARY KEY (wmsserverid);
 H   ALTER TABLE ONLY public.tlkpwmsserver DROP CONSTRAINT "wmsserver-pkey";
       public         postgres    false    375            ,           2606    55876     tlkpwmsserver wmsserver-uniqname 
   CONSTRAINT     ]   ALTER TABLE ONLY public.tlkpwmsserver
    ADD CONSTRAINT "wmsserver-uniqname" UNIQUE (name);
 L   ALTER TABLE ONLY public.tlkpwmsserver DROP CONSTRAINT "wmsserver-uniqname";
       public         postgres    false    375            .           2606    55878    tlkpwmsserver wmsserver-uniqurl 
   CONSTRAINT     [   ALTER TABLE ONLY public.tlkpwmsserver
    ADD CONSTRAINT "wmsserver-uniqurl" UNIQUE (url);
 K   ALTER TABLE ONLY public.tlkpwmsserver DROP CONSTRAINT "wmsserver-uniqurl";
       public         postgres    false    375            0           2606    55880    yenikapi yenikapi_pkey 
   CONSTRAINT     U   ALTER TABLE ONLY public.yenikapi
    ADD CONSTRAINT yenikapi_pkey PRIMARY KEY (gid);
 @   ALTER TABLE ONLY public.yenikapi DROP CONSTRAINT yenikapi_pkey;
       public         postgres    false    411            #           1259    55881    fki_fkey_element_units    INDEX     N   CREATE INDEX fki_fkey_element_units ON public.tblelement USING btree (units);
 *   DROP INDEX public.fki_fkey_element_units;
       public         postgres    false    234                       1259    55882     idx_standardisiedid-vocabularyid    INDEX     }   CREATE UNIQUE INDEX "idx_standardisiedid-vocabularyid" ON public.tlkpreadingnote USING btree (standardisedid, vocabularyid);
 6   DROP INDEX public."idx_standardisiedid-vocabularyid";
       public         postgres    false    230    230            N           1259    55883    ind_environmentaldata-elementid    INDEX     g   CREATE INDEX "ind_environmentaldata-elementid" ON public.tblenvironmentaldata USING btree (elementid);
 5   DROP INDEX public."ind_environmentaldata-elementid";
       public         postgres    false    257            O           1259    55884 #   ind_environmentaldata-rasterlayerid    INDEX     o   CREATE INDEX "ind_environmentaldata-rasterlayerid" ON public.tblenvironmentaldata USING btree (rasterlayerid);
 9   DROP INDEX public."ind_environmentaldata-rasterlayerid";
       public         postgres    false    257                       1259    55885    ind_measurement-datingtype    INDEX     _   CREATE INDEX "ind_measurement-datingtype" ON public.tblmeasurement USING btree (datingtypeid);
 0   DROP INDEX public."ind_measurement-datingtype";
       public         postgres    false    232                       1259    55886    ind_measurement-radius    INDEX     W   CREATE INDEX "ind_measurement-radius" ON public.tblmeasurement USING btree (radiusid);
 ,   DROP INDEX public."ind_measurement-radius";
       public         postgres    false    232            Z           1259    55887    ind_objectregion-object    INDEX     Y   CREATE INDEX "ind_objectregion-object" ON public.tblobjectregion USING btree (objectid);
 -   DROP INDEX public."ind_objectregion-object";
       public         postgres    false    263            [           1259    55888    ind_objectregion-region    INDEX     Y   CREATE INDEX "ind_objectregion-region" ON public.tblobjectregion USING btree (regionid);
 -   DROP INDEX public."ind_objectregion-region";
       public         postgres    false    263            .           1259    55889    ind_radius-sample    INDEX     M   CREATE INDEX "ind_radius-sample" ON public.tblradius USING btree (sampleid);
 '   DROP INDEX public."ind_radius-sample";
       public         postgres    false    237            p           1259    55890    ind_reading-measurement    INDEX     Y   CREATE INDEX "ind_reading-measurement" ON public.tblreading USING btree (measurementid);
 -   DROP INDEX public."ind_reading-measurement";
       public         postgres    false    273            s           1259    55891    ind_readingreadingnote-reading    INDEX     g   CREATE INDEX "ind_readingreadingnote-reading" ON public.tblreadingreadingnote USING btree (readingid);
 4   DROP INDEX public."ind_readingreadingnote-reading";
       public         postgres    false    275            t           1259    55892 "   ind_readingreadingnote-readingnote    INDEX     o   CREATE INDEX "ind_readingreadingnote-readingnote" ON public.tblreadingreadingnote USING btree (readingnoteid);
 8   DROP INDEX public."ind_readingreadingnote-readingnote";
       public         postgres    false    275            �           1259    55893    ind_result-relyear-noteid    INDEX     �   CREATE INDEX "ind_result-relyear-noteid" ON public.tblvmeasurementreadingnoteresult USING btree (vmeasurementresultid, relyear, readingnoteid);
 /   DROP INDEX public."ind_result-relyear-noteid";
       public         postgres    false    312    312    312            3           1259    55894    ind_sample-sampletype    INDEX     M   CREATE INDEX "ind_sample-sampletype" ON public.tblsample USING btree (type);
 +   DROP INDEX public."ind_sample-sampletype";
       public         postgres    false    238            4           1259    55895    ind_sample-tree    INDEX     L   CREATE INDEX "ind_sample-tree" ON public.tblsample USING btree (elementid);
 %   DROP INDEX public."ind_sample-tree";
       public         postgres    false    238            �           1259    55896 !   ind_securitydefault-securitygroup    INDEX     m   CREATE INDEX "ind_securitydefault-securitygroup" ON public.tblsecuritydefault USING btree (securitygroupid);
 7   DROP INDEX public."ind_securitydefault-securitygroup";
       public         postgres    false    284            �           1259    55897 &   ind_securitydefault-securitypermission    INDEX     w   CREATE INDEX "ind_securitydefault-securitypermission" ON public.tblsecuritydefault USING btree (securitypermissionid);
 <   DROP INDEX public."ind_securitydefault-securitypermission";
       public         postgres    false    284            �           1259    55898 /   ind_securitygroupmembereship-childsecuritygroup    INDEX     �   CREATE INDEX "ind_securitygroupmembereship-childsecuritygroup" ON public.tblsecuritygroupmembership USING btree (childsecuritygroupid);
 E   DROP INDEX public."ind_securitygroupmembereship-childsecuritygroup";
       public         postgres    false    288            �           1259    55899 /   ind_securitygroupmembership-parentsecuritygroup    INDEX     �   CREATE INDEX "ind_securitygroupmembership-parentsecuritygroup" ON public.tblsecuritygroupmembership USING btree (parentsecuritygroupid);
 E   DROP INDEX public."ind_securitygroupmembership-parentsecuritygroup";
       public         postgres    false    288            �           1259    55900     ind_securityobject-securitygroup    INDEX     k   CREATE INDEX "ind_securityobject-securitygroup" ON public.tblsecurityobject USING btree (securitygroupid);
 6   DROP INDEX public."ind_securityobject-securitygroup";
       public         postgres    false    290            �           1259    55901 %   ind_securityobject-securitypermission    INDEX     u   CREATE INDEX "ind_securityobject-securitypermission" ON public.tblsecurityobject USING btree (securitypermissionid);
 ;   DROP INDEX public."ind_securityobject-securitypermission";
       public         postgres    false    290            �           1259    55902    ind_securitytree-securitygroup    INDEX     j   CREATE INDEX "ind_securitytree-securitygroup" ON public.tblsecurityelement USING btree (securitygroupid);
 4   DROP INDEX public."ind_securitytree-securitygroup";
       public         postgres    false    286            �           1259    55903 #   ind_securitytree-securitypermission    INDEX     t   CREATE INDEX "ind_securitytree-securitypermission" ON public.tblsecurityelement USING btree (securitypermissionid);
 9   DROP INDEX public."ind_securitytree-securitypermission";
       public         postgres    false    286            �           1259    55904    ind_securitytree-tree    INDEX     [   CREATE INDEX "ind_securitytree-tree" ON public.tblsecurityelement USING btree (elementid);
 +   DROP INDEX public."ind_securitytree-tree";
       public         postgres    false    286            �           1259    55905 (   ind_securityusermembership-securitygroup    INDEX     {   CREATE INDEX "ind_securityusermembership-securitygroup" ON public.tblsecurityusermembership USING btree (securitygroupid);
 >   DROP INDEX public."ind_securityusermembership-securitygroup";
       public         postgres    false    294            �           1259    55906 &   ind_securityvmeasurement-securitygroup    INDEX     w   CREATE INDEX "ind_securityvmeasurement-securitygroup" ON public.tblsecurityvmeasurement USING btree (securitygroupid);
 <   DROP INDEX public."ind_securityvmeasurement-securitygroup";
       public         postgres    false    296            �           1259    55907 +   ind_securityvmeasurement-securitypermission    INDEX     �   CREATE INDEX "ind_securityvmeasurement-securitypermission" ON public.tblsecurityvmeasurement USING btree (securitypermissionid);
 A   DROP INDEX public."ind_securityvmeasurement-securitypermission";
       public         postgres    false    296            �           1259    55908 %   ind_securityvmeasurement-vmeasurement    INDEX     u   CREATE INDEX "ind_securityvmeasurement-vmeasurement" ON public.tblsecurityvmeasurement USING btree (vmeasurementid);
 ;   DROP INDEX public."ind_securityvmeasurement-vmeasurement";
       public         postgres    false    296                       1259    55909    ind_taxon-taxonrank    INDEX     R   CREATE INDEX "ind_taxon-taxonrank" ON public.tlkptaxon USING btree (taxonrankid);
 )   DROP INDEX public."ind_taxon-taxonrank";
       public         postgres    false    365            �           1259    55910    ind_vmeasurement-measurement    INDEX     c   CREATE INDEX "ind_vmeasurement-measurement" ON public.tblvmeasurement USING btree (measurementid);
 2   DROP INDEX public."ind_vmeasurement-measurement";
       public         postgres    false    306            �           1259    55911    ind_vmeasurement-vmeasurementop    INDEX     i   CREATE INDEX "ind_vmeasurement-vmeasurementop" ON public.tblvmeasurement USING btree (vmeasurementopid);
 5   DROP INDEX public."ind_vmeasurement-vmeasurementop";
       public         postgres    false    306                       1259    55912 (   ind_vmeasurementderivedcache-measurement    INDEX     {   CREATE INDEX "ind_vmeasurementderivedcache-measurement" ON public.tblvmeasurementderivedcache USING btree (measurementid);
 >   DROP INDEX public."ind_vmeasurementderivedcache-measurement";
       public         postgres    false    233                       1259    55913 )   ind_vmeasurementderivedcache-vmeasurement    INDEX     }   CREATE INDEX "ind_vmeasurementderivedcache-vmeasurement" ON public.tblvmeasurementderivedcache USING btree (vmeasurementid);
 ?   DROP INDEX public."ind_vmeasurementderivedcache-vmeasurement";
       public         postgres    false    233            �           1259    55914 #   ind_vmeasurementgroup-vmeasurement1    INDEX     p   CREATE INDEX "ind_vmeasurementgroup-vmeasurement1" ON public.tblvmeasurementgroup USING btree (vmeasurementid);
 9   DROP INDEX public."ind_vmeasurementgroup-vmeasurement1";
       public         postgres    false    309            �           1259    55915 #   ind_vmeasurementgroup-vmeasurement2    INDEX     v   CREATE INDEX "ind_vmeasurementgroup-vmeasurement2" ON public.tblvmeasurementgroup USING btree (membervmeasurementid);
 9   DROP INDEX public."ind_vmeasurementgroup-vmeasurement2";
       public         postgres    false    309                       1259    55916 &   ind_vmeasurementmetacache-vmeasurement    INDEX     w   CREATE INDEX "ind_vmeasurementmetacache-vmeasurement" ON public.tblvmeasurementmetacache USING btree (vmeasurementid);
 <   DROP INDEX public."ind_vmeasurementmetacache-vmeasurement";
       public         postgres    false    231            9           1259    55917 8   ind_vmeasurementreadingresult-vmeasurementresult_relyear    INDEX     �   CREATE UNIQUE INDEX "ind_vmeasurementreadingresult-vmeasurementresult_relyear" ON public.tblvmeasurementreadingresult USING btree (vmeasurementresultid, relyear);
 N   DROP INDEX public."ind_vmeasurementreadingresult-vmeasurementresult_relyear";
       public         postgres    false    239    239            �           1259    55918 '   ind_vmrelyearreadingnote-vmeasurementid    INDEX     �   CREATE INDEX "ind_vmrelyearreadingnote-vmeasurementid" ON public.tblvmeasurementrelyearreadingnote USING btree (vmeasurementid);
 =   DROP INDEX public."ind_vmrelyearreadingnote-vmeasurementid";
       public         postgres    false    315            �           1259    55919    index_globaltags    INDEX     a   CREATE UNIQUE INDEX index_globaltags ON public.tbltag USING btree (tag) WHERE (ownerid IS NULL);
 $   DROP INDEX public.index_globaltags;
       public         tellervo    false    300    300            �           1259    55920    index_privatetags    INDEX     o   CREATE UNIQUE INDEX index_privatetags ON public.tbltag USING btree (tag, ownerid) WHERE (ownerid IS NOT NULL);
 %   DROP INDEX public.index_privatetags;
       public         tellervo    false    300    300    300                       1259    55921    parent_object_index    INDEX     S   CREATE INDEX parent_object_index ON public.tblobject USING btree (parentobjectid);
 '   DROP INDEX public.parent_object_index;
       public         postgres    false    229            �           1259    55922    pkey_indexname    INDEX     T   CREATE UNIQUE INDEX pkey_indexname ON public.tlkpindextype USING btree (indexname);
 "   DROP INDEX public.pkey_indexname;
       public         postgres    false    340                       1259    55923    postgis_object_extent    INDEX     �   CREATE INDEX postgis_object_extent ON public.tblobject USING gist (locationgeometry);

ALTER TABLE public.tblobject CLUSTER ON postgis_object_extent;
 )   DROP INDEX public.postgis_object_extent;
       public         postgres    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    229            }           1259    55924    postgis_region_the_geom    INDEX     �   CREATE INDEX postgis_region_the_geom ON public.tblregion USING gist (the_geom);

ALTER TABLE public.tblregion CLUSTER ON postgis_region_the_geom;
 +   DROP INDEX public.postgis_region_the_geom;
       public         postgres    false    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    2    279            Y           2618    55925    tblsecuritygroup protectadmin    RULE     |   CREATE RULE protectadmin AS
    ON UPDATE TO public.tblsecuritygroup
   WHERE (old.securitygroupid = 1) DO INSTEAD NOTHING;
 3   DROP RULE protectadmin ON public.tblsecuritygroup;
       public       postgres    false    235    235    235    235            �           2620    55926 &   tblsecuritygroup checkGroupIsDeletable    TRIGGER     �   CREATE TRIGGER "checkGroupIsDeletable" BEFORE DELETE ON public.tblsecuritygroup FOR EACH ROW EXECUTE PROCEDURE public."checkGroupIsDeletable"();
 A   DROP TRIGGER "checkGroupIsDeletable" ON public.tblsecuritygroup;
       public       postgres    false    235    1195            �           2620    55927 ,   tblmeasurement check_measurementdatingerrors    TRIGGER     �   CREATE TRIGGER check_measurementdatingerrors BEFORE INSERT OR UPDATE ON public.tblmeasurement FOR EACH ROW EXECUTE PROCEDURE public.check_datingerrors();
 E   DROP TRIGGER check_measurementdatingerrors ON public.tblmeasurement;
       public       postgres    false    1581    232            �           2620    55928 5   tblvmeasurementrelyearreadingnote check_vm_is_derived    TRIGGER     �   CREATE TRIGGER check_vm_is_derived BEFORE INSERT OR UPDATE ON public.tblvmeasurementrelyearreadingnote FOR EACH ROW EXECUTE PROCEDURE cpgdb.vmeasurementrelyearnotetrigger();
 N   DROP TRIGGER check_vm_is_derived ON public.tblvmeasurementrelyearreadingnote;
       public       postgres    false    315    1148            �           2620    55929 &   tlkpreadingnote default_standardisedid    TRIGGER     �   CREATE TRIGGER default_standardisedid BEFORE INSERT OR UPDATE ON public.tlkpreadingnote FOR EACH ROW EXECUTE PROCEDURE public.create_defaultreadingnotestandardisedid();
 ?   DROP TRIGGER default_standardisedid ON public.tlkpreadingnote;
       public       postgres    false    1583    230            �           2620    55930 +   tblsecurityuser enforce_atleastoneadminuser    TRIGGER     �   CREATE TRIGGER enforce_atleastoneadminuser AFTER UPDATE ON public.tblsecurityuser FOR EACH ROW EXECUTE PROCEDURE public.enforce_atleastoneadminuserupdate();
 D   DROP TRIGGER enforce_atleastoneadminuser ON public.tblsecurityuser;
       public       postgres    false    1233    293            �           2620    55931 5   tblsecurityusermembership enforce_atleastoneadminuser    TRIGGER     �   CREATE TRIGGER enforce_atleastoneadminuser AFTER UPDATE ON public.tblsecurityusermembership FOR EACH ROW EXECUTE PROCEDURE public.enforce_atleastoneadminuserupdate();
 N   DROP TRIGGER enforce_atleastoneadminuser ON public.tblsecurityusermembership;
       public       postgres    false    294    1233            �           2620    55932 .   tblsecurityuser enforce_atleastoneadminuserdel    TRIGGER     �   CREATE TRIGGER enforce_atleastoneadminuserdel BEFORE DELETE ON public.tblsecurityuser FOR EACH ROW EXECUTE PROCEDURE public.enforce_atleastoneadminuserdelete();
 G   DROP TRIGGER enforce_atleastoneadminuserdel ON public.tblsecurityuser;
       public       postgres    false    1232    293            �           2620    55933 8   tblsecurityusermembership enforce_atleastoneadminuserdel    TRIGGER     �   CREATE TRIGGER enforce_atleastoneadminuserdel BEFORE DELETE ON public.tblsecurityusermembership FOR EACH ROW EXECUTE PROCEDURE public.enforce_atleastoneadminuserdelete();
 Q   DROP TRIGGER enforce_atleastoneadminuserdel ON public.tblsecurityusermembership;
       public       postgres    false    294    1232            �           2620    55934 +   tblsecuritydefault enforce_noadminpermedits    TRIGGER     �   CREATE TRIGGER enforce_noadminpermedits AFTER INSERT OR UPDATE ON public.tblsecuritydefault FOR EACH ROW EXECUTE PROCEDURE public.enforce_noadminpermeditsonupdatecreate();
 D   DROP TRIGGER enforce_noadminpermedits ON public.tblsecuritydefault;
       public       postgres    false    284    1237            �           2620    55935 +   tblsecurityelement enforce_noadminpermedits    TRIGGER     �   CREATE TRIGGER enforce_noadminpermedits AFTER INSERT OR UPDATE ON public.tblsecurityelement FOR EACH ROW EXECUTE PROCEDURE public.enforce_noadminpermeditsonupdatecreate();
 D   DROP TRIGGER enforce_noadminpermedits ON public.tblsecurityelement;
       public       postgres    false    1237    286            �           2620    55936 *   tblsecurityobject enforce_noadminpermedits    TRIGGER     �   CREATE TRIGGER enforce_noadminpermedits AFTER INSERT OR UPDATE ON public.tblsecurityobject FOR EACH ROW EXECUTE PROCEDURE public.enforce_noadminpermeditsonupdatecreate();
 C   DROP TRIGGER enforce_noadminpermedits ON public.tblsecurityobject;
       public       postgres    false    290    1237            �           2620    55937 0   tblsecurityvmeasurement enforce_noadminpermedits    TRIGGER     �   CREATE TRIGGER enforce_noadminpermedits AFTER INSERT OR UPDATE ON public.tblsecurityvmeasurement FOR EACH ROW EXECUTE PROCEDURE public.enforce_noadminpermeditsonupdatecreate();
 I   DROP TRIGGER enforce_noadminpermedits ON public.tblsecurityvmeasurement;
       public       postgres    false    296    1237            �           2620    55938 .   tblsecuritydefault enforce_noadminpermeditsdel    TRIGGER     �   CREATE TRIGGER enforce_noadminpermeditsdel BEFORE DELETE ON public.tblsecuritydefault FOR EACH ROW EXECUTE PROCEDURE public.enforce_noadminpermeditsondelete();
 G   DROP TRIGGER enforce_noadminpermeditsdel ON public.tblsecuritydefault;
       public       postgres    false    1236    284            �           2620    55939 .   tblsecurityelement enforce_noadminpermeditsdel    TRIGGER     �   CREATE TRIGGER enforce_noadminpermeditsdel BEFORE DELETE ON public.tblsecurityelement FOR EACH ROW EXECUTE PROCEDURE public.enforce_noadminpermeditsondelete();
 G   DROP TRIGGER enforce_noadminpermeditsdel ON public.tblsecurityelement;
       public       postgres    false    286    1236            �           2620    55940 -   tblsecurityobject enforce_noadminpermeditsdel    TRIGGER     �   CREATE TRIGGER enforce_noadminpermeditsdel BEFORE DELETE ON public.tblsecurityobject FOR EACH ROW EXECUTE PROCEDURE public.enforce_noadminpermeditsondelete();
 F   DROP TRIGGER enforce_noadminpermeditsdel ON public.tblsecurityobject;
       public       postgres    false    290    1236            �           2620    55941 3   tblsecurityvmeasurement enforce_noadminpermeditsdel    TRIGGER     �   CREATE TRIGGER enforce_noadminpermeditsdel BEFORE DELETE ON public.tblsecurityvmeasurement FOR EACH ROW EXECUTE PROCEDURE public.enforce_noadminpermeditsondelete();
 L   DROP TRIGGER enforce_noadminpermeditsdel ON public.tblsecurityvmeasurement;
       public       postgres    false    1236    296            �           2620    55942    tblobject enforce_object-parent    TRIGGER     �   CREATE TRIGGER "enforce_object-parent" BEFORE INSERT OR UPDATE ON public.tblobject FOR EACH ROW EXECUTE PROCEDURE public."enforce_object-parent"();
 :   DROP TRIGGER "enforce_object-parent" ON public.tblobject;
       public       postgres    false    1238    229            �           2620    55943 (   tblcurationevent trig_loanid_when_loaned    TRIGGER     �   CREATE TRIGGER trig_loanid_when_loaned BEFORE INSERT OR UPDATE ON public.tblcurationevent FOR EACH ROW EXECUTE PROCEDURE public.check_tblcuration_loanid_is_not_null_when_loaned();
 A   DROP TRIGGER trig_loanid_when_loaned ON public.tblcurationevent;
       public       tellervo    false    1196    249            �           2620    55944 &   tblcurationevent trig_no_status_update    TRIGGER     �   CREATE TRIGGER trig_no_status_update BEFORE UPDATE ON public.tblcurationevent FOR EACH ROW EXECUTE PROCEDURE public.enforce_no_status_update_on_curation();
 ?   DROP TRIGGER trig_no_status_update ON public.tblcurationevent;
       public       tellervo    false    249    1235            �           2620    55945 "   tblcurationevent trig_nodoubleloan    TRIGGER     �   CREATE TRIGGER trig_nodoubleloan BEFORE INSERT ON public.tblcurationevent FOR EACH ROW EXECUTE PROCEDURE public.enforce_no_loan_when_on_loan();
 ;   DROP TRIGGER trig_nodoubleloan ON public.tblcurationevent;
       public       tellervo    false    249    1234            �           2620    55946 7   tblodkdefinition trigger_updateodkformversion-increment    TRIGGER     �   CREATE TRIGGER "trigger_updateodkformversion-increment" BEFORE UPDATE ON public.tblodkdefinition FOR EACH ROW EXECUTE PROCEDURE public."update_odkformdef-versionincrement"();
 R   DROP TRIGGER "trigger_updateodkformversion-increment" ON public.tblodkdefinition;
       public       tellervo    false    265    1523            �           2620    55947 *   tblelement update_element_rebuildmetacache    TRIGGER     �   CREATE TRIGGER update_element_rebuildmetacache AFTER INSERT OR UPDATE ON public.tblelement FOR EACH ROW EXECUTE PROCEDURE cpgdb.rebuildmetacacheforelement();
 C   DROP TRIGGER update_element_rebuildmetacache ON public.tblelement;
       public       postgres    false    234    1128            �           2620    55948 "   tblrasterlayer update_layerenvdata    TRIGGER     �   CREATE TRIGGER update_layerenvdata AFTER INSERT OR UPDATE ON public.tblrasterlayer FOR EACH ROW EXECUTE PROCEDURE public.update_layerenvdata();
 ;   DROP TRIGGER update_layerenvdata ON public.tblrasterlayer;
       public       postgres    false    1521    271            �           2620    55949 ?   tblmeasurement update_measurement_lastmodifiedtimestamp_trigger    TRIGGER     �   CREATE TRIGGER update_measurement_lastmodifiedtimestamp_trigger BEFORE UPDATE ON public.tblmeasurement FOR EACH ROW EXECUTE PROCEDURE public.update_lastmodifiedtimestamp();
 X   DROP TRIGGER update_measurement_lastmodifiedtimestamp_trigger ON public.tblmeasurement;
       public       postgres    false    1535    232            �           2620    55950 5   tblobject update_object_lastmodifiedtimestamp_trigger    TRIGGER     �   CREATE TRIGGER update_object_lastmodifiedtimestamp_trigger BEFORE UPDATE ON public.tblobject FOR EACH ROW EXECUTE PROCEDURE public.update_lastmodifiedtimestamp();
 N   DROP TRIGGER update_object_lastmodifiedtimestamp_trigger ON public.tblobject;
       public       postgres    false    229    1535            �           2620    55951 (   tblobject update_object_rebuildmetacache    TRIGGER     �   CREATE TRIGGER update_object_rebuildmetacache AFTER INSERT OR UPDATE ON public.tblobject FOR EACH ROW EXECUTE PROCEDURE cpgdb.rebuildmetacacheforobject();
 A   DROP TRIGGER update_object_rebuildmetacache ON public.tblobject;
       public       postgres    false    229    1129            �           2620    55952 %   tblregion update_objectregion_trigger    TRIGGER     �   CREATE TRIGGER update_objectregion_trigger AFTER INSERT OR UPDATE ON public.tblregion FOR EACH ROW EXECUTE PROCEDURE cpgdb.objectregionmodified();
 >   DROP TRIGGER update_objectregion_trigger ON public.tblregion;
       public       postgres    false    279    1123            �           2620    55953 5   tblradius update_radius_lastmodifiedtimestamp_trigger    TRIGGER     �   CREATE TRIGGER update_radius_lastmodifiedtimestamp_trigger BEFORE UPDATE ON public.tblradius FOR EACH ROW EXECUTE PROCEDURE public.update_lastmodifiedtimestamp();
 N   DROP TRIGGER update_radius_lastmodifiedtimestamp_trigger ON public.tblradius;
       public       postgres    false    237    1535            �           2620    55954 (   tblradius update_radius_rebuildmetacache    TRIGGER     �   CREATE TRIGGER update_radius_rebuildmetacache AFTER INSERT OR UPDATE ON public.tblradius FOR EACH ROW EXECUTE PROCEDURE cpgdb.rebuildmetacacheforradius();
 A   DROP TRIGGER update_radius_rebuildmetacache ON public.tblradius;
       public       postgres    false    1130    237            �           2620    55955 -   tblsample update_sample-lastmodifiedtimestamp    TRIGGER     �   CREATE TRIGGER "update_sample-lastmodifiedtimestamp" BEFORE UPDATE ON public.tblsample FOR EACH ROW EXECUTE PROCEDURE public.update_lastmodifiedtimestamp();
 H   DROP TRIGGER "update_sample-lastmodifiedtimestamp" ON public.tblsample;
       public       postgres    false    1535    238            �           2620    55956 (   tblsample update_sample_rebuildmetacache    TRIGGER     �   CREATE TRIGGER update_sample_rebuildmetacache AFTER INSERT OR UPDATE ON public.tblsample FOR EACH ROW EXECUTE PROCEDURE cpgdb.rebuildmetacacheforsample();
 A   DROP TRIGGER update_sample_rebuildmetacache ON public.tblsample;
       public       postgres    false    238    1131            �           2620    55957 4   tblelement update_tree_lastmodifiedtimestamp_trigger    TRIGGER     �   CREATE TRIGGER update_tree_lastmodifiedtimestamp_trigger BEFORE UPDATE ON public.tblelement FOR EACH ROW EXECUTE PROCEDURE public.update_lastmodifiedtimestamp();
 M   DROP TRIGGER update_tree_lastmodifiedtimestamp_trigger ON public.tblelement;
       public       postgres    false    234    1535            �           2620    55958 #   tblvmeasurement update_vmeasurement    TRIGGER     �   CREATE TRIGGER update_vmeasurement AFTER INSERT OR UPDATE ON public.tblvmeasurement FOR EACH ROW EXECUTE PROCEDURE cpgdb.vmeasurementmodifiedtrigger();
 <   DROP TRIGGER update_vmeasurement ON public.tblvmeasurement;
       public       postgres    false    1147    306            �           2620    55959 A   tblvmeasurement update_vmeasurement_lastmodifiedtimestamp_trigger    TRIGGER     �   CREATE TRIGGER update_vmeasurement_lastmodifiedtimestamp_trigger BEFORE UPDATE ON public.tblvmeasurement FOR EACH ROW EXECUTE PROCEDURE public.update_lastmodifiedtimestamp();
 Z   DROP TRIGGER update_vmeasurement_lastmodifiedtimestamp_trigger ON public.tblvmeasurement;
       public       postgres    false    306    1535            �           2620    55960 +   tblelement update_vmeasurementcache_extents    TRIGGER     �   CREATE TRIGGER update_vmeasurementcache_extents AFTER UPDATE ON public.tblelement FOR EACH ROW EXECUTE PROCEDURE cpgdb.elementlocationchangedtrigger();
 D   DROP TRIGGER update_vmeasurementcache_extents ON public.tblelement;
       public       postgres    false    234    1060            �           2620    55961 M   tblvmeasurementresult update_vmeasurementresult_lastmodifiedtimestamp_trigger    TRIGGER     �   CREATE TRIGGER update_vmeasurementresult_lastmodifiedtimestamp_trigger BEFORE UPDATE ON public.tblvmeasurementresult FOR EACH ROW EXECUTE PROCEDURE public.update_lastmodifiedtimestamp();
 f   DROP TRIGGER update_vmeasurementresult_lastmodifiedtimestamp_trigger ON public.tblvmeasurementresult;
       public       postgres    false    316    1535            E           2606    55962 +   tblelement fkey_element-elementauthenticity    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblelement
    ADD CONSTRAINT "fkey_element-elementauthenticity" FOREIGN KEY (elementauthenticityid) REFERENCES public.tlkpelementauthenticity(elementauthenticityid);
 W   ALTER TABLE ONLY public.tblelement DROP CONSTRAINT "fkey_element-elementauthenticity";
       public       postgres    false    334    5348    234            F           2606    55967 $   tblelement fkey_element-elementshape    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblelement
    ADD CONSTRAINT "fkey_element-elementshape" FOREIGN KEY (elementshapeid) REFERENCES public.tlkpelementshape(elementshapeid);
 P   ALTER TABLE ONLY public.tblelement DROP CONSTRAINT "fkey_element-elementshape";
       public       postgres    false    234    336    5350            G           2606    55972 #   tblelement fkey_element-elementtype    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblelement
    ADD CONSTRAINT "fkey_element-elementtype" FOREIGN KEY (elementtypeid) REFERENCES public.tlkpelementtype(elementtypeid);
 O   ALTER TABLE ONLY public.tblelement DROP CONSTRAINT "fkey_element-elementtype";
       public       postgres    false    234    5352    338            H           2606    55977 $   tblelement fkey_element-locationtype    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblelement
    ADD CONSTRAINT "fkey_element-locationtype" FOREIGN KEY (locationtypeid) REFERENCES public.tlkplocationtype(locationtypeid);
 P   ALTER TABLE ONLY public.tblelement DROP CONSTRAINT "fkey_element-locationtype";
       public       postgres    false    342    234    5357            I           2606    55982    tblelement fkey_element_units    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblelement
    ADD CONSTRAINT fkey_element_units FOREIGN KEY (units) REFERENCES public.tlkpunit(unitid) ON UPDATE RESTRICT ON DELETE RESTRICT;
 G   ALTER TABLE ONLY public.tblelement DROP CONSTRAINT fkey_element_units;
       public       postgres    false    234    5400    367            �           2606    55987 +   tlkpelementtype fkey_elementtype-vocabulary    FK CONSTRAINT     �   ALTER TABLE ONLY public.tlkpelementtype
    ADD CONSTRAINT "fkey_elementtype-vocabulary" FOREIGN KEY (vocabularyid) REFERENCES public.tlkpvocabulary(vocabularyid);
 W   ALTER TABLE ONLY public.tlkpelementtype DROP CONSTRAINT "fkey_elementtype-vocabulary";
       public       postgres    false    373    338    5416            `           2606    55992 7   tblenvironmentaldata fkey_environmentaldata_rasterlayer    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblenvironmentaldata
    ADD CONSTRAINT fkey_environmentaldata_rasterlayer FOREIGN KEY (rasterlayerid) REFERENCES public.tblrasterlayer(rasterlayerid);
 a   ALTER TABLE ONLY public.tblenvironmentaldata DROP CONSTRAINT fkey_environmentaldata_rasterlayer;
       public       postgres    false    271    5227    257            a           2606    55997 0   tblenvironmentaldata fkey_environmentaldata_tree    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblenvironmentaldata
    ADD CONSTRAINT fkey_environmentaldata_tree FOREIGN KEY (elementid) REFERENCES public.tblelement(elementid) ON UPDATE CASCADE ON DELETE CASCADE;
 Z   ALTER TABLE ONLY public.tblenvironmentaldata DROP CONSTRAINT fkey_environmentaldata_tree;
       public       postgres    false    257    5157    234            b           2606    56002 *   tbliptracking fkey_iptracking_securityuser    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbliptracking
    ADD CONSTRAINT fkey_iptracking_securityuser FOREIGN KEY (securityuserid) REFERENCES public.tblsecurityuser(securityuserid);
 T   ALTER TABLE ONLY public.tbliptracking DROP CONSTRAINT fkey_iptracking_securityuser;
       public       postgres    false    293    5276    259            <           2606    56007 *   tblmeasurement fkey_measurement-datingtype    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblmeasurement
    ADD CONSTRAINT "fkey_measurement-datingtype" FOREIGN KEY (datingtypeid) REFERENCES public.tlkpdatingtype(datingtypeid) ON UPDATE CASCADE ON DELETE CASCADE;
 V   ALTER TABLE ONLY public.tblmeasurement DROP CONSTRAINT "fkey_measurement-datingtype";
       public       postgres    false    5342    330    232            =           2606    56012 3   tblmeasurement fkey_measurement-measurementvariable    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblmeasurement
    ADD CONSTRAINT "fkey_measurement-measurementvariable" FOREIGN KEY (measurementvariableid) REFERENCES public.tlkpmeasurementvariable(measurementvariableid);
 _   ALTER TABLE ONLY public.tblmeasurement DROP CONSTRAINT "fkey_measurement-measurementvariable";
       public       postgres    false    344    5359    232            >           2606    56017 /   tblmeasurement fkey_measurement-measuringmethod    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblmeasurement
    ADD CONSTRAINT "fkey_measurement-measuringmethod" FOREIGN KEY (measuringmethodid) REFERENCES public.tlkpmeasuringmethod(measuringmethodid);
 [   ALTER TABLE ONLY public.tblmeasurement DROP CONSTRAINT "fkey_measurement-measuringmethod";
       public       postgres    false    232    5361    346            ?           2606    56022 &   tblmeasurement fkey_measurement-radius    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblmeasurement
    ADD CONSTRAINT "fkey_measurement-radius" FOREIGN KEY (radiusid) REFERENCES public.tblradius(radiusid) ON UPDATE CASCADE ON DELETE RESTRICT;
 R   ALTER TABLE ONLY public.tblmeasurement DROP CONSTRAINT "fkey_measurement-radius";
       public       postgres    false    5168    237    232            @           2606    56027 ,   tblmeasurement fkey_measurement-securityuser    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblmeasurement
    ADD CONSTRAINT "fkey_measurement-securityuser" FOREIGN KEY (measuredbyid) REFERENCES public.tblsecurityuser(securityuserid);
 X   ALTER TABLE ONLY public.tblmeasurement DROP CONSTRAINT "fkey_measurement-securityuser";
       public       postgres    false    232    293    5276            A           2606    56032 ,   tblmeasurement fkey_measurement-supervisedby    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblmeasurement
    ADD CONSTRAINT "fkey_measurement-supervisedby" FOREIGN KEY (supervisedbyid) REFERENCES public.tblsecurityuser(securityuserid);
 X   ALTER TABLE ONLY public.tblmeasurement DROP CONSTRAINT "fkey_measurement-supervisedby";
       public       postgres    false    5276    232    293            B           2606    56037 $   tblmeasurement fkey_measurement-unit    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblmeasurement
    ADD CONSTRAINT "fkey_measurement-unit" FOREIGN KEY (unitid) REFERENCES public.tlkpunit(unitid);
 P   ALTER TABLE ONLY public.tblmeasurement DROP CONSTRAINT "fkey_measurement-unit";
       public       postgres    false    367    232    5400            �           2606    56042 >   tblvmeasurementreadingnoteresult fkey_noteresult-readingresult    FK CONSTRAINT       ALTER TABLE ONLY public.tblvmeasurementreadingnoteresult
    ADD CONSTRAINT "fkey_noteresult-readingresult" FOREIGN KEY (vmeasurementresultid, relyear) REFERENCES public.tblvmeasurementreadingresult(vmeasurementresultid, relyear) ON UPDATE CASCADE ON DELETE CASCADE;
 j   ALTER TABLE ONLY public.tblvmeasurementreadingnoteresult DROP CONSTRAINT "fkey_noteresult-readingresult";
       public       postgres    false    5177    312    239    239    312            1           2606    56047 &   tblobject fkey_object-coveragetemporal    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblobject
    ADD CONSTRAINT "fkey_object-coveragetemporal" FOREIGN KEY (coveragetemporalid) REFERENCES public.tlkpcoveragetemporal(coveragetemporalid);
 R   ALTER TABLE ONLY public.tblobject DROP CONSTRAINT "fkey_object-coveragetemporal";
       public       postgres    false    5330    229    321            2           2606    56052 0   tblobject fkey_object-coveragetemporalfoundation    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblobject
    ADD CONSTRAINT "fkey_object-coveragetemporalfoundation" FOREIGN KEY (coveragetemporalfoundationid) REFERENCES public.tlkpcoveragetemporalfoundation(coveragetemporalfoundationid);
 \   ALTER TABLE ONLY public.tblobject DROP CONSTRAINT "fkey_object-coveragetemporalfoundation";
       public       postgres    false    229    323    5332            3           2606    56057     tblobject fkey_object-objecttype    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblobject
    ADD CONSTRAINT "fkey_object-objecttype" FOREIGN KEY (objecttypeid) REFERENCES public.tlkpobjecttype(objecttypeid);
 L   ALTER TABLE ONLY public.tblobject DROP CONSTRAINT "fkey_object-objecttype";
       public       postgres    false    5363    229    348            4           2606    56062    tblobject fkey_object-project    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblobject
    ADD CONSTRAINT "fkey_object-project" FOREIGN KEY (projectid) REFERENCES public.tblproject(projectid);
 I   ALTER TABLE ONLY public.tblobject DROP CONSTRAINT "fkey_object-project";
       public       postgres    false    5219    267    229            c           2606    56067 (   tblobjectregion fkey_objectregion-object    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblobjectregion
    ADD CONSTRAINT "fkey_objectregion-object" FOREIGN KEY (objectid) REFERENCES public.tblobject(objectid) ON UPDATE CASCADE ON DELETE CASCADE;
 T   ALTER TABLE ONLY public.tblobjectregion DROP CONSTRAINT "fkey_objectregion-object";
       public       postgres    false    263    229    5133            d           2606    56072 (   tblobjectregion fkey_objectregion_region    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblobjectregion
    ADD CONSTRAINT fkey_objectregion_region FOREIGN KEY (regionid) REFERENCES public.tblregion(regionid);
 R   ALTER TABLE ONLY public.tblobjectregion DROP CONSTRAINT fkey_objectregion_region;
       public       postgres    false    263    5247    279            �           2606    56077 )   tlkpobjecttype fkey_objecttype-vocabulary    FK CONSTRAINT     �   ALTER TABLE ONLY public.tlkpobjecttype
    ADD CONSTRAINT "fkey_objecttype-vocabulary" FOREIGN KEY (vocabularyid) REFERENCES public.tlkpvocabulary(vocabularyid);
 U   ALTER TABLE ONLY public.tlkpobjecttype DROP CONSTRAINT "fkey_objecttype-vocabulary";
       public       postgres    false    5416    373    348            e           2606    56082 0   tblodkdefinition fkey_odkdefinition-securityuser    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblodkdefinition
    ADD CONSTRAINT "fkey_odkdefinition-securityuser" FOREIGN KEY (ownerid) REFERENCES public.tblsecurityuser(securityuserid);
 \   ALTER TABLE ONLY public.tblodkdefinition DROP CONSTRAINT "fkey_odkdefinition-securityuser";
       public       tellervo    false    293    265    5276            f           2606    56087 ,   tblodkinstance fkey_odkinstance-securityuser    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblodkinstance
    ADD CONSTRAINT "fkey_odkinstance-securityuser" FOREIGN KEY (ownerid) REFERENCES public.tblsecurityuser(securityuserid);
 X   ALTER TABLE ONLY public.tblodkinstance DROP CONSTRAINT "fkey_odkinstance-securityuser";
       public       tellervo    false    266    293    5276            g           2606    56092 '   tblproject fkey_project-projectcategory    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblproject
    ADD CONSTRAINT "fkey_project-projectcategory" FOREIGN KEY (projectcategoryid) REFERENCES public.tlkpprojectcategory(projectcategoryid);
 S   ALTER TABLE ONLY public.tblproject DROP CONSTRAINT "fkey_project-projectcategory";
       public       tellervo    false    5369    267    353            �           2606    56097 4   tlkpprojectcategory fkey_projectcategory-vocabulary2    FK CONSTRAINT     �   ALTER TABLE ONLY public.tlkpprojectcategory
    ADD CONSTRAINT "fkey_projectcategory-vocabulary2" FOREIGN KEY (vocabularyid) REFERENCES public.tlkpvocabulary(vocabularyid);
 `   ALTER TABLE ONLY public.tlkpprojectcategory DROP CONSTRAINT "fkey_projectcategory-vocabulary2";
       public       tellervo    false    5416    353    373            i           2606    56102 4   tblprojectprojecttype fkey_projectprojectype-project    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblprojectprojecttype
    ADD CONSTRAINT "fkey_projectprojectype-project" FOREIGN KEY (projectid) REFERENCES public.tblproject(projectid);
 `   ALTER TABLE ONLY public.tblprojectprojecttype DROP CONSTRAINT "fkey_projectprojectype-project";
       public       tellervo    false    268    5219    267            j           2606    56107 8   tblprojectprojecttype fkey_projectprojectype-projecttype    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblprojectprojecttype
    ADD CONSTRAINT "fkey_projectprojectype-projecttype" FOREIGN KEY (projecttypeid) REFERENCES public.tlkpprojecttype(projecttypeid);
 d   ALTER TABLE ONLY public.tblprojectprojecttype DROP CONSTRAINT "fkey_projectprojectype-projecttype";
       public       tellervo    false    5373    355    268            �           2606    56112 +   tlkpprojecttype fkey_projecttype-vocabulary    FK CONSTRAINT     �   ALTER TABLE ONLY public.tlkpprojecttype
    ADD CONSTRAINT "fkey_projecttype-vocabulary" FOREIGN KEY (vocabularyid) REFERENCES public.tlkpvocabulary(vocabularyid);
 W   ALTER TABLE ONLY public.tlkpprojecttype DROP CONSTRAINT "fkey_projecttype-vocabulary";
       public       tellervo    false    355    5416    373            L           2606    56117    tblradius fkey_radius-heartwood    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblradius
    ADD CONSTRAINT "fkey_radius-heartwood" FOREIGN KEY (heartwoodid) REFERENCES public.tlkpcomplexpresenceabsence(complexpresenceabsenceid);
 K   ALTER TABLE ONLY public.tblradius DROP CONSTRAINT "fkey_radius-heartwood";
       public       postgres    false    319    5328    237            M           2606    56122    tblradius fkey_radius-pith    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblradius
    ADD CONSTRAINT "fkey_radius-pith" FOREIGN KEY (pithid) REFERENCES public.tlkpcomplexpresenceabsence(complexpresenceabsenceid);
 F   ALTER TABLE ONLY public.tblradius DROP CONSTRAINT "fkey_radius-pith";
       public       postgres    false    319    237    5328            N           2606    56127    tblradius fkey_radius-sample    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblradius
    ADD CONSTRAINT "fkey_radius-sample" FOREIGN KEY (sampleid) REFERENCES public.tblsample(sampleid) ON UPDATE CASCADE ON DELETE RESTRICT;
 H   ALTER TABLE ONLY public.tblradius DROP CONSTRAINT "fkey_radius-sample";
       public       postgres    false    5174    237    238            O           2606    56132    tblradius fkey_radius-sapwood    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblradius
    ADD CONSTRAINT "fkey_radius-sapwood" FOREIGN KEY (sapwoodid) REFERENCES public.tlkpcomplexpresenceabsence(complexpresenceabsenceid);
 I   ALTER TABLE ONLY public.tblradius DROP CONSTRAINT "fkey_radius-sapwood";
       public       postgres    false    5328    319    237            P           2606    56137 6   tblradius fkey_radius_lastringunderbarkpresenceabsence    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblradius
    ADD CONSTRAINT fkey_radius_lastringunderbarkpresenceabsence FOREIGN KEY (lrubpa) REFERENCES public.tlkppresenceabsence(presenceabsenceid);
 `   ALTER TABLE ONLY public.tblradius DROP CONSTRAINT fkey_radius_lastringunderbarkpresenceabsence;
       public       postgres    false    350    237    5367            k           2606    56142 #   tblreading fkey_reading-measurement    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblreading
    ADD CONSTRAINT "fkey_reading-measurement" FOREIGN KEY (measurementid) REFERENCES public.tblmeasurement(measurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 O   ALTER TABLE ONLY public.tblreading DROP CONSTRAINT "fkey_reading-measurement";
       public       postgres    false    232    5148    273            7           2606    56147 +   tlkpreadingnote fkey_readingnote-vocabulary    FK CONSTRAINT     �   ALTER TABLE ONLY public.tlkpreadingnote
    ADD CONSTRAINT "fkey_readingnote-vocabulary" FOREIGN KEY (vocabularyid) REFERENCES public.tlkpvocabulary(vocabularyid);
 W   ALTER TABLE ONLY public.tlkpreadingnote DROP CONSTRAINT "fkey_readingnote-vocabulary";
       public       postgres    false    5416    230    373            l           2606    56152 5   tblreadingreadingnote fkey_readingreadingnote-reading    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblreadingreadingnote
    ADD CONSTRAINT "fkey_readingreadingnote-reading" FOREIGN KEY (readingid) REFERENCES public.tblreading(readingid) ON UPDATE CASCADE ON DELETE CASCADE;
 a   ALTER TABLE ONLY public.tblreadingreadingnote DROP CONSTRAINT "fkey_readingreadingnote-reading";
       public       postgres    false    275    5234    273            m           2606    56157 9   tblreadingreadingnote fkey_readingreadingnote-readingnote    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblreadingreadingnote
    ADD CONSTRAINT "fkey_readingreadingnote-readingnote" FOREIGN KEY (readingnoteid) REFERENCES public.tlkpreadingnote(readingnoteid) ON UPDATE CASCADE;
 e   ALTER TABLE ONLY public.tblreadingreadingnote DROP CONSTRAINT "fkey_readingreadingnote-readingnote";
       public       postgres    false    275    5141    230            �           2606    56162 E   tblvmeasurementrelyearreadingnote fkey_relyearreadingnote-readingnote    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementrelyearreadingnote
    ADD CONSTRAINT "fkey_relyearreadingnote-readingnote" FOREIGN KEY (readingnoteid) REFERENCES public.tlkpreadingnote(readingnoteid) ON UPDATE CASCADE ON DELETE RESTRICT;
 q   ALTER TABLE ONLY public.tblvmeasurementrelyearreadingnote DROP CONSTRAINT "fkey_relyearreadingnote-readingnote";
       public       postgres    false    315    5141    230            �           2606    56167 F   tblvmeasurementrelyearreadingnote fkey_relyearreadingnote-vmeasurement    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementrelyearreadingnote
    ADD CONSTRAINT "fkey_relyearreadingnote-vmeasurement" FOREIGN KEY (vmeasurementid) REFERENCES public.tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 r   ALTER TABLE ONLY public.tblvmeasurementrelyearreadingnote DROP CONSTRAINT "fkey_relyearreadingnote-vmeasurement";
       public       postgres    false    5310    306    315            p           2606    56172 *   tblrequestlog fkey_requestlog-securityuser    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblrequestlog
    ADD CONSTRAINT "fkey_requestlog-securityuser" FOREIGN KEY (securityuserid) REFERENCES public.tblsecurityuser(securityuserid);
 V   ALTER TABLE ONLY public.tblrequestlog DROP CONSTRAINT "fkey_requestlog-securityuser";
       public       postgres    false    293    5276    281            �           2606    56177 1   tblvmeasurementresult fkey_result-vmeasurement_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementresult
    ADD CONSTRAINT "fkey_result-vmeasurement_id" FOREIGN KEY (vmeasurementid) REFERENCES public.tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 ]   ALTER TABLE ONLY public.tblvmeasurementresult DROP CONSTRAINT "fkey_result-vmeasurement_id";
       public       postgres    false    306    5310    316            R           2606    56182    tblsample fkey_sample-box    FK CONSTRAINT     |   ALTER TABLE ONLY public.tblsample
    ADD CONSTRAINT "fkey_sample-box" FOREIGN KEY (boxid) REFERENCES public.tblbox(boxid);
 E   ALTER TABLE ONLY public.tblsample DROP CONSTRAINT "fkey_sample-box";
       public       postgres    false    244    5181    238            S           2606    56187 #   tblsample fkey_sample-datecertainty    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsample
    ADD CONSTRAINT "fkey_sample-datecertainty" FOREIGN KEY (datecertaintyid) REFERENCES public.tlkpdatecertainty(datecertaintyid);
 O   ALTER TABLE ONLY public.tblsample DROP CONSTRAINT "fkey_sample-datecertainty";
       public       postgres    false    238    328    5338            T           2606    56192 "   tblsample fkey_sample-samplestatus    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsample
    ADD CONSTRAINT "fkey_sample-samplestatus" FOREIGN KEY (samplestatusid) REFERENCES public.tlkpsamplestatus(samplestatusid);
 N   ALTER TABLE ONLY public.tblsample DROP CONSTRAINT "fkey_sample-samplestatus";
       public       postgres    false    5377    238    358            U           2606    56197     tblsample fkey_sample-sampletype    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsample
    ADD CONSTRAINT "fkey_sample-sampletype" FOREIGN KEY (typeid) REFERENCES public.tlkpsampletype(sampletypeid);
 L   ALTER TABLE ONLY public.tblsample DROP CONSTRAINT "fkey_sample-sampletype";
       public       postgres    false    5381    238    360            V           2606    56202    tblsample fkey_sample-tree    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsample
    ADD CONSTRAINT "fkey_sample-tree" FOREIGN KEY (elementid) REFERENCES public.tblelement(elementid) ON UPDATE CASCADE ON DELETE RESTRICT;
 F   ALTER TABLE ONLY public.tblsample DROP CONSTRAINT "fkey_sample-tree";
       public       postgres    false    238    5157    234            q           2606    56207 5   tblsecuritydefault fkey_securitydefault-securitygroup    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecuritydefault
    ADD CONSTRAINT "fkey_securitydefault-securitygroup" FOREIGN KEY (securitygroupid) REFERENCES public.tblsecuritygroup(securitygroupid) ON UPDATE CASCADE ON DELETE CASCADE;
 a   ALTER TABLE ONLY public.tblsecuritydefault DROP CONSTRAINT "fkey_securitydefault-securitygroup";
       public       postgres    false    284    5163    235            r           2606    56212 :   tblsecuritydefault fkey_securitydefault-securitypermission    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecuritydefault
    ADD CONSTRAINT "fkey_securitydefault-securitypermission" FOREIGN KEY (securitypermissionid) REFERENCES public.tlkpsecuritypermission(securitypermissionid) ON UPDATE CASCADE;
 f   ALTER TABLE ONLY public.tblsecuritydefault DROP CONSTRAINT "fkey_securitydefault-securitypermission";
       public       postgres    false    284    362    5385            v           2606    56217 F   tblsecuritygroupmembership fkey_securitygroupmembership-securitygroup1    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecuritygroupmembership
    ADD CONSTRAINT "fkey_securitygroupmembership-securitygroup1" FOREIGN KEY (parentsecuritygroupid) REFERENCES public.tblsecuritygroup(securitygroupid) ON UPDATE CASCADE ON DELETE CASCADE;
 r   ALTER TABLE ONLY public.tblsecuritygroupmembership DROP CONSTRAINT "fkey_securitygroupmembership-securitygroup1";
       public       postgres    false    5163    288    235            w           2606    56222 F   tblsecuritygroupmembership fkey_securitygroupmembership-securitygroup2    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecuritygroupmembership
    ADD CONSTRAINT "fkey_securitygroupmembership-securitygroup2" FOREIGN KEY (childsecuritygroupid) REFERENCES public.tblsecuritygroup(securitygroupid) ON UPDATE CASCADE ON DELETE CASCADE;
 r   ALTER TABLE ONLY public.tblsecuritygroupmembership DROP CONSTRAINT "fkey_securitygroupmembership-securitygroup2";
       public       postgres    false    5163    288    235            x           2606    56227 ,   tblsecurityobject fkey_securityobject-object    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityobject
    ADD CONSTRAINT "fkey_securityobject-object" FOREIGN KEY (objectid) REFERENCES public.tblobject(objectid) ON UPDATE CASCADE ON DELETE CASCADE;
 X   ALTER TABLE ONLY public.tblsecurityobject DROP CONSTRAINT "fkey_securityobject-object";
       public       postgres    false    290    229    5133            y           2606    56232 3   tblsecurityobject fkey_securityobject-securitygroup    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityobject
    ADD CONSTRAINT "fkey_securityobject-securitygroup" FOREIGN KEY (securitygroupid) REFERENCES public.tblsecuritygroup(securitygroupid) ON UPDATE CASCADE;
 _   ALTER TABLE ONLY public.tblsecurityobject DROP CONSTRAINT "fkey_securityobject-securitygroup";
       public       postgres    false    290    5163    235            z           2606    56237 8   tblsecurityobject fkey_securityobject-securitypermission    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityobject
    ADD CONSTRAINT "fkey_securityobject-securitypermission" FOREIGN KEY (securitypermissionid) REFERENCES public.tlkpsecuritypermission(securitypermissionid) ON UPDATE CASCADE;
 d   ALTER TABLE ONLY public.tblsecurityobject DROP CONSTRAINT "fkey_securityobject-securitypermission";
       public       postgres    false    362    290    5385            s           2606    56242 2   tblsecurityelement fkey_securitytree-securitygroup    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityelement
    ADD CONSTRAINT "fkey_securitytree-securitygroup" FOREIGN KEY (securitygroupid) REFERENCES public.tblsecuritygroup(securitygroupid) ON UPDATE CASCADE;
 ^   ALTER TABLE ONLY public.tblsecurityelement DROP CONSTRAINT "fkey_securitytree-securitygroup";
       public       postgres    false    286    5163    235            t           2606    56247 7   tblsecurityelement fkey_securitytree-securitypermission    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityelement
    ADD CONSTRAINT "fkey_securitytree-securitypermission" FOREIGN KEY (securitypermissionid) REFERENCES public.tlkpsecuritypermission(securitypermissionid) ON UPDATE CASCADE;
 c   ALTER TABLE ONLY public.tblsecurityelement DROP CONSTRAINT "fkey_securitytree-securitypermission";
       public       postgres    false    362    286    5385            u           2606    56252 )   tblsecurityelement fkey_securitytree-tree    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityelement
    ADD CONSTRAINT "fkey_securitytree-tree" FOREIGN KEY (elementid) REFERENCES public.tblelement(elementid) ON UPDATE CASCADE ON DELETE CASCADE;
 U   ALTER TABLE ONLY public.tblsecurityelement DROP CONSTRAINT "fkey_securitytree-tree";
       public       postgres    false    286    5157    234            {           2606    56257 C   tblsecurityusermembership fkey_securityusermembership-securitygroup    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityusermembership
    ADD CONSTRAINT "fkey_securityusermembership-securitygroup" FOREIGN KEY (securitygroupid) REFERENCES public.tblsecuritygroup(securitygroupid) ON UPDATE CASCADE ON DELETE CASCADE;
 o   ALTER TABLE ONLY public.tblsecurityusermembership DROP CONSTRAINT "fkey_securityusermembership-securitygroup";
       public       postgres    false    294    5163    235            |           2606    56262 ?   tblsecurityvmeasurement fkey_securityvmeasurement-securitygroup    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityvmeasurement
    ADD CONSTRAINT "fkey_securityvmeasurement-securitygroup" FOREIGN KEY (securitygroupid) REFERENCES public.tblsecuritygroup(securitygroupid) ON UPDATE CASCADE;
 k   ALTER TABLE ONLY public.tblsecurityvmeasurement DROP CONSTRAINT "fkey_securityvmeasurement-securitygroup";
       public       postgres    false    296    5163    235            }           2606    56267 D   tblsecurityvmeasurement fkey_securityvmeasurement-securitypermission    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityvmeasurement
    ADD CONSTRAINT "fkey_securityvmeasurement-securitypermission" FOREIGN KEY (securitypermissionid) REFERENCES public.tlkpsecuritypermission(securitypermissionid) ON UPDATE CASCADE;
 p   ALTER TABLE ONLY public.tblsecurityvmeasurement DROP CONSTRAINT "fkey_securityvmeasurement-securitypermission";
       public       postgres    false    296    5385    362            ~           2606    56272 >   tblsecurityvmeasurement fkey_securityvmeasurement-vmeasurement    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsecurityvmeasurement
    ADD CONSTRAINT "fkey_securityvmeasurement-vmeasurement" FOREIGN KEY (vmeasurementid) REFERENCES public.tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 j   ALTER TABLE ONLY public.tblsecurityvmeasurement DROP CONSTRAINT "fkey_securityvmeasurement-vmeasurement";
       public       postgres    false    296    5310    306                       2606    56277 !   tbltag fkey_tagowner-securityuser    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbltag
    ADD CONSTRAINT "fkey_tagowner-securityuser" FOREIGN KEY (ownerid) REFERENCES public.tblsecurityuser(securityuserid) ON UPDATE CASCADE ON DELETE CASCADE;
 M   ALTER TABLE ONLY public.tbltag DROP CONSTRAINT "fkey_tagowner-securityuser";
       public       tellervo    false    300    5276    293            �           2606    56282    tlkptaxon fkey_taxon-taxonrank    FK CONSTRAINT     �   ALTER TABLE ONLY public.tlkptaxon
    ADD CONSTRAINT "fkey_taxon-taxonrank" FOREIGN KEY (taxonrankid) REFERENCES public.tlkptaxonrank(taxonrankid) ON UPDATE CASCADE;
 J   ALTER TABLE ONLY public.tlkptaxon DROP CONSTRAINT "fkey_taxon-taxonrank";
       public       postgres    false    5394    366    365            Y           2606    56287 .   tblcrossdate fkey_tblcrossdate-tblvmeasurement    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblcrossdate
    ADD CONSTRAINT "fkey_tblcrossdate-tblvmeasurement" FOREIGN KEY (vmeasurementid) REFERENCES public.tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 Z   ALTER TABLE ONLY public.tblcrossdate DROP CONSTRAINT "fkey_tblcrossdate-tblvmeasurement";
       public       postgres    false    306    5310    247            Z           2606    56292 5   tblcrossdate fkey_tblcrossdate-tblvmeasurement_master    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblcrossdate
    ADD CONSTRAINT "fkey_tblcrossdate-tblvmeasurement_master" FOREIGN KEY (mastervmeasurementid) REFERENCES public.tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE RESTRICT;
 a   ALTER TABLE ONLY public.tblcrossdate DROP CONSTRAINT "fkey_tblcrossdate-tblvmeasurement_master";
       public       postgres    false    306    5310    247            [           2606    56297 (   tblcurationevent fkey_tblcuration-tblbox    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblcurationevent
    ADD CONSTRAINT "fkey_tblcuration-tblbox" FOREIGN KEY (boxid) REFERENCES public.tblbox(boxid) ON DELETE CASCADE;
 T   ALTER TABLE ONLY public.tblcurationevent DROP CONSTRAINT "fkey_tblcuration-tblbox";
       public       tellervo    false    244    5181    249            \           2606    56302 )   tblcurationevent fkey_tblcuration-tblloan    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblcurationevent
    ADD CONSTRAINT "fkey_tblcuration-tblloan" FOREIGN KEY (loanid) REFERENCES public.tblloan(loanid);
 U   ALTER TABLE ONLY public.tblcurationevent DROP CONSTRAINT "fkey_tblcuration-tblloan";
       public       tellervo    false    236    5165    249            ]           2606    56307 +   tblcurationevent fkey_tblcuration-tblsample    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblcurationevent
    ADD CONSTRAINT "fkey_tblcuration-tblsample" FOREIGN KEY (sampleid) REFERENCES public.tblsample(sampleid);
 W   ALTER TABLE ONLY public.tblcurationevent DROP CONSTRAINT "fkey_tblcuration-tblsample";
       public       tellervo    false    238    249    5174            ^           2606    56312 1   tblcurationevent fkey_tblcuration-tblsecurityuser    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblcurationevent
    ADD CONSTRAINT "fkey_tblcuration-tblsecurityuser" FOREIGN KEY (curatorid) REFERENCES public.tblsecurityuser(securityuserid);
 ]   ALTER TABLE ONLY public.tblcurationevent DROP CONSTRAINT "fkey_tblcuration-tblsecurityuser";
       public       tellervo    false    293    249    5276            _           2606    56317 4   tblcurationevent fkey_tblcuration-tlkpcurationstatus    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblcurationevent
    ADD CONSTRAINT "fkey_tblcuration-tlkpcurationstatus" FOREIGN KEY (curationstatusid) REFERENCES public.tlkpcurationstatus(curationstatusid);
 `   ALTER TABLE ONLY public.tblcurationevent DROP CONSTRAINT "fkey_tblcuration-tlkpcurationstatus";
       public       tellervo    false    249    325    5334            J           2606    56322 %   tblelement fkey_tblelement_tlkpdomain    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblelement
    ADD CONSTRAINT fkey_tblelement_tlkpdomain FOREIGN KEY (domainid) REFERENCES public.tlkpdomain(domainid);
 O   ALTER TABLE ONLY public.tblelement DROP CONSTRAINT fkey_tblelement_tlkpdomain;
       public       postgres    false    234    5344    332            5           2606    56327 #   tblobject fkey_tblobject_tlkpdomain    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblobject
    ADD CONSTRAINT fkey_tblobject_tlkpdomain FOREIGN KEY (domainid) REFERENCES public.tlkpdomain(domainid);
 M   ALTER TABLE ONLY public.tblobject DROP CONSTRAINT fkey_tblobject_tlkpdomain;
       public       postgres    false    5344    229    332            h           2606    56332 %   tblproject fkey_tblproject_tlkpdomain    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblproject
    ADD CONSTRAINT fkey_tblproject_tlkpdomain FOREIGN KEY (domainid) REFERENCES public.tlkpdomain(domainid);
 O   ALTER TABLE ONLY public.tblproject DROP CONSTRAINT fkey_tblproject_tlkpdomain;
       public       tellervo    false    267    332    5344            Q           2606    56337 #   tblradius fkey_tblradius_tlkpdomain    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblradius
    ADD CONSTRAINT fkey_tblradius_tlkpdomain FOREIGN KEY (domainid) REFERENCES public.tlkpdomain(domainid);
 M   ALTER TABLE ONLY public.tblradius DROP CONSTRAINT fkey_tblradius_tlkpdomain;
       public       postgres    false    332    237    5344            n           2606    56342 (   tblredate fkey_tblredate-tblvmeasurement    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblredate
    ADD CONSTRAINT "fkey_tblredate-tblvmeasurement" FOREIGN KEY (vmeasurementid) REFERENCES public.tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 T   ALTER TABLE ONLY public.tblredate DROP CONSTRAINT "fkey_tblredate-tblvmeasurement";
       public       postgres    false    5310    277    306            o           2606    56347 '   tblredate fkey_tblredate-tlkpdatingtype    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblredate
    ADD CONSTRAINT "fkey_tblredate-tlkpdatingtype" FOREIGN KEY (redatingtypeid) REFERENCES public.tlkpdatingtype(datingtypeid) ON UPDATE CASCADE ON DELETE RESTRICT;
 S   ALTER TABLE ONLY public.tblredate DROP CONSTRAINT "fkey_tblredate-tlkpdatingtype";
       public       postgres    false    277    330    5342            W           2606    56352 #   tblsample fkey_tblsample_tlkpdomain    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblsample
    ADD CONSTRAINT fkey_tblsample_tlkpdomain FOREIGN KEY (domainid) REFERENCES public.tlkpdomain(domainid);
 M   ALTER TABLE ONLY public.tblsample DROP CONSTRAINT fkey_tblsample_tlkpdomain;
       public       postgres    false    5344    332    238            �           2606    56357 ,   tbltruncate fkey_tbltruncate-tblvmeasurement    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbltruncate
    ADD CONSTRAINT "fkey_tbltruncate-tblvmeasurement" FOREIGN KEY (vmeasurementid) REFERENCES public.tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 X   ALTER TABLE ONLY public.tbltruncate DROP CONSTRAINT "fkey_tbltruncate-tblvmeasurement";
       public       postgres    false    306    301    5310            �           2606    56362 /   tblvmeasurement fkey_tblvmeasurement_tlkpdomain    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurement
    ADD CONSTRAINT fkey_tblvmeasurement_tlkpdomain FOREIGN KEY (domainid) REFERENCES public.tlkpdomain(domainid);
 Y   ALTER TABLE ONLY public.tblvmeasurement DROP CONSTRAINT fkey_tblvmeasurement_tlkpdomain;
       public       postgres    false    306    332    5344            �           2606    56367 D   tbluserdefinedfieldvalue fkey_userdefinedfieldvalue-userdefinedfield    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbluserdefinedfieldvalue
    ADD CONSTRAINT "fkey_userdefinedfieldvalue-userdefinedfield" FOREIGN KEY (userdefinedfieldid) REFERENCES public.tlkpuserdefinedfield(userdefinedfieldid);
 p   ALTER TABLE ONLY public.tbluserdefinedfieldvalue DROP CONSTRAINT "fkey_userdefinedfieldvalue-userdefinedfield";
       public       tellervo    false    5404    369    305            �           2606    56372 -   tblvmeasurement fkey_vmeasurement-measurement    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurement
    ADD CONSTRAINT "fkey_vmeasurement-measurement" FOREIGN KEY (measurementid) REFERENCES public.tblmeasurement(measurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 Y   ALTER TABLE ONLY public.tblvmeasurement DROP CONSTRAINT "fkey_vmeasurement-measurement";
       public       postgres    false    232    306    5148            �           2606    56377 .   tblvmeasurement fkey_vmeasurement-securityuser    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurement
    ADD CONSTRAINT "fkey_vmeasurement-securityuser" FOREIGN KEY (owneruserid) REFERENCES public.tblsecurityuser(securityuserid);
 Z   ALTER TABLE ONLY public.tblvmeasurement DROP CONSTRAINT "fkey_vmeasurement-securityuser";
       public       postgres    false    5276    306    293            �           2606    56382 0   tblvmeasurement fkey_vmeasurement-vmeasurementop    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurement
    ADD CONSTRAINT "fkey_vmeasurement-vmeasurementop" FOREIGN KEY (vmeasurementopid) REFERENCES public.tlkpvmeasurementop(vmeasurementopid) ON UPDATE CASCADE;
 \   ALTER TABLE ONLY public.tblvmeasurement DROP CONSTRAINT "fkey_vmeasurement-vmeasurementop";
       public       postgres    false    306    5412    371            �           2606    56387 9   tblvmeasurementgroup fkey_vmeasurementgroup-vmeasurement1    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementgroup
    ADD CONSTRAINT "fkey_vmeasurementgroup-vmeasurement1" FOREIGN KEY (vmeasurementid) REFERENCES public.tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 e   ALTER TABLE ONLY public.tblvmeasurementgroup DROP CONSTRAINT "fkey_vmeasurementgroup-vmeasurement1";
       public       postgres    false    306    5310    309            �           2606    56392 9   tblvmeasurementgroup fkey_vmeasurementgroup-vmeasurement2    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementgroup
    ADD CONSTRAINT "fkey_vmeasurementgroup-vmeasurement2" FOREIGN KEY (membervmeasurementid) REFERENCES public.tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE RESTRICT;
 e   ALTER TABLE ONLY public.tblvmeasurementgroup DROP CONSTRAINT "fkey_vmeasurementgroup-vmeasurement2";
       public       postgres    false    306    309    5310            X           2606    56397 N   tblvmeasurementreadingresult fkey_vmeasurementreadingresult-vmeasurementresult    FK CONSTRAINT       ALTER TABLE ONLY public.tblvmeasurementreadingresult
    ADD CONSTRAINT "fkey_vmeasurementreadingresult-vmeasurementresult" FOREIGN KEY (vmeasurementresultid) REFERENCES public.tblvmeasurementresult(vmeasurementresultid) ON UPDATE CASCADE ON DELETE CASCADE;
 z   ALTER TABLE ONLY public.tblvmeasurementreadingresult DROP CONSTRAINT "fkey_vmeasurementreadingresult-vmeasurementresult";
       public       postgres    false    5322    239    316            �           2606    56402 /   tblvmeasurementtotag fkey_vmeasurementtotag-tag    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementtotag
    ADD CONSTRAINT "fkey_vmeasurementtotag-tag" FOREIGN KEY (tagid) REFERENCES public.tbltag(tagid) ON UPDATE CASCADE ON DELETE CASCADE;
 [   ALTER TABLE ONLY public.tblvmeasurementtotag DROP CONSTRAINT "fkey_vmeasurementtotag-tag";
       public       tellervo    false    300    317    5296            �           2606    56407 8   tblvmeasurementtotag fkey_vmeasurementtotag-vmeasurement    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementtotag
    ADD CONSTRAINT "fkey_vmeasurementtotag-vmeasurement" FOREIGN KEY (vmeasurementid) REFERENCES public.tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 d   ALTER TABLE ONLY public.tblvmeasurementtotag DROP CONSTRAINT "fkey_vmeasurementtotag-vmeasurement";
       public       tellervo    false    317    306    5310            K           2606    56412 #   tblelement tblelement_objectid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblelement
    ADD CONSTRAINT tblelement_objectid_fkey FOREIGN KEY (objectid) REFERENCES public.tblobject(objectid) ON UPDATE CASCADE ON DELETE RESTRICT;
 M   ALTER TABLE ONLY public.tblelement DROP CONSTRAINT tblelement_objectid_fkey;
       public       postgres    false    229    5133    234            6           2606    56417 '   tblobject tblobject_parentobjectid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblobject
    ADD CONSTRAINT tblobject_parentobjectid_fkey FOREIGN KEY (parentobjectid) REFERENCES public.tblobject(objectid) ON UPDATE CASCADE ON DELETE RESTRICT;
 Q   ALTER TABLE ONLY public.tblobject DROP CONSTRAINT tblobject_parentobjectid_fkey;
       public       postgres    false    5133    229    229            C           2606    56422 J   tblvmeasurementderivedcache tblvmeasurementderivedcache_measurementid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementderivedcache
    ADD CONSTRAINT tblvmeasurementderivedcache_measurementid_fkey FOREIGN KEY (measurementid) REFERENCES public.tblmeasurement(measurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 t   ALTER TABLE ONLY public.tblvmeasurementderivedcache DROP CONSTRAINT tblvmeasurementderivedcache_measurementid_fkey;
       public       postgres    false    5148    232    233            D           2606    56427 K   tblvmeasurementderivedcache tblvmeasurementderivedcache_vmeasurementid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementderivedcache
    ADD CONSTRAINT tblvmeasurementderivedcache_vmeasurementid_fkey FOREIGN KEY (vmeasurementid) REFERENCES public.tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 u   ALTER TABLE ONLY public.tblvmeasurementderivedcache DROP CONSTRAINT tblvmeasurementderivedcache_vmeasurementid_fkey;
       public       postgres    false    5310    233    306            :           2606    56432 C   tblvmeasurementmetacache tblvmeasurementmetacache_datingtypeid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementmetacache
    ADD CONSTRAINT tblvmeasurementmetacache_datingtypeid_fkey FOREIGN KEY (datingtypeid) REFERENCES public.tlkpdatingtype(datingtypeid) ON UPDATE CASCADE ON DELETE RESTRICT;
 m   ALTER TABLE ONLY public.tblvmeasurementmetacache DROP CONSTRAINT tblvmeasurementmetacache_datingtypeid_fkey;
       public       postgres    false    231    330    5342            ;           2606    56437 E   tblvmeasurementmetacache tblvmeasurementmetacache_vmeasurementid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementmetacache
    ADD CONSTRAINT tblvmeasurementmetacache_vmeasurementid_fkey FOREIGN KEY (vmeasurementid) REFERENCES public.tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE CASCADE;
 o   ALTER TABLE ONLY public.tblvmeasurementmetacache DROP CONSTRAINT tblvmeasurementmetacache_vmeasurementid_fkey;
       public       postgres    false    231    306    5310            �           2606    56442 T   tblvmeasurementreadingnoteresult tblvmeasurementreadingnoteresult_readingnoteid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tblvmeasurementreadingnoteresult
    ADD CONSTRAINT tblvmeasurementreadingnoteresult_readingnoteid_fkey FOREIGN KEY (readingnoteid) REFERENCES public.tlkpreadingnote(readingnoteid) ON UPDATE CASCADE ON DELETE CASCADE;
 ~   ALTER TABLE ONLY public.tblvmeasurementreadingnoteresult DROP CONSTRAINT tblvmeasurementreadingnoteresult_readingnoteid_fkey;
       public       postgres    false    5141    312    230            8           2606    56447 4   tlkpreadingnote tlkpreadingnote_parentreadingid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tlkpreadingnote
    ADD CONSTRAINT tlkpreadingnote_parentreadingid_fkey FOREIGN KEY (parentreadingid) REFERENCES public.tblreading(readingid) ON UPDATE CASCADE ON DELETE CASCADE;
 ^   ALTER TABLE ONLY public.tlkpreadingnote DROP CONSTRAINT tlkpreadingnote_parentreadingid_fkey;
       public       postgres    false    230    5234    273            9           2606    56452 A   tlkpreadingnote tlkpreadingnote_parentvmrelyearreadingnoteid_fkey    FK CONSTRAINT       ALTER TABLE ONLY public.tlkpreadingnote
    ADD CONSTRAINT tlkpreadingnote_parentvmrelyearreadingnoteid_fkey FOREIGN KEY (parentvmrelyearreadingnoteid) REFERENCES public.tblvmeasurementrelyearreadingnote(relyearreadingnoteid) ON UPDATE CASCADE ON DELETE CASCADE;
 k   ALTER TABLE ONLY public.tlkpreadingnote DROP CONSTRAINT tlkpreadingnote_parentvmrelyearreadingnoteid_fkey;
       public       postgres    false    315    5320    230            �           2606    56457 3   tlkpuserdefinedfield tlkpuserdefinedfields-datatype    FK CONSTRAINT     �   ALTER TABLE ONLY public.tlkpuserdefinedfield
    ADD CONSTRAINT "tlkpuserdefinedfields-datatype" FOREIGN KEY (datatype) REFERENCES public.tlkpdatatype(datatype);
 _   ALTER TABLE ONLY public.tlkpuserdefinedfield DROP CONSTRAINT "tlkpuserdefinedfields-datatype";
       public       tellervo    false    369    327    5336            e      x������ � �      f      x������ � �      g      x������ � �      h      x������ � �      D      x������ � �      i   Y   x��˻�  �:L��%@���`i[-_7��w�c��+J�U��C�2먵tI�^�]� <� U�`�ZH���R
����ٜ1�JT      j   V  x��TKs�0>K�b�����[H˅R��������D�$�Ϳ��NB�.ew��ۧ�YZQ#��U�Y۴���
��7��P~ŀ
�;k��`f�0��;G�*[��8���"�ܱ&��C��}�����:/�i6e���R����&��n�yqʞ���cl2���y>���5��ƞ���c�k��*dcN�?���(��uFU�.6Ϋ���**g�RD�]�!�5\�5���NhX6Xm�
��Ɍ�7�	+Gp�D��A��(a��޹^\0g�v���?[�ۥVhc��WW�+�@T�6����L�����+�(��h=�s��)#IU�" ���2@tP"<
�$�z"U"��.!�?��O�kF
��m�]��X�6�H��Hٮ��W���e����Pv9�����>9-ѿ\�!!���P��0#���Gɋ�E4�doR�w��9���0�T�������rS/f�ך
vw�G�jЈ����S�tU�Im���P��H^��Na�K���-�t����Ӿ5�y���,m�0������!�����4�G�!���T��o��)����
�[Zd���۠����qΟ?R�j      l      x������ � �      n   y   x�M̱�0@�Z�"�,@x�L���q����ݧ�m��Jg�8���ZA�Ŕ�ӷDc$t��Ex�]2Ssq@k9�po�[���$�L�B���%�Q�����]zʪ)>�q�>z�_��&G      r      x������ � �      _   |   x�����0D��)� с��3D'ȏq��G����l�Cw��k.V�J�R'���uo5gw�"!6B%N7�*eeY`%�ʏ���&w���H�B�W͵=Qټ����c�/?�u�m�1�5�      v      x������ � �      x   N   x�ȱ� �Z�����ΒFM����C(Ü�@RQ!qB��G��7���vl?�ިFUuS<k��)mo��7�?��      y      x������ � �      a   h   x�U�1
�0 ��~ �����ED]JM ��Wq�J����}"e��C(��Ta��3��z����Ѹ.�qH`�ar�|(o�_��J����a��Z{f7C      ]   �   x����m� ��3�b4��("�{M�%�H�d�J�XH^�� 7��;y׃��i���D;��i$0c��$'�����-9*C�x��j⹐$Q|���#�(c��Z���4�w�9޷Q���]Z�_� ����I�β���C����)�T�Ea��|�WB�,Ҙ3����^I��m��Y)���<�+)��,�;��>      Z   �   x�m��
1E��+����<&I���h�M�&`�6��Y�\��9a��9Y�[	h�bӸ��|]�:�̭=7s}lM�'53<q"XΈ2E�8brf�HɌl�|L�].;��o����U�d��35�#�PMp$�.�%�e�Z���2�      |      x������ � �      ~      x������ � �            x������ � �      �   Z   x��ɱ� ��p�3��CXX�@rt��&�{-�R�8�҇��"��fbp������ �B�h�]��J���}���c� ]�!|      �      x������ � �      b   u   x�����0k{�_@%[����	�؊��I� ��U�܏�=Mb�J��$u���da���Mn�it&;�ݭ��=|���ЈӇ�f�R�\� ���Eа�n�^��� nK����'�      �      x������ � �      �   l  x�U����0Ϧ�?ɒi�W����x��!C�ll۰q�y�����6zõ1}�l���'7Vcn\���n\�l���0��c5��A�dg�3�M�E��A�V��9�����_g�V�n�]��������ټ��|���Yr_y��R48���~�ei�ti<]�Q����9�a��3��f��	��Bkaa^(+~a"�&��
�!C�r|�	Y2̔&�Ly2����0S���3F0̜#f����-^�����ä�$¤�^a�1�bJ�aR��0)K�Ii2Lʓa�w3�Tof�)Z�(SZ�Xʂ����1�T6S}�0L��f=�P\Rd�%�W�%K�Y�d�%O�����U1�      �      x������ � �      �      x������ � �      �      x������ � �      �      x��]Y�۶�~v~GS3bc_t�}�c�%U������� �͉��PTl��9 ���fK��X�JZ$ b�pp�������|5M�t���U�#��Q�*;����t�ۇ����g���vҸ�����S�'�tt�4ד��O�>���鬾8��KweN���W�[�g׮�(�i���*�yZV��*���-b��I{wU�����o���ԕOIJFg�����p�&i�\��Q5��%'gߟl���$�B�*�H�"�1�cL�'DNM�FJ�1��|���ɪ����������Շ�$����7���4y��������C�_��vV�����-�7�~=-]մW�ߒf�T��~�q&�C7������#���..\��M��j.���~|����Ʒ�sg���.ʪ�k�%�{iM���Օ��b&���mar:ʘuN"q�N�T6J�˻�`�,E��RN��E��pVQ��%�f~y:Җ;��96/�� )1����NA��ӑb�1����[��D��p8e+��U�l.�v���K��k&��Z^S��3B�X�j�
ŵ����`];�����60fS�n�IMRBP���-�ܙ�^�7�?1���'�ku�s��6��Y���=;]���e9�k���V��ՒN��߰(]�2%0��4��tT�Gɟf��B�~{�v%�n>yG��I�f2>�m�"Q�
AaMC^Q�%�HrD�V瑎;۩Y��<�s�r�����d�F�ʉ`�Q�a��S[hn��
k��X�Yg��uTS��43���(�2�X�Y�b!vV� 
	_�S�0�V��'H��p�q|��V(�	��0�Y(��Բb��v��gO��%�/��]�9,�|#�D"˄����g�B0�3nVbQ�8�DSM,VP FN��Eb%�|J�v�-6��2u(���-�g�y����?��ϣ�$- lq�C��"��_��Ѯ�$
8hЫ=0оK�����K,y�6ܛ�.G�(ۙD��A{*[�	d�(�Ù��7௔-�l�)Q؀�P��
���*2�mdWʖ��bVdF�%w�ؚ,s��|e�G�ڑݔ��cyP��vt�y����$B��C�[�"l�IcVy��j�,�^93�v�;���U�@��hט\�1vbl�-Ƹ0@]�I���"�%�O0�p�2�$[b�z��B�C�1��N)�f9���W�h�M��PXD	��6}�KߌH1���Di!�J7��
4$����%qm\�͵�df�qDq�2C�4g>D��ƅ#عLj��U�h�g�RL	/p��R�J �w���� BatHm\<_�t^#�-�#4R�Y_/��\�Ca��j��V�ht�Δ��PA��p��j{e�s`g�M�)z����	Q�"��=*B�v�Ÿ \�qn
f�~�u����VR-r�[8f0/��*k���lt�@)��0�"t�$l�f�#������q(׍�'��Z��i���@A{��R��۾I�S�nDj���3PОb�r��!8C�K�A�F�aJ��,��B��.38��q+�H]a0+,���B3�mF��<״�dCAiɸ�Xl���f�^,nIA��`�[;V
���� ��/��L�����*�~�yr@�b�t~��ENƄh��x�-AcVY�5�����|t���z��n+�ђȼoLQ����m]^7��qU^ϒ�ɒ����*O@'�G�g�ρ���̨݅?����t4J.�3n����Am�K0�`�b�T��-4B�}`<����W���_/jO�ϫ��1a�ϵ`A;Lh���0+�
�Hf������x=4�)J����$�1�=XN1�%h"5�2�rB��TH)Ik]$[�	0 ˝��x2��>Z�x/5��z�Ώg(�Ŷ �o���N:���'H��;��K��Wy_6���[������o��i�Z����p�~�Q-�[V�p��K���M��L`ۥ�&{=ˡ��?�^��f������O��*�):$�Eh�ܿ�V���RMp�ԀE봥��J�Z�-�oIi_`7"����2�0z�|���ȈuNY���FZ23�*m&��ҙF�����	��ZY �Y�8�lƬ�o3R
?���Q�v3v�PR�C&�1��	p�y�Au��1G	�����Ȱ��X��옢P:�Q�N��Y���%��6,8K�kO�M�Ą{3՗�Qv���%�v�ͼՋ��4@c��s��?3�BFS��Ba�p0��3��e_�<m����.����*���cQ���]{弻25���}[�UkW��h�"�-�_s��1^���N��ݛw���0_@�zَ���F�G�塆����P=��K�O7�j��V"h���0�O��LuO���b��m3Ֆc@9�<� k���e�t�H����]:� d<�Bj�+�G}�vvu]�KW�A]z
�ssu=�o
�*%����:�ҽ�W!��WÔȰ����q+Iwl���?��	�u���w�]�~b\>��V�Kc>Ϫ��_��Lg�̊�mY�伪f���t��i	�%�{o4�)��G�g��`N6�/��z�S�(:y�G���R�>r�/���5�1�w��vR���h!��B�/p1��(v����������xGޞ�)�е��a��s���ߌ�C�����E�&�v<�}c�żo�nn�9�39���R��Da�����Wv�&/&wew��D7l:�YMn����(�V(��{��-�}b�Ę�mJ�У�m�rB�z	�w��d"^�L�O�MO�P��S���9f*�t�a��IΟ#T�8J9�&z��g/.� ���j�����}>�|Q;�ܠMAX8Oκ��E����ay�� ��*uڀI��_\�jwsɷm��P�O���%�=4U9o�/go?���/p�"�f���������Hwi+cT��ʢtu�ϮLYm,:��>�d�u�ݬ��+�-��l�!>���j��j���O @x��|�޺��+�!�o�3��y�mmK�mcۖ���TXV]��d �h�~cg���x=���'R�o�
�m���e���
��."tr~����屳�2-�b�c:'��nd!�,��
g=Ka��N��z_�ِ�Kx���Ё��&֛��6 ���<��1����O���lEf�ZGz��&꿽GZՒ�K�H��@R��?�+�u�}a�e�GC@��Ake2^�ضHZZ�o�?����X�=At�i]�"ET��O�xg1��~k��С�2�"\�R#v����e��ऴ�4l8��8jb
��J)样��g���m�:WJ킒�������N%���d�>��O����M:l[�\G�8�aO9[ �x�ǚ8���.�a;&T����N�� (��agͪx_��ep�C ��v��.�?��{&8���Н���o}�m�qy�9.g?W�ԥ�AvN9{�w�փ΋�Z�Q�i!$���x}[�O�wI��ACp������SL@��'�3�h�'b�Q
0�a)�g7�����;l�Ͱ�a��RqS��٨�����̶�owu�����o��'�����	�L��,��W�sH�� �h7�N����I�!�wm_�B���q.�-#��|I3d�`��˴V����i1�W��@��O�����K�}�4�S&$
;�K��f���������1�}��]zR�\�G��)\p"���:Bg��E�u0��G���<R��<����J3���0Z��ּ�|�c׀��K�}�:w��<��-=������Ϡ�U���zp8#e<�X���*=����2�j¨[�E�o�#Z (}�� ���abp��|Q�G�{���ӻ�&T����.\��	�)AT���B�"�F
:�JS���(B��c���u���Z���Ӱ�i6�_��)ָ��7ծ��2�r3�����-��%Y���������B����¢��0�xr��u���*��yrg�v�RD��q!�V�0����ؖR�aS��$�[
�lL`Ɣ��Tl zO&��]���b��@�+�D�`X�7˯!-q��s���4��3{YϪ�t�ޜ���������� �  R����k.a�vn��%X�M����
�Fu vY������A�_�Z|~�g]�l�����2o.ovQ���񖶞]��v�>��>e��s�����<����;�L��a$\���31��o�,-]9A���Ʊb,ec[0Q0�,ulg��T2	�6V�_X)��8�(@�"t��<A4��t�Fz�ĊT���>J�"t���,SX	\�D�*Rp&�~O������c����r�msŃ_+�h(8T"�x��qQ��3��y�X��s���@#�����78BoQ�p�>��8#4=�	G�2���1�к]�*�V�Ě�c��'��5%I�T��_�p���z;��py2[4}F")fz3������ϊ�x9w���w_�1���3k�$,sY|��(�-�NGk���V�`VY,�(�/�fL�R;#	δtYa�����q�$%�f~y:�����d�Y���Q��(7Zr@���q:�1�˸�*����
����
�e�|�[�����i��J}6�#4����T��$���E�^_�Ӽv>q�^܏$<��+|Z�$��#��1���M�6^Mۃp���6a�2��J�e*�R����>��tJ����=�7��v�As�)̂��`X;��㛘c�RL� aʹ�zv|r��7���*L�.BYrΧR�y���0��ߩpr�\�i���Ф�m���7˪[�퉬�]����&�*�i��o��v�zN�m�K4��R� �v$���A�r�n��v���JS�	Ga��=�����1�|i�~K��Ik�'L�܍0b����=��/��o*w��\�� #���t��I <�S�Fao�M::�}�9@�A:���tt>{�G���U'B�ؤc��{��cBT�6L:�&����i�(M"�C�=��M�=��7e�f���$�`dt�0�)���܃��� ��4B+�v	��K$5�:����A�7	�L(���I�3��P�p!]Q�J0�[Ȃ�[�1�\Ye�<�/LZ����)ƀ<��H��L��P5��:0ݽ̓l�;��<��'�Kz2���.+���=���R!�-���E�@;�7�M�%��p�����EP����64t�K-�@�u={^����WW�u��D��~uta�$Rv,��{��H&�	�Y�1�E����TX���~��w�.�V      c   �   x�m���0��]@�$K��_��X��C�����G$p$蜋cОM�N�O�1r��=<CwVC�ߚL���rv���W`$4�� l\��I�`Y������nUŲ^t���~�y���ZιF4ݬv�x�1ʝ��^�-���5�      �   H   x����0�3SL��c;���:�>�FB E�Bucn�K<Q�m��d�b�����W�݌Sh9��3|�ݔp      �      x������ � �      `   t   x�E�A
1 ��+�A@=z�6���J�
�{+�{����T��͑����*�n�%N=�wpI�Nb�)�)���
U~t�ed���"���>`d?�x��p�f���_�6���0K      �      x������ � �      �      x������ � �      �   ]   x�ʹ�0 ���%(��,�i��<�#q��8��\2��,Qm�{Ƀ:y�	ֿ������$���,�L���2�
bSSN��낈Fc
      �   6   x�3�4�4M3KL�HK�M�41�512JֵLIJ�53N40MJ�0��0����� �
�      �      x������ � �      �   A   x�3�t�,JM˯�4�3�2�NLK,�sL8�S����l3ΐԜ�Ԣ�|��`O����W� -:�      �      x������ � �      �      x������ � �      �   �  x�}�[n�0�o{ހ�y�C��@!�NZ������R�c��L��#����봟.��~�}L�� 	SI��_�ꀼIG�Ȝ� eK��/���t��S���b�Ob�X���S�)O�eQ�<��}lsQ�ٖ���V�r��Z`�-��lk`��e3��"�5�U��~�A&评n���J�It��Ib�A�M��,�"�5��ܭn����ix� .�+g�Z��'��=�-�y@ء��G�D�
�#�� z���r�l�u��>�VdI1�h��� ��^��:R;rQ�^v;��!����:vK��1��׏�t�^�?ϧ�W��\j����h�z�p�eU�	��p
pN�� n#�>��� #�- �B�[�f�>fH�Rs��a����ۈ��t>�f��M\Ӝ��B-3��Z��\K�e� k�O��p[�V��1�Rd�+�Q��r�
+��-�Ln�����?����      �      x������ � �      �     x����i�0���]@A�lg�Npd���:�h��H��~�xP�ƘZu�@��I�6���ؤ�@A��}����H
X��p��xME0���cŬ褐�|�Ѡ�h���:�8jYK��K�[�^xh:�nm��B���*_P4 |#�0n\VeVLw�E� �O�Z��^���$e'5K�ާ�j��I~����T����`��{N~m>��!^0�7ĕR����<���'�
�|��p���'�*�ФH.Ϡ��u�m]��^F�T      ^   M   x�ʹ�@ �z��=�^L��7^��K�,�Hk�,s��5�\��
g�&��\���d�,����x�����      �      x������ � �      \   q   x�}�1�0Eg|*0�8c��'�B(�:��R��K�����׳UZB�\���Cch��
L��0����_<?����td��@�JnBڱ�W�����E,ϵ�����G)��$�      �      x������ � �      d   �  x���[��:E�+sQ�)���A~,?�?��]�e�6��$����Z���>��,�t6-����˷r��N~U��4�����������������+ap���3�/�_�b�����_ؾ�b����7a�`����<^�З�c_�**��2�j&�f�򌿢	(�L�j��/��+�����6��&�n���I�h��ک��m�y����:����ڽ�m���18l�8�m���a�08l�2�mЊ�aT08l�*�mPP���J�AA͒6((Z�UK��@ْ64P��m	'�q�<K���m�6
ϭ;�1MθM08lCS���mh��aZ���18lC����6cp�K�`�dq�,i���%m0P��ʖ��@ݒ6����q���m͎����^�����W܆N��3�m���6t��]18lCo���m���6t��T,i����mpP������AՒ68([�uK������go\�ཨ���Y�b�6vm�F������6����08l�`�0*�m��a�bp؆�08n� K�0P��6P��-i�Z&P��L�qI��#�DI��Zyٻ������ڷ��yp������atX&��0L��F09F���[V�����L�zAt�	^o"��C�ʖ���-N3(\�
fP�,J�łA��X0(^��>���M=�h犍\T\v+� 9.�N[M�cQ��XԊ�q,�`t��Ǣ6��cQ��XԎ�q,�ct�:0:����e�T�$
��B@�X(]��b!�xY,���۶�}}B)Z}�qN)K���<O�=��8FǱ���q,�0:��2FǱЊ�q,T0:��*FǱІ�q,�0:����e�PT�$

�Ţ��e�h�tY,�]���Ţ�gW�N��bӨ��ś��t���s}@�c�FǱh��q,Z��8�1:�E����Xct�����X([CuKba�pY,T.����e�0P�,/��}�����R�<�Z&�e�q��l;�>89������X����>x@�8��BǱ��:��}���q,����cq> t�����X8([GuKb�pY,T.����e�pP�,�����s�m�^x�d�m[|�r�v�k�FG�1�FǱ��q,ct�Q1:�Ū/DǱ��q,F��8�0:�����b��e��nq,*��%��*�Ģ(]�J�vI,*��%���>��ek���Q����Z�T�wt�>W��XT2�Nn��ߟM�����a(B�wh/�!:�E{���=��d��o�^Ct|�����۴�-�O�QݒX0(\��b��tY,*�]�
���BƇ�mo�I��,�T6>���q���unw,�G��%�_�?�(cx}�<Z1\^��
���Q����s�a���G5�/��gJ:���Q���d)hݪ�����]C�{��x�_��c^�[�����^��|���@��e�?�5�>~��~�>��賗k����z����S����f���#�u��GFs��GF���̀�xdct<2�bt<2L0:yL1:�0:ʖ�CuKF���e#�@岑�A鲑�A����A񲑱��[��Vi���+c-��(�e�\3b�'#c]3!<�	���X�LOFƺfBx22�w�������u���dd��%�'#c]-!<Z��G�KF���e#�A�ґ�{��pP�ld8h_62�Zy>��v��E��+ͳ�m�3v�N|�dd��xd����ȸ~:C1:�at<2�at<2F��xd��xd�����eKF��[��9(\��9��e�(]��9��e��(^��9-��ﴗ��V��z,��q���'#C��@x<2�~���!K*�G�,� <����xd�2
��!K(�G�,� <�t��xd��e#C�. x�Ȑ
�����{�Ȑ
ʗ���}�����?���G�      �      x������ � �      �   �  x��WA�[7\;���"%Q9DO��HJ�?B�H-�ߢ�������p�#)�StҔad�L�뛪�q��6��g4�iԷ9�v�Q^�6�{y7�W�o�bUP���iR:^�%̂��҉�~�ix��Y{#��q���{��]��9iK*U˭�Ƙ/ij�_���x����f=�&7�A��hw�b���������#&��Ev�Ҷm4��3�r����:��$��s�b��}���4���hJ���˴�M��Gd��іΨ3/�YG'GR�Dk�o���q��6�ؙ70�M�)$��C������,��K%�|�h��Yw�[_�����)k7#V?d ^�U(t׆���jc��JW/��9h��I����\��ih�8M���n[_�&��Z���k�ݎ�#�h�npM�T�֕{��ЎSvG.êl��k::YR����m��s滑@Q'G����yy�9��ihu��s�H���K�='p/��h�=���w�[`��;ú���OC[~^sO5���ha��Uvٶ�y[s��)�8��ʎ=��:=�|I��<�{����� w�O���k���?�=�m����)���c5'8U����ɣ�5�N�����c��+E�����z'�L���%�i07p>�����@�5E�Z�LC����E�����ЩI�v12$��O!Xd�g����Q0�v���`L����U�A3����05��K�)�S凼�����\Ý�S�j�H��`��6���O����5a�7w  ��B�/�v�Ӹ�!�2Ł��;9&&�=�������ƘX�=����-�h���%�i����]�s�o�9J�#�;ǆ��V<V �����pʆ�9PO��iܶ���	��ާT����Z�\g��3�����\F:����B���`��8�>�ۓVtc� Q���Pe���.Z�3��~|���O���      �      x������ � �      �   M   x�3�LL*N�+�2���K��-�I-I�2���/QH,(��LNL�I�2�,����/��2��v.-.�ύ�2�k����� �+      �   Z   x�3��v.-.�ύ�2�t*�ϫJUHLO�2��r�LcN�ʪļ�̼T.N���Ԣ<.S΀�ԌL�΢�d.3NӜ3(?71�+F��� k#�      �   p   x��;B!�z�*X���+��Z�0y�0İ{_{r�t?�f��Ċ.�Lt`M�1�x���5Y��+�T=�l�fʖ&�o��y�3t�锴��\�{���&�c�u{%�      �   y   x�e̽
1 �9y�>�x�;�*��K�F-��H�ҷ������u��x�OJ��C6�|�59�lS�70��Jmd'�2���Ť��~�%��2��=�Rq�D�����r����n	0      �   ,   x��(�*.)��K�(���+Qi9��`FR~~NjbW� Ou=      �   @   x�3�,����/��2�L�HL.�2�L,((ʯ��M,Iͩ�2�LL+I-�2�LJM�/J����� D@      �   W   x�E��	�0 �s2E'���%m"J
1*�^o�>���V�P;�]`
��j��"�}�\*�>�Y��;n����/���'D� f�#�      �      x�3���ON���/.������� A�h      �      x������ � �      �     x���]n� ǟ�)|��Y��w�m4��JLF��H�fK�I������{�/%K��U���0S+>�z��y2� �'3��(:	�ɳ���%%¤�/}���*'N3�>��;W�[R����C�8���?>8yko��p"�a���uBL#�SG����8I�^�s�ΕhV�L��Y ��i���/��uЬaF���K�X�����<Xz��ڂh����(��x�W��:%Ҧ�A�I+����Wf�Ӆ:{��W?�_B�檉�մΏ;��#�pX�=)�>zE��      �      x�m�K��Hn�ǪUp�s*]"������أv(��XI�*>R�5�6���w���DHyۓ{E"^���w�6wݮ����eh�H���Ǐqz����/ӲB�v�ҵ7��_�u[�������z�����?���V?5��p� y���u��W�z��J�ߵgr
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
p���y$ȡJ<`�C/�����ҏ�Faɬ9���e9�K��A1����տ�9�&�J�6�-슟�����zv�8      �   ~   x�Mλ
1��zλ({WKe��&���������L��p��te��ջ��I�[5hK��Jv�K�J�Ѕ�I������ܱ����/�e�h�
f�QWtx?�y.�AE��������O67      �   �   x�e�91Ek�.'b� c���&��8��Y$4�_��K����3���M�e����$I�������U�'v���U��|���kO>XW��=��>�Y�#%�B�5	��4�H��ytZ �g
>      �   :   x�3��v.-.�ύ�2�,��KW(�L)��2�LM,ʩ,��O��s�$��"	��qqq ���      �   ^   x�=˹�0 �ڞ� �(,0)qP�0?����Z��j�ӊ$a�%�Iwd?rI��źS5�p���(8@Vۊ���p�'X�Hb��� �
�"}      �      x�m�K��Jn�ǥUp�s*}E#H��=iϺ{��(����#u������x%� RV{R (�x�_��-�t�۷���������Q�w��S�ݟ.�|�|˿�ݿMs��4���0��^ֹ�^���6���®}{�>!U*e}��Vð��n\�4�����=�nΎ݁���]�/CG�ص�������;HQ�k��q�����ߵ��2ɣ���k�4T�0>E[mh�.��w�\R��\��mn�<���-���_�eh�,zV��2-�|[��@1����{�����;�b��Z�y��yш��-����Q��n��C=�����mj�#��
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
�]&i��L�8ӄ��^VQM�f�K�M�`YS*����M�բk�&��VV�U1'���� L�v,߀%w��K����(��f���� I Һ������4�� mE��1��"�A=6�AU�&�P%�7`�]������ү�F0y�W׻�� ˹�d�;(�����l�O1A��IGFc�&���H��nôB�X�w���n����      �   &   x�3�,(J-N�+�2�LL3�9K�����b���� �`
      �   �   x�U�I�0E��)rS�ҍIMj)u*�)���b��=��qB*��O�wGx6�&R6L������t�3̭r�X�i�I��d1���;d�ܭ��и��+ԉ��{7��2z.�UWwx�2F�����b~g;w�a _�8d      �   �   x�EOK� ]wN�h�W���F �P�p{i*�n�7o�'9�e����@X�0U܃�d�*� $*=�Į�	^iI���K���9��ȕ@���4��׿����t-���(̾�:˸�Z$>��B�y�b��rdQ��k�/�l�G��1��>l3<� ���Zn      [   �  x�}����0E㞯Phl!ބ��ٛ9�D#5�Z�D�QS��fG��*"�=��m	Z�0H�D���"Pxy^��:��nW��'/�6Z$F��O�s*R�z�Z+�"�E��Ph%8��Ĝ�Dx�l*W�����A� R�{sM5�0ӈd�J�zB��؆�r>;��]*���T��0!�%�M��9��<�����@a�!��,����û��Ē���aq��ޔ��"����ԫ�̌���,�x4
X��چT��k&�M�eŊ�4�(�";׼�������U��/��rfHFf>b9^	th&����_�߯�u�#���R���/�C����EW�V�LL�8B�~��r[q�-��J+���{�F=�[���[d�m�F�I;MR�p�w��j��aSĺm�G���H��#h�A]���\����g������t:�� ,�w�      �   ^   x�340��+(J-(HM�240����9}S�K��N��0˔3 ��$31'�R!*f4�6���rR�\ΐ�|���r��̼�b�=... @^"�      �   r   x�3�NM.����2�t�H,J�O��2�t�/J�2��v.-.�ύ�2�����/��2����2���/��2ҕřɉ9
��E�y%\��ɉy\���E%�@�b�1z\\\ �o"%      �   ;   x�3�JML�2�-HI,I�2�tI�I2�9��RA"f�~�
�E���ř�y\1z\\\ �"�      �      x���Ͳ�H�&�>|
�Ǝ�n�?K�d�*S��H�̾m�Iu@���v�Mzy{1���U�:_�~���̾fe����` <�����#�����_��>4e{*+�e�%A�IS|��n����Tޅ������r�7�]�H���6gl)��x/һ,Xs��u�I�]��I�����U|����,4h�Ӗe٬����i�Ea�bڗ�����7ݰm������a�cׯ�y�p�K��E3�U��}U.��=�%�g�+x��Ǻ�K_m�[����Q�!p�˳��V}�>U�	��y���˗�]}2v�q�����aW6][���n�t-ѱ}C8G��ׄ޿lO]�V�����;׻���e����}���t�C׮����?u���|W��恳��n6ժ��wi�ɜ�ѾlۺlK°~�(
�_n ��au<�7�1�9���˶�'`���Ou{�����#x��6���C�T-F�s��<�����ıKA|�F3��K�?�o�:���j��z��˷�����9�\�����p���Zt��}��V]�A/�2�ל�HVUV�W}y�z|���?�^p}�|G��k��v`6f�`c��꽀u1W��k�T���o�g�E|���?�7�b���+���+N�t
�Ot��.ݗ۶~�*�?�}��\��zQj$s6��X:B���@�K�2i_��Zx��/W\vp�1ͯ����vwQ���(��l>�-2VQ{������.��V".�|;�؎}�T�ᇇ�Y�:�/&<p��9�a_o89X����g�O�fا�w7��w_�*��"��$�$���j�1YU_.u=ُ3d_�������(�)��ӫ��WM���#x��О�\?�w͉��sW�DĶL�8�kH6�(�����;D����Ͻ����s�A7l���dg�Ü{�zU�"I+2�5i<�����Q`��{�*��ќC�8~�s�p�Fv�����z<H� � �s����[$�]
��~,Wr�&�"��`����q��I��
���.�uy��h�\E�!�5�Pź*�?>�*�yI�����9����$�7�?�	2�a��	N]�$

���\�Gs�?�r�Q���R�x�Wm��%�~�đ�˧��b�ǶX9�ƚ׹)ip�f�&�B��H�;�A��I�r]����c�>ܥ�
��\ ���Vۡ�t+(��n��/8����C��3h���v��ɀ���XAP�0b3�������ù��^b�Ű���=���	�}�3J���y���<`���!epY�u����{_bN�s�3�����U�ǪV�eh��
�8M�m���C�.\oyQn*ȝۗ���$>*�t�$p6��^~ �6�8��\Z�ס�Bu-������x����t�aUI�p~*�M �%�PȆF��m�=Q6��[&[d\�>�v���_q`�icq*��F��8*6嬳b����P=�+��x��X�Tz^��' 2mϼ��G,�q����qVG|�p���E�*������ܕ.�<Z@&� n����"���aT��jq�uY>RlC|��#�I���(�!;�`����~��U�U�W�.�	�'����!9�x��0�my�Ah��"��<��^�8ߺ,ȇ��Pe�./8X�<qAqE�/׽������Mq��	;hb�-Q�b�_���������A�b�{�#�4�}WrE�w+|�^C�oR����\%}��~����/�O5�a5���B>��	��ִ1wX=E�(D`��u	r�v�L'(�����ZdP1����N�.�u�����q���ZQg���,+��`�XQ�ǵ���qP1N*t���
GL�F��2��������]�\u�ן�[?@%)e[!�Ą�΅'
I�C�b��E.8C�LX"�6.��p�5~4����w��^h�tN������:@ǂ&9z!	IDq�#5��}]�8fT����խ*�bCwU�����O�`��Sa����J^CL�8�IQ(E�)�Ά���V�u8����#7�Ü������=
���7T��+��q}�����
*�f�g�͆.q^k9�����Xm������������7�'��2�.���.�}�=��3*|�uCM����Ҵ�ԽSI<����@����W���@��k|!�X���`]�/TlL���B =a��>!��n�L}*;\��F�	U�؅3�q��/��٧���h�с�/�F9��4%14R��\�U�s�9d�-��WX������J�<�I/��}�.ZXƿ<,�|���\(s0']m���Z�Dy	��5�^�m���]u��'�'�(4h��v�}"�y���~h��[[�<l����j�ކ�����iH�C���\۔����di�K0d����� ;Ȁ��.�y�Cؠn��<����QBY����İ�qD*qȭц�o��@g
�Nվ>bm���1��i��3by�ZQUd�7��'�y��5D_u<z��K6�p*)?x������G�3���z��87�`UHo�ɫz 2v��2Ln���������U�p�}�������ܮ
u��z{M]^a�P��d�~O��U��;��6���]�~�r0ZZ<�3�_ ��Y��_��Y�c
����u�8'�,fY��@w��&ZW�W]{����2XnYCZ٩��&��0�s��zU��V����w��'��1f,N�&��i*l�6��>��
�ʔ
�ʌ
�Xr��C���a"��۾�j��*�E�cpF���^��9�A���ṯ��;���̣�
�Hڪ+���D1����P��7�&	v���؝v��{55�T�M�}�������J�_�+����]���Q>�f�Ao�xNё�ph��!� I>����_�s���Ϡ)Yx{S��o46CZx�6qֺ\W�JY���.~��+�Xwǉ7"�I��E(���ܹ�3��1�8߼�:s�4,�R��FK1�������OE���h��VM�I)D�+�T!�7ʔ�*M�i���"�6BN�UU���'��n�&dP罈}���Sj���������0*J��la�O�r�����j��Y��F�v�e���$5�6�?<�8uY<��a���6�V�@7�,7t�������5лx[f��p~�b�0J��(��\۔S\|��)娌�,x78d�˨�PF��@-WC:���@s���Ч���@�rW�����P�偉�6�|E$����7�:��	�TJ#���Wqg?]H�G��|l���9����d�%?lA�+��6� �6R�X�3[��NT�.E�;ȚK"�R��$�5�d���"�m7o���L��R�p�ttTd���EJ�q<�܈�J_��E�D�QF7(�s�e������*��(�A{,O��p���ػR�-1p�|�)��g
�Fà��:9�#x(��p*�g�*(����@η��]�~/�MՋk�wm�@�h&�����u4�)�і* �F\�)�gY� Z�eۉ����74�P� �JH����`�`�
蹗���qe:[��H�S�� 
H�#
:^�L����/�Mכ�� ���������*x؉��2����~�����Cm��~y���t�	Ȭ�GX[
lsq���ι ���/fz�w���裐Ѻ��l�nSr�~�����T]�]�6�r���d9�uG ���fo����<��2υ3�Ų)1�-�ج�&����+|�^��@�❇�r$� ����dFzh�ks;����|U�S�Dgt��<�����OR��z��M�X��\N+߾�����X��#}}x� �'�8Ρ����6�5{�I��� ������E�ǹ)d��Z�^�w� C�9H:[�H&���@��}�����$��?>�{��:��O/"qR�����7��o�Q:���}��S_>��d�t<�!JX$���Q୮ܠ��|��=&Q���.mht�\)a�ܦlʯG/pq���ߍZ�Ώ�yw�c� J
L�"�%h�v��ɻ�G    �ǲ�{�bFފ�į�(���ٷݭN���z�١CCO�'}`[���_��P�?��{VsW�a�����'��-o�?t�mծ|w"/ �w�Y D!$z��#.(]U��� ���Ia/87������~=�hE��̨����w�~<$�PT�B��ߪ~U�12������aէ]�Wߞ����3��*�]���=��uw�{���D̽o�u׏Ǣ�,y�����p"�B��gU1�4s�G�I���0\B!�Z���^7rB� i�9&|<�WS����f�W)���Q��:,�,�
���e���@k���Q�� ��u��|E;�(�K��`�����������8�S����8��	j�
r���v�����Ӻ;p���NG����p��U/>*��ؼ������{y�8��ϻ�������_@'1�Qv6���dvI��-`G(̝K���?�U�b�b����$7H6��w��Rp&aQ�D<n����%?��zy�݁E�En�\�����:��2�_|_�F��_�h;|p�'�����|����vi���a���'���c��j2{}�'n<�D�ʉwY'r"1�*q7>k�>�ҏ���a�9������V
���}��+���6̂T]����.�n�H�ù�盗���[]w]���|ބ����{Iů�ZP�ݛr�?��b r�h��(@7�,7�� ���z�ÜJx�@��0�"4�HR���4���b״���&�ƛ��D�|3��뗺����!��(�������	<}��K�PIr?D��]���RU�s�䬝�������f94���⒲��-Q�Mln����=����Ο$��!�}JU#��n��c��lb�L���#�%���S#��Ԏ���c����[���	Ҕ�����AR��������%vk�[�»O~�i�>F��%-����їBBs�x_Įz����[3Gƒņrg o嶵jy��#qVr�$�b����Ƃ�Jm �"C���z�̟��Ö�a2|A �Z�_��Z�$���O��Y�8^�BH��~��7F�E�u�և��5:;cpe��L1���֘�(��ؒ+&��:}�8��0/y���#�h��1�z�����4�%�h��ڞk���c�y,��t;�����͝c�@�K!�j 07A0P�l6�]l��v-U�sL�5����-�M�T[�M�qV>Q{B�y轐�7�M�RO��[�1�E�/�vh����O{O��I|s�{��S9^&
�zW�Hi2�-l��W�[2o�AI
�W(ȍ�˶:b�Լp|��˛g�xB�x�o�c��ç1����r�t�o�A��3IG"�%�o�`UÆm�'L}?o^�̨�R���c���#&�5��#�i��Z3-�`G���9xٵ1ǅ�0(A�Y6'�<:�����r#H��'��;�y�c0˽��c}��wϦ!�{�@`(7.o�k���xJ��TB���;���Tg��ϻ�S��~_~S��a�:F9&�\����eI-u��ö��8��y����X#J�Ͳ�Hͯ��Ͽ��/�k�ރY��K��
����#0gD��B|M���4����yñ8F��D��h��c���ȝ�y]��S5�����F��}E��I(���{[0�7C]q�-�uv�^ �#4����W\H���%/�d1��z��P)b0I���n!�k0�|)ʍ�eS�4NK��F��G����Z�-%�	J����^<%S}*����`�=��]}�.r"���/���8���a�@�՗�_��C�s�{��f���Ʈ}��[��?�}������_��?��(�2�~]abNcPC�*�	��A�3�����^����M�-���Fsn 'm���P_ ��>�6�.�k�����h����0�N^��þl�(׾�K"oI�=q
._�;�%cc�@��Sџ�vG��r��ƪ���盗k|L��t$�H��ϐ�-����4x�p�ޚ��0M럇���e�u�3^��`��$����l�X�fU�]���[	��Y�v�(�v��Ǫ�_ٶ�/�(���tAQ,���C����I��t����ދ�J6��1�FUGI,�%�5�]ub���m��]��>- !o͆���[���H\c�t�F,IH�{��bz�ӎK-�� � ���Z�������9�;uJ�Pw�P5{����Kt��۳����6����n׻�w��p0
�h�x��}�b>�v�).Y%5�vz��{��l��ѿt{OR���V��-_0�����d�~�7ȏ��04S
&����[3r��5q�{$��h̓R��0�U�'=��qkMj~���1��"�~������`Ʉѭ�ui2*�'����T���o#	E��
*�>t�I܇ \оǉH ����U��/�숩6�8y<	~�����@Ϥ-:1�d,� �V/�b�ZB�������%>v{Yt���}���<�S�=I��we_��H�6�����-�����hbV2;,�F8�%�|s�o ����l���Zכ�;��Gt��T!/ǂ��X�v�hR��F��hF����w���z�2�޼�����P��s���9҂�L5ϥ����I��a���bSc!���gA>#���	GU1u�b�,��(q}q�{�������NU��T3����ntCU�zӝx�o��8�t���Xw	[��d�%G�t��C���K��hW���<b�����&���p��F������6�D8��:�nB���M�7��Ռ_���s��B̒�0�;�_�f[j^��7ɡ��A���c�ML�?�;��.�3@u���"���yWC�ܔ�U�߁ z�� ����C��/��c3��˱�T��9�C�G�X,ٙI(�oXȏ�5Sr7=O�L�P�drN	�]�����g��cbs���k�7����y*hS0h����:�3!�Sj/4�����<�k�-ĸ��ؾ�ƘghV���yT�S5T����ѝ���G�����	�GO�[�G�5~v�\�,J�Y��o�T�v����I2����#�^>�y��b�w�vα�:�$���Q�}��-ko��U�W68�S�9}E�!�[9��R�O��=���2�.����}�]��]1Q�4��U��V���*O�(�I�at���ݙ�0�؉�3T@n����}�%��J����h��`@}�x��盗XGv�.��{��/��P0�;���R��8�g�K���80\"�)4��W��q��-�d)�!�]P/2�5x�[����o
qI�qQ����I>���C+����(������B�J���vX��ڮ�㢃��<�At�	Q��3�rϨC��o�E��������>�I����깾�ӱ��7I�#?Z~�qN�ip�t�Q�F�*mt���U����B���n�����Z�TF�U���Fz���U�㥼=W��Xt��a�c]v�+���m����~��*�8n~������>��X$!o�\l�B��@���93��W��Ɖ�؜��h���B'��Y��P�.ʸb�5����f~m�O@W �TL��Ь��}{�����a �CN.?�&s;2�҉~z��`��/J ��ǡ>�J�Y��~�f�u�H7yǽ܌��!��wREO��nYE%H\N��@��.7�פ��E��H���->w��	ӈh�b�{�_�K�^��Ȗk��3b�H��$*��p����%�7�|��3
�1�]9��t1���d(i�+�(A+��v�`0H�͊#?��W��3��VV,Z��VO�&yˁ��ܫ��%?��uSn�5?+y{o�b�� �E�˖�.@t��w�ފp	.��)V]�FZ����bJ�9�\8��A,�������@��������zq��ul�]x��Լk�=;Aa �m�Rj��쾅�V��@?��G�ќ��s��Q����X��V�(4����3ItmA�c:�ͲF�K�=Y�~��vϧg^Jj"�4@���O����TnMi׻z7��_+����h��J��e�lF    S�C����ퟧ��Jj�� ��Z��& �X��m��6��6ծ�[�O�ݾޜ^L��=�(;%�{���˂�(�sԵ�a�Wӳ/��ͽ�-X$:*����&3�n�)�f�+�.`4�y
�w}�$��v]��m����{!����S�����<0GC�Ϩ��������;=�W]���W�1zVkZ�~%�g���A�=���r�)�_�w��o#���T�H	2#cu�P�f�)|��mՏ&,�
IjV.�����P��:��j�~a�H�6j��j% I�-[a �m�u�H��8�)	r�!βdN��G��.���ͮ�g��{Ť��M�g��*��n��
6|Xj@x�o��jʖ��G�D���-����##�mGs$$��b��+�z����𾻷�@,�O~r2=��䯻����i�A�ۧ�dgVБ0'	�1���3+,7�$V.���mB�S$U�ԗ0O8R���_AlK0��H,���}�\��ܢ������[w��v�7.ˎ�����v����S}|d+:�ᱽ$�ܖ�;��P��>y�C;(`K8�,ުͧv���k��wX���e8a�·���M_A��#�!��xF��|]��z|�i7N�Nn�|A0?�Z{.�c}8��7��~a��OX��o��\R�4��b�V�Uo�pi.�o���ą�M�߇�_˸�~m9O�V_�����I�{�"����첶��/�z�ˇR���H,/]L��Ɖ>�Q�4L����,a�.	ۚ�u��'�sf5Um5K�q��X�b�UMk��Lnw��;� �J���|5]�-�/������0�t_�O���!���T�� ���Q�B��r�K���Se2��Nk�\"�J�G��T�ā��Mw)�
�h��O�|����f�g������^��:&@4�u��}J��/�N���2���i`3տʔ��I=C�d�@�sB	60H�
9�/LQ�5r�)w/���f���2Z�� ���]q�lxp)�X�G��������'�+����+9s�9
�|~�:�Eh�N����$A�������S�)��U_Au���y�R�z��ż�����j
��\	�1����H��d=�Ad#�������-e�i��툘�h���a���.D�H���A�r��ǚ�u��g���x���̺�O't&��M���"�ޤ,��)�n%[�C�V���c�͹��M�nO�����h�sjr_q^ǩ��y��|���Z5qh���F&�8X/�X�l�|)�ܷ��Q�֭O������*r�x�SU��� H���N���q�ٮi4�ǩF�b���E�[�'S�R��Q��'�2o��#{KL� >��_��Kqr�W�;�R�|����)K%M��>X�HP�h�^ (ȍ�W���i�J�VʲPWxI!�xлe����DyA|�,�sbIm		Z�R��~��g|X3�[Anl_J�A�my���M��g�Su:�j����8������m�^0k=�1{$�l{����@��e�2�ǳW��l�-�O����d���Y2c����� >��˛�ך�~����w���+��u5��V*oeg��Z��eO�b3���1��Y�KF�顷�#���
i�z����ͩ;���'I�n�J��u��
0��>.�$�(Q@n���a��Й2�"R�th���%�|�xkz��,PA��Mk��y��R\w���ǋkE;�3�	�Л�9�2�
�Fo���*)!^�;YzE	�A���ʙ��)B�H�(T��������<M�4���-��1<�
w�h��E�	5kǠ#�/�U��*;z�%��ڽ�"�*E��b��z��.����Y��.��tzQ!��4)F����D�y��r+����,5L4aQ���Ĳ1��1���]��SOd1���d�=4���l�E�dY�z��b������{=&�u)k �Fƭ^X�̗֣{�򵛚�W�i�H����VK���!C��u�/i~�d%�`L*���g�IAe.�GO �Qy6��$��t�tܧ�cgsS�$��Ӯ�ˣ$�úi�;ɽ�,�I.e]mQ�V�g���{�5m*���M��xZ�?%<�+*�7���*��8�b��V�� I��a��e0R]5��Y~x�����0�ı8��CX�Ў0n���HjŹ)�"��X�)Nn�w�ԇ�`p9����L�S#r���I�>��}B�S�߂���A��� G�0g����q�����He ���k������@�2g�U����p�N�I��u�X�O��W(s���6l��n����R��$�_��e*BU?5//��	���Q�q������^�sM�o<��(��GM��y��-�A��?����G���<�N��|�(��f)�N��
W��2���C��}�o�����L&�]A����t��j��D�R�I�D��Ǹ@��-dE�d����y`"W�>͟�ugd��]�qz#f��J��7އ]͜���\��z�@��[��x�����S��PN��,\jMw^�H��]V@�����r�ĥ���T�������+Y)6�� ]���.Iۚ�4�Jʁ���~umD7>n������l�&㸦�w:$��rB��!�<o�͖�{�ﲡ�T@�;]9�=	o
�'�'f{���a��eͼ�ot��e&x]9�#��w���7�E��1*i�������矩�U�M7�eW[��A�A�7�D.��P���N�A�1�2��eNu�I�k�V�x�N}YIE��Ir8��N�Ox��Nm�_$@[ẂX�rgI��<�3&�^}�l!hw�U��a�;��YO�+�� ��uE�t[)�%a;���rE10)wcp
�򡯡̏�'Rf:P�D���h�0�rT�T�P 4�ˑJ��`�o�eg��C- ���$�QY�Rg-ZЂ*Ҁ�������T^�|��!�� �5�
-'g��P���P�b�oF�,��|���U�+0s�wD�LB�yl�������d��a�p8i����A� M�����nU[�U&a_�o2�q��_��7�굂Z�
$�B� Ѽ�Rm��
^��L����	BVM�r��HK@����4�h�׷#V�S���3�neZ��U�է2e��ķR�j����w.:DWX�(�3�G �	�Vw���ِ�;+��@�����u�Yj������
4	�p+q\�쟷P!n���ЗsI�eni�^�#��3�LloH�K���p4C�����<L�S:��0YoFw�LM��i�GW��4E����q����ư��?���V������P�=��ø/	��Ϻ�j�a�]����aZ�#��}m����$$�|�X��4�S�镜Î�)بp�Ϙ��%|/B�ǁjYy�P�]Q0�B���ʼd>�1hg�p�W���/���r�Y��`�X �C��g�>4c�ܷ�k<l��>�M���a���`H0�BAnl�a���"���jk
}*�U[�U��V2��
�)E�)��|�hkܣOP������� (7�G_�䋗:\1��E@!���<禐ٻ#�HӋWi�>:W1�T�c�Y�]Gqj ��/�N�qI?�G��b�',�|��4�)K-����P�F��ۛ6??H�k���G��2�J/���v8_!d)+�����X ��%hc��W�u|�F�I�;�	�J������x�n�)�@A���!ܾ]�����+Bg�)��k���~�ZW'	�fe،�v|�#�0wF\<�K�n�%��,P��6ɮ�v|C�&����Fŝ����� L�M'
���%�璦����le��a���
C|c
��1e�kw�a�of(��pz�|�2O��l6>����IU+Y7��iR?tl�����q�S%ͼ�Qx
��lG�~��{d�"�_,_�]Ǯ����u^��~ Z����l������-qO��s�yt~f�7�H��@��.�op�ި
��@I	�8��B>�;d�B2y�+�β�k�>�QJ%A�fѝ��!���ϴ��<�52�-Rʮ	�MAKQw���o�񡲠 ��S����&q��bc�8m��fD'@k��s|%� �  ��s�Ω��禐��Us���EV���W)'9�h�j�(�=͕��K"��3$`|6w���s^�tRX�˾D^�²��!0wF,7���8�Ieȫ�B[�	��\��Ʃ�岄OF��s/Ms�/�k|�d���=�.�_>?s�>��pV�Oͼ��U�h�>Н �-8f�w����
FL��E�B�Ղ��ܚ�w�6�~fe�sM2����&���׋�lt��P�(��7�;�����Q$s�Q��
�WZ�dN���Ɖ~�,�deG	�e���qnf	�?@�5|�U�,�;�fT��},ǪJW�7�3:�)�&���ޘQ�zN����Z�(r��T�W������XNO ��!�6��3o�� �p۽D�2^�U0SxR��3�F'��D�"�9L=4j(PҚ�Q]��`���!cq�q�!�07AL���a%=�&�S��Z���f���װ
�<��J_T���\WؔUo	 ?k�;o�����(�CXm�y�ڊ}E�(�ʄYF#� :q�X�}q�l��?�?����4U�u!����� �_����?q^̟�R�'	��*��9<�[�s�2��Y,�80���{U��9Aү���z�rg����χS=�7��ȴL��3�u��e;�볃,��W(����9>	��N*�Wx_�����)(-��v+�'z6�7��^�\2���wrrޭ�����<ƒ��m8y-��v�>��嬷�(��Z��@ܟ<��0�^��F�~e1��9���2�P��p��mQ����f�M R�u��c9��*hO�Fx+/��/D�:m c�o&���EV�q�W�"��t�	�Y+ߘ������R��?���ߞ��`{,�ʒX��o�P���+�ozG��Kqke��{s&~�Y��u4�?�#{*1�8w��l�,1>2r�Z=V$���@���_��b� 3]5�I��i;V$��g	mA��j5Is��X��F�� ��	�`H�`r߬�&k$[;��J+�F�E��ri�F��v� ɖ��$FX�"!�%0|�����%�&��`�_@_���6�
��R��_B�[��")묨`�`]TD(���b-���B�h��Kj�K�m+���X!��k��N��h��d)j�������^}�|���}�KRv�Z��U:>vE��6�4�A2�%.uT{*WC�%�eDj��Qt#6�|n�B��:tl���g�7�܆U!5ǫ��(�hw��0� }Sj+Fu��bʖ&+�:M0�S-���k�����&�Sa9��`�X��jX{��<����g'`�X���� m�%��0���h���.�ي����ϔ�V%�8�*��l���*{�@A����Ʊ��3Br�6��׊��\��D�Ar�1(Ƿ�yXh-�y��+|i��j+J��,�b=e&��z@�v�WO~s�|���nJ_ݪ��P�rR0G7�S��a�ؼl�(��]����~�Ow�rQS_ߤA��Wi�e�/��˶�Z<w���9�Ф聐��}WA��&jl�
��ْ-$Yn�`�OG9�[���|�3q�4�G�Kp�0����1�$�Y?���V�U3�&���_�?���E�~U5W� Ŋ�/��9�GsjO�5����į&��S�������	kؔ�.�&S��4|Ot5�ⴼ���Ҙk��8�u�����_ �݂BW(ݘ`iT� :��`��p������.�N_�)�q7 ��m�4�ŁŦY���t�B�-����!*`Z��u,�P�՛�>�ٮ�Yv"d2�P�89	���� ����x7��U�ϋ�%��,�ƶI�ũ�IM]¡2,��	]#����(�����Y�l���Qt��:��G$�qv�/Z>�Z�08���o.R�B�RF��r3�c�>�Ĭy�+��p��]�!�'� ��X�������|��)��kN|���û���n�쭊�l<��:J����[w���U�Ɩ�R�\Y	�'��d	u�F�_�%�`�)�#㻴-��k&��|#!��\�W	)�#�g��'���^���Xy�|���l���%���f�Q޸�øA���C<8#�v������dm8yV�j�$�����(ZR(��X{�A����zSv��k���&	���θbe@����kd5�NYg6�c���DA�7չ\�"��'ε�W���	,���߸�G�D$��D+Ob7��}9���'����(��`����6�TK�{��I�7ﰙ*���?*o㍶A;���Kh͒+�r%�;�G}C-Rw�=��F6����c?����J�mIb۠l����B��I���d�C�ەI�1il�H?�̌V�{ Z9�g>���i}E^y���llƙ�Ͳ������U5|�(�I�S�e�$Z���x�[�.L���_Ȍ�Ԓ��W�K������t�_^Ψx�H�\K#�o:7�_{��)5>��K(�_� �Ǯy�^�NZ<�M�|����*M���;(��ym�ʬ��I�r�=���}Y�4]���N��T9����Օ�Oٕ��Ӧ��N���.Uޥ�.�������4)�R�o��^/깖R.�Ly���9$��4
�I����2eWf��~�|֯2%��4�/�Rs_:.�'!��]�	����/��-_���dʍ���ؼd�����;��=}g%J>zdʥ̶�Z"��6b��#�un�a?r�ȉ�)2�N���ږ�j˔#����U��y��<�OU�m4�i����#�Z��-�[�Re�-F(��b�MUKI�	�\ٙ~��c7�c��L�C?{ڪoZ�&Zs�\��GW�C=0��� �qY�ʾ����P�!!�:J]~4)s��!�H�P~cѧo9�+s��Oe��}aȪ��j��K�qL��ԵU���`AT�2]��ܘ�k�U=���\y�/}�rW7��!�yd����T���-vK��ʾ�]���aoyN�??����^���s>��`a�33�f�r�0��c��=��M��\��|Օ��",��P����(�'�/R$i�[B��|����8���Z�-O��ii6����j�az���H�����[(s��Kq�n�
[�^���@��4dܫ@Mt�A��%�s�L-L�^ ��g�^ow'��.I�L/�`|)d���©����0p�Lg���ǰ�U�錙֪�:W��e������E���J����Z'�#[�n,b�#%OEm��|N��KtY�ԗ�XU��n8Id�S�9ۍ��:�J}	Q$�S>�̋�3b����ok�앋�)�m�>�./w�Mڌ1ڶ����*elٓL���ٹ�ˮ|�y"����i��P�HEܥ<	�N��n*�?�����QWI4�J�[�Xa+���yx;�'��ؕ��N��ur���cx��d[G�K�̗"C�O֨�$='�x��G�l>v����%ap����j�'�n������A��ׇ�b���vu�      �   �   x�U�K�0D���D�A��t�B�Q	�� q����T=c�G��~�P�4x���%��~L[��dѧ=qZ�JB��1Ԡ�3��K�c��t�r�4��8o��$^c�"�IR
%tΒ2�{()>xM�bXJ���9݇ZR�$^\�Ҩ7��ɷ�6i�
"z�~<      p      x������ � �      �   @   x�3�4�740(�P������M-)J-�2�2�8s3���<�b4����<3��Լ/F��� .
�      �      x������ � �      �      x������ � �      �   L   x�3�t�IM����2�.�1�9=�RR+@LΠԔĒT۔�%�(5��6�t.�/.�I�s���%Cy1z\\\ :�-      �   �   x�U���0���)������I��0��C��zI[ ���H��Η�#�ݤ*'M�I�i�xp�R�����=�<K�8f�V,+��IT�� 5��|�B�팖��<��vo��9_ۈv��kE�\��"ox��j�`�&���7��矷�I*e����a�8�8_�p�SJ�� V      �      x������ � �      �      x������ � �      E   �   x�M�1�0���Wdl�$F�7u.t�$�k=1�\4��d�vx�}���sHqy�@l�k��H�	��eH���n�*��n��a��`_���z��?֬�P$��U�\�a�A��Z��*/�|F��dc�ݭι��{�9���?�      H      x�K.HOI�4�4�����  �'      F      x��K����'�n}�^��An�c{��p{���̆�;���{C�v�|{�����*�yH���K�*�3��w�������_�?�y�����?L��<������,?���e������w������iX�q����o��61}��i3�k�m�k�mh��_�6��j��Ŧ��o�]��6�������7�КTߤ�Sk�m����<���i[��R��.�����?���P�Y���CS�N��9�5nd�>�TSO&�TǶ�.<}w��n#i����x��[ts�i�4�5=Yd��V��9��úi4'����M-V���;��8��>����v�'���+{2�񦽒�7C���n3��6}��^�����a�������H��i?��G�4�^��2ʞs,c���Ĵ��]�v��eG�����I��e� !��f��B�5�g���X�~��{lP�����|5�_AϾ��|���Y���_Ҍ��<�z���S���N:��l
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
�l4�4�{Ӯ�i\%��)���g��O"��(Q�]��d����V-��&{-�V�HD5���1���T�g��TR�(�<�(ܿH�V�P$^�U��=8��4{�t��D�x�+�w������Kx\�Y��VLf�V�9���,H�ϡ9Mۗ�:$vo�f(�j�Y&�=!�h(\�W5�	Y�I�:�E� {SQ��L�@������*��3�M��WY�l�*�1�n*RO�� /x�N�V=���L�w��ܓ��Z�E��luX�H�B�_��i�x��������x{{�?�O1�      G      x������ � �      I      x������ � �     