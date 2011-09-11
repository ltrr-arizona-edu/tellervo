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
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import edu.cornell.dendro.corina.admin.control.UpdateUserEvent;
import edu.cornell.dendro.corina.admin.model.MyNode;
import edu.cornell.dendro.corina.admin.model.TransferableGroup;
import edu.cornell.dendro.corina.admin.model.TransferableUser;
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
	        Transferable transferable = (Transferable) dragNode.getUserObject();
			Cursor cursor = DragSource.DefaultMoveNoDrop;
			dragSource.startDrag(e, cursor, transferable, this);
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
		
		// get new parent node
		Point loc = e.getLocation();
		TreePath destinationPath = getPathForLocation(loc.x, loc.y);
		MyNode newParent = ((MyNode) destinationPath.getLastPathComponent());

		//check if it's a valid drop
		if(testDropTarget(destinationPath, SelectedTreePath)){
			
			MyNode newChild = (MyNode) getSelectedNode();
			MyNode oldParent = (MyNode) getSelectedNode().getParent();
			
			try {
				//TODO: add a check so if the request fails, the tree doesn't change
				//makeChange(newChild, newParent, oldParent);
				oldParent.remove(getSelectedNode());
				newParent.add(newChild);
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
		
	}

	//sends the command to update the user/group structure after a drop
	private void makeChange(MyNode newChild, MyNode newParent, MyNode oldParent) {
		if(newChild.getType().equals(MyNode.Type.USER)){
			ArrayList<WSISecurityGroup> oldMemList = new ArrayList<WSISecurityGroup>();
			ArrayList<WSISecurityGroup> newMemList = new ArrayList<WSISecurityGroup>();
			oldMemList.add(((TransferableGroup) oldParent.getData()).getGroup());
			newMemList.add(((TransferableGroup) newParent.getData()).getGroup());
			new UpdateUserEvent(((TransferableUser) newChild.getData()).getUser(), oldMemList, newMemList, new JDialog()).dispatch();
		}
	}

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

	private boolean testDropTarget(TreePath destination, TreePath beingDroppedPath) {

		if (destination == null)
			return false;

		MyNode droppedOn = (MyNode) destination.getLastPathComponent();
		MyNode beingDropped = (MyNode) beingDroppedPath.getLastPathComponent();

		if (!droppedOn.getAllowsChildren() || destination.equals(beingDroppedPath)
				|| beingDroppedPath.isDescendant(destination)
				|| beingDroppedPath.getParentPath().equals(destination)
				|| droppedOn.getRestrictedChildTypes().contains(beingDropped.getType())) {
			return false;
		} else {
			return true;
		}
	}
}