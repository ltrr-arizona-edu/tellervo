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
import edu.cornell.dendro.corina.io.*;
import java.applet.*;
import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.ui.Builder;

/**
 * @author Lucas Madar
 *
 */
public class EditorMeasurePanel extends JPanel implements MeasurementReceiver {
	private JLabel lastMeasurement;
	private Editor editor;
	//private LegacyCorinaMeasuringDevice dev;
	private AbstractSerialMeasuringDevice dev;
	
	/* audioclips to play... */
	private AudioClip measure_one;
	private AudioClip measure_dec;
	private AudioClip measure_error;
	
	public EditorMeasurePanel(Editor myeditor, AbstractSerialMeasuringDevice device) {
		super(new FlowLayout(FlowLayout.RIGHT));
		
		editor = myeditor;
		
		lastMeasurement = new JLabel("[No last measurement]");
		add(lastMeasurement);
				
		JButton leave = Builder.makeButton("stop_measuring");
		add(leave);
		leave.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				editor.stopMeasuring();
			}
		});

		JLabel text = new JLabel("<html><i>Currently in measure mode</i>.<br>Table will not be manually editable.");
		add(text);		

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
			
		/* ignore this... */ }
		
		// now, watch for info!
		//dev = new LegacyCorinaMeasuringDevice(device, this);
		dev = device;
	}
	
//	public EditorMeasurePanel(Editor myEditor){
//		new EditorMeasurePanel(myEditor, new SerialDeviceSelector().getDevice());
//	}
	
	public void receiverUpdateStatus(String status) {
		lastMeasurement.setText(status);
	}
	
	public void receiverNewMeasurement(Integer value) {
		// this is an error; don't allow the measurement and play a bad noise.
		if(value.intValue() == 0 || value.intValue() > 9900) {
			if(measure_error != null)
				measure_error.play();
			
			lastMeasurement.setText("[Last measurement (error): " + value + "]");

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
		
		lastMeasurement.setText("[Last measurement: " + value + " in " + y.toString() + "]");		
	}
	
	public void cleanup() {
		dev.close();
	}
}
