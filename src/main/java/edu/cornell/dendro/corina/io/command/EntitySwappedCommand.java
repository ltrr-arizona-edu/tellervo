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
package edu.cornell.dendro.corina.io.command;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.io.control.ImportNodeSelectedEvent;
import edu.cornell.dendro.corina.io.control.ImportSwapEntityEvent;
import edu.cornell.dendro.corina.io.model.TridasRepresentationTableTreeRow;

public class EntitySwappedCommand implements ICommand {

	@Override
	public void execute(MVCEvent argEvent) {
		ImportSwapEntityEvent event = (ImportSwapEntityEvent) argEvent;

		// Get the tree node representing the entity we're going to replace
		DefaultMutableTreeNode oldNode = event.oldRow.getDefaultMutableTreeNode();
		
		// Get the tree node representing the parent of the entity we're replacing
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) event.oldRow.getDefaultMutableTreeNode().getParent();
		
		// Get the tree node representing the new entity that we now want
		DefaultMutableTreeNode newNode = event.getValue().getDefaultMutableTreeNode();
		
		// Calculate the index for the node we're replacing
		int newNodeIndex = parentNode.getIndex(oldNode);
		
		// Loop through the old node cloning across all its children onto the new node
		// otherwise all the child enties will be lost
		for(int i=0; i<oldNode.getChildCount(); i++)
		{
			try {
				copySubTree(newNode, oldNode);
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Go ahead and remove the old node
		event.model.getTreeModel().removeNodeFromParent((MutableTreeNode) event.oldRow.getDefaultMutableTreeNode());
		
		// Add new node in its place
		event.model.getTreeModel().insertNodeInto(newNode, parentNode, newNodeIndex);
		
		if(event.selectNodeAfterSwap)
		{
			// Select the new node
			TridasRepresentationTableTreeRow row = new TridasRepresentationTableTreeRow(newNode, null);	
			ImportNodeSelectedEvent event2 = new ImportNodeSelectedEvent(event.model, row);
			event2.dispatch();
		}
		
	}

	private DefaultMutableTreeNode copySubTree(DefaultMutableTreeNode destNode, DefaultMutableTreeNode srcNode) throws CloneNotSupportedException
	{
	    if (srcNode == null)
	    {
	        return destNode;
	    }
	    for (int i = 0; i < srcNode.getChildCount(); i++)
	    {
	        DefaultMutableTreeNode child = (DefaultMutableTreeNode)srcNode.getChildAt(i);
	        DefaultMutableTreeNode clone = new DefaultMutableTreeNode(child.getUserObject()); // better than toString()
	        destNode.add(clone);
	        copySubTree(clone, child);
	    }
	    return destNode;
	 }
	
	
}
