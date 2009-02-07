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

package edu.cornell.dendro.corina.manip;

import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementFactory;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.FileElement;
import edu.cornell.dendro.corina.sample.Sample;

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
			Sample s = new FileElement("Demo Data/chil/chil001.crn").load();
			assertTrue(s.getCount() != null);

			// clean it
			UndoableEdit undo = Clean.clean(s);
			assertEquals(s.getWJIncr(), null);
			assertEquals(s.getWJDecr(), null);
			assertEquals(s.getElements(), null);
			assertEquals(s.getCount(), null);
			assertTrue(!s.hasMeta("filename"));

			// undo it
			undo.undo();
			assertTrue(s.getCount() != null);
			assertTrue(s.hasMeta("filename"));

			// redo it
			undo.redo();
			assertEquals(s.getCount(), null);
			assertTrue(!s.hasMeta("filename"));
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
			Element e1 = ElementFactory.createElement("Demo Data/chil/chil001.crn");
			Element e2 = ElementFactory.createElement("Demo Data/chil/chil002.crn");
			Element e3 = ElementFactory.createElement("Demo Data/chil/chil006.crn");
			Element e4 = ElementFactory.createElement("Demo Data/chil/chil007.crn");
			ElementList l = new ElementList();
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