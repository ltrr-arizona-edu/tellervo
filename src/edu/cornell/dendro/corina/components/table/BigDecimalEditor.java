/**
 * Created at Aug 22, 2010, 1:02:01 PM
 */
package edu.cornell.dendro.corina.components.table;

import java.awt.Component;
import java.math.BigDecimal;

import javax.swing.AbstractCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 * @author Daniel
 *
 */
public class BigDecimalEditor extends AbstractCellEditor {
	private static final long serialVersionUID = 1L;
	
	private final BigDecimalTextField field;
	 
	public BigDecimalEditor(int argEditorPrecision){
		field = new BigDecimalTextField(true);
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
		field.setValue(value);
		return field;
	}
 
	public Object getCellEditorValue() {
		return new BigDecimal(((Number) field.getValue()).toString());
	}
}
