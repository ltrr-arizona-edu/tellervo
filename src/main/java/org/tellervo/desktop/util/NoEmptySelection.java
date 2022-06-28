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
package org.tellervo.desktop.util;

import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.ListSelectionModel;

/*
  this is a quick hack.

  FIXME: the proper way appears to be to implement ListSelectionModel, or
  subclass DefaultListSelectionModel.  probably not hard, but takes some doing.
*/

public class NoEmptySelection {
    // don't allow deselecting the only selection
    public static void noEmptySelection(JTable table) {
        final JTable glue = table;

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel model = glue.getSelectionModel();
                if (model.isSelectionEmpty()) {
                    // user's trying to screw it up, fixing...
                    int x = model.getAnchorSelectionIndex();
                    model.addSelectionInterval(x, x);
                }
            }
        });
    }
}
