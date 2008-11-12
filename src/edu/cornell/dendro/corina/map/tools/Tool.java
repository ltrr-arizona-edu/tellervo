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
// Copyright 2003 Ken Harris <kbh7@cornell.edu>
//

package edu.cornell.dendro.corina.map.tools;

import edu.cornell.dendro.corina.map.View;
import edu.cornell.dendro.corina.map.Projection;
import edu.cornell.dendro.corina.map.MapPanel;
import edu.cornell.dendro.corina.site.LegacySite;
import edu.cornell.dendro.corina.site.SiteNotFoundException;

import java.util.List;
import java.util.Collections;
import java.util.Comparator;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import javax.swing.KeyStroke;
import javax.swing.JToggleButton;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;

// TODO: document me!

// abstract class tool:
// -- automatic click to select this tool (and unselect all others)
// -- specify a cursor to use (png)
// -- on-click, on-drag, ...
// -- (gets Projection passed to it -- it doesn't need map -- needs panel, though, for cursor, etc.)
public abstract class Tool implements MouseListener, MouseMotionListener, KeyListener {
    // (public because MapPanel needs it for its decorators)
    // remember to call repaint() in the mouse listeners if you change, add, or remove decoration
    abstract Icon getIcon();
    abstract Cursor getCursor();
    abstract String getTooltip();
    abstract String getName();
    abstract Character getKey();
    abstract KeyStroke getFastKey(); // like "space", or "cmd-space" (macize!)
    JToggleButton button;
    JToggleButton getButton() {
        return button;
    }

    //
    // key, mouse, and mouse-moved listeners.  classes can implement
    // any of these they want -- i provide empty bodies here so they can
    // ignore the rest.
    //
    public void keyPressed(KeyEvent e) { } // BUG: these don't work -- focus problems?
    public void keyReleased(KeyEvent e) { }
    public void keyTyped(KeyEvent e) { }
    public void mouseClicked(MouseEvent e) { }
    public void mousePressed(MouseEvent e) { }
    public void mouseDragged(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseMoved(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }

    protected MapPanel p;
    protected ToolBox b;
    Tool(MapPanel p, ToolBox box) {
        this.p = p;
        this.b = box;

        Icon i = getIcon(); // get icon, but scale its image to 24x24 (doesn't maintain aspect ratio!)
                            // ASSUMES icon is an imageicon -- enforce via api!
        i = new ImageIcon(((ImageIcon) i).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
        button = new JToggleButton(getName(), i);
        button.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        // (default text position is horiz=right, vert=center (i think), which is exactly what i want)
        final Tool glue = this;
        button.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JToggleButton source = (JToggleButton) e.getSource();
                if (source.isSelected())
                    b.selected(glue);
                else
                    source.setSelected(true); // tried to deselect?  don't do that!
            }
        });

        // no tooltips -- labels are easier for users -- BUT if users turn off labels, then tooltip=label would be useful!
        //            String tooltip = getTooltip();
        //            if (getKey() != null)
        //                tooltip += " (" + Character.toUpperCase(getKey().charValue()) + ")"; // inelegant
        //            b.setToolTipText(tooltip);
    }

    // this is used by both mousePressed (mac) and mouseReleased (windows);
    // also used by every class that wants to show the site list on right-click.
    protected boolean maybeShowPopup(MouseEvent e, View v) {
        // popup trigger: right-click brings up a list of sites at this location
        if (e.isPopupTrigger()) {
            try {
                // get site, and look up all sites at that location
                Projection r = Projection.makeProjection(v);
                Point pt = e.getPoint();
                LegacySite site = p.siteForPoint(r, pt, 20*((int) v.getZoom())); // what's 20*...?
                List sites = p.sitesForPoint(site);

                // sort by "code: name"
                Collections.sort(sites, new Comparator() {
                    public int compare(Object o1, Object o2) {
                        LegacySite s1 = (LegacySite) o1;
                        LegacySite s2 = (LegacySite) o2;
                        String name1 = s1.getCode() + (s1.getName()==null ? "" : (": " + s1.getName())); // REFACTOR!
                        String name2 = s2.getCode() + (s2.getName()==null ? "" : (": " + s2.getName()));
                        return name1.compareTo(name2);
                    }
                });

                // make a popup menu with all the sites, and show it
                JPopupMenu popup = new JPopupMenu();
                for (int i=0; i<sites.size(); i++) {
                    final LegacySite s = (LegacySite) sites.get(i);
                    JMenuItem menuitem = popup.add(s.getCode() + (s.getName()==null ? "" : (": " + s.getName()))); // "
                    menuitem.addActionListener(new AbstractAction() {
                        public void actionPerformed(ActionEvent e) {
                            p.toFront(s);
                        }
                    });
                    }
                popup.show(p, pt.x, pt.y);
                // (REFACTOR: site.tostring should be "code: name" routine?)
                } catch (SiteNotFoundException snfe) {
                    // ignore
                }
            return true;
            } else {
                return false;
            }
        }

    public void decorate(Graphics g) { }
    }
