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

package corina.graph;

import corina.Sample;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// TODO: combine this with popupmenu somehow.  i'd rather just say
// "PopupMenu.addPopupMenu(blah);" and be done with it.
public class PopupListener extends MouseAdapter {

    private GrapherPanel gp;
    private PopupMenu myMenu;

    public PopupListener(PopupMenu myMenu, GrapherPanel widget) {
	gp = widget;
	this.myMenu = myMenu;
	widget.addMouseListener(this);
    }

    public void mousePressed(MouseEvent e) {
	maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
	maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
	if (e.isPopupTrigger()) { // right-click?
	    // select this graph
	    int n = gp.getGraphAt(e.getPoint());

	    // not on a graph?  die.
	    if (n == -1)
		return;

	    // select it
	    gp.current = n;
	    gp.repaint();

	    // show the popup
	    myMenu.setSample((Sample) ((Graph) gp.graphs.get(gp.current)).graph);
	    myMenu.show(e.getComponent(), e.getX(), e.getY());
	}
    }
}
