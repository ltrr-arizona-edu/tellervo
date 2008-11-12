// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt
// Created on Mar 11, 2005

package vectormap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.font.LineMetrics;

/**
 * Implementation of Layer that simply draws longitude/latitude grid lines.  An attempt is made to
 * draw them in an aesthetically pleasing manner.
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public class GridLayer implements Layer {
  protected static final Color COLOR = new Color(Color.gray.getRed(), Color.gray.getGreen(), Color.gray.getBlue(), 64);
  protected static final int MIN_PIXELS = 20;
  protected static final int MIN_DEGREES = 1;
  protected static final int PRINT_INTERVAL_PIXELS = 40;
  protected Dimension dimension = new Dimension();
  protected int width;
  protected int height;
  //private float pixPerHorDeg;
  //private float pixPerVerDeg;
  private Interval horizontal = new Interval();
  private Interval vertical = new Interval();

  public void init(VectorMap map, Projection projection) { }

  public void setSize(Dimension size) {
    //System.out.println("Setting size: "+ size);
    this.dimension.width = size.width;
    this.dimension.height = size.height;
    width = size.width;
    height = size.height;
    float pixPerHorDeg = size.width / 360f;
    float pixPerVerDeg = size.height / 180f;
    calculateLineInterval(horizontal, MIN_PIXELS, MIN_DEGREES, pixPerHorDeg);
    calculateLineInterval(vertical, MIN_PIXELS, MIN_DEGREES, pixPerVerDeg);
  }

  public Dimension getSize() {
    return dimension;
  }

  private static final class Interval {
    float pixels;
    float degrees;
    public String toString() {
      return "[Interval: " + pixels + " pixels, " + degrees + " degrees]";
    }
  }
  private static void calculateLineInterval(Interval interval, int minPixels, int minDegrees, float pixelsPerDegree) {
    // first figure out the number of pixels to represent the minimum degrees
    float pixPerMinDegInterval = pixelsPerDegree * minDegrees;
    System.out.println("pixPerMinDegInterval: " + pixPerMinDegInterval);
    // figure out the number of degrees that represent the minim
    float degPerMinPixInterval = minPixels / pixelsPerDegree;
    System.out.println("degPerMinPixInterval: " + degPerMinPixInterval);

    int pixInterval = nearestMultipleOfX(pixPerMinDegInterval, minPixels);
    if (pixInterval == 0) pixInterval = minPixels;
    float pixDegInterval = pixInterval / pixelsPerDegree;
    System.out.println("pixInterval: " + pixInterval);

    int degInterval = nearestMultipleOfX(degPerMinPixInterval, minDegrees);
    if (degInterval == 0) degInterval = minDegrees;
    System.out.println("degInterval: " + degInterval);

    if (pixDegInterval < degInterval) {
      interval.pixels = pixInterval;
      interval.degrees = pixInterval / pixelsPerDegree;
    } else {
      interval.degrees = degInterval;
      interval.pixels = degInterval * pixelsPerDegree;
    }
  }
  // http://qbnz.com/pages/tutorials/algorsnz.txt
  private static int nearestMultipleOfX(float value, int multiple) {
    return ((int) ((value + (multiple - 1))) / multiple) * multiple; 
  }
  private static void drawLabel(float x, float y, String s, Graphics g, boolean vertical) {
    FontMetrics fm = g.getFontMetrics();
    LineMetrics lm = fm.getLineMetrics(s, g);
    Rectangle r = fm.getStringBounds(s, g).getBounds();
    if (vertical) {
      r.setLocation((int) x, (int) (y - (r.getHeight() / 2)));
    } else {
      r.setLocation((int) (x - (r.getWidth() / 2)), (int) y);
    }
    /*r.grow(2, 2);
    g.drawRoundRect((int) r.getMinX(), (int) r.getMinY(), (int) r.getWidth(), (int) r.getHeight(), 4, 4);
    g.setColor(Color.white);
    r.grow(-2, -2);*/
    Color originalColor = g.getColor();
    g.setColor(Color.white);
    g.fillRect((int) r.getMinX(), (int) r.getMinY(), (int) r.getWidth(), (int) r.getHeight());
    g.setColor(originalColor);
    g.drawString(s, (int) r.getMinX(), (int) (r.getMaxY() - lm.getDescent()));
  }
  public void draw(Graphics g, Rectangle viewrect, boolean exclusive) {
    System.out.println("Grid viewrect: "+ viewrect);

    g.setColor(COLOR);
    Font f = Font.getFont("SanSerif PLAIN 6");
    g.setFont(f);
    Interval i = horizontal;
    System.out.println(i);
    
    int startpixel = nearestMultipleOfX(viewrect.x, (int) i.pixels);
    int startdegree = (int) ((startpixel / i.pixels) * i.degrees);
    int lastlen = PRINT_INTERVAL_PIXELS;
    for (float d = startpixel; d <= viewrect.x + viewrect.width; d += i.pixels, startdegree += i.degrees, lastlen += i.pixels) {
      g.drawLine((int) d, 0, (int) d, viewrect.y + viewrect.height);
      if (lastlen >= PRINT_INTERVAL_PIXELS) {
        drawLabel(d, viewrect.y + 2, String.valueOf(startdegree - 180), g, false);
        lastlen = 0;
      }
    }
    i = vertical;
    System.out.println(i);
    startpixel = nearestMultipleOfX(viewrect.y, (int) i.pixels);
    startdegree = (int) ((startpixel / i.pixels) * i.degrees);
    lastlen = PRINT_INTERVAL_PIXELS;
    for (float d = startpixel; d <= viewrect.y + viewrect.height; d += i.pixels, startdegree += i.degrees, lastlen += i.pixels) {
      g.drawLine(0, (int) d, viewrect.x + viewrect.width, (int) d);
      if (lastlen >= PRINT_INTERVAL_PIXELS) {
        drawLabel(viewrect.x + 2, d, String.valueOf(90 - startdegree), g, true);
        lastlen = 0;
      }
    }

    //g.setColor(Color.green);
    //g.drawRect(viewrect.x, viewrect.y, viewrect.width - 1, viewrect.height - 1);
  }
}