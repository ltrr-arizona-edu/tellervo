package org.tellervo.desktop.bulkdataentry.view.odkwizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.widgets.AbstractWizardPanel;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.prefs.wrappers.TextComponentWrapper;

import net.miginfocom.swing.MigLayout;




public class WizardGetFolder extends AbstractWizardPanel {


	private static final long serialVersionUID = 1L;
	private JTextField txtFolder;

	/**
	 * Create the panel.
	 */
	public WizardGetFolder() {
		super("Step 1 - Import data from...", 
				"The first step is to define where your ODK form instances are located.  The ODK Collect app on your device stores "
				+ "them in the 'odk/instances/ folder in the base of your device's disk.  You may be able to mount your device's disk "
				+ "as a mass storage device via a USB cable, or mount your device's SD-card via a card reader.  Alternatively you may choose "
				+ "to copy this folder across to your computer using an app on your device such as Dropbox, ES File Explorer, PushBullet to name "
				+ "a few.  Through whatever means you choose, ultimately you need to provide Tellervo with the location of the folder containing "
				+ "your ODK data instances.");
		setLayout(new MigLayout("", "[grow]", "[]"));
		
		txtFolder = new JTextField();
		add(txtFolder, "flowx,cell 0 0,growx");
		
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fc = new JFileChooser(App.prefs.getPref(PrefKey.FOLDER_ODK_LAST_READ, null));
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showOpenDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) txtFolder.setText(fc.getSelectedFile().getAbsolutePath());				
			}
			
		});

		add(btnBrowse, "cell 0 0");
		
		linkPrefs();
	}

	public String getODKInstancesFolder()
	{
		return this.txtFolder.getText();
	}
	
	private void linkPrefs()
	{
		new TextComponentWrapper(txtFolder, PrefKey.FOLDER_ODK_LAST_READ, null);

	}
	
}
