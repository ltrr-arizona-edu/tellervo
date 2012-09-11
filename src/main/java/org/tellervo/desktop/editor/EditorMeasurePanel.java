/**
 * 
 */
package org.tellervo.desktop.editor;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.Year;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice;
import org.tellervo.desktop.hardware.MeasurePanel;
import org.tellervo.desktop.hardware.MeasurementReceiver;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice.DataDirection;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.SoundUtil;
import org.tellervo.desktop.util.SoundUtil.SystemSound;


/**
 * @author Lucas Madar
 *
 */
public class EditorMeasurePanel extends MeasurePanel implements MeasurementReceiver {

	private static final long serialVersionUID = 1L;
	private Editor editor;
	private Integer cachedValue= null;
	
	@SuppressWarnings("serial")
	public EditorMeasurePanel(Editor myeditor, final AbstractMeasuringDevice device) {
		super(device, myeditor);
		editor = myeditor;
		
		setMessageText("");
	
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
				setMessageText(I18n.getText("editor.measuring.latewood"));
				setLastMeasurementIndicators(value);
				return;
			}
			else
			{
				y = editor.measured(cachedValue, value.intValue());
				cachedValue = null;
				setMessageText(I18n.getText("editor.measuring.earlywood"));
			}
		}
		else
		{
			y = editor.measured(value.intValue());
		}
		
		if(y.column() == 0) 
		{
			SoundUtil.playSystemSound(SystemSound.MEASURE_DECADE);
		} else 
		{
			SoundUtil.playSystemSound(SystemSound.MEASURE_RING);
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
