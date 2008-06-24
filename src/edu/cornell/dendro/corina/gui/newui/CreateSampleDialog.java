/**
 * 
 */
package edu.cornell.dendro.corina.gui.newui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;

import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.util.Center;

/**
 * @author lucasm
 *
 */
public class CreateSampleDialog extends JDialog {
	public final static int OPERATION_NEW = 1;
	public final static int OPERATION_SAVEAS = 2;
	
	private int operation = OPERATION_NEW;
	private Sample templateSample;
	private HierarchyPanel panelHierarchyContent;
	private MeasurementPanel panelMeasurementContent;
	
	/**
	 * @param owner
	 * @param title
	 * @param modal
	 */
	public CreateSampleDialog(Frame owner, boolean modal, Sample templateSample) {
		super(owner, modal);
		this.templateSample = templateSample;
		setupDialog();
	}

	/**
	 * @param owner
	 * @param title
	 * @param modal
	 */
	public CreateSampleDialog(Dialog owner, boolean modal, Sample templateSample) {
		super(owner, modal);
		this.templateSample = templateSample;
		setupDialog();
	}
	
	private void setupDialog() {
		switch(operation) {
		case OPERATION_NEW:
			setTitle("Create a new sample...");
			break;
			
		case OPERATION_SAVEAS:
			setTitle("Save as...");
			break;
		}

        panelHierarchyHolder = new javax.swing.JPanel();
        panelMeasurementHolder = new javax.swing.JPanel();
        panelButtons = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        seperatorButtons = new javax.swing.JSeparator();
        lblCodeName = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                
        panelHierarchyHolder.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Hierarchy Details"));

        org.jdesktop.layout.GroupLayout panelHierarchyHolderLayout = new org.jdesktop.layout.GroupLayout(panelHierarchyHolder);
        panelHierarchyHolder.setLayout(panelHierarchyHolderLayout);
        panelHierarchyHolderLayout.setHorizontalGroup(
            panelHierarchyHolderLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 336, Short.MAX_VALUE)
        );
        panelHierarchyHolderLayout.setVerticalGroup(
            panelHierarchyHolderLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 226, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout panelMeasurementHolderLayout = new org.jdesktop.layout.GroupLayout(panelMeasurementHolder);
        panelMeasurementHolder.setLayout(panelMeasurementHolderLayout);
        panelMeasurementHolderLayout.setHorizontalGroup(
            panelMeasurementHolderLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 344, Short.MAX_VALUE)
        );
        panelMeasurementHolderLayout.setVerticalGroup(
            panelMeasurementHolderLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 254, Short.MAX_VALUE)
        );

        btnOk.setText("OK");

        seperatorButtons.setBackground(new java.awt.Color(153, 153, 153));
        seperatorButtons.setOpaque(true);

        lblCodeName.setText("C-ADN-1-1-1");
        lblCodeName.setToolTipText("Laboratory code of your new sample");

        org.jdesktop.layout.GroupLayout panelButtonsLayout = new org.jdesktop.layout.GroupLayout(panelButtons);
        panelButtons.setLayout(panelButtonsLayout);
        panelButtonsLayout.setHorizontalGroup(
            panelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, seperatorButtons, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .add(lblCodeName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnOk)
                .addContainerGap())
        );
        panelButtonsLayout.setVerticalGroup(
            panelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelButtonsLayout.createSequentialGroup()
                .add(seperatorButtons, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnOk)
                    .add(lblCodeName))
                .add(17, 17, 17))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 730, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(panelHierarchyHolder, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(18, 18, 18)
                .add(panelMeasurementHolder, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .add(panelButtons, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 308, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panelMeasurementHolder, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(panelHierarchyHolder, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelButtons, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        
        panelHierarchyContent = new HierarchyPanel(panelHierarchyHolder);
        //panelMeasurementContent = new MeasurementEditorPanel();
        		
		//JPanel holder = new JPanel();
		panelHierarchyHolder.add(panelHierarchyContent, BorderLayout.NORTH);
		panelMeasurementHolder.add(panelMeasurementContent, BorderLayout.SOUTH);
		        
		panelHierarchyContent.initialize(templateSample);
		
		pack();
		Center.center(this);
		
		panelHierarchyContent.setVisible(true);
		panelMeasurementContent.setVisible(true);
		
		panelHierarchyHolder.setVisible(true);
		panelMeasurementHolder.setVisible(true);

	
	}
	
    private javax.swing.JButton btnOk;
    private javax.swing.JLabel lblCodeName;
    private javax.swing.JPanel panelButtons;
    private javax.swing.JPanel panelHierarchyHolder;
    private javax.swing.JPanel panelMeasurementHolder;
    private javax.swing.JSeparator seperatorButtons;
	
}
