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

package corina.print;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;

// there should be a lot of really simple subclasses of this --
// basically everything except perhaps bargraphs, empty lines, and
// horizontal rules.  re-using height(), especially, is what the game
// is all about.

/**
   A line of text.

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class TextLine implements Line {

    private String text;
    private int size = NORMAL_SIZE;
    private Font font; // (the constructor sets this!)

    /**
       Create a text line, of the normal size.

       @param text the text to write
    */
    public TextLine(String text) {
        this.text = text;
        this.font = new Font("serif", Font.PLAIN, size);
    }

    /**
       Create a text line.

       @param text the text to write
       @param size the size to use
    */
    public TextLine(String text, int size) {
        this.text = text;
        this.size = size;
        this.font = new Font("serif", Font.PLAIN, size);
    }

    public void print(Graphics g, PageFormat pf, float y) {
        // baseline
        float baseline = (float) (y + height(g));
        Graphics2D g2 = (Graphics2D) g; // needed for drawString()

	g2.setFont(font);
	g2.drawString(text, (float) pf.getImageableX(), baseline);
    }

    public int height(Graphics g) {
        return g.getFontMetrics(font).getHeight();
    }
}
