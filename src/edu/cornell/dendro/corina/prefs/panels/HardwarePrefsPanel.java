package edu.cornell.dendro.corina.prefs.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JLabel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice;
import edu.cornell.dendro.corina.hardware.SerialDeviceSelector;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.BaudRate;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.DataBits;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.FlowControl;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.LineFeed;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.StopBits;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.PortParity;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.UnitMultiplier;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import net.miginfocom.swing.MigLayout;
import javax.swing.JCheckBox;
import javax.swing.JButton;

public class HardwarePrefsPanel extends JPanel {
	private AbstractSerialMeasuringDevice device;
	private JPanel panel;
	private JPanel panel_1;
	private JCheckBox chkDisableBarcodes;
	private JLabel lblPlatformType;
	private JLabel lblNewLabel;
	private JLabel lblBaud;
	private JLabel lblParity;
	private JLabel lblFlowControl;
	private JLabel lblDataBits;
	private JLabel lblStopBits;
	private JComboBox cboPlatformType;
	private JComboBox cboPort;
	private JComboBox cboBaud;
	private JComboBox cboParity;
	private JComboBox comboBox_4;
	private JComboBox cboDatabits;
	private JComboBox cboStopbits;
	private JButton btnTestConnection;

	/**
	 * Create the panel.
	 */
	public HardwarePrefsPanel() {
		setLayout(new MigLayout("", "[grow]", "[][][grow]"));
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Measuring Platform", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(panel, "cell 0 0,grow");
		panel.setLayout(new MigLayout("", "[][grow][][grow]", "[][][][][][]"));
		
		lblPlatformType = new JLabel("Type:");
		panel.add(lblPlatformType, "cell 0 0,alignx trailing");
		
		cboPlatformType = new JComboBox();
		panel.add(cboPlatformType, "cell 1 0 2 1,alignx left");
		
		lblNewLabel = new JLabel("Port:");
		panel.add(lblNewLabel, "cell 0 1,alignx trailing");
		
		cboPort = new JComboBox();
		panel.add(cboPort, "cell 1 1,alignx left");
		
		lblFlowControl = new JLabel("Flow control:");
		panel.add(lblFlowControl, "cell 2 1,alignx trailing");
		
		comboBox_4 = new JComboBox();
		panel.add(comboBox_4, "cell 3 1,alignx left");
		
		lblBaud = new JLabel("Baud:");
		panel.add(lblBaud, "cell 0 2,alignx trailing");
		
		cboBaud = new JComboBox();
		panel.add(cboBaud, "cell 1 2,alignx left");
		
		lblDataBits = new JLabel("Data bits:");
		panel.add(lblDataBits, "cell 2 2,alignx trailing");
		
		cboDatabits = new JComboBox();
		panel.add(cboDatabits, "cell 3 2,alignx left");
		
		lblParity = new JLabel("Parity:");
		panel.add(lblParity, "cell 0 3,alignx trailing");
		
		cboParity = new JComboBox();
		panel.add(cboParity, "cell 1 3,alignx left");
		
		lblStopBits = new JLabel("Stop bits:");
		panel.add(lblStopBits, "cell 2 3,alignx trailing");
		
		cboStopbits = new JComboBox();
		panel.add(cboStopbits, "cell 3 3,alignx left");
		
		btnTestConnection = new JButton("Test connection");
		panel.add(btnTestConnection, "cell 0 4 2 1,alignx left");
		
		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Barcode scanner", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(panel_1, "cell 0 1,grow");
		panel_1.setLayout(new MigLayout("", "[]", "[]"));
		
		chkDisableBarcodes = new JCheckBox("Disable support for barcode scanner");
		panel_1.add(chkDisableBarcodes, "cell 0 0");

		setupGui();
	}

	
	private void setupGui(){
	}

	public JComboBox getCboPlatformType() {
		return cboPlatformType;
	}
	public JComboBox getCboPort() {
		return cboPort;
	}
	public JComboBox getCboBaud() {
		return cboBaud;
	}
	public JComboBox getCboParity() {
		return cboParity;
	}

	public JComboBox getCboDatabits() {
		return cboDatabits;
	}
	public JComboBox getCboStopbits() {
		return cboStopbits;
	}

	
}
