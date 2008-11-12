package edu.cornell.dendro.corina.prefs;

import java.awt.Window;
import java.awt.Rectangle;

import java.util.StringTokenizer;

/**
   The position and size of a window.  It's stored <a
   href="http://www.x.org/">X</a>-style, i.e., "300x200+50+40" means a
   window 300 pixels wide, 200 pixels tall, with the upper-left corner
   50 pixels from the top of the screen and 40 pixels from the left
   edge of the screen.

   <p>Methods are provided for both encoding the geometry given a window
   (for storing in preferences, for example) and decoding a geometry
   string directly to a window (for reading from preferences, for example).</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Geometry {
    /** Given a window, generate a geometry string.
	@param w the window to examine
	@return a geometry string for this window */
    public static String encode(Window w) {
	Rectangle bounds = w.getBounds();
	return bounds.width + "x" + bounds.height + "+" +
	       bounds.x + "+" + bounds.y;
    }

    /** Given a window and a geometry string, set the window's
	geometry to that.  (If it would go off the edge of the
	screen, or be too small, it's adjusted, as little as possible.)
	@param w the window to move/resize
	@param geometry the geometry to use */
    public static void decode(Window w, String geometry) {
	// parse |geometry|
	Rectangle geom = new Rectangle();
	StringTokenizer tok = new StringTokenizer(geometry, "+x ");
	geom.width = Integer.parseInt(tok.nextToken());
	geom.height = Integer.parseInt(tok.nextToken());
	geom.x = Integer.parseInt(tok.nextToken());
	geom.y = Integer.parseInt(tok.nextToken());

	// make sure the window is big enough, and not off-screen.
	// (partially off-screen is ok, but not completely.  title bar
	// must be visible, and at least 100px wide on-screen, for dragging.)
	// WRITEME

	// set window bounds
	w.setBounds(geom);
    }
}
