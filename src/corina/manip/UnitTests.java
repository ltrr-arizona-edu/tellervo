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

package corina.manip;

import corina.Sample;
import corina.Element;

import junit.framework.TestCase;

import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import javax.swing.undo.UndoableEdit;

public class UnitTests extends TestCase {
    public UnitTests(String name) {
        super(name);
    }

    //
    // testing Clean.java
    //
    public void testClean() {
        try {
            // load sample
            Sample s = new Sample("Demo Data/chil/chil001.crn");
            assertTrue(s.count != null);

            // clean it
            UndoableEdit undo = Clean.clean(s);
            assertEquals(s.incr, null);
            assertEquals(s.decr, null);
            assertEquals(s.elements, null);
            assertEquals(s.count, null);
            assertTrue(!s.meta.containsKey("filename"));

            // undo it
            undo.undo();
            assertTrue(s.count != null);
            assertTrue(s.meta.containsKey("filename"));

            // redo it
            undo.redo();
            assertEquals(s.count, null);
            assertTrue(!s.meta.containsKey("filename"));
        } catch (IOException ioe) {
            fail();
        }
    }

    //
    // testing Sum.java
    //
    public void testSum() {
        try {
	    // FIXME: i don't want to use some ill-defined "Demo Data" here
            Element e1 = new Element("Demo Data/chil/chil001.crn");
            Element e2 = new Element("Demo Data/chil/chil002.crn");
            Element e3 = new Element("Demo Data/chil/chil006.crn");
            Element e4 = new Element("Demo Data/chil/chil007.crn");
            List l = new ArrayList();
            l.add(e1);
            l.add(e2);
            l.add(e3);
            l.add(e4);
            Sample m = Sum.sum(l);
        } catch (Exception ioe) {
            fail();
        }
    }
}
