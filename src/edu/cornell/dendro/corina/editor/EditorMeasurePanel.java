/**
 * 
 */
package edu.cornell.dendro.corina.editor;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.AbstractAction;
import java.awt.event.*;

import edu.cornell.dendro.corina.hardware.*;

import java.applet.*;
import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;

import javax.swing.BoxLayout;


import java.awt.BorderLayout;
import net.miginfocom.swing.MigLayout;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Font;

/**
 * @author Lucas Madar
 *
 */
public class EditorMeasurePanel extends JPanel implements MeasurementReceiver {
	private JLabel lastMeasurement;
	private Editor editor;

	private AbstractSerialMeasuringDevice dev;
	
	/* audioclips to play... */
	private AudioClip measure_one;
	private AudioClip measure_dec;
	private AudioClip measure_error;
	
	private JButton btnReset;
	private JButton btnRecord;
	private JButton btnQuit;
	private JLabel txtCurrentValue;
	
	public EditorMeasurePanel(Editor myeditor, final AbstractSerialMeasuringDevice device) {
		
		editor = myeditor;
		

		

		setLayout(new MigLayout("", "[][][][][644.00px,grow][]", "[][][32px][][][][][]"));
		
		btnQuit = new JButton(I18n.getText("menus.edit.stop_measuring"));
		btnQuit.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				editor.stopMeasuring();
			}
		});
		
				JLabel text = new JLabel("<html><i>Currently in measure mode</i>. Table will not be manually editable.");
				add(text, "cell 0 0 5 1,alignx left,aligny center");		
		add(btnQuit, "cell 0 2,alignx left,aligny center");

		
		btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				device.zeroMeasurement();
				
			}
		});
		add(btnReset, "cell 1 2,alignx left,aligny center");
		
		btnRecord = new JButton("Record");
		btnRecord.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				device.requestMeasurement();	
			}
		});
		add(btnRecord, "cell 2 2,alignx left,aligny center");
		
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
		
		// now, watch for info!
		dev = device;
		dev.setMeasurementReceiver(this);
				
		txtCurrentValue = new JLabel();
		txtCurrentValue.setFont(new Font("Synchro LET", Font.BOLD, 20));
		txtCurrentValue.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCurrentValue.setText("0"+" \u03bc"+"m");
		add(txtCurrentValue, "cell 4 2,growx");
		
		
		lastMeasurement = new JLabel("[No last measurement]");
		add(lastMeasurement, "cell 0 3 5 1,alignx right,aligny center");
				
		// Show/hide data request buttons depending on platform abilities
		btnReset.setVisible(device.isRequestDataCapable());
		btnRecord.setVisible(device.isRequestDataCapable());
		txtCurrentValue.setVisible(device.isCurrentValueCapable());
		
		// Set the device to zero to start with
		if(device.isRequestDataCapable())
		{
			device.zeroMeasurement();
		}
	}
		
	public void receiverUpdateStatus(String status) {
		lastMeasurement.setText(status);
	}
	
	public void receiverNewMeasurement(Integer value) {
		
		if(value.intValue() == 0) 
		{
			// Value was zero so must be an error
			if(measure_error != null)
				measure_error.play();
			
			lastMeasurement.setText("Error: measurement was zero");

			return;
		}
		else if (value.intValue() >= 50000)
		{
			// Value was over 5cm so warn user
			if(measure_error != null)
				measure_error.play();
			
			Alert.message("Warning", "This measurement was over 5cm so it will be disregarded!");
			
			lastMeasurement.setText("Error: measurement was too big: " + value +"\u03bc"+"m");

			return;
			
		}
		else if (value.intValue() < 0)
		{
			// Value was negative so warn user
			if(measure_error != null)
				measure_error.play();
			
			Alert.message("Warning", "This measurement was negative so it will be disregarded!");
			
			lastMeasurement.setText("Error: measurement was negative: " + value +"\u03bc"+"m");

			return;
			
		}
		
		
		Year y = editor.measured(value.intValue());
		
		if(y.column() == 0) {
			if(measure_dec != null)
				measure_dec.play();
		} else {
			if(measure_one != null)
				measure_one.play();				
		}
		
		lastMeasurement.setText("Last measurement was " + value +"\u03bc"+"m in year " + y.toString() + "]");		
	}
	
	public void cleanup() {
		dev.close();
	}

	@Override
	public void receiverUpdateCurrentValue(Integer value) {
		txtCurrentValue.setText(String.valueOf(value)+" \u03bc"+"m");
		
	}
	

}
