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

package corina.formats;

import corina.Sample;
import corina.Element;
import corina.ui.I18n;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
   A Tucson file containing multiple samples.

   WRITEME

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class PackedTucson extends Tucson {

    public String toString() {
        return I18n.getText("format.packed_tucson");
    }

    public String getDefaultExtension() {
	return ".TUC"; // ???
    }

    // can't load a packed tucson file (yet).  this should never get called,
    // but in case it does...
    public Sample load(BufferedReader r) throws IOException {
        throw new WrongFiletypeException();
    }

    // return the largest prefix (string) common to s1 and s2
    private static String commonPrefix(String s1, String s2) {
        int i;
        for (i=0; i<s1.length() && i<s2.length(); i++)
            if (s1.charAt(i) != s2.charAt(i))
                break;
        // now: i is the first differing character, so return everything before that
        return s1.substring(0, i);
    }

    public void save(Sample s, BufferedWriter w) throws IOException {
        // make sure it's a master, else ioe
        if (s.elements == null)
            throw new IOException("Packed Tucson format is only available " +
				  "for summed samples with Elements");

        // load all samples into a buffer.  (this way, i can make the
        // 3-line header the maximal common prefix instead of a
        // generic "000")
        int n = s.elements.size();
        Sample buf[] = new Sample[n];
        for (int i=0; i<n; i++)
            buf[i] = ((Element) s.elements.get(i)).load();

        // figure out the common prefix (ugh, it's 'reduce again!)
        String prefix = buf[0].meta.get("id").toString();
        for (int i=1; i<n; i++)
            prefix = commonPrefix(prefix, buf[i].meta.get("id").toString());
        // (save3lineheader() automatically makes sure it's 6 chars long)

        // save the header, using that prefix
        save3LineHeader(w, prefix); // was: "000   "

        // IDEA: use commonprefix of title for header title?

        // save each element in turn
        for (int i=0; i<n; i++) {
            s = buf[i];
            saveData(s, w); // hackish!

            // BUG: might not be AD-only.  saveData() should call
            // verifyAD(), then, not save()
        }
        // (shouldn't that be saveData(elem)?)
    }

    // save a 3-line header -- itrdb uses these.
    // |id| is usually just 3 chars (like, "PFU").
    private void save3LineHeader(BufferedWriter w,
				 String id) throws IOException {
	// ensure exactly 6 chars
	if (id.length() > 6)
	    id = id.substring(0, 6);
	else while (id.length() < 6)
	    id += " ";

	// write out lines -- mostly blank, fill in rest later?
	w.write(id + " 1 Untitled");
	w.newLine();
	w.write(id + " 2 ");
	w.newLine();
	w.write(id + " 3 ");
	w.newLine();
    }
}
