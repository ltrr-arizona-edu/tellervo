package corina.util;

import java.awt.Point;

// -- see also (poorer) implementations in eyes.*
// -- float is ~7 sigfigs, and sin(.0001) is 1/1000 of a pixel at 1000 pixels out -- that's plenty

public class Angle {
    public static float angle(Point a, Point b) {
        float theta;
        float dx = b.x - a.x;
        float dy = b.y - a.y;
        if (dx == 0)
            theta = (float) Math.PI * (b.y<a.y ? 3/2f : 1/2f); // force floats!
        else
            theta = (float) Math.atan(dy/dx);
        if (b.x < a.x)
            theta += Math.PI;
        return theta;
    }
}
