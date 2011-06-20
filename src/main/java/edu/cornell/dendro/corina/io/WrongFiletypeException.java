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

package edu.cornell.dendro.corina.io;

import java.io.IOException;

/**
   A file load operation was attempted with the wrong format loader.
   (Or, from the Sample(filename) constructor if no usable loader was
   found.)

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
@SuppressWarnings("serial")
public class WrongFiletypeException extends IOException {
    /** Make a new wrong filetype exception. */
    public WrongFiletypeException() {
        // nothing needed (this only exists for the javadoc tag)
    }
    
    public WrongFiletypeException(String s) {
    	// For now, just dump debug info
    	System.out.println("WFTE: " + s);
    }
}
