package edu.cornell.dendro.corina.io.model;

import javax.swing.tree.DefaultMutableTreeNode;

import org.tridas.interfaces.ITridas;

public class TridasRepresentationTableTreeRow {

	public DefaultMutableTreeNode node;
	public ImportStatus action = ImportStatus.UNKNOWN;
	public ImportEntityModel model;
	
	public enum ImportStatus{
		IGNORE("Ignored"),
		STORED_IN_DATABASE("Stored in database"),
		PENDING("Attention required"),
		UNSUPPORTED("Unsupported - ignored"),
		UNKNOWN("Unknown");
		
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
	
	public TridasRepresentationTableTreeRow(DefaultMutableTreeNode node, 
			ImportStatus action)
	{
		this.node   = node;
		
		if(action!=null) this.action = action;
		
		ITridas entity = (ITridas) node.getUserObject();
		ITridas parent = null;
		try{
			parent = (ITridas) ((DefaultMutableTreeNode)node.getParent()).getUserObject();
		} catch (Exception e)
		{
			
		}
		
		this.model  = new ImportEntityModel(entity, parent);
	}

	public TridasRepresentationTableTreeRow() {
		
	}
	
	
}
