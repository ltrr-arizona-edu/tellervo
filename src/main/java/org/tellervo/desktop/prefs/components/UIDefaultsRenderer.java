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

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.TableCellRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UIDefaultsRenderer implements TableCellRenderer {
  private final static Logger log = LoggerFactory.getLogger(UIDefaultsRenderer.class);
  private ColorRenderer colorRenderer;
  private FontRenderer fontRenderer;

  public UIDefaultsRenderer(boolean isBordered) {
    colorRenderer = new ColorRenderer(isBordered);
    fontRenderer = new FontRenderer(isBordered);
  }

  public Component getTableCellRendererComponent(
    JTable table,
    Object obj,
    boolean isSelected,
    boolean hasFocus,
    int row,
    int column) {
      
    if (obj instanceof ColorUIResource) {
      log.trace("returning colorRenderer"+ obj + " " + row + " " + column);
      return colorRenderer.getTableCellRendererComponent(table, obj, isSelected, hasFocus, row, column);
    }
    if (obj instanceof FontUIResource) {
      log.trace("returning fontRenderer"+ obj + " " + row + " " + column);
      return fontRenderer.getTableCellRendererComponent(table, obj, isSelected, hasFocus, row, column);
    }
      
    return null;
  }

}
