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
import org.tridas.interfaces.ITridas;

import com.dmurph.mvc.ObjectEvent;


public class ImportMergeEntitiesEvent extends ObjectEvent<Class<? extends ITridas>> {

	private static final long serialVersionUID = 1L;
	public final ImportModel model;
	

	public ImportMergeEntitiesEvent(ImportModel model, Class<? extends ITridas> clazz) {
		super(IOController.MERGE_ENTITIES, clazz);
		this.model = model;
	}

}
