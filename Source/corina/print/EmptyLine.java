package corina.print;

import java.awt.Graphics;
import java.awt.print.PageFormat;

public class EmptyLine implements Line {
    private int height=18; // 1/4"
    public EmptyLine() {
    }
    public void print(Graphics g, PageFormat pf, float y) {
        // do nothing.
    }
    public int height(Graphics g) {
        return height;
    }
}
