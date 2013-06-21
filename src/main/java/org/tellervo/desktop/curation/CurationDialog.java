package org.tellervo.desktop.curation;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.ui.Builder;
import org.tridas.schema.TridasSample;

public class CurationDialog extends JDialog implements ActionListener{

	private final static Logger log = LoggerFactory.getLogger(CurationDialog.class);

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JButton okButton;
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
	
	public boolean wasChanged()
	{
		return curationPanel.wasChanged();
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
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		
		this.pack();
		this.setIconImage(Builder.getApplicationIcon());
		this.setLocationRelativeTo(parent);
		this.setTitle("Sample Curation");
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		this.setModal(true);
	}

	
	public String getCurrentCurationStatus()
	{
		return curationPanel.getCurrentCurationStatus();
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		
		if (event.getActionCommand().equals("OK"))
		{
			this.setVisible(false);
		}
		
	}

}
