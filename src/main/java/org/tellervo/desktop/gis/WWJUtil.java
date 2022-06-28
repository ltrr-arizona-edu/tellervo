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
package org.tellervo.desktop.gis;

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
