package corina.util;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import java.awt.Dimension;
import java.awt.Color;

/** A line, since JSeparator apparently can't be used (reliably, anyway)
outside of menus. */

public class JLine extends JPanel implements SwingConstants {
    /** Create a new horizontal line. */
    public JLine() {
        this(HORIZONTAL);
    }
    /** Create a new line, whose orientation may be HORIZONTAL or VERTICAL. */
    public JLine(int orientation) {
        setBorder(BorderFactory.createLineBorder(Color.gray));

        if (orientation == HORIZONTAL) {
            setMaximumSize(new Dimension(1000, 1));
            setPreferredSize(new Dimension(100, 1));
            setMinimumSize(new Dimension(1, 1));
        } else if (orientation == VERTICAL) {
            setMaximumSize(new Dimension(1, 1000));
            setPreferredSize(new Dimension(1, 100));
            setMinimumSize(new Dimension(1, 1));
        } else {
            throw new IllegalArgumentException("orientation must be VERTICAL or HORIZONTAL");
        }
    }
}
