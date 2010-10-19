/*
 * Ui_PreferencesPanel.java
 *
 * Created on October 17, 2008, 11:07 AM
 */

package edu.cornell.dendro.corina.prefs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.gis.GrfxWarning;
import edu.cornell.dendro.corina.gis.WMSTableModel;
import edu.cornell.dendro.corina.hardware.AbstractSerialMeasuringDevice;
import edu.cornell.dendro.corina.hardware.SerialDeviceSelector;
import edu.cornell.dendro.corina.hardware.SerialMeasuringDeviceConstants;
import edu.cornell.dendro.corina.prefs.wrappers.FormatWrapper;
import edu.cornell.dendro.corina.schema.WSIWmsServer;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.Font;

/**
 *
 * @author  lucasm
 */
public class Ui_PreferencesPanel extends javax.swing.JPanel implements ActionListener{

	private static final long serialVersionUID = -4791515457924840453L;
	final JFileChooser fc = new JFileChooser();
	WMSTableModel wmsModel = new WMSTableModel();
	GrfxWarning warn = new GrfxWarning();
	AbstractSerialMeasuringDevice device;
	
    /** Creates new form Ui_PreferencesPanel */
    public Ui_PreferencesPanel() {
        
        setupGUI();

        
    }

    private void setupGUI()   
    {
    	initComponents();
    	
    
    	// Enable/Disable mapping 
    	setMappingEnabled(!App.prefs.getBooleanPref("opengl.failed", false));
    	
    	// Set up WMS stuff
    	this.tblWMS.setModel(wmsModel);
    	populateWMSTable();
    	this.btnWMSAdd.setEnabled(false);
    	this.btnWMSRemove.setEnabled(false);
    	
    	// Set up platform types
    	new FormatWrapper(cboPlatformType, 
    			Prefs.SERIAL_DEVICE, 
    			App.prefs.getPref(Prefs.SERIAL_DEVICE, SerialMeasuringDeviceConstants.NONE), 
    			SerialMeasuringDeviceConstants.ALL_DEVICES);
    	lblPort = new javax.swing.JLabel();
    	
    	        lblPort.setText("Port:");
    	        panelPlatform.add(lblPort, "2, 3, left, center");
    	cboPort = new javax.swing.JComboBox();
    	
    	        cboPort.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "COM1", "COM2", "COM3", "COM4" }));
    	        panelPlatform.add(cboPort, "4, 3, 5, 1, left, top");
    	
    	lblDatabits = new JLabel("Databits:");
    	panelPlatform.add(lblDatabits, "6, 5, left, default");
    	
    	cboDatabits = new JComboBox();
    	cboDatabits.setEnabled(false);
    	cboDatabits.setModel(new DefaultComboBoxModel(new String[] {"8"}));
    	panelPlatform.add(cboDatabits, "8, 5, left, default");
    	
    	JLabel lblParity = new JLabel("Parity:");
    	panelPlatform.add(lblParity, "2, 7, left, default");
    	
    	JComboBox cboParity = new JComboBox();
    	cboParity.setEnabled(false);
    	cboParity.setModel(new DefaultComboBoxModel(new String[] {"None"}));
    	panelPlatform.add(cboParity, "4, 7, left, default");
    	
    	lblStopbits = new JLabel("Stopbits:");
    	panelPlatform.add(lblStopbits, "6, 7, left, default");
    	
    	cboStopbits = new JComboBox();
    	cboStopbits.setEnabled(false);
    	cboStopbits.setModel(new DefaultComboBoxModel(new String[] {"1", "2"}));
    	panelPlatform.add(cboStopbits, "8, 7, left, default");
    	panelPlatform.add(lblBaud, "2, 5, left, center");
    	panelPlatform.add(cboBaud, "4, 5, left, top");
    	
    	lblUnits = new JLabel("Units:");
    	panelPlatform.add(lblUnits, "2, 9, left, center");
    	
    	cboUnits = new JComboBox();
    	cboUnits.setEnabled(false);
    	cboUnits.setModel(new DefaultComboBoxModel(new String[] {"N/A"}));
    	panelPlatform.add(cboUnits, "4, 9, left, default");
    	
    	lblLineFeed = new JLabel("Line feed:");
    	panelPlatform.add(lblLineFeed, "6, 9, left, center");
    	
    	cboLineFeed = new JComboBox();
    	cboLineFeed.setEnabled(false);
    	cboLineFeed.setModel(new DefaultComboBoxModel(new String[] {"N/A"}));
    	panelPlatform.add(cboLineFeed, "8, 9, left, default");
        this.btnStartMeasuring.setEnabled(cboPlatformType.getSelectedIndex()!=0);
        
        panelMeasureHolder = new JPanel();
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        GroupLayout gl_panelTestComms = new GroupLayout(panelTestComms);
        gl_panelTestComms.setHorizontalGroup(
        	gl_panelTestComms.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panelTestComms.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_panelTestComms.createParallelGroup(Alignment.LEADING)
        				.addComponent(btnStartMeasuring, GroupLayout.PREFERRED_SIZE, 209, GroupLayout.PREFERRED_SIZE)
        				.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 352, GroupLayout.PREFERRED_SIZE)
        				.addComponent(panelMeasureHolder, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 703, Short.MAX_VALUE))
        			.addContainerGap())
        );
        gl_panelTestComms.setVerticalGroup(
        	gl_panelTestComms.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panelTestComms.createSequentialGroup()
        			.addComponent(btnStartMeasuring)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(panelMeasureHolder, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap())
        );
        
        JLabel lblPlatformLogData = new JLabel("Platform log: measurements displayed in microns");
        lblPlatformLogData.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 11));
        scrollPane.setColumnHeaderView(lblPlatformLogData);
        
        txtComCheckLog = new JTextPane();
        scrollPane.setViewportView(txtComCheckLog);
        panelTestComms.setLayout(gl_panelTestComms);
        
    	cboPlatformType.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cboPlatformType.getSelectedIndex()!=0)
				{
					btnStartMeasuring.setEnabled(true);
				}
				else
				{
					btnStartMeasuring.setEnabled(false);

				}
				
			}
		});
    	
    	btnStartMeasuring.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				startMeasurementTest();
			}
		});
    	
    	internationalizeComponents();
    	
    }
    
    @SuppressWarnings("unchecked")
	private void populateWMSTable()
    {
    	ArrayList<WSIWmsServer> serverDetails = Dictionary.getMutableDictionary("wmsServerDictionary");

    	if(serverDetails==null || serverDetails.size()==0)
    	{
    		return;
    	}

    	
    }
    
      
    private void startMeasurementTest()
    {
    	if(device!=null)
    	{
    		device.close();
    	}
    	
    	txtComCheckLog.setText("");
    	
    	if(measurePanel!=null)
    	{
    		measurePanel.cancelCountdown();
    		measurePanel = null;
    		panelMeasureHolder.removeAll();
    	}
    	
		// Set up the measuring device
		try {
			device = new SerialDeviceSelector ().getDevice();	
		}
		catch (Exception ioe) {
			Alert.error(I18n.getText("error"), 
					I18n.getText("error.initExtComms"));
			return;
		} 
		
		// initialize 
		try{
			if(device!=null)
			{
				device.initialize();
			}
			else
			{
				return;
			}
		} catch (Exception ioe)
		{
			Alert.error(I18n.getText("error"), 
					I18n.getText("error.initExtComms")+".\n"+
					I18n.getText("error.possWrongComPort"));
			return;
		}
		
		// add the measure panel...
		measurePanel = new TestMeasurePanel(txtComCheckLog, device);
		panelMeasureHolder.setLayout(new BorderLayout());
		panelMeasureHolder.add(measurePanel, BorderLayout.CENTER);
		
		
	
    }
    
    private void internationalizeComponents()
    {
    	// main buttons
    	this.btnOk.setText(I18n.getText("general.ok"));
    	this.btnResetAll.setText(I18n.getText("preferences.resetAll"));
    	this.btnCancel.setText(I18n.getText("general.cancel"));

        // First tab
    	this.propertiesTabs.setTitleAt(0, I18n.getText("preferences.network"));
    	this.propertiesTabs.setIconAt(0, Builder.getIcon("networksettings.png", 22));
    	this.panelWebservice.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), I18n.getText("preferences.webservice")));
        this.panelNetworkConnections.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), I18n.getText("preferences.networkConnection")));
        this.panelEmail.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), I18n.getText("preferences.email")));   	
        this.lblWSURL.setText(I18n.getText("general.url")+":");
        this.lblSMTPServer.setText(I18n.getText("preferences.smtpServer")+":");
        this.lblProxyPort.setText(I18n.getText("general.port")+":");
        this.lblProxyPort1.setText(I18n.getText("general.port")+":");
        this.lblProxyServer.setText(I18n.getText("preferences.httpProxy")+":");
        this.lblProxyServer1.setText(I18n.getText("preferences.httpSecureProxy")+":");
        this.btnDefaultProxy.setText(I18n.getText("preferences.defaultProxy"));
        this.btnNoProxy.setText(I18n.getText("preferences.directConnection"));
        this.btnManualProxy.setText(I18n.getText("preferences.useManualProxy"));
        this.btnReloadDictionary.setText(I18n.getText("preferences.reloadDictionary"));
        
        // Second tab
        this.panelHardware.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), I18n.getText("preferences.measuringPlatform")));
        this.panelTestComms.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), I18n.getText("preferences.checkConnection")));        
    	this.propertiesTabs.setTitleAt(1, I18n.getText("preferences.hardware"));
    	this.propertiesTabs.setIconAt(1, Builder.getIcon("hardware.png", 22));
        this.lblPlatformType.setText(I18n.getText("general.type")+":");
        this.lblPort.setText(I18n.getText("general.port")+":");
        this.lblBaud.setText(I18n.getText("preferences.baud")+":");
        this.btnStartMeasuring.setText(I18n.getText("preferences.hardware.testmeasuring"));
        
        // Third tab
    	this.propertiesTabs.setTitleAt(2, I18n.getText("preferences.statistics"));
    	this.propertiesTabs.setIconAt(2, Builder.getIcon("chart.png", 22));
        this.panelNumberFormats.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), I18n.getText("preferences.numberFormats")));
        this.panelSigScores.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), I18n.getText("preferences.sigScores")));
        this.lblTScore.setText(I18n.getText("statistics.tscore")+":");
        this.lblRValue.setText(I18n.getText("statistics.rvalue")+":");
        this.lblTrend.setText(I18n.getText("statistics.trend")+":");
        this.lblDScore.setText(I18n.getText("statistics.dscore")+":");
        this.lblWJ.setText(I18n.getText("statistics.weiserjahre")+":");
        this.lblMinOverlap.setText(I18n.getText("preferences.minYearsOverlap")+":");
        this.lblMinOverlapDScore.setText(I18n.getText("preferences.minYearsOverlapForDScore")+":");
        this.chkHighlightSig.setText(I18n.getText("preferences.highlightSignificantYears")+":");
        this.lblHighlightColor.setText(I18n.getText("preferences.highlightColor")+":");
                
    	// Fourth tab
    	this.propertiesTabs.setTitleAt(3, I18n.getText("preferences.appearance"));
    	this.propertiesTabs.setIconAt(3, Builder.getIcon("appearance.png", 22));
        this.panelEditor.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), I18n.getText("preferences.sampleEditor")));
        this.panelCharts.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), I18n.getText("preferences.charts")));
        this.panelUI.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), I18n.getText("preferences.ui")));
        this.lblDisplayUnits.setText(I18n.getText("preferences.units")+":");
        this.lblTextColor.setText(I18n.getText("preferences.textColor")+":");
        this.lblEditorBGColor.setText(I18n.getText("preferences.backgroundColor")+":");
        this.btnFont.setText(I18n.getText("preferences.font")+":");
        this.chkShowEditorGrid.setText(I18n.getText("preferences.gridlines"));
        this.lblAxisCursorColor.setText(I18n.getText("preferences.axisCursorColor")+":");
        this.lblChartBGColor.setText(I18n.getText("preferences.backgroundColor")+":");
        this.lblGridColor.setText(I18n.getText("preferences.gridColor")+":");
        this.chkShowChartGrid.setText(I18n.getText("preferences.gridlines"));

        // Fifth tab
        this.propertiesTabs.setTitleAt(4, I18n.getText("preferences.mapping"));
    	this.propertiesTabs.setIconAt(4, Builder.getIcon("map.png", 22));
    	this.panelGrfxWarning.setLayout(new BorderLayout());
    	this.panelGrfxWarning.add(warn, BorderLayout.CENTER);
    	
    }
    
    public void setMappingEnabled(Boolean b)
    {
    	this.panelGrfxWarning.setVisible(!b);
    	    	
    	this.panelWMS.setVisible(b);
    	this.panelWMS.setEnabled(b);
    	this.btnWMSAdd.setEnabled(b);
    	this.btnWMSRemove.setEnabled(b);
    	this.tblWMS.setEnabled(b);
    	
    	if(!b)
    	{
    		warn.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                	if (evt.getActionCommand().equals("fail"))
            		{
            			Ui_PreferencesPanel.this.setMappingEnabled(false);
            		}
                	else if (evt.getActionCommand().equals("pass"))
                	{
                		Ui_PreferencesPanel.this.setMappingEnabled(true);
                	}
                }
            });
    	}
    	
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        proxyButtonGroup = new javax.swing.ButtonGroup();
        propertiesTabs = new javax.swing.JTabbedPane();
        panelNetworkConnections = new javax.swing.JPanel();
        panelWebservice = new javax.swing.JPanel();
        lblWSURL = new javax.swing.JLabel();
        txtWSURL = new javax.swing.JTextField();
        btnReloadDictionary = new javax.swing.JButton();
        panelEmail = new javax.swing.JPanel();
        lblSMTPServer = new javax.swing.JLabel();
        txtSMTPServer = new javax.swing.JTextField();
        panelProxy = new javax.swing.JPanel();
        lblProxyServer = new javax.swing.JLabel();
        txtProxyURL = new javax.swing.JTextField();
        lblProxyPort = new javax.swing.JLabel();
        spnProxyPort = new javax.swing.JSpinner();
        lblProxyServer1 = new javax.swing.JLabel();
        txtProxyURL1 = new javax.swing.JTextField();
        lblProxyPort1 = new javax.swing.JLabel();
        spnProxyPort1 = new javax.swing.JSpinner();
        btnDefaultProxy = new javax.swing.JRadioButton();
        btnNoProxy = new javax.swing.JRadioButton();
        btnManualProxy = new javax.swing.JRadioButton();
        panelHardware = new javax.swing.JPanel();
        panelPlatform = new javax.swing.JPanel();
        cboPlatformType = new javax.swing.JComboBox();
        lblPlatformType = new javax.swing.JLabel();
        lblBaud = new javax.swing.JLabel();
        cboBaud = new javax.swing.JComboBox();
        cboBaud.setEnabled(false);
        panelTestComms = new javax.swing.JPanel();
        btnStartMeasuring = new javax.swing.JButton();
        panelStatistics = new javax.swing.JPanel();
        panelNumberFormats = new javax.swing.JPanel();
        cboTScore = new javax.swing.JComboBox();
        lblTScore = new javax.swing.JLabel();
        lblRValue = new javax.swing.JLabel();
        cboRValue = new javax.swing.JComboBox();
        lblTrend = new javax.swing.JLabel();
        cboTrend = new javax.swing.JComboBox();
        lblDScore = new javax.swing.JLabel();
        cboDScore = new javax.swing.JComboBox();
        lblWJ = new javax.swing.JLabel();
        cboWJ = new javax.swing.JComboBox();
        panelSigScores = new javax.swing.JPanel();
        spnMinOverlap = new javax.swing.JSpinner();
        lblMinOverlap = new javax.swing.JLabel();
        lblMinOverlapDScore = new javax.swing.JLabel();
        spnMinOverlapDScore = new javax.swing.JSpinner();
        chkHighlightSig = new javax.swing.JCheckBox();
        cboHighlightColor = new javax.swing.JComboBox();
        lblHighlightColor = new javax.swing.JLabel();
        panelAppearance = new javax.swing.JPanel();
        panelEditor = new javax.swing.JPanel();
        cboDisplayUnits = new javax.swing.JComboBox();
        cboTextColor = new javax.swing.JComboBox();
        cboEditorBGColor = new javax.swing.JComboBox();
        btnFont = new javax.swing.JButton();
        chkShowEditorGrid = new javax.swing.JCheckBox();
        lblDisplayUnits = new javax.swing.JLabel();
        lblTextColor = new javax.swing.JLabel();
        lblEditorBGColor = new javax.swing.JLabel();
        lblFont = new javax.swing.JLabel();
        lblShowEditorGrid = new javax.swing.JLabel();
        panelCharts = new javax.swing.JPanel();
        lblAxisCursorColor = new javax.swing.JLabel();
        lblChartBGColor = new javax.swing.JLabel();
        cboChartBGColor = new javax.swing.JComboBox();
        cboAxisCursorColor = new javax.swing.JComboBox();
        lblGridColor = new javax.swing.JLabel();
        cboGridColor = new javax.swing.JComboBox();
        lblShowChartGrid = new javax.swing.JLabel();
        chkShowChartGrid = new javax.swing.JCheckBox();
        panelUI = new javax.swing.JPanel();
        scrollPaneUIDefaults = new javax.swing.JScrollPane();
        panelMapping = new javax.swing.JPanel();
        panelWMS = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblWMS = new javax.swing.JTable();
        btnWMSAdd = new javax.swing.JButton();
        btnWMSRemove = new javax.swing.JButton();
        panelGrfxWarning = new javax.swing.JPanel();
        panelButtons = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnResetAll = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        seperatorButtons = new javax.swing.JSeparator();

        propertiesTabs.setMinimumSize(new java.awt.Dimension(500, 400));

        panelWebservice.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Web service"));

        lblWSURL.setText("URL:");

        txtWSURL.setText("https://dendro.cornell.edu");
        txtWSURL.setMinimumSize(new java.awt.Dimension(5, 28));
        txtWSURL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtWSURLActionPerformed(evt);
            }
        });

        btnReloadDictionary.setText("Force Dictionary Reload");

        org.jdesktop.layout.GroupLayout gl_panelWebservice = new org.jdesktop.layout.GroupLayout(panelWebservice);
        panelWebservice.setLayout(gl_panelWebservice);
        gl_panelWebservice.setHorizontalGroup(
            gl_panelWebservice.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelWebservice.createSequentialGroup()
                .addContainerGap()
                .add(lblWSURL, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 125, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(gl_panelWebservice.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btnReloadDictionary)
                    .add(txtWSURL, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE))
                .addContainerGap())
        );
        gl_panelWebservice.setVerticalGroup(
            gl_panelWebservice.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelWebservice.createSequentialGroup()
                .add(gl_panelWebservice.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblWSURL)
                    .add(txtWSURL, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnReloadDictionary)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelEmail.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Email"));

        lblSMTPServer.setText("SMTP Server:");

        txtSMTPServer.setText("appsmtp.mail.cornell.edu");
        txtSMTPServer.setMinimumSize(new java.awt.Dimension(5, 28));

        org.jdesktop.layout.GroupLayout gl_panelEmail = new org.jdesktop.layout.GroupLayout(panelEmail);
        panelEmail.setLayout(gl_panelEmail);
        gl_panelEmail.setHorizontalGroup(
            gl_panelEmail.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelEmail.createSequentialGroup()
                .addContainerGap()
                .add(lblSMTPServer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(txtSMTPServer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                .addContainerGap())
        );
        gl_panelEmail.setVerticalGroup(
            gl_panelEmail.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelEmail.createSequentialGroup()
                .add(gl_panelEmail.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblSMTPServer)
                    .add(txtSMTPServer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelProxy.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Network Connection"));

        lblProxyServer.setText("HTTP Proxy:");
        lblProxyServer.setEnabled(false);

        txtProxyURL.setEnabled(false);
        txtProxyURL.setMinimumSize(new java.awt.Dimension(5, 28));

        lblProxyPort.setText("Port:");
        lblProxyPort.setEnabled(false);

        spnProxyPort.setEnabled(false);

        lblProxyServer1.setText("HTTPS Proxy:");
        lblProxyServer1.setEnabled(false);

        txtProxyURL1.setEnabled(false);
        txtProxyURL1.setMinimumSize(new java.awt.Dimension(5, 28));

        lblProxyPort1.setText("Port:");
        lblProxyPort1.setEnabled(false);

        spnProxyPort1.setEnabled(false);

        proxyButtonGroup.add(btnDefaultProxy);
        btnDefaultProxy.setText("Use system default proxy settings");
        btnDefaultProxy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDefaultProxyActionPerformed(evt);
            }
        });

        proxyButtonGroup.add(btnNoProxy);
        btnNoProxy.setText("Direct connection");

        proxyButtonGroup.add(btnManualProxy);
        btnManualProxy.setText("Use manual proxy settings:");

        org.jdesktop.layout.GroupLayout gl_panelProxy = new org.jdesktop.layout.GroupLayout(panelProxy);
        panelProxy.setLayout(gl_panelProxy);
        gl_panelProxy.setHorizontalGroup(
            gl_panelProxy.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelProxy.createSequentialGroup()
                .addContainerGap()
                .add(gl_panelProxy.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(gl_panelProxy.createSequentialGroup()
                        .add(gl_panelProxy.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(btnManualProxy)
                            .add(btnNoProxy)
                            .add(btnDefaultProxy))
                        .add(299, 299, 299))
                    .add(gl_panelProxy.createSequentialGroup()
                        .add(28, 28, 28)
                        .add(gl_panelProxy.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(lblProxyServer)
                            .add(lblProxyServer1))
                        .add(18, 18, 18)
                        .add(gl_panelProxy.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(txtProxyURL, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                            .add(txtProxyURL1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(gl_panelProxy.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, lblProxyPort)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, lblProxyPort1))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(gl_panelProxy.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(spnProxyPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(spnProxyPort1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .add(85, 85, 85))
        );
        gl_panelProxy.setVerticalGroup(
            gl_panelProxy.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelProxy.createSequentialGroup()
                .add(btnDefaultProxy)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnNoProxy)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnManualProxy)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_panelProxy.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(spnProxyPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblProxyPort)
                    .add(txtProxyURL, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblProxyServer))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_panelProxy.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(spnProxyPort1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblProxyPort1)
                    .add(txtProxyURL1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblProxyServer1))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout gl_panelNetworkConnections = new org.jdesktop.layout.GroupLayout(panelNetworkConnections);
        panelNetworkConnections.setLayout(gl_panelNetworkConnections);
        gl_panelNetworkConnections.setHorizontalGroup(
            gl_panelNetworkConnections.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelNetworkConnections.createSequentialGroup()
                .addContainerGap()
                .add(gl_panelNetworkConnections.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, panelWebservice, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(panelProxy, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, panelEmail, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        gl_panelNetworkConnections.setVerticalGroup(
            gl_panelNetworkConnections.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelNetworkConnections.createSequentialGroup()
                .addContainerGap()
                .add(panelWebservice, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(panelProxy, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(panelEmail, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(137, Short.MAX_VALUE))
        );

        propertiesTabs.addTab("Network", panelNetworkConnections);

        panelPlatform.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Measuring Platform"));

        cboPlatformType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "EveIO", "Velmex TA UniSlide with QC10 Encoder", "Lintab", "Generic serial platform" }));

        lblPlatformType.setText("Type:");

        lblBaud.setText("Baud:");

        cboBaud.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "9600", "4800", "2400", "1200", "300", "110" }));

        panelTestComms.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Test Connection"));

        btnStartMeasuring.setText("Test measuring");

        GroupLayout gl_panelHardware = new GroupLayout(panelHardware);
        gl_panelHardware.setHorizontalGroup(
        	gl_panelHardware.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panelHardware.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_panelHardware.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_panelHardware.createSequentialGroup()
        					.addComponent(panelPlatform, GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE)
        					.addContainerGap())
        				.addComponent(panelTestComms, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE)))
        );
        gl_panelHardware.setVerticalGroup(
        	gl_panelHardware.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panelHardware.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(panelPlatform, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(panelTestComms, GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
        			.addContainerGap())
        );
        panelHardware.setLayout(gl_panelHardware);
        panelPlatform.setLayout(new FormLayout(new ColumnSpec[] {
        		FormFactory.RELATED_GAP_COLSPEC,
        		ColumnSpec.decode("75px"),
        		FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
        		ColumnSpec.decode("max(174px;pref)"),
        		FormFactory.RELATED_GAP_COLSPEC,
        		ColumnSpec.decode("left:75px"),
        		FormFactory.RELATED_GAP_COLSPEC,
        		ColumnSpec.decode("max(86dlu;default)"),},
        	new RowSpec[] {
        		RowSpec.decode("27px"),
        		FormFactory.RELATED_GAP_ROWSPEC,
        		FormFactory.DEFAULT_ROWSPEC,
        		FormFactory.PARAGRAPH_GAP_ROWSPEC,
        		RowSpec.decode("27px"),
        		FormFactory.RELATED_GAP_ROWSPEC,
        		RowSpec.decode("27px"),
        		FormFactory.RELATED_GAP_ROWSPEC,
        		FormFactory.DEFAULT_ROWSPEC,}));
        panelPlatform.add(lblPlatformType, "2, 1, fill, center");
        panelPlatform.add(cboPlatformType, "4, 1, 5, 1, left, top");

        propertiesTabs.addTab("Hardware", panelHardware);

        panelNumberFormats.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Number Formats"));

        cboTScore.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0.5", "0.49", "0.492", "0.4915", "0.49152", "49%", "49.2%", "49.15%", "49.152%" }));
        cboTScore.setSelectedIndex(1);

        lblTScore.setText("T-score:");

        lblRValue.setText("R-value:");

        cboRValue.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0.5", "0.49", "0.492", "0.4915", "0.49152", "49%", "49.2%", "49.15%", "49.152%" }));
        cboRValue.setSelectedIndex(1);

        lblTrend.setText("Trend:");

        cboTrend.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0.5", "0.49", "0.492", "0.4915", "0.49152", "49%", "49.2%", "49.15%", "49.152%" }));
        cboTrend.setSelectedIndex(1);

        lblDScore.setText("D-score:");

        cboDScore.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0.5", "0.49", "0.492", "0.4915", "0.49152", "49%", "49.2%", "49.15%", "49.152%" }));
        cboDScore.setSelectedIndex(1);

        lblWJ.setText("Weiserjahre:");

        cboWJ.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0.5", "0.49", "0.492", "0.4915", "0.49152", "49%", "49.2%", "49.15%", "49.152%" }));
        cboWJ.setSelectedIndex(1);

        org.jdesktop.layout.GroupLayout gl_panelNumberFormats = new org.jdesktop.layout.GroupLayout(panelNumberFormats);
        panelNumberFormats.setLayout(gl_panelNumberFormats);
        gl_panelNumberFormats.setHorizontalGroup(
            gl_panelNumberFormats.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, gl_panelNumberFormats.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(gl_panelNumberFormats.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, lblRValue, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, lblTrend, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, lblDScore, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, lblWJ, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, lblTScore, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 94, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_panelNumberFormats.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(cboRValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cboTScore, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cboTrend, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cboDScore, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cboWJ, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        gl_panelNumberFormats.setVerticalGroup(
            gl_panelNumberFormats.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelNumberFormats.createSequentialGroup()
                .addContainerGap()
                .add(gl_panelNumberFormats.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblTScore)
                    .add(cboTScore, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_panelNumberFormats.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblRValue)
                    .add(cboRValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_panelNumberFormats.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblTrend)
                    .add(cboTrend, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_panelNumberFormats.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblDScore)
                    .add(cboDScore, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_panelNumberFormats.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblWJ)
                    .add(cboWJ, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        panelSigScores.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Significant Scores"));

        lblMinOverlap.setText("Minimum years overlap:");

        lblMinOverlapDScore.setText("Minimum overlap for D-Score:");

        chkHighlightSig.setText("Highlight significant scores");

        cboHighlightColor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Black", "Red", "Orange" }));

        lblHighlightColor.setText("Highlight color:");

        org.jdesktop.layout.GroupLayout gl_panelSigScores = new org.jdesktop.layout.GroupLayout(panelSigScores);
        panelSigScores.setLayout(gl_panelSigScores);
        gl_panelSigScores.setHorizontalGroup(
            gl_panelSigScores.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelSigScores.createSequentialGroup()
                .addContainerGap()
                .add(gl_panelSigScores.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(gl_panelSigScores.createSequentialGroup()
                        .add(gl_panelSigScores.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, lblMinOverlapDScore, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, lblMinOverlap, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(gl_panelSigScores.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(spnMinOverlapDScore, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                            .add(spnMinOverlap, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)))
                    .add(chkHighlightSig)
                    .add(gl_panelSigScores.createSequentialGroup()
                        .add(lblHighlightColor)
                        .add(18, 18, 18)
                        .add(cboHighlightColor, 0, 181, Short.MAX_VALUE)))
                .addContainerGap())
        );
        gl_panelSigScores.setVerticalGroup(
            gl_panelSigScores.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelSigScores.createSequentialGroup()
                .addContainerGap()
                .add(gl_panelSigScores.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblMinOverlap)
                    .add(spnMinOverlap, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_panelSigScores.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblMinOverlapDScore)
                    .add(spnMinOverlapDScore, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(chkHighlightSig)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(gl_panelSigScores.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblHighlightColor)
                    .add(cboHighlightColor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout gl_panelStatistics = new org.jdesktop.layout.GroupLayout(panelStatistics);
        panelStatistics.setLayout(gl_panelStatistics);
        gl_panelStatistics.setHorizontalGroup(
            gl_panelStatistics.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelStatistics.createSequentialGroup()
                .addContainerGap()
                .add(panelNumberFormats, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelSigScores, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        gl_panelStatistics.setVerticalGroup(
            gl_panelStatistics.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, gl_panelStatistics.createSequentialGroup()
                .addContainerGap()
                .add(gl_panelStatistics.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, panelSigScores, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, panelNumberFormats, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(294, 294, 294))
        );

        propertiesTabs.addTab("Statistics", panelStatistics);

        panelEditor.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Sample editor"));

        cboDisplayUnits.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1/100mm", "Microns" }));

        cboTextColor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Black", "Grey", "White" }));

        cboEditorBGColor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Black", "Grey", "White" }));

        btnFont.setText("Times New Roman 12pt");

        chkShowEditorGrid.setSelected(true);
        chkShowEditorGrid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkShowEditorGridActionPerformed(evt);
            }
        });

        lblDisplayUnits.setText("Display units:");

        lblTextColor.setText("Text color:");

        lblEditorBGColor.setText("Background color:");

        lblFont.setText("Font:");

        lblShowEditorGrid.setText("Gridlines:");

        org.jdesktop.layout.GroupLayout gl_panelEditor = new org.jdesktop.layout.GroupLayout(panelEditor);
        panelEditor.setLayout(gl_panelEditor);
        gl_panelEditor.setHorizontalGroup(
            gl_panelEditor.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelEditor.createSequentialGroup()
                .addContainerGap()
                .add(gl_panelEditor.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(lblEditorBGColor, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                    .add(lblTextColor, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblDisplayUnits, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblFont, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblShowEditorGrid, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_panelEditor.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(chkShowEditorGrid)
                    .add(btnFont, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, cboTextColor, 0, 395, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, cboEditorBGColor, 0, 395, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, cboDisplayUnits, 0, 395, Short.MAX_VALUE))
                .addContainerGap())
        );
        gl_panelEditor.setVerticalGroup(
            gl_panelEditor.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelEditor.createSequentialGroup()
                .add(gl_panelEditor.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblDisplayUnits, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cboDisplayUnits, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_panelEditor.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cboTextColor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblTextColor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_panelEditor.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblEditorBGColor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cboEditorBGColor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_panelEditor.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnFont)
                    .add(lblFont, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_panelEditor.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkShowEditorGrid)
                    .add(lblShowEditorGrid))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelCharts.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Charts"));

        lblAxisCursorColor.setText("Axis/cursor color:");

        lblChartBGColor.setText("Background color:");

        cboChartBGColor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Black", "Grey", "White" }));

        cboAxisCursorColor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Black", "Grey", "White" }));

        lblGridColor.setText("Gridline color:");

        cboGridColor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Turquoise", "Grey", "White" }));

        lblShowChartGrid.setText("Draw gridlines:");

        chkShowChartGrid.setSelected(true);
        chkShowChartGrid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkShowChartGridActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout gl_panelCharts = new org.jdesktop.layout.GroupLayout(panelCharts);
        panelCharts.setLayout(gl_panelCharts);
        gl_panelCharts.setHorizontalGroup(
            gl_panelCharts.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelCharts.createSequentialGroup()
                .addContainerGap()
                .add(gl_panelCharts.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(lblShowChartGrid, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblGridColor, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblChartBGColor, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblAxisCursorColor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 139, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_panelCharts.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(chkShowChartGrid)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, cboChartBGColor, 0, 392, Short.MAX_VALUE)
                    .add(gl_panelCharts.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cboGridColor, 0, 392, Short.MAX_VALUE))
                    .add(cboAxisCursorColor, 0, 392, Short.MAX_VALUE))
                .addContainerGap())
        );
        gl_panelCharts.setVerticalGroup(
            gl_panelCharts.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelCharts.createSequentialGroup()
                .add(gl_panelCharts.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cboAxisCursorColor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblAxisCursorColor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_panelCharts.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cboChartBGColor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblChartBGColor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_panelCharts.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cboGridColor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblGridColor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_panelCharts.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkShowChartGrid)
                    .add(lblShowChartGrid))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelUI.setBorder(javax.swing.BorderFactory.createTitledBorder("User interface"));

        org.jdesktop.layout.GroupLayout gl_panelUI = new org.jdesktop.layout.GroupLayout(panelUI);
        panelUI.setLayout(gl_panelUI);
        gl_panelUI.setHorizontalGroup(
            gl_panelUI.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelUI.createSequentialGroup()
                .addContainerGap()
                .add(scrollPaneUIDefaults, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)
                .addContainerGap())
        );
        gl_panelUI.setVerticalGroup(
            gl_panelUI.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelUI.createSequentialGroup()
                .add(scrollPaneUIDefaults, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout gl_panelAppearance = new org.jdesktop.layout.GroupLayout(panelAppearance);
        panelAppearance.setLayout(gl_panelAppearance);
        gl_panelAppearance.setHorizontalGroup(
            gl_panelAppearance.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, gl_panelAppearance.createSequentialGroup()
                .addContainerGap()
                .add(gl_panelAppearance.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, panelUI, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, panelEditor, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, panelCharts, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        gl_panelAppearance.setVerticalGroup(
            gl_panelAppearance.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelAppearance.createSequentialGroup()
                .addContainerGap()
                .add(panelEditor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(panelCharts, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(panelUI, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        propertiesTabs.addTab("Appearance", panelAppearance);

        panelWMS.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Web Map Services (WMS)"));

        tblWMS.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"NASA Earth Observations", "http://neowms.sci.gsfc.nasa.gov/wms/wms"}
            },
            new String [] {
                "Name", "URL"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblWMS);

        btnWMSAdd.setText("Add");

        btnWMSRemove.setText("Remove");

        org.jdesktop.layout.GroupLayout gl_panelWMS = new org.jdesktop.layout.GroupLayout(panelWMS);
        panelWMS.setLayout(gl_panelWMS);
        gl_panelWMS.setHorizontalGroup(
            gl_panelWMS.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, gl_panelWMS.createSequentialGroup()
                .addContainerGap()
                .add(gl_panelWMS.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)
                    .add(gl_panelWMS.createSequentialGroup()
                        .add(btnWMSRemove)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnWMSAdd)))
                .addContainerGap())
        );
        gl_panelWMS.setVerticalGroup(
            gl_panelWMS.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelWMS.createSequentialGroup()
                .add(gl_panelWMS.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnWMSAdd)
                    .add(btnWMSRemove))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout gl_panelGrfxWarning = new org.jdesktop.layout.GroupLayout(panelGrfxWarning);
        panelGrfxWarning.setLayout(gl_panelGrfxWarning);
        gl_panelGrfxWarning.setHorizontalGroup(
            gl_panelGrfxWarning.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 561, Short.MAX_VALUE)
        );
        gl_panelGrfxWarning.setVerticalGroup(
            gl_panelGrfxWarning.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 125, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout gl_panelMapping = new org.jdesktop.layout.GroupLayout(panelMapping);
        panelMapping.setLayout(gl_panelMapping);
        gl_panelMapping.setHorizontalGroup(
            gl_panelMapping.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, gl_panelMapping.createSequentialGroup()
                .addContainerGap()
                .add(gl_panelMapping.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, panelWMS, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, panelGrfxWarning, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        gl_panelMapping.setVerticalGroup(
            gl_panelMapping.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelMapping.createSequentialGroup()
                .add(13, 13, 13)
                .add(panelGrfxWarning, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelWMS, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        propertiesTabs.addTab("Mapping", panelMapping);

        btnOk.setText("OK");

        btnResetAll.setText("Reset all to default");

        btnCancel.setText("Cancel");

        seperatorButtons.setBackground(new java.awt.Color(153, 153, 153));
        seperatorButtons.setOpaque(true);

        org.jdesktop.layout.GroupLayout gl_panelButtons = new org.jdesktop.layout.GroupLayout(panelButtons);
        panelButtons.setLayout(gl_panelButtons);
        gl_panelButtons.setHorizontalGroup(
            gl_panelButtons.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelButtons.createSequentialGroup()
                .addContainerGap()
                .add(btnResetAll)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 273, Short.MAX_VALUE)
                .add(btnOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnCancel)
                .addContainerGap())
            .add(seperatorButtons, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
        );
        gl_panelButtons.setVerticalGroup(
            gl_panelButtons.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelButtons.createSequentialGroup()
                .add(seperatorButtons, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(gl_panelButtons.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnCancel)
                    .add(btnOk)
                    .add(btnResetAll))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(6, 6, 6)
                .add(propertiesTabs, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
                .addContainerGap())
            .add(panelButtons, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(propertiesTabs, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelButtons, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void chkShowEditorGridActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkShowEditorGridActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_chkShowEditorGridActionPerformed

private void chkShowChartGridActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkShowChartGridActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_chkShowChartGridActionPerformed

private void txtWSURLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtWSURLActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtWSURLActionPerformed

private void btnDefaultProxyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDefaultProxyActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_btnDefaultProxyActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton btnCancel;
    protected javax.swing.JRadioButton btnDefaultProxy;
    protected javax.swing.JButton btnFont;
    protected javax.swing.JRadioButton btnManualProxy;
    protected javax.swing.JRadioButton btnNoProxy;
    protected javax.swing.JButton btnOk;
    protected javax.swing.JButton btnReloadDictionary;
    protected javax.swing.JButton btnResetAll;
    protected javax.swing.JButton btnStartMeasuring;
    protected javax.swing.JButton btnWMSAdd;
    protected javax.swing.JButton btnWMSRemove;
    protected javax.swing.JComboBox cboAxisCursorColor;
    protected javax.swing.JComboBox cboBaud;
    protected javax.swing.JComboBox cboChartBGColor;
    protected javax.swing.JComboBox cboDScore;
    protected javax.swing.JComboBox cboDisplayUnits;
    protected javax.swing.JComboBox cboEditorBGColor;
    protected javax.swing.JComboBox cboGridColor;
    protected javax.swing.JComboBox cboHighlightColor;
    protected javax.swing.JComboBox cboPlatformType;
    protected javax.swing.JComboBox cboPort;
    protected javax.swing.JComboBox cboRValue;
    protected javax.swing.JComboBox cboTScore;
    protected javax.swing.JComboBox cboTextColor;
    protected javax.swing.JComboBox cboTrend;
    protected javax.swing.JComboBox cboWJ;
    protected javax.swing.JCheckBox chkHighlightSig;
    protected javax.swing.JCheckBox chkShowChartGrid;
    protected javax.swing.JCheckBox chkShowEditorGrid;
    protected javax.swing.JScrollPane jScrollPane2;
    protected javax.swing.JLabel lblAxisCursorColor;
    protected javax.swing.JLabel lblBaud;
    protected javax.swing.JLabel lblChartBGColor;
    protected javax.swing.JLabel lblDScore;
    protected javax.swing.JLabel lblDisplayUnits;
    protected javax.swing.JLabel lblEditorBGColor;
    protected javax.swing.JLabel lblFont;
    protected javax.swing.JLabel lblGridColor;
    protected javax.swing.JLabel lblHighlightColor;
    protected javax.swing.JLabel lblMinOverlap;
    protected javax.swing.JLabel lblMinOverlapDScore;
    protected javax.swing.JLabel lblPlatformType;
    protected javax.swing.JLabel lblPort;
    protected javax.swing.JLabel lblProxyPort;
    protected javax.swing.JLabel lblProxyPort1;
    protected javax.swing.JLabel lblProxyServer;
    protected javax.swing.JLabel lblProxyServer1;
    protected javax.swing.JLabel lblRValue;
    protected javax.swing.JLabel lblSMTPServer;
    protected javax.swing.JLabel lblShowChartGrid;
    protected javax.swing.JLabel lblShowEditorGrid;
    protected javax.swing.JLabel lblTScore;
    protected javax.swing.JLabel lblTextColor;
    protected javax.swing.JLabel lblTrend;
    protected javax.swing.JLabel lblWJ;
    protected javax.swing.JLabel lblWSURL;
    protected javax.swing.JPanel panelAppearance;
    protected javax.swing.JPanel panelButtons;
    protected javax.swing.JPanel panelCharts;
    protected javax.swing.JPanel panelEditor;
    protected javax.swing.JPanel panelEmail;
    protected javax.swing.JPanel panelGrfxWarning;
    protected javax.swing.JPanel panelHardware;
    protected javax.swing.JPanel panelMapping;
    protected javax.swing.JPanel panelNetworkConnections;
    protected javax.swing.JPanel panelNumberFormats;
    protected javax.swing.JPanel panelPlatform;
    protected javax.swing.JPanel panelProxy;
    protected javax.swing.JPanel panelSigScores;
    protected javax.swing.JPanel panelStatistics;
    protected javax.swing.JPanel panelTestComms;
    protected javax.swing.JPanel panelUI;
    protected javax.swing.JPanel panelWMS;
    protected javax.swing.JPanel panelWebservice;
    protected javax.swing.JTabbedPane propertiesTabs;
    protected javax.swing.ButtonGroup proxyButtonGroup;
    protected javax.swing.JScrollPane scrollPaneUIDefaults;
    protected javax.swing.JSeparator seperatorButtons;
    protected javax.swing.JSpinner spnMinOverlap;
    protected javax.swing.JSpinner spnMinOverlapDScore;
    protected javax.swing.JSpinner spnProxyPort;
    protected javax.swing.JSpinner spnProxyPort1;
    protected javax.swing.JTable tblWMS;
    protected javax.swing.JTextField txtProxyURL;
    protected javax.swing.JTextField txtProxyURL1;
    protected javax.swing.JTextField txtSMTPServer;
    protected javax.swing.JTextField txtWSURL;
    protected TestMeasurePanel measurePanel;
    protected JPanel panelMeasureHolder;
    private JLabel lblDatabits;
    private JLabel lblStopbits;
    private JComboBox cboDatabits;
    private JComboBox cboStopbits;
    private JLabel lblUnits;
    private JLabel lblLineFeed;
    private JComboBox cboUnits;
    private JComboBox cboLineFeed;
    private JTextPane txtComCheckLog;
    // End of variables declaration//GEN-END:variables
	
    
    @Override
	public void actionPerformed(ActionEvent e) {

    	
	}

    public void setSelectedTabIndex(int i)
    {
    	if(i>=propertiesTabs.getTabCount() || i <0)
    	{
    		System.out.println("Invalid tab index");
    	}
    	propertiesTabs.setSelectedIndex(i);
    }
}
