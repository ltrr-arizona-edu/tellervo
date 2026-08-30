import importlib.util
from pathlib import Path
import sys
import unittest


SCRIPT = Path(__file__).resolve().parents[1] / "generate-corrupt-tlkptaxon-repair.py"
SPEC = importlib.util.spec_from_file_location("taxon_repair_generator", SCRIPT)
generator = importlib.util.module_from_spec(SPEC)
sys.modules[SPEC.name] = generator
SPEC.loader.exec_module(generator)


def row(taxon_id, rank, label, htmllabel, parent_id, colid=None,
        colparent_id=None):
    return {
        "taxonid": taxon_id,
        "colid": taxon_id if colid is None else colid,
        "taxonrankid": str(rank),
        "label": label,
        "htmllabel": htmllabel,
        "parenttaxonid": parent_id,
        "colparentid": parent_id if colparent_id is None else colparent_id,
    }


def legacy(taxon_id, rank):
    return row("legacy-" + taxon_id, rank, "n/a", "n/a", "n/a")


class RepairActionTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.canonical, cls.new_to_old = generator.parse_upgrade()

    def build(self, rows):
        return generator.build_actions(rows, self.canonical, self.new_to_old)

    def test_restores_unoccupied_legacy_id(self):
        actions, discards = self.build([legacy("5KWC", 8)])
        self.assertEqual([], discards)
        self.assertEqual([("legacy-5KWC", "5KWC", "restore")], [
            (action.source_id, action.target_id, action.action) for action in actions])

    def test_moves_displaced_row_before_restoring_legacy_id(self):
        rows = [
            legacy("5KWC", 8),
            row("5KWC", 6, "Manilkara", "<i>Manilkara</i>", "FY4"),
        ]
        actions, _ = self.build(rows)
        self.assertEqual(
            {("5KWC", "383", "move"), ("legacy-5KWC", "5KWC", "restore")},
            {(action.source_id, action.target_id, action.action) for action in actions},
        )

    def test_deduplicates_later_upgrade_row(self):
        rows = [
            legacy("949X", 9),
            row("949X", 9, "Acer glabrum Torr.",
                "<i>Acer glabrum</i> Torr.", "MLD"),
        ]
        actions, _ = self.build(rows)
        self.assertEqual("deduplicate", actions[0].action)

    def test_merges_displaced_row_when_target_already_exists(self):
        rows = [
            legacy("63SMF", 8),
            row("63SMF", 7, "Sequoia", "<i>Sequoia</i>", "8SY"),
            row("649", 7, "Aextoxicaceae Engl. & Gilg",
                "Aextoxicaceae Engl. & Gilg", "T8"),
        ]
        actions, _ = self.build(rows)
        self.assertEqual(
            {("63SMF", "649", "merge"),
             ("legacy-63SMF", "63SMF", "restore")},
            {(action.source_id, action.target_id, action.action) for action in actions},
        )

    def test_discards_only_exact_empty_placeholder(self):
        actions, discards = self.build([
            row("legacy-", 9, "n/a", "n/a", "n/a"),
        ])
        self.assertEqual([], actions)
        self.assertEqual(["legacy-"], discards)

    def test_refuses_unknown_custom_taxon(self):
        with self.assertRaises(SystemExit):
            self.build([legacy("CUSTOM-ID", 9)])


if __name__ == "__main__":
    unittest.main()
