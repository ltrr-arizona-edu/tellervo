package corina.gui;

import java.awt.LayoutManager2;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JLabel;

import java.util.List;
import java.util.ArrayList;

// a dialoglayout is basically 2 columns
// -- the left column is right-aligned ("labels")
// -- the right column is left-aligned ("controls")
// -- in a given row, everything is top-aligned
// -- a "header" is a left-aligned label in its own row
// -- a separator is a line in its own row
// all good?
// (this will be used for samplemetaview, siteproperties, and probably others.)

public class DialogLayout implements LayoutManager2 {
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
            if (r instanceof NormalRow)
                secondColumn = Math.max(secondColumn, ((NormalRow) r).l.getPreferredSize().width);
        }
        secondColumn += border.left + EXTRA;

        for (int i=0; i<n; i++) {
            Row r = (Row) rows.get(i);
            if (r instanceof HeaderRow) {
                System.out.println("y=" + lastY);

                // header
                Component header = ((HeaderRow) r).h;
                int w = parent.getWidth() - (border.left + border.right);
                int h = header.getMinimumSize().height;
                int x = border.left;
                int y = lastY;
                header.setBounds(x, y, w, h);
                lastY += h;

                System.out.println("   h=" + h);
                System.out.println("y=" + lastY);
            } else if (r instanceof NormalRow) {
                System.out.println("y=" + lastY);
                
                // label
                JLabel l = (JLabel) ((NormalRow) r).l;
                int w1 = l.getMinimumSize().width;
                int h1 =  l.getPreferredSize().height;
                int x1 = secondColumn - w1;
                int y1 = lastY;
                l.setBounds(x1, y1, w1, h1);

                // controller
                Component c = ((NormalRow) r).c;
                int w2 = c.getPreferredSize().width;
                w2 = Math.min(w2, parent.getWidth() - (border.right + PADDING + secondColumn));
                int h2 =  c.getPreferredSize().height;
                int x2 = secondColumn + PADDING; // buffer between 'em
                int y2 = lastY;
                c.setBounds(x2, y2, w2, h2);

                lastY += Math.max(h1, h2);

                System.out.println("   h1=" + h1);
                System.out.println("   h2=" + h2);
                System.out.println("y=" + lastY);
            }
        }
        
        // focus on the first one?  (is that my job?)  (is it even possible?)
    }
    public Dimension minimumLayoutSize(Container parent) {
        return getLayoutSize(parent, false);
    }
    public Dimension preferredLayoutSize(Container parent) {
        return getLayoutSize(parent, true);
    }
    public Dimension getLayoutSize(Container parent, boolean isPreferred) {
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
        int getHeight(boolean isPreferred) {
            return (isPreferred ? h.getPreferredSize().height : h.getMinimumSize().height);
        }
        int getWidth(boolean isPreferred) {
            return (isPreferred ? h.getPreferredSize().width : h.getMinimumSize().width);
        }
    }
    private class NormalRow extends Row {
        Component l, c;
        NormalRow(Component l, Component c) {
            this.l = l;
            this.c = c;
        }
        int getHeight(boolean isPreferred) {
            return Math.max((isPreferred ? l.getPreferredSize().height : l.getMinimumSize().height),
                            (isPreferred ? c.getPreferredSize().height : c.getMinimumSize().height));
        }
        int getLabelWidth(boolean isPreferred) {
            return (isPreferred ? l.getPreferredSize().width : l.getMinimumSize().width);
        }
        int getControlWidth(boolean isPreferred) {
            return (isPreferred ? c.getPreferredSize().width : c.getMinimumSize().width);
        }
    }
    private List rows = new ArrayList();

    public void addLayoutComponent(Component comp, Object constraints) {
        // constraints is either (1) null, meaning this is a header label (full span)
        // or (2) a String, which is the label for this component.
        if (constraints == null) {
            // add a header row
            rows.add(new HeaderRow(comp));

        } else if (!(constraints instanceof String)) {
            throw new IllegalArgumentException(); // you suck

        } else {
            JLabel label = new JLabel((String) constraints);

            // i need to add my own label.  i've no idea if adding a component
            // to my parent is legal in this context.  if it gives any trouble, yank it out,
            // and i'll just have to draw my own label.

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

    /*
     usage:
     JPanel panel = new JPanel(new DialogLayout());
     panel.add(new JLabel("Identification"));
     panel.add(new JTextField("", 32), "Name:");
     ...
     panel.add(makeHorizontalLine());
     */
}
