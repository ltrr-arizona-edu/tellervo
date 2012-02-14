/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.prefs.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class ColorEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
  private final static Logger log = LoggerFactory.getLogger(ColorEditor.class);  
  
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
    log.debug(c.toString());
    return c;
  }
}
