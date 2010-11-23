package edu.cornell.dendro.corina.io.command;

import java.io.IOException;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.io.control.FileSelectedEvent;
import edu.cornell.dendro.corina.io.control.ImportEntitySelectedEvent;
import edu.cornell.dendro.corina.ui.Alert;

public class FileSelectedCommand implements ICommand {

	@Override
	public void execute(MVCEvent argEvent) {
		FileSelectedEvent event = (FileSelectedEvent) argEvent;
		try {
			event.model.setFileToImport(event.file);
		} catch (IOException e) {
			Alert.errorLoading(event.file.getAbsolutePath(), e);
		}
	}

}
