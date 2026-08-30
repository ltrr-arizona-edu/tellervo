#!/usr/bin/env python3
"""Fetch an auditable tlkptaxon supplement from the Catalogue of Life API."""

from __future__ import annotations

import argparse
from concurrent.futures import ThreadPoolExecutor, as_completed
import csv
import json
from pathlib import Path
import sys
from urllib.error import HTTPError, URLError
from urllib.request import Request, urlopen


RANK_IDS = {
    "kingdom": 1,
    "subkingdom": 2,
    "phylum": 3,
    "division": 4,
    "class": 5,
    "order": 6,
    "family": 7,
    "subfamily": 19,
    "genus": 8,
    "subgenus": 100,
    "section": 101,
    "subsection": 102,
    "species": 9,
    "subspecies": 10,
    "race": 11,
    "variety": 12,
    "subvariety": 13,
    "form": 14,
    "subform": 15,
}

# Two 2022-era identifiers are no longer retained even as name usages in the
# current dataset.  Preserve the historical IDs and labels recovered from
# tellervochristy's request log, while sourcing rank and parent relationships
# from their exact current Catalogue of Life matches.
FALLBACK_MATCHES = {
    "7NHJZ": {
        "current_id": "VK8LK",
        "label": "Malus domestica (Suckow) Borkh.",
        "labelHtml": "<i>Malus domestica</i> (Suckow) Borkh.",
    },
    "8WRQD": {
        "current_id": "9L569",
        "label": "Betula dauurica Pall.",
        "labelHtml": "<i>Betula dauurica</i> Pall.",
    },
}


def parse_args():
    parser = argparse.ArgumentParser()
    parser.add_argument("ids", type=Path, help="one Catalogue of Life ID per line")
    parser.add_argument("output", type=Path, help="CSV file to create")
    parser.add_argument("--dataset", default="3LR",
                        help="Catalogue of Life API dataset key (default: 3LR)")
    parser.add_argument("--workers", type=int, default=8)
    return parser.parse_args()


def request_json(url: str):
    request = Request(url, headers={"User-Agent": "Tellervo-taxon-repair/1.0"})
    with urlopen(request, timeout=30) as response:
        return json.load(response)


def fetch(dataset: str, taxon_id: str):
    base = f"https://api.catalogueoflife.org/dataset/{dataset}"
    url = f"{base}/taxon/{taxon_id}"
    try:
        record = request_json(url)
    except HTTPError as exc:
        if exc.code != 404:
            raise RuntimeError(f"{taxon_id}: API request failed: {exc}") from exc
        # Removed/synonymized IDs are absent from the current taxon tree but
        # remain available as name usages.  Keep the historical ID and record
        # its current Catalogue of Life status and relationship.
        url = f"{base}/nameusage/{taxon_id}"
        try:
            record = request_json(url)
        except HTTPError as fallback_exc:
            fallback = FALLBACK_MATCHES.get(taxon_id)
            if fallback_exc.code != 404 or fallback is None:
                raise RuntimeError(
                    f"{taxon_id}: taxon and nameusage API requests failed: {fallback_exc}"
                ) from fallback_exc
            url = f"{base}/taxon/{fallback['current_id']}"
            try:
                record = request_json(url)
            except (HTTPError, URLError, TimeoutError) as match_exc:
                raise RuntimeError(
                    f"{taxon_id}: current exact-match API request failed: {match_exc}"
                ) from match_exc
            record["label"] = fallback["label"]
            record["labelHtml"] = fallback["labelHtml"]
            record["status"] = (
                f"historical ID; current accepted match {fallback['current_id']}")
        except (URLError, TimeoutError) as fallback_exc:
            raise RuntimeError(
                f"{taxon_id}: nameusage API request failed: {fallback_exc}"
            ) from fallback_exc
    except (URLError, TimeoutError) as exc:
        raise RuntimeError(f"{taxon_id}: API request failed: {exc}") from exc

    expected_id = FALLBACK_MATCHES.get(taxon_id, {}).get("current_id", taxon_id)
    if record.get("id") != expected_id:
        raise RuntimeError(f"{taxon_id}: API returned ID {record.get('id')!r}")
    rank = record.get("name", {}).get("rank")
    if rank not in RANK_IDS:
        raise RuntimeError(f"{taxon_id}: unsupported rank {rank!r}")
    required = ("parentId", "label", "labelHtml")
    missing = [field for field in required if not record.get(field)]
    if missing:
        raise RuntimeError(f"{taxon_id}: missing {', '.join(missing)}")

    return {
        "taxonid": taxon_id,
        "colid": taxon_id,
        "colparentid": record["parentId"],
        "taxonrankid": RANK_IDS[rank],
        "label": record["label"],
        "htmllabel": record["labelHtml"],
        "parenttaxonid": record["parentId"],
        "status": record.get("status", ""),
        "dataset_key": record.get("datasetKey", ""),
        "source_url": url,
    }


def main() -> int:
    args = parse_args()
    if args.output.exists():
        raise SystemExit(f"refusing to overwrite existing output: {args.output}")
    ids = [line.strip() for line in args.ids.read_text().splitlines()
           if line.strip() and not line.lstrip().startswith("#")]
    if len(ids) != len(set(ids)):
        raise SystemExit("input contains duplicate IDs")

    records = []
    errors = []
    with ThreadPoolExecutor(max_workers=args.workers) as executor:
        futures = {executor.submit(fetch, args.dataset, taxon_id): taxon_id
                   for taxon_id in ids}
        for future in as_completed(futures):
            try:
                records.append(future.result())
            except RuntimeError as exc:
                errors.append(str(exc))
    if errors:
        raise SystemExit("\n".join(sorted(errors)))

    records.sort(key=lambda row: row["taxonid"])
    fieldnames = list(records[0])
    with args.output.open("w", encoding="utf-8", newline="") as stream:
        writer = csv.DictWriter(stream, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(records)
    statuses = sorted({record["status"] for record in records})
    print(f"Created {args.output} with {len(records)} rows; statuses={statuses}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
