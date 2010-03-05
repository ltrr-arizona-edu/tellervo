CREATE OR REPLACE FUNCTION fixsamples() RETURNS void AS $$
DECLARE
   ref refcursor;
   _code text;
   _sampleid uuid;
   _elementid uuid;
   
   lastSampleID uuid;
   lastElementID uuid;
   lastCode text;
   
   num integer;
   newcode text;
BEGIN
   num := 0;
   
   OPEN ref FOR
      select samp.code,samp.sampleid,samp.elementid from (select count(sampleid) as cnt,elementid,code from tblsample group by elementid,code) bar
      inner join tblsample samp on (samp.elementid=bar.elementid and samp.code=bar.code) where cnt>1 order by bar.elementid,bar.code,createdtimestamp;

   LOOP
      FETCH ref INTO _code, _sampleid, _elementid;
      EXIT WHEN NOT FOUND;

      --RAISE NOTICE '%, %, %', _elementid, _sampleid, _code;
      
      IF _elementid <> lastElementID OR lastElementID IS NULL THEN
         lastElementID := _elementid;
         num := 0;
      END IF;

      IF _code <> lastCode OR lastCode IS NULL THEN
         lastCode := _code;
         num := 0;
      END IF;

      IF num = 0 THEN
         newcode := _code;
      ELSE
         newcode := _code || '(x' || num || ')';
         UPDATE tblSample SET code=newcode WHERE sampleid=_sampleid;
      END IF;

      num := num + 1;

      RAISE NOTICE '% -> %', _code, newcode;
   END LOOP;

END;
$$ LANGUAGE plpgsql VOLATILE;
