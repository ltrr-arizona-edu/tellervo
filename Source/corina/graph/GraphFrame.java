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
import corina.Element;
import corina.SampleListener;
import corina.SampleEvent;
import corina.index.Index;
import corina.files.WrongFiletypeException;
import corina.cross.Cross;
import corina.gui.XFrame;
import corina.gui.XMenubar;
import corina.gui.SaveableDocument;
import corina.gui.PrintableDocument;
import corina.gui.HasPreferences;
import corina.gui.FileDialog;
import corina.gui.UserCancelledException;
import corina.prefs.PrefsDialog;
import corina.gui.Bug;
import corina.util.Overwrite;
import corina.ui.Builder;
import corina.ui.I18n;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.print.*; // !!!
import javax.swing.*;
import javax.swing.event.*;

import java.awt.dnd.*;
import java.awt.datatransfer.*;

/**
   A graph.  It graphs any number of samples (or any Graphable) and
   allows the user to scroll, slide the samples up/down/left/right,
   and scale them vertically.

   @see Graphable

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$
*/

// to add:
// - allow copying to clipboard (how?)

public class GraphFrame extends XFrame implements SampleListener,
						  SaveableDocument,
						  PrintableDocument,
						  Printable,
                                                  HasPreferences {

    // SampleListener
    public void sampleRedated(SampleEvent e) { /* FIXME: handle this */ }
    public void sampleDataChanged(SampleEvent e) {
	// data changed, need to recheck getScale();
	Sample source = (Sample) e.getSource();
	for (int i=0; i<plot.graphs.size(); i++) {
	    Graph g = (Graph) plot.graphs.get(i);
	    if (g.graph == source) {
		g.scale = g.graph.getScale();
		break;
	    }
	}

	// repaint
	repaint();
    }
    public void sampleMetadataChanged(SampleEvent e) {
	plot.updateTitle(); // title might have changed
    }
    public void sampleFormatChanged(SampleEvent e) { }
    public void sampleElementsChanged(SampleEvent e) { } // should this update the count line?

    // initial vertical spacing between graphs, in pixels
    public final static int SPACING = 20;

    // gui
    public GrapherPanel plot;			// the plot area itself
    private JScrollPane scroller;		// scroller enclosing the plot

    // data
    private List samples;		// of Graph

    // adjust vertical spacing
    public void squeezeTogether() {
	for (int i=0; i<samples.size(); i++)
	    ((Graph) samples.get(i)).yoffset = 0;
	repaint();
    }
    public void spreadOut() {
	for (int i=0; i<samples.size(); i++)
	    ((Graph) samples.get(i)).yoffset = i*SPACING;
	repaint();
    }
    public void squishTogether() {
        // squish together samples in visible window

        // BUG: assumes sample[current] is visible.

        // first, set samples[current] = 0
        ((Graph) samples.get(plot.current)).yoffset = 0;

        // compute viewport range
        Year viewportLeft = plot.bounds.getStart().add(scroller.getHorizontalScrollBar().getValue() / plot.yearSize);
        int viewportSize = scroller.getSize().width / plot.yearSize;
        Range viewport=new Range(viewportLeft, viewportSize);

        // use only middle 50%?
        // viewport = new Range(viewport.getStart().add(viewport.span()/4), viewport.span()/2);
        // no, then samples which only have a couple years showing are ignored entirely.
        // better would be to (1) run once with full viewport, then again with 50%, or
        // (2) emphasize the center to begin with (how?)

        // for each other graph, minimize chi^2 (chi) in viewport
        for (int i=0; i<samples.size(); i++) {
            // (skip current)
            if (i == plot.current)
                continue;

            // make sure it's there at all, otherwise, don't bother.
            Range range = ((Graph) samples.get(i)).getRange();
            Range overlap = range.intersection(viewport).intersection(((Graph) samples.get(plot.current)).getRange());
            if (overlap.span() == 0)
                continue;

            // compute mean of sample[current][y] - sample[i][y]

            List data = ((Graph) samples.get(i)).graph.getData();
            int j = overlap.getStart().diff(range.getStart()); // index into data[i]
            double dataScale = ((Graph) samples.get(i)).scale;

            List base = ((Graph) samples.get(plot.current)).graph.getData();
            int k = overlap.getStart().diff(((Graph) samples.get(plot.current)).getRange().getStart()); // graph.getStart()); // index into base=data[plot.current]
            double baseScale = ((Graph) samples.get(plot.current)).scale;

            double mean = 0.0;
            for (Year y=overlap.getStart(); y.compareTo(overlap.getEnd())<=0; y=y.add(1)) {
                mean += ((Number) data.get(j++)).doubleValue()*dataScale - ((Number) base.get(k++)).doubleValue()*baseScale;
            }
            mean /= overlap.span();

            // make -mean its new offset
            ((Graph) samples.get(i)).yoffset = (int) -mean;
        }

        // make the lowest one have yoffset=0 now
        int min = ((Graph) samples.get(0)).yoffset;
        for (int i=1; i<samples.size(); i++)
            min = Math.min(min, ((Graph) samples.get(i)).yoffset);
        for (int i=0; i<samples.size(); i++)
            ((Graph) samples.get(i)).yoffset -= min;

        // repaint
        repaint();
    }

    // adjust horizontal scale -- NOT FINISHED
    public void fixedScale() {
	// use std scale
	plot.queryScale();
	repaint();
    }
    public void fixedWidth() {
	// use width for scale
	plot.yearSize = getWidth() / plot.bounds.span();
    }

    // add a new sample
    public void add(Sample s) {
	samples.add(new Graph(s)); // doesn't get next yoffset, is that ok?  (yeah, sure)
	// need to recompute range now!
	repaint();
    }

    //
    // SaveableDocument
    //
    private String filename=null;
    public boolean isSaved() {
	return true; // fixme: false if saved once, but modified (?)
    }
    public void save() { // copied from gridder.java -- refactor?
        if (filename == null) {
            try {
                filename = FileDialog.showSingle("Save");
            } catch (UserCancelledException uce) {
                return;
            }

            // check for already-exists
            if (new File(filename).exists() && Overwrite.overwrite(filename))
                return; // should return FAILURE -- how?
        }

        // save!
        try {
            LoadSave.save(filename, samples);
        } catch (IOException ioe) {
            // error!
            JOptionPane.showMessageDialog(null,
                                          "Error: " + ioe.getMessage(),
                                          "Error saving",
                                          JOptionPane.ERROR_MESSAGE);
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
    public int getPrintingMethod() {
        return PrintableDocument.PRINTABLE;
    }
    public Pageable makePageable(PageFormat pf) {
        return null;
    }
    public Printable makePrintable(PageFormat pf) {
        return this;
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

        // figure out the margins
        int xmin = (int) pageFormat.getImageableX();
        int xmax = (int) (pageFormat.getWidth() - pageFormat.getImageableX() - pageFormat.getImageableWidth());
        int ymin = (int) pageFormat.getImageableY();
        int ymax = (int) (pageFormat.getHeight() - pageFormat.getImageableY() - pageFormat.getImageableHeight());

        // draw it
        Graphics2D g2 = (Graphics2D) graphics;
        double sx = pageFormat.getImageableWidth() / plot.getSize().width;
        double sy = 1;
        g2.scale(sx, sy);
        plot.paintComponent(g2); // ack!

        // done
        return PAGE_EXISTS;
    }

    // custom menus for graph windows
    private JMenuItem _axisMenu, _gridlinesMenu, _baselinesMenu;
    private JMenu[] makeMenus() {
	// spacing
	JMenu v = Builder.makeMenu("view");

	// Show/hide axis
	_axisMenu = Builder.makeMenuItem("vert_show");
	_axisMenu.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    if (getAxisVisible()) { // visible, now hide
			_axisMenu.setText(I18n.getText("vert_show"));
			setAxisVisible(false);
		    } else {
			_axisMenu.setText(I18n.getText("vert_hide"));
			setAxisVisible(true);
		    }
		}
	    });
	v.add(_axisMenu);

	// Show/hide gridlines
	_gridlinesMenu = Builder.makeMenuItem(Boolean.getBoolean("corina.graph.graphpaper") ?
					      "grid_hide" : "grid_show");
	_gridlinesMenu.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    boolean vis = Boolean.getBoolean("corina.graph.graphpaper");
		    if (vis) { // visible, now hide
			System.setProperty("corina.graph.graphpaper", "false");
			_gridlinesMenu.setText(I18n.getText("grid_show"));
		    } else {
			System.setProperty("corina.graph.graphpaper", "true");
			_gridlinesMenu.setText(I18n.getText("grid_hide"));
		    }
		    repaint();
		}
	    });
	v.add(_gridlinesMenu);

	// Show/hide baselines
	_baselinesMenu = Builder.makeMenuItem(Boolean.getBoolean("corina.graph.baselines") ?
					      "base_hide" : "base_show");
	_baselinesMenu.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    boolean vis = Boolean.getBoolean("corina.graph.baselines");
		    if (vis) { // visible, now hide
			plot.setBaselinesVisible(false);
			_baselinesMenu.setText(I18n.getText("base_show"));
		    } else {
			plot.setBaselinesVisible(true);
			_baselinesMenu.setText(I18n.getText("base_hide"));
		    }
		    plot.recreateAgent();
		    repaint();
		}
	    });
	v.add(_baselinesMenu);

	// ---
	v.addSeparator();

	// Squeeze together
	JMenuItem squeeze = Builder.makeMenuItem("baselines_align");
	squeeze.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    squeezeTogether();
		}
	    });
	v.add(squeeze);

	// Spread apart
	JMenuItem spread = Builder.makeMenuItem("baselines_spread");
	spread.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    spreadOut();
		}
	    });
	v.add(spread);

        // Squish
        JMenuItem squish = Builder.makeMenuItem("baselines_squish");
        squish.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
        // -- i don't know how i'd put VK_SPACE in a properties file ("space" doesn't seem to work),
        // but i don't think anybody should ever change it, either.
        squish.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                try { // this is probably still buggy...
                    squishTogether();
                } catch (Exception ex) {
                    Bug.bug(ex);
                }
            }
        });
        v.add(squish);

	/* -- horizontal scaling doesn't work yet
	   (plus there's no i18n text for these)
	// scale
	JMenu scale = Builder.makeMenu("scale");

	JMenuItem fixedScale = Builder.makeMenuItem("fixed_scale");
	fixedScale.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    fixedScale();
		}
	    });
	scale.add(fixedScale);

	JMenuItem fixedWidth = Builder.makeMenuItem("fixed_width");
	fixedWidth.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    fixedWidth();
		}
	    });
	scale.add(fixedWidth);
	*/

	// (return the menus)
	return new JMenu[] { v, };
    }

    private Axis a = new Axis();
    private boolean _axisVisible = false;
    public void setAxisVisible(boolean visible) {
	_axisVisible = visible;

	if (_axisVisible) {
	    scroller.setRowHeaderView(a);
	    DropTarget t2 = new DropTarget(a, dtl); // on the axis!
	    repaint();
	} else {
	    scroller.setRowHeaderView(null);
	    repaint();
	}
    }
    public boolean getAxisVisible() {
	return _axisVisible;
    }

    // drop target
    private DropTargetListener dtl;

    // construct a GrapherPanel, add a GrapherListener, etc.
    private void createPanelAndDisplay() {
	// create a grapherpanel; make scrollerpanel which scrolls by decades/centuries
	plot = new GrapherPanel(samples, this);
	scroller = new JScrollPane(plot,
				   ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
				   ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	scroller.getHorizontalScrollBar().setUnitIncrement(plot.yearSize * 10);
	scroller.getHorizontalScrollBar().setBlockIncrement(plot.yearSize * 100);
	setContentPane(scroller); // not getContentPane().add(scroller, BorderLayout.CENTER)?

	// corner!
	JLabel black = new JLabel();
	black.setBackground(Color.getColor("corina.graph.background", Color.black));
	black.setOpaque(true);
	scroller.setCorner(JScrollPane.LOWER_LEFT_CORNER, black);

	// vert axis!
	setAxisVisible(false);

	// to set the initial title
	plot.updateTitle();

	// special case: if there's an Index, align baselines (this test is ugly!)
	if (samples.size()==2 &&
	    ((((Graph) samples.get(0)).graph instanceof Index) ||
	     (((Graph) samples.get(1)).graph instanceof Index)))
	    squeezeTogether();

	// hack, hack! -- but it works.
	plot.setHoriz(scroller.getHorizontalScrollBar());

	// ooh, menubar
	setJMenuBar(new XMenubar(this, makeMenus()));

	// drag-n-drop
	dtl = new DropPlotter(this);
	DropTarget t1 = new DropTarget(getJMenuBar(), dtl); // on the menubar!
	DropTarget t3 = new DropTarget(plot, dtl); // on the plot!

	// context menu
	new PopupListener(new PopupMenu(), plot);

	// display the window
	pack();
	int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	setSize(new Dimension(width, 480));
	show();

	// give it focus, so you don't have to tab to it
	plot.requestFocus(); // must be after show(), i think

	// if there's more than one sample, scroll to start of
	// overlap, i.e., the start of the SECOND graph.  (there's
	// certainly a better way to do this.)
	if (samples.size() > 1) {
	    Graph g1 = (Graph) samples.get(0);
	    Year y1 = g1.graph.getStart().add(g1.xoffset);
	    Graph g2 = (Graph) samples.get(1);
	    Year y2 = g2.graph.getStart().add(g2.xoffset);
	    if (y1.compareTo(y2) > 0) {
		Year tmp = y1;
		y1 = y2;
		y2 = tmp;
	    }
	    for (int i=2; i<samples.size(); i++) {
		Graph gi = (Graph) samples.get(i);
		Year yi = gi.graph.getStart().add(gi.xoffset);
		if (yi.compareTo(y1) < 0) {
		    y2 = y1;
		    y1 = yi;
		} else if (yi.compareTo(y2) < 0) {
		    y2 = yi;
		}
	    }
	    scrollTo(y2.add(-1));
	}
    }

    // ----------------------------------------------------------------------
    // below here is just various constructors
    //

    /** Graph a single Sample.
	@param s the Sample to graph */
    public GraphFrame(Sample s) {
	// samples
	samples = new ArrayList(2); // 2 things, max
	samples.add(new Graph(s));

	// summed -- add count, too
	if (s.isSummed())
	    samples.add(new Graph(s.count, s.range.getStart(),
				  I18n.getText("number_of_samples")));

	// observe
	s.addSampleListener(this);

	// go
	createPanelAndDisplay();
    }

    /** Graph all the files in a List of Elements.
	@param ss the List to get the Elements from */
    public GraphFrame(List ss) {
	// samples
	boolean problem = false;
	samples = new ArrayList(ss.size());
	for (int i=0; i<ss.size(); i++) {
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
	    JOptionPane.showMessageDialog(null,
					  "Some samples were not able to be loaded.",
					  "Error loading sample(s)",
					  JOptionPane.ERROR_MESSAGE);
        }

        // no samples => don't bother doing anything
        if (samples.isEmpty()) {
		dispose();
		return;
        }

	// set initial y-offsets: spread 'em out
	spreadOut();

	// go
	createPanelAndDisplay();
    }

    /** Graph an Index, and its target Sample.
	@param i the Index to graph */
    public GraphFrame(Index i) {
	// samples
	samples = new ArrayList(2);
	samples.add(new Graph(i.getTarget()));
	samples.add(new Graph(i));

	// go
	createPanelAndDisplay();
    }

    /** Graph the two samples of a Cross, at one of the statistically
        significant overlaps.  The graph is automatically scrolled to
        the start of the overlap interval.
	@param c the Cross to graph
	@param movingPosition the end-year of the moving sample
	of the cross to graph */
    public GraphFrame(Cross c, Year movingPosition) {
	// careful!
	Graph tmp;

	// samples
	samples = new ArrayList(2);
	samples.add(new Graph(c.getFixed()));
	samples.add(tmp = new Graph(c.getMoving()));

	// compute offset of moving sample
	tmp.xoffset = movingPosition.diff(c.getMoving().range.getEnd());

	// set initial y-offsets: spread 'em out
	spreadOut();

	// go
	createPanelAndDisplay();
    }

    /** (Re)create a plot that was saved to disk.
	@param filename the filename of the plot to load */
    public GraphFrame(String filename) throws WrongFiletypeException {
	// load
	try {
	    samples = LoadSave.load(filename);
	} catch (IOException ioe) {
	    throw new WrongFiletypeException();
	}

	// no files loaded?  ouch.
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
	int dy = Math.abs(y.diff(plot.bounds.getStart()));

	// scroll
	scroller.getHorizontalScrollBar().setValue(dy * plot.yearSize);
    }

    // live-updating preferences
    public void refreshFromPreferences() {
	plot.update();
	repaint();
    }
                                                  }
