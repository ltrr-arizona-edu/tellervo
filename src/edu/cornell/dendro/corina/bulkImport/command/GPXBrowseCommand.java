package edu.cornell.dendro.corina.bulkImport.command;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;
import com.dmurph.mvc.model.HashModel;
import com.dmurph.mvc.model.MVCArrayList;

import edu.cornell.dendro.corina.bulkImport.control.GPXBrowse;
import edu.cornell.dendro.corina.bulkImport.model.IBulkImportSectionModel;
import edu.cornell.dendro.corina.gis.GPXParser;
import edu.cornell.dendro.corina.gis.GPXParser.GPXWaypoint;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.I18n;

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
