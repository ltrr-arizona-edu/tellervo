/**
 * 
 */
package corina.editor;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.AbstractAction;
import java.awt.event.*;
import corina.io.*;

/**
 * @author Lucas Madar
 *
 */
public class EditorMeasurePanel extends JPanel implements SerialSampleIOListener {
	private JLabel lastMeasurement;
	private Editor editor;
	private SerialSampleIO port;
	
	public EditorMeasurePanel(Editor myeditor, SerialSampleIO ioport) {
		super(new FlowLayout(FlowLayout.RIGHT));
		
		editor = myeditor;
		port = ioport;
		
		port.addSerialSampleIOListener(this);

		lastMeasurement = new JLabel("[No last measurement]");
		add(lastMeasurement);
				
		JButton leave = new JButton("Stop measuring");
		add(leave);
		leave.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				editor.stopMeasuring();
			}
		});

		JLabel text = new JLabel("<html><i>Currently in measure mode</i>.<br>Table will not be manually editable.");
		add(text);		
	}
	
	public void cleanup() {
		port.close();
	}
	
	public void SerialSampleIONotify(SerialSampleIOEvent sse) {
		if(sse.getType() == SerialSampleIOEvent.BAD_SAMPLE_EVENT) {
			lastMeasurement.setText("There was an error reading the previous sample!");
		}
		else if(sse.getType() == SerialSampleIOEvent.NEW_SAMPLE_EVENT) {
			Integer value = (Integer) sse.getValue();
			lastMeasurement.setText("[Last measurement: " + value + "]");
			editor.measured(value.intValue());
		}
	}
}
