DROP VIEW vwringsleaderboard;

CREATE OR REPLACE VIEW vwringsleaderboard AS 
 SELECT su.securityuserid, (su.lastname::text || ', '::text) || su.firstname::text AS name, count(r.*) AS rings
   FROM tblvmeasurement vm
   LEFT JOIN tblsecurityuser su ON vm.owneruserid = su.securityuserid
   LEFT JOIN tblmeasurement m ON vm.measurementid = m.measurementid
   LEFT JOIN tblreading r ON m.measurementid = r.measurementid
  WHERE vm.vmeasurementopid = 5
  GROUP BY (su.lastname::text || ', '::text) || su.firstname::text, su.securityuserid
  ORDER BY count(r.*) DESC;

ALTER TABLE vwringsleaderboard OWNER TO aps03pwb;

DROP VIEW vwringwidthleaderboard;

CREATE OR REPLACE VIEW vwringwidthleaderboard AS 
 SELECT su.securityuserid, (su.lastname::text || ', '::text) || su.firstname::text AS name, sum(r.reading) AS totalringwidth
   FROM tblvmeasurement vm
   LEFT JOIN tblsecurityuser su ON vm.owneruserid = su.securityuserid
   LEFT JOIN tblmeasurement m ON vm.measurementid = m.measurementid
   LEFT JOIN tblreading r ON m.measurementid = r.measurementid
  WHERE vm.vmeasurementopid = 5
  GROUP BY (su.lastname::text || ', '::text) || su.firstname::text, su.securityuserid
  ORDER BY sum(r.reading) DESC;

ALTER TABLE vwringwidthleaderboard OWNER TO aps03pwb;

DROP VIEW vwsampleleaderboard;

CREATE OR REPLACE VIEW vwsampleleaderboard AS 
 SELECT su.securityuserid, (su.lastname::text || ', '::text) || su.firstname::text AS name, count(m.*) AS samples
   FROM tblvmeasurement vm
   LEFT JOIN tblsecurityuser su ON vm.owneruserid = su.securityuserid
   LEFT JOIN tblmeasurement m ON vm.measurementid = m.measurementid
  WHERE vm.vmeasurementopid = 5
  GROUP BY (su.lastname::text || ', '::text) || su.firstname::text, su.securityuserid
  ORDER BY count(m.*) DESC;

ALTER TABLE vwsampleleaderboard OWNER TO aps03pwb;

DROP VIEW vwleaderboard;

CREATE OR REPLACE VIEW vwleaderboard AS 
 SELECT slb.securityuserid, slb.name, slb.samples, rlb.rings, rwlb.totalringwidth
   FROM vwsampleleaderboard slb, vwringsleaderboard rlb, vwringwidthleaderboard rwlb
  WHERE slb.securityuserid = rlb.securityuserid AND slb.securityuserid = rwlb.securityuserid;

ALTER TABLE vwleaderboard OWNER TO aps03pwb;

