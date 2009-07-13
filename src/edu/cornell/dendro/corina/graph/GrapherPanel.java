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

package edu.cornell.dendro.corina.graph;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.event.EventListenerList;

import edu.cornell.dendro.corina.Build;
import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.gui.XFrame;
import edu.cornell.dendro.corina.sample.Sample;

@SuppressWarnings("serial")
public class GrapherPanel extends JPanel implements KeyListener, MouseListener,
		MouseMotionListener, AdjustmentListener, Scrollable {

	
	// public data
	public List<Graph> graphs; // of Graph
	public int current = 0; // currenly selected sample

	// gui
	private JScrollPane scroller = null;
	private JFrame myFrame; // for setTitle(), dispose()

	private final GraphInfo gInfo;
	private final GraphManager manager;
	
	private JPopupMenu popup = new JPopupMenu("Save");
	
	private Font graphNamesFont = new Font("Dialog", Font.PLAIN, 15);
	private Font tickFont = new Font("Dialog", Font.PLAIN, 11);	

	private String scorePostpend = "";
	
	/** The plot agent used to obtain the plotter to plot the graphs */
	private PlotAgent plotAgent;
	
	private String emptyGraphText = "Nothing to graph";
	
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
		// special case: empty graph
		if (graphs.isEmpty()) {
			info.setDrawBounds(new Range(new Year(0), 100));
			info.setEmptyBounds(new Range(new Year(0), 1));
			return;
		}
		
		Range boundsr = null, emptyr = null;
		Graph g0 = graphs.get(0);
		boundsr = g0.getRange();
		for (int i = 1; i < graphs.size(); i++) { // this looks familiar ... yes, i do believe it's (reduce), again, just like in sum.  ick.
			Graph g = graphs.get(i);
			boundsr = boundsr.union(g.getRange());
		}
		
		if(info.isShowGraphNames()) {
			int mgw = calculateMaxGraphNameWidth(info, graphics) + 
						yearsBeforeLabel + yearsAfterLabel;
			Range strange = new Range(boundsr.getStart().add(-mgw), 1);
		
			emptyr = new Range(strange.getStart(), boundsr.getStart());
			boundsr = boundsr.union(strange);		
		}
	
		info.setDrawBounds(boundsr);
		info.setEmptyBounds(emptyr);
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
			Graph gr = graphs.get(i);
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
	@Override
	public boolean isFocusTraversable() {
		return true;
	}

	// ok, this is the magic bullet i was looking for.
	@Override
	public boolean isManagingFocus() {
		return true;
	}

	// update the window title with the current graph's title
	public void updateTitle() {
		// perhaps it can be null?
		if(myFrame == null)
			return;
		
		// current graph, and its title
		String title;
		if(graphs.size() > 0) {
			Graph g = graphs.get(current);
			title = g.graph.toString();
			
			// if offset, use "title (at ...)"
			if (g.xoffset != 0)
				title += " (at " + g.getRange() + ")";
		}
		else
			title = "Empty graph";


		// set title
		myFrame.setTitle(title + scorePostpend + " - " + Build.VERSION + " " + Build.TIMESTAMP);
		
		if(myFrame instanceof GraphWindow) {
			((GraphWindow)myFrame).panelSelectionChanged();
		}
	}

	// for horiz scrollbar
	public void adjustmentValueChanged(AdjustmentEvent ae) {
		repaint();
	}

	// hack to get the horizontal scrollbar to this
	public void setScroller(JScrollPane h) {
		scroller = h;

		// add update listener, required for keeping baselines drawn
		if (gInfo.isShowBaselines())
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
	
	
	// stuff for dealing with the vertical axis
	private Axis vertaxis = null;
		

	// used for clickers and draggers: get graph nr at point
	public int getGraphAt(Point p) {
		// try each sample...
		int bottom = gInfo.getGraphHeight(this) - GrapherPanel.AXIS_HEIGHT;
		for (int i = 0; i < graphs.size(); i++) {
			// get graph
			Graph gg = graphs.get(i);

			// hit?
			if (gg.getPlotter().contact(gInfo, gg, p, bottom))
				return i;
		}

		// fail: -1
		return -1;
	}

	// MouseMotionListener, for vertical line under cursor ----------
	private Point dragStart = null;

	private int startX; // initial xoff
	private Integer lastX; // last xoffset

	public void mouseDragged(MouseEvent e) {
		// FIXME: move all the dragStart code into mousePressed

		// TODO: if user drags the axis, scroll?

		// didn't drag from a graph?  sorry.
		if (clicked == -1)
			return;

		Graph dragGraph;
		
		// just starting a drag?
		if (dragStart == null) {
			// dragging something?
			int n = getGraphAt(e.getPoint());

			// nope, ignore
			if (n == -1)
				return;

			// also ignore if it's not draggable
			// store graph as g
			if (!(dragGraph = graphs.get(n)).isDraggable()) {
				// complain!
				Toolkit.getDefaultToolkit().beep();
				return;
			}

			// yes, store
			dragStart = (Point) e.getPoint().clone();
			dragStart.y += dragGraph.yoffset;
			startX = dragGraph.xoffset;
			lastX = null;

			// select it, too, while we're at it
			current = n;
		}
		else
			// populate g with the current graph
			dragGraph = graphs.get(current);

		// change yoffset[n], but only if no ctrl
		if(!e.isControlDown())
			dragGraph.yoffset = (int) dragStart.getY() - e.getY();

		// change xoffset[n], but only if no shift
		int dx = 0;
		if (!e.isShiftDown()) {
			dx = (int) (e.getX() - dragStart.getX());
			dx -= dx % gInfo.getYearWidth();
		}
		
		dragGraph.xoffset = startX + dx / gInfo.getYearWidth();

		// data changed, so update
		if(lastX == null || dragGraph.xoffset != lastX) {
			lastX = dragGraph.xoffset;
			fireGrapherEvent(new GrapherEvent(this, dragGraph, GrapherEvent.Type.XOFFSET_CHANGED));
			calculateScores();
			updateTitle();
		}
		
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
		int oldX = cursorX;
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
		if (cursorX != oldX) {
			// OLD: repaint();

			// only update part of display that's needed!

			// vertical line
			repaint(oldX - 1, 0, 3, getHeight() - AXIS_HEIGHT);
			repaint(cursorX - 1, 0, 3, getHeight() - AXIS_HEIGHT);

			// text
			// repaint(old, 0, 50, 15); // HACK!
			// repaint(cursorX, 0, 50, 15); // HACK!
			// HACK: this assumes something about the text size.
			// also, FIXME: in the future i'll draw text on either side of the line.
			// almost-as-bad new version:
			repaint(oldX - 50, 0, 100, 15);
			repaint(cursorX - 50, 0, 100, 15);
		}

		// crosshair cursor
		setCursor(crosshair); // PERF: is setCursor() expensive?
	}

	private Cursor crosshair = new Cursor(Cursor.CROSSHAIR_CURSOR);

	// ------------------------------------------------------------

	private final static List<Integer> emptyIntegerList = Collections.emptyList();
	private final static Graph emptyGraph = new Graph(emptyIntegerList, new Year(1000), "empty");
	
	// KeyListener ------------------------------------------------------------
	/** Deal with key-pressed events, as described above.
	 @param e the event to process */
	public void keyPressed(KeyEvent e) {
		ensureScrollerExists();
		
		JScrollBar horiz = scroller.getHorizontalScrollBar();
		Range bounds = gInfo.getDrawBounds();

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
		Graph g = (graphs.size() > 0) ? graphs.get(current) : emptyGraph;
		
		// IDEA: if i had some way of saying "shift tab" => { block }
		// then i wouldn't need these nested if/case statements.

		// parse it...ugh
		if (m == InputEvent.SHIFT_MASK) { // shift keys
			switch (k) {
			case KeyEvent.VK_TAB:
				current = (current == 0 ? graphs.size() - 1 : current - 1);
				repaint = true;
				break;
			case KeyEvent.VK_PERIOD:
				g.bigger();
				fireGrapherEvent(new GrapherEvent(this, g, GrapherEvent.Type.SCALE_CHANGED));

				repaint = true;
				break;
			case KeyEvent.VK_COMMA:
				g.smaller();
				fireGrapherEvent(new GrapherEvent(this, g, GrapherEvent.Type.SCALE_CHANGED));
				
				repaint = true;
				break;
			case KeyEvent.VK_EQUALS:
				g.slide(1);
				fireGrapherEvent(new GrapherEvent(this, g, GrapherEvent.Type.YOFFSET_CHANGED));

				repaint = true;
				break;
			default:
				unknown = true;
			}
		} else if (m == InputEvent.CTRL_MASK) { // control keys
			switch (k) {
			// change the graph scale!
			case KeyEvent.VK_W: {
				int curheight = gInfo.getTenUnitHeight();
				if(--curheight < 2)
					curheight = 2;
				gInfo.setTenUnitHeight(curheight);
				repaint = false;
				break;
			}
			case KeyEvent.VK_S: {
				int curheight = gInfo.getTenUnitHeight();
				curheight++;
				gInfo.setTenUnitHeight(curheight);
				repaint = false;
				break;
			}
			case KeyEvent.VK_A: {
				int curwidth = gInfo.getYearWidth();
				Year y = yearForPosition(gInfo, horiz.getValue());								
				if(--curwidth < 2)
					curwidth = 2;
				gInfo.setYearWidth(curwidth);
				
				horiz.setValue(Math.abs(y.diff(getRange().getStart())) * gInfo.getYearWidth());				
				repaint = false;
				break;
			}
			case KeyEvent.VK_D: {
				int curwidth = gInfo.getYearWidth();
				Year y = yearForPosition(gInfo, horiz.getValue());								
				curwidth++;
				gInfo.setYearWidth(curwidth);
				
				horiz.setValue(Math.abs(y.diff(getRange().getStart())) * gInfo.getYearWidth());								
				repaint = false;
				break;
			}
			case KeyEvent.VK_LEFT: {
				g.left();
				fireGrapherEvent(new GrapherEvent(this, g, GrapherEvent.Type.XOFFSET_CHANGED));

				// see if our graph bounds changed at all. 
				Year y1 = bounds.getStart();
				Year y2 = bounds.getEnd();
				boolean endBoundChanged = false;
				
				computeRange();
				bounds = gInfo.getDrawBounds();
				if(!bounds.getEnd().equals(y2))
					endBoundChanged = true;
				if(!bounds.getStart().equals(y1) || endBoundChanged)
				{
					setPreferredSize(new Dimension(bounds.span() * yearWidth, getGraphHeight()));
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
				fireGrapherEvent(new GrapherEvent(this, g, GrapherEvent.Type.XOFFSET_CHANGED));

				// see if our graph bounds changed at all. 
				Year y1 = bounds.getStart();
				Year y2 = bounds.getEnd();
				boolean startBoundChanged = false;
				
				computeRange();
				bounds = gInfo.getDrawBounds();
				if(!bounds.getStart().equals(y1))
					startBoundChanged = true;
				if(!bounds.getEnd().equals(y2) || startBoundChanged)
				{
					setPreferredSize(new Dimension(bounds.span() * yearWidth, getGraphHeight()));
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
				fireGrapherEvent(new GrapherEvent(this, g, GrapherEvent.Type.YOFFSET_CHANGED));
				repaint = true;
				break;
			case KeyEvent.VK_DOWN:
				g.slide(-10);
				fireGrapherEvent(new GrapherEvent(this, g, GrapherEvent.Type.YOFFSET_CHANGED));
				repaint = true;
				break;
			case KeyEvent.VK_MINUS:
				g.slide(-1);
				fireGrapherEvent(new GrapherEvent(this, g, GrapherEvent.Type.YOFFSET_CHANGED));
				repaint = true;
				break;
			case KeyEvent.VK_EQUALS: // unshifted equals == plus
				g.slide(1);
				fireGrapherEvent(new GrapherEvent(this, g, GrapherEvent.Type.YOFFSET_CHANGED));
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
			calculateScores();
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
		this.requestFocus();
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
		if(dragStart != null) {
			dragStart = null;
			
			// see if any bounds changed and update accordingly
			Range bounds = gInfo.getDrawBounds();
			Year start = bounds.getStart();
			Year end = bounds.getEnd();
			boolean endBoundChanged = false;
			boolean startBoundChanged = false;
			
			computeRange();
			bounds = gInfo.getDrawBounds();
			if(!bounds.getEnd().equals(end))
				endBoundChanged = true;
			if(!bounds.getStart().equals(start))
				startBoundChanged = true;
			
			if(startBoundChanged || endBoundChanged){
				int yearWidth = gInfo.getYearWidth();
				setPreferredSize(new Dimension(bounds.span() * yearWidth, getGraphHeight()));
				revalidate();					
				
				JScrollBar horiz = scroller.getHorizontalScrollBar();
				
				if(!(endBoundChanged && horiz.getValue() == horiz.getMinimum()))
					horiz.setValue(horiz.getValue() - yearWidth);
				
				if(!(startBoundChanged && horiz.getValue() == horiz.getMinimum()))
					horiz.setValue(horiz.getValue() + yearWidth);				

			}
		}
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
			Graph g = graphs.get(i);
			
			g.setColor(GraphInfo.screenColors[i % GraphInfo.screenColors.length].getColor(),
					GraphInfo.printerColors[i % GraphInfo.printerColors.length].getColor());
		}
	}

	public void tryPrint(int printStyle) {
		new GraphPrintDialog(myFrame, graphs, this, printStyle);
	}

	public GrapherPanel(List<Graph> graphs, final JFrame myFrame) {
		// set up the graph info, which loads a lot of default preferences.	
		this(graphs, myFrame, new GraphInfo());
	}
	
	// graphs = List of Graph.
	// frame = window; (used for: title set to current graph, closed when ESC pressed.)
	public GrapherPanel(List<Graph> graphs, final JFrame myFrame, GraphInfo graphInfo) {		
		// my frame
		this.myFrame = myFrame;

		// cursor: a crosshair.
		// note: (mac crosshair doesn't invert on 10.[01], so it's invisible on black)
		setCursor(crosshair);

		// use the passed graphinfo
		gInfo = graphInfo;
		// create a new manager that listens to changes in the graphInfo
		
		manager = new GraphManager();
		
		// set up the default plotagent
		this.plotAgent = PlotAgent.getDefault();

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
		
		// assign plot agents to all the graphs
		for (int i = 0; i < graphs.size(); i++) {
			Graph cg = graphs.get(i);

			// assign each graph a plotting agent
			cg.setAgent(plotAgent);
		}

		// set default scrolly window size
		setPreferredSize(new Dimension(gInfo.getDrawBounds().span() * gInfo.getYearWidth(), getGraphHeight()));
		
		// background -- default is black
		setBackground(gInfo.getBackgroundColor());

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
		
		// when we're put in a scrollpane, update accordingly
		addHierarchyListener(new HierarchyListener() {
			public void hierarchyChanged(HierarchyEvent e) {		
				if(e.getChanged() instanceof JScrollPane) {
					postScrollpanedInit();
				}
			}			
		});
	}
	
	private void postScrollpanedInit() {
		// calculate our initial scores, and set the initial title...
		calculateScores();
		updateTitle();
		
		// add our axis / baselines, etc
		manager.horizontalScrollbarStatusChanged();
		manager.verticalAxisStatusChanged();		
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
		Range bounds = info.getDrawBounds();
		
		if(info.isShowGraphNames()) {
			int yeardiff = yearForPosition(info, l).
							compareTo(info.getEmptyBounds().getEnd());

			if(yeardiff < 0)
				l += -yeardiff * info.getYearWidth();
		}

		// bottom
		int bottom = info.getGraphHeight(this) - AXIS_HEIGHT;

		// draw horizontal lines
		// (would it help if everything was a big generalpath?  it appears not.)
		Color major = info.getMajorLineColor();
		Color mid = info.getMidLineColor();
		Color minor = info.getMinorLineColor();
		int yearWidth = info.getYearWidth();
		int unitHeight = info.getTenUnitHeight();
		
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
		return info.getDrawBounds().getStart().add(x / info.getYearWidth());
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
		int bottom = info.getGraphHeight(this) - AXIS_HEIGHT;
		int yearWidth = info.getYearWidth();
		Range bounds = info.getDrawBounds();

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
			if(info.isShowGraphNames() && 
					y.compareTo(info.getEmptyBounds().getEnd()) < -5) {
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
		int bottom = info.getGraphHeight(this) - GrapherPanel.AXIS_HEIGHT;
		int yearWidth = info.getYearWidth();
		Rectangle temprect = new Rectangle(0, 0, info.getEmptyBounds().span() * yearWidth, bottom);
		
		// we're not on the screen, don't draw this...
		if(!temprect.intersects(g2.getClipBounds()))
			return;
		
		int[] overlaps = new int[graphs.size()];		
		float unitScale = info.getTenUnitHeight() / 10.0f;			
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
			Graph gr = graphs.get(i);
			String gn = gr.getGraphName();
			int stringWidth = g2.getFontMetrics().stringWidth(gn);
			
			// gnw = x coordinate for start of string
			int gnw = ((info.getEmptyBounds().span() * yearWidth) - stringWidth) - 
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
	
	@Override
	public void paintComponent(Graphics g) {
		ensureScrollerExists();

		// graphics setup
		super.paintComponent(g);

		paintGraph(g, gInfo);
		paintVertbar(g, gInfo);
	}

	/**
	 * Change the message when the graph is empty
	 * @param emptyGraphText
	 */
	public void setEmptyGraphText(String emptyGraphText) {
		this.emptyGraphText = emptyGraphText;
	}
	
	/**
	 * Paint a "Nothing to graph" when there's nothing available
	 * @param g
	 */
	private void paintNoGraphs(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		Composite oldComposite = g2.getComposite();
		Font oldFont = g2.getFont();
		
		g2.setColor(Color.blue.darker());
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
		g2.setFont(g2.getFont().deriveFont(72.0f));
		
		int width = g2.getFontMetrics().stringWidth(emptyGraphText);
		
		float x = (float)((getWidth() / 2) - (width / 2)); 
		float y = (float)(getHeight() / 2);

		g2.drawString(emptyGraphText, x, y);
		
		g2.setComposite(oldComposite);
		g2.setFont(oldFont);
	}
	
	/** Paint this panel.  Draws a horizontal axis in white (on a
	 black background), then draws each graph in a different color.
	 @param g the Graphics to draw this panel onto */
	//private static final BasicStroke BLCENTER_STROKE = new BasicStroke(1);
	public void paintGraph(Graphics g, GraphInfo info) {
		Graphics2D g2 = (Graphics2D) g;
		int bottom = info.getGraphHeight(this) - GrapherPanel.AXIS_HEIGHT;

		// from here down, everything is drawn in order.  this
		// means that the first thing drawn (the graphpaper) is
		// the bottommost layer, on up to the vertical-bar on top.
		
		// draw graphpaper
		if (info.isShowGraphPaper())
			paintGraphPaper(g2, info);

		/* TODO: Draw a harsh line every 4??
		if (info.drawBaselines()) {
			int l = g2.getClipBounds().x;
			int r = l + g2.getClipBounds().width;
			float unitScale = (float) info.getYearSize() / 10.0f;			
			int yeardiff = yearForPosition(info, l).
							compareTo(info.getEmptyBounds().getEnd());

			if(yeardiff < 0) {
				Year leftYear = yearForPosition(info, l);
				l = leftYear.diff(info.getDrawBounds().getStart()) * 
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
		
		int nGraphs = graphs.size();
		
		// no graphs? mention it
		if(nGraphs < 1) {
			paintNoGraphs(g2);
			return;
		}
		
		// draw graphs
		for (int i = 0; i < nGraphs; i++) {
			// get graph
			Graph graph = graphs.get(i);
		
			// draw it
			if(info.isPrinting()) {
				// get printing color, printing thickness...
				g2.setColor(graph.getColor(true));
				graph.draw(info, g2, bottom, graph.getThickness(true), 0);
			} else {
				// use the thickness we have on our local graph...
				int thickness = graph.getThickness(false) * ((current == i) ? 2 : 1);
				g2.setColor(graph.getColor(false));				
				graph.draw(info, g2, bottom, thickness, scroller.getHorizontalScrollBar().getValue());
			}			
		}
		
		// draw component names, if applicable...
		if(info.isShowGraphNames()) {
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
		update(true);
	}
		
	/**
	 * Sets the graph plot agent for all non-density graphs
	 * Must call update(true) for graphs to be redrawn
	 * @param agent
	 */
	public void setPlotAgent(PlotAgent agent) {
		this.plotAgent = agent;
	}
	
	/**
	 * Update the graph
	 * 
	 * @param redraw should we redraw the graph, or just update internal structures?
	 */
	public void update(boolean redraw) {
		// notification that a preference or the sample list has changed.
		for (int i = 0; i < graphs.size(); i++) {
			Graph cg = graphs.get(i);
			
			// set each graph to have a the default agent
			cg.setAgent(plotAgent);
						
			// assign the new graph a color
			if(!cg.hasColor())
				cg.setColor(GraphInfo.screenColors[i % GraphInfo.screenColors.length].getColor(),
				 		GraphInfo.printerColors[i % GraphInfo.printerColors.length].getColor());	
		}
		
		// recompute range
		computeRange();

		if(vertaxis != null)
			vertaxis.setAxisType(PlotAgent.getDefault().getAxisType());
		
		if(!redraw)
			return;
		
		// change size
		setPreferredSize(new Dimension(gInfo.getDrawBounds().span() * gInfo.getYearWidth(), getGraphHeight()));
		// and redo ourselves!
		revalidate();
		// redraw
		repaint();
	}
	
	// scroll to the bottom of the graph
	public void scrollToBottomLeft() {
		scrollRectToVisible(new Rectangle(new Point(0, getPreferredSize().height)));
	}

	//
	// Scrollable
	//
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		if(orientation == SwingConstants.VERTICAL)
			return visibleRect.height;
		else
			return visibleRect.width;
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		if(orientation == SwingConstants.VERTICAL)
			return gInfo.getTenUnitHeight() * 10; // 100 (?)
		else
			return gInfo.getYearWidth() * 10; // one decade (?)
	}

	public Dimension getPreferredScrollableViewportSize() {
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width - 40; // Magic number!
		int frames = (myFrame != null) ? myFrame.getInsets().left + myFrame.getInsets().right : 0;

		// actually, this should be the amount of border, but
		// i don't know to get that reliably
		// we do now!
		int width = screenWidth - frames;
		if(vertaxis != null)
			width -= Axis.AXIS_WIDTH;
		return new Dimension(width, getGraphHeight());
	}

	/**
	 * Whether the graph should scroll vertically
	 * Defaults to false
	 * 
	 * @param useVerticalScrollbar
	 */
	public void setUseVerticalScrollbar(boolean useVerticalScrollbar) {
		this.useVerticalScrollbar = useVerticalScrollbar;
	}
	
	private boolean useVerticalScrollbar = false;
	
	public boolean getScrollableTracksViewportHeight() {
		return !useVerticalScrollbar; // don't scroll vertically by default
	}

	public boolean getScrollableTracksViewportWidth() {
		return false;
	}
	
	public GraphInfo getPrinterGraphInfo() {
		return gInfo.getPrinter();
	}
	
	public Range getRange() {
		return gInfo.getDrawBounds();
	}
	
	/**
	 * @return the width of this graph in its current state, in pixels
	 */
	public int getGraphPixelWidth() {
		return gInfo.getDrawBounds().span() * gInfo.getYearWidth();
	}
	
	/**
	 * Package scope only - get the graph's info!
	 * @return our graphInfo structure - use sparingly!
	 */
	GraphInfo getGraphInfo() {
		return gInfo;
	}
	
	public Range getGraphingRange() {
		if(!gInfo.isShowGraphNames())
			return gInfo.getDrawBounds();
		
		return new Range(gInfo.getEmptyBounds().getEnd(), gInfo.getDrawBounds().getEnd());
	}
		
	public int getGraphHeight() {
		return getMaxPixelHeight() + GrapherPanel.AXIS_HEIGHT + gInfo.getTenUnitHeight();
	}
	
	/**
	 * Override getPreferredSize(Dimension) instead
	 */
	@Override
	public Dimension getPreferredSize() {
		ensureScrollerExists();

		Dimension parentPreferredDimensions = super.getPreferredSize();
		return getPreferredSize(parentPreferredDimensions,
				(scroller != null) ? scroller.getViewport().getExtentSize()
						: parentPreferredDimensions);
	}
	
	/**
	 * Meant to be overridden:
	 * 
	 * @param parentPreferredDimensions JPanel's idea of what our size should be
	 * @param scrollExtentDimensions our parent JScrollPane viewport's size
	 * @return our preferred dimensions
	 */
	public Dimension getPreferredSize(Dimension parentPreferredDimensions, Dimension scrollExtentDimensions) {
		parentPreferredDimensions.height = scrollExtentDimensions.height;
		
		return parentPreferredDimensions;		
	}
	
	public int getMaxPixelHeight() {
		int maxh = 0;
		int nGraphs = graphs.size();
		
		// kludge! no graphs, 1000 height
		if(nGraphs == 0)
			return 100;
		
		for (int i = 0; i < nGraphs; i++) {
			Graph cg = graphs.get(i);
			int val = cg.getPlotter().getYRange(gInfo, cg);
			if(val > maxh)
				maxh = val;
		}
		return maxh;
	}
	
	/**
	 * Get the values of max and min
	 * @return an array - v[0] = min, v[1] = max
	 */
	public int[] getMinMaxGraphValue() {
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		
		// no graphs? nice, small height 
		if(graphs.size() == 0) {
			return new int[] { 0, 100 };
		}
		
		for(Graph g : graphs) {
			for(Number v : g.graph.getData()) {
				int val = v.intValue();
				
				if(max < val)
					max = val;
				
				if(min > val)
					min = val;
			}
		}

		return new int[] { min, max };
	}
	
	/*
	 * QuickScorer class
	 * 
	 * This class is used to generate T-scores and R-scores
	 * for display in our title bar.
	 */

	QuickScorer scoreCalculator = new QuickScorer();

	/*
	 * calculateScores()
	 * 
	 * when we have exactly two samples, we can do t- and r-score calculations on them.
	 */
	private void calculateScores() {
		// only makes sense for two samples
		
		scorePostpend = "";
		
		if(graphs.size() != 2)
			return;
		
		int next = (current + 1) % graphs.size();

		// no scores!
		// this shouldn't happen...
		if(next == current)
			return;
		
		Sample s1, s2;
		Graph g1, g2;
		
		try {
			g1 = graphs.get(current);
			g2 = graphs.get(next);
			s1 = (Sample) g1.graph;
			s2 = (Sample) g2.graph;
		} catch (java.lang.ClassCastException ce) {
			// only works on Samples. Bail out if we're not dealing with them.
			return;
		}
				
		Year start1 = s1.getStart().add(g1.xoffset);
		Year start2 = s2.getStart().add(g2.xoffset);
		
        int o1, o2;
        if (start2.compareTo(start1) > 0) {
            o1 = start2.diff(start1);
            o2 = 0;
        } else {
            o1 = 0;
            o2 = start1.diff(start2);
        }

        scoreCalculator.calculate(s1, s2, o1, o2);

        scorePostpend = " [t: " + scoreCalculator.tscore + ", r: " + scoreCalculator.rvalue + "] ";
	}
	
	// stuff to make graphcontroller realizable
	public List<Graph> getSamples() {
		return graphs;
	}
	
	/**
	 * A class to 'manage' the graph panel
	 * Listens for property changes in GraphInfo and updates the graph accordingly
	 * @author Lucas Madar
	 *
	 */
	private class GraphManager implements PropertyChangeListener, AdjustmentListener {				
		public GraphManager() {
			gInfo.addPropertyChangeListener(this);
		}
		
		@SuppressWarnings("unused")
		public void remove() {
			gInfo.removePropertyChangeListener(this);
		}
		
		public void propertyChange(PropertyChangeEvent evt) {
			if(evt.getSource() != gInfo)
				return;
			
			System.out.println("Property: " + evt.getPropertyName());

			Set<UpdateAction> actions = updateActions.get(evt.getPropertyName());
			if(actions == null)
				return;

			System.out.print("Actions: ");
			for(UpdateAction a : actions) {
				System.out.print(a + ", ");
			}
			System.out.println();
			
			// baselines were added or removed
			if(actions.contains(UpdateAction.BASELINES_SCROLLBAR_UPDATE))
				horizontalScrollbarStatusChanged();

			// vertical axis added or removed
			if(actions.contains(UpdateAction.VERTICAL_AXIS_SCROLLBAR_UPDATE))
				verticalAxisStatusChanged();
			
			// recompute the range
			if(actions.contains(UpdateAction.RECOMPUTE_RANGE))
				computeRange();
			
			// graph size changed
			if(actions.contains(UpdateAction.UPDATE_SIZE)) 
				setPreferredSize(new Dimension(gInfo.getDrawBounds().span() * gInfo.getYearWidth(), getGraphHeight()));
			
			// revalidate - is this necessary?
			if(actions.contains(UpdateAction.REVALIDATE))
				revalidate();

			// repaint the vertical axis
			if(actions.contains(UpdateAction.VERTICAL_AXIS_REPAINT) && vertaxis != null) {
				vertaxis.repaint();
			}
			
			// repaint the graph
			if(actions.contains(UpdateAction.REPAINT))
				repaint();
		}

		/**
		 * Called when the status of the horizontal scrollbar chaged
		 */
		public void horizontalScrollbarStatusChanged() {
			ensureScrollerExists();
			
			if (!gInfo.isShowBaselines())
				scroller.getHorizontalScrollBar().removeAdjustmentListener(this);
			else
				scroller.getHorizontalScrollBar().addAdjustmentListener(this);			
		}
		
		/**
		 * Called when the status of the vertical axis changed
		 */
		public void verticalAxisStatusChanged() {
			ensureScrollerExists();

			if (gInfo.isShowVertAxis()) {
				vertaxis = new Axis(gInfo, PlotAgent.getDefault().getAxisType(), GrapherPanel.this);
				scroller.setRowHeaderView(vertaxis);
			} else {
				scroller.setRowHeaderView(null);
			}			
		}
		
		// the horizontal scrollbar changed
		public void adjustmentValueChanged(AdjustmentEvent e) {
			repaint();
		}		
	}
	
	private static Map<String, Set<UpdateAction>> updateActions = new HashMap<String, Set<UpdateAction>>();
	static {
		updateActions.put(GraphInfo.SHOW_GRAPH_PAPER_PROPERTY, 
				EnumSet.of(UpdateAction.REPAINT));
		
		updateActions.put(GraphInfo.SHOW_BASELINES_PROPERTY, 
				EnumSet.of(UpdateAction.REPAINT, UpdateAction.BASELINES_SCROLLBAR_UPDATE));
		
		updateActions.put(GraphInfo.SHOW_HUNDREDPERCENTLINES_PROPERTY, 
				EnumSet.of(UpdateAction.REPAINT));
		
		updateActions.put(GraphInfo.SHOW_GRAPH_NAMES_PROPERTY,
				EnumSet.of(UpdateAction.RECOMPUTE_RANGE, UpdateAction.UPDATE_SIZE,
						UpdateAction.REVALIDATE, UpdateAction.REPAINT));
		
		updateActions.put(GraphInfo.SHOW_VERT_AXIS_PROPERTY, 
				EnumSet.of(UpdateAction.VERTICAL_AXIS_SCROLLBAR_UPDATE, UpdateAction.REPAINT));
		
		updateActions.put(GraphInfo.YEAR_WIDTH_PROPERTY, 
				EnumSet.of(UpdateAction.RECOMPUTE_RANGE, UpdateAction.UPDATE_SIZE, UpdateAction.REVALIDATE, UpdateAction.REPAINT));

		updateActions.put(GraphInfo.TEN_UNIT_HEIGHT_PROPERTY, 
				EnumSet.of(UpdateAction.REPAINT, UpdateAction.VERTICAL_AXIS_REPAINT));
	};
	
	private static enum UpdateAction {
		REPAINT,
		BASELINES_SCROLLBAR_UPDATE,
		RECOMPUTE_RANGE,
		UPDATE_SIZE,
		REVALIDATE,
		VERTICAL_AXIS_SCROLLBAR_UPDATE,
		VERTICAL_AXIS_REPAINT;
	}
	
	private EventListenerList grapherListeners = new EventListenerList();

	/**
	 * Add a grapher listener to this graph
	 * @param listener
	 */
	public void addGrapherListener(GrapherListener listener) {
		grapherListeners.add(GrapherListener.class, listener);
	}
	
	/**
	 * Remove a grapher listener from this graph
	 * @param listener
	 */
	public void removeGrapherListner(GrapherListener listener) {
		grapherListeners.remove(GrapherListener.class, listener);
	}
	
	protected void fireGrapherEvent(GrapherEvent evt) {
		Object[] listeners = grapherListeners.getListenerList();
		
		for(int i = 0; i < listeners.length; i+=2) {
			if(listeners[i] == GrapherListener.class)
				((GrapherListener)listeners[i+1]).graphChanged(evt);
		}
	}
}
