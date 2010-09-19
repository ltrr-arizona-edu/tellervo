package edu.cornell.dendro.corina.command.bulkImport;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;
import com.dmurph.mvc.model.MVCArrayList;
import com.dmurph.mvc.tracking.ITrackable;

import edu.cornell.dendro.corina.control.bulkImport.CopyRowEvent;
import edu.cornell.dendro.corina.model.bulkImport.ISingleRowModel;

public class CopyRowCommand implements ICommand, ITrackable {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(MVCEvent argEvent) {

		CopyRowEvent event = (CopyRowEvent) argEvent;
		MVCArrayList<Object> rows = (MVCArrayList<Object>) event.model.getRows();
		ISingleRowModel selRow = (ISingleRowModel) rows.get(event.getSelectedRowIndex());
	
		rows.add(event.model.createClonedRowInstance(selRow));
		
		
	}
	
	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingAction()
	 */
	@Override
	public String getTrackingAction() {
		return "Copy row";
	}
	
	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingCategory()
	 */
	@Override
	public String getTrackingCategory() {
		return "Bulk Import";
	}
	
	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingLabel()
	 */
	@Override
	public String getTrackingLabel() {
		return null;
	}
	
	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingValue()
	 */
	@Override
	public Integer getTrackingValue() {
		return null;
	}

}
