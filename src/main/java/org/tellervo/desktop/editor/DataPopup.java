/*******************************************************************************
 * Copyright (C) 2001 Ken Harris
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
 *     Peter Brewer
 ******************************************************************************/

package org.tellervo.desktop.editor;

import java.awt.event.*;
import javax.swing.*;

// -- not implemented!  need to implement SampleDataView.insertYear(),
// deleteYear() first

// to use:
//    listener = new PopupListener(new DataPopup(myTable), myTable); <-- simplyfiy?  make one an inner class?

public class DataPopup extends JPopupMenu {

	private static final long serialVersionUID = 1L;

	// constructor MUST GET table (or similar) passed to it!
    public DataPopup() {
	// insert year
	JMenuItem ins = new JMenuItem("Insert");
	ins.addActionListener(new AbstractAction() {

		private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent ae) {
			    // insertYear();
			}
	    });
	add(ins);

	// delete year
	JMenuItem del = new JMenuItem("Delete");
	del.addActionListener(new AbstractAction() {
		private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent ae) {
			    // deleteYear();
			}
	    });
	add(del);
    }
}
