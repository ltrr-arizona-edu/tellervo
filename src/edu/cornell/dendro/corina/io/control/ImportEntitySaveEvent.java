package edu.cornell.dendro.corina.io.control;

import java.awt.Window;
import java.io.File;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.ObjectEvent;

import edu.cornell.dendro.corina.io.model.ImportEntityModel;
import edu.cornell.dendro.corina.io.model.ImportModel;

public class ImportEntitySaveEvent extends MVCEvent {

	private static final long serialVersionUID = 1L;
	
	public final ImportModel model;
	public final Window window;

	public ImportEntitySaveEvent(ImportModel model, Window window) {
		super(IOController.ENTITY_SAVE);
		this.model = model;
		this.window = window;
	}

}
