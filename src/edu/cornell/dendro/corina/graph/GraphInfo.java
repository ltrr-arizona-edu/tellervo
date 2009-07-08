/**
 * 
 */
package edu.cornell.dendro.corina.graph;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JPanel;

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.util.ColorUtils;

/**
 * This class describes the settings of a graph
 * 
 * @author Lucas Madar
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

	/** For handling property changes */
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);
	
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
	private int tenUnitHeight;

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
		this.tenUnitHeight = src.tenUnitHeight;
	}

	/**
	 * Acquire a printing version of this graphInfo
	 * 
	 * @return a GraphInfo copy for printing
	 */
	public GraphInfo getPrinter() {
		return new GraphInfo(this, true);
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
		tenUnitHeight = GraphPrefs.TENUNIT_HEIGHT.get();
	}

	/**
	 * 
	 * @return true if this is a printing graphinfo
	 */
	public boolean isPrinting() {
		return printing;
	}
	
	/**
	 * @return the foreColor
	 */
	public Color getForeColor() {
		return foreColor;
	}

	/**
	 * @param foreColor the foreColor to set
	 */
	public void setForeColor(Color foreColor) {
		Color oldValue = this.foreColor;
		this.foreColor = foreColor;
		changes.firePropertyChange(FORE_COLOR_PROPERTY, oldValue, foreColor);
	}

	/**
	 * @return the majorLineColor
	 */
	public Color getMajorLineColor() {
		return majorLineColor;
	}

	/**
	 * @param majorLineColor the majorLineColor to set
	 */
	public void setMajorLineColor(Color majorLineColor) {
		Color oldValue = this.majorLineColor;
		this.majorLineColor = majorLineColor;
		changes.firePropertyChange(MAJOR_LINE_COLOR_PROPERTY, oldValue, majorLineColor);	
	}

	/**
	 * @return the midLineColor
	 */
	public Color getMidLineColor() {
		return midLineColor;
	}

	/**
	 * @param midLineColor the midLineColor to set
	 */
	public void setMidLineColor(Color midLineColor) {
		Color oldValue = this.midLineColor;
		this.midLineColor = midLineColor;
		changes.firePropertyChange(MID_LINE_COLOR_PROPERTY, oldValue, midLineColor);	
	}

	/**
	 * @return the minorLineColor
	 */
	public Color getMinorLineColor() {
		return minorLineColor;
	}

	/**
	 * @param minorLineColor the minorLineColor to set
	 */
	public void setMinorLineColor(Color minorLineColor) {
		Color oldValue = this.minorLineColor;
		this.minorLineColor = minorLineColor;
		changes.firePropertyChange(MINOR_LINE_COLOR_PROPERTY, oldValue, minorLineColor);
	}

	/**
	 * @return the backgroundColor
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * @param backgroundColor the backgroundColor to set
	 */
	public void setBackgroundColor(Color backgroundColor) {
		Color oldValue = this.backgroundColor;
		this.backgroundColor = backgroundColor;
		changes.firePropertyChange(BACKGROUND_COLOR_PROPERTY, oldValue, backgroundColor);
	}

	/**
	 * @return the drawBounds
	 */
	public Range getDrawBounds() {
		return drawBounds;
	}

	/**
	 * @param drawBounds the drawBounds to set
	 */
	public void setDrawBounds(Range drawBounds) {
		Range oldValue = this.drawBounds;
		this.drawBounds = drawBounds;
		changes.firePropertyChange(DRAW_BOUNDS_PROPERTY, oldValue, drawBounds);	
	}

	/**
	 * @return the emptyBounds
	 */
	public Range getEmptyBounds() {
		return emptyBounds;
	}

	/**
	 * @param emptyBounds the emptyBounds to set
	 */
	public void setEmptyBounds(Range emptyBounds) {
		Range oldValue = this.emptyBounds;
		this.emptyBounds = emptyBounds;
		changes.firePropertyChange(EMPTY_BOUNDS_PROPERTY, oldValue, emptyBounds);	
	}

	/**
	 * @return the dottedIndexes
	 */
	public boolean isDottedIndexes() {
		return dottedIndexes;
	}

	/**
	 * @param dottedIndexes the dottedIndexes to set
	 */
	public void setDottedIndexes(boolean dottedIndexes) {
		boolean oldValue = this.dottedIndexes;
		this.dottedIndexes = dottedIndexes;
		changes.firePropertyChange(DOTTED_INDEXES_PROPERTY, oldValue, dottedIndexes);
	}

	/**
	 * @return the thickerSapwood
	 */
	public boolean isThickerSapwood() {
		return thickerSapwood;
	}

	/**
	 * @param thickerSapwood the thickerSapwood to set
	 */
	public void setThickerSapwood(boolean thickerSapwood) {
		boolean oldValue = this.thickerSapwood;
		this.thickerSapwood = thickerSapwood;
		changes.firePropertyChange(THICKER_SAPWOOD_PROPERTY, oldValue, thickerSapwood);
	}

	/**
	 * @return the showGraphPaper
	 */
	public boolean isShowGraphPaper() {
		return showGraphPaper;
	}

	/**
	 * @param showGraphPaper the showGraphPaper to set
	 */
	public void setShowGraphPaper(boolean showGraphPaper) {
		boolean oldValue = this.showGraphPaper;
		this.showGraphPaper = showGraphPaper;
		changes.firePropertyChange(SHOW_GRAPH_PAPER_PROPERTY, oldValue, showGraphPaper);
	}

	/**
	 * @return the showBaselines
	 */
	public boolean isShowBaselines() {
		return showBaselines;
	}

	/**
	 * @param showBaselines the showBaselines to set
	 */
	public void setShowBaselines(boolean showBaselines) {
		boolean oldValue = this.showBaselines;
		this.showBaselines = showBaselines;
		changes.firePropertyChange(SHOW_BASELINES_PROPERTY, oldValue, showBaselines);
	}

	/**
	 * @return the showHundredpercentlines
	 */
	public boolean isShowHundredpercentlines() {
		return showHundredpercentlines;
	}

	/**
	 * @param showHundredpercentlines the showHundredpercentlines to set
	 */
	public void setShowHundredpercentlines(boolean showHundredpercentlines) {
		boolean oldValue = this.showHundredpercentlines;
		this.showHundredpercentlines = showHundredpercentlines;
		changes.firePropertyChange(SHOW_HUNDREDPERCENTLINES_PROPERTY, oldValue, showHundredpercentlines);
	}

	/**
	 * @return the showGraphNames
	 */
	public boolean isShowGraphNames() {
		return showGraphNames;
	}

	/**
	 * @param showGraphNames the showGraphNames to set
	 */
	public void setShowGraphNames(boolean showGraphNames) {
		boolean oldValue = this.showGraphNames;
		this.showGraphNames = showGraphNames;
		changes.firePropertyChange(SHOW_GRAPH_NAMES_PROPERTY, oldValue, showGraphNames);
	}

	/**
	 * @return the showVertAxis
	 */
	public boolean isShowVertAxis() {
		return showVertAxis;
	}

	/**
	 * @param showVertAxis the showVertAxis to set
	 */
	public void setShowVertAxis(boolean showVertAxis) {
		boolean oldValue = this.showVertAxis;
		this.showVertAxis = showVertAxis;
		changes.firePropertyChange(SHOW_VERT_AXIS_PROPERTY, oldValue, showVertAxis);
	}

	/**
	 * @return the yearWidth
	 */
	public int getYearWidth() {
		return yearWidth;
	}

	/**
	 * @param yearWidth the yearWidth to set
	 */
	public void setYearWidth(int yearWidth) {
		int oldValue = this.yearWidth;
		this.yearWidth = yearWidth;
		changes.firePropertyChange(YEAR_WIDTH_PROPERTY, oldValue, yearWidth);
	}

	/**
	 * @return the unitHeight
	 */
	public int getTenUnitHeight() {
		return tenUnitHeight;
	}

	/**
	 * @param unitHeight the unitHeight to set
	 */
	public void setTenUnitHeight(int tenUnitHeight) {
		int oldValue = this.tenUnitHeight;
		this.tenUnitHeight = tenUnitHeight;
		changes.firePropertyChange(TEN_UNIT_HEIGHT_PROPERTY, oldValue, tenUnitHeight);
	}

	/**
	 * @return the printHeight
	 */
	public int getPrintHeight() {
		return printHeight;
	}

	/**
	 * @param printHeight the printHeight to set
	 */
	public void setPrintHeight(int printHeight) {
		int oldValue = this.printHeight;
		this.printHeight = printHeight;
		changes.firePropertyChange(PRINT_HEIGHT_PROPERTY, oldValue, printHeight);
	}

	/**
	 * Get the graph height (via the panel, or the printHeight if printing)
	 * @param panel
	 * @return the height, in pixels
	 */
	public int getGraphHeight(JPanel panel) {
		return printing ? this.printHeight : panel.getHeight();
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changes.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changes.removePropertyChangeListener(listener);
	}
	
	/** The property name for printing */
	public static final String PRINTING_PROPERTY = "graphInfo.printing";

	/** The property name for foreColor */
	public static final String FORE_COLOR_PROPERTY = "graphInfo.foreColor";

	/** The property name for majorLineColor */
	public static final String MAJOR_LINE_COLOR_PROPERTY = "graphInfo.majorLineColor";

	/** The property name for midLineColor */
	public static final String MID_LINE_COLOR_PROPERTY = "graphInfo.midLineColor";

	/** The property name for minorLineColor */
	public static final String MINOR_LINE_COLOR_PROPERTY = "graphInfo.minorLineColor";

	/** The property name for backgroundColor */
	public static final String BACKGROUND_COLOR_PROPERTY = "graphInfo.backgroundColor";

	/** The property name for drawBounds */
	public static final String DRAW_BOUNDS_PROPERTY = "graphInfo.drawBounds";

	/** The property name for emptyBounds */
	public static final String EMPTY_BOUNDS_PROPERTY = "graphInfo.emptyBounds";

	/** The property name for dottedIndexes */
	public static final String DOTTED_INDEXES_PROPERTY = "graphInfo.dottedIndexes";

	/** The property name for thickerSapwood */
	public static final String THICKER_SAPWOOD_PROPERTY = "graphInfo.thickerSapwood";

	/** The property name for showGraphPaper */
	public static final String SHOW_GRAPH_PAPER_PROPERTY = "graphInfo.showGraphPaper";

	/** The property name for showBaselines */
	public static final String SHOW_BASELINES_PROPERTY = "graphInfo.showBaselines";

	/** The property name for showHundredpercentlines */
	public static final String SHOW_HUNDREDPERCENTLINES_PROPERTY = "graphInfo.showHundredpercentlines";

	/** The property name for showGraphNames */
	public static final String SHOW_GRAPH_NAMES_PROPERTY = "graphInfo.showGraphNames";

	/** The property name for showVertAxis */
	public static final String SHOW_VERT_AXIS_PROPERTY = "graphInfo.showVertAxis";

	/** The property name for yearWidth */
	public static final String YEAR_WIDTH_PROPERTY = "graphInfo.yearWidth";

	/** The property name for tenUnitHeight */
	public static final String TEN_UNIT_HEIGHT_PROPERTY = "graphInfo.tenUnitHeight";

	/** The property name for printHeight */
	public static final String PRINT_HEIGHT_PROPERTY = "graphInfo.printHeight";

	/** The property name for printerColors */
	public static final String PRINTER_COLORS_PROPERTY = "graphInfo.printerColors";

	/** The property name for screenColors */
	public static final String SCREEN_COLORS_PROPERTY = "graphInfo.screenColors";	
}
