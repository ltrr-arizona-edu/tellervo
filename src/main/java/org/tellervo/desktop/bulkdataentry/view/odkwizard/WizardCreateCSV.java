package org.tellervo.desktop.bulkdataentry.view.odkwizard;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.widgets.AbstractWizardPanel;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.prefs.wrappers.CheckBoxWrapper;
import org.tellervo.desktop.prefs.wrappers.TextComponentWrapper;
import org.tellervo.desktop.ui.Alert;

import net.miginfocom.swing.MigLayout;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
				"In addition to adding your ODK data directly to the Tellervo database, you also have the option of creating CSV files too. A file is created for any object data, and another for the element and samples.");
		setLayout(new MigLayout("", "[][grow][]", "[][]"));
		
		chkCreateCSV = new JCheckBox("Create CSV export file(s) in addition to database import?");
		chkCreateCSV.setBackground(Color.WHITE);
		chkCreateCSV.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				setEnableDisable();
				
			}
			
		});
		add(chkCreateCSV, "cell 0 0 2 1");
		
		lblCsvOutputFile = new JLabel("Folder to output CSV files to:");
		add(lblCsvOutputFile, "cell 0 1,alignx trailing");
		
		txtCSVFile = new JTextField();
		add(txtCSVFile, "cell 1 1,growx");
		txtCSVFile.setColumns(10);
		
		btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				pickCSVExportFolder();
				
				
				
				
			}
			
		});
		add(btnBrowse, "cell 2 1");
		
		linkPrefs();


	}
	
	private void pickCSVExportFolder()
	{
		// Alternative using VFSJFileChooser
		/*VFSJFileChooser fc = new VFSJFileChooser();
		fc.setFileSelectionMode(VFSJFileChooser.SELECTION_MODE.DIRECTORIES_ONLY);
		VFSJFileChooser.RETURN_TYPE returnVal = fc.showOpenDialog(getParent());
		fc.setAcceptAllFileFilterUsed(false);
		if (returnVal == VFSJFileChooser.RETURN_TYPE.APPROVE) txtCSVFile.setText(fc.getSelectedFile().getAbsolutePath());*/
		
			
		JFileChooser fc = new JFileChooser(App.prefs.getPref(PrefKey.ODK_CSV_FILENAME, null));
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setAcceptAllFileFilterUsed(false);
		
		int returnVal = fc.showOpenDialog(getParent());

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			
			File f = new File(fc.getSelectedFile().getAbsolutePath());
			
			if(f.exists() && f.isFile())
			{
				Alert.error("Invalid", "You need to pick an output folder");
				pickCSVExportFolder();
				return;
			}
			if(f.exists() && f.isDirectory())
			{
				// Great
				App.prefs.setPref(PrefKey.ODK_CSV_FILENAME, fc.getSelectedFile().getAbsolutePath());
			}
			else if (f.exists()==false)
			{
				try{
				f.mkdir();
				} catch (Exception e)
				{
					Alert.error("Error", "Failed to create output folder");
				}
			}
			
			txtCSVFile.setText(fc.getSelectedFile().getAbsolutePath());	
		}
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

	
	@Override
	public boolean doPageValidation(){
		
		if(!this.chkCreateCSV.isSelected()) return true;

		
		File folder = new File(txtCSVFile.getText());
		if(folder.exists() && folder.isFile())
		{
			Alert.error("Error", "Export folder must be a folder and not a file");
			return false;
		}
		if(folder.exists()==false)
		{
			try{
				folder.mkdir();
				} catch (Exception e)
				{
					Alert.error("Error", "Failed to create output folder");
					return false;
				}
		}
		
		if(folder.canWrite())
		{
			return true;
		}
		else
		{
			Alert.error("Error", "Output folder is not writeable");
			return false;
		}
	}

}
