package corina.prefs.components;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.AbstractCellEditor;
import javax.swing.table.TableCellEditor;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

import corina.util.CorinaLog;

/**
 * @author Aaron
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class UIDefaultsEditor extends AbstractCellEditor implements TableCellEditor {
  private static final CorinaLog log = new CorinaLog(UIDefaultsEditor.class);
  
  private ColorEditor colorEditor = new ColorEditor();
  private FontEditor fontEditor = new FontEditor();
  private TableCellEditor chosenEditor;
  
  public Component getTableCellEditorComponent(JTable table,
    Object value, boolean isSelected, int row, int column) {
      
    log.trace("getTableCellEditorComponent " + value + " " + isSelected + " " + row + " " + column);
    if (value instanceof ColorUIResource) {
      log.trace("returning colorEditor");
      chosenEditor = colorEditor;
      return colorEditor.getTableCellEditorComponent(table, value, isSelected, row, column);
    } else if (value instanceof FontUIResource) {
      log.trace("returning fontEditor");
      fontEditor.setText(table.getModel().getValueAt(row, 0).toString());
      chosenEditor = fontEditor;
      return fontEditor.getTableCellEditorComponent(table, value, isSelected, row, column);
    }
    else return null;
  }
  
  public Object getCellEditorValue() {
    return chosenEditor.getCellEditorValue();
  }
}
