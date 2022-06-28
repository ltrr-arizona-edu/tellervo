/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.prefs.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import java.awt.FlowLayout;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.hardware.AbstractHIDMeasuringDevice;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice;
import org.tellervo.desktop.hardware.AbstractSerialMeasuringDevice;
import org.tellervo.desktop.hardware.PlatformTestPanel;
import org.tellervo.desktop.hardware.MeasuringDeviceSelector;
import org.tellervo.desktop.hardware.UnsupportedPortParameterException;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice.BaudRate;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice.DataBits;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice.DeviceProtocol;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice.FlowControl;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice.LineFeed;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice.PortParity;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice.StopBits;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.prefs.wrappers.CheckBoxWrapper;
import org.tellervo.desktop.prefs.wrappers.DoubleSpinnerWrapper;
import org.tellervo.desktop.prefs.wrappers.FormatWrapper;
import org.tellervo.desktop.prefs.wrappers.SpinnerWrapper;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.ArrayListModel;

import javax.swing.UIManager;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import java.awt.Font;
import java.awt.BorderLayout;

import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class HardwarePrefsPanel extends AbstractPreferencesPanel{
	private AbstractMeasuringDevice device;
	private JPanel panel;
	private JPanel panelBarcode;
	private JCheckBox chkDisableBarcodes;
	private JLabel lblPlatformType;
	private JLabel lblPort;
	private JLabel lblBaud;
	private JLabel lblParity;
	private JLabel lblFlowControl;
	private JLabel lblDataBits;
	private JLabel lblStopBits;
	private JComboBox cboPlatformType;
	private JComboBox cboPort;
	private JComboBox cboBaud;
	private JComboBox cboParity;
	private JComboBox cboFlowControl;
	private JComboBox cboDatabits;
	private JComboBox cboStopbits;
	private JButton btnTestConnection;
	private JLabel lblLineFeed;
	private JComboBox cboLineFeed;
	private JLabel lblMeasureCumulatively;
	private JCheckBox chkMeasureCumulatively;
	private JButton btnDefaults;
	private JPanel panel_1;
	private Boolean okToUseDefaults = false;
	private JCheckBox chkReverseMeasuring;
	private JLabel lblReverseMeasuring;
	private JSpinner spnMultiply;
	private JLabel lblMultiply;
	private JPanel panelWarn;
	private JPanel panel_3;
	private JLabel label;
	private JTextArea txtWarning;
	private JLabel lblTimeout;
	private JSpinner spnTimeout;
	private JLabel lblSecs;
	private JPanel panel_2;
	private JCheckBox chkDisableTimeout;

	
	
	/**
	 * Create the panel.
	 */
	public HardwarePrefsPanel(final JDialog parent)  {
		super(I18n.getText("preferences.hardware"), 
				"hardware.png", 
				"Set measuring platform and barcode scanner preferences",
				parent);
		
		setLayout(new MigLayout("", "[grow,fill]", "[][][grow]"));
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Measuring Platform", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(panel, "cell 0 0,grow, hidemode 2");
		panel.setLayout(new MigLayout("", "[75.00][grow][120.00][grow]", "[grow][][][][][][][][]"));
		
		panelWarn = new JPanel();
		panelWarn.setBorder(new LineBorder(new Color(221,221,225)));
		panelWarn.setBackground(new Color(251,251,200));
		panel.add(panelWarn, "cell 0 0 4 1,grow, hidemode 2");
		panelWarn.setLayout(new MigLayout("", "[49.00][grow]", "[29.00,grow]"));
		
		panel_3 = new JPanel();
		panel_3.setBackground(new Color(251,251,200));
		panelWarn.add(panel_3, "cell 0 0,grow");
		
		label = new JLabel();
		label.setIcon(Builder.getIcon("warning.png", 48));
		panel_3.add(label);
		
		txtWarning = new JTextArea();
		txtWarning.setWrapStyleWord(true);
		txtWarning.setText("The native libraries required by Tellervo to interface with the serial port cannot be found.  Please check your installation and contact the Tellervo developers for more information.");
		txtWarning.setLineWrap(true);
		txtWarning.setFont(new Font("Dialog", Font.PLAIN, 12));
		txtWarning.setEditable(false);
		txtWarning.setBorder(new EmptyBorder(5, 5, 5, 5));
		txtWarning.setBackground((Color) null);
		panelWarn.add(txtWarning, "cell 1 0,growx,wmin 10");
		
		lblTimeout = new JLabel("Timeout:");
		panel.add(lblTimeout, "cell 0 1,alignx right");
		
		panel_2 = new JPanel();
		panel.add(panel_2, "cell 1 1 3 1,grow");
		panel_2.setLayout(new MigLayout("fill, insets 0", "[143.00][]", "[]"));
		
		spnTimeout = new JSpinner();
		panel_2.add(spnTimeout, "flowx,cell 0 0");
		spnTimeout.setModel(new SpinnerNumberModel(300, 10, 9999, 1));
		
		new SpinnerWrapper(spnTimeout, 
        		PrefKey.SERIAL_PORT_TIMEOUT_LENGTH,
        		App.prefs.getIntPref(PrefKey.SERIAL_PORT_TIMEOUT_LENGTH, 300));
		
		lblSecs = new JLabel("secs");
		panel_2.add(lblSecs, "cell 0 0");
		
		chkDisableTimeout = new JCheckBox("Disable");
		new CheckBoxWrapper(chkDisableTimeout, PrefKey.SERIAL_PORT_TIMEOUT_DISABLE, false );
		
		chkDisableTimeout.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				setTimeoutGUI();
				
			}
			
		});
		
				panel_2.add(chkDisableTimeout, "cell 0 0");
		
		lblPlatformType = new JLabel("Type:");
		panel.add(lblPlatformType, "cell 0 2,alignx trailing");	
		
		panel_1 = new JPanel();
		panel.add(panel_1, "cell 1 2 3 1,grow");
		panel_1.setLayout(new MigLayout("fill,  insets 0", "[32px][32.00px,grow][147px,fill]", "[25px]"));
		panel_1.setOpaque(false);
		cboPlatformType = new JComboBox();
		panel_1.add(cboPlatformType, "cell 0 0,alignx left,aligny top");
		
		// Set up platform types
		new FormatWrapper(cboPlatformType, 
				PrefKey.SERIAL_DEVICE, 
				App.prefs.getPref(PrefKey.SERIAL_DEVICE, "[none]"), 
				MeasuringDeviceSelector.getAvailableDevicesNames());
		
    	cboPlatformType.addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent arg0) {
					setGuiEnabledByPlatformType(okToUseDefaults);
					btnTestConnection.setEnabled(isReadyToTestConnection());
				}
    	});
    	
    	btnDefaults = new JButton("Default port settings");
    	panel_1.add(btnDefaults, "cell 2 0,alignx right,aligny top");
    	btnDefaults.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setGuiEnabledByPlatformType(true);
			}
    	});
		
		lblPort = new JLabel("Port:");
		panel.add(lblPort, "cell 0 3,alignx trailing");
		cboPort = new JComboBox();
		panel.add(cboPort, "cell 1 3,alignx left");
		
		if (AbstractSerialMeasuringDevice.hasSerialCapability()) 
		{
			setupCOMPort();
		}

		
		
		lblDataBits = new JLabel("Data bits / Word length:");
		panel.add(lblDataBits, "cell 2 3,alignx trailing");
		cboDatabits = new JComboBox();
		cboDatabits.setModel(new DefaultComboBoxModel(new String[] {}));
		panel.add(cboDatabits, "cell 3 3,alignx left");
		
		new FormatWrapper(cboDatabits, 
				PrefKey.SERIAL_DATABITS, 
				App.prefs.getPref(PrefKey.SERIAL_DATABITS, "8"), 
				DataBits.allValuesAsArray());
		
		lblBaud = new JLabel("Baud:");
		panel.add(lblBaud, "cell 0 4,alignx trailing");
		cboBaud = new JComboBox();
		cboBaud.setModel(new DefaultComboBoxModel(new String[] {}));
		panel.add(cboBaud, "cell 1 4,alignx left");
		
		new FormatWrapper(cboBaud, 
    			PrefKey.SERIAL_BAUD, 
    			App.prefs.getPref(PrefKey.SERIAL_BAUD, "9600"), 
    			BaudRate.allValuesAsArray());

		
		lblFlowControl = new JLabel("Handshaking / Flow control:");
		panel.add(lblFlowControl, "cell 2 4,alignx trailing");
		cboFlowControl = new JComboBox();
		cboFlowControl.setModel(new DefaultComboBoxModel(new String[] {}));
		panel.add(cboFlowControl, "cell 3 4,alignx left");
		
		new FormatWrapper(cboFlowControl, 
				PrefKey.SERIAL_FLOWCONTROL, 
				App.prefs.getPref(PrefKey.SERIAL_FLOWCONTROL, "None"), 
				FlowControl.allValuesAsArray());
		
		lblParity = new JLabel("Parity:");
		panel.add(lblParity, "cell 0 5,alignx trailing");
		cboParity = new JComboBox();
		cboParity.setModel(new DefaultComboBoxModel(new String[] {}));
		panel.add(cboParity, "cell 1 5,alignx left");
		
    	new FormatWrapper(cboParity, 
    			PrefKey.SERIAL_PARITY, 
    			App.prefs.getPref(PrefKey.SERIAL_PARITY, "None"), 
    			PortParity.allValuesAsArray());
    	
    	lblMultiply = new JLabel("Correction factor:");
    	panel.add(lblMultiply, "cell 2 5,alignx right");
    	
    	
        double min = -1000.00;  
        double value = 1.00;  
        double max = 1000.00;  
        double stepSize = 0.01;  
        SpinnerNumberModel model = new SpinnerNumberModel(value, min, max, stepSize);  
        spnMultiply = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 1000.0, 1.0));  
        JSpinner.NumberEditor editor = (JSpinner.NumberEditor)spnMultiply.getEditor();  
        DecimalFormat format = editor.getFormat();  
        format.setMinimumFractionDigits(2);  
        format.setMaximumFractionDigits(2);
        editor.getTextField().setHorizontalAlignment(SwingConstants.CENTER);  
        //Dimension d = spnMultiply.getPreferredSize();  
        //d.width = 85;  
    	
    	
        new DoubleSpinnerWrapper(spnMultiply, 
        		PrefKey.SERIAL_MULTIPLIER,
        		App.prefs.getDoublePref(PrefKey.SERIAL_MULTIPLIER, 1.00));
        
        
    	panel.add(spnMultiply, "cell 3 5,alignx left");
		
    	lblStopBits = new JLabel("Stop bits:");
    	panel.add(lblStopBits, "cell 0 6,alignx trailing");
    	cboStopbits = new JComboBox();
    	cboStopbits.setModel(new DefaultComboBoxModel(new String[] {}));
    	panel.add(cboStopbits, "cell 1 6,alignx left");	
    	
    	new FormatWrapper(cboStopbits, 
    			PrefKey.SERIAL_STOPBITS, 
    			App.prefs.getPref(PrefKey.SERIAL_STOPBITS, "2"), 
    			StopBits.allValuesAsArray());
    	
		panelBarcode = new JPanel();
		panelBarcode.setBorder(new TitledBorder(null, "Barcode scanner", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(panelBarcode, "cell 0 1,grow");
		panelBarcode.setLayout(new MigLayout("", "[]", "[]"));
		
		chkDisableBarcodes = new JCheckBox();
		
		if(App.prefs.getBooleanPref(PrefKey.WEBSERVICE_DISABLED, false))
		{
			chkDisableBarcodes.setText("Disable support for barcode scanner (database connection required)");
			chkDisableBarcodes.setEnabled(false);
		}
		else
		{
			chkDisableBarcodes.setText("Disable support for barcode scanner");
			chkDisableBarcodes.setEnabled(true);
		}
		
		panelBarcode.add(chkDisableBarcodes, "cell 0 0");
		new CheckBoxWrapper(chkDisableBarcodes, PrefKey.BARCODES_DISABLED, false );
		
		lblMeasureCumulatively = new JLabel("Measure cumulatively:");
		panel.add(lblMeasureCumulatively, "cell 2 6,alignx trailing");
		
		chkMeasureCumulatively = new JCheckBox("");
		panel.add(chkMeasureCumulatively, "cell 3 6");
		new CheckBoxWrapper(chkMeasureCumulatively, PrefKey.SERIAL_MEASURE_CUMULATIVELY, false);
		
		lblLineFeed = new JLabel("Line feed:");
		panel.add(lblLineFeed, "cell 0 7,alignx trailing");
		cboLineFeed = new JComboBox();
		panel.add(cboLineFeed, "cell 1 7,alignx left");
		
		new FormatWrapper(cboLineFeed, 
				PrefKey.SERIAL_LINEFEED, 
				App.prefs.getPref(PrefKey.SERIAL_LINEFEED, "CR"), 
				LineFeed.allValuesAsArray());
		
		lblReverseMeasuring = new JLabel("Reverse measuring:");
		panel.add(lblReverseMeasuring, "cell 2 7,alignx right");
		
		chkReverseMeasuring = new JCheckBox("");
		panel.add(chkReverseMeasuring, "cell 3 7");
		new CheckBoxWrapper(chkReverseMeasuring, PrefKey.SERIAL_MEASURE_IN_REVERSE, false);
		
		
		btnTestConnection = new JButton("Test connection");
		panel.add(btnTestConnection, "cell 2 8 2 1,alignx right");
		btnTestConnection.setEnabled(false);
		btnTestConnection.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
		    	// Make sure the device is closed
		    	if(device!=null)
		    	{
		    		device.close();
		    	}
				
				// Set up the measuring device
				try{
					device = MeasuringDeviceSelector.getSelectedDevice(true);	
				} catch (IOException e)
				{
					Alert.error(I18n.getText("error"), 
							e.getLocalizedMessage());
					return;
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				try {
					device.setPortParamsFromPrefs();
					PlatformTestPanel.showDialog(device, null, parent);
				} catch (UnsupportedPortParameterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
		});
    	
    	setGuiEnabledByPlatformType(false);
    	okToUseDefaults = true;
    	
	}

	
	public Boolean isReadyToTestConnection()
	{
		if(device==null) return false;
		
		if(device.getDeviceProtocol().equals(DeviceProtocol.SERIAL))
		{
	
			if(cboPort.getModel().getSize()==0) return false;
			if(cboPort.getSelectedItem()==null) return false;
			if(cboPort.getSelectedItem().toString().equals("<choose a serial port>")) return false;
			
			if(cboPlatformType.getSelectedItem()==null) return false;
			if(cboPlatformType.getSelectedItem().toString().equals("[none]")) return false;
			
			for(String platformname : MeasuringDeviceSelector.getAvailableDevicesNames())
			{
				if(platformname.equals(cboPlatformType.getSelectedItem().toString())) return true;
			}
		}
		else if (device.getDeviceProtocol().equals(DeviceProtocol.HID_USB))
		{
			if(((AbstractHIDMeasuringDevice)device).isDevicePresent())
			{
				return true;
			}
		}

		return false;
	}
	
	public void setBackgroundColor(Color col)
	{
		this.setBackground(col);
		this.panel.setBackground(col);
		this.panelBarcode.setBackground(col);
	}
	
	public void setBarcodePanelVisible(Boolean b)
	{
		this.panelBarcode.setVisible(b);
	}
	
	@SuppressWarnings("unchecked")
	private void setupCOMPort()
	{
		if (AbstractSerialMeasuringDevice.hasSerialCapability()) {
	
			// first, enumerate all the ports.
			Vector<String> comportlist = AbstractSerialMeasuringDevice.enumerateSerialPorts();
	
			// do we have a COM port selected that's not in the list? (ugh!)
			String curport = App.prefs.getPref(PrefKey.SERIAL_PORT, null);
			if (curport != null && !comportlist.contains(curport)) {
				comportlist.add(curport);
			} else if (curport == null) {
				curport = "<choose a serial port>";
				comportlist.add(curport);
			}
				
			ArrayListModel<String> portmodel = new ArrayListModel<String>();
			
			portmodel.addAll(comportlist);
			cboPort.setModel(portmodel);
			
			if (curport != null)
				cboPort.setSelectedItem(curport);

			cboPort.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					App.prefs.setPref(PrefKey.SERIAL_PORT,
							(String) cboPort.getSelectedItem());
					btnTestConnection.setEnabled(isReadyToTestConnection());
				}
			});
			
			panelWarn.setVisible(false);

		}
		else
		{
			panelWarn.setVisible(true);
			cboBaud.setEnabled(false);
			cboParity.setEnabled(false);
			cboFlowControl.setEnabled(false);
			cboDatabits.setEnabled(false);
			cboStopbits.setEnabled(false);
			cboLineFeed.setEnabled(false);
			cboPort.setEnabled(false);
			btnTestConnection.setEnabled(false);
			chkReverseMeasuring.setEnabled(false);
			spnMultiply.setEnabled(false);
			cboPlatformType.setEnabled(false);
			btnDefaults.setEnabled(false);
			chkReverseMeasuring.setEnabled(false);
		}

	}

	/**
	 * Enabled/Disable form items depending on the device selected.  Also
	 * set the selected values to the default for that platform.
	 */
	private void setGuiEnabledByPlatformType(Boolean useDefaults)
	{
						
		if (!AbstractSerialMeasuringDevice.hasSerialCapability())
		{
			lblPlatformType.setEnabled(false);
			cboPlatformType.setEnabled(false);
			lblPort.setEnabled(false);
			cboPort.setEnabled(false);
			lblBaud.setEnabled(false);
			cboBaud.setEnabled(false);
			lblParity.setEnabled(false);
			cboParity.setEnabled(false);
			lblStopBits.setEnabled(false);
			cboStopbits.setEnabled(false);
			lblLineFeed.setEnabled(false);
			cboLineFeed.setEnabled(false);
			
			lblDataBits.setEnabled(false);
			cboDatabits.setEnabled(false);
			lblFlowControl.setEnabled(false);
			cboFlowControl.setEnabled(false);
			
			lblMultiply.setEnabled(false);
			spnMultiply.setEnabled(false);
			lblMeasureCumulatively.setEnabled(false);
			chkMeasureCumulatively.setEnabled(false);
			lblReverseMeasuring.setEnabled(false);
			chkReverseMeasuring.setEnabled(false);
			
			btnTestConnection.setEnabled(false);
			btnDefaults.setEnabled(false);
			panelWarn.setVisible(true);
			

			return;
		}
		
		panelWarn.setVisible(false);
		
		// Set up the measuring device
		try {
			device = MeasuringDeviceSelector.getSelectedDevice(false);
			

		} catch (Exception e) {
			cboPort.setEnabled(false);
			cboBaud.setEnabled(false);
			cboParity.setEnabled(false);
			cboStopbits.setEnabled(false);
			cboLineFeed.setEnabled(false);
			
			cboDatabits.setEnabled(false);
			cboFlowControl.setEnabled(false);
			spnMultiply.setEnabled(false);
			chkMeasureCumulatively.setEnabled(false);
			chkReverseMeasuring.setEnabled(false);
			
			btnTestConnection.setEnabled(false);
			return;
		} 
		
		// Set GUI depending on protocol type
		if(device.getDeviceProtocol().equals(DeviceProtocol.SERIAL))
		{
			cboPort.setEnabled(true);
			btnDefaults.setEnabled(true);
		}
		else if(device.getDeviceProtocol().equals(DeviceProtocol.HID_USB))
		{
			cboPort.setEnabled(false);
			btnDefaults.setEnabled(false);
		}
		
		btnTestConnection.setEnabled(true);
		
		cboBaud.setEnabled(device.isBaudEditable());
		cboParity.setEnabled(device.isParityEditable());
		cboFlowControl.setEnabled(device.isFlowControlEditable());
		cboDatabits.setEnabled(device.isDatabitsEditable());
		cboStopbits.setEnabled(device.isStopbitsEditable());
		cboLineFeed.setEnabled(device.isLineFeedEditable());
		chkMeasureCumulatively.setEnabled(device.isMeasureCumulativelyConfigurable());
		chkReverseMeasuring.setEnabled(device.isReverseMeasureCapable());
		spnMultiply.setEnabled(device.isCorrectionFactorEditable());	
				
		if(useDefaults)
		{
			
			cboBaud.setSelectedItem(device.getBaudRate().toString());
			cboParity.setSelectedItem(device.getParity().toString());
			cboFlowControl.setSelectedItem(device.getFlowControl().toString());
			cboDatabits.setSelectedItem(device.getDataBits().toString());
			cboStopbits.setSelectedItem(device.getStopBits().toString());
			cboLineFeed.setSelectedItem(device.getLineFeed().toString());
			chkMeasureCumulatively.setSelected(device.getMeasureCumulatively());
			chkReverseMeasuring.setSelected(device.getReverseMeasuring());
			spnMultiply.setValue(device.getCorrectionMultipier());
			
		}
		else
		{
			cboBaud.setSelectedItem(App.prefs.getPref(PrefKey.SERIAL_BAUD, 
					device.getBaudRate().toString()));
			
			
			
		}
		
		setTimeoutGUI();
		
		

	}
	
	private void setTimeoutGUI()
	{
		this.spnTimeout.setEnabled(!chkDisableTimeout.isSelected());
	}
	
	public void setTestVisible(Boolean b)
	{
		this.btnTestConnection.setVisible(b);
	}
	
	public JComboBox getCboPlatformType() {
		return cboPlatformType;
	}

	public JComboBox getCboPort() {
		return cboPort;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}
	

	
	public JSpinner getSpnTimeout() {
		return spnTimeout;
	}

}
