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
import java.awt.Stroke;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.font.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ProgressMonitor;
import javax.swing.RepaintManager;
import javax.swing.Scrollable;
import javax.swing.filechooser.FileFilter;

import corina.Build;
import corina.Range;
import corina.Sample;
import corina.Year;
import corina.core.App;
import corina.gui.Bug;
import corina.gui.XFrame;
import corina.ui.Alert;
import corina.util.ColorUtils;

public class GrapherPanel extends JPanel implements KeyListener, MouseListener,
		MouseMotionListener, AdjustmentListener, Scrollable {

	/** Width in pixels of one year on the x-axis. */
	//public int yearSize;
	
	// data
	/* private */public List graphs; // of Graph

	/* private */public int current = 0; // currenly selected sample

	///* private */private Range bounds; // bounds for entire graph
	//private Range emptyRange = null;

	// gui
	private JScrollPane scroller = null;
	private JFrame myFrame; // for setTitle(), dispose()

	private GraphInfo gInfo;
	
	private JPopupMenu popup = new JPopupMenu("Save");
	
	private Font graphNamesFont = new Font("Dialog", Font.PLAIN, 15);
	private Font tickFont = new Font("Dialog", Font.PLAIN, 11);	
	
	private String[] plotAgentName =   {
			"Standard Plot", 
			"Semi-Log Plot",
			"Toothed Plot"
			};
	
	private CorinaGraphPlotter[] plotAgentInstance = {
			new StandardPlot(),
			new SemilogPlot(),
			new DensityPlot()
	};
	
	// if we don't have a match (prefs are munged?), defeault to standard plot.
	private int defPlotAgent = 0;
	// this is the index in to the array above of plot agents for density plot.
	// perhaps we could 'auto-find' this, but it makes more sense at this point
	// to kludge it in.
	private final int densityPlotAgent = 2;
	
	// compute the initial range of the year-axis
	// (union of all graph ranges)
	// fixme: put |bounds| dfn here, change method name to be similar (computeBounds()?)
	// setPreferredSize() doesn't belong here, either.
	// -- not threadsafe -- bounds changes
	
	private void computeRange() {
		// convenience method for local JPanel...
		computeRange(gInfo, null);
	}
	
	private final int yearsBeforeLabel = 0;
	private final int yearsAfterLabel = 5;
	public void computeRange(GraphInfo info, Graphics graphics) {
		Range boundsr = null, emptyr = null;
		if (graphs.isEmpty()) {
			// can't happen
			Bug.bug(new IllegalArgumentException("no graphs!"));
			// bounds = new Range();
		} else {
			Graph g0 = (Graph) graphs.get(0);
			boundsr = g0.getRange();
			for (int i = 1; i < graphs.size(); i++) { // this looks familiar ... yes, i do believe it's (reduce), again, just like in sum.  ick.
				Graph g = (Graph) graphs.get(i);
				boundsr = boundsr.union(g.getRange());
			}
		}
		
		if(info.drawGraphNames()) {
			int mgw = calculateMaxGraphNameWidth(info, graphics) + 
						yearsBeforeLabel + yearsAfterLabel;
			Range strange = new Range(boundsr.getStart().add(-mgw), 1);
		
			emptyr = new Range(strange.getStart(), boundsr.getStart());
			boundsr = boundsr.union(strange);		
		}
	
		info.setDrawRange(boundsr);
		info.setEmptyRange(emptyr);
	}
	
	// somewhat obvious, calculates the maximum graph name width.
	private int calculateMaxGraphNameWidth(GraphInfo info, Graphics g) {
		int mw = 0;
		Font font; 
		FontMetrics fontmetrics;

		// if a graphics context is passed, use that... otherwise, use my panel
		if(g != null) {
			font = graphNamesFont;
			fontmetrics = g.getFontMetrics(font);
		}
		else {
			font = graphNamesFont;
			fontmetrics = getFontMetrics(font);
		}

		for(int i = 0; i < graphs.size(); i++) {
			Graph gr = (Graph) graphs.get(i);
			int w = fontmetrics.stringWidth(gr.getGraphName());
			if(w > mw)
				mw = w;
		}
		
		return 1 + (int) ((float) mw / (float) info.getYearWidth());		
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
	public void setScroller(JScrollPane h) {
		scroller = h;

		// add update listener, required for keeping baselines drawn
		if (gInfo.drawBaselines())
			scroller.getHorizontalScrollBar().addAdjustmentListener(this);
	}

	private void ensureScrollerExists() {
		if (scroller != null)
			return;

		// look for jscrollpane
		Container pop = getParent().getParent();
		if (pop instanceof JScrollPane) {
			setScroller((JScrollPane) pop);
		}
	}
	
	public void setGraphPaperVisible(boolean visible) {
		// set the preference...
		App.prefs.setPref("corina.graph.graphpaper", Boolean.toString(visible));

		// reload the prefs into the graphInfo
		gInfo.reloadPrefs();
		
		// redraw
		repaint();		
	}

	public void setBaselinesVisible(boolean visible) {
		// set the preference...
		App.prefs.setPref("corina.graph.baselines", Boolean.toString(visible));

		// reload the prefs into the graphInfo
		gInfo.reloadPrefs();
		
		ensureScrollerExists();		
		
		// add/remove listener so they get updated properly
		if (!gInfo.drawBaselines())
			scroller.getHorizontalScrollBar().removeAdjustmentListener(this);
		else
			scroller.getHorizontalScrollBar().addAdjustmentListener(this);

		// redraw
		repaint();
	}

	public void setHundredpercentlinesVisible(boolean visible) {
		// set the preference...
		App.prefs.setPref("corina.graph.hundredpercentlines", Boolean.toString(visible));

		// reload the prefs into the graphInfo
		gInfo.reloadPrefs();
		
		// redraw
		repaint();
	}

	public void setComponentNamesVisible(boolean visible) {
		// set the preference...
		App.prefs.setPref("corina.graph.componentnames", Boolean.toString(visible));

		// reload the prefs into the graphInfo
		gInfo.reloadPrefs();

		// recompute the range...
		computeRange();

		// messy redrawing...
		setPreferredSize(new Dimension(gInfo.getDrawRange().span() * gInfo.getYearWidth(), 200));
		recreateAgent();
		revalidate();					
		repaint();
	}
	
	// stuff for dealing with the vertical axis
	private Axis vertaxis = null;
	
	public void setAxisVisible(boolean visible) {
		// set the preference...
		App.prefs.setPref("corina.graph.vertical-axis", Boolean.toString(visible));

		// reload the prefs into the graphInfo
		gInfo.reloadPrefs();
		
		ensureScrollerExists();

		if (gInfo.drawVertAxis()) {
			vertaxis = new Axis(gInfo);
			scroller.setRowHeaderView(vertaxis);
			repaint();
		} else {
			scroller.setRowHeaderView(null);
			repaint();
		}
	}
	
	

	// used for clickers and draggers: get graph nr at point
	public int getGraphAt(Point p) {
		// try each sample...
		int bottom = gInfo.getHeight(this) - GrapherPanel.AXIS_HEIGHT;
		for (int i = 0; i < graphs.size(); i++) {
			// get graph
			Graph gg = (Graph) graphs.get(i);

			// hit?
			if (gg.getAgent().contact(gInfo, gg, p, bottom))
				return i;
		}

		// fail: -1
		return -1;
	}

	// MouseMotionListener, for vertical line under cursor ----------
	private Point dragStart = null;

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
		((Graph) graphs.get(current)).yoffset = (int) dragStart.getY()
				- e.getY();

		// change xoffset[n], but only if no shift
		int dx = 0;
		if (!e.isShiftDown()) {
			dx = (int) (e.getX() - dragStart.getX());
			dx -= dx % gInfo.getYearWidth();
		}
		((Graph) graphs.get(current)).xoffset = startX + (int) dx / gInfo.getYearWidth();
		//        recomputeDrops(); -- writeme?

		// repaint
		updateTitle();
		repaint();
	}

	private int cursorX = 0;

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
		int yearWidth = gInfo.getYearWidth();

		// update cursorX
		cursorX = e.getX();

		// put it on the nearest gridline
		int distanceLeftToGridline = cursorX % yearWidth;
		boolean roundRight = (distanceLeftToGridline >= yearWidth / 2);
		cursorX -= distanceLeftToGridline;
		if (roundRight)
			cursorX += yearWidth;

		// refresh, but only if necessary
		if (cursorX != old) {
			// OLD: repaint();

			// only update part of display that's needed!

			// vertical line
			repaint(old - 1, 0, 3, getHeight() - AXIS_HEIGHT);
			repaint(cursorX - 1, 0, 3, getHeight() - AXIS_HEIGHT);

			// text
			// repaint(old, 0, 50, 15); // HACK!
			// repaint(cursorX, 0, 50, 15); // HACK!
			// HACK: this assumes something about the text size.
			// also, FIXME: in the future i'll draw text on either side of the line.
			// almost-as-bad new version:
			repaint(old - 50, 0, 100, 15);
			repaint(cursorX - 50, 0, 100, 15);
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
		ensureScrollerExists();
		
		JScrollBar horiz = scroller.getHorizontalScrollBar();
		Range bounds = gInfo.getDrawRange();

		// extract some info once so i don't have to do it later
		int m = e.getModifiers();
		int k = e.getKeyCode();
		
		// cache yearsize, we use it a lot here
		int yearWidth = gInfo.getYearWidth();

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
			// change the graph scale!
			case KeyEvent.VK_W: {
				int curheight = gInfo.get10UnitHeight();
				if(--curheight < 2)
					curheight = 2;
				gInfo.set10UnitHeight(curheight);
				recreateAgent();
				revalidate();
				vertaxis.repaint();
				repaint = true;
				break;
			}
			case KeyEvent.VK_S: {
				int curheight = gInfo.get10UnitHeight();
				curheight++;
				gInfo.set10UnitHeight(curheight);
				recreateAgent();
				revalidate();					
				vertaxis.repaint();				
				repaint = true;
				break;
			}
			case KeyEvent.VK_A: {
				int curwidth = gInfo.getYearWidth();
				Year y = yearForPosition(gInfo, horiz.getValue());								
				if(--curwidth < 2)
					curwidth = 2;
				gInfo.setYearWidth(curwidth);
				
				computeRange();				
				setPreferredSize(new Dimension(bounds.span() * yearWidth, 200));
				horiz.setValue(Math.abs(y.diff(getRange().getStart())) * gInfo.getYearWidth());
				recreateAgent();
				revalidate();					
				repaint = true;
				break;
			}
			case KeyEvent.VK_D: {
				int curwidth = gInfo.getYearWidth();
				Year y = yearForPosition(gInfo, horiz.getValue());								
				curwidth++;
				gInfo.setYearWidth(curwidth);
				
				computeRange();
				setPreferredSize(new Dimension(bounds.span() * yearWidth, 200));
				horiz.setValue(Math.abs(y.diff(getRange().getStart())) * gInfo.getYearWidth());				
				recreateAgent();
				revalidate();					
				repaint = true;
				break;
			}
			case KeyEvent.VK_LEFT: {
				g.left();
				// see if our graph bounds changed at all. 
				Year y1 = bounds.getStart();
				Year y2 = bounds.getEnd();
				boolean endBoundChanged = false;
				
				computeRange();
				bounds = gInfo.getDrawRange();
				if(!bounds.getEnd().equals(y2))
					endBoundChanged = true;
				if(!bounds.getStart().equals(y1) || endBoundChanged)
				{
					setPreferredSize(new Dimension(bounds.span() * yearWidth, 200));
					recreateAgent();
					revalidate();					
				}
				
				// if we're changing the start boundary, AND we're already at the *end*, 
				// don't move the scroll bar, it'll move for us.
				if(!(endBoundChanged && horiz.getValue() == horiz.getMinimum()))
					horiz.setValue(horiz.getValue() - yearWidth);				
				repaint = true;								
				break;
			}
			case KeyEvent.VK_RIGHT: {
				g.right();

				// see if our graph bounds changed at all. 
				Year y1 = bounds.getStart();
				Year y2 = bounds.getEnd();
				boolean startBoundChanged = false;
				
				computeRange();
				bounds = gInfo.getDrawRange();
				if(!bounds.getStart().equals(y1))
					startBoundChanged = true;
				if(!bounds.getEnd().equals(y2) || startBoundChanged)
				{
					setPreferredSize(new Dimension(bounds.span() * yearWidth, 200));
					recreateAgent();
					revalidate();					
				}
				
				// if we're changing the start boundary, AND we're already at the beginning, 
				// don't move the scroll bar, it'll move for us.
				if(!(startBoundChanged && horiz.getValue() == horiz.getMinimum()))
					horiz.setValue(horiz.getValue() + yearWidth);				

				repaint = true;				
				break;
			}
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
				horiz.setValue(horiz.getValue() - yearWidth * 10);
				break;
			case KeyEvent.VK_RIGHT:
				// -- OLD: horiz.setValue(horiz.getValue() + horiz.getUnitIncrement());
				horiz.setValue(horiz.getValue() + yearWidth * 10);
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

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	// ----------------------------------------------------------------------

	// MouseListener, for click-to-select --------------------
	private int clicked = -1;

	public void mouseClicked(MouseEvent e) {
		clicked = getGraphAt(e.getPoint());
		if (clicked != -1) {
			current = clicked;
			repaint();
			/* if (n != -1) */updateTitle();

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
	
	private void setDefaultGraphColors() {
		int i;
		
		for(i = 0; i < graphs.size(); i++) {
			Graph g = (Graph) graphs.get(i);
			
			g.setColor(gInfo.screenColors[i % gInfo.screenColors.length].getColor(),
					gInfo.printerColors[i % gInfo.printerColors.length].getColor());
		}
	}

	public void tryPrint(int printStyle) {
		new GraphPrintDialog(myFrame, graphs, this, printStyle);
	}
	
	// graphs = List of Graph.
	// frame = window; (used for: title set to current graph, closed when ESC pressed.)
	public GrapherPanel(List graphs, final JFrame myFrame) {		
		// my frame
		this.myFrame = myFrame;

		// cursor: a crosshair.
		// note: (mac crosshair doesn't invert on 10.[01], so it's invisible on black)
		setCursor(crosshair);

		// set up the graph info, which loads a lot of default preferences.
		gInfo = new GraphInfo();
		// set default agent number
		String defAgentName = App.prefs.getPref("corina.graph.defaultagent", "corina.graph.StandardPlot"); 
		for(int i = 0; i < plotAgentInstance.length; i++) {
			if(plotAgentInstance[i].getClass().getName().equals(defAgentName)) {
				defPlotAgent = i;
				break;
			}
		}

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
		
		// set default colors
		setDefaultGraphColors();

		// update bounds
		computeRange();
		
		// set default scrolly window size
		setPreferredSize(new Dimension(gInfo.getDrawRange().span() * gInfo.getYearWidth(), 200));

		for (int i = 0; i < graphs.size(); i++) {
			Graph cg = (Graph) graphs.get(i);
			// make sure sapwood and unmeas_pre are integers			
			if (cg.graph instanceof Sample) {
				Sample s = (Sample) ((Graph) graphs.get(i)).graph;
				Object sap = s.meta.get("sapwood");
				Object pre = s.meta.get("unmeas_pre");

				boolean sapBad = (sap != null && !(sap instanceof Integer));
				boolean preBad = (pre != null && !(pre instanceof Integer));

				if (sapBad || preBad) {
					Alert
							.error(
									"Text found instead of numbers",
									"One or more metadata fields contained text where a number\n"
											+ "was expected.  The graph might not display all information\n"
											+ "(like sapwood count).  Double-check the sample's metadata fields.");
					// PROBLEM: be more specific -- *which* sample, and *what* value?
					// plus, let me edit it here (button: "edit sample now", opens metadata view)
					// better: just don't display it, or ... (?)
					return;
				}
			}
			
			// set each graph to have a the default agent; or the density agent.
			if(cg.graph instanceof DensityGraph)
				cg.setAgent(plotAgentInstance[densityPlotAgent]);
			else
				cg.setAgent(plotAgentInstance[defPlotAgent]);
		}

		// background -- default is black
		setBackground(gInfo.getBackgroundColor());

		// create drawing agent
		recreateAgent();
		
		// ensure that we're double buffered
		setDoubleBuffered(true);

		/*addMouseListener(new MouseAdapter() {
		 public void mouseClicked(MouseEvent e) {
		 System.out.println(e);
		 if (!e.isPopupTrigger()) return;
		 
		 System.out.println("Popup triggered!");
		 
		 popup.show(GrapherPanel.this, e.getX(), e.getY());
		 }  
		 });*/
	}
	
	public void postScrollpanedInit() {
		setBaselinesVisible(Boolean.valueOf(App.prefs.getPref("corina.graph.baselines")).booleanValue());
		setHundredpercentlinesVisible(Boolean.valueOf(App.prefs.getPref("corina.graph.hundredpercentlines")).booleanValue());
		setAxisVisible(Boolean.valueOf(App.prefs.getPref("corina.graph.vertical-axis")).booleanValue());
	}

	public void recreateAgent() {
		// did we deprecate this horrible beast?
		// myAgent = new StandardPlot(gInfo.getDrawRange(), gInfo);
	}

	/** Colors to use for graphs: blue, green, red, cyan, yellow, magenta. */
	/*
	public final Color COLORS[] = { new Color(0.00f, 0.53f, 1.00f), // blue
			new Color(0.27f, 1.00f, 0.49f), // green
			new Color(1.00f, 0.28f, 0.27f), // red
			new Color(0.22f, 0.80f, 0.82f), // cyan
			new Color(0.82f, 0.81f, 0.23f), // yellow
			new Color(0.85f, 0.26f, 0.81f), // magenta
	};
	*/

	// number of pixels between the bottom of the panel and the baseline
	/*package?*/static final int AXIS_HEIGHT = 30;

	// timing: this seems to take a significant portion of the time used to
	// draw the graph; usually 20-30 ms, but often jumping to 80-90
	// ms.  still far too much garbage being created here.
	private void paintGraphPaper(Graphics2D g2, GraphInfo info) {
		// visible range: [l..r]
		int l = g2.getClipBounds().x;
		int r = l + g2.getClipBounds().width;
		int origl = l;
		Range bounds = info.getDrawRange();
		
		if(info.drawGraphNames()) {
			int yeardiff = yearForPosition(info, l).
							compareTo(info.getEmptyRange().getEnd());

			if(yeardiff < 0)
				l += -yeardiff * info.getYearWidth();
		}

		// bottom
		int bottom = info.getHeight(this) - AXIS_HEIGHT;

		// draw horizontal lines
		// (would it help if everything was a big generalpath?  it appears not.)
		Color major = info.getMajorLineColor();
		Color mid = info.getMidLineColor();
		Color minor = info.getMinorLineColor();
		int yearWidth = info.getYearWidth();
		int unitHeight = info.get10UnitHeight();
		
		// be sure to draw all the way to our first vert. line....
		Year leftYear = yearForPosition(info, l);
		int x0 = leftYear.diff(bounds.getStart()) * yearWidth;
		
		g2.setColor(minor);
		int i = 1;
		for (int y = bottom - unitHeight; y > 0; y -= unitHeight) {
			// BUG: 10?  is that right?  EXTRACT CONST, at least
			if (i % 5 == 0) {
				if(x0 != origl)
					g2.drawLine(origl, y, x0, y);
				g2.setColor((i % 10 == 0) ? major : mid);
				g2.drawLine(x0, y, r, y);
				g2.setColor(minor);				
			} 
			else
				g2.drawLine(x0, y, r, y);
			i++;
		}

		// -----

		// draw vertical lines.
		// PERF: isn't every 5th line here just going to get overwritten?
		// -- for vert lines, it's a bit harder (right now, anyway)
		for (int x = x0; x < r; x += yearWidth) { // thin lines
			g2.drawLine(x, 0, x, bottom);
		}

		// crosses AD/BC boundary?
		// (LOD: EXTRACT "crosses-boundary"?  well, it's pretty trivial now)
		if (bounds.intersection(AD_BC).span() == 2) {
			// thick vertical decade lines: can't just go every
			// 5*yearSize, because that would not take the zero-gap into
			// account.  so start at -5 and go backward, and also start at
			// +5 and go forward.
			for (Year y = new Year(-5); y.compareTo(bounds.getStart()) > 0; y = y
					.add(-5)) {
				int x = y.diff(bounds.getStart()) * yearWidth;
				if (x > r) // (note: this test is backwards from elsewhere; we're going right-to-left)
					continue;
				if (x < l)
					break;
				g2.setColor((y.mod(10) == 0) ? major : mid);
				g2.drawLine(x, 0, x, bottom);
			}
			for (Year y = new Year(5); y.compareTo(bounds.getEnd()) < 0; y = y
					.add(5)) {
				int x = y.diff(bounds.getStart()) * yearWidth;
				if (x < l)
					continue;
				if (x > r)
					break;
				g2.setColor((y.mod(10) == 0) ? major : mid);
				g2.drawLine(x, 0, x, bottom);
			}
		} else {
			// doesn't cross AD/BC boundary; just draw lines.
			Year y1 = yearForPosition(info, l);
			y1 = y1.add(-(y1.mod(5) + 5)); // y -= (y%5) + 5; // EXTRACT: Year.sub()?
			for (Year y = y1; y.compareTo(bounds.getEnd()) < 0; y = y.add(5)) {
				int x = y.diff(bounds.getStart()) * yearWidth; // EXTRACT: yearToPosition(y)
				if (x < l)
					continue;
				if (x > r)
					break;
				g2.setColor((y.mod(10) == 0) ? major : mid);
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
	private Year yearForPosition(GraphInfo info, int x) {
		return info.getDrawRange().getStart().add(x / info.getYearWidth());
	}
	
	public int getYearWidth() {
		return gInfo.getYearWidth();
	}

	// timing: down to around 10 ms
	private void paintHorizAxis(Graphics g, GraphInfo info) {
		Graphics2D g2 = (Graphics2D) g;
		Font oldfont = g2.getFont();
		
		g2.setFont(tickFont);
		g2.setColor(info.getForeColor());

		int l = g2.getClipBounds().x;
		int r = l + g2.getClipBounds().width;
		int bottom = info.getHeight(this) - AXIS_HEIGHT;
		int yearWidth = info.getYearWidth();
		Range bounds = info.getDrawRange();

		Year startYear = yearForPosition(info, l).add(-5); // go one further, just to be sure
		// actually, go 5 further; i need to make sure to draw the text, even if it's
		// not completely on the screen, and i'm ASSUMING the text isn't wider than 5
		// years' worth -- if it is, it's probably going to start getting hard to read.
		int x = startYear.diff(bounds.getStart()) * yearWidth; // x-position of tick
		for (Year y = startYear; y.compareTo(bounds.getEnd()) <= 0; y = y
				.add(1)) {
			// out of visible viewport?
			if (x > r)
				break;

			// don't draw years or ticks in the empty part of the graph
			if(info.drawGraphNames() && 
					y.compareTo(info.getEmptyRange().getEnd()) < -5) {
				x += yearWidth;
				continue;
			}

			// draw a label for the decade
			if (y.column() == 0 || y.isYearOne())
				g2.drawString(y.toString(), x, bottom + 25);

			// draw a tick mark for the year
			int drop = bottom + 5;
			if (y.mod(10) == 0)
				drop += 10;
			else if (y.mod(5) == 0)
				drop += 5;
			g2.drawLine(x, bottom, x, drop);

			// next tick
			x += yearWidth;
		}

		// draw a horizontal bar
		g2.drawLine(l, bottom, r, bottom);
		g2.setFont(oldfont);
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

	public void drawGraphNames(Graphics g, GraphInfo info) {
		Graphics2D g2 = (Graphics2D) g;
		int bottom = info.getHeight(this) - GrapherPanel.AXIS_HEIGHT;
		int yearWidth = info.getYearWidth();
		Rectangle temprect = new Rectangle(0, 0, info.getEmptyRange().span() * yearWidth, bottom);
		
		// we're not on the screen, don't draw this...
		if(!temprect.intersects(g2.getClipBounds()))
			return;
		
		int[] overlaps = new int[graphs.size()];		
		float unitScale = (float) info.get10UnitHeight() / 10.0f;			
		Stroke oldstroke;
		Font oldfont;
		BasicStroke connectorLine = new BasicStroke(1, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 10f, new float[] { 8f }, 0f);			
		
		// bump up the text size...
		oldfont = g2.getFont();
		g2.setFont(graphNamesFont);

		oldstroke = g2.getStroke();
		g2.setStroke(connectorLine);

		int lineHeight = g2.getFontMetrics().getHeight();		
		int halflineHeight = lineHeight / 2;
		for(int i = 0; i < graphs.size(); i++) {
			Graph gr = (Graph) graphs.get(i);
			String gn = gr.getGraphName();
			int stringWidth = g2.getFontMetrics().stringWidth(gn);
			
			// gnw = x coordinate for start of string
			int gnw = ((info.getEmptyRange().span() * yearWidth) - stringWidth) - 
			          (yearWidth * yearsAfterLabel);
			
			// if this is an indexed sample, set this to be at the 100% line
			int grfirstvalue = 1000;			
			// otherwise, get the first point!
			if(!((gr.graph instanceof Sample) && ((Sample) gr.graph).isIndexed())) {
				try {
					grfirstvalue = ((Number) gr.graph.getData().get(0)).intValue();
				} catch (ClassCastException cce) {
					grfirstvalue = bottom - (int) (gr.yoffset * unitScale) - (lineHeight / 2);
				}
			}
			
			int y = bottom - (int) (grfirstvalue * gr.scale * unitScale) - (int) (gr.yoffset * unitScale);
			
			// at this point, we want to find something as close to the original 'y' as possible...
			
			for(int j = 0; j < i; j++) {
				int obottom = overlaps[j] + halflineHeight;
				int otop = overlaps[j] - (lineHeight + halflineHeight);

				// if we overlap, restart the loop...
				if(y <= obottom && y >= otop) {
					y = otop - 1;
					j = -1;
				}
			}
			
			overlaps[i] = y + halflineHeight;
			
			g2.setColor(gr.getColor(info.isPrinting()));
			
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF);			
			g2.drawString(gn, gnw, y + halflineHeight);
			
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);			
			g2.drawLine(gnw + stringWidth + 1, 
					y, 
					gnw + stringWidth + yearWidth * 4,
					bottom - (int) (grfirstvalue * gr.scale * unitScale) - (int) (gr.yoffset * unitScale));
					
		}
		
		g2.setStroke(oldstroke);
		g2.setFont(oldfont);
	}
	
	public void paintComponent(Graphics g) {
		ensureScrollerExists();

		// graphics setup
		super.paintComponent(g);

		paintGraph(g, gInfo);
		paintVertbar(g, gInfo);
	}

	/** Paint this panel.  Draws a horizontal axis in white (on a
	 black background), then draws each graph in a different color.
	 @param g the Graphics to draw this panel onto */
	private static final BasicStroke BLCENTER_STROKE = new BasicStroke(1);
	public void paintGraph(Graphics g, GraphInfo info) {
		Graphics2D g2 = (Graphics2D) g;
		int bottom = info.getHeight(this) - GrapherPanel.AXIS_HEIGHT;

		// from here down, everything is drawn in order.  this
		// means that the first thing drawn (the graphpaper) is
		// the bottommost layer, on up to the vertical-bar on top.
		
		// draw graphpaper
		if (info.drawGraphPaper())
			paintGraphPaper(g2, info);

		/* TODO: Draw a harsh line every 4??
		if (info.drawBaselines()) {
			int l = g2.getClipBounds().x;
			int r = l + g2.getClipBounds().width;
			float unitScale = (float) info.getYearSize() / 10.0f;			
			int yeardiff = yearForPosition(info, l).
							compareTo(info.getEmptyRange().getEnd());

			if(yeardiff < 0) {
				Year leftYear = yearForPosition(info, l);
				l = leftYear.diff(info.getDrawRange().getStart()) * 
								  info.getYearSize();				
			}
			
			g2.setColor(info.getBLCenterColor());
			g2.setStroke(BLCENTER_STROKE);			

			for (int i = 0; i < graphs.size(); i++) {
				// get graph
				Graph graph = (Graph) graphs.get(i);

				// draw a line at the center of the graph... (define center as
				// '1000')
				int y = bottom - (int) (1000 * graph.scale * unitScale)
						- (int) (graph.yoffset * unitScale);

				g2.drawLine(l, y, r, y);
			}
		}
		*/
		

		// ?? -- figure out which years to draw the scale
		// -- 1, 5, 10, 50, 100, 500, ...
		// WRITE ME

		// draw scale
		paintHorizAxis(g2, info);
				
		// force antialiasing for graphs -- it looks so much better,
		// and everybody's computer is fast enough for it these days
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
				
		// draw graphs
		for (int i = 0; i < graphs.size(); i++) {
			// get graph
			Graph graph = (Graph) graphs.get(i);
		
			// draw it
			if(info.isPrinting()) {
				// get printing color, printing thickness...
				g2.setColor(graph.getColor(true));
				graph.getAgent().draw(info, g2, bottom, graph, graph.getThickness(true), 0);
			} else {
				// use the thickness we have on our local graph...
				int thickness = graph.getThickness(false) * ((current == i) ? 2 : 1);
				g2.setColor(graph.getColor(false));				
				graph.getAgent().draw(info, g2, bottom, graph, thickness, scroller.getHorizontalScrollBar().getValue());						
			}			
		}
		
		// draw component names, if applicable...
		if(info.drawGraphNames()) {
			drawGraphNames(g2, info);						
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
	}
	
	private static final BasicStroke CURSOR_STROKE = new BasicStroke(1);
	private void paintVertbar(Graphics g, GraphInfo info) {
		Graphics2D g2 = (Graphics2D) g;

		if (inside) {
			// set color/stroke
			g2.setStroke(CURSOR_STROKE);
			g2.setColor(info.getForeColor());

			// draw the vertical bar
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF);
			g2.drawLine(cursorX, 0, cursorX, getHeight() - AXIS_HEIGHT);

			// if near the right side, it's invisible.
			// so: if on the right half of the (vis)screen, draw on the left side.

			// draw the label on the right side of the line?  else, left.
			int viewportX = cursorX - scroller.getHorizontalScrollBar().getValue(); // is this correct?
			int viewportWidth = getParent().getWidth();
			boolean right = (viewportX < viewportWidth / 2);

			final int eps = 5;
			final int ascent = g2.getFontMetrics().getAscent(); // PERF: memoize me!
			String str = yearForPosition(gInfo, cursorX).toString();

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

	// location-dependent tooltip
	//    public String getToolTipText(MouseEvent event) {
	// -- for sample S, year Y, and S[Y] = V, this should be: "S\nY: V"
	//       return "event=" + event;
	//    }

	public void update() {
		// notification that a preference or the sample list has changed.
		for (int i = 0; i < graphs.size(); i++) {
			Graph cg = (Graph) graphs.get(i);
			
			// we only want graphs that don't have an agent yet!
			if(cg.getAgent() != null)
				continue;
			
			// set each graph to have a the default agent; or the density agent.
			if(cg.graph instanceof DensityGraph)
				cg.setAgent(plotAgentInstance[densityPlotAgent]);
			else
				cg.setAgent(plotAgentInstance[defPlotAgent]);
			
		}
		
		revalidate();
		repaint();
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
		return gInfo.getYearWidth() * 10; // one decade (?)
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
	
	public GraphInfo getPrinterGraphInfo() {
		return gInfo.getPrinter();
	}
	
	public Range getRange() {
		return gInfo.getDrawRange();
	}
}
