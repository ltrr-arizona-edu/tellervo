package corina.map;

import java.awt.Dimension;

// holds the current view of the map: location (lat/long of center), zoom factor, and target size (pixels x pixels)
public class View implements Cloneable {

    Location center = new Location(30, 38); // the aegean

    double zoom=1; // is this a bad abstraction?  well, it's easy to implement

    Dimension size = new Dimension(640, 640);

    public Object clone() {
        View v2 = new View();
        v2.center = (Location) center.clone();
        v2.zoom = zoom;
        v2.size = (Dimension) size.clone();
        return v2;
    }
}
