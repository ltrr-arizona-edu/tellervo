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

package corina.util;

import java.io.FileInputStream;
import java.io.IOException;

/**
    Utility functions for working with GZip compression.

    @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
    @version $Id$
*/
public class GZIP {
    private GZIP() {
        // don't instantiate me
    }

    /**
        Check if the specified file is gzip-compressed.  (Gzip
	compression has a distinctive 2-byte header which I can easily
	check.)
        
	@param filename the filename of the file to check
	@return true, if the file is gzip-compressed, else false
	@exception IOException if there was a problem loading 2 bytes
	from the file */
    public static boolean isCompressed(String filename) throws IOException {
        FileInputStream fis=null; // (make compiler happy)
        
        try {
            fis = new FileInputStream(filename);
            int b1 = fis.read();
            int b2 = fis.read();
            boolean compressed = (b1 == 0x1f && b2 == 0x8b); // ioe here is fine
            // FIXME: use GZIPInputStream.GZIP_MAGIC here!
            return compressed;
        } finally {
            if (fis != null) // it can be null if the file didn't exist
		fis.close();
        }
    }
}
