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
/**
 * 
 */
package org.tellervo.desktop.hardware;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JTextPane;

import org.tellervo.desktop.hardware.AbstractSerialMeasuringDevice;
import org.tellervo.desktop.hardware.MeasurePanel;
import org.tellervo.desktop.hardware.MeasurementReceiver;
import org.tellervo.desktop.hardware.AbstractSerialMeasuringDevice.DataDirection;
import org.tellervo.desktop.ui.I18n;
import org.tridas.io.util.StringUtils;



/**
 * @author Lucas Madar
 *
 */
public class TestMeasurePanel extends MeasurePanel implements MeasurementReceiver {

	private static final long serialVersionUID = 1L;
	private final JTextPane txt;
	private final JTextPane log;
	private TimeoutTask task;
	private final JLabel infoLabel;
	
	
	public TestMeasurePanel(JLabel infoLabel, JTextPane txtLog, JTextPane txtComCheckLog, final AbstractSerialMeasuringDevice device, Color bgcolor) 
	{
		super(device, bgcolor);
		
		this.txt = txtComCheckLog;
		this.log = txtLog;
		this.infoLabel = infoLabel;

		// Hide extra widgets
		btnQuit.setVisible(false);
				
		startCountdown();
		
	}

	public void startCountdown()
	{
		java.util.Timer timer = new java.util.Timer();
		task = new TimeoutTask(infoLabel, this.lblMessage, this);
		timer.scheduleAtFixedRate(task, 0, 1000);
		
		log.setText(log.getText()+"********************************\n");
		log.setText(log.getText()+"Starting measuring platform test\n");
		log.setText(log.getText()+"********************************\n");
		log.setText(log.getText()+"Platform type: "+super.dev.toString()+"\n");
		log.setText(log.getText()+"Port         : "+super.dev.getPortName()+"\n");
		log.setText(log.getText()+"Baud rate    : "+super.dev.getBaud()+"\n");
		log.setText(log.getText()+"Data bits    : "+super.dev.getDataBits()+"\n");
		log.setText(log.getText()+"Stop bits    : "+super.dev.getStopBits()+"\n");
		log.setText(log.getText()+"Parity       : "+super.dev.getParity()+"\n");
		log.setText(log.getText()+"Flow control : "+super.dev.getFlowControl()+"\n");
		log.setText(log.getText()+"Line feed    : "+super.dev.getLineFeed()+"\n");
		log.setText(log.getText()+"Cumulative   : "+super.dev.getMeasureCumulatively()+"\n");
		log.setText(log.getText()+"********************************\n");
	}
	
	public void cancelCountdown()
	{
		if(task!=null)
		{
			task.cancel();
		}
		
		setLastValue(null);
	}
	

	public void receiverUpdateStatus(String status) {
		 txt.setText("["+I18n.getText("preferences.hardware.status")+"] : "+status+"\n");
	}
	
	public void receiverNewMeasurement(Integer value) {
		
		cancelCountdown();
		
		if(!checkNewValueIsValid(value))
		{
			return;
		}
				
		txt.setText(txt.getText()+value+"μm\n");
		infoLabel.setText("Success!  Data received from platform");
	}
		
	@Override
	public void receiverRawData(DataDirection dir, String value) {
		
		if(value==null) return;
		if(log==null) return;
		
		log.setText(log.getText()+StringUtils.leftPad("["+dir.toString()+"] ", 11)+value+"\n");
		try{ log.setCaretPosition(log.getText().length()-1); } catch (Exception e){ }
	}
	
	class TimeoutTask extends java.util.TimerTask
	{
		private JLabel label;
		private Integer countdown =11;
		private final JLabel infoLabel;
		TestMeasurePanel parent;
		
		TimeoutTask(JLabel infoLabel, JLabel label, TestMeasurePanel parent)
		{
			this.label = label;
			this.parent = parent;
			this.infoLabel = infoLabel;
		}
		
		@Override
		public void run() {
			if(countdown==1)
			{
				setLastValue(null);
				infoLabel.setText(I18n.getText("preferences.hardware.nodatareceived"));
				parent.lblMessage.setText("");
				this.cancel();
				//parent.setVisible(false);
				return;
			}
			
			countdown--;
			
			parent.lblMessage.setText(I18n.getText("preferences.hardware.timeremaining")+": "+countdown);
			
			
		}
		
	}



}
