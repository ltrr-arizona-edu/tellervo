// 
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package corina.prefs;

import java.util.List;
import java.util.ArrayList;

// the template used to construct the preferences dialog

public class PrefsTemplate {

    // data
    private static List categories; // of String
    private static List options; // of Option

    /** A (category, description, property, type, value) tuple.  These
	  used by the GUI preferences tool.
       @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
       @version $Id$ */
    public static class Option {
	public final static int TYPE_STRING = 0; // arbitrary String
	public final static int TYPE_COLOR = 1; // a Ccolor
	public final static int TYPE_BOOL = 2; // a boolean
	public final static int TYPE_FONT = 3; // a Font
        	public final static int TYPE_HIDDEN = 4; // don't show user
                
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

	// 5 categories
	categories.add("Editor");
	categories.add("Index");
	categories.add("Crossdate");
	categories.add("Graph");
	categories.add("Advanced");

	// editor
	options.add(new Option("Editor", "Background color:",
                        "corina.edit.background", Option.TYPE_COLOR));
	options.add(new Option("Editor", "Text color:",
                        "corina.edit.foreground", Option.TYPE_COLOR));
	options.add(new Option("Editor", "Font:",
                        "corina.edit.font", Option.TYPE_FONT));
	options.add(new Option("Editor", "Draw Gridlines",
                        "corina.edit.gridlines", Option.TYPE_BOOL));
	options.add(new Option("Editor", "Serial Port:",
                        "corina.measure.port", Option.TYPE_STRING));

        /*
         something like:
         (define *prefs-template*
          '(prefs-group :title "Editor"
            (prefs-field :title "Background color:" :key corina-edit-background :type color)
            (prefs-field :title "Text color:" :key corina-edit-foreground :type color)
            (prefs-field :title "Font:" :key corina-edit-font :type font)
            (prefs-field :title "Draw Gridlines" :key corina-edit-gridlines :type boolean))
          (prefs-group :title "Index"
           ...))

         -- is each field its own alist?  hmm...

         -- put defaults here?  and load from disk on startup.
         (define *prefs-values* nil)
         */

	// index
	options.add(new Option("Index", "High-pass filter weights:",
                        "corina.index.lowpass", Option.TYPE_STRING));
	options.add(new Option("Index", "Polynomial degrees:",
                        "corina.index.polydegs", Option.TYPE_STRING));
	options.add(new Option("Index", "Cubic spline s-value:",
                        "corina.index.cubicfactor", Option.TYPE_STRING));

	// crossdate
	options.add(new Option("Crossdate", "T-Score format:",
                        "corina.cross.tscore.format", Option.TYPE_STRING));
	options.add(new Option("Crossdate", "Trend format:",
                        "corina.cross.trend.format", Option.TYPE_STRING));
	options.add(new Option("Crossdate", "D-Score format:",
                        "corina.cross.dscore.format", Option.TYPE_STRING));
	options.add(new Option("Crossdate", "Minimum Overlap:",
                        "corina.cross.overlap", Option.TYPE_STRING));
	options.add(new Option("Crossdate", "Font:",
                        "corina.cross.font", Option.TYPE_FONT));
	options.add(new Option("Crossdate", "Background color:",
                        "corina.cross.background", Option.TYPE_COLOR));
	options.add(new Option("Crossdate", "Text color:",
                        "corina.cross.foreground", Option.TYPE_COLOR));
	options.add(new Option("Crossdate", "Draw Gridlines",
                        "corina.cross.gridlines", Option.TYPE_BOOL));

	options.add(new Option("Crossdate", "Grid Font:",
                        "corina.grid.font", Option.TYPE_FONT));
	options.add(new Option("Crossdate", "Grid on-screen Scale:",
                        "corina.grid.scale", Option.TYPE_HIDDEN));
	options.add(new Option("Crossdate", "Highlight Significant Scores",
                        "corina.grid.highlight", Option.TYPE_BOOL));
	options.add(new Option("Crossdate", "Highlight Color:",
                        "corina.grid.highlightcolor", Option.TYPE_COLOR));

        options.add(new Option("Crossdate", "Date view:",
                               "corina.cross.dating", Option.TYPE_HIDDEN));

        // graph
	options.add(new Option("Graph", "Pixels per year:",
                        "corina.graph.pixelsperyear", Option.TYPE_STRING));
	options.add(new Option("Graph", "Line thickness:",
                        "corina.graph.thickness", Option.TYPE_STRING));
	options.add(new Option("Graph", "Draw Baselines",
                        "corina.graph.baselines", Option.TYPE_BOOL));
	options.add(new Option("Graph", "Draw Sapwood (as thicker line)",
                        "corina.graph.sapwood", Option.TYPE_BOOL));
	options.add(new Option("Graph", "Background Color:",
                        "corina.graph.background", Option.TYPE_COLOR));
	options.add(new Option("Graph", "Axis/Cursor Color:",
                        "corina.graph.foreground", Option.TYPE_COLOR));
	options.add(new Option("Graph", "Draw Graphpaper",
                        "corina.graph.graphpaper", Option.TYPE_BOOL));
	options.add(new Option("Graph", "Graphpaper Color:",
                        "corina.graph.graphpaper.color", Option.TYPE_COLOR));
	options.add(new Option("Graph", "Use Dotted Lines for Indexes",
                        "corina.graph.dotindexes", Option.TYPE_BOOL));

	// advanced
	options.add(new Option("Advanced", "Username:",
                        "user.name", Option.TYPE_STRING));
	options.add(new Option("Advanced", "Data Directory:",
                        "corina.dir.data", Option.TYPE_STRING));
	options.add(new Option("Advanced", "Text Printer Command:",
                        "corina.print.command", Option.TYPE_STRING));
	options.add(new Option("Advanced", "Menubar Font:",
                        "corina.menubar.font", Option.TYPE_FONT));
        options.add(new Option("Advanced", "Browser fields:",
                               "corina.browser.fields", Option.TYPE_HIDDEN));
        options.add(new Option("Advanced", "Browser folder:",
                               "corina.browser.folder", Option.TYPE_HIDDEN));
        options.add(new Option("Advanced", "Browser sort:",
                               "corina.browser.sort", Option.TYPE_HIDDEN));
        options.add(new Option("Advanced", "Browser reverse?",
                               "corina.browser.reverse", Option.TYPE_HIDDEN));
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
