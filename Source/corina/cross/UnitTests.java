package corina.cross;

import junit.framework.TestCase;

import java.util.StringTokenizer;

public class UnitTests extends TestCase {
    public UnitTests(String name) {
        super(name);
    }

    // testing ???
    // WRITE ME: test the crosses themselves!

    // testing histogram
    public void testHistogram() {
        // make up a bunch of data
        int n = 1000;
        double x[] = new double[n];
        for (int i=0; i<n; i++)
            x[i] = Math.random();
        x[345] = Double.NaN; // give it something screwy to choke on

        // make a histogram of it
        String fmt = "#.##";
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
