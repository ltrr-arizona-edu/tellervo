package edu.cornell.dendro.corina.io.control;

import org.tridas.interfaces.ITridas;

import com.dmurph.mvc.ObjectEvent;

import edu.cornell.dendro.corina.io.model.ImportModel;
import edu.cornell.dendro.corina.io.model.TridasRepresentationTableTreeRow;

public class ImportMergeEntitiesEvent extends ObjectEvent<Class<? extends ITridas>> {

	private static final long serialVersionUID = 1L;
	public final ImportModel model;
	

	public ImportMergeEntitiesEvent(ImportModel model, Class<? extends ITridas> clazz) {
		super(ImportController.MERGE_ENTITIES, clazz);
		this.model = model;
	}

}
