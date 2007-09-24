package corina.map;

import corina.util.ColorUtils;

import java.awt.Color;
import java.awt.Stroke;
import java.awt.BasicStroke;

public class Palette {
    // earthmap10k.jpg uses a blue sort of like this one
    private static Color waterBlue = new Color(25, 72, 98);

    // 50/50, waterBlue/white
    private static Color lightBlue = ColorUtils.blend(waterBlue, 0.50f,
						      Color.white, 0.50f);

    // 25/75, waterBlue/white
    private static Color lightLightBlue = ColorUtils.blend(waterBlue, 0.25f,
							   Color.white, 0.75f);

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

    // --------------------------------------------------
    // NEW STUFF

    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    private static final Stroke NORMAL_STROKE = new BasicStroke(1);

    private static class Brush {
	Color color=TRANSPARENT;
	Stroke stroke=NORMAL_STROKE;
	Brush(Color color) {
	    this.color = color;
	}
	Brush(Color color, float thickness) {
	    this.color = color;
	    this.stroke = new BasicStroke(thickness);
	}
	Brush(Color color, float thickness, float dashes[]) {
	    this.color = color;
	    this.stroke = new BasicStroke(thickness,
					  BasicStroke.CAP_BUTT,
					  BasicStroke.JOIN_ROUND,
					  1, // DOCUMENT ME!
					  dashes,
					  0); // DOCUMENT ME!
	}
	// WRITEME: Brush(String) -- takes "blue 1.2 5,1,1", or "--" = default
    }

    private static final Brush NULL_BRUSH = new Brush(TRANSPARENT); // move to Brush.

    // (use this dash pattern for "intermittent" things)
    private static final float INTERM[] = new float[] { 4, 2, }; // to Brush.?

    private static final Brush BRUSHES[][] = new Brush[][] {
	{ // category=1 "US and Canada"
	    new Brush(Color.gray), // us/can state/province borders
	    NULL_BRUSH,
	    new Brush(Color.gray, 1, new float[] { 3, }), // us/can state/province borders, over water
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
	    new Brush(waterBlue, 2), // permanent major rivers
	    new Brush(waterBlue, 2), // additional major rivers
	    new Brush(lightBlue, 1.5f), // additional rivers
	    new Brush(lightBlue, 1), // minor rivers
	    new Brush(lightBlue, 1.5f), // double-lined rivers
	    new Brush(lightBlue, 2, INTERM), // intermittent rivers -- major
	    new Brush(lightLightBlue, 1.5f, INTERM), // intermittent rivers -- additional
	    new Brush(lightLightBlue, 1, INTERM), // intermittent rivers -- minor
	    NULL_BRUSH, // major canals
	    NULL_BRUSH, // canals of lesser importance
	    NULL_BRUSH,
	    NULL_BRUSH, // canals -- irrigation type
	    NULL_BRUSH,
	    NULL_BRUSH,
	},
	{ // category=3 "International Boundaries"
	    new Brush(Color.darkGray, 2), // "demarcated or delimited boundary"
	    new Brush(Color.darkGray, 2, new float[] {5,1,1,1}), // "indefinite or in dispute"
	    new Brush(Color.darkGray, 1), // "other line of separation of sovreignty on land"
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
	    new Brush(Color.red), // ???
	},
	{ // category=4 "Coast, Islands and Lakes"
	    new Brush(Color.black, 2), // coast, islands and lakes that appear on all maps
	    new Brush(Color.black, 2), // additional major islands and lakes
	    new Brush(lightBlue, 1.5f), // intermediate islands and lakes
	    new Brush(lightBlue), // minor islands and lakes
	    NULL_BRUSH,
	    new Brush(lightBlue, 1, INTERM), // intermittent major lakes
	    new Brush(lightBlue, 1, INTERM), // intermittent minor lakes
	    NULL_BRUSH, // reefs
	    NULL_BRUSH, // salt pans -- major
	    NULL_BRUSH, // salt pans -- minor
	    NULL_BRUSH,
	    NULL_BRUSH,
	    NULL_BRUSH, // ice shelves -- major
	    NULL_BRUSH, // ice shelves -- minor
	    NULL_BRUSH, // glaciers
	},
    };

    public static Color getColor(MapFile.Header h) {
	return BRUSHES[h.getCategory()-1][h.getType()-1].color;
    }
    public static Stroke getStroke(MapFile.Header h) {
	return BRUSHES[h.getCategory()-1][h.getType()-1].stroke;
    }

    // TODO: be able to show a complete legend!  (it'll never be printed)
    // it'll look like:
    //
    //   ----  Coasts, islands, and lakes
    //   - -   Minor islands and lakes
    //
    // perhaps on tabs, one per category?  or maybe just scroll a list.
    // 
    // (later, i can let users change this, even: click on one, or click an "Edit"
    // button, and modify it.  save it in ~/.corina/palette.properties, and use
    // that from then on.)
    // 
    // (also, a checkbox next to each one -- unchecking them makes them undisplayed,
    // and dims the sample pattern in the legend.)
}
