-- Some 1.3.2 installations already use a different stable ID for this term.
-- The vocabulary uniqueness constraint is authoritative: if "plot" is already
-- present in vocabulary 2, there is nothing left for this patch to add.
INSERT INTO tlkpobjecttype (objecttype, objecttypeid, vocabularyid)
VALUES ('plot', 1000, 2)
ON CONFLICT DO NOTHING;
