package corina.ui;

import javax.swing.KeyStroke;

import corina.core.App;

public class RuntimeUtils {

    // if str contains source, return a new string with target instead of source (once!);
    // returns str if it doesn't contain source.  (think str ~= s/source/target/.)
    // (if source=="", returns target+source, concatenated)
    // this is like java 1.4's java.lang.String.replaceFirst()
    public static String substitute(String str, String source, String target) {
        int index = str.indexOf(source);
        if (index == -1) // not present
            return str;
        int start = index, end = index + source.length();
        return str.substring(0, start) + target + str.substring(end);
    }
    
    // generate a keystroke, substituting either "meta" (mac) or "control" (other) for "accel", if needed
    // -- just be careful, "accel X" works fine but "accel x" returns null (!! -- not my fault...)
    public static KeyStroke getKeyStroke(String str) {
        return KeyStroke.getKeyStroke(substitute(str, "accel", App.platform.isMac() ? "meta" : "control"));
    }

    // this is why, even if i could, i shouldn't use serialization for widgets: they need to get
    // instantiated differently on different platforms, because they're going to end up different.
}
