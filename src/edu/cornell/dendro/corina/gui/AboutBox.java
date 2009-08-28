package edu.cornell.dendro.corina.gui;


import java.awt.Component;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.SwingConstants;

import edu.cornell.dendro.corina.Build;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.DefaultResourceBundle;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.Center;

/*
 * AboutBox2.java
 *
 * Created on October 16, 2008, 9:50 AM
 */



/**
 *
 * @author  peterbrewer
 */
public class AboutBox extends javax.swing.JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static AboutBox instance = null;

	
    /** Creates new form AboutBox */
    public AboutBox() {  	
    	
        initComponents();
        addDetails();
        pack();

        // can't resize -- does this get rid of minimize/maximize buttons?
        setResizable(false);
        
        // center it
        Center.center(this);
        
    }
    
    public static synchronized AboutBox getInstance() {
        if (instance == null) {
          instance = new AboutBox();
        } 
        return instance;
      }
    

    
    private void addDetails(){
    
    	// Set icon
	    Icon icon = Builder.getIcon("corina-application.png", 128);
	    if (icon != null) {
	    	  lblIcon.setIcon(icon);
	    }

        // Build strings
        String strVersion = MessageFormat.format(I18n.getText("version"),
                new Object[] { Build.VERSION });
        String strTimestamp = MessageFormat.format(I18n.getText("timestamp"),
                new Object[] { Build.TIMESTAMP });       
        String strCopyright = MessageFormat.format(I18n.getText("copyright"),
        		new Object[] { Build.YEAR,
              Build.AUTHOR });
        String strDescription = I18n.getText("description");
        String strLicense = fileResourceToString("Licenses/CorinaLicense.txt");
        
        // Set text in dialog
        txtCopyright.setText(strCopyright);
        lblVersion.setText(strVersion);
        lblCompiledAt.setText(strTimestamp);     
        txtDescription.setText(strDescription);        
        txtLicense.setText(strLicense);
        txtLicense.setCaretPosition(0);
    }
    
    public String fileResourceToString(String filename){
    	
        File file = new File(filename);
        StringBuffer contents = new StringBuffer();
        BufferedReader reader = null;
        InputStream is = getClass().getClassLoader().getResourceAsStream("edu/cornell/dendro/corina_resources/Licenses/CorinaLicense.txt"); 
        
        try {
        	reader = new BufferedReader(new InputStreamReader(is));
        	String text = null;
        	while((text = reader.readLine()) != null)
        	{
        		contents.append(text)
        		.append(System.getProperty("line.separator"));
         	}
      	
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        return contents.toString();
    	
    }
    
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        panelAbout = new javax.swing.JPanel();
        panelSummary = new javax.swing.JPanel();
        lblCorina = new javax.swing.JLabel();
        lblVersion = new javax.swing.JLabel();
        lblCompiledAt = new javax.swing.JLabel();
        txtDescription = new javax.swing.JTextPane();
        txtCopyright = new javax.swing.JTextPane();
        seperator = new javax.swing.JSeparator();
        lblIcon = new javax.swing.JLabel();
        panelLicense = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        txtLicense = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblCorina.setFont(new java.awt.Font("Lucida Grande", 1, 18));
        lblCorina.setText("Corina");

        lblVersion.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        lblVersion.setText("Version : 2.01");

        lblCompiledAt.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        lblCompiledAt.setText("Compiled at: 1231231");

        org.jdesktop.layout.GroupLayout panelSummaryLayout = new org.jdesktop.layout.GroupLayout(panelSummary);
        panelSummary.setLayout(panelSummaryLayout);
        panelSummaryLayout.setHorizontalGroup(
            panelSummaryLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelSummaryLayout.createSequentialGroup()
                .add(51, 51, 51)
                .add(panelSummaryLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblCompiledAt)
                    .add(lblVersion)
                    .add(lblCorina))
                .addContainerGap(88, Short.MAX_VALUE))
        );
        panelSummaryLayout.setVerticalGroup(
            panelSummaryLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelSummaryLayout.createSequentialGroup()
                .add(24, 24, 24)
                .add(lblCorina)
                .add(18, 18, 18)
                .add(lblVersion)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lblCompiledAt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        txtDescription.setBorder(null);
        txtDescription.setEditable(false);
        txtDescription.setFont(new java.awt.Font("Lucida Grande", 0, 11));
        txtDescription.setOpaque(false);

        txtCopyright.setBorder(null);
        txtCopyright.setEditable(false);
        txtCopyright.setFont(new java.awt.Font("Lucida Grande", 0, 8));
        txtCopyright.setOpaque(false);

        seperator.setBackground(new java.awt.Color(255, 255, 255));

        org.jdesktop.layout.GroupLayout panelAboutLayout = new org.jdesktop.layout.GroupLayout(panelAbout);
        panelAbout.setLayout(panelAboutLayout);
        panelAboutLayout.setHorizontalGroup(
            panelAboutLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelAboutLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelAboutLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, seperator, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                    .add(panelAboutLayout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtCopyright, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, panelAboutLayout.createSequentialGroup()
                        .add(lblIcon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 128, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(panelSummary, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, txtDescription, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelAboutLayout.setVerticalGroup(
            panelAboutLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelAboutLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelAboutLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(panelSummary, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblIcon, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE))
                .add(43, 43, 43)
                .add(txtDescription, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(seperator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(2, 2, 2)
                .add(txtCopyright, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(82, Short.MAX_VALUE))
        );

        tabbedPane.addTab("About", panelAbout);

        scrollPane.setFont(new java.awt.Font("Courier", 0, 9));

        txtLicense.setEditable(false);
        txtLicense.setFont(new java.awt.Font("Courier", 0, 9));
        txtLicense.setDragEnabled(false);
        txtLicense.setFocusable(false);
        txtLicense.setOpaque(false);
        scrollPane.setViewportView(txtLicense);

        org.jdesktop.layout.GroupLayout panelLicenseLayout = new org.jdesktop.layout.GroupLayout(panelLicense);
        panelLicense.setLayout(panelLicenseLayout);
        panelLicenseLayout.setHorizontalGroup(
            panelLicenseLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelLicenseLayout.createSequentialGroup()
                .addContainerGap()
                .add(scrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelLicenseLayout.setVerticalGroup(
            panelLicenseLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelLicenseLayout.createSequentialGroup()
                .addContainerGap()
                .add(scrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab("License", panelLicense);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AboutBox dialog = new AboutBox();
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JLabel lblCompiledAt;
    protected javax.swing.JLabel lblCorina;
    protected javax.swing.JLabel lblIcon;
    protected javax.swing.JLabel lblVersion;
    protected javax.swing.JPanel panelAbout;
    protected javax.swing.JPanel panelLicense;
    protected javax.swing.JPanel panelSummary;
    protected javax.swing.JScrollPane scrollPane;
    protected javax.swing.JSeparator seperator;
    protected javax.swing.JTabbedPane tabbedPane;
    protected javax.swing.JTextPane txtCopyright;
    protected javax.swing.JTextPane txtDescription;
    protected javax.swing.JTextPane txtLicense;
    // End of variables declaration//GEN-END:variables
    
}
