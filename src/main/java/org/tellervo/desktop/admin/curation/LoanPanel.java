package org.tellervo.desktop.admin.curation;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.ui.I18n.TellervoLocale;
import org.tellervo.schema.WSILoan;
import org.tridas.io.util.DateUtils;
import static java.text.DateFormat.*;
import com.ibm.icu.text.SimpleDateFormat;

import java.awt.BorderLayout;
import java.text.DateFormat;
import java.util.Locale;

public class LoanPanel extends JPanel {
	private JTextField txtFirstname;
	private JTextField txtLastName;
	private JTextField txtOrganisation;
	private JTextField txtIssueDate;
	private JTextField txtDueDate;
	private JTextArea txtNotes;
	private JList lstSamples;
	private JList lstFiles;
	private JTextField txtReturned;

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
		panelDetails.setLayout(new MigLayout("", "[right][135px,grow,fill][135px,grow,fill]", "[][][][][][grow]"));
		
		JLabel lblName = new JLabel("Name:");
		panelDetails.add(lblName, "cell 0 0");
		
		txtFirstname = new JTextField();
		panelDetails.add(txtFirstname, "cell 1 0");
		txtFirstname.setColumns(10);
		
		txtLastName = new JTextField();
		panelDetails.add(txtLastName, "cell 2 0");
		txtLastName.setColumns(10);
		
		JLabel lblOrganisation = new JLabel("Organisation:");
		panelDetails.add(lblOrganisation, "cell 0 1");
		
		txtOrganisation = new JTextField();
		panelDetails.add(txtOrganisation, "cell 1 1 2 1");
		txtOrganisation.setColumns(10);
		
		JLabel lblIssued = new JLabel("Date issued");
		panelDetails.add(lblIssued, "cell 0 2");
		
		txtIssueDate = new JTextField();
		panelDetails.add(txtIssueDate, "cell 1 2");
		txtIssueDate.setEditable(false);
		txtIssueDate.setColumns(10);
		
		JLabel lblDateDue = new JLabel("Date due:");
		panelDetails.add(lblDateDue, "cell 0 3,alignx trailing");
		
		txtDueDate = new JTextField();
		panelDetails.add(txtDueDate, "flowx,cell 1 3");
		txtDueDate.setColumns(10);
		
		JLabel lblDateReturned = new JLabel("Date returned:");
		panelDetails.add(lblDateReturned, "cell 0 4,alignx trailing");
		
		txtReturned = new JTextField();
		txtReturned.setColumns(10);
		panelDetails.add(txtReturned, "cell 1 4,growx");
		
		JLabel lblNotes = new JLabel("Notes:");
		panelDetails.add(lblNotes, "cell 0 5,aligny top");
		
		JScrollPane scrollPane = new JScrollPane();
		panelDetails.add(scrollPane, "cell 1 5 2 1,grow");
		
		txtNotes = new JTextArea();
		scrollPane.setViewportView(txtNotes);
		
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
		
		
		
		
		String country = App.prefs.getPref(PrefKey.LOCALE_COUNTRY_CODE, "xxx");
		String language = App.prefs.getPref(PrefKey.LOCALE_LANGUAGE_CODE, "xxx");
		TellervoLocale loc = I18n.getTellervoLocale(country, language);
		
		DateFormat dateFormat =  getDateInstance(LONG, loc.getLocale());
		
		txtFirstname.setText(loan.getFirstname());
		txtLastName.setText(loan.getLastname());
		txtOrganisation.setText(loan.getOrganisation());
		
		txtNotes.setText(loan.getNotes());
		if(loan.isSetDuedate())
		{
			txtDueDate.setText(dateFormat.format(loan.getDuedate().toGregorianCalendar().getTime()));
		}
		else
		{
			txtDueDate.setText("");
		}
		if(loan.isSetIssuedate())
		{
			txtIssueDate.setText(dateFormat.format(loan.getIssuedate().toGregorianCalendar().getTime()));
		}
		else
		{
			txtIssueDate.setText("");
		}
		if(loan.isSetReturndate())
		{
			txtReturned.setText(dateFormat.format(loan.getReturndate().toGregorianCalendar().getTime()));
		}
		else
		{
			txtReturned.setText("");
		}
		
		
	}


}
