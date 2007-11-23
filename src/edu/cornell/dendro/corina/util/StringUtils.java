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

package edu.cornell.dendro.corina.util;

import java.util.StringTokenizer;
import java.io.UnsupportedEncodingException;


/**
   Some handy string utilities.

   <h2>Left to do</h2>
   <ul>
     <li>splitBy() would be shorter/simpler if it used a tokenizer
         (see StringTokenizer, countTokens(), nextToken())

     <li>splitBy() might be exactly the same as
         browser/SearchField.parseIntoWords() -- check it out
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class StringUtils {
    // don't instantiate me
    private StringUtils() { }

    /**
       Pad some text on the left (i.e., right-align it) until it's a
       specified width.  If the text is already longer than the
       desired length, it is returned.

       @param text string to pad
       @param size length of resulting string
       @return the original text, padded on the left
    */
    public static String leftPad(String text, int size) {
	int numSpaces = size - text.length();
	if (numSpaces <= 0)
	    return text;

	StringBuffer buf = new StringBuffer(size);

	for (int i=0; i<numSpaces; i++)
	    buf.append(' ');
	for (int i=numSpaces; i<size; i++)
	    buf.append(text.charAt(i-numSpaces));

	return buf.toString();
    }

    /**
       Split some text into lines.  For example,
       <code>"a\nb\nc"</code> becomes <code>String[]
       {"a","b","c"}</code>.

       @param text the text, separated by <code>'\n'</code> chars
       @return the string, as an array of strings
    */
    public static String[] splitByLines(String text) {
	return splitBy(text, '\n');
    }

    /**
       Split some text, using an arbitrary separator character.

       @param text the text
       @param sep the separator character to watch for
       @return the string, as an array of strings
    */
    public static String[] splitBy(String text, char sep) {
	// count separators
	int newlines = 0;
	for (int i=0; i<text.length(); i++)
	    if (text.charAt(i) == sep)
		newlines++;

	// allocate space for output
	String result[] = new String[newlines + 1];
	int n = 0; // next space to fill

	// create output
	int start = 0;
	for (;;) {
	    int newline = text.indexOf(sep, start);
	    if (newline == -1) {
		result[n++] = text.substring(start); // (to end)
		break;
	    } else {
		result[n++] = text.substring(start, newline);
		start = newline + 1;
	    }
	}

	// return it
	return result;
    }
    
    /**
        Convert a sequence of integers in a string into an array
        of ints.

        <p>For example, extractInts("1 2 3") = int[] { 1, 2, 3 }.</p>

        <p>Bug: what happens if the string isn't parseable?</p>

        @param s the string to parse
        @return an array of ints, representing the string
    */
    public static int[] extractInts(String s) {
        StringTokenizer tok = new StringTokenizer(s, " ");
        int n = tok.countTokens();
        int r[] = new int[n];
        for (int i=0; i<n; i++)
            r[i] = Integer.parseInt(tok.nextToken());
        return r;
    }

    /**
       Given a string, escape any &lt; &gt; &amp; ' " characters for
       XML.  Also, if any characters are unprintable, they're escaped
       as raw values (&amp;#xxxx;), so loading the output in any old
       text editor shouldn't mangle anything.

       @param input a string
       @return the same string, with &lt;/&gt;/&amp; escaped
    */
    public static String escapeForXML(String input) {
	// FIXME: if there are no <>& symbols, just return the string
	// as-is to save the GC

	// MAYBE: does SAX or somebody already have a method that does
	// this better?

	// MAYBE: use regexps in 1.4?

	// BETTER: isn't there a String.replace() or something that
	// would do this in about 2 lines?
    
	StringBuffer output = new StringBuffer();
	for (int i=0; i<input.length(); i++) {
	    char c = input.charAt(i);
	    switch (c) {
	    case '&':
		output.append("&amp;");
		break;
	    case '<':
		output.append("&lt;");
		break;
	    case '>':
		output.append("&gt;");
		break;
	    case '\"':
		output.append("&quot;");
		break;
	    case '\'':
		output.append("&apos;");
		break;
	    default:
		if (Character.isISOControl(c)) {
		    // if it's not printable, &#x-escape it.

		    // (this came up when trying to save
		    // "<dating>^D</dating>" which shouldn't happen,
		    // anyway, but better safe than sorry.)

		    // BUG: aren't there non-iso-control characters
		    // which won't show up correctly?  do they need
		    // the right encoding=""?  do they have it?

			if(c < 32)
				output.append("ILLEGAL-XML-CHAR:");
			else
				output.append("&#x");

		    String hex = Integer.toHexString((int) c);
		    for (int ii=0; ii<4-hex.length(); ii++)
			output.append("0");
		    output.append(hex);

		    output.append(";");
		} else {
		    output.append(c);
		}
	    }
	}
	return output.toString();
    }


    /** 
     escapeForCSV
      
     given a string, replace any occurances of " with double "'s, and return it surrounded in "'s
      
     @param input a string
     @return the same string, with "'s escaped
    */

    public static String escapeForCSV(String input) {
    	StringBuffer output = new StringBuffer();

    	output.append("\"");
    	
    	for (int i=0; i<input.length(); i++) {
    		char c = input.charAt(i);
    		output.append(c);
    		if(c == '"')
    			output.append(c);
    	}
    	
    	output.append("\"");
    	
    	return output.toString();
    }

    
    /**
       In a string, replace one substring with another.

       <p>If str contains source, return a new string with (the first
       occurrence of) source replaced by target.</p>

       <p>If str doesn't contain source (or str is the empty string),
       returns str.</p>

       <p>Think: str ~= s/source/target/</p>

       <p>This is like Java 1.4's String.replaceFirst() method; when I
       decide to drop support for Java 1.3, I can use that method
       instead.</p>

       @param str the base string
       @param source the substring to look for
       @param target the replacement string to use
       @return the original string, str, with its first instance of
       source replaced by target
    */
    public static String substitute(String str, String source, String target) {
        int index = str.indexOf(source);
        if (index == -1) // not present
            return str;
        int start = index, end = index + source.length();
        return str.substring(0, start) + target + str.substring(end);
    }
    
    private static final char[] hexChars ={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
    public static String bytesToHex(byte[] in) {
    	StringBuffer hex = new StringBuffer();
    	
    	for(int i = 0; i < in.length; i++) {
    		int msb = (in[i] & 0xFF) / 16;
    		int lsb = (in[i] & 0xFF) % 16;
    		
    		hex.append(hexChars[msb]);
    		hex.append(hexChars[lsb]);
    	}
    	
    	return hex.toString();
    }
}
