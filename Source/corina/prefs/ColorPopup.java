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

package corina.prefs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JColorChooser;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.Icon;

public class ColorPopup extends JComboBox {

    // a named color: supply these
    static class NamedColor {
	Color color;
	String name;
	NamedColor(String name, Color color) {
	    this.name = name;
	    this.color = color;
	}
    }

    // a simple colored-rectangle icon
    static class ColorIcon implements Icon {
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
	public void setColor(Color c) {
	    color = c;
	}
    }

    // for a given NamedColor, show "[###] Name", where [###] is a box of that color (ColorIcon)
    static class ColorLabelRenderer extends JLabel implements ListCellRenderer {
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

    static NamedColor[] DEFAULT_COLORS = new NamedColor[] {
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

    // original
    public ColorPopup(String property) {
	// color
	Color oldColor = Color.getColor(property);
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
	final NamedColor other = new NamedColor("Other...",
						(stdColor||oldColor==null) ? new Color(0, 204, 204) : oldColor);
	addItem(other);
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
		}
	    });

	// set the renderer
	setRenderer(new ColorLabelRenderer());
    }

    // ----------------------------------------

    // this constructor was hacked to get the spiffy new ColorPopup to
    // work with the old klutzy XML preferences dialog.
    public ColorPopup(Color oldColor, PrefsTemplate.Option target) {
	// color
	boolean stdColor = false;

	// add a bunch of standard colors
	for (int i=0; i<DEFAULT_COLORS.length; i++) {
	    addItem(DEFAULT_COLORS[i]);
	    if (DEFAULT_COLORS[i].color.equals(oldColor)) {
		setSelectedIndex(i);
		stdColor = true;
	    }
	}

	// glue
	final PrefsTemplate.Option glue = target;

	// "other" item
	final NamedColor other = new NamedColor("Other...",
						(stdColor||oldColor==null) ? new Color(0, 204, 204) : oldColor);
	addItem(other);
	if (!stdColor)
	    setSelectedIndex(DEFAULT_COLORS.length);
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

		    // whatever was selected, assign it
		    Color color = ((NamedColor) ((JComboBox) e.getSource()).getSelectedItem()).color;
		    glue.value = "#" + Integer.toHexString(color.getRGB() & 0x00ffffff);
		}
	    });

	// set the renderer
	setRenderer(new ColorLabelRenderer());
    }
 }
