package edu.cornell.dendro.corina.io.model;

import org.tridas.interfaces.ITridas;

public class TridasRepresentationTableTreeRow {

	public ITridas entity;
	public ImportStatus action;
	
	public enum ImportStatus{
		IGNORE("Ignored"),
		STORED_IN_DATABASE("Stored in database"),
		PENDING("Attention required"),
		UNSUPPORTED("Unsupported - ignored");
		
		String name;
		ImportStatus(String name)
		{
			this.name = name;
		}
		
		public String toString()
		{
			return this.name;
		}
	}
	
	public TridasRepresentationTableTreeRow(ITridas entity, ImportStatus action)
	{
		this.entity = entity;
		this.action = action;
	}

	public TridasRepresentationTableTreeRow() {
		
	}
	
	
}
