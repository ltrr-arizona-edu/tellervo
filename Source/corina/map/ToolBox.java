package corina.map;

import corina.gui.JarIcon;
import corina.util.ColorUtils;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.Color;
import java.awt.BasicStroke;
import javax.swing.JToolBar;
import javax.swing.JToggleButton;
import javax.swing.JPanel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

// a box of mapping tools: ruler, hand, zoom, etc.
public class ToolBox extends JToolBar {

    // abstract class tool:
    // -- automatic click-to-select, unselect-all-others
    // -- specify a cursor to use (png)
    // -- on-click, on-drag, ...
    // -- (gets renderer passed to it -- it doesn't need map -- needs panel, though, for cursor, etc.)
    public abstract class Tool implements MouseListener, MouseMotionListener { // extends jtogglebutton?  let's see what else it needs to do first.
                                                                               // (public because MapPanel needs it for its decorators)
        // remember to call repaint() in the mouse listeners if you change, add, or remove decoration
        abstract Icon getIcon();
        abstract Cursor getCursor();
        JToggleButton b;
        JToggleButton getButton() {
            return b;
        }
        Tool() {
            b = new JToggleButton(getIcon());
            b.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    JToggleButton source = (JToggleButton) e.getSource();
                    if (source.isSelected())
                        selected();
                    else
                        source.setSelected(true); // tried to deselect?  don't do that!
                }
            });
        }
        private void selected() {
            // disable all others
            for (int i=0; i<tools.length; i++)
                if (tools[i] != this)
                    tools[i].getButton().setSelected(false);

            // remove old mouse/mousemotion listeners, and decorator
            p.removeMouseListener(oldTool);
            p.removeMouseMotionListener(oldTool);
            ((MapPanel) p).removeDecorator(oldTool);

            // set up new mouse/mousemotion listeners
            p.addMouseListener(this);
            p.addMouseMotionListener(this);
            ((MapPanel) p).addDecorator(this);
            
            // (we'll remove this one next time)
            oldTool = this;

            // set cursor
            p.setCursor(getCursor());
        }
        abstract void decorate(Graphics g);
    }

    // scroll around by dragging.
    // features:
    // -- cursor looks like a hand
    // -- press, drag to scroll.
    private class Hand extends Tool {
        Icon getIcon() {
            return JarIcon.getIcon("Images/hand.png");
        }
        Cursor getCursor() {
            return new Cursor(Cursor.HAND_CURSOR);
        }

        // mouse
        public void mouseClicked(MouseEvent e) { }

        Location l1, l2;
        Renderer r;
        public void mousePressed(MouseEvent e) {
            // record where this was
            r = Renderer.makeRenderer(view);
            l1 = r.unrender(e.getPoint());
        }
        public void mouseDragged(MouseEvent e) {
            // record this place
            l2 = r.unrender(e.getPoint());

            Location diff = new Location(); // make a location.diff(location) (-- LoD)
            diff.latitude = l2.latitude - l1.latitude;
            diff.longitude = l2.longitude - l1.longitude;

            view.center.latitude -= diff.latitude;
            view.center.longitude -= diff.longitude;

            // reset l1 now -- only for live-update dragging, not gridline-dragging
            // l1 = l2;

            // now update the buffer, and redraw
            ((MapPanel) p).updateBufferGridlinesOnly(); // ugly!
            p.repaint();
        }
        public void mouseReleased(MouseEvent e) {
            ((MapPanel) p).updateBuffer(); // only for gridline-dragging
            p.repaint();
        }

        public void mouseEntered(MouseEvent e) { }
        public void mouseMoved(MouseEvent e) { }
        public void mouseExited(MouseEvent e) { }

        void decorate(Graphics g) { } // no decorations
    }
    
    // measure the distance between 2 points on the map, of course.
    // features:
    // -- snap-to-sites?
    // -- shift = allow latitude XOR longitude diffs only
    // -- auto-scroll
    private class Ruler extends Tool {
        Icon getIcon() {
            return JarIcon.getIcon("Images/ruler.png");
        }
        Cursor getCursor() {
            return Toolkit.getDefaultToolkit().createCustomCursor(((ImageIcon) JarIcon.getIcon("Images/ruler-pointer.png")).getImage(),
                                                                  new Point(0, 0), "Ruler");
        }

        // mouse
        public void mouseClicked(MouseEvent e) { }

        boolean draw=false;
        int dist;
        Point pointA, pointB;
        Location locA, locB;
        Renderer r;
        public void mousePressed(MouseEvent e) {
            r = Renderer.makeRenderer(view);
            pointA = e.getPoint();
            locA = r.unrender(pointA);
        }
        public void mouseDragged(MouseEvent e) {
            pointB = e.getPoint();
            locB = r.unrender(pointB);

            // compute distance.  too friggin' easy, just the way i likes it!
            dist = locA.distanceTo(locB);

            // tell myself how to draw this in decorate()
            draw = true;

            // force a redraw, otherwise, how would the panel know?
            p.repaint();
        }
        public void mouseReleased(MouseEvent e) {
            // stop drawing line
            draw = false;

            // again, force redraw
            p.repaint();
        }

        public void mouseEntered(MouseEvent e) { }
        public void mouseMoved(MouseEvent e) { }        
        public void mouseExited(MouseEvent e) { }

        // GENERIC.  see also (poorer) implementations in eyes.*
        private double angle(Point a, Point b) {
            double theta;
            double dx = b.x - a.x;
            double dy = b.y - a.y;
            if (dx == 0)
                theta = Math.PI * (b.y<a.y ? 3/2. : 1/2.); // force doubles!
            else
                theta = Math.atan(dy/dx);
            if (b.x < a.x)
                theta += Math.PI;
            return theta;
        }
        // GENERIC
        private void arrow(Graphics2D g2, Point head, Point tail, double angle) {
            double theta = angle(tail, head);
            GeneralPath arrow = new GeneralPath();
            arrow.moveTo((float) (head.x + 10*Math.cos(theta+angle)),
                         (float) (head.y + 10*Math.sin(theta+angle)));
            arrow.lineTo((float) head.x, (float) head.y);
            arrow.lineTo((float) (head.x + 10*Math.cos(theta-angle)),
                         (float) (head.y + 10*Math.sin(theta-angle)));
            g2.draw(arrow);
        }
                           
        void decorate(Graphics g) {
            if (draw) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.red);

                // draw a line between the two points
                g2.drawLine(pointA.x, pointA.y, pointB.x, pointB.y);

                // draw flat line, arrow on both ends?
                arrow(g2, pointA, pointB, Math.PI/2);
                arrow(g2, pointB, pointA, Math.PI/2);
                arrow(g2, pointA, pointB, Math.PI*3/4);
                arrow(g2, pointB, pointA, Math.PI*3/4);

                // construct string, measure it
                String str = dist + " km";
                int dx = g2.getFontMetrics().stringWidth(str);
                int dy = g2.getFontMetrics().getHeight();
                int x = (pointA.x+pointB.x)/2 - dx/2;
                int y = (pointA.y+pointB.y)/2 + dy/2;

                // draw (partially-transparent) box
                g2.setColor(new Color(255, 255, 255, 200));
                g2.fillRect(x, y-dy, dx, dy);
                g2.setColor(Color.black);
                g2.drawRect(x-1, y-dy-1, dx+1, dy+1);

                // draw string
                g2.setColor(Color.black);
                g2.drawString(str, x, y-3);
            }
        }
    }

    // zoom or unzoom on the map.
    // features:
    // -- cursor looks like magnifying glass (+ or -)
    // -- alt-click(?) to unzoom
    // -- cursor looks like crosshair for zoom-area?
    private class Zoom extends Tool {
        Icon getIcon() {
            return JarIcon.getIcon("Images/zoom.png");
        }
        Cursor getCursor() {
            return Toolkit.getDefaultToolkit().createCustomCursor(((ImageIcon) JarIcon.getIcon("Images/zoom-small.png")).getImage(),
                                              new Point(0, 0), "Zoomer");
            // can't respond to keyboard events?  (Well, why not?  tool can implements keyboard listener, or some such, too)
        }

        // mouse
        Renderer r;
        public void mouseClicked(MouseEvent e) {
            // recenter on this point
            view.center = r.unrender(e.getPoint());

            // zoom in by a factor of 2 (zoom out if alt pressed?)
            view.zoom *= 2;

            // now update the buffer, and redraw
            ((MapPanel) p).updateBuffer(); // ugly!
            p.repaint();
        }

        boolean draw=false;
        Point p1, p2;
        public void mousePressed(MouseEvent e) {
            r = Renderer.makeRenderer(view);

            // WRITE ME: store mouse-down point
            p1 = e.getPoint();
        }
        public void mouseDragged(MouseEvent e) {
            draw = true;
            p2 = e.getPoint();
            p.repaint();
            
            // (set cursor to CROSSHAIR?  i'd need a hook into the panel, then, not just a getCursor() method.)
        }
        public void mouseReleased(MouseEvent e) {
            // i wasn't just dragging?  bah.
            if (!draw)
                return;

            // i just finished dragging, so computer new zoom and location

            // location is easy: take the average -- SIMILAR to MOUSECLICKED; REFACTOR
            p2 = e.getPoint();
            Point center = new Point((p1.x+p2.x)/2, (p1.y+p2.y)/2);
            view.center = r.unrender(center); // REFACTOR: Renderer.centerOn(Point) -- LoD

            // well, zoom isn't too hard, either.  zoom *= panelsize / boxsize
            // (show the user more than she wants: use the smaller ratio)
            double xratio = (double) view.size.width / Math.abs(p1.x - p2.x);
            double yratio = (double) view.size.height / Math.abs(p1.y - p2.y);
            double ratio = Math.min(xratio, yratio);
            view.zoom *= ratio;

            // BUG: this doesn't change the popup menu.
            // which means the popup menu will have to have an "other" entry.
            // (or let you just type it in, like illustrator)
            
            // update buffer once for everything, and redraw
            ((MapPanel) p).updateBuffer(); // ugly!
            draw = false;
            p.repaint();
        }
        public void mouseEntered(MouseEvent e) { }
        public void mouseMoved(MouseEvent e) { }
        public void mouseExited(MouseEvent e) { }

        void decorate(Graphics g) {
            if (draw) {
                Graphics2D g2 = (Graphics2D) g;

                // below here, it depends which is where, so we'll make new points
                // nw = the northwest (top-left) corner, se = the southeast corner
                Point nw = new Point(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y));
                Point se = new Point(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y));
                                
                // draw box
                g2.setColor(Color.black); // transparency in an outline is slow, it seems.
                g2.drawRect(nw.x, nw.y, se.x-nw.x, se.y-nw.y);
                g2.setColor(ColorUtils.addAlpha(Color.gray, 50));
                // darken everything BUT what we're zooming in on
                // here, i dunno what width/height are really, but say w=2000, h=1000
                g2.fillRect(0, 0, 2000, nw.y); // top, all the way across
                g2.fillRect(0, se.y, 2000, 1000-se.y); // bottom, all the way across
                g2.fillRect(0, nw.y, nw.x, se.y-nw.y); // left, between top and bottom
                g2.fillRect(se.x, nw.y, 2000-se.x, se.y-nw.y); // right, between top and bottom

                // set font now?  *shrug*  don't care that much.
                int dx, dy = g2.getFontMetrics().getAscent();
                int EPS = 2;
                
                // first corner: draw position
                String nwString = r.unrender(nw).toString(); // don't compute me here!
                g2.setColor(ColorUtils.addAlpha(Color.yellow, 230));
                dx = g2.getFontMetrics().stringWidth(nwString);
                g2.fillRect(nw.x, nw.y, dx+2*EPS, dy+EPS);
                g2.setColor(Color.black);
                g2.drawString(nwString, nw.x+EPS, nw.y+dy);
                g2.drawRect(nw.x, nw.y, dx+2*EPS, dy+EPS);

                // second corner: draw position
                String seString = r.unrender(se).toString(); // i'm more ok to compute here
                g2.setColor(ColorUtils.addAlpha(Color.yellow, 230));
                dx = g2.getFontMetrics().stringWidth(seString);
                g2.fillRect(se.x-dx-2*EPS, se.y-dy-EPS, dx+2*EPS, dy+EPS);
                g2.setColor(Color.black);
                g2.drawString(seString, se.x-dx-EPS, se.y-EPS);
                g2.drawRect(se.x-dx-2*EPS, se.y-dy-EPS, dx+2*EPS, dy+EPS);
            }
        }
    }

    private Tool tools[];
    private View view;
    private JPanel p;
    private Tool oldTool;

    // get passed: mappanel (for cursors, events, etc.), and renderer (for rendering/unrendering)
    public ToolBox(View v, JPanel p) {
        // keep view, panel
        this.view = v;
        this.p = p;

        tools = new Tool[] {
            new Hand(),
            new Ruler(),
            new Zoom(),
        };

        for (int i=0; i<tools.length; i++)
            add(tools[i].getButton());

        // start out with the hand
        tools[0].getButton().setSelected(true);
        tools[0].selected();
    }
}
