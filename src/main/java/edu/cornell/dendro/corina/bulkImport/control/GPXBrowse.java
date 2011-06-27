/*******************************************************************************
 * Copyright (C) 2010 Daniel Murphy and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Daniel Murphy
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.bulkImport.control;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.model.HashModel;
import com.dmurph.mvc.tracking.ITrackable;

public class GPXBrowse extends MVCEvent implements ITrackable{
	
	private static final long serialVersionUID = 1L;
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
