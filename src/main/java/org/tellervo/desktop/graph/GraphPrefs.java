/**
 * 
 */
package org.tellervo.desktop.graph;

import java.awt.Color;
import java.awt.Toolkit;

import org.tellervo.desktop.prefs.PrefHandle;
import org.tellervo.desktop.prefs.PrefHolder;


/**
 * Holds preference names for things in the graph package, as well as defaults
 * 
 * @author Lucas Madar
 *
 */
public final class GraphPrefs extends PrefHolder {
	public final static String PREFIX = "corina.graph.";
	public final static Integer DPI = Toolkit.getDefaultToolkit().getScreenResolution();

	public final static PrefHandle<PlotAgent> PLOT_AGENT = mkEnum(PREFIX, "defaultagent", PlotAgent.class, PlotAgent.STANDARD);
	
	public final static PrefHandle<Color> BACKGROUND = mkPref(PREFIX, "background", Color.black);
	public final static PrefHandle<Color> FOREGROUND = mkPref(PREFIX, "foreground", Color.white);
	public final static PrefHandle<Color> MAJOR_LINE = mkPref(PREFIX, "graphpaper.color", new Color(0, 51, 51));

	public final static PrefHandle<Color> BACKGROUND_PRINT = mkPref(PREFIX, "background", Color.white);
	public final static PrefHandle<Color> FOREGROUND_PRINT = mkPref(PREFIX, "foreground", Color.black);
	public final static PrefHandle<Color> MAJOR_LINE_PRINT = mkPref(PREFIX, "graphpaper.color", new Color(255, 204, 204));
	
	public final static PrefHandle<Boolean> VERTICAL_AXIS = mkPref(PREFIX, "vertical-axis", true);
	public final static PrefHandle<Boolean> GRAPHPAPER = mkPref(PREFIX, "graphpaper", true);
	public final static PrefHandle<Boolean> BASELINES = mkPref(PREFIX, "baselines", true);
	public final static PrefHandle<Boolean> HUNDREDPERCENTLINES = mkPref(PREFIX, "hundredpercentlines", false);
	public final static PrefHandle<Boolean> COMPONENTNAMES = mkPref(PREFIX, "componentnames", false);
	public final static PrefHandle<Boolean> SAPWOOD_THICKER = mkPref(PREFIX, "sapwood", true);
	public final static PrefHandle<Boolean> INDEXES_DOTTED = mkPref(PREFIX, "dotindexes", true);
	
	public final static PrefHandle<Integer> YEAR_WIDTH = mkPref(PREFIX, "pixelsperyear", DPI / 8);
	public final static PrefHandle<Integer> TENUNIT_HEIGHT = mkPref(PREFIX, "pixelspertenunit", DPI / 8);
	
	private GraphPrefs() {
		// don't instantiate this class!
	}
}
