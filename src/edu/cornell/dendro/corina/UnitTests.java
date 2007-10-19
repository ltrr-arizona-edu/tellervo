package edu.cornell.dendro.corina;

import junit.framework.TestCase;

import java.util.List;
import java.util.Iterator;

import edu.cornell.dendro.corina.metadata.MetadataTemplate;

public class UnitTests extends TestCase {
    public UnitTests(String name) {
        super(name);
    }

    //
    // testing Year.java
    //
    public void testYearDefault() {
        Year y = new Year();
        assertEquals(y.toString(), "1001"); // was: y.intValue(), 1001
    }
    public void testYearAdd() {
        Year y = new Year(-1).add(1);
        assertEquals(y.toString(), "1");
        y = new Year(2).add(-4);
        assertEquals(y.toString(), "-3");
        y = new Year(-2).add(1); // oops, my original algorithm didn't handle this (!)
        assertEquals(y.toString(), "-1");
    }
    public void testYearDiff() {
        int d = new Year(5).diff(new Year(2));
        assertEquals(3, d);
        d = new Year(1).diff(new Year(1));
        assertEquals(0, d);
        d = new Year(0).diff(new Year(0));
        assertEquals(0, d);

        d = new Year(-2).diff(new Year(-4));
        assertEquals(2, d);
        d = new Year(-4).diff(new Year(-2));
        assertEquals(-2, d);
        
        d = new Year(5).diff(new Year(-4));
        assertEquals(8, d);
        d = new Year(-4).diff(new Year(5));
        assertEquals(-8, d);
    }
    public void testYearCompare() {
        int lt = new Year(5).compareTo(new Year(10));
        assertTrue(lt < 0);
        int gt = new Year(5).compareTo(new Year(-5));
        assertTrue(gt > 0);
        int eq = new Year(-1).compareTo(new Year(-1));
        assertTrue(eq == 0);
    }

    //
    // testing Range.java
    //
    public void testRangeString() {
        Range r = new Range(" -5 - 5");
        assertEquals(r.getStart().toString(), "-5"); // was: .intValue(), -5
        assertEquals(r.getEnd().toString(), "5"); // was: .intValue(), 5
    }
    public void testRangeSpan() {
        Range r = new Range("1 - 5");
        assertEquals(r.span(), 5);
        r = new Range("-1 - 1");
        assertEquals(r.span(), 2);
    }
    public void testRangeOverlap() {
        Range r1 = new Range("1 - 5");
        { // full overlap
            Range r2 = new Range("1 - 5");
            assertEquals(r1.overlap(r2), 5);
        }
        { // zero overlap
            Range r2 = new Range("10 - 50");
            assertEquals(r1.overlap(r2), 0);
        }
        { // partial overlap
            Range r2 = new Range("3 - 10");
            assertEquals(r1.overlap(r2), 3);
        }
    }
    public void testRangeContains() {
        Year y = new Year(50);
        assertEquals(new Range("1 - 5").contains(y),	false);
        assertEquals(new Range("99 - 100").contains(y), false);
        assertEquals(new Range("1 - 49").contains(y),	false);
        assertEquals(new Range("1 - 50").contains(y),	true);
        assertEquals(new Range("1 - 100").contains(y),	true);
        assertEquals(new Range("50 - 100").contains(y), true);
    }

    //
    // testing MetadataTemplate.java
    //
    public void testMetadataLoad() {
	Iterator i = MetadataTemplate.getFields();
	assertTrue(i != null);
	int n = 0;
	while (i.hasNext()) {
	    assertTrue(i.next() != null);
	    n++;
	}
	assertTrue(n >= 1);
    }
}
