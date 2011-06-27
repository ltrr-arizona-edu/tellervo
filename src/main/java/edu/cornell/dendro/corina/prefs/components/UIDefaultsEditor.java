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
package edu.cornell.dendro.corina.prefs.components;

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

import edu.cornell.dendro.corina.logging.CorinaLog;

/**
 * @author Aaron
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
@SuppressWarnings("serial")
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

  @Override
public void cancelCellEditing() {
    chosenEditor.cancelCellEditing();
  }

  @Override
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
