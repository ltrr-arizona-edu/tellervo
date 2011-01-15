package edu.cornell.dendro.corina.io.control;

import com.dmurph.mvc.ObjectEvent;

import edu.cornell.dendro.corina.io.model.ImportModel;
import edu.cornell.dendro.corina.io.model.TridasRepresentationTableTreeRow;

public class ImportSwapEntityEvent extends ObjectEvent<TridasRepresentationTableTreeRow> {

	private static final long serialVersionUID = 1L;
	public final ImportModel model;
	public final TridasRepresentationTableTreeRow oldRow;
	public ImportSwapEntityEvent(ImportModel model, TridasRepresentationTableTreeRow newRow, TridasRepresentationTableTreeRow oldRow) {
		super(IOController.ENTITY_SWAPPED, newRow);
		this.model = model;
		this.oldRow = oldRow;
	}

}
