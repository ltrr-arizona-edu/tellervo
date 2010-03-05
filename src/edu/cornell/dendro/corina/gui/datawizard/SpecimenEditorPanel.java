/*
 * Specimen.java
 *
 * Created on June 9, 2008, 11:44 AM
 */

package edu.cornell.dendro.corina.gui.datawizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.InputVerifier;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import javax.swing.text.DateFormatter;
import javax.swing.text.MaskFormatter;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.dictionary.BasicDictionaryElement;
import edu.cornell.dendro.corina.dictionary.Pith;
import edu.cornell.dendro.corina.dictionary.SpecimenContinuity;
import edu.cornell.dendro.corina.dictionary.SpecimenType;
import edu.cornell.dendro.corina.dictionary.Taxon;
import edu.cornell.dendro.corina.dictionary.TerminalRing;
import edu.cornell.dendro.corina.tridas.TridasElement;
import edu.cornell.dendro.corina.tridas.TridasSample;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.webdbi.IntermediateResource;

/**
 *
 * @author  peterbrewer
 */
public class SpecimenEditorPanel extends BaseEditorPanel<TridasSample> {
    
    /** Creates new form Specimen */
    public SpecimenEditorPanel() {
        initComponents();
          
        initialize();
        validateForm();
		//setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 50));
    }
    
    private DateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    private void initialize() {
    	// set up the mapping from our comboboxes to our dictionaries
    	mapToDictionaries();
    	
    	// quick initial setup: leave things blank
    	txtCollectionDate.setText("");
    	txtSpecimenName.setText("Specimen Code");
    	// muck with labels
        lblCollectionDate.setText(lblCollectionDate.getText() + " (YYYY-MM-DD)");
        
        // select on focus!
     	setSelectAllOnFocus(txtCollectionDate);  
     	setSelectAllOnFocus(txtSpecimenName);
     	
     	// and of course, the specimen name
     	setCapsNoWhitespace(txtSpecimenName);

     	// the fields we validate before ok can be pressed
    	setFieldValidateButtons(txtSpecimenName);
     	
    	// make our spinners have an indeterminate value
    	setSpinnerIndeterminate(spnSapwoodCount);
    	setSpinnerIndeterminate(spnUnmeasPost);
    	setSpinnerIndeterminate(spnUnmeasPre);
    	    	
     	// force a check of date when we leave the txtCollectionDate field
    	final BasePanel parentGlue = this;
    	txtCollectionDate.setInputVerifier(new InputVerifier() {
    		public boolean verify(JComponent input) {
    			if(input != txtCollectionDate)
    				return true;
    			
    			try {
    				String value = txtCollectionDate.getText();
    				
    				// null date is ok
    				if(value.equals(""))
    					return true;
    				
    				isoDateFormat.parse(value);    				
    				
    				// success!
    				return true;
    			} catch(ParseException pe) {
    				int action = JOptionPane.showConfirmDialog(parentGlue, "The date you entered was invalid.\n" + 
    						"Would you like to leave it blank?", 
    						"Invalid Entry", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
    				
    				if(action == JOptionPane.YES_OPTION)
    					txtCollectionDate.setText("");

    				return false;
    			}
    		}
    	});
    
    	initButtons();
    }

	public void populate(String parentPrefix) {
    	lblNamePrefix.setText(parentPrefix);
	}
    
	public void setDefaultsFrom(TridasSample spec) {
		String v;
		Integer iv;
		Boolean b;
		
		// populate name
		v = spec.toString();
		if(!v.equals(TridasSample.NAME_INVALID))
			txtSpecimenName.setText(v);
		
		v = spec.getDateCollected();
		txtCollectionDate.setText((v == null) ? "" : v);
		
		selectEqualValueIn(cboPith, spec.getPith());
		selectEqualValueIn(cboContinuity, spec.getSpecimenContinuity());
		selectEqualValueIn(cboSpecimenType, spec.getSpecimenType());
		selectEqualValueIn(cboTerminalRing, spec.getTerminalRing());
		
		// integer spinner stuff
		if((iv = spec.getSapwoodCount()) != null)
			spnSapwoodCount.setValue(iv);
		if((iv = spec.getUnmeasuredPost()) != null)
			spnUnmeasPost.setValue(iv);
		if((iv = spec.getUnmeasuredPre()) != null)
			spnUnmeasPre.setValue(iv);
		
		// checkboxes
		if((b = spec.getIsSapwoodCountVerified()) != null)
			chkSapwoodCountVerified.setSelected(b.booleanValue());
		if((b = spec.getIsPithVerified()) != null)
			chkPithVerified.setSelected(b.booleanValue());
		if((b = spec.getIsSpecimenContinuityVerified()) != null)
			chkContinuityVerified.setSelected(b.booleanValue());
		if((b = spec.getIsTerminalRingVerified()) != null)
			chkTerminalRingVerified.setSelected(b.booleanValue());
		if((b = spec.getIsUnmeasuredPreVerified()) != null)
			chkUnmeasPreVerified.setSelected(b.booleanValue());
		if((b = spec.getIsUnmeasuredPostVerified()) != null)
			chkUnmeasPostVerified.setSelected(b.booleanValue());
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

	
    private void initButtons() {
    	//getRootPane().setDefaultButton(btnApply);
    	btnApply.setEnabled(false);
    	btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commit();
			}    		
    	});

    	btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}    		
    	});
}
    
    private void mapToDictionaries() {
    	mapToDictionary(cboTerminalRing, "TerminalRing");
    	mapToDictionary(cboContinuity, "SpecimenContinuity");
    	mapToDictionary(cboPith, "Pith");
    	mapToDictionary(cboSpecimenType, "SpecimenType");
    }
    
    private void mapToDictionary(JComboBox cbo, String dict) {
    	List<? extends BasicDictionaryElement> list = App.dictionary.getDictionary(dict);
    	Object[] array = new Object[list.size() + 1];
    	
    	array[0] = "- Not Specified -";
    	System.arraycopy(list.toArray(), 0, array, 1, list.size());
    	
    	cbo.setModel(new DefaultComboBoxModel(array));
    }
    
	public void commit() {
		TridasSample sp = new TridasSample(TridasSample.ID_NEW, txtSpecimenName.getText());
    	assimilateUpdateObject(sp);
    	IntermediateResource ir = new IntermediateResource(getParentObject(), sp);
    	
    	if(cboSpecimenType.getSelectedItem() instanceof SpecimenType)
    		sp.setSpecimenType(cboSpecimenType.getSelectedItem().toString());
    	if(txtCollectionDate.getText().length() > 0)
    		sp.setDateCollected(txtCollectionDate.getText());

    	// our drop-down dictionary elements
    	if(cboContinuity.getSelectedItem() instanceof SpecimenContinuity) {
    		sp.setSpecimenContinuity(cboContinuity.getSelectedItem().toString());
    		sp.setIsSpecimenContinuityVerified(chkContinuityVerified.isSelected());
    	}

    	if(cboTerminalRing.getSelectedItem() instanceof TerminalRing) {
    		sp.setTerminalRing(cboTerminalRing.getSelectedItem().toString());
    		sp.setIsTerminalRingVerified(chkTerminalRingVerified.isSelected());
    	}

    	if(cboPith.getSelectedItem() instanceof Pith) {
    		sp.setPith(cboPith.getSelectedItem().toString());
    		sp.setIsPithVerified(chkPithVerified.isSelected());
    	}
    	
    	// now, our spinners
    	if(spnSapwoodCount.getValue() instanceof Integer) {
    		sp.setSapwoodCount((Integer) spnSapwoodCount.getValue());
    		sp.setIsSapwoodCountVerified(chkSapwoodCountVerified.isSelected());
    	}

    	if(spnUnmeasPre.getValue() instanceof Integer) {
    		sp.setUnmeasuredPre((Integer) spnUnmeasPre.getValue());
    		sp.setIsUnmeasuredPreVerified(chkUnmeasPreVerified.isSelected());
    	}

    	if(spnUnmeasPost.getValue() instanceof Integer) {
    		sp.setUnmeasuredPost((Integer) spnUnmeasPost.getValue());
    		sp.setIsUnmeasuredPostVerified(chkUnmeasPostVerified.isSelected());
    	}

    	if(!createOrUpdateObject(ir))
    		return;
    	
		if(ir.getObject().get(0) instanceof TridasSample) {
			setNewObject((TridasSample) ir.getObject().get(0));
		}
		
    	dispose();
    }
    
	protected void validateForm() {
    	boolean nameOk;

    	// tree name name
		int len = txtSpecimenName.getText().length();

		if(len > 0 && !txtSpecimenName.getText().equals("Specimen Code"))
			nameOk = true;
		else
			nameOk = false;
		
		colorField(txtSpecimenName, nameOk);

		setFormValidated(nameOk);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelButtons = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        btnApply = new javax.swing.JButton();
        seperatorButtons = new javax.swing.JSeparator();
        panelMetadata = new javax.swing.JPanel();
        spnSapwoodCount = new javax.swing.JSpinner();
        chkContinuityVerified = new javax.swing.JCheckBox();
        spnUnmeasPost = new javax.swing.JSpinner();
        cboContinuity = new javax.swing.JComboBox();
        lblPith = new javax.swing.JLabel();
        lblVerifed = new javax.swing.JLabel();
        spnUnmeasPre = new javax.swing.JSpinner();
        chkUnmeasPreVerified = new javax.swing.JCheckBox();
        cboTerminalRing = new javax.swing.JComboBox();
        chkTerminalRingVerified = new javax.swing.JCheckBox();
        chkUnmeasPostVerified = new javax.swing.JCheckBox();
        lblSapwoodCount = new javax.swing.JLabel();
        chkPithVerified = new javax.swing.JCheckBox();
        lblTerminalRing = new javax.swing.JLabel();
        lblUnmeasPre = new javax.swing.JLabel();
        cboPith = new javax.swing.JComboBox();
        chkSapwoodCountVerified = new javax.swing.JCheckBox();
        lblUnmeasPost = new javax.swing.JLabel();
        lblContinuity = new javax.swing.JLabel();
        panelNameDetails = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        lblSpecimenType = new javax.swing.JLabel();
        lblCollectionDate = new javax.swing.JLabel();
        cboSpecimenType = new javax.swing.JComboBox();
        txtSpecimenName = new javax.swing.JTextField();
        txtCollectionDate = new javax.swing.JFormattedTextField();
        lblNamePrefix = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Specimen Details");
        setName("Specimen Details"); // NOI18N

        btnCancel.setText("Cancel");

        btnApply.setText("Apply");

        seperatorButtons.setBackground(new java.awt.Color(153, 153, 153));
        seperatorButtons.setOpaque(true);

        /*
        org.jdesktop.layout.GroupLayout panelButtonsLayout = new org.jdesktop.layout.GroupLayout(panelButtons);
        panelButtons.setLayout(panelButtonsLayout);
        panelButtonsLayout.setHorizontalGroup(
            panelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelButtonsLayout.createSequentialGroup()
                .addContainerGap(421, Short.MAX_VALUE)
                .add(btnApply)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnCancel)
                .add(5, 5, 5))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, seperatorButtons, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
        );
        panelButtonsLayout.setVerticalGroup(
            panelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelButtonsLayout.createSequentialGroup()
                .add(seperatorButtons, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btnCancel)
                    .add(btnApply))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        */

        spnSapwoodCount.setToolTipText("Number of sapwood rings");

        chkContinuityVerified.setToolTipText("His the continuity of the specimen been verified?");
        chkContinuityVerified.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkContinuityVerifiedActionPerformed(evt);
            }
        });

        spnUnmeasPost.setToolTipText("Number of unmeasured rings at the end of the sample");

        cboContinuity.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Not specified -", "Continuous", "Partially continuous", "Not continuous" }));
        cboContinuity.setToolTipText("Continuity of the specimen");
        cboContinuity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboContinuityActionPerformed(evt);
            }
        });

        lblPith.setLabelFor(cboPith);
        lblPith.setText("Pith:");

        lblVerifed.setText("Verified?");

        spnUnmeasPre.setToolTipText("Number of unmeasured rings at the beginning of the sample");

        chkUnmeasPreVerified.setToolTipText("Has the number of unmeasured rings at the beginning of the sample been verified?");
        chkUnmeasPreVerified.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkUnmeasPreVerifiedActionPerformed(evt);
            }
        });

        cboTerminalRing.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Not specified -", "Unknown", "Near edge", "Waney edge" }));
        cboTerminalRing.setToolTipText("Type of terminal ring");
        cboTerminalRing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTerminalRingActionPerformed(evt);
            }
        });

        chkTerminalRingVerified.setToolTipText("Has the terminal ring been verified");
        chkTerminalRingVerified.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkTerminalRingVerifiedActionPerformed(evt);
            }
        });

        chkUnmeasPostVerified.setToolTipText("Has the number of unmeasured rings at the end of the sample been verified?");
        chkUnmeasPostVerified.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkUnmeasPostVerifiedActionPerformed(evt);
            }
        });

        lblSapwoodCount.setLabelFor(spnSapwoodCount);
        lblSapwoodCount.setText("Sapwood count:");

        chkPithVerified.setToolTipText("Has the pith specification been verified");
        chkPithVerified.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPithVerifiedActionPerformed(evt);
            }
        });

        lblTerminalRing.setLabelFor(cboTerminalRing);
        lblTerminalRing.setText("Terminal ring:");

        lblUnmeasPre.setLabelFor(spnUnmeasPre);
        lblUnmeasPre.setText("Unmeasured rings at beginning:");

        cboPith.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Not specified -", "Present", "Present but undateable", "Absent" }));
        cboPith.setToolTipText("Pith specification");
        cboPith.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPithActionPerformed(evt);
            }
        });

        chkSapwoodCountVerified.setToolTipText("Has the sapwood count been verified");
        chkSapwoodCountVerified.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSapwoodCountVerifiedActionPerformed(evt);
            }
        });

        lblUnmeasPost.setLabelFor(spnUnmeasPost);
        lblUnmeasPost.setText("Unmeasured rings at end:");

        lblContinuity.setLabelFor(cboContinuity);
        lblContinuity.setText("Continuity:");

        org.jdesktop.layout.GroupLayout panelMetadataLayout = new org.jdesktop.layout.GroupLayout(panelMetadata);
        panelMetadata.setLayout(panelMetadataLayout);
        panelMetadataLayout.setHorizontalGroup(
            panelMetadataLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelMetadataLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelMetadataLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, panelMetadataLayout.createSequentialGroup()
                        .add(lblVerifed, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .add(panelMetadataLayout.createSequentialGroup()
                        .add(panelMetadataLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(lblUnmeasPost, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(lblTerminalRing, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(lblPith, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(lblSapwoodCount, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(lblContinuity)
                            .add(lblUnmeasPre, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .add(panelMetadataLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, cboTerminalRing, 0, 257, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, cboPith, 0, 257, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, spnUnmeasPre, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, cboContinuity, 0, 257, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, spnSapwoodCount, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, spnUnmeasPost, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE))
                        .add(31, 31, 31)
                        .add(panelMetadataLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(chkTerminalRingVerified)
                            .add(chkContinuityVerified)
                            .add(chkPithVerified)
                            .add(chkSapwoodCountVerified)
                            .add(chkUnmeasPreVerified)
                            .add(chkUnmeasPostVerified))
                        .add(41, 41, 41))))
        );
        panelMetadataLayout.setVerticalGroup(
            panelMetadataLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelMetadataLayout.createSequentialGroup()
                .add(lblVerifed)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelMetadataLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkContinuityVerified)
                    .add(lblContinuity)
                    .add(cboContinuity, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelMetadataLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkTerminalRingVerified)
                    .add(cboTerminalRing, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblTerminalRing))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelMetadataLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkPithVerified)
                    .add(cboPith, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblPith))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelMetadataLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panelMetadataLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(chkSapwoodCountVerified)
                        .add(spnSapwoodCount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(lblSapwoodCount))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelMetadataLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkUnmeasPreVerified)
                    .add(lblUnmeasPre)
                    .add(spnUnmeasPre, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelMetadataLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkUnmeasPostVerified)
                    .add(lblUnmeasPost)
                    .add(spnUnmeasPost, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel10.setLabelFor(txtSpecimenName);
        jLabel10.setText("Name:");

        lblSpecimenType.setLabelFor(cboSpecimenType);
        lblSpecimenType.setText("Type:");

        lblCollectionDate.setLabelFor(txtCollectionDate);
        lblCollectionDate.setText("Collection date:");

        cboSpecimenType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Not specified -", "Core", "Section", "Charcoal" }));
        cboSpecimenType.setToolTipText("Type of specimen");
        cboSpecimenType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSpecimenTypeActionPerformed(evt);
            }
        });

        txtSpecimenName.setText("1");
        txtSpecimenName.setToolTipText("Code name for this specimen");
        txtSpecimenName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSpecimenNameActionPerformed(evt);
            }
        });

        txtCollectionDate.setText("25/07/2003");
        txtCollectionDate.setToolTipText("Date this specimen was collected");
        txtCollectionDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCollectionDateActionPerformed(evt);
            }
        });

        lblNamePrefix.setText("C-ABC-1-");
        lblNamePrefix.setToolTipText("Lab code for this specimen");

        org.jdesktop.layout.GroupLayout panelNameDetailsLayout = new org.jdesktop.layout.GroupLayout(panelNameDetails);
        panelNameDetails.setLayout(panelNameDetailsLayout);
        panelNameDetailsLayout.setHorizontalGroup(
            panelNameDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelNameDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelNameDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jLabel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 104, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblCollectionDate)
                    .add(lblSpecimenType))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelNameDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panelNameDetailsLayout.createSequentialGroup()
                        .add(lblNamePrefix)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtSpecimenName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE))
                    .add(cboSpecimenType, 0, 328, Short.MAX_VALUE)
                    .add(txtCollectionDate, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelNameDetailsLayout.setVerticalGroup(
            panelNameDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelNameDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelNameDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel10)
                    .add(txtSpecimenName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblNamePrefix))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelNameDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cboSpecimenType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblSpecimenType))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelNameDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(txtCollectionDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblCollectionDate))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelNameDetails, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(panelMetadata, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            //.add(panelButtons, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(panelNameDetails, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 117, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(4, 4, 4)
                .add(panelMetadata, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                //.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                //.add(panelButtons, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtSpecimenNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSpecimenNameActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtSpecimenNameActionPerformed

    private void txtCollectionDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCollectionDateActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtCollectionDateActionPerformed

    private void cboSpecimenTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSpecimenTypeActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_cboSpecimenTypeActionPerformed

    private void cboTerminalRingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTerminalRingActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_cboTerminalRingActionPerformed

    private void cboContinuityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboContinuityActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_cboContinuityActionPerformed

    private void chkPithVerifiedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPithVerifiedActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_chkPithVerifiedActionPerformed

    private void chkContinuityVerifiedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkContinuityVerifiedActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_chkContinuityVerifiedActionPerformed

    private void chkSapwoodCountVerifiedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSapwoodCountVerifiedActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_chkSapwoodCountVerifiedActionPerformed

    private void chkUnmeasPreVerifiedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkUnmeasPreVerifiedActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_chkUnmeasPreVerifiedActionPerformed

    private void cboPithActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPithActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_cboPithActionPerformed

    private void chkTerminalRingVerifiedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkTerminalRingVerifiedActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_chkTerminalRingVerifiedActionPerformed

    private void chkUnmeasPostVerifiedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkUnmeasPostVerifiedActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_chkUnmeasPostVerifiedActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnCancel;
    private javax.swing.JComboBox cboContinuity;
    private javax.swing.JComboBox cboPith;
    private javax.swing.JComboBox cboSpecimenType;
    private javax.swing.JComboBox cboTerminalRing;
    private javax.swing.JCheckBox chkContinuityVerified;
    private javax.swing.JCheckBox chkPithVerified;
    private javax.swing.JCheckBox chkSapwoodCountVerified;
    private javax.swing.JCheckBox chkTerminalRingVerified;
    private javax.swing.JCheckBox chkUnmeasPostVerified;
    private javax.swing.JCheckBox chkUnmeasPreVerified;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel lblCollectionDate;
    private javax.swing.JLabel lblContinuity;
    private javax.swing.JLabel lblNamePrefix;
    private javax.swing.JLabel lblPith;
    private javax.swing.JLabel lblSapwoodCount;
    private javax.swing.JLabel lblSpecimenType;
    private javax.swing.JLabel lblTerminalRing;
    private javax.swing.JLabel lblUnmeasPost;
    private javax.swing.JLabel lblUnmeasPre;
    private javax.swing.JLabel lblVerifed;
    private javax.swing.JPanel panelButtons;
    private javax.swing.JPanel panelMetadata;
    private javax.swing.JPanel panelNameDetails;
    private javax.swing.JSeparator seperatorButtons;
    private javax.swing.JSpinner spnSapwoodCount;
    private javax.swing.JSpinner spnUnmeasPost;
    private javax.swing.JSpinner spnUnmeasPre;
    private javax.swing.JFormattedTextField txtCollectionDate;
    private javax.swing.JTextField txtSpecimenName;
    // End of variables declaration//GEN-END:variables
    
}
