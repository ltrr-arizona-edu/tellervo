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

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;

public class UnitTests extends TestCase {
    public UnitTests(String name) {
	super(name);
    }

    //
    // testing Filetype factory
    //
    public void testFactory() {
        try {
            Filetype f = Filetype.makeFiletype("corina.files.Corina");
        } catch (ClassNotFoundException cnfe) {
            fail();
        }
        try {
            Filetype f = Filetype.makeFiletype("some.bogus.filetype");
            fail();
        } catch (ClassNotFoundException cnfe) {
            // succeed
        }
    }

    static final String TMP = System.getProperty("java.io.tmpdir") + File.separator;

    //
    // test any filetype
    //
    private void testFiletype(String filetype) {
        try {
            // load a sample
            Sample s = new Sample("Demo Data/chil/chil001.crn");

            // save it as |filetype| in /tmp
            Filetype f=null;
            try {
                f = Filetype.makeFiletype(filetype);
            } catch (ClassNotFoundException cnfe) {
                fail();
            }
            f.save(TMP + "unittest.tmp", s);

            // load it again
            Sample s2 = new Sample(TMP + "unittest.tmp");

            // are they the same data?  count?
            assertTrue(s.data.equals(s2.data));
            if (s.count != null)
                assertTrue(s.count.equals(s2.count));
            if (s.elements != null)
                assertTrue(s.elements.equals(s2.elements));

            // problem here: metadata aren't *exactly* the same every
            // time.  the filename is /tmp/ instead of ../Demo Data/,
            // for example.  should i check all of meta except
            // filename?  (and author?)

            // (clean up when you're done)
            new File(TMP + "unittest.tmp").delete();
        } catch (IOException ioe) {
            fail();
        }
    }

    //
    // test Tucson.java
    //
    public void testTucson() {
        testFiletype("corina.files.Tucson");
    }

    //
    // test Corina.java
    //
    public void testCorina() {
        testFiletype("corina.files.Corina");
    }

    //
    // test TwoColumn.java
    //
    public void test2col() {
        testFiletype("corina.files.TwoColumn");
    }

    //
    // test Heidelberg.java
    //
    public void testHeidelberg() {
        testFiletype("corina.files.Heidelberg");
    }

    // left to add, load+save: Heidelberg, Hohenheim, TSAPMatrix
    // left to add, save-only: Epson, HTML (?)
}
