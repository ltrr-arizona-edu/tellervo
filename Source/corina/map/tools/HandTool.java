package corina.map.tools;

import corina.map.Location;
import corina.map.Vector3;
import corina.map.View;
import corina.map.Renderer;
import corina.map.MapPanel;
import corina.site.Site;
import corina.site.SiteInfo;
import corina.site.SiteNotFoundException;
import corina.util.Angle;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import java.awt.event.MouseEvent;

// scroll around by dragging.
// features:
// -- cursor looks like a hand
// -- press, drag to scroll.
public class HandTool extends Tool {
    private View v;

    public HandTool(MapPanel p, View v, ToolBox b) {
        super(p, b);
        this.v = v; }

    Icon getIcon() {
        ClassLoader cl = this.getClass().getClassLoader();
        return new ImageIcon(cl.getResource("Images/hand.png")); }
    Cursor getCursor() {
        return new Cursor(Cursor.HAND_CURSOR); }
    String getTooltip() {
        return "Hand Tool"; }
    String getName() {
        return "Scroll"; }
    Character getKey() {
        return new Character('h'); }
    KeyStroke getFastKey() {
        return KeyStroke.getKeyStroke(new Character(' '), 0); } // space

    // mouse
    Location l1 = new Location(), l2 = new Location();
    Renderer r;
    boolean popup = false;
    boolean drag = false; // was it dragged at all?

    public void mousePressed(MouseEvent e) {
        // (popup)
        if (maybeShowPopup(e, v)) {
            popup = true;
            return; }
        else {
            popup = false; }

        // record where this was
        r = Renderer.createRenderer(v);
        r.unrender(e.getPoint(), l1);
        drag = false; }

    private Location diff = new Location();

    public void mouseDragged(MouseEvent e) {
        // BUG: this doesn't let you right-click-and-drag to get the popup on win32,
        // because the win32 popup trigger is on mouse-released.  (how to deal with that?)
        if (popup)
            return;

        drag = true;

        // record this place
        r.unrender(e.getPoint(), l2);

        // make a location.diff(location) (-- LoD) (pass it minuend, subtrahend, difference, so no allocs needed)
        diff.latitude = l2.latitude - l1.latitude;
        diff.longitude = l2.longitude - l1.longitude;

        v.center.latitude -= diff.latitude;
        v.center.longitude -= diff.longitude;

        // reset l1 now -- only for live-update dragging, not gridline-dragging
        // l1 = l2;

        // now update the buffer, and redraw
        // p.updateBufferQuick(); // ugly!
	//            p.updateBuffer(); // slow!
	p.updateBuffer(MapPanel.LAYER_OVERLAY | MapPanel.LAYER_GRIDLINES | /*MapPanel.LAYER_SITES | */MapPanel.LAYER_SCALE);
	/*
	  BUG:
	  -- need to turn off linemap here, or force grid to overwrite linemap (exactly 1 erase() for grid+linemap),
	  or something like that.  i broke it by using updatebuffer(int) like this, though.
	 */
        p.repaint(); }

    public void mouseReleased(MouseEvent e) {
        if (popup)
            return;

        // (popup)
        if (maybeShowPopup(e, v)) {
            popup = true;
            return; }
        else {
            popup = false; }

        // just a click?  don't bother redrawing, then.
        if (!drag)
            return;

        // p.updateBuffer(); // only for gridline-dragging
	// p.updateBufferLinemapOnly(); // hack!  hack!  double-hack!
	p.updateBuffer(MapPanel.LAYER_LINEMAP | MapPanel.LAYER_GRIDLINES | MapPanel.LAYER_SITES); // (because they're both on the same buffer)
	// BUG: this should only update the buffers that weren't updated by mouse-drag events
	// -- which is currently just the line map.  (this updates the labels, too,
	// which costs 273/720ms = almost 40% longer)
	// INTERFACE: i should be able to request any combination of updates, then:
	// p.updateBuffers(OVERLAY | GRIDLINES | SITES | SCALE); above, and
	// p.updateBuffers(LINES); here (or even !whatever-it-was-before)
        p.repaint(); }

    public void decorate(Graphics g) { } } // no decorations
