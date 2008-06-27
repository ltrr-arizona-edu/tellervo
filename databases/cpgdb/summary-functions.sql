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
	INNER JOIN tblMeasurement m ON m.measurementID = d.measurementID
	INNER JOIN tblRadius r on r.radiusID = m.radiusID
	INNER JOIN tblSpecimen sp on sp.specimenID = r.specimenID
	INNER JOIN tblTree t on t.treeID = sp.treeID
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
	INNER JOIN tblMeasurement m ON m.measurementID = d.measurementID
	INNER JOIN tblRadius r on r.radiusID = m.radiusID
	INNER JOIN tblSpecimen sp on sp.specimenID = r.specimenID
	INNER JOIN tblTree t on t.treeID = sp.treeID
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

CREATE OR REPLACE FUNCTION cpgdb.GetVMeasurementLabel(integer)
RETURNS text AS $_$
DECLARE
   VMID ALIAS FOR $1;

   rec record;

   siten text;
   subsiten text;
   treen text;
   specimenn text;
   radiusn text;
   measurementn text;
   ret text;

   count integer;
BEGIN
   count := 0;
   FOR rec IN SELECT s.code as a,su.name as b,t.name as c,sp.name as d,r.name as e,vm.name as f 
	FROM tblVMeasurementDerivedCache d
	INNER JOIN tblMeasurement m ON m.measurementID = d.measurementID
	INNER JOIN tblRadius r on r.radiusID = m.radiusID
	INNER JOIN tblSpecimen sp on sp.specimenID = r.specimenID
	INNER JOIN tblTree t on t.treeID = sp.treeID
	INNER JOIN tblSubsite su on su.subsiteID = t.subsiteID
	INNER JOIN tblSite s on s.siteID = su.siteID
	INNER JOIN tblVMeasurement vm on vm.vmeasurementid = d.vmeasurementid
	WHERE d.VMeasurementID = VMID
   LOOP
      count := count + 1;

      siten := rec.a;
      subsiten := rec.b;
      treen := rec.c;
      specimenn := rec.d;
      radiusn := rec.e;
      measurementn := rec.f;
   END LOOP;

   IF count = 0 THEN
      RAISE EXCEPTION 'No data found for vmeasurement %', VMID;
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

   ret := ret || '-' || treen || '-' || specimenn || '-' || radiusn || '-' || measurementn;

   RETURN ret;
END;
$_$
LANGUAGE 'PLPGSQL' STABLE;
