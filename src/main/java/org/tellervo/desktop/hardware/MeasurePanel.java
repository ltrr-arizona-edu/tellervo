/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
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
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.hardware;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gis.ITRDBMarkerLayerBuilder;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.SoundUtil;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.NormalTridasUnit;



public abstract class MeasurePanel extends JPanel implements MeasurementReceiver {

	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(MeasurePanel.class);

	/* audioclips to play... */
	protected AudioClip measure_one;
	protected AudioClip measure_dec;
	protected AudioClip measure_error;
	
	protected JButton btnReset;
	protected JButton btnRecord;
	protected JButton btnQuit;
	
	private JLabel lblCurrentPosition;
	private JLabel lblLastPosition;
	private JLabel lblLastValue;
	
	private JLabel txtLastValue;
	private JLabel txtLastPosition;
	private JLabel txtCurrentPosition;
	
	protected AbstractMeasuringDevice dev;
	private JPanel panel = new JPanel();
	protected JLabel lblMessage;
	private Color bgcolor = null;

	
	public MeasurePanel(final AbstractMeasuringDevice device, Color bgcolor)
	{
		this.bgcolor = bgcolor;
		init(device);
	}
	
	public MeasurePanel(final AbstractMeasuringDevice device) {
		init(device);
	}
	
	private void init(final AbstractMeasuringDevice device)
	{
		setBorder(null);
		this.setBackground(bgcolor);	
		dev = device;
		
		setLayout(new MigLayout("insets 0", "[150px,grow][150,grow][150.00px,grow]", "[][][14.00][][grow,fill]"));
		
		measure_one = SoundUtil.getMeasureSound();
		measure_dec = SoundUtil.getMeasureDecadeSound();
		measure_error = SoundUtil.getMeasureErrorSound();
		
		SoundUtil.playMeasureInitSound();

		
		panel.setBackground(bgcolor);
		add(panel, "cell 0 0 3 1,grow");
		
		btnQuit = new JButton(I18n.getText("menus.edit.stop_measuring"));
		btnQuit.setIcon(Builder.getIcon("stop.png", 22));
		panel.add(btnQuit);
		
				
				btnReset = new JButton("Zero");
				btnReset.setIcon(Builder.getIcon("zero.png", 22));
				panel.add(btnReset);
				
				btnRecord = new JButton("Record");
				btnRecord.setIcon(Builder.getIcon("record.png", 22));
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
		
		txtLastPosition = new JLabel();
		txtLastPosition.setFont(new Font("Synchro LET", Font.BOLD, 20));
		txtLastPosition.setHorizontalAlignment(SwingConstants.RIGHT);
		txtLastPosition.setText("-");
		txtLastPosition.setBackground(Color.WHITE);
		txtLastPosition.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		add(txtLastPosition, "cell 0 1,growx");
		
		txtLastValue = new JLabel();	
		txtLastValue.setFont(new Font("Synchro LET", Font.BOLD, 20));
		txtLastValue.setHorizontalAlignment(SwingConstants.RIGHT);
		txtLastValue.setText("-");
		txtLastValue.setBackground(Color.WHITE);
		txtLastValue.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		add(txtLastValue, "cell 1 1,growx");
		
		txtCurrentPosition = new JLabel();
		txtCurrentPosition.setFont(new Font("Synchro LET", Font.BOLD, 20));
		txtCurrentPosition.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCurrentPosition.setText("-");
		txtCurrentPosition.setBackground(Color.WHITE);
		txtCurrentPosition.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		add(txtCurrentPosition, "cell 2 1,growx");
		
		
		lblLastPosition = new JLabel("Previous position");
		lblLastPosition.setFont(new Font("Dialog", Font.BOLD, 8));
		add(lblLastPosition, "cell 0 2,alignx right,aligny center");
		
		lblLastValue = new JLabel("Previous measurement");
		lblLastValue.setFont(new Font("Dialog", Font.BOLD, 8));
		add(lblLastValue, "cell 1 2,alignx right");
		
		lblCurrentPosition = new JLabel("Current position:");
		lblCurrentPosition.setFont(new Font("Dialog", Font.BOLD, 8));
		add(lblCurrentPosition, "cell 2 2,alignx right");
		
		lblMessage = new JLabel("");
		add(lblMessage, "cell 0 3 3 1");
			
		
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
			txtCurrentPosition.setVisible(dev.isCurrentValueCapable());
		}
		else
		{
			btnRecord.setVisible(false);
			btnReset.setVisible(false);
			txtCurrentPosition.setVisible(false);
		}
		
		// Hide any displays that aren't supported
		setCurrentPositionGuiVisible(device.isCurrentValueCapable());
		setLastPositionGuiVisible(device.getMeasureCumulatively());
		
	}
		
	/**
	 * Set whether the current position gui should be visible
	 * 
	 * @param b
	 */
	public void setCurrentPositionGuiVisible(Boolean b)
	{
		txtCurrentPosition.setVisible(b);
		lblCurrentPosition.setVisible(b);
	}
	
	/**
	 * Set whether the last position gui should be visible
	 * 
	 * @param b
	 */
	public void setLastPositionGuiVisible(Boolean b)
	{
		txtLastPosition.setVisible(b);
		lblLastPosition.setVisible(b);
	}
	
	/**
	 * Set whether the last value gui should be visible 
	 * 
	 * @param b
	 */
	public void setLastValueGuiVisible(Boolean b)
	{
		txtLastValue.setVisible(b);
		lblLastValue.setVisible(b);	
	}
		
	/**
	 * Set the current position 
	 * 
	 * @param i
	 */
	public void setCurrentPosition(Integer i)
	{
		if(i==null)
		{
			txtCurrentPosition.setText("-");
		}
		else if (i.equals(0))
		{
			txtCurrentPosition.setText("Err: 0 "+micron());
		}
		else if (i < 0)
		{
			txtCurrentPosition.setText("Err: negative: "+i+" "+micron());
		}
		else
		{
			txtCurrentPosition.setText(i+" "+micron());
		}
	}
	
	/**
	 * Set the last position
	 * 
	 * @param i
	 */
	public void setLastPosition(Integer i)
	{
		if(i==null)
		{
			txtLastPosition.setText("-");
		}
		else if (i.equals(0))
		{
			txtLastPosition.setText("Err: 0 "+micron());
		}
		else if (i < 0)
		{
			txtLastPosition.setText("Err: negative: "+i+" "+micron());
		}
		else
		{
			txtLastPosition.setText(i+" "+micron());
		}
	}
	
	/**
	 * Set the last value
	 * 
	 * @param i
	 */
	public void setLastValue(Integer i)
	{
		if(i==null)
		{
			txtLastValue.setText("-");
		}
		else if (i.equals(0))
		{
			txtLastValue.setText("Err: 0 "+micron());
		}
		else if (i < 0)
		{
			txtLastValue.setText("Err: negative: "+i+" "+micron());
		}
		else
		{
			txtLastValue.setText(i+" "+micron());
		}
	}
	
	/**
	 * Update the status message
	 */
	public void receiverUpdateStatus(String status) {
		lblMessage.setText(status);
	}
	
	
	protected Boolean checkNewValueIsValid(Integer value)
	{
		if(value.intValue() == 0) 
		{
			// Value was zero so must be an error
			if(measure_error != null)
				measure_error.play();
			
			this.txtLastValue.setText("Err: 0 "+micron());

			return false;
		}
		else if (value.intValue() >= 50000)
		{
			// Value was over 5cm so warn user
			if(measure_error != null)
				measure_error.play();
			
			Alert.message("Warning", "This measurement was over 5cm so it will be disregarded!");
			
			lblLastPosition.setText("Error: too big: " + value +" "+micron());

			return false;
			
		}
		else if (value.intValue() < 0)
		{
			// Value was negative so warn user
			if(measure_error != null)
				measure_error.play();
			
			Alert.message("Warning", "This measurement was negative so it will be disregarded!");
			
			lblLastPosition.setText("Error: negative: " + value +" " + micron());

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
		
		//log.debug("New value received from platform : "+value);
		NormalTridasUnit displayUnits = NormalTridasUnit.MICROMETRES;
		
		try{
			displayUnits = TridasUtils.getUnitFromName(App.prefs.getPref(PrefKey.DISPLAY_UNITS, NormalTridasUnit.MICROMETRES.name().toString()));
		} catch (Exception e)
		{
			//log.error("unable to determine preferred units");	
		}
		
		if(displayUnits.equals(NormalTridasUnit.MICROMETRES))
		{
			txtCurrentPosition.setText(String.valueOf(value)+" "+micron());	
		}
		else if (displayUnits.equals(NormalTridasUnit.HUNDREDTH_MM))
		{
			txtCurrentPosition.setText(String.valueOf(value/10));	
		}
		else if (displayUnits.equals(NormalTridasUnit.FIFTIETH_MM))
		{
			txtCurrentPosition.setText(String.valueOf(value/20));
		}
		else if (displayUnits.equals(NormalTridasUnit.TWENTIETH_MM))
		{
			txtCurrentPosition.setText(String.valueOf(value/50));
		}
		else if (displayUnits.equals(NormalTridasUnit.TENTH_MM))
		{
			txtCurrentPosition.setText(String.valueOf(value/100));
		}
		else
		{
			txtCurrentPosition.setText(String.valueOf(value));	

		}
	}
	
	/**
	 * Static string to return the micron symbol
	 * 
	 * @return
	 */
	public static String micron()
	{
		return "\u03bc"+"m";
	}
	
	/**
	 * Set the default focus for the panel
	 */
	public void setDefaultFocus()
	{
		btnRecord.requestFocusInWindow();
	}
}



