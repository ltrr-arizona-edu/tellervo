// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package vectormap;

/**
 * Tests the culling algorithm.
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public class testcull {
  protected static class CullResult {
    public int[] array_x;
    public int[] array_y;
    public int numpoints;
  }
  protected static void cull(int[] block_x, int[] block_y, float threshold, CullResult cullresult) {
    int writeIdx = 1;
    int startIdx = 0;
    int endIdx = startIdx + 1;
    //int[] newx = new int[block_x.length];
    //int[] newy = new int[block_x.length];
    //newx[0] = block_x[0];
    //newy[0] = block_y[0];
    while (endIdx < block_x.length) {
      if (distance(block_x[startIdx], block_y[startIdx], block_x[endIdx], block_y[endIdx]) >= threshold) {
        block_x[writeIdx] = block_x[endIdx];
        block_y[writeIdx] = block_y[endIdx];
        System.out.println("Found: " + startIdx + " " + endIdx);
        startIdx = endIdx;
        writeIdx++;
      }
      endIdx++;
    }
    System.out.println("Done.");
    System.out.println("StartIdx: " + startIdx);
    System.out.println("EndIdx: " + endIdx);
    System.out.println("WriteIdx: " + writeIdx);
    if (block_x.length > 1 && startIdx != (block_x.length - 1)) {
      block_x[writeIdx] = block_x[block_x.length - 1];
      block_y[writeIdx] = block_y[block_y.length - 1];
      writeIdx++;
    }
    cullresult.array_x = block_x;
    cullresult.array_y = block_y;
    cullresult.numpoints = writeIdx;
  }
  // pythagorean distance
  private static final double distance(int x1, int y1, int x2, int y2) {
    return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
  }

  private static void printArray(int[] arrayx, int[] arrayy, int n) {
    System.out.print(n + " [ ");
    for (int i = 0; i < n; i++) {
      System.out.print(arrayx[i] + "," + arrayy[i] + " ");
    }
    System.out.println("]");
  }
  public static void main(String[] args) throws Exception {
    int[] arrayx = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11 };
    int[] arrayy = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    printArray(arrayx, arrayy, arrayx.length);
    CullResult cr = new CullResult();
    cull(arrayx, arrayy, 2, cr);
    printArray(cr.array_x, cr.array_y, cr.numpoints);
    int[] arrayx2 = new int[cr.numpoints];
    int[] arrayy2 = new int[cr.numpoints];
    System.arraycopy(cr.array_x, 0, arrayx2, 0, cr.numpoints);
    System.arraycopy(cr.array_y, 0, arrayy2, 0, cr.numpoints);
    cull(cr.array_x, cr.array_y, 2, cr);
    printArray(cr.array_x, cr.array_y, cr.numpoints);
  }
}
