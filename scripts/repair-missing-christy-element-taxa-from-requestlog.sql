\set ON_ERROR_STOP on

-- Recover tblelement.taxonid values lost when taxon-upgrade-6 was applied to
-- tellervochristy on 2022-07-05.  This repair uses only requests recorded
-- before that upgrade:
--
--   * update requests identify elements directly by UUID;
--   * create requests are matched by parent object UUID and element code, then
--     restricted to the request closest to tblelement.createdtimestamp; and
--   * the most recent valid pre-upgrade assignment wins.
--
-- The default is a full transactional dry run: the UPDATE and verification
-- execute, then roll back.  Pass -v apply=1 to commit.

\if :{?apply}
\else
  \set apply 0
\endif

BEGIN;

CREATE TEMP TABLE christy_legacy_taxon_map (
    old_id varchar PRIMARY KEY,
    taxonid varchar NOT NULL,
    expected_label varchar NOT NULL
) ON COMMIT DROP;

-- These legacy Catalogue of Life names differ from their current dictionary
-- labels only by an author suffix or abbreviation.  Keeping them explicit
-- makes the non-exact mappings reviewable and prevents fuzzy name matching.
INSERT INTO christy_legacy_taxon_map (old_id, taxonid, expected_label) VALUES
    ('968088', '94JK',  'Acer saccharum Marshall'),
    ('45022',  '6Y6H',  'Prunus L.'),
    ('56941',  'PTB',   'Aesculus L.'),
    ('58212',  '3HXZ',  'Carya Nutt.'),
    ('4230',   '622TP', 'Asteraceae Dumort.'),
    ('89078',  '84MS',  'Ulmus L.'),
    ('4344',   '3HP',   'Malvales Juss. ex Bercht. & J. Presl'),
    ('100868', '8BDZ',  'Zygophyllum L.');

DO $$
DECLARE
    upgrade_rows integer;
    bad_mappings integer;
BEGIN
    SELECT count(*) INTO upgrade_rows
    FROM public.tblupgradelog
    WHERE filename = 'taxon-upgrade-6.sql'
      AND timestamp = timestamptz '2022-07-05 08:43:09.56057+00';

    IF upgrade_rows <> 1 THEN
        RAISE EXCEPTION
            'Christy repair preflight failed: expected the 2022-07-05 taxon-upgrade-6 log entry';
    END IF;

    SELECT count(*) INTO bad_mappings
    FROM christy_legacy_taxon_map AS mapping
    LEFT JOIN public.tlkptaxon AS taxon USING (taxonid)
    WHERE taxon.taxonid IS NULL
       OR taxon.label IS DISTINCT FROM mapping.expected_label;

    IF bad_mappings <> 0 THEN
        RAISE EXCEPTION
            'Christy repair preflight failed: % legacy mappings do not match tlkptaxon',
            bad_mappings;
    END IF;
END
$$;

CREATE TEMP TABLE christy_taxon_repair_latest ON COMMIT DROP AS
WITH upgrade AS (
    SELECT timestamp AS upgrade_time
    FROM public.tblupgradelog
    WHERE filename = 'taxon-upgrade-6.sql'
), raw_request AS (
    SELECT requestlogid,
           createdtimestamp,
           substring(request FROM '<c:request type="([^"]+)"') AS kind,
           substring(request FROM 'parentEntityID="([^"]+)"') AS parent_id,
           replace(
               substring(request FROM '<tridas:title>([^<]*)</tridas:title>'),
               '&amp;', '&'
           ) AS title,
           substring(
               request FROM '<tridas:identifier[^>]*>([^<]+)</tridas:identifier>'
           ) AS identifier,
           substring(
               request FROM '<tridas:taxon[^>]*normalId="([^"]*)"'
           ) AS old_id,
           replace(
               substring(request FROM '<tridas:taxon[^>]*normal="([^"]*)"'),
               '&amp;', '&'
           ) AS normal
    FROM public.tblrequestlog
    CROSS JOIN upgrade
    WHERE request ~* '<tridas:taxon'
      AND createdtimestamp < upgrade.upgrade_time
), dictionary_name_map AS (
    SELECT lower(btrim(label)) AS normal_key,
           min(taxonid) AS taxonid
    FROM public.tlkptaxon
    GROUP BY lower(btrim(label))
    HAVING count(DISTINCT taxonid) = 1
), mapped_request AS (
    SELECT request.*,
           coalesce(legacy.taxonid, dictionary.taxonid) AS mapped_taxonid,
           CASE
             WHEN legacy.taxonid IS NOT NULL THEN 'legacy override'
             WHEN dictionary.taxonid IS NOT NULL THEN 'exact dictionary label'
           END AS mapping_source
    FROM raw_request AS request
    LEFT JOIN christy_legacy_taxon_map AS legacy USING (old_id)
    LEFT JOIN dictionary_name_map AS dictionary
      ON dictionary.normal_key = lower(btrim(request.normal))
), update_event AS (
    SELECT request.requestlogid,
           request.createdtimestamp,
           request.old_id,
           request.normal,
           request.mapped_taxonid AS taxonid,
           request.mapping_source,
           element.elementid,
           'update'::varchar AS source,
           element.createdtimestamp AS element_createdtimestamp
    FROM mapped_request AS request
    JOIN public.tblelement AS element
      ON request.identifier = element.elementid::text
    WHERE request.kind = 'update'
      AND element.taxonid IS NULL
), create_match AS (
    SELECT request.requestlogid,
           request.createdtimestamp,
           request.old_id,
           request.normal,
           request.mapped_taxonid AS taxonid,
           request.mapping_source,
           element.elementid,
           'create'::varchar AS source,
           element.createdtimestamp AS element_createdtimestamp,
           row_number() OVER (
               PARTITION BY element.elementid
               ORDER BY abs(extract(epoch FROM
                            (request.createdtimestamp - element.createdtimestamp))),
                        request.requestlogid
           ) AS create_rank
    FROM mapped_request AS request
    JOIN public.tblelement AS element
      ON request.parent_id = element.objectid::text
     AND request.title = element.code
    WHERE request.kind = 'create'
      AND element.taxonid IS NULL
), create_event AS (
    SELECT requestlogid,
           createdtimestamp,
           old_id,
           normal,
           taxonid,
           mapping_source,
           elementid,
           source,
           element_createdtimestamp
    FROM create_match
    WHERE create_rank = 1
), ranked_event AS (
    SELECT event.*,
           row_number() OVER (
               PARTITION BY elementid
               ORDER BY requestlogid DESC
           ) AS event_rank
    FROM (
        SELECT * FROM update_event
        UNION ALL
        SELECT * FROM create_event
    ) AS event
)
SELECT requestlogid,
       createdtimestamp,
       old_id,
       normal,
       taxonid,
       mapping_source,
       elementid,
       source,
       element_createdtimestamp
FROM ranked_event
WHERE event_rank = 1;

CREATE UNIQUE INDEX ON christy_taxon_repair_latest (elementid);

\echo 'Christy request-log recovery evidence:'
SELECT source, mapping_source, count(*) AS elements
FROM christy_taxon_repair_latest
GROUP BY source, mapping_source
ORDER BY source, mapping_source;

SELECT count(*) AS null_elements,
       count(*) FILTER (WHERE taxonid IS NOT NULL) AS recoverable,
       count(*) FILTER (WHERE taxonid IS NULL) AS unresolved
FROM christy_taxon_repair_latest;

DO $$
DECLARE
    null_elements integer;
    evidence_rows integer;
    create_rows integer;
    update_rows integer;
    exact_rows integer;
    override_rows integer;
    bad_create_timestamps integer;
    missing_taxa integer;
BEGIN
    SELECT count(*) INTO null_elements
    FROM public.tblelement
    WHERE taxonid IS NULL;

    SELECT count(*),
           count(*) FILTER (WHERE source = 'create'),
           count(*) FILTER (WHERE source = 'update'),
           count(*) FILTER (WHERE mapping_source = 'exact dictionary label'),
           count(*) FILTER (WHERE mapping_source = 'legacy override'),
           count(*) FILTER (
               WHERE source = 'create'
                 AND abs(extract(epoch FROM
                         (createdtimestamp - element_createdtimestamp))) > 1
           )
      INTO evidence_rows,
           create_rows,
           update_rows,
           exact_rows,
           override_rows,
           bad_create_timestamps
    FROM christy_taxon_repair_latest
    WHERE taxonid IS NOT NULL;

    SELECT count(*) INTO missing_taxa
    FROM christy_taxon_repair_latest AS evidence
    LEFT JOIN public.tlkptaxon AS taxon USING (taxonid)
    WHERE evidence.taxonid IS NULL
       OR taxon.taxonid IS NULL;

    IF null_elements <> 813
       OR evidence_rows <> 813
       OR create_rows <> 676
       OR update_rows <> 137
       OR exact_rows <> 665
       OR override_rows <> 148
       OR bad_create_timestamps <> 0
       OR missing_taxa <> 0 THEN
        RAISE EXCEPTION
            'Christy repair fingerprint mismatch: null=%, evidence=%, create=%, update=%, exact=%, override=%, bad timestamps=%, missing taxa=%',
            null_elements, evidence_rows, create_rows, update_rows,
            exact_rows, override_rows, bad_create_timestamps, missing_taxa;
    END IF;
END
$$;

SET LOCAL client_min_messages = warning;

WITH updated AS (
    UPDATE public.tblelement AS element
       SET taxonid = evidence.taxonid
      FROM christy_taxon_repair_latest AS evidence
     WHERE element.elementid = evidence.elementid
       AND element.taxonid IS NULL
       AND evidence.taxonid IS NOT NULL
    RETURNING element.elementid
)
SELECT count(*) AS updated_elements FROM updated;

DO $$
DECLARE
    remaining integer;
BEGIN
    SELECT count(*) INTO remaining
    FROM public.tblelement AS element
    JOIN christy_taxon_repair_latest AS evidence USING (elementid)
    WHERE element.taxonid IS NULL;

    IF remaining <> 0 THEN
        RAISE EXCEPTION
            'Christy repair verification failed: % candidate elements remain NULL',
            remaining;
    END IF;
END
$$;

SELECT count(*) AS null_elements_after_update
FROM public.tblelement
WHERE taxonid IS NULL;

\if :apply
  \echo 'Applying Christy taxon repair (COMMIT).'
  COMMIT;
\else
  \echo 'Dry run complete; rolling back. Pass -v apply=1 to commit.'
  ROLLBACK;
\endif
