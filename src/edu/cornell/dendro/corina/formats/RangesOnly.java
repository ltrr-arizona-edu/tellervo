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

import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.I18n;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
   The name and range of a sum's elements (no data).

   For a sum, output the name and range of each elem<p>ent, one per
   line, with columns separated by tabs, and elements separated by
   newlines.  For example:</p>

<pre>
Sample	Start	End	Span
ZKB-1   1001    1236    236
ZKB-2   1003    1072    70
ZKB-3   1011    1099    89
</pre>

   <p>(Useful for dumping into spreadsheets, or generating bar
   graphs.)</p>

   <h2>Left to do</h2>
   <ul>
     <li>i18n (2 ioe's)
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class RangesOnly implements Filetype, PackedFileType {

    @Override
	public String toString() {
        return I18n.getText("format.ranges_only");
    }

    public String getDefaultExtension() {
	return ".TXT";
    }

    /**
       Throws a WrongFiletypeException - RangesOnly files can't be
       loaded.  (This should never be called.)

       @return never returns
       @exception WrongFiletypeException always thrown
    */
    public Sample load(BufferedReader r) throws IOException {
        throw new WrongFiletypeException();
    }

    public void save(Sample s, BufferedWriter w) throws IOException {
		
		ArrayList<Sample> sl = new ArrayList<Sample>();
		sl.add(s);
		
		// and pass them to savesamples
		saveSamples(sl, w);


    }



	public void saveSamples(List<Sample> sl, BufferedWriter w)
			throws IOException {

		// write a header
		w.write(I18n.getText("sample"));
		w.write("\t");
		w.write(I18n.getText("browser_start"));
		w.write("\t");
		w.write(I18n.getText("browser_end"));
		w.write("\t");
		w.write(I18n.getText("browser_length"));
		w.newLine();

		
		
	        int n = sl.size();
	        for (int i=0; i<n; i++) {
	            Sample s = sl.get(i);

	            // output (name, start, end)
	            if (s.getDisplayTitle()!=null)
	                w.write(s.getDisplayTitle());
	            else
	                w.write("unknown");
	            w.write("\t");
	            w.write(s.getRange().getStart().toString());
	            w.write("\t");
	            w.write(s.getRange().getEnd().toString());
	            w.write("\t");
	            w.write(String.valueOf(s.getRange().span()));
		    w.newLine();
	        }
	}

	public String getDeficiencyDescription() {
		return this.toString() + " file format has no metadata capabilities.";
	}

	public Boolean isLossless() {
		return false;
	}
	
	public Boolean isMultiFileCapable() {
		return true;
	}
}
