package edu.cornell.dendro.corina.hardware;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTabbedPane;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;

import edu.cornell.dendro.corina.prefs.TestMeasurePanel;
import edu.cornell.dendro.corina.ui.Builder;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import java.awt.Font;
import javax.swing.JCheckBox;

public class PlatformTestDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextPane txtDataReceived;
	private JTextPane txtLog;
	private final AbstractSerialMeasuringDevice device;
	private TestMeasurePanel panelControls;
	private JLabel lblInfo;
	
	
	public void finish()
	{
		if(panelControls!=null)
		{
			panelControls.cancelCountdown();
			panelControls.dev.close();
		}
		
		dispose();
	}

	/**
	 * Create the dialog.
	 */
	public PlatformTestDialog(AbstractSerialMeasuringDevice device) {
		this.device = device;
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 364);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			contentPanel.add(tabbedPane);
			{
				JPanel panelTitle = new JPanel();
				panelTitle.setBackground(Color.WHITE);
				contentPanel.add(panelTitle, BorderLayout.NORTH);
				panelTitle.setLayout(new MigLayout("", "[grow]", "[]"));
				{
					lblInfo = new JLabel("Please attempt to measure a few rings...");
					lblInfo.setFont(new Font("Dialog", Font.PLAIN, 11));
					panelTitle.add(lblInfo, "cell 0 0");
				}
			}

			JPanel panelCapabilities = new JPanel();
			tabbedPane.addTab("Capabilities", null, panelCapabilities, null);
			panelCapabilities.setLayout(new MigLayout("", "[][]", "[][][]"));
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
				JPanel panelCommLog = new JPanel();
				tabbedPane.addTab("Communications Log", null, panelCommLog, null);
				panelCommLog.setLayout(new MigLayout("", "[3px,grow,fill]", "[3px,grow,fill]"));
				{
					JScrollPane scrollPane = new JScrollPane();
					panelCommLog.add(scrollPane, "cell 0 0,alignx left,aligny top");
					{
						txtLog = new JTextPane();
						txtLog.setEditable(false);
						scrollPane.setViewportView(txtLog);
					}
				}
			}
			
			{
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
						
						
						panelControls = new TestMeasurePanel(lblInfo, txtLog, txtDataReceived, device);
					}
				}
			}
		}
		{
			contentPanel.add(panelControls, BorderLayout.SOUTH);
		}

		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new MigLayout("", "[][][113px,grow][54px]", "[25px]"));
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
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

}
