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
package org.tellervo.desktop.sample;

import java.io.IOException;

import org.tellervo.desktop.gui.UserCancelledException;


public interface SampleLoader {
	/**
	 * Get a String-based representation of the 'path' information for the file
	 * For File-based samples, this is a file name
	 * For URL-based samples, this is its URI.toString(); 
	 * @return
	 */
	//public String getLocationRepresentation();

	// self explanatory
	public Sample load() throws IOException;
	public BaseSample loadBasic() throws IOException;
	public boolean save(Sample s) throws IOException, UserCancelledException;

	// populate any internal loader data from this sample
	public void preload(BaseSample bs);
	
	public String getName();
	public String getShortName();
	public SampleType getSampleType();
}
