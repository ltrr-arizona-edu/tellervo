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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.tellervo.desktop.bulkdataentry.control.PopulateFromODKFileEvent;
import org.tellervo.desktop.bulkdataentry.model.ElementModel;
import org.tellervo.desktop.bulkdataentry.model.ObjectModel;
import org.tellervo.desktop.bulkdataentry.model.SingleElementModel;
import org.tellervo.desktop.bulkdataentry.model.SingleObjectModel;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.odk.ODKParser;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.I18n;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;


public class PopulateFromODKFileCommand implements ICommand {
	
	
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
		
		PopulateFromODKFileEvent event = (PopulateFromODKFileEvent) argEvent;
		ArrayList<File> filesProcessed = new ArrayList<File>();
		ArrayList<File> filesFailed = new ArrayList<File>();
		
		JFileChooser fc = new JFileChooser(App.prefs.getPref(PrefKey.FOLDER_LAST_READ, null));
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showOpenDialog(null);

	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	
				App.prefs.setPref(PrefKey.FOLDER_LAST_READ, fc.getSelectedFile().getAbsolutePath());

				SuffixFileFilter fileFilter = new SuffixFileFilter(".xml");
				@SuppressWarnings("unchecked")
				Iterator<File> iterator = FileUtils.iterateFiles(fc.getSelectedFile(), fileFilter, TrueFileFilter.INSTANCE);	
				
				File file = null;
				try {
					

					
					while(iterator.hasNext())
					{
						file = iterator.next();
						filesProcessed.add(file);
												
						if(event.model instanceof ObjectModel)
						{
							ODKParser parser = new ODKParser(file, TridasObject.class);
							
							if(!parser.isValidODKFile()) {
								filesFailed.add(file);
								continue;
							}
							
							ObjectModel model = (ObjectModel) event.model;
							
							SingleObjectModel newrow = (SingleObjectModel) model.createRowInstance();
							
							newrow.setProperty(SingleObjectModel.OBJECT_CODE, parser.getFieldValue("PlotSubplotID"));
							newrow.setProperty(SingleObjectModel.TITLE, parser.getFieldValue("PlotSubplotID"));
							newrow.setProperty(SingleObjectModel.LATITUDE, parser.getLatitude("Location"));
							newrow.setProperty(SingleObjectModel.LONGITUDE, parser.getLongitude("Location"));
							newrow.setProperty(SingleObjectModel.LOCATION_PRECISION, parser.getError("Location"));
							newrow.setProperty(SingleObjectModel.VEGETATION_TYPE, parser.getFieldValue("VegetationType"));
	
							newrow.setProperty(SingleObjectModel.COMMENTS, "Imported from "+file.getName());
	
							
							model.getRows().add(newrow);
						}
						else if (event.model instanceof ElementModel)
						{
							ODKParser parser = new ODKParser(file, TridasElement.class);
							
							if(!parser.isValidODKFile()) {
								filesFailed.add(file);
								continue;
							}
							
							ElementModel model = (ElementModel) event.model;
							SingleElementModel newrow = (SingleElementModel) model.createRowInstance();
							
							
							newrow.setProperty(SingleElementModel.OBJECT, getTridasObjectByCode(parser.getFieldValue("PlotSubplotID").toString()));
							newrow.setProperty(SingleElementModel.TITLE, parser.getFieldValue("TreeNO"));
							model.getRows().add(newrow);
						}
					}
								
				} catch (FileNotFoundException e) {
					if(file!=null) filesFailed.add(file);
					Alert.error(I18n.getText("error"), I18n.getText("error.fileNotFound"));
				} catch (IOException e) {
					if(file!=null) filesFailed.add(file);
					Alert.error(I18n.getText("error"), "Invalid ODK file");
				}  catch (Exception e) {
					if(file!=null) filesFailed.add(file);
					Alert.error(I18n.getText("error"), e.getLocalizedMessage());
				}    
				Integer goodfiles = filesProcessed.size()-filesFailed.size();
				Integer badfiles = filesFailed.size();
				
				if(badfiles>0)
				{
					Alert.error("ODK Import", "There were problems parsing "+badfiles+" files.\nA total of "+goodfiles+" were successfully imported.");
				}
				else
				{
					Alert.message("ODK Import", "A total of "+goodfiles+" have been imported.");
				}
				
	    }		
		


	}
	
	private TridasObjectEx getTridasObjectByCode(String code)
	{
		List<TridasObjectEx> entities = App.tridasObjects.getObjectList();

		for(TridasObjectEx obj : entities)
		{
			if(TridasUtils.getGenericFieldByName(obj, "tridas.objectcode").equals(code))
			{
				return obj;
			}
		}
		return null;
	}
}
