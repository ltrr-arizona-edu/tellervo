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
import edu.cornell.dendro.corina.Sample;
import edu.cornell.dendro.corina.Weiserjahre;
import edu.cornell.dendro.corina.util.StringUtils;
import edu.cornell.dendro.corina.ui.I18n;

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
		if (line == null || !line.startsWith("HEADER:"))
			throw new WrongFiletypeException(); // no HEADER: found

		// new sample, with given filename
		Sample s = new Sample();

		// don't know end, yet
		Year end = null;
		int length = -1;

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
			String value = line.substring(i + 1);

			// got end-date.
			if (tag.equals("DateEnd"))
				end = new Year(value);

			if (tag.equals("Length"))
				length = Integer.parseInt(value);

			// WRITE ME: parse other tags and interpret metadata as
			// intelligently as possible
			if(tag.equals("Species"))
				s.meta.put("species", value);
			else if(tag.equals("Location"))
				s.meta.put("title", value);
		}

		// if we got here, line starts with DATA:...
		String dataFormat = line.substring(5);
				
		// no end?  die.
		if (end == null)
			throw new WrongFiletypeException();
		
		StreamTokenizer t = new StreamTokenizer(r);
		
		if(dataFormat.compareToIgnoreCase("single") == 0 || dataFormat.compareToIgnoreCase("tree") == 0) {
			parseSingle(s, t, length);
		}
		else if(dataFormat.compareToIgnoreCase("double") == 0) {
			parseDouble(s, t, length);
		}
		else if(dataFormat.compareToIgnoreCase("chrono") == 0) {
			parseFull(s, t, length);
		}
		else {
			// hmm. what should we do here?
			// let's call this a bug, so we get better user feedback.

			IOException ioe = new IOException("Couldn't open Heidelberg data format: " + dataFormat);
			new edu.cornell.dendro.corina.gui.Bug(ioe);
			throw ioe;
		}

		// set range, and return
		s.range = new Range(end.add(1 - s.data.size()), end);
		return s;
	}

	private void parseFull(Sample s, StreamTokenizer t, int length) throws IOException {
		int idx = 0;

		// data -- i'll assume all data is (width,count,up,down)

		s.count = new ArrayList();
		s.incr = new ArrayList();
		s.decr = new ArrayList();
		
		for (;;) {
			// parse (datum, count, up, dn)
			int datum, count, up, dn;
			try {
				t.nextToken();
				datum = (int) t.nval;
				t.nextToken();
				count = (int) t.nval;
				t.nextToken();
				up = (int) t.nval;
				t.nextToken();
				dn = (int) t.nval;
			} catch (IOException ioe) {
				throw new WrongFiletypeException();
			}

			// (0,0,0,0) seems to mean end-of-sample
			if (datum == 0)
				break;

			// add to lists
			s.data.add(new Integer(datum));
			s.count.add(new Integer(count));
			s.incr.add(new Integer(up));
			s.decr.add(new Integer(dn));

			idx++;

			// break out if we have 'length' samples
			if (idx == length)
				break;
		}
	}
	
	private void parseSingle(Sample s, StreamTokenizer t, int length) throws IOException {
		int idx = 0;

		// data -- i'll assume all data is just a width
		
		for (;;) {
			// parse (datum)
			int datum;
			try {
				t.nextToken();
				datum = (int) t.nval;
			} catch (IOException ioe) {
				throw new WrongFiletypeException();
			}

			// (0) seems to mean end-of-sample
			if (datum == 0)
				break;

			// add to lists
			s.data.add(new Integer(datum));

			idx++;

			// break out if we have 'length' samples
			if (idx == length)
				break;
		}
		
	}

	private void parseDouble(Sample s, StreamTokenizer t, int length) throws IOException {
		int idx = 0;

		// data -- i'll assume all data is (width,count)

		s.count = new ArrayList();
		
		for (;;) {
			// parse (datum, count)
			int datum, count;
			try {
				t.nextToken();
				datum = (int) t.nval;
				t.nextToken();
				count = (int) t.nval;
			} catch (IOException ioe) {
				throw new WrongFiletypeException();
			}

			// (0,0) seems to mean end-of-sample
			if (datum == 0)
				break;

			// add to lists
			s.data.add(new Integer(datum));
			s.count.add(new Integer(count));

			idx++;

			// break out if we have 'length' samples
			if (idx == length)
				break;
		}
		
	}


	public void save(Sample s, BufferedWriter w) throws IOException {
		// header
		w.write("HEADER:");
		w.newLine();
		w.write("DateEnd=" + s.range.getEnd());
		w.newLine();
		w.write("Length=" + s.data.size());
		w.newLine();
		
		String tmp;
		if((tmp = (String) s.meta.get("title")) != null) {
			w.write("Location=" + tmp);
			w.newLine();
		}
		if((tmp = (String) s.meta.get("species")) != null) {
			w.write("Species=" + tmp);
			w.newLine();
		}

		// WRITE ME: finish out the header stuff

		// NOTE
		// We convert all values of '0' to '1' here.
		// This is heidelberg's signal for end. Hmm.
		// This should be better documented
		// (BUG: what if it's negative?)
		// (ICK: if it changes the data, it's harder to test)
		
		// We're writing out a 'tree' or a 'single'...
		// Data is for a single sample, not a sum or anything.
		if(s.count == null) {
			w.write("DATA:Tree");
			w.newLine();
			int column = 0;

			for (int i = 0; i < s.data.size(); i++) {
				String datum = s.data.get(i).toString();
				if (datum.equals("0"))
					datum = "1"; 
				
				// six space padding in this type of file
				writeDatum6(w, datum);
				
				// newline every 10
				if (column % 10 == 9)
					w.newLine();
				
				column++;
			}
			
			// extra 0's to pad
			while (column % 10 != 0) {
				w.write("     0");
				column++;
			}
			w.newLine();
		}
		// Data is for a sum without Weiserjahre data
		else if(!s.hasWeiserjahre()) {
			w.write("DATA:Double");
			w.newLine();
			int column = 0;

			for (int i = 0; i < s.data.size(); i++) {
				String datum = s.data.get(i).toString();
				if (datum.equals("0"))
					datum = "1"; 
				
				// six space padding in this type of file
				writeDatum6(w, datum);
				writeDatum6(w, s.count.get(i));
				
				// newline every 5
				if (column % 5 == 4)
					w.newLine();
				
				column++;
			}
			
			// extra 0's to pad
			while (column % 5 != 0) {
				w.write("     0     0");
				column++;
			}
			w.newLine();
			
		}
		else {
			boolean countIsNull = (s.count == null);
			boolean wjIsNull = !s.hasWeiserjahre();
			
			// data
			w.write("DATA:Chrono"); // what's "chrono" mean?
			w.newLine();
			int column = 0;
			for (int i = 0; i < s.data.size(); i++) {
				String datum = s.data.get(i).toString();
				if (datum.equals("0"))
					datum = "1"; // DOCUMENT me!
				// (BUG: what if it's negative?)
				// (ICK: if it changes the data, it's harder to test)
				String count = (countIsNull ? "0" : s.count.get(i).toString());
				String up = (wjIsNull ? "0" : s.incr.get(i).toString());
				String dn = (wjIsNull ? "0" : s.decr.get(i).toString());

				// output ("%-5d%-5d%-5d%-5d", datum, count, up, dn)
				writeDatum5(w, datum);
				// WAS: w.write(StringUtils.leftPad(s.data.get(i).toString(), 5));
				writeDatum5(w, (countIsNull ? "0" : s.count.get(i)));
				// WAS: w.write(StringUtils.leftPad(countIsNull ? "0" : s.count.get(i).toString(), 5));
				w.write(StringUtils.leftPad(wjIsNull ? "0" : s.incr.get(i)
						.toString(), 5));
				w.write(StringUtils.leftPad(wjIsNull ? "0" : s.decr.get(i)
						.toString(), 5));

				// newline every 4
				if (column % 4 == 3)
					w.newLine();

				column++;
			}

			// COMBINE these: newline-every-4, and extra-0's-to-pad both
			// care about exactly one value: position (column).

			// extra 0's to pad
			while (column % 4 != 0) {
				w.write("    0    0    0    0");
				column++;
			}
			w.newLine();
		}
	}

	private void writeDatum5(BufferedWriter w, Object o) throws IOException {
		w.write(StringUtils.leftPad(o.toString(), 5));
	}

	private void writeDatum6(BufferedWriter w, Object o) throws IOException {
		w.write(StringUtils.leftPad(o.toString(), 6));
	}
}
