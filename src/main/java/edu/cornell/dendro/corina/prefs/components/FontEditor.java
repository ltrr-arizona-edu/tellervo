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

import java.awt.Component;
import java.awt.Font;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2fprod.common.swing.JFontChooser;

@SuppressWarnings("serial")
public class FontEditor extends AbstractCellEditor implements TableCellEditor {
	private final static Logger log = LoggerFactory.getLogger(FontEditor.class);  
  private Font currentFont;
  private String text;

  private Runnable showdialog = new Runnable() {
    public void run() {
        Font f = JFontChooser.showDialog(new javax.swing.JFrame(), text, currentFont);
      if (f != null) currentFont = f;
      text = null;
      fireEditingStopped();
    }
  };

  public void setText(String text) {
    this.text = text;   
  }
  
  public void setFont(Font f) {
    currentFont = f;
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
