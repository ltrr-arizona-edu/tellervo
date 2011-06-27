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
package edu.cornell.dendro.corina.io.control;

import java.awt.Window;

import com.dmurph.mvc.MVCEvent;

import edu.cornell.dendro.corina.io.model.ImportModel;

public class ImportEntitySaveEvent extends MVCEvent {

	private static final long serialVersionUID = 1L;
	
	public final ImportModel model;
	public final Window window;

	public ImportEntitySaveEvent(ImportModel model, Window window) {
		super(IOController.ENTITY_SAVE);
		this.model = model;
		this.window = window;
	}

}
