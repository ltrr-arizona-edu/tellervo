package edu.cornell.dendro.corina.command.bulkImport;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;
import com.dmurph.mvc.model.MVCArrayList;

import edu.cornell.dendro.corina.control.bulkImport.CopyRowEvent;

public class CopyRowCommand implements ICommand {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(MVCEvent argEvent) {

		CopyRowEvent event = (CopyRowEvent) argEvent;
		MVCArrayList<Object> rows = (MVCArrayList<Object>) event.model.getRows();
		Object selRow = rows.get(event.getSelectedRowIndex());
		
		rows.add(selRow);
		
		
	}

}
