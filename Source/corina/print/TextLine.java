package corina.print;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;

// there should be a lot of really simple subclasses of this -- basically everything
// except perhaps bargraphs, empty lines, and horizontal rules.  re-using height(),
// especially, is what the game is all about.

public class TextLine implements Line {

    private String text;
    private int size = 10;
    private Font font; // (the constructor sets this!)

    private boolean smallcaps = false; // what a hack!
    
    public TextLine(String text) {
        this.text = text;
        this.font = new Font("serif", Font.PLAIN, size);
    }
    public TextLine(String text, int size) {
        this.text = text;
        this.size = size;
        this.font = new Font("serif", Font.PLAIN, size);
    }
    public TextLine(String text, int size, boolean smallcaps) {
        this.text = text;
        this.size = size;
        this.smallcaps = smallcaps;
        this.font = new Font("serif", Font.PLAIN, size);
    }
    
    public void print(Graphics g, PageFormat pf, float y) {
        // baseline
        float baseline = (float) (y + height(g));
        Graphics2D g2 = (Graphics2D) g;

        // -- inefficient, and too specific => refactor
        if (smallcaps) {
            float x = (float) pf.getImageableX();
            Font smallFont = new Font("serif", Font.PLAIN, (int) (size * 0.8)); // sc are 80%?
            for (int i=0; i<text.length(); i++) {
                char c = text.charAt(i);
                boolean small = Character.isLowerCase(c);
                c = Character.toUpperCase(c);
                g2.setFont(small ? smallFont : font);
                String str = String.valueOf(c);
                g2.drawString(str, x, baseline);
                x += g2.getFontMetrics().stringWidth(str);
            }
            return;
        }

        g2.setFont(font);
        g2.drawString(text, (float) pf.getImageableX(), baseline);
    }

    public int height(Graphics g) {
        return g.getFontMetrics(font).getHeight();
    }
}
