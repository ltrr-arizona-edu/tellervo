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
            setMaximumSize(new Dimension(1000, THIN));
            setPreferredSize(new Dimension(100, THIN));
            setMinimumSize(new Dimension(THIN, THIN));
        } else if (orientation == VERTICAL) {
            setMaximumSize(new Dimension(THIN, 1000));
            setPreferredSize(new Dimension(THIN, 100));
            setMinimumSize(new Dimension(THIN, THIN));
        } else {
            throw new IllegalArgumentException("orientation must be" +
					       "VERTICAL or HORIZONTAL");
        }
    }

    // 1 doesn't work in 1.4?, 2 looks too thick.  maybe i'll need to write my own.
    private static final int THIN = 1;
}
