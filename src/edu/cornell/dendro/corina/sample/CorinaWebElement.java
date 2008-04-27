package edu.cornell.dendro.corina.sample;

import java.awt.BorderLayout;
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

public class CorinaWebElement implements SampleLoader, ResourceEventListener {
	private URI uri;
	private String sampleID;
	private String shortName;
	
	private class CWELoadDialog extends JDialog {
		
		private JList list;
		
		public CWELoadDialog() {
			super();
			
			setModal(true);
			setTitle("Loading remote sample...");
			setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			
			list = new JList(new DefaultListModel());
			
			list.setVisibleRowCount(10);
		    list.setBorder(BorderFactory.createTitledBorder("Status"));
		    
		    getContentPane().add(list, BorderLayout.CENTER);
		    pack();
		    setSize(300, 200);
		    Center.center(this);
		}
		
		public void addStatus(String s) {
			((DefaultListModel)list.getModel()).addElement(s);
			list.ensureIndexIsVisible(list.getModel().getSize() - 1);
		}
		
		private boolean success = false;

		public boolean isSuccessful() {
			return success;
		}
		
		public void setSuccessful(boolean successful) {
			this.success = successful;
			dispose();
		}
	}
	
	public CorinaWebElement(String strUri) throws URISyntaxException {
		this.uri = new URI(strUri);
		
		String path = uri.getPath();
		if(path.startsWith("/measurement/")) {
			sampleID = path.substring("/measurement/".length());
			shortName = "Sample " + sampleID;
		}
		else {
			new Bug(new Exception("Bad things were passed to CorinaWebElement()!"));
		}
	}

	public String getName() {
		return uri.toString();
	}

	public String getShortName() {
		return shortName;
	}

	private CWELoadDialog dlg;
	private Exception failEx = null;
	
	public Sample load() throws IOException {
		
		// create an initial sample
		Sample s = new Sample();
		s.setMeta("id", sampleID);

		// set up the resource
		MeasurementResource xf = new MeasurementResource();
		xf.setObject(s);
		xf.addResourceEventListener(this);
		
		// start our query (remotely)
		xf.query();
		
		dlg = new CWELoadDialog();
		dlg.addStatus("Loading [id: " + sampleID + "]");
		dlg.addStatus("Connecting to server...");
		dlg.setVisible(true);
		
		if(!dlg.isSuccessful())
			throw new IOException("Failed to load: " + failEx);
		
		shortName = xf.getObject().getMeta("title").toString() + " [" + sampleID + "]";
		
		return xf.getObject();
	}

	public BaseSample loadBasic() throws IOException {
		return load();
	}

	public void resourceChanged(ResourceEvent re) {
		if(dlg == null)
			return; // we don't care anymore!
		
		int eventType = re.getEventType();
		switch(eventType) {
		case ResourceEvent.RESOURCE_QUERY_COMPLETE:
			dlg.setSuccessful(true);
			break;
			
		case ResourceEvent.RESOURCE_QUERY_FAILED:
			failEx = re.getAttachedException();
			dlg.setSuccessful(false);
			break;
			
		case ResourceEvent.RESOURCE_DEBUG_OUT:
			dlg.addStatus("Sent request, awaiting reply...");
			break;
			
		case ResourceEvent.RESOURCE_DEBUG_IN:
			dlg.addStatus("Reply received, processing...");
			
		default:
			break;
		}
	}
}
