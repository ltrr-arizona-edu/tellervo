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

package corina.cross;

import corina.print.Line;
import corina.print.EmptyLine;
import corina.print.TextLine;
import corina.print.ByLine;
import corina.print.PrintableDocument;

import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.print.PageFormat;

// i think line should be an abstract class, not an interface (there's nothing else a line
// will ever need to do or be), and it should have a hell of a lot more functionality for
// the taking.  this is fowler's dream job now.

// if i'm feeling up to it: i've created an admirable abstraction for printing documents
// consisting of lines of text, like tables.  can i make another one that's good for printing
// generic square-celled documents? -- well, that's not very useful; it works for grids,
// and probably nothing else.  but what would be useful is knowing how to print something
// of arbitrary size, and have a corina.print class that either (1) scales it down to fit on
// a page, or (2) tiles it on as many pages as needed -- that would be a powerful abstraction.

public class TablePrinter implements PrintableDocument {
    // would printable->lineprintable->tableprinter be an adventageous class hierarchy?
    
    // i18n
    private static ResourceBundle msg = ResourceBundle.getBundle("TextBundle");

    private List lines = new ArrayList();
    public List getLines() {
	return lines;
    }

    public TablePrinter(Table t, PageFormat pf) {
        // title
        lines.add(new TextLine("Crossdating Table for: " + t.singleton, Line.TITLE_SIZE));
        // -- ok, it's now a constant that's defined in one place, which is great.  but i'm not
        // sure there isn't an even better way to do this -- i'm more inclined to say
        // "add a line for the title that says xyz" than "add a text line of title-size that says xyz" -- more classes?
        lines.add(new EmptyLine()); // (do my other printers do this?)
        // -- wouldn't a factory be a good choice for things like empty lines?  memory isn't an issue, but it might be nice.

        // table, with header
        lines.add(new RowHeaderLine());
        lines.add(new ThinLine(0.0f, 1.0f));
        for (int i=0; i<t.table.size(); i++)
            lines.add(new RowLine((Table.Row) t.table.get(i), t));
        // RENAME: looking at it from this context, t.table is a silly name; it should be t.rows

        // print by-line
        lines.add(new EmptyLine());
        lines.add(new ThinLine(0.0f, 0.3f));
        lines.add(new ByLine());
    }

    // subclass rowline?  yes, because:
    // -- it's fairly simple, so i don't mind trying it here
    // -- duplicate code!  bad smell!  all those columns are bad!
    // -- if it works well, i can extend it to crossprinter, sampleprinter, etc.
    // -- (if that works well, i can make my super-row)
    private static class RowHeaderLine implements Line {
        // (no constructor needed, it's constant)

        public void print(Graphics g, PageFormat pf, float y) {
            // baseline
            float baseline = (float) (y + height(g));
            Graphics2D g2 = (Graphics2D) g;

            float left = (float) pf.getImageableX();
            float width = (float) pf.getImageableWidth();
            
            g2.setFont(NORMAL);

            g2.drawString(msg.getString("title"),	left, baseline);
            g2.drawString(msg.getString("tscore"),	left + width*0.5f, baseline);
            g2.drawString(msg.getString("trend"),	left + width*0.6f, baseline);
            g2.drawString(msg.getString("dscore"),	left + width*0.7f, baseline);
            g2.drawString(msg.getString("overlap"),	left + width*0.8f, baseline);
            g2.drawString(msg.getString("distance"),	left + width*0.9f, baseline);
        }

        // damn it, isn't this what OO is supposed to let me avoid?  subclass!
        public int height(Graphics g) {
            return g.getFontMetrics(NORMAL).getHeight();
        }
    }

    // like html's <hr>, sort of -- defaults to middle 1/2 of page, but anything's possible
    private static class ThinLine implements Line {
        private float start=0.25f, finish=0.75f;
        public ThinLine() {
            // use defaults
        }
        public ThinLine(float start, float finish) {
            this.start = start;
            this.finish = finish;
        }
        public void print(Graphics g, PageFormat pf, float y) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(0.1f)); // -- either make this settable, or make it a class constant, not an instance one

            // margins of printable page
            float left = (float) pf.getImageableX();
            float right = (float) (pf.getImageableX() + pf.getImageableWidth());

            // fraction i'm going to draw
            float realLeft = left + (right-left) * start;
            float realRight = left + (right-left) * finish;

            // draw it
            g2.drawLine((int) realLeft, (int) (y+1), (int) realRight, (int) (y+1));
        }
        public int height(Graphics g) {
            return 2;
        }
    }

    private static class RowLine implements Line {
        private Table.Row row;
        private Table table; // used for formatting (f1.format()) -- UGH!
        public RowLine(Table.Row row, Table table) {
            this.row = row;
            this.table = table;
        }
        public void print(Graphics g, PageFormat pf, float y) {
            // baseline
            float baseline = (float) (y + g.getFontMetrics().getHeight()); // first baseline!  (who?)
            Graphics2D g2 = (Graphics2D) g;

            float left = (float) pf.getImageableX();
            float width = (float) pf.getImageableWidth();

            g2.setFont(NORMAL);

            // will i need to use 2 lines?
            float titleWidth = g2.getFontMetrics().stringWidth(row.title);
            // -- WORKING HERE XXX

            g2.drawString(row.title,		left, baseline);
            // BUG: row.title is OFTEN wider than its column, and if that's the case,
            // draw it on its own line, and put the stats on the next line by themselves, perhaps
            // with slightly tighter vertical spacing.

            // HACK: use 4" for now
            if (titleWidth > 4)
                baseline += g.getFontMetrics().getHeight();

            g2.drawString(table.f1.format(row.cross.t),		left + width*0.5f, baseline);
            g2.drawString(table.f2.format(row.cross.tr),	left + width*0.6f, baseline);
            g2.drawString(table.f3.format(row.cross.d),		left + width*0.7f, baseline);
            g2.drawString(String.valueOf(row.cross.n),		left + width*0.8f, baseline);
            if (row.cross.dist != null)
                g2.drawString(row.cross.dist.toString(),	left + width*0.9f, baseline);
        }

        public int height(Graphics g) {
            // need 2 lines?
            float titleWidth = g.getFontMetrics().stringWidth(row.title);
            // UGH!  need to know how big the page is here before i can tell if i'll need 2 lines ... *groan*
            // --- AND HERE XXX
            // -- idea: lineddocument contains pf, perhaps also g, and gets passed instead ... ?
            // HACK: use 4" for now
            if (titleWidth > 4)
                return g.getFontMetrics(NORMAL).getHeight() * 2;
            
            return g.getFontMetrics(NORMAL).getHeight();
        }
    }
}
