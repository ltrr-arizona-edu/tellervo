//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package corina.site;

import corina.map.View; // not sure what should be in site and what should be in map ...
import corina.map.MapPanel;
import corina.map.Location;
import corina.map.SiteRenderer;
import corina.gui.ButtonLayout;
import corina.gui.DialogLayout;
import corina.util.JLine;
import corina.util.Platform;
import corina.util.OKCancel;
import corina.util.JDisclosureTriangle;
// REFACTOR: jline and the layouts should be in the same package...

import java.util.Properties;
import java.util.Arrays;
import java.util.Enumeration;

import java.io.File;
import java.io.IOException;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/*
 TODO for the "Site Info" dialog:
 -- update the database when any field is changed by the user (and save!)
 -- update the database when any popup is changed by the user: country, ? (and save!)
 -- update the database when any checkbox is changed by the user: type, ? (and save!)
 -- (update data: type="1,4" => type="FU")
 -- make "species" less free-form?
 -- implement "change..."
 -- implement Platform.isPC, enable/test win32 "open..." command
 -- add "show on map"?  ("hide others"?) ...
 */

/*
 stuff the user should be able to do from the sitedb (why's this here?):
  - view all sites; sort by ...
  - search for sites by name
  - add sites
  - edit sites
  - make a map of a site
  - given a site, find nearby sites
*/

public class SiteInfo {
    // a singleton dialog
    private static JDialog diag;
    static {
        diag = new JDialog(new JFrame(), "Site Info"); // (title gets overwritten)
        diag.setResizable(false);
        // first-time only, put on right edge of screen -- how?  if moved, save position?
    }

    // a site icon
    static class SiteIcon implements Icon {
        private final static int WIDTH = 48;
        private final static int HEIGHT = 32;
	public int getIconHeight() {
	    return HEIGHT; }
	public int getIconWidth() {
	    return WIDTH; }
	public void paintIcon(Component c, Graphics g, int x, int y) {
            View view = new View(); // default view is fine -- SHOULDN'T BE NEEDED AT ALL (why not?)

            MapPanel.setFontForLabel(g, view);
            Font f = g.getFont();
            g.setFont(new Font(f.getName(), f.getStyle(), (int) (f.getSize() * 1.75))); // ugly!  was: 1.4
            g.setColor(site.getSiteColor());

            SiteRenderer.drawLabel((Graphics2D) g, new Point(1, 1), site, 1, view,
                                   new Point(WIDTH/2, HEIGHT/2), false); }
        public SiteIcon(Site site) {
            this.site = site; }
        public void setSite(Site site) {
            this.site = site; }
        private Site site = null; }
    static SiteIcon siteIcon = new SiteIcon(null);

    // web cam
    private static JPanel webcam = WebCam.makeVeniceWebcam();

    // ------------------------------------------------------------------------------------------------------------------------
    // panel to show for 1 site
    // ------------------------------------------------------------------------------------------------------------------------
    private static JPanel singlePanel;
    private static Site site;
    private static JLabel siteName;
    private static JTextField nameField;
    private static JTextField codeField;
    private static JTextField idField;
    private static JTextField locationField;
    private static JTextField altitudeField;
    private static JComboBox countryPopup;
    private static JTextField speciesField;
    private static JCheckBox typeForest, typeMedieval, typeAncient, typeUnknown;
    private static JTextArea commentsField;
    private static JLabel sizeLabel;
    private static JLabel folderField;
    //private static
    // ADD widgets here...
    private static boolean locationListenerDisabled=false;
    static {
        siteName = new JLabel(); // FIXME: use 2 lines, if necessary, but after that just use an elipsis -- break lines at words
        
        // create my widgets
        nameField = new JTextField("", 16);
        codeField = new JTextField("", 4);
        idField = new JTextField("", 4);
        locationField = new JTextField("", 10);
        altitudeField = new JTextField("", 5);

        // i dunno what the fuck this crap does today, please document
        String codes[] = SiteDB.getSiteDB().getCountries();
        String countries[] = new String[codes.length];
        for (int i=0; i<codes.length; i++)
            countries[i] = Country.getName(codes[i]);
        Arrays.sort(countries);
        String entries[] = new String[2 + countries.length];
        entries[0] = "- not specified -";
        for (int i=0; i<countries.length; i++)
            entries[i+1] = countries[i];
        entries[entries.length-1] = "Other...";
        countryPopup = new JComboBox(entries);
        countryPopup.setMaximumRowCount(50); // don't introduce scrollbars unless i really need 'em
        /*
         // will be:
         countryPopup = new JCodedPopup(new String[] { null, "- not specified -", "Tur
         -- list every country for which there's a sample (plus some user-defined alway-on ones? -- no, an "other" block.
         */

        speciesField = new JTextField("", 16); // temporary (heh)
        typeForest = new JCheckBox("Forest");
        typeMedieval = new JCheckBox("Medieval");
        typeAncient = new JCheckBox("Ancient");
        typeUnknown = new JCheckBox("Unknown");
        commentsField = new JTextArea(5, 32);
        sizeLabel = new JLabel();
        folderField = new JLabel(""); // make editable on win32?

        // temporary things that i won't need to change later
        JButton changeButton = new JButton("Change...");
        JButton openButton = new JButton("Open in " + (Platform.isMac ? "Finder" : "Explorer")); // AAH, REFACTOR: add interface for platform-specific file manager: getname, opendir.  (mac->finder, win32->explorer, unix->...)

	// add listener: if code/name changes, update label
	SiteDB.getSiteDB().addSiteDBListener(new SiteDBAdapter() {
		public void siteCodeChanged(SiteEvent e) {
		    if (e.getSource() == site) {
			siteName.repaint(); // it reads site directly, so i just need to force a redraw
			diag.setTitle(site.getCode() + " Info"); } }
		public void siteNameChanged(SiteEvent e) {
		    if (e.getSource() == site) {
			siteName.setText(site.getName());
			siteName.repaint(); } } });

	// add listener: icon/label is drag source -- (Q: what exactly do you want to drag?)
	// WRITEME

	// add listener: if name changes, update db
	nameField.getDocument().addDocumentListener(new DocumentListener() {
		public void changedUpdate(DocumentEvent e) { update(); }
		public void insertUpdate(DocumentEvent e) { update(); }
		public void removeUpdate(DocumentEvent e) { update(); }
		private void update() {
		    // why's this get called on creation?
		    if (site == null)
			return;

		    // (POSSIBLE BUG: need to watch for infinite loops here?)

		    site.setName(nameField.getText());
		    SiteDB.getSiteDB().fireSiteNameChanged(site); } });

	// add listener: if code changes, update db
	codeField.getDocument().addDocumentListener(new DocumentListener() {
		public void changedUpdate(DocumentEvent e) { update(); }
		public void insertUpdate(DocumentEvent e) { update(); }
		public void removeUpdate(DocumentEvent e) { update(); }
		private void update() {
		    // why's this get called on creation?
		    if (site == null)
			return;

		    // (POSSIBLE BUG: need to watch for infinite loops here?)

		    site.setCode(codeField.getText());
		    SiteDB.getSiteDB().fireSiteCodeChanged(site);
		}
	    });

	// add listener: if id changes, update db
	idField.getDocument().addDocumentListener(new DocumentListener() {
		public void changedUpdate(DocumentEvent e) { update(); }
		public void insertUpdate(DocumentEvent e) { update(); }
		public void removeUpdate(DocumentEvent e) { update(); }
		private void update() {
		    // why's this get called on creation?
		    if (site == null)
			return;

		    // (POSSIBLE BUG: need to watch for infinite loops here?)

		    site.setID(idField.getText());
		    SiteDB.getSiteDB().fireSiteIDChanged(site);
		}
	    });

	// add listener: if location changes, update text
	SiteDB.getSiteDB().addSiteDBListener(new SiteDBAdapter() {
		public void siteMoved(SiteEvent e) {
		    Site s = (Site) e.getSource();
		    locationListenerDisabled = true;
		    locationField.setText(s.getLocation()==null ? "" : s.getLocation().toString());
		    locationListenerDisabled = false;
		}
	    });

	// REFACTOR: want to be able to use either (1) one info
	// window, or (2) many info windows, from the same codebase.
	// so i'll need SiteInfo(Site) and setSite(Site), and static
	// SiteInfo.show(Site).

	// add listener: if text changes, update db
	// PROBLEM: don't want to redraw map every keypress, perf won't allow it
	// instead, every /n/ seconds, or on RET only?
	locationField.getDocument().addDocumentListener(new DocumentListener() {
		public void changedUpdate(DocumentEvent e) { update(); }
		public void insertUpdate(DocumentEvent e) { update(); }
		public void removeUpdate(DocumentEvent e) { update(); }
		private void update() {
		    // why's this get called on creation?
		    if (site == null)
			return;

		    // prevent infinite loops, which are bad
		    if (locationListenerDisabled)
			return;

		    System.out.println("location text updated");

		    site.setLocation(new Location(locationField.getText()));
		    SiteDB.getSiteDB().fireSiteMoved(site);
		}		
	    });

        // add listener: "other..." => bring up dialog
        // REFACTOR: abstract me away!  (country+countrypopup+countrydialog?)
        countryPopup.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // why's this get called on creation?
                if (site == null)
                    return;
                
                int index = countryPopup.getSelectedIndex();

                // --n/s--
                if (index == 0) {
                    site.setCountry(null);
		    SiteDB.getSiteDB().fireSiteCountryChanged(site);
                    return;
                }

                // "Other..."
                if (index == countryPopup.getItemCount()-1) {
                    // this returns a code, or null
                    site.setCountry(CountryDialog.showDialog(diag, site.getCountry()));
		    SiteDB.getSiteDB().fireSiteCountryChanged(site);

                    // whatever it is, it can't stay on "Other...".  select "-n/s-", whatever it is, or add it to the popup.

                    // case 0: it's <none>.  easy.
                    if (site.getCountry() == null) {
                        countryPopup.setSelectedIndex(0);
                        return;
                    }

                    // case 1: it's a species in the list.  not too hard.
                    String name = Country.getName(site.getCountry());
                    for (int i=1; i<countryPopup.getItemCount(); i++) {
                        if (countryPopup.getItemAt(i).equals(name)) {
                            countryPopup.setSelectedIndex(i);
                            return;
                        }
                    }

                    // case 2: it's a species not in the list.  ugh.
                    for (int i=1; i<countryPopup.getItemCount(); i++) {
                        if (((String) countryPopup.getItemAt(i)).compareTo(name) > 0)
                            break;
                    }
                    countryPopup.insertItemAt(name, index);
                    countryPopup.setSelectedIndex(index);

                    // MAYBE: do i need to add it to some other list now?
                    // need to update popup and list when country is chosen?
                    // -- need to remove item from list if last item from country is removed.  oy...
                    return;
                }

                // user selected a country from the popup.  which?
                String name = (String) countryPopup.getItemAt(index);

                // get code that matches name, return that
                site.setCountry(Country.getCode(name));
            }
        });

        // add listener: "change..." => choose dir
        // WRITE ME

        // add listener: "open..." => open in finder
        openButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                final String dir = site.getFolder(); // folderField.getText();
                String abs = System.getProperty("corina.dir.data") + File.separator + dir;
                File f = new File(abs);
                if (f.exists() && f.isDirectory()) {
                    if (Platform.isMac) {
                        try {
                            Runtime.getRuntime().exec(new String[] { "/usr/bin/open", abs });
                        } catch (IOException ioe) {
                            // shouldn't happen
                        }
                    } else if (Platform.isWindows) {
                        try {
                            Runtime.getRuntime().exec(new String[] { "c:\\winnt\\system32\\cmd.exe", "/c", "start", abs });
                        } catch (IOException ioe) {
                            // shouldn't happen
                        }
//                    } else if (Platform.isUnix) {
                        // xterm!  (gnome/kde folks, please add proper hooks here)
                        // -- just dim it if you can't find something decent to use.
//                    }
                }
                } else {
                    // whoops...
                }
            }
        });

	// add listener: type in comments => update db
	commentsField.getDocument().addDocumentListener(new DocumentListener() {
		public void changedUpdate(DocumentEvent e) { update(); }
		public void insertUpdate(DocumentEvent e) { update(); }
		public void removeUpdate(DocumentEvent e) { update(); }
		private void update() {
		    // why's this get called on creation?
		    if (site == null)
			return;

		    // update db
		    site.setComments(commentsField.getText());
		    SiteDB.getSiteDB().fireSiteCommentsChanged(site); }});

	// WRITEME: add sitedblistener: when comments changes, update this field
	// (not very urgent) -- (will need to update comments->sitedb listener, for that, too)

        // lay everything out
	singlePanel = new JPanel();
	singlePanel.setLayout(new BoxLayout(singlePanel, BoxLayout.Y_AXIS));
        // singlePanel.setBorder(BorderFactory.createEmptyBorder(14, 20, 20, 20)); // (standard border)

	// icon and title
	siteName.setIcon(siteIcon);
	{ // border on a jlabel borders the text, but not the icon (ugh!)
	    JPanel nest = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    nest.add(siteName);
	    nest.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8)); // 14, 20, 20, 20)); // (standard border)
	    singlePanel.add(nest);
	}
	// singlePanel.add(siteName);

	addSectionSpacing(singlePanel);

	// section: name & id
	{
	    JPanel name = new JPanel(new DialogLayout());
	    name.add(nameField, "Name:");
	    name.add(codeField, "Code:");
	    name.add(idField, "ID:");
	    name.add(Box.createVerticalStrut(8), null);

	    JDisclosureTriangle triangle = new JDisclosureTriangle("Name & ID:", name, diag);
	    singlePanel.add(triangle);
	}

	addSectionSpacing(singlePanel);

	{ // section: location
	    JPanel group = new JPanel(new DialogLayout());

	    group.add(locationField, "Location:");
	    {
		JPanel fff = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		fff.add(altitudeField);
		fff.add(Box.createHorizontalStrut(8)); // 8 ok?
		fff.add(new JLabel(" meters"));
		group.add(fff, "Altitude:");
	    }
	    group.add(countryPopup, "Country:");
	    group.add(Box.createVerticalStrut(8), null);

	    JDisclosureTriangle triangle = new JDisclosureTriangle("Location:", group, diag);
	    singlePanel.add(triangle);
	}

	addSectionSpacing(singlePanel);

	{ // section: type
	    JPanel group = new JPanel(new DialogLayout());

	    group.add(speciesField, "Species:");
	    group.add(typeForest, "Epoch:");
	    group.add(Box.createVerticalStrut(4), null); // (8)
	    group.add(typeMedieval, "");
	    group.add(Box.createVerticalStrut(4), null); // (8)
	    group.add(typeAncient, "");
	    group.add(Box.createVerticalStrut(4), null); // (8)
	    group.add(typeUnknown, "");
	    group.add(Box.createVerticalStrut(8), null);

	    JDisclosureTriangle triangle = new JDisclosureTriangle("Type:", group, diag);
	    singlePanel.add(triangle);
	}

	addSectionSpacing(singlePanel);

	{
	    JPanel group = new JPanel(new DialogLayout());

	    // group.add(sizeLabel, "Size:");
	    {
		JPanel fff = new JPanel();
		fff.setLayout(new BoxLayout(fff, BoxLayout.X_AXIS));
		fff.add(folderField);
		fff.add(changeButton);
		group.add(fff, "Folder:");
	    }
	    group.add(openButton, "");
	    group.add(Box.createVerticalStrut(8), null);

	    JDisclosureTriangle triangle = new JDisclosureTriangle("Samples:", group, diag, false);
	    singlePanel.add(triangle);

	    // this is a BAD INTERFACE.
	    // what should it be?

	    // Samples for this site are stored in the folder:
	    //    {...}
	    //    (contains {...} files)
	    // (Change...) (Open in Finder)
	}

	addSectionSpacing(singlePanel);

	// section: masters
	JList mastersList = new JList();
	JScrollPane mastersScroller = new JScrollPane(mastersList);
	forceToDefaultSize(mastersScroller, mastersList);
	JDisclosureTriangle masters = new JDisclosureTriangle("Masters:", mastersScroller, diag, false);
	// WRITEME: implement!
	singlePanel.add(masters);
	// WRITEME: make "masters" triangle-label draggable?

	addSectionSpacing(singlePanel);

	// section: non-fits
	JList nonfitsList = new JList();
	JScrollPane nonfitsScroller = new JScrollPane(nonfitsList);
	forceToDefaultSize(nonfitsScroller, nonfitsList);
	JDisclosureTriangle nonfits = new JDisclosureTriangle("Non-Fits:", nonfitsScroller, diag, false);
	// WRITEME: implement!
	singlePanel.add(nonfits);
	// WRITEME: make "non-fits" triangle-label draggable?

	addSectionSpacing(singlePanel);

	// section: comments
	JScrollPane commentsScroller = new JScrollPane(commentsField);
	forceToDefaultSize(commentsScroller, commentsField);
	JDisclosureTriangle comments = new JDisclosureTriangle("Comments:", commentsScroller, diag, false);
	// WRITEME: implement!
	singlePanel.add(comments);

	/*
	addSectionSpacing(singlePanel);

	// section: completely gratuitous webcam feature
	JDisclosureTriangle live = new JDisclosureTriangle("Live View:", webcam, diag);
	singlePanel.add(live);
	*/
    }

    private static void addSectionSpacing(JPanel p) {
        // p.add(Box.createVerticalStrut(4));
        p.add(new JLine());
        // p.add(Box.createVerticalStrut(8));
    }
    private static void forceToDefaultSize(JComponent target, Scrollable source) {
	target.setMinimumSize(source.getPreferredScrollableViewportSize());
	target.setPreferredSize(source.getPreferredScrollableViewportSize());
	target.setMaximumSize(source.getPreferredScrollableViewportSize());
    }

    // --------------------------------------------------------------------------------------------
    // one site
    // --------------------------------------------------------------------------------------------
    public static void showInfo(Site s) {
        // hmm...
        siteIcon.setSite(s);
        siteName.setText(s.getName());
        diag.setTitle(s.getCode() + " Info");

        // set the widgets with the data
        nameField.setText(s.getName());
        codeField.setText(s.getCode());
        idField.setText(s.getID());
        locationField.setText(s.getLocation()==null ? "" : s.getLocation().toString());
        altitudeField.setText(s.altitude==null ? "" : s.altitude.toString());
        if (s.getCountry() == null) {
            countryPopup.setSelectedIndex(0);
        } else {
	    // REFACTOR: extract class - CountryPopup?
	    // (this loop selects the entry in the popup
            for (int i=1; i<countryPopup.getItemCount()-1; i++) {
                if (Country.getName(s.getCountry()).equals(countryPopup.getItemAt(i))) {
                    countryPopup.setSelectedIndex(i);
                    break;
                }
            }
        }
        speciesField.setText(s.species);
        // type
        int intType;
        try {
            intType = Integer.parseInt(s.type);
        } catch (NumberFormatException nfe) {
            intType = Site.TYPE_UNKNOWN;
        }
        typeForest.setSelected(intType == Site.TYPE_FOREST);
        typeMedieval.setSelected(intType == Site.TYPE_MEDIEVAL);
        typeAncient.setSelected(intType == Site.TYPE_ANCIENT);
        typeUnknown.setSelected(intType == Site.TYPE_UNKNOWN); // ack!
        // comments -- inline, or README files?
        // size -- tricky...
        folderField.setText(s.getFolder());

        // record the site, so the listeners know who to update
        site = s;

        // use this panel
        diag.setContentPane(singlePanel);

        // show the dialog
        diag.pack();
        diag.show();
    }

    // ------------------------------------------------------------------------------------------------
    // panel to show for many sites
    // ------------------------------------------------------------------------------------------------
    private static JPanel multiPanel;
    private static JLabel label;
    private static JList list;
    static {
        label = new JLabel("", JLabel.CENTER); // text gets set later
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        list = new JList(); // contents get set later

        // doo all the layout now
        multiPanel = new JPanel();
        multiPanel.setLayout(new BoxLayout(multiPanel, BoxLayout.Y_AXIS));
        multiPanel.setBorder(BorderFactory.createEmptyBorder(24, 20, 20, 20));

        multiPanel.add(label);
        multiPanel.add(Box.createVerticalStrut(8));
        multiPanel.add(new JScrollPane(list));
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // many sites
    // ------------------------------------------------------------------------------------------------------------------------
    public static void showInfo(Site s[]) {
        // set the label
        label.setText(s.length + " sites are selected"); // i18n!

        // set the list data
        final Site glue[] = s;
        list.setModel(new AbstractListModel() {
            public Object getElementAt(int index) {
                return glue[index].getName();
            }
            public int getSize() {
                return glue.length;
            }
        });

        // use this panel
        diag.setContentPane(multiPanel);

        // show the dialog
        diag.show();
    }

        // id
        // name
        // code
        // id
        // ---
        // location
        // location
        // country
        // ---
        // data
        // species -- how to deal with multiple?  (just use blank for now)
        // type
        // comments
        // ---
        // files
        // folder
        // change/open

        // pack and show

    // events:
    // -- typed => change site
    // -- selected => change site
    // -- background thread to rewrite db every 10s iff change made?
    // -- (what if user quits?)

    // what i'll need later:
    // -- click on a site, put data here
    // so maybe more of a "static showInfo()" method.

    //
    // testing
    //
    public static void main(String args[]) {
        Site s[] = new Site[5];
        for (int i=0; i<5; i++) {
            s[i] = new Site();
            s[i].setName("site number " + i);
        }
        showInfo(s[0]);
/*
        final Site[] glue = s;
        (new Thread() {
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (Exception e) { } // ignore
                showInfo(glue);
            }
        }).start();
 */
    }
}
