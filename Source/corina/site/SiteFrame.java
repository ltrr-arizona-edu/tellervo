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

import corina.gui.XFrame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/*
  stuff the user should be able to do from here:
  - view all sites; sort by ...
  - search for sites by name
  - add sites
  - edit sites
  - make a map of a site
  - given a site, find nearby sites
*/

public class SiteFrame extends XFrame {

    // data
    private SiteDB db = SiteDB.getSiteDB();

    /*
    protected class SiteTableModel extends AbstractTableModel {
	public int getColumnCount() {
	    return 3; // ?
	}
	public int getRowCount() {
	    return db.sites.size();
	}
	public Object getValueAt(int row, int col) {
	    // site /row/
	}
    }
    */

    public SiteFrame() {
	// title
	setTitle("Site Database");

	// WRITE ME

	// pack and show
	pack();
	setSize(new Dimension(640, 480));
	show();
    }

    public static void showSiteInfo(Site s) {
        // for now, just put up a very simple dialog saying what it is
        String html = "<html><table><tr><td>Country<td>" + s.country +
        "<tr><td>Code<td>" + s.code +
        "<tr><td>Name<td>" + s.name +
        "<tr><td>ID<td>" + s.id +
        "<tr><td>Species<td>" + s.species +
        "<tr><td>Type<td>" + s.type2 +
        "<tr><td>Location<td>" + s.location;
        final JDialog d = new JDialog();
        d.setTitle(s.name);
        d.getContentPane().add(new JLabel(html), BorderLayout.CENTER);
        d.pack();
        d.show();
    }
}
