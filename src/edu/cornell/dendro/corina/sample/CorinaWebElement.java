package edu.cornell.dendro.corina.sample;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JList;

import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.webdbi.*;

/**
 * This class is intended to be used only for corinaweb:// urls
 * Use at your own risk with other things :)
 * 
 * @author lucasm
 *
 */

public class CorinaWebElement implements SampleLoader {
	private URI uri;
	private ResourceIdentifier resourceIdentifier;
	
	/**
	 * Old way of doing it: parsing a bad URL.
	 * Perhaps we'll do this again with a GET request
	 * @deprecated
	 * @param strUri
	 * @throws URISyntaxException
	 */
	public CorinaWebElement(String strUri) throws URISyntaxException {
		this.uri = new URI(strUri);
		
		String path = uri.getPath();
		String sampleID = null;
		
		if(path.startsWith("/measurement/")) {
			sampleID = path.substring("/measurement/".length());
			//shortName = "Sample " + sampleID;
		}
		else {
			new Bug(new Exception("Bad things were passed to CorinaWebElement()!"));
		}
		
		this.resourceIdentifier = new ResourceIdentifier("measurement", sampleID);
	}
	
	public CorinaWebElement(ResourceIdentifier resourceIdentifier) {
		this.resourceIdentifier = resourceIdentifier;
	}

	public String getName() {
		return resourceIdentifier.toString();
	}

	public String getShortName() {
		return resourceIdentifier.toString();
	}

	public Sample load() throws IOException {
		// set up the resource
		MeasurementResource xf = new MeasurementResource();
		xf.attachIdentifier(resourceIdentifier);
		
		PrototypeLoadDialog dlg = new PrototypeLoadDialog(xf);
		
		// start our query (remotely)
		xf.query();		
		
		dlg.setVisible(true);
		
		if(!dlg.isSuccessful())
			throw new IOException("Failed to load: " + dlg.getFailException());
		
		return xf.getObject();
	}

	public BaseSample loadBasic() throws IOException {
		return load();
	}
}
