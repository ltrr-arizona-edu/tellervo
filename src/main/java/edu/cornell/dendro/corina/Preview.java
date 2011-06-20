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

package edu.cornell.dendro.corina;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
   A Preview is a short summary of a document to show to the user when
   she's trying to decide which document to open.  It consists of a
   title, and any number of bulleted items.

   <p>The Preview is shown for file-open dialogs.  For example, the Preview
   for a Sample might look like:</p>

   <div style="border: 1px black solid">
     <p><b>Zonguldak 34E (1001-1036)</b></p>
     <ul>
       <li>Species: Quercus sp.
       <li>Format: Corina
       <li>Indexed
     </ul>
   </div>

   <p>To make a Preview for something, implement Previewable, and
   have getPreview() return a new Preview for your object.  You'll
   need to extend Preview for the Object you return, and set the
   title, and add any lines you want.</p>

   <h2>Left to do</h2>
   <ul>
     <li>I18n for inner classes
   </ul>

   @see edu.cornell.dendro.corina.gui.PreviewComponent
   @see edu.cornell.dendro.corina.Previewable
 
   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public abstract class Preview {

    /** Make a new Preview. */
    public Preview() {
        // nothing: this block exists just for the javadoc tag
    }

    /** The title of this file. */
    protected String title="";

    /**
       Get the title of this preview.
    
       @return the title
    */
    public final String getTitle() {
	return title;
    }

    /** The items for this preview, to display in a list.
        Each item should be a String. */
    protected List<String> items = new ArrayList<String>();

    /**
       Return the list of items.  (The returned List is immutable.)
        
       @return the list of items
    */
    public final List<String> getItems() {
        return Collections.unmodifiableList(items);
    }

    /** A Preview for "Not a dendro data file".  The title contains the text
        "Not a dendro data file", and there are no items. */
    public static class NotDendroDataPreview extends Preview {
        /** Make a new "Not a dendro data file" preview. */
	public NotDendroDataPreview() {
	    title = "Not a dendro data file";
	}
    }
    
    /** A Preview for "Too many samples selected".  The title contains the text
    "xx items selected", and there are no items. */
public static class TooManySelectedPreview extends Preview {
public TooManySelectedPreview(int size) {
    title = size + " items selected";
}
}
    

    /** A preview for "Error loading".  The title is "Error Loading", and
        it contains one item: the result of e.getMessage(). */
    public static class ErrorLoadingPreview extends Preview {
        /**
           Make a new "Error loading" preview.

           @param e the exception to use
        */
	public ErrorLoadingPreview(Exception e) {
	    title = "Error loading";
	    items.add(e.getMessage());
	}
    }
}
