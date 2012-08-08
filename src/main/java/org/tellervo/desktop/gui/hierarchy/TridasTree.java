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
package org.tellervo.desktop.gui.hierarchy;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasGenericField;
import org.tridas.util.TridasObjectEx;

public class TridasTree extends JTree {

	private static final long serialVersionUID = 5358870985497921555L;

	
	public TridasTree(DefaultMutableTreeNode top) {
		super(top);
		
	}
 
	
	public String getToolTipString(Object value, boolean selected,
			boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
	
		return value.toString();
		
	}
	
	
	@Override
	public String convertValueToText(Object value, boolean selected,
		boolean expanded, boolean leaf, int row,
		boolean hasFocus) {
		
		if(value == null) return "";
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

		if(node.getUserObject() instanceof TridasObjectEx)
		{
			TridasObjectEx obj = ((TridasObjectEx)node.getUserObject());
			for(TridasGenericField gf : obj.getGenericFields())
			{
				if (gf.getName().equals("tellervo.objectLabCode"))
				{
					return gf.getValue() + " - "+obj.getTitle();
				}
			}
			return obj.getTitle();
		}
		else if(node.getUserObject() instanceof ITridas)
		{
		    return ((ITridas)node.getUserObject()).getTitle();
		}
		else 
		{
			return node.getUserObject().toString();
		}
		    

	}



}
