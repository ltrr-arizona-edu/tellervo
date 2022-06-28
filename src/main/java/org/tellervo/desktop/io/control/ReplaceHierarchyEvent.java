package org.tellervo.desktop.io.control;

import javax.swing.JFrame;
import javax.swing.tree.DefaultMutableTreeNode;

import org.tellervo.desktop.io.model.ImportModel;
import org.tellervo.desktop.io.model.TridasRepresentationTableTreeRow;
import org.tridas.interfaces.ITridas;

import com.dmurph.mvc.MVCEvent;


public class ReplaceHierarchyEvent extends MVCEvent {

	private static final long serialVersionUID = 1L;
	public final DefaultMutableTreeNode currentNode;
	public final ITridas newParent;
	public final ImportModel model;
	public final JFrame parentDialog;

	
	public ReplaceHierarchyEvent(ImportModel model, JFrame parentDialog, DefaultMutableTreeNode node, ITridas par) {
		super(IOController.REPLACE_HIERARCHY);
		
		newParent = par;
		currentNode = node;
		this.model = model;
		this.parentDialog = parentDialog;

		
	}

}
