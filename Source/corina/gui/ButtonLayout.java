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
    
    public void layoutContainer(Container parent) {
        Insets border = parent.getInsets();

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
            int wholeWidth = n*maxwidth + (n-1)*LEADING;
            for (int i=0; i<n; i++) { // second pass: cut
                Component m = parent.getComponent(i);
                if (m.isVisible()) {
                    Dimension d = m.getPreferredSize();
                    m.setSize(maxwidth, d.height);
                    m.setLocation(parent.getWidth() - wholeWidth - border.right + i*maxwidth + i*LEADING, border.top);
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
