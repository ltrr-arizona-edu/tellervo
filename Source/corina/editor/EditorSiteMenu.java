package corina.editor;

import corina.Sample;
import corina.site.Site;
import corina.site.SiteDB;
import corina.site.SiteInfoDialog;
import corina.site.SiteNotFoundException;
import corina.map.MapFrame;
import corina.ui.Builder;
import corina.ui.I18n;
import corina.ui.Alert;
import corina.gui.Bug;

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
			Site mySite = SiteDB.getSiteDB().getSite(sample);

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
			Site mySite = SiteDB.getSiteDB().getSite(sample);

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
