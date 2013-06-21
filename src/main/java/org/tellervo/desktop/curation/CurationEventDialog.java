package org.tellervo.desktop.curation;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

import org.tellervo.desktop.tridasv2.ui.EnumComboBoxItemRenderer;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tellervo.schema.CurationStatus;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSICuration;
import org.tridas.schema.TridasSample;

public class CurationEventDialog extends JDialog implements ActionListener{


	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private ArrayList<TridasSample> sampleList;
	private JComboBox<CurationStatus> cboCurationStatus;
	private JTextArea txtNotes;
	private JButton btnAdd;
	private JButton btnCancel;
	private final Component parent;

	/**
	 * Create the dialog.
	 * @wbp.parser.constructor
	 */
	public CurationEventDialog(Component parent, TridasSample s) {
		
		this.parent = parent;
		if(s==null){
			dispose();
		}
		sampleList = new ArrayList<TridasSample>();
		sampleList.add(s);
		setupGUI();
		
		
	}
	
	/**
	 * Create the dialog.
	 */
	public CurationEventDialog(Component parent, Collection<TridasSample> samples)
	{
		this.parent = parent;

		if(samples==null || samples.size()==0)
		{
			dispose();
		}
		
		sampleList.addAll(samples);
		setupGUI();
	}
	
	private void setupGUI()
	{
		setBounds(100, 100, 450, 157);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[right][190px:n,grow]", "[][40px:n,grow,top]"));
		{
			JLabel lblCurationStatus = new JLabel("Curation status:");
			contentPanel.add(lblCurationStatus, "cell 0 0,alignx trailing");
		}
		{
			CurationStatus[] statuses = {null, CurationStatus.ARCHIVED, CurationStatus.DESTROYED, CurationStatus.MISSING, CurationStatus.ON___DISPLAY, CurationStatus.RETURNED___TO___OWNER};
			cboCurationStatus = new JComboBox<CurationStatus>();
			cboCurationStatus.setModel(new DefaultComboBoxModel<CurationStatus>(statuses));
			cboCurationStatus.setRenderer(new EnumComboBoxItemRenderer());
			contentPanel.add(cboCurationStatus, "cell 1 0,growx");
		}
		{
			JLabel lblNotes = new JLabel("Notes:");
			contentPanel.add(lblNotes, "cell 0 1,alignx right");
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			contentPanel.add(scrollPane, "cell 1 1,grow");
			{
				txtNotes = new JTextArea();
				txtNotes.setWrapStyleWord(true);
				txtNotes.setLineWrap(true);
				scrollPane.setViewportView(txtNotes);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnAdd = new JButton("Add");
				btnAdd.setActionCommand("Add");
				btnAdd.addActionListener(this);
				buttonPane.add(btnAdd);
				getRootPane().setDefaultButton(btnAdd);
			}
			{
				btnCancel = new JButton("Cancel");
				btnCancel.setActionCommand("Cancel");
				btnCancel.addActionListener(this);
				buttonPane.add(btnCancel);
			}
		}
		
		this.setIconImage(Builder.getApplicationIcon());
		this.setTitle("Add curation event");
		this.setLocationRelativeTo(parent);
		this.setModal(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getActionCommand().equals("Cancel"))
		{
			dispose();
		}
		else if (event.getActionCommand().equals("Add"))
		{
			if(addCurationEvent())
			{
				dispose();
			}
			
		}
		
	}
	
	private boolean addCurationEvent()
	{
		if(cboCurationStatus.getSelectedItem()==null) return false;
		CurationStatus cs = (CurationStatus)cboCurationStatus.getSelectedItem();
	
		// Force notes for interesting situations
		if(txtNotes.getText().equals(null))
		{
			if(cs.equals(CurationStatus.DESTROYED) )
			{
				Alert.message("Details required", "Please provide further details as to why the sample was destroyed");
				return false;
			}
			else if(cs.equals(CurationStatus.ON___DISPLAY) )
			{
				Alert.message("Details required", "Please provide further details as to where this sample is displayed and when it will be returned");
				return false;
			}
		}
		
		
		WSICuration curationEvent = new WSICuration();
		curationEvent.setNotes(txtNotes.getText());
		curationEvent.setStatus(cs);
		curationEvent.setSample(sampleList.get(0));
		
		// Create resource
		EntityResource<WSICuration> resource;
		resource = new EntityResource<WSICuration>(curationEvent, TellervoRequestType.CREATE, WSICuration.class);

		// set up a dialog...
		Window parentWindow = SwingUtilities.getWindowAncestor(getParent());
		TellervoResourceAccessDialog dialog = TellervoResourceAccessDialog.forWindow(parentWindow, resource);			
		resource.query();
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			Alert.error("Error", dialog.getFailException().getMessage());
			return false;
		}
		else
		{
			return true;
		}
	}

}
