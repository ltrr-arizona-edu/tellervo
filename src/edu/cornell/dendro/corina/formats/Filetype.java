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

import edu.cornell.dendro.corina.Sample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
   This is the interface which all file formats must implement.

   <p>A file format which implements this interface should not have
   any object state.  That is, the methods, though they're object
   methods, should behave more like class methods -- one Filetype
   object may be used for multiple samples.  (They're object
   methods simply because Java doesn't allow class methods in
   interfaces.)</p>

   <p>A load() method should <i>not</i> call close() when it's done
   loading, especially if it couldn't load a sample from the reader,
   for performance reasons; though if it does, it will still work.  If
   a load() fails, Corina resets the stream to the start, and tries
   again with a different loader.  (If the steam was already closed, a
   new one will be automatically opened, but this is less efficient.
   SAX, for example, always closes its streams.)</p>

   <p>The save() method doesn't need to close the writer, but it
   doesn't matter if it does.  The writer will be closed immediately
   after use, and closing a writer twice has no effect.</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public interface Filetype {
    /**
       Load a sample from a reader.

       @param r the reader to read from
       @return the sample
    */
    public Sample load(BufferedReader r) throws IOException,
						WrongFiletypeException;

    /**
       Save a sample to a writer.

       @param s the sample to save
       @param w the writer to write to
    */
    public void save(Sample s, BufferedWriter w) throws IOException;

    /**
       The human-readable name of this file format.

       @return the name of this format
    */
    public String toString();

    /**
       The default filename extension for this file format, including
       the period.

       <p>For example, almost all HTML files will be saved as
       "name.html", so we can save the user some effort (and give him
       a hint, if he's unsure) by providing the ".html" for him.</p>

       <p>If there's no default extension (for some reason), it is appropriate
       to return "" (the empty string).</p>

       @return the default extension (including the period)
    */
    public String getDefaultExtension();
}
