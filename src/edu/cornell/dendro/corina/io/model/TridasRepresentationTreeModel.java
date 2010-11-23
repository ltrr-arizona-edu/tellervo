package edu.cornell.dendro.corina.io.model;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.netbeans.swing.outline.RowModel;

public class TridasRepresentationTreeModel extends DefaultTreeModel implements RowModel{

	private static final long serialVersionUID = 1L;
	private final DefaultMutableTreeNode root;
	
	public TridasRepresentationTreeModel() {
		super(new DefaultMutableTreeNode());
		root = (DefaultMutableTreeNode) getRoot();
	}

	@Override
	public Class getColumnClass(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getColumnName(int arg0) {
		switch (arg0)
		{
		case 0: return "TRiDaS Entity";
		case 1: return "Action";
		default:
			throw new RuntimeException("Index out of range for getColumnName()");
		}
	}

	@Override
	public Object getValueFor(Object arg0, int arg1) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) arg0;
		TridasRepresentationTableTreeRow row = (TridasRepresentationTableTreeRow) node.getUserObject();
		
		switch (arg1)
		{
		case 0: return node;
		case 1: return row.action;
		default:
			throw new RuntimeException("Index out of range for getValueFor()");
		}
	}

	@Override
	public boolean isCellEditable(Object arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setValueFor(Object arg0, int arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
