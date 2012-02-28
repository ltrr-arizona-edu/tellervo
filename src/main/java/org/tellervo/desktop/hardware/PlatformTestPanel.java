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
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import javax.swing.border.BevelBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.setupwizard.SetupWizard;
import org.tellervo.desktop.ui.Builder;

public class PlatformTestPanel extends JPanel {
	private final static Logger log = LoggerFactory.getLogger(PlatformTestPanel.class);
	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	private JTextPane txtDataReceived;
	private JTextPane txtLog;
	private AbstractSerialMeasuringDevice device;
	private TestMeasurePanel panelControls;
	private JLabel lblInfo;
	private String startupMessage = "Please attempt to measure a few rings...";
	private String errorMessage;
	
	/**
	 * Create the dialog.
	 */
	public PlatformTestPanel(AbstractSerialMeasuringDevice device) {
		
		if(device!=null)
		{
			init();
		}
	}
	
	public PlatformTestPanel()
	{
		
	}
	
	
	
	public void finish()
	{
		if(panelControls!=null)
		{
			panelControls.cancelCountdown();
			panelControls.dev.close();
		}
		
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
		try{
			device = SerialDeviceSelector.getSelectedDevice(true);
			device.setPortParamsFromPrefs();
		} catch (IOException e)
		{
			log.warn("Problem setting device from preferences");
			errorMessage = e.getMessage();
		}catch (UnsupportedPortParameterException e) 
		{
			log.warn("Problem setting device from preferences");
			errorMessage = e.getMessage();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			log.warn("Problem setting device from preferences");
			errorMessage = e.getMessage();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			log.warn("Problem setting device from preferences");
			errorMessage = e.getMessage();
		}
		
		init();
	}
	
	public static void showDialog(AbstractSerialMeasuringDevice device)
	{
		final JDialog dialog = new JDialog();
		final PlatformTestPanel panel = new PlatformTestPanel(device);
		
		dialog.setModal(true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setIconImage(Builder.getApplicationIcon());
		dialog.setTitle("Test Platform Connection");
		
		dialog.setLayout(new BorderLayout());
		dialog.add(panel, BorderLayout.CENTER);
		dialog.pack();
		dialog.setVisible(true);
		
		dialog.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				panel.finish();
				dialog.dispose();
			}
		});
	}
		
	private void init()
	{
		
		setBounds(100, 100, 569, 355);
		setLayout(new BorderLayout());
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setOpaque(false);
		add(contentPanel, BorderLayout.CENTER);
		
		if(device==null)
		{			
			contentPanel.add( new HardwareErrorPanel(errorMessage));
			return;
		}

		contentPanel.setLayout(new MigLayout("", "[428.00px,grow]", "[50.00px,fill][247.00px,grow][74.00px:74.00px:74.00px]"));
		{
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			contentPanel.add(tabbedPane, "cell 0 1,grow");
			{
				JPanel panelTitle = new JPanel();
				panelTitle.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
				panelTitle.setBackground(Color.WHITE);
				contentPanel.add(panelTitle, "cell 0 0,growx,aligny top");
				panelTitle.setLayout(new MigLayout("", "[grow]", "[]"));
				{
					lblInfo = new JLabel(startupMessage);
					lblInfo.setFont(new Font("Dialog", Font.PLAIN, 11));
					panelTitle.add(lblInfo, "cell 0 0");
					lblInfo.setIcon(Builder.getIcon("documentinfo.png", 22));
				}
			}
			
			{
							{
								JPanel panelLog = new JPanel();
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
				tabbedPane.addTab("Communications Log", null, panelCommLog, null);
				panelCommLog.setLayout(new MigLayout("", "[3px,grow,fill]", "[3px,grow,fill]"));
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
			}
			
			
		}
		{
			panelControls = new TestMeasurePanel(lblInfo, txtLog, txtDataReceived, device);
			contentPanel.add(panelControls, "cell 0 2,alignx left,aligny top");
		}

		{
			JPanel buttonPane = new JPanel();
			add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new MigLayout("", "[][][113px,grow][54px]", "[25px]"));
			{
				JButton btnRestartTest = new JButton("Restart Test");
				buttonPane.add(btnRestartTest, "cell 0 0");
				btnRestartTest.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						lblInfo.setText(startupMessage);
						txtLog.setText("");
						txtDataReceived.setText("");
						panelControls.startCountdown();
					}
					
				});
			}
			{
				JButton okButton = new JButton("Close");
				buttonPane.add(okButton, "cell 3 0,alignx left,aligny top");
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						finish();
					}
				});
			}
		}
	}

	public AbstractSerialMeasuringDevice getDevice() {
		return device;
	}

}
