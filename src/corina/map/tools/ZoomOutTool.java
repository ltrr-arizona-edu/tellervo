package corina.map.tools;

import corina.map.View;
import corina.map.Projection;
import corina.map.MapPanel;
import corina.ui.Builder;

import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.Point;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import java.awt.event.MouseEvent;

public class ZoomOutTool extends Tool {
    private View v;

    public ZoomOutTool(MapPanel p, View v, ToolBox b) {
        super(p, b);
        this.v = v;
    }

    Icon getIcon() {
	return Builder.getIcon("zoom.png");
    }
    Cursor getCursor() {
	Image image = Builder.getImage("zoom-small.png");
	return Toolkit.getDefaultToolkit().createCustomCursor(image,
							      new Point(0, 0),
							      "Zoomer");
    }
    String getTooltip() {
        return "Zoom Out Tool";
    }
    String getName() {
        return "Zoom Out";
    }
    Character getKey() {
        return null;
    }
    KeyStroke getFastKey() {
        return null;
    } // KeyStroke.getKeyStroke(new Character(' '), Event.META_MASK); // cmd-space
    // return KeyStroke.getKeyStroke("meta space"); // meta-space on mac, control-space elsewhere

    public void mouseClicked(MouseEvent e) {
        // recenter on this point
        Projection r = Projection.makeProjection(v);
        r.unproject(e.getPoint(), v.center);

        // zoom out by a factor of 2
        float newZoom = v.getZoom() / 1.25f;
        v.setZoom((float) Math.max(0.10, Math.min(newZoom, 16.00)));
//         System.out.println("zoom=" + v.getZoom());
        p.setZoom();
        
        // now update the buffer, and redraw
	p.updateBuffer(); // ugly!
	p.repaint();
    }
}
