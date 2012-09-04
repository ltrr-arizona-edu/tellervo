package org.tellervo.desktop.prefs.panels;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JDialog;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.prefs.wrappers.CheckBoxWrapper;
import org.tellervo.desktop.prefs.wrappers.TextComponentWrapper;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.ExtensionFileFilter;

import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.JTextField;
import javax.swing.JButton;

public class SoundPrefsPanel extends AbstractPreferencesPanel {

	private static final long serialVersionUID = 1L;
	private JCheckBox chkEnableSoundSystem;
	private JCheckBox chkUseSystemDefaults;
	
	private JTextField txtPlatformInit;
	private JTextField txtRingMeasured;
	private JTextField txtDecadeMeasured;
	private JTextField txtMeasurementError;
	private JTextField txtBarcodeScanned;
	
	private JButton btnBrowseRingMeasured;
	private JButton btnBrowseDecadeMeasured;
	private JButton btnBrowsePlatformInit;
	private JButton btnBrowseMeasurementError;
	private JButton btnBrowseBarcodeScanned;
	
	
	public SoundPrefsPanel(final JDialog parent) {
		super(I18n.getText("preferences.sound"), 
				"sound.png", 
				"Configure sounds within Tellervo",
				parent);
		setLayout(new MigLayout("", "[186px,grow]", "[15px][grow]"));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Sound system", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(panel, "cell 0 0,grow");
		panel.setLayout(new MigLayout("", "[][165.00,grow][]", "[center][][36.00][][][][][][]"));
		
		JLabel lblEnableSoundsIn = new JLabel("Enable sounds in Tellervo:");
		panel.add(lblEnableSoundsIn, "cell 0 0,alignx right,aligny center");
		
		chkEnableSoundSystem = new JCheckBox("");
		panel.add(chkEnableSoundSystem, "cell 1 0 2 1,aligny center");
		chkEnableSoundSystem.setSelected(true);
		
		JLabel lblNewLabel = new JLabel("Use system defaults:");
		panel.add(lblNewLabel, "cell 0 1,alignx right");
		
		chkUseSystemDefaults = new JCheckBox("");
		chkUseSystemDefaults.setSelected(true);
		panel.add(chkUseSystemDefaults, "cell 1 1");
		chkUseSystemDefaults.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setGuiFromPref();
			}
			
		});
		
		JLabel lblPlatformInit = new JLabel("Platform initalization:");
		panel.add(lblPlatformInit, "cell 0 3,alignx trailing");
		
		txtPlatformInit = new JTextField();
		panel.add(txtPlatformInit, "cell 1 3,growx");
		txtPlatformInit.setColumns(10);
		
		btnBrowsePlatformInit = new JButton("...");
		panel.add(btnBrowsePlatformInit, "cell 2 3");

		btnBrowsePlatformInit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				txtPlatformInit.setText(getFileChooserFilename());
			}
		});
		
		JLabel lblRingMeasured = new JLabel("Ring measured:");
		panel.add(lblRingMeasured, "cell 0 4,alignx trailing");
		
		txtRingMeasured = new JTextField();
		panel.add(txtRingMeasured, "cell 1 4,growx");
		txtRingMeasured.setColumns(10);
		
		btnBrowseRingMeasured = new JButton("...");
		panel.add(btnBrowseRingMeasured, "cell 2 4");
		
		btnBrowseRingMeasured.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				txtRingMeasured.setText(getFileChooserFilename());
			}
		});
		
		JLabel lblDecadeMeasured = new JLabel("Decade measured:");
		panel.add(lblDecadeMeasured, "cell 0 5,alignx trailing");
		
		txtDecadeMeasured = new JTextField();
		panel.add(txtDecadeMeasured, "cell 1 5,growx");
		txtDecadeMeasured.setColumns(10);
		
		btnBrowseDecadeMeasured = new JButton("...");
		panel.add(btnBrowseDecadeMeasured, "cell 2 5");
		
		btnBrowseDecadeMeasured.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				txtDecadeMeasured.setText(getFileChooserFilename());
			}
		});
		
		JLabel lblMeasurementError = new JLabel("Measurement error:");
		panel.add(lblMeasurementError, "cell 0 6,alignx trailing");
		
		txtMeasurementError = new JTextField();
		panel.add(txtMeasurementError, "cell 1 6,growx");
		txtMeasurementError.setColumns(10);
		
		btnBrowseMeasurementError = new JButton("...");
		panel.add(btnBrowseMeasurementError, "cell 2 6");
		
		btnBrowseMeasurementError.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				txtMeasurementError.setText(getFileChooserFilename());
			}
		});
		
		JLabel lblBarcodeScanned = new JLabel("Barcode scanned:");
		panel.add(lblBarcodeScanned, "cell 0 7,alignx trailing");
		
		txtBarcodeScanned = new JTextField();
		panel.add(txtBarcodeScanned, "cell 1 7,growx");
		txtBarcodeScanned.setColumns(10);
		
		btnBrowseBarcodeScanned = new JButton("...");
		panel.add(btnBrowseBarcodeScanned, "cell 2 7");
		
		btnBrowseBarcodeScanned.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				txtBarcodeScanned.setText(getFileChooserFilename());
			}
		});
		
		linkToPrefs();
		setGuiFromPref();
	}

	/**
	 * Simple JFileChooser for picking a sound file
	 *  
	 * @return
	 */
	private String getFileChooserFilename()
	{
		JFileChooser fc = new JFileChooser();
		
		// Set up the file filter
		FileFilter filter1 = new ExtensionFileFilter("Sound files", new String[] { "wav", "au", "aiff", "rmf" });
		fc.setFileFilter(filter1);
		
		// Try and go to the last folder used
		if(App.prefs.getPref(PrefKey.SOUND_LAST_FOLDER, null)!=null)
		{
			fc.setSelectedFile(new File(App.prefs.getPref(PrefKey.SOUND_LAST_FOLDER, null)));
		}
		
		// Show the dialog
		fc.showOpenDialog(parent);
		
		try{
			// Save the folder the file is from for next time
			App.prefs.setPref(PrefKey.SOUND_LAST_FOLDER, fc.getSelectedFile().getParent());
			
			// Return the file name
			return fc.getSelectedFile().getAbsolutePath();
		} catch (Exception e)
		{
			return null;
		}
	}
	
	private void setGuiFromPref()
	{
		txtPlatformInit.setEnabled(!chkUseSystemDefaults.isSelected());
		txtRingMeasured.setEnabled(!chkUseSystemDefaults.isSelected());
		txtDecadeMeasured.setEnabled(!chkUseSystemDefaults.isSelected());
		txtMeasurementError.setEnabled(!chkUseSystemDefaults.isSelected());
		txtBarcodeScanned.setEnabled(!chkUseSystemDefaults.isSelected());
		
		btnBrowseRingMeasured.setEnabled(!chkUseSystemDefaults.isSelected());
		btnBrowseDecadeMeasured.setEnabled(!chkUseSystemDefaults.isSelected());
		btnBrowsePlatformInit.setEnabled(!chkUseSystemDefaults.isSelected());
		btnBrowseMeasurementError.setEnabled(!chkUseSystemDefaults.isSelected());
		btnBrowseBarcodeScanned.setEnabled(!chkUseSystemDefaults.isSelected());
	}
	
	@Override
	public void refresh() {
		
	}
	
	private void linkToPrefs()
	{
		new CheckBoxWrapper(chkEnableSoundSystem, PrefKey.SOUND_ENABLED, true);
		new CheckBoxWrapper(chkUseSystemDefaults, PrefKey.SOUND_USE_SYSTEM_DEFAULTS, true);
		new TextComponentWrapper(txtPlatformInit, PrefKey.SOUND_PLATFORM_INIT_FILE, null);
		new TextComponentWrapper(txtRingMeasured, PrefKey.SOUND_MEASURE_RING_FILE, null);
		new TextComponentWrapper(txtDecadeMeasured, PrefKey.SOUND_MEASURE_DECADE_FILE, null);
		new TextComponentWrapper(txtMeasurementError, PrefKey.SOUND_MEASURE_ERROR_FILE, null);
		new TextComponentWrapper(txtBarcodeScanned, PrefKey.SOUND_BARCODE_FILE, null);

	}
}
