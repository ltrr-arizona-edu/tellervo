package corina.cross;

import corina.Year;
import corina.Range;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/*
  a renderer for ranges in jtable cells.

  draws the start year right-aligned, just left of center.
  draws the end year right-aligned, just right of the right edge.
  draws a "-" center aligned.

  left to do:
  -- odd-rows-blue feature
  ---- if isSelected, use table.getSelectionColor() for background
  ---- if !isSelected && colorOddRows, use Browser.oddRowColor for background
  ---- otherwise, use white(?)

  -- give it a minimum width, or draw "..." if it's too narrow?

  -- memoize dashWidth,descent?  nah, they're not very expensive, i
     think, and they'd need to watch the font for changes, anyway.
*/
public class RangeRenderer extends DefaultTableCellRenderer {

    // use Browser.oddRowColor
    private boolean colorOddRows = false;
    public RangeRenderer(boolean colorOddRows) {
	this.colorOddRows = colorOddRows;
    }
    public RangeRenderer() {
	// (needed by compiler)
    }

    private Range range;

    public Component getTableCellRendererComponent(JTable table, Object value,
						   boolean isSelected, boolean hasFocus,
						   int row, int column) {
	this.range = (Range) value;

	return super.getTableCellRendererComponent(table, value,
						   isSelected, hasFocus,
						   row, column);
    }

    public void paintComponent(Graphics g) {
	Graphics2D g2 = (Graphics2D) g;

	// compute baseline (from DecimalRenderer)
	int baseline = getHeight() - g2.getFontMetrics().getDescent(); // (is this right?)

	// fill background -- (is this needed/allowed/automatic?) (apparently it's needed.)
	g2.setColor(getBackground());
	g2.fillRect(0, 0, getWidth(), getHeight());

	// set foreground
	g2.setColor(getForeground());

	// center
	int center = getWidth() / 2;
	int dashWidth = g2.getFontMetrics().stringWidth(DASH);
	g2.drawString(DASH, center - dashWidth/2, baseline);

	// start
	String start = range.getStart().toString();
	int startWidth = g2.getFontMetrics().stringWidth(start);
	g2.drawString(start, center - dashWidth/2 - startWidth, baseline);

	// end
	String end = range.getEnd().toString();
	int endWidth = g2.getFontMetrics().stringWidth(end);
	int right = getWidth() - dashWidth/2;
	g2.drawString(end, right - endWidth, baseline);
    }

    private final static String DASH = " - ";
}
