package org.tellervo.desktop.tridasv2.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.ui.Builder;

public class TridasReferencesPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(TridasReferencesPanel.class);
	private Boolean hasResults;
	private JTextField txtRef;
	private JList<String> lstReferences;
	private JButton btnAdd;
	private JButton btnRemoveSelected;
	private JButton btnSave;
	private JPanel panelBottom;
	private JButton btnCancel;
	private JPanel panel;
    
    
	/**
	 * Create the dialog.
	 */
	public TridasReferencesPanel(ArrayList<String> selectedItems) {	
		
		
		
		setupGUI(selectedItems);
		
	}
		
	
	private void setupGUI(ArrayList<String> selectedlist)
	{
		setLayout(new BorderLayout(0, 0));
		
		
		

		panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);
		
		
			
		lstReferences = new JList<String>();
		scrollPane.setViewportView(lstReferences);
		
		lstReferences.setModel(new DefaultListModel());
		
		
		if(selectedlist!=null){
			for(String s : selectedlist)
			{
				((DefaultListModel)lstReferences.getModel()).addElement(s);	
			}
		}
		
		
		
		JPanel panelRight = new JPanel();
		panel.add(panelRight, BorderLayout.EAST);
		panelRight.setLayout(new MigLayout("", "[fill]", "[]"));
		
		btnAdd = new JButton();
		btnAdd.setIcon(Builder.getIcon("edit_add.png", 16));
		btnAdd.addActionListener(this);
		btnAdd.setActionCommand("Add");
		
		
		panelRight.add(btnAdd, "flowy,cell 0 0");
		
		btnRemoveSelected = new JButton();
		btnRemoveSelected.setIcon(Builder.getIcon("edit_remove.png", 16));
		btnRemoveSelected.setActionCommand("Remove");
		btnRemoveSelected.addActionListener(this);
		panelRight.add(btnRemoveSelected, "cell 0 0");
		
		panelBottom = new JPanel();
		panel.add(panelBottom, BorderLayout.SOUTH);
		panelBottom.setLayout(new MigLayout("", "[grow]", "[]"));
		
		txtRef = new JTextField();
		panelBottom.add(txtRef, "flowx,cell 0 0,growx");
		txtRef.setColumns(10);
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(this);
		btnSave.setActionCommand("Save");
		panelBottom.add(btnSave, "cell 0 0");
		
		btnCancel = new JButton("Cancel");
		btnCancel.setActionCommand("Cancel");
		btnCancel.addActionListener(this);
		panelBottom.add(btnCancel, "cell 0 0");
		setTextPanelEnabled(false);
	}
	
	
	public ArrayList<String> getList()
	{
		ArrayList<String> lst = new ArrayList<String>();
		
		for (int i = 0; i < lstReferences.getModel().getSize(); i++) {
			lst.add(String.valueOf(lstReferences.getModel().getElementAt(i)));
        }
		
		return lst;
	}
			
	
	public Boolean getHasResults() {
		return hasResults;
	}

	public void setHasResults(Boolean hasResults) {
		this.hasResults = hasResults;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Save"))
		{
			log.debug("Saving changes");
			
			if(txtRef.getText()!=null && txtRef.getText().length()>0)
			{
				((DefaultListModel) lstReferences.getModel()).addElement(txtRef.getText());
			}
			
			txtRef.setText("");
			setTextPanelEnabled(false);
		}
		if(e.getActionCommand().equals("Add"))
		{
			log.debug("Add new reference");
			setTextPanelEnabled(true);
			txtRef.setText("");
			
		}
		if(e.getActionCommand().equals("Remove"))
		{
			log.debug("Remove selected reference");
			if(lstReferences.getSelectedIndex()>=0)
			{
				((DefaultListModel) lstReferences.getModel()).removeElementAt(lstReferences.getSelectedIndex());
			}
		}
		if(e.getActionCommand().equals("Cancel"))
		{
			log.debug("Cancel changes");
			txtRef.setText("");
			setTextPanelEnabled(false);
		}
	}
	
	private void setTextPanelEnabled(boolean enabled)
	{
		this.txtRef.setEditable(enabled);
		this.btnSave.setEnabled(enabled);
		this.btnCancel.setEnabled(enabled);
	}
}

	