package corina.print;

import java.awt.Graphics;
import java.awt.Font;
import java.awt.print.PageFormat;

public interface Line {
    // pick some good sizes -- constants, woo-hoo!
    public static final int TITLE_SIZE = 18;
    public static final int SECTION_SIZE = 14;
    public static final int NORMAL_SIZE = 10;

    // standard font -- best way to store? -- with the size consts above, this now seems
    // somewhere between 'redundant' and 'silly'.
    public static final Font NORMAL = new Font("serif", Font.PLAIN, NORMAL_SIZE);

    /** print the line, on page pf, at position y */
    public void print(Graphics g, PageFormat pf, float y);

    /** how tall is this line, in points? */
    public int height(Graphics g);
}
