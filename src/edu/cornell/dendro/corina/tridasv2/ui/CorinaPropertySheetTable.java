/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2.ui;

import com.l2fprod.common.propertysheet.PropertySheetTable;

public class CorinaPropertySheetTable extends PropertySheetTable {
	private boolean isEditable;
	
	public CorinaPropertySheetTable() {
		isEditable = true;
	}
	
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
		
		if(!isEditable)
			cancelEditing();
		
		repaint();
	}
	
	public boolean isEditable() {
		return isEditable;
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		if(!isEditable)
			return false;
		
		return super.isCellEditable(row, column);
	}
}