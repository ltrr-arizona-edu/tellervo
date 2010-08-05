package edu.cornell.dendro.corina.prefs;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.hardware.LegacySerialSampleIO;
import edu.cornell.dendro.corina.prefs.components.UIDefaultsComponent;
import edu.cornell.dendro.corina.prefs.wrappers.CheckBoxWrapper;
import edu.cornell.dendro.corina.prefs.wrappers.ColorComboBoxWrapper;
import edu.cornell.dendro.corina.prefs.wrappers.FontButtonWrapper;
import edu.cornell.dendro.corina.prefs.wrappers.FormatWrapper;
import edu.cornell.dendro.corina.prefs.wrappers.RadioButtonWrapper;
import edu.cornell.dendro.corina.prefs.wrappers.SpinnerWrapper;
import edu.cornell.dendro.corina.prefs.wrappers.TextComponentWrapper;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.ArrayListModel;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;



public class PreferencesDialog extends Ui_PreferencesPanel {
	// it's really important to only show one prefs dialog! :)
	private static JFrame dialog;

	public synchronized static void showPreferences() {

		// does it already exist? just bring it to the front
		if(dialog != null) {
			dialog.setVisible(true);
			dialog.setExtendedState(JFrame.NORMAL);
			dialog.toFront();
			
			return;
		}
		
		// construct a new dialog!
		dialog = new JFrame("Preferences");
		dialog.setIconImage(Builder.getApplicationIcon());
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// steal the content pane from an instance of PreferencesDialog
		dialog.setContentPane(new PreferencesDialog());
		dialog.pack();
		
		Center.center(dialog);
		dialog.setVisible(true);
		
		// on close, set our internal static thingy to null
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				synchronized(dialog) {
					dialog = null;
				}
			}
		});
	}
	
	/// begin actual, non-static code!
	private void initPrefsDialog() {
		final PreferencesDialog glue = this;
		
		// populate everything
		populateDialog();		
	}
	
	private void setEnableProxy(boolean isEnabled) {
		txtProxyURL.setEnabled(isEnabled);
		txtProxyURL1.setEnabled(isEnabled);
		spnProxyPort.setEnabled(isEnabled);
		spnProxyPort1.setEnabled(isEnabled);
		lblProxyServer.setEnabled(isEnabled);
		lblProxyServer1.setEnabled(isEnabled);
		lblProxyPort.setEnabled(isEnabled);
		lblProxyPort1.setEnabled(isEnabled);
	}
	
	private void setupCOMPort()
	{
		if (LegacySerialSampleIO.hasSerialCapability()) {

			boolean addedPort = false;
	
			// first, enumerate all the ports.
			Vector comportlist = LegacySerialSampleIO.enumeratePorts();
	
			// do we have a COM port selected that's not in the list? (ugh!)
			String curport = App.prefs.getPref("corina.serialsampleio.port");
			if (curport != null && !comportlist.contains(curport)) {
				comportlist.add(curport);
				addedPort = true;
			} else if (curport == null) {
				curport = "<choose a serial port>";
				comportlist.add(curport);
			}
	
			// make the combobox, and select the current port...
			//final JComboBox comports = new JComboBox(comportlist);
			
			ArrayListModel<String> portmodel = new ArrayListModel<String>();
			
			portmodel.addAll(comportlist);
			cboPort.setModel(portmodel);
			
			if (curport != null)
				cboPort.setSelectedItem(curport);
	
				
			
			this.cboPort.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					App.prefs.setPref("corina.serialsampleio.port",
							(String) cboPort.getSelectedItem());
				}
			});
		}

	}
	
	private void populateDialog() {
		// general
		// TODO: implement cancel?
		btnOk.setVisible(false); // no 'cancel' - prefs are automatically applied!
		btnCancel.setText("Ok");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		
		// sample editor
		// TODO: display units
		new ColorComboBoxWrapper(cboTextColor, Prefs.EDIT_FOREGROUND, Color.black);
		new ColorComboBoxWrapper(cboEditorBGColor, Prefs.EDIT_BACKGROUND, Color.white);
		new FontButtonWrapper(btnFont, Prefs.EDIT_FONT, getFont());
		new CheckBoxWrapper(chkShowEditorGrid, Prefs.EDIT_GRIDLINES, true);
		scrollPaneUIDefaults.setViewportView(new UIDefaultsComponent());
		
		// graph
		new ColorComboBoxWrapper(cboAxisCursorColor, Prefs.GRAPH_AXISCURSORCOLOR, Color.white);
		new ColorComboBoxWrapper(cboChartBGColor, Prefs.GRAPH_BACKGROUND, Color.black);
		new ColorComboBoxWrapper(cboGridColor, Prefs.GRAPH_GRIDLINES_COLOR, Color.darkGray);
		new CheckBoxWrapper(chkShowChartGrid, Prefs.GRAPH_GRIDLINES, true);
		
		// networking - proxy
		btnDefaultProxy.setActionCommand("default");
		btnManualProxy.setActionCommand("manual");
		btnNoProxy.setActionCommand("direct");
		new TextComponentWrapper(txtProxyURL, "corina.proxy.http", null);
		new SpinnerWrapper(spnProxyPort, "corina.proxy.http_port", 80);
		new TextComponentWrapper(txtProxyURL1, "corina.proxy.https", null);
		new SpinnerWrapper(spnProxyPort1, "corina.proxy.https_port", 443);
		new RadioButtonWrapper(new JRadioButton[] { btnDefaultProxy, btnManualProxy, btnNoProxy }, 
				"corina.proxy.type", "default");
				
		// manual proxy button behavior
		setEnableProxy(btnManualProxy.isSelected());
		btnManualProxy.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				setEnableProxy(btnManualProxy.isSelected());
			}
		});
		
		// networking - server & smtp
		new TextComponentWrapper(txtWSURL, "corina.webservice.url", null);
		new TextComponentWrapper(txtSMTPServer, "corina.mail.mailhost", null);
		
		// Measuring platform stuff
		setupCOMPort();
		
		// force dictionary reload
		btnReloadDictionary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CorinaResourceAccessDialog dlg = new CorinaResourceAccessDialog(App.dictionary);
				
				App.dictionary.query();
				dlg.setVisible(true);
			}
		});
		
		// statistics
		// TODO: Cofecha (does anyone use this?)
		new FormatWrapper(cboTScore, "corina.cross.tscore.format", "0.00");
		new FormatWrapper(cboRValue, "corina.cross.rvalue.format", "0.00");
		new FormatWrapper(cboTrend, "corina.cross.trend.format", "0.0%");
		new FormatWrapper(cboDScore, "corina.cross.dscore.format", "0.00");
		new FormatWrapper(cboWJ, "corina.cross.weiserjahre.format", "0.0%");
		new SpinnerWrapper(spnMinOverlap, "corina.cross.overlap", 15);
		new SpinnerWrapper(spnMinOverlapDScore, "corina.cross.d-overlap", 100);
		new CheckBoxWrapper(chkHighlightSig, Prefs.GRID_HIGHLIGHT, true);
		new ColorComboBoxWrapper(cboHighlightColor, Prefs.GRID_HIGHLIGHTCOLOR, Color.green);
	}
	
	private PreferencesDialog() {
		super();
		
		initPrefsDialog();
	}
}
