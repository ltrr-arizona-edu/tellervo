ALTER TABLE tblobject ALTER COLUMN projectid DROP DEFAULT;

-- Remove index so sub-objects with same code can be included in database
DROP INDEX IF EXISTS object_code_index;
DO $$
BEGIN
  IF to_regclass('public.unique_objectcode_perparent') IS NULL THEN
    ALTER TABLE tblobject
      ADD CONSTRAINT unique_objectcode_perparent
      UNIQUE (code, parentobjectid);
  END IF;
END
$$;
