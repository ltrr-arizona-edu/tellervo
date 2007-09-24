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

package edu.cornell.dendro.corina.util;

import java.io.StringWriter;

/**
   A Writer for Strings like StringWriter, but using the
   <code>'\n'</code> character for newlines instead of the filesystem
   default, regardless of platform.

   <p>Useful for platforms where a newline on disk is different from a
   newline everywhere else (like Windows).  Suppose you want to print
   some data to a text area; if you use a normal StringWriter, it
   assumes you'll want to use the filesystem's newlines, so on Windows
   you'll get extra <code>'\r'</code> characters.</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class PureStringWriter extends StringWriter {

    /** Create a new PureStringWriter (with the default buffer size). */
    public PureStringWriter() {
	super();
    }

    /** Create a new PureStringWriter with the specified buffer size.
	@param bufsize the buffer size to use */
    public PureStringWriter(int bufsize) {
        super(bufsize);
    }

    /** Write a portion of a character array, except <code>'\r'</code>
	characters.
	@param cbuf the character array to read from
	@param off the offset into the array to start reading
	@param len the number of characters to read out of the array */
    public void write(char[] cbuf, int off, int len) {        
        for (int i=off; i<off+len; i++) {
            char c = cbuf[i];
            if (c != '\r')
                super.write(c);
        }
    }

   /*
     I could also implement these, if i wanted:
     - public void write(int c) { }
     - public void write(String str) { }
     - public void write(String str, int off, int len) { }
     that might help performance (and might not), but i'm not noticing any
     performance problems, so i'll leave them alone for now.
   */
}
