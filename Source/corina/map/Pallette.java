package corina.map;

import java.awt.Color;

public class Pallette {
    private static Color colors[][] = new Color[][] {
    { // category=0 "jmap-added"
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
        Color.blue, // permanent major rivers
        Color.blue, // additional major rivers
        new Color(0.7f, 0.7f, 1.0f), // additional rivers
        new Color(0.7f, 0.7f, 1.0f), // minor rivers
        new Color(0.7f, 0.7f, 1.0f), // double-lined rivers
        new Color(0.7f, 0.7f, 1.0f), // intermittent rivers -- major
        new Color(0.9f, 0.9f, 1.0f), // intermittent rivers -- additional
        new Color(0.9f, 0.9f, 1.0f), // intermittent rivers -- minor
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
        new Color(0.7f, 0.7f, 1.0f), // intermediate islands and lakes
        new Color(0.7f, 0.7f, 1.0f), // minor islands and lakes
        null,
        new Color(0.7f, 0.7f, 1.0f), // intermittent major lakes
        new Color(0.7f, 0.7f, 1.0f), // intermittent minor lakes
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
