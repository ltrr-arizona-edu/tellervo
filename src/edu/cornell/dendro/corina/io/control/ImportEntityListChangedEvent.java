package edu.cornell.dendro.corina.io.control;

import org.tridas.interfaces.ITridas;

import com.dmurph.mvc.ObjectEvent;

import edu.cornell.dendro.corina.io.model.ImportModel;

public class ImportEntityListChangedEvent extends ObjectEvent<ITridas> {

	private static final long serialVersionUID = 1L;
	public final ImportModel model;
	public final ITridas parentEntity;
	

	public ImportEntityListChangedEvent(ImportModel model, ITridas currentEntity, ITridas parentEntity) {
		super(ImportController.ENTITY_LIST_CHANGED, currentEntity);
		this.parentEntity = parentEntity;
		this.model = model;
	}

}
