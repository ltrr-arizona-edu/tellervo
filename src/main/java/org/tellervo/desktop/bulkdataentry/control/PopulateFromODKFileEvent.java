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

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.model.HashModel;


public class PopulateFromODKFileEvent extends MVCEvent{

	private static final long serialVersionUID = 1L;
	
	public final HashModel model;
	
	public PopulateFromODKFileEvent(HashModel model) {
		super(BulkImportController.POPULATE_FROM_ODK_FILE);
		
		this.model = model;
		
	}


}
