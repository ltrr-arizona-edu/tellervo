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

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.FontUtil;
import org.tellervo.desktop.util.MultiMonitorRobot;
import org.tellervo.desktop.util.SoundUtil;
import org.tellervo.desktop.util.SoundUtil.SystemSound;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.NormalTridasUnit;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;


public abstract class MeasurePanel extends JPanel implements MeasurementReceiver, AWTEventListener{

	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(MeasurePanel.class);
	
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
	private Color bgcolor = null;
	protected JToggleButton btnMouseTrigger;
	protected JTextArea txtInfoMessage;

	private Point mousePoint;
	
	private final Window parent;
	private Color parentBGColor;
	private JPanel panelInfo;
	private JLabel lblInfoIcon;
	protected String measureMessage = "";
	private JScrollPane scrollPane;
	
	private JPanel panelButtons;
	private JPanel panelLastPosition;
	private JPanel panelCurrentPosition;
	private JPanel panelLastValue;
	private Color lcdBlueColor = new Color(5, 116, 255);
	private JPanel panelDigitalDisplay;
	
	public MeasurePanel(final AbstractMeasuringDevice device, Color bgcolor, Window parent)
	{
		this.bgcolor = bgcolor;
		this.parent = parent;
		
		init(device);
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public MeasurePanel(final AbstractMeasuringDevice device, Window parent) {
		this.parent = parent;
		parentBGColor = parent.getBackground();
		init(device);
	}
	
	protected void setInfoMessageVisible(Boolean b)
	{
		txtInfoMessage.setVisible(b);
		scrollPane.setVisible(b);
		lblInfoIcon.setVisible(b);
	}
	
	private void init(final AbstractMeasuringDevice device)
	{
		setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Measuring Controls", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.setBackground(bgcolor);	
		dev = device;
		
		setLayout(new MigLayout("hidemode 2,insets 0", "[grow,fill][][]", "[20px,grow,fill][fill]"));
				
		SoundUtil.playSystemSound(SystemSound.MEASURING_PLATFORM_INIT);
		
		panelInfo = new JPanel();
		add(panelInfo, "cell 0 0,growx,wmin 10");
		panelInfo.setLayout(new MigLayout("", "[30px:30px:30px,center][126.00px,grow,fill]", "[grow]"));
		
		lblInfoIcon = new JLabel("");
		lblInfoIcon.setIcon(Builder.getIcon("info.png", 22));
		panelInfo.add(lblInfoIcon, "pad 5 0 5 0,cell 0 0,alignx center,aligny top");
		
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelInfo.add(scrollPane, "cell 1 0,grow");
		
		txtInfoMessage = new JTextArea();
		scrollPane.setViewportView(txtInfoMessage);
		txtInfoMessage.setBorder(new LineBorder(Color.GRAY));
		txtInfoMessage.setBackground(Color.WHITE);
		txtInfoMessage.setWrapStyleWord(true);
		txtInfoMessage.setEditable(false);
		txtInfoMessage.setLineWrap(true);
		
		
		
		long eventMask = AWTEvent.MOUSE_EVENT_MASK + AWTEvent.KEY_EVENT_MASK + AWTEvent.MOUSE_MOTION_EVENT_MASK;
		Toolkit.getDefaultToolkit().addAWTEventListener(this, eventMask);		
				
				panelButtons = new JPanel();
				add(panelButtons, "cell 1 0 2 1,grow");
				panelButtons.setLayout(new MigLayout("", "[105.00,grow,fill]", "[][][122.00,grow]"));
				
				btnMouseTrigger = new JToggleButton(I18n.getText("measuring.mousetrigger.enable"));
				panelButtons.add(btnMouseTrigger, "cell 0 0,growx");
				btnMouseTrigger.setFocusable(false);
				btnMouseTrigger.setIcon(Builder.getIcon("mouse.png", 22));
				
				btnQuit = new JButton(I18n.getText("menus.edit.stop_measuring"));
				panelButtons.add(btnQuit, "cell 0 1,growx");
				btnQuit.setIcon(Builder.getIcon("stop.png", 22));
								
								panelDigitalDisplay = new JPanel();
								add(panelDigitalDisplay, "cell 0 1,grow");
								panelDigitalDisplay.setLayout(new MigLayout("hidemode 3", "[grow,fill][grow,fill][grow,fill]", "[57px,fill]"));
								
								panelLastPosition = new JPanel();
								panelDigitalDisplay.add(panelLastPosition, "cell 0 0,alignx left,aligny top");
								panelLastPosition.setBorder(null);
								panelLastPosition.setBackground(Color.BLACK);
								panelLastPosition.setLayout(new MigLayout("novisualpadding", "[grow,fill]", "[29.00px,grow,fill][]"));
								
								txtLastPosition = new JLabel();
								panelLastPosition.add(txtLastPosition, "cell 0 0,alignx left,aligny top");
								txtLastPosition.setFont(FontUtil.getFontFromResources("Digitaldream.ttf", 25, Font.PLAIN));
								txtLastPosition.setForeground(lcdBlueColor);
								txtLastPosition.setHorizontalAlignment(SwingConstants.RIGHT);
								txtLastPosition.setText("-");
								txtLastPosition.setBackground(Color.WHITE);
								txtLastPosition.setBorder(null);
								
								
								lblLastPosition = new JLabel("Previous cumulative position ("+micron()+")");
								lblLastPosition.setForeground(Color.LIGHT_GRAY);
								panelLastPosition.add(lblLastPosition, "cell 0 1");
								lblLastPosition.setFont(new Font("Dialog", Font.PLAIN, 8));
								
								panelLastValue = new JPanel();
								panelDigitalDisplay.add(panelLastValue, "cell 1 0");
								panelLastValue.setBackground(Color.BLACK);
								panelLastValue.setLayout(new MigLayout("", "[grow,fill]", "[24px,grow,fill][]"));
								
								txtLastValue = new JLabel();
								panelLastValue.add(txtLastValue, "cell 0 0,alignx left,aligny top");
								
								
										
										txtLastValue.setFont(FontUtil.getFontFromResources("Digitaldream.ttf", 25, Font.PLAIN));
										txtLastValue.setForeground(lcdBlueColor);
										txtLastValue.setHorizontalAlignment(SwingConstants.RIGHT);
										txtLastValue.setText("-");
										txtLastValue.setBackground(Color.WHITE);
										txtLastValue.setBorder(null);
										
										lblLastValue = new JLabel("Previous measurement ("+micron()+")");
										lblLastValue.setForeground(Color.LIGHT_GRAY);
										panelLastValue.add(lblLastValue, "cell 0 1");
										lblLastValue.setFont(new Font("Dialog", Font.PLAIN, 8));
										
										panelCurrentPosition = new JPanel();
										panelDigitalDisplay.add(panelCurrentPosition, "cell 2 0");
										panelCurrentPosition.setBackground(Color.BLACK);
										panelCurrentPosition.setLayout(new MigLayout("", "[150,grow]", "[grow,fill][]"));
										
										txtCurrentPosition = new JLabel();
										txtCurrentPosition.setText("-");
										panelCurrentPosition.add(txtCurrentPosition, "cell 0 0,growx");
										txtCurrentPosition.setFont(FontUtil.getFontFromResources("Digitaldream.ttf", 25, Font.PLAIN));
										txtCurrentPosition.setForeground(lcdBlueColor);
										txtCurrentPosition.setHorizontalAlignment(SwingConstants.RIGHT);
										txtCurrentPosition.setBackground(Color.WHITE);
										txtCurrentPosition.setBorder(null);
										
										lblCurrentPosition = new JLabel("Live position ("+micron()+")");
										lblCurrentPosition.setForeground(Color.LIGHT_GRAY);
										panelCurrentPosition.add(lblCurrentPosition, "cell 0 1");
										lblCurrentPosition.setFont(new Font("Dialog", Font.PLAIN, 8));
										
												
												btnReset = new JButton("Zero");
												add(btnReset, "cell 1 1");
												btnReset.setIcon(Builder.getIcon("zero.png", 22));
												
												btnRecord = new JButton("Record");
												add(btnRecord, "cell 2 1");
												btnRecord.setIcon(Builder.getIcon("record.png", 22));
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
														setLastPosition(0);
														
													}
												});
						btnMouseTrigger.addActionListener(new ActionListener(){

							@Override
							public void actionPerformed(ActionEvent arg0) {
								//btnMouseTrigger.setSelected(btnMouseTrigger.isSelected());
								toggleMouseTrigger(btnMouseTrigger.isSelected());
								
							}
						});
				setCurrentPosition(null);

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
			btnMouseTrigger.setVisible(dev.isRequestDataCapable());
			this.panelCurrentPosition.setVisible(dev.isCurrentValueCapable());
			this.panelLastPosition.setVisible(dev.measureCumulatively);
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
		
		this.setMessageText("Ready to measure...");	
	}
	
	protected void updateInfoText()
	{
		try{
			if(btnMouseTrigger.isSelected())
			{
				btnMouseTrigger.setText(I18n.getText("measuring.mousetrigger.ison"));
				txtInfoMessage.setText(I18n.getText("menus.edit.measuremode.mousetriggerinfo.on")+"\n\n"+measureMessage);
			}
			else
			{
				btnMouseTrigger.setText(I18n.getText("measuring.mousetrigger.enable"));
				txtInfoMessage.setText(measureMessage);
			}
		} catch (Exception e)
		{
			
		}
	}
	
	private void toggleMouseTrigger(boolean selected) {
		
		btnMouseTrigger.setSelected(selected);
		updateInfoText();
		if(selected)
		{
			PointerInfo a = MouseInfo.getPointerInfo();
			mousePoint = a.getLocation();

			// Transparent 16 x 16 pixel cursor image.
			Image cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
	
			// Create a new blank cursor.
			Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
			    cursorImg, new Point(), "blank cursor");
	
			setCursor(blankCursor);
			
			btnMouseTrigger.setBackground(Color.RED);
			parent.repaint();
		
		}
		
		else
		{
			setCursor(Cursor.getDefaultCursor());
			btnMouseTrigger.setBackground(parentBGColor);
			parent.repaint();
		}
		

		
	}
	
	
	/**
	 * Set whether the current position gui should be visible
	 * 
	 * @param b
	 */
	public void setCurrentPositionGuiVisible(Boolean b)
	{
		txtCurrentPosition.setVisible(true);
		lblCurrentPosition.setVisible(true);
		if(b==false)
		{
			setCurrentPosition(null);
		}
	}
	
	/**
	 * Set whether the last position gui should be visible
	 * 
	 * @param b
	 */
	public void setLastPositionGuiVisible(Boolean b)
	{
		//txtLastPosition.setVisible(b);
		//lblLastPosition.setVisible(b);
		if(b==false)
		{
			setLastPosition(null);
		}
		
	}
	
	/**
	 * Set whether the last value gui should be visible 
	 * 
	 * @param b
	 */
	public void setLastValueGuiVisible(Boolean b)
	{
		//txtLastValue.setVisible(b);
		//lblLastValue.setVisible(b);
		if(b==false)
		{
			setLastValue(null);
		}
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
			log.debug("Setting current position to: N/A");
			txtCurrentPosition.setText("N/A");
		}
		else
		{
			log.debug("Setting current position to: "+i);
			txtCurrentPosition.setText(i+"");
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
			txtLastPosition.setText("N/A");
		}
		else
		{
			if(this.dev.getMeasureCumulatively())
			{
				txtLastPosition.setText(i+"");
			}
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
			txtLastValue.setText("N/A");
		}
		else
		{
			txtLastValue.setText(i+"");
		}
	}
	
	/**
	 * Update the status message
	 */
	public void receiverUpdateStatus(String status) {
		setMessageText(status);
	}
	
	public void setMessageText(String txt)
	{
		measureMessage = txt;
		this.updateInfoText();
	}
	
	protected Boolean checkNewValueIsValid(Integer value)
	{
		if(value.intValue() == 0) 
		{
			// Value was zero so must be an error
			SoundUtil.playSystemSound(SystemSound.MEASURE_ERROR);
			Alert.message("Warning", "This measurement was 0cm so it will be disregarded.\nRight click on cell to insert missing ring.");
			this.txtLastValue.setText("Error");

			return false;
		}
		else if (value.intValue() >= 50000)
		{
			// Value was over 5cm so warn user
			SoundUtil.playSystemSound(SystemSound.MEASURE_ERROR);
			
			Alert.message("Warning", "This measurement was over 5cm so it will be disregarded");
			
			this.txtLastValue.setText("Error");

			return false;
			
		}
		else if (value.intValue() < 0)
		{
			// Value was negative so warn user
			SoundUtil.playSystemSound(SystemSound.MEASURE_ERROR);
			
			Alert.message("Warning", "This measurement was negative so it will be disregarded");
			
			this.txtLastValue.setText("Error");

			return false;
			
		}
		
		return true;
	}
	
	public abstract void receiverNewMeasurement(Integer value);
	
	public void cleanup() {
		try{
			dev.close();
		} catch (Exception e)
		{
			log.error("Error closing measuring device");
		}
		
		Toolkit.getDefaultToolkit().removeAWTEventListener(this);
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
		
		/*if(displayUnits.equals(NormalTridasUnit.MICROMETRES))
		{
			setCurrentPosition(value);	
		}
		else if (displayUnits.equals(NormalTridasUnit.HUNDREDTH_MM))
		{
			setCurrentPosition(value/10);	
		}
		else if (displayUnits.equals(NormalTridasUnit.FIFTIETH_MM))
		{
			setCurrentPosition(value/20);	
		}
		else if (displayUnits.equals(NormalTridasUnit.TWENTIETH_MM))
		{
			setCurrentPosition(value/50);	
		}
		else if (displayUnits.equals(NormalTridasUnit.TENTH_MM))
		{
			setCurrentPosition(value/100);	
		}
		else
		{*/
			setCurrentPosition(value);	
		//}
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

	private void moveMouseBackToButton()
	{
		if(this.btnMouseTrigger.isSelected())
		{
			if(mousePoint!=null)
			{
				MultiMonitorRobot.moveMouseToVirtualCoords(mousePoint);
			}
		}
	}
	
	
	
	 public void eventDispatched(AWTEvent e)
	    {
	    	if(e instanceof KeyEvent && btnMouseTrigger.isSelected())
	    	{
				if(((KeyEvent)e).getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					// Escape pressed so cancel mouse trigger
					toggleMouseTrigger(false);
				}
	    	}
	    	
			else if (e instanceof MouseEvent && btnMouseTrigger.isSelected())
			{
				
				MouseEvent me = (MouseEvent) e;
				if(me.getID()==MouseEvent.MOUSE_CLICKED)
				{
					if(me.getButton() == MouseEvent.BUTTON1)
					{
						// Left click
						dev.requestMeasurement();	
						updateInfoText();
					}
					if(me.getButton() == MouseEvent.BUTTON3)
					{
						// Right click
						dev.zeroMeasurement();	
						updateInfoText();
					}
				}
				else if (me.getID()==MouseEvent.MOUSE_MOVED || me.getID()==MouseEvent.MOUSE_DRAGGED)
				{
					// Mouse was moved, so move it back
					moveMouseBackToButton();
				}
				
				me.consume();
				
			}
	    }
	
}



