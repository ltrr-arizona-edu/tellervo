package corina.ui;

import corina.util.Platform;

import java.util.ResourceBundle;

public class I18n {

    // ----------------------------------------
    // extract parts of a string -- given a resource "key"!
    // key: "copy"
    // (value: "&Copy [accel C]")
    // text: "Copy"
    // keystroke: "control C" (may be null)
    // mnemonic: 'C' (may be null)
    // ----------------------------------------

    public static String getText(String key) {
	String value = msg.getString(key);

	StringBuffer buf = new StringBuffer();

	int n = value.length();
	boolean ignore = false;
	for (int i=0; i<n; i++) {
	    char c = value.charAt(i);
	    switch (c) {
	    case '&': continue;
	    case '[': ignore = true; break;
	    case ']': ignore = false; break;
	    default:
		if (!ignore)
		    buf.append(c);
	    }
	}

	return buf.toString().trim();
    }

    public static String getKeyStroke(String key) {
	String value = msg.getString(key);

	int left = value.indexOf('[');
	int right = value.indexOf(']');

	if (left==-1 || right==-1)
	    return null;

	String stroke = value.substring(left+1, right).trim();

	// accel = command on mac, control on pc
	stroke = substitute(stroke, "accel", Platform.isMac ? "meta" : "control");

	return stroke;
    }

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

    public static Character getMnemonic(String key) {
	String value = msg.getString(key);

	int amp = value.indexOf('&');

	if (amp==-1 || amp==value.length()-1)
	    return null;

	return new Character(Character.toUpperCase(value.charAt(amp+1)));
    }

    // ----
    private final static ResourceBundle msg = ResourceBundle.getBundle("TextBundle");
}
