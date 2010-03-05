DROP TYPE femess_ret CASCADE;
CREATE TYPE femess_ret AS (
   objectid uuid,
   objectcode text,
   elementcode text,
   taxonid bigint,
   taxonname text,
   taxoncount integer,
   constituents text
);

CREATE OR REPLACE FUNCTION fixelement_common_taxon(taxonidsin bigint[]) RETURNS text AS $$
DECLARE
   taxonids bigint[] := taxonidsin;
   numrows integer := 0;
   taxondepth integer;

   curtf text;
   prevtf text;

   curtaxa typfulltaxonomy;
   prevtaxa typfulltaxonomy;
   ret tlkptaxon;
   idx integer;
   tmpid bigint;
BEGIN
   idx := array_upper(taxonids, 1) - array_lower(taxonids, 1);

   -- If it's got 2 and one is plantae...
   IF idx = 1 AND 1 = ANY(taxonids) THEN
      FOR idx IN array_lower(taxonids, 1)..array_upper(taxonids, 1) LOOP
         IF taxonids[idx] <> 1 THEN
            tmpid := taxonids[idx];
            taxonids := ARRAY[tmpid];
            EXIT;
         END IF;
      END LOOP;
   END IF;

   FOR idx IN array_lower(taxonids, 1)..array_upper(taxonids, 1) LOOP
      curtaxa := cpgdb.qryTaxonomy(taxonids[idx]::integer);

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

   return curtf;
END;
$$ LANGUAGE PLPGSQL STABLE;

CREATE OR REPLACE FUNCTION fix_elements_mess() RETURNS SETOF femess_ret AS $$
DECLARE
   ref refcursor;

   numfubar integer;
   codebase text;
   objid uuid;
   codearray text[];
   elemidarray uuid[];

   elem tblelement;

   _taxonid bigint;

   objectcode text;

   taxonmap text[];
   taxoncntmap integer[];

   idx integer;
   cnt integer := 0;
   tcnt integer;

   ov femess_ret;
BEGIN

   OPEN ref FOR select eq.count,eq.ocode,eq.objectid,eq.codes,eq.elements from (select count(elementid) as count, regexp_replace(code, E'\\([0-9]+\\)', '') as ocode, 
                objectid, array_accum(code) as codes, array_accum(elementid) as elements from tblelement group by objectid, ocode) eq 
                where eq.count > 1 order by count desc;

   LOOP
      FETCH ref INTO numfubar, codebase, objid, codearray, elemidarray;

      IF NOT FOUND THEN
         EXIT;
      END IF;

      _taxonid := NULL;
      taxonmap := '{}';
      taxoncntmap := '{}';

      SELECT code INTO objectcode FROM tblObject WHERE objectID=objid;

      ov.objectid := objid;
      ov.objectcode := objectcode;
      ov.elementcode := codebase;

      FOR idx IN array_lower(elemidarray, 1)..array_upper(elemidarray, 1) LOOP
         SELECT * INTO elem FROM tblElement WHERE elementID=elemidarray[idx];

         IF taxonmap[elem.taxonid] IS NULL THEN
            taxonmap[elem.taxonid] := '';
            taxoncntmap[elem.taxonid] := 0;
         END IF;

         taxonmap[elem.taxonid] := taxonmap[elem.taxonid] || codearray[idx] || ', ';
         taxoncntmap[elem.taxonid] := taxoncntmap[elem.taxonid] + 1;
      END LOOP;

      tcnt := 0;
      FOR _taxonid IN array_lower(taxonmap, 1)..array_upper(taxonmap, 1) LOOP
         IF taxonmap[_taxonid] IS NULL THEN
            CONTINUE;
         END IF;

         SELECT label INTO ov.taxonname FROM tlkptaxon WHERE taxonid=_taxonid;
         ov.taxonid := _taxonid;
         ov.constituents := taxonmap[_taxonid];
         ov.taxoncount := taxoncntmap[_taxonid];
         
         RETURN NEXT ov;
      END LOOP;
      

      cnt := cnt + 1;
      --EXIT WHEN cnt > 10;
   END LOOP;
END
$$ LANGUAGE PLPGSQL VOLATILE;

CREATE OR REPLACE FUNCTION fix_elements_common_taxon() RETURNS void AS $$
DECLARE
   ref refcursor;

   objid uuid;
   eprefix text;
   ctaxon text;
   otaxon text;

   rt text;
   ele tblelement;
   tid bigint;
BEGIN

   OPEN ref FOR select qry.objectid,qry.elementcode,fixelement_common_taxon(qry.ids) FROM (select sq.* from (select objectcode,objectid,elementcode,array_accum(taxonname) as taxa,
           array_accum(taxoncount) as weight, array_accum(taxonid) as ids, count(taxonname) as tcnt
           from fix_elements_mess() GROUP BY objectid, objectcode, elementcode) as sq WHERE sq.tcnt > 1 order by sq.tcnt desc) as qry;

   LOOP
      FETCH ref INTO objid, eprefix, ctaxon;
      EXIT WHEN NOT FOUND;

      SELECT taxonid INTO tid FROM tlkpTaxon WHERE label=ctaxon;

      rt := '^' || eprefix || E'(\\(\\d+\\))?';

      FOR ele IN SELECT * FROM tblElement WHERE objectid=objid AND code ~ rt LOOP
         SELECT label INTO otaxon FROM tlkpTaxon WHERE taxonid=ele.taxonid;

         RAISE NOTICE '% %: % -> %', eprefix, ele.code, otaxon, ctaxon;

         UPDATE tblElement SET taxonid=tid WHERE elementId=ele.elementid;
      END LOOP;
   END LOOP;

END;
$$ LANGUAGE PLPGSQL VOLATILE;

CREATE OR REPLACE FUNCTION merge_like_elements() RETURNS void AS $$
DECLARE
   ref refcursor;   

   cnt integer;
   newcode text;
   objid uuid;
   elementsarray uuid[];

   mastere tblelement;
   idx integer;
BEGIN
   OPEN ref FOR select count(elementid) as count, regexp_replace(code, E'\\([0-9]+\\)', '') as ocode, objectid, array_accum(elementid) as elements from tblelement group by objectid, ocode;

   LOOP
      FETCH ref INTO cnt, newcode, objid, elementsarray;
      EXIT WHEN NOT FOUND;

      SELECT * INTO mastere FROM tblElement WHERE objectid=objid AND code=newcode;
      IF NOT FOUND THEN
         RAISE EXCEPTION 'No master element for %/%', objid, newcode;
      END IF;

      IF NOT mastere.elementid = ANY(elementsarray) THEN
         RAISE EXCEPTION 'Master element not in elements array?? %/%', objid, newcode;
      END IF;

      FOR idx IN array_lower(elementsarray, 1)..array_upper(elementsarray,1) LOOP
         IF elementsarray[idx] = mastere.elementid THEN
            CONTINUE;
         END IF;

         SELECT COUNT(*) INTO cnt FROM tblSample WHERE elementid=elementsarray[idx];
         RAISE NOTICE 'Moving % samples to %', cnt, newcode;

         UPDATE tblSample SET elementid=mastere.elementid WHERE elementid=elementsarray[idx];
         DELETE FROM tblElement WHERE elementid=elementsarray[idx];
      END LOOP;
       
   END LOOP;

END;
$$ LANGUAGE PLPGSQL VOLATILE;
