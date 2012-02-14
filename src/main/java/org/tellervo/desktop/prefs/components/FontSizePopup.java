/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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
package org.tellervo.desktop.prefs.components;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;

import org.tellervo.desktop.core.App;


@SuppressWarnings("serial")
public class FontSizePopup extends JComboBox {

	static class SizeLabelRenderer extends JLabel implements ListCellRenderer {
	public SizeLabelRenderer() {
	    setOpaque(true);
	}
	public Component getListCellRendererComponent(JList list, Object value,
						      int index,
						      boolean isSelected, boolean cellHasFocus) {
	    if (isSelected) {
		setBackground(list.getSelectionBackground());
		setForeground(list.getSelectionForeground());
	    } else {
		setBackground(list.getBackground());
		setForeground(list.getForeground());
	    }

	    int size = Integer.parseInt((String) value);
	    setFont(getFont().deriveFont(Font.PLAIN, size));
	    setText((String) value);

	    return this;
	}
    }

    int sizes[] = new int[] {
	8, 9, 10, 11, 12, 14, 16, 18, 20,
    };

    public FontSizePopup(String property, JTable preview) {
	Font oldFont = App.prefs.getFontPref(property, new Font(null));
	for (int i=0; i<sizes.length; i++) {
	    addItem(String.valueOf(sizes[i]));
	    if (oldFont.getSize() == sizes[i])
		setSelectedIndex(i);
	}

	// render in its size: how well does this work?  pretty darned
	// well.
	setRenderer(new SizeLabelRenderer());

	// ugh.
	final JTable glue = preview;

	// the combobox (i.e., the selected value) is in the selected
	// size, as well (this works less well)
	addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    int size = Integer.parseInt((String) ((JComboBox) e.getSource()).getSelectedItem());
		    Font font = getFont();
		    /*
		      setFont(new Font(font.getName(), Font.PLAIN, size));
		    */

		    // set both the table font size, and its row
		    // height (which is why preview isn't the generic
		    // jcomponent)
		    glue.setFont(new Font(font.getName(), Font.PLAIN, size));
		    glue.setRowHeight(size + 4);
		}
	    });
    }
}
