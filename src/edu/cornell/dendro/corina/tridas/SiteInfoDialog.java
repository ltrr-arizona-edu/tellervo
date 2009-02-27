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

package edu.cornell.dendro.corina.tridas;

import edu.cornell.dendro.corina.gui.Layout;
import edu.cornell.dendro.corina.gui.Help;
import edu.cornell.dendro.corina.util.OKCancel;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.ui.Builder;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
   A dialog which shows Site info, and lets the user edit it.

<p>TODO:
<ul>
   <li>make 'ok' put data back into site object: location, altitude,
   folder

   <li>put folder line at bottom: list folder, and "change..." button
   <li>also for folder line, allow open-in-finder, open-in-browser

   <li>keep track of which sites (by hash) are being info'd, and disallow dupes
   <li>sitedb shouldn't ever be explicitly named.  Site.getSite("ZKB").
       (it should also be able to be database- or file-backed.)
   <li>site shouldn't return null
   <li>wrap comments, so the horiz scrollbar is never needed
   <li>convert * to degree sign in location field as-you-type?
   <li>"type" and "species" words aren't lined up properly (altitude,
   either: the []-meters and ancient-medieval-forest-unknown panels
   have nonzero padding, i think)
   <li>refactor and javadoc
   <li>add "help" button?
   <li>i18n of folder stuff
   <li>i18n of names of all countries?  (nobody will translate them all, but they could translate some common ones)
   <li>add "import from gps" feature (sets loc, alt)
   <li>put "folder" stuff on its own tab?
   <li>make "masters"/"nonfits" on a tab (or 2 tabs)?
</ul>

   @see edu.cornell.dendro.corina.tridas.CountryPopup
   @see edu.cornell.dendro.corina.tridas.Country

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class SiteInfoDialog extends JDialog {

	private static Component strutH(int width) {
		return Box.createHorizontalStrut(width);
	}

	private LegacySite site;
	private LegacySite originalSite;

	private JTextField name, code, id;

	// ADDME: location, altitude
	private CountryPopup country;

	private JTextField species, location, altitude;

	private JCheckBox ancient, medieval, forest, unknown;

	private JTextArea comments;
	
	private JButton location_edit;
	
	private JTextField folder;
	
	private boolean doSave = false;

	// ADDME: folder

	// display info for |site|;
	// center over |window|, or on screen if null.
	public SiteInfoDialog(LegacySite infosite, Window window) {
		super((Frame) window, infosite.getName(), true);

		// save site reference
		this.site = infosite;
		this.originalSite = (LegacySite) infosite.clone();

		// set dialog title
		setTitle(site.getName());

		// id line
		name = new JTextField(site.getName(), 40);
		code = new JTextField(site.getCode(), 3);
		id = new JTextField(site.getId(), 3);

		JPanel line_1 = Layout.flowLayoutL(labelOnTop("site_name", name),
				strutH(12), labelOnTop("site_code", code), strutH(12),
				labelOnTop("site_id", id));

		// location line
		// FIXME: getLocationAsString, getAltitudeAsString()
		location = new JTextField(site.getLocation() == null ? "" : site
				.getLocation().toString(), 15);
		location.setEditable(false);
		
		location_edit = new JButton("Change");
		location_edit.setFont(location_edit.getFont().deriveFont(9.0f));
		location_edit.setPreferredSize(new Dimension(20, 20));
		final JDialog _parent = this;
		location_edit.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				new LocationEditorDialog(site, _parent);
				location.setText(site.getLocation() == null ? "" : site.getLocation().toString());
			}
		});
		
		
		altitude = new JTextField(site.getAltitude() == null ? "" : site
				.getAltitude().toString(), 5);
		country = new CountryPopup(this, site.getCountry());

		JPanel altitude2 = Layout.flowLayoutL(altitude, new JLabel(" "
				+ I18n.getText("meters")));

		JPanel line_2 = Layout.flowLayoutL(
				labelOnTop("site_location", location), 
				labelOnTopNotKey(" ", location_edit),
				strutH(12), 
				labelOnTop("site_altitude", altitude2), 
				strutH(12), 
				labelOnTop("site_country", country));

		// type line
		species = new JTextField(site.getSpecies(), 20);

		boolean types[] = site.getTypes();
		ancient = new JCheckBox(I18n.getText("site_ancient"), types[0]);
		medieval = new JCheckBox(I18n.getText("site_medieval"), types[1]);
		forest = new JCheckBox(I18n.getText("site_forest"), types[2]);
		unknown = new JCheckBox(I18n.getText("site_unknown"), types[3]);
		JPanel type = Layout.flowLayoutL(ancient, medieval, forest, unknown);

		JPanel line_3 = Layout.flowLayoutL(labelOnTop("site_species", species),
				strutH(12), labelOnTop("site_type", type));

		folder = new JTextField(site.getFolder(), 40);
		JPanel line_4 = Layout.flowLayoutL(labelOnTop("storage_path", folder));
		
		// comments line
		comments = new JTextArea(site.getComments(), 3, 50);

		JPanel line_5 = Layout.flowLayoutL(labelOnTop("site_comments",
				new JScrollPane(comments)));

		// put them all together
		JPanel content = Layout.boxLayoutY(line_1, line_2, line_3, line_4, line_5);

		// buttons line
		JButton help = Builder.makeButton("help");
		Help.addToButton(help, "editing_site_info");
		JButton cancel = Builder.makeButton("cancel");
		final JButton ok = Builder.makeButton("ok");
		AbstractAction buttonAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				// if 'ok' clicked, writeback
				boolean kill = true;
				if (e.getSource() == ok) {
					if (!writeback())
						kill = false;
					else
						doSave = true; // we updated the site.
				} 

				// close this dialog if we're told to kill
				if (kill)
					dispose();
			}
		};
		cancel.addActionListener(buttonAction);
		ok.addActionListener(buttonAction);

		JPanel buttons = Layout.buttonLayout(help, null, cancel, ok);
		buttons.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		// everything together
		JPanel everything = Layout.borderLayout(null, null, content, null,
				buttons);
		everything.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setContentPane(everything);

		name.selectAll();
		setResizable(false);
		OKCancel.addKeyboardDefaults(ok);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		pack();
		if (window != null)
			Center.center(this, window);
		else
			Center.center(this); // (on screen)
		show();
	}

	private boolean writeback() {
		Location loc;
		Integer alt;

		try {
			// try to parse location, if it's set at all.
			String text = location.getText();
			if(text.length() == 0)
				loc = null;
			else
				loc = new Location(text);
		} catch (NumberFormatException nfe) {
			JOptionPane.showMessageDialog(this, "Invalid location set.");
			return false;
		}
		
		try {
			String text = altitude.getText();
			if(text.length() == 0)
				alt = null;
			else
				alt = Integer.parseInt(text);
		} catch (NumberFormatException nfe) {
			JOptionPane.showMessageDialog(this, "Invalid altitude, must be numeric!");
			return false;
		}

		// - name, code, id
		site.setName(name.getText());
		site.setCode(code.getText());
		site.setId(id.getText());

		// - location, altitude, country
		site.setLocation(loc);
		site.setAltitude(alt);
		site.setCountry(country.getCountry());

		// - species, type
		site.setSpecies(species.getText());
		site.setTypes(new boolean[] { ancient.isSelected(),
				medieval.isSelected(), forest.isSelected(),
				unknown.isSelected() });

		// - comments
		site.setComments(comments.getText());
		
		// TODO: save this site now
		// future: site.getStorage().save(site);
		// no, don't do this. let whatever is calling our panel save it.

		return true;
	}

	// (note: label is actually an i18n key!)
	private static JComponent labelOnTop(String key, JComponent component) {
		String text = I18n.getText(key) + ":";

		return Layout.borderLayout(new JLabel(text), null, component, null,
				null);
	}

	private static JComponent labelOnTopNotKey(String key, JComponent component) {
		String text = key;

		return Layout.borderLayout(new JLabel(text), null, component, null,
				null);
	}
	
	public boolean shouldSave() {
		// check for modification. Let 'ok' equal 'cancel' when the site was not modified at all. 
		if(doSave && site.equals(originalSite))
			return false;
		
		return doSave;
	}
}
