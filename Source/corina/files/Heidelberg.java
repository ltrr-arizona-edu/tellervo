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

import java.io.StreamTokenizer;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
   <p>Frank Rinn's "Heidelberg" format.  I believe this format is the
   default format for his TSAP program.</p>

   <p>This class is unfinished.  It doesn't load or save metadata yet,
   and assumes all files are summed.</p>

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class Heidelberg extends Filetype {

    /** Return the human-readable name of this file format.
	@return the name of this file format */
    public String toString() {
	return "Heidelberg";
    }

    /** Load a sample in Heidelberg format.
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

	// make sure it's a heidelberg file
	String line = r.readLine();
	if (line==null || !line.startsWith("HEADER:"))
	    throw new WrongFiletypeException(); // no HEADER: found

	// new sample, with given filename
	Sample s = new Sample();
//	s.meta.put("filename", filename);

	// don't know end, yet
	Year end = null;

	// metadata
	for (;;) {
	    // read a line
	    line = r.readLine();

	    // got to data, stop
	    if (line.startsWith("DATA:"))
		break;

	    // parse line as "variable = value", and put into s.meta
	    int i = line.indexOf("=");
	    if (i == -1)
		throw new WrongFiletypeException();
	    String tag = line.substring(0, i);
	    String value = line.substring(i+1);

	    // got end-date.
	    if (tag.equals("DateEnd"))
		end = new Year(value);

	    // WRITE ME: parse other tags and interpret metadata as
	    // intelligently as possible
	}

	s.count = new ArrayList();
	s.incr = new ArrayList();
	s.decr = new ArrayList();

	// data -- i'll assume all data is (width,count,up,down)
	StreamTokenizer t = new StreamTokenizer(r);
	for (;;) {
	    // parse (datum, count, up, dn)
	    t.nextToken();
	    int datum = (int) t.nval;
	    t.nextToken();
	    int count = (int) t.nval;
	    t.nextToken();
	    int up = (int) t.nval;
	    t.nextToken();
	    int dn = (int) t.nval;

	    // (0,0,0,0) seems to mean end-of-sample
	    if (datum == 0)
		break;

	    // add to lists
	    s.data.add(new Integer(datum));
	    s.count.add(new Integer(count));
	    s.incr.add(new Integer(up));
	    s.decr.add(new Integer(dn));
	}

	// close file
	r.close();

	// no end?  die.
	if (end == null)
	    throw new WrongFiletypeException();

	// set range, and return
	s.range = new Range(end.add(1 - s.data.size()), end);
	return s;
    }

    /** Save a sample in Heidelberg format.
	@param filename file to save to
	@param s Sample to save
	@exception IOException if something goes wrong
	@see Sample */
    public void save(Sample s) throws IOException {
//        w = new BufferedWriter(new FileWriter(filename));

	// header
	w.write("HEADER:");
	w.newLine();
	w.write("DateEnd=" + s.range.getEnd());
	w.newLine();
	w.write("Length=" + s.data.size());
	w.newLine();

	// WRITE ME: finish out the header stuff

	// don't want to die if somebody tries to save a raw file
	boolean countIsNull = (s.count == null);
	boolean wjIsNull = !s.hasWeiserjahre();

	// data
	w.write("DATA:Chrono"); // what's "chrono" mean?
	w.newLine();
	int i; // this is used outside the loop BUT SHOULDN'T BE: FIXME
	for (i=0; i<s.data.size(); i++) {
	    String datum = s.data.get(i).toString();
	    String count = (countIsNull ? "0" : s.count.get(i).toString());
	    String up = (wjIsNull ? "0" : s.incr.get(i).toString());
	    String dn = (wjIsNull ? "0" : s.decr.get(i).toString());

	    // output ("%-5d%-5d%-5d%-5d", datum, count, up, dn)
	    w.write(leftPad(s.data.get(i).toString(), 5));
	    w.write(leftPad(countIsNull ? "0" : s.count.get(i).toString(), 5));
	    w.write(leftPad(wjIsNull ? "0" : s.incr.get(i).toString(), 5));
	    w.write(leftPad(wjIsNull ? "0" : s.decr.get(i).toString(), 5));

	    // newline every 4
	    if (i % 4 == 3)
		w.newLine();
	}

	// extra 0's to pad
	do {
	    w.write("    0    0    0    0");
	} while (i++%4 != 3);
	w.newLine();

	// close file
	w.close();
    }
}
