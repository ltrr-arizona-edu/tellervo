package edu.cornell.dendro.corina.manip;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice;
import edu.cornell.dendro.corina.hardware.MeasurementReceiver;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.hardware.SerialDeviceSelector;
import javax.swing.BoxLayout;

import org.tridas.schema.NormalTridasUnit;


public abstract class MeasurePanel extends JPanel implements MeasurementReceiver {

	private static final long serialVersionUID = 1L;
	
	/* audioclips to play... */
	protected AudioClip measure_one;
	protected AudioClip measure_dec;
	protected AudioClip measure_error;
	
	protected JButton btnReset;
	protected JButton btnRecord;
	protected JButton btnQuit;
	protected JLabel txtCurrentValue;
	protected JLabel lastMeasurement;
	protected AbstractSerialMeasuringDevice dev;
	private JPanel panel;
	
	public MeasurePanel(final AbstractSerialMeasuringDevice device) {
				
		dev = device;

		
		setLayout(new MigLayout("", "[][][grow][grow][][][][644.00px,grow][]", "[][32px][][][16.00,grow][]"));
		
		AudioClip measInit;
		try {
			measure_one = Applet.newAudioClip(getClass().getClassLoader().getResource("edu/cornell/dendro/corina_resources/Sounds/meas1.wav"));
			measure_dec = Applet.newAudioClip(getClass().getClassLoader().getResource("edu/cornell/dendro/corina_resources/Sounds/measdec.wav"));
			measure_error = Applet.newAudioClip(getClass().getClassLoader().getResource("edu/cornell/dendro/corina_resources/Sounds/measerr.wav"));
			
			// play this to indicate measuring is on...
			measInit = Applet.newAudioClip(getClass().getClassLoader().getResource("edu/cornell/dendro/corina_resources/Sounds/measinit.wav"));
			if(measInit != null)
				measInit.play();
			 	System.out.println("Sound played");
		} catch (Exception ae) { 
			System.out.println("Failed to play sound");
			System.out.println(ae.getMessage());
			
		}
		
		panel = new JPanel();
		panel.setBorder(null);
		add(panel, "cell 3 1,grow");
		
		btnQuit = new JButton(I18n.getText("menus.edit.stop_measuring"));
		panel.add(btnQuit);
		
				
				btnReset = new JButton("Reset");
				panel.add(btnReset);
				
				btnRecord = new JButton("Record");
				panel.add(btnRecord);
				btnRecord.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						dev.requestMeasurement();	
					}
				});
				btnReset.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						dev.zeroMeasurement();
						
					}
				});
				
		txtCurrentValue = new JLabel();
		txtCurrentValue.setFont(new Font("Synchro LET", Font.BOLD, 20));
		txtCurrentValue.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCurrentValue.setText("0"+" \u03bc"+"m");
		add(txtCurrentValue, "cell 7 1,growx");
		
		
		lastMeasurement = new JLabel("[Last measurement: ]");
		add(lastMeasurement, "cell 3 2 5 1,alignx right,aligny center");
				
				

		
		// Set the device to zero to start with
		
		if(dev!=null)
		{
			dev.setMeasurementReceiver(this);
		
			if(dev.isRequestDataCapable())
			{
				dev.zeroMeasurement();
			}
			// Show/hide data request buttons depending on platform abilities
			btnRecord.setVisible(dev.isRequestDataCapable());
			btnReset.setVisible(dev.isRequestDataCapable());
			txtCurrentValue.setVisible(dev.isCurrentValueCapable());
		}
		else
		{
			btnRecord.setVisible(false);
			btnReset.setVisible(false);
			txtCurrentValue.setVisible(false);
		}
	}
		
	public void receiverUpdateStatus(String status) {
		lastMeasurement.setText(status);
	}
	
	protected Boolean checkNewValueIsValid(Integer value)
	{
		if(value.intValue() == 0) 
		{
			// Value was zero so must be an error
			if(measure_error != null)
				measure_error.play();
			
			lastMeasurement.setText("Error: measurement was zero");

			return false;
		}
		else if (value.intValue() >= 50000)
		{
			// Value was over 5cm so warn user
			if(measure_error != null)
				measure_error.play();
			
			Alert.message("Warning", "This measurement was over 5cm so it will be disregarded!");
			
			lastMeasurement.setText("Error: measurement was too big: " + value +"\u03bc"+"m");

			return false;
			
		}
		else if (value.intValue() < 0)
		{
			// Value was negative so warn user
			if(measure_error != null)
				measure_error.play();
			
			Alert.message("Warning", "This measurement was negative so it will be disregarded!");
			
			lastMeasurement.setText("Error: measurement was negative: " + value +"\u03bc"+"m");

			return false;
			
		}
		
		return true;
	}
	
	public abstract void receiverNewMeasurement(Integer value);
	
	public void cleanup() {
		dev.close();
	}

	@Override
	public void receiverUpdateCurrentValue(Integer value) {
		
		NormalTridasUnit displayUnits = NormalTridasUnit.valueOf(App.prefs.getPref("corina.displayunits", NormalTridasUnit.HUNDREDTH_MM.value().toString()));
		
		if(displayUnits.equals(NormalTridasUnit.MICROMETRES))
		{
			txtCurrentValue.setText(String.valueOf(value)+" \u03bc"+"m");	
		}
		else if (displayUnits.equals(NormalTridasUnit.HUNDREDTH_MM))
		{
			txtCurrentValue.setText(String.valueOf(value/10));	
		}
	}
	
	public void setDefaultFocus()
	{
		btnRecord.requestFocusInWindow();
	}
	

}



