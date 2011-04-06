package edu.cornell.dendro.corina.gui.dbbrowse;

import javax.swing.tree.DefaultMutableTreeNode;

import org.tridas.interfaces.ITridas;

public class TridasMutableTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = -3003692874339198418L;


	public TridasMutableTreeNode(ITridas entity) {
		super(entity);
		

	}
	


}
