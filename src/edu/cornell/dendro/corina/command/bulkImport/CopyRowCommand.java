package edu.cornell.dendro.corina.command.bulkImport;

import com.dmurph.mvc.ICloneable;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.control.bulkImport.CopyRowEvent;

public class CopyRowCommand implements ICommand {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(MVCEvent argEvent) {
		CopyRowEvent event = (CopyRowEvent) argEvent;
		ICloneable selected = (ICloneable) event.model.getRows().get(event.selectedRowIndex);
		ICloneable newInstance = event.model.createRowInstance();
		newInstance.cloneFrom(selected);
		
		event.model.getRows().add(newInstance);
	}
}
