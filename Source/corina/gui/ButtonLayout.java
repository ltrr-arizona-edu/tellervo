package corina.gui;

import java.awt.LayoutManager;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

// lays out items like a 1-row gridlayout inside a right-side flowlayout,
// just as most (apple's, sun's) HIGs say:
//                                           [ Cancel ] [   OK   ]
// i've never written a layout manager before, so i'm pretty sure this
// class is buggy ... help me ...

public class ButtonLayout implements LayoutManager {
    private static final int LEADING = 10; // hmm...
    
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            int maxwidth=0;
            int n = parent.getComponentCount();
            for (int i=0; i<n; i++) { // first pass: measure
                Component m = parent.getComponent(i);
                if (m.isVisible()) {
                    Dimension d = m.getPreferredSize();
                    maxwidth = Math.max(maxwidth, d.width);
                }
            }
            for (int i=0; i<n; i++) { // second pass: cut
                Component m = parent.getComponent(i);
                if (m.isVisible()) {
                    Dimension d = m.getPreferredSize();
                    m.setSize(maxwidth, d.height);
                    m.setLocation(parent.getWidth() - (LEADING + maxwidth)*(n-i), LEADING);
                }
            }
        }
    }

    public Dimension minimumLayoutSize(Container parent) {
        Dimension d = new Dimension(0, 0);

        int n = parent.getComponentCount();
        for (int i=0; i<n; i++) {
            Component m = parent.getComponent(i);
            Dimension s = m.getMinimumSize();
            d.height = Math.max(d.height, s.height);
            d.width = Math.max(d.width, s.width);
        }
        d.width *= n;

        d.height += 2*LEADING;
        d.width += 2*LEADING;
        return d;
    }
    public Dimension preferredLayoutSize(Container parent) {
        Dimension d = new Dimension(0, 0);

        int n = parent.getComponentCount();
        for (int i=0; i<n; i++) {
            Component m = parent.getComponent(i);
            Dimension s = m.getPreferredSize();
            d.height = Math.max(d.height, s.height);
            d.width = Math.max(d.width, s.width);
        }
        d.width *= n;

        d.height += 2*LEADING;
        d.width += 2*LEADING;
        return d;
    }

    public void addLayoutComponent(String name, Component comp) {
        // do nothing
    }
    public void removeLayoutComponent(Component comp) {
        // do nothing
    }
}
