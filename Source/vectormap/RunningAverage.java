// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package vectormap;

/**
 * Compiles a running average
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public class RunningAverage {
  private double avg;
  private int count;

  /**
   * Constructs object with no initial values
   */
  public RunningAverage() {}

  /**
   * Constructs object with initial average, and update count
   */
  public RunningAverage(double i, int c) {
    avg = i;
    count = c;
  }

  /**
   * Constructs object with set of values
   */
  public RunningAverage(double[] vals) {
    set(vals);
  }

  /**
   * Adds a set of values to the average
   * @param vals values to add to the average
   */
  public synchronized void set(double[] vals) {
    clear();
    for (int i = 0; i < vals.length; i++) {
      avg += vals[i];
    }
    avg /= vals.length;
    count = vals.length;
  }

  /**
   * Adds a value to the average
   * @param val value to add to the average
   */
  public synchronized void add(double val) {
    avg = ((count * avg) + val) / ++count;
  }

  /**
   * Returns the current average
   * @return the current average
   */
  public synchronized double getAverage() {
    return avg;
  }

  /**
   * Clears average
   */
  public synchronized void clear() {
    avg = 0;
    count = 0;
  }

  /**
   * Returns string representation.
   */
  public String toString() {
    return "Average: " + getAverage() + " Measurements: " + count;
  }

  public static void main(String[] args) throws Exception {
    RunningAverage ra = new RunningAverage();
    double[] vals = new double[args.length];
    for (int i = 0; i < args.length; i++) {
      vals[i] = Double.valueOf(args[i]).doubleValue();
      ra.add(vals[i]);
      System.err.println("average1: " + ra.getAverage());
    }
    ra.set(vals);
    System.err.println("average2: " + ra.getAverage());
  }
}
