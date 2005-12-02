/**
 * 
 */
package corina.graph;

import corina.core.App;
import java.awt.Color;
import java.awt.Toolkit;

import corina.util.ColorUtils;

/**
 * @author Lucas Madar
 *
 */
public class GraphInfo {
	
	public GraphInfo() {
		reloadPrefs();
	}
	
	public void reloadPrefs() {
		// set some defaults...
		// EXTRACT: should have default value for pref in prefs, not here	
		foreColor = App.prefs.getColorPref("corina.graph.foreground", Color.white);
		majorLineColor = App.prefs.getColorPref("corina.graph.graphpaper.color", new Color(0, 51, 51));
		minorLineColor = ColorUtils.blend(majorLineColor, App.prefs.getColorPref("corina.graph.background", Color.black));
		
		showGraphPaper = Boolean.valueOf(App.prefs.getPref("corina.graph.graphpaper")).booleanValue();
		showBaselines = Boolean.valueOf(App.prefs.getPref("corina.graph.baselines")).booleanValue();
		
		// decide how many pixels per year
		int ppy = Toolkit.getDefaultToolkit().getScreenResolution();
		
		try {
			ppy = Integer.parseInt(App.prefs.getPref(
					"corina.graph.pixelsperyear", Integer.toString(ppy)));
		} catch (NumberFormatException nfe) {
			// do nothing, use the default.
		}
		yearSize = ppy;
	}
	
	// graph visual settings
	boolean showGraphPaper;
	boolean drawGraphPaper() { return showGraphPaper; }
	
	boolean showBaselines;
	boolean drawBaselines() { return showBaselines; }
	
	int yearSize;
	void setYearSize(int size) { yearSize = size; }
	int getYearSize() { return yearSize; }
	
	// color settings
	private Color foreColor;
	private Color majorLineColor;
	private Color minorLineColor;
	
	public Color getMajorLineColor() { return majorLineColor; }
	public Color getMinorLineColor() { return minorLineColor; }
	public Color getForeColor() { return foreColor; }
	
	public void setMajorLineColor(Color c) { majorLineColor = c; }
	public void setMinorLineColor(Color c) { minorLineColor = c; }
	public void setForeColor(Color c) { foreColor = c; }
	

	// Let's contain the color list in here
	// graph color list logic below!
	
	public class colorPair {
		private String colorName;
		private Color colorVal;
		
		String getColorName() { return colorName; }
		Color getColor() { return colorVal; }
		
		public colorPair(String colorName, Color colorVal) {
			this.colorName = colorName;
			this.colorVal = colorVal;
		}
	};
	
	public final colorPair printerColors[] = { 
			new colorPair("Black", Color.BLACK),
			new colorPair("Blue", Color.BLUE),
			new colorPair("Cyan", Color.CYAN),
			new colorPair("Light Gray", Color.lightGray),
			new colorPair("Green", Color.green),
			new colorPair("Magenta", Color.magenta),
			new colorPair("Orange", Color.orange),
			new colorPair("Red", Color.red),
			new colorPair("Dark Gray", Color.darkGray),
			new colorPair("Pink", Color.pink),
			new colorPair("Yellow", Color.yellow),
			new colorPair("Gray", Color.gray) 
			};

	public final colorPair screenColors[] = { 
			new colorPair("Blue", Color.BLUE),
			new colorPair("Cyan", Color.CYAN),
			new colorPair("Light Gray", Color.lightGray),
			new colorPair("Green", Color.green),
			new colorPair("Magenta", Color.magenta),
			new colorPair("Orange", Color.orange),
			new colorPair("Red", Color.red),
			new colorPair("Dark Gray", Color.darkGray),
			new colorPair("Pink", Color.pink),
			new colorPair("Yellow", Color.yellow),
			new colorPair("Gray", Color.gray) 
			};
	
}
