/*
 * HelpWiki.java
 *
 * Created on September 17, 2008, 2:08 PM
 */

package edu.cornell.dendro.corina.gui;

import org.mozilla.browser.MozillaPanel;
import org.mozilla.browser.IMozillaWindow.VisibilityMode;

/**
 *
 * @author  peterbrewer
 */
public class HelpWiki extends javax.swing.JDialog {
    
    /** Creates new form HelpWiki */
    public HelpWiki(java.awt.Frame parent, boolean modal, String page) {
        super(parent, modal);
        
        if(page=="") page = "http://dendro.cornell.edu/corina-manual/UserGuideContents";
        
        initComponents(page);

    }
    
    private void initComponents(String page) {

        mozillaPanel = new org.mozilla.browser.MozillaPanel();
        mozillaPanel.load(page);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        org.jdesktop.layout.GroupLayout mozillaPanelLayout = new org.jdesktop.layout.GroupLayout(mozillaPanel);
        mozillaPanel.setLayout(mozillaPanelLayout);
        mozillaPanelLayout.setHorizontalGroup(
            mozillaPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 211, Short.MAX_VALUE)
        );
        mozillaPanelLayout.setVerticalGroup(
            mozillaPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 214, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(mozillaPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(mozillaPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

    
        pack();

    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * @param args the command line arguments
     */
    public static void main() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                HelpWiki dialog = new HelpWiki(new javax.swing.JFrame(), true, "");
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    //private java.awt.Panel mozillaPanel;
	private MozillaPanel mozillaPanel;
    // End of variables declaration//GEN-END:variables
    
}
