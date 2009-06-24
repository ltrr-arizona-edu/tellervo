-- First parameter : VMeasurementResultGroupID
-- Second parameter: newVMeasurementResultID
CREATE OR REPLACE FUNCTION cpgdb.qappVMeasurementResultReadingOpSum(uuid, uuid) RETURNS integer AS $$
DECLARE
   ref refcursor;
   v_year integer;    -- the record's year
   v_reading integer; -- the record's reading
   v_id varchar;      -- the record's vMeasurementresultid

   startyear integer; -- the minimum year in the data
   endyear integer;   --     maximum

   idx integer;
   avg double precision;

   -- self explanatory
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

      -- initialize if we haven't hit this index before
      IF samplecount[idx] IS NULL THEN
         samplecount[idx] := 0;
         sum[idx] := 0;
         wj_dec[idx] := 0;
         wj_inc[idx] := 0;
      END IF;

      -- Check if we've moved on to another compoment
      -- If we have, skip over WJ formation
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

   -- Loop back over our data, compute averages, and dump it all!
   FOR i IN startyear..endyear LOOP
      idx := i - startyear;
      IF samplecount[idx] IS NULL THEN
         RAISE EXCEPTION 'Sum is not contiguous at year %', i;
      END IF;

      -- Make sure our math rounds properly. arg.
      avg := round(cast(sum[idx] as numeric) / samplecount[idx]);

      INSERT INTO tblVMeasurementReadingResult(VMeasurementResultID, RelYear, Reading, WJInc, WJDec, Count) VALUES
         ($2, idx, avg, wj_inc[idx], wj_dec[idx], samplecount[idx]);

      -- RAISE NOTICE '%: % [%] (%/%)', idx, avg, samplecount[idx], wj_inc[idx], wj_dec[idx];
   END LOOP;

   RETURN idx;
END;
$$ LANGUAGE plpgsql VOLATILE;
