package corina.map;

import java.awt.Point;

public class RectangularRenderer extends Renderer {
    public RectangularRenderer(View v) {
        this.view = v;
    }

    private static final int EXTRA_ZOOM = 16;

    public Vector3 project(Location loc) {
        Vector3 v = new Vector3();
        v.x = view.size.width/2 + (loc.longitude - view.center.longitude)*view.zoom*EXTRA_ZOOM;
        v.y = view.size.height/2 - (loc.latitude - view.center.latitude)*view.zoom*EXTRA_ZOOM;
        v.z = +1; // it's a flat map, everything's theoretically visible. -- THIS IS ANOTHER REASON WHY VECTOR3 IS WRONG
        return v;
    }

    // just the opposite of project()
    public Location unrender(Point p) {
        Location l = new Location();
        l.longitude = view.center.longitude + (p.x - view.size.width/2)/(view.zoom*EXTRA_ZOOM);
        l.latitude = view.center.latitude + (p.y - view.size.height/2)/(-view.zoom*EXTRA_ZOOM);
        return l;
    }
}
