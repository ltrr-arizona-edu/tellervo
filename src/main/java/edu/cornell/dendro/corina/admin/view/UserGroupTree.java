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

import edu.cornell.dendro.corina.admin.model.GroupNode;
import edu.cornell.dendro.corina.admin.model.MyNode;
import edu.cornell.dendro.corina.admin.model.UserNode;
import edu.cornell.dendro.corina.admin.model.UserGroupAdminModel;

public class UserGroupTree extends JTree implements TreeSelectionListener, DragGestureListener, DropTargetListener, DragSourceListener {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TreePath SelectedTreePath = null;
	private MyNode SelectedNode = null;
	private DragSource dragSource = null;
	
	public UserGroupTree(){
		
		addTreeSelectionListener(this);
	    dragSource = DragSource.getDefaultDragSource();
	    DragGestureRecognizer dgr = dragSource.createDefaultDragGestureRecognizer(
	        this, DnDConstants.ACTION_MOVE, this                              
	      );
	    
	    dgr.setSourceActions(dgr.getSourceActions() & ~InputEvent.BUTTON3_MASK);
	}
	
	public UserGroupTree(TreeNode root) {
		super(root);
	}

	/** DragGestureListener interface method */
	  public void dragGestureRecognized(DragGestureEvent e) {
		System.out.println("drag recognized");
	    //Get the selected node
	    MyNode dragNode = getSelectedNode();
	    if (dragNode != null) {

	      //Get the Transferable Object
	      Transferable transferable = (Transferable) dragNode.getUserObject();

	      //Select the appropriate cursor;
	      Cursor cursor = DragSource.DefaultMoveNoDrop;	   
	      dragSource.startDrag(e, cursor, transferable, this);
	    }
	  }

	  public MyNode getSelectedNode() {
		    return SelectedNode;
	  }
	  
	  /** DragSourceListener interface method */
	  public void dragDropEnd(DragSourceDropEvent dsde) {
	  }

	  /** DragSourceListener interface method */
	  public void dragEnter(DragSourceDragEvent dsde) {
	  }

	  /** DragSourceListener interface method */
	  public void dragOver(DragSourceDragEvent dsde) {
	  }

	  /** DragSourceListener interface method */
	  public void dropActionChanged(DragSourceDragEvent dsde) {
	  }

	  /** DragSourceListener interface method */
	  public void dragExit(DragSourceEvent dsde) {
	  }
	  
	  /** DropTargetListener interface method - What we do when drag is released */
	  public void drop(DropTargetDropEvent e) {
	    try {
	      Transferable tr = e.getTransferable();

	      //can only drop into groups
	      if (!tr.isDataFlavorSupported(GroupNode.ID_FLAVOR)){
	    	  e.rejectDrop();
	    	  return;
	      }

	      Object child = tr.getTransferData(UserNode.ID_FLAVOR);

	      //get new parent node
	      Point loc = e.getLocation();
	      TreePath destinationPath = getPathForLocation(loc.x, loc.y);

	      final String msg = testDropTarget(destinationPath, SelectedTreePath);
	      if (msg != null) {
	        e.rejectDrop();
	        return;
	      }

	      MyNode newParent = (MyNode) destinationPath.getLastPathComponent();

	      //get old parent node
	      MyNode oldParent = (MyNode) getSelectedNode().getParent();

	      //make new child node
	      MyNode newChild = new MyNode(child);

	      try {
	        newParent.add(newChild);
	        oldParent.remove(getSelectedNode());
	        e.acceptDrop (DnDConstants.ACTION_MOVE);
	      }
	      catch (java.lang.IllegalStateException ils) {
	        e.rejectDrop();
	      }

	      e.getDropTargetContext().dropComplete(true);

	      //expand nodes appropriately - this probably isnt the best way...
	      DefaultTreeModel model = (DefaultTreeModel) getModel();
	      model.reload(oldParent);
	      model.reload(newParent);
	      TreePath parentPath = new TreePath(newParent.getPath());
	      expandPath(parentPath);
	    }
	    catch (IOException io) { e.rejectDrop(); }
	    catch (UnsupportedFlavorException ufe) {e.rejectDrop();}
	  } //end of method


	  /** DropTaregetListener interface method */
	  public void dragEnter(DropTargetDragEvent e) {
	  }

	  /** DropTaregetListener interface method */
	  public void dragExit(DropTargetEvent e) { 
	  }

	  /** DropTaregetListener interface method */
	  public void dragOver(DropTargetDragEvent e) {
	    //set cursor location. Needed in setCursor method
	    Point cursorLocationBis = e.getLocation();
	        TreePath destinationPath = 
	      getPathForLocation(cursorLocationBis.x, cursorLocationBis.y);

	    // if destination path is okay accept drop...
	    if (testDropTarget(destinationPath, SelectedTreePath) == null){
	    	e.acceptDrag(DnDConstants.ACTION_MOVE ) ;
	    }
	    // ...otherwise reject drop
	    else {
	    	e.rejectDrag() ;
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
	    SelectedNode = (MyNode)SelectedTreePath.getLastPathComponent();
	  }

	  /** Convenience method to test whether drop location is valid
	  @param destination The destination path 
	  @param dropper The path for the node to be dropped
	  @return null if no problems, otherwise an explanation
	  */
	  private String testDropTarget(TreePath destination, TreePath dropper) {
	    //Typical Tests for dropping
	 
	    //Test 1.
	    boolean destinationPathIsNull = destination == null;
	    if (destinationPathIsNull) 
	      return "Invalid drop location.";

	    //Test 2.
	    MyNode node = (MyNode) destination.getLastPathComponent();
	    if ( !node.getAllowsChildren() )
	      return "This node does not allow children";

	    if (destination.equals(dropper))
	      return "Destination cannot be same as source";

	    //Test 3.
	    if ( dropper.isDescendant(destination)) 
	       return "Destination node cannot be a descendant.";

	    //Test 4.
	    if ( dropper.getParentPath().equals(destination)) 
	       return "Destination node cannot be a parent.";

	    return null;
	  }
}