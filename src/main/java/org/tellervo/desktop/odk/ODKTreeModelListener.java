package org.tellervo.desktop.odk;

import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

public class ODKTreeModelListener implements TreeModelListener {

	JTree myTree;
	
	public ODKTreeModelListener(JTree myTree)
	{
		this.myTree = myTree;
	}
	
	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void treeNodesInserted(TreeModelEvent e) {
		
		myTree.expandPath(e.getTreePath());
		
		myTree.setSelectionPath(e.getTreePath());
	}

	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		// TODO Auto-generated method stub

	}

}
