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
    private Point p1, p2;

    private double theta;

    // compute the angle of a line drawn from a to b -- EXISTS ELSEWHERE, REFACTOR
    public static double angle(Point a, Point b) {
	double theta;
	double dx = b.x - a.x;
	double dy = b.y - a.y;
	if (dx == 0)
	    theta = Math.PI * (b.y < a.y ? 3/2 : 1/2);
	else
	    theta = Math.atan(dy/dx);
	if (b.x < a.x)
	    theta += Math.PI;
	return theta;
    }

    public Scanner(BufferedImage img, Point p1, Point p2) {
	this.img = img;
	this.p1 = p1;
	this.p2 = p2;

	rescan();
    }

    public void setPath(Point p1, Point p2) {
	this.p1 = p1;
	this.p2 = p2;

	// better invalidate rings now that the path has changed
	rings = new ArrayList();
    }

    public void rescan() {
	theta = angle(p1, p2);
	brightness = new ArrayList();
	distances = new ArrayList();
	rings = new ArrayList();
	nextRing = 0;
	phase1();
	phase2();
    }

    /* private */ List brightness;
    /* private */ List distances;

    private double dist(Point p1, Point p2) {
	double dx = p1.x - p2.x;
	double dy = p1.y - p2.y;
	return Math.sqrt(dx*dx + dy*dy);
    }

    private class Tuple implements Comparable {
        int x, y;
        double dist;
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
    }

    public static final int SWIPE = 21; // 21 makes it tick-width; 41 is pretty robust

    // phase 1
    // - from 0.0 to dist(p1, p2)
    // - going ds+=0.5 (???) at a time,
    // - compute ints (x,y)
    // - brightness = pixel[x][y]
    // - distance = dist(p1, (x,y)) (a double)
    private void phase1() {
	double smax = dist(p1, p2);

	List tuples = new ArrayList();

	for (double s=0.0; s<smax; s+=1.0) {

	    // base x,y => same distance across the front
	    int bx = p1.x + (int) (s * Math.cos(theta));
	    int by = p1.y + (int) (s * Math.sin(theta));
	    double dist = dist(p1, new Point(bx, by));

	    // average these?  ok, try it

	    int sum = 0;
	    int num = 0;

	    for (int i=-SWIPE/2; i<=SWIPE/2; i++) {

		int x = p1.x + (int) (s * Math.cos(theta)) + (int) (i*Math.cos(theta+Math.PI/2));
		int y = p1.y + (int) (s * Math.sin(theta)) + (int) (i*Math.sin(theta+Math.PI/2));

		int rgb;
		try {
		    rgb = img.getRGB(x, y);
		} catch (ArrayIndexOutOfBoundsException e) {
		    // if it's not a valid pixel, just ignore it
                    // WHY WOULD THIS HAPPEN?  DOES THIS HAPPEN?
		    continue;
		}
		int r = (rgb & 0x00ff0000) >> 16;
		int g = (rgb & 0x0000ff00) >> 8;
		int b = (rgb & 0x000000ff) >> 0;
		sum += (r + g + b);

		num++;
	    }

            tuples.add(new Tuple(bx, by, dist, sum/num));
        }

        // now we've got a bunch of tuples (x,y,dist,brightness)

        // so sort them by (x,y), so duplicates are easily removed
        Collections.sort(tuples);

        // remove duplicates -- would a hash be better, then?
        // isn't this |uniq|?  is that in the java api?  (would a Set help?)
        for (int i=0; i<tuples.size()-1; i++) {
            Tuple t1 = (Tuple) tuples.get(i);
            Tuple t2 = (Tuple) tuples.get(i+1);
            if (t1.equals(t2))
                tuples.remove(i+1);
        }

        // sort them by dist
        Sort.sort(tuples, "dist");

        // now stuff what's left in brightness, distances
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
    private void phase2() {
        int n = brightness.size();

        // schweingruber's method: given threshold, look for (1)
        // increasing through threshold, (2) maximum (this is
        // ring-width?), (3) decreasing through threshold

        final int THRESHOLD_TOP = 300; // compute dynamically?  ask user?
        final int THRESHOLD_BOTTOM = 250; // compute dynamically?  ask user?

        // two thresholds, hmm ... how about the WATERSHED ALGORITHM?

        // jeff says this sounds like perfect FFT territory, but
        // neither of us is quite sure about FFTs.  to research.

        boolean inRing = true; // false;
        int a=0, z=0;

        double lastDist = 0.0;

        for (int i=0; i<n; i++) {
            // what are we looking at?
            int bri = ((Number) brightness.get(i)).intValue();
            double dist = ((Number) distances.get(i)).doubleValue();

            // entered ring?
            if (!inRing && bri<THRESHOLD_BOTTOM) {
                inRing = true;
                a = i;
            }

            // left ring?
            if (inRing && bri>THRESHOLD_TOP) {
                inRing = false;
                z = i;

                // look for max
                int min = -1, minPos = -1;
                for (int j=a; j<=z; j++) {
                    int b = ((Number) brightness.get(j)).intValue();
                    if (b < min) {
                        b = min;
                        minPos = j;
                    }
                }

                // add ring -- use z (i.e., getting lighter beyond THRESHOLD_TOP)
                Ring r = new Ring();
                double abs = ((Number) distances.get(z)).doubleValue();
                r.dist = abs - lastDist;
                lastDist = abs;
                r.p = new Point(p1.x + (int) (abs*Math.cos(theta)),
                                p1.y + (int) (abs*Math.sin(theta)));
                rings.add(r);
            }
        }

        // don't count a zero-sized ring at the start
        if (!rings.isEmpty() && ((Ring) rings.get(0)).dist==0.0)
            rings.remove(0);
    }

    private List rings;
    public class Ring {
        Point p;
        double dist;
    }

    // grab one at a time, until done (<0)
    private int nextRing;
    public Ring getNextRing() { // this method is WORTHLESS!
        if (nextRing == rings.size())
            return null; // eep!  (that's ok, this method was obsolete as soon as it was written.)

        return (Ring) rings.get(nextRing++);
    }

    // this is a better interface, methinks.  but it doesn't work.  d'oh!
    public Ring[] getRings() {
        return (Ring[]) rings.toArray(new Ring[0]);
    }

    // for debugging only so far: output density-versus-distance to /tmp/rawdata
    public void outputRawData() throws IOException {
        BufferedWriter w = new BufferedWriter(new FileWriter("/tmp/rawdata"));
        int n = brightness.size();
        for (int i=0; i<n; i++) {
            w.write(distances.get(i) + " " + brightness.get(i));
            // w.write(distances.get(i) + " " + (256*3 - ((Number) brightness.get(i)).intValue()));
            w.newLine();
        }
        w.close();
    }
}
