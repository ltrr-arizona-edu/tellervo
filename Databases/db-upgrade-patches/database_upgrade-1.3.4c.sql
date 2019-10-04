update tlkptaxon set colid=concat('X-', taxonid) where colid is null;

ALTER TABLE public.tblcurationevent DROP CONSTRAINT "fkey_tblcuration-tblbox";

ALTER TABLE public.tblcurationevent
  ADD CONSTRAINT "fkey_tblcuration-tblbox" FOREIGN KEY (boxid)
      REFERENCES public.tblbox (boxid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;