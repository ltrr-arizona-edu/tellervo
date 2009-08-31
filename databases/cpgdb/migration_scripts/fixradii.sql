CREATE OR REPLACE FUNCTION fixradii() RETURNS void AS $$
DECLARE
   ref refcursor;
   _code text;
   _radiusid uuid;
   _sampleid uuid;
   
   lastRadiusID uuid;
   lastSampleID uuid;
   lastCode text;
   
   num integer;
   newcode text;
BEGIN
   num := 0;
   
   OPEN ref FOR
      select rad.code,rad.radiusid,rad.sampleid from (select count(radiusid) as cnt,sampleid,code from tblradius group by sampleid,code) bar
      inner join tblradius rad on (rad.sampleid=bar.sampleid and rad.code=bar.code) where cnt>1 order by bar.sampleid,bar.code,createdtimestamp;

   LOOP
      FETCH ref INTO _code, _radiusid, _sampleid;
      EXIT WHEN NOT FOUND;

      RAISE NOTICE '%, %, %', _sampleid, _radiusid, _code;
      
      IF _sampleid <> lastSampleID OR lastSampleID IS NULL THEN
         lastSampleID := _sampleid;
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
         -- UPDATE tblSample SET code=newcode WHERE sampleid=_sampleid;
      END IF;

      num := num + 1;

      RAISE NOTICE '% -> %', _code, newcode;
   END LOOP;

END;
$$ LANGUAGE plpgsql VOLATILE;
