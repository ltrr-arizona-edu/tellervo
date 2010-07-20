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

import java.awt.BorderLayout;
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
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.cross.Cross;
import edu.cornell.dendro.corina.formats.WrongFiletypeException;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.gui.FileDialog;
import edu.cornell.dendro.corina.gui.SaveableDocument;
import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.gui.XFrame;
import edu.cornell.dendro.corina.gui.menus.HelpMenu;
import edu.cornell.dendro.corina.gui.menus.WindowMenu;
import edu.cornell.dendro.corina.index.Index;
import edu.cornell.dendro.corina.prefs.PrefsEvent;
import edu.cornell.dendro.corina.prefs.PrefsListener;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleEvent;
import edu.cornell.dendro.corina.sample.SampleListener;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.Overwrite;
import edu.cornell.dendro.corina.util.PopupListener;

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

	/**
	 * 
	 */
	private static final long serialVersionUID = 5018275020308926052L;

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
	public GraphElementsPanel elemPanel;
	public GraphController controller;

	private JScrollPane scroller; // scroller enclosing the plot

	// data
	private List<Graph> samples; // of Graph

	// add a new sample
	public void add(Sample s) {
		samples.add(new Graph(s)); // doesn't get next yoffset, is that ok?
									// (yeah, sure)
		plot.update();
		
		// be careful with the elements panel, too...
		elemPanel.loadSamples(samples);
		elemPanel.setSelectedIndex(plot.current);
	}

	// add a List of ELEMENTS
	public void add(ElementList ns) {
		// samples
		boolean problem = false;
		for (int i = 0; i < ns.size(); i++) {
			Element e = ns.get(i);

			if (!ns.isActive(e)) // skip inactive
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
		
		plot.update();
		
		// be careful with the elements panel, too...
		elemPanel.loadSamples(samples);
		elemPanel.setSelectedIndex(plot.current);
	}

	@Override
	public void remove(int idx) {
		samples.remove(idx);
		
		if(plot.current > samples.size())
			plot.current--;

		plot.update();
		
		// be careful with the elements panel, too...
		elemPanel.loadSamples(samples);
		elemPanel.setSelectedIndex(plot.current);
	}

	//
	// SaveableDocument
	//
	private String filename = null;

	public boolean isSaved() {
		return true; // fixme: false if saved once, but modified (?)
	}

    // saveabledocument -- yes, we can use save as...
    public boolean isNameChangeable() { return true; };
	
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

	// drop target
	private DropTargetListener dtl;

	// construct a GrapherPanel, add a GrapherListener, etc.
	private void createPanelAndDisplay() {
		// create a graph panel; put it in a scroll pane
		plot = new GrapherPanel(samples, this);
		
		scroller = new JScrollPane(plot,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		controller = new GraphController(plot, scroller);
		
		JPanel content = new JPanel(new BorderLayout());
		setContentPane(content);
		
		elemPanel = new GraphElementsPanel(samples, this);
		content.add(elemPanel, BorderLayout.EAST);
		content.add(scroller, BorderLayout.CENTER);
		// re-enable me when graph toolbar is done
		//content.add(new GraphToolbar(this), BorderLayout.NORTH);
		
		elemPanel.setVisible(false);
		
		// create actions for toolbar and menubar (below)
		GraphActions actions = new GraphActions(plot, elemPanel, controller);		
		
		// create toolbar
		JToolBar toolbar = new GraphToolbar(actions);
		content.add(toolbar, BorderLayout.NORTH);
				
		// set initial y-offsets: spread 'em out
		controller.spreadOut(50);						
		
		// corner!
		JLabel black = new JLabel();
		black.setBackground(GraphPrefs.BACKGROUND.get());
		black.setOpaque(true);
		scroller.setCorner(ScrollPaneConstants.LOWER_LEFT_CORNER, black);

		// special case: if there's an Index, align baselines (ugly test!)
		// REFACTOR: this doesn't do anything, does it?
		if (samples.size() == 2
				&& ((((Graph) samples.get(0)).graph instanceof Index) || (((Graph) samples
						.get(1)).graph instanceof Index)))
			controller.squeezeTogether();

		// ooh, menubar
		{
			JMenuBar menubar = new JMenuBar();
			
			menubar.add(new GraphFileMenu(this));
			menubar.add(new GraphEditMenu(this));
			menubar.add(new GraphViewMenu(this, actions));
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
		plot.addMouseListener(new PopupListener() {
			@Override
			public void showPopup(MouseEvent e) {
				// select this graph
				int n = plot.getGraphAt(e.getPoint());

				// not on a graph? bail.
				if (n == -1){
					final GraphBGPopupMenu bgpopup = new GraphBGPopupMenu();
					bgpopup.show(e.getComponent(), e.getX(), e.getY());
					return;
				}

				
				// Create sample popup menu
				final SamplePopupMenu popup = new SamplePopupMenu(plot);
				
				// select it
				plot.current = n;
				plot.repaint();
				
				elemPanel.setSelectedIndex(plot.current);

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
		samples = new ArrayList<Graph>(2); // 2 things, max
		samples.add(new Graph(s));

		// summed -- add count, too
		if (s.isSummed())
			samples.add(new Graph(s.getCount(), s.getRange().getStart(), I18n
					.getText("number_of_samples")));

		// observe
		s.addSampleListener(this);

		// go
		createPanelAndDisplay();
	}

	/**
	 * Graph all the files in a List of Elements.
	 * 
	 * @param ss the List to get the Elements from
	 */
	public GraphWindow(ElementList ss) {
		// Empty list? Open a dialog to create it
		if(ss == null) {
			try {
				ss = FileDialog.showMulti(I18n.getText("plot"));
			} catch (UserCancelledException uce) {
				dispose();
				return;
			}
		}

		// samples
		boolean problem = false;
		samples = new ArrayList<Graph>(ss.size());
		for (int i = 0; i < ss.size(); i++) {
			Element e = ss.get(i);

			if (!ss.isActive(e)) // skip inactive
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
	 * Graph all the files in a List of Elements AND
	 * a single sample to go with it.
	 * 
	 * @param s
	 *            the Sample to graph
	 * 
	 * @param ss
	 *            the List to get the Elements from
	 */
	public GraphWindow(Sample s, ElementList ss) {
		// samples
		boolean problem = false;
		samples = new ArrayList<Graph>(ss.size() + 2);
		samples.add(new Graph(s));

		// summed -- add count, too
		if (s.isSummed())
			samples.add(new Graph(s.getCount(), s.getRange().getStart(), I18n
					.getText("number_of_samples")));

		// observe
		s.addSampleListener(this);
		
		for (int i = 0; i < ss.size(); i++) {
			Element e = ss.get(i);

			if (!ss.isActive(e)) // skip inactive
				continue;

			try {
				Sample ns = e.load();
				samples.add(new Graph(ns));
				ns.addSampleListener(this);
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
		this((ElementList) null);
	}

	/**
	 * Graph an Index, and its target Sample.
	 * 
	 * @param i
	 *            the Index to graph
	 */
	public GraphWindow(Index i) {
		// samples
		samples = new ArrayList<Graph>(2);
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
		samples = new ArrayList<Graph>(2);
		samples.add(new Graph(c.getFixed()));
		samples.add(tmp = new Graph(c.getMoving()));

		// compute offset of moving sample
		tmp.xoffset = movingPosition.diff(c.getMoving().getRange().getEnd());

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
			samples = (List<Graph>) LoadSave.load(filename);
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
		scroller.getHorizontalScrollBar().setValue(dy * plot.getYearWidth());
	}
	
	public void panelSelectionChanged() {
		if(elemPanel.getSelectedIndex() != plot.current)
		{
			elemPanel.setSelectedIndex(plot.current);
			
			Graph g = (Graph) samples.get(plot.current);
			Color gcolor = g.getColor(false);			
			elemPanel.setColor(gcolor);	
		}
	}
	
	public void listSelectionChanged() {
		if(plot.current == elemPanel.getSelectedIndex() || elemPanel.getSelectedIndex() < 0)
			return;
		// select it
		plot.current = elemPanel.getSelectedIndex();
		
		Graph g = (Graph) samples.get(plot.current);
		Color gcolor = g.getColor(false);
		elemPanel.setColor(gcolor);
		
		plot.repaint();		
		plot.updateTitle();
	}
	
	public void setActiveColor(Color c) {
		Graph g = (Graph) plot.graphs.get(plot.current);
		g.setColor(c, c);
		plot.repaint();
	}

	// live-updating preferences
	public void prefChanged(PrefsEvent e) {
		plot.update();
		repaint();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		App.prefs.removePrefsListener(this);
	}

	public Object getSavedDocument() {
		return plot;
	}

	@Override
	public void sampleDisplayUnitsChanged(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}
}