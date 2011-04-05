/**
 * 
 */
package edu.cornell.dendro.corina.editor;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice;
import edu.cornell.dendro.corina.hardware.MeasurePanel;
import edu.cornell.dendro.corina.hardware.MeasurementReceiver;

/**
 * @author Lucas Madar
 *
 */
public class EditorMeasurePanel extends MeasurePanel implements MeasurementReceiver {

	private static final long serialVersionUID = 1L;
	private Editor editor;

	
	public EditorMeasurePanel(Editor myeditor, final AbstractSerialMeasuringDevice device) {
		super(device);
		editor = myeditor;
	
		btnQuit.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				editor.stopMeasuring();
			}
		});	
	}

	
	public void receiverNewMeasurement(Integer value) {
		
		if(!checkNewValueIsValid(value))
		{
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
		
		lastMeasurement.setText("[Last measurement was " + value +"\u03bc"+"m in year " + y.toString() + "]");		
	}


	@Override
	public void receiverRawData(String value) {
		//IGNORE
		
	}
	


}
