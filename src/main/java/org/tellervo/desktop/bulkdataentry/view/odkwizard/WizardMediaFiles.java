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
	private JTextField txtFinalPrefix;
	private JCheckBox chkImportMedia;
	private JCheckBox chkRenameFiles;
	private JLabel lblCopyFilesTo;
	private JLabel lblFinalLocation;
	private JButton btnBrowse;

	/**
	 * Create the panel.
	 */
	public WizardMediaFiles() {
		super("Step 2 - Media files", 
				"If you created media files (photos, videos and/or sound files) you need to define how they should be handled. "
				+ "Media files are not stored directly within the Tellervo database, instead the files should be stored in an accessible location "
				+ "(e.g a shared file server or webserver) and links to the files stored instead.  You therefore need to specify where the files should be "
				+ "copied to (e.g. a folder on your computer) but also the final location where these files will be accessed by Tellervo (e.g. a "
				+ "web accessible URL).  Note that if you specify a final location as a local folder on your computer then other Tellervo users will not "
				+ "be able to follow the links. The filename given to the media files by ODK Collect are random so you also have the option of renaming "
				+ "them using the samples codes you specified while collecting.");
		setLayout(new MigLayout("", "[][grow][]", "[23px][][][]"));
		
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
		
		txtFinalPrefix = new JTextField();
		add(txtFinalPrefix, "cell 1 2,growx");
		txtFinalPrefix.setColumns(10);
		
		chkRenameFiles = new JCheckBox("Rename media files to match Tellervo codes?");
		chkRenameFiles.setSelected(true);
		chkRenameFiles.setBackground(Color.WHITE);
		add(chkRenameFiles, "cell 1 3 2 1");
		
		
		
		linkPrefs();


	}
	
	private void setEnableDisable(){
		
		txtCopyTo.setEnabled(chkImportMedia.isSelected());

		lblCopyFilesTo.setEnabled(chkImportMedia.isSelected());
		txtFinalPrefix.setEnabled(chkImportMedia.isSelected());
		btnBrowse.setEnabled(chkImportMedia.isSelected());
		
		lblFinalLocation.setEnabled(chkImportMedia.isSelected());
		txtFinalPrefix.setEnabled(chkImportMedia.isSelected());
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
		return this.txtFinalPrefix.getText();
	}
	
	private void linkPrefs()
	{
		new TextComponentWrapper(txtCopyTo, PrefKey.ODK_COPY_TO, null);
		new TextComponentWrapper(txtFinalPrefix, PrefKey.ODK_FINAL_PREFIX, null);
		new CheckBoxWrapper(chkImportMedia, PrefKey.ODK_IMPORT_MEDIA, true);
		new CheckBoxWrapper(chkRenameFiles, PrefKey.ODK_RENAME_MEDIA, true);
	}


}
