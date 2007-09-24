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

package edu.cornell.dendro.corina.browser;

import javax.swing.table.TableCellRenderer;
import javax.swing.JTable;
import java.awt.Component;
import java.awt.RenderingHints;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.Icon;

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
        @param defaultSortColumn the name of the default column to sort by. Can be null.
    */
    public SortedHeaderRenderer(JTable table, String defaultSortColumn) {
        normal = table.getTableHeader().getDefaultRenderer();
        if(defaultSortColumn != null)
        	sortColumn = defaultSortColumn;
        else
        	sortColumn = "";
    }

    // the default header renderer
    private TableCellRenderer normal;

    // which column to sort by?  (as its text label)
    private String sortColumn;

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
    
    private static class SortArrowIcon implements Icon {
    	private boolean reversed;
    	private int size;
    	
    	public SortArrowIcon(boolean reversed, int size) {
    		this.size = size;
    		this.reversed = reversed;
    	}
    	
    	public int getIconHeight() {
    		return size;
    	}
    	
    	public int getIconWidth() {
    		return size;
    	}
    	
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Color bgcolor = (c == null) ? Color.GRAY : c.getBackground();
			
			// turn on antialiasing
			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			int dx = (int) (size / 1.25);
			int dy = reversed ? -dx : dx;
			
			// magic: align icon with specified font size
			y += 5*size/6 + (reversed ? 0 : -dy);
			// give it some space!
			x += size*2;
			
			int shift = reversed ? -1 : 1;
			
			g.translate(x, y);
			
			// draw the right diagonal
			g.setColor(bgcolor.darker());
            g.drawLine(dx / 2, dy, 0, 0);
            g.drawLine(dx / 2, dy + shift, 0, shift);
            
            // Left diagonal. 
            g.setColor(bgcolor.brighter());
            g.drawLine(dx / 2, dy, dx, 0);
            g.drawLine(dx / 2, dy + shift, dx, shift);
            
            // Horizontal line. 
            if (!reversed) {
                g.setColor(bgcolor.darker().darker());
            } else {
                g.setColor(bgcolor.brighter().brighter());
            }
            g.drawLine(dx, 0, 0, 0);

            g.setColor(bgcolor);
            g.translate(-x, -y);			
		}
	}
    
    /**
	 * Return the component which does the rendering. This is used by the JTable
	 * architecture; you never need to call this explicitly.
	 * 
	 * @return the default renderer component, with a triangle drawn on it, if
	 *         its text matches the sort field
	 */
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row, int column) {
    	
        Component c = normal.getTableCellRendererComponent(table,
                value,
                isSelected,
                hasFocus,
                row,
                column);
        
        // if it's a normal table header (and it should be...)
        if(c instanceof JLabel) {
        	JLabel l = (JLabel) c;
        	
        	l.setHorizontalTextPosition(JLabel.LEFT);
        	
        	if(sortColumn.equals(value)) 
        		l.setIcon(new SortArrowIcon(reversed, l.getFont().getSize()));
        	else
        		l.setIcon(null); // no icon!
        }
        
        return c;	
    }
}
