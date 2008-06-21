package edu.cornell.dendro.corina.gui.newui;

import java.util.Collection;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.site.*;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.webdbi.IntermediateResource;
import edu.cornell.dendro.corina.webdbi.PrototypeLoadDialog;
import edu.cornell.dendro.corina.webdbi.SearchParameters;

public class ComboBoxPopulator {
	private JComboBox cbo;
	
	public ComboBoxPopulator(JComboBox cbo) {
		this.cbo = cbo;		
	}
	
	public void populate(GenericIntermediateObject parent) {
		if(parent == null) {
			cbo.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXX");
			populateSiteList();
			// site list!
			return;
		}
		
		if(parent instanceof Site) {
			cbo.setPrototypeDisplayValue("XXXXXXXXXXXX");
			populateSubsites(((Site) parent).getSubsites());
			// subsite
			return;
		}
		
		if(parent instanceof Subsite) {
			populateTrees(parent.getID());
			// tree
			return;
		}
		
		if(parent instanceof Tree) {
			populateSpecimens(parent.getID());
			// specimen
			return;
		}
		
		if(parent instanceof Specimen) {
			populateRadii(parent.getID());
			// radius
			return;
		}
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
    	Object selectedSiteObj = cbo.getSelectedItem();
    	Site selectedSite = (selectedSiteObj instanceof Site ? (Site) selectedSiteObj : null);
    	
    	Object[] siteList = formulateArrayFromCollection(sites, "site");
    	
    	cbo.setModel(new javax.swing.DefaultComboBoxModel(siteList));
    	
    	// if our site list was updated in the background,
    	// we have to compare sites. blurgh.
    	if(selectedSite != null) {
    		for(int i = 0; i < cbo.getModel().getSize(); i++) {
    			Object obj = cbo.getModel().getElementAt(i);
    			
    			// don't compare against non-sites
    			if(!(obj instanceof Site))
    				continue;
    			
    			if(((Site)obj).equals(selectedSite)) {
    				cbo.setSelectedIndex(i);
    				break;
    			}
    		}
    	}    	
    }
    
    private void populateSubsites(List<Subsite> subsites) {
    	Object selectedSubsiteObj = cbo.getSelectedItem();
    	Subsite selectedSubsite = (selectedSubsiteObj instanceof Subsite ? (Subsite) selectedSubsiteObj : null);
    	
    	Object[] subsiteList = formulateArrayFromCollection(subsites, "subsite");
    	
    	cbo.setModel(new javax.swing.DefaultComboBoxModel(subsiteList));
    	
    	// if our site list was updated in the background,
    	// we have to compare sites. blurgh.
    	if(selectedSubsite != null) {
    		for(int i = 0; i < cbo.getModel().getSize(); i++) {
    			Object obj = cbo.getModel().getElementAt(i);
    			
    			// skip over non-subsites
    			if(!(obj instanceof Subsite))
    				continue;
    			
    			if(((Subsite)obj).equals(selectedSubsite)) {
    				cbo.setSelectedIndex(i);
    				break;
    			}
    		}
    	}    	
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
    	cbo.setModel(new DefaultComboBoxModel(treeList));		
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
    	cbo.setModel(new DefaultComboBoxModel(specimenList));		
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
    	cbo.setModel(new DefaultComboBoxModel(radiusList));		
    }
}
