package edu.cornell.dendro.corina.io.control;

import com.dmurph.mvc.control.FrontController;

import edu.cornell.dendro.corina.io.command.EntitySelectedCommand;
import edu.cornell.dendro.corina.io.command.FileSelectedCommand;

public class ImportController extends FrontController {

    public static final String ENTITY_SELECTED = "IMPORT_ENTITY_SELECTED";
    public static final String FILE_SELECTED = "IMPORT_FILE_SELECTED";

    public ImportController()
    {
    	registerCommand(ENTITY_SELECTED, EntitySelectedCommand.class);
    	registerCommand(FILE_SELECTED, FileSelectedCommand.class);

    }

	
}
