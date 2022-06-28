/*******************************************************************************
 * Copyright (C) 2011 Daniel Murphy and Peter Brewer.
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
package org.tellervo.desktop.bulkdataentry.command;

import org.tellervo.desktop.bulkdataentry.control.CopyRowEvent;

import com.dmurph.mvc.ICloneable;
import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;


public class CopyRowCommand implements ICommand {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(MVCEvent argEvent) {
		
		try {
	        MVC.splitOff(); // so other mvc events can execute
		} catch (IllegalThreadException e) {
		        // this means that the thread that called splitOff() was not an MVC thread, and the next event's won't be blocked anyways.
		        e.printStackTrace();
		} catch (IncorrectThreadException e) {
		        // this means that this MVC thread is not the main thread, it was already splitOff() previously
		        e.printStackTrace();
		}
		
		CopyRowEvent event = (CopyRowEvent) argEvent;
		ICloneable selected = (ICloneable) event.model.getRows().get(event.getValue());
		ICloneable newInstance = event.model.createRowInstance();
		newInstance.cloneFrom(selected);
		
		event.model.getRows().add(newInstance);
	}
}
