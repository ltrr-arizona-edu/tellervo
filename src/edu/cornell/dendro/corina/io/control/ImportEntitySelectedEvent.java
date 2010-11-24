package edu.cornell.dendro.corina.io.control;

import com.dmurph.mvc.ObjectEvent;

import edu.cornell.dendro.corina.io.model.ImportModel;
import edu.cornell.dendro.corina.io.model.TridasRepresentationTableTreeRow;

public class ImportEntitySelectedEvent extends ObjectEvent<TridasRepresentationTableTreeRow> {

	private static final long serialVersionUID = 1L;
	public final ImportModel model;
	

	public ImportEntitySelectedEvent(ImportModel model, TridasRepresentationTableTreeRow row) {
		super(ImportController.ENTITY_SELECTED, row);
		this.model = model;
	}

}
