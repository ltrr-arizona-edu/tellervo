package corina.util;

import java.util.Comparator;

/**
  Natural-order sorting, so numbers like "17" and "7" are put in the
  proper order.

  <p>This is a Java port of "strnatcmp.c".  This file was based on the
  work of Martin Pool, who generously made his program open-source,
  but any bugs in this version are entirely my fault.  See <a
  href="http://sourcefrog.net/projects/natsort/index.html">his natsort
  page</a> for more info.</p>

  <p>Usage:</p>

<pre>
  NaturalSort.compare("Photo 7.jpeg", "Photo 17.jpeg")
  => -1
</pre>
<pre>
  NaturalSort.compareIgnoreCase("Photo 7.jpeg", "photo 17.jpeg"); // ignore case
  => -1
</pre>
<pre>
  Collections.sort(myList, new NaturalSort.NaturalComparator()); // a list
</pre>
<pre>
  Collections.sort(myList, new NaturalSort.CINaturalComparator()); // a list, ignore case
</pre>

  <p>His original version, in C, is http://sourcefrog.net/projects/natsort/natsort.c</p>

  <p>The license for that is the <a
  href="http://www.gzip.org/zlib/zlib_license.html">Zlib license</a>:</p>

<blockquote>
  <p>This software is provided 'as-is', without any express or implied
  warranty.  In no event will the authors be held liable for any damages
  arising from the use of this software.</p>

  <p>Permission is granted to anyone to use this software for any purpose,
  including commercial applications, and to alter it and redistribute it
  freely, subject to the following restrictions:</p>

  <ol>
  <li>The origin of this software must not be misrepresented; you must not
     claim that you wrote the original software. If you use this software
     in a product, an acknowledgment in the product documentation would be
     appreciated but is not required.
  <li>Altered source versions must be plainly marked as such, and must not be
     misrepresented as being the original software.
  <li>This notice may not be removed or altered from any source distribution.
  </ol>
</blockquote>

  <p>I believe these terms mean it's ok to port it to Java and include
  it in my GNU GPL-licensed program.</p>

  22 January 2003

  @author Ken Harris <kbh7@cornell.edu>
*/
public class NaturalSort {
    // don't instantiate me
    private NaturalSort() { }

    /* The longest run of digits wins.  That aside, the greatest
       value wins, but we can't know that it will until we've scanned
       both numbers to know that they have the same magnitude, so we
       remember it in BIAS. */
    private static int compareRight(String a, int ai, String b, int bi) {
	int bias = 0;

	for (;; ai++, bi++) {
	    char ca = (a.length()==ai ? '\0' : a.charAt(ai));
	    char cb = (b.length()==bi ? '\0' : b.charAt(bi));

	    if (!Character.isDigit(ca)  &&  !Character.isDigit(cb))
		return bias;
	    else if (!Character.isDigit(ca))
		return -1;
	    else if (!Character.isDigit(cb))
		return +1;
	    else if (ca < cb) {
		if (bias != 0)
		    bias = -1;
	    } else if (ca > cb) {
		if (bias != 0)
		    bias = +1;
	    } else if (ai==a.length()  &&  bi==b.length())
		return bias;
	}
    }

    /* Compare two left-aligned numbers: the first to have a
       different value wins. */
    private static int compareLeft(String a, int ai, String b, int bi) {
	for (;; ai++, bi++) {
	    char ca = (a.length()==ai ? '\0' : a.charAt(ai));
	    char cb = (b.length()==bi ? '\0' : b.charAt(bi));

	    if (!Character.isDigit(ca)  &&  !Character.isDigit(cb))
		return 0;
	    else if (!Character.isDigit(ca))
		return -1;
	    else if (!Character.isDigit(cb))
		return +1;
	    else if (ca < cb)
		return -1;
	    else if (ca > cb)
		return +1;
	}
    }

    private static int strnatcmp0(String a, String b, boolean foldCase) {
	int ai, bi;
	char ca, cb;
	boolean fractional;
	int result;
     
	ai = bi = 0;
	while (true) {
	    ca = (ai == a.length() ? '\0' : a.charAt(ai));
	    cb = (bi == b.length() ? '\0' : b.charAt(bi));

	    /* skip over leading spaces or zeros */
	    while (Character.isWhitespace(ca)) {
		ai++;
		ca = (ai == a.length() ? '\0' : a.charAt(ai));
	    }

	    while (Character.isWhitespace(cb)) {
		bi++;
		cb = (bi == b.length() ? '\0' : b.charAt(bi));
	    }

	    /* process run of digits */
	    if (Character.isDigit(ca)  &&  Character.isDigit(cb)) {
		fractional = (ca == '0' || cb == '0');

		if (fractional) {
		    if ((result = compareLeft(a, ai, b, bi)) != 0)
			return result;
		} else {
		    if ((result = compareRight(a, ai, b, bi)) != 0)
			return result;
		}
	    }

	    if (ca=='\0' && cb=='\0') {
		/* The strings compare the same.  Perhaps the caller
		   will want to call strcmp to break the tie. */
		return 0;
	    }

	    if (foldCase) {
		ca = Character.toUpperCase(ca);
		cb = Character.toUpperCase(cb);
	    }
	  
	    if (ca < cb)
		return -1;
	    else if (ca > cb)
		return +1;

	    ++ai; ++bi;
	}
    }

    /** Compare two strings using natural ordering; case-sensitive.
	@param a the first string
	@param b the second string
	@return -1, 0, or +1, depending on whether a is less than,
	equal to, or greater than b */
    public static int compare(String a, String b) {
	return strnatcmp0(a, b, false);
    }

    /** Compare two strings using natural ordering; case-insensitive.
	@param a the first string
	@param b the second string
	@return -1, 0, or +1, depending on whether a is less than,
	equal to, or greater than b */
    public static int compareIgnoreCase(String a, String b) {
	return strnatcmp0(a, b, true);
    }

    /** A comparator class, using natural ordering. */
    public static class NaturalComparator implements Comparator {
	public int compare(Object o1, Object o2) {
	    return NaturalSort.compare((String) o1, (String) o2);
	}
    }

    /** A comparator class, using case-insensitive natural ordering. */
    public static class CINaturalComparator implements Comparator {
	public int compare(Object o1, Object o2) {
	    return NaturalSort.compareIgnoreCase((String) o1, (String) o2);
	}
    }
}
