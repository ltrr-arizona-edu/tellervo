package corina.util;

import java.awt.Color;

public class ColorUtils {
    // blend c1 and c2
    public static Color blend(Color c1, Color c2) {
        return new Color((c1.getRed() + c2.getRed()) / 2,
                         (c1.getGreen() + c2.getGreen()) / 2,
                         (c1.getBlue() + c2.getBlue()) / 2);
    }

    // blend f1 of c1 and f2 of c2 (f1+f2=1.0)
    public static Color blend(Color c1, float f1, Color c2, float f2) {
        return new Color((int) (f1*c1.getRed() + f2*c2.getRed()),
                         (int) (f1*c1.getGreen() + f2*c2.getGreen()),
                         (int) (f1*c1.getBlue() + f2*c2.getBlue()));
    }

    // make c transparent
    public static Color addAlpha(Color c, int alpha) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
    }
    public static Color addAlpha(Color c, float f) {
        return addAlpha(c, (int) Math.round(255*f));
    }

    public static boolean reallyDark(Color c) {
        Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsb);
        return (hsb[2] < 0.1f); // pick a value... 10%
    }
    private static float hsb[] = new float[3];
}
