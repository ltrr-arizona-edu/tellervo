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

package corina.util;

import java.util.Comparator;

import java.text.Collator;

/**
   A smart string compare function.  Uses some voodoo to get around
   problems with sorting non-English letters without being
   locale-specific.

   <p>This method solves problems with how Java does both String
   comparison, and Collator-based (locale-specific) string
   comparisons:</p>

   <ul>
     <li>String comparison compares Unicode values, so letters like
     "&Ccedil;" get put after "Z", instead of between "C" and "D"
     where you would expect it.

     <li>The Collator class is used to work around this, but it's
     locale-specific.  For example, if the locale is set to "Turkish",
     "&Ccedil;" will be sorted between "C" and "D".  Unfortunately,
     this is useless if you're trying to sort Turkish names while
     running your program in America, or, worse, trying to sort names
     of different languages, like English and Turkish.
   </ul>

   <p>This sorting scheme ignores case, and sorts first by the letter
   <i>with all accents removed</i>, and then by Unicode value.  This
   puts "&Ccedil;" between "C" and "D", and also is
   locale-agnostic.</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
 */
public class StringComparator implements Comparator {

    /**
       The compare method used by Comparator.  Simply calls
<pre>
   return compare((String) o1, (String) o2);
</pre>

       @param o1 the first string to compare
       @param o2 the second string to compare
       @return the result of their comparison
    */
    public int compare(Object o1, Object o2) {
	return compare((String) o1, (String) o2);
    }

    /**
       Compare two strings using voodoo.

       @param s1 the first string
       @param s2 the second string
       @return -1, 0, or +1, if the first string is less than, equal
       to, or greater than the second string
     */
    public static int compare(String s1, String s2) {
	// PERF: extract this?
	// this collator is a comparator that will ignore case, accents
	Collator collator = Collator.getInstance();
	collator.setStrength(Collator.PRIMARY);

	// BETTER: wrap natural sort around this?

	// ALSO: maybe CollatorKeys would yield better perf.  investigate.

	// task: compare name1, s2
	for (int i=0; i<s1.length(); i++) {
	    // s2 shorter: return +1
	    if (s2.length() <= i)
		return +1;

	    // compare chars s1[i], s2[i]:

	    // first, make them into strings: unfortunately,
	    // collator only compares strings, not chars,
	    // so make some 1-character strings.
	    String str1 = s1.substring(i, i+1);
	    String str2 = s2.substring(i, i+1);

	    // try comparing, ignoring case and accents.
	    // this will put "J" before "K", for instance,
	    // but "C" and "c" and "C," will all sort the same.
	    int test = collator.compare(str1, str2);
	    if (test != 0)
		return test;

	    // now compare again, ignoring case, but not accents.
	    // this will put "C" and "c" together, but separate
	    // out "C,", which will go after all C's.
	    test = str1.compareToIgnoreCase(str2);
	    if (test != 0)
		return test;
	}

	// s2 longer
	if (s2.length() > s1.length())
	    return -1;

	// same
	return 0;
    }
}
