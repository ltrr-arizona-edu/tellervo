package corina.map;

import corina.util.ColorUtils;

import java.awt.Color;

public class Pallette {
    // earthmap10k.jpg uses a blue sort of like this one
    private static Color waterBlue = new Color(25, 72, 98);

    //    private static Color lightBlue = new Color(0.7f, 0.7f, 1.0f);
//    private static Color lightBlue = ColorUtils.addAlpha(Color.blue, 127);
    private static Color lightBlue = ColorUtils.addAlpha(waterBlue, 127);
    // private static Color lightLightBlue = new Color(0.9f, 0.9f, 1.0f);
//    private static Color lightLightBlue = ColorUtils.addAlpha(Color.blue, 63);
    private static Color lightLightBlue = ColorUtils.addAlpha(waterBlue, 63);

    private static Color colors[][] = new Color[][] {
    { // category=0 "corina-added"
        null,
        null,
        null,
        Color.pink, // gridlines, all (?)
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
    },
    { // category=1 "US and Canada"
        null,
        Color.gray, // us/can state/province borders
        null,
        Color.gray, // us/can state/province borders, over water
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
    },
    { // category=2 "Rivers"
        null,
        waterBlue, // permanent major rivers
        waterBlue, // additional major rivers
        lightBlue, // additional rivers
        lightBlue, // minor rivers
        lightBlue, // double-lined rivers
        lightBlue, // intermittent rivers -- major
        lightLightBlue, // intermittent rivers -- additional
        lightLightBlue, // intermittent rivers -- minor
                        //        new Color(0.9f, 0.9f, 1.0f), // intermittent rivers -- additional
        // new Color(0.9f, 0.9f, 1.0f), // intermittent rivers -- minor
        null, // major canals
        null, // canals of lesser importance
        null,
        null, // canals -- irrigation type
        null,
        null,
    },
    { // category=3 "International Boundaries"
        null,
        Color.darkGray, // "demarcated or delimited boundary"
        Color.darkGray, // "indefinite or in dispute"
        Color.darkGray, // "other line of separation of sovreignty on land"
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        Color.red,
    },
    { // category=4 "Coast, Islands and Lakes"
        null,
        Color.black, // coast, islands and lakes that appear on all maps
        Color.black, // additional major islands and lakes
        lightBlue, // intermediate islands and lakes
        lightBlue, // minor islands and lakes
        null,
        lightBlue, // intermittent major lakes
        lightBlue, // intermittent minor lakes
        null, // reefs
        null, // salt pans -- major
        null, // salt pans -- minor
        null,
        null,
        null, // ice shelves -- major
        null, // ice shelves -- minor
        null, // glaciers
    },
    };
    public static Color getColor(Map.Header h) {
        return colors[h.category][h.type];
    }
}
