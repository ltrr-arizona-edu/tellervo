package edu.cornell.dendro.corina.command.bulkImport;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;
import com.dmurph.mvc.model.HashModel;
import com.dmurph.mvc.model.MVCArrayList;

import edu.cornell.dendro.corina.control.bulkImport.GPXBrowse;
import edu.cornell.dendro.corina.gis.GPXParser;
import edu.cornell.dendro.corina.gis.GPXParser.GPXWaypoint;
import edu.cornell.dendro.corina.model.bulkImport.IBulkImportSectionModel;
import edu.cornell.dendro.corina.ui.Alert;

public class GPXBrowseCommand implements ICommand {

	@Override
	public void execute(MVCEvent argEvent) {
		GPXBrowse event = (GPXBrowse) argEvent;
		HashModel model = event.model;
		
		FileDialog dialog  = new FileDialog(new JFrame());
		dialog.setFile("*.gpx");
		dialog.setVisible(true);
		String curFile;

		if((curFile = dialog.getFile())!=null)
		{
			 try {
				GPXParser parser = new GPXParser(dialog.getDirectory() + curFile);
				MVCArrayList<GPXWaypoint> list = (MVCArrayList<GPXWaypoint>) model.getProperty(IBulkImportSectionModel.WAYPOINT_LIST);
				list.addAll(parser.getWaypoints());
			} catch (FileNotFoundException e) {
				Alert.error("Error", "File not found");
			} catch (IOException e) {
				Alert.error("Error", "Error reading GPX file");
			}
		}
		
	}

}
