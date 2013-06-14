package org.tellervo.desktop.admin.curation;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.ui.Builder;
import org.tridas.schema.TridasSample;

public class CurationDialog extends JDialog {

	private final static Logger log = LoggerFactory.getLogger(CurationDialog.class);

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JButton okButton;
	private JButton cancelButton;
	private CurationPanel curationPanel;
	private Component parent;
	
	
	/**
	 * Create the dialog.
	 */
	public CurationDialog(TridasSample sample, Component parent) {
		
		if(sample.isSetIdentifier())
		{
			log.debug("CurationDialog instantiated with sample id:"+sample.getIdentifier().getValue());
		}
		else
		{
			log.debug("CurationDialog instantiated with no sample id");
		}
		setupGUI(sample);

	}
	
	private void setupGUI( TridasSample sample){
		setBounds(100, 100, 632, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			curationPanel = new CurationPanel(sample);
			contentPanel.add(curationPanel);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		this.pack();
		this.setIconImage(Builder.getApplicationIcon());
		this.setLocationRelativeTo(parent);
		this.setTitle("Sample Curation");
	}

}
