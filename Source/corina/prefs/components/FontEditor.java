package corina.prefs.components;

import javax.swing.AbstractCellEditor;
import javax.swing.table.TableCellEditor;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import java.awt.Font;
import java.awt.Component;

import com.ozten.font.JFontChooser;

import corina.util.CorinaLog;

public class FontEditor extends AbstractCellEditor implements TableCellEditor {
  private static final CorinaLog log = new CorinaLog(FontEditor.class);
  
  private Font currentFont;
  private String text;

  private Runnable showdialog = new Runnable() {
    public void run() {
      Font f = JFontChooser.showDialog(new javax.swing.JFrame(), text, text, currentFont);
      if (f != null) currentFont = f;
      text = null;
      fireEditingStopped();
    }
  };

  public void setText(String text) {
    this.text = text;   
  }
  
  //Implement the one CellEditor method that AbstractCellEditor doesn't.
  public Object getCellEditorValue() {
    return currentFont;
  }
  
  //Implement the one method defined by TableCellEditor.
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    SwingUtilities.invokeLater(showdialog);
    return table.getCellRenderer(row, column).getTableCellRendererComponent(table, value, isSelected, true, row, column);
  }
}
