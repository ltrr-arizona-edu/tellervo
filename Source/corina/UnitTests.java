package corina;

import junit.framework.TestCase;

import java.util.List;

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
    }
    public void testYearDiff() {
        int d = new Year(5).diff(new Year(2));
        assertEquals(d, 3);
        d = new Year(1).diff(new Year(1));
	assertEquals(d, 0);
    }
    public void testYearCompare() {
        int lt = new Year(5).compareTo(new Year(10));
	assert(lt < 0);
	int gt = new Year(5).compareTo(new Year(-5));
        assert(gt > 0);
	int eq = new Year(-1).compareTo(new Year(-1));
        assert(eq == 0);
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
    // testing Metadata.java
    //
    public void testMetadataLoad() {
	assert(Metadata.fields != null && Metadata.fields.length > 0);
	assert(Metadata.preview != null && Metadata.preview.length > 0);
    }
}
