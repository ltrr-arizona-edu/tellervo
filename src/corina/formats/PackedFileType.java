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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

/**
   A 'packed' file can save multiple samples in a single file. It has no facilities for loading;
   these are handled by the normal format readers.
   
   Currently, only packed tucson is what we have.
*/
public interface PackedFileType {
    /**
       Save a sample list to a writer.

       @param s the sample list to save
       @param w the writer to write to
    */
    public void saveSamples(List<Sample> sl, BufferedWriter w) throws IOException;
}
