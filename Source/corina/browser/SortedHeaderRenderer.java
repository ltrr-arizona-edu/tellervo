//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2003 Ken Harris <kbh7@cornell.edu>
//

package corina.browser;

import corina.util.ColorUtils;

import javax.swing.table.TableCellRenderer;
import javax.swing.JComponent;
import javax.swing.JTable;
import java.awt.Component;
import java.awt.RenderingHints;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

/**
    A table header renderer which adds a small triangle to the "sort" column.
    
    <p>In most tables, you can click on a header to sort the table by that column,
    and click again to sort backwards.  The header where you clicked also indicates
    that that column is the sort column with a small triangle, or an upside-down
    triangle if it's reversed.  (Mac OS also highlights the whole header cell.)
    This class provides the renderer (drawing only) to support such behavior in
    Swing JTables.</p>

    <p>To use a SortedHeaderRenderer, if <code>table</code> is your JTable, just say:</p>

<pre>
    SortedHeaderRenderer shr = new SortedHeaderRenderer(table);
    table.getTableHeader().setDefaultRenderer(shr);
</pre>
    
    <p>Remember, this class represents the view only.  It doesn't
    handle sorting, or deal with clicks in the header, or anything
    else.  It only draws little arrows in the header for the column
    you tell it.</p>

    <h2>Left to do</h2>
    <ul>
        <li>if text is too wide, put "..." before arrow, so they don't overlap
        <li>design: make this extend JComponent, so get() is simply return (... ? this : c);
        <li>design: should i make it set itself as the renderer, so you only have
        to say new SortedTableRenderer(table)?
    </ul>

    @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
    @version $Id$
*/
public class SortedHeaderRenderer implements TableCellRenderer {

    /**
        Make a new sorted header renderer.
    
        The table argument is only used for the calls
        <code>table.getTableHeader().getDefaultRenderer()</code>
        to get the default renderer for the table header.
        
        @param table the JTable which will use this class
        as its renderer
    */
    public SortedHeaderRenderer(JTable table) {
        normal = table.getTableHeader().getDefaultRenderer();
    }

    // the default header renderer
    private TableCellRenderer normal;

    // which column to sort by?  (as its text label)
    private String sortColumn = ""; // (don't use null)

    // is the sort reversed?
    private boolean reversed = false;

    /**
        Set a new sort column.  This is the text string to
        match of the headers.  The null value is not allowed.

        @param sortColumn the name of the new column which
        is used for sorting
    */
    public void setSortColumn(String sortColumn) {
        if (sortColumn == null)
            throw new NullPointerException();
    
        this.sortColumn = sortColumn;
    }

    /**
        Tell the renderer whether this sort is forward or reverse.
        If it's reversed, the triangle is drawn upside-down.

        @param reversed is the sort a reverse-sort?
    */
    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }

    /**
        Return the component which does the rendering.  This is used
        by the JTable architecture; you never need to call this explicitly.
        
        @return the default renderer component, with a triangle drawn
        on it, if its text matches the sort field
    */
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row, int column) {
        // figure out what would normally be drawn.
        final Component c = normal.getTableCellRendererComponent(table,
                                                                 value,
                                                                 isSelected,
                                                                 hasFocus,
                                                                 row,
                                                                 column);
        // i'm being asked to draw: |value|;
        // the sort is: |sortColumn|.
        if (sortColumn.equals(value)) {
            // make a new component.
            return new JComponent() {
                    public void paintComponent(Graphics g) {
                        // start with default
                        c.setBounds(getBounds());
                        c.paint(g);

                        // then paint my arrow on it
                        paintArrow(g);
                    }

                    // paint a little arrow on the right side of the header
                    private void paintArrow(Graphics g) {
                        // turn on antialiasing
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                            RenderingHints.VALUE_ANTIALIAS_ON);

                        // then paint little triangle arrow -- DOCUMENT THIS!

                        // compute points
                        int h = getHeight();
                        int w = getWidth();
                        
                        int top = h/3;
                        int bottom = h*2/3;

                        int right = w - h/3;
                        int left = right - h/3;
                        int middle = (right+left)/2;

                        // stuff in x,y arrays
                        x[0] = left;
                        x[1] = middle;
                        x[2] = right;
                        if (!reversed) {
                            y[0] = bottom;
                            y[1] = top;
                            y[2] = bottom;
                        } else {
                            y[0] = top;
                            y[1] = bottom;
                            y[2] = top;
                        }
                        
                        // draw it
                        g2.setColor(ColorUtils.reallyDark(c.getBackground()) ? LIGHTEN : DARKEN);
                        g2.fillPolygon(x, y, 3);
                    }
                };
        } else {
            return c;
        }
    }

    // x,y arrays for points to draw
    private int x[] = new int[3];
    private int y[] = new int[3];

    private final static Color DARKEN = ColorUtils.addAlpha(Color.black, 0.5f); // 50% opaque black
    private final static Color LIGHTEN = ColorUtils.addAlpha(Color.white, 0.5f); // 50% opaque white
}
