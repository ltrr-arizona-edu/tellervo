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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.filechooser.FileFilter;

import com.keypoint.PngEncoder;

import corina.Build;
import corina.Range;
import corina.Sample;
import corina.Year;
import corina.core.App;
import corina.gui.Bug;
import corina.gui.XFrame;
import corina.ui.Alert;
import corina.util.ColorUtils;

public class GrapherPanel extends JPanel
                       implements KeyListener,
				  MouseListener, MouseMotionListener,
				  AdjustmentListener,
				  Scrollable
{
    /** Width in pixels of one year on the x-axis. */
    public /* final */ int yearSize;

    // data
    /* private */ public List graphs;		// of Graph
    /* private */ public int current = 0;	// currenly selected sample
    /* private */ public Range bounds;		// bounds for entire graph

    // gui
    private JScrollBar horiz=null;
    private JFrame myFrame;			// for setTitle(), dispose()

    // (precomputed)
    private boolean baselines;			// draw baselines below graphs?  (much slower (--only if it's done naively, fixme))

    // drawing agent!
    private StandardPlot myAgent;

    private JPopupMenu popup = new JPopupMenu("Save");
    
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
	myFrame.setTitle(title + " - " + Build.VERSION + " " + Build.TIMESTAMP);
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

    private void ensureHorizExists() {
	if (horiz != null)
	    return;

	// look for horizontal scrollbar
	Container pop = getParent().getParent();
	if (pop instanceof JScrollPane) {
	    setHoriz(((JScrollPane) pop).getHorizontalScrollBar());
	}
    }

    // toggle baselines
    public void setBaselinesVisible(boolean visible) {
	// toggle baselines
	baselines = !baselines;
  App.prefs.setPref("corina.graph.baselines", String.valueOf(baselines));

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
        // FIXME: move all the dragStart code into mousePressed

	// TODO: if user drags the axis, scroll?

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
//        recomputeDrops(); -- writeme?

	// repaint
	updateTitle();
	repaint();
    }
    private int cursorX=0;
    // this is BUG #199, because mouseMoved events stop being
    // generated as soon as a mouseExited event is fired.  idea:
    // compute dx, and on mouseExited set cursorX to (dx<0 ?
    // minCursorX : maxCursorX), but that doesn't take into account
    // moving off the bottom.  there's got to be a way to track
    // mouseMoved events for the focused window regardless of the
    // position of the mouse.
    public void mouseMoved(MouseEvent e) {
	// old cursorX
	int old = cursorX;

	// update cursorX
	cursorX = e.getX();

	// put it on the nearest gridline
	int distanceLeftToGridline = cursorX % yearSize;
	boolean roundRight = (distanceLeftToGridline >= yearSize/2);
	cursorX -= distanceLeftToGridline;
	if (roundRight)
	    cursorX += yearSize;

	// refresh, but only if necessary
	if (cursorX != old) {
	    // OLD: repaint();

	    // only update part of display that's needed!

	    // vertical line
	    repaint(old-1, 0, 3, getHeight() - AXIS_HEIGHT);
	    repaint(cursorX-1, 0, 3, getHeight() - AXIS_HEIGHT);

	    // text
	    // repaint(old, 0, 50, 15); // HACK!
	    // repaint(cursorX, 0, 50, 15); // HACK!
	    // HACK: this assumes something about the text size.
	    // also, FIXME: in the future i'll draw text on either side of the line.
	    // almost-as-bad new version:
	    repaint(old-50, 0, 100, 15);
	    repaint(cursorX-50, 0, 100, 15);
	}

        // crosshair cursor
        setCursor(crosshair); // PERF: is setCursor() expensive?
    }
    private Cursor crosshair = new Cursor(Cursor.CROSSHAIR_CURSOR);
    // ------------------------------------------------------------

    // KeyListener ------------------------------------------------------------
    /** Deal with key-pressed events, as described above.
	@param e the event to process */
    public void keyPressed(KeyEvent e) {
	ensureHorizExists();

	// extract some info once so i don't have to do it later
	int m = e.getModifiers();
	int k = e.getKeyCode();

	// repaint graph?
	boolean repaint = false;

	// unknown key?
	boolean unknown = false;

	// graph
	Graph g = (Graph) graphs.get(current);

	// IDEA: if i had some way of saying "shift tab" => { block }
	// then i wouldn't need these nested if/case statements.

	// parse it...ugh
	if (m == KeyEvent.SHIFT_MASK) { // shift keys
	    switch (k) {
	    case KeyEvent.VK_TAB:
		current = (current == 0 ? graphs.size() - 1 : current - 1);
		repaint = true;
		break;
	    case KeyEvent.VK_PERIOD:
		g.bigger();
		repaint = true;
		break;
	    case KeyEvent.VK_COMMA:
		g.smaller();
		repaint = true;
		break;
	    case KeyEvent.VK_EQUALS:
		g.slide(1);
		repaint = true;
		break;
	    default:
		unknown = true;
	    }
	} else if (m == KeyEvent.CTRL_MASK) { // control keys
	    switch (k) {
	    case KeyEvent.VK_LEFT:
		g.left();
		repaint = true;
		break;
	    case KeyEvent.VK_RIGHT:
		g.right();
		repaint = true;
		break;
	    default:
		unknown = true;
	    }
	} else { // unmodified keys
	    switch (k) {
	    case KeyEvent.VK_UP:
		g.slide(10);
		repaint = true;
		break;
	    case KeyEvent.VK_DOWN:
		g.slide(-10);
		repaint = true;
		break;
	    case KeyEvent.VK_MINUS:
		g.slide(-1);
		repaint = true;
		break;
	    case KeyEvent.VK_EQUALS: // unshifted equals == plus
		g.slide(1);
		repaint = true;
		break;
	    case KeyEvent.VK_LEFT:
		// BUG: horiz.getUnitIncrement() returns 1 here.  why?  i have no idea.
		// i guess because i don't set it explicitly.  though i would expect
		// values returned by the Scrollable interface would work.  oh well,
		// i'll just work around it for now.
		// -- OLD: horiz.setValue(horiz.getValue() - horiz.getUnitIncrement());
		horiz.setValue(horiz.getValue() - yearSize*10);
		break;
	    case KeyEvent.VK_RIGHT:
		// -- OLD: horiz.setValue(horiz.getValue() + horiz.getUnitIncrement());
		horiz.setValue(horiz.getValue() + yearSize*10);
		break;
	    case KeyEvent.VK_PAGE_UP:
		// BUG: if parent isn't viewport, ignore?: horiz.setValue(horiz.getValue() - yearSize*100);
		horiz.setValue(horiz.getValue() - getParent().getWidth());
		// -- OLD: horiz.setValue(horiz.getValue() - horiz.getBlockIncrement());
		break;
	    case KeyEvent.VK_PAGE_DOWN:
		// BUG: if parent isn't viewport, ignore?: horiz.setValue(horiz.getValue() + yearSize*100);
		horiz.setValue(horiz.getValue() + getParent().getWidth());
		// -- OLD: horiz.setValue(horiz.getValue() + horiz.getBlockIncrement());
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
	    // computeRange(); -- this introduces lots of bugs,
	    // but probably needs to be done, eventually.
	    // also, see sun's jscrollpane tutorial: it has
	    // demos on how to resize the scrollable area.
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
	}
    }
    public void mouseEntered(MouseEvent e) {
	inside = true;
	mouseMoved(e); // sort of...
	repaint(); // blunt!
    }
    private boolean inside = true; // is the cursor in the area?
    public void mouseExited(MouseEvent e) {
	inside = false;
	repaint(); // another hack!
    }
    public void mousePressed(MouseEvent e) {
	mouseClicked(e); // select it right away
    }
    public void mouseReleased(MouseEvent e) {
	// reset drag?  that seems awkward
	dragStart = null;
	clicked = -1;
  System.out.println(e);
        if (e.isPopupTrigger()) {
          popup.show(GrapherPanel.this, e.getX(), e.getY());
          return;
        }
    }
    // TODO: put the mouse-listener and mouse-motion-listener
    // stuff together.
    // ------------------------------------------------------------

    public void queryScale() {
	// pixels / year -- weird stuff here because yearSize is final
	int tmp = 10;
	try {
	    tmp = Integer.parseInt(App.prefs.getPref("corina.graph.pixelsperyear", "10"));
	    // FIXME: why not use Integer.getInteger()?
	} catch (NumberFormatException nfe) {
	    // show warning dialog?
	}
	yearSize = tmp;
    }

    // graphs = List of Graph.
    // frame = window; (used for: title set to current graph, closed when ESC pressed.)
    public GrapherPanel(List graphs, final JFrame myFrame) {
        // yearSize
        queryScale();

        // my frame
        this.myFrame = myFrame;

        // cursor: a crosshair.
	// note: (mac crosshair doesn't invert on 10.[01], so it's invisible on black)
	setCursor(crosshair);

	// baselines?
	baselines = Boolean.valueOf(App.prefs.getPref("corina.graph.baselines")).booleanValue();

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

		boolean sapBad = (sap != null && !(sap instanceof Integer));
		boolean preBad = (pre != null && !(pre instanceof Integer));

		if (sapBad || preBad) {
		    Alert.error("Text found instead of numbers",
				"One or more metadata fields contained text where a number\n" +
				"was expected.  The graph might not display all information\n" +
				"(like sapwood count).  Double-check the sample's metadata fields.");
      // PROBLEM: be more specific -- *which* sample, and *what* value?
      // plus, let me edit it here (button: "edit sample now", opens metadata view)
                    // better: just don't display it, or ... (?)
		    break;
		}
	    }
	}

	// background -- default is black
	setBackground(Color.getColor("corina.graph.background", Color.black));

	// create drawing agent
	recreateAgent();
  
  JMenuItem save = new JMenuItem("Save...");
        save.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent ae) {
            final JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileFilter() {
              public boolean accept(File f) {
                return f.getName().endsWith(".png");
              }
              public String getDescription() {
                return "PNG image files";
              }
            });
            chooser.setMultiSelectionEnabled(false);
            chooser.setAcceptAllFileFilterUsed(true);
            //chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int returnVal = chooser.showSaveDialog(myFrame);
            if (returnVal != JFileChooser.APPROVE_OPTION) return;

            EventQueue.invokeLater(new Runnable() {
              public void run() {
                Rectangle rect = getBounds();
                final Image fileImage = 
                    createImage(rect.width,rect.height);
                final Graphics g = fileImage.getGraphics();
    
                //write to the image
                paint(g);
     
                // write it out in the format you want
    
                new Thread(new Runnable() {
                  public void run() {
                    try {
                      
                      PngEncoder encoder = new PngEncoder(fileImage);
                      byte[] bytes = encoder.pngEncode(false);
                      /* (bytes == null) {
                        PngEncoderB encoderb = new PngEncoderB((BufferedImage) fileImage);
                        bytes = encoderb.pngEncode(false);
                      }*/
                      if (bytes != null) {
                        FileOutputStream fos = new FileOutputStream(chooser.getSelectedFile());
                        try {
                          fos.write(bytes);
                          fos.flush();
                        } finally {
                          try {
                            fos.close();
                          } catch (IOException ioe) {
                            ioe.printStackTrace();
                          }
                        }
                      } else {
                        System.err.println("ERROR IN ENCODER");
                      }
                      
                    } catch (Exception e) {
                      System.err.println("ERROR SAVING GRAPH TO: " + chooser.getSelectedFile());
                      e.printStackTrace();
                    }
        
                    //dispose of the graphics content
                    g.dispose();
                  }
                }).start();
              }
            });
          }
        });      
        popup.add(save);
      
        /*addMouseListener(new MouseAdapter() {
          public void mouseClicked(MouseEvent e) {
            System.out.println(e);
            if (!e.isPopupTrigger()) return;
            
            System.out.println("Popup triggered!");
          
            popup.show(GrapherPanel.this, e.getX(), e.getY());
          }  
        });*/
    }
	
    public void recreateAgent() {
	myAgent = new StandardPlot(bounds, this);
    }

    /** Colors to use for graphs: blue, green, red, cyan, yellow, magenta. */
    public final /* static */ Color COLORS[] = {
        new Color(0.00f, 0.53f, 1.00f), // blue
        new Color(0.27f, 1.00f, 0.49f), // green
        new Color(1.00f, 0.28f, 0.27f), // red
        new Color(0.22f, 0.80f, 0.82f), // cyan
        new Color(0.82f, 0.81f, 0.23f), // yellow
        new Color(0.85f, 0.26f, 0.81f), // magenta
    };

    // number of pixels between the bottom of the panel and the baseline
    /*package?*/ static final int AXIS_HEIGHT = 30;

    // BUG: this won't update when you refreshFromPreferences(), as it is now
    // -- but my new prefs framework should take care of that.
    private Color FORE_COLOR = Color.getColor("corina.graph.foreground", Color.white);

    // BUG: this won't update when you refreshFromPreferences(), as it is now
    // -- but my new prefs framework should take care of that.
    private Color major = Color.getColor("corina.graph.graphpaper.color", new Color(0, 51, 51));

    // color for thin lines: halfway between |major| and |background|.
    // (real 50% alpha is far too slow on any computer i have access to today,
    // except of course macintoshes, which can make swing really scream.)
    private Color minor = ColorUtils.blend(major, Color.getColor("corina.graph.background", Color.black));
    // REFACTOR: use Prefs class methods, not Color class methods
    // EXTRACT: should have default value for pref in prefs, not here

    // timing: this seems to take a significant portion of the time used to
    // draw the graph; usually 20-30 ms, but often jumping to 80-90
    // ms.  still far too much garbage being created here.
    private void paintGraphPaper(Graphics2D g2) {
	// visible range: [l..r]
	int l = g2.getClipBounds().x;
	int r = l + g2.getClipBounds().width;

	// bottom
        int bottom = getHeight() - AXIS_HEIGHT;

	// draw horizontal lines
	// (would it help if everything was a big generalpath?  it appears not.)
	g2.setColor(minor);
	int i = 1;
	for (int y=bottom-10; y>0; y-=10) {
	    // BUG: 10?  is that right?  EXTRACT CONST, at least
	    if (i % 5 == 0)
		g2.setColor(major);
	    g2.drawLine(l, y, r, y);
	    if (i % 5 == 0)
		g2.setColor(minor);
	    i++;
	}

	// -----

	// draw vertical lines.
	// PERF: isn't every 5th line here just going to get overwritten?
	// -- for vert lines, it's a bit harder (right now, anyway)
	Year leftYear = yearForPosition(l);
	int x0 = leftYear.diff(bounds.getStart()) * yearSize;
	for (int x=x0; x<r; x+=yearSize) { // thin lines
	    g2.drawLine(x, 0, x, bottom);
	}

	// (thick lines)
	g2.setColor(major);

	// crosses AD/BC boundary?
	// (LOD: EXTRACT "crosses-boundary"?  well, it's pretty trivial now)
	if (bounds.intersection(AD_BC).span() == 2) {
	    // thick vertical decade lines: can't just go every
	    // 5*yearSize, because that would not take the zero-gap into
	    // account.  so start at -5 and go backward, and also start at
	    // +5 and go forward.
	    for (Year y=new Year(-5); y.compareTo(bounds.getStart())>0; y=y.add(-5)) {
		int x = y.diff(bounds.getStart()) * yearSize;
		if (x > r) // (note: this test is backwards from elsewhere; we're going right-to-left)
		    continue;
		if (x < l)
		    break;
		g2.drawLine(x, 0, x, bottom);
	    }
	    for (Year y=new Year(5); y.compareTo(bounds.getEnd())<0; y=y.add(5)) {
		int x = y.diff(bounds.getStart()) * yearSize;
		if (x < l)
		    continue;
		if (x > r)
		    break;
		g2.drawLine(x, 0, x, bottom);
	    }
	} else {
	    // doesn't cross AD/BC boundary; just draw lines.
	    Year y1 = yearForPosition(l);
	    y1 = y1.add(-(y1.mod(5) + 5)); // y -= (y%5) + 5; // EXTRACT: Year.sub()?
	    for (Year y=y1; y.compareTo(bounds.getEnd())<0; y=y.add(5)) {
		int x = y.diff(bounds.getStart()) * yearSize; // EXTRACT: yearToPosition(y)
		if (x < l)
		    continue;
		if (x > r)
		    break;
		g2.drawLine(x, 0, x, bottom);
	    }
	}
    }

    // if r.intersection(AD_BC)==2, then r crosses the ad/bc boundary
    private static final Range AD_BC = new Range(new Year(-1), new Year(1));

    /*
      to get year -> position, it's just position = yearSize * (year -
      bounds.getStart()) so to get position -> year, it's just year =
      bounds.getStart() + position / yearSize, right?
      -- it's +/-1, anyway, which is a heck of a lot better than
         drawing every x-position
	 -- well, is it correct, or off-by-one?  i think it's correct...
    */
    private Year yearForPosition(int x) {
	return bounds.getStart().add(x / yearSize);
    }

    // timing: down to around 10 ms
    private void paintHorizAxis(Graphics g) {
	Graphics2D g2 = (Graphics2D) g;
	g2.setColor(FORE_COLOR);

	int l = g2.getClipBounds().x;
	int r = l + g2.getClipBounds().width;
        int bottom = getHeight() - AXIS_HEIGHT;

	Year startYear = yearForPosition(l).add(-5); // go one further, just to be sure
	// actually, go 5 further; i need to make sure to draw the text, even if it's
	// not completely on the screen, and i'm ASSUMING the text isn't wider than 5
	// years' worth -- if it is, it's probably going to start getting hard to read.
	int x=startYear.diff(bounds.getStart())*yearSize; // x-position of tick
	for (Year y=startYear; y.compareTo(bounds.getEnd())<=0; y=y.add(1)) {
	    // out of visible viewport?
	    if (x > r)
		break;

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
	g2.drawLine(l, bottom, r, bottom);
    }

    /*
      PERFORMANCE:
      -- in http://www.asktog.com/basics/firstPrinciples.html, tog says
      "Acknowledge all button clicks by visual or aural feedback
      within 50 milliseconds."

      so i'll set it as a goal that paintComponent() should, for all
      normal uses, return within 50 ms.  i can't guess what systems
      every person running corina will have, but my reference platform
      is a 500 MHz PPC G4.  if it returns within 50 ms on a 500 MHz
      computer (rather slow by today's standards), i'll be satisfied.

      TIMING:
      -- empty screen: 30-50 ms [GOOD!]
      -- 1 sample visible: 40-50 ms [GOOD!]
      -- lots of samples visible: 200-400 ms
         [a bit sluggish -- but only needed for scrolling]
    */

    /** Paint this panel.  Draws a horizontal axis in white (on a
	black background), then draws each graph in a different color.
	@param g the Graphics to draw this panel onto */
    public void paintComponent(Graphics g) {
	ensureHorizExists();

	// graphics setup
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;

	// from here down, everything is drawn in order.  this
	// means that the first thing drawn (the graphpaper) is
	// the bottommost layer, on up to the vertical-bar on top.

	// draw graphpaper
	if (Boolean.valueOf(App.prefs.getPref("corina.graph.graphpaper")).booleanValue()) {
	    // PERF: is this expensive?  (it's not just a bool!)
	    paintGraphPaper(g2);
	}

	// ?? -- figure out which years to draw the scale
	// -- 1, 5, 10, 50, 100, 500, ...
	// WRITE ME

	// draw scale
	paintHorizAxis(g2);

	// force antialiasing for graphs -- it looks so much better,
	// and everybody's computer is fast enough for it these days
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);

	// draw graphs
	for (int i=0; i<graphs.size(); i++) {
	    // get graph
	    Graph graph = (Graph) graphs.get(i);

	    // set color
	    g2.setColor(COLORS[i % COLORS.length]);

	    // draw it
	    myAgent.draw(g2, graph, current==i, horiz.getValue());
	}

        // for each year that's all-down, draw a RED vertical line.
	// TODO: this should be enabled by a menuitem:
	// - View->Mark All-Drop Years (reword). [/ Unmark ...]
	// - but disabled by default for single-samples and indexes.
        // BUG: this is expensive, and should be done only:
	// (1) on startup, and
	// (2) whenever an .xoffset changes.
	// PERF: it shouldn't be expensive.  a better algorithm:
	// -- keep 2 arrays of bits: "present", and "down"
	// -- (the first bit in each array is the first year drawn, etc.)
	// -- before drawing, set "present" to false, and "down" to true
	// -- when drawing, for each year:
	// ---- set present[y] to true
	// ---- if it's not-down, set down[y] to false
	// -- then, draw red lines for each year where (present[y] and down[y])
/*
        {
            int n = bounds.span();
            boolean down[] = new boolean[n]; // ugh!
            for (int i=0; i<n; i++)
                down[i] = true;
            for (int j=0; j<graphs.size(); j++) {
                Graphable gr = (Graphable) ((Graph) graphs.get(j)).graph;
                int di = gr.getStart().diff(bounds.getStart()) + ((Graph) graphs.get(j)).xoffset; // already starting this many years in
                List d = gr.getData();
/* -- all downs only
                for (int i=1; i<d.size(); i++) {
                    double a = ((Number) d.get(i-1)).doubleValue();
                    double b = ((Number) d.get(i  )).doubleValue();
                    if (b >= a)
                        down[i+di] = false;
                }
                */
                /*
                // minima only -- is this better?
                for (int i=1; i<d.size()-1; i++) {
                    double a = ((Number) d.get(i-1)).doubleValue();
                    double b = ((Number) d.get(i  )).doubleValue();
                    double c = ((Number) d.get(i+1)).doubleValue();
                    if (a <= b || b >= c)
                        down[i+di] = false; // BUG: fails if i+di<0, etc.
                }
                down[n-1] = false;
            }
            down[0] = false;
            g2.setColor(Color.red);
            g2.setStroke(new BasicStroke(1));
            for (int i=0; i<n; i++) {
                if (down[i]) {
                    int xx = i * yearSize;
                    g2.drawLine(xx, 0, xx, bottom); // refactor me?
                }
            }
            // FIXME: only draw lines where there are at least (2, 3, ?) samples?
        }
*/
// FIXME: what they really want is general-purpose decorators: lines
// (possibly with arrowheads), boxes, text, etc.  "mark all decreasing
// years" should just add decorators, not be a special mode.

        // paint a vertical line at the cursor; and the year
        if (inside) {
	    // set color/stroke
            g2.setStroke(CURSOR_STROKE);
            g2.setColor(FORE_COLOR);

	    // draw the vertical bar
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
	    g2.drawLine(cursorX, 0, cursorX, getHeight() - AXIS_HEIGHT);

	    // if near the right side, it's invisible.
	    // so: if on the right half of the (vis)screen, draw on the left side.

	    // draw the label on the right side of the line?  else, left.
	    int viewportX = cursorX - horiz.getValue(); // is this correct?
	    int viewportWidth = getParent().getWidth();
	    boolean right = (viewportX < viewportWidth/2);

	    final int eps = 5;
	    final int ascent = g2.getFontMetrics().getAscent(); // PERF: memoize me!
	    String str = yearForPosition(cursorX).toString();

	    int x = cursorX;
	    int y = ascent + eps;

	    if (right) {
		x += eps;
	    } else {
		int width = g2.getFontMetrics().stringWidth(str);
		x -= (width + eps);
	    }

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.drawString(str, x, y);
        }
    }

    private static final BasicStroke CURSOR_STROKE = new BasicStroke(1);

    // location-dependent tooltip
//    public String getToolTipText(MouseEvent event) {
// -- for sample S, year Y, and S[Y] = V, this should be: "S\nY: V"
//       return "event=" + event;
//    }

    public void update() {
	myAgent.update();
    }

    //
    // Scrollable
    //
    public int getScrollableBlockIncrement(Rectangle visibleRect,
					   int orientation, int direction) {
	// orient=vert never happens
	return visibleRect.width;
    }
    public int getScrollableUnitIncrement(Rectangle visibleRect,
					  int orientation, int direction) {
	// orient=vert never happens
	return yearSize * 10; // one decade (?)
    }
    public Dimension getPreferredScrollableViewportSize() {
	int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;

	// actually, this should be the amount of border, but
	// i don't know to get that reliably
	int width = screenWidth - 10;
	return new Dimension(width, 480);
    }
    public boolean getScrollableTracksViewportHeight() {
	return true; // never scroll vertically
    }
    public boolean getScrollableTracksViewportWidth() {
	return false;
    }
}
