import argparse
import importlib.util
from pathlib import Path
import sys
import tempfile
import unittest


SCRIPT = Path(__file__).resolve().parents[1] / "compile-taxon-evidence.py"
SPEC = importlib.util.spec_from_file_location("compile_taxon_evidence", SCRIPT)
MODULE = importlib.util.module_from_spec(SPEC)
assert SPEC.loader is not None
sys.modules[SPEC.name] = MODULE
SPEC.loader.exec_module(MODULE)


class CompileTaxonEvidenceTests(unittest.TestCase):
    def test_normalize_name_decodes_xml_and_whitespace(self):
        self.assertEqual(
            MODULE.normalize_name("  Pinus   densiflora Siebold &amp; Zucc. "),
            "pinus densiflora siebold & zucc.",
        )

    def test_resolve_requests_uses_current_id_exact_name_and_alias(self):
        requests = [
            MODULE.RawRequestEvidence("db", "00000000-0000-0000-0000-000000000001", "NEW", "New name", "one", "2020-01-01+00"),
            MODULE.RawRequestEvidence("db", "00000000-0000-0000-0000-000000000002", "OLD", "Exact label", "two", "2020-01-02+00"),
            MODULE.RawRequestEvidence("db", "00000000-0000-0000-0000-000000000003", "ALIAS", "Former name", "three", "2020-01-03+00"),
        ]
        resolved, unresolved = MODULE.resolve_requests(
            requests,
            [("NEW", "New name"), ("EXACT", "Exact label"), ("TARGET", "Target label")],
            {"ALIAS": "TARGET"},
        )
        self.assertEqual([row.taxonid for row in resolved], ["NEW", "EXACT", "TARGET"])
        self.assertEqual(unresolved, [])

    def test_resolve_requests_rejects_ambiguous_name(self):
        request = MODULE.RawRequestEvidence(
            "db", "00000000-0000-0000-0000-000000000001",
            "OLD", "Same label", "source", "2020-01-01+00",
        )
        resolved, unresolved = MODULE.resolve_requests(
            [request], [("A", "Same label"), ("B", "Same label")], {},
        )
        self.assertEqual(resolved, [])
        self.assertEqual(unresolved, [request])

    def test_load_aliases_rejects_conflicts(self):
        with tempfile.TemporaryDirectory() as directory:
            path = Path(directory) / "aliases.csv"
            path.write_text(
                "raw_taxonid,taxonid\nOLD,A\nOLD,B\n", encoding="utf-8")
            with self.assertRaisesRegex(ValueError, "conflicting aliases"):
                MODULE.load_aliases(path)

    def test_destination_sql_creates_latest_and_conflict_views(self):
        evidence = [MODULE.Evidence(
            "00000000-0000-0000-0000-000000000001",
            "TAXON", "db:tblelement", "2020-01-01 00:00:00+00",
        )]
        sql = MODULE.destination_sql("taxon_recovery", "evidence", evidence, True)
        self.assertIn("CREATE VIEW taxon_recovery.evidence_latest", sql)
        self.assertIn("CREATE VIEW taxon_recovery.evidence_conflicts", sql)
        self.assertIn("00000000-0000-0000-0000-000000000001,TAXON", sql)

    def test_verbose_flag_is_repeatable(self):
        args = MODULE.parse_args(["-vv"])
        self.assertEqual(args.verbose, 2)


if __name__ == "__main__":
    unittest.main()
