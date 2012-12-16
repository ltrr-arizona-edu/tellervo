/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
//
// This file is part of Corina.
// 
// Tellervo is free software; you can redistribute it and/or modify
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

package org.tellervo.desktop.util;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.JTable;

/**
   A listener for popup menus.

   <p>Since Mac and Windows systems treat different mouse events as
   popup events, the standard Java idiom for creating a popup listener
   is:

<pre>
    public void mousePressed(MouseEvent e) { // mac
	maybeShowPopup(e);
    }
    public void mouseReleased(MouseEvent e) { // windows
	maybeShowPopup(e);
    }
    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
	    // (build popup here, if necessary)
	    popup.show(e.getComponent(), e.getX(), e.getY());
	}
    }
</pre>

   Naturally, this begs to be refactored.  Hence this class.</p>

   <p>There are 2 ways to use this class.</p>

   <ul>

   <li>If you only need to show an existing JPopupMenu when a
   popup mouse event occurs, then you can simply say:</p>

<pre>
    component.addMouseListener(new PopupListener(popup));
</pre>

    <li>If you want to do something more complex than simply
    showing an existing popup menu (for example, build the popup
    lazily), you can extend PopupListener by defining your own
    <code>showPopup()</code> method:</p>

<pre>
   component.addMouseListener(new PopupListener() {
      public void showPopup(MouseEvent e) {
         JPopupMenu popup = new JPopupMenu();
         popup.add(new JMenuItem("1"));
         popup.show(e.getComponent(), e.getX(), e.getY());
      }
   });
</pre>

   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class PopupListener extends MouseAdapter {

    /**
       Make a new popup listener, with no popup.  If you use this
       constructor, it's assumed you'll be defining your own
       showPopup() method -- otherwise this adapter won't do anything.
     */
    public PopupListener() {

    	
    }

    /**
       Make a new popup listener, with the specified popup.  If you
       just want this popup shown when the popup trigger is given, you
       don't need to do anything else (but you can still override
       showPopup() if you really want).
       @param popup the popup to show
    */
    public PopupListener(JPopupMenu popup) {


	this.popup = popup;
    }

    private JPopupMenu popup = null;

    /** Watch for mouse-pressed popup events.  Mac systems generate
	popup events on mouse-down.
	@param e the mouse-pressed event
    */
    @Override
	public void mousePressed(MouseEvent e) { // mac
	maybeShowPopup(e);
    }

    /** Watch for mouse-released popup events.  Windows systems
	generate popup events on mouse-up, because Windows supports
	what is perhaps the weirdest and most counterintuitive user
	interaction ever conceived: the
	right-click-drag-release-select-from-menu.
	@param e the mouse-released event
    */
    @Override
	public void mouseReleased(MouseEvent e) { // win32
	maybeShowPopup(e);
    }

    // if e is a popup event, call showPopup()
    private void maybeShowPopup(MouseEvent e) {
	if (e.isPopupTrigger()) {
	    showPopup(e);
	}
    }

    // for jpopupmenu, you can usually say popupMenu.showPopup(e.getComponent(),
    // e.getX(), e.getY());

    // (was: showPopup(x,y).  but lots of places used e.getComponent() instead
    // of remembering which component to show it on.  plus, even for e.getX()/
    // e.getY(), this takes advantage of "looks like it did before" (which
    // make(1) does, too, for example), so people will be more likely to use it.)

    /**
       Called when a popup mouse event is generated.  Implement this
       method to do what you want on popup events.

       <p>The default behavior is:</p>
       <ul>
         <li>if a JTable was clicked on, and the row that was clicked
         on was not selected, select it (and deselect the rows that
         were selected)

	 <li>show the popup here (assuming it's non-null)
       </ul>

       <p>Remember, you can get the position of the click with
       e.getX() and e.getY(), and the component that was clicked on
       with e.getComponent().</p>

       @param e the popup mouse event
    */
    public void showPopup(MouseEvent e) {
	// if table, and this row not selected, select this row
	if (e.getSource() instanceof JTable) {
	    JTable table = (JTable) e.getSource();
	    int row = table.rowAtPoint(e.getPoint());
	    if (row!=-1 && !table.isRowSelected(row))
		table.setRowSelectionInterval(row, row);
	}

	// show popup
	if (popup != null)
	    popup.show(e.getComponent(), e.getX(), e.getY());
    }
}
