package edu.cornell.dendro.corina.gui.newui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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
	
	/**
	 * Did we succeed in creating a new webdb object?
	 * @return
	 */
	public boolean didSucceed() {
		return succeeded;
	}

	/**
	 * Convenience method: Force a JTextField to auto-capitalize 
	 * and not allow any whitespace chars
	 * @param field
	 */
	protected void setCapsNoWhitespace(JTextField field) {
		field.addKeyListener(new KeyAdapter() {
    		public void keyTyped(KeyEvent ke) {
    			char k = ke.getKeyChar();
    			
    			// don't allow any whitespace
    			if(Character.isWhitespace(k)) {
    				ke.consume(); // om nom nom nom!
    				return;
    			}
    			
    			// force uppercase
    			ke.setKeyChar(Character.toUpperCase(k));
    		}
    	});
	}
	
	/**
	 * Only allow number characters, - in front, and one decimal point
	 * @param field
	 * @param canHaveSign
	 */
	protected void setNumbersOnly(JTextField field, boolean canHaveSign) {
		final JTextField gField = field;
		final boolean gCanHaveSign = canHaveSign;
		
		field.addKeyListener(new KeyAdapter() {
    		public void keyTyped(KeyEvent ke) {
    			char k = ke.getKeyChar();

    			// always allow minuses
    			if(Character.isDigit(k))
    				return;
    			
    			// only allow a or at the beginning
    			if(gCanHaveSign && k == KeyEvent.VK_MINUS) {
    				if(gField.getCaretPosition() != 0)
    					ke.consume();
    				return;
    			}
    			
    			// only allow one period
    			if(k == KeyEvent.VK_PERIOD) {
    				String txt = gField.getText();
    				
    				if(txt.indexOf(KeyEvent.VK_PERIOD) != -1)
    					ke.consume();
    				return;
    			}
    			
    			// not here!
    			ke.consume();
    		}
    	});
	}
	
	/**
	 * Convenience function: makes a JTextField select all
	 * when it receives focus
	 * @param field
	 */
	protected void setSelectAllOnFocus(JTextField field) {
		final JTextField glue = field;
		
		field.addFocusListener(new FocusAdapter() {
    		public void focusGained(FocusEvent fe) {
    			glue.selectAll();
    		}
		});
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
				validateButtons();
			}

			public void insertUpdate(DocumentEvent e) {
				validateButtons();
			}
		});
	}
	
	/**
	 * Intended to be overridden by a function that enables/disables buttons.
	 * Called by anything that has been added with setFieldValidateButtons
	 */
	protected void validateButtons() {
		
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
