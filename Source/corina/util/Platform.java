package corina.util;

// platform-specific crap that java can't take care of.
// (gee, thanks, james.)

public class Platform {
    public static final boolean isMac = System.getProperty("os.name").startsWith("Mac OS");
}

