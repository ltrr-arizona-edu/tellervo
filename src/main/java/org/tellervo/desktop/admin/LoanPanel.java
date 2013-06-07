package org.tellervo.desktop.admin;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;

import org.tellervo.schema.WSILoan;

import java.awt.BorderLayout;

public class LoanPanel extends JPanel {
	private JTextField txtFirstname;
	private JTextField txtLastName;
	private JTextField txtOrganisation;
	private JTextField textField;
	private JTextField txtDueDate;
	private JTextArea textArea;
	private JList lstSamples;
	private JList lstFiles;

	/**
	 * Create the panel.
	 */
	public LoanPanel() {
		
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
		add(tabbedPane);
		
		JPanel panelDetails = new JPanel();
		tabbedPane.addTab("Details", null, panelDetails, null);
		panelDetails.setLayout(new MigLayout("", "[right][grow,fill][grow,fill][]", "[][][][grow]"));
		
		JLabel lblName = new JLabel("Name:");
		panelDetails.add(lblName, "cell 0 0");
		
		txtFirstname = new JTextField();
		panelDetails.add(txtFirstname, "cell 1 0");
		txtFirstname.setColumns(10);
		
		txtLastName = new JTextField();
		panelDetails.add(txtLastName, "cell 2 0 2 1");
		txtLastName.setColumns(10);
		
		JLabel lblOrganisation = new JLabel("Organisation:");
		panelDetails.add(lblOrganisation, "cell 0 1");
		
		txtOrganisation = new JTextField();
		panelDetails.add(txtOrganisation, "cell 1 1 3 1");
		txtOrganisation.setColumns(10);
		
		JLabel lblIssuedduereturned = new JLabel("Date issued/due:");
		panelDetails.add(lblIssuedduereturned, "cell 0 2");
		
		textField = new JTextField();
		panelDetails.add(textField, "cell 1 2");
		textField.setEditable(false);
		textField.setColumns(10);
		
		txtDueDate = new JTextField();
		panelDetails.add(txtDueDate, "flowx,cell 2 2");
		txtDueDate.setColumns(10);
		
		JButton btnDueDate = new JButton("...");
		panelDetails.add(btnDueDate, "cell 3 2");
		
		JLabel lblNotes = new JLabel("Notes:");
		panelDetails.add(lblNotes, "cell 0 3,aligny top");
		
		JScrollPane scrollPane = new JScrollPane();
		panelDetails.add(scrollPane, "cell 1 3 3 1,grow");
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		JPanel panelSamples = new JPanel();
		tabbedPane.addTab("Samples", null, panelSamples, null);
		panelSamples.setLayout(new MigLayout("", "[grow][fill]", "[][][grow][]"));
		
		JScrollPane scrollPaneSamples = new JScrollPane();
		panelSamples.add(scrollPaneSamples, "cell 0 0 1 4,grow");
		
		lstSamples = new JList();
		scrollPaneSamples.setViewportView(lstSamples);
		
		JButton btnAddSample = new JButton("+");
		panelSamples.add(btnAddSample, "cell 1 0");
		
		JButton btnRemoveSample = new JButton("-");
		panelSamples.add(btnRemoveSample, "cell 1 1");
		
		JButton btnViewSample = new JButton("View");
		panelSamples.add(btnViewSample, "cell 1 3");
		
		JPanel panelFiles = new JPanel();
		tabbedPane.addTab("Associated Files", null, panelFiles, null);
		panelFiles.setLayout(new MigLayout("", "[grow][fill]", "[][][grow][]"));
		
		JScrollPane scrollPaneFiles = new JScrollPane();
		panelFiles.add(scrollPaneFiles, "cell 0 0 1 4,grow");
		
		lstFiles = new JList();
		scrollPaneFiles.setViewportView(lstFiles);
		
		JButton btnAddFile = new JButton("+");
		panelFiles.add(btnAddFile, "cell 1 0");
		
		JButton btnRemoveFile = new JButton("-");
		panelFiles.add(btnRemoveFile, "cell 1 1");
		
		JButton btnViewFile = new JButton("View");
		panelFiles.add(btnViewFile, "cell 1 3");
		
	}
	
	public void setLoan(WSILoan loan)
	{
		if(loan==null) return;
		
		txtFirstname.setText(loan.getFirstname());
		txtLastName.setText(loan.getLastname());
		txtOrganisation.setText(loan.getOrganisation());
		
	}

}
