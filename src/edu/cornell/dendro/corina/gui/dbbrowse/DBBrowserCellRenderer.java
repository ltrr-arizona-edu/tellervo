package edu.cornell.dendro.corina.gui.dbbrowse;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

import edu.cornell.dendro.corina.sample.Element;

public class DBBrowserCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	
	/** Make odd rows have this background color */
	public static final Color BROWSER_ODD_ROW_COLOR = new Color(236, 243, 254);
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);
		
		// if it's been selected, grey it out
		DBBrowserTableModel model = (DBBrowserTableModel) table.getModel();
		Element element = model.getElementAt(row);
		if(disableSelections && browser.isSelectedElement(element)) {
			setForeground(UIManager.getColor("Label.disabledForeground"));
			setBackground(UIManager.getColor("Label.background"));
		}
		else {
			// every-other-line colors
			if (!isSelected)
				setBackground(row % 2 == 0 
						? BROWSER_ODD_ROW_COLOR
						: table.getBackground());
			
			// default foreground
			setForeground(null);
		}
				
		// special case for isReconciled column
		if(column == 9) {
			if(value instanceof Boolean) 
				setToolTipText(((Boolean) value) ? "Not reconciled" : "Reconciled");
		}
		else if(value != null) {
			setToolTipText(value.toString());
		}
		else
			this.setToolTipText(null);
		
		return c;
	}

	private DBBrowser browser;
	private boolean disableSelections;
	
	public DBBrowserCellRenderer(DBBrowser browser, boolean disableSelections) {
		this.browser = browser;
		this.disableSelections = disableSelections;
	}
}
