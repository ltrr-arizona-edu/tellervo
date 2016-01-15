package org.tellervo.desktop.bulkdataentry.view.odkwizard;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.widgets.AbstractWizardPanel;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.prefs.wrappers.CheckBoxWrapper;
import org.tellervo.desktop.prefs.wrappers.TextComponentWrapper;

import net.miginfocom.swing.MigLayout;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;


public class WizardCreateCSV extends AbstractWizardPanel {


	private static final long serialVersionUID = 1L;
	private JTextField txtCSVFile;
	private JCheckBox chkCreateCSV;
	private JLabel lblCsvOutputFile;
	private JButton btnBrowse;

	/**
	 * Create the panel.
	 */
	public WizardCreateCSV() {
		super("Step 3 - Create CSV file", 
				"In addition to adding your ODK data directly to the Tellervo database, you also have the option of creating CSV files too.");
		setLayout(new MigLayout("", "[][grow][]", "[][]"));
		
		chkCreateCSV = new JCheckBox("Create CSV export file in addition to database import?");
		chkCreateCSV.setBackground(Color.WHITE);
		chkCreateCSV.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				setEnableDisable();
				
			}
			
		});
		add(chkCreateCSV, "cell 0 0 2 1");
		
		lblCsvOutputFile = new JLabel("CSV output file:");
		add(lblCsvOutputFile, "cell 0 1,alignx trailing");
		
		txtCSVFile = new JTextField();
		add(txtCSVFile, "cell 1 1,growx");
		txtCSVFile.setColumns(10);
		
		btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fc = new JFileChooser(App.prefs.getPref(PrefKey.ODK_CSV_FILENAME, null));
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showOpenDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) txtCSVFile.setText(fc.getSelectedFile().getAbsolutePath());	
				
			}
			
		});
		add(btnBrowse, "cell 2 1");
		
		linkPrefs();


	}
	
	private void setEnableDisable()
	{
		lblCsvOutputFile.setEnabled(chkCreateCSV.isSelected());
		txtCSVFile.setEnabled(chkCreateCSV.isSelected());
		btnBrowse.setEnabled(chkCreateCSV.isSelected());
	}
	
	private void linkPrefs()
	{
		new TextComponentWrapper(txtCSVFile, PrefKey.ODK_CSV_FILENAME, null);
		new CheckBoxWrapper(chkCreateCSV, PrefKey.ODK_CREATE_CSV, false);
	}
	
	public boolean isCreateCSVFileSelected()
	{
		return this.chkCreateCSV.isSelected();
	}
		
	public String getCSVFilename()
	{
		return this.txtCSVFile.getText();
	}


}
