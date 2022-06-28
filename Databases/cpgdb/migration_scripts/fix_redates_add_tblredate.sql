CREATE FUNCTION tmp_fix_redates() RETURNS void AS $$
DECLARE
   opid integer;
   redateyear integer;
   vmid uuid;
   rr tblredate%ROWTYPE;
BEGIN
   SELECT vmeasurementopid INTO opid FROM tlkpvmeasurementop WHERE name='Redate';

   FOR vmid IN SELECT vmeasurementid FROM tblvmeasurement WHERE vmeasurementopid=opid LOOP
      SELECT * INTO rr FROM tblredate WHERE vmeasurementid=vmid;
      IF NOT FOUND THEN
         SELECT vmeasurementopparam INTO redateyear FROM tblvmeasurement WHERE vmeasurementid=vmid;
         RAISE NOTICE '% is redate without tblredate link, fixing', vmid;
         INSERT INTO tblredate(vmeasurementid, startyear, justification) VALUES (vmid, redateyear, 'Redated before justifications were implemented');
      END IF;
   END LOOP;
END;
$$ LANGUAGE PLPGSQL VOLATILE;

select * from tmp_fix_redates();

DROP FUNCTION tmp_fix_redates();
