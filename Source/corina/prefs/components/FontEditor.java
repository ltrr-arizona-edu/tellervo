package corina.prefs.components;

import javax.swing.AbstractCellEditor;
import javax.swing.table.TableCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.Font;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.ozten.font.JFontChooser;

import corina.util.CorinaLog; 

public class FontEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
  private static final CorinaLog log = new CorinaLog(FontEditor.class);
  
  Font currentFont;
  JButton button;
  JFontChooser fontChooser;
  String text;
  protected static final String EDIT = "edit";

  public FontEditor() {
    button = new JButton();
    button.setActionCommand(EDIT);
    button.addActionListener(this);
    button.setBorderPainted(false);
  }
  
  public void setText(String text) {
    this.text = text;   
  }
  
  public void actionPerformed(ActionEvent e) {
    log.debug("FontEditor actionPerformed!");
    //if (EDIT.equals(e.getActionCommand())) {
      //The user has clicked the cell, so
      //bring up the dialog.
      //button.setBackground(currentColor);
      Font f = JFontChooser.showDialog(new javax.swing.JFrame(), null, text, currentFont); 
      if (f != null) currentFont = f;

      fireEditingStopped(); //Make the renderer reappear.

      text = null;
    /*} else { //User pressed dialog's "OK" button.
      currentColor = colorChooser.getColor();
    }*/
  }

  //Implement the one CellEditor method that AbstractCellEditor doesn't.
  public Object getCellEditorValue() {
    return currentFont;
  }
  
  //Implement the one method defined by TableCellEditor.
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    log.debug("value: " + value);
    currentFont = (Font) value;
    return button;
  }
}
