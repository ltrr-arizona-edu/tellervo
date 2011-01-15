package edu.cornell.dendro.corina.io.control;

import java.io.File;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.ObjectEvent;

import edu.cornell.dendro.corina.io.model.ImportModel;

public class FileSelectedEvent extends ObjectEvent<File> {

	private static final long serialVersionUID = 1L;
	
	public final ImportModel model;
	public final File file;
	public final String fileType;
	

	public FileSelectedEvent(ImportModel model, File file, String fileType) {
		super(IOController.FILE_SELECTED, file);
		this.model = model;
		this.file = file;
		this.fileType = fileType;
	}

}
