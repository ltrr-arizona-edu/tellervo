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
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice.DataDirection;
import edu.cornell.dendro.corina.ui.I18n;

/**
 * @author Lucas Madar
 *
 */
public class EditorMeasurePanel extends MeasurePanel implements MeasurementReceiver {

	private static final long serialVersionUID = 1L;
	private Editor editor;
	private Integer cachedValue= null;
	
	@SuppressWarnings("serial")
	public EditorMeasurePanel(Editor myeditor, final AbstractSerialMeasuringDevice device) {
		super(device);
		editor = myeditor;
		
		lblMessage.setText(I18n.getText("editor.measuring.earlywood"));
	
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
				
		Year y = null;
		if(editor.getSample().containsSubAnnualData())
		{
			// Doing early/late wood measurements.  
			// Data are sent in pairs.  First value is 
			// cached locally.
			
			if (cachedValue==null)
			{
				cachedValue = value.intValue();
				lblMessage.setText(I18n.getText("editor.measuring.latewood"));
				setLastMeasurementIndicators(value);
				return;
			}
			else
			{
				y = editor.measured(cachedValue, value.intValue());
				cachedValue = null;
				lblMessage.setText(I18n.getText("editor.measuring.earlywood"));
			}
		}
		else
		{
			y = editor.measured(value.intValue());
		}
		
		if(y.column() == 0) {
			if(measure_dec != null)
				measure_dec.play();
		} else {
			if(measure_one != null)
				measure_one.play();				
		}
		
		setLastMeasurementIndicators(value);
	}
	
	private void setLastMeasurementIndicators(int value)
	{
		setLastValue(value);
		
		if(super.dev.isMeasureCumulativelyConfigurable())
		{
			setLastPosition(super.dev.getPreviousPosition());
		}
	}


	@Override
	public void receiverRawData(DataDirection dir, String value) {
		// IGNORE
		
	}



	


}
