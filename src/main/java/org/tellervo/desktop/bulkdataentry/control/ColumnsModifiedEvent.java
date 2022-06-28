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
package org.tellervo.desktop.bulkdataentry.control;

import org.tellervo.desktop.bulkdataentry.model.ColumnListModel;

import com.dmurph.mvc.StringEvent;
import com.dmurph.mvc.tracking.ITrackable;


/**
 * @author daniel
 *
 */
public class ColumnsModifiedEvent extends StringEvent implements ITrackable{
	private static final long serialVersionUID = 2L;
	
	public final ColumnListModel model;
	
	public ColumnsModifiedEvent(String argKey, String argValue, ColumnListModel argModel) {
		super(argKey, argValue);
		model = argModel;
	}

	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingAction()
	 */
	@Override
	public String getTrackingAction() {
		if(key.equals(ColumnChooserController.COLUMN_ADDED)){
			return "Column Added";
		}else if(key.equals(ColumnChooserController.COLUMN_REMOVED)){
			return "Column Removed";
		}
		return "Unknown Key";
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
		return getValue();
	}

	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingValue()
	 */
	@Override
	public Integer getTrackingValue() {
		return null;
	}
}
