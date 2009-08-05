/**
 * 
 */
package edu.cornell.dendro.corina.gui.dbbrowse;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import edu.cornell.dendro.corina.ui.Builder;

/**
 * Displays a Boolean in one of its three states:
 * Null: disabled background
 * True: a checkbox
 * False: absence of checkbox
 * 
 * @author Lucas Madar
 *
 */
public class BooleanCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

	private Icon applyIcon;
	
	public BooleanCellRenderer() {
		applyIcon = Builder.getIcon("apply.png", 16);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		if(value == null) {
			setBackground(table.getGridColor());
			setIcon(null);
		}
		else {
			Boolean b;
			
			if(value instanceof Boolean)
				b = (Boolean) value;
			else
				b = Boolean.valueOf(value.toString());

			setBackground(null);
			setIcon(b ? applyIcon : null);
		}
		
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);
	}
	
	@Override
	public boolean isOpaque() {
		return true;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		Icon icon = getIcon();
		
		if(icon != null) {
			int x = getWidth()/2 - icon.getIconWidth()/2;
			int y = getHeight()/2 - icon.getIconHeight()/2;
		
			icon.paintIcon(this, g, x, y);
		}
	}
}
