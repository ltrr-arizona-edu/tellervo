package corina.map;

import java.awt.Dimension;

// holds the current view of the map: location (lat/long of center), zoom factor, and target size (pixels x pixels)
public class View implements Cloneable {
    public Location center = new Location(38f, 30f); // the aegean; n.b.: floats!

    public float zoom=1; // is this a bad abstraction?  well, it's easy to implement

    public Dimension size = new Dimension(640, 640);

    // REFACTOR: why do i need clone?  wouldn't a copy-constructor be simpler?
    public Object clone() {
        View v2 = new View();
        v2.center = (Location) center.clone();
        v2.zoom = zoom;
        v2.size = (Dimension) size.clone();
        return v2;
    }
}
