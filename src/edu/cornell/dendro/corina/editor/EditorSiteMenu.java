package edu.cornell.dendro.corina.editor;

import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.tridas.LegacySite;
import edu.cornell.dendro.corina.tridas.LegacySiteDB;
import edu.cornell.dendro.corina.tridas.SiteInfoDialog;
import edu.cornell.dendro.corina.tridas.SiteNotFoundException;
import edu.cornell.dendro.corina.map.MapFrame;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.gui.Bug;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

public class EditorSiteMenu extends JMenu {

    // note: this menu isn't a sample listener.  it's tricky, because
    // it depends more on the sitedb than the sample.  should it be
    // a samplelistener and a sitelistener, then?  maybe.

    private Sample sample;

    public EditorSiteMenu(Sample s) {
	super(I18n.getText("site")); // i18n -- mnemonic!

	this.sample = s;

	// map
	JMenuItem map = Builder.makeMenuItem("map");
	map.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    try {
			// get site
			LegacySite mySite = LegacySiteDB.getSiteDB().getSite(sample);

			// draw map there
			new MapFrame(mySite, mySite);
			// no MapFrame(Site) yet, but this is almost as good

		    } catch (SiteNotFoundException snfe) {
			Alert.error("No Site Found",
				    "Couldn't find a site for this sample.");
		    } catch (Exception e) {
			Bug.bug(e); // WHY?
		    }
		}
	    });
	add(map);

	// site properties
	JMenuItem props = Builder.makeMenuItem("properties");
	props.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    try {
			// get my site -- FIXME: LoD says this should be sample.getSite();
			LegacySite mySite = LegacySiteDB.getSiteDB().getSite(sample);

			// show props
			new SiteInfoDialog(mySite, null);
			// FIXME: null=center on screen; shouldn't i center on the editor?
		    } catch (SiteNotFoundException snfe) {
			Alert.error("No Site Found",
				    "Couldn't find a site for this sample.");
		    }
		}
	    });
	add(props);
    }
}
