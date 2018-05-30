package org.tellervo.desktop.prefs.panels;

import java.awt.Component;
import java.awt.Insets;
import java.awt.TextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

import net.miginfocom.swing.MigLayout;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.prefs.wrappers.CheckBoxWrapper;
import org.tellervo.desktop.prefs.wrappers.EditableComboBoxWrapper;
import org.tellervo.desktop.prefs.wrappers.TextComponentWrapper;
import org.tellervo.desktop.tridasv2.LabCodeFormatter;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.ExtensionFileFilter;
import org.tellervo.desktop.util.SoundUtil;
import org.tellervo.desktop.util.SoundUtil.SystemSound;
import org.tellervo.desktop.versioning.UpdateChecker;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextPane;

public class GeneralPrefsPanel extends AbstractPreferencesPanel {

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
	private JButton btnPlayPlatformInit;
	private JButton btnPlayRingMeasured;
	private JButton btnPlayDecadeMeasured;
	private JButton btnPlayMeasurementError;
	private JButton btnPlayBarcodeScanned;
	private JPanel panelPrivacy;
	private JCheckBox chkAutoUpdate;
	private JLabel lblAutomaticallyCheckFor;
	private JButton btnCheckForUpdates;
	private JPanel panel_1;
	private JCheckBox chkUseDefaultPDFViewer;
	private JTextField txtPDFViewer;
	private JLabel lblPdfProgram;
	private JPanel panel_2;
	private JComboBox cboLabCodeStyle;
	private JLabel lblLabCodeStyle;
	private JTextPane txtLabCodeInstructions;
	
	
	public GeneralPrefsPanel(final JDialog parent) {
		super(I18n.getText("preferences.general"), 
				"home.png", 
				"Configure sounds and other miscellaneous settings within Tellervo",
				parent);
		setLayout(new MigLayout("", "[186px,grow]", "[15px][][][][grow]"));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Sound system", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(panel, "cell 0 0,grow");
		panel.setLayout(new MigLayout("", "[][34.00][158.00,grow][]", "[center][][36.00][][][][][][]"));
		
		JLabel lblEnableSoundsIn = new JLabel("Enable sounds in Tellervo:");
		panel.add(lblEnableSoundsIn, "cell 0 0,alignx right,aligny center");
		
		chkEnableSoundSystem = new JCheckBox("");
		panel.add(chkEnableSoundSystem, "cell 1 0 3 1,aligny center");
		chkEnableSoundSystem.setSelected(true);
		
		JLabel lblNewLabel = new JLabel("Use system defaults:");
		panel.add(lblNewLabel, "cell 0 1,alignx right");
		
		chkUseSystemDefaults = new JCheckBox("");
		chkUseSystemDefaults.setSelected(true);
		panel.add(chkUseSystemDefaults, "cell 1 1 2 1");
		chkUseSystemDefaults.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setGuiFromPref();
			}
			
		});
		
		// Platform init
		JLabel lblPlatformInit = new JLabel("Platform initalization:");
		panel.add(lblPlatformInit, "cell 0 3,alignx trailing");
		btnPlayPlatformInit = new JButton("");
		btnPlayPlatformInit.setIcon(Builder.getIcon("play.png", 16));
		btnPlayPlatformInit.setMargin(new Insets(1, 1, 1, 1));
		btnPlayPlatformInit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SoundUtil.playSystemSound(SystemSound.MEASURING_PLATFORM_INIT);
			}	
		});
		panel.add(btnPlayPlatformInit, "cell 1 3");
		txtPlatformInit = new JTextField();
		panel.add(txtPlatformInit, "cell 2 3,growx");
		txtPlatformInit.setColumns(10);
		btnBrowsePlatformInit = new JButton("");
		btnBrowsePlatformInit.setIcon(Builder.getIcon("open.png", 16));
		btnBrowsePlatformInit.setMargin(new Insets(1, 1, 1, 1));
		btnBrowsePlatformInit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String file = getFileChooserFilename();
				if(file!=null)	txtPlatformInit.setText(file);
			}
		});
		panel.add(btnBrowsePlatformInit, "cell 3 3");
		
		
		// Ring Measured
		JLabel lblRingMeasured = new JLabel("Ring measured:");
		panel.add(lblRingMeasured, "cell 0 4,alignx trailing");
		btnPlayRingMeasured = new JButton("");
		btnPlayRingMeasured.setMargin(new Insets(1, 1, 1, 1));
		btnPlayRingMeasured.setIcon(Builder.getIcon("play.png", 16));
		btnPlayRingMeasured.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SoundUtil.playSystemSound(SystemSound.MEASURE_RING);
			}	
		});
		panel.add(btnPlayRingMeasured, "cell 1 4");
		txtRingMeasured = new JTextField();
		panel.add(txtRingMeasured, "cell 2 4,growx");
		txtRingMeasured.setColumns(10);
		btnBrowseRingMeasured = new JButton("");
		btnBrowseRingMeasured.setIcon(Builder.getIcon("open.png", 16));
		btnBrowseRingMeasured.setMargin(new Insets(1, 1, 1, 1));
		panel.add(btnBrowseRingMeasured, "cell 3 4");
		btnBrowseRingMeasured.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String file = getFileChooserFilename();
				if(file!=null)	txtRingMeasured.setText(file);
			}
		});
		
		// Decade Measured
		JLabel lblDecadeMeasured = new JLabel("Decade measured:");
		panel.add(lblDecadeMeasured, "cell 0 5,alignx trailing");
		btnPlayDecadeMeasured = new JButton("");
		btnPlayDecadeMeasured.setMargin(new Insets(1, 1, 1, 1));
		btnPlayDecadeMeasured.setIcon(Builder.getIcon("play.png", 16));
		btnPlayDecadeMeasured.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SoundUtil.playSystemSound(SystemSound.MEASURE_DECADE);
			}	
		});
		panel.add(btnPlayDecadeMeasured, "cell 1 5");
		txtDecadeMeasured = new JTextField();
		panel.add(txtDecadeMeasured, "cell 2 5,growx");
		txtDecadeMeasured.setColumns(10);
		btnBrowseDecadeMeasured = new JButton("");
		btnBrowseDecadeMeasured.setIcon(Builder.getIcon("open.png", 16));
		btnBrowseDecadeMeasured.setMargin(new Insets(1, 1, 1, 1));
		panel.add(btnBrowseDecadeMeasured, "cell 3 5");
		btnBrowseDecadeMeasured.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String file = getFileChooserFilename();
				if(file!=null)	txtDecadeMeasured.setText(file);
			}
		});
		
		// Measuring Error
		JLabel lblMeasurementError = new JLabel("Measurement error:");
		panel.add(lblMeasurementError, "cell 0 6,alignx trailing");
		btnPlayMeasurementError = new JButton("");
		btnPlayMeasurementError.setMargin(new Insets(1, 1, 1, 1));
		btnPlayMeasurementError.setIcon(Builder.getIcon("play.png", 16));
		btnPlayMeasurementError.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SoundUtil.playSystemSound(SystemSound.MEASURE_ERROR);
			}	
		});
		panel.add(btnPlayMeasurementError, "cell 1 6");
		txtMeasurementError = new JTextField();
		panel.add(txtMeasurementError, "cell 2 6,growx");
		txtMeasurementError.setColumns(10);
		btnBrowseMeasurementError = new JButton("");
		btnBrowseMeasurementError.setIcon(Builder.getIcon("open.png", 16));
		btnBrowseMeasurementError.setMargin(new Insets(1, 1, 1, 1));
		panel.add(btnBrowseMeasurementError, "cell 3 6");
		btnBrowseMeasurementError.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String file = getFileChooserFilename();
				if(file!=null)	txtMeasurementError.setText(file);
			}
		});
		
		
		// Barcode scanned
		JLabel lblBarcodeScanned = new JLabel("Barcode scanned:");
		panel.add(lblBarcodeScanned, "cell 0 7,alignx trailing");
		btnPlayBarcodeScanned = new JButton("");
		btnPlayBarcodeScanned.setMargin(new Insets(1, 1, 1, 1));
		btnPlayBarcodeScanned.setIcon(Builder.getIcon("play.png", 16));
		btnPlayBarcodeScanned.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SoundUtil.playSystemSound(SystemSound.BARCODE_SCAN);
			}	
		});
		panel.add(btnPlayBarcodeScanned, "cell 1 7");
		txtBarcodeScanned = new JTextField();
		panel.add(txtBarcodeScanned, "cell 2 7,growx");
		txtBarcodeScanned.setColumns(10);
		btnBrowseBarcodeScanned = new JButton("");
		btnBrowseBarcodeScanned.setIcon(Builder.getIcon("open.png", 16));
		btnBrowseBarcodeScanned.setMargin(new Insets(1, 1, 1, 1));
		panel.add(btnBrowseBarcodeScanned, "cell 3 7");
		
		panelPrivacy = new JPanel();
		panelPrivacy.setBorder(new TitledBorder(null, "Privacy and Updates", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(panelPrivacy, "cell 0 1,grow");
		panelPrivacy.setLayout(new MigLayout("", "[][21px][]", "[21px][grow,fill]"));
		
		lblAutomaticallyCheckFor = new JLabel("Check for updates on startup:");
		panelPrivacy.add(lblAutomaticallyCheckFor, "cell 0 0");
		
		chkAutoUpdate = new JCheckBox("");
		panelPrivacy.add(chkAutoUpdate, "cell 1 0,alignx left,aligny top");
		
		btnCheckForUpdates = new JButton("Check now");
		
		final Component glue = this;
		btnCheckForUpdates.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				UpdateChecker.doUpdateCheck(true, glue);				
			}
			
		});
		
		panelPrivacy.add(btnCheckForUpdates, "cell 2 0");
		
		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Helper programs", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(panel_1, "cell 0 2,grow");
		panel_1.setLayout(new MigLayout("", "[][grow]", "[][]"));
		
		chkUseDefaultPDFViewer = new JCheckBox("Use default PDF program");
		chkUseDefaultPDFViewer.setSelected(true);
		
		chkUseDefaultPDFViewer.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setGuiFromPref();
			}
			
		});
		
		panel_1.add(chkUseDefaultPDFViewer, "cell 1 0");
		
		lblPdfProgram = new JLabel("PDF Viewer:");
		panel_1.add(lblPdfProgram, "cell 0 1,alignx trailing");
		
		txtPDFViewer = new JTextField();
		txtPDFViewer.setEnabled(false);
		panel_1.add(txtPDFViewer, "cell 1 1,growx");
		txtPDFViewer.setColumns(10);
		
		panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Series Labeling", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(panel_2, "cell 0 3,grow");
		panel_2.setLayout(new MigLayout("", "[][grow]", "[grow][]"));
		
		txtLabCodeInstructions = new JTextPane();
		txtLabCodeInstructions.setEditable(false);
		txtLabCodeInstructions.setText("Define how your series codes are presented by choosing from the redefined styles or by crafting your own using the following special tags:\n   %LABACRONYM% = Acronym for your lab\n   %OBJECTS% - Hierarchical list of objects delimited with /\n   %OBJECT% - Top level object\n   %BLOBJECT% - Object immediately attached to element\n   %ELEMENT% - Element code\n   %SAMPLE% - Sample code\n   %RADIUS% - Radius code\n   %SERIES% - Series code");
		panel_2.add(txtLabCodeInstructions, "cell 1 0,grow");
		
		lblLabCodeStyle = new JLabel("Lab code style:");
		panel_2.add(lblLabCodeStyle, "cell 0 1,alignx trailing");
		
		cboLabCodeStyle = new JComboBox();
		cboLabCodeStyle.setEditable(true);
		cboLabCodeStyle.setModel(new DefaultComboBoxModel(new String[] {"%OBJECTS%-%ELEMENT%-%SAMPLE%-%RADIUS%-%SERIES%", "%OBJECT%-%ELEMENT%-%SAMPLE%-%RADIUS%-%SERIES%", "%BLOBJECT%-%ELEMENT%-%SAMPLE%-%RADIUS%-%SERIES%"}));
		panel_2.add(cboLabCodeStyle, "cell 1 1,growx");
		btnBrowseBarcodeScanned.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String file = getFileChooserFilename();
				if(file!=null)	txtBarcodeScanned.setText(file);
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
			fc.setCurrentDirectory(new File(App.prefs.getPref(PrefKey.SOUND_LAST_FOLDER, null)));
		}
		
		// Show the dialog
		fc.showOpenDialog(parent);
		
		try{
			// Save the folder the file is from for next time
			App.prefs.setPref(PrefKey.SOUND_LAST_FOLDER, fc.getCurrentDirectory().getCanonicalPath());
			
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
		
		txtPDFViewer.setEnabled(!chkUseDefaultPDFViewer.isSelected());
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
		new CheckBoxWrapper(chkAutoUpdate, PrefKey.CHECK_FOR_UPDATES, true);
		new CheckBoxWrapper(chkUseDefaultPDFViewer, PrefKey.USE_DEFAULT_PDF_VIEWER, true);
		new TextComponentWrapper(txtPDFViewer, PrefKey.PDF_VIEWER_EXECUTABLE, null);
		
		
		cboLabCodeStyle.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				System.out.println("Item changed");
				App.prefs.setPref(PrefKey.LABCODE_STYLE, cboLabCodeStyle.getEditor().getItem().toString());
			}
			
		});
		
		cboLabCodeStyle.setEditable(true);
		cboLabCodeStyle.getEditor().getEditorComponent().addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("Focus lost");
				App.prefs.setPref(PrefKey.LABCODE_STYLE, cboLabCodeStyle.getEditor().getItem().toString());
			}
			
		});

		
		//new EditableComboBoxWrapper(cboLabCodeStyle, PrefKey.LABCODE_STYLE, LabCodeFormatter.getDefaultFormatter().getCodeFormat());

	}
}
