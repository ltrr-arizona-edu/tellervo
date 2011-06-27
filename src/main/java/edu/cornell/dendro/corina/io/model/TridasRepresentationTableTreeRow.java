/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
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
