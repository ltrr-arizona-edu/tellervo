UPDATE tblSample SET code='A' WHERE sampleID IN (SELECT s.sampleID FROM tblsample s INNER JOIN tblElement e USING (elementID) WHERE s.code = e.code);
UPDATE tblSample SET code='A(0)' WHERE sampleid in (SELECT first from (select code,first(sampleid), count(sampleid) as cnt from tblSample group by elementid, code) foo WHERE foo.cnt > 1);
 
