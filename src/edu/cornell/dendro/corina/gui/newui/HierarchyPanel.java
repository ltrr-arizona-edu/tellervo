/*
 * CreateSample.java
 *
 * Created on June 2, 2008, 3:30 PM
 */

package edu.cornell.dendro.corina.gui.newui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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

public class HierarchyPanel extends javax.swing.JPanel {
	private JDialog parent;
	private Site lastSelectedSite;
    
    /** Creates new form CreateSample */
    public HierarchyPanel(JPanel parent) {
    	//this.parent = parent;
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
    
    /**
     * For each button, set its action
     */
    public void setupButtons() {
    	// new SITE
    	btnNewSite.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent ae) {
    			SiteDialog sd = null;// = new SiteDialog(parent, true);
    			
    			sd.setVisible(true);
    			
    			if(sd.didSucceed()) {
    				// Repopulate the subsites box
    				populateSiteList();
    				
    				// choose our 'select a site' box to reset things...
    				cboSite.setSelectedIndex(0);
    				
    				// and select the subsite that we just created
    				cboSite.setSelectedItem(sd.getNewObject());
    			}
    		}
    	});

    	// new SUBSITE
    	btnNewSubsite.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent ae) {
    			Site site = (Site) cboSite.getSelectedItem();
    			SubsiteDialog sd = null;// = new SubsiteDialog(parent, true, site);
    			
    			sd.setVisible(true);
    			
    			if(sd.didSucceed()) {
    				// Repopulate the subsites box
    				populateSubsites(site.getSubsites());
    				
    				// and select the subsite that we just created
    				cboSubsite.setSelectedItem(sd.getNewObject());
    			}
    		}
    	});
    	
    	btnNewTree.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent ae) {
    			Subsite subsite = (Subsite) cboSubsite.getSelectedItem();
    			TreeDialog td = null;// = new TreeDialog(parent, true, getLabel(2), subsite);
    			
    			td.setVisible(true);
    			
    			if(td.didSucceed()) {
    				// add item to the tree box and select it
    				((DefaultComboBoxModel)cboTree.getModel()).addElement(td.getNewObject());
    				cboTree.setSelectedItem(td.getNewObject());
    			}    			
    		}
    	});

    	btnNewSpecimen.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent ae) {
    			Tree tree = (Tree) cboTree.getSelectedItem();
    			SpecimenDialog sd = null;// = new SpecimenDialog(parent, true, getLabel(3), tree);
    			
    			sd.setVisible(true);

    			if(sd.didSucceed()) {
    				// add item to the tree box and select it
    				((DefaultComboBoxModel)cboSpecimen.getModel()).addElement(sd.getNewObject());
    				cboSpecimen.setSelectedItem(sd.getNewObject());
    			}    
    		}
    	});
    	
    	btnNewRadius.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent ae) {
    			Specimen specimen = (Specimen) cboSpecimen.getSelectedItem();
    			RadiusDialog rd = null;// = new RadiusDialog(parent, true, getLabel(4), specimen);
    			
    			rd.setVisible(true);

    			if(rd.didSucceed()) {
    				// add item to the tree box and select it
    				((DefaultComboBoxModel)cboRadius.getModel()).addElement(rd.getNewObject());
    				cboRadius.setSelectedItem(rd.getNewObject());
    			}    
    		}
    	});
}
    
    public void setupBoxes() {
        // SITE listener
    	// For selection state changes
        cboSite.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent ie) {
        		if(ie.getStateChange() != ItemEvent.SELECTED)
        			return;
        		
        		JComboBox src = (JComboBox) ie.getSource();        		
        		Object o = src.getSelectedItem();
        		
        		// maintain our selection boxes and label
        		disableBoxes(1);
        		updateLabel();

        		// The user chose the 'choose a site' option
        		if(!(o instanceof Site)) {
        			return;
        		}
        		
        		// yay, now choose a subsite!
        		Site s = (Site) o;
        		populateSubsites(s.getSubsites());
        		
        		// If we only have one subsite, select it
        		if(s.getSubsites().size() == 1) {
        			cboSubsite.setSelectedIndex(1);
        			cboTree.requestFocus();
        		}
        	}
        });
        
        // Our site combobox is editable
        cboSite.setEditable(true);
        
        // we need this glue for the following modification listeners
        final JTextField cboSiteEditor = (JTextField) cboSite.getEditor().getEditorComponent();

        // Perform autocomplete when the user types things into the site editor box
        
        cboSiteEditor.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				// ignore -- ??
			}

			public void insertUpdate(DocumentEvent e) {
				String text = cboSiteEditor.getText();
				// we only care if this insert happens at the end of the box
				if (e.getOffset() + 1 == text.length()) {
					// get a list of keys greater (alphabetically) than what we have
					// we really only care about the first entry that's greater than what we have
					SortedMap<String, Site> similarSites = App.sites.getSimilarSites(text);

					String completion;
					if (similarSites.firstKey().startsWith(text))
						completion = similarSites.firstKey();
					else
						return;

					// add it -- but you can't change a document
					// in a documentlistener, or an illegal state
					// exception gets thrown. (the documentation
					// doesn't seem to mention this rather
					// important fact!)

					final String glue = completion;
					final int curs = e.getOffset();
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							cboSiteEditor.setText(glue);
							cboSiteEditor.setSelectionStart(curs + 1);
							cboSiteEditor.setSelectionEnd(cboSiteEditor.getText().length());
						}
					});
				}
			}

			public void removeUpdate(DocumentEvent e) {
				// ignore
			}
		});

        // add a listener for new keys being typed into the site editor box
        // we need to do this to force caps and no whitespace, as well as
        // handling enter-key autoselection
        cboSiteEditor.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {			
				if(e.getKeyChar() == KeyEvent.VK_ENTER) {
					Site selectedSite; 
					
					// if we have the site code the user typed in
					if((selectedSite = App.sites.findSite(cboSiteEditor.getText())) != null) {
						// select it, don't allow it to be edited, and etc
						cboSubsite.requestFocus();
						cboSite.setSelectedItem(selectedSite);
						cboSiteEditor.setCaretPosition(0);
						cboSiteEditor.setEditable(false);
					}
					else {
						JOptionPane.showMessageDialog(parent, 
								"Invalid site code '" + cboSiteEditor.getText() + 
								"'", "Bad site code", JOptionPane.ERROR_MESSAGE);
						cboSiteEditor.selectAll();
					}
					e.consume();
					return;
				}
				
				// no whitespace!
				if(Character.isWhitespace(e.getKeyChar())) {
					e.consume();
					return;
				}
				
				// force everything else to uppercase
				e.setKeyChar(Character.toUpperCase(e.getKeyChar()));
			}
        });
        
        // on focus select all of the text in the site editor box
        cboSiteEditor.addFocusListener(new FocusListener() {
        	public void focusGained(FocusEvent fe) {
        		cboSiteEditor.setEditable(true);
        		cboSiteEditor.setCaretPosition(0);
        		cboSiteEditor.selectAll();
        	}

			public void focusLost(FocusEvent fe) {
				// TODO: ensure we don't have garbage in the box
			}
        });
        
        // request default site
        cboSiteEditor.requestFocusInWindow();

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
    	lblCodeName.setText(getLabel(5));    	
    }
    
    /**
     * Get a text label from what's been currently selected
     * depth 1 = C-
     * depth 2 = C-Site/subsite
     * depth 3 = C-Site/subsite-Tree
     * depth 4 = C-Site/subsite-Tree-Specimen
     * depth 5 = C-Site/subsite-Tree-Specimen-Radius
     * @param maxdepth
     * @return
     */
    private String getLabel(int maxdepth) {
    	StringBuffer sb = new StringBuffer();
    	Object o;
    	
    	sb.append("C-");
    	do {
    		if(maxdepth < 2)
    			break;
    		
    		// site
    		o = cboSite.getSelectedItem();
    		if((o != null) && (o instanceof Site)) {
    			sb.append(((Site)o).getCode());
    		}
    		else
    			break;

    		// subsite
    		o = cboSubsite.getSelectedItem();
    		if((o != null) && (o instanceof Subsite)) {
    			Subsite ss = (Subsite) o;

    			// don't add /Main (it's assumed)
    			if(!ss.toString().equalsIgnoreCase("main")) {
    				sb.append("/");
    				sb.append(ss);
    			}
    			sb.append("-");
    		}
    		else
    			break;
    		
    		if(maxdepth < 3)
    			break;
    		
    		// tree
    		o = cboTree.getSelectedItem();
    		if((o != null) && (o instanceof Tree)) {
    			sb.append(o);
    			sb.append("-");
    		}
    		else
    			break;

    		if(maxdepth < 4)
    			break;
    		
    		// specimen
    		o = cboSpecimen.getSelectedItem();
    		if((o != null) && (o instanceof Specimen)) {
    			sb.append(o);
    			sb.append("-");
    		}
    		else
    			break;
    		
    		if(maxdepth < 5)
    			break;
    		
    		// radius
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
    	
    	if(list.size() == 0)
    		dstArray[0] = "No " + firstItem;
    	else
    		dstArray[0] = "Choose a " + firstItem;
    	
    	System.arraycopy(srcArray, 0, dstArray, 1, srcArray.length);
    	
    	return dstArray;
    }
    
    private void populateSiteList() {
    	Collection<Site> sites = App.sites.getSites();
    	Object selectedSiteObj = cboSite.getSelectedItem();
    	Site selectedSite = (selectedSiteObj instanceof Site ? (Site) selectedSiteObj : null);
    	
    	Object[] siteList = formulateArrayFromCollection(sites, "site");
    	
    	cboSite.setModel(new javax.swing.DefaultComboBoxModel(siteList));
    	
    	// if our site list was updated in the background,
    	// we have to compare sites. blurgh.
    	if(selectedSite != null) {
    		for(int i = 0; i < cboSite.getModel().getSize(); i++) {
    			Object obj = cboSite.getModel().getElementAt(i);
    			
    			// don't compare against non-sites
    			if(!(obj instanceof Site))
    				continue;
    			
    			if(((Site)obj).equals(selectedSite)) {
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
    	
    	Object[] subsiteList = formulateArrayFromCollection(subsites, "subsite");
    	
    	cboSubsite.setModel(new javax.swing.DefaultComboBoxModel(subsiteList));
    	
    	// if our site list was updated in the background,
    	// we have to compare sites. blurgh.
    	if(selectedSubsite != null) {
    		for(int i = 0; i < cboSubsite.getModel().getSize(); i++) {
    			Object obj = cboSubsite.getModel().getElementAt(i);
    			
    			// skip over non-subsites
    			if(!(obj instanceof Subsite))
    				continue;
    			
    			if(((Subsite)obj).equals(selectedSubsite)) {
    				cboSubsite.setSelectedIndex(i);
    				break;
    			}
    		}
    	}
    	
    	cboSubsite.setEnabled(true);
    	cboSubsite.requestFocus();
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

    	Object[] treeList = formulateArrayFromCollection(resource.getObject(), "tree");
    	cboTree.setModel(new DefaultComboBoxModel(treeList));		
    	cboTree.setEnabled(true);
    	cboTree.requestFocus();
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

    	Object[] specimenList = formulateArrayFromCollection(resource.getObject(), "specimen");
    	cboSpecimen.setModel(new DefaultComboBoxModel(specimenList));		
    	cboSpecimen.setEnabled(true);
    	cboSpecimen.requestFocus();
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

    	Object[] radiusList = formulateArrayFromCollection(resource.getObject(), "radius");
    	cboRadius.setModel(new DefaultComboBoxModel(radiusList));		
    	cboRadius.setEnabled(true);
    	cboRadius.requestFocus();
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
