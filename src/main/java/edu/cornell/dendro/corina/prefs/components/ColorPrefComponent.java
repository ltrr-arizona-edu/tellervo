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
// Copyright 2003 Ken Harris <kbh7@cornell.edu>
//

package edu.cornell.dendro.corina.prefs.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import edu.cornell.dendro.corina.core.App;

/**
    A popup menu for picking a color.  Given a key (like "corina.graph.background"),
    this popup will change the value of that preference using the Prefs class methods.

    <h2>Left to do</h2>
    <ul>
        <li>What if it's null?  I think I need a default Color in the constructor
        <li>I18n
    </ul>

    @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
    @version $Id$
*/
@SuppressWarnings("serial")
public class ColorPrefComponent extends JComboBox {

    // a named color: supply these
    private static class NamedColor {
	Color color;
	String name;
	NamedColor(String name, Color color) {
	    this.name = name;
	    this.color = color;
	}
    }

    // a simple colored-rectangle icon
    private static class ColorIcon implements Icon {
	private Color color;
	private final static int WIDTH = 30;
	private final static int HEIGHT = 12;
	private final static int SPACER = 3;
	public int getIconHeight() {
	    return HEIGHT;
	}
	public int getIconWidth() {
	    return WIDTH + 2*SPACER;
	}
	public void paintIcon(Component c, Graphics g, int x, int y) {
	    g.setColor(color.darker());
	    g.drawRect(x+SPACER, y, WIDTH, HEIGHT);
	    g.setColor(color);
	    g.fillRect(x+SPACER+1, y+1, WIDTH-1, HEIGHT-1);
	}
	public void setColor(Color color) {
	    this.color = color;
	}
    }

    // for a given NamedColor, show "[###] Name", where [###] is a box of that color (ColorIcon)
    private static class ColorLabelRenderer extends JLabel implements ListCellRenderer {
	private ColorIcon icon = new ColorIcon();
	public ColorLabelRenderer() {
	    setOpaque(true);
	    setIcon(icon);
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

	    NamedColor color = (NamedColor) value;
	    setText(color.name);
	    icon.setColor(color.color);

	    return this;
	}
    }

    // BUG: no i18n!
    private final static NamedColor[] DEFAULT_COLORS = new NamedColor[] {
	new NamedColor("Blue",		Color.blue),
	new NamedColor("Cyan",		Color.cyan),
	new NamedColor("Green",		Color.green),
	new NamedColor("Yellow",	Color.yellow),
	new NamedColor("Orange",	Color.orange),
	new NamedColor("Pink",		Color.pink),
	new NamedColor("Magenta",	Color.magenta),
	new NamedColor("Red",		Color.red),

	new NamedColor("Black",		Color.black),
	new NamedColor("Dark Gray",	Color.darkGray),
	new NamedColor("Gray",		Color.gray),
	new NamedColor("Light Gray",	Color.lightGray),
	new NamedColor("White",		Color.white),
    };

    // a nice color to use for the "other..." entry by default
    private static final Color OTHER_COLOR = new Color(0, 204, 204);

    /**
        Create a new popup for a color preference.
    
        @param preference the preference key that this popup corresponds to
    */
    public ColorPrefComponent(String preference) {
	// color
      Color oldColor = App.prefs.getColorPref(preference, Color.black); // BUG: what if it's not parseable?
	boolean stdColor = false;

	// add a bunch of standard colors
	for (int i=0; i<DEFAULT_COLORS.length; i++) {
	    addItem(DEFAULT_COLORS[i]);
	    if (DEFAULT_COLORS[i].color.equals(oldColor)) {
		setSelectedIndex(i);
		stdColor = true;
	    }
	}

	// "other" item
	// BUG: no i18n!
	final NamedColor other = new NamedColor("Other...",
						(stdColor||oldColor==null) ? OTHER_COLOR : oldColor);
	addItem(other);
	if (!stdColor)
	    setSelectedIndex(DEFAULT_COLORS.length);

        // glue
        final String pref = preference;

        // when the user selects something, deal with it
	addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    // "other" selected?
		    if ((NamedColor) ((JComboBox) e.getSource()).getSelectedItem() == other) {
			// ask for new color
			Color newColor = JColorChooser.showDialog(null, // no parent component?
								  "Choose New Color",
								  other.color);
			if (newColor != null)
			    other.color = newColor;
		    }
                    
                    // encode color as string
		    Color color = ((NamedColor) ((JComboBox) e.getSource()).getSelectedItem()).color;
		    String value = "#" + Integer.toHexString(color.getRGB() & 0x00ffffff);

                    // set preference
        App.prefs.setPref(pref, value);
		}
	    });

	// set the renderer
	setRenderer(new ColorLabelRenderer());

	// display all
	setMaximumRowCount(getItemCount());
    }
}
