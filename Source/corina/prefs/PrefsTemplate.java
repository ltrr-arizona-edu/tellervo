/*
    THIS FILE IS OBSOLETE!  delete it ASAP.

    -- only used by: Prefs.getCorinaProperties(), for figuring out
    which properties to save.
*/

package corina.prefs;

import java.util.List;
import java.util.ArrayList;

// the template used to construct the preferences dialog

public class PrefsTemplate {

    // data
    private static List categories; // of String
    private static List options; // of Option

    /**
       A (category, description, property, type, value) tuple.  These
       used by the GUI preferences tool.
    */
    public static class Option {
	public final static int TYPE_STRING = 0; // arbitrary String
	public final static int TYPE_COLOR = 1; // a Ccolor
	public final static int TYPE_BOOL = 2; // a boolean
	public final static int TYPE_FONT = 3; // a Font
	public final static int TYPE_HIDDEN = 4; // don't show user
        public final static int TYPE_FORMAT = 5; // a formatting string, like "0.000"
                
	public String category; // category, as a string; use title capitalization
	public String description; // human-readable description
	public String property; // java property this sets
	public int type=TYPE_STRING; // type of this option (see constants above)
	public String value; // the current value

        public Option(String c, String d, String p, int t) {
            category = c;
            description = d;
            property = p;
            type = t;
            readValue();
        }
        public void readValue() {
            value = System.getProperty(property);
            if (value == null)
                value = "";
        }
    }

    // this gets done when the class PrefsTemplate is loaded, which is
    // the first time the user selects "Preferences..." -- this is
    // *not* done on startup.  (and not just for performance reasons:
    // if this is loaded before Prefs.load() occurs, the options don't
    // get the user's init values, and all hell breaks loose.  of
    // course, Option could be made smart enough to re-check, but it's
    // better to simply not init the template then.)
    static {
	// categories, options
	categories = new ArrayList();
	options = new ArrayList();
    }

    // public access to options
    public static List getOptions() {
	return options;
    }

    // public access to categories
    public static List getCategories() {
	return categories;
    }
}
