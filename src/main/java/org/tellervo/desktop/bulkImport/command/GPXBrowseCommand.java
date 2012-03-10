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
package org.tellervo.desktop.bulkImport.command;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.tellervo.desktop.bulkImport.control.GPXBrowse;
import org.tellervo.desktop.bulkImport.model.IBulkImportSectionModel;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gis.GPXParser;
import org.tellervo.desktop.gis.GPXParser.GPXWaypoint;
import org.tellervo.desktop.io.AbstractDendroReaderFileFilter;
import org.tellervo.desktop.io.DendroReaderFileFilter;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.I18n;
import org.tridas.io.TridasIO;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;
import com.dmurph.mvc.model.HashModel;
import com.dmurph.mvc.model.MVCArrayList;


public class GPXBrowseCommand implements ICommand {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(MVCEvent argEvent) {
		
		/*try {
	        MVC.splitOff(); // so other mvc events can execute
		} catch (IllegalThreadException e) {
		        // this means that the thread that called splitOff() was not an MVC thread, and the next event's won't be blocked anyways.
		        e.printStackTrace();
		} catch (IncorrectThreadException e) {
		        // this means that this MVC thread is not the main thread, it was already splitOff() previously
		        e.printStackTrace();
		}*/
		
		GPXBrowse event = (GPXBrowse) argEvent;
		HashModel model = event.model;
		
		JFileChooser fc = new JFileChooser(App.prefs.getPref(PrefKey.FOLDER_LAST_GPS, null));
		fc.setFileFilter(new GPXFilenameFilter());
		int returnVal = fc.showOpenDialog(event.parent);

	    if (returnVal == JFileChooser.APPROVE_OPTION) {
			 try {
					GPXParser parser = new GPXParser(fc.getSelectedFile().getAbsolutePath());
					MVCArrayList<GPXWaypoint> list = (MVCArrayList<GPXWaypoint>) model.getProperty(IBulkImportSectionModel.WAYPOINT_LIST);
					ArrayList<GPXWaypoint> wplist = parser.getWaypoints();
					Collections.sort(wplist);
					list.clear();
					list.addAll(wplist);
					Collections.sort(list);
								
				} catch (FileNotFoundException e) {
					Alert.error(I18n.getText("error"), I18n.getText("error.fileNotFound"));
				} catch (IOException e) {
					Alert.error(I18n.getText("error"), I18n.getText("gis.invalidgpx"));
				}     
	    }
	}

	public class GPXFilenameFilter extends AbstractDendroReaderFileFilter
	{

		public boolean accept(File file) {
			if(file.getAbsolutePath().toLowerCase().endsWith("gpx")) return true;
			
			if(file.isDirectory()) return true;
			
			return false;
		}

		@Override
		public String getDescription() {
			return "GPS Exchange format (*.gpx)";
		}
		
	}
}
