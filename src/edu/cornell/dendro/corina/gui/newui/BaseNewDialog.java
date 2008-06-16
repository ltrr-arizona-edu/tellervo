package edu.cornell.dendro.corina.gui.newui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import edu.cornell.dendro.corina.site.GenericIntermediateObject;
import edu.cornell.dendro.corina.site.Subsite;
import edu.cornell.dendro.corina.webdbi.IntermediateResource;
import edu.cornell.dendro.corina.webdbi.PrototypeLoadDialog;

/**
 * This is a convenience class to make it so that we can easily 
 * do the basic tasks of each of our 'new' dialogs
 * 
 * @author lucasm
 *
 */

public abstract class BaseNewDialog<OBJT> extends JDialog {

	// the new object that we've created
	private OBJT newObject;
	
	// did we succeed in our create query?
	private boolean succeeded = false;
	
	/**
	 * Set the new object to create
	 * @param obj
	 */
	protected void setNewObject(OBJT obj) {
		this.newObject = obj;
	}
	
	public OBJT getNewObject() {
		return newObject;
	}
	
	/**
	 * Query our webservice to create this object
	 * 
	 * @param resource
	 * @returns true if success
	 */
	protected boolean createObject(IntermediateResource resource) {
		PrototypeLoadDialog dlg = new PrototypeLoadDialog(resource);
		
		// start our query (remotely)
		resource.query();		
		
		dlg.setVisible(true);
		
		if(!dlg.isSuccessful()) {
			JOptionPane.showMessageDialog(this, "Could not create: " + dlg.getFailException(), 
					"Failed to create", JOptionPane.ERROR_MESSAGE);
		}
		else				
			succeeded = true;

		return succeeded;
	}
	
	public boolean didSucceed() {
		return succeeded;
	}
	
	public BaseNewDialog() throws HeadlessException {
		// TODO Auto-generated constructor stub
	}

	public BaseNewDialog(Frame owner) throws HeadlessException {
		super(owner);
		// TODO Auto-generated constructor stub
	}

	public BaseNewDialog(Dialog owner) throws HeadlessException {
		super(owner);
		// TODO Auto-generated constructor stub
	}

	public BaseNewDialog(Frame owner, boolean modal) throws HeadlessException {
		super(owner, modal);
		// TODO Auto-generated constructor stub
	}

	public BaseNewDialog(Frame owner, String title) throws HeadlessException {
		super(owner, title);
		// TODO Auto-generated constructor stub
	}

	public BaseNewDialog(Dialog owner, boolean modal) throws HeadlessException {
		super(owner, modal);
		// TODO Auto-generated constructor stub
	}

	public BaseNewDialog(Dialog owner, String title) throws HeadlessException {
		super(owner, title);
		// TODO Auto-generated constructor stub
	}

	public BaseNewDialog(Frame owner, String title, boolean modal) throws HeadlessException {
		super(owner, title, modal);
		// TODO Auto-generated constructor stub
	}

	public BaseNewDialog(Dialog owner, String title, boolean modal) throws HeadlessException {
		super(owner, title, modal);
		// TODO Auto-generated constructor stub
	}

	public BaseNewDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		// TODO Auto-generated constructor stub
	}

	public BaseNewDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) throws HeadlessException {
		super(owner, title, modal, gc);
		// TODO Auto-generated constructor stub
	}

}
