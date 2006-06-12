/**
 * 
 */
package corina.site;

import java.awt.Dimension;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuBar;
import javax.swing.JScrollPane;

import corina.Build;
import corina.core.App;
import corina.gui.XFrame;
import corina.gui.menus.*;
import corina.util.*;
import corina.gui.SaveableDocument;

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
		List refSites = SiteDB.getSiteDB().sites;
		mySites = new ArrayList(refSites.size());
		
		// our list of sites should be immutable!
		mySites = new ArrayList(refSites.size());
		for(int i = 0; i < refSites.size(); i++)
			mySites.add(((Site)refSites.get(i)).clone());
	}
	private List mySites; // immutable site list
	
	// saveabledocument interface
	public boolean isSaved() {
		return !sitePanel.isDataModified();
	}
	
	public String getFilename() {
		return "xxxxx";
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
		SiteDB.getSiteDB().sites = mySites;
		// create a new cloned list of sites (from the ones we just installed!)
		createImmutableSitelist();

		// Save the new site list to disk
		// if we're successful, 
		// notify that we're no longer modified, plus we should redraw.
		if(SiteDB.getSiteDB().save())
			sitePanel.setDataModified(false);		
	}
}
