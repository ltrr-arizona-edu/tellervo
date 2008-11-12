package edu.cornell.dendro.corina.prefs.components;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

import edu.cornell.dendro.corina.logging.CorinaLog;

public class UIDefaultsRenderer implements TableCellRenderer {
  private static CorinaLog log = new CorinaLog(UIDefaultsRenderer.class);
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
