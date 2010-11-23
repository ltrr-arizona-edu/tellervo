package edu.cornell.dendro.corina.io.model;

import org.tridas.interfaces.ITridas;

public class TridasRepresentationTableTreeRow {

	public ITridas entity;
	public ImportAction action;
	
	public enum ImportAction{
		IGNORE,
		STORED_IN_DATABASE,
		PENDING;
	}
	
	public TridasRepresentationTableTreeRow(ITridas entity, ImportAction action)
	{
		this.entity = entity;
		this.action = action;
	}

	public TridasRepresentationTableTreeRow() {
		
	}
	
	
}
