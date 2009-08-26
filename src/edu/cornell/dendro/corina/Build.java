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

import java.io.*;

/**
   The build timestamp.

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Build {
    // don't instantiate me
    private Build() {
    }

    /**
       The date and time Corina was compiled.

       <p>This is computed at compile-time by running <code>date "+%Y
       %B %e %l:%M %p (%Z)"</code>, which returns a string like "23
       October 6 5:06 PM (EDT)".  This string is stored in the file
       Timestamp, which is added to the final Corina.jar file.</p>
    */
    public final static String TIMESTAMP = loadTimestamp();

    // read the first line of the text file "Timestamp", included in
    // this jar, and return it
    private static String loadTimestamp() {
	try {
	    ClassLoader cl = edu.cornell.dendro.corina.Build.class.getClassLoader();
	    InputStream is = cl.getResourceAsStream("Timestamp");
      if (is != null) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
	      try {
   	      return br.readLine();
        } finally {
          br.close();
        }
      }
	} catch (IOException ioe) {
	    new edu.cornell.dendro.corina.gui.Bug(ioe);
	}

	return "(time/date unknown)";
    }

    /** Version string. */
    public final static String VERSION = "2.10"; // beta

    /** Year (for copyright). */
    public final static String YEAR = "2001-2009";

    /** Author's name (for copyright). */
    public final static String AUTHOR = "Peter Brewer, Lucas Madar, Ken Harris and Aaron Hamid";
}
