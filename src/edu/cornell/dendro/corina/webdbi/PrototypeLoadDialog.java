package edu.cornell.dendro.corina.webdbi;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JList;

import edu.cornell.dendro.corina.util.Center;

public class PrototypeLoadDialog extends JDialog implements ResourceEventListener {
	private JList list;
	private ResourceObject<?> myResource;
	private boolean success = false;
	private Exception failException = null;
	
	public PrototypeLoadDialog(ResourceObject<?> myResource) {
		super();
		
		// attach to our resource
		this.myResource = myResource;
		myResource.addResourceEventListener(this);
		
		setModal(true);
		setTitle("Accessing remote server...");
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		list = new JList(new DefaultListModel());
		
		list.setVisibleRowCount(10);
	    list.setBorder(BorderFactory.createTitledBorder("Status"));
	    
	    getContentPane().add(list, BorderLayout.CENTER);
	    pack();
	    setSize(300, 200);
	    Center.center(this);
	    
		addStatus(myResource.getQueryType() + " on: " + 
				  myResource.getResourceName());
		
		if(myResource.getIdentifier() != null)
			addStatus("Identifier: " + myResource.getIdentifier());
		
		if(myResource.getSearchParameters() != null)
			addStatus("Search params: " + myResource.getSearchParameters());
		
		addStatus("Connecting to server...");		
	}
	
	public void addStatus(String s) {
		((DefaultListModel)list.getModel()).addElement(s);
		list.ensureIndexIsVisible(list.getModel().getSize() - 1);
	}
	
	public boolean isSuccessful() {
		return success;
	}
	
	public Exception getFailException() {
		return failException;
	}
	
	private void setSuccessful(boolean successful) {
		this.success = successful;
		dispose();
	}
	
	public void resourceChanged(ResourceEvent re) {		
		int eventType = re.getEventType();
		switch(eventType) {
		case ResourceEvent.RESOURCE_QUERY_COMPLETE:
			setSuccessful(true);
			break;
			
		case ResourceEvent.RESOURCE_QUERY_FAILED:
			failException = re.getAttachedException();
			setSuccessful(false);
			break;
			
		case ResourceEvent.RESOURCE_DEBUG_OUT:
			addStatus("Sent request, awaiting reply...");
			break;
			
		case ResourceEvent.RESOURCE_DEBUG_IN:
			addStatus("Reply received, processing...");
			break;
			
		default:
			break;
		}
	}
	
	public void dispose() {
		myResource.removeResourceEventListener(this);
		
		super.dispose();
	}
}