package org.tellervo.desktop.bulkdataentry.view.odkwizard;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.widgets.AbstractWizardPanel;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.prefs.wrappers.CheckBoxWrapper;
import org.tellervo.desktop.prefs.wrappers.TextComponentWrapper;


public class WizardMediaFiles extends AbstractWizardPanel {


	private static final long serialVersionUID = 1L;
	private JTextField txtCopyTo;
	private JTextField txtFinalFolder;
	private JCheckBox chkImportMedia;
	private JCheckBox chkRenameFiles;
	private JLabel lblCopyFilesTo;
	private JLabel lblFinalLocation;
	private JButton btnBrowse;
	private JLabel lblFilenamePrefix;
	private JTextField txtFilenamePrefix;

	/**
	 * Create the panel.
	 */
	public WizardMediaFiles() {
		super("Step 2 - Media files", 
				"If you created media files (photos, videos and/or sound files) you need to define how they should be handled by the importer. "
				+ "Media files are not stored directly within the Tellervo database, instead links are stored so you should eventually "
				+ "store the files in an accessible location (e.g. a shared file server or webserver).  The Tellervo importer consolidates the media "
				+ "files by copying them into a single folder ready for you to deploy of your file server.  You have the option of renaming the "
				+ "files to match the sample codes you specified while collecting and you can also add a prefix to the filename.  Once the importer "
				+ "has finished you need to remember to copy the media files from the local folder to the final folder you specified below."
);
		setLayout(new MigLayout("", "[][grow][]", "[23px][][][][]"));

		chkImportMedia = new JCheckBox("Include media files in import?");
		chkImportMedia.setBackground(Color.WHITE);
		chkImportMedia.setSelected(true);
		
		chkImportMedia.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				setEnableDisable();
				
			}
			
		});
		add(chkImportMedia, "cell 1 0 2 1,alignx left,aligny top");
		
		lblCopyFilesTo = new JLabel("Copy files to:");
		add(lblCopyFilesTo, "cell 0 1,alignx trailing");
		
		txtCopyTo = new JTextField();
		add(txtCopyTo, "cell 1 1,growx");
		txtCopyTo.setColumns(10);
		
		btnBrowse = new JButton("Browse");
		
		btnBrowse.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser fc = new JFileChooser(App.prefs.getPref(PrefKey.ODK_COPY_TO, null));
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showOpenDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) txtCopyTo.setText(fc.getSelectedFile().getAbsolutePath());			
			}
			
		});
		
		add(btnBrowse, "cell 2 1");
		
		lblFinalLocation = new JLabel("FInal location:");
		add(lblFinalLocation, "cell 0 2,alignx trailing");
		
		txtFinalFolder = new JTextField();
		add(txtFinalFolder, "cell 1 2,growx");
		txtFinalFolder.setColumns(10);
		
		chkRenameFiles = new JCheckBox("Rename media files to match Tellervo codes?");
		chkRenameFiles.setSelected(true);
		chkRenameFiles.setBackground(Color.WHITE);
		add(chkRenameFiles, "cell 1 3 2 1");
		
		lblFilenamePrefix = new JLabel("Filename prefix:");
		add(lblFilenamePrefix, "cell 0 4,alignx trailing");
		
		txtFilenamePrefix = new JTextField();
		add(txtFilenamePrefix, "cell 1 4,growx");
		txtFilenamePrefix.setColumns(10);
		
		
		
		linkPrefs();


	}
	
	private void setEnableDisable(){
		
		txtCopyTo.setEnabled(chkImportMedia.isSelected());

		lblCopyFilesTo.setEnabled(chkImportMedia.isSelected());
		txtFinalFolder.setEnabled(chkImportMedia.isSelected());
		btnBrowse.setEnabled(chkImportMedia.isSelected());
		
		lblFinalLocation.setEnabled(chkImportMedia.isSelected());
		txtFinalFolder.setEnabled(chkImportMedia.isSelected());
		chkRenameFiles.setEnabled(chkImportMedia.isSelected());
		
		
	}
	
	public boolean isIncludeMediaFilesSelected()
	{
		return this.chkImportMedia.isSelected();
	}
	
	public boolean isRenameMediaFilesSelected()
	{
		return this.chkRenameFiles.isSelected();
	}
	
	public String getCopyToLocation()
	{
		return this.txtCopyTo.getText();
	}
	
	public String getFinalLocation()
	{
		return this.txtFinalFolder.getText();
	}
	
	private void linkPrefs()
	{
		new TextComponentWrapper(txtCopyTo, PrefKey.ODK_COPY_TO, null);
		new TextComponentWrapper(txtFinalFolder, PrefKey.ODK_FINAL_PREFIX, null);
		new CheckBoxWrapper(chkImportMedia, PrefKey.ODK_IMPORT_MEDIA, true);
		new CheckBoxWrapper(chkRenameFiles, PrefKey.ODK_RENAME_MEDIA, true);
	}


}
