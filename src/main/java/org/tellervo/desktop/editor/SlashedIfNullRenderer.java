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
package org.tellervo.desktop.editor;


import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.JComponent;

import org.tellervo.desktop.sample.Sample;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;

/*
  problems with this:
  -- not enough use (if only 1 row, rest of display is white/background color)
  -- this makes everything left-aligned ... oops
  when those 2 are fixed, enable this by default (in SampleDataView.java).

  -- might be misleading if you have a blue background, etc., like peter does.
*/

@SuppressWarnings("serial")
public class SlashedIfNullRenderer extends DefaultTableCellRenderer {
    // draw slashes if
    // -- sample.editable==false and value==null
    // OR:
    // -- sample.editable==true and cell.isEditable==true

	private static final long serialVersionUID = 1L;

	public SlashedIfNullRenderer(Sample sample, TableModel model) {
	this.sample = sample;
	this.model = model;
    }
    private Sample sample;
    private TableModel model;

    @Override
	public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
	if (!sample.isEditable() && value==null)
	    return slasher;

	if (sample.isEditable() && !model.isCellEditable(row, column))
	    return slasher;

	return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	// TODO: if there's a marker on this cell, draw it.
	// TODO: doesn't right-align numbers (why not?)
	// TODO: if there's a comment on this year, use it as a tooltip.
	// TODO: if it's an MR, draw it as a special char (maybe '*'?), and/or color it differently.
    }

    private static final Color DARK = new Color(0.7333f, 0.7765f, 0.8431f);
    private static final Color LIGHT = new Color(0.8196f, 0.8510f, 0.9216f);
    private static final int THIN = 2;
    private static final int THICK = 5;

    private static JComponent slasher = new JComponent() {
	@Override
	public void paintComponent(Graphics g) {
	    setOpaque(true);

	    int w = getWidth(), h = getHeight();

	    // fill light
	    g.setColor(LIGHT);
	    g.fillRect(0, 0, w, h);

	    // dark stripes
	    g.setColor(DARK);
	    ((Graphics2D) g).setStroke(new BasicStroke(THIN-1f));
	    for (int x=0; x<w+h; x+=THIN+THICK)
		g.drawLine(x, 0, x-h, h);
	}
	};
}
