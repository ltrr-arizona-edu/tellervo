package corina.map;

import java.awt.Point;

// a renderer interface for points on a map.
public abstract class Renderer {
    // factory
    public static Renderer makeRenderer(View view) {
        return new RectangularRenderer(view);
    }

    public abstract Vector3 project(Location loc); // FIXME: returns a Point, not a Vector3, or null if it's not visible (?)
    public abstract Location unrender(Point p);
    public boolean isVisible(Vector3 p1, Vector3 p2) {
        // z-clipping: THIS DOESNT EVEN MAKE SENSE IN RECTANGULAR PROJECTION!
        if (p1.z < 0 || p2.z < 0)
            return false;

        // check for entirely outside viewport -- even this isn't entirely correct...
        if ((p1.x > view.size.width && p2.x > view.size.width) ||
            (p1.y > view.size.height && p2.y > view.size.height) ||
            (p1.x < 0 && p2.x < 0) ||
            (p1.y < 0 && p2.y < 0))
            return false;

        return true;
    }

    public View view;

    // nonzero iff the current segment is visible.
    public static final int VISIBLE_NO = 0; // out of view
    public static final int VISIBLE_YES = 1; // in view
    public static final int VISIBLE_POINT = 2; // in view but only 1 pixel
}
