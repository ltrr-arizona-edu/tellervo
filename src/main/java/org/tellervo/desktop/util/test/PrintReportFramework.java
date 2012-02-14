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
import java.util.Collections;
import java.util.List;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.XMLDebugView;
import org.tellervo.desktop.platform.Platform;
import org.tellervo.desktop.sample.CorinaWsiTridasElement;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.schema.SearchReturnObject;
import org.tellervo.desktop.tridasv2.TridasComparator;
import org.tellervo.desktop.wsi.corina.CorinaResourceAccessDialog;
import org.tellervo.desktop.wsi.corina.SearchParameters;
import org.tellervo.desktop.wsi.corina.resources.EntitySearchResource;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;


public class PrintReportFramework {

	public static Sample getCorinaSampleFromVMID(String domain, String id) throws IOException {
		// make tridas identifier
		TridasIdentifier identifier = new TridasIdentifier();
		identifier.setDomain(domain);
		identifier.setValue(id);
		
		// make element
		CorinaWsiTridasElement element = new CorinaWsiTridasElement(identifier);
		
		// load it
		return element.load();
	}

	public static List<TridasSample> getExampleTridasSamples() throws IOException 
	{
		// Find all objects 
    	SearchParameters sampparam = new SearchParameters(SearchReturnObject.SAMPLE);
		sampparam.setLimit(10);
		sampparam.addSearchForAll();
		
		
		EntitySearchResource<TridasSample> sampresource = new EntitySearchResource<TridasSample>(sampparam);
		
		CorinaResourceAccessDialog dialog = new CorinaResourceAccessDialog(sampresource);
		sampresource.query();	
		dialog.setVisible(true);
		if(!dialog.isSuccessful()) 
		{ 
			System.out.println("oopsey doopsey.  Error getting samples");
			return null;
		}
		List<TridasSample> samplist = sampresource.getAssociatedResult();

		// Sort list intelligently
		TridasComparator numSorter = new TridasComparator(TridasComparator.Type.SITE_CODES_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		
		Collections.sort(samplist, numSorter);
		
		return samplist;
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
			s = getCorinaSampleFromVMID(domain, measurementID);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
			return;
		}
		
		System.out.println("Loaded " + s.toString());
	}

	public static TridasObject getCorinaObjectFromID(String domain, String objid) {
		// TODO Auto-generated method stub
		return null;
	}

}
