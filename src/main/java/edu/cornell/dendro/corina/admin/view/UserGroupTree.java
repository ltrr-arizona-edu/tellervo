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
 *     Dan Girshovich
 ******************************************************************************/

package edu.cornell.dendro.corina.admin.view;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.InputEvent;
import java.io.IOException;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import edu.cornell.dendro.corina.admin.model.MyNode;
import edu.cornell.dendro.corina.admin.model.UserGroupAdminModel;
import edu.cornell.dendro.corina.schema.WSISecurityGroup;
import edu.cornell.dendro.corina.schema.WSISecurityUser;

public class UserGroupTree extends JTree implements TreeSelectionListener,
		DragGestureListener, DropTargetListener, DragSourceListener {

	private static final long serialVersionUID = 1L;
	private TreePath SelectedTreePath = null;
	private MyNode SelectedNode = null;
	private DragSource dragSource = null;

	/**
	 * An extension of JTree that allows for dragging and dropping nodes
	 * 
	 * @param root
	 *            the root node of the tree
	 * */
	public UserGroupTree(TreeNode root) {
		super(root);
		addTreeSelectionListener(this);
		dragSource = DragSource.getDefaultDragSource();
		DragGestureRecognizer dgr = dragSource
				.createDefaultDragGestureRecognizer(this,
						DnDConstants.ACTION_MOVE, this);

		// block the right mouse button
		dgr.setSourceActions(dgr.getSourceActions() & ~InputEvent.BUTTON3_MASK);

		// not sure why, but this needs to be here:
		new DropTarget(this, this);
	}

	/** DragGestureListener interface method */
	public void dragGestureRecognized(DragGestureEvent e) {
		MyNode dragNode = getSelectedNode();
		if (dragNode != null) {
			Cursor cursor = DragSource.DefaultMoveNoDrop;
			dragSource.startDrag(e, cursor, dragNode, this);
		}
	}

	public MyNode getSelectedNode() {
		return SelectedNode;
	}

	public void dragDropEnd(DragSourceDropEvent dsde) {	}

	public void dragEnter(DragSourceDragEvent dsde) { }

	public void dragOver(DragSourceDragEvent dsde) { }

	public void dropActionChanged(DragSourceDragEvent dsde) { }

	public void dragExit(DragSourceEvent dsde) { }

	public void drop(DropTargetDropEvent e) {
		try {
			//actually gets a MyNode object
			Transferable dropNode = e.getTransferable();

			// can only drop into groups
			if (!((MyNode) dropNode).getType().equals(MyNode.Type.GROUP)) {
				e.rejectDrop();
				return;
			}

			// get new parent node
			Point loc = e.getLocation();
			TreePath destinationPath = getPathForLocation(loc.x, loc.y);

			//check if it's a valid drop
			if(testDropTarget(destinationPath, SelectedTreePath)){
				
				Object childData = dropNode.getTransferData(null);
				
				MyNode newParent = (MyNode) destinationPath.getLastPathComponent();
				MyNode oldParent = (MyNode) getSelectedNode().getParent();
				MyNode newChild = new MyNode(childData);
	
				try {
					newParent.add(newChild);
					oldParent.remove(getSelectedNode());
					e.acceptDrop(DnDConstants.ACTION_MOVE);
				} catch (java.lang.IllegalStateException ils) {
					e.rejectDrop();
				}
	
				e.getDropTargetContext().dropComplete(true);
	
				// expand nodes appropriately - this probably isnt the best way...
				DefaultTreeModel model = (DefaultTreeModel) getModel();
				model.reload(oldParent);
				model.reload(newParent);
				TreePath parentPath = new TreePath(newParent.getPath());
				expandPath(parentPath);
			}
			else{
				e.rejectDrop();
			}
			
		} catch (IOException io) {
			e.rejectDrop();
		} catch (UnsupportedFlavorException ufe) {
			e.rejectDrop();
		}
		
	} // end of method

	/** DropTaregetListener interface method */
	public void dragEnter(DropTargetDragEvent e) {
	}

	/** DropTaregetListener interface method */
	public void dragExit(DropTargetEvent e) {
	}

	/** DropTaregetListener interface method */
	public void dragOver(DropTargetDragEvent e) {
		// set cursor location. Needed in setCursor method
		Point cursorLocationBis = e.getLocation();
		TreePath destinationPath = getPathForLocation(cursorLocationBis.x,
				cursorLocationBis.y);

		// if destination path is okay accept drop...
		if (testDropTarget(destinationPath, SelectedTreePath)) {
			e.acceptDrag(DnDConstants.ACTION_MOVE);
		}
		// ...otherwise reject drop
		else {
			e.rejectDrag();
		}
	}

	/** DropTaregetListener interface method */
	public void dropActionChanged(DropTargetDragEvent e) {
	}

	/** TreeSelectionListener - sets selected node */
	public void valueChanged(TreeSelectionEvent evt) {
		SelectedTreePath = evt.getNewLeadSelectionPath();
		if (SelectedTreePath == null) {
			SelectedNode = null;
			return;
		}
		SelectedNode = (MyNode) SelectedTreePath.getLastPathComponent();
	}

	private boolean testDropTarget(TreePath destination, TreePath beingDropped) {

		if (destination == null)
			return false;

		MyNode droppedOn = (MyNode) destination.getLastPathComponent();

		if (!droppedOn.getAllowsChildren() || destination.equals(beingDropped)
				|| beingDropped.isDescendant(destination)
				|| beingDropped.getParentPath().equals(destination)) {
			return false;
		} else {
			return true;
		}
	}
}