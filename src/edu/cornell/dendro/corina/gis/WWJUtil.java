package edu.cornell.dendro.corina.gis;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class WWJUtil {

	
	
	/**
	 * Set the map cache location
	 * 
	 * @param location
	 */
	public static void setCacheLocation(String location) {
		StringBuilder sb = new StringBuilder("<?xml version=\"1.0\"?>");

		sb.append("<dataFileStore><writeLocations><location wwDir=\"");
		sb.append(location);
		sb.append("\" create=\"true\"/></writeLocations></dataFileStore>");
		try {

			File file = File.createTempFile("adsc", ".xml");
			file.deleteOnExit();
			FileWriter fw = new java.io.FileWriter(file);

			fw.write(sb.toString());
			fw.close();
			Configuration.setValue(
					AVKey.DATA_FILE_STORE_CONFIGURATION_FILE_NAME, file
							.getAbsolutePath());
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	
}
