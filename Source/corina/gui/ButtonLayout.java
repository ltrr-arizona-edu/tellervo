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

package corina.gui;

import java.awt.LayoutManager;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;

// lays out items like a 1-row gridlayout inside a right-side flowlayout,
// just as most (apple's, sun's) HIGs say:
//                                           [ Cancel ] [   OK   ]

public class ButtonLayout implements LayoutManager {
    private static final int LEADING = 12; // says apple

    // fixme:
    // -- if there's a glue component (has maxwidth>1000),
    // 	-- align items before glue to left edge
    // 	-- align items after glue to right edge
    // -- (but still maintain consistent width for all items except glue)
    
    public void layoutContainer(Container parent) {
        Insets border = parent.getInsets();

        synchronized (parent.getTreeLock()) {
            int maxwidth=0;
            int n = parent.getComponentCount();
            int glueIndex = -1;
            int nn=0; // nn is like n, but doesn't count glue
            for (int i=0; i<n; i++) { // first pass: measure
                Component m = parent.getComponent(i);
                if (m.getMaximumSize().width > 32000) {
                    glueIndex = i; // is-a glue object
                    continue;
                }
                if (m.isVisible()) {
                    Dimension d = m.getPreferredSize();
                    maxwidth = Math.max(maxwidth, d.width);
                    if (glueIndex != -1)
                        nn++;
                }
            }
            if (nn == 0) // if they're only on the right side...
                nn = n;
            int wholeWidth = nn*maxwidth + (nn-1)*LEADING;
            boolean beforeGlue = (glueIndex!=-1);
            int j=0;
            for (int i=0; i<n; i++) { // second pass: cut
                Component m = parent.getComponent(i);
                if (i == glueIndex) {
                    beforeGlue = false;
                    continue;
                }
                if (m.isVisible()) {
                    Dimension d = m.getPreferredSize();
                    m.setSize(maxwidth, d.height);
                    if (beforeGlue) {
                        m.setLocation(border.left + i*(maxwidth + LEADING), border.top);
                    } else {
                        int x = parent.getWidth() - wholeWidth - border.right + j*(maxwidth + LEADING);
                        m.setLocation(parent.getWidth() - wholeWidth - border.right + j*(maxwidth + LEADING), border.top);
                        j++; // j is like i, but doesn't count the glue
                    }
                }
            }
        }
    }

    private Dimension getLayoutSize(Container parent, boolean isPreferred) {
        Insets border = parent.getInsets();

        Dimension d = new Dimension(0, 0);

        int n = parent.getComponentCount();
        for (int i=0; i<n; i++) {
            Component m = parent.getComponent(i);
            Dimension s = (isPreferred ? m.getPreferredSize() : m.getMinimumSize());
            d.height = Math.max(d.height, s.height);
            d.width = Math.max(d.width, s.width);
        }
        d.width = (n-1)*LEADING + n*d.width;

        // insets
        d.width += border.left + border.right;
        d.height += border.top + border.bottom;
        return d;
    }
    public Dimension minimumLayoutSize(Container parent) {
        return getLayoutSize(parent, false);
    }
    public Dimension preferredLayoutSize(Container parent) {
        return getLayoutSize(parent, true);
    }

    public void addLayoutComponent(String name, Component comp) {
        // do nothing
    }
    public void removeLayoutComponent(Component comp) {
        // do nothing
    }
}
