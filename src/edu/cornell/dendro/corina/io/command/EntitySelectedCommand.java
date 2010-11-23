package edu.cornell.dendro.corina.io.command;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.io.control.ImportEntitySelectedEvent;

public class EntitySelectedCommand implements ICommand {

	@Override
	public void execute(MVCEvent argEvent) {
		ImportEntitySelectedEvent event = (ImportEntitySelectedEvent) argEvent;
		event.model.setSelectedNode(event.row);
	}

}
