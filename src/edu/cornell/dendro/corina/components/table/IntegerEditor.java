/**
 * Created at Aug 22, 2010, 3:48:12 AM
 */
package edu.cornell.dendro.corina.components.table;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 * @author Daniel
 *
 */
public class IntegerEditor extends AbstractCellEditor {
	private static final long serialVersionUID = 1L;
	
	private final IntegerTextField field;
	 
	public IntegerEditor(){
		field = new IntegerTextField(true);
	}
	
	public boolean stopCellEditing() {
 
		System.out.println("stopCellEditing: " + field.getValue());
 
		if( field.isEditValid() && field.getInputVerifier().verify(field)) {
			
			return super.stopCellEditing();
		} else {
			JOptionPane.showMessageDialog(field, "Invalid value: "
					+ field.getText());
			return false;
		}
 
	}
 
	// set the edited value in our JFormattedTextField
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
 
		System.out.println("getTableCellEditorComponent: " + value);
		field.setValue(value);
 
		return field;
	}
 
	public Object getCellEditorValue() {
		System.out.println("getCellEditorValue: " + field.getValue());
		return ((Number) field.getValue()).doubleValue();
	}
}
