-- Recover missing tblelement.taxonid values from a CSV exported by
-- scripts/recover-missing-taxonids.sh.
--
-- Run only after repair-tlkptaxon-generic.sql. This file is invoked by the
-- wrapper, which safely replaces the local CSV placeholders in a temporary
-- copy of this file.

\set ON_ERROR_STOP on

BEGIN;

-- Element updates can invoke legacy PL/Java cache triggers which emit a large
-- number of non-fatal NOTICE messages on damaged databases. Keep warnings and
-- errors visible while allowing the recovery summary to remain readable.
SET LOCAL client_min_messages = warning;

LOCK TABLE public.tblelement IN SHARE ROW EXCLUSIVE MODE;
LOCK TABLE public.tlkptaxon IN SHARE MODE;

DO $preflight$
BEGIN
    IF to_regclass('public.tblelement') IS NULL
       OR to_regclass('public.tlkptaxon') IS NULL THEN
        RAISE EXCEPTION
            'Recovery aborted: public.tblelement or public.tlkptaxon is missing';
    END IF;

    IF EXISTS (
        SELECT 1 FROM public.tlkptaxon WHERE taxonid LIKE 'legacy-%'
    ) THEN
        RAISE EXCEPTION
            'Recovery aborted: run repair-tlkptaxon-generic.sql first';
    END IF;

    IF EXISTS (
        SELECT 1 FROM public.tblelement AS element
        LEFT JOIN public.tlkptaxon AS taxon ON taxon.taxonid = element.taxonid
        WHERE COALESCE(element.taxonid, '') <> '' AND taxon.taxonid IS NULL
    ) THEN
        RAISE EXCEPTION
            'Recovery aborted: target already contains orphaned taxon links';
    END IF;
END
$preflight$;

CREATE TEMP TABLE imported_taxon_evidence (
    elementid uuid NOT NULL,
    taxonid varchar NOT NULL,
    source text NOT NULL,
    evidence_timestamp timestamp with time zone NOT NULL
) ON COMMIT DROP;

\copy imported_taxon_evidence (elementid, taxonid, source, evidence_timestamp) FROM '__EVIDENCE_FILE__' WITH (FORMAT csv, HEADER true)

DO $evidence_preflight$
BEGIN
    IF EXISTS (
        SELECT elementid
        FROM imported_taxon_evidence
        GROUP BY elementid
        HAVING count(*) > 1
    ) THEN
        RAISE EXCEPTION
            'Recovery aborted: evidence export contains duplicate element IDs';
    END IF;
END
$evidence_preflight$;

-- Request logs made before the June 2022 taxon upgrade contain Catalogue of
-- Life 2008 identifiers. Resolve the known legacy identifiers to the
-- authoritative IDs installed by repair-tlkptaxon-generic.sql.
CREATE TEMP TABLE legacy_request_taxon_id_map (
    old_id varchar PRIMARY KEY,
    taxonid varchar NOT NULL
) ON COMMIT DROP;

INSERT INTO legacy_request_taxon_id_map (old_id, taxonid) VALUES
    ('15351',   '56YL'),
    ('15398',   '6Q7C'),
    ('15400',   '6QPY'),
    ('1541824', '4J269'),
    ('1569234', '4J2HX'),
    ('1575148', '4J29V'),
    ('1610047', '8K9Y'),
    ('1610055', '8KBM'),
    ('1616818', '4J224'),
    ('1644497', '4J2J5'),
    ('1646211', '5WVCR'),
    ('1646801', '4HQ2V'),
    ('1646867', '4J234'),
    ('1646872', '6WHCL'),
    ('1646911', '4J2F3'),
    ('1646918', '4J24Y'),
    ('281',     'P'),
    ('51976',   '73Y7'),
    ('60046',   '63PVP'),
    ('63263',   '63SMF');

-- Numeric IDs stored directly in pre-upgrade tblelement rows belong to the
-- old tlkptaxon namespace. This complete mapping was extracted from
-- database_upgrade-1.9-taxon-upgrade-6.sql.
CREATE TEMP TABLE legacy_database_taxon_id_map (
    old_id varchar PRIMARY KEY,
    taxonid varchar NOT NULL
) ON COMMIT DROP;

\copy legacy_database_taxon_id_map (old_id, taxonid) FROM '__LEGACY_MAP_FILE__' WITH (FORMAT csv, HEADER true)

CREATE TEMP TABLE taxon_recovery_candidates ON COMMIT DROP AS
WITH normalized_evidence AS (
    SELECT evidence.*,
           CASE
             WHEN evidence.taxonid LIKE 'legacy-%'
               THEN substring(evidence.taxonid FROM 8)
             ELSE evidence.taxonid
           END AS unprefixed_taxonid
    FROM imported_taxon_evidence AS evidence
), resolved_evidence AS (
    SELECT evidence.*,
           COALESCE(request_mapping.taxonid,
                    database_mapping.taxonid,
                    evidence.unprefixed_taxonid)
             AS target_taxonid
    FROM normalized_evidence AS evidence
    LEFT JOIN legacy_request_taxon_id_map AS request_mapping
      ON request_mapping.old_id = evidence.unprefixed_taxonid
     AND evidence.source LIKE '%:tblrequestlog:%'
    LEFT JOIN legacy_database_taxon_id_map AS database_mapping
      ON database_mapping.old_id = evidence.unprefixed_taxonid
     AND evidence.source LIKE '%:tblelement'
)
SELECT element.elementid,
       evidence.taxonid AS evidence_taxonid,
       evidence.target_taxonid,
       evidence.source,
       evidence.evidence_timestamp,
       (taxon.taxonid IS NOT NULL) AS target_taxon_exists
FROM public.tblelement AS element
JOIN resolved_evidence AS evidence USING (elementid)
LEFT JOIN public.tlkptaxon AS taxon
  ON taxon.taxonid = evidence.target_taxonid
WHERE COALESCE(element.taxonid, '') = '';

CREATE TEMP TABLE taxon_recovery_summary ON COMMIT DROP AS
SELECT
    (SELECT count(*) FROM public.tblelement
     WHERE COALESCE(taxonid, '') = '') AS missing_before,
    count(*) AS with_evidence,
    count(*) FILTER (WHERE target_taxon_exists) AS recoverable,
    count(*) FILTER (WHERE NOT target_taxon_exists) AS evidence_taxon_not_found
FROM taxon_recovery_candidates;

UPDATE public.tblelement AS element
SET taxonid = candidate.target_taxonid
FROM taxon_recovery_candidates AS candidate
WHERE element.elementid = candidate.elementid
  AND COALESCE(element.taxonid, '') = ''
  AND candidate.target_taxon_exists;

DO $verify$
DECLARE
    expected_count bigint;
    recovered_count bigint;
BEGIN
    SELECT recoverable INTO expected_count FROM taxon_recovery_summary;

    SELECT count(*) INTO recovered_count
    FROM taxon_recovery_candidates AS candidate
    JOIN public.tblelement AS element USING (elementid)
    WHERE candidate.target_taxon_exists
      AND element.taxonid = candidate.target_taxonid;

    IF recovered_count <> expected_count THEN
        RAISE EXCEPTION
            'Recovery verification failed: expected % updates, verified %',
            expected_count, recovered_count;
    END IF;

    IF EXISTS (
        SELECT 1 FROM public.tblelement AS element
        LEFT JOIN public.tlkptaxon AS taxon ON taxon.taxonid = element.taxonid
        WHERE COALESCE(element.taxonid, '') <> '' AND taxon.taxonid IS NULL
    ) THEN
        RAISE EXCEPTION
            'Recovery verification failed: orphaned taxon links were created';
    END IF;
END
$verify$;

SELECT missing_before,
       with_evidence,
       recoverable AS recovered,
       evidence_taxon_not_found,
       missing_before - recoverable AS remaining_missing
FROM taxon_recovery_summary;

-- These rows have evidence, but its taxon ID is not in the repaired target
-- dictionary. They are reported for review and are never applied.
SELECT elementid,
       evidence_taxonid,
       target_taxonid,
       source,
       evidence_timestamp
FROM taxon_recovery_candidates
WHERE NOT target_taxon_exists
ORDER BY elementid;

COMMIT;
