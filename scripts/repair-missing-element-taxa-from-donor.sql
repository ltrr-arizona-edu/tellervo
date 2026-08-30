\set ON_ERROR_STOP on

-- Recover missing tblelement.taxonid values from three independent sources:
--   1. explicit element create/update requests (highest priority),
--   2. an older donor database containing the same element UUIDs, and
--   3. direct-measurement metadata cache values (lowest priority).
--
-- Required:
--   -v donor_conninfo='dbname=tellervoltrr'
-- Optional:
--   -v apply=1       Commit the repair. The default is a dry run/rollback.

\if :{?donor_conninfo}
\else
  \echo 'ERROR: pass -v donor_conninfo=dbname=DONOR_DATABASE'
  \quit 2
\endif

\if :{?apply}
\else
  \set apply 0
\endif

BEGIN;

CREATE TEMP TABLE taxon_repair_evidence (
    elementid uuid NOT NULL,
    taxonid varchar NOT NULL,
    source varchar NOT NULL,
    priority integer NOT NULL,
    PRIMARY KEY (elementid, taxonid, source)
) ON COMMIT DROP;

-- Exact UUID matches are record identities, not name-based guesses.
INSERT INTO taxon_repair_evidence (elementid, taxonid, source, priority)
SELECT target.elementid, donor.taxonid, 'donor UUID', 200
FROM public.tblelement AS target
JOIN dblink(
        :'donor_conninfo',
        'SELECT elementid, taxonid FROM public.tblelement WHERE taxonid IS NOT NULL'
     ) AS donor(elementid uuid, taxonid varchar)
  USING (elementid)
WHERE target.taxonid IS NULL;

-- Map the legacy Catalogue of Life 2008 identifiers found in this database's
-- request log to the authoritative identifiers installed by taxon-upgrade-6.
CREATE TEMP TABLE old_col_taxon_map (
    old_id varchar PRIMARY KEY,
    taxonid varchar NOT NULL
) ON COMMIT DROP;

INSERT INTO old_col_taxon_map (old_id, taxonid) VALUES
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

WITH parsed AS (
    SELECT requestlogid,
           substring(request FROM '<c:request type="([^"]+)"') AS kind,
           substring(request FROM 'parentEntityID="([^"]+)"') AS parent_id,
           substring(request FROM '<tridas:title>([^<]*)</tridas:title>') AS title,
           substring(request FROM '<tridas:identifier[^>]*>([^<]+)</tridas:identifier>') AS identifier,
           substring(request FROM '<tridas:taxon[^>]*normalId="([^"]+)"') AS old_id
    FROM public.tblrequestlog
    WHERE request ~* '<tridas:taxon'
), resolved AS (
    SELECT parsed.*,
           CASE
             WHEN kind = 'update' AND identifier ~* '^[0-9a-f-]{36}$'
               THEN identifier::uuid
             WHEN kind = 'create'
               THEN element.elementid
           END AS elementid
    FROM parsed
    LEFT JOIN public.tblelement AS element
      ON parsed.kind = 'create'
     AND parsed.parent_id ~* '^[0-9a-f-]{36}$'
     AND element.objectid = parsed.parent_id::uuid
     AND element.code = parsed.title
), latest AS (
    SELECT resolved.*,
           row_number() OVER (
               PARTITION BY elementid ORDER BY requestlogid DESC
           ) AS row_number
    FROM resolved
    WHERE elementid IS NOT NULL
)
INSERT INTO taxon_repair_evidence (elementid, taxonid, source, priority)
SELECT latest.elementid, mapping.taxonid, 'request log', 300
FROM latest
JOIN public.tblelement AS element USING (elementid)
JOIN old_col_taxon_map AS mapping USING (old_id)
WHERE latest.row_number = 1
  AND element.taxonid IS NULL;

-- A direct VMeasurement represents one measurement/element. Its cached common
-- taxon is therefore usable only when it resolves to exactly one dictionary row.
WITH cache_evidence AS (
    SELECT DISTINCT element.elementid, cache.commontaxonname
    FROM public.tblelement AS element
    JOIN public.tblsample AS sample USING (elementid)
    JOIN public.tblradius AS radius USING (sampleid)
    JOIN public.tblmeasurement AS measurement USING (radiusid)
    JOIN public.tblvmeasurement AS vmeasurement USING (measurementid)
    JOIN public.tblvmeasurementmetacache AS cache USING (vmeasurementid)
    WHERE element.taxonid IS NULL
      AND vmeasurement.measurementid IS NOT NULL
      AND NULLIF(btrim(cache.commontaxonname), '') IS NOT NULL
), unambiguous AS (
    SELECT evidence.elementid, min(taxon.taxonid) AS taxonid
    FROM cache_evidence AS evidence
    JOIN public.tlkptaxon AS taxon
      ON lower(btrim(taxon.label)) = lower(btrim(evidence.commontaxonname))
    GROUP BY evidence.elementid
    HAVING count(DISTINCT evidence.commontaxonname) = 1
       AND count(DISTINCT taxon.taxonid) = 1
)
INSERT INTO taxon_repair_evidence (elementid, taxonid, source, priority)
SELECT elementid, taxonid, 'direct measurement cache', 100
FROM unambiguous;

DO $$
DECLARE
    conflict_count integer;
    missing_dictionary_count integer;
BEGIN
    SELECT count(*) INTO conflict_count
    FROM (
        SELECT elementid
        FROM taxon_repair_evidence
        GROUP BY elementid
        HAVING count(DISTINCT taxonid) > 1
    ) AS conflicts;

    IF conflict_count > 0 THEN
        RAISE EXCEPTION
            'Taxon repair aborted: % elements have conflicting evidence',
            conflict_count;
    END IF;

    SELECT count(DISTINCT evidence.taxonid) INTO missing_dictionary_count
    FROM taxon_repair_evidence AS evidence
    LEFT JOIN public.tlkptaxon AS taxon USING (taxonid)
    WHERE taxon.taxonid IS NULL;

    IF missing_dictionary_count > 0 THEN
        RAISE EXCEPTION
            'Taxon repair aborted: % referenced taxa are absent from tlkptaxon',
            missing_dictionary_count;
    END IF;
END
$$;

CREATE TEMP TABLE taxon_repair_candidates ON COMMIT DROP AS
SELECT elementid, taxonid, source
FROM (
    SELECT evidence.*,
           row_number() OVER (
               PARTITION BY elementid ORDER BY priority DESC, source
           ) AS row_number
    FROM taxon_repair_evidence AS evidence
) AS ranked
WHERE row_number = 1;

\echo 'Recovery candidates by selected source:'
SELECT source, count(*) AS elements
FROM taxon_repair_candidates
GROUP BY source
ORDER BY source;

\echo 'Repair totals:'
SELECT (SELECT count(*) FROM public.tblelement WHERE taxonid IS NULL) AS missing_before,
       count(*) AS recoverable,
       (SELECT count(*) FROM public.tblelement WHERE taxonid IS NULL) - count(*) AS unresolved_after
FROM taxon_repair_candidates;

\if :apply
  SET LOCAL client_min_messages = warning;

  WITH updated AS (
      UPDATE public.tblelement AS element
         SET taxonid = candidate.taxonid
        FROM taxon_repair_candidates AS candidate
       WHERE element.elementid = candidate.elementid
         AND element.taxonid IS NULL
      RETURNING element.elementid
  )
  SELECT count(*) AS updated_elements FROM updated;

  DO $$
  DECLARE
      expected integer;
      remaining_candidates integer;
  BEGIN
      SELECT count(*) INTO expected FROM taxon_repair_candidates;
      SELECT count(*) INTO remaining_candidates
      FROM public.tblelement AS element
      JOIN taxon_repair_candidates AS candidate USING (elementid)
      WHERE element.taxonid IS NULL;

      IF remaining_candidates <> 0 THEN
          RAISE EXCEPTION
              'Taxon repair verification failed: % of % candidates remain NULL',
              remaining_candidates, expected;
      END IF;
  END
  $$;

  SELECT count(*) AS missing_after
  FROM public.tblelement
  WHERE taxonid IS NULL;

  \echo 'Applying taxon repair (COMMIT).'
  COMMIT;
\else
  \echo 'Dry run only; rolling back. Pass -v apply=1 to commit.'
  ROLLBACK;
\endif
