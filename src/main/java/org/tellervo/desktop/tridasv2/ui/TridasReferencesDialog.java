package org.tellervo.desktop.tridasv2.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.ui.Builder;
import org.tridas.schema.ControlledVoc;


public class TridasReferencesDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(TridasReferencesDialog.class);

	private final TridasReferencesPanel contentPanel;

	
	/**
	 * Create the dialog.
	 */
	public TridasReferencesDialog(Component parent, ArrayList<String> lsst) {
		
		contentPanel = new TridasReferencesPanel(lsst);
		
		
		getContentPane().add(contentPanel);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		contentPanel.add(panel, BorderLayout.SOUTH);
		
		JButton btnOk = new JButton("OK");
		btnOk.setActionCommand("OK");
		btnOk.addActionListener(this);
		panel.add(btnOk);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setActionCommand("Cancel");
		btnCancel.addActionListener(this);
		panel.add(btnCancel);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 491, 368);
		
		this.setModal(true);
		this.setUndecorated(true);
		
		this.pack();
		this.setTitle("File references");
		this.setIconImage(Builder.getApplicationIcon());
		this.setBounds(parent.getLocationOnScreen().x, 
				parent.getLocationOnScreen().y, 
				300, 
				300);
		

		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent ev) {
		if(ev.getActionCommand()=="Cancel")
		{
			contentPanel.setHasResults(false);
			this.dispose();
		}
		if(ev.getActionCommand()=="OK")
		{	
			contentPanel.setHasResults(true);
			this.dispose();
		}
	}
	
	public ArrayList<String> getList()
	{
		return contentPanel.getList();
	}

	
	public Boolean hasResults()
	{
		return contentPanel.getHasResults();
	}
	

	
}

	