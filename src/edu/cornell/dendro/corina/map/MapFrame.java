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

package edu.cornell.dendro.corina.map;

import java.awt.Adjustable;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.cornell.dendro.corina.Build;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gui.Layout;
import edu.cornell.dendro.corina.gui.PrintableDocument;
import edu.cornell.dendro.corina.gui.XFrame;
import edu.cornell.dendro.corina.gui.menus.EditMenu;
import edu.cornell.dendro.corina.gui.menus.FileMenu;
import edu.cornell.dendro.corina.gui.menus.HelpMenu;
import edu.cornell.dendro.corina.gui.menus.WindowMenu;
import edu.cornell.dendro.corina.map.tools.ToolBox;
import edu.cornell.dendro.corina.site.Location;
import edu.cornell.dendro.corina.site.Site;
import edu.cornell.dendro.corina.site.SiteDB;
import edu.cornell.dendro.corina.site.SiteInfoDialog;
import edu.cornell.dendro.corina.site.SitePrinter;
import edu.cornell.dendro.corina.ui.Builder;

public class MapFrame extends XFrame implements PrintableDocument, ComponentListener {

    private MapPanel mapPanel;

    private JTabbedPane tabs;

    private LabelSet labels;
    
    // printing
    public Object getPrinter(PageFormat pf) {
        int tab = tabs.getSelectedIndex();

        if (tab == 0)
            return new MapPrinter(mapPanel.getView(), labels, pf);
        else // tab == 1
            return new SitePrinter(SiteDB.getSiteDB().sites); // print them all?
    }
    public String getPrintTitle() {
        return "Map"; // TODO: add location/sites/etc.
    }

    public MapFrame() {
    	// this seems downright silly ... we'll have no silliness here!
   		this(SiteDB.getSiteDB());
    }

    public MapFrame(Site s1, Site s2) {
        super();
        
        // set mappanel
        List tmp = new ArrayList();
        tmp.add(s1);
        tmp.add(s2);
        labels = new LabelSet(tmp); // this is the master copy!
        
        init();
        
        mapPanel.setSites(tmp);
        
        // center between them
        Location mid = Location.midpoint(s1.getLocation(), s2.getLocation());
        mapPanel.getView().center = mid;
        
    }

    private MapFrame(SiteDB db) {
        super();
        
        labels = new LabelSet(); // this is the master copy!
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
    public void componentResized(ComponentEvent e) {
	// tell the panel how big he's going to be now
	mapPanel.setWidth(mapPanel.getWidth());
	mapPanel.setHeight(mapPanel.getHeight());

	// try just updating it:
	mapPanel.updateBuffer();
	// -- seems to work fine.  (same read errors as before, but they're
	// MapFile's fault.)
    }
    public void componentHidden(ComponentEvent e) { }
    public void componentMoved(ComponentEvent e) { }
    public void componentShown(ComponentEvent e) { }
    // timer is private, hence i need to implement
    // componentlistener myself, so i need to
    // have a bunch of empty method bodies.  ack.
    // EXTRACT: if i really need this (do i?), make a WindowListener(?)

    public void init() {
        setTitle("Atlas - " + Build.VERSION + " " + Build.TIMESTAMP);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // add mappanel
        mapPanel = new MapPanel(this, labels);

        // toolbox
        ToolBox tools = new ToolBox(mapPanel.getView(), mapPanel, this);

        // scrollbars -- need more complex layout for this stuff now
        JScrollBar sb1 = new JScrollBar(Adjustable.HORIZONTAL,
                                        (int) mapPanel.getView().center.getLongitudeAsDegrees(),
                                        10, // changes -- augh!
                                        -180, 180);
        sb1.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
		// FIXME: if scrollbar didn't change, don't update
                mapPanel.getView().center.setLongitudeAsDegrees(e.getValue());
                mapPanel.updateBuffer();
                mapPanel.repaint();
            }
        });
        JScrollBar sb2 = new JScrollBar(Adjustable.VERTICAL,
                                        - (int) mapPanel.getView().center.getLatitudeAsDegrees(),
                                        10, // changes -- augh!
                                        -90, 90);
        sb2.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
		// FIXME: if scrollbar didn't change, don't update

		// (+90 is up, -90 is down, so negate 'em)
                mapPanel.getView().center.setLatitudeAsDegrees(-e.getValue());
                mapPanel.updateBuffer();
                mapPanel.repaint();
            }
        });

	// TO ZOOM:
	// -- set mapPanel.getView().zoom to new value
	// -- call mapPanel.updateBuffer()
	// -- call MapPanel.repaint()

	int w = sb2.getPreferredSize().width; // width of a vert scrollbar
	JPanel sb1Container = Layout.borderLayout(null,
						  null, sb1, Box.createHorizontalStrut(w),
						  null);

	JPanel inner = Layout.borderLayout(null,
					   null, mapPanel, sb2,
					   sb1Container);

	
	JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	{
            slider = new JSlider((int) (MINIMUM_ZOOM * SLIDER_DETAIL),
                                 (int) (MAXIMUM_ZOOM * SLIDER_DETAIL),
                                 (int) (1.5 * SLIDER_DETAIL));
	    // TODO: change slider when zoom changes
	    slider.addChangeListener(new ChangeListener() {
		    public void stateChanged(ChangeEvent e) {
			// no live-zooming, yet -- we're too slow.
			if (!settingSlider) {
			    float oldZoom = mapPanel.getView().getZoom();
			    float newZoom = slider.getValue() / SLIDER_DETAIL;
			    if (oldZoom == newZoom) // otherwise, this gets called on mouse-up(!)
				return;

			    // set zoom on panel
			    mapPanel.getView().setZoom(newZoom);
			    mapPanel.updateBuffer();
			    // mapPanel.repaint();
			}
		    }
		});
	    JLabel large = new JLabel(Builder.getIcon("mountains-large.png"));
	    JLabel small = new JLabel(Builder.getIcon("mountains-small.png"));

	    // allow clicking on these!
	    large.addMouseListener(new MouseAdapter() {
		    @Override
			public void mouseClicked(MouseEvent e) {
			slider.setValue(slider.getValue() + 100);
			mapPanel.updateBuffer();
		    }
		});
	    small.addMouseListener(new MouseAdapter() {
		    @Override
			public void mouseClicked(MouseEvent e) {
			slider.setValue(slider.getValue() - 100);
			mapPanel.updateBuffer();
		    }
		});

            bottom.add(new JLabel("Zoom:"));
	    bottom.add(small);
	    bottom.add(slider);
	    bottom.add(large);
	    if (App.platform.isMac())
		bottom.add(Box.createHorizontalStrut(16));
	}

JPanel content = Layout.borderLayout(null,
                                     tools, inner, null,
                                     bottom);
//setContentPane(content);

// --
	sitesPanel = new SiteListPanel(mapPanel, labels);

tabs = new JTabbedPane();
tabs.addTab("Map", content);
tabs.addTab("Sites", sitesPanel);

setContentPane(tabs);
// --

        // watch for resize
        addComponentListener(this);

        // add menubar
        JMenuBar menubar = new JMenuBar(); // FUTURE: CorinaMenuBar!
        menubar.add(new MapFileMenu(this));
        menubar.add(new MapEditMenu());
        menubar.add(new MapViewMenu());
        if (App.platform.isMac())
            menubar.add(new WindowMenu(this));
        menubar.add(new HelpMenu());
        setJMenuBar(menubar);

        // set size, and show it
        pack();
        setSize(new Dimension(700, 500));
        show();
    }

SiteListPanel sitesPanel;
    
private class MapFileMenu extends FileMenu {
        // TODO: fix printing; it should work, but it's broken

        MapFileMenu(MapFrame f) {
	    super(f);
	}

        // see how easy this is?
        @Override
		public void addCloseSaveMenus() {
            super.addCloseSaveMenus();

            addExportPNGMenu();
	    /* -- DISABLED, until i figure out SVG and how to include batik (merge jar or optional download)
            addExportPNGMenu();
            addExportSVGMenu();
	    */
        }

        private void addExportPNGMenu() {
            // TODO: use ui.builder?  i18n?
            JMenuItem png = new JMenuItem("Export as PNG...");
            add(png);
            png.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    Snapshot.exportPNG(mapPanel.getView(), labels, f);
                }
            });
        }
        
        private void addExportSVGMenu() {
            // TODO: use ui.builder?  i18n?
            JMenuItem svg = new JMenuItem("Export as SVG...");
            add(svg);
            svg.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    Snapshot.exportSVG(mapPanel.getView(), labels);
                }
            });
        }
    }

    private class MapEditMenu extends EditMenu {
        // TODO: implement undo/redo
        // TODO: implement select-all
        // TODO: dim get-info if nothing is selected -- (extend LabelSet to do selection first)

        @Override
		protected void init() {
            addUndoRedo();
            addSeparator();

            addClipboard();
            addSeparator();

            addSelectAll();

            addSeparator();
            addGetInfo();
            
            addPreferences();
        }

        private void addGetInfo() {
            JMenuItem getInfo = Builder.makeMenuItem("get_info...");
            getInfo.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    Iterator iter = labels.getSelectedSites();

                    if (iter.hasNext()) {
                        Site site = (Site) iter.next();
                        new SiteInfoDialog(site, null);
                    } else {
                        // nothing selected => do nothing.
                        // BETTER: this menuitem shouldn't even be enabled.
                    }
                }
            });
            add(getInfo);
        }
    }

    private class MapViewMenu extends JMenu {
        // TODO: implement rect/spher switching
        public MapViewMenu() {
            super("View");

            JMenuItem show = new JMenuItem("Show All Sites");
            JMenuItem hide = new JMenuItem("Hide All Sites");

            show.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    labels.showAllSites();
                    mapPanel.updateBufferLabelsOnly();
                    sitesPanel.repaint();
                }
            });

            // TODO: use same action for both?

            hide.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    labels.hideAllSites();
                    mapPanel.updateBufferLabelsOnly();
                    sitesPanel.repaint();
                }
            });

            add(show);
            add(hide);

            addSeparator();

            JRadioButtonMenuItem rect = new JRadioButtonMenuItem("Rectangular Projection", true);
            JRadioButtonMenuItem spher = new JRadioButtonMenuItem("Spherical Projection", false);
            spher.setEnabled(false); // TEMP!
            
            add(rect);
            add(spher);

            // mutex them
            ButtonGroup group = new ButtonGroup();
            group.add(rect);
            group.add(spher);

            // mark as target
            JMenuItem mark = Builder.makeMenuItem("mark_as_target");
            mark.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    sitesPanel.markSelectionAsTarget();
                }
            });
            
            addSeparator();
            add(mark);

            // which columns to view?
            addSeparator();

            for (int i=0; i<SiteListPanel.FIELD_NAMES.length; i++) {
                JCheckBoxMenuItem m = new JCheckBoxMenuItem(SiteListPanel.FIELD_NAMES[i]);
                if (i < 2) {
                    m.setEnabled(false);
                    m.setSelected(true);
                }
                // TODO: call setSelected() based on fields
                add(m);
            }
            // TODO: hook these up! (action listener)

        }
    }

    private JSlider slider;
    private boolean settingSlider=false;

    // if a tool changes the zoom, it should call this!
    public void setZoom() {
	// update the slider
	settingSlider = true; // don't let the slider event listener hear about this!
	slider.setValue((int) (mapPanel.getView().getZoom() * SLIDER_DETAIL));
	settingSlider = false;
	slider.repaint();
    }

    private static final float SLIDER_DETAIL = 100;

    // why aren't these in View?  because the zoom range of 0.5 to 25 is purely for the user,
    // as part of the window controls.  for me (the program), sometimes i'll want to set the
    // zoom out of this range.  for example, getting extra detail on a high-res map.
    private static final float MINIMUM_ZOOM = 0.5f;
    private static final float MAXIMUM_ZOOM = 25;

    // WRITEME: menus (under View menu?) to change coloring scheme: by country, species, type?
}
