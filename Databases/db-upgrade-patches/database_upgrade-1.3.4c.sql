update tlkptaxon set colid=concat('X-', taxonid) where colid is null;

ALTER TABLE public.tblcurationevent
  DROP CONSTRAINT IF EXISTS "fkey_tblcuration-tblbox";

ALTER TABLE public.tblcurationevent
  ADD CONSTRAINT "fkey_tblcuration-tblbox" FOREIGN KEY (boxid)
      REFERENCES public.tblbox (boxid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;
      
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM pg_constraint
    WHERE conrelid = 'public.tblupgradelog'::regclass
      AND conname = 'uniqupgradelog'
  ) THEN
    ALTER TABLE tblupgradelog
      ADD CONSTRAINT uniqupgradelog UNIQUE (filename);
  END IF;
END
$$;
