/*
 * CreateSample.java
 *
 * Created on June 2, 2008, 3:30 PM
 */

package edu.cornell.dendro.corina.gui.newui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.dictionary.SiteRegion;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.site.GenericIntermediateObject;
import edu.cornell.dendro.corina.site.Radius;
import edu.cornell.dendro.corina.site.Site;
import edu.cornell.dendro.corina.site.Specimen;
import edu.cornell.dendro.corina.site.Subsite;
import edu.cornell.dendro.corina.site.Tree;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.webdbi.IntermediateResource;
import edu.cornell.dendro.corina.webdbi.PrototypeLoadDialog;
import edu.cornell.dendro.corina.webdbi.ResourceEvent;
import edu.cornell.dendro.corina.webdbi.ResourceEventListener;
import edu.cornell.dendro.corina.webdbi.SearchParameters;

/**
 *
 * @author  peterbrewer
 */
public class CreateSample extends javax.swing.JPanel {
	private JDialog parent;
    
    /** Creates new form CreateSample */
    public CreateSample(JDialog parent) {
    	this.parent = parent;
        initComponents();
    }
    
    public void initialize(Sample template) {
    	populateSiteList();
    	
        // Whenever the site list changes, make sure we repopulate our site list
        App.sites.addResourceEventListener(new ResourceEventListener() {
        	public void resourceChanged(ResourceEvent re) {	
        		if(re.getEventType() == ResourceEvent.RESOURCE_QUERY_COMPLETE)
        			populateSiteList();
        	}
        });
        
        if(template == null) {
        	disableBoxes(1);
        	lblCodeName.setText("C-");
        } else {
        	// TODO: Implement this
        	// It's complicated, because we have to get a bunch of lists first!
        }
        setupBoxes();
        setupButtons();
    }
    
    public void setupButtons() {
    	btnNewSite.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent ae) {
    			SiteDialog sd = new SiteDialog(parent, true);
    			
    			sd.setVisible(true);
    			
    			//TODO: Success?
    		}
    	});
    	
    	btnNewSubsite.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent ae) {
    			Site site = (Site) cboSite.getSelectedItem();
    			SubsiteDialog sd = new SubsiteDialog(parent, true, site);
    			
    			sd.setVisible(true);
    			
    			// add and autoselect...
    			if(sd.didSucceed()) {
    				populateSubsites(site.getSubsites());
    				cboSubsite.setSelectedItem(sd.getNewSubsite());
    			}
    		}
    	});
    	
    	btnNewTree.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent ae) {
    			Subsite subsite = (Subsite) cboSubsite.getSelectedItem();
    			TreeDialog td = new TreeDialog(parent, true, getLabel(), subsite);
    			
    			td.setVisible(true);
    			
    			// add and autoselect...
    			/*
    			if(sd.didSucceed()) {
    				populateSubsites(site.getSubsites());
    				cboSubsite.setSelectedItem(sd.getNewSubsite());
    			}
    			*/
    		}
    	});
    }
    
    public void setupBoxes() {
        // SITE listener
        cboSite.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent ie) {
        		if(ie.getStateChange() != ItemEvent.SELECTED)
        			return;
        		
        		JComboBox src = (JComboBox) ie.getSource();        		
        		Object o = src.getSelectedItem();
        		
        		disableBoxes(1);
        		updateLabel();
        		if(!(o instanceof Site)) {
        			return;
        		}
        		
        		Site s = (Site) o;
        		cboSubsite.setModel(emptyBoxModel);
        		populateSubsites(s.getSubsites());
        	}
        });

        // SUBSITE listener
        cboSubsite.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent ie) {
        		if(ie.getStateChange() != ItemEvent.SELECTED)
        			return;
        		
        		JComboBox src = (JComboBox) ie.getSource();        		
        		Object o = src.getSelectedItem();

        		disableBoxes(2);
        		updateLabel();
        		if(!(o instanceof Subsite)) {
        			return;
        		}
        		
        		Subsite s = (Subsite) o;
        		populateTrees(s.getID());
        	}
        });

        // TREE listener
        cboTree.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent ie) {
        		if(ie.getStateChange() != ItemEvent.SELECTED)
        			return;
        		
        		JComboBox src = (JComboBox) ie.getSource();        		
        		Object o = src.getSelectedItem();

        		disableBoxes(3);
        		updateLabel();
        		if(!(o instanceof Tree)) {
        			return;
        		}
        		
        		Tree t = (Tree) o;
        		populateSpecimens(t.getID());
        	}
        });
        
        // SPECIMEN listener
        cboSpecimen.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent ie) {
        		if(ie.getStateChange() != ItemEvent.SELECTED)
        			return;
        		
        		JComboBox src = (JComboBox) ie.getSource();        		
        		Object o = src.getSelectedItem();

        		disableBoxes(4);
        		updateLabel();
        		if(!(o instanceof Specimen)) {
        			return;
        		}
        		
        		Specimen s = (Specimen) o;
        		populateRadii(s.getID());
        	}
        });
        
        // RADIUS listener
        cboRadius.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent ie) {
        		if(ie.getStateChange() != ItemEvent.SELECTED)
        			return;
        		
        		JComboBox src = (JComboBox) ie.getSource();        		
        		Object o = src.getSelectedItem();

        		disableBoxes(5);
        		updateLabel();
        		if(!(o instanceof Radius)) {
        			return;
        		}
        		
        		btnOk.setEnabled(true);
        	}
        });
    }
    
    // nothing to see here...
    private final static DefaultComboBoxModel emptyBoxModel = new DefaultComboBoxModel(new String[] { "----" });
    
    // this makes our lives easier...
    private void disableBoxes(int boxposition) {
    	switch(boxposition) {
    	case 0:
    	case 1:
    		cboSubsite.setModel(emptyBoxModel);
    		cboSubsite.setEnabled(false);
    		btnNewSubsite.setEnabled(false);
    	case 2:
    		cboTree.setModel(emptyBoxModel);
    		cboTree.setEnabled(false);
    		btnNewTree.setEnabled(false);
    	case 3:
    		cboSpecimen.setModel(emptyBoxModel);
    		cboSpecimen.setEnabled(false);
    		btnNewSpecimen.setEnabled(false);
    	case 4:
    		cboRadius.setModel(emptyBoxModel);
    		cboRadius.setEnabled(false);
    		btnNewRadius.setEnabled(false);
    		
    	default:
    		btnOk.setEnabled(false);
    	}
    }
    
    private void updateLabel() {
    	lblCodeName.setText(getLabel());    	
    }
    
    private String getLabel() {
    	StringBuffer sb = new StringBuffer();
    	Object o;
    	
    	sb.append("C-");
    	do {
    		o = cboSite.getSelectedItem();
    		if((o != null) && (o instanceof Site)) {
    			sb.append(((Site)o).getCode());
    		}
    		else
    			break;

    		o = cboSubsite.getSelectedItem();
    		if((o != null) && (o instanceof Subsite)) {
    			Subsite ss = (Subsite) o;
    			if(!ss.toString().equalsIgnoreCase("main")) {
    				sb.append("/");
    				sb.append(ss);
    			}
    			sb.append("-");
    		}
    		else
    			break;
    		
    		o = cboTree.getSelectedItem();
    		if((o != null) && (o instanceof Tree)) {
    			sb.append(o);
    			sb.append("-");
    		}
    		else
    			break;

    		o = cboSpecimen.getSelectedItem();
    		if((o != null) && (o instanceof Specimen)) {
    			sb.append(o);
    			sb.append("-");
    		}
    		else
    			break;
    		
    		o = cboRadius.getSelectedItem();
    		if((o != null) && (o instanceof Radius)) {
    			sb.append(o);
    		}
    		else
    			break;
    	}
    	while(false);
    	
    	return sb.toString();
    }
    
    /**
     * Create an array from a collection of something with a first item of type String
     * Useful for populating a combo box with a "Choose one" option at the top.
     * @param list
     * @param firstItem
     * @return
     */
    private Object[] formulateArrayFromCollection(Collection<?> list, String firstItem) {
    	Object[] srcArray = list.toArray();
    	Object[] dstArray = new Object[srcArray.length + 1];
    	
    	dstArray[0] = firstItem;
    	System.arraycopy(srcArray, 0, dstArray, 1, srcArray.length);
    	
    	return dstArray;
    }
    
    private void populateSiteList() {
    	Collection<Site> sites = App.sites.getSites();
    	Object selectedSiteObj = cboSite.getSelectedItem();
    	Site selectedSite = (selectedSiteObj instanceof Site ? (Site) selectedSiteObj : null);
    	
    	Object[] siteList = formulateArrayFromCollection(sites, "Choose a site");
    	
    	cboSite.setModel(new javax.swing.DefaultComboBoxModel(siteList));
    	
    	// if our site list was updated in the background,
    	// we have to compare sites. blurgh.
    	if(selectedSite != null) {
    		for(int i = 0; i < cboSite.getModel().getSize(); i++) {
    			if(((Site)cboSite.getModel().getElementAt(i)).equals(selectedSite)) {
    				cboSite.setSelectedIndex(i);
    				break;
    			}
    		}
    		        	
    		populateSubsites(selectedSite.getSubsites());
    	}    	
    }
    
    private void populateSubsites(List<Subsite> subsites) {
    	Object selectedSubsiteObj = cboSubsite.getSelectedItem();
    	Subsite selectedSubsite = (selectedSubsiteObj instanceof Subsite ? (Subsite) selectedSubsiteObj : null);
    	
    	Object[] subsiteList = formulateArrayFromCollection(subsites, "Choose a subsite");
    	
    	cboSubsite.setModel(new javax.swing.DefaultComboBoxModel(subsiteList));
    	
    	// if our site list was updated in the background,
    	// we have to compare sites. blurgh.
    	if(selectedSubsite != null) {
    		for(int i = 0; i < cboSubsite.getModel().getSize(); i++) {
    			if(((Subsite)cboSubsite.getModel().getElementAt(i)).equals(selectedSubsite)) {
    				cboSubsite.setSelectedIndex(i);
    				break;
    			}
    		}
    	}
    	
    	cboSubsite.setEnabled(true);
    	btnNewSubsite.setEnabled(true);
    }
    
    private void populateTrees(String parentID) {
		SearchParameters sp = new SearchParameters("tree");
		sp.addSearchConstraint("subsiteid", "=", parentID);
		
		IntermediateResource resource = new IntermediateResource(sp);
		PrototypeLoadDialog dlg = new PrototypeLoadDialog(resource);
		
		resource.query();
		dlg.setVisible(true);
		
		if(!dlg.isSuccessful()) {
			Alert.error("Could not load trees", 
					"An error occured while acquiring a tree list: " + dlg.getFailException());
			return;
		}

    	Object[] treeList = formulateArrayFromCollection(resource.getObject(), "Choose a tree");
    	cboTree.setModel(new DefaultComboBoxModel(treeList));		
    	cboTree.setEnabled(true);
    	btnNewTree.setEnabled(true);
    }

    private void populateSpecimens(String parentID) {
		SearchParameters sp = new SearchParameters("specimen");
		sp.addSearchConstraint("treeid", "=", parentID);
		
		IntermediateResource resource = new IntermediateResource(sp);
		PrototypeLoadDialog dlg = new PrototypeLoadDialog(resource);
		
		resource.query();
		dlg.setVisible(true);
		
		if(!dlg.isSuccessful()) {
			Alert.error("Could not load specimens", 
					"An error occured while acquiring a specimen list: " + dlg.getFailException());
			return;
		}

    	Object[] specimenList = formulateArrayFromCollection(resource.getObject(), "Choose a specimen");
    	cboSpecimen.setModel(new DefaultComboBoxModel(specimenList));		
    	cboSpecimen.setEnabled(true);
    	btnNewSpecimen.setEnabled(true);
    }

    private void populateRadii(String parentID) {
		SearchParameters sp = new SearchParameters("radius");
		sp.addSearchConstraint("specimenid", "=", parentID);
		
		IntermediateResource resource = new IntermediateResource(sp);
		PrototypeLoadDialog dlg = new PrototypeLoadDialog(resource);
		
		resource.query();
		dlg.setVisible(true);
		
		if(!dlg.isSuccessful()) {
			Alert.error("Could not load radii", 
					"An error occured while acquiring a radius list: " + dlg.getFailException());
			return;
		}

    	Object[] radiusList = formulateArrayFromCollection(resource.getObject(), "Choose a radius");
    	cboRadius.setModel(new DefaultComboBoxModel(radiusList));		
    	cboRadius.setEnabled(true);
    	btnNewRadius.setEnabled(true);
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelNewValues = new javax.swing.JPanel();
        cboSite = new javax.swing.JComboBox();
        lblSite = new javax.swing.JLabel();
        lblSubsite = new javax.swing.JLabel();
        lblTree = new javax.swing.JLabel();
        tblSpecimen = new javax.swing.JLabel();
        lblRadius = new javax.swing.JLabel();
        cboSubsite = new javax.swing.JComboBox();
        cboTree = new javax.swing.JComboBox();
        cboSpecimen = new javax.swing.JComboBox();
        cboRadius = new javax.swing.JComboBox();
        btnNewSite = new javax.swing.JButton();
        btnNewSubsite = new javax.swing.JButton();
        btnNewTree = new javax.swing.JButton();
        btnNewSpecimen = new javax.swing.JButton();
        btnNewRadius = new javax.swing.JButton();
        panelButtons = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        seperatorButtons = new javax.swing.JSeparator();
        lblCodeName = new javax.swing.JLabel();

        cboSite.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select a site" }));

        lblSite.setLabelFor(cboSite);
        lblSite.setText("Site:");

        lblSubsite.setLabelFor(btnNewSubsite);
        lblSubsite.setText("Subsite:");

        lblTree.setLabelFor(cboTree);
        lblTree.setText("Tree:");

        tblSpecimen.setLabelFor(cboSpecimen);
        tblSpecimen.setText("Specimen:");

        lblRadius.setLabelFor(cboRadius);
        lblRadius.setText("Radius:");

        cboSubsite.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select a Site first" }));
        cboSubsite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSubsiteActionPerformed(evt);
            }
        });

        cboTree.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select a SubSite first" }));

        cboSpecimen.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select a Tree first" }));

        cboRadius.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select a Specimen First" }));

        btnNewSite.setText("New");

        btnNewSubsite.setText("New");

        btnNewTree.setText("New");

        btnNewSpecimen.setText("New");

        btnNewRadius.setText("New");

        org.jdesktop.layout.GroupLayout panelNewValuesLayout = new org.jdesktop.layout.GroupLayout(panelNewValues);
        panelNewValues.setLayout(panelNewValuesLayout);
        panelNewValuesLayout.setHorizontalGroup(
            panelNewValuesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelNewValuesLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelNewValuesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, lblSubsite, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblSite, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(tblSpecimen, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, lblTree, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, lblRadius, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(18, 18, 18)
                .add(panelNewValuesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panelNewValuesLayout.createSequentialGroup()
                        .add(cboSite, 0, 236, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnNewSite))
                    .add(panelNewValuesLayout.createSequentialGroup()
                        .add(cboSubsite, 0, 236, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnNewSubsite))
                    .add(panelNewValuesLayout.createSequentialGroup()
                        .add(cboTree, 0, 236, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnNewTree))
                    .add(panelNewValuesLayout.createSequentialGroup()
                        .add(cboSpecimen, 0, 236, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnNewSpecimen))
                    .add(panelNewValuesLayout.createSequentialGroup()
                        .add(cboRadius, 0, 236, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnNewRadius)))
                .addContainerGap())
        );
        panelNewValuesLayout.setVerticalGroup(
            panelNewValuesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelNewValuesLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelNewValuesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblSite)
                    .add(cboSite, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnNewSite))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelNewValuesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblSubsite)
                    .add(cboSubsite, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnNewSubsite))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelNewValuesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblTree)
                    .add(cboTree, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnNewTree))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelNewValuesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(tblSpecimen)
                    .add(cboSpecimen, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnNewSpecimen))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelNewValuesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblRadius)
                    .add(cboRadius, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnNewRadius))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            .add(org.jdesktop.layout.GroupLayout.TRAILING, seperatorButtons, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .add(lblCodeName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
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

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelButtons, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(panelNewValues, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(panelNewValues, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelButtons, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        getAccessibleContext().setAccessibleName("Create sample");
    }// </editor-fold>//GEN-END:initComponents

    private void cboSubsiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSubsiteActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_cboSubsiteActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNewRadius;
    private javax.swing.JButton btnNewSite;
    private javax.swing.JButton btnNewSpecimen;
    private javax.swing.JButton btnNewSubsite;
    private javax.swing.JButton btnNewTree;
    private javax.swing.JButton btnOk;
    private javax.swing.JComboBox cboRadius;
    private javax.swing.JComboBox cboSite;
    private javax.swing.JComboBox cboSpecimen;
    private javax.swing.JComboBox cboSubsite;
    private javax.swing.JComboBox cboTree;
    private javax.swing.JLabel lblCodeName;
    private javax.swing.JLabel lblRadius;
    private javax.swing.JLabel lblSite;
    private javax.swing.JLabel lblSubsite;
    private javax.swing.JLabel lblTree;
    private javax.swing.JPanel panelButtons;
    private javax.swing.JPanel panelNewValues;
    private javax.swing.JSeparator seperatorButtons;
    private javax.swing.JLabel tblSpecimen;
    // End of variables declaration//GEN-END:variables
    
}
