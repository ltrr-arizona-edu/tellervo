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
package org.tellervo.desktop.io.command;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.io.control.ImportNodeSelectedEvent;
import org.tellervo.desktop.io.control.ImportSwapEntityEvent;
import org.tellervo.desktop.io.model.TridasRepresentationTableTreeRow;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;


public class EntitySwappedCommand implements ICommand {
	private final static Logger log = LoggerFactory.getLogger(EntitySwappedCommand.class);

	@Override
	public void execute(MVCEvent argEvent) {
		
		log.debug("Executing EntitySwappedCommand");
		
		try {
			log.debug("splitOff() called in EntitySwappedCommand");
	        MVC.splitOff(); // so other mvc events can execute
		} catch (IllegalThreadException e) {
				log.error("splitOff() called from non-MVC thread");
		        e.printStackTrace();
		} catch (IncorrectThreadException e) {
				log.error("splitOff() called, but this is not the main thread");
				e.printStackTrace();
		}
		
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
		
		log.debug("Completed executing EntitySwappedCommand");
		
	}

	public static DefaultMutableTreeNode copySubTree(DefaultMutableTreeNode destNode, DefaultMutableTreeNode srcNode) throws CloneNotSupportedException
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
