package corina.map;

import java.awt.Point;

// a renderer interface for points on a map.
public abstract class Renderer {
    // factory -- TODO: let me pick which renderer at runtime!
    public static Renderer createRenderer(View view) {
        return new RectangularRenderer(view);
    }
    // PERF: rectrend is really fast, but sphericalrenderer needs to
    // do some matrix multiplications; caching renderers (by their
    // views) would definitely be adventageous, then -- as
    // getRenderer() perhaps, to indicate that it's not really
    // creating a new one every time.

    public abstract void render(Location loc, Vector3 pt); // project loc, store result in pt
    public abstract void unrender(Point pt, Location loc); // unproject pt, store result in loc

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

    protected View view;

    // nonzero iff the current segment is visible.
    public static final int VISIBLE_NO = 0; // out of view
    public static final int VISIBLE_YES = 1; // in view
    public static final int VISIBLE_POINT = 2; // in view but only 1 pixel

    // compute the number of pixels |km| is at |p|, for drawing a scale/legend
    private Location leftLoc = new Location(), rightLoc = new Location();
    private Vector3 rightVec = new Vector3();
    public int pixelsForDistanceAtPoint(Point p, float km) {
        unrender(p, leftLoc);
        double radius = 40000. * Math.cos(Math.toRadians(leftLoc.latitude)); // radius of circle at latitude
        double deg = (km / radius) * 360.; // deg for |km|
        deg = Math.abs(deg); // (just in case)
        // rightLoc = new Location(leftLoc.latitude, leftLoc.longitude + (float) deg); // PERF: new!
	rightLoc.latitude = leftLoc.latitude;
	rightLoc.longitude = leftLoc.longitude + (float) deg;
        render(rightLoc, rightVec);
        return (int) (rightVec.x - p.x); }
}
