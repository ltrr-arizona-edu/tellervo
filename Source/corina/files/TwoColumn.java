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

package corina.files;

import corina.Year;
import corina.Range;
import corina.Sample;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;

import java.io.StreamTokenizer;
import java.io.FileWriter;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.io.Writer;
import java.io.BufferedWriter;

/**
   <p>A simple tab-delimited 2-column data file.  This is useful for
   transferring data to and from non-dendro programs, like
   spreadsheets and graphics utilities.</p>

   <p>Actually, the load() method doesn't care about tabs or newlines
   - they're both just whitespace.  It loads a sequence of integers in
   pairs: year datum.  Only the first year matters; all others are
   ignored.  Year -5 means 5 BC, not 6 BC, like some programs
   believe.</p>

   <p>The save() method saves a text file, one year's data per line,
   as "year &lt;tab&gt; width".  (Native newlines are used.)</p>

   <p>New feature: if there's a count (i.e., it's a summed file), it
   will be saved in 3 columns, the third column being the count.</p>

   <p>(Perhaps adding count and Weiserjahre would be a useful feature?
   Make it actually care about newlines, or watch the year values, so
   it knows what's one year's data.  It should ignore the stop-value
   "999", too, which some programs add.)</p>

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class TwoColumn extends Filetype {

    /** Return the human-readable name of this file format.
	@return the name of this file format */
    public String toString() {
	return "2-column";
    }

    /** Load tab-delimited data from a file.
	@param filename the file to load data from
	@return the loaded Sample
	@exception FileNotFoundException if the file cannot be found
	@exception WrongFiletypeException if it's obviously not a 2-column file
	@exception IOException if any problem with I/O occurs
	@see Sample */
    public Sample load() throws IOException {
        // sample to return
        Sample s = new Sample();

        // start/current/end years
        Year start=null, current=null;

        // maybe read a line of crap, up to 120 chars.
        maybeEatCrap(s);

        // set up a tokenizer on the file
//	r = new BufferedReader(new FileReader(filename));
        StreamTokenizer t = new StreamTokenizer(r);

        // this is the last-chance catch-all for random text data, so
        // don't be too afraid to throw a WrongFiletypeException:
        // we'll be getting all sorts of crap here.

        boolean hasYear, hasCount;

        // figure out what it has, by looking at 3 lines
        try {
            // we'll read 3 lines, but put them back by the end of the block
            r.mark(3*80);

            // read 3 lines of numbers -- yes, this means you can't load a 1- or 2-line file.
            double a[] = parseLine(r.readLine());
            double b[] = parseLine(r.readLine());
            double c[] = parseLine(r.readLine());
            
            // if they're sequential, they're probably years
            int a0 = (int) a[0];
            int b0 = (int) b[0];
            int c0 = (int) c[0];
            hasYear = (a0+1==b0) && (b0+1==c0);

            // if there's an extra column, it's the count
            int normal = 1 + (hasYear ? 1 : 0);
            hasCount = ((a.length==normal+1) && (b.length==normal+1) && (c.length==normal+1));

            // ok, done with that, put everything back
            r.reset();
        } catch (NullPointerException npe) {
	    // (thrown by r.readLine())
	    throw new WrongFiletypeException(); // not enough lines
	}

        // if no year, let's make one up.
        if (!hasYear)
            start = Year.DEFAULT;

        // hasData
        s.data = new ArrayList();
        if (hasCount)
            s.count = new ArrayList();

        for (;;) {
            String line = r.readLine();

            // no more data
            if (line == null)
                break;

            // get ready to parse
            StringTokenizer tok = new StringTokenizer(line, ",; \t");

            if (hasYear) {
		int y;
		try {
		    y = Integer.parseInt(tok.nextToken());
		} catch (NumberFormatException nfe) {
		    throw new WrongFiletypeException();
		}
                // just ignore, unless it's the first one
                if (start == null)
                    start = new Year(y);
            }

            // allow floating-point values here
            double d;
	    try {
		d = Double.parseDouble(tok.nextToken());
	    } catch (NoSuchElementException nsee) { // not enough tokens
		throw new WrongFiletypeException();
	    } catch (NumberFormatException nfe) { // not a number
		throw new WrongFiletypeException();
	    }
            s.data.add(new Integer((int) Math.round(d)));
            
            if (hasCount) {
                int c = Integer.parseInt(tok.nextToken());
                s.count.add(new Integer(c));
            }
        }

        // close file (asap)
        r.close();

        // the 800 rule.
        s.guessIndexed();

        // set range
        s.range = new Range(start, s.data.size());

        // return it
        return s;
    }

    // if there's a line with text (letters) on it (up to 120 chars long),
    // put it in the comments field, and skip past it.
    private void maybeEatCrap(Sample s) throws IOException {
        boolean isCrap = false;
        r.mark(120);
        String l = r.readLine();
	if (l == null)
	    throw new WrongFiletypeException(); // no data!
        for (int i=0; i<l.length(); i++) {
            if (Character.isLetter(l.charAt(i))) {
                isCrap = true;
                break;
            }
        }
        if (isCrap)
            s.meta.put("comments", "Header line was: \"" + l + "\"");
        else
            r.reset();
    }

    // given a string like "1 2 3", return an int array like double[] {1, 2, 3}.
    private double[] parseLine(String s) throws IOException {
	try {
	    StringTokenizer tok = new StringTokenizer(s, ",; \t");
	    int n = tok.countTokens();
	    double x[] = new double[n];
	    for (int i=0; i<n; i++)
		x[i] = Double.parseDouble(tok.nextToken());
	    return x;
	} catch (NumberFormatException nfe) {
	    throw new WrongFiletypeException();
	}
    }
    
    // editor needs this to save to a custom writer for the clipboard
    public TwoColumn(Writer wr) {
        w = new BufferedWriter(wr);
    }
    
    public TwoColumn() { } // if you have any other constructors, you don't get this for free
    
    /** Save tab-delimited data to a file.
	@param filename file to save the data to
	@param s the Sample to save
	@exception IOException if any problem with I/O occurs
	@see Sample */
    public void save(Sample s) throws IOException {
//	w = new BufferedWriter(new FileWriter(filename));

        Year y = s.range.getStart();
        boolean summed = s.isSummed();

        for (int i=0; i<s.data.size(); i++) {
            String line = y + "\t" + s.data.get(i);
            if (summed)
                line += "\t" + s.count.get(i);

            w.write(line);
            w.newLine();

            y = y.add(+1);
        }

        // close file
        w.close();
    }
}
