/**
 * 
 */
package corina.graph;

import corina.core.App;
import corina.Range;
import java.awt.Color;
import java.awt.Toolkit;

import corina.util.ColorUtils;
import javax.swing.JPanel;

/**
 * @author Lucas Madar
 *
 */
public class GraphInfo {
	
	boolean printing;
	
	public GraphInfo(boolean forprinting)
	{
		giInit(forprinting);
	}
	
	public GraphInfo() {
		giInit(false);
	}
	
	private void giInit(boolean forprinting) {
		printing = forprinting;
		reloadPrefs();
	}

	// gets a printing version of this...
	// a method here in case it gets more complicated than just constructing it with printing on
	public GraphInfo getPrinter() {
		return new GraphInfo(true);
	}
	
	void setPrinting(boolean isprinting) { printing = isprinting; }
	boolean isPrinting() { return printing; }
	
	// color settings
	private Color foreColor;
	private Color majorLineColor;
	private Color minorLineColor;
	private Color backgroundColor;
	
	
	public void reloadPrefs() {
		// set some defaults...
		
		// EXTRACT: should have default value for pref in prefs, not here	
		
		if(!printing) {
			backgroundColor = App.prefs.getColorPref("corina.graph.background", Color.black);
			foreColor = App.prefs.getColorPref("corina.graph.foreground", Color.white);
			majorLineColor = App.prefs.getColorPref("corina.graph.graphpaper.color", new Color(0, 51, 51));
			minorLineColor = ColorUtils.blend(majorLineColor, 
				App.prefs.getColorPref("corina.graph.background", backgroundColor));
		}
		else {
			backgroundColor = App.prefs.getColorPref("corina.graph.print.background", Color.white);
			foreColor = App.prefs.getColorPref("corina.graph.print.foreground", Color.black);
			majorLineColor = App.prefs.getColorPref("corina.graph.print.graphpaper.color", new Color(255, 204, 204));
			minorLineColor = ColorUtils.blend(majorLineColor, 
				App.prefs.getColorPref("corina.graph.print.background", backgroundColor));			
		}

		showVertAxis = Boolean.valueOf(App.prefs.getPref("corina.graph.vertical-axis")).booleanValue();
		showGraphPaper = Boolean.valueOf(App.prefs.getPref("corina.graph.graphpaper")).booleanValue();
		showBaselines = Boolean.valueOf(App.prefs.getPref("corina.graph.baselines")).booleanValue();
		showGraphNames = Boolean.valueOf(App.prefs.getPref("corina.graph.componentnames")).booleanValue();
		
		// decide how many pixels per year
		// the default is DPI / 8 (screen DPI is typically 72, so the default is typically 9)
		int ppy = Toolkit.getDefaultToolkit().getScreenResolution() / 8;
		
		try {
			ppy = Integer.parseInt(App.prefs.getPref(
					"corina.graph.pixelsperyear", Integer.toString(ppy)));
		} catch (NumberFormatException nfe) {
			// do nothing, use the default.
		}
		yearSize = ppy;
	}
	// important graph stuff...
	private Range drawBounds;
	private Range emptyBounds;
	
	public void setEmptyRange(Range r) { emptyBounds = r; }
	public void setDrawRange(Range r) { drawBounds = r; }
	public Range getEmptyRange() { return emptyBounds; }
	public Range getDrawRange() { return drawBounds; }
	
	private int printHeight = 0;
	public int getPrintHeight() { return printHeight; }
	public void setPrintHeight(int h) { printHeight = h; }
	
	public int getHeight(JPanel panel) {
		if(printing)
			return printHeight;
		return panel.getHeight();
	}
	
	// graph visual settings
	private boolean showGraphPaper;
	public boolean drawGraphPaper() { return showGraphPaper; }
	
	private boolean showBaselines;
	public boolean drawBaselines() { return showBaselines; }
	
	private boolean showGraphNames;
	public boolean drawGraphNames() { return showGraphNames; }
	
	private boolean showVertAxis;
	public boolean drawVertAxis() { return showVertAxis; }
	
	private int yearSize;
	public void setYearSize(int size) { yearSize = size; }
	public int getYearSize() { return yearSize; }


	public Color getBackgroundColor() { 
		return backgroundColor; 
	}
	public Color getMajorLineColor() { 
		return majorLineColor; 
	}
	public Color getMinorLineColor() { 
		return minorLineColor; 
	}
	public Color getForeColor() { 
		return foreColor;
	}
	
	public Color getBLCenterColor() { 
		return ColorUtils.blend(minorLineColor, foreColor); 
	}
	
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
