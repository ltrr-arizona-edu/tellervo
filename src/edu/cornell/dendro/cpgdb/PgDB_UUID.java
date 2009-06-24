package edu.cornell.dendro.cpgdb;

import java.sql.SQLException;
import java.util.UUID;

import org.postgresql.util.PGobject;

/**
 * Ridiculous class to work around the fact that there
 * is no native UUID type to JDBC and postgresql can't
 * convert "text" to uuids.
 * 
 * @author Lucas Madar
 *
 */

public class PgDB_UUID extends PGobject {
	private static final long serialVersionUID = 1L;

	private PgDB_UUID(String uuidstr) throws SQLException {
		super();
		
		this.setType("uuid");
		this.setValue(uuidstr);
	}

	private PgDB_UUID(UUID uuid) throws SQLException {
		super();
		
		this.setType("uuid");
		this.setValue((uuid == null) ? null : uuid.toString());
	}
	
	@Override
	public String toString() {
		return this.getValue();
	}
	
	public static PgDB_UUID fromUUID(UUID uuid) throws SQLException {
		return new PgDB_UUID(uuid);
	}
	
	public static PgDB_UUID fromString(String uuid) throws SQLException {
		return new PgDB_UUID(uuid);
	}
	
	public static PgDB_UUID randomInstance() throws SQLException {
		return new PgDB_UUID(UUID.randomUUID());
	}
}
