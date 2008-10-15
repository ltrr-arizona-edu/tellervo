package edu.cornell.dendro.corina.prefs;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import say.swing.JFontChooser;

import edu.cornell.dendro.corina.prefs.wrappers.CheckBoxWrapper;
import edu.cornell.dendro.corina.prefs.wrappers.ColorComboBoxWrapper;
import edu.cornell.dendro.corina.prefs.wrappers.FontButtonWrapper;
import edu.cornell.dendro.corina.prefs.wrappers.SpinnerWrapper;
import edu.cornell.dendro.corina.prefs.wrappers.TextComponentWrapper;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.Center;



public class PreferencesDialog extends Ui_PreferencesDialog {
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
		dialog.setIconImage(Builder.getImage("Preferences16.gif"));
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// steal the content pane from an instance of PreferencesDialog
		// TODO: Gross. Make this extend a JFrame, not a JDialog! :)
		PreferencesDialog tmp = new PreferencesDialog(null, false);
		dialog.setContentPane(tmp.getContentPane());
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
	
	private void populateDialog() {
		// sample editor
		// TODO: display units
		new ColorComboBoxWrapper(cboTextColor, Prefs.EDIT_FOREGROUND, Color.black);
		new ColorComboBoxWrapper(cboEditorBGColor, Prefs.EDIT_BACKGROUND, Color.white);
		new FontButtonWrapper(btnFont, Prefs.EDIT_FONT, getFont());
		new CheckBoxWrapper(jCheckBox3, Prefs.EDIT_GRIDLINES, true);
		
		// graph
		new ColorComboBoxWrapper(cboAxisCursorColor, Prefs.GRAPH_AXISCURSORCOLOR, Color.white);
		new ColorComboBoxWrapper(cboChartBGColor, Prefs.GRAPH_BACKGROUND, Color.black);
		new ColorComboBoxWrapper(cboGridColor, Prefs.GRAPH_GRIDLINES_COLOR, Color.darkGray);
		new CheckBoxWrapper(chkShowChartGrid, Prefs.GRAPH_GRIDLINES, true);
		
		// networking
		new CheckBoxWrapper(chkUseProxy, "corina.proxy.enabled", false);
		new TextComponentWrapper(txtProxyURL, "corina.proxy.http", null);
		new SpinnerWrapper(spnProxyPort, "corina.proxy.http_port", 80);
		new TextComponentWrapper(txtProxyURL1, "corina.proxy.https", null);
		new SpinnerWrapper(spnProxyPort1, "corina.proxy.https_port", 443);
		
		// proxy checkbox behavior
		setEnableProxy(chkUseProxy.isSelected());
		chkUseProxy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setEnableProxy(chkUseProxy.isSelected());
			}
		});
	}
	
	private PreferencesDialog(Frame parent, boolean modal) {
		super(parent, modal);
		
		initPrefsDialog();
	}
}