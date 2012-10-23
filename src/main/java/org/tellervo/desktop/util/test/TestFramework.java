/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
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
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.util.test;

import java.io.IOException;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.XMLDebugView;
import org.tellervo.desktop.platform.Platform;
import org.tellervo.desktop.sample.TellervoWsiTridasElement;
import org.tellervo.desktop.sample.Sample;
import org.tridas.schema.TridasIdentifier;


public class TestFramework {

	public static Sample getSampleForID(String domain, String id) throws IOException {
		// make tridas identifier
		TridasIdentifier identifier = new TridasIdentifier();
		identifier.setDomain(domain);
		identifier.setValue(id);
		
		// make element
		TellervoWsiTridasElement element = new TellervoWsiTridasElement(identifier);
		
		// load it
		return element.load();
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    App.platform = new Platform();
	    App.platform.init();
	    
		App.init(null, null);
		
		XMLDebugView.showDialog();
		
		String domain = App.domain;
		String measurementID = "02189be5-b19c-5dbd-9035-73ae8827dc7a";
		Sample s;
		
		try {
			s = getSampleForID(domain, measurementID);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
			return;
		}
		
		System.out.println("Loaded " + s.toString());
	}

}
