package edu.cornell.dendro.corina.io.control;

import com.dmurph.mvc.control.FrontController;

import edu.cornell.dendro.corina.io.command.NodeSelectedCommand;
import edu.cornell.dendro.corina.io.command.EntitySwappedCommand;
import edu.cornell.dendro.corina.io.command.FileSelectedCommand;
import edu.cornell.dendro.corina.io.command.UpdateEntityListCommand;

public class ImportController extends FrontController {

    public static final String ENTITY_SELECTED = "IMPORT_ENTITY_SELECTED";
    public static final String FILE_SELECTED = "IMPORT_FILE_SELECTED";
    public static final String ENTITY_SWAPPED = "IMPORT_ENTITY_SWAPPED";
    public static final String ENTITY_LIST_CHANGED = "IMPORT_ENTITY_LIST_CHANGED";
  
    public ImportController()
    {
    	registerCommand(ENTITY_SELECTED, NodeSelectedCommand.class);
    	registerCommand(ENTITY_SWAPPED, EntitySwappedCommand.class);
    	registerCommand(FILE_SELECTED, FileSelectedCommand.class);
    	registerCommand(ENTITY_LIST_CHANGED, UpdateEntityListCommand.class);

    }

	
}
