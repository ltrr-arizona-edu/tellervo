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

package corina.formats;

import corina.Year;
import corina.Range;
import corina.Sample;
import corina.Weiserjahre;
import corina.util.StringUtils;
import corina.ui.I18n;

import java.util.ArrayList;

import java.io.StreamTokenizer;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;

/**
   Frank Rinn's "Heidelberg" format.  I believe this format is the
   default format for his TSAP program.

   <p>This class is unfinished.  It doesn't load or save metadata yet,
   and assumes all files are summed.</p>

   <h2>Left to do</h2>
   <ul>
     <li>doesn't load or save metadata yet
     <li>assumes all files are summed
     <li>doesn't allow 0's in the data stream; either handle them
         properly, or don't allow them to be saved
	 (change unit test to add zeros)
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Heidelberg implements Filetype {

    public String toString() {
	return I18n.getText("format.heidelberg");
    }

    public String getDefaultExtension() {
	return ".mpl";
    }

    public Sample load(BufferedReader r) throws IOException {
	// make sure it's a heidelberg file
	String line = r.readLine();
	if (line==null || !line.startsWith("HEADER:"))
	    throw new WrongFiletypeException(); // no HEADER: found

	// new sample, with given filename
	Sample s = new Sample();

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

	// no end?  die.
	if (end == null)
	    throw new WrongFiletypeException();

	// set range, and return
	s.range = new Range(end.add(1 - s.data.size()), end);
	return s;
    }

    public void save(Sample s, BufferedWriter w) throws IOException {
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
	int column = 0;
	for (int i=0; i<s.data.size(); i++) {
	    String datum = s.data.get(i).toString();
	    if (datum.equals("0"))
		datum = "1"; // DOCUMENT me!
	    // (BUG: what if it's negative?)
	    // (ICK: if it changes the data, it's harder to test)
	    String count = (countIsNull ? "0" : s.count.get(i).toString());
	    String up = (wjIsNull ? "0" : s.incr.get(i).toString());
	    String dn = (wjIsNull ? "0" : s.decr.get(i).toString());

	    // output ("%-5d%-5d%-5d%-5d", datum, count, up, dn)
	    writeDatum(w, s.data.get(i));
	    // WAS: w.write(StringUtils.leftPad(s.data.get(i).toString(), 5));
	    writeDatum(w, (countIsNull ? "0" : s.count.get(i)));
	    // WAS: w.write(StringUtils.leftPad(countIsNull ? "0" : s.count.get(i).toString(), 5));
	    w.write(StringUtils.leftPad(wjIsNull ? "0" : s.incr.get(i).toString(), 5));
	    w.write(StringUtils.leftPad(wjIsNull ? "0" : s.decr.get(i).toString(), 5));

	    // newline every 4
	    if (column % 4 == 3)
	    	w.newLine();
	    
	    column++;	    
	}

	// COMBINE these: newline-every-4, and extra-0's-to-pad both
	// care about exactly one value: position (column).

	// EXTRACT method: writeData(data, count, incr, decr)

	// REFACTOR: use iterators instead of for loop?

	// extra 0's to pad
	while (column % 4 != 3) {
	    w.write("    0    0    0    0");
	    column++;
	}
	w.newLine();
    }

    private void writeData(BufferedWriter w,
			   String data, String count,
			   String incr, String decr) throws IOException {
	writeDatum(w, data);
	writeDatum(w, count);
	writeDatum(w, incr);
	writeDatum(w, decr);
    }

    private void writeDatum(BufferedWriter w, Object o) throws IOException {
	w.write(StringUtils.leftPad(o.toString(), 5));
    }
}
