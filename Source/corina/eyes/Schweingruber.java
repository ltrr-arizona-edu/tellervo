package corina.eyes;

import corina.util.Angle;

import java.util.List;
import java.util.ArrayList;

// input: [brightness distance] tuples -- later, DensityGraph (DensitySamples?)
//        p1, p2 -- later: path
// output: list of ring widths
// params: thresh1, thresh2

/**
   Schweingruber's algorithm for determining where tree rings begin,
   given a graph of density (brightness).

*/
public class Schweingruber {

    public static float THRESHOLD_TOP = 100;
    public static float THRESHOLD_BOTTOM = 80;

    /**
       Run Schweingruber's algorithm.

       @param brightness
       @param distances
       @param path
       @return a List of Numbers, representing ring widths
    */
    public static List schweingruber(List brightness, List distances,
				     Path path) {
        // schweingruber's method: given threshold, look for (1)
        // increasing through threshold, (2) maximum (this is
        // ring-width?), (3) decreasing through threshold

        // (two thresholds, hmm ... how about the WATERSHED ALGORITHM?)

        // try FFTs?  to research.

        boolean inRing = false; // WAS: true; why?

        double lastDist = 0.0;

	/*
	  what's schweingruber's algorithm?
	  -- have 2 thresholds
	  -- if graph goes above lower thresh, set flag
	  -- if graph goes above upper thresh, and flag is set, that's
	     a ring boundary; clear
	  -- have to go below lower thresh again
	  -- (oops, did i mess that up?  try again.)
	 */

	// output
	List rings = new ArrayList();

	float low = Math.min(THRESHOLD_BOTTOM, THRESHOLD_TOP);
	float high = Math.max(THRESHOLD_BOTTOM, THRESHOLD_TOP);

        int n = brightness.size();
        for (int i=0; i<n; i++) {
            // what are we looking at?
	    // -- EXTRACT CLASS: these 2 values make up the density graph
            float bri = ((Number) brightness.get(i)).floatValue();
            double dist = ((Number) distances.get(i)).doubleValue();

            // entered ring?
            if (!inRing && bri<low) {
                inRing = true;
            }

            // exited ring?
            if (inRing && bri>high) {
                inRing = false;

		// to be accurate, i'll interpolate to find out
		// exactly where it crossed the threshold.
		double x1 = ((Number) distances.get(i-1)).doubleValue();
		double x2 = ((Number) distances.get(i  )).doubleValue();
		double y1 = ((Number) brightness.get(i-1)).doubleValue();
		double y2 = ((Number) brightness.get(i  )).doubleValue();
		double y3 = high;
		double x3 = interpolate(x1, y1, x2, y2, y3);

                // add ring
		// (i.e., getting lighter beyond THRESHOLD_TOP)
		double absDist = x3;

		// ring width
		double ring = absDist - lastDist;

		// no zero-length rings at start
		if (rings.isEmpty() && ring == 0.0) {
		    System.out.println("zero-length ring avoided");
		    continue;
		}
		// DOES THIS EVER HAPPEN?

		// last ring measured so far
                lastDist = absDist;

		// add it
                rings.add(new Double(ring));
            }
        }

	return rings;
    }

    /*
      y-y1   y-y2
      ---- = ----
      x-x1   x-x2

      y3-y1   y3-y2
      ----- = -----
      x3-x1   x3-x2

      (y3-y1)(x3-x2) = (x3-x1)(y3-y2)

      y3x3-x2y3-y1x3+x2y1 = x3y3-x3y2-x1y3+x1y2
        ^^        ^^        ^^   ^^

      x3(y3-y1-y3+y2) = x1y2-x1y3-x2y1+x2y3

      x3(y2-y1) = x1(y2-y3)+x2(y3-y2)

      -- what'd i miss?
    */
    private static double interpolate(double x1, double y1,
				      double x2, double y2,
				                 double y3) {

	double x3 = (x1*(y2-y3)+x2*(y3-y1/*was:y2*/)) / (y2-y1);

	return x3;
    }
}
