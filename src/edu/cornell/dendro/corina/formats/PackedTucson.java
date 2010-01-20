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

import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.I18n;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
   A Tucson file containing multiple samples.

   WRITEME

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class PackedTucson extends Tucson implements PackedFileType {

    @Override
	public String toString() {
        return I18n.getText("format.packed_tucson");
    }

    @Override
	public String getDefaultExtension() {
	return ".TUC"; // ???
    }

    // can't load a packed tucson file (yet).  this should never get called,
    // but in case it does...
    @Override
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
    
    public void saveSamples(List<Sample> sl, BufferedWriter w) throws IOException {
    	List<Sample> outsamples = new ArrayList<Sample>(sl.size());
    	String prefix = null;
    	
    	// iterate through the samples, making a list of them and
    	// gathering data. 
    	// We follow the logic in save() below:
    	// if it's got elements, ignore the sample and use the elements.
    	// Otherwise, we save the sample.
    	
    	
    	//@todo Need to fix 'prefix' to something useful
    	for(int i = 0; i < sl.size(); i++) {
    		Sample s = (Sample) sl.get(i);
    		
    		if(s.getElements() != null) {
    			for(int j = 0; j < s.getElements().size(); j++) {
    				Sample tmp = (s.getElements().get(j)).load();
    				
    				try {
    					if(prefix == null){
    						//prefix = tmp.getMeta("id").toString();
    						prefix = s.getIdentifier().getValue().toString();
    						prefix = prefix.substring(prefix.length()-6);
    					}
    					else{
    						//prefix = commonPrefix(prefix, tmp.getMeta("id").toString());
        					prefix = s.getIdentifier().getValue().toString();
        					prefix = prefix.substring(prefix.length()-6);
    					}
    				} catch (NullPointerException npe) {
    					throw new IOException("Invalid META ID in file " + tmp.getMeta("filename"));
    				}
    				
    				outsamples.add(tmp);
    			}
    		} else {
    			try {
    				if(prefix == null){
    					//prefix = s.getMeta("id").toString();
    					prefix = s.getIdentifier().getValue().toString();
    					prefix = prefix.substring(prefix.length()-6);
    				}
    				else{
    					//prefix = commonPrefix(prefix, s.getMeta("id").toString());
    					prefix = s.getIdentifier().getValue().toString();
    					prefix = prefix.substring(prefix.length()-6);
    				}
    			} catch (NullPointerException npe) {
    				throw new IOException("Invalid META ID in file " + s.getMeta("filename"));
    			}
				
				outsamples.add(s);    			
    		}
    	}
    	
        // save the header, using that prefix
        save3LineHeader(w, prefix, (Sample) sl.get(0)); // was: "000   "
        
        for(int i = 0; i < outsamples.size(); i++)
        	saveData((Sample) outsamples.get(i), w);
    }

    @Override
	public void save(Sample s, BufferedWriter w) throws IOException {
        // make sure it's a master, else ioe
/*        if (s.getElements() == null)
            throw new IOException("Packed Tucson format is only available " +
				  "for summed samples with Elements");

        // load all samples into a buffer.  (this way, i can make the
        // 3-line header the maximal common prefix instead of a
        // generic "000")
        int n = s.getElements().size();
        Sample buf[] = new Sample[n];
        for (int i=0; i<n; i++)
            buf[i] = (s.getElements().get(i)).load();

        // figure out the common prefix (ugh, it's 'reduce again!)
        String prefix = buf[0].getMeta("id").toString();
        for (int i=1; i<n; i++)
            prefix = commonPrefix(prefix, buf[i].getMeta("id").toString());
        // (save3lineheader() automatically makes sure it's 6 chars long)

        // save the header, using that prefix
        save3LineHeader(w, prefix, s); // was: "000   "

        // IDEA: use commonprefix of title for header title?

        // save each element in turn
        for (int i=0; i<n; i++) {
            s = buf[i];
            saveData(s, w); // hackish!

            // BUG: might not be AD-only.  saveData() should call
            // verifyAD(), then, not save()
        }
        // (shouldn't that be saveData(elem)?)*/
    	
		ArrayList<Sample> sl = new ArrayList<Sample>();
		sl.add(s);
		
		// and pass them to savesamples
		saveSamples(sl, w);
	
    	
    	
    }

    // save a 3-line header -- itrdb uses these.
    // |id| is usually just 3 chars (like, "PFU").
    private void save3LineHeader(BufferedWriter w,
				 String id, Sample s) throws IOException {
	// ensure exactly 6 chars
	if (id.length() > 6)
	    id = id.substring(0, 6);
	else while (id.length() < 6)
	    id += " ";

	// write out lines -- mostly blank, fill in rest later?
	
	
	
	w.write(id + " 1 " + s.getDisplayTitle());
	w.newLine();
	w.write(id + " 2 ");
	w.newLine();
	w.write(id + " 3 Exported from Corina");
	w.newLine();
    }
    
    @Override
    public Boolean isPackedFileCapable()
    {
    	return true;
    }
}
