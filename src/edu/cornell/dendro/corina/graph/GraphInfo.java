/**
 * 
 */
package edu.cornell.dendro.corina.graph;

import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.JPanel;

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.util.ColorUtils;

/**
 * @author Lucas Madar
 * 
 */
public class GraphInfo {

	public final static class colorPair {
		private String colorName;
		private Color colorVal;

		public colorPair(String colorName, Color colorVal) {
			this.colorName = colorName;
			this.colorVal = colorVal;
		}

		public Color getColor() {
			return colorVal;
		}

		public String getColorName() {
			return colorName;
		}

		public void setColor(Color color) {
			colorVal = color;
		}
	}

	/** True when printing, false otherwise */
	private boolean printing;

	// color settings
	/** The graph's foreground color */
	private Color foreColor;
	/** The graph's major line color (every 10 years?) */
	private Color majorLineColor;
	/** The graph's midline color (every 5 years?) */
	private Color midLineColor;
	/** The graph's minor line color (every year) */
	private Color minorLineColor;
	/** The graph's background color */
	private Color backgroundColor;

	// important graph stuff...
	/** The total bounds for drawing the graph */
	private Range drawBounds;
	/** The empty range in which graph names are drawn */
	private Range emptyBounds;
	
	// graph visual settings
	/** Should indexes be drawn with dotted lines? */
	private boolean dottedIndexes;
	/** Should sapwood be drawn with thicker lines? */
	private boolean thickerSapwood;

	/** Draw graph paper grid */
	private boolean showGraphPaper;
	/** Draw baselines */
	private boolean showBaselines;
	/** Draw 100% lines (only relevant for indexes) */
	private boolean showHundredpercentlines;

	/** Draw graph names in empty range? */
	private boolean showGraphNames;
	/** Draw vertical axis? */
	private boolean showVertAxis;
	/** Width of one year */
	private int yearWidth;
	/** Height of 10 graph units */
	private int unitHeight;

	/** The height of what we're printing (only used when printing) */
	private int printHeight = 0;

	
	public final static colorPair printerColors[] = {
			new colorPair("Blue", new Color(0.00f, 0.53f, 1.00f)),
			new colorPair("Green", new Color(0.27f, 1.00f, 0.49f)),
			new colorPair("Red", new Color(1.00f, 0.28f, 0.27f)),
			new colorPair("Cyan", new Color(0.22f, 0.80f, 0.82f)),
			new colorPair("Yellow", new Color(0.82f, 0.81f, 0.23f)),
			new colorPair("Magenta", new Color(0.85f, 0.26f, 0.81f)),
			new colorPair("Gray", Color.gray),
			new colorPair("Orange", Color.ORANGE),
			new colorPair("Black", Color.BLACK),
			new colorPair("Pink", Color.PINK) };
	
	public final static colorPair screenColors[] = {
			new colorPair("Blue", new Color(0.00f, 0.53f, 1.00f)),
			new colorPair("Green", new Color(0.27f, 1.00f, 0.49f)),
			new colorPair("Red", new Color(1.00f, 0.28f, 0.27f)),
			new colorPair("Cyan", new Color(0.22f, 0.80f, 0.82f)),
			new colorPair("Yellow", new Color(0.82f, 0.81f, 0.23f)),
			new colorPair("Magenta", new Color(0.85f, 0.26f, 0.81f)),
			new colorPair("Gray", Color.gray),
			new colorPair("Orange", Color.ORANGE),
			new colorPair("White", Color.WHITE),
			new colorPair("Pink", Color.PINK) };

	/**
	 * Construct a new GraphInfo structure for screen display only
	 * Initial values based on preferences
	 */
	public GraphInfo() {
		this(false);
	}

	/**
	 * Construct a new GraphInfo structure
	 * Initial values based on preferences
	 * @param forprinting
	 */
	public GraphInfo(boolean forprinting) {
		this.printing = forprinting;
		
		resetColors();
		initialize();
	}
	
	/**
	 * Construct a new GraphInfo structure based on the settings in src
	 * Copies everything over except for colors, which are loaded from preferences
	 * @param src
	 * @param forprinting
	 */
	public GraphInfo(GraphInfo src, boolean forprinting) {
		resetColors();
		
		this.drawBounds = src.drawBounds;
		this.emptyBounds = src.emptyBounds;
		
		this.printHeight = src.printHeight;
		
		this.dottedIndexes = src.dottedIndexes;
		this.thickerSapwood = src.thickerSapwood;
		
		this.showGraphPaper = src.showGraphPaper;
		this.showBaselines = src.showBaselines;
		this.showHundredpercentlines = src.showHundredpercentlines;
		
		this.showGraphNames = src.showGraphNames;
		this.showVertAxis = src.showVertAxis;
		
		this.yearWidth = src.yearWidth;
		this.unitHeight = src.unitHeight;
	}

	public boolean drawBaselines() {
		return showBaselines;
	}

	public boolean drawGraphNames() {
		return showGraphNames;
	}

	public boolean drawGraphPaper() {
		return showGraphPaper;
	}

	public boolean drawHundredpercentlines() {
		return showHundredpercentlines;
	}

	public boolean drawVertAxis() {
		return showVertAxis;
	}

	public int get10UnitHeight() {
		return unitHeight;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public Color getBLCenterColor() {
		return ColorUtils.blend(minorLineColor, foreColor);
	}

	public Range getDrawRange() {
		return drawBounds;
	}

	public Range getEmptyRange() {
		return emptyBounds;
	}

	public Color getForeColor() {
		return foreColor;
	}

	public int getHeight(JPanel panel) {
		if (printing)
			return printHeight;
		return panel.getHeight();
	}

	public Color getMajorLineColor() {
		return majorLineColor;
	}

	public Color getMidLineColor() {
		return midLineColor;
	}

	public Color getMinorLineColor() {
		return minorLineColor;
	}

	// gets a printing version of this...
	// a method here in case it gets more complicated than just constructing it
	// with printing on
	public GraphInfo getPrinter() {
		return new GraphInfo(this, true);
	}

	public int getPrintHeight() {
		return printHeight;
	}

	public int getYearWidth() {
		return yearWidth;
	}

	public boolean indexesDotted() {
		return dottedIndexes;
	}

	// OVERRIDES
	// These are to be used ONLY by utilites that draw graphs
	// They allow changes to the graphInfo without saving preferences!
	public void overrideDrawGraphNames(boolean value) {
		showGraphNames = value;
	}

	public void overrideShowVertAxis(boolean value) {
		showVertAxis = value;
	}

	/**
	 * Reset graph colors to their default values
	 */
	public void resetColors() {
		if (!printing) {
			backgroundColor = GraphPrefs.BACKGROUND.get();
			foreColor = GraphPrefs.FOREGROUND.get();
			majorLineColor = GraphPrefs.MAJOR_LINE.get();
			minorLineColor = ColorUtils.blend(majorLineColor, backgroundColor);
			midLineColor = ColorUtils.blend(majorLineColor, minorLineColor);
		} else {
			backgroundColor = GraphPrefs.BACKGROUND_PRINT.get();
			foreColor = GraphPrefs.FOREGROUND_PRINT.get();
			majorLineColor = GraphPrefs.MAJOR_LINE_PRINT.get();
			minorLineColor = ColorUtils.blend(majorLineColor, backgroundColor);
			midLineColor = ColorUtils.blend(majorLineColor, minorLineColor);			
		}
	}

	/** 
	 * Set up the default values from prefs
	 */
	private void initialize()
	{
		showVertAxis = GraphPrefs.VERTICAL_AXIS.get();
		showGraphPaper = GraphPrefs.GRAPHPAPER.get();
		showBaselines = GraphPrefs.BASELINES.get();
		showHundredpercentlines = GraphPrefs.HUNDREDPERCENTLINES.get();
		showGraphNames = GraphPrefs.COMPONENTNAMES.get();
		thickerSapwood = GraphPrefs.SAPWOOD_THICKER.get();
		dottedIndexes = GraphPrefs.INDEXES_DOTTED.get();

		yearWidth = GraphPrefs.YEAR_WIDTH.get();
		unitHeight = GraphPrefs.TENUNIT_HEIGHT.get();
	}

	public boolean sapwoodThicker() {
		return thickerSapwood;
	}

	public void set10UnitHeight(int size) {
		unitHeight = size;
	}

	public void setDrawRange(Range r) {
		drawBounds = r;
	}

	public void setEmptyRange(Range r) {
		emptyBounds = r;
	}

	public void setForeColor(Color c) {
		foreColor = c;
	}

	public void setMajorLineColor(Color c) {
		majorLineColor = c;
	}

	public void setMinorLineColor(Color c) {
		minorLineColor = c;
	}

	public void setPrintHeight(int h) {
		printHeight = h;
	}

	public void setYearWidth(int size) {
		yearWidth = size;
	}

	boolean isPrinting() {
		return printing;
	}

	void setPrinting(boolean isprinting) {
		printing = isprinting;
	}
}
