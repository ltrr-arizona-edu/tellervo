/*
 * PanelImportWizard3.java
 *
 * Created on June 10, 2008, 2:36 PM
 */

package edu.cornell.dendro.corina.gui.newui;

/**
 *
 * @author  peterbrewer
 */
public class PanelImportWizard3 extends javax.swing.JPanel {
    
    /** Creates new form panelImportWizard3 */
    public PanelImportWizard3() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblIsALiveTree = new javax.swing.JLabel();
        lblTreeName = new javax.swing.JLabel();
        lblTaxon = new javax.swing.JLabel();
        lblOrigTaxon = new javax.swing.JLabel();
        lblLocation = new javax.swing.JLabel();
        cboIsALiveTree = new javax.swing.JComboBox();
        cboTaxon = new javax.swing.JComboBox();
        txtOriginalTaxon = new javax.swing.JTextField();
        lblTreePrefix = new javax.swing.JLabel();
        panelLocation = new javax.swing.JPanel();
        lblLatitude = new javax.swing.JLabel();
        lblLongitude = new javax.swing.JLabel();
        lblPrecision = new javax.swing.JLabel();
        spnPrecision = new javax.swing.JSpinner();
        lblSpinUnits = new javax.swing.JLabel();
        txtLongitude = new javax.swing.JFormattedTextField();
        txtLatitude = new javax.swing.JFormattedTextField();
        btnGPSImport = new javax.swing.JButton();
        cboTreeName = new javax.swing.JComboBox();
        radNewTree = new javax.swing.JRadioButton();
        radExistingTree = new javax.swing.JRadioButton();

        lblIsALiveTree.setLabelFor(cboIsALiveTree);
        lblIsALiveTree.setText("Is a live tree:");

        lblTreeName.setLabelFor(cboTreeName);
        lblTreeName.setText("Tree:");

        lblTaxon.setLabelFor(cboTaxon);
        lblTaxon.setText("Taxon:");

        lblOrigTaxon.setLabelFor(txtOriginalTaxon);
        lblOrigTaxon.setText("Originally identified as:");

        lblLocation.setText("Location:");

        cboIsALiveTree.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Unspecified", "Yes", "No" }));

        cboTaxon.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pinus nigra", "Quercus robur" }));
        cboTaxon.setToolTipText("The most detailed taxonomic name for this tree.  This should be a species where possible.");

        txtOriginalTaxon.setText("Pinus nigra");
        txtOriginalTaxon.setToolTipText("The original identification of this tree");

        lblTreePrefix.setText("C-ABC-");
        lblTreePrefix.setToolTipText("Laboratory code prefix for tree");

        panelLocation.setAlignmentX(0.0F);
        panelLocation.setAlignmentY(0.0F);

        lblLatitude.setLabelFor(txtLatitude);
        lblLatitude.setText("Latitude:");

        lblLongitude.setLabelFor(txtLongitude);
        lblLongitude.setText("Longitude:");

        lblPrecision.setLabelFor(spnPrecision);
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
                .add(btnGPSImport, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 103, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
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
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cboTreeName.setEditable(true);
        cboTreeName.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        radNewTree.setText("New ");

        radExistingTree.setText("Existing");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblIsALiveTree)
                    .add(lblTreeName)
                    .add(lblTaxon)
                    .add(lblOrigTaxon)
                    .add(lblLocation, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 154, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(radNewTree)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(radExistingTree))
                    .add(cboIsALiveTree, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cboTaxon, 0, 407, Short.MAX_VALUE)
                    .add(txtOriginalTaxon, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(lblTreePrefix)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cboTreeName, 0, 352, Short.MAX_VALUE))
                    .add(panelLocation, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(radNewTree)
                    .add(radExistingTree))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblTreeName)
                    .add(lblTreePrefix)
                    .add(cboTreeName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblTaxon)
                    .add(cboTaxon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblOrigTaxon)
                    .add(txtOriginalTaxon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblIsALiveTree)
                    .add(cboIsALiveTree, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panelLocation, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblLocation))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGPSImport;
    private javax.swing.JComboBox cboIsALiveTree;
    private javax.swing.JComboBox cboTaxon;
    private javax.swing.JComboBox cboTreeName;
    private javax.swing.JLabel lblIsALiveTree;
    private javax.swing.JLabel lblLatitude;
    private javax.swing.JLabel lblLocation;
    private javax.swing.JLabel lblLongitude;
    private javax.swing.JLabel lblOrigTaxon;
    private javax.swing.JLabel lblPrecision;
    private javax.swing.JLabel lblSpinUnits;
    private javax.swing.JLabel lblTaxon;
    private javax.swing.JLabel lblTreeName;
    private javax.swing.JLabel lblTreePrefix;
    private javax.swing.JPanel panelLocation;
    private javax.swing.JRadioButton radExistingTree;
    private javax.swing.JRadioButton radNewTree;
    private javax.swing.JSpinner spnPrecision;
    private javax.swing.JFormattedTextField txtLatitude;
    private javax.swing.JFormattedTextField txtLongitude;
    private javax.swing.JTextField txtOriginalTaxon;
    // End of variables declaration//GEN-END:variables
    
}