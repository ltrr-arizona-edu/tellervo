package corina.util;

import java.awt.Color;

public class ColorUtils {
    // blend c1 and c2
    public static Color blend(Color c1, Color c2) {
        return new Color((c1.getRed() + c2.getRed()) / 2,
                         (c1.getGreen() + c2.getGreen()) / 2,
                         (c1.getBlue() + c2.getBlue()) / 2);
    }

    // make c transparent
    public static Color addAlpha(Color c, int alpha) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
    }
}
