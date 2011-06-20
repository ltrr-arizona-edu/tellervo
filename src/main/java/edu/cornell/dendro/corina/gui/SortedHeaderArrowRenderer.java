package edu.cornell.dendro.corina.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

public class SortedHeaderArrowRenderer implements TableCellRenderer {
	/**
	 * Make a new sorted header renderer.
	 * 
	 * The table argument is only used for the calls
	 * <code>table.getTableHeader().getDefaultRenderer()</code> to get the
	 * default renderer for the table header.
	 * 
	 * @param table
	 *            the JTable which will use this class as its renderer
	 * @param defaultSortColumn
	 *            the number of the default column to sort by. Can be null.
	 */
	public SortedHeaderArrowRenderer(JTable table, Integer defaultSortColumn) {
		normal = table.getTableHeader().getDefaultRenderer();
		if (defaultSortColumn != null)
			sortColumn = defaultSortColumn;
		else
			sortColumn = -1;
	}

	// the default header renderer
	private TableCellRenderer normal;

	// which column to sort by? (as its text label)
	private int sortColumn;

	// is the sort reversed?
	private boolean reversed = false;

	/**
	 * Set a new sort column. This is the index number to match of the headers.
	 * The null value is allowed.
	 * 
	 * @param sortColumn
	 *            the number of the new column which is used for sorting
	 */
	public void setSortColumn(Integer sortColumn) {
		if (sortColumn != null)
			this.sortColumn = sortColumn;
		else
			this.sortColumn = -1;
	}

	/**
	 * Tell the renderer whether this sort is forward or reverse. If it's
	 * reversed, the triangle is drawn upside-down.
	 * 
	 * @param reversed
	 *            is the sort a reverse-sort?
	 */
	public void setReversed(boolean reversed) {
		this.reversed = reversed;
	}
	
	/**
	 * Is this sort column reversed?
	 * @return
	 */
	public boolean isReversed() {
		return reversed;
	}

	private static class SortArrowIcon implements Icon {
		private boolean reversed;
		private int size;

		public SortArrowIcon(boolean reversed, int size) {
			this.size = size;
			this.reversed = reversed;
		}

		public int getIconHeight() {
			return size;
		}

		public int getIconWidth() {
			return size;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			Color bgcolor = (c == null) ? Color.GRAY : c.getBackground();

			// turn on antialiasing
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			int dx = (int) (size / 1.25);
			int dy = reversed ? -dx : dx;

			// magic: align icon with specified font size
			y += 5 * size / 6 + (reversed ? 0 : -dy);
			// give it some space!
			x += size * 2;

			int shift = reversed ? -1 : 1;

			g.translate(x, y);

			// draw the right diagonal
			g.setColor(bgcolor.darker());
			g.drawLine(dx / 2, dy, 0, 0);
			g.drawLine(dx / 2, dy + shift, 0, shift);

			// Left diagonal.
			g.setColor(bgcolor.brighter());
			g.drawLine(dx / 2, dy, dx, 0);
			g.drawLine(dx / 2, dy + shift, dx, shift);

			// Horizontal line.
			if (!reversed) {
				g.setColor(bgcolor.darker().darker());
			} else {
				g.setColor(bgcolor.brighter().brighter());
			}
			g.drawLine(dx, 0, 0, 0);

			g.setColor(bgcolor);
			g.translate(-x, -y);
		}
	}

	/**
	 * Return the component which does the rendering. This is used by the JTable
	 * architecture; you never need to call this explicitly.
	 * 
	 * @return the default renderer component, with a triangle drawn on it, if
	 *         its text matches the sort field
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		Component c = normal.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);

		// if it's a normal table header (and it should be...)
		if (c instanceof JLabel) {
			JLabel l = (JLabel) c;

			l.setHorizontalTextPosition(SwingConstants.LEFT);

			if (sortColumn == column)
				l.setIcon(new SortArrowIcon(reversed, l.getFont().getSize()));
			else
				l.setIcon(null); // no icon!
		}

		return c;
	}
}
