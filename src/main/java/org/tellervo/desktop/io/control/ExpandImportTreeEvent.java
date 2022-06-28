package org.tellervo.desktop.io.control;

import org.netbeans.swing.outline.Outline;
import org.tellervo.desktop.io.model.TridasRepresentationTreeModel;

import com.dmurph.mvc.MVCEvent;


public class ExpandImportTreeEvent extends MVCEvent {

	private static final long serialVersionUID = 1L;
	public Boolean expand;
	public TridasRepresentationTreeModel model;
	public Outline treeTable;
	
	public ExpandImportTreeEvent(Boolean expand, TridasRepresentationTreeModel model, Outline treeTable) {
		super(IOController.EXPAND_IMPORT_TREE);
		this.model = model;
		this.treeTable = treeTable;
		this.expand = expand;
	}

}
