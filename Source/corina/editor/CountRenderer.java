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

package corina.editor;

import java.awt.Component;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JTable;
import javax.swing.JComponent;
import javax.swing.table.TableCellRenderer;

public class CountRenderer extends JComponent implements TableCellRenderer {
    // GENERALIZE: take any number (double)
    private int val, max;
    public CountRenderer(int max) {
        // range is 0..max
        this.max = max;
    }

    // me!
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        val = ((Integer) value).intValue();
        return this;
    }

    // make it look like an aqua relevence control.  see:
    // http://developer.apple.com/techpubs/macosx/Essentials/AquaHIGuidelines/AHIGControls/Progress_In_ce_Controls.html
    public void paintComponent(Graphics g) {
        int w = getSize().width;
        double frac = (double) val / (double) max;
        int stop = (int) (frac * w);

        // zero is a special case
        if (val == 0) {
            // but that looks sort of silly
            //g.setColor(dark);
            //g.fillRect(2, TOP+(HEIGHT/2)-1, 3, 2);
            return;
        }

        // draw dark lines
        g.setColor(dark);
        //        for (int x=0; x<stop; x+=2)
        //            g.drawLine(x, TOP, x, TOP+HEIGHT-1);
        // actually, let's draw a big rectangle, instead.  it's simpler and maybe faster.
        g.fillRect(0, TOP, stop, HEIGHT);

        // draw light lines
        g.setColor(light);
        for (int x=1; x<stop; x+=2)
            g.drawLine(x, TOP, x, TOP+HEIGHT-1);
    }

    private static Color dark = new Color(136, 136, 136);
    private static Color light = new Color(184, 184, 184);
    private static int TOP=4, HEIGHT=8;
}
