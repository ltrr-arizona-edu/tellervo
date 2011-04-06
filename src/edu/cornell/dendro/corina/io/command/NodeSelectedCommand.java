package edu.cornell.dendro.corina.io.command;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.io.control.ImportNodeSelectedEvent;

public class NodeSelectedCommand implements ICommand {

	@Override
	public void execute(MVCEvent argEvent) {
		ImportNodeSelectedEvent event = (ImportNodeSelectedEvent) argEvent;
		
		if(event.getValue()!=null) event.model.setSelectedNode(event.getValue());
		
	}

}
