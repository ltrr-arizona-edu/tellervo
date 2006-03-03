/**
 * 
 */
package corina.editor;

import javax.swing.*;
import java.awt.Rectangle;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextPane;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Date;

import javax.swing.JScrollPane;
import javax.swing.text.*;

import java.awt.FlowLayout;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.Printable;
import java.awt.print.PageFormat;

import corina.Sample;
import corina.Weiserjahre;
import corina.Year;
import corina.print.EmptyLine;
import corina.print.Line;
import corina.print.TabbedLineFactory;
import corina.print.TextLine;
import corina.ui.I18n;
import corina.util.StringUtils;

/**
 * @author Lucas Madar
 *
 */
public class SamplePrintEditor extends JPanel {

	private SPTextPane textpane;
	private Sample s;
	private Printable printable;
	/**
	 * 
	 */

	/*
	 * The following three classes allow us to change the default paragraph view
	 * assigned to new paragraphs in the JTextPane.
	 * 
	 * This allows us to change the characters that indicate "decimal point", for
	 * decimal point tab stops. Makes Weiserjahre data line up nicely.
	 */
	private class SPEditorKit extends StyledEditorKit {
	    public ViewFactory getViewFactory() {
	    	return new SPViewFactory();
	    }
	}

	class SPViewFactory implements ViewFactory {
		public View create(Element elem) {
			String kind = elem.getName();
			if (kind != null)
				if (kind.equals(AbstractDocument.ContentElementName)) {
					return new LabelView(elem);
				} else if (kind.equals(AbstractDocument.ParagraphElementName)) {
					// We changed this so we can mess with tab stops
					return new SPParagraphView(elem);
				} else if (kind.equals(AbstractDocument.SectionElementName)) {
					return new BoxView(elem, View.Y_AXIS);
					// we changed this so we can zoom in on text
					//return new ScaledBoxView(elem, View.Y_AXIS);
				} else if (kind.equals(StyleConstants.ComponentElementName)) {
					return new ComponentView(elem);
				} else if (kind.equals(StyleConstants.IconElementName)) {
					return new IconView(elem);
				}
			// default to text display
			return new LabelView(elem);
		}
	}
	
	/*
	private class ScaledBoxView extends BoxView {
		private double zoomFactor = 2.0f;
	    public ScaledBoxView(Element elem, int axis) {
	        super(elem,axis);
	    }
	    
	    public double getZoomFactor() {
	    	return zoomFactor;
	    }
	    
	    public void setZoomFactor(double zoom) {
	    	zoomFactor = zoom;
	    }
	 
	    public void paint(Graphics g, Shape allocation) {
	        Graphics2D g2d = (Graphics2D)g;
	        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
	                             RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	        double zoomFactor = getZoomFactor();
	        AffineTransform old=g2d.getTransform();
	        g2d.scale(zoomFactor, zoomFactor);
	        super.paint(g2d, allocation);
	        g2d.setTransform(old);
	    }
	 
	    public float getMinimumSpan(int axis) {
	    	return (float) (super.getMinimumSpan(axis) * getZoomFactor());
	    }
	 
	    public float getMaximumSpan(int axis) {
	    	return (float) (super.getMaximumSpan(axis) * getZoomFactor());
	    }
	 
	    public float getPreferredSpan(int axis) {
	    	return (float) (super.getPreferredSpan(axis) * getZoomFactor());
	    }
	 
	    public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException {
	        double zoomFactor = getZoomFactor();
	        Rectangle alloc;
	        alloc = a.getBounds();
	        Shape s = super.modelToView(pos, alloc, b);
	        alloc = s.getBounds();
	        alloc.x*=zoomFactor;
	        alloc.y*=zoomFactor;
	        alloc.width*=zoomFactor;
	        alloc.height*=zoomFactor;
	 
	        return alloc;
	    }
	 
	 
	    public int viewToModel(float x, float y, Shape a, Position.Bias[] bias) {
	        double zoomFactor = getZoomFactor();
	        Rectangle alloc = a.getBounds();
	        x/=zoomFactor;
	        y/=zoomFactor;
	        alloc.x/=zoomFactor;
	        alloc.y/=zoomFactor;
	        alloc.width/=zoomFactor;
	        alloc.height/=zoomFactor;
	 
	        return super.viewToModel(x, y, alloc, bias);
	    }
	 
	    protected void layout(int width, int height) {
	        super.layout(
	        		new Double(width/getZoomFactor()).intValue(),
	        		new Double(height*getZoomFactor()).intValue());
	    }
	 
	}
	*/
	
	private class SPParagraphView extends ParagraphView {
		char mutantTabDecimalChars[];
		char mutantTabChars[];
		
		public SPParagraphView(Element elem) {
			super(elem);
			
			mutantTabDecimalChars = new char[4];
			mutantTabDecimalChars[0] = '\t';
			mutantTabDecimalChars[1] = '.';
			mutantTabDecimalChars[2] = '*';
			mutantTabDecimalChars[3] = '/';
			
			mutantTabChars = new char[1];
			mutantTabChars[0] = '\t';
		}
			    
	    public float nextTabStop(float x, int tabOffset) {
	    	float tabBase = this.getTabBase();

			// If the text isn't left justified, offset by 10 pixels!
	    	if(createRow().getAlignment(View.X_AXIS) != 0)
	    		return x + 10.0f;
	    	/*
	    	 * This hack mimics the following, which is a private variable! ARGH!
	    	 * 
			 * if (justification != StyleConstants.ALIGN_LEFT)
			 * 	return x + 10.0f;
			 */
	    	
			x -= tabBase;
			TabSet tabs = getTabSet();
			if (tabs == null) {
				// a tab every 72 pixels.
				return (float) (tabBase + (((int) x / 72 + 1) * 72));
			}
			TabStop tab = tabs.getTabAfter(x + .01f);
			if (tab == null) {
				// no tab, do a default of 5 pixels.
				// Should this cause a wrapping of the line?
				return tabBase + x + 5.0f;
			}
			int alignment = tab.getAlignment();
			int offset;
			switch (alignment) {
			default:
			case TabStop.ALIGN_LEFT:
				// Simple case, left tab.
				return tabBase + tab.getPosition();
			case TabStop.ALIGN_BAR:
				// PENDING: what does this mean?
				return tabBase + tab.getPosition();
			case TabStop.ALIGN_RIGHT:
			case TabStop.ALIGN_CENTER:
				offset = findOffsetToCharactersInString(mutantTabChars, tabOffset + 1);
				break;
			case TabStop.ALIGN_DECIMAL:
				offset = findOffsetToCharactersInString(mutantTabDecimalChars,
						tabOffset + 1);
				break;
			}
			if (offset == -1) {
				offset = getEndOffset();
			}
			float charsSize = getPartialSize(tabOffset + 1, offset);
			switch (alignment) {
			case TabStop.ALIGN_RIGHT:
			case TabStop.ALIGN_DECIMAL:
				// right and decimal are treated the same way, the new
				// position will be the location of the tab less the
				// partialSize.
				return tabBase + Math.max(x, tab.getPosition() - charsSize);
			case TabStop.ALIGN_CENTER:
				// Similar to right, but half the partialSize.
				return tabBase
						+ Math.max(x, tab.getPosition() - charsSize / 2.0f);
			}
			// will never get here!
			return x;
		}
		
	}
	
	private class SPTextPane extends JTextPane implements Printable {
		public SPTextPane() {
			super();
			this.setEditorKit(new SPEditorKit());
		}
		
		public void append(String style, String text) {
	        StyledDocument doc = (StyledDocument)getDocument();
	        try {
	        	int oldLength = doc.getLength();
	        	doc.insertString(oldLength, text, null);
	        	int newLength = doc.getLength();
	        	doc.setParagraphAttributes(oldLength, newLength - oldLength, getStyle(style), false);
	        } catch (BadLocationException e) {} 
		}
		public void paintComponent(Graphics g) {
			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON);
			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_RENDERING, 
					RenderingHints.VALUE_RENDER_QUALITY);
			super.paintComponent(g);
		}
		
		protected int printingPage = -1;
		protected double pageEndY = 0;
		protected double pageStartY = 0;
		public int print(Graphics g, PageFormat pageFormat, int pagenum) {
			Graphics2D g2 = (Graphics2D) g;
			View root;
			
			textpane.setSize((int)pageFormat.getImageableWidth(), Integer.MAX_VALUE);
			textpane.validate();
			
			root = textpane.getUI().getRootView(textpane);
			
			g2.setClip((int)pageFormat.getImageableX(), (int) pageFormat.getImageableY(),
					(int)pageFormat.getImageableWidth(), (int) pageFormat.getImageableHeight());
			
			if(pagenum > printingPage) {
				printingPage = pagenum;
				pageStartY += pageEndY;
				pageEndY = g2.getClipBounds().getHeight();
			}
			
			g2.translate(g2.getClipBounds().getX(), g2.getClipBounds().getY());
			
			Rectangle alloc = new Rectangle(0, (int) -pageStartY,
					(int) (textpane.getMinimumSize().getWidth()),
					(int) (textpane.getPreferredSize().getHeight()));
			
			if(printView(g2, alloc, root)) {
				return Printable.PAGE_EXISTS;
			}
			else {
				pageStartY = 0;
				pageEndY = 0;
				printingPage = -1;
				return Printable.NO_SUCH_PAGE;
			}
			
			/*
			if(pagenum > 1)
				return Printable.NO_SUCH_PAGE;
			
	        ((Graphics2D)g).translate(pageFormat.getImageableX(), pageFormat.getImageableY());
	        RepaintManager currentManager = RepaintManager.currentManager(this);
	        currentManager.setDoubleBufferingEnabled(false);
			paint(g);
	        currentManager.setDoubleBufferingEnabled(true);			
			return Printable.PAGE_EXISTS;
			*/
		}
		protected boolean printView(Graphics2D graphics2D, Shape allocation, View view)
		{
			//This function paints the page if it exists
	 
			boolean pageExists = false;
			Rectangle clipRectangle = graphics2D.getClipBounds();
			Shape childAllocation;
			View childView;
	 
			if(view.getViewCount() > 0)
			{
	 			for(int i = 0;i<view.getViewCount();i++)
				{
	 				childAllocation = view.getChildAllocation(i,allocation);
					if (childAllocation != null)
					{
						childView = view.getView(i);
	 
						if(printView(graphics2D,childAllocation,childView)) 
						{
							pageExists = true;
						}	 
					}
				}
			}
			else 
			{
				//The below if statement checks if there are pages currently to paint
	 
				if(allocation.getBounds().getMaxY() >= clipRectangle.getY())
				{
					pageExists = true;
	 
					if((allocation.getBounds().getHeight() > clipRectangle.getHeight()) &&
					(allocation.intersects(clipRectangle)))
					{
						view.paint(graphics2D,allocation);
					}
	 
					else
					{
	 
						if(allocation.getBounds().getY() >= clipRectangle.getY())
						{
	 
							if(allocation.getBounds().getMaxY() <= clipRectangle.getMaxY() - 15)
							{
								view.paint(graphics2D,allocation);
							}
	 
							else
							{
	 
								if(allocation.getBounds().getY() < pageEndY)
								{
									pageEndY = allocation.getBounds().getY();
								}
	 
							}
	 
						}
	 
					}
	 
				}
	 
			}
	 
			return pageExists;
		}		
	}
	
	public SamplePrintEditor(Sample sample, SampleBit bits, JFrame parent, int width) {
		super();
		
		this.s = sample;
		
		System.out.println("width: " + width);
		
		final JDialog d = new JDialog(parent, "Sample print preview editor", true);
		d.setContentPane(this);
		setLayout(new BorderLayout());
		
		textpane = new SPTextPane();
		textpane.setMinimumSize(new Dimension(width, 300));
		textpane.setPreferredSize(new Dimension(width, 600));
		
		add(new JScrollPane(textpane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
				
		// create our various styles
		createStyles();	
		makeTabs(width);
		
		// insert things using our styles
		addBits(bits);
		
		JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
	    String oktext = corina.ui.I18n.getText("ok");
	    if (oktext == null) oktext = "Ok";
	    JButton okButton = new JButton(oktext);
	    buttonpanel.add(okButton);
	    okButton.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent ae) {
	    	  d.dispose();
	    	  printable = textpane;
	      }
	    });
	    
	    String canceltext = corina.ui.I18n.getText("cancel");
	    if (canceltext == null) canceltext = "Cancel";
	    JButton cancelButton = new JButton(canceltext);
	    buttonpanel.add(cancelButton);
	    cancelButton.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent ae) {
	    	  d.dispose();
	    	  printable = null;
	      }
	    });
	    
		
		add(buttonpanel, BorderLayout.SOUTH);
				
		d.pack();
		d.setVisible(true);
	}
	
	public Printable getPrintable() {
		return printable;
	}
		
	private void addBits(SampleBit bits) {
		StringBuffer sb = new StringBuffer();
		int lines = 0;
		
		if(bits.wantTitle()) {
			sb.setLength(0);
			sb.append(s.meta.get("title").toString());
			
			sb.append("\n");
			textpane.append("title", sb.toString());
			lines++;
		}
		
		if(bits.wantPrintInfo()) {
	        Date date = new Date();
	        String dateString = DateFormat.getDateInstance().format(date);
	        String timeString = DateFormat.getTimeInstance().format(date);

	        Object args[] = new Object[] {
	        		System.getProperty("user.name", "(unknown user)"),
	        		dateString,
	        		timeString,
	        };

			sb.setLength(0);	        
	        sb.append(MessageFormat.format(I18n.getText("printed_by"), args));
	        
	        sb.append("\n");
			textpane.append("print info", sb.toString());
			lines++;
		}

		// get a blank line in here...
		if(lines != 0)
			textpane.append("default", "\n");
		
		if(bits.wantSampleHeader() && !s.isIndexed()) {
	        // print radius, avg width, but only for non-indexed samples
	        float radius = s.computeRadius() / 1000f;
	        float average = radius / s.data.size();
	        DecimalFormat df = new DecimalFormat("0.000"); // to 3 places

	        sb.setLength(0);
	        sb.append("Radius: " + df.format(radius) + " cm, " +
                                   "Average ring width: " + df.format(average) + " cm\n\n");
            textpane.append("sample header", sb.toString());
		}
		
		if(bits.wantSampleData()) {
			if(s.isSummed()) {
				textpane.append("summed data", getSummed());
				textpane.append("summed data info", getSummedInfo());
			}
			else
				textpane.append("raw data", getRawData());
		}
		
		if(bits.wantMetaData()) {
			textpane.append("metadata", getMetadata());
		}
		
		if(bits.wantWeiserjahre() && s.hasWeiserjahre()) {
			textpane.append("section heading", "Weiserjahre Data\n");
			textpane.append("weiserjahre data", getWeiserjahre());
			textpane.append("weiserjahre data info", getWeiserjahreInfo());
		}
		
		if(bits.wantElements() && s.elements != null) {
			textpane.append("elements", getElements());
		}
	}

	// creates all the default styles we use in our jtextpane
	private void createStyles() {
		Style s;
		
		s = textpane.addStyle("default", null);
		
		s = textpane.addStyle("title", null);
		StyleConstants.setFontSize(s, TITLE_SIZE);
		StyleConstants.setFontFamily(s, "Serif");
		
		s = textpane.addStyle("section heading", null);
		StyleConstants.setFontSize(s, SECTION_SIZE);
		StyleConstants.setFontFamily(s, "Serif");		

		s = textpane.addStyle("print info", null);
		StyleConstants.setFontSize(s, NORMAL_SIZE);
		StyleConstants.setFontFamily(s, "Serif");

		s = textpane.addStyle("sample header", null);
		StyleConstants.setFontSize(s, NORMAL_SIZE);
		StyleConstants.setFontFamily(s, "Serif");
		
		s = textpane.addStyle("summed data", null);
		StyleConstants.setFontSize(s, TINY_SIZE);
		StyleConstants.setFontFamily(s, "Serif");

		s = textpane.addStyle("summed data info", null);
		StyleConstants.setFontSize(s, NORMAL_SIZE);
		StyleConstants.setFontFamily(s, "Serif");

		s = textpane.addStyle("raw data", null);
		StyleConstants.setFontSize(s, NORMAL_SIZE);
		StyleConstants.setFontFamily(s, "Serif");

		s = textpane.addStyle("metadata", null);
		StyleConstants.setFontSize(s, NORMAL_SIZE);
		StyleConstants.setFontFamily(s, "Serif");
		
		s = textpane.addStyle("weiserjahre data info", null);
		StyleConstants.setFontSize(s, NORMAL_SIZE);
		StyleConstants.setFontFamily(s, "Serif");

		s = textpane.addStyle("weiserjahre data", null);
		StyleConstants.setFontSize(s, NORMAL_SIZE);
		StyleConstants.setFontFamily(s, "Serif");		

		s = textpane.addStyle("elements", null);
		StyleConstants.setFontSize(s, NORMAL_SIZE);
		StyleConstants.setFontFamily(s, "Serif");		
	}
	
	// creates all the tabstops we use in our jtextpane, adding them to the existing styles.
	private void makeTabs(int width) {
		Style style;
		StringBuffer sb = new StringBuffer();
		TabSet tabset;

		// build the summed data tab specification
		sb.setLength(0);
		sb.append("< 4%");
		for(int i = 0; i < 10; i++) {
			sb.append(" 5.7%"); // data
		}
		sb.append(" 3.3%");
		for (int i = 0; i < 10; i++)
			sb.append(" 3.4%"); // count
		/*
		sb.append("> 4% <");
		for (int i = 0; i < 10; i++)
			sb.append(" 5.7%"); // data
		sb.append(" 5%");
		for (int i = 0; i < 10; i++)
			sb.append(" 3.4%"); // count
			*/

		style = textpane.getStyle("summed data");
		tabset = EditorTabSetFactory.buildTabset(sb.toString(), width);
		StyleConstants.setTabSet(style, tabset);
				
		// raw data and weiserjahre are very similar, but weiserjahre uses our
		// hacked "decimal" coding
		sb.setLength(0);
		sb.append("> 5% <");
		for (int i = 0; i < 10; i++)
			sb.append(" 9%");

		style = textpane.getStyle("raw data");
		tabset = EditorTabSetFactory.buildTabset(sb.toString(), width);
		StyleConstants.setTabSet(style, tabset);

		sb.setLength(0);
		sb.append("> 5% *");
		for (int i = 0; i < 10; i++)
			sb.append(" 9%");
		
		style = textpane.getStyle("weiserjahre data");
		tabset = EditorTabSetFactory.buildTabset(sb.toString(), width);
		StyleConstants.setTabSet(style, tabset);

		style = textpane.getStyle("elements");
		//> 12% <> 58% ^ 7% ^ 8% ^ 8% ^ 7% <		
		tabset = EditorTabSetFactory.buildTabset(
				"< 12% < 1% 50% ^ 7% 8% 8% 7%", 
				width);
		StyleConstants.setTabSet(style, tabset);		
	}
	
	/*
	 * The following functions make huge datasets and append them to our JTextPane
	 */
	private String getSummed() {
		StringBuffer sb = new StringBuffer();
		
		for (Year y = s.range.getStart(); s.range.contains(y); y = y.add(1)) {
			
			if (!y.equals(s.range.getStart()) && y.column() != 0)
				continue;

			Year decade = y;
			while (decade.column() != 0)
				decade = decade.add(-1);

			// we start out indented...
			sb.append("\t");
			sb.append(String.valueOf(y));

			// data
			for (int i = 0; i < 10; i++) {
				sb.append("\t");
				if (s.range.contains(decade.add(i)))
					sb.append( ((Number) s.data.get(decade.add(i).diff(
							s.range.getStart()))).intValue());
			}
			sb.append("\t");
			// count
			for (int i = 0; i < 10; i++) {
				sb.append("\t");
				if (s.range.contains(decade.add(i)))
					sb.append( ((Number) s.count.get(decade.add(i).diff(
							s.range.getStart()))).intValue());
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	private String getSummedInfo() {
		StringBuffer sb = new StringBuffer();
		// extra data for summed files
		sb.append("Number of samples in data set: "
				+ (s.elements == null ? "unknown" : String.valueOf(s.elements
						.size())) + "\n");
		sb.append("Number of rings in data set: "
				+ s.countRings() + "\n");
		sb.append("Length of data set: " + s.range.span()
				+ " years\n\n");
		
		return sb.toString();
	}
	
	private String getRawData() {
		StringBuffer sb = new StringBuffer();

		// header...
		sb.append("\t\t0\t1\t2\t3\t4\t5\t6\t7\t8\t9\n");

		for (Year y = s.range.getStart(); s.range.contains(y); y = y.add(1)) {
			if (!y.equals(s.range.getStart()) && y.column() != 0)
				continue;

			sb.append(y.toString());
			sb.append("\t");

			Year decade = y;
			while (decade.column() != 0)
				decade = decade.add(-1);

			// loop through years
			for (int i = 0; i < 10; i++) {
				sb.append("\t");
				if (s.range.contains(decade.add(i)))
					sb.append((Number) s.data.get(decade.add(i).diff(s.range.getStart())));
			}

			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	private String getMetadata() {
		StringBuffer sb = new StringBuffer();
		
		if (s.meta.containsKey("id"))
			sb.append("ID Number " + s.meta.get("id") + "\n");
		if (s.meta.containsKey("title"))
			sb.append("Title of sample: " + s.meta.get("title") + "\n");
		sb.append(s.isAbsolute() ? "Absolutely dated\n"
				: "Relatively dated\n");
		if (s.meta.containsKey("unmeas_pre"))
			sb.append(s.meta.get("unmeas_pre")
					+ " unmeasured rings at beginning of sample.\n");
		if (s.meta.containsKey("unmeas_post"))
			sb.append(s.meta.get("unmeas_post")
					+ " unmeasured rings at end of sample.\n");
		if (s.meta.containsKey("filename"))
			sb.append("File saved as " + s.meta.get("filename") + "\n");

		// - comments -- loop
		if (s.meta.containsKey("comments")) {
			String comments[] = StringUtils.splitByLines((String) s.meta
					.get("comments"));
			for (int i = 0; i < comments.length; i++) {
				if(i == 0)
					sb.append("Comments:\t" + comments[i] + "\n");
				else
					sb.append("\t" + comments[i] + "\n");
			}
		}

		if (s.meta.containsKey("type"))
			sb.append("Type of sample " + s.meta.get("type") + "\n");
		if (s.meta.containsKey("species"))
			sb.append("Species: " + s.meta.get("species") + "\n");
		// TODO: look up species name (if it's a code)
		if (s.meta.containsKey("format")) { // use a switch?
			if (s.meta.get("format").equals("R"))
				sb.append("Raw format\n");
			else if (s.isIndexed())
				sb.append("Indexed format\n");
			else
				sb.append("Unknown format\n");
		}
		if (s.meta.containsKey("sapwood"))
			sb.append(s.meta.get("sapwood") + " sapwood rings.\n");
		if (s.meta.containsKey("pith")) {
			String p = (String) s.meta.get("pith");
			if (p.equals("P"))
				sb.append("Pith present and datable\n");
			else if (p.equals("*"))
				sb.append("Pith present but undatable\n");
			else if (p.equals("N")) // uppercase only?
				sb.append("No pith present\n");
			else
				sb.append("Unknown pith\n");
		}
		if (s.meta.containsKey("terminal"))
			sb.append("Last ring measured "
					+ s.meta.get("terminal") + "\n");
		if (s.meta.containsKey("continuous")) {
			String c = (String) s.meta.get("continuous");
			if (c.equals("C")) // uppercase only?
				sb.append("Last ring measured is continuous\n");
			else if (c.equals("R")) // uppercase only?
				sb.append("Last ring measured is partially continuous\n");
		}
		if (s.meta.containsKey("quality"))
			sb.append("The quality of the sample is "
					+ s.meta.get("quality") + "\n");

		sb.append("\n");
		
		return sb.toString();
	}

	private String getWeiserjahre() {
		StringBuffer sb = new StringBuffer();

		for (Year y = s.range.getStart(); s.range.contains(y); y = y.add(1)) {
			if (!y.equals(s.range.getStart()) && y.column() != 0)
				continue;

			sb.append(y.toString());
			sb.append("\t");

			// if first row, find first year -- REFACTOR
			Year decade = y;
			while (decade.column() != 0)
				decade = decade.add(-1);

			// loop through years
			for (int i = 0; i < 10; i++) {
				sb.append("\t");
				if (s.range.contains(decade.add(i))) {
					String wval =
						((Number) s.incr.get(decade.add(i).diff(
							s.range.getStart()))).toString() +
						(Weiserjahre.isSignificant(s, decade.add(i).diff(
								s.range.getStart())) ? Weiserjahre.SIGNIFICANT
									: Weiserjahre.INSIGNIFICANT) + 							
						((Number) s.decr.get(decade.add(i).diff(
							s.range.getStart()))).toString();
					sb.append(wval);
				}
			}
			sb.append("\n");
		}
		sb.append("\n");
		
		return sb.toString();
	}
	
	private String getWeiserjahreInfo() {
		StringBuffer sb = new StringBuffer();

		int sigs = s.countSignificantIntervals();
		int threes = s.count3SampleIntervals();
		sb.append("Number of intervals with >3 samples: " + threes + "\n");
		float pct = (float) sigs / (float) threes;
		DecimalFormat fmt = new DecimalFormat("0.0%");
		sb.append("Number of significant intervals: " + sigs
			+ " (" + fmt.format(pct) + " of intervals with >3 samples)\n\n");
		
		return sb.toString();
	}

	// (\u2020 and \u2021 (DAGGER, DOUBLE DAGGER) look the best,
	// TODO: can i use them on Macs?
	private static final String footnoteSymbol1 = "\u2020";
	private static final String footnoteSymbol2 = "\u2021";
	
	private String getElements() {
		StringBuffer sb = new StringBuffer();
		boolean footnote = false;
		boolean footnote2 = false;
		
		sb.append("This data set is composed of the following files:\n");

		// add table header -- skip "unmeas/pre/post" headers
		// (not enough space, and it's fairly obvious, anyway)
		sb.append("ID\t\tFilename\tPith\t\tRange\t\tTerminal\n"); // FIXME: i18n me!

		// write out all elements
		for (int i = 0; i < s.elements.size(); i++) {
			corina.Element e = (corina.Element) s.elements.get(i);

			if (e.details == null) {
				try {
					e.loadMeta();
				} catch (FileNotFoundException fnfe) {
					e.error = fnfe;
					footnote = true;
				} catch (IOException ioe) {
					e.error = ioe;
					footnote2 = true;
				}
			}

			// not loaded?
			if (e.details == null) {
				String mark = ((e.error instanceof FileNotFoundException) ? footnoteSymbol1
						: footnoteSymbol2);
				mark = mark + " ";
				sb.append("\t" + mark + "\t" + e.filename + "\n");
			} else {
				String x = "";

				x += (e.details.containsKey("id") ? e.details.get("id")
						.toString() : "");
				x += "\t";
				x += ""; // no footnote
				x += "\t";
				x += (e.details.containsKey("filename") ? e.details.get(
						"filename").toString() : ""); // title?
				x += "\t";
				x += (e.details.containsKey("pith") ? "+"
						+ e.details.get("pith").toString() : "");
				x += "\t";
				x += (e.details.containsKey("unmeas_pre") ? "+"
						+ e.details.get("unmeas_pre").toString() : "");
				x += "\t";
				x += e.getRange().toString();
				x += "\t";
				x += (e.details.containsKey("unmeas_post") ? "+"
						+ e.details.get("unmeas_post").toString() : "");
				x += "\t";
				x += (e.details.containsKey("terminal") ? e.details.get(
						"terminal").toString() : "");

				sb.append(x + "\n");
			}
		}

		// if one or more files couldn't load, print the correct footnotes for it.
		if (footnote || footnote2)
			sb.append("\n");
		if (footnote)
			sb.append(footnoteSymbol1
				+ " This file wasn't found; it was probably moved, renamed, or deleted.\n");
		if (footnote2)
			sb.append(footnoteSymbol2
				+ " This file couldn't be loaded; it might have been corrupted.\n");
		
		return sb.toString();
	}
	
	
	public final static int TITLE_SIZE = 18;
	public final static int SECTION_SIZE = 14;
	public final static int NORMAL_SIZE = 10;
	public final static int TINY_SIZE = 9;

}
