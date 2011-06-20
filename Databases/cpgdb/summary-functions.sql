--
-- param 1: VMeasurementID
-- Requires that tblvmeasurementderivedcache is populated for this id!
--
CREATE OR REPLACE FUNCTION cpgdb.GetVMeasurementSummaryInfo(tblvmeasurement.vmeasurementid%TYPE)
RETURNS typVMeasurementSummaryInfo AS $$
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

   -- Get unique objects
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

   -- Now, get the list of associated taxa
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
         -- Find the first non-empty taxa field
         taxondepth := 15;
         LOOP
            curtf := cpgdb._getTaxonForDepth(taxondepth, curtaxa);
            EXIT WHEN curtf IS NOT NULL;
            taxondepth := taxondepth - 1;
         END LOOP;
      ELSE
         -- Find the first matching taxa field
         LOOP
            curtf := cpgdb._getTaxonForDepth(taxondepth, curtaxa);
            prevtf := cpgdb._getTaxonForDepth(taxondepth, prevtaxa);            
            -- NOTE: NULLs are not considered matches by this
            -- I think this is fine?
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
$$
LANGUAGE 'PLPGSQL' STABLE;

CREATE OR REPLACE FUNCTION cpgdb._getTaxonForDepth(integer, typfulltaxonomy)
RETURNS text AS $$
   SELECT CASE $1
     WHEN 1 THEN $2.kingdom 
     WHEN 2 THEN $2.subkingdom
     WHEN 3 THEN $2.phylum
     WHEN 4 THEN $2.division
     WHEN 5 THEN $2.class
     WHEN 6 THEN $2.txorder
     WHEN 7 THEN $2.family
     WHEN 8 THEN $2.genus
     WHEN 9 THEN $2.species
     WHEN 10 THEN $2.subspecies
     WHEN 11 THEN $2.race
     WHEN 12 THEN $2.variety
     WHEN 13 THEN $2.subvariety
     WHEN 14 THEN $2.form
     WHEN 15 THEN $2.subform
     ELSE 'invalid'
   END;
$$
LANGUAGE 'SQL' IMMUTABLE;

--
-- GetLabel: returns a lab code label for a specified object
-- 
-- 1: label type ('vmeasurement', 'sample', 'element', )
-- 2: label id
-- 3: boolean, true = prefix only (e.g. C-XXX-A-B-1-D), false = full lab code (C-XXX-A-B-1-)
--
CREATE OR REPLACE FUNCTION cpgdb.GetLabel(text, uuid, boolean)
RETURNS text AS $_$
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
   
   -- VMeasurement is a special case
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
         -- More than one constituent? It's a sum or something derived from one.

         -- No prefix, then.
         IF PrefixOnly THEN
            RETURN '';
         END IF;

         RETURN measurementn;
      END IF;

      -- Start with silly cornell prefix and object
      ret := 'C-' || objectn;

      ret := ret || '-' || elementn || '-' || samplen || '-' || radiusn || '-';

      -- add the name if we're not a prefix
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

   -- Start out with the basics
   selection := 's.code as a,t.code as c';
   query := ' FROM tblobject s INNER JOIN tblelement t ON t.objectid = s.objectid';

   -- add sample
   IF queryLevel > 1 THEN
      query := query || ' INNER JOIN tblsample sp ON sp.elementid = t.elementid';
      selection := selection || ',sp.code as d';
   END IF;

   -- add radius
   IF queryLevel > 2 THEN
      query := query || ' INNER JOIN tblradius r ON r.sampleid = sp.sampleid';
      selection := selection || ',r.code as e';
   END IF;

   -- execute our messy query
   FOR rec IN EXECUTE 'SELECT ' || selection || query || whereClause LOOP
      -- Start with silly cornell prefix and object
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
$_$
LANGUAGE 'PLPGSQL' STABLE;

CREATE OR REPLACE FUNCTION cpgdb.GetVMeasurementLabel(tblvmeasurement.vmeasurementid%TYPE)
RETURNS text AS $_$
   SELECT cpgdb.GetLabel('vmeasurement', $1, false);
$_$ LANGUAGE SQL STABLE;

CREATE OR REPLACE FUNCTION cpgdb.GetVMeasurementPrefix(tblvmeasurement.vmeasurementid%TYPE)
RETURNS text AS $_$
   SELECT cpgdb.GetLabel('vmeasurement', $1, true);
$_$ LANGUAGE SQL STABLE;

