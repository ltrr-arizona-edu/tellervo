package corina.index;

import java.text.DecimalFormatSymbols;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

// given a String to render, draw everything before the |dot| to the left of
// a certain x-position, and everything after it to the right.  the |dot| char
// is centered at |position| of the way across the column.

public class DecimalRenderer extends DefaultTableCellRenderer {

    // default decimal point for this locale
    private char dot = new DecimalFormatSymbols().getDecimalSeparator();
    private String dotString = String.valueOf(dot);

    // sample is something like "0.00" (it has nothing to do with corina.Sample)
    public DecimalRenderer(String sample) {
	this.sample = sample;
    }

    private int offset=0;
    private String sample=null;
    private boolean offsetSet=false;

    public Component getTableCellRendererComponent(JTable table, Object value,
						   boolean isSelected, boolean hasFocus,
						   int row, int column) {
	// hack!
	if (!offsetSet) {
	    // use the table's font -- only needs to get set once
	    super.setFont(table.getFont());

	    // compute position from a sample value -- needs a table, too
	    int split = sample.indexOf(dot); // assumes: there is a dot somewhere

	    // if not, let's assume it's at the end -- HOW?  i think this is harder...
	    if (split == -1) {
		int width = table.getGraphics().getFontMetrics().stringWidth(sample);
		offset = width / 2;
		offsetSet = true;
	    } else {
		// good, there was a dot
		String leftValue = sample.substring(0, split);
		String rightValue = sample.substring(split+1);
		int leftWidth = table.getGraphics().getFontMetrics().stringWidth(leftValue);
		int rightWidth = table.getGraphics().getFontMetrics().stringWidth(rightValue);
		// if i don't know the column width here, i can't compute position
		// but i wouldn't want to, since it'd be wrong if it ever was resized
		// instead i should have a pixel offset from centerline
		offset = (leftWidth - rightWidth) / 2;

		offsetSet = true;
	    }
	}

	if (!(value instanceof String))
	    value = String.valueOf(value); // for Integers...
	setText((String) value); // assumes: value is a string
	if (isSelected) {
	    setBackground(table.getSelectionBackground());
	    setForeground(table.getSelectionForeground());
	} else {
	    setBackground(table.getBackground());
	    setForeground(table.getForeground());
	}
	return this;
    }

    public void paintComponent(Graphics g) {
	Graphics2D g2 = (Graphics2D) g;

	// compute baseline
	int baseline = getHeight() - g2.getFontMetrics().getDescent(); // (is this right?)

	// get text to draw
	String value = getText();

	// fill background -- (is this needed/allowed/automatic?)
	g2.setColor(getBackground());
	g2.fillRect(0, 0, getWidth(), getHeight()); // on isOpaque() only?

	// set foreground
	g2.setColor(getForeground());

	// get everything to the left of the dot
	int split = value.indexOf(dot); // assumes: there is a dot somewhere

	// hack -- no dot!
	if (split == -1) {
	    int guide = getWidth() / 2 + offset;
	    int width = g2.getFontMetrics().stringWidth(value);
	    // (TODO: draw elipsis here if not enough room)
	    g2.drawString(value, guide - width, baseline);
	    return;
	}

	String valueLeft = value.substring(0, split);

	// compute position the dot should be at
	int guide = offset + getWidth() / 2;

	// compute width of left half
	int dotWidth = g2.getFontMetrics().stringWidth(dotString);
	int leftWidth = g2.getFontMetrics().stringWidth(valueLeft);

	// if they go outside the cell, just draw "..."
	int width = g2.getFontMetrics().stringWidth(value);
	if ((guide - dotWidth/2 - leftWidth < 0) ||
	    (guide - dotWidth/2 - leftWidth + width >= getWidth())) {
	    String elipsis = "\u2026"; // "..." -- 3 dots -- the
	    // unicode is \u2026, but "..." is guaranteed -- pick one
	    int elipsisWidth = g2.getFontMetrics().stringWidth(elipsis);
	    g2.drawString(elipsis, getWidth()/2 - elipsisWidth/2, baseline);
	    return;
	}

	// draw them all at once
	g2.drawString(value, guide - dotWidth/2 - leftWidth, baseline);
    }
}
