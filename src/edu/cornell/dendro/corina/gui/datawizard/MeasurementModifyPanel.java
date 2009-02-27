package edu.cornell.dendro.corina.gui.datawizard;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.dictionary.User;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleSummary;
import edu.cornell.dendro.corina.tridas.TridasEntityBase;
import edu.cornell.dendro.corina.util.LegacySampleExtractor;

/**
 *
 * @author  peterbrewer
 */
public class MeasurementModifyPanel extends BaseEditorPanel<TridasEntityBase> {
	private Sample s;
    private boolean validForm;

    /** Creates new form panelImportWizard4 */
    public MeasurementModifyPanel(Sample s) {
    	super();
    	
        initComponents();
        setupUserLists();
        
        // hide the reading count for now
        lblReadingCount.setVisible(false);

        this.s = s;
        
        initialize();
        populateSampleData();
        
        // tell our parent about our status...
        validateForm();
    }
    
    private void initialize() {
    	cboMeasuredBy.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED)
					validateForm();
			}
    	});

    	cboOwnedBy.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED)
					validateForm();
			}
    	});
    	
    	setFieldValidateButtons(txtMeasurementName);
    	
    	setSelectAllOnFocus(txtMeasurementName);
    	setSelectAllOnFocus(txtStartYear);
    	setSelectAllOnFocus(txtDatingErrorNegative);
    	setSelectAllOnFocus(txtDatingErrorPositive);
    	
    	this.setCapsNoWhitespace(txtMeasurementName);
    }
    
	public void populateSampleData() {
		Object o;
		String v;

		if((o = s.getMeta("comments")) != null)
			txtDescription.setText(o.toString());

		if((o = s.getMeta("name")) != null)
			txtMeasurementName.setText(o.toString());
				
		if((o = s.getMeta("dating")) != null) {
			if((v = LegacySampleExtractor.getLegacyMapping("dating", o.toString())) != null) {
				selectEqualValueIn(cboDatingType, v);
			}
		}
		
		cboMeasuredBy.setSelectedItem(s.getMeta("author"));
		cboOwnedBy.setSelectedItem(s.getMeta("owner"));
				
		// update reading count (hey, it's useful here!)
		lblReadingCount.setText(s.getData().size() + " readings");
		lblReadingCount.setVisible(true);
		
		txtStartYear.setText(s.getRange().getStart().toString());
		txtStartYear.setEnabled(false);
		
		// PREFIX!
		if((o = s.getMeta("::summary")) != null) {
			SampleSummary ss = (SampleSummary) o;
			lblMeasurementPrefix.setText(ss.getLabPrefix());
		}
		else
			lblMeasurementPrefix.setText("[LAB PREFIX]-");
	}
	
	// convenience method for setDefaultsFrom
	private void selectEqualValueIn(JComboBox cbo, String value) {
		if(value != null) {
			int len = cbo.getModel().getSize();

			for(int i = 0; i < len; i++) {
				Object o = cbo.getModel().getElementAt(i);
				
				// match it?
				if(o.toString().equalsIgnoreCase(value)) {
					cbo.setSelectedIndex(i);
					break;
				}
			}
		}
	}
        
    protected void validateForm() {
    	boolean nameOk;
    	boolean measuredByOk;
    	boolean ownedByOk;
    	
    	nameOk = txtMeasurementName.getText().length() > 0;
    	measuredByOk = cboMeasuredBy.getSelectedItem() instanceof User;
    	ownedByOk = cboOwnedBy.getSelectedItem() instanceof User;
    	
    	colorField(txtMeasurementName, nameOk);
    	colorField(cboMeasuredBy, measuredByOk);
    	colorField(cboOwnedBy, ownedByOk);
    	
    	setFormValidated(nameOk && measuredByOk && ownedByOk);
    }

    private int numModifications;
    private void setMetaMod(String key, Object value) {
    	Object oldValue = s.getMeta(key);
    	
    	if(oldValue == null || !oldValue.equals(value)) {
    		s.setMeta(key, value);
    		numModifications++;
    	}
    }
    
    public void commit() {
    	// User selected OK!: copy dialog info into our sample!
    	
    	numModifications = 0;
    	
    	setMetaMod("author", cboMeasuredBy.getSelectedItem());
    	setMetaMod("owner", cboOwnedBy.getSelectedItem());
    	
    	// name is important, title is not
    	setMetaMod("name", txtMeasurementName.getText());

    	// but update the title anyway, so we get some visual feedback
    	SampleSummary ss;
    	if((ss = (SampleSummary) s.getMeta("::summary")) != null) {
    		setMetaMod("title", ss.getLabPrefix() + txtMeasurementName.getText());
    	}
    	  	
    	String datingType = cboDatingType.getSelectedItem().toString();
    	if(datingType.equalsIgnoreCase("absolute"))
    		setMetaMod("dating", "A");
    	else if(datingType.equalsIgnoreCase("relative"))
    		setMetaMod("dating", "R");
    	else if(datingType.equalsIgnoreCase("Absolute with uncertainty"))
    		setMetaMod("dating", "AU");
    	
    	setMetaMod("comments", txtDescription.getText());

    	
    	// let our sample know we changed it
    	if(numModifications > 0) {
    		s.setModified();
    		s.fireSampleMetadataChanged();
    	}
    	
    	succeeded = true;
    }    
    
    private void setupUserLists() {
    	List<User> users = (List<User>) App.dictionary.getDictionary("Users");
    	
    	Object[] srcArray = users.toArray();
    	Object[] dstArray = new Object[srcArray.length + 1];
    	
    	dstArray[0] = "-- Choose a User --";
    	System.arraycopy(srcArray, 0, dstArray, 1, srcArray.length);
    	
    	cboMeasuredBy.setModel(new DefaultComboBoxModel(dstArray));
    	cboOwnedBy.setModel(new DefaultComboBoxModel(dstArray));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRadius = new javax.swing.JPanel();
        lblRadiusPrefix = new javax.swing.JLabel();
        cboRadiusName = new javax.swing.JComboBox();
        radNewRadius = new javax.swing.JRadioButton();
        RadExistingRadius = new javax.swing.JRadioButton();
        panelMeasurement = new javax.swing.JPanel();
        lblMeasurementName = new javax.swing.JLabel();
        lblMeasurementPrefix = new javax.swing.JLabel();
        txtMeasurementName = new javax.swing.JTextField();
        chkIsPublished = new javax.swing.JCheckBox();
        lblMeasuredBy = new javax.swing.JLabel();
        cboMeasuredBy = new javax.swing.JComboBox();
        lblDescription = new javax.swing.JLabel();
        lblOwnedBy = new javax.swing.JLabel();
        cboOwnedBy = new javax.swing.JComboBox();
        lblDatingType = new javax.swing.JLabel();
        cboDatingType = new javax.swing.JComboBox();
        lblDatingErrorPositive = new javax.swing.JLabel();
        txtDatingErrorPositive = new javax.swing.JTextField();
        lblDatingErrorNegative = new javax.swing.JLabel();
        txtDatingErrorNegative = new javax.swing.JTextField();
        lblStartYear = new javax.swing.JLabel();
        txtStartYear = new javax.swing.JTextField();
        lblReadingCount = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextPane();

        panelRadius.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Radius"));

        lblRadiusPrefix.setText("C-ABC-1-1-");

        cboRadiusName.setEditable(true);
        cboRadiusName.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "A" }));

        radNewRadius.setText("New");

        RadExistingRadius.setText("Existing");

        org.jdesktop.layout.GroupLayout panelRadiusLayout = new org.jdesktop.layout.GroupLayout(panelRadius);
        panelRadius.setLayout(panelRadiusLayout);
        panelRadiusLayout.setHorizontalGroup(
            panelRadiusLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelRadiusLayout.createSequentialGroup()
                .addContainerGap()
                .add(lblRadiusPrefix)
                .add(5, 5, 5)
                .add(panelRadiusLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(panelRadiusLayout.createSequentialGroup()
                        .add(radNewRadius)
                        .add(18, 18, 18)
                        .add(RadExistingRadius, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE))
                    .add(cboRadiusName, 0, 449, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelRadiusLayout.setVerticalGroup(
            panelRadiusLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelRadiusLayout.createSequentialGroup()
                .add(panelRadiusLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(radNewRadius)
                    .add(RadExistingRadius))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelRadiusLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblRadiusPrefix)
                    .add(cboRadiusName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        //panelMeasurement.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Measurement"));

        lblMeasurementName.setLabelFor(txtMeasurementName);
        lblMeasurementName.setText("Name:");

        lblMeasurementPrefix.setLabelFor(txtMeasurementName);
        lblMeasurementPrefix.setText("C-ABC-1-1-A-");

        chkIsPublished.setText("Publish");

        lblMeasuredBy.setLabelFor(cboMeasuredBy);
        lblMeasuredBy.setText("Measured by:");

        cboMeasuredBy.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-- Select --", "Unknown", "Peter Brewer", "Carol Griggs", "Peter Kuniholm" }));

        lblDescription.setLabelFor(txtDescription);
        lblDescription.setText("Description:");

        lblOwnedBy.setLabelFor(cboOwnedBy);
        lblOwnedBy.setText("Owner:");

        cboOwnedBy.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-- Select --", "Unknown", "Peter Brewer", "Carol Griggs", "Peter Kuniholm" }));

        lblDatingType.setLabelFor(cboDatingType);
        lblDatingType.setText("Dating type:");

        cboDatingType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Absolute", "Relative", "Absolute with uncertainty" }));

        lblDatingErrorPositive.setLabelFor(txtDatingErrorPositive);
        lblDatingErrorPositive.setText("+");
        lblDatingErrorPositive.setEnabled(false);

        txtDatingErrorPositive.setText("0");
        txtDatingErrorPositive.setEnabled(false);

        lblDatingErrorNegative.setLabelFor(txtDatingErrorNegative);
        lblDatingErrorNegative.setText("-");
        lblDatingErrorNegative.setEnabled(false);

        txtDatingErrorNegative.setText("0");
        txtDatingErrorNegative.setEnabled(false);

        lblStartYear.setLabelFor(txtStartYear);
        lblStartYear.setText("Start year:");

        txtStartYear.setText("2006");

        lblReadingCount.setText("154 readings");

        jScrollPane1.setViewportView(txtDescription);

        org.jdesktop.layout.GroupLayout panelMeasurementLayout = new org.jdesktop.layout.GroupLayout(panelMeasurement);
        panelMeasurement.setLayout(panelMeasurementLayout);
        panelMeasurementLayout.setHorizontalGroup(
            panelMeasurementLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelMeasurementLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelMeasurementLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panelMeasurementLayout.createSequentialGroup()
                        .add(panelMeasurementLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(lblMeasurementName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(lblDatingType, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(lblOwnedBy, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(lblMeasuredBy, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE))
                        .add(panelMeasurementLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(panelMeasurementLayout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(panelMeasurementLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(cboOwnedBy, 0, 404, Short.MAX_VALUE)
                                    .add(cboMeasuredBy, 0, 404, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, panelMeasurementLayout.createSequentialGroup()
                                        .add(panelMeasurementLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                            .add(org.jdesktop.layout.GroupLayout.LEADING, txtStartYear, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                                            .add(cboDatingType, 0, 279, Short.MAX_VALUE))
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(panelMeasurementLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(panelMeasurementLayout.createSequentialGroup()
                                                .add(lblDatingErrorPositive, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(txtDatingErrorPositive, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 34, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(lblDatingErrorNegative)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(txtDatingErrorNegative, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                            .add(lblReadingCount)))))
                            .add(panelMeasurementLayout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblMeasurementPrefix)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(txtMeasurementName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(chkIsPublished, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                    .add(panelMeasurementLayout.createSequentialGroup()
                        .add(panelMeasurementLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, lblStartYear, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, lblDescription, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelMeasurementLayout.setVerticalGroup(
            panelMeasurementLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelMeasurementLayout.createSequentialGroup()
                .add(panelMeasurementLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkIsPublished)
                    .add(txtMeasurementName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblMeasurementPrefix)
                    .add(lblMeasurementName))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelMeasurementLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cboMeasuredBy, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblMeasuredBy))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(panelMeasurementLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cboOwnedBy, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblOwnedBy))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(panelMeasurementLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblDatingType)
                    .add(txtDatingErrorNegative, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblDatingErrorNegative)
                    .add(txtDatingErrorPositive, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblDatingErrorPositive)
                    .add(cboDatingType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(panelMeasurementLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(txtStartYear, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblReadingCount)
                    .add(lblStartYear))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(panelMeasurementLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblDescription)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, panelMeasurement, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    )//.add(org.jdesktop.layout.GroupLayout.TRAILING, panelRadius, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                //.add(panelRadius, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                //.addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(panelMeasurement, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton RadExistingRadius;
    private javax.swing.JComboBox cboDatingType;
    private javax.swing.JComboBox cboMeasuredBy;
    private javax.swing.JComboBox cboOwnedBy;
    private javax.swing.JComboBox cboRadiusName;
    private javax.swing.JCheckBox chkIsPublished;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDatingErrorNegative;
    private javax.swing.JLabel lblDatingErrorPositive;
    private javax.swing.JLabel lblDatingType;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblMeasuredBy;
    private javax.swing.JLabel lblMeasurementName;
    private javax.swing.JLabel lblMeasurementPrefix;
    private javax.swing.JLabel lblOwnedBy;
    private javax.swing.JLabel lblRadiusPrefix;
    private javax.swing.JLabel lblReadingCount;
    private javax.swing.JLabel lblStartYear;
    private javax.swing.JPanel panelMeasurement;
    private javax.swing.JPanel panelRadius;
    private javax.swing.JRadioButton radNewRadius;
    private javax.swing.JTextField txtDatingErrorNegative;
    private javax.swing.JTextField txtDatingErrorPositive;
    private javax.swing.JTextPane txtDescription;
    private javax.swing.JTextField txtMeasurementName;
    private javax.swing.JTextField txtStartYear;
    // End of variables declaration//GEN-END:variables
    
}
