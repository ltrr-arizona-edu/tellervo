package corina.cross;

import corina.Sample;

import junit.framework.TestCase;

import java.util.StringTokenizer;

import java.text.DecimalFormat;

public class UnitTests extends TestCase {
    public UnitTests(String name) {
        super(name);
    }

    // testing ???
    // WRITE ME: test the crosses themselves!

    // testing corina.cross.overlap
    public void testOverlap() {
        try {
            // try an unreasonably big overlap for some medium-length samples
            System.setProperty("corina.cross.overlap", "500");
            Sample s1 = new Sample("Demo Data/chil/chil001.crn"); // these are <500 years long
            Sample s2 = new Sample("Demo Data/chil/chil002.crn");
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
        double x[] = new double[n];
        for (int i=0; i<n; i++)
            x[i] = Math.random();
        x[345] = Double.NaN; // give it something screwy to choke on

        // make a histogram of it
	char dot = new DecimalFormat().getDecimalFormatSymbols().getDecimalSeparator();
        String fmt = "#" + dot + "##";
        Histogram h = new Histogram(x, fmt);

        // test countbuckets
        int buckets = h.countBuckets();
        assertTrue(buckets > 0);
        assertTrue(buckets < 1000);

        // test getfullestbucket
        int fullest = h.getFullestBucket();
        boolean found = false;
        for (int i=0; i<buckets; i++) {
            int num = h.getNumber(i);
            if (num == fullest)
                found = true;
            assertTrue(num <= fullest);
        }
        assertTrue(found);

        // test getnumber
        int total = 0;
        for (int i=0; i<buckets; i++) {
            total += h.getNumber(i);
        }
        // make sure sum(number) = number of data i started with
        assertEquals(total, n);

        // test for out-of-bounds
        try {
            h.getRange(buckets);
            fail();
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // succeed
        }
        try {
            h.getNumber(buckets);
            fail();
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // succeed
        }

        // test getRange()
        String range = h.getRange(0);
        StringTokenizer tok = new StringTokenizer(range, " ");
        try {
            Double.parseDouble(tok.nextToken());
            tok.nextToken();
            Double.parseDouble(tok.nextToken());
        } catch (NumberFormatException nfe) {
            fail();
        }
    }
}
