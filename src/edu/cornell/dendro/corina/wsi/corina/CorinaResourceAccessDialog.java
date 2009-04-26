package edu.cornell.dendro.corina.wsi.corina;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.wsi.ResourceEvent;
import edu.cornell.dendro.corina.wsi.ResourceEventListener;

public class CorinaResourceAccessDialog extends JDialog implements ResourceEventListener {
	private JList list;
	private CorinaResource myResource;
	private boolean success = false;
	private Exception failException = null;
	private JProgressBar progressBar;
	
	/**
	 * Construct an access dialog with no owner parent (not preferable)
	 * @param resource
	 */
	public CorinaResourceAccessDialog(CorinaResource resource) {
		super();
		
		initialize(resource);
	}

	/**
	 * Construct an access dialog as a parent of a dialog
	 * @param dialog
	 * @param resource
	 */
	public CorinaResourceAccessDialog(Dialog dialog, CorinaResource resource) {
		super(dialog);
		
		initialize(resource);
	}

	/**
	 * Construct an access dialog as a parent of a frame
	 * 
	 * @param frame
	 * @param resource
	 */
	public CorinaResourceAccessDialog(Frame frame, CorinaResource resource) {
		super(frame);
		
		initialize(resource);
	}

	
	private void initialize(CorinaResource resource) {
		resource.setOwnerWindow(this);
		
		// attach to our resource
		this.myResource = resource;
		myResource.addResourceEventListener(this);
		
		// Set dialog defaults
		setModal(true);
		setTitle("Please wait...");
		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		// Create form items
		list = new JList(new DefaultListModel());
		list.setVisibleRowCount(10);
	    list.setBorder(BorderFactory.createTitledBorder("Status"));
	    progressBar = new JProgressBar();
	    progressBar.setIndeterminate(true);
	    progressBar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
	    
	    final CorinaResourceAccessDialog glue = this;
	    progressBar.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent mousee) {
				Object[] options = new String[] { "Abort", "Continue" };
				int ret = JOptionPane.showOptionDialog(progressBar, 
						"Would you like to abort the operation\n" +
						"'" + myResource.getQueryType() + "' on '" + 
						myResource.getResourceName() + "'", 
						"Abort?", 
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.QUESTION_MESSAGE, null, 
						options, options[1]);
				
				if(ret == 0) {
					myResource.removeResourceEventListener(glue);
					myResource.abortQuery();
					
					glue.failException = new UserCancelledException();
					glue.setSuccessful(false);
				}
			}
	    });
	 
	    // Add items to dialog
	    getContentPane().add(list, BorderLayout.CENTER);
	    getContentPane().add(progressBar, BorderLayout.SOUTH);
	    
	    // Add basic nerdy info to list 
		addStatus(myResource.getQueryType() + " on: " + 
				  myResource.getResourceName());
		
		// more nerdy info if this is a ResourceObject
		/*
		if(myResource instanceof ResourceObject<?>) {
			ResourceObject<? >myResourceObject = (ResourceObject<?>) myResource;
			
			if(myResourceObject.getIdentifier() != null)
				addStatus("Identifier: " + myResourceObject.getIdentifier());	

			if(myResourceObject.getSearchParameters() != null)
				addStatus("Search params: " + myResourceObject.getSearchParameters());
		}
		*/
		
		addStatus("Connecting to server...");	
		
		// Hide nerdy info
		list.setVisible(false);
	    
		// Finish up
		pack();
	    //setSize(300, 200);
	    Center.center(this);
		
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