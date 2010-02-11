/**
 * 
 */
package edu.cornell.dendro.corina.gui.dbbrowse;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.ui.Builder;

/**
 * Displays a Boolean in one of its three states:
 * Null: empty
 * True: a 'tick' icon
 * False: a 'cross' icon
 * 
 * @author Lucas Madar
 *
 */
public class BooleanCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

	private Icon yesIcon;
	private Icon noIcon;

	/** Make odd rows have this background color */
	public static final Color BROWSER_ODD_ROW_COLOR = new Color(236, 243, 254);

	private ElementListManager manager;

	private boolean disableSelections;
	
	
	public BooleanCellRenderer(ElementListManager manager, boolean disableSelections) {
		yesIcon = Builder.getIcon("checked.png", 16);
		noIcon = Builder.getIcon("notchecked.png", 16);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		Component c = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);
		
		// if it's been selected, grey it out
		ElementListTableModel model = (ElementListTableModel) table.getModel();
		Element element = model.getElementAt(row);
		if (disableSelections && manager.isElementDisabled(element)) {
			setForeground(UIManager.getColor("Label.disabledForeground"));
			setBackground(UIManager.getColor("Label.background"));
		} else {
			// every-other-line colors
			if (!isSelected) {
				setBackground(row % 2 == 0 ? BROWSER_ODD_ROW_COLOR : table
						.getBackground());
			}
			// default foreground
			setForeground(null);
		}

		// Tool tip
		if (value instanceof Boolean) {
			setToolTipText((Boolean) value ? "Reconciled" : "Not reconciled");
		}
		else if (value != null) {
			setToolTipText(value.toString());
		} else {
			setToolTipText(null);
		}		
		
		// Set cell value
		if(value == null) {
			setIcon(null);
		}
		else {
			Boolean b;
			
			if(value instanceof Boolean)
				b = (Boolean) value;
			else
				b = Boolean.valueOf(value.toString());

			//setBackground(null);
			//setBackground(table.getGridColor());
			setIcon(b ? yesIcon : noIcon);
		}
		
		return c;
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
