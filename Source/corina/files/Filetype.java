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

package corina.files;

import corina.Sample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringWriter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
   <p>Interface for loading and saving a Sample to/from a file on
   disk.</p>

   <p>A file-format class should contain in its Javadoc documentation
   the complete format specification, sufficiently complete to
   re-implement a loader/saver.</p>

   @see corina.Sample

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public abstract class Filetype {

    // [audit for resets, close-reopens, etc.?]

    //
    // for the programmer: a filetype has to implement these
    //

    // everybody uses these
    protected BufferedReader r;
    protected BufferedWriter w;

    // you have to implement these
    public Sample load(String filename) throws FileNotFoundException,
        WrongFiletypeException,
        IOException {
            r = new BufferedReader(new FileReader(filename));
            Sample s = load();
            s.meta.put("filename", filename);
            return s;
        }
    /*
    public Sample load(java.net.URL url) throws IOException {
	try {
	    Class.forName("corina.browser.ItrdbURLConnection");
	} catch (Exception e) {
	    // ignore
	}
	r = new BufferedReader(new java.io.InputStreamReader(url.openStream()));
	Sample s = load();
	s.meta.put("filename", url.toString());
	return s;
    }
    */
    public abstract Sample load() throws IOException; // from |r|

    public void save(String filename, Sample sample) throws IOException {
        w = new BufferedWriter(new FileWriter(filename));
        save(sample);
    }
    public StringBuffer saveToBuffer(Sample sample) throws IOException {
        StringWriter sw = new StringWriter(4096); // 16 is way too small for most things.
        w = new BufferedWriter(sw);
        save(sample);
        return sw.getBuffer();
    }
    public abstract void save(Sample sample) throws IOException;

    // and here are some nifty tools to help you
    protected String leftPad(String orig, int size) {
	StringBuffer buf = new StringBuffer(orig);
	while (buf.length() < size)
	    buf.insert(0, ' ');
	return buf.toString();
    }

    //
    // for the user-interface
    //

    /** Return the human-readable name of this file format.  In the
	user-interface, this is the title the user should normally
	see, so it should itself be enough to determine exactly what
	filetype it is.
	@return the name of this file format */
    public abstract String toString();

    /** Return a unique character in the name to use as a mnemonic.
	(A mnemonic is a character from the title that's usually
	underlined, so the user can activate it quickly through a
	keypress.)  Every file format should have a mnemonic.
        For convention's sake, use an upper-case (or numeric) letter.
	@return character to use as mnemonic */
    public abstract char getMnemonic();

    //
    // for clients who need a filetype object
    //

    public static Filetype makeFiletype(String name)
	throws ClassNotFoundException
    {
	try {
	    // get a class, and get its default constructor
	    Constructor cons = Class.forName(name).getConstructor(new Class[] {});

	    // return an instance of this class
	    return (Filetype) cons.newInstance(new Object[] {});

	    // either it's a Filetype that exists, or it's not.
	    // collapse all other exceptions into a CNFE for clients.
	} catch (ClassNotFoundException cnfe) {
	    throw cnfe;
	} catch (Exception e) {
	    throw new ClassNotFoundException();
	}
    }
}
