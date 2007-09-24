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

package corina.gui.layouts;

import java.awt.LayoutManager;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;

/**
    Make components equal-width, and right-aligned.

    <p>The most common example:</p>

    <table border="1" cellspacing="0" width="100%">
      <tr>
        <td align="right">

        <table border="1" cellspacing="0">
          <tr>
            <td width="20%" align="center">Cancel</td>
            <td width="20%" align="center"><b>OK</b></td>
          </tr>
        </table>

        </td>
      </tr>
    </table>

    <p>This is how <a href="http://???">Apple's Human Interface Guidelines</a> suggest placing
    buttons.  Even <a href="http://???">Sun's Java guidelines</a> suggest this, yet they provide
    no easy way to do it, nor do their demo examples follow it.  (The
    easiest way is to put a GridLayout in a right-aligned FlowLayout,
    and adjust the spacing.)</p>

    <p>The most common use of this is, of course, for buttons.  Put
    them in the SOUTH of a BorderLayout, and you're set.</p>

    <p>An easy way to use this is via the Layout class.  You can make a
    panel with a few buttons simply by saying</p>

<pre>
    JPanel buttons = Layout.buttonLayout(cancel, ok);
</pre>

    <p>Sometimes you'll want some buttons on the left, and the rest on
    the right.  You can put a gap in the layout by passing either null
    or a glue object (as created by Box.createHorizontalGlue()).</p>

<pre>
    JPanel buttons = Layout.buttonLayout(help, null, cancel, ok);
</pre>

    <p>This layout will put an appropriate amount of space between the
    buttons, but won't put any space around the outside of the buttons.
    You'll probably want a space around the entire window, anyway.</p>

    <h2>Left to do:</h2>
    <ul>
        <li>look up URLs
        <li>if rightmost buttons are "cancel" and "ok" (either order),
            make put them in the order "ok"-"cancel" on win32, "cancel"-"ok" on mac.
            (what to do on unix?)  (is that really better?)
        <li>refactor -- it's a bit confusing
    </ul>

    @see java.awt.BorderLayout
    @see corina.gui.Layout

    @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
    @version $Id$
*/
public class ButtonLayout implements LayoutManager {

    /**
       Make a new ButtonLayout.
    */
    public ButtonLayout() {
	// (needed only for a place to put the javadoc comment)
    }

    private static final int LEADING = 12; // suggests apple

    // fixme:
    // -- if there's a glue component (has maxwidth>1000),
    // 	-- align items before glue to left edge
    // 	-- align items after glue to right edge
    // -- (but still maintain consistent width for all items except glue)
    
    // REFACTOR!  i can't figure out how this mess works any more.

    /**
       Lay out the components in the specified container.

       @param parent the container to lay out the components in
    */
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

    // used by getLayoutSize() -- i make one here so i don't
    // have to make a new one each time the container is laid out
    // (which would cause lots of garbage to be created).
    private Dimension d = new Dimension();

    // get the size needed to lay out this container in |parent|.
    // if |isPreferred|, return the preferred size, else return
    // the minimum size.
    private Dimension getLayoutSize(Container parent, boolean isPreferred) {
        Insets border = parent.getInsets();

	d.height = d.width = 0;

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

    /**
        Return the minimum size required to lay out these components in
        the parent.

        @param the parent to lay out
        @return the minimum layout size
    */
    public Dimension minimumLayoutSize(Container parent) {
        return getLayoutSize(parent, false);
    }

    /**
        Return the preferred size required to lay out these components
        in the parent.

        @param the parent to lay out
        @return the preferred layout size
    */
    public Dimension preferredLayoutSize(Container parent) {
        return getLayoutSize(parent, true);
    }

    /**
        Optional, and not implemented.
    */
    public void addLayoutComponent(String name, Component comp) {
        // do nothing
    }

    /**
        Optional, and not implemented.
    */
    public void removeLayoutComponent(Component comp) {
        // do nothing
    }
}
