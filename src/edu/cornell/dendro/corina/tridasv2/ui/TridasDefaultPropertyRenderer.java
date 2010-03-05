package edu.cornell.dendro.corina.tridasv2.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;

import com.l2fprod.common.swing.renderer.DefaultCellRenderer;


public class TridasDefaultPropertyRenderer extends DefaultCellRenderer  {
	private static final long serialVersionUID = 1L;

	private Font font;
	
	@Override
	protected String convertToString(Object value) {
		if(value == null)
			return "";
		else
			return "Click to remove...";		
	}

	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		// blank out the value if we're null...
		if (table instanceof CorinaPropertySheetTable && 
				!((CorinaPropertySheetTable)table).isCellEditable(row, column))
			value = null;
		
		// if we're not selected, be inconspicuous
		if(!isSelected)
			setForeground(Color.GRAY.brighter());
		
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		// make the font italic
		if(font == null)
			font = c.getFont().deriveFont(Font.ITALIC);		
		c.setFont(font);
		
		return c;
	}
}
