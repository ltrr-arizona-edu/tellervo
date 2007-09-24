// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package vectormap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import corina.util.ColorUtils;

/**
 * Taken from Palette.  Lots of comments are Ken's not mine.
 * This PaintScheme reproduces the "fixed"/hardcoded paint scheme in Corina.
 * @author Aaron Hamid (aaron at users dot sf dot net)
 */
public final class FixedPaintScheme implements PaintScheme {
  public static final FixedPaintScheme INSTANCE = new FixedPaintScheme();

  // earthmap10k.jpg uses a blue sort of like this one
  private static final Color waterBlue = new Color(25, 72, 98);

  // 50/50, waterBlue/white
  private static final Color lightBlue = ColorUtils.blend(waterBlue, 0.50f, Color.white, 0.50f);

  // 25/75, waterBlue/white
  private static final Color lightLightBlue = ColorUtils.blend(waterBlue, 0.25f, Color.white, 0.75f);

  /*
    how about just text lines?
    category=1 type=1 color=gray stroke=1
    category=1 type=2 color=blue stroke=1.2
    ...
    i think i can live with that.

    -- i'll want dotted lines, too.
    category=1 type=2 color=blue thickness=1.2 dashes=5,1,1,1
    (also allowing things like "_." for "dashes" would be nifty)

    -- defaults:
    color=clear?
    thickness=1.0
    dashes=none

    -- can i steal the Properties class for i/o?
    "1,2 = blue 1.2 5,1,1"
  */

  private static final Stroke NORMAL_STROKE = new BasicStroke(1,
                                                              BasicStroke.CAP_BUTT,
                                                              BasicStroke.JOIN_BEVEL,
                                                              1.0f);

  private static Brush createBrush(Color c) {
    return new PaintScheme.Brush(c, NORMAL_STROKE);
  }
  private static Brush createBrush(Color c, float t) {
    return new PaintScheme.Brush(c, new BasicStroke(t,
                                                    BasicStroke.CAP_BUTT,
                                                    BasicStroke.JOIN_BEVEL,
                                                    1.0f));
  }
  private static Brush createBrush(Color c, float t, float dashes[]) {
    return new PaintScheme.Brush(c, new BasicStroke(t,
                                                    BasicStroke.CAP_BUTT,
                                                    BasicStroke.JOIN_BEVEL,
                                                    1.0f, // miterlimit
                                                    dashes,
                                                    0.0f)); // phase offset
  }

  private static final Brush NULL_BRUSH = createBrush(Color.RED, 4.0f); // should be obvious if we see this

  // (use this dash pattern for "intermittent" things)
  private static final float INTERM[] = new float[] { 4, 2, }; // to Brush.?

  private static final Brush BRUSHES[][] = new Brush[][] {
    { /* category=0
      # THE BACKGROUND COLOR MUST BE THE FIRST COLOR SPECIFIED
      0 01 black  #background color
      0 02 indianred  #gridlines around earth (longitudinal)
      0 03 indianred  #gridlines around earth (latitude)
      0 04 yellow #the circle around cities
      0 05 yellow #the color of the text for the cities
      */
      null,
      createBrush(Color.black),
      NULL_BRUSH,
      NULL_BRUSH,
      NULL_BRUSH,
      NULL_BRUSH,
    },
    { // category=1 "US and Canada"
      null,
      createBrush(Color.gray), // us/can state/province borders
      NULL_BRUSH,
      createBrush(Color.gray, 1, new float[] { 3 }), // us/can state/province borders, over water
      NULL_BRUSH,
      NULL_BRUSH,
      NULL_BRUSH,
      NULL_BRUSH,
      NULL_BRUSH,
      NULL_BRUSH,
      NULL_BRUSH,
      NULL_BRUSH,
      NULL_BRUSH,
      NULL_BRUSH, // DOCUMENT ME: what are all these nulls?  "don't exist"?  "don't draw"?
      NULL_BRUSH,
    },
    { // category=2 "Rivers"
      null,
      createBrush(waterBlue, 2), // permanent major rivers
      createBrush(waterBlue, 2), // additional major rivers
      createBrush(lightBlue, 1.5f), // additional rivers
      createBrush(lightBlue, 1), // minor rivers
      createBrush(lightBlue, 1.5f), // double-lined rivers
      createBrush(lightBlue, 2, INTERM), // intermittent rivers -- major
      createBrush(lightLightBlue, 1.5f, INTERM), // intermittent rivers -- additional
      createBrush(lightLightBlue, 1, INTERM), // intermittent rivers -- minor
      NULL_BRUSH, // major canals
      NULL_BRUSH, // canals of lesser importance
      NULL_BRUSH,
      NULL_BRUSH, // canals -- irrigation type
      NULL_BRUSH,
      NULL_BRUSH,
    },
    { // category=3 "International Boundaries"
      null,
      createBrush(Color.darkGray, 2), // "demarcated or delimited boundary"
      createBrush(Color.darkGray, 2, new float[] {5,1,1,1}), // "indefinite or in dispute"
      createBrush(Color.darkGray, 1), // "other line of separation of sovreignty on land"
      NULL_BRUSH,
      NULL_BRUSH,
      NULL_BRUSH,
      NULL_BRUSH,
      NULL_BRUSH,
      NULL_BRUSH,
      NULL_BRUSH,
      NULL_BRUSH,
      NULL_BRUSH,
      NULL_BRUSH,
      NULL_BRUSH,
      createBrush(Color.red), // ???
    },
    { // category=4 "Coast, Islands and Lakes"
      null,
      createBrush(Color.black, 2), // coast, islands and lakes that appear on all maps
      createBrush(Color.black, 2), // additional major islands and lakes
      createBrush(lightBlue, 1.5f), // intermediate islands and lakes
      createBrush(lightBlue), // minor islands and lakes
      NULL_BRUSH,
      createBrush(lightBlue, 1, INTERM), // intermittent major lakes
      createBrush(lightBlue, 1, INTERM), // intermittent minor lakes
      NULL_BRUSH, // reefs
      NULL_BRUSH, // salt pans -- major
      NULL_BRUSH, // salt pans -- minor
      NULL_BRUSH,
      NULL_BRUSH,
      NULL_BRUSH, // ice shelves -- major
      NULL_BRUSH, // ice shelves -- minor
      NULL_BRUSH, // glaciers
    }
  };

  public Brush[][] getScheme() {
    return BRUSHES;
  }
}