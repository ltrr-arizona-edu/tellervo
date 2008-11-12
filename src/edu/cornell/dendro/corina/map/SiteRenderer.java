package edu.cornell.dendro.corina.map;

import edu.cornell.dendro.corina.site.LegacySite;
import edu.cornell.dendro.corina.util.ColorUtils;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.BasicStroke;
import java.awt.Graphics2D;

public class SiteRenderer {

    // need FONT-SETTING method here, too
    
    // draw a little label, like those flags on hors d'oeuvres to tell you
    // which ones have dead animals in them and which ones are food.
    // REFACTOR: make this an instance method
    // REFACTOR: passing in |view| is a wart
    // REFACTOR: passing in Offset, if it's not going to be a public class, is a wart
    public static void drawLabel(Graphics2D g2, Point p /* site point */, LegacySite site, int numSites,
                                 View view /* used only for clipping */, Point t /* bubble center */, boolean selected) {
        // measure the text
	String text = site.getCode();
        int textWidth = g2.getFontMetrics().stringWidth(text);
        int textHeight = g2.getFontMetrics().getHeight(); // MEMOIZE: don't recompute for each label

        // NEW POLICY: all computations in here should be OAOO -- again, this gets called way too many times

        // corners of round-rect
        rect.x = t.x - (textWidth/2 + EPS);
        rect.width = textWidth + 2*EPS;
        rect.y = t.y - (textHeight/2 + EPS/4);
        rect.height = textHeight + EPS/2;

        // IMPORTANT: don't draw off-screen labels!
        // NEXT THING TO DO: don't draw sites that aren't visible (outside of view.width/view.height)
        if (!rect.intersects(new Rectangle(view.size))) {
            // System.out.println("site " + text + " is out of range!");
            // INCORRECT!  need to check:
            // -- all of box (drop-shadow included) is out of range
            // -- point (of site, not label) is out of range
            // -- both are out of range on the same side -- stretchy crossing corner should still be drawn
            // (but this is not horrible, and helps performance quite a bit)
            return;
        }

        // color of label body
        Color body = g2.getColor();

	// color of outline, text - black is good
	Color outlineColor = Color.black; // body.darker().darker().darker();

	// selected?  50% darker.
	if (selected) {
	    // original idea: reverse 'em
	    //Color tmp = body;
	    //body = outlineColor;
	    //outlineColor = tmp;
	    body = new Color(body.getRed()/2, body.getGreen()/2, body.getBlue()/2);
	}

        // extra drop shadows -- REFACTOR?
        if (numSites > 1) {
            int extra = Math.min(numSites-1, 4); // up to 4 shadows

            shadow.width = rect.width;
            shadow.height = rect.height;

            for (int nr=0; nr<extra; nr++) {
                Color c = body;
                for (int i=extra; i>=nr; i--)
                    c = c.darker(); // PERF: very bad... O(n^2) allocations!

                // these 4 lines were in the "draw the bubble" block below
                shadow.x = rect.x + OFFSET*(extra-nr);
                shadow.y = rect.y + OFFSET*(extra-nr);

                // outline
                g2.setColor(outlineColor);
                g2.setStroke(outlineStroke);
                g2.drawRoundRect(shadow.x, shadow.y, shadow.width, shadow.height, ROUND, ROUND);

                // fill
                g2.setColor(c);
                g2.setStroke(fillStroke);
                g2.fillRoundRect(shadow.x, shadow.y, shadow.width, shadow.height, ROUND, ROUND);
            }
            g2.setColor(body);
        }

        // if no stretchy visible, don't draw it (very common case)
        boolean stretchy = !rect.contains(p);
        
        // draw frontmost bubble: outline
        g2.setColor(outlineColor);
        g2.setStroke(outlineStroke);
        g2.drawRoundRect(rect.x, rect.y, rect.width, rect.height, ROUND, ROUND);
        if (stretchy)
            drawStretchy(p, t, rect, textWidth, textHeight, g2, false);
        // BETTER: method to put triangle coords in a fixed array
        // why?  because almost everything drawStretchy() does, except for the last draw command,
        // is exactly the same for both calls.

        // draw frontmost bubble: stroke
        g2.setColor(body);
        g2.setStroke(fillStroke);
        g2.fillRoundRect(rect.x, rect.y, rect.width, rect.height, ROUND, ROUND);
        if (stretchy)
            drawStretchy(p, t, rect, textWidth, textHeight, g2, true);

        // (don't worry, roundrect doesn't appear to take up (much) more time than rect)

        // write the text (in white, if it's a really dark label)
        g2.setColor(ColorUtils.reallyDark(body) ? Color.white : outlineColor);
        g2.drawString(text, t.x - textWidth/2, t.y + textHeight/2 - EPS/2); // is this y-value right?
    }

    private static void drawStretchy(Point point, Point center, Rectangle label,
                                     int textWidth, int textHeight, Graphics2D g2, boolean fill) {
        // compute corners of label-rect
        kern.y = center.y - textHeight/8;
        kern.height = textHeight/4;
        kern.x = center.x - textWidth/8;
        kern.width = textWidth/4;
        // FIXME: using a rect is stupid, since i'll just keep computing right and bottom --
        // use top,bottom,left,right ints instead.

        // this point is always the same
        tx[0] = point.x; ty[0] = point.y;

        // first point (going CW)
        if (point.x>=kern.x && point.y<=kern.y) {
            tx[1] = kern.x;
            ty[1] = kern.y; }
        else if (point.y>=kern.y && point.x>=(kern.x+kern.width)) {
            tx[1] = kern.x+kern.width;
            ty[1] = kern.y; }
        else if (point.x<=kern.x+kern.width && point.y>=kern.y+kern.height) {
            tx[1] = kern.x+kern.width;
            ty[1] = kern.y+kern.height; }
        else {
            tx[1] = kern.x;
            ty[1] = kern.y+kern.height; }

        // second point (going CW)
        if (point.x<=kern.x && point.y>=kern.y) {
            tx[2] = kern.x;
            ty[2] = kern.y; }
        else if (point.y<=kern.y && point.x<=kern.x+kern.width) {
            tx[2] = kern.x+kern.width;
            ty[2] = kern.y; }
        else if (point.x>=kern.x+kern.width && point.y<=kern.y+kern.height) {
            tx[2] = kern.x+kern.width;
            ty[2] = kern.y+kern.height; }
        else {
            tx[2] = kern.x;
            ty[2] = kern.y+kern.height; }

        // draw polygon, then fill with color
        // (it really is too bad that i can't switch on method invocation, and also
        // that draw/fill isn't simply a boolean parameter, which is sort of related)
        if (!fill)
            g2.drawPolygon(tx, ty, 3);
        else
            g2.fillPolygon(tx, ty, 3); }
    
    private static Rectangle kern = new Rectangle(); // kernel = center of label, where triangles end

    private static Rectangle rect = new Rectangle(); // label
    private static Rectangle shadow = new Rectangle(); // drop-shadow

    private static int tx[] = new int[3], ty[] = new int[3]; // triangle corners (x,y)
    private static BasicStroke outlineStroke = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
    private static BasicStroke fillStroke = new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
    private static Point t = new Point();

    private static final int ROUND = 3; // roundness of round-rects
    private static final int OFFSET = 2; // offset of drop-shadows for multiple-sites
    private static final int EPS = 4; // small value: diameter of the dot, and distance from dot to text
}
