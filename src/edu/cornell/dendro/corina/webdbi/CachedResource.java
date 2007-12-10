package edu.cornell.dendro.corina.webdbi;

import edu.cornell.dendro.corina.core.App;

import org.jdom.*;
import org.jdom.output.*;
import org.jdom.input.*;
import java.io.*;

/*
 * A cached resource
 */

public class CachedResource extends Resource {
	private String cachedResourcePath;
	
	public CachedResource(String cachedResourceName) {
		// we only support read-only resources :>
		super(cachedResourceName, new ResourceQueryType(ResourceQueryType.READ));

		cachedResourcePath = App.prefs.getCorinaDir() + getResourceName() + ".xmlcache";
		
		Document doc;
		File cachedResourceFile = new File(cachedResourcePath);
		if(cachedResourceFile.exists()) {
			try {
				System.out.println("Loading cache for '" + getResourceName() + "'");
				SAXBuilder builder = new SAXBuilder();
				doc = builder.build(cachedResourceFile);
			} catch (Exception e) {
				System.out.println("Unable to load cache for '" + getResourceName() + "': " + e);
				return;
			}
			
			processQueryResult(doc);
		}
	}
	
	protected final void querySucceeded(Document doc) {
		super.querySucceeded(doc);
		
		// Simple: save everything to a file
		
		System.out.println("Saving cache for '" + getResourceName() + "' to " + cachedResourcePath);
		
		try {
			FileWriter writer = new FileWriter(cachedResourcePath);
			XMLOutputter outputter = new XMLOutputter();
			
			outputter.output(doc, writer);
			
			writer.close();
			
		} catch (IOException ioe) {
			System.out.println("Failed in saving cache: " + ioe);
		}
	}
}
