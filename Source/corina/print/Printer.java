package corina.print;

import java.awt.print.Printable;
import java.awt.print.PageFormat;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;

import java.util.List;
import java.util.ArrayList;

public class Printer {

    public static Printable print(PrintableDocument doc) {
        return new LinePrinter(doc);
    }

    private static class LinePrinter implements Printable {
        private List lines;

        private List firstRowOfPage = null; // needed by print()

        LinePrinter(PrintableDocument doc) {
            this.lines = doc.getLines();
        }

        // (does this ignore pf?  an old comment said it once might have.)
        public int print(Graphics g, PageFormat pf, int page) {
            // store margins
            float left = (float) pf.getImageableX();
            float right = left + (float) pf.getImageableWidth();
            float top = (float) pf.getImageableY();
            float bottom = top + (float) pf.getImageableHeight();

            // next pixel row to draw: start at the top
            float position = top;

            // never printed before ... paginate
            if (firstRowOfPage == null) {
                firstRowOfPage = new ArrayList();
                firstRowOfPage.add(new Integer(0));

                float ruler = top; // if this is an int, lines are placed to the nearest 1/72" only -- would that be ok?
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
                    l.print(g, pf, position);
                    position += l.height(g);
                    row++;
                } else {
                    break; // ... inelegant
                }
            }

            // if you want (or for debugging), print out page boundaries like real printers do
            final boolean debug=false;
            if (debug) {
                Graphics2D g2 = (Graphics2D) g;
                final float stroke = 0.1f;
                g2.setStroke(new BasicStroke(stroke));
                g2.setColor(Color.black);
                final int inch = 72, radius = inch/2;

                // horizontal lines
                g2.drawLine((int) (left-inch/2), (int) top, (int) (left+inch*3/2), (int) top);
                g2.drawLine((int) (right-inch*3/2), (int) top, (int) (right+inch/2), (int) top);
                g2.translate(0, -stroke);
                g2.drawLine((int) (left-inch/2), (int) bottom, (int) (left+inch*3/2), (int) bottom);
                g2.drawLine((int) (right-inch*3/2), (int) bottom, (int) (right+inch/2), (int) bottom);
                g2.translate(0, +stroke);

                // vertical lines
                g2.drawLine((int) left, (int) (top-inch/2), (int) left, (int) (top+inch*3/2));
                g2.drawLine((int) right, (int) (top-inch/2), (int) right, (int) (top+inch*3/2));
                g2.drawLine((int) left, (int) (bottom-inch*3/2), (int) left, (int) (bottom+inch/2));
                g2.drawLine((int) right, (int) (bottom-inch*3/2), (int) right, (int) (bottom+inch/2));

                // circles
                g2.drawOval((int) (left - radius/2), (int) (top - radius/2), radius, radius);
                g2.drawOval((int) (right - radius/2), (int) (top - radius/2), radius, radius);
                g2.drawOval((int) (left - radius/2), (int) (bottom - radius/2), radius, radius);
                g2.drawOval((int) (right - radius/2), (int) (bottom - radius/2), radius, radius);
            }
            return PAGE_EXISTS;
        }
    }
}
