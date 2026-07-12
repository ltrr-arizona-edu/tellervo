/*
 * Copyright (c) 2004-2026, PostgreSQL Global Development Group
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package org.postgresql.pljava;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Compile-time contract for a PL/Java function that returns a result set.
 *
 * <p>Tellervo only needs this API contract. Keeping it locally avoids depending
 * on the obsolete {@code postgresql:pljava-public} implementation artifact.</p>
 */
public interface ResultSetHandle {

	ResultSet getResultSet() throws SQLException;

	void close() throws SQLException;
}
