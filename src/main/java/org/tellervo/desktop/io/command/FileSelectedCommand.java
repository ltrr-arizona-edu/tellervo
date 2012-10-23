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

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.gui.Bug;
import org.tellervo.desktop.io.control.FileSelectedEvent;
import org.tellervo.desktop.io.model.TridasRepresentationTreeModel;
import org.tellervo.desktop.ui.Alert;
import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.TridasIO;
import org.tridas.io.exceptions.ConversionWarning;
import org.tridas.io.exceptions.ConversionWarning.WarningType;
import org.tridas.io.exceptions.InvalidDendroFileException;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.NormalTridasVariable;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasValues;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;


public class FileSelectedCommand implements ICommand {
	private final static Logger log = LoggerFactory.getLogger(FileSelectedCommand.class);

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

		
		FileSelectedEvent event = (FileSelectedEvent) argEvent;
		try {
			event.model.setFileToImport(event.file);
			event.model.setConversionWarnings(null);
			
		} catch (IOException e) {
			Alert.errorLoading(event.file.getAbsolutePath(), e);
			return;
		}
		
		// Create a reader based on the file type supplied
		AbstractDendroFileReader reader;
		reader = TridasIO.getFileReader(event.fileType);
		if(reader==null) 
		{
			Alert.error("Error", "Unknown file type");
			return;
		}
		else
		{
			event.model.setFileType(event.fileType);
		}
		
		// Try and load the file
		try {
			reader.loadFile(event.file.getAbsolutePath());
		} catch (IOException e) {
			Alert.errorLoading(event.file.getAbsolutePath(), e);
			return;
		} catch (InvalidDendroFileException e) {
			event.model.setFileException(e);
			return;
		}
		catch(NullPointerException e)
		{
			Alert.error("Invalid File", e.getLocalizedMessage());
		}
		
		// Dispatch any warnings
		if(reader.getWarnings().length>0)
		{
			event.model.setConversionWarnings(reader.getWarnings());
		}
		
		// Warn if project contains derivedSeries
		if(reader.getProjects()[0].isSetDerivedSeries())
		{
			event.model.appendConversionWarning(new ConversionWarning(
					WarningType.IGNORED, 
					"Tellervo does not currently support importing of derived series / chronologies"));
		}
		
		// Add custom Tellervo warnings for unsupported aspects of TRiDaS
		
		try{
			
		for(TridasObject ob :TridasUtils.getObjectList(reader.getProjects()[0]))
		{
			if(ob.isSetLinkSeries())
			{
				event.model.appendConversionWarning(new ConversionWarning(
						WarningType.IGNORED, 
						"Tellervo does not currently support the linkSeries tags in objects",
						"linkSeries"));
			}
		}
			
		for (TridasMeasurementSeries series: TridasUtils.getMeasurementSeriesFromTridasProject(reader.getProjects()[0]))
		{
			if(series.isSetWoodCompleteness())
			{
				event.model.appendConversionWarning(new ConversionWarning(
						WarningType.IGNORED, 
						"Tellervo does not support the WoodCompleteness tags in a series, only in the radius",
						"woodCompleteness"));
			}
			
			
			if(series.isSetValues())
			{
				for(TridasValues values : series.getValues())
				{
					if(values.isSetUnit())
					{
						if(!values.getUnit().isSetNormalTridas())
						{
							event.model.appendConversionWarning(new ConversionWarning(
									WarningType.NOT_STRICT, 
									"Series does not use standard units - assuming 1/100th mm"));
						}
					}
					else
					{
						event.model.appendConversionWarning(new ConversionWarning(
								WarningType.AMBIGUOUS, 
								"Series does not have units associated - assuming 1/100th mm"));
					}
					
					if(values.isSetVariable())
					{
						if(values.getVariable().isSetNormalTridas())
						{
							NormalTridasVariable var = values.getVariable().getNormalTridas();
							
							switch(var)
							{
							case RING_WIDTH:
								break;
							default:
								event.model.appendConversionWarning(new ConversionWarning(
										WarningType.UNREPRESENTABLE, 
										"Tellervo currently only supports whole ring width values"));
							}
						}
					}
					else
					{
						event.model.appendConversionWarning(new ConversionWarning(
								WarningType.AMBIGUOUS, 
								"Series does not specify what variable is provided - assuming whole ring width"));
					}
				}
			}
		}
		} catch (Exception ex)
		{
			log.error("Error modifying series to deal with Tellervo limitations");
			new Bug(ex);
		}
		
		
		// Extract project from file
		TridasRepresentationTreeModel treeMdl = new TridasRepresentationTreeModel(reader.getProjects()[0]);
		event.model.setTreeModel(treeMdl);
		
	}
	
	

}
