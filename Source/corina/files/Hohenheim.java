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
import corina.Weiserjahre;

import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
   <p>"Hohemheim" format.  I do not know what program uses this format
   natively.</p>

   <p>I have been given one .K file and one .M file, and been told to
   write a Hohemheim interface from that.  I'm making huge assumptions
   about Hohemheim's program.  The only guarantee I can make today is
   that this will correctly load ABI_HOH.K and ABI_HOH.M.  I don't
   even know what all the metadata fields are, so I can't save
   Hohemheim files.</p>

   <p>Found a partial spec in Frank Rinn's TSAP manual, page 39.  He
   calls it "Hemmenhofen".</p>

   <p>Apparently there are 2 Hohemheim file formats.  .K files are
   data-only, and .M files are data-count-weiserjahre.  Unfortunately,
   I'm not going to count on the filename extension being correct for
   figuring out what type it is, because apparently nobody ever cares
   enough to be consistent.  And the headers are identical, for better
   or for worse.  On one hand, I can use the same parser for that
   line, but on the other hand, I won't know until it's too late which
   parser to use for the data.</p>

   <p>Possible bug: it stops when it hits a value of 0.  On a sample
   that ends in a -9 year, I suppose it would not have any
   zero-padding, so I should watch readLine() as well.</p>

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class Hohenheim extends Filetype {

    /** Return the human-readable name of this file format.
	@return the name of this file format */
    public String toString() {
	return "Hohenheim";
    }

    /** Load a sample in Hohenheim format.
	@param filename file to load from
	@return Sample loaded from the file
	@exception FileNotFoundException if the file cannot be found
	@exception WrongFiletypeException if the file is obviously not
	this filetype
	@exception IOException if something goes wrong
	@see Sample */
    public Sample load() throws IOException {
	// set up a reader
//	r = new BufferedReader(new FileReader(filename));

	// make sure it's a hohenheim file: first lines starts with "+"
	String line = r.readLine();
	if (line==null || !line.startsWith("+"))
	    throw new WrongFiletypeException(); // no header found

	// new sample, with given filename
	Sample s = new Sample();

	// first line has the title after the "+" (padded)
	s.meta.put("title", line.substring(1).trim());

	// second line has all the metadata, range, etc.
	line = r.readLine();
	s.meta.put("species", line.substring(34, 37)); // #4: species
	// s.meta.put("sapwood", new Integer(line.substring(40, 43))); // #6: sapwood
	// ADD OTHER METADATA PARSING HERE!

	// start/end are in the first line, too
	Year start;
	try {
	    start = new Year(line.substring(53, 58));
	} catch (NumberFormatException nfe) {
	    throw new IOException("can't parse year: " + line);
	}

	// if i was just loading metadata, i'd just take end from
	// (59..64), but since i'm loading all of the data anyway,
	// i'll use the actual data length, in case it's wrong here.

	// column to start parsing at
	int column = 0;
	line = r.readLine();

	// summed files have length=76, raw files have length=40.  if
	// it's summed, make a new List for the counts.
	boolean isSummed = (line.length() > 70);
	if (isSummed)
	    s.count = new ArrayList();

	if (!isSummed) {

	    // data -- raw
	    for (;;) {
		// datum
		int datum=0;
		try {
		    datum = Integer.parseInt(line.substring(column, column+4).trim());
		} catch (NumberFormatException nfe) {
		    throw new IOException("Can't parse '" + nfe.getMessage() + "' as a number.");
		} catch (StringIndexOutOfBoundsException sioobe) {
		    throw new WrongFiletypeException();
		}

		// on zero, break;
		if (datum == 0)
		    break;

		// add this data
		s.data.add(new Integer(datum));

		// next column: 4 spaces away.  4 10's are 40, so skip back to 0.
		column += 4;
		if (column == 40) {
		    column = 0;
		    line = r.readLine();
		}
	    }

	} else {

	    // data -- summed
	    s.incr = new ArrayList();
	    s.decr = new ArrayList();
	    for (;;) {
		// datum, count, up, down
		int datum=0, count=0, up=0, dn=0;
		try {
		    datum = Integer.parseInt(line.substring(column, column+4).trim());
		    count = Integer.parseInt(line.substring(column+5, column+9).trim());
		    up = Integer.parseInt(line.substring(column+10, column+14).trim());
		    dn = Integer.parseInt(line.substring(column+15, column+19).trim());
		} catch (NumberFormatException nfe) {
		    throw new IOException("'" + nfe.getMessage() + "' isn't a number.");
		}

		// on zero, break;
		if (datum == 0)
		    break;

		// insert this data
		s.data.add(new Integer(datum));
		s.count.add(new Integer(count));
		s.incr.add(new Integer(up));
		s.decr.add(new Integer(dn));

		// next column: 19 spaces away.  4 19's are 76, so skip back to 0.
		column += 19;
		if (column > 70) {
		    column = 0;
		    line = r.readLine();
		}
	    }
	}

	// close file
        r.close();

	// compute range
	s.range = new Range(start, s.data.size());

	// return it
	return s;
    }

    /** Save a Sample to disk.  I don't know how to save Hohenheim
	samples, yet, so this just throws an exception.
	@param filename the file to save to
	@param s the sample to save
	@exception IOException if an I/O exception occurs */
    public void save(Sample sample) throws IOException {
	// simply this mess!
//	w = new BufferedWriter(new FileWriter(filename));

	// header -- 2 lines of metadata: WRITE ME
	StringBuffer line1, line2;
	line1 = new StringBuffer("+                                        " +
				 "                                    ");
	w.write(line1.toString());
	w.newLine();
	line2 = new StringBuffer(".                                        " +
				 "                                      ");

	// write out the range/start/end, "%5d"-style
	StringBuffer sp;

	sp = new StringBuffer(String.valueOf(sample.range.span()));
	while (sp.length() < 5) sp.insert(0, ' ');
	line2.replace(47, 52, sp.toString());

	sp = new StringBuffer(sample.range.getStart().toString());
	while (sp.length() < 5) sp.insert(0, ' ');
	line2.replace(53, 58, sp.toString());

	sp = new StringBuffer(sample.range.getEnd().toString());
	while (sp.length() < 5) sp.insert(0, ' ');
	line2.replace(59, 64, sp.toString());

	w.write(line2.toString());
	w.newLine();

	if (sample.isSummed()) {

	    int i, n = sample.data.size();
	    for (i=0; i<n; i++) {
		w.write(leftPad(sample.data.get(i).toString(), 4));
		w.write(leftPad(sample.count.get(i).toString(), 5));
		w.write(leftPad(sample.incr.get(i).toString(), 5));
		w.write(leftPad(sample.decr.get(i).toString(), 5));

		if (i % 4 == 3)
		    w.newLine();
	    }

	    i--; // step back one?
	    if (i % 4 != 3) {
		while (i++ % 4 != 3)
		    w.write("   0    0    0    0");
		w.newLine();
	    }

	} else { // not summed

	    int i, n = sample.data.size();
	    for (i=0; i<n; i++) {
		String x = leftPad(sample.data.get(i).toString(), 4);
		w.write(x);

		if (i % 10 == 9)
		    w.newLine();
	    }

	    i--; // step back one?
	    if (i % 10 != 9) {
		while (i++ % 10 != 9)
		    w.write("   0");
		w.newLine();
	    }
	}

	// close
	w.close();
    }
}
