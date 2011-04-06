package edu.cornell.dendro.corina.prefs.components;

import javax.swing.AbstractCellEditor;
import javax.swing.table.TableCellEditor;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.cornell.dendro.corina.logging.CorinaLog;

@SuppressWarnings("serial")
public class ColorEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
  private static final CorinaLog log = new CorinaLog(ColorEditor.class);
  
  Color currentColor;
  //JButton button;
  JColorChooser colorChooser;
  JDialog dialog;
  protected static final String EDIT = "edit";
  
  private Runnable showdialog = new Runnable() {
    public void run() {
      colorChooser.setColor(currentColor);
      dialog.setVisible(true);
      currentColor = colorChooser.getColor();
    }
  };
  
  public ColorEditor() {
    /*button = new JButton();
    button.setActionCommand(EDIT);
    button.addActionListener(this);*/
    //button.setBorderPainted(false);

    //Set up the dialog that the button brings up.
    colorChooser = new JColorChooser();
      dialog = JColorChooser.createDialog(null, "Pick a Color", true, //modal
    colorChooser, null, //OK button handler
  null); //no CANCEL button handler
  }
  
  public void setColor(Color c) {
    currentColor = c;
  }

  public void actionPerformed(ActionEvent e) {
    log.debug("action: " + e.getActionCommand());
    if (EDIT.equals(e.getActionCommand())) {
      //The user has clicked the cell, so
      //bring up the dialog.
      //button.setBackground(currentColor);
      colorChooser.setColor(currentColor);
      dialog.setVisible(true);

      //fireEditingStopped(); //Make the renderer reappear.

    } else { //User pressed dialog's "OK" button.
      currentColor = colorChooser.getColor();
      //stopCellEditing();
    }
  }

  //Implement the one CellEditor method that AbstractCellEditor doesn't.
  public Object getCellEditorValue() {
    return currentColor;
  }

  //Implement the one method defined by TableCellEditor.
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    log.debug("value: " + value);
    currentColor = (Color) value;
    
    colorChooser.setColor(currentColor);
    log.debug("before");
    SwingUtilities.invokeLater(showdialog);
    log.debug("after");
          
    //return button;
    Component c = table.getCellRenderer(row, column).getTableCellRendererComponent(table, value, isSelected, true, row, column);
    log.debug(c);
    return c;
  }
}
