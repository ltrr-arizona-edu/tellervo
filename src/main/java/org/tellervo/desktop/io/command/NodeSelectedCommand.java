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
package org.tellervo.desktop.io.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.io.control.ImportNodeSelectedEvent;
import org.tridas.schema.TridasMeasurementSeries;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;


public class NodeSelectedCommand implements ICommand {
	private final static Logger log = LoggerFactory.getLogger(NodeSelectedCommand.class);

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
		
		ImportNodeSelectedEvent event = (ImportNodeSelectedEvent) argEvent;
		
		// Update the model to show the selected node
		try{
		if(event.getValue()!=null) event.model.setSelectedRow(event.getValue());
		} catch (Exception e)
		{
			log.error(e.getMessage());
			e.printStackTrace();
		}
		
		// If the node is a series, we also need to update the data table
		if(event.getValue().getCurrentEntity() instanceof TridasMeasurementSeries)
		{
			
		}
		
	}

}
