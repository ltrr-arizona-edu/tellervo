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

package edu.cornell.dendro.corina.gui.layouts;

import java.awt.LayoutManager2;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JLabel;

import java.util.List;
import java.util.ArrayList;

import javax.swing.*; // DEBUG

/**
    Put components in a vertical stack, with labels to the left.

    <p>This layout is commonly used for dialogs.  For example:</p>

    <table border="1" cellspacing="0" align="center">
        <tr>
            <td align="right">Name:</td>
            <td><input type="text" size="20" /></td>
        </tr>

        <tr>
            <form>
            <td align="right">Password:</td>
            <td><input type="password" size="20" /></td>
            </form>
        </tr>
    </table>

    <p>The left column has right-aligned labels, and the right column has left-aligned
    components.  In each row, everything is top-aligned.</p>

    <p>To use the DialogLayout, use Container's add(Component comp,
    Object constraints) method.  The <code>constraints</code> argument
    is the label to use (as a String, not a JLabel).  For example,
    to create the layout above, you could say:</p>

<pre>
     JPanel panel = new JPanel(new DialogLayout());
     panel.add(new JTextField("", 20), "Name:");
     panel.add(new JPasswordField("", 20), "Password:");
</pre>

    <p>If you want to break up your layout into sections, add a JLabel
    with Container's <code>add(Component comp)</code> method.  It will
    appear on the left side, with no additional label.</p>

    <h2>Left to do:</h2>
    <ul>
	<li>note: behavior changed -- if you want components not to
	    fill, you neet to put them in a flow-L yourself; (should
	    i offer to not-fill?) -- YES!  i need a fill/no-fill
	    option -- which is probably why i need to change the syntax
	    here, so constraint is the fill/no-fill option, not the label.
	    right?
        <li>Bug: some (container?) components don't line up!
        <li>Remove the add(comp) support?  it makes it much more complex,
            and can be easily achieved by the user
        <li>Need (better) documentation
        <li>Change interface?  Is add(component, label) really best?
	<li>Remove out.println() calls (or convert them to logging calls)
    </ul>

Usage:
<pre>

// pretend it's a 2-column grid layout
p.add(new JLabel("Filetype:"));
p.add(new JLabel("Filetype:"));

p.add(new JLine());

p.add(new JLabel("Filetype:"));
p.add(new JLabel("Filetype:"));

how about:

p.add(component, DialogLayout.LABEL);
p.add(component, DialogLayout.COMPONENT);
p.add(component, DialogLayout.COMPONENT_FILL); ??
p.add(component, DialogLayout.FULL_LINE);

where these are just marker objects?

-- and/or: new DialogLayout.LabelMarker(spacing) (??)

</pre>

    @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
    @version $Id$
*/
public class DialogLayout implements LayoutManager2 {
    /**
        Make a new DialogLayout.
    */
    public DialogLayout() {
        // no code -- this c'tor only for the javadoc tag
    }

    // WRITEME: make EXTRA, PADDING user-specifyable?

    // extra padding to the left of the label column
    private static final int EXTRA = 10;

    // padding between the two columns
    private static final int PADDING = 8;

    public void layoutContainer(Container parent) {
        int n = rows.size();
        Insets border = parent.getInsets();
        int lastY = border.top;

        // figure out where the second column should start
        int secondColumn = 0;
        for (int i=0; i<n; i++) {
            Row r = (Row) rows.get(i);
            if (r instanceof NormalRow) {
		NormalRow nr = (NormalRow) r;
		secondColumn = Math.max(secondColumn,
					// nr.l.getMinimumSize().width);
					nr.getLabelWidth(false));
	    }
        }
        secondColumn += border.left + EXTRA;

        for (int i=0; i<n; i++) {
            Row r = (Row) rows.get(i);
            if (r instanceof HeaderRow) {
                // header
                Component header = ((HeaderRow) r).h;
                int w = parent.getWidth() - (border.left + border.right);
                int h = header.getMinimumSize().height;
                int x = border.left;
                int y = lastY;
                header.setBounds(x, y, w, h);
                lastY += h;
            } else if (r instanceof NormalRow) {
                // label
                JLabel l = (JLabel) ((NormalRow) r).l;
                int w1 = l.getMinimumSize().width;
                // int h1 =  l.getPreferredSize().height;
		int h1 =  l.getMinimumSize().height;
                int x1 = secondColumn - w1;
                int y1 = lastY;
                l.setBounds(x1, y1, w1, h1);

                // control
                Component c = ((NormalRow) r).c;
                // WAS: int w2 = c.getPreferredSize().width;
		int w2 = c.getMaximumSize().width; // NEW!
                w2 = Math.min(w2, parent.getWidth() - (border.right + PADDING + secondColumn));
                int h2 =  c.getPreferredSize().height;
                int x2 = secondColumn + PADDING; // buffer between 'em
                int y2 = lastY;
                c.setBounds(x2, y2, w2, h2);

                lastY += Math.max(h1, h2);
            }
        }
        
        // focus on the first one?  (is that my job?)  (is it even possible?)
    }
    public Dimension minimumLayoutSize(Container parent) {
        return getLayoutSize(parent, false);
    }
    public Dimension preferredLayoutSize(Container parent) {
        // return getLayoutSize(parent, true);
	Dimension d = getLayoutSize(parent, true);
        // d.width = 10000; -- WHY?
	return d;
    }
    private Dimension getLayoutSize(Container parent, boolean isPreferred) {
        // strategy:
        // this.minwidth = max(header.minwidth, (max(label.minwidth) + max(controls.minwidth)))
        // this.minheight = sum foreach row (header,control) (header.minheight or max(label.minheight, control.minheight))
        int n = parent.getComponentCount();
        int w1=0, w2=0, wh=0;
        Insets border = parent.getInsets();
        int height = border.top + border.bottom;
        for (int i=0; i<n; i++) {
            Row r = (Row) rows.get(i);
            height += r.getHeight(isPreferred);
            if (r instanceof HeaderRow) {
                wh = Math.max(wh, ((HeaderRow) r).getWidth(isPreferred));
            } else {
                w1 = Math.max(w1, ((NormalRow) r).getLabelWidth(isPreferred));
                w2 = Math.max(w1, ((NormalRow) r).getControlWidth(isPreferred));
            }
        }
        wh = border.left + border.right + Math.max(wh, EXTRA+w1+PADDING+w2);
        return new Dimension(wh, height);
    }

    public void addLayoutComponent(String name, Component comp) {
        // it's a header...
        addLayoutComponent(comp, null);
    }
    public void removeLayoutComponent(Component comp) {
        // remove the row containing |comp|
        int n = rows.size();
        for (int i=0; i<n; i++) {
            Row r = (Row) rows.get(i);
            if (r instanceof HeaderRow && ((HeaderRow) r).h.equals(comp)) {
                rows.remove(r);
                return;
            } else if (r instanceof NormalRow && ((NormalRow) r).c.equals(comp)) {
                rows.remove(r);
                return;
            }
        }
    }

    // i'm too cool for LayoutManager, i have to use LayoutManager2.  yee-ha.
    private abstract class Row {
        abstract int getHeight(boolean isPreferred);
    }
    private class HeaderRow extends Row {
        Component h;
        HeaderRow(Component h) {
            this.h = h;
        }
        @Override
		int getHeight(boolean isPreferred) {
	    int headerHeight = (isPreferred ? h.getPreferredSize().height
				            : h.getMinimumSize().height);
            return headerHeight;
        }
        int getWidth(boolean isPreferred) {
	    int headerWidth = (isPreferred ? h.getPreferredSize().width
			                   : h.getMinimumSize().width);
            return headerWidth;
        }
    }
    private class NormalRow extends Row {
        Component l, c;
        NormalRow(Component l, Component c) {
            this.l = l;
            this.c = c;
        }
        @Override
		int getHeight(boolean isPreferred) {

            int labelHeight = (isPreferred ? l.getPreferredSize().height
			                   : l.getMinimumSize().height);

	    int controlHeight = (isPreferred ? c.getPreferredSize().height
				             : c.getMinimumSize().height);

	    return Math.max(labelHeight, controlHeight);
        }
        int getLabelWidth(boolean isPreferred) {
	    int labelWidth = (isPreferred ? l.getPreferredSize().width
			                  : l.getMinimumSize().width);
	    return labelWidth;
        }
        int getControlWidth(boolean isPreferred) {
	    int controlWidth = (isPreferred ? c.getPreferredSize().width
				            : c.getMinimumSize().width);
            return controlWidth;
        }
    }
    @SuppressWarnings("unchecked")
	private List rows = new ArrayList();

    @SuppressWarnings("unchecked")
	public void addLayoutComponent(Component comp, Object constraints) {
        // constraints is either (1) null, meaning this is a header
        // label (full span) or (2) a String, which is the label for
        // this component.
        if (constraints == null) {
            // add a header row
            rows.add(new HeaderRow(comp));
            //System.out.println("header row!  comp=" + comp);

        } else if (!(constraints instanceof String)) {
            throw new IllegalArgumentException(); // you suck
	    // BETTER: use the given object as a component,
	    // instead of jlabel((string) constraints)!

        } else {
            JLabel label = new JLabel((String) constraints);
            
            //System.out.println("normal row!  label=" + label);

            // i need to add my own label.  i've no idea if adding a component
            // to my parent is legal in this context.
            // (NOTE: if this fails, draw label myself)

            // short version: kids, don't try this at home.
            comp.getParent().add(label);

            // (it needs to come before this, apparently.)
            rows.add(new NormalRow(label, comp));
        }

        // WRITE ME -- done?
    }
    public float getLayoutAlignmentX(Container target) {
        return 0.5f;
    }
    public float getLayoutAlignmentY(Container target) {
        return 0.0f;
    }
    public void invalidateLayout(Container target) {
        // don't think i have to do anything here
    }

    public Dimension maximumLayoutSize(Container target) { // why?
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public static void main(String args[]) {
        JPanel p = new JPanel(new DialogLayout());
        p.add(new JLabel("Header"));
        p.add(new JLabel("A"), "1:");
        p.add(new JTextField("B"), "2:");
        p.add(new JPasswordField("C"), "3:");
        p.add(edu.cornell.dendro.corina.gui.Layout.borderLayout(
                    new JLabel("N"),
                    new JLabel("W"),
                    new JLabel("C"),
                    new JLabel("E"),
                    new JLabel("S")), "4:");

        JFrame f = new JFrame();
        f.setContentPane(p);
        
        f.pack();
        f.setVisible(true);
    }

    /*
      WORKING HERE!

      TODO: constraints object should be an inner class:

      DialogLayoutConstraints {
      - String label
      - bool: fill?
      }

      - how to do full-width labels?
    */
}
