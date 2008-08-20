/**
 * 
 */
package edu.cornell.dendro.corina.site;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuBar;
import edu.cornell.dendro.corina.Build;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gui.XFrame;
import edu.cornell.dendro.corina.gui.menus.*;
import edu.cornell.dendro.corina.util.*;
import edu.cornell.dendro.corina.gui.SaveableDocument;

/**
 * @author Lucas Madar
 *
 */
public class SiteEditor extends XFrame implements SaveableDocument {
	SiteEditorPanel sitePanel;
	
	public SiteEditor() {
		super();
		
		setTitle(null);
		
        // make our copy of the site list
        createImmutableSitelist();
        
        // add the site menu
        sitePanel = new SiteEditorPanel(this);
        setContentPane(sitePanel);
        
        // add menubar
        JMenuBar menubar = new JMenuBar();
        menubar.add(new FileMenu(this));
        menubar.add(new EditMenu());
        //menubar.add(new MapViewMenu());
        if (App.platform.isMac())
            menubar.add(new WindowMenu(this));
        menubar.add(new HelpMenu());
        setJMenuBar(menubar);
        
        // set size, and show it
        pack();
        setSize(new Dimension(700, 400));
        Center.center(this);
        setVisible(true);
	}
	
	@Override
	public void setTitle(String appendage) {
		if(appendage != null)
			super.setTitle("Site List " + appendage + " - " + Build.VERSION + " " + Build.TIMESTAMP);
		else
			super.setTitle("Site List - " + Build.VERSION + " " + Build.TIMESTAMP);
	}
	
	public List getImmutableSitelist() {
		return mySites;
	}

	private void createImmutableSitelist() {
		List refSites = LegacySiteDB.getSiteDB().sites;
		mySites = new ArrayList(refSites.size());
		
		// our list of sites should be immutable!
		mySites = new ArrayList(refSites.size());
		for(int i = 0; i < refSites.size(); i++)
			mySites.add(((LegacySite)refSites.get(i)).clone());
	}
	
	private List mySites; // immutable site list
	
	// saveabledocument interface
	public boolean isSaved() {
		return !sitePanel.isDataModified();
	}
	
	public String getFilename() {
		return null;
	}
	
	public void setFilename(String x) {
		// don't do anything.
	}
	
	public boolean isNameChangeable() {
		// this is the Site DB. We can't save as.
		return false;
	}
	
	public String getDocumentTitle() {
		return "Corina Site Database";
	}
	
	public void save() {
		// set our sites to be the global list of sites
		LegacySiteDB.getSiteDB().sites = mySites;
		
		// create a new cloned list of sites (from the ones we just installed!)
		createImmutableSitelist();
		sitePanel.reloadSitelist();

		// Save the new site list to disk
		// if we're successful, 
		// notify that we're no longer modified, plus we should redraw.
		if(LegacySiteDB.getSiteDB().save())
			sitePanel.setDataModified(false);		
	}
	
	public Object getSaverClass() {
		return this;
	}
}
