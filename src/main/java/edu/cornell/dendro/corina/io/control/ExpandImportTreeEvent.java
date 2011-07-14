package edu.cornell.dendro.corina.io.control;

import org.netbeans.swing.outline.Outline;

import com.dmurph.mvc.MVCEvent;

import edu.cornell.dendro.corina.io.model.TridasRepresentationTreeModel;

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
