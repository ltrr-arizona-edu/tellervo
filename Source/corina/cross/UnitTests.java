package corina.cross;

import junit.framework.TestCase;

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

        // stuff i don't really test here: getRange()
    }
}
