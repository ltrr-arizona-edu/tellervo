package corina.map.tools;

import corina.map.View;
import corina.map.Renderer;
import corina.map.MapPanel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import java.awt.Event;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;

// zoom or unzoom on the map.
// features:
// -- cursor looks like magnifying glass (+ or -)
// -- alt-click(?) to unzoom
// -- cursor looks like crosshair for zoom-area?
public class ZoomInTool extends Tool {
    private View v;

    public ZoomInTool(MapPanel p, View v, ToolBox b) {
        super(p, b);
        this.v = v; }

    Icon getIcon() {
        ClassLoader cl = this.getClass().getClassLoader();
        return new ImageIcon(cl.getResource("Images/zoom.png")); }
    Cursor getCursor() {
        ClassLoader cl = this.getClass().getClassLoader();
        ImageIcon icon = new ImageIcon(cl.getResource("Images/zoom-small.png"));
        return Toolkit.getDefaultToolkit().createCustomCursor(icon.getImage(), new Point(0, 0), "Zoomer"); }
    String getTooltip() {
        return "Zoom In Tool"; }
    String getName() {
        return "Zoom In"; }
    Character getKey() {
        return new Character('z'); }
    KeyStroke getFastKey() {
        return KeyStroke.getKeyStroke(new Character(' '), Event.META_MASK); }
    // cmd-space
    // return KeyStroke.getKeyStroke("meta space"); // meta-space on mac, control-space elsewhere

    // keyboard
    public void keyPressed(KeyEvent e) {
        System.out.println("key pressed, zoom-in tool sees it");
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            aborted = true;
            System.out.println("it's escape!");
        } else {
            System.out.println("it's not escape...");
        }
    }
    
    // mouse
    private Renderer r;
    public void mouseClicked(MouseEvent e) {
        // recenter on this point
        r.unrender(e.getPoint(), v.center);

        // zoom in by a factor of 2 (zoom out if alt pressed?)
        v.zoom *= 1.25; // EXTRACT CONSTANT

        // now update the buffer, and redraw
        p.updateBuffer(); // ugly!
        p.repaint(); }

    boolean draw=false;
    boolean aborted = false;
    Point p1, p2;

    public void mousePressed(MouseEvent e) {
        r = Renderer.createRenderer(v);

        // store mouse-down point
        p1 = e.getPoint();

        // not aborted!
        aborted = false; }

    public void mouseDragged(MouseEvent e) {
        if (aborted)
            return;

        // (set cursor to CROSSHAIR?  i'd need a hook into the panel, then, not just a getCursor() method.)

        draw = true;
        p2 = e.getPoint();
        p.repaint(); }

    public void mouseReleased(MouseEvent e) {
        // i wasn't just dragging?  bah.
        if (!draw)
            return;

        // that's probably a click
        if (Math.sqrt((p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y)) < 5) {
            draw = false;
            p.repaint();
            return; }

        // i just finished dragging, so compute new zoom and location

        // location is easy: take the average -- SIMILAR to MOUSECLICKED; REFACTOR
        p2 = e.getPoint();
        Point centerPoint = new Point((p1.x+p2.x)/2, (p1.y+p2.y)/2); // (one new() on mouse-released is fine)
        r.unrender(centerPoint, v.center); // REFACTOR: Renderer.centerOn(Point) -- LoD

        // well, zoom isn't too hard, either.  zoom *= panelsize / boxsize
        // (show the user more than she wants: use the smaller ratio)
        float xratio = (float) v.size.width / Math.abs(p1.x - p2.x);
        float yratio = (float) v.size.height / Math.abs(p1.y - p2.y);
        float ratio = Math.min(xratio, yratio);
        float newZoom = v.zoom * ratio;
        v.zoom = (float) Math.max(0.10, Math.min(newZoom, 16.00));
//        System.out.println("zoom=" + v.zoom);
        p.setZoom();

        // BUG: this doesn't change the popup menu (placard)

        // update buffer once for everything, and redraw
        p.updateBuffer(); // ugly!
        draw = false;
	p.repaint(); }

    public void decorate(Graphics g) {
        if (aborted)
            return;

        if (draw) {
            Graphics2D g2 = (Graphics2D) g;

            // below here, it depends which is where, so we'll make new points
            // nw = the northwest (top-left) corner, se = the southeast corner
            // (recycle points to save memory de/allocations)
            nw.x = Math.min(p1.x, p2.x);
            nw.y = Math.min(p1.y, p2.y);
            se.x = Math.max(p1.x, p2.x);
            se.y = Math.max(p1.y, p2.y);

            // draw box
            g2.setColor(Color.white);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRect(nw.x, nw.y, se.x-nw.x, se.y-nw.y);
            g2.setColor(Color.black);
            g2.setStroke(dottedStroke); // new stroke with new phase (0..15) here, if you want marching ants
            g2.drawRect(nw.x, nw.y, se.x-nw.x, se.y-nw.y); } }

        private Point nw = new Point();
        private Point se = new Point();
        private BasicStroke dottedStroke = new BasicStroke(1f,
                                                           BasicStroke.CAP_BUTT,
                                                           BasicStroke.JOIN_BEVEL,
                                                           10f, new float[] { 8f }, 0f); }
