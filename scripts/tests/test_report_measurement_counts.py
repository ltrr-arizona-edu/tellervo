import importlib.util
from pathlib import Path
import sys
import unittest


SCRIPT = Path(__file__).resolve().parents[1] / "report-measurement-counts.py"
SPEC = importlib.util.spec_from_file_location("measurement_count_report", SCRIPT)
report = importlib.util.module_from_spec(SPEC)
sys.modules[SPEC.name] = report
SPEC.loader.exec_module(report)


class FakePsql:
    def __init__(self, responses):
        self.responses = iter(responses)

    def query(self, database, sql):
        response = next(self.responses)
        if isinstance(response, Exception):
            raise response
        return response


class MeasurementCountReportTest(unittest.TestCase):
    def test_discovers_only_matching_database_prefix(self):
        psql = FakePsql([["postgres", "tellervoa", "tellervob", "unrelated"]])
        self.assertEqual(
            ["tellervoa", "tellervob"],
            report.discover_databases(psql, "postgres", "tellervo"),
        )

    def test_counts_both_tables(self):
        result = report.count_database(FakePsql([["true\ttrue"], ["123\t45"]]), "tellervoa")
        self.assertEqual("OK", result.status)
        self.assertEqual(123, result.tblmeasurement)
        self.assertEqual(45, result.tblvmeasurement)

    def test_reports_missing_table_without_running_count_query(self):
        result = report.count_database(FakePsql([["true\tfalse"]]), "tellervoa")
        self.assertEqual("MISSING", result.status)
        self.assertIn("public.tblvmeasurement", result.detail)

    def test_reports_database_query_error(self):
        result = report.count_database(
            FakePsql([report.PsqlError("permission denied")]), "tellervoa")
        self.assertEqual("ERROR", result.status)
        self.assertEqual("permission denied", result.detail)


if __name__ == "__main__":
    unittest.main()
