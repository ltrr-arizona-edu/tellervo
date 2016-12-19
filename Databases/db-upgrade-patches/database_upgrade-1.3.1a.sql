ALTER TABLE tblobject ALTER COLUMN projectid DROP DEFAULT;

-- Remove index so sub-objects with same code can be included in database
DROP INDEX object_code_index;
ALTER TABLE tblobject ADD CONSTRAINT unique_objectcode_perparent UNIQUE (code, parentobjectid),
