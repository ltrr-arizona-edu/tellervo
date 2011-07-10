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
package edu.cornell.dendro.corina.prefs.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice;
import edu.cornell.dendro.corina.hardware.PlatformTestDialog;
import edu.cornell.dendro.corina.hardware.SerialDeviceSelector;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.BaudRate;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.DataBits;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.FlowControl;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.LineFeed;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.PortParity;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.StopBits;
import edu.cornell.dendro.corina.prefs.Prefs.PrefKey;
import edu.cornell.dendro.corina.prefs.wrappers.CheckBoxWrapper;
import edu.cornell.dendro.corina.prefs.wrappers.FormatWrapper;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.ArrayListModel;

@SuppressWarnings("serial")
public class HardwarePrefsPanel extends AbstractPreferencesPanel{
	private AbstractSerialMeasuringDevice device;
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

	/**
	 * Create the panel.
	 */
	public HardwarePrefsPanel()  {
		super(I18n.getText("preferences.hardware"), 
				"hardware.png", 
				"Set measuring platform and barcode scanner preferences");
		
		setLayout(new MigLayout("", "[grow]", "[][][grow]"));
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Measuring Platform", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(panel, "cell 0 0,grow");
		panel.setLayout(new MigLayout("", "[][grow][][grow]", "[][][][][33.00][][]"));
		
		lblPlatformType = new JLabel("Type:");
		panel.add(lblPlatformType, "cell 0 0,alignx trailing");	
		cboPlatformType = new JComboBox();
		panel.add(cboPlatformType, "cell 1 0 2 1,alignx left");
    	
		// Set up platform types
    	new FormatWrapper(cboPlatformType, 
    			PrefKey.SERIAL_DEVICE, 
    			App.prefs.getPref(PrefKey.SERIAL_DEVICE, "[none]"), 
    			SerialDeviceSelector.getAvailableDevicesNames());
    	cboPlatformType.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				setGuiEnabledByPlatformType();
			}
    	});
    	
		
		lblPort = new JLabel("Port:");
		panel.add(lblPort, "cell 0 1,alignx trailing");
		cboPort = new JComboBox();
		panel.add(cboPort, "cell 1 1,alignx left");
		setupCOMPort();
		
		lblDataBits = new JLabel("Data bits / Word length:");
		panel.add(lblDataBits, "cell 2 1,alignx trailing");
		cboDatabits = new JComboBox();
		cboDatabits.setModel(new DefaultComboBoxModel(new String[] {}));
		panel.add(cboDatabits, "cell 3 1,alignx left");
		new FormatWrapper(cboDatabits, 
				PrefKey.SERIAL_DATABITS, 
				App.prefs.getPref(PrefKey.SERIAL_DATABITS, "8"), 
				DataBits.allValuesAsArray());
		
		lblBaud = new JLabel("Baud:");
		panel.add(lblBaud, "cell 0 2,alignx trailing");
		cboBaud = new JComboBox();
		cboBaud.setModel(new DefaultComboBoxModel(new String[] {}));
		panel.add(cboBaud, "cell 1 2,alignx left");
    	new FormatWrapper(cboBaud, 
    			PrefKey.SERIAL_BAUD, 
    			App.prefs.getPref(PrefKey.SERIAL_BAUD, "9600"), 
    			BaudRate.allValuesAsArray());
		
		lblFlowControl = new JLabel("Handshaking / Flow control:");
		panel.add(lblFlowControl, "cell 2 2,alignx trailing");
		cboFlowControl = new JComboBox();
		cboFlowControl.setModel(new DefaultComboBoxModel(new String[] {}));
		panel.add(cboFlowControl, "cell 3 2,alignx left");
		new FormatWrapper(cboFlowControl, 
				PrefKey.SERIAL_FLOWCONTROL, 
				App.prefs.getPref(PrefKey.SERIAL_FLOWCONTROL, "None"), 
				FlowControl.allValuesAsArray());
		
		lblParity = new JLabel("Parity:");
		panel.add(lblParity, "cell 0 3,alignx trailing");
		cboParity = new JComboBox();
		cboParity.setModel(new DefaultComboBoxModel(new String[] {}));
		panel.add(cboParity, "cell 1 3,alignx left");
    	new FormatWrapper(cboParity, 
    			PrefKey.SERIAL_PARITY, 
    			App.prefs.getPref(PrefKey.SERIAL_PARITY, "None"), 
    			PortParity.allValuesAsArray());
		
		
		lblLineFeed = new JLabel("Line feed:");
		panel.add(lblLineFeed, "cell 2 3,alignx trailing");
		cboLineFeed = new JComboBox();
		panel.add(cboLineFeed, "cell 3 3,alignx left");
		new FormatWrapper(cboLineFeed, 
				PrefKey.SERIAL_LINEFEED, 
				App.prefs.getPref(PrefKey.SERIAL_LINEFEED, "CR"), 
				LineFeed.allValuesAsArray());
		
		
    	lblStopBits = new JLabel("Stop bits:");
    	panel.add(lblStopBits, "cell 0 4,alignx trailing");
    	cboStopbits = new JComboBox();
    	cboStopbits.setModel(new DefaultComboBoxModel(new String[] {}));
    	panel.add(cboStopbits, "cell 1 4,alignx left");	
    	new FormatWrapper(cboStopbits, 
    			PrefKey.SERIAL_STOPBITS, 
    			App.prefs.getPref(PrefKey.SERIAL_STOPBITS, "2"), 
    			StopBits.allValuesAsArray());
		

		panelBarcode = new JPanel();
		panelBarcode.setBorder(new TitledBorder(null, "Barcode scanner", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(panelBarcode, "cell 0 1,grow");
		panelBarcode.setLayout(new MigLayout("", "[]", "[]"));
		
		chkDisableBarcodes = new JCheckBox("Disable support for barcode scanner");
		panelBarcode.add(chkDisableBarcodes, "cell 0 0");
		new CheckBoxWrapper(chkDisableBarcodes, "corina.barcodes.disable", false );
		
		btnTestConnection = new JButton("Test connection");
    	panel.add(btnTestConnection, "cell 0 5 4 1,alignx right");
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
					device = SerialDeviceSelector.getSelectedDevice(true);	
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
				
				PlatformTestDialog testConnDialog = new PlatformTestDialog(device);
				
				testConnDialog.setVisible(true);
			}
    		
    	});
    	
    	setGuiEnabledByPlatformType();
	}

	@SuppressWarnings("unchecked")
	private void setupCOMPort()
	{
		if (AbstractSerialMeasuringDevice.hasSerialCapability()) {
	
			// first, enumerate all the ports.
			Vector<String> comportlist = AbstractSerialMeasuringDevice.enumeratePorts();
	
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
				}
			});
		}

	}

	/**
	 * Enabled/Disable form items depending on the device selected.  Also
	 * set the selected values to the default for that platform.
	 */
	private void setGuiEnabledByPlatformType()
	{
		AbstractSerialMeasuringDevice device;
		
		// Set up the measuring device
		try {
			device = SerialDeviceSelector.getSelectedDevice(false);
		} catch (Exception e) {
			cboBaud.setEnabled(false);
			cboParity.setEnabled(false);
			cboFlowControl.setEnabled(false);
			cboDatabits.setEnabled(false);
			cboStopbits.setEnabled(false);
			cboLineFeed.setEnabled(false);
			cboPort.setEnabled(false);
			btnTestConnection.setEnabled(false);
			return;
		} 
		
		cboPort.setEnabled(true);
		btnTestConnection.setEnabled(true);
		
		cboBaud.setEnabled(device.isBaudEditable());
		cboBaud.setSelectedItem(device.getBaudRate().toString());

		cboParity.setEnabled(device.isParityEditable());
		cboParity.setSelectedItem(device.getParity().toString());
		
		cboFlowControl.setEnabled(device.isFlowControlEditable());
		cboFlowControl.setSelectedItem(device.getFlowControl().toString());

		cboDatabits.setEnabled(device.isDatabitsEditable());
		cboDatabits.setSelectedItem(device.getDataBits().toString());

		cboStopbits.setEnabled(device.isStopbitsEditable());
		cboStopbits.setSelectedItem(device.getStopBits().toString());

		cboLineFeed.setEnabled(device.isLineFeedEditable());
		cboLineFeed.setSelectedItem(device.getLineFeed().toString());


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
	

	
}
