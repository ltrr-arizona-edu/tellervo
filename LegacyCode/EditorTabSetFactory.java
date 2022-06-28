/**
 * 
 */
package org.tellervo.desktop.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.text.*;

/**
A factory for creating fixed tab-stops for JTextPanes. Derived from TabbedLineFactory.

<p>The way it works is: you give it a specification for the lines you'll
be adding, and then use the factory for creating those lines.</p>

<p>When you create a factory, you feed it a specification string.
The specification string consists of percentages, and the four
characters: &lt; ^ &gt; |.  They mean:</p>

<dl>
<dt>30% (or any percentage)</dt>
<dd>Move right by 30% of the width of the page.  If you skip
right by more than 100%, there won't be any error, but you
won't see anything printed off the right edge.  Non-integer
percentages (e.g., <code>"33.3%"</code>) are perfectly legal.</dd>

<dt>&gt;</dt>
<dd>Some left-aligned text goes here.</dd>

<dt>^</dt>
<dd>Some center-aligned text goes here.</dd>

<dt>&lt;</dt>
<dd>Some right-aligned text goes here.</dd>

<dt>|</dt>
<dd>Draw a vertical bar here.  It's the full height of the line,
so if you add multiple lines from this factory in a row, they'll
connect all the way down your table.</dd>
</dl>

<p>(Extra whitespace in the spec string is ignored.)</p>

<p>For example, the spec string <code>"> 50% | 50 <"</code> creates
a table consisting of 2 columns: one left-aligned on the left half
of the page, and one right-aligned on the right half of the page,
with a vertical bar between them.</p>

<p>A fancer example: <code>"10% | 2% > 38% | 2% > 38% | 10%"</code>
makes a 2-column table, with both columns left-aligned, and vertical
bars down the middle and on both sides.  (The extra 2% is there to
prevent the text from running into the lines.)</p>

<p>Once you've created a factory, how do you use it?  Simple!  Just
call the <code>makeLine()</code> method, passing it a string with
all of your table entries, separated by tab characters
(<code>'\t'</code>).  To use the last example above:</p>

<pre>
// print a table of the numbers 1-5, along with their squares
TabbedLineFactory f = new TabbedLineFactory("10% | 2% > 38% | 2% > 38% | 10%");
lines.add(f.makeLine("n \t n squared"));
for (int i=1; i<=5; i++)
&nbsp;&nbsp;&nbsp;lines.add(f.makeLine(i + "\t" + i*i));
</pre>

 * @author Lucas Madar
 *
 */
public class EditorTabSetFactory {	
	private static final String BAR = "|";
	private static final String LEFT = ">";
	private static final String CENTER = "^";
	private static final String RIGHT = "<";
	private static final String DECIMAL = "*";

	/** Create a tabbed-line factory, given a line specification (see
	 above).
	 @param spec the line specification */
	@SuppressWarnings("unchecked")
	public static TabSet buildTabset(String spec, int width) {
		// parse spec
		StringTokenizer tok = new StringTokenizer(spec, " *<>^|", true);
		int n = tok.countTokens();
		List tabs = new ArrayList();
		int mode = TabStop.ALIGN_LEFT;
		float pos = 0f;

		for (int i = 0; i < n; i++) {
			String t = tok.nextToken();

			// skip whitespace (but i need to watch for it,
			// because it's a delimiter)
			if (t.trim().length() == 0)
				continue;

			// store |<>^ and %ages
			if (t.equals(BAR))
				mode = TabStop.ALIGN_BAR;
			else if (t.equals(LEFT))
				mode = TabStop.ALIGN_LEFT;
			else if (t.equals(CENTER))
				mode = TabStop.ALIGN_CENTER;
			else if (t.equals(RIGHT))
				mode = TabStop.ALIGN_RIGHT;
			else if (t.equals(DECIMAL))
				mode = TabStop.ALIGN_DECIMAL;
			else if (t.endsWith("%")) {
				Float value = new Float(t.substring(0, t.length() - 1));
				pos += value.floatValue();
				int x = (int) (width * (pos / 100f));
				
				tabs.add(new TabStop(x, mode, TabStop.LEAD_NONE));
			} else
				throw new IllegalArgumentException();
		}
		
		return new TabSet((TabStop[]) tabs.toArray(new TabStop[tabs.size()]));
	}
}
