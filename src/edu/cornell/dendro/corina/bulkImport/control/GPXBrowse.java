package edu.cornell.dendro.corina.bulkImport.control;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.model.HashModel;
import com.dmurph.mvc.tracking.ITrackable;

public class GPXBrowse extends MVCEvent implements ITrackable{

	public final HashModel model;
	
	public GPXBrowse(HashModel model)
	{
		super(BulkImportController.BROWSE_GPX_FILE);
		this.model = model;
	}

	@Override
	public String getTrackingAction() {
		return "Browse GPX";
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
