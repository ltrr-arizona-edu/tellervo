package edu.cornell.dendro.corina.io.control;

import com.dmurph.mvc.control.FrontController;

import edu.cornell.dendro.corina.io.command.EntitySelectedCommand;
import edu.cornell.dendro.corina.io.command.EntitySwappedCommand;
import edu.cornell.dendro.corina.io.command.FileSelectedCommand;

public class ImportController extends FrontController {

    public static final String ENTITY_SELECTED = "IMPORT_ENTITY_SELECTED";
    public static final String FILE_SELECTED = "IMPORT_FILE_SELECTED";
    public static final String ENTITY_SWAPPED = "IMPORT_ENTITY_SWAPPED";

    public ImportController()
    {
    	registerCommand(ENTITY_SELECTED, EntitySelectedCommand.class);
    	registerCommand(ENTITY_SWAPPED, EntitySwappedCommand.class);
    	registerCommand(FILE_SELECTED, FileSelectedCommand.class);

    }

	
}
