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

public class HardwarePrefsPanel extends JPanel {
	private JComboBox cboPlatformType;
	private JComboBox cboPort;
	private JComboBox cboBaud;
	private JComboBox cboParity;
	private JComboBox cboUnits;
	private JComboBox cboDatabits;
	private JComboBox cboStopbits;
	private JComboBox cboLineFeed;
	private JComboBox cboFlowControl;
	private AbstractSerialMeasuringDevice device;
	private JLabel lblFlowControl;

	/**
	 * Create the panel.
	 */
	public HardwarePrefsPanel() {

		setupGui();

		cboPlatformType.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				showHidePortOptions();

				
			}
		});
	}

	
	private void setupGui(){
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("left:54px"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(76dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(48dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblPlatformType = new JLabel();
		lblPlatformType.setText("Type:");
		add(lblPlatformType, "1, 2, left, center");
		
		cboPlatformType = new JComboBox();
		cboPlatformType.setModel(new DefaultComboBoxModel(SerialDeviceSelector.getAvailableDevicesNames()));
		add(cboPlatformType, "3, 2, 5, 1, left, default");
		
		JLabel lblPort = new JLabel();
		lblPort.setText("Port:");
		add(lblPort, "1, 4, left, default");
		
		cboPort = new JComboBox();
		cboPort.setModel(new DefaultComboBoxModel(new String[] {"COM1", "COM2"}));
		add(cboPort, "3, 4, left, center");
		
		lblFlowControl = new JLabel("Flow control:");
		add(lblFlowControl, "5, 4, left, default");
		
		cboFlowControl = new JComboBox();
		cboFlowControl.setEnabled(false);
		cboFlowControl.setModel(new DefaultComboBoxModel(FlowControl.values()));
		add(cboFlowControl, "7, 4, fill, default");
		
		JLabel label_2 = new JLabel();
		label_2.setText("Baud:");
		add(label_2, "1, 6, left, default");
		
		cboBaud = new JComboBox();
		cboBaud.setModel(new DefaultComboBoxModel(BaudRate.values()));
		cboBaud.setEnabled(false);
		add(cboBaud, "3, 6, left, default");
		
		JLabel label_5 = new JLabel("Databits:");
		add(label_5, "5, 6, left, default");
		
		cboDatabits = new JComboBox();
		cboDatabits.setModel(new DefaultComboBoxModel(DataBits.values()));
		cboDatabits.setEnabled(false);
		add(cboDatabits, "7, 6, left, default");
		
		JLabel label_3 = new JLabel("Parity:");
		add(label_3, "1, 8, left, default");
		
		cboParity = new JComboBox();
		cboParity.setModel(new DefaultComboBoxModel(PortParity.values()));
		cboParity.setEnabled(false);
		add(cboParity, "3, 8, left, default");
		
		JLabel label_6 = new JLabel("Stopbits:");
		add(label_6, "5, 8, left, default");
		
		cboStopbits = new JComboBox();
		cboStopbits.setModel(new DefaultComboBoxModel(StopBits.values()));
		cboStopbits.setEnabled(false);
		add(cboStopbits, "7, 8, left, default");
		
		JLabel lblFactor = new JLabel("Factor:");
		add(lblFactor, "1, 10, left, default");
		
		cboUnits = new JComboBox();
		cboUnits.setModel(new DefaultComboBoxModel(UnitMultiplier.values()));
		cboUnits.setEnabled(false);
		add(cboUnits, "3, 10, left, default");
		
		JLabel lblLineFeed = new JLabel("Line feed:");
		add(lblLineFeed, "5, 10, left, default");
		
		cboLineFeed = new JComboBox();
		cboLineFeed.setModel(new DefaultComboBoxModel(LineFeed.values()));
		cboLineFeed.setEnabled(false);
		add(cboLineFeed, "7, 10, left, default");
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
	public JComboBox getCboUnits() {
		return cboUnits;
	}
	public JComboBox getCboDatabits() {
		return cboDatabits;
	}
	public JComboBox getCboStopbits() {
		return cboStopbits;
	}
	public JComboBox getCboLineFeed() {
		return cboLineFeed;
	}
	
	
    public void showHidePortOptions()
    {
    	device = null;
		try {
			device = SerialDeviceSelector.getSelectedDevice(false);	
		}
		catch (Exception ioe) {	} 
    
    	if(device!=null)
    	{
    		cboBaud.setEnabled(device.isBaudEditable());
    		cboParity.setEnabled(device.isParityEditable());
    		cboUnits.setEnabled(device.isUnitsEditable());
    		cboDatabits.setEnabled(device.isDatabitsEditable());
    		cboStopbits.setEnabled(device.isStopbitsEditable());
    		cboLineFeed.setEnabled(device.isLineFeedEditable());
    		cboFlowControl.setEnabled(device.isFlowControlEditable());
    		  		
    		cboBaud.setSelectedItem(device.getBaudRate());
    		cboParity.setSelectedItem(device.getParity());
    		cboUnits.setSelectedItem(device.getUnitMultiplier());
    		cboDatabits.setSelectedItem(device.getDataBits());
    		cboStopbits.setSelectedItem(device.getStopBits());
    		cboLineFeed.setSelectedItem(device.getLineFeed());
    		cboFlowControl.setSelectedItem(device.getFlowControl());
    		
    		return;
    	}
    	else
    	{
    		cboBaud.setEnabled(false);
    		cboParity.setEnabled(false);
    		cboUnits.setEnabled(false);
    		cboDatabits.setEnabled(false);
    		cboStopbits.setEnabled(false);
    		cboLineFeed.setEnabled(false);
    		cboFlowControl.setEnabled(false);
    		
    	}
    }
	
}
