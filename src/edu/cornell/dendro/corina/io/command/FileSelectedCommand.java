package edu.cornell.dendro.corina.io.command;

import java.awt.Color;
import java.io.IOException;

import org.apache.commons.lang.WordUtils;
import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.TridasIO;
import org.tridas.io.exceptions.InvalidDendroFileException;
import org.tridas.io.exceptions.InvalidDendroFileException.PointerType;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.io.LineHighlighter;
import edu.cornell.dendro.corina.io.TridasFileImportPanel;
import edu.cornell.dendro.corina.io.TridasTreeModel;
import edu.cornell.dendro.corina.io.control.FileSelectedEvent;
import edu.cornell.dendro.corina.io.control.ImportEntitySelectedEvent;
import edu.cornell.dendro.corina.io.model.TridasRepresentationTreeModel;
import edu.cornell.dendro.corina.ui.Alert;

public class FileSelectedCommand implements ICommand {

	@Override
	public void execute(MVCEvent argEvent) {
		FileSelectedEvent event = (FileSelectedEvent) argEvent;
		try {
			event.model.setFileToImport(event.file);
		} catch (IOException e) {
			Alert.errorLoading(event.file.getAbsolutePath(), e);
			return;
		}
		
		// Create a reader based on the file type supplied
		AbstractDendroFileReader reader;
		reader = TridasIO.getFileReader(event.fileType);
		if(reader==null) 
		{
			Alert.error("Error", "Unknown file type");
			return;
		}
		
		// Try and load the file
		try {
			reader.loadFile(event.file.getAbsolutePath());
		} catch (IOException e) {
			Alert.errorLoading(event.file.getAbsolutePath(), e);
			return;
		} catch (InvalidDendroFileException e) {
			event.model.setFileException(e);
			return;
		}
		catch(NullPointerException e)
		{
			Alert.error("Invalid File", e.getLocalizedMessage());
		}
		
		// Dispatch any warnings
		if(reader.getWarnings().length>0)
		{
			event.model.setConversionWarnings(reader.getWarnings());
		}
		
		// Extract project from file
		TridasRepresentationTreeModel treeMdl = new TridasRepresentationTreeModel(reader.getProject());
		event.model.setTreeModel(treeMdl);
		
	}

}
