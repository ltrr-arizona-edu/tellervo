package edu.cornell.dendro.corina.io.control;

import com.dmurph.mvc.MVCEvent;

import edu.cornell.dendro.corina.io.model.ImportModel;
import edu.cornell.dendro.corina.io.model.TridasRepresentationTableTreeRow;

public class ImportEntitySelectedEvent extends MVCEvent {

	private static final long serialVersionUID = 1L;
	public final ImportModel model;
	public final TridasRepresentationTableTreeRow row;
	

	public ImportEntitySelectedEvent(TridasRepresentationTableTreeRow row, ImportModel model) {
		super(ImportController.ENTITY_SELECTED);
		this.row = row;
		this.model = model;
	}

}
