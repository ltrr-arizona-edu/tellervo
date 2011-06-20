package edu.cornell.dendro.corina.tridasv2.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import edu.cornell.dendro.corina.util.ColorUtils;

/**
 * A wrapper around TableCellRenderer that highlights
 * table cells with invalid values
 * 
 * @author Lucas Madar
 *
 */

public class RequiredCellRendererWrapper implements TableCellRenderer {
	private TableCellRenderer renderer;
	
	public RequiredCellRendererWrapper(TableCellRenderer renderer) {	
		this.renderer = renderer;
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		Component c = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		if(value == null || value.toString() == "") {
			if(isSelected) {
				Color blended = ColorUtils.blend(Color.red, c.getBackground());
				c.setBackground(blended);
			}
			else {
				c.setBackground(Color.red);
			}
		}
		
		return c;
	}

}
