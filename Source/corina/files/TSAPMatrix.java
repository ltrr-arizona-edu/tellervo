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

import java.io.IOException;

/**
   <p>"TSAP Matrix Format".  I think these files we have come from
   Hohenheim's program.  They all end in .OUT, so I suspect this is
   Hohenheim trying to export in Heidelberg, though the TSAP reference
   manual doesn't mention this filetype.  Maybe it's a deprecated
   format.</p>

   <p>The first line is always "TSAP-MATRIX-FORMAT".  It's a fairly
   straightforward text-based format.</p>

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class TSAPMatrix extends Filetype {

    /** Return the human-readable name of this file format.
	@return the name of this file format */
    public String toString() {
	return "TSAP";
    }

    /** Return a unique character in the name to use as a mnemonic.
	@return character to use as mnemonic */
    public char getMnemonic() {
	return 'P';
    }

    /** Load a sample in TSAP-MATRIX format.
	@param filename file to load from
	@return Sample loaded from the file
	@exception WrongFiletypeException if the file is obviously not
	this filetype
	@exception IOException if something goes wrong
	@see Sample */
    public Sample load() throws WrongFiletypeException, IOException {
	// make sure it's a tsap-matrix file
	String line = r.readLine();
	if (!line.startsWith("TSAP-MATRIX-FORMAT"))
	    throw new WrongFiletypeException(); // incorrect header found

	// new sample, with given filename.  as far as i know, all
	// TSAP-MATRIX-FORMAT files are summed, so make wj and count
	// Lists, too.
	Sample s = new Sample();
	s.incr = new ArrayList(); // are all TSAP-MATRIX files summed?
	s.decr = new ArrayList();
	s.count = new ArrayList();
//	s.meta.put("filename", filename);

	// after the TSAP-... header line, there are a series of "Tag:
	// value" lines, then an empty line
	for (;;) {
	    line = r.readLine();

	    // stop on empty line
	    if (line.length() == 0)
		break;

	    // as a temporary fix, so we don't lose data, let's just
	    // append all the metadata lines to the ;comments field.
	    if (s.meta.get("comments") == null)
		s.meta.put("comments", line.trim());
	    else
		s.meta.put("comments", s.meta.get("comments") + "\n" + line.trim());

	    // WRITE ME: store metadata lines appropriately
	}

	// now there's a line that says "Year, 100 Val  100 Nos ...".  skip it, too.
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
	    if (start == null)
		start = new Year(line.substring(3, 8), true); // true => "in a zero-year system"

	    // data, count, up, down
	    int datum=0, count=0, up=0, dn=0;
	    try {
		datum = Integer.parseInt(line.substring(20, 24).trim());
		count = Integer.parseInt(line.substring(37, 40).trim());
		up = Integer.parseInt(line.substring(53, 56).trim());
		dn = Integer.parseInt(line.substring(69, 72).trim());
	    } catch (NumberFormatException nfe) {
		throw new IOException("Can't parse '" + nfe.getMessage() + "' as a number.");
	    }

	    // insert this data
	    s.data.add(new Integer(datum));
	    s.count.add(new Integer(count));
	    s.incr.add(new Integer(up));
	    s.decr.add(new Integer(dn));
	}

	// close file
	r.close();

	// set range
	s.range = new Range(start, s.data.size());

	// return the result
	return s;
    }

    /** Save a Sample to disk.  I don't know how to save TSAPMatrix
	samples, really, so these might not be readable by any program
	other than Corina.
	@param filename the file to save to
	@param s the sample to save
	@exception IOException if an I/O exception occurs */
    public void save(Sample sample) throws IOException {
        // header
        w.write("TSAP-MATRIX-FORMAT");
        w.newLine();

        // WRITE ME: other header stuff here ("Project:", "Location:")

        w.newLine();
        w.write("    Year,       100 Val        100 Nos        100 Nois       100 Nods");
        w.newLine();
        for (int i=0; i<sample.data.size(); i++) { // zero year!
                                                   // format: "     820,            129,              1,              0,              0"
                                                   // (year,width,count,up,down)

            w.write(leftPad(sample.getStart().add(i).toString(), 8));
            w.write(',');
            w.write(leftPad(sample.data.get(i).toString(), 15));
            w.write(',');
            w.write(leftPad(sample.count==null ? "1" : sample.count.get(i).toString(), 15)); // count may be null?
            w.write(',');

            w.write(leftPad(sample.incr==null ? "0" : sample.incr.get(i).toString(), 15)); // incr may be null?
            w.write(',');
            w.write(leftPad(sample.decr==null ? "0" : sample.decr.get(i).toString(), 15)); // decr may be null?

            // newline
            w.newLine();
        }

        // close file
        w.close();
    }
}
