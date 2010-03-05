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
   "TSAP Matrix Format".  I think these files we have come from
   Hohenheim's program.  They all end in .OUT, so I suspect this is
   Hohenheim trying to export in Heidelberg, though the TSAP reference
   manual doesn't mention this filetype.  Maybe it's a deprecated
   format.

   <p>The first line is always "TSAP-MATRIX-FORMAT".  It's a fairly
   straightforward text-based format.</p>

   <h2>Left to do</h2>
   <ul>
     <li>i18n
     <li>Bug with zero-year handling
     <li>Input is too inflexible (if spaces are wrong, won't load)
     <li>Input is too flexible: doesn't check for second header line
     <li>Doesn't deal with any other metadata (Project, Location, etc.)
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class TSAPMatrix implements Filetype {

    @Override
	public String toString() {
	return I18n.getText("format.tsap_matrix") + " (*"+ getDefaultExtension()+")";
    }

    /**
       Return the default extension for files of this type.  All of
       the files I've seen end with ".out", so that's what I'll use.

       @return the string ".out"
    */
    public String getDefaultExtension() {
	return ".out";
    }

    public Sample load(BufferedReader r) throws WrongFiletypeException,
						IOException {
	// make sure it's a tsap-matrix file
	String line = r.readLine();
	if (line==null || !line.startsWith("TSAP-MATRIX-FORMAT"))
	    throw new WrongFiletypeException(); // incorrect header found

        // new sample, with given filename.  as far as i know, all
        // TSAP-MATRIX-FORMAT files are summed, so make wj and count
        // Lists, too.
        Sample s = new Sample();
        s.setWJIncr(new ArrayList()); // are all TSAP-MATRIX files summed?
        s.setWJDecr(new ArrayList());
        s.setCount(new ArrayList());

        // after the TSAP-... header line, there are a series of "Tag:
        // value" lines, then an empty line
        for (;;) {
            line = r.readLine();

            // stop on empty line
            if (line.length() == 0)
                break;

            // as a temporary fix, so we don't lose data, let's just
            // append all the metadata lines to the comments field.
            if (!s.hasMeta("comments"))
                s.setMeta("comments", line.trim());
            else
                s.setMeta("comments", s.getMeta("comments") + "\n" + line.trim());

            // -- TODO: store metadata lines appropriately here
        }

	// now there's a line that says "Year, 100 Val 100 Nos ...".
	// skip it, too.
	r.readLine();

	// this is the start year, initially null;
	Year start=null;

	// now there's a series of lines: " year, data, count, up, down"
	for (;;) {
	    // read the line
	    line = r.readLine();

	    // stop at the end
	    if (line==null || line.length()==0)
		break;

	    // from the first line, read the year
	    try {
		if (start == null)
		    start = new Year(line.substring(3, 8), true); // true => "in a zero-year system"
	    } catch (NumberFormatException nfe) {
		throw new IOException("can't parse year: " + line);
	    }

	    // FIXME: above(year), below -- don't use absolute positions,
	    // in case somebody edits the file by hand and they get off --
	    // just read 5 ints.  (don't i have a int[] readInts() method somewhere?)

	    // data, count, up, down
	    int datum=0, count=0, up=0, dn=0;
	    try {
		datum = Integer.parseInt(line.substring(20, 24).trim());
		count = Integer.parseInt(line.substring(37, 40).trim());
		up = Integer.parseInt(line.substring(53, 56).trim());
		dn = Integer.parseInt(line.substring(69, 72).trim());
	    } catch (NumberFormatException nfe) {
		throw new IOException("Can't parse '" + nfe.getMessage() +
				      "' as a number.");
		// (can i do anything more with this?)
	    } catch (StringIndexOutOfBoundsException sioobe) {
		throw new WrongFiletypeException();
		// (why would a SIOOBE occur?  if it can't happen, say so!)
	    }

	    // insert this data
	    s.getData().add(new Integer(datum));
	    s.getCount().add(new Integer(count));
	    s.getWJIncr().add(new Integer(up));
	    s.getWJDecr().add(new Integer(dn));
	}

	// might it be indexed?  suuuure...
	s.guessIndexed();

	// set range
	s.setRange(new Range(start, s.getData().size()));

	// return the result
	return s;
    }

    /**
       Save a Sample to disk.

       <p>(I don't know how to save TSAPMatrix samples, really, apart
       from seeing a couple files in this format, so these might not
       be readable by any program other than Corina.)</p>
    */
    public void save(Sample s, BufferedWriter w) throws IOException {
        // header
        w.write("TSAP-MATRIX-FORMAT");
        w.newLine();

        // WRITE ME: other header stuff here ("Project:", "Location:")

        w.newLine();
        w.write("    Year,       100 Val        100 Nos        100 Nois       100 Nods");
        w.newLine();
        for (int i=0; i<s.getData().size(); i++) {
	    // format: "     820,            129,              1,              0,              0"
	    // (year,width,count,up,down)

	    String year = s.getStart().add(i).toString();
	    year = StringUtils.leftPad(year, 8);

	    String width = s.getData().get(i).toString();
	    width = StringUtils.leftPad(width, 15);

	    // WRITEME: count, up, dn

	    // BUG: zero year is incorrect -- -5..+5 gets printed as -5..+4, should be -4..+5
            w.write(StringUtils.leftPad(s.getStart().add(i).toString(), 8)); // zero year
            w.write(',');
            w.write(StringUtils.leftPad(s.getData().get(i).toString(), 15));
            w.write(',');
            w.write(StringUtils.leftPad(s.hasCount() ? "1" : s.getCount().get(i).toString(), 15)); // count may be null?
            w.write(',');
	    // REFACTOR: this is way too low-level.  move to superclass?

            // may not have wj
            w.write(StringUtils.leftPad(s.hasWeiserjahre() ? s.getWJIncr().get(i).toString() : "0", 15));
            w.write(',');
            w.write(StringUtils.leftPad(s.hasWeiserjahre() ? s.getWJDecr().get(i).toString() : "0", 15));

            // newline
            w.newLine();
        }
    }

	public Boolean isPackedFileCapable() {
		return false;
	}

	public String getDeficiencyDescription() {
		return this.toString() + " file format has no metadata capabilities.";
	}

	public Boolean isLossless() {
		return false;
	}
}
