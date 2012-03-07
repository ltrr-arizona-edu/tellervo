/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.io.control;

import org.tellervo.desktop.io.model.ImportModel;
import org.tellervo.desktop.io.model.TridasRepresentationTableTreeRow;

import com.dmurph.mvc.MVC;
import com.dmurph.mvc.ObjectEvent;


public class ImportSwapEntityEvent extends ObjectEvent<TridasRepresentationTableTreeRow> {

	private static final long serialVersionUID = 1L;
	public final ImportModel model;
	public final TridasRepresentationTableTreeRow oldRow;
	private TridasRepresentationTableTreeRow newRowForReturn;
	public Boolean selectNodeAfterSwap = true;
	
	public ImportSwapEntityEvent(ImportModel model, TridasRepresentationTableTreeRow newRow, TridasRepresentationTableTreeRow oldRow) {
		super(IOController.ENTITY_SWAPPED, newRow);
		this.model = model;
		this.oldRow = oldRow;
	}
	
	public ImportSwapEntityEvent(ImportModel model, TridasRepresentationTableTreeRow newRow, TridasRepresentationTableTreeRow oldRow, Boolean selectOnCompletion) {
		super(IOController.ENTITY_SWAPPED, newRow);
		this.model = model;
		this.oldRow = oldRow;
		this.selectNodeAfterSwap = selectOnCompletion;
	}
	
	public TridasRepresentationTableTreeRow getNewRow()
	{
		return newRowForReturn;
	}
	
	/**
	 * Dispatches the event.  Events are dispatched globally, so make
	 * sure your key is unique!
	 */
	public void dispatch(){
		
		
		super.dispatch();
	}

}
