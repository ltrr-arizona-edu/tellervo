package edu.cornell.dendro.corina.index;

import junit.framework.TestCase;

import java.util.List;
import java.util.ArrayList;

public class UnitTests extends TestCase {
    public UnitTests(String name) {
        super(name);
    }

    //
    // testing High-pass filter
    //
    public void testFilter() {
        // make new random list
        List l = new ArrayList();
        for (int i=0; i<50; i++)
            l.add(new Integer((int) (Math.random() * 100)));

        // filter with {1}, get the same thing
        List o = HighPass.filter(l, new int[] { 1 });
        assertEquals(l.size(), o.size());
        for (int i=0; i<50; i++)
            assertEquals(((Number) l.get(i)).intValue(), ((Number) o.get(i)).intValue());

        // filter with {0}, get 0
        List o2 = HighPass.filter(l, new int[] { 0 });
        assertEquals(l.size(), o2.size());
        for (int i=0; i<50; i++)
            assertEquals(((Number) o2.get(i)).intValue(), 0);

        // filter with {1,1,1}, check averages
        List o3 = HighPass.filter(l, new int[] { 1, 1, 1 });
        assertEquals(l.size(), o3.size());
        for (int i=1; i<49; i++) {
            int in1 = ((Number) l.get(i-1)).intValue();
            int in2 = ((Number) l.get(i)).intValue();
            int in3 = ((Number) l.get(i+1)).intValue();
            assertEquals(((Number) o3.get(i)).intValue(), (in1+in2+in3)/3);
        }
    }
}
