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
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package edu.cornell.dendro.corina.editor;

import java.awt.Component;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

// (i know DefaultTableCellRenderer is a JLabel, but it also makes
// revalidate(), etc., into no-ops, which helps performance.  if i
// didn't extend this, i should override those to be no-ops myself,
// but i'm lazy, and this works nearly as well.)

public class CountRenderer extends DefaultTableCellRenderer {
    private int val, max;
    public CountRenderer(int max) { // GENERALIZE: take any number (double)
        // range is 0..max
        this.max = max;

        // we are opaque (is this needed still?)
        setOpaque(true);
    }
    
    public void setMax(int max) {
    	this.max = max;
    }

    // me!
    @Override
	public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        // update my value
        val = (value == null) ? 0 : ((Number) value).intValue();

        // set background color
        super.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());

        // return myself
        return this;
    }

    // make it look like an aqua relevence control.  see:
    // http://developer.apple.com/techpubs/macosx/Essentials/AquaHIGuidelines/AHIGControls/Progress_In_ce_Controls.html
    @Override
	public void paintComponent(Graphics g) {
        int w=getWidth(), h=getHeight();
        double frac = (double) val / (double) max;
        int stop = (int) (frac * w);

        // fill background myself. (why do i have to?)
        g.setColor(super.getBackground());
        g.fillRect(0, 0, w, h);

        // zero is a special case
        if (val == 0) {
            // but that looks sort of silly
            //g.setColor(DARK);
            //g.fillRect(2, TOP+(HEIGHT/2)-1, 3, 2);
            return;
        }

        // draw dark lines -- well, just draw a big rectangle.  it's simpler and probably faster.
        g.setColor(DARK);
        g.fillRect(0, TOP, stop, HEIGHT);

        // draw light lines
        g.setColor(LIGHT);
        for (int x=1; x<stop; x+=2)
            g.drawLine(x, TOP, x, TOP+HEIGHT-1);
    }

    private static Color DARK = new Color(136, 136, 136);
    private static Color LIGHT = new Color(184, 184, 184);
    private static int TOP=4, HEIGHT=8;
}
