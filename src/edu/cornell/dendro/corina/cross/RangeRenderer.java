package edu.cornell.dendro.corina.cross;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import edu.cornell.dendro.corina.Range;

/*
 * a renderer for ranges in jtable cells.
 * 
 * draws the start year right-aligned, just left of center. draws the end year
 * right-aligned, just right of the right edge. draws a "-" center aligned.
 * 
 * left to do: -- odd-rows-blue feature ---- if isSelected, use
 * table.getSelectionColor() for background ---- if !isSelected && colorOddRows,
 * use Browser.oddRowColor for background ---- otherwise, use white(?)
 *  -- give it a minimum width, or draw "..." if it's too narrow?
 *  -- memoize dashWidth,descent? nah, they're not very expensive, i think, and
 * they'd need to watch the font for changes, anyway.
 */
public class RangeRenderer extends DefaultTableCellRenderer {
  private final static String DASH = " - ";
  // use Browser.oddRowColor
  private boolean colorOddRows = false;
  private Range range;

  public RangeRenderer() {}

  public RangeRenderer(boolean colorOddRows) {
    this.colorOddRows = colorOddRows;
  }

  private int preferredWidth;

  @Override
public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    this.range = (Range) value;
    final FontMetrics fm = table.getGraphics().getFontMetrics();
    preferredWidth = fm.stringWidth(DASH) + fm.stringWidth(range.getStart().toString()) + fm.stringWidth(range.getEnd().toString());
    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
  }

  @Override
public Dimension getPreferredSize() {
    Dimension d = super.getPreferredSize();
    d.width = preferredWidth;
    return d;
  }

  @Override
public void paintComponent(Graphics g) {
    //final Graphics2D g2 = (Graphics2D) g;

    final FontMetrics fm = g.getFontMetrics();
    // compute baseline (from DecimalRenderer)
    final int baseline = getHeight() - fm.getDescent(); // (is this right?)

    // fill background -- (is this needed/allowed/automatic?) (apparently it's
    // needed.)
    g.setColor(getBackground());
    g.fillRect(0, 0, getWidth(), getHeight());

    // set foreground
    g.setColor(getForeground());

    // center
    //int center = getWidth() / 2;
    final int width = getWidth();
    final int dashWidth = fm.stringWidth(DASH);
    final int startOfDash = (width - dashWidth) / 2;
    g.drawString(DASH, startOfDash, baseline);

    // start
    final String start = range.getStart().toString();
    int startWidth = fm.stringWidth(start);
    g.drawString(start, startOfDash - startWidth, baseline);

    // end
    final String end = range.getEnd().toString();
    int endWidth = fm.stringWidth(end);
    int right = width - dashWidth / 2;
    g.drawString(end, right - endWidth, baseline);
  }
}