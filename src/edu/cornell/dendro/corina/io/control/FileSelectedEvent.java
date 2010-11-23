package edu.cornell.dendro.corina.io.control;

import java.io.File;

import com.dmurph.mvc.MVCEvent;

import edu.cornell.dendro.corina.io.model.ImportModel;

public class FileSelectedEvent extends MVCEvent {

	private static final long serialVersionUID = 1L;
	public final File file;
	public final ImportModel model;

	public FileSelectedEvent(File file, ImportModel model) {
		super(ImportController.FILE_SELECTED);
		this.file = file;
		this.model = model;
	}

}
