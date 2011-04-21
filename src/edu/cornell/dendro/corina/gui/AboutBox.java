package edu.cornell.dendro.corina.gui;


import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;

import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import edu.cornell.dendro.corina.Build;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;

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
        setIconImage(Builder.getApplicationIcon());

        addDetails();
        pack();

        // can't resize -- does this get rid of minimize/maximize buttons?
        setResizable(false);
        
        // center it
        setLocationRelativeTo(null);
        
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
        String strVersion = MessageFormat.format(I18n.getText("about.version"),
                new Object[] { Build.VERSION });
        String strTimestamp = MessageFormat.format(I18n.getText("about.timestamp"),
                new Object[] { Build.TIMESTAMP });
        String strRevision = MessageFormat.format(I18n.getText("about.revision"),
                    new Object[] { Build.REVISION_NUMBER }); 
        String strCopyright = MessageFormat.format(I18n.getText("about.copyright"),
        		new Object[] { Build.YEAR,
              Build.AUTHOR });
        
        String strDescription = I18n.getText("about.description");
        String strLicense = fileResourceToString("Licenses/CorinaLicense.txt");
        
        // Set text in dialog
        txtCopyright.setText(strCopyright);
        lblVersion.setText(strVersion);
        lblCompiledAt.setText(strTimestamp);   
        lblRevision.setText(strRevision);

        txtDescription.setText(strDescription);        
        txtLicense.setText(strLicense);
        txtLicense.setCaretPosition(0);
        
        tabbedPane.setTitleAt(0, I18n.getText("about"));
        tabbedPane.setTitleAt(1, I18n.getText("about.license"));
        
        
    }
    
    public String fileResourceToString(String filename){
    	
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
        
        lblRevision = new JLabel("Revision:");
        lblRevision.setFont(new Font("Lucida Grande", Font.PLAIN, 10));

        GroupLayout gl_panelSummary = new GroupLayout(panelSummary);
        gl_panelSummary.setVerticalGroup(
        	gl_panelSummary.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panelSummary.createSequentialGroup()
        			.addGap(24)
        			.addComponent(lblCorina)
        			.addGap(18)
        			.addComponent(lblVersion)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(lblCompiledAt, GroupLayout.PREFERRED_SIZE, 13, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(lblRevision)
        			.addContainerGap(10, Short.MAX_VALUE))
        );
        gl_panelSummary.setHorizontalGroup(
        	gl_panelSummary.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panelSummary.createSequentialGroup()
        			.addGap(51)
        			.addGroup(gl_panelSummary.createParallelGroup(Alignment.LEADING)
        				.addComponent(lblRevision)
        				.addComponent(lblCompiledAt)
        				.addComponent(lblVersion)
        				.addComponent(lblCorina))
        			.addContainerGap(164, Short.MAX_VALUE))
        );
        panelSummary.setLayout(gl_panelSummary);

        txtDescription.setBorder(null);
        txtDescription.setEditable(false);
        txtDescription.setFont(new java.awt.Font("Lucida Grande", 0, 11));
        txtDescription.setOpaque(false);

        txtCopyright.setBorder(null);
        txtCopyright.setEditable(false);
        txtCopyright.setFont(new java.awt.Font("Lucida Grande", 0, 8));
        txtCopyright.setOpaque(false);

        seperator.setBackground(new java.awt.Color(255, 255, 255));

        org.jdesktop.layout.GroupLayout gl_panelAbout = new org.jdesktop.layout.GroupLayout(panelAbout);
        panelAbout.setLayout(gl_panelAbout);
        gl_panelAbout.setHorizontalGroup(
            gl_panelAbout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, gl_panelAbout.createSequentialGroup()
                .addContainerGap()
                .add(gl_panelAbout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, seperator, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                    .add(gl_panelAbout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtCopyright, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, gl_panelAbout.createSequentialGroup()
                        .add(lblIcon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 128, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(panelSummary, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, txtDescription, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE))
                .addContainerGap())
        );
        gl_panelAbout.setVerticalGroup(
            gl_panelAbout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelAbout.createSequentialGroup()
                .addContainerGap()
                .add(gl_panelAbout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
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

        org.jdesktop.layout.GroupLayout gl_panelLicense = new org.jdesktop.layout.GroupLayout(panelLicense);
        panelLicense.setLayout(gl_panelLicense);
        gl_panelLicense.setHorizontalGroup(
            gl_panelLicense.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelLicense.createSequentialGroup()
                .addContainerGap()
                .add(scrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
                .addContainerGap())
        );
        gl_panelLicense.setVerticalGroup(
            gl_panelLicense.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gl_panelLicense.createSequentialGroup()
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
    protected javax.swing.JLabel lblRevision;
    protected javax.swing.JPanel panelAbout;
    protected javax.swing.JPanel panelLicense;
    protected javax.swing.JPanel panelSummary;
    protected javax.swing.JScrollPane scrollPane;
    protected javax.swing.JSeparator seperator;
    protected javax.swing.JTabbedPane tabbedPane;
    protected javax.swing.JTextPane txtCopyright;
    protected javax.swing.JTextPane txtDescription;
    protected javax.swing.JTextPane txtLicense;
}
