-- Prevent any new NULL element taxon links while allowing databases with
-- historical corruption to be upgraded and repaired. PostgreSQL enforces a
-- NOT VALID check for new inserts and updates but does not scan old rows when
-- the constraint is added.
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conrelid = 'public.tblelement'::regclass
          AND conname = 'enforce_element_taxon_not_null'
    ) THEN
        ALTER TABLE public.tblelement
            ADD CONSTRAINT enforce_element_taxon_not_null
            CHECK (taxonid IS NOT NULL) NOT VALID;
    END IF;
END
$$;

-- Clean databases can use the native NOT NULL property immediately. Damaged
-- databases keep the enforced check until their historical NULLs are repaired.
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM public.tblelement WHERE taxonid IS NULL
    ) THEN
        ALTER TABLE public.tblelement
            VALIDATE CONSTRAINT enforce_element_taxon_not_null;
        ALTER TABLE public.tblelement
            ALTER COLUMN taxonid SET NOT NULL;
    ELSE
        RAISE WARNING
            'Existing NULL tblelement.taxonid values require repair; new NULL values are now prohibited';
    END IF;
END
$$;
