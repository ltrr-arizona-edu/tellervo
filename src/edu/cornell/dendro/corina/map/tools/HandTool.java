package edu.cornell.dendro.corina.map.tools;

import edu.cornell.dendro.corina.map.Point3D;
import edu.cornell.dendro.corina.map.View;
import edu.cornell.dendro.corina.map.Projection;
import edu.cornell.dendro.corina.map.MapPanel;
import edu.cornell.dendro.corina.site.Location;
import edu.cornell.dendro.corina.site.Site;
import edu.cornell.dendro.corina.site.SiteNotFoundException;
import edu.cornell.dendro.corina.util.Angle;
import edu.cornell.dendro.corina.ui.Builder;

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
        this.v = v;
    }

    Icon getIcon() {
	return Builder.getIcon("hand.png");
    }
    Cursor getCursor() {
        return new Cursor(Cursor.HAND_CURSOR);
    }
    String getTooltip() {
        return "Hand Tool";
    }
    String getName() {
        return "Scroll";
    }
    Character getKey() {
        return new Character('h');
    }
    KeyStroke getFastKey() {
        return KeyStroke.getKeyStroke(new Character(' '), 0);  // space
    }

    // mouse
    Location l1 = new Location(), l2 = new Location();
    Projection r;
    boolean popup = false;
    boolean drag = false; // was it dragged at all?

    public void mousePressed(MouseEvent e) {
        // (popup)
        if (maybeShowPopup(e, v)) {
            popup = true;
            return;
	} else {
            popup = false;
	}

        // record where this was
        r = Projection.makeProjection(v);
        r.unproject(e.getPoint(), l1);
        drag = false;
    }

    private Location diff = new Location();

    public void mouseDragged(MouseEvent e) {
        // BUG: this doesn't let you right-click-and-drag to get the popup on win32,
        // because the win32 popup trigger is on mouse-released.  (how to deal with that?)
        if (popup)
            return;

        drag = true;

        // record this place
        r.unproject(e.getPoint(), l2);

        // make a location.diff(location) (-- LoD) (pass it minuend, subtrahend, difference, so no allocs needed)
        diff.setLatitudeAsSeconds(l2.getLatitudeAsSeconds() - l1.getLatitudeAsSeconds());
        diff.setLongitudeAsSeconds(l2.getLongitudeAsSeconds() - l1.getLongitudeAsSeconds());

        v.center.setLatitudeAsSeconds(v.center.getLatitudeAsSeconds() - diff.getLatitudeAsSeconds());
        v.center.setLongitudeAsSeconds(v.center.getLongitudeAsSeconds() - diff.getLongitudeAsSeconds());

        // reset l1 now -- only for live-update dragging, not gridline-dragging -- HUH?
        // l1 = l2;

        // now update the buffer, and redraw
	p.updateBuffer(); // eventually, this won't be needed, and repaint() will do everything, quickly
        p.repaint();
    }

    public void mouseReleased(MouseEvent e) {
        if (popup)
            return;

        // (popup)
        if (maybeShowPopup(e, v)) {
            popup = true;
            return;
	} else {
            popup = false;
	}

        // just a click?  don't bother redrawing, then.
        if (!drag)
            return;

	// WHY was i redrawing on mouse-up?  dumb...
	// p.updateBuffer();
	// p.repaint();
    }

    public void decorate(Graphics g) { } // no decorations
}
