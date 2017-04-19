INSERT INTO tlkptaxon (taxonid, taxonrankid, label, parenttaxonid) values (1009, 9, 'Pinus rigida Mill.', 455);
INSERT INTO tlkptaxon (taxonid, taxonrankid, label, parenttaxonid) values (1010, 9, 'Quercus montana Willd.', 565);
INSERT INTO tlkptaxon (taxonid, taxonrankid, label, parenttaxonid) values (1011, 9, 'Abies lasiocarpa (Hook.) Nutt.', 6);

UPDATE tlkptaxon SET colid='X-' || taxonid WHERE colid IS NULL;
ALTER TABLE tlkptaxon ALTER COLUMN colid SET NOT NULL;