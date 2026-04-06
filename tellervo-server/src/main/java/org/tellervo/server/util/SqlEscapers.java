package org.tellervo.server.util;

public final class SqlEscapers {

	private SqlEscapers() {
	}

	/**
	 * Preserves the legacy commons-lang escapeSql behavior while this code still
	 * builds SQL with string concatenation.
	 */
	public static String escapeSql(String value) {
		if (value == null) {
			return null;
		}
		return value.replace("'", "''");
	}
}
