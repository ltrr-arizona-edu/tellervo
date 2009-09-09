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

package edu.cornell.dendro.corina.formats;

import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.util.StringUtils;
import edu.cornell.dendro.corina.ui.I18n;

import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
   "Hohemheim" format.  I do not know what program uses this format
   natively.

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

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Hohenheim implements Filetype {

    @Override
	public String toString() {
	return I18n.getText("format.hohenheim");
    }

    public String getDefaultExtension() {
	return ".M"; // ???
    }

    public Sample load(BufferedReader r) throws IOException {
	// make sure it's a hohenheim file: first lines starts with "+"
	String line = r.readLine();
	if (line==null || !line.startsWith("+"))
	    throw new WrongFiletypeException(); // no header found

	// new sample, with given filename
	Sample s = new Sample();

	// first line has the title after the "+" (padded)
	s.setMeta("title", line.substring(1).trim());

	// second line has all the metadata, range, etc.
	line = r.readLine();
	s.setMeta("species", line.substring(34, 37)); // #4: species
	// s.setMeta("sapwood", new Integer(line.substring(40, 43)));
	   // #6: sapwood
	// WHY is this disabled?

	// -- TODO: add other metadata parsing here!

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
	    s.setCount(new ArrayList());

	if (!isSummed) {

	    // data -- raw
	    for (;;) {
		// datum
		String text="";
		int datum=0;
		try {
		    text = line.substring(column, column+4).trim();
		    datum = Integer.parseInt(text);
		} catch (NumberFormatException nfe) {
		    throw new IOException("Can't parse '" + text +
					  "' as a number.");
		} catch (StringIndexOutOfBoundsException sioobe) {
		    throw new WrongFiletypeException();
		}

		// on zero, break;
		if (datum == 0)
		    break;

		// add this data
		s.getData().add(new Integer(datum));

		// next column: 4 spaces away.  4 10's are 40, so skip
		// back to 0.
		column += 4;
		if (column == 40) {
		    column = 0;
		    line = r.readLine();
		}
	    }

	} else {

	    // data -- summed
	    s.setWJIncr(new ArrayList());
	    s.setWJDecr(new ArrayList());
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
		s.getData().add(new Integer(datum));
		s.getCount().add(new Integer(count));
		s.getWJIncr().add(new Integer(up));
		s.getWJDecr().add(new Integer(dn));

		// next column: 19 spaces away.  4 19's are 76, so
		// skip back to 0.
		column += 19;
		if (column > 70) {
		    column = 0;
		    line = r.readLine();
		}
	    }
	}

	// compute range
	s.setRange(new Range(start, s.getData().size()));

	// return it
	return s;
    }

    public void save(Sample sample, BufferedWriter w) throws IOException {
	// header -- 2 lines of metadata: WRITE ME
	StringBuffer line1, line2;
	line1 = new StringBuffer("+                                        " +
				 "                                    ");
	w.write(line1.toString());
	w.newLine();

	line2 = new StringBuffer(".                                        " +
				 "                                      ");

	// write out the range/start/end, "%5d"-style
	line2.replace(47, 52, StringUtils.leftPad(String.valueOf(sample.getRange().span()), 5));
	line2.replace(53, 58, StringUtils.leftPad(sample.getRange().getStart().toString(), 5));
	line2.replace(59, 64, StringUtils.leftPad(sample.getRange().getEnd().toString(), 5));

	w.write(line2.toString());
	w.newLine();

	if (sample.isSummed()) {

	    int i, n = sample.getData().size();
	    for (i=0; i<n; i++) {
		w.write(StringUtils.leftPad(sample.getData().get(i).toString(), 4));
		w.write(StringUtils.leftPad(sample.getCount().get(i).toString(), 5));
		w.write(StringUtils.leftPad(sample.getWJIncr().get(i).toString(), 5));
		w.write(StringUtils.leftPad(sample.getWJDecr().get(i).toString(), 5));

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

	    int i, n = sample.getData().size();
	    for (i=0; i<n; i++) {
		String x = StringUtils.leftPad(sample.getData().get(i).toString(), 4);
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
    }

	public Boolean isMultiFileCapable() {
		return false;
	}

	public String getDeficiencyDescription() {
		return this.toString() + " file format has limited metadata capabilities.";
	}

	public Boolean isLossless() {
		return false;
	}
}
