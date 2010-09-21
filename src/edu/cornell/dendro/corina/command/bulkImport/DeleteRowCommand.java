package edu.cornell.dendro.corina.command.bulkImport;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.control.bulkImport.DeleteRowEvent;

public class DeleteRowCommand implements ICommand {

	@Override
	public void execute(MVCEvent argEvent) {
		DeleteRowEvent event = (DeleteRowEvent) argEvent;
		event.model.getRows().remove((int)event.getValue());
	}
}
