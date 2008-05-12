package edu.cornell.dendro.corina.map;

import edu.cornell.dendro.corina.site.LegacySite;
import edu.cornell.dendro.corina.site.LegacySiteDB;

import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.HashSet;

/**
   A way to organize Sites to draw on the map.

   <p>A LabelSet object will keep track of:</p>
   <ul>
     <li>which sites are visible
     <li>which sites are selected
     <li>which groups of sites are in the same location on the map
     <li>for each location (site or group of sites), where the label should be drawn
   </ul>

   <h2>Left to do:</h2>
   <ul>
     <li>Write the selection stuff
     <li>Write the offset stuff
     <li>Move getLocations() to Site?  (Law of Demeter.)  Or better: make it getVisibleLocations().
   </ul>
 
   @see edu.cornell.dendro.corina.site.LegacySite

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class LabelSet {

    /* DOCUMENT ME! -- does this need to be its own class? */
    public static class Offset {
        public float angle = 0; // angle, in ???
        public float distance = 0; // distance, in ???
    }
    
    /** Make a new LabelSet. */
    public LabelSet() {
        // WRITEME: what's a default label set?

        // everything visible
        showAllSites();
    }
    
    public LabelSet(List sitelist) {
        Iterator all = sitelist.iterator();
        while (all.hasNext()) {
            LegacySite site = (LegacySite) all.next();
            if (site.getLocation() != null) {
                visibleSites.add(site);
                visibleLocations.add(site.getLocation());
            }
        }    	
    }

    /**
       Get all of the Locations used by Sites.

       <p>If you're an SQL junkie, this is pretty much like
       <code>SELECT location FROM sites GROUP BY location</code>
       (I think).</p>

       @return an Iterator for all of the Locations
    */
    public Iterator getLocations() {
    	/*
        // TODO: WRITEME
        return new Iterator() {
            public boolean hasNext() {
                // TODO: WRITEME
                return false;
            }
            public Object next() {
                // TODO: WRITEME
                return null;
            }
            public void remove() {
                // TODO: WRITEME
            }
        };
        */
    	return visibleLocations.iterator();
    }

    //
    // VISIBILITY
    //

    private Set visibleSites = new HashSet(); // TODO: default should be all, not none, right?
    private Set visibleLocations = new HashSet();

    /**
       Is this site visible?

       @param site the Site to check
       @return true, if the site is visible, else false
    */
    public boolean isVisible(LegacySite site) {
        return visibleSites.contains(site);
    }

    /**
       Change a site's visibility.

       @param site the Site to change
       @param visible if true, set it to visible; if false, invisible
    */
    public void setVisible(LegacySite site, boolean visible) {
        if (visible) {
        	visibleSites.add(site);
            visibleLocations.add(site.getLocation());
        }
        else {
            visibleSites.remove(site);
            visibleLocations.remove(site.getLocation());
        }
    }

    /**
       Count the number of visible sites.

       @return the number of visible sites
    */
    public int countVisibleSites() {
        return visibleSites.size();
    }

    public void showAllSites() {
        // add all sites from the site db which have a location.
        // (if i didn't check if it had a location, the user would
        // see sites becoming checked that the user couldn't check
        // himself, which would be weird -- but SitesLayer double-checks
        // anyway, so it wouldn't really hurt anything.)
        // since it's a set, so it doesn't matter that some of these
        // are probably already there.
        Iterator all = LegacySiteDB.getSiteDB().sites.iterator();
        while (all.hasNext()) {
            LegacySite site = (LegacySite) all.next();
            if (site.getLocation() != null && site.getLocation().valid()) {
                visibleSites.add(site);
                visibleLocations.add(site.getLocation());
            }
        }
    }
    public void hideAllSites() {
        visibleSites.clear();
        visibleLocations.clear();
    }
    
    public void rehashLocations() {
    	visibleLocations.clear();
    	Iterator iter = visibleSites.iterator();
    	
    	while(iter.hasNext()) {
    		visibleLocations.add(((LegacySite)iter.next()).getLocation());
    	}
    }
    
    //
    // SELECTION
    //

    private Set selectedSites = new HashSet(); // TODO: default should be all, not none, right?

    /**
        Is this site selected?

     @param site the Site to check
     @return true, if the site is visible, else false
     */
    public boolean isSelected(LegacySite site) {
        return selectedSites.contains(site);
    }

    /**
       Select or deselect a site.

       @param site the Site to change
       @param selected if true, select it; if false, deselect it
    */
    public void setSelected(LegacySite site, boolean selected) {
        if (selected)
            selectedSites.add(site);
        else
            selectedSites.remove(site);
    }

    /**
        Count the number of selected sites.

       @return the number of selected sites
    */
    public int countSelectedSites() {
        return selectedSites.size();
    }

    public void selectAllSites() {
        Iterator all = LegacySiteDB.getSiteDB().sites.iterator();
        while (all.hasNext()) {
            LegacySite site = (LegacySite) all.next();
            if (site.getLocation() != null)
                selectedSites.add(site);
        }
    }

    public void deselectAllSites() {
        selectedSites.clear();
    }

    public Iterator getSelectedSites() {
        return selectedSites.iterator(); // BUG?: does this iterator have a remove() method?
    }
}
