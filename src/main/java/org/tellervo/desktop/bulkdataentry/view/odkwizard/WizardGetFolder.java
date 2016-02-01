package org.tellervo.desktop.bulkdataentry.view.odkwizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.widgets.AbstractWizardPanel;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.prefs.wrappers.TextComponentWrapper;

import net.miginfocom.swing.MigLayout;

import javax.swing.JRadioButton;
import javax.swing.JPanel;

import java.awt.Color;




public class WizardGetFolder extends AbstractWizardPanel implements ActionListener {


	private static final long serialVersionUID = 1L;
	private JTextField txtFolder;
	private JRadioButton radLocal;
	private JRadioButton radDownload;
	private JButton btnBrowse;

	/**
	 * Create the panel.
	 */
	public WizardGetFolder() {
		super("Step 1 - Import data from...", 
				"The first step is to define where your ODK form instances are located.  ODK form instances can be accessed from your "
				+ "hard disk or from your Tellervo server if you have uploaded them there.");
		setLayout(new MigLayout("", "[grow]", "[][][][grow]"));
		
		radDownload = new JRadioButton("Download from server");
		radDownload.setSelected(true);
		radDownload.setBackground(Color.WHITE);
		radDownload.addActionListener(this);
		radDownload.setActionCommand("RadioChange");
		add(radDownload, "cell 0 0");
		
		radLocal = new JRadioButton("Files in local folder");
		radLocal.setBackground(Color.WHITE);
		radLocal.addActionListener(this);
		radLocal.setActionCommand("RadioChange");
		add(radLocal, "cell 0 1");
		
		txtFolder = new JTextField();
		txtFolder.setEnabled(false);
		add(txtFolder, "flowx,cell 0 2,growx");
		
		
		btnBrowse = new JButton("Browse");
		btnBrowse.setEnabled(false);
		btnBrowse.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fc = new JFileChooser(App.prefs.getPref(PrefKey.FOLDER_ODK_LAST_READ, null));
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showOpenDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) txtFolder.setText(fc.getSelectedFile().getAbsolutePath());				
			}
			
		});

		add(btnBrowse, "cell 0 2");
		
		linkPrefs();
		updateGUI();
	}

	/**
	 * Returns true is we're getting data from the server, or false if ODK files are stored locally
	 * 
	 * @return
	 */
	public boolean isRemoteAccessSelected()
	{
		return radDownload.isSelected();
	}
	
	public String getODKInstancesFolder()
	{
		return this.txtFolder.getText();
	}
	
	private void updateGUI()
	{
		txtFolder.setEnabled(radLocal.isSelected());
		btnBrowse.setEnabled(radLocal.isSelected());
		
	}
	
	private void linkPrefs()
	{
		ButtonGroup bg = new ButtonGroup();
		bg.add(radDownload);
		bg.add(radLocal);
		
		new TextComponentWrapper(txtFolder, PrefKey.FOLDER_ODK_LAST_READ, null);

	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		
		if(evt.getActionCommand().equals("RadioChange"))
		{
			updateGUI();
		}
	}

}
