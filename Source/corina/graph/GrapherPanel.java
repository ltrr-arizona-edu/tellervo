//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package corina.graph;

import corina.Year;
import corina.Range;
import corina.Sample;
import corina.gui.XFrame;
import corina.gui.Bug;
import corina.editor.Editor;
import corina.util.ColorUtils;
import corina.util.Platform;

import java.util.List;

import java.awt.Dimension;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Cursor;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JOptionPane;

public class GrapherPanel extends JPanel
                                implements KeyListener,
					   MouseListener,
					   MouseMotionListener,
					   AdjustmentListener
{
    /** Width in pixels of one year on the x-axis. */
    public /* final */ int yearSize;

    // data
    /* private */ public List graphs;		// of Graph
    /* private */ public int current = 0;	// currenly selected sample
    /* private */ public Range bounds;		// bounds for entire graph

    // gui
    private JScrollBar horiz;
    private JFrame myFrame;			// for setTitle(), dispose()

    // (precomputed)
    private boolean baselines;			// draw baselines below graphs?  (much slower (--only if it's done naively, fixme))

    // drawing agent!
    private StandardPlot myAgent;

    // compute the initial range of the year-axis
    // (union of all graph ranges)
    // fixme: put |bounds| dfn here, change method name to be similar (computeBounds()?)
    // setPreferredSize() doesn't belong here, either.
    // -- not threadsafe -- bounds changes
    private void computeRange() {
        if (graphs.isEmpty()) {
            // can't happen
            Bug.bug(new IllegalArgumentException("no graphs!"));
            // bounds = new Range();
        } else {
            Graph g0 = (Graph) graphs.get(0);
            bounds = g0.getRange();
            for (int i=1; i<graphs.size(); i++) { // this looks familiar ... yes, i do believe it's (reduce), again, just like in sum.  ick.
                Graph g = (Graph) graphs.get(i);
                bounds = bounds.union(g.getRange());
            }
        }
        setPreferredSize(new Dimension(bounds.span() * yearSize, 200));
    }
	
    // here, we union ranges in Graphs; in Sum.java, we union ranges
    // in Elements.  is there a way to combine this, like lisp's
    // (collect elements 'union :slot range)?

    /** Returns true, to indicate that this panel can accept focus.
	(Needed to respond to KeyEvents.)
	@return true, meaning can-accept-focus */
    public boolean isFocusTraversable() {
	return true;
    }

    // ok, this is the magic bullet i was looking for.
    public boolean isManagingFocus() {
	return true;
    }

    // update the window title with the current graph's title
    public void updateTitle() {
	// current graph, and its title
	Graph g = (Graph) graphs.get(current);
	String title = g.graph.toString();

	// if offset, use "title (at ...)"
	if (g.xoffset != 0)
	    title += " (at " + g.getRange() + ")";

	// set title
	myFrame.setTitle(title);
    }

    // for horiz scrollbar
    public void adjustmentValueChanged(AdjustmentEvent ae) {
	repaint();
    }

    // hack to get the horizontal scrollbar to this
    public void setHoriz(JScrollBar h) {
	horiz = h;

	// add update listener, required for keeping baselines drawn
	if (baselines)
	    horiz.addAdjustmentListener(this);
    }

    // toggle baselines
    public void setBaselinesVisible(boolean visible) {
	// toggle baselines
	baselines = !baselines;
	System.setProperty("corina.graph.baselines", String.valueOf(baselines));

	// add/remove listener so they get updated properly
	if (!baselines)
	    horiz.removeAdjustmentListener(this);
	else
	    horiz.addAdjustmentListener(this);

	// redraw
	repaint();
    }
    public boolean getBaselinesVisible() {
	return baselines;
    }

    // used for clickers and draggers: get graph nr at point
    public int getGraphAt(Point p) {
	// try each sample...
	for (int i=0; i<graphs.size(); i++) {
	    // get graph
	    Graph gg = (Graph) graphs.get(i);

	    // hit?
	    if (myAgent.contact(gg, p))
		return i;
	}

	// fail: -1
	return -1;
    }

    // MouseMotionListener, for vertical line under cursor ----------
    private Point dragStart=null;
    private int startX; // initial xoff
    public void mouseDragged(MouseEvent e) {
	// didn't drag from a graph?  sorry.
	if (clicked == -1)
	    return;

	// just starting a drag?
	if (dragStart == null) {
	    // dragging something?
	    int n = getGraphAt(e.getPoint());

	    // nope, ignore
	    if (n == -1)
		return;

	    // yes, store
	    dragStart = (Point) e.getPoint().clone();
	    dragStart.y += ((Graph) graphs.get(n)).yoffset;
	    startX = ((Graph) graphs.get(n)).xoffset;

	    // select it, too, while we're at it
	    current = n;
	}

	// change yoffset[n]
	((Graph) graphs.get(current)).yoffset = (int) dragStart.getY() - e.getY();

	// change xoffset[n], but only if no shift
	int dx = 0;
	if (!e.isShiftDown()) {
	    dx = (int) (e.getX() - dragStart.getX());
	    dx -= dx % yearSize;
	}
	((Graph) graphs.get(current)).xoffset = startX + (int) dx / yearSize;

	// repaint
	updateTitle();
	repaint();
    }
    private int cursorX=0;
    // this is bug #199, because mouseMoved events stop being generated as soon as a
    // mouseExited event is fired.  idea: compute dx, and on mouseExited set cursorX
    // to (dx<0 ? minCursorX : maxCursorX), but that doesn't take into account moving
    // off the bottom.  there's got to be a way to track mouseMoved events for the focused
    // window regardless of the position of the mouse.
    public void mouseMoved(MouseEvent e) {
	// old cursorX
	int old = cursorX;

	// update cursorX
	cursorX = e.getX();

	// put it on the nearest gridline
	if (cursorX % yearSize < yearSize/2)
	    cursorX -= (cursorX % yearSize);
	else
	    cursorX -= (cursorX % yearSize) - yearSize;

	// refresh! -- but only if necessary
	if (cursorX != old)
	    repaint();
    }
    // ------------------------------------------------------------

    // KeyListener ------------------------------------------------------------
    /** Deal with key-pressed events, as described above.
	@param e the event to process */
    public void keyPressed(KeyEvent e) {
	// extract some info once so i don't have to do it later
	int m = e.getModifiers();
	int k = e.getKeyCode();

	// repaint graph?
	boolean repaint = false;

	// unknown key?
	boolean unknown = false;

	// parse it...ugh
	if (m == KeyEvent.SHIFT_MASK) { // shift keys
	    switch (k) {
	    case KeyEvent.VK_TAB:
		current = (current == 0 ? graphs.size() - 1 : current - 1);
		repaint = true;
		break;
	    case KeyEvent.VK_PERIOD:
		((Graph) graphs.get(current)).bigger();
		repaint = true;
		break;
	    case KeyEvent.VK_COMMA:
		((Graph) graphs.get(current)).smaller();
		repaint = true;
		break;
	    case KeyEvent.VK_EQUALS:
		((Graph) graphs.get(current)).slide(1);
		repaint = true;
		break;
	    default:
		unknown = true;
	    }
	} else if (m == KeyEvent.CTRL_MASK) { // control keys
	    switch (k) {
	    case KeyEvent.VK_LEFT:
		((Graph) graphs.get(current)).left();
		repaint = true;
		break;
	    case KeyEvent.VK_RIGHT:
		((Graph) graphs.get(current)).right();
		repaint = true;
		break;
	    default:
		unknown = true;
	    }
	} else { // unmodified keys
	    switch (k) {
	    case KeyEvent.VK_UP:
		((Graph) graphs.get(current)).slide(10);
		repaint = true;
		break;
	    case KeyEvent.VK_DOWN:
		((Graph) graphs.get(current)).slide(-10);
		repaint = true;
		break;
	    case KeyEvent.VK_MINUS:
		((Graph) graphs.get(current)).slide(-1);
		repaint = true;
		break;
	    case KeyEvent.VK_EQUALS: // unshifted equals == plus
		((Graph) graphs.get(current)).slide(1);
		repaint = true;
		break;
	    case KeyEvent.VK_LEFT:
		horiz.setValue(horiz.getValue() - horiz.getUnitIncrement());
		break;
	    case KeyEvent.VK_RIGHT:
		horiz.setValue(horiz.getValue() + horiz.getUnitIncrement());
		break;
	    case KeyEvent.VK_PAGE_UP:
		horiz.setValue(horiz.getValue() - horiz.getBlockIncrement());
		break;
	    case KeyEvent.VK_PAGE_DOWN:
		horiz.setValue(horiz.getValue() + horiz.getBlockIncrement());
		break;
	    case KeyEvent.VK_HOME:
		horiz.setValue(horiz.getMinimum());
		break;
	    case KeyEvent.VK_END:
		horiz.setValue(horiz.getMaximum());
		break;
	    case KeyEvent.VK_ESCAPE:
		((XFrame) myFrame).close();
		break;
	    case KeyEvent.VK_TAB:
		current = (current + 1) % graphs.size();
		repaint = true;
		break;
	    default:
		unknown = true;
	    }
	}

	// repaint, if necessary
	if (repaint) {
	    // computeRange(); -- this introduces lots of bugs.  but needs to be done, eventually.
	    repaint();
	}

	// yummy
	if (!unknown)
	    e.consume();

	// updating the title is quick, so don't worry about doing it
	// a lot (oh wait, on windows2000 it isn't.  ouch.)
	updateTitle();
    }
    public void keyReleased(KeyEvent e) { }
    public void keyTyped(KeyEvent e) { }
    // ----------------------------------------------------------------------

    // MouseListener, for click-to-select --------------------
    private int clicked=-1;
    public void mouseClicked(MouseEvent e) {
	clicked = getGraphAt(e.getPoint());
	if (clicked != -1) {
	    current = clicked;
	    repaint();
	    /* if (n != -1) */ updateTitle();

	    // this should be: current = n; repaint(); if (n != -1) updateTitle();
	    // but that has problems with keyboard accels on current==-1
	    // actually the title should be "Plot: xyz", or "Plot", so if-stmt isn't even needed

	    // double-click?  edit it!
	    if (e.getClickCount() == 2) {
		Graph g = (Graph) graphs.get(clicked);
		if (g.graph instanceof Sample)
		    new Editor((Sample) g.graph);
	    }
	}
    }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mousePressed(MouseEvent e) {
	mouseClicked(e); // select it right away
    }
    public void mouseReleased(MouseEvent e) {
	// reset drag?  that seems awkward
	dragStart = null;
	clicked = -1;
    }
    // ------------------------------------------------------------

    public void queryScale() {
	// pixels / year -- weird stuff here because yearSize is final
	int tmp = 10;
	try {
	    tmp = Integer.parseInt(System.getProperty("corina.graph.pixelsperyear", "10"));
	} catch (NumberFormatException nfe) {
	    // show warning dialog?
	}
	yearSize = tmp;
    }

    // graphs = List of Graph
    public GrapherPanel(List graphs, JFrame myFrame) {
        // yearSize
        queryScale();

        // my frame
        this.myFrame = myFrame;

        // cursor -- (mac crosshair doesn't invert, so it's invisible on black)
        if (!Platform.isMac)
            setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR)); // use a crosshair

	// baselines?
	baselines = Boolean.getBoolean("corina.graph.baselines");

	// key listener -- apparently the focus gets screwed up and
	// keys stop responding if I don't add a key listener to both
	// the JFrame and JPanel, I don't know why.
	addKeyListener(this);

	// motion listener
	addMouseMotionListener(this);

	// click/drag listener
	addMouseListener(this);

	// copy data ref
	this.graphs = graphs;
	    
	// update bounds
	computeRange();

	// make sure sapwood and unmeas_pre are integers
	for (int i=0; i<graphs.size(); i++) {
	    if (((Graph) graphs.get(i)).graph instanceof Sample) {
		Sample s = (Sample) ((Graph) graphs.get(i)).graph;
		Object sap = s.meta.get("sapwood");
		Object pre = s.meta.get("unmeas_pre");

		if ((sap != null && !(sap instanceof Integer)) ||
		    (pre != null && !(pre instanceof Integer))) {
		    JOptionPane.showMessageDialog(null,
      // problem: be more specific -- *which* sample, and *what* value?
      // plus, let me edit it here (button: "edit sample now", opens metadata view)
						  "One or more metadata fields contained text where a number\n" +
						  "was expected.  The graph might not display all information\n" +
						  "(like sapwood count).  Double-check the sample's metadata fields.",
						  "Text found instead of numbers",
						  JOptionPane.WARNING_MESSAGE);
		    break;
		}
	    }
	}

	// background -- default is black
	setBackground(Color.getColor("corina.graph.background", Color.black));

	// create drawing agent
	myAgent = new StandardPlot(bounds, this);
    }
	
    public void recreateAgent() {
	myAgent = new StandardPlot(bounds, this);
    }

    /** Colors to use for graphs. */
    public final /* static */ Color COLORS[] = {
	new Color(0.00f, 0.53f, 1.00f), // blue
	new Color(0.27f, 1.00f, 0.49f), // green
	new Color(1.00f, 0.28f, 0.27f), // red
	new Color(0.22f, 0.80f, 0.82f), // cyan
	new Color(0.82f, 0.81f, 0.23f), // yellow
	new Color(0.85f, 0.26f, 0.81f), // magenta
    };

    static int getBottom(JPanel panel) {
	return panel.getHeight() - 30;
    }

    private void paintGraphPaper(Graphics2D g2) {
	// setup
	Color major = Color.getColor("corina.graph.graphpaper.color", Color.green);

        // real 50% alpha is slow on my
        // athlon-900.  ouch.  (enable this in about 5 years.)
        // so fake it.  (for thin lines.)
        g2.setColor(ColorUtils.blend(major, Color.getColor("corina.graph.background", Color.black)));

	// bottom, right
	int bottom = GrapherPanel.getBottom(this);
	int right = yearSize * bounds.span();

	// draw thin lines.
	for (int i=bottom; i>0; i-=10) // horiz
	    g2.drawLine(0, i, right, i);
	for (int i=0; i<right; i+=yearSize) // vert
	    g2.drawLine(i, 0, i, bottom);

	// (for thick lines.)
	g2.setColor(major);

	for (int i=bottom; i>0; i-=10*5) // horiz
	    g2.drawLine(0, i, right, i);

	// thick vertical decade lines: can't just go every
	// 5*yearSize, because that would not take the zero-gap into
	// account.  so start at -5 and go backward, and also start at
	// +5 and go forward.
	for (Year y=new Year(-5); y.compareTo(bounds.getStart())>0; y=y.add(-5)) {
	    int x = y.diff(bounds.getStart()) * yearSize;
	    g2.drawLine(x, 0, x, bottom);
	}
	for (Year y=new Year(5); y.compareTo(bounds.getEnd())<0; y=y.add(5)) {
	    int x = y.diff(bounds.getStart()) * yearSize;
	    g2.drawLine(x, 0, x, bottom);
	}
    }

    /** Paint this panel.  Draws a horizontal axis in white (on a
	black background), then draws each graph in a different color.
	@param g the Graphics to draw this panel onto */
    public void paintComponent(Graphics g) {
	// graphics setup
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;

	// from here down, everything is drawn in order.  this
	// means that the first thing drawn (the graphpaper) is
	// the bottommost layer, on up to the vertical-bar on top.

	// draw graphpaper
	if (Boolean.getBoolean("corina.graph.graphpaper"))
	    paintGraphPaper(g2);

	// figure out which years to draw the scale -- 1, 5, 10, 50, 100, 500, ...
	// WRITE ME

	// draw scale
	int x=0; // x-position of tick
	int bottom = GrapherPanel.getBottom(this); // baseline
	g2.setColor(Color.getColor("corina.graph.foreground", Color.white));
	for (Year y=bounds.getStart(); y.compareTo(bounds.getEnd())<=0; y=y.add(1)) {
	    // draw a label for the decade
	    if (y.column()==0 || y.isYearOne())
		g2.drawString(y.toString(), x, bottom+25);

	    // draw a tick mark for the year
	    int drop = bottom+5;
	    if (y.mod(10) == 0)
		drop += 10;
	    else if (y.mod(5) == 0)
		drop += 5;
	    g2.drawLine(x, bottom, x, drop);

	    // next tick
	    x += yearSize;
	}
	    
	// draw a horizontal bar
	g2.drawLine(0, bottom, yearSize*bounds.span(), bottom);

	// antialias, if requested -- only for graphs
	if (Boolean.getBoolean("corina.graph.antialias"))
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

	// draw graphs
	for (int j=0; j<graphs.size(); j++) {
	    // get graphable
	    Graphable s = (Graphable) ((Graph) graphs.get(j)).graph;

	    // set color
	    g2.setColor(COLORS[j % COLORS.length]);

	    // draw it
	    myAgent.draw(g2, (Graph) graphs.get(j), current==j, horiz.getValue());
	}

	// paint a vertical line at the cursor; and the year
	{
	    g2.setStroke(new BasicStroke(1.0f));
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
	    g2.setColor(Color.getColor("corina.graph.foreground", Color.white));
	    g2.drawLine(cursorX, 0, cursorX, bottom);
	    g2.drawString(bounds.getStart().add(cursorX / yearSize).toString(), cursorX+5, 15);
	}
    }

    // location-dependent tooltip
//    public String getToolTipText(MouseEvent event) {
// -- for sample S, year Y, and S[Y] = V, this should be: "S\nY: V"
//       return "event=" + event;
//    }

    public void update() {
	myAgent.update();
    }
}
