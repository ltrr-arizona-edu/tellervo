/*
 * NewSite.java
 *
 * Created on June 2, 2008, 3:38 PM
 */

package corinaguidesign;

/**
 *
 * @author  peterbrewer
 */
public class Tree extends javax.swing.JDialog {
    
    /** Creates new form NewSite */
    public Tree(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtTreeName = new javax.swing.JTextField();
        lblSite = new javax.swing.JLabel();
        lblTreeName = new javax.swing.JLabel();
        panelButtons = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        btnApply = new javax.swing.JButton();
        cboTaxon = new javax.swing.JComboBox();
        lblTaxon = new javax.swing.JLabel();
        lblLocation = new javax.swing.JLabel();
        panelLocation = new javax.swing.JPanel();
        lblLatitude = new javax.swing.JLabel();
        lblLongitude = new javax.swing.JLabel();
        lblPrecision = new javax.swing.JLabel();
        spnPrecision = new javax.swing.JSpinner();
        lblSpinUnits = new javax.swing.JLabel();
        txtLongitude = new javax.swing.JFormattedTextField();
        txtLatitude = new javax.swing.JFormattedTextField();
        btnGPSImport = new javax.swing.JButton();
        txtSiteSubsiteName = new javax.swing.JTextField();
        txtOriginalTaxon = new javax.swing.JTextField();
        lblOrigTaxon = new javax.swing.JLabel();
        chkIsLiveTree = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New Tree");

        txtTreeName.setText("Name of this tree");
        txtTreeName.setToolTipText("Name of this tree");

        lblSite.setText("Site / subsite:");

        lblTreeName.setText("Tree:");

        btnCancel.setText("Cancel");

        btnApply.setText("Apply");

        org.jdesktop.layout.GroupLayout panelButtonsLayout = new org.jdesktop.layout.GroupLayout(panelButtons);
        panelButtons.setLayout(panelButtonsLayout);
        panelButtonsLayout.setHorizontalGroup(
            panelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelButtonsLayout.createSequentialGroup()
                .addContainerGap(475, Short.MAX_VALUE)
                .add(btnApply)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnCancel)
                .add(5, 5, 5))
        );
        panelButtonsLayout.setVerticalGroup(
            panelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnApply)
                    .add(btnCancel))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cboTaxon.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pinus nigra", "Quercus robur" }));
        cboTaxon.setToolTipText("The most detailed taxonomic name for this tree.  This should be a species where possible.");

        lblTaxon.setText("Taxon:");

        lblLocation.setText("Location:");

        panelLocation.setAlignmentX(0.0F);
        panelLocation.setAlignmentY(0.0F);

        lblLatitude.setText("Latitude:");

        lblLongitude.setText("Longitude:");

        lblPrecision.setText("Precision:");

        spnPrecision.setToolTipText("Precision of locality information in meters");
        spnPrecision.setEditor(new javax.swing.JSpinner.NumberEditor(spnPrecision, ""));

        lblSpinUnits.setText("m");

        txtLongitude.setText("50.23");
        txtLongitude.setToolTipText("Longitude in decimal degrees");

        txtLatitude.setText("34.5");
        txtLatitude.setToolTipText("Latitude in decimal degrees");

        btnGPSImport.setText("GPS Import");
        btnGPSImport.setToolTipText("Import coordinates from GPX (GPS XML) file");

        org.jdesktop.layout.GroupLayout panelLocationLayout = new org.jdesktop.layout.GroupLayout(panelLocation);
        panelLocation.setLayout(panelLocationLayout);
        panelLocationLayout.setHorizontalGroup(
            panelLocationLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelLocationLayout.createSequentialGroup()
                .add(panelLocationLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(lblLatitude, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(txtLatitude, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelLocationLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtLongitude, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 86, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblLongitude))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelLocationLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(panelLocationLayout.createSequentialGroup()
                        .add(spnPrecision, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 77, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblSpinUnits))
                    .add(lblPrecision, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(btnGPSImport, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE))
        );
        panelLocationLayout.setVerticalGroup(
            panelLocationLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelLocationLayout.createSequentialGroup()
                .add(panelLocationLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblLatitude)
                    .add(lblLongitude)
                    .add(lblPrecision))
                .add(4, 4, 4)
                .add(panelLocationLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(txtLatitude, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtLongitude, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(spnPrecision, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblSpinUnits)
                    .add(btnGPSImport))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        txtSiteSubsiteName.setEditable(false);
        txtSiteSubsiteName.setText("ABC - Example site / Main");
        txtSiteSubsiteName.setToolTipText("Site and subsite name for this tree");

        txtOriginalTaxon.setText("Pinus nigra");
        txtOriginalTaxon.setToolTipText("The original identification of this tree");

        lblOrigTaxon.setText("Original identified as:");

        chkIsLiveTree.setText("is a live tree");
        chkIsLiveTree.setToolTipText("Is or was this tree live when sampled?");
        chkIsLiveTree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkIsLiveTreeActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelButtons, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(lblLocation, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblSite, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblTreeName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblTaxon, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblOrigTaxon, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(txtTreeName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                            .add(txtSiteSubsiteName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)))
                    .add(layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(cboTaxon, 0, 449, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(chkIsLiveTree)
                            .add(txtOriginalTaxon, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)))
                    .add(layout.createSequentialGroup()
                        .add(18, 18, 18)
                        .add(panelLocation, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblSite)
                    .add(txtSiteSubsiteName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblTreeName)
                    .add(txtTreeName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblTaxon)
                    .add(cboTaxon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblOrigTaxon)
                    .add(txtOriginalTaxon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkIsLiveTree)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 31, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblLocation)
                    .add(panelLocation, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelButtons, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chkIsLiveTreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkIsLiveTreeActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_chkIsLiveTreeActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Tree dialog = new Tree(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnGPSImport;
    private javax.swing.JComboBox cboTaxon;
    private javax.swing.JCheckBox chkIsLiveTree;
    private javax.swing.JLabel lblLatitude;
    private javax.swing.JLabel lblLocation;
    private javax.swing.JLabel lblLongitude;
    private javax.swing.JLabel lblOrigTaxon;
    private javax.swing.JLabel lblPrecision;
    private javax.swing.JLabel lblSite;
    private javax.swing.JLabel lblSpinUnits;
    private javax.swing.JLabel lblTaxon;
    private javax.swing.JLabel lblTreeName;
    private javax.swing.JPanel panelButtons;
    private javax.swing.JPanel panelLocation;
    private javax.swing.JSpinner spnPrecision;
    private javax.swing.JFormattedTextField txtLatitude;
    private javax.swing.JFormattedTextField txtLongitude;
    private javax.swing.JTextField txtOriginalTaxon;
    private javax.swing.JTextField txtSiteSubsiteName;
    private javax.swing.JTextField txtTreeName;
    // End of variables declaration//GEN-END:variables
    
}
