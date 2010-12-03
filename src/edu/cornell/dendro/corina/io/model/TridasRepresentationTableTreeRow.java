package edu.cornell.dendro.corina.io.model;

import javax.swing.tree.DefaultMutableTreeNode;

public class TridasRepresentationTableTreeRow {

	public DefaultMutableTreeNode node;
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
	
	public TridasRepresentationTableTreeRow(DefaultMutableTreeNode node, ImportStatus action)
	{
		this.node = node;
		this.action = action;
	}

	public TridasRepresentationTableTreeRow() {
		
	}
	
	
}
