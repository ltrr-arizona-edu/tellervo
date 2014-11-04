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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import javax.swing.border.BevelBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.ui.Builder;
import javax.swing.JTextField;

public class PlatformTestPanel extends JPanel implements ActionListener {
	private final static Logger log = LoggerFactory.getLogger(PlatformTestPanel.class);
	private static final long serialVersionUID = 1L;
	private JPanel contentPanel = new JPanel();
	private JTextPane txtDataReceived;
	private JTextPane txtLog;
	private AbstractMeasuringDevice device;
	private TestMeasurePanel panelControls;
	private JTextArea lblInfo;
	private String startupMessage = "Please attempt to measure a few rings...";
	private String errorMessage;
	private final JDialog parent;
	private Color bgcolor;
	private JTextField txtSend;
	private JButton btnSend;
	
	/**
	 * Create the dialog.
	 */
	public PlatformTestPanel(JDialog parent, AbstractMeasuringDevice device, Color bgcolor) {
		this.parent = parent;
		this.device = device;
		this.bgcolor = bgcolor;
		init();
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public PlatformTestPanel(Color bgcolor)
	{
		parent = null;
		this.bgcolor = bgcolor;
		init();
	}
	
	
	
	public void finish()
	{
		if(panelControls!=null)
		{
			panelControls.cancelCountdown();
			panelControls.cleanup();
		}
		
		if(parent!=null)
		{
			parent.dispose();
		}
		
		try{
		device.close();
		} catch (Exception e){}
		
	}

	/**
	 * Set up the device from the preferences
	 */
	public void setupDevice()
	{
    	// Make sure the device is closed
    	if(device!=null)
    	{
    		device.close();
    	}
		
		// Set up the measuring device
		try {
			device = MeasuringDeviceSelector.getSelectedDevice(true);
		} catch (IOException e) {
			log.error("Problem talking to device");
			errorMessage = e.getMessage();
		} catch (InstantiationException e) {
			log.error("Problem talking to device");
			errorMessage = e.getMessage();
		} catch (IllegalAccessException e) {
			log.error("Problem talking to device");
			errorMessage = e.getMessage();
		}
		try{	
			device.setPortParamsFromPrefs();
		} catch (IOException e)
		{
			log.error("Problem setting device from preferences");
			errorMessage = e.getMessage();
		}catch (UnsupportedPortParameterException e) 
		{
			log.error("Problem setting device from preferences");
			errorMessage = e.getMessage();
		
		} catch (Exception e)
		{
			log.error("Problem setting device from preferences");
			errorMessage = e.getMessage();
		}
	
		init();
	}
	
	public static void showDialog(AbstractMeasuringDevice device, Color bgcolor, Window parent)
	{
		JDialog dialog = new JDialog();
		final PlatformTestPanel panel = new PlatformTestPanel(dialog, device, bgcolor);
		
		dialog.setModal(true);
		dialog.setIconImage(Builder.getApplicationIcon());
		dialog.setTitle("Test Platform Connection");
		dialog.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				panel.finish();
			}
		});
		
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.getContentPane().add(panel, BorderLayout.CENTER);
		dialog.pack();
		dialog.setSize(700, 600);
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);
		

	}
	
	
	
	@Override
	public void setBackground(Color col)
	{
		super.setBackground(col);
		try{contentPanel.setBackground(col);
		panelControls.setBackground(col);
		} catch (NullPointerException e)
		{}
	}
	
	private void init()
	{
		contentPanel.removeAll();
		setLayout(new BorderLayout());
	
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setOpaque(false);
		add(contentPanel, BorderLayout.CENTER);
		
		if(device==null)
		{			
			contentPanel.add( new HardwareErrorPanel(errorMessage));
			return;
		}

		contentPanel.setLayout(new MigLayout("", "[][428.00px,grow,fill][]", "[75.00px,fill][247.00px,grow]"));

		{
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			contentPanel.add(tabbedPane, "cell 1 1,grow");
			{
				JPanel panelTitle = new JPanel();
				panelTitle.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
				panelTitle.setBackground(Color.WHITE);
				contentPanel.add(panelTitle, "cell 0 0 3 1,growx,aligny top");
				panelTitle.setLayout(new MigLayout("", "[grow]", "[75px,top]"));
				{
					JLabel lblIcon = new JLabel("");
					lblIcon.setIcon(Builder.getIcon("documentinfo.png", 22));
					panelTitle.add(lblIcon, "flowx,cell 0 0,aligny top");
				}
				{
					lblInfo = new JTextArea(startupMessage);
					lblInfo.setWrapStyleWord(true);
					lblInfo.setLineWrap(true);
					lblInfo.setEditable(false);
					lblInfo.setFocusable(false);
					lblInfo.setBorder(null);
					lblInfo.setFont(new Font("Dialog", Font.PLAIN, 11));
					panelTitle.add(lblInfo, "cell 0 0,growx,wmin 10");
	
				}
			}
			
			{
							{
								JPanel panelLog = new JPanel();
								panelLog.setBackground(bgcolor);
								tabbedPane.addTab("Data Received", null, panelLog, null);
								panelLog.setLayout(new MigLayout("", "[3px,grow]", "[21px,grow]"));
								{
									JScrollPane scrollPane = new JScrollPane();
									scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
									panelLog.add(scrollPane, "cell 0 0,grow");
									{
										txtDataReceived = new JTextPane();
										txtDataReceived.setEditable(false);
										scrollPane.setViewportView(txtDataReceived);
										
									}
								}
							}
							
							
				
							JPanel panelCapabilities = new JPanel();
							panelCapabilities.setBackground(bgcolor);
							tabbedPane.addTab("Capabilities", null, panelCapabilities, null);
							panelCapabilities.setLayout(new MigLayout("", "[][]", "[][][][]"));
							{
								JLabel lblZeroFromSoftware = new JLabel("Can be reset to zero from software:");
								panelCapabilities.add(lblZeroFromSoftware, "cell 0 0");
								
							}
							JLabel lblSoftZero = new JLabel("");
							panelCapabilities.add(lblSoftZero, "cell 1 0");
							{
								JLabel lblRecordFromSoftware = new JLabel("Accepts requests for data from software:");
								panelCapabilities.add(lblRecordFromSoftware, "cell 0 1");
							}
							JLabel lblSoftFire = new JLabel("");
							panelCapabilities.add(lblSoftFire, "cell 1 1");
							{
								JLabel lblRealtimeMeasurementValues = new JLabel("Reports live measurement values:");
								panelCapabilities.add(lblRealtimeMeasurementValues, "cell 0 2");
							}
							JLabel lblLiveValues = new JLabel("");
							panelCapabilities.add(lblLiveValues, "cell 1 2");
							{
								if(device.isRequestDataCapable())
								{
									lblSoftZero.setIcon(Builder.getIcon("success.png", 16));
								}
								else
								{
									lblSoftZero.setIcon(Builder.getIcon("cancel.png", 16));

								}
							}
							{
								if(device.isRequestDataCapable())
								{
									lblSoftFire.setIcon(Builder.getIcon("success.png", 16));
								}
								else
								{
									lblSoftFire.setIcon(Builder.getIcon("cancel.png", 16));

								}
							}
							{
								if(device.isCurrentValueCapable())
								{
									lblLiveValues.setIcon(Builder.getIcon("success.png", 16));
								}
								else
								{
									lblLiveValues.setIcon(Builder.getIcon("cancel.png", 16));

								}
							}
			}
			{
				JPanel panelCommLog = new JPanel();
				panelCommLog.setBackground(bgcolor);
				tabbedPane.addTab("Communications Log", null, panelCommLog, null);
				panelCommLog.setLayout(new MigLayout("", "[3px,grow,fill]", "[3px,grow,fill][]"));
				{
					JScrollPane scrollPane = new JScrollPane();
					panelCommLog.add(scrollPane, "cell 0 0,alignx left,aligny top");
					{
						txtLog = new JTextPane();
						txtLog.setFont(new Font("Monospaced", Font.PLAIN, 11));
						txtLog.setEditable(false);
						scrollPane.setViewportView(txtLog);
					
					}
				}
				{
					JPanel panel = new JPanel();
					panelCommLog.add(panel, "flowx,cell 0 1");
					panel.setLayout(new MigLayout("", "[114px,grow,fill][70px]", "[25px]"));
					{
						txtSend = new JTextField();
						panel.add(txtSend, "cell 0 0,alignx left,aligny center");
						txtSend.setColumns(10);
						txtSend.setVisible(device instanceof AbstractSerialMeasuringDevice && device.isRequestDataCapable());
						txtSend.setActionCommand("SendCommand");
						txtSend.addActionListener(this);
					}
					{
						btnSend = new JButton("Send");
						panel.add(btnSend, "cell 1 0,alignx left,aligny top");
						btnSend.setActionCommand("SendCommand");
						btnSend.addActionListener(this);
							
		
						btnSend.setVisible(device instanceof AbstractSerialMeasuringDevice && device.isRequestDataCapable());
					}
					
				}
			}
			
			
		}

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(bgcolor);
			add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new MigLayout("", "[][][113px,grow][54px]", "[25px]"));
			{
				JButton btnRestartTest = new JButton("Restart Test");
				btnRestartTest.setVisible(false);
				buttonPane.add(btnRestartTest, "cell 0 0");
				btnRestartTest.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						log.debug("Restarting measuring platform test");
						lblInfo.setText(startupMessage);
						txtLog.setText("");
						txtDataReceived.setText("");
						setupDevice();
						panelControls = new TestMeasurePanel(lblInfo, txtLog, txtDataReceived, device, bgcolor, parent);
						panelControls.startCountdown();
					}
					
				});
			}
			{
				JButton okButton = new JButton("Close");
				buttonPane.add(okButton, "cell 3 0,alignx left,aligny top");
				//getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						finish();
					}
				});
				
				if(parent==null)
				{
					okButton.setVisible(false);
				}
			}
			
		}
		{
			panelControls = new TestMeasurePanel(lblInfo, txtLog, txtDataReceived, device, bgcolor, parent);
			contentPanel.add(panelControls, "cell 0 1,alignx left,aligny top");
		}

	}

	public AbstractMeasuringDevice getDevice() {
		return device;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		
		if(evt.getActionCommand().equals("SendCommand"))
		{
			if(device instanceof AbstractSerialMeasuringDevice){
				if(txtSend.getText()!=null)
				{
					((AbstractSerialMeasuringDevice)device).sendRequest(txtSend.getText());
					txtSend.setText("");
				}
			}
		}
		
		
	}

}
