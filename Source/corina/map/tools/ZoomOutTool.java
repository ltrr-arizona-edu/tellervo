package corina.map.tools;

import corina.map.View;
import corina.map.Renderer;
import corina.map.MapPanel;

import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.Point;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import java.awt.event.MouseEvent;

public class ZoomOutTool extends Tool {
    private View v;

    public ZoomOutTool(MapPanel p, View v, ToolBox b) {
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
        return "Zoom Out Tool"; }
    String getName() {
        return "Zoom Out"; }
    Character getKey() {
        return null; }
    KeyStroke getFastKey() {
        return null; } // KeyStroke.getKeyStroke(new Character(' '), Event.META_MASK); // cmd-space
                         // return KeyStroke.getKeyStroke("meta space"); // meta-space on mac, control-space elsewhere

    public void mouseClicked(MouseEvent e) {
        // recenter on this point
        Renderer r = Renderer.createRenderer(v);
        r.unrender(e.getPoint(), v.center);

        // zoom out by a factor of 2
        float newZoom = v.zoom / 1.25f;
        v.zoom = (float) Math.max(0.10, Math.min(newZoom, 16.00));
//         System.out.println("zoom=" + v.zoom);
        p.setZoom();
        
        // now update the buffer, and redraw
	p.updateBuffer(); // ugly!
	p.repaint(); } }
