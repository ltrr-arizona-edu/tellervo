/**
 * 
 */
package edu.cornell.dendro.corina.gui.newui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;

import javax.swing.JDialog;
import javax.swing.JPanel;

import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.util.Center;

/**
 * @author lucasm
 *
 */
public class CreateSampleDialog extends JDialog {
	public final static int OPERATION_NEW = 1;
	public final static int OPERATION_SAVEAS = 2;
	
	private int operation = OPERATION_NEW;
	private Sample templateSample;
	private CreateSample content;
	
	/**
	 * @param owner
	 * @param title
	 * @param modal
	 */
	public CreateSampleDialog(Frame owner, boolean modal, Sample templateSample) {
		super(owner, modal);
		this.templateSample = templateSample;
		setupDialog();
	}

	/**
	 * @param owner
	 * @param title
	 * @param modal
	 */
	public CreateSampleDialog(Dialog owner, boolean modal, Sample templateSample) {
		super(owner, modal);
		this.templateSample = templateSample;
		setupDialog();
	}
	
	private void setupDialog() {
		switch(operation) {
		case OPERATION_NEW:
			setTitle("Create a new sample...");
			break;
			
		case OPERATION_SAVEAS:
			setTitle("Save as...");
			break;
		}
		
		content = new CreateSample(this);
		
		JPanel holder = new JPanel();
		holder.add(content, BorderLayout.NORTH);
		holder.add(new PanelImportWizard4(), BorderLayout.SOUTH);
		
		setContentPane(holder);
		pack();
		Center.center(this);
		
		content.initialize(templateSample);
	}
}
