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

import javax.swing.JDialog;
import javax.swing.JButton;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
   Given an OK button (JButton), make the OK button the default (heavy
   outline, press return to activate) and the escape key simply close
   the dialog.

   <p>This assumes the OK button has been added to a JDialog.  It uses
   <code>getTopLevelAncestor()</code> to find what JDialog the button
   was placed in, so if you use this method before adding it to a
   JDialog, a NullPointerException is thrown.</p>

   <p>This also assumes the "Cancel" button simply closes the dialog.
   If your cancel button needs to do something special (for whatever
   reason), don't use this method.</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class OKCancel {
    // don't instantiate me
    private OKCancel() { }

    // add esc=>cancel and return=>ok bindings
    // TODO: accept "cancel" button code, too -- null=dispose
    // (why, do i need it?  yeah, grep for setdefaultbutton.)

    /**
       Make ok/cancel act like normal ok/cancel buttons.
       @param ok the OK button to use
       @exception NullPointerException if the ok button hasn't been
       placed in a dialog yet
    */
    public static void addKeyboardDefaults(JButton ok) {
	// must be a dialog?
	final JDialog dialog = (JDialog) ok.getTopLevelAncestor();
        
        // esc => cancel
        dialog.addKeyListener(new KeyAdapter() {
            @Override
			public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    dialog.dispose();
            }
        });

        // return => ok
        dialog.getRootPane().setDefaultButton(ok);
    }

    // other things to do:
    // -- make panel with just ok/cancel, given "ok", "cancel" code (runnables?)
}
