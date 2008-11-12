package edu.cornell.dendro.corina.gui.datawizard;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIDefaults;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import edu.cornell.dendro.corina.site.GenericIntermediateObject;
import edu.cornell.dendro.corina.site.Radius;
import edu.cornell.dendro.corina.site.Site;
import edu.cornell.dendro.corina.site.Specimen;
import edu.cornell.dendro.corina.site.Subsite;
import edu.cornell.dendro.corina.site.Tree;
import edu.cornell.dendro.corina.webdbi.IntermediateResource;
import edu.cornell.dendro.corina.webdbi.PrototypeLoadDialog;

/**
 * This is a convenience class to make it so that we can easily 
 * do the basic tasks of each of our 'new' dialogs
 * 
 * @author lucasm
 *
 */

public abstract class BaseEditorPanel<OBJT> extends BasePanel {

	// the new object that we've created
	private OBJT newObject;
	// our parent object, if any
	private Object parentObject;
	// the object we're updating (instead of creating), if any
	private Object updatingObject;
	
	// did we succeed in our create query?
	// normally, we shouldn't touch this!
	protected boolean succeeded = false;
	
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
	 * Sets the parent object for this dialog. 
	 * childClass should be the class you are setting the parent object of
	 * (e.g., childClass should be Subsite for a Site parent object)
	 * @param obj
	 * @param childClass
	 */
	public void setParentObject(Object obj, Class<?> childClass) {
		boolean valid = false;
		
		if(childClass.equals(Subsite.class) && obj.getClass().equals(Site.class)) 
			valid = true;
		else if(childClass.equals(Tree.class) && obj.getClass().equals(Subsite.class)) 
			valid = true;
		else if(childClass.equals(Specimen.class) && obj.getClass().equals(Tree.class)) 
			valid = true;
		else if(childClass.equals(Radius.class) && obj.getClass().equals(Specimen.class)) 
			valid = true;
		/*
		 * maybe for measurements?
		else if(childClass.equals(Subsite.class) && obj.getClass().equals(Radius.class)) 
			valid = true;
			*/

		if(!valid)
			throw new IllegalArgumentException("Object (" + obj.getClass() + ") is not a parent for child ("+ getClass() +")");
		
		this.parentObject = obj;
	}
	
	protected GenericIntermediateObject getParentObject() {
		return (GenericIntermediateObject) parentObject;
	}
	
	/**
	 * Populate our dialog using any necessary information from our parent dialog
	 */
	public void populate() {
		
	}

	/**
	 * Populate our dialog using the supplied prefix
	 */
	public void populate(String parentPrefix) {
		
	}

	/**
	 * Query our webservice to create this object
	 * 
	 * @param resource
	 * @returns true if success
	 */
	protected boolean createOrUpdateObject(IntermediateResource resource) {
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
	
	/**
	 * Did we succeed in creating a new webdb object?
	 * @return
	 */
	public boolean didSucceed() {
		return succeeded;
	}

	/**
	 * Makes this JTextField call our validateButtons() method
	 * every time its contents change.
	 * 
	 * @param field
	 */
	protected void setFieldValidateButtons(JTextField field) {
		field.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				// hello stupid bug.. this isn't used on JTextFields for some awful reason
			}

			public void removeUpdate(DocumentEvent e) {
				validateForm();
			}

			public void insertUpdate(DocumentEvent e) {
				validateForm();
			}
		});
	}

	/**
	 * Return the value of our name field, if any
	 * @return our name field or null
	 */
	public String getLocalName() {
		return null;
	}
	
	/**
	 * Save our changes to the db
	 */
	public void commit() {
		System.out.println("Commit not yet implemented :(");
	}
	
	private WizardChildMonitor wizardToNotify;
	/**
	 * 
	 * @param wizardToNotify
	 */
	public void setWizardToNotify(WizardChildMonitor wizardToNotify) {
		this.wizardToNotify = wizardToNotify;
	}
	
	/**
	 * Called to notify the wizard our 'ok' state has changed
	 */
	protected void notifyWizard() {
		if(wizardToNotify != null)
			wizardToNotify.notifyChildFormStateChanged();
	}

	// default to not validating
	private boolean formIsValidated = false;
	
	protected void setFormValidated(boolean validated) {
		if(validated != formIsValidated) {
			this.formIsValidated = validated;
			notifyWizard();
		}
	}
	
	public boolean isFormValidated() {
		return formIsValidated;
	}
	
	// apply any defaults from this object
	public void setDefaultsFrom(OBJT obj) {
		
	}
	
	/**
	 * Causes the dialog to update the resource given instead of
	 * creating a new resource.
	 * 
	 * @param obj
	 */
	public void setUpdateObject(OBJT obj) {
		if(obj instanceof GenericIntermediateObject) {
			if(((GenericIntermediateObject) obj).isNew())
				throw new IllegalArgumentException("Trying to create an update dialog for a new resource!");
		}
		
		updatingObject = obj;
	}
	
	/**
	 * Steal the identify of our updating object, if it's set
	 * @param obj
	 */
	protected void assimilateUpdateObject(OBJT obj) {
		if(updatingObject == null)
			return;
		
		((GenericIntermediateObject) obj).assimilateIntermediateObject((GenericIntermediateObject) updatingObject);
	}
	
	/**
	 * Intended to be overridden by a function that enables/disables buttons.
	 * Called by anything that has been added with setFieldValidateButtons
	 */
	protected void validateForm() {
		
	}
	
	public BaseEditorPanel() throws HeadlessException {
		setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 30));
	}

	/*
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
	*/
	
	/**
	 * AWFUL KLUDGE!!!
	 * This method to be renamed when refactoring is complete!
	 * @deprecated
	 */
	protected Container getContentPane() {
		return this;
	}
	
	/**
	 * @deprecated
	 * @param stupid
	 */
	protected void setDefaultCloseOperation(int stupid) {
		return;
	}
	
	/**
	 * @deprecated
	 */
	protected void dispose() {
		
	}
	
	/**
	 * @deprecated
	 */
	protected void pack() {
		
	}
	
	public void setTitle(String title) {
	}
}
