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

import corina.map.tools.ToolBox;
import corina.site.Site;
import corina.site.SiteDB;
import corina.gui.XFrame;
import corina.gui.PrintableDocument;
import corina.gui.XMenubar;
import corina.ui.Builder;
import corina.util.Platform;

import java.text.DecimalFormat;
import java.text.ParseException;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
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
import javax.swing.JSlider;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.Box;
import javax.swing.AbstractAction;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;

public class MapFrame extends XFrame implements PrintableDocument, ComponentListener {

    /* private */ MapPanel mapPanel;
    private JMenuItem byCountry, bySpecies, byEpoch; // coloring scheme.

    public int getPrintingMethod() {
        return PrintableDocument.PRINTABLE;
    }
    public Pageable makePageable(PageFormat pf) {
        return null;
    }
    public Printable makePrintable(PageFormat pf) {
            mapPanel.setPageFormat(pf);
            return mapPanel;
    }
    public String getPrintTitle() {
        return "Map"; // add location/sites/etc.
    }

    public MapFrame() {
        this(SiteDB.getSiteDB()); // this seems downright silly ... we'll have no silliness here!

        // TESTING: site list sidebar
//        SiteList sl = new SiteList();
//        sl.setMapFrame(this);
//        getContentPane().add(sl, BorderLayout.WEST); // use a slide-border?
    }

    public MapFrame(Site s1, Site s2) {
        super();
        init();
        
        // set mappanel
        List tmp = new ArrayList();
        tmp.add(s1);
        tmp.add(s2);
        mapPanel.setSites(tmp);
        
        // TESTING: site list sidebar
        SiteList sl = new SiteList();
        sl.setMapFrame(this);
        getContentPane().add(sl, BorderLayout.WEST); // use a slide-border?

        // center between them
        mapPanel.getView().center = Location.midpoint(s1.getLocation(), s2.getLocation());
    }

    // add |site| to s2, if it's not there; then repaints map
    public void show(Site site) {
        mapPanel.show(site);
    }
    // remove |site| from s2, if it's there; then repaints map
    public void hide(Site site) {
        mapPanel.hide(site);
    }

    private MapFrame(SiteDB db) {
        super();
        init();

        // set mappanel.sites
        mapPanel.setSites(db.sites);

        // center?  well, don't bother for now.
        // TODO: center on the average site in the map.  (what's "average" on a circular loop?)
        // TODO: zoom out to show all sites (?) -- seems harder
        // TODO: no, ignore those ideas.  better: center on the last place the user was looking
        // mapPanel.getRenderer().setLocation((Location) s1.getLocation().clone());
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

    public void init() {
        setTitle("Map");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // add mappanel
        mapPanel = new MapPanel(this);
        // mapPanel.updateBuffer(); // it's "resized" when the frame first appears
        // getContentPane().add(mapPanel, BorderLayout.CENTER);

        // toolbox
        ToolBox tools = new ToolBox(mapPanel.getView(), mapPanel, this);
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

	// TO ZOOM:
	// -- set mapPanel.getView().zoom to new value
	// -- call mapPanel.updateBuffer()
	// -- call MapPanel.repaint()

	JPanel sb1Container = new JPanel(new BorderLayout());
	sb1Container.add(sb1, BorderLayout.CENTER);
	sb1Container.add(Box.createHorizontalStrut(sb2.getPreferredSize().width), BorderLayout.EAST);

	JPanel inner = new JPanel(new BorderLayout());
	inner.add(sb1Container, BorderLayout.SOUTH);
	inner.add(sb2, BorderLayout.EAST);
	inner.add(mapPanel, BorderLayout.CENTER);

	JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	{
	    slider = new JSlider(50, 1600, 100);
	    // TODO: change slider when zoom changes
	    slider.addChangeListener(new ChangeListener() {
		    public void stateChanged(ChangeEvent e) {
			// no live-zooming, yet -- we're too slow.
			if (!settingSlider && !slider.getValueIsAdjusting()) {
			    // set zoom on panel
			    mapPanel.getView().zoom = slider.getValue() / 100f;
			    mapPanel.updateBuffer();
			    mapPanel.repaint();
			}
		    }
		});
	    ClassLoader cl = this.getClass().getClassLoader();
	    JLabel large = new JLabel(new ImageIcon(cl.getResource("Images/mountains-large.png")));
	    JLabel small = new JLabel(new ImageIcon(cl.getResource("Images/mountains-small.png")));

	    bottom.add(small);
	    bottom.add(slider);
	    bottom.add(large);
	    if (Platform.isMac)
		bottom.add(Box.createHorizontalStrut(16));
	}

	getContentPane().add(inner, BorderLayout.CENTER);
	getContentPane().add(bottom, BorderLayout.SOUTH);

        // watch for resize
        addComponentListener(this);

        // add menubar
        setJMenuBar(new XMenubar(this, null)); // WAS: makeMenus()));

        // set size, and show it
        pack();
        setSize(new Dimension(640, 480));
        //	componentResized(null); // initial panel size
//        mapPanel.updateBuffer(); // is this better?
        show();
    }

    private JSlider slider;
    private boolean settingSlider=false;

    // if a tool changes the zoom, it should call this!
    public void setZoom() {
	// update the slider
	settingSlider = true; // don't let the slider event listener hear about this!
	slider.setValue((int) (mapPanel.getView().zoom * 100f));
	settingSlider = false;
	slider.repaint();
    }

    private boolean viewForests=true;
    private boolean viewMedieval=true;
    private boolean viewAncient=true;
    private boolean viewOther=true;

    private JMenu[] makeMenus() {
	JMenu view = Builder.makeMenu("view");

	/*
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

	/*
        JMenu color = new XMenubar.XMenu("Color");
        color.setMnemonic('C');

        // color by country
        byCountry = new XMenubar.XRadioButtonMenuItem("by Country");
        byCountry.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // WRITE ME
            }
        });

        // color by species
        bySpecies = new XMenubar.XRadioButtonMenuItem("by Species");
        bySpecies.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // WRITE ME
            }
        });

        // color by epoch
        byEpoch = new XMenubar.XRadioButtonMenuItem("by Epoch");
        byEpoch.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // WRITE ME
            }
        });

        // WRITE ME: button group, select first one, etc.

        color.add(byCountry);
        color.add(bySpecies);
        color.add(byEpoch);
        */
                        
        return new JMenu[] { view, /*color*/ };
    }
}
