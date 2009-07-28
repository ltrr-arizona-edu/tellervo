package edu.cornell.dendro.corina.tridasv2.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.lowagie.text.Font;


public class TridasDefaultPropertyRenderer extends JLabel implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	public TridasDefaultPropertyRenderer() {
		super("...");
		
		setFont(getFont().deriveFont(Font.ITALIC));
		setForeground(Color.GRAY.brighter());
		setAlignmentX(RIGHT_ALIGNMENT);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		if(isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		}
		else {
			setForeground(Color.GRAY.brighter());
			setBackground(table.getBackground());			
		}
		
		if(value == null || 
				// blank it out if we're not editable, too...
				(table instanceof CorinaPropertySheetTable && !((CorinaPropertySheetTable)table).isCellEditable(row, column)))
			setText("");
		else
			setText("Click to remove...");
		
		return this;
	}
}
