package corina.eyes;

import corina.Year;
import corina.util.Angle;

import java.awt.Color;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.image.BufferedImage;
import java.awt.geom.GeneralPath;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

import java.awt.image.*; // !!

import javax.swing.JPanel;

/**
   A panel showing the scanned section, the user's path across it, and
   each ring marked.
*/
public class SectionPanel extends JPanel
                       implements MouseListener, MouseMotionListener {

    private BufferedImage img;

    /*
      for starters, i'll let the user adjust:
      - contrast
      - brightness
      - sharpness
      and also a checkbox to invert it.

      contrast and brightness are easy: they're just RescaleOps.

    private BufferedImageOp contrastDown   = new RescaleOp(0.50f, +0.25f, null);
    private BufferedImageOp contrastUp     = new RescaleOp(2.00f, -0.50f, null);
    private BufferedImageOp brightnessDown = new RescaleOp(1.00f, -0.50f, null);
    private BufferedImageOp brightnessUp   = new RescaleOp(1.00f, +0.50f, null);

      i'll let the contrast go 1/4x to 4x, and brightness from -0.75 to +0.75.
      i.e., contrast = (x, x/2) for x=1/4..4, and brightness = (1, x) for x=-0.75..+0.75.

      sharpness is a little harder: i have to use a 3x3 ...

    private static final float[] SHARPEN3x3_3 = {
        0.f, -1.f,  0.f,
        -1.f,  5.f, -1.f,
        0.f, -1.f,  0.f,
    };

      -- TODO: how to use this?
      -- TODO: what's the general-case sharpen/blur?
      -- TODO: what limits to use?

      BUT: hmm, rescaleops are really really slow.  (>20sec for 800x800
      image, 1.3.1 or 1.4.1.)  strange.  until i've found a way to make
      it run acceptably fast, i can't use it.
    */

    private static final Stroke ARROW_STROKE = new BasicStroke(2.5f);
    private static final Stroke TICK_STROKE = new BasicStroke(2.0f);

    /**
       Draws the image, an arrow representing the path, and ticks for
       each year.
    */
    public void paintComponent(Graphics g) {
	// boilerplate
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;

	// draw image
	g2.drawImage(img, 0, 0, this);

	// for line drawing, use antialiasing
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);

	drawArrow(g2);

	drawTicks(g2);
    }

    // draw arrow along path; future: get Path to do it, itself
    private void drawArrow(Graphics2D g2) {
	// thick red line
	g2.setColor(Color.red);
	g2.setStroke(ARROW_STROKE);

	// dot
	g2.fillOval(path.a.x-3, path.a.y-3, 6, 6);

	// line
	g2.drawLine(path.a.x, path.a.y, path.b.x, path.b.y);

	// arrow
	double theta = Angle.angle(path.a, path.b);
	GeneralPath arrow = new GeneralPath();
	arrow.moveTo((float) (path.b.x + 10*Math.cos(theta+Math.PI*3/4)),
		     (float) (path.b.y + 10*Math.sin(theta+Math.PI*3/4)));
	arrow.lineTo((float) path.b.x, (float) path.b.y);
	arrow.lineTo((float) (path.b.x + 10*Math.cos(theta-Math.PI*3/4)),
		     (float) (path.b.y + 10*Math.sin(theta-Math.PI*3/4)));
	g2.draw(arrow);
    }

    // draw tick at each ring
    private void drawTicks(Graphics2D g2) {
	g2.setStroke(TICK_STROKE);
	g2.setColor(Color.red);

	double theta = Angle.angle(path.a, path.b);
	// Year y = new Year(); // start at 1001

	// FIXME: instead of getting Scanner.Rings from |scanner|,
	// just get a list of distances (double).  this is, after
	// all, what schweingruber *wants* to return.

	Point ticks[] = scanner.getTicks();
	for (int i=0; i<ticks.length; i++) {
	    Point t = ticks[i];

	    // draw tick; use generalpath, because it can take floats
	    GeneralPath gp = new GeneralPath();
	    gp.moveTo((float) (t.x - 10*Math.cos(theta+Math.PI/2)),
		      (float) (t.y - 10*Math.sin(theta+Math.PI/2)));
	    gp.lineTo((float) (t.x + 10*Math.cos(theta+Math.PI/2)),
		      (float) (t.y + 10*Math.sin(theta+Math.PI/2)));
	    g2.draw(gp);

	    // label it
	    /*
	      g2.rotate(theta, r.p.x, r.p.y);
	      g2.drawString(y.toString(), r.p.x, r.p.y);
	      g2.rotate(-theta, r.p.x, r.p.y);
	      y = y.add(+1); // next one
	    */
	}
    }

    // FIXME: shouldn't need scanner here; take Eyes, instead, and ask him to do stuff for ya.
    private Scanner scanner;

    private Path path;

    private Eyes eyes;

    /**
       Create a new SectionPanel.

       @param img the image to display
       @param path the path to follow
       @param scanner obsolete: used to find rings
       @param eyes a reference to the Eyes that contains me, for
       updating
     */
    public SectionPanel(BufferedImage img, Path path, Scanner scanner, Eyes eyes) {
	this.img = img;
	this.scanner = scanner;
	this.path = path;
	this.eyes = eyes;

	// set size
	int width = img.getWidth();
	int height = img.getHeight();
	size = new Dimension(width, height);

	// and add mouse listeners
	super.addMouseListener(this);
	super.addMouseMotionListener(this);
    }

    /**
       Returns the preferred size of the panel, which is the size of
       the image to be displayed.
       @return the size of the image
    */
    public Dimension getPreferredSize() {
	return size;
    }

    private Dimension size;

    //
    // mouse handlers
    //

    // point which is being dragged, or null, if nothing is being dragged
    private Point dragPoint = null;

    // we'll let the user hit this far away from a point and still call it a hit.
    // (actually, a rectangle, not a circle.)
    private static final int SLOP = 5;

    // did the user hit this point?
    private boolean hit(Point click, Point target) {
	Rectangle sloppy = new Rectangle(target.x-SLOP,
					 target.y-SLOP,
					 2*SLOP,
					 2*SLOP);
	return sloppy.contains(click);
    }

    /** Ignored. */
    public void mouseClicked(MouseEvent e) { }

    /** Ignored. */
    public void mouseEntered(MouseEvent e) { }

    /** Ignored. */
    public void mouseExited(MouseEvent e) { }

    /** Starts a drag. */
    public void mousePressed(MouseEvent e) {
	if (hit(e.getPoint(), path.a)) {
	    dragPoint = path.a;
	} else if (hit(e.getPoint(), path.b)) {
	    dragPoint = path.b;
	}
    }

    /** Ends a drag. */
    public void mouseReleased(MouseEvent e) {
	dragPoint = null;
    }

    /** Ignored. */
    public void mouseMoved(MouseEvent e) { }

    /** Move the point (of the user's path) being dragged, and scroll
	there, if necessary. */
    public void mouseDragged(MouseEvent e) {
	if (dragPoint == null)
	    return;

	dragPoint.x = e.getX();
	dragPoint.y = e.getY();

	// normalize
	if (dragPoint.x < 0)
	    dragPoint.x = 0;
	if (dragPoint.x > img.getWidth())
	    dragPoint.x = img.getWidth();
	if (dragPoint.y < 0)
	    dragPoint.y = 0;
	if (dragPoint.y > img.getHeight())
	    dragPoint.y = img.getHeight();

	scrollRectToVisible(new Rectangle(dragPoint));

	// rescan, repaint, etc.
	eyes.updateAll();

	// PERF: this redraws the entire panel, when only part of it
	// (where the arrow was, where it will be) need to be drawn.
	// can i reasonably do anything about that?
    }
}
