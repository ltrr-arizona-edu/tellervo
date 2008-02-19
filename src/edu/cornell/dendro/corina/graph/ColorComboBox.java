/**
 * 
 */
package edu.cornell.dendro.corina.graph;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * @author Lucas Madar
 * 
 */
public class ColorComboBox extends JComboBox {

	// a named color: supply these
	/*
	private static class NamedColor {
		Color color;

		String name;

		NamedColor(String name, Color color) {
			this.name = name;
			this.color = color;
		}
	}*/

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
	private static class ColorLabelRenderer extends JLabel implements
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

			GraphInfo.colorPair color = (GraphInfo.colorPair) value;
			setText(color.getColorName());
			icon.setColor(color.getColor());

			return this;
		}
	}

	// a nice color to use for the "other..." entry by default
	private static final Color OTHER_COLOR = new Color(0, 204, 204);
	
	public void setColor(Color c) {
		int i;
		
		for(i = 0; i < GraphInfo.screenColors.length; i++) {
			if(c.equals(GraphInfo.screenColors[i].getColor()))
				break;
		}
		
		// we matched a color!
		if(i != GraphInfo.screenColors.length) {
			setSelectedIndex(i);
			otherColor.setColor(OTHER_COLOR);
			return;
		}

		// it's "other"
		otherColor.setColor(c);
		setSelectedIndex(i);
	}
	
	public Color getSelectedColor() {
		int idx = getSelectedIndex();
		
		if(idx == GraphInfo.screenColors.length)
			return otherColor.getColor();
		
		return GraphInfo.screenColors[idx].getColor();
	}

	/**
	 * Create a new popup for a color preference.
	 * 
	 * @param preference
	 *            the preference key that this popup corresponds to
	 */
	public ColorComboBox() {
		// color
		boolean stdColor = false;

		// add a bunch of standard colors
		for (int i = 0; i < GraphInfo.screenColors.length; i++) {
			addItem(GraphInfo.screenColors[i]);
		}
		
		otherColor = new GraphInfo.colorPair("Other...", OTHER_COLOR);
		addItem(otherColor);

		// "other" item
		// BUG: no i18n!
		/*
		final NamedColor other = new GraphInfo.colorPair("Other...",
				(stdColor || oldColor == null) ? OTHER_COLOR : oldColor);
		addItem(other);
		if (!stdColor)
			setSelectedIndex(DEFAULT_COLORS.length);

		// glue
		final String pref = preference;
		*/

		// when the user selects something, deal with it
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				// "other" selected?
				if ((NamedColor) ((JComboBox) e.getSource()).getSelectedItem() == other) {
					// ask for new color
					Color newColor = JColorChooser.showDialog(null, // no parent
																	// component?
							"Choose New Color", other.color);
					if (newColor != null)
						other.color = newColor;
				}

				// encode color as string
				Color color = ((NamedColor) ((JComboBox) e.getSource())
						.getSelectedItem()).color;
				String value = "#"
						+ Integer.toHexString(color.getRGB() & 0x00ffffff);

				// set preference
				App.prefs.setPref(pref, value);
				*/
			}
		});

		// set the renderer
		setRenderer(new ColorLabelRenderer());

		// display all
		setMaximumRowCount(getItemCount());
	}

	// to store our colors, and other goodness..
	private GraphInfo.colorPair otherColor;
}
