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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import corina.Element;
import corina.Range;
import corina.Sample;
import corina.SampleEvent;
import corina.SampleListener;
import corina.Year;
import corina.core.App;
import corina.cross.Cross;
import corina.formats.WrongFiletypeException;
import corina.gui.Bug;
import corina.gui.FileDialog;
import corina.gui.SaveableDocument;
import corina.gui.UserCancelledException;
import corina.gui.XFrame;
import corina.gui.menus.EditMenu;
import corina.gui.menus.FileMenu;
import corina.gui.menus.HelpMenu;
import corina.gui.menus.WindowMenu;
import corina.index.Index;
import corina.prefs.PrefsEvent;
import corina.prefs.PrefsListener;
import corina.ui.Alert;
import corina.ui.Builder;
import corina.ui.I18n;
import corina.util.Overwrite;
import corina.util.PopupListener;

/**
 * A graph. It graphs any number of samples (or any Graphable) and allows the
 * user to scroll, slide the samples up/down/left/right, and scale them
 * vertically.
 * 
 * @see Graphable
 * 
 * @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i
 *         style="color: gray">dot</i> edu&gt;
 * @version $Id$
 */

// TODO:
// -- clean up .* imports: swing, awt.event
// -- extract some parts: svg exporting, printing?, png exporting?
// -- extract squish? (all 3?)
// -- make this use either GrapherPanel or BargraphPanel
// ---- rename GrapherPanel to GraphPanel
// ---- extract BargraphPanel from BargraphFrame
// -- add view->as Graph, as Bargraph, as Skeleton Plot options
// -- saving should save the same format for any of these, with a "type="
// -- printing should print the current view (ask the view to print it)
// -- if you drag onto the graph panel (while scrolling), don't draw cursor
// -- "mark all drops" feature
// -- put "-1" on x-axis, too?
// -- mark -1 and +1 with longer ticks
public class GraphWindow extends XFrame implements SampleListener,
		SaveableDocument,
		// DISABLED until printing works better:
		// PrintableDocument,
		Printable, PrefsListener {

	// SampleListener
	private void update(Sample s) {
		// data/format changed, need to recheck getScale();
		// BUG?: if data changed, but not format, won't this be unnecesary at
		// best, wrong at worst?
		// REFACTOR: why isn't this a 1-liner?
		// (reset-scale (find :key #'graph s plot.graphs))
		for (int i = 0; i < plot.graphs.size(); i++) {
			Graph g = (Graph) plot.graphs.get(i);
			if (g.graph == s) {
				g.scale = g.graph.getScale();
				break;
			}
		}

		// repaint
		repaint();
	}

	public void sampleRedated(SampleEvent e) {
		/* FIXME: handle this */
		update((Sample) e.getSource());
		// TODO: update title, too?
	}

	public void sampleDataChanged(SampleEvent e) {
		update((Sample) e.getSource());
	}

	public void sampleMetadataChanged(SampleEvent e) {
		plot.updateTitle(); // title might have changed
		// TODO: only if field=title?

		// TODO: make this: only if field==format!
		update((Sample) e.getSource());
	}

	public void sampleElementsChanged(SampleEvent e) {
	}

	// gui
	public GrapherPanel plot; // the plot area itself

	private JScrollPane scroller; // scroller enclosing the plot

	// data
	private List samples; // of Graph

	// adjust vertical spacing
	public void squeezeTogether() {
		for (int i = 0; i < samples.size(); i++)
			((Graph) samples.get(i)).yoffset = 0;
		repaint();
	}

	public void spreadOut() {
		// 20 'units' = 0 - 200%
		int spacing = plot.getYearSize() / 7;
		for (int i = 0; i < samples.size(); i++)
			((Graph) samples.get(i)).yoffset = i * spacing;
		repaint();
	}

	public void squishTogether() {
		// squish together samples in visible window

		// BUG: assumes sample[current] is visible.

		// first, set samples[current] = 0
		((Graph) samples.get(plot.current)).yoffset = 0;

		// compute viewport range
		// REFACTOR: write a getYearForPoint() method, and call that on both
		// ends of the visible JViewPane
		Year viewportLeft = plot.getRange().getStart().add(
				scroller.getHorizontalScrollBar().getValue() / plot.getYearSize());
		int viewportSize = scroller.getWidth() / plot.getYearSize();
		Range viewport = new Range(viewportLeft, viewportSize);

		// idea: emphasize middle 50% of viewport

		// for each other graph, minimize chi^2 (chi) in viewport
		for (int i = 0; i < samples.size(); i++) {
			// (skip current)
			if (i == plot.current)
				continue;

			// make sure it's there at all, otherwise, don't bother.
			// intersect(viewport, graph[i], graph[current])
			Range range = ((Graph) samples.get(i)).getRange();
			Range overlap = range.intersection(viewport);
			overlap = overlap.intersection(((Graph) samples.get(plot.current))
					.getRange());
			if (overlap.span() == 0)
				continue;

			// now, compute mean of sample[current][y] - sample[i][y]

			List data = ((Graph) samples.get(i)).graph.getData();
			int j = overlap.getStart().diff(range.getStart()); // index into
																// data[i]
			double dataScale = ((Graph) samples.get(i)).scale;

			List base = ((Graph) samples.get(plot.current)).graph.getData();
			int k = overlap.getStart().diff(
					((Graph) samples.get(plot.current)).getRange().getStart()); // graph.getStart());
																				// //
																				// index
																				// into
																				// base=data[plot.current]
			double baseScale = ((Graph) samples.get(plot.current)).scale;

			double mean = 0.0;
			for (Year y = overlap.getStart(); y.compareTo(overlap.getEnd()) <= 0; y = y
					.add(1)) {
				mean += ((Number) data.get(j++)).doubleValue() * dataScale
						- ((Number) base.get(k++)).doubleValue() * baseScale;
			}
			mean /= overlap.span();

			// make -mean its new offset
			((Graph) samples.get(i)).yoffset = (int) -mean;
		}

		// make the lowest one have yoffset=0 now
		int min = ((Graph) samples.get(0)).yoffset;
		for (int i = 1; i < samples.size(); i++)
			min = Math.min(min, ((Graph) samples.get(i)).yoffset);
		for (int i = 0; i < samples.size(); i++)
			((Graph) samples.get(i)).yoffset -= min;

		// repaint
		repaint();
	}

	// add a new sample
	public void add(Sample s) {
		samples.add(new Graph(s)); // doesn't get next yoffset, is that ok?
									// (yeah, sure)
		// need to recompute range now!
		repaint();
	}

	//
	// SaveableDocument
	//
	private String filename = null;

	public boolean isSaved() {
		return true; // fixme: false if saved once, but modified (?)
	}

	public void save() { // copied from gridder.java -- refactor?
		if (filename == null) {
			try {
				filename = FileDialog.showSingle("Save");

				// check for already-exists
				Overwrite.overwrite(filename);

			} catch (UserCancelledException uce) {
				return;
			}
		}

		// REFACTOR: combine these try-catch clauses!

		// save!
		try {
			LoadSave.save(filename, samples);
		} catch (IOException ioe) {
			// error!
			Alert.error("Error saving", "Error: " + ioe.getMessage());
		}
	}

	public void setFilename(String fn) {
		filename = fn;
	}

	public String getFilename() {
		return filename;
	}

	public String getDocumentTitle() {
		return getTitle();
	}

	//
	// PrintableDocument
	//
	public Object getPrinter(PageFormat pf) {
		return this; // !!! -- what about pf?
	}

	public String getPrintTitle() {
		String s = "Graph: " + samples.get(0);
		if (samples.size() > 1)
			s += ", and others";
		return s;
	}

	// Printable
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
		// only 1 page, at least yet
		if (pageIndex != 0)
			return NO_SUCH_PAGE;

		// draw it
		Graphics2D g2 = (Graphics2D) graphics;
		double sx = pageFormat.getImageableWidth() / plot.getWidth();
		double sy = 1;
		g2.scale(sx, sy);
		plot.paintComponent(g2); // ack!

		// done
		return PAGE_EXISTS;
	}

	public String toSVG() {
		try {
			// (the next 3 steps taken from
			// http://xml.apache.org/batik/svggen.html)

			// Get a DOMImplementation
			DOMImplementation domImpl = GenericDOMImplementation
					.getDOMImplementation();

			// Create an instance of org.w3c.dom.Document
			Document document = domImpl.createDocument(null, "svg", null);

			// Create an instance of the SVG Generator
			SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

			// draw the graph to it
			plot.paintComponent(svgGenerator);

			// stream it to a string
			Writer out = new StringWriter();
			svgGenerator.stream(out, false); // (false = don't use css)

			return out.toString();
		} catch (IOException ioe) {
			// technically, string writers say they can throw ioe's
			// (because writer can), but i don't think it can ever
			// actually happen.
			new Bug(ioe);
			return null; // can't happen
		}
	}

	// copy this graph to the clipboard as SVG -- is this really valuable? does
	// it work?
	private void copyToClipboard() {
		final String glue = toSVG();

		// copy = svg to clipboard
		// BROKEN: this doesn't work, for some reason. i'm not exactly
		// sure what i'd need to do to get copy-svg-to-clipboard to work.
		// help?

		// REFACTOR: this would be TextClipboard.copy(glue),
		// except it should be copied as data flavor
		// "image/svg+xml; class=java.lang.String".

		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();

		try {
			final DataFlavor f = new DataFlavor(
					"image/svg+xml; class=java.lang.String");

			Transferable t = new Transferable() {
				public Object getTransferData(DataFlavor flavor)
						throws UnsupportedFlavorException {
					if (f.equals(flavor))
						return glue;
					else
						throw new UnsupportedFlavorException(flavor);
				}

				public DataFlavor[] getTransferDataFlavors() {
					return new DataFlavor[] { f };
				}

				public boolean isDataFlavorSupported(DataFlavor flavor) {
					return f.equals(flavor);
				}
			};

			ClipboardOwner o = new ClipboardOwner() {
				public void lostOwnership(Clipboard clipboard,
						Transferable contents) {
					// who cares?
				}
			};

			c.setContents(t, o);
			// TextClipboard.copy(out.toString());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe!"); // !!!
		}
	}

	// an Edit menu with Copy = copy SVG to clipboard
	private static class GraphEditMenu extends EditMenu {
		GraphEditMenu(GraphWindow window) {
			this.window = window;
		}

		private GraphWindow window;

		/*
		 * -- DISABLED until i'm sure it works: copy svg to clipboard protected
		 * void addCopy() { JMenuItem copy = Builder.makeMenuItem("copy");
		 * copy.addActionListener(new AbstractAction() { public void
		 * actionPerformed(ActionEvent e) { window.copyToClipboard(); } });
		 * this.add(copy); }
		 */
	}

	private static class GraphViewMenu extends JMenu {
		// custom menus for graph windows
		private JMenuItem _axisMenu, _gridlinesMenu, _baselinesMenu, _compnamesMenu;

		private GraphWindow window;

		GraphViewMenu(GraphWindow win) {
			super(I18n.getText("view"));

			this.window = win;

			// Show/hide axis
			_axisMenu = Builder.makeMenuItem(Boolean.valueOf(
					App.prefs.getPref("corina.graph.vertical-axis"))
					.booleanValue() ? "vert_hide" : "vert_show");
			_axisMenu.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					boolean vis = Boolean.valueOf(
							App.prefs.getPref("corina.graph.vertical-axis")).booleanValue();
					
					window.plot.setAxisVisible(!vis);
					
					_axisMenu.setText(I18n.getText(vis ? "vert_show"
							: "vert_hide"));
				}
			});
			this.add(_axisMenu);

			// Show/hide gridlines
			_gridlinesMenu = Builder.makeMenuItem(Boolean.valueOf(
					App.prefs.getPref("corina.graph.graphpaper"))
					.booleanValue() ? "grid_hide" : "grid_show");
			_gridlinesMenu.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					boolean vis = Boolean.valueOf(
							App.prefs.getPref("corina.graph.graphpaper")).booleanValue();
					
					window.plot.setGraphPaperVisible(!vis);

					_gridlinesMenu.setText(I18n.getText(vis ? "grid_show"
							: "grid_hide"));
					repaint();
				}
			});
			this.add(_gridlinesMenu);

			// Show/hide baselines
			_baselinesMenu = Builder
					.makeMenuItem(Boolean.valueOf(
							App.prefs.getPref("corina.graph.baselines"))
							.booleanValue() ? "base_hide" : "base_show");
			_baselinesMenu.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {					
					boolean vis = Boolean.valueOf(App.prefs.getPref("corina.graph.baselines")).booleanValue();

					window.plot.setBaselinesVisible(!vis);
					window.plot.recreateAgent();
					
					_baselinesMenu.setText(I18n.getText(vis ? "base_show"
							: "base_hide"));
					repaint();
				}
			});
			this.add(_baselinesMenu);

			// Show/hide graph component names
			_compnamesMenu = Builder
					.makeMenuItem(Boolean.valueOf(
							App.prefs.getPref("corina.graph.componentnames"))
							.booleanValue() ? "compn_hide" : "compn_show");
			_compnamesMenu.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {					
					boolean vis = Boolean.valueOf(App.prefs.getPref("corina.graph.componentnames")).booleanValue();

					window.plot.setComponentNamesVisible(!vis);
					
					_compnamesMenu.setText(I18n.getText(vis ? "compn_show"
							: "compn_hide"));
					repaint();
				}
			});
			this.add(_compnamesMenu);
			
			// ---
			this.addSeparator();

			// TODO: put the baseline menuitems under an "Align" menu (and
			// reword them)

			// Squeeze together
			JMenuItem squeeze = Builder.makeMenuItem("baselines_align");
			squeeze.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					window.squeezeTogether();
				}
			});
			this.add(squeeze);

			// Spread apart
			JMenuItem spread = Builder.makeMenuItem("baselines_spread");
			spread.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					window.spreadOut();
				}
			});
			this.add(spread);

			// Squish
			JMenuItem squish = Builder.makeMenuItem("baselines_squish");
			squish.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
			// -- i don't know how i'd put VK_SPACE in a properties file
			// ("space" doesn't seem to work),
			// but i don't think anybody should ever change it, either.
			squish.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					try {
						window.squishTogether();
					} catch (Exception ex) {
						// see squishTogether() method for at least 1 remaining
						// bug
						new Bug(ex);
					}
				}
			});
			this.add(squish);
		}
	}
	
	// drop target
	private DropTargetListener dtl;

	// construct a GrapherPanel, add a GrapherListener, etc.
	private void createPanelAndDisplay() {
		// create a graph panel; put it in a scroll pane
		plot = new GrapherPanel(samples, this);
		scroller = new JScrollPane(plot,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		setContentPane(scroller);
				
		// set initial y-offsets: spread 'em out
		spreadOut();						
		
		// corner!
		JLabel black = new JLabel();
		black.setBackground(Color.getColor("corina.graph.background",
				Color.black));
		black.setOpaque(true);
		scroller.setCorner(JScrollPane.LOWER_LEFT_CORNER, black);

		// to set the initial title
		plot.updateTitle();
		
		// to turn on baselines and vert axis, if enabled...
		plot.postScrollpanedInit();		

		// special case: if there's an Index, align baselines (ugly test!)
		// REFACTOR: this doesn't do anything, does it?
		if (samples.size() == 2
				&& ((((Graph) samples.get(0)).graph instanceof Index) || (((Graph) samples
						.get(1)).graph instanceof Index)))
			squeezeTogether();

		// ooh, menubar
		{
			JMenuBar menubar = new JMenuBar();

			menubar.add(new FileMenu(this));
			menubar.add(new GraphEditMenu(this));
			menubar.add(new GraphViewMenu(this));
			if (App.platform.isMac())
				menubar.add(new WindowMenu(this));
			menubar.add(new HelpMenu());

			setJMenuBar(menubar);
		}

		// drag-n-drop
		dtl = new DropPlotter(this);
		DropTarget t1 = new DropTarget(getJMenuBar(), dtl); // on the menubar!
		DropTarget t3 = new DropTarget(plot, dtl); // on the plot!

		// context menu
		final SamplePopupMenu popup = new SamplePopupMenu();
		plot.addMouseListener(new PopupListener() {
			public void showPopup(MouseEvent e) {
				// select this graph
				int n = plot.getGraphAt(e.getPoint());

				// not on a graph? bail.
				if (n == -1)
					return;

				// select it
				plot.current = n;
				plot.repaint();

				// show the popup
				popup
						.setSample((Sample) ((Graph) plot.graphs
								.get(plot.current)).graph);
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
		// IDEA: if you set a member variable currentSample(?) whenever a new
		// sample is selected, you can simply say
		// new PopupListener(popup, plot)
		// here.

		App.prefs.addPrefsListener(this);
		
		// display the window
		pack();
		show();

		// give it focus, so you don't have to tab to it
		plot.requestFocus(); // must be after show(), i think

		// scroll to start of second graph
		scrollToSecondGraph();
	}

	// if there's more than one sample, scroll to start of
	// overlap, i.e., the start of the SECOND graph. (there's
	// certainly a better way to do this.)
	private void scrollToSecondGraph() {
		if (samples.size() == 1)
			return;
		// FIXME: this only makes sense if the graphs overlap.
		// if they don't ... ?

		// make y1, y2 the start of the first 2 graphs
		Year y1 = getStartOfGraph(0);
		Year y2 = getStartOfGraph(1);
		if (y1.compareTo(y2) > 0) {
			Year tmp = y1;
			y1 = y2;
			y2 = tmp;
		}

		// loop through the rest, keeping y1,y2 the first two, yearwise
		for (int i = 2; i < samples.size(); i++) {
			Year yi = getStartOfGraph(i);

			if (yi.compareTo(y1) < 0) {
				y2 = y1;
				y1 = yi;
			} else if (yi.compareTo(y2) < 0) {
				y2 = yi;
			}
		}

		// scroll to 1 year before the second one
		scrollTo(y2.add(-1));
	}

	// ?
	private Year getStartOfGraph(int i) {
		Graph g = (Graph) samples.get(i);
		return g.graph.getStart().add(g.xoffset);
	}

	// ----------------------------------------------------------------------
	// below here is just various constructors
	//

	/**
	 * Graph a single Sample.
	 * 
	 * @param s
	 *            the Sample to graph
	 */
	public GraphWindow(Sample s) {
		// samples
		samples = new ArrayList(2); // 2 things, max
		samples.add(new Graph(s));

		// summed -- add count, too
		if (s.isSummed())
			samples.add(new Graph(s.count, s.range.getStart(), I18n
					.getText("number_of_samples")));

		// observe
		s.addSampleListener(this);

		// go
		createPanelAndDisplay();
	}

	/**
	 * Graph all the files in a List of Elements.
	 * 
	 * @param ss
	 *            the List to get the Elements from
	 */
	public GraphWindow(List ss) {
		// samples
		boolean problem = false;
		samples = new ArrayList(ss.size());
		for (int i = 0; i < ss.size(); i++) {
			Element e = (Element) ss.get(i);

			if (!e.isActive()) // skip inactive
				continue;

			try {
				Sample s = e.load();
				samples.add(new Graph(s));
				s.addSampleListener(this);
			} catch (IOException ioe) {
				problem = true; // ick.
			}
		}

		// problem?
		if (problem) {
			Alert.error("Error loading sample(s)",
					"Some samples were not able to be loaded.");
		}

		// no samples => don't bother doing anything
		if (samples.isEmpty()) {
			dispose();
			return;
		}

		// go
		createPanelAndDisplay();
	}

	/**
	 * Graph any files the user chooses.
	 */
	public GraphWindow() {
		// get samples
		List ss = null;
		try {
			ss = FileDialog.showMulti(I18n.getText("plot"));
		} catch (UserCancelledException uce) {
			dispose();
			return;
		}

		// REFACTOR: everything below this point is the same as
		// GraphWindow(List)

		// samples
		boolean problem = false;
		samples = new ArrayList(ss.size());
		for (int i = 0; i < ss.size(); i++) {
			Element e = (Element) ss.get(i);

			if (!e.isActive()) // skip inactive
				continue;

			try {
				Sample s = e.load();
				samples.add(new Graph(s));
				s.addSampleListener(this);
			} catch (IOException ioe) {
				problem = true; // ick.
			}
		}

		// problem?
		if (problem) {
			Alert.error("Error loading sample(s)",
					"Some samples were not able to be loaded.");
		}

		// no samples => don't bother doing anything
		if (samples.isEmpty()) {
			dispose();
			return;
		}

		// go
		createPanelAndDisplay();
	}

	/**
	 * Graph an Index, and its target Sample.
	 * 
	 * @param i
	 *            the Index to graph
	 */
	public GraphWindow(Index i) {
		// samples
		samples = new ArrayList(2);
		samples.add(new Graph(i.getTarget()));
		samples.add(new Graph(i));

		// go
		createPanelAndDisplay();
	}

	/**
	 * Graph the two samples of a Cross, at one of the statistically significant
	 * overlaps. The graph is automatically scrolled to the start of the overlap
	 * interval.
	 * 
	 * @param c
	 *            the Cross to graph
	 * @param movingPosition
	 *            the end-year of the moving sample of the cross to graph
	 */
	public GraphWindow(Cross c, Year movingPosition) {
		// careful!
		Graph tmp;

		// samples
		samples = new ArrayList(2);
		samples.add(new Graph(c.getFixed()));
		samples.add(tmp = new Graph(c.getMoving()));

		// compute offset of moving sample
		tmp.xoffset = movingPosition.diff(c.getMoving().range.getEnd());

		// go
		createPanelAndDisplay();
	}

	/**
	 * (Re)create a plot that was saved to disk.
	 * 
	 * @param filename
	 *            the filename of the plot to load
	 */
	public GraphWindow(String filename) throws WrongFiletypeException {
		// load
		try {
			samples = LoadSave.load(filename);
		} catch (IOException ioe) {
			throw new WrongFiletypeException();
		}

		// no files loaded? ouch.
		if (samples.isEmpty())
			throw new WrongFiletypeException();

		// record filename
		this.filename = filename;

		// it worked, display it
		createPanelAndDisplay();
	}

	// scroll the left side to a particular year
	public void scrollTo(Year y) {
		// compute how much to scroll
		int dy = Math.abs(y.diff(plot.getRange().getStart()));

		// scroll
		scroller.getHorizontalScrollBar().setValue(dy * plot.getYearSize());
	}

	// live-updating preferences
	public void prefChanged(PrefsEvent e) {
		plot.update();
		repaint();
	}

	protected void finalize() throws Throwable {
		super.finalize();
		App.prefs.removePrefsListener(this);
	}
}