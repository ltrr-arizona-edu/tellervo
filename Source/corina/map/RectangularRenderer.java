package corina.map;

import java.awt.Point;

public class RectangularRenderer extends Renderer {
    public RectangularRenderer(View v) {
        this.view = v;
    }

    // (normal zoom doesn't look quite the same on spherical and rectangular, so adjust it)
    private static final int EXTRA_ZOOM = 16;

    public void render(Location loc, Vector3 pt) {
        float zoom = view.zoom * EXTRA_ZOOM;

        pt.x = view.size.width/2 + (loc.longitude - view.center.longitude)*zoom;
        pt.y = view.size.height/2 - (loc.latitude - view.center.latitude)*zoom;
        pt.z = +1; // it's a flat map, everything's theoretically visible. -- THIS IS ANOTHER REASON WHY VECTOR3 IS WRONG
    }

    // unproject pt, store result in loc
    public void unrender(Point pt, Location loc) {
        // FIXME: use a set method!
        loc.longitude = view.center.longitude + (pt.x - view.size.width/2)/(view.zoom*EXTRA_ZOOM);
        loc.latitude = view.center.latitude + (pt.y - view.size.height/2)/(-view.zoom*EXTRA_ZOOM);
    }
}
