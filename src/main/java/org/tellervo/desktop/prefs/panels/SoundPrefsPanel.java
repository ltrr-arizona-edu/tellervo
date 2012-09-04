package org.tellervo.desktop.prefs.panels;

import javax.swing.JDialog;

import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.prefs.wrappers.CheckBoxWrapper;
import org.tellervo.desktop.ui.I18n;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import javax.swing.JButton;

public class SoundPrefsPanel extends AbstractPreferencesPanel {

	private static final long serialVersionUID = 1L;
	private JCheckBox chkEnableSoundSystem;
	private JTextField txtPlatformInit;
	private JTextField txtRingMeasured;
	private JTextField txtDecadeMeasured;
	private JTextField txtMeasurementError;
	private JTextField txtBarcodeScanned;
	
	
	public SoundPrefsPanel(final JDialog parent) {
		super(I18n.getText("preferences.sound"), 
				"sound.png", 
				"Configure sounds within Tellervo",
				parent);
		setLayout(new MigLayout("", "[186px,grow]", "[15px][grow]"));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Sound system", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(panel, "cell 0 0,grow");
		panel.setLayout(new MigLayout("", "[][165.00,grow][]", "[center][][][][][][][]"));
		
		JLabel lblEnableSoundsIn = new JLabel("Enable sounds in Tellervo:");
		panel.add(lblEnableSoundsIn, "cell 0 0,aligny center");
		
		chkEnableSoundSystem = new JCheckBox("");
		panel.add(chkEnableSoundSystem, "cell 1 0 2 1,aligny center");
		chkEnableSoundSystem.setSelected(true);
		
		JLabel lblPlatformInit = new JLabel("Platform initalization:");
		lblPlatformInit.setEnabled(false);
		panel.add(lblPlatformInit, "cell 0 2,alignx trailing");
		
		txtPlatformInit = new JTextField();
		txtPlatformInit.setEnabled(false);
		panel.add(txtPlatformInit, "cell 1 2,growx");
		txtPlatformInit.setColumns(10);
		
		JButton btnBrowsePlatformInit = new JButton("...");
		btnBrowsePlatformInit.setEnabled(false);
		panel.add(btnBrowsePlatformInit, "cell 2 2");
		
		JLabel lblRingMeasured = new JLabel("Ring measured:");
		lblRingMeasured.setEnabled(false);
		panel.add(lblRingMeasured, "cell 0 3,alignx trailing");
		
		txtRingMeasured = new JTextField();
		txtRingMeasured.setEnabled(false);
		panel.add(txtRingMeasured, "cell 1 3,growx");
		txtRingMeasured.setColumns(10);
		
		JButton btnBrowseRingMeasured = new JButton("...");
		btnBrowseRingMeasured.setEnabled(false);
		panel.add(btnBrowseRingMeasured, "cell 2 3");
		
		JLabel lblDecadeMeasured = new JLabel("Decade measured:");
		lblDecadeMeasured.setEnabled(false);
		panel.add(lblDecadeMeasured, "cell 0 4,alignx trailing");
		
		txtDecadeMeasured = new JTextField();
		txtDecadeMeasured.setEnabled(false);
		panel.add(txtDecadeMeasured, "cell 1 4,growx");
		txtDecadeMeasured.setColumns(10);
		
		JButton btnBrowseDecadeMeasured = new JButton("...");
		btnBrowseDecadeMeasured.setEnabled(false);
		panel.add(btnBrowseDecadeMeasured, "cell 2 4");
		
		JLabel lblMeasurementError = new JLabel("Measurement error:");
		lblMeasurementError.setEnabled(false);
		panel.add(lblMeasurementError, "cell 0 5,alignx trailing");
		
		txtMeasurementError = new JTextField();
		txtMeasurementError.setEnabled(false);
		panel.add(txtMeasurementError, "cell 1 5,growx");
		txtMeasurementError.setColumns(10);
		
		JButton btnBrowseMeasurementError = new JButton("...");
		btnBrowseMeasurementError.setEnabled(false);
		panel.add(btnBrowseMeasurementError, "cell 2 5");
		
		JLabel lblBarcodeScanned = new JLabel("Barcode scanned:");
		lblBarcodeScanned.setEnabled(false);
		panel.add(lblBarcodeScanned, "cell 0 6,alignx trailing");
		
		txtBarcodeScanned = new JTextField();
		txtBarcodeScanned.setEnabled(false);
		panel.add(txtBarcodeScanned, "cell 1 6,growx");
		txtBarcodeScanned.setColumns(10);
		
		JButton btnBrowseBarcodeScanned = new JButton("...");
		btnBrowseBarcodeScanned.setEnabled(false);
		panel.add(btnBrowseBarcodeScanned, "cell 2 6");
		
		JButton btnReturnToDefault = new JButton("Return to defaults");
		btnReturnToDefault.setEnabled(false);
		panel.add(btnReturnToDefault, "cell 1 7,alignx right");
		
		linkToPrefs();
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}
	
	private void linkToPrefs()
	{
		new CheckBoxWrapper(chkEnableSoundSystem, PrefKey.SOUND_ENABLED, true);
	}

}
