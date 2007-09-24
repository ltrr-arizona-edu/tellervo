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

package edu.cornell.dendro.corina.site;

import junit.framework.TestCase;
import edu.cornell.dendro.corina.core.App;

public class UnitTests extends TestCase {
    public UnitTests(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
      super.setUp();
      if (!App.isInitialized()) App.init(null);
    }

    //
    // testing SiteDB.java
    //
    public void testLock() {
	final String filename = "dummy";

	// acquire lock -- it should be available
	boolean lock = Lock.acquire(filename);

	// try to acquire again -- it should not be available
	boolean lock2 = Lock.acquire(filename);
	assertTrue(!lock2); // note: not-lock2!

	// release it
	Lock.release(filename);

	// acquire it again -- it should be available
	boolean lock3 = Lock.acquire(filename);
	assertTrue(lock3);

	// release it
	Lock.release(filename);
    }

    //
    // testing SiteDB.java
    //
    public void testSiteDB() {
	// violates OAOO: startup.java sets this, too (call startup.initSax()?)
        if (App.prefs.getPref("org.xml.sax.driver") == null)
            App.prefs.setPref("org.xml.sax.driver", "org.apache.crimson.parser.XMLReaderImpl");
        if (App.prefs.getPref("corina.dir.data") == null)
            App.prefs.setPref("corina.dir.data", "Demo Data");
        try {
	    SiteDB db = SiteDB.getSiteDB(); // this is SLOW!
	    assertTrue(db != null);
	    assertTrue(db.sites != null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
