//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package corina.eyes;

import corina.util.Sort;
import corina.util.Angle;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class Scanner {

    private BufferedImage img;
    private Path path;

    public Scanner(BufferedImage img, Path path) {
	this.img = img;
	this.path = path;

	rescan();
    }

    public void rescan() {
	brightness = new ArrayList();
	distances = new ArrayList();
	rings = new ArrayList();

	// get density graph from bitmap
	sample();

	// get rings from density graph
	rings = Schweingruber.schweingruber(brightness, distances,
					    path);
    }

    // peeked at directly by DensityPanel
    /*private*/ List brightness;
    /*private*/ List distances;

    // ugh!
    private static double dist(Point A, Point B) {
	double dx = A.x - B.x;
	double dy = A.y - B.y;
	return Math.sqrt(dx*dx + dy*dy);
    }

    // (needs to be public, for util.sort)
    public class Tuple implements Comparable {
        int x, y;
        public double dist;
        int brightness;
        Tuple(int x, int y, double dist, int brightness) {
            this.x = x;
            this.y = y;
            this.dist = dist;
            this.brightness = brightness;
        }
        public int compareTo(Object o2) {
            Tuple t2 = (Tuple) o2;
            if (x < t2.x)
                return -1;
            if (x > t2.x)
                return +1;
            if (y < t2.y)
                return -1;
            if (y > t2.y)
                return +1;
            return 0;
        }
	public boolean equals(Object o2) {
	    Tuple t2 = (Tuple) o2;
	    return x==t2.x && y==t2.y;
	}
	public int hashCode() { // (because i defined equals())
	    return new Integer(x).hashCode() +
		   new Integer(y).hashCode();
	}
    }

    // 21 makes it tick-width; 41 is pretty robust
    public static /* final */ int SWIPE = 21;

    // phase 1
    // - from 0.0 to dist(p1, p2)
    // - going ds+=0.5 (???) at a time,
    // - compute ints (x,y)
    // - brightness = pixel[x][y]
    // - distance = dist(p1, (x,y)) (a double)
    // inputs:
    // -- p1, p2 (later: path)
    // -- image
    // outputs:
    // -- [brightness distance] tuples
    // params:
    // -- swipe
    private void sample() {
	double smax = dist(path.a, path.b); // total path length
	double theta = Angle.angle(path.a, path.b); // (obsolete!)

	List tuples = new ArrayList();

	for (double s=0.0; s<smax; s+=1.0) { // comments above say 0.5 -- ?? -- too many dupes?

	    // base x,y => same distance across the front
	    int bx = path.a.x + (int) (s * Math.cos(theta));
	    int by = path.a.y + (int) (s * Math.sin(theta));
	    double dist = dist(path.a, new Point(bx, by));

	    // average these?  ok, try it

	    int sum = 0;
	    int num = 0;

	    for (int i=-SWIPE/2; i<=SWIPE/2; i++) { // why 0.5 above, but 1(.0) here?

		int x = path.a.x + (int) (s * Math.cos(theta)) +
		                   (int) (i * Math.cos(theta+Math.PI/2));
		int y = path.a.y + (int) (s * Math.sin(theta)) +
		                   (int) (i * Math.sin(theta+Math.PI/2));

		int rgb;
		try {
		    rgb = img.getRGB(x, y);
		} catch (ArrayIndexOutOfBoundsException e) {
		    // if it's not a valid pixel, just ignore it.
		    // this means the user tried to drag the path
		    // outside the image, and we didn't stop it.
		    continue;
		}
		int r = (rgb & 0x00ff0000) >> 16;
		int g = (rgb & 0x0000ff00) >>  8;
		int b = (rgb & 0x000000ff) >>  0;
		sum += (r + g + b) / 3;

		num++;
	    }

	    // can i store them in a hash by x/y, so dupes don't even get added?

            tuples.add(new Tuple(bx, by, dist, Math.round(sum/(float)num)));
	    // FIXME: why round, anyway?  why can't brightness be a float?
        }

        // now we've got a bunch of tuples (x,y,dist,brightness)

	// PERF: ok, this block does remove some samples, but do i care?
	// OR: if they have the same x,y, they'll have the same dist, so i can
	// remove them as i add them later with no penalty, right?
	// IF SO, then i can remove compareTo, equals, and x,y from Tuple,
	// and then it's just the DensityGraph.  nifty!
	{
	    // so sort them by (x,y), so duplicates are easily removed
	    Collections.sort(tuples);

	    // remove duplicates -- would a hash be better, then?
	    // isn't this |uniq|?  is that in the java api?  (would a Set help?)
	    for (int i=0; i<tuples.size()-1; i++) {
		Tuple t1 = (Tuple) tuples.get(i);
		Tuple t2 = (Tuple) tuples.get(i+1);
		if (t1.equals(t2)) // compares x,y
		    tuples.remove(i+1);
	    }
	}

        // sort them by dist
        Sort.sort(tuples, "dist");

        // now stuff what's left in brightness, distances
	// LATER: this is a DensityGraph
        for (int i=0; i<tuples.size(); i++) {
            Tuple t = (Tuple) tuples.get(i);
            brightness.add(new Integer(t.brightness));
            distances.add(new Double(t.dist));
        }
    }

    // original plan for phase 2:
    // - have a bunch of (distance,brightness) tuples
    // - (make sure they're sorted, increasing distance)
    // - from (s, b) compute (ds/db, b)
    // - wherever ds/db crosses the 0-axis, that's a ring

    // original phase 2 didn't work so well.  plan B: schweingruber!
    // -> now in class Schweingruber.

    // REFACTOR: rings is created, and then never changed.  why not an array?
    private List rings;

    // convert |rings| from List of Double to double[], and return it
    // -- seems silly to copy it each time, when it's a read-only structure anyway.
    public double[] getWidths() {
	int n = rings.size();
	double widths[] = new double[n];
	for (int i=0; i<n; i++)
	    widths[i] = ((Number) rings.get(i)).doubleValue();
	return widths;
    }

    // FIXME: produce lazily?
    public Point[] getTicks() {
	int n = rings.size();
	double theta = Angle.angle(path.a, path.b);
	Point ticks[] = new Point[n];
	double dist = 0;
	for (int i=0; i<n; i++) {
	    dist += ((Number) rings.get(i)).doubleValue();
	    int x = path.a.x + (int) (dist * Math.cos(theta));
	    int y = path.a.y + (int) (dist * Math.sin(theta));
	    ticks[i] = new Point(x, y);
	    // TODO: use Point2D.* for greater precision?
	}
	return ticks;
    }

    // REFACTOR:
    // -x- make double[] getWidths() method
    // -x- make Point[] getTicks() method
    // -x- get rid of getRings() method
    // -x- get rid of Ring class
    // --- make ticks from widths,path lazily?
    // -x- make Schweingruber return widths only
    // --- compute tick-points after it's returned
}
