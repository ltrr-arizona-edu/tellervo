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

import javax.swing.JFrame;

import org.tellervo.desktop.bulkImport.control.GPXBrowse;
import org.tellervo.desktop.bulkImport.model.IBulkImportSectionModel;
import org.tellervo.desktop.gis.GPXParser;
import org.tellervo.desktop.gis.GPXParser.GPXWaypoint;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.I18n;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;
import com.dmurph.mvc.model.HashModel;
import com.dmurph.mvc.model.MVCArrayList;


public class GPXBrowseCommand implements ICommand {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(MVCEvent argEvent) {
		GPXBrowse event = (GPXBrowse) argEvent;
		HashModel model = event.model;
		
		FileDialog dialog  = new FileDialog(new JFrame());
		dialog.setTitle("Choose GPX File");
		dialog.setFilenameFilter(new GPXFilenameFilter());
		dialog.setVisible(true);
		String curFile;

		if((curFile = dialog.getFile())!=null)
		{
			 try {
				GPXParser parser = new GPXParser(dialog.getDirectory() + curFile);
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

	public class GPXFilenameFilter implements FilenameFilter
	{

		@Override
		public boolean accept(File dir, String name) {
			if(name.endsWith("gpx")) return true;
			
			return false;
		}
		
	}
}
