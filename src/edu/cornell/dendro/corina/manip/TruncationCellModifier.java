package edu.cornell.dendro.corina.manip;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.editor.DecadalModel;
import edu.cornell.dendro.corina.editor.support.AbstractTableCellModifier;

public class TruncationCellModifier extends AbstractTableCellModifier {
	private Range range;
	
	public TruncationCellModifier(Range range) {
		this.range = range;
	}
	
	public void updateRange(Range range) {
		this.range = range;
		
		repaintParent();
	}

	public void modifyComponent(Component c, JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		DecadalModel model = (DecadalModel)table.getModel();
		Year year = model.getYear(row, column);		
		
		if(!range.contains(year))
			c.setBackground(Color.black);
	}

}
