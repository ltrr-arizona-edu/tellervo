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
// Copyright 2003 Ken Harris <kbh7@cornell.edu>
//

package edu.cornell.dendro.corina.formats;

import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.Sample;
import edu.cornell.dendro.corina.ui.I18n;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
   A file format for exporting several datasets to a spreadsheet.

   <p>This format exports any number of datasets (the elements of a
   sample) into a text file suitable for loading in a spreadsheet.
   Once in the spreadsheet, you can easily graph or manipulate them
   using the spreadsheet's facilities.  You can use this if you want
   to try out a new way of looking at the data, and know how to use a
   spreadsheet, but don't know how to hack Corina.</p>

<pre>
Year      Sample A  Sample B  Sample C
1001                30        96
1002      49        86        69
1003      22        42
</pre>

   <p>There are tabs between each column, and newlines after each row.</p>

   <p>You don't even need to save this to a file; remember, the
   export-dialog has a "Copy" button, so you can simply copy the
   spreadsheet format and paste it into your spreadsheet.</p>

   <h2>Left to do</h2>
   <ul>
     <li>i18n -- 2 ioe's (also, I18n should be in util, not ui, right?)
     <li>if you can't load one of them, don't fail everything, just skip it
         (or: put its filename there, along with "can't load" in cell 1)
     <li>future: allow loading spreadsheet format files?
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Spreadsheet implements Filetype, PackedFileType {
    /**
       Make a new spreadsheet-saving Filetype.
    */
    public Spreadsheet() {
	// do nothing -- (only here for the javadoc tag)
    }

    /**
       Throw a WrongFiletypeException; spreadsheet format is
       write-only.

       (This should never get called.)

       @return never returns
       @exception WrongFiletypeException every time
    */
    public Sample load(BufferedReader r) throws IOException {
        throw new WrongFiletypeException();
    }

    @Override
	public String toString() {
        return I18n.getText("format.spreadsheet");
    }

    /**
       Return the extension ".TXT".  I don't know what format
       spreadsheets normally expect, but it's just a text file, so
       I'll use this for now.

       @return the string ".TXT"
    */
    public String getDefaultExtension() {
	return ".TXT";
    }


	// save
    // deprecated! this is a packed sample format!
	public void save(Sample s, BufferedWriter w) throws IOException {
		// verify it's a master
		if (s.getElements() == null)
			throw new IOException("Spreadsheet format is only available "
					+ "for summed samples with Elements");

		// load all the samples into a list
		List<Sample> slist = new ArrayList<Sample>();		
		for (int i = 0; i < s.getElements().size(); i++) {
			try {
				slist.add((s.getElements().get(i)).load());
			} catch (IOException ioe) {
				String filename = (s.getElements().get(i)).toString();
				throw new IOException("Can't load element " + filename);
			}			
		}
		
		// and pass them to savesamples
		saveSamples(slist, w);
	}

	// for PackedFileType
    public void saveSamples(List<Sample> sl, BufferedWriter w) throws IOException {
		// load all the elements into a buffer
		int n = sl.size();
		Range r = null;

		// make range a union of all the ranges.		
		for (int i = 0; i < n; i++) {
			if(r == null)
				r = ((Sample)sl.get(i)).getRange();
			else
				r = r.union(((Sample)sl.get(i)).getRange());
		}

		// write header
		w.write(I18n.getText("year"));
		for (int i = 0; i < n; i++)
			w.write("\t" + ((Sample)sl.get(i)).getMeta("title"));
		w.newLine();

		// save line(=year)-at-a-time
		for (Year y = r.getStart(); y.compareTo(r.getEnd()) <= 0; y = y.add(1)) {
			// write year
			w.write(y.toString());

			// write each datum for this year
			for (int i = 0; i < n; i++) {
				// tab
				w.write("\t");

				Sample s = (Sample) sl.get(i);
				
				// data[y]
				if (s.getRange().contains(y)) {
					Year start = s.getRange().getStart();
					int index = y.diff(start);
					w.write(s.getData().get(index).toString());
				}
			}

			// end-of-line
			w.newLine();
		}
	}
}
