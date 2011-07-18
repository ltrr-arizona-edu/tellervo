/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina;

import edu.cornell.dendro.corina.cross.Cross;
import edu.cornell.dendro.corina.cross.Histogram;
import edu.cornell.dendro.corina.cross.Trend;
import edu.cornell.dendro.corina.sample.FileElement;
import edu.cornell.dendro.corina.sample.Sample;

import junit.framework.TestCase;

import java.util.StringTokenizer;

import java.text.DecimalFormat;

public class CrossdatingTests extends TestCase {
    public CrossdatingTests(String name) {
        super(name);
    }

    // testing ???
    // WRITE ME: test the crosses themselves!

    // testing corina.cross.overlap
    public void testOverlap() {
        try {
            // try an unreasonably big overlap for some medium-length samples
            System.setProperty("corina.cross.overlap", "500");
            Sample s1 = new FileElement("Demo Data/chil/chil001.crn").load(); // these are <500 years long
            Sample s2 = new FileElement("Demo Data/chil/chil002.crn").load();
            Cross c = new Trend(s1, s2);
            c.run();
            fail();
        } catch (IllegalArgumentException e) {
	    // success
        } catch (Exception e) {
	    fail();
	}
    }

    // testing histogram
    public void testHistogram() {
        // make up a bunch of data
        int n = 1000;
        float x[] = new float[n];
        for (int i=0; i<n; i++)
            x[i] = (float) Math.random();
        x[345] = Float.NaN; // give it something screwy to choke on
	x[0] = Float.POSITIVE_INFINITY; // really screwy!

        // make a histogram of it
	char dot = new DecimalFormat().getDecimalFormatSymbols().getDecimalSeparator();
        String fmt = "#" + dot + "##";
        Histogram h = new Histogram(x, fmt);

        // test countbuckets
        int buckets = h.getNumberOfBuckets();
        assertTrue(buckets > 0);
        assertTrue(buckets < 1000);

        // test getfullestbucket
        int fullest = h.getFullestBucket();
        boolean found = false;
        for (int i=0; i<buckets; i++) {
            int num = h.getBucketItems(i);
            if (num == fullest)
                found = true;
            assertTrue(num <= fullest);
        }
        assertTrue(found);

        // test getnumber
        int total = 0;
        for (int i=0; i<buckets; i++) {
            total += h.getBucketItems(i);
        }
        // make sure sum(number) = number of data i started with
        assertEquals(total, n);

        // test for out-of-bounds
        try {
            h.getBucketRange(buckets);
            fail();
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // succeed
        }
        try {
            h.getBucketItems(buckets);
            fail();
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // succeed
        }

        // test getRange()
        String range = h.getBucketRange(0);
        StringTokenizer tok = new StringTokenizer(range, " ");
        try {
            Float.parseFloat(tok.nextToken());
            tok.nextToken();
            Float.parseFloat(tok.nextToken());
        } catch (NumberFormatException nfe) {
            fail();
        }
    }
}
