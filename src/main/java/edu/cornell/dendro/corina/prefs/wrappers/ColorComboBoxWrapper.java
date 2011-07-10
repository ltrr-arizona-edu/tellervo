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
package edu.cornell.dendro.corina.prefs.wrappers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ItemEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import edu.cornell.dendro.corina.prefs.Prefs.PrefKey;

public class ColorComboBoxWrapper extends ItemWrapper<Color> {
	
	public ColorComboBoxWrapper(JComboBox cbo, PrefKey key, Color defaultColor)
	{
		super(key.getValue(), defaultColor, Color.class);
		init(cbo);
	}
	
	/**
	 * Use ColorComboBoxWrapper(JComboBox PrefKey Color) instead
	 * 
	 * @param cbo
	 * @param prefName
	 * @param defaultColor
	 */
	@Deprecated
	public ColorComboBoxWrapper(JComboBox cbo, String prefName,
			Color defaultColor) {
		super(prefName, defaultColor, Color.class);
		init(cbo);

	}
	
	private void init(JComboBox cbo)
	{
		// set up the combo box
		cbo.addItemListener(this);
		cbo.setModel(new DefaultComboBoxModel(DEFAULT_COLORS));
		cbo.setRenderer(new ColorLabelRenderer());
		
		// set the color in the combobox.
		// hopefully, the default specified color is useful; otherwise we're stuck!
		int i;
		for(i = 0; i < DEFAULT_COLORS.length; i++) {
			if(DEFAULT_COLORS[i].color.equals(getValue())) {
				cbo.setSelectedIndex(i);
				break;
			}
		}

		// errr... bad color setting? bad default?
		if(i == DEFAULT_COLORS.length) {
			NamedColor unknown = new NamedColor(getValue().toString(), getValue());
			cbo.addItem(unknown);
			cbo.setSelectedItem(unknown);
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() != ItemEvent.SELECTED)
			return;
		
		setValue(((NamedColor) e.getItem()).color);
	}

	// a named color: for nifty rendering
	private static class NamedColor {
		Color color;
		String name;

		NamedColor(String name, Color color) {
			this.name = name;
			this.color = color;
		}
	}

	private final static NamedColor[] DEFAULT_COLORS = new NamedColor[] {
			new NamedColor("Blue", Color.blue),
			new NamedColor("Cyan", Color.cyan),
			new NamedColor("Green", Color.green),
			new NamedColor("Yellow", Color.yellow),
			new NamedColor("Orange", Color.orange),
			new NamedColor("Pink", Color.pink),
			new NamedColor("Magenta", Color.magenta),
			new NamedColor("Red", Color.red),
			new NamedColor("Evergreen", new Color(0, 102, 102)), // graphs use this by default?
			new NamedColor("Black", Color.black),
			new NamedColor("Dark Gray", Color.darkGray),
			new NamedColor("Gray", Color.gray),
			new NamedColor("Light Gray", Color.lightGray),
			new NamedColor("White", Color.white), };
	
	// a simple colored-rectangle icon creator
	private static class ColorIcon implements Icon {
		private Color color;
		private final static int WIDTH = 30;
		private final static int HEIGHT = 12;
		private final static int SPACER = 3;

		public int getIconHeight() {
			return HEIGHT;
		}

		public int getIconWidth() {
			return WIDTH + 2 * SPACER;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			g.setColor(color.darker());
			g.drawRect(x + SPACER, y, WIDTH, HEIGHT);
			g.setColor(color);
			g.fillRect(x + SPACER + 1, y + 1, WIDTH - 1, HEIGHT - 1);
		}

		public void setColor(Color color) {
			this.color = color;
		}
	}

	// for a given NamedColor, show "[###] Name", where [###] is a box of that
	// color (ColorIcon)
	@SuppressWarnings("serial")
	private class ColorLabelRenderer extends JLabel implements
			ListCellRenderer {
		private ColorIcon icon = new ColorIcon();

		public ColorLabelRenderer() {
			setOpaque(true);
			setIcon(icon);
		}

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
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

}
