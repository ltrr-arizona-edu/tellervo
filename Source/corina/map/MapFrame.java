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

package corina.map;

import corina.site.Site;
import corina.site.SiteDB;
import corina.gui.XFrame;
import corina.gui.PrintableDocument;
import corina.gui.XMenubar;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.ParseException;

import java.util.Timer;
import java.util.TimerTask;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.BorderLayout;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import java.awt.print.Printable;
import java.awt.print.Pageable;
import java.awt.print.PageFormat;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.Box;
import javax.swing.AbstractAction;

public class MapFrame extends XFrame implements PrintableDocument, ComponentListener {

    private MapPanel mapPanel;
    private Site s1=null, s2[]=null;
    private JComboBox zoomer;
    private JMenuItem zoomIn, zoomOut;

    public int getPrintingMethod() {
        return PrintableDocument.PRINTABLE;
    }
    public Pageable makePageable(PageFormat pf) {
        return null;
    }
    public Printable makePrintable(PageFormat pf) {
        try {
            mapPanel.setPageFormat(pf);
            return mapPanel;
        } catch (Exception e) {
            System.out.println("e! " + e);
            return null; // ouch!
        }
    }
    public String getPrintTitle() {
        return "Map"; // add location/sites/etc.
    }

    public MapFrame() throws IOException {
        super();
        init(); // init stuff
    }

    public MapFrame(Site s1, Site s2) throws IOException {
        super();
        init();

        // set s1, s2
        this.s1 = s1;
        this.s2 = new Site[] { s2 };
        mapPanel.s1 = s1;
        mapPanel.s2 = new Site[] { s2 };

        // center between them
        mapPanel.getView().center = Location.midpoint(s1.location, s2.location);
    }

    public MapFrame(Site s1, Site s2[]) throws IOException {
        super();
        init();

        // set s1, s2
        this.s1 = s1;
        this.s2 = s2;
        mapPanel.s1 = s1;
        mapPanel.s2 = s2;

        // center on s1
        mapPanel.getView().center = (Location) s1.location.clone();
    }

    public MapFrame(SiteDB db) throws IOException {
        super();
        init();

        // set s1, s2
        this.s1 = null;
        this.s2 = (Site[]) db.sites.toArray(new Site[0]);
        mapPanel.s1 = null;
        mapPanel.s2 = s2;

        // center?  well, don't bother for now.
        // TODO: center on the average site in the map.  (what's "average" on a circular loop?)
        // TODO: zoom out to show all sites (?) -- seems harder
        // mapPanel.getRenderer().location = (Location) s1.location.clone();
    }

    // component listener
    private Timer t = null;
    public void componentResized(ComponentEvent e) {
	// tell the panel how big he's going to be now
	mapPanel.setWidth(mapPanel.getSize().width);
	mapPanel.setHeight(mapPanel.getSize().height);

	// update after a slight delay -- this makes live-resizing
	// much more responsive (otherwise it'd compute each
	// intermediate size).  BUG: the downside of this is there are
	// now intermittent drawing errors.  oops.
	if (t != null)
	    t.cancel();
	t = new Timer();
	t.schedule(new TimerTask() {
		public void run() {
		    mapPanel.updateBuffer(); // expensive
		    mapPanel.repaint(); // cheap
		    t = null;
		}
	    }, 250 /* ms */);
    }
    public void componentHidden(ComponentEvent e) { } // timer is private, hence i need to implement
    public void componentMoved(ComponentEvent e) { } // componentlistener myself, hence i need to
    public void componentShown(ComponentEvent e) { } // have a bunch of empty method bodies.  ack.

    public void init() throws IOException {
        setTitle("Map");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // add mappanel
        mapPanel = new MapPanel();
        // mapPanel.updateBuffer(); // it's "resized" when the frame first appears
        getContentPane().add(mapPanel, BorderLayout.CENTER);

        // toolbox
        ToolBox tools = new ToolBox(mapPanel.getView(), mapPanel);
        getContentPane().add(tools, BorderLayout.NORTH);

        // status bar -- doesn't work (!) (?)
        // final JLabel label = new JLabel("xyz");
        // getContentPane().add(label, BorderLayout.SOUTH);
        mapPanel.setLabel(this); // FIXME: OBSOLETE?

        // scrollbars -- need more complex layout for this stuff now
        JScrollBar sb1 = new JScrollBar(JScrollBar.HORIZONTAL,
                                        (int) mapPanel.getView().center.longitude,
                                        10, // changes -- augh!
                                        -180, 180);
        sb1.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                mapPanel.getView().center.longitude = e.getValue();
                mapPanel.updateBuffer();
                mapPanel.repaint();
            }
        });
        JScrollBar sb2 = new JScrollBar(JScrollBar.VERTICAL,
                                        - (int) mapPanel.getView().center.latitude,
                                        10, // changes -- augh!
                                        -90, 90);
        sb2.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                mapPanel.getView().center.latitude = -e.getValue(); // +90 is up, -90 is down, so negate 'em
                mapPanel.updateBuffer();
                mapPanel.repaint();
            }
        });
	JPanel flow = new JPanel();
	flow.setLayout(new BorderLayout());
	zoomer = new JComboBox(new String[] { "10%", "25%", "50%", "75%", "100%", "150%", "200%", "400%", "800%", "1600%" });
        zoomer.setSelectedIndex(4); // 100% is default
        Font f = zoomer.getFont();
        zoomer.setFont(new Font(f.getFontName(), f.getStyle(), sb1.getPreferredSize().height * 3/4));
        zoomer.setPreferredSize(new Dimension(zoomer.getPreferredSize().width, sb1.getPreferredSize().height));
        zoomer.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                try {
			String pct = (String) zoomer.getSelectedItem();
			DecimalFormat fmt = new DecimalFormat("#%");
			double z = ((Number) fmt.parse(pct)).doubleValue();

			updateZoomMenus();

			mapPanel.getView().zoom = z;
			mapPanel.updateBuffer();
			mapPanel.repaint();
		    } catch (ParseException pe) {
			// can't happen.  (trust me.)
		    }
		}
	    });
	flow.add(zoomer, BorderLayout.WEST);
	flow.add(sb1, BorderLayout.CENTER);
	flow.add(Box.createHorizontalStrut(sb2.getPreferredSize().width), BorderLayout.EAST);
	flow.setMaximumSize(new Dimension(1000, 10));
	getContentPane().add(flow, BorderLayout.SOUTH);
	getContentPane().add(sb2, BorderLayout.EAST);

	// watch for resize
	addComponentListener(this);

	// add menubar
	setJMenuBar(new XMenubar(this, makeMenus()));

	// set size, and show it
	pack();
	setSize(new Dimension(640, 480));
//	componentResized(null); // initial panel size
        mapPanel.updateBuffer(); // is this better?
	show();
    }

    private boolean viewForests=true;
    private boolean viewMedieval=true;
    private boolean viewAncient=true;
    private boolean viewOther=true;

    private void updateZoomMenus() {
	int i = zoomer.getSelectedIndex();
	int n = zoomer.getItemCount();
	zoomIn.setEnabled(i < n-1);
	zoomOut.setEnabled(i > 0);
    }

    private JMenu[] makeMenus() {
	JMenu view = new XMenubar.XMenu("View");
	view.setMnemonic('V');

	// zoom in
	zoomIn = new XMenubar.XMenuItem("Zoom In");
	zoomIn.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    int i = zoomer.getSelectedIndex() + 1;
		    zoomer.setSelectedIndex(i);
		    updateZoomMenus();
                    // breakage.  better:
                    // figure out what's closest to the current zoom.
                    // increment that.
                    // ok, the popup needs an "other..." option.
		}
	    });
	view.add(zoomIn);

	// zoom out
	zoomOut = new XMenubar.XMenuItem("Zoom Out");
	zoomOut.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    int i = zoomer.getSelectedIndex() - 1;
		    zoomer.setSelectedIndex(i);
		    updateZoomMenus();
		}
	    });
	view.add(zoomOut);

	// zoom to > ...

	/*
	// ---
	view.addSeparator();

	// [x] forest sites
	JCheckBoxMenuItem ancient = new XMenubar.XCheckBoxMenuItem("Forest Sites");
	ancient.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    viewAncient = !viewAncient;
		    repaint();
		}
	    });
	view.add(ancient);
	*/

	return new JMenu[] { view };
    }
}
