package corina.prefs.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.AbstractCellEditor;
import javax.swing.table.TableCellEditor;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

import corina.util.CorinaLog;

/**
 * @author Aaron
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class UIDefaultsEditor extends AbstractCellEditor implements TableCellEditor, CellEditorListener {
  private static final CorinaLog log = new CorinaLog(UIDefaultsEditor.class);
  
  private ColorEditor colorEditor = new ColorEditor();
  private FontEditor fontEditor = new FontEditor();
  private TableCellEditor chosenEditor;
  
  public UIDefaultsEditor() {
    colorEditor.addCellEditorListener(this);
    fontEditor.addCellEditorListener(this);
  }

  public Component getTableCellEditorComponent(JTable table,
    Object value, boolean isSelected, int row, int column) {
      
    log.trace("getTableCellEditorComponent " + value + " " + isSelected + " " + row + " " + column);
    if (value instanceof ColorUIResource) {
      log.trace("returning colorEditor");
      chosenEditor = colorEditor;
      colorEditor.setColor((Color) value);
      return colorEditor.getTableCellEditorComponent(table, value, isSelected, row, column);
    } else if (value instanceof FontUIResource) {
      log.trace("returning fontEditor");
      fontEditor.setText(table.getModel().getValueAt(row, 0).toString());
      fontEditor.setFont((Font) value);
      chosenEditor = fontEditor;
      return fontEditor.getTableCellEditorComponent(table, value, isSelected, row, column);
    }
    else return null;
  }
  
  public Object getCellEditorValue() {
    return chosenEditor.getCellEditorValue();
  }

  public void cancelCellEditing() {
    chosenEditor.cancelCellEditing();
  }

  public boolean stopCellEditing() {
    return chosenEditor.stopCellEditing();
  }

  public void editingCanceled(ChangeEvent e) {
    super.cancelCellEditing();     
  }

  public void editingStopped(ChangeEvent e) {
    super.stopCellEditing();  
  }
}