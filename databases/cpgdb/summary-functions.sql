--
-- param 1: VMeasurementID
-- Requires that tblvmeasurementderivedcache is populated for this id!
--
CREATE OR REPLACE FUNCTION cpgdb.GetVMeasurementSummaryInfo(integer)
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

   -- Get unique sites
   FOR rec IN SELECT s.code 
	FROM tblVMeasurementDerivedCache d
	INNER JOIN tblMeasurement m ON m.MeasurementID = d.MeasurementID
	INNER JOIN tblRadius r on r.radiusID = m.radiusID
	INNER JOIN tblSample sp on sp.sampleID = r.sampleID
	INNER JOIN tblElement t on t.elementID = sp.elementID
	INNER JOIN tblSubsite su on su.subsiteID = t.subsiteID
	INNER JOIN tblSite s on s.siteID = su.siteID
	WHERE d.VMeasurementID = VMID
	GROUP BY(s.code)   
   LOOP
      IF numrows = 0 THEN
         ret.siteCode = rec.code;
      ELSE
         ret.siteCode = ret.siteCode || ';' || rec.code;
      END IF;
      numrows := numrows + 1;
   END LOOP;
   ret.siteCount = numrows;   

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

CREATE OR REPLACE FUNCTION cpgdb.GetLabel(text, integer)
RETURNS text AS $_$
DECLARE
   OBJID ALIAS FOR $2;
   labelfor text;
   
   queryLevel integer;
   query text;
   selection text;
   whereClause text;

   rec record;

   siten text;
   subsiten text;
   elementn text;
   samplen text;
   radiusn text;
   measurementn text;
   ret text;

   count integer;

BEGIN
   labelfor := lower($1);
   
   -- VMeasurement is a special case
   IF labelfor = 'vmeasurement' THEN 
      count := 0;
      FOR rec IN SELECT s.code as a,su.name as b,t.name as c,sp.name as d,r.name as e,vs.name as f 
	FROM tblVMeasurementDerivedCache d
	INNER JOIN tblMeasurement m ON m.MeasurementID = d.MeasurementID
	INNER JOIN tblRadius r on r.radiusID = m.radiusID
	INNER JOIN tblSample sp on sp.sampleID = r.sampleID
	INNER JOIN tblElement t on t.elementID = sp.elementID
	INNER JOIN tblSubsite su on su.subsiteID = t.subsiteID
	INNER JOIN tblSite s on s.siteID = su.siteID
	INNER JOIN tblVMeasurement vs on vs.vmeasurementid = d.vmeasurementid
	WHERE d.VMeasurementID = OBJID
      LOOP
         count := count + 1;

         siten := rec.a;
         subsiten := rec.b;
         elementn := rec.c;
         samplen := rec.d;
         radiusn := rec.e;
         measurementn := rec.f;
      END LOOP;

      IF count = 0 THEN
         RAISE EXCEPTION 'No data found for vmeasurement %', OBJID;
      ELSIF COUNT > 1 THEN
         -- More than one constituent? It's a sum or something derived from one.
         RETURN measurementn;
      END IF;

      -- Start with silly cornell prefix and site
      ret := 'C-' || siten;

      -- Tack on the subsitename, if it's not Main
      IF subsiten <> 'Main' THEN
         ret := ret || '/' || subsiten;
      END IF;

      ret := ret || '-' || elementn || '-' || samplen || '-' || radiusn || '-' || measurementn;

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
   selection := 's.code as a,su.name as b,t.name as c';
   query := ' FROM tblsite s INNER JOIN tblsubsite su ON su.siteid = s.siteid INNER JOIN tblelement t ON t.subsiteid = su.subsiteid';

   -- add sample
   IF queryLevel > 1 THEN
      query := query || ' INNER JOIN tblsample sp ON sp.elementid = t.elementid';
      selection := selection || ',sp.name as d';
   END IF;

   -- add radius
   IF queryLevel > 2 THEN
      query := query || ' INNER JOIN tblradius r ON r.sampleid = sp.sampleid';
      selection := selection || ',r.name as e';
   END IF;

   -- execute our messy query
   FOR rec IN EXECUTE 'SELECT ' || selection || query || whereClause LOOP
      -- Start with silly cornell prefix and site
      ret := 'C-' || rec.a;

      -- Tack on the subsitename, if it's not Main
      IF rec.b <> 'Main' THEN
         ret := ret || '/' || rec.b;
      END IF;

      -- element
      ret := ret || '-' || rec.c;
   
      -- sample
      IF queryLevel > 1 THEN
         ret := ret || '-' || rec.d;
      END IF;

      -- radius
      IF queryLevel > 2 THEN
         ret := ret || '-' || rec.e;
      END IF;
   
      RETURN ret;   
   END LOOP; -- we only get here if nothing was found!

   RAISE EXCEPTION 'No results for % %', $1, $2;
END;
$_$
LANGUAGE 'PLPGSQL' STABLE;

CREATE OR REPLACE FUNCTION cpgdb.GetVMeasurementLabel(integer)
RETURNS text AS $_$
   SELECT cpgdb.GetLabel('vmeasurement', $1);
$_$ LANGUAGE SQL STABLE;

