package corina.eyes;

import java.util.List;

import java.awt.Point;

public class Path {
    // for now, just 2 (public) points
    public Point a, b;

    public Path() {
	a = new Point(0, 100);
	b = new Point(100, 0);
    }

    /*
    private List points;

    // c'tor?
    // default 2 points?  require?
    // - how to add points?
    // - how to extract points?  (return new array)

    // ...

    // travel |d| along the path, and end up at a point
    Point pointAfterDistance(double distance) {
	// ...
	return null;
    }

    // length of path
    double length() {
	// WRITEME
	return 0.0;
    }

    // draw me
    void draw() {
	// WRITEME
    }

    // angle the path is pointing at dist d
    double angle(double d) {
	// WRITEME
	return 0.0;
    }

    // do i need sampling methods here, too?
    */
}
