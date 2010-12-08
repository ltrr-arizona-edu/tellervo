package edu.cornell.dendro.corina.io.control;

import com.dmurph.mvc.control.FrontController;

import edu.cornell.dendro.corina.io.command.EntitySaveCommand;
import edu.cornell.dendro.corina.io.command.EntitySwappedCommand;
import edu.cornell.dendro.corina.io.command.FileSelectedCommand;
import edu.cornell.dendro.corina.io.command.MergeEntitiesCommand;
import edu.cornell.dendro.corina.io.command.NodeSelectedCommand;

public class ImportController extends FrontController {

    public static final String ENTITY_SELECTED = "IMPORT_ENTITY_SELECTED";
    public static final String FILE_SELECTED = "IMPORT_FILE_SELECTED";
    public static final String ENTITY_SWAPPED = "IMPORT_ENTITY_SWAPPED";
    public static final String MERGE_ENTITIES = "IMPORT_MERGE_ENTITIES";

    public static final String ENTITY_SAVE = "IMPORT_ENTITY_SAVE";

    public ImportController()
    {
    	registerCommand(ENTITY_SELECTED, NodeSelectedCommand.class);
    	registerCommand(ENTITY_SWAPPED, EntitySwappedCommand.class);
    	registerCommand(FILE_SELECTED, FileSelectedCommand.class);
    	registerCommand(MERGE_ENTITIES, MergeEntitiesCommand.class);
    	registerCommand(ENTITY_SAVE, EntitySaveCommand.class);
    	


    }

	
}
