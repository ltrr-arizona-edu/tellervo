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

public class ColorComboBoxWrapper extends ItemWrapper<Color> {
	public ColorComboBoxWrapper(JComboBox cbo, String prefName,
			Color defaultColor) {
		super(prefName, defaultColor, Color.class);

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
