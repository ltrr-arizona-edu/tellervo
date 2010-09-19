package edu.cornell.dendro.corina.command.bulkImport;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;
import com.dmurph.mvc.model.MVCArrayList;
import com.dmurph.mvc.tracking.ITrackable;

import edu.cornell.dendro.corina.control.bulkImport.DeleteRowEvent;

public class DeleteRowCommand implements ICommand, ITrackable {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(MVCEvent argEvent) {

		DeleteRowEvent event = (DeleteRowEvent) argEvent;
		MVCArrayList<Object> rows = (MVCArrayList<Object>) event.model.getRows();
		rows.remove(rows.get(event.getSelectedRowIndex()));
	}

	@Override
	public String getTrackingAction() {
		return "Delete row";
	}

	@Override
	public String getTrackingCategory() {
		return "Bulk Import";
	}

	@Override
	public String getTrackingLabel() {
		return null;
	}

	@Override
	public Integer getTrackingValue() {
		return null;
	}

}
