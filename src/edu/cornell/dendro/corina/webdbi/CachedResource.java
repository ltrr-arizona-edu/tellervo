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
		super(cachedResourceName);

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
			
			loadDocument(doc);
		}
	}
	
	protected final void loadSucceeded(Document doc) {
		super.loadSucceeded(doc);
		
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
