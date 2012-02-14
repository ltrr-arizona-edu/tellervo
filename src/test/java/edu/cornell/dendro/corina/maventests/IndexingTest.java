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
package edu.cornell.dendro.corina.maventests;

import junit.framework.TestCase;

import java.util.List;
import java.util.ArrayList;
import edu.cornell.dendro.corina_indexing.*;

public class IndexingTest extends TestCase {
    public IndexingTest(String name) {
        super(name);
    }

    //
    // testing High-pass filter
    //
	public void testFilter() {
        // make new random list
        List<Integer> l = new ArrayList<Integer>();
        for (int i=0; i<50; i++)
            l.add(new Integer((int) (Math.random() * 100)));

        // filter with {1}, get the same thing
        List<?> o = HighPass.filter(l, new int[] { 1 });
        assertEquals(l.size(), o.size());
        for (int i=0; i<50; i++)
            assertEquals(((Number) l.get(i)).intValue(), ((Number) o.get(i)).intValue());

        // filter with {0}, get 0
        List<?> o2 = HighPass.filter(l, new int[] { 0 });
        assertEquals(l.size(), o2.size());
        for (int i=0; i<50; i++)
            assertEquals(((Number) o2.get(i)).intValue(), 0);

        // filter with {1,1,1}, check averages
        List<?> o3 = HighPass.filter(l, new int[] { 1, 1, 1 });
        assertEquals(l.size(), o3.size());
        for (int i=1; i<49; i++) {
            int in1 = ((Number) l.get(i-1)).intValue();
            int in2 = ((Number) l.get(i)).intValue();
            int in3 = ((Number) l.get(i+1)).intValue();
            assertEquals(((Number) o3.get(i)).intValue(), (in1+in2+in3)/3);
        }
    }
}
