package corina.editor;

import corina.Sample;
import corina.Element;
import corina.Year;

import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import java.text.DecimalFormat;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;

import java.awt.print.Printable;
import java.awt.print.PageFormat;

public class SamplePrinter implements Printable {

    /*
     thoughts on printing samples:

     -- (get rid of epson.java)

     -- dump lines, etc., into corina.print.*
     -- (consider reworking this abstraction in scheme)
     */

    /*
     LEFT TO DO:
     -- move all generic printing stuff (page breaking, plain text lines, text alignment/measuring, etc.) into its own class/package

     -- if i show the user the page-layout dialog, am i restricting myself to smaller printable area?
     -- can i just make my own pageformat, or tweak that pageformat, to get the full imageable area?
     -- it would suck to make users do "page setup" every time, or suffer huge margins
     -- (hey, you should be serializing the pageformat to ~/.corina/ anywho!)
     */

    public SamplePrinter(Sample s) {
        // save a ref to the sample
        this.s = s;

        // everything after here -- the print*() methods -- adds stuff to |lines|

        // header
        printHeader();
        
        // data
        if (s.isSummed())
            printSummedData();
        else
            printRawData();

        // metadata
        printMetadata();

        // weiserjahre
        if (s.hasWeiserjahre())
            printWeiserjahre();

        // elements
        if (s.elements != null)
            printElements();
    }

    // (printable)  actually print a page; again, stolen from crossprinter.java, REFACTOR.
    // BUG: ignores pageformat?
    public int print(Graphics g, PageFormat pf, int page) {
        // store margins
        left = pf.getImageableX();
        top = pf.getImageableY();
        right = left + pf.getImageableWidth();
        bottom = top + pf.getImageableHeight();

        // next pixel row to draw: start at the top
        double position = top;

        // never printed before ... paginate
        if (firstRowOfPage == null) {
            firstRowOfPage = new ArrayList();
            firstRowOfPage.add(new Integer(0));

            double ruler = top;
            for (int row=0; row<lines.size(); row++) {
                Line l = (Line) lines.get(row);
                if (ruler + l.height(g) > bottom) {
                    firstRowOfPage.add(new Integer(row));
                    ruler = top;
                }
                ruler += l.height(g);
            }
        }

        // not a real page
        if (page >= firstRowOfPage.size())
            return NO_SUCH_PAGE;

        // print rows of page
        int row = ((Integer) firstRowOfPage.get(page)).intValue();
        while (row < lines.size()) {
            Line l = (Line) lines.get(row);
            if (position + l.height(g) < bottom) {
                l.print(g, position);
                position += l.height(g);
                row++;
            } else {
                break; // ... inelegant
            }
        }

        // if you want (or for debugging), print out page boundaries like printers do
        if (false) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(0.1f));
            g2.setColor(Color.black);
            final int inch = 72;
            g2.drawLine((int) (left-inch/2), (int) top, (int) (left+inch*3/2), (int) top);
            g2.drawLine((int) (right-inch*3/2), (int) top, (int) (right+inch/2), (int) top);
            g2.drawLine((int) (left-inch/2), (int) bottom, (int) (left+inch*3/2), (int) bottom);
            g2.drawLine((int) (right-inch*3/2), (int) bottom, (int) (right+inch/2), (int) bottom);

            g2.drawLine((int) left, (int) (top-inch/2), (int) left, (int) (top+inch*3/2));
            g2.drawLine((int) right, (int) (top-inch/2), (int) right, (int) (top+inch*3/2));
            g2.drawLine((int) left, (int) (bottom-inch*3/2), (int) left, (int) (bottom+inch/2));
            g2.drawLine((int) right, (int) (bottom-inch*3/2), (int) right, (int) (bottom+inch/2));
                                                
            final int radius = inch/2;
            g2.drawOval((int) (left - radius/2), (int) (top - radius/2), radius, radius);
            g2.drawOval((int) (right - radius/2), (int) (top - radius/2), radius, radius);
            g2.drawOval((int) (left - radius/2), (int) (bottom - radius/2), radius, radius);
            g2.drawOval((int) (right - radius/2), (int) (bottom - radius/2), radius, radius);
        }
        
        return PAGE_EXISTS;
    }

    private List firstRowOfPage = null; // needed by print()
    
    //
    // PRINTING METHODS -- these construct the lines to print
    //
    
    // this doesn't really belong here, but where?
    private String getUserName() {
        return System.getProperty("user.name", "(unknown user)");
    }
    
    private void printHeader() {
        lines.add(new TitleLine((String) s.meta.get("title")));
        lines.add(new TextLine("Printed by: " + getUserName()));
        lines.add(new TextLine(new Date().toString())); // is this default-locale?
        lines.add(new EmptyLine());

        double radius = s.computeRadius() / 1000.; // only for non-indexed samples?
        double average = radius / s.data.size();
        DecimalFormat df = new DecimalFormat("0.000");
        if (!s.isIndexed()) {
            lines.add(new TextLine("Radius: " + df.format(radius) + " cm, " +
                                   "Average ring width: " + df.format(average) + " cm"));
            lines.add(new EmptyLine());
        }
    }

    private void printRawData() {
        lines.add(new DataHeaderLine());
        lines.add(new EmptyLine());

        // i really want a pure-data decade model here!
        
        // make emptyline a singleton?  nah, it's just an Integer, and there are a finite number.  (no there aren't.)

        for (Year y = s.range.getStart(); s.range.contains(y); y=y.add(1)) {
            if (!y.equals(s.range.getStart()) && y.column()!=0)
                continue;
            lines.add(new DataLine(y));
            lines.add(new EmptyLine());
        }
    }

    private void printSummedData() {
        // -- header line?  no

        // i really want a pure-data decade model here!
        // make emptyline a singleton?  nah, it's just an Integer, and there are a finite number.  (no there aren't.)

        for (Year y = s.range.getStart(); s.range.contains(y); y=y.add(1)) {
            if (!y.equals(s.range.getStart()) && y.column()!=0)
                continue;
            lines.add(new DataCountLine(y));
        }

        // extra data for summed files
        lines.add(new TextLine("Number of samples in data set: " +
                               (s.elements==null ? "unknown" : String.valueOf(s.elements.size()))));
        lines.add(new TextLine("Number of rings in data set: " + s.countRings()));
        lines.add(new TextLine("Length of data set: " + s.range.span() + " years"));
        // TODO: line up these 3 values?
        /* i'd think it might be easier on the eyes just to make a bullet list with units:
            * 601 intervals with >3 samples
            * 125 significant intervals (12.8%)
        */
        lines.add(new EmptyLine());
    }

    private void printMetadata() {
        if (s.meta.containsKey("id"))
            lines.add(new TextLine("ID Number " + s.meta.get("id")));
        if (s.meta.containsKey("title"))
            lines.add(new TextLine("Title of sample: " + s.meta.get("title")));
        lines.add(new TextLine(s.isAbsolute() ? "Absolutely dated" : "Relatively dated"));
        if (s.meta.containsKey("unmeas_pre"))
            lines.add(new TextLine(s.meta.get("unmeas_pre") + " unmeasured rings at beginning of sample."));
        if (s.meta.containsKey("unmeas_post"))
            lines.add(new TextLine(s.meta.get("unmeas_post") + " unmeasured rings at end of sample."));
        if (s.meta.containsKey("filename"))
            lines.add(new TextLine("File saved as " + s.meta.get("filename")));

        // - comments -- loop
        if (s.meta.containsKey("comments")) { // line-by-line
            String comments = (String) s.meta.get("comments");
            StringTokenizer tok = new StringTokenizer(comments, "\n");
            int n = tok.countTokens();
            for (int i=0; i<n; i++)
                lines.add(new TextLine("Comments: " + tok.nextToken()));
            // this repeats "Comments:" every line -- we don't want that, do we?
        }

        if (s.meta.containsKey("type"))
            lines.add(new TextLine("Type of sample " + s.meta.get("type")));
        if (s.meta.containsKey("species"))
            lines.add(new TextLine("Species: " + s.meta.get("species"))); // TODO: look up species name
        if (s.meta.containsKey("format")) { // use a switch?
            if (s.meta.get("format").equals("R"))
                lines.add(new TextLine("Raw format"));
            else if (s.isIndexed())
                lines.add(new TextLine("Indexed format"));
            else
                lines.add(new TextLine("Unknown format"));
        }
        if (s.meta.containsKey("sapwood"))
            lines.add(new TextLine(s.meta.get("sapwood") + " sapwood rings."));
        if (s.meta.containsKey("pith")) {
            String p = (String) s.meta.get("pith");
            // BUG: if s.meta is a HashMap, p might be null here
            if (p.equals("P"))
                lines.add(new TextLine("Pith present and datable"));
            else if (p.equals("*"))
                lines.add(new TextLine("Pith present but undatable"));
            else if (p.equals("N")) // uppercase only?
                lines.add(new TextLine("No pith present"));
            else
                lines.add(new TextLine("Unknown pith"));
        }
        if (s.meta.containsKey("terminal"))
            lines.add(new TextLine("Last ring measured " + s.meta.get("terminal") ));
        if (s.meta.containsKey("continuous")) {
            String c = (String) s.meta.get("continuous");
            // BUG: see pith, above
            if (c.equals("C")) // uppercase only?
                lines.add(new TextLine("Last ring measured is continuous"));
            else if (c.equals("R")) // uppercase only?
                lines.add(new TextLine("Last ring measured is partially continuous"));
        }
        if (s.meta.containsKey("quality"))
            lines.add(new TextLine("The quality of the sample is " + s.meta.get("quality")));

        lines.add(new EmptyLine());
    }

    private void printWeiserjahre() {
        lines.add(new SectionLine("Weiserjahre data"));

        for (Year y = s.range.getStart(); s.range.contains(y); y=y.add(1)) {
            if (!y.equals(s.range.getStart()) && y.column()!=0)
                continue;
            lines.add(new WeiserjahreLine(y));
        }
        // -- use "x/y", with |x| right-aligned and |y| left-aligned

        lines.add(new EmptyLine());

        // 3 more lines: wj summary
        int sig = s.countSignificantIntervals();
        lines.add(new TextLine("Number of intervals with >3 samples: " + s.count3SampleIntervals()));
        lines.add(new TextLine("Number of significant intervals: " + sig));
        double pct = (double) sig / (double) s.incr.size();
        DecimalFormat fmt = new DecimalFormat("0.0%");
        lines.add(new TextLine("Percent significant intervals: " + fmt.format(pct)));
        lines.add(new EmptyLine());
    }

    private void printElements() {
        lines.add(new SectionLine("This data set is composed of the following files:"));
        for (int i=0; i<s.elements.size(); i++)
            lines.add(new ElementLine((Element) s.elements.get(i)));
    }

    //
    // printing layer, stolen from crossprint.java -- REFACTOR
    //

    interface Line {
        public static final Font NORMAL = new Font("serif", Font.PLAIN, 10); // standard font
        public void print(Graphics g, double y);
        public int height(Graphics g);
    }
    class EmptyLine implements Line {
        private int height=18; // 1/4"
        public EmptyLine() {
        }
        public void print(Graphics g, double y) {
            // do nothing.
        }
        public int height(Graphics g) {
            return height;
        }
    }
    private class TextLine implements Line {
        private String text;
        public TextLine(String text) {
            this.text = text;
        }
        public void print(Graphics g, double y) {
            // baseline
            float baseline = (float) (y + height(g));
            Graphics2D g2 = (Graphics2D) g;

            g2.setFont(NORMAL);
            g2.drawString(text, (float) left, baseline);
        }
        public int height(Graphics g) {
            return g.getFontMetrics(NORMAL).getHeight();
        }
    }
    private class TitleLine implements Line {
        private String title;
        public TitleLine(String title) {
            this.title = title;
        }
        public void print(Graphics g, double y) {
            // baseline
            float baseline = (float) (y + height(g));
            Graphics2D g2 = (Graphics2D) g;

            // title
            g2.setFont(new Font("serif", Font.PLAIN, 14)); // too many news!
            g2.drawString(title, (float) left, baseline); // |left| is from enclosing class
        }
        public int height(Graphics g) {
            return g.getFontMetrics(new Font("serif", Font.PLAIN, 14)).getHeight();
        }
    }
    private class SectionLine implements Line { // REFACTOR: SECTION, TITLE extend TEXT!
        private String text;
        public SectionLine(String text) {
            this.text = text;
        }
        public void print(Graphics g, double y) {
            // baseline
            float baseline = (float) (y + height(g));
            Graphics2D g2 = (Graphics2D) g;

            g2.setFont(new Font("serif", Font.PLAIN, 14)); // REFACTOR -> constant
            g2.drawString(text, (float) left, baseline);
        }
        public int height(Graphics g) {
            return g.getFontMetrics(new Font("serif", Font.PLAIN, 14)).getHeight();
        }
    }
    private class DataHeaderLine implements Line {
        public void print(Graphics g, double y) {
            // baseline
            float baseline = (float) (y + height(g));
            Graphics2D g2 = (Graphics2D) g;

            g2.setFont(NORMAL);

            // f's?
            double col1 = 0.25 * 72; // 3/4"?
            double width = (right - left - col1) / 10;

            for (int i=0; i<10; i++) {
                String label = String.valueOf(i);
                int x = g.getFontMetrics().stringWidth(label);
                g2.drawString(label, (float) (left + col1 + width*(i+1) - x), baseline);
            }
            // how to draw:
            // -- decade, left(?)-aligned in left column
            // -- each data value, right-aligned, in equally-spaced columns
        }
        public int height(Graphics g) {
            return g.getFontMetrics(NORMAL).getHeight();
        }
    }
    private class DataLine implements Line {
        private Year decade;
        public DataLine(Year decade) {
            this.decade = decade;
        }
        public void print(Graphics g, double y) {
            // baseline
            float baseline = (float) (y + height(g));
            Graphics2D g2 = (Graphics2D) g;

            g2.setFont(NORMAL);

            // f's?
            double col1 = 0.25 * 72; // 3/4"?
            double width = (right - left - col1) / 10;

            // decade
            g2.drawString(decade.toString(), (float) left, baseline);

            // (snap decade back to real decade, now -- BREAKS IMMUTABILITY, if i even thought i had that.)
            Year fake = decade;
            while (fake.column() != 0)
                fake = fake.add(-1);
            
            // loop through years
            for (int i=0; i<10; i++) {
                // don't draw it, if it ain't there
                if (!s.range.contains(fake.add(i)))
                    continue;
                int x = ((Number) s.data.get(fake.add(i).diff(s.range.getStart()))).intValue();
                String value = String.valueOf(x);
                int w = g.getFontMetrics().stringWidth(value);
                g2.drawString(value, (float) (left + col1 + width*(i+1) - w), baseline);
            }
        }
        public int height(Graphics g) {
            return g.getFontMetrics(NORMAL).getHeight();
            // instead of a bunch of extra emptylines, how about just doubling this?
            // no, because then the margins at the end of a page don't count (but they sort of don't, anyway).
        }
    }
    private class DataCountLine implements Line {
        private Year decade;
        public DataCountLine(Year decade) {
            this.decade = decade;
        }
        public void print(Graphics g, double y) {
            // baseline
            float baseline = (float) (y + height(g));
            Graphics2D g2 = (Graphics2D) g;

            g2.setFont(new Font("serif", Font.PLAIN, 9)); // no, smaller!  (9pt?)

            // f's?
            double col1 = 0.25 * 72; // 1/4"?
            double fullWidth = right - left;
            double width = (fullWidth - col1) * 0.60 / 10; // 60% of the width for the data, ...
            final double buf = 0.25*72; // 1/4" space between 'em?
            double widthRight = (fullWidth - col1 - width*10 - buf) / 10; // ... rest for count

            // decade
            g2.drawString(decade.toString(), (float) left, baseline);

            // (snap decade back to real decade, now -- BREAKS IMMUTABILITY, if i even thought i had that.)
            Year fake = decade;
            while (fake.column() != 0)
                fake = fake.add(-1);

            // loop through years
            for (int i=0; i<10; i++) {
                // don't draw it, if it ain't there
                if (!s.range.contains(fake.add(i)))
                    continue;

                // data
                int x = ((Number) s.data.get(fake.add(i).diff(s.range.getStart()))).intValue();
                String value = String.valueOf(x);
                int w = g.getFontMetrics().stringWidth(value);
                g2.drawString(value, (float) (left + col1 + width*(i+1) - w), baseline);

                // count
                int z = ((Number) s.count.get(fake.add(i).diff(s.range.getStart()))).intValue();
                /* String */ value = String.valueOf(z);
                /* int */ w = g.getFontMetrics().stringWidth(value);
                g2.drawString(value, (float) (left + col1 + width*10 + buf + widthRight*(i+1) - w), baseline);
            }
        }
        public int height(Graphics g) {
            return g.getFontMetrics(new Font("serif", Font.PLAIN, 9)).getHeight();
            // instead of a bunch of extra emptylines, how about just doubling this?
            // no, because then the margins at the end of a page don't count (but they sort of don't, anyway).
        }
    }
    private class WeiserjahreLine implements Line {
        private Year decade;
        public WeiserjahreLine(Year decade) {
            this.decade = decade;
        }
        public void print(Graphics g, double y) {
            // baseline
            float baseline = (float) (y + height(g));
            Graphics2D g2 = (Graphics2D) g;

            g2.setFont(new Font("serif", Font.PLAIN, 9));

            // f's?
            double col1 = 0.25 * 72; // 3/4"?
            double width = (right - left - col1) / 10;

            // decade
            g2.drawString(decade.toString(), (float) left, baseline);

            // (snap decade back to real decade, now -- BREAKS IMMUTABILITY, if i even thought i had that.)
            Year fake = decade;
            while (fake.column() != 0)
                fake = fake.add(-1);

            // loop through years
            for (int i=0; i<10; i++) {
                // don't draw it, if it ain't there
                if (!s.range.contains(fake.add(i)))
                    continue;

                // draw right-aligned "x1" + left-aligned "/x2"
                float position = (float) (left + col1 + width*(i + 0.5)); // the "/" starts here

                String x1 = ((Number) s.incr.get(fake.add(i).diff(s.range.getStart()))).toString();
                String x2 = ((Number) s.decr.get(fake.add(i).diff(s.range.getStart()))).toString();
                
                int w = g.getFontMetrics().stringWidth(x1);
                g2.drawString(x1, (float) (position - w), baseline);
                g2.drawString("/" + x2, position, baseline);
            }
        }
        public int height(Graphics g) {
            return g.getFontMetrics(new Font("serif", Font.PLAIN, 9)).getHeight();
        }
    }
    private class ElementLine implements Line {
        private Element e;
        ElementLine(Element e) {
            this.e = e;
            if (e.details == null) {
                try {
                    e.loadMeta();
                } catch (IOException ioe) {
                    // ok, details is still null, which isn't the end of the world
                }
            }
        }
        // prints ( id filename pith unmeas_pre range unmeas_post terminal ) -- and might need to loadMeta() first
        // if load fails, just print filename
        // (draw active flag first, as what?)

        // 123456 G:\data\blah.rec           P +1 1001-1036 +1 ++
        public void print(Graphics g, double y) {
            // baseline
            float baseline = (float) (y + height(g));
            Graphics2D g2 = (Graphics2D) g;

            g2.setFont(NORMAL);

            // filename will always be printed, so do that first
            g2.drawString(e.filename, (float) (left + 72 * 1.0), baseline);
                
            // if it wasn't loaded, that's all there is
            if (e.details == null)
                return;

            // otherwise, print some metadata...
            
            if (e.details.containsKey("id"))
                g2.drawString(e.details.get("id").toString(), (float) left, baseline);

            // FIXME: use relative columns
            if (e.details.containsKey("pith"))
                g2.drawString(e.details.get("pith").toString(), (float) (left + 72 * 3.5), baseline);
            if (e.details.containsKey("unmeas_pre"))
                g2.drawString("+" + e.details.get("unmeas_pre"), (float) (left + 72 * 4.0), baseline);

            g2.drawString(e.range.toString(), (float) (left + 72 * 4.5), baseline); // center around "-"?

            if (e.details.containsKey("unmeas_post"))
                g2.drawString("+" + e.details.get("unmeas_post"), (float) (left + 72 * 5.5), baseline);
            if (e.details.containsKey("terminal"))
                g2.drawString(e.details.get("terminal").toString(), (float) (left + 72 * 6.0), baseline);
        }
        public int height(Graphics g) {
            return g.getFontMetrics(NORMAL).getHeight();
        }
    }

    //
    // PRIVATE DATA
    //
    
    // sample to print
    private Sample s;

    // lines to print
    private List lines = new ArrayList();

    // margins
    private double left, right, top, bottom; // why the heck do i need to know inches down to 10^-8?
}
