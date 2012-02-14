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
package org.tellervo.desktop.index;

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.text.DecimalFormatSymbols;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Given a String to render, draw everything before the |dot| to the left of a
 * certain x-position, and everything after it to the right. the |dot| char is
 * centered at |position| of the way across the column.
 * aaron [
 * we have a couple
 * of options with how to deal with the presence or absence of decimal point
 * when doing the layout currently we just add the decimal point (and its width)
 * if the decimal point is present in the incoming source value (regardless of
 * the sample format) that means in a sample format WITHOUT a decimal point,
 * decimal source values will be left shifted a half-decimal-point-width to
 * accomate the decimal, while in sample formats WITH a decimal point, incoming
 * source values without a decimal point will not be left shifted this amount -
 * the other option is to fix the widt of the decimal point shift depending on
 * whether the decimal point was present in the sample format or not that would
 * result in all values lining up, regardless of whether they were mixed (some
 * with decimal point, some without) it's not really a big deal so I'm not going
 * to do anythin about this right now
 * ] aaron 1/18/05
 */
@SuppressWarnings("serial")
public class DecimalRenderer extends DefaultTableCellRenderer {
  // "..." -- 3 dots -- the
  // unicode is \u2026, but "..." is guaranteed -- pick one
  // default decimal point for this locale
  private static final String elipsis = "\u2026";

  private static char dot = new DecimalFormatSymbols().getDecimalSeparator();
  private static String dotString = String.valueOf(dot);

  private int offset = 0;
  private String sample = null;
  private boolean offsetSet = false;

  // sample is something like "0.00" (it has nothing to do with tellervo.Sample)
  public DecimalRenderer(String sample) {
    this.sample = sample;
  }

  @Override
public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    // hack!
    if (!offsetSet) {
      final FontMetrics fm = table.getGraphics().getFontMetrics();
      // use the table's font -- only needs to get set once
      //super.setFont(table.getFont());

      // compute position from a sample value -- needs a table, too
      final int split = sample.indexOf(dot); // assumes: there is a dot somewhere

      // if not, let's assume it's at the end -- HOW? i think this is harder...

      // aaron [ see note in class doc ]

      if (split == -1) {
        offset = fm.stringWidth(sample) / 2;
      } else {
        // good, there was a dot
        String leftValue = sample.substring(0, split);
        String rightValue = sample.substring(split + 1);
        int leftWidth = fm.stringWidth(leftValue);
        int rightWidth = fm.stringWidth(rightValue);
        // if i don't know the column width here, i can't compute position
        // but i wouldn't want to, since it'd be wrong if it ever was resized
        // instead i should have a pixel offset from centerline
        offset = (leftWidth - rightWidth) / 2;
      }
      offsetSet = true;
    }
    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
  }

  @Override
public void paintComponent(Graphics g) {
    final FontMetrics fm = g.getFontMetrics();
    // compute baseline
    final int baseline = getHeight() - fm.getDescent(); // (is this right?)

    // get text to draw
    final String value = getText();

    // fill background -- (is this needed/allowed/automatic?)
    g.setColor(getBackground());
    g.fillRect(0, 0, getWidth(), getHeight()); // on isOpaque() only?

    // set foreground
    g.setColor(getForeground());

    // get everything to the left of the dot
    final int split = value.indexOf(dot); // assumes: there is a dot somewhere
    final int width = getWidth();
    final int center = width / 2;
    // compute position the dot should be at
    final int guide = center + offset;

    final int strwidth = fm.stringWidth(value);

    int dotWidth;
    String valueLeft;

    // hack -- no dot!
    if (split == -1) {
      // a string with no decimal is essentially
      // a string with a "phantom" decimal at the
      // very end
      // the decimal is "phantom" because it is not
      // drawn (dotWidth = 0) so the text will
      // NOT be aligned with values with decimals
      dotWidth = 0;
      valueLeft = value;
    } else {
      dotWidth = fm.stringWidth(dotString);
      valueLeft = value.substring(0, split);
    }

    final int halfDotWidth = dotWidth / 2;

    // compute width of left half
    final int leftWidth = fm.stringWidth(valueLeft);

    // if they go outside the cell, just draw "..."
    if ((guide - halfDotWidth - leftWidth < 0)
        || (guide - halfDotWidth - leftWidth + strwidth >= width)) {
      final int elipsisWidth = fm.stringWidth(elipsis);
      g.drawString(elipsis, center - elipsisWidth / 2, baseline);
      return;
    }

    // draw them all at once
    g.drawString(value, guide - halfDotWidth - leftWidth, baseline);
  }
}
